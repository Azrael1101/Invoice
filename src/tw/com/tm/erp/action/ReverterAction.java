package tw.com.tm.erp.action;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.*;
import tw.com.tm.erp.utils.AjaxUtils;

public class ReverterAction {
    
    private static final Log log = LogFactory.getLog(ReverterAction.class);
    
    private ReverterService reverterService;
    
    public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
		    returnMap = reverterService.executeInitial(parameterMap);
		}catch (Exception ex) {
		    System.out.println("執行反轉單初始化時發生錯誤，原因：" + ex.getMessage());
		    MessageBox msgBox = new MessageBox();
	        msgBox.setMessage(ex.getMessage());
	        returnMap = new HashMap();
	        returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    public List<Properties> performTransaction(Map parameterMap){
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
			Object bean = reverterService.executeReverter(parameterMap);
			reverterService.executeProcess(parameterMap, bean);
			msgBox.setMessage("執行反轉單成功");
			Command cmd_ok = new Command();
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{"changeOrderNo()", ""});
    	    msgBox.setOk(cmd_ok);
		}catch (Exception ex) {
			log.error("執行反轉單失敗，原因 " + ex.getMessage());
			msgBox.setMessage("執行反轉單失敗，原因 " + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
	public void setReverterService(ReverterService reverterService) {
		this.reverterService = reverterService;
	}
}
