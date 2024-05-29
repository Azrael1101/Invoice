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
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImPromotionFullService;
import tw.com.tm.erp.hbm.service.ImPromotionItemService;
import tw.com.tm.erp.hbm.service.ImPromotionMainService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImPromotionFullAction {
    
    private static final Log log = LogFactory.getLog(ImPromotionFullAction.class);
    
    private ImPromotionMainService imPromotionMainService;
    
    private ImPromotionItemService imPromotionItemService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    private ImPromotionFullService imPromotionFullService;
    
    public void setImPromotionMainService(ImPromotionMainService imPromotionMainService) {
	this.imPromotionMainService = imPromotionMainService;
    }
    
    public void setImPromotionItemService(ImPromotionItemService imPromotionItemService) {
	this.imPromotionItemService = imPromotionItemService;
    }
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }
    
    
    /**
	 * @param imPromotionFullService the imPromotionFullService to set
	 */
	public void setImPromotionFullService(
			ImPromotionFullService imPromotionFullService) {
		this.imPromotionFullService = imPromotionFullService;
	}

	public List<Properties> performInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{	
	    validateInitialRequiredParameters(parameterMap, false);
	    returnMap = imPromotionFullService.executeInitial(parameterMap);
	}catch (Exception ex) {
	    System.out.println("執行促銷單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    private void validateInitialRequiredParameters(Map parameterMap, boolean isDirectSearch) throws Exception{
	
	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	//======check loginBrandCode、loginEmployeeCode========
	if(!StringUtils.hasText(loginBrandCode)){
	    throw new Exception("loginBrandCode參數為空值！");
	}else if(!StringUtils.hasText(loginEmployeeCode)){
            throw new Exception("loginEmployeeCode參數為空值！");
	}else if(!StringUtils.hasText(orderTypeCode) && !isDirectSearch){
            throw new Exception("orderTypeCode參數為空值！");
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
    
    public List<Properties> performTransaction(Map parameterMap){
    	log.info("performTransaction");
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	try{
    		Object formLinkBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Long headId = imPromotionFullService.getPromotionHeadId(formLinkBean); 
    		String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
    		if(!StringUtils.hasText(formStatus)){
    			throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");	
    		}
    		String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
    		if(!StringUtils.hasText(beforeChangeStatus)){
    			throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");	
    		}
    		String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
    			imPromotionFullService.saveActualOrderNo(headId, employeeCode);
    		}	    
    		Map resultMap = imPromotionFullService.updateAJAXPromotion(parameterMap);
    		ImPromotion promotionBean = (ImPromotion)resultMap.get("entityBean");
    		msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
    		wrappedMessageBox(msgBox, promotionBean, true, false);
//  		預留流程
    		executeProcess(otherBean, resultMap, promotionBean, msgBox);	    
    	}catch (Exception ex) {
    		ex.printStackTrace();
    		System.out.println("執行促銷單存檔時發生錯誤，原因：" + ex.toString());
    		log.error("執行促銷單存檔失敗，原因：" + ex.toString());
    		msgBox.setMessage(ex.getMessage());
    		listErrorMessageBox(msgBox);
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
	    Map resultMap = imPromotionMainService.updatePromotionWithActualOrderNO(parameterMap);
	    ImPromotion promotionBean = (ImPromotion)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
                wrappedMessageBox(msgBox, promotionBean, true, true);
	    }else{
		wrappedMessageBox(msgBox, promotionBean, false, true);
	    }
	}catch (Exception ex) {
	    System.out.println("執行促銷單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行促銷單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, ImPromotion promotionBean, boolean isStartProcess, boolean isExecFunction){
	
	String orderTypeCode = promotionBean.getOrderTypeCode();
	String orderNo = promotionBean.getOrderNo();
	String status = promotionBean.getStatus();
	String identification = orderTypeCode + "-" + orderNo;
        Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"refreshForm('')", ""});
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
    
    private void executeProcess(Object otherBean, Map resultMap, ImPromotion promotionBean, MessageBox msgBox) 
            throws Exception {

        String message = null;
        String identification = MessageStatus.getIdentification(promotionBean.getBrandCode(), 
	        promotionBean.getOrderTypeCode(), promotionBean.getOrderNo());
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
                    wrappedMessageBox(msgBox, promotionBean, true, false);
	        	imPromotionFullService.startProcess(promotionBean);
            }else{
	        if(msgBox != null)
	            wrappedMessageBox(msgBox, promotionBean, false, false);
	        if(!OrderStatus.SAVE.equals(formStatus) || (OrderStatus.REJECT.equals(beforeChangeStatus)
    	                && OrderStatus.SAVE.equals(formStatus))){
	            if(StringUtils.hasText(assignmentId)){
                        Long assignId = NumberUtils.getLong(assignmentId);
	                Boolean result = Boolean.valueOf(approvalResult);
	                Object[] processInfo = imPromotionFullService.completeAssignment(assignId, result);
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
            message = "執行促銷流程發生錯誤，原因：" + ex.toString();
            siProgramLogAction.createProgramLog(ImPromotionFullService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, promotionBean.getLastUpdatedBy());
            log.error(message);
        }
    }

    public List<Properties> performTransactionForBackGround(Map parameterMap){

        Map returnMap = new HashMap(0);	
        ImPromotion promotionBean = null;
        Map resultMap = null;
        String message = null;
        String identification = null;
        try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = imPromotionMainService.getPromotionHeadId(formLinkBean);
            promotionBean = imPromotionMainService.findById(headId);
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");	    	    
            try{      	     	       
	        promotionBean = imPromotionItemService.saveCheckedPromotionData(headId, parameterMap, ImPromotionMainService.PROGRAM_ID, employeeCode);
            }catch(Exception ex){
	        if(promotionBean != null){
	            identification = MessageStatus.getIdentification(promotionBean.getBrandCode(), 
		    promotionBean.getOrderTypeCode(), promotionBean.getOrderNo());
	            message = ex.getMessage();
	            siProgramLogAction.createProgramLog(ImPromotionMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
		            identification, message, promotionBean.getLastUpdatedBy());
	        }
            }
            resultMap = new HashMap();
            resultMap.put("entityBean", promotionBean);
            //==============execute flow==================
            if(promotionBean != null)
                executeProcess(otherBean, resultMap, promotionBean, null);	       
        }catch (Exception ex) {
            message = "執行促銷單背景存檔失敗，原因：" + ex.toString();
            System.out.println(message);
            log.error(message);
        }
        return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    public List<Properties> performSearchInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
            String vatPickerId = (String)PropertyUtils.getProperty(otherBean, "vatPickerId");
            boolean isDirectSearch = true;
            if(StringUtils.hasText(vatPickerId)){
        	isDirectSearch = false;
            }
	    validateInitialRequiredParameters(parameterMap, isDirectSearch);
	    returnMap = imPromotionMainService.executeSearchInitial(parameterMap);
	}catch (Exception ex) {
	    System.out.println("執行促銷活動查詢初始化時發生錯誤，原因：" + ex.toString());
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
    		returnMap = imPromotionMainService.getSearchSelection(parameterMap);	    
    	}catch (Exception ex) {
    		MessageBox msgBox = new MessageBox();
    		msgBox.setMessage(ex.getMessage());
    		returnMap = new HashMap();
    		returnMap.put("vatMessage" ,msgBox);
    	}

    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    private void listErrorMessageBox(MessageBox msgBox){

	Command cmd_error = new Command();
	cmd_error.setCmd(Command.FUNCTION);
	cmd_error.setParameters(new String[] { "showMessage()", "" });
	msgBox.setOk(cmd_error);
    }
}
