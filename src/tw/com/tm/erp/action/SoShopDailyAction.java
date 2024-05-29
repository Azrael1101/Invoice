package tw.com.tm.erp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.service.SoShopDailyHeadMainService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;

public class SoShopDailyAction {
    
    private static final Log log = LogFactory.getLog(SoShopDailyAction.class);
    
    private SoShopDailyHeadMainService soShopDailyHeadMainService;
      
    public void setSoShopDailyHeadMainService(SoShopDailyHeadMainService soShopDailyHeadMainService) {
        this.soShopDailyHeadMainService = soShopDailyHeadMainService;
    }
   
    
    public List<Properties> performInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{	    
	    validateRequiredParameters(parameterMap);		
	    returnMap = soShopDailyHeadMainService.executeInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行POS每日資料維護初始化時發生錯誤，原因：" + ex.toString());
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
	List errorMsgs = new ArrayList();
	try{
	    validateRequiredParameters(parameterMap);
	    SoShopDailyHead shopDailyHead = soShopDailyHeadMainService.checkShopDaily(parameterMap, errorMsgs);
	    if(shopDailyHead != null){
		String resultMsg = soShopDailyHeadMainService.saveOrUpdateSoShopDaily(parameterMap, shopDailyHead);
		msgBox.setMessage(resultMsg);
		wrappedMessageBox(msgBox, errorMsgs, true);
	    }else{
		wrappedMessageBox(msgBox, errorMsgs, false);
	    }
	}catch (Exception ex) {
	    log.error("執行POS每日資料存檔失敗，原因：" + ex.toString());
            msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage" ,msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    private void wrappedMessageBox(MessageBox msgBox, List errorMsgs, boolean isSuccess){
	
	if(isSuccess){
	    Command cmd_ok = new Command();
	    msgBox.setType(MessageBox.CONFIRM);
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"resetForm()", ""});
	    msgBox.setOk(cmd_ok);
	    Command cmd_cancel = new Command();
	    cmd_cancel.setCmd(Command.WIN_CLOSE);
	    msgBox.setCancel(cmd_cancel);	
	    
	}else{
	    StringBuffer assemblyMsg = new StringBuffer("");
	    if(errorMsgs != null && errorMsgs.size() > 0){
                for(int i = 0; i < errorMsgs.size(); i++){
        	    String errorMsg = (String)errorMsgs.get(i);
        	    assemblyMsg.append(errorMsg);
        	    if(i != errorMsgs.size() - 1){
        	        assemblyMsg.append("\n");
        	    }
                }
                msgBox.setMessage(assemblyMsg.toString());
	    }
	}
    }
    
    private void validateRequiredParameters(Map parameterMap) throws Exception{
	
	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	//======check loginBrandCode、loginEmployeeCode========
	if(!StringUtils.hasText(loginBrandCode)){
	    throw new Exception("loginBrandCode參數為空值！");
	}else if(!StringUtils.hasText(loginEmployeeCode)){
            throw new Exception("loginEmployeeCode參數為空值！");
	}
    }
    
    public List<Properties> performSearchInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    validateRequiredParameters(parameterMap);
	    returnMap = soShopDailyHeadMainService.executeSearchInitial(parameterMap);	    
	}catch (Exception ex) {
	    System.out.println("執行POS每日資料查詢初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    public List<Properties> performSearchSelection(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    returnMap = soShopDailyHeadMainService.getSearchSelection(parameterMap);	    
	}catch (Exception ex) {
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    public List<Properties> performMaintainInitial(Map parameterMap){
	
	Map returnMap = null;	
	try{
	    validateRequiredParameters(parameterMap);
	    returnMap = soShopDailyHeadMainService.executeMaintainInitial(parameterMap);	    
	}catch (Exception ex) {
	    System.out.println("執行POS每日資料維護初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
	}
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    public List<Properties> performMaintainSearch(Map parameterMap){
	
	Map returnMap = new HashMap();
	MessageBox msgBox = new MessageBox();
	try{
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");	

	    Map conditionMap = validateMaintainSearchParameters(parameterMap);
	    if(!"T2".equals(loginBrandCode))
	    {
	    	log.info("not T2"+loginBrandCode);
	    	soShopDailyHeadMainService.createMaintainData(conditionMap);
	    }
	    else
	    {
	    	log.info("is T2"+loginBrandCode);
	    	soShopDailyHeadMainService.createDailyData(conditionMap);
	    }
	    
	    Command cmd_ok = new Command();
	    cmd_ok.setCmd(Command.FUNCTION);
	    cmd_ok.setParameters(new String[]{"doSearch()", ""});
	    msgBox.setOk(cmd_ok);
	    msgBox.setMessage("資料已成功建立！");   
	}catch (Exception ex) {
	    log.error("執行POS每日資料維護查詢時發生錯誤，原因：" + ex.toString());	    
            msgBox.setMessage(ex.getMessage());           
	}
	returnMap.put("vatMessage" ,msgBox);
	
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
    
    private Map validateMaintainSearchParameters(Map parameterMap) throws Exception{
	
	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	//======check loginBrandCode、loginEmployeeCode========
	if(!StringUtils.hasText(loginBrandCode)){
	    throw new Exception("loginBrandCode參數為空值！");
	}else if(!StringUtils.hasText(loginEmployeeCode)){
            throw new Exception("loginEmployeeCode參數為空值！");
	}
	
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");
 	String startSalesDate = (String)PropertyUtils.getProperty(formBindBean, "startSalesDate");
 	String endSalesDate = (String)PropertyUtils.getProperty(formBindBean, "endSalesDate");
 	String batch = (String)PropertyUtils.getProperty(formBindBean, "batch");
 	log.info("validateBatch=" +batch);
	//======check shopCode、startSalesDate、endSalesDate========
	if(!StringUtils.hasText(startSalesDate)){
            throw new Exception("startSalesDate參數為空值！");
	}else if(!StringUtils.hasText(endSalesDate)){
            throw new Exception("endSalesDate參數為空值！");
	}
	String salesUnit = "專櫃";
	if("T2".equals(loginBrandCode)){
	    salesUnit = "機台";
	}
	
	Map conditionMap = new HashMap();
	conditionMap.put("loginBrandCode", loginBrandCode);
	conditionMap.put("loginEmployeeCode", loginEmployeeCode);
	conditionMap.put("shopCode", shopCode);
	conditionMap.put("startSalesDate", DateUtils.parseDate("yyyy/MM/dd", startSalesDate));
	conditionMap.put("endSalesDate", DateUtils.parseDate("yyyy/MM/dd", endSalesDate));
	conditionMap.put("batch", batch);
	conditionMap.put("salesUnit", salesUnit);
	
	return conditionMap;
    }
}
