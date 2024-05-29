package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.service.AdCustomerServiceService;
import tw.com.tm.erp.hbm.service.BuPurchaseService;
import tw.com.tm.erp.hbm.service.BuSupplierModService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class AdCustomerServiceAction {

	private static final Log log = LogFactory.getLog(AdCustomerServiceAction.class);	

	private AdCustomerServiceService adCustomerServiceService;

	private WfApprovalResultService wfApprovalResultService;

	public void setAdCustomerServiceService(AdCustomerServiceService adCustomerServiceService) {
		this.adCustomerServiceService = adCustomerServiceService;
	}
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = adCustomerServiceService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行客服維護存檔初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ 
		log.info("performTransaction");
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			adCustomerServiceService.validateHead(parameterMap);
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			adCustomerServiceService.executeFindActualBuPurchase(parameterMap);  //在此塞入前端單頭值
			// 前端資料塞入bean
			adCustomerServiceService.updateBuGoalAchevementBean(parameterMap);   //沒有功能
			// 存檔							
			resultMap = adCustomerServiceService.updateAJAXBuGoalAchevement(parameterMap);
			AdCustServiceHead adCustServiceHead = (AdCustServiceHead) resultMap.get("entityBean");
			wrappedMessageBox(msgBox, adCustServiceHead, true,false);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			//executeProcess(otherBean, resultMap, adCustServiceHead, msgBox);

		} catch (ProcessFailedException px) {
			log.error("執行地點維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行地點維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	/**
	 * 轉派
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> doClose(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		try {
			HashMap resultMap = adCustomerServiceService.updatedoClose(parameterMap);

			msgBox.setMessage("關檔成功");
			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.WIN_CLOSE);
			msgBox.setOk(cmd_ok);
			msgBox.setType(MessageBox.ALERT);
		} catch (Exception ex) {
			System.out.println("轉派失敗，原因：" + ex.toString());
			log.error("轉派失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param locationBean
	 * @param isStartProcess
	 * @param isExecFunction 
	 */
	private void wrappedMessageBox(MessageBox msgBox, AdCustServiceHead adCustServiceHead, boolean isStartProcess, boolean isExecFunction){

		Long headId = adCustServiceHead.getHeadId();

		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(headId + "表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}
		msgBox.setOk(cmd_ok);
	}

	/**
	 * 起流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param suppliermod
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, AdCustServiceHead adCustServiceHead, MessageBox msgBox)
	throws Exception {
		log.info("new executeProcess");
		String message = null;
		try {

			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			//	Boolean approvalResult = (Boolean) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeStatus");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			//	String nextStatus = (String) PropertyUtils.getProperty(otherBean, "nextStatus");
			String nextStatus = adCustServiceHead.getStatus();
			String approvalResultMsg = getApprovalResult(processId, beforeStatus,nextStatus);
			log.info("approvalResult=====" + approvalResult);
			log.info("process起動");
			//若無processId 則 startProcess
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {

				if (msgBox != null){
					wrappedMessageBox(msgBox, adCustServiceHead,  true, false);
				}                                          //---起流程---
				Object[] processObj = AdCustomerServiceService.startProcess(adCustServiceHead); // 起流程後，取回流程的processId
				// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
				System.out.println("流程 起始 id ======> " + processObj[0]);
				adCustServiceHead.setProcessId((Long) processObj[0]);
				log.info("processId="+ adCustServiceHead.getProcessId() );
				//有processId 則completeAssignment	
			} else {
				if (msgBox != null){
					wrappedMessageBox(msgBox, adCustServiceHead, false ,false);
				}
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					//	ceapProcessService.updateProcessSubject(Long.valueOf(processId), buSupplierModService.getProcessSubject(buSupplierMod));
					log.info("login....assignmentIdSTART" );
					Object[] processInfo = AdCustomerServiceService.completeAssignment(assignId, result, adCustServiceHead);
					//			System.out.println("流程 起始 id >>>>>>> " + processInfo[0]);
					log.info("assignId=" + assignId);
					resultMap.put("processId", processInfo[0]);
					resultMap.put("activityId", processInfo[1]);
					resultMap.put("activityName", processInfo[2]);
					resultMap.put("result", approvalResultMsg);
					//	resultMap.put("lastUpdatedBy", loginEmployeeCode);
					resultMap.put("approvalComment", approvalComment);
					WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId + "、result=" + approvalResult);
				}
			}
		} catch (Exception ex) {
			message = "" +
			"" +
			"，原因：" + ex.toString();
			log.error(message);
		}
	}
	private String getApprovalResult(String processId, String beforeStatus, String nextStatus){
		String approvalResult = "送出";
		if(StringUtils.hasText(processId)){	
			if(OrderStatus.SAVE.equals(nextStatus) || OrderStatus.VOID.equals(nextStatus)
					|| OrderStatus.REJECT.equals(nextStatus)){
				approvalResult = OrderStatus.getChineseWord(nextStatus);
			}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)){
				approvalResult = "核准";
			}
		}
		return approvalResult;
	}
	
	public List<Properties> performSearchInitial(Map parameterMap){
		log.info("performSearchInitial");
		Map returnMap = null;	
		try{
			returnMap = adCustomerServiceService.executeSearchInitial(parameterMap);


		}catch (Exception ex) {
			System.out.println("執行採購單查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
}




