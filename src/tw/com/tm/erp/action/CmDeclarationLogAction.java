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
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.CmDeclarationLog;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CmDeclarationLogService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class CmDeclarationLogAction {
    
    private static final Log log = LogFactory.getLog(CmDeclarationLogAction.class);
    
    private CmDeclarationLogService cmDeclarationLogService;

    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;

	public void setCmDeclarationLogService(CmDeclarationLogService cmDeclarationLogService) {
		this.cmDeclarationLogService = cmDeclarationLogService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }

    /**
	 * 取得單號
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */

	public List<Properties> getOrderNo(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		CmDeclarationLog head = new CmDeclarationLog();
		Object otherBean = null;
		try {
			otherBean = parameterMap.get("vatBeanOther");
			String status = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			
			// 驗證必要欄位
			if(!StringUtils.hasText(status))
		    	throw new Exception("status參數為空值，無法執行存檔！");
			if(!StringUtils.hasText(formAction))
		    	throw new Exception("formAction參數為空值，無法執行存檔！");
			if(formId == 0L)
				throw new Exception("identify參數為空值，無法執行存檔！");
			if(!OrderStatus.SIGNING.equals(status))
				cmDeclarationLogService.updateParameter(parameterMap);
				head.setStatus(status);
				head.setIdentify(formId);
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, head, true, true);
			} else {
				wrappedMessageBox(msgBox, head, false, true);
			}
		} catch (ValidationErrorException ve) {
			log.error("執行報關單儲存失敗，原因：" + ve.toString());
			msgBox.setMessage(ve.getMessage());
			listErrorMsg(msgBox);
		} catch (Exception ex) {
			log.error("執行報關單儲存失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

    
    public List<Properties> performTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		CmDeclarationLog head = new CmDeclarationLog();
		Object otherBean = null;
		try {
			otherBean = parameterMap.get("vatBeanOther");
			String status = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			//log.info("status = " + status);
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			//log.info("formAction = " + formAction);
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			//log.info("formId = " + formId);
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
			head.setStatus(status);
			head.setIdentify(formId);
			head.setBrandCode(brandCode);
			head.setLastUpdatedBy(employeeCode);
			head.setOrderNo(String.valueOf(formId));
			head.setOrderTypeCode("CDL");
			// 驗證必要欄位
			if(!StringUtils.hasText(status))
		    	throw new Exception("status參數為空值，無法執行存檔！");
			if(!StringUtils.hasText(formAction))
		    	throw new Exception("formAction參數為空值，無法執行存檔！");
			if(formId == 0L)
				throw new Exception("identify參數為空值，無法執行存檔！");
			if(!OrderStatus.SIGNING.equals(status)){
				cmDeclarationLogService.updateParameter(parameterMap);
				if(OrderStatus.SIGNING.equals(formAction)){
					resultMap = cmDeclarationLogService.updateCheckedParameter(parameterMap);
					head.setStatus(formAction);
				}
			}
			msgBox.setMessage("報關單已送出");
			executeProcess(otherBean, resultMap, head, msgBox, false);
		} catch(FormException ve){
			log.error("執行報關單修改失敗，原因：" + ve.toString());
			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			listErrorMsg(msgBox);
		} catch (Exception ex) {
			log.error("執行報關單修改失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {

		Map returnMap = new HashMap(0);
		Map resultMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String message = null;
		CmDeclarationLog head = new CmDeclarationLog();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String status = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			head.setIdentify(formId);
			head.setStatus(status);
			try {
				log.info("for BG formAction = " + formAction);
				if(OrderStatus.SIGNING.equals(status) && OrderStatus.SIGNING.equals(formAction)){
					cmDeclarationLogService.updateCheckedParameter(parameterMap);
					head.setStatus(formAction);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
			msgBox.setMessage("表單已送出");
			executeProcess(otherBean, resultMap, head, msgBox, true);
		}catch(Exception ex){
			message = "執行銷貨單背景存檔失敗，原因：" + ex.toString();
    	    log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
    
    /**
     * 執行流程
     * @param otherBean
     * @param resultMap
     * @param cmMovementHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap, CmDeclarationLog cmDeclarationLog, MessageBox msgBox, Boolean isBackground)
			throws Exception {
    	resultMap.put("entityBean" , cmDeclarationLog);
		String message = null;
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String status = cmDeclarationLog.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, status);

			log.info( "processId = " + processId );
			log.info( "assignmentId = " + assignmentId );
			log.info( "approvalResult = " + approvalResult );
			log.info( "approvalComment = " + approvalComment );
			log.info( "status = " + status );
			log.info( "approvalResultMsg = " + approvalResultMsg );
			
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null && !isBackground ){
					wrappedMessageBox(msgBox, cmDeclarationLog, true, false);
				}
					CmDeclarationLogService.startProcess(cmDeclarationLog); 
			} else {
				if (msgBox != null){  // 代表在流程,lcNo已存進去,不用care了,要readOnly
					wrappedMessageBox(msgBox, cmDeclarationLog, false, false);
				}
//				if (!OrderStatus.FORM_SAVE.equals(status)) {
					if (StringUtils.hasText(assignmentId)) {
						Long assignId = NumberUtils.getLong(assignmentId);
						Boolean result = Boolean.valueOf(approvalResult);
						Object[] processInfo = CmDeclarationLogService.completeAssignment(assignId, result);
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap,wfApprovalResultService);
					} else {
						throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId+ "、AssignmentId=" + assignmentId+ "、result=" + approvalResult);
					}
				}
//			}
		} catch (Exception ex) {
			message = "執行報關單修改流程發生錯誤，原因：" + ex.toString();
			log.error(message);
		}
	}
    
    /**
     * 取得簽核結果
     * @param processId
     * @param formStatus
     * @return
     */

    private String getApprovalResult(String processId, String formStatus) {

		String approvalResult = "送出";
		if (StringUtils.hasText(processId)) {
			if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)) {
				approvalResult = OrderStatus.getChineseWord(formStatus);
			}
		}
		return approvalResult;
	}

    
    /**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, CmDeclarationLog head, boolean isStartProcess, boolean isExecFunction ){
    	
    	String status = head.getStatus();
    	String identification = MessageStatus.getIdentification("T2", "CM", String.valueOf(head.getIdentify()));
    	Command cmd_ok = new Command();
    	if (isStartProcess) {
		    cmd_ok.setCmd(Command.FUNCTION);
		    //log.info( "是否出錯 = " + MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) );
//    		if( MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) ){
//    			msgBox.setType(MessageBox.ALERT);
//    			cmd_ok.setParameters(new String[]{"showMessage()", ""}); 
//    		}else{
			Command cmd_cancel = new Command();
		    cmd_cancel.setCmd(Command.WIN_CLOSE);
		    msgBox.setCancel(cmd_cancel);
		    
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
//    		}
    	}else{
    		if( isExecFunction ){
    			msgBox.setMessage(identification + "表單已送出，是否繼續新增！");		
    		}else{
    			msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
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
    
    /**
     * pop 錯誤訊息
     */
    private void listErrorMsg(MessageBox msgBox){
    	Command cmd_ok = new Command();
    	msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    	msgBox.setType(MessageBox.ALERT);
    	cmd_ok.setCmd(Command.FUNCTION);
		cmd_ok.setParameters(new String[]{"showMessage()", ""}); 
		msgBox.setOk(cmd_ok);
    }
}
