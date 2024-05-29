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
import tw.com.tm.erp.utils.AjaxUtils;
//Service
import tw.com.tm.erp.hbm.service.BuTruckService;
//DAO
import tw.com.tm.erp.hbm.dao.BuTruckDAO;
//Bean
import tw.com.tm.erp.hbm.bean.BuTruckMod;






public class BuTruckAction {
	private static final Log log = LogFactory.getLog(BuTruckAction.class);
//Service
	BuTruckService buTruckService;
	public void setBuTruckService(BuTruckService buTruckService)
	{
		this.buTruckService = buTruckService;
	}
//DAO
	BuTruckDAO buTruckDAO;
	public void setBuTruckDAO(BuTruckDAO buTruckDAO)
	{
		this.buTruckDAO = buTruckDAO;
	}
//Bean
	BuTruckMod buTruckMod;
	public void setBuTruckMod(BuTruckMod buTruckMod)
	{
		this.buTruckMod = buTruckMod;
	}
	
	/**
	 * 保卡初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performBuTruckInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = buTruckService.executeBuTruckInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行保卡存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 保卡送出
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performBuTruckTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  


			// 驗證
			//log.info("驗證");
			//buTruckService.validateBuTruckHead(parameterMap);
			
			
			// 取得實際主檔
			log.info("取得實際主檔");
			buTruckService.executeFindActualBuTruck(parameterMap);

			// 前端資料塞入bean
			log.info("前端資料塞入bean");
			buTruckService.updateBuTruckBean(parameterMap);

			// 存檔	
			log.info("存檔");
			resultMap = buTruckService.updateAJAXBuTruck(parameterMap);

			wrappedMessageBox(msgBox, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


		} catch (ProcessFailedException px) {
			log.error("執行保卡維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行保卡維護單存檔失敗，原因：" + ex.toString());
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
		}else{
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"createRefreshForm()", "","","",""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);
	}
}