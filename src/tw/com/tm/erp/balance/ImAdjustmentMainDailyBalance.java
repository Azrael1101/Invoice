package tw.com.tm.erp.balance;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class ImAdjustmentMainDailyBalance implements DailyBlanceInterface {
	private static final Log log = LogFactory.getLog(ImAdjustmentMainDailyBalance.class);
    private static ApplicationContext context = SpringUtils.getApplicationContext(); 
    
    public String performBalance(String[] brandCodeArray, Date balanceDate, List errorMsgs, String processName, String processLotNo, String opUser, Date startDate, Date endDate){
    System.out.println("ImAdjustmentMainDailyBalance starting...");
	String returnMessage = MessageStatus.SUCCESS;
	String msg = null;
	try{
	    ImAdjustmentHeadDAO imAdjustmentHeadDAO = (ImAdjustmentHeadDAO) context.getBean("imAdjustmentHeadDAO");
	    ImGeneralAdjustmentService imGeneralAdjustmentService = (ImGeneralAdjustmentService) context.getBean("imGeneralAdjustmentService");
	    String identification = null;
	    for(int i = 0; i < brandCodeArray.length; i++){ 
			//調整單結算	
			List<ImAdjustmentHead> imAdjustmentHeads = imAdjustmentHeadDAO.findAdjustmentByProperty(brandCodeArray[i], OrderStatus.FINISH, startDate, endDate);
			if(imAdjustmentHeads != null && imAdjustmentHeads.size() > 0){
			    for(int j = 0; j < imAdjustmentHeads.size(); j++){
			    	ImAdjustmentHead adjustmentHead = null;
			    	try{
			    		adjustmentHead = (ImAdjustmentHead) imAdjustmentHeads.get(j);				    		
			    		identification = MessageStatus.getIdentificationMsg(adjustmentHead.getBrandCode(), adjustmentHead.getOrderTypeCode(), adjustmentHead.getOrderNo());
			    		// === 廠商註解 2019/12/28 海關上傳重整單修正
			    		//if(adjustmentHead.getOrderTypeCode().equals("BEF")){
						//	log.info("BEF單不做日結");
						//}else{
							imGeneralAdjustmentService.executeDailyBalance(adjustmentHead, opUser);
						//}	
						// === 廠商註解
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
	    System.out.println("ImAdjustmentMainDailyBalance finish...");
	    return returnMessage;
	}catch(Exception ex){
	    msg = "執行調整單結算時發生錯誤，原因：";
            log.error(msg + ex.toString());
            errorMsgs.add(msg + ex.getMessage() + "<br>");
            BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo, opUser);
	    returnMessage = MessageStatus.ERROR;
	    return returnMessage;
        }finally{
            BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "調整單模組結算作業完成！", balanceDate, processLotNo, opUser);        
        }	
    }
}
