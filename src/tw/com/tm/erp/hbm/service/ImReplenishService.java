package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
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
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuGoalEmployeeLine;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeHead;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeLine;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameter;
import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameterId;
import tw.com.tm.erp.hbm.bean.ImReplenishCalendar;
import tw.com.tm.erp.hbm.bean.ImReplenishDisplay;
import tw.com.tm.erp.hbm.bean.ImReplenishHead;
import tw.com.tm.erp.hbm.bean.ImReplenishLimition;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishCalendarDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishDisplayDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishLimitionDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImReplenishService {
    private static final Log log = LogFactory.getLog(ImReplenishService.class);
    
    private BaseDAO baseDAO;
    private BuBrandDAO buBrandDAO;
    private ImWarehouseDAO imWarehouseDAO;
    private ImReplenishHeadDAO imReplenishHeadDAO;
    private ImReplenishDisplayDAO imReplenishDisplayDAO;
    private ImReplenishLimitionDAO imReplenishLimitionDAO;
    private ImReplenishCalendarDAO imReplenishCalendarDAO;
    private ImItemDAO imItemDAO;
    private ImItemCategoryDAO imItemCategoryDAO;
    
    private ImReplenishCalendarService imReplenishCalendarService;
    
    public void setImReplenishCalendarService(
    	ImReplenishCalendarService imReplenishCalendarService) {
        this.imReplenishCalendarService = imReplenishCalendarService;
    }

    public void setImReplenishLimitionDAO(
    	ImReplenishLimitionDAO imReplenishLimitionDAO) {
        this.imReplenishLimitionDAO = imReplenishLimitionDAO;
    }

    public void setImReplenishDisplayDAO(ImReplenishDisplayDAO imReplenishDisplayDAO) {
        this.imReplenishDisplayDAO = imReplenishDisplayDAO;
    }

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
        this.imItemCategoryDAO = imItemCategoryDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
        this.imItemDAO = imItemDAO;
    }

    public void setImReplenishHeadDAO(ImReplenishHeadDAO imReplenishHeadDAO) {
        this.imReplenishHeadDAO = imReplenishHeadDAO;
    }

    public void setImReplenishCalendarDAO(
    	ImReplenishCalendarDAO imReplenishCalendarDAO) {
        this.imReplenishCalendarDAO = imReplenishCalendarDAO;
    }

    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
        this.imWarehouseDAO = imWarehouseDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /**
     * 展示明細欄位
     */
    public static final String[] GRID_DISPLAY_FIELD_NAMES = { 
	"indexNo", "itemCode", "itemCName", 
	"displayQuantity", "lineId", "isLockRecord", 
	"isDeleteRecord", "message"
    };

    /**
     * 展示明細預設值
     */
    public static final String[] GRID_DISPLAY_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", "", AjaxUtils.IS_LOCK_RECORD_FALSE, 
	AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };   	
    
    /**
     * 展示明細對應型態
     */
    public static final int[] GRID_DISPLAY_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE
    };
    
    /**
     * 限制明細欄位
     */
    public static final String[] GRID_LIMITION_FIELD_NAMES = { 
	"indexNo", "itemCode", "itemCName", 
	//"category00", "category00Name", "category01",
	//"category01Name", "category02", "category02Name",
	"itemBrand", "reserve1", "itemBrandName",  "lineId",
	"isLockRecord", "isDeleteRecord", "message"
    };

    /**
     * 限制明細預設值
     */
    public static final String[] GRID_LIMITION_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	//"", "", "",
	//"", "", "",
	"", "", "", "",
	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };   
    
    /**
     * 限制明細對應型態
     */
    public static final int[] GRID_LIMITION_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	//AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, 
	//AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };
    
    /**
     * 日曆明細欄位
     */
    public static final String[] GRID_CALENDAR_FIELD_NAMES = { 
	"indexNo", "replenishDate", "replenishType", 
	"lowStockQuantity", "highStockQuantity", "lineId",
	"isLockRecord", "isDeleteRecord", "message"
    };

    /**
     * 日曆明細預設值
     */
    public static final String[] GRID_CALENDAR_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", "", "",
	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };   
    
    /**
     * 日曆明細對應型態
     */
    public static final int[] GRID_CALENDAR_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_LONG, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING
    };
    
    
    /**
     * 自動補貨查詢明細
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"warehouseCode", "warehouseName", 	"warehouseArea",
	"creationDate", "createdNameBy", 	"lastUpdateDate", 
	"lastUpdatedNameBy","headId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "", "",
	"", "", "", 
	"", ""
    };
    
    /**
     * 基本參數自動補貨查詢明細
     */
    public static final String[] GRID_BASIC_PARAMETER_SEARCH_FIELD_NAMES = { 
	"id.type", "id.parameter", "value"
    };

    public static final String[] GRID_BASIC_PARAMETER_SEARCH_FIELD_DEFAULT_VALUES = { 
	"", "", ""
    };
    
    /**
     * 將明細表 mark 為刪除的刪掉
     * @param head
     */
    private void deleteLine(ImReplenishHead head){
	
	List<ImReplenishCalendar> imReplenishCalendars = head.getImReplenishCalendarLines(); //(List<ImReplenishCalendar>)baseDAO.findByProperty("ImReplenishCalendar", "and head.headId = ?", new Object[]{head.getHeadId()});
	if(imReplenishCalendars != null && imReplenishCalendars.size() > 0){
	    for(int i = imReplenishCalendars.size() - 1; i >= 0; i--){
		ImReplenishCalendar imReplenishCalendar = imReplenishCalendars.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imReplenishCalendar.getIsDeleteRecord())){
		    imReplenishCalendars.remove(imReplenishCalendar);
		    imReplenishCalendarDAO.delete(imReplenishCalendar);
		}
	    }
	}
	List<ImReplenishDisplay> imReplenishDisplays = head.getImReplenishDisplayLines(); //(List<ImReplenishDisplay>)baseDAO.findByProperty("ImReplenishDisplay", "and head.headId = ?", new Object[]{head.getHeadId()});
	if(imReplenishDisplays != null && imReplenishDisplays.size() > 0){
	    for(int i = imReplenishDisplays.size() - 1; i >= 0; i--){
		ImReplenishDisplay imReplenishDisplay = imReplenishDisplays.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imReplenishDisplay.getIsDeleteRecord())){
		    imReplenishDisplays.remove(imReplenishDisplay);
		    imReplenishDisplayDAO.delete(imReplenishDisplay);
		}
	    }
	}
	
	List<ImReplenishLimition> imReplenishLimitions = head.getImReplenishLimitionLines(); //(List<ImReplenishLimition>)baseDAO.findByProperty("ImReplenishLimition", "and head.headId = ?", new Object[]{head.getHeadId()});
	if(imReplenishLimitions != null && imReplenishLimitions.size() > 0){
	    for(int i = imReplenishLimitions.size() - 1; i >= 0; i--){
		ImReplenishLimition imReplenishLimition = imReplenishLimitions.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imReplenishLimition.getIsDeleteRecord())){
		    imReplenishLimitions.remove(imReplenishLimition);
		    imReplenishLimitionDAO.delete(imReplenishLimition);
		}
	    }
	}
	
    }
    
    /**
     * 自動補貨初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	HashMap findObjs = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    ImReplenishHead head = getActualHead(otherBean, resultMap);

	    resultMap.put("form", head);
	    resultMap.put("brandName",buBrandDAO.findById(head.getBrandCode()).getBrandName());
	    resultMap.put("createByName", employeeName);

	    findObjs.put("brandCode", loginBrandCode);
	    List<ImWarehouse> allWarehouses = imWarehouseDAO.find(findObjs);
	    
	    multiList.put("allWarehouses"	, AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true, head.getWarehouseCode() != null ? head.getWarehouseCode() : "" ));
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH.getServiceInitial()+ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getServiceInitial()+ex.getMessage());

	}
    }
    
    /**
     * 自動補貨查詢初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	HashMap findObjs = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    findObjs.put("brandCode", loginBrandCode);
	    
	    List<ImWarehouse> allWarehouses = imWarehouseDAO.find(findObjs);
	    
	    multiList.put("allWarehouses"	, AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true, "" ));
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH.getServiceSearchInitial()+ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getServiceSearchInitial()+ex.getMessage());
	}
    }
    
    /**
     * 自動補貨查詢初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeBasicParameterSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	HashMap findObjs = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    findObjs.put("brandCode", loginBrandCode);
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceSearchInitial()+ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceSearchInitial()+ex.getMessage());
	}
    }
    
    /**
     * 自動補貨初始化 bean 額外顯示欄位
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeBasicParameterInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	HashMap findObjs = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    ImReplenishBasicParameter head = getActualBasicParameter(otherBean, resultMap);

	    resultMap.put("form", head);
	    resultMap.put("brandName",buBrandDAO.findById(head.getId().getBrandCode()).getBrandName());
	    resultMap.put("createByName", employeeName);

	    findObjs.put("brandCode", loginBrandCode);
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceInitial()+ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceInitial()+ex.getMessage());

	}
    }
    
    /**
     * 建立一個初始化的bean
     * @param otherBean
     * @return
     * @throws Exception
     */
    public ImReplenishHead executeNew(Object otherBean) throws Exception{
	ImReplenishHead head = new ImReplenishHead();
	try {
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    head.setBrandCode(loginBrandCode);
	    head.setWarehouseCode(null);
	    head.setWarehouseArea(null);
	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());

	} catch (Exception e) {
	    log.error("建立新"+Imformation.AUTO_REPLENISH.getDescription()+"主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新"+Imformation.AUTO_REPLENISH.getDescription()+"主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 建立一個初始化的bean
     * @param otherBean
     * @return
     * @throws Exception
     */
    public ImReplenishBasicParameter executeNewBasicParameter(Object otherBean) throws Exception{
	ImReplenishBasicParameter head = new ImReplenishBasicParameter();
	ImReplenishBasicParameterId pId  = new ImReplenishBasicParameterId();
	try {
	    log.info("1=1");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    log.info("loginEmployeeCode = " + loginEmployeeCode);
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    log.info("loginBrandCode = " + loginBrandCode);
	    
	    pId.setBrandCode(loginBrandCode);
	    pId.setType(null);
	    pId.setParameter(null);
	    
	    head.setValue(null);
	    head.setId(pId);
//	    head.getId().setCreatedBy(loginEmployeeCode);
//	    head.getId().setCreationDate(new Date());
//	    head.getId().setLastUpdatedBy(loginEmployeeCode);
//	    head.getId().setLastUpdateDate(new Date());

	} catch (Exception e) {
	    log.error("建立新"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 依formId查詢出bean
     * @param formId
     * @return
     * @throws Exception
     */
    public ImReplenishHead executeFind(Long formId) throws Exception {
	try {
	    ImReplenishHead head = (ImReplenishHead) baseDAO.findByPrimaryKey(ImReplenishHead.class,formId);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + formId + "查詢"+Imformation.AUTO_REPLENISH.getDescription()+"主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + formId + "查詢"+Imformation.AUTO_REPLENISH.getDescription()+"主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 依formId查詢出bean
     * @param formId
     * @return
     * @throws Exception
     */
    public ImReplenishBasicParameter executeFindBasicParameter(Object otherBean) throws Exception {
	ImReplenishBasicParameterId id = null;
	try {
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String type = (String)PropertyUtils.getProperty(otherBean, "type");
	    Double parameter = NumberUtils.getDouble((String)PropertyUtils.getProperty(otherBean, "parameter"));
	    
	    id = new ImReplenishBasicParameterId(loginBrandCode,type,parameter);
	    ImReplenishBasicParameter head = (ImReplenishBasicParameter) baseDAO.findByPrimaryKey(ImReplenishBasicParameter.class,id);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + id + "查詢"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + id + "查詢"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    
    
    /**
     * 尋找匹配 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> executeMatch(Properties httpRequest) throws Exception{
	log.info( "===========<executeMatch>===========");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Date date = new Date();
	String condition = null;
	Object[] values = null;
	try{
	    // 取得複製時所需的必要資訊
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String warehouseCode = httpRequest.getProperty("warehouseCode");
	    String warehouseArea = httpRequest.getProperty("warehouseArea");

	    log.info( "brandCode = " + brandCode );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "warehouseArea = " + warehouseArea );

	    if(StringUtils.hasText(warehouseArea)){
		condition = "and brandCode = ? and warehouseCode = ? and warehouseArea = ? ";
		values = new Object[]{brandCode, warehouseCode, warehouseArea};
	    }else{
		condition = "and brandCode = ? and warehouseCode = ? ";
		values = new Object[]{brandCode, warehouseCode };
	    }
	    
	    ImReplenishHead head = (ImReplenishHead)baseDAO.findFirstByProperty("ImReplenishHead", condition, values);

	    if( head != null ){
		log.info( " 已存在定義檔 " );
	    }else{
		log.info( " 不存在定義檔 " );

		// 設定單頭 
		head = new ImReplenishHead();
		head.setBrandCode(brandCode);
		head.setWarehouseCode(warehouseCode);
		head.setWarehouseArea(warehouseArea);
		head.setCreatedBy(loginEmployeeCode);
		head.setCreationDate(date);
		head.setLastUpdatedBy(loginEmployeeCode);
		head.setLastUpdateDate(date);

		baseDAO.save(head);
	    }

	    properties.setProperty("headId", String.valueOf(head.getHeadId()));

	    result.add(properties);
	    log.info( "===========<executeMatch/>===========");
	    return result;
	}catch(Exception ex){
	    log.error("尋找匹配時發生錯誤，原因：" + ex.toString());
	    throw new Exception("尋找匹配時發生錯誤，原因：" + ex.getMessage());
	}

    }
    
    /**
     * 匯入日曆明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportCalendarLists(Long headId, List lineLists) throws Exception{
	try{
	    imReplenishCalendarService.deleteCalendarLine(headId);

	    if(lineLists != null && lineLists.size() > 0){
		ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
		for(int i = 0; i < lineLists.size(); i++){
		    ImReplenishCalendar  line = (ImReplenishCalendar)lineLists.get(i);
		    line.setHead(head);
		    line.setIndexNo(i+1L);
		    imReplenishHeadDAO.save(line);
		}      	    
	    }     	
	}catch (Exception ex) {
	    log.error("自動補貨日曆明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自動補貨日曆明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    /**
     * 匯入陳列明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportDisplayLists(Long headId, List lineLists) throws Exception{
	try{
	    imReplenishCalendarService.deleteDisplayLine(headId);
	    if(lineLists != null && lineLists.size() > 0){
		ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
		for(int i = 0; i < lineLists.size(); i++){
		    ImReplenishDisplay  line = (ImReplenishDisplay)lineLists.get(i);
		    line.setHead(head);
		    line.setIndexNo(i+1L);
		    imReplenishHeadDAO.save(line);
		}      	    
	    }     	
	}catch (Exception ex) {
	    log.error("自動補貨陳列明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("自動補貨陳列明細匯入時發生錯誤，原因：" + ex.getMessage());
	}        
    }
    
    /**
     * 匯入限制明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportLimitionLists(Long headId, List lineLists) throws Exception{
    	try{
    		imReplenishCalendarService.deleteLimitionLine(headId);

    		if(lineLists != null && lineLists.size() > 0){
    			ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
    			for(int i = 0; i < lineLists.size(); i++){
    				ImReplenishLimition  line = (ImReplenishLimition)lineLists.get(i);
    				if(StringUtils.hasText(line.getItemCode()) && StringUtils.hasText(line.getItemBrand()))
    					throw new Exception ("品號與商品品牌，不能同時存在");
    				line.setHead(head);
    				line.setIndexNo(i+1L);
    				imReplenishHeadDAO.save(line);
    			}
    		}
    	}catch (Exception ex) {
    		log.error("自動補貨限制明細匯入時發生錯誤，原因：" + ex.toString());
    		throw new Exception("自動補貨限制明細匯入時發生錯誤，原因：" + ex.getMessage());
    	}        
    }
    
    /**
     * 尋找匹配是否要新增 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findMatch(Properties httpRequest) throws Exception{
	log.info( "===========<findMatch>===========");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String condition = null;
	Object[] values = null; 
	try{
	    // 取得複製時所需的必要資訊
	    String brandCode = httpRequest.getProperty("brandCode");
	    String warehouseCode = httpRequest.getProperty("warehouseCode");
	    String warehouseArea = httpRequest.getProperty("warehouseArea");

	    log.info( "brandCode = " + brandCode );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "warehouseArea = " + warehouseArea );
	    
	    if(StringUtils.hasText(warehouseArea)){
		condition = "and brandCode = ? and warehouseCode = ? and warehouseArea = ? ";
		values = new Object[]{brandCode, warehouseCode, warehouseArea};
	    }else{
		condition = "and brandCode = ? and warehouseCode = ?";
		values = new Object[]{brandCode, warehouseCode};
	    }
	    
	    ImReplenishHead head = (ImReplenishHead)baseDAO.findFirstByProperty("ImReplenishHead", condition, values);

	    if( head == null ){
		log.info( " 不存在定義檔 " );
		properties.setProperty("isNew", "Y");
	    }else{
		properties.setProperty("isNew", "N");
	    }

	    result.add(properties);
	    log.info( "===========<findMatch/>===========");
	    return result;
	}catch(Exception ex){
	    log.error("尋找匹配時發生錯誤，原因：" + ex.toString());
	    throw new Exception("尋找匹配時發生錯誤，原因：" + ex.getMessage());
	}

    }
    
    /**
     * 依formId取得實際主檔 in initial  
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    protected ImReplenishHead getActualHead(Object otherBean, Map resultMap) throws Exception{
	ImReplenishHead head = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    
	    head = (null == formId) ? executeNew(otherBean) : executeFind(formId);
	    resultMap.put("form", head);
	} catch (Exception e) {
	    log.error("取得實際主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 依formId取得實際主檔 in initial  
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    protected ImReplenishBasicParameter getActualBasicParameter(Object otherBean, Map resultMap) throws Exception{
	ImReplenishBasicParameter head = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Double formId =  StringUtils.hasText(formIdString)? NumberUtils.getDouble(formIdString):null;
	    
	    head = (null == formId) ? executeNewBasicParameter(otherBean) : executeFindBasicParameter(otherBean);
	    resultMap.put("form", head);
	} catch (Exception e) {
	    log.error("取得實際主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際主檔失敗,原因:"+e.toString());
	}
	return head;
    }
    
    /**
     * 透過headId取得自動補貨
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImReplenishHead getActualHead(Object formLinkBean ) throws FormException, Exception{

	ImReplenishHead head = null;
	String id = (String)PropertyUtils.getProperty(formLinkBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    head = (ImReplenishHead)baseDAO.findByPrimaryKey(ImReplenishHead.class, headId);
	    if(null == head){
		throw new NoSuchObjectException("查無"+Imformation.AUTO_REPLENISH.getDescription()+"主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的"+Imformation.AUTO_REPLENISH.getDescription()+"主鍵為空值！");
	}
	return head;

    }
    
    /**
     * 透過複合件取得自動補貨
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public ImReplenishBasicParameter getActualBasicParameter(Object formBindBean,Map parameterMap, Boolean isThrow ) throws FormException, Exception{

	ImReplenishBasicParameter head = null;
	String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
	String type = (String)PropertyUtils.getProperty(formBindBean, "type");
	Double parameter = NumberUtils.getDouble((String)PropertyUtils.getProperty(formBindBean, "parameter"));
	Long value = NumberUtils.getLong((String)PropertyUtils.getProperty(formBindBean, "value"));
	
	if(StringUtils.hasText(type) && null != parameter && null != value){
	    ImReplenishBasicParameterId id = new ImReplenishBasicParameterId(brandCode,type,parameter);
	    head = (ImReplenishBasicParameter)baseDAO.findByPrimaryKey(ImReplenishBasicParameter.class, id);
	    log.info("head = " + head);
	    if(null == head){
		head = new ImReplenishBasicParameter();
		if(isThrow){
		    throw new NoSuchObjectException("查無"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主鍵：" + id + "的資料！");
		}else{
		    parameterMap.put("isSave", true);
		}
	    }else{
		parameterMap.put("isSave", false);
	    }
	}else{
	    throw new ValidationErrorException("傳入的"+Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主鍵為空值！");
	}
	// 前端輸入的蓋過來
	head.setId(new ImReplenishBasicParameterId(brandCode,type,parameter));
	head.setValue(value);
	return head;

    }
    
    /**
     * ajax 第一次載入明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	List<Properties> gridDatas = new ArrayList();
	HashMap findObjs = new HashMap();
	String tableName = null;
	String[] gridFieldName = null;
	String[] gridFieldDefaultValues = null;
	Map searchMap = null;
	HashMap map = new HashMap();
	try {

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");
	    Long div = NumberUtils.getLong(httpRequest.getProperty("div"));
	    
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    findObjs.put("and head.headId = :headId", headId);
	    
	    if(2 == div){ // 日曆
		tableName = "ImReplenishCalendar";
		gridFieldName = GRID_CALENDAR_FIELD_NAMES;
		gridFieldDefaultValues = GRID_CALENDAR_FIELD_DEFAULT_VALUES;
		
		searchMap = baseDAO.search(tableName, findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
		List<ImReplenishCalendar> lines = (List<ImReplenishCalendar>)searchMap.get(BaseDAO.TABLE_LIST); 
		
		map.put("headId", headId);
		if (lines != null && lines.size() > 0) {

		    // 設定明細其他欄位
//		    setLineOtherColumns(lines, brandCode);

		    // 取得第一筆的INDEX
		    Long firstIndex = lines.get(0).getIndexNo(); 
		    // 取得最後一筆 INDEX 
		    Long maxIndex = (Long)baseDAO.search(tableName, "count(head.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX  
		    result.add(AjaxUtils.getAJAXPageData(httpRequest,gridFieldName, gridFieldDefaultValues,lines, gridDatas, firstIndex, maxIndex));
		} else {
		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,gridFieldName, gridFieldDefaultValues, map,gridDatas));
		}
	    }else if(3 == div){ // 陳列
		tableName = "ImReplenishDisplay";
		gridFieldName = GRID_DISPLAY_FIELD_NAMES;
		gridFieldDefaultValues = GRID_DISPLAY_FIELD_DEFAULT_VALUES;
		
		searchMap = baseDAO.search(tableName, findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
		List<ImReplenishDisplay> lines = (List<ImReplenishDisplay>)searchMap.get(BaseDAO.TABLE_LIST); 
		
		map.put("headId", headId);
		if (lines != null && lines.size() > 0) {

		    // 設定明細其他欄位
		    setDisplayLineOtherColumns(lines, brandCode);

		    // 取得第一筆的INDEX
		    Long firstIndex = lines.get(0).getIndexNo(); 
		    // 取得最後一筆 INDEX 
		    Long maxIndex = (Long)baseDAO.search(tableName, "count(head.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX  
		    result.add(AjaxUtils.getAJAXPageData(httpRequest,gridFieldName, gridFieldDefaultValues,lines, gridDatas, firstIndex, maxIndex));
		} else {
		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,gridFieldName, gridFieldDefaultValues, map,gridDatas));
		}
		
	    }else if(4 == div){ // 限制
		tableName = "ImReplenishLimition";
		gridFieldName = GRID_LIMITION_FIELD_NAMES;
		gridFieldDefaultValues = GRID_LIMITION_FIELD_DEFAULT_VALUES;
		
		searchMap = baseDAO.search(tableName, findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
		List<ImReplenishLimition> lines = (List<ImReplenishLimition>)searchMap.get(BaseDAO.TABLE_LIST); 
		
		map.put("headId", headId);
		if (lines != null && lines.size() > 0) {

		    // 設定明細其他欄位
		    setLimitionLineOtherColumns(lines, brandCode);

		    // 取得第一筆的INDEX
		    Long firstIndex = lines.get(0).getIndexNo(); 
		    // 取得最後一筆 INDEX 
		    Long maxIndex = (Long)baseDAO.search(tableName, "count(head.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX  
		    result.add(AjaxUtils.getAJAXPageData(httpRequest,gridFieldName, gridFieldDefaultValues,lines, gridDatas, firstIndex, maxIndex));
		} else {
		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,gridFieldName, gridFieldDefaultValues, map,gridDatas));
		}
		
	    }else{
		throw new Exception("查無此明細:"+div);
	    }

	    return result;
	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getServiceAJAXPageData() + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getServiceAJAXPageData() + ex.toString());
	}   
    }
    
    /**
     * ajax picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
		pickerResult.put("replenishResult", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	}catch(Exception ex){
	    log.error("檢視失敗，原因：" + ex.toString());
	    Map messageMap = new HashMap();
	    messageMap.put("type"   , "ALERT");
	    messageMap.put("message", "檢視失敗，原因："+ex.toString());
	    messageMap.put("event1" , null);
	    messageMap.put("event2" , null);
	    resultMap.put("vatMessage",messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }
    
    /**
     * ajax picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getBasicParameterSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
		pickerResult.put("replenishResult", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	}catch(Exception ex){
	    log.error("檢視失敗，原因：" + ex.toString());
	    Map messageMap = new HashMap();
	    messageMap.put("type"   , "ALERT");
	    messageMap.put("message", "檢視失敗，原因："+ex.toString());
	    messageMap.put("event1" , null);
	    messageMap.put("event2" , null);
	    resultMap.put("vatMessage",messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }
    
    /**
     * ajax 取得search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXBasicParameterSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String type = httpRequest.getProperty("type");// 參數類別
	    String parameter = httpRequest.getProperty("parameter");// 參數
	    String value = httpRequest.getProperty("value");// 參數值
	    
	    log.info("parameter = " + parameter);
	    log.info("value = " + value);
	    
	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and id.brandCode = :brandCode",brandCode);
	    findObjs.put(" and id.type = :type",type);
	    findObjs.put(" and id.parameter = :parameter", !"".equalsIgnoreCase(parameter) ? NumberUtils.getDouble(parameter) : null);
	    findObjs.put(" and value = :value", !"".equalsIgnoreCase(value) ? NumberUtils.getLong(value) : null);

	    //==============================================================	    

	    Map imReplenishBasicParameterMap = baseDAO.search( "ImReplenishBasicParameter", findObjs, "order by id.type, id.parameter, value", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImReplenishBasicParameter> imReplenishBasicParameters = (List<ImReplenishBasicParameter>) imReplenishBasicParameterMap.get(BaseDAO.TABLE_LIST); 

	    if (imReplenishBasicParameters != null && imReplenishBasicParameters.size() > 0) {

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)baseDAO.search("ImReplenishBasicParameter", "count(id.type) as rowCount" ,findObjs, "", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_BASIC_PARAMETER_SEARCH_FIELD_NAMES, GRID_BASIC_PARAMETER_SEARCH_FIELD_DEFAULT_VALUES,imReplenishBasicParameters, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_BASIC_PARAMETER_SEARCH_FIELD_NAMES, GRID_BASIC_PARAMETER_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceAJAXSearchPageData() + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getServiceAJAXSearchPageData() + ex.getMessage());
	}	
    }
    
    /**
     * ajax 取得search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String warehouseCode = httpRequest.getProperty("warehouseCode");// 庫別
	    String warehouseArea = httpRequest.getProperty("warehouseArea" );

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and brandCode = :brandCode",brandCode);
	    findObjs.put(" and warehouseCode = :warehouseCode",warehouseCode);
	    findObjs.put(" and warehouseArea = :warehouseArea",warehouseArea);

	    //==============================================================	    

	    Map imReplenishHeadMap = baseDAO.search( "ImReplenishHead", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImReplenishHead> imReplenishHeads = (List<ImReplenishHead>) imReplenishHeadMap.get(BaseDAO.TABLE_LIST); 

	    if (imReplenishHeads != null && imReplenishHeads.size() > 0) {

		setOtherColumns(imReplenishHeads);

		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)baseDAO.search("ImReplenishHead", "count(headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,imReplenishHeads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error(Imformation.AUTO_REPLENISH.getServiceAJAXSearchPageData() + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getServiceAJAXSearchPageData() + ex.getMessage());
	}	
    }
    
    /**
     * 取得是否存在補貨細配置檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXIsExistence(Properties httpRequest)throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String type = null;
	Double parameter = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    type = httpRequest.getProperty("type");
	    parameter = NumberUtils.getDouble((String)httpRequest.getProperty("parameter"));

	    ImReplenishBasicParameter head = (ImReplenishBasicParameter)imReplenishHeadDAO.findOneParameter(brandCode, type, parameter);
	    if(null != head){
		properties.setProperty("value", Long.toString(head.getValue()));
		properties.setProperty("message", "目前為更新資料");
	    }else{
		properties.setProperty("value", "");
		properties.setProperty("message", "目前為新增資料");
	    }
	    result.add(properties);	

	    return result;	        
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、參數：" + type + "、參數值：" + parameter+ " 查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢品號資料失敗！");
	}        
    }
    
    /**
     * 設定其他欄位
     * @param 
     */
    private void setOtherColumns(List<ImReplenishHead> heads){
	for (ImReplenishHead imReplenishHead : heads) {
	    
	    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(imReplenishHead.getBrandCode(), imReplenishHead.getWarehouseCode(), null);
	    if( null != imWarehouse ){
		imReplenishHead.setWarehouseName(imWarehouse.getWarehouseName());
	    }else{
		imReplenishHead.setWarehouseName(MessageStatus.DATA_NOT_FOUND);
	    }
	    imReplenishHead.setCreatedNameBy(UserUtils.getLoginNameByEmployeeCode(imReplenishHead.getCreatedBy()));
	    imReplenishHead.setLastUpdatedNameBy(UserUtils.getLoginNameByEmployeeCode(imReplenishHead.getLastUpdatedBy()));
	}
    }
    
    /**
     * 設定其他欄位明細
     * @param 
     */
    private void setDisplayLineOtherColumns(List<ImReplenishDisplay> lines, String brandCode){
	for (ImReplenishDisplay imReplenishDisplay : lines) {
	    ImItem imItem = imItemDAO.findItem(brandCode, imReplenishDisplay.getItemCode());
	    if(null != imItem){
		imReplenishDisplay.setItemCName(imItem.getItemCName());
	    }else{
		imReplenishDisplay.setItemCName(MessageStatus.DATA_NOT_FOUND);
	    }
	}
    }
    
    /**
     * 設定其他欄位明細
     * @param 
     */
    private void setLimitionLineOtherColumns(List<ImReplenishLimition> lines, String brandCode){
    	for (ImReplenishLimition imReplenishLimition : lines) {

    		if(StringUtils.hasText(imReplenishLimition.getItemCode())){
    			ImItem imItem = imItemDAO.findItem(brandCode, imReplenishLimition.getItemCode());
    			if(null != imItem){
    				imReplenishLimition.setItemCName(imItem.getItemCName());
    			}else{
    				imReplenishLimition.setItemCName(MessageStatus.DATA_NOT_FOUND);
    			}
    		}
    		if(StringUtils.hasText(imReplenishLimition.getItemBrand())){
    			ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(brandCode,ImItemCategoryDAO.ITEM_BRAND,imReplenishLimition.getItemBrand(),"Y");
    			if(null != imItemCategory){
    				imReplenishLimition.setItemBrandName(imItemCategory.getCategoryName());
    			}else{
    				imReplenishLimition.setItemBrandName(MessageStatus.DATA_NOT_FOUND);
    			}
    		}
    	}
    }
    
    /**
     * 將檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    /**
     * 將檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveBasicParameterSearchResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_BASIC_PARAMETER_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    /**
     * ajax  更新自動補貨明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{ 
	String errorMsg = null;
	String tableName = null;
	String[] gridFieldNames = null;
	int[] gridFieldTypes = null;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    Long div = NumberUtils.getLong(httpRequest.getProperty("div"));
	    
	    if (headId == null) {
		throw new ValidationErrorException("傳入的主鍵為空值！");
	    }

	    if(2 == div){
		tableName = "ImReplenishCalendar";
		gridFieldNames = GRID_CALENDAR_FIELD_NAMES;
		gridFieldTypes = GRID_CALENDAR_FIELD_TYPES;
	    }else if(3 == div){
		tableName = "ImReplenishDisplay";
		gridFieldNames = GRID_DISPLAY_FIELD_NAMES;
		gridFieldTypes = GRID_DISPLAY_FIELD_TYPES;
	    }else if(4 == div){
		tableName = "ImReplenishLimition";
		gridFieldNames = GRID_LIMITION_FIELD_NAMES;
		gridFieldTypes = GRID_LIMITION_FIELD_TYPES;
	    }
	    
	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, gridFieldNames);
	    // Get INDEX NO
	    Map findObjs = new HashMap();
	    findObjs.put("and head.headId = :headId", headId);
	    int indexNo = ((Long) baseDAO.search(tableName, "count(head.headId) as rowCount", findObjs, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT)).intValue(); // 取得最後一筆 INDEX
	    log.info( "MaxIndexNo = " + indexNo );
	    
	    if (upRecords != null) {
		for (Properties upRecord : upRecords) {
		    // 先載入HEAD_ID OR LINE DATA
		    Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
		    
		    if(2 == div){ // 日曆
			String replenishDate = upRecord.getProperty(gridFieldNames[1]);
			if (StringUtils.hasText(replenishDate)) {
			    
			    ImReplenishCalendar line = (ImReplenishCalendar)baseDAO.findFirstByProperty(tableName, "and head.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "line = " + line + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( line != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames, gridFieldTypes);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				baseDAO.update(line);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				line = new ImReplenishCalendar(); 

				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames,gridFieldTypes);
				line.setHead(new ImReplenishHead(headId));
				line.setCreatedBy(loginEmployeeCode);
				line.setCreationDate(date);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				line.setIndexNo(Long.valueOf(indexNo));
				baseDAO.save(line);
			    }
			} // 含配貨日期則處理
		    }else if(3 == div){ // 展示
			String itemCode = upRecord.getProperty(gridFieldNames[1]);
			if (StringUtils.hasText(itemCode)) {
			    
			    ImReplenishDisplay line = (ImReplenishDisplay)baseDAO.findFirstByProperty(tableName, "and head.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "line = " + line + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( line != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames, gridFieldTypes);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				baseDAO.update(line);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				line = new ImReplenishDisplay(); 

				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames,gridFieldTypes);
				line.setHead(new ImReplenishHead(headId));
				line.setCreatedBy(loginEmployeeCode);
				line.setCreationDate(date);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				line.setIndexNo(Long.valueOf(indexNo));
				baseDAO.save(line);
			    }
			} // 含品號則處理
		    }else if(4 == div){ // 限制
		    String itemCode = upRecord.getProperty(gridFieldNames[1]);
			String itemBrand = upRecord.getProperty(gridFieldNames[3]);
			if (StringUtils.hasText(itemCode) || StringUtils.hasText(itemBrand)) {
			    
			    ImReplenishLimition line = (ImReplenishLimition)baseDAO.findFirstByProperty(tableName, "and head.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
			    log.info( "line = " + line + "\nlineId = " + lineId);
			    Date date = new Date();
			    if ( line != null ) {
				log.info( "更新 = " + headId + " | "+ lineId  );
				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames, gridFieldTypes);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				baseDAO.update(line);

			    } else {
				indexNo++;
				log.info( "新增 = " + headId + " | "+  indexNo);
				line = new ImReplenishLimition(); 

				AjaxUtils.setPojoProperties(line,upRecord, gridFieldNames,gridFieldTypes);
				line.setHead(new ImReplenishHead(headId));
				line.setCreatedBy(loginEmployeeCode);
				line.setCreationDate(date);
				line.setLastUpdatedBy(loginEmployeeCode);
				line.setLastUpdateDate(date);
				line.setIndexNo(Long.valueOf(indexNo));
				baseDAO.save(line);
			    }
			} // 含品號則處理
		    }else{
			throw new Exception("查無此明細:" +div);
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);

	} catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getServiceUpdateAJAXPageLinesData() + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getServiceUpdateAJAXPageLinesData() + ex.getMessage());
	}   
    }
    
    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateBean(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap();
	try{
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    //取得欲更新的bean
	    ImReplenishHead head = getActualHead(formLinkBean); 
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
	    resultMap.put("entityBean", head);
	    return resultMap;      
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getDescription()+"資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public Map updateBasicParameterBean(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap();
	try{
//	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    //取得欲更新的bean
	    ImReplenishBasicParameter head = getActualBasicParameter(formBindBean,parameterMap, false); 
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
	    resultMap.put("entityBean", head);
	    
	    return resultMap;      
	} catch (FormException fe) {
	    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 檢核,存取主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXAutoReplenish(Map parameterMap)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    log.info("取得 head ");
	    ImReplenishHead head = getActualHead(formLinkBean);

	    // 檢核
	    log.info("deleteLine ");
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
		deleteLine(head); 
	    }

	    log.info("更新主檔明細檔  ");
	    // 更新主檔明細檔  
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
		// 更新主檔明細檔
		updateAutoReplenish(head, employeeCode );
	    }
	    resultMsg = head.getWarehouseCode() + "-" + head.getWarehouseArea() + "存檔成功！ 是否繼續新增？";

	    log.info("結束");
	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error(Imformation.AUTO_REPLENISH.getDescription()+"檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error(Imformation.AUTO_REPLENISH.getDescription()+"存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getDescription()+"存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }
    
    /**
     * 檢核,存取主檔,明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map updateAJAXBasicParameter(Map parameterMap)throws Exception{
	Map resultMap = new HashMap();
	List errorMsgs = null;
	String resultMsg = null;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    log.info("取得 head ");
	    ImReplenishBasicParameter head = getActualBasicParameter(formBindBean,parameterMap, false); 
	    Boolean isSave = (Boolean)parameterMap.get("isSave");
	    
	    log.info("head = " + head);
	    log.info("isSave = " + isSave);
	    log.info("更新主檔明細檔  ");
	    // 更新主檔明細檔  
	    if( OrderStatus.FORM_SUBMIT.equals(formAction) ){
		// 更新主檔明細檔
		resultMsg = updateBasicParameter(head, isSave, employeeCode );
	    }

	    log.info("結束");
	    resultMap.put("entityBean", head);
	    resultMap.put("resultMsg", resultMsg);
	} catch( ValidationErrorException ve ){
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"檢核時發生錯誤，原因：" + ve.toString());
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	} catch (Exception e) {
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"存檔時發生錯誤，原因：" + e.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"存檔時發生錯誤，原因：" + e.getMessage());
	}

	return resultMap;
    }
    
    /**
     * 更新主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateAutoReplenish(ImReplenishHead head, String employeeCode )throws Exception{
	try{
	    Date date = new Date();
	    if (head.getHeadId() != null) {
		head.setLastUpdateDate(date);
		head.setLastUpdatedBy(employeeCode);
		
		List<ImReplenishDisplay> imReplenishDisplays = (List<ImReplenishDisplay>)baseDAO.findByProperty("ImReplenishDisplay", "and head.headId = ?", new Object[]{head.getHeadId()});
		for (ImReplenishDisplay imReplenishDisplay : imReplenishDisplays) {
		    imReplenishDisplay.setLastUpdateDate(date);
		    imReplenishDisplay.setLastUpdatedBy(employeeCode);
		}
		
		List<ImReplenishLimition> imReplenishLimitions = (List<ImReplenishLimition>)baseDAO.findByProperty("ImReplenishLimition", "and head.headId = ?", new Object[]{head.getHeadId()});
		for (ImReplenishLimition imReplenishLimition : imReplenishLimitions) {
		    imReplenishLimition.setLastUpdateDate(date);
		    imReplenishLimition.setLastUpdatedBy(employeeCode);
		}
		
		List<ImReplenishCalendar> imReplenishCalendars = (List<ImReplenishCalendar>)baseDAO.findByProperty("ImReplenishCalendar", "and head.headId = ?", new Object[]{head.getHeadId()});
		for (ImReplenishCalendar imReplenishCalendar : imReplenishCalendars) {
		    imReplenishCalendar.setLastUpdateDate(date);
		    imReplenishCalendar.setLastUpdatedBy(employeeCode);
		}
		imReplenishHeadDAO.update(head);
	    }
	}catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 更新主檔明細檔
     * @param head
     * @param employeeCode
     */
    private String updateBasicParameter(ImReplenishBasicParameter head, Boolean isSave, String employeeCode )throws Exception{
	try{
	    if (!isSave) {
		imReplenishHeadDAO.update(head);
		return  head.getId().getType() + "-" + head.getId().getParameter() + "更新成功！";
	    }else{
		imReplenishHeadDAO.save(head);
		return  head.getId().getType() + "-" + head.getId().getParameter() + "新增成功！";
	    }
	    
	}catch (Exception ex) {
	    log.error(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(Imformation.AUTO_REPLENISH_BASIC_PARAMETER.getDescription()+"主檔明細檔存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 驗證主檔明細檔
     * @param parameterMap
     * @throws Exception
     */
    public String validate(Map parameterMap) throws Exception {

	StringBuffer sb = new StringBuffer();
	Object formBindBean = parameterMap.get("vatBeanFormBind");
//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");

//	Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

	String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");
	String warehouseCode = (String) PropertyUtils.getProperty(formBindBean, "warehouseCode");
	String warehouseArea = (String) PropertyUtils.getProperty(formBindBean, "warehouseArea");
	
	// 驗證庫別
	if(!StringUtils.hasText(warehouseCode)){
	    sb.append("請輸入庫別代碼\n");
	}else{
	    ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, warehouseCode, null);
	    if(null == imWarehouse){
		sb.append("依品牌:"+brandCode+" 庫別:"+warehouseCode+"查無資料\n");
	    }
	}
	
	// 驗證庫別區域
	//if(!StringUtils.hasText(warehouseArea)){
	  //  sb.append("請輸入倉庫區域\n");
	//}else{
	//}
	
	return sb.toString();
    }
    
    /**
     * 驗證主檔明細檔
     * @param parameterMap
     * @throws Exception
     */
    public String validateBasicParameter(Map parameterMap) throws Exception {

	StringBuffer sb = new StringBuffer();
	Object formBindBean = parameterMap.get("vatBeanFormBind");
//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
//	Object otherBean = parameterMap.get("vatBeanOther");

//	String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//	Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

//	String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");
	log.info("1=1");
	String type = (String) PropertyUtils.getProperty(formBindBean, "type");
	Double parameter = NumberUtils.getDouble( (String)PropertyUtils.getProperty(formBindBean, "parameter") );
	Double value = NumberUtils.getDouble((String) PropertyUtils.getProperty(formBindBean, "value") );
	
	// 驗證庫別
	if(!StringUtils.hasText(type)){
	    sb.append("請輸入參數類別\n");
	}
	
	// 驗證參數
	if( null == parameter){
	    sb.append("請輸入參數\n");
	}
	// 驗證參數值
	if( null == value){
	    sb.append("請輸入參數值\n");
	}
	
	return sb.toString();
    }
}
