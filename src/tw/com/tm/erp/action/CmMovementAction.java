package tw.com.tm.erp.action;

import java.lang.reflect.InvocationTargetException;
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
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class CmMovementAction {

    private static final Log log = LogFactory.getLog(CmMovementAction.class);

    private CmMovementService cmMovementService;

    private WfApprovalResultService wfApprovalResultService;

    private SiProgramLogAction siProgramLogAction;

    public void setCmMovementService(CmMovementService cmMovementService) {
	this.cmMovementService =  cmMovementService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setWfApprovalResultService(
	    WfApprovalResultService wfApprovalResultService) {
	this.wfApprovalResultService = wfApprovalResultService;
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
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));

			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// 取得欲更新的bean
			CmMovementHead cmMovementHead = cmMovementService.getActualCmMovement(formLinkBean);

			// 確認是否允許流程，不允許會丟例外
			/*ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(cmMovementHead.getProcessId()), processId,
					assignmentId);*/

			Map resultMap = cmMovementService.updateCmMovementWithActualOrderNO(parameterMap);
			CmMovementHead cmMovementHeadNew = (CmMovementHead) resultMap.get("entityBean");

			msgBox.setMessage((String) resultMap.get("resultMsg"));

			if (processId == 0) {
				wrappedMessageBox(msgBox, cmMovementHeadNew, true, true);
			} else {
				wrappedMessageBox(msgBox, cmMovementHeadNew, false, true);
			}
		} catch (Exception ex) {
			log.error("執行移倉單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
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
	    returnMap = cmMovementService.executeInitial(parameterMap);
	}catch (Exception ex) {
	    log.error("執行移倉單存檔初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 移倉單查詢初始化
     *
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;
	try{
	    returnMap = cmMovementService.executeSearchInitial(parameterMap);
	}catch (Exception ex) {
	    log.error("執行移倉單查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 送出
     * @param parameterMap
     * @return
     */
	public List<Properties> performTransaction(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		CmMovementHead cmMovementHead = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object formBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");

			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			// 驗證必要欄位
			String status = (String) PropertyUtils.getProperty(formBean, "status");

			if (!StringUtils.hasText(status)) {
				throw new ValidationErrorException("status參數為空值，無法執行存檔！");
			}

			cmMovementHead = cmMovementService.getActualCmMovement(formLinkBean);
			log.info("取海關單號:狀態"+status+" 動作:"+formAction+" 是否已有單號:"+cmMovementHead.getMoveWhNo()+" 單別:"+cmMovementHead.getOrderTypeCode());
			if(status.equals("SAVE") && (formAction.equals("SAVE")|| formAction.equals("SUBMIT")) & null==cmMovementHead.getMoveWhNo() 
					& (cmMovementHead.getOrderTypeCode().equals("RWK")|| cmMovementHead.getOrderTypeCode().equals("RKW")
					|| cmMovementHead.getOrderTypeCode().equals("RWD")|| cmMovementHead.getOrderTypeCode().equals("RDW") 
					|| cmMovementHead.getOrderTypeCode().equals("RMK")|| cmMovementHead.getOrderTypeCode().equals("RMM")
					|| cmMovementHead.getOrderTypeCode().equals("RMW")|| cmMovementHead.getOrderTypeCode().equals("RVM")
					|| cmMovementHead.getOrderTypeCode().equals("RMV")|| cmMovementHead.getOrderTypeCode().equals("RAP")|| cmMovementHead.getOrderTypeCode().equals("RPA"))){

				log.info("進入取海關單號:");
				cmMovementService.saveRpNo(cmMovementHead, employeeCode);//移倉單申請編號
			}//送出時且為暫存中... need check(status = save)
				

			// 流程控制，避免重複流程造成的錯誤
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
			// 確認是否允許流程，不允許會丟例外
			//ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(cmMovementHead.getProcessId()), processId,
			//		assignmentId);

			// )前端資料塞入bean
			resultMap = cmMovementService.updateCmMovementBean(parameterMap);

			// 檢核與 存取db head and line
			resultMap = cmMovementService.updateAJAXCmMovement(parameterMap, formAction);
			cmMovementHead = (CmMovementHead) resultMap.get("entityBean");

			// 更新運送單資料 by Weichun 2011.03.24
			
			
			if ("RWK".equals(cmMovementHead.getOrderTypeCode())||"RWD".equals(cmMovementHead.getOrderTypeCode()) 
				|| "RMK".equals(cmMovementHead.getOrderTypeCode())|| "RMM".equals(cmMovementHead.getOrderTypeCode())
				|| "RMV".equals(cmMovementHead.getOrderTypeCode())|| "RAP".equals(cmMovementHead.getOrderTypeCode())
				|| "RPA".equals(cmMovementHead.getOrderTypeCode())|| "RVM".equals(cmMovementHead.getOrderTypeCode()))
				cmMovementService.updateCmTransfer(cmMovementHead, formBean, employeeCode);

			msgBox.setMessage((String) resultMap.get("resultMsg"));

			executeProcess(otherBean, resultMap, cmMovementHead, msgBox);

			cmMovementService.update(cmMovementHead);

			cmMovementService.executeAutoCreateImMovement(cmMovementHead);

		} catch (ValidationErrorException ve) {
			log.error("執行移倉單檢核失敗，原因：" + ve.toString());
			if (MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage())) {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			} else {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行移倉單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
		
	public List<Properties> saveWhNo(Map parameterMap) {
		
		MessageBox msgBox = new MessageBox();
		Map returnMap = new HashMap(0);
		CmMovementHead cmMovementHead = null;
		log.info("aaaaa");
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			cmMovementHead = cmMovementService.getActualCmMovement(formLinkBean);
			String moveWhNo = (String)PropertyUtils.getProperty(otherBean, "moveWhNo");
			log.info("bbbbb:"+moveWhNo);
			String msg = cmMovementService.updateMoveWhNo(cmMovementHead, moveWhNo);
			msgBox.setMessage(msg);
			log.info("cccccc");
			//msgBox.setMessage("移倉申請書儲存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgBox.setMessage(e.getMessage());
			returnMap.put("vatMessage", msgBox);
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			//msgBox.setOk(cmd_bf);
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}	

    /**
     * 背景送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {

	Map returnMap = new HashMap(0);
	CmMovementHead cmMovementHead = null;
	Map resultMap = new HashMap(0);
	String message = null;
	try {
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");

	    resultMap = cmMovementService.updateAJAXCmMovement(parameterMap, formAction);
	    cmMovementHead = (CmMovementHead) resultMap.get("entityBean");
	    //==============execute flow==================
	    if (cmMovementHead != null){
		executeProcess(otherBean, resultMap, cmMovementHead, null);
	    }
	    cmMovementService.update(cmMovementHead);

	    cmMovementService.executeAutoCreateImMovement(cmMovementHead);
	} catch (Exception ex) {
	    message = "執行移倉單背景存檔失敗，原因：" + ex.toString();
	    log.error(message);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 執行流程
     * @param otherBean
     * @param resultMap
     * @param cmMovementHead
     * @param msgBox
     * @throws Exception
     */
    private void executeProcess(Object otherBean, Map resultMap,
	    CmMovementHead cmMovementHead, MessageBox msgBox)
    throws Exception {

	String message = null;
	String identification = MessageStatus.getIdentification(
		cmMovementHead.getBrandCode(), cmMovementHead
		.getOrderTypeCode(), cmMovementHead.getOrderNo());
	try {
	    String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
	    String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
	    String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
	    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
//	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
	    String formStatus = cmMovementHead.getStatus();
	    String approvalResultMsg = this.getApprovalResult(processId, formStatus);

	    log.info( "processId = " + processId );
	    log.info( "assignmentId = " + assignmentId );
	    log.info( "approvalResult = " + approvalResult );
	    log.info( "approvalComment = " + approvalComment );
	    log.info( "formStatus = " + formStatus );
	    log.info( "approvalResultMsg = " + approvalResultMsg );

	    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		if (msgBox != null){
		    wrappedMessageBox(msgBox, cmMovementHead, true, false);
		}
		Object processObj[] = CmMovementService.startProcess(cmMovementHead);
		cmMovementHead.setProcessId((Long)processObj[0]);
	    } else {
		if (msgBox != null)
		    wrappedMessageBox(msgBox, cmMovementHead, false, false);
//		if (!OrderStatus.FORM_SAVE.equals(formStatus)) {
		if (StringUtils.hasText(assignmentId)) {
		    Long assignId = NumberUtils.getLong(assignmentId);
		    Boolean result = Boolean.valueOf(approvalResult);
		    Object[] processInfo = CmMovementService
		    .completeAssignment(assignId, result);
		    resultMap.put("processId", processInfo[0]);
		    resultMap.put("activityId", processInfo[1]);
		    resultMap.put("activityName", processInfo[2]);
		    resultMap.put("result", approvalResultMsg);
		    resultMap.put("approvalComment", approvalComment);
		    WfApprovalResultUtils.logApprovalResult(resultMap,
			    wfApprovalResultService);
		} else {
		    throw new ProcessFailedException(
			    "Complete assignment失敗，ProcessId=" + processId
			    + "、AssignmentId=" + assignmentId
			    + "、result=" + approvalResult);
		}
//		}
	    }
	} catch (Exception ex) {
	    message = "執行移倉單流程發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(CmMovementService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,cmMovementHead.getLastUpdatedBy());
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
    /*private void wrappedMessageBox(MessageBox msgBox, CmMovementHead head, boolean isStartProcess, boolean isExecFunction ){

	String orderTypeCode = head.getOrderTypeCode();
	String orderNo = head.getOrderNo();
	String status = head.getStatus();
	String identification = orderTypeCode + "-" + orderNo;

	Command cmd_ok = new Command();
	if (isStartProcess) {
	    cmd_ok.setCmd(Command.FUNCTION);
	    log.info( "是否出錯 = " + MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) );
//	    if( MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) ){
//	    msgBox.setType(MessageBox.ALERT);
//	    cmd_ok.setParameters(new String[]{"showMessage()", ""});
//	    }else{
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);

	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
//	    }
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
    }*/
    //For國際免稅店海關上傳
    private void wrappedMessageBox(MessageBox msgBox, CmMovementHead head, boolean isStartProcess, boolean isExecFunction ){
        
    	log.info("aaaaaaaaa:"+isExecFunction+" "+isStartProcess);
    	String orderTypeCode = head.getOrderTypeCode();
    	String orderNo = head.getOrderNo();
    	String status = head.getStatus();
    	String identification = orderTypeCode + "-" + orderNo;
        log.info("海關單別:"+orderTypeCode);
    	if(orderTypeCode.equals("RWD")||orderTypeCode.equals("RDW")||orderTypeCode.equals("RWK")||orderTypeCode.equals("RKW")||orderTypeCode.equals("RMV")){
		    	Command cmd_ok = new Command();
		    	if (isStartProcess) {
		    	    cmd_ok.setCmd(Command.FUNCTION);
		    	    log.info( "是否出錯 = " + MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) );
		//    	    if( MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) ){
		//    	    msgBox.setType(MessageBox.ALERT);
		//    	    cmd_ok.setParameters(new String[]{"showMessage()", ""});
		//    	    }else{
		    	    Command cmd_cancel = new Command();
		    	    cmd_cancel.setCmd(Command.WIN_CLOSE);
		    	    msgBox.setCancel(cmd_cancel);
		
		    	    msgBox.setType(MessageBox.CONFIRM);
		    	    cmd_ok.setParameters(new String[]{"refreshCusForm()", ""});
		//    	    }
		    	}else{
		    	    if( isExecFunction ){
		    		msgBox.setMessage(identification + "表單已送出！");
		    	    }else{
		    	    	log.info("海關狀態呈現:"+head.getStatus());
		    	     if(head.getStatus()!="SAVE"&&head.getStatus()!="VOID"){
			    	    cmd_ok.setCmd(Command.FUNCTION);	
			    	    msgBox.setType(MessageBox.CONFIRM);	
			    		msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！是否送簽海關?");
			    		cmd_ok.setParameters(new String[]{"refreshCusForm()", ""});
		    	     }else{
		    	    	 msgBox.setMessage(identification + "表單已送出！"); 
		    	    	 cmd_ok.setCmd(Command.WIN_CLOSE);
		    	     }
		    	    }
		    	    //msgBox.setType(MessageBox.CONFIRM);
		    	    //cmd_ok.setParameters(new String[]{"refreshCusForm()", ""});
		    	    
		    	}
		
		    	msgBox.setOk(cmd_ok);
		
		    	if (isExecFunction) {
		    	    Command cmd_bf = new Command();
		    	    cmd_bf.setCmd(Command.FUNCTION);
		    	    cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
		    	    msgBox.setBefore(cmd_bf);
		    	}
    	}else{
    		Command cmd_ok = new Command();
    		if (isStartProcess) {
    		    cmd_ok.setCmd(Command.FUNCTION);
    		    log.info( "是否出錯 = " + MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) );
//    		    if( MessageStatus.VALIDATION_FAILURE.equals( msgBox.getMessage()) ){
//    		    msgBox.setType(MessageBox.ALERT);
//    		    cmd_ok.setParameters(new String[]{"showMessage()", ""});
//    		    }else{
    		    Command cmd_cancel = new Command();
    		    cmd_cancel.setCmd(Command.WIN_CLOSE);
    		    msgBox.setCancel(cmd_cancel);

    		    msgBox.setType(MessageBox.CONFIRM);
    		    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
//    		    }
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
	 * 修改調撥日期
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> modifyReqNo(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		try {
			HashMap resultMap = cmMovementService.updateReqNo(parameterMap);
            if(resultMap.get("vatMessage")==null){
			   msgBox.setMessage("完稅移倉申請書號碼修改成功");
            }else{
               msgBox.setMessage("此單別不可修改移倉申請書號碼");	
            }
			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.WIN_CLOSE);
			msgBox.setOk(cmd_ok);
			msgBox.setType(MessageBox.ALERT);
		} catch (Exception ex) {
			System.out.println("申請書號碼修改失敗，原因：" + ex.toString());
			log.error("申請書號碼修改失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
