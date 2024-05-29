package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImInventoryCountsService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImInventoryCountsAction {
    
    private static final Log log = LogFactory.getLog(ImInventoryCountsAction.class);
    
    private ImInventoryCountsService imInventoryCountsService;
    
    public SiProgramLogAction siProgramLogAction;
    
    private WfApprovalResultService wfApprovalResultService;
    
	public void setImInventoryCountsService(ImInventoryCountsService imInventoryCountsService) {
		this.imInventoryCountsService = imInventoryCountsService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imInventoryCountsService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行盤點單初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
	        msgBox.setMessage(ex.getMessage());
	        returnMap = new HashMap();
	        returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
		    Object otherBean = parameterMap.get("vatBeanOther");
		    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    	//======check beforeChangeStatus、formStatus========
	    	if(!StringUtils.hasText(beforeChangeStatus)){
		        throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
		    }else if(!StringUtils.hasText(formStatus)){
		        throw new Exception("formStatus參數為空值，無法執行存檔！");
		    }
	    	    //=====================================================
		    Map resultMap = null;
		    if((OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.COUNTING.equals(formStatus))
			    || (OrderStatus.COUNTING.equals(beforeChangeStatus) && OrderStatus.COUNT_FINISH.equals(formStatus))){
		    	imInventoryCountsService.updateInventoryCounts(parameterMap);
		    	List errorMsgs  = imInventoryCountsService.updateCheckedInventoryCountsData(parameterMap);
				if(errorMsgs.size() > 0){
				    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
		    }
		    resultMap = imInventoryCountsService.updateAJAXInventoryCounts(parameterMap);
		    ImInventoryCountsHead inventoryCountsHeadBean = (ImInventoryCountsHead)resultMap.get("entityBean");
		    executeProcess(otherBean, resultMap, inventoryCountsHeadBean, msgBox);
		}catch (ValidationErrorException ex) {
		    log.error("執行盤點單存檔時失敗");
		    msgBox.setMessage(ex.getMessage());
		    listErrorMessageBox(msgBox);
		}catch (Exception ex) {
		    log.error("執行盤點單存檔失敗，原因：" + ex.toString());
	        msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
   	private void wrappedMessageBox(MessageBox msgBox, ImInventoryCountsHead inventoryCountsHead, boolean isStartProcess, boolean isExecFunction){
		String orderTypeCode = inventoryCountsHead.getOrderTypeCode();
		String orderNo = inventoryCountsHead.getOrderNo();
		String status = inventoryCountsHead.getStatus();
		String identification = orderTypeCode + "-" + orderNo;
	    Command cmd_ok = new Command();
		if(isStartProcess){
			log.info("開始新流程");
			if(OrderStatus.COUNTING.equals(status) || OrderStatus.COUNT_FINISH.equals(status)){
	            msgBox.setMessage(identification + "表單已成功送出，是否繼續新增？");
			}else{
			    msgBox.setMessage(identification + "表單已成功" + OrderStatus.getChineseWord(status) + "，是否繼續新增？");
			}
		        msgBox.setType(MessageBox.CONFIRM);
		        cmd_ok.setCmd(Command.FUNCTION);
		    	cmd_ok.setParameters(new String[]{"resetForm()", ""});
		    	Command cmd_cancel = new Command();
		    	cmd_cancel.setCmd(Command.WIN_CLOSE);
		    	msgBox.setCancel(cmd_cancel);	
		}else{
			log.info("接續舊流程");
			msgBox.setMessage(identification + "表單已成功送出");
			cmd_ok.setCmd(Command.WIN_CLOSE);
		}
		msgBox.setOk(cmd_ok);
		if(isExecFunction){
			log.info("背景送出");
		    Command cmd_bf = new Command();
		    cmd_bf.setCmd(Command.FUNCTION);
		    cmd_bf.setParameters(new String[]{"execSubmitBgAction()", ""});
		    msgBox.setBefore(cmd_bf);
		}
    }
   	
      
    public List<Properties> performSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imInventoryCountsService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
		    System.out.println("執行盤點單查詢初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
	            msgBox.setMessage(ex.getMessage());
	            returnMap = new HashMap();
	            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
       
    public List<Properties> performSearchSelection(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imInventoryCountsService.getSearchSelection(parameterMap);	    
		}catch (Exception ex) {
		    System.out.println("執行盤點單檢視時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
	        msgBox.setMessage(ex.getMessage());
	        returnMap = new HashMap();
	        returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    

    public List<Properties> performTransactionBatch(Map parameterMap){
    	log.info("performTransactionBatch(Map parameterMap) begin!!");
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    	    String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
    	    //check formStatus
    	    if(!StringUtils.hasText(formStatus)){
    	        throw new Exception("formStatus參數為空值，無法執行存檔！");
    	    }
        	if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.COUNTING.equals(formStatus) ) {
        		//盤點單: 批次暫存 或 批次送出
        		
        		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        		List <ImInventoryCountsHead> imInventoryCountsHeads = imInventoryCountsService.executeBatchCreateImInventoryCounts3((HashMap)parameterMap, loginEmployeeCode);
        		StringBuffer messageStr = new StringBuffer(orderTypeCode + "-");
            	for (int i=0 ; i<imInventoryCountsHeads.size() ; i++) {
        			ImInventoryCountsHead imInventoryCountsHead = (ImInventoryCountsHead)imInventoryCountsHeads.get(i);
        	    	if(i == 0){
        	    		messageStr.append(imInventoryCountsHead.getOrderNo());
        	    	}else if(i == (imInventoryCountsHeads.size() - 1)){
        	    		messageStr.append("~" + imInventoryCountsHead.getOrderNo());
        	    	}
        			ImInventoryCountsService.startProcess(imInventoryCountsHead);
        		}
        		ImInventoryCountsHead inventoryCountsHead = new ImInventoryCountsHead();
        		inventoryCountsHead.setStatus(formStatus);
        		inventoryCountsHead.setOrderTypeCode(orderTypeCode);
        		inventoryCountsHead.setOrderNo(messageStr.toString());
        		wrappedMessageBox(msgBox, inventoryCountsHead, true, false);
        	}
        	
    	}catch(Exception ex) {
    	    log.error("執行批次盤點單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
    	}
    	returnMap.put("vatMessage" ,msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);    	
	}
    
    /**
     * 取得單號
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> getOrderNo(Map parameterMap){
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try{
		Object otherBean = parameterMap.get("vatBeanOther");
		String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    	//======check beforeChangeStatus、formStatus========
    	if(!StringUtils.hasText(beforeChangeStatus)){
	        throw new ValidationErrorException("beforeChangeStatus參數為空值，無法執行存檔！");
	    }else if(!StringUtils.hasText(formStatus)){
	        throw new ValidationErrorException("formStatus參數為空值，無法執行存檔！");
	    }
    	//取單號並且更新主檔
	    Map resultMap = imInventoryCountsService.updateInventoryCountsWithActualOrderNO(parameterMap);
	    ImInventoryCountsHead imInventoryCountsHead = (ImInventoryCountsHead)resultMap.get("entityBean");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
            wrappedMessageBox(msgBox, imInventoryCountsHead, true, true);
	    }else{
	    	wrappedMessageBox(msgBox, imInventoryCountsHead, false, true);
	    }
	}catch (Exception ex) {
	    log.error("執行盤點單存檔失敗，原因：" + ex.toString());
        msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performTransactionForBackGround(Map parameterMap){
    	Map returnMap = new HashMap(0);
    	String message = null;
    	String identification = null;
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
        	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Long headId = imInventoryCountsService.getInventoryCountsHeadId(formLinkBean);
    	    //String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    //取得欲更新的bean
    	    ImInventoryCountsHead imInventoryCountsHead = imInventoryCountsService.getActualInventoryCounts(headId);
    	    Map resultMap = new HashMap();  	
        	//=====================================================
	        try{
	    	    if((OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.COUNTING.equals(formStatus))
	    		    || (OrderStatus.COUNTING.equals(beforeChangeStatus) && OrderStatus.COUNT_FINISH.equals(formStatus))){
	    	    	imInventoryCountsService.updateInventoryCounts(parameterMap);
	    	    	List errorMsgs  = imInventoryCountsService.updateCheckedInventoryCountsData(parameterMap);
	    	    	if(errorMsgs.size() == 0){
	    	    		resultMap = imInventoryCountsService.updateAJAXInventoryCounts(parameterMap);	    
	    	    	    imInventoryCountsHead = (ImInventoryCountsHead)resultMap.get("entityBean");
	    	    	}
	    	    }
	        }catch(Exception ex){
    			if(imInventoryCountsHead != null){
    			    identification = MessageStatus.getIdentification(imInventoryCountsHead.getBrandCode(),imInventoryCountsHead.getOrderTypeCode(), imInventoryCountsHead.getOrderNo());
    			    message = ex.getMessage();
    			    siProgramLogAction.createProgramLog(ImInventoryCountsService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
    				    identification, message, imInventoryCountsHead.getLastUpdatedBy());
    			}
	        }
	        imInventoryCountsHead = imInventoryCountsService.getActualInventoryCounts(headId);
    	    //預留執行process的地方
    	    if(imInventoryCountsHead != null){
    	    	resultMap.put("entityBean", imInventoryCountsHead);
    	        executeProcess(otherBean, resultMap, imInventoryCountsHead, null);
    	    }
    	}catch (Exception ex) {
    	    message = "執行盤點單背景存檔失敗，原因：" + ex.toString();
    	    log.error(message);
    	}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
	 * 送出錯誤後，呼叫的function
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
    private void listErrorMessageBox(MessageBox msgBox){
    	//如果為背景送出
			Command cmd_error = new Command();
			cmd_error.setCmd(Command.FUNCTION);
			cmd_error.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_error);
    }
    
    private void executeProcess(Object otherBean, Map resultMap, ImInventoryCountsHead imInventoryCountsHead, MessageBox msgBox) throws Exception {
		String message = null;
		String identification = MessageStatus.getIdentification(imInventoryCountsHead.getBrandCode(), 
			imInventoryCountsHead.getOrderTypeCode(), imInventoryCountsHead.getOrderNo());
		try{
		    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
		    String assignmentId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
		    String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
		    String approvalComment = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
		    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
		    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		    log.info("formStatus =" + formStatus);
		    String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, formStatus);
		    if(!StringUtils.hasText(processId) || "0".equals(processId)){
				if(msgBox != null)
			    wrappedMessageBox(msgBox, imInventoryCountsHead, true, false);
		        ImInventoryCountsService.startProcess(imInventoryCountsHead);
		    }else{
				if(msgBox != null)
				    wrappedMessageBox(msgBox, imInventoryCountsHead, false, false);
				if(!OrderStatus.SAVE.equals(formStatus) || (OrderStatus.REJECT.equals(beforeChangeStatus) && OrderStatus.SAVE.equals(formStatus))){
				    if(StringUtils.hasText(assignmentId)){
				        Long assignId = NumberUtils.getLong(assignmentId);
				        Boolean result = Boolean.valueOf(approvalResult);
				        Object[] processInfo = ImInventoryCountsService.completeAssignment(assignId, result);
				        resultMap.put("processId", processInfo[0]);
				        resultMap.put("activityId", processInfo[1]);
				        resultMap.put("activityName", processInfo[2]);
				        resultMap.put("result", approvalResultMsg);
				        resultMap.put("approvalComment", approvalComment);
				        WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				    }else{
				        throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
				    }
				}
		    }           
		}catch(Exception ex){
		message = "執行銷貨流程發生錯誤，原因：" + ex.toString();
		siProgramLogAction.createProgramLog(ImInventoryCountsService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
		    identification, message, imInventoryCountsHead.getLastUpdatedBy());
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
    
    public List<Properties> performListInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imInventoryCountsService.executeListInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行盤點清單初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
	        msgBox.setMessage("執行盤點清單初始化時發生錯誤，原因：" + ex.getMessage());
	        returnMap = new HashMap();
	        returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    public List<Properties> performListTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
	    	imInventoryCountsService.updateInventoryCountLists(parameterMap);
	    	msgBox.setMessage("盤點單清單建立成功");
			Command cmd_reset = new Command();
			cmd_reset.setCmd(Command.FUNCTION);
			cmd_reset.setParameters(new String[] { "changeCountsId()", "" });
			msgBox.setOk(cmd_reset);
		}catch (FormException ex) {
		    log.error("執行盤點單清單建立失敗");
		    msgBox.setMessage(ex.getMessage());
		    listErrorMessageBox(msgBox);
		}catch (Exception ex) {
		    log.error("執行盤點單清單建立失敗，原因：" + ex.toString());
	        msgBox.setMessage("執行盤點單清單建立失敗，原因：" +ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
}
