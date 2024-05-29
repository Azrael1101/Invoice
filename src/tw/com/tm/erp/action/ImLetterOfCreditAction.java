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
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImLetterOfCreditService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class ImLetterOfCreditAction {
    
    private static final Log log = LogFactory.getLog(ImLetterOfCreditAction.class);
    
    private ImLetterOfCreditService imLetterOfCreditService;
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public void setImLetterOfCreditService(
			ImLetterOfCreditService imLetterOfCreditService) {
		this.imLetterOfCreditService = imLetterOfCreditService;
	}
    public void setWfApprovalResultService(
			WfApprovalResultService wfApprovalResultService) {
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
		try {
			Map resultMap = imLetterOfCreditService.updateImLetterOfCreditForBackground(parameterMap);
			ImLetterOfCreditHead imLetterOfCreditHead = (ImLetterOfCreditHead) resultMap.get("entityBean");
			
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			
			Object otherBean = parameterMap.get("vatBeanOther");
			String processId = (String) PropertyUtils.getProperty(otherBean,
					"processId");
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, imLetterOfCreditHead, true, true);
			} else {
				wrappedMessageBox(msgBox, imLetterOfCreditHead, false, true);
			}
			
		} catch (ValidationErrorException ve) {
			log.error("執行信用狀檢核lcNo失敗，原因：" + ve.toString());
			msgBox.setMessage(ve.getMessage());
			listErrorMsg(msgBox);
		} catch (Exception ex) {
			log.error("執行信用狀存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
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
		    returnMap = imLetterOfCreditService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行信用狀存檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 */
    public List<Properties> performTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		ImLetterOfCreditHead head = null;
		Object otherBean = null;
		try {
		
			Object formBean = parameterMap.get("vatBeanFormBind");
			otherBean = parameterMap.get("vatBeanOther");
			
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			
			// 驗證必要欄位
			String status = (String)PropertyUtils.getProperty(formBean, "status");
			if(!StringUtils.hasText(status)){
		    	throw new ValidationErrorException("status參數為空值，無法執行存檔！");
		    }
			
			// 前端資料塞入bean
			resultMap = imLetterOfCreditService.updateImLetterOfCredit(parameterMap);
			
			// 檢核與 存取db head and line
			resultMap = imLetterOfCreditService.updateAJAXImLetterOfCredit(parameterMap, formAction);
			
			head = (ImLetterOfCreditHead) resultMap.get("entityBean");	
			
			msgBox.setMessage((String) resultMap.get("resultMsg") );
			
			executeProcess(otherBean, resultMap, head, msgBox, false);
		
		} catch( ValidationErrorException ve ){
			log.error("執行信用狀檢核失敗，原因：" + ve.toString());
			if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			}else{
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			log.error("執行信用狀存檔失敗，原因：" + ex.toString());
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
		ImLetterOfCreditHead head = null;
		try {
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			
			resultMap = imLetterOfCreditService.updateAJAXImLetterOfCredit(parameterMap, formAction); 
			msgBox.setMessage((String) resultMap.get("resultMsg") );
			
			head = (ImLetterOfCreditHead) resultMap.get("entityBean");
			//==============execute flow==================
			if (head != null){
				executeProcess(otherBean, resultMap, head, msgBox, true);
			}
		} catch (Exception ex) {
			message = "執行移倉單背景存檔失敗，原因：" + ex.toString();
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
    private void executeProcess(Object otherBean, Map resultMap,
    		ImLetterOfCreditHead imLetterOfCreditHead, MessageBox msgBox, Boolean isBackground)
			throws Exception {

		String message = null;
		String identification = MessageStatus.getIdentification(imLetterOfCreditHead.getBrandCode(), 
				ImLetterOfCreditService.LC_ID, imLetterOfCreditHead.getReserve5());
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String status = imLetterOfCreditHead.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, status);

			log.info( "processId = " + processId );
			log.info( "assignmentId = " + assignmentId );
			log.info( "approvalResult = " + approvalResult );
			log.info( "approvalComment = " + approvalComment );
			log.info( "status = " + status );
			log.info( "approvalResultMsg = " + approvalResultMsg );
			
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null && !isBackground ){
					wrappedMessageBox(msgBox, imLetterOfCreditHead, true, false);
				}
//				if( !MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) ){  // 代表按送出或暫存或背景送出且沒有出錯
				ImLetterOfCreditService.startProcess(imLetterOfCreditHead); 
//				}
				
			} else {
				if (msgBox != null){  // 代表在流程,lcNo已存進去,不用care了,要readOnly
					wrappedMessageBox(msgBox, imLetterOfCreditHead, false, false);
				}
//				if (!OrderStatus.FORM_SAVE.equals(status)) {
					if (StringUtils.hasText(assignmentId)) {
						Long assignId = NumberUtils.getLong(assignmentId);
						Boolean result = Boolean.valueOf(approvalResult);
						Object[] processInfo = ImLetterOfCreditService
								.completeAssignment(assignId, result);
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap,
								wfApprovalResultService);
					} else {
						throw new ProcessFailedException(
								"Complete assignment失敗，ProcessId=" + processId
										+ "、AssignmentId=" + assignmentId
										+ "、result=" + approvalResult);
					}
				}
//			}
		} catch (Exception ex) {
			message = "執行信用狀流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(ImLetterOfCreditService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,imLetterOfCreditHead.getLastUpdatedBy());
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
    private void wrappedMessageBox(MessageBox msgBox, ImLetterOfCreditHead head, boolean isStartProcess, boolean isExecFunction ){
    	
    	String lcNo = head.getLcNo();
    	String status = head.getStatus();

    	String identification = ImLetterOfCreditService.LC_ID + "-" + lcNo;

    	
    	Command cmd_ok = new Command();
    	if (isStartProcess) {
		    cmd_ok.setCmd(Command.FUNCTION);
		    log.info( "是否出錯 = " + MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) );
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
    			msgBox.setMessage(identification + "表單已送出！");		
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
