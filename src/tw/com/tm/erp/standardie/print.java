package tw.com.tm.erp.standardie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;

public class print implements Printable {
	
	public int pageSize;//列印的總頁數
	private double paperW=0;//列印的紙張寬度
	private double paperH=0;//列印的紙張高度

	
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex >= pageSize) {
			System.err.println("退出列印");
			//退出列印
			return Printable.NO_SUCH_PAGE;
		}else {
			Graphics2D g2 = (Graphics2D) graphics;
			g2.setColor(Color.BLUE);
			Paper p = new Paper();
			//p.setImageableArea(0, 0, paperW, paperH);// 設置可列印區域
			p.setImageableArea(0, 0, 135, 450);
			p.setSize(140, 450);// 設置紙張的大小
			pageFormat.setPaper(p);
			drawCurrentPageText(g2,pageFormat);//調用列印內容的方法
			System.err.println("初始完成");
			return PAGE_EXISTS;
		}
	}
	
	// 列印內容
	private void drawCurrentPageText(Graphics2D g2, PageFormat pf) {
		 Font font = null;
		 //設置列印的字體
		 font = new Font("新細明體", Font.BOLD, 11);
		 g2.setFont(font);// 設置字體
		 //g2.drawString("列印測試列印測試列印測試列印測試列印測試列印測試列印測試列印測試列印測試列印測試列印測試",200,200);
	}
	
	public void starPrint() {
		try{
			SoSalesOrderHead head = new SoSalesOrderHead();
			head.setCustomerCode("CustomerCode");
			head.setGuiCode("GuiCode");
			head.setSuperintendentCode("T49674");
			head.setSalesOrderDate(new Date());
			
			List<SoSalesOrderItem> soSalesOrderItems = new ArrayList();
			SoSalesOrderItem item = new SoSalesOrderItem();
			item.setItemCName("123");
			item.setItemCode("123");
			item.setActualUnitPrice(12.0D);
			item.setQuantity(1.0D);
			SoSalesOrderItem item2 = new SoSalesOrderItem();
			item.setItemCName("321");
			item.setItemCode("321");
			item.setActualUnitPrice(32.0D);
			item.setQuantity(2.0D);
			soSalesOrderItems.add(item);
			soSalesOrderItems.add(item2);
			head.setSoSalesOrderItems(soSalesOrderItems);
		
			PrinterJob prnJob = PrinterJob.getPrinterJob();
			PageFormat pageFormat = new PageFormat();
			pageFormat.setOrientation(PageFormat.PORTRAIT);
			//prnJob.setPrintable(this);
			prnJob.setPrintable(new Printable() {
				    @Override
				    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				    	System.out.println("print");
					    if(pageIndex>0){
					    	System.out.println("NO_SUCH_PAGE");
					    	return NO_SUCH_PAGE;
					    }
					    Graphics2D graphics2D = (Graphics2D) graphics;
					    Font font = new Font("宋體", Font.PLAIN, 5);
					    graphics2D.setFont(font);
					    
					    Paper p = new Paper();
						p.setImageableArea(0, 0, 135, 450);
						p.setSize(140, 450);// 設置紙張的大小
						pageFormat.setPaper(p);
						
					    
					    drawString(graphics2D, "//////////////////////////////", 10, 17, 119, 8);
					    font = new Font("宋體", Font.PLAIN, 7);
					    graphics2D.setFont(font);
					    int yIndex = 30;
					    int lineHeight = 10;
					    int lineWidth = 120;
					    Color defaultColor = graphics2D.getColor();
					    Color grey = new Color(145, 145, 145);
					    //收貨資訊
					    yIndex = drawString(graphics2D,"customer_code："+head.getCustomerCode() , 10, yIndex, lineWidth, lineHeight);
					    //yIndex = drawString(graphics2D, "收貨地址：XX大廈", 10, yIndex +  lineHeight, lineWidth, lineHeight);
					    //收貨資訊邊框
					    Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,new float[]{4, 4},0);
					    graphics2D.setStroke(stroke);
					    graphics2D.drawRect(5, 10, 129, yIndex);
					    //藥店名稱
					    lineWidth = 129;
					    lineHeight = 8;
					    graphics2D.setFont(new Font("宋體", Font.BOLD, 8));
					    graphics2D.setColor(defaultColor);
					    yIndex = drawString(graphics2D, "invoice_no："+head.getGuiCode(), 5, yIndex  + lineHeight + 20, lineWidth, 12);
					    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
					    graphics2D.setColor(grey);
					    yIndex = drawString(graphics2D, "superintendent_code："+head.getSuperintendentCode(), 5, yIndex  + lineHeight  + 2, lineWidth, lineHeight);
					    yIndex = drawString(graphics2D, "sales_date："+head.getSalesOrderDate().toLocaleString(), 5 +  lineWidth/2, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "品名", 5, yIndex +  lineHeight * 2 - 5, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "規格", (lineWidth/10)*4, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "單價", (lineWidth/10)*8, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "數量", (lineWidth/10)*10, yIndex, lineWidth, lineHeight);
					    List<SoSalesOrderItem> items = head.getSoSalesOrderItems();
					    for (int i=0; i<items.size(); i ++ ){
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 7));
						    yIndex = drawString(graphics2D, items.get(i).getItemCode(), 5, yIndex +  15, (lineWidth/10)*7, 10);
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
						    graphics2D.setColor(grey);
						    yIndex = drawString(graphics2D, items.get(i).getItemCName(), 5, yIndex +  11, lineWidth, lineHeight);
						    yIndex = drawString(graphics2D, items.get(i).getQuantity().toString(), (lineWidth/10)*8, yIndex, lineWidth, lineHeight);
						    yIndex = drawString(graphics2D, items.get(i).getActualUnitPrice().toString(), (lineWidth/10)*10, yIndex, lineWidth, lineHeight);
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 7));
						    yIndex = yIndex +  2;
						    graphics2D.drawLine(5, yIndex, 5  + lineWidth, yIndex);
					    }
					    graphics2D.setColor(defaultColor);
					    //yIndex = drawString(graphics2D, "會員名稱：小清新", 5, yIndex  + lineHeight * 2, lineWidth, lineHeight);
					    /*yIndex = drawString(graphics2D, "總  數："+head.getTotalItemQuantity(), 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    yIndex = drawString(graphics2D, "總  計："+head.getTotalActualSalesAmount(), 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    List<SoSalesOrderPayment> soSalesOrderPayments = head.getSoSalesOrderPayments();
					    for(SoSalesOrderPayment soSalesOrderPayment:soSalesOrderPayments) {
					    	yIndex = drawString(graphics2D, "收  款："+soSalesOrderPayment.getPosPaymentType()+" , "+soSalesOrderPayment.getPayQty(), 5, yIndex  + lineHeight, lineWidth, lineHeight);
					    }*/
//					    yIndex = drawString(graphics2D, "找  零：44.70", 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
					    graphics2D.setColor(grey);
//					    yIndex = drawString(graphics2D, "電話：020-123456", 5, yIndex  + lineHeight * 2, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "地址：XX大廈", 5, yIndex  + lineHeight, lineWidth, lineHeight);
					    yIndex = yIndex +  20;
					    graphics2D.drawLine(0, yIndex, 140, yIndex);
					    return PAGE_EXISTS;
				    }
			    }, pageFormat);
			//彈出列印對話框，也可以選擇不彈出列印提示框，直接列印
			// if (!prnJob.printDialog())	return;
			 //獲取所連接的目標列印機的進紙規格的寬度，單位：1/72(inch)
			 paperW=prnJob.getPageFormat(null).getPaper().getWidth();
		 
			//獲取所連接的目標列印機的進紙規格的寬度，單位：1/72(inch)
			 paperH=prnJob.getPageFormat(null).getPaper().getHeight();
		 
			 prnJob.print();//啟動列印工作
			 System.err.println("列印完成：");
		} catch (PrinterException ex) {
			ex.printStackTrace();
			System.err.println("列印錯誤：" + ex.toString());
		 }
	}

	public static void main(String[] args) {
		print pm = new print();// 實例化列印類
		pm.pageSize = 2;//列印兩頁
		pm.starPrint();
		/*print pm = new print();// 實例化列印類
		pm.pageSize = 2;//列印
		
		SoSalesOrderHead head = new SoSalesOrderHead();
		head.setCustomerCode("CustomerCode");
		head.setGuiCode("GuiCode");
		head.setSuperintendentCode("T49674");
		head.setSalesOrderDate(new Date());
		
		List<SoSalesOrderItem> soSalesOrderItems = new ArrayList();
		SoSalesOrderItem item = new SoSalesOrderItem();
		item.setItemCName("123");
		item.setItemCode("123");
		item.setActualUnitPrice(12.0D);
		item.setQuantity(1.0D);
		SoSalesOrderItem item2 = new SoSalesOrderItem();
		item.setItemCName("321");
		item.setItemCode("321");
		item.setActualUnitPrice(32.0D);
		item.setQuantity(2.0D);
		soSalesOrderItems.add(item);
		soSalesOrderItems.add(item2);
		head.setSoSalesOrderItems(soSalesOrderItems);
		
		pm.doPrint(head);
		pm.starPrint();*/
	}
	
	public void doPrint(SoSalesOrderHead head) {
		try {
			
			if(PrinterJob.lookupPrintServices().length>0){
				/* 列印格式 */
				PageFormat pageFormat = new PageFormat();
				//設定列印起點從左上角開始，從左到右，從上到下列印
				pageFormat.setOrientation(PageFormat.PORTRAIT);
				/* 列印頁面格式設定 */
				Paper paper = new Paper();
				//設定列印寬度（固定，和具體的印表機有關）和高度（跟實際列印內容的多少有關）
				paper.setSize(140, 450);
				//設定列印區域 列印起點座標、列印的寬度和高度
				paper.setImageableArea(0, 0, 135, 450);
				pageFormat.setPaper(paper);
			    
			    Book book = new Book();
			    book.append(new Printable() {
				    @Override
				    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
					    if(pageIndex>0){
					    	return NO_SUCH_PAGE;
					    }
					    Graphics2D graphics2D = (Graphics2D) graphics;
					    Font font = new Font("宋體", Font.PLAIN, 5);
					    graphics2D.setFont(font);
					    drawString(graphics2D, "//////////////////////////////", 10, 17, 119, 8);
					    font = new Font("宋體", Font.PLAIN, 7);
					    graphics2D.setFont(font);
					    int yIndex = 30;
					    int lineHeight = 10;
					    int lineWidth = 120;
					    Color defaultColor = graphics2D.getColor();
					    Color grey = new Color(145, 145, 145);
					    //收貨資訊
					    yIndex = drawString(graphics2D,"customer_code："+head.getCustomerCode() , 10, yIndex, lineWidth, lineHeight);
					    //yIndex = drawString(graphics2D, "收貨地址：XX大廈", 10, yIndex +  lineHeight, lineWidth, lineHeight);
					    //收貨資訊邊框
					    Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,new float[]{4, 4},0);
					    graphics2D.setStroke(stroke);
					    graphics2D.drawRect(5, 10, 129, yIndex);
					    //藥店名稱
					    lineWidth = 129;
					    lineHeight = 8;
					    graphics2D.setFont(new Font("宋體", Font.BOLD, 8));
					    graphics2D.setColor(defaultColor);
					    yIndex = drawString(graphics2D, "invoice_no："+head.getGuiCode(), 5, yIndex  + lineHeight + 20, lineWidth, 12);
					    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
					    graphics2D.setColor(grey);
					    yIndex = drawString(graphics2D, "superintendent_code："+head.getSuperintendentCode(), 5, yIndex  + lineHeight  + 2, lineWidth, lineHeight);
					    yIndex = drawString(graphics2D, "sales_date："+head.getSalesOrderDate().toLocaleString(), 5 +  lineWidth/2, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "品名", 5, yIndex +  lineHeight * 2 - 5, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "規格", (lineWidth/10)*4, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "單價", (lineWidth/10)*8, yIndex, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "數量", (lineWidth/10)*10, yIndex, lineWidth, lineHeight);
					    List<SoSalesOrderItem> items = head.getSoSalesOrderItems();
					    for (int i=0; i<items.size(); i ++ ){
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 7));
						    yIndex = drawString(graphics2D, items.get(i).getItemCode(), 5, yIndex +  15, (lineWidth/10)*7, 10);
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
						    graphics2D.setColor(grey);
						    yIndex = drawString(graphics2D, items.get(i).getItemCName(), 5, yIndex +  11, lineWidth, lineHeight);
						    yIndex = drawString(graphics2D, items.get(i).getQuantity().toString(), (lineWidth/10)*8, yIndex, lineWidth, lineHeight);
						    yIndex = drawString(graphics2D, items.get(i).getActualUnitPrice().toString(), (lineWidth/10)*10, yIndex, lineWidth, lineHeight);
						    graphics2D.setFont(new Font("宋體", Font.PLAIN, 7));
						    yIndex = yIndex +  2;
						    graphics2D.drawLine(5, yIndex, 5  + lineWidth, yIndex);
					    }
					    graphics2D.setColor(defaultColor);
					    //yIndex = drawString(graphics2D, "會員名稱：小清新", 5, yIndex  + lineHeight * 2, lineWidth, lineHeight);
					    /*yIndex = drawString(graphics2D, "總  數："+head.getTotalItemQuantity(), 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    yIndex = drawString(graphics2D, "總  計："+head.getTotalActualSalesAmount(), 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    List<SoSalesOrderPayment> soSalesOrderPayments = head.getSoSalesOrderPayments();
					    for(SoSalesOrderPayment soSalesOrderPayment:soSalesOrderPayments) {
					    	yIndex = drawString(graphics2D, "收  款："+soSalesOrderPayment.getPosPaymentType()+" , "+soSalesOrderPayment.getPayQty(), 5, yIndex  + lineHeight, lineWidth, lineHeight);
					    }*/
//					    yIndex = drawString(graphics2D, "找  零：44.70", 5, yIndex +  lineHeight, lineWidth, lineHeight);
					    graphics2D.setFont(new Font("宋體", Font.PLAIN, 6));
					    graphics2D.setColor(grey);
//					    yIndex = drawString(graphics2D, "電話：020-123456", 5, yIndex  + lineHeight * 2, lineWidth, lineHeight);
//					    yIndex = drawString(graphics2D, "地址：XX大廈", 5, yIndex  + lineHeight, lineWidth, lineHeight);
					    yIndex = yIndex +  20;
					    graphics2D.drawLine(0, yIndex, 140, yIndex);
					    return PAGE_EXISTS;
				    }
			    }, pageFormat);
			    System.out.println("book append end");
			    //獲取預設印表機
			    PrinterJob printerJob = PrinterJob.getPrinterJob();
			    printerJob.setPageable(book);
			    System.out.println("printerJob.setPageable(book)");
			    try {
			    	printerJob.print();
			    	System.out.println("列印結束");
			    }catch (PrinterException e) {
			    	e.printStackTrace();
			    	System.out.println("列印異常");
			    }
			} else{
				System.out.println("沒法發現印表機服務");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

}
