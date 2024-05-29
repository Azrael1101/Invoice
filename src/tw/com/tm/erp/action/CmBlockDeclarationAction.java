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
import tw.com.tm.erp.hbm.bean.CmBlockDeclarationHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.CmBlockDeclarationHeadDAO;
import tw.com.tm.erp.hbm.service.CmBlockDeclarationService;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class CmBlockDeclarationAction {

	private static final Log log = LogFactory.getLog(CmBlockDeclarationAction.class);
	public static final String PROGRAM_ID = "CM_BLOCK_DECLARATION";
	private CmBlockDeclarationService cmBlockDeclarationService;
	private SiProgramLogAction siProgramLogAction;
	private WfApprovalResultService wfApprovalResultService;
	private CmBlockDeclarationHeadDAO cmBlockDeclarationHeadDAO;

	/**
	 * @param cmBlockDeclarationHeadDAO the cmBlockDeclarationHeadDAO to set
	 */
	public void setCmBlockDeclarationHeadDAO(CmBlockDeclarationHeadDAO cmBlockDeclarationHeadDAO) {
		this.cmBlockDeclarationHeadDAO = cmBlockDeclarationHeadDAO;
	}

	/**
	 * @param cmBlockDeclarationService
	 *            the cmBlockDeclarationService to set
	 */
	public void setCmBlockDeclarationService(CmBlockDeclarationService cmBlockDeclarationService) {
		this.cmBlockDeclarationService = cmBlockDeclarationService;
	}

	/**
	 * @param siProgramLogAction
	 *            the siProgramLogAction to set
	 */
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	/**
	 * @param wfApprovalResultService
	 *            the wfApprovalResultService to set
	 */
	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	/**
	 * 單據初始化
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = cmBlockDeclarationService.executeInitial(parameterMap);
		} catch (Exception ex) {
			System.out.println("執行鎖定報單單據初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 送出
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		CmBlockDeclarationHead cmBlockDeclarationHead = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object formBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");

			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "action");

			// 驗證必要欄位
			String status = (String) PropertyUtils.getProperty(formBean, "status");
			if (!StringUtils.hasText(status)) {
				throw new ValidationErrorException("status參數為空值，無法執行存檔！");
			}

			// 捆綁前端訊息
			String message = "";
			resultMap = cmBlockDeclarationService.saveCmBlockDeclarationBean(parameterMap, formAction);
			message = "報單單據鎖定作業已"
					+ OrderStatus.getChineseWord(((CmBlockDeclarationHead) resultMap.get("entityBean")).getStatus()) + "！";

			cmBlockDeclarationHead = (CmBlockDeclarationHead) resultMap.get("entityBean");
			cmBlockDeclarationHeadDAO.update(cmBlockDeclarationHead);
			msgBox.setMessage(message);

			executeProcess(otherBean, resultMap, cmBlockDeclarationHead, msgBox);
		} catch (ValidationErrorException ve) {
			log.error("執行報單單據鎖定作業檢核失敗，原因：" + ve.toString());
			if (MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage())) {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			} else {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行報單單據鎖定作業存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 起流程
	 * 
	 * @param otherBean
	 * @param resultMap
	 * @param cmBlockDeclarationHead
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, CmBlockDeclarationHead cmBlockDeclarationHead, MessageBox msgBox) throws Exception {

		String message = null;
		String identification = MessageStatus.getIdentification("T2", cmBlockDeclarationHead.getOrderTypeCode(),
				cmBlockDeclarationHead.getOrderNo());
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			String formStatus = cmBlockDeclarationHead.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, formStatus);

			log.info("processId = " + processId);
			log.info("assignmentId = " + assignmentId);
			log.info("formStatus = " + formStatus);
			log.info("approvalResultMsg = " + approvalResultMsg);

			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null) {
					wrappedMessageBox(msgBox, cmBlockDeclarationHead, true, false);
				}
				Object processObj[] = cmBlockDeclarationService.startProcess(cmBlockDeclarationHead);
			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, cmBlockDeclarationHead, false, false);
				// if (!OrderStatus.FORM_SAVE.equals(formStatus)) {
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Object[] processInfo = cmBlockDeclarationService.completeAssignment(assignId, true);

				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId);
				}
			}
		} catch (Exception ex) {
			message = "執行報單鎖定作業流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(CmMovementService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification,
					message, cmBlockDeclarationHead.getLastUpdatedBy());
			log.error(message);
		}
	}

	/**
	 * 取得簽核結果
	 *
	 * @param processId
	 * @param formStatus
	 * @return
	 */
	private String getApprovalResult(String processId, String formStatus) {

		String approvalResult = "送出";
		if (StringUtils.hasText(processId)) {
			if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)
					|| OrderStatus.REJECT.equals(formStatus)) {
				approvalResult = OrderStatus.getChineseWord(formStatus);
			} else if (OrderStatus.SIGNING.equals(formStatus)) {
				approvalResult = "核准";
			}
		}
		return approvalResult;
	}

	/**
	 * 綑綁回前畫面的視窗
	 *
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, CmBlockDeclarationHead cmBlockDeclarationHead, boolean isStartProcess, boolean isExecFunction) {

		String orderNo = cmBlockDeclarationHead.getOrderNo();
		String status = cmBlockDeclarationHead.getStatus();
		String identification = orderNo;

		Command cmd_ok = new Command();

		if (isExecFunction) {
			msgBox.setMessage(identification + "表單已送出！");
		} else {
			msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
		}
		cmd_ok.setCmd(Command.WIN_CLOSE);

		msgBox.setOk(cmd_ok);

		if (isExecFunction) {
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
			msgBox.setBefore(cmd_bf);
		}
	}
}
