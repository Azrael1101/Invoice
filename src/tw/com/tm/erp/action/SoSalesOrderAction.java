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
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class SoSalesOrderAction {
    
    private static final Log log = LogFactory.getLog(SoSalesOrderAction.class);
    
    private SoSalesOrderService soSalesOrderService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    public void setSoSalesOrderService(SoSalesOrderService soSalesOrderService) {
	this.soSalesOrderService = soSalesOrderService;
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
    	    //======check beforeChangeStatus、formStatus========
    	    if(!StringUtils.hasText(beforeChangeStatus)){
	        throw new ValidationErrorException("beforeChangeStatus參數為空值，無法執行存檔！");
	    }else if(!StringUtils.hasText(formStatus)){
	        throw new ValidationErrorException("formStatus參數為空值，無法執行存檔！");
	    }
    	    //=====================================================
	    Map resultMap = null;
	    if(OrderStatus.UNCONFIRMED.equals(formStatus)){
		resultMap = soSalesOrderService.executeAJAXAntiConfirm(parameterMap);
	    }else if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
		soSalesOrderService.updateSalesOrder(parameterMap);
		List errorMsgs  = soSalesOrderService.updateCheckedSalesOrderData(parameterMap);
		if(errorMsgs.size() > 0){
		    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		}	
		resultMap = soSalesOrderService.updateAJAXSOToDelivery(parameterMap);
	    }else{
		resultMap = soSalesOrderService.updateAJAXSalesOrder(parameterMap);
	    }
	    SoSalesOrderHead salesOrderHeadBean = (SoSalesOrderHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    if("SOP".equals(salesOrderHeadBean.getOrderTypeCode())){
		wrappedMessageBox(msgBox, salesOrderHeadBean, true, false);
	    }else{
		executeProcess(otherBean, resultMap, salesOrderHeadBean, msgBox);
	    }
	}catch (Exception ex) {
	    System.out.println("執行銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行銷貨單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, SoSalesOrderHead salesOrderHead, boolean isStartProcess, boolean isExecFunction){
	
	String orderTypeCode = salesOrderHead.getOrderTypeCode();
	String orderNo = salesOrderHead.getOrderNo();
	String status = salesOrderHead.getStatus();
	String identification = orderTypeCode + "-" + orderNo;
        Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.HANDLER);
	    cmd_ok.setParameters(new String[]{"main", "resetHandler"});
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
    
    public List<Properties> performInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    returnMap = soSalesOrderService.executeInitial(parameterMap);
	    
	}catch (Exception ex) {
	    System.out.println("執行銷貨單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    private void executeProcess(Object otherBean, Map resultMap, SoSalesOrderHead salesOrderHeadBean, MessageBox msgBox) 
            throws Exception {

        String message = null;
        String identification = MessageStatus.getIdentification(salesOrderHeadBean.getBrandCode(), 
        	salesOrderHeadBean.getOrderTypeCode(), salesOrderHeadBean.getOrderNo());
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
    	            wrappedMessageBox(msgBox, salesOrderHeadBean, true, false);
    	        SoSalesOrderService.startProcess(salesOrderHeadBean);
    	    }else{
    		if(msgBox != null)
    		    wrappedMessageBox(msgBox, salesOrderHeadBean, false, false);
    		if(!OrderStatus.SAVE.equals(formStatus) || (OrderStatus.REJECT.equals(beforeChangeStatus)
	    	        && OrderStatus.SAVE.equals(formStatus))){
    		    if(StringUtils.hasText(assignmentId)){
    	                Long assignId = NumberUtils.getLong(assignmentId);
		        Boolean result = Boolean.valueOf(approvalResult);
		        Object[] processInfo = SoSalesOrderService.completeAssignment(assignId, result);
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
	    message = "執行銷貨流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(SoSalesOrderService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
		    identification, message, salesOrderHeadBean.getLastUpdatedBy());
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
	    Map resultMap = soSalesOrderService.updateSalesOrderWithActualOrderNO(parameterMap);
	    SoSalesOrderHead salesOrderHeadBean = (SoSalesOrderHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
                wrappedMessageBox(msgBox, salesOrderHeadBean, true, true);
	    }else{
		wrappedMessageBox(msgBox, salesOrderHeadBean, false, true);
	    }
	}catch (Exception ex) {
	    System.out.println("執行促銷單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行促銷單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performTransactionForBackGround(Map parameterMap){
	
	Map returnMap = new HashMap(0);	
	SoSalesOrderHead salesOrderHeadBean = null;
	Map resultMap = new HashMap(0);
	String message = null;
	String identification = null;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = soSalesOrderService.getSalesOrderHeadId(formLinkBean);
            salesOrderHeadBean = soSalesOrderService.findSoSalesOrderHeadById(headId);
            resultMap.put("entityBean", salesOrderHeadBean);
	    try{      	     	       
		List errorMsgs  = soSalesOrderService.updateCheckedSalesOrderData(parameterMap);
		if(errorMsgs.size() == 0){
		    resultMap = soSalesOrderService.updateAJAXSOToDelivery(parameterMap);
		    salesOrderHeadBean = (SoSalesOrderHead)resultMap.get("entityBean");
		}
	    }catch(Exception ex){
		if(salesOrderHeadBean != null){
		    identification = MessageStatus.getIdentification(salesOrderHeadBean.getBrandCode(), 
			    salesOrderHeadBean.getOrderTypeCode(), salesOrderHeadBean.getOrderNo());
		    message = ex.getMessage();
		    siProgramLogAction.createProgramLog(SoSalesOrderService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
			    identification, message, salesOrderHeadBean.getLastUpdatedBy());
		}
	    }
	  //==============execute flow==================
	    if(salesOrderHeadBean != null)
	        executeProcess(otherBean, resultMap, salesOrderHeadBean, null);	       
	}catch (Exception ex) {
	    message = "執行銷貨單背景存檔失敗，原因：" + ex.toString();
	    System.out.println(message);
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
}
