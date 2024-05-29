package tw.com.tm.erp.action;

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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.service.SoDeliveryService;
import tw.com.tm.erp.hbm.service.TmpAjaxSearchDataService;
import tw.com.tm.erp.utils.AjaxUtils;


public class SoDeliveryAction {

    private static final Log log = LogFactory.getLog(SoDeliveryAction.class);

	private SoDeliveryService soDeliveryService;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private SiProgramLogAction siProgramLogAction;
	private TmpAjaxSearchDataService tmpAjaxSearchDataService;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	public void setSoDeliveryService(SoDeliveryService soDeliveryService) {
		this.soDeliveryService = soDeliveryService;
	}
	public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
		this.soDeliveryHeadDAO = soDeliveryHeadDAO;
	}

	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}

	public String checkEmpCode(String employeeCode) throws Exception{
		String isTrue;
		HashMap findObjs = new HashMap();
		findObjs.put("employeeCode", employeeCode);
		List<BuEmployeeWithAddressView> employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
		if(employees.size()==0){
			isTrue="false";
		}
		else{
			isTrue="true";
		}
		System.out.println("checkEmpCode:"+isTrue);
		return isTrue;
	}

    /**
	 * 執行入提單交易流程
	 *
	 * @param parameterMap
	 * @return
     * @throws Exception 
	 */
	public List<Properties> performTransaction(Map parameterMap) throws Exception {
		
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		String beforeChangeStatus = new String("");
		String employeeCode = new String("");
		String logLevel = new String("SUCCESS");
		String logMessage = new String("");
		String groupCode = "";
		Long headId = new Long(0);
		Command cmd_bf = new Command();
		String isTrue = "";
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			headId = soDeliveryService.getSoDeliveryHeadId(formLinkBean);
//Maco 2016.07.18 員工檢核
			isTrue = checkEmpCode(employeeCode);
			groupCode = (String) PropertyUtils.getProperty(otherBean, "userType");
			System.out.println("gggggggggaaaa:"+groupCode);
			if (!StringUtils.hasText(beforeChangeStatus)) {
				throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");
			}
			
			if (isTrue.equals("false")) {
				throw new ValidationErrorException("工號欄位錯誤 請重新輸入！");
			}
			if(isTrue.equals("true")){
				System.out.println("beforeChangeStatus-test:" + beforeChangeStatus);
				HashMap resultMap = soDeliveryService.updateAJAXSoDelivery(parameterMap);
				log.info("update Data compile");
				SoDeliveryHead deliveryBean = (SoDeliveryHead) resultMap.get("entityBean");
				log.info("get entityBean to SoDeliveryHead...");
				returnMap.put("orderNo", deliveryBean.getOrderNo());
				log.info("getOrderNo...");
				logLevel = "SUCCESS";
				if(groupCode.equals("SERVICE_SEARCH_SODEL")){
					System.out.println("有");
					logMessage="客服更新資料成功";
				}else{
					System.out.println("沒有");
				    logMessage="資料更新成功，狀態「"+ OrderStatus.getChineseWord(beforeChangeStatus)+"」";
				}
				msgBox.setMessage("入提單("+deliveryBean.getOrderNo()+")存檔成功");
				cmd_bf.setCmd(Command.FUNCTION);
				cmd_bf.setParameters(new String[] { "doFormAccessControl()", "" });
				msgBox.setOk(cmd_bf);
			}
		}catch (ValidationErrorException fex) {
			msgBox.setMessage("工號欄位錯誤 請重新輸入工號!!");
			//cmd_bf.setCmd(Command.FUNCTION);
			//cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		}catch (FormException fex) {
			msgBox.setMessage("執行入提單存檔時發生錯誤");
			logLevel = "ERROR";
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("執行入提單存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行入提單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
			logLevel = "ERROR";
			logMessage="資料更新失敗";
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		}
		log.info("test log for 待提領 y-0"+logMessage);
		soDeliveryService.updateSoDeliveyLog(headId, "MANUAL", "UPDATE", logLevel, logMessage, employeeCode);
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	
	public List<Properties>  updateDeliveryDataBySp(Map parameterMap) throws Exception {
		log.info("updateDeliveryDataBySp...");
		String spType = new String("");
		String employeeCode = new String("");
		String brandCode = new String("");
		String orderTypeCode =new String("");
		String deliveryNo =new String("");
		
		Map resultMap  = new HashMap(0);
		Map messageMap = new HashMap();
		Map updateMap  = new HashMap(0);
		Map findMap    = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			spType = (String) PropertyUtils.getProperty(otherBean, "spType");
			employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			deliveryNo = (String) PropertyUtils.getProperty(otherBean, "deliveryNo");
			updateMap.put("employeeCode",employeeCode);
			log.info("spType="+spType);
			if( soDeliveryHeadDAO.SP_TYPE_CUSTOMER_PO_NO.equals(spType)){
		    	log.info(deliveryNo+"/"+(String) PropertyUtils.getProperty(otherBean, "customerPoNo"));
		    	updateMap.put("deliveryNo",deliveryNo);
		    	updateMap.put("customerPoNo",(String) PropertyUtils.getProperty(otherBean, "customerPoNo"));
			}else if(soDeliveryHeadDAO.SP_TYPE_SALES_ORDER_ID.equals(spType)){
		    	updateMap.put("salesOrderId",(String) PropertyUtils.getProperty(otherBean, "salesOrderId"));
			 }else if(soDeliveryHeadDAO.SP_TYPE_SALES_ORDER_DATE.equals(spType)){
			    updateMap.put("SalesOrderDate",(String) PropertyUtils.getProperty(otherBean, "SalesOrderDate"));
			 }else if(soDeliveryHeadDAO.SP_TYPE_LADING_NO.equals(spType)){
			    updateMap.put("deliveryNo",deliveryNo);
			 }else if(soDeliveryHeadDAO.SP_TYPE_DELETE_LINE_DATA.equals(spType)){
			    updateMap.put("deliveryNo",deliveryNo);
			    updateMap.put("customerPoNo",(String) PropertyUtils.getProperty(otherBean, "customerPoNo"));
		    }
			updateMap.put("logType","MANUAL");
			String returnString = soDeliveryService.updateDeliveryDataBySp(spType, updateMap);
			
			resultMap.put("formId",String.valueOf( soDeliveryService.findHeadIdBySearchKey( brandCode,  orderTypeCode,  deliveryNo,  null)));
			
			if("SUCCESS".equalsIgnoreCase(returnString)){
			}else{
				resultMap.put("formId","0");
				messageMap.put("type", "ALERT");
				messageMap.put("message", returnString);
				messageMap.put("event1", null);
				messageMap.put("event2", null);	
			}
		} catch (Exception ex) {
			resultMap.put("formId","0");
			log.error("更新入提資料時發生錯誤，原因：" + ex.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", "更新入提資料失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
		}
		resultMap.put("vatMessage", messageMap);
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	

    /**
	 * 綑綁回前畫面的視窗
	 *
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, SoDeliveryHead head, boolean isStartProcess, boolean isExecFunction) {
		log.info("wrappedMessageBox...............");
		String orderTypeCode = head.getOrderTypeCode();
		String orderNo = head.getOrderNo();
		String status = head.getStatus();
		String identification = orderTypeCode + "-" + orderNo;

		Command cmd_ok = new Command();
		Command cmd_cancel = new Command();
		if (isStartProcess) {
			log.info("wrappedMessageBox.startProcess");
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });

			cmd_cancel.setCmd(Command.FUNCTION);
			cmd_cancel.setParameters(new String[] { "createRefreshForm()", "" });
			// msgBox.setCancel(cmd_cancel);

		} else {
			if (isExecFunction) {
				msgBox.setMessage(identification + "表單已送出，是否列印報表?");
			} else {
				msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
			}
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			// msgBox.setType(MessageBox.CONFIRM);
		}
		msgBox.setType(MessageBox.CONFIRM);
		msgBox.setOk(cmd_ok);
		msgBox.setCancel(cmd_cancel);
		if (isExecFunction) {
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
			msgBox.setBefore(cmd_bf);
		}
	}

    /**
	 * pop 錯誤訊息
	 */
	private void listErrorMsg(MessageBox msgBox) {
		Command cmd_ok = new Command();
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		msgBox.setType(MessageBox.ALERT);
		cmd_ok.setCmd(Command.FUNCTION);
		cmd_ok.setParameters(new String[] { "showMessage()", "" });

		msgBox.setOk(cmd_ok);
	}



}

