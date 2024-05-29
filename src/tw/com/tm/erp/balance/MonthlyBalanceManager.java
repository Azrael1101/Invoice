package tw.com.tm.erp.balance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.closevalidation.MonthlyBalanceValidationManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;

public class MonthlyBalanceManager {

    private static final Log log = LogFactory.getLog(MonthlyBalanceManager.class);
  
    /**
     * 執行月結作業
     * 
     * @param brandCode
     * @param balanceDate
     * @param opUser
     * @return List
     * @throws FormException
     * @throws Exception
     */
    public List performAction(String brandCode, Date balanceDate, String opUser){

//	String returnMessage = null;
	String message = null;
	String performResult = MessageStatus.SUCCESS;
	BuBrandService brandService = null;
	String[] brandCodeArray = null;
	Date currentDate = new Date();
	String uuid = null;
	List errorMsgs = new ArrayList(0);
	String msg = null;
	String processName = brandCode + MessageStatus.MONTHLY_BALANCE;
	try {
	    String execBalanceDate = DateUtils.format(balanceDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String execBalanceMonth = execBalanceDate.substring(0, 6); //YYYYMM
	    brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
	    uuid = UUID.randomUUID().toString();
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, 
		    "月結作業程序開始執行...", currentDate, uuid, opUser);
	    // 執行月結檢核
	    MonthlyBalanceValidationManager monthlyBalanceValidationManager = (MonthlyBalanceValidationManager) SpringUtils.getApplicationContext()
		    .getBean("monthlyBalanceValidationManager");
	    
	    boolean execAllProcess = monthlyBalanceValidationManager.performAction(brandCode, balanceDate, currentDate,
		    errorMsgs, processName, uuid, opUser,"N");	    
	    
	    if (errorMsgs.size() == 0) {	
	    	brandCodeArray = getMonthlyBalanceBrandCode(brandCode, brandService);	
			if(execAllProcess){
				MonthlyBalanceProcessor monthlyBalanceProcessor = (MonthlyBalanceProcessor) SpringUtils.getApplicationContext()
			    .getBean("monthlyBalanceProcessor");
				for( int i = 0; i < brandCodeArray.length; i++){
					HashMap conditionMap = new HashMap(0);
					conditionMap.put("brandCode",brandCodeArray[i]);
					conditionMap.put("execBalanceYear",execBalanceMonth.substring(0, 4));//YYYY
					conditionMap.put("execBalanceMonth",execBalanceMonth.substring(4, 6));//MM
					conditionMap.put("execOperator",opUser);//Operator
					monthlyBalanceProcessor.execBalanceTask(conditionMap);
				}
			}
			if (errorMsgs.size() == 0) {
			    updateMonthlyBalanceMonth(brandCode, execBalanceMonth, brandService);
			}else{
			    performResult = MessageStatus.FAIL;
			}
	    }else{
	    	performResult = MessageStatus.FAIL;
	    }
	    
	    return errorMsgs;
	} catch (Exception ex) {
	    msg = "執行月結作業失敗，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(ex.getMessage() + "<br>");    
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
	    performResult = MessageStatus.FAIL;
	    return errorMsgs;
	} finally {
	    // 將工作紀錄到資料庫
	    if (MessageStatus.SUCCESS.equals(performResult)) {
		message = MessageStatus.MONTHLY_BALANCE_SUCCESS;
	    } else if (MessageStatus.FAIL.equals(performResult)) {
		message = MessageStatus.MONTHLY_BALANCE_FAIL;
	    }
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, message, currentDate, uuid, opUser);
	    // 將執行月結的品牌其月關帳、月結標記設為N
	    if(brandCodeArray != null){
		changeMonthlyBalanceToIdle(brandCode, brandService);
	    }
	}
    }

    /**
     * 取得欲執行月結的品牌並將其月結、月關帳標記設為Y
     * 
     * @param brandCode
     * @param brandService
     * @return String[]
     * @throws ValidationErrorException
     * @throws Exception
     */
    private String[] getMonthlyBalanceBrandCode(String brandCode, BuBrandService brandService) 
        throws ValidationErrorException, Exception {

	String[] brandCodeArray = null;
	if (brandCode != null) {
	    BuBrand brandPO = brandService.findById(brandCode);
	    String monthlyClosing = brandPO.getMonthlyClosing();
	    String monthlyBalancing = brandPO.getMonthlyBalancing();
	    	    
	    if ((monthlyClosing == null || "N".equals(monthlyClosing))
		    && (monthlyBalancing == null || "N".equals(monthlyBalancing))) {
		try {
		    brandPO.setMonthlyClosing("Y");
		    brandPO.setMonthlyBalancing("Y");
		    brandService.updateBuBrand(brandPO);
		    brandCodeArray = new String[] { brandCode };
		    return brandCodeArray;
		} catch (Exception ex) {
		    log.error("品牌：" + brandPO.getBrandCode()
			    + "的月關帳、月結標記更新為執行狀態時發生錯誤，原因：" + ex.toString());
		    throw new ValidationErrorException("品牌："
			    + brandPO.getBrandCode() + "的月關帳、月結標記更新為執行狀態時發生錯誤！");
		}
	    } else {
		throw new ValidationErrorException("品牌："
			+ brandPO.getBrandCode() + "的月關帳或月結標記為執行狀態，無法執行關帳作業！");
	    }
	} else {
	    throw new ValidationErrorException("參數barndCode為空值，請聯絡系統管理人員處理！");
	}
    }

    private void changeMonthlyBalanceToIdle(String brandCode,
	    BuBrandService brandService) {

        BuBrand brandPO = null;
	try {
	    brandPO = brandService.findById(brandCode);
	    brandPO.setMonthlyClosing("N");
	    brandPO.setMonthlyBalancing("N");
            brandService.updateBuBrand(brandPO);
	} catch (Exception ex) {
	    log.error("將品牌：" + brandPO.getBrandCode()
	        + "的月關帳、月結標記更新為閒置狀態失敗！");
        }	
    }

    private void updateMonthlyBalanceMonth(String brandCode, String execBalanceMonth,
        BuBrandService brandService) throws Exception {
	
    	BuBrand brandPO = null;
	    try {
		    brandPO = brandService.findById(brandCode);
		    brandPO.setMonthlyBalanceMonth(execBalanceMonth);	    
		    brandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
		    throw new Exception("更新品牌：" + brandPO.getBrandCode()
		        + "的月結年月失敗！");
		}
    }
}
