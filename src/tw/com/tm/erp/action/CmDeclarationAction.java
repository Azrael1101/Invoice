package tw.com.tm.erp.action;

import java.util.ArrayList;
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
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CmDeclarationHeadService;
import tw.com.tm.erp.hbm.service.CmDeclarationLogService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class CmDeclarationAction {

	private static final Log log = LogFactory.getLog(CmDeclarationAction.class);

	public static final String PROGRAM_ID = "CM_DECLARATION";

	private CmDeclarationHeadService cmDeclarationHeadService;

	private SiProgramLogAction siProgramLogAction;
	
	private WfApprovalResultService wfApprovalResultService;

	public void setCmDeclarationHeadService(
			CmDeclarationHeadService cmDeclarationHeadService) {
		this.cmDeclarationHeadService = cmDeclarationHeadService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
    	this.wfApprovalResultService = wfApprovalResultService;
        }

	public List<Properties> performInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = cmDeclarationHeadService.executeInitial(parameterMap); 
		} catch (Exception ex) {
			System.out.println("執行海關進出倉單初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	  // change status only
	public List<Properties> performTransaction(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			Object vatBeanFormBind = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Long headId = NumberUtils.getLong((String)PropertyUtils.getProperty(formLinkBean, "headId"));
			String declNo = (String) PropertyUtils.getProperty(formLinkBean, "declNo");
			String declType = (String) PropertyUtils.getProperty(vatBeanFormBind, "declType");
			String strType = (String) PropertyUtils.getProperty(vatBeanFormBind, "strType");
			
			if(!StringUtils.hasText(declNo))
				throw new Exception("請輸入報關單號");
			else{
				CmDeclarationHead cmDeclarationHead  = cmDeclarationHeadService.getCmDeclarationHeadByDeclNo(declNo);
				if(headId == 0 && cmDeclarationHead != null)
					throw new Exception("報關單號不可重複");
			}
			
			if(!StringUtils.hasText(declType))
				throw new Exception("請輸入報關類別");
			
			if(!StringUtils.hasText(strType))
				throw new Exception("請輸入進出倉別，1:進倉　2:出倉");
			
			if(!"1".equals(strType) && !"2".equals(strType))
				throw new Exception("請輸入進出倉別，1:進倉　2:出倉");
			
			String beforeChangeStatus = (String) PropertyUtils.getProperty(vatBeanFormBind, "status");
			Map resultMap = new HashMap();
			resultMap = cmDeclarationHeadService.updateParameter(parameterMap);
			CmDeclarationHead head = (CmDeclarationHead)resultMap.get("entityBean");
			returnMap.put("formId", head.getHeadId());
			returnMap.put("status", head.getStatus());
			returnMap.put("statusName",  OrderStatus.getChineseWord(head.getStatus()));
			resultMap.put("beforeChangeStatus", beforeChangeStatus);
			executeProcess(otherBean, resultMap, head, msgBox, false);
		} catch (Exception ex) {
			log.error("執行海關進出倉存檔失敗，原因：" + ex.toString());
			msgBox.setMessage("執行海關進出倉存檔失敗，原因："+ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
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
			if (OrderStatus.SAVE.equals(formStatus) || OrderStatus.VOID.equals(formStatus)) {
				approvalResult = OrderStatus.getChineseWord(formStatus);
			}
		}
		return approvalResult;
	}
	
    /**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, CmDeclarationHead head, boolean isStartProcess, boolean isExecFunction ){
    	
    	String status = head.getStatus();
    	Command cmd_ok = new Command();
    	if (isStartProcess) {
		    cmd_ok.setCmd(Command.FUNCTION);
			Command cmd_cancel = new Command();
		    cmd_cancel.setCmd(Command.WIN_CLOSE);
		    msgBox.setCancel(cmd_cancel);
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setParameters(new String[]{"showDatails()", ""});
			msgBox.setMessage("報關單已成功儲存");
    	}else{
    		if( isExecFunction ){
    			msgBox.setMessage("報關單" + head.getDeclNo() + "表單已送出！");		
    		}else{
    			msgBox.setMessage("報關單" + head.getDeclNo() + "表單已" + OrderStatus.getChineseWord(status) + "！");
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

	private void executeProcess(Object otherBean, Map resultMap, CmDeclarationHead cmDeclarationHead, MessageBox msgBox, Boolean isBackground) throws Exception {
		String message = null;
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean,"processId");
			String assignmentId = (String) PropertyUtils.getProperty(otherBean,"assignmentId");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			String status = cmDeclarationHead.getStatus();
			String approvalResultMsg = this.getApprovalResult(processId, status);
			log.info( "processId = " + processId );
			log.info( "assignmentId = " + assignmentId );
			log.info( "approvalResult = " + approvalResult );
			log.info( "approvalComment = " + approvalComment );
			log.info( "status = " + status );
			log.info( "approvalResultMsg = " + approvalResultMsg );
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null && !isBackground ){
					wrappedMessageBox(msgBox, cmDeclarationHead, true, false);
				}
			} else {
				if (msgBox != null){
					wrappedMessageBox(msgBox, cmDeclarationHead, false, false);
				}
					if (StringUtils.hasText(assignmentId)) {
						Long assignId = NumberUtils.getLong(assignmentId);
						Boolean result = Boolean.valueOf(approvalResult);
						Object[] processInfo = CmDeclarationHeadService.completeAssignment(assignId, result);
						resultMap.put("processId", processInfo[0]);
						resultMap.put("activityId", processInfo[1]);
						resultMap.put("activityName", processInfo[2]);
						resultMap.put("result", approvalResultMsg);
						resultMap.put("approvalComment", approvalComment);
						WfApprovalResultUtils.logApprovalResult(resultMap,wfApprovalResultService);
					} else {
						throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId+ "、AssignmentId=" + assignmentId+ "、result=" + approvalResult);
					}
				}
		} catch (Exception ex) {
			message = "執行預算修改修改流程發生錯誤，原因：" + ex.toString();
			log.error(message);
		}
	}
}
