package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class SoPostingTallyAction {

	private static final Log log = LogFactory.getLog(SoPostingTallyAction.class);

	private SoPostingTallyService soPostingTallyService;

	public void setSoPostingTallyService(SoPostingTallyService soPostingTallyService) {
		this.soPostingTallyService = soPostingTallyService;
	}

	public List<Properties> performSearchInitial(Map parameterMap){

		Map returnMap = null;	
		try{
			validateRequiredParameters(parameterMap);
			returnMap = soPostingTallyService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			log.info("執行POS資料過帳查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}

		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	private void validateRequiredParameters(Map parameterMap) throws Exception{

		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		//======check loginBrandCode、loginEmployeeCode========
		if(!StringUtils.hasText(loginBrandCode)){
			throw new Exception("loginBrandCode參數為空值！");
		}else if(!StringUtils.hasText(loginEmployeeCode)){
			throw new Exception("loginEmployeeCode參數為空值！");
		}
	}

	/**
	 * 執行過帳作業
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> execPosting(Map parameterMap){

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Map resultMap = null;
			HashMap conditionMap  = validatePostingRequiredParameters(parameterMap);
			conditionMap.put("soStatus", OrderStatus.SIGNING);
			List errorMsgs = new ArrayList();
			List postingDatas = new ArrayList();
			soPostingTallyService.validatePostingDateForAjax(conditionMap, errorMsgs, postingDatas);
			if(errorMsgs.size() > 0){
				throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
			}
			resultMap = soPostingTallyService.executePosting(conditionMap, postingDatas);
			msgBox.setMessage((String)resultMap.get("resultMsg"));	    
		}catch (Exception ex) {
			log.info("執行POS資料過帳作業時發生錯誤，原因：" + ex.toString());
			log.error("執行POS資料過帳作業失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	private HashMap validatePostingRequiredParameters(Map parameterMap) throws Exception{

		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String status = (String)PropertyUtils.getProperty(otherBean, "status");
		String postingSalesUnitBegin = (String)PropertyUtils.getProperty(otherBean, "postingSalesUnitBegin");
		String postingSalesUnitEnd = (String)PropertyUtils.getProperty(otherBean, "postingSalesUnitEnd");
		String postingTransactionDate = (String)PropertyUtils.getProperty(otherBean, "postingTransactionDate");
		Date transactionDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, postingTransactionDate);
		String salesUnitType = (String)PropertyUtils.getProperty(otherBean, "salesUnitType");
		String timeScope = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
		String postbatch = (String)PropertyUtils.getProperty(otherBean, "batch");
		log.info("postbatch = "+postbatch);
		//======check loginBrandCode、loginEmployeeCode========
		if(!StringUtils.hasText(brandCode)){
			throw new Exception("brandCode參數為空值！");
		}else if(!StringUtils.hasText(employeeCode)){
			throw new Exception("employeeCode參數為空值！");
		}else if(!StringUtils.hasText(orderTypeCode)){
			throw new Exception("orderTypeCode參數為空值！");
		}else if(!StringUtils.hasText(status)){
			throw new Exception("status參數為空值！");
		}else if(!StringUtils.hasText(postingTransactionDate)){
			throw new Exception("postingTransactionDate參數為空值！");
		}else if(!StringUtils.hasText(salesUnitType)){
			throw new Exception("salesUnitType參數為空值！");
		}else if(!StringUtils.hasText(timeScope)){
			throw new Exception("timeScope參數為空值！");
		}
		if("T2".equals(brandCode)){
			if(!StringUtils.hasText(postingSalesUnitBegin)){
				throw new Exception("postingSalesUnitBegin參數為空值！");
			}
			if(!StringUtils.hasText(postingSalesUnitEnd)){
				throw new Exception("postingSalesUnitEnd參數為空值！");
			}
			if(!StringUtils.hasText(postbatch)){
				throw new Exception("batch參數為空值！");
			}
		}

		HashMap conditionMap = new HashMap();
		conditionMap.put("brandCode", brandCode);
		conditionMap.put("employeeCode", employeeCode);
		conditionMap.put("orderTypeCode", orderTypeCode);
		conditionMap.put("status", status);
		conditionMap.put("postingSalesUnitBegin", postingSalesUnitBegin);
		conditionMap.put("postingSalesUnitEnd", postingSalesUnitEnd);
		conditionMap.put("transactionDate", transactionDate);
		conditionMap.put("salesUnitType", salesUnitType);
		conditionMap.put("timeScope", timeScope);
		conditionMap.put("batch", postbatch);

		return conditionMap;
	}

	/**
	 * 執行反過帳作業
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> execAntiPosting(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Map resultMap = null;
			HashMap conditionMap  = validatePostingRequiredParameters(parameterMap);
			String brandCode = (String)conditionMap.get("brandCode");
			String orderTypeCode = (String)conditionMap.get("orderTypeCode");
			String batch = (String)conditionMap.get("batch");
			Date transactionDate = (Date)conditionMap.get("transactionDate");
			ValidateUtil.isAfterClose(brandCode, orderTypeCode, "D", transactionDate,batch);
			resultMap = soPostingTallyService.executeAntiPosting(conditionMap);
			msgBox.setMessage((String)resultMap.get("resultMsg"));	    
		}catch (Exception ex) {
			log.info("執行POS資料過帳作業時發生錯誤，原因：" + ex.toString());
			log.error("執行POS資料過帳作業失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 執行單筆過帳作業時的初始化.
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = this.soPostingTallyService.executeInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行單筆過帳作業時的初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 執行單筆(反)過帳作業.  
	 */
	public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		HashMap conditionMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			validateRequiredParameters(parameterMap);
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String orderNo = (String)PropertyUtils.getProperty(formBindBean, "orderNo");
			String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			String transationDate = (String)PropertyUtils.getProperty(formBindBean, "salesDate");
	    	log.info("====X=====");
	    	log.info("brandCode:"+brandCode);
	    	log.info("orderTypeCode"+orderTypeCode);
	    	log.info("orderNo"+orderNo);
	    	log.info("====X=====");
			log.info("取前端資料完成");
			if("FINISH".equals(formAction))
			{
				log.info("單筆過帳");
				List<SoSalesOrderHead> soOrderHeadList = soPostingTallyService.validPostingData(parameterMap);
				soPostingTallyService.executePosting(soOrderHeadList, loginEmployeeCode);//第一個參數填入so的head_id且so的status須為signing且該日該機台為已過帳
				msgBox.setMessage("執行單筆過帳作業成功!");
			}
			else if("ROLLBACK".equals(formAction))
			{
				//MACO 單筆反過帳 2016.03.18
				log.info("單筆反過帳");
				conditionMap.put("brandCode", brandCode);
				conditionMap.put("orderNo", orderNo);
				conditionMap.put("orderTypeCode", orderTypeCode);
				conditionMap.put("transationDate", transationDate);
				conditionMap.put("employeeCode",loginEmployeeCode);
				soPostingTallyService.validRollBackPostingData(parameterMap);
				Map resultMap = soPostingTallyService.executeSingleAntiPosting(conditionMap);
				msgBox.setMessage(resultMap.get("resultMsg").toString());
			}
		}catch (Exception ex) {
			log.error("執行單筆過帳作業失敗，原因：");
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

}
