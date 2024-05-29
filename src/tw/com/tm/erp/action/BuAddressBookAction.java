package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.hbm.service.ImLetterOfCreditService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.hbm.service.WfApprovalResultService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;


public class BuAddressBookAction {
    
    private static final Log log = LogFactory.getLog(BuAddressBookAction.class);
    
    private BuAddressBookService buAddressBookService;
    private SiProgramLogAction siProgramLogAction;
    
	public void setBuAddressBookService(BuAddressBookService buAddressBookService) {
		this.buAddressBookService = buAddressBookService;
	}
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }
    
    
    public List<Properties> performSearchSupplierInitial(Map parameterMap){
    	Map returnMap = null;	
    	try{
    	    returnMap = buAddressBookService.executeSearchSupplierInitial(parameterMap);	    
    	}catch (Exception ex) {
    	    log.info("執行通訊錄供應商查詢初始化時發生錯誤，原因：" + ex.toString());
    	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
    	}
    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
    
    public List<Properties> performSearchSupplierSelection(Map parameterMap){
    	Map returnMap = null;	
    	try{
    	    returnMap = buAddressBookService.getSearchSupplierSelection(parameterMap);	    
    	}catch (Exception ex) {
    	    System.out.println("執行通訊錄供應商檢視時發生錯誤，原因：" + ex.toString());
    	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
    	}
    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
 	}
    
    public List<Properties> performSearchCustomerInitial(Map parameterMap){
    	Map returnMap = null;	
    	try{
    	    returnMap = buAddressBookService.executeSearchCustomerInitial(parameterMap);	    
    	}catch (Exception ex) {
    	    log.info("執行客戶資料查詢初始化時發生錯誤，原因：" + ex.toString());
    	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
    	}
    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
    
    public List<Properties> performSearchCustomerSelection(Map parameterMap){
    	Map returnMap = null;	
    	try{
    	    returnMap = buAddressBookService.getSearchCustomerSelection(parameterMap);	    
    	}catch (Exception ex) {
    	    System.out.println("執行客戶資料檢視時發生錯誤，原因：" + ex.toString());
    	    MessageBox msgBox = new MessageBox();
            msgBox.setMessage(ex.getMessage());
            returnMap = new HashMap();
            returnMap.put("vatMessage" ,msgBox);
    	}
    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
 	}
    
}
