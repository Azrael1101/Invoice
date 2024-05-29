package tw.com.tm.erp.hbm.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exportdb.POSExportData;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ExportService {
    private static final Log log = LogFactory.getLog(ExportService.class);
    
    private BuBrandDAO buBrandDAO;
    private BuCommonPhraseService buCommonPhraseService;
    
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
        this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }

    /**
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeDBFInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    Map multiList = new HashMap(0);
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("employeeName", employeeName);
	    resultMap.put("employeeCode", loginEmployeeCode);
	    resultMap.put("brandCode", loginBrandCode);

	    List<BuCommonPhraseLine> allBudgetYearLists = buCommonPhraseService.getCommonPhraseLinesById("BudgetYearList", false);
	    List<BuCommonPhraseLine> allMonths = buCommonPhraseService.getCommonPhraseLinesById("Month", false);
	    List<BuCommonPhraseLine> allExportTypes = buCommonPhraseService.getCommonPhraseLinesById("ExportDBFType", false);
	    
	    multiList.put("allBudgetYearLists"	, AjaxUtils.produceSelectorData(allBudgetYearLists, "lineCode", "name", false, true ));
	    multiList.put("allMonths"	, AjaxUtils.produceSelectorData(allMonths, "lineCode", "name", false, true ));
	    multiList.put("allExportTypes"	, AjaxUtils.produceSelectorData(allExportTypes, "lineCode", "name", false, true ));
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("匯出初始化失敗，原因：" + ex.toString());
	    throw new Exception("匯出初始化失敗，原因：" + ex.toString());

	}
    }
    
    public List<Properties> exportErpToPos(Map parameterMap) throws Exception{
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	//DateFormat df = new SimpleDateFormat("yyyymmdd");
    	
    	try {			
			Object formBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Object formLink = parameterMap.get("vatBeanFormLink");
    		
    		//String exportType = (String)PropertyUtils.getProperty(formLink, "exportType");
    		int exportType = Integer.valueOf( (String)PropertyUtils.getProperty(formLink, "exportType") );
		    String startDate = (String)PropertyUtils.getProperty(formBean ,"startDate");
		    //String reportDate = df.format(startDate);
		    String endDate = (String)PropertyUtils.getProperty(formBean, "endDate");
		    String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		    
		    log.info("exportType:"+exportType);
			log.info("startDate:"+startDate);
			log.info("endDate:"+endDate);
			log.info("brandCode:"+brandCode);
			
		    POSExportData export = new POSExportData();
		    System.out.println("============start+++++++++++++++++++++++++++");
		    
		    export.doPOSExportData(exportType, brandCode, DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate)
			    , DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate), "T48351"); 
		    System.out.println("============end+++++++++++++++++++++++++++");
		    msgBox.setMessage("下載成功");	    
			
		} catch (Exception ex) {
		    log.error("取得檔案錯誤，原因：" + ex.toString());
		    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		    throw new Exception("取得檔案錯誤！");
		}
		returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
    
    /**
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executePOSInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    Map multiList = new HashMap(0);
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("employeeName", employeeName);
	    resultMap.put("employeeCode", loginEmployeeCode);
	    resultMap.put("brandCode", loginBrandCode);
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("POS初始化失敗，原因：" + ex.toString());
	    throw new Exception("POS初始化失敗，原因：" + ex.toString());

	}
    }
    
}
