package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeHead;
import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameter;
import tw.com.tm.erp.hbm.bean.ImReplenishHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ImReplenishService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ImReplenishAction {
    private static final Log log = LogFactory.getLog(ImReplenishAction.class);
    
    private ImReplenishService imReplenishService;
    
    public void setImReplenishService(ImReplenishService imReplenishService) {
        this.imReplenishService = imReplenishService;
    }

    /**
     * 自動補貨初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performInitial(Map parameterMap) {
	Map returnMap = null;
	try {
	    returnMap = imReplenishService.executeInitial(parameterMap);
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getActionInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap.put("vatMessage", msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 自動補貨基本參數初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performBasicParameterInitial(Map parameterMap) {
	Map returnMap = null;
	try {
	    returnMap = imReplenishService.executeBasicParameterInitial(parameterMap);
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getActionInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap.put("vatMessage", msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 自動補貨查詢初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performBasicParameterSearchInitial(Map parameterMap) {
	Map returnMap = null;
	try {
	    returnMap = imReplenishService.executeBasicParameterSearchInitial(parameterMap);
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getActionSearchInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap.put("vatMessage", msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 查詢初始化
     * @param parameterMap
     * @return
     */
    public List<Properties> performSearchInitial(Map parameterMap) {
	Map returnMap = null;
	try {
	    returnMap = imReplenishService.executeSearchInitial(parameterMap);
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getActionSearchInitial() + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap.put("vatMessage", msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performTransaction(Map parameterMap){

	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap();
	ImReplenishHead head = null;
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formBean = parameterMap.get("vatBeanFormBind");
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    log.info("驗證");
	    // 驗證
	    String error = imReplenishService.validate(parameterMap);
	    if(StringUtils.hasText(error) ){
		throw new Exception(error);
	    }
	    log.info("前端資料塞入bean");
	    // 前端資料塞入bean
	    resultMap = imReplenishService.updateBean(parameterMap); 

	    log.info("檢核與 存取db head and line");
	    // 檢核與 存取db head and line
	    resultMap = imReplenishService.updateAJAXAutoReplenish(parameterMap);
	    head = (ImReplenishHead) resultMap.get("entityBean");	

	    msgBox.setMessage((String) resultMap.get("resultMsg") );

	    wrappedMessageBox(msgBox, true);
	    
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getActionPerformTransaction() + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * 參數送出
     * @param parameterMap
     * @return
     */
    public List<Properties> performBasicParameterTransaction(Map parameterMap){

	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap();
	ImReplenishBasicParameter head = null;
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formBean = parameterMap.get("vatBeanFormBind");
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    log.info("驗證");
	    // 驗證
	    String error = imReplenishService.validateBasicParameter(parameterMap);
	    if(StringUtils.hasText(error) ){
		throw new Exception(error);
	    }
	    log.info("前端資料塞入bean");
	    // 前端資料塞入bean
//	    resultMap = imReplenishService.updateBasicParameterBean(parameterMap); 

	    log.info("檢核與 存取db head and line");
	    // 檢核與 存取db head and line
	    resultMap = imReplenishService.updateAJAXBasicParameter(parameterMap);
	    head = (ImReplenishBasicParameter) resultMap.get("entityBean");	

	    msgBox.setMessage((String) resultMap.get("resultMsg") );

	    wrappedMessageBox(msgBox, true);
	    
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getActionPerformTransaction() + ex.toString());
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
	    msgBox.setType(MessageBox.ALERT);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"createRefreshForm()", ""});
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);
	}

	msgBox.setOk(cmd_ok);
    }

}
