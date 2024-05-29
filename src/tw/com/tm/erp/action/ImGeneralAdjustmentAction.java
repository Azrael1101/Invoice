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
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.service.FiInvoiceHeadMainService;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImGeneralAdjustmentAction {
    private static final Log log = LogFactory.getLog(ImGeneralAdjustmentAction.class);

    private ImGeneralAdjustmentService imGeneralAdjustmentService;

    private WfApprovalResultService wfApprovalResultService;

    private SiProgramLogAction siProgramLogAction;
	
    //for 儲位用
    private ImStorageAction imStorageAction;
    
    public void setImGeneralAdjustmentService(
	    ImGeneralAdjustmentService imGeneralAdjustmentService) {
	this.imGeneralAdjustmentService = imGeneralAdjustmentService;
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
	    ImAdjustmentHead imAdjustmentHead = imGeneralAdjustmentService.getActualImAdjustment(formLinkBean); 
	    
	    //確認是否允許流程，不允許會丟例外
	    //ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imAdjustmentHead.getProcessId()), processId, assignmentId);
	    
	    Map resultMap = imGeneralAdjustmentService.updateImAdjustmentWithActualOrderNO(parameterMap);
	    ImAdjustmentHead imAdjustmentHeadNew = (ImAdjustmentHead) resultMap.get("entityBean");

	    msgBox.setMessage((String) resultMap.get("resultMsg") );
	    
	    if (processId == 0) {
		wrappedMessageBox(msgBox, imAdjustmentHeadNew, true, true, otherBean);
	    } else {
		wrappedMessageBox(msgBox, imAdjustmentHeadNew, false, true, otherBean);
	    }
	} catch (Exception ex) {
	    log.error("執行調整單存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 調整單初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imGeneralAdjustmentService.executeInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 拆/併貨調整單初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performApartInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imGeneralAdjustmentService.executeApartInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行拆/併貨調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    /**
     * 離島調整單初始化
     * 
     * @param parameterMap
     * @return List<Properties> 
     */
    public List<Properties> performOffShoreIsIandInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imGeneralAdjustmentService.executeOffShoreIsIandInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行離島調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    /**
     * 查詢調整單初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imGeneralAdjustmentService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行查詢調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 查詢拆/併貨調整單初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performApartSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = imGeneralAdjustmentService.executeApartSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行查詢調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 一般調整單送出 
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransaction(Map parameterMap){

    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	ImAdjustmentHead imAdjustmentHead = null;
    	try {
    		Object formLinkBean = null;//
    		Object formBean = null;//
    		Object otherBean = null;//

    		// 驗證必要欄位
    		String status = (String)PropertyUtils.getProperty(formBean, "status");
    		if(!StringUtils.hasText(status)){
    			throw new ValidationErrorException("status參數為空值，無法執行存檔！");
    		}

    		// 取得欲更新的bean
    		imAdjustmentHead = null;//請使用imGeneralAdjustmentService.getActualImAdjustment();   傳入對應參數 完成資料存入
    		// 流程控制，避免重複流程造成的錯誤 
    		Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
    		Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
    		// 確認是否允許流程，不允許會丟例外
    		//ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imAdjustmentHead.getProcessId()), processId, assignmentId);

    		// 前端資料塞入bean
    		resultMap = null; // 請使用imGeneralAdjustmentService.updateImAdjustmentBean();  傳入對應參數 完成資料存入

    		//for 儲位用
    		if(imStorageAction.isStorageExecute(imAdjustmentHead)){
    			//取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
    			imAdjustmentHead = imGeneralAdjustmentService.getActualImAdjustment(formLinkBean); 
    			executeStorage(imAdjustmentHead);
    		}

    		// 檢核與 存取db head and line
    		resultMap = imGeneralAdjustmentService.updateImGeneralAdjustment(parameterMap);

    		imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");	
    		msgBox.setMessage((String) resultMap.get("resultMsg") );
    		executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);
    		log.info("msgBox.GetMessage = " + msgBox.getMessage());
    		imGeneralAdjustmentService.update(imAdjustmentHead);

    		// 執行invoice流程
    		imGeneralAdjustmentService.startInvoiceProcess(imAdjustmentHead, otherBean,parameterMap);

    	} catch (StorageException sex) {
    		msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
    		msgBox.setType(MessageBox.CONFIRM);
    		Command cmd_bf = new Command();
    		cmd_bf.setCmd(Command.FUNCTION);
    		cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
    		msgBox.setOk(cmd_bf);
    		//msgBox.setCancel(cmd_cancel);
    	} catch( ValidationErrorException ve ){
    		log.error("執行調整單檢核失敗，原因：" + ve.toString());
    		if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    			listErrorMsg(msgBox);
    		}else{
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    		}	
    	} catch (Exception ex) {
    		log.error("執行調整單存檔失敗，原因：" + ex.toString());
    		msgBox.setMessage(ex.getMessage());
    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 拆/併貨調整單送出 
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionApart(Map parameterMap){

    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	ImAdjustmentHead imAdjustmentHead = null;
    	try {

    		log.info("Reset - performTransactionApart");
    		Object formBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");

    		String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

    		// 驗證必要欄位
    		String status = (String)PropertyUtils.getProperty(formBean, "status");

    		if(!StringUtils.hasText(status)){
    			throw new ValidationErrorException("status參數為空值，無法執行存檔！");
    		}
    		// 前端資料塞入bean
    		resultMap = imGeneralAdjustmentService.updateImAdjustmentBean(parameterMap); 

    		log.info("Reset - updateImAdjustmentBean");
    		// 檢核與 存取db head and line
    		resultMap = imGeneralAdjustmentService.updateImApartAdjustment(parameterMap, formAction);
    		
    		imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");	
    		msgBox.setMessage((String) resultMap.get("resultMsg") );
    		wrappedMessageBox(msgBox, imAdjustmentHead, true, false);
    		executeApartProcess(otherBean, resultMap, imAdjustmentHead,msgBox);
    		//executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);
    	} catch (StorageException sex) {
    		msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
    		msgBox.setType(MessageBox.CONFIRM);
    		Command cmd_bf = new Command();
    		cmd_bf.setCmd(Command.FUNCTION);
    		cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
    		msgBox.setOk(cmd_bf);
    		//msgBox.setCancel(cmd_cancel);
    	} catch( ValidationErrorException ve ){
    		log.error("執行調整單檢核失敗，原因：" + ve.toString());
    		if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    			listErrorMsg(msgBox);
    		}else{
    			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    		}	
    	} catch (Exception ex) {
    		log.error("執行調整單存檔失敗，原因：" + ex.toString());
    		msgBox.setMessage(ex.getMessage());
    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    private void wrappedMessageBox(MessageBox msgBox, ImAdjustmentHead imAdjustmentHead, boolean isStartProcess, boolean isExecFunction) {
		String brandCode = imAdjustmentHead.getBrandCode();
		String orderTypeCode = imAdjustmentHead.getOrderTypeCode();
		String orderNo = imAdjustmentHead.getOrderNo();
		String status = imAdjustmentHead.getStatus();
		String identification = orderTypeCode + "-" + orderNo;

		
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", "","","",""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
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
     * 調整單背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	ImAdjustmentHead imAdjustmentHead = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    resultMap = imGeneralAdjustmentService.updateImGeneralAdjustment(parameterMap); 
	    imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (imAdjustmentHead != null){
		executeProcess(otherBean, resultMap, imAdjustmentHead, null);
	    }
	    imGeneralAdjustmentService.update(imAdjustmentHead);
	    // 執行invoice流程
	    imGeneralAdjustmentService.startInvoiceProcess(imAdjustmentHead, otherBean, resultMap);
	} catch (Exception ex) {
	    message = "執行調整單背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 拆/併貨調整單背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGroundApart(Map parameterMap) {

	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap(0);
	String message = null;
	ImAdjustmentHead imAdjustmentHead = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

	    resultMap = imGeneralAdjustmentService.updateImApartAdjustment(parameterMap, formAction); 
	    imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (imAdjustmentHead != null)
		executeProcess(otherBean, resultMap, imAdjustmentHead, null);
	} catch (Exception ex) {
	    message = "執行拆/併調整單背景存檔失敗，原因：" + ex.toString();
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
    private void executeProcess(Object otherBean, Map resultMap, ImAdjustmentHead imAdjustmentHead, MessageBox msgBox)
    throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(
		imAdjustmentHead.getBrandCode(), imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
	try {
	    String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
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
		    wrappedMessageBox(msgBox, imAdjustmentHead, true, false, otherBean);
		}
		Object processObj[] = ImGeneralAdjustmentService.startProcess(imAdjustmentHead,resultMap);
		imAdjustmentHead.setProcessId((Long)processObj[0]);
	    } else {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, imAdjustmentHead, false, false, otherBean);
		}
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = ImGeneralAdjustmentService
		    .completeAssignment(assignId, result,formAction);
		    if (!OrderStatus.SAVE.equals(formStatus) ) {
			resultMap.put("processId", processInfo[0]);
			resultMap.put("activityId", processInfo[1]);
			resultMap.put("activityName", processInfo[2]);
			resultMap.put("result", approvalResultMsg);
			resultMap.put("approvalComment", approvalComment);
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
	    message = "執行調整單流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(ImGeneralAdjustmentService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,imAdjustmentHead.getLastUpdatedBy());
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
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, ImAdjustmentHead head, boolean isStartProcess, boolean isExecFunction, Object otherBean )throws Exception{

	String orderTypeCode = head.getOrderTypeCode();
	String orderNo = head.getOrderNo();
	String status = head.getStatus();
	String identification = orderTypeCode + "-" + orderNo;

	String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
	
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
		if(!StringUtils.hasText(userType)){
		    msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");   
		}else{
		    if(ImGeneralAdjustmentService.CREATOR.equalsIgnoreCase(userType)){
			if("ARCHIVE".equalsIgnoreCase(formAction)){
			    msgBox.setMessage(identification + "表單已存檔！");   
			}else{
			    msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！"); 
			}
		    }else{
			msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！"); 
		    }
		}
		
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
	private void executeApartProcess(Object otherBean, Map resultMap, ImAdjustmentHead imAdjustmentHead, MessageBox msgBox)
	throws Exception {
    
    String message = null;
    String identification = 
        	MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo() );
    	//MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getInvoiceNo() );
try{
    String processId          = (String)PropertyUtils.getProperty(otherBean, "processId");
    String assignmentId       = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
    //String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
   // String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    String approvalResult     = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
    String approvalComment    = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
    //String approvalResultMsg  = getApprovalResult(processId, beforeChangeStatus, formStatus);
        
    if(!StringUtils.hasText(processId) || "0".equals(processId)){
        	if(msgBox != null)
        	    wrappedMessageBox(msgBox, imAdjustmentHead, true, false);
        	
        	ImGeneralAdjustmentService.startApartProcess(imAdjustmentHead);
    }else{
	if(msgBox != null)
	    wrappedMessageBox(msgBox, imAdjustmentHead, false, false);
        	    
	if(StringUtils.hasText(assignmentId)){
	    Long assignId = NumberUtils.getLong(assignmentId);
	    Boolean result = Boolean.valueOf(approvalResult);

	    Object[] processInfo = ImGeneralAdjustmentService.completeApartAssignment(assignId, result, imAdjustmentHead);
	    resultMap.put("processId",       processInfo[0]);
	    resultMap.put("activityId",      processInfo[1]);
	    resultMap.put("activityName",    processInfo[2]);
	   // resultMap.put("result",          approvalResultMsg);
	    resultMap.put("approvalComment", approvalComment);
	    WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
	}else{
	    throw new ProcessFailedException("Complete assignment失敗，ProcessId="
                	        + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
	}
    }
}catch(Exception ex){
    message = "執行 重整單 流程發生錯誤，原因：" + ex.toString();
    siProgramLogAction.createProgramLog(FiInvoiceHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imAdjustmentHead.getLastUpdatedBy());
    log.error(message);
}
}
/*    private String getApprovalResult(String processId, String beforeChangeStatus, String formStatus){
    	
    	String approvalResult = "送出";
    	if(StringUtils.hasText(processId)){	
    	    if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)
    		    || OrderStatus.REJECT.equals(formStatus)){
    		approvalResult = OrderStatus.getChineseWord(formStatus);
    	    }else if(!OrderStatus.REJECT.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
    		approvalResult = "核准";
    	    }
    	}
    	return approvalResult;
        }*/
}