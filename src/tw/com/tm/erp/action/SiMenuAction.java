package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.SiMenuService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;

public class SiMenuAction {
	private static final Log log = LogFactory.getLog(SiMenuAction.class);
	private SiMenuService siMenuService;
	
	public void setSiMenuService(SiMenuService siMenuService) {
		this.siMenuService = siMenuService;
	}
	
	public List<Properties> performInitial(Map parameterMap){
    	Map returnMap = new HashMap();	
		try{
		    Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String loginEmployeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			returnMap.put("brandCode", brandCode);	
			returnMap.put("createdBy", loginEmployeeCode);
			returnMap.put("creationDate", DateUtils.getCurrentDateStr());
		}
		catch(Exception ex){
		    log.error("執行KWE選單維護初始化時發生錯誤，原因：" + ex.toString());  //修正錯誤訊息
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
	public List<Properties> performTransaction(Map parameterMap){ //List<Properties>
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		try {
	  		//SiMenu siMenuBean = (SiMenu)resultMap.get("entityBean");
	  		//Object otherBean = parameterMap.get("vatBeanOther");
	  		Object formBindBean = parameterMap.get("vatBeanFormBind");
	  		
	  		//驗證輸入參數...
  			String name = (String)PropertyUtils.getProperty(formBindBean, "name");
			if(name == null || name.trim().length() == 0){
			    msgBox.setMessage("請輸入選單名稱!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			String url = (String)PropertyUtils.getProperty(formBindBean, "url");
			if(url == null || url.trim().length() == 0){
			    msgBox.setMessage("請輸入網址!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			//寫入資料...
			HashMap resultMap = siMenuService.updateAJAXMenu(parameterMap);
	  		
	  		//executeProcess(otherBean, siMenuBean, msgBox);
	  		msgBox.setMessage((String) resultMap.get("resultMsg") + ",是否繼續新增？");
		} 
		catch(ProcessFailedException px) {
	  		log.error("系統選單模組發生錯誤，原因：" + px.toString());
	  		msgBox.setMessage(px.getMessage());
	  	} 
		catch(Exception ex) {
	  		log.error("系統選單模組存檔失敗，原因：" + ex.toString());
	  		msgBox.setMessage(ex.getMessage());
	  	}
	  	returnMap.put("vatMessage", msgBox);
	  	return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	public List<Properties> performSearchSelection(Map parameterMap){
		log.info("performSearchSelection");
		Map returnMap = null;	
		try{
			return siMenuService.getSearchSelection(parameterMap);	    
		}catch (Exception ex) {
			log.info("執行選單檢視時發生錯誤，原因：" + ex.toString());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage(ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	/*
	private void executeProcess(Object otherBean, SiMenu siMenuBean, MessageBox msgBox) throws Exception {
	    try{
	    	wrappedMessageBox(msgBox, siMenuBean, true);
	    }
	    catch(Exception pe){
	    	log.error("系統選單模組發生錯誤，原因：" + pe.toString());
	    	throw new Exception(pe.getMessage());
	    }	
	}
	
	private void wrappedMessageBox(MessageBox msgBox, SiMenu siMenuBean, boolean isStartProcess) {
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
	  	}
	  	else{
	        msgBox.setMessage(menuId + "表單已送出！");
	  	    cmd_ok.setCmd(Command.WIN_CLOSE);	       
	  	}
	  	msgBox.setOk(cmd_ok);
    }
	*/
}
