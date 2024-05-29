package tw.com.tm.erp.hbm.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.bean.Sale;
import tw.com.tm.erp.hbm.bean.SaleheadTable;
import tw.com.tm.erp.hbm.dao.PosMachineNumberControllerDAO;
import tw.com.tm.erp.test.EInvoiceCheck;
import tw.com.tm.erp.test.PrintInvoice;
import tw.com.tm.erp.utils.DateUtils;

public class PosMachineNumberService {
	
	public static void main(String[] args) {
		try {
			SaleheadTable saleHeadTable = new SaleheadTable();
			Method [] methods = saleHeadTable.getClass().getMethods();
			List<Method> mods = Arrays.asList(methods);
			mods.forEach(m->{
				System.out.println(m.getName());
			});
			                             
		}catch(Exception e) {
			e.printStackTrace();		
		}
	}

	private PosMachineNumberControllerDAO posMachineNumberControllerDAO;
	public void setPosMachineNumberControllerDAO(PosMachineNumberControllerDAO posMachineNumberControllerDAO) {
		this.posMachineNumberControllerDAO = posMachineNumberControllerDAO;
	}
	private SaleService saleService;
	public void setSaleService(SaleService saleService) {
		this.saleService = saleService;
	}
	private SaleHeadTableService saleHeadTableService;
	public void setSaleHeadTableService(SaleHeadTableService saleHeadTableService) {
		this.saleHeadTableService = saleHeadTableService;
	}

	public void DetectLocalSale() {
		try {
			//get sale
			List<Sale> allSales = saleService.getAllSale();
			if(allSales != null && allSales.size()>0) {
				Sale sale = allSales.get(0);
				// sale to salehead find
				SaleheadTable saleHeadTable = saleHeadTableService.getSaleByTransactionSno(sale.getSeqNo());
				if(saleHeadTable != null ) {
					String posMachineCode = saleHeadTable.getPosMachineCode();
					//need to print get Invoice Number
					
					// get current invoice number
					PosMachineNumberController posMachineNumberController = getNextInvoiceNumber(posMachineCode);
					String nextInvoiceNumber = combineInvoice(posMachineNumberController);
					//printer call saleHead & next invoice number
					PrintInvoice print = new PrintInvoice();
					//set customerPoNo
					saleHeadTable.setCustomerPoNo(nextInvoiceNumber);
					//set random number to saleHeadTable
					
					//call printer
					print.pageSize = 1;//列印兩頁
					print.setSaleHeadTable(saleHeadTable);
					print.starPrint();
					
					//rewrite salehead
					saleHeadTable.setReceiverMemo("");
					saleHeadTable.setBrandCode("");
					saleHeadTable.setAreaCode("");
					saleHeadTable.setOrderTypeCode("");
					saleHeadTable.setTaxType("");
					saleHeadTable.setTaxRate(0.0D);
					saleHeadTable.setInvoiceNo("");
					saleHeadTable.setInvoiceAddress("");
					saleHeadTable.setReceiverTele("");
					saleHeadTable.setTransportContent("");
					saleHeadTableService.updateSaleHeadTable(saleHeadTable);
					
					//rewrite posMachineNumberController
					posMachineNumberController.setCurrentUse(nextInvoiceNumber.substring(3));
					updatePosMachineNumberController(posMachineNumberController);
					
					//check this taxYearMonth
					checkLocalInvoice(posMachineCode,false,"SALE");
				}
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void CheckInvoice() {
		try {
			String posMachineCode = "";
			//check next taxYearMonth
			if(checkLocalInvoice(posMachineCode,true,"SALE")) {
				
				//check this taxYearMonth
				checkLocalInvoice(posMachineCode,false,"SALE");
			}else {
				//call get Number
				EInvoiceCheck.callGetInvoiceNumber(posMachineCode, "A01", "SALE");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean checkLocalInvoice(String posMachineCode,boolean isNextTaxYearMonth,String executeWay) {
		boolean isAvaliable = false;
		try {
			String taxYearMonth = getTaxYearMonth(isNextTaxYearMonth);
			String status= "Y";
			
			// 取得 本機機台號碼 期別 啟用狀態為 Y 之可用區間號碼 
			List<PosMachineNumberController> allAvaliableInvoice = new ArrayList<PosMachineNumberController>();
			allAvaliableInvoice = posMachineNumberControllerDAO.getAvaliableInvoiceNumber(posMachineCode, taxYearMonth, status);
			System.out.println("allAvaliableInvoice::"+allAvaliableInvoice);
			
			//有號碼區間
			if(allAvaliableInvoice != null && allAvaliableInvoice.size()>0) {
				isAvaliable = true;
			} else {
				isAvaliable = false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isAvaliable;
	}
	
	public PosMachineNumberController getNextInvoiceNumber(String posMachineCode) {
		PosMachineNumberController posMachineNumberController = null;
		try {
			String taxYearMonth = getTaxYearMonth(false);
			String status= "Y";
			PosMachineNumberController nextPosMachineNumberController = posMachineNumberControllerDAO.getNextInvoiceNumber(posMachineCode, taxYearMonth, status);
			if(nextPosMachineNumberController != null ) {
				posMachineNumberController = nextPosMachineNumberController;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return posMachineNumberController;
	}
	
	public String combineInvoice(PosMachineNumberController nextPosMachineNumberController) {
		String rtnInvoiceNumber = "";
		try {
			rtnInvoiceNumber += nextPosMachineNumberController.getInvoiceHeader();
			rtnInvoiceNumber += "-";
			rtnInvoiceNumber += String.format("%08d", nextPosMachineNumberController.getCurrentUse());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rtnInvoiceNumber;
	}
	
	public String updateAvaliableInvoiceNumber(String posMachineCode,String executeWay) {
		String useableInvoice = "0";
		try {
			if(checkAvaliableNumberInvoice(posMachineCode,executeWay)) {
				String taxYearMonth= "";
				String status= "Y";
				String twDate = DateUtils.formatToTWDate(DateUtils.getCurrentDate(), DateUtils.C_DATA_PATTON_YYYYMMDD);
				taxYearMonth = twDate.substring(0, 5);
				
				// 取得 本機機台號碼 當期別 啟用狀態為 Y 之可用區間號碼 
				List<PosMachineNumberController> allAvaliableInvoice = posMachineNumberControllerDAO.getAvaliableInvoiceNumber(posMachineCode, taxYearMonth, status);
				System.out.println("allAvaliableInvoice:"+allAvaliableInvoice.size());
				//FIFO 先進先出
				PosMachineNumberController avaliableInvoice = allAvaliableInvoice.get(0);
				String currentUse = avaliableInvoice.getCurrentUse();
				System.out.println("currentUse:"+currentUse);
				useableInvoice = String.valueOf(Long.valueOf(currentUse) + 1L);
				
				//update current use
				avaliableInvoice.setCurrentUse(String.format("%08d", useableInvoice));
				posMachineNumberControllerDAO.update(avaliableInvoice);
				
				//check
				checkAvaliableNumberInvoice(posMachineCode,executeWay);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return useableInvoice;
	}
	
	public boolean checkAvaliableNumberInvoice(String posMachineCode,String executeWay) {
		System.out.println("checkAvaliableNumberInvoice");
		boolean isAvaliable = true;
		try {
			String taxYearMonth= "";
			String status= "Y";
			String twDate = DateUtils.formatToTWDate(DateUtils.getCurrentDate(), DateUtils.C_DATA_PATTON_YYYYMMDD);
			taxYearMonth = twDate.substring(0, 5);
			
			// 取得 本機機台號碼 當期別 啟用狀態為 Y 之可用區間號碼 
			List<PosMachineNumberController> allAvaliableInvoice = new ArrayList<PosMachineNumberController>();
			allAvaliableInvoice = posMachineNumberControllerDAO.getAvaliableInvoiceNumber(posMachineCode, taxYearMonth, status);
			System.out.println("allAvaliableInvoice::"+allAvaliableInvoice);
			//有號碼區間
			if(allAvaliableInvoice != null && allAvaliableInvoice.size()>0) {
				//FIFO 先進先出
				PosMachineNumberController avaliableInvoice = allAvaliableInvoice.get(0);
				//Long invoiceStart = Long.valueOf(avaliableInvoice.getInvoiceStart());
				Long invoiceEnd = Long.valueOf(avaliableInvoice.getInvoiceEnd());
				Long currentUse = Long.valueOf(avaliableInvoice.getCurrentUse());
				System.out.println("invoiceEnd::"+invoiceEnd);
				System.out.println("currentUse::"+currentUse);
				//可用數量
				Long avaliableNum = invoiceEnd - currentUse ;
				System.out.println("avaliableNum::"+avaliableNum);
				if(avaliableNum < 20 && allAvaliableInvoice.size() < 1 ) {
					isAvaliable = false;
					//call get Number
					EInvoiceCheck.callGetInvoiceNumber(posMachineCode, "A01", executeWay);
				}
			} else {
				isAvaliable = false;
				//call get Number 
				EInvoiceCheck.callGetInvoiceNumber(posMachineCode, "A01", executeWay);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isAvaliable;
	}
	
	public static String getTaxYearMonth(boolean isNextMonth) {
		String returnTaxYearMonth = "";
		try {
			String twDate = DateUtils.formatToTWDate(DateUtils.getCurrentDate(), DateUtils.C_DATA_PATTON_YYYYMMDD);
			String taxYearMonthStr = "";
			Long texYearMonthLong = Long.valueOf(twDate.substring(0, 5));
			
			if(texYearMonthLong%2==0) {
				taxYearMonthStr = String.format("%05d", texYearMonthLong);
			}else {
				taxYearMonthStr = String.format("%05d", texYearMonthLong+1);
			}
			
			if(isNextMonth) {
				taxYearMonthStr = String.valueOf(Long.valueOf(taxYearMonthStr)+2);
			}
			System.out.println(taxYearMonthStr);
			returnTaxYearMonth = taxYearMonthStr;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return returnTaxYearMonth;
	}
	
	public void updatePosMachineNumberController(PosMachineNumberController posMachineNumberController) {
		try {
			posMachineNumberControllerDAO.update(posMachineNumberController);
		}catch(Exception e) {
			e.printStackTrace();
		}
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
