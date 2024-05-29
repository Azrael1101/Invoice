package tw.com.tm.erp.importdb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * 商品匯入
 * 
 * @author T02049
 * 
 */

public class ImItemImportData implements ImportDataAbs {
    private static final Log log = LogFactory.getLog(ImItemImportData.class);

    public ImportInfo initial(HashMap uiProperties) {
	log.info("ImItemImportData.initial");
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
	return imInfo;//>>>>>>>>>>>>>>>>ImportDBService.loadXlsData
    }

    private ImportInfo doT1Initial(ImportInfo imInfo){

	imInfo.addFieldName("itemCode");
	imInfo.addFieldName("brandCode");
	imInfo.addFieldName("itemCName");
	imInfo.addFieldName("itemEName");
	imInfo.addFieldName("itemLevel");
	imInfo.addFieldName("lotControl");
	imInfo.addFieldName("supplierItemCode");
	imInfo.addFieldName("releaseDate");
	imInfo.addFieldName("expiryDate");
	imInfo.addFieldName("foreignListPrice");
	imInfo.addFieldName("supplierQuotationPrice");
	imInfo.addFieldName("standardPurchaseCost");
	imInfo.addFieldName("isComposeItem");
	imInfo.addFieldName("specLength");
	imInfo.addFieldName("specWidth");
	imInfo.addFieldName("specHeight");
	imInfo.addFieldName("specWeight");
	imInfo.addFieldName("salesRatio");
	imInfo.addFieldName("salesUnit");
	imInfo.addFieldName("purchaseUnit");
	imInfo.addFieldName("description");
	imInfo.addFieldName("category01");
	imInfo.addFieldName("category02");
	imInfo.addFieldName("category03");
	imInfo.addFieldName("category04");
	imInfo.addFieldName("category05");
	imInfo.addFieldName("category06");
	imInfo.addFieldName("category07");
	imInfo.addFieldName("category08");
	imInfo.addFieldName("category09");
	imInfo.addFieldName("category10");
	imInfo.addFieldName("category11");
	imInfo.addFieldName("category12");
	imInfo.addFieldName("category13");
	imInfo.addFieldName("category14");
	imInfo.addFieldName("category15");
	imInfo.addFieldName("category16");
	imInfo.addFieldName("category17");
	imInfo.addFieldName("category18");
	imInfo.addFieldName("category19");
	imInfo.addFieldName("category20");
	imInfo.addFieldName("category22");
	imInfo.addFieldName("category21");
	imInfo.addFieldName("enable");

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
	imInfo.addFieldName("alcolhoPercent"); // 酒精濃度
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
//	imInfo.addFieldName("reserve5"); // 備註5
//	imInfo.addFieldName("declRatio"); // 銷售單位/報關單號換算
//	imInfo.addFieldName("minRatio"); // 最小單位/銷貨單位的比例換算


//	imInfo.addFieldName("standardPurchaseCost"); // 計價後成本
//	imInfo.addFieldName("accountCode"); // T2 會計科目代號
//	imInfo.addFieldName("customsItemCode"); // T2 海關品號
//	imInfo.addFieldName("bonusType"); // T2 獎金類別
//	imInfo.addFieldName("budgetType"); // T2 預算類別
//	imInfo.addFieldName("taxRelativeItemCode"); // T2 對應稅別品號

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
//	imInfo.setFieldType("reserve5", "java.lang.String"); // 備註5
	log.info("================================================================================== alcolhoPercent");
//	imInfo.setFieldType("declRatio", "java.lang.Double");
//	imInfo.setFieldType("minRatio", "java.lang.Double");

//	imInfo.setFieldType("standardPurchaseCost", "java.lang.Double");
//	imInfo.setFieldType("accountCode", "java.lang.String"); // T2 會計科目代號
//	imInfo.setFieldType("customsItemCode", "java.lang.String"); // T2 海關品號
//	imInfo.setFieldType("bonusType", "java.lang.String"); // T2 獎金類別
//	imInfo.setFieldType("budgetType", "java.lang.String"); // T2 預算類別
//	imInfo.setFieldType("taxRelativeItemCode", "java.lang.String"); // T2 對應稅別品號

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

    /*
     * private ImportInfo getImItemCompose() { ImportInfo imInfo2 = new
     * ImportInfo(); imInfo2.setKeyIndex(0); imInfo2.setKeyValue("T2");
     * 
     * imInfo2.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImItemCompose.class.getName());
     * 
     * imInfo2.addFieldName("key"); imInfo2.addFieldName("composeId");
     * imInfo2.addFieldName("composeItemCode");
     * 
     * imInfo2.setFieldType("key", "java.lang.String");
     * imInfo2.setFieldType("composeId", "java.lang.Long");
     * imInfo2.setFieldType("composeItemCode", "java.lang.String"); return
     * imInfo2; }
     */

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
	log.info("ImItemImportData.updateDB");
	ImItemService imItemService = (ImItemService) SpringUtils.getApplicationContext().getBean("imItemService");

	ImItemPriceService imItemPriceService = (ImItemPriceService) SpringUtils.getApplicationContext().getBean("imItemPriceService");
	BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
	
	StringBuffer result = new StringBuffer();
	String enableExist = "N";	// 是否已存在啟用價格 2009.10.27 arthur 

	String yyyyMMddHHMMSS = DateUtils.format( new Date(), "yyyyMMddHHmmss"); 
	
	String tmpBrandCode = null;
	String tmpImportLotNo = null;

	int allCount = 0;
	int correctCount = 0 ;
	int failCount = 0 ;
	for (int index = 0; index < entityBeans.size(); index++) {
	    enableExist = "N";
	    ImItem imItem = (ImItem) entityBeans.get(index); // 匯入的商品

	    log.info("imItem.getSalesRatio() 匯入過來的 = " + imItem.getSalesRatio());

	    String brandCode = imItem.getBrandCode();

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
			// 設定非T2的預設值
			imItem = setDefaultValues(imItem);
			log.info( imItem.getItemCode() + "-" + imItem.getBrandCode() +"-" +imItem.getReserve5() );

			log.info( "imItem.getItemBrand() = " + imItem.getItemBrand() );
			log.info("imItem.getReserve5() = " + imItem.getReserve5());
			// 檢核商品主檔
			checkImItem(imItem, index, null);

			List<ImItemPrice> prices = imItem.getImItemPrices();
			for (int index1 = 0; index1 < prices.size(); index1++) {
			    ImItemPrice price = prices.get(index1);
			    price.setEnable("N");
			    price.setItemCode(imItem.getItemCode());
			    price.setBrandCode(imItem.getBrandCode());

			    if(imItem.getBrandCode().indexOf("T2") > -1){
				// 價格類別
				price.setTypeCode("1");
				// 含稅價
				price.setIsTax(imItem.getIsTax());
				// 稅別1(免稅)或2(零稅)或3(應稅)，請檢查!
				price.setTaxCode("3");
				
				// 若T2A將直接定價
//				BuCommonPhraseLine itemImportConfig = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine","", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and attribute1 = ? and enable = ? ", new Object[]{"ItemImportConfig",tmpBrandCode, "Y", "Y"}, "order by indexNo" );
//				if(null != itemImportConfig){
//				    if(null == price.getBeginDate() && "N".equalsIgnoreCase(price.getEnable())){
//					price.setBeginDate(new Date());
//					price.setEnable("Y");
//					price.setUnitPrice(NumberUtils.getDouble(itemImportConfig.getParameter1())); // 取得直接定價的價錢
//				    }
//				}
				
			    }
			    // 檢核商品價格檔
			    doValidateImItemPrice(price, imItem);
			    
			}

			log.info( "doValidateImItemPrice after " );
			// 設定匯入批號
			imItem.setImportLotNo(yyyyMMddHHMMSS);
			
			imItemService.save(imItem);

			log.info( "imItemDAO.save after " );
			result.append("新增商品" + imItem.getItemCode() + "匯入 <br/>");
			correctCount++ ;
		    } catch (Exception ex) {
			failCount++;
			ex.printStackTrace();
			log.error("匯入商品資料有問題 品號: " + imItem.getItemCode()+ ex.getMessage() );
			result.append("匯入商品資料有問題 品號: " + imItem.getItemCode() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
		    }

		} else { // 資料庫已存在此品牌的商品
		    List<ImItemPrice> itemPrices = imItem.getImItemPrices(); //  匯入商品的價格明細
		    if (null != itemPrices && itemPrices.size() > 0) {
			log.info("ImItemImportData.updateDB.update " + imItem.getItemCode());
			List<ImItemPrice> oldPrices = oldItem.getImItemPrices();	// 已存在資料庫商品的價格明細
			ImItemPrice itemPrice = itemPrices.get(0);
			itemPrice.setEnable("N");
			itemPrice.setItemCode(imItem.getItemCode());
			itemPrice.setBrandCode(imItem.getBrandCode());
			
			ImItemPrice searchImItemPrice = null;
			if(imItem.getBrandCode().indexOf("T2") <= -1){
			    // 百貨
			    searchImItemPrice = imItemPriceService.findByItemTypeEnableDate(imItem.getBrandCode(), itemPrice.getItemCode(), itemPrice.getTypeCode());
			}else{
			    // 免稅
			    searchImItemPrice = imItemPriceService.findByItemTypeEnableDate(imItem.getBrandCode(), itemPrice.getItemCode(), "1" );
			}
			
			if ((null != searchImItemPrice) && ("N".equalsIgnoreCase(searchImItemPrice.getEnable()))) {
			    log.info("ImItemImportData.updateDB.searchImItemPrice.getEnable " + searchImItemPrice.getEnable() + "/"+ searchImItemPrice.getPriceId());
			    for (ImItemPrice oldPrice : oldPrices) {
				Long pid = oldPrice.getPriceId();								
				if (searchImItemPrice.getPriceId().equals(pid) && 
					null==oldPrice.getBeginDate() && 		// 無啟用日
					"N".equals(oldPrice.getEnable())) {		// 尚未啟用
				    try{
					if(brandCode.indexOf("T2") <= -1){
					    // 百貨
					    oldPrice.setTypeCode( itemPrice.getTypeCode());
					    oldPrice.setUnitPrice(itemPrice.getUnitPrice());
					    oldPrice.setIsTax(    itemPrice.getIsTax());
					    oldPrice.setTaxCode(  itemPrice.getTaxCode());
					    oldPrice.setBrandCode(imItem.getBrandCode());
					}else{
					    // 免稅
					    oldPrice.setUnitPrice(itemPrice.getUnitPrice());
					}

				    } catch (Exception ex) {
					failCount++;
					ex.printStackTrace();
					log.error("匯入商品資料有問題 品號: " + imItem.getItemCode() + ex.getMessage());
					result.append("匯入商品資料有問題 品號: " + imItem.getItemCode() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
				    }
				}
			    }

			} else {
			    for (ImItemPrice oldPrice : oldPrices) {		
				if ("Y".equals(oldPrice.getEnable())) // 是否已存在啟用價格 2009.10.27 arthur 
				    enableExist = "Y";
			    }
			    log.info("ImItemImportData.updateDB.searchImItemPrice.newPrice " + itemPrice.getItemCode()+"/"+itemPrice.getTypeCode());
			    if ("N".equals( enableExist ))		// 未曾啟用新增一筆  2009.10.27 arthur
				oldPrices.add(itemPrice);
			}
		    }

		    // 複製資料
		    copyImportItemToDBItem(oldItem, imItem);

		    try {
			if ("N".equals( enableExist )){ // 未定價
			    checkImItem(oldItem, index, enableExist);
			    doValidateImItemPrice(oldItem.getImItemPrices().get(0), oldItem);
//			    imItemService.save(oldItem, "UPDATE");
			}else{ // imItemPrice enable = Y 曾經啟用價格 已訂價
			    if(brandCode.indexOf("T2") <= -1){
				checkImItem(oldItem, index, enableExist);
			    }else{
				throw new Exception("品號：" + imItem.getItemCode() + "已經啟用價格，不能更新商品主檔" );
			    }
//			    imItemService.save(oldItem, "UPDATEITEM");	// 僅更新 imItem, 不檢核 imItemPrice
			}
			// 設定匯入批號
			oldItem.setImportLotNo(yyyyMMddHHMMSS);
			
			imItemService.update(oldItem);
			correctCount++;
		    } catch (Exception ex) {
			failCount++;
			ex.printStackTrace();
			log.error("匯入商品資料有問題 品號: " + imItem.getItemCode());
			result.append("匯入商品資料有問題 品號: " + imItem.getItemCode() + " 錯誤原因 : " + ex.getMessage() + "<br/>");
		    }
		    // BeanUtils.copyProperties(imItem,oldItem);
		    // List<ImItemPrice> prices = oldItem.getImItemPrices();
		    //result.append("修改商品" + imItem.getItemCode() + "匯入<br/>");
		}
	    }else{


	    }
	    if(StringUtils.hasText(imItem.getItemCode())){
		allCount++;
	    }

	    if(null == tmpImportLotNo){
		tmpImportLotNo = imItem.getImportLotNo();
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
	if( imItem.getBrandCode().indexOf("T2") <= -1 ){		// 非  T2  2009.11.02 arthur 
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
	public ImItem checkImItem(ImItem imItem, int index, String enableExist) throws Exception {

		BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
		BuBrandDAO buBrandDAO = (BuBrandDAO) SpringUtils.getApplicationContext().getBean("buBrandDAO");
		ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean(
				"imItemCategoryDAO");
		ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean(
				"imItemEancodeDAO");

		log.info("imItem.getReserve5() = " + imItem.getReserve5());
		// 依品牌匯入
		String brandCode = imItem.getBrandCode();
		String itemCode = imItem.getItemCode();

		// 品牌
		if (!StringUtils.hasText(brandCode)) {
			throw new NoSuchDataException("品號:" + itemCode + "未輸入品牌");
		} else {
			BuBrand buBrand = buBrandDAO.findById(brandCode);
			if (null == buBrand) {
				throw new NoSuchDataException("品號：" + itemCode + "依據品牌：" + brandCode + "查無其品牌代號！");
			} else {
				// 檢核登入人員與匯入所打的品牌是否一致
				if (!brandCode.equals(imItem.getReserve5())) {
					throw new Exception("作業人員登入品牌: " + imItem.getReserve5() + "與新增品號品牌: " + brandCode + " 不符合，請檢查!");
				} else {
					imItem.setReserve5(null);
				}
			}
		}

		// 商品主檔
		if (!StringUtils.hasText(itemCode)) {
			throw new NoSuchDataException("第" + (index + 1) + "筆品號未輸入");
		} else {
			log.info("brandCode = " + brandCode);
			log.info("itemCode = " + itemCode);
			log.info("itemCode.length() = " + itemCode.length());
			if (itemCode.length() > 13) {
				throw new NoSuchDataException("品號：" + itemCode + "必須小於13碼以下");
			}
		}

		// 品號
		log.info("Pattern.compile([^a-zA-Z\\d\\s]+) = " + Pattern.compile("[^a-zA-Z\\d\\s]+").matcher(itemCode).find());
		if (Pattern.compile("[^a-zA-Z\\d\\s]+").matcher(itemCode).find()) {
			throw new NoSuchDataException("品號：" + itemCode + "不可有特殊符號，請重新輸入!");
		} else if (StringUtils.containsWhitespace(itemCode)) {
			throw new NoSuchDataException("品號：" + itemCode + "含有空白，請檢查");
		}

		// 批號管理
		if (!(StringUtils.hasText(imItem.getLotControl()))) {
			if (brandCode.indexOf("T2") > -1) {
				imItem.setLotControl("N");
			} else {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入批號管理(LotControl)欄位，請檢查!");
			}
		} else {
			List<BuCommonPhraseLine> allLotControls = baseDAO.findByProperty("BuCommonPhraseLine",
					"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
							"LotControl", imItem.getLotControl(), "Y" });
			if (null == allLotControls) {
				throw new NoSuchDataException("品號:" + itemCode + " 批號管理(LotControl)欄位錯誤，非N/1/2");
			}
		}

		// 組合商品
		if (!StringUtils.hasText(imItem.getIsComposeItem())) {
			imItem.setIsComposeItem("N");
		} else {
			if (!"Y".equals(imItem.getIsComposeItem()) && !"N".equals(imItem.getIsComposeItem())) {
				throw new Exception("品號:" + itemCode + " 是否為組合商品欄位錯誤，非Y/N");
			}
		}

		// 服務性商品
		if (!StringUtils.hasText(imItem.getIsServiceItem())) {
			imItem.setIsServiceItem("N");
		} else {
			if (!"Y".equals(imItem.getIsServiceItem()) && !"N".equals(imItem.getIsServiceItem())) {
				throw new Exception("品號:" + itemCode + " 是否為服務性商品欄位錯誤，非Y/N");
			}
		}

		log.info("imItem.getSalesRatio() before= " + imItem.getSalesRatio());
		// 交易與進貨單位比
		if (null == imItem.getSalesRatio() || 0 == imItem.getSalesRatio()) {
			imItem.setSalesRatio(1L);
		} else {
			log.info("imItem.getSalesRatio().toString() = " + imItem.getSalesRatio().toString());
			if (!ValidateUtil.isNumber(imItem.getSalesRatio().toString())) {
				throw new NoSuchDataException("品號:" + itemCode + " 交易與進貨單位比:" + imItem.getSalesRatio() + "必須為數值");
			}
		}
		log.info("imItem.getSalesRatio() after= " + imItem.getSalesRatio());

		// 交易單位
		if (!StringUtils.hasText(imItem.getSalesUnit())) {
			throw new NoSuchDataException("品號:" + itemCode + "未輸入交易單位");
		} else {
			log.info("imItem.getSalesUnit() = " + imItem.getSalesUnit());
			List<BuCommonPhraseLine> allItemUnit = null;
			if (brandCode.indexOf("T2") > -1) {
				allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"T2ItemUnit", imItem.getSalesUnit(), "Y" });
			} else {
				allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"ItemUnit", imItem.getSalesUnit(), "Y" });
			}
			log.info("allItemUnit = " + allItemUnit);
			log.info("allItemUnit.size() = " + allItemUnit.size());
			if (null == allItemUnit || allItemUnit.size() == 0) {
				throw new NoSuchDataException("查品號:" + itemCode + "交易單位:" + imItem.getSalesUnit() + "不存在");
			}
		}

		// 進貨單位
		if (!StringUtils.hasText(imItem.getPurchaseUnit())) {
			throw new NoSuchDataException("品號:" + itemCode + "未輸入進貨單位");
		} else {
			List<BuCommonPhraseLine> allItemUnit = null;
			if (brandCode.indexOf("T2") > -1) {
				allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"T2ItemUnit", imItem.getPurchaseUnit(), "Y" });
			} else {
				allItemUnit = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"ItemUnit", imItem.getPurchaseUnit(), "Y" });
			}
			if (null == allItemUnit || allItemUnit.size() == 0) {
				throw new NoSuchDataException("查品號:" + itemCode + "進貨單位:" + imItem.getPurchaseUnit() + "不存在");
			}
		}

		// 預計採購比
		if (null == imItem.getPurchaseRatio()) {
			imItem.setPurchaseRatio(0L);
		} else {
			if (!ValidateUtil.isNumber(imItem.getPurchaseRatio().toString())) {
				throw new NoSuchDataException("品號:" + itemCode + "預計採購比:" + imItem.getPurchaseRatio() + "必須為數值");
			}
		}

		// 帳務類別
		if (!StringUtils.hasText(imItem.getCategory20())) {
			imItem.setCategory20("01");
		} else {
			List<BuCommonPhraseLine> allCategory20 = baseDAO.findByProperty("BuCommonPhraseLine",
					"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
							"ItemCategory20", imItem.getCategory20(), "Y" });
			if (null == allCategory20 || allCategory20.size() == 0) {
				throw new Exception("品號:" + itemCode + " 帳務類別欄位:" + imItem.getCategory20() + "不存在");
			}
		}

		// 每箱數量
		if (null == imItem.getBoxCapacity() || 0 == imItem.getBoxCapacity()) {
			imItem.setBoxCapacity(1L);
		} else {
			if (!ValidateUtil.isNumber(imItem.getBoxCapacity().toString())) {
				throw new NoSuchDataException("品號:" + itemCode + "每箱數量:" + imItem.getBoxCapacity() + "必須為數值");
			}
		}

		if (brandCode.indexOf("T2") > -1) {
			// 打品號,檢查不能等於B品號已啟用的國際碼
			ImItemEancode line = (ImItemEancode) imItemEancodeDAO.findOneEanCodeByProperty(brandCode, itemCode, itemCode);
			if (null != line) {
				throw new FormException("品號:" + itemCode + " 不能等於品號:" + line.getItemCode() + " 已啟用的國際碼:" + line.getEanCode());
			}

			// 檢核大類中類,商品品牌,業種,業種子類,稅別,原幣成本必填

			// 中文名稱/品名
			if (StringUtils.hasText(imItem.getItemCName())) {
				if (imItem.getItemCName().length() > 24) {
					throw new NoSuchDataException("品號：" + itemCode + "的品名必須小於24碼以下");
				}
			}

			// 英文名稱/其他品名
			if (StringUtils.hasText(imItem.getItemEName())) {
				if (imItem.getItemEName().length() > 200) {
					throw new NoSuchDataException("品號：" + itemCode + "的其他品名必須小於200碼以下");
				}
			}

			// 大類
			if (!StringUtils.hasText(imItem.getCategory01())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入大類");
			} else {
				ImItemCategory imItemCategory01 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY01, imItem.getCategory01(), "Y");
				if (null == imItemCategory01) {
					throw new NoSuchDataException("品號:" + itemCode + "大類:" + imItem.getCategory01() + "不存在");
				}
			}
			// 中類
			if (!StringUtils.hasText(imItem.getCategory02())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入中類");
			} else {
				ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), "Y");
				if (null == imItemCategory02) {
					throw new NoSuchDataException("品號:" + itemCode + "中類:" + imItem.getCategory02() + "不存在");
				} else {
					ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode,
							ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), imItem.getCategory01(), "Y");
					if (null == imItemCategory02s) {
						throw new NoSuchDataException("品號:" + itemCode + "大類" + imItem.getCategory01() + "不包含中類:"
								+ imItem.getCategory02());
					}
				}
			}

			// 小類
			if (StringUtils.hasText(imItem.getCategory03())) {
				ImItemCategory imItemCategory03 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), "Y");
				if (null == imItemCategory03) {
					throw new NoSuchDataException("品號:" + itemCode + "小類:" + imItem.getCategory03() + "不存在");
				} else {
					if (StringUtils.hasText(imItem.getCategory02())) {
						ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode,
								ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), imItem.getCategory02(), "Y");
						if (null == imItemCategory02s) {
							throw new NoSuchDataException("品號:" + itemCode + "中類" + imItem.getCategory02() + "不包含小類:"
									+ imItem.getCategory03());
						}
					}
				}
			}

			// 年份
			if (StringUtils.hasText(imItem.getCategory05())) {
				ImItemCategory category05 = imItemCategoryDAO.findByCategoryCode(brandCode, "CATEGORY05",
						imItem.getCategory05(), "Y");
				if (null == category05) {
					throw new NoSuchDataException("品號:" + itemCode + "年份:" + imItem.getCategory05() + "不存在");
				}
			}

			// 季別
			if (StringUtils.hasText(imItem.getCategory06())) {
				ImItemCategory category06 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY06,
						imItem.getCategory06(), "Y");
				if (null == category06) {
					throw new NoSuchDataException("品號:" + itemCode + "季別:" + imItem.getCategory06() + "不存在");
				}
			}

			// 性別
			if (StringUtils.hasText(imItem.getCategory07())) {
				ImItemCategory category07 = imItemCategoryDAO.findByCategoryCode(brandCode, ImItemCategoryDAO.CATEGORY07,
						imItem.getCategory07(), "Y");
				if (null == category07) {
					throw new NoSuchDataException("品號:" + itemCode + "性別:" + imItem.getCategory07() + "不存在");
				}
			}

			// 製造商/供應商
			if (!StringUtils.hasText(imItem.getCategory17())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入製造商/供應商");
			} else {
				List<BuSupplierWithAddressView> buSupplierWithAddressView = baseDAO.findByProperty(
						"BuSupplierWithAddressView", "and brandCode = ? and supplierCode = ? ", new Object[] { brandCode,
								imItem.getCategory17() });
				if (null == buSupplierWithAddressView || buSupplierWithAddressView.size() == 0) {
					throw new NoSuchDataException("品號:" + itemCode + "製造商/供應商:" + imItem.getCategory17() + "不存在");
				}
			}

			// vip折扣
			if (!StringUtils.hasText(imItem.getVipDiscount())) {
				throw new Exception("品號:" + itemCode + " 未輸入vip折扣");
			} else {
				List<BuCommonPhraseLine> allVipDiscount = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"VipDiscount", imItem.getVipDiscount(), "Y" });
				if (null == allVipDiscount || allVipDiscount.size() == 0) {
					throw new Exception("品號:" + itemCode + " vip折扣代碼:" + imItem.getVipDiscount() + "不存在");
				}
			}

			// 業種
			if (!StringUtils.hasText(imItem.getCategoryType())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入業種");
			} else {
				ImItemCategory imItemCategory00 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY00, imItem.getCategoryType(), "Y");
				if (null == imItemCategory00) {
					throw new NoSuchDataException("品號:" + itemCode + "業種:" + imItem.getCategoryType() + "不存在");
				}
			}

			// 寄賣品
			if (!StringUtils.hasText(imItem.getIsConsignSale())) {
				imItem.setIsConsignSale("N");
			} else {
				if (!"Y".equals(imItem.getIsConsignSale()) && !"N".equals(imItem.getIsConsignSale())) {
					throw new Exception("品號:" + itemCode + " 寄賣品欄位:" + imItem.getIsConsignSale() + "錯誤，非Y/N");
				}
			}

			// 海關品號
			if (!StringUtils.hasText(imItem.getCustomsItemCode())) {
				imItem.setCustomsItemCode(itemCode);
			}

			// 商品類別 NGSE
			if (!StringUtils.hasText(imItem.getItemType())) {
				imItem.setItemType("N");
			} else {
				List<BuCommonPhraseLine> allItemType = baseDAO.findByProperty("BuCommonPhraseLine",
						"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[] {
								"ItemType", imItem.getItemType(), "Y" });
				if (null == allItemType || allItemType.size() == 0) {
					throw new Exception("品號:" + itemCode + " 商品類別:" + imItem.getItemType() + "，不存在");
				}
			}

			// 商品品牌
			log.info("imItem.getItemBrand() = " + imItem.getItemBrand());
			if (!StringUtils.hasText(imItem.getItemBrand())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入商品品牌");
			} else {
				ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.ITEM_BRAND, imItem.getItemBrand(), "Y");
				if (imItemCategory == null) {
					throw new FormException("品號:" + itemCode + "的商品品牌" + imItem.getItemBrand() + "不存在");
				}
			}

			// 獎金類別 待anber

			// 標準毛利率
			if (null == imItem.getMargen()) {
				imItem.setMargen(0d);
			}

			// 最高採購量
			if (null == imItem.getMaxPurchaseQuantity()) {
				imItem.setMaxPurchaseQuantity(0d);
			}

			// 最低採購量
			if (null == imItem.getMinPurchaseQuantity()) {
				imItem.setMinPurchaseQuantity(0d);
			}

			// 稅別
			if (!StringUtils.hasText(imItem.getIsTax())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入稅別");
			} else {
				if (!"F".equals(imItem.getIsTax()) && !"P".equals(imItem.getIsTax())) {
					throw new Exception("品號:" + itemCode + " 稅別:" + imItem.getIsTax() + "欄位錯誤，非F(保稅)/P(完稅)");
				}
				// 檢核品號與稅別 by Weichun 2012.03.30
				if ("F".equals(itemCode.substring(itemCode.length() - 1))) {
					if (!imItem.getIsTax().equals("F"))
						throw new Exception("品號：" + itemCode + "與稅別：" + imItem.getIsTax() + "(完稅)不符！");
				}
				if ("P".equals(itemCode.substring(itemCode.length() - 1))) {
					if (!imItem.getIsTax().equals("P"))
						throw new Exception("品號：" + itemCode + "與稅別：" + imItem.getIsTax() + "(保稅)不符！");
				}
			}

			// 採購幣別
			if (!StringUtils.hasText(imItem.getPurchaseCurrencyCode())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入採購幣別");
			} else {
				List<BuCurrency> allCurrencyCodes = baseDAO.findByProperty("BuCurrency", "and currencyCode = ? ",
						new Object[] { imItem.getPurchaseCurrencyCode() });
				log.info("allCurrencyCodes = " + allCurrencyCodes);
				if (null == allCurrencyCodes || allCurrencyCodes.size() == 0) {
					throw new NoSuchDataException("品號:" + itemCode + "採購幣別:" + imItem.getPurchaseCurrencyCode() + "不存在");
				}
			}

			// 業種子類
			if (!StringUtils.hasText(imItem.getItemCategory())) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入業種子類");
			} else {
				ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.ITEM_CATEGORY, imItem.getItemCategory(), "Y");
				if (null == imItemCategory) {
					throw new NoSuchDataException("品號:" + itemCode + "業種子類:" + imItem.getItemCategory() + "不存在");
				} else {
					// 檢核是否為業種下的分類
					ImItemCategory imItemCategoryParent = imItemCategoryDAO.findByCategoryCode(brandCode,
							ImItemCategoryDAO.ITEM_CATEGORY, imItem.getItemCategory(), imItem.getCategoryType(), "Y");
					if (null == imItemCategoryParent) {
						throw new NoSuchDataException("品號:" + itemCode + "業種:" + imItem.getCategoryType() + " 不包含業種子類"
								+ imItem.getItemCategory());
					}
				}
			}

			// 原幣成本
			log.info("imItem.getPurchaseAmount() = " + imItem.getPurchaseAmount());
			if (null == imItem.getPurchaseAmount()) {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入原幣成本");
			} else if (imItem.getPurchaseAmount() < 0d) {
				throw new NoSuchDataException("品號:" + itemCode + "原幣成本不得小於0");
			} else {
				// 到小數六碼
				imItem.setPurchaseAmount(NumberUtils.round(imItem.getPurchaseAmount(), 6));
			}

			// // 銷售單位/報關單號換算
			// if(null == imItem.getDeclRatio()){
			// throw new NoSuchDataException("品號:" + itemCode + "未輸入銷售單位/報關單號換算");
			// }
			//
			// // 最小單位/銷貨單位的比例換算
			// if(null == imItem.getMinRatio()){
			// throw new NoSuchDataException("品號:" + itemCode + "未輸入最小單位/銷貨單位的比例換算");
			// }

			// 負庫存
			if (!StringUtils.hasText(imItem.getAllowMinusStock())) {
				imItem.setAllowMinusStock("N");
			} else {
				if (!"Y".equals(imItem.getAllowMinusStock()) && !"N".equals(imItem.getAllowMinusStock())) {
					throw new Exception("品號:" + itemCode + " 負庫存欄位錯誤，非Y/N");
				} else {
					if ("Y".equalsIgnoreCase((imItem.getAllowMinusStock()))) {
						if ("F".equalsIgnoreCase(imItem.getIsTax())) {
							throw new NoSuchDataException("品號:" + itemCode + "允許負庫存的稅別不得為保稅");
						} else if (!"N".equalsIgnoreCase(imItem.getLotControl())) {
							throw new NoSuchDataException("品號:" + itemCode + "允許負庫存的批號管理只能為 N-不執行批號管理");
						}
					}
				}
			}
        	// 酒精濃度
        	if ( null == imItem.getAlcolhoPercent()) {
        	    imItem.setAlcolhoPercent(0D);
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
			
		} else { // 百貨
					// 計價後成本
			if (StringUtils.hasText(enableExist)) {
				if ("Y".equalsIgnoreCase(enableExist)) { // 已訂價
					throw new Exception("品號:" + itemCode + "已訂價不得修改計價後成本:" + imItem.getStandardPurchaseCost());
				}
			}

			// 大類
			if (StringUtils.hasText(imItem.getCategory01())) {
				ImItemCategory imItemCategory01 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY01, imItem.getCategory01(), "Y");
				if (null == imItemCategory01) {
					throw new NoSuchDataException("品號:" + itemCode + "大類:" + imItem.getCategory01() + "不存在");
				}
			}
			// 中類
			if (StringUtils.hasText(imItem.getCategory02())) {
				ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), "Y");
				if (null == imItemCategory02) {
					throw new NoSuchDataException("品號:" + itemCode + "中類:" + imItem.getCategory02() + "不存在");
				} else {
					if (StringUtils.hasText(imItem.getCategory01())) {
						ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode,
								ImItemCategoryDAO.CATEGORY02, imItem.getCategory02(), imItem.getCategory01(), "Y");
						if (null == imItemCategory02s) {
							throw new NoSuchDataException("品號:" + itemCode + "大類" + imItem.getCategory01() + "不包含中類:"
									+ imItem.getCategory02());
						}
					}
				}
			}

			// 小類
			if (StringUtils.hasText(imItem.getCategory03())) {
				ImItemCategory imItemCategory03 = imItemCategoryDAO.findByCategoryCode(brandCode,
						ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), "Y");
				if (null == imItemCategory03) {
					throw new NoSuchDataException("品號:" + itemCode + "小類:" + imItem.getCategory03() + "不存在");
				} else {
					if (StringUtils.hasText(imItem.getCategory02())) {
						ImItemCategory imItemCategory02s = imItemCategoryDAO.findByCategoryCode(brandCode,
								ImItemCategoryDAO.CATEGORY03, imItem.getCategory03(), imItem.getCategory02(), "Y");
						if (null == imItemCategory02s) {
							throw new NoSuchDataException("品號:" + itemCode + "中類" + imItem.getCategory02() + "不包含小類:"
									+ imItem.getCategory03());
						}
					}
				}
			}

			// 寄賣品
			if (!StringUtils.hasText(imItem.getIsConsignSale())) {
				imItem.setIsConsignSale("N");
			}

			// 會計類別
			if (!StringUtils.hasText(imItem.getAccountCode())) {
				imItem.setAccountCode("4101001");
			}

			if (!StringUtils.hasText(imItem.getItemType())) {
				imItem.setItemType("N");
			}

			if (!StringUtils.hasText(imItem.getCategoryType())) {
				imItem.setCategoryType(brandCode);
			}

			// COACH手錶品號新舊自動轉換
			// POS資料交換時
			if ("HW".equals(imItem.getCategory01())) {
				if ("W".equals(itemCode.substring(2, 3))) {
					imItem.setReserve5(itemCode.substring(0, 2) + itemCode.substring(3));
				} else {
					throw new FormException("品號:" + itemCode + "其大類為手錶，但其品號第三並非「W」");
				}
			} else {
				imItem.setReserve5("");
			}
		}

		log.info("imItem.getItemId() = " + imItem.getItemId());

		// 商品啟用
		if (!(StringUtils.hasText(imItem.getEnable()))) {
			if (brandCode.indexOf("T2") > -1) {
				imItem.setEnable("Y");
			} else {
				throw new NoSuchDataException("品號:" + itemCode + "未輸入啟用(Enable)欄位");
			}
		} else {
			if (!"Y".equals(imItem.getEnable()) && !"N".equals(imItem.getEnable()))
				throw new NoSuchDataException("品號:" + itemCode + " 啟用(Enable)欄位錯誤，非Y/N");
		}

		// 補貨係數
		if (imItem.getReplenishCoefficient() == null || imItem.getReplenishCoefficient() == 0D) {
			imItem.setReplenishCoefficient(1D);
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


	String brandCode = imItem.getBrandCode();
	Double unitPrice = imItemPrice.getUnitPrice();

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
//	    if(!StringUtils.hasText(imItemPrice.getIsTax())){
//	    throw new Exception("未輸入含稅價，請檢查!");
//	    }else{
//	    if( !"P".equals(imItemPrice.getIsTax()) &&  !"F".equals(imItemPrice.getIsTax()) ){
//	    throw new Exception("含稅價必須為P(完稅)或F(免稅)，請檢查!");
//	    }
//	    }
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

	if( null == unitPrice ){
	    throw new Exception("未輸入目前價格，請檢查!");			
	}else{
	    if( imItemPrice.getUnitPrice() < 0d ){
		throw new Exception("目前價格:"+imItemPrice.getUnitPrice()+"必須大於或等於0，請檢查!");
	    }else{
		// 檢查不得含有小數點
		String enable = imItemPrice.getEnable();
		// 檢查未啟用的價錢
		if("N".equalsIgnoreCase(enable) ){
		    if(ValidateUtil.isDecimal(unitPrice) ){
			throw new Exception("目前價格:"+unitPrice+" 不得含有小數點");
		    }
		}
	    }
	}

	return imItemPrice;
    }



    /**
     * 複製DATA
     * @param oldItem DBItem 已存在資料庫的bean
     * @param imItem  匯入資料庫的bean
     */
    public void copyImportItemToDBItem(ImItem oldItem, ImItem imItem) throws Exception {

	oldItem.setBoxCapacity(imItem.getBoxCapacity());
	oldItem.setBrandCode(imItem.getBrandCode());
	oldItem.setCategory01(imItem.getCategory01());
	oldItem.setCategory02(imItem.getCategory02());
	oldItem.setCategory03(imItem.getCategory03());
	oldItem.setCategory04(imItem.getCategory04());
	oldItem.setCategory05(imItem.getCategory05());
	oldItem.setCategory06(imItem.getCategory06());
	oldItem.setCategory07(imItem.getCategory07());
	oldItem.setCategory08(imItem.getCategory08());
	oldItem.setCategory09(imItem.getCategory09());
	oldItem.setCategory10(imItem.getCategory10());
	oldItem.setCategory11(imItem.getCategory11());
	oldItem.setCategory12(imItem.getCategory12());
	oldItem.setCategory13(imItem.getCategory13());
	oldItem.setCategory14(imItem.getCategory14());
	oldItem.setCategory15(imItem.getCategory15());
	oldItem.setCategory16(imItem.getCategory16());
	oldItem.setCategory17(imItem.getCategory17());
	oldItem.setCategory18(imItem.getCategory18());
	oldItem.setCategory19(imItem.getCategory19());
	oldItem.setCategory20(imItem.getCategory20());
	oldItem.setCategory21(imItem.getCategory21());
	oldItem.setCategory22(imItem.getCategory22());
	
	//oldItem.setCreatedBy(imItem.getCreatedBy());
	//oldItem.setCreationDate(imItem.getCreationDate());
	oldItem.setDescription(imItem.getDescription());
	// System.out.println("old =" + oldItem.getEnable() + " new =" + imItem.getEnable() );
	oldItem.setEnable(imItem.getEnable());
	oldItem.setExpiryDate(imItem.getExpiryDate());
	oldItem.setForeignListPrice(imItem.getForeignListPrice());
	oldItem.setIsComposeItem(imItem.getIsComposeItem());
	oldItem.setIsServiceItem(imItem.getIsServiceItem());
	oldItem.setItemCName(imItem.getItemCName());
	oldItem.setItemCode(imItem.getItemCode());
	oldItem.setItemEName(imItem.getItemEName());
	oldItem.setItemLevel(imItem.getItemLevel());
	oldItem.setLastUpdateDate(imItem.getLastUpdateDate());
	oldItem.setLastUpdatedBy(imItem.getLastUpdatedBy());
	oldItem.setLotControl(imItem.getLotControl());
	oldItem.setPurchaseRatio(imItem.getPurchaseRatio());
	oldItem.setPurchaseUnit(imItem.getPurchaseUnit());
	oldItem.setReleaseDate(imItem.getReleaseDate());

	oldItem.setReserve1(imItem.getReserve1());
	oldItem.setReserve2(imItem.getReserve2());
	oldItem.setReserve3(imItem.getReserve3());
	oldItem.setReserve4(imItem.getReserve4());
	oldItem.setPayOline(imItem.getPayOline());
	oldItem.setReserve5(imItem.getReserve5());
	oldItem.setSalesRatio(imItem.getSalesRatio());
	oldItem.setSalesUnit(imItem.getSalesUnit());
	oldItem.setSpecHeight(imItem.getSpecHeight());
	oldItem.setSpecLength(imItem.getSpecLength());
	oldItem.setSpecWeight(imItem.getSpecWeight());
	oldItem.setSpecWidth(imItem.getSpecWidth());
	oldItem.setStandardPurchaseCost(imItem.getStandardPurchaseCost());
	oldItem.setSupplierItemCode(imItem.getSupplierItemCode());
	oldItem.setSupplierQuotationPrice(imItem.getSupplierQuotationPrice());
	oldItem = setDefaultValues(oldItem);

	if( imItem.getReserve5().indexOf("T2") > -1 ){
//	    oldItem.setAccountCode(imItem.getAccountCode());
	    oldItem.setReleaseString(imItem.getReleaseString()); // 上市日期字串 
	    oldItem.setExpiryString(imItem.getExpiryString()); // 下市日期字串
	    oldItem.setVipDiscount(imItem.getVipDiscount());
	    oldItem.setCategoryType(imItem.getCategoryType());
	    oldItem.setIsConsignSale(imItem.getIsConsignSale());
//	    oldItem.setCustomsItemCode(imItem.getCustomsItemCode());
	    oldItem.setItemType(imItem.getItemType());
	    oldItem.setItemBrand(imItem.getItemBrand());

//	    oldItem.setBonusType(imItem.getBonusType());
	    oldItem.setMargen(imItem.getMargen());
	    oldItem.setMaxPurchaseQuantity( imItem.getMaxPurchaseQuantity() );
	    oldItem.setMinPurchaseQuantity( imItem.getMinPurchaseQuantity() );
//	    oldItem.setBudgetType(imItem.getBudgetType());
	    oldItem.setIsTax(imItem.getIsTax());
	    oldItem.setPurchaseCurrencyCode(imItem.getPurchaseCurrencyCode());
	    oldItem.setItemCategory(imItem.getItemCategory());
	    oldItem.setPurchaseAmount(imItem.getPurchaseAmount());
//	    oldItem.setDeclRatio(imItem.getDeclRatio());
//	    oldItem.setMinRatio(imItem.getMinRatio());

//	    oldItem.setTaxRelativeItemCode(imItem.getTaxRelativeItemCode());
	    oldItem.setColorCode(imItem.getColorCode());
	    oldItem.setMaterial(imItem.getMaterial());
	    oldItem.setValidityDay(imItem.getValidityDay());
	    oldItem.setForeignCategory(imItem.getForeignCategory());
	    oldItem.setReplenishCoefficient(imItem.getReplenishCoefficient());
	    oldItem.setAllowMinusStock(imItem.getAllowMinusStock());
	    oldItem.setAlcolhoPercent(imItem.getAlcolhoPercent());
	}
    }
}
