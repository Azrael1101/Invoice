package tw.com.tm.erp.action;

import java.util.ArrayList;
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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CeapProcessService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImMovementMainService;
//for 儲位用
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImMovementMainAction {

    private static final Log log = LogFactory.getLog(ImMovementMainAction.class);

	private ImMovementMainService imMovementMainService;
	private SiProgramLogAction siProgramLogAction;
	private WfApprovalResultService wfApprovalResultService;
	private CeapProcessService ceapProcessService;
	//for 儲位用
	private ImStorageAction imStorageAction;
	
	public void setImMovementMainService(ImMovementMainService imMovementMainService) {
		this.imMovementMainService = imMovementMainService;
	}
	
	public void setCeapProcessService(CeapProcessService ceapProcessService) {
		this.ceapProcessService = ceapProcessService;
	}

	public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
		this.wfApprovalResultService = wfApprovalResultService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	//for 儲位用
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}

   
	/**
	 * 背景送出下取得單號
	 *
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> getOrderNo(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Map resultMap = imMovementMainService.updateImMovementWithActualOrderNO(parameterMap);
			ImMovementHead imMovementHead = (ImMovementHead) resultMap.get("entityBean");

			msgBox.setMessage((String) resultMap.get("resultMsg") );
			Object otherBean = parameterMap.get("vatBeanOther");
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String departmentLogin = (String) PropertyUtils.getProperty(otherBean, "departmentLogin");
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				wrappedMessageBox(msgBox, imMovementHead, true, true,departmentLogin);
			} else {
				wrappedMessageBox(msgBox, imMovementHead, false, true,departmentLogin);
			}
		} catch (Exception ex) {
			log.error("執行調撥單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

    /**
	 * 執行調撥單交易流程
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap) {

    	Map returnMap = new HashMap(0);
    	HashMap resultMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction new新版");
		try {
			log.info("1.performTransaction new新版111");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			log.info("1.performTransaction new新版222");
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			log.info("formLinkBean:"+formLinkBean.toString());
			log.info("formBindBean:"+formBindBean.toString());
			log.info("otherBean:"+otherBean.toString());
			Long headId = imMovementMainService.getImMovementHeadId(formLinkBean);
            ImMovementHead imMovementHead = imMovementMainService.findById(headId);
            String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
            String departmentLogin = (String) PropertyUtils.getProperty(otherBean, "departmentLogin");
            
            imMovementMainService.removeDetailItemCode(imMovementHead);//移除空白item
            
            String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
            System.out.println("beforeChangeStatus:" + beforeChangeStatus);
            if(beforeChangeStatus.equals("SAVE")&& null==imMovementHead.getSendRpNo() && imMovementHead.getOrderTypeCode().equals("WMF")){
				imMovementMainService.saveRpNo(imMovementHead, employeeCode);//移倉單申請編號
			}
            
			if (!StringUtils.hasText(beforeChangeStatus)) {
				throw new ValidationErrorException("原單據狀態參數為空值，無法執行存檔！");
			}

			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
			
			// 確認是否允許流程，不允許會丟例外
			//ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imMovementHead.getProcessId()), processId, assignmentId);

			if (OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.WAIT_IN.equals(beforeChangeStatus)
					|| OrderStatus.WAIT_OUT.equals(beforeChangeStatus)) {
				imMovementMainService.saveActualOrderNo(headId, employeeCode);  //設定正式單號以及最後更新日期 最後更新者
			}
			
			resultMap = imMovementMainService.updateAJAXMovementParcial(parameterMap); //將前端資料塞入bean
			
			parameterMap.put("resultMap", resultMap);
			
			imMovementHead = imMovementMainService.checkImMovement(parameterMap);  //檢核單頭以及單身資料正確性 移除刪除的明細資料
			
			//imMovementHead = (ImMovementHead) resultMap.get("entityBean");
			parameterMap.put("imMovementHead", imMovementHead);
			 //for 儲位用
				if(imStorageAction.isStorageExecute(imMovementHead)){
					//取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
					executeStorage(imMovementHead);
				}
				log.info("確認取回的bean總數量:"+imMovementHead.getItemCount());
				
			 resultMap = imMovementMainService.updateAJAXMovement(parameterMap);
			
			
			
			//imMovementService.updateItemIndex(movementBean); // 重新排序INDEX_NO
			returnMap.put("orderNo", imMovementHead.getOrderNo());
			if("01".equals(departmentLogin)){
				msgBox.setMessage((String) resultMap.get("resultMsg")+"!");
			}
			else
			{
				msgBox.setMessage((String) resultMap.get("resultMsg") + "，是否列印報表？");
			}
			
			log.info("準備起流程～");
			executeProcess(otherBean, resultMap, imMovementHead, msgBox);

			// 最後在更新一次 proccessId
			
			imMovementMainService.updateProcessId(imMovementHead.getHeadId(), imMovementHead.getProcessId() == null ? (Long)resultMap
					.get("processId") : imMovementHead.getProcessId());
		}catch (StorageException sex) {
			msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
			msgBox.setType(MessageBox.CONFIRM);
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
			msgBox.setOk(cmd_bf);
			//msgBox.setCancel(cmd_cancel);
		} catch (FormException fex) {
			msgBox.setMessage("執行調撥單存檔時發生錯誤");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("執行調撥單存檔時發生錯誤，原因：" + ex.toString());
			log.error("執行調撥單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	/*
    private void executeProcess(Object otherBean, ImMovementHead movementBean, MessageBox msgBox) throws Exception {
    	log.info("original executeProcess");
    	HashMap resultMap = new HashMap(0);
    	Object[] processResult = new Object[3];
        String assignId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
        String procId = (String)PropertyUtils.getProperty(otherBean, "processId");
        String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        Boolean approvalResult = (Boolean)PropertyUtils.getProperty(otherBean, "approvalResult");
        String originalStatus = (String)PropertyUtils.getProperty(otherBean, "status");
        //Boolean approvalResult = true;
        String resultMessage = new String("");

        try{
	        if(!StringUtils.hasText(procId)&&
	        		(OrderStatus.SIGNING.equals(movementBean.getStatus())||OrderStatus.SAVE.equals(movementBean.getStatus()))){
	        	processResult = ImMovementService.startProcess(movementBean);
	        	resultMessage = "送出";
	        	wrappedMessageBox(msgBox, movementBean, true, false);
	        }else{

			    if(StringUtils.hasText(procId) && StringUtils.hasText(assignId)){
		            Long assignmentId = NumberUtils.getLong(assignId);
		            System.out.println("result:"+approvalResult);
		            processResult = ImMovementService.completeAssignment(assignmentId, approvalResult);
		            if (OrderStatus.REJECT.equals(originalStatus))
		            	resultMessage = "送出";
		            else
		            	resultMessage = (approvalResult?"核准":"駁回");
		            wrappedMessageBox(msgBox, movementBean, false, false);
			    }else{
			    	throw new ProcessFailedException("Complete assignment時發生錯誤,ProcessId="
			    			+ procId +",AssignmentId="+ assignId +",result="+approvalResult);
			    }
	        }
		    resultMap.put("brandCode"    , movementBean.getBrandCode());
		    resultMap.put("orderTypeCode", movementBean.getOrderTypeCode());
		    resultMap.put("orderNo"      , movementBean.getOrderNo());
		    resultMap.put("processId"    , processResult[0]);
		    resultMap.put("activityId"   , processResult[1]);
		    resultMap.put("activityName" , processResult[2]);
		    resultMap.put("approver"     , loginEmployeeCode);
		    resultMap.put("result"       , resultMessage);
		    wfApprovalResultService.saveApprovalResult(resultMap);

        }catch(Exception pe){
        	log.error("調撥單流程執行時發生錯誤，原因：" + pe.toString());
			throw new Exception(pe.getMessage());
        }
    }
	 */

	  /**
	 * 綑綁回前畫面的視窗
	 *
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, ImMovementHead head, boolean isStartProcess, boolean isExecFunction , String departmentLogin ) {
		log.info("wrappedMessageBox...............");
		log.info("departmentLogin..............."+departmentLogin);
		String orderTypeCode = head.getOrderTypeCode();
		String orderNo = head.getOrderNo();
		String status = head.getStatus();
		String identification = orderTypeCode + "-" + orderNo;

		Command cmd_ok = new Command();
		Command cmd_cancel = new Command();
		if (isStartProcess) {
			if("01".equals(departmentLogin)){
				log.info("wrappedMessageBox01.startProcess");
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "closeWindows('AFTER_SUBMIT')", "" });

				cmd_cancel.setCmd(Command.FUNCTION);
				cmd_cancel.setParameters(new String[] { "createRefreshForm()", "" });
				// msgBox.setCancel(cmd_cancel);
			}
			else{
				log.info("wrappedMessageBox01.startProcess");
				cmd_ok.setCmd(Command.FUNCTION);
				cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });

				cmd_cancel.setCmd(Command.FUNCTION);
				cmd_cancel.setParameters(new String[] { "createRefreshForm()", "" });
				// msgBox.setCancel(cmd_cancel);
			}

		} else {
			if (isExecFunction) {
				msgBox.setMessage(identification + "表單已送出，是否列印報表?");
			} else {
				msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "，是否列印報表?");
			}
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] { "openReportWindow('AFTER_SUBMIT')", "" });
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			// msgBox.setType(MessageBox.CONFIRM);
		}
		msgBox.setType(MessageBox.CONFIRM);
		msgBox.setOk(cmd_ok);
		msgBox.setCancel(cmd_cancel);
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


    /**
	 * 背景送出
	 *
	 * @param parameterMap
	 * @return List<Properties>
	 */
    public List<Properties> performTransactionForBackGround(Map parameterMap) {
		log.info("performTransactionForBackGround");
		Map returnMap = new HashMap(0);
		ImMovementHead imMovement = null;
		Map resultMap = new HashMap(0);
		String message = null;
		String identification = null;
		try {
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = imMovementMainService.getImMovementHeadId(formLinkBean);
			log.info("performTransactionForBackGround headId=" + headId);

			try {
				imMovement = imMovementMainService.findById(headId);

				// 流程控制，避免重複流程造成的錯誤 2010.07.02
				Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
				Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));

				// 確認是否允許流程，不允許會丟例外
				//ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imMovement.getProcessId()), processId, assignmentId);

				log.info("bbb:" + imMovement.getStatus());
				resultMap.put("entityBean", imMovement);
				List errorMsgs = imMovementMainService.updateCheckedImMovementData(parameterMap);
				if (errorMsgs.size() == 0) {
					log.info("ggg:" + imMovement.getStatus());
					resultMap = imMovementMainService.updateAJAXMovement(parameterMap);
					imMovement = (ImMovementHead) resultMap.get("entityBean");
					log.info("ddd:" + imMovement.getStatus());
				} else {
					if (OrderStatus.UNCONFIRMED.equals(imMovement.getStatus())) {
						log.info("save fail: change status from unconfirmed to save");
						imMovement.setStatus(OrderStatus.SAVE);
					}
				}

				if (imMovement != null) {
					executeProcess(otherBean, resultMap, imMovement, null);

					//最後在更新一次  proccessId 2010.07.02
					imMovementMainService.updateProcessId(imMovement.getHeadId(), imMovement.getProcessId());
				}

			} catch (Exception ex) {
				if (imMovement != null) {
					log.info(ex.getMessage());
					identification = MessageStatus.getIdentification(imMovement.getBrandCode(), imMovement
							.getOrderTypeCode(), imMovement.getOrderNo());
					message = ex.getMessage();
					siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message,
							imMovement.getLastUpdatedBy());
				}
			}

			// ==============execute flow==================
//			 if (imMovement != null) {
//			executeProcess(otherBean, resultMap, imMovement, null);
//			String userType = (String) PropertyUtils.getProperty(otherBean, "userType");
//			String typeCode = (String) PropertyUtils.getProperty(otherBean, "typeCode");
//			if (("WAREHOUSE".equals(userType)) || // WAREHOUSE 啟動調撥單 FLOW
//					("SHIPPING".equals(userType) && "RR".equals(typeCode))) { // SHIPPING && RR 啟動 INVOICE FLOW
//				imMovementService.updateAJAXStartOtherFlow(imMovement, userType);
//			}
//		}

		} catch (Exception ex) {
			message = "執行調撥單背景存檔失敗，原因：" + ex.toString();
			log.error(message);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

    /**
	 * 調撥單起流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param imMovement
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, Map resultMap, ImMovementHead imMovement, MessageBox msgBox)
			throws Exception {
		log.info("new executeProcess");
		String message = null;
		String identification = MessageStatus.getIdentification(imMovement.getBrandCode(), imMovement.getOrderTypeCode(),
				imMovement.getOrderNo());
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			Boolean approvalResult = (Boolean) PropertyUtils.getProperty(otherBean, "approvalResult");
			// String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			// String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String updateForm = (String) PropertyUtils.getProperty(otherBean, "updateForm");
			String formStatus = imMovement.getStatus();
			String departmentLogin = (String) PropertyUtils.getProperty(otherBean, "departmentLogin");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, formStatus);
			log.info("processId=" + processId + " assignmentId=" + assignmentId + " approvalResult=" + approvalResult
					+ " approvalComment=" + approvalComment + " beforeChangeStatus=" + beforeChangeStatus + " formStatus="
					+ formStatus + " approvalResultMsg=" + approvalResultMsg);
			log.info("ccc:" + imMovement.getStatus());
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null)
					wrappedMessageBox(msgBox, imMovement, "Y".equals(updateForm) ? false : true, false,departmentLogin);

				if("T1GS".equals(loginEmployeeCode)|| "01".equals(departmentLogin)|| "T1GS".equals(loginBrandCode)|| "T1BS".equals(loginBrandCode)){
					log.info("新百貨流程5~~~~~~~~~~~~~");
					Object[] processObj01 = ImMovementMainService.startProcess01(imMovement); // 起流程後，取回流程的processId
					// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
					System.out.println("流程 起始 id ======> " + processObj01[0]);
					imMovement.setProcessId((Long) processObj01[0]);
				}
				else{
					log.info("4~~~~~~~~~~~~~");
					Object[] processObj = ImMovementMainService.startProcess(imMovement); // 起流程後，取回流程的processId
					// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
					System.out.println("流程 起始 id ======> " + processObj[0]);
					imMovement.setProcessId((Long) processObj[0]);
				}

			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, imMovement, false, false,departmentLogin);
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					// Boolean result = Boolean.valueOf(approvalResult);
					ceapProcessService.updateProcessSubject(Long.valueOf(processId), imMovementMainService.getProcessSubject(imMovement));
					Object[] processInfo = imMovementMainService.completeAssignment(assignId, approvalResult);
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
			message = "執行調撥流程發生錯誤，原因：" + ex.toString();
			siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message, imMovement
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

	/**
	 * 作廢，背景送出下取得單號
	 *
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> voidMovement(Map parameterMap) {
		log.info("voidMovement");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String action = (String) PropertyUtils.getProperty(otherBean, "action");

			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			Boolean approvalResult = (Boolean) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "status");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String approvalResultMsg = getApprovalResult(processId, beforeChangeStatus, action);
			log.info("processId:" + processId + " action:" + action + " assignmentId:" + assignmentId);
			log.info("loginEmployeeCode:" + loginEmployeeCode);

			Long headId = imMovementMainService.getImMovementHeadId(formLinkBean);
			log.info("headId:" + headId);
			Map resultMap = imMovementMainService.updateImMovementToVoid(headId, loginEmployeeCode, action);

			msgBox.setMessage((String) resultMap.get("resultMsg"));
			ImMovementHead imMovement = (ImMovementHead) resultMap.get("entityBean");
			//wrappedMessageBox(msgBox, imMovement, false, false);
			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.WIN_CLOSE);
			msgBox.setType(MessageBox.ALERT);
			msgBox.setOk(cmd_ok);
			if(OrderStatus.REJECT.equals(action)) // 如果作駁回的動作，流程要回到起單人身上
				executeProcess(otherBean, resultMap, imMovement, msgBox);

		} catch (Exception ex) {
			log.error("執行調撥單作廢失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 修改調撥日期
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> modifyDeliveryDate(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		try {
			HashMap resultMap = imMovementMainService.updateDate(parameterMap);

			msgBox.setMessage("調撥日期修改成功");
			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.WIN_CLOSE);
			msgBox.setOk(cmd_ok);
			msgBox.setType(MessageBox.ALERT);
		} catch (Exception ex) {
			System.out.println("轉出日期修改失敗，原因：" + ex.toString());
			log.error("轉出日期修改失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	/**
	 * 完成移倉作業後，自動起調撥單 2010.07.14
	 *
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return
	 */
	public int autoCreateImMovement(String brandCode, String orderTypeCode, String orderNo, Date date) throws Exception {
		int count = 0;
		MessageBox msgBox = new MessageBox();
		try {
			ImMovementHead imMovementHead = imMovementMainService
					.findMovementByIdentification(brandCode, orderTypeCode, orderNo);
			ImMovementHead newImMovementHead = null;
			if (imMovementHead == null)
				throw new ValidationErrorException("調撥單：" + orderTypeCode + orderNo + "不存在，請重新確認！");
			else if ("WHF".equals(orderTypeCode) || "WHP".equals(orderTypeCode)) {
				newImMovementHead = imMovementMainService.createNewImMovement(imMovementHead, date);
				if (newImMovementHead != null){
					log.info("========== 自動起調撥單流程 ==========");
					imMovementMainService.startProcess(newImMovementHead);
				}else
					log.info("調撥單：" + orderTypeCode + orderNo + "，無法自動起調撥單。");
			} else {
				log.info("調撥單：" + orderTypeCode + orderNo + "，此類型調撥單無須自動起調撥單");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("自動起調撥單失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
			throw ex;
		}
		return count;
	}

	/**
	 * 執行調撥單複製交易流程
	 *
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performCopyTransaction(Map parameterMap) {

		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.調撥單單據複製開始...");
		try {
			//Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			//String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String timeScope = (String) PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(otherBean, AjaxUtils.SEARCH_KEY);

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.size(); i++) {
				Long headId = NumberUtils.getLong((String) ((Properties) result.get(i)).getProperty("headId"));
				System.out.println("headId ::: " + headId);

				ImMovementHead imMovementHead = imMovementMainService.findById(headId);

				if (imMovementHead != null) {
					ImMovementHead newImMovementHead = imMovementMainService.createToNewImMovement(imMovementHead, parameterMap);
					if (newImMovementHead != null) {
						ImMovementMainService.startProcess(newImMovementHead); // 調撥單起流程
						if (sb.length() == 0)
							sb.append("調撥單：" + imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo()
									+ "複製完成，新產生的調撥單：" + newImMovementHead.getOrderTypeCode()
									+ newImMovementHead.getOrderNo());
						else
							sb.append("\n").append(
									"調撥單：" + imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo()
											+ "複製完成，新產生的調撥單：" + newImMovementHead.getOrderTypeCode()
											+ newImMovementHead.getOrderNo());
					} else {
						String identification = MessageStatus.getIdentification(imMovementHead.getBrandCode(),
								imMovementHead.getOrderTypeCode(), imMovementHead.getOrderNo());
						String message = "調撥單：" + imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo() + "複製失敗！";
						if (sb.length() == 0)
							sb.append(message);
						else
							sb.append("\n").append(message);
						siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message,
								imMovementHead.getLastUpdatedBy());
					}
				} else {
					String identification = MessageStatus.getIdentification(imMovementHead.getBrandCode(), imMovementHead
							.getOrderTypeCode(), imMovementHead.getOrderNo());
					String message = "調撥單：" + imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo()
							+ "不存在，請確認後在查詢！";
					if (sb.length() == 0)
						sb.append(message);
					else
						sb.append("\n").append(message);
					siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message,
							imMovementHead.getLastUpdatedBy());
				}
			}
			msgBox.setMessage(sb.length()==0? "未複製任何調撥單單據！" : sb.toString());
			msgBox.setType(MessageBox.ALERT);

		} catch (FormException fex) {
			msgBox.setMessage("執行調撥單複製時發生錯誤");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println("執行調撥單複製時發生錯誤，原因：" + ex.toString());
			log.error("執行調撥單複製時發生錯誤，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 執行儲位單 更新 與 比對
	 */
	public void executeStorage(ImMovementHead imMovementHead) throws Exception {
		//更新儲位單頭 2011.11.11 by Caspar
		Map storageMap = new HashMap();
		storageMap.put("storageTransactionDate", "deliveryDate");
		storageMap.put("storageTransactionType", ImStorageService.MOVE);
		storageMap.put("deliveryWarehouseCode", "deliveryWarehouseCode");
		storageMap.put("arrivalWarehouseCode", "arrivalWarehouseCode");
		ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imMovementHead);
		
		//更新儲位單身與比對 2011.11.11 by Caspar
		storageMap.put("beanItem", "imMovementItems");
		storageMap.put("quantity", "deliveryQuantity");
		storageMap.put("pickOrderTypeCode", imMovementHead.getOriginalOrderTypeCode());
		storageMap.put("pickOrderNo", imMovementHead.getOriginalOrderNo());
		imStorageAction.executeImStorageItem(storageMap, imMovementHead, imStorageHead);
	}
	
}