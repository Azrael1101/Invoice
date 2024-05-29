package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandViewSimplify;
import tw.com.tm.erp.hbm.bean.ImItemOnHandViewId;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImDeliveryDetailCountView;
import tw.com.tm.erp.hbm.bean.ImDeliveryDetailCountViewId;
import tw.com.tm.erp.hbm.bean.ImItemOnHandTmp;
import tw.com.tm.erp.hbm.bean.ImItemOnHandTmpId;
import tw.com.tm.erp.hbm.bean.ImDailyOnHandModifyView;
import tw.com.tm.erp.hbm.bean.ImDailyOnHandModifyViewId;
import tw.com.tm.erp.hbm.bean.ImMovementView;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceLine;
import tw.com.tm.erp.hbm.bean.ImOrderOnHandModifyView;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryDetailCountViewDAO;
import tw.com.tm.erp.hbm.dao.ImMonthlyBalanceLineDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.StringTools;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.OracleCallableStatement;
import java.sql.ResultSet;

import org.springframework.context.ApplicationContext;
import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.Statement;

public class ImItemOnHandViewService {

	private static final Log log = LogFactory.getLog(ImItemOnHandViewService.class);
	
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private BuBrandDAO buBrandDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	private ImItemCategoryService imItemCategoryService;
	private NativeQueryDAO nativeQueryDAO;
	private ImItemOnHandViewSimplify imItemOnHandViewSimplify;
	private ImDeliveryDetailCountViewDAO imDeliveryDetailCountViewDAO;
	private ImMonthlyBalanceLineDAO imMonthlyBalanceLineDAO;
	private ImDeliveryHeadDAO imDeliveryHeadDAO;
	
	DataSource dataSource = null;
    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"id.brandCode", "id.itemCode", "itemCName", "id.warehouseCode",
		"warehouseName", "id.lotNo", "itemBrand", "itemBrandName",
		"unitPrice", "stockOnHandQty", "outUncommitQty", "inUncommitQty", 
		"moveUncommitQty", "otherUncommitQty", "currentOnHandQty", "category01", 
		"category01Name", "category02", "category02Name","category03",
		"category03Name", "category17", "supplierName", "supplierItemCode",
		"itemEName", "boxCapacity", "category07", "category09",
		"category13" 
	};
	
	public static final String[] GRID_SEARCH_FIELD_NAMES_NEW = { 
		"id.brandCode", "id.itemCode", "itemCName", "id.warehouseCode",
		"warehouseName","unitPrice" ,"id.lotNo", "stockOnHandQty", "outUncommitQty", 
		"otherUncommitQty",
		"currentOnHandQty"
	};
	
	public static final String[] GRID_SEARCH_FIELD_NAMES_DAILY_MODIFY = { 
		"transationDate", "id.year","id.month","id.day","itemCode","warehouseCode","id.brandCode",
		"id.orderTypeCode", "name", "beginningOnHandQuantity", "dayTotal", "sum", "currentOnHand"
	};
	
	public static final String[] GRID_SEARCH_FIELD_NAMES_ORDER_MODIFY = { 
		"transationDate", "year","month","day","itemCode","warehouseCode","id.brandCode",
		"id.orderTypeCode", "name", "id.orderNo","customerPoNo", "transactionSeqNo","quantity", "sum"
	};

	public static final int[] GRID_SEARCH_FIELD_TYPES = { 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING,
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "",
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "", 
		"", "", "", "", 
		"" 
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES_NEW = { 
		"", "", "", "", 
		"", "", "", "",
		"", "", ""
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES_DAILY_MODIFY = { 
		"", "","","","","","",
		"", "", "", "", "", ""
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES_ORDER_MODIFY = { 
		"", "", "", "", "", "","",
		"", "", "", "", "", "",""
	};

	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	public void setImDeliveryDetailCountViewDAO(ImDeliveryDetailCountViewDAO imDeliveryDetailCountViewDAO) {
		this.imDeliveryDetailCountViewDAO = imDeliveryDetailCountViewDAO;
	}
	
	public void setImMonthlyBalanceLineDAO(ImMonthlyBalanceLineDAO imMonthlyBalanceLineDAO) {
		this.imMonthlyBalanceLineDAO = imMonthlyBalanceLineDAO;
	}
	
	public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
		this.imDeliveryHeadDAO = imDeliveryHeadDAO;
	}
	
	public ImItemOnHandView findImItemOnHandViewById(String itemCode, String brandCode, String warehouseCode, String lotNo) {
		ImItemOnHandView re = (ImItemOnHandView) imItemOnHandViewDAO.findByPrimaryKey(ImItemOnHandView.class,
				new ImItemOnHandViewId(itemCode, brandCode, warehouseCode, lotNo));

		if (null == re)
			re = null;
		return re;
	}
	
	public ImDeliveryDetailCountView findImDeliveryDetailCountViewById(String itemCode, String warehouseCode, String shipDay, String shipMonth, String shipYear) {
		ImDeliveryDetailCountView re = (ImDeliveryDetailCountView) imDeliveryDetailCountViewDAO.findByPrimaryKey(ImDeliveryDetailCountView.class,
				new ImDeliveryDetailCountViewId(itemCode, warehouseCode, shipDay, shipMonth, shipYear));

		if (null == re)
			re = null;
		return re;
	}

	/*
	 * public List<ImItemOnHandView> findImItemOnHandViewByValue( String
	 * brandCode, String itemCName, String startItemCode, String endItemCode,
	 * String startWarehouseCode, String endWarehouseCode, String startLotNo,
	 * String endLotNo, String categorySearchString) {
	 * System.out.println(brandCode+"/"+ startItemCode +"/"+
	 * startWarehouseCode+"/"+ startLotNo); List<ImItemOnHandView> re =
	 * imItemOnHandViewDAO. findImItemOnHandViewByValue(brandCode, itemCName,
	 * startItemCode, endItemCode, startWarehouseCode, endWarehouseCode,
	 * startLotNo, endLotNo, categorySearchString); System.out.println(null ==
	 * re); System.out.println(re); if (re.size() < 1){ re = null; } return re; }
	 */

	public List<ImItemOnHandView> find(HashMap findObjs) {
		List<ImItemOnHandView> re = imItemOnHandViewDAO.find(findObjs);

		if (re.size() < 1) {
			re = null;
		}
		return re;
	}

	public List<ImItemOnHandView> findByWarehouseEmployee(HashMap findObjs) {
		List<ImItemOnHandView> re = imItemOnHandViewDAO.findByWarehouseEmployee(findObjs);

		if (re.size() < 1) {
			re = null;
		}
		return re;
	}

	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", loginBrandCode);
			findObjs.put("warehouseCode", "");
			findObjs.put("warehouseName", "");
			findObjs.put("storage", "");
			findObjs.put("storageArea", "");
			findObjs.put("storageBin", "");
			findObjs.put("warehouseTypeId", 0L);
			findObjs.put("categoryCode", "");
			findObjs.put("locationId", 0L);
			findObjs.put("warehouseManager", "");
			findObjs.put("taxTypeCode", "");
			findObjs.put("enable", "Y");
			findObjs.put("employeeCode", loginEmployeeCode);

			
			Map multiList = new HashMap(0); 
			log.info(loginBrandCode+"-----------------------");
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			
			List<ImWarehouse> imDeliveryWarehouses = imWarehouseDAO.find(findObjs);
			multiList.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(imDeliveryWarehouses, "warehouseCode", "warehouseName", true, true));
			
			List<ImWarehouse> imArrivalWarehouses = imWarehouseDAO.find(findObjs);
			multiList.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(imArrivalWarehouses, "warehouseCode", "warehouseName", true, true));
			
			List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
			multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode", "categoryName", true, !"Y".equalsIgnoreCase((String) resultMap.get("itemCategoryMode"))));
            
			List<ImItemCategory> allItemBrands = imItemCategoryDAO.findAllBrand(loginBrandCode, "ItemBrand");
			log.info("allItemBrands:"+allItemBrands.size());
			multiList.put("allItemBrands", AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));

			
			Map itemCategoryMap = imItemCategoryService.getItemCategoryRelatedList(loginBrandCode, null, null);
			List<ImItemCategory> allCategory01 = (List<ImItemCategory>) itemCategoryMap.get("allCategory01");
			List<ImItemCategory> allCategory02 = (List<ImItemCategory>) itemCategoryMap.get("allCategory02");
			List<ImItemCategory> allCategory03 = (List<ImItemCategory>) itemCategoryMap.get("allCategory03");
			
			multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory03", AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));

			List<ImItemCategory> allCategory07 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY07");
			multiList.put("allCategory07", AjaxUtils.produceSelectorData(allCategory07, "categoryCode", "categoryName", false, true));
			
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			ex.printStackTrace();
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}
	
	public List<Properties> executeSearchInitialNew(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", loginBrandCode);
			findObjs.put("warehouseCode", "");
			findObjs.put("warehouseName", "");
			findObjs.put("storage", "");
			findObjs.put("storageArea", "");
			findObjs.put("storageBin", "");
			findObjs.put("warehouseTypeId", 0L);
			findObjs.put("categoryCode", "");
			findObjs.put("locationId", 0L);
			findObjs.put("warehouseManager", "");
			findObjs.put("taxTypeCode", "");
			findObjs.put("enable", "Y");
			findObjs.put("employeeCode", loginEmployeeCode);

			
			Map multiList = new HashMap(0); 
			log.info(loginBrandCode+"-----------------------");
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			
			List<ImWarehouse> imDeliveryWarehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(loginBrandCode, loginEmployeeCode, null);
			multiList.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(imDeliveryWarehouses, "warehouseCode", "warehouseName", true, true));
			
			List<ImWarehouse> imArrivalWarehouses = imWarehouseDAO.getWarehouseByWarehouseEmployee(loginBrandCode, loginEmployeeCode, null);
			multiList.put("allArrivalWarehouses", AjaxUtils.produceSelectorData(imArrivalWarehouses, "warehouseCode", "warehouseName", true, true));
			
			List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
			multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode", "categoryName", true, !"Y".equalsIgnoreCase((String) resultMap.get("itemCategoryMode"))));
            
			List<ImItemCategory> allItemBrands = imItemCategoryDAO.findAllBrand(loginBrandCode, "ItemBrand");
			log.info("allItemBrands:"+allItemBrands.size());
			multiList.put("allItemBrands", AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));

			
			Map itemCategoryMap = imItemCategoryService.getItemCategoryRelatedList(loginBrandCode, null, null);
			List<ImItemCategory> allCategory01 = (List<ImItemCategory>) itemCategoryMap.get("allCategory01");
			List<ImItemCategory> allCategory02 = (List<ImItemCategory>) itemCategoryMap.get("allCategory02");
			List<ImItemCategory> allCategory03 = (List<ImItemCategory>) itemCategoryMap.get("allCategory03");
			
			multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory02", AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory03", AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));

			List<ImItemCategory> allCategory07 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY07");
			multiList.put("allCategory07", AjaxUtils.produceSelectorData(allCategory07, "categoryCode", "categoryName", false, true));
			
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			ex.printStackTrace();
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}

	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageData");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String itemCodeOrEanCode = httpRequest.getProperty("itemCodeOrEanCode");// 品牌代號
			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String startItemCode = httpRequest.getProperty("startItemCode");// 單別
			String endItemCode = httpRequest.getProperty("endItemCode");
			String itemCName = httpRequest.getProperty("itemCName");
			String startWarehouseCode = httpRequest.getProperty("startWarehouseCode");// 出貨庫別
			String endWarehouseCode = httpRequest.getProperty("endWarehouseCode");
			String startLotNo = httpRequest.getProperty("startLotNo");
			String endLotNo = httpRequest.getProperty("endLotNo");// 轉入庫別
			String showZero = httpRequest.getProperty("showZero");
			String showMinus = httpRequest.getProperty("showMinus"); // 負庫存
			String itemCategory = httpRequest.getProperty("itemCategory");
			String itemBrand = httpRequest.getProperty("itemBrand");
			String category01 = httpRequest.getProperty("category01");
			String category02 = httpRequest.getProperty("category02");
			String category03 = httpRequest.getProperty("category03");
			String category04 = httpRequest.getProperty("category04");
			String category07 = httpRequest.getProperty("category07");
			String category09 = httpRequest.getProperty("category09");
			String category13 = httpRequest.getProperty("category13");
			String taxType = httpRequest.getProperty("taxType");
			String category17 = httpRequest.getProperty("category17"); // 廠商
			String itemCodeList = httpRequest.getProperty("itemCodeList"); // 多品號
			if (itemCodeList != null && !"".equals(itemCodeList)) {
				itemCodeList = itemCodeList.replaceAll(" ", ""); // 把空白鑑移除
				itemCodeList = itemCodeList.replaceAll(",", "','"); // 前端傳回的資料僅用逗號分隔，資料庫查詢需加上單引號
				itemCodeList = "'" + itemCodeList + "'";
			}
			//轉品號
			if("eanCode".equals(itemCodeOrEanCode)){
				startItemCode = transferToItemCode(startItemCode);
				log.info(startItemCode);
				endItemCode="";
				itemCodeList="";
			}

		    
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("startItemCode", startItemCode);
			findObjs.put("endItemCode", endItemCode);
			findObjs.put("itemCName", itemCName);
			findObjs.put("startWarehouseCode", startWarehouseCode);
			findObjs.put("endWarehouseCode", endWarehouseCode);
			findObjs.put("startLotNo", startLotNo);
			findObjs.put("endLotNo", endLotNo);
			findObjs.put("showZero", showZero);
			findObjs.put("showMinus", showMinus); // 負庫存
			findObjs.put("itemCategory", itemCategory);
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("category01", category01);
			findObjs.put("category02", category02);
			findObjs.put("category03", category03);
			findObjs.put("category04", category04); //尺寸
			findObjs.put("category07", category07); //性別
			findObjs.put("category09", category09); //系列
			findObjs.put("category13", category13); //款式
			findObjs.put("taxType", taxType);
			findObjs.put("category17", category17); // 加入廠商代號的條件
			findObjs.put("itemCodeList", itemCodeList); // 多品號

			List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) imItemOnHandViewDAO.findPageLine(findObjs,
					iSPage, iPSize, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("ImItemOnHandView.size" + imItemOnHandViews.size());

			if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				log.info("firstIndex = " + firstIndex);	
				
				Long maxIndex = (Long) imItemOnHandViewDAO.findPageLine(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("maxIndex = " + maxIndex);
				
				log.info("ImItemOnHand.AjaxUtils.getAJAXPageData ");
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						imItemOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("ImItemOnHand.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
			log.info("finish");
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	
	public List<Properties> executeAJAXSearchPageDataNew(Properties httpRequest) throws Exception {
		log.info("---------------------getAJAXSearchPageDataNew---------------------"); 
		try {
			
			String action = httpRequest.getProperty("action");
			String onHandTimeScope = httpRequest.getProperty("onHandTimeScope");
			log.info("---------------------action: "+action+"---------------------");
			log.info("---------------------onHandTimeScope: "+onHandTimeScope+"---------------------");
			if("search".equals(action)){
				log.info("---------------------執行查詢---------------------");
				imItemOnHandViewDAO.deleteTmp(onHandTimeScope);
				saveTmp(httpRequest);
			}else{
				log.info("---------------------執行翻頁---------------------");
			}
			List<Properties> result = new ArrayList(); 
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			log.info("---------------------暫存TABLE取值---------------------");
			// ======================帶入Head的值=========================
			String itemCodeOrEanCode = httpRequest.getProperty("itemCodeOrEanCode");// 品牌代號
			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String startItemCode = httpRequest.getProperty("startItemCode");// 單別
			String endItemCode = httpRequest.getProperty("endItemCode");
			String itemCName = httpRequest.getProperty("itemCName");
			String startWarehouseCode = httpRequest.getProperty("startWarehouseCode");// 出貨庫別
			String endWarehouseCode = httpRequest.getProperty("endWarehouseCode");
			String startLotNo = httpRequest.getProperty("startLotNo");
			String endLotNo = httpRequest.getProperty("endLotNo");// 轉入庫別
			String showZero = httpRequest.getProperty("showZero");
			String showMinus = httpRequest.getProperty("showMinus"); // 負庫存
			String itemCategory = httpRequest.getProperty("itemCategory");
			String itemBrand = httpRequest.getProperty("itemBrand");
			String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
			log.info("---------------------暫存TABLE取值完成---------------------");
			boolean flag = false;
			String warehouseRange = "'999999'";
			log.info(warehouseEmployee+"----------------------------");
			log.info("---------------------暫存TABLE取品號---------------------");
			String itemCodeList = httpRequest.getProperty("itemCodeList"); // 多品號
			if (itemCodeList != null && !"".equals(itemCodeList)) {
				itemCodeList = itemCodeList.replaceAll(" ", ""); // 把空白鑑移除
				itemCodeList = itemCodeList.replaceAll(",", "','"); // 前端傳回的資料僅用逗號分隔，資料庫查詢需加上單引號
				itemCodeList = "'" + itemCodeList + "'";
			}
			log.info("---------------------暫存TABLE取品號完成---------------------");
			//轉品號
			log.info("---------------------暫存TABLE轉國際碼---------------------");
			if("eanCode".equals(itemCodeOrEanCode)){
				startItemCode = transferToItemCode(startItemCode);
				log.info(startItemCode);
				endItemCode="";
				itemCodeList="";
			}
			log.info("---------------------暫存TABLE轉國際碼完成---------------------");
			
			String[] warehouseList = warehouseEmployee.split(",");
			log.info("---------------------暫存TABLE取庫別---------------------");
			for(int i = (warehouseList.length+3)/2; i < warehouseList.length; i++){
				log.info(warehouseList[i]);
				if(warehouseList[i].equals(startWarehouseCode)){
					flag = true;
				}
				
				if(flag){
					warehouseRange = warehouseRange + ",'" + warehouseList[i] + "'";
				}
				else if(!StringUtils.hasText(startWarehouseCode) && !StringUtils.hasText(endWarehouseCode)){
					warehouseRange = warehouseRange + ",'" + warehouseList[i] + "'";
				}
				
				if(warehouseList[i].equals(endWarehouseCode)){
					flag = false;
				}
			}
			log.info("---------------------暫存TABLE取庫別完成---------------------");
			log.info(warehouseRange+"--------------------------");

			log.info("---------------------暫存TABLE條件塞入---------------------");
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("startItemCode", startItemCode);
			findObjs.put("endItemCode", endItemCode);
			findObjs.put("itemCName", itemCName);
			findObjs.put("startWarehouseCode", startWarehouseCode);
			findObjs.put("endWarehouseCode", endWarehouseCode);
			findObjs.put("startLotNo", startLotNo);
			findObjs.put("endLotNo", endLotNo);
			findObjs.put("showZero", showZero);
			findObjs.put("showMinus", showMinus); // 負庫存
			findObjs.put("itemCategory", itemCategory);
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("itemCodeList", itemCodeList); // 多品號
			findObjs.put("warehouseRange", warehouseRange);
			findObjs.put("onHandTimeScope", onHandTimeScope);
			log.info("---------------------暫存TABLE條件塞入完成---------------------");
			List<ImItemOnHandTmp> imItemOnHandViews = (List<ImItemOnHandTmp>) imItemOnHandViewDAO.findPageLineNew(findObjs,
					iSPage, iPSize, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			
			log.info("--------------------------ImItemOnHandView.size" + imItemOnHandViews.size()+"--------------------------");

			if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX 
				log.info("--------------------------firstIndex = " + firstIndex + "--------------------------");	
				
				Long maxIndex = (Long) imItemOnHandViewDAO.findPageLineNew(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("--------------------------maxIndex = " + maxIndex + "--------------------------");
				
				log.info("--------------------------ImItemOnHand.AjaxUtils.getAJAXPageData --------------------------");
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES_NEW, GRID_SEARCH_FIELD_DEFAULT_VALUES_NEW,
						imItemOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				Long maxIndex = (Long) imItemOnHandViewDAO.findPageLineNew(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("--------------------------maxIndex = " + maxIndex + "--------------------------");
				log.info("--------------------------ImItemOnHand.AjaxUtils.getAJAXPageDataDefault --------------------------");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES_NEW,
						GRID_SEARCH_FIELD_DEFAULT_VALUES_NEW, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	
	public List<Properties> getAJAXSearchPageDataOnHandModify(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageDataOnHandModify"); 
		try {

			List<Properties> result = new ArrayList(); 
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String itemCode = httpRequest.getProperty("itemCode");
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String brandCode = httpRequest.getProperty("loginBrandCode");
			String beginningYear = httpRequest.getProperty("beginningYear");
			String beginningMonth = httpRequest.getProperty("beginningMonth");
			String endingYear = httpRequest.getProperty("endingYear");
			String endingMonth = httpRequest.getProperty("endingMonth");
			String endingDay = httpRequest.getProperty("endingDay");
			String forwardYear = "";
			String forwardMonth = "";
			Double shiftQuantity = 0.0;
			
			Calendar now = Calendar.getInstance();
			
			if("".equals(beginningYear) || beginningYear == null)	{
				beginningYear = String.format("%04d", now.get(Calendar.YEAR));
			}
			
			if("".equals(beginningMonth) || beginningMonth == null)	{
				beginningMonth = String.format("%02d", now.get(Calendar.MONTH)+1);//0~11  
			}
			
			//判斷上個月是否有跨年
			if("01".equals(beginningMonth)){
				forwardYear = String.format("%04d", Integer.valueOf(beginningYear)-1);
				forwardMonth = "12";
			}else{
				forwardYear = String.format("%04d", Integer.valueOf(beginningYear));
				forwardMonth = String.format("%02d", Integer.valueOf(beginningMonth)-1);
			}
			
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("itemCode", itemCode);
			findObjs.put("warehouseCode", warehouseCode);
			findObjs.put("brandCode", brandCode);
			findObjs.put("beginningYear", beginningYear);
			findObjs.put("beginningMonth", beginningMonth);
			findObjs.put("endingYear", endingYear);
			findObjs.put("endingMonth", endingMonth);
			findObjs.put("endingDay", endingDay);


			List<ImDailyOnHandModifyView> imDailyOnHandModify = (List<ImDailyOnHandModifyView>) imItemOnHandViewDAO.findOnHandModifyQuantity(findObjs,
					iSPage, iPSize, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			
			log.info("ImDailyOnHandModify.size" + imDailyOnHandModify.size());
			
			shiftQuantity = Double.parseDouble((String)imItemOnHandViewDAO.findOnHandModifyQuantity(findObjs,
					1, 1, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_SHIFT).get("shiftQuantity"));
			
			
			ImMonthlyBalanceLine imMonthlyBalanceLine = imMonthlyBalanceLineDAO.findById(brandCode, itemCode, warehouseCode, "000000000000", forwardYear, forwardMonth);
			
			for(int i = 0; i < imDailyOnHandModify.size();i++){
				
				imDailyOnHandModify.get(i).setBeginningOnHandQuantity(imMonthlyBalanceLine.getEndingOnHandQuantity());
				imDailyOnHandModify.get(i).setSum(imDailyOnHandModify.get(i).getSum() + shiftQuantity);
				imDailyOnHandModify.get(i).setCurrentOnHand(imMonthlyBalanceLine.getEndingOnHandQuantity()+imDailyOnHandModify.get(i).getSum());
			}
			
			if (imDailyOnHandModify != null && imDailyOnHandModify.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX 
				log.info("firstIndex = " + firstIndex);	
				
				Long maxIndex = (Long) imItemOnHandViewDAO.findOnHandModifyQuantity(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("ImDailyOnHandModify maxIndex = " + maxIndex);
				
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES_DAILY_MODIFY, GRID_SEARCH_FIELD_DEFAULT_VALUES_DAILY_MODIFY,
						imDailyOnHandModify, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("ImItemOnHand.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES_DAILY_MODIFY,
						GRID_SEARCH_FIELD_DEFAULT_VALUES_DAILY_MODIFY, map, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	/*單據異動明細*/
	public List<Properties> getAJAXSearchPageDataOnHandModifyPerOrder(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageDataOnHandModifyOrder"); 
		try {

			List<Properties> result = new ArrayList(); 
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String itemCode = httpRequest.getProperty("itemCode");
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String brandCode = httpRequest.getProperty("loginBrandCode");
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			String year = httpRequest.getProperty("year");
			String month = httpRequest.getProperty("month");
			String day = httpRequest.getProperty("day");

		    
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("itemCode", itemCode);
			findObjs.put("warehouseCode", warehouseCode);
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("year", year);
			findObjs.put("month", month);
			findObjs.put("day", day);

			List<ImOrderOnHandModifyView> imOrderOnHandModify = (List<ImOrderOnHandModifyView>) imItemOnHandViewDAO.findOnHandOrderModifyQuantity(findObjs,
					iSPage, iPSize, ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			
			log.info("ImOrderOnHandModifyView.size" + imOrderOnHandModify.size());
			
			for(int i = 0; i < imOrderOnHandModify.size(); i++){
				ImDeliveryHead imDeliveryHead = imDeliveryHeadDAO.findDeliveryByIdentification(brandCode,imOrderOnHandModify.get(i).getId().getOrderTypeCode() ,imOrderOnHandModify.get(i).getId().getOrderNo());
				if(imDeliveryHead != null){
					imOrderOnHandModify.get(i).setTransactionSeqNo(imDeliveryHead.getTransactionSeqNo());
				}
			}

			
			if (imOrderOnHandModify != null && imOrderOnHandModify.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX 
				log.info("firstIndex = " + firstIndex);	
				
				Long maxIndex = (Long) imItemOnHandViewDAO.findOnHandOrderModifyQuantity(findObjs, -1, -1,
						ImItemOnHandViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("ImOrderOnHandModifyView maxIndex = " + maxIndex);
				
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES_ORDER_MODIFY, GRID_SEARCH_FIELD_DEFAULT_VALUES_ORDER_MODIFY,
						imOrderOnHandModify, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("ImOrderOnHandModifyView.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES_ORDER_MODIFY,
						GRID_SEARCH_FIELD_DEFAULT_VALUES_ORDER_MODIFY, map, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	
	
	public void saveTmp(Properties httpRequest) throws Exception {
		log.info("---------------------saveTmp---------------------");
		Map returnMap = new HashMap(0); 
		try {
			// ======================帶入Head的值=========================
			log.info("---------------------取頁面值---------------------");
			String itemCodeOrEanCode = httpRequest.getProperty("itemCodeOrEanCode");// 品牌代號
			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String startItemCode = httpRequest.getProperty("startItemCode");// 單別
			String endItemCode = httpRequest.getProperty("endItemCode");
			String itemCName = httpRequest.getProperty("itemCName");
			String startWarehouseCode = httpRequest.getProperty("startWarehouseCode");// 出貨庫別
			String endWarehouseCode = httpRequest.getProperty("endWarehouseCode");
			String startLotNo = httpRequest.getProperty("startLotNo");
			String endLotNo = httpRequest.getProperty("endLotNo");// 轉入庫別
			String showZero = httpRequest.getProperty("showZero");
			String showMinus = httpRequest.getProperty("showMinus"); // 負庫存
			String itemCategory = httpRequest.getProperty("itemCategory");
			String itemBrand = httpRequest.getProperty("itemBrand");
			String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
			String itemCodeList = httpRequest.getProperty("itemCodeList"); // 多品號
			String onHandTimeScope = httpRequest.getProperty("onHandTimeScope"); 
			String category01 = httpRequest.getProperty("category01"); 
			String category02 = httpRequest.getProperty("category02"); 
			String category03 = httpRequest.getProperty("category03"); 

			log.info("---------------------取完頁面值---------------------");
			boolean flag = false;
			String warehouseRange = "'999999'";

			log.info("---------------------取品號---------------------");
			if (itemCodeList != null && !"".equals(itemCodeList)) {
				itemCodeList = itemCodeList.replaceAll(" ", ""); // 把空白鑑移除
				itemCodeList = itemCodeList.replaceAll(",", "','"); // 前端傳回的資料僅用逗號分隔，資料庫查詢需加上單引號
				itemCodeList = "'" + itemCodeList + "'";
			}
			//轉品號
			log.info("---------------------取完品號---------------------");
			log.info("---------------------國際碼轉換---------------------");
			if("eanCode".equals(itemCodeOrEanCode)){
				startItemCode = transferToItemCode(startItemCode);
				log.info(startItemCode);
				endItemCode="";
				itemCodeList="";
			}
			log.info("---------------------國際碼轉換完成---------------------");
			log.info("---------------------取庫別---------------------");
			String[] warehouseList = warehouseEmployee.split(",");
			
			for(int i = 0; i < warehouseList.length; i++){
				log.info(warehouseList[i]);
				if(warehouseList[i].equals(startWarehouseCode)){
					flag = true;
				}
				
				if(flag){
					warehouseRange = warehouseRange + ",'" + warehouseList[i] + "'";
				}
				else if(!StringUtils.hasText(startWarehouseCode) && !StringUtils.hasText(endWarehouseCode)){
					warehouseRange = warehouseRange + ",'" + warehouseList[i] + "'";
				}
				
				if(warehouseList[i].equals(endWarehouseCode)){
					flag = false;
				}
			}
			log.info("---------------------取庫別完成---------------------");
			log.info("warehouseRange:"+warehouseRange+"--------------------------");

			log.info("---------------------條件塞入MAP---------------------");
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("startItemCode", startItemCode);
			findObjs.put("endItemCode", endItemCode);
			findObjs.put("itemCName", itemCName);
			findObjs.put("startWarehouseCode", startWarehouseCode);
			findObjs.put("endWarehouseCode", endWarehouseCode);
			findObjs.put("startLotNo", startLotNo);
			findObjs.put("endLotNo", endLotNo);
			findObjs.put("showZero", showZero);
			findObjs.put("showMinus", showMinus); // 負庫存
			findObjs.put("itemCategory", itemCategory);
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("category01", category01);
			findObjs.put("category02", category02);
			findObjs.put("category03", category03);
			findObjs.put("itemCodeList", itemCodeList); // 多品號
			findObjs.put("warehouseRange", warehouseRange);
			findObjs.put("onHandTimeScope", onHandTimeScope);
			log.info("---------------------條件塞入MAP完成---------------------");
			
			imItemOnHandViewDAO.insertImItemOnHandView(findObjs);
			

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}
	
	/*private void saveIntoTmp(List<ImItemOnHandViewSimplify> imItemOnHandViews, String onHandTimeScope) throws Exception {
		log.info("saveIntoTmp");
		try{
			for(ImItemOnHandViewSimplify imItemOnHandViewSimplify : imItemOnHandViews){ 
				
				ImItemOnHandTmp imItemOnHandTmp = new ImItemOnHandTmp();
				ImItemOnHandTmpId imItemOnHandTmpId = new ImItemOnHandTmpId();
				imItemOnHandTmpId.setBrandCode(imItemOnHandViewSimplify.getId().getBrandCode());
				imItemOnHandTmpId.setItemCode(imItemOnHandViewSimplify.getId().getItemCode());
				imItemOnHandTmpId.setLotNo(imItemOnHandViewSimplify.getId().getLotNo());
				imItemOnHandTmpId.setWarehouseCode(imItemOnHandViewSimplify.getId().getWarehouseCode());
				imItemOnHandTmpId.setOnHandTimeScope(onHandTimeScope);
				
				imItemOnHandTmp.setId(imItemOnHandTmpId);
				imItemOnHandTmp.setOrganizationCode(imItemOnHandViewSimplify.getOrganizationCode());
				imItemOnHandTmp.setItemCName(imItemOnHandViewSimplify.getItemCName());
				imItemOnHandTmp.setWarehouseName(imItemOnHandViewSimplify.getWarehouseName());
				imItemOnHandTmp.setStockOnHandQty(imItemOnHandViewSimplify.getStockOnHandQty());
				imItemOnHandTmp.setOutUncommitQty(imItemOnHandViewSimplify.getOutUncommitQty());
				imItemOnHandTmp.setInUncommitQty(imItemOnHandViewSimplify.getInUncommitQty());
				imItemOnHandTmp.setMoveUncommitQty(imItemOnHandViewSimplify.getMoveUncommitQty());
				imItemOnHandTmp.setOtherUncommitQty(imItemOnHandViewSimplify.getOtherUncommitQty());
				imItemOnHandTmp.setCurrentOnHandQty(imItemOnHandViewSimplify.getCurrentOnHandQty());
				imItemOnHandTmp.setItemCategory(imItemOnHandViewSimplify.getItemCategory());
				imItemOnHandTmp.setItemBrand(imItemOnHandViewSimplify.getItemBrand());
				//imItemOnHandTmp.setOnHandTimeScope(onHandTimeScope);
				imItemOnHandViewDAO.save(imItemOnHandTmp); 
				
				log.info("finish");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("存入暫存資料表發生錯誤，原因：" + ex.toString());
			throw new Exception("存入暫存資料表失敗！");
		}
	}*/

	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys, true);
			log.info("getSearchSelection.result:" + result.size());

			if (result.size() > 0)
				pickerResult.put("imOnHandResult", result);
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

	public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		Object pickerBean = parameterMap.get("vatBeanPicker");
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object otherBean = parameterMap.get("vatBeanOther");
		String timeScope = (String) PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
		String isAllClick = (String) PropertyUtils.getProperty(otherBean, "isAllClick");
		log.info("timeScope:" + timeScope);
		log.info("isAllClick:" + isAllClick);
		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");// 品牌代號
		String startItemCode = (String) PropertyUtils.getProperty(otherBean, "startItemCode");// 單別
		String endItemCode = (String) PropertyUtils.getProperty(otherBean, "endItemCode");
		String itemCName = (String) PropertyUtils.getProperty(otherBean, "itemCName");
		String startWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "startWarehouseCode");// 出貨庫別
		String endWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "endWarehouseCode");
		String startLotNo = (String) PropertyUtils.getProperty(otherBean, "startLotNo");
		String endLotNo = (String) PropertyUtils.getProperty(otherBean, "endLotNo");// 轉入庫別
		String showZero = (String) PropertyUtils.getProperty(otherBean, "showZero");
		String showMinus = (String) PropertyUtils.getProperty(otherBean, "showMinus"); // 負庫存

		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("startItemCode", startItemCode);
		findObjs.put("endItemCode", endItemCode);
		findObjs.put("itemCName", itemCName);
		findObjs.put("startWarehouseCode", startWarehouseCode);
		findObjs.put("endWarehouseCode", endWarehouseCode);
		findObjs.put("startLotNo", startLotNo);
		findObjs.put("endLotNo", endLotNo);
		findObjs.put("showZero", showZero);
		findObjs.put("showMinus", showMinus);
		List allDataList = (List) imItemOnHandViewDAO.findPageLine(findObjs, -1, -1,
				imItemOnHandViewDAO.QUARY_TYPE_SELECT_ALL).get("form");

		if (allDataList.size() > 0)
			AjaxUtils.updateAllResult(timeScope, isAllClick, allDataList);

		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}

	/**
	 * 取得頁面條件下的可用庫存（明細匯出）
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public SelectDataInfo getImItemOnHandData(HttpServletRequest httpRequest) throws Exception {

		try {
			// ===================== 查詢參數值 =========================
			String brandCode = httpRequest.getParameter("loginBrandCode");// 品牌代號
			String startItemCode = httpRequest.getParameter("startItemCode");// 單別
			String endItemCode = httpRequest.getParameter("endItemCode");
			String itemCName = httpRequest.getParameter("itemCName");
			String startWarehouseCode = httpRequest.getParameter("startWarehouseCode");// 出貨庫別
			String endWarehouseCode = httpRequest.getParameter("endWarehouseCode");
			String startLotNo = httpRequest.getParameter("startLotNo");
			String endLotNo = httpRequest.getParameter("endLotNo");// 轉入庫別
			String showZero = httpRequest.getParameter("showZero");
			String showMinus = httpRequest.getParameter("showMinus"); // 負庫存
			String itemCategory = httpRequest.getParameter("itemCategory");
			String itemBrand = httpRequest.getParameter("itemBrand");
			String category01 = httpRequest.getParameter("category01");
			String category02 = httpRequest.getParameter("category02");
			String category03 = httpRequest.getParameter("category03");
			String category04 = httpRequest.getParameter("category04");
			String category07 = httpRequest.getParameter("category07");
			String category09 = httpRequest.getParameter("category09");
			String category13 = httpRequest.getParameter("category13");
			String taxType = httpRequest.getParameter("taxType");
			String category17 = httpRequest.getParameter("category17"); // 廠商
			String itemCodeList = httpRequest.getParameter("itemCodeList"); // 多品號
			if (itemCodeList != null && !"".equals(itemCodeList)) {
				itemCodeList = itemCodeList.replaceAll(" ", ""); // 把空白鑑移除
				itemCodeList = itemCodeList.replaceAll(",", "','"); // 前端傳回的資料僅用逗號分隔，資料庫查詢需加上單引號
				itemCodeList = "'" + itemCodeList + "'";
			}
			
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("startItemCode", startItemCode);
			findObjs.put("endItemCode", endItemCode);
			findObjs.put("itemCName", itemCName);
			findObjs.put("startWarehouseCode", startWarehouseCode);
			findObjs.put("endWarehouseCode", endWarehouseCode);
			findObjs.put("startLotNo", startLotNo);
			findObjs.put("endLotNo", endLotNo);
			findObjs.put("showZero", showZero);
			findObjs.put("showMinus", showMinus); // 負庫存
			findObjs.put("itemCategory", itemCategory);
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("category01", category01);
			findObjs.put("category02", category02);
			findObjs.put("category03", category03);
			findObjs.put("category04", category04); //尺寸
			findObjs.put("category07", category07); //性別
			findObjs.put("category09", category09); //系列
			findObjs.put("category13", category13); //款式
			findObjs.put("taxType", taxType);
			findObjs.put("category17", category17); // 加入廠商代號的條件
			findObjs.put("itemCodeList", itemCodeList); // 多品號
			// ==============================================================

			List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) imItemOnHandViewDAO.findPageLine(findObjs,
					ImItemOnHandViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			// 可用庫存excel表的欄位順序
			Object[] object = new Object[] { 
				"indexNo", "itemCode", "itemCName", "warehouseCode", 
				"warehouseName", "lotNo", "itemBrand", "itemBrandName", 
				"unitPrice", "stockOnHandQty", "outUncommitQty", "inUncommitQty",
				"moveUncommitQty", "otherUncommitQty", "currentOnHandQty", "category01", 
				"category01Name", "category02", "category02Name","category03",
				"category03Name", "category17", "supplierName", "supplierItemCode",
				"itemEName", "boxCapacity", "category07","category09", 
				"category13"
			};

			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
			List rowData = new ArrayList();
			log.info("imItemOnHandViews.size() = " + imItemOnHandViews.size());
			
			for (int i = 0; i < imItemOnHandViews.size(); i++) {
				Object[] dataObject = new Object[object.length];
				for (int j = 0; j < object.length; j++) {
					Object vo = new Object();
					ImItemOnHandView iov = imItemOnHandViews.get(i);
					if (object[j].equals("indexNo"))
						vo = i + 1;
					else
						vo = PropertyUtils.getProperty(iov, GRID_SEARCH_FIELD_NAMES[j].toString());
					String actualValue = null;
					if (vo != null) {
						actualValue = vo.toString();
					}
					dataObject[j] = actualValue;
				}
				rowData.add(dataObject);
			}
			
			return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			log.error("載入頁面顯示的庫存查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的庫存查詢失敗！");
		}
	}

	/**
	 * 匯出條碼
	 *
	 * @param httpRequest
	 */
	public void exportBarCode(HttpServletRequest httpRequest) throws Exception {
		try {
			String loginBrandCode = httpRequest.getParameter("loginBrandCode");
			String warehouseCode = httpRequest.getParameter("warehouseCode");
			HttpSession httpSession = httpRequest.getSession();
			if (httpSession != null) {
				List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) imItemOnHandViewDAO.findBarCode(
						loginBrandCode, warehouseCode);
				List<ImItemOnHandView> newList = new ArrayList();
				for (ImItemOnHandView imItemOnHandView : imItemOnHandViews) {
					DecimalFormat df1 = new DecimalFormat( "#,###,###,###,##0" );
					//System.out.println("售價-------> " + imItemOnHandView.getUnitPrice());
					//System.out.println("數量-------> " + imItemOnHandView.getCurrentOnHandQty());
					imItemOnHandView.setUnitPrice_format(df1.format(imItemOnHandView.getUnitPrice()));
					imItemOnHandView.setCurrentOnHandQty_format(df1.format(imItemOnHandView.getCurrentOnHandQty()));
					newList.add(imItemOnHandView);
				}
				httpSession.setAttribute("detailEntityBeans", newList);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("可用庫存匯出條碼發生錯誤，原因：" + ex.toString());
			throw new Exception("可用庫存匯出條碼發生錯誤：" + ex.getMessage());
		}
	}
	
	
	 public String transferToItemCode(String startItemCode){

			String sql = "";
			StringBuffer newCodes = new StringBuffer();
			StringBuffer sqlCodes = new StringBuffer();

			sql = sql +" SELECT E.ITEM_CODE " +
					" FROM IM_ITEM_EANCODE E " +
					" WHERE 1=1 AND ENABLE ='Y'";

			if(StringUtils.hasText(startItemCode)){ // 已多品號或國際碼為主
				sql = sql+" AND E.EAN_CODE = '"+startItemCode+"'";	
			}
			else{
				sql = sql+" AND 1=2 ";
			}
		    List results = nativeQueryDAO.executeNativeSql(sql);
		    
			return results.get(0).toString();
		    }
	
	public Map executeMainInitial(Map parameterMap) throws Exception{
		log.info("executeMainInitial");
		Map resultMap = new HashMap(0);
		Calendar now = Calendar.getInstance();
		String year;
		String month;

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			ImItemOnHandView imItemOnHandView = this.executeFindActualImItemOnHandView(parameterMap);
			ImDeliveryDetailCountView imDeliveryDetailCountView = this.executeFindActualImDeliveryDetailCountView(parameterMap);
			
			now.add(Calendar.MONTH, -1);
			year = String.format("%04d", now.get(Calendar.YEAR));
			month = String.format("%02d", now.get(Calendar.MONTH)+1);//0~11
			parameterMap.put("year", year);
			parameterMap.put("month", month);
			
			Double beginningOnHandQuantity = this.executeFindBeginningOnHandQuantity(parameterMap);
			
			resultMap.put("imItemOnHandView", imItemOnHandView);
			resultMap.put("imDeliveryDetailCountView", imDeliveryDetailCountView);
			resultMap.put("beginningOnHandQuantity", beginningOnHandQuantity);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("商品庫存主檔初始化失敗，原因：" + ex.toString());
			throw new Exception("商品庫存主檔初始化失敗，原因：" + ex.toString());
		}
	}
	
	public ImItemOnHandView executeFindActualImItemOnHandView(Map parameterMap)throws FormException, Exception {
		log.info("executeFindActualImItemOnHandView");
		Object otherBean = parameterMap.get("vatBeanOther");
		ImItemOnHandView imItemOnHandView = null;
		
		try {
			String itemCode = (String) PropertyUtils.getProperty(otherBean , "itemCode");
			String warehouseCode = (String) PropertyUtils.getProperty(otherBean , "warehouseCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean , "loginBrandCode");
			log.info(warehouseCode + "ffff" + itemCode);
			if(!StringUtils.hasText(itemCode) || !StringUtils.hasText(warehouseCode)){
				//imItemOnHandView = this.executeNew();
			}
			else{
				log.info(warehouseCode + "  " + itemCode);
				imItemOnHandView = findImItemOnHandViewById(itemCode, brandCode, warehouseCode, "000000000000");
				List<ImMovementView> imMovementViews = (List<ImMovementView>)imItemOnHandViewDAO.findMoveIn(itemCode, warehouseCode);
				
				if(imMovementViews.isEmpty()){
					imItemOnHandView.setIsMoveIn("N");
				}else{
					imItemOnHandView.setIsMoveIn("Y");
				}
			}
			
			log.info("ItemCName:"+imItemOnHandView.getItemCName());

			parameterMap.put( "entityBean", imItemOnHandView);
			return imItemOnHandView;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("取得庫存資料主檔失敗,原因:" + e.toString());
			throw new Exception("取得庫存資料主檔失敗,原因:" + e.toString());
		}
	}
	
	public ImDeliveryDetailCountView executeFindActualImDeliveryDetailCountView(Map parameterMap)throws FormException, Exception {
		log.info("executeFindActualImDeliveryDetailCountView");
		Object otherBean = parameterMap.get("vatBeanOther");
		ImDeliveryDetailCountView imDeliveryDetailCountView = null;
		
		Calendar now = Calendar.getInstance();
		//String shipDay = "19";
		//String shipMonth = "11";
		//String shipYear = "2016";
		
		String shipDay = String.format("%02d", now.get(Calendar.DATE));
		String shipMonth = String.format("%02d", now.get(Calendar.MONTH)+1);
		String shipYear = String.format("%04d", now.get(Calendar.YEAR));
		
		log.info("當天日期:"+shipYear+shipMonth+shipDay);
		
		try { 
			String itemCode = (String) PropertyUtils.getProperty(otherBean , "itemCode");
			String warehouseCode = (String) PropertyUtils.getProperty(otherBean , "warehouseCode");
			//String brandCode = (String) PropertyUtils.getProperty(otherBean , "loginBrandCode");

			if(!StringUtils.hasText(itemCode) || !StringUtils.hasText(warehouseCode)){
				//imItemOnHandView = this.executeNew();
			}
			else{
				imDeliveryDetailCountView = findImDeliveryDetailCountViewById(itemCode, warehouseCode, shipDay, shipMonth, shipYear);
			}

			parameterMap.put( "entityBean", imDeliveryDetailCountView);
			return imDeliveryDetailCountView;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("取得銷售量主檔失敗,原因:" + e.toString());
			throw new Exception("取得銷售量主檔失敗,原因:" + e.toString());
		}
	}
	
	public Double executeFindBeginningOnHandQuantity(Map parameterMap) throws Exception{
		Object otherBean = parameterMap.get("vatBeanOther");
		
		try{
			String itemCode = (String) PropertyUtils.getProperty(otherBean , "itemCode");
			String warehouseCode = (String) PropertyUtils.getProperty(otherBean , "warehouseCode");
			String brandCode = (String) PropertyUtils.getProperty(otherBean , "loginBrandCode");
			String year = (String)parameterMap.get("year");
			String month = (String)parameterMap.get("month");
			Double result = 0.0;
			log.info("year+month"+year+month);
			//ImMonthlyBalanceLine imMonthlyBalanceLine = imMonthlyBalanceLineDAO.findById(brandCode, itemCode, warehouseCode, "000000000000", "2017", "06");
			ImMonthlyBalanceLine imMonthlyBalanceLine = imMonthlyBalanceLineDAO.findById(brandCode, itemCode, warehouseCode, "000000000000", year, month);
			if(imMonthlyBalanceLine != null){
				result = imMonthlyBalanceLine.getEndingOnHandQuantity();
			}else{
				result = 0.0;
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("取得期初庫存失敗,原因:" + e.toString());
			throw new Exception("取得期初庫存失敗,原因:" + e.toString());
		}
	}
	
	
	public void test(Map parameterMap)  throws Exception {
		/******
		Connection conn = null;
		Statement stmt = null;
		String sqlString = "";
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			log.info("連接成功MySQLToJava");
			conn = DriverManager.getConnection("jdbc:mysql://10.1.5.30:3306/yaotest","root","");
			log.info("連接成功MySQL");
			stmt = conn.createStatement();
			sqlString = "SELECT * FROM new_table";
			stmt.execute(sqlString);
			ResultSet rs = stmt.getResultSet();
			while(rs.next()){
				log.info(rs.getString("C1"));
			}
			
		}catch(Exception e){
			log.info("?????????????????");
		}
		**********/
		
		
		
		
		
		
		log.info("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		CallableStatement calStmt = null;
		//OracleCallableStatement calStmt = null;
		Connection conn = null;
		//ResultSet rs = null;
		
		try{
		conn = dataSource.getConnection();
		//製作ERP_COMMAND
		calStmt = conn.prepareCall("{call ERP.APP_IM_ON_HAND_PACKAGE.start_for_T2(?,?,?,?,?)}"); // 呼叫store procedure 
		log.info("cccccccccccccccccccccccccccccccccccc");
		calStmt.setString(1, "");
		log.info("dddddddddddddddddddddddddddddddddd");
		calStmt.setString(2, "");
		log.info("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
		calStmt.setString(3, "");
		log.info("ffffffffffffffffffffffffffff");
		calStmt.setString(4, "");
		log.info("ggggggggggggggggggggggggggggg");
		calStmt.setString(5, "000000000000");
		log.info("hhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		//calStmt.registerOutParameter(6, OracleTypes.CURSOR);
		calStmt.execute();
		//rs = calStmt.getResultSet();
		
		log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}catch (SQLException e) {
			log.error("createE2PCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("createE2PCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("createE2PCommand() 呼叫createE2PCommand發生錯誤，原因：" + ex.getMessage());
			throw new Exception("createE2PCommand() 呼叫SQL發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
	public List<Properties> getBeginningOnHand(Properties httpRequest) throws Exception {
		try{
			List<Properties> result = new ArrayList();
			Properties properties = new Properties();
			
			String itemCode = httpRequest.getProperty("itemCode");
			String warehouseCode = httpRequest.getProperty("warehouseCode");
			String brandCode = httpRequest.getProperty("loginBrandCode");
			String year = httpRequest.getProperty("year");
			String month = httpRequest.getProperty("month");
			String forwardYear = "";
			String forwardMonth = "";
			
			//判斷上個月是否有跨年
			if("01".equals(month)){
				forwardYear = String.format("%04d", Integer.valueOf(year)-1);
				forwardMonth = "12";
			}else{
				forwardYear = String.format("%04d", Integer.valueOf(year));
				forwardMonth = String.format("%02d", Integer.valueOf(month)-1);
			}

			ImMonthlyBalanceLine imMonthlyBalanceLine = imMonthlyBalanceLineDAO.findById(brandCode, itemCode, warehouseCode, "000000000000", forwardYear, forwardMonth);
			properties.setProperty("beginningOnHand", imMonthlyBalanceLine.getEndingOnHandQuantity().toString());

			result.add(properties);
			return result;
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("取得期初庫存失敗，原因：" + ex.toString());
			throw new Exception("取得期初庫存失敗失敗！");
		}
		
	}
	/*
	public ImItemOnHandView findById(String itemCode, String warehouseCode) throws Exception {
		ImItemOnHandView result;
		try {
			 //id = id.trim().toUpperCase();
			 ImItemOnHandViewId imItemOnHandViewId = new ImItemOnHandViewId(itemCode, brandCode, warehouseCode, "000000000000");
			 result = (ImItemOnHandView)imItemOnHandViewDAO.findById("ImItemOnHandView",imItemOnHandViewId);
			 return result;
		 } catch (Exception ex) {
			 log.error("商品代號:" + itemCode + "與庫別：" + warehouseCode + "查詢庫存資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("商品代號:" + itemCode + "與庫別：" + warehouseCode + "查詢庫存資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	}*/

}