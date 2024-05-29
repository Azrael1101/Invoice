package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImWarehouseService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImWarehouseAction {
	
	private static final Log log = LogFactory.getLog(ImWarehouseAction.class);
    
    private ImWarehouseService imWarehouseService;
    
    public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}
	
	/**
	 * 查詢倉庫初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = imWarehouseService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行查詢倉庫初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 庫別初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performImWareHouseInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			log.info("庫別初始化");
			returnMap = imWarehouseService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行庫別維護存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	
	public List<Properties> performImWareHouseTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
			//Object formLinkBean = parameterMap.get("vatBeanFormLink");
			//Object otherBean = parameterMap.get("vatBeanOther");
			
			
			// 驗證
		//	imWarehouseService.checkImWarehouse(parameterMap);

			// 取得實際主檔
			imWarehouseService.executeFindActualImWareHouse(parameterMap);

			// 前端資料塞入bean
			imWarehouseService.updateImWareHouseBean(parameterMap);

			// 存檔							
			resultMap = imWarehouseService.updateAJAXImWarehouse(parameterMap);

			wrappedMessageBox(msgBox, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));
            

		} catch (ProcessFailedException px) {
			log.error("執行庫別維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行庫別維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);
		returnMap.put("bean", resultMap.get("entityBean"));
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param locationBean
	 * @param isStartProcess
	 */
	private void wrappedMessageBox(MessageBox msgBox, boolean isStartProcess){

		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", "","","",""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);
	}

}
