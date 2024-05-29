package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.dao.ImItemDiscountModHeadDAO;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModLine;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ImItemDiscountModService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModHead;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImItemDiscountModAction {

	private static final Log log = LogFactory.getLog(ImItemDiscountModAction.class);	
	
	private ImItemDiscountModHeadDAO ImItemDiscountModHeadDAO;
    private SiProgramLogAction siProgramLogAction;
    private WfApprovalResultService wfApprovalResultService;
    private ImItemDiscountModService imItemDiscountModService;
	
	public void setImItemDiscountModService(ImItemDiscountModService imItemDiscountModService) {
		this.imItemDiscountModService = imItemDiscountModService;
	}
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
    public void setWfApprovalResultService(
			WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}
    
	
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imItemDiscountModService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行商品折扣檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

    /**
     * 執行流程
     * @param Object otherBean
     * @param Map resultMap
     * @param ImItemDiscountModHead
     * @param MessageBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap,
    		ImItemDiscountModHead head, MessageBox msgBox)
			throws Exception {

		String message = null;
		String identification = MessageStatus.getIdentification(
				head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus = head.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, formStatus);

			log.info( "processId = " + processId );
			log.info( "assignmentId = " + assignmentId );
			log.info( "approvalResult = " + approvalResult );
			log.info( "approvalComment = " + approvalComment );
			log.info( "formStatus = " + formStatus );
			log.info( "approvalResultMsg = " + approvalResultMsg );
log.info("jason1");			
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
log.info("jason2");
				if (msgBox != null){
log.info("jason3");
					wrappedMessageBox(msgBox, head, true, false);
				}
				ImItemDiscountModService.startProcess(head);
			} else {
log.info("jason4");
				if (msgBox != null){
log.info("jason5");
					wrappedMessageBox(msgBox, head, false, false);
				}
				if (StringUtils.hasText(assignmentId)) {
log.info("jason6");
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					Object[] processInfo = ImItemDiscountModService
							.completeAssignment(assignId, result);
					if (!OrderStatus.SAVE.equals(formStatus) ) {
log.info("jason7");
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
			message = "執行商品折扣流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(ImItemDiscountModService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,head.getLastUpdatedBy());
			log.error(message);
		}
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
		ImItemDiscountModHead head = new ImItemDiscountModHead();
		Object otherBean = null;
			try {
		
				Object formBean = parameterMap.get("vatBeanFormBind");
				otherBean = parameterMap.get("vatBeanOther");
							
				String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			
				// 驗證必要欄位
				String status = (String)PropertyUtils.getProperty(formBean, "status");
				if(!StringUtils.hasText(status)){
		    	throw new ValidationErrorException("status參數為空值，無法執行存檔！");
		    }
			
				// 前端資料塞入bean 
				resultMap = imItemDiscountModService.updateImItemDiscountBean(parameterMap);
				// 檢核與 存取db head and line
				resultMap = imItemDiscountModService.updateAJAXImItemDiscount(parameterMap, formAction );
			
				head = (ImItemDiscountModHead) resultMap.get("entityBean");	
			
				msgBox.setMessage((String) resultMap.get("resultMsg") );
			
				executeProcess( otherBean, resultMap, head, msgBox );
		
			} catch( ValidationErrorException ve ){
				log.error("執行商品折扣檢核失敗，原因：" + ve.toString());
				if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			}else{
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			log.error("執行商品折扣存檔失敗，原因：" + ex.toString());
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
	 * 取得單號
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> getOrderNo(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Map resultMap = imItemDiscountModService.updateImItemDiscountWithActualOrderNO(parameterMap);
			ImItemDiscountModHead imItemDiscountHead = (ImItemDiscountModHead) resultMap.get("entityBean");
			
			msgBox.setMessage((String) resultMap.get("resultMsg") );
			Object otherBean = parameterMap.get("vatBeanOther");
			String processId = (String) PropertyUtils.getProperty(otherBean,
					"processId");
			
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, imItemDiscountHead, true, true);
			} else {
				wrappedMessageBox(msgBox, imItemDiscountHead, false, true);
			}
		} catch (Exception ex) {
			log.error("執行商品折扣存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

    /**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, ImItemDiscountModHead head, boolean isStartProcess, boolean isExecFunction ){
    	
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
		    cmd_ok.setParameters(new String[]{"createNewForm()", ""});
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
	 * 查詢商品折扣初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imItemDiscountModService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行查詢商品折扣初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
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
		ImItemDiscountModHead head = null;
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
		
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");		
			resultMap = imItemDiscountModService.updateAJAXImItemDiscount(parameterMap,formAction); 
			head = (ImItemDiscountModHead) resultMap.get("entityBean");
			
			//==============execute flow==================
			if (head != null){
				executeProcess(otherBean, resultMap, head, null);
			}
			// 執行流程
//			imItemDiscountModService.startProcess(head);
		} catch (Exception ex) {
			message = "執行商品折扣背景存檔失敗，原因：" + ex.toString();
			log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
    

}
