package tw.com.tm.erp.hbm.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
import tw.com.tm.erp.hbm.bean.TmpBarcode;
import tw.com.tm.erp.hbm.bean.TmpBarcodeExcel;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
 
public class GenerateBarCodeService {
    private static final Log log = LogFactory.getLog(GenerateBarCodeService.class);

    public static String TMP01 = "TMP01";
    public static String TMP02 = "TMP02";
    public static String PA = "PA";
    private BaseDAO baseDAO;
    private NativeQueryDAO nativeQueryDAO;
    private ImItemDAO imItemDAO;
    private ImItemCategoryDAO imItemCategoryDAO;
    private TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    private ImWarehouseDAO imWarehouseDAO;

    private BuBrandService buBrandService;
    private BuCommonPhraseService buCommonPhraseService;
    private ImPriceListService imPriceListService;
    private TmpAjaxSearchDataService tmpAjaxSearchDataService;

    public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
        this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }
    public void setTmpAjaxSearchDataDAO(TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO) {
        this.tmpAjaxSearchDataDAO = tmpAjaxSearchDataDAO;
    }
    public void setImItemDAO(ImItemDAO imItemDAO) {
        this.imItemDAO = imItemDAO;
    }
    public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
        this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
    }
    public void setBaseDAO(BaseDAO baseDAO) {
    	this.baseDAO = baseDAO;
    }
    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
    	this.nativeQueryDAO = nativeQueryDAO;
    }
    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
    	this.imItemCategoryDAO = imItemCategoryDAO;
    }
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}
    public void setBuBrandService(BuBrandService buBrandService) {
    	this.buBrandService = buBrandService;
    }
    public void setImPriceListService(ImPriceListService imPriceListService) {
    	this.imPriceListService = imPriceListService;
    }
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
    	this.buCommonPhraseService = buCommonPhraseService;
    }
    
    
    /**
     *  進貨單明細
     */
    public static final String[] RECEIVE_GRID_FIELD_NAMES = {
	"itemCode",		"cateogry02",         // "indexNo",
	"itemBrand",	"category13",	"unitPrice",
	"quantity",  	"itemCName",	"declartionDate",	"description"
    };
    public static final String[] RECEIVE_GRID_FIELD_DEFAULT_VALUES = {
	"",   	"",   // "0",
	"", 	"", "0.0",
	"0.0", 	"",	"", ""
    };

    /**
     *  進貨單明細 - 外標(標籤比較大)
     */
    public static final String[] RECEIVE02_GRID_FIELD_NAMES = {
	"itemCode",		"itemCName",         // "indexNo",
	"cateogry02",	"cateogry02Name",	"boxCapacity",
	"quantity",		"supplierItemCode",	"declartionDate", "description"
    };
    public static final String[] RECEIVE02_GRID_FIELD_DEFAULT_VALUES = {
	"",   	"",   // "0",
	"", 	"", 	"0.0",
	"0",	"",		"", 	""
    };

    /**
     * 變價單明細
     */
    public static final String[] PAJ_GRID_FIELD_NAMES = {
	"orderNo",	"enableDate",	"warehouseCode",	"itemCode",	"category02",  // "indexNo",
	"category02Name", "itemBrand",	"category13", 	"originalPrice", 
	"PAunitPrice", "currentOnHandQty", "PAitemCName", "status", "description2"
    };

    public static final String[] PAJ_GRID_FIELD_DEFAULT_VALUES = {
	"",	"",	"",	"", "",
	"", "", "", "0.0",	
	"0.0", "", "", "", ""
    };

    /**
     * 定變價單明細(含報單日期)
     */
    public static final String[] PAJ_2_GRID_FIELD_NAMES = {
	"orderNoDecl",			"enableDateDecl",		"customerWarehouseCode", 	"warehouseCodeDecl",	"itemCodeDecl",
	"category02Decl",   		"itemBrandDecl",		"category13Decl", 		"originalPriceDecl",	"PAunitPriceDecl",
	"imCurrentOnHandQtyDecl", 	"cmCurrentOnHandQtyDecl", 	"PAitemCNameDecl", 		"declarationNo",	"declarationSeq",
	"declDate",			"statusDecl", "itemNo",	"description1"
    };

    public static final String[] PAJ_2_GRID_FIELD_DEFAULT_VALUES = {
	"",	"",	"", 	"",	"",
	"",	"", 	"", 	"0.0",	"0.0",
	"",	"",	"",	"",	"",
	"",	"", "0", ""
    };

    /**
     * 調撥單明細
     */
    public static final String[] MOVEMENT_GRID_FIELD_NAMES = {
	"arrivalWarehouseCode",	"deliveryWarehouseCode",
	"originalOrderNo",	"boxNo",    	"taxType",
	"deliveryQuantity",	"boxCount", 	"deliveryDate"
    };

    public static final String[] MOVEMENT_GRID_FIELD_DEFAULT_VALUES = {
	"",   	"",
	"", 	"", 	"",
	"0.0", 	"0",	""
    };

    /**
     * 調撥單食品菸明細
     */
    public static final String[] MOVEMENT03_GRID_FIELD_NAMES = {
	"itemCode",		"itemCName",        "boxCapacity",
	"indexNo",		"deliveryDate",     "lotNo",	"description"
    };

    public static final String[] MOVEMENT03_GRID_FIELD_DEFAULT_VALUES = {
	"",   	"",  	"0.0",
	"", 	"", 	"",		""
    };

    /**
     * 調撥單化妝品精品明細
     */
    public static final String[] MOVEMENT06_GRID_FIELD_NAMES = {
	"originalOrderNo",		"indexNo",   "weight",
	"deliveryQuantity",		"boxNo",     "boxCount",	"description"
    };

    public static final String[] MOVEMENT06_GRID_FIELD_DEFAULT_VALUES = {
	"",   	"0",  	"0.0",
	"0", 	"", 	"0",	""
    };

    /**
     * 商品主檔明細(暫存)
     */
    public static final String[] ITEM_TMP_GRID_FIELD_NAMES = {
    "imIndexNo", "imItemCode", "imUnitPrice", 
    "paper", "imItemCName","beginDate","imItemDesc"
    };

    public static final String[] ITEM_TMP_GRID_FIELD_DEFAULT_VALUES = {
	"",	"", "",
	"",	"", "", ""
    };

    /**
     * 商品主檔明細(建檔)
     */
    public static final String[] ITEM_GRID_FIELD_NAMES = {
	"itemCodeBuilder",	"unitPriceBuilder",	"paperBuilder",
	"itemCNameBuilder", "description"
    };

    public static final String[] ITEM_GRID_FIELD_DEFAULT_VALUES = {
	"",	"0",   	"",
	"", ""
    };

    /**
     * 調撥單轉補條碼
     */
    public static final String[] MOVEMENT07_GRID_FIELD_NAMES = {
	"itemCodeMovement",	"unitPriceMovement",	"paperMovement",
	"itemCNameMovement", "description"
    };

    public static final String[] MOVEMENT07_GRID_FIELD_DEFAULT_VALUES = {
	"",	"0", "",
	"", ""
    };

    /**
     * 進貨調整單(T2A)
     */
    public static final String[] ADJUSTRECEIVE_GRID_FIELD_NAMES = {
	"itemCode",	"category02", "itemBrand", "category13", "unitPrice",
	"quantity", "itemCName", "creationDate", "description"
    };

    public static final String[] ADJUSTRECEIVE_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "", "", "0",
	"", "", "", ""
    };

    /**
     * 進貨調整單外標(T2A)
     */
    public static final String[] ADJUSTRECEIVE02_GRID_FIELD_NAMES = {
	"itemCode",	"itemCName", "adjustDate", "seq", "boxCapacity",
	"declNo", "quantity", "description"
	};

    public static final String[] ADJUSTRECEIVE02_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "", "", "0",
	"", "", ""
    };

	/**
	 * 進貨調整單-效期外標(T2A) by Weichun 2011.01.10
	 * 格式：原廠貨號, 品名, 效期, 品號, 裝箱量 , 項次, 張數, 進貨量, 進貨日期, 說明
	 */
	public static final String[] ADJUSTRECEIVE03_GRID_FIELD_NAMES = { "supplierItemCode", "itemCName", "expireDate",
			"itemCode", "boxCapacity", "indexNo", "quantity", "difQuantity", "adjustDate", "description" };

	public static final String[] ADJUSTRECEIVE03_GRID_FIELD_DEFAULT_VALUES = { "", "", "", "", "0", "", "0", "0", "", "" };

    /**
     * 取得headId
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXHeadId(Properties httpRequest)throws Exception{
	List re = new ArrayList();
	Properties pro = new Properties();
	String headId = "";
	String headIds = "";
	String errorMsg = "";
	try{
	    pro.setProperty("headId", headId);
	    pro.setProperty("headIds", headIds);
	    pro.setProperty("errorMsg", errorMsg);
	    re.add(pro);
	}catch(Exception e){
	    log.error("取得headId發生錯誤，原因：" + e.toString());
	    throw new Exception("取得headId發生錯誤，原因：" + e.toString());
	}
	return re;
    }

    /**
     * 更新PAGE 所有的LINE
     *
     * @param httpRequest
     * @return String message
     * @throws FormException
     * @throws NumberFormatException
     */
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws NumberFormatException, FormException, Exception {
	log.info("==========<updateAJAXPageLinesData>=============");
	String errorMsg = null;
	log.info("==========</updateAJAXPageLinesData>=============");
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * AJAX Load Page Data
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException ,Exception{
	log.info("=====<getAJAXPageData>=====");
	String sql = null;

	String sqlMax = null;
	String[] gridFieldNames = null;
	String[] gridFieldDefaultValues = null;
	Integer valuesColumn = null;

	List<Properties> re = new ArrayList();
	List<Properties> gridDatas = new ArrayList();
	try{
	    Long div = NumberUtils.getLong(httpRequest.getProperty("div"));

	    Map map = getSQLByBarcode(httpRequest);

	    sql = (String)map.get("sql");
	    sqlMax = (String)map.get("sqlMax");

	    int iSPage = AjaxUtils.getStartPage(httpRequest);
	    int iPSize = AjaxUtils.getPageSize(httpRequest);

	    if( 2 == div ){ // (定)變價單
	    	gridFieldNames = PAJ_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  PAJ_GRID_FIELD_DEFAULT_VALUES;

	    }else if(  3 == div  ){ // 進貨單
	    	gridFieldNames = RECEIVE_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  RECEIVE_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 4 == div ){ // 調撥單 麥頭
	    	gridFieldNames = MOVEMENT_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  MOVEMENT_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 5 == div ){ // 商品主檔(暫存)
	    	gridFieldNames = ITEM_TMP_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  ITEM_TMP_GRID_FIELD_DEFAULT_VALUES;
	    	valuesColumn = 0;

	    }else if( 6 == div  ){ // 進貨單外標
	    	gridFieldNames = RECEIVE02_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  RECEIVE02_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 7 == div ){ // 調撥單-食品菸
	    	gridFieldNames = MOVEMENT03_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  MOVEMENT03_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 8 == div ){ // 調撥單-化妝品,精品
	    	gridFieldNames = MOVEMENT06_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  MOVEMENT06_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 9 == div ){ // 變價單(含報單日期)
	    	gridFieldNames = PAJ_2_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  PAJ_2_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 10 == div ){ // 商品主檔(建檔)
	    	gridFieldNames = ITEM_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  ITEM_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 11 == div ){ // 調撥單轉補條碼
	    	gridFieldNames = MOVEMENT07_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  MOVEMENT07_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 12 == div ){ // 進貨調整單(T2A)
	    	gridFieldNames = ADJUSTRECEIVE_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  ADJUSTRECEIVE_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 13 == div ){ // 進貨調整單外標(T2A)
	    	gridFieldNames = ADJUSTRECEIVE02_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  ADJUSTRECEIVE02_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 14 == div ){ // 進貨調整單效期外標(T2A) by Weichun 2011.01.10
	    	gridFieldNames = ADJUSTRECEIVE03_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  ADJUSTRECEIVE03_GRID_FIELD_DEFAULT_VALUES;

	    }else if( 15 == div  ){ // 進貨單外標(Excel匯入) by Weichun 2011.03.02
	    	gridFieldNames = RECEIVE02_GRID_FIELD_NAMES;
	    	gridFieldDefaultValues =  RECEIVE02_GRID_FIELD_DEFAULT_VALUES;
	    	valuesColumn = 0;
	    }


	    log.info("111ddd sql.toString() =  " + sql.toString());
	    List results = nativeQueryDAO.executeNativeSql(sql.toString(), iSPage, iPSize);

	    if (null != results && results.size() > 0) {
	    	// 取得第一筆的INDEX
	    	Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // NumberUtils.getLong(((Object[])results.get(0))[0].toString());
	    	log.info(" firstIndex =  " + firstIndex);
	    	// 取得最後一筆 INDEX
	    	log.info(" sqlMax.toString() =  " + sqlMax.toString());
	    	List maxResult = nativeQueryDAO.executeNativeSql(sqlMax.toString());
	    	Long maxIndex = NumberUtils.getLong( ((Object) maxResult.get(maxResult.size()-1)).toString() );
	    	log.info(" maxIndex =  " + maxIndex);
	    	re.add(AjaxUtils.getAJAXJoinTablePageData(httpRequest, gridFieldNames, gridFieldDefaultValues, results, gridDatas,firstIndex, maxIndex, valuesColumn));

	    } else {
	    	re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, gridFieldNames, gridFieldDefaultValues, gridDatas));
	    }

	    log.info("=====<getAJAXPageData/>=====");
	    return re;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("載入頁面顯示的條碼明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的條碼單明細失敗！");
	}
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
	public SelectDataInfo getAJAXExportData(HttpServletRequest httpRequest) throws Exception {
		String sql = null;
		Object[] object = null;
		List rowData = new ArrayList();
		try {
			Long div = NumberUtils.getLong(httpRequest.getParameter("div"));
			String orderTypeCode = httpRequest.getParameter("orderTypeCode");
			String brandCode = httpRequest.getParameter("brandCode");
			String description = httpRequest.getParameter("description");

			Properties properties = new Properties();
			Enumeration paramNames = httpRequest.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String name = (String) paramNames.nextElement();
				String[] values = httpRequest.getParameterValues(name);
				if ((null != values) && (values.length > 0)) {
					String value = values[0];
					properties.setProperty(name, value);
				}
			}
			
			Map map = getSQLByBarcode(properties);

			sql = (String) map.get("sql");

			List lists = nativeQueryDAO.executeNativeSql(sql);

			// 可用庫存excel表的欄位順序
			if (2 == div) { // (定)變價單
				object = new Object[] { "orderNo", "enableDate", "warehouseCode", "itemCode", "category02", "category02Name",
						"itemBrand", "category13", "originalPrice", "unitPrice", "currentOnHandQty", "itemCName", "status", "description2" };
			} else if (3 == div) { // 進貨單
				if ("EIP".equalsIgnoreCase(orderTypeCode)) {
					object = new Object[] { "itemCode", "cateogry02", "itemBrand", "categroy13", "unitPrice", "quantity",
							"itemCName", "declarationDate", "description" }; // orderDate
				} else {
					object = new Object[] { "itemCode", "cateogry02", "itemBrand", "categroy13", "unitPrice", "quantity",
							"itemCName", "declarationDate", "description" };
				}
			} else if (4 == div) { // 調撥單 麥頭
				object = new Object[] { "arrivalWarehouseCode", "deliveryWarehouseCode", "orderNo", "boxNo", "taxType",
						"deliveryQuantity", "boxCount", "deliveryDate" };
			} else if (5 == div) { // 商品主檔(暫存)
				object = new Object[] { "itemCode", "unitPrice", "paper", "itemCName", "beginDate", "imItemDesc" };
			} else if (6 == div) { // 進貨單外標
				object = new Object[] { "itemCode", "itemCName", "categroy02", "categroy02Name", "boxCapacity", "quantity",
						"supplierItemCode", "declarationDate", "description" };
			} else if (7 == div) { // 調撥單-食品菸
				object = new Object[] { "itemCode", "itemCName", "boxCapacity", "indexNo", "deliveryDate", "lotNo", "description" };
			} else if (8 == div) { // 調撥單-化妝品,精品
				object = new Object[] { "itemCode", "itemCName", "boxCapacity", "indexNo", "deliveryDate", "lotNo", "description" };
			} else if (9 == div) { // 定變價單(含報單日期)
				object = new Object[] { "orderNo", "enableDate", "customsWarehouseCode", "warehouseCode", "itemCode",
						"category02", "itemBrand", "category13", "originalPrice", "unitPrice", "currentOnHandQty",
						"cmCurrentOnHandQty", "itemCName", "declarationNo", "declarationSeq", "declDate", "status",
						"indexNo", "description1" };
			} else if (10 == div) { // 商品主檔(建檔)
				object = new Object[] { "itemCode", "unitPrice", "paper", "itemCName", "description" };
			} else if (11 == div) { // 調撥單轉補條碼
				object = new Object[] { "itemCode", "unitPrice", "paper", "itemCName", "description" };
			} else if (12 == div) { // 進貨調整單(T2A)
				object = new Object[] { "itemCode", "category02", "itemBrand", "category13", "unitPrice", "diffQuantity",
						"itemCName", "creationDate", "description" };
			} else if (13 == div) { // 進貨調整單外標(T2A)
				object = new Object[] { "itemCode", "itemCName", "adjustmentDate", "seq", "boxCapacity", "declNo",
						"diffQuantity", "description" };
			} else if (14 == div) { // 進貨調整單效期外標(T2A) add by Weichun 2011.01.11
				object = new Object[] { "supplierItemCode", "itemCName", "expireDate", "itemCode", "boxCapacity", "indexNo",
						"quantity", "difQuantity", "adjustDate", "description" };
			}

			log.info(" lists.size() = " + lists.size());
			if (5 == div) { // 商品主檔(暫存)
				
				for (int i = 0; i < lists.size(); i++) {
					//TmpBarcode  tmpBarcode = (TmpBarcode)lists.get(i);
					Object[] getObj = (Object[]) lists.get(i);
					Object[] dataObject = new Object[object.length];
					//String beginDate = tmpBarcode.getBeginDate();
					String[] searchData = ((String) getObj[0]).split(AjaxUtils.SEARCH_KEY_DELIMITER);
					log.info("beginDate = " + searchData[5]);
					ImItem imitem = imItemDAO.findItem(brandCode, searchData[1]);
					if (null != imitem) {
						dataObject[0] = searchData[1]; // 品號
						dataObject[1] = NumberUtils.getLong(searchData[2]); // 售價
						dataObject[2] = NumberUtils.getLong(searchData[3]); // 張數
						dataObject[3] = searchData[4]; // 品名
						//dataObject[4] = beginDate;
						dataObject[4] = searchData[5];
						dataObject[5] = hasDescription(description) ? searchData[6]: "";	//說明
//						dataObject[5] = "TEST";	//說明
						rowData.add(dataObject);
					}
					

				}
			} else if (15 == div) { // 進貨單外標(Excel轉入) add by Weichun 2011.03.04
				object = new Object[] { "itemCode", "itemCName", "categroy02", "categroy02Name", "boxCapacity", "quantity",
						"supplierItemCode", "declarationDate" };
				for (int i = 0; i < lists.size(); i++) {
					Object[] getObj = (Object[]) lists.get(i);
					Object[] dataObject = new Object[object.length];
					String[] searchData = ((String) getObj[0]).split(AjaxUtils.SEARCH_KEY_DELIMITER);

					//ImItem imitem = imItemDAO.findItem(brandCode, searchData[0]);
					//if (null != imitem) {
						dataObject[0] = searchData[0]; // 品號
						dataObject[1] = searchData[1]; // 品名
						dataObject[2] = searchData[2]; // 中類
						dataObject[3] = searchData[3]; // 中類名稱
						dataObject[4] = NumberUtils.getLong(String.valueOf(searchData[4])); // 每箱數量
						dataObject[5] = NumberUtils.getLong(String.valueOf(searchData[5])); // 數量
						dataObject[6] = searchData[6]; // 廠商貨號
						dataObject[7] = ""; // 進貨日期
						rowData.add(dataObject);
					//}
				}
			}else { // 其他
				for (int i = 0; i < lists.size(); i++) {
					Object[] getObj = (Object[]) lists.get(i);
					Object[] dataObject = new Object[object.length];
					for (int j = 0; j < object.length; j++) {
						dataObject[j] = getObj[j];
					}
					rowData.add(dataObject);
				}
			}

			return new SelectDataInfo(object, rowData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("匯出excel發生錯誤，原因：" + e.toString());
			throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
		}
	}

    /**
     * 產生sql by httpRequest
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public Map getSQLByBarcode(Properties httpRequest) throws Exception{
    	StringBuffer sql = new StringBuffer();
    	StringBuffer sqlBody = new StringBuffer();
    	StringBuffer sqlMax = new StringBuffer();
    	Map map = new HashMap();
    	try {
    		String brandCode = httpRequest.getProperty("brandCode");
    		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
    		String orderNo = httpRequest.getProperty("orderNo");
    		String orderNoEnd = httpRequest.getProperty("orderNoEnd");
    		String barCodeType = httpRequest.getProperty("barCodeType");
    		String category01 = httpRequest.getProperty("category01");
    		String category02 = httpRequest.getProperty("category02");
    		String orderBy = httpRequest.getProperty("orderBy");
    		String taxType = httpRequest.getProperty("taxType");
    		String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");
    		String warehouseCode = httpRequest.getProperty("warehouseCode");
    		String showZero = httpRequest.getProperty("showZero");
    		String samePrice = httpRequest.getProperty("samePrice");
    		String startDate = httpRequest.getProperty("startDate");
    		String endDate = httpRequest.getProperty("endDate");
    		String supplierCode = httpRequest.getProperty("supplierCode");
    		String description = httpRequest.getProperty("description");
    		Long div = NumberUtils.getLong(httpRequest.getProperty("div"));
    		String timeScope = httpRequest.getProperty("timeScope");

    		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
    		if(!StringUtils.hasText(organizationCode)){
    			throw new Exception("依品牌:"+brandCode+"查無此組織代碼");
    		}

    		log.info(" brandCode = " + brandCode );
    		log.info(" orderTypeCode = " + orderTypeCode );
    		log.info(" orderNo = " + orderNo );
    		log.info(" orderNoEnd = " + orderNoEnd );
    		log.info(" barCodeType = " + barCodeType );
    		log.info(" category01 = " + category01 );
    		log.info(" category02 = " + category02 );
    		log.info(" customsWarehouseCode = " + customsWarehouseCode );
    		log.info(" warehouseCode = " + warehouseCode );
    		log.info(" showZero = " + showZero );
    		log.info(" samePrice = " + samePrice );
    		log.info(" startDate = " + startDate );
    		log.info(" endDate = " + endDate );
    		log.info(" supplierCode = " + supplierCode );
    		log.info(" description = " + description );
    		log.info(" div = " + div );
    		log.info(" timeScope = " + timeScope );
			
    		if( 2 == div ){ // (定)變價單
    			StringBuffer onHandCondition = new StringBuffer();
//  			onHandCondition.append(" AND O.ORGANIZATION_CODE = '").append(organizationCode).append("'");
    			if(StringUtils.hasText(customsWarehouseCode)){
    				if(customsWarehouseCode.length() != 0){
    				onHandCondition.append(" AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("'");//BY JASON
    			
    				}
    				//庫別
    				if(StringUtils.hasText(warehouseCode)){
    					onHandCondition.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("'");
    				}
    				sqlMax.append("select count(ITEM_CODE) AS rowCount FROM(");
    			}else{
    				sql.append("SELECT ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, CATEGORY_NAME, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, CURRENT_ON_HAND_QTY , ITEM_C_NAME, STATUS, DESCRIPTION  FROM("); // rownum,//20160413 BY JASON 
    				sql.append("SELECT INDEX_NO,ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, CATEGORY_NAME, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, SUM(CURRENT_ON_HAND_QTY) AS CURRENT_ON_HAND_QTY , ITEM_C_NAME, STATUS, DESCRIPTION  FROM(");//20160413 BY JASON INSTER INDEX_NO
    				sqlMax.append("select count(rownum) AS rowCount FROM(").append(sql);
    			}
    			//20160413 by jason
    			if(customsWarehouseCode.length()==0){
    			sql.append("SELECT L.INDEX_NO,H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, C.CATEGORY_NAME, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,20160413 BY JASON INSTER L.INDEX_NO
    			sqlMax.append("SELECT L.INDEX_NO,H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, C.CATEGORY_NAME, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
    			}else{
        			sql.append("SELECT H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, C.CATEGORY_NAME, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,20160413 BY JASON INSTER L.INDEX_NO
        			sqlMax.append("SELECT H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, C.CATEGORY_NAME, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
        			
    			}
    			//-------
    			sqlBody.append("FROM IM_PRICE_ADJUSTMENT H, IM_PRICE_LIST L, IM_ITEM I, IM_ITEM_CATEGORY C, IM_WAREHOUSE W, ")//BY JASON 增加IM_WAREHOUSE W,
    			.append("(SELECT BRAND_CODE ,WAREHOUSE_CODE,ITEM_CODE, NVL (stock_on_hand_qty, 0)- NVL (out_uncommit_qty, 0)+ NVL (in_uncommit_qty, 0)+ NVL (move_uncommit_qty, 0)+ NVL (other_uncommit_qty,0) AS CURRENT_ON_HAND_QTY FROM IM_ON_HAND) O ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE  ")
    			.append("AND I.BRAND_CODE = O.BRAND_CODE(+) ")
    			.append("AND I.ITEM_CODE = O.ITEM_CODE(+) ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
    			.append("AND I.IS_TAX = '").append(taxType).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND I.BRAND_CODE = C.BRAND_CODE(+) ")
    			.append("AND I.CATEGORY02 = C.CATEGORY_CODE(+) ")
    			.append("AND 'CATEGORY02' = C.CATEGORY_TYPE(+) ")
    			.append(onHandCondition)
    			.append(" AND W.WAREHOUSE_CODE = O.WAREHOUSE_CODE ");//BY JASON

    			if(StringUtils.hasText(orderNo)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

    			if("N".equalsIgnoreCase(showZero)){
    				sqlBody.append("AND O.CURRENT_ON_HAND_QTY <> 0 ");
    			}

    			if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
    				if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
    					.append("AND H.ENABLE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
    				}else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}
    			}

    			if(StringUtils.hasText(supplierCode)){
    				sqlBody.append("AND H.SUPPLIER_CODE = '").append(supplierCode).append("' ");
    			}

    			if(StringUtils.hasText(category01)){
    				sqlBody.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
    			}

    			if(StringUtils.hasText(category02)){
    				sqlBody.append("AND I.CATEGORY02 = '").append(category02).append("' ");
    			}

    			if(StringUtils.hasText(samePrice) && "N".equals(samePrice) && "PAJ".equals(orderTypeCode)){
    				sqlBody.append("AND L.ORIGINAL_PRICE <> L.UNIT_PRICE ");
    			}

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    			if(StringUtils.hasText(customsWarehouseCode)){
//  				sqlMax.append(")");
    			}else{
    				sql.append(") GROUP BY INDEX_NO,ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, CATEGORY_NAME, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, ITEM_C_NAME, STATUS, DESCRIPTION ");//20160413 BY JASON INSTER INDEX_NO
    				sqlMax.append(") GROUP BY INDEX_NO,ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, CATEGORY_NAME, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, ITEM_C_NAME, STATUS, DESCRIPTION ) )");//20160413 BY JASON INSTER INDEX_NO
    			}

    			if(StringUtils.hasText(orderBy)){
    				//sql.append(" order By ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, CATEGORY02"); // rownum
    				//20160413 by jason
    				if(orderBy.equals("L.ITEM_CODE ASC"))
    					sql.append(" order By ITEM_CODE ASC");
    				else if(orderBy.equals("L.ITEM_CODE DESC"))
    					sql.append(" order By ITEM_CODE DESC");
    				else if(orderBy.equals("L.INDEX_NO ASC"))
    					sql.append(" order By INDEX_NO ASC");
    				else if(orderBy.equals("L.INDEX_NO DESC"))
    					sql.append(" order By INDEX_NO DESC");
    				//-----
    			}

    			if(!StringUtils.hasText(customsWarehouseCode)){
    				sql.append(") ");
    			}else{
    				sqlMax.append(") ");
    			}
    		}else if(  3 == div  ){ // 進貨單
    			if("EIP".equalsIgnoreCase(orderTypeCode)){
    				sql.append("SELECT L.ITEM_CODE, NVL(I.CATEGORY02,'') AS CATEGORY02, NVL(I.ITEM_BRAND,'') AS ITEM_BRAND, NVL(I.CATEGORY13,'') AS CATEGORY13, NVL(I.UNIT_PRICE, '') AS UNIT_PRICE, L.QUANTITY, NVL(I.ITEM_C_NAME, '') AS ITEM_C_NAME, H.CREATION_DATE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
    			}else{
    				sql.append("SELECT L.ITEM_CODE, NVL(I.CATEGORY02,'') AS CATEGORY02, NVL(I.ITEM_BRAND,'') AS ITEM_BRAND, NVL(I.CATEGORY13,'') AS CATEGORY13, NVL(I.UNIT_PRICE, '') AS UNIT_PRICE, L.QUANTITY, NVL(I.ITEM_C_NAME, '') AS ITEM_C_NAME, H.DECLARATION_DATE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
    			}

    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM L, IM_ITEM_CURRENT_PRICE_VIEW I ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO = '").append(orderNo).append("' ")
    			.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(category02)){
    				sqlBody.append("AND I.CATEGORY02 = '").append(category02).append("' ");
    			}

    			if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
    				if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
    					.append("AND H.WAREHOUSE_IN_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
    				}else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}
    			}

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    			if(StringUtils.hasText(orderBy)){
    				sql.append(" order By ").append(orderBy); // // rownum
//  				sqlMax.append(" order By ").append(orderBy);
    			}
    		}else if( 4 == div ){ // 調撥單 麥頭

    			sql.append("SELECT H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO AS ORDER_NO, L.BOX_NO, H.TAX_TYPE, SUM(L.DELIVERY_QUANTITY) AS DELIVERY_QUANTITY, H.BOX_COUNT, H.DELIVERY_DATE ");
    			sqlMax.append("SELECT count(rownum) as rowCount FROM( ")
    			.append("SELECT H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO, H.TAX_TYPE,H.BOX_COUNT, H.DELIVERY_DATE ");

    			sqlBody.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}
    			sqlBody.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    			sql.append("GROUP BY H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO, H.TAX_TYPE,H.BOX_COUNT, H.DELIVERY_DATE ")
    			.append("ORDER BY H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO ");
    			sqlMax.append("GROUP BY H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO, H.TAX_TYPE,H.BOX_COUNT, H.DELIVERY_DATE ")
    			.append("ORDER BY H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO )");

    		}else if( 5 == div ){ // 商品主檔(暫存)
    			sql.append("SELECT SELECTION_DATA, TIME_SCOPE ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM TMP_AJAX_SEARCH_DATA S ")
    			.append("WHERE S.TIME_SCOPE = '").append(timeScope).append("' ")
    			.append("order By id ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
    		}else if( 6 == div  ){ // 進貨單外標

    			sql.append("SELECT L.ITEM_CODE, I.ITEM_C_NAME, I.CATEGORY02, (SELECT CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.BRAND_CODE  = I.BRAND_CODE AND CATEGORY_TYPE ='CATEGORY02' AND C.CATEGORY_CODE = I.CATEGORY02) AS CATEGORY02_NAME, I.BOX_CAPACITY, L.QUANTITY, I.SUPPLIER_ITEM_CODE,  H.DECLARATION_DATE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM L, IM_ITEM I ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO = '").append(orderNo).append("' ")
    			.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(category01)){
    				sqlBody.append("AND I.CATEGORY01 IN ('").append(category01).append("') "); // '03','09'
    			}

    			if(StringUtils.hasText(category02)){
    				sqlBody.append("AND I.CATEGORY02 = '").append(category02).append("' ");
    			}

    			if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
    				if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
    					.append("AND H.WAREHOUSE_IN_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
    				}else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}
    			}

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    			if(StringUtils.hasText(orderBy)){
    				sql.append(" order By ").append(orderBy); // rownum
//  				sqlMax.append(" order By ").append(orderBy);
    			}
    		}else if( 7 == div ){ // 調撥單-食品菸

    			sql.append("SELECT L.ITEM_CODE, I.ITEM_C_NAME, I.BOX_CAPACITY, L.INDEX_NO, H.DELIVERY_DATE, L.LOT_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM I ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND I.CATEGORY01 IN (  '07','08' ) ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}
    			sqlBody.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    		}else if( 8 == div ){ // 調撥單-化妝品,精品

    			sql.append("SELECT H.ORDER_TYPE_CODE || H.ORDER_NO AS ORDER_NO, L.INDEX_NO, L.WEIGHT, L.DELIVERY_QUANTITY, L.BOX_NO, H.BOX_COUNT, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM I ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND I.CATEGORY01 IN (  '01','03' ) ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}
    			sqlBody.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    		}else if( 9 == div ){ // 變價單(含報單日期)
 
    			StringBuffer onHandCondition = new StringBuffer();
    			if(StringUtils.hasText(customsWarehouseCode)){
    				onHandCondition.append(" AND C.CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("'");
    				//庫別
    				if(StringUtils.hasText(warehouseCode)){
    					onHandCondition.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("'");
    				}
    				sqlMax.append("select count(ITEM_CODE) AS rowCount FROM(");
    			}else{
    				sql.append("SELECT * FROM(");
    				sql.append("SELECT ORDER_NO, ENABLE_DATE, CUSTOMS_WAREHOUSE_CODE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, SUM(CURRENT_ON_HAND_QTY) AS IM_CURRENT_ON_HAND_QTY, CM_CURRENT_ON_HAND_QTY, ITEM_C_NAME, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, STATUS, INDEX_NO, DESCRIPTION FROM(");
    				sqlMax.append("select count(rownum) AS rowCount FROM(").append(sql);
    			}

    			sql.append("SELECT H.ORDER_NO, H.ENABLE_DATE, W.CUSTOMS_WAREHOUSE_CODE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, C.CURRENT_ON_HAND_QTY AS CM_CURRENT_ON_HAND_QTY, I.ITEM_C_NAME,  C.DECLARATION_NO, C.DECLARATION_SEQ,  C.DECL_DATE, H.STATUS, L.INDEX_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,
//    			sql.append("SELECT H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, L.INDEX_NO, W.CUSTOMS_WAREHOUSE_CODE, C.CURRENT_ON_HAND_QTY AS CM_CURRENT_ON_HAND_QTY, C.DECLARATION_NO, C.DECLARATION_SEQ, C.DECL_DATE "); // rownum,
    			sqlMax.append("SELECT H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, L.ITEM_CODE, I.CATEGORY02, I.ITEM_BRAND, I.CATEGORY13, L.ORIGINAL_PRICE, L.UNIT_PRICE, NVL(O.CURRENT_ON_HAND_QTY,0) AS CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, H.STATUS, L.INDEX_NO, W.CUSTOMS_WAREHOUSE_CODE, C.CURRENT_ON_HAND_QTY AS CM_CURRENT_ON_HAND_QTY, C.DECLARATION_NO, C.DECLARATION_SEQ, C.DECL_DATE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION "); // rownum,

    			sqlBody.append("FROM IM_PRICE_ADJUSTMENT H, IM_PRICE_LIST L, IM_ITEM I, IM_ITEM_ON_HAND_VIEW O, IM_WAREHOUSE W, CM_DECLARATION_ON_HAND_VIEW C ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = I.ITEM_CODE  ")
    			.append("AND I.BRAND_CODE = O.BRAND_CODE(+) ")
    			.append("AND I.ITEM_CODE = O.ITEM_CODE(+) ")
    			.append("AND O.BRAND_CODE = W.BRAND_CODE ")
    			.append("AND O.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
    			.append("AND O.BRAND_CODE = C.BRAND_CODE ")
    			.append("AND O.ITEM_CODE = C.CUSTOMS_ITEM_CODE ")
    			.append("AND W.CUSTOMS_WAREHOUSE_CODE = C.CUSTOMS_WAREHOUSE_CODE ")

    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
    			.append("AND I.IS_TAX = '").append(taxType).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append(onHandCondition);

    			if(StringUtils.hasText(orderNo)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

    			if("N".equalsIgnoreCase(showZero)){
    				sqlBody.append("AND O.CURRENT_ON_HAND_QTY <> 0 ")
    				.append("AND C.CURRENT_ON_HAND_QTY <> 0 ");
    			}

    			if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
    				if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
    					.append("AND H.ENABLE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
    				}else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.ENABLE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}
    			}

    			if(StringUtils.hasText(customsWarehouseCode)){
    				sqlBody.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("' ");
    			}

    			if(StringUtils.hasText(supplierCode)){
    				sqlBody.append("AND H.SUPPLIER_CODE = '").append(supplierCode).append("' ");
    			}

    			if(StringUtils.hasText(category01)){
    				sqlBody.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
    			}

    			if(StringUtils.hasText(category02)){
    				sqlBody.append("AND I.CATEGORY02 = '").append(category02).append("' ");
    			}

    			if(StringUtils.hasText(samePrice) && "N".equals(samePrice) && "PAJ".equals(orderTypeCode)){
    				sqlBody.append("AND L.ORIGINAL_PRICE <> L.UNIT_PRICE ");
    			}

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);

    			if(StringUtils.hasText(customsWarehouseCode)){
    			}else{
    				sql.append(") GROUP BY ORDER_NO, ENABLE_DATE, CUSTOMS_WAREHOUSE_CODE, WAREHOUSE_CODE, ITEM_CODE, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, CM_CURRENT_ON_HAND_QTY, CATEGORY02, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, ITEM_C_NAME, STATUS, INDEX_NO, DESCRIPTION ");
    				sqlMax.append(") GROUP BY ORDER_NO, ENABLE_DATE, CUSTOMS_WAREHOUSE_CODE, WAREHOUSE_CODE, ITEM_CODE, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, CM_CURRENT_ON_HAND_QTY, CATEGORY02, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, ITEM_C_NAME, STATUS, INDEX_NO, DESCRIPTION ) )");
    			}

    			if(StringUtils.hasText(orderBy)){
    				sql.append(" order By ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ").append(orderBy.substring(2)); // rownum
    			}

    			if(!StringUtils.hasText(customsWarehouseCode)){
    				sql.append(") ");
    			}else{
    				sqlMax.append(") ");
    			}

    		}else if( 10 == div ){ // 商品主檔(建檔)
    			sql.append("SELECT L.ITEM_CODE, P.UNIT_PRICE, L.PAPER, I.ITEM_C_NAME, L.INDEX_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_ITEM_BARCODE_HEAD H, IM_ITEM_BARCODE_LINE L, IM_ITEM_CURRENT_PRICE_VIEW P, IM_ITEM I ") //IM_ITEM_ON_HAND_VIEW
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = P.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = P.ITEM_CODE ")
    			.append("AND P.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND P.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

    			sqlBody.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

    			if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
    				if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.DUE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
    					.append("AND H.DUE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.DUE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
    				}else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
    					sqlBody.append("AND H.DUE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
    				}
    			}

    			if(StringUtils.hasText(category01)){
    				sqlBody.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
    			}

    			if(StringUtils.hasText(category02)){
    				sqlBody.append("AND I.CATEGORY02 = '").append(category02).append("' ");
    			}

//  			if(StringUtils.hasText(warehouseCode)){
//  			sqlBody.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("'");
//  			}

    			sqlBody.append(" order By L.INDEX_NO ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
    		}else if(11 == div){		// 調撥單轉補條碼
    			sql.append("SELECT L.ITEM_CODE, P.UNIT_PRICE, L.DELIVERY_QUANTITY, I.ITEM_C_NAME, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
//    			sql.append("SELECT L.ITEM_CODE, P.UNIT_PRICE, L.DELIVERY_QUANTITY, I.ITEM_C_NAME, L.INDEX_NO ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM_CURRENT_PRICE_VIEW P, IM_ITEM I ")
    			.append("WHERE H.HEAD_ID = L.HEAD_ID ")
    			.append("AND H.BRAND_CODE = P.BRAND_CODE ")
    			.append("AND L.ITEM_CODE = P.ITEM_CODE ")
    			.append("AND P.BRAND_CODE = I.BRAND_CODE ")
    			.append("AND P.ITEM_CODE = I.ITEM_CODE ")
    			.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
    			.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
    			.append("AND H.STATUS NOT IN ( 'VOID' ) ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}
    			sqlBody.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
    		}else if(12 == div){	// 調整單-進貨(T2A)
    			sql.append("select L.item_code, I.category02, I.item_brand, I.category13, 0 as unit_Price, abs(L.DIF_QUANTITY) as DIF_QUANTITY , I.item_c_name, H.creation_date, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append("SELECT count(rownum) as rowCount ");

    			sqlBody.append("  from IM_ADJUSTMENT_HEAD H , IM_ADJUSTMENT_LINE L , IM_ITEM I ")
    			.append("where H.head_id = L.head_id ")
    			.append("and L.item_code = I.item_code ")
    			.append("and H.brand_code = '").append(brandCode).append("' ")
    			.append("and I.brand_code = '").append(brandCode).append("' ")
    			.append("and H.order_type_code = '").append(orderTypeCode).append("' ")
    			.append("and H.order_no = '").append(orderNo).append("' ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

    			if (StringUtils.hasText(orderBy)) { //新增排序 by Caspar 2012/02/24
					sqlBody.append(" ORDER BY ").append(orderBy);
				}
    			
    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
    		}else if(13 == div){
    			sql.append(" select L.item_code, I.item_c_name, H.ADJUSTMENT_DATE, L.INDEX_NO as SEQ, I.BOX_CAPACITY, L.ORIGINAL_DECLARATION_NO as DECLNO, abs(L.DIF_QUANTITY) as DIF_QUANTITY, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append(" SELECT count(rownum) as rowCount ");

    			sqlBody.append(" from IM_ADJUSTMENT_HEAD H , IM_ADJUSTMENT_LINE L , IM_ITEM I ")
    			.append(" where H.head_id = L.head_id ")
    			.append(" and L.item_code = I.item_code ")
    			.append( "and H.brand_code = '").append(brandCode).append("' ")
    			.append(" and I.brand_code = '").append(brandCode).append("' ")
    			.append(" and H.order_type_code = '").append(orderTypeCode).append("' ")
    			.append(" and H.order_no = '").append(orderNo).append("' ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
    		}
    		else if(14 == div){ // 新增效期外標 add by Weichun 2011.01.10
    			sql.append(" select I.SUPPLIER_ITEM_CODE, I.item_c_name, '20' || SUBSTR(L.item_code,LENGTH(L.item_code)-5) AS EXPIRE_DATE, L.item_code, I.BOX_CAPACITY, L.INDEX_NO as DECLNO, CEIL(ABS (L.DIF_QUANTITY) /I.BOX_CAPACITY ) AS QUANTITY, abs(L.DIF_QUANTITY) as DIF_QUANTITY ,TO_CHAR(H.ADJUSTMENT_DATE,'YYYYMMDD'), " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");
    			sqlMax.append(" SELECT count(rownum) as rowCount ");

    			sqlBody.append(" from IM_ADJUSTMENT_HEAD H , IM_ADJUSTMENT_LINE L , IM_ITEM I ")
    			.append(" where H.head_id = L.head_id ")
    			.append(" and L.item_code = I.item_code ")
    			.append( "and H.brand_code = '").append(brandCode).append("' ")
    			.append(" and I.brand_code = '").append(brandCode).append("' ")
    			.append(" and H.order_type_code = '").append(orderTypeCode).append("' ")
    			.append(" and H.order_no = '").append(orderNo).append("' ");

    			if(StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd) ){
    				sqlBody.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
    			}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
    			}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
    				sqlBody.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
    			}

				if (StringUtils.hasText(orderBy)) { //新增排序 by Weichun 2011/04/26
					if ("EXPIRE_DATE".equals(orderBy))
						sqlBody.append(" ORDER BY ").append("SUBSTR(L.item_code,LENGTH(L.item_code)-5) ");
					else
						sqlBody.append(" ORDER BY ").append(orderBy);
				} else
					sqlBody.append(" ORDER BY ").append("L.INDEX_NO ");

    			sql.append(sqlBody);
    			sqlMax.append(sqlBody);
			} else if (15 == div) { // 新增進貨外標(Excel匯入) add by Weichun 2011.03.02
				sql.append("SELECT SELECTION_DATA, TIME_SCOPE ");
				sqlMax.append("SELECT count(rownum) as rowCount ");

				sqlBody.append("FROM TMP_AJAX_SEARCH_DATA S ").append("WHERE S.TIME_SCOPE = '").append(timeScope).append(
						"' ").append("order By id ");

				sql.append(sqlBody);
				sqlMax.append(sqlBody);

			}


    		log.info(" sql.toString() =  " + sql.toString());
    		log.info(" sqlMax.toString() = " + sqlMax.toString());
    		map.put("sql", sql.toString());
    		map.put("sqlMax", sqlMax.toString());

    		return map;
    	} catch (Exception e) {
    		log.error("產生SQL發生錯誤，原因:" + e.toString());
    		throw new Exception("產生SQL發生錯誤，原因:" + e.getMessage());
    	}
    }

    /**
     * 取得CC開窗URL字串
     *
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    String reportFileName = (String)PropertyUtils.getProperty(otherBean, "reportFileName");

	    String startDate = (String)PropertyUtils.getProperty(otherBean, "startDate");
	    log.info("before startDate = " + startDate);
	    if(StringUtils.hasText(startDate)){
		startDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		startDate = startDate.replace("/", "");
	    }
	    String endDate = (String)PropertyUtils.getProperty(otherBean, "endDate");
	    log.info("before endDate = " + endDate);
	    if(StringUtils.hasText(endDate)){
		endDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		endDate = endDate.replace("/", "");
	    }
	    log.info("startDate = " + startDate);
	    log.info("endDate = " + endDate);
	    String category01 = (String)PropertyUtils.getProperty(otherBean, "category01");
	    String category02 = (String)PropertyUtils.getProperty(otherBean, "category02");
	    String warehouseCode = (String)PropertyUtils.getProperty(otherBean, "warehouseCode");
	    String taxType = (String)PropertyUtils.getProperty(otherBean, "taxType");
	    String showZero = (String)PropertyUtils.getProperty(otherBean, "showZero");
	    String samePrice = (String)PropertyUtils.getProperty(otherBean, "samePrice");
	    String supplierCode = (String)PropertyUtils.getProperty(otherBean, "supplierCode");
	    String customsWarehouseCode = (String)PropertyUtils.getProperty(otherBean, "customsWarehouseCode");

	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞
	    Map parameters = new HashMap(0);
	    parameters.put("prompt0", brandCode);
	    parameters.put("prompt1", orderTypeCode);
	    parameters.put("prompt2", orderNo);
	    parameters.put("prompt3", startDate);
	    parameters.put("prompt4", endDate);
	    parameters.put("prompt5", category01);
	    parameters.put("prompt6", category02);
	    parameters.put("prompt7", warehouseCode);
	    parameters.put("prompt8", taxType);
	    parameters.put("prompt9", showZero);
	    parameters.put("prompt10", samePrice);
	    parameters.put("prompt11", supplierCode);
	    parameters.put("prompt12", customsWarehouseCode);

	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName, parameters);
	    returnMap.put("reportUrl", reportUrl);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	}catch(IllegalAccessException iae){
	    System.out.println(iae.getMessage());
	    throw new IllegalAccessException(iae.getMessage());
	}catch(InvocationTargetException ite){
	    System.out.println(ite.getMessage());
	    throw new InvocationTargetException(ite, ite.getMessage());
	}catch(NoSuchMethodException nse){
	    System.out.println(nse.getMessage());
	    throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());
	}
    }


    /**
     * 初始化 bean
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    Map multiList = new HashMap(0);

	    String brandName = (buBrandService.findById(loginBrandCode)).getBrandName();
	    resultMap.put("brandName", brandName);
	    resultMap.put("brandCode", loginBrandCode);

	    List<BuCommonPhraseLine> allBarCodeTypes = buCommonPhraseService.getCommonPhraseLinesById("BarCodeConfig", false);
	    List<ImItemCategory> allCategory01 = imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY01");
	    List<BuCommonPhraseLine> allCustomsWarehouseCode = buCommonPhraseService.getCommonPhraseLinesById("CustomsWarehouseCode", false);
	    List<ImWarehouse> allWarehouseCode = imWarehouseDAO.findImWarehouse(loginBrandCode, "", "");
	    
	    multiList.put("allBarCodeTypes", AjaxUtils.produceSelectorData(allBarCodeTypes, "lineCode", "name", false, true)); // 類型˙
	    multiList.put("allCategory01", AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", false, true)); // 大類˙
	    multiList.put("allCustomsWarehouseCode", AjaxUtils.produceSelectorData(allCustomsWarehouseCode, "lineCode", "name", false, true)); // 關別˙
	    multiList.put("allWarehouseCode", AjaxUtils.produceSelectorData(allWarehouseCode, "warehouseCode", "warehouseName", false, true)); // 庫別˙
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("條碼初始化失敗，原因：" + ex.toString());
	    throw new Exception("條碼初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 將商品主檔輸入結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, ITEM_TMP_GRID_FIELD_NAMES, 0);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 條碼匯出
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<String> executeMatch(Properties httpRequest) throws Exception{
	log.info( "===========<executeMatch>===========");

	Map map = new HashMap();
	try{
	    // 取得複製時所需的必要資訊
	    String brandCode = httpRequest.getProperty("brandCode");
	    String barCodeType = httpRequest.getProperty("barCodeType");
	    String braCodeTypePre = barCodeType.substring(0, 2);
	    String barCodeTypeSuffix = barCodeType.substring(3, barCodeType.length());
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");

	    String orderNo = httpRequest.getProperty("orderNo").trim().toUpperCase();
	    String orderNoEnd = httpRequest.getProperty("orderNoEnd").trim().toUpperCase();
	    String orderBy = httpRequest.getProperty("orderBy");
	    String price = httpRequest.getProperty("price");
	    String samePrice = httpRequest.getProperty("samePrice");
	    String category = httpRequest.getProperty("category");
	    String category01 = httpRequest.getProperty("category01");
	    String category02 = httpRequest.getProperty("category02");
	    String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");
	    String warehouseCode = httpRequest.getProperty("warehouseCode");
	    String showZero = httpRequest.getProperty("showZero");
	    String taxType = httpRequest.getProperty("taxType");
	    String startDate = httpRequest.getProperty("startDate");
	    String beginDate = httpRequest.getProperty("beginDate");
	    String endDate = httpRequest.getProperty("endDate");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    String description = httpRequest.getProperty("description");
	    String timeScope = httpRequest.getProperty("timeScope");

	    log.info( "brandCode = " + brandCode );
	    log.info( "barCodeType = " + barCodeType );
	    log.info( "orderTypeCode = " + orderTypeCode );
	    log.info( "orderNo = " + orderNo );
	    log.info( "orderNoEnd = " + orderNoEnd );
	    log.info( "orderBy = " + orderBy );
	    log.info( "price = " + price );
	    log.info( "samePrice = " + samePrice );
	    log.info( "category = " + category );
	    log.info( "category01 = " + category01 );
	    log.info( "category02 = " + category02 );
	    log.info( "customsWarehouseCode = " + customsWarehouseCode );
	    log.info( "warehouseCode = " + warehouseCode );
	    log.info( "showZero = " + showZero );
	    log.info( "taxType = " + taxType );
	    log.info( "startDate = " + startDate );
	    log.info( "beginDate = " + beginDate );
	    log.info( "endDate = " + endDate );
	    log.info( "supplierCode = " + supplierCode );
	    log.info( "description = " + description );
	    log.info( "timeScope = " + timeScope);

	    map.put("brandCode", brandCode);
	    map.put("barCodeType", barCodeType);
	    map.put("braCodeTypePre", braCodeTypePre);
	    map.put("barCodeTypeSuffix", barCodeTypeSuffix);
	    map.put("orderTypeCode", orderTypeCode);
	    map.put("orderNo", orderNo);
	    map.put("orderNoEnd", orderNoEnd);
	    map.put("orderBy", orderBy);
	    map.put("price", price);
	    map.put("samePrice", samePrice);
	    map.put("category", category);
	    map.put("category01", category01);
	    map.put("category02", category02);
	    map.put("customsWarehouseCode",customsWarehouseCode);
	    map.put("warehouseCode", warehouseCode);
	    map.put("showZero", showZero);
	    map.put("taxType", taxType);
	    map.put("startDate", startDate);
	    map.put("beginDate", beginDate);
	    map.put("endDate", endDate);
	    map.put("supplierCode", supplierCode);
	    map.put("description", description);
	    map.put("timeScope", timeScope);

	    List<String> lists = writeTxt(httpRequest, map); // 寫入txt

	    log.info( "===========<executeMatch/>===========");
	    return lists;
	} catch (IOException e) {
	    throw new IOException("寫入檔案發生錯誤");
	}catch(Exception ex){
		ex.printStackTrace();
	    log.error("尋找匹配條碼產生時發生錯誤，原因：" + ex.toString());
	    throw new Exception("尋找匹配條碼產生時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 匯入補條碼明細
     * @param headId
     * @param lineLists
     * @throws Exception
     */
    public void executeImportLists(String timeScope,String brandCode, List lineLists) throws Exception{
//COVER_BY_MACO
		Map findObjs = new HashMap();
		try{
		    log.info(" timeScope = " + timeScope);
		    log.info(" brandCode = " + brandCode);
		    tmpAjaxSearchDataService.deleteByTimeScope(timeScope);
		    if(lineLists != null && lineLists.size() > 0){
				for(int i = 0; i < lineLists.size(); i++){
				    TmpAjaxSearchData tmpAjaxSearchData = new TmpAjaxSearchData();
				    TmpBarcode  tmpBarcode = (TmpBarcode)lineLists.get(i);
				    String itemCode = tmpBarcode.getItemCode();
				    Long paper = tmpBarcode.getPaper();
				    String beginDate=tmpBarcode.getBeginDate();


				    log.info(" BEGIN_DATE = " + beginDate);
				    findObjs.put(" and brandCode = :brandCode",brandCode);
				    findObjs.put(" and itemCode = :itemCode",itemCode);
				    findObjs.put(" and typeCode = :priceType","1");
				    Map searchMap = imItemCurrentPriceViewDAO.search( "ImItemCurrentPriceView", findObjs, -1, -1, BaseDAO.QUERY_SELECT_ALL );
				    List<ImItemCurrentPriceView> imItemCurrentPriceViews = (List<ImItemCurrentPriceView>) searchMap.get(BaseDAO.TABLE_LIST);
				    StringBuffer selectionData = new StringBuffer();
				    if(imItemCurrentPriceViews != null && imItemCurrentPriceViews.size() > 0 ){
						ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViews.get(0);
						selectionData.append(i+1L).append(",")
						.append(itemCode).append(",")
						.append(imItemCurrentPriceView.getUnitPrice().longValue()).append(",")
						.append(paper).append(",")
						.append(imItemCurrentPriceView.getItemCName()).append(",");	
						if(null != beginDate)
							selectionData.append(beginDate).append(" ,");
						else
							selectionData.append(" ,");
						if(null != imItemCurrentPriceView.getDescription())
							selectionData.append(imItemCurrentPriceView.getDescription());
						else
							selectionData.append(" ");
				    }else{
						selectionData.append(i+1L).append(",")
						.append(itemCode).append(",")
						.append("0,")
						.append(paper).append(",")
						.append(" ").append(",")
						.append(MessageStatus.DATA_NOT_FOUND).append(", ");
				    }
				    
				    tmpAjaxSearchData.setTimeScope(timeScope);
				    tmpAjaxSearchData.setChecked("Y");
				    tmpAjaxSearchData.setRowId(i+1L);
				    tmpAjaxSearchData.setSelectionData(selectionData.toString());
				    tmpAjaxSearchDataDAO.save(tmpAjaxSearchData);
				}
		    }
		}catch (Exception ex) {
		    log.error("補條碼(暫存)明細匯入時發生錯誤，原因：" + ex.toString());
		    throw new Exception("補條碼(暫存)明細匯入時發生錯誤，原因：" + ex.getMessage());
		}
    }

	/**
	 * 匯入進貨條碼明細
	 *
	 * @param headId
	 * @param lineLists
	 * @throws Exception
	 */
	public void executeImportExcel(String timeScope, String brandCode, List lineLists) throws Exception {

		try {
			log.info(" timeScope = " + timeScope);
			log.info(" brandCode = " + brandCode);

			tmpAjaxSearchDataService.deleteByTimeScope(timeScope);

			if (lineLists != null && lineLists.size() > 0) {
				for (int i = 0; i < lineLists.size(); i++) {
					TmpAjaxSearchData tmpAjaxSearchData = new TmpAjaxSearchData();
					TmpBarcodeExcel tmpBarcode = (TmpBarcodeExcel) lineLists.get(i);
					String itemCode = tmpBarcode.getItemCode();
					String sql = "SELECT I.ITEM_CODE,"
							+ "       I.ITEM_C_NAME,"
							+ "       I.CATEGORY02,"
							+ "       (SELECT CATEGORY_NAME"
							+ "          FROM IM_ITEM_CATEGORY C"
							+ "         WHERE     C.BRAND_CODE = I.BRAND_CODE"
							+ "               AND CATEGORY_TYPE = 'CATEGORY02'"
							+ "               AND C.CATEGORY_CODE = I.CATEGORY02)"
							+ "          AS CATEGORY02_NAME,"
							+ "       I.BOX_CAPACITY,"
							+ "       I.SUPPLIER_ITEM_CODE"
							+ "  FROM  IM_ITEM I"
							+ "  WHERE I.ITEM_CODE = '" + itemCode + "'";

					List results = nativeQueryDAO.executeNativeSql(sql.toString());
					Object obj = new Object();
					StringBuffer selectionData = new StringBuffer();
					if (results != null && results.size() > 0) {
						obj = results.get(0);
						String itemCName = String.valueOf(((Object[]) obj)[1]);
						String categroy02 = String.valueOf(((Object[]) obj)[2]);
						String categroy02Name = String.valueOf(((Object[]) obj)[3]);
						Long boxCapacity = NumberUtils.getLong(String.valueOf(((Object[]) obj)[4]));
						String supplierItemCode = String.valueOf(((Object[]) obj)[5]);
						selectionData.append(itemCode).append(",").append(itemCName).append(",").append(categroy02).append(
								",").append(categroy02Name).append(",").append(boxCapacity).append(",").append(
								tmpBarcode.getQuantity()).append(",").append(supplierItemCode).append(", ");
					}
					tmpAjaxSearchData.setTimeScope(timeScope);
					tmpAjaxSearchData.setChecked("Y");
					tmpAjaxSearchData.setRowId(i + 1L);
					tmpAjaxSearchData.setSelectionData(selectionData.toString());
					tmpAjaxSearchDataDAO.save(tmpAjaxSearchData);
				}
			}
		} catch (Exception ex) {
			log.error("進貨外標明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("進貨外標明細匯入時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 寫入txt
	 *
	 * @param brandCode
	 * @param barCodeType
	 * @param orderNo
	 * @param bufferedWriter
	 * @throws Exception
	 */
	public List<String> writeTxt(Properties properties, Map map) throws Exception {
		String barCodeType = (String) map.get("barCodeType");
		String braCodeTypePre = (String) map.get("braCodeTypePre");
		String barCodeTypeSuffix = (String) map.get("barCodeTypeSuffix");

		// 分割點
		if ("ImReceiveHead".equals(barCodeTypeSuffix)) {
			if ("01".equals(braCodeTypePre)) { // 進貨單轉條碼
				// 進貨單
				return writeTmp01(properties);
			} else if ("02".equals(braCodeTypePre)) { // 皮件.酒外標 - 進貨單轉條碼
				return writeTmp02(map);
			} else if ("03".equals(braCodeTypePre)) { // 進貨單轉條碼（Excel匯入）
				return writeTmp03(properties);
			}

		} else if ("ImMovementHead".equals(barCodeTypeSuffix)) {
			if ("03".equals(braCodeTypePre)) { // 食品、菸帶效其商品外標 = > 轉到調撥單
				return writeImReceiveHead(map);
			} else if ("05".equals(braCodeTypePre)) { // 調撥單轉出貨裝箱單麥頭
				return writeImMovementHead05(map);
			} else if ("06".equals(braCodeTypePre)) { // 化妝品.精品轉出口麥頭
				return writeImMovementHead06(map);
			} else if ("07".equals(braCodeTypePre)) { // 調撥單轉補條碼
				return writeImMovementHead07(map);
			}
		} else if ("ImPriceAdjustment".equals(barCodeTypeSuffix)) { // 變價單條碼 -
			// 變價單
			return writeImPriceAdjustment(map);
		} else if ("01_Decl_ImPriceAdjustment".equals(barCodeType)) {
			return writeImPriceAdjustmentDecl(map); // 含報單
		} else if ("ImItem".equals(barCodeTypeSuffix)) {
			if ("01".equals(braCodeTypePre)) {
				return writeImItemTmp(map); // 商品主檔(暫存)
			} else if ("02".equals(braCodeTypePre)) {
				return writeImItem(map); // 商品主檔(建檔)
			}
		} else if ("ImAdjustmentHead".equals(barCodeTypeSuffix)) {
			if ("01".equals(braCodeTypePre)) { // 進貨單轉條碼 進貨單
				return writeImAdjustment01(properties);
			} else if ("02".equals(braCodeTypePre)) { // 皮件.酒外標 - 進貨單轉條碼
				return writeImAdjustment02(properties);
			} else if ("03".equals(braCodeTypePre)) { // 效期外標 add by Weichun 2011.01.11
				return writeImAdjustment03(properties);
			}
		}

		log.info("查無此匯出功能");
		return null;
	}

	/**
	 * 進貨單轉條碼 進貨單
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<String> writeTmp01(Properties properties) throws Exception {
		String orderTypeCode = properties.getProperty("orderTypeCode");
		String orderNo = properties.getProperty("orderNo");
		String price = properties.getProperty("price");
		String category = properties.getProperty("category");
		String description = properties.getProperty("description");

		List<String> lists = new ArrayList<String>();

		Map sqlMap = getSQLByBarcode(properties);

		String sql = (String) sqlMap.get("sql");

		List results = nativeQueryDAO.executeNativeSql(sql);

		if (null != results && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				StringBuffer sb = new StringBuffer();

				String dateStr = "";
				if (null != ((Object[]) results.get(i))[7]) {
					log.info("date = " + ((Object[]) results.get(i))[7].toString().substring(2, 7).replace("-", ""));
					dateStr = ((Object[]) results.get(i))[7].toString().substring(2, 7).replace("-", "");
				}

				sb.append(txtFormat(((Object[]) results.get(i))[0], 15, "R")); // 品號
				if ("Y".equalsIgnoreCase(category)) {
					sb.append(txtFormat(((Object[]) results.get(i))[1], 6, "R")); // 商品類別
				} else {
					sb.append(txtFormat("", 6, "R"));
				}
				sb.append(txtFormat("-" + ((Object[]) results.get(i))[2], 7, "R")) // 商品品牌
						.append(txtFormat("-" + (AjaxUtils.getPropertiesValue(((Object[]) results.get(i))[3], "")), 9, "R")); // 商品系列
																																// 修正14變9

				if ("Y".equalsIgnoreCase(price)) {
					sb.append(txtFormat(((Object[]) results.get(i))[4], 13, "L")); // 售價
																					// 修正8變13
				} else {
					sb.append(txtFormat("", 13, "L"));
				}

				sb.append(txtFormat(((Object[]) results.get(i))[5], 5, "L")) // 數量
						.append(txtFormat(((Object[]) results.get(i))[6], 26, "R")); // 商品品名

				sb.append(txtFormat(NumberUtils.getInt(dateStr) * 2, 4, "L")); // 保稅報單日期
																				// ,
																				// 完稅則抓起單日期
				//說明
				if("Y".equals(description)){
					sb.append(txtFormat("", 1, "R"));								//空白
					sb.append(txtFormat(((Object[])results.get(i))[8], 26, "R"));	//說明
				}
				log.info("sb = " + sb);
				lists.add(sb.toString());
			}
			StringBuffer end = new StringBuffer();
			end.append(txtFormat("END", 15, "R")).append(txtFormat("", 6, "R")).append(txtFormat("", 7, "R")).append(
					txtFormat("", 16, "R")).append(txtFormat("", 6, "L")).append(txtFormat("1", 5, "L")).append(
					txtFormat("進貨條碼" + orderTypeCode + orderNo, 26, "R"));
			lists.add(end.toString());
		}
		return lists;
	}

	/**
	 * 進貨單轉條碼（Excel匯入）
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<String> writeTmp03(Properties properties) throws Exception {

		List<String> lists = new ArrayList<String>();
		Map sqlMap = getSQLByBarcode(properties);

		String sql = (String) sqlMap.get("sql");
		List results = nativeQueryDAO.executeNativeSql(sql);

		if (null != results && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				StringBuffer sb = new StringBuffer();

				Object[] resultList = (Object[]) results.get(i);
				String[] resultValue = String.valueOf(resultList[0]).split(",");
				sb.append("    "); // 項次留空白
				sb.append(txtFormat(resultValue[0], 14, "R")); // 品號（靠左，右邊補空白）
				sb.append(txtFormat(resultValue[1], 25, "R")); // 品名（靠左，右邊補空白）
				sb.append(txtFormat(resultValue[2], 4, "R")); // 中類（靠左，右邊補空白）
				sb.append(txtFormat(resultValue[3], 22, "R")); // 中類名稱（靠左，右邊補空白）
				sb.append(txtFormat(resultValue[4], 6, "L")); // 數量（靠右，左邊補空白）
				sb.append(txtFormat(resultValue[5], 6, "L")); // 每箱數量（靠右，左邊補空白）
				sb.append(txtFormat(resultValue[6], 30, "R")); // 廠商貨號（靠左，右邊補空白）
				log.info("sb = " + sb);
				lists.add(sb.toString());
			}
//			StringBuffer end = new StringBuffer();
//			end.append(txtFormat("END", 15, "R")).append(txtFormat("", 6, "R")).append(txtFormat("", 7, "R")).append(
//					txtFormat("", 16, "R")).append(txtFormat("", 6, "L")).append(txtFormat("1", 5, "L")).append(
//					txtFormat("進貨條碼" + orderTypeCode + orderNo, 26, "R"));
//			lists.add(end.toString());
		}
		return lists;
	}

    /**
     * 皮件.酒外標 - 進貨單轉條碼
     * @param map
     * @return
     * @throws Exception
     */
    public List<String> writeTmp02(Map map)throws Exception{
	String  brandCode = (String)map.get("brandCode");
	String 	orderTypeCode = (String)map.get("orderTypeCode");
	String  orderNo = (String)map.get("orderNo");
	String orderBy = (String)map.get("orderBy");
	String  category = (String)map.get("category");
	String  category01 = (String)map.get("category01");
	String  category02 = (String)map.get("category02");
	String  startDate = (String)map.get("startDate");
	String  endDate = (String)map.get("endDate");
	String description = (String)map.get("description");

	List<String> lists = new ArrayList<String>();
	StringBuffer sql = new StringBuffer();
	
	sql.append("SELECT L.INDEX_NO, L.ITEM_CODE, I.ITEM_C_NAME, I.CATEGORY02, (SELECT CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.BRAND_CODE  = I.BRAND_CODE AND CATEGORY_TYPE ='CATEGORY02' AND C.CATEGORY_CODE = I.CATEGORY02) AS CATEGORY02_NAME, L.QUANTITY, I.SUPPLIER_ITEM_CODE, I.BOX_CAPACITY, H.DECLARATION_DATE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
	.append("FROM IM_RECEIVE_HEAD H, IM_RECEIVE_ITEM L, IM_ITEM I ")
	.append("WHERE H.HEAD_ID = L.HEAD_ID ")
	.append("AND H.BRAND_CODE = I.BRAND_CODE  ")
	.append("AND L.ITEM_CODE = I.ITEM_CODE ");

	if(StringUtils.hasText(category01)){
	    sql.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
	}
	sql.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND H.ORDER_NO = '").append(orderNo).append("' ")
	.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
	.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append("AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(category01)){
	    sql.append("AND I.CATEGORY01 IN ('").append(category01).append("') "); // '03','09'
	}
	if(StringUtils.hasText(category02) && "Y".equalsIgnoreCase(category)){
	    sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
	}

	if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
	    if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
		sql.append("AND H.WAREHOUSE_IN_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
		.append("AND H.WAREHOUSE_IN_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
	    }else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
		sql.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
	    }else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
		sql.append("AND H.WAREHOUSE_IN_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
	    }
	}

	if(StringUtils.hasText(orderBy)){
	    sql.append(" order by ").append(orderBy) ;
	}
	List results = nativeQueryDAO.executeNativeSql(sql.toString());


	if (null != results && results.size() > 0) {
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();
		Double boxCapacity = NumberUtils.getDouble(((Object[]) results.get(i))[7].toString());
		Double quantity = NumberUtils.getDouble(((Object[]) results.get(i))[5].toString());

		// 若0預設帶1
		if(boxCapacity == 0 ){
		    boxCapacity = 1D;
		}

		String dateStr = "";
		if(null != ((Object[]) results.get(i))[8] ){
		    log.info( "date = " + ((Object[]) results.get(i))[8].toString().substring(2,7).replace("-", ""));
		    dateStr = ((Object[]) results.get(i))[8].toString().substring(2,7).replace("-", "");
		}
		log.info( "boxCapacity = " + boxCapacity );
		log.info( "quantity = " + quantity );

		sb.append( txtFormat( ((Object[]) results.get(i))[0], 4, "L"  )) // 項次
		.append(   txtFormat( ((Object[]) results.get(i))[1], 14, "R" )) // 品號
		.append(   txtFormat( ((Object[]) results.get(i))[2], 25, "R" )); // 商品品名

		if(StringUtils.hasText(category)){
		    sb.append(   txtFormat( ((Object[]) results.get(i))[3], 4, "R"  )) // 類別
		    .append(   txtFormat( ((Object[]) results.get(i))[4], 22, "R" )); // 類別名稱
		}else{
		    sb.append(   txtFormat( "", 4, "R"  )) // 類別
		    .append(   txtFormat( "", 22, "R" )); // 類別名稱
		}
		sb.append(   txtFormat( ((Object[]) results.get(i))[7], 6, "L" ))	// 裝箱
		.append(   txtFormat( ((Object[]) results.get(i))[5], 6, "L"  )) // 進貨數量
		.append(   txtFormat( ((Object[]) results.get(i))[6], 30, "R" )) // 原廠代碼
		.append(   txtFormat( (int)Math.ceil(quantity / boxCapacity) ,5, "L")) // 張數
		.append(   txtFormat(NumberUtils.getInt(dateStr) * 2 ,4, "L"))
		.append(	txtFormat("", 1, "R"  ))	//空白
		.append(	txtFormat( ((Object[]) results.get(i))[9], 26, "R"  ));	//說明
		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}

	return lists;
    }

    // 變價單條碼 - 變價單
    public List<String> writeImPriceAdjustment(Map map)throws Exception{
		String brandCode = (String)map.get("brandCode");
		String orderNo = (String)map.get("orderNo");
		String taxType = (String)map.get("taxType");
		String warehouseCode = (String)map.get("warehouseCode"); // 庫別
		String customsWarehouseCode = (String)map.get("customsWarehouseCode");	// 關別
		
		String orderTypeCode = (String)map.get("orderTypeCode");
		String price = (String)map.get("price");
		String category = (String)map.get("category");
		String category01 = (String)map.get("category01");
		String category02 = (String)map.get("category02");
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		String supplierCode = (String)map.get("supplierCode");
		String orderBy = (String)map.get("orderBy");
		String showZero = (String)map.get("showZero");
		String samePrice = (String)map.get("samePrice");
		String description = (String)map.get("description");
	//	String actualOrderNo = orderNo.substring(3, orderNo.length());
	
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		if(!StringUtils.hasText(organizationCode)){
		    throw new Exception("依品牌:"+brandCode+"查無此組織代碼");
		}
	
		List<String> lists = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		StringBuffer onHandCondition = new StringBuffer();
	//	onHandCondition.append(" AND O.ORGANIZATION_CODE = '").append(organizationCode).append("'");
		if(StringUtils.hasText(customsWarehouseCode)){
			if(customsWarehouseCode!=null)//by jason
			onHandCondition.append(" AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("' ");//by jason 20160330
			if(StringUtils.hasText(warehouseCode)){
				onHandCondition.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("' ");
				
			}
		}else{
		    sql.append("SELECT ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, UNIT_PRICE, CURRENT_ON_HAND_QTY , ITEM_C_NAME, INDEX_NO, ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, DESCRIPTION FROM("); // rownum,
		    sql.append("SELECT ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, UNIT_PRICE, SUM(CURRENT_ON_HAND_QTY) AS CURRENT_ON_HAND_QTY , ITEM_C_NAME, INDEX_NO, ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, DESCRIPTION FROM(");
		}
	
		sql.append("SELECT L.ITEM_CODE, I.CATEGORY02, I.ITEM_BRAND, I.CATEGORY13, L.UNIT_PRICE, O.CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, L.INDEX_NO, H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
		.append("FROM IM_PRICE_ADJUSTMENT H, IM_PRICE_LIST L, IM_ITEM I, IM_ITEM_ON_HAND_VIEW O, IM_WAREHOUSE W ")
		.append("WHERE H.HEAD_ID = L.HEAD_ID ")
		.append("AND H.BRAND_CODE = I.BRAND_CODE ")
		.append("AND L.ITEM_CODE = I.ITEM_CODE ")
		.append("AND I.BRAND_CODE = O.BRAND_CODE(+) ")
		.append("AND I.ITEM_CODE = O.ITEM_CODE(+) ")
		.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND I.IS_TAX = '").append(taxType).append("' ")
		.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
		.append("AND H.STATUS NOT IN ( 'VOID' ) ")
		.append(" AND O.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")//by jason20160330
		.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ").append(onHandCondition);
	
		if(StringUtils.hasText(orderNo)){
		    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
		}
	
		if("N".equalsIgnoreCase(showZero)){
		    sql.append("AND O.CURRENT_ON_HAND_QTY <> 0 ");
		}
	
		if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
		    if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
			sql.append("AND H.ENABLE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
			.append("AND H.ENABLE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
		    }else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
			sql.append("AND H.ENABLE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
		    }else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
			sql.append("AND H.ENABLE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
		    }
		}
	
		if(StringUtils.hasText(supplierCode)){
		    sql.append("AND H.SUPPLIER_CODE = '").append(supplierCode).append("' ");
		}
	
		if(StringUtils.hasText(category01)){
		    sql.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
		}
	
		if(StringUtils.hasText(category02) && "Y".equalsIgnoreCase(category) ){
		    sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
		}
	
		if(StringUtils.hasText(samePrice) && "N".equals(samePrice) && "PAJ".equals(orderTypeCode)){
		    sql.append("AND L.ORIGINAL_PRICE <> L.UNIT_PRICE ");
		}
	
		if(StringUtils.hasText(customsWarehouseCode)){
	//	    sql.append(") ");
		}else{
		    sql.append(") GROUP BY ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, UNIT_PRICE, ITEM_C_NAME, INDEX_NO, DESCRIPTION ");
		}
	
		if(StringUtils.hasText(orderBy)){
		    sql.append(" order by ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ").append(orderBy.substring(2));
		}else
		sql.append(" order by  WAREHOUSE_CODE ");//20160330 by jason
		if(!StringUtils.hasText(customsWarehouseCode)){
		    sql.append(")");
		}
	
		List results = nativeQueryDAO.executeNativeSql(sql.toString());
	
		Map priceMap = new HashMap();
		StringBuffer sb = new StringBuffer();
		StringBuffer end = new StringBuffer();
	
		if (null != results && results.size() > 0) {
		    for (int i = 0; i < results.size(); i++) {
				Object itemCodeObj = ((Object[]) results.get(i))[0]; // 品號
				Object category02Obj = ((Object[]) results.get(i))[1]; // 中類
				Object itemBrandObj = ((Object[]) results.get(i))[2]; // 商品品牌
				Object category13Obj = ((Object[]) results.get(i))[3]; // 系列
				Object unitPriceObj = ((Object[]) results.get(i))[4];  // 售價
				Object storckObj = ((Object[]) results.get(i))[5];  // 庫存數量
				Object itemCNameObj = ((Object[]) results.get(i))[6];  // 品名
		
				Object orderNoObj = ((Object[]) results.get(i))[8];// 單號
				Object enableDateObj = ((Object[]) results.get(i))[9];// 生效日期
				Object warehouseCodeObj = ((Object[]) results.get(i))[10];// 庫別
		
				sb.delete(0, sb.length());
				sb.append( txtFormat( itemCodeObj, 15, "R")); // 品號
		
				if("Y".equalsIgnoreCase(category)){
				    sb.append(   txtFormat( category02Obj, 6, "R"));  // 中類
				}else{
				    sb.append(   txtFormat( "", 6, "R"));  // 中類
				}
		
				sb.append(   txtFormat( itemBrandObj, 7, "R"))  // 商品品牌
				.append(   txtFormat( category13Obj, 9, "R")); // 系列14>9
		
				if("Y".equalsIgnoreCase(price)){
				    sb.append(   txtFormat( unitPriceObj,  13, "L")); // 售價 8>13
				}else{
				    sb.append(   txtFormat( "",  13, "L")); // 售價
				}
		
				sb.append(   txtFormat( storckObj, 5, "L"))  // 庫存數量
				.append(   txtFormat( itemCNameObj, 26, "R")) // 品名
				.append(   txtFormat("", 1, "R"  ))								  //空白
				.append(   txtFormat( ((Object[]) results.get(i))[11], 26, "R")); // 說明
		
				// 若不包含則放入map
				log.info( "單號 = " + orderNoObj + " 生效日期= " + enableDateObj + " 倉別= " + warehouseCodeObj );
				//20160330  by jason

				if( !priceMap.containsKey((String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj)) ){
		
				    if(i > 0 ){//by jason 20160318 
					// 加入END行數
					end.delete(0, end.length());
					end.append( txtFormat( "END "+((Object[]) results.get(i-1))[10], 15, "R"  ) )
					.append( txtFormat( "", 6, "R"  ) )
					.append( txtFormat( "", 7, "R"  ) )
					.append( txtFormat( "", 18, "R"  ) )
					.append( txtFormat( "", 4, "L"  ) )
					.append( txtFormat( "1", 5, "L"  ) )
					.append( txtFormat( "調價 "+ ((Object[]) results.get(i-1))[8] +" "+ DateUtils.format((Date)((Object[]) results.get(i-1))[9], "yyMMdd") , 26, "R" ) ); // 要改成生效日期 
		
					log.info( "end = " + end );
					lists.add( end.toString() );
				    }

				    priceMap.put((String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj), (String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj));
				}
				
				
				log.info( "sb = " + sb );
				lists.add( sb.toString() );
				
				if(  i == results.size()-1 ){
					
				    end.delete(0, end.length());
				    end.append( txtFormat( "END "+warehouseCodeObj, 15, "R"  ) )
				    .append( txtFormat( "", 6, "R"  ) )
				    .append( txtFormat( "", 7, "R"  ) )
				    .append( txtFormat( "", 18, "R"  ) )
				    .append( txtFormat( "", 4, "L"  ) )
				    .append( txtFormat( "1", 5, "L"  ) )
				    .append( txtFormat( "調價 "+ orderNoObj +" "+ DateUtils.format((Date)enableDateObj, "yyMMdd") , 26, "R" ) ); // 要改成生效日期
		
				    log.info( "end = " + end );
				    lists.add( end.toString() );
				    
				}
						
		    }
		}else{
		    log.info( "查無單號" );
		}
		return lists;
    }

 // 變價單條碼 - 變價單(含報單日期)
    public List<String> writeImPriceAdjustmentDecl(Map map)throws Exception{
		String brandCode = (String)map.get("brandCode");
		String orderNo = (String)map.get("orderNo");
		String taxType = (String)map.get("taxType");
		String warehouseCode = (String)map.get("warehouseCode"); // 庫別
		String customsWarehouseCode = (String)map.get("customsWarehouseCode"); // 關別
	
		String orderTypeCode = (String)map.get("orderTypeCode");
		String price = (String)map.get("price");
		String category = (String)map.get("category");
		String category01 = (String)map.get("category01");
		String category02 = (String)map.get("category02");
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		String supplierCode = (String)map.get("supplierCode");
		String orderBy = (String)map.get("orderBy");
		String showZero = (String)map.get("showZero");
		String samePrice = (String)map.get("samePrice");
		String description = (String)map.get("description");
	
		String organizationCode = UserUtils.getOrganizationCodeByBrandCode(brandCode);
		if(!StringUtils.hasText(organizationCode)){
		    throw new Exception("依品牌:"+brandCode+"查無此組織代碼");
		}
	
		List<String> lists = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		StringBuffer onHandCondition = new StringBuffer();
		if(StringUtils.hasText(warehouseCode)){
		    onHandCondition.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("' ");
		}else{
	//
		    sql.append("SELECT ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, UNIT_PRICE, CURRENT_ON_HAND_QTY , ITEM_C_NAME, INDEX_NO, ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, CUSTOMS_WAREHOUSE_CODE, CM_CURRENT_ON_HAND_QTY, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, ORIGINAL_PRICE, STATUS, DESCRIPTION  FROM("); // rownum,
		    sql.append("SELECT ITEM_CODE, CATEGORY02, ITEM_BRAND, CATEGORY13, UNIT_PRICE, SUM(CURRENT_ON_HAND_QTY) AS CURRENT_ON_HAND_QTY , ITEM_C_NAME, INDEX_NO, ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, CUSTOMS_WAREHOUSE_CODE, CM_CURRENT_ON_HAND_QTY, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, ORIGINAL_PRICE, STATUS, DESCRIPTION  FROM(");
		}
		sql.append("SELECT L.ITEM_CODE, I.CATEGORY02, I.ITEM_BRAND, I.CATEGORY13, L.UNIT_PRICE, O.CURRENT_ON_HAND_QTY, I.ITEM_C_NAME, L.INDEX_NO, H.ORDER_NO, H.ENABLE_DATE, O.WAREHOUSE_CODE, W.CUSTOMS_WAREHOUSE_CODE, C.CURRENT_ON_HAND_QTY AS CM_CURRENT_ON_HAND_QTY, C.DECLARATION_NO, C.DECLARATION_SEQ, C.DECL_DATE, L.ORIGINAL_PRICE, H.STATUS, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
		.append("FROM IM_PRICE_ADJUSTMENT H, IM_PRICE_LIST L, IM_ITEM I, IM_ITEM_ON_HAND_VIEW O, IM_WAREHOUSE W, CM_DECLARATION_ON_HAND_VIEW C ")
		.append("WHERE H.HEAD_ID = L.HEAD_ID ")
		.append("AND H.BRAND_CODE = I.BRAND_CODE ")
		.append("AND L.ITEM_CODE = I.ITEM_CODE ")
		.append("AND I.BRAND_CODE = O.BRAND_CODE(+) ")
		.append("AND I.ITEM_CODE = O.ITEM_CODE(+) ")
		.append("AND O.BRAND_CODE = W.BRAND_CODE ")
		.append("AND O.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		.append("AND O.BRAND_CODE = C.BRAND_CODE ")
		.append("AND O.ITEM_CODE = C.CUSTOMS_ITEM_CODE ")
		.append("AND W.CUSTOMS_WAREHOUSE_CODE = C.CUSTOMS_WAREHOUSE_CODE ")
	
		.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND I.IS_TAX = '").append(taxType).append("' ")
		.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
		.append("AND H.STATUS NOT IN ( 'VOID' ) ")
		.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ").append(onHandCondition);
	
		if(StringUtils.hasText(orderNo)){
		    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
		}
	
		if("N".equalsIgnoreCase(showZero)){
		    sql.append("AND O.CURRENT_ON_HAND_QTY <> 0 ")
		    .append("AND C.CURRENT_ON_HAND_QTY <> 0 ");
		}
	
		if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
		    if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
				sql.append("AND H.ENABLE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
				.append("AND H.ENABLE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
		    }else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
		    	sql.append("AND H.ENABLE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
		    }else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
		    	sql.append("AND H.ENABLE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
		    }
		}
	
		if(StringUtils.hasText(customsWarehouseCode)){
		    sql.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("' ");
		}
	
		if(StringUtils.hasText(supplierCode)){
		    sql.append("AND H.SUPPLIER_CODE = '").append(supplierCode).append("' ");
		}
	
		if(StringUtils.hasText(category01)){
		    sql.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
		}
	
		if(StringUtils.hasText(category02) && "Y".equalsIgnoreCase(category) ){
		    sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
		}
	
		if(StringUtils.hasText(samePrice) && "N".equals(samePrice) && "PAJ".equals(orderTypeCode)){
		    sql.append("AND L.ORIGINAL_PRICE <> L.UNIT_PRICE ");
		}
	
		if(StringUtils.hasText(warehouseCode)){
	//	    sql.append(") ");
		}else{
		    sql.append(") GROUP BY ORDER_NO, ENABLE_DATE, CUSTOMS_WAREHOUSE_CODE, WAREHOUSE_CODE, ITEM_CODE, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, CM_CURRENT_ON_HAND_QTY, CATEGORY02, ITEM_BRAND, CATEGORY13, ORIGINAL_PRICE, UNIT_PRICE, ITEM_C_NAME, STATUS, INDEX_NO, DESCRIPTION ");
		}
	
		if(StringUtils.hasText(orderBy)){
		    sql.append(" order by ORDER_NO, ENABLE_DATE, WAREHOUSE_CODE, ").append(orderBy.substring(2));
		}
	
		if(!StringUtils.hasText(warehouseCode)){
		    sql.append(")");
		}
	
		List results = nativeQueryDAO.executeNativeSql(sql.toString());
	
		Map priceMap = new HashMap();
		StringBuffer sb = new StringBuffer();
		StringBuffer end = new StringBuffer();
	
		if (null != results && results.size() > 0) {
		    for (int i = 0; i < results.size(); i++) {
		
				Object itemCodeObj = ((Object[]) results.get(i))[0]; 	// 品號
				Object category02Obj = ((Object[]) results.get(i))[1]; 	// 中類
				Object itemBrandObj = ((Object[]) results.get(i))[2]; 	// 商品品牌
				Object category13Obj = ((Object[]) results.get(i))[3]; 	// 系列
				Object unitPriceObj = ((Object[]) results.get(i))[4];  	// 售價
				Object cmStorckObj = ((Object[]) results.get(i))[12];  	// 庫存數量-> 改為報單庫存
				Object itemCNameObj = ((Object[]) results.get(i))[6];  	// 品名
		
				Object orderNoObj = ((Object[]) results.get(i))[8];		// 單號
				Object enableDateObj = ((Object[]) results.get(i))[9];	// 生效日期
				Object warehouseCodeObj = ((Object[]) results.get(i))[10];// 庫別
		
				String dateStr = "";
				if(null != ((Object[]) results.get(i))[15] ){			// 報單日期
				    log.info( "date = " + ((Object[]) results.get(i))[15].toString().substring(2,7).replace("-", ""));
				    dateStr = ((Object[]) results.get(i))[15].toString().substring(2,7).replace("-", "");
				}
		
				sb.delete(0, sb.length());
				sb.append( txtFormat( itemCodeObj, 15, "R")); // 品號
		
				if("Y".equalsIgnoreCase(category)){
				    sb.append(   txtFormat( category02Obj, 6, "R"));  // 中類
				}else{
				    sb.append(   txtFormat( "", 6, "R"));  // 中類
				}
		
				sb.append(   txtFormat( itemBrandObj, 7, "R"))  // 商品品牌
				.append(   txtFormat( category13Obj, 9, "R")); // 系列14>9
		
				if("Y".equalsIgnoreCase(price)){
				    sb.append(   txtFormat( unitPriceObj,  13, "L")); // 售價 8>13
				}else{
				    sb.append(   txtFormat( "",  13, "L")); // 售價
				}
		
				sb.append(   txtFormat( cmStorckObj, 5, "L"))  // 庫存數量
				.append(   txtFormat( itemCNameObj, 26, "R")); // 品名
		
				sb.append(   txtFormat(NumberUtils.getInt(dateStr) * 2 ,4, 	"L")); 	// 報單日期
				sb.append(   txtFormat("",1, "R"));									// 空白
				sb.append(   txtFormat(((Object[]) results.get(i))[18] ,26, "R"));	// 說明
				
				// 若不包含則放入map
				log.info( "單號 = " + orderNoObj + " 生效日期= " + enableDateObj + " 倉別= " + warehouseCodeObj );
				if( !priceMap.containsKey((String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj)) ){
		
				    if(i -1 > 0){
						// 加入END行數
						end.delete(0, end.length());
						end.append( txtFormat( "END "+((Object[]) results.get(i-1))[10], 15, "R"  ) )
						.append( txtFormat( "", 6, "R"  ) )
						.append( txtFormat( "", 7, "R"  ) )
						.append( txtFormat( "", 18, "R"  ) )
						.append( txtFormat( "", 4, "L"  ) )
						.append( txtFormat( "1", 5, "L"  ) )
						.append( txtFormat( "調價 "+ ((Object[]) results.get(i-1))[8] +" "+ DateUtils.format((Date)((Object[]) results.get(i-1))[9], "yyMMdd") , 26, "R" ) ); // 要改成生效日期
			
						log.info( "end = " + end );
						lists.add( end.toString() );
				    }
				    priceMap.put((String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj), (String.valueOf(orderNoObj) + enableDateObj + warehouseCodeObj));
				}
		
				log.info( "sb = " + sb );
				lists.add( sb.toString() );
		
				// 最後一行
				if(  i == ( results.size() - 1 ) ){
				    end.delete(0, end.length());
				    end.append( txtFormat( "END "+warehouseCodeObj, 15, "R"  ) )
				    .append( txtFormat( "", 6, "R"  ) )
				    .append( txtFormat( "", 7, "R"  ) )
				    .append( txtFormat( "", 18, "R"  ) )
				    .append( txtFormat( "", 4, "L"  ) )
				    .append( txtFormat( "1", 5, "L"  ) )
				    .append( txtFormat( "調價 "+ orderNoObj +" "+ DateUtils.format((Date)enableDateObj, "yyMMdd") , 26, "R" ) ); // 要改成生效日期
		
				    log.info( "end = " + end );
				    lists.add( end.toString() );
				}
		    }
		}else{
		    log.info( "查無單號" );
		}
		return lists;
    }


    // 食品、菸帶效其商品外標 =>調撥單
    public List<String> writeImReceiveHead(Map map)throws Exception{
	String  brandCode = (String)map.get("brandCode");
	String 	orderTypeCode = (String)map.get("orderTypeCode");
	String  orderNo = (String)map.get("orderNo");
	String  orderNoEnd = (String)map.get("orderNoEnd");
	String description = (String)map.get("description");

	List<String> lists = new ArrayList<String>();
	StringBuffer sql = new StringBuffer();
	DecimalFormat df = new DecimalFormat("000000000000");
	sql.append("SELECT L.ITEM_CODE, I.ITEM_C_NAME, I.BOX_CAPACITY, L.INDEX_NO, H.DELIVERY_DATE, L.DELIVERY_QUANTITY, L.LOT_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
	.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM I ")
	.append("WHERE H.HEAD_ID = L.HEAD_ID ")
	.append("AND L.ITEM_CODE = I.ITEM_CODE ")
	.append("AND H.BRAND_CODE = I.BRAND_CODE ")
	.append("AND I.CATEGORY01 IN (  '07','08' ) ")
	.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append("AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
	}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
	}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
	}

	sql.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");
	List results = nativeQueryDAO.executeNativeSql(sql.toString());

	if (null != results && results.size() > 0) {
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();
		Double boxCapacity = NumberUtils.getDouble(((Object[]) results.get(i))[2].toString());
		Double quantity = NumberUtils.getDouble(((Object[]) results.get(i))[5].toString());

		// 若0預設帶1
		if(boxCapacity == 0 ){
		    boxCapacity = 1D;
		}

		String dateStr = "";
		if(null != ((Object[]) results.get(i))[4] ){
		    log.info( "date = " + ((Object[]) results.get(i))[4].toString().substring(0,10).replace("-", "/"));
		    dateStr = ((Object[]) results.get(i))[4].toString().substring(0,10).replace("-", "/");
		}

		log.info( "boxCapacity = " + boxCapacity );
		log.info( "quantity = " + quantity );

		sb.append(  txtFormat( ((Object[]) results.get(i))[0], 13, "R")) 		// 品號
		.append(   txtFormat( ((Object[]) results.get(i))[1], 25, "R"))			// 商品品名
		.append(   txtFormat( ((Object[]) results.get(i))[2], 2, "L")) 			// 裝箱量
		.append(   txtFormat( ((Object[]) results.get(i))[3], 3, "L"))			// 項次
		.append(   txtFormat( dateStr, 10, "L") ) 								// 進貨日期
		.append(   txtFormat( (int)Math.ceil(quantity / boxCapacity), 4, "L")) 	// 標籤張數
		.append(   txtFormat( df.format( NumberUtils.getLong( String.valueOf(((Object[]) results.get(i))[6]))), 12, "R"))		// 有效期限
		.append(   txtFormat("", 1, "R"  ))										//空白
		.append(   txtFormat( ((Object[]) results.get(i))[7], 26, "R"));		// 說明
		
		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}

	return lists;
    }

    // 調撥單-轉出貨裝箱單麥頭
    public List<String> writeImMovementHead05(Map map)throws Exception{
	String  brandCode = (String)map.get("brandCode");
	String 	orderTypeCode = (String)map.get("orderTypeCode");
	String  orderNo = (String)map.get("orderNo");
	String orderNoEnd = (String)map.get("orderNoEnd");

	List<String> lists = new ArrayList<String>();
	StringBuffer sql = new StringBuffer();

	sql.append(" SELECT H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO AS ORDER_NO, L.BOX_NO, H.TAX_TYPE, ")
	.append(" SUM(L.DELIVERY_QUANTITY) AS DELIVERY_QUANTITY, H.BOX_COUNT, H.DELIVERY_DATE, H.ARRIVAL_STORE_CODE ")
	.append(" FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L ")
	.append(" WHERE H.HEAD_ID = L.HEAD_ID ")
	.append(" AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append(" AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append(" AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
	}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
	}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
	}
	sql.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

	sql.append("GROUP BY H.ARRIVAL_WAREHOUSE_CODE, H.DELIVERY_WAREHOUSE_CODE, H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO, H.TAX_TYPE, H.BOX_COUNT, H.DELIVERY_DATE, H.ARRIVAL_STORE_CODE ")
	.append("ORDER BY H.ORDER_TYPE_CODE || H.ORDER_NO,L.BOX_NO");

	List results = nativeQueryDAO.executeNativeSql(sql.toString());

	if (null != results && results.size() > 0) {
	    Map isOneMap = new HashMap();
	    for (int i = 0; i < results.size(); i++) {
		if(isOneMap.containsKey(((Object[]) results.get(i))[2])){ // 是否單別+單號已存在
		    isOneMap.put(((Object[]) results.get(i))[2], false ); // 表示重複
		}else{
		    isOneMap.put(((Object[]) results.get(i))[2], true );
		}
	    }

	    Map orderNoMap = new HashMap();
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();

		String dateStr = "";
		if(null != ((Object[]) results.get(i))[7] ){
		    //log.info( "date = " + ((Object[]) results.get(i))[7].toString().substring(0,10).replace("-", "/"));
		    dateStr = ((Object[]) results.get(i))[7].toString().substring(0,10).replace("-", "/");
		}

		sb.append(  txtFormat( ((Object[]) results.get(i))[0], 16, "R")) // 出貨店別
		.append(   txtFormat(( ((Object[]) results.get(i))[1] ), 8, "R") ) // 出庫倉代碼 - > 入庫倉代碼    + "->" + ((Object[]) results.get(i))[0]
		.append(   txtFormat( ((Object[]) results.get(i))[2], 18, "R"));  // 單號

		if(orderNoMap.containsKey(((Object[]) results.get(i))[2])){ // 是否單別+單號已存在
		    if( ((Long)orderNoMap.get(((Object[]) results.get(i))[2])) + 1L == NumberUtils.getLong( String.valueOf(((Object[]) results.get(i))[3])) ){
			// 表示連續箱號則不用顯示
			sb.append(   txtFormat( "", 4, "L") );	 // 箱號 含-的空白
		    }else{
			// 表示不連續則顯示
			sb.append(   txtFormat( ((Long)orderNoMap.get(((Object[]) results.get(i))[2])) + 1L, 3, "L") )	 // 箱號
			.append(  "-" );
		    }

		    sb.append(   txtFormat( (((Object[]) results.get(i))[3]), 3, "L") );
		    orderNoMap.put(((Object[]) results.get(i))[2], NumberUtils.getLong( String.valueOf(((Object[]) results.get(i))[3]) )); // 覆蓋掉了
		}else{
		    orderNoMap.put(((Object[]) results.get(i))[2], NumberUtils.getLong( String.valueOf(((Object[]) results.get(i))[3]) ));
		    // 表示只有一張單
		    if(isOneMap.containsKey(((Object[]) results.get(i))[2])){
			Boolean isOne = (Boolean)isOneMap.get(((Object[]) results.get(i))[2]); // 是否只有一張單
			log.info("單號 = " + ((Object[]) results.get(i))[2] +" 是否只有一筆 = " + isOne );
			if(isOne){
			    sb.append(   txtFormat( "", 4, "L") );	 // 箱號 含-的空白
			}else{
			    sb.append(   txtFormat( 1L, 3, "L") )	 // 箱號
			    .append(  "-" );
			}
		    }
		    sb.append(   txtFormat( (((Object[]) results.get(i))[3]), 3, "L") );
		}

		sb.append(   txtFormat( "1", 3, "L"));	// 張數

		if("P".equalsIgnoreCase(String.valueOf(((Object[]) results.get(i))[4]))){
		    sb.append(   txtFormat( " ", 1, "R")); // 完稅補空白
		    sb.append(   txtFormat( ((Object[]) results.get(i))[4], 1, "R")); // 稅別
		}else{ // 保稅
		    sb.append(   txtFormat( ((Object[]) results.get(i))[4], 1, "R")); // 稅別
		    sb.append(   txtFormat( " ", 1, "R"));
		}
		sb.append(   txtFormat( ((Object[]) results.get(i))[5], 4, "L")) // 數量
		.append(   txtFormat( ((Object[]) results.get(i))[6], 4, "L")) // 總箱
		.append(   txtFormat( dateStr, 10, "L")); // 出貨日期
		if(StringUtils.hasText( (String)(((Object[]) results.get(i))[8])) ){
			sb.append(   txtFormat( "", 2, "L")) // 空白兩格
			.append(   txtFormat( ((Object[]) results.get(i))[8], 5, "L")); // 轉入店別
		}
		
		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}

	return lists;
    }

    // 化妝品.精品轉出口麥頭 調撥單
    public List<String> writeImMovementHead06(Map map)throws Exception{
	String  brandCode = (String)map.get("brandCode");
	String 	orderTypeCode = (String)map.get("orderTypeCode");
	String  orderNo = (String)map.get("orderNo");
	String orderNoEnd = (String)map.get("orderNoEnd");
	String description = (String)map.get("description");
	
	DecimalFormat df = new DecimalFormat("000");
	DecimalFormat dfNumber = new DecimalFormat("00");

	List<String> lists = new ArrayList<String>();
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT H.ORDER_TYPE_CODE || H.ORDER_NO AS ORDER_NO, L.INDEX_NO, L.WEIGHT, L.DELIVERY_QUANTITY, L.BOX_NO, H.BOX_COUNT, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
	.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM I ")
	.append("WHERE H.HEAD_ID = L.HEAD_ID ")
	.append("AND L.ITEM_CODE = I.ITEM_CODE ")
	.append("AND H.BRAND_CODE = I.BRAND_CODE ")
	.append("AND I.CATEGORY01 IN (  '01','03' ) ")
	.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append("AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
	}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
	}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
	}
	sql.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

	List results = nativeQueryDAO.executeNativeSql(sql.toString());

	if (null != results && results.size() > 0) {
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();

		log.info( "日期 = " + DateUtils.getCurrentDateStr("MMdd") );

		sb.append(  txtFormat( "FC"+ DateUtils.getCurrentDateStr("MMdd")+ dfNumber.format( Math.random()*99 ) , 8, "R"))	// FC DATE(MMDD) + 兩碼亂數自動產生麥頭單號
		.append(   txtFormat( ((Object[]) results.get(i))[0], 18, "R") + "-" ) 		// 調撥單號
		.append(   txtFormat(df.format( ((Object[]) results.get(i))[1] ), 3, "L")) 	// 項次
		.append(   txtFormat( ((Object[]) results.get(i))[2], 8,  "L")) 			// 重量
		.append(   txtFormat( ((Object[]) results.get(i))[3], 4,  "L"))			  	// 數量
		.append(   txtFormat( ((Object[]) results.get(i))[4], 3,  "L"))				// 箱號
		.append(   txtFormat( ((Object[]) results.get(i))[5], 1,  "L"))				// 總箱數
		.append(   txtFormat( "", 1, "R"))											// 空白
		.append(   txtFormat( ((Object[]) results.get(i))[6], 26, "R"));			// 說明


		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}

	return lists;
    }

    // 調撥單轉補條碼
    public List<String> writeImMovementHead07(Map map)throws Exception{
	String  brandCode = (String)map.get("brandCode");
	String 	orderTypeCode = (String)map.get("orderTypeCode");
	String  orderNo = (String)map.get("orderNo");
	String orderNoEnd = (String)map.get("orderNoEnd");
	String description = (String)map.get("description");

	List<String> lists = new ArrayList<String>();
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT L.ITEM_CODE, P.UNIT_PRICE, L.DELIVERY_QUANTITY, I.ITEM_C_NAME, L.INDEX_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ")
	.append("FROM IM_MOVEMENT_HEAD H, IM_MOVEMENT_ITEM L, IM_ITEM_CURRENT_PRICE_VIEW P, IM_ITEM I ")
	.append("WHERE H.HEAD_ID = L.HEAD_ID ")
	.append("AND L.ITEM_CODE = I.ITEM_CODE ")
	.append("AND H.BRAND_CODE = I.BRAND_CODE ")
	.append("AND I.ITEM_CODE = P.ITEM_CODE ")
	.append("AND I.BRAND_CODE = P.BRAND_CODE ")
	.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append("AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
	}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
	}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
	}
	sql.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

	List results = nativeQueryDAO.executeNativeSql(sql.toString());

	if (null != results && results.size() > 0) {
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();

		sb.append(   txtFormat( ((Object[]) results.get(i))[0], 8, "R"))	// 品號
		.append(   txtFormat( "", 29, "L")) 								// 空白
		.append(   txtFormat( ((Object[]) results.get(i))[1], 8, "L")) 		// 售價
		.append(   txtFormat( ((Object[]) results.get(i))[2], 5, "L"))		// 張數
		.append(   txtFormat( ((Object[]) results.get(i))[3], 27, "R"))		// 中文名稱
		.append(   txtFormat( "", 1, "R"))									// 空白
		.append(   txtFormat( ((Object[]) results.get(i))[5], 26, "R")); 	// 說明

		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}

	return lists;
    }

    /**
     * 商品主檔補條碼(暫存)
     * @param map
     * @return
     * @throws Exception
     */
    public List<String> writeImItemTmp(Map map)throws Exception{
    	List<String> lists = new ArrayList<String>();

    	String timeScope = (String)map.get("timeScope");
    	String brandCode = (String)map.get("brandCode");
    	String description = (String)map.get("description");

    	log.info("timeScope = " + timeScope);
    	List<TmpAjaxSearchData> tmpAjaxSearchDatas = tmpAjaxSearchDataService.findByTimeScope(timeScope);
    	for (TmpAjaxSearchData tmpAjaxSearchData : tmpAjaxSearchDatas) {
    		StringBuffer sb = new StringBuffer();

    		String selectionDate = tmpAjaxSearchData.getSelectionData();
    		String[] searchDatas = selectionDate.split(AjaxUtils.SEARCH_KEY_DELIMITER);
    		String itemCode = searchDatas[1];
    		ImItem imitem = imItemDAO.findItem(brandCode, itemCode);
    		if( null != imitem ){
    			String unitPrice = searchDatas[2];
    			String paper = searchDatas[3];
    			String itemCName = searchDatas[4];
    			String imItemDesc = searchDatas[6];
    			String beginDate = dateTransfer(searchDatas[5]);
    			
    			sb.append(   txtFormat( itemCode, 13, "R"))	// 品號
    			.append(   txtFormat( "", 29, "L")) 	// 空白
    			.append(   txtFormat( unitPrice, 8, "L")) 	// 售價
    			.append(   txtFormat( paper, 5, "L"))	// 張數
    			.append(   txtFormat( itemCName, 26, "R")); // 中文名稱
	
    			if(hasDescription(description))
    				sb.append(   txtFormat( imItemDesc, 26, "R"));	// 說明
  				if( null != beginDate && beginDate.length()>0 )
        			sb.append(   txtFormat( beginDate, 8, "R")); //進貨日期
    			log.info( "sb = " + sb );
    			lists.add( sb.toString() );
    		}
    	}

    	return lists;
    }
  //切割時間與日期
    public static String dateTokens(String beginDate)
    {
    	String tempString;
    	if(beginDate.equals(""))
    	{
    		tempString ="";
    	}
    	else
    	{
    		String[] tempStr1 = beginDate.split(" ");
    		tempString = tempStr1[0];
    	}
    	return tempString;
    }
//進貨日期轉換格式  Ex:20150904=>3018  15*2=30  09*2=18
    public static String dateTransfer(String beginDate)
    {
    	log.info("取代前 beginDate=XX"+beginDate+"XX");
    	beginDate = beginDate.trim();
    	log.info("取代後 beginDate=XX"+beginDate+"XX");

    	try {
    		if(beginDate.length()==8)
    		{
    			int isIntString = Integer.parseInt(beginDate);
    			String yearCode = (String) beginDate.substring(2, 4);
    			log.info("YEAR_CODE=XX"+yearCode+"XX");
    			String monthCode = (String) beginDate.substring(4, 6);
    			log.info("MOUNTH_CODE=XX"+monthCode+"XX");
    			int year = Integer.parseInt(yearCode)%100*2;
    			int month = Integer.parseInt(monthCode)*2;
    			log.info("year"+year+ "\t month"+month);
    			beginDate = Integer.toString(year/10)+Integer.toString(year%10)
    			+Integer.toString(month/10)+Integer.toString(month%10);
    		}
    		else if(beginDate.length()==0)
    		{
    			log.info("beginDate.LENGTH"+beginDate.length());
    		}
    		else
    		{
    			log.info("格式錯誤 beginDate="+beginDate);
    			beginDate = "格式錯誤";
    		}

    	} catch(Exception e)
    	{
    		log.info("格式錯誤");
    		beginDate = "格式錯誤";
    	}
		return beginDate;

    }

    /**
     * 商品主檔補條碼(建檔)
     * @param map
     * @return
     * @throws Exception
     */
    public List<String> writeImItem(Map map)throws Exception{
	String brandCode = (String)map.get("brandCode");
	String orderTypeCode = (String)map.get("orderTypeCode");
	String orderNo = (String)map.get("orderNo");
	String orderNoEnd = (String)map.get("orderNoEnd");
	String startDate = (String)map.get("startDate");
	String endDate = (String)map.get("endDate");
	String category01 = (String)map.get("category01");
	String category02 = (String)map.get("category02");
	String description = (String)map.get("description");
//	String  warehouseCode = (String)map.get("warehouseCode");

	List<String> lists = new ArrayList<String>();

	StringBuffer sql = new StringBuffer();
	sql.append("SELECT L.ITEM_CODE, P.UNIT_PRICE, L.PAPER, I.ITEM_C_NAME, L.INDEX_NO, " + (hasDescription(description)?" I.DESCRIPTION":"''") + " AS DESCRIPTION ");

	sql.append("FROM IM_ITEM_BARCODE_HEAD H, IM_ITEM_BARCODE_LINE L, IM_ITEM_CURRENT_PRICE_VIEW P, IM_ITEM I ") //, IM_ITEM_ON_HAND_VIEW O
	.append("WHERE H.HEAD_ID = L.HEAD_ID ")
	.append("AND H.BRAND_CODE = P.BRAND_CODE ")
	.append("AND L.ITEM_CODE = P.ITEM_CODE ")
	.append("AND P.BRAND_CODE = I.BRAND_CODE ")
	.append("AND P.ITEM_CODE = I.ITEM_CODE ")
	.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND H.ORDER_NO NOT LIKE '%TMP%' ")
	.append("AND H.STATUS NOT IN ( 'VOID' ) ");

	if(StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO >= '").append(orderNo).append("' AND H.ORDER_NO <= '").append(orderNoEnd).append("' ");
	}else if(!StringUtils.hasText(orderNo) && StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNoEnd).append("' ");
	}else if(StringUtils.hasText(orderNo) && !StringUtils.hasText(orderNoEnd)){
	    sql.append("AND H.ORDER_NO = '").append(orderNo).append("' ");
	}
	sql.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");

	if(StringUtils.hasText(startDate) || StringUtils.hasText(endDate)){
	    if( StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
		sql.append("AND H.DUE_DATE >= TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ")
		.append("AND H.DUE_DATE <= TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
	    }else if(StringUtils.hasText(startDate) && !StringUtils.hasText(endDate) ){
		sql.append("AND H.DUE_DATE = TO_DATE( '").append(startDate).append("', 'YYYY/MM/DD') ");
	    }else if(!StringUtils.hasText(startDate) && StringUtils.hasText(endDate) ){
		sql.append("AND H.DUE_DATE = TO_DATE( '").append(endDate).append("', 'YYYY/MM/DD') ");
	    }
	}

	if(StringUtils.hasText(category01)){
	    sql.append("AND I.CATEGORY01 IN ('").append(category01).append("') ");
	}

	if(StringUtils.hasText(category02)){
	    sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
	}

//	if(StringUtils.hasText(warehouseCode)){
//	    sql.append(" AND O.WAREHOUSE_CODE = '").append(warehouseCode).append("'");
//	}
	sql.append(" order By L.INDEX_NO ");


	List results = nativeQueryDAO.executeNativeSql(sql.toString());
	if (null != results && results.size() > 0) {
	    for (int i = 0; i < results.size(); i++) {
		StringBuffer sb = new StringBuffer();

		sb.append(   txtFormat( ((Object[]) results.get(i))[0], 13, "R"))	// 品號
		.append(   txtFormat( "", 29, "L")) 								// 空白
		.append(   txtFormat( ((Object[]) results.get(i))[1], 8, "L")) 		// 售價
		.append(   txtFormat( ((Object[]) results.get(i))[2], 5, "L"))		// 張數
		.append(   txtFormat( ((Object[]) results.get(i))[3], 27, "R")) 	// 中文名稱
		.append(   txtFormat("", 1, "R"  ))									//空白
		.append(   txtFormat( ((Object[]) results.get(i))[5], 26, "R"));	// 說明

		log.info( "sb = " + sb );
		lists.add( sb.toString() );
	    }
	}else{
	    log.info( "查無單號" );
	}


	return lists;
    }

    /**
     * 格式化向左向右對齊
     * @param obj
     * @param length
     * @return
     */
    public static String txtFormat(Object obj, int length, String asign){
	StringBuffer sb = new StringBuffer();
	sb.append(CommonUtils.insertCharacterWithLimitedLength(AjaxUtils.getPropertiesValue(obj,""), length, length, CommonUtils.SPACE, asign));
	//log.info( " 格式化後 = " + asign);
	return sb.toString();
//	String start = AjaxUtils.getPropertiesValue( obj, "");

//	String asign = "";
//	String beginLocation = "1";
//	if( ValidateUtil.isNumber(start)){
//	// 數字向右對齊
//	beginLocation = String.valueOf((length - 1));
//	}else{
//	// 向左對齊
//	asign = "-";
//	}
//	String end = String.format("%1$" + asign + length + "s", start);
//	log.info("格式前 =" + end.length() );
//	log.info("格式前 end =" + end );
//	// 代表有中文
//	log.info("end.getBytes().length =" + end.getBytes().length );
//	if( end.getBytes().length > length ){
//	log.info("格式後 getBytes 長度 =" + end.getBytes() );
//	byte[] b = new byte[length];

//	System.arraycopy(end.getBytes(), 0, b, 0, length);

//	log.info("格式後 =" + new String(b));
//	return new String(b);
//	}

//	return end;
    }

    // 寫入
    public void writeFile(String strLine) {
	try {

	    FileWriter wrFile = new FileWriter("c:/FILE/out.txt");

	    BufferedWriter bufferedWriter = new BufferedWriter(wrFile);

	    bufferedWriter.write(strLine);
//	    bufferedWriter.newLine();

	    bufferedWriter.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    /**
     * 進貨調整單(T2A)
     * @param map
     * @return
     * @throws Exception
     */
    public List<String> writeImAdjustment01(Properties properties)throws Exception{
    	//String 	orderTypeCode = properties.getProperty("orderTypeCode");
    	//String  orderNo = properties.getProperty("orderNo");
    	String  price = properties.getProperty("price");
    	//String  category = properties.getProperty("category");

    	List<String> lists = new ArrayList<String>();
    	Map sqlMap = getSQLByBarcode(properties);
    	String sql = (String)sqlMap.get("sql");

    	List results = nativeQueryDAO.executeNativeSql(sql);
    	if (null != results && results.size() > 0) {
    		for (int i = 0; i < results.size(); i++) {
    			StringBuffer sb = new StringBuffer();

    			String dateStr = "";
    			if(null != ((Object[]) results.get(i))[7] ){
    				log.info( "date = " + ((Object[]) results.get(i))[7].toString().substring(2,7).replace("-", ""));
    				dateStr = ((Object[]) results.get(i))[7].toString().substring(2,7).replace("-", "");
    			}

    			sb.append( txtFormat( ((Object[]) results.get(i))[0], 15, "R"  )); // 品號
    			//if("Y".equalsIgnoreCase(category)){
    				sb.append(   txtFormat( ((Object[]) results.get(i))[1], 6, "R" )); // 商品類別
    			//}else{
    				//sb.append(   txtFormat( "", 6, "R" ));
    			//}
    			sb.append(   txtFormat( "-"+((Object[]) results.get(i))[2], 7, "R" )) // 商品品牌
    			.append(   txtFormat( "-"+( AjaxUtils.getPropertiesValue( ((Object[]) results.get(i))[3], "")), 9, "R" )) // 商品系列  修正14變9

    			//if("Y".equalsIgnoreCase(price)){
    				//sb.append(   txtFormat(((Object[]) results.get(i))[4],13, "L"));	// 售價  修正8變13
    			//}else{
    			.append(   txtFormat( "0",13, "L"))
    			//}

    			.append(   txtFormat(((Object[]) results.get(i))[5], 5,  "L"))  // 數量
    			.append(   txtFormat(((Object[]) results.get(i))[6],26,  "R"))	// 商品品名
    			.append(   txtFormat(NumberUtils.getInt(dateStr) * 2 ,4, "L"))  // 保稅報單日期   , 完稅則抓起單日期
    			.append(	txtFormat("", 1, "R"  ))							//空白
    			.append(   txtFormat(((Object[]) results.get(i))[8],26,  "R"));	// 說明
    			log.info( "sb = " + sb );
    			lists.add( sb.toString() );
    		}
    		/*StringBuffer end = new StringBuffer();
    		end.append( txtFormat( "END", 15, "R"  ) )
    		.append( txtFormat( "", 6, "R"  ) )
    		.append( txtFormat( "", 7, "R"  ) )
    		.append( txtFormat( "", 16, "R"  ) )
    		.append( txtFormat( "", 6, "L"  ) )
    		.append( txtFormat( "1", 5, "L"  ) )
    		.append( txtFormat( "進貨條碼"+orderTypeCode+ orderNo , 26, "R" ) );
    		lists.add( end.toString() );
    		*/
    	}
    	return lists;
    }

    /**
     * 進貨調整單外標(T2A)
     * @param map
     * @return
     * @throws Exception
     */
    public List<String> writeImAdjustment02(Properties properties)throws Exception{
    	//String 	orderTypeCode = properties.getProperty("orderTypeCode");
    	//String  orderNo = properties.getProperty("orderNo");
    	//String  price = properties.getProperty("price");
    	//String  category = properties.getProperty("category");

    	List<String> lists = new ArrayList<String>();
    	Map sqlMap = getSQLByBarcode(properties);
    	String sql = (String)sqlMap.get("sql");

    	List results = nativeQueryDAO.executeNativeSql(sql);
    	if (null != results && results.size() > 0) {
    	    for (int i = 0; i < results.size(); i++) {
    		StringBuffer sb = new StringBuffer();
    		Double boxCapacity = NumberUtils.getDouble(((Object[]) results.get(i))[4].toString());
    		Double quantity = NumberUtils.getDouble(((Object[]) results.get(i))[6].toString());
    		// 若0預設帶1
    		if(boxCapacity == 0 ){
    		    boxCapacity = 1D;
    		}

    		String dateStr = "";
    		if(null != ((Object[]) results.get(i))[2] ){
    		    log.info( "date = " + ((Object[]) results.get(i))[2].toString().replaceAll("-", ""));
    		    dateStr = ((Object[]) results.get(i))[2].toString().replaceAll("-", "");
    		}

    		sb.append(   txtFormat( ((Object[]) results.get(i))[0], 13, "R" )) 		 // 原廠貨號
    		.append(   txtFormat( ((Object[]) results.get(i))[1], 24, "R" )) 		 // 品名
    		.append(   txtFormat( dateStr, 8, "R" ))								 // 進貨日期
    		.append(   txtFormat( ((Object[]) results.get(i))[3], 13, "L"  )) 		 // 序號
    		.append(   txtFormat( ((Object[]) results.get(i))[4], 10, "L"  )) 		 // 裝箱量
    		.append(   txtFormat( ((Object[]) results.get(i))[5], 18, "R"  )) 		 // 報單
    		.append(   txtFormat( (int)Math.ceil(quantity / boxCapacity), 12, "L" )) // 數量
    		.append(   txtFormat( ((Object[]) results.get(i))[6], 5,  "L")) 		 // 張數
    		.append(	txtFormat("", 1, "R"  ))									 //空白
    		.append(   txtFormat( ((Object[]) results.get(i))[7], 26, "R")); 		 // 說明
    		//.append(   txtFormat(NumberUtils.getInt(dateStr) * 2 ,4, "L"));
    		log.info( "sb = " + sb );
    		lists.add( sb.toString() );
    	    }
    	}else{
    	    log.info( "查無單號" );
    	}
    	return lists;
    }

	/**
	 * 進貨調整單-效期外標 (T2A)
	 *
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public List<String> writeImAdjustment03(Properties properties) throws Exception {

		List<String> lists = new ArrayList<String>();
		Map sqlMap = getSQLByBarcode(properties);
		String sql = (String) sqlMap.get("sql");

		List results = nativeQueryDAO.executeNativeSql(sql);
		if (null != results && results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				StringBuffer sb = new StringBuffer();

				sb.append(txtFormat(((Object[]) results.get(i))[0], 13, "R")) 		// 原廠貨號
						.append(txtFormat(((Object[]) results.get(i))[1], 24, "R")) // 品名
						.append(txtFormat(((Object[]) results.get(i))[2], 8, "R")) 	// 效期
						.append(txtFormat(((Object[]) results.get(i))[3], 13, "R")) // 品號
						.append(txtFormat(((Object[]) results.get(i))[4], 4, "L")) 	// 裝箱量
						.append(txtFormat(((Object[]) results.get(i))[5], 3, "R")) 	// 項次
						.append(txtFormat(((Object[]) results.get(i))[6], 4, "L")) 	// 張數
						.append(txtFormat(((Object[]) results.get(i))[7], 5, "L")) 	// 進貨量
						.append(txtFormat(((Object[]) results.get(i))[8], 8, "R")) 	// 進貨日期
						.append(	txtFormat("", 1, "R"  ))						//空白
						.append(txtFormat(((Object[]) results.get(i))[9], 26, "R")); // 說明
				
				log.info("sb = " + sb);
				lists.add(sb.toString());
			}
		} else {
			log.info("查無單號");
		}
		return lists;
	}

    public static void main(String args[]){
//	Properties httpRequest = new Properties();
//	httpRequest.put("brandCode", "T2");
//	httpRequest.put("barCodeType", "02_ImReceiveHead");
//	httpRequest.put("orderNo", "20091026");

//	GenerateBarCodeService generateBarCodeService = new GenerateBarCodeService();
//	try {
//	generateBarCodeService.executeMatch(httpRequest);
//	} catch (Exception e) {
//	log.error("error");
//	}
//	GenerateBarCodeService generateBarCodeService = new GenerateBarCodeService();
//	System.out.println( GenerateBarCodeService.txtFormat("111", 10) );
//	System.out.println( GenerateBarCodeService.txtFormat("中文111", 10, "R") );
//	System.out.println( GenerateBarCodeService.txtFormat(123, 10, "L") );

	System.out.println( "Math.ceil(2 / 12) = " + Math.ceil(2D / 12D));

//	generateBarCodeService.writeFile( GenerateBarCodeService.txtFormat("中文111", 10, "L") );
//	System.out.println(NumberUtils.getInt(DateUtils.getCurrentDateStr("yyMM")) * 2);
//	String pattern = "%,20";
//	System.out.println( String.format(pattern, 1235) );
//	generateBarCodeService.writeFile("test2");
    }
    
    /**
     * 判斷是否顯示「說明」
     * @return
     */
    private boolean hasDescription(String desc){
    	if("Y".equals(desc)){
    		return true;
    	}
    	return false;
    }
}
