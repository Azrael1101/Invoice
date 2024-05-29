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
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;


public class MonthlyCloseValidationManager {

    private static final Log log = LogFactory.getLog(MonthlyCloseValidationManager.class);

    private Properties config = new Properties();

    private static final String CONFIG_FILE = "/monthly_close_validation.properties";

    /**
     * 讀取月關帳檢核設定檔
     * 
     * @throws Exception
     */
    public void loadConfig() throws Exception {
    	try {
    		config.load(MonthlyCloseValidationManager.class.getResourceAsStream(CONFIG_FILE));
    	} catch (IOException ex) {
    		throw new Exception("讀取月關帳檢核設定檔失敗！");
    	}
    }

    /**
     * 執行月關帳檢核作業
     * 
     * @param brandCode
     * @param opUser
     */
    public boolean performAction(String brandCode, Date closeDate, Date currentDate, List errorMsgs,
    		String processName, String processLotNo, String opUser, String isEnforced) {

    	String msg = null;
    	boolean execAllProcess = true;
    	try {
    		// 執行一般檢核
    		execAllProcess  = doValidate(brandCode, closeDate, currentDate, errorMsgs, processName, processLotNo, opUser, isEnforced);
    		if (execAllProcess && errorMsgs.size() == 0) {
    			// 執行單據檢核
    			loadConfig();
    			Enumeration enumeration = config.elements();
    			while (enumeration.hasMoreElements()) {
    				Class clsTask = Class.forName(enumeration.nextElement().toString());
    				Object objTask = clsTask.newInstance();
    				Method mthdPerformBalance = clsTask.getMethod("executeValidate", new Class[] { String.class, Date.class, List.class, 
    						String.class, Date.class, String.class, String.class });
    				mthdPerformBalance.invoke(objTask, brandCode, closeDate,errorMsgs, processName, currentDate, processLotNo, opUser);
    			}
    		}
    	} catch (Exception ex) {
    		execAllProcess = false;
    		msg = "執行月關帳檢核發生錯誤，原因：";
    		log.error(msg + ex.getMessage());
    		errorMsgs.add(msg + ex.getMessage() + "<br>");
    		BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, processLotNo, opUser);
    	}
    	return execAllProcess;
    }

    private boolean doValidate(String brandCode, Date closeDate, Date currentDate, List errorMsgs,
    		String processName, String processLotNo, String opUser, String isEnforced)  throws Exception {

	boolean execAllProcess = true;
	try {
		String shortCloseDate = DateUtils.format(closeDate,DateUtils.C_DATA_PATTON_YYYYMMDD);    
		String shortCurrentDate = DateUtils.format(currentDate,DateUtils.C_DATA_PATTON_YYYYMMDD);

		String execCloseMonth = shortCloseDate.substring(0, 6); //YYYYMM
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
		
		if(Integer.parseInt(execCloseMonth) > Integer.parseInt(currentMonth) && !"Y".equals(enforcedClose)){
			throw new ValidationErrorException("關帳月份不可大於" + currentMonth + "！");
		}else{
			String dailyCloseDate = brandPO.getDailyCloseDate();
			String monthlyCloseMonth = brandPO.getMonthlyCloseMonth();
			String monthlyBalanceMonth = brandPO.getMonthlyBalanceMonth();
			
			//檢核日關帳日期是否大於或等於關帳月份的最後一天
			if (!StringUtils.hasText(dailyCloseDate)) {
				throw new ValidationErrorException(brandCode + "的日關帳日期為空值！");
			}else if (dailyCloseDate.length() != 8) {
				throw new ValidationErrorException(brandCode + "的日關帳日期長度不足8碼！");
			}else if(Integer.parseInt(shortCloseDate) > Integer.parseInt(dailyCloseDate) && !"Y".equals(enforcedClose)){
				throw new ValidationErrorException(brandCode + "的日關帳必須先調整至" + shortCloseDate + "，才能執行" + execCloseMonth + "的月關帳！");		    
			}
			
			//檢核關帳月份是否為已關月份的下個月，需逐月關帳
			if (!StringUtils.hasText(monthlyCloseMonth)) {
				throw new ValidationErrorException(brandCode + "的月關帳年月為空值！");
			} else if (monthlyCloseMonth.length() != 6) {
				throw new ValidationErrorException(brandCode + "的月關帳年月長度不足6碼！");
			}else if(Integer.parseInt(execCloseMonth) > Integer.parseInt(monthlyCloseMonth)){
				int origiMonthlyCloseYear = Integer.parseInt(monthlyCloseMonth.substring(0, 4));
				int origiMonthlyCloseMonth = Integer.parseInt(monthlyCloseMonth.substring(4, 6));
				origiMonthlyCloseMonth++;
				if(origiMonthlyCloseMonth > 12){
					origiMonthlyCloseMonth = 1;
					origiMonthlyCloseYear++;
				}
				
				String newMonthlyCloseMonth = String.valueOf(origiMonthlyCloseYear) + String.valueOf(CommonUtils.insertCharacterWithFixLength(String.valueOf(origiMonthlyCloseMonth), 2, CommonUtils.ZERO));

				if(Integer.parseInt(newMonthlyCloseMonth) != Integer.parseInt(execCloseMonth)){
					throw new ValidationErrorException(brandCode + "必須先執行完成" + Integer.parseInt(newMonthlyCloseMonth) + "的月關帳！");
				}else{
					//檢核關帳月份的上個月月結是否已完成(
					if (!StringUtils.hasText(monthlyBalanceMonth)) {
						throw new ValidationErrorException(brandCode + "的月結年月為空值！");
					} else if (monthlyBalanceMonth.length() != 6) {
						throw new ValidationErrorException(brandCode + "的月結年月長度不足6碼！");
					} else if(Integer.parseInt(execCloseMonth) > Integer.parseInt(monthlyBalanceMonth)){
						int origiMonthlyBalanceYear = Integer.parseInt(monthlyBalanceMonth.substring(0, 4));
						int origiMonthlyBalanceMonth = Integer.parseInt(monthlyBalanceMonth.substring(4, 6));
						origiMonthlyBalanceMonth++;
						if(origiMonthlyBalanceMonth > 12){
							origiMonthlyBalanceMonth = 1;
							origiMonthlyBalanceYear++;
						}
						String newMonthlyBalanceMonth = String.valueOf(origiMonthlyBalanceYear) + String.valueOf(CommonUtils.insertCharacterWithFixLength(String.valueOf(origiMonthlyBalanceMonth), 2, CommonUtils.ZERO));
						if(Integer.parseInt(newMonthlyBalanceMonth) != Integer.parseInt(execCloseMonth)){
							throw new ValidationErrorException(brandCode + "必須先執行完成" + Integer.parseInt(newMonthlyBalanceMonth) + "的月結！");
						}		
					}			
				}	    
			}else{
				//月關帳往前調整
				execAllProcess = false;
				if(Integer.parseInt(execCloseMonth) < Integer.parseInt(monthlyCloseMonth)){
					if(Integer.parseInt(shortCloseDate) > Integer.parseInt(dailyCloseDate)){
						throw new ValidationErrorException(brandCode + "的日關帳需先調整到" + shortCloseDate + "，才能將月關帳調整到" + execCloseMonth + "！");		    
					}	
				}
				//如果欲關帳的月份加兩個月後還是小於今天
				if(currentDate.after(DateUtils.addMonths(closeDate, 3)) && !"Y".equals(enforcedClose)){
					throw new ValidationErrorException(brandCode + "的月關帳無法調整至兩個月以前！");
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
