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
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ShopSet;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.TbcnWfAssignment;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.TbcnWfAssignmentDAO;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
import tw.com.tm.erp.hbm.service.ShopSetService;
import tw.com.tm.erp.hbm.service.BuPurchaseService;
//import tw.com.tm.erp.hbm.service.CompetenceService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;


public class ShopSetAction {

	private static final Log log = LogFactory.getLog(ShopSetAction.class);	
	private ShopSetService shopSetService;
	private WfApprovalResultService wfApprovalResultService;
	private SiProgramLogAction siProgramLogAction;
	//private BuPurchaseService buPurchaseService;

	//private WfApprovalResultService wfApprovalResultService;

	//private BuPurchaseHeadDAO buPurchaseHeadDAO;

	//private TbcnWfAssignmentDAO tbcnWfAssignmentDAO;

	//private BuEmployeeDAO buEmployeeDAO;

	//private CompetenceService competenceService;
	
	public void setShopSetService(ShopSetService shopSetService) {
		this.shopSetService = shopSetService;
	}
	
	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	/*public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}*

	/*public void setTbcnWfAssignmentDAO(TbcnWfAssignmentDAO tbcnWfAssignmentDAO) {
		this.tbcnWfAssignmentDAO = tbcnWfAssignmentDAO;
	}*/

	/*public void setBuPurchaseService(BuPurchaseService buPurchaseService) {
		this.buPurchaseService = buPurchaseService;
	}*/

	/*public void setCompetenceService(CompetenceService competenceService) {
		this.competenceService = competenceService;
	}*/

	/*public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
	}*/
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = shopSetService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行需求單初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
			ex.printStackTrace();
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
	/*
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
	}*/
	/**working been page 
	 * @param parameterMap
	 * @return
	 */
	/*
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
	}*/
	/**
	 * 轉派
	 *
	 * @param parameterMap
	 * @return
	 */
	/*
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
	}*/
	/**
	 * 工作回報
	 *
	 * @param parameterMap
	 * @return
	 */
	/*
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
	}*/
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
			shopSetService.validateHead(parameterMap);    //沒有功能
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			shopSetService.executeFindActualShopSet(parameterMap);  //在此塞入前端單頭值
			// 前端資料塞入bean
			//buPurchaseService.updateBuGoalAchevementBean(parameterMap);   //沒有功能
			// 存檔							
			resultMap = shopSetService.updateAJAXBuGoalAchevement(parameterMap);
			//BuPurchaseHead buPurchaseHead = (BuPurchaseHead) resultMap.get("entityBean");
			ShopSet shopSet = (ShopSet)resultMap.get("entityBean");
			//****wrappedMessageBox(msgBox, shopSet, true, false, otherBean);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			executeProcess(otherBean, resultMap, shopSet, msgBox);

		} catch (ProcessFailedException px) {
			log.error("執行地點維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
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
	/*
	private void wrappedMessageBox(MessageBox msgBox, ShopSet shopSet, boolean isStartProcess, boolean isExecFunction, Object otherBean){

		Long headId = shopSet.getHeadId();

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
	}*/
	
	/**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, ShopSet shopSet, boolean isStartProcess, boolean isExecFunction, Object otherBean )throws Exception{

		String orderTypeCode = shopSet.getOrderTypeCode();
		String orderNo = shopSet.getOrderNo();
		String status = shopSet.getStatus();
		String identification = orderTypeCode + "-" + orderNo;
	
		String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
		String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
		
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
				if(!StringUtils.hasText(userType)){
				    msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");   
				}else{
				    if(ImGeneralAdjustmentService.CREATOR.equalsIgnoreCase(userType)){
						if("ARCHIVE".equalsIgnoreCase(formAction)){
						    msgBox.setMessage(identification + "表單已存檔！");   
						}else{
						    msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！"); 
						}
				    }else{
				    	msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！"); 
				    }
				}
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
     * 執行流程
     * @param otherBean
     * @param resultMap
     * @param imAdjustmentHead
     * @param msgBox
     * @throws Exception
     */
	private void executeProcess(Object otherBean, Map resultMap, ShopSet shopSet, MessageBox msgBox)
    throws Exception {

		String message = null;
		String identification = MessageStatus.getIdentification(
				shopSet.getBrandCode(), shopSet.getOrderTypeCode(), shopSet.getOrderNo());
		try {
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
	//	    String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus = shopSet.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, formStatus);
			
		    log.info( "processId = " + processId );
		    log.info( "assignmentId = " + assignmentId );
		    log.info( "approvalResult = " + approvalResult );
		    log.info( "approvalComment = " + approvalComment );
		    log.info( "formStatus = " + formStatus );
		    log.info( "approvalResultMsg = " + approvalResultMsg );
	
		    if (!StringUtils.hasText(processId) || "0".equals(processId)) {
		    	if (msgBox != null){
		    		wrappedMessageBox(msgBox, shopSet, true, false, otherBean);
		    	}
		    	Object processObj[] = shopSetService.startProcess(shopSet,resultMap);
		    	shopSet.setProcessId((Long)processObj[0]);
		    } else {
				if (msgBox != null){
					wrappedMessageBox(msgBox, shopSet, false, false, otherBean);
				}
				if (StringUtils.hasText(assignmentId)) {
				    Long assignId = NumberUtils.getLong(assignmentId);
				    Boolean result = Boolean.valueOf(approvalResult);
				    Object[] processInfo = ImGeneralAdjustmentService.completeAssignment(assignId, result,formAction);
					    if (!OrderStatus.SAVE.equals(formStatus) ) {
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				    }
				} else {
				    throw new ProcessFailedException(
					    "Complete assignment失敗，ProcessId=" + processId
					    + "、AssignmentId=" + assignmentId
					    + "、result=" + approvalResult);
				}
	
		    }
		} catch (Exception ex) {
			message = "執行調整單流程發生錯誤，原因：" + ex.toString();
		    siProgramLogAction.createProgramLog(ShopSetService.PROGRAM_ID,MessageStatus.LOG_ERROR, identification, message,shopSet.getLastUpdatedBy());
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
	/*
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
	}*/
	/*
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
	}*/

}





