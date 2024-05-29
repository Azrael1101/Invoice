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
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.service.BuLocationService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImInventoryCountsBatchCreateService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImInventoryCountsBatchCreateAction {
	
	private static final Log log = LogFactory.getLog(ImInventoryCountsBatchCreateAction.class);	
	
	private ImInventoryCountsBatchCreateService imInventoryCountsBatchCreateService;
	
	public void setImInventoryCountsBatchCreateService(ImInventoryCountsBatchCreateService imInventoryCountsBatchCreateService) {
		this.imInventoryCountsBatchCreateService = imInventoryCountsBatchCreateService;
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
			
  		HashMap resultMap = imInventoryCountsBatchCreateService.updateAJAXMenu(parameterMap);
  		
  		SiMenu siMenuBean = (SiMenu) resultMap.get("entityBean");
  		Object otherBean = parameterMap.get("vatBeanOther");
//  		executeProcess(otherBean, siMenuBean, msgBox);
  		msgBox.setMessage((String) resultMap.get("resultMsg") + ",是否繼續新增？");
  	} catch (ProcessFailedException px) {
  		log.error("系統選單模組發生錯誤，原因：" + px.toString());
  		msgBox.setMessage(px.getMessage());
  	} catch (Exception ex) {
  		log.error("系統選單模組存檔失敗，原因：" + ex.toString());
  		msgBox.setMessage(ex.getMessage());
  	}
  	
  	returnMap.put("vatMessage", msgBox);
  	return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	private void executeProcess(Object otherBean, SiMenu siMenuBean, MessageBox msgBox) throws Exception {
      
    try{
    	wrappedMessageBox(msgBox, siMenuBean, true);
   
    }catch(Exception pe){
    	log.error("系統選單模組發生錯誤，原因：" + pe.toString());
    	throw new Exception(pe.getMessage());
    }	
  }
	
	private void wrappedMessageBox(MessageBox msgBox, SiMenu siMenuBean, boolean isStartProcess){
  	
  	Long menuId = siMenuBean.getMenuId();
  	
      Command cmd_ok = new Command();
  	if(isStartProcess){
  	    msgBox.setType(MessageBox.CONFIRM);
  	    cmd_ok.setCmd(Command.FUNCTION);
  	    cmd_ok.setParameters(new String[]{"kweNewInitial()", ""});
  	    Command cmd_cancel = new Command();
  	    //cmd_cancel.setCmd(Command.WIN_CLOSE);
  	    //cmd_ok.setParameters(new String[]{"", ""});
  	    //msgBox.setCancel(cmd_cancel);
  	}else{
        msgBox.setMessage(menuId + "表單已送出！");
  	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
  	}
  	
  	msgBox.setOk(cmd_ok);
  }

}
