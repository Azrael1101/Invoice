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
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImPickHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.service.ImPickService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImPickAction {

	private static final Log log = LogFactory.getLog(ImPickAction.class);

	private ImPickService imPickService;

    private WfApprovalResultService wfApprovalResultService;
    
    private BaseDAO baseDAO;
    
	public void setImPickService(ImPickService imPickService) {
    	this.imPickService = imPickService;
    }

    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
    	this.wfApprovalResultService = wfApprovalResultService;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = imPickService.executeInitial(parameterMap); 
		} catch (Exception ex) {
			log.error("執行挑貨單初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	public List<Properties> performTransaction(Map parameterMap){
		log.info("performTransaction");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object otherBean    = parameterMap.get("vatBeanOther");
			String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String identification = (String)PropertyUtils.getProperty(otherBean, "identification");
			
			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");
			
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			ImPickHead imPickHead = imPickService.findById(formLinkBean);
			returnMap.put(AjaxUtils.AJAX_FORM, imPickHead);
			
			imPickService.updateHeadBean(parameterMap, returnMap);
			
			//預扣 與 結案
			if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus) ||
					OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus)){
				imPickService.check(parameterMap, returnMap);
				if(imPickService.deleteProgramLog(identification, 1) != 0 ){
					throw new FormException(MessageStatus.VALIDATION_FAILURE);
				}
				imPickService.updateAfterCheck(parameterMap, returnMap);

			//暫存
			}else if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.SAVE.equals(formStatus)){
				imPickService.updateSave(parameterMap, returnMap);

			//作廢
			}else if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.VOID.equals(formStatus)){

				imPickService.updateVoid(parameterMap, returnMap);			
			//其他
			}else{
				throw new Exception("查無單據送出對應方法");
			}
			
			imPickHead = (ImPickHead)returnMap.get(AjaxUtils.AJAX_FORM);
			wrappedMessageBox(msgBox, imPickHead, false, false);
			executeProcess(otherBean, imPickHead);
			
		}catch (FormException ex) {
			log.error("FormException 執行挑貨單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage("執行挑貨單存檔失敗");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"showMessage()", ""});
			msgBox.setOk(cmd_bf);
		}catch (Exception ex) {
			log.error("Exception 執行挑貨單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isCompleteAssignment
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, ImPickHead head, boolean isCompleteAssignment, boolean isExecFunction ){
    	String status = head.getStatus();
    	msgBox.setMessage("挑貨單"+head.getOrderNo() + "已" + OrderStatus.getChineseWord(status) + "！");
    	Command cmd_ok = new Command();
		Command cmd_cancel = new Command();
    	
		if(isCompleteAssignment){
    		msgBox.setType(MessageBox.ALERT);
    		cmd_ok.setCmd(Command.WIN_CLOSE);
    	}else{
    		msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createNewForm()",""});
			cmd_cancel.setCmd(Command.WIN_CLOSE);
    	}
    	
    	msgBox.setOk(cmd_ok);
    	msgBox.setCancel(cmd_cancel);
    	
    	if (isExecFunction) {
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
			msgBox.setBefore(cmd_bf);
		}
    }

    
    private void executeProcess(Object otherBean, ImPickHead imPickHead) throws Exception {
    	String message = null;
    	Object[] processInfo = null;
        try{
        	
        	String assignmentId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
            String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
            
            Boolean result = Boolean.valueOf(approvalResult);
            Long assignId = NumberUtils.getLong(assignmentId);
            
            log.info("assignmentId = " + assignmentId);
            log.info("approvalResult = " + approvalResult);
            
            if(!StringUtils.hasText(assignmentId))
            	processInfo = ImPickService.startProcess(imPickHead);
            else
            	processInfo = ImPickService.completeAssignment(assignId, result);
            
            long processId = (Long)processInfo[0];
            //long activityId = (Long)processInfo[1];
            //String activityName = (String)processInfo[2];
            imPickHead.setProcessId(processId);
            baseDAO.update(imPickHead);
        }catch(Exception ex){
            message = "執行流程發生錯誤，原因：" + ex.toString();
            log.error(message);
            throw new Exception(message);
        }
    }
    
	//---------------------------------------- ↓↓↓ Search ↓↓↓ ----------------------------------------//
	public List<Properties> performSearchInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = imPickService.executeSearchInitial(parameterMap); 
		} catch (Exception ex) {
			System.out.println("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
