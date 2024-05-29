package tw.com.tm.erp.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.closevalidation.MonthlyCloseValidationManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;

public class MonthlyCloseManager {

    private static final Log log = LogFactory.getLog(MonthlyCloseManager.class);

    private Properties config = new Properties();

    private static final String CONFIG_FILE = "/monthly_close.properties";

    /**
     * 讀取月關帳作業設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception {
	try {
	    config.load(MonthlyCloseManager.class
		    .getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("讀取月關帳作業設定檔失敗！");
	}
    }

    
    /**
     * 執行月關帳作業
     * 
     * @param brandCode
     * @param closeDate
     * @param opUser
     * @return List
     * @throws FormException
     * @throws Exception
     */
    public List performAction(String brandCode, Date closeDate, String opUser){
    	String returnMessage = null;
    	String message = null;
    	String performResult = MessageStatus.SUCCESS;
    	BuBrandService brandService = null;
    	String[] brandCodeArray = null;
    	String updateMonthlyBalanceMonth = "N";
    	Date currentDate = new Date();
    	String uuid = null;
    	List errorMsgs = new ArrayList(0);
    	String msg = null;
    	String processName = brandCode + MessageStatus.MONTHLY_CLOSE;
    	try {
    		String execCloseDate = DateUtils.format(closeDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
    		String execCloseMonth = execCloseDate.substring(0, 6); //YYYYMM
    		brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
    		uuid = UUID.randomUUID().toString();
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, 
    				"月關帳作業程序開始執行...", currentDate, uuid, opUser);
    		// 執行月關帳檢核
    		MonthlyCloseValidationManager monthlyCloseValidationManager = (MonthlyCloseValidationManager)
    			SpringUtils.getApplicationContext().getBean("monthlyCloseValidationManager");
    		boolean execAllProcess = monthlyCloseValidationManager.performAction(brandCode, closeDate, currentDate, errorMsgs,
    				processName, uuid, opUser, "N");
    		if (errorMsgs.size() == 0) {	
    			Object[] execInfoArray = getMonthlyCloseBrandCode(brandCode, execCloseMonth, brandService);
    			brandCodeArray = (String[])execInfoArray[0];
    			updateMonthlyBalanceMonth = (String)execInfoArray[1];		
    			if(execAllProcess){
    				loadConfig();
    				Enumeration enumeration = config.elements();
    				while (enumeration.hasMoreElements()){
    					Class clsTask = Class.forName(enumeration.nextElement().toString());
    					Object objTask = clsTask.newInstance();
    					Method mthdPerformBalance = clsTask.getMethod("performBalance",
    							new Class[] { String[].class, Date.class, List.class, String.class, String.class, String.class, Date.class, Date.class });
    					returnMessage = (String)mthdPerformBalance.invoke(objTask, brandCodeArray, currentDate, errorMsgs,
    							processName, uuid, opUser, null, null);
    				}
    			}
    			if (errorMsgs.size() == 0) {
    				updateMonthlyCloseMonth(brandCode, execCloseMonth, updateMonthlyBalanceMonth, brandService);
    			}else{
    				performResult = MessageStatus.FAIL;
    			}
    		}else{
    			performResult = MessageStatus.FAIL;
    		}

    		return errorMsgs;
    	} catch (Exception ex) {
    		msg = "執行月關帳作業失敗，原因：";
    		log.error(msg + ex.toString());
    		errorMsgs.add(ex.getMessage() + "<br>");    
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
    		performResult = MessageStatus.FAIL;
    		return errorMsgs;
    	} finally {
    		// 將工作紀錄到資料庫
    		if (MessageStatus.SUCCESS.equals(performResult)) {
    			message = MessageStatus.MONTHLY_CLOSE_SUCCESS;
    		} else if (MessageStatus.FAIL.equals(performResult)) {
    			message = MessageStatus.MONTHLY_CLOSE_FAIL;
    		}
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, message, currentDate, uuid, opUser);
    		// 將執行月關帳的品牌其日結、月關帳、月結標記設為N
    		if(brandCodeArray != null){
    			changeMonthlyCloseToIdle(brandCode, brandService);
    		}
    	}
    }

    /**
     * 取得欲執行月關帳的品牌並將其日結、月關帳標記設為Y
     * 
     * @param brandCode
     * @param brandService
     * @return String[]
     * @throws ValidationErrorException
     * @throws Exception
     */
    private Object[] getMonthlyCloseBrandCode(String brandCode, String execCloseMonth,
	    BuBrandService brandService) throws ValidationErrorException,
	    Exception {

	String[] brandCodeArray = null;
	String updateMonthlyBalanceMonth = "N";
	if (brandCode != null) {
	    BuBrand brandPO = brandService.findById(brandCode);
	    String dailyBalancing = brandPO.getDailyBalancing();
	    String monthlyClosing = brandPO.getMonthlyClosing();
	    String monthlyBalancing = brandPO.getMonthlyBalancing();
	    String monthlyBalanceMonth = brandPO.getMonthlyBalanceMonth();
	    if(Integer.parseInt(monthlyBalanceMonth) > Integer.parseInt(execCloseMonth)){
		updateMonthlyBalanceMonth = "Y";
	    }
	    	    
	    if ((dailyBalancing == null || "N".equals(dailyBalancing))
		    && (monthlyClosing == null || "N".equals(monthlyClosing))
		    && (monthlyBalancing == null || "N".equals(monthlyBalancing))) {
		try {
		    brandPO.setDailyBalancing("Y");
		    brandPO.setMonthlyClosing("Y");
		    brandPO.setMonthlyBalancing("Y");
		    brandService.updateBuBrand(brandPO);
		    brandCodeArray = new String[] { brandCode };
		    return new Object[]{brandCodeArray, updateMonthlyBalanceMonth};
		} catch (Exception ex) {
		    log.error("品牌：" + brandPO.getBrandCode()
			    + "的日結、月關帳、月結標記更新為執行狀態時發生錯誤，原因：" + ex.toString());
		    throw new ValidationErrorException("品牌："
			    + brandPO.getBrandCode() + "的日結、月關帳、月結標記更新為執行狀態時發生錯誤！");
		}
	    } else {
		throw new ValidationErrorException("品牌："
			+ brandPO.getBrandCode() + "的日結或月關帳或月結標記為執行狀態，無法執行關帳作業！");
	    }
	} else {
	    throw new ValidationErrorException("參數barndCode為空值，請聯絡系統管理人員處理！");
	}
    }

    private void changeMonthlyCloseToIdle(String brandCode,
	    BuBrandService brandService) {

        BuBrand brandPO = null;
	try {
	    brandPO = brandService.findById(brandCode);
            brandPO.setDailyBalancing("N");
	    brandPO.setMonthlyClosing("N");
	    brandPO.setMonthlyBalancing("N");
            brandService.updateBuBrand(brandPO);
	} catch (Exception ex) {
	    log.error("將品牌：" + brandPO.getBrandCode()
	        + "的日結、月關帳、月結標記更新為閒置狀態失敗！");
        }	
    }

    private void updateMonthlyCloseMonth(String brandCode, String execCloseMonth,
	    String updateMonthlyBalanceMonth, BuBrandService brandService) throws Exception {
	
	BuBrand brandPO = null;
        try {
	    brandPO = brandService.findById(brandCode);
	    brandPO.setMonthlyCloseMonth(execCloseMonth);
	    if("Y".equals(updateMonthlyBalanceMonth)){
	        brandPO.setMonthlyBalanceMonth(execCloseMonth);
	    }
	    
	    brandService.updateBuBrand(brandPO);
	} catch (Exception ex) {
	    throw new Exception("更新品牌：" + brandPO.getBrandCode()
	        + "的月關帳年月失敗！");
	}
    }
}
