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
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.ImItemCategoryService;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuBasicDataAction {

	private static final Log log = LogFactory.getLog(BuBasicDataAction.class);	

	private BuCountryDAO buCountryDAO;

	private BuBasicDataService buBasicDataService;
	private ImItemCategoryService imItemCategoryService;

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	/**
	 * 商品類別查詢 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performItemCategorySearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = imItemCategoryService.executeSearchInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行商品類別查詢初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	/**
	 * 國別初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performBuCountryInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = buBasicDataService.executeBuCountryInitial(parameterMap);	    
		}catch (Exception ex) {
			log.error("執行國別維護存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	/**
	 * 國別送出
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performBuCountryTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			buBasicDataService.validateBuCountryHead(parameterMap);

			// 取得實際主檔
			buBasicDataService.executeFindActualBuCountry(parameterMap);

			// 前端資料塞入bean
			buBasicDataService.updateBuCountryBean(parameterMap);

			// 存檔							
			resultMap = buBasicDataService.updateAJAXBuCountry(parameterMap);

			wrappedMessageBox(msgBox, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


		} catch (ProcessFailedException px) {
			log.error("執行國別維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行國別維護單存檔失敗，原因：" + ex.toString());
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

	/**
	 * 幣別初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performBuCurrencyInitial(Map parameterMap){
		Map returnMap = null;	
		try{

			returnMap = buBasicDataService.executeBuCurrencyInitial(parameterMap);    
		}catch (Exception ex) {
			log.error("執行幣別維護存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	public List<Properties> performBuExchangeInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			System.out.println("匯率Action");
			returnMap = buBasicDataService.executeBuExchangeInitial(parameterMap);    
		}catch (Exception ex) {
			log.error("執行幣別維護存檔初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

	/**
	 * 幣別送出
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performBuCurrencyTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			buBasicDataService.validateBuCurrencyHead(parameterMap);
			// 取得實際主檔
			buBasicDataService.executeFindActualBuCurrency(parameterMap);

			// 前端資料塞入bean
			buBasicDataService.updateBuCurrencyBean(parameterMap);

			// 存檔							
			resultMap = buBasicDataService.updateAJAXBuCurrency(parameterMap);
			if(resultMap.get("isUpdate").toString().equals("0")){
				wrappedMessageBox(msgBox, true);
			}else{
				wrappedMessageBox(msgBox, false);	
			}
			msgBox.setMessage((String) resultMap.get("resultMsg"));


		} catch (ProcessFailedException px) {
			log.error("執行幣別維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行幣別維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	/**
	 * 匯率送出
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performBuExchangeRateTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			// 驗證
			buBasicDataService.validateBuExchangeRateHead(parameterMap);
			// 取得實際主檔
			buBasicDataService.executeFindActualBuExchange(parameterMap);

			// 前端資料塞入bean
			buBasicDataService.updateBuExchangeRateBean(parameterMap);

			// 存檔							
			resultMap = buBasicDataService.updateAJAXBuExchangeRate(parameterMap);

			wrappedMessageBox(msgBox, true);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


		} catch (ProcessFailedException px) {
			log.error("執行幣別維護單流程時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行幣別維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 清除LOG初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performBuDeleteLogInitial(Map parameterMap){
		Map returnMap = null;	
		try{

			returnMap = buBasicDataService.executeBuDeleteLogInitial(parameterMap);    
		}catch (Exception ex) {
			log.error("執行清除LOG初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	public List<Properties> performBuDeleteLogTransaction(Map parameterMap){ 

		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  		
			
			resultMap = buBasicDataService.saveBuDeleteLog(parameterMap);

			wrappedMessageBox(msgBox, false);

			msgBox.setMessage("存檔成功");


		} catch (ProcessFailedException px) {
			log.error("執行清除LOG作業時發生錯誤，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行清除LOG作業存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}

		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 更換Menu初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performChangeMenuInitial(Map parameterMap){
		Map returnMap = null;	
		try{

			returnMap = buBasicDataService.executeChangeMenuInitial(parameterMap);    
		}catch (Exception ex) {
			log.error("執行清除LOG初始化時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	
	
	/**
	 * 更換Menu送出
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	
	 public List<Properties> performChangeMenuTransaction(Map parameterMap){ 

			Map returnMap = new HashMap(0); 
			MessageBox msgBox = new MessageBox();
			Map resultMap = new HashMap(0); 
			try {  
			
				//Object formBindBean = parameterMap.get("vatBeanFormBind");
				//Object otherBean = parameterMap.get("vatBeanOther");

				// 驗證
				
				// 取得實際主檔
				

				// 前端資料塞入bean
				

				// 存檔							
				resultMap = buBasicDataService.updateAJAXbuChangeMenu(parameterMap);
					
				wrappedMessageBox(msgBox, true);

				msgBox.setMessage((String) resultMap.get("resultMsg"));



			} catch (ProcessFailedException px) {
				log.error("執行更換Menu流程時發生錯誤，原因：" + px.toString());
				msgBox.setMessage(px.getMessage());
			} catch (Exception ex) {
				log.error("執行更換Menu存檔失敗，原因：" + ex.toString());
				msgBox.setMessage(ex.getMessage());
			}

			returnMap.put("vatMessage", msgBox);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		}
	 
}
