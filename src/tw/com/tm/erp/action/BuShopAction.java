package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;

import tw.com.tm.erp.hbm.service.BuShopMainService;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuShopAction {

	private static final Log log = LogFactory.getLog(BuShopAction.class);
	private BuShopMainService buShopMainService;
    
    public void setBuShopMainService(BuShopMainService buShopMainService) {
		this.buShopMainService = buShopMainService;
	}
    public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			log.info("店別初始化");
			returnMap = buShopMainService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行店別維護存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
    public List<Properties> performTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
		
			Object otherBean = parameterMap.get("vatBeanOther");
	

			// 驗證 把畫面的值 存成BEAN 驗證
		//	buShopMainService.validateHead(parameterMap);

			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buShopMainService.executeFindActualShop(parameterMap); 

			// 前端資料塞入bean 
			buShopMainService.updateShopBean(parameterMap);

									
			// 存檔  改狀態
			resultMap = buShopMainService.updateAJAXBuShop(parameterMap);

			wrappedMessageBox(msgBox, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));
			
		} catch (ProcessFailedException px) {
			log.error("執行維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);

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
