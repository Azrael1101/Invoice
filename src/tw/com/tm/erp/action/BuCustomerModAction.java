package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuCustomerMod;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuCustomerModService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuCustomerModAction {

	private static final Log log = LogFactory.getLog(BuCustomerModAction.class);
	
	private BuCustomerModService buCustomerModService;
	private BuCustomerMod buCustomerMod;
	public void setBuCustomerModService(BuCustomerModService buCustomerModService){
		this.buCustomerModService = buCustomerModService;
	}
	public void setBuCustomerMod(BuCustomerMod buCustomerMod){
		this.buCustomerMod = buCustomerMod;
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
			log.info("會員建立/變更初始化!!");
			returnMap = buCustomerModService.executeInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		} 
		return AjaxUtils.parseReturnDataToJSON(returnMap);	

	}
	public List<Properties> performPosInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			log.info("會員建立/變更初始化!!");
			returnMap = buCustomerModService.executePosInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		} 
		return AjaxUtils.parseReturnDataToJSON(returnMap);	

	}
	public List<Properties> searchPerformInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			log.info("會員建立/變更初始化!!");
			returnMap = buCustomerModService.executeSearchInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
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


			// 驗證 把畫面的值 存成BEAN 驗證
			buCustomerModService.validateHead(parameterMap);

			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buCustomerModService.executeFindActualMod(parameterMap); 

			// 前端資料塞入bean 
			buCustomerModService.updateCustomerModBean(parameterMap);

									
			// 存檔  改狀態
			resultMap = buCustomerModService.updateAJAXBuCustomerMod(parameterMap);

			BuCustomerMod buCustomerMod = (BuCustomerMod) resultMap.get("entityBean");
			wrappedMessageBox(msgBox, buCustomerMod, true , false);

			msgBox.setMessage((String) resultMap.get("resultMsg"));
		}
		catch (ProcessFailedException px) 
		{
			log.error("執行維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		}
		catch (Exception ex) 
		{
			log.error("執行維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	public List<Properties> performPosTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  


			// 驗證 把畫面的值 存成BEAN 驗證
			buCustomerModService.posSysValidateHead(parameterMap);

			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buCustomerModService.executeFindActualMod(parameterMap); 

			// 前端資料塞入bean 
			buCustomerModService.updateCustomerModBean(parameterMap);

									
			// 存檔  改狀態
			resultMap = buCustomerModService.updateAJAXBuCustomerMod(parameterMap);

			BuCustomerMod buCustomerMod = (BuCustomerMod) resultMap.get("entityBean");
			wrappedMessageBoxPos(msgBox, checkAutoAdd(parameterMap));

			msgBox.setMessage((String) resultMap.get("resultMsg"));
		}
		catch (ProcessFailedException px) 
		{
			log.error("執行維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		}
		catch (Exception ex) 
		{
			log.error("執行維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);

		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	private void wrappedMessageBox(MessageBox msgBox,BuCustomerMod buCustomerMod , boolean isStartProcess, boolean isExecFunction){

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
	
	private void wrappedMessageBoxPos(MessageBox msgBox, Boolean autoAdd){

		log.info("wrappedMessageBoxPos...............");

		Command cmd_ok = new Command();
		msgBox.setType(MessageBox.ALERT);
		if(autoAdd){
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
		}else{
			cmd_ok.setCmd(Command.WIN_CLOSE);
		}
		msgBox.setOk(cmd_ok);
	}
	
	private Boolean checkAutoAdd(Map parameterMap) throws Exception {
		Boolean result ;
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
		if("T1GS".equals(brandCode)){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
}
