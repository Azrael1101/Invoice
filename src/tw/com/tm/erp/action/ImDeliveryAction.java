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
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.service.ImDeliveryMainService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class ImDeliveryAction {
    
    private static final Log log = LogFactory.getLog(ImDeliveryAction.class);
    
    private SiProgramLogAction siProgramLogAction;
    
    private ImDeliveryMainService imDeliveryMainService;
    
    private WfApprovalResultService wfApprovalResultService;
    
    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    
    private CmDeclarationHeadDAO cmDeclarationHeadDAO;

	public void setImDeliveryMainService(ImDeliveryMainService imDeliveryMainService) {
    	this.imDeliveryMainService = imDeliveryMainService;
    }
    
    public void setWfApprovalResultService(WfApprovalResultService wfApprovalResultService) {
    	this.wfApprovalResultService = wfApprovalResultService;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }
    
    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
		this.imDeliveryHeadDAO = imDeliveryHeadDAO;
	}
    
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}
	
    public List<Properties> performTransaction(Map parameterMap){

    	Map returnMap = new HashMap(0);
	    List errorMsgs = new ArrayList(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String organizationCode = (String)PropertyUtils.getProperty(otherBean, "organizationCode");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
			String procId = (String)PropertyUtils.getProperty(otherBean, "processId");
			ImDeliveryHead imDeliveryHead = imDeliveryMainService.findImDeliveryHeadById(formId);
			HashMap resultMap = new HashMap(0);
			resultMap.put("beforeChangeStatus", beforeChangeStatus);
			resultMap.put("organizationCode", organizationCode);
			resultMap.put("formAction", formAction);
			log.info("==ImDeliveryAction.performTransaction==beforeChangeStatus = " + beforeChangeStatus + ",userType="+userType);
			//如果是原本是暫存單
			if(OrderStatus.SAVE.equals(beforeChangeStatus)){
				//更新主檔資料
				imDeliveryHead = imDeliveryMainService.updateAJAXMovement(parameterMap , imDeliveryHead);
				if(!"SAVE".equals(formAction)){
					//如果只是暫存檢查主檔資料
					errorMsgs = imDeliveryMainService.checkDeliveryData(imDeliveryHead);
					msgBox.setMessage(imDeliveryMainService.updateDelivery(imDeliveryHead, resultMap, loginEmployeeCode, errorMsgs));
				}
			}else if (OrderStatus.SIGNING.equals(beforeChangeStatus) && userType.equals("SHIPPING")){
			    imDeliveryHead = imDeliveryMainService.updateAJAXMovement(parameterMap , imDeliveryHead);
			    errorMsgs = imDeliveryMainService.updateCheckData(imDeliveryHead);
			    if(errorMsgs.size() > 0){
				throw new Exception(MessageStatus.VALIDATION_FAILURE);
			    }
			    if(imDeliveryHead.getLatestExportDeclNo() == null)
				imDeliveryHead.setExportDeclNoLog(imDeliveryHead.getLatestExportDeclNo());
			    else
				imDeliveryHead.setExportDeclNoLog(","+imDeliveryHead.getLatestExportDeclNo());
			    //log.info("==ImDeliveryAction.performTransaction==imDeliveryHead.getHeadId() = " + imDeliveryHead.getHeadId() + ",imDeliveryHead.getExportDeclNo()="+imDeliveryHead.getExportDeclNo());
			    SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO
			    	.findByPrimaryKey(SoSalesOrderHead.class, imDeliveryHead.getSalesOrderId());
			    soSalesOrderHead.setExportDeclNo(imDeliveryHead.getExportDeclNo());
			    soSalesOrderHead.setExportDeclDate(imDeliveryHead.getExportDeclDate());
			    soSalesOrderHead.setExportDeclType(imDeliveryHead.getExportDeclType());
			    soSalesOrderHead.setLatestExportDeclNo(imDeliveryHead.getLatestExportDeclNo());
			    soSalesOrderHead.setLatestExportDeclDate(imDeliveryHead.getLatestExportDeclDate());
			    soSalesOrderHead.setLatestExportDeclType(imDeliveryHead.getLatestExportDeclType());
			    soSalesOrderHead.setExportDeclNoLog(imDeliveryHead.getExportDeclNoLog());
			    soSalesOrderHeadDAO.update(soSalesOrderHead);
			    
			    //修改報單狀態為FINISH
			    CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(imDeliveryHead.getExportDeclNo());
			    //log.info("==ImDeliveryAction.performTransaction==cmDeclarationHead.getHeadId() = " + cmDeclarationHead.getHeadId());
			    if(cmDeclarationHead != null){
				cmDeclarationHead.setStatus("FINISH");
				cmDeclarationHeadDAO.update(cmDeclarationHead);
			    }			    
			}else if (OrderStatus.SIGNING.equals(beforeChangeStatus) && userType.equals("WAREHOUSEMANAGER")){
			    //若倉庫進行驗收後，改變warehouse_status='FINISH'
			    imDeliveryHead.setWarehouseStatus("FINISH");			    
			}
	    	imDeliveryHeadDAO.update(imDeliveryHead);
	    	if(StringUtils.hasText(procId) && !OrderStatus.SAVE.equals(imDeliveryHead.getStatus()))
	    		executeProcess(otherBean, imDeliveryHead, msgBox);
	    	else
	    		wrappedMessageBox(msgBox, imDeliveryHead, false, false);
		}  catch (Exception ex) {
			log.error("執行出貨單時存檔失敗"+ex.getMessage());
			msgBox.setMessage("執行出貨單時存檔失敗");
			listErrorMessageBox(msgBox);
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    public List<Properties> performTransactionForBackGround(Map parameterMap){
      	
    	Map returnMap = new HashMap(0);	
	    List errorMsgs = new ArrayList(0);
    	String message = null;
    	try{
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
    		ImDeliveryHead imDeliveryHead = imDeliveryMainService.findImDeliveryHeadById(formId);
    	    try{      	     	       
				if(!OrderStatus.SIGNING.equals(imDeliveryHead.getStatus())){
					//如果只是暫存檢查主檔資料
					imDeliveryHead = imDeliveryMainService.updateAJAXMovement(parameterMap , imDeliveryHead);
					errorMsgs = imDeliveryMainService.checkDeliveryData(imDeliveryHead);
				}
				if(errorMsgs.size() > 0)
					throw new Exception();
				else
					imDeliveryHead.setStatus(OrderStatus.SIGNING);
    	    }catch(Exception ex){
    	    	log.error("執行銷貨單背景存檔檢核未通過");
    	    }
    	  //==============execute flow==================
    	    if(imDeliveryHead != null){
    	    	imDeliveryHeadDAO.update(imDeliveryHead);
    	        executeProcess(otherBean, imDeliveryHead , null);
    	    }
    	}catch (Exception ex) {
    	    message = "執行銷貨單背景存檔失敗，原因：" + ex.toString();
    	    log.error(message);
    	}
		return AjaxUtils.parseReturnDataToJSON(returnMap);    	
    }

    
    /**
	 * 取得單號
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> getOrderNo(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			Long formId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "formId"));
			ImDeliveryHead imDeliveryHead = imDeliveryMainService.findImDeliveryHeadById(formId);
			imDeliveryHead = imDeliveryMainService.updateAJAXMovement(parameterMap , imDeliveryHead);
	    	wrappedMessageBox(msgBox, imDeliveryHead, false, true);
	    	
		} catch (Exception ex) {
			log.error("出貨單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
    private void executeProcess(Object otherBean, ImDeliveryHead imDeliveryHead, MessageBox msgBox) throws Exception {
    	String message = null;
        String identification = MessageStatus.getIdentification(imDeliveryHead.getBrandCode(), 
        		imDeliveryHead.getOrderTypeCode(), imDeliveryHead.getOrderNo());
    	HashMap resultMap = new HashMap(0);    
    	Object[] processResult = new Object[3];
        String assignId = (String)PropertyUtils.getProperty(otherBean, "assignmentId");
        String procId = (String)PropertyUtils.getProperty(otherBean, "processId");
        String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        Boolean approvalResult = (Boolean)PropertyUtils.getProperty(otherBean, "approvalResult");
        String resultMessage = new String("");
        try{
			    if(StringUtils.hasText(procId) && StringUtils.hasText(assignId)){
		            Long assignmentId = NumberUtils.getLong(assignId);
		            processResult = ImDeliveryMainService.completeAssignment(assignmentId, approvalResult); ////TODO 
		            if (OrderStatus.SAVE.equals(imDeliveryHead.getStatus()))
		            	resultMessage = "暫存";
		            else	
		            	resultMessage = (approvalResult?"核准":"駁回");
		            wrappedMessageBox(msgBox, imDeliveryHead, false, false);
			    }else{
			    	throw new Exception("Complete assignment時發生錯誤,ProcessId="
			    			+ procId +",AssignmentId="+ assignId +",result="+approvalResult);
			    }
		    resultMap.put("brandCode"    , imDeliveryHead.getBrandCode());
		    resultMap.put("orderTypeCode", imDeliveryHead.getOrderTypeCode());
		    resultMap.put("orderNo"      , imDeliveryHead.getOrderNo());
		    resultMap.put("processId"    , processResult[0]);
		    resultMap.put("activityId"   , processResult[1]);
		    resultMap.put("activityName" , processResult[2]);
		    resultMap.put("approver"     , loginEmployeeCode);
		    resultMap.put("result"       , resultMessage);
		    wfApprovalResultService.saveApprovalResult(resultMap);
        }catch(Exception pe){
        	message = "執行銷貨流程發生錯誤，原因：" + pe.toString();
    	    siProgramLogAction.createProgramLog(ImDeliveryMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
    		    identification, message, imDeliveryHead.getLastUpdatedBy());
    	    log.error(message);
    	    throw new Exception();
        }	
    }
    
    private void wrappedMessageBox(MessageBox msgBox, ImDeliveryHead imDeliveryHead, boolean isStartProcess, boolean isExecFunction){
    	
    	String orderTypeCode = imDeliveryHead.getOrderTypeCode();
    	String orderNo = imDeliveryHead.getOrderNo();
    	String status = imDeliveryHead.getStatus();
    	String identification = orderTypeCode + "-" + orderNo;
        Command cmd_ok = new Command();
        if(msgBox != null){
	    	if(isStartProcess){
	    	    msgBox.setType(MessageBox.CONFIRM);
	    	    cmd_ok.setCmd(Command.FUNCTION);
	    	    cmd_ok.setParameters(new String[]{"createNewForm()", ""});
	    	    Command cmd_cancel = new Command();
	    	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    	    msgBox.setCancel(cmd_cancel);
	    	}else{
	    	    if(OrderStatus.SIGNING.equals(status)|| isExecFunction){
	                msgBox.setMessage(identification + "表單已送出！");
	                cmd_ok.setCmd(Command.WIN_CLOSE);
	    	    }else if(OrderStatus.SAVE.equals(status)){
	    	        msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
	    	    }else{
	    	    	msgBox.setMessage(identification + "表單已" + OrderStatus.getChineseWord(status) + "！");
	    	    	cmd_ok.setCmd(Command.WIN_CLOSE);
	    	    }
	    	}
	    	msgBox.setOk(cmd_ok);
	    	//如果為背景送出
	    	if (isExecFunction) {
				Command cmd_bf = new Command();
				cmd_bf.setCmd(Command.FUNCTION);
				cmd_bf.setParameters(new String[] { "execSubmitBgAction()", "" });
				msgBox.setBefore(cmd_bf);
			}
        }
    }

    /**
	 * 送出錯誤後，呼叫的function
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
    private void listErrorMessageBox(MessageBox msgBox){
    	//如果為背景送出
			Command cmd_error = new Command();
			cmd_error.setCmd(Command.FUNCTION);
			cmd_error.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_error);
    }

    public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
        this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
    }

    
}
