package tw.com.tm.erp.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;

/*import com.sun.image.codec.jpeg.ImageFormatException;
 import com.sun.image.codec.jpeg.JPEGCodec;
 import com.sun.image.codec.jpeg.JPEGImageDecoder;
 import com.sun.image.codec.jpeg.JPEGImageEncoder;*/

/**
 * <p>
 * Title : 檔案工具
 * </p>
 * 
 * @author shan
 * @version 1.0
 */

public class FileTools {

	public static final String JPEG = "jpg";

	public static final String GIF = "gif";

	public static final String PNG = "png";

	public static String SYSTEMENCODING = "big5";

	public static String DEFAULTFILEENCODING = "utf-8";

	public static File WriteToFileByStream(InputStream fin, String ToFile, boolean overwrite) throws FileNotFoundException, IOException {
		File f = new File(ToFile);
		if (!overwrite)// 如果不要覆寫這個函式會幫您找到您要存入的新檔名
			f = GetDifferentFileName(f);
		File pf = f.getParentFile();
		if (!pf.exists())
			pf.mkdirs();
		FileOutputStream fout = new FileOutputStream(f);
		byte[] buffer = new byte[4096];
		int bytes_read;
		while ((bytes_read = fin.read(buffer)) != -1)
			fout.write(buffer, 0, bytes_read);
		fin.close();
		fout.close();
		return f;
	}

	public static File WriteToFileByStream(File InputFile, String ToFile, boolean overwrite) throws FileNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(InputFile);
		File f = new File(ToFile);
		if (!overwrite)// 如果不要覆寫這個函式會幫您找到您要存入的新檔名
			f = GetDifferentFileName(f);
		File pf = f.getParentFile();
		if (!pf.exists())
			pf.mkdirs();
		FileOutputStream fout = new FileOutputStream(f);
		byte[] buffer = new byte[4096];
		int bytes_read;
		while ((bytes_read = fin.read(buffer)) != -1)
			fout.write(buffer, 0, bytes_read);
		fin.close();
		fout.close();
		return f;
	}

	public static void WriteToStream(byte source[], OutputStream sOut) throws FileNotFoundException, IOException {
		sOut.write(source, 0, source.length);
		sOut.close();
	}

	public static File GetDifferentFileName(File f) {
		String parent = f.getParent() + "\\";
		String newFileName = f.getName();
		String name = newFileName.substring(0, newFileName.indexOf("."));
		String subname = "." + newFileName.substring(newFileName.indexOf(".") + 1, newFileName.length());

		if (f.exists()) {
			int order = 0;
			while (f.exists()) {
				f = new File(parent + name + "-" + order + subname);
				order++;
			}
		}
		return f;
	}

	/*
	 * public static ArrayList ReadFromFileToArrayList( File f ) { ArrayList
	 * FileStrings = new ArrayList(); try{ if( f.exists() ) { BufferedReader in =
	 * new BufferedReader( new FileReader(f) ); String temp ; while((temp =
	 * in.readLine()) != null ) FileStrings.add(temp); in.close(); }
	 * }catch(Exception e){} return FileStrings; }
	 */
	// 20070314 shan utf-8
	public static ArrayList<String> ReadFromFileToArrayList(File f) {
		return ReadFromFileToArrayList(f, null);
	}

	public static ArrayList<String> ReadFromFileToArrayList(File f, String encoding) {
		ArrayList<String> FileStrings = new ArrayList();
		try {
			if (f.exists()) {
				if ((encoding == null) || (encoding.length() == 0))
					encoding = DEFAULTFILEENCODING;
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
				String temp;
				while ((temp = in.readLine()) != null) {
					FileStrings.add(temp);
				}
				in.close();
			}
		} catch (Exception e) {
		}
		return FileStrings;
	}

	// 20070502 shan read from file by byte and change line by user define byte
	// to
	// change line
	// 如果有全形空白 Byte數會有問題
	public static ArrayList<String> ReadFromFileToArrayList(File f, int length, String charset) throws Exception {
		ArrayList<String> FileStrings = new ArrayList();
		byte[] source = ReadFromFileByStream(f);
		int lengthInFile = source.length;
		byte bs[] = null;
		int times = lengthInFile / length;
		int off = 0;
		int index = 0;
		for (; index < times; index++) {
			bs = new byte[length];
			off = index * length;
			for (int i = off; i < off + length; i++)
				bs[i - off] = source[i];
			FileStrings.add(new String(bs, charset));
		}
		/*
		 * off = index * length ; if((lengthInFile-off) > 0 ) { bs = new
		 * byte[lengthInFile-off]; dataStream.read( bs, off , lengthInFile -off ) ;
		 * FileStrings.add( new String( bs ,charset) ) ; }
		 */
		return FileStrings;
	}

	public static String ReadFromFile(File f) {
		return ReadFromFile(f, true);
	}

	/*
	 * public static String ReadFromFile( File f , boolean changeLine ) {
	 * StringBuffer FileString = new StringBuffer(); try{ if( f.exists() ) {
	 * BufferedReader in = new BufferedReader( new FileReader(f) ); String temp ;
	 * while((temp = in.readLine()) != null ) { if(changeLine)
	 * FileString.append(temp + '\n' ); else FileString.append(temp); }
	 * in.close(); } }catch(Exception e){} return FileString.toString() ; }
	 */
	// 20070314 shan utf-8
	public static String ReadFromFile(File f, boolean changeLine) {
		StringBuffer FileString = new StringBuffer();
		try {
			if (f.exists()) {
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), DEFAULTFILEENCODING));
				String temp;
				while ((temp = in.readLine()) != null) {
					if (changeLine)
						FileString.append(temp + '\n');
					else
						FileString.append(temp);
				}
				in.close();
			}
		} catch (Exception e) {
		}
		return FileString.toString();
	}

	public static String ReadFromFile(String FileName) {
		return ReadFromFile(new File(FileName), true);
	}

	public static String ReadFromFile(String FileName, boolean changeLine) {
		return ReadFromFile(new File(FileName), changeLine);
	}
	
	public static void MoveFile(File fromFile, File toFile) {
	
			CopyFile(fromFile,toFile);
			fromFile.deleteOnExit();
	}

	public static void CopyFile(File FromFile, File ToFile) {
		try {
			FileInputStream fin = new FileInputStream(FromFile);
			FileOutputStream fout = new FileOutputStream(ToFile);
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = fin.read(buffer)) != -1)
				fout.write(buffer, 0, bytes_read);
			fin.close();
			fout.close();
		} catch (Exception e) {
		}
	}

	public static void CopyFile(String FromFile, String ToFile) {
		try {
			FileInputStream fin = new FileInputStream(FromFile);
			FileOutputStream fout = new FileOutputStream(ToFile);
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = fin.read(buffer)) != -1)
				fout.write(buffer, 0, bytes_read);
			fin.close();
			fout.close();
		} catch (Exception e) {
		}
	}

	public static File WriteToFile(String FileName, File FileSource) {
		try {
			return WriteToFile(FileName, new FileInputStream(FileSource));
		} catch (Exception e) {
			return null;
		}
	}

	public static File WriteToFile(String FileName, InputStream in) {
		return WriteToFile(FileName, StringTools.InputToString(in));
	}

	public static File WriteToFile(String FileName, String Source, String Encode) {
		if (Encode == null)
			Encode = "Big5";
		try {
			File f = new File(FileName);
			File pf = f.getParentFile();
			if (!pf.exists())
				pf.mkdir();
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), Encode);
			osw.write(Source);
			osw.flush();
			osw.close();
			return (new File(FileName));
		} catch (Exception e) {
			return null;
		}
	}

	public static File WriteToFile(String FileName, String Source) {
		if ((Source != null) && (!Source.equals(""))) {
			try {
				File f = new File(FileName);
				File pf = f.getParentFile();
				if (!pf.exists())
					pf.mkdirs();
				PrintWriter out = new PrintWriter(new FileWriter(f));
				out.print(Source);
				out.flush();
				out.close();
				return f;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static File WriteToFile(String FileName, byte Source[]) {
		if ((Source != null) && (Source.length > 0)) {
			try {
				File f = new File(FileName);
				File pf = f.getParentFile();
				if (!pf.exists())
					pf.mkdirs();
				FileOutputStream fout = new FileOutputStream(f);
				fout.write(Source, 0, Source.length);
				fout.flush();
				fout.close();
				return f;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static Vector SpiltFile(File DataFile, int SplitSize, int SplitLines) {
		Vector rFile = new Vector();
		if (DataFile.length() > SplitSize) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(DataFile));
				String temp = DataFile.getAbsolutePath();
				String fname = temp.substring(0, temp.indexOf("."));
				String sfname = temp.substring(temp.indexOf("."), temp.length());
				do {
					File newFile = new File(fname + "_" + System.currentTimeMillis() + sfname);
					FileWriter fout = new FileWriter(newFile);
					int lineCount = 0;
					while ((temp = in.readLine()) != null) {
						fout.write(temp + '\n');
						lineCount++;
						if (lineCount == SplitLines)
							break;
					}
					fout.flush();
					fout.close();
				} while (temp != null);
				in.close();
			} catch (Exception e) {
			}
		} else {
			rFile.add(DataFile);
		}
		return rFile;
	}

	public static byte[] ReadFromFileByStream(File InputFile) throws FileNotFoundException, IOException, IOException {
		FileInputStream finput = new FileInputStream(InputFile);
		int lengthInFile = finput.available();
		byte bs[] = new byte[lengthInFile];
		DataInputStream dataStream = new DataInputStream(finput);
		try {
			for (int i = 0; i < lengthInFile; i++) {
				bs[i] = dataStream.readByte();
			}
		} finally {
			dataStream.close();
		}
		return bs;
	}

	// 20051002 shan 依照檔案的SIZE決定是否要壓縮 ..
	public static String CreateSmallImage(File ReadFile, String Folder, String FileName, String Format, int resultWidth, int resultHeight,
			int compressWidth, int compressHeight) throws Exception {
		BufferedImage bi = ImageIO.read(ReadFile);
		int actualWidth = bi.getWidth();
		int actualHeight = bi.getHeight();
		if ((actualWidth > compressWidth) || (actualHeight > compressHeight))
			return CreateSmallImage(ReadFile, Folder, FileName, Format, resultWidth, resultHeight);
		else {
			CopyFile(ReadFile, new File(Folder + FileName + "." + Format));
			return FileName + "." + Format;
		}
	}

	// 20051002 shan 依照檔案的SIZE決定是否要壓縮 ..
	public static String CreateSmallImage(File ReadFile, String Folder, String FileName, String Format, int resultWidth, int resultHeight,
			int compressSize) throws Exception {
		if (ReadFile.length() > compressSize)
			return CreateSmallImage(ReadFile, Folder, FileName, Format, resultWidth, resultHeight);
		else {
			CopyFile(ReadFile, new File(Folder + FileName + "." + Format));
			return FileName + "." + Format;
		}
	}

	/**
	 * Create small image
	 * 
	 * @param ReadFile
	 * @param Folder
	 * @param FileName
	 * @param Format
	 * @param resultWidth
	 * @param resultHeight
	 * @return
	 * @throws Exception
	 *             FileTools.CreateSmallImage(new File("c:/" + cFile), "c:/" ,
	 *             "test2" , "jpg", sx, sy);
	 */
	public static String CreateSmallImage(File ReadFile, String Folder, String FileName, String Format, int resultWidth, int resultHeight)
			throws Exception {
		BufferedImage bi = ImageIO.read(ReadFile);

		int actualWidth = bi.getWidth();
		int actualHeight = bi.getHeight();
		double sx = (float) (resultWidth) / actualWidth;
		double sy = (float) (resultHeight) / actualHeight;
		if (Format.equalsIgnoreCase(GIF))
			Format = PNG;

		if (!Format.equalsIgnoreCase(JPEG) && !Format.equalsIgnoreCase(PNG))
			throw new RuntimeException("Not Support This Format!");

		BufferedImage nbi = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = nbi.createGraphics();
		AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
		g2.setTransform(at);
		g2.drawImage(bi, 0, 0, null);
		ImageIO.write(nbi, Format, new File(Folder + FileName + "." + Format));
		return FileName + "." + Format;
	}

	/**
	 * Clip Image
	 * 
	 * @param ReadFile
	 * @param Folder
	 * @param FileName
	 * @param Format
	 * @param clipx
	 * @param clipy
	 * @param clipWidth
	 * @param clipHeight
	 * @return
	 * @throws Exception
	 *             FileTools.CreateClipImage(new File(file), "c:/" , "test1" ,
	 *             "jpg", x, y , xw , yw )
	 */
	public static String CreateClipImage(File ReadFile, String Folder, String FileName, String Format, int clipx, int clipy, int clipWidth,
			int clipHeight) throws Exception {
		BufferedImage bi = ImageIO.read(ReadFile);
		if (Format.equalsIgnoreCase(GIF))
			Format = PNG;
		if (!Format.equalsIgnoreCase(JPEG) && !Format.equalsIgnoreCase(PNG))
			throw new RuntimeException("Not Support This Format!");
		BufferedImage nbi = new BufferedImage(clipWidth, clipHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = nbi.createGraphics();
		// 20070717 shan
		// Ellipse2D ellipse = new Ellipse2D.Float();
		// ellipse.setFrame(clipx, clipy, clipWidth, clipHeight);
		// g2.setClip(ellipse);
		g2.setClip(clipx, clipy, clipWidth, clipHeight);
		g2.drawImage(bi, 0, 0, null);
		ImageIO.write(nbi, Format, new File(Folder + FileName + "." + Format));
		return FileName + "." + Format;
	}

	/** Gzip the contents of the from file and save in the to file. */
	public static void gzipFile(String from, String to) throws IOException {
		// Create stream to read from the from file
		FileInputStream in = new FileInputStream(from);
		// Create stream to compress data and write it to the to file.
		GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(to));
		// Copy bytes from one stream to the other
		byte[] buffer = new byte[4096];
		int bytes_read;
		while ((bytes_read = in.read(buffer)) != -1)
			out.write(buffer, 0, bytes_read);
		// And close the streams
		in.close();
		out.close();
	}

	/** Zip the contents of the directory, and save it in the zipfile */
	public static void zipDirectory(String dir, String zipfile) throws IOException, IllegalArgumentException {
		// Check that the directory is a directory, and get its contents
		File d = new File(dir);
		if (!d.isDirectory())
			throw new IllegalArgumentException("Compress: not a directory:  " + dir);
		String[] entries = d.list();
		byte[] buffer = new byte[4096]; // Create a buffer for copying
		int bytes_read;

		// Create a stream to compress data and write it to the zipfile
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

		// Loop through all entries in the directory
		for (int i = 0; i < entries.length; i++) {
			File f = new File(d, entries[i]);
			if (f.isDirectory())
				continue; // Don't zip sub-directories
			FileInputStream in = new FileInputStream(f); // Stream to read
			// file
			ZipEntry entry = new ZipEntry(f.getPath()); // Make a ZipEntry
			out.putNextEntry(entry); // Store entry
			while ((bytes_read = in.read(buffer)) != -1)
				// Copy bytes
				out.write(buffer, 0, bytes_read);
			in.close(); // Close input stream
		}
		// When we're done with the whole loop, close the output stream
		out.close();
	}

	public static String getSystemEncoding() {
		return System.getProperty("file.encoding");
	}

	public static String getDEFAULTFILEENCODING() {
		return DEFAULTFILEENCODING;
	}

	public static void setDEFAULTFILEENCODING(String defaultfileencoding) {
		DEFAULTFILEENCODING = defaultfileencoding;
	}

	public static String getSYSTEMENCODING() {
		return SYSTEMENCODING;
	}

	public static void setSYSTEMENCODING(String systemencoding) {
		if (systemencoding == null) {
			SYSTEMENCODING = getSystemEncoding();
		} else {
			SYSTEMENCODING = systemencoding;
		}
	}

	/**
	 * 20070720 shan resize jpeg image
	 * 
	 * @param ReadFile
	 * @param Folder
	 * @param FileName
	 * @param resultWidth
	 * @param resultHeight
	 * @return
	 * @throws ImageFormatException
	 * @throws IOException
	 *             FileTools.resizeJPEG(new File("c:/" + cFile), "c:/" , "test3" ,
	 *             sx, sy);
	 */
	/*
	 * public static String resizeJPEG(File ReadFile, String Folder, String
	 * FileName, int resultWidth, int resultHeight ) throws
	 * ImageFormatException, IOException{ InputStream imageIn = new
	 * FileInputStream(ReadFile); JPEGImageDecoder decoder =
	 * JPEGCodec.createJPEGDecoder(imageIn); FileOutputStream fout = new
	 * FileOutputStream(new File(Folder + FileName + ".JPG" )); BufferedImage
	 * image = decoder.decodeAsBufferedImage(); image =
	 * resizeAWTs(image,resultWidth,resultHeight); JPEGImageEncoder encoder =
	 * JPEGCodec.createJPEGEncoder(fout); encoder.encode(image); return FileName +
	 * ".JPG"; }
	 */

	// Resize Image
	public static BufferedImage resizeAWTs(BufferedImage src, double width, double hight) {
		double factorX = 1.00;
		double factorY = 1.00;
		int w = src.getWidth();
		double wD = Double.parseDouble("" + w);
		int h = src.getHeight();
		double hD = Double.parseDouble("" + h);
		factorX = width / wD;
		if (factorX > 1)
			factorX = 1;
		factorY = hight / hD;
		if (factorY > 1)
			factorY = 1;
		if (factorX < factorY) {
			factorY = factorX;
		} else {
			factorX = factorY;
		}
		int newW = (int) (Math.floor(factorX * w));
		int newH = (int) (Math.floor(factorY * h));
		Image temp = src.getScaledInstance(newW, newH, Image.SCALE_AREA_AVERAGING);
		BufferedImage tgt = createBlankImage(src, newW, newH);
		Graphics2D g = tgt.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		return tgt;
	}

	// Uses image to create another image of the same "type", but of a factored
	// size
	public static BufferedImage createBlankImage(BufferedImage src, int w, int h) {
		int type = src.getType();
		if (type != BufferedImage.TYPE_CUSTOM)
			return new BufferedImage(w, h, type);
		else {
			ColorModel cm = src.getColorModel();
			WritableRaster raster = src.getRaster().createCompatibleWritableRaster(w, h);
			boolean isRasterPremultiplied = src.isAlphaPremultiplied();
			return new BufferedImage(cm, raster, isRasterPremultiplied, null);
		}
	}

	/**
	 * Used to list the files / subdirectories in a given directory.
	 * 
	 * @param dir
	 *            Directory to start listing from
	 */
	public static void listAllSubDirectoryFiles(String dirName, List allFields) {
		if (StringUtils.hasText(dirName)) {
			File dir = new File(dirName);
			File[] children = dir.listFiles();
			if (null != children) {
				for (int index = 0; index < children.length; index++) {
					File file = children[index];
					if (file.isDirectory()) {
						listAllSubDirectoryFiles(file.getAbsolutePath(), allFields);
					} else {
						allFields.add(file.getAbsoluteFile());
					}
				}
			}
		}
	}
}
