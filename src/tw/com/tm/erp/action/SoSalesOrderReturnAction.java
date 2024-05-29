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
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImDeliveryMainService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class SoSalesOrderReturnAction {
    
    private static final Log log = LogFactory.getLog(SoSalesOrderReturnAction.class);
    
    private ImDeliveryMainService imDeliveryMainService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public void setImDeliveryMainService(ImDeliveryMainService imDeliveryMainService) {
	this.imDeliveryMainService = imDeliveryMainService;
    }
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }
    
    public List<Properties> performTransaction(Map parameterMap){
	
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    //取得欲更新的bean
    	    //======check beforeChangeStatus、formStatus========
	    if(!StringUtils.hasText(beforeChangeStatus)){
	        throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
	    }else if(!StringUtils.hasText(formStatus)){
	        throw new Exception("formStatus參數為空值，無法執行存檔！");
	    }
    	    //=====================================================
	    Map resultMap = null;
	    if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
	    	//更新主檔
	    	imDeliveryMainService.updateDelivery(parameterMap);
	    	List errorMsgs  = imDeliveryMainService.updateCheckedSalesOrderReturnData(parameterMap);
			if(errorMsgs.size() > 0){
			    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
			}
	    }
	    resultMap = imDeliveryMainService.updateAJAXSalesOrderReturn(parameterMap);	    
	    ImDeliveryHead deliveryHeadBean = (ImDeliveryHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    executeProcess(otherBean, resultMap, deliveryHeadBean, msgBox);	   
	}catch (ValidationErrorException ex) {
	    log.error("執行銷退單存檔時失敗");
	    msgBox.setMessage(ex.getMessage());
	    listErrorMessageBox(msgBox);
	}catch (Exception ex) {
	    log.error("執行銷退單存檔失敗，原因：" + ex.toString());
        msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, ImDeliveryHead deliveryHead, boolean isStartProcess, boolean isExecFunction){
	
	String orderTypeCode = deliveryHead.getOrderTypeCode();
	String orderNo = deliveryHead.getOrderNo();
	String status = deliveryHead.getStatus();
	String identification = orderTypeCode + "-" + orderNo;
    Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"refresh()", ""});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}else{
	    if(OrderStatus.SIGNING.equals(status) || isExecFunction){
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
    
    private void executeProcess(Object otherBean, Map resultMap, ImDeliveryHead deliveryHeadBean, MessageBox msgBox) 
            throws Exception {

        String message = null;
        String identification = MessageStatus.getIdentification(deliveryHeadBean.getBrandCode(), 
        	deliveryHeadBean.getOrderTypeCode(), deliveryHeadBean.getOrderNo());
        try{
            String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
            String assignmentId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
            String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
            String approvalComment = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
            String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
    	    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");	    
    	    String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, formStatus);
	    
            if(!StringUtils.hasText(processId) || "0".equals(processId)){
        	if(msgBox != null)
    	        wrappedMessageBox(msgBox, deliveryHeadBean, true, false);
        		ImDeliveryMainService.startProcessForReturn(deliveryHeadBean);
    	    }else{
    		if(msgBox != null)
    		    wrappedMessageBox(msgBox, deliveryHeadBean, false, false);
    		if(!OrderStatus.SAVE.equals(formStatus) || (OrderStatus.REJECT.equals(beforeChangeStatus)
	    	        && OrderStatus.SAVE.equals(formStatus))){
    		    if(StringUtils.hasText(assignmentId)){
    	                Long assignId = NumberUtils.getLong(assignmentId);
		        Boolean result = Boolean.valueOf(approvalResult);
		        Object[] processInfo = ImDeliveryMainService.completeAssignmentForReturn(assignId, result);
		        resultMap.put("processId", processInfo[0]);
		        resultMap.put("activityId", processInfo[1]);
		        resultMap.put("activityName", processInfo[2]);
		        resultMap.put("result", approvalResultMsg);
		        resultMap.put("approvalComment", approvalComment);
		        WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
    		    }else{
    		        throw new ProcessFailedException("Complete assignment失敗，ProcessId="
	    	                + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
    		    }
    		}
    	    }           
        }catch(Exception ex){
	    message = "執行銷退流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(ImDeliveryMainService.PROGRAM_ID_RETURN, MessageStatus.LOG_ERROR, 
		    identification, message, deliveryHeadBean.getLastUpdatedBy());
	    log.error(message);
	}       
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
	    Map resultMap = imDeliveryMainService.updateDeliveryWithActualOrderNO(parameterMap);
	    ImDeliveryHead deliveryHeadBean = (ImDeliveryHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
	    	wrappedMessageBox(msgBox, deliveryHeadBean, true, true);
	    }else{
	    	wrappedMessageBox(msgBox, deliveryHeadBean, false, true);
	    }
	}catch (Exception ex) {
	    System.out.println("執行銷退單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行銷退單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performTransactionForBackGround(Map parameterMap){
	
	Map returnMap = new HashMap(0);	
	ImDeliveryHead deliveryHeadBean = null;
	Map resultMap = new HashMap(0);;
	String message = null;
	String identification = null;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = imDeliveryMainService.getDeliveryHeadId(formLinkBean);
            deliveryHeadBean = imDeliveryMainService.findImDeliveryHeadById(headId);
            resultMap.put("entityBean", deliveryHeadBean);
	    try{      	     	       
		List errorMsgs  = imDeliveryMainService.updateCheckedSalesOrderReturnData(parameterMap);
		if(errorMsgs.size() == 0){
		    resultMap = imDeliveryMainService.updateAJAXSalesOrderReturn(parameterMap);
		    deliveryHeadBean = (ImDeliveryHead)resultMap.get("entityBean");
		}
	    }catch(Exception ex){
		if(deliveryHeadBean != null){
		    identification = MessageStatus.getIdentification(deliveryHeadBean.getBrandCode(), 
			    deliveryHeadBean.getOrderTypeCode(), deliveryHeadBean.getOrderNo());
		    message = ex.getMessage();
		    siProgramLogAction.createProgramLog(ImDeliveryMainService.PROGRAM_ID_RETURN, MessageStatus.LOG_ERROR, 
			    identification, message, deliveryHeadBean.getLastUpdatedBy());
		}
	    }
	  //==============execute flow==================
	    if(deliveryHeadBean != null)
	        executeProcess(otherBean, resultMap, deliveryHeadBean, null);	       
	}catch (Exception ex) {
	    message = "執行銷退單背景存檔失敗，原因：" + ex.toString();
	    System.out.println(message);
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
}
