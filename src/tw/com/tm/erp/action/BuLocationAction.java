package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuLocationService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuLocationAction {
	
	private static final Log log = LogFactory.getLog(BuLocationAction.class);	
	
	private BuLocationService buLocationService;
	
	public void setBuLocationService(BuLocationService buLocationService) {
		this.buLocationService = buLocationService;
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
		    returnMap = buLocationService.executeInitial(parameterMap);	    
		}catch (Exception ex) {
		    log.error("執行地點維護存檔初始化時發生錯誤，原因：" + ex.toString());
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
			buLocationService.validateHead(parameterMap);
			
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			buLocationService.executeFindActualBuLocation(parameterMap); 
			
			// 前端資料塞入bean
			buLocationService.updateBuLocationBean(parameterMap);
			
			// 存檔							
			resultMap = buLocationService.updateAJAXBuLocation(parameterMap);
  		
	  		BuLocation buLocation = (BuLocation) resultMap.get("entityBean");
	  		
	  		wrappedMessageBox(msgBox, buLocation, true);
	  		
	  		msgBox.setMessage((String) resultMap.get("resultMsg"));
  		
  		
	  	} catch (ProcessFailedException px) {
	  		log.error("執行地點維護單流程時發生錯誤，原因：" + px.toString());
	  		msgBox.setMessage(px.getMessage());
	  	} catch (Exception ex) {
	  		log.error("執行地點維護單存檔失敗，原因：" + ex.toString());
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
	private void wrappedMessageBox(MessageBox msgBox, BuLocation locationBean, boolean isStartProcess){
  	
	  	Long locationId = locationBean.getLocationId();
	  	
	      Command cmd_ok = new Command();
	  	if(isStartProcess){
	  	    msgBox.setType(MessageBox.CONFIRM);
	  	    cmd_ok.setCmd(Command.FUNCTION);
	  	    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
	  	    Command cmd_cancel = new Command();
	  	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	  	    msgBox.setCancel(cmd_cancel);
	  	}else{
	        msgBox.setMessage(locationId + "表單已送出！");
	  	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
	  	}
	  	
	  	msgBox.setOk(cmd_ok);
	}

	
}
