package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderPaymentDAO;

public class SoSalesOrderPaymentService {

    private static final Log log = LogFactory
	    .getLog(SoSalesOrderPaymentService.class);
    
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    
    private SoSalesOrderPaymentDAO soSalesOrderPaymentDAO;

    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
    
    public void setSoSalesOrderPaymentDAO(SoSalesOrderPaymentDAO soSalesOrderPaymentDAO) {
	this.soSalesOrderPaymentDAO = soSalesOrderPaymentDAO;
    }

    public void deleteSalesOrderPayment(SoSalesOrderHead salesOrderHead) throws ValidationErrorException{
    
        try{
            SoSalesOrderHead salesOrderHeadPO = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, salesOrderHead.getHeadId());
            if(salesOrderHeadPO == null){
    	        throw new ValidationErrorException("查無銷貨單主鍵：" + salesOrderHead.getHeadId() + "的資料！");
    	    }
            salesOrderHeadPO.setSoSalesOrderPayments(new ArrayList<SoSalesOrderPayment>(0));
            soSalesOrderHeadDAO.update(salesOrderHeadPO);
        }catch (Exception ex) {
	    log.error("刪除付款明細時發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException("刪除付款明細失敗！");
	}
    }
}
