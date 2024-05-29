package tw.com.tm.erp.hbm.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.bean.Transaction;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;

public class TransactionDAO{
	
	private static final Log log = LogFactory.getLog(TransactionDAO.class);
	
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}
	private SoSalesOrderItemDAO soSalesOrderItemDAO;
	public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
		this.soSalesOrderItemDAO = soSalesOrderItemDAO;
	}
	private SoSalesOrderPaymentDAO soSalesOrderPaymentDAO;
	public void setSoSalesOrderPaymentDAO(SoSalesOrderPaymentDAO soSalesOrderPaymentDAO) {
		this.soSalesOrderPaymentDAO = soSalesOrderPaymentDAO;
	}
	
	public Transaction findTransactionByHeadId(Class findClass, Long headId) throws Exception {
		log.info("findTransactionByHeadId....");
		Transaction transaction = null;
		SoSalesOrderHead soSalesOrderHead = null;
		SoSalesOrderItem soSalesOrderItem = null;
		SoSalesOrderPayment soSalesOrderPayment = null;
		try {
			if(findClass == SoSalesOrderHead.class) {
				soSalesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(findClass, headId);
				transaction.setSoSalesOrderHead(soSalesOrderHead);
			}else if (findClass == SoSalesOrderItem.class) {
				soSalesOrderItem = (SoSalesOrderItem) soSalesOrderItemDAO.findByPrimaryKey(findClass, headId);
				transaction.setSoSalesOrderItem(soSalesOrderItem);
			}else if(findClass == SoSalesOrderPayment.class) {
				soSalesOrderPayment = (SoSalesOrderPayment) soSalesOrderPaymentDAO.findByPrimaryKey(findClass, headId);
				transaction.setSoSalesOrderPayment(soSalesOrderPayment);
			}
		} catch (Exception ex) {
		    log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因："+ ex.getMessage());
		}
		 return transaction;
	}
	
	public void saveTransaction(Class findClass, Transaction transaction) throws Exception {
		log.info("saveTransaction.....");
		try {
			if(findClass == SoSalesOrderHead.class) {
				soSalesOrderHeadDAO.save(transaction.getSoSalesOrderHead());
			}else if (findClass == SoSalesOrderItem.class) {
				soSalesOrderItemDAO.save(transaction.getSoSalesOrderItem());
			}else if(findClass == SoSalesOrderPayment.class) {
				soSalesOrderPaymentDAO.save(transaction.getSoSalesOrderPayment());
			}
			
		} catch (Exception ex) {
		     ex.toString();
		}
	}
	
}
