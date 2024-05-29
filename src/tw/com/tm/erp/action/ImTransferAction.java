package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImTransfer;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
import tw.com.tm.erp.hbm.service.ImTransferService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

public class ImTransferAction {

	private static final Log log = LogFactory.getLog(ImTransferAction.class);

	public static final String PROGRAM_ID = "IM_TRANSFER";

	// private ImItemService imItemService;

	private ImTransferService imTransferService;

	// public void setImItemService(ImItemService imItemService) {
	// this.imItemService = imItemService;
	// }

	public void setImTransferService(ImTransferService imTransferService) {
		this.imTransferService = imTransferService;
	}

	

	/**前台
	 * 
	 * 
	 * @param parameterMap
	 * @return
	 */	
	
	public List<Properties> performTransaction1(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		
		
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		
		
		
		try {
			
			Object otherBean = parameterMap.get("vatBeanOther");
			resultMap = imTransferService.updateAJAXImTransfer1(parameterMap);
			msgBox.setType(MessageBox.ALERT);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			ImTransfer imTransfer = (ImTransfer) resultMap.get("entityBean");
			msgBox.setMessage("執行追加單新增存檔成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行追加單新增存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		 returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**後台
	 * 
	 * 
	 * @param parameterMap
	 * @return
	 */
	public List<Properties> performImTransferTransaction(Map parameterMap){ 
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
			
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object formBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
						

			// 流程控制，避免重複流程造成的錯誤
			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			Long assignmentId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "assignmentId"));
			// 確認是否允許流程，不允許會丟例外
			//ProcessHandling.checkedCompleteAssignment(NumberUtils.getLong(imTransfers.getProcessId()), processId,
			//		assignmentId);
			
			System.out.println(processId + "====aaaa=====");
			
			if ("0".equals(String.valueOf(processId))) {
				resultMap = imTransferService.updateAJAXimTransfer(parameterMap);
				
				
				
				String orderNo =(String)resultMap.get("orderNo");
				String orderType =(String)resultMap.get("orderType");
				List<ImTransfer> imTransfers = (List<ImTransfer>)resultMap.get("entityBean");
				
				
			for (Iterator iterator = imTransfers.iterator(); iterator.hasNext();) {
				ImTransfer imTransfer = (ImTransfer) iterator.next();
				
				Object processObj[] =  ImTransferService.startProcess(imTransfer);
				
				break;
			 }
			}
			else{
				System.out.println("====vvvvv=====");
				
				if (StringUtils.hasText(String.valueOf(assignmentId))&& !("0".equals(String.valueOf(assignmentId)))) {
				    Long assignId = NumberUtils.getLong(assignmentId);
				    Boolean result = Boolean.valueOf("true");
				    Object[] processInfo = ImTransferService.completeAssignment(assignId, true);
				    
				    }
			}
			
			
			 

			
			
			
			
			
			//wrappedMessageBox(msgBox, null, true);
			msgBox.setMessage("送出完成");
		} catch (ProcessFailedException px) {
			log.error("執行公司維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行公司維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}



	//====================================================================================================================
	
	//public List<Properties> performTransactionlist(Map parameterMap) {
		//Map returnMap = new HashMap(0);
		//MessageBox msgBox = new MessageBox();
		//Map resultMap = new HashMap(0);
		//try {
			//Object otherBean = parameterMap.get("vatBeanOther");
			//resultMap = imTransferService.updateAJAXImTransfer(parameterMap);
			//msgBox.setType(MessageBox.ALERT);
			//msgBox.setMessage((String) resultMap.get("resultMsg"));
			//ImTransfer imTransfer = (ImTransfer) resultMap.get("entityBean");
			//msgBox.setMessage("Service3:執行追加單後台新增存檔成功");
		//} catch (Exception ex) {
			//ex.printStackTrace();
			//log.error("Service3:執行追加單後台存檔失敗，原因：" + ex.toString());
			//msgBox.setMessage(ex.getMessage());
		//}
		 //returnMap.put("vatMessage", msgBox);
		//return AjaxUtils.parseReturnDataToJSON(returnMap);
	//}

	
	
	
	
	//====================================================================================================================
	
	/**
	 * 綑綁MESSAGE
	 * 
	 * @param msgBox
	 * @param locationBean
	 * @param isStartProcess
	 */
	private void wrappedMessageBox(MessageBox msgBox, ImItem imItem,
			boolean isStartProcess) {

		Command cmd_ok = new Command();
		if (isStartProcess) {
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[] {
					"createRefreshForm('" + imItem.getItemId() + "')", "" });
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);
	}

	
	
	
	private void executeProcess(Object otherBean, Map resultMap, ImTransfer imTransfer, MessageBox msgBox) throws Exception {
		 String message = null;
		  
		 try{
			 

			 
				 Object processObj[] =  ImTransferService.startProcess(imTransfer);  
				 
				
			
		 }catch(Exception ex){
			
		 }
	 }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
