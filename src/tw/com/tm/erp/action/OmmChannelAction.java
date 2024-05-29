package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.service.DbcInformationTablesService;
import tw.com.tm.erp.hbm.service.OmmChannelService;
import tw.com.tm.erp.hbm.service.OmmChannelTXFService;
import tw.com.tm.erp.utils.AjaxUtils;

public class OmmChannelAction {
	private static final Log log = LogFactory.getLog(OmmChannelAction.class);

	// spring IOC service
	private OmmChannelTXFService ommChannelTXFService;
	private OmmChannelService ommChannelService;
	
	public void setOmmChannelTXFService(OmmChannelTXFService ommChannelTXFService) {
		this.ommChannelTXFService = ommChannelTXFService;
	}
	
	public void setOmmChannelService(OmmChannelService ommChannelService) {
		this.ommChannelService = ommChannelService;
	}



	/**
	 * 
	 * 表單初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap) {
		log.info("performInitial...");
		Map returnMap = new HashMap(0);

		try {
			returnMap = ommChannelTXFService.executeInitial(parameterMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行通道建立作業初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", msgBox);
		}

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 
	 * 表單送出
	 * 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap) {
		log.info("performTransaction...");
		
		Map returnMap = new HashMap(0);
		HashMap resultMap = new HashMap(0);
		OmmChannelTXFHead ommChannelTXFHead = null;

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String formBeforeStatus = (String) PropertyUtils.getProperty(otherBean, "formStatus");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			String formAfterStatus = "";

			// 驗證必要欄位
			verifyRequiredFields(parameterMap);
			
			// 前端資料塞入bean(entityBean),並更新
			resultMap = ommChannelTXFService.updateOmmChannelTXFHeadBean(parameterMap);
			parameterMap.put("resultMap", resultMap);
			
			// 取得下個狀態
			formAfterStatus = getFormAfterStatus(formBeforeStatus, formAction);
			resultMap.put("formAfterStatus", formAfterStatus);
			
			// 流程控制
			ommChannelTXFService.executeTransaction(parameterMap);
			
			ommChannelTXFHead = (OmmChannelTXFHead) resultMap.get("entityBean");
			
			// 將資料帶回前端
			returnMap.put("form", ommChannelTXFHead);
			returnMap.put("orderNo", ommChannelTXFHead.getOrderNo());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行通道建立作業存檔時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 查詢頁面初始化
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> executeSearchInitial(Map parameterMap) {
		log.info("executeSearchInitial...");
		
		Map returnMap = new HashMap(0);
		
		try {
			returnMap = ommChannelService.executeSearchInitial(parameterMap);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("執行通道查詢作業初始化時發生錯誤，原因：" + e.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(e.getMessage());
			returnMap.put("vatMessage", msgBox);
		}
		
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 
	 * 驗證必要欄位formStatus、formAction
	 * 
	 * @param parameterMap
	 * @throws Exception
	 */
	private void verifyRequiredFields(Map parameterMap) throws Exception {
		Object vatBeanOther = parameterMap.get("vatBeanOther");
		String formStatus = (String) PropertyUtils.getProperty(vatBeanOther, "formStatus");
		String formAction = (String) PropertyUtils.getProperty(vatBeanOther, "formAction");
		
		log.info("formStatus"+formStatus+"; formAction"+formAction);
		
		if (!StringUtils.hasText(formStatus)) {
			throw new ValidationErrorException("formStatus參數為空值，無法執行存檔！");
		}
		
		if (!StringUtils.hasText(formAction)) {
			throw new ValidationErrorException("formAction參數為空值，無法執行存檔！");
		}
	}
	
	/**
	 * 
	 * 透過表單狀態、表單動作，取得表單下個狀態
	 * 
	 * @param formBeforeStatus
	 * @param formAction
	 * @return List<Properties>
	 * @throws Exception
	 */
	private String getFormAfterStatus(String formBeforeStatus, String formAction) throws Exception {
		String formAfterStatus = "";
		
		if(OrderStatus.SAVE.equals(formBeforeStatus)) {
			if(OrderStatus.FORM_SAVE.equals(formAction)) {
				formAfterStatus = OrderStatus.SAVE;
			}else if(OrderStatus.FORM_SUBMIT.equals(formAction)) {
				formAfterStatus = OrderStatus.FINISH;
			}else if(OrderStatus.FORM_VOID.equals(formAction)) {
				formAfterStatus = OrderStatus.VOID;
			}else {
				throw new ValidationErrorException("formAction參數異常，無法執行存檔！");
			}
			
		}else if(OrderStatus.FINISH.equals(formBeforeStatus)) {
			
		}else{
			throw new ValidationErrorException("formStatus參數異常，無法執行存檔！");
		}
		
		return formAfterStatus;
	}
	
	/**
	 * 檢核主檔資料是否存在,
	 * 若存在則返回已存在的訊息;
	 * 若不存在則查出父主檔明細,存入異動檔明細後,
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> getDetailByCondition(Properties httpRequest) throws Exception {
		log.info("getDetailByCondition...");
		List<Properties> result = new ArrayList();
		
		try {
			result = ommChannelTXFService.updateDetailByCondition(httpRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		Calendar startCalendar = new GregorianCalendar(2021, 0, 1);
		
		System.out.println(startCalendar.getTime());
	}
}
