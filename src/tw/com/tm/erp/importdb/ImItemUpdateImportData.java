package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImItemCategoryService;
import tw.com.tm.erp.hbm.service.ImItemPriceService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.ValidateUtil;

/**
 * 商品匯入 型態數值為null會變0
 * 	  BoxCapacity
 *        foreignListPrice		原幣零售價
 *        supplierQuotationPrice	原廠報價
 *        purchaseRatio			預計採購比
 *        MaxPurchaseQuantity		T2 最高採購量
 *        MinPurchaseQuantity		T2 最低採購量
 *        Margen			標準毛利率
 *        ReplenishCoefficient		補貨係數
 *        declRatio 			銷售單位/報關單號換算
 *        minRatio 			最小單位/銷貨單位的比例換算
 * @author T02049	
 * 
 */
public class ImItemUpdateImportData implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImItemUpdateImportData.class);
	
	private static String SPACE = "<S>";
	
	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImItemUpdateImportData.initial");
		log.info("ImItemUpdateImportData.initial");
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);	
		log.info("user.getBrandCode() = " + user.getBrandCode());
		
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImItem.class.getName());

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "0");

		imInfo.addFieldName("key");
		// set field type
		imInfo.setFieldType("key", "java.lang.String");
		
		if(user.getBrandCode().indexOf("T2") <= -1){
			imInfo.setXlsColumnLength(48);
			doT1Initial(imInfo);
			imInfo.setOneRecordDetailKeys(new int[] { 44 });
		}else{
			imInfo.setXlsColumnLength(74);
			doT2Initial(imInfo);
			imInfo.setOneRecordDetailKeys(new int[] { 73 });
		}
		

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

		// set default value
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode() );
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		defaultValue.put("reserve5", user.getBrandCode() );	// 20091120 借用存放 user.brandCode 存檔前 clear
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getImItemPrice(uiProperties, user.getBrandCode()));
		// imInfo.addDetailImportInfos(getImItemCompose());

		imInfo.setImportDataStartRecord(1);
		return imInfo;
	}

	private ImportInfo doT1Initial(ImportInfo imInfo){
		
		imInfo.addFieldName("itemCode");	// 品號
		imInfo.addFieldName("brandCode");	// 品牌
		imInfo.addFieldName("itemCName");	// 中文品名
		imInfo.addFieldName("itemEName");	// 英文品名
		imInfo.addFieldName("itemLevel");	// 商品等級
		imInfo.addFieldName("lotControl");	// 批號管理
		imInfo.addFieldName("supplierItemCode"); // 廠商貨號
		imInfo.addFieldName("releaseDate");	// 上市日期
		imInfo.addFieldName("expiryDate");	// 下市日期
		imInfo.addFieldName("foreignListPrice");	// 原幣零售價
		imInfo.addFieldName("supplierQuotationPrice");	// 原廠報價
		imInfo.addFieldName("standardPurchaseCost");	// 標準進價
		imInfo.addFieldName("isComposeItem");		// 是否組合商品
		imInfo.addFieldName("specLength");		// 長
		imInfo.addFieldName("specWidth");		// 寬
		imInfo.addFieldName("specHeight");		// 高
		imInfo.addFieldName("specWeight");		// 重量
		imInfo.addFieldName("salesRatio");		// 交易單位與進貨單位的比例
		imInfo.addFieldName("salesUnit");		// 交易單位
		imInfo.addFieldName("purchaseUnit");		// 進貨單位
		imInfo.addFieldName("description");		// 說明
		imInfo.addFieldName("category01");		// 大類
		imInfo.addFieldName("category02");		// 中類
		imInfo.addFieldName("category03");		// 小類
		imInfo.addFieldName("category04");		// 尺寸
		imInfo.addFieldName("category05");		// 年份
		imInfo.addFieldName("category06");		// 季別
		imInfo.addFieldName("category07");		// 性別
		imInfo.addFieldName("category08");		// 材質
		imInfo.addFieldName("category09");		// 款式	
		imInfo.addFieldName("category10");		// 顏色
		imInfo.addFieldName("category11");		// 款式編號
		imInfo.addFieldName("category12");		// 屬性
		imInfo.addFieldName("category13");		// 系列
		imInfo.addFieldName("category14");		// 產地
		imInfo.addFieldName("category15");		// 功能
		imInfo.addFieldName("category16");		// 樣本編號
		imInfo.addFieldName("category17");		// 製造商
		imInfo.addFieldName("category18");		// 其他1
		imInfo.addFieldName("category19");		// 其他2
		imInfo.addFieldName("category20");		// 分類20(帳務類型)
		imInfo.addFieldName("category22");		// 材質1
		imInfo.addFieldName("category21");		// 材質2
		imInfo.addFieldName("enable");			// 啟用

		// set field type
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("brandCode", "java.lang.String");
		imInfo.setFieldType("itemCName", "java.lang.String");
		imInfo.setFieldType("itemEName", "java.lang.String");
		imInfo.setFieldType("itemLevel", "java.lang.String");
		imInfo.setFieldType("lotControl", "java.lang.String");
		imInfo.setFieldType("supplierItemCode", "java.lang.String");
		imInfo.setFieldType("releaseDate", "java.util.Date");
		imInfo.setFieldType("expiryDate", "java.util.Date");
		imInfo.setFieldType("foreignListPrice", "java.lang.Double");
		imInfo.setFieldType("supplierQuotationPrice", "java.lang.Double");
		imInfo.setFieldType("standardPurchaseCost", "java.lang.Double");
		imInfo.setFieldType("isComposeItem", "java.lang.String");
		imInfo.setFieldType("specLength", "java.lang.String");
		imInfo.setFieldType("specWidth", "java.lang.String");
		imInfo.setFieldType("specHeight", "java.lang.String");
		imInfo.setFieldType("specWeight", "java.lang.String");
		imInfo.setFieldType("salesRatio", "java.lang.Long");
		imInfo.setFieldType("salesUnit", "java.lang.String");
		imInfo.setFieldType("purchaseUnit", "java.lang.String");
		imInfo.setFieldType("description", "java.lang.String");
		imInfo.setFieldType("category01", "java.lang.String");
		imInfo.setFieldType("category02", "java.lang.String");
		imInfo.setFieldType("category03", "java.lang.String");
		imInfo.setFieldType("category04", "java.lang.String");
		imInfo.setFieldType("category05", "java.lang.String");
		imInfo.setFieldType("category06", "java.lang.String");
		imInfo.setFieldType("category07", "java.lang.String");
		imInfo.setFieldType("category08", "java.lang.String");
		imInfo.setFieldType("category09", "java.lang.String");
		imInfo.setFieldType("category10", "java.lang.String");
		imInfo.setFieldType("category11", "java.lang.String");
		imInfo.setFieldType("category12", "java.lang.String");
		imInfo.setFieldType("category13", "java.lang.String");
		imInfo.setFieldType("category14", "java.lang.String");
		imInfo.setFieldType("category15", "java.lang.String");
		imInfo.setFieldType("category16", "java.lang.String");
		imInfo.setFieldType("category17", "java.lang.String");
		imInfo.setFieldType("category18", "java.lang.String");
		imInfo.setFieldType("category19", "java.lang.String");
		imInfo.setFieldType("category20", "java.lang.String");
		imInfo.setFieldType("category22", "java.lang.String");
		imInfo.setFieldType("category21", "java.lang.String");
		imInfo.setFieldType("enable", "java.lang.String");
		
		return imInfo;
	}
	
	private ImportInfo doT2Initial(ImportInfo imInfo){
		
		imInfo.addFieldName("itemCode"); // 商品品號
		imInfo.addFieldName("supplierItemCode"); // 原廠編號
		imInfo.addFieldName("itemCName"); // 中文名稱/品名
		imInfo.addFieldName("itemEName"); // 英文名稱/品名2
		imInfo.addFieldName("itemBrand"); // T2 商品品牌
		imInfo.addFieldName("category01"); // 大類
		imInfo.addFieldName("category02"); // 中類
		imInfo.addFieldName("category03"); // 小類    
		imInfo.addFieldName("foreignCategory"); // T2 國外類別
		imInfo.addFieldName("category05"); // 年份 
		imInfo.addFieldName("category06"); // 季別
		imInfo.addFieldName("category13"); // 系列 
		imInfo.addFieldName("colorCode"); // T2 色碼
		imInfo.addFieldName("category10"); // 顏色    
		imInfo.addFieldName("category08"); // 材質  
		imInfo.addFieldName("material"); // T2 材質說明
		imInfo.addFieldName("category11"); // 款式編號
		imInfo.addFieldName("category09"); // 款式  
		imInfo.addFieldName("category04"); // 尺寸
		imInfo.addFieldName("validityDay"); // T2 效期天數
		imInfo.addFieldName("releaseString"); // 上市日期
		imInfo.addFieldName("expiryString"); // 下市日期
		imInfo.addFieldName("category14"); // 產地  
		imInfo.addFieldName("category15"); // 功能  
		imInfo.addFieldName("specLength"); // 長
		imInfo.addFieldName("specWidth");  // 寬
		imInfo.addFieldName("specHeight"); // 高
		imInfo.addFieldName("specWeight"); // 重量
		imInfo.addFieldName("description"); // 說明
		imInfo.addFieldName("category07"); // 性別
		imInfo.addFieldName("category12"); // 屬性
		imInfo.addFieldName("category16"); // 樣本編號
		imInfo.addFieldName("category18"); // 其他1  
		imInfo.addFieldName("category19"); // 其他2  
		imInfo.addFieldName("category17"); // 製造商/供應商
		imInfo.addFieldName("salesUnit");  // 交易單位
		imInfo.addFieldName("purchaseUnit"); // 進貨單位
		imInfo.addFieldName("vipDiscount"); // T2 vip折扣
		imInfo.addFieldName("categoryType"); // T2 業種
		imInfo.addFieldName("itemCategory"); // T2 業種子類
		imInfo.addFieldName("purchaseCurrencyCode"); // T2 採購幣別
		imInfo.addFieldName("purchaseAmount"); // T2 原幣成本
		imInfo.addFieldName("isTax"); // T2 稅別
		imInfo.addFieldName("foreignListPrice"); // 原幣零售價
		imInfo.addFieldName("supplierQuotationPrice"); // 原廠報價
		imInfo.addFieldName("margen"); // T2 標準毛利率
		imInfo.addFieldName("maxPurchaseQuantity"); // T2 最高採購量
		imInfo.addFieldName("minPurchaseQuantity"); // T2 最低採購量
		imInfo.addFieldName("itemLevel"); // 商品等級
		imInfo.addFieldName("replenishCoefficient"); // T2 補貨係數
		imInfo.addFieldName("isConsignSale"); // T2 寄賣品
		imInfo.addFieldName("boxCapacity"); // T2 每箱數量
		imInfo.addFieldName("salesRatio"); // 交易與進貨單位比
		imInfo.addFieldName("purchaseRatio"); // T2 預計採購比
		imInfo.addFieldName("isComposeItem"); // 組合商品
		imInfo.addFieldName("isServiceItem"); // T2 是否為服務性商品
		imInfo.addFieldName("category20"); // 帳務類別
		imInfo.addFieldName("enable"); // 啟用
		imInfo.addFieldName("itemType"); // T2 商品類別
		imInfo.addFieldName("allowMinusStock"); // T2 是否允許負庫存
		imInfo.addFieldName("lotControl"); // 批號管理
		imInfo.addFieldName("brandCode"); // 事業體代號
		imInfo.addFieldName("alcolhoPercent");	// 酒精濃度
		//MACO 網購註記欄位
		imInfo.addFieldName("EStore");	// 網購註記
		imInfo.addFieldName("EStoreReserve1");	// 網購分類
		imInfo.addFieldName("declRatio"); // 銷售單位/報關單位換算(預設1)
		imInfo.addFieldName("minRatio"); // 最小單位/銷貨單位的比例換算(預設1)
		imInfo.addFieldName("budgetType"); // T2 預算類別	(預設A)	
		imInfo.addFieldName("reserve1"); // 備註1
		imInfo.addFieldName("reserve2"); // 備註2
		imInfo.addFieldName("reserve3"); // 備註3
		imInfo.addFieldName("reserve4"); // 備註4
		imInfo.addFieldName("payOline"); // 電子支付
//		imInfo.addFieldName("reserve5"); // 備註5
//		imInfo.addFieldName("declRatio"); // 銷售單位/報關單號換算
//		imInfo.addFieldName("minRatio"); // 最小單位/銷貨單位的比例換算
//		imInfo.addFieldName("declRatio"); // 銷售單位/報關單號換算
//		imInfo.addFieldName("minRatio"); // 最小單位/銷貨單位的比例換算
		
//		imInfo.addFieldName("standardPurchaseCost"); // 計價後成本
//		imInfo.addFieldName("accountCode"); // T2 會計科目代號
//		imInfo.addFieldName("customsItemCode"); // T2 海關品號
//		imInfo.addFieldName("bonusType"); // T2 獎金類別
//		imInfo.addFieldName("budgetType"); // T2 預算類別
//		imInfo.addFieldName("taxRelativeItemCode"); // T2 對應稅別品號
		
		// set field type
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("supplierItemCode", "java.lang.String");
		imInfo.setFieldType("itemCName", "java.lang.String");
		imInfo.setFieldType("itemEName", "java.lang.String");
		imInfo.setFieldType("itemBrand", "java.lang.String"); // T2 商品品牌
		imInfo.setFieldType("category01", "java.lang.String");
		imInfo.setFieldType("category02", "java.lang.String");
		imInfo.setFieldType("category03", "java.lang.String");
		imInfo.setFieldType("foreignCategory", "java.lang.String"); // T2 國外類別
		imInfo.setFieldType("category05", "java.lang.String");
		imInfo.setFieldType("category06", "java.lang.String");
		imInfo.setFieldType("category13", "java.lang.String");
		imInfo.setFieldType("colorCode", "java.lang.String"); // T2 色碼
		imInfo.setFieldType("category10", "java.lang.String");
		imInfo.setFieldType("category08", "java.lang.String");
		imInfo.setFieldType("material", "java.lang.String"); // T2 材質說明
		imInfo.setFieldType("category11", "java.lang.String");
		imInfo.setFieldType("category09", "java.lang.String");
		imInfo.setFieldType("category04", "java.lang.String");
		imInfo.setFieldType("validityDay", "java.lang.String"); // T2 效期天數
		imInfo.setFieldType("releaseString", "java.lang.String"); // T2 上市日期字串
		imInfo.setFieldType("expiryString", "java.lang.String");	// T2 下市日期字串
		imInfo.setFieldType("category14", "java.lang.String");
		imInfo.setFieldType("category15", "java.lang.String");
		imInfo.setFieldType("specLength", "java.lang.String");
		imInfo.setFieldType("specWidth", "java.lang.String");
		imInfo.setFieldType("specHeight", "java.lang.String");
		imInfo.setFieldType("specWeight", "java.lang.String");
		imInfo.setFieldType("description", "java.lang.String");
		imInfo.setFieldType("category07", "java.lang.String");
		imInfo.setFieldType("category12", "java.lang.String");
		imInfo.setFieldType("category16", "java.lang.String");
		imInfo.setFieldType("category18", "java.lang.String");
		imInfo.setFieldType("category19", "java.lang.String");
		imInfo.setFieldType("category17", "java.lang.String");
		imInfo.setFieldType("salesUnit", "java.lang.String");
		imInfo.setFieldType("purchaseUnit", "java.lang.String");
		imInfo.setFieldType("vipDiscount", "java.lang.String"); // T2 vip折扣
		imInfo.setFieldType("categoryType", "java.lang.String"); // T2 業種
		imInfo.setFieldType("itemCategory", "java.lang.String"); // T2 業種子類
		imInfo.setFieldType("purchaseCurrencyCode", "java.lang.String"); // T2 採購幣別
		imInfo.setFieldType("purchaseAmount", "java.lang.Double"); // T2 原幣成本
		imInfo.setFieldType("isTax", "java.lang.String"); // T2 稅別
		imInfo.setFieldType("foreignListPrice", "java.lang.Double");
		imInfo.setFieldType("supplierQuotationPrice", "java.lang.Double");
		imInfo.setFieldType("margen", "java.lang.Double"); // T2 毛利率
		imInfo.setFieldType("maxPurchaseQuantity", "java.lang.Double"); // T2 最高採購量
		imInfo.setFieldType("minPurchaseQuantity", "java.lang.Double"); // T2 最低採購量
		imInfo.setFieldType("itemLevel", "java.lang.String");
		imInfo.setFieldType("replenishCoefficient", "java.lang.Double"); // T2 補貨係數
		imInfo.setFieldType("isConsignSale", "java.lang.String"); // T2 寄賣品
		imInfo.setFieldType("boxCapacity", "java.lang.Long"); // T2 每箱容量
		imInfo.setFieldType("salesRatio", "java.lang.Long");
		imInfo.setFieldType("purchaseRatio", "java.lang.Long"); // T2 採購預算分配比率
		imInfo.setFieldType("isComposeItem", "java.lang.String");
		imInfo.setFieldType("isServiceItem", "java.lang.String"); // T2 是否為服務性商品
		imInfo.setFieldType("category20", "java.lang.String");
		imInfo.setFieldType("enable", "java.lang.String");
		imInfo.setFieldType("itemType", "java.lang.String"); // T2 商品類別
		imInfo.setFieldType("allowMinusStock", "java.lang.String"); // T2 是否允許負庫存
		imInfo.setFieldType("lotControl", "java.lang.String");
		imInfo.setFieldType("brandCode", "java.lang.String");
		imInfo.setFieldType("alcolhoPercent", "java.lang.Double"); //酒精濃度
		
		//MACO 網購註記欄位
		imInfo.setFieldType("EStore", "java.lang.String"); // 網購註記
		imInfo.setFieldType("EStoreReserve1", "java.lang.String"); // 網購分類
		imInfo.setFieldType("declRatio", "java.lang.Double"); // 銷售單位/報關單位換算(預設1)
		imInfo.setFieldType("minRatio", "java.lang.Double"); // 最小單位/銷貨單位的比例換算(預設1)
		imInfo.setFieldType("budgetType", "java.lang.String"); // T2 預算類別	(預設A)
		imInfo.setFieldType("reserve1", "java.lang.String"); // 備註1
		imInfo.setFieldType("reserve2", "java.lang.String"); // 備註2
		imInfo.setFieldType("reserve3", "java.lang.String"); // 備註3
		imInfo.setFieldType("reserve4", "java.lang.String"); // 備註4
		imInfo.setFieldType("payOline", "java.lang.String"); // 電子支付
//		imInfo.setFieldType("reserve5", "java.lang.String"); // 備註5
//		imInfo.setFieldType("declRatio", "java.lang.Double");
//		imInfo.setFieldType("minRatio", "java.lang.Double");
		
		
//		imInfo.setFieldType("standardPurchaseCost", "java.lang.Double");
//		imInfo.setFieldType("accountCode", "java.lang.String"); // T2 會計科目代號
//		imInfo.setFieldType("customsItemCode", "java.lang.String"); // T2 海關品號
//		imInfo.setFieldType("bonusType", "java.lang.String"); // T2 獎金類別
//		imInfo.setFieldType("budgetType", "java.lang.String"); // T2 預算類別
//		imInfo.setFieldType("taxRelativeItemCode", "java.lang.String"); // T2 對應稅別品號
		
		return imInfo;
	}
	
	
	/**
	 * im item price detail config
	 * 
	 * @return
	 */
	private ImportInfo getImItemPrice(HashMap uiProperties, String brandCode) {
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue(ImportDBService.DEFAULT_KEY_HEAD + "1");

		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImItemPrice.class.getName());

		imInfo.addFieldName("key");
		// item price
		imInfo.setFieldType("key", "java.lang.String");
		
		if(brandCode.indexOf("T2") <= -1){
			imInfo.addFieldName("typeCode");
			imInfo.addFieldName("unitPrice");
			imInfo.addFieldName("isTax");
			imInfo.addFieldName("taxCode");
			
			imInfo.setFieldType("typeCode", "java.lang.String");
			imInfo.setFieldType("unitPrice", "java.lang.Double");
			imInfo.setFieldType("isTax", "java.lang.String");
			imInfo.setFieldType("taxCode", "java.lang.String");
		}else{
			imInfo.addFieldName("unitPrice");
			
			imInfo.setFieldType("unitPrice", "java.lang.Double");
		}
		
		
		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);		
		HashMap defaultValue = new HashMap();
		defaultValue.put("creationDate", new Date());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("createdBy", user.getEmployeeCode() );
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode() );
		imInfo.setDefaultValue(defaultValue);

		return imInfo;
	}

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
    	log.info("ImItemUpdateImportData.updateDB");
    	ImItemService imItemService = (ImItemService) SpringUtils.getApplicationContext().getBean("imItemService");
    	
    	ImItemPriceService imItemPriceService = (ImItemPriceService) SpringUtils.getApplicationContext().getBean("imItemPriceService");
    	ImItemPriceDAO imItemPriceDAO =  (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");
    	
    	StringBuffer result = new StringBuffer();

    	String tmpBrandCode = null;
    	String tmpImportLotNo = null;
    	
    	String yyyyMMddHHMMSS = DateUtils.format( new Date(), "yyyyMMddHHmmss"); 
    	
    	int allCount = 0;
    	int correctCount = 0 ;
    	int failCount = 0 ;
    	for (int index = 0; index < entityBeans.size(); index++) {
    	    ImItem imItem = (ImItem) entityBeans.get(index); // 匯入的商品

    	    log.info("imItem.getSalesRatio() 匯入過來的 = " + imItem.getSalesRatio());

//    	    imItem.setBrandCode(imItem.getReserve5());
    	    String loginBrandCode = imItem.getReserve5(); // 作業人員登入品牌
    	    String brandCode = imItem.getBrandCode();	  // 匯入打的品牌

    	    if( null == tmpBrandCode){
    		tmpBrandCode = brandCode;
    	    }

    	    if(StringUtils.hasText(imItem.getItemCode())){
    		imItem.setItemCode(imItem.getItemCode().trim().toUpperCase());
    	    }else{
    		imItem.setItemCode("");
    	    }
		    
    	    if ((null != imItem) && (StringUtils.hasText(imItem.getItemCode())) ) { // && (StringUtils.hasText(imItem.getBrandCode()))
    		ImItem oldItem = imItemService.findItem(imItem.getBrandCode(), imItem.getItemCode());
    		// read old data
    		if (null == oldItem) { // 匯入的商品品號資料在資料庫沒有
    		    log.info("ImItemImportData.updateDB.create " + imItem.getItemCode());
    		    try {
    			doValidateImItemBrandCode(imItem, index);
    			
    			throw new Exception("資料不存在 <br/>");
    		    } catch (Exception ex) {
    			failCount++;
    			ex.printStackTrace();
    			log.error("匯入商品資料有問題 品號: " + imItem.getItemCode()+ ex.getMessage() );
    			result.append("匯入商品資料有問題 品號: " + imItem.getItemCode() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
    		    }

    		} else { // 資料庫已存在此品牌的商品
    		    
    		    try {
    			// 複製資料
    			copyImportItemToDBItem(oldItem, imItem);
    			
    			checkImItem(oldItem, loginBrandCode, brandCode, index);
    			// 設定匯入批號
    			
    			oldItem.setImportLotNo(yyyyMMddHHMMSS);
    			
    			if(null == tmpImportLotNo){
    			    tmpImportLotNo = oldItem.getImportLotNo();
    			}
    			
    			imItemService.update(oldItem);
    			correctCount++;
    			result.append("修改商品" + imItem.getItemCode() + "匯入<br/>");
    		    } catch (Exception ex) {
    			failCount++;
    			ex.printStackTrace();
    			log.error("匯入商品資料有問題 品號: " + imItem.getItemCode());
    			result.append("匯入商品資料有問題 品號: " + imItem.getItemCode() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
    		    }
    		}
    	    }else{
    		log.info("imItem is null");
    		log.info("imItem.imitemCode is null");

    	    }
    	    if(StringUtils.hasText(imItem.getItemCode())){
    		allCount++;
    	    }

    	}
    	result.append("匯入總筆數 : " + allCount  + " 成功: " + correctCount + " 失敗: " + failCount );
    	if(tmpBrandCode.indexOf("T2") > -1 && null != tmpImportLotNo ){
    	    result.append( "<br/>匯入批號: " + tmpImportLotNo );
    	}
    	return result.toString();
    }    
    
    /**
     * 設定預設值
     * @param imItem
     * @return
     * @throws Exception
     */
    public ImItem setDefaultValues(ImItem imItem) throws Exception {
	if(  imItem.getBrandCode().indexOf("T2") <= -1 ){		// 非  T2  2009.11.02 arthur 
	    if( !StringUtils.hasText(imItem.getItemCategory()) ) 	// 預設業種(商品類別)為品牌 
		imItem.setItemCategory( imItem.getBrandCode() );	
	    if( !StringUtils.hasText(imItem.getBudgetType()) ) 		// 預設扣預算方式為 A:ALL
		imItem.setBudgetType("A") ;
	    if( !StringUtils.hasText(imItem.getIsTax()) ) 		// 預設稅別 P
		imItem.setIsTax("P") ;
	}else{
	    if( imItem.getStandardPurchaseCost() == null ){ 		// 預設標準進價為0
		imItem.setStandardPurchaseCost(0d) ;
	    }
	}
	return imItem;

    }
    
    /**
     * 檢核 ImItem
     * @param imItem
     * @return
     * @throws Exception
     */
    public void doValidateImItemBrandCode(ImItem imItem, int index) throws Exception {
    	
    	BuBrandDAO buBrandDAO = (BuBrandDAO) SpringUtils.getApplicationContext().getBean("buBrandDAO");
    	
    	log.info("imItem.getReserve5() = " + imItem.getReserve5());
    	// 依品牌匯入
    	String brandCode = imItem.getBrandCode();
		
    	// 品牌
    	if(!StringUtils.hasText(brandCode)){
    	    throw new NoSuchDataException("品號:"+imItem.getItemCode()+"未輸入品牌");
    	}else{
    	    BuBrand buBrand = buBrandDAO.findById(brandCode);
    	    if (null == buBrand) {
    		throw new NoSuchDataException("品號：" + imItem.getItemCode()
    			+ "依據品牌：" + brandCode + "查無其品牌代號！");
    	    }else{
    		// 檢核登入人員與匯入所打的品牌是否一致
    		if(!brandCode.equals(imItem.getReserve5())){
    		    throw new Exception("作業人員登入品牌: "+imItem.getReserve5()+"與新增品號品牌: " + brandCode +" 不符合，請檢查!");
    		}else{
    		    imItem.setReserve5(null);
    		}
    	    }
    	}
    }	
    
    /**
     * 檢核 ImItem
     * @param imItem
     * @return
     * @throws Exception
     */
    public ImItem checkImItem(ImItem imItem, String loginBrandCode, String importBrandCode, int index) throws Exception {
    	
    	BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
    	BuBrandDAO buBrandDAO = (BuBrandDAO) SpringUtils.getApplicationContext().getBean("buBrandDAO");
    	ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean("imItemCategoryDAO");
    	ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean("imItemEancodeDAO");
    	
    	log.info("imItem.getReserve5() = " + imItem.getReserve5());
    	// 依品牌匯入
    	String brandCode = imItem.getBrandCode();
    	String itemCode = imItem.getItemCode();
    	// 品牌
    	if(!StringUtils.hasText(importBrandCode)){
    	    throw new NoSuchDataException("品號:"+itemCode+"未輸入品牌");
    	}else{
    	    BuBrand buBrand = buBrandDAO.findById(importBrandCode);
    	    if (null == buBrand) {
    		throw new NoSuchDataException("品號：" + itemCode
    			+ "依據品牌：" + importBrandCode + "查無其品牌代號！");
    	    }else{
    		// 檢核登入人員與匯入所打的品牌是否一致
    		if(!importBrandCode.equals(loginBrandCode)){
    		    throw new Exception("作業人員登入品牌: "+importBrandCode+"與新增品號品牌: " + loginBrandCode +" 不符合，請檢查!");
    		}else{
    		    imItem.setReserve5(null);
    		}
    	    }
    	}
    	
    	// 商品主檔
    	if(!StringUtils.hasText(itemCode)){
    	    throw new NoSuchDataException("第"+(index+1)+"筆品號未輸入");
    	}else{
    	    log.info("brandCode = " + brandCode);
    	    log.info("itemCode = " + itemCode);
    	    log.info("itemCode.length() = " + itemCode.length());
    	    if( brandCode.indexOf("T2") > -1 ){
    		if(itemCode.length() > 13 ){
    		    throw new NoSuchDataException("品號：" + itemCode + "必須小於13碼以下" );
    		}
    	    }
    	}
    	
    	// 品號
    	log.info("Pattern.compile([^a-zA-Z\\d\\s]+) = " + Pattern.compile("[^a-zA-Z\\d\\s]+").matcher( itemCode ).find());
    	if( Pattern.compile("[^a-zA-Z\\d\\s]+").matcher( itemCode ).find() ){
    	    throw new NoSuchDataException("品號：" + itemCode
    		    + "不可有特殊符號，請重新輸入!");
    	}else if(StringUtils.containsWhitespace(itemCode)){
    	    throw new NoSuchDataException("品號：" + itemCode
    		    + "含有空白，請檢查");
    	}
    	
    	// 批號管理
    	if (!(StringUtils.hasText(imItem.getLotControl()))) {
    	    if(brandCode.indexOf("T2") > -1){
    		imItem.setLotControl("N");
    	    }else{
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入批號管理(LotControl)欄位，請檢查!");
    	    }
    	} else {
    	    List<BuCommonPhraseLine> allLotControls = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "LotControl", imItem.getLotControl(), "Y" } );
    	    if(null == allLotControls || allLotControls.size() == 0){
    		throw new NoSuchDataException("品號:" + itemCode+ " 批號管理(LotControl)欄位錯誤，非N/1/2");
    	    }
    	}
    	
    	// 組合商品
    	if(!StringUtils.hasText(imItem.getIsComposeItem())){
    	    imItem.setIsComposeItem("N");
    	}else{
    	    if( !"Y".equals(imItem.getIsComposeItem()) && !"N".equals(imItem.getIsComposeItem()) ){
    		throw new Exception("品號:" + itemCode +  " 是否為組合商品欄位錯誤，非Y/N");
    	    }
    	}    

    	// 服務性商品
    	if (!StringUtils.hasText(imItem.getIsServiceItem())) {
    	    imItem.setIsServiceItem("N");
    	}else{
    	    if( !"Y".equals(imItem.getIsServiceItem()) && !"N".equals(imItem.getIsServiceItem()) ){
    		throw new Exception("品號:" + itemCode +  " 是否為服務性商品欄位錯誤，非Y/N");
    	    }
    	}    

    	log.info("imItem.getSalesRatio() before= " + imItem.getSalesRatio());
    	// 交易與進貨單位比
    	if ( null == imItem.getSalesRatio() || 0 == imItem.getSalesRatio() ) {
    	    imItem.setSalesRatio(1L);
    	}else{
    	    log.info("imItem.getSalesRatio().toString() = " + imItem.getSalesRatio().toString());
    	    if(!ValidateUtil.isNumber(imItem.getSalesRatio().toString())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ " 交易與進貨單位比:"+ imItem.getSalesRatio() + "必須為數值");
    	    }
    	}
    	log.info("imItem.getSalesRatio() after= " + imItem.getSalesRatio());

    	// 交易單位
    	if(!StringUtils.hasText(imItem.getSalesUnit())){
    	    throw new NoSuchDataException("品號:" + itemCode
    		    + "未輸入交易單位");
    	}else{
    	    log.info("imItem.getSalesUnit() = " + imItem.getSalesUnit() );
    	    List<BuCommonPhraseLine> allItemUnit = null;
    	    if(brandCode.indexOf("T2") > -1){
    		allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "T2ItemUnit", imItem.getSalesUnit(), "Y" } );
    	    }else{
    		allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "ItemUnit", imItem.getSalesUnit(), "Y" } );
    	    } 
    	    log.info("allItemUnit = " + allItemUnit ); 
    	    log.info("allItemUnit.size() = " + allItemUnit.size() );
    	    if( null == allItemUnit || allItemUnit.size() == 0 ){
    		throw new NoSuchDataException("查品號:" + itemCode
    			+ "交易單位:"+ imItem.getSalesUnit() +"不存在");
    	    }
    	}

    	// 進貨單位
    	if(!StringUtils.hasText(imItem.getPurchaseUnit())){
    	    throw new NoSuchDataException("品號:" + itemCode
    		    + "未輸入進貨單位");
    	}else{
    	    List<BuCommonPhraseLine> allItemUnit = null;
    	    if(brandCode.indexOf("T2") > -1){
    		allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "T2ItemUnit", imItem.getPurchaseUnit(), "Y" } );
    	    }else{
    		allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "ItemUnit", imItem.getPurchaseUnit(), "Y" } );
    	    }
    	    if( null == allItemUnit || allItemUnit.size() == 0 ){
    		throw new NoSuchDataException("查品號:" + itemCode
    			+ "進貨單位:"+ imItem.getPurchaseUnit() +"不存在");
    	    }
    	}

    	// 預計採購比
    	if ( null == imItem.getPurchaseRatio()) {
    	    imItem.setPurchaseRatio(0L);
    	}else{
    	    if(!ValidateUtil.isNumber(imItem.getPurchaseRatio().toString())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "預計採購比:"+imItem.getPurchaseRatio()+"必須為數值");
    	    }
    	}

    	// 每箱數量
    	if ( null == imItem.getBoxCapacity() || 0 == imItem.getBoxCapacity() ) {
    	    imItem.setBoxCapacity(1L);
    	}else{
    	    if(!ValidateUtil.isNumber(imItem.getBoxCapacity().toString())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "每箱數量:"+imItem.getBoxCapacity()+"必須為數值");
    	    }
    	}
    	
    	// 年份
    	if(StringUtils.hasText(imItem.getCategory05())){
    	    ImItemCategory category05 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY05, imItem.getCategory05(), "Y");
    	    if(null == category05){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "年份:"+imItem.getCategory05()+"不存在");
    	    }	
    	}

    	// 季別  目前沒在使用 所以不驗證
    	if(StringUtils.hasText(imItem.getCategory06())){
    	    ImItemCategory category06 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY06, imItem.getCategory06(), "Y");
    	    if(null == category06){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "季別:"+imItem.getCategory06()+"不存在");
    	    }
    	}
    	
    	// 性別
    	if(StringUtils.hasText(imItem.getCategory07())){
    	    ImItemCategory category07 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY07, imItem.getCategory07(), "Y");
    	    if(null == category07){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "性別:"+imItem.getCategory07()+"不存在");
    	    }
    	}
    	
    	// 屬性　
    	if(StringUtils.hasText(imItem.getCategory12())){
    	    ImItemCategory category12 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY12, imItem.getCategory12(), "Y");
    	    if(null == category12){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "屬性:"+imItem.getCategory12()+"不存在");
    	    }
    	}
    	
    	// 帳務類別
    	if(!StringUtils.hasText(imItem.getCategory20())){
    	    imItem.setCategory20("01");
    	}else{
    	    List<BuCommonPhraseLine> allCategory20 = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "ItemCategory20", imItem.getCategory20(), "Y" } );
    	    if( null == allCategory20 || allCategory20.size() == 0){
    		throw new Exception("品號:" + itemCode +  " 帳務類別欄位:"+ imItem.getCategory20() +"不存在");
    	    }	
    	}
    	
    	// 補貨係數
    	if(null == imItem.getReplenishCoefficient()){
    	    imItem.setReplenishCoefficient(1D);
    	}
    	
    	if(brandCode.indexOf("T2") > -1){
    	    // 打品號,檢查不能等於B品號的國際碼
    	    ImItemEancode line = (ImItemEancode)imItemEancodeDAO.findOneEanCodeByProperty(brandCode, itemCode,itemCode) ;
    	    if(null != line){
    		throw new FormException("品號:" + itemCode
    			+ " 不能等於品號:"+line.getItemCode()+" 已啟用的國際碼:" + line.getEanCode());
    	    }
    	    
    	    // 檢核大類中類,商品品牌,業種,業種子類,稅別,原幣成本必填

    	    // 中文名稱/品名
    	    if(StringUtils.hasText(imItem.getItemCName())){
    		if(imItem.getItemCName().length() > 24 ){
    		    throw new NoSuchDataException("品號：" + itemCode + "的品名必須小於24碼以下" );
    		}
    	    }

    	    // 英文名稱/其他品名
    	    if(StringUtils.hasText(imItem.getItemEName())){
    		if(imItem.getItemEName().length() > 200 ){
    		    throw new NoSuchDataException("品號：" + itemCode + "的其他品名必須小於200碼以下" );
    		}
    	    }

    	    // 大類
    	    if(!StringUtils.hasText(imItem.getCategory01())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入大類");
    	    }else{
    		ImItemCategory imItemCategory01 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY01, imItem.getCategory01(), "Y");
    		if(null == imItemCategory01){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "大類:"+imItem.getCategory01()+"不存在");
    		}
    	    }
    	    // 中類
    	    if(!StringUtils.hasText(imItem.getCategory02())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入中類");
    	    }else{
    		ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), "Y");
    		if(null == imItemCategory02){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "中類:"+imItem.getCategory02()+"不存在");
    		}else{
    		    ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), imItem.getCategory01(), "Y");
    		    if( null == imItemCategory02s ){
    			throw new NoSuchDataException("品號:" + itemCode
    				+ "大類"+imItem.getCategory01()+"不包含中類:"+imItem.getCategory02());
    		    }
    		}
    	    }

    	    // 製造商/供應商
    	    if(!StringUtils.hasText(imItem.getCategory17())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入製造商/供應商");
    	    }else{
    		List<BuSupplierWithAddressView> buSupplierWithAddressView = baseDAO.findByProperty("BuSupplierWithAddressView", "and brandCode = ? and supplierCode = ? ", new Object[]{ brandCode, imItem.getCategory17() } );
    		if(null == buSupplierWithAddressView || buSupplierWithAddressView.size() == 0 ){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "製造商/供應商:"+imItem.getCategory17()+"不存在");
    		}
    	    }

    	    // vip折扣
    	    if(!StringUtils.hasText(imItem.getVipDiscount())){
    		throw new Exception("品號:" + itemCode +  " 未輸入vip折扣");
    	    }else{
    		List<BuCommonPhraseLine> allVipDiscount = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "VipDiscount", imItem.getVipDiscount(), "Y" } );
    		if( null == allVipDiscount || allVipDiscount.size() == 0 ){
    		    throw new Exception("品號:" + itemCode +  " vip折扣代碼:"+imItem.getVipDiscount()+"不存在");
    		}
    	    }

    	    // 業種
    	    if(!StringUtils.hasText(imItem.getCategoryType())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入業種");
    	    }else{
    		ImItemCategory imItemCategory00 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY00, imItem.getCategoryType(), "Y");
    		if(null == imItemCategory00){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "業種:"+imItem.getCategoryType()+"不存在");
    		}
    	    }

    	    // 寄賣品
    	    if(!StringUtils.hasText(imItem.getIsConsignSale())){
    		imItem.setIsConsignSale("N");
    	    }else{
    		if( !"Y".equals(imItem.getIsConsignSale()) && !"N".equals(imItem.getIsConsignSale()) ){
    		    throw new Exception("品號:" + itemCode +  " 寄賣品欄位:"+imItem.getIsConsignSale()+"錯誤，非Y/N");
    		}
    	    }

    	    // 海關品號
    	    if(!StringUtils.hasText(imItem.getCustomsItemCode())){
    		imItem.setCustomsItemCode(itemCode);
    	    }

    	    // 商品類別 NGSE
    	    if(!StringUtils.hasText(imItem.getItemType())){
    		imItem.setItemType("N");
    	    }else{
    		List<BuCommonPhraseLine> allItemType = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "ItemType", imItem.getItemType(), "Y" } );
    		if(null == allItemType || allItemType.size() == 0 ){
    		    throw new Exception("品號:" + itemCode +  " 商品類別:"+imItem.getItemType()+"，不存在");
    		}
    	    }

    	    // 商品品牌
    	    log.info("imItem.getItemBrand() = " + imItem.getItemBrand());
    	    if(!StringUtils.hasText(imItem.getItemBrand())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入商品品牌");
    	    }else{
    		ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.ITEM_BRAND, imItem.getItemBrand(), "Y");
    		if(imItemCategory == null){
    		    throw new FormException("品號:" + itemCode
    			    + "的商品品牌" + imItem.getItemBrand() + "不存在");
    		}
    	    }

    	    // 獎金類別 待anber

    	    // 標準毛利率
    	    if(null == imItem.getMargen()){
    		imItem.setMargen(0d);
    	    }

    	    // 最高採購量
    	    if(null == imItem.getMaxPurchaseQuantity()){
    		imItem.setMaxPurchaseQuantity(0d);
    	    }

    	    // 最低採購量
    	    if(null == imItem.getMinPurchaseQuantity()){
    		imItem.setMinPurchaseQuantity(0d);
    	    }

    	    // 稅別
    	    if(!StringUtils.hasText(imItem.getIsTax())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入稅別");
    	    }else{
    		if( !"F".equals(imItem.getIsTax()) && !"P".equals(imItem.getIsTax()) ){
    		    throw new Exception("品號:" + itemCode +  " 稅別:"+imItem.getIsTax()+"欄位錯誤，非F(保稅)/P(免稅)");
    		}	
    	    }

    	    // 採購幣別
    	    if(!StringUtils.hasText(imItem.getPurchaseCurrencyCode())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入採購幣別");
    	    }else{
    		List<BuCurrency> allCurrencyCodes = baseDAO.findByProperty("BuCurrency", "and currencyCode = ? ", new Object[]{imItem.getPurchaseCurrencyCode()});
    		log.info("allCurrencyCodes = " + allCurrencyCodes );
    		if(null == allCurrencyCodes || allCurrencyCodes.size() == 0 ){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "採購幣別:"+imItem.getPurchaseCurrencyCode()+ "不存在");
    		}
    	    }

    	    // 業種子類
    	    if(!StringUtils.hasText(imItem.getItemCategory())){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入業種子類");
    	    }else{
    		ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.ITEM_CATEGORY, imItem.getItemCategory(), "Y");
    		if(null == imItemCategory){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "業種子類:"+imItem.getItemCategory()+"不存在");
    		}else{
    		    // 檢核是否為業種下的分類
    		    ImItemCategory imItemCategoryParent = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.ITEM_CATEGORY, imItem.getItemCategory(), imItem.getCategoryType(), "Y");
    		    if( null == imItemCategoryParent ){
    			throw new NoSuchDataException("品號:" + itemCode
    				+ "業種:"+imItem.getCategoryType()+" 不包含業種子類"+imItem.getItemCategory());
    		    }
    		}
    	    }

    	    // 原幣成本
    	    log.info("imItem.getPurchaseAmount() = " + imItem.getPurchaseAmount());
    	    if(  null == imItem.getPurchaseAmount() ){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "未輸入原幣成本");
    	    }else if(imItem.getPurchaseAmount() < 0d ){
    		throw new NoSuchDataException("品號:" + itemCode
    			+ "原幣成本不得小於0");
    	    }else{
    		// 到小數六碼
    		imItem.setPurchaseAmount(NumberUtils.round(imItem.getPurchaseAmount(), 6));
    	    }

    	    // 銷售單位/報關單號換算
//    	    if(null == imItem.getDeclRatio()){
//    		throw new NoSuchDataException("品號:" + itemCode
//    			+ "未輸入銷售單位/報關單號換算");
//    	    }
//
//    	    // 最小單位/銷貨單位的比例換算
//    	    if(null == imItem.getMinRatio()){
//    		throw new NoSuchDataException("品號:" + itemCode
//    			+ "未輸入最小單位/銷貨單位的比例換算");
//    	    }
    	    
    	    // 負庫存
    	    if(!StringUtils.hasText(imItem.getAllowMinusStock())){
    		imItem.setAllowMinusStock("N");
    	    }else{
    		if( !"Y".equals(imItem.getAllowMinusStock()) && !"N".equals(imItem.getAllowMinusStock()) ){
    		    throw new Exception("品號:" + itemCode +  " 負庫存欄位錯誤，非Y/N");
    		}else{
    		    if("Y".equalsIgnoreCase((imItem.getAllowMinusStock()))){
    			if("F".equalsIgnoreCase(imItem.getIsTax())){
    			    throw new NoSuchDataException("品號:" + itemCode
    				    + "允許負庫存的稅別不得為保稅");
    			}else if(!"N".equalsIgnoreCase(imItem.getLotControl())){
    			    throw new NoSuchDataException("品號:" + itemCode
    				    + "允許負庫存的批號管理只能為 N-不執行批號管理");
    			}
    		    }
    		}
    	    }
    	    
        	// 酒精濃度
        	if ( null == imItem.getAlcolhoPercent()) {
        	    imItem.setAlcolhoPercent(0.0);
        	}else{
        		log.info("-------------------------------------酒精濃度-------------------");
        		log.info(ValidateUtil.isNumber(imItem.getAlcolhoPercent().toString()));
        	    if(imItem.getAlcolhoPercent() > 100 || imItem.getAlcolhoPercent() < 0){
        		throw new NoSuchDataException("品號:" + itemCode
        			+ "酒精濃度:"+imItem.getAlcolhoPercent()+"必須數值 <= 100 或 >= 0");
        	    }
        	}
    
        	
        	// 電子支付
			if (!StringUtils.hasText(imItem.getPayOline())){
				throw new NoSuchDataException("品號:" + itemCode + "未輸入電子支付選項");
			}else{ 
				if (!"Y".equals(imItem.getPayOline()) && !"N".equals(imItem.getPayOline())){ 
					throw new NoSuchDataException("品號:" + itemCode + "電子支付選項錯誤，非Y/N");
				}
				if ("A01".equals(imItem.getCategory02())||
					"A02".equals(imItem.getCategory02())||
					"A03".equals(imItem.getCategory02())){
					if ("N".equals(imItem.getPayOline())){
						throw new NoSuchDataException("品號:" + itemCode + "中類為不可折抵、累計");
						}
				}else{
					if ("Y".equals(imItem.getPayOline())){
						throw new NoSuchDataException("品號:" + itemCode + "中類為可折抵、累計");
						}
				}
			}
        	
    	}else{ // 百貨
    	    
    	    // 大類
    	    if(StringUtils.hasText(imItem.getCategory01())){
    		ImItemCategory imItemCategory01 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY01, imItem.getCategory01(), "Y");
    		if(null == imItemCategory01){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "大類:"+imItem.getCategory01()+"不存在");
    		}
    	    }
    	    // 中類
    	    if(StringUtils.hasText(imItem.getCategory02())){
    		ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), "Y");
    		if(null == imItemCategory02){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "中類:"+imItem.getCategory02()+"不存在");
    		}else{
    		    if(StringUtils.hasText(imItem.getCategory01())){
    			ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), imItem.getCategory01(), "Y");
    			if( null == imItemCategory02s ){
    			    throw new NoSuchDataException("品號:" + itemCode
    				    + "大類"+imItem.getCategory01()+"不包含中類:"+imItem.getCategory02());
    			}
    		    }
    		}
    	    }
    	    
    	    // 小類
    	    if(StringUtils.hasText(imItem.getCategory03())){
    		ImItemCategory imItemCategory03 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), "Y");
    		if(null == imItemCategory03){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "小類:"+imItem.getCategory03()+"不存在");
    		}else{
    		    if(StringUtils.hasText(imItem.getCategory02())){
    			ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), imItem.getCategory02(), "Y");
    			if( null == imItemCategory02s ){
    			    throw new NoSuchDataException("品號:" + itemCode
    				    + "中類"+imItem.getCategory02()+"不包含小類:"+imItem.getCategory03());
    			}
    		    }
    		}
    	    }
    	    
    	    // 系列
    	    if(StringUtils.hasText(imItem.getCategory13())){
    		ImItemCategory category13 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY13, imItem.getCategory13(), "Y");
    		if(null == category13){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "系列:"+imItem.getCategory13()+"不存在");
    		}
    	    }
    	    
    	    // 製造商/供應商
    	    if(StringUtils.hasText(imItem.getCategory17())){
    		List<BuSupplierWithAddressView> buSupplierWithAddressView = baseDAO.findByProperty("BuSupplierWithAddressView", "and brandCode = ? and supplierCode = ? ", new Object[]{ brandCode, imItem.getCategory17() } );
    		if(null == buSupplierWithAddressView || buSupplierWithAddressView.size() == 0 ){
    		    throw new NoSuchDataException("品號:" + itemCode
    			    + "製造商/供應商:"+imItem.getCategory17()+"不存在");
    		}
    	    }
    	    
    	    // 是否寄賣品
    	    if (!StringUtils.hasText(imItem.getIsConsignSale())) {
    		imItem.setIsConsignSale("N");
    	    }

    	    // 會計
    	    if (!StringUtils.hasText(imItem.getAccountCode())) {
    		imItem.setAccountCode("4101001");
    	    }

    	    // 商品類別
    	    if (!StringUtils.hasText(imItem.getItemType())) {
    		imItem.setItemType("N");
    	    }

    	    // 業種
    	    if (!StringUtils.hasText(imItem.getCategoryType())) {
    		imItem.setCategoryType(brandCode);
    	    }

    	    // COACH手錶品號新舊自動轉換
    	    // POS資料交換時
    	    if ("HW".equals(imItem.getCategory01())) {
    		if ("W".equals(itemCode.substring(2, 3))) {
    		    imItem.setReserve5(itemCode.substring(0, 2)
    			    + itemCode.substring(3));
    		} else {
    		    throw new FormException("品號:" + itemCode
    			    + "其大類為手錶，但其品號第三並非「W」");
    		}
    	    } else {
    		imItem.setReserve5("");
    	    }
    	}

    	log.info("imItem.getItemId() = " + imItem.getItemId());

    	// 商品啟用
    	if (!(StringUtils.hasText(imItem.getEnable()))) {
    	    if(brandCode.indexOf("T2") > -1){
    		imItem.setEnable("Y");
    	    }
    	} else {
    	    if (!"Y".equals(imItem.getEnable()) && !"N".equals(imItem.getEnable()))
    		throw new NoSuchDataException("品號:" + itemCode+ " 啟用(Enable)欄位錯誤，非Y/N");
    	}
    	
    	// 重量
		if (StringUtils.hasText(imItem.getSpecWeight())) {
			if (imItem.getSpecWeight().length() > 40) {
				throw new NoSuchDataException("品號：" + itemCode + "的重量必須小於40碼以下");
			}
		}
		
    	return imItem; 
    }
    
    /**
     * 檢核 ImItemPrice
     * @param imItemPrice
     * @return
     * @throws Exception
     */
    public ImItemPrice doValidateImItemPrice(ImItemPrice imItemPrice, ImItem imItem) throws Exception {
    	
    	
    	String brandCode = imItemPrice.getBrandCode();
    	
    	
    	if(brandCode.indexOf("T2") <= -1){
    	    if(!StringUtils.hasText(imItemPrice.getEnable())){
    		throw new Exception("未輸入價格是否啟用，請檢查!");
    	    }else{
    		if( !"Y".equals(imItemPrice.getEnable()) && !"N".equals(imItemPrice.getEnable()) ){
    		    throw new Exception("價格是否啟用必須為Y或N值，請檢查!");
    		}
    	    }

    	    if(!StringUtils.hasText(imItemPrice.getTypeCode())){
    		throw new Exception("未輸入價格類別，請檢查!");
    	    }else{
    		if( !"1".equals(imItemPrice.getTypeCode()) ){
    		    throw new Exception("價格類別必須為1，請檢查!");
    		}
    	    }
//  	    if(!StringUtils.hasText(imItemPrice.getIsTax())){
//  	    throw new Exception("未輸入含稅價，請檢查!");
//  	    }else{
//  	    if( !"P".equals(imItemPrice.getIsTax()) &&  !"F".equals(imItemPrice.getIsTax()) ){
//  	    throw new Exception("含稅價必須為P(完稅)或F(免稅)，請檢查!");
//  	    }
//  	    }
    	    imItemPrice.setIsTax("P");

    	    if(!StringUtils.hasText(imItemPrice.getTaxCode())){
    		throw new Exception("未輸入稅別，請檢查!");
    	    }else{
    		if( !"1".equals(imItemPrice.getTaxCode()) &&  !"2".equals(imItemPrice.getTaxCode()) && !"3".equals(imItemPrice.getTaxCode())){
    		    throw new Exception("稅別必須為1(免稅)或2(零稅)或3(應稅)，請檢查!");
    		}
    	    }
    	}else{ // T2
    	    // 價格類別
    	    if(!StringUtils.hasText(imItemPrice.getTypeCode())){
    		imItemPrice.setTypeCode("1");
    	    }

    	    // 含稅價
    	    if(!StringUtils.hasText(imItemPrice.getIsTax())){
    		imItemPrice.setIsTax(imItem.getIsTax());
    	    } 

    	    // 稅別1(免稅)或2(零稅)或3(應稅)，請檢查!
    	    if(!StringUtils.hasText(imItemPrice.getTaxCode())){
    		imItemPrice.setTaxCode("3");
    	    }
    	}

//    	if(null==imItemPrice.getUnitPrice()){
//    	    throw new Exception("未輸入目前價格，請檢查!");			
//    	}else{
//    	    if( imItemPrice.getUnitPrice() < 0d ){
//    		throw new Exception("目前價格:"+imItemPrice.getUnitPrice()+"必須大於0，請檢查!");
//    	    }
//    	}
    	
    	return imItemPrice;
    }
    
    
    
    /**
     * 複製DATA
     * @param oldItem DBItem 已存在資料庫的bean
     * @param imItem  匯入資料庫的bean
     */
    public void copyImportItemToDBItem(ImItem oldItem, ImItem imItem) throws Exception {
    	log.info("oldItem = " + oldItem);
    	log.info("imItem = " + imItem);
    	
    	log.info("imItem.getBoxCapacity() = " + imItem.getBoxCapacity());
    	log.info("imItem.getBrandCode() = " + imItem.getBrandCode());
    	log.info("imItem.getCategory01() = " + imItem.getCategory01());
    	log.info("imItem.getCategory02() = " + imItem.getCategory02());
    	log.info("imItem.getCategory03() = " + imItem.getCategory03());
    	log.info("imItem.getCategory04() = " + imItem.getCategory04());
    	log.info("imItem.getCategory05() = " + imItem.getCategory05());
    	log.info("imItem.getCategory06() = " + imItem.getCategory06());
    	log.info("imItem.getCategory07() = " + imItem.getCategory07());
    	log.info("imItem.getCategory08() = " + imItem.getCategory08());
    	log.info("imItem.getCategory09() = " + imItem.getCategory09());
    	log.info("imItem.getCategory10() = " + imItem.getCategory10());
    	log.info("imItem.getCategory11() = " + imItem.getCategory11());
    	log.info("imItem.getCategory12() = " + imItem.getCategory12());
    	log.info("imItem.getCategory13() = " + imItem.getCategory13());
    	log.info("imItem.getCategory14() = " + imItem.getCategory14());
    	log.info("imItem.getCategory15() = " + imItem.getCategory15());
    	log.info("imItem.getCategory16() = " + imItem.getCategory16());
    	log.info("imItem.getCategory17() = " + imItem.getCategory17());
    	log.info("imItem.getCategory18() = " + imItem.getCategory18());
    	log.info("imItem.getCategory19() = " + imItem.getCategory19());
    	log.info("imItem.getCategory20() = " + imItem.getCategory20());
    	log.info("imItem.getCategory21() = " + imItem.getCategory21());
    	log.info("imItem.getCategory22() = " + imItem.getCategory22());
    	log.info("imItem.getDescription() = " + imItem.getDescription());
    	log.info("imItem.getEnable() = " + imItem.getEnable());
    	log.info("imItem.getExpiryDate() = " + imItem.getExpiryDate());
    	log.info("imItem.getForeignListPrice() = " + imItem.getForeignListPrice());
    	log.info("imItem.getIsComposeItem() = " + imItem.getIsComposeItem());
    	log.info("imItem.getIsServiceItem() = " + imItem.getIsServiceItem());
    	log.info("imItem.getItemCName() = " + imItem.getItemCName());
    	log.info("imItem.getItemCode() = " + imItem.getItemCode());
    	log.info("imItem.getItemEName() = " + imItem.getItemEName());
    	log.info("imItem.getItemLevel() = " + imItem.getItemLevel());
    	log.info("imItem.getLastUpdateDate() = " + imItem.getLastUpdateDate());
    	log.info("imItem.getLastUpdatedBy() = " + imItem.getLastUpdatedBy());
    	log.info("imItem.getLotControl() = " + imItem.getLotControl());
    	log.info("imItem.getPurchaseRatio() = " + imItem.getPurchaseRatio());
    	log.info("imItem.getPurchaseUnit() = " + imItem.getPurchaseUnit());
    	log.info("imItem.getReleaseDate() = " + imItem.getReleaseDate());
    	log.info("imItem.getReserve1() = " + imItem.getReserve1());
    	log.info("imItem.getReserve2() = " + imItem.getReserve2());
    	log.info("imItem.getReserve3() = " + imItem.getReserve3());
    	log.info("imItem.getReserve4() = " + imItem.getReserve4());
    	log.info("imItem.getReserve5() = " + imItem.getReserve5());
    	log.info("imItem.getSalesUnit() = " + imItem.getSalesUnit());
    	log.info("imItem.getSpecHeight() = " + imItem.getSpecHeight());
    	log.info("imItem.getSpecLength() = " + imItem.getSpecLength());
    	log.info("imItem.getSpecWeight() = " + imItem.getSpecWeight());
    	log.info("imItem.getSpecWidth() = " + imItem.getSpecWidth());
    	log.info("imItem.getStandardPurchaseCost() = " + imItem.getStandardPurchaseCost());
    	log.info("imItem.getSupplierItemCode() = " + imItem.getSupplierItemCode());
    	log.info("imItem.getSupplierQuotationPrice() = " + imItem.getSupplierQuotationPrice());
    	log.info("imItem.getEStore() = " + imItem.getEStore());
    	log.info("imItem.getEStoreReserve1() = " + imItem.getEStoreReserve1());
    	log.info("imItem.getDeclRatio() = " + imItem.getDeclRatio());
    	log.info("imItem.getMinRatio() = " + imItem.getMinRatio());
    	log.info("imItem.getBudgetType() = " + imItem.getBudgetType());
    	log.info("imItem.getReserve1() = " + imItem.getReserve1());
    	log.info("imItem.getReserve2() = " + imItem.getReserve2());
    	log.info("imItem.getReserve3() = " + imItem.getReserve3());
    	log.info("imItem.getReserve4() = " + imItem.getReserve4());
    	log.info("imItem.getPayOline() = " + imItem.getPayOline());

    	if(StringUtils.hasText(imItem.getEStore())){	//網購註記
    	    if(SPACE.equalsIgnoreCase(imItem.getEStore())){
    		oldItem.setEStore("");
    	    }else{
    		oldItem.setEStore(imItem.getEStore());
    	    }
    	}
    	 // 電子支付
    	oldItem.setPayOline(imItem.getPayOline());
    	
    	if(StringUtils.hasText(imItem.getEStoreReserve1())){	//網購安全庫存
    	    if(SPACE.equalsIgnoreCase(imItem.getEStoreReserve1())){
    		oldItem.setEStoreReserve1("");
    	    }else{
    		oldItem.setEStoreReserve1(imItem.getEStoreReserve1());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getBudgetType())){	//預算類別
    	    if(SPACE.equalsIgnoreCase(imItem.getBudgetType())){
    		oldItem.setBudgetType("");
    	    }else{
    		oldItem.setBudgetType(imItem.getBudgetType());
    	    }
    	}
    	if(imItem.getDeclRatio() != null && 0 != imItem.getDeclRatio() ){	// 報關數量比例
    	    oldItem.setDeclRatio(imItem.getDeclRatio());
    	}
    	if(imItem.getMinRatio() != null && 0 != imItem.getMinRatio() ){	// 最小單位比
    	    oldItem.setMinRatio(imItem.getMinRatio());
    	}
	


	if(imItem.getBoxCapacity() != null && 0 != imItem.getBoxCapacity() ){	// 每箱數量
    	    oldItem.setBoxCapacity(imItem.getBoxCapacity());
    	}
	
//    	oldItem.setBrandCode(imItem.getBrandCode());	// 品牌
    	
    	if(StringUtils.hasText(imItem.getCategory04())){	// 尺寸
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory04())){
    		oldItem.setCategory04("");
    	    }else{
    		oldItem.setCategory04(imItem.getCategory04());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory05())){	// 年份
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory05())){
    		oldItem.setCategory05("");
    	    }else{
    		oldItem.setCategory05(imItem.getCategory05());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory06())){	// 季別
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory06())){
    		oldItem.setCategory06("");
    	    }else{
    		oldItem.setCategory06(imItem.getCategory06());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory07())){	// 性別
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory07())){
    		oldItem.setCategory07("");
    	    }else{
    		oldItem.setCategory07(imItem.getCategory07());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory08())){	// 材質    
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory08())){
    		oldItem.setCategory08("");
    	    }else{
    		oldItem.setCategory08(imItem.getCategory08());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory09())){	// 款式
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory09())){
    		oldItem.setCategory09("");
    	    }else{
    		oldItem.setCategory09(imItem.getCategory09());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory10())){	// 顏色
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory10())){
    		oldItem.setCategory10("");
    	    }else{
    		oldItem.setCategory10(imItem.getCategory10());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory11())){	// 款式編號
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory11())){
    		oldItem.setCategory11("");
    	    }else{
    		oldItem.setCategory11(imItem.getCategory11());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory12())){	// 屬性
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory12())){
    		oldItem.setCategory12("");
    	    }else{
    		oldItem.setCategory12(imItem.getCategory12());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory13())){	// 系列
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory13())){
    		oldItem.setCategory13("");
    	    }else{
    		oldItem.setCategory13(imItem.getCategory13());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory14())){	// 產地    
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory14())){
    		oldItem.setCategory14("");
    	    }else{
    		oldItem.setCategory14(imItem.getCategory14());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory15())){	// 功能    
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory15())){
    		oldItem.setCategory15("");
    	    }else{
    		oldItem.setCategory15(imItem.getCategory15());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory16())){	// 樣本編號
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory16())){
    		oldItem.setCategory16("");
    	    }else{
    		oldItem.setCategory16(imItem.getCategory16());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getCategory18())){	// 其他1   
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory18())){
    		oldItem.setCategory18("");  
    	    }else{ 
    		oldItem.setCategory18(imItem.getCategory18());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory19())){	// 其他2   
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory19())){
    		oldItem.setCategory19("");
    	    }else{
    		oldItem.setCategory19(imItem.getCategory19());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getCategory20())){	//帳務類別
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory20())){
    		oldItem.setCategory20("");
    	    }else{
    		oldItem.setCategory20(imItem.getCategory20());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getCategory21())){	//材質2
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory21())){
    		oldItem.setCategory21("");
    	    }else{
    		oldItem.setCategory21(imItem.getCategory21());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getCategory22())){	//材質1
    	    if(SPACE.equalsIgnoreCase(imItem.getCategory22())){
    		oldItem.setCategory22("");
    	    }else{
    		oldItem.setCategory22(imItem.getCategory22());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getDescription())){	// 說明
    	    if(SPACE.equalsIgnoreCase(imItem.getDescription())){
    		oldItem.setDescription("");
    	    }else{
    		oldItem.setDescription(imItem.getDescription());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getEnable())){	// 商品啟用
    	    oldItem.setEnable(imItem.getEnable());
    	}
    	if(imItem.getExpiryDate() != null){	// 下市日期
    	    oldItem.setExpiryDate(imItem.getExpiryDate());
    	}
    	if(imItem.getForeignListPrice() != null && imItem.getForeignListPrice() != 0D ){	// 原幣零售價
    	    oldItem.setForeignListPrice(imItem.getForeignListPrice());
    	}
    	
    	if(StringUtils.hasText(imItem.getIsComposeItem())){	// 是否為組合商品
    	    oldItem.setIsComposeItem(imItem.getIsComposeItem()); 
    	}
    	if(StringUtils.hasText(imItem.getIsServiceItem())){	// 是否為服務性商品
    	    oldItem.setIsServiceItem(imItem.getIsServiceItem());
    	}
    	
    	if(StringUtils.hasText(imItem.getItemCName())){		// 中文名稱
    	    if(SPACE.equalsIgnoreCase(imItem.getItemCName())){
    		oldItem.setItemCName("");
    	    }else{
    		oldItem.setItemCName(imItem.getItemCName());
    	    }
    	}
    	oldItem.setItemCode(imItem.getItemCode());		// 品號
    	
    	if(StringUtils.hasText(imItem.getItemEName())){
    	    if(SPACE.equalsIgnoreCase(imItem.getItemEName())){	// 英文名稱
    		oldItem.setItemEName("");
    	    }else{
    		oldItem.setItemEName(imItem.getItemEName());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getItemLevel())){		// 商品等級
    	    if(SPACE.equalsIgnoreCase(imItem.getItemLevel())){
    		oldItem.setItemLevel("");
    	    }else{
    		oldItem.setItemLevel(imItem.getItemLevel());
    	    }
    	}
    	oldItem.setLastUpdateDate(imItem.getLastUpdateDate());
    	oldItem.setLastUpdatedBy(imItem.getLastUpdatedBy());
    	
    	if(StringUtils.hasText(imItem.getLotControl())){	// 是否批號管理
    	    oldItem.setLotControl(imItem.getLotControl());
    	}
    	
    	if(imItem.getPurchaseRatio() != null && imItem.getPurchaseRatio() != 0D ){			// 採購預算分配比率
    	    oldItem.setPurchaseRatio(imItem.getPurchaseRatio());
    	}
    	if(StringUtils.hasText(imItem.getPurchaseUnit())){	// 進貨單位
    	    oldItem.setPurchaseUnit(imItem.getPurchaseUnit());
    	}
    	if(imItem.getReleaseDate() != null){			// 上市日期
    	    oldItem.setReleaseDate(imItem.getReleaseDate());
    	}

    	if(StringUtils.hasText(imItem.getReserve1())){
    	    if(SPACE.equalsIgnoreCase(imItem.getReserve1())){
    		oldItem.setReserve1("");
    	    }else{
    		oldItem.setReserve1(imItem.getReserve1());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getReserve2())){
    	    if(SPACE.equalsIgnoreCase(imItem.getReserve2())){
    		oldItem.setReserve2("");
    	    }else{
    		oldItem.setReserve2(imItem.getReserve2());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getReserve3())){
    	    if(SPACE.equalsIgnoreCase(imItem.getReserve3())){
    		oldItem.setReserve3("");
    	    }else{
    		oldItem.setReserve3(imItem.getReserve3());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getReserve4())){
    	    if(SPACE.equalsIgnoreCase(imItem.getReserve4())){
    		oldItem.setReserve4("");
    	    }else{
    		oldItem.setReserve4(imItem.getReserve4());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getReserve5())){
    	    if(SPACE.equalsIgnoreCase(imItem.getReserve5())){
    		oldItem.setReserve5("");
    	    }else{
    		oldItem.setReserve5(imItem.getReserve5());
    	    }
    	}
//    	oldItem.setSalesRatio(imItem.getSalesRatio());
    	
    	if(StringUtils.hasText(imItem.getSalesUnit())){		// 交易單位
    	    oldItem.setSalesUnit(imItem.getSalesUnit());
    	}
    	
    	if(StringUtils.hasText(imItem.getSpecHeight())){	// 高
    	    if(SPACE.equalsIgnoreCase(imItem.getSpecHeight())){
    		oldItem.setSpecHeight("");
    	    }else{	
    		oldItem.setSpecHeight(imItem.getSpecHeight());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getSpecLength())){	// 長
    	    if(SPACE.equalsIgnoreCase(imItem.getSpecLength())){
    		oldItem.setSpecLength("");
    	    }else{
    		oldItem.setSpecLength(imItem.getSpecLength());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getSpecWeight())){	// 寬
    	    if(SPACE.equalsIgnoreCase(imItem.getSpecWeight())){
    		oldItem.setSpecWeight("");
    	    }else{
    		oldItem.setSpecWeight(imItem.getSpecWeight());
    	    }
    	}
    	if(StringUtils.hasText(imItem.getSpecWidth())){		// 重量
    	    if(SPACE.equalsIgnoreCase(imItem.getSpecWidth())){
    		oldItem.setSpecWidth("");
    	    }else{
    		oldItem.setSpecWidth(imItem.getSpecWidth());
    	    }
    	}
    	
    	if(StringUtils.hasText(imItem.getSupplierItemCode())){	// 原廠編號
    	    if(SPACE.equalsIgnoreCase(imItem.getSupplierItemCode())){
    		oldItem.setSupplierItemCode("");
    	    }else{
    		oldItem.setSupplierItemCode(imItem.getSupplierItemCode());
    	    }
    	}
    	if(imItem.getSupplierQuotationPrice() != null && imItem.getSupplierQuotationPrice() != 0D){	// 原廠報價
    	    oldItem.setSupplierQuotationPrice(imItem.getSupplierQuotationPrice());
    	}
    	oldItem = setDefaultValues(oldItem);
	    
    	if( imItem.getReserve5().indexOf("T2") > -1 ){
//  	    oldItem.setAccountCode(imItem.getAccountCode());
    	     
    	    if(StringUtils.hasText(imItem.getReleaseString())){ // 上市日期字串 
    		if(SPACE.equalsIgnoreCase(imItem.getReleaseString())){
    		    oldItem.setReleaseString("");
    		}else{
    		    oldItem.setReleaseString(imItem.getReleaseString());
    		}
    	    }
    	    if(StringUtils.hasText(imItem.getExpiryString())){ // 下市日期字串
    		if(SPACE.equalsIgnoreCase(imItem.getExpiryString())){
    		    oldItem.setExpiryString("");
    		}else{
    		    oldItem.setExpiryString(imItem.getExpiryString());
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getCategory01())){	// 大類
    		oldItem.setCategory01(imItem.getCategory01());
    	    }
    	    if(StringUtils.hasText(imItem.getCategory02())){	// 中類
    		oldItem.setCategory02(imItem.getCategory02());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getCategory03())){	// 小類
    		if(SPACE.equalsIgnoreCase(imItem.getCategory03())){
    		    oldItem.setCategory03("");
    		}else{
    		    oldItem.setCategory03(imItem.getCategory03());
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getCategory17())){	// 製造商  
    		oldItem.setCategory17(imItem.getCategory17());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getVipDiscount())){ 	// vip折扣
    		oldItem.setVipDiscount(imItem.getVipDiscount());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getCategoryType())){  // 業種
    		oldItem.setCategoryType(imItem.getCategoryType());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getIsConsignSale())){ // 寄賣品(Y/N)
    		oldItem.setIsConsignSale(imItem.getIsConsignSale());
    	    }
//  	    oldItem.setCustomsItemCode(imItem.getCustomsItemCode());
    	    
    	    if(StringUtils.hasText(imItem.getItemType())){ 	// 商品類別 N)正貨 G)贈品 S)試用品 E)陳列品
    		if(SPACE.equalsIgnoreCase(imItem.getItemType())){
    		    oldItem.setItemType("");
    		}else{
    		    oldItem.setItemType(imItem.getItemType());
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getItemBrand())){ 	// 商品品牌
    		oldItem.setItemBrand(imItem.getItemBrand());
    	    }
    	    
//  	    oldItem.setBonusType(imItem.getBonusType());
    	    if(imItem.getMargen() != null && imItem.getMargen() != 0D ){			// 毛利率
    		oldItem.setMargen(imItem.getMargen());
    	    }
    	    
    	    if(imItem.getMaxPurchaseQuantity() != null && imItem.getMaxPurchaseQuantity() != 0D){	// 最高採購量
    		oldItem.setMaxPurchaseQuantity(imItem.getMaxPurchaseQuantity());
    	    }
    	    
    	    if(imItem.getMinPurchaseQuantity() != null && imItem.getMinPurchaseQuantity() != 0D){	// 最低採購量
    		oldItem.setMinPurchaseQuantity(imItem.getMinPurchaseQuantity());
    	    }
//  	    oldItem.setBudgetType(imItem.getBudgetType());
    	    
    	    if(StringUtils.hasText(imItem.getIsTax())){ 	//  (F)保稅/(P)完稅
    		oldItem.setIsTax(imItem.getIsTax());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getPurchaseCurrencyCode())){ 	// 採購幣別
    		oldItem.setPurchaseCurrencyCode(imItem.getPurchaseCurrencyCode());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getItemCategory())){ 		// 業種子類
    		oldItem.setItemCategory(imItem.getItemCategory());
    	    }
//    	    oldItem.setPurchaseAmount(imItem.getPurchaseAmount());

//  	    oldItem.setTaxRelativeItemCode(imItem.getTaxRelativeItemCode());
    	    
    	    if(StringUtils.hasText(imItem.getColorCode())){		// 色碼
    		if(SPACE.equalsIgnoreCase(imItem.getColorCode())){
    		    oldItem.setColorCode("");
    		}else{
    		    oldItem.setColorCode(imItem.getColorCode());
    		}
    	    }
    	    if(StringUtils.hasText(imItem.getMaterial())){		// 材質說明
    		if(SPACE.equalsIgnoreCase(imItem.getMaterial())){
    		    oldItem.setMaterial("");
    		}else{
    		    oldItem.setMaterial(imItem.getMaterial());
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getValidityDay())){		// 有效天數
    		if(SPACE.equalsIgnoreCase(imItem.getValidityDay())){
    		    oldItem.setValidityDay("");
    		}else{
    		    oldItem.setValidityDay(imItem.getValidityDay());
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getForeignCategory())){	// 國外類別
    		if(SPACE.equalsIgnoreCase(imItem.getForeignCategory())){
    		    oldItem.setForeignCategory("");
    		}else{
    		    oldItem.setForeignCategory(imItem.getForeignCategory());
    		}
    	    }
    	    
    	    if(imItem.getReplenishCoefficient() != null && imItem.getReplenishCoefficient() != 0D){	        // 補貨係數
    		oldItem.setReplenishCoefficient(imItem.getReplenishCoefficient());
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getAllowMinusStock())){	// 是否允許負庫存
    		oldItem.setAllowMinusStock(imItem.getAllowMinusStock());
    	    }
    	    
//    	    log.info(" DeclRatio = " + imItem.getDeclRatio());
//    	    if( null != imItem.getDeclRatio() && 0d != imItem.getDeclRatio() ){	// 銷售單位/報關單號換算
//    		oldItem.setDeclRatio(imItem.getDeclRatio());
//    	    }
//	    
//    	    log.info(" MinRatio = " + imItem.getMinRatio()); 
//    	    if( null != imItem.getMinRatio() && 0d != imItem.getMinRatio() ){	// 最小單位/銷貨單位的比例換算 
//    		oldItem.setMinRatio(imItem.getMinRatio());
//    	    }
    	    if(imItem.getAlcolhoPercent() != null && imItem.getAlcolhoPercent() != 0D) { // 酒精濃度
    	    	log.info("---------------設定前--------------");
    	    	log.info("------"+imItem.getAlcolhoPercent());
				oldItem.setAlcolhoPercent(imItem.getAlcolhoPercent());
				log.info("---------------設定後--------------");
			}
	    
    	}else{
    	    // 百貨
    	    
    	    // 計價後成本
    	    boolean isSetPrice = false; // 未訂過價
    	    List<ImItemPrice> imItemPrices = oldItem.getImItemPrices();
    	    for (ImItemPrice imItemPrice : imItemPrices) {
    		String enable = imItemPrice.getEnable();
    		if("Y".equalsIgnoreCase(enable)){
    		    isSetPrice = true; // 已訂過價
    		    break;
    		}
    	    }
    	    if(!isSetPrice){ // 未訂過價可修改
    		if(imItem.getStandardPurchaseCost() != null && imItem.getStandardPurchaseCost() != 0D ){		// 標準進價
    		    oldItem.setStandardPurchaseCost(imItem.getStandardPurchaseCost());
    		}
    	    }else{
    		if(0D != imItem.getStandardPurchaseCost()){
    		    if(imItem.getStandardPurchaseCost() != oldItem.getStandardPurchaseCost()){
    			throw new Exception("品號:" + oldItem.getItemCode()
    				+ "已訂價不得修改計價後成本:"+imItem.getStandardPurchaseCost());
    		    }
    		}
    	    }
    	    
    	    if(StringUtils.hasText(imItem.getCategory01())){	// 大類 // 中類不存在, 才可將大類清空
    		if(SPACE.equalsIgnoreCase(imItem.getCategory01())){
    		    oldItem.setCategory01("");
    		}else{ // 中類存在
    		    oldItem.setCategory01(imItem.getCategory01());
    		}
    	    }
    	       	    
 	    if(StringUtils.hasText(imItem.getCategory02())){	// 中類
 		if(SPACE.equalsIgnoreCase(imItem.getCategory02())){
 		    oldItem.setCategory02("");
 		}else{ // 大類有值才會能寫入中類
 		    oldItem.setCategory02(imItem.getCategory02());
 		}
 	    }
    	    
 	    if(StringUtils.hasText(imItem.getCategory03())){	// 小類
 		if(SPACE.equalsIgnoreCase(imItem.getCategory03())){
 		    oldItem.setCategory03("");
 		}else{ // 大類和中類都存在才能存小類
 		    oldItem.setCategory03(imItem.getCategory03());
 		}
 	    }
 	    
    	    if(StringUtils.hasText(imItem.getCategory17())){	// 製造商  
    		if(SPACE.equalsIgnoreCase(imItem.getCategory17())){
    		    oldItem.setCategory17("");
    		}else{
    		    oldItem.setCategory17(imItem.getCategory17());
    		}
    		
    	    }
    	}
    }
}
