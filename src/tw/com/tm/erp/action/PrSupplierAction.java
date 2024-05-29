package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.PrSupplierMod;
import tw.com.tm.erp.hbm.bean.MessageBox;
//import tw.com.tm.erp.hbm.dao.PrSupplierDAO;
import tw.com.tm.erp.hbm.service.PrSupplierService;
import tw.com.tm.erp.utils.AjaxUtils;


public class PrSupplierAction {	
	private static final Log log = LogFactory.getLog(PrSupplierAction.class);	
	private PrSupplierService prSupplierService;

	public void setPrSupplierService(PrSupplierService prSupplierService) {
		this.prSupplierService = prSupplierService;
	}
	
	/**
	 * 資訊廠商初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 * 實作List介面的集合物件
	 * 將頁面LIST類別物件資訊放到Map parameterMap中,先在Map returnMap畫出一個位置,
	 * returnMaP的位置 buCompanyService.executeBuCompanyInitial,放著parameterMap的物件
	 * returnMaP畫出空間叫HashMap
	 */ 
	public List<Properties> performPrSupplierInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    /* parameterMap傳入參數到buCompanyService.executeBuCompanyInitial,returnMap物件中  */
		    returnMap = prSupplierService.executePrSupplierInitial(parameterMap);			    
		}catch (Exception ex) {
		    log.error("執行資訊廠商存檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/**
	 * 送出
	 * @param parameterMap
	 * @return
	 */  
	public List<Properties> performModTransaction(Map parameterMap){
    	
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap();
		Object otherBean = null;
		
		try {
		
			otherBean = parameterMap.get("vatBeanOther");
			
			// 驗證 把畫面的值 存成BEAN 驗證
			prSupplierService.validateHead(parameterMap);
			// 驗對則存到資料庫(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			prSupplierService.executeFindActualMod(parameterMap);
			// 前端資料塞入bean 
			prSupplierService.updatePrSupplierModBean(parameterMap);
			// 存檔  改狀態
			resultMap = prSupplierService.updateAJAXImItemDiscountMod(parameterMap);
	
			wrappedMessageBox(msgBox, true , false);

			msgBox.setMessage((String) resultMap.get("resultMsg"));


			//executeProcess(otherBean, resultMap, imItemDiscountMod, msgBox);

		} catch( ProcessFailedException px ){
			log.error("執行商品折扣檢核失敗，原因：" + px.toString());
			msgBox.setMessage(px.getMessage());
			
		} catch (Exception ex) {
			log.error("執行商品折扣存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }


    /**
	 * 綑綁MESSAGE 
	 * @param msgBox
	 * @param 
	 * @param isStartProcess
	 */
	
	private void wrappedMessageBox(MessageBox msgBox, boolean isStartProcess, boolean isExecFunction){

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
	
	/**
	 * 初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 */
	public List<Properties> performPrSupplierSearchInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = prSupplierService.searchExecuteInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行商品折扣檔初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}

}
