package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementOrderTypeSetting;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImMovementOrderTypeSettingService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImMovementOrderTypeSettingAction {

	private static final Log log = LogFactory.getLog(ImMovementOrderTypeSettingAction.class);
	
	private ImMovementOrderTypeSettingService imMovementOrderTypeSettingService;
	
	public void setImMovementOrderTypeSettingService(ImMovementOrderTypeSettingService imMovementOrderTypeSettingService){
		this.imMovementOrderTypeSettingService = imMovementOrderTypeSettingService;
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
			returnMap = imMovementOrderTypeSettingService.executeInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行抽成率初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		} 
		return AjaxUtils.parseReturnDataToJSON(returnMap);	

	}
	
	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			imMovementOrderTypeSettingService.validateHead(parameterMap);

			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			imMovementOrderTypeSettingService.executeFindActual(parameterMap); 

			// 前端資料塞入bean
			imMovementOrderTypeSettingService.updateImMovementOrderTypeSettingBean(parameterMap);

			// 存檔							
			resultMap = imMovementOrderTypeSettingService.updateAJAXImMovementOrderSetting(parameterMap);

			ImMovementOrderTypeSetting imMovementOrderTypeSetting = (ImMovementOrderTypeSetting) resultMap.get("entityBean");

			wrappedMessageBox(msgBox, imMovementOrderTypeSetting, true);

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
	 * @param ImMovementOrderTypeSettingBean
	 * @param isStartProcess
	 */
	private void wrappedMessageBox(MessageBox msgBox, ImMovementOrderTypeSetting imMovementBean,
			boolean isStartProcess) {
		String orderTypeCode = imMovementBean.getOrderTypeCode();
		Command cmd_ok = new Command();

		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			msgBox.setMessage(orderTypeCode + "表單已送出！");
			cmd_ok.setCmd(Command.WIN_CLOSE);	       
		}

		msgBox.setOk(cmd_ok);
	}
	
	
	
	
	
}