package tw.com.tm.erp.action;

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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.SoDepartmentOrderHeadDAO;
import tw.com.tm.erp.hbm.service.SoDepartmentOrderService;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class SoDepartmentOrderAction {
/***************************************** LOG登記 *****************************************/	
	private static final Log log = LogFactory.getLog(SoDepartmentOrderAction.class);
/***************************************** Spring IOC *****************************************/
	private SoDepartmentOrderService soDepartmentOrderService;
	
	public void setSoDepartmentOrderService(SoDepartmentOrderService soDepartmentOrderService){
		this.soDepartmentOrderService = soDepartmentOrderService;
	}
	
	private SoSalesOrderService soSalesOrderService;
	
	public void setSoSalesOrderService(SoSalesOrderService soSalesOrderService){
		this.soSalesOrderService = soSalesOrderService;
	}
	
/***************************************** Action Function *****************************************/
/**主頁面初始化**/
	public List<Properties> performPosMainInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = soDepartmentOrderService.executePosMainInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行POS初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
/**登入頁面初始化**/	
	public List<Properties> performLoginInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = soDepartmentOrderService.executeLoginInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("登入初始化錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	//promotion
	public List<Properties> performPromoteInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = soDepartmentOrderService.executePromotionInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行Promotion初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
/**POS銷售單送出**/
	public List<Properties> performPosMainTransaction(Map parameterMap) {
		log.info("百貨POS單據送出");
		
		Map returnMap = new HashMap(0);

		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			//Object formLinkBean = parameterMap.get("vatBeanFormLink");
			//String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			// ======check beforeChangeStatus、formAction========
			//if (!StringUtils.hasText(beforeChangeStatus)) {
			//	throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
			//} else if (!StringUtils.hasText(formAction)) {
			//	throw new Exception("formAction參數為空值，無法執行存檔！");
			//}
			// =====================================================

			Map resultMap = new HashMap();
			
			Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formBindBean, "headId"));
			String useYear = (String) PropertyUtils.getProperty(formBindBean, "useYear");
			SoSalesOrderHead soSalesOrderHead = soDepartmentOrderService.updateSalesOrder(parameterMap);// 更新銷貨單主檔
			//SoSalesOrderHead soSalesOrderHead = soDepartmentOrderService.copyToRealSalesHead(soDepartmentOrderHead);
			
			
		    // 執行檢核
		    HashMap findObjs = new HashMap();
		    findObjs.put("brandCode", soSalesOrderHead.getBrandCode());
		    findObjs.put("posMachineCode", soSalesOrderHead.getPosMachineCode());
		    findObjs.put("salesOrderDate", DateUtils.format(soSalesOrderHead.getSalesOrderDate(),DateUtils.C_DATE_PATTON_SLASH) );
		    findObjs.put("customerPoNoStart", (String) PropertyUtils.getProperty(formBindBean, "headId"));
		    findObjs.put("customerPoNoend", (String) PropertyUtils.getProperty(formBindBean, "headId"));
		    findObjs.put("msNo", soSalesOrderHead.getHeadId());
		    findObjs.put("orderTypeCode", "SOP");
		    findObjs.put("identification", "POS");
		    findObjs.put("opUser", soSalesOrderHead.getSuperintendentCode());
		    //doValidate(findObjs);
		    log.info(soSalesOrderHead.getSoSalesOrderItems().get(0).getItemCode());
		    //soSalesOrderService.saveNoValidate(soSalesOrderHead, findObjs);
			log.info("銷售單號:"+soSalesOrderHead.getHeadId());
			
			soSalesOrderHead = soDepartmentOrderService.findT2ById(headId);
			//soDepartmentOrderHead.setSalesoOrderId(soSalesOrderHead.getHeadId());
			//soDepartmentOrderHead = soDepartmentOrderService.modifySoSalesOrder(soDepartmentOrderHead,loginEmployeeCode);// 更新銷貨單主檔
			//soDepartmentOrderService.updateCouponRecord(soDepartmentOrderHead, useYear);
			//soDepartmentOrderService.updateRealSalesHead(soDepartmentOrderHead);
			//Map resultMap = null;
			/*
			if (!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formAction)) {
				soDepartmentOrderHead.updateSalesOrder(parameterMap);// 更新銷貨單主檔
				List errorMsgs = posDepartmentService.updateCheckedSalesOrderData(parameterMap);// 更新檢核後的銷貨單資料
				if (errorMsgs.size() > 0)
					throw new FormException(MessageStatus.VALIDATION_FAILURE);

				soSalesOrderHead = posDepartmentService.findById(headId);
				
				resultMap = posDepartmentService.updateAJAXSOToDelivery(parameterMap, errorMsgs);
			}
			
			*/
				/*
			if (OrderStatus.UNCONFIRMED.equals(formAction)) {// 執行反確認
				resultMap = posDepartmentService.executeAJAXAntiConfirm(parameterMap);
			} else if (!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formAction)) {
				posDepartmentService.updateSalesOrder(parameterMap);// 更新銷貨單主檔
				List errorMsgs = posDepartmentService.updateCheckedSalesOrderData(parameterMap);// 更新檢核後的銷貨單資料
				if (errorMsgs.size() > 0)
					throw new FormException(MessageStatus.VALIDATION_FAILURE);

				soSalesOrderHead = posDepartmentService.findById(headId);
				
				resultMap = posDepartmentService.updateAJAXSOToDelivery(parameterMap, errorMsgs);
			} else {
				resultMap = posDepartmentService.updateAJAXSalesOrder(parameterMap);
			}
				*/
			//soDepartmentOrderHead = (SoDepartmentOrderHead) resultMap.get("entityBean");
			resultMap.put("entityBean", soSalesOrderHead);

			wrappedMessageBox(msgBox, soSalesOrderHead, true, false);


		} catch (FormException e) {
			log.error("執行銷貨單存檔失敗");
			e.printStackTrace();
			msgBox.setMessage("執行銷貨單存檔失敗");
		} catch (Exception ex) {
			log.error("執行銷貨單時存檔失敗");
			ex.printStackTrace();
			msgBox.setMessage("執行銷貨單時存檔失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
/**銷售單檢核**/
    private void doValidate(HashMap parameterMap) throws Exception {

    	String dateType = "銷貨日期";
    	String brandCode = (String) parameterMap.get("brandCode");
    	String actualShopCode = (String) parameterMap.get("posMachineCode");
    	Date actualSalesDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String)parameterMap.get("salesOrderDate") );
    	String orderTypeCode = (String) parameterMap.get("orderTypeCode");
    	// 檢核是否關帳
    	//ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, actualSalesDate);
    	// 檢核是否過帳
    	SoPostingTallyService soPostingTallyService = (SoPostingTallyService) SpringUtils
    		.getApplicationContext().getBean("soPostingTallyService");
    	SoPostingTally soPostingTally = soPostingTallyService
    		.findSoPostingTallyById(actualShopCode, actualSalesDate);
    	if (soPostingTally != null
    		&& "Y".equalsIgnoreCase(soPostingTally.getIsPosting())) {
    	    throw new ValidationErrorException("專櫃代號：" + actualShopCode
    		    + "、交易日期：" + DateUtils.format(actualSalesDate)
    		    + "已完成過帳，無法執行匯入！");
    	}
    }
/**單據送出後返回處**/
	private void wrappedMessageBox(MessageBox msgBox, SoSalesOrderHead soSalesOrderHead, boolean isStartProcess, boolean isExecFunction) {
		String brandCode = soSalesOrderHead.getBrandCode();
		String status = OrderStatus.SIGNING;
		Long identification = soSalesOrderHead.getHeadId();
		
		Command cmd_ok = new Command();
		if (isStartProcess) {
			Command cmd_cancel = new Command();
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "createNewForm()", "" });
			msgBox.setMessage("序號:" + identification + "表單已送出！");
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		} else {

			if (OrderStatus.SIGNING.equals(status) || isExecFunction) {
				msgBox.setMessage("序號:" + identification + "表單已送出！");
			} else {
				msgBox.setMessage("序號:" + identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
			}
			cmd_ok.setCmd(Command.WIN_CLOSE);

		}

		msgBox.setOk(cmd_ok);

		if (isExecFunction) {
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
			msgBox.setBefore(cmd_bf);
		}
	}
	
/**登入頁面初始化**/	
	public List<Properties> performGoodsSearchInitial(Map parameterMap){
		log.info("performGoodsSearchInitial");
		Map returnMap = null;	
		try{
			returnMap = soDepartmentOrderService.executeGoodsSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("登入初始化錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	public List<Properties> performSalesSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = soDepartmentOrderService.executeSalesSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("登入初始化錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
}	
	
	