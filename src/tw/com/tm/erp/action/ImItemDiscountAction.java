package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.dao.ImItemDiscountHeadDAO;

import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.ImItemDiscountLine;
import tw.com.tm.erp.hbm.bean.ImItemDiscountMod;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuSupplierModService;
import tw.com.tm.erp.hbm.service.ImItemDiscountService;
import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImItemDiscountAction {

	private static final Log log = LogFactory.getLog(ImItemDiscountAction.class);	
	
	private ImItemDiscountService imItemDiscountService;
		
	public void setImItemDiscountService(ImItemDiscountService imItemDiscountService) {
		this.imItemDiscountService = imItemDiscountService;
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
		    returnMap = imItemDiscountService.searchExecuteInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行商品折扣檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 */
    public List<Properties> performTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		ImItemDiscountHead head = null;
		Object otherBean = null;
		try {
		
			Object formBean = parameterMap.get("vatBeanFormBind");
			otherBean = parameterMap.get("vatBeanOther");
			
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			
			// 驗證必要欄位
//			String status = (String)PropertyUtils.getProperty(formBean, "status");
//			if(!StringUtils.hasText(status)){
//		    	throw new ValidationErrorException("status參數為空值，無法執行存檔！");
//		    }
			
			// 前端資料塞入bean 
			resultMap = imItemDiscountService.updateImItemDiscount(parameterMap);
			
			// 檢核與 存取db head and line
			resultMap = imItemDiscountService.updateAJAXImItemDiscount(parameterMap, formAction);
			
			head = (ImItemDiscountHead) resultMap.get("entityBean");	
			
			msgBox.setMessage((String) resultMap.get("resultMsg") );
			
//			executeProcess(otherBean, resultMap, head, msgBox, false);
		
		} catch( ValidationErrorException ve ){
			log.error("執行商品折扣檢核失敗，原因：" + ve.toString());
			if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
				listErrorMsg(msgBox);
			}else{
				msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
			}
		} catch (Exception ex) {
			log.error("執行商品折扣存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
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
    
    /**
	 * ImItemDiscount初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performImItemDiscountformInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = imItemDiscountService.executeImItemDiscountInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行商品折扣檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 */    
    public List<Properties> performModTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		Object otherBean = null;
		
		try {
		
			otherBean = parameterMap.get("vatBeanOther");
			
			// 驗證 把畫面的值 存成BEAN 驗證
			imItemDiscountService.validateHead(parameterMap);
			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			imItemDiscountService.executeFindActual(parameterMap);
			// 前端資料塞入bean 
			imItemDiscountService.updateImitemDiscountModBean(parameterMap);
			// 存檔  改狀態
			resultMap = imItemDiscountService.updateAJAXImItemDiscountMod(parameterMap);
			
			ImItemDiscountMod imItemDiscountMod = (ImItemDiscountMod) resultMap.get("entityBean");
	
			wrappedMessageBox(msgBox, imItemDiscountMod, true , false);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


			executeProcess(otherBean, resultMap, imItemDiscountMod, msgBox);

		} catch( ProcessFailedException px ){
			log.error("執行商品折扣檢核失敗，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
			
		} catch (Exception ex) {
			log.error("執行商品折扣存檔失敗，原因：" + ex.toString());
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
	
	private void wrappedMessageBox(MessageBox msgBox,ImItemDiscountMod imItemDiscountMod , boolean isStartProcess, boolean isExecFunction){

		log.info("wrappedMessageBox...............");
		
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
	private void executeProcess(Object otherBean, Map resultMap, ImItemDiscountMod imItemDiscountMod, MessageBox msgBox)
			throws Exception {
		log.info("執行簽核流程");
		String message = null;
		/*try {
			
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
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
				log.info("processId="+ buSupplierMod.getProcessId() );
				resultMap.put("processId", processObj[0]);
				resultMap.put("activityId", processObj[1]);
				resultMap.put("activityName", processObj[2]);
				resultMap.put("result", approvalResultMsg);
				resultMap.put("approvalComment", approvalComment);
				BuSupplierMod entityMap = (BuSupplierMod) resultMap.get("entityBean");
				entityMap.setOrderTypeCode("SPM");
				entityMap.setOrderNo(headId.toString());
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
					Object[] processInfo = BuSupplierModService.completeAssignment(assignId, result, buSupplierMod);
					log.info("assignId=" + assignId);
					resultMap.put("processId", processInfo[0]);
					resultMap.put("activityId", processInfo[1]);
					resultMap.put("activityName", processInfo[2]);
					resultMap.put("result", approvalResultMsg);
					resultMap.put("approvalComment", approvalComment);
					BuSupplierMod entityMap = (BuSupplierMod) resultMap.get("entityBean");
					entityMap.setOrderTypeCode("SPM");
					entityMap.setOrderNo(headId.toString());
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
		}*/
	}
	

}
