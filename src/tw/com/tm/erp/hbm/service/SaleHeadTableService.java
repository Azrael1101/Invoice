package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.bean.Sale;
import tw.com.tm.erp.hbm.bean.SaleheadTable;
import tw.com.tm.erp.hbm.dao.PosMachineNumberControllerDAO;
import tw.com.tm.erp.hbm.dao.SaleHeadTableDAO;
import tw.com.tm.erp.test.EInvoiceCheck;
import tw.com.tm.erp.utils.DateUtils;

public class SaleHeadTableService {
	
	public static void main(String[] args) {
		try {
			                                                                                      
		}catch(Exception e) {
			e.printStackTrace();		
		}
	}

	private SaleHeadTableDAO saleHeadTableDAO;
	public void setSaleHeadTableDAO(SaleHeadTableDAO saleHeadTableDAO) {
		this.saleHeadTableDAO = saleHeadTableDAO;
	}
	
//	public List<SaleheadTable> getAllSale(Long transactionSno) {
//		List<SaleheadTable> rtnList = null;
//		try {
//			rtnList = saleHeadTableDAO.getSaleHeadTableByTransactionSno(transactionSno);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return rtnList;
//	}
	
	public SaleheadTable getSaleByTransactionSno(Long transactionSno) {
		SaleheadTable saleHeadTable = null;
		try {
			saleHeadTable = saleHeadTableDAO.getSaleHeadTableByTransactionSno(transactionSno);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return saleHeadTable;
	}
	
	public void updateSaleHeadTable(SaleheadTable saleHeadTable) {
		try {
			saleHeadTableDAO.update(saleHeadTable);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
