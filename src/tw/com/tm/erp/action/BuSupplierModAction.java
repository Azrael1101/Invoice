package tw.com.tm.erp.action;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuSupplierModService;
import tw.com.tm.erp.hbm.service.CeapProcessService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class BuSupplierModAction {

	private static final Log log = LogFactory.getLog(BuSupplierModAction.class);
	
	private BuSupplierModService buSupplierModService;
	
	
	
	public void setBuSupplierModService(BuSupplierModService buSupplierModService){
		this.buSupplierModService = buSupplierModService;
	}
	
	private CeapProcessService ceapProcessService;
	
	public void setCeapProcessService(CeapProcessService ceapProcessService) {
		this.ceapProcessService = ceapProcessService;
	}
	
	private WfApprovalResultService wfApprovalResultService;
	
	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}
	private SiProgramLogAction siProgramLogAction;
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
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
			log.info("供應商初始化!!");
			returnMap = buSupplierModService.executeInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		} 
		return AjaxUtils.parseReturnDataToJSON(returnMap);	

	}
	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			/*簽核流程
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");
			Long headId = buSupplierModService.getSupplierHeadId(formLinkBean); 
			BuSupplierMod supplierModBean = buSupplierModService.findById(headId);
			Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));
//--------------------------------------------------------------------------------------
			*/
			
			log.info("aaa");
			// 驗證 把畫面的值 存成BEAN 驗證
			buSupplierModService.validateHead(parameterMap);
			log.info("bbb");
			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buSupplierModService.executeFindActual(parameterMap); 
			log.info("ccc");
			// 前端資料塞入bean 
			buSupplierModService.updateSupplierModBean(parameterMap);
			log.info("ddd");
									
			// 存檔  改狀態
			resultMap = buSupplierModService.updateAJAXBuSupplierMod(parameterMap);

			BuSupplierMod buSupplierMod = (BuSupplierMod) resultMap.get("entityBean");
			log.info("CATEGORY08:"+buSupplierMod.getCategory08());
			wrappedMessageBox(msgBox, buSupplierMod, true , false);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


			executeProcess(otherBean, resultMap, buSupplierMod, msgBox);

			// 最後在更新一次 proccessId
			
			//buSupplierModService.updateProcessId(buSupplierMod.getHeadId(), buSupplierMod.getProcessId() == null ? (Long)resultMap.get("processId") : buSupplierMod.getProcessId());
			log.info("processssssssssssssssssssssssssoutout");
		} catch (ProcessFailedException px) {
			log.error("執行維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param 
	 * @param isStartProcess
	 */
	
	private void wrappedMessageBox(MessageBox msgBox,BuSupplierMod buSupplierMod , boolean isStartProcess, boolean isExecFunction){

		log.info("wrappedMessageBox...............");
//		String supplierCode = buSupplierMod.getSupplierCode();
//		String supplierTypeCode = buSupplierMod.getSupplierTypeCode();
//		String status = buSupplierMod.getStatus();
//		String identification = orderTypeCode + "-" + orderNo;
		
		
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
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
	private void executeProcess(Object otherBean, Map resultMap, BuSupplierMod buSupplierMod, MessageBox msgBox)
			throws Exception {
		log.info("執行簽核流程");
		String message = null;
		try {
			
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
		//	Boolean approvalResult = (Boolean) PropertyUtils.getProperty(otherBean, "approvalResult");
	        String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
		    String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeStatus");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String nextStatus = buSupplierMod.getStatus();
			String approvalResultMsg = getApprovalResult(processId, beforeStatus,nextStatus);
			log.info("approvalResult:" + approvalResult);
			if (msgBox != null)
			{
				wrappedMessageBox(msgBox, buSupplierMod,  true, false);
			} 
			log.info("process起動");
			//若無processId 則 startProcess
			if (!StringUtils.hasText(processId) || "0".equals(processId))
			{				
				Long headId = buSupplierMod.getHeadId();
				//---起流程---
				log.info("CATEGORY08:"+buSupplierMod.getCategory08());
				Object[] processObj = BuSupplierModService.startProcess(buSupplierMod); // 起流程後，取回流程的processId
				// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
				System.out.println("------流程資訊顯示------");
				System.out.println("processId ======> " + processObj[0]);
				System.out.println("activityId ======> " + processObj[1]);
				System.out.println("activityName ======> " + processObj[2]);
				System.out.println("------流程資訊結束------");
				//buSupplierMod.setProcessId((Long) processObj[0]);
				log.info("processId="+ buSupplierMod.getProcessId() );
				//有processId 則completeAssignment	
				resultMap.put("processId", processObj[0]);
				resultMap.put("activityId", processObj[1]);
				resultMap.put("activityName", processObj[2]);
				resultMap.put("result", approvalResultMsg);
				resultMap.put("approvalComment", approvalComment);
				BuSupplierMod entityMap = (BuSupplierMod) resultMap.get("entityBean");
				entityMap.setOrderTypeCode("SPM");
				entityMap.setOrderNo(headId.toString());
				//resultMap.put("orderTypeCode","SPM");
				resultMap.put("entityBean",entityMap);
				WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
			}
			else
			{
				if (StringUtils.hasText(assignmentId))
				{
					log.info("assignmentId="+ assignmentId);
					Long headId = buSupplierMod.getHeadId();
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
				//	ceapProcessService.updateProcessSubject(Long.valueOf(processId), buSupplierModService.getProcessSubject(buSupplierMod));
					Object[] processInfo = BuSupplierModService.completeAssignment(assignId, result, buSupplierMod);
		//			System.out.println("流程 起始 id >>>>>>> " + processInfo[0]);
					log.info("assignId=" + assignId);
					resultMap.put("processId", processInfo[0]);
					resultMap.put("activityId", processInfo[1]);
					resultMap.put("activityName", processInfo[2]);
					resultMap.put("result", approvalResultMsg);
				//	resultMap.put("lastUpdatedBy", loginEmployeeCode);
					resultMap.put("approvalComment", approvalComment);
					BuSupplierMod entityMap = (BuSupplierMod) resultMap.get("entityBean");
					entityMap.setOrderTypeCode("SPM");
					entityMap.setOrderNo(headId.toString());
					//resultMap.put("orderTypeCode","SPM");
					resultMap.put("entityBean",entityMap);
					WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				}
				else
				{
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
						+ assignmentId + "、result=" + approvalResult);
				}
			}
		} catch (Exception ex) {
			message = "" +
					"" +
					"，原因：" + ex.toString();
			log.error(message);
			ex.printStackTrace();
		}
	}
	
	private String getApprovalResult(String processId, String beforeStatus,String nextStatus){
		String approvalResult = "送出";
		if(StringUtils.hasText(processId)){	
			if(OrderStatus.SAVE.equals(nextStatus) || OrderStatus.VOID.equals(nextStatus)
					|| OrderStatus.REJECT.equals(nextStatus)){
				approvalResult = OrderStatus.getChineseWord(nextStatus);
			}else if(!OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)){
				approvalResult = "核准";
			}
		}
		return approvalResult;
	}

}
