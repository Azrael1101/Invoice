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
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImPromotionService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;

public class PoPurchaseAction {
    
    private static final Log log = LogFactory.getLog(PoPurchaseAction.class);
    
    private PoPurchaseOrderHeadService poPurchaseOrderHeadService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
    
    
    public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
        this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
    }

    public void setPoPurchaseOrderHeadService(PoPurchaseOrderHeadService poPurchaseOrderHeadService) {
	this.poPurchaseOrderHeadService = poPurchaseOrderHeadService;
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
			System.out.println("----------updateHead Po start----------");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
		    	    Object otherBean = parameterMap.get("vatBeanOther");
		    	    Long headId = poPurchaseOrderHeadService.getPoPurchaseHeadId(formLinkBean); 
		    	    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			    if(!StringUtils.hasText(formStatus)){
			        throw new ValidationErrorException("單據狀態參數為空值，無法執行存檔！");	
			    }
			    String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			    if(!StringUtils.hasText(beforeChangeStatus)){
			        throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");	
			    }
			    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
			    /*if((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) 
		    		&& OrderStatus.SIGNING.equals(formStatus)){
				poPurchaseOrderHeadService.saveActualOrderNo(headId, employeeCode);
			    }*/	    
			    Map resultMap = poPurchaseOrderHeadService.updateAJAXPoPurchase(parameterMap);
			    PoPurchaseOrderHead poPurchaseBean = (PoPurchaseOrderHead)resultMap.get("entityBean");
			    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			    executeProcess(otherBean, resultMap, poPurchaseBean, msgBox);
	    
		}catch (Exception ex) {
		    System.out.println("執行採購單存檔時發生錯誤，原因：" + ex.toString());
		    log.error("執行採購單存檔失敗，原因：" + ex.toString());
	        msgBox.setMessage(ex.getMessage());
		}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, PoPurchaseOrderHead poPurchaseBean, boolean isStartProcess, boolean isExecFunction){
	
	String orderTypeCode = poPurchaseBean.getOrderTypeCode();
	String orderNo = poPurchaseBean.getOrderNo();
	String status = poPurchaseBean.getStatus();
	String identification = orderTypeCode + "-" + orderNo;
        Command cmd_ok = new Command();
	if(isStartProcess){
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.HANDLER);
	    cmd_ok.setParameters(new String[]{"main", "clearFormHandler"});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}else{
	    if(OrderStatus.SIGNING.equals(status)){
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
    private void executeProcess(Object otherBean, Map resultMap, PoPurchaseOrderHead poPurchase, MessageBox msgBox) 
    	throws Exception {
        
        String message = null;
        String identification = MessageStatus.getIdentification(poPurchase.getBrandCode(), 
        	 poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
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
                        wrappedMessageBox(msgBox, poPurchase, true, false);
                    
            	    PoPurchaseOrderHeadService.startProcess(poPurchase);
                }else{
            	if(msgBox != null)
            	    wrappedMessageBox(msgBox, poPurchase, false, false);
            	if(StringUtils.hasText(assignmentId)){
                        Long assignId = NumberUtils.getLong(assignmentId);
            	    Boolean result = Boolean.valueOf(approvalResult);
            	   
        	    
            	    Object[] processInfo = PoPurchaseOrderHeadService.completeAssignment(assignId, result,poPurchase);
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
                message = "執行採購流程發生錯誤，原因：" + ex.toString();
                siProgramLogAction.createProgramLog(PoPurchaseOrderHeadService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, poPurchase.getLastUpdatedBy());
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
	    Map resultMap = poPurchaseOrderHeadService.updatePoPurchaseWithActualOrderNO(parameterMap);
	    PoPurchaseOrderHead poPurchaseBean = (PoPurchaseOrderHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
                wrappedMessageBox(msgBox, poPurchaseBean, true, true);
	    }else{
		wrappedMessageBox(msgBox, poPurchaseBean, false, true);
	    }
	}catch (Exception ex) {
	    System.out.println("執行採購單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行採購單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    public List<Properties> performInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    //returnMap = imPromotionService.executeInitial(parameterMap);
	    
	}catch (Exception ex) {
	    System.out.println("執行採購單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    public List<Properties> performTransactionForBackGround(Map parameterMap){
	
	Map returnMap = new HashMap(0);	
	PoPurchaseOrderHead poPurchase = null;
	Map resultMap = null;
	String message = null;
	String identification = null;
	boolean isError = false;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = poPurchaseOrderHeadService.getPoPurchaseHeadId(formLinkBean);
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");	    	    
	    try{      	     	       
		poPurchase = poPurchaseOrderHeadService.findById(headId);
		identification = MessageStatus.getIdentification(poPurchase.getBrandCode(), 
			poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
		poPurchase.setStatus(OrderStatus.SIGNING);
		poPurchaseOrderHeadService.checkPoPurchaseData(poPurchase, PoPurchaseOrderHeadService.PROGRAM_ID, identification);
		poPurchase.setLastUpdateDate(new Date());
		
		poPurchaseOrderHeadDAO.update(poPurchase);
	    }catch(Exception ex){
		isError = true;
	    }
	    resultMap = new HashMap();
	    resultMap.put("entityBean", poPurchase);
	  //==============execute flow==================
	    if(isError){
		poPurchase.setStatus(OrderStatus.SAVE);
		poPurchaseOrderHeadDAO.update(poPurchase);
	    }
	    if(poPurchase != null )
		executeProcess(otherBean, resultMap, poPurchase, null);
	}catch (Exception ex) {
	    message = "執行採購單背景存檔失敗，原因：" + ex.toString();
	    System.out.println(message);
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
}
