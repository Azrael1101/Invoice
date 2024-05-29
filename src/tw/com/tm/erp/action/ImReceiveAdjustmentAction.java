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
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImReceiveAdjustmentService;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImReceiveAdjustmentAction {
    private static final Log log = LogFactory.getLog(ImReceiveAdjustmentAction.class);

    private ImReceiveAdjustmentService imReceiveAdjustmentService;

    private WfApprovalResultService wfApprovalResultService;

    private SiProgramLogAction siProgramLogAction;
    
    //for 儲位用
    private ImStorageAction imStorageAction;

    public void setImReceiveAdjustmentService(ImReceiveAdjustmentService imReceiveAdjustmentService) {
	this.imReceiveAdjustmentService = imReceiveAdjustmentService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setWfApprovalResultService(
	    WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }

	//for 儲位用
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
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
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
	    Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    //取得欲更新的bean
    	    ImAdjustmentHead imAdjustmentHead = imReceiveAdjustmentService.getActualImAdjustment(formLinkBean); 
	    
	    //確認是否允許流程，不允許會丟例外
	    ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imAdjustmentHead.getProcessId()), processId, assignmentId);
	    
	    Map resultMap = imReceiveAdjustmentService.updateImAdjustmentWithActualOrderNO(parameterMap);
	    ImAdjustmentHead imAdjustmentHeadNew = (ImAdjustmentHead) resultMap.get("entityBean");

	    msgBox.setMessage((String) resultMap.get("resultMsg") );
	    if (processId == 0) {
		wrappedMessageBox(msgBox, imAdjustmentHeadNew, true, true);
	    } else {
		wrappedMessageBox(msgBox, imAdjustmentHeadNew, false, true);
	    }
	} catch (Exception ex) {
	    log.error("執行調整單存檔失敗，原因：" + ex.toString());
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
	    returnMap = imReceiveAdjustmentService.executeInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行進貨短溢調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imReceiveAdjustmentService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行進貨短溢調整單查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 進貨短溢調整單送出 
     * @param parameterMap
     * @return
     */
    public List<Properties> performMoreOrLessTransaction(Map parameterMap){

    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	ImAdjustmentHead imAdjustmentHead = null;
    	try {

    		Object formBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Object formLinkBean = parameterMap.get("vatBeanFormLink");

    		String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

    		// 驗證必要欄位
    		String status = (String)PropertyUtils.getProperty(formBean, "status");

    		if(!StringUtils.hasText(status)){
    			throw new ValidationErrorException("status參數為空值，無法執行存檔！");
    		}

    		imAdjustmentHead = imReceiveAdjustmentService.getActualImAdjustment(formLinkBean);

    		// 流程控制，避免重複流程造成的錯誤 
    		Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
    		Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
    		// 確認是否允許流程，不允許會丟例外
    		ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imAdjustmentHead.getProcessId()), processId, assignmentId);

    		// 前端資料塞入bean
    		resultMap = imReceiveAdjustmentService.updateImAdjustmentBean(parameterMap); 

    		//for 儲位用
    		if(imStorageAction.isStorageExecute(imAdjustmentHead)){
    			//取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
    			imAdjustmentHead = imReceiveAdjustmentService.getActualImAdjustment(formLinkBean);
    			executeStorage(imAdjustmentHead);
    		}

    		// 檢核與 存取db head and line
    		resultMap = imReceiveAdjustmentService.updateImReceiveMoreOrLessAdjustment(parameterMap, formAction);

    		msgBox.setMessage((String) resultMap.get("resultMsg") );
    		imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");	

    		executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);

    		imReceiveAdjustmentService.update(imAdjustmentHead);

    	} catch (StorageException sex) {
    		msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
    		msgBox.setType(MessageBox.CONFIRM);
    		Command cmd_bf = new Command();
    		cmd_bf.setCmd(Command.FUNCTION);
    		cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
    		msgBox.setOk(cmd_bf);
    		//msgBox.setCancel(cmd_cancel);
    	} catch( ValidationErrorException ve ){
    		log.error("執行進貨短溢調整單檢核失敗，原因：" + ve.toString());
    		if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    			listErrorMsg(msgBox);
    		}else{
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    		}	
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("執行進貨短溢調整單存檔失敗，原因：" + ex.toString());
    		msgBox.setMessage(ex.getMessage());
    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 進貨短溢調整單背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performMoreOrLessTransactionForBackGround(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	ImAdjustmentHead imAdjustmentHead = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

	    resultMap = imReceiveAdjustmentService.updateImReceiveMoreOrLessAdjustment(parameterMap, formAction); 
	    imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (imAdjustmentHead != null){
		executeProcess(otherBean, resultMap, imAdjustmentHead, null);
	    }
	    imReceiveAdjustmentService.update(imAdjustmentHead);
	} catch (Exception ex) {
	    message = "執行進貨短溢調整單背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 執行流程
     * @param otherBean
     * @param resultMap
     * @param imAdjustmentHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap,
	    ImAdjustmentHead imAdjustmentHead, MessageBox msgBox)
    throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(
		imAdjustmentHead.getBrandCode(), imAdjustmentHead
		.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
	try {
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
	    String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
	    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus = imAdjustmentHead.getStatus();
	    String approvalResultMsg = this.getApprovalResult(processId, formStatus);

	    log.info( "processId = " + processId );
	    log.info( "assignmentId = " + assignmentId );
	    log.info( "approvalResult = " + approvalResult );
	    log.info( "approvalComment = " + approvalComment );
	    log.info( "formStatus = " + formStatus );
	    log.info( "approvalResultMsg = " + approvalResultMsg );

	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, imAdjustmentHead, true, false);
		}
		Object processObj[] = ImReceiveAdjustmentService.startProcess(imAdjustmentHead);
		imAdjustmentHead.setProcessId((Long)processObj[0]);
	    } else {
		if (msgBox != null)
		    wrappedMessageBox(msgBox, imAdjustmentHead, false, false);
//		if (!OrderStatus.FORM_SAVE.equals(formStatus)) {
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = ImReceiveAdjustmentService
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
//		}
	    }
	} catch (Exception ex) {
	    message = "執行調整單流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(ImReceiveAdjustmentService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,imAdjustmentHead.getLastUpdatedBy());
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
    private void wrappedMessageBox(MessageBox msgBox, ImAdjustmentHead head, boolean isStartProcess, boolean isExecFunction ){

	String orderTypeCode = head.getOrderTypeCode();
	String orderNo = head.getOrderNo();
	String status = head.getStatus();
	String identification = orderTypeCode + "-" + orderNo;

	Command cmd_ok = new Command();
	if (isStartProcess) {
	    cmd_ok.setCmd(Command.FUNCTION);
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);

	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
	}else{
	    if( isExecFunction ){
		msgBox.setMessage(identification + "表單已送出！");		
	    }else{
		msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
	    }
	    if(orderTypeCode.equals("AIF")){
	    	if(head.getStatus().equals(OrderStatus.FINISH)){
	    		cmd_ok.setCmd(Command.FUNCTION);
		    	cmd_ok.setParameters(new String[]{"openDeclarationLog()", ""});
	    	}else{
	    		cmd_ok.setCmd(Command.WIN_CLOSE);
	    	}	    	
	    }else{
	    	cmd_ok.setCmd(Command.WIN_CLOSE);
	    }	    	 
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
    
	public void executeStorage(ImAdjustmentHead imAdjustmentHead) throws Exception {
		log.info("executeStorage");
		//更新儲位單頭 2013.4.15 by Caspar
		Map storageMap = new HashMap();
		storageMap.put("storageTransactionDate", "adjustmentDate");
		storageMap.put("storageTransactionType", ImStorageService.ADJ);
		storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");
		ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imAdjustmentHead);
		
		//更新儲位單身與比對 2013.4.15 by Caspar
		storageMap.put("beanItem", "imAdjustmentLines");
		storageMap.put("quantity", "difQuantity");
		imStorageAction.executeImStorageItem(storageMap, imAdjustmentHead, imStorageHead);
	}
}