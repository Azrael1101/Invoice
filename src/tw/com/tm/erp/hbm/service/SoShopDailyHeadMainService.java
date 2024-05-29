package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuShopMachineId;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.bean.SoShopDailyHeadId;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SoShopDailyHeadDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;

public class SoShopDailyHeadMainService {

    private static final Log log = LogFactory
	    .getLog(SoShopDailyHeadMainService.class);
    
    public static final String[] GRID_SEARCH_FIELD_NAMES = {"id.shopCode", "id.salesDate", "visitorCount", "actualSalesAmount", 
	    "lastUpdatedBy", "lastUpdateTime"};

    public static final int[] GRID_SEARCH_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_LONG,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {"", "", "", "", "",""};
    
    public static final String[] GRID_MAINTAIN_FIELD_NAMES = {"indexNo", "id.shopCode", "id.salesDate", "visitorCount", "actualSalesAmount", 
	    "lastUpdatedBy", "lastUpdateTime"};

    public static final int[] GRID_MAINTAIN_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
    
    private SoShopDailyHeadDAO soShopDailyHeadDAO;
    private SoPostingTallyDAO soPostingTallyDAO;
    private BuBasicDataService buBasicDataService;

    private NativeQueryDAO nativeQueryDAO;
    
    private BuShopMachineDAO buShopMachineDAO;
    
    private BuShopDAO buShopDAO;
	public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
		this.soPostingTallyDAO = soPostingTallyDAO;
	    }
	public void setSoShopDailyHeadDAO(SoShopDailyHeadDAO soShopDailyHeadDAO) {
	this.soShopDailyHeadDAO = soShopDailyHeadDAO;
    }
    
    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
	this.buBasicDataService = buBasicDataService;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
	this.nativeQueryDAO = nativeQueryDAO;
	}
    
    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
    	this.buShopMachineDAO = buShopMachineDAO;
    	}
    
    public void setBuShopDAO(BuShopDAO buShopDAO) {
    	this.buShopDAO = buShopDAO;
    	}
    /**
     * 依據primary key為查詢條件，取得POS每日資料檔
     * 
     * @param id
     * @return SoShopDailyHead
     * @throws Exception
     */
    public SoShopDailyHead findSoShopDailyHeadById(SoShopDailyHeadId id, String salesUnit)
	    throws Exception {    	
	try {
	    SoShopDailyHead shopDailyHead = (SoShopDailyHead) soShopDailyHeadDAO
		    .findByPrimaryKey(SoShopDailyHead.class, id);
	    return shopDailyHead;
	} catch (Exception ex) {
	    log.error("依據" + salesUnit + "(" + id.getShopCode() + ")、銷售日期("
		    + DateUtils.format(id.getSalesDate())
		    + ")查詢POS每日資料檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據" + salesUnit + "(" + id.getShopCode() + ")、銷售日期("
		    + DateUtils.format(id.getSalesDate())
		    + ")查詢POS每日資料檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增至POS每日資料檔
     * 
     * @param saveObj
     */
    private void insertSoShopDailyHead(SoShopDailyHead saveObj, String loginUser) {
	saveObj.setCreatedBy(loginUser);
	saveObj.setLastUpdatedBy(loginUser);
	saveObj.setCreationDate(new Date());
	saveObj.setLastUpdateDate(new Date());
	soShopDailyHeadDAO.save(saveObj);
    }

    /**
     * 更新至POS每日資料檔
     * 
     * @param updateObj
     */
    private void modifySoShopDailyHead(SoShopDailyHead updateObj, String loginUser) {
	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	soShopDailyHeadDAO.update(updateObj);
    }
    
    /**
     * POS每日資料維護初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
            HashMap argumentMap = getRequestParameter(parameterMap);
            String loginBrandCode = (String)argumentMap.get("loginBrandCode");
            String loginEmployeeCode = (String)argumentMap.get("loginEmployeeCode");
	    String shopCodeKey = (String)argumentMap.get("shopCodeKey");
	    String salesDateKey = (String)argumentMap.get("salesDateKey");
	   
	    Map multiList = new HashMap(0);
	    SoShopDailyHead form = null;
	    if(!StringUtils.hasText(shopCodeKey) && !StringUtils.hasText(salesDateKey)){
	    	form = createNewShopDailyBean(argumentMap, resultMap);
	        if(!"T2".equals(loginBrandCode)){
	            List<BuShop> allShops = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	            multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCode", false, true));
	        }else{
		    List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	            multiList.put("allShops", AjaxUtils.produceSelectorData(allShopMachines, "posMachineCode", "posMachineCode", false, true));	       
	        }
	    }else{
	    	form = findShopDailyBean(argumentMap, resultMap);
	    	BuShop shop = new BuShop();
	    	shop.setShopCode(shopCodeKey);
	    	List<BuShop> allShops = new ArrayList(0);
	    	allShops.add(shop);
	    	multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCode", false, true));
	    }	    
	    resultMap.put("multiList",multiList);
	
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("POS每日資料維護初始化失敗，原因：" + ex.toString());
	    throw new Exception("POS每日資料維護初始化失敗，原因：" + ex.getMessage());
	}           
    }
    
    private HashMap getRequestParameter(Map parameterMap) throws Exception{
	
	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
	String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	String shopCodeKey = (String)PropertyUtils.getProperty(otherBean, "shopCodeKey");
	String salesDateKey = (String)PropertyUtils.getProperty(otherBean, "salesDateKey");
	String salesUnit = "專櫃";
	
	HashMap conditionMap = new HashMap();
	conditionMap.put("loginBrandCode", loginBrandCode);
	conditionMap.put("loginEmployeeCode", loginEmployeeCode);
	conditionMap.put("shopCodeKey", shopCodeKey);
	conditionMap.put("salesDateKey", salesDateKey);
	if("T2".equals(loginBrandCode)){
	    salesUnit = "機台";
	}
	conditionMap.put("salesUnit", salesUnit);
	
	return conditionMap;
    }
    
    
    /**
     * 產生一筆新的bean
     * 
     * @param argumentMap
     * @param resultMap
     * @return SoShopDailyHead
     * @throws Exception
     */
    public SoShopDailyHead createNewShopDailyBean(Map argumentMap, Map resultMap) throws Exception {
	
	try{	
	    String loginEmployeeCode  = (String)argumentMap.get("loginEmployeeCode");	    
	    SoShopDailyHeadId shopDailyHeadId  = new SoShopDailyHeadId();
	    shopDailyHeadId.setSalesDate(new Date());
	    SoShopDailyHead form  = new SoShopDailyHead();
	    form.setId(shopDailyHeadId);
	    form.setCreatedBy(loginEmployeeCode);
	    form.setLastUpdatedBy(loginEmployeeCode);	    	   
	    resultMap.put("form", form);
	    
	    return form;
	}catch (Exception ex) {
	    log.error("產生POS每日資料維護Bean失敗，原因：" + ex.toString());
	    throw new Exception("產生POS每日資料維護Bean發生錯誤！");
	} 	
    }
    
    public SoShopDailyHead findShopDailyBean(Map argumentMap, Map resultMap) 
            throws FormException, Exception {

        try{
            String salesUnit = (String)argumentMap.get("salesUnit");
            String shopCodeKey = (String)argumentMap.get("shopCodeKey");
	    String salesDateKey = (String)argumentMap.get("salesDateKey");	    
	    SoShopDailyHeadId id = new SoShopDailyHeadId();
	    id.setShopCode(shopCodeKey);
	    id.setSalesDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, salesDateKey));
    	    
	    SoShopDailyHead form = findSoShopDailyHeadById(id, salesUnit);
            if(form != null){
	        resultMap.put("form", form);	        
	        return form;
            }else{
	        throw new NoSuchObjectException("查無" + salesUnit + "(" + shopCodeKey + ")、銷售日期(" + salesDateKey + ")的POS每日資料！");
            }	    
       }catch (FormException fe) {
           log.error("查詢POS每日資料失敗，原因：" + fe.toString());
           throw new FormException(fe.getMessage());
       }catch (Exception ex) {
           log.error("查詢POS每日資料發生錯誤，原因：" + ex.toString());
           throw new Exception("查詢POS每日資料發生錯誤！");
       }	
    }
    
    
    /**
     * 判斷是否為新資料，並將POS每日資料新增或更新至POS每日資料檔
     * 
     * @param parameterMap
     * @param shopDailyHead
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String saveOrUpdateSoShopDaily(Map parameterMap, SoShopDailyHead shopDailyHead) 
            throws FormException, Exception {
        
        try{
    	    HashMap argumentMap = getRequestParameter(parameterMap);
    	    String salesUnit = (String)argumentMap.get("salesUnit");
    	    String loginEmployeeCode = (String)argumentMap.get("loginEmployeeCode");
    	    String shopCodeKey = (String)argumentMap.get("shopCodeKey");
    	    String salesDateKey = (String)argumentMap.get("salesDateKey");
    	   
    	    SoShopDailyHead shopDailyHeadPO = findSoShopDailyHeadById(shopDailyHead.getId(), salesUnit);	    
	    if (shopDailyHeadPO == null) {
		insertSoShopDailyHead(shopDailyHead, loginEmployeeCode);
	    } else if (!StringUtils.hasText(shopCodeKey) && !StringUtils.hasText(salesDateKey)) {
		throw new ValidationErrorException(salesUnit + "(" + shopDailyHead.getId().getShopCode() + ")、" +
				"銷售日期(" + DateUtils.format(shopDailyHead.getId().getSalesDate())+ ")已經存在，請勿重複建立！");			
	    } else {
		String createdBy = shopDailyHeadPO.getCreatedBy();
		Date creationDate = shopDailyHeadPO.getCreationDate();
		BeanUtils.copyProperties(shopDailyHead, shopDailyHeadPO);
		shopDailyHeadPO.setCreatedBy(createdBy);
		shopDailyHeadPO.setCreationDate(creationDate);
		modifySoShopDailyHead(shopDailyHeadPO, loginEmployeeCode);
	    }
    	        	    
	    return salesUnit + "("+ shopDailyHead.getId().getShopCode() + ")、銷售日期(" + DateUtils.format(shopDailyHead.getId().getSalesDate()) + ")存檔成功！";
        }  catch (Exception ex) {
	    log.error("POS每日資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("POS每日資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
   
    /**
     * 檢核POS每日資料
     * 
     * @param parameterMap
     * @param errorMsgs
     * @return SoShopDailyHead
     * @throws Exception
     */
    public SoShopDailyHead checkShopDaily(Map parameterMap, List errorMsgs) throws Exception {
	
	try{
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");
            String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
	    String shopCode = (String)PropertyUtils.getProperty(formLinkBean, "shopCode");
	    String salesDate = (String)PropertyUtils.getProperty(formLinkBean, "salesDate");	
	    String visitorCount = (String)PropertyUtils.getProperty(formBindBean, "visitorCount");
    	    String totalActualSalesAmount = (String)PropertyUtils.getProperty(formBindBean, "totalActualSalesAmount");
    	    String salesUnit = "專櫃";
    	    if("T2".equals(loginBrandCode)){
	        salesUnit = "機台";
	    }
    	    
    	    
            Long actualVisitorCount = null;
            Double actualTotalActualSalesAmount = null;
            SoShopDailyHead shopDailyHead = null;
            
	    if (!StringUtils.hasText(shopCode)) {
	        errorMsgs.add("請選擇" + salesUnit + "！");
	    }
	    
	    if (!StringUtils.hasText(salesDate)) {
	        errorMsgs.add("請選擇銷售日期！");
	    } 
	    
	    if (StringUtils.hasText(visitorCount)) {
		try{
	            actualVisitorCount = Long.parseLong(visitorCount);
		}catch(NumberFormatException nfe){
		    if(!"T2".equals(loginBrandCode))
		        errorMsgs.add("來客數必須為數字！");
		    else
			errorMsgs.add("交易筆數必須為數字！");
		}  
	    }
	
	    if (!StringUtils.hasText(totalActualSalesAmount)) {
	        errorMsgs.add("請輸入營收彙總金額！");
	    }else{
		try{
		    actualTotalActualSalesAmount = Double.parseDouble(totalActualSalesAmount);
		}catch(NumberFormatException nfe){
		    errorMsgs.add("營收彙總金額必須為數字！");
		}
	    }   
	    
	    if(errorMsgs.size() == 0){
	        SoShopDailyHeadId id = new SoShopDailyHeadId();
	        id.setShopCode(shopCode);
	        id.setSalesDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, salesDate));
	        shopDailyHead = new SoShopDailyHead();
	        shopDailyHead.setId(id);
	        shopDailyHead.setVisitorCount(actualVisitorCount);
	        shopDailyHead.setTotalActualSalesAmount(actualTotalActualSalesAmount);
	    }
	    
	    return shopDailyHead;	    
	}catch(Exception ex){
	    log.error("檢核POS每日資料失敗，原因：" + ex.toString());
	    throw new Exception("檢核POS每日資料失敗，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 執行POS每日資料查詢初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");	
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");	    
	    Map multiList = new HashMap(0);
	    if(!"T2".equals(loginBrandCode)){
	        List<BuShop> allShops = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	        multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCode", false, true));
	    }else{
		List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	        multiList.put("allShops", AjaxUtils.produceSelectorData(allShopMachines, "posMachineCode", "posMachineCode", false, true));		
	    }	    
	    
	    resultMap.put("multiList",multiList);
		
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("POS每日資料查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("POS每日資料查詢初始化失敗，原因：" + ex.toString());
	}           
    }
    
    /**
     * 顯示查詢頁面的line
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

    	try{
    	    List<Properties> result = new ArrayList();
    	    List<Properties> gridDatas = new ArrayList();
    	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小  	  
    	    //======================帶入查詢的值=========================
    	    String brandCode = httpRequest.getProperty("brandCode");
    	    String employeeCode = httpRequest.getProperty("employeeCode");
    	    String shopCode = httpRequest.getProperty("shopCode");// 營業單位
    	    String batch = httpRequest.getProperty("batch");// 班次   
    	    StringBuffer searchShopCodes = new StringBuffer();
    	    String startSalesDate = httpRequest.getProperty("startSalesDate");
    	    String endSalesDate = httpRequest.getProperty("endSalesDate");
    	    String incharge = httpRequest.getProperty("incharge");
    	    
    	    Date actualStartSalesDate = null;
    	    Date actualEndSalesDate = null;
    	    if(StringUtils.hasText(startSalesDate)){
    		actualStartSalesDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startSalesDate);
    	    }
    	    if(StringUtils.hasText(endSalesDate)){
    		actualEndSalesDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endSalesDate);
	    }  
    	    //======================組出欲查詢的營業單位=========================
    	    if(!StringUtils.hasText(shopCode)){
                if(!"T2".equals(brandCode)){
    	            List<BuShop> allShops = buBasicDataService.getShopForEmployee(brandCode, employeeCode, null);
    	            for(int i = 0; i < allShops.size(); i++){
    	        	BuShop buShop = (BuShop)allShops.get(i);
    	        	searchShopCodes.append("'");
    	        	searchShopCodes.append(buShop.getShopCode());
    	    		searchShopCodes.append("'");
    	    		if(i != allShops.size() - 1)
    	    		    searchShopCodes.append(",");   	        	
    	            }   	        
    	        }else{
    		    List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y", incharge);
    		    for(int j = 0; j < allShopMachines.size(); j++){
    			BuShopMachine buShopMachine = (BuShopMachine)allShopMachines.get(j);
 	        	searchShopCodes.append("'");
 	        	searchShopCodes.append(buShopMachine.getId().getPosMachineCode());
 	    		searchShopCodes.append("'");
 	    		if(j != allShopMachines.size() - 1)
 	    		    searchShopCodes.append(",");   	        	
 	            }   
    	       		
    	        }    		
    	    }else{
    		searchShopCodes.append("'");
    		searchShopCodes.append(shopCode);
    		searchShopCodes.append("'");
    	    }
    	    //=======================執行查詢==============================   	   
    	    HashMap findObjs = new HashMap();
    	    findObjs.put("shopCodes", searchShopCodes.toString());
    	    findObjs.put("startSalesDate", actualStartSalesDate);
    	    findObjs.put("endSalesDate", actualEndSalesDate);	
    	    findObjs.put("batch", batch);	
    	    log.info("batchbatch="+batch);
    	    HashMap shopDailyHeadMap = soShopDailyHeadDAO.findPageLine(findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
    	    List<SoShopDailyHead> shopDailyHeads = (List<SoShopDailyHead>)shopDailyHeadMap.get(BaseDAO.TABLE_LIST);
    	    System.out.println("ResultSize=["+ shopDailyHeads.size() + "]");
    	    if (shopDailyHeads != null && shopDailyHeads.size() > 0) {
    	    	Long firstIndex =Long.valueOf(iSPage * iPSize) + 1;    // 取得第一筆的INDEX 
    	        shopDailyHeadMap = soShopDailyHeadDAO.findPageLine(findObjs, -1, iPSize, BaseDAO.QUERY_RECORD_COUNT);
    	    	Long maxIndex = (Long)shopDailyHeadMap.get(BaseDAO.TABLE_RECORD_COUNT);	// 取得最後一筆 INDEX
    	        produceReturnWrappedBeans(shopDailyHeads);
    	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, shopDailyHeads, gridDatas, firstIndex, maxIndex));
    	    } else {
    	        result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    	    }

    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的POS每日資料查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的POS每日資料查詢失敗！");
    	}	
    }
    
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	//AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    public Map getSearchSelection(Map parameterMap) throws Exception{
	
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 )
	        pickerResult.put("result", result);
	    
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	    
	    return resultMap;
	}catch(Exception ex){
	    log.error("執行POS每日資料檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行POS每日資料檢視失敗，原因：" + ex.getMessage());		
	}
    }
    
    /**
     * 執行POS每日資料維護初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeMaintainInitial(Map parameterMap) throws Exception{
        
        HashMap resultMap = new HashMap();
        try{
            Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");	
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");	    
	    Map multiList = new HashMap(0);
	    if(!"T2".equals(loginBrandCode)){
	        List<BuShop> allShops = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	        multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCode", false, true));
	    }else{
		List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	        multiList.put("allShops", AjaxUtils.produceSelectorData(allShopMachines, "posMachineCode", "posMachineCode", false, true));
	    List<BuBatchConfig>  allBatch = buShopMachineDAO.findBatchTime();
	        multiList.put("allBatch", AjaxUtils.produceSelectorData(allBatch, "batchId", "batchId", false, false));
	        log.info("allBatch="+allBatch);
	    }	    
	    String lastSalesDate = DateUtils.format(DateUtils.addDays(new Date(), -1), DateUtils.C_DATE_PATTON_SLASH);
	    resultMap.put("multiList",multiList);
	    resultMap.put("lastSalesDate", lastSalesDate);
	    
	    return resultMap;       	
        }catch (Exception ex) {
	    log.error("POS每日資料維護初始化失敗，原因：" + ex.toString());
	    throw new Exception("POS每日資料維護初始化失敗，原因：" + ex.toString());
	}           
    }
    public void createDailyData(Map conditionMap) throws Exception{
    	String loginBrandCode = (String)conditionMap.get("loginBrandCode");
    	String loginEmployeeCode = (String)conditionMap.get("loginEmployeeCode");
    	//String shopCode = (String)conditionMap.get("shopCode");
    	String salesUnit = (String)conditionMap.get("salesUnit");
    	String batch = (String)conditionMap.get("batch");
    	log.info("createMainBatch = "+batch);
    	Date startSalesDate = (Date)conditionMap.get("startSalesDate");
    	Date endSalesDate = (Date)conditionMap.get("endSalesDate");
    	Date currentDate = new Date();
    	List allShops = null;
    	
    	try{
    		allShops = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
    		
    	    String actualShopCode  = null;
    	    Date IdDate = null;
    	    BuShop buShop = null;	   
    	    if(allShops != null && allShops.size() > 0)
    	    {
    	    	for(int i = 0; i < allShops.size(); i++)
    	    	{
    	    		BuShopMachine buShopMachine = (BuShopMachine)allShops.get(i);
    	    		
    	    		actualShopCode = buShopMachine.getId().getPosMachineCode();    
    	    		log.info("MACHINE="+actualShopCode);
    	    		buShop = buShopDAO.findShopCode(buShopMachine.getId().getShopCode());                        

	    	    	SoShopDailyHead soByBatch = (SoShopDailyHead)soShopDailyHeadDAO.findHeadByBatch(actualShopCode,endSalesDate,batch);
	    	    	if(null==soByBatch)
	    	    	{
	    	    		log.info("營業銷售資料建立:"+actualShopCode);
	    	    		soShopDailyHeadDAO.save(produceShopDailyData(actualShopCode, endSalesDate, loginEmployeeCode,batch, currentDate));
	    	    		
	
	    	    	}
	    	    	else
	    	    	{
	    	    		log.info("沒東西");
	    	    	}
	    	       	log.info("shop:"+actualShopCode);
	    	    	log.info("tran:"+endSalesDate);
	    	    	log.info("batch:"+batch);
	    	    	SoPostingTally postingByBatch = (SoPostingTally)soPostingTallyDAO.findHeadByBatch(actualShopCode,endSalesDate,batch);
	    	    	if(null==postingByBatch)
	    	    	{
	    	    		log.info("過帳資料建立:"+actualShopCode);
	    	    		soShopDailyHeadDAO.save(produceSoPostingTally(loginBrandCode,loginEmployeeCode, endSalesDate,actualShopCode, batch, currentDate));
	    	    	}
	    	    	else
	    	    	{
	    	    		log.info("也沒東西");
	    	    	}
    	    	}
    	    }
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		log.error("POS每日資料維護建立查詢資料失敗，原因：" + ex.toString());
    		throw new Exception(ex.getMessage());
    	}
    }
    
    
    /**
     * 執行POS每日資料維護初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public void createMaintainData(Map conditionMap) throws Exception{
        
	int limitDays = 6;
	int rangeDays = 0;
	String loginBrandCode = (String)conditionMap.get("loginBrandCode");
	String loginEmployeeCode = (String)conditionMap.get("loginEmployeeCode");
	String shopCode = (String)conditionMap.get("shopCode");
	String salesUnit = (String)conditionMap.get("salesUnit");
	String batch = (String)conditionMap.get("batch");
	log.info("createMainBatch = "+batch);
	Date startSalesDate = (Date)conditionMap.get("startSalesDate");
	Date endSalesDate = (Date)conditionMap.get("endSalesDate");
	Date currentDate = new Date();
	List allShops = null;
	try{
	    //========================執行檢核======================================
	    if(startSalesDate.after(endSalesDate)){
	        throw new ValidationErrorException("銷售日期的起始日期不可大於結束日期！");
	    }else if((rangeDays = DateUtils.daysBetweenWithoutTime(startSalesDate, endSalesDate)) > limitDays){
	        throw new ValidationErrorException("建立資料時銷售起始日期與結束日期之間不可大於" + limitDays + "天！");
	    }
	    //========================查詢欲建立的專櫃或機台====================================== 
	    if(!"T2".equals(loginBrandCode)){
		if(StringUtils.hasText(shopCode)){
		    BuShop newBuShop = new BuShop();
		    newBuShop.setShopCode(shopCode);
		    newBuShop.setShopType("1");
		    allShops = new ArrayList();
		    allShops.add(newBuShop);
	        }else{
		    allShops = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
	        }
	    }else{
		if(StringUtils.hasText(shopCode)){
		    BuShopMachineId newShopMachineId = new BuShopMachineId();
		    newShopMachineId.setPosMachineCode(shopCode);
		    BuShopMachine newShopMachine = new BuShopMachine();
		    newShopMachine.setId(newShopMachineId);
		    allShops = new ArrayList();
		    allShops.add(newShopMachine);		    
		}else{
	            allShops = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
		}
	    }	       
	    //========================建立專櫃或機台=============================================
	    List createDates = new ArrayList();
	    for(int index = 0; index <= rangeDays; index++) {
	        createDates.add(DateUtils.addDays(startSalesDate, index));		    
	    }
	    String actualShopCode  = null;
	    Date IdDate = null;
	    BuShop buShop = null;	    
	    if(allShops != null && allShops.size() > 0){
                for(int i = 0; i < allShops.size(); i++){
                    //String actualShopCode  = null;
                    if(!"T2".equals(loginBrandCode)){
                    	 //BuShop buShop = null;
                         buShop = (BuShop)allShops.get(i);
                        if("1".equals(buShop.getShopType())){
                            actualShopCode = buShop.getShopCode();                      	
                        }else{
                            continue;
                        }                           
                    }else{
                        BuShopMachine buShopMachine = (BuShopMachine)allShops.get(i);
                        actualShopCode = buShopMachine.getId().getPosMachineCode();                                               
                        buShop = buShopDAO.findShopCode(buShopMachine.getId().getShopCode());                        
                    }
                    for(int j = 0; j < createDates.size(); j++){
                        Date salesDate = (Date)createDates.get(j);
                        SoShopDailyHead soByBatch = soShopDailyHeadDAO.findHeadByBatch(actualShopCode,salesDate,batch);
                        if(null==soByBatch){
                        	soShopDailyHeadDAO.save(produceShopDailyData(actualShopCode, salesDate, loginEmployeeCode,batch, currentDate));
                        	IdDate = (Date)createDates.get(j); 
                        }else{
                        	
                        }
                        /*if(findSoShopDailyHeadById(new SoShopDailyHeadId(actualShopCode, salesDate), salesUnit) == null){
                            soShopDailyHeadDAO.save(produceShopDailyData(actualShopCode, salesDate, loginEmployeeCode,batch, currentDate));
                            IdDate = (Date)createDates.get(j); 
                        }*/               
                    }                
                }		
	    }
	    //log.info("create1"+buShop.getBrandCode());
	    SoShopDailyHeadId soShopDailyHeadId = new SoShopDailyHeadId(actualShopCode, IdDate, batch);
	    SoShopDailyHead soShopDailyHead = findSoShopDailyHeadById(soShopDailyHeadId, salesUnit);	   
	   if(buShop.getBrandCode().equals("T2") ){
	    	log.info("create PostingTally"+batch);	    		    						
			Date executeDate = endSalesDate;
			//DateUtils.addDays(executeDate, -1)
	    	SoPostingTallyService postingTallyService = (SoPostingTallyService)SpringUtils.getApplicationContext().getBean("soPostingTallyService");			
			postingTallyService.createT2PostingTallyWithSoDailyHead(executeDate, buShop.getBrandCode(), loginEmployeeCode,batch);
	    }else{
	    	log.info("create PostingTally"+batch);	    		    						
			Date executeDate = endSalesDate;
			//DateUtils.addDays(executeDate, -1)
	    	SoPostingTallyService postingTallyService = (SoPostingTallyService)SpringUtils.getApplicationContext().getBean("soPostingTallyService");			
			postingTallyService.createT2PostingTallyWithSoDailyHead(executeDate, buShop.getBrandCode(), loginEmployeeCode,batch);
	    }
        }catch (Exception ex) {
	    log.error("POS每日資料維護建立查詢資料失敗，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}           
    }
    
    private SoShopDailyHead produceShopDailyData(String shopCode, Date salesDate, String loginUser, String batch, Date currentDate){
	
	SoShopDailyHeadId id = new SoShopDailyHeadId();
	id.setShopCode(shopCode);
	id.setSalesDate(salesDate);
	id.setBatch(batch);
	SoShopDailyHead shopDailyHead = new SoShopDailyHead();
	shopDailyHead.setId(id);
	shopDailyHead.setVisitorCount(0L);
	shopDailyHead.setTotalActualSalesAmount(0D);	
	shopDailyHead.setCreatedBy(loginUser);
	shopDailyHead.setCreationDate(currentDate);
	shopDailyHead.setLastUpdatedBy(loginUser);
	shopDailyHead.setLastUpdateDate(currentDate);
	
	return shopDailyHead;
    }
    private SoPostingTally produceSoPostingTally(String brandCode,String loginUser, Date salesDate,String posMachineCode,String batch, Date currentDate){
    	

		SoPostingTallyId soPostingTallyId = new SoPostingTallyId();
		SoPostingTally soPostingTally = new SoPostingTally();
		soPostingTallyId.setShopCode(posMachineCode);
		soPostingTallyId.setTransactionDate(salesDate);
		soPostingTallyId.setBatch(batch);
		soPostingTally.setBrandCode(brandCode);
		soPostingTally.setCreateDate(new Date());
		soPostingTally.setCreatedBy(loginUser);
		soPostingTally.setIsPosting("N");    						
		soPostingTally.setLastUpdateDate(new Date());
		soPostingTally.setLastUpdatedBy(loginUser);
		soPostingTally.setId(soPostingTallyId);
    	
    	return soPostingTally;
        }
    /**
     * 更新PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
	
        try{
            String employeeCode = httpRequest.getProperty("employeeCode");
            String batch = httpRequest.getProperty("batch");     
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));	
	    String errorMsg = null;
	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_MAINTAIN_FIELD_NAMES);
	    if (upRecords != null) {
		Date currentDate = new Date();
	        for (Properties upRecord : upRecords) {
		    // 先載入HEAD_ID OR LINE DATA
	            String shopCode = upRecord.getProperty(GRID_MAINTAIN_FIELD_NAMES[1]);
		    String salesDate = upRecord.getProperty(GRID_MAINTAIN_FIELD_NAMES[2]);
		   //String batch = upRecord.getProperty(GRID_MAINTAIN_FIELD_NAMES[5]);		    
		    if (StringUtils.hasText(shopCode) && StringUtils.hasText(salesDate)) {
			Date actualSalesDate = DateUtils.parseDate("yyyy/MM/dd", salesDate);
			SoShopDailyHead shopDailyHeadPO = (SoShopDailyHead) soShopDailyHeadDAO.findByPrimaryKey(SoShopDailyHead.class, 
				new SoShopDailyHeadId(shopCode, actualSalesDate, batch));
			if(shopDailyHeadPO != null){
			    Long visitorCount = NumberUtils.getLong(upRecord.getProperty(GRID_MAINTAIN_FIELD_NAMES[3]));
			    Double totalActualSalesAmount = NumberUtils.getDouble(upRecord.getProperty(GRID_MAINTAIN_FIELD_NAMES[4]));			    
			    long origVisitorCount = 0L;
			    double origTotalActualSalesAmount = 0D;
			    if(shopDailyHeadPO.getVisitorCount() != null){
				origVisitorCount = (shopDailyHeadPO.getVisitorCount()).longValue();
			    }
			    if(shopDailyHeadPO.getTotalActualSalesAmount() != null){
				origTotalActualSalesAmount = (shopDailyHeadPO.getTotalActualSalesAmount()).doubleValue();
			    }
			    if(visitorCount.longValue() != origVisitorCount || totalActualSalesAmount.doubleValue() != origTotalActualSalesAmount){
				shopDailyHeadPO.setVisitorCount(visitorCount);
				shopDailyHeadPO.setTotalActualSalesAmount(totalActualSalesAmount);
				shopDailyHeadPO.setLastUpdatedBy(employeeCode);
				shopDailyHeadPO.setLastUpdateDate(currentDate);	
				soShopDailyHeadDAO.update(shopDailyHeadPO);
			    }			    
			}
	            }
		}
	    }	    
	    
	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新POS每日資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新POS每日資料失敗！"); 
        }	
    }
    
    private void produceReturnWrappedBeans(List searchResult){
	
	if(searchResult != null && searchResult.size() > 0){
	    DecimalFormat df = new DecimalFormat("###,###,###,###");
	    for(int i = 0; i < searchResult.size(); i++){
		SoShopDailyHead shopDailyHead = (SoShopDailyHead)searchResult.get(i);
		Double totalActualSalesAmount = shopDailyHead.getTotalActualSalesAmount();
		Date lastUpdateDate = shopDailyHead.getLastUpdateDate();
		if(totalActualSalesAmount != null){
		    shopDailyHead.setActualSalesAmount(df.format(totalActualSalesAmount.doubleValue()));
		}
		if(lastUpdateDate != null){
		    shopDailyHead.setLastUpdateTime(DateUtils.format(lastUpdateDate, DateUtils.C_TIME_PATTON_SLASH));
		}	       
	    }	    
	}	
    }
    
    /**
     * 顯示查詢頁面的line
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getTotalAccount(Properties httpRequest) throws Exception{

    	try{
    	    List<Properties> result = new ArrayList();
    	    //======================帶入查詢的值========================= 
    	    String brandCode = httpRequest.getProperty("brandCode");
    	    String employeeCode = httpRequest.getProperty("employeeCode");
    	    String shopCode = httpRequest.getProperty("shopCode");// 營業單位 
    	    String batch = httpRequest.getProperty("batch"); 
    	    StringBuffer searchShopCodes = new StringBuffer();
    	    String startSalesDate = httpRequest.getProperty("startSalesDate");
    	    String endSalesDate = httpRequest.getProperty("endSalesDate");
    	    //======================組出欲查詢的營業單位=========================
    	    if(!StringUtils.hasText(shopCode)){
                if(!"T2".equals(brandCode)){
    	            List<BuShop> allShops = buBasicDataService.getShopForEmployee(brandCode, employeeCode, null);
    	            for(int i = 0; i < allShops.size(); i++){
    	        	BuShop buShop = (BuShop)allShops.get(i);
    	        	searchShopCodes.append("'");
    	        	searchShopCodes.append(buShop.getShopCode());
    	    		searchShopCodes.append("'");
    	    		if(i != allShops.size() - 1)
    	    		    searchShopCodes.append(",");   	        	
    	            }   	        
    	        }else{
    		    List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, null);
    		    for(int j = 0; j < allShopMachines.size(); j++){
    			BuShopMachine buShopMachine = (BuShopMachine)allShopMachines.get(j);
 	        	searchShopCodes.append("'");
 	        	searchShopCodes.append(buShopMachine.getId().getPosMachineCode());
 	    		searchShopCodes.append("'");
 	    		if(j != allShopMachines.size() - 1)
 	    		    searchShopCodes.append(",");   	        	
 	            }   
    	       		
    	        }    		
    	    }else{
	    		searchShopCodes.append("'");
	    		searchShopCodes.append(shopCode);
	    		searchShopCodes.append("'");
    	    }
    	    
    	    String tmpSqlItem = " select sum(nvl(TOTAL_ACTUAL_SALES_AMOUNT,0)) as totalActualSalesAmount from SO_SHOP_DAILY_HEAD " +
    	    					" where SHOP_CODE in ("+searchShopCodes.toString()+") " +
    	    					" and SALES_DATE >= to_date('"+startSalesDate+"','yyyy/MM/dd') " +
    	    					" and SALES_DATE <= to_date('"+endSalesDate+"','yyyy/MM/dd') ";
    	    //營業銷售分班次
    	    					if("T2".equals(brandCode))
    	    					{
    	    						tmpSqlItem = tmpSqlItem + " and BATCH = " + batch ;
    	    					}
    	    String tmpSqlItem1= " select sum(nvl(VISITOR_COUNT,0)) as totalActualSalesCount from SO_SHOP_DAILY_HEAD " +
    	    					" where SHOP_CODE in ("+searchShopCodes.toString()+") " +
    	    					" and SALES_DATE >= to_date('"+startSalesDate+"','yyyy/MM/dd') " +
    	    					" and SALES_DATE <= to_date('"+endSalesDate+"','yyyy/MM/dd') ";
    	    //營業銷售分班次
    	    					if("T2".equals(brandCode))
    	    					{
    	    						tmpSqlItem1 = tmpSqlItem1 + " and BATCH = " + batch ;
    	    					}
    	    
    	    List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
    	    List itemList1 = nativeQueryDAO.executeNativeSql(tmpSqlItem1);
    	    Object obj = itemList.get(0);
    	    Object obj1 = itemList1.get(0);
			Double totalActualSalesAmount = ((BigDecimal)obj).doubleValue();
			Double totalActualSalesCount  = ((BigDecimal)obj1).doubleValue();
			
			log.info("totalActualSalesAmount = " + totalActualSalesAmount);
			log.info("totalActualSalesCount = " + totalActualSalesCount);
			Properties properties = new Properties();
			properties.setProperty("TotalActualSalesAmount", OperationUtils.roundToStr(totalActualSalesAmount,0));
			properties.setProperty("TotalActualSalesCount", OperationUtils.roundToStr(totalActualSalesCount,0));
			result.add(properties);
    	    return result;
    	}catch(Exception ex){
    	    log.error("載入頁面顯示的POS每日資料查詢發生錯誤，原因：" + ex.toString());
    	    throw new Exception("載入頁面顯示的POS每日資料查詢失敗！");
    	}	
    }
}
