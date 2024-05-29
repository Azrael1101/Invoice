package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.MachineSaleService;
import tw.com.tm.erp.utils.AjaxUtils;


public class MachineSaleAction {
    private MachineSaleService machineSaleService;
    
    public void setMachineSaleService(MachineSaleService machineSaleService) {
	this.machineSaleService = machineSaleService;
    }
    
    public List<Properties> performInitial(Map parameterMap){
	Map returnMap = null;
	try{
	    returnMap = machineSaleService.executeInitial(parameterMap);
	}catch(Exception ex){
	    System.out.println("====ERR==="+ex.toString());
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
	public List<Properties> performLockItem(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try{
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		HashMap lockMap = new HashMap();
		Object vatBeanOther = parameterMap.get("vatBeanOther");
		Object pickerBean = parameterMap.get("vatBeanPicker");		
		lockMap.put("vatBeanPicker", pickerBean);
		lockMap.put("vatBeanOther", vatBeanOther);
		lockMap.put("formBindBean", formBindBean);
		machineSaleService.saveLockItem(lockMap);
		
		}catch(Exception ex){
		    System.out.println("===ERR==="+ex.toString());
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
