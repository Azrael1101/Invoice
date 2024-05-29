package tw.com.tm.erp.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Date; 

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.service.ImPromotionService;
import tw.com.tm.erp.hbm.service.FiInvoiceHeadMainService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadMainService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;

public class FiInvoiceMainAction {
    
    private static final Log log = LogFactory.getLog(FiInvoiceMainAction.class);
    
    private FiInvoiceHeadMainService fiInvoiceHeadMainService; 
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    private FiInvoiceHeadDAO fiInvoiceHeadDAO;
    
    
    public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
        this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
    }

    public void setFiInvoiceHeadMainService(FiInvoiceHeadMainService fiInvoiceHeadMainService) {
	this.fiInvoiceHeadMainService = fiInvoiceHeadMainService;
    }

    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
        this.siProgramLogAction = siProgramLogAction;
    }
    
    
    public List<Properties> performInitial(Map parameterMap){
	log.info("performInitial");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.executeInitial(parameterMap);
	}catch (Exception ex) {
	    System.out.println("執行 Invoice 初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    
    public List<Properties> performTransaction(Map parameterMap){
	log.info("fiInvoiceHeadMainAction.performTransaction()" );
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try{
	    Object otherBean  = parameterMap.get("vatBeanOther");
	    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    if(!StringUtils.hasText(formStatus))
	    	throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");	
	    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    
	    if(!StringUtils.hasText(beforeChangeStatus))
	    	throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");	
	    
	    fiInvoiceHeadMainService.deleteProgramLog(parameterMap, 0);	// delete SiProgramLog
	    //update Head Data from form to bean, 預備 check 前先存主檔資料
	    fiInvoiceHeadMainService.updateFiInvoiceData(parameterMap);
	    if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
		// check Invoice head & line data
	    	List errorMsgs = fiInvoiceHeadMainService.updateCheckedFiInvoiceData(parameterMap);
    	    	if( errorMsgs.size()>0 || fiInvoiceHeadMainService.deleteProgramLog(parameterMap,1)>0 ){
    	    	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
    	    	}
    	}
	    log.info("fiInvoiceHeadMainService.updateAJAXFiInvoice(");
	    // 確定所有 DATA 正確後存檔
	    Map resultMap = fiInvoiceHeadMainService.updateAJAXFiInvoice(parameterMap);
	    FiInvoiceHead fiInvoiceBean = (FiInvoiceHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    //executeProcess(otherBean, resultMap, fiInvoiceBean, msgBox);
    	if(!OrderStatus.FINISH.equals(fiInvoiceBean.getStatus()))
    		executeProcess(otherBean, resultMap, fiInvoiceBean, msgBox);
    	else
    		wrappedMessageBox(msgBox, fiInvoiceBean, false, false);
	}catch (FormException ex) {
	    log.error("執行 Invoice 存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	    Command cmd_bf = new Command();
	    cmd_bf.setCmd(Command.FUNCTION);
	    cmd_bf.setParameters(new String[]{"showMessage()", ""});
	    msgBox.setOk(cmd_bf);
	
	}catch (Exception ex) {
	    System.out.println("執行  Invoice 單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行  Invoice 存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    

    /**取得單號
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> getOrderNo(Map parameterMap){
	log.info("getOrderNo");
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try{
	    Map resultMap = fiInvoiceHeadMainService.updateAJAXFiInvoice(parameterMap);
	    FiInvoiceHead fiInvoiceBean = (FiInvoiceHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
                wrappedMessageBox(msgBox, fiInvoiceBean, true, true);
	    }else{
		wrappedMessageBox(msgBox, fiInvoiceBean, false, true);
	    }
	}catch (Exception ex) {
	    log.error("執行採購單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    
    public List<Properties> performTransactionForBackGround(Map parameterMap){
	log.info("performTransactionForBackGround");
	Map returnMap = new HashMap(0);	
	FiInvoiceHead fiInvoice = null;
	Map resultMap = new HashMap(0);
	String message = null;
	String identification = null;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean    = parameterMap.get("vatBeanOther");
            Long   headId       = fiInvoiceHeadMainService.getFiInvoiceHeadId(formLinkBean);
            log.info("performTransactionForBackGround headId=" + headId);
           
	    try{      	
		fiInvoice = fiInvoiceHeadMainService.findById(headId);
	        resultMap.put("entityBean", fiInvoice);
		List errorMsgs = fiInvoiceHeadMainService.updateCheckedFiInvoiceData(parameterMap);
		if( errorMsgs.size()>0 || fiInvoiceHeadMainService.deleteProgramLog(parameterMap,1)>0 ){	// check SiProgramLog.size>0
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
		resultMap  = fiInvoiceHeadMainService.updateAJAXFiInvoice(parameterMap);
		if( fiInvoiceHeadMainService.deleteProgramLog(parameterMap, 1) > 0 ){	// check SiProgramLog.size>0
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}
		fiInvoice = (FiInvoiceHead)resultMap.get("entityBean");

	    }catch(Exception ex){
		if(fiInvoice != null){
		    log.info(ex.getMessage());
		    identification = 
			MessageStatus.getIdentification( fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getReserve1() );
		    	//MessageStatus.getIdentification( fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getInvoiceNo() );
		    message = ex.getMessage();
		    siProgramLogAction.createProgramLog(FiInvoiceHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
			    identification, message, fiInvoice.getLastUpdatedBy());
		}
	    }

	    //==============execute flow==================
	    if(fiInvoice != null )
		executeProcess(otherBean, resultMap, fiInvoice, null);
	}catch (Exception ex) {
	    message = "執行採購單背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    

    private void executeProcess(Object otherBean, Map resultMap, FiInvoiceHead fiInvoice, MessageBox msgBox) 
    	throws Exception {
        
        String message = null;
        String identification = 
            	MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getReserve1() );
        	//MessageStatus.getIdentification(fiInvoice.getBrandCode(), fiInvoice.getOrderTypeCode(), fiInvoice.getInvoiceNo() );
	try{
	    String processId          = (String)PropertyUtils.getProperty(otherBean, "processId");
	    String assignmentId       = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
	    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    String approvalResult     = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
	    String approvalComment    = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
	    String approvalResultMsg  = getApprovalResult(processId, beforeChangeStatus, formStatus);
            
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
            	if(msgBox != null)
            	    wrappedMessageBox(msgBox, fiInvoice, true, false);
            	
            	FiInvoiceHeadMainService.startProcess(fiInvoice);
	    }else{
		if(msgBox != null)
		    wrappedMessageBox(msgBox, fiInvoice, false, false);
            	    
		if(StringUtils.hasText(assignmentId)){
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);

		    Object[] processInfo = FiInvoiceHeadMainService.completeAssignment(assignId, result, fiInvoice);
		    resultMap.put("processId",       processInfo[0]);
		    resultMap.put("activityId",      processInfo[1]);
		    resultMap.put("activityName",    processInfo[2]);
		    resultMap.put("result",          approvalResultMsg);
		    resultMap.put("approvalComment", approvalComment);
		    WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
		}else{
		    throw new ProcessFailedException("Complete assignment失敗，ProcessId="
                    	        + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
		}
	    }
	}catch(Exception ex){
	    message = "執行 INVOICDE 流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(FiInvoiceHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, fiInvoice.getLastUpdatedBy());
	    log.error(message);
	}
    }
    
    
    private String getApprovalResult(String processId, String beforeChangeStatus, String formStatus){
	
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
    }
    
    
    private void wrappedMessageBox(MessageBox msgBox, FiInvoiceHead fiInvoiceBean, boolean isStartProcess, boolean isExecFunction){
	
	String orderTypeCode  = fiInvoiceBean.getOrderTypeCode();
	String invoiceNo      = fiInvoiceBean.getInvoiceNo() ;
	String status         = fiInvoiceBean.getStatus();
	String identification = orderTypeCode + " - " + invoiceNo;
    Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    //cmd_ok.setCmd(Command.HANDLER);
	    //cmd_ok.setParameters(new String[]{"main", "resetHandler"});
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"createNewForm()",""});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}else{
	    if(OrderStatus.SIGNING.equals(status) || OrderStatus.FINISH.equals(status)){
                msgBox.setMessage(identification + "表單已送出！");
	    }else{
	        msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
	    }
	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
	}
	
	msgBox.setOk(cmd_ok);
	if(isExecFunction){
	    Command cmd_bf = new Command();
	    cmd_bf.setCmd(Command.FUNCTION);
	    cmd_bf.setParameters(new String[]{"execSubmitBgAction()", ""});
	    msgBox.setBefore(cmd_bf);
	}
    }
    
    
    /**準備資料給 search page 
     * @param parameterMap
     * @return
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	log.info("performSearchInitial");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    System.out.println("執行 Invoice 查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    
    public List<Properties> performSearchSelection(Map parameterMap){
	log.info("performSearchSelection");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.getSearchSelection(parameterMap);	    
	}catch (Exception ex) {
	    log.info("執行 Invoice 檢視時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    
    
    
    /**準備資料給 search page for PoPurchaseOrderHead
     * @param parameterMap
     * @return
     */
    public List<Properties> performSearchPoHInitial(Map parameterMap){
	log.info("performSearchPoHInitial");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.executeSearchPoHInitial(parameterMap);	    
	}catch (Exception ex) {
	    System.out.println("執行 Invoice 查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    
    public List<Properties> performSearchPoHSelection(Map parameterMap){
	log.info("performSearchSelection");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.saveSearchPoHSelection(parameterMap);	    
	}catch (Exception ex) {
	    log.info("執行 Invoice 檢視時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    
    
    /**準備資料給 search page for PoPurchaseOrderLine
     * @param parameterMap
     * @return
     */
    public List<Properties> performSearchPoLInitial(Map parameterMap){
	log.info("performSearchPoLInitial");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.executeSearchPoLInitial(parameterMap);	    
	}catch (Exception ex) {
	    System.out.println("執行採購單明細查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    
    public List<Properties> performSearchPoLSelection(Map parameterMap){
	log.info("performSearchSelection");
	Map returnMap = null;	
	try{
	    returnMap = fiInvoiceHeadMainService.saveSearchPoLSelection(parameterMap);	    
	}catch (Exception ex) {
	    log.info("執行採購單明細檢視時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
}
