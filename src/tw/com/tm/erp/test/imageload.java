package tw.com.tm.erp.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class imageload {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		try {
			File file = new File("/Users/lukelyu/ERP/workspace/project/pos_2.0_web/src/002-1.png");//本地圖片
			if(file.exists()) {
				BufferedImage image = (BufferedImage)ImageIO.read(file);
			}else {
				System.out.println("image file not exits");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
