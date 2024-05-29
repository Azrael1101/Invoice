package tw.com.tm.erp.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.closevalidation.MonthlyCloseValidationManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;
import tw.com.tm.erp.utils.DateUtils;

public class MonthlyCloseMainManager {

	private static final Log log = LogFactory.getLog(MonthlyCloseMainManager.class);

	private Properties config = new Properties();

	private static final String CONFIG_FILE = "/monthly_main_close.properties";

	public static final String PROGRAM_ID= "MONTHLY_CLOSE_MAIN_MANAGER";

	private BuCommonPhraseService buCommonPhraseService;

	private BuBrandService buBrandService;

	private MonthlyCloseValidationManager monthlyCloseValidationManager;

	private SiProgramLogAction siProgramLogAction;

	/**
	 * 讀取月關帳作業設定檔
	 * 
	 * @throws Exception
	 */
	public void loadConfig() throws Exception {
		try {
			config.load(MonthlyCloseMainManager.class.getResourceAsStream(CONFIG_FILE));
		} catch (IOException ex) {
			throw new Exception("讀取月關帳作業設定檔失敗！");
		}
	}


	/**
	 * 執行月關作業
	 * 
	 * @param brandCode
	 * @param closeDate
	 * @param opUser
	 * @return List
	 * @throws FormException
	 * @throws Exception
	 */
	public List performAction(String brandCode, Date closeDate, String opUser, String isEnforced){
		String returnMessage = null;
		String message = null;
		String performResult = MessageStatus.SUCCESS;
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
			uuid = UUID.randomUUID().toString();
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "月關帳作業程序開始執行...", currentDate, uuid, opUser);
			// 執行月關帳檢核
			boolean execAllProcess = monthlyCloseValidationManager.performAction(brandCode, closeDate, currentDate, errorMsgs, processName, uuid, opUser, isEnforced);
			if (errorMsgs.size() == 0) {	
				Object[] execInfoArray = getMonthlyCloseBrandCode(brandCode, execCloseMonth);
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
								processName, uuid, opUser, null, closeDate);
					}
				}
				if (errorMsgs.size() == 0) {
					updateMonthlyCloseMonth(brandCode, execCloseMonth, updateMonthlyBalanceMonth, opUser);
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
				changeMonthlyCloseToIdle(brandCode);
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
	private Object[] getMonthlyCloseBrandCode(String brandCode, String execCloseMonth) 
	throws ValidationErrorException, Exception {

		String[] brandCodeArray = null;
		String updateMonthlyBalanceMonth = "N";
		if (brandCode != null) {
			BuBrand brandPO = buBrandService.findById(brandCode);
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
					buBrandService.updateBuBrand(brandPO);
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

	private void changeMonthlyCloseToIdle(String brandCode) {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setDailyBalancing("N");
			brandPO.setMonthlyClosing("N");
			brandPO.setMonthlyBalancing("N");
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			log.error("將品牌：" + brandPO.getBrandCode() + "的日結、月關帳、月結標記更新為閒置狀態失敗！");
		}	
	}

	private void updateMonthlyCloseMonth(String brandCode, String execCloseMonth, 
			String updateMonthlyBalanceMonth, String opUser)
	throws Exception {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setMonthlyCloseMonth(execCloseMonth);
			if("Y".equals(updateMonthlyBalanceMonth)){
				brandPO.setMonthlyBalanceMonth(execCloseMonth);
			}
			brandPO.setLastUpdatedBy(opUser);
			brandPO.setLastUpdateDate(new Date());
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			throw new Exception("更新品牌：" + brandPO.getBrandCode() + "的月關帳年月失敗！");
		}
	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			BuBrand buBrand = buBrandService.findById(brandCode);
			resultMap.put("brandCode", brandCode);
			resultMap.put("brandName", buBrand.getBrandName());
			resultMap.put("monthlyCloseYear", buBrand.getMonthlyCloseMonth().substring(0, 4));
			resultMap.put("monthlyCloseMonth", buBrand.getMonthlyCloseMonth().substring(4, 6));
			List <BuCommonPhraseLine> months = buCommonPhraseService.findEnableLineById("Month");
			multiList.put("months" , AjaxUtils.produceSelectorData(months  ,"lineCode" ,"name",  false,  true, 
					buBrand.getMonthlyBalanceMonth().substring(4, 6).replaceAll("0", "")));
			resultMap.put("multiList", multiList);
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("月關帳初始化失敗，原因：" + ex.toString());
			throw new Exception("月關帳初始化失敗，原因：" + ex.getMessage());
		}
	}

	/**
	 * 執行月關作業接口
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String opUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String monthlyCloseYear = (String)PropertyUtils.getProperty(formBindBean, "monthlyCloseYear");
			String monthlyCloseMonth = (String)PropertyUtils.getProperty(formBindBean, "monthlyCloseMonth");
			String isEnforced = (String)PropertyUtils.getProperty(formBindBean, "isEnforced");
			int lastDay = DateUtils.getLastDayOfMonth(Integer.parseInt(monthlyCloseYear), Integer.parseInt(monthlyCloseMonth));
			StringBuffer lastDate = new StringBuffer(monthlyCloseYear);
			lastDate.append("-");
			lastDate.append(monthlyCloseMonth);
			lastDate.append("-");
			lastDate.append(lastDay);
			Date actualCloseDate = DateUtils.parseDate(lastDate.toString());
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, brandCode);
			List errorMsgs = this.performAction(brandCode, actualCloseDate, opUser, isEnforced);
			if(errorMsgs.size() == 0)
				msgBox.setMessage(MessageStatus.MONTHLY_CLOSE_SUCCESS);
			else{
				for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
				}
				throw new FormException();
			}
		}catch(FormException e){
			log.error("執行月關帳時失敗");
			msgBox.setMessage("執行月關帳時失敗");
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error("執行月關帳時失敗");
			msgBox.setMessage("執行月關帳時失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 送出錯誤後，呼叫的function
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	private void listErrorMessageBox(MessageBox msgBox){
		//如果為背景送出
		Command cmd_error = new Command();
		cmd_error.setCmd(Command.FUNCTION);
		cmd_error.setParameters(new String[] { "showMessage()", "" });
		msgBox.setOk(cmd_error);
	}

	public String getIdentification(String brandCode) throws Exception{
		try{
			return brandCode;
		}catch(Exception ex){
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setMonthlyCloseValidationManager(
			MonthlyCloseValidationManager monthlyCloseValidationManager) {
		this.monthlyCloseValidationManager = monthlyCloseValidationManager;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

}
