package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.utils.NumberUtils;



import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuPurchaseService;






import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.PoEosDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;

import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImSysLog;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.EosHead;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemOnHandViewId;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
import tw.com.tm.erp.hbm.bean.BuPurchaseLineTmp;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;

public class PoEosService {

	// Log
	private static final Log log = LogFactory.getLog(PoEosService.class);
	
	private PoEosDAO poEosDAO;
	private BaseDAO baseDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	private BuBrandDAO buBrandDAO;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private BuPurchaseService buPurchaseService;
	private BuPurchaseHead buPurchaseHead;
	private BuPurchaseLine buPurchaseLine;
	private BuPurchaseHeadDAO buPurchaseHeadDAO;
	private BuPurchaseLineDAO buPurchaseLineDAO;
	private ImItemDAO imItemDAO;
	private BuShopDAO buShopDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	private ImItemCategoryService imItemCategoryService;
	private TmpAjaxSearchDataService tmpAjaxSearchDataService;
	private ImItemCurrentPriceView imItemCurrentPriceView;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	public void setImItemCurrentPriceView(ImItemCurrentPriceView imItemCurrentPriceView) {
		this.imItemCurrentPriceView = imItemCurrentPriceView;
	}
	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}
	
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setPoEosDAO(PoEosDAO poEosDAO) {
		this.poEosDAO = poEosDAO;
	}
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}
	public void setBuPurchaseService(BuPurchaseService buPurchaseService) {
		this.buPurchaseService = buPurchaseService;
	}
	
	public void setBuPurchaseLine(BuPurchaseLine buPurchaseLine) {
		this.buPurchaseLine = buPurchaseLine;
	}
	public void setBuPurchaseHead(BuPurchaseHead buPurchaseHead) {
		this.buPurchaseHead = buPurchaseHead;
	}
	public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
	}
	public void setBuPurchaseLineDAO(BuPurchaseLineDAO buPurchaseLineDAO) {
		this.buPurchaseLineDAO = buPurchaseLineDAO;
	}
	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}
	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	
	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService)
	{
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}
	public void setBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO)
	{
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO)
	{
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	
	/**
	 * 查詢picker用的欄位
	 * 
	 * 
	 * 	
	 */
	 public static final String[] GRID_SEARCH_FIELD_NAMES = {"headId","orderTypeCode", "orderNo","arrivalWarehouseCode","request","status" };

	 public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {"", "", "", "", "",
		 "", "", "", "", "", "" };
	 
	 
	 /*
	  * itemNo				:			品號	
	  * specInfo			:			預留欄位
	  * itemName			:			品名
	  * supplier			:			來源庫
	  * purTotalAmount		:			來源庫存
	  * quantity			:			需求量
	  * shopCode			:			店別
	  * reTotalAmount		:			店庫存
	  */
	 public static final String[] GRID_TEST_SEARCH_FIELD_NAMES = {
		 "lineId",				"itemNo",				"specInfo",				"itemName",				"supplier",
		 "purTotalAmount",		"totalQty",				"boxCapacity",			"quantity",				"box",
		 "unitPrice",			"shopCode",				"reTotalAmount",		"enable"};
	 
	 public static final String[] GRID_SAVE_FIELD_NAMES = {
		 "index",				"lineId",				"itemNo",				"specInfo",				"itemName",
		 "supplier",			"purTotalAmount",		"totalQty",				"boxCapacity",			"quantity",
		 "box","unitPrice",		 "shopCode",			"reTotalAmount",		"enable"};

	 public static final String[] GRID_TEST_FIELD_DEFAULT_VALUES = {
		 "",					"",						"",						"",						"",
		 "0",					"0",					"0",					"0",					"0",
		 "0",					"",						"0",					"N"};
	
	
	
	 public static final int[] GRID_TEST_FIELD_TYPES = {
		 AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		 AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,
		 AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG};
	
	
	 public static final String[] GRID_FIELD_SEARCH_NAMES = {"headId","orderTypeCode", "orderNo","department","request","status" };
	    public static final int[] GRID_FIELD_SEARCH_TYPES = {AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG };
	    public static final String[] GRID_FIELD_SEARCH_DEFAULT_VALUES = {"","","", "", "","",""};
	
	
		 public static final String[] GRID_FIELD_SEARCH_DETAIL_NAMES = {"orderTime","lineId","buPurchaseHead.orderNo", "itemNo","itemName","purTotalAmount","quantity","boxCapacity","box","itemCategory","enable" };
		    public static final int[] GRID_FIELD_SEARCH_DETAIL_TYPES = {AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };
		    public static final String[] GRID_FIELD_SEARCH_DETAIL_DEFAULT_VALUES = {"","","","","","","", "", "", "", ""};
	
	
	 // 頁面初始化
		public Map executeSearchDetailInitial(Map parameterMap) throws Exception{
			Map resultMap = new HashMap();
			Map multiList = new HashMap(0);
			Object otherBean     = parameterMap.get("vatBeanOther");
			String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
			String userType = (String)PropertyUtils.getProperty(otherBean, "userType");
			Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			Date toDay = new Date();
			Calendar cal = Calendar.getInstance();
	        cal.setTime(toDay);  
	        cal.add(Calendar.DAY_OF_MONTH, -7); 

			try{
				List<BuShop> allShops	= buShopDAO.findShopListWithOrder(brandCode, "Y", "2");
				List<ImItemCategory> allCategroyTypes = imItemCategoryDAO.findByCategoryType(brandCode, "CATEGORY00");
				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));
				multiList.put("allItemCategory", AjaxUtils.produceSelectorData(allCategroyTypes, "categoryCode", "categoryName", true, true));
				resultMap.put("multiList",multiList);
				resultMap.put("orderTypeCode", orderTypeCode);
				resultMap.put("warehouseCode", userType);
				//resultMap.put("enable","Y");
				//2016.10.24預設帶7天前的日期 MACO
				resultMap.put("startDate",cal.getTime());
				resultMap.put("enable","N");
				resultMap.put("endDate",toDay);
			}catch(Exception ex){
				ex.printStackTrace();
				log.error("需求初始化失敗，原因：" + ex.toString());         //修正錯誤訊息
				throw new Exception("需求單初始化失敗，原因：" + ex.toString());
			}
			return resultMap;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 頁面初始化
		public Map executeInitial(Map parameterMap) throws Exception{
			Map resultMap = new HashMap();
			Map multiList = new HashMap(0);
			Object otherBean     = parameterMap.get("vatBeanOther");
			String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
			Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			
			//String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			try{
				
				BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
//				List<ImWarehouse> allWarehouses	= baseDAO.findByProperty( "ImWarehouse"  , new String[] { "brandCode","enable" },new Object[] {"T2"	,"Y"});
//				multiList.put("allWarehouse", AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName",true, true));
				
				
				
				Map itemCategoryMap = imItemCategoryService.getItemCategoryRelatedList(brandCode, null, null);
		/*
				List<ImItemCategory> allCategory01 = (List<ImItemCategory>) itemCategoryMap.get("allCategory01");
				List<ImItemCategory> allCategory02 = (List<ImItemCategory>) itemCategoryMap.get("allCategory02");
				List<ImItemCategory> allCategory03 = (List<ImItemCategory>) itemCategoryMap.get("allCategory03");
		*/
				List<ImItemCategory> allCategory01 = imItemCategoryDAO.findCategoryByBrandCode(brandCode, "CATEGORY01", "Y");
				List<ImItemCategory> allCategory02 = imItemCategoryDAO.findCategoryByBrandCode(brandCode, "CATEGORY02", "Y");
				List<ImItemCategory> allCategory03 = imItemCategoryDAO.findCategoryByBrandCode(brandCode, "CATEGORY03", "Y");
		
				multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
				multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
				multiList.put("allCategory03", AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));
				
				List<ImItemCategory> allCategroyTypes = imItemCategoryDAO.findByCategoryType(brandCode, "ITEM_CATEGORY");
				multiList.put("allCategroyTypes", AjaxUtils.produceSelectorData(allCategroyTypes, "categoryCode", "categoryName", false, true));
				List<BuShop> allShops	= buShopDAO.findShopListWithOrder(brandCode, "Y", "2");
 				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));
				BuBrand     buBrand     = buBrandService.findById( brandCode );
				BuPurchaseHead puHead = null;
				if(formId != null){
					puHead = buPurchaseService.findById(formId);
					BuShop shop = buShopDAO.findById(puHead.getDepartment());
					resultMap.put("warehouseCode", shop.getSalesWarehouseCode());
					resultMap.put("category01",puHead.getCategorySystem() );
					resultMap.put("category02",puHead.getCategoryGroup() );
					resultMap.put("itemCategory",puHead.getCategoryItem() );
					resultMap.put("isTax",puHead.getWarehouseControl() );
					resultMap.put("itemBrand",puHead.getCategoryCode() );
				}else{
					puHead = buPurchaseService.createNewPoPurchaseHead(parameterMap, resultMap, buBrand, buOrderType );
					puHead.setRequestCode("");
					puHead.setRequest("");
					puHead.setDepartment("");
				}
				resultMap.put("form", puHead);
				
				//resultMap.put("employeeCode",      employeeCode);        //為什麼要塞??
				resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(puHead.getCreatedBy()));

				resultMap.put("multiList",         multiList);
				resultMap.put("statusName",OrderStatus.getChineseWord(puHead.getStatus()));
				resultMap.put("enable","Y");
			}catch(Exception ex){
				ex.printStackTrace();
				log.error("需求初始化失敗，原因：" + ex.toString());         //修正錯誤訊息
				throw new Exception("需求單初始化失敗，原因：" + ex.toString());
			}
			return resultMap;
		}

		public Map executeSearchInitial(Map parameterMap) throws Exception{
			Map resultMap = new HashMap();
			Map multiList = new HashMap(0);
			Object otherBean     = parameterMap.get("vatBeanOther");
			String brandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String userType      = (String)PropertyUtils.getProperty(otherBean, "userType");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			//String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			//String orderTypeCode = "EOS";

			try{
				List<BuShop> allShops	= buShopDAO.findShopListWithOrder(brandCode, "Y", "2");
 				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));
				BuBrand buBrand = buBrandService.findById(brandCode);
				if("warehouse".equals(userType)){
					resultMap.put("status","SIGNING");
					resultMap.put("userType",userType);
				}
				resultMap.put("orderTypeCode",orderTypeCode);
				resultMap.put("brandCode",buBrand.getBrandCode());
				resultMap.put("brandName",buBrand.getBrandName());
				resultMap.put("multiList",multiList);

				//resultMap.put("statusName",OrderStatus.getChineseWord(puHead.getStatus()));
				//resultMap.put("enable","Y");
			}catch(Exception ex){
				log.error("需求初始化失敗，原因：" + ex.toString());         //修正錯誤訊息
				throw new Exception("需求單初始化失敗，原因：" + ex.toString());
			}
			return resultMap;
		}	
	 
	 
	 
	 /**
	  * ajax 取得倉庫search分頁
	  * 
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
		/*
	 public List<Properties> getAJAXSearchPageData(Properties httpRequest)
	 throws Exception {
		 try {
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 // ======================帶入Head的值=========================

			 String warehouse 	 = httpRequest.getProperty("warehouseCode");
			 String itemCategory = httpRequest.getProperty("itemCategory");
			 String category01	 = httpRequest.getProperty("category01");
			 String category02	 = httpRequest.getProperty("category02");
			 String zore	 	 = "0";

			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
  
			 // ==============================================================
			 findObjs.put(" and model.id.warehouseCode = :warehouse", warehouse);
			 findObjs.put(" and model.itemCategory     = :itemCategory", itemCategory);
			 findObjs.put(" and model.category01	   = :category01", category01);
			 findObjs.put(" and model.category02	   = :category02", category02);
			 findObjs.put(" and model.currentOnHandQty > :zore", zore);
			 // ==============================================================



			 Map imItemOnHandViewMap = imItemOnHandViewDAO.search("ImItemOnHandView as model",findObjs, "order by model.id.warehouseCode asc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);

			 List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) imItemOnHandViewMap.get(BaseDAO.TABLE_LIST);
			 log.info(imItemOnHandViews.size());

			 if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {
				 //this.setOtherColumns(eos);
				 Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				 Long maxIndex = (Long) imItemOnHandViewDAO.search(
						 "ImItemOnHandView as model",
						 "count(model.id.warehouseCode) as rowCount", findObjs,
						 "order by model.id.warehouseCode desc",
						 BaseDAO.QUERY_RECORD_COUNT).get(
								 BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				 
				 result.add(AjaxUtils.getAJAXPageData(httpRequest,
						 GRID_SEARCH_FIELD_NAMES,
						 GRID_SEARCH_FIELD_DEFAULT_VALUES, imItemOnHandViews,
						 gridDatas, firstIndex, maxIndex));
			 } else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						 GRID_SEARCH_FIELD_NAMES,
						 GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
			 
		 } catch (Exception ex) {
			 log.error("載入頁面顯示的倉庫查詢發生錯誤，原因：" + ex.toString());
			 ex.printStackTrace();
			 throw new Exception("載入頁面顯示的倉庫查詢失敗！");
		 }
	 }
	 */
	 public List<Properties> saveSearchResult(Properties httpRequest)
	 throws Exception {
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }
	 
	 
	 
	    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
			 Map resultMap = new HashMap(0);
			 Map pickerResult = new HashMap(0);
			 try{
				 Object pickerBean = parameterMap.get("vatBeanPicker");
				 String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
				 ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
				 System.out.println("Search Key is : "+searchKeys.get(0));
				 List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
				 if(result.size() > 0)
					 pickerResult.put("result", result);
				 else{
					 Map messageMap = new HashMap();
					 messageMap.put("type"   , "ALERT");
					 messageMap.put("message", "請選擇檢視項目！");
					 messageMap.put("event1" , null);
					 messageMap.put("event2" , null);
					 resultMap.put("vatMessage",messageMap);
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
	 public Map saveSearchSelection(Map parameterMap) throws Exception{
			log.info("getSearchPoLSelection");
			Map resultMap = new HashMap(0);
			Map pickerResult = new HashMap(0);
			try{
			    Object pickerBean       = parameterMap.get("vatBeanPicker");
			    String timeScope        = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			    ArrayList searchKeys    = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			    Double exchangeRate = 1D; 
			    try{
			    	exchangeRate        = NumberUtils.getDouble((String)PropertyUtils.getProperty(pickerBean, "exchangeRate"));
			    	if(exchangeRate == 0D)
			    		exchangeRate = 1D;
			    }catch(Exception e){
			    	
			    }
			    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			    log.info(" selected size = " + result.size() );
			    if(result.size() > 0 )
			    {
			        pickerResult.put("result", result);
			        // 產生 FiInvoiceLine
			        Object otherBean = parameterMap.get("vatBeanOther");
			        String id       = (String)PropertyUtils.getProperty(otherBean, "headId");
			        String formName = (String)PropertyUtils.getProperty(otherBean, "formName");
			        Long headId = NumberUtils.getLong(id);
			        List <ImItemOnHandView> imViews = new ArrayList();
//			        for( int i=0; i < result.size(); i++){
//			            //log.info(NumberUtils.getLong( result.get(i).getProperty("headId")));
//			            Long imViewId = NumberUtils.getLong( result.get(i).getProperty("headId") ); 
//			            EosLine eosLine = this.findSaveById( imViewId );
//			            imViews.add(eosLine);
//			        }
//			        if( "FiInvoice".equals(formName)){
//			            savePoLine2InvoiceLine( headId, imViews );
//			        }else if( "ImReceive".equals(formName)){
//			            savePoLine2ReceiveItem( headId, imViews, exchangeRate );
//			            //imReceiveHeadMainService.savePoLine2ReceiveItem( headId, poLines );
//			        }
			    }
			    
			    resultMap.put("vatBeanPicker", pickerResult);
			    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
			    
			    return resultMap;
			}catch(Exception ex){
			    log.error("執行 PurchaseOrderLine 檢視失敗，原因：" + ex.toString());
			    throw new Exception("執行 PurchaseOrderLine 檢視失敗，原因：" + ex.getMessage());		
			}
		    }
	 public String findNewStatus(Long headId, String nowStatus, String formAction) throws Exception{

			List<Properties> result = new ArrayList();

			String newStatus = "";
			try {
				if(nowStatus.equals("SAVE"))
				{
					if(formAction.equals("SUBMIT"))
					{
						newStatus = "SIGNING";
					}
					if(formAction.equals("VOID"))
					{
						newStatus = "VOID";
					}
					if(formAction.equals("SAVE"))
					{
						newStatus = "SAVE";
					}
				}
				else if(nowStatus.equals("SIGNING"))
				{
					if(formAction.equals("SUBMIT"))
					{
						newStatus = "FINISH";
					}
				}
				else
				{
					log.error("訂貨單"+headId+"狀態變更失敗，原狀態：" + nowStatus + "、執行操作：" + formAction+ "時發生錯誤");
					throw new Exception("狀態轉換失敗！");
				}
				log.info("訂貨單"+headId+"狀態變更完成，原狀態:"+nowStatus+"、執行操作:"+formAction+"、新狀態:"+newStatus);


				return newStatus;	        
			} catch (Exception ex) {
				log.error("訂貨單"+headId+"狀態變更失敗，原狀態：" + nowStatus + "、執行操作：" + formAction+ "時發生錯誤，原因：" + ex.toString());
				throw new Exception("狀態轉換失敗！");
			}        
		}
/*		public List<Properties> findInfoByItemCode(Properties httpRequest) throws Exception{

			List<Properties> result = new ArrayList();
			Properties properties = new Properties();
			String brandCode = null;
			String itemCode = null;
			String shopCode = null;
			Long quantity = 0L;
			Long box = 0L;
			try {
				brandCode = httpRequest.getProperty("brandCode");
				quantity = Long.getLong(httpRequest.getProperty("quantity"));
				shopCode = httpRequest.getProperty("shopCode");
				itemCode = httpRequest.getProperty("itemNo");
				itemCode = itemCode.trim().toUpperCase();
				ImItem imitem = imItemDAO.findById(itemCode);
				if(imitem != null){

					
					String categoryType = imitem.getItemCategory();
					String isTax = imitem.getIsTax();
					String warehouseCode = "";
					
					if(categoryType.equals("T"))
					{
						warehouseCode ="D6200";
					}
					else
					{
						if(isTax.equals("F"))
						{
							warehouseCode = "F9900";
						}
						else
						{
							warehouseCode = "P9900";
						}
					}
					
					ImItemOnHandViewId id = new ImItemOnHandViewId();
					id.setBrandCode(brandCode);
					id.setItemCode(itemCode);
					id.setWarehouseCode(warehouseCode);
					id.setLotNo("000000000000");
					ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
					
					ImItemOnHandViewId id2 = new ImItemOnHandViewId();
					id2.setBrandCode(brandCode);
					id2.setItemCode(itemCode);
					id2.setWarehouseCode(shopCode);
					id2.setLotNo("000000000000");
					ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
					
					box = quantity/imitem.getBoxCapacity();
					
					
					properties.setProperty("itemNo", imitem.getItemCode());
					properties.setProperty("itemName", imitem.getItemCName());
					properties.setProperty("boxCapacity", imitem.getBoxCapacity().toString());
					properties.setProperty("purTotalAmount", item.getCurrentOnHandQty().toString());
					properties.setProperty("boxCapacity", imitem.getBoxCapacity().toString());
					properties.setProperty("quantity", quantity.toString());
					properties.setProperty("box", box.toString());
					properties.setProperty("shopCode", shopCode);
					properties.setProperty("reTotalAmount", item2.getCurrentOnHandQty().toString());
					
					
					

				}else{
					properties.setProperty("itemNo", imitem.getItemCode());
					properties.setProperty("itemName", "查無此品號");   
				}
				result.add(properties);	

				return result;	        
			} catch (Exception ex) {
				log.error("依據品牌代號：" + brandCode + "、工號：" + itemCode + "查詢員工資料時發生錯誤，原因：" + ex.toString());
				throw new Exception("查詢員工資料失敗！");
			}        
		}*/
	 

		public List<Properties> updatePickerData(Map parameterMap) throws Exception {
			log.info("updatePickerData....");
			Map returnMap = new HashMap(0);
			int startLineId = 0;
			Object otherBean = parameterMap.get("vatBeanOther");
			Object pickerBean = parameterMap.get("vatBeanPicker");
//頁面資訊
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String arrivalWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "arrivalWarehouseCode");
			Integer lineId = (Integer) PropertyUtils.getProperty(otherBean, "lineId");
			
			log.info("formIdString:" + formIdString);
			log.info("arrivalWarehouseCode:" + arrivalWarehouseCode);
			log.info("lineId:" + lineId);
			
//帶回來的結果
			List<Object> pickResults = (List<Object>) PropertyUtils.getProperty(pickerBean, "imOnHandResult");
			log.info("pickResult.size:" + pickResults.size());



			try {
				Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
				if (null != formId && null != pickResults && pickResults.size() > 0) 
				{
//取單頭
					BuPurchaseHead puHead =  buPurchaseService.findById(formId);

					if (null != puHead)
					{
//取單身
						List<BuPurchaseLine> items = puHead.getBuPurchaseLines();
						log.info("items.size:" + items.size());
						String itemCode = new String("");
						/*
						if (items.size() > 0 && lineId <= items.size())
						{
							log.info("lineId -1 <= items.size():" + (lineId <= items.size()));
							Object firstResult = pickResults.get(0);
							itemCode = (String) PropertyUtils.getProperty(firstResult, "id_itemCode");
							lotNo = (String) PropertyUtils.getProperty(firstResult, "id_lotNo");
							log.info("itemCode:" + itemCode + "/lotNo=" + lotNo);
							BuPurchaseLine item = items.get(lineId - 1);
							log.info("success get item data ");
							item.setItemNo(itemCode);

							startLineId = 1;
						}
						*/
						log.info("pickResults.size():" + pickResults.size());
						for (int i = 0 ; i < pickResults.size() ; i++)
						{
							Object pickResult = pickResults.get(i);
							itemCode = (String) PropertyUtils.getProperty(pickResult, "id_itemCode");
							boolean isRepeat=false;
							for(int j = 0 ; j < items.size() ; j++)
							{
								if(itemCode.equals(items.get(j).getItemNo()))
								{
									isRepeat=true;
								}
							}
							if(!isRepeat)
							{
								BuPurchaseLine newItem = new BuPurchaseLine();
								newItem.setItemNo(itemCode);
								newItem.setQuantity(0);
								items.add(newItem);
								log.info(itemCode+"已取得");
							}
							else
							{
								log.info(itemCode+"已在清單中");
							}
						}
						puHead.setBuPurchaseLines(items);
						log.info("items.size:" + items.size());
						buPurchaseHeadDAO.update(puHead);
					}
				}

				return AjaxUtils.parseReturnDataToJSON(returnMap);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception("發生錯誤，原因：" + ex.getMessage());
			} 
		}
		
/*		public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception
		{
			try
			{
				List<Properties> result = new ArrayList();
				List<Properties> gridDatas = new ArrayList();
				int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
				int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
				HashMap map = new HashMap();
				HashMap findObjs = new HashMap();
				// ======================帶入Head的值=============================
				String brandCode = httpRequest.getProperty("brandCode");
				String formIdString = httpRequest.getProperty("headId");
				String shopCode = httpRequest.getProperty("shopCode");
				if(null!=formIdString)
				{
					// ==============================================================
					log.info("formId="+formIdString);
					Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
					//List<BuShopMachine> buShopMachines = buShopMachineDAO.findByProperty("BuShopMachine", fieldNames, fieldValue);
					List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.findPageLine(formId, iSPage, iPSize);
					// ==============================================================
					log.info("BuPurchaseLine.size:" + buPurchaseLines.size());
					// "itemNo",			"specInfo",		"itemName",		"supplier",
					// "purTotalAmount",	"quantity",		"shopCode",		"reTotalAmount"};
					if (buPurchaseLines.size() > 0)
					{


						// 取得明細裏各人員姓名
						for (BuPurchaseLine buPurchaseLine : buPurchaseLines)
						{
							log.info(buPurchaseLine.getItemNo());
							String itemCode = buPurchaseLine.getItemNo();
							String warehouseCode = null;
							if(null!=itemCode)
							{
								List<ImItem> imItems = imItemDAO.findByProperty("itemCode", itemCode);
								ImItem imItem = imItems.get(0);
								String itemCategory = imItem.getItemCategory();
								Long boxCapacity = imItem.getBoxCapacity();
								Long box = buPurchaseLine.getQuantity()/boxCapacity;
								
								log.info("itemCategory"+itemCategory);
								if(itemCategory.equals("T")|| itemCategory.equals("F") || itemCategory.equals("B") || itemCategory.equals("D") || itemCategory.equals("K"))
								{
									warehouseCode = "D6200";
								}
								else
								{
									if(imItem.getIsTax().equals("F"))
									{
										warehouseCode = "F9900";
									}
									else
									{
										warehouseCode = "P9900";
									}
								}

								ImItemOnHandViewId id = new ImItemOnHandViewId();
								id.setBrandCode(brandCode);
								id.setItemCode(itemCode);
								id.setWarehouseCode(warehouseCode);
								id.setLotNo("000000000000");
								ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
								log.info(item.getItemCategory());
								//List<ImItemOnHandView> item = imItemOnHandViewDAO.findByProperty("ImItemOnHandView", "id.itemCode", itemCode);
//品名
								buPurchaseLine.setItemName(item.getItemCName());
//來源庫
								buPurchaseLine.setSupplier(item.getId().getWarehouseCode());
//來源庫存
								buPurchaseLine.setPurTotalAmount((int)(item.getCurrentOnHandQty()+0));
								ImItemOnHandViewId id2 = new ImItemOnHandViewId();
								id2.setBrandCode(brandCode);
								id2.setItemCode(itemCode);
								id2.setWarehouseCode(shopCode);
								id2.setLotNo("000000000000");
								ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
//店別
								buPurchaseLine.setShopCode(shopCode);
//店庫存
								buPurchaseLine.setReTotalAmount((int)(item2.getCurrentOnHandQty()+0));
//基本量
//箱數
								buPurchaseLine.setBox(box);
								buPurchaseLine.setBoxCapacity(boxCapacity);

								
							}
							else{
								log.info("NO DATA!");
							}
						}
						Long firstIndex =iSPage * iPSize + 1L; // 取得第一筆的INDEX
						Long maxIndex = buPurchaseLineDAO.findByProperty("BuPurchaseLine", "buPurchaseHead.headId", formId).size()+0L; // 取得最後一筆 INDEX
						log.info("firstIndex:"+firstIndex+"   maxIndex:"+maxIndex);
						//sult.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES,buShopEmployees, gridDatas, firstIndex, maxIndex));
						result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES,buPurchaseLines, gridDatas, firstIndex, maxIndex));

					} 
					else 
					{
						log.info("NO item");
						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES,GRID_TEST_FIELD_DEFAULT_VALUES, gridDatas));
					}
				}
				return result;
			} 
			catch (Exception ex) 
			{
				log.error("載入頁面顯示的倉庫查詢發生錯誤，原因：" + ex.toString());
				ex.printStackTrace();
				throw new Exception("載入頁面顯示的倉庫查詢失敗！");
			}

		}*/
		/*
		public List<Properties> updatePickerData(Map parameterMap) throws IllegalAccessException, InvocationTargetException,
		NoSuchMethodException {
			log.info("updatePickerData....");
			Map returnMap = new HashMap(0);
			int startLineId = 0;
			Object otherBean = parameterMap.get("vatBeanOther");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info("formIdString:" + formIdString);

			List<Object> pickResults = (List<Object>) PropertyUtils.getProperty(pickerBean, "imOnHandResult");
			log.info("pickResult.size:" + pickResults.size());

			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
//			Integer lineId = StringUtils.hasText(lineIdString)?
//			Integer.valueOf(lineIdString)-1:null;
			
			
			try{
				if ( null != pickResults && pickResults.size() > 0) 
				{
					for(int i =0;i<pickResults.size();i++)
					{
						String itemCode = new String("");
						Object firstResult = pickResults.get(i);
						itemCode = (String) PropertyUtils.getProperty(firstResult, "id_itemCode");
						log.info("itemCode:" + itemCode );
					}
				}
				return AjaxUtils.parseReturnDataToJSON(returnMap);
			} catch (IllegalAccessException iae) {
				System.out.println(iae.getMessage());
				throw new IllegalAccessException(iae.getMessage());
			} catch (InvocationTargetException ite) {
				System.out.println(ite.getMessage());
				throw new InvocationTargetException(ite, ite.getMessage());
			} catch (NoSuchMethodException nse) {
				System.out.println(nse.getMessage());
				throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
			}
		}
		*/
		
		/**
		 * 更新PAGE的LINE
		 *
		 * @param httpRequest
		 * @return List<Properties>
		 * @throws Exception
		 */
		public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
			log.info("updateAJAXPageLinesData....");
			try {
				//======================================取得單頭資訊==================================================
				String errorMsg = null;
				String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
				int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
				int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
				Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
				String shopCode = httpRequest.getProperty("shopCode");
				String status = httpRequest.getProperty("status");
				
				if (headId == null) {
					throw new ValidationErrorException("傳入的補貨單主鍵為空值！");
				}
				BuPurchaseHead buPurchaseHead = buPurchaseService.findById(headId);
				//===================================================================================================
				log.info("status:"+status);
				if (OrderStatus.SAVE.equals(status)||OrderStatus.SIGNING.equals(status)) {//狀態若為暫存狀態才會更新明細資訊
					
					List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,GRID_SAVE_FIELD_NAMES);//取得前端明細資訊
					int indexNo = buPurchaseLineDAO.findPageLineMaxIndex(headId).intValue();
					if (upRecords != null) {
						for (Properties upRecord : upRecords) {
							
							Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
							BuPurchaseLine buPurchaseLine = buPurchaseLineDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);//取得現有明細資訊
							String itemCode = upRecord.getProperty("itemNo");
							
							if(StringUtils.hasText(itemCode))
							{
								log.info("itemCode:"+itemCode);
								String sorceWarehouse = findSorceWarehouse("","","","",itemCode);
								int qty = Integer.parseInt(upRecord.getProperty("quantity"));

								if (StringUtils.hasText(itemCode)) {

									//更新明細資料
									if ( null != buPurchaseLine ) {
										log.info( "更新 = " + headId + " | "+ headId  );
										buPurchaseLine.setItemNo(itemCode);
										buPurchaseLine.setItemName(upRecord.getProperty("itemName"));
										buPurchaseLine.setShopCode(shopCode);
										buPurchaseLine.setSupplier(sorceWarehouse);
										buPurchaseLine.setQuantity(qty);
										buPurchaseLine.setEnable(upRecord.getProperty("enable"));
										buPurchaseLineDAO.update(buPurchaseLine);
									}else{
										
										//新增明細資料
										indexNo++;
										BuPurchaseLine line1 = new BuPurchaseLine(); 
										line1.setBuPurchaseHead(buPurchaseHead);
										line1.setIndexNo(Long.valueOf(indexNo));
										line1.setItemNo(itemCode);
										line1.setItemName(upRecord.getProperty("itemName"));
										line1.setShopCode(shopCode);
										line1.setSupplier(sorceWarehouse);
										line1.setQuantity(qty);
										line1.setEnable("N");
										//當使用者輸入需求量後才新增明細
										if(line1.getQuantity()>0)
										{
											buPurchaseLineDAO.save(line1);
										}
									}
								}
								else
								{
									log.info("無資料");
								}

							}
						}
					}
				}

				return AjaxUtils.getResponseMsg(errorMsg);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("更新調撥明細時發生錯誤，原因：" + ex.toString());
				throw new Exception("更新調撥明細失敗！");
			}
		}
		public List<Properties> updateAJAXDetailPageLinesData(Properties httpRequest) throws Exception {
			log.info("updateAJAXDetailPageLinesData....");
			try {
				String errorMsg = null;
				return AjaxUtils.getResponseMsg(errorMsg);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("更新調撥明細時發生錯誤，原因：" + ex.toString());
				throw new Exception("更新調撥明細失敗！");
			}
		}

		 public Map executeFindHead(Map parameterMap) throws FormException, Exception
		 {
			 Object otherBean = parameterMap.get("vatBeanOther");
			 Object formBean = parameterMap.get("vatBeanFormBind");
			 Map resultMap = new HashMap(0);
			 BuPurchaseHead buPurchaseHead = null;
			 try
			 {
				 
				 String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
				 String headIdString =(String)PropertyUtils.getProperty(formBean, "headId");
				 String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
				 BuBrand     buBrand     = buBrandService.findById( brandCode );
				 BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
				 Long headId = StringUtils.hasText(headIdString) ? Long.valueOf(headIdString) : null;
				 
				 log.info("送出時的unitId="+headId);
	//查詢頁面
				 if(StringUtils.hasText(headIdString))
				 {
					 log.info("Update");
					 buPurchaseHead=buPurchaseService.findById(headId);
				 }
	//新增頁面
				 else
				 {
					 log.info("Insert");
					 //buUnit=this.executeNewUnit(formBindBean);
					 buPurchaseHead=buPurchaseService.createNewPoPurchaseHead(parameterMap, resultMap, buBrand, buOrderType );
				 }
				 parameterMap.put( "entityBean", buPurchaseHead);
				 return parameterMap;
			 }
			 catch (Exception e)
			 {
				 log.error("取得實際單位主檔失敗,原因:"+e.toString());
				 throw new Exception("取得實際單位主檔失敗,原因:"+e.toString());
			 }
		 }
		 

			public void updateHeadBean(Map parameterMap) throws FormException, Exception
			{
				BuPurchaseHead buPurchaseHead = null;
				try
				{
					Object formBindBean = parameterMap.get("vatBeanFormBind");
					// Object formLinkBean = parameterMap.get("vatBeanFormLink");
					// Object otherBean = parameterMap.get("vatBeanOther");
					
					String shopCode = (String) PropertyUtils.getProperty(formBindBean, "department");
					
					
					buPurchaseHead = (BuPurchaseHead) parameterMap.get("entityBean");
					
					AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buPurchaseHead);


					
					List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.getItems(buPurchaseHead.getHeadId());
					List<BuPurchaseLine> deleteItem = new ArrayList();
					for(BuPurchaseLine line : buPurchaseLines){
						if(line.getQuantity()==0L){
							log.info(line.getItemNo() + "  "+line.getQuantity()+"   需求量為0，明細刪除!");
							deleteItem.add(line);
							
						}
						else{
							log.info(line.getItemNo() + "  "+line.getQuantity());
						}
					}

					buPurchaseLines.removeAll(deleteItem);

					Long index = 1L;
					for(BuPurchaseLine line : buPurchaseLines){
						line.setIndexNo(index);
						index++;
					}
					buPurchaseHead.setBuPurchaseLines(buPurchaseLines);
					buPurchaseHead.setDepartment(shopCode);
					buPurchaseHeadDAO.update(buPurchaseHead);
					parameterMap.put("entityBean", buPurchaseHead);
				}
				
				catch (Exception ex)
				{
						ex.printStackTrace();
						log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
						throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
				}
			}
			public Map executeIndexReset(Map parameterMap) throws Exception
			{
				MessageBox msgBox = new MessageBox();
				HashMap resultMap = new HashMap(0);
				String resultMsg = null;
				Date date = new Date();
				try
				{
					Object formBindBean = parameterMap.get("vatBeanFormBind");
					Object otherBean = parameterMap.get("vatBeanOther");
					String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
					BuPurchaseHead buPurchaseHead = (BuPurchaseHead) parameterMap.get("entityBean");
					List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.getItems(buPurchaseHead.getHeadId());
					for(int i=0;i<buPurchaseLines.size();i++){
						buPurchaseLines.get(i).setIndexNo(i+1L);
						buPurchaseLineDAO.update(buPurchaseLines.get(i));
					}


					resultMap.put("resultMsg", resultMsg);
					resultMap.put("entityBean", buPurchaseHead);
					resultMap.put("vatMessage", msgBox);

					return resultMap;

				}
				catch (Exception ex)
				{
					log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
					ex.printStackTrace();
					throw new Exception("維護單存檔失敗，原因：" + ex.toString());
				}
			}
			public Map executeShip(Map parameterMap) throws Exception
			{
				MessageBox msgBox = new MessageBox();
				HashMap resultMap = new HashMap(0);
				String resultMsg = null;
				Date date = new Date();
				try
				{
					Object formBindBean = parameterMap.get("vatBeanFormBind");
					Object otherBean = parameterMap.get("vatBeanOther");
					String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
					String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
					BuPurchaseHead buPurchaseHead = (BuPurchaseHead) parameterMap.get("entityBean");
					List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.getItems(buPurchaseHead.getHeadId());
					for(int i=0;i<buPurchaseLines.size();i++){
						buPurchaseLines.get(i).setIndexNo(i+1L);
						log.info(buPurchaseLines.get(i).getItemNo());
						buPurchaseLines.get(i).setEnable("Y");
						buPurchaseLineDAO.update(buPurchaseLines.get(i));
					}
					log.info("=============="+buPurchaseLines.size()+"===================");
					buPurchaseHead.setBuPurchaseLines(buPurchaseLines);
					this.findNewStatus(buPurchaseHead.getHeadId(),buPurchaseHead.getStatus(),formAction);
					buPurchaseHeadDAO.merge(buPurchaseHead);

					log.info("訂貨單"+buPurchaseHead.getOrderTypeCode()+"-"+buPurchaseHead.getOrderNo()+"已完成出貨！");

					resultMsg = "訂貨單"+buPurchaseHead.getOrderTypeCode()+"-"+buPurchaseHead.getOrderNo()+"已完成出貨！ 是否繼續？";

					resultMap.put("resultMsg", resultMsg);
					resultMap.put("entityBean", buPurchaseHead);
					resultMap.put("vatMessage", msgBox);

					return resultMap;

				}
				catch (Exception ex)
				{
					log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
					ex.printStackTrace();
					throw new Exception("維護單存檔失敗，原因：" + ex.toString());
				}
			}
			public Map updateAJAX(Map parameterMap) throws Exception
			{
				MessageBox msgBox = new MessageBox();
				HashMap resultMap = new HashMap(0);
				String resultMsg = null;
				Date date = new Date();
				try
				{
					Object formBindBean = parameterMap.get("vatBeanFormBind");
					//	 Object formLinkBean = parameterMap.get("vatBeanFormLink");
					Object otherBean = parameterMap.get("vatBeanOther");
					String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
					BuPurchaseHead buPurchaseHead = (BuPurchaseHead) parameterMap.get("entityBean");
					//String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
					String formIdString = (String) PropertyUtils.getProperty(formBindBean, "headId");
					String category01 = (String) PropertyUtils.getProperty(formBindBean, "category01");
					String category02 = (String) PropertyUtils.getProperty(formBindBean, "category02");
					String itemCategory = (String) PropertyUtils.getProperty(formBindBean, "itemCategory");
					String categoryCode = (String) PropertyUtils.getProperty(formBindBean, "itemBrand");
					String isTax = (String) PropertyUtils.getProperty(formBindBean, "isTax");

					String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
					List<BuPurchaseLine> buPurchaseLine =buPurchaseLineDAO.getItems(buPurchaseHead.getHeadId());
					if(buPurchaseLine.size()==0)
					{
						throw new Exception("此單尚未選取任何商品，無法送出");
					}
					buPurchaseHead = saveActualOrderNo(buPurchaseHead,loginEmployeeCode);
					buPurchaseHead.setStatus(this.findNewStatus(buPurchaseHead.getHeadId(),buPurchaseHead.getStatus(),formAction));
		//存檔
					if (OrderStatus.FORM_SUBMIT.equals(formAction))
					{
						


						
				    	if(!StringUtils.hasText(formIdString) )
				    	{

				    		buPurchaseHead.setLastUpdateDate(date);
				    		buPurchaseHead.setLastUpdatedBy(loginEmployeeCode);
				    		buPurchaseHead.setCategorySystem(category01);
				    		buPurchaseHead.setCategoryGroup(category02);
				    		buPurchaseHead.setCategoryItem(itemCategory);
				    		buPurchaseHead.setWarehouseControl(isTax);
				    		buPurchaseHead.setCreatedBy(loginEmployeeCode);
				    		buPurchaseHead.setCreationDate(date);
				    		buPurchaseHead.setCategoryCode(categoryCode);
				    		buPurchaseHeadDAO.save(buPurchaseHead);
				    	}
				    	else
				    	{
				    		buPurchaseHead.setCategorySystem(category01);
				    		buPurchaseHead.setCategoryGroup(category02);
				    		buPurchaseHead.setCategoryItem(itemCategory);
				    		buPurchaseHead.setWarehouseControl(isTax);
				    		buPurchaseHead.setLastUpdateDate(date);
				    		buPurchaseHead.setCategoryCode(categoryCode);
							buPurchaseHead.setLastUpdatedBy(loginEmployeeCode);
							buPurchaseHeadDAO.merge(buPurchaseHead);
				    	}
						//log.info(buPurchaseHead.getUnitId().toString());
						log.info("訂貨單"+buPurchaseHead.getOrderTypeCode()+"-"+buPurchaseHead.getOrderNo()+"存檔成功！");
					}
					resultMsg = "訂貨單"+buPurchaseHead.getOrderTypeCode()+"-"+buPurchaseHead.getOrderNo()+"存檔成功！ 是否繼續新增？";

					resultMap.put("resultMsg", resultMsg);
					resultMap.put("entityBean", buPurchaseHead);
					resultMap.put("vatMessage", msgBox);

					return resultMap;

				}
				catch (Exception ex)
				{
					log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
					ex.printStackTrace();
					throw new Exception("維護單存檔失敗，原因：" + ex.toString());
				}
			}
			
			
		public BuPurchaseHead saveActualOrderNo(BuPurchaseHead buPurchaseHead, String loginUser) throws ObtainSerialNoFailedException,
		FormException, Exception {

			if (buPurchaseHead == null)
			{
				throw new NoSuchObjectException("查無調撥單主鍵：" + buPurchaseHead.getHeadId() + "的資料！");
			} else { // 取得正式的單號

				this.setOrderNo(buPurchaseHead);
				buPurchaseHead.setLastUpdatedBy(loginUser);
				buPurchaseHead.setLastUpdateDate(new Date());
				buPurchaseHeadDAO.merge(buPurchaseHead);
			}
			return buPurchaseHead;
		}
	
	private void setOrderNo(BuPurchaseHead head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		log.info("3.setOrderNo...original_order=" + orderNo);
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode()
							+ "單號失敗！");
				else {
					head.setOrderNo(serialNo);
					log.info("the order no. is " + serialNo);
				}
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	
	public List<Properties> findWarehouseByShop(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String shopCode =  httpRequest.getProperty("department");
//用UnitCode找UnitId
			log.info("shopCode = " + shopCode);

		    List<BuShop> Shops = buShopDAO.findByProperty("BuShop", "shopCode", shopCode);
		    log.info("BuShop.size()="+Shops.size());
		    String warehouseCode=null;
		    if(Shops.size() == 0)
		    {
		    	log.info("無資料");
		    }
		    else
		    {
		    	log.info("有資料");
		    	warehouseCode = Shops.get(0).getSalesWarehouseCode();
//用UnitId與OrgSNo找rsSNo
		    	log.info("warehouseCode="+warehouseCode);
		    }
			String no =warehouseCode.toString()+"的商品需求清單";
		    properties.setProperty("no", no);
			properties.setProperty("warehouseCode", warehouseCode.toString());
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
			throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
		}
		return list;
	}
	 public void validate(Map parameterMap) throws Exception {

		 List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseHeadDAO.findEnableLineById("EosOrderLock");
		if(buCommonPhraseLines.size()>0){

			for(BuCommonPhraseLine buCommonPhraseLine:buCommonPhraseLines){

				if(buCommonPhraseLine.getAttribute1().equals("Y")){
					throw new Exception("倉庫正在出貨作業，請稍後再嘗試送出");
				}
			}
		}

		 //Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 //Object otherBean = parameterMap.get("vatBeanOther");

		 //String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//		 Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		 //String countryCode = (String) PropertyUtils.getProperty(formBindBean, "countryCode");

	 }
	
	 public List<Properties> getAJAXSearchDetailPageData(Properties httpRequest) throws Exception{

		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 //String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			 String orderTypeCode = "EOS";
			 String shopCode = httpRequest.getProperty("shopCode");
			 String orderNo = httpRequest.getProperty("orderNo");
			 String warehouseCode = httpRequest.getProperty("warehouseCode");
			 String status = httpRequest.getProperty("status");
			 String brandCode = httpRequest.getProperty("brandCode");  
			 String startDate = httpRequest.getProperty("startDate");
			 String endDate = httpRequest.getProperty("endDate");
			 String categoryItem = httpRequest.getProperty("categoryItem");
			 String enable = httpRequest.getProperty("enable");
			 String isTax = httpRequest.getProperty("isTax");
			 Date startDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate);
			 Date endDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate);
			 String warehouseCodeSearch = warehouseCodeSwitch(warehouseCode,isTax);
			 log.info("orderTypeCode:"+orderTypeCode);
			 log.info("enable:"+enable);
			 log.info("orderNo:"+orderNo);
			 log.info("status:"+status);
			 log.info("brandCode:"+brandCode);
			 log.info("shopCode:"+shopCode);
			 log.info("warehouseCode:"+warehouseCode);
			 log.info("startDate:"+startDate);
			 log.info("endDate:"+endDate);
			 log.info("categoryItem:"+categoryItem);
			 HashMap map = new HashMap();
			 HashMap findMap = new HashMap();
			 findMap.put("orderNo",orderNo);
			 findMap.put("enable",enable);
			 findMap.put("orderTypeCode",orderTypeCode);
			 findMap.put("status",status);
			 findMap.put("brandCode",brandCode);
			 findMap.put("shopCode",shopCode);
			 findMap.put("warehouseCode",warehouseCodeSearch);
			 findMap.put("startDate",startDate);
			 findMap.put("endDate",endDate);
			 findMap.put("categoryItem",categoryItem);
			 //==============================================================	
			 List<BuPurchaseLine> buPurchaseLine = buPurchaseLineDAO.findEosRequestDetail(findMap,iSPage,iPSize);
			 log.info("buPurchaseLine.size"+ buPurchaseLine.size());	
			 if (buPurchaseLine != null && buPurchaseLine.size() > 0) {
				 for(int i=0;i<buPurchaseLine.size();i++)
				 {
					 
					 ImItemOnHandViewId id = new ImItemOnHandViewId();
						id.setBrandCode(brandCode);
						id.setItemCode(buPurchaseLine.get(i).getItemNo());
						id.setWarehouseCode(buPurchaseLine.get(i).getSupplier());
						id.setLotNo("000000000000");
						ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
					 log.info(i+".HEAD_ID:"+buPurchaseLine.get(i).getBuPurchaseHead().getHeadId()+"/LINE_ID:"+buPurchaseLine.get(i).getLineId()+"/ITEM_CODE:"+buPurchaseLine.get(i).getItemNo()  );
					 buPurchaseLine.get(i).setPurTotalAmount(item.getCurrentOnHandQty().intValue());
					 //2016.10.26 MACO 明細資訊新增業種
					 try{
						 String itemCateogry = imItemCategoryDAO.findByCategoryCode(brandCode, "ITEM_CATEGORY", buPurchaseLine.get(i).getBuPurchaseHead().getCategoryItem(), "Y").getParentCategoryCode();
						 buPurchaseLine.get(i).setItemCategory(imItemCategoryDAO.findByCategoryCode(brandCode, "CATEGORY00", itemCateogry, "Y").getCategoryName());
						 Date orderTime = buPurchaseLine.get(i).getBuPurchaseHead().getLastUpdateDate();
						 buPurchaseLine.get(i).setOrderTime(DateUtils.format(orderTime, DateUtils.C_TIME_PATTON_SLASH));
					 }
					 catch(Exception e){
						log.error("取得商品業種名稱失敗");
						throw new Exception("取得商品業種名稱失敗");
					 }
					// buPurchaseLine.get(i).setPurTotalAmount(10);
					 //2016.10.21 MACO 若是保稅倉出貨不看基本量(基本量=1)
					 if(buPurchaseLine.get(i).getSupplier().equals("F9900")||buPurchaseLine.get(i).getSupplier().equals("P9900")){
						 buPurchaseLine.get(i).setBoxCapacity(1L);//基本量
					 }
					 else{
						 buPurchaseLine.get(i).setBoxCapacity(item.getBoxCapacity());
					 }
					 
					 buPurchaseLine.get(i).setBox( ((int)Math.ceil(buPurchaseLine.get(i).getQuantity()/ buPurchaseLine.get(i).getBoxCapacity()))+0L);

				 }
				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				// Long maxIndex = (Long)buPurchaseHeadDAO.search("BuPurchaseHead as model,BuPurchaseLine as model1", "count(model.orderTypeCode) as rowCount" ,findObjs,iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				 Long maxIndex = buPurchaseLineDAO.findEosRequestDetailMaxIndex(findMap);
				 log.info("firstIndex:"+firstIndex+"/maxIndex:"+maxIndex);
				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_SEARCH_DETAIL_NAMES, GRID_FIELD_SEARCH_DETAIL_DEFAULT_VALUES, buPurchaseLine, gridDatas, firstIndex, maxIndex));
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_SEARCH_DETAIL_NAMES, GRID_FIELD_SEARCH_DETAIL_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的單位功能查詢發生錯誤，原因：" + ex.toString());
			 ex.printStackTrace();
			 throw new Exception("載入頁面顯示的單位功能");
		 }	
	 }
		public static String warehouseCodeSwitch(String warehouseCode,String taxType)throws Exception{
			if("9900".equals(warehouseCode))
			{
				if("F".equals(taxType)){
					warehouseCode = "'F9900'";
				}
				else if("P".equals(taxType)){
					warehouseCode = "'P9900'";
				}
				else{
					warehouseCode = "'P9900','F9900'";
				}
			}
			else
			{
				warehouseCode = "'D6200'";
			}
			return warehouseCode;
		}
		public List<Properties> updateStatus(Properties httpRequest)throws Exception{
			List list = new ArrayList();
			Properties properties = new Properties();
			try{
				
				String brandCode = httpRequest.getProperty("brandCode");
				log.info("brandCode = " + brandCode);
				String orderTypeCode = httpRequest.getProperty("orderTypeCode");
				log.info("orderTypeCode = " + orderTypeCode);
				String orderNo = httpRequest.getProperty("orderNo");
				log.info("orderNo = " + orderNo);
				String itemCode = httpRequest.getProperty("itemCode");
				log.info("itemCode = " + itemCode);
				String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
				log.info("loginEmployeeCode = " + loginEmployeeCode);
				Long lineId =  NumberUtils.getLong(httpRequest.getProperty("lineId"));
				log.info("lineId = " + lineId);
				String enable = httpRequest.getProperty("enable");
				log.info("enable = " + enable);
				Date today = new Date();
			    BuPurchaseLine line = buPurchaseLineDAO.findById(lineId);

		
				if(line != null)
			    {
			    	log.info("有資料");
			    	line.setEnable(enable);
			    	line.setLastUpdateDate(today);
			    	line.setLastUpdatedBy(loginEmployeeCode);
			    	log.info(line.getItemNo()+"已出貨");
			    	buPurchaseLineDAO.update(line);
			    	List<BuPurchaseHead> head = buPurchaseHeadDAO.findByOrderNo(brandCode, orderTypeCode, orderNo, "", "");
			    	List<BuPurchaseLine> lineList  = buPurchaseLineDAO.findLinebyStatus(head.get(0).getHeadId(), "Y");
			    	if(lineList.size()==0)
			    	{
			    		log.info("明細已全數送出，修改單頭狀態為完成");
			    		head.get(0).setStatus("FINISH");
			    		buPurchaseHeadDAO.update(head.get(0));
			    	}
			    }
				else
				{
					log.info("執行錯誤，無資料");
				}
				//properties.setProperty("statusByAC", statusByAC);
				//properties.setProperty("statusByED", statusByED);
				//properties.setProperty("statusByBC", statusByBC);
				list.add(properties);
			}
			catch(Exception e)
			{
				log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
				e.printStackTrace();
				throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
			}
			
			return list;
		}
	
	
		public List<Properties> updateAllDetailStatus(Properties httpRequest)throws Exception{
			List list = new ArrayList();
			Properties properties = new Properties();
			try{
				
				 String orderTypeCode = "EOS";
				 String shopCode = httpRequest.getProperty("shopCode");
				 String warehouseCode = httpRequest.getProperty("warehouseCode");
				 String orderNo = httpRequest.getProperty("orderNo");
				 String status = httpRequest.getProperty("status");
				 String brandCode = httpRequest.getProperty("brandCode");  
				 String startDate = httpRequest.getProperty("startDate");
				 String endDate = httpRequest.getProperty("endDate");
				 String categoryItem = httpRequest.getProperty("categoryItem");
				 String isTax = httpRequest.getProperty("isTax");
				 Date startDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate);
				 Date endDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate);
				 String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
				 String enable = "Y";
				 String warehouseCodeWithoutTax = warehouseCode;
				 warehouseCode = warehouseCodeSwitch(warehouseCode,isTax);
				 Date today = new Date();
				 HashMap map = new HashMap();
				 HashMap findMap = new HashMap();
				 findMap.put("orderNo",orderNo);
				 findMap.put("orderTypeCode",orderTypeCode);
				 findMap.put("status",status);
				 findMap.put("brandCode",brandCode);
				 findMap.put("shopCode",shopCode);
				 findMap.put("warehouseCode",warehouseCode);
				 findMap.put("startDate",startDate);
				 findMap.put("endDate",endDate);
				 findMap.put("categoryItem",categoryItem);
				List<BuPurchaseLine> buPurchaseLine = buPurchaseLineDAO.findEosRequestDetailAll(findMap);

				for(BuPurchaseLine line:buPurchaseLine)
				{
					if(line != null)
				    {
				    	log.info("有資料");
				    	line.setEnable(enable);
				    	line.setLastUpdateDate(today);
				    	line.setLastUpdatedBy(loginEmployeeCode);
				    	log.info(line.getItemNo()+"已出貨");
				    	buPurchaseLineDAO.update(line);
				    	BuPurchaseHead head = line.getBuPurchaseHead();
				    	List<BuPurchaseLine> lineList  = buPurchaseLineDAO.findLinebyStatus(head.getHeadId(), "Y");
				    	if(lineList.size()==0)
				    	{
				    		log.info(head.getOrderTypeCode()+head.getOrderNo()+"明細已全數送出，修改單頭狀態為完成");
				    		head.setStatus("FINISH");
				    		buPurchaseHeadDAO.update(head);
				    	}
				    }
					else
					{
						log.info("執行錯誤，無資料");
					}
					//properties.setProperty("statusByAC", statusByAC);
					//properties.setProperty("statusByED", statusByED);

				}

				BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("EosOrderLock","lock"+warehouseCodeWithoutTax);

				if(null != buCommonPhraseLine){
							Date date = new Date();
							buCommonPhraseLine.setAttribute1("N");
							
							buCommonPhraseLine.setLastUpdateDate(date);
							buCommonPhraseLine.setLastUpdatedBy("SYSTEM");
							buCommonPhraseLineDAO.update(buCommonPhraseLine);
							log.info("明細送出動作已完成，設定解除單據送出鎖定狀態");
				}
				else{
					log.info("錯誤"+warehouseCodeWithoutTax);
				}
				properties.setProperty("lock"+warehouseCodeWithoutTax, "N");
				list.add(properties);
			}
			catch(Exception e)
			{
				log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
				e.printStackTrace();
				throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
			}
			
			return list;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			 String startOrderNo = httpRequest.getProperty("startOrderNo");
			 String endOrderNo = httpRequest.getProperty("endOrderNo");
			 String status = httpRequest.getProperty("status");
			 String brandCode = httpRequest.getProperty("brandCode");  
			 String shopCode = httpRequest.getProperty("shopCode");
			 String userType = httpRequest.getProperty("userType1");
			 String startDate = httpRequest.getProperty("startDate");
			 String endDate = httpRequest.getProperty("endDate");
			 Date startDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate);
			 Date endDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate);
			 
			 log.info("orderTypeCode:"+orderTypeCode);
			 log.info("startOrderNo:"+startOrderNo);
			 log.info("endOrderNo:"+endOrderNo);
			 log.info("status:"+status);
			 log.info("brandCode:"+brandCode);
			 log.info("userType1:"+userType);
			 log.info("shopCode:"+shopCode);
			 log.info("startDate:"+startDate);
			 log.info("endDate:"+endDate);

			 
			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
			 findObjs.put(" and model.brandCode  = :brandCode", brandCode);
			    findObjs.put(" and model.orderTypeCode  = :orderTypeCode", orderTypeCode);
			    findObjs.put(" and model.orderNo NOT LIKE :TMP", "TMP%");
			    findObjs.put(" and model.orderNo  >= :startOrderNo", startOrderNo);
			    findObjs.put(" and model.orderNo  <= :endOrderNo", endOrderNo);
			    findObjs.put(" and model.status  = :status", status);
			    findObjs.put(" and model.department  = :shopCode", shopCode);
			    if("warehouse".equals(userType)){
			    	log.info("內倉出貨");
			    	String categorySystem = "08";
			    	String categoryGroup = "A02";
			    	findObjs.put(" and model.categorySystem  = :categorySystem",categorySystem );
			    	findObjs.put(" and model.categoryGroup  != :categoryGroup",categoryGroup );
			    }
			    findObjs.put(" and model.requestDate  >= :startDate", startDateDate);
			    findObjs.put(" and model.requestDate  <= :endDate", endDateDate);
			    //findObjs.put(" and s.salesOrderDate  >= :salesOrderStartDate",salesOrderStartDate);
			    //findObjs.put(" and s.salesOrderDate  <= :salesOrderEndDate",salesOrderEndDate);
			 //==============================================================	    

			 Map unitMap = buPurchaseHeadDAO.search( "BuPurchaseHead as model", findObjs,"order by orderNo desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			 List<BuPurchaseHead> buPurchaseHead = (List<BuPurchaseHead>) unitMap.get(BaseDAO.TABLE_LIST); 

			 log.info("buPurchaseHead.size"+ buPurchaseHead.size());	
			 if (buPurchaseHead != null && buPurchaseHead.size() > 0) {

				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buPurchaseHeadDAO.search("BuPurchaseHead as model", "count(model.orderTypeCode) as rowCount" ,findObjs,iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_SEARCH_NAMES, GRID_FIELD_SEARCH_DEFAULT_VALUES, buPurchaseHead, gridDatas, firstIndex, maxIndex));
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_SEARCH_NAMES, GRID_FIELD_SEARCH_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的單位功能查詢發生錯誤，原因：" + ex.toString());
			 ex.printStackTrace();
			 throw new Exception("載入頁面顯示的單位功能");
		 }	
	 }
	 public List<Properties> getSearchEosSelection(Map parameterMap)
		throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean,
				AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
				pickerBean, AjaxUtils.SEARCH_KEY);

		List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
				searchKeys);
		System.out.println("size:::" + result.size());
		if (result.size() > 0) {
			pickerResult.put("result", result);
		}
		resultMap.put("vatBeanPicker", pickerResult);
		resultMap.put("topLevel", new String[] { "vatBeanPicker" });
	} catch (Exception ex) {
		log.error("檢視失敗，原因：" + ex.toString());
		Map messageMap = new HashMap();
		messageMap.put("type", "ALERT");
		messageMap.put("message", "檢視失敗，原因：" + ex.toString());
		messageMap.put("event1", null);
		messageMap.put("event2", null);
		resultMap.put("vatMessage", messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
}
	

/*	    	public SelectDataInfo exportExcelDetail(HttpServletRequest httpRequest) throws Exception {
	    		try {

	    			Long headId = NumberUtils.getLong(httpRequest.getParameter("headId")); // 要顯示的HEAD_ID
	    			String brandCode = httpRequest.getParameter("brandCode"); // 品牌代號
	    			String exportBeanName = httpRequest.getParameter("exportBeanName"); // 調撥單種類
	    			// 可用庫存excel表的欄位順序
	    			Object[] object = null;


	    				 object = new Object[] {
		    					 "index",
		    					 "orderTypeCode",
		    					 "orderNo",
		    					 "itemNo",
		    					 "itemName",
		    					 "category01",
		    					 "category02",
		    					 "isTax",
		    					 "supplier",
		    					 "purTotalAmount",
		    					 "boxCapacity",
		    					 "quantity",
		    					 "box",
		    					 "shopCode",
		    					 "reTotalAmount"};
	    	//		else
	    		//		object = new Object[] { "indexNo", "boxNo", "itemCode", "itemName", "unitPrice", "deliveryWarehouseCode",
	    			//			"lotNo", "deliveryQuantity", "arrivalWarehouseCode", "originalDeclarationNo",
	    				//		"originalDeclarationSeq", "originalDeclarationDate" };

	    			List<BuPurchaseHead> buPurchaseHeads = buPurchaseHeadDAO.findByProperty("BuPurchaseHead", "headId", headId);
	    			//log.info(buPurchaseHeads.get(0).getHeadId());
	    			List<BuPurchaseLine> buPurchaseLine =buPurchaseLineDAO.findByProperty("BuPurchaseLine", "buPurchaseHead.headId", headId);
	    			log.info(buPurchaseLine.size());
	    			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
	    			List rowData = new ArrayList();
	    			for (int i = 0; i < buPurchaseLine.size(); i++)
	    			{
						Object[] dataObject = new Object[object.length];


						BuPurchaseLine newbuPurchaseLine = (BuPurchaseLine)buPurchaseLine.get(i);
						
						log.info(newbuPurchaseLine.getItemNo());
						String itemCode = newbuPurchaseLine.getItemNo();
						String warehouseCode = null;
						if(null!=itemCode)
						{
							List<ImItem> imItems = imItemDAO.findByProperty("itemCode", itemCode);
							ImItem imItem = imItems.get(0);
							String itemName = imItem.getItemCName();
							String itemCategory = imItem.getItemCategory();
							String shopCode = newbuPurchaseLine.getShopCode();
							Long boxCapacity = imItem.getBoxCapacity();
							Long box = buPurchaseLine.get(i).getQuantity()/boxCapacity;
							
							log.info("itemCategory"+itemCategory);
							if(itemCategory.equals("T")|| itemCategory.equals("F") || itemCategory.equals("B") || itemCategory.equals("D") || itemCategory.equals("K"))
							{
								warehouseCode = "D6200";
							}
							else
							{
								if(imItem.getIsTax().equals("F"))
								{
									warehouseCode = "F9900";
								}
								else
								{
									warehouseCode = "P9900";
								}
							}

							ImItemOnHandViewId id = new ImItemOnHandViewId();
							id.setBrandCode(brandCode);
							id.setItemCode(itemCode);
							id.setWarehouseCode(warehouseCode);
							id.setLotNo("000000000000");
							ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
							log.info(item.getItemCategory());
							//List<ImItemOnHandView> item = imItemOnHandViewDAO.findByProperty("ImItemOnHandView", "id.itemCode", itemCode);

							ImItemOnHandViewId id2 = new ImItemOnHandViewId();
							id2.setBrandCode(brandCode);
							id2.setItemCode(itemCode);
							id2.setWarehouseCode(shopCode);
							id2.setLotNo("000000000000");
							ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
							int index = i+1;
//索引值							
							dataObject[0] = Integer.toString(index);
//單別
							dataObject[1] = buPurchaseHeads.get(0).getOrderTypeCode();
//單號
							dataObject[2] = buPurchaseHeads.get(0).getOrderNo();
//品號
							dataObject[3] = itemCode;
//品名
							dataObject[4] = itemName;
//大類
							dataObject[5] = imItem.getCategory01();
//中類
							dataObject[6] = imItem.getCategory02();
//稅別
							dataObject[7] = imItem.getIsTax();
//來源庫
							dataObject[8] = item.getId().getWarehouseCode();
//來源庫存
							dataObject[9] = NumberUtils.getDouble(item.getCurrentOnHandQty());
//基本量
							dataObject[10] = NumberUtils.getLong(boxCapacity);
//需求量
							dataObject[11] = NumberUtils.getLong(newbuPurchaseLine.getQuantity()+0L);
//箱數
							dataObject[12] = NumberUtils.getLong(box);
//店別
							dataObject[13] = shopCode;
//店庫存
							dataObject[14] = NumberUtils.getDouble(item2.getCurrentOnHandQty());
							rowData.add(dataObject);
						
						}
						
						*/
/*
	    				log.info("4");
							dataObject[0] = i+1; 	// INDEX_NO
		    				dataObject[1] = buPurchaseLine.get(i).getItemNo(); 	// ITEM_NO
							dataObject[2] = buPurchaseLine.get(i).getItemName().toString(); 	// ITEM_NAME
							dataObject[3] = buPurchaseLine.get(i).getSupplier().toString(); 	// SUPPLIER
							dataObject[4] = buPurchaseLine.get(i).getPurTotalAmount().toString(); 	// PUR_TOTAL_AMOUNT
							dataObject[5] = 2; 				// boxCapacity
							dataObject[6] = buPurchaseLine.get(i).getQuantity().toString(); 	// QUANTITY
							dataObject[7] = 1;		   		// box
							dataObject[8] = buPurchaseLine.get(i).getShopCode().toString(); 	// SHOP_CODE
							dataObject[9] = buPurchaseLine.get(i).getReTotalAmount().toString(); 	// RE_TOTAL_AMOUNT	
						log.info("6"); 
*/


/*					}
	    			return new SelectDataInfo(object, rowData);
	    		} catch (Exception ex) {
	    			 ex.printStackTrace();
	    			log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
	    			throw new Exception("載入頁面顯示的調撥明細失敗！");
	    		}
	    	}*/
	

	    	public void executeImportLists(Long headId, String shopCode, List lineLists) throws Exception{
	    		try{
	    			log.info(" headId = " + headId);
	    			log.info(" shopCode = " + shopCode);
	    			//imAdjustmentLineService.deleteLineLists(headId);

	    			if(lineLists != null && lineLists.size() > 0)
	    			{
	    				BuPurchaseHead buPurchaseHead = buPurchaseHeadDAO.findById(headId);
	    				List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.findByProperty("BuPurchaseHead", "headId", headId);
	    				//buPurchaseHead.setDefaultWarehouseCode(defaultWarehouseCode);
	    				for(int i = 0; i < lineLists.size(); i++)
	    				{
	    					log.info("1");
	    					BuPurchaseLine  buPurchaseLineTmp  = (BuPurchaseLine)lineLists.get(i);
	    					log.info(buPurchaseLineTmp.getItemNo());
	    					//buPurchaseLine.setItemNo(buPurchaseLineTmp.getItemNo());
	    					//buPurchaseLine.setQuantity(buPurchaseLineTmp.getQuantity());
	    					//buPurchaseLine.setShopCode(buPurchaseLineTmp.getShopCode());
	    					//log.info("2");
	    					buPurchaseLineTmp.setIndexNo(i+1L);
	    					buPurchaseLines.add(buPurchaseLineTmp);
	    					buPurchaseHead.setBuPurchaseLines(buPurchaseLines);
	    				}
	    				
    					buPurchaseHeadDAO.merge(buPurchaseHead);
	    			}
	    		}
	    		catch (Exception ex) 
	    		{
	    			log.error("調整單明細匯入時發生錯誤，原因：" + ex.toString());
	    			ex.printStackTrace();
	    			throw new Exception("調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
	    		}
	    	}

	   	 
	   	 
	   	public SelectDataInfo exportEosDetail(HttpServletRequest httpRequest) throws Exception {
    		try {

                
    			log.info("exportEosDetail");
    			//Long headId = NumberUtils.getLong(httpRequest.getParameter("headId")); // 要顯示的HEAD_ID   			
    			String brandCode = httpRequest.getParameter("brandCode"); // 品牌代號
    			String exportBeanName = httpRequest.getParameter("exportBeanName"); // 調撥單種類
    			String warehouseCode = httpRequest.getParameter("warehouseCode");
    			String shopCode = httpRequest.getParameter("shopCode");
    			String orderNo = httpRequest.getParameter("orderNo");
    			String status = httpRequest.getParameter("status");
    			String startDate = httpRequest.getParameter("startDate");
    			String endDate = httpRequest.getParameter("endDate");
    			String function = httpRequest.getParameter("function");
    			String categoryItem = httpRequest.getParameter("categoryItem");
    			String enable = httpRequest.getParameter("enable");
    			String isTax = httpRequest.getParameter("isTax");
    			String orderTypeCode = "EOS";
    			Date startDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate);
    			Date endDateDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate);
    			warehouseCode = warehouseCodeSwitch(warehouseCode,isTax);
    			log.info("orderTypeCode:"+orderTypeCode);
    			log.info("orderNo:"+orderNo);
	   			log.info("status:"+status);
	   			log.info("brandCode:"+brandCode);
	   			log.info("shopCode:"+shopCode);
	   			log.info("warehouseCode:"+warehouseCode);
	   			log.info("startDate:"+startDate);
	   			log.info("endDate:"+endDate);
	   			log.info("function:"+function);
	   			log.info("categoryItem:"+categoryItem);
	   			log.info("enable:"+enable);
	   			
	   			HashMap map = new HashMap();
	   			HashMap findMap = new HashMap();
	   			findMap.put("orderNo",orderNo);
	   			findMap.put("orderTypeCode",orderTypeCode);
	   			findMap.put("status",status);
	   			findMap.put("brandCode",brandCode);
	   			findMap.put("shopCode",shopCode);
	   			findMap.put("warehouseCode",warehouseCode);
	   			findMap.put("startDate",startDate);
	   			findMap.put("endDate",endDate);
	   			findMap.put("categoryItem",categoryItem);
	   			findMap.put("enable",enable);
   			 //==============================================================	
   			 List<BuPurchaseLine> buPurchaseLine = buPurchaseLineDAO.findEosRequestDetailAll(findMap);
   			 log.info("buPurchaseLine.size"+ buPurchaseLine.size());	
    			// 可用庫存excel表的欄位順序
    			Object[] object = null;

    			if(function.equals("1"))
    			{
    				 object = new Object[] {
	    					 "shopCode", "orderNo","itemBrandCode", "itemNo","itemName","unitPrice",
	    					 "quantity","purTotalAmount","category02","box","storage","lotNo","itemCategoty"};
    			}
    			else
    			{
    				object = new Object[] {
	    					 "shopCode", "orderNo", "itemNo","itemName","box",
	    					 "quantity","purTotalAmount","orderTime"};
    			}
    			log.info(buPurchaseLine.size());
    			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
    			List rowData = new ArrayList();
    			for (int i = 0; i < buPurchaseLine.size(); i++)
    			{
					Object[] dataObject = new Object[object.length];
					log.info(buPurchaseLine.get(i).getBuPurchaseHead().getDepartment());
					log.info(buPurchaseLine.get(i).getBuPurchaseHead().getOrderNo());
					log.info(buPurchaseLine.get(i).getItemNo());
					log.info(buPurchaseLine.get(i).getItemName());
					log.info(brandCode+"  "+buPurchaseLine.get(i).getItemNo()+"  "+warehouseCode+"  "+"000000000000");
					ImItemOnHandViewId id = new ImItemOnHandViewId();
					id.setBrandCode(brandCode);
					id.setItemCode(buPurchaseLine.get(i).getItemNo());
					id.setWarehouseCode(buPurchaseLine.get(i).getSupplier());
					id.setLotNo("000000000000");
					ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
					ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, buPurchaseLine.get(i).getItemNo(), "1");

					if(function.equals("1")){
//SHOP_CODE
						dataObject[0] = buPurchaseLine.get(i).getBuPurchaseHead().getDepartment();
						 if(StringUtils.hasText(buPurchaseLine.get(i).getBuPurchaseHead().getOtherGroup()))
						 {
							 dataObject[0] = dataObject[0] +buPurchaseLine.get(i).getBuPurchaseHead().getOtherGroup();
						 }
//單號
						dataObject[1] = buPurchaseLine.get(i).getBuPurchaseHead().getOrderNo();
//品牌					
						dataObject[2] = imItemCurrentPriceView.getItemBrand();
//品號
						dataObject[3] = buPurchaseLine.get(i).getItemNo();
//品名
						dataObject[4] = buPurchaseLine.get(i).getItemName();
//UNIT_PRICE
						dataObject[5] = imItemCurrentPriceView.getUnitPrice();
//QUANTITY
						dataObject[6] =  buPurchaseLine.get(i).getQuantity();
//WAREHOUSE_QUENTITY
						dataObject[7] = item.getCurrentOnHandQty();
//CATEGORY02
						dataObject[8] = item.getCategory02Name();
//BOX
						dataObject[9] = ((int)Math.ceil(buPurchaseLine.get(i).getQuantity()/item.getBoxCapacity()));
//STORAGE
						dataObject[10] = "";
//LOT_NO
						dataObject[11] = "";
//業種maco20161026
						try{
							String itemCateogry = imItemCategoryDAO.findByCategoryCode(brandCode, "ITEM_CATEGORY", buPurchaseLine.get(i).getBuPurchaseHead().getCategoryItem(), "Y").getParentCategoryCode();
						log.info("itemCateogry"+itemCateogry);
							dataObject[12] = imItemCategoryDAO.findByCategoryCode(brandCode, "CATEGORY00", itemCateogry, "Y").getCategoryName();
						}
						 catch(Exception e){
								log.error("取得商品業種名稱失敗");
								throw new Exception("取得商品業種名稱失敗");
							 }
					
					}
					else
					{
//SHOP_CODE
						dataObject[0] = buPurchaseLine.get(i).getBuPurchaseHead().getDepartment();
						 if(StringUtils.hasText(buPurchaseLine.get(i).getBuPurchaseHead().getOtherGroup()))
						 {
							 dataObject[0] = dataObject[0] +buPurchaseLine.get(i).getBuPurchaseHead().getOtherGroup();
						 }
//單號
						dataObject[1] = buPurchaseLine.get(i).getBuPurchaseHead().getOrderNo();
//品號
						dataObject[2] = buPurchaseLine.get(i).getItemNo();
//品名
						dataObject[3] = buPurchaseLine.get(i).getItemName();
//BOX
						dataObject[4] = ((int)Math.ceil(buPurchaseLine.get(i).getQuantity()/item.getBoxCapacity()));
//QUANTITY
						dataObject[5] =  buPurchaseLine.get(i).getQuantity();
//WAREHOUSE_QUENTITY
						dataObject[6] = item.getCurrentOnHandQty();
//訂貨時間
						dataObject[7] = buPurchaseLine.get(i).getBuPurchaseHead().getLastUpdateDate();

					}
					rowData.add(dataObject);
    			}
					
					
/*
    				log.info("4");
						dataObject[0] = i+1; 	// INDEX_NO
	    				dataObject[1] = buPurchaseLine.get(i).getItemNo(); 	// ITEM_NO
						dataObject[2] = buPurchaseLine.get(i).getItemName().toString(); 	// ITEM_NAME
						dataObject[3] = buPurchaseLine.get(i).getSupplier().toString(); 	// SUPPLIER
						dataObject[4] = buPurchaseLine.get(i).getPurTotalAmount().toString(); 	// PUR_TOTAL_AMOUNT
						dataObject[5] = 2; 				// boxCapacity
						dataObject[6] = buPurchaseLine.get(i).getQuantity().toString(); 	// QUANTITY
						dataObject[7] = 1;		   		// box
						dataObject[8] = buPurchaseLine.get(i).getShopCode().toString(); 	// SHOP_CODE
						dataObject[9] = buPurchaseLine.get(i).getReTotalAmount().toString(); 	// RE_TOTAL_AMOUNT	
					log.info("6"); 
*/


    			return new SelectDataInfo(object, rowData);
    		} catch (Exception ex) {
    			 ex.printStackTrace();
    			log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
    			throw new Exception("載入頁面顯示的調撥明細失敗！");
    		}
    	}
	   	 
	   	 
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	
	   	 public List<Properties> getEosList(Properties httpRequest) throws Exception{
		 		List list = new ArrayList();

				 try{
					 List<Properties> result = new ArrayList();
					 List<Properties> gridDatas = new ArrayList();
					 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
					 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
					 //======================帶入Head的值=========================
					 String headId = httpRequest.getProperty("headId");
					 String brandCode = httpRequest.getProperty("brandCode");
					 String warehouseCode = httpRequest.getProperty("warehouseCode");
					 String department = httpRequest.getProperty("department");
					 String itemCategory = httpRequest.getProperty("itemCategory");  
					 String category01 = httpRequest.getProperty("category01");
					 String category02s = httpRequest.getProperty("category02s");
					 String category02 = httpRequest.getProperty("category02");
					 String category03 = httpRequest.getProperty("category03");
					 String isTax = httpRequest.getProperty("isTax");
					 String status = httpRequest.getProperty("status");
					 String itemBrand = httpRequest.getProperty("itemBrand");
					 String itemBrands = httpRequest.getProperty("itemBrands");


					 String filterA = httpRequest.getProperty("filterA");
					 String filterB = httpRequest.getProperty("filterB");
					 String filterC1 = httpRequest.getProperty("filterC1");
					 String filterC2 = httpRequest.getProperty("filterC2");
					 String filterD1 = httpRequest.getProperty("filterD1");  
					 String filterD2 = httpRequest.getProperty("filterD2");
					 String sortCondition = httpRequest.getProperty("sortCondition");
					 String sortType = httpRequest.getProperty("sortType");

					 if(!"".equals(category02s))
					 {
						 category02 = category02s;
					 }
					 
	//利用查詢條件查詢品號
					 HashMap map = new HashMap();
					 HashMap findObjs = new HashMap();
					 String srcWarehouse;
					 //查來源庫
					 log.info(itemCategory+"  "+category02+"  "+isTax);
					 srcWarehouse = this.findSorceWarehouse(itemCategory,category01,category02,isTax,"");
					 //暫存狀態抓取ON_HAND
					 
					 if(status.equals(OrderStatus.SAVE))
					 {
						 if("".equals(category02s)||null==category02s)
						 {
							 if(!("".equals(category02)||null==category02))
								 category02 = "'" + category02 + "'";
						 }
						 else
						 {
							 category02 = category02s;
						 }
						 if("".equals(itemBrands)||null==itemBrands)
						 {
							 if(!("".equals(itemBrand)||null==itemBrand))
								 itemBrand = "'" + itemBrand + "'";
						 }
						 else
						 {
							 itemBrand = itemBrands;
						 }
						 findObjs.put("brandCode", brandCode);
						 findObjs.put("warehouseCode", warehouseCode);
						 findObjs.put("itemCategory", itemCategory);
						 findObjs.put("category01", category01);
						 findObjs.put("category02", category02);
						 findObjs.put("category03", category03);
						 findObjs.put("isTax", isTax);
						 findObjs.put("itemBrand", itemBrand);
						 findObjs.put("srcWarehouse", srcWarehouse);

						 findObjs.put("filterA", filterA);
						 findObjs.put("filterB", filterB);
						 findObjs.put("filterC1", filterC1);
						 findObjs.put("filterC2", filterC2);
						 findObjs.put("filterD1", filterD1);
						 findObjs.put("filterD2", filterD2);
						 findObjs.put("sortCondition", sortCondition);
						 findObjs.put("sortType", sortType);

						 
						 List<ImItemOnHandView> imItemOnHandViews2 = imItemOnHandViewDAO.findEosDetailItem(findObjs,iSPage,iPSize); 
							 log.info("imItemOnHandView2.size"+ imItemOnHandViews2.size());

							 if (imItemOnHandViews2 != null && imItemOnHandViews2.size() > 0) {
								 List<BuPurchaseLine> buPurchaseLines = new ArrayList();
								 int count = 0;
								 for (ImItemOnHandView imItemOnHandView : imItemOnHandViews2)
								 {
									 log.info("buPurchaseHead.headId"+NumberUtils.getLong(headId)+"itemNo"+imItemOnHandView.getId().getItemCode());
									 String[] fieldName = {"buPurchaseHead.headId","itemNo"};
									 Object[] fieldValue = {NumberUtils.getLong(headId),imItemOnHandView.getId().getItemCode()};
									 List<BuPurchaseLine> findList = buPurchaseLineDAO.findByProperty("BuPurchaseLine",fieldName , fieldValue);
									 BuPurchaseLine buPurchaseLine;
									 if(findList.size()>0){
										 buPurchaseLine = findList.get(0);
									 }
									 else
									 {
										 buPurchaseLine = new BuPurchaseLine();
									 }
									 if(null!=imItemOnHandView.getId().getItemCode())
									 {
										 
											ImItemOnHandViewId id2 = new ImItemOnHandViewId();
											id2.setBrandCode(brandCode);
											id2.setItemCode(imItemOnHandView.getId().getItemCode());
											id2.setWarehouseCode(warehouseCode);
											id2.setLotNo("000000000000");
											ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
										 
										 
										 buPurchaseLine.setItemNo(imItemOnHandView.getId().getItemCode());//品號
										 buPurchaseLine.setItemName(imItemOnHandView.getItemCName());//品名
										 buPurchaseLine.setShopCode(warehouseCode);//店別
										 buPurchaseLine.setReTotalAmount((int)(item2.getCurrentOnHandQty()+0));//店庫存
										 
										 //2016.10.21 MACO 若是保稅倉出貨不看基本量(基本量=1)
										 log.info("來源庫:"+srcWarehouse);
										 if(srcWarehouse.equals("F9900")||srcWarehouse.equals("P9900")){
											 buPurchaseLine.setBoxCapacity(1L);
										 }
										 else{
											 
											 buPurchaseLine.setBoxCapacity(imItemOnHandView.getBoxCapacity());//基本量
										 }
										 buPurchaseLine.setSupplier(srcWarehouse);//來源庫
										 HashMap conditionMap = new HashMap();
										 conditionMap.put("orderTypeCode","EOS");
										 conditionMap.put("itemNo",buPurchaseLine.getItemNo());
										 conditionMap.put("status","SIGNING");
										 conditionMap.put("brandCode",brandCode);
										 conditionMap.put("warehouseCode",buPurchaseLine.getSupplier());
										 conditionMap.put("enable","N");
										 int onShip = buPurchaseLineDAO.findItemOnShip(conditionMap);
										 buPurchaseLine.setPurTotalAmount((int)(imItemOnHandView.getCurrentOnHandQty()-onShip));
										 buPurchaseLine.setTotalQty(onShip);
										 buPurchaseLine.setUnitPrice(imItemOnHandView.getUnitPrice());
										 //buPurchaseLine.setPurTotalAmount((int)(imItemOnHandView.getCurrentOnHandQty()+0));//來源庫存
										 if(null!=buPurchaseLine.getQuantity())
											 buPurchaseLine.setBox(buPurchaseLine.getQuantity()/imItemOnHandView.getBoxCapacity());
										 else
											 buPurchaseLine.setBox(0L);
										 //buPurchaseLine.setBox(buPurchaseLine.getQuantity()/imItemOnHandView.getBoxCapacity());//箱數
									 }
									 else{
										 log.info("NO DATA!");
									 }
									 buPurchaseLines.add(buPurchaseLine);
								 }
								 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
								 Long maxIndex =  imItemOnHandViewDAO.findEosDetailItemCount(findObjs,iSPage,iPSize); 

								 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, buPurchaseLines, gridDatas, firstIndex, maxIndex));
							 }
							 else
							 {
								 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, map, gridDatas));
							 }
				}
				else
				{
					//送出後抓EOS明細
					buPurchaseHead = buPurchaseHeadDAO.findById(NumberUtils.getLong(headId));
					List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.findPageLine(NumberUtils.getLong(headId),iSPage,iPSize);

					 if (buPurchaseLines != null && buPurchaseLines.size() > 0)
					 {

						 int count = 0;
						 for (BuPurchaseLine buPurchaseLine : buPurchaseLines)
						 {
								srcWarehouse = buPurchaseLine.getSupplier();
								if(null!=buPurchaseLine.getItemNo())
								{
									String itemCode = buPurchaseLine.getItemNo();
	//品號								
									buPurchaseLine.setItemNo(itemCode);
	//店別
									buPurchaseLine.setShopCode(warehouseCode);
									
									ImItemOnHandViewId id = new ImItemOnHandViewId();
									id.setBrandCode(brandCode);
									id.setItemCode(itemCode);
									id.setWarehouseCode(warehouseCode);
									id.setLotNo("000000000000");
									ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
	//店庫存
									buPurchaseLine.setReTotalAmount((int)(item.getCurrentOnHandQty()+0));
									log.info("商品"+itemCode+"的店庫存:"+buPurchaseLine.getReTotalAmount());
	//基本量
									buPurchaseLine.setBoxCapacity(item.getBoxCapacity());

	//品名
									 //2016.10.21 MACO 若是保稅倉出貨不看基本量(基本量=1)
									log.info("來源庫:"+srcWarehouse);
									 if(srcWarehouse.equals("F9900")||srcWarehouse.equals("P9900")){
										 buPurchaseLine.setBoxCapacity(1L); 
									 }
									 else{
										 buPurchaseLine.setBoxCapacity(item.getBoxCapacity());//基本量

									 }
	//來源庫
									buPurchaseLine.setSupplier(srcWarehouse);
									ImItemOnHandViewId id2 = new ImItemOnHandViewId();
									id2.setBrandCode(brandCode);
									id2.setItemCode(itemCode);
									id2.setWarehouseCode(srcWarehouse);
									id2.setLotNo("000000000000");
									ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
	//來源庫存
									if(item2 != null)
									{
										HashMap conditionMap = new HashMap();
										conditionMap.put("orderTypeCode","EOS");
										conditionMap.put("itemNo",buPurchaseLine.getItemNo());
										conditionMap.put("status","SIGNING");
										conditionMap.put("brandCode",brandCode);
										conditionMap.put("warehouseCode",buPurchaseLine.getSupplier());
										conditionMap.put("enable","N");

										
										int onShip = buPurchaseLineDAO.findItemOnShip(conditionMap);
										buPurchaseLine.setPurTotalAmount((int)(item2.getCurrentOnHandQty()-onShip));
										buPurchaseLine.setTotalQty(onShip);
										log.info("商品"+itemCode+"的來源庫存:"+buPurchaseLine.getPurTotalAmount()+"  在途量:"+onShip);
									}
									else
										buPurchaseLine.setPurTotalAmount(0);
									buPurchaseLine.setUnitPrice(item2.getUnitPrice());
	//箱數

									buPurchaseLine.setBox(buPurchaseLine.getQuantity()/item2.getBoxCapacity());
									

								}
								else{
									log.info("NO DATA!");
								}
						 }
						 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
						 Long maxIndex = buPurchaseLineDAO.getItems(NumberUtils.getLong(headId)).size()+0L;
						 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, buPurchaseLines, gridDatas, firstIndex, maxIndex));
					 }
					 else
					 {
						 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, map, gridDatas));
					 }
				}
				return result;
					 
					 
				 }catch(Exception ex){
					 log.error("載入頁面顯示的單位功能查詢發生錯誤，原因：" + ex.toString());
					 ex.printStackTrace();
					 throw new Exception("載入頁面顯示的單位功能");
				 }		
		   	 }

		 public List<Properties> updateSingleInsert(Properties httpRequest) throws Exception{

				List<Properties> result = new ArrayList();
				Properties properties = new Properties();
				HashMap findObjs = new HashMap();
				String msg = "";
				String srcWarehouse="";
				Double warehouseQty=0d;
				Long lineId = 0L ;
				int iSPage = 0;
				int iPSize = 1;
				String quantity = httpRequest.getProperty("quantity");
				String function = httpRequest.getProperty("function");
				String itemCode = httpRequest.getProperty("itemCode");
				Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
				String brandCode = httpRequest.getProperty("brandCode");
				String warehouseCode = httpRequest.getProperty("warehouseCode");
				String department = httpRequest.getProperty("department");
				String itemCategory = httpRequest.getProperty("itemCategory");  
				String category01 = httpRequest.getProperty("category01");
				String category02 = httpRequest.getProperty("category02");
				String category03 = httpRequest.getProperty("category03");
				String isTax = httpRequest.getProperty("isTax");
				String status = httpRequest.getProperty("status");
				String itemBrand = httpRequest.getProperty("itemBrand");
				try{
				 srcWarehouse = this.findSorceWarehouse("","","","",itemCode);
				 findObjs.put("itemCode", itemCode);
				 findObjs.put("brandCode", brandCode);
				 findObjs.put("warehouseCode", warehouseCode);
				 findObjs.put("itemCategory", itemCategory);
				 findObjs.put("category01", category01);
				 findObjs.put("category02", category02);
				 findObjs.put("category03", category03);
				 findObjs.put("isTax", isTax);
				 findObjs.put("itemBrand", itemBrand);
				 findObjs.put("srcWarehouse", srcWarehouse);
				 log.info("itemCode"+ findObjs.get("itemCode"));
				 List<ImItemOnHandView> imItemOnHandViews2 = imItemOnHandViewDAO.findEosDetailItem(findObjs,iSPage,iPSize); 
				 log.info("imItemOnHandView2.size"+ imItemOnHandViews2.size());
				 if(imItemOnHandViews2.size()>0)
				 {
					 ImItemOnHandView imItemOnHandView = imItemOnHandViews2.get(0);
					 warehouseQty = imItemOnHandView.getCurrentOnHandQty();
					 HashMap conditionMap = new HashMap();
						conditionMap.put("orderTypeCode","EOS");
						conditionMap.put("itemNo",itemCode);
						conditionMap.put("status","SIGNING");
						conditionMap.put("brandCode",brandCode);
						conditionMap.put("warehouseCode",srcWarehouse);
						conditionMap.put("enable","N");
					 int onShip = buPurchaseLineDAO.findItemOnShip(conditionMap);
					 warehouseQty = warehouseQty-onShip;
					 if("insert".equals(function))
					 {
	
						 	BuPurchaseHead buPurchaseHead = buPurchaseService.findById(headId);
						 	log.info("H:"+buPurchaseHead.getHeadId()+"   I:"+itemCode+"    QTY:"+quantity);
						 	BuPurchaseLine buPurchaseLine = buPurchaseLineDAO.findEosItemByItemNo(buPurchaseHead.getHeadId(), itemCode);
						 	int indexNo = buPurchaseLineDAO.findPageLineMaxIndex(headId).intValue();
	
								log.info("itemCode:"+itemCode);
								int qty = Integer.parseInt(quantity);
	
	
									//更新明細資料
									if ( null!=buPurchaseLine ) {
										log.info( "更新 = " + headId + " | "+ buPurchaseLine.getLineId()  );
										buPurchaseLine.setItemNo(itemCode);
										buPurchaseLine.setItemName(imItemOnHandView.getItemCName());
										buPurchaseLine.setShopCode(warehouseCode);
										buPurchaseLine.setSupplier(srcWarehouse);
										buPurchaseLine.setQuantity(qty);
										buPurchaseLine.setEnable("N");
										buPurchaseLineDAO.update(buPurchaseLine);
										lineId = buPurchaseLine.getLineId();
										msg = "寫入成功,"+itemCode+"需求量已更新為"+qty;
									}else{
										
										//新增明細資料
										indexNo++;
										BuPurchaseLine line1 = new BuPurchaseLine(); 
										line1.setBuPurchaseHead(buPurchaseHead);
										line1.setIndexNo(Long.valueOf(indexNo));
										line1.setItemNo(itemCode);
										line1.setItemName(imItemOnHandView.getItemCName());
										line1.setShopCode(warehouseCode);
										line1.setSupplier(srcWarehouse);
										line1.setQuantity(qty);
										line1.setEnable("N");
										//當使用者輸入需求量後才新增明細
										if(line1.getQuantity()>0)
										{
											buPurchaseLineDAO.save(line1);
											lineId = line1.getLineId();
											msg = "寫入成功,新增"+itemCode+"需求量"+qty;
										}
										else
										{
											msg = "寫入失敗,"+itemCode+"需求量並未更新";
										}
									}
									properties.setProperty("requestQty", quantity);
								}
								else
								{
									log.info("庫存查詢"+srcWarehouse+":"+warehouseQty);
									
								}

						properties.setProperty("boxCapacity", srcWarehouse.equals("D6200")?imItemOnHandViews2.get(0).getBoxCapacity().toString():"1");
				 	}
				 	else
				 	{
				 		msg = "庫存查詢失敗，請確認您所輸入的商品";
				 	}
					log.info("sorceWarehouse:"+srcWarehouse+"   warehouseQty:"+warehouseQty);
					properties.setProperty("lineId", lineId.toString());
					properties.setProperty("itemCode", itemCode);
					properties.setProperty("msg1", msg);
					properties.setProperty("sorceWarehouse", srcWarehouse);
					properties.setProperty("warehouseQty", warehouseQty.toString());
					result.add(properties);	

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return result;
			}
		 
		 
		 /*判斷倉庫出貨庫別*/
		 public String findSorceWarehouse(String itemCategory,String category01, String category02, String isTax, String itemCode)
		 {
			
			 String sorceWarehouse="";
			 if(!itemCode.equals(""))
			 {
				 log.info("itemCode"+ itemCode);
				 ImItem item = imItemDAO.findById(itemCode);
				 itemCategory = item.getItemCategory();
				 category01 = item.getCategory01();
				 category02 = item.getCategory02();
				 isTax = item.getIsTax();
			 }
			 log.info(itemCategory+"  "+category01+"  "+category02+"  "+isTax+"  "+itemCode);
//業種子類若為菸酒巧克力>>內倉出貨 
			/* if(itemCategory.equals("T")|| 
					 itemCategory.equals("F") || 
					 itemCategory.equals("B") || 
					 itemCategory.equals("D") || 
					 itemCategory.equals("K"))*/
			 //20160929 Maco 菸酒巧克力(T) 台產(F)由內倉出貨 / 菸酒巧克力的雪茄(A02)與非正貨類(1T)皆由外倉出貨
			 if(itemCategory.equals("T")|| 
					 itemCategory.equals("F"))
			 {
//若為雪茄則例外，由外倉出貨

				 if(category02.replaceAll("'", "").equals("A02")||category01.equals("1T"))
				 {
					 if(isTax.equals("F"))
					 {
						 sorceWarehouse = "F9900";
					 }
					 else
					 {
						 sorceWarehouse = "P9900";
					 }
				 }
				 else
				 {
					 sorceWarehouse = "D6200";
				 }
			 }
			 else
			 {
				 if(isTax.equals("F"))
				 {
					 sorceWarehouse = "F9900";
				 }
				 else
				 {
					 sorceWarehouse = "P9900";
				 }
			 }
				 return sorceWarehouse;
		 }


		 
		 public List<Properties> updateLockStatus(Properties httpRequest) throws Exception{

				List<Properties> result = new ArrayList();
				Properties properties = new Properties();
				//前端
				
				String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
				String warehouse = httpRequest.getProperty("warehouse");
				String lock = httpRequest.getProperty("lock");
				Date date = new Date();
				try {

					//撈資料
					List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseHeadDAO.findEnableLineById("EosOrderLock");

					if(buCommonPhraseLines.size()>0){

						for(BuCommonPhraseLine buCommonPhraseLine:buCommonPhraseLines){

							if(buCommonPhraseLine.getAttribute2().equals(warehouse)&& StringUtils.hasText(lock)){

								buCommonPhraseLine.setAttribute1(lock);
								buCommonPhraseLine.setLastUpdateDate(date);
								buCommonPhraseLine.setLastUpdatedBy(loginEmployeeCode);
								buCommonPhraseLineDAO.update(buCommonPhraseLine);
							}
							//回傳
							log.info(buCommonPhraseLine.getLineCode()+"/"+buCommonPhraseLine.getAttribute1());
							properties.setProperty(buCommonPhraseLine.getId().getLineCode(), buCommonPhraseLine.getAttribute1());
						}
					}
					else
					{
						throw new Exception("更新鎖定狀態資料失敗！查無EosOrderLock資料");
					}
					result.add(properties);	

					return result;	        
				} catch (Exception ex) {
					log.error("庫別：" + warehouse + "、鎖定狀態：" + lock + "更新資料時發生錯誤，原因：" + ex.toString());
					ex.printStackTrace();
					throw new Exception("更新鎖定狀態資料失敗！");
				}        
			}
		 
	    	
		 /*	    	
		 	   	 public List<Properties> getEosList1(Properties httpRequest) throws Exception{
		 	 		List list = new ArrayList();
		 //取得單頭資料

		 	 		
		 			 try{
		 				 List<Properties> result = new ArrayList();
		 				 List<Properties> gridDatas = new ArrayList();
		 				 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		 				 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		 				// int iPSize = 10;// 取得每頁大小
		 				 //======================帶入Head的值=========================

		 				 String headId = httpRequest.getProperty("headId");
		 				 String brandCode = httpRequest.getProperty("brandCode");
		 				 String warehouseCode = httpRequest.getProperty("warehouseCode");
		 				 String department = httpRequest.getProperty("department");
		 				 String itemCategory = httpRequest.getProperty("itemCategory");  
		 				 String category01 = httpRequest.getProperty("category01");
		 				 String category02 = httpRequest.getProperty("category02");
		 				 String category03 = httpRequest.getProperty("category03");
		 				 String isTax = httpRequest.getProperty("isTax");
		 				 String status = httpRequest.getProperty("status");
		 				 String itemBrand = httpRequest.getProperty("itemBrand");

		 				 
		 				 log.info("headId:"+headId);
		 				 log.info("brandCode:"+brandCode);
		 				 log.info("warehouseCode:"+warehouseCode);
		 				 log.info("department:"+department);
		 				 log.info("itemCategory:"+itemCategory);
		 				 log.info("category01:"+category01);
		 				 log.info("category02:"+category02);
		 				 log.info("category03:"+category03);
		 				 log.info("isTax:"+isTax);
		 				 log.info("status:"+status);
		 				 log.info("itemBrand:"+itemBrand);
		 				 
		 				 
		 				 
		 //利用查詢條件查詢品號
		 				 HashMap map = new HashMap();
		 				 if(status.equals(OrderStatus.SAVE))
		 				 {

		 					 HashMap findObjs = new HashMap();
		 					 findObjs.put(" and model.id.brandCode  = :brandCode", brandCode);
		 					 findObjs.put(" and model.id.warehouseCode  = :warehouseCode", warehouseCode);
		 					 findObjs.put(" and model.itemCategory  = :itemCategory", itemCategory);
		 					 findObjs.put(" and model.category01  = :category01", category01);
		 					 findObjs.put(" and model.category02  = :category02", category02);
		 					 findObjs.put(" and model.category03  = :category03", category03);
		 					 findObjs.put(" and model.isTax  = :isTax", isTax);
		 					 findObjs.put(" and model.itemBrand  = :itemBrand", itemBrand);
		 					    //findObjs.put(" and s.salesOrderDate  >= :salesOrderStartDate",salesOrderStartDate);
		 					    //findObjs.put(" and s.salesOrderDate  <= :salesOrderEndDate",salesOrderEndDate);
		 					 //==============================================================	    
		 	//額外欄位存入
		 					 Map returnMap = imItemOnHandViewDAO.search( "ImItemOnHandView as model", findObjs,"order by model.id.itemCode asc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
		 					 List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) returnMap.get(BaseDAO.TABLE_LIST); 
		 					 log.info("gggggggggggggggggggggggggggggggggggggggggggggggg");	
		 					 log.info("imItemOnHandView.size"+ imItemOnHandViews.size());	
		 					 if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {
		 						 List<BuPurchaseLine> buPurchaseLines = new ArrayList();
		 						 int count = 0;
		 						 for (ImItemOnHandView imItemOnHandView : imItemOnHandViews)
		 						 {
		 							 log.info("buPurchaseHead.headId"+NumberUtils.getLong(headId)+"itemNo"+imItemOnHandView.getId().getItemCode());
		 							 String[] fieldName = {"buPurchaseHead.headId","itemNo"};
		 							 Object[] fieldValue = {NumberUtils.getLong(headId),imItemOnHandView.getId().getItemCode()};
		 							 List<BuPurchaseLine> findList = buPurchaseLineDAO.findByProperty("BuPurchaseLine",fieldName , fieldValue);
		 							 BuPurchaseLine buPurchaseLine;
		 							 if(findList.size()>0){
		 								 buPurchaseLine = findList.get(0);
		 								 
		 							 }
		 							 else
		 							 {
		 								 buPurchaseLine = new BuPurchaseLine();
		 							 }
		 							 
		 	
		 							 
		 							 
		 			BuPurchaseHead newHead = buPurchaseService.findById(headId);
		 						List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,GRID_SAVE_FIELD_NAMES);
		 						//int indexNo = upRecords.size();
		 						log.info("updateAJAXPageLinesData.maxIndexNo:" + upRecords.size());
		 						buPurchaseHead.setHeadId(headId);
		 						int indexNo = buPurchaseLineDAO.findPageLineMaxIndex(headId).intValue();
		 						if (upRecords != null) {
		 							for (Properties upRecord : upRecords) {
		 								
		 								Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
		 								BuPurchaseLine buPurchaseLine = buPurchaseLineDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
		 								
		 							  * 
		 								if(null!=imItemOnHandView.getId().getItemCode())
		 								{
		 									String itemCode = imItemOnHandView.getId().getItemCode();
		 	//品號								
		 									buPurchaseLine.setItemNo(itemCode);
		 	//店別
		 									buPurchaseLine.setShopCode(warehouseCode);
		 	//店庫存
		 									buPurchaseLine.setReTotalAmount((int)(imItemOnHandView.getCurrentOnHandQty()+0));
		 	//基本量
		 									buPurchaseLine.setBoxCapacity(imItemOnHandView.getBoxCapacity());
		 									
		 									
		 									
		 									String srcWarehouse;
		 									if(itemCategory.equals("T")|| itemCategory.equals("F") || itemCategory.equals("B") || itemCategory.equals("D") || itemCategory.equals("K"))
		 									{
		 										if(category02.equals("A02"))
		 										{
		 											srcWarehouse = "F9900";
		 										}
		 										else
		 										{
		 											srcWarehouse = "D6200";
		 										}
		 									}
		 									else
		 									{
		 										if(isTax.equals("F"))
		 										{
		 											srcWarehouse = "F9900";
		 										}
		 										else
		 										{
		 											srcWarehouse = "P9900";
		 										}
		 									}
		 	//品名
		 									buPurchaseLine.setItemName(imItemOnHandView.getItemCName());
		 	//來源庫
		 									buPurchaseLine.setSupplier(srcWarehouse);
		 									ImItemOnHandViewId id = new ImItemOnHandViewId();
		 									id.setBrandCode(brandCode);
		 									id.setItemCode(itemCode);
		 									id.setWarehouseCode(srcWarehouse);
		 									id.setLotNo("000000000000");
		 									ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
		 	//來源庫存
		 									if(item != null)
		 									{
		 										buPurchaseLine.setPurTotalAmount((int)(item.getCurrentOnHandQty()+0));
		 									}
		 									else
		 										buPurchaseLine.setPurTotalAmount(0);
		 	//箱數
		 	
		 									//buPurchaseLine.setBox(buPurchaseLine.getQuantity()/imItemOnHandView.getBoxCapacity());
		 	
		 								}
		 								else{
		 									log.info("NO DATA!");
		 								}
		 								buPurchaseLines.add(buPurchaseLine);
		 						 }
		 						 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		 						 Long maxIndex = (Long)imItemOnHandViewDAO.search("ImItemOnHandView as model", "count(model.id.itemCode) as rowCount" ,findObjs,iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		 					 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, buPurchaseLines, gridDatas, firstIndex, maxIndex));
		 				 }
		 				 else
		 				 {
		 					 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, map, gridDatas));
		 				 }
		 			}
		 			else
		 			{
		 				buPurchaseHead = buPurchaseHeadDAO.findById(NumberUtils.getLong(headId));
		 				List<BuPurchaseLine> buPurchaseLines = buPurchaseLineDAO.findPageLine(NumberUtils.getLong(headId),iSPage,iPSize);
		 				 if (buPurchaseLines != null && buPurchaseLines.size() > 0)
		 				 {

		 					 int count = 0;
		 					 for (BuPurchaseLine buPurchaseLine : buPurchaseLines)
		 					 {

		 							if(null!=buPurchaseLine.getItemNo())
		 							{
		 								String itemCode = buPurchaseLine.getItemNo();

		 					
		 //品號								
		 								buPurchaseLine.setItemNo(itemCode);
		 //店別
		 								buPurchaseLine.setShopCode(warehouseCode);
		 								
		 								ImItemOnHandViewId id = new ImItemOnHandViewId();
		 								id.setBrandCode(brandCode);
		 								id.setItemCode(itemCode);
		 								id.setWarehouseCode(warehouseCode);
		 								id.setLotNo("000000000000");
		 								ImItemOnHandView item = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id);
		 //店庫存
		 								buPurchaseLine.setReTotalAmount((int)(item.getCurrentOnHandQty()+0));
		 //基本量
		 								buPurchaseLine.setBoxCapacity(item.getBoxCapacity());
		 								
		 								
		 								
		 								String srcWarehouse;
		 								if(itemCategory.equals("T")|| itemCategory.equals("F") || itemCategory.equals("B") || itemCategory.equals("D") || itemCategory.equals("K"))
		 								{
		 									if(category02.equals("A02"))
		 									{
		 										srcWarehouse = "F9900";
		 									}
		 									else
		 									{
		 										srcWarehouse = "D6200";
		 									}
		 								}
		 								else
		 								{
		 									if(isTax.equals("F"))
		 									{
		 										srcWarehouse = "F9900";
		 									}
		 									else
		 									{
		 										srcWarehouse = "P9900";
		 									}
		 								}
		 //品名
		 								buPurchaseLine.setItemName(item.getItemCName());
		 //來源庫
		 								buPurchaseLine.setSupplier(srcWarehouse);
		 								ImItemOnHandViewId id2 = new ImItemOnHandViewId();
		 								id2.setBrandCode(brandCode);
		 								id2.setItemCode(itemCode);
		 								id2.setWarehouseCode(srcWarehouse);
		 								id2.setLotNo("000000000000");
		 								ImItemOnHandView item2 = (ImItemOnHandView)imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class, id2);
		 //來源庫存
		 								if(item2 != null)
		 									buPurchaseLine.setPurTotalAmount((int)(item2.getCurrentOnHandQty()+0));
		 								else
		 									buPurchaseLine.setPurTotalAmount(0);
		 //箱數

		 								buPurchaseLine.setBox(buPurchaseLine.getQuantity()/item2.getBoxCapacity());

		 							}
		 							else{
		 								log.info("NO DATA!");
		 							}

		 					 }
		 					 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
		 					 Long maxIndex = buPurchaseLines.size()+0L;
		 					 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, buPurchaseLines, gridDatas, firstIndex, maxIndex));
		 				 }
		 				 else
		 				 {
		 					 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_TEST_SEARCH_FIELD_NAMES, GRID_TEST_FIELD_DEFAULT_VALUES, map, gridDatas));
		 				 }
		 			}
		 			return result;
		 				 
		 				 
		 			 }catch(Exception ex){
		 				 log.error("載入頁面顯示的單位功能查詢發生錯誤，原因：" + ex.toString());
		 				 ex.printStackTrace();
		 				 throw new Exception("載入頁面顯示的單位功能");
		 			 }		
		 	   	 }
		 */
}
