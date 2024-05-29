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
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.DbfExportData;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.CustmsControlService;
import tw.com.tm.erp.hbm.service.ExportService;
import tw.com.tm.erp.utils.AjaxUtils;


import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.service.UploadControlService;
import tw.com.tm.erp.hbm.dao.UploadControlDAO;


public class UploadControlAction
{
    private static final Log log = LogFactory.getLog(UploadControlAction.class);
    private UploadControlService uploadControlService;
    
    public void setUploadControlService(UploadControlService uploadControlService)
    {
        this.uploadControlService = uploadControlService;
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
    		System.out.println("UploadControlAction.initial");
    		returnMap = uploadControlService.executeInitial(parameterMap);	    
    	}catch (Exception ex) {
    		log.error("執行匯出初始化時發生錯誤，原因：" + ex.toString());
    		MessageBox msgBox = new MessageBox();
    		msgBox.setMessage(ex.getMessage());
    		returnMap = new HashMap();
    		returnMap.put("vatMessage" ,msgBox);
    	}
    	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }
	
}