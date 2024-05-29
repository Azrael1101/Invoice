package tw.com.tm.erp.closevalidation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;


public class MonthlyBalanceValidationManager {

    private static final Log log = LogFactory.getLog(MonthlyBalanceValidationManager.class);

    /**
     * 執行月結檢核作業
     * 
     * @param brandCode
     * @param opUser
     */
    public boolean performAction(String brandCode, Date balanceDate, Date currentDate, List errorMsgs,
	    String processName, String processLotNo, String opUser, String isEnforced) {

	String msg = null;
	boolean execAllProcess = true;
	try {
	    // 執行一般檢核
	    execAllProcess  = doValidate(brandCode, balanceDate, currentDate, errorMsgs, processName, processLotNo, opUser, isEnforced);
	} catch (Exception ex) {
	    execAllProcess = false;
	    msg = "執行月結檢核發生錯誤，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(msg + ex.getMessage() + "<br>");
	    BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, processLotNo, opUser);
	}
	
	return execAllProcess;
    }

    private boolean doValidate(String brandCode, Date balanceDate, Date currentDate, List errorMsgs, String processName, String processLotNo, String opUser, String isEnforced) 
    throws Exception {

    	boolean execAllProcess = true;
    	try {
    		String shortBalanceDate = DateUtils.format(balanceDate,DateUtils.C_DATA_PATTON_YYYYMMDD);    
    		String shortCurrentDate = DateUtils.format(currentDate,DateUtils.C_DATA_PATTON_YYYYMMDD);
    		//結算年月
    		String execBalanceMonth = shortBalanceDate.substring(0, 6); //YYYYMM
    		//目前月份
    		String currentMonth = shortCurrentDate.substring(0, 6); //YYYYMM
    		String enforcedClose = "N";
    		BuBrandService brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
			BuBrand brandPO = brandService.findById(brandCode);
			
			if("Y".equals(isEnforced)){
				if("Y".equals(brandPO.getEnforcedClose())){
					enforcedClose = "Y";
				}
				else{
					throw new ValidationErrorException(brandCode + "不可強制執行月結！");
				}
			}
			
    		if(Integer.parseInt(execBalanceMonth) > Integer.parseInt(currentMonth) && !"Y".equals(enforcedClose)){
    			throw new ValidationErrorException("結算月份不可大於" + currentMonth + "！");
    		}else{
    			String monthlyCloseMonth = brandPO.getMonthlyCloseMonth();
    			String monthlyBalanceMonth = brandPO.getMonthlyBalanceMonth();	
    			//檢核月結月份是否已月關帳完成

    			if (!StringUtils.hasText(monthlyCloseMonth)) {
    				throw new ValidationErrorException(brandCode + "的月關帳年月為空值！");
    			} else if (monthlyCloseMonth.length() != 6) {
    				throw new ValidationErrorException(brandCode + "的月關帳年月長度不足6碼！");
    			}else if(Integer.parseInt(monthlyCloseMonth) < Integer.parseInt(execBalanceMonth)){
    				throw new ValidationErrorException(brandCode + "必須先執行完成" + execBalanceMonth + "的月關帳！");
    			}	
    			String nextBalanceMonth = DateUtils.format(DateUtils.addDays(DateUtils.getLastDateOfMonth(DateUtils.parseDate("yyyyMMdd",monthlyBalanceMonth+"01")),1),"yyyyMM");

    			// 檢核月結月份是否為已月結月份的下個月，需逐月月結
    			if (!StringUtils.hasText(monthlyBalanceMonth)) {
    				throw new ValidationErrorException(brandCode + "的月結年月為空值！");
    			} else if (monthlyBalanceMonth.length() != 6) {
    				throw new ValidationErrorException(brandCode+ "的月結年月長度不足6碼！");
    			} else if (Integer.parseInt(execBalanceMonth) != Integer.parseInt(nextBalanceMonth) && 
    					Integer.parseInt(execBalanceMonth) != Integer.parseInt(monthlyBalanceMonth) && 
    					!"Y".equals(enforcedClose)) {
    				throw new ValidationErrorException(brandCode + " 僅可執行 " + monthlyBalanceMonth + " 與 " + nextBalanceMonth + " 的月結！ ");
    			} else if (Integer.parseInt(execBalanceMonth) < Integer.parseInt(monthlyBalanceMonth)) {
    				// 月結往前調整
    				if (Integer.parseInt(execBalanceMonth) < Integer.parseInt(monthlyCloseMonth)) {
    					throw new ValidationErrorException(brandCode + "的月關帳需先調整到" + execBalanceMonth + "，才能重新結算" + execBalanceMonth + "的月結！");
    				}
    			}
    		}
    	} catch (ValidationErrorException ex) {
    		execAllProcess = false;
    		errorMsgs.add(ex.getMessage() + "<br>");
    		BalanceAndCloseUtils.createSystemLog(processName,MessageStatus.LOG_ERROR, ex.getMessage(), currentDate,processLotNo, opUser);
    	}

    	return execAllProcess;
    }
}
