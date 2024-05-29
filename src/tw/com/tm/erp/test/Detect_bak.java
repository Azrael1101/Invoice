package tw.com.tm.erp.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.print.Paper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import tw.com.tm.erp.utils.DateUtils;

import org.apache.commons.lang.StringUtils;

import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.dao.PosMachineNumberControllerDAO;
import tw.com.tm.erp.hbm.service.PosMachineNumberService;
import tw.com.tm.erp.standardie.print;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Detect_bak implements Runnable {
	
	private PrintConnect printConnect;
	private PosMachineNumberService posMachineNumberService;
	private PosMachineNumberControllerDAO posMachineNumberControllerDAO;
	
	public Detect_bak(PrintConnect printConnect,PosMachineNumberService posMachineNumberService,PosMachineNumberControllerDAO posMachineNumberControllerDAO) {
		System.out.println(" initial Detect() ...");
		this.printConnect = printConnect;
		this.posMachineNumberService = posMachineNumberService;
		this.posMachineNumberControllerDAO = posMachineNumberControllerDAO;
		System.out.println(" getting printConnect ");
	}
	
	public void run() {
			Connection conn = this.printConnect.getSaleConnection();
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			PreparedStatement pstmt3 = null;
			PreparedStatement pstmt4 = null;

		while (true) {
			try {
				String saleOrderNo = "";
				
				ResultSet ts = conn.prepareStatement(
						"select seq_no from tasameng.sale"
						).executeQuery();
				while(ts.next()) {
					saleOrderNo = String.valueOf(ts.getString(1));
				}
				ResultSet rs = conn.prepareStatement(                                                                                                                         
						"select count(*) from tasameng.salehead_table where transaction_sno ="+
						" '"+saleOrderNo+"' ;"
						).executeQuery();
				
				ResultSet detail = conn.prepareStatement(
						"select c.FULL_DESC, b.sales_order_date, a.customer_po_no, a.transaction_time, a.pos_machine_code,"+
						" a.total_actual_sales_amt, a.total_sales_qty, b.sales_qty, b.tax_sale_prc, a.shop_code, a.status"+
						" from tasameng.salehead_table a"+
						" left outer join tasameng.saleitem_table b on a.head_sno = b.head_sno"+
						" left outer join tasameng.plumst_table c on b.item_code = c.id where a.transaction_sno ="+
						" '"+saleOrderNo+"' and a.print_status is null ;").executeQuery();
				ResultSet invoice = conn.prepareStatement(
						"select * from tasameng.pos_machine_number"
						).executeQuery();
				
				try {
					//ElectronicInvoiceResponseBean electronicInvoiceResponseBean = HttpRequestTest.getInvoiceNumber("A01", "99"); //Brian取發票號碼
					List printData = new ArrayList();
					StringBuffer saleItems = new StringBuffer();
					
					String invoiceNum = ""; 
					String invoiceNum2 = "";
					String nextInvoiceNum = "";
					Long   currentNum = null;
					Long   endNum = null;
					String invoiceStart = "";
					String invoiceHeader = "";
					String invoiceEnd = "";
					String itemCName = "";
					String SDate = "";
					String minguoDate = "";
					String transactionTime = "";
					String posMachineCode = "99";
					String totalActualSalesAmt = "";
					String totalActualSalesQty = "";
					String random = "";//隨機碼
					String saleAmount = "";//銷售額
					String totalSaleAmpunt = "";//總計額
					String vatNumC = "";//顧客統編
					String vatNumB = "";//采盟統編
					String key = "";
					String QrcodeL = "";
					String QrcodeR = "";
					String item = "";
					String sales_qty = "";
					String tax_sale_prc = "";
					String varNum = "";
					String shopCode = "";
					String status = "";
					
					if(saleOrderNo != null && !saleOrderNo.equals("")) {
					System.out.println("取號Waiting...");
					
					//boolean isAvaliable = posMachineNumberService.checkAvaliableNumberInvoice(posMachineCode,"SALE");
					//String useableInvoice = posMachineNumberService.getAvaliableInvoiceNumber(posMachineCode,"SALE");
					
						String taxYearMonth= "";
						String sys= "Y";
						String sysDateStr = DateUtils.format(DateUtils.getCurrentDate(),DateUtils.C_DATA_PATTON_YYYYMMDD);
						taxYearMonth = sysDateStr.substring(0,6);
						
						taxYearMonth = String.valueOf((Long.valueOf(taxYearMonth)+1)%2==0?(Long.valueOf(taxYearMonth)+1):0);
						
						taxYearMonth="11102";
						System.out.println("taxYearMonth:"+taxYearMonth);
						// 取得 本機機台號碼 當期別 啟用狀態為 Y 之可用區間號碼 
						List<PosMachineNumberController> allAvaliableInvoice = new ArrayList<PosMachineNumberController>();
						allAvaliableInvoice = posMachineNumberControllerDAO.getAvaliableInvoiceNumber(posMachineCode, taxYearMonth, sys);
						//有號碼區間
						if(allAvaliableInvoice != null && allAvaliableInvoice.size()>0) {
							//FIFO 先進先出
							PosMachineNumberController avaliableInvoice = allAvaliableInvoice.get(0);
							invoiceStart = avaliableInvoice.getInvoiceStart();
							invoiceEnd = avaliableInvoice.getInvoiceEnd();
							invoiceNum = posMachineNumberService.updateAvaliableInvoiceNumber(posMachineCode,"SALE");
							invoiceHeader = avaliableInvoice.getInvoiceHeader();
							//可用數量
							currentNum = Long.valueOf(invoiceEnd) - Long.valueOf(invoiceNum) ;
						}
					
//					while (invoice.next()) {
//						invoiceNum = String.valueOf(invoice.getString(2));
//						
//						invoiceStart = String.valueOf(invoice.getString(4));
//							System.out.println("起始號碼:"+invoiceStart);
//						invoiceEnd = String.valueOf(invoice.getString(5));
//							System.out.println("結束號碼:"+invoiceEnd);
//							
//						endNum = Long.parseLong(invoiceEnd.substring(2, 10));
//						currentNum = Long.parseLong(invoiceNum.substring(2, 10));
//						System.out.println("目前號碼:"+currentNum);
//						//發票號碼庫存量檢查
//						EInvoiceCheck.checkInvoiceNumber(currentNum,endNum,"SALE");
//					}
					while (rs.next()) {
						while(detail.next()) {
							
							int size = rs.getInt(1);
							//String schedule = String.valueOf(rs.getInt(5));
							itemCName = String.valueOf(detail.getString(1));
							SDate = new SimpleDateFormat("yyyy-MM-dd").format(detail.getTimestamp(2));
							minguoDate = transferADDateToMinguoDate(SDate);
							//String invoiceNum = electronicInvoiceResponseBean.getInvoiceHead()+"-"+electronicInvoiceResponseBean.getInvoiceNumber();
							transactionTime = String.valueOf(detail.getString(4)); 
							//String invoiceNum2 = electronicInvoiceResponseBean.getInvoiceHead()+electronicInvoiceResponseBean.getInvoiceNumber();
							//String invoiceNum2 = invoiceNum.substring(0, 2)+"-"+invoiceNum.substring(2, 10);
							//System.out.println(invoiceNum2);
							posMachineCode = String.valueOf(detail.getString(5));
							totalActualSalesAmt = String.valueOf(detail.getInt(6));
							totalActualSalesQty = String.valueOf(detail.getInt(7));
							sales_qty = String.valueOf(detail.getInt(8));
							tax_sale_prc = String.valueOf(detail.getInt(9));
							shopCode = String.valueOf(detail.getString(10));
							status  = String.valueOf(detail.getString(11));
							
							random = randomNum();//隨機碼
							saleAmount = String.format("%08d", Long.parseLong(totalActualSalesAmt));//銷售額
							totalSaleAmpunt = String.format("%08d", Long.parseLong(totalActualSalesAmt));//總計額
							vatNumC = "00000000";//顧客統編
							vatNumB = "12371287";//采盟統編
							key = "ydXZt4LAN1U HN/j1juVcRA==";	
							
							//String[] items = new String[3];
							for(int i=0;i<size;i++) {
								item = ":"+itemCName+":"+sales_qty+":"+tax_sale_prc;
							}
							String items = StringUtils.removeStart(item, ":");
							saleItems.append(items);
						}	
						//System.out.println(saleItems);
					}
					
					QrcodeL = invoiceNum+minguoDate+random+saleAmount+totalSaleAmpunt+vatNumC+vatNumB+key+
							":"+"**********"+":"+totalActualSalesQty+":"+totalActualSalesQty+":1:";
					QrcodeR = String.valueOf(saleItems);
						System.out.println(QrcodeL);
						System.out.println(QrcodeR);
					//-------------存發票號碼--------
						//varNum = invoiceNum.substring(0,2);
						nextInvoiceNum = String.valueOf(invoiceNum);
//						System.out.println("存入號碼："+nextInvoiceNum);
						invoiceNum2 = invoiceHeader+"-"+nextInvoiceNum;
						
						printData.add(itemCName); //0
						printData.add(SDate); //1
						printData.add(transactionTime); //2
						printData.add(minguoDate); //3
						printData.add(posMachineCode); //4
						printData.add(nextInvoiceNum); //5
						printData.add(invoiceNum2); //6
						printData.add(QrcodeL); //7
						printData.add(random); //8
						printData.add(totalActualSalesAmt); //9
						printData.add(QrcodeR); //10
						//System.out.println(printData);
						
						
							PrintInvoice print = new PrintInvoice();
							
							print.pageSize = 1;//列印兩頁
							print.head = printData;
							print.starPrint();

							PosMachineNumberController avaliableInvoice = allAvaliableInvoice.get(0);
							avaliableInvoice.setCurrentUse("0000"+invoiceNum);
							posMachineNumberControllerDAO.update(avaliableInvoice);
							System.out.println("存入 PosMachineNumberController setCurrentUse 成功 "+invoiceNum);
//							pstmt = conn.prepareStatement("update tasameng.pos_machine_number set current_use = '"+nextInvoiceNum + "' where invoice_sno = 1"); 
//							pstmt.executeUpdate();
//							System.out.println("存入成功");
							//-----------------------------
							pstmt2 = conn.prepareStatement("update tasameng.salehead_table set customer_po_no = '"+nextInvoiceNum+"',print_status='FINISH' where transaction_sno = '"+saleOrderNo+"'");
							pstmt2.executeUpdate();
							System.out.println("存入salehead_table成功");
							//-----------------------------
//							pstmt3 = conn.prepareStatement("update tasameng.invoice_table set print='Y' where seq_no='"+
//									saleOrderNo+"'"
//									);
//							pstmt3.executeUpdate();
							//-----------------------------
//							pstmt4 = conn.prepareStatement("delete from tasameng.sale where seq_no = '"+saleOrderNo+"'");
//							pstmt4.executeUpdate();
							//-----------------------------
							
							
							System.out.println(" ending print ...");
							System.out.println(" wait for print ...2sec");
						}
				} catch(Exception ex) {
					ex.printStackTrace();
				}  finally {
					if(rs != null) {
						rs.close();
					}
					if(detail != null) {
						detail.close();
					}
				}
				
				Thread.sleep(10000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String transferADDateToMinguoDate(String dateString) {
		LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return MinguoDate.from(localDate).format(DateTimeFormatter.ofPattern("yyyMMdd"));
	}
	
	public static String randomNum() {
		
		String str="0123456789";
		StringBuilder sb=new StringBuilder(4);
		for(int i=0;i<4;i++)
		{
		     char ch=str.charAt(new Random().nextInt(4));
		     sb.append(ch);
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}

	
}
