package tw.com.tm.erp.closevalidation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class SoCloseValidation implements CloseValidationInterface {

    private static final Log log = LogFactory.getLog(SoCloseValidation.class);
    
    private static ApplicationContext context = SpringUtils
	    .getApplicationContext();

    public void executeValidate(String brandCode, Date closeDate,
	    List errorMsgs, String processName, Date executeDate, String processLotNo, String opUser) {

	doSalesOrderValidate(brandCode, closeDate, errorMsgs, processName, executeDate, processLotNo, opUser);
	doDeliveryValidate(brandCode, closeDate, errorMsgs, processName, executeDate, processLotNo, opUser);	
    }

    private void doSalesOrderValidate(String brandCode, Date closeDate,
	    List errorMsgs, String processName, Date executeDate, String processLotNo, String opUser) {

	String msg = null;
	try {
	    SoSalesOrderHeadDAO salesOrderHeadDAO = (SoSalesOrderHeadDAO) context
		    .getBean("soSalesOrderHeadDAO");
	    List<SoSalesOrderHead> salesOrderHeads = salesOrderHeadDAO
		    .findSalesOrderByCriteria(new String[]{brandCode}, closeDate,
			    new String[] { //OrderStatus.SIGNING,
				    //OrderStatus.UNCONFIRMED 
		    		  OrderStatus.UNKNOW 
		    });

	    if (salesOrderHeads != null){
		String identification = null;	
		for (SoSalesOrderHead salesOrderHead : salesOrderHeads) {
		    identification = MessageStatus.getIdentificationMsg(
			    salesOrderHead.getBrandCode(), salesOrderHead
				    .getOrderTypeCode(), salesOrderHead
				    .getOrderNo());
		    
		    msg = identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "無法執行關帳程序！";
		    errorMsgs.add(msg + "<br>");
		    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, executeDate, processLotNo, opUser);
		}
	    }else{
		throw new ValidationErrorException("查詢銷貨單主檔回傳結果為空值！");	
	    }
	} catch (Exception ex) {
	    msg = "檢核銷貨單是否能執行關帳程序時發生錯誤，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(msg + ex.getMessage() + "<br>");
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), executeDate, processLotNo, opUser);
	}
    }
    
    private void doDeliveryValidate(String brandCode, Date closeDate,
	    List errorMsgs, String processName, Date executeDate, String processLotNo, String opUser) {

	String msg = null;
	try {
	    ImDeliveryHeadDAO deliveryHeadDAO = (ImDeliveryHeadDAO) context
		    .getBean("imDeliveryHeadDAO");
	    List<ImDeliveryHead> deliveryHeads = deliveryHeadDAO
		    .findDeliveryByProperty(new String[]{brandCode}, closeDate,
			    new String[] { //OrderStatus.SIGNING,
				    //OrderStatus.SAVE
				    OrderStatus.UNKNOW
				    }, "IOU" );
	    
	    if (deliveryHeads != null){
		String identification = null;	
		for (ImDeliveryHead deliveryHead : deliveryHeads) {
		    identification = MessageStatus.getIdentificationMsg(
			    deliveryHead.getBrandCode(), deliveryHead
				    .getOrderTypeCode(), deliveryHead
				    .getOrderNo());
		    
		    msg = identification + "的狀態為" + OrderStatus.getChineseWord(deliveryHead.getStatus()) + "無法執行關帳程序！";
		    errorMsgs.add(msg + "<br>");
		    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, executeDate, processLotNo, opUser);
		}
	    }else{
		throw new ValidationErrorException("查詢出貨單主檔回傳結果為空值！");	
	    }
	} catch (Exception ex) {
	    msg = "檢核出貨單是否能執行關帳程序時發生錯誤，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(msg + ex.getMessage() + "<br>");
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), executeDate, processLotNo, opUser);
	}
    }
}
