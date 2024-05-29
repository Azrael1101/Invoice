package tw.com.tm.erp.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;

import org.apache.pdfbox.encoding.Encoding;

import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.SaleheadTable;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.test.ZxingSample;
import tw.com.tm.erp.utils.DateUtils;

public class PrintInvoice implements Printable {
	
	public int pageSize;//列印的總頁數
	public double paperW = cmToPixel(10f,DPI);//列印的紙張寬度
	public double paperH = cmToPixel(10f,DPI);//列印的紙張高度
	public double imageAbleX = 0;
	public double imageAbleY = 0;
	public static final float DPI = 72;
	//定義全域 打印機
	private String defined_printer = "";
	//定義打印方法 把全域打印機的值帶進（形式參數內）
	public void setDefined_printer(String defined_printer) {
		//定義打印的方法 並將全域的塞進這個封包
		this.defined_printer = defined_printer;
	}
	//建立 集合 head
	//public List head = new ArrayList();
	public SaleheadTable saleHeadTable = null;
	public SaleheadTable getSaleHeadTable() {
		return saleHeadTable;
	}

	public void setSaleHeadTable(SaleheadTable saleHeadTable) {
		this.saleHeadTable = saleHeadTable;
	}
	//定義庫存資料連資料庫
	private ImItemDAO imItemDAO;
	public List head;
	//定義庫存連線的方法 並將全域的塞進這個封包
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }

    @Override 
    //定義 print 方法（圖型.頁面格式.頁索引）
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex >= pageSize) 
			return Printable.NO_SUCH_PAGE;//退出列印
		else {
//			BufferedImage img = new BufferedImage((int)Math.round(paperW),(int)Math.round(paperH), BufferedImage.TYPE_INT_RGB);
//			Graphics2D g2 = img.createGraphics();
			//強制轉型(Graphics2D)
			Graphics2D g2 = (Graphics2D) graphics;
			//g2.translate(imageAbleX, imageAbleY);
			g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			// 設置紙張的大小 
			Paper p = new Paper(); 
//			p.setSize(paperW, paperH);
//			p.setImageableArea(imageAbleX, imageAbleY, paperW, paperH);
			p.setSize(pageFormat.getWidth(), pageFormat.getHeight());
			p.setImageableArea(pageFormat.getImageableX(), pageFormat.getImageableY(), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
			pageFormat.setPaper(p);
			//pageFormat.setOrientation(PageFormat.LANDSCAPE);
			
			System.out.println(pageFormat.getImageableX());
			System.out.println(pageFormat.getImageableY());
			System.out.println(pageFormat.getImageableWidth());
			System.out.println(pageFormat.getImageableHeight());
			System.out.println(pageFormat.getWidth());
			System.out.println(pageFormat.getHeight());
			
			drawCurrentPageText(g2, pageFormat);//調用列印內容的方法
			return PAGE_EXISTS;
		}
	}
    
    public float cmToPixel(float cm, float dpi) {
        return (dpi / 2.54f) * cm;
    }
	
	public List<String> getPrinters(){
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        List<String> printerList = new ArrayList<String>();
        for(PrintService printerService: printServices){
            printerList.add( printerService.getName());
        }
        return printerList;
    }


	// 列印內容
	private void drawCurrentPageText(Graphics2D g2, PageFormat pf) {
		//System.out.println("drawCurrentPageText....");
		try {
			 Graphics2D graphics2D = (Graphics2D) g2;
			 int font_distence = 5;
			 int font_height = 10;
			 int yIndex = 30;
			 File file = null;
			 BufferedImage image = null;
			 Font font = new Font(Font.DIALOG, Font.BOLD, 15);
			 Color defaultColor = graphics2D.getColor();
			 Color grey = new Color(145, 145, 145);
			 //System.out.println(items.size());
			 
			 graphics2D.setFont(font);
			 drawString(graphics2D, "采盟股份有限公司", 0, 15, 140, 70);//font_height+font_distence
			    
			 font = new Font("宋體", Font.PLAIN, 17);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,"電子發票證明聯" , 0, 40, 127, 30);
			    
			 font = new Font(Font.DIALOG, Font.CENTER_BASELINE, 15);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,"110年11-12月" , 3, 60, 120, 25);
			 
			 
			 font = new Font(Font.DIALOG, Font.BOLD, 15);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,saleHeadTable.getCustomerPoNo(), 5, 75, 180, 25);
			 
			 font = new Font(Font.DIALOG, Font.PLAIN, 8);
			 graphics2D.setFont(font);
			 String saleDt = DateUtils.format(saleHeadTable.getSalesOrderDate())+ " " + saleHeadTable.getTransactionTime();
			 yIndex = drawString(graphics2D,saleDt, 15, 83, 180, font_height+font_distence);
			 
			 font = new Font(Font.DIALOG, Font.PLAIN, 8);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,"隨機碼："+ (String)head.get(8), 11, 	92, 170, 8);
			 font = new Font(Font.DIALOG, Font.PLAIN, 8);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,"總計："+(String)head.get(9) , 70, 92, 170, 8);
			 
			 font = new Font(Font.DIALOG, Font.PLAIN, 8);
			 graphics2D.setFont(font);
			 yIndex = drawString(graphics2D,"賣方12371287" , 11, 107, 180, 8);
			 
			 
			 ZxingSample zxingSample = new ZxingSample();
			 String barCodeString = "11101"+(String)head.get(5)+(String)head.get(8);
			 ZxingSample.toBarCodeMatrix(barCodeString, 150, 50, "D:\\MA\\barCode.jpg", "jpg");
			 //一維條形碼
			 file = new File("D:\\\\MA\\\\barCode.jpg");//本地圖片
			    if(file.exists()) {
			     image = (BufferedImage)ImageIO.read(file);
			    }else {
			     System.out.println("image file not exits");
			    }
			    drawImage(graphics2D,image,0, 100, 120, 13);
			  
			 //二維QRcode
			  String qrCodeStringL = (String)head.get(7);
			  	System.out.println(qrCodeStringL);
			  String qrCodeStringR = "**"+(String)head.get(10);
			  	System.out.println(qrCodeStringR);
			  ZxingSample.zxingCodeCreate(qrCodeStringL, 400, 400, "D:\\\\MA\\\\zxingcodeL.jpg", "jpg"); //左二維碼生成
			  ZxingSample.zxingCodeCreate(qrCodeStringR, 400, 400, "D:\\\\MA\\\\zxingcodeR.jpg", "jpg"); //右二維碼生成
			 file = new File("D:\\\\MA\\\\zxingcodeL.jpg");//本地圖片
			    if(file.exists()) {
			     image = (BufferedImage)ImageIO.read(file);
			    }else {
			     System.out.println("image file not exits");
			    }
			    drawImage(graphics2D,image,0, 115, 60, 60);
			 
			 file = new File("D:\\\\MA\\\\zxingcodeR.jpg");//本地圖片
			    if(file.exists()) {
			     image = (BufferedImage)ImageIO.read(file);
			    }else {
			     System.out.println("image file not exits");
			    }
			    drawImage(graphics2D,image, 50, 115, 60, 60);
			 //
			font = new Font(Font.DIALOG, Font.PLAIN, 8);
				 graphics2D.setFont(font);
				 yIndex = drawString(graphics2D,"中山展售" , 8, 	180, 170, 9);  
			font = new Font(Font.DIALOG, Font.PLAIN, 8);
				 graphics2D.setFont(font);
				 yIndex = drawString(graphics2D,"機 "+(String)head.get(4) , 80, 180, 170, 9); 
				 
			font = new Font(Font.DIALOG, Font.PLAIN, 8);
				 graphics2D.setFont(font);
				 yIndex = drawString(graphics2D,"退貨憑電子發票證明聯正本辦理" , 3, 190, 170, 9);
			    
			    graphics2D.setColor(defaultColor);
			    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
			    graphics2D.setColor(grey);
			    yIndex = yIndex +  20;
			    //graphics2D.drawLine(0, yIndex, 140, yIndex);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
//	public void printItem(Graphics2D graphics2D,int yIndex,int startX,int startY,SoSalesOrderItem item) {
//		int font_height = 10;
//		int font_distence = 5;
//		try {
//			
//			Font font = new Font(Font.DIALOG, Font.BOLD, 14);
//	 		graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,item.getItemCode() , startX, startY, 80, font_height+font_distence);
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,item.getItemCode() , startX+100, startY, 80, font_height+font_distence);
//			
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,String.valueOf(item.getOriginalUnitPrice()) , startX, startY+15, 50, font_height+font_distence);
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,String.valueOf(item.getQuantity()) , startX+70, startY+15, 50, font_height+font_distence);
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,item.getActualSalesAmount()+" TX" , startX+140, startY+15, 50, font_height+font_distence);
//			
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,"DISCOUNT" , startX, startY+30, 80, font_height+font_distence);
//			font = new Font(Font.DIALOG, Font.BOLD, 14);
//			graphics2D.setFont(font);
//			yIndex = drawString(graphics2D,String.valueOf(item.getDiscountRate()) , startX+100, startY+30, 80, font_height+font_distence);
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public void starPrint() {
		System.out.println("starPrint....");
		try {
			PrinterJob prnJob = PrinterJob.getPrinterJob();
			
			List<String> printers = getPrinters();
			printers.forEach(s -> {
				System.out.println("printers name:"+s);
			});
			
			char[] SET_PRINT_AREA = new char[]{27,87,0,0,0,0,80,1,44,2};
			byte[] SET_PRINT_AREA_bytes = new String(SET_PRINT_AREA).getBytes("UTF-8");
			//-----------------------------------------------------------------------------------------------------------
			
//			String[] PRINTABLE_AREA = new String[] {Integer.toHexString(27),Integer.toHexString(87)
//					,Integer.toHexString(0),Integer.toHexString(0),Integer.toHexString(0),Integer.toHexString(0)
//					,Integer.toHexString(160),Integer.toHexString(1),Integer.toHexString(88),Integer.toHexString(2)};
//			StringBuilder sb = new StringBuilder();
//			sb.append(PRINTABLE_AREA[0]);
//			for(int i=1;i<PRINTABLE_AREA.length;i++) {
//				char[] chars = PRINTABLE_AREA[i].toCharArray();
//				sb.append(" ").append(chars);
//			}
//			byte[] PRINTABLE_AREA_bytes = sb.toString().getBytes("UTF-8");
			
			//-----------------------------------------------------------------------------------------------------------
//			
//			for(int i=0;i<SET_PRINT_AREA_bytes.length;i++) {
//				System.out.println(SET_PRINT_AREA_bytes[i]);
//			}
			
			this.setDefined_printer("EPSON TM-T82III Receipt");
			//printBytes(defined_printer, SET_PRINT_AREA_bytes); 
			printBytes(defined_printer, SET_PRINT_AREA_bytes);
			
			PageFormat pageFormat = new PageFormat();
			Paper p = new Paper(); 
			p.setSize(555.0, 802.0);//紙張 寬高
			p.setImageableArea(12.0, 19.0, 680.0, 883.0);//可印x y  可印寬可印高
			pageFormat.setPaper(p);
			//pageFormat.setOrientation(PageFormat.LANDSCAPE);
			
			prnJob.setPrintable(this, pageFormat);
			
//			prnJob.setPrintable(this);
			
			//彈出列印對話框，也可以選擇不彈出列印提示框，直接列印
			//if (!prnJob.printDialog()) return;
			
			prnJob.print();//啟動列印工作
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("列印錯誤：" + ex.toString());
		}
	}
	
	private PrintService findPrintService(String printerName,PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }
	
	/**
	* 字串輸出
	* @param graphics2D  畫筆
	* @param text     列印文字
	* @param x       列印起點 x 座標
	* @param y       列印起點 y 座標
	* @param lineWidth   行寬
	* @param lineHeight  行高
	* @return 返回終點 y 座標
	*/
	private static int drawString(Graphics2D graphics2D, String text, int x, int y, int lineWidth, int lineHeight){
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		//System.out.println(fontMetrics);
		if(fontMetrics.stringWidth(text)<lineWidth){
			graphics2D.drawString(text, x, y);
			return y;
		} else{
			char[] chars = text.toCharArray();
			int charsWidth = 0;
			StringBuffer sb = new StringBuffer();
			for (int i=0; i<chars.length; i++  ){
				if((charsWidth  + fontMetrics.charWidth(chars[i]))>lineWidth){
					graphics2D.drawString(sb.toString(), x, y);
					sb.setLength(0);
					y = y  + lineHeight;
					charsWidth = fontMetrics.charWidth(chars[i]);
					sb.append(chars[i]);
				} else{
					charsWidth = charsWidth  + fontMetrics.charWidth(chars[i]);
					sb.append(chars[i]);
				}
			}
			if(sb.length()>0){
				graphics2D.drawString(sb.toString(), x, y);
				y = y +  lineHeight;
			}
			return y - lineHeight;
		}
	}
    
    /**
     * 圖片輸出
     * @param graphics2D  底版
     * @param img     列印圖片
     * @param x       列印起點 x 座標
     * @param y       列印起點 y 座標
     * @param lineWidth   行寬
     * @param lineHeight  行高
     * @return 返回終點 y 座標
     */
     private static int drawImage(Graphics2D graphics2D, BufferedImage img, int x, int y, int lineWidth, int lineHeight){
      FontMetrics fontMetrics = graphics2D.getFontMetrics();
      //System.out.println(fontMetrics);
      //int width = img.getWidth();
      //int height = img.getHeight();
      graphics2D.drawImage(img, x, y, lineWidth, lineHeight, null);
      return y - lineWidth;
     }
                                                                                                        
     public void printBytes(String printerName, byte[] bytes) {
         DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
         PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
         PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
         PrintService service = findPrintService(printerName, printService);
         DocPrintJob job = service.createPrintJob();
         try {
             Doc doc = new SimpleDoc(bytes, flavor, null);
             job.print(doc, null);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    
     public static String bytesToHex(byte[] bytes) {  
    	    StringBuffer sb = new StringBuffer();  
    	    for(int i = 0; i < bytes.length; i++) {  
    	        String hex = Integer.toHexString(bytes[i] & 0xFF);  
    	        if(hex.length() < 2){  
    	            sb.append(0);  
    	        }  
    	        sb.append(hex);  
    	    }  
    	    return sb.toString();  
    }  
     
    public byte[] hexStringToByte(String hex) { 
    	 int len = (hex.length() / 2); 
    	 byte[] result = new byte[len]; 
    	 char[] achar = hex.toCharArray(); 
    	 for (int i = 0; i < len; i++) { 
	    	 int pos = i * 2; 
	    	 result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1])); 
    	 } 
    	 //System.out.println(Arrays.toString(result)); 
    	 return result; 
    } 
    
    private byte charToByte(char c) { 
    	//return (byte) "0123456789ABCDEF".indexOf(c); 
    	return (byte) "0123456789abcdef".indexOf(c); 
    } 
    
    public String string2Hex(String plainText, String charset) throws UnsupportedEncodingException {
    	return String.format("%040x", new BigInteger(1, plainText.getBytes(charset)));
    }
    
}
