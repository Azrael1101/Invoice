package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ImItemBarcodeHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImItemBarcodeHeadAction {
    private static final Log log = LogFactory.getLog(ImItemBarcodeHeadAction.class);
    
    private ImItemBarcodeHeadService imItemBarcodeHeadService;
    private WfApprovalResultService wfApprovalResultService;
    private SiProgramLogAction siProgramLogAction;
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
        this.wfApprovalResultService = wfApprovalResultService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
        this.siProgramLogAction = siProgramLogAction;
    }

    public void setImItemBarcodeHeadService(ImItemBarcodeHeadService imItemBarcodeHeadService) {
        this.imItemBarcodeHeadService = imItemBarcodeHeadService;
    }

    /**
     * 執行流程
     * @param otherBean
     * @param resultMap
     * @param imAdjustmentHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap, ImItemBarcodeHead head, MessageBox msgBox)throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
	try {
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
	    String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
//	    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
	    String formStatus = head.getStatus();

	    log.info( "processId = " + processId );
	    log.info( "assignmentId = " + assignmentId );
	    log.info( "approvalResult = " + approvalResult );
//	    log.info( "approvalComment = " + approvalComment );
	    log.info( "formStatus = " + formStatus );

	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, true, false);
		}
		ImItemBarcodeHeadService.startProcess(head);
	    } else {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, head, false, false);
		}
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = ImItemBarcodeHeadService.completeAssignment(assignId, result);
//		    if (!OrderStatus.SAVE.equals(formStatus) ) {
//			resultMap.put("processId", processInfo[0]);
//			resultMap.put("activityId", processInfo[1]);
//			resultMap.put("activityName", processInfo[2]);
//			resultMap.put("result", approvalResultMsg);
//			resultMap.put("approvalComment", approvalComment);
//			WfApprovalResultUtils.logApprovalResult(resultMap,
//				wfApprovalResultService);
//		    }
		} else {
		    throw new ProcessFailedException(
			    "Complete assignment失敗，ProcessId=" + processId
			    + "、AssignmentId=" + assignmentId
			    + "、result=" + approvalResult);
		    		
		}

	    }
	} catch (Exception ex) {
	    message = Imformation.REPLENISH_BARCODE.getActionExecuteProcess() + ex.toString();
	    siProgramLogAction.createProgramLog(ImItemBarcodeHeadService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,head.getLastUpdatedBy());
	    log.error(message);
	}
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
	    Map resultMap = imItemBarcodeHeadService.updateImAdjustmentWithActualOrderNO(parameterMap);
	    ImItemBarcodeHead head = (ImItemBarcodeHead) resultMap.get("entityBean");

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
	    log.error(Imformation.REPLENISH_BARCODE.getActionPerformTransaction() + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
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
    
    /**
     * 初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performInitial(Map parameterMap){
	Map returnMap = null;	
	try{
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    
	    returnMap = imItemBarcodeHeadService.executeInitial(parameterMap);
	}catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getActionInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    /**
     * 查詢補條碼初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imItemBarcodeHeadService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getActionSearchInitial() + ex.toString());
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
	ImItemBarcodeHead head = null;
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formBean = parameterMap.get("vatBeanFormBind");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    // 驗證必要欄位
	    String status = (String)PropertyUtils.getProperty(formBean, "status");

	    if(!StringUtils.hasText(status)){
		throw new ValidationErrorException("status參數為空值，無法執行存檔！");
	    }
	    // 前端資料塞入bean
	    resultMap = imItemBarcodeHeadService.updateBean(parameterMap); 

	    // 檢核與 存取db head and line
	    resultMap = imItemBarcodeHeadService.updateAJAXBarcode(parameterMap);
	    head = (ImItemBarcodeHead) resultMap.get("entityBean");	

	    msgBox.setMessage((String) resultMap.get("resultMsg") );

	    executeProcess(otherBean, resultMap, head, msgBox);
	} catch( ValidationErrorException ve ){
	    log.error(Imformation.REPLENISH_BARCODE.getActionError() + ve.toString());
	    if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		listErrorMsg(msgBox);
	    }else{
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
	    }	
	} catch (Exception ex) {
	    log.error(Imformation.REPLENISH_BARCODE.getActionPerformTransaction() + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 補條碼背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	ImItemBarcodeHead head = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    resultMap = imItemBarcodeHeadService.updateAJAXBarcode(parameterMap); 
	    head = (ImItemBarcodeHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (head != null){
		executeProcess(otherBean, resultMap, head, null);
	    }
	} catch (Exception ex) {
	    message = Imformation.REPLENISH_BARCODE.getActionPerformTransactionForBackGround() + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, ImItemBarcodeHead head, boolean isStartProcess, boolean isExecFunction ){

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
    
}
