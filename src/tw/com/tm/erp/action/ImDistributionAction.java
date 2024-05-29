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
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImDistributionHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImDistributionAction {
    
    private static final Log log = LogFactory.getLog(ImDistributionAction.class);
    
    private ImDistributionHeadService imDistributionHeadService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public void setImDistributionHeadService(ImDistributionHeadService imDistributionHeadService) {
		this.imDistributionHeadService = imDistributionHeadService;
	}
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }
    
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imDistributionHeadService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行配貨單存檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
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
			Object vatBeanFormLink = parameterMap.get("vatBeanFormLink");
		    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    	//Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
	    	//Long headId = NumberUtils.getLong((String)PropertyUtils.getProperty(vatBeanFormLink, "headId"));
		    if(!StringUtils.hasText(beforeChangeStatus)){
		        throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
		    }else if(!StringUtils.hasText(formStatus)){
		        throw new Exception("formStatus參數為空值，無法執行存檔！");
		    }
		    /*
		    log.info("beforeChangeStatus = " + beforeChangeStatus);
		    log.info("formStatus = " + formStatus);
	    	log.info("formId = " + formId);
	    	log.info("headId = " + headId);
		    */
		    Map resultMap = imDistributionHeadService.updateParameter(parameterMap);
		    ImDistributionHead imDistributionHead = (ImDistributionHead)resultMap.get("entityBean");
		    String resultMsg = imDistributionHeadService.updateGetOrderNo(imDistributionHead);
		    //log.info("imDistributionHead.id = " + imDistributionHead.getHeadId());
		    //log.info("imDistributionHead.no = " + imDistributionHead.getOrderNo());
		    msgBox.setMessage(resultMsg + "是否繼續新增？");
		    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
		    if(!StringUtils.hasText(processId) || "0".equals(processId)){
		    	wrappedMessageBox(msgBox, imDistributionHead, true, true);
		    }else{
		    	wrappedMessageBox(msgBox, imDistributionHead, false, true);
		    }
		}catch (Exception ex) {
		    log.error("執行配貨單存檔失敗，原因：" + ex.toString());
	        msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performTransactionForBackGround(Map parameterMap){
		Map returnMap = new HashMap(0);
		Map resultMap = new HashMap(0);;
		String message = null;
		Object otherBean = parameterMap.get("vatBeanOther");
		try{
		    	Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
				ImDistributionHead head = imDistributionHeadService.findById(formId);
				log.info("head.id = " + head.getHeadId());
			    log.info("head.no = " + head.getOrderNo());
		    	try{
		    		List errorMsgs  = imDistributionHeadService.updateCheck(parameterMap, resultMap);
		    		head = (ImDistributionHead)resultMap.get("entityBean");
		    	}catch(Exception ex){
				    log.error(ex.getMessage());
		    	}
		    	
		    if(head != null)
		        executeProcess(otherBean, resultMap, head, null);	       
		}catch (Exception ex) {
		    message = "執行配貨單背景存檔失敗，原因：" + ex.toString();
		    log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    
    public List<Properties> performTransaction(Map parameterMap){
	    Map resultMap = null;
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
		    Object otherBean = parameterMap.get("vatBeanOther");
		    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
		    if(!StringUtils.hasText(beforeChangeStatus)){
		        throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
		    }else if(!StringUtils.hasText(formStatus)){
		        throw new Exception("formStatus參數為空值，無法執行存檔！");
		    }
		    log.info("beforeChangeStatus = " + beforeChangeStatus);
		    log.info("formStatus = " + formStatus);
		    resultMap = imDistributionHeadService.updateParameter(parameterMap);
		    if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
		    	//log.info("go check SIGNING");
		    	List errorMsgs  = imDistributionHeadService.updateCheck(parameterMap, resultMap);
		    	//log.info("after check SIGNING");
		    }
		    ImDistributionHead imDistributionHead = (ImDistributionHead)resultMap.get("entityBean");
		    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
		    executeProcess(otherBean, resultMap, imDistributionHead, msgBox);	   
		}catch (FormException ex) {
		    log.error("執行配貨單存檔時失敗");
		    msgBox.setMessage(ex.getMessage());
		    listErrorMessageBox(msgBox);
		}catch (Exception ex) {
		    log.error("執行配貨單存檔失敗，原因：" + ex.toString());
	        msgBox.setMessage("執行配貨單存檔失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, ImDistributionHead head, boolean isStartProcess, boolean isExecFunction){
		String orderTypeCode = head.getOrderTypeCode();
		String orderNo = head.getOrderNo();
		String status = head.getStatus();
		String identification = orderTypeCode + "-" + orderNo;
	    Command cmd_ok = new Command();
		if(isStartProcess){
		    msgBox.setType(MessageBox.CONFIRM);
		    cmd_ok.setCmd(Command.FUNCTION);
		    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
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
    
    
    private void executeProcess(Object otherBean, Map resultMap, ImDistributionHead head, MessageBox msgBox) 
            throws Exception {

        String message = null;
        String identification = MessageStatus.getIdentification(head.getBrandCode(), 
        	head.getOrderTypeCode(), head.getOrderNo());
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
    	        wrappedMessageBox(msgBox, head, true, false);
        		ImDistributionHeadService.startProcess(head);
    	    }else{
	    		if(msgBox != null)
	    		    wrappedMessageBox(msgBox, head, false, false);
    		    if(StringUtils.hasText(assignmentId)){
	    	        Long assignId = NumberUtils.getLong(assignmentId);
			        Boolean result = Boolean.valueOf(approvalResult);
			        Object[] processInfo = ImDistributionHeadService.completeAssignment(assignId, result);
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
        }catch(Exception ex){
	    message = "執行配貨單流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(imDistributionHeadService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
		    identification, message, head.getLastUpdatedBy());
	    log.error(message);
	}       
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
