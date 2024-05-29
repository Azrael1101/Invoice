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
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.service.SoDeliveryMoveService;
import tw.com.tm.erp.hbm.service.SoDeliveryService;
import tw.com.tm.erp.hbm.service.CeapProcessService;
import tw.com.tm.erp.hbm.service.TmpAjaxSearchDataService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class SoDeliveryMoveAction {

    private static final Log log = LogFactory.getLog(SoDeliveryMoveAction.class);

	private SoDeliveryService soDeliveryService;
	private SoDeliveryMoveService soDeliveryMoveService;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private SiProgramLogAction siProgramLogAction;
	private TmpAjaxSearchDataService tmpAjaxSearchDataService;
	private WfApprovalResultService wfApprovalResultService;
	public void setSoDeliveryService(SoDeliveryService soDeliveryService) {
		this.soDeliveryService = soDeliveryService;
	}
	
	public void setSoDeliveryMoveService(SoDeliveryMoveService soDeliveryMoveService) {
		this.soDeliveryMoveService = soDeliveryMoveService;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
		this.soDeliveryHeadDAO = soDeliveryHeadDAO;
	}

	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

    /**
	 * 執行入提單交易流程
	 *
	 * @param parameterMap
	 * @return
     * @throws Exception 
	 */
	  /**
	 * 執行調撥單交易流程
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap) {

    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String logMessage = new String();
		Long lineHeadId = new Long(0);
		log.info("1.performTransaction");
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = soDeliveryMoveService.getHeadId(formLinkBean);
			
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			
			if (!StringUtils.hasText(beforeChangeStatus)) {
				throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");
			}
			
			// 流程控制，避免重複流程造成的錯誤 2010.07.02
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));

			SoDeliveryMoveHead soDeliveryMoveHead = soDeliveryMoveService.findById(headId);
			// 確認是否允許流程，不允許會丟例外
			System.out.println("===========processId--111============"+processId);
			System.out.println("===========assignmentId--111============"+assignmentId);
			
			ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(soDeliveryMoveHead.getProcessId()), processId, assignmentId);

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			soDeliveryMoveService.saveActualOrderNo(headId, employeeCode);
			
			HashMap resultMap = soDeliveryMoveService.updateAJAX(parameterMap);

			SoDeliveryMoveHead bean = (SoDeliveryMoveHead) resultMap.get("entityBean");
			
			returnMap.put("orderNo",bean.getOrderNo());
			
			if ("SUBMIT".equals(formAction)){
				
				List<SoDeliveryMoveLine> lines =bean.getSoDeliveryMoveLines();
				//append soDeliveryLog by each line 
				
				for(SoDeliveryMoveLine line : lines){
					logMessage=bean.getOrderTypeCode()+bean.getOrderNo()+"-"+line.getIndexNo()+
					           " 將入提庫存從「"+soDeliveryMoveService.getStoreArea(bean.getDeliveryStoreArea())+"：" + line.getDeliveryStoreCode() + "」移轉至"+
							   "「"+soDeliveryMoveService.getStoreArea(bean.getArrivalStoreArea() )+"："+line.getArrivalStoreCode()+"」";
					lineHeadId = soDeliveryService.findHeadIdBySearchKey(bean.getBrandCode(), line.getDeliveryOrderType(), line.getDeliveryOrderNo(), null);
					
					soDeliveryService.updateSoDeliveyLog(lineHeadId, "MANUAL", "MOVE", "SUCCESS", logMessage, bean.getMoveEmployee());
				}
				//msgBox.setMessage((String) resultMap.get("resultMsg") + "，是否列印報表？");
			}
			
			executeProcess(otherBean, resultMap, bean, msgBox);

			// 最後在更新一次 proccessId
			soDeliveryMoveService.updateProcessId(bean.getHeadId(), bean.getProcessId() == null ? (Long)resultMap
					.get("processId") : bean.getProcessId());
			
			//if (!OrderStatus.SAVE.equals(beforeChangeStatus))
			//	wrappedMessageBox(msgBox, bean,  false, false);
			
		} catch (FormException fex) {
			msgBox.setMessage("執行入提移轉單存檔時發生錯誤");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("執行入提移轉存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行入提移轉存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);		
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	
    /**
	 * 綑綁回前畫面的視窗
	 *
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, SoDeliveryMoveHead head, boolean isStartProcess, boolean isExecFunction) {
		log.info("wrappedMessageBox...............");
		String orderTypeCode = head.getOrderTypeCode();
		String orderNo = head.getOrderNo();
		String status = head.getStatus();
		String identification = orderTypeCode + "-" + orderNo;

		Command cmd_ok = new Command();
		Command cmd_cancel = new Command();
		//if (isStartProcess) {
		//	log.info("wrappedMessageBox.isStartProcess");
		//	cmd_ok.setCmd(Command.FUNCTION);
		//	cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });

		//	cmd_cancel.setCmd(Command.FUNCTION);
		//	cmd_cancel.setParameters(new String[] { "createRefreshForm()", "" });
			// msgBox.setCancel(cmd_cancel);

		//} else {
		//	log.info("wrappedMessageBox.NoisStartProcess");
		//	if (OrderStatus.CLOSE.equals(status)){
		//		log.info("wrappedMessageBox.123");
		//		msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
		//		cmd_ok.setCmd(Command.FUNCTION);
		//		cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });
		//		cmd_cancel.setCmd(Command.FUNCTION);
		//		cmd_cancel.setParameters(new String[] { "tempRedirect()", "" });
		//	}
		//	else{
		//		log.info("wrappedMessageBox.456");
		//		msgBox.setMessage(identification + "表單存檔成功！");
		//		cmd_ok.setCmd(Command.FUNCTION);
		//		cmd_ok.setParameters(new String[] { "tempRedirect()", "" });
		//	}
		//}
		
		if (OrderStatus.CLOSE.equals(status)){
			log.info("wrappedMessageBox.123");
			msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });
			cmd_cancel.setCmd(Command.FUNCTION);
			cmd_cancel.setParameters(new String[] { "tempRedirect()", "" });
			msgBox.setType(MessageBox.CONFIRM);
			msgBox.setCancel(cmd_cancel);
		}
		else{
			log.info("wrappedMessageBox.456");
			msgBox.setMessage(identification + "表單存檔成功！");
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "tempRedirect()", "" });
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
	private void listErrorMsg(MessageBox msgBox) {
		Command cmd_ok = new Command();
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		msgBox.setType(MessageBox.ALERT);
		cmd_ok.setCmd(Command.FUNCTION);
		cmd_ok.setParameters(new String[] { "showMessage()", "" });

		msgBox.setOk(cmd_ok);
	}

	public List<Properties> saveMoveLine(Map parameterMap) throws Exception {
		log.info("進入Action");
		Map resultMap = new HashMap(0);
		Map saveMap = new HashMap(0);
		Map messageMap = new HashMap(0);
		String message = new String("");
		Long  indexNo = new Long(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String actionType = (String) PropertyUtils.getProperty(otherBean, "actionType");
			log.info("formId:"+formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			if("CREATE".equalsIgnoreCase(actionType))
				soDeliveryMoveService.saveMoveLine(parameterMap, saveMap);
			else
				soDeliveryMoveService.deleteMoveLine(parameterMap, saveMap); 
			message =  (String)saveMap.get("message");
			indexNo = (Long)saveMap.get("indexNo");
			if(StringUtils.hasText(message)){
				messageMap.put("type", "ALERT");
				messageMap.put("message", message);
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
//				resultMap.put("bagCounts1", 0);
//				resultMap.put("bagCounts2", 0);
//				resultMap.put("bagCounts3", 0);
//				resultMap.put("bagCounts4", 0);
//				resultMap.put("bagCounts5", 0);
//				resultMap.put("bagCounts6", 0);
//				resultMap.put("bagCounts7", 0); 
			}
			 List<Object[]> bagCounts = soDeliveryMoveService.getBagCounts(formId);
			 if(bagCounts.size()>0){
				 Object[] dataObject = (Object[]) bagCounts.get(0);
				 resultMap.put("bagCounts1", dataObject[0]);
				 resultMap.put("bagCounts2", dataObject[1]);
				 resultMap.put("bagCounts3", dataObject[2]);
				 resultMap.put("bagCounts4", dataObject[3]);
				 resultMap.put("bagCounts5", dataObject[4]);
				 resultMap.put("bagCounts6", dataObject[5]);
				 resultMap.put("bagCounts7", dataObject[6]);
			 }else{
				 resultMap.put("bagCounts1", 0);
				 resultMap.put("bagCounts2", 0);
				 resultMap.put("bagCounts3", 0);
				 resultMap.put("bagCounts4", 0);
				 resultMap.put("bagCounts5", 0);
				 resultMap.put("bagCounts6", 0);
				 resultMap.put("bagCounts7", 0); 
			 }
			
			resultMap.put("recordCounts",indexNo == null ? 0 :indexNo);
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("新增入提移轉單身時發生錯誤，原因："+ex.getMessage());
		}
	}
	
	/**
	 * 移轉單起流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param imMovement
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, SoDeliveryMoveHead soDeliveryMove, MessageBox msgBox)
			throws Exception {
		log.info("new executeProcess");
		String message = null;
		String identification = MessageStatus.getIdentification(soDeliveryMove.getBrandCode(), soDeliveryMove.getOrderTypeCode(),
				soDeliveryMove.getOrderNo());
		try {
			log.info("===============approvalResult================"+PropertyUtils.getProperty(otherBean, "approvalResult"));
			
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			// String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			// String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String updateForm = (String) PropertyUtils.getProperty(otherBean, "updateForm");
			String formStatus = soDeliveryMove.getStatus();
			String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, formStatus);
			log.info("processId=" + processId + " assignmentId=" + assignmentId + " approvalResult=" + approvalResult
					+ " approvalComment=" + approvalComment + " beforeChangeStatus=" + beforeChangeStatus + " formStatus="
					+ formStatus + " approvalResultMsg=" + approvalResultMsg);
			log.info("ccc--Status:" + soDeliveryMove.getStatus());
			log.info("ccc--processId:" + processId);
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null)
					wrappedMessageBox(msgBox, soDeliveryMove, "Y".equals(updateForm) ? false : true, false);

				Object[] processObj = soDeliveryMoveService.startProcess(soDeliveryMove); // 起流程後，取回流程的processId
				// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
				System.out.println("流程 起始 id ==============================> " + processObj[0]);
				soDeliveryMove.setProcessId((Long) processObj[0]);
			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, soDeliveryMove, false, false);
				
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					Object[] processInfo = soDeliveryMoveService.completeAssignment(assignId, result);
					System.out.println("流程 起始 id >>>>>>> " + processInfo[0]);
					resultMap.put("processId", processInfo[0]);
					resultMap.put("activityId", processInfo[1]);
					resultMap.put("activityName", processInfo[2]);
					resultMap.put("result", approvalResultMsg);
					resultMap.put("lastUpdatedBy", loginEmployeeCode);
					resultMap.put("approvalComment", approvalComment);
					WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId + "、result=" + approvalResult);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			message = "執行移轉流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog("SO_DELIVERY", MessageStatus.LOG_ERROR, identification, message, soDeliveryMove
					.getLastUpdatedBy());
			log.error(message);
		}
	}

	private String getApprovalResult(String processId, String beforeChangeStatus, String formStatus) {
		log.info("getApprovalResult formStatus:" + formStatus);
		String approvalResult = "送出";
		if (StringUtils.hasText(processId)) {
			if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)
					|| OrderStatus.REJECT.equals(formStatus)) {
				approvalResult = OrderStatus.getChineseWord(formStatus);
			} else if (!OrderStatus.REJECT.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)) {
				approvalResult = "核准";
			}
		}
		return approvalResult;
	}
}

