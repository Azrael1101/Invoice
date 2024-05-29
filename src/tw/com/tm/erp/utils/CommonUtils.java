/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Apr 1, 2008
 */
package tw.com.tm.erp.utils;

import java.math.BigDecimal;
import java.util.Calendar;

import tw.com.tm.erp.hbm.service.ImOnHandService;
import tw.com.tm.erp.hbm.service.ServiceFactory;

/**
 * @author Dumars.Tsai
 */
public class CommonUtils {
	
	public final static String ZERO = "0";
	public final static String SPACE = " ";
	
	public static String setEnableValue(String value) {
		if(value == null || "".equals(value)) {
			return "Y";
		} else {
			return "N";
		}
	}
	
	/**
	 * 套用F&P批號規則來產生一個新批號,長度為6碼,格式依序為<br>
	 * 日期(2碼):1~31<br>
	 * 月份(1碼):A~Z<br>
	 * 年份(1碼):A~L<br>
	 * 序號(2碼):01~99
	 * @return 產生的批號
	 */
	public static String createNewLotNo() {
		// 產生日期資料
		Calendar cal = Calendar.getInstance();
		/*
		 * 依照F&P規則
		 * 年份由2005~2030,以英文字母A~Z取代
		 * ex: 2005=A, 2006=B,.... 2030=Z
		 */
		String year = String.valueOf((char)(cal.get(Calendar.YEAR) - 2005 + 65));
		/*
		 * 依照F&P規則
		 * 月份1~12,以英文字母A~L取代
		 * ex: 1月=A, 2月=B,....12月=L
		 */
		String month = String.valueOf((char)(cal.get(Calendar.MONTH) + 65));
		/*
		 * 日期補滿兩碼長度
		 */
		String day = String.valueOf(cal.get(Calendar.DATE));
		if(day.length() == 1) {
			day = CommonUtils.insertCharacterWithFixLength(day, 2, ZERO);
		}
		
		/*
		 * 檢查今日是否已建立過新批號,有的話最後兩碼取最大值+1,否則由01開始
		 */
		//ImOnHandDAO dao = DAOFactory.getInstance().getImOnHandDAO();
		ImOnHandService service = ServiceFactory.getInstance().getImOnHandService();
		String todayMaxLotNo = service.findMaxLotNo(day + month + year);
		if(todayMaxLotNo == null || "".equals(todayMaxLotNo)) {
			return (day + month + year + "01");
		} else {
			int seq = Integer.parseInt(todayMaxLotNo.substring(4, 6)) + 1;
			String newSeq = insertCharacterWithFixLength(String.valueOf(seq), 2, ZERO);
			return (day + month + year + newSeq);
		}
	}
	
	/**
	 * 依照需求的字串長度在原字串前面補足所需的文字或空白
	 * @param source 原字串
	 * @param length 所需的字串長度
	 * @param character 要補位的字元
	 * @return 新字串
	 */
	public static String insertCharacterWithFixLength(String source,
			int length, String character) {
		while(source.length() < length) {
			source = character + source;
		}
		return source;
	}
	
	
	/**
	 * 依照需求的字串長度在原字串前面或後面補足所需的文字或空白
	 * 
	 * @param source
	 * @param length
	 * @param character
	 * @param align
	 * @return String
	 */
	public static String insertCharacterWithFixLength(String source,
			int length, String character, String align) {
		while(source.length() < length) {
		    if("R".equalsIgnoreCase(align)){
			source += character;
		    }else if("L".equalsIgnoreCase(align)){
			source = character + source;
		    }
		}
		return source;
	}
	
	public static boolean isEmpty(String string) {
		if(string == null || "".equals(string)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(Number number) {
		if(number == null || number.equals(0)) {
			return true;
		} else {
			return false;
		} 
	}
	
	public static double round(double origNumber, int scale){
	    BigDecimal decimal = new   BigDecimal(origNumber);
	    return decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 依照限定的byte長度在原字串前面或後面補足所需的文字或空白
	 * 
	 * @param source
	 * @param dataLength
	 * @param formatLength
	 * @param character
	 * @param align
	 * @return String
	 */
	public static String insertCharacterWithLimitedLength(String source, int dataLength,
	        int formatLength, String character, String align) {
	     
	    if(source != null){
		int actualDataLength = (dataLength > formatLength)?formatLength:dataLength;
	        byte[] currentBytes = source.getBytes();
		if(currentBytes.length > actualDataLength){		
		    source = new String(currentBytes, 0 , actualDataLength);
		    currentBytes = source.getBytes();
		}	
	        while(currentBytes.length < formatLength){
	            if("R".equalsIgnoreCase(align)){
		        source += character;
		    }else if("L".equalsIgnoreCase(align)){
		        source = character + source;
	            }
	            currentBytes = source.getBytes();
		}	
	    }else{
		source = "";
		for(int i = 0; i < formatLength; i++){
		    if("R".equalsIgnoreCase(align)){
		        source += character;
		    }else if("L".equalsIgnoreCase(align)){
		        source = character + source;
	            }
		}		
	    }
	       
	    return source;
        }
}
