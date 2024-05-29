package tw.com.tm.erp.action;

import java.util.Date;
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
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.service.ImAdjustmentHeadService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadMainService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImAdjustmentHeadAction {
    
    private static final Log log = LogFactory.getLog(ImAdjustmentHeadAction.class);
    
    private ImAdjustmentHeadService imAdjustmentHeadService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    private ImAdjustmentHeadDAO imAdjustmentHeadDAO;

    

	public void setImAdjustmentHeadService(ImAdjustmentHeadService imAdjustmentHeadService) {
		this.imAdjustmentHeadService = imAdjustmentHeadService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
	    this.wfApprovalResultService = wfApprovalResultService;
	}
    
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	    this.siProgramLogAction = siProgramLogAction;
	}
	

    public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
	    this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}

    public List<Properties> performTransaction(Map parameterMap){
	
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	
	try{
		
	    Map resultMap = imAdjustmentHeadService.updateAJAXImAdjustment(parameterMap);
	    ImAdjustmentHead imAdjustmentHead = (ImAdjustmentHead)resultMap.get("entityBean");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    //Object otherBean = parameterMap.get("vatBeanOther");
	    wrappedMessageBox(msgBox,imAdjustmentHead,true,false);
	    executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);
	}catch (Exception ex) {
	    System.out.println("執行拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行拆/拼/變更貨號單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    
    }
    
    
    private void wrappedMessageBox(MessageBox msgBox, ImAdjustmentHead imAdjustmentBean, boolean isStartProcess,boolean isExecFunction){
	
	String orderTypeCode = imAdjustmentBean.getOrderTypeCode();
	String orderNo = imAdjustmentBean.getOrderNo();
	String status = imAdjustmentBean.getStatus();
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
	    returnMap = imAdjustmentHeadService.executeInitial(parameterMap);
	    
	}catch (Exception ex) {
	    System.out.println("執行商品調整單初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    private void executeProcess(Object otherBean, Map resultMap, ImAdjustmentHead imAdjustmentHead, MessageBox msgBox) 
	throws Exception {
    
        String message = null;
        String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), 
        	imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
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
                      
            		wrappedMessageBox(msgBox, imAdjustmentHead, true, false);
            		imAdjustmentHeadService.startProcess(imAdjustmentHead);
                    
                }else{
            	if(msgBox != null)
            	    wrappedMessageBox(msgBox, imAdjustmentHead, false, false);
            	if(StringUtils.hasText(assignmentId)){
                        Long assignId = NumberUtils.getLong(assignmentId);
            	    Boolean result = Boolean.valueOf(approvalResult);
            	    Object[] processInfo = imAdjustmentHeadService.completeAssignment(assignId, result,approvalComment);
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
                message = "執行拆/拼/變更貨號流程發生錯誤，原因：" + ex.toString();
                siProgramLogAction.createProgramLog(PoPurchaseOrderHeadService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imAdjustmentHead.getLastUpdatedBy());
                log.error(message);
                throw new ProcessFailedException("拆/拼/變更貨號單流程啟動失敗！");
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
	    Map resultMap = imAdjustmentHeadService.updateImAdjustmentHeadWithActualOrderNO(parameterMap);
	    ImAdjustmentHead imAdjHead = (ImAdjustmentHead)resultMap.get("entityBean");
	    msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String processId = (String)PropertyUtils.getProperty(otherBean, "processId");
	    if(!StringUtils.hasText(processId) || "0".equals(processId)){
                wrappedMessageBox(msgBox, imAdjHead, true, true);
	    }else{
		wrappedMessageBox(msgBox, imAdjHead, false, true);
	    }
	}catch (Exception ex) {
	    System.out.println("執行拆/拼/變更貨號單存檔時發生錯誤，原因：" + ex.toString());
	    log.error("執行拆/拼/變更貨號單存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
public List<Properties> performTransactionForBackGround(Map parameterMap){
	
	Map returnMap = new HashMap(0);	
	ImAdjustmentHead imAdjHead = null;
	Map resultMap = null;
	String message = null;
	String identification = null;
	boolean isError = false;
	try{
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = imAdjustmentHeadService.getImAdjustmentHeadId(formLinkBean);
            String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");	    	    
	    try{      	     	       
		imAdjHead = imAdjustmentHeadService.findById(headId);
		identification = MessageStatus.getIdentification(imAdjHead.getBrandCode(), 
			    imAdjHead.getOrderTypeCode(), imAdjHead.getOrderNo());
		imAdjHead.setStatus(OrderStatus.SIGNING);
		imAdjustmentHeadService.checkImAdjustmentData(imAdjHead, imAdjustmentHeadService.PROGRAM_ID, identification);
		imAdjHead.setLastUpdateDate(new Date());
		
		imAdjustmentHeadDAO.update(imAdjHead);
	    }catch(Exception ex){
		isError = true;
		imAdjHead = imAdjustmentHeadService.findById(headId);
		if(imAdjHead != null){
		    message = ex.getMessage();
		    siProgramLogAction.createProgramLog(ImAdjustmentHeadService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
			    identification, message, imAdjHead.getLastUpdatedBy());
		}
	    }
	    resultMap = new HashMap();
	    resultMap.put("entityBean", imAdjHead);
	  //==============execute flow==================
	    if(isError){
		imAdjHead.setStatus(OrderStatus.SAVE);
		imAdjustmentHeadDAO.update(imAdjHead);
	    }
	    if(imAdjHead != null){
		executeProcess(otherBean, resultMap, imAdjHead, null);
	    }
	        	       
	}catch (Exception ex) {
	    message = "執行拆/拼/變更貨號單背景存檔失敗，原因：" + ex.toString();
	    System.out.println(message);
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
	
	/**
	 * 報單展延初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performExtentionInitial(Map parameterMap){
	
		log.info("performExtentionInitial");
		Map returnMap = null;	
		try{
			returnMap = imAdjustmentHeadService.executeExtentionInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行報單展延初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	

	public List<Properties> performExtentionTransaction(Map parameterMap){
		log.info("performExtentionTransaction");
		Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
		ImAdjustmentHead imAdjustmentHead = null;
		
		try{
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
    		Object formBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		
    		// 驗證必要欄位
    		String status = (String)PropertyUtils.getProperty(formBean, "status");

    		if(!StringUtils.hasText(status)){
    			throw new ValidationErrorException("status參數為空值，無法執行存檔！");
    		}
    		
    		// 驗證 把畫面的值 存成BEAN 驗證
    		//imAdjustmentHeadService.validateHead(parameterMap);			
    		// 取得欲更新的bean
			imAdjustmentHead = imAdjustmentHeadService.getActualExtention(formLinkBean);
			// 前端資料塞入bean 
			resultMap = imAdjustmentHeadService.updateImAdjustmentExtentionBean(parameterMap);
			// 存檔  改狀態
			resultMap = imAdjustmentHeadService.updateAJAXExtention2(parameterMap);
			
			parameterMap.put("resultMap", resultMap);
			
			returnMap.put("orderNo", imAdjustmentHead.getOrderNo());
			
			imAdjustmentHead = (ImAdjustmentHead) resultMap.get("entityBean");
			
			wrappedMessageBoxExtention(msgBox, imAdjustmentHead, true , false);
			
			msgBox.setMessage((String)resultMap.get("resultMsg"));
			
			log.info("準備起流程!!");
			executeExtentionProcess(otherBean, resultMap, imAdjustmentHead, msgBox);
			
			//imAdjustmentHeadService.update(imAdjustmentHead);

		/*}catch (FormException ex) {
			log.info("FormException=-------");
			log.error("執行報單展延存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage("執行報單展延存檔失敗");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"refreshForm()", ""});
			msgBox.setOk(cmd_bf);*/
		}catch (Exception ex) {
			log.info("Exception=-------");
			log.error("執行報單展延存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
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
	private void executeExtentionProcess(Object otherBean, Map resultMap, ImAdjustmentHead imAdjustmentHead, MessageBox msgBox) 
		throws Exception {
    
        String message = null;
        String identification = MessageStatus.getIdentification(imAdjustmentHead.getBrandCode(), 
        imAdjustmentHead.getOrderTypeCode(), imAdjustmentHead.getOrderNo());
        try{
        	String formAction 		  = (String)PropertyUtils.getProperty(otherBean,"formAction");
        	String processId          = (String)PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId       = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
			String approvalResult     = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment    = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
			//String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus         = imAdjustmentHead.getStatus();
			String approvalResultMsg  = this.getApprovalResult(processId, formStatus);
    
            if(!StringUtils.hasText(processId) || "0".equals(processId)){
            	if(msgBox != null)
            	{
            		wrappedMessageBoxExtention(msgBox, imAdjustmentHead, true, false);
            	}
            	Object processObj[] = ImAdjustmentHeadService.startExtentionProcess(imAdjustmentHead);
            	imAdjustmentHead.setProcessId((Long)processObj[0]);
             }else{
            	if(msgBox != null)
            	{
            		wrappedMessageBoxExtention(msgBox, imAdjustmentHead, false, false);
            	}
            	if(StringUtils.hasText(assignmentId)){
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					Object[] processInfo = ImAdjustmentHeadService.completeExtenionAssignment(assignId, result,formAction);
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
        	message = "執行報單展延流程發生錯誤，原因：" + ex.toString();
        	siProgramLogAction.createProgramLog(PoPurchaseOrderHeadService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imAdjustmentHead.getLastUpdatedBy());
            log.error(message);
            throw new ProcessFailedException("報單展延流程啟動失敗！");
        }
    }
       
	
	/**
     * 查詢報單展延初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performExtentionSearchInitial(Map parameterMap){
    	Map returnMap = null;	
    	try{
    		returnMap = imAdjustmentHeadService.executeExtentionSearchInitial(parameterMap);	    
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
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param 
	 * @param isStartProcess
	 */
	
	private void wrappedMessageBoxExtention(MessageBox msgBox,ImAdjustmentHead imAdjustmentHead , boolean isStartProcess, boolean isExecFunction){

		log.info("wrappedMessageBox...............");
		String orderTypeCode = imAdjustmentHead.getOrderTypeCode();
		String orderNo = imAdjustmentHead.getOrderNo();
		String status = imAdjustmentHead.getStatus();
		//Long headId = imAdjustmentHead.getHeadId();
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
			msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
			
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

    /**
     * 取得簽核結果
     * @param processId
     * @param formStatus
     * @return
     */
    private String getApprovalResult(String processId, String formStatus) {

	String approvalResult = "送出";
	if (StringUtils.hasText(processId)) {
	    if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)) {
		approvalResult = OrderStatus.getChineseWord(formStatus);
	    }
	}
	return approvalResult;
    }
    
	
	
	
}
