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
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException; 
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.exceptions.ValidationErrorException;

import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.MessageBox;

import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
//for 儲位用
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;

import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;

public class ImReceiveMainAction {

	private static final Log log = LogFactory.getLog(ImReceiveMainAction.class);

	private ImReceiveHeadMainService	imReceiveHeadMainService;
	private WfApprovalResultService		wfApprovalResultService;
	private SiProgramLogAction			siProgramLogAction;
	private ImReceiveHeadDAO			imReceiveHeadDAO;
	private BuBrandService				buBrandService;
	//for 儲位用
	private ImStorageAction 			imStorageAction;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setImReceiveHeadMainService(ImReceiveHeadMainService imReceiveHeadMainService) {
		this.imReceiveHeadMainService = imReceiveHeadMainService;
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

	//for 儲位用
	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}

	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	
	/** 初始化, 取得各項下拉選單項目
	 * @param parameterMap
	 * @return List<Properties>
	 */
	 public List<Properties> performInitial(Map parameterMap){
		 log.info("performInitial");
		 Map returnMap = null;	
		 try{
			 returnMap = imReceiveHeadMainService.executeInitial(parameterMap);
		 }catch (Exception ex) {
			 log.info("執行進貨單初始化時發生錯誤，原因：" + ex.toString());
			 MessageBox msgBox = new MessageBox();
			 msgBox.setMessage(ex.getMessage());
			 returnMap = new HashMap();
			 returnMap.put("vatMessage" ,msgBox);
		 }
		 return AjaxUtils.parseReturnDataToJSON(returnMap);	
	 }


	 /**前景送出
	  * @param parameterMap
	  * @return List<Properties>
	  */
	 public List<Properties> performTransaction (Map parameterMap){
		 log.info("performTransaction");
		 Map returnMap = new HashMap(0);
		 MessageBox msgBox = new MessageBox();
		 try{
			 Object otherBean    = parameterMap.get("vatBeanOther");
			 String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
			 if(!StringUtils.hasText(formStatus)){
				 throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			 }
			 String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			 if(!StringUtils.hasText(beforeChangeStatus)){
				 throw new Exception("原單據狀態參數為空值，無法執行存檔！");	
			 }

			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Long headId = imReceiveHeadMainService.getImReceiveHeadId(formLinkBean); 
			 ImReceiveHead imReceiveBean = imReceiveHeadMainService.findById(headId);
			 if(null == imReceiveBean)
				 throw new Exception("查無進貨單主鍵：" + headId + "的資料！");

			 String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			 BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(imReceiveBean.getBrandCode(), loginEmployeeCode);
			 if(employeeWithAddressView == null){
				 throw new Exception("查無員工代號: " + loginEmployeeCode +" 對應之員工");
			 }

			 Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			 Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));

			 //確認是否允許流程，不允許會丟例外
			 ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imReceiveBean.getProcessId()), processId, assignmentId);

			 imReceiveHeadMainService.deleteProgramLog(parameterMap, 0);					// delete SiProgramLog
			 //if(!OrderStatus.SIGNING.equals(beforeChangeStatus) && OrderStatus.SIGNING.equals(formStatus)){
			 if(OrderStatus.SIGNING.equals(formStatus)){
				 imReceiveHeadMainService.updateImReceiveWithActualOrderNO(parameterMap, "FG" );		// update Head Data from form to bean & get actual OrderNo
				 List errorMsgs = imReceiveHeadMainService.updateCheckedImReceiveData(parameterMap);	// check ImReceive head & item & expense data
				 //if( errorMsgs.size()>0 || imReceiveHeadMainService.deleteProgramLog(parameterMap,1)>0 ){// check SiProgramLog.size>0
				 if(imReceiveHeadMainService.deleteProgramLog(parameterMap,1)>0 ){
					 throw new FormException(MessageStatus.VALIDATION_FAILURE);
				 }
				 //重新查詢進貨單
				 imReceiveBean = imReceiveHeadMainService.findById(headId);
			 }

			 //for 儲位用
			 if(imStorageAction.isStorageExecute(imReceiveBean)){
				 //取單號後，扣庫存前，執行更新儲位單頭與單身，比對單據明細與儲位明細
				 executeStorage(imReceiveBean);
			 }

			 Map resultMap = imReceiveHeadMainService.updateAJAXImReceive(parameterMap);
			 if( imReceiveHeadMainService.deleteProgramLog(parameterMap, 1) > 0 ){	// check SiProgramLog.size>0
				 throw new FormException(MessageStatus.VALIDATION_FAILURE);
			 }

			 imReceiveBean = (ImReceiveHead)resultMap.get("entityBean");
			 msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			 if(!OrderStatus.FINISH.equals(imReceiveBean.getStatus()) && !OrderStatus.CLOSE.equals(imReceiveBean.getStatus()))
				 executeProcess(otherBean, resultMap, imReceiveBean, msgBox);
			 else
				 wrappedMessageBox(msgBox, imReceiveBean, false, false);

			 String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
			 String typeCode = (String)PropertyUtils.getProperty(otherBean, "typeCode");
			 String warehouseStatus = (String)PropertyUtils.getProperty(otherBean, "warehouseStatus");

			 if(("WAREHOUSE".equals(userType) && !OrderStatus.FINISH.equals(warehouseStatus)) || // WAREHOUSE 啟動調撥單 FLOW
					 ("SHIPPING".equals(userType) && "RR".equals(typeCode)) && "Y".equals(imReceiveBean.getFinanceConfirm())) {	// SHIPPING && RR 啟動 INVOICE FLOW
				 imReceiveHeadMainService.updateAJAXStartOtherFlow(imReceiveBean, userType);
			 }

			 //最後再度更新一次
			 imReceiveHeadDAO.update(imReceiveBean);
		 }catch (StorageException sex) {
			 msgBox.setMessage("儲位匹配錯誤，是否執行自動重新匹配儲位");
			 msgBox.setType(MessageBox.CONFIRM);
			 Command cmd_bf = new Command();
			 cmd_bf.setCmd(Command.FUNCTION);
			 cmd_bf.setParameters(new String[] { "eventStorageService()", "" });
			 msgBox.setOk(cmd_bf);
			 //msgBox.setCancel(cmd_cancel);
		 }catch (FormException ex) {
			 msgBox.setMessage("執行進貨單存檔失敗");
			 Command cmd_bf = new Command();
			 cmd_bf.setCmd(Command.FUNCTION);
			 cmd_bf.setParameters(new String[]{"showMessage()", ""});
			 msgBox.setOk(cmd_bf);
		 }catch (Exception ex) {
			 log.error("執行進貨單存檔失敗，原因：" + ex.getMessage());
			 msgBox.setMessage(ex.getMessage());
		 }
		 returnMap.put("vatMessage" ,msgBox);
		 return AjaxUtils.parseReturnDataToJSON(returnMap);
	 }


	 /**取得單號 / for 背景送出
	  * @param parameterMap
	  * @return List<Properties>
	  */
	 public List<Properties> getOrderNo(Map parameterMap){
		 log.info("getOrderNo");
		 Map returnMap = new HashMap(0);
		 MessageBox msgBox = new MessageBox();
		 try{
			 Object otherBean = parameterMap.get("vatBeanOther");
			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Long headId = imReceiveHeadMainService.getImReceiveHeadId(formLinkBean); 
			 ImReceiveHead imReceiveBean = imReceiveHeadMainService.findById(headId);
			 if(null == imReceiveBean)
				 throw new Exception("查無進貨單主鍵：" + headId + "的資料！");

			 Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			 Long assignmentId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "assignmentId"));

			 //確認是否允許流程，不允許會丟例外
			 ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imReceiveBean.getProcessId()), processId, assignmentId);
			 
			 Map resultMap = imReceiveHeadMainService.updateImReceiveWithActualOrderNO(parameterMap, "BG");
			 imReceiveBean = (ImReceiveHead)resultMap.get("entityBean");
			 msgBox.setMessage((String)resultMap.get("resultMsg") + "是否繼續新增？");
			 
			 if(processId == 0){
				 wrappedMessageBox(msgBox, imReceiveBean, true, true);
			 }else{
				 wrappedMessageBox(msgBox, imReceiveBean, false, true);
			 }
		 }catch (Exception ex) {
			 log.error("執行進貨單存檔失敗，原因：" + ex.toString());
			 msgBox.setMessage(ex.getMessage());
		 }
		 returnMap.put("vatMessage" ,msgBox);
		 return AjaxUtils.parseReturnDataToJSON(returnMap);
	 }


	 /**背景送出
	  * @param parameterMap
	  * @return List<Properties>
	  */
	 public List<Properties> performTransactionForBackGround(Map parameterMap){
		 log.info("performTransactionForBackGround");
		 Map returnMap = new HashMap(0);	
		 Map resultMap = new HashMap(0);
		 ImReceiveHead imReceiveBean = null;
		 try{
			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean    = parameterMap.get("vatBeanOther");
			 Long   headId       = imReceiveHeadMainService.getImReceiveHeadId(formLinkBean);
			 imReceiveHeadMainService.deleteProgramLog(parameterMap, 0);	// delete SiProgramLog
			 imReceiveBean = imReceiveHeadMainService.findById(headId);
			 resultMap.put("entityBean", imReceiveBean);
			 try{
				 List errorMsgs = imReceiveHeadMainService.updateCheckedImReceiveData(parameterMap);
				 if( errorMsgs.size()>0 || imReceiveHeadMainService.deleteProgramLog(parameterMap,1)>0 )	// check SiProgramLog.size>0
					 throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				 resultMap  = imReceiveHeadMainService.updateAJAXImReceive(parameterMap);
				 if( imReceiveHeadMainService.deleteProgramLog(parameterMap, 1) > 0 )	// check SiProgramLog.size>0
					 throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
			 }catch(Exception e){

			 }
			 imReceiveBean = (ImReceiveHead)resultMap.get("entityBean");
			 if(imReceiveBean != null ){
				 executeProcess(otherBean, resultMap, imReceiveBean, null);
				 String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
				 String typeCode = (String)PropertyUtils.getProperty(otherBean, "typeCode");
				 String warehouseStatus = (String)PropertyUtils.getProperty(otherBean, "warehouseStatus");
				 
				 if(("WAREHOUSE".equals(userType) && !OrderStatus.FINISH.equals(warehouseStatus)) || // WAREHOUSE 啟動調撥單 FLOW
						 ("SHIPPING".equals(userType) && "RR".equals(typeCode))) {	// SHIPPING && RR 啟動 INVOICE FLOW
					 imReceiveHeadMainService.updateAJAXStartOtherFlow(imReceiveBean, userType);
				 }
				 //最後再度更新一次
				 imReceiveHeadDAO.update(imReceiveBean);	 
			 }
		 }catch (Exception ex) {
			 String message = "執行進貨單背景存檔失敗，原因：" + ex.toString();
			 String identification = 
				 MessageStatus.getIdentification(imReceiveBean.getBrandCode(), imReceiveBean.getOrderTypeCode(), imReceiveBean.getOrderNo());
			 siProgramLogAction.createProgramLog(ImReceiveHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, 
					 identification, message, imReceiveBean.getLastUpdatedBy());
			 log.error(message);
		 }
		 return AjaxUtils.parseReturnDataToJSON(returnMap);
	 }


	 /** 執行流程 */
	 private void executeProcess(Object otherBean, Map resultMap, ImReceiveHead imReceive, MessageBox msgBox) throws Exception {
		 String message = null;
		 String identification = 
			 MessageStatus.getIdentification( imReceive.getBrandCode(), imReceive.getOrderTypeCode(), imReceive.getOrderNo());
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
					 wrappedMessageBox(msgBox, imReceive, true, false);

				 BuBrand buBrand  = buBrandService.findById( imReceive.getBrandCode() );	// 取是否為 T2, Run 不同 flow 
				 Object processObj[] =  ImReceiveHeadMainService.startProcess(imReceive, buBrand.getBranchCode());  
				 imReceive.setProcessId((Long)processObj[0]);
			 }else{
				 if(msgBox != null)
					 wrappedMessageBox(msgBox, imReceive, false, false);

				 if(StringUtils.hasText(assignmentId)){
					 Long assignId = NumberUtils.getLong(assignmentId);
					 Boolean result = Boolean.valueOf(approvalResult);

					 Object[] processInfo = ImReceiveHeadMainService.completeAssignment(assignId, result, imReceive);
					 resultMap.put("processId",       processInfo[0]);
					 resultMap.put("activityId",      processInfo[1]);
					 resultMap.put("activityName",    processInfo[2]);
					 resultMap.put("result",          approvalResultMsg);
					 resultMap.put("approvalComment", approvalComment);
					 WfApprovalResultUtils.logApprovalResult(resultMap, wfApprovalResultService);
				 }else{
					 throw new ProcessFailedException("Complete assignment失敗，ProcessId="
							 + processId + "、AssignmentId=" + assignmentId + "、result=" + approvalResult);
				 }
			 }
		 }catch(Exception ex){
			 message = "執行進貨流程發生錯誤，原因：" + ex.toString();
			 siProgramLogAction.createProgramLog(ImReceiveHeadMainService.PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imReceive.getLastUpdatedBy());
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


	 private void wrappedMessageBox(MessageBox msgBox, ImReceiveHead imReceiveBean, boolean isStartProcess, boolean isExecFunction){
		 String orderTypeCode  = imReceiveBean.getOrderTypeCode();
		 String orderNo        = imReceiveBean.getOrderNo();
		 String status         = imReceiveBean.getStatus();
		 String identification = orderTypeCode + "-" + orderNo;
		 Command cmd_ok = new Command();
		 if(isStartProcess){
			 msgBox.setType(MessageBox.CONFIRM);
			 //cmd_ok.setCmd(Command.HANDLER);
			 //cmd_ok.setParameters(new String[]{"main", "clearFormHandler"});
			 //cmd_ok.setParameters(new String[]{"refreshForm('')",""});
			 cmd_ok.setCmd(Command.FUNCTION);
			 cmd_ok.setParameters(new String[]{"createNewForm()",""});
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
			 returnMap = imReceiveHeadMainService.executeSearchInitial(parameterMap);	    
		 }catch (Exception ex) {
			 System.out.println("執行進貨單查詢初始化時發生錯誤，原因：" + ex.toString());
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
			 returnMap = imReceiveHeadMainService.getSearchSelection(parameterMap);	    
		 }catch (Exception ex) {
			 log.info("執行進貨單檢視時發生錯誤，原因：" + ex.toString());
			 MessageBox msgBox = new MessageBox();
			 msgBox.setMessage(ex.getMessage());
			 returnMap = new HashMap();
			 returnMap.put("vatMessage" ,msgBox);
		 }
		 return AjaxUtils.parseReturnDataToJSON(returnMap);	
	 }
	 
	 /**
	  * 執行儲位單 更新 與 比對
	  */
	 public void executeStorage(ImReceiveHead imReceiveHead) throws Exception {
		 
		 //更新儲位單頭 2011.11.11 by Caspar
		 Map storageMap = new HashMap();
		 storageMap.put("storageTransactionDate", "warehouseInDate");
		 storageMap.put("storageTransactionType", ImStorageService.IN);
		 storageMap.put("status", "warehouseStatus");
		 //if("IMR".equals(buOrderType.getTypeCode())){
			 //storageMap.put("deliveryWarehouseCode", "");
			 storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");
		 //}else{
			 //storageMap.put("deliveryWarehouseCode", "deliveryWarehouseCode");	
			 //storageMap.put("arrivalWarehouseCode", "");
		 //}
		 
		 ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, imReceiveHead);

		 //更新儲位單身與比對 2011.11.11 by Caspar
		 storageMap.put("beanItem", "imReceiveItems");
		 storageMap.put("quantity", "receiptQuantity");
		 imStorageAction.executeImStorageItem(storageMap, imReceiveHead, imStorageHead);
	 }

}