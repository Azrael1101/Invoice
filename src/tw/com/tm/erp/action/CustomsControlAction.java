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

public class CustomsControlAction {
    private static final Log log = LogFactory.getLog(CustomsControlAction.class);
    
    private ExportService exportService; 
    private CustmsControlService CustmsControlService = new CustmsControlService();
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
		System.out.println("INIINITINIINITINIINITINIINITINIINITINIINIT");
	    returnMap = CustmsControlService.executeDBFInitial(parameterMap);	    
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
	    //resultMap = dbfExportData.exportDBF(parameterMap);
	    resultMap = CustmsControlService.doSubmit(parameterMap);
	    
	    msgBox.setMessage((String) resultMap.get("resultMsg") );

//	    executeProcess(otherBean, resultMap, imAdjustmentHead, msgBox);

	} catch (Exception ex) {
	    log.error("執行匯出DBF錯誤，原因：" + ex.toString());
	    ex.printStackTrace();
	    msgBox.setMessage(ex.getMessage());
	}
	returnMap.put("vatMessage", msgBox);
	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	public CustmsControlService getCustmsControlService() {
		return CustmsControlService;
	}

	public void setCustmsControlService(CustmsControlService custmsControlService) {
		CustmsControlService = custmsControlService;
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
			log.info("初始化!!");
			returnMap = CustmsControlService.executeInitial(parameterMap);
		}catch (Exception ex) {
			log.error("執行初始化時發生錯誤，原因：" + ex.toString());
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
	public List<Properties> performTransaction(Map parameterMap){ 
		log.info("performTransaction");
		Map returnMap = new HashMap(0); 
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0); 
		try {  
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			log.info("otherBean~~~~~~"+otherBean);
			// 驗證
			//buPurchaseService.validateHead(parameterMap);    //沒有功能
			// 驗對則存檔(因為AjaxUtils.copyJSONBeantoPojoBean的關係)
			//buPurchaseService.executeFindActualBuPurchase(parameterMap);  //在此塞入前端單頭值
			// 前端資料塞入bean
			//buPurchaseService.updateBuGoalAchevementBean(parameterMap);   //沒有功能
			// 存檔							
			//esultMap = buPurchaseService.updateAJAXBuGoalAchevement(parameterMap);
			//BuPurchaseHead buPurchaseHead = (BuPurchaseHead) resultMap.get("entityBean");
			//wrappedMessageBox(msgBox, buPurchaseHead, true,false);
			msgBox.setMessage((String) resultMap.get("resultMsg"));
			//if(!buPurchaseHead.getOrderTypeCode().equals("LOA")){
				//executeProcess(otherBean, resultMap, buPurchaseHead, msgBox);
			//}
			/*if(buPurchaseHead.getOrderTypeCode()=="LOA")
			{
			Long headId = buPurchaseHead.getHeadId();
			competenceService.updateCompetenceData(headId);
			throw new Exception("updateCompetenceData 存檔失敗");
			}*/
		//} catch (ProcessFailedException px) {
		//	log.error("執行地點維護單流程時發生錯誤，原因：" + px.toString());
			//msgBox.setMessage(px.getMessage());
		} catch (Exception ex) {
			log.error("執行地點維護單存檔失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	
}
