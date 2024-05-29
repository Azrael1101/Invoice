package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuShopSetTlement;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.BuShopSetTlementService;
import tw.com.tm.erp.utils.AjaxUtils;

public class BuShopSetTlementAction {

    private static final Log log = LogFactory.getLog(BuShopSetTlementAction.class);
    private BuShopSetTlementService buShopSetTlementService;


    public void setBuShopSetTlementService(
    	BuShopSetTlementService buShopSetTlementService) {
        this.buShopSetTlementService = buShopSetTlementService;
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
	    returnMap = buShopSetTlementService.executeInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行專櫃拆帳初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 搜尋初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performSearchInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = buShopSetTlementService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行專櫃拆帳初始化時發生錯誤，原因：" + ex.toString());
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
    public List<Properties> performTransaction(Map parameterMap){
	
	Map returnMap = new HashMap(0);
	Map resultMap = new HashMap();
	MessageBox msgBox = new MessageBox();
//	BuShopSetTlement head = null;
	
	List errorMsgs = new ArrayList(0);
	try {
	    
//	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
//	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    errorMsgs  = buShopSetTlementService.checkBuShopSetTlement(parameterMap);
	    
	    if(errorMsgs.size() > 0){
		throw new ValidationErrorException(errorMsgs.toString());
	    }
	    // 前端資料塞入bean
	    buShopSetTlementService.updateBuShopSetTlementBean(parameterMap);

	    // 檢核與 存取db
	    resultMap = buShopSetTlementService.updateAJAXBuShopSetTlement(parameterMap);
	    
	    // 綑綁前端js如何處理
	    wrappedMessageBox(msgBox,resultMap);
	    
//	    head = (BuShopSetTlement) resultMap.get("entityBean");	
	    	    
	} catch( ValidationErrorException ve ){
	    log.error("執行專櫃拆帳資料檢核失敗，原因：" + errorMsgs);
	    msgBox.setMessage(errorMsgs.toString());

	} catch (Exception ex) {
	    log.error("執行專櫃拆帳資料存檔失敗，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}

	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

    /**
     * 綑綁回前畫面的視窗
     * @param msgBox
     * @param head
     * @param isStartProcess
     * @param isExecFunction
     */
    private void wrappedMessageBox(MessageBox msgBox, Map resultMap){
	BuShopSetTlement buShopSetTlement = (BuShopSetTlement) resultMap.get("entityBean");
	
	//設定訊息內容
	msgBox.setMessage((String) resultMap.get("resultMsg") );
	
	//設定confirm
	msgBox.setType(MessageBox.ALERT);
	
	//頁面按確定後執行js的函數
	Command cmd_ok = new Command();
	cmd_ok.setCmd(Command.FUNCTION);
	if (null != buShopSetTlement ) {
	    cmd_ok.setParameters(new String[]{"createRefreshForm('"+buShopSetTlement.getLineId()+"')",""});
	}
	
	//按ok執行
	msgBox.setBefore(cmd_ok);
    }



}
