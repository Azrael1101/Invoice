package tw.com.tm.erp.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title : String 工具
 * </p>
 * 
 * @author shan
 * @version 1.0
 */

public class StringTools extends StringUtils {
	private static final int BYTE_MASK = 0xFF;

	public static final int HEX_WIDTH = 32;

	// 內定的null string
	public static final String DEFAULTSTRINGNULL = "nulls"; // 20061124 shan
	// change null =>
	// nulls

	// 內定的null int
	public static final int DEFAULTINT = 0; // 20061123 shan change -1 => 0

	// 內定的null float
	public static final int DEFAULTFLOAT = 0;

	// 自動補空白 StringUtils.leftPad

	public static String dumpByteAsHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 6);
		for (int i = 0; i < bytes.length; i += HEX_WIDTH) {
			for (int j = 0; j < HEX_WIDTH; j++) {
				if (i + j >= bytes.length) {
					if (bytes.length > HEX_WIDTH) {
						sb.append("   ");
					}
					continue;
				}
				String s = Integer.toHexString(bytes[i + j] & BYTE_MASK);
				if (s.length() < 2) {
					sb.append('0');
				}
				sb.append(s);
				sb.append(' ');
			}

			for (int j = 0; j < HEX_WIDTH; j++) {
				if (i + j >= bytes.length) {
					if (bytes.length > HEX_WIDTH) {
						sb.append(' ');
					}
					continue;
				}
				char ch = (char) (bytes[i + j] & BYTE_MASK);
				if (ch >= ' ' && ch < 127) {
					sb.append(ch);
				} else {
					sb.append(' ');
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public static boolean isUTF8(byte[] data) {
		int count_good_utf = 0;
		int count_bad_utf = 0;
		byte current_byte = 0x00;
		byte previous_byte = 0x00;
		for (int i = 1; i < data.length; i++) {
			current_byte = data[i]; // 不確定是否正確
			previous_byte = data[i - 1];
			if ((current_byte & 0xC0) == 0x80) {
				if ((previous_byte & 0xC0) == 0xC0) {
					count_good_utf++;
				} else if ((previous_byte & 0x80) == 0x00) {
					count_bad_utf++;
				}
			} else if ((previous_byte & 0xC0) == 0xC0) {
				count_bad_utf++;
			}
		}
		// System.out.println(count_good_utf);
		// System.out.println(count_bad_utf);
		if (count_good_utf > count_bad_utf) {
			return true;
		} else {
			return false;
		}
	}

	// unicode transform to big5
	public String unicodeToBig5(String str) {
		str = str.replaceAll("\\u005cu", "");
		byte[] tmpStr = new byte[str.length() / 2];
		for (int i = 0; i < tmpStr.length; i++) {
			tmpStr[i] = Integer.valueOf(str.substring(2 * i, 2 * (i + 1)), 16).byteValue();
		}

		try {
			return new String(tmpStr, "UTF-16");
		} catch (Exception ignored) {
			ignored.printStackTrace(System.out);
		}
		return "";
	}

	// big5 transform to unicode
	public String changeToUni(String str) {
		System.getProperty("");
		int countChar = str.length();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < countChar; i++) {
				// don't transform '\r' , '\n'
				if (str.substring(i, i + 1).equals("\r") || str.substring(i, i + 1).equals("\n")) {
					sb.append("\n");
				} else {
					// encode by using utf-16
					byte[] uni_str = str.substring(i, i + 1).getBytes("utf-16");
					sb.append("\\u");
					// start to transform to unicode
					for (int j = 2; j < uni_str.length; j++) {
						int tmp = uni_str[j];
						if (tmp < 0) {
							tmp = 256 + tmp;
							if (tmp < 16) {
								sb.append("0" + Integer.toHexString(tmp));
							} else {
								sb.append(Integer.toHexString(tmp));
							}
						} else if (tmp < 16) {
							sb.append("0" + Integer.toHexString(tmp));
						} else {
							sb.append(Integer.toHexString(tmp));
						}
					}
				}
			}
			return sb.toString();
		} catch (Exception e1) {
			e1.printStackTrace(System.out);
		}
		return "";
	}

	/*
	 * public static String getSystemEncode() { return
	 * System.getProperty("file.encoding"); }
	 */

	public static Vector<String> SubStrings(String Source, int size) {
		Vector<String> records = new Vector();
		if (Source != null) {
			int length = Source.length();
			for (int i = 0; i < length / size; i++) {
				records.add(Source.substring(0, size));
				Source = Source.substring(size, Source.length());
			}
			if (Source.length() > 0) {
				records.add(Source);
			}
		}
		return records;
	}

	public static String TrimString(String Str) {
		return TrimString(Str, "");
	}

	public static String TrimString(String Str, String rStr) {
		try {
			return ((null == Str) || (Str.equals(""))) ? rStr : Str;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getNULLStr(String nullstring) {
		if (nullstring == null) {
			return DEFAULTSTRINGNULL;
		} else {
			return nullstring.trim();
		}
	}

	public static String StringDelete(String Source, String Start, String End) {
		StringBuffer sb = new StringBuffer();
		while (Source.indexOf(Start) >= 0) {
			int s = Source.indexOf(Start);
			int e = Source.indexOf(End, Source.indexOf(Start)) + End.length() + 1;
			sb.append(Source.substring(0, s));
			Source = Source.substring(e, Source.length());
		}
		sb.append(Source);
		return sb.toString();
	}

	public static String StringDelete(String Source, String DelStr) {
		StringBuffer sb = new StringBuffer();
		while (Source.indexOf(DelStr) >= 0) {
			sb.append(Source.substring(0, Source.indexOf(DelStr)));
			Source = Source.substring(Source.indexOf(DelStr) + DelStr.length(), Source.length());
		}
		sb.append(Source);
		return sb.toString();
	}

	// 把inputstream 轉成 string
	public static String InputToString(InputStream in) {
		try {
			StringBuffer sb = new StringBuffer();
			byte b[] = new byte[4096];
			int a = 0;
			while ((a = in.read(b, 0, b.length)) != -1) {
				sb.append(new String(b, 0, a));
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {
			return "Error in InputToString";
		}
	}

	public static String StringReplaceAll(String Source, String start, String end, Properties replaces, String defaultNull) {
		StringBuffer sb = new StringBuffer();
		if (defaultNull == null) {
			defaultNull = DEFAULTSTRINGNULL;
		}
		while (Source.indexOf(start) >= 0) {
			sb.setLength(0);
			String proName = Source.substring(Source.indexOf(start) + start.length(), Source.indexOf(end, Source.indexOf(start)));
			String proValue = replaces.getProperty(proName);
			sb.append(Source.substring(0, Source.indexOf(start)));
			if ((proValue == null) || (proValue.equals(""))) {
				proValue = defaultNull;
			}
			sb.append(proValue);
			sb.append(Source.substring(Source.indexOf(end, Source.indexOf(start)) + end.length(), Source.length()));
			Source = sb.toString();
		}
		return sb.toString();
	}

	public static String StringReplaceAll(String Source, Properties replacments) {
		Enumeration regexs = replacments.keys();
		while (regexs.hasMoreElements()) {
			StringBuffer sb = new StringBuffer();
			String regex = (String) regexs.nextElement();
			String replacement = replacments.getProperty(regex);
			while (Source.indexOf(regex) >= 0) {
				sb.append(Source.substring(0, Source.indexOf(regex)));
				sb.append(replacement);
				Source = Source.substring(Source.indexOf(regex) + regex.length(), Source.length());
			}
			sb.append(Source);
			Source = sb.toString();
			sb = null;
		}
		return Source;
	}

	// 字串取代 用 INDEX
	public static String StringReplaceAll(String Source, String regex, Vector replacments) {
		int replaceIndex = 1;
		StringBuffer sb = new StringBuffer();
		while (Source.indexOf(regex) >= 0) {
			sb.append(Source.substring(0, Source.indexOf(regex)));
			if (replaceIndex < replacments.size()) {
				sb.append(replacments.get(replaceIndex++));
			}
			Source = Source.substring(Source.indexOf(regex) + regex.length(), Source.length());
		}
		sb.append(Source);
		Source = sb.toString();
		sb = null;
		return Source;
	}

	public static String StringReplaceAll(String Source, String regex, String replacement) {
		StringBuffer sb = new StringBuffer();
		while (Source.indexOf(regex) >= 0) {
			sb.append(Source.substring(0, Source.indexOf(regex)));
			sb.append(replacement);
			Source = Source.substring(Source.indexOf(regex) + regex.length(), Source.length());
		}
		sb.append(Source);
		return sb.toString();
	}

	public static String StringDecode(InputStream in, String Decode) {
		String Dec = "UTF-8";
		if (Decode != null) {
			Dec = Decode;
		}
		try {
			String temp;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Dec));
			StringBuffer reString = new StringBuffer();
			while ((temp = br.readLine()) != null) {
				reString.append(temp);
			}
			br.close();
			return reString.toString();
		} catch (Exception e) {
		}
		return null;
	}

	// 把big5 轉成 8859-1
	public static String changeToBig5(String Source) {
		try {
			return (new String(Source.getBytes("ISO-8859-1"), "Big5"));
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static String changeToUTF8(String Source) {
		try {
			return (new String(Source.getBytes("ISO-8859-1"), "UTF-8"));
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	// 把big5 轉成 8859-1
	public static String changeTo8859(String Source) {
		try {
			return (new String(Source.getBytes("Big5"), "ISO-8859-1"));
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static String changeEncoding(String Source, String sourceEncoding, String targetEncoding) {
		try {
			return (new String(Source.getBytes(sourceEncoding), targetEncoding));
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static int StringToInt(String source) {
		try {
			return Integer.parseInt(source);
		} catch (Exception e) {
			return DEFAULTINT;
		}
	}

	public static float StringToFloat(String source) {
		try {
			return Float.parseFloat(source);
		} catch (Exception e) {
			return DEFAULTFLOAT;
		}
	}

	// 丟一個sting近來 如果可以轉換就會回傳 數字字串 如果不行就會回傳 "-1"
	public static String StringIntToString(String source) {
		return String.valueOf(StringToInt(source));
	}

	public static int StringToIntWithFormat(String source) {
		int re = DEFAULTINT;
		try {
			NumberFormat nf = NumberFormat.getInstance();
			Number nb = nf.parse(source);
			re = nb.intValue();
		} catch (Exception e) {
		}
		return re;
	}

	public static String SubString(String Source, int Start) {
		return SubString(Source, Start, Source.length());
	}

	// 包含起始跟結束的那個CHAR Start = 0 and End = Source.length
	public static String SubString(String Source, int Start, int End) {
		if (Source != null) {
			StringBuffer sb = new StringBuffer(Source);
			if (Source.length() > End) {
				Source = sb.substring(Start, End);
			} else {
				return Source;
			}
		}
		return Source;
	}

	public static String SubString(String Source, String Start, String End) {
		return SubString(Source, Start, End, null);
	}

	// OtherString(剩下的字串)
	public static String SubString(String Source, String Start, String End, StringBuffer OtherString) {
		int ints, inte, intsl, intel;
		ints = Source.indexOf(Start);
		inte = Source.indexOf(End, ints);

		if (inte < 0) {
			inte = Source.length();
		}

		intsl = Start.length();
		intel = End.length();
		if ((ints < 0) || (inte < 0)) {
			return "";
		}
		if (OtherString != null) {
			OtherString.append(Source.substring(inte + intel - 1, Source.length())); // 20040819
		}
		// OtherString.append( Source.substring( inte + intel , Source.length()
		// ) );
		return Source.substring(ints + intsl, inte);
	}

	// get SubString by Byte 中文碼 算 2 Bytes , intclue start and not include end
	public static String SubStringByByte(String Source, int Start, int End) {
		if ((Source != null) && (Source.length() > Start) && (Source.length() >= End)) {
			byte[] bytes = Source.getBytes();
			byte[] newBytes = new byte[End - Start];
			int order = 0;
			for (int index = Start; index < End; index++) {
				newBytes[order++] = bytes[index];
			}
			return new String(newBytes);
		}
		return Source;
	}

	// 取得多筆的子字串
	public static Vector<String> SubStrings(String Source, String Start, String End) {
		Vector<String> subs = new Vector();
		StringBuffer sb = new StringBuffer();
		String temp = SubString(Source, Start, End, sb);
		while (!temp.equalsIgnoreCase("")) {
			subs.add(temp);
			String nexttemp = sb.toString();
			sb.setLength(0);
			temp = SubString(nexttemp, Start, End, sb);
		}
		sb = null;
		return subs;
	}

	/*
	 * if string length = 0 will be remove example : 1,2,3,,4 -> 1,2,3,4 1,2,3,
	 * ,4 -> 1,2,3, ,4
	 */
	public static Vector<String> SubStrings(String Source, String Split) {
		if (Source != null) {
			// 20060922 shan
			Vector<String> strings = new Vector();
			String splitString[] = StringUtils.splitByWholeSeparator(Source, Split);
			for (int i = 0; i < splitString.length; i++) {
				strings.add(splitString[i]);
			}
			return strings;
		}
		return null;
	}

	/*
	 * public static Vector<String> SubStrings(String Source, String Split ,
	 * boolean notRemoveWholeSeparator) { Vector<String> re = new Vector();
	 * if(notRemoveWholeSeparator){ StringTokenizer s = new
	 * StringTokenizer(Source,Split); while(s.hasMoreTokens()){
	 * re.add(s.nextToken()); } return re ; } return SubStrings(Source,Split); }
	 */

	public static String BufferReaderToString(BufferedReader in) {
		StringBuffer FileString = new StringBuffer();
		try {
			if (in != null) {
				String temp;
				while ((temp = in.readLine()) != null) {
					FileString.append(temp + '\n');
				}
				in.close();
			}
		} catch (Exception e) {
		}
		return FileString.toString();
	}

	public static String StringEncodeDecode(String Source, String Decode, String Encode) {
		try {
			if ((Decode != null) && (Encode != null)) {
				return (new String(Source.getBytes(Decode), Encode));
			} else if (Encode != null) {
				return (new String(Source.getBytes(), Encode));
			} else if (Decode != null) {
				return (new String(Source.getBytes(Decode)));
			}
		} catch (Exception e) {
		}
		return Source;
	}

	public static ByteArrayInputStream StringToInputStream(String Source, String Encode) {
		try {
			return new ByteArrayInputStream(Source.getBytes(Encode));
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	/*
	 * Counting char Units The Java platform uses the Unicode Standard to define
	 * its characters. The Unicode Standard once defined characters as
	 * fixed-width, 16-bit values in the range U+0000 through U+FFFF. The U+
	 * prefix signifies a valid Unicode character value as a hexadecimal number.
	 * The Java language conveniently adopted the fixed-width standard for the
	 * char type. Thus, a char value could represent any 16-bit Unicode
	 * character. "abcd日期格式" = 8 countingCharacterUnits("abcd日期格式") = 8
	 * countingBytes("abcd日期格式") = 12
	 */
	/*
	 * Counting Character Units When Unicode version 4.0 defined a significant
	 * number of new characters above U+FFFF, the 16-bit char type could no
	 * longer represent all characters. Starting with the Java 2 Platform,
	 * Standard Edition 5.0 (J2SE 5.0), the Java platform began to support the
	 * new Unicode characters as pairs of 16-bit char values called a surrogate
	 * pair. Two char units act as a surrogate representation of Unicode
	 * characters in the range U+10000 through U+10FFFF. Characters in this new
	 * range are called supplementary characters. Although a single char value
	 * can still represent a Unicode value up to U+FFFF, only a char surrogate
	 * pair can represent supplementary characters. The leading or high value of
	 * the pair is in the U+D800 through U+DBFF range. The trailing or low value
	 * is in the U+DC00 through U+DFFF range. The Unicode Standard allocates
	 * these two ranges for special use in surrogate pairs. The standard also
	 * defines an algorithm for mapping between a surrogate pair and a character
	 * value above U+FFFF. Using surrogate pairs, programmers can represent any
	 * character in the Unicode Standard. This special use of 16-bit units is
	 * called UTF-16, and the Java Platform uses UTF-16 to represent Unicode
	 * characters. The char type is now a UTF-16 code unit, not necessarily a
	 * complete Unicode character (code point). The length method cannot count
	 * supplementary characters since it only counts char units. Fortunately,
	 * the J2SE 5.0 API has a new String method: codePointCount(int beginIndex,
	 * int endIndex). This method tells you how many Unicode code points
	 * (characters) are between the two indices. The index values refer to code
	 * unit or char locations. The value of the expression endIndex - beginIndex
	 * is the same value provided by the length method. This difference is not
	 * always the same as the value returned by the codePointCount method. If
	 * your text contains surrogate pairs, the length counts are definitely
	 * different. A surrogate pair defines a single character code point, which
	 * can be either one or two char units.
	 */
	public static int countingCharacterUnits(String sourceData) {
		int charCount = sourceData.length();
		int characterCount = sourceData.codePointCount(0, charCount);
		return characterCount;
	}

	/*
	 * How many bytes are in a String? The answer depends on the byte-oriented
	 * character set encoding used. One common reason for asking "how many
	 * bytes?" is to make sure you're satisfying string length constraints in a
	 * database. The getBytes method converts its Unicode characters into a
	 * byte-oriented encoding, and it returns a byte[]. One byte-oriented
	 * encoding is UTF-8, which is unlike most other byte-oriented encodings
	 * since it can accurately represent all Unicode code points.
	 */
	public static int countingBytes(String sourceData, String encode) {
		if (encode == null)
			encode = "UTF-8";
		byte[] utf8 = null;
		int byteCount = 0;
		try {
			utf8 = sourceData.getBytes(encode);
			byteCount = utf8.length;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return byteCount;
	}

	// counting Encode char counts
	public static int countingEncodeBytes(String sourceData, String encode) {
		if (((sourceData != null) && (sourceData.length() > 0)) && ((encode != null) && (encode.length() > 0))) {
			if (countingBytes(sourceData, encode) - sourceData.length() > 0)
				return countingBytes(sourceData, encode) - sourceData.length();
			else
				return 0;
		}
		return 0;
	}

	/**
	 * if Last String is Special String Will Be Remove Example : Source =
	 * 123,456,789, SpecString = , -> 123,456,789
	 * 
	 * @return String
	 */
	public static String removeLastSpecString(String source, String specString) {
		int lastIndex = source.lastIndexOf(specString);
		if (source.length() == (lastIndex + specString.length())) {
			return source.substring(0, lastIndex);
		}
		return source;
	}

	public static String replaceHTMLCodeForWeb(String sourceHtml) {
		sourceHtml = StringTools.replace(sourceHtml, "&lt", "&amp;lt");
		sourceHtml = StringTools.replace(sourceHtml, "&gt", "&amp;gt");
		sourceHtml = StringTools.replace(sourceHtml, "&", "&amp;");
		sourceHtml = StringTools.replace(sourceHtml, "<", "&lt;");
		sourceHtml = StringTools.replace(sourceHtml, ">", "&gt;");
		return "<pre>" + sourceHtml + "</pre>";
	}

	public static String replaceNullToSpace(String source) {

		if (source == null)
			source = "";

		return source;
	}

	/**
	 * 類似StringTokenizer功能
	 * 
	 * @param s
	 * @param delim
	 * @return
	 * @throws Exception
	 */
	public static String[] StringToken(String s, String delim) throws Exception {
		if (s == null)
			return null;

		ArrayList list = new ArrayList();

		if (delim == null) {
			list.add(s);
			return (String[]) list.toArray(new String[list.size()]);
		}

		int subStart, afterDelim = 0;
		int delimLength = delim.length();
		while ((subStart = s.indexOf(delim, afterDelim)) != -1) {
			list.add(s.substring(afterDelim, subStart));
			afterDelim = subStart + delimLength;
		}
		if (afterDelim <= s.length())
			list.add(s.substring(afterDelim));

		if (list.size() >= 2) {
			if (((String) list.get(list.size() - 1)).equals(""))
				list.remove(list.size() - 1);
			if (((String) list.get(0)).equals(""))
				list.remove(0);
		}

		String[] sa = (String[]) list.toArray(new String[list.size()]);
		return sa;
	}

	/**
	 * 將 field1,field2 -> 'field1','field2' or field1 -> 'field1'
	 * 
	 * @param items
	 * @return
	 */
	public static String addSqlInQuery(String items) {
		StringBuffer re = new StringBuffer();
		if (null != items && items.length() > 0 && ( items.indexOf("'") < 0 ) ) {
			if (items.indexOf(",") > 0) {
				String itemAry[] = items.split(",");
				for(int index = 0 ; index < itemAry.length ; index++){
					re.append("'");
					re.append(items);
					re.append("'");
					if( index < (itemAry.length - 1) ){
						re.append(",");
					}
				}
			} else {
				re.append("'");
				re.append(items);
				re.append("'");
			}
		}
		return re.toString() ;
	}
	
	public static List setBeanValue(List entityBeans, String fieldName, Object value) throws Exception{
	    
	    if(entityBeans != null && entityBeans.size() > 0){
	        for(Object entityBean : entityBeans){
	            PropertyUtils.setProperty(entityBean, fieldName, value);
	        }
	        return entityBeans;
	    }else{
		return new ArrayList(0);
	    }
	}
	
	public static String parseSQLinStatement(List allCode) throws Exception{
	    String inResult ="";
	    if(allCode.size()>1000){
		throw new Exception("查詢in條件筆數超過一千筆，請分千筆查詢");
	    }else{
		for(Iterator iter = allCode.iterator();iter.hasNext();){
		    String item = (String)iter.next();
		    inResult += "'"+item+"',";
		}
		inResult = inResult.substring(0,inResult.length()-1);
	    }
	    return inResult;
	}
}
