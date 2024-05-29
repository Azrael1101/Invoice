package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.TbcnWfAssignment;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.TbcnWfAssignmentDAO;
import tw.com.tm.erp.hbm.service.BuPurchaseService;
//import tw.com.tm.erp.hbm.service.CompetenceService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class BuPurchaseAction {

	private static final Log log = LogFactory.getLog(BuPurchaseAction.class);	

	private BuPurchaseService buPurchaseService;

	private WfApprovalResultService wfApprovalResultService;

	private BuPurchaseHeadDAO buPurchaseHeadDAO;

	private TbcnWfAssignmentDAO tbcnWfAssignmentDAO;

	private BuEmployeeDAO buEmployeeDAO;

	//private CompetenceService competenceService;

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setTbcnWfAssignmentDAO(TbcnWfAssignmentDAO tbcnWfAssignmentDAO) {
		this.tbcnWfAssignmentDAO = tbcnWfAssignmentDAO;
	}

	public void setBuPurchaseService(BuPurchaseService buPurchaseService) {
		this.buPurchaseService = buPurchaseService;
	}

	/*public void setCompetenceService(CompetenceService competenceService) {
		this.competenceService = competenceService;
	}*/

	public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
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
			returnMap = buPurchaseService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行地點維護存檔初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	/**準備資料給 search page 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performSearchInitial(Map parameterMap){
		log.info("performSearchInitial");
		Map returnMap = null;	
		try{
			returnMap = buPurchaseService.executeSearchInitial(parameterMap);


		}catch (Exception ex) {
			System.out.println("執行採購單查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	/**working been page 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performInitialwork(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = buPurchaseService.executeInitialworking(parameterMap);
		} catch (Exception ex) {
			log.error("執行工作回報單進階輸入初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	/**
	 * 轉派
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> assignTask(Map parameterMap) {
		Object otherBean = parameterMap.get("vatBeanOther");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");

		try {
			String rqInChargeCode    =  (String) PropertyUtils.getProperty(otherBean, "rqInChargeCode");
			returnMap = buPurchaseService.updateTask(parameterMap);
			BuPurchaseHead buPurchaseHead = (BuPurchaseHead) buPurchaseService.executeFindActualBuGoalAchevement(parameterMap);  //在此塞入前端單頭值
			log.info("2.performTransaction:::"+buPurchaseHead);
			if(null !=((String)PropertyUtils.getProperty(otherBean, "processId"))){
			    log.info("3.process_null:::");
				//Integer a = (Integer) PropertyUtils.getProperty(otherBean, "processId");
				Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
				//Long procId = Long.valueOf(a);
				log.info("3-1.process_:::"+processId);
				BuEmployee emp = buEmployeeDAO.findById(rqInChargeCode);
				String loginName = emp.getLoginName();
				log.info("3-2.process_:::"+emp);
				//assig
				TbcnWfAssignment tbcnWfAssignment = tbcnWfAssignmentDAO.findAssignmentId(processId);
				log.info("3-3.process:::"+tbcnWfAssignment+"__"+processId);
				if (tbcnWfAssignment!=null){
				    log.info("4.tbcnWfAssignment!=null");
					if("ACCEPTED".equals(tbcnWfAssignment.getCurrentStatus())){
					    log.info("5.ACCEPTED");
						Long assiId = tbcnWfAssignment.getAssignmentId();
						buPurchaseService.reAssignment(assiId, processId, loginName , buPurchaseHead);
						msgBox.setMessage("轉派成功");
					}
					else
					{
						msgBox.setMessage("此流程狀態不正確");
					}
				}
				else
				{
					msgBox.setMessage("查無此流程");
				}
			}
			else
			{
				msgBox.setMessage("轉派成功,但未起流程");
			}

			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.WIN_CLOSE);
			msgBox.setOk(cmd_ok);
			msgBox.setType(MessageBox.ALERT);
		} catch (Exception ex) {
			System.out.println("轉派失敗，原因：" + ex.toString());
			log.error("轉派失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	/**
	 * 工作回報
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransactionAdvance(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			returnMap = buPurchaseService.updateAJAXworkingcondition(parameterMap);
			msgBox.setMessage("明細存檔成功!");
		} catch (Exception ex) {
			log.error("執行工作回報單明細時存檔失敗");
			msgBox.setMessage("執行工作回報單明細時存檔失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ 
		log.info("performTransaction");
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			buPurchaseService.validateHead(parameterMap);    //沒有功能
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buPurchaseService.executeFindActualBuPurchase(parameterMap);  //在此塞入前端單頭值
			// 前端資料塞入bean
			buPurchaseService.updateBuGoalAchevementBean(parameterMap);   //沒有功能
			// 存檔							
			resultMap = buPurchaseService.updateAJAXBuGoalAchevement(parameterMap);
			BuPurchaseHead buPurchaseHead = (BuPurchaseHead) resultMap.get("entityBean");
			wrappedMessageBox(msgBox, buPurchaseHead, true,false);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			if(!buPurchaseHead.getOrderTypeCode().equals("LOA")&!buPurchaseHead.getOrderTypeCode().equals("WOA")){
				executeProcess(otherBean, resultMap, buPurchaseHead, msgBox);
			}
			/*if(buPurchaseHead.getOrderTypeCode()=="LOA")
			{
			Long headId = buPurchaseHead.getHeadId();
			competenceService.updateCompetenceData(headId);
			throw new Exception("updateCompetenceData 存檔失敗");
			}*/
		} catch (ProcessFailedException px) {
			log.error("執行地點維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行地點維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	/**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param locationBean
	 * @param isStartProcess
	 * @param isExecFunction 
	 */
	private void wrappedMessageBox(MessageBox msgBox, BuPurchaseHead buPurchaseHead, boolean isStartProcess, boolean isExecFunction){

		Long headId = buPurchaseHead.getHeadId();

		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(headId + "表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}
		msgBox.setOk(cmd_ok);
	}
	/**
	 * 起流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param suppliermod
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, BuPurchaseHead buPurchaseHead, MessageBox msgBox)
	throws Exception {
		log.info("new executeProcess");
		String message = null;
		try {
			//Long proId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			String processId         =  (String) PropertyUtils.getProperty(otherBean,"processId");
			//String processId = "3280235";
			String domain            = (String) PropertyUtils.getProperty(otherBean, "domain");
			String assignmentId      = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			//String assignmentId    ="3273845";
			String approvalResult    = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment   = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeStatus      = (String) PropertyUtils.getProperty(otherBean, "beforeStatus");
			String status            = (String) PropertyUtils.getProperty(otherBean, "status");
			String nextStatus        = (String) PropertyUtils.getProperty(otherBean, "nextStatus");
			String orderTypeCode     = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String versionType;
			String processType;
			//String versionIRQ = "20140310";
			//String versionPRC = "20140916";
			//String processIRQ = "process";
			//String processPRC = "processPRC";
			//String nextStatus = buPurchaseHead.getStatus();
			String approvalResultMsgIRQ = getApprovalResultIRQ(processId, beforeStatus,nextStatus);
			String approvalResultMsgPRC = getApprovalResultPRC(processId, beforeStatus,nextStatus);
			log.info("approvalResult=====" + approvalResult);
			log.info("process起動");
			log.info("status~~~"+status);
			log.info("assignmentId~~~"+assignmentId);
			//if("REPLAN".equals(status))
			//{
			//若無processId 則 startProcess
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null){
					wrappedMessageBox(msgBox, buPurchaseHead,  true, false);
				}                                          //---起流程---
				if(StringUtils.hasText(orderTypeCode))
				{
					if(orderTypeCode.equalsIgnoreCase("IRQ"))
					{
						versionType = "20140310";
						processType = "process";
					}
					else
					{
						versionType = "20140916";
						processType = "processPRC";
					}
					Object[] processObj = BuPurchaseService.startProcess(buPurchaseHead,versionType,processType); // 起流程後，取回流程的processId
					//起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
					System.out.println("流程 起始 id ======> " + processObj[0]);
					buPurchaseHead.setProcessId((Long)processObj[0]);
					buPurchaseHeadDAO.update(buPurchaseHead);
				}
				//有processId 則completeAssignment	
			} else {
				if (msgBox != null){
					wrappedMessageBox(msgBox, buPurchaseHead, false ,false);
				}
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					//ceapProcessService.updateProcessSubject(Long.valueOf(processId), buSupplierModService.getProcessSubject(buSupplierMod));
					log.info("login....assignmentIdSTART" );
					Object[] processInfo = BuPurchaseService.completeAssignment(assignId, result, buPurchaseHead);
					//System.out.println("流程 起始 id >>>>>>> " + processInfo[0]);
					log.info("assignId=" + assignId);
					buPurchaseHead.setAssignmentId(assignId);
					buPurchaseHeadDAO.update(buPurchaseHead);
					resultMap.put("processId", processInfo[0]);
					resultMap.put("activityId", processInfo[1]);
					resultMap.put("activityName", processInfo[2]);
					if(orderTypeCode.equalsIgnoreCase("IRQ"))
					{
						resultMap.put("result", approvalResultMsgIRQ);
					}else{
						resultMap.put("result", approvalResultMsgPRC);	
					}
					//	resultMap.put("lastUpdatedBy", loginEmployeeCode);
					resultMap.put("approvalComment", approvalComment);
					WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId + "、result=" + approvalResult);
				}
			}

			/*}else{
				if (StringUtils.hasText(assignmentId)) {
				log.info("~~~~~重新轉派~~~~~~");
				Long assignId = NumberUtils.getLong(assignmentId);
				log.info("assignId~~~"+assignId);
				log.info("~~~~~~~~~~~~~~~~~~~~");
				Object[] processInfo = BuPurchaseService.reAssignment(assignId,processId ,domain, buPurchaseHead);
				log.info("====================");
				resultMap.put("processId", processInfo[0]);
				resultMap.put("activityId", processInfo[1]);
				resultMap.put("activityName", processInfo[2]);
				resultMap.put("result", approvalResultMsgIRQ);
				//resultMap.put("lastUpdatedBy", loginEmployeeCode);
				resultMap.put("approvalComment", approvalComment);
				WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				}
			}*/
		} catch (Exception ex) {
			message = "" +
			"" +
			"，原因：" + ex.toString();
			log.error(message);
		}
	}
	private String getApprovalResultIRQ(String processId, String beforeStatus, String nextStatus){
		String approvalResult = "送出";
		if(StringUtils.hasText(processId)){	
			if(OrderStatus.SAVE.equals(nextStatus) || OrderStatus.VOID.equals(nextStatus)
					|| OrderStatus.REJECT.equals(nextStatus)|| OrderStatus.SUSPEND.equals(nextStatus)|| OrderStatus.REPLAN.equals(nextStatus)){
				approvalResult = OrderStatus.getChineseWord(nextStatus);
			}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.PLAN.equals(nextStatus)){
				approvalResult = "核准";
			}
		}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.PLAN.equals(nextStatus)){
			approvalResult = "核准";
		}
		return approvalResult;
	}
	private String getApprovalResultPRC(String processId, String beforeStatus, String nextStatus){
		String approvalResult = "送出";
		if(StringUtils.hasText(processId)){	
			if(OrderStatus.SAVE.equals(nextStatus) || OrderStatus.VOID.equals(nextStatus)
					|| OrderStatus.REJECT.equals(nextStatus)){
				approvalResult = OrderStatus.getChineseWord(nextStatus);
			}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)){
				approvalResult = "核准";
			}
		}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)){
			approvalResult = "核准";
		}
		return approvalResult;
	}

}





