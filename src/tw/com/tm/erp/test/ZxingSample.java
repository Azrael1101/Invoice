package tw.com.tm.erp.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ZxingSample {

	//二維碼顏色
	private static final int BLACK = 0xFF000000;
	//二維碼顏色
	private static final int WHITE = 0xFFFFFFFF;
	private static final String CHARSET = null;
	
	/**
	* ZXing 方式生成二維碼
	*
	* @param text       內容
	* @param width      二維碼寬
	* @param height     二維碼高
	* @param outPutPath 二維碼生成儲存路徑
	* @param imageType  二維碼生成格式
	*/
	public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType) {
		Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
		//設定編碼字符集
		his.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			//生成二維碼
			BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
			//二維碼寬高
			int codeWidth = encode.getWidth();
			int codeHeight = encode.getHeight();
			//將二維碼放入緩衝流
			BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < codeWidth; i++  ) {
				for (int j = 0; j < codeHeight; j++  ) {
					//迴圈將二維碼內容定入圖片
					image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
				}
			}
			File outPutImage = new File(outPutPath);
			//如果圖片不存在建立圖片
			if (!outPutImage.exists())
			outPutImage.getParentFile().mkdir();
			//將二維碼寫入圖片
			ImageIO.write(image, imageType, outPutImage);
			
			System.out.println("二維碼生成完成");
			
		} catch (WriterException e) {
			e.printStackTrace();
			System.out.println("二維碼生成失敗");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("生成二維碼圖片失敗");
		}
	}
	
	/**
     * 將字串編成一維條碼的矩陣  
     * @param str
     * @param width
     * @param height
     */ 
    public static void toBarCodeMatrix(String str, Integer width, 
            Integer height, String outPutPath, String imageType) { 

        if (width == null || width < 200) { 
            width = 200; 
        } 

        if (height == null || height < 50) { 
            height = 50; 
        } 

        try { 
            // 文字編碼 
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(); 
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
            //生成一維碼
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_128, width, height, hints); 
            //一維碼長寬
            int barWidth = bitMatrix.getWidth();
			int barHeight = bitMatrix.getHeight();
            
			BufferedImage image = new BufferedImage(barWidth, barHeight, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < barWidth; i++  ) {
				for (int j = 0; j < barHeight; j++  ) {
					image.setRGB(i, j, bitMatrix.get(i, j) ? BLACK : WHITE);
				}
			}
			File outPutImage = new File(outPutPath);
			//如果圖片不存在建立圖片
			if (!outPutImage.exists())
			outPutImage.getParentFile().mkdir();
			//將一維碼寫入圖片
			ImageIO.write(image, imageType, outPutImage);
			
			System.out.println("一維碼生成完成");
			 
        } catch (WriterException e) {
			e.printStackTrace();
			System.out.println("一維碼生成失敗");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("生成一維碼圖片失敗");
		} 
    } 
	
	/**
     * 根據點矩陣生成黑白圖。 
     */ 
    public static BufferedImage toBufferedImage(BitMatrix matrix) { 
        int width = matrix.getWidth(); 
        int height = matrix.getHeight(); 
        BufferedImage image = new BufferedImage(width, height, 
                BufferedImage.TYPE_INT_RGB); 
        for (int x = 0; x < width; x++) { 
            for (int y = 0; y < height; y++) { 
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE); 
            } 
        } 
        return image; 
    }
	
	/**
     * 根據矩陣、圖片格式，生成檔案。
     */ 
    public static void writeToFile(BitMatrix matrix, String format, File file) 
            throws IOException { 
        BufferedImage image = toBufferedImage(matrix); 
        if (!ImageIO.write(image, format, file)) { 
            throw new IOException("Could not write an image of format " 
                    + format + " to " + file); 
        } 
    }
    
	public static void main(String[] args) {
		
		try {
			zxingCodeCreate("AB112233441020523999900000144000001540000000001234567ydXZt4LAN1U HN/j1juVcRA==:**********:3:3:1:乾電池:1:105:",
					300, 300, "results/zxingcode.jpg", "jpg");
			toBarCodeMatrix("10404UZ176908720122",200,50, "results/barCode.jpg", "jpg");
//			String text = "1234567890";   
//	        String result;  
//	        String format = "jpg";   
//	        
//	        File outputFile = new File("/Users/lukelyu/TM/workspace/BarcodeSample/results"+File.separator+"zxingcode.jpg");   
//	        writeToFile(toBarCodeMatrix(text, null, null), format, outputFile); 
		}catch(Exception e) {
			e.printStackTrace();
		}
        
	}

}
