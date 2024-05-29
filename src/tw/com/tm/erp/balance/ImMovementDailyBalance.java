package tw.com.tm.erp.balance;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class ImMovementDailyBalance implements DailyBlanceInterface {
	private static final Log log = LogFactory.getLog(ImMovementDailyBalance.class);
    
    private static ApplicationContext context = SpringUtils.getApplicationContext();
    
    public String performBalance(String[] brandCodeArray, Date balanceDate, List errorMsgs,
    		String processName, String processLotNo, String opUser, Date startDate, Date endDate){
    	System.out.println("ImMovementDailyBalance starting...");
    	String returnMessage = MessageStatus.SUCCESS;
    	String msg = null;
    	try{
    		ImMovementHeadDAO imMovementHeadDAO = (ImMovementHeadDAO) context.getBean("imMovementHeadDAO");
    		ImMovementService imMovementService = (ImMovementService) context.getBean("imMovementService");
    		String identification = null;
    		for(int i = 0; i < brandCodeArray.length; i++){
    			//出貨單結算	
    			List<ImMovementHead> imMovementHeads = imMovementHeadDAO.findMovementByProperty(brandCodeArray[i], OrderStatus.FINISH);
    			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "調撥單模組結算預計執行"+imMovementHeads.size()+"筆", balanceDate, processLotNo, opUser);        
    			if(imMovementHeads != null && imMovementHeads.size() > 0){
    				for(int j = 0; j < imMovementHeads.size(); j++){
    					ImMovementHead movementHead = null;
    					try{
    						movementHead = (ImMovementHead) imMovementHeads.get(j);					
    						identification = MessageStatus.getIdentificationMsg(movementHead.getBrandCode(), movementHead.getOrderTypeCode(), movementHead.getOrderNo());
    						imMovementService.executeDailyBalance(movementHead, opUser);
    					}catch(Exception ex){
    						msg = identification + "執行結算時發生錯誤，原因：";
    						log.error(msg + ex.toString());
    						errorMsgs.add(msg + ex.getMessage() + "<br>");
    						BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
    						returnMessage = MessageStatus.ERROR;
    					}
    				}		    
    			}

    		}    
    		System.out.println("ImMovementDailyBalance finish...");
    		return returnMessage;
    	}catch(Exception ex){
    		msg = "執行調撥單結算時發生錯誤，原因：";
    		log.error(msg + ex.toString());
    		errorMsgs.add(msg + ex.getMessage() + "<br>");
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
    		returnMessage = MessageStatus.ERROR;
    		return returnMessage;
    	}finally{
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "調撥單模組結算作業完成！", balanceDate, processLotNo, opUser);        
    	}	
    }
}
