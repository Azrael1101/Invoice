package tw.com.tm.erp.balance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.service.ImDeliveryService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class SoDailyBalance implements DailyBlanceInterface {
    
    private static final Log log = LogFactory.getLog(SoDailyBalance.class);
    
    private static ApplicationContext context = SpringUtils.getApplicationContext();
    
    public String performBalance(String[] brandCodeArray, Date balanceDate, List errorMsgs,
    		String processName, String processLotNo, String opUser, Date startDate, Date endDate){
    	System.out.println("SoDailyBalance starting...");
    	String returnMessage = MessageStatus.SUCCESS;
    	String msg = null;
    	try{
    		ImDeliveryHeadDAO imDeliveryHeadDAO = (ImDeliveryHeadDAO) context.getBean("imDeliveryHeadDAO");
    		ImDeliveryService imDeliveryService = (ImDeliveryService) context.getBean("imDeliveryService");
    		String identification = null;
    		for(int i = 0; i < brandCodeArray.length; i++){
    			//出貨單結算
    			boolean isLoop = true;
    			List<ImDeliveryHead> imDeliveryHeads = new ArrayList(0);
    			while(isLoop){
	    			imDeliveryHeads = imDeliveryHeadDAO.findDeliveryByProperty(brandCodeArray[i], OrderStatus.FINISH, "IOU",startDate, endDate);
	    			if(imDeliveryHeads != null && imDeliveryHeads.size() > 0){
	    				for(int j = 0; j < imDeliveryHeads.size(); j++){
	    					ImDeliveryHead deliveryHead = null;
	    					try{
	    						deliveryHead = (ImDeliveryHead)imDeliveryHeads.get(j);
	    						identification = MessageStatus.getIdentificationMsg(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
	    						imDeliveryService.executeDailyBalance(deliveryHead, opUser);
	    					}catch(Exception ex){
	    						msg = identification + "執行結算時發生錯誤，原因：";
	    						log.error(msg + ex.toString());
	    						errorMsgs.add(msg + ex.getMessage() + "<br>");
	    						BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
	    						returnMessage = MessageStatus.ERROR;
	    					}
	    				}	
	    				isLoop = imDeliveryHeads.size()>= imDeliveryHeadDAO.MAX_DAILY_BALANCE_RECORD;
	    			}else{
	    				isLoop= false;
	    			}
    			}
    			//退貨單結算
    			isLoop = true;
    			while(isLoop){
	    			imDeliveryHeads = imDeliveryHeadDAO.findDeliveryByProperty(brandCodeArray[i], OrderStatus.FINISH, "IR",startDate, endDate);
	    			if(imDeliveryHeads != null && imDeliveryHeads.size() > 0){
	    				for(int k = 0; k < imDeliveryHeads.size(); k++){
	    					ImDeliveryHead deliveryHead = null;
	    					try{
	    						deliveryHead = (ImDeliveryHead)imDeliveryHeads.get(k);
	    						identification = MessageStatus.getIdentificationMsg(deliveryHead.getBrandCode(), deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
	    						imDeliveryService.executeDailyBalanceForReturn(deliveryHead, opUser);
	    					}catch(Exception ex){
	    						msg = identification + "執行結算時發生錯誤，原因：";
	    						log.error(msg + ex.toString());
	    						errorMsgs.add(msg + ex.getMessage() + "<br>");
	    						BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
	    						returnMessage = MessageStatus.ERROR;
	    					}
	    				}	
	    				isLoop = imDeliveryHeads.size()>= imDeliveryHeadDAO.MAX_DAILY_BALANCE_RECORD;
	    			}else{
	    				isLoop= false;
	    			}
    			}
    		}    
    		System.out.println("SoDailyBalance finish...");
    		return returnMessage;
    	}catch(Exception ex){
    		msg = "執行銷售模組結算時發生錯誤，原因：";
    		log.error(msg + ex.toString());
    		errorMsgs.add(msg + ex.getMessage() + "<br>");
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
    		returnMessage = MessageStatus.ERROR;
    		return returnMessage;
    	}finally{
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "銷貨模組結算作業完成！", balanceDate, processLotNo, opUser);        
    	}	
    }
}
