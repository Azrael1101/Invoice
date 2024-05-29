package tw.com.tm.erp.closevalidation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

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

public class DailyCloseValidationManager {

    private static final Log log = LogFactory
	    .getLog(DailyCloseValidationManager.class);

    private Properties config = new Properties();

    private static final String CONFIG_FILE = "/daily_close_validation.properties";

    /**
     * 讀取日關帳檢核設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception {
	try {
	    config.load(DailyCloseValidationManager.class
		    .getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("讀取日關帳檢核設定檔失敗！");
	}
    }

    /**
     * 執行日關帳檢核作業
     * 
     * @param brandCode
     * @param opUser
     */
    public boolean performAction(String brandCode, Date closeDate, Date currentDate, List errorMsgs,
	    String processName, String processLotNo, String opUser) {

	String msg = null;
	boolean execAllProcess = true;
	try {
	    // 執行一般檢核
	    execAllProcess  = doValidate(brandCode, closeDate, currentDate, errorMsgs,
		    processName, processLotNo, opUser);
	    if (execAllProcess && errorMsgs.size() == 0) {
		// 執行單據檢核
		loadConfig();
		Enumeration enumeration = config.elements();
		while (enumeration.hasMoreElements()) {
		    Class clsTask = Class.forName(enumeration.nextElement()
			    .toString());
		    Object objTask = clsTask.newInstance();
		    Method mthdPerformBalance = clsTask.getMethod(
			    "executeValidate", new Class[] { String.class,
				    Date.class, List.class, String.class,
				    Date.class, String.class, String.class });
		    mthdPerformBalance.invoke(objTask, brandCode, closeDate,
			    errorMsgs, processName, currentDate, processLotNo, opUser);
		}
	    }
	} catch (Exception ex) {
	    execAllProcess = false;
	    msg = "執行日關帳檢核發生錯誤，原因：";
	    log.error(msg + ex.toString());
	    errorMsgs.add(msg + ex.getMessage() + "<br>");
	    BalanceAndCloseUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    processLotNo, opUser);
	}
	
	return execAllProcess;
    }

    private boolean doValidate(String brandCode, Date closeDate, Date currentDate,
    		List errorMsgs, String processName, String processLotNo, String opUser) throws Exception {
    	boolean execAllProcess = true;
    	try {
    		String shortCloseDateString = DateUtils.format(closeDate,DateUtils.C_DATA_PATTON_YYYYMMDD);
    		if (DateUtils.getShortDate(closeDate).after(DateUtils.getShortDate(currentDate))) {
    			throw new ValidationErrorException("關帳日期不可大於" + DateUtils.format(currentDate) + "！");
    		} else {
    			BuBrandService brandService = (BuBrandService) SpringUtils.getApplicationContext().getBean("buBrandService");
    			BuBrand brandPO = brandService.findById(brandCode);
    			String dailyCloseDate = brandPO.getDailyCloseDate();
    			if (!StringUtils.hasText(dailyCloseDate)) {
    				throw new ValidationErrorException(brandCode + "的日關帳日期為空值！");
    			}else if (dailyCloseDate.length() != 8) {
    				throw new ValidationErrorException(brandCode + "的日關帳日期長度不足8碼！");
    			}else if(Integer.parseInt(shortCloseDateString) <= Integer.parseInt(dailyCloseDate)){
    				//日關帳往前調整
    				execAllProcess = false;
    				String monthlyCloseMonth = brandPO.getMonthlyCloseMonth();
    				if (!StringUtils.hasText(monthlyCloseMonth)) {
    					throw new ValidationErrorException(brandCode + "的月關帳年月為空值！");
    				} else if (monthlyCloseMonth.length() != 6) {
    					throw new ValidationErrorException(brandCode+ "的月關帳年月長度不足6碼！");
    				} else {
    					int year = Integer.parseInt(monthlyCloseMonth.substring(0,4));
    					int month = Integer.parseInt(monthlyCloseMonth.substring(5));
    					//月關允許的最後一天
    					Date lastDateofYearMonth = DateUtils.getLastDateOfMonth(year, month);
    					String lastDateofYearMonthString = DateUtils.formatTime(lastDateofYearMonth, DateUtils.C_DATA_PATTON_YYYYMMDD);
    					log.info("lastDateofYearMonthString = " + lastDateofYearMonthString);
    					if ( Integer.parseInt(shortCloseDateString) < Integer.parseInt(lastDateofYearMonthString) ) {
    						throw new ValidationErrorException(brandCode+ "的月關帳需先調整到"
    								+ DateUtils.getLastDateOfLastMonth(closeDate).substring(0, 6)
    								+ "，才能將日關帳調整到" + shortCloseDateString + "！");
    					}
    				}
    			}
    		}
    	} catch (ValidationErrorException ex) {
    		execAllProcess = false;
    		errorMsgs.add(ex.getMessage() + "<br>");
    		BalanceAndCloseUtils.createSystemLog(processName,MessageStatus.LOG_ERROR, ex.getMessage(), currentDate, processLotNo, opUser);
    	}
    	return execAllProcess;
    }
}
