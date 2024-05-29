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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImStorageItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.service.ImStorageService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImStorageAction {

	private static final Log log = LogFactory.getLog(ImStorageAction.class);

	public static final String PROGRAM_ID = "IM_STORAGE";

	private ImStorageService imStorageService;
	private SiProgramLogAction siProgramLogAction;
	private WfApprovalResultService wfApprovalResultService;
	private BaseDAO baseDAO;
	

	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
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
			returnMap = imStorageService.executeInitial(parameterMap); 
		} catch (Exception ex) {
			System.out.println("執行儲位單初始化時發生錯誤，原因：" + ex.toString());
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
			Long processId = NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean, "processId"));
			
			if(!StringUtils.hasText(formStatus))
				throw new Exception("單據狀態參數為空值，無法執行存檔！");	
			if(!StringUtils.hasText(beforeChangeStatus))
				throw new Exception("原單據狀態參數為空值，無法執行存檔！");

			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			ImStorageHead imStorageHead = imStorageService.getBeanByHeadId(formLinkBean);
			returnMap.put(AjaxUtils.AJAX_FORM, imStorageHead);

			String identification = imStorageService.getIdentification(parameterMap, returnMap);
			returnMap.put(AjaxUtils.IDENTIFICATION, identification);

			imStorageService.updateHeadBean(parameterMap, returnMap);
			if(OrderStatus.SAVE.equals(beforeChangeStatus) && OrderStatus.FINISH.equals(formStatus)){
				imStorageService.check(parameterMap, returnMap);
				if(imStorageService.deleteProgramLog(identification, 1) != 0 ){
					throw new FormException(MessageStatus.VALIDATION_FAILURE);
				}
			}
			
			imStorageService.updateAfterCheck(parameterMap, returnMap);
				
			//}else if (OrderStatus.SAVE.equals(formStatus)){
			//	msgBox.setMessage("儲位單暫存成功");
			//}else if (OrderStatus.VOID.equals(formStatus)){
			//	msgBox.setMessage("儲位單作廢成功");
			//}else{
			//	throw new Exception("查無對應方法");
			//}
			
			executeProcess(otherBean, imStorageHead, msgBox);
			
			//最後再度更新一次
			if(processId == 0)
				imStorageService.update(imStorageHead);
			
		}catch (FormException ex) {
			log.error("FormException 執行儲位單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage("執行儲位單存檔失敗");
			Command cmd_bf = new Command();
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[]{"showMessage()", ""});
			msgBox.setOk(cmd_bf);
		}catch (Exception ex) {
			log.error("Exception 執行儲位單存檔失敗，原因：" + ex.getMessage());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	/**
	 * 綑綁回前畫面的視窗
	 * @param msgBox
	 * @param head
	 * @param isStartProcess
	 * @param isExecFunction
	 */
	private void wrappedMessageBox(MessageBox msgBox, ImStorageHead head, boolean isStartProcess, boolean isExecFunction ){

		String status = head.getStatus();
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()",""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
			msgBox.setMessage("儲位單已成功儲存，請問是否繼續新增");
		}else{
			msgBox.setMessage("儲位單" + head.getOrderNo() + "已" + OrderStatus.getChineseWord(status) + "！");
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
	 * 儲位單起流程
	 *
	 * @param otherBean
	 * @param resultMap
	 * @param imMovement
	 * @param msgBox
	 * @throws Exception
	 */
	private void executeProcess(Object otherBean, ImStorageHead imStorageHead, MessageBox msgBox) throws Exception {
		log.info("executeProcess");
		String message = null;
		try {
			String processId = (String) PropertyUtils.getProperty(otherBean, "processId");
			log.info("processId = " + processId);
			String assignmentId = (String) PropertyUtils.getProperty(otherBean, "assignmentId");
			log.info("assignmentId = " + assignmentId);
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");
			log.info("approvalResult = " + approvalResult);
			// String approvalResult = (String)PropertyUtils.getProperty(otherBean, "approvalResult");
			String approvalComment = (String) PropertyUtils.getProperty(otherBean, "approvalComment");
			log.info("approvalComment = " + approvalComment);
			// String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			log.info("beforeChangeStatus = " + beforeChangeStatus);
			String formStatus = imStorageHead.getStatus();
			String approvalResultMsg = "OK";
			
			log.info("processId=" + processId + " assignmentId=" + assignmentId + " approvalResult=" + approvalResult
					+ " approvalComment=" + approvalComment + " beforeChangeStatus=" + beforeChangeStatus + " formStatus="
					+ formStatus + " approvalResultMsg=" + approvalResultMsg);
			
			if (!StringUtils.hasText(processId) || "0".equals(processId)) {
				if (msgBox != null)
					wrappedMessageBox(msgBox, imStorageHead, true, false);
				Object[] processObj = ImStorageService.startProcess(imStorageHead); // 起流程後，取回流程的processId
				// 起流程後，紀錄PROCESS_ID，避免之後重複起同一個流程
				log.info("流程 起始 id ======> " + processObj[0]);
				imStorageHead.setProcessId((Long) processObj[0]);
			} else {
				if (msgBox != null)
					wrappedMessageBox(msgBox, imStorageHead, false, false);
				if (StringUtils.hasText(assignmentId)) {
					Long assignId = NumberUtils.getLong(assignmentId);
					Boolean result = Boolean.valueOf(approvalResult);
					Object[] processInfo = ImStorageService.completeAssignment(assignId, result);
					log.info("流程 起始 id >>>>>>> " + processInfo[0]);
				} else {
					throw new ProcessFailedException("Complete assignment失敗，ProcessId=" + processId + "、AssignmentId="
							+ assignmentId + "、result=" + approvalResult);
				}
			}
		} catch (Exception ex) {
			message = "執行儲位單流程發生錯誤，原因：" + ex.toString();
			log.error(message);
		}
	}
	
	//---------------------------------------- ↓↓↓ 各單據呼叫儲位單 ↓↓↓ ----------------------------------------//

	/**
	 * 建立/更新儲位單頭
	 */
	public ImStorageHead executeImStorageHead(Map storageMap, Object objHead) throws StorageException {
		log.info("executeImStorageHead");
		ImStorageHead imStorageHead = null;
		//建立/更新儲位單頭
		imStorageHead = imStorageService.executeImStorageHead(storageMap, objHead);
		return imStorageHead;
	}
	
	/**
	 * 建立/更新儲位單頭
	 */
	public void executeImStorageItem(Map storageMap, Object objHead, ImStorageHead imStorageHead) throws StorageException {
		
		log.info("executeImStorageItem");
		
		String status = imStorageHead.getStatus();
		log.info("imStorageHead.getStatus() = " + status);
		
		if(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || OrderStatus.UNCONFIRMED.equals(status) ||
				null == imStorageHead.getImStorageItems() || 0 == imStorageHead.getImStorageItems().size()){
			
			//建立/更新儲位單明細
			imStorageService.executeImStorageItem(storageMap, objHead, imStorageHead, false);

			//如果有轉出庫，展儲位
			//if(StringUtils.hasText(imStorageHead.getDeliveryWarehouseCode())){
				imStorageService.executeImStorageItemExtend(storageMap, imStorageHead, "N");
				imStorageHead = imStorageService.getBeanByHeadId(imStorageHead.getStorageHeadId());
			//}

			//重新設置明細的儲位、進貨日、批號資訊
			imStorageHead = imStorageService.updateResetImStorageItem(imStorageHead, null);

		}
		
		//比對儲位單明細
		imStorageService.compareImStroage(storageMap, objHead, imStorageHead);
		
		imStorageService.updateHead(imStorageHead, imStorageHead.getLastUpdatedBy());
	}
	
	
	/**
	 * 明細中eventService(自定事件) , 建立/更新儲位單身(展儲位)
	 */
	public List<Properties> executeAJAXImStorageItem (Properties httpRequest) throws StorageException {
		List re = new ArrayList();
		Properties pro = new Properties();
		try{
			log.info("executeAJAXImStorageItem");
			Long storageHeadId = NumberUtils.getLong(httpRequest.getProperty("storageHeadId"));
			ImStorageHead imStorageHead = imStorageService.getBeanByHeadId(storageHeadId);
			if(null == imStorageHead)
				throw new StorageException("查無storageHeadId：" + storageHeadId + " 對應之儲位單");
			
			String storageStatus = httpRequest.getProperty("storageStatus");
			
			log.info("storageStatus = " + storageStatus);
			
			if(OrderStatus.SAVE.equals(storageStatus) || OrderStatus.REJECT.equals(storageStatus) ||  OrderStatus.UNCONFIRMED.equals(storageStatus) || 
					null == imStorageHead.getImStorageItems() || 0 == imStorageHead.getImStorageItems().size()){
				
				String deliveryWarehouseCode = httpRequest.getProperty("deliveryWarehouseCode");
				String arrivalWarehouseCode = httpRequest.getProperty("arrivalWarehouseCode");
				String pickOrderTypeCode = httpRequest.getProperty("pickOrderTypeCode");
				String pickOrderNo = httpRequest.getProperty("pickOrderNo");
				String inLotNo = httpRequest.getProperty("warehouseInDate");
				String isClean = httpRequest.getProperty("isClean");
				
				if(StringUtils.hasText(inLotNo))
					inLotNo = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, inLotNo), DateUtils.C_DATA_PATTON_YYYYMMDD);
				
				//if(!StringUtils.hasText(isClean))
				    	//isClean = "Y";
				
				imStorageHead.setDeliveryWarehouseCode(deliveryWarehouseCode);
				imStorageHead.setArrivalWarehouseCode(arrivalWarehouseCode);
				
				Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
				
				String storageTransactionType = httpRequest.getProperty("storageTransactionType");
				String beanItem = httpRequest.getProperty("beanItem");
				String quantity = httpRequest.getProperty("quantity");

				Map storageMap = new HashMap();
				storageMap.put("storageTransactionType", storageTransactionType);
				storageMap.put("beanItem", beanItem);
				storageMap.put("quantity", quantity);
				storageMap.put("pickOrderTypeCode", pickOrderTypeCode);
				storageMap.put("pickOrderNo", pickOrderNo);
				
				if(0 != headId){
					String beanHead = httpRequest.getProperty("beanHead");
					Object objHead = baseDAO.findById(beanHead, headId);

					//建立/更新儲位單明細
					imStorageService.executeImStorageItem(storageMap, objHead, imStorageHead, true);
				}
				
				//展儲位
				//if(StringUtils.hasText(imStorageHead.getDeliveryWarehouseCode())){
				imStorageService.executeImStorageItemExtend(storageMap, imStorageHead, isClean);
				//}
				
				imStorageHead = imStorageService.getBeanByHeadId(imStorageHead.getStorageHeadId());

				//重新設置明細的儲位、進貨日、批號資訊
				imStorageService.updateResetImStorageItem(imStorageHead, inLotNo);
			}
			
		}catch(StorageException e){
			e.printStackTrace();
			pro.setProperty("errorMsg", "展出儲位明細發生錯誤，原因：" + e.getMessage());
		}
		re.add(pro);
		return re;
	}
	
	
	/**
	 * 取得儲位正式單號，並更新來源單號<br>
	 * Object objHead 
	 */
	public ImStorageHead updateOrderNo(Object objHead) throws Exception {
		return imStorageService.updateOrderNo(objHead);
	}
	
	public List<Properties> deleteImStorageItem(Properties httpRequest) throws StorageException {
		List re = new ArrayList();
		Properties pro = new Properties();
		try{
		Long storageHeadId = NumberUtils.getLong(httpRequest.getProperty("storageHeadId"));
		log.info("執行刪除除未明戲測試:"+storageHeadId);
		ImStorageHead imStorageHead = imStorageService.getBeanByHeadId(storageHeadId);
		log.info("取得儲位頭:"+imStorageHead.getBrandCode());
		List<ImStorageItem> imStorageItems =imStorageHead.getImStorageItems();
		log.info("刪除前數量:"+imStorageItems.size());
		log.info("imStorageHead.getStatus():"+imStorageHead.getStatus());
		for(int i = imStorageItems.size()-1; i >= 0 ; i--){
			imStorageItems.remove(i);
		}
		log.info("刪除後數量:"+imStorageItems.size());
		imStorageHead.setImStorageItems(imStorageItems);
		log.info("執行刪除明細");
		baseDAO.update(imStorageHead);
		}catch(StorageException e){
			e.printStackTrace();
			pro.setProperty("errorMsg", "展出儲位明細發生錯誤，原因：" + e.getMessage());
		}
		re.add(pro);
		return re;
	}
	
	
	//---------------------------------------- ↓↓↓ Search ↓↓↓ ----------------------------------------//
	public List<Properties> performSearchInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = imStorageService.executeSearchInitial(parameterMap); 
		} catch (Exception ex) {
			System.out.println("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	
	//---------------------------------------- ↓↓↓ OnHandSearch ↓↓↓ ----------------------------------------//
	public List<Properties> performOnHandInitial(Map parameterMap) {
		Map returnMap = null;
		try {
			returnMap = imStorageService.executeOnHandInitial(parameterMap); 
		} catch (Exception ex) {
			System.out.println("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage("執行儲位庫存查詢初始化時發生錯誤，原因：" + ex.getMessage());
			returnMap.put("vatMessage", "msgBox");
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 判斷是否要執行儲位
	 * @param Object bean
	 * @return
	 * @throws Exception
	 */
	public boolean isStorageExecute(Object objHead) throws Exception{
		return imStorageService.isStorageExecute(objHead);
	}
}
