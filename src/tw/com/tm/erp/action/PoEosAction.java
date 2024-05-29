package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.utils.AjaxUtils;

import tw.com.tm.erp.hbm.service.PoEosService;



public class PoEosAction
{
	//Log
	private static final Log log = LogFactory.getLog(PoEosAction.class);

	private PoEosService poEosService;
	public void setPoEosService(PoEosService poEosService)
	{
		this.poEosService = poEosService;
	}

	public List<Properties> performTransaction (Map parameterMap){

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try
		{  
			Object otherBean = parameterMap.get("vatBeanOther");
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			if(formAction.equals("SHIP")){
				poEosService.executeFindHead(parameterMap); 
				resultMap = poEosService.executeShip(parameterMap);
				wrappedMessageBox(msgBox, poEosService,1, true , false);
			}
			else{
				log.info("1.驗證 把畫面的值 存成BEAN 驗證");
				// 驗證 把畫面的值 存成BEAN 驗證
				poEosService.validate(parameterMap);//判斷倉庫是否在出貨
	
				log.info("2.驗證完成 生成Unit");
				// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
				poEosService.executeFindHead(parameterMap); 
	
				log.info("3.前端資料塞入bean");
				// 前端資料塞入bean 
				poEosService.updateHeadBean(parameterMap);
	
				log.info("4.存檔 改狀態");
				// 存檔  改狀態
				resultMap = poEosService.updateAJAX(parameterMap);
				log.info("5.存檔完成 送出訊息");
				//存檔完成 送出訊息
				wrappedMessageBox(msgBox, poEosService,2, true , false);
			}
			
			
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



	/**
	 * 單位建立頁面初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performInitial(Map parameterMap)
	{
		log.info("初始化:Action");
		Map returnMap = null;	
		try
		{
			returnMap = poEosService.executeInitial(parameterMap);
		}
		catch (Exception ex)
		{
			log.error("執行單位建立初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	public List<Properties> performSearchDetailInitial(Map parameterMap)
	{
		log.info("初始化:Action");
		Map returnMap = null;	
		try
		{
			returnMap = poEosService.executeSearchDetailInitial(parameterMap);
		}
		catch (Exception ex)
		{
			log.error("執行單位建立初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	/**
	 * 查詢EOS初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			   returnMap = poEosService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行查詢倉庫初始化時發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
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
			returnMap = poEosService.saveSearchSelection(parameterMap);	    
		}catch (Exception ex) {
			log.info("執行採購單明細檢視時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	private void wrappedMessageBox(MessageBox msgBox,PoEosService buUnitService ,int div, boolean isStartProcess, boolean isExecFunction)
	{

		log.info("Message Box");
//		String supplierCode = buSupplierMod.getSupplierCode();
//		String supplierTypeCode = buSupplierMod.getSupplierTypeCode();
//		String status = buSupplierMod.getStatus();
//		String identification = orderTypeCode + "-" + orderNo;
		
		
		
		
		String f = "createRefreshForm()";
		f=(div==1)?"refreshFromWithHeadId()":"createRefreshForm()";

		
		Command cmd_ok = new Command();
		if(isStartProcess){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{f, ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{f, ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);

	}


}