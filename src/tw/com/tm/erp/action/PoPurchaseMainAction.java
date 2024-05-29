package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Date; 

import org.apache.commons.beanutils.PropertyUtils; 
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 
import org.springframework.util.StringUtils; 

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus; 
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ProcessFailedException; 
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.CeapProcessService;
import tw.com.tm.erp.hbm.service.ImPromotionService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadMainService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;

public class PoPurchaseMainAction {

	private static final Log log = LogFactory.getLog(PoPurchaseMainAction.class);

	private PoPurchaseOrderHeadMainService	poPurchaseOrderHeadMainService;
	private WfApprovalResultService			wfApprovalResultService;
	private SiProgramLogAction				siProgramLogAction;
	private PoPurchaseOrderHeadDAO			poPurchaseOrderHeadDAO;
	private BuBrandService					buBrandService;
	private CeapProcessService				ceapProcessService;

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}

	public void setPoPurchaseOrderHeadMainService(PoPurchaseOrderHeadMainService poPurchaseOrderHeadMainService) {
		this.poPurchaseOrderHeadMainService = poPurchaseOrderHeadMainService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setCeapProcessService(CeapProcessService ceapProcessService) {
		this.ceapProcessService = ceapProcessService;
	}
	
	public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean    = parameterMap.get("vatBeanOther");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");

			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");
			
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Long headId = poPurchaseOrderHeadMainService.getPoPurchaseHeadId(formLinkBean); 
			PoPurchaseOrderHead poPurchaseBean = poPurchaseOrderHeadMainService.findById(headId);
			Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));

			//確認是否允許流程，不允許會丟例外
			if(! (beforeChangeStatus.equals(OrderStatus.FINISH) && formStatus.equals(OrderStatus.CLOSE)) )
				ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(poPurchaseBean.getProcessId()), processId, assignmentId);

			if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
				// update Head Data from form to bean
				poPurchaseOrderHeadMainService.updatePoPurchaseWithActualOrderNO(parameterMap, "FG" );
				// check PO head & line data
				List errorMsgs = poPurchaseOrderHeadMainService.updateCheckedPurchaseOrderData(parameterMap);
				if(errorMsgs.size() > 0){
					throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
			}

			Map resultMap = poPurchaseOrderHeadMainService.updateAJAXPoPurchase(parameterMap);
			poPurchaseBean = (PoPurchaseOrderHead)resultMap.get("entityBean");
			msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			if(OrderStatus.CLOSE.equals(poPurchaseBean.getStatus())){

				wrappedMessageBox(msgBox, poPurchaseBean, false, false);
			}

			else
				executeProcess(otherBean, resultMap, poPurchaseBean, msgBox);

			//最後再度更新一次
			if(processId == 0)
				poPurchaseOrderHeadDAO.update(poPurchaseBean);
		}catch (FormException ex) {
			log.error("執行採購單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage("執行採購單存檔失敗");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"showMessage()", ""});
			msgBox.setOk(cmd_bf);
		}catch (Exception ex) {
			log.error("執行採購單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	/**
	 * 取得單號
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> getOrderNo(Map parameterMap){
		log.info("getOrderNo");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean    = parameterMap.get("vatBeanOther");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			
			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");
			
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Long headId = poPurchaseOrderHeadMainService.getPoPurchaseHeadId(formLinkBean); 
			PoPurchaseOrderHead poPurchaseBean = poPurchaseOrderHeadMainService.findById(headId);
			Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));

			//確認是否允許流程，不允許會丟例外
			ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(poPurchaseBean.getProcessId()), processId, assignmentId);
			
			Map resultMap = poPurchaseOrderHeadMainService.updatePoPurchaseWithActualOrderNO(parameterMap, "BG");
			poPurchaseBean = (PoPurchaseOrderHead)resultMap.get("entityBean");
			msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			if(processId == 0){
				wrappedMessageBox(msgBox, poPurchaseBean, true, true);
			}else{
				wrappedMessageBox(msgBox, poPurchaseBean, false, true);
			}
		}catch (Exception ex) {
			log.error("執行採購單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		} 
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = poPurchaseOrderHeadMainService.executeInitial(parameterMap);
		}catch (Exception ex) {
			System.out.println("執行採購單初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}


	public void performReverse(Long headId){
		try{
			poPurchaseOrderHeadMainService.executeReverse(headId);
		}catch (Exception ex) {
			log.error("執行採購單反轉時發生錯誤，原因：");
		}
	}

	public List<Properties> performTransactionForBackGround(Map parameterMap){
		log.info("performTransactionForBackGround");
		Map returnMap = new HashMap(0);	
		PoPurchaseOrderHead poPurchase = null;
		Map resultMap = new HashMap(0);
		String message = null;
		String identification = null;
		try{
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");
			Long   headId       = poPurchaseOrderHeadMainService.getPoPurchaseHeadId(formLinkBean);
			try{      	
				poPurchase = poPurchaseOrderHeadMainService.findById(headId);
				resultMap.put("entityBean", poPurchase);
				List errorMsgs = poPurchaseOrderHeadMainService.updateCheckedPurchaseOrderData(parameterMap);
				if(errorMsgs.size() == 0){
					resultMap  = poPurchaseOrderHeadMainService.updateAJAXPoPurchase(parameterMap);
					poPurchase = (PoPurchaseOrderHead)resultMap.get("entityBean");
				}
			}catch(Exception ex){
				if(poPurchase != null){
					log.info(ex.getMessage());
					identification = 
						MessageStatus.getIdentification(poPurchase.getBrandCode(), poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
					message = ex.getMessage();
					siProgramLogAction.createProgramLog(PoPurchaseOrderHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
							identification, message, poPurchase.getLastUpdatedBy());
				}
			}

			//==============execute flow==================
			if(poPurchase != null )
				executeProcess(otherBean, resultMap, poPurchase, null);
			//最後再度更新一次
			poPurchaseOrderHeadDAO.update(poPurchase);
		}catch (Exception ex) {
			message = "執行採購單背景存檔失敗，原因：" + ex.toString();
			log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	private void executeProcess(Object otherBean, Map resultMap, PoPurchaseOrderHead poPurchase, MessageBox msgBox) throws Exception {
		String message = null;
		String identification = MessageStatus.getIdentification(poPurchase.getBrandCode(), 
				poPurchase.getOrderTypeCode(), poPurchase.getOrderNo());
		try{
			String processId          = (String)PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId       = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
			String approvalResult     = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment    = (String)PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus         = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String approvalResultMsg  = getApprovalResult(processId, beforeChangeStatus, formStatus);

			if(!StringUtils.hasText(processId) || "0".equals(processId)){
				if(msgBox != null)
					wrappedMessageBox(msgBox, poPurchase, true, false);

				BuBrand buBrand  = buBrandService.findById( poPurchase.getBrandCode() );	// 取是否為 T2, Run 不同 flow 
				Object processObj[] = PoPurchaseOrderHeadMainService.startProcess(poPurchase, buBrand.getBranchCode());
				poPurchase.setProcessId((Long)processObj[0]);
			}else{
				if(msgBox != null)
					wrappedMessageBox(msgBox, poPurchase, false, false);

				if(StringUtils.hasText(assignmentId)){
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					Object[] processInfo = PoPurchaseOrderHeadMainService.completeAssignment(assignId, result,poPurchase);
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
			ceapProcessService.updateProcessSubject(poPurchase.getProcessId(), poPurchaseOrderHeadMainService.getProcessSubject(poPurchase));
		}catch(Exception ex){
			message = "執行採購流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(PoPurchaseOrderHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, poPurchase.getLastUpdatedBy());
			log.error(message);
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


	private void wrappedMessageBox(MessageBox msgBox, PoPurchaseOrderHead poPurchaseBean, boolean isStartProcess, boolean isExecFunction){
		String orderTypeCode = poPurchaseBean.getOrderTypeCode();
		String orderNo = poPurchaseBean.getOrderNo();
		String status = poPurchaseBean.getStatus();
		Long headId = poPurchaseBean.getHeadId();
		String identification = orderTypeCode + "-" + orderNo;
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()",""});
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


	/**準備資料給 search page 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performSearchInitial(Map parameterMap){
		log.info("performSearchInitial");
		Map returnMap = null;	
		try{
			returnMap = poPurchaseOrderHeadMainService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			System.out.println("執行採購單查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	public List<Properties> performSearchSelection(Map parameterMap){
		log.info("performSearchSelection");
		Map returnMap = null;	
		try{
			returnMap = poPurchaseOrderHeadMainService.getSearchSelection(parameterMap);	    
		}catch (Exception ex) {
			log.info("執行採購單檢視時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	//-----by jason
	public List<Properties> performTransactionClose(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean    = parameterMap.get("vatBeanOther");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String orderNo	= (String)PropertyUtils.getProperty(otherBean, "orderNo");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			List<PoPurchaseOrderHead> headIds = poPurchaseOrderHeadDAO.findAllNoCloseHead(beforeChangeStatus,brandCode,orderNo,orderTypeCode);
			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");
			PoPurchaseOrderHead poPurchaseBean = headIds.get(0);
			Long headId = poPurchaseBean.getHeadId();
			Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));
			Long processId = poPurchaseBean.getProcessId();

			Map resultMap = poPurchaseOrderHeadMainService.updateAJAXPoPurchaseClose(parameterMap,headId);
			poPurchaseBean = (PoPurchaseOrderHead)resultMap.get("entityBean");
			msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			
			if(OrderStatus.CLOSE.equals(poPurchaseBean.getStatus())){
				
				String totalReturnedAmount = poPurchaseOrderHeadMainService.updateAJAXHeadTotalAmount(headId);
				String status = poPurchaseBean.getStatus();
				String identification = orderTypeCode + "-" + orderNo;
				msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！"
								+"\n"+"退回預算:"+totalReturnedAmount);

			}
			else
				executeProcess(otherBean, resultMap, poPurchaseBean, msgBox);

			//最後再度更新一次
			if(processId == 0)
				poPurchaseOrderHeadDAO.update(poPurchaseBean);
		}catch (FormException ex) {
			log.error("執行採購單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage("執行採購單存檔失敗");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"showMessage()", ""});
			msgBox.setOk(cmd_bf);
		}catch (Exception ex) {
			log.error("執行採購單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

//-----


}
