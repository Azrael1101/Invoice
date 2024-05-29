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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.DbfExportData;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.ExportService;
import tw.com.tm.erp.utils.AjaxUtils;

public class ExportAction {
    private static final Log log = LogFactory.getLog(ExportAction.class);
    
    private ExportService exportService;
    private DbfExportData dbfExportData;
    
    public void setDbfExportData(DbfExportData dbfExportData) {
        this.dbfExportData = dbfExportData;
    }

    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
    
    /**
     * 初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performDBFInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = exportService.executeDBFInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行匯出初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }

    /**
     * 轉DBF送出 
     * @param parameterMap
     * @return
     */
    public List<Properties> performDBFExport(Map parameterMap){
    	
	Map returnMap = new HashMap(0);
	MessageBox msgBox = new MessageBox();
	Map resultMap = new HashMap();
	try {

	    Object formBean = parameterMap.get("vatBeanFormBind");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    // 檢核與 存取db head and line
	    resultMap = dbfExportData.exportDBF(parameterMap);

	    msgBox.setMessage((String) resultMap.get("resultMsg") );

//	    executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);

	} catch( ValidationErrorException ve ){
	    log.error("執行匯出DBF錯誤，原因：" + ve.toString());
	    if( MessageStatus.VALIDATION_FAILURE.equals(ve.getMessage()) ){
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
//		listErrorMsg(msgBox);
	    }else{
		msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
	    }	
	} catch (Exception ex) {
	    log.error("執行匯出DBF錯誤，原因：" + ex.toString());
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * T2POS產生檔案初始化
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public List<Properties> performPOSInitial(Map parameterMap){
	Map returnMap = null;	
	try{
	    returnMap = exportService.executePOSInitial(parameterMap);	    
	}catch (Exception ex) {
	    log.error("執行產生T2POS-TEXT初始化時發生錯誤，原因：" + ex.toString());
	    MessageBox msgBox = new MessageBox();
	    msgBox.setMessage(ex.getMessage());
	    returnMap = new HashMap();
	    returnMap.put("vatMessage" ,msgBox);
	}
	return AjaxUtils.parseReturnDataToJSON(returnMap);	
    }    
}
