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
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CustomsUploadSettingService;
import tw.com.tm.erp.utils.AjaxUtils;


public class CustomsUploadSettingAction {	
	private static final Log log = LogFactory.getLog(CustomsUploadSettingAction.class);	

	CustomsUploadSettingService customsUploadSettingService;
	
	public void setCustomsUploadSettingService(CustomsUploadSettingService customsUploadSettingService){
		this.customsUploadSettingService = customsUploadSettingService;
	}
	/**
	 * 公司初始化
	 * 
	 * @param parameterMap
	 * @return List<Properties>
	 * 實作List介面的集合物件
	 * 將頁面LIST類別物件資訊放到Map parameterMap中,先在Map returnMap畫出一個位置,
	 * returnMaP的位置 buCompanyService.executeBuCompanyInitial,放著parameterMap的物件
	 * returnMaP畫出空間叫HashMap
	 */ 
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = customsUploadSettingService.executeInitial(parameterMap);			    
		}catch (Exception ex) {
		    log.error("海關上傳設定初始化時發生錯誤，原因：" + ex.toString());
		    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
	
	/**
	 * 公司送出
	 * @param parameterMap 
	 * 	 * @return
	 * 
	 */
	public List<Properties> performTransaction(Map parameterMap){ 
	    Map returnMap = new HashMap(0); 
	    MessageBox msgBox = new MessageBox();
	    Map resultMap = new HashMap(0); 
	    try {

		//驗證
		//buCompanyService.validateBuCompanyHead(parameterMap);
		// 取得實際主檔
		//buCompanyService.executeFindActualBuCompany(parameterMap);
		// 前端資料塞入bean
		//buCompanyService.updateBuCompanyBean(parameterMap);
		//存檔
		resultMap = customsUploadSettingService.updateAJAX(parameterMap);
		
		wrappedMessageBox(msgBox, true);
		msgBox.setMessage((String) resultMap.get("resultMsg"));
	    } catch (ProcessFailedException px) {
		log.error("執行海關上傳設定更新時發生錯誤，原因：" + px.toString());
		msgBox.setMessage(px.getMessage());
	    } catch (Exception ex) {
		ex.printStackTrace();
		log.error("執行海關上傳設定更新失敗，原因：" + ex.toString());
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
		cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
		Command cmd_cancel = new Command();
		cmd_cancel.setCmd(Command.WIN_CLOSE);
		msgBox.setCancel(cmd_cancel);
	    }
	    msgBox.setOk(cmd_ok);
	}

}
