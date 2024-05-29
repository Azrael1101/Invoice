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
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.service.BuLocationService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.SiFunctionService;
import tw.com.tm.erp.hbm.service.SiMenuService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class SiFunctionAction {
	
	private static final Log log = LogFactory.getLog(SiFunctionAction.class);	
	
	private SiFunctionService siFunctionService;
	
	public void setSiFunctionService(
			SiFunctionService siFunctionService) {
		this.siFunctionService = siFunctionService;
	}

	
	/**
	 * 
	 * @param parameterMap 
	 * @return
	 */
	public List<Properties> performTransaction(Map parameterMap){ //List<Properties>
		
  	Map returnMap = new HashMap(0); 
  	MessageBox msgBox = new MessageBox();
		try {  
			
			log.info( "送入後端開始" );
  		HashMap resultMap = siFunctionService.updateAJAXMenu(parameterMap);
  		log.info( "後端處理結束" );
  		
  		SiFunction siFunction = (SiFunction) resultMap.get("entityBean");
  		Object otherBean = parameterMap.get("vatBeanOther");
  		String resultMsg = (String) resultMap.get("resultMsg");
  		
  		// 封裝顯示前端pop視窗形式與訊息
  		executeProcess(siFunction, msgBox, resultMsg);
  		
  	} catch (ProcessFailedException px) {
  		log.error("執行系統功能單流程時發生錯誤，原因：" + px.toString());
  		msgBox.setMessage(px.getMessage());
  	} catch (Exception ex) {
  		log.error("執行系統功能單存檔失敗，原因：" + ex.toString());
  		msgBox.setMessage(ex.getMessage());
  	}
  	
  	returnMap.put("vatMessage", msgBox);
  	return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	private void executeProcess( SiFunction siFunction, MessageBox msgBox, String resultMsg) throws Exception {
      
    try{
    	wrappedMessageBox(msgBox, siFunction, resultMsg, true);
   
    }catch(Exception pe){
    	log.error("系統功能執行時發生錯誤，原因：" + pe.toString());
    	throw new Exception(pe.getMessage());
    }	
  }
	
	private void wrappedMessageBox(MessageBox msgBox, SiFunction siFunction, String resultMsg, boolean isStartProcess){
  	
  	String menuId = siFunction.getFunctionCode();
  	
    Command cmd_ok = new Command();
  	if(isStartProcess){
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"kweSiInitialLine()", ""});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	   
  		msgBox.setType(MessageBox.ALERT);
  		
  		msgBox.setMessage( resultMsg );
  	}else{
        msgBox.setMessage(menuId + "表單已送出！");
  	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
  	}
  	
  	msgBox.setOk(cmd_ok);
  }

}
