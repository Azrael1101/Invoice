package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.com.tm.erp.hbm.bean.PosMachineNumberController;
import tw.com.tm.erp.hbm.bean.Sale;
import tw.com.tm.erp.hbm.dao.PosMachineNumberControllerDAO;
import tw.com.tm.erp.hbm.dao.SaleDAO;
import tw.com.tm.erp.test.EInvoiceCheck;
import tw.com.tm.erp.utils.DateUtils;

public class SaleService {
	
	public static void main(String[] args) {
		try {
			
		}catch(Exception e) {
			e.printStackTrace();		
		}
	}

	private SaleDAO saleDAO;
	public void setSaleDAO(SaleDAO saleDAO) {
		this.saleDAO = saleDAO;
	}
	
	public List<Sale> getAllSale() {
		List<Sale> rtnList = null;
		try {
			rtnList = saleDAO.getSale();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rtnList;
	}
	
}
