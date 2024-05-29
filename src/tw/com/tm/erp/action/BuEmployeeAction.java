package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.service.BuEmployeeService;
import tw.com.tm.erp.hbm.service.SiGroupService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class BuEmployeeAction {
    
    private static final Log log = LogFactory.getLog(BuEmployeeAction.class);
    
    private BuEmployeeService buEmployeeService;
    
    public List<Properties> performTransaction(Map parameterMap){
     	Map returnMap = new HashMap(0);
  		MessageBox msgBox = new MessageBox();
  		try {
  			//log.info("getting vatBeanOther");
	    	Object otherBean = parameterMap.get("vatBeanOther");
	    	//log.info("going buEmployeeService.updateAJAXMovement");
  			HashMap resultMap = buEmployeeService.updateAJAXMovement(parameterMap);
  			log.info("(String)resultMap === "+(String)resultMap.get("resultMsg"));
  			if((String)resultMap.get("resultMsg")!=""){
  				msgBox.setMessage((String)resultMap.get("resultMsg"));
  			}else{
  				msgBox.setMessage("OK");
  			}
  			//這裡設置執行後的ALERT 訊息
  			//設置則為CONFIRM...
  	    msgBox.setType(MessageBox.PROMPT);
  	    
        Command cmd_ok = new Command();
        //Command cmd_cancel = new Command();
        cmd_ok.setCmd(Command.FUNCTION);
        //按確定執行
  	    cmd_ok.setParameters(new String[]{"kweInitialChild()"});
  	    msgBox.setOk(cmd_ok);
  	    //按取消執行
  	    //cmd_cancel.setCmd(Command.WIN_CLOSE);
  	    //msgBox.setCancel(cmd_cancel);
  	    
  		} catch (ProcessFailedException px) {
  			log.error("執行員工時發生錯誤，原因：" + px.toString());
  			msgBox.setMessage(px.getMessage());
  		} catch (Exception ex) {
  			log.error("執行員工存檔失敗，原因：" + ex.toString());
  			msgBox.setMessage(ex.getMessage());
  		}
  		returnMap.put("vatMessage", msgBox);
  		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

		public void setBuEmployeeService(BuEmployeeService buEmployeeService) {
			this.buEmployeeService = buEmployeeService;
		}
}
