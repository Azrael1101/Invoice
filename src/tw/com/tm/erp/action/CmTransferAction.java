package tw.com.tm.erp.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.bean.CmTransfer;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.hbm.service.CmTransferService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA : Weichun.Liao
 * PG : Weichun.Liao
 * Filename : CmTransferAction.java
 * Function : 貨櫃（物）運送單
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	2011/4/6		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
public class CmTransferAction {

	private static final Log log = LogFactory.getLog(CmTransferAction.class);

	private CmTransferService cmTransferService;

	private CmMovementService cmMovementService;

	private WfApprovalResultService wfApprovalResultService;

	private SiProgramLogAction siProgramLogAction;

	private ImMovementHeadDAO imMovementHeadDAO;

	public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
		this.imMovementHeadDAO = imMovementHeadDAO;
	}

	public void setCmTransferService(CmTransferService cmTransferService) {
		this.cmTransferService = cmTransferService;
	}

	public void setCmMovementService(CmMovementService cmMovementService) {
		this.cmMovementService = cmMovementService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	/**
	 * 初始化
	 *
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap) {
		System.out.println("===== 表單初始化 =====");
		Map returnMap = null;
		try {
			returnMap = cmTransferService.executeInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行運送單存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
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
		CmTransfer cmTransfer = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object formBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");

			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");

			// 驗證必要欄位
			String status = (String) PropertyUtils.getProperty(formBean, "status");

			if (!StringUtils.hasText(status)) {
				throw new ValidationErrorException("status參數為空值，無法執行存檔！");
			}

			cmTransfer = cmTransferService.getActualCmTransfer(formBean);

			// 前端資料塞入bean
			resultMap = cmTransferService.updateCmTransferBean(parameterMap);

			// 檢核存取DB
			resultMap = cmTransferService.updateAJAXCmTransfer(parameterMap, formAction);
			cmTransfer = (CmTransfer) resultMap.get("entityBean");
			msgBox.setMessage((String) resultMap.get("resultMsg"));

			// 出站時間
			String leaveTimeT = (String) PropertyUtils.getProperty(formBean, "leaveTimeT");
			String arriveTimeT = (String) PropertyUtils.getProperty(formBean, "arriveTimeT");
			if (!leaveTimeT.equals("")) {
				String leaveDateString = (Integer.parseInt(leaveTimeT.substring(0, 3)) + 1911) + "/"
						+ leaveTimeT.substring(3, 5) + "/" + leaveTimeT.substring(5, 7) + " " + leaveTimeT.substring(7, 9)
						+ ":" + leaveTimeT.substring(9) + ":00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = sdf.parse(leaveDateString);
				cmTransfer.setLeaveTime(date);
			} else {
				cmTransfer.setLeaveTime(null);
			}
			if (!arriveTimeT.equals("")) {
				String arriveDateString = (Integer.parseInt(arriveTimeT.substring(0, 3)) + 1911) + "/"
						+ arriveTimeT.substring(3, 5) + "/" + arriveTimeT.substring(5, 7) + " "
						+ arriveTimeT.substring(7, 9) + ":" + arriveTimeT.substring(9) + ":00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = sdf.parse(arriveDateString);
				cmTransfer.setArriveTime(date);
			}else{
				cmTransfer.setArriveTime(null);
			}

			// TMA表示移倉單的運送單，需同時修改移倉單資料，並且檢核數量
			if("TMA".equals(cmTransfer.getTransferOrderNo().substring(0,3))){
				CmMovementHead cmMovementHead = cmTransferService.executeFindCmMovement(cmTransfer.getTransferOrderNo());
				String orgStatus = cmMovementHead.getStatus(); // 需保持移倉單原始狀態，僅修改運送單資料
				AjaxUtils.copyJSONBeantoPojoBean(formBean, cmMovementHead);
				cmMovementHead.setStatus(orgStatus);

				// 檢核運送單出貨箱數以及件數是否與移倉單相符
				Double boxCount = 0.0;
				Double itemCount = 0.0;
				List<CmMovementLine> cmMovementLines = cmMovementHead.getCmMovementLines();
				for (int i = 0; i < cmMovementLines.size(); i++) {
					ImMovementHead imHead = imMovementHeadDAO.findMovementByIdentification(cmMovementHead.getBrandCode(),
							((CmMovementLine) cmMovementLines.get(i)).getOrderTypeCode(), ((CmMovementLine) cmMovementLines
									.get(i)).getOrderNo());
					boxCount += imHead.getBoxCount();
					itemCount += imHead.getItemCount();
				}
				if (boxCount.intValue() != cmTransfer.getLeaveBox())
					throw new Exception("放行總數量箱數與移倉單不符，請確認後重新輸入！");
				if (itemCount.intValue() != cmTransfer.getLeaveQuantity())
					throw new Exception("放行總數量件數與移倉單不符，請確認後重新輸入！");
				if (boxCount.intValue() != cmTransfer.getTruckBox())
					throw new Exception("本櫃車箱數與移倉單不符，請確認後重新輸入！");
				if (itemCount.intValue() != cmTransfer.getTruckQuantity())
					throw new Exception("本櫃車件數與移倉單不符，請確認後重新輸入！");
				cmMovementService.update(cmMovementHead);
			}

			cmTransferService.update(cmTransfer);
			// 起流程
			executeProcess(otherBean, resultMap, cmTransfer, msgBox);

		} catch (ValidationErrorException ve) {
			log.error("執行運送單檢核失敗，原因：" + ve.toString());
			if (MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage())) {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			} else {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			log.error("執行運送單存檔失敗，原因：" + pe.toString());
			msgBox.setMessage("時間格式輸入錯誤，請重新輸入！：" + pe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行運送單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * pop 錯誤訊息
	 */
	private void listErrorMsg(MessageBox msgBox) {
		Command cmd_ok = new Command();
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		msgBox.setType(MessageBox.ALERT);
		cmd_ok.setCmd(Command.FUNCTION);
		cmd_ok.setParameters(new String[] { "showMessage()", "" });
		msgBox.setOk(cmd_ok);
	}

	/**
	 * 執行流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param cmMovementHead
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, CmTransfer cmTransfer, MessageBox msgBox) throws Exception {

		String message = null;
		String identification = MessageStatus.getIdentification("T2", cmTransfer.getTransferOrderNo().substring(0, 3),
				cmTransfer.getTransferOrderNo().substring(3));
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			String formStatus = cmTransfer.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, formStatus);

			log.info("processId = " + processId);
			log.info("assignmentId = " + assignmentId);
			log.info("formStatus = " + formStatus);
			log.info("approvalResultMsg = " + approvalResultMsg);

			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null) {
					wrappedMessageBox(msgBox, cmTransfer, true, false);
				}
				Object processObj[] = CmTransferService.startProcess(cmTransfer);
			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, cmTransfer, false, false);
				// if (!OrderStatus.FORM_SAVE.equals(formStatus)) {
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Object[] processInfo = CmTransferService.completeAssignment(assignId, true);

				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId);
				}
			}
		} catch (Exception ex) {
			message = "執行運送單流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(CmMovementService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification,
					message, cmTransfer.getLastUpdatedBy());
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
	private void wrappedMessageBox(MessageBox msgBox, CmTransfer cmTransfer, boolean isStartProcess, boolean isExecFunction) {

		String orderNo = cmTransfer.getTransferOrderNo();
		String status = cmTransfer.getStatus();
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

	/**
	 * 執行反確認功能
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> executeReverter(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		CmTransfer cmTransfer = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object formBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");

			cmTransfer = cmTransferService.getActualCmTransfer(formBean);
			cmTransfer.setStatus(OrderStatus.SAVE);
			cmTransferService.update(cmTransfer);
			// 起流程
			executeProcess(otherBean, resultMap, cmTransfer, msgBox);
			msgBox.setMessage("反確認成功，" + msgBox.getMessage());
		} catch (ValidationErrorException ve) {
			log.error("執行運送單檢核失敗，原因：" + ve.toString());
			if (MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage())) {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			} else {
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行運送單反確認失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
