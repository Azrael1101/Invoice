package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exceptions.StorageException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImPickHead;
import tw.com.tm.erp.hbm.bean.ImPickItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImStorageItem;
import tw.com.tm.erp.hbm.bean.ImStorageOnHand;
import tw.com.tm.erp.hbm.bean.ImStorageOnHandView;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImStorageOnHandDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.sp.AppExtendItemStorageInfoService;

/**
 * 報單單頭 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImStorageService {

    private static final Log log = LogFactory.getLog(ImStorageService.class);

    public static final String PROGRAM_ID = "IM_STORAGE";

    public static final String PROGRAM_ID_BATCH = "IM_STORAGE_BATCH";

    public static final String IN = "IN";

    public static final String MOVE = "MOVE";

    public static final String OUT = "OUT";

    public static final String ADJ = "ADJ";
    
    public static final String BLOCK = "BLOCK";

    public static final String STORAGE_LOT_NO_DEFAULT = "00000000";

    public static final String STORAGE_IN_NO_DEFAULT = "00000000";

    public static final String STORAGE_CODE_DEFAULT = "00000000";

    private BaseDAO baseDAO;

    public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
	this.siProgramLogDAO = siProgramLogDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    private BuBrandDAO buBrandDAO;

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    private BuOrderTypeService buOrderTypeService;

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }

    private ImItemService imItemService;

    public void setImItemService(ImItemService imItemService) {
	this.imItemService = imItemService;
    }

    private ImWarehouseService imWarehouseService;

    public ImWarehouseService getImWarehouseService() {
	return imWarehouseService;
    }

    public void setImWarehouseService(ImWarehouseService imWarehouseService) {
	this.imWarehouseService = imWarehouseService;
    }

    private SiProgramLogAction siProgramLogAction;

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    private SiProgramLogDAO siProgramLogDAO;

    public SiProgramLogDAO getSiProgramLogDAO() {
	return siProgramLogDAO;
    }

    private ImStorageOnHandDAO imStorageOnHandDAO;

    public void setImStorageOnHandDAO(ImStorageOnHandDAO imStorageOnHandDAO) {
	this.imStorageOnHandDAO = imStorageOnHandDAO;
    }

    private NativeQueryDAO nativeQueryDAO;

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
	this.nativeQueryDAO = nativeQueryDAO;
    }

    private AppExtendItemStorageInfoService appExtendItemStorageInfoService;

    public void setAppExtendItemStorageInfoService(
	    AppExtendItemStorageInfoService appExtendItemStorageInfoService) {
	this.appExtendItemStorageInfoService = appExtendItemStorageInfoService;
    }

    private ImItemCategoryDAO imItemCategoryDAO;

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	this.imItemCategoryDAO = imItemCategoryDAO;
    }

    private ImItemCategoryService imItemCategoryService;

    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
	this.imItemCategoryService = imItemCategoryService;
    }

    /**
     * 明細欄位
     */
    public static final String[] GRID_FIELD_NAMES = { 
	"indexNo", "itemCode", "itemCName", "storageInNo" , 
	"storageLotNo", "deliveryWarehouseCode", "deliveryStorageCode", "remark",
	"storageQuantity", "arrivalWarehouseCode", "arrivalStorageCode",
	"storageLineId", "isLockRecord", "isDeleteRecord", "message"
    };

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	"", "", "", "",
	"", "", "", "",
	"", "", "",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };         

    public static final int[] GRID_FIELD_TYPES = { 
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 查詢儲位單欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
	"orderTypeCode", "orderNo", "sourceOrderTypeCode", 
	"sourceOrderNo", "storageTransactionDate", "status", 
	"storageHeadId"
    };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "",
	""
    };

    /**
     * 查詢儲位庫存欄位
     */
    public static final String[] GRID_ON_HAND_FIELD_NAMES = { 
	"id.brandCode", "id.itemCode", "itemCName",
	"id.warehouseCode", "id.storageLotNo",
	"id.storageInNo", "id.storageCode",
	"currentOnHandQty", "blockQty"
    };

    public static final String[] GRID_ON_HAND_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "",
	"", "", 
	"", ""
    };

    /**
     * 單頭欄位
     */
    public static final String[] HEAD_FIELD_NAMES = { 
	"brandCode", "storageTransactionDate", "deliveryWarehouseCode", "arrivalWarehouseCode", "sourceOrderTypeCode", 
	"sourceOrderNo", "createdBy"
    };

    /**
     * 單身欄位
     */
    public static final String[] ITEM_FIELD_NAMES = { 
	"itemCode", "quantity"
    };


    /**
     * executeInitial
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception {
	HashMap returnMap = new HashMap();
	ImStorageHead head = null;
	Map multiList = new HashMap(0);
	try {
	    Object otherBean = parameterMap.get(AjaxUtils.VAT_BEAN_OTHER);
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.FORM_ID);
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String sourceOrderTypeCode = (String)PropertyUtils.getProperty(otherBean, "sourceOrderTypeCode");
	    String sourceOrderNo = (String)PropertyUtils.getProperty(otherBean, "sourceOrderNo");

	    Map confMap = new HashMap();
	    confMap.put("brandCode", loginBrandCode);
	    confMap.put("employeeCode", loginEmployeeCode);
	    confMap.put("orderTypeCode", orderTypeCode);

	    BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode, orderTypeCode));

	    if(null != buOrderType)
		confMap.put("storageTransactionType", buOrderType.getOrderCondition());

	    //initial Head
	    if(StringUtils.hasText(sourceOrderTypeCode) && StringUtils.hasText(sourceOrderNo)){
		head = (ImStorageHead)baseDAO.findFirstByProperty("ImStorageHead", "",
			" and brandCode = ? and sourceOrderTypeCode = ? and sourceOrderNo = ? ",
			new String[] {loginBrandCode, sourceOrderTypeCode, sourceOrderNo} , "");
	    }else if(null != formId){
		formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		head = getBeanByHeadId(formId);
	    }else{
		confMap.put("status", OrderStatus.SAVE);
		head = createImStorageHead(confMap);
	    }

	    if(null == head)
		throw new Exception("查無儲位單資料");

	    returnMap.put(AjaxUtils.AJAX_FORM, head);
	    returnMap.put("storageHeadId", head.getStorageHeadId());

	    //initial Other
	    String status = (String)PropertyUtils.getProperty(returnMap.get(AjaxUtils.AJAX_FORM), "status");

	    returnMap.put("identification", MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo()) );
	    returnMap.put("programId",PROGRAM_ID);
	    returnMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    returnMap.put("statusName", OrderStatus.getChineseWord(status));
	    returnMap.put("createByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));

	    returnMap.put("deliveryWarehouse", "#F.deliveryWarehouseCode");
	    returnMap.put("arrivalWarehouse", "#F.arrivalWarehouseCode");
	    returnMap.put("storageTransactionType", head.getStorageTransactionType());
	    returnMap.put("storageStatus", "#F.status");

	    //initial Select
	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"ISO");
	    multiList.put("allOrderTypeCodes" , AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, false ));

	    List<ImWarehouse> allWarehouses = imWarehouseService.getWarehouseByWarehouseEmployee(head.getBrandCode(),head.getCreatedBy(), null);
	    multiList.put("allWarehouses" , AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true ));

	    returnMap.put(AjaxUtils.AJAX_MULTILIST, multiList);

	    return returnMap;
	} catch (Exception ex) {
	    log.error("儲位單初始化失敗，原因：" + ex.toString());
	    throw new Exception("儲位單初始化失敗，原因：" + ex.getMessage());
	}
    }

    /**
     * 存檔,取得暫存碼
     * @param ImStorageHead
     * @throws Exception
     */ 
    public void updateHead(ImStorageHead head, String loginEmployeeCode) throws StorageException{
	try{
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());
	    baseDAO.update(head);	   
	}catch(Exception ex){
	    log.error("更新儲位單發生錯誤，原因：" + ex.toString());
	    throw new StorageException("更新儲位單發生錯誤，原因：" + ex.getMessage());
	}	
    }

    public void update(ImStorageHead head) throws StorageException{
	try{
	    baseDAO.update(head);	   
	}catch(Exception ex){
	    log.error("更新儲位單發生錯誤，原因：" + ex.toString());
	    throw new StorageException("更新儲位單發生錯誤，原因：" + ex.getMessage());
	}	
    }

    public ImStorageHead findById(Long headId) throws StorageException {
	try {
	    return (ImStorageHead)baseDAO.findById("ImStorageHead", headId);
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
	    throw new StorageException("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 為了匯出用
     * @param headId
     * @return
     * @throws Exception
     */
    public ImStorageHead getBeanByHeadId(Long headId) throws StorageException {
	try {
	    return (ImStorageHead)baseDAO.findByPrimaryKey(ImStorageHead.class,headId);
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
	    throw new StorageException("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * by headId 取得bean
     */
    public ImStorageHead getBeanByHeadId(Object vatBeanFormLink) throws Exception {
	Long headId = null;
	try {
	    String headIdString = (String)PropertyUtils.getProperty(vatBeanFormLink, "storageHeadId");
	    headId =  StringUtils.hasText(headIdString)? Long.valueOf(headIdString):null;
	    ImStorageHead head = (ImStorageHead)baseDAO.findByPrimaryKey(ImStorageHead.class,headId);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 第一次載入明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	try {
	    log.info("getAJAXPageData");
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long storageHeadId = NumberUtils.getLong(httpRequest.getProperty("storageHeadId"));
	    log.info("storageHeadId = " + storageHeadId);
	    ImStorageHead imStorageHead = getBeanByHeadId(storageHeadId);
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.imStorageHead.storageHeadId = :storageHeadId", storageHeadId);
	    Map searchMap = baseDAO.search("ImStorageItem as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE); 
	    List<ImStorageItem> imStorageItems = (List<ImStorageItem>)searchMap.get(BaseDAO.TABLE_LIST); 

	    HashMap parameterMap = new HashMap();
	    parameterMap.put("storageHeadId", storageHeadId);

	    if(null != imStorageHead){
		parameterMap.put("brandCode", imStorageHead.getBrandCode());
		parameterMap.put("deliveryWarehouseCode", imStorageHead.getDeliveryWarehouseCode());
		parameterMap.put("arrivalWarehouseCode", imStorageHead.getArrivalWarehouseCode());
	    }

	    if (imStorageItems != null && imStorageItems.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = imStorageItems.get(0).getIndexNo(); 
		// 取得最後一筆 INDEX
		Long maxIndex = (Long)baseDAO.search("ImStorageItem as model", "count(model.imStorageHead.storageHeadId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		setItemInfo(parameterMap, imStorageItems);
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imStorageItems, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, parameterMap, gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("載入頁面顯示的明細失敗！");
	}
    }


    /**
     * 更新單身
     */
    public List<Properties> updateAJAXPageData(Properties httpRequest)throws Exception {
	String errorMsg = null;
	int indexNo = 0;
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long storageHeadId = NumberUtils.getLong(httpRequest.getProperty("storageHeadId"));
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    //String detailKey = httpRequest.getProperty("detailKey");
	    log.info( "storageHeadId = " + storageHeadId );
	    if (storageHeadId == null) 
		throw new ValidationErrorException("傳入的儲位單主鍵為空值！");

	    // 將STRING資料轉成List Properties record data
	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
	    // Get INDEX NO 
	    ImStorageItem imStorageItemMax = ((ImStorageItem)baseDAO.findLastByProperty("ImStorageItem","", " and imStorageHead.storageHeadId = ? ", new Object[]{storageHeadId}, "order by indexNo"));
	    if(null != imStorageItemMax){
		indexNo = imStorageItemMax.getIndexNo().intValue();
	    }

	    log.info( "MaxIndexNo = " + indexNo );
	    ImStorageHead head = getBeanByHeadId(storageHeadId);
	    if(null == head)
		throw new ValidationErrorException("查無儲位單主鍵之單頭！");

	    Map findObjs = new HashMap();
	    findObjs.put("and model.imStorageHead.storageHeadId = :storageHeadId", storageHeadId);
	    if( IN.equals(head.getStorageTransactionType()) || OrderStatus.SAVE.equals(head.getStatus()) || 
		    OrderStatus.REJECT.equals(head.getStatus()) || OrderStatus.UNCONFIRMED.equals(head.getStatus()) ){
		// 考慮狀態
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long storageLineId = NumberUtils.getLong(upRecord.getProperty("storageLineId"));
			log.info("storageLineId = " + storageLineId);
			String itemCode = upRecord.getProperty("itemCode");
			if (StringUtils.hasText(itemCode)) {
			    ImStorageItem imStorageItem = (ImStorageItem)baseDAO.findFirstByProperty("ImStorageItem", "and imStorageHead.storageHeadId = ? and storageLineId = ?", new Object[]{ storageHeadId, storageLineId } );
			    Date date = new Date();
			    if ( imStorageItem != null ) {
				log.info( "更新 = " + storageHeadId + " | "+ storageLineId  );
				AjaxUtils.setPojoProperties(imStorageItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				imStorageItem.setLastUpdatedBy(loginEmployeeCode);
				imStorageItem.setLastUpdateDate(date);
				baseDAO.update(imStorageItem);
			    } else {
				indexNo++;
				log.info( "新增 = " + storageHeadId + " | "+  indexNo);
				imStorageItem = new ImStorageItem(); 
				AjaxUtils.setPojoProperties(imStorageItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				imStorageItem.setImStorageHead(new ImStorageHead(storageHeadId));
				imStorageItem.setCreatedBy(loginEmployeeCode);
				imStorageItem.setCreationDate(date);
				imStorageItem.setLastUpdatedBy(loginEmployeeCode);
				imStorageItem.setLastUpdateDate(date);
				imStorageItem.setIndexNo(Long.valueOf(indexNo));
				baseDAO.save(imStorageItem);
			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新儲位單明細資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新儲位單明細資料失敗！");
	}   
    }

    /**
     * 匯入
     */
    public void executeImportLists(Long headId, List lineLists) throws Exception {
	try{
	    ImStorageHead head = getBeanByHeadId(headId);
	    head.setImStorageItems(lineLists);
	    List<ImStorageItem> list = head.getImStorageItems();
	    if(list != null && list.size() > 0){
		for(int i = 0; i < list.size(); i++){
		    ImStorageItem  item = (ImStorageItem)list.get(i);
		    if(MOVE.equals(head.getStorageTransactionType())){
			if(null == item.getDeliveryStorageCode())
			    item.setDeliveryStorageCode(STORAGE_CODE_DEFAULT);
			item.setDeliveryWarehouseCode(head.getDeliveryWarehouseCode());
		    }else{
			item.setDeliveryStorageCode(null);
			item.setDeliveryWarehouseCode(null);
		    }

		    //if(null == item.getArrivalStorageCode())
		    //	item.setArrivalStorageCode(STORAGE_CODE_DEFAULT);
		    item.setArrivalWarehouseCode(head.getArrivalWarehouseCode());

		    //if(null == item.getStorageInNo())
		    //	item.setStorageInNo(STORAGE_IN_NO_DEFAULT);
		    //if(null == item.getStorageLotNo())
		    //	item.setStorageLotNo(STORAGE_LOT_NO_DEFAULT);

		    item.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
		    item.setIndexNo(i+1L);
		}    
		baseDAO.update(head);
	    }     	
	}catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("儲位單明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("儲位單明細匯入時發生錯誤，原因：" + ex.getMessage());
	}  
    }

    /**
     * 匯入 with 進貨日
     */
    public void executeImportLists(Long headId, Date warehouseInDate, List lineLists) throws Exception {
	try{
	    ImStorageHead head = getBeanByHeadId(headId);
	    head.setImStorageItems(lineLists);
	    List<ImStorageItem> list = head.getImStorageItems();
	    if(list != null && list.size() > 0){
		for(int i = 0; i < list.size(); i++){
		    ImStorageItem  item = (ImStorageItem)list.get(i);
		    item.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
		    item.setIndexNo(i+1L);
		}    
		baseDAO.update(head);
	    }     	
	}catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("儲位單明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("儲位單明細匯入時發生錯誤，原因：" + ex.getMessage());
	}  
    }

    /**
     * AJAX Line Change
     * 
     * @param headObj(brandCode,orderTypeCode,itemCode)
     * @throws FormException
     */
    public List<Properties> getAJAXItemCode(Properties httpRequest) throws FormException {
	List re = new ArrayList();
	Properties pro = new Properties();
	String brandCode     = httpRequest.getProperty("brandCode");
	String itemCode      = httpRequest.getProperty("itemCode");
	try {
	    ImItem imItem = imItemService.findItem(brandCode, itemCode);
	    String itemCName = MessageStatus.ITEM_NOT_FOUND;
	    if ( null != imItem ){
		itemCName = AjaxUtils.getPropertiesValue( imItem.getItemCName(),           itemCName ); 
	    }
	    pro.setProperty("ItemCName", itemCName);
	    re.add(pro);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return re;
    }

    private void setItemInfo(HashMap parameterMap, List<ImStorageItem> imStorageItems) throws Exception {
	for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
	    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
	    ImItem item = imItemService.findItem((String)parameterMap.get("brandCode"), imStorageItem.getItemCode());
	    if(null != item)
		imStorageItem.setItemCName(item.getItemCName());
	    else
		imStorageItem.setItemCName(MessageStatus.ITEM_NOT_FOUND);
	}
    }

    /**
     * 更新單頭跟單身的庫別
     * @param httpRequest
     * @return
     */
    public List<Properties> updateChangeWarehouse(Properties httpRequest) throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String deliveryWarehouseCode = httpRequest.getProperty("deliveryWarehouseCode");
	    String arrivalWarehouseCode = httpRequest.getProperty("arrivalWarehouseCode");
	    ImStorageHead head = getBeanByHeadId(headId);
	    head.setDeliveryWarehouseCode(deliveryWarehouseCode);
	    head.setArrivalWarehouseCode(arrivalWarehouseCode);
	    List<ImStorageItem> items = head.getImStorageItems();
	    for (Iterator iterator = items.iterator(); iterator.hasNext();) {
		ImStorageItem item = (ImStorageItem) iterator.next();
		item.setDeliveryWarehouseCode(deliveryWarehouseCode);
		item.setArrivalWarehouseCode(arrivalWarehouseCode);
	    }
	    baseDAO.update(head);
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("更新明細庫別發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新明細庫別失敗！");
	}
    }

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
    public void updateHeadBean(Map parameterMap,Map returnMap)throws FormException, Exception {
	ImStorageHead head = null;
	try{
	    Object formBindBean = parameterMap.get(AjaxUtils.VAT_BEAN_FORM_BIND);
	    head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	    Object formOtherBean = parameterMap.get(AjaxUtils.VAT_BEAN_OTHER);
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(formOtherBean, "loginEmployeeCode");
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, head);
	    updateHead(head, loginEmployeeCode);
	    returnMap.put(AjaxUtils.AJAX_FORM, head);
	} catch (Exception ex) {
	    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	    throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 檢查
     */
    public void check(Map parameterMap, Map returnMap) throws Exception {
	String message = null;
	String identification = null;
	ImStorageHead head = null;
	try{
	    head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	    identification = (String)returnMap.get(AjaxUtils.IDENTIFICATION);
	    deleteProgramLog(identification, 0);
	    checkHead(head, identification);
	    checkItem(head, identification);
	}catch (Exception ex) {
	    message = "檢核失敗，原因：" + ex.getMessage();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    log.error(message);	
	}
    }

    /**
     * 取得寫入programlog的辨識值，通常為品牌+單別+單號
     */
    public String getIdentification(Map parameterMap, Map returnMap) throws Exception {
	log.info("test ImStorgeService getProgramId ok");
	ImStorageHead head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	return MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
    }

    /**
     * 檢查單頭
     */
    public void checkHead(ImStorageHead head,String identification) throws Exception {
	log.info("checkHead");
	if(null == head.getStorageTransactionDate())
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入異動日期", head.getLastUpdatedBy());

	//if(ADJ.equals(head.getStorageTransactionType())){
	if(!StringUtils.hasText(head.getArrivalWarehouseCode()))
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入轉入庫", head.getLastUpdatedBy());
	//}

	if(MOVE.equals(head.getStorageTransactionType())){
	    if(!StringUtils.hasText(head.getDeliveryWarehouseCode()))
		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入轉出庫", head.getLastUpdatedBy());
	    //if(!StringUtils.hasText(head.getArrivalWarehouseCode()))
	    //siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請輸入轉入庫", head.getLastUpdatedBy());
	    if(!StringUtils.hasText(head.getSourceOrderNo()) && 
		    StringUtils.hasText(head.getDeliveryWarehouseCode()) && StringUtils.hasText(head.getArrivalWarehouseCode()) &&
		    !head.getDeliveryWarehouseCode().equals(head.getArrivalWarehouseCode()))
		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "轉出庫與轉入庫需為同一庫別", head.getLastUpdatedBy());
	}

    }

    /**
     * 檢查單頭
     */
    public void checkItem(ImStorageHead head,String identification) throws Exception {
	log.info("checkItem");
	List<ImStorageItem> imStorageItems = head.getImStorageItems();
	int itemCount = 0;

	Map itemcountMap = new HashMap();

	for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
	    ImStorageItem item = (ImStorageItem) iterator.next();
	    String itemCode = item.getItemCode();
	    if(!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(item.getIsDeleteRecord())){
		itemCount++;
		ImItem imItem = imItemService.findItem(head.getBrandCode(), itemCode);
		if( null == imItem )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項品號輸入錯誤", head.getLastUpdatedBy());

		if( null == item.getStorageQuantity() )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項實出量不可為空", head.getLastUpdatedBy());

		if( !StringUtils.hasText(item.getStorageInNo()) )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項進貨日不可為空", head.getLastUpdatedBy());

		if( !StringUtils.hasText(item.getStorageLotNo()) )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項效期不可為空", head.getLastUpdatedBy());

		if( StringUtils.hasText(item.getDeliveryWarehouseCode()) && !StringUtils.hasText(item.getDeliveryStorageCode()) )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項轉出儲位不可為空", head.getLastUpdatedBy());

		if( StringUtils.hasText(item.getArrivalWarehouseCode()) && !StringUtils.hasText(item.getArrivalStorageCode()) )
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項轉入儲位轉入儲位不可為空", head.getLastUpdatedBy());

		//if(!StringUtils.hasText(item.getDeliveryWarehouseCode()))
		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項出庫未填寫", head.getLastUpdatedBy());
		//if(!StringUtils.hasText(item.getArrivalWarehouseCode()))
		//siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "明細第"+item.getIndexNo()+"項入庫未填寫", head.getLastUpdatedBy());

		//計算品號與數量
		if(null == itemcountMap.get(itemCode)){
		    //log.info("keyString = " + keyString);
		    itemcountMap.put(itemCode, (item.getStorageQuantity()) );
		}else{
		    Double quantity = (Double)itemcountMap.get(itemCode);
		    itemcountMap.put(itemCode, quantity + (item.getStorageQuantity()) );
		}
	    }
	}

	if("IAM".equals(head.getOrderTypeCode()) && null != head.getSourceOrderTypeCode() && null != head.getSourceOrderNo()){
	    log.info("IAM");
	    ImReceiveHead imReceiveHead = (ImReceiveHead)baseDAO.findFirstByProperty("ImReceiveHead", "",
		    " and brandCode = ? and orderTypeCode = ? and orderNo = ? ",
		    new String[] {head.getBrandCode(), head.getSourceOrderTypeCode(), head.getSourceOrderNo()} , "");
	    if(null != imReceiveHead){
		try{
	    	    compareImStroageWithReceive(imReceiveHead, head, identification);
		}catch(Exception ex){
		 siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
	    			"比對來源單別: "+head.getSourceOrderTypeCode()+" 單號: "+head.getSourceOrderNo()+" 異常", head.getLastUpdatedBy());
		}
		
	    }else{
	    	siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
	    			"查無來源單別: "+head.getSourceOrderTypeCode()+" 單號: "+head.getSourceOrderNo()+" 對應的單據", head.getLastUpdatedBy());
	    }

	}


	//如果是調整單 請檢察正負是否相同
	if("IAA".equals(head.getOrderTypeCode())){
	    Iterator itemcount = itemcountMap.keySet().iterator();
	    while (itemcount.hasNext()) {
		String itemCode = (String) itemcount.next();
		Double quantity = (Double)itemcountMap.get(itemCode);
		if(0 != quantity){
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "品號"+itemCode+"的數量正負相加不為零", head.getLastUpdatedBy());
		}
	    }
	}

	if(0 == itemCount)
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, "請至少輸入一筆正確可用資料", head.getLastUpdatedBy());	
    }

    /**檢核是否有porgramLog
     */
    public Integer deleteProgramLog(String identification, Integer opType) throws Exception {
	Integer errorCnt = 0;
	try{
	    if( opType==0 ){
		// clear 原有 ERROR RECORD
		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    }else if( opType==1 ){
		List errorLogs = siProgramLogDAO.findByIdentification(PROGRAM_ID, null, identification);
		if(null!=errorLogs)
		    errorCnt = errorLogs.size();
	    }
	    return errorCnt;
	}catch (Exception ex) {
	    log.error("進貨單刪除 Program Log 失敗，原因：" + ex.toString());
	    throw new Exception("進貨單刪除 Program Log 失敗，原因：" + ex.getMessage());
	}
    }

    /**
     * 檢核後的更新
     */
    public void updateAfterCheck(Map parameterMap, Map returnMap) throws Exception {
	try{
	    Object otherBean    = parameterMap.get("vatBeanOther");
	    String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
	    String identification = (String)PropertyUtils.getProperty(otherBean, "identification");
	    ImStorageHead head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	    if(OrderStatus.FINISH.equals(formStatus)){
		updateStorageOnHand(head, formStatus, PROGRAM_ID, identification, false);
		deleteLine(parameterMap, returnMap);
	    }
	    setOrderNo(parameterMap, returnMap);
	    head.setStatus(formStatus);
	    baseDAO.update(head);
	}catch (Exception ex){
	    throw ex;
	}
    }

    /**
     * 刪除單身IS_DELETE_RECORD_TRUE
     */
    public void deleteLine(Map parameterMap, Map returnMap) throws Exception {
	log.info("test ImStorgeService deleteLine ok");

	ImStorageHead head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	log.info("head = " + head);
	List<ImStorageItem> imStorageItems = head.getImStorageItems();
	if(imStorageItems != null && imStorageItems.size() > 0){
	    for(int i = imStorageItems.size() - 1; i >= 0; i--){
		ImStorageItem imStorageItem = imStorageItems.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imStorageItem.getIsDeleteRecord()) || 0 == imStorageItem.getStorageQuantity()){
		    imStorageItems.remove(imStorageItem);
		}
	    }
	}
	for (int i = 0; i < imStorageItems.size(); i++) {
	    ImStorageItem line = imStorageItems.get(i); 
	    line.setIndexNo(i+1L);
	}
	head.setImStorageItems(imStorageItems);
	returnMap.put(AjaxUtils.AJAX_FORM,head);
    }

    /**
     * 刪除單身IS_DELETE_RECORD_TRUE
     */
    public void deleteLine(ImStorageHead head) throws Exception {
	log.info("test ImStorgeService deleteLine ok");

	log.info("head = " + head);
	List<ImStorageItem> imStorageItems = head.getImStorageItems();
	if(imStorageItems != null && imStorageItems.size() > 0){
	    for(int i = imStorageItems.size() - 1; i >= 0; i--){
		ImStorageItem imStorageItem = imStorageItems.get(i);         
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imStorageItem.getIsDeleteRecord())){
		    imStorageItems.remove(imStorageItem);
		}
	    }
	}
	for (int i = 0; i < imStorageItems.size(); i++) {
	    ImStorageItem line = imStorageItems.get(i); 
	    line.setIndexNo(i+1L);
	}
	head.setImStorageItems(imStorageItems);
    }

    /**
     * 設定單號
     */
    public void setOrderNo(Map parameterMap, Map returnMap) throws ObtainSerialNoFailedException {
	ImStorageHead head = (ImStorageHead)returnMap.get(AjaxUtils.AJAX_FORM);
	String orderNo = head.getOrderNo();
	if (AjaxUtils.isTmpOrderNo(orderNo)) {
	    try {
		setOrderNo(head);
		returnMap.put(AjaxUtils.AJAX_FORM, head);
		returnMap.put(AjaxUtils.IDENTIFICATION, getIdentification(parameterMap, returnMap)); // 更新temp單
	    } catch (Exception ex) {
		throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
	    }
	}
    }


    public void setOrderNo(ImStorageHead head) throws ObtainSerialNoFailedException {
	log.info("setOrderNo");
	String orderNo = head.getOrderNo();
	if (null == orderNo || AjaxUtils.isTmpOrderNo(orderNo)) {
	    try {
		String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
		if ("unknow".equals(serialNo)) 
		    throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode() + "單號失敗！");
		else{
		    head.setCreationDate(new Date());
		    head.setOrderNo(serialNo);
		}	
	    } catch (Exception ex) {
		throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
	    }
	}
    }


    /**執行儲位單查詢初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	log.info("executeSearchInitial");
	HashMap resultMap = new HashMap();
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
	    if(null == buOrderType)
		throw new Exception("查無對應之單據類別");
	    Map multiList = new HashMap(0);
	    log.info("buOrderType.getTypeCode() = " + buOrderType.getTypeCode());
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, buOrderType.getTypeCode());
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
	    resultMap.put("multiList",multiList);

	    return resultMap;
	}catch (Exception ex) {
	    log.error("儲位單查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("儲位單查詢初始化失敗，原因：" + ex.toString());
	}           
    }

    /**
     * 取得查詢的單身
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest)throws Exception {
	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

	    String orderNo = httpRequest.getProperty("orderNo" );
	    Date startDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,httpRequest.getProperty("startDate") );
	    Date endDate = DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH,httpRequest.getProperty("endDate") );

	    String status = httpRequest.getProperty("status");
	    String sourceOrderTypeCode = httpRequest.getProperty("sourceOrderTypeCode");
	    String sourceOrderNo = httpRequest.getProperty("sourceOrderNo");
	    String employeeCode = httpRequest.getProperty("itemCode");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
	    findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
	    findObjs.put(" and model.orderNo = :orderNo",orderNo);
	    findObjs.put(" and model.storageTransactionDate >= :startDate",startDate);
	    findObjs.put(" and model.storageTransactionDate <= :endDate",endDate);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.sourceOrderTypeCode = :sourceOrderTypeCode",sourceOrderTypeCode);
	    findObjs.put(" and model.sourceOrderNo = :sourceOrderNo", sourceOrderNo);
	    findObjs.put(" and model.employeeCode = :employeeCode", employeeCode);
	    //==============================================================	    
	    Map headMap = baseDAO.search( "ImStorageHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<ImStorageHead> heads = (List<ImStorageHead>) headMap.get(BaseDAO.TABLE_LIST); 
	    if (heads != null && heads.size() > 0) {
		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)baseDAO.search("ImStorageHead as model", "count(model.storageHeadId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,heads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的儲位單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的儲位單查詢失敗！");
	}		
    }

    /**
     * 將儲位查詢結果存檔
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
     * ajax picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
		pickerResult.put("imStorageResult", result);
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
     * sql 匯出excel
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws Exception
     */
    public SelectDataInfo getAJAXSearchExportData(HttpServletRequest httpRequest) throws Exception{
	StringBuffer sql = new StringBuffer();
	Object[] object = null;
	List rowData = new ArrayList();
	try {
	    String brandCode = httpRequest.getParameter("brandCode");
	    String orderTypeCode = httpRequest.getParameter("orderTypeCode");
	    String orderNo = httpRequest.getParameter("orderNo");
	    String startDate = httpRequest.getParameter("startDate");//DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, httpRequest.getParameter("startDate"));
	    String endDate = httpRequest.getParameter("endDate");//DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, httpRequest.getParameter("endDate"));
	    String sourceOrderTypeCode = httpRequest.getParameter("sourceOrderTypeCode");
	    String sourceOrderNo = httpRequest.getParameter("sourceOrderNo");
	    String employeeCode = httpRequest.getParameter("employeeCode");
	    String status = httpRequest.getParameter("status");

	    sql.append("SELECT ORDER_TYPE_CODE, ORDER_NO, SOURCE_ORDER_TYPE_CODE, SOURCE_ORDER_NO, TRANSACTION_DATE, STATUS ")
	    .append("FROM IM_STORAGE_HEAD ")
	    .append("WHERE BRAND_CODE = '").append(brandCode).append("' ")
	    .append("AND ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
	    .append("AND SUBSTR(ORDER_NO,1,3) <> 'TMP' ");

	    if (StringUtils.hasText(status)) 
		sql.append("AND STATUS = '").append(status).append("' ");
	    if(StringUtils.hasText(employeeCode))
		sql.append("AND CREATED_BY = '").append(employeeCode).append("' ");
	    if (StringUtils.hasText(orderNo))
		sql.append("AND ORDER_NO = '").append(orderNo).append("' ");
	    if (StringUtils.hasText(sourceOrderTypeCode))
		sql.append("AND SOURCE_ORDER_TYPE_CODE = '").append(sourceOrderTypeCode).append("' ");
	    if (StringUtils.hasText(sourceOrderNo))
		sql.append("AND SOURCE_ORDER_NO = '").append(sourceOrderNo).append("' ");
	    if (null != startDate)
		sql.append("AND TRANSACTION_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
	    if (null !=  endDate)
		sql.append("AND TRANSACTION_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");

	    sql.append("ORDER BY LAST_UPDATE_DATE DESC ");

	    log.info("sql = " + sql);

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());
	    object = new Object[] { "orderTypeCode", "orderNo", "sourceOrderTypeCode", 
		    "sourceOrderNo", "storageTransactionDate", "status"
	    };
	    log.info(" lists.size() = " + lists.size() );
	    for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
		Object[] getObj = (Object[])lists.get(i);
		Object[] dataObject = new Object[object.length];
		for (int j = 0; j < object.length; j++) {
		    if(j==5){ //狀態
			String statusCode = String.valueOf(getObj[5]);
			dataObject[j] = OrderStatus.getChineseWord(statusCode);
		    }else{
			dataObject[j] = getObj[j];
		    }
		}
		rowData.add(dataObject);
	    }
	    return new SelectDataInfo(object, rowData);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("匯出excel發生錯誤，原因：" + e.toString());
	    throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * 各單據呼叫儲位單扣庫存
     */
    public void updateStorageOnHandBySource(Object objHead, String nextStatus,
	    String programId, String identification, boolean isReject) throws Exception {
	log.info("updateStorageOnHandBySource");

	String brandcode = BeanUtils.getNestedProperty(objHead, "brandCode");
	String sourceOrderTypeCode = BeanUtils.getNestedProperty(objHead, "orderTypeCode");
	String sourceOrderNo = BeanUtils.getNestedProperty(objHead, "orderNo");
	ImStorageHead imStorageHead = (ImStorageHead)baseDAO.findFirstByProperty("ImStorageHead", "",
		" and brandCode = ? and sourceOrderTypeCode = ? and sourceOrderNo = ? ",
		new String[] {brandcode, sourceOrderTypeCode, sourceOrderNo} , "");

	if(null == imStorageHead){
	    log.error("查無對應儲位單");
	    //throw new Exception("查無對應儲位單");
	}else if (null == imStorageHead.getImStorageItems() || 0 == imStorageHead.getImStorageItems().size()){
	    log.error("查無儲位明細");
	}else{
	    //更新庫存
	    updateStorageOnHand(imStorageHead, nextStatus, programId, identification, isReject);

	    //清除明細IS_DELETE_RECORD_TRUE
	    deleteLine(imStorageHead);

	    imStorageHead.setStatus(nextStatus);
	    baseDAO.update(imStorageHead);
	}
    }

    /**
     * 各單據呼叫儲位單將明細數量乘負一
     */
    public void resetStorageQuantity(Object objHead) throws Exception {
	log.info("resetStorageQuantity");

	String brandcode = BeanUtils.getNestedProperty(objHead, "brandCode");
	String sourceOrderTypeCode = BeanUtils.getNestedProperty(objHead, "orderTypeCode");
	String sourceOrderNo = BeanUtils.getNestedProperty(objHead, "orderNo");
	ImStorageHead imStorageHead = (ImStorageHead)baseDAO.findFirstByProperty("ImStorageHead", "",
		" and brandCode = ? and sourceOrderTypeCode = ? and sourceOrderNo = ? ",
		new String[] {brandcode, sourceOrderTypeCode, sourceOrderNo} , "");

	if(null == imStorageHead){
	    log.error("查無對應儲位單");
	    //throw new Exception("查無對應儲位單");
	}else{

	    List<ImStorageItem> imStorageItems= imStorageHead.getImStorageItems();
	    for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
		ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
		imStorageItem.setStorageQuantity(imStorageItem.getStorageQuantity() * -1 );
	    }
	    baseDAO.update(imStorageHead);
	}
    }

    /**
     * 透過各單據的資料建立儲位單頭
     */
    public ImStorageHead executeImStorageHead(Map storageMap, Object objHead) throws StorageException {
	try{
	    log.info("executeImStorageHead");
	    ImStorageHead imStorageHead = null;
	    String brandCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "brandCode");
	    String orderTypeCode = null;
	    Date storageTransactionDate = (Date)BeanUtil.getBeanValue(storageMap, objHead, "storageTransactionDate");
	    String storageTransactionType = (String)BeanUtil.getBeanValue(storageMap, objHead, "storageTransactionType");
	    String sourceOrderTypeCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "orderTypeCode");
	    String sourceOrderNo =(String)BeanUtil.getBeanValue(storageMap, objHead, "orderNo");
	    String status =(String)BeanUtil.getBeanValue(storageMap, objHead, "status");
	    String deliveryWarehouseCode = null;
	    String arrivalWarehouseCode = null;
	    String lastUpdatedBy = (String)BeanUtil.getBeanValue(storageMap, objHead, "lastUpdatedBy");

	    log.info("sourceOrderTypeCode = " + sourceOrderTypeCode);
	    log.info("sourceOrderNo = " + sourceOrderNo);

	    if(IN.equals(storageTransactionType)){
		orderTypeCode = "ISI";
		arrivalWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "arrivalWarehouseCode");
		//arrivalWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "deliveryWarehouseCode");
	    }else if(MOVE.equals(storageTransactionType)){
		orderTypeCode = "ISM";
		deliveryWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "deliveryWarehouseCode");
		arrivalWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "arrivalWarehouseCode");
	    }else if(OUT.equals(storageTransactionType)){
		orderTypeCode = "ISO";
		//arrivalWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "arrivalWarehouseCode");
		deliveryWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "deliveryWarehouseCode");
	    }else if(ADJ.equals(storageTransactionType)){
		orderTypeCode = "ISA";
		//deliveryWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "deliveryWarehouseCode");
		arrivalWarehouseCode = (String)BeanUtil.getBeanValue(storageMap, objHead, "arrivalWarehouseCode");
	    }else{
		throw new Exception("未輸入正確的儲位調整類型(storageTransactionType)");
	    }

	    log.info("deliveryWarehouseCode = " + deliveryWarehouseCode);
	    log.info("arrivalWarehouseCode = " + arrivalWarehouseCode);

	    Map parameterMap = new HashMap();
	    parameterMap.put("brandCode", brandCode);
	    parameterMap.put("employeeCode", lastUpdatedBy);
	    parameterMap.put("orderTypeCode", orderTypeCode);
	    parameterMap.put("storageTransactionDate", storageTransactionDate);
	    parameterMap.put("storageTransactionType", storageTransactionType);
	    parameterMap.put("sourceOrderTypeCode", sourceOrderTypeCode);
	    parameterMap.put("sourceOrderNo", sourceOrderNo);
	    parameterMap.put("status", status);
	    parameterMap.put("deliveryWarehouseCode", deliveryWarehouseCode);
	    parameterMap.put("arrivalWarehouseCode", arrivalWarehouseCode);

	    imStorageHead = createImStorageHead(parameterMap);

	    return imStorageHead;
	}catch(Exception ex){
	    throw new StorageException("執行儲位單相關失敗");
	}
    }

    /**
     * 依據參數建立儲位單頭
     */
    public ImStorageHead createImStorageHead(Map confMap) throws Exception {
	log.info("createImStorageHead");
	ImStorageHead imStorageHead = null;
	try{
	    //建立單頭
	    String brandCode = null;
	    String employeeCode = null;
	    String orderTypeCode = null;
	    Date storageTransactionDate = new Date();
	    String storageTransactionType = null;
	    String sourceOrderTypeCode = null;
	    String sourceOrderNo = null;
	    String status = null;
	    String deliveryWarehouseCode = null;
	    String arrivalWarehouseCode = null;

	    if(null != confMap){

		if(null != confMap.get("brandCode"))
		    brandCode = (String)confMap.get("brandCode");

		if(null != confMap.get("employeeCode"))
		    employeeCode = (String)confMap.get("employeeCode");

		if(null != confMap.get("orderTypeCode"))
		    orderTypeCode = (String)confMap.get("orderTypeCode");

		if(null != confMap.get("storageTransactionDate"))
		    storageTransactionDate = (Date)confMap.get("storageTransactionDate");

		if(null != confMap.get("storageTransactionType"))
		    storageTransactionType = (String)confMap.get("storageTransactionType");

		if(null != confMap.get("sourceOrderTypeCode"))
		    sourceOrderTypeCode = (String)confMap.get("sourceOrderTypeCode");

		if(null != confMap.get("sourceOrderNo"))
		    sourceOrderNo = (String)confMap.get("sourceOrderNo");

		if(null != confMap.get("status"))
		    status = (String)confMap.get("status");

		if(null != confMap.get("deliveryWarehouseCode"))
		    deliveryWarehouseCode = (String)confMap.get("deliveryWarehouseCode");

		if(null != confMap.get("arrivalWarehouseCode"))
		    arrivalWarehouseCode = (String)confMap.get("arrivalWarehouseCode");

		if(StringUtils.hasText(brandCode) && StringUtils.hasText(sourceOrderTypeCode) && 
			StringUtils.hasText(sourceOrderNo) && StringUtils.hasText(orderTypeCode))

		    imStorageHead = (ImStorageHead)baseDAO.findFirstByProperty("ImStorageHead", "",
			    " and brandCode = ? and orderTypeCode = ? and sourceOrderTypeCode = ? and sourceOrderNo = ? ",
			    new String[] {brandCode, orderTypeCode, sourceOrderTypeCode, sourceOrderNo} , "");
	    }

	    if(null == imStorageHead){
		log.info("新建儲位單");
		imStorageHead = new ImStorageHead();
		imStorageHead.setBrandCode(brandCode);
		imStorageHead.setOrderTypeCode(orderTypeCode);
		imStorageHead.setStorageTransactionDate(DateUtils.getShortDate(storageTransactionDate));
		imStorageHead.setStorageTransactionType(storageTransactionType);
		if(AjaxUtils.isTmpOrderNo(sourceOrderNo))
		    imStorageHead.setOrderNo(AjaxUtils.getTmpOrderNo());
		else{
		    String serialNo = buOrderTypeService.getOrderSerialNo(imStorageHead.getBrandCode(), imStorageHead.getOrderTypeCode());
		    imStorageHead.setOrderNo(serialNo);
		}
		imStorageHead.setSourceOrderTypeCode(sourceOrderTypeCode);
		imStorageHead.setSourceOrderNo(sourceOrderNo);
		imStorageHead.setDeliveryWarehouseCode(deliveryWarehouseCode);
		imStorageHead.setArrivalWarehouseCode(arrivalWarehouseCode);
		imStorageHead.setStatus(status);
		imStorageHead.setCreatedBy(employeeCode);
		imStorageHead.setCreationDate(new Date());
		imStorageHead.setLastUpdatedBy(employeeCode);
		imStorageHead.setLastUpdateDate(new Date());
		baseDAO.save(imStorageHead);
	    }else if(OrderStatus.SAVE.equals(imStorageHead.getStatus())){
		log.info("若儲位單為暫存，則更新儲位單");
		imStorageHead.setStorageTransactionDate(DateUtils.getShortDate(storageTransactionDate));
		imStorageHead.setDeliveryWarehouseCode(deliveryWarehouseCode);
		imStorageHead.setArrivalWarehouseCode(arrivalWarehouseCode);
		imStorageHead.setLastUpdatedBy(employeeCode);
		imStorageHead.setLastUpdateDate(new Date());
		baseDAO.update(imStorageHead);
	    }

	}catch(Exception ex){
	    ex.printStackTrace();
	    throw ex;
	}

	return imStorageHead;
    }

    /**
     * 依據各單據與參數 建立/更新儲位單身
     * Param itemDeleteFlag => true 則建立新儲位明細
     */
    public void executeImStorageItem(Map storageMap, Object objHead, ImStorageHead imStorageHead, boolean itemDeleteFlag) throws StorageException{
	log.info("executeImStorageItem");

	try{
	    List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
	    List objItems = (List)BeanUtil.getBeanValue(storageMap, objHead, "beanItem");

	    boolean newItems = false;

	    //如果儲位明細為空，建立新明細
	    if(null == imStorageItems || 0 == imStorageItems.size()){
	    	log.info("如果儲位明細為空，建立新明細");
	    	imStorageItems = new ArrayList<ImStorageItem>(0);
	    	newItems = true;
	    	//如果儲位明細不為空，而且itemDeleteFlag為true，建立新明細
	    }else if(null != imStorageItems && itemDeleteFlag){
	    	log.info("如果儲位明細不為空，而且itemDeleteFlag為true，建立新明細");
	    	//baseDAO.delete(imStorageItems);
	    	imStorageItems = new ArrayList<ImStorageItem>(0);
	    	newItems = true;
	    }/* else if (imStorageItems.size() != objItems.size()){
	    	log.info("如果儲位明細不為空，而且imStorageItems.size() != objItems.size()，建立新明細");
	    	imStorageItems = new ArrayList<ImStorageItem>(0);
	    	newItems = true;
	    }*/

	    int i = 0;
	    for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
	    	Object objItem = (Object) iterator.next();

			if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals((String)BeanUtil.getBeanValue(storageMap, objItem, "isDeleteRecord")))
			    continue;
	
			ImStorageItem imStorageItem = null;
			if(newItems){
			    imStorageItem = new ImStorageItem();
			    imStorageItem.setImStorageHead(imStorageHead);
			    imStorageItems.add(imStorageItem);
			    //設定頁面資訊
			    imStorageItem.setItemCode((String)BeanUtil.getBeanValue(storageMap, objItem, "itemCode"));
			    imStorageItem.setStorageQuantity((Double)BeanUtil.getBeanValue(storageMap, objItem, "quantity"));
			    imStorageItem.setIsDeleteRecord((String)BeanUtil.getBeanValue(storageMap, objItem, "isDeleteRecord"));
			}else{
			    imStorageItem = imStorageItems.get(i++);
			}
	
			//設定轉出庫
			imStorageItem.setDeliveryWarehouseCode(imStorageHead.getDeliveryWarehouseCode());
	
			//設定轉入庫
			imStorageItem.setArrivalWarehouseCode(imStorageHead.getArrivalWarehouseCode());
			
	    }
	    imStorageHead.setImStorageItems(imStorageItems);
	    baseDAO.update(imStorageHead);

	}catch(Exception ex){
	    ex.printStackTrace();
	    throw new StorageException(ex);
	}
    }


    /**
     * 依據各單據與參數建立/更新儲位單身
     */
    public void executeImStorageItemExtend(Map storageMap, ImStorageHead imStorageHead, String isClean) throws StorageException{
	log.info("executeImStorageItemExtend");
	String pickOrderTypeCode = "";
	String pickOrderNo = "";
	ImPickHead imPickHead = null;
	String brandCode = imStorageHead.getBrandCode();
	Long storageHeadId = imStorageHead.getStorageHeadId();

	try{
	    pickOrderTypeCode = (String)storageMap.get("pickOrderTypeCode");
	    pickOrderNo = (String)storageMap.get("pickOrderNo");

	    log.info("brandCode = " + brandCode);
	    log.info("pickOrderTypeCode = " + pickOrderTypeCode);
	    log.info("pickOrderNo = " + pickOrderNo);

	    //找尋挑貨單
	    if(StringUtils.hasText(pickOrderTypeCode) && StringUtils.hasText(pickOrderNo))
		imPickHead = (ImPickHead)baseDAO.findFirstByProperty("ImPickHead", "", " and brandCode = ? and orderTypeCode = ? and orderNo = ? ",
			new String[] {brandCode, pickOrderTypeCode, pickOrderNo} , "");

	    //如果是從挑貨單轉的
	    if(null != imPickHead){
		log.info("null != imPickHead");
		List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
		List<ImStorageItem> imStorageItemsNew = new ArrayList<ImStorageItem>(0);
		for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
		    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
		    Double storageQuantity = imStorageItem.getStorageQuantity();

		    //利用此儲位明細品號去挑貨單找出來尚可用的明細
		    List<ImPickItem> imPickItems = baseDAO.findByProperty("ImPickItem", " and imPickHead.headId = ? and itemCode = ? and (blockQuantity - commitQuantity) > 0 and arrivalWarehouseCode = ? ", 
			    new Object[]{ imPickHead.getHeadId(), imStorageItem.getItemCode(), imStorageItem.getArrivalWarehouseCode()});
		    //如果有找到挑貨單明細，則分配數量
		    if(null != imPickItems && imPickItems.size() > 0){
			log.info("null != imPickItems");
			for (Iterator iterator2 = imPickItems.iterator(); iterator2.hasNext();) {
			    ImPickItem imPickItem = (ImPickItem) iterator2.next();
			    log.info("null != imPickItems");
			    Double avaliableQuantity = imPickItem.getBlockQuantity() - imPickItem.getCommitQuantity();

			    ImStorageItem imStorageItemNew = new ImStorageItem();
			    BeanUtils.copyProperties(imStorageItemNew, imStorageItem);
			    imStorageItemNew.setIndexNo(null);
			    imStorageItemNew.setStorageLineId(null);

			    if(avaliableQuantity >= storageQuantity){
				imStorageItemNew.setStorageQuantity(storageQuantity);
			    }else{
				imStorageItemNew.setStorageQuantity(avaliableQuantity);
			    }

			    imStorageItemNew.setStorageInNo(imPickItem.getStorageInNo());
			    imStorageItemNew.setStorageLotNo(imPickItem.getStorageLotNo());
			    imStorageItemNew.setDeliveryStorageCode(imPickItem.getStorageCode());
			    imStorageItemNew.setPickLineId(imPickItem.getLineId());
			    imStorageItemsNew.add(imStorageItemNew);

			    storageQuantity = storageQuantity - avaliableQuantity;
			    if(storageQuantity <= 0 )
				break;
			}
		    }

		    //如果還有剩下的，則原樣複製
		    if(storageQuantity > 0){
			ImStorageItem imStorageItemNew = new ImStorageItem();
			BeanUtils.copyProperties(imStorageItemNew, imStorageItem);
			imStorageItemNew.setStorageQuantity(storageQuantity);
			imStorageItemNew.setIndexNo(null);
			imStorageItemNew.setStorageLineId(null);
			imStorageItemsNew.add(imStorageItemNew);
		    }
		}

		imStorageHead.setImStorageItems(imStorageItemsNew);
		baseDAO.update(imStorageHead);

		//一般展報單
	    }else{
		log.info("null == imPickHead");
		HashMap extendMap = new HashMap();
		extendMap.put("processObjectName", "imStorageService");
		extendMap.put("searchMethodName", "getBeanByHeadId");
		extendMap.put("searchKey", storageHeadId);
		extendMap.put("subEntityBeanName", "imStorageItems");
		extendMap.put("tableType", "IM_STROAGE");
		extendMap.put("isClean", isClean);
		appExtendItemStorageInfoService.executeExtendItemStorage(extendMap);		
	    }
	}catch (Exception e){
	    throw new StorageException(e);
	}
    }

    /**
     * 重新設置明細的儲位、進貨日、批號資訊
     */
    public ImStorageHead updateResetImStorageItem(ImStorageHead imStorageHead, String inLotNo) throws StorageException{
	log.info("updateResetImStorageItem " + inLotNo);
	List<ImStorageItem> imStorageItems =imStorageHead.getImStorageItems();
	for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
	    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();

	    //設定轉出儲位
	    if(!StringUtils.hasText(imStorageHead.getDeliveryWarehouseCode()))
		imStorageItem.setDeliveryStorageCode(null);
	    else if(!StringUtils.hasText(imStorageItem.getDeliveryStorageCode()))
		imStorageItem.setDeliveryStorageCode(STORAGE_CODE_DEFAULT);

	    //設定轉入儲位
	    if(!StringUtils.hasText(imStorageHead.getArrivalWarehouseCode()))
		imStorageItem.setArrivalStorageCode(null);
	    else if(!StringUtils.hasText(imStorageItem.getArrivalStorageCode()))
		imStorageItem.setArrivalStorageCode(STORAGE_CODE_DEFAULT);

	    //設定明細的進貨日
	    if (!StringUtils.hasText(imStorageItem.getStorageInNo())){
		if(StringUtils.hasText(inLotNo))
		    imStorageItem.setStorageInNo(inLotNo);	
		else
		    imStorageItem.setStorageInNo(STORAGE_IN_NO_DEFAULT);
	    }

	    //設定明細的批號
	    if (!StringUtils.hasText(imStorageItem.getStorageLotNo()))
		imStorageItem.setStorageLotNo(STORAGE_LOT_NO_DEFAULT);

	}
	baseDAO.update(imStorageHead);
	return imStorageHead;
    }

    /**
     * 依據刪除儲位單明細
     */
    public void deleteImStorageItem(ImStorageHead imStorageHead) throws Exception{
	List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
	imStorageHead.setImStorageItems(null);
	for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
	    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
	    baseDAO.delete(imStorageItem);
	}
    }

    /**
     * 依據各單據與參數比對儲位庫存
     */
    public void compareImStroage(Map storageMap, Object objHead, ImStorageHead imStorageHead) throws StorageException {
	log.info("compareImStroage");
	try{
	    List objItems = (List)BeanUtil.getBeanValue(storageMap, objHead, "beanItem");
	    Map onHandMap = new HashMap();


	    List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
	    if(null != objItems && objItems.size() > 0){
		if(null == imStorageItems || imStorageItems.size() == 0){
		    return;
		    //throw new StorageException("儲位不可為空");
		}
	    }

	    Map storageOnHandMap = new HashMap();
	    for (Iterator iterator = objItems.iterator(); iterator.hasNext();) {
		Object objItem = (Object) iterator.next();
		String isDeleteRecord = (String)BeanUtil.getBeanValue(storageMap, objItem, "isDeleteRecord");
		String itemCode = (String)BeanUtil.getBeanValue(storageMap, objItem, "itemCode");
		Double sourceQuantity = (Double)BeanUtil.getBeanValue(storageMap, objItem, "quantity");
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(isDeleteRecord) && 0 != sourceQuantity){
		    if(null == onHandMap.get(itemCode)){
			onHandMap.put(itemCode, sourceQuantity);
		    }else{
			Double quantity = (Double)onHandMap.get(itemCode);
			onHandMap.put(itemCode, quantity + sourceQuantity);
		    }
		}
	    }

	    for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
		ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
		String itemCode = imStorageItem.getItemCode();
		Double storageQuantity = imStorageItem.getStorageQuantity();
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(imStorageItem.getIsDeleteRecord()) && 0 != storageQuantity ){
		    if(null == storageOnHandMap.get(itemCode)){
			storageOnHandMap.put(itemCode, imStorageItem.getStorageQuantity());
		    }else{
			Double quantity = (Double)storageOnHandMap.get(itemCode);
			storageOnHandMap.put(itemCode, quantity + imStorageItem.getStorageQuantity());
		    }
		}
	    }

	    Set storageOnHandSet = storageOnHandMap.keySet();
	    Set onHandSet = onHandMap.keySet();
	    if(storageOnHandSet.size() != onHandSet.size()){
		throw new StorageException("來源單據與儲位單據品號數量不同");
	    }

	    for (Iterator iterator = onHandSet.iterator(); iterator.hasNext();) {
		String  objItemCode = (String ) iterator.next();
		Double objQuantity = 0D;
		if(null != onHandMap.get(objItemCode))
		    objQuantity = (Double)onHandMap.get(objItemCode);
		else
		    throw new StorageException("來源單據查無品號"+objItemCode+"對應的數量");

		Double storageQuantity = 0D;
		if(null != storageOnHandMap.get(objItemCode))
		    storageQuantity = (Double)storageOnHandMap.get(objItemCode);
		else
		    throw new StorageException("儲位單據查無品號"+objItemCode+"對應的數量");

		log.info("objItemCode = " + objItemCode);
		log.info("objQuantity = " + objQuantity);
		log.info("storageQuantity = " + storageQuantity);

		if(!objQuantity.equals(storageQuantity)){
		    throw new StorageException("品號"+objItemCode+"來源單據與儲位單據數量不符");
		}
	    }
	}catch(Exception ex){
	    log.error(ex.toString());
	    throw new StorageException(ex);
	}
    }

    /**
     * 依據比對儲位單數量不可超過進貨單
     */
    public void compareImStroageWithReceive(ImReceiveHead imReceiveHead, ImStorageHead imStorageHead, String identification) throws Exception {
	log.info("compareImStroageWithReceive");

	//進貨單庫存
	Map onHandMap = new HashMap();
	//已上架儲位單
	Map storageUsedMap = new HashMap();
	//待上架儲位單
	Map storageOnHandMap = new HashMap();

	try{
	    //進貨單明細
	    List<ImReceiveItem> imReceiveItems = imReceiveHead.getImReceiveItems();

	    //已上架儲位單明細
	    List<ImStorageItem> usedImStorageItems = new ArrayList<ImStorageItem>(0);
	    //找出已上架儲位單
	    List<ImStorageHead> usedImStorageHeads = baseDAO.findByProperty("ImStorageHead", 
		    " and brandCode = ? and orderTypeCode = ? and orderNo <> ? and status = ? and sourceOrderTypeCode = ? and sourceOrderNo = ?", 
		    new Object[]{ imStorageHead.getBrandCode(), imStorageHead.getOrderTypeCode(), imStorageHead.getOrderNo(), 
		    OrderStatus.FINISH, imStorageHead.getSourceOrderTypeCode(), imStorageHead.getSourceOrderNo()});
	    //加總已上架儲位單
	    if(null != usedImStorageHeads && usedImStorageHeads.size() > 0){
		for (Iterator iterator = usedImStorageHeads.iterator(); iterator.hasNext();) {
		    ImStorageHead usedImStorageHead = (ImStorageHead) iterator.next();

		    if(null != usedImStorageHead && null != usedImStorageHead.getImStorageItems() && usedImStorageHead.getImStorageItems().size() > 0){
			List<ImStorageItem> imStorageItems = usedImStorageHead.getImStorageItems();
			for (Iterator iterator2 = imStorageItems.iterator(); iterator2.hasNext();) {
			    ImStorageItem imStorageItem = (ImStorageItem) iterator2.next();
			    usedImStorageItems.add(imStorageItem);
			}
		    }
		}
	    }

	    //待上架儲位單明細
	    List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
	    if(null != imReceiveItems && imReceiveItems.size() > 0){
		if(null == imStorageItems || imStorageItems.size() == 0){
		    throw new StorageException("儲位不可為空");
		}
	    }

	    //進貨單庫存
	    for (Iterator iterator = imReceiveItems.iterator(); iterator.hasNext();) {
		ImReceiveItem imReceiveItem = (ImReceiveItem) iterator.next();
		String itemCode = imReceiveItem.getItemCode();
		Double quantity = imReceiveItem.getReceiptQuantity();
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(imReceiveItem.getIsDeleteRecord()) && 0 != quantity){
		    if(null == onHandMap.get(itemCode)){
			onHandMap.put(itemCode, quantity);
		    }else{
			Double onHandQuantity = (Double)onHandMap.get(itemCode);
			onHandMap.put(itemCode, onHandQuantity + quantity);
		    }
		}
	    }

	    //已上架儲位單
	    for (Iterator iterator = usedImStorageItems.iterator(); iterator.hasNext();) {
		ImStorageItem usedImStorageItem = (ImStorageItem) iterator.next();
		String itemCode = usedImStorageItem.getItemCode();
		Double storageQuantity = usedImStorageItem.getStorageQuantity();
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(usedImStorageItem.getIsDeleteRecord()) && 0 != storageQuantity ){
		    if(null == storageUsedMap.get(itemCode)){
			storageUsedMap.put(itemCode, usedImStorageItem.getStorageQuantity());
		    }else{
			Double quantity = (Double)storageUsedMap.get(itemCode);
			storageUsedMap.put(itemCode, quantity + usedImStorageItem.getStorageQuantity());
		    }
		}
	    }


	    //待上架儲位單
	    for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
		ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
		String itemCode = imStorageItem.getItemCode();
		Double storageQuantity = imStorageItem.getStorageQuantity();
		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(imStorageItem.getIsDeleteRecord()) && 0 != storageQuantity ){
		    if(null == storageOnHandMap.get(itemCode)){
			storageOnHandMap.put(itemCode, imStorageItem.getStorageQuantity());
		    }else{
			Double quantity = (Double)storageOnHandMap.get(itemCode);
			storageOnHandMap.put(itemCode, quantity + imStorageItem.getStorageQuantity());
		    }
		}
	    }

	    //進貨單扣除已出量
	    log.info("進貨單扣除已出量");
	    Set storageUsedSet = storageUsedMap.keySet();
	    for (Iterator iterator = storageUsedSet.iterator(); iterator.hasNext();) {
		String itemCode = (String) iterator.next();
		Double quantity = 0D;
		if(null != onHandMap.get(itemCode)){
		    quantity = (Double)onHandMap.get(itemCode);
		}else{
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
			    "進貨單查無品號"+itemCode+"對應的數量", imStorageHead.getLastUpdatedBy());
		}

		Double storageQuantity = 0D;
		if(null != storageOnHandMap.get(itemCode))
		    storageQuantity = (Double)storageOnHandMap.get(itemCode);

		log.info("itemCode = " + itemCode);
		log.info("quantity = " + quantity);
		log.info("storageQuantity = " + storageQuantity);
		if(storageQuantity > quantity){
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
			    "品號"+itemCode+"超過進貨單的數量", imStorageHead.getLastUpdatedBy());
		}else{
		    storageOnHandMap.put(itemCode, quantity - storageQuantity);
		}
	    }


	    //比對儲位單的數量是否大於進貨單的進貨量
	    log.info("比對儲位單的數量是否大於進貨單的進貨量");
	    Set storageOnHandSet = storageOnHandMap.keySet();
	    for (Iterator iterator = storageOnHandSet.iterator(); iterator.hasNext();) {
		String  itemCode = (String) iterator.next();
		Double quantity = 0D;
		if(null != onHandMap.get(itemCode)){
		    quantity = (Double)onHandMap.get(itemCode);
		}else{
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
			    "進貨單查無品號"+itemCode+"對應的數量", imStorageHead.getLastUpdatedBy());
		}

		Double storageQuantity = 0D;
		if(null != storageOnHandMap.get(itemCode))
		    storageQuantity = (Double)storageOnHandMap.get(itemCode);

		log.info("itemCode = " + itemCode);
		log.info("quantity = " + quantity);
		log.info("storageQuantity = " + storageQuantity);
		if(storageQuantity > quantity){
		    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, 
			    "品號"+itemCode+"超過進貨單的數量", imStorageHead.getLastUpdatedBy());
		}
	    }

	}catch(Exception ex){
	    throw new Exception(ex);
	}
    }

    /**
     * 儲位單扣庫存
     */
    public void updateStorageOnHand(ImStorageHead head, String nextStatus, 
	    String programId, String identification, boolean isReject) throws Exception{
	try{
	    log.info("updateStorageOnHand");
	    String beforeStatus = head.getStatus();

	    BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(head.getBrandCode(), head.getOrderTypeCode()) );
	    if(null == buOrderType)
		throw new Exception("查無對應之單據類別");

	    boolean allowMinus = true;

	    //如果分為正式單，則為儲位調撥與調整，必須判斷庫存剩餘
	    if(!StringUtils.hasText(buOrderType.getMonthlyBalanceTypeCode()))
		allowMinus = false;

	    //建立imStorageOnHandMap
	    Map imStorageOnHandMap = new HashMap();
	    List<ImStorageItem> items = head.getImStorageItems();

	    //IN => arrival
	    //MOVE => delivery,arrival
	    //OUT => delivery
	    //ADJ => arrival
	    //計算總數
	    for (Iterator iterator = items.iterator(); iterator.hasNext();) {
		ImStorageItem item = (ImStorageItem) iterator.next();
		//log.info("item = " + item.getItemCode());
		//log.info("item.getStorageInNo = " + item.getStorageInNo());
		//log.info("item.getStorageLotNo = " + item.getStorageLotNo());
		//log.info("item.getDeliveryWarehouseCode() = " + item.getDeliveryWarehouseCode());
		//log.info("item.getDeliveryStorageCode = " + item.getDeliveryStorageCode());
		//log.info("item.getArrivalWarehouseCode() = " + item.getArrivalWarehouseCode());
		//log.info("item.getArrivalStorageCode = " + item.getArrivalStorageCode());

		if(AjaxUtils.IS_DELETE_RECORD_FALSE.equals(item.getIsDeleteRecord())){

		    //Delivery
		    if(StringUtils.hasText(item.getDeliveryWarehouseCode())){
			if( (OrderStatus.SAVE.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)) ||
				(OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)) ||
				(OrderStatus.SAVE.equals(beforeStatus) && OrderStatus.WAIT_IN.equals(nextStatus)) ||
				(OrderStatus.UNCONFIRMED.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus)) ||
				(OrderStatus.SAVE.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(OrderStatus.UNCONFIRMED.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(isReject && (OrderStatus.SIGNING.equals(beforeStatus) && OrderStatus.SAVE.equals(nextStatus))) ||
				(isReject && (OrderStatus.SIGNING.equals(beforeStatus) && OrderStatus.REJECT.equals(nextStatus))) ||
				(isReject && (OrderStatus.SIGNING.equals(beforeStatus) && OrderStatus.UNCONFIRMED.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.SAVE.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.REJECT.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.UNCONFIRMED.equals(nextStatus))) ||
				(isReject && (OrderStatus.WAIT_IN.equals(beforeStatus) && OrderStatus.SAVE.equals(nextStatus))) ||
				(isReject && (OrderStatus.WAIT_IN.equals(beforeStatus) && OrderStatus.REJECT.equals(nextStatus))) ||
				(isReject && (OrderStatus.WAIT_IN.equals(beforeStatus) && OrderStatus.UNCONFIRMED.equals(nextStatus)))  
			) {

			    StringBuffer keyBuffer = new StringBuffer("");
			    keyBuffer.append(item.getItemCode()).append("#").append(item.getDeliveryWarehouseCode());
			    keyBuffer.append("#");
			    if(null != item.getDeliveryStorageCode())
				keyBuffer.append(item.getDeliveryStorageCode());
			    keyBuffer.append("#");
			    if(null != item.getStorageInNo())
				keyBuffer.append(item.getStorageInNo());
			    keyBuffer.append("#");
			    if(null != item.getStorageLotNo())
				keyBuffer.append(item.getStorageLotNo());
			    String keyString = keyBuffer.toString();

			    if(null == imStorageOnHandMap.get(keyString)){
				log.info("keyString = " + keyString);
				imStorageOnHandMap.put(keyString, (item.getStorageQuantity()*-1) );
			    }else{
				Double quantity = (Double)imStorageOnHandMap.get(keyString);
				imStorageOnHandMap.put(keyString, quantity + (item.getStorageQuantity()*-1) );
			    }
			}

			//把挑貨單的明細給清空
			if(null != item.getPickLineId()){
			    ImPickItem imPickItem = (ImPickItem)baseDAO.findById("ImPickItem", item.getPickLineId());
			    if(null == imPickItem)
				throw new Exception("查無lineId:" + item.getPickLineId() + "對應的挑貨單明細");
			    Double storageQuantity = item.getStorageQuantity();
			    if(isReject)
				storageQuantity = storageQuantity * -1;
			    imPickItem.setCommitQuantity(imPickItem.getCommitQuantity() + storageQuantity);
			    baseDAO.update(imPickItem);

			    //把blockQuantity給還原
			    //imStorageOnHandDAO.updateBlockUncommitQuantity("TM", head.getBrandCode(), item.getItemCode(), 
				    //item.getDeliveryWarehouseCode(), item.getDeliveryStorageCode(), item.getStorageInNo(), 
				    //item.getStorageLotNo(), storageQuantity * -1, head.getLastUpdatedBy(), allowMinus);
			    executeStorageOnhand(BLOCK, head.getBrandCode(), item.getItemCode(), item.getDeliveryWarehouseCode(), 
			    		item.getDeliveryStorageCode(), item.getStorageInNo(), item.getStorageLotNo(),
			    		storageQuantity * -1, head.getLastUpdatedBy(), allowMinus);
			}
		    }

		    //Arrival
		    if(StringUtils.hasText(item.getArrivalWarehouseCode())){
			if( (OrderStatus.SAVE.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) || 
				(OrderStatus.REJECT.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(OrderStatus.UNCONFIRMED.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(OrderStatus.SIGNING.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(OrderStatus.WAIT_IN.equals(beforeStatus) && OrderStatus.FINISH.equals(nextStatus)) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.SAVE.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.REJECT.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.UNCONFIRMED.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.SIGNING.equals(nextStatus))) ||
				(isReject && (OrderStatus.FINISH.equals(beforeStatus) && OrderStatus.WAIT_IN.equals(nextStatus)))
			) {

			    StringBuffer keyBuffer = new StringBuffer("");
			    keyBuffer.append(item.getItemCode()).append("#").append(item.getArrivalWarehouseCode());
			    keyBuffer.append("#");
			    if(null != item.getArrivalStorageCode())
				keyBuffer.append(item.getArrivalStorageCode());
			    keyBuffer.append("#");
			    if(null != item.getStorageInNo())
				keyBuffer.append(item.getStorageInNo());
			    keyBuffer.append("#");
			    if(null != item.getStorageLotNo())
				keyBuffer.append(item.getStorageLotNo());
			    String keyString = keyBuffer.toString();

			    if(null == imStorageOnHandMap.get(keyString)){
				log.info("keyString = " + keyString);
				imStorageOnHandMap.put(keyString, item.getStorageQuantity());
			    }else{
				Double quantity = (Double)imStorageOnHandMap.get(keyString);
				imStorageOnHandMap.put(keyString, quantity + item.getStorageQuantity());
			    }
			}
		    }
		}
	    }

	    log.info("imStorageOnHandMap.size = " + imStorageOnHandMap.keySet().size());

	    //扣庫存
	    Iterator imStorageOnHand = imStorageOnHandMap.keySet().iterator();
	    while (imStorageOnHand.hasNext()) {
		String keyString = (String) imStorageOnHand.next();
		Double quantity = (Double)imStorageOnHandMap.get(keyString);
		String [] keyStringSet = keyString.split("#");

		String itemCode = null;
		String warehouseCode = null;
		String storageCode = null;
		String storageInNo = null;
		String storageLotNo = null;

		if(keyStringSet.length > 0 && StringUtils.hasText(keyStringSet[0]))
		    itemCode = keyStringSet[0];
		if(keyStringSet.length > 1 && StringUtils.hasText(keyStringSet[1]))
		    warehouseCode = keyStringSet[1];
		if(keyStringSet.length > 2 && StringUtils.hasText(keyStringSet[2]))
		    storageCode = keyStringSet[2];
		if(keyStringSet.length > 3 && StringUtils.hasText(keyStringSet[3]))
		    storageInNo = keyStringSet[3];
		if(keyStringSet.length > 4 && StringUtils.hasText(keyStringSet[4]))
		    storageLotNo = keyStringSet[4];

		//log.info("keyString = " + keyString);
		//log.info("quantity = " + quantity);
		//log.info("itemCode = " + itemCode);
		//log.info("warehouseCode = " + warehouseCode);
		//log.info("storageCode = " + storageCode);
		//log.info("storageInNo = " + storageInNo);
		//log.info("storageLotNo = " + storageLotNo);
		//log.info("head.getStorageTransactionType() = " + head.getStorageTransactionType());

		if(OUT.equals(head.getStorageTransactionType()))
		    quantity = quantity * -1;

		if(isReject)
		    quantity = quantity * -1;

		if(0 != quantity){
		    executeStorageOnhand(head.getStorageTransactionType(), head.getBrandCode(), itemCode, warehouseCode, 
			    storageCode, storageInNo, storageLotNo, quantity, head.getLastUpdatedBy(), allowMinus);

		}
	    }

	}catch(Exception ex){
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, ex.getMessage(), head.getLastUpdatedBy());
	    throw ex;
	}
    }

    /**
     * 執行扣除庫存
     */
    public void executeStorageOnhand(String storageTransactionType, String brandCode, String itemCode, 
	    String warehouseCode, String storageCode, String storageInNo, String storageLotNo, 
	    Double quantity, String lastUpdatedBy, boolean allowMinus ) throws Exception{

	int tryTimes = 3; // 嘗試連接次數
	int interval = 3000; // 等待時間
	for (int i = 1; i <= tryTimes; i++) { // 如果更新失敗，經過5秒後重試
	    try {	 
		if(IN.equals(storageTransactionType)){
		    imStorageOnHandDAO.updateInUncommitQuantity("TM", brandCode, 
			    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, lastUpdatedBy, allowMinus);
		}else if(MOVE.equals(storageTransactionType)){
		    imStorageOnHandDAO.updateMoveUncommitQuantity("TM", brandCode, 
			    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, lastUpdatedBy, allowMinus);
		}else if(OUT.equals(storageTransactionType)){
		    imStorageOnHandDAO.updateOutUncommitQuantity("TM", brandCode, 
			    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, lastUpdatedBy, allowMinus);
		}else if(ADJ.equals(storageTransactionType)){
		    imStorageOnHandDAO.updateOtherUncommitQuantity("TM", brandCode, 
			    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, lastUpdatedBy, allowMinus);
		}else if(BLOCK.equals(storageTransactionType)){
		    imStorageOnHandDAO.updateBlockUncommitQuantity("TM", brandCode, 
				    itemCode, warehouseCode, storageCode, storageInNo, storageLotNo, quantity, lastUpdatedBy, allowMinus);
		}else{
		    throw new Exception("儲位單未定義類別");
		}
		
		break;
		
	    }catch (Exception e) {
		try {
		    Thread.sleep(interval);
		} catch (InterruptedException ie) {
		    ie.printStackTrace();
		}
		if (i == tryTimes) {
		    throw e;
		}
	    }
	}
    }

    /**
     * 取得儲位單頭<br>
     * Object objHead 
     */
    public ImStorageHead getImStorageHead(Object objHead) throws Exception {
	String brandCode = (String)BeanUtil.getBeanValue(null, objHead, "brandCode");
	String sourceOrderTypeCode = (String)BeanUtil.getBeanValue(null, objHead, "orderTypeCode");
	String sourceOrderNo = (String)BeanUtil.getBeanValue(null, objHead, "orderNo");

	ImStorageHead imStorageHead  = (ImStorageHead)baseDAO.findFirstByProperty("ImStorageHead", "",
		" and brandCode = ? and sourceOrderTypeCode = ? and sourceOrderNo = ? ",
		new String[] {brandCode, sourceOrderTypeCode, sourceOrderNo} , "");
	return imStorageHead;
    }

    /**
     * 取得儲位正式單號，並更新來源單號<br>
     * Object objHead 
     */
    public ImStorageHead updateOrderNo(Object objHead) throws Exception {
	log.info("updateOrderNo");
	ImStorageHead imStorageHead = getImStorageHead(objHead);

	String sourceOrderTypeCode = (String)BeanUtil.getBeanValue(null, objHead, "orderTypeCode");
	String sourceOrderNo = (String)BeanUtil.getBeanValue(null, objHead, "orderNo");
	String lastUpdatedBy = (String)BeanUtil.getBeanValue(null, objHead, "lastUpdatedBy");

	log.info("sourceOrderTypeCode = " + sourceOrderTypeCode);
	log.info("sourceOrderNo = " + sourceOrderNo);

	if(null != imStorageHead){
	    imStorageHead.setSourceOrderNo(sourceOrderNo);
	    if(AjaxUtils.isTmpOrderNo(imStorageHead.getOrderNo())){
		String serialNo = buOrderTypeService.getOrderSerialNo(imStorageHead.getBrandCode(), imStorageHead.getOrderTypeCode());
		imStorageHead.setOrderNo(serialNo);
		log.info("serialNo = " + serialNo);
	    }
	    imStorageHead.setLastUpdatedBy(lastUpdatedBy);
	    imStorageHead.setLastUpdateDate(new Date());
	    return imStorageHead;
	}else{
	    throw new Exception("查無來源單別：" + sourceOrderTypeCode + "，來源單號：" + sourceOrderNo + "對應之儲位單");
	}
    }


    //---------------------------------------- ↓↓↓ OnHandSearch ↓↓↓ ----------------------------------------//


    /**
     * executeOnHandInitial
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeOnHandInitial(Map parameterMap) throws Exception {
	HashMap returnMap = new HashMap();
	Map multiList = new HashMap(0);
	try {
	    Object otherBean = parameterMap.get(AjaxUtils.VAT_BEAN_OTHER);
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    returnMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    List<ImWarehouse> allWarehouses = imWarehouseService.getWarehouseByWarehouseEmployee(loginBrandCode, loginEmployeeCode, null);
	    multiList.put("allWarehouses" , AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName", true, true ));

	    List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
	    multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode", "categoryName", true, true));

	    Map itemCategoryMap = imItemCategoryService.getItemCategoryRelatedList(loginBrandCode, null, null);
	    List<ImItemCategory> allCategory01 = (List<ImItemCategory>) itemCategoryMap.get("allCategory01");
	    List<ImItemCategory> allCategory02 = (List<ImItemCategory>) itemCategoryMap.get("allCategory02");
	    List<ImItemCategory> allCategory03 = (List<ImItemCategory>) itemCategoryMap.get("allCategory03");

	    multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
	    multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
	    multiList.put("allCategory03", AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));

	    List<ImItemCategory> allCategory07 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY07");
	    multiList.put("allCategory07", AjaxUtils.produceSelectorData(allCategory07, "categoryCode", "categoryName", false, true));

	    returnMap.put("loginBrandCode",loginBrandCode);
	    returnMap.put(AjaxUtils.AJAX_MULTILIST, multiList);
	    return returnMap;
	} catch (Exception ex) {
	    throw ex;
	}
    }

    /**
     * 取得儲位帳
     */
    public List<Properties> getAJAXOnHandPageData(Properties httpRequest)throws Exception {
	log.info("getAJAXOnHandPageData");
	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String startItemCode = httpRequest.getProperty("startItemCode"); //品號起
	    String endItemCode = httpRequest.getProperty("endItemCode"); //品號迄
	    String itemCodeList = httpRequest.getProperty("itemCodeList"); //品號串
	    String itemCName = httpRequest.getProperty("itemCName"); //品名
	    String warehouseCode = httpRequest.getProperty("warehouseCode");// 庫別
	    String showZero = httpRequest.getProperty("showZero"); //零庫存
	    String showMinus = httpRequest.getProperty("showMinus"); //負庫存
	    String itemCategory = httpRequest.getProperty("itemCategory"); //業種子類
	    String itemBrand = httpRequest.getProperty("itemBrand"); //商品品牌
	    String category01 = httpRequest.getProperty("category01"); //大類
	    String category02 = httpRequest.getProperty("category02"); //中類
	    String category03 = httpRequest.getProperty("category03"); //小類
	    String category04 = httpRequest.getProperty("category04"); //尺寸
	    String category07 = httpRequest.getProperty("category07"); //性別
	    String category09 = httpRequest.getProperty("category09"); //款式
	    String category13 = httpRequest.getProperty("category13"); //系列
	    String category17 = httpRequest.getProperty("category17"); //廠商
	    String isTax = httpRequest.getProperty("isTax"); //稅別
	    String storageCode = httpRequest.getProperty("storageCode");// 儲位
	    String storageInNo = httpRequest.getProperty("storageInNo");// 進貨日
	    String storageLotNo = httpRequest.getProperty("storageLotNo");// 批號效期

	    String [] itemCodeLists = itemCodeList.split(",");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.id.brandCode = :brandCode", brandCode);

	    if(StringUtils.hasText(itemCodeList))
		findObjs.put(" and model.id.itemCode in (:itemCodeList)", itemCodeLists);
	    else{
		if(StringUtils.hasText(startItemCode) && StringUtils.hasText(endItemCode)){
		    findObjs.put(" and model.id.itemCode <= :startItemCode", startItemCode);
		    findObjs.put(" and model.id.itemCode >= :endItemCode", endItemCode);
		}else if(StringUtils.hasText(startItemCode)){
		    findObjs.put(" and model.id.itemCode like :startItemCode", startItemCode);
		}else if(StringUtils.hasText(endItemCode)){
		    findObjs.put(" and model.id.itemCode like :endItemCode", endItemCode);
		}
	    }

	    findObjs.put(" and model.itemCName like :itemCName", "%"+itemCName+"%");
	    findObjs.put(" and model.id.warehouseCode = :warehouseCode", warehouseCode);

	    Double currentOnHandQty = 0D;
	    if ("N".equalsIgnoreCase(showZero) && "N".equalsIgnoreCase(showMinus)) // 只顯示大於零的庫存
		findObjs.put(" and model.currentOnHandQty > :currentOnHandQty", currentOnHandQty);
	    else if ("N".equalsIgnoreCase(showZero) && "Y".equalsIgnoreCase(showMinus)) // 只顯示負庫存
		findObjs.put(" and model.currentOnHandQty < :currentOnHandQty", currentOnHandQty);
	    else if ("Y".equalsIgnoreCase(showZero) && "N".equalsIgnoreCase(showMinus)) // 不顯示負庫存
		findObjs.put(" and model.currentOnHandQty >= :currentOnHandQty", currentOnHandQty);

	    findObjs.put(" and model.itemCategory = :itemCategory", itemCategory);
	    findObjs.put(" and model.itemBrand = :itemBrand", itemBrand);
	    findObjs.put(" and model.category01 = :category01", category01);
	    findObjs.put(" and model.category02 = :category02", category02);
	    findObjs.put(" and model.category03 = :category03", category03);
	    findObjs.put(" and model.category04 = :category04", category04);
	    findObjs.put(" and model.category07 = :category07", category07);
	    findObjs.put(" and model.category09 = :category09", category09);
	    findObjs.put(" and model.category13 = :category13", category13);
	    findObjs.put(" and model.category17 = :category17", category17);
	    findObjs.put(" and model.isTax = :isTax", isTax);

	    findObjs.put(" and model.id.storageCode = :storageCode", storageCode);
	    findObjs.put(" and model.id.storageInNo = :storageInNo", storageInNo);
	    findObjs.put(" and model.id.storageLotNo = :storageLotNo", storageLotNo);

	    //==============================================================	    
	    Map headMap = baseDAO.search( "ImStorageOnHandView as model", findObjs, 
		    " order by id.itemCode, id.warehouseCode, id.storageCode, id.storageInNo, id.storageLotNo desc ", 
		    iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );

	    List<ImStorageOnHandView> onHands = (List<ImStorageOnHandView>) headMap.get(BaseDAO.TABLE_LIST); 
	    if (onHands != null && onHands.size() > 0) {
		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		Long maxIndex = (Long)baseDAO.search("ImStorageOnHandView as model", "count(model.id.brandCode) as rowCount" ,findObjs, "", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_ON_HAND_FIELD_NAMES, GRID_ON_HAND_FIELD_DEFAULT_VALUES, onHands, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_ON_HAND_FIELD_NAMES, GRID_ON_HAND_FIELD_DEFAULT_VALUES, map, gridDatas));
	    }
	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的儲位單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的儲位單查詢失敗！");
	}		
    }

    /**
     * 更新儲位庫存查詢
     */
    public List<Properties> saveOnHandSearchResult(Properties httpRequest)throws Exception {
	String errorMsg = null;
	try {
	    AjaxUtils.updateSearchResult(httpRequest, GRID_ON_HAND_FIELD_NAMES);
	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新儲位帳庫存資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新儲位帳庫存資料失敗！");
	}   
    }

    /**
     * sql 匯出excel
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXOnHandExportData(HttpServletRequest httpRequest) throws Exception{
	log.info("getAJAXOnHandExportData");
	StringBuffer sql = new StringBuffer();
	Object[] object = null;
	List rowData = new ArrayList();
	try {

	    String brandCode = httpRequest.getParameter("loginBrandCode");// 品牌代號
	    String startItemCode = httpRequest.getParameter("startItemCode"); //品號起
	    String endItemCode = httpRequest.getParameter("endItemCode"); //品號迄
	    String itemCodeList = httpRequest.getParameter("itemCodeList"); //品號串
	    String itemCName = httpRequest.getParameter("itemCName"); //品名
	    String warehouseCode = httpRequest.getParameter("warehouseCode");// 庫別
	    String showZero = httpRequest.getParameter("showZero"); //零庫存
	    String showMinus = httpRequest.getParameter("showMinus"); //負庫存
	    String itemCategory = httpRequest.getParameter("itemCategory"); //業種子類
	    String itemBrand = httpRequest.getParameter("itemBrand"); //商品品牌
	    String category01 = httpRequest.getParameter("category01"); //大類
	    String category02 = httpRequest.getParameter("category02"); //中類
	    String category03 = httpRequest.getParameter("category03"); //小類
	    String category04 = httpRequest.getParameter("category04"); //尺寸
	    String category07 = httpRequest.getParameter("category07"); //性別
	    String category09 = httpRequest.getParameter("category09"); //款式
	    String category13 = httpRequest.getParameter("category13"); //系列
	    String category17 = httpRequest.getParameter("category17"); //廠商
	    String isTax = httpRequest.getParameter("isTax"); //稅別
	    String storageCode = httpRequest.getParameter("storageCode");// 儲位
	    String storageInNo = httpRequest.getParameter("storageInNo");// 進貨日
	    String storageLotNo = httpRequest.getParameter("storageLotNo");// 批號效期

	    if (null != itemCodeList && !"".equals(itemCodeList)) {
		itemCodeList = itemCodeList.replaceAll(",", "','"); // 前端傳回的資料僅用逗號分隔，資料庫查詢需加上單引號
		itemCodeList = "'" + itemCodeList + "'";
		System.out.println("=============> " + itemCodeList);
	    }

	    log.info("itemCodeList:"+itemCodeList+" startItemCode:"+startItemCode+" endItemCode:"+endItemCode);

	    sql.append(" SELECT A.ITEM_CODE, A.ITEM_C_NAME, A.WAREHOUSE_CODE, A.STORAGE_CODE, A.STORAGE_IN_NO, A.STORAGE_LOT_NO, ")
	    .append(" A.STOCK_ON_HAND_QTY + A.IN_UNCOMMIT_QTY - A.OUT_UNCOMMIT_QTY + A.MOVE_UNCOMMIT_QTY + A.OTHER_UNCOMMIT_QTY AS QTY,  A.BLOCK_QTY")
	    .append(" FROM ERP.IM_STORAGE_ON_HAND_VIEW A WHERE 1 = 1 ")
	    .append(" AND A.BRAND_CODE = '"+brandCode+"'");

	    if (StringUtils.hasText(itemCodeList)) { // 如果輸入多品號，則不用範圍查詢
		sql.append(" and A.ITEM_CODE in (" +itemCodeList + ")");
	    } else {
		if (StringUtils.hasText(startItemCode)) {
		    if (StringUtils.hasText(endItemCode)) {
			sql.append(" and A.ITEM_CODE >= '"+startItemCode+"' ");
			sql.append(" and A.ITEM_CODE <= '"+endItemCode+"' ");
		    } else {
			sql.append(" and A.ITEM_CODE like '"+startItemCode+"' ");
		    }
		} else if (StringUtils.hasText(endItemCode)) {
		    sql.append(" A.ITEM_CODE like '%"+endItemCode+"%' ");
		}
	    }

	    if (StringUtils.hasText(itemCName)) 
		sql.append(" AND ITEM_C_NAME like '%").append(itemCName).append("%' ");

	    if (StringUtils.hasText(warehouseCode)) 
		sql.append(" AND WAREHOUSE_CODE = '").append(warehouseCode).append("' ");

	    Double currentOnHandQty = 0D;
	    if ("N".equalsIgnoreCase(showZero) && "N".equalsIgnoreCase(showMinus)) // 只顯示大於零的庫存
		sql.append(" AND CURRENT_ON_HAND_QTY > ").append(currentOnHandQty);
	    else if ("N".equalsIgnoreCase(showZero) && "Y".equalsIgnoreCase(showMinus)) // 只顯示負庫存
		sql.append(" and model.currentOnHandQty < ").append(currentOnHandQty);
	    else if ("Y".equalsIgnoreCase(showZero) && "N".equalsIgnoreCase(showMinus)) // 不顯示負庫存
		sql.append(" and model.currentOnHandQty >= ").append(currentOnHandQty);

	    if (StringUtils.hasText(itemCategory)) 
		sql.append(" AND ITEM_CATEGORY = '").append(itemCategory).append("' ");

	    if (StringUtils.hasText(itemBrand)) 
		sql.append(" AND ITEM_BRAND = '").append(itemBrand).append("' ");

	    if (StringUtils.hasText(category01)) 
		sql.append(" AND CATEGORY01 = '").append(category01).append("' ");

	    if (StringUtils.hasText(category02)) 
		sql.append(" AND CATEGORY02 = '").append(category02).append("' ");

	    if (StringUtils.hasText(category03)) 
		sql.append(" AND CATEGORY03 = '").append(category03).append("' ");

	    if (StringUtils.hasText(category04)) 
		sql.append(" AND CATEGORY04 = '").append(category04).append("' ");

	    if (StringUtils.hasText(category07)) 
		sql.append(" AND CATEGORY07 = '").append(category07).append("' ");

	    if (StringUtils.hasText(category09)) 
		sql.append(" AND CATEGORY09 = '").append(category09).append("' ");

	    if (StringUtils.hasText(category13)) 
		sql.append(" AND CATEGORY13 = '").append(category13).append("' ");

	    if (StringUtils.hasText(category17)) 
		sql.append(" AND CATEGORY17 = '").append(category17).append("' ");

	    if (StringUtils.hasText(isTax)) 
		sql.append(" AND IS_TAX = '").append(isTax).append("' ");

	    if (StringUtils.hasText(storageCode)) 
		sql.append(" AND STORAGE_CODE = '").append(storageCode).append("' ");

	    if (StringUtils.hasText(storageInNo)) 
		sql.append(" AND STORAGE_IN_NO = '").append(storageInNo).append("' ");

	    if (StringUtils.hasText(storageLotNo)) 
		sql.append(" AND STORAGE_LOT_NO = '").append(storageLotNo).append("' ");

	    sql.append(" ORDER BY ITEM_CODE, WAREHOUSE_CODE, STORAGE_CODE, STORAGE_IN_NO, STORAGE_LOT_NO ");

	    log.info("sql = " + sql);

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());
	    object = new Object[] { 
		    "itemCode", "itemCName", "warehouseCode", "storageCode", 
		    "storageInNo", "storageLotNo" ,"currentOnHandQty", "blockQty"
	    };

	    log.info(" lists.size() = " + lists.size() );

	    for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
		Object[] getObj = (Object[])lists.get(i);
		Object[] dataObject = new Object[object.length];
		for (int j = 0; j < object.length; j++) {
		    dataObject[j] = getObj[j];
		}
		rowData.add(dataObject);
	    }
	    return new SelectDataInfo(object, rowData);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("匯出excel發生錯誤，原因：" + e.toString());
	    throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * 儲位調整匯入
     * @param 
     * @return
     * @throws Exception
     */
    public List<ImStorageHead> executeBatchImportT2(List<ImStorageHead> imStorageHeads) throws Exception {
	log.info("executeBatchImportT2");
	List<ImStorageHead> newImStorageHeads = new ArrayList<ImStorageHead>(0);
	try {
	    if (imStorageHeads != null) {
		for (int i = 0; i < imStorageHeads.size(); i++) {
		    ImStorageHead imStorageHead = (ImStorageHead) imStorageHeads.get(i);
		    log.info("insertImStorageHead");

		    imStorageHead.setOrderTypeCode("IAA");
		    imStorageHead.setStorageTransactionType(ADJ);

		    setOrderNo(imStorageHead);
		    imStorageHead.setStatus(OrderStatus.SAVE);

		    addImstorageItems(imStorageHead);

		    baseDAO.save(imStorageHead);
		    newImStorageHeads.add(imStorageHead);
		}
	    }
	} catch (Exception ex) {
	    log.error("執行儲位單批次匯入失敗，原因：" + ex.toString());
	    throw ex;
	}
	return newImStorageHeads;
    }

    /**
     * 儲位上架匯入
     * @param 
     * @return
     * @throws Exception
     */
    public List<ImStorageHead> executeBatchImportT2M(List<ImStorageHead> imStorageHeads) throws Exception {
	log.info("executeBatchImportT2M");
	List<ImStorageHead> newImStorageHeads = new ArrayList<ImStorageHead>(0);
	try {
	    if (imStorageHeads != null) {
		for (int i = 0; i < imStorageHeads.size(); i++) {
		    ImStorageHead imStorageHead = (ImStorageHead) imStorageHeads.get(i);
		    log.info("insertImStorageHead");

		    imStorageHead.setOrderTypeCode("IAM");
		    imStorageHead.setStorageTransactionType(MOVE);

		    setOrderNo(imStorageHead);
		    imStorageHead.setStatus(OrderStatus.SAVE);

		    log.info("find imReceiveHead");
		    ImReceiveHead imReceiveHead = (ImReceiveHead)baseDAO.findFirstByProperty("ImReceiveHead", "",
			    " and brandCode = ? and orderTypeCode = ? and orderNo = ? ",
			    new String[] {imStorageHead.getBrandCode(), 
			    imStorageHead.getSourceOrderTypeCode(), imStorageHead.getSourceOrderNo()} , "");

		    //設定儲位單的進貨批號
		    List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();
		    for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
			ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
			imStorageItem.setStorageInNo(DateUtils.format(imReceiveHead.getWarehouseInDate(), DateUtils.C_DATA_PATTON_YYYYMMDD));    
		    }

		    baseDAO.save(imStorageHead);
		    newImStorageHeads.add(imStorageHead);
		}
	    }
	} catch (Exception ex) {
	    log.error("執行儲位單批次匯入失敗，原因：" + ex.toString());
	    throw ex;
	}
	return newImStorageHeads;
    }


    public void addImstorageItems(ImStorageHead imStorageHead){
	log.info("addImstorageItems");

	HashMap addMap = new HashMap();
	HashMap minusMap = new HashMap();

	List<ImStorageItem> imStorageItems = imStorageHead.getImStorageItems();

	for (Iterator iterator = imStorageItems.iterator(); iterator.hasNext();) {
	    ImStorageItem imStorageItem = (ImStorageItem) iterator.next();
	    String itemCode = imStorageItem.getItemCode();
	    Double quantity = NumberUtils.getDouble(imStorageItem.getStorageQuantity());

	    if(quantity > 0){
		if(null != addMap.get(itemCode))
		    addMap.put(itemCode, (Double)addMap.get(itemCode) + quantity);
		else
		    addMap.put(itemCode, quantity);
	    }else if(quantity < 0){
		if(null != minusMap.get(itemCode))
		    minusMap.put(itemCode, (Double)minusMap.get(itemCode) + quantity);
		else
		    minusMap.put(itemCode, quantity);
	    }
	}


	Iterator addItems = addMap.keySet().iterator();
	while (addItems.hasNext()) {
	    String itemCode = (String) addItems.next();
	    Double addQuantity = 0D;

	    if(null != addMap.get(itemCode))
		addQuantity = (Double)addMap.get(itemCode);

	    Double minusQuantity = 0D;
	    if(null != minusMap.get(itemCode))
		minusQuantity = (Double)minusMap.get(itemCode);

	    if(addQuantity - minusQuantity > 0){
		ImStorageItem imStorageItem = new ImStorageItem();
		imStorageItem.setItemCode(itemCode);
		imStorageItem.setStorageQuantity( (addQuantity - minusQuantity) * -1 );
		imStorageItem.setArrivalWarehouseCode(imStorageHead.getArrivalWarehouseCode());
		imStorageItem.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
		imStorageItems.add(imStorageItem);
	    }
	}

    }



    /**
     * 流程起始
     *
     * @param form
     * @return
     * @throws Exception
     */
    public static Object[] startProcess(ImStorageHead form) throws Exception {
	log.info("startProcess");
	try {
	    String packageId = "Im_Storage";
	    String processId = "process";
	    String version = "20130205";
	    String sourceReferenceType = "ImStorage";

	    HashMap context = new HashMap();
	    //context.put("brandCode", form.getBrandCode());
	    context.put("formId", form.getStorageHeadId());
	    //context.put("orderNo", form.getOrderNo());
	    //context.put("orderType", form.getOrderTypeCode());
	    Object[] object = ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);

	    return object;
	} catch (Exception e) {
	    //e.printStackTrace();
	    log.error("儲位單流程執行時發生錯誤，原因：" + e.toString());
	    throw new ProcessFailedException(e.getMessage());
	}
    }

    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws Exception {
	try {
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("form", approveResult);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	} catch (Exception e) {
	    log.error("完成任務時發生錯誤：" + e.getMessage());
	    throw new Exception(e);
	}
    }

    /**
     * 執行反確認單據反轉
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public Object executeReverter(Long headId, String employeeCode) throws Exception {
	log.info("executeReverter headId = " + headId);

	ImStorageHead head = null;
	head = findById(headId);

	if (null == head) {
	    throw new Exception("查無此單: ");
	}

	BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(head.getBrandCode(), head.getOrderTypeCode()) );
	if(null == buOrderType)
	    throw new Exception("查無對應之單據類別"); 

	if(null == buOrderType.getMonthlyBalanceTypeCode()){
	    // status必為finish
	    if (!(OrderStatus.FINISH.equals(head.getStatus()))) {
		throw new Exception("未簽核完成, 不可反確.");
	    }

	    String identification = MessageStatus.getIdentification(head.getBrandCode(), head.getOrderTypeCode(), head.getOrderNo());
	    updateStorageOnHand(head, OrderStatus.SAVE, PROGRAM_ID, identification, true);

	    head.setStatus(OrderStatus.SAVE);
	    head.setCreatedBy(employeeCode);
	    head.setLastUpdateDate(new Date());
	    baseDAO.update(head);
	}else{
	    throw new Exception("此單據類別不可反確認"); 
	}
	return head;
    }

    /**
     * 執行反確認流程起始
     * 
     * @param parameterMap
     * @return List<Properties>
     */
    public void executeReverterProcess(Object bean) throws Exception {
	String status = org.apache.commons.beanutils.BeanUtils.getNestedProperty(bean, "status");
	//起流程
	if(OrderStatus.SAVE.equals(status))
	    ImStorageService.startProcess((ImStorageHead)bean);
    }

    /**
     * 判斷是否要執行儲位
     * @param Object bean
     * @return
     * @throws Exception
     */
    public boolean isStorageExecute(Object objHead) throws Exception{
	log.info("isStorageExecute");
	boolean isStorageExecute = false;
	String brandCode = (String)BeanUtil.getBeanValue(null, objHead, "brandCode");
	if("T2".equals(brandCode))
	    isStorageExecute = false;
	return isStorageExecute;
    }

}