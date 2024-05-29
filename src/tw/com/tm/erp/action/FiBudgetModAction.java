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
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CmDeclarationLogService;
import tw.com.tm.erp.hbm.service.FiBudgetModHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

import tw.com.tm.erp.hbm.dao.FiBudgetModHeadDAO;

public class FiBudgetModAction {
    
    private static final Log log = LogFactory.getLog(FiBudgetModAction.class);
    
    private FiBudgetModHeadService fiBudgetModHeadService;

    private WfApprovalResultService wfApprovalResultService;
    
    private SiProgramLogAction siProgramLogAction;
    
    private FiBudgetModHeadDAO fiBudgetModHeadDAO;

	public void setFiBudgetModHeadService(FiBudgetModHeadService fiBudgetModHeadService) {
		this.fiBudgetModHeadService = fiBudgetModHeadService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }
    
    public void setFiBudgetModHeadDAO(FiBudgetModHeadDAO fiBudgetModHeadDAO) {
		this.fiBudgetModHeadDAO = fiBudgetModHeadDAO;
	}

    /**
	 * 取得單號
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */

	public List<Properties> getOrderNo(Map parameterMap) {

		Map returnMap = new HashMap(0);
		Map resultMap = new HashMap();
		MessageBox msgBox = new MessageBox();
		FiBudgetModHead head = new FiBudgetModHead();
		Object otherBean = null;
		try {
			otherBean = parameterMap.get("vatBeanOther");
			String status = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			log.info("formId = " + formId);
			// 驗證必要欄位
			if(!StringUtils.hasText(status))
		    	throw new Exception("status參數為空值，無法執行存檔！");
			if(!StringUtils.hasText(formAction))
		    	throw new Exception("formAction參數為空值，無法執行存檔！");
			if(formId == 0L)
				throw new Exception("identify參數為空值，無法執行存檔！");
			if(!OrderStatus.SIGNING.equals(status)){
				resultMap = fiBudgetModHeadService.updateParameter(parameterMap);
				head = (FiBudgetModHead)resultMap.get("entityBean");
			}else{
				head = fiBudgetModHeadService.findById(formId);
			}
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			msgBox.setMessage(head.getOrderTypeCode()+"-"+head.getOrderNo()+"存檔成功，是否繼續新增？");
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, head, true, true);
			} else {
				wrappedMessageBox(msgBox, head, false, true);
			}
		} catch (ValidationErrorException ve) {
			log.error("執行預算修改儲存失敗，原因：" + ve.toString());
			msgBox.setMessage(ve.getMessage());
			listErrorMsg(msgBox);
		} catch (Exception ex) {
			log.error("執行預算修改儲存失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

    
    public List<Properties> performTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		FiBudgetModHead head = new FiBudgetModHead();
		Object otherBean = null;
		Object formBindBean = null;
		try {
			otherBean = parameterMap.get("vatBeanOther");
			formBindBean = parameterMap.get("vatBeanFormBind");
			String status = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String budgetYear = (String)PropertyUtils.getProperty(formBindBean,"budgetYear");
		    String budgetMonth = (String)PropertyUtils.getProperty(formBindBean,"budgetMonth");
		    String itemType = (String)PropertyUtils.getProperty(formBindBean,"itemType");
		    
			head = fiBudgetModHeadService.getActualHead(formId);
			resultMap.put("entityBean", head);
			
			// 驗證必要欄位
			
			HashMap findPOObjs = new HashMap();
			findPOObjs.put("brandCode", brandCode);
			findPOObjs.put("budgetYear", budgetYear);
			findPOObjs.put("budgetMonth",  budgetMonth);
			findPOObjs.put("itemType",  itemType);
			findPOObjs.put("formId",  formId);
			List<FiBudgetModHead> fiBudgetModHeads = fiBudgetModHeadDAO.getDiffBudget(findPOObjs);  //檢核該年度月份預算是否尚在簽核-Jerome
			log.info("errorMsgs==" + fiBudgetModHeads.size());
			if(fiBudgetModHeads.size() > 0){
				throw new Exception("該年度、月份、業種之預算尚在簽核中");
			}
			
			if(!StringUtils.hasText(status))
		    	throw new Exception("status參數為空值，無法執行存檔！");
			if(!StringUtils.hasText(formAction))
		    	throw new Exception("formAction參數為空值，無法執行存檔！");
				//如果送出的話 不取實際單號
			fiBudgetModHeadService.updateParameter(parameterMap);
//			if(!OrderStatus.SIGNING.equals(status) && OrderStatus.SIGNING.equals(formAction)){
				resultMap = fiBudgetModHeadService.updateCheckedParameter(parameterMap);
//			}
			head = (FiBudgetModHead)resultMap.get("entityBean");
			head.setStatus(formAction);
			fiBudgetModHeadService.update(head);
			msgBox.setMessage(head.getOrderTypeCode()+"-"+head.getOrderNo()+"存檔成功，是否繼續新增？");
			executeProcess(otherBean, resultMap, head, msgBox, false);
		} catch(FormException ve){
			log.error("執行預算修改修改失敗，原因：" + ve.toString());
			msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			listErrorMsg(msgBox);
		} catch (Exception ex) {
			log.error("執行預算修改修改失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
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
		Map resultMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String message = null;
		FiBudgetModHead head = new FiBudgetModHead();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			head = fiBudgetModHeadService.findById(formId);
			resultMap.put("entityBean", head);
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			try {
				if(OrderStatus.SIGNING.equals(formAction) && !OrderStatus.SIGNING.equals(beforeChangeStatus)){
					resultMap = fiBudgetModHeadService.updateCheckedParameter(parameterMap);
					head = (FiBudgetModHead)resultMap.get("entityBean");
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
			msgBox.setMessage("表單已送出");
			executeProcess(otherBean, resultMap, head, msgBox, true);
		}catch(Exception ex){
			message = "執行銷貨單背景存檔失敗，原因：" + ex.toString();
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
    private void executeProcess(Object otherBean, Map resultMap, FiBudgetModHead fiBudgetModHead, MessageBox msgBox, Boolean isBackground)
			throws Exception {

		String message = null;
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String status = fiBudgetModHead.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, status);
			log.info( "processId = " + processId );
			log.info( "assignmentId = " + assignmentId );
			log.info( "approvalResult = " + approvalResult );
			log.info( "approvalComment = " + approvalComment );
			log.info( "status = " + status );
			log.info( "approvalResultMsg = " + approvalResultMsg );
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null && !isBackground ){
					wrappedMessageBox(msgBox, fiBudgetModHead, true, false);
				}
				FiBudgetModHeadService.startProcess(fiBudgetModHead); 
			} else {
				if (msgBox != null){
					wrappedMessageBox(msgBox, fiBudgetModHead, false, false);
				}
					if (StringUtils.hasText(assignmentId)) {
						Long assignId = NumberUtils.getLong(assignmentId);
						Boolean result = Boolean.valueOf(approvalResult);
						Object[] processInfo = CmDeclarationLogService.completeAssignment(assignId, result);
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap,wfApprovalResultService);
					} else {
						throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId+ "、AssignmentId=" + assignmentId+ "、result=" + approvalResult);
					}
				}
		} catch (Exception ex) {
			message = "執行預算修改修改流程發生錯誤，原因：" + ex.toString();
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
			if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)) {
				approvalResult = OrderStatus.getChineseWord(formStatus);
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
    private void wrappedMessageBox(MessageBox msgBox, FiBudgetModHead head, boolean isStartProcess, boolean isExecFunction ){
    	
    	String status = head.getStatus();
    	String identification = head.getOrderTypeCode()+"-"+head.getOrderNo();
    	Command cmd_ok = new Command();
    	if (isStartProcess) {
		    cmd_ok.setCmd(Command.FUNCTION);
			Command cmd_cancel = new Command();
		    cmd_cancel.setCmd(Command.WIN_CLOSE);
		    msgBox.setCancel(cmd_cancel);
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
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
}
