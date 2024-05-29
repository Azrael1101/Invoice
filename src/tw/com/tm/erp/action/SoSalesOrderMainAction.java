package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.service.ImPromotionService;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.PosImportDataService;
import tw.com.tm.erp.hbm.service.SiResendService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.hbm.service.TmpImportPosItemService;
import tw.com.tm.erp.hbm.service.TmpImportPosPaymentService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class SoSalesOrderMainAction {

	private static final Log log = LogFactory.getLog(SoSalesOrderAction.class);

	private static final String CUSTOMER_PO_NO_PROCESS_NAME = "UPDATE_CUSTOMER_PO_NO";

	private WfApprovalResultService wfApprovalResultService;

	private SiProgramLogAction siProgramLogAction;
	
	private SiResendService siResendService;

	private SoSalesOrderMainService soSalesOrderMainService;
	//for 儲位用
	private ImStorageAction imStorageAction;
	
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	
	private SiProgramLogDAO siProgramLogDAO;
	
	public static final String STATUS = "UNCONFIRMED";

	public void setWfApprovalResultService(
			WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setSoSalesOrderMainService(
			SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}
	
	//for 儲位用
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
	
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
	
	public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
		this.siProgramLogDAO = siProgramLogDAO;
	}
	
	public void setSiResendService(SiResendService siResendService) {
		this.siResendService = siResendService;
	}
	
	public List<Properties> performInitialAdvance(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = soSalesOrderMainService.executeInitialAdvance(parameterMap);
		} catch (Exception ex) {
			log.error("執行銷貨單進階輸入初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = soSalesOrderMainService.executeInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行銷貨單初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performCustomerPoNoInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = soSalesOrderMainService.executeCustomerPoNoInitial(parameterMap);
		} catch (Exception ex) {
			log.error("執行銷貨售貨單初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage", msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performTransaction(Map parameterMap) {
		log.info("===enter performTransaction===");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			// ======check beforeChangeStatus、formAction========
			if (!StringUtils.hasText(beforeChangeStatus)) {
				throw new Exception("beforeChangeStatus參數為空值，無法執行存檔！");
			} else if (!StringUtils.hasText(formAction)) {
				throw new Exception("formAction參數為空值，無法執行存檔！");
			}
			// =====================================================

			Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
			SoSalesOrderHead soSalesOrderHead = soSalesOrderMainService.findById(headId);
			// 流程控制，避免重複流程造成的錯誤
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
			log.info("headId = " + PropertyUtils.getProperty(formLinkBean, "headId"));
			log.info("processId = " + processId);
			log.info("salesOrderHead.getProcessId() = " + soSalesOrderHead.getProcessId());
			log.info("assignmentId = " + PropertyUtils.getProperty(otherBean, "assignmentId"));// 由工作清單來，由ceap來的
			// 確認是否允許流程，不允許會丟例外
			ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(soSalesOrderHead.getProcessId()), processId, assignmentId);

			Map resultMap = null;
			if (OrderStatus.UNCONFIRMED.equals(formAction)) {// 執行反確認
				resultMap = soSalesOrderMainService.executeAJAXAntiConfirm(parameterMap);
			} else if (!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formAction)) {
				soSalesOrderMainService.updateSalesOrder(parameterMap);// 更新銷貨單主檔
				List errorMsgs = soSalesOrderMainService.updateCheckedSalesOrderData(parameterMap);// 更新檢核後的銷貨單資料
				if (errorMsgs.size() > 0)
					throw new FormException(MessageStatus.VALIDATION_FAILURE);

				soSalesOrderHead = soSalesOrderMainService.findById(headId);

				//for 儲位用
				if(imStorageAction.isStorageExecute(soSalesOrderHead)){
					//取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
					executeStorage(soSalesOrderHead);
				}
				log.info("ccccccccccccccccc");
				resultMap = soSalesOrderMainService.updateAJAXSOToDelivery(parameterMap, errorMsgs);
			} else {
				resultMap = soSalesOrderMainService.updateAJAXSalesOrder(parameterMap);
			}

			soSalesOrderHead = (SoSalesOrderHead) resultMap.get("entityBean");
			msgBox.setMessage((String) resultMap.get("resultMsg") + "是否繼續新增？");
			if ("SOP".equals(soSalesOrderHead.getOrderTypeCode())) {
				// 設置messageBox內容
				wrappedMessageBox(msgBox, soSalesOrderHead, true, false);
			} else {
				executeProcess(otherBean, resultMap, soSalesOrderHead, msgBox);
				soSalesOrderMainService.update(soSalesOrderHead);
				log.info("after executeProcess---salesOrderHeadBean.getProcessId() = "+ soSalesOrderHead.getProcessId());
			}
		} catch (StorageException sex) {
			msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
			msgBox.setType(MessageBox.CONFIRM);
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
			msgBox.setOk(cmd_bf);
			//msgBox.setCancel(cmd_cancel);
		} catch (FormException e) {
			log.error("執行銷貨單存檔失敗");
			msgBox.setMessage("執行銷貨單存檔失敗");
			listErrorMessageBox(msgBox);
		} catch (Exception ex) {
			log.error("執行銷貨單時存檔失敗");
			msgBox.setMessage("執行銷貨單時存檔失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		log.info("===leave performTransaction===");
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performTransactionAdvance(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			returnMap = soSalesOrderMainService.updateAJAXSalesOrderItem(parameterMap);
			msgBox.setMessage("明細存檔成功!");
		} catch (Exception ex) {
			log.error("執行銷貨單明細時存檔失敗");
			msgBox.setMessage("執行銷貨單明細時存檔失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 更新客戶售貨單
	 * 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performCustomerPoNoUpdate(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		String uuid = UUID.randomUUID().toString();
		Date executeDate = new Date();
		try {
			parameterMap.put("UUID", uuid);
			parameterMap.put("PROCESS_NAME", CUSTOMER_PO_NO_PROCESS_NAME);
			parameterMap.put("EXECUTE_DATE", executeDate);
			returnMap = soSalesOrderMainService.updateCustomerPoNo(parameterMap);
			msgBox.setMessage((String) returnMap.get("resultMsg"));
		} catch (Exception ex) {
			log.error("執行銷貨單售貨單更新存檔失敗，原因" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
			SiSystemLogUtils.createSystemLog(CUSTOMER_PO_NO_PROCESS_NAME,
					MessageStatus.LOG_ERROR, ex.getMessage(), executeDate,
					uuid, "SYS");
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
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
			Map resultMap = soSalesOrderMainService
					.updateSalesOrderWithActualOrderNO(parameterMap);
			SoSalesOrderHead salesOrderHeadBean = (SoSalesOrderHead) resultMap.get("entityBean");
			msgBox.setMessage((String) resultMap.get("resultMsg") + "是否繼續新增？");
			Object otherBean = parameterMap.get("vatBeanOther");
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, salesOrderHeadBean, true, true);
			} else {
				wrappedMessageBox(msgBox, salesOrderHeadBean, false, true);
			}
		} catch (Exception ex) {
			log.info("執行促銷單存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行促銷單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performTransactionForBackGround(Map parameterMap) {
		Map returnMap = new HashMap(0);
		SoSalesOrderHead salesOrderHeadBean = null;
		Map resultMap = new HashMap(0);
		String message = null;
		String identification = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
			salesOrderHeadBean = soSalesOrderMainService.findById(headId);
			resultMap.put("entityBean", salesOrderHeadBean);
			try {
				List errorMsgs = soSalesOrderMainService.updateCheckedSalesOrderData(parameterMap);
				resultMap = soSalesOrderMainService.updateAJAXSOToDelivery(parameterMap, errorMsgs);
				if (errorMsgs.size() == 0) {
					salesOrderHeadBean = (SoSalesOrderHead) resultMap.get("entityBean");
				}
			} catch (FormException e) {

			} catch (Exception e) {
				if (salesOrderHeadBean != null) {
					identification = MessageStatus.getIdentification(salesOrderHeadBean.getBrandCode(),
							salesOrderHeadBean.getOrderTypeCode(),salesOrderHeadBean.getOrderNo());
					message = e.getMessage();
					siProgramLogAction.createProgramLog(SoSalesOrderService.PROGRAM_ID,
							MessageStatus.LOG_ERROR, identification, message,
							salesOrderHeadBean.getLastUpdatedBy());
				}
			}
			// ==============execute flow==================
			if (salesOrderHeadBean != null
					&& !"SOP".equals(salesOrderHeadBean.getOrderTypeCode())) {
				executeProcess(otherBean, resultMap, salesOrderHeadBean, null);
				soSalesOrderMainService.update(salesOrderHeadBean);
			}

		} catch (Exception ex) {
			message = "執行銷貨單背景存檔失敗，原因：" + ex.toString();
			log.info(message);
			log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	private void executeProcess(Object otherBean, Map resultMap,
			SoSalesOrderHead salesOrderHeadBean, MessageBox msgBox)
					throws Exception {
		String message = null;
		String identification = MessageStatus.getIdentification(
				salesOrderHeadBean.getBrandCode(), salesOrderHeadBean
				.getOrderTypeCode(), salesOrderHeadBean.getOrderNo());
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, formAction);

			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null)
					wrappedMessageBox(msgBox, salesOrderHeadBean, true, false);
					Object processObj[] = null;
				if ("T2".equals(salesOrderHeadBean.getBrandCode()) || "T6CO".equals(salesOrderHeadBean.getBrandCode())) {
					processObj = SoSalesOrderMainService.startProcessT2(salesOrderHeadBean);
				} else {
					processObj = SoSalesOrderMainService.startProcess(salesOrderHeadBean);
				}
				salesOrderHeadBean.setProcessId((Long) processObj[0]);
			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, salesOrderHeadBean, false, false);
				if (!OrderStatus.SAVE.equals(formAction)
						|| (OrderStatus.REJECT.equals(beforeChangeStatus) && OrderStatus.SAVE.equals(formAction))) {
					if (StringUtils.hasText(assignmentId)) {
						Long assignId = NumberUtils.getLong(assignmentId);
						Boolean result = Boolean.valueOf(approvalResult);
						Object[] processInfo = SoSalesOrderService.completeAssignment(assignId, result);
						log.error("SoSalesOrderService.completeAssignment,0,1,2 = "
								+ processInfo[0] + "," + processInfo[1] + "," + processInfo[2]);
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
					} else {
						throw new ProcessFailedException(
								"Complete assignment失敗，ProcessId=" + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
					}
				}
			}
		} catch (Exception ex) {
			message = "執行銷貨流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(ImPromotionService.PROGRAM_ID,
					MessageStatus.LOG_ERROR, identification, message,salesOrderHeadBean.getLastUpdatedBy());
			log.error(message);
		}
	}

	private String getApprovalResult(String processId, String beforeChangeStatus, String formAction) {
		String approvalResult = "送出";
		if (StringUtils.hasText(processId)) {
			if (OrderStatus.SAVE.equals(formAction)
					|| OrderStatus.VOID.equals(formAction)
					|| OrderStatus.REJECT.equals(formAction)) {
				approvalResult = OrderStatus.getChineseWord(formAction);
			} else if (!OrderStatus.REJECT.equals(beforeChangeStatus)
					&& OrderStatus.SIGNING.equals(formAction)) {
				approvalResult = "核准";
			}
		}
		return approvalResult;
	}

	private void wrappedMessageBox(MessageBox msgBox, SoSalesOrderHead salesOrderHead, boolean isStartProcess, boolean isExecFunction) {
		String brandCode = salesOrderHead.getBrandCode();
		String orderTypeCode = salesOrderHead.getOrderTypeCode();
		String orderNo = salesOrderHead.getOrderNo();
		String status = salesOrderHead.getStatus();
		String identification = orderTypeCode + "-" + orderNo;
		
		if ("T2".equals(brandCode) & "SOE".equals(orderTypeCode)){
			if (isExecFunction) {
				msgBox.setMessage(identification + "表單已送出，是否列印報表?");
			} else {
				msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
			}
		}
		
		Command cmd_ok = new Command();
		if (isStartProcess) {
			
			msgBox.setType(MessageBox.CONFIRM);
			Command cmd_cancel = new Command();
			
			if ("T2".equals(brandCode) & "SOE".equals(orderTypeCode)){
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "openReportWindow()", "" });
			}
			else{
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "createNewForm()", "" });
			}
			
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		} else {
			if ("T2".equals(brandCode) & "SOE".equals(orderTypeCode)){
				msgBox.setType(MessageBox.CONFIRM);
				
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "openReportWindow()", "" });
				
				Command cmd_cancel = new Command();
				cmd_cancel.setCmd(Command.WIN_CLOSE);
				msgBox.setCancel(cmd_cancel);
			}
			else{
				if (OrderStatus.SIGNING.equals(status) || isExecFunction) {
					msgBox.setMessage(identification + "表單已送出！");
				} else {
					msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
				}
				cmd_ok.setCmd(Command.WIN_CLOSE);
			}
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
	 * 送出錯誤後，呼叫的function
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	private void listErrorMessageBox(MessageBox msgBox) {
		// 如果為背景送出
		Command cmd_error = new Command();
		cmd_error.setCmd(Command.FUNCTION);
		cmd_error.setParameters(new String[] { "showMessage()", "" });
		msgBox.setOk(cmd_error);
	}

	// 整批檔案轉檔 轉入ERP系統
	public String tableTransfer(Long requestID) throws Exception {
		Map posCommandMap = soSalesOrderMainService.qryPOSCommand(requestID);
		String brandCode = (String) posCommandMap.get("brandCode");
		String dataType = (String) posCommandMap.get("dataType");
		List returnBeans = soSalesOrderMainService.crtSoHeadBean(posCommandMap);
		HashMap tmpM = (HashMap)returnBeans.get(1);
		String posMachineCode = (String) tmpM.get("posMachineCode");
		Date actualSalesDate = (Date) tmpM.get("actualSalesDate");
		HashMap parameterMap = new HashMap();
		parameterMap.put("brandCode", brandCode);
		// parameterMap.put("fileKey", fileKey);
		parameterMap.put("posMachineCode", posMachineCode);
		parameterMap.put("actualSalesDate", actualSalesDate);
		// parameterMap.put("transactionDate", transactionDate);
		parameterMap.put("orderTypeCode", dataType);
		parameterMap.put("identification", "POS");
		parameterMap.put("opUser", "SYS");
		System.out.println("tableTransfer=======actualSalesDate = "+actualSalesDate+", posMachineCode = " + posMachineCode);
		// ==========POS重傳時刪除相關銷售、出貨資料，並將轉檔資料存檔==========
		soSalesOrderMainService.saveT2PosNoValidate((List)returnBeans.get(0), parameterMap);     
		// ==========取報單訊息、批號、檢核無誤後扣庫存並將狀態改為SIGNING===========
		// updatePosData(processName, executeDate, uuid, parameterMap) ;
		return "";
	}
	
	/**
	 * 執行儲位單 更新 與 比對
	 */
	public void executeStorage(SoSalesOrderHead soSalesOrderHead) throws Exception {
		log.info("executeStorage");
		//更新儲位單頭 2011.11.11 by Caspar
		Map storageMap = new HashMap();
		storageMap.put("storageTransactionDate", "salesOrderDate");
		storageMap.put("storageTransactionType", ImStorageService.OUT);
		storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
		ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, soSalesOrderHead);
		
		//更新儲位單身與比對 2011.11.11 by Caspar
		storageMap.put("beanItem", "soSalesOrderItems");
		storageMap.put("quantity", "quantity");
		imStorageAction.executeImStorageItem(storageMap, soSalesOrderHead, imStorageHead);
	}
	
	public void bookDeductStockAction() throws Exception {
		
			List<SoSalesOrderHead> salesOrderHeads = null;
			salesOrderHeads = soSalesOrderMainService.updatesiResendSO();
			int i = 0;
			List errorMsgs = new ArrayList(0);
			log.info("salesOrderHeads.size()---:"+salesOrderHeads.size());
			for (Iterator iterator = salesOrderHeads.iterator(); iterator.hasNext();) {
				try{
					SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) iterator.next();
					String brandCode = soSalesOrderHead.getBrandCode();
			    	String orderTypeCode = soSalesOrderHead.getOrderTypeCode();
			    	String identification = MessageStatus.getIdentification(brandCode,
			    		orderTypeCode, soSalesOrderHead.getOrderNo());
			    	String opUser = soSalesOrderHead.getLastUpdatedBy();
			    	log.info("soSalesOrderHead.getHeadId()---:"+soSalesOrderHead.getHeadId());
			    	siResendService.updateSoCmData(soSalesOrderHead);
			    	soSalesOrderMainService.updateSalesOrderQty(soSalesOrderHead, errorMsgs, identification, opUser);
			    	if(StringUtils.hasText(soSalesOrderHead.getAppCustomerCode())){
			    		log.info("有AppCustomerCode進入Risend ftp");
			    		soSalesOrderMainService.appTransferToFTP(soSalesOrderHead); //risend 重送若有AppCustomerCode也產ftp給17-- by Brian
			    	}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			//soSalesOrderMainService.updateSalesOrderQty();			
	}
	
}