package tw.com.tm.erp.balance;

import java.util.ArrayList;
import java.util.Date;
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
import tw.com.tm.erp.closevalidation.MonthlyBalanceValidationManager;
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

public class MonthlyBalanceMainManager {

	private static final Log log = LogFactory.getLog(MonthlyBalanceMainManager.class);

	public static final String PROGRAM_ID= "MONTHLY_BALANCE_MAIN_MANAGER";
	
	public static final String PROGRAM_ID_CM= "MONTHLY_BALANCE_MAIN_MANAGER_CM";
	
	private BuCommonPhraseService buCommonPhraseService;

	private BuBrandService buBrandService;

	private MonthlyBalanceValidationManager monthlyBalanceValidationManager;
	
	private MonthlyBalanceMainProcessor monthlyBalanceMainProcessor;
	
	private SiProgramLogAction siProgramLogAction;
	
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
	public List performAction(String brandCode, Date balanceDate, String opUser, String mode, String isEnforced){
		String message = null;
		String performResult = MessageStatus.SUCCESS;
		String[] brandCodeArray = null;
		Date currentDate = new Date();
		String uuid = null;
		List errorMsgs = new ArrayList(0);
		String msg = null;
		String processName = brandCode + MessageStatus.MONTHLY_BALANCE;
		try {
			String execBalanceDate = DateUtils.format(balanceDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String execBalanceMonth = execBalanceDate.substring(0, 6); //YYYYMM
			uuid = UUID.randomUUID().toString();
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "月結作業程序開始執行...", currentDate, uuid, opUser);
			// 執行月結檢核
			boolean execAllProcess = monthlyBalanceValidationManager.performAction(brandCode, balanceDate, currentDate, errorMsgs, processName, uuid, opUser, isEnforced);	    
			if (errorMsgs.size() == 0) {	
				brandCodeArray = getMonthlyBalanceBrandCode(brandCode);	
				if(execAllProcess){
					for( int i = 0; i < brandCodeArray.length; i++){
						HashMap conditionMap = new HashMap(0);
						conditionMap.put("brandCode",brandCodeArray[i]);
						conditionMap.put("execBalanceYear",execBalanceMonth.substring(0, 4));//YYYY
						conditionMap.put("execBalanceMonth",execBalanceMonth.substring(4, 6));//MM
						conditionMap.put("execOperator",opUser);//Operator
						conditionMap.put("execMode",mode);//Mode
						monthlyBalanceMainProcessor.execBalanceTask(conditionMap);
					}
				}
				if (errorMsgs.size() == 0) {
					updateMonthlyBalanceMonth(brandCode, execBalanceMonth, opUser);
				}else{
					performResult = MessageStatus.FAIL;
				}
			}else{
				performResult = MessageStatus.FAIL;
			}

			return errorMsgs;
		} catch (Exception ex) {
			msg = "執行月結作業失敗，原因：";
			log.error(msg + ex.getMessage());
			errorMsgs.add(ex.getMessage() + "<br>");    
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), currentDate, uuid, opUser);
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
				changeMonthlyBalanceToIdle(brandCode);
			}
		}
	}

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
	public List performActionCM(String brandCode, Date balanceDate, String opUser){
		log.info("performActionCM");
		String message = null;
		String performResult = MessageStatus.SUCCESS;
		String[] brandCodeArray = null;
		Date currentDate = new Date();
		String uuid = null;
		List errorMsgs = new ArrayList(0);
		String msg = null;
		String processName = brandCode + MessageStatus.MONTHLY_BALANCE;
		try {
			String execBalanceDate = DateUtils.format(balanceDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String execBalanceMonth = execBalanceDate.substring(0, 6); //YYYYMM
			uuid = UUID.randomUUID().toString();
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "海關月結作業程序開始執行...", currentDate, uuid, opUser);
			// 執行月結檢核
			boolean execAllProcess = monthlyBalanceValidationManager.performAction(brandCode, balanceDate, currentDate, errorMsgs, processName, uuid, opUser, "N");	    
			if (errorMsgs.size() == 0) {	
				brandCodeArray = getMonthlyBalanceBrandCode(brandCode);	
				if(execAllProcess){
					for( int i = 0; i < brandCodeArray.length; i++){
						HashMap conditionMap = new HashMap(0);
						conditionMap.put("brandCode",brandCodeArray[i]);
						conditionMap.put("execBalanceYear",execBalanceMonth.substring(0, 4));//YYYY
						conditionMap.put("execBalanceMonth",execBalanceMonth.substring(4, 6));//MM
						conditionMap.put("execOperator",opUser);//Operator
						monthlyBalanceMainProcessor.execBalanceTaskCM(conditionMap);
					}
				}
				if (errorMsgs.size() == 0) {
					updateMonthlyBalanceMonth(brandCode, execBalanceMonth, opUser);
				}else{
					performResult = MessageStatus.FAIL;
				}
			}else{
				performResult = MessageStatus.FAIL;
			}

			return errorMsgs;
		} catch (Exception ex) {
			msg = "執行海關月結作業失敗，原因：";
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
				changeMonthlyBalanceToIdle(brandCode);
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
	private String[] getMonthlyBalanceBrandCode(String brandCode) 
	throws ValidationErrorException, Exception {

		String[] brandCodeArray = null;
		if (brandCode != null) {
			BuBrand brandPO = buBrandService.findById(brandCode);
			String monthlyClosing = brandPO.getMonthlyClosing();
			String monthlyBalancing = brandPO.getMonthlyBalancing();

			if ((monthlyClosing == null || "N".equals(monthlyClosing))
					&& (monthlyBalancing == null || "N".equals(monthlyBalancing))) {
				try {
					brandPO.setMonthlyClosing("Y");
					brandPO.setMonthlyBalancing("Y");
					buBrandService.updateBuBrand(brandPO);
					brandCodeArray = new String[] { brandCode };
					return brandCodeArray;
				} catch (Exception ex) {
					log.error("品牌：" + brandPO.getBrandCode() + "的月關帳、月結標記更新為執行狀態時發生錯誤，原因：" + ex.toString());
					throw new ValidationErrorException("品牌：" + brandPO.getBrandCode() + "的月關帳、月結標記更新為執行狀態時發生錯誤！");
				}
			} else {
				throw new ValidationErrorException("品牌：" + brandPO.getBrandCode() + "的月關帳或月結標記為執行狀態，無法執行關帳作業！");
			}
		} else {
			throw new ValidationErrorException("參數barndCode為空值，請聯絡系統管理人員處理！");
		}
	}

	private void changeMonthlyBalanceToIdle(String brandCode) {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setMonthlyClosing("N");
			brandPO.setMonthlyBalancing("N");
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			log.error("將品牌：" + brandPO.getBrandCode() + "的月關帳、月結標記更新為閒置狀態失敗！");
		}	
	}

	private void updateMonthlyBalanceMonth(String brandCode, String execBalanceMonth, String opUser) throws Exception {
		BuBrand brandPO = null;
		try {
			brandPO = buBrandService.findById(brandCode);
			brandPO.setMonthlyBalanceMonth(execBalanceMonth);
			brandPO.setLastUpdatedBy(opUser);
			brandPO.setLastUpdateDate(new Date());
			buBrandService.updateBuBrand(brandPO);
		} catch (Exception ex) {
			throw new Exception("更新品牌：" + brandPO.getBrandCode() + "的月結年月失敗！");
		}
	}

	/**
	 * 初始化 月結欄位
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
			resultMap.put("brandCode",brandCode);
			resultMap.put("brandName",buBrand.getBrandName());
			resultMap.put("monthlyBalanceYear",buBrand.getMonthlyBalanceMonth().substring(0, 4));
			resultMap.put("monthlyBalanceMonth",buBrand.getMonthlyBalanceMonth().substring(4, 6));
			List <BuCommonPhraseLine> months = buCommonPhraseService.findEnableLineById("Month");
			multiList.put("months" , AjaxUtils.produceSelectorData(months  ,"lineCode" ,"name",  false,  true, 
					buBrand.getMonthlyBalanceMonth().substring(4, 6).replaceAll("0", "")));
			resultMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("月結初始化失敗，原因：" + ex.toString());
			throw new Exception("月結初始化失敗，原因：" + ex.getMessage());
		}
	}

	/**
	 * 執行月結作業接口
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
			String monthlyBalanceYear = (String)PropertyUtils.getProperty(formBindBean, "monthlyBalanceYear");
			String monthlyBalanceMonth = (String)PropertyUtils.getProperty(formBindBean, "monthlyBalanceMonth");
			String isEnforced = (String)PropertyUtils.getProperty(formBindBean, "isEnforced");
			int lastDay = DateUtils.getLastDayOfMonth(Integer.parseInt(monthlyBalanceYear), Integer.parseInt(monthlyBalanceMonth));
			StringBuffer lastDate = new StringBuffer(monthlyBalanceYear);
			lastDate.append("-");
			lastDate.append(monthlyBalanceMonth);
			lastDate.append("-");
			lastDate.append(lastDay);
			Date actualBalanceDate = DateUtils.parseDate(lastDate.toString());
			String mode = (String)PropertyUtils.getProperty(formBindBean, "mode");
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, brandCode);
			List errorMsgs = this.performAction(brandCode, actualBalanceDate, opUser, mode, isEnforced);
			if(errorMsgs.size() == 0)
				msgBox.setMessage(MessageStatus.MONTHLY_BALANCE_SUCCESS);
			else{
				for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
				}
				throw new FormException();
			}
		}catch(FormException e){
			log.error("執行月結時失敗");
			msgBox.setMessage("執行月結時失敗");
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error("執行月結時失敗");
			msgBox.setMessage("執行月結時失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitialCM(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			BuBrand buBrand = buBrandService.findById(brandCode);
			resultMap.put("brandCode",brandCode);
			resultMap.put("brandName",buBrand.getBrandName());
			resultMap.put("monthlyBalanceYear",buBrand.getMonthlyBalanceMonth().substring(0, 4));
			resultMap.put("monthlyBalanceMonth",buBrand.getMonthlyBalanceMonth().substring(4, 6));
			List <BuCommonPhraseLine> months = buCommonPhraseService.findEnableLineById("Month");
			multiList.put("months" , AjaxUtils.produceSelectorData(months  ,"lineCode" ,"name",  false,  true, 
					buBrand.getMonthlyBalanceMonth().substring(4, 6).replaceAll("0", "")));
			resultMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch(Exception ex){
			log.error("海關月結初始化失敗，原因：" + ex.toString());
			throw new Exception("海關月結初始化失敗，原因：" + ex.getMessage());
		}
	}
	
	
	public List<Properties> performTransactionCM(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String opUser = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String monthlyBalanceYear = (String)PropertyUtils.getProperty(formBindBean, "monthlyBalanceYear");
			String monthlyBalanceMonth = (String)PropertyUtils.getProperty(formBindBean, "monthlyBalanceMonth");
			int lastDay = DateUtils.getLastDayOfMonth(Integer.parseInt(monthlyBalanceYear), Integer.parseInt(monthlyBalanceMonth));
			StringBuffer lastDate = new StringBuffer(monthlyBalanceYear);
			lastDate.append("-");
			lastDate.append(monthlyBalanceMonth);
			lastDate.append("-");
			lastDate.append(lastDay);
			Date actualBalanceDate = DateUtils.parseDate(lastDate.toString());
			siProgramLogAction.deleteProgramLog(PROGRAM_ID_CM, null, brandCode);
			List errorMsgs = this.performActionCM(brandCode, actualBalanceDate, opUser);
			if(errorMsgs.size() == 0)
				msgBox.setMessage(MessageStatus.MONTHLY_BALANCE_SUCCESS);
			else{
				for (Iterator iterator = errorMsgs.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					siProgramLogAction.createProgramLog(PROGRAM_ID_CM, MessageStatus.LOG_ERROR, brandCode, object.toString().replaceAll("<br>", ""), "MIS");
				}
				throw new FormException();
			}
		}catch(FormException e){
			log.error("執行月結時失敗");
			msgBox.setMessage("執行月結時失敗");
			listErrorMessageBox(msgBox);
		}catch (Exception ex) {
			log.error("執行月結時失敗");
			msgBox.setMessage("執行月結時失敗，原因：" + ex.getMessage());
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

	public void setMonthlyBalanceValidationManager(
			MonthlyBalanceValidationManager monthlyBalanceValidationManager) {
		this.monthlyBalanceValidationManager = monthlyBalanceValidationManager;
	}

	public void setMonthlyBalanceMainProcessor(
			MonthlyBalanceMainProcessor monthlyBalanceMainProcessor) {
		this.monthlyBalanceMainProcessor = monthlyBalanceMainProcessor;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}


}
