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
import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuGoalService;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ImReceiveAdjustmentService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class BuGoalAction {
    private static final Log log = LogFactory.getLog(BuGoalAction.class);

    private BuGoalService buGoalService;
    private WfApprovalResultService wfApprovalResultService;

    private SiProgramLogAction siProgramLogAction;

    public void setBuGoalService(BuGoalService buGoalService) {
	this.buGoalService = buGoalService;
    }
    public void setWfApprovalResultService(
	    WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    /**
     * 業績目標定義檔,取得單號
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> getOrderNoBasic(Map parameterMap) {

	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try {
	    Map resultMap = buGoalService.updateBuGoalWithActualOrderNO(parameterMap);
	    BuGoalHead head = (BuGoalHead) resultMap.get("entityBean");

	    msgBox.setMessage((String) resultMap.get("resultMsg") );
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String) PropertyUtils.getProperty(otherBean,
	    "processId");
	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		wrappedMessageBox(msgBox, head, true, true);
	    } else {
		wrappedMessageBox(msgBox, head, false, true);
	    }
	} catch (Exception ex) {
	    log.error("執行業績目標設定存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 業績目標設定檔,取得單號
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> getOrderNoDeploy(Map parameterMap) {

	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    Map resultMap = buGoalService.updateBuGoalDeployWithActualOrderNO(parameterMap);
	    BuGoalDeployHead headNew = (BuGoalDeployHead) resultMap.get("entityBean");

	    msgBox.setMessage((String) resultMap.get("resultMsg") );
	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		wrappedMessageBox(msgBox, headNew, true, true);
	    } else {
		wrappedMessageBox(msgBox, headNew, false, true);
	    }
	} catch (Exception ex) {
	    log.error("執行業績目標設定存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 目標佔比/人員佔比 初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = buGoalService.executeInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行目標維護存檔初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 目標設定初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performDeployInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = buGoalService.executeDeployInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行目標設定存檔初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 查詢目標佔比/人員設定 初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = buGoalService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行查詢目標佔比/人員設定存檔初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 查詢目標設定初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchDeployInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = buGoalService.executeSearchDeployInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行查詢目標設定存檔初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 基本檔 送出
     * @param parameterMap 
     * @return
     */
    public List<Properties> performTransaction(Map parameterMap){ 

	Map returnMap = new HashMap(0); 
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap(0); 
	try {  
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    log.info("updateBuGoalBean");
	    // 前端資料塞入bean
	    buGoalService.updateBuGoalBean(parameterMap);
	    log.info("updateAJAXBuGoal");
	    // 存檔							
	    resultMap = buGoalService.updateAJAXBuGoal(parameterMap);
	    log.info("AFTER updateAJAXBuGoal");
	    BuGoalHead buGoalHead = (BuGoalHead) resultMap.get("entityBean");
	    log.info("AFTER resultMap.get(entityBean)");
	    msgBox.setMessage((String) resultMap.get("resultMsg"));
	    log.info("AFTER setMessage");
	    executeProcess(otherBean, resultMap, buGoalHead, msgBox);
	    log.info("AFTER executeProcess");
	} catch (ValidationErrorException ve) {
	    log.error("執行業績目標基本檔檢核失敗，原因：" + ve.toString());
	    if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		listErrorMsg(msgBox);
	    }else{
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
	    }	
	} catch (Exception ex) {
	    log.error("執行業績目標基本檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}

	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 業績目標設定 送出
     * @param parameterMap 
     * @return
     */
    public List<Properties> performTransactionDeploy(Map parameterMap){ 

	Map returnMap = new HashMap(0); 
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap(0); 
	try {  
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    // 前端資料塞入bean
	    buGoalService.updateBuGoalDeployBean(parameterMap);

	    // 存檔							
	    resultMap = buGoalService.updateAJAXBuGoalDeploy(parameterMap);

	    BuGoalDeployHead buGoalDeployHead = (BuGoalDeployHead) resultMap.get("entityBean");
	    
	    msgBox.setMessage((String) resultMap.get("resultMsg"));

	    executeProcess(otherBean, resultMap, buGoalDeployHead, msgBox);

	} catch( ValidationErrorException ve ){
	    log.error("執行業績目標設定檢核失敗，原因：" + ve.toString());
	    if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		listErrorMsg(msgBox);
	    }else{
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
	    }	
	} catch (Exception ex) {
	    log.error("執行業績目標設定存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}

	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    /**
     * 業績目標定義檔 背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	BuGoalHead head = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    resultMap = buGoalService.updateAJAXBuGoal(parameterMap); 
	    head = (BuGoalHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (head != null){
		executeProcess(otherBean, resultMap, head, null);
	    }
	} catch (Exception ex) {
	    message = "執行業績目標定義檔背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    /**
     * 業績目標設定 背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGroundDeploy(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	BuGoalDeployHead head = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    resultMap = buGoalService.updateAJAXBuGoalDeploy(parameterMap); 
	    head = (BuGoalDeployHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (head != null){
		executeProcess(otherBean, resultMap, head, null);
	    }
	} catch (Exception ex) {
	    message = "執行業績目標設定背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 業績目標定義檔 執行流程
     * @param otherBean
     * @param resultMap
     * @param imAdjustmentHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap,
	    BuGoalHead head, MessageBox msgBox)
    throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(
		head.getBrandCode(), BuGoalService.BU_GOAL_BASIC, head.getReserve1());
	try {
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
	    String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
	    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus = head.getStatus();
	    String approvalResultMsg = this.getApprovalResult(processId, formStatus);

	    log.info( "processId = " + processId );
	    log.info( "assignmentId = " + assignmentId );
	    log.info( "approvalResult = " + approvalResult );
	    log.info( "approvalComment = " + approvalComment );
	    log.info( "formStatus = " + formStatus );
	    log.info( "approvalResultMsg = " + approvalResultMsg );

	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, true, false);
		}
		BuGoalService.startProcessBasic(head);
	    } else {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, false, false);
		}
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = BuGoalService
		    .completeAssignment(assignId, result);
		    if (!OrderStatus.SAVE.equals(formStatus) ) {
			resultMap.put("processId", processInfo[0]);
			resultMap.put("activityId", processInfo[1]);
			resultMap.put("activityName", processInfo[2]);
			resultMap.put("result", approvalResultMsg);
			resultMap.put("approvalComment", approvalComment);

			// 補回 orderTypeCode, orderNo 使可寫入簽核紀錄
			BuGoalHead buGoalHead = (BuGoalHead)resultMap.get("entityBean");
			buGoalHead.setOrderTypeCode(BuGoalService.BU_GOAL_BASIC);
			buGoalHead.setOrderNo(buGoalHead.getReserve1());
			resultMap.put("entityBean", buGoalHead);

			WfApprovalResultUtils.logApprovalResult(resultMap,
				wfApprovalResultService);
		    }
		} else {
		    throw new ProcessFailedException(
			    "Complete assignment失敗，ProcessId=" + processId
			    + "、AssignmentId=" + assignmentId
			    + "、result=" + approvalResult);
		}

	    }
	} catch (Exception ex) {
	    message = "執行業績目標設定流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(BuGoalService.BASIC_PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    log.error(message);
	}
    }

    /**
     * 業績目標設定檔 執行流程
     * @param otherBean
     * @param resultMap
     * @param imAdjustmentHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap,
	    BuGoalDeployHead head, MessageBox msgBox)
    throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(
		head.getBrandCode(), BuGoalService.BU_GOAL_DEPLOY, head.getReserve1());
	try {
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
	    String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
	    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus = head.getStatus();
	    String approvalResultMsg = this.getApprovalResult(processId, formStatus);

	    log.info( "processId = " + processId );
	    log.info( "assignmentId = " + assignmentId );
	    log.info( "approvalResult = " + approvalResult );
	    log.info( "approvalComment = " + approvalComment );
	    log.info( "formStatus = " + formStatus );
	    log.info( "approvalResultMsg = " + approvalResultMsg );

	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, true, false);
		}
		BuGoalService.startProcess(head);
	    } else {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, false, false);
		}
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = BuGoalService
		    .completeAssignment(assignId, result);
		    if (!OrderStatus.SAVE.equals(formStatus) ) {
			resultMap.put("processId", processInfo[0]);
			resultMap.put("activityId", processInfo[1]);
			resultMap.put("activityName", processInfo[2]);
			resultMap.put("result", approvalResultMsg);
			resultMap.put("approvalComment", approvalComment);

			// 補回 orderTypeCode, orderNo 使可寫入簽核紀錄
			BuGoalDeployHead buGoalDeployHead = (BuGoalDeployHead)resultMap.get("entityBean");
			buGoalDeployHead.setOrderTypeCode(BuGoalService.BU_GOAL_DEPLOY);
			buGoalDeployHead.setOrderNo(buGoalDeployHead.getReserve1());
			resultMap.put("entityBean", buGoalDeployHead);

			WfApprovalResultUtils.logApprovalResult(resultMap,
				wfApprovalResultService);
		    }
		} else {
		    throw new ProcessFailedException(
			    "Complete assignment失敗，ProcessId=" + processId
			    + "、AssignmentId=" + assignmentId
			    + "、result=" + approvalResult);
		}

	    }
	} catch (Exception ex) {
	    message = "執行業績目標設定流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(BuGoalService.DEPLOY_PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
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
	    if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus) || OrderStatus.REJECT.equals(formStatus) ) {
		approvalResult = OrderStatus.getChineseWord(formStatus);
	    }else if(OrderStatus.SIGNING.equals(formStatus)){
		approvalResult = "核准";
	    }
	}
	return approvalResult;
    }

    /**
     * 綑綁MESSAGE 業績目標定義
     * @param msgBox
     * @param locationBean
     * @param isStartProcess
     */
    private void wrappedMessageBox(MessageBox msgBox, BuGoalHead buGoalHead, boolean isStartProcess){

	Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"createRefreshForm()", "" });
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}

	msgBox.setOk(cmd_ok);
    }

    /**
     * 綑綁回前畫面的視窗 業績目標設定
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, BuGoalDeployHead head, boolean isStartProcess, boolean isExecFunction ){

	String status = head.getStatus();
	String identification = BuGoalService.BU_GOAL_DEPLOY + "-" + head.getReserve1();

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
     * 綑綁回前畫面的視窗 業績目標設定
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, BuGoalHead head, boolean isStartProcess, boolean isExecFunction ){

	String status = head.getStatus();
	String identification = BuGoalService.BU_GOAL_DEPLOY + "-" + head.getReserve1();

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
