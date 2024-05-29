package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.UniqueConstraintException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.BuVipPromote;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImItemSerial;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PosItemEancode;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemComposeDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEanPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemSerialDAO;
import tw.com.tm.erp.hbm.dao.ImMonthlyBalanceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.dao.SiSystemLogDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.view.Barcode;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.test.Ftp_Test;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA : 
 * PG : Weichun.Liao
 * Filename : ImItemService.java
 * Function : 
 * 
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	2012/2/22		Weichun.Liao	Create
 *--------------------------------------------------------------------------------------- 
 */
public class ImItemService {
    private static final Log log = LogFactory.getLog(ImItemService.class);

    private ImItemDAO imItemDAO;
    private NativeQueryDAO nativeQueryDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
    private ImItemOnHandViewDAO imItemOnHandViewDAO;
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    private ImItemPriceDAO imItemPriceDAO;
    private ImItemComposeDAO imItemComposeDAO;
    private BuBrandDAO buBrandDAO;
    private ImItemCategoryDAO imItemCategoryDAO;
    private BuCommonPhraseService buCommonPhraseService;
    private ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO;
    private BuCurrencyDAO buCurrencyDAO;
    private BaseDAO baseDAO;
    private ImItemEancodeDAO imItemEancodeDAO;
    private ImItemSerialDAO imItemSerialDAO;
    private ImItemEanPriceViewDAO imItemEanPriceViewDAO;
    private ImItemPriceViewDAO imItemPriceViewDAO;
    private BuEmployeeDAO buEmployeeDAO;
    private SiSystemLogDAO siSystemLogDAO;

	
    
    private BuBasicDataService buBasicDataService;
    private ImItemCategoryService imItemCategoryService;
    private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
    private ImReceiveHeadDAO imReceiveHeadDAO;
    private BuCountryDAO buCountryDAO;
	private PosExportDAO posExportDAO;

    private final static String SAVE = "SAVE";
    private final static String UPDATE = "UPDATE";

    private final static String T1 = "T1";
    private final static String T2 = "T2";

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    /**
     * 查詢欄位 商品主檔T1
     */
    public static final String[] T1_GRID_SEARCH_FIELD_NAMES = { "itemCode",
	"itemCName", "itemEName", "category01", "category02",
	"supplierItemCode",  "enableName", "itemId" }; // "itemLevel",

    public static final String[] T1_GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
	"", "", "", "", "", "" }; // "",

    /**
     * 查詢欄位 商品主檔T2
     */
    public static final String[] T2_GRID_SEARCH_FIELD_NAMES = {
	"itemCode", "itemCName", "itemEName", "category01", "category02",
	"supplierItemCode",  "enableName", "unitPrice", "salesUnit", "purchaseUnit",
	"priceLastUpdateDate", "itemBrand", "itemBrandName", "category17", "supplierName",
	"isTax", "itemId"
    }; // "itemLevel",

    public static final String[] T2_GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "", "", "",
	"", "", "", "", "",
	"", "", "", "", "",
	"", ""
    }; // "",

    /**
     * 查詢欄位 國際碼T2
     */
    public static final String[] T2_EANCODE_GRID_SEARCH_FIELD_NAMES = {
	"eanCode", "itemCode", "itemCName", "itemEName", "enableName ",
	"isTax",  "category01", "category01Name","itemBrand", "itemBrandName",
	"category02", "category02Name", "lastUpdatedBy", "lastUpdateDate", "createdBy",
	"creationDate", "itemId"
    };

    public static final String[] T2_EANCODE_GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "", "", "", "",
	"", "", "", "", "",
	"", "", "", "", "",
	"", ""
    };


    /**
     * (商品序號)查詢明細欄位
     */
    public static final String[] SERIAL_LINE_GRID_SEARCH_FIELD_NAMES = {
	"itemCode", "serial", "isUsedName",
	"lastUpdateDate", "lineId" };

    public static final String[] SERIAL_LINE_GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "","",
	"", "" };

    /**
     * (商品序號)查詢主檔序號欄位
     */
    public static final String[] SERIAL_GRID_SEARCH_FIELD_NAMES = {
	"itemCode", "serial", "isUsedName",
	"lastUpdateDate", "headId" };

    public static final String[] SERIAL_GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	"", "","",
	"", "" };

    /**
     * 標準售價明細欄位
     */
    public static final String[] T3_GRID_FIELD_NAMES = { "indexNo", "typeCode",
	"unitPrice", "beginDate", "createdByName", "enableName", "priceId" };

    public static final String[] T3_GRID_FIELD_DEFAULT_VALUES = { "", "", "",
	"", "", "停用", "" };

    public static final int[] T3_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG
    };

	/**
	 * 組合商品明細欄位
	 */
	public static final String[] T5_GRID_FIELD_NAMES = { "indexNo", "composeItemCode", "composeItemName", "quantity",
			"purchaseCurrencyCode", "itemPrice", "itemCost", "remark", "reserve1", "composeId" };

	public static final String[] T5_GRID_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "" };

	public static final int[] T5_GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG };

    /**
     * 庫存明細
     */
    public static final String[] T6_GRID_FIELD_NAMES = { "indexNo",
	"warehouseCode", "warehouseName", "stockOnHandQty",
	"outUncommitQty", "inUncommitQty", "moveUncommitQty",
	"otherUncommitQty", "currentOnHandQty" };

    public static final String[] T6_GRID_FIELD_DEFAULT_VALUES = { "", "", "",
	"", "", "", "", "", "" };

    public static final int[] T6_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE };

    /**
     * 月結明細
     */
    public static final String[] T7_GRID_FIELD_NAMES = { 
    "indexNo", "lineYear", "lineMonth", 
    "periodSalesQuantity", "periodPurchaseQuantity", "periodMovementQuantity", 
    "adjustQuantity", "periodOtherQuantity", "endingOnHandQuantity", 
    "averageUnitCost", "endingOnHandAmount" };
    public static final String[] T7_GRID_FIELD_DEFAULT_VALUES = { 
    "", "", "",
	"", "", "", 
	"", "", "",
	"", "" };

    public static final int[] T7_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE };

    /**
     * 國際碼
     */
    public static final String[] T9_GRID_FIELD_NAMES = {
	"indexNo",	"eanCode",		"enable",
	"lineId",	"message"  // "isLockRecord",	"isDeleteRecord",
    };

    public static final String[] T9_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "" // AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,
    };

    public static final int[] T9_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING // AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    };

    /**
     * 商品序號
     */
    public static final String[] SERIAL_GRID_FIELD_NAMES = {
	"indexNo",	"isUsed",		"serial",
	"lineId",	"isLockRecord",	"isDeleteRecord",  "message"
    };

    public static final String[] SERIAL_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""
    };

    public static final int[] SERIAL_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
    };

    /**
     * 進貨明細紀錄
     */
    public static final String[] T10_GRID_FIELD_NAMES = {
	"lineIndexNo", "imReceiveHead.orderTypeCode","imReceiveHead.orderNo",
	"imReceiveHead.receiptDate", "receiptQuantity","foreignUnitPrice" //,"quantity"
    };

    public static final String[] T10_GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", ""
    };

    public void setBuSupplierWithAddressViewDAO(
    	BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
        this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
        this.nativeQueryDAO = nativeQueryDAO;
    }
    
    public void setSiSystemLogDAO(SiSystemLogDAO siSystemLogDAO) {
		this.siSystemLogDAO = siSystemLogDAO;
	}

    public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
    	this.imItemPriceViewDAO = imItemPriceViewDAO;
        }

    public void setImItemEanPriceViewDAO(ImItemEanPriceViewDAO imItemEanPriceViewDAO) {
	this.imItemEanPriceViewDAO = imItemEanPriceViewDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
	this.imItemDAO = imItemDAO;
    }

    public void setBuCommonPhraseLineDAO(
	    BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	this.imItemCategoryDAO = imItemCategoryDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }
    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
    	this.buEmployeeDAO = buEmployeeDAO;
        }
    public void setBuCommonPhraseService(
	    BuCommonPhraseService buCommonPhraseService) {
	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
	this.imItemOnHandViewDAO = imItemOnHandViewDAO;
    }

    public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
	this.imItemPriceDAO = imItemPriceDAO;
    }

    public void setImItemComposeDAO(ImItemComposeDAO imItemComposeDAO) {
	this.imItemComposeDAO = imItemComposeDAO;
    }

    public void setImItemSerialDAO(ImItemSerialDAO imItemSerialDAO) {
	this.imItemSerialDAO = imItemSerialDAO;
    }

    public void setImMonthlyBalanceHeadDAO(
	    ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO) {
	this.imMonthlyBalanceHeadDAO = imMonthlyBalanceHeadDAO;
    }
    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	this.buCurrencyDAO = buCurrencyDAO;
    }
    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }
    public void setImItemEancodeDAO(ImItemEancodeDAO imItemEancodeDAO) {
	this.imItemEancodeDAO = imItemEancodeDAO;
    }
    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
	this.buBasicDataService = buBasicDataService;
    }
    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
	this.imItemCategoryService = imItemCategoryService;
    }

    public void setBuSupplierWithAddressViewService(
    		BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
    	this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
    }

    public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
    	this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }

    public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
    	this.imReceiveHeadDAO = imReceiveHeadDAO;
    }

    public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

    public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}


    /**
     * 匯入存檔用
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String save(ImItem modifyObj) throws Exception {
	if (null != modifyObj) {
	    imItemDAO.save(modifyObj);
	}
	return modifyObj.getItemCode() + "存檔成功";
    }

    /**
     * 匯入更新檔用
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String update(ImItem modifyObj) throws Exception {
	if (null != modifyObj) {
	    log.info("imItemDAO.update before");
	    imItemDAO.update(modifyObj);
	    log.info("imItemDAO.update after");
	}
	return modifyObj.getItemCode() + "存檔成功";
    }

    /**
     * 匯入國際碼存檔用
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String save(ImItemEancode modifyObj) throws Exception {
	if (null != modifyObj) {
	    imItemEancodeDAO.save(modifyObj);
	}
	return modifyObj.getItemCode() + "存檔成功";
    }

    /**
     * 匯入國際碼更新檔用
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String update(ImItemEancode modifyObj) throws Exception {
	if (null != modifyObj) {
	    imItemEancodeDAO.update(modifyObj);
	}
	return modifyObj.getItemCode() + "存檔成功";
    }

    public String save(ImItem imItem, String action) throws Exception {
	try {

	    if("N".equals(imItem.getIsComposeItem())){
		imItem.setImItemComposes(new ArrayList(0));
	    }

	    checkImItem(imItem, action);// 檢查商品主檔
	    checkImItemPrice(imItem); // 檢查商品價格
	    checkImItemEanCode(imItem); // 檢查國際碼

	    if (SAVE.equals(action)){
		if( imItem.getBrandCode().indexOf("T2") <= -1 ){
		    imItem.setItemType("N");
		    imItem.setBudgetType("A");
		    imItem.setCategoryType(imItem.getBrandCode());
		    imItem.setItemCategory(imItem.getBrandCode());
		}
//		else{
//		imItem.setItemBrand("AA");
//		}

		imItemDAO.save(imItem);
	    }else{
		imItemDAO.update(imItem);
	    }
	    return imItem.getItemCode() + "存檔成功";

	} catch (FormException fe) {
	    log.error("更新商品主檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());

	} catch (Exception ex) {
	    log.error("更新商品主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}
    }

    /**
     * create item no validate 20080922 shan
     *
     * @param modifyObj
     * @return
     * @throws Exception
     */
    public String create(ImItem modifyObj) throws Exception {
	if (null != modifyObj) {
	    if (!isDuplication(modifyObj.getBrandCode(), modifyObj
		    .getItemCode())) {
		imItemDAO.save(modifyObj);
	    } else {
		imItemDAO.update(modifyObj);
	    }

	}
	return modifyObj.getItemCode() + "存檔成功";
    }

    /**
     * 檢核是否為重覆之品號
     *
     * @param itemCode
     * @return boolean
     */
    public boolean isDuplication(String itemCode) throws Exception {
	try {
	    if (itemCode != null && !("".equals(itemCode))) {
		if (imItemDAO.findById(itemCode) != null) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		throw new FormException("輸入的品號為空白，請重新輸入");

	    }
	} catch (Exception ex) {
	    log.error("檢核品號重覆時發生錯誤，原因：" + ex.toString());
	    throw new Exception("檢核品號重覆時發生錯誤，原因：" + ex.getMessage());

	}

    }

    public boolean isDuplication(String brandCode, String itemCode)
    throws Exception {
	try {
	    if (StringUtils.hasText(brandCode) && StringUtils.hasText(itemCode)) {
		if (imItemDAO.findItem(brandCode, itemCode) != null) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		throw new FormException("輸入的品號為空白，請重新輸入");

	    }
	} catch (Exception ex) {
	    log.error("檢核品號重覆時發生錯誤，原因：" + ex.toString());
	    throw new Exception("檢核品號重覆時發生錯誤，原因：" + ex.getMessage());

	}

    }

    /**
     * 檢核商品主檔
     * @param imItem
     * @param action
     * @throws Exception
     */
    public void checkImItem(ImItem imItem, String action) throws Exception {
	String brandCode = imItem.getBrandCode();
	String itemCode = imItem.getItemCode();

	BuBrand buBrand = buBrandDAO.findById(brandCode);
	if (null == buBrand) {
	    throw new NoSuchDataException("品號：" + itemCode
		    + "依據品牌：" + brandCode + "查無其品牌代號！");
	}

	if(itemCode.length() > 13 ){
	    throw new NoSuchDataException("品號：" + itemCode + "必須小於13碼以下" );
	}

	log.info("Pattern.compile([^a-zA-Z\\d\\s]+) = " + Pattern.compile("[^a-zA-Z\\d\\s]+").matcher( itemCode ).find());
	if( Pattern.compile("[^a-zA-Z\\d\\s]+").matcher( itemCode ).find() ){
	    throw new NoSuchDataException("品號：" + itemCode
		    + "不可有特殊符號，請重新輸入!");
	}else if(StringUtils.containsWhitespace(itemCode)){
	    throw new NoSuchDataException("品號：" + itemCode
		    + "不可含有空白，請重新輸入!");
	}

	if(brandCode.indexOf("T2") > -1){
	    // 打品號,檢查不能等於B品號已啟用的國際碼
	    ImItemEancode line = (ImItemEancode)imItemEancodeDAO.findOneEanCodeByProperty(brandCode, itemCode,itemCode) ;
	    if(null != line){
		throw new FormException("品號:" + itemCode
			+ " 不能等於品號:"+line.getItemCode()+" 已啟用的國際碼:" + line.getEanCode());
	    }

	    // 檢核大類中類,商品品牌,業種,業種子類,稅別,原幣成本必填
	    if(!StringUtils.hasText(imItem.getCategory01())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入大類");
	    }
	    if(!StringUtils.hasText(imItem.getCategory02())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入中類");
	    }

	    // 製造商/供應商
	    if(!StringUtils.hasText(imItem.getCategory17())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入製造商/供應商");
	    }else{
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(brandCode, imItem.getCategory17());
		if(null == buSupplierWithAddressView){
		    throw new NoSuchDataException("品號:" + itemCode
			    + "查無此製造商/供應商:"+imItem.getCategory17());
		}
	    }


	    if(!StringUtils.hasText(imItem.getIsTax())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入稅別");
	    }
	    if(!StringUtils.hasText(imItem.getCategoryType())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入業種");
	    }
	    if(!StringUtils.hasText(imItem.getItemCategory())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入業種子類");
	    }
	    if(!StringUtils.hasText(imItem.getItemBrand())){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入商品品牌");
	    }else{
		ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, ImItemCategoryDAO.ITEM_BRAND, imItem.getItemBrand());
		if(imItemCategory == null){
		    throw new FormException("品號:" + itemCode
			    + "的商品品牌" + imItem.getItemBrand() + "不存在");
		}
	    }

	    if(imItem.getPurchaseAmount() == null){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入原幣成本");
	    }else if(imItem.getPurchaseAmount() < 0d ){
		throw new NoSuchDataException("品號:" + itemCode
			+ "原幣成本不得小於0");
	    }else{
		// 到小數六碼
		imItem.setPurchaseAmount(NumberUtils.round(imItem.getPurchaseAmount(), 6));
	    }

	    // 採購幣別
	    if(null == imItem.getPurchaseCurrencyCode()){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入採購幣別");
	    }

	    // vip折扣
	    if(null == imItem.getVipDiscount()){
		throw new NoSuchDataException("品號:" + itemCode
			+ "未輸入VIP折扣");
	    }

//	    // 銷售單位/報關單號換算
//	    if(null == imItem.getDeclRatio()){
//	    throw new NoSuchDataException("品號:" + itemCode
//	    + "未輸入銷售單位/報關單號換算");
//	    }

//	    // 最小單位/銷貨單位的比例換算
//	    if(null == imItem.getMinRatio()){
//	    throw new NoSuchDataException("品號:" + itemCode
//	    + "未輸入最小單位/銷貨單位的比例換算");
//	    }

	    // 負庫存
	    if("Y".equalsIgnoreCase((imItem.getAllowMinusStock()))){
		if("F".equalsIgnoreCase(imItem.getIsTax())){
		    throw new NoSuchDataException("品號:" + itemCode
			    + "允許負庫存的稅別不得為保稅");
		}else if(!"N".equalsIgnoreCase(imItem.getLotControl())){
		    throw new NoSuchDataException("品號:" + itemCode
			    + "允許負庫存的批號管理只能為 N-不執行批號管理");
		}
	    }
	    // 酒精濃度
	    if(null == imItem.getAlcolhoPercent()){
		throw new NoSuchDataException("品號:" + itemCode
			+ "酒精濃度數值必須 <= 100 或 >= 0");
	    }else{
	    	if( imItem.getAlcolhoPercent() > 100 || imItem.getAlcolhoPercent() < 0){
	    		throw new NoSuchDataException("品號:" + itemCode
	    				+ "酒精濃度數值必須 <= 100 或 >= 0");
	    	}
	    }
	}


	log.info("imItem.getItemId() = " + imItem.getItemId());

	if (null != imItem.getItemId()){
	    if (StringUtils.hasText(imItem.getIsComposeItem())) {
		if ("Y".equals(imItem.getIsComposeItem())) {
		    List<ImItemCompose> imItemComposes = imItem.getImItemComposes();
		    checkComposeItem(brandCode, itemCode,
			    imItemComposes);
		}
	    } else {
		throw new NoSuchDataException("品號:" + itemCode
			+ "組合商品(isComposeItem)欄位錯誤");
	    }
	}

	if (!(StringUtils.hasText(imItem.getEnable()))) {
	    throw new NoSuchDataException("品號:" + itemCode
		    + "啟用(Enable)欄位錯誤");
	} else {
	    if (!"Y".equals(imItem.getEnable())
		    && !"N".equals(imItem.getEnable()))
		throw new NoSuchDataException("品號:" + itemCode
			+ "啟用(Enable)欄位錯誤，非Y/N");
	}

	if (!(StringUtils.hasText(imItem.getLotControl()))) {
	    throw new NoSuchDataException("品號:" + itemCode
		    + "批號管理(LotControl)欄位錯誤");
	} else {
	    List<BuCommonPhraseLine> allLotControls = baseDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ? ", new Object[]{ "LotControl", imItem.getLotControl(), "Y" } );
	    if(null == allLotControls){
		throw new NoSuchDataException("品號:" + itemCode
			+ "批號管理(LotControl)欄位錯誤，非N/1/2");
	    }
	}

	if (!StringUtils.hasText(imItem.getIsServiceItem())) {
	    imItem.setIsServiceItem("N");
	}

	if (!StringUtils.hasText(imItem.getIsConsignSale())) {
	    imItem.setIsConsignSale("N");
	}

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
		imItem.setReserve5(itemCode.substring(0, 2)
			+ itemCode.substring(3));
	    } else {
		throw new FormException("品號:" + itemCode
			+ "其大類為手錶，但其品號第三並非「W」");
	    }
	} else {
	    imItem.setReserve5("");
	}
	if (SAVE.equals(action)) {
	    if (this.isDuplication(brandCode, itemCode)) {
		throw new FormException("品號:" + itemCode
			+ "己存在，請重新輸入");
	    }
	}

	// UPDATEITEM 僅 update imItem 不 check imItemPrice 2009.10.27 arthur
	if (!"UPDATEITEM".equals(action))
	    checkItemPrice(brandCode, itemCode, imItem
		    .getImItemPrices());
	// 補貨係數
	if(imItem.getReplenishCoefficient() == null ){
	    imItem.setReplenishCoefficient(1D);
	}
    }

    /**
     * 檢查售價
     * @param head
     * @throws Exception
     */
    public void checkImItemPrice(ImItem head) throws Exception {
	List<ImItemPrice> imitemPrices = head.getImItemPrices();
	for (ImItemPrice imItemPrice : imitemPrices) {
	    Double unitPrice = imItemPrice.getUnitPrice();
	    String enable = imItemPrice.getEnable();
	    // 檢查未啟用的價錢
	    if("N".equalsIgnoreCase(enable) ){
		if( imItemPrice.getUnitPrice() < 0d ){
		    throw new Exception("售價:"+unitPrice+"必須大於或等於0，請檢查!");
		}else{
		    if(ValidateUtil.isDecimal(unitPrice) ){
			throw new Exception("售價:"+unitPrice+" 不得含有小數點");
		    }
		}
	    }
	}
    }

    /**
     * 檢核國際碼
     * @param imItem
     * @throws Exception
     */
    public void checkImItemEanCode(ImItem head) throws Exception {
	int i = 1;
	Map map = new HashMap();
	String tab = "國際碼明細";

	List<ImItemEancode> imItemEancodes = head.getImItemEancodes();
	if(imItemEancodes != null && imItemEancodes.size() > 0){
	    for (ImItemEancode imItemEancode : imItemEancodes) {
		String eanCode = imItemEancode.getEanCode();
		String message = imItemEancode.getMessage();

		if( map.containsKey(eanCode)){
		    throw new UniqueConstraintException("的第" + i + "筆的國際碼:" + map.get(eanCode) + "重複，請重新輸入");
		}else{
		    map.put(eanCode, eanCode);
		}
//		if( message != null ){
//		throw new UniqueConstraintException(tab + "第" + i + "筆的國際碼:" + imItemEancode.getEanCode() + "己存在，請重新輸入");
//		}
		i++;
	    }
	}
    }

    /**
     * 檢核商品序號
     * @param imItem
     * @throws Exception
     */
    public void checkImItemSerial(ImItem head) throws Exception {
	int i = 1;
	Map map = new HashMap();
	String tab = "商品序號明細";

	List<ImItemSerial> imItemSerials = head.getImItemSerials();
	if(imItemSerials != null && imItemSerials.size() > 0){
	    for (ImItemSerial imItemSerial : imItemSerials) {
		String serial = imItemSerial.getSerial();
//		String message = imItemSerial.getMessage();

		if( map.containsKey(serial)){
		    throw new UniqueConstraintException(tab + "第" + i + "筆序號:" + map.get(serial) + "重複，請重新輸入");
		}else{
		    map.put(serial, serial);
		}
//		if( message != null ){
//		throw new UniqueConstraintException(tab + "第" + i + "筆序號:" + serial + "己存在，請重新輸入");
//		}
		i++;
	    }
	}
    }

    private void checkComposeItem(String brandCode, String itemCode,
	    List<ImItemCompose> imItemComposes) throws FormException {
	if (imItemComposes.size() > 0) {
	    int i = 1;
	    for (ImItemCompose imItemCompose : imItemComposes) {
		imItemCompose.setBrandCode(brandCode);
		imItemCompose.setItemCode(itemCode);
		if (StringUtils.hasText(imItemCompose.getComposeItemCode())) {
		    if (imItemDAO.findItem(imItemCompose.getBrandCode(),
			    imItemCompose.getComposeItemCode()) != null) {
			if (imItemCompose.getQuantity() <= 0) {
			    throw new FormException("組合商品中第" + i + "項組合品項("
				    + imItemCompose.getComposeItemCode()
				    + ")未輸入「數量」");
			}
		    } else {
			throw new FormException("查無第" + i + "項組合品項之品號("
				+ imItemCompose.getComposeItemCode() + ")");
		    }
		} else {
		    throw new FormException("組合商品中第" + i + "項組合品項未輸入品號");
		}
		i++;
	    }
	} else {
	    throw new FormException("未輸入組合商品組合品項，請重新輸入");
	}

    }

    public void checkItemPrice(String brandCode, String itemCode,
	    List<ImItemPrice> imItemPrices) throws FormException {

	HashMap enablePriceMap = new HashMap();
	HashMap disablePriceMap = new HashMap();
	String priceType = "";
	String priceTypeCode = "";

	for (ImItemPrice imItemPrice : imItemPrices) {
	    imItemPrice.setBrandCode(brandCode);
	    imItemPrice.setItemCode(itemCode);
	    imItemPrice.setTaxCode("3");
	    imItemPrice
	    .setIsTax("F".equals(imItemPrice.getIsTax()) ? "F" : "P");

	    // log.info( imItemPrice.getUnitPrice() + "-" +
	    // imItemPrice.getEnable() + "*" + imItemPrice.getPriceId() );
	    priceType = imItemPrice.getTypeCode();
	    if ("Y".equals(imItemPrice.getEnable())) { // 已啟用
		if (!(StringUtils.hasText((String) enablePriceMap
			.get(priceType)))) {
		    enablePriceMap.put(priceType, "Y");
		}
	    } else {
		if (!(StringUtils.hasText((String) disablePriceMap
			.get(priceType)))) {
		    disablePriceMap.put(priceType, "N");
		} else {
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO
		    .findById("PriceType", priceType);
		    throw new FormException("價格資料中「"
			    + (buCommonPhraseLine != null ? buCommonPhraseLine
				    .getName() : "") + "」重覆，請重新輸入");
		}
	    }
	}

	// log.info(disablePriceMap + "*" + enablePriceMap );
	if (!(disablePriceMap.isEmpty()) && !(enablePriceMap.isEmpty())) {
	    List<BuCommonPhraseLine> priceTypes = buCommonPhraseLineDAO
	    .findEnableLineById("PriceType");
	    for (BuCommonPhraseLine type : priceTypes) {
		priceTypeCode = type.getId().getLineCode();
		if (StringUtils.hasText((String) disablePriceMap
			.get(priceTypeCode))) {
		    if (StringUtils.hasText((String) enablePriceMap
			    .get(priceTypeCode))) {
			throw new FormException("價格資料中「" + type.getName()
				+ "」已啟用，如需更改價格，請使用變價單");
		    }
		}
	    }
	}
    }

    /**
     * 將國際碼明細表 mark 為刪除的刪掉
     * @param head
     */
    public void deleteImItemEanCode(ImItem head){
	List<ImItemEancode> imItemEancodes = head.getImItemEancodes();
	if(imItemEancodes != null && imItemEancodes.size() > 0){
	    for(int i = imItemEancodes.size() - 1; i >= 0; i--){
		ImItemEancode imItemEancode = imItemEancodes.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imItemEancode.getIsDeleteRecord())){
		    log.info( "刪除 i = " + i);
		    imItemEancodes.remove(imItemEancode);
		    imItemEancode.setImItem(null);
		    imItemEancodeDAO.delete(imItemEancode);
		}
	    }
	}
    }

    /**
     * 將商品序號明細表 mark 為刪除的刪掉
     * @param head
     */
    public void deleteImItemSerial(ImItem head){
	List<ImItemSerial> imItemSerials = head.getImItemSerials();
	if(imItemSerials != null && imItemSerials.size() > 0){
	    for(int i = imItemSerials.size() - 1; i >= 0; i--){
		ImItemSerial imItemSerial = imItemSerials.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imItemSerial.getIsDeleteRecord())){
		    if("N".equalsIgnoreCase(imItemSerial.getIsUsed())){
			log.info( "刪除 i = " + i);
			imItemSerials.remove(imItemSerial);
			imItemSerial.setItemId(null);
			imItemSerialDAO.delete(imItemSerial);
		    }else{
			imItemSerial.setMessage("已被使用過的不能刪除");
		    }
		}
	    }
	}
    }

    public ImItem findById(String itemCode) {
	return imItemDAO.findById(itemCode);
    }

    public ImItem findByItemId(Long id) {
	return imItemDAO.findByItemId(id);
    }

    /**
     * 商品資料清單查詢作業
     *
     * @param brandId
     * @param startItemCode
     * @param endItemCode
     * @param itemName
     * @param enable
     * @param categorySearchString
     * @return List
     */
    public List<ImItem> findItemList(String brandCode, String startItemCode,
	    String endItemCode, String itemName, String enable,
	    String categorySearchString) throws Exception {
	try {

	    return imItemDAO.findItemList(brandCode, startItemCode,
		    endItemCode, itemName, enable, categorySearchString);

	} catch (Exception ex) {
	    log.error("查詢商品資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public ImItem findItem(String brandCode, String itemCode) throws Exception {

	try {
	    System.out.println(brandCode + "/" + itemCode);
	    return imItemDAO.findItem(brandCode, itemCode);

	} catch (Exception ex) {
	    log.error("查詢商品資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品資料時發生錯誤，原因：" + ex.getMessage());
	}

    }

    /**
     * 商品資料查詢作業
     *
     * @param itemCode
     * @return List
     */

    public List<ImItem> findByHsql(String hsql) throws Exception {

	try {

	    return imItemDAO.getHibernateTemplate().find(hsql);

	} catch (Exception ex) {
	    log.error("查詢商品資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 利用ID 取得多個 ITEM 20081007 shan
     *
     * @param ids
     * @return
     */
    public List<ImItem> find(String ids) {
	return imItemDAO.find(ids);
    }

    /**
     * 搜尋組出的sql
     * @param httpRequest
     * @return
     */
    public Map getSearchSQL(Properties httpRequest){
	Map map = new HashMap();

	String startItemCode = httpRequest.getProperty("startItemCode");
	String enable = httpRequest.getProperty("enable");
	String brandCode = httpRequest.getProperty("brandCode");
	String itemName = httpRequest.getProperty("itemName");

	String supplierItemCode = httpRequest.getProperty("supplierItemCode");
	String category01 = httpRequest.getProperty("category01");
	String category02 = httpRequest.getProperty("category02");
	String category03 = httpRequest.getProperty("category03");

	String category13 = httpRequest.getProperty("category13"); // 系列
	String category17 = httpRequest.getProperty("category17"); // 製造商

	String itemBrand = httpRequest.getProperty("itemBrand");	// 商品品牌
	String category05 = httpRequest.getProperty("category05");
	String category06 = httpRequest.getProperty("category06");
	String itemEName = httpRequest.getProperty("itemEName");
	String category11 = httpRequest.getProperty("category11");
	String foreignCategory = httpRequest.getProperty("foreignCategory");

	// 組sql 和最大筆數sql
	StringBuffer sql = new StringBuffer();
	StringBuffer sqlMax = new StringBuffer();
	StringBuffer sqlBody = new StringBuffer();

	sql.append("SELECT E.EAN_CODE, I.ITEM_CODE, I.ITEM_C_NAME, I.ITEM_E_NAME, DECODE(E.ENABLE, 'Y', '啟用', '停用') AS ENABLE, ")
	.append("I.IS_TAX, I.CATEGORY01, ")
	.append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'CATEGORY01' AND I.CATEGORY01 = C.CATEGORY_CODE AND I.BRAND_CODE = C.BRAND_CODE) AS CATEGORY01_NAME, " )
	.append("I.ITEM_BRAND, ")
	.append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'ItemBrand' AND I.ITEM_BRAND = C.CATEGORY_CODE AND I.BRAND_CODE = C.BRAND_CODE) AS ITEM_BRAND_NAME, ")
	.append("I.CATEGORY02, ")
	.append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'CATEGORY02' AND I.CATEGORY02 = C.CATEGORY_CODE AND I.BRAND_CODE = C.BRAND_CODE) AS CATEGORY02_NAME, " )
	.append("E.LAST_UPDATED_BY, E.LAST_UPDATE_DATE, E.CREATED_BY, E.CREATION_DATE, I.ITEM_ID ");

	sqlMax.append("SELECT count(I.ITEM_CODE) AS rowCount ");

	//sqlBody.append("FROM IM_ITEM I, IM_ITEM_EANCODE E ")
	//.append("WHERE I.ITEM_ID = E.ITEM_ID ");
	sqlBody.append("FROM IM_ITEM I LEFT JOIN IM_ITEM_EANCODE E on I.ITEM_ID = E.ITEM_ID WHERE 1=1 ");

	if(StringUtils.hasText(enable)){
	    sqlBody.append("AND E.ENABLE ='").append(enable).append("' ");
	}

	if(StringUtils.hasText(category01)){
	    sqlBody.append("AND I.CATEGORY01 ='").append(category01).append("' ");
	}
	if(StringUtils.hasText(category02)){
	    sqlBody.append("AND I.CATEGORY02 ='").append(category02).append("' ");
	}
	if(StringUtils.hasText(category03)){
	    sqlBody.append("AND I.CATEGORY03 ='").append(category03).append("' ");
	}

	StringBuffer newCodes = new StringBuffer();
	StringBuffer sqlCodes = new StringBuffer();
	String itemCodes = httpRequest.getProperty("itemCodes");
	if(StringUtils.hasText(itemCodes)){ // 已多品號或國際碼為主
	    String[] codes = itemCodes.split(",");
	    for (String code : codes) {
		newCodes.append("'").append(code).append("'").append(",");
	    }
	    if(codes.length > 0){
		newCodes.delete(newCodes.length()-1, newCodes.length());
	    }
	    String needItemCodes = newCodes.toString();
	    //sqlCodes.insert(0, "AND (E.ITEM_CODE IN (").append(needItemCodes).append(") OR E.EAN_CODE IN (").append(needItemCodes).append(")) ");
	    sqlCodes.insert(0, "AND (I.ITEM_CODE IN (").append(needItemCodes).append(") OR E.EAN_CODE IN (").append(needItemCodes).append(")) ");
	}else{
	    if(StringUtils.hasText(startItemCode)){
		//sqlBody.append("AND (E.ITEM_CODE like '%").append(startItemCode).append("%' OR E.EAN_CODE LIKE '%").append(startItemCode).append("%') ");
	    sqlBody.append("AND (I.ITEM_CODE like '%").append(startItemCode).append("%' OR E.EAN_CODE LIKE '%").append(startItemCode).append("%') ");	
	    }
	}

	if(StringUtils.hasText(itemBrand)){
	    sqlBody.append("AND I.ITEM_BRAND ='").append(itemBrand).append("' ");
	}
	if(StringUtils.hasText(itemEName)){
	    sqlBody.append("AND I.ITEM_E_NAME ='").append(itemEName).append("' ");
	}
	if(StringUtils.hasText(category11)){
	    sqlBody.append("AND I.CATEGORY11 like '%").append(category11).append("%' ");
	}
	if(StringUtils.hasText(foreignCategory)){
	    sqlBody.append("AND I.FOREIGN_CATEGORY = '").append(foreignCategory).append("' ");
	}
	if(StringUtils.hasText(itemName)){
	    sqlBody.append("AND I.ITEM_C_NAME like '%").append(itemName).append("%' ");
	}
	if(StringUtils.hasText(supplierItemCode)){
	    sqlBody.append("AND I.SUPPLIER_ITEM_CODE = '").append(supplierItemCode).append("' ");
	}
	if(StringUtils.hasText(category05)){
	    sqlBody.append("AND I.CATEGORY05 = '").append(category05).append("' ");
	}
	if(StringUtils.hasText(category06)){
	    sqlBody.append("AND I.CATEGORY06 = '").append(category06).append("' ");
	}
	if(StringUtils.hasText(category13)){
	    sqlBody.append("AND I.CATEGORY13 like '%").append(category13).append("%' ");
	}
	if(StringUtils.hasText(category17)){
	    sqlBody.append("AND I.CATEGORY17 like '%").append(category17).append("%' ");
	}
	sqlBody.append(sqlCodes.toString()).append(" and I.BRAND_CODE = '").append(brandCode).append("' ");

	sql.append(sqlBody);
	sqlMax.append(sqlBody);

	map.put("sql", sql.toString());
	map.put("sqlMax", sqlMax.toString());
	return map;
    }

    public String getItemCName(String brandCode, String itemCode) {
	List items = imItemCurrentPriceViewDAO.findByProperty(
		"ImItemCurrentPriceView", new String[] { "brandCode",
		"itemCode" }, new String[] { brandCode, itemCode });
	if ((null != items) && (items.size() > 0)) {
	    ImItemCurrentPriceView imItemCurrentPriceView = (ImItemCurrentPriceView) items
	    .get(0);
	    if (StringUtils.hasText(imItemCurrentPriceView.getItemCName())) {
		return imItemCurrentPriceView.getItemCName();
	    } else {
		return "商品未命名";
	    }
	}
	return null;
    }

    public List getImItemCurrentPriceView(String brandCode, String itemCode) {
	List items = imItemCurrentPriceViewDAO.findByProperty(
		"ImItemCurrentPriceView", new String[] { "brandCode",
		"itemCode" }, new String[] { brandCode, itemCode });
	return items;
    }

    /**
     * ajax取得商品名稱
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXItemName(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String itemCode = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    itemCode = httpRequest.getProperty("itemCode");
	    itemCode = itemCode.trim().toUpperCase();
	    Long itemId = NumberUtils.getLong( httpRequest.getProperty("itemId") );

	    if( itemCode != ""){
		properties.setProperty("itemCode", itemCode);

		ImItem imItem = findItem(brandCode, itemCode);
		if(imItem != null){
		    properties.setProperty("itemCode", AjaxUtils.getPropertiesValue( imItem.getItemCode(), ""));

		    properties.setProperty("itemName", AjaxUtils.getPropertiesValue( imItem.getItemCName(), ""));
		} else {
		    properties.setProperty("itemName", "");
		}
	    }else if( itemId != 0L ){
		ImItem imItem = findByItemId(itemId);
		if(imItem != null){
		    properties.setProperty("itemCode", AjaxUtils.getPropertiesValue( imItem.getItemCode(), ""));
		    properties.setProperty("itemName", AjaxUtils.getPropertiesValue( imItem.getItemCName(), ""));
		} else {
		    properties.setProperty("itemName", "");
		}
	    }

	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、品號：" + itemCode + "查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢品號資料失敗！");
	}
    }

    /**
     * ajax取得商品序號
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXSerial(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String serial = null;
	ImItemSerial imItemSerial = null;
	Long itemId = null;
	Long lineId = null;
	try {
	    serial = AjaxUtils.getPropertiesValue(httpRequest.getProperty("serial"),"");
	    lineId = NumberUtils.getLong(httpRequest.getProperty("lineId"));
	    serial = serial.trim().toUpperCase();
	    itemId = NumberUtils.getLong( httpRequest.getProperty("itemId") );

	    // by picker
	    if(lineId != 0L){
		imItemSerial = (ImItemSerial)imItemSerialDAO.findFirstByProperty("ImItemSerial", "and lineId = ?", new Object[]{lineId});
		if(imItemSerial != null){
		    properties.setProperty("serial", AjaxUtils.getPropertiesValue( imItemSerial.getSerial(), ""));
		    properties.setProperty("serialMemo", "");
		} else {
		    properties.setProperty("serialMemo", "查無此資料");
		}
	    }else if(StringUtils.hasText(serial) ){ // by 手打
		imItemSerial = (ImItemSerial)imItemSerialDAO.findFirstByProperty("ImItemSerial", "and itemId = ? and serial = ?", new Object[]{itemId, serial});

		if(imItemSerial != null){
		    properties.setProperty("serial", AjaxUtils.getPropertiesValue( imItemSerial.getSerial(), ""));
		    properties.setProperty("serialMemo", "");
		} else {
		    properties.setProperty("serial", AjaxUtils.getPropertiesValue( serial, ""));
		    properties.setProperty("serialMemo", "查無此資料");
		}
	    }

	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據itemId:" + itemId + "查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢品號資料失敗！");
	}
    }

    /**
     * 查詢啟用日期的變價商品
     *
     * @param beginDate
     * @return
     */
    public List<ImItem> findByBeginDate(Date beginDate, String brandCode) {
	return imItemDAO.findByBeginDate(beginDate, brandCode);
    }

    /**
     * 利用啟用日期新增要列印的變價商品條碼
     *
     * @param beginDate
     * @param brandCode
     * @return
     */
    public List<Barcode> findByBeginDateBarCode(Date beginDate, String brandCode) throws Exception{
    	// 判斷重覆
    	Set itemCodeExist = new HashSet();
    	List<Barcode> barcodes = new ArrayList();
    	List items = imItemDAO.findByBeginDate(beginDate, brandCode);
    	for (int index = 0; index < items.size(); index++) {
    		Object[] item = (Object[]) items.get(index);
    		String itemCode = (String) item[0];
    		if (!itemCodeExist.contains(itemCode)) {
    			Barcode barcode = new Barcode();
    			barcode.setItemCode(itemCode);
    			barcode.setItemCName((String) item[1]);
    			if(StringUtils.hasText((String) item[2])){
					BuCountry buCountry = buCountryDAO.findById((String) item[2]);
					if(null != buCountry)
						barcode.setCategory14(buCountry.getCountryCName());
				}
    			//barcode.setCategory14((String) item[2]);
    			barcode.setCategory08((String) item[3]);
    			barcode.setUnitPrice((Double) item[4]);
    			barcode.setQuantity(0d);

    			//設置日期(商品最近進貨日期)
    			Date orderDate = imReceiveHeadDAO.findOrderDateByItemCode(brandCode, itemCode );
    			if(null != orderDate){
    				Calendar cal = Calendar.getInstance();
    				cal.setTime(orderDate);
    				if("T1CO".equals(brandCode))
    					cal.add(Calendar.MONTH, -1);
    				barcode.setDate(cal.get(Calendar.YEAR) + "." + String.format("%02d", cal.get(Calendar.MONTH) + 1 ));
    			}

    			barcodes.add(barcode);
    			itemCodeExist.add(itemCode);
    		}
    	}
    	return barcodes;
    }

    /**
     * 新增要列印的商品條碼
     *
     * @param brandCode
     * @param itemCode
     * @param barcodes
     * @throws Exception
     */
    public List<Barcode> addItemToBarCode(String brandCode, String itemCode,
	    List<Barcode> barcodes) throws Exception {
	if (null == barcodes) {
	    barcodes = new ArrayList();
	}
	if (StringUtils.hasText(brandCode) && StringUtils.hasText(itemCode)) {
	    Barcode barcode = new Barcode();
	    ImItem item = findItem(brandCode, itemCode);
	    if (null != item) {
		barcode.setItemCode(item.getItemCode());
		barcode.setItemCName(item.getItemCName());
		if(null != item.getCategory14()){
			BuCountry buCountry = buCountryDAO.findById(item.getCategory14());
			if(null != buCountry)
				barcode.setCategory14(buCountry.getCountryCName());
		}
		//barcode.setCategory14(item.getCategory14());
		barcode.setCategory08(item.getCategory08());
		Double itemPrice = new Double(0);

		ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO
		.findCurrentPriceByValue(brandCode, itemCode, "1");
		if (null != imItemCurrentPriceView
			&& null != imItemCurrentPriceView.getUnitPrice())
		    itemPrice = imItemCurrentPriceView.getUnitPrice();

		barcode.setUnitPrice(itemPrice);
		barcode.setQuantity(1d);

		//設置日期(商品最近進貨日期)
		Date orderDate = imReceiveHeadDAO.findOrderDateByItemCode(brandCode, itemCode );
		if(null != orderDate){
			Calendar cal = Calendar.getInstance();
			cal.setTime(orderDate);
			if("T1CO".equals(brandCode))
				cal.add(Calendar.MONTH, -1);
			barcode.setDate(cal.get(Calendar.YEAR) + "." + String.format("%02d", cal.get(Calendar.MONTH) + 1 ));
		}

		boolean exist = false;
		SortedMap<String, Barcode> map = new TreeMap();
		for (Barcode cbarCode : barcodes) {
		    map.put(cbarCode.getItemCode(), cbarCode);
		    if (cbarCode.getItemCode().equalsIgnoreCase(
			    barcode.getItemCode())) {
			exist = true;
			break;
		    }
		}

		if (!exist) {
		    map.put(barcode.getItemCode(), barcode);
		    List list = Arrays.asList(map.values().toArray());
		    barcodes = new ArrayList(list);
		    // barcodes.add(barcode);
		}

	    }
	}
	return barcodes;
    }

    /**
     * 設定明細其他欄位
     * @param imItemSerials
     */
    public void setOtherColumn(List<ImItemSerial> imItemSerials) {
	for (ImItemSerial imItemSerial : imItemSerials) {
	    imItemSerial.setIsUsedName("N".equalsIgnoreCase(imItemSerial.getIsUsed()) ? "是" : "否");
	}
    }

    /**
     * 初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception {
	log.info("============<executeInitial>===============");
	HashMap resultMap = new HashMap();
	Map multiList = new HashMap();
	ImItem imItem = null;
	try {
	    HashMap argumentMap = getRequestParameter(parameterMap, false);

	    Long formId = (Long) argumentMap.get("formId");
	    String loginBrandCode = (String) argumentMap.get("loginBrandCode");
	    String loginEmployeeCode = (String) argumentMap.get("loginEmployeeCode");
	    BuBrand buBrand = buBrandDAO.findById(loginBrandCode);

	    log.info("executeInitial formId = " + formId );
	    if (formId == null) {
		imItem = createNewImItem(argumentMap, resultMap);
	    } else {
		imItem = findImItem(argumentMap, resultMap, multiList);
	    }

	    // 判定是否鎖單價欄位
	    String enablePrice = "false";
	    // 判定是否鎖原幣成本
	    String isLockPurchaseAmount = "false";
	    List<ImItemPrice> imItemPrices = imItem.getImItemPrices();
	    for (ImItemPrice imItemPrice : imItemPrices) {
		if( imItemPrice.getItemId() != null ){
		    enablePrice = "true";
		}
		if("Y".equals(imItemPrice.getEnable())){
		    isLockPurchaseAmount = "true";
		    break;
		}
	    }

	    log.info("imItem.getItemCName() = " + imItem.getItemCName() );
	    log.info("imItem.getItemEName() = " + imItem.getItemEName() );
	    log.info("isLockPurchaseAmount = " + isLockPurchaseAmount);

	    ImItemCategory category = imItemCategoryDAO.findByCategoryCode(loginBrandCode, ImItemCategoryDAO.ITEM_BRAND, imItem.getItemBrand(), "Y" );

	    resultMap.put("itemBrandName", AjaxUtils.getPropertiesValue( category == null ? "查無此類別" : category.getCategoryName(), "" ));
	    resultMap.put("isLockPurchaseAmount", isLockPurchaseAmount);
	    resultMap.put("enablePrice", enablePrice );
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(imItem.getLastUpdatedBy()));
	    BuEmployee buEmployee = buEmployeeDAO.findById(loginEmployeeCode);
	    resultMap.put("department",buEmployee.getEmployeeDepartment());

	    resultMap.put("category07", "A"); // default 中性
	    resultMap.put("itemCategoryBack", imItem.getItemCategory()); // 業種子類備用

	    String suppilerCode = imItem.getCategory17();
	    BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewService.findByBrandCodeAndSupplierCode(loginBrandCode, suppilerCode);
	    resultMap.put("supplierName", AjaxUtils.getPropertiesValue( suppilerCode == null ? "" : (buSupplierWithAddressView == null ? "查無此廠商" : buSupplierWithAddressView.getChineseName()), "" )); // 廠商名稱
	    resultMap.put("branch",buBrand.getBuBranch().getBranchCode());
	    resultMap.put("form", imItem);

	    // 取得下拉
	    getInitData(multiList, resultMap, loginBrandCode, imItem.getCategoryType());

	    resultMap.put("multiList", multiList);
	    log.info("============</executeInitial>===============");
	    return resultMap;
	} catch (Exception ex) {
		ex.printStackTrace();
	    log.error("商品主檔初始化失敗，原因：" + ex.toString());
	    throw new Exception("商品主檔初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSerialInitial(Map parameterMap) throws Exception {
	log.info("============<executeSerialInitial>===============");
	HashMap resultMap = new HashMap();
	Map multiList = new HashMap();
	ImItem imItem = null;
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String formId = (String) PropertyUtils.getProperty(otherBean, "formId");
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
	    String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");

	    log.info("formId = " + formId );
	    if (formId == null) {
		throw new Exception("formId參數為空值，無法執行初始化！");
	    } else {
		imItem = imItemDAO.findByItemId(NumberUtils.getLong(formId));
	    }

	    resultMap.put("form", imItem);
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));

	    resultMap.put("multiList", multiList);
	    log.info("============</executeSerialInitial>===============");
	    return resultMap;
	} catch (Exception ex) {
	    log.error("商品序號初始化失敗，原因：" + ex.toString());
	    throw new Exception("商品序號初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSerialSearchInitial(Map parameterMap) throws Exception {
	log.info("============<executeSerialSearchInitial>===============");
	HashMap resultMap = new HashMap();
	try {
//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String itemCode = (String) PropertyUtils.getProperty(otherBean, "itemCode");
	    String isUsed = (String) PropertyUtils.getProperty(otherBean,"isUsed");
	    String serial = (String) PropertyUtils.getProperty(otherBean,"serial");

	    resultMap.put("itemCode", itemCode);
	    resultMap.put("isUsed", isUsed);
	    resultMap.put("serial", serial);

	    log.info("============</executeSerialSearchInitial>===============");
	    return resultMap;
	} catch (Exception ex) {
	    log.error("商品序號查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("商品序號查詢初始化失敗，原因：" + ex.toString());
	}
    }

    /**
     * 商品國際碼查詢初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeEanCodeSearchInitial(Map parameterMap) throws Exception {
	log.info("============<executeEanCodeSearchInitial>===============");
	HashMap resultMap = new HashMap();
	try {
	    Calendar calendar = Calendar.getInstance();

//	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
	    String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
	    String eanCode = (String) PropertyUtils.getProperty(otherBean, "eanCode");
	    String formId = (String) PropertyUtils.getProperty(otherBean, "formId");

	    log.info("loginEmployeeCode = " + loginEmployeeCode);
	    log.info("loginBrandCode = " + loginBrandCode);
	    log.info("eanCode = " + eanCode);
	    log.info("formId = " + formId);

	    resultMap.put("brandCode", loginBrandCode);
	    resultMap.put("itemCode", formId);
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());

	    // 依brandCode和 itemCode
	    ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findOneCurrentPriceByProperty(loginBrandCode, "1", formId);
	    if(null != imItemCurrentPriceView ){

		// 其他欄位 \稅別\商品品牌\中類\售價\
		Double unitPrice = imItemCurrentPriceView.getUnitPrice();
		String taxType = imItemCurrentPriceView.getIsTax();
		String itemBrand = imItemCurrentPriceView.getItemBrand();
		String category02 = imItemCurrentPriceView.getCategory02();

		resultMap.put("unitPrice", unitPrice);
		resultMap.put("taxType", taxType);
		resultMap.put("itemBrand", itemBrand);
		resultMap.put("category02", category02);

		// 品牌 品號 國際碼
		ImItemEancode imItemEancode = imItemEancodeDAO.findEanCodeByProperty(loginBrandCode, formId, eanCode);
		if( null != imItemEancode ){

		    resultMap.put("enable", imItemEancode.getEnable());
		    resultMap.put("eanCode", imItemEancode.getEanCode());
		    resultMap.put("createdBy", imItemEancode.getCreatedBy());
		    if(imItemEancode.getCreationDate()!=null){
		    	calendar.setTime((Date)imItemEancode.getCreationDate());
			    resultMap.put("creationDate", calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONDAY)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH));
			}
		    resultMap.put("lastUpdatedBy", imItemEancode.getLastUpdatedBy());
		    if(imItemEancode.getLastUpdateDate()!=null){
		    	calendar.setTime((Date)imItemEancode.getLastUpdateDate());
			    resultMap.put("lastUpdateDate", calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONDAY)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH));
		    }
		    resultMap.put("createByName", UserUtils.getUsernameByEmployeeCode(imItemEancode.getCreatedBy()));
		    resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(imItemEancode.getLastUpdatedBy()));
		}else{
		    log.info("查品牌:"+loginBrandCode + " 品號:"+formId+ " 國際碼:"+ eanCode+" 無資料");
		}

		// 商品品牌
		ImItemCategory imItemCategoryItemBrand = imItemCategoryDAO.findByCategoryCode(loginBrandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y");
		if(null != imItemCategoryItemBrand){
		    String itemBrandName = imItemCategoryItemBrand.getCategoryName();
		    resultMap.put("itemBrandName", itemBrandName);
		}else{
		    throw new Exception("查品牌:"+loginBrandCode + " 品號:"+formId+ " 商品品牌:"+ itemBrand+" 無資料");
		}

		// 中類
		ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(loginBrandCode, ImItemCategoryDAO.CATEGORY02, category02, "Y");
		if(null != imItemCategory02){
		    String category02Name = imItemCategory02.getCategoryName();
		    resultMap.put("category02Name", category02Name);
		}else{
		    throw new Exception("查品牌:"+loginBrandCode + " 品號:"+formId+ " 中類:"+ category02+" 無資料");
		}

	    }else{
		if(StringUtils.hasText(formId)){
		    ImItemPriceView imItemPriceView = imItemPriceViewDAO.findOneItemPriceView(loginBrandCode, "1", formId);
		    Double unitPrice = null;
		    String taxType = null;
		    String itemBrand = null;
		    String category02 = null;
		    if(null != imItemPriceView){
			unitPrice = imItemPriceView.getUnitPrice();
			taxType = imItemPriceView.getIsTax();
			itemBrand = imItemPriceView.getItemBrand();
			category02 = imItemPriceView.getCategory02();

			resultMap.put("unitPrice", unitPrice);
			resultMap.put("taxType", taxType);
			resultMap.put("itemBrand", itemBrand);
			resultMap.put("category02", category02);
		    }else{
			log.info("查品牌:"+loginBrandCode + " 品號:"+formId+ "無資料");
		    }

		    // 品牌 品號 國際碼
		    ImItemEancode imItemEancode = imItemEancodeDAO.findEanCodeByProperty(loginBrandCode, formId, eanCode);
		    if( null != imItemEancode ){
			resultMap.put("enable", imItemEancode.getEnable());
			resultMap.put("eanCode", imItemEancode.getEanCode());
			resultMap.put("createdBy", imItemEancode.getCreatedBy());
			calendar.setTime((Date)imItemEancode.getCreationDate());
			resultMap.put("creationDate", calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONDAY)+1+"/"+calendar.get(Calendar.DAY_OF_MONTH));
			resultMap.put("lastUpdatedBy", imItemEancode.getLastUpdatedBy());
			calendar.setTime((Date)imItemEancode.getLastUpdateDate());
			resultMap.put("lastUpdateDate", calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONDAY)+1+"/"+calendar.get(Calendar.DAY_OF_MONTH));

			resultMap.put("createByName", UserUtils.getUsernameByEmployeeCode(imItemEancode.getCreatedBy()));
			resultMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(imItemEancode.getLastUpdatedBy()));
		    }else{
			log.info("查品牌:"+loginBrandCode + " 品號:"+formId+ " 國際碼:"+ eanCode+" 無資料");
		    }

		    // 商品品牌
		    if(null != itemBrand){
			ImItemCategory imItemCategoryItemBrand = imItemCategoryDAO.findByCategoryCode(loginBrandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y");
			if(null != imItemCategoryItemBrand){
			    String itemBrandName = imItemCategoryItemBrand.getCategoryName();
			    resultMap.put("itemBrandName", itemBrandName);
			}else{
			    throw new Exception("查品牌:"+loginBrandCode + " 品號:"+formId+ " 商品品牌:"+ itemBrand+" 無資料");
			}
		    }

		    // 中類
		    if(null == category02){
			ImItemCategory imItemCategory02 = imItemCategoryDAO.findByCategoryCode(loginBrandCode, ImItemCategoryDAO.CATEGORY02, category02, "Y");
			if(null != imItemCategory02){
			    String category02Name = imItemCategory02.getCategoryName();
			    resultMap.put("category02Name", category02Name);
			}else{
			    throw new Exception("查品牌:"+loginBrandCode + " 品號:"+formId+ " 中類:"+ category02+" 無資料");
			}
		    }
		}
	    }

	    log.info("============</executeEanCodeSearchInitial>===============");
	    return resultMap;
	} catch (Exception ex) {
	    log.error(Imformation.valueOf("EANCODE").getServiceSearchInitial() + ex.toString());
	    throw new Exception(Imformation.valueOf("EANCODE").getServiceSearchInitial() + ex.toString());
	}
    }

    /**
     * 匯入商品序號
     *
     * @param headId
     * @param priceLists
     * @throws Exception
     */
    public void executeImportSerial(Long itemId, String loginEmployeeCode, List imItemSerials) throws Exception{
	Date date = new Date();
	Long indexNo = 0L;
	Long indexNoTmp = null;
	try{
	    ImItem imItem = findByItemId(itemId);
	    if(imItem == null){
		throw new NoSuchObjectException("查無商品主鍵：" + itemId + "的資料");
	    }

	    log.info("itemId = " + itemId);
	    log.info("brandCode = " + imItem.getBrandCode());
	    log.info("itemCode = " + imItem.getItemCode());

	    List<ImItemSerial> lines = imItem.getImItemSerials();
	    if(lines!= null && lines.size() > 0 ){
		indexNo = lines.get(lines.size()-1).getIndexNo();
	    }

	    if(imItemSerials != null && imItemSerials.size() > 0){
		for(int i = 0; i < imItemSerials.size(); i++){
		    ImItemSerial  imItemSerial = (ImItemSerial)imItemSerials.get(i);

		    indexNoTmp = indexNo + 1L + i;
		    log.info("serial = " + imItemSerial.getSerial());
		    log.info("indexNo = " + indexNoTmp);
		    ImItemSerial imItemSerialCheck = (ImItemSerial)imItemSerialDAO.findFirstByProperty("ImItemSerial", "and itemId = ? and serial = ?", new Object[]{ itemId, imItemSerial.getSerial()});
		    if( imItemSerialCheck == null ){

			imItemSerial.setItemId(itemId);
			imItemSerial.setBrandCode(imItem.getBrandCode());
			imItemSerial.setItemCode(imItem.getItemCode());
			imItemSerial.setIsUsed("N");
			imItemSerial.setIsDeleteRecord(AjaxUtils.IS_DELETE_RECORD_FALSE);
			imItemSerial.setIsLockRecord(AjaxUtils.IS_LOCK_RECORD_FALSE);

			imItemSerial.setCreatedBy(loginEmployeeCode);
			imItemSerial.setCreationDate(date);
			imItemSerial.setLastUpdatedBy(loginEmployeeCode);
			imItemSerial.setLastUpdateDate(date);

			imItemSerial.setIndexNo(indexNoTmp);
			imItemSerialDAO.save(imItemSerial);
		    }else{
			indexNo = indexNo - 1;
			log.error("serial:"+ imItemSerial.getSerial()+ "已重複");
		    }
		}
	    }
	}catch (Exception ex) {
	    log.error("商品序號明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("商品序號明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }


    /**
     * 初始化下拉
     * @param multiList
     * @param resultMap
     * @param loginBrandCode
     * @throws Exception
     */
    public void getInitData(Map multiList, HashMap resultMap, String loginBrandCode, String categoryType)
    throws Exception {
	List<BuCommonPhraseLine> allItemCategory20 = buCommonPhraseService
	.getCommonPhraseLinesById("ItemCategory20", false);

	List<BuCommonPhraseLine> allItemUnit = null;
	if(loginBrandCode.indexOf("T2") > -1){
	    allItemUnit = buCommonPhraseLineDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and enable = ? ", new Object[]{"T2ItemUnit", "Y"} );
	}else{
	    allItemUnit = buCommonPhraseLineDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and enable = ? ", new Object[]{"ItemUnit", "Y"} );
	}

	List<BuCommonPhraseLine> allPriceType = buCommonPhraseService
	.getCommonPhraseLinesById("PriceType", false);

	List<BuCommonPhraseLine> allLotControls = buCommonPhraseService.getCommonPhraseLinesById("LotControl", false);

	List<ImItemCategory> allCategoryType = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
	List<ImItemCategory> allCategory01 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY01");
	List<ImItemCategory> allCategory02 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY02");
	List<ImItemCategory> allCategory03 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY03");
	List<ImItemCategory> allCategory05 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY05");
	List<ImItemCategory> allCategory06 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY06");
	List<ImItemCategory> allCategory07 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY07");
	List<ImItemCategory> allCategory12 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY12");
	List<ImItemCategory> allCategory13 = this.imItemCategoryDAO
	.findByCategoryType(loginBrandCode, "CATEGORY13");
	multiList.put("allItemUnit", AjaxUtils.produceSelectorData(allItemUnit,
		"lineCode", "name", true, false));
	multiList.put("allPriceType", AjaxUtils.produceSelectorData(
		allPriceType, "lineCode", "name", false, false));

	HashMap allItemCategory = (HashMap) buCommonPhraseService
	.findCommonPhraseLinesById("ItemCategory");
	for (int i = 1; i <= 20; i++) {
	    if (i < 10) {
		multiList.put("category0" + i + "CName", allItemCategory
			.get("CATEGORY0" + i));
	    } else {
		multiList.put("category" + i + "CName", allItemCategory
			.get("CATEGORY" + i));
	    }
	}

	multiList.put("allCategoryType", AjaxUtils.produceSelectorData(
		allCategoryType, "categoryCode", "categoryName", false, false));
	multiList.put("allCategory01", AjaxUtils.produceSelectorData(
		allCategory01, "categoryCode", "categoryName", true, true));
	multiList.put("allCategory02", AjaxUtils.produceSelectorData(
		allCategory02, "categoryCode", "categoryName", true, true));
	multiList.put("allCategory03", AjaxUtils.produceSelectorData(
		allCategory03, "categoryCode", "categoryName", true, true));
	multiList.put("allCategory05", AjaxUtils.produceSelectorData(
		allCategory05, "categoryCode", "categoryName", false, true));
	multiList.put("allCategory06", AjaxUtils.produceSelectorData(
		allCategory06, "categoryCode", "categoryName", false, true));
	multiList.put("allCategory07", AjaxUtils.produceSelectorData(
		allCategory07, "categoryCode", "categoryName", false, true));
	multiList.put("allCategory12", AjaxUtils.produceSelectorData(
		allCategory12, "categoryCode", "categoryName", false, true));
	multiList.put("allCategory13", AjaxUtils.produceSelectorData(
		allCategory13, "categoryCode", "categoryName", true, true));
	multiList.put("allCategory20", AjaxUtils.produceSelectorData(
		allItemCategory20, "lineCode", "name", false, false));

	multiList.put("allLotControls"	, AjaxUtils.produceSelectorData(allLotControls, "lineCode", "name", true, true, "N"));

	// 一般下拉
	List<ImItemCategory> allCategoryTypes = imItemCategoryDAO.findCategoryByBrandCode(loginBrandCode, "CATEGORY00", "Y");
//	List<BuCurrency> allCurrencyCodes = buCurrencyDAO.findByProperty("BuCurrency", "enable", "Y");
	List<BuCurrency> allCurrencyCodes = buBasicDataService.findCurrencyList("", "", "");
	List<BuCommonPhraseLine> allItemTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"ItemType", "Y"}, "indexNo" );
	List<BuCommonPhraseLine> allBonusTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"BonusType", "Y"}, "indexNo" );
	List<BuCommonPhraseLine> allVipDiscounts = baseDAO.findByProperty("BuCommonPhraseLine", new String[]{ "id.buCommonPhraseHead.headCode", "enable"}, new Object[]{"VipDiscount", "Y"}, "indexNo" );

	multiList.put("allCategoryTypes"	, AjaxUtils.produceSelectorData(allCategoryTypes, "categoryCode", "categoryName", true, true));
	multiList.put("allCurrencyCodes"	, AjaxUtils.produceSelectorData(allCurrencyCodes, "currencyCode", "currencyCName", true, true));
	multiList.put("allItemTypes"	, AjaxUtils.produceSelectorData(allItemTypes, "lineCode", "name", true, true));
	multiList.put("allBonusTypes"	, AjaxUtils.produceSelectorData(allBonusTypes, "lineCode", "name", true, true));
	multiList.put("allVipDiscounts"	, AjaxUtils.produceSelectorData(allVipDiscounts, "lineCode", "name", true, true));

	//是否撈出業種子類的下拉
	log.info("業種子類 categoryType = " + categoryType);
//	if( StringUtils.hasText(categoryType) ){

	List allItemSubcategory = imItemCategoryDAO.findByParentCategoryCode( loginBrandCode, ImItemCategoryDAO.ITEM_CATEGORY, categoryType, "Y" );
	multiList.put("allItemSubcategory"	, AjaxUtils.produceSelectorData(allItemSubcategory, "categoryCode", "categoryName", true, true));
//	}
    }

    // create default ImItem
    public ImItem createNewImItem(Map argumentMap, Map resultMap)
    throws Exception {

	try {
	    String loginEmployeeCode = (String) argumentMap.get("loginEmployeeCode");
	    String loginBrandCode = (String) argumentMap.get("loginBrandCode");

	    Date date = new Date();

	    ImItem form = new ImItem();
	    form.setBrandCode(loginBrandCode);
	    form.setCreatedBy(loginEmployeeCode);
	    form.setCreationDate(date);
	    form.setLastUpdateDate(date);

	    form.setSalesRatio(1L); 				// 庫存單位
	    form.setPurchaseRatio(0L); 				// 預計採購比
	    form.setForeignListPrice(0.0D); 		// 原幣零售價
	    form.setSupplierQuotationPrice(0.0D); 	// 原廠報價(原幣)
	    form.setStandardPurchaseCost(0.0D);		// 進貨成本(台幣)
	    form.setBoxCapacity(1L);				// 每箱數量
	    form.setAlcolhoPercent(0D);             // 酒精濃度
	    form.setEStore("N");					// 網購註記
	    if( loginBrandCode.indexOf("T2") <= -1 ){
		form.setItemType("N");
		form.setBudgetType("A");
		form.setCategoryType(loginBrandCode);
		form.setItemCategory(loginBrandCode);
	    }else{
		form.setItemBrand("AA");
		form.setReplenishCoefficient(1D);//補貨係數
		form.setDeclRatio(1D);//銷售單位/報關單號換算
		form.setMinRatio(1D);//最小單位/銷貨單位的比例換算
	    }
	    form.setLastUpdatedBy(loginEmployeeCode);
	    form.setCreatedBy(loginEmployeeCode);

	    resultMap.put("form", form);

	    return form;
	} catch (Exception ex) {
	    log.error("產生新商品主檔失敗，原因：" + ex.toString());
	    throw new Exception("產生新商品主檔發生錯誤！");
	}
    }

    public ImItem findImItem(Map argumentMap, Map resultMap, Map multiList)
    throws FormException, Exception {
	try {
	    log.info("findImItem(Map argumentMap, Map resultMap, Map multiList)");
	    Long formId = (Long) argumentMap.get("formId");
	    ImItem form = this.findByItemId(formId);
	    if (form != null) {
		log.info("判定是否鎖單價欄位");
		return form;
	    } else {
		throw new NoSuchObjectException("查無商品主檔主鍵：" + formId + "的資料！");
	    }
	} catch (FormException fe) {
	    log.error("查詢商品主檔單失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("查詢商品主檔單發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品主檔單發生錯誤！");
	}
    }

    private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction)
    throws Exception {

	Object otherBean = parameterMap.get("vatBeanOther");
	String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,
	"loginBrandCode");
	String loginEmployeeCode = (String) PropertyUtils.getProperty(
		otherBean, "loginEmployeeCode");
	String formIdString = (String) PropertyUtils.getProperty(otherBean,
	"formId");
	Long formId = StringUtils.hasText(formIdString) ? Long
		.valueOf(formIdString) : null;
		HashMap conditionMap = new HashMap();
		conditionMap.put("loginBrandCode", loginBrandCode);
		conditionMap.put("loginEmployeeCode", loginEmployeeCode);
		// conditionMap.put("orderTypeCode", orderTypeCode);
		conditionMap.put("formId", formId);
		if (isSubmitAction) {
		    String beforeChangeStatus = (String) PropertyUtils.getProperty(
			    otherBean, "beforeChangeStatus");
		    String formStatus = (String) PropertyUtils.getProperty(otherBean,
		    "formStatus");
		    conditionMap.put("beforeChangeStatus", beforeChangeStatus);
		    conditionMap.put("formStatus", formStatus);
		}

		return conditionMap;
    }

    /**
     * 商品主檔outline
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
	public Map updateAJAXImItem(Map parameterMap) throws FormException, Exception {

		HashMap resultMap = new HashMap();
		Date date = new Date();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			String itemCode = (String) PropertyUtils.getProperty(formBindBean, "itemCode");
			String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");
			String id = (String) PropertyUtils.getProperty(otherBean, "formId");
			String category06 = (String) PropertyUtils.getProperty(otherBean, "category06");

			long itemId = -1;
			if (StringUtils.hasText(id)) {
				itemId = NumberUtils.getLong(id);
			}
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formStatus = (String) PropertyUtils.getProperty(otherBean, "formAction");
			ImItem imItemPO;
			ImItemEancode imItemEancodePO;
			String resultMsg = "";

			log.info(" brandCode = " + brandCode);
			log.info(" itemCode = " + itemCode);

			if (itemId == -1) {
				imItemPO = imItemDAO.findItem(brandCode, itemCode);
				if (imItemPO != null) {
					throw new Exception("商品品號不可重複新增");
				}
				//modify add 多判斷也不能跟國際碼重覆
				imItemEancodePO = imItemEancodeDAO.findByBrandCodeAndEanCode(brandCode, itemCode);
				if (imItemEancodePO != null) {
					throw new Exception("商品品號不可跟國際碼重複");
				}
				imItemPO = new ImItem();
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemPO);
				// 檢核品號與稅別 by Weichun 2012.03.30
				if ("F".equals(imItemPO.getItemCode().substring(imItemPO.getItemCode().length() - 1))) {
					if (!imItemPO.getIsTax().equals("F"))
						throw new Exception("商品品號：" + imItemPO.getItemCode() + "與所選稅別：" + imItemPO.getIsTax()
								+ "(完稅)不符，請重新確認後再送出！");
				}
				if ("P".equals(imItemPO.getItemCode().substring(imItemPO.getItemCode().length() - 1))) {
					if (!imItemPO.getIsTax().equals("P"))
						throw new Exception("商品品號：" + imItemPO.getItemCode() + "與所選稅別：" + imItemPO.getIsTax()
								+ "(保稅)不符，請重新確認後再送出！");
				}
				imItemPO.setCreatedBy(employeeCode);
				imItemPO.setCreationDate(date);
				imItemPO.setStatus(formStatus);

				// 若T2A將直接定價
				// setPrice(imItemPO,brandCode);

				this.save(imItemPO, SAVE);
				
				resultMsg = "新增成功";
			} else {
				imItemPO = this.findByItemId(itemId);
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemPO);

				if (brandCode.indexOf("T2") <= -1 && "".equals(category06)) { // 特別指定清空季別
					imItemPO.setCategory06(category06);
				}
				imItemPO.setStatus(formStatus);
				imItemPO.setLastUpdateDate(date);
				imItemPO.setLastUpdatedBy(employeeCode);

				this.save(imItemPO, UPDATE);
				resultMsg = "修改成功";
			}
			this.getLevelDataMax(imItemPO); //計算階層
			resultMap.put("entityBean", imItemPO);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
		} catch (FormException fe) {
			log.error("商品主檔維護作業存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("商品主檔維護作業存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.getMessage());
		}
	}

    /**
     * 商品序號 outline
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public Map updateAJAXImItemSerial(Map parameterMap) throws FormException,Exception {

	HashMap resultMap = new HashMap();
	String resultMsg = "";
	long itemId = -1;
	try {
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
//	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String itemCode = (String) PropertyUtils.getProperty(formBindBean, "itemCode");
	    String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");

	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
	    String formId = (String) PropertyUtils.getProperty(otherBean, "formId");
	    String employeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");

	    log.info( " brandCode = " + brandCode );
	    log.info( " itemCode = " + itemCode );
	    log.info( " formId = " + formId );

	    if (StringUtils.hasText(formId)) {
		itemId = NumberUtils.getLong(formId);
	    }else{
		throw new ValidationErrorException("formId參數為空值，無法執行存檔！");
	    }

	    ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
	    if(null == imItem){
		throw new Exception("查品牌:"+ brandCode + " 品號:" + itemCode + "無此資料");
	    }else{
		// 檢核
		if( OrderStatus.FORM_SUBMIT.equals(formAction)){
		    // 刪除
		    deleteImItemSerial(imItem);
		    checkImItemSerial(imItem);
		}
		// 存檔
		updateImItemSerial(imItem, employeeCode);

		resultMsg = "品號:"+imItem.getItemCode()+"存檔成功！ 是否繼續修改？";
	    }

	    resultMap.put("entityBean", imItem);
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;
	} catch (Exception ex) {
	    log.error("商品序號維護作業存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}
    }

    /**
     * 存檔商品序號
     * @param head
     * @param employeeCode
     * @return
     * @throws FormException
     * @throws Exception
     */
    public void updateImItemSerial(ImItem head, String employeeCode) throws Exception {
	Date date = new Date();
	if (head.getItemId() != null) {
	    List<ImItemSerial> lines = head.getImItemSerials();
	    for (ImItemSerial line : lines) {
		line.setLastUpdateDate(date);
		line.setLastUpdatedBy(employeeCode);
	    }
	    imItemSerialDAO.update(head);
	}else{
	    throw new Exception("ItemId參數為空值，無法執行存檔！");
	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest)throws Exception {
	String brandCode = httpRequest.getProperty("brandCode");
	String errorMsg = null;
	String[] gridSearchFieldNames = null;
	if(brandCode.indexOf("T2") > -1){
	    gridSearchFieldNames = T2_GRID_SEARCH_FIELD_NAMES;
	}else{
	    gridSearchFieldNames = T1_GRID_SEARCH_FIELD_NAMES;
	}
	AjaxUtils.updateSearchResult(httpRequest, gridSearchFieldNames);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 國際碼用
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveEanCodeSearchResult(Properties httpRequest)throws Exception {
	String errorMsg = null;
	String[] gridSearchFieldNames = null;
	gridSearchFieldNames = T2_EANCODE_GRID_SEARCH_FIELD_NAMES;
	AjaxUtils.updateSearchResult(httpRequest, gridSearchFieldNames);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 商品序號
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSerialLineSearchResult(Properties httpRequest)throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, SERIAL_LINE_GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 商品序號
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSerialSearchResult(Properties httpRequest)throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, SERIAL_GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 設定商品主檔其他欄位
     * @param imItems
     */
    private void setOtherColumns(String brandCode, List<ImItem> imItems){
	for(ImItem imItem : imItems){
	    if("Y".equals(imItem.getEnable())){
		imItem.setEnableName("啟用");
	    }else{
		imItem.setEnableName("停用");
	    }

	    if(brandCode.indexOf("T2") > -1){
		String itemCode = imItem.getItemCode();
		String itemBrand = imItem.getItemBrand();
		String category17 = imItem.getCategory17();
		// 查售價
		List objs = (List)imItemCurrentPriceViewDAO.findByProperty("ImItemCurrentPriceView", "unitPrice, priceLastUpdateDate", "and brandCode = ? and itemCode = ? and typeCode = ?", new Object[]{brandCode, itemCode, "1"},"");
		log.info("brandCode = " +brandCode);
		log.info("itemCode = " +itemCode);
		log.info("objs = " +objs);
		if(null != objs && objs.size() > 0 ){
		    Object[] row = (Object[])objs.get(0);
		    Object unitPrice = row[0];
		    Date priceLastUpdateDate = (row[1] != null ? (Date)row[1] : null);
		    imItem.setUnitPrice(NumberUtils.getDouble(String.valueOf(unitPrice)));
		    imItem.setPriceLastUpdateDate(priceLastUpdateDate);
		}
		// 查商品品牌
		ImItemCategory imItemCategory = imItemCategoryDAO.findByCategoryCode( brandCode, ImItemCategoryDAO.ITEM_BRAND, itemBrand, "Y" );
		if(imItemCategory != null ){
		    imItem.setItemBrandName(imItemCategory.getCategoryName());
		}else{
		    imItem.setItemBrandName("查無此商品品牌");
		}
		// 查廠商名稱
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO.findByBrandCodeAndSupplierCode(brandCode, category17);
	        if(buSupplierWithAddressView != null){
	            imItem.setSupplierName(buSupplierWithAddressView.getChineseName());
	        }else{
	            imItem.setSupplierName("查無此供應商");
	        }

	    }
	}
    }

    /**
     * 若配置檔
     * @param imItemPO
     * @param brandCode
     */
    private void setPrice(ImItem imItemPO,String brandCode){
	BuCommonPhraseLine itemImportConfig = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine","", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and attribute1 = ? and enable = ? ", new Object[]{"ItemImportConfig",brandCode,"Y", "Y"}, "order by indexNo" );
	if(null != itemImportConfig){
	    List<ImItemPrice> imItemPrices = imItemPO.getImItemPrices();
	    if(null != imItemPrices && imItemPrices.size() > 0){
		ImItemPrice price = imItemPrices.get(0);
		if(null == price.getBeginDate() && "N".equalsIgnoreCase(price.getEnable())){
		    price.setBeginDate(new Date());
		    price.setEnable("Y");
		    log.info("NumberUtils.getDouble(itemImportConfig.getParameter1()) = " + NumberUtils.getDouble(itemImportConfig.getParameter1()));
		    price.setUnitPrice(NumberUtils.getDouble(itemImportConfig.getParameter1())); // 取得直接定價的價錢
		    List<ImItemPrice> priceTmps = new ArrayList();
		    priceTmps.add(price);
		    imItemPO.setImItemPrices(priceTmps);
		}
	    }
	}
    }

    /**
     * 取得商品資料 by 條碼
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXItemCode(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	String brandCode = null;
    	String itemCode = null;
    	Map findObjs = new HashMap();
    	try {
    		brandCode = httpRequest.getProperty("brandCode");
    		itemCode = httpRequest.getProperty("itemCode");
    		itemCode = itemCode.trim().toUpperCase();
    		String priceType = httpRequest.getProperty("priceType");
    		String category01 = httpRequest.getProperty("category01");
    		String cateogry02 = httpRequest.getProperty("cateogry02");

    		if( itemCode != ""){
    			properties.setProperty("itemCode", itemCode);

    			findObjs.put(" and brandCode = :brandCode",brandCode);
    			findObjs.put(" and itemCode = :itemCode",itemCode);
    			findObjs.put(" and typeCode = :priceType",priceType);

    			if(StringUtils.hasText(category01)){
    				findObjs.put(" and category01 = :category01",category01);
    			}
    			if(StringUtils.hasText(cateogry02)){
    				findObjs.put(" and cateogry02 = :cateogry02",cateogry02);
    			}

    			Map searchMap = imItemCurrentPriceViewDAO.search( "ImItemCurrentPriceView", findObjs, -1, -1, BaseDAO.QUERY_SELECT_ALL );
    			List<ImItemCurrentPriceView> imItemCurrentPriceViews = (List<ImItemCurrentPriceView>) searchMap.get(BaseDAO.TABLE_LIST);

    			if(imItemCurrentPriceViews != null && imItemCurrentPriceViews.size() > 0 ){
    				ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViews.get(0);
    				properties.setProperty("itemCode", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCode(), ""));
    				properties.setProperty("unitPrice", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getUnitPrice().longValue(), ""));
    				properties.setProperty("itemCName", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getItemCName(), ""));
    				properties.setProperty("paper", "1");
//COVER_BY_MACO    
    				//properties.setProperty("beginDate", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getBeginDate(), ""));
    				properties.setProperty("description", AjaxUtils.getPropertiesValue( imItemCurrentPriceView.getDescription(), ""));
    				ImItem imItem = (ImItem)baseDAO.findFirstByProperty("ImItem", "and brandCode = ? and itemCode = ?", new Object[]{imItemCurrentPriceView.getBrandCode(), imItemCurrentPriceView.getItemCode()});
    				properties.setProperty("itemId", imItem.getItemId().toString());
    			} else {
    				properties.setProperty("itemCName", MessageStatus.DATA_NOT_FOUND);
    				properties.setProperty("unitPrice", "0");
    				properties.setProperty("paper", "0");
    				//properties.setProperty("beginDate", "");
    				properties.setProperty("description", "");
    				properties.setProperty("itemId", "-1");
    			}
    		}
    		result.add(properties);
    		return result;
    	} catch (Exception ex) {
    		log.error("依據品牌代號：" + brandCode + "、品號：" + itemCode + "查詢時發生錯誤，原因：" + ex.toString());
    		throw new Exception("查詢品號資料失敗！");
    	}
    }

    /**
     * 取得商品國際碼資料 by 條碼
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public  List<Properties> getAJAXEanCode(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	String brandCode = null;
    	String eanCodeCondition = null;
    	StringBuffer sb = new StringBuffer();
    	Map key = new HashMap();
    	try {
    		brandCode = httpRequest.getProperty("brandCode");
    		eanCodeCondition = httpRequest.getProperty("eanCodeCondition");

    		if( eanCodeCondition != ""){ // 輸入的國際碼值
    			properties.setProperty("eanCodeCondition", eanCodeCondition);

    			// 查詢國際碼
    			List<ImItemEancode> imItemEancodes = imItemEancodeDAO.findByProperty("ImItemEancode", "and brandCode = ? and (itemCode = ? or eanCode = ?) ", new Object[]{brandCode, eanCodeCondition, eanCodeCondition});
    			// 將查到多筆的部份封裝成result, 先回傳第一筆itemCodeBack

    			if( null != imItemEancodes   && imItemEancodes.size() > 0 ){
    				for (ImItemEancode imItemEancode : imItemEancodes) {
    					String itemCode = imItemEancode.getItemCode();
    					String eanCode = imItemEancode.getEanCode();
    					sb.append(itemCode).append(AjaxUtils.SEARCH_KEY_DELIMITER).append(eanCode).append(AjaxUtils.GRID_ROW_SPLIT);
    					key.put(itemCode, eanCode);
    				}
    				log.info("查國際碼的所有資料 = " + sb);
    			}else{
    				log.info("查有無品號");
    				// 查詢品號
    				// 若查不到國際碼則查詢商品品號和此品號的國際碼
    				//modify 2012.07.04 查無國際碼時去查有無建商品品號
    				ImItem imItem = imItemDAO.findItem(brandCode, eanCodeCondition); // 以eanCode 代 itemCode
    				
    				if(null != imItem ){
    					String itemCode = imItem.getItemCode();
						String eanCode = "";
						sb.append(itemCode).append(AjaxUtils.SEARCH_KEY_DELIMITER).append(eanCode).append(AjaxUtils.GRID_ROW_SPLIT);
//						if(!key.containsKey(itemCode)){
//							log.info(" key.get(itemCode) = " + key.get(itemCode));
//							String eanCodeValue =(key.get(itemCode) !=null) ? (String)key.get(itemCode) : "null";
//							log.info(" eanCodeValue = " + eanCodeValue);
//							log.info(" eanCodeValue.equalsIgnoreCase(eanCode) = " + eanCodeValue.equalsIgnoreCase(eanCode));
//							if(!eanCodeValue.equalsIgnoreCase(eanCode)){
//								log.info(" 1 = 1");
//								sb.append(itemCode).append(AjaxUtils.SEARCH_KEY_DELIMITER).append(eanCode).append(AjaxUtils.GRID_ROW_SPLIT);
//								log.info(" 1 = 2");
//							}
//						}
    					//List<ImItemEancode> eancodes = imItem.getImItemEancodes();
//    					for (ImItemEancode imItemEancode2 : eancodes) {
//    						String itemCode = imItemEancode2.getItemCode();
//    						String eanCode = imItemEancode2.getEanCode();
//    						log.info(" itemCode = " + itemCode);
//    						log.info(" eanCode = " + eanCode);
//    						log.info(" key.containsKey(itemCode) = " + key.containsKey(itemCode));
//    						if(!key.containsKey(itemCode)){
//    							log.info(" key.get(itemCode) = " + key.get(itemCode));
//    							String eanCodeValue = (String)key.get(itemCode);
//    							log.info(" eanCodeValue = " + eanCodeValue);
//    							log.info(" eanCodeValue.equalsIgnoreCase(eanCode) = " + eanCodeValue.equalsIgnoreCase(eanCode));
//    							if(!eanCodeValue.equalsIgnoreCase(eanCode)){
//    								log.info(" 1 = 1");
//    								sb.append(itemCode).append(AjaxUtils.SEARCH_KEY_DELIMITER).append(eanCode).append(AjaxUtils.GRID_ROW_SPLIT);
//    								log.info(" 1 = 2");
//    							}
//    						}
//    					}
    				}
    			}
    			log.info(" 1 = 3");
    			log.info(" sb.length() = "+ sb.length());
    			if(sb.length() > 3 ){
    				sb.delete(sb.length()-3, sb.length());
    			}
    			log.info(" 所有品號 = " + sb);
    			properties.setProperty("keys", sb.toString());
    		}
    		result.add(properties);

    		return result;
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("依據品牌代號：" + brandCode + "、國際碼：" + eanCodeCondition + "查詢時發生錯誤，原因：" + ex.toString());
    		throw new Exception("查詢國際碼資料失敗！");
    	}
    }

    public List<Properties> getSearchSelection(Map parameterMap)
    throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
	    log.info("getSearchSelection.parameterMap:"
		    + parameterMap.keySet().toString());
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,
		    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
		    pickerBean, AjaxUtils.SEARCH_KEY);
	    log.info("getSearchSelection.picker_parameter:" + timeScope + "/"
		    + searchKeys.toString());

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
		    searchKeys);
	    log.info("getSearchSelection.result:" + result.size());
	    if (result.size() > 0)
		pickerResult.put("result", result);
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

    /**
     * 商品序號
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSerialSearchSelection(Map parameterMap)throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
	    log.info("getSearchSelection.parameterMap:"+ parameterMap.keySet().toString());
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
	    log.info("getSearchSelection.picker_parameter:" + timeScope + "/"+ searchKeys.toString());

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,searchKeys);
	    log.info("getSearchSelection.result:" + result.size());
	    if (result.size() > 0)
		pickerResult.put("serialResult", result);
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

    /**
     * 查詢
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		String[] gridSearchFieldNames = null;
		String[] gridSearchFieldDefaultValues = null;
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
			String startItemCode = httpRequest.getProperty("startItemCode");
			String endItemCode = httpRequest.getProperty("endItemCode");
			String enable = httpRequest.getProperty("enable");
			String brandCode = httpRequest.getProperty("brandCode");
			String itemName = httpRequest.getProperty("itemName");

			if (brandCode.indexOf("T2") > -1) {
				gridSearchFieldNames = T2_GRID_SEARCH_FIELD_NAMES;
				gridSearchFieldDefaultValues = T2_GRID_SEARCH_FIELD_DEFAULT_VALUES;
			} else {
				gridSearchFieldNames = T1_GRID_SEARCH_FIELD_NAMES;
				gridSearchFieldDefaultValues = T1_GRID_SEARCH_FIELD_DEFAULT_VALUES;
			}

			String supplierItemCode = httpRequest.getProperty("supplierItemCode");
			String itemLevel = httpRequest.getProperty("itemLevel");
			String category01 = httpRequest.getProperty("category01");
			String category02 = httpRequest.getProperty("category02");
			String category03 = httpRequest.getProperty("category03");

			String category13 = httpRequest.getProperty("category13"); // 系列
			String category17 = httpRequest.getProperty("category17"); // 製造商

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			// findObjs.put(" and ITEM_CODE >= :startItemCode", startItemCode);
			// findObjs.put(" and ITEM_CODE <= :endItemCode", endItemCode);
			findObjs.put(" and ENABLE = :enable", enable);
			findObjs.put(" and ITEM_LEVEL = :itemLevel", itemLevel);

			findObjs.put(" and CATEGORY01 = :category01", category01);
			findObjs.put(" and CATEGORY02 = :category02", category02);
			findObjs.put(" and CATEGORY03 = :category03", category03);

			StringBuffer newCodes = new StringBuffer();
			String itemCodes = httpRequest.getProperty("itemCodes");
			if (StringUtils.hasText(itemCodes)) { // 以多品號為主
				String[] codes = itemCodes.split(",");
				/* 修改 SQL語法 in 的程式撰寫方式 by Weichun 2011.11.14
				for (String code : codes) {
					newCodes.append("'").append(code).append("'").append(",");
				}
				if (codes.length > 0) {
					newCodes.delete(newCodes.length() - 1, newCodes.length());
				}
				newCodes.insert(0, " and ITEM_CODE in (").append(") ");
				*/
				findObjs.put(" and ITEM_CODE in (:codes)", codes);
			} else {
				findObjs.put(" and ITEM_CODE like :startItemCode", "%" + startItemCode + "%");
			}

			if (brandCode.indexOf("T2") > -1) {
				// 商品品牌
				String itemBrand = httpRequest.getProperty("itemBrand");
				String category05 = httpRequest.getProperty("category05");
				String category06 = httpRequest.getProperty("category06");
				String itemEName = httpRequest.getProperty("itemEName");
				String category11 = httpRequest.getProperty("category11");
				String foreignCategory = httpRequest.getProperty("foreignCategory");

				findObjs.put(" and ITEM_E_NAME = :itemEName", itemEName);
				findObjs.put(" and CATEGORY11 like :category11", "%" + category11 + "%");
				findObjs.put(" and FOREIGN_CATEGORY = :foreignCategory", foreignCategory);
				findObjs.put(" and ITEM_C_NAME like :itemName", "%" + itemName + "%");
				findObjs.put(" and SUPPLIER_ITEM_CODE like :supplierItemCode", "%" + supplierItemCode + "%");
				findObjs.put(" and ITEM_BRAND = :itemBrand", itemBrand);
				findObjs.put(" and CATEGORY05 = :category05", category05);
				findObjs.put(" and CATEGORY06 = :category06", category06);
				findObjs.put(" and CATEGORY13 like :category13", "%" + category13 + "%");
				findObjs.put(" and CATEGORY17 like :category17", "%" + category17 + "%");
			} else {
				// findObjs.put(" and ITEM_CODE like :endItemCode","%"+endItemCode+"%");
				findObjs.put(" and ITEM_C_NAME = :itemName", itemName);
				findObjs.put(" and SUPPLIER_ITEM_CODE = :supplierItemCode", supplierItemCode);
				findObjs.put(" and CATEGORY13 = :category13", category13);
				findObjs.put(" and CATEGORY17 = :category17", category17);
			}
			findObjs.put(newCodes.toString() + " and BRAND_CODE = :brandCode", brandCode);
			// ==============================================================
			Map imItemMap = imItemDAO.search("ImItem", findObjs, "order by itemCode", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<ImItem> imItems = (List<ImItem>) imItemMap.get(BaseDAO.TABLE_LIST);
			// log.info("cmDeclaration.size" + cmDeclaration.size());
			if (imItems != null && imItems.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imItemDAO.search("ImItem", "count(ITEM_CODE) as rowCount", findObjs,
						"order by itemCode", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
																															// INDEX

				setOtherColumns(brandCode, imItems);

				result.add(AjaxUtils.getAJAXPageData(httpRequest, gridSearchFieldNames, gridSearchFieldDefaultValues,
						imItems, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, gridSearchFieldNames, gridSearchFieldDefaultValues,
						map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}

	}

    /**
     * 查詢 國際碼
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXEanCodeSearchPageData(Properties httpRequest) throws Exception {
	String[] gridSearchFieldNames = null;
	String[] gridSearchFieldDefaultValues = null;
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小


	    gridSearchFieldNames = T2_EANCODE_GRID_SEARCH_FIELD_NAMES;
	    gridSearchFieldDefaultValues = T2_EANCODE_GRID_SEARCH_FIELD_DEFAULT_VALUES;

	    Map map = getSearchSQL(httpRequest);

	    String sql = (String)map.get("sql");
	    String sqlMax = (String)map.get("sqlMax");

	    // ==============================================================
	    List results = nativeQueryDAO.executeNativeSql(sql, iSPage, iPSize);

	    if (null != results && results.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // NumberUtils.getLong(((Object[])results.get(0))[0].toString());
		log.info(" firstIndex =  " + firstIndex);
		// 取得最後一筆 INDEX
		log.info(" sqlMax.toString() =  " + sqlMax.toString());
		List maxResult = nativeQueryDAO.executeNativeSql(sqlMax);
		Long maxIndex = NumberUtils.getLong( ((Object) maxResult.get(maxResult.size()-1)).toString() );
		log.info(" maxIndex =  " + maxIndex);
		result.add(AjaxUtils.getAJAXJoinTablePageData(httpRequest, gridSearchFieldNames, gridSearchFieldDefaultValues, results, gridDatas,
			firstIndex, maxIndex, null));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, gridSearchFieldNames, gridSearchFieldDefaultValues, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的選單功能查詢失敗！");
	}

    }

    /**
     * sql 匯出excel 國際碼
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public SelectDataInfo getAJAXEanCodeExportData(HttpServletRequest httpRequest) throws Exception{
	Object[] object = null;
	List rowData = new ArrayList();
	try {
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
	    String brandCode = properties.getProperty("brandCode");
	    Map map = getSearchSQL(properties);

	    String sql = (String)map.get("sql");

	    List lists = nativeQueryDAO.executeNativeSql(sql);

	    object = new Object[] {
		    "eanCode", 		"itemCode", 		"itemCName", 		"itemEName", 		"enableName",
		    "isTax", 	     	"category01",		"category01Name", 	"itemBrand", 		"itemBrandName",
		    "category02", 	"category02Name",	"lastUpdatedBy",	"lastUpdateDate",	"createdBy",
		    "creationDate"
	    };

	    log.info(" lists.size() = " + lists.size() );
	    for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
		Object[] getObj = (Object[])lists.get(i);
		Object[] dataObject = new Object[object.length];
		for (int j = 0; j < (object.length); j++) {
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
     * sql 匯出excel
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws Exception
     */
    public SelectDataInfo getAJAXExportData(HttpServletRequest httpRequest) throws Exception{
	StringBuffer sql = new StringBuffer();
	Object[] object = null;
	List rowData = new ArrayList();
	try {
	    String brandCode = httpRequest.getParameter("brandCode");
	    String startItemCode = httpRequest.getParameter("startItemCode");
	    String endItemCode = httpRequest.getParameter("endItemCode");
	    String enable = httpRequest.getParameter("enable");
	    String itemName = httpRequest.getParameter("itemName");

	    String supplierItemCode = httpRequest.getParameter("supplierItemCode");
	    String itemLevel = httpRequest.getParameter("itemLevel");
	    String category01 = httpRequest.getParameter("category01");
	    String category02 = httpRequest.getParameter("category02");
	    String category03 = httpRequest.getParameter("category03");

	    String category13 = httpRequest.getParameter("category13"); // 系列
	    String category17 = httpRequest.getParameter("category17"); // 製造商

	    log.info("brandCode = " + brandCode);
	    log.info("startItemCode = " + startItemCode);
	    log.info("endItemCode = " + endItemCode);
	    log.info("enable = " + enable);
	    log.info("itemName = " + itemName);
	    log.info("supplierItemCode = " + supplierItemCode);
	    log.info("itemLevel = " + itemLevel);
	    log.info("category01 = " + category01);
	    log.info("category02 = " + category02);
	    log.info("category03 = " + category03);
	    log.info("category13 = " + category13);
	    log.info("category17 = " + category17);

	    sql.append("SELECT I.ITEM_CODE, I.ITEM_C_NAME, I.ITEM_E_NAME, I.CATEGORY01, ")
	    .append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'CATEGORY01' ")
	    .append("AND I.CATEGORY01 = C.CATEGORY_CODE AND I.BRAND_CODE = C.BRAND_CODE) AS CATEGORY01_NAME, ")
	    .append("I.CATEGORY02, ")
	    .append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'CATEGORY02' ")
	    .append("AND I.CATEGORY02 = C.CATEGORY_CODE AND I.BRAND_CODE = C.BRAND_CODE) AS CATEGORY02_NAME, ")
	    .append("I.SUPPLIER_ITEM_CODE, I.ENABLE, '售價', I.SALES_UNIT, I.PURCHASE_UNIT, ")
	    .append("'最近調價日', I.ITEM_BRAND, ")
	    .append("(SELECT C.CATEGORY_NAME FROM IM_ITEM_CATEGORY C WHERE C.CATEGORY_TYPE = 'ItemBrand' ")
	    .append("AND I.ITEM_BRAND = C.CATEGORY_CODE ")
	    .append("AND I.BRAND_CODE = C.BRAND_CODE) AS ITEM_BRAND_NAME, ")
	    .append("I.CATEGORY17 AS SUPPLIER_CODE, A.CHINESE_NAME AS SUPPLIER_NAME, I.IS_TAX, ")
	    .append("I.ALCOLHO_PERCENT ")

	    .append("FROM IM_ITEM I, BU_ADDRESS_BOOK A, Bu_SUPPLIER S ")
	    .append("WHERE I.BRAND_CODE = S.BRAND_CODE(+) ")
	    .append("AND I.CATEGORY17 = S.SUPPLIER_CODE(+) ")
	    .append("AND S.ADDRESS_BOOK_ID = A.ADDRESS_BOOK_ID(+) ");

	    StringBuffer newCodes = new StringBuffer();
	    String itemCodes = httpRequest.getParameter("itemCodes");
	    if(StringUtils.hasText(itemCodes)){
		String[] codes = itemCodes.split(",");
		for (String code : codes) {
		    newCodes.append("'").append(code).append("'").append(",");
		}
		if(codes.length > 0){
		    newCodes.delete(newCodes.length()-1, newCodes.length());
		}
		newCodes.insert(0, "AND I.ITEM_CODE in (").append(") ");
		sql.append(newCodes);
	    }else{
		if(StringUtils.hasText(startItemCode)){
		    sql.append("AND I.ITEM_CODE like '%").append(startItemCode).append("%' ");
		}
	    }

	    if(brandCode.indexOf("T2") > -1){

		// 商品品牌
		String itemBrand = httpRequest.getParameter("itemBrand");
		String category05 = httpRequest.getParameter("category05");
		String category06 = httpRequest.getParameter("category06");
		String itemEName = httpRequest.getParameter("itemEName");
		String category11 = httpRequest.getParameter("category11");
		String foreignCategory = httpRequest.getParameter("foreignCategory");

		if(StringUtils.hasText(itemEName)){
		    sql.append("AND I.ITEM_E_NAME = '").append(itemEName).append("' ");
		}
		if(StringUtils.hasText(category11)){
		    sql.append("AND I.CATEGORY11 like '%").append(category11).append("%' ");
		}

		if(StringUtils.hasText(foreignCategory)){
		    sql.append("AND I.FOREIGN_CATEGORY = '").append(foreignCategory).append("' ");
		}

		if(StringUtils.hasText(itemName)){
		    sql.append("AND I.ITEM_C_NAME LIKE '%").append(itemName).append("%' ");
		}
		if(StringUtils.hasText(itemBrand)){
		    sql.append("AND I.ITEM_BRAND = '").append(itemBrand).append("' ");
		}

		if(StringUtils.hasText(category05)){
		    sql.append("AND I.CATEGORY05 = '").append(category05).append("' ");
		}
		if(StringUtils.hasText(category06)){
		    sql.append("AND I.CATEGORY06 = '").append(category06).append("' ");
		}
		if(StringUtils.hasText(category13)){
		    sql.append("AND I.CATEGORY13 like '%").append(category13).append("%' ");
		}
	    }else{
		if(StringUtils.hasText(itemName)){
		    sql.append("AND I.ITEM_C_NAME = '").append(itemName).append("' ");
		}
		if(StringUtils.hasText(category13)){
		    sql.append("AND I.CATEGORY13 like '%").append(category13).append("%' ");
		}
	    }

	    sql.append("AND I.BRAND_CODE = '").append(brandCode).append("' ");

	    if(StringUtils.hasText(endItemCode)){
		sql.append("AND I.ITEM_CODE like '%").append(endItemCode).append("%' ");
	    }

	    if(StringUtils.hasText(enable)){
		sql.append("AND I.ENABLE = '").append(enable).append("' ");
	    }

	    if(StringUtils.hasText(supplierItemCode)){
		sql.append("AND I.SUPPLIER_ITEM_CODE = '").append(supplierItemCode).append("' ");
	    }

	    if(StringUtils.hasText(category01)){
		sql.append("AND I.CATEGORY01 = '").append(category01).append("' ");
	    }

	    if(StringUtils.hasText(category02)){
		sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
	    }

	    if(StringUtils.hasText(category03)){
		sql.append("AND I.CATEGORY03 = '").append(category03).append("' ");
	    }

	    if(StringUtils.hasText(category17)){
		sql.append("AND I.CATEGORY17 = '").append(category17).append("' ");
	    }

	    sql.append("ORDER BY I.ITEM_CODE ");

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());

	    object = new Object[] { "itemCode", 	"itemCName", 		"itemEName", 		"category01", 	"category01Name",
		    		"category02", 	     	"category02Name",	"supplierItemCode", 	"enable", 	"unitPrice",
		    		"salesUnit", 		"purchaseUnit",		"priceLastUpdateDate",	"itemBrand",	"itemBrandName",
		    		"supplierCode", 	"supplierName",		"isTax",    "alcolhoPercent"
	    };

	    log.info(" lists.size() = " + lists.size() );
	    for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
		Object[] getObj = (Object[])lists.get(i);
		Object[] dataObject = new Object[object.length];
		Date priceLastUpdateDate = null;
		for (int j = 0; j < object.length; j++) {
		    if(j==9){ //額外塞售價
			String itemCode = String.valueOf(getObj[0]);
//			log.info(" itemCode = " +itemCode);
			List objs = (List)imItemCurrentPriceViewDAO.findByProperty("ImItemCurrentPriceView", "unitPrice, priceLastUpdateDate", "and brandCode = ? and itemCode = ? and typeCode = ?", new Object[]{brandCode, itemCode, "1"},"");
			if(null != objs && objs.size() > 0 ){
			    Object[] row = (Object[])objs.get(0);
			    Object unitPrice = row[0];
			    priceLastUpdateDate =  (row[1] != null ? (Date)row[1] : null);
//			    log.info(" unitPrice = " +unitPrice);
//			    log.info(" priceLastUpdateDate = " +priceLastUpdateDate);
			    dataObject[j] = NumberUtils.getDouble(String.valueOf(unitPrice));
			}
		    }else if(j == 12){ // 最近調價日
			if(null != priceLastUpdateDate){
			    dataObject[j] = priceLastUpdateDate;
			}
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
     * 查詢商品序號
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSerialSearchPageData(Properties httpRequest)throws Exception {
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("loginBrandCode");
	    String itemCode = httpRequest.getProperty("itemCode");
	    String isUsed = httpRequest.getProperty("isUsed");
	    String serial = httpRequest.getProperty("serial");
	    String isHead = httpRequest.getProperty("isHead");

	    String[] gridSearchFieldNames = null;
	    String[] gridSearchFieldDefaultValues = null;

	    if("true" == isHead){
		gridSearchFieldNames = SERIAL_GRID_SEARCH_FIELD_NAMES;
		gridSearchFieldDefaultValues = SERIAL_GRID_SEARCH_FIELD_DEFAULT_VALUES;
	    }else{
		gridSearchFieldNames = SERIAL_LINE_GRID_SEARCH_FIELD_NAMES;
		gridSearchFieldDefaultValues = SERIAL_LINE_GRID_SEARCH_FIELD_DEFAULT_VALUES;
	    }

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and BRAND_CODE = :brandCode", brandCode);
	    findObjs.put(" and ITEM_CODE like :itemCode","%"+itemCode+"%");
	    findObjs.put(" and IS_USED = :isUsed", isUsed);
	    findObjs.put(" and SERIAL = :serial", serial);

	    // ==============================================================
	    Map imItemSerialMap = imItemSerialDAO.search("ImItemSerial", findObjs, "order by indexNo,lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List<ImItemSerial> imItemSerials = (List<ImItemSerial>) imItemSerialMap.get(BaseDAO.TABLE_LIST);
	    if (imItemSerials != null && imItemSerials.size() > 0) {

		// 設定其他欄位
		setOtherColumn(imItemSerials);

		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = (Long) imItemSerialDAO.search("ImItemSerial",
			"count(ITEM_CODE) as rowCount", findObjs, iSPage,
			iPSize, BaseDAO.QUERY_RECORD_COUNT).get(
				BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			gridSearchFieldNames,
			gridSearchFieldDefaultValues, imItemSerials, gridDatas,
			firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			gridSearchFieldNames,
			gridSearchFieldDefaultValues, map, gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的功能查詢失敗！");
	}

    }

    /**
     * ajax 商品主檔載入時,取得規格及類別、標準售價分頁、組合商品、庫存資料、成本資料、T2其他欄位、國際碼、進貨紀錄資料 等資料
     *
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long itemId = NumberUtils.getLong(httpRequest.getProperty("itemId"));// 要顯示的ITEM_ID
			int tab = NumberUtils.getInt(httpRequest.getProperty("tab"));
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			log.info("iSPage = " + iSPage);
			log.info("iPSize = " + iPSize);
			log.info("tab = " + tab);
			log.info("itemId = " + itemId);
			// ======================帶入Head的值=========================
			HashMap map = new HashMap();
			map.put("itemId", itemId);
			map.put("startPage", iSPage);
			map.put("pageSize", iPSize);
			// ======================取得頁面所需資料===========================
			ImItem imItem = findByItemId(itemId);
			HashMap findObjs = new HashMap();
			if (tab == 3) { // 標準售價

				if (imItem != null) {
					// List<ImItemPrice> imItemPrices = imItemCurrentPriceViewDAO.findPageLine("ImItemPrice", "", "itemId", itemId, iSPage, iPSize);
					findObjs.put("and model.itemId = :itemId", imItem.getItemId());
				} else {
					findObjs.put("and 1 <> :itemId", "1");
					log.info("查無 itemId : " + itemId);
				}

				Map searchMap = baseDAO.search("ImItemPrice as model", findObjs, "order by indexNo", iSPage, iPSize,
						BaseDAO.QUERY_SELECT_RANGE);
				List<ImItemPrice> imItemPrices = (List<ImItemPrice>) searchMap.get(BaseDAO.TABLE_LIST);
				log.info("imItemPrices.size() = " + imItemPrices.size());
				if (imItemPrices != null && imItemPrices.size() > 0) {
					// ((ImItemPrice) imItemCurrentPriceViewDAO
					// .findPageLineMaxIndex("ImItemPrice", "", "itemId",
					// itemId, "")).getIndexNo();
					// refreshItemData(map, cmDeclarationItems);

					// 設定其他
					for (ImItemPrice imItemPrice : imItemPrices) {
						if ("Y".equals(imItemPrice.getEnable())) {
							imItemPrice.setEnableName("啟用");
						} else {
							imItemPrice.setEnableName("停用");
						}
						imItemPrice.setCreatedByName(UserUtils.getUsernameByEmployeeCode(imItemPrice.getCreatedBy()));
					}

					// 取得第一筆的INDEX
					Long firstIndex = imItemPrices.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImItemPrice as model", "count(model.itemId) as rowCount",
							findObjs, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
																									// INDEX

					result.add(AjaxUtils.getAJAXPageData(httpRequest, T3_GRID_FIELD_NAMES, T3_GRID_FIELD_DEFAULT_VALUES,
							imItemPrices, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T3_GRID_FIELD_NAMES,
							T3_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}
			} else if (tab == 5) { // 組合商品
				List<ImItemCompose> imItemComposes = imItemComposeDAO.findByProperty("imItem", imItem);
				System.out.println("組合商品筆數：" + imItemComposes.size());
				if (imItemComposes != null && imItemComposes.size() > 0) {

					// 設定其他
					for (ImItemCompose imItemCompose : imItemComposes) {
						String itemCName = "查無品號";
						ImItem item = imItemDAO.findItem(imItemCompose.getBrandCode(), imItemCompose.getComposeItemCode());
						if (item != null){
							imItemCompose.setComposeItemName(item.getItemCName());
							// 新增幣別、定價、成本 update by Weichun 2012.02.23
							imItemCompose.setPurchaseCurrencyCode(item.getPurchaseCurrencyCode());
							List<ImItemPrice> itemPriceLists = imItemPriceDAO.findByProperty("ImItemPrice",
									" AND itemId = ? AND enable = ? order by beginDate desc ", new Object[] { item.getItemId(), "Y" });
							if (itemPriceLists.size() > 0)
								imItemCompose.setItemPrice(((ImItemPrice) itemPriceLists.get(0)).getUnitPrice()); // 定價
							imItemCompose.setItemCost(getLastUnitCost(imItemCompose.getComposeItemCode())); // 成本
						} else{
							imItemCompose.setComposeItemName(itemCName);
						}							
					}

					// 取得第一筆的INDEX
					Long firstIndex = imItemComposes.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = imItemComposes.get(imItemComposes.size() - 1).getIndexNo();

					// imItemComposes
					result.add(AjaxUtils.getAJAXPageData(httpRequest, T5_GRID_FIELD_NAMES, T5_GRID_FIELD_DEFAULT_VALUES,
							imItemComposes, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T5_GRID_FIELD_NAMES,
							T5_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}
			} else if (tab == 6) { // 庫存資料

				if (imItem != null) {
					// imItemOnHandViews = imItemOnHandViewDAO.findPageLine(imItemgetItemCode(), iSPage, iPSize);
					findObjs.put(" and model.id.brandCode = :brandCode", imItem.getBrandCode());
					findObjs.put(" and model.id.itemCode = :itemCode", imItem.getItemCode());
				} else {
					log.info("查無 itemId : " + itemId);
				}

				Map searchMap = baseDAO.search("ImItemOnHandView as model", findObjs, "order by model.id.itemCode desc",
						iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
				List<ImItemOnHandView> imItemOnHandViews = (List<ImItemOnHandView>) searchMap.get(BaseDAO.TABLE_LIST);

				if (imItemOnHandViews != null && imItemOnHandViews.size() > 0) {

					int index = iSPage * iPSize;
					// 設定其他
					for (ImItemOnHandView imItemOnHandView : imItemOnHandViews) {
						imItemOnHandView.setIndexNo(++index);
						imItemOnHandView.setWarehouseCode(imItemOnHandView.getId().getWarehouseCode());
					}
					log.info("庫存資料明細 = " + imItemOnHandViews.size());
					// 取得第一筆的INDEX
					Long firstIndex = Long.valueOf(imItemOnHandViews.get(0).getIndexNo());
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImItemOnHandView as model",
							"count(model.id.itemCode) as rowCount", findObjs, "order by model.id.itemCode desc",
							BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

					result.add(AjaxUtils.getAJAXPageData(httpRequest, T6_GRID_FIELD_NAMES, T6_GRID_FIELD_DEFAULT_VALUES,
							imItemOnHandViews, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T6_GRID_FIELD_NAMES,
							T6_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}
			} else if (tab == 7) { // 月結資料
				if (imItem != null) {
					// imMonthlyBalanceHeads = imMonthlyBalanceHeadDAO.findImMonthlyBalanceHeadByItemCode(imItem.getBrandCode(), imItem.getItemCode(), iPSize);
					findObjs.put(" and model.id.brandCode = :brandCode", imItem.getBrandCode());
					findObjs.put(" and model.id.itemCode = :itemCode", imItem.getItemCode());
				} else {
					log.info("查無 itemId : " + itemId);
				}
				// 資料顯示排序年、月都從大到小 by Weichun 2011.11.11
				Map searchMap = baseDAO.search("ImMonthlyBalanceHead as model", findObjs,
						"order by model.id.year desc, model.id.month desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
				List<ImMonthlyBalanceHead> imMonthlyBalanceHeads = (List<ImMonthlyBalanceHead>) searchMap
						.get(BaseDAO.TABLE_LIST);
				if (imMonthlyBalanceHeads != null && imMonthlyBalanceHeads.size() > 0) {
					int index = iSPage * iPSize;
					// 設定其他
					for (ImMonthlyBalanceHead imMonthlyBalanceHead : imMonthlyBalanceHeads) {
	
						imMonthlyBalanceHead.setIndexNo(++index);
						imMonthlyBalanceHead.setLineYear(imMonthlyBalanceHead.getId().getYear());
						imMonthlyBalanceHead.setLineMonth(imMonthlyBalanceHead.getId().getMonth());
						imMonthlyBalanceHead.setAdjustQuantity((imMonthlyBalanceHead.getPeriodAdjustCostQuantity()==null?0:imMonthlyBalanceHead.getPeriodAdjustCostQuantity()), (imMonthlyBalanceHead.getPeriodAdjustmentQuantity()==null?0:imMonthlyBalanceHead.getPeriodAdjustmentQuantity()));
					}
					// 取得第一筆的INDEX
					Long firstIndex = Long.valueOf(imMonthlyBalanceHeads.get(0).getIndexNo());
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImMonthlyBalanceHead as model",
							"count(model.id.itemCode) as rowCount", findObjs, "order by model.id.itemCode",
							BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
					// INDEX

					result.add(AjaxUtils.getAJAXPageData(httpRequest, T7_GRID_FIELD_NAMES, T7_GRID_FIELD_DEFAULT_VALUES,
							imMonthlyBalanceHeads, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T7_GRID_FIELD_NAMES,
							T7_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}
			} else if (tab == 9) { // 國際碼

				if (imItem != null) {
					findObjs.put(" and model.brandCode = :brandCode", imItem.getBrandCode());
					findObjs.put(" and model.itemCode = :itemCode", imItem.getItemCode());
				} else {
					log.info("查無 itemId : " + itemId);
				}

				Map searchMap = baseDAO.search("ImItemEancode as model", findObjs, "order by model.indexNo", iSPage, iPSize,
						BaseDAO.QUERY_SELECT_RANGE);
				List<ImItemEancode> imItemEancodes = (List<ImItemEancode>) searchMap.get(BaseDAO.TABLE_LIST);

				if (imItemEancodes != null && imItemEancodes.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = imItemEancodes.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImItemEancode as model", "count(model.itemCode) as rowCount",
							findObjs, "order by model.indexNo", BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
																																// INDEX

					result.add(AjaxUtils.getAJAXPageData(httpRequest, T9_GRID_FIELD_NAMES, T9_GRID_FIELD_DEFAULT_VALUES,
							imItemEancodes, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T9_GRID_FIELD_NAMES,
							T9_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}
			} else if (tab == 10) { // 進貨明細紀錄
				if (imItem != null) {
					StringBuffer sb = new StringBuffer();
					sb.append(" and (model.imReceiveHead.warehouseStatus = 'FINISH' or model.imReceiveHead.status IN ('FINISH', 'CLOSE'))");
					findObjs.put(sb.toString() + " and model.imReceiveHead.brandCode = :brandCode", imItem.getBrandCode());
					findObjs.put(" and model.itemCode = :itemCode", imItem.getItemCode());
				} else {
					log.info("查無 itemId : " + itemId);
				}

				Map searchMap = baseDAO.search("ImReceiveItem as model", findObjs,
						"order by model.imReceiveHead.receiptDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
				List<ImReceiveItem> imReceiveItems = (List<ImReceiveItem>) searchMap.get(BaseDAO.TABLE_LIST);

				if (imReceiveItems != null && imReceiveItems.size() > 0) {

					int index = iSPage * iPSize;
					// 設定其他
					for (ImReceiveItem imReceiveItem : imReceiveItems) {
						imReceiveItem.setLineIndexNo(++index);
					}
					// 取得第一筆的INDEX
					Long firstIndex = imReceiveItems.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImReceiveItem as model", "count(model.itemCode) as rowCount",
							findObjs, "order by model.indexNo", BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
																																// INDEX

					result.add(AjaxUtils.getAJAXPageData(httpRequest, T10_GRID_FIELD_NAMES, T10_GRID_FIELD_DEFAULT_VALUES,
							imReceiveItems, gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, T10_GRID_FIELD_NAMES,
							T10_GRID_FIELD_DEFAULT_VALUES, gridDatas));
				}

			} else {
				log.info("查無此標籤 = " + tab);
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的明細失敗！");
		}
	}

    /**
     * 載入商品序號明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSerialPageData(Properties httpRequest)throws Exception {
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long itemId = NumberUtils.getLong(httpRequest.getProperty("itemId"));// 要顯示的ITEM_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    log.info("iSPage = " + iSPage);
	    log.info("iPSize = " + iPSize);

	    // ======================帶入Head的值=========================
	    HashMap map = new HashMap();
	    map.put("itemId", itemId);
	    map.put("startPage", iSPage);
	    map.put("pageSize", iPSize);
	    // ======================取得頁面所需資料===========================

	    ImItem imItem = this.findByItemId(itemId);
	    HashMap findObjs = new HashMap();
	    if (imItem != null) {
		findObjs.put("and model.itemId = :itemId", imItem.getItemId());
	    } else {
		log.error("查無 itemId : " + itemId);
		throw new Exception("itemId參數為空值，無法執行明細載入！");
	    }

	    Map searchMap = baseDAO.search("ImItemSerial as model",findObjs, "order by indexNo", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
	    List<ImItemSerial> imItemSerials = (List<ImItemSerial>) searchMap.get(BaseDAO.TABLE_LIST);

	    if (imItemSerials != null && imItemSerials.size() > 0) {

		// 取得第一筆的INDEX
		Long firstIndex = imItemSerials.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = (Long) baseDAO.search(
			"ImItemSerial as model",
			"count(model.itemId) as rowCount", findObjs,
			BaseDAO.QUERY_RECORD_COUNT).get(
				BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			SERIAL_GRID_FIELD_NAMES, SERIAL_GRID_FIELD_DEFAULT_VALUES,
			imItemSerials, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			SERIAL_GRID_FIELD_NAMES, SERIAL_GRID_FIELD_DEFAULT_VALUES,
			gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的明細失敗！");
	}
    }

    /**
     * 商品主檔 picker 全選
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {

	Map resultMap = new HashMap(0);
	int iSPage  = -1;
	int iPSize  = -1;

	log.info(parameterMap.keySet().toString());
//	Object pickerBean = parameterMap.get("vatBeanPicker");
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object otherBean = parameterMap.get("vatBeanOther");

	try{
	    String timeScope = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
	    String isAllClick = (String)PropertyUtils.getProperty(otherBean, "isAllClick");
	    log.info("timeScope:"+timeScope);
	    log.info("isAllClick:"+isAllClick);

	    // 所有預查詢的條件
	    String brandCode =(String)PropertyUtils.getProperty(otherBean,"loginBrandCode");// 品牌代號

	    String startItemCode = (String)PropertyUtils.getProperty(formBindBean,"startItemCode");
	    String endItemCode = (String)PropertyUtils.getProperty(formBindBean,"endItemCode");
	    String enable = (String)PropertyUtils.getProperty(formBindBean,"enable");
	    String itemName = (String)PropertyUtils.getProperty(formBindBean,"itemName");
	    String supplierItemCode = (String)PropertyUtils.getProperty(formBindBean,"supplierItemCode");
	    String itemLevel = (String)PropertyUtils.getProperty(formBindBean,"itemLevel");
	    String category01 = (String)PropertyUtils.getProperty(formBindBean,"category01");
	    String category02 = (String)PropertyUtils.getProperty(formBindBean,"category02");
	    String category03 = (String)PropertyUtils.getProperty(formBindBean,"category03");
	    String category13 = (String)PropertyUtils.getProperty(formBindBean,"category13"); //系列
	    String category17 = (String)PropertyUtils.getProperty(formBindBean,"category17"); //製造商

	    //==============================================================
	    Map findObjs = new HashMap();

	    findObjs.put(" and ITEM_CODE like :startItemCode","%"+startItemCode+"%");
	    findObjs.put(" and ITEM_CODE like :endItemCode","%"+endItemCode+"%");
	    findObjs.put(" and ENABLE = :enable", enable);
	    findObjs.put(" and BRAND_CODE = :brandCode", brandCode);
	    findObjs.put(" and ITEM_C_NAME = :itemName", itemName);
	    findObjs.put(" and SUPPLIER_ITEM_CODE = :supplierItemCode",
		    supplierItemCode);
	    findObjs.put(" and ITEM_LEVEL = :itemLevel", itemLevel);

	    findObjs.put(" and CATEGORY01 = :category01", category01);
	    findObjs.put(" and CATEGORY02 = :category02", category02);
	    findObjs.put(" and CATEGORY03 = :category03", category03);

	    findObjs.put(" and CATEGORY13 = :category13", category13);
	    findObjs.put(" and CATEGORY17 = :category17", category17);

	    if(brandCode.indexOf("T2") > -1){
		// 商品品牌
		String itemBrand = (String)PropertyUtils.getProperty(formBindBean,"itemBrand"); //製造商
		findObjs.put(" and ITEM_BRAND = :itemBrand", itemBrand);
	    }

	    // ==============================================================
	    Map imItemMap = imItemDAO.search("ImItem", "itemId" , findObjs, iSPage,iPSize, BaseDAO.QUERY_SELECT_ALL);
	    List<ImItem> imItems = (List<ImItem>) imItemMap.get(BaseDAO.TABLE_LIST);

	    if(imItems.size()>0){
		AjaxUtils.updateAllResult(timeScope,isAllClick,imItems);
	    }


	}catch (Exception e) {
	    log.error("全選更新發生問題，原因：" + e.toString());
	    throw new Exception("全選更新發生問題，原因：" + e.getMessage());
	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }

    /**
     * 更新明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest) throws Exception {

		String errorMsg = "新增成功";
		Map returnMap = new HashMap();

		Map eanCodeMap = new HashMap();
		Date date = new Date();
		try {

			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

			Long itemId = NumberUtils.getLong(httpRequest.getProperty("itemId"));
			String brandCode = httpRequest.getProperty("brandCode");
			String itemCode = httpRequest.getProperty("itemCode");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			int tab = NumberUtils.getInt(httpRequest.getProperty("tab"));

			log.info("itemId = " + itemId);
			log.info("brandCode = " + brandCode);
			log.info("itemCode = " + itemCode);
			log.info("tab = " + tab);

			if (itemId == null) {
				throw new ValidationErrorException("傳入的商品維護單主鍵為空值！");
			}
			ImItem imItem = findByItemId(itemId);
			// String status = imItem.getStatus();
			String[] GRID_FIELD_NAMES = {};
			String[] GRID_FIELD_DEFAULT_VALUES = {};
			int[] GRID_FIELD_TYPES = {};
			long indexNo = 0;
			if (tab == 3) {
				GRID_FIELD_NAMES = T3_GRID_FIELD_NAMES;
				GRID_FIELD_DEFAULT_VALUES = T3_GRID_FIELD_DEFAULT_VALUES;
				GRID_FIELD_TYPES = T3_GRID_FIELD_TYPES;
				indexNo = 0;
				ImItemPrice p = (ImItemPrice) imItemPriceDAO.findPageLineMaxIndex("ImItemPrice", "", "itemId", itemId,
						"indexNo desc");
				if (p != null) {
					indexNo = p.getIndexNo();
				}
			} else if (tab == 5) {
				GRID_FIELD_NAMES = T5_GRID_FIELD_NAMES;
				GRID_FIELD_DEFAULT_VALUES = T5_GRID_FIELD_DEFAULT_VALUES;
				GRID_FIELD_TYPES = T5_GRID_FIELD_TYPES;
				indexNo = 0;

				/*
				 * ImItemCompose c = (ImItemCompose) imItemComposeDAO
				 * .findPageLineMaxIndex("ImItemPrice", "", "itemId", itemId,
				 * "indexNo desc"); if (p != null) { indexNo = p.getIndexNo(); }
				 */
			} else if (tab == 9) {
				GRID_FIELD_NAMES = T9_GRID_FIELD_NAMES;
				GRID_FIELD_DEFAULT_VALUES = T9_GRID_FIELD_DEFAULT_VALUES;
				GRID_FIELD_TYPES = T9_GRID_FIELD_TYPES;
				ImItemEancode imItemEancode = ((ImItemEancode) imItemEancodeDAO.findPageLineMaxIndex("ImItemEancode",
						"imItem.", "itemId", itemId, "indexNo desc"));
				if (imItemEancode != null) {
					indexNo = imItemEancode.getIndexNo().intValue();
				}
				log.info("indexNo = " + indexNo);
			}

			// if (true || OrderStatus.SAVE.equals(status)) {
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					GRID_FIELD_NAMES);
			// Get INDEX NO
			log.info("indexNo 跑回圈前 = " + indexNo);
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					if (tab == 3) { // 標準售價
						Long priceId = NumberUtils.getLong(upRecord.getProperty("priceId"));
						String unitPrice = upRecord.getProperty("unitPrice");
						if (StringUtils.hasText(unitPrice) && Double.parseDouble(unitPrice) >= 0) {
							ImItemPrice imItemPricePO = imItemPriceDAO.findById(priceId);
							if (imItemPricePO != null) {
								Double price = imItemPricePO.getUnitPrice();
								if (!"Y".equals(imItemPricePO.getEnable())) {
									AjaxUtils.setPojoProperties(imItemPricePO, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
									imItemPricePO.setLastUpdatedBy(loginEmployeeCode);
									imItemPricePO.setLastUpdateDate(date);
									imItemCurrentPriceViewDAO.update(imItemPricePO);
								}
							} else {
								indexNo++;
								ImItemPrice imItemPrice = new ImItemPrice();
								AjaxUtils.setPojoProperties(imItemPrice, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								imItemPrice.setIndexNo(Long.valueOf(indexNo));
								imItemPrice.setItemId(itemId);
								imItemPrice.setEnable("N");
								imItemPrice.setItemCode(imItem.getItemCode());
								imItemPrice.setSalesUnit(imItem.getSalesUnit());
								imItemPrice.setBrandCode(imItem.getBrandCode());
								imItemPrice.setCreatedBy(loginEmployeeCode);
								imItemPrice.setCreationDate(date);
								imItemPrice.setLastUpdatedBy(loginEmployeeCode);
								imItemPrice.setLastUpdateDate(date);

								imItemPriceDAO.save(imItemPrice);
							}
						}
					} else if (tab == 5) { // 組合商品
						Long composeId = NumberUtils.getLong(upRecord.getProperty("composeId"));
						List<ImItemCompose> imItemComposes = imItemComposeDAO.findByItem(imItem);
						if (imItemComposes.size() > 0)
							indexNo = imItemComposes.size();
						log.info("composeId = " + composeId);
						String composeItemCode = upRecord.getProperty("composeItemCode");
						log.info("composeItemCode = " + composeItemCode);
						Long quantity = NumberUtils.getLong(upRecord.getProperty("quantity"));
						String remark = upRecord.getProperty("remark");
						String reserve1 = upRecord.getProperty("reserve1");
						ImItem composeImItem = imItemDAO.findItem(brandCode, composeItemCode);
						if (StringUtils.hasText(composeItemCode) && null == composeImItem)
							throw new ValidationErrorException("品號：" + composeItemCode + "不存在，請重新輸入！");
						if (StringUtils.hasText(composeItemCode)) {
							ImItemCompose imItemComposePO = imItemComposeDAO.findById(composeId);
							if (imItemComposePO != null && null != composeImItem) {
								log.info("imItemComposePO != null");
								imItemComposePO.setBrandCode(imItem.getBrandCode());
								imItemComposePO.setItemCode(imItem.getItemCode());
								imItemComposePO.setComposeItemCode(composeItemCode);
								imItemComposePO.setQuantity(quantity);
								imItemComposePO.setRemark(remark);
								imItemComposePO.setReserve1(reserve1);
								imItemComposePO.setImItem(imItem);
								imItemComposePO.setLastUpdatedBy(loginEmployeeCode);
								imItemComposePO.setLastUpdateDate(new Date());
								imItemComposeDAO.attachDirty(imItemComposePO);
							} else {
								log.info("imItemComposePO == null");
								indexNo++;
								ImItemCompose imItemCompose = new ImItemCompose();
								imItemCompose.setBrandCode(imItem.getBrandCode());
								imItemCompose.setItemCode(imItem.getItemCode());
								imItemCompose.setComposeItemCode(composeItemCode);
								imItemCompose.setQuantity(quantity);
								imItemCompose.setRemark(remark);
								imItemCompose.setReserve1(reserve1);
								imItemCompose.setIndexNo(Long.valueOf(indexNo));
								imItemCompose.setImItem(imItem);
								imItemCompose.setCreatedBy(loginEmployeeCode);
								imItemCompose.setCreationDate(new Date());
								imItemCompose.setLastUpdatedBy(loginEmployeeCode);
								imItemCompose.setLastUpdateDate(new Date());
								imItemComposeDAO.attachDirty(imItemCompose);
							}
						}
					} else if (tab == 9) { // 國際碼
						log.info("tab == 9");
						String newEanCode = upRecord.getProperty("eanCode").trim();
						log.info("eanCode = " + newEanCode);
						if (StringUtils.hasText(newEanCode)) {
							Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
							String newEnable = upRecord.getProperty("enable");
							log.info("lineId = " + lineId);
							log.info("enable = " + newEnable);
							ImItemEancode imItemEancode = imItemEancodeDAO.findById(lineId);

							if (imItemEancode != null) {
								String beforeEanCode = imItemEancode.getEanCode();
								// AjaxUtils.setPojoProperties(imItemEancode,upRecord,
								// GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								log.info(" 更新 " + indexNo);
								writeErrorMessage(brandCode, beforeEanCode, newEanCode, newEnable, imItemEancode, eanCodeMap);

								imItemEancode.setLastUpdatedBy(loginEmployeeCode);
								imItemEancode.setLastUpdateDate(new Date());
								imItemEancodeDAO.update(imItemEancode);
							} else {
								indexNo++;
								log.info("國際碼 indexNo = " + indexNo);
								imItemEancode = new ImItemEancode();
								// AjaxUtils.setPojoProperties(imItemEancode,upRecord,
								// GRID_FIELD_NAMES,GRID_FIELD_TYPES);
								log.info(" 新增 " + indexNo);
								imItemEancode.setImItem(new ImItem(itemId));
								imItemEancode.setBrandCode(brandCode);
								imItemEancode.setItemCode(itemCode);

								writeErrorMessage(brandCode, null, newEanCode, newEnable, imItemEancode, eanCodeMap);

								imItemEancode.setCreatedBy(loginEmployeeCode);
								imItemEancode.setCreationDate(new Date());
								imItemEancode.setLastUpdatedBy(loginEmployeeCode);
								imItemEancode.setLastUpdateDate(new Date());
								imItemEancode.setIndexNo(Long.valueOf(indexNo));
								imItemEancodeDAO.save(imItemEancode);
							}

						}
					}

				}
			}

			// } catch (FormException fe) {
			// log.info(" fe.getMessage() = " + fe.getMessage());
			// MessageBox msgBox = new MessageBox();
			// msgBox.setMessage(fe.getMessage());
			// returnMap = new HashMap();
			// returnMap.put("vatMessage" ,msgBox);
			//
			// return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新商品明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新商品明細失敗！" + ex.getMessage());
		}
		return AjaxUtils.getResponseMsg(errorMsg);
	}

    /**
     * 更新明細
     *
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateAJAXSerialPageLinesData(Properties httpRequest)throws Exception {

	Date date = new Date();
	String errorMsg = "新增成功";
	long indexNo = 0;
	try {

	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

	    Long itemId = NumberUtils.getLong(httpRequest.getProperty("itemId"));
	    String itemCode = httpRequest.getProperty("itemCode");
	    String brandCode = httpRequest.getProperty("brandCode");
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

	    log.info("itemId = " + itemId);
	    log.info("loginEmployeeCode = " + loginEmployeeCode);

	    if (itemId == null) {
		throw new ValidationErrorException("傳入的商品序號維護單主鍵為空值！");
	    }

	    ImItemSerial imItemSerialMax = ((ImItemSerial) imItemSerialDAO.findPageLineMaxIndex("ImItemSerial", "","itemId", itemId, "indexNo desc"));
	    if (imItemSerialMax != null) {
		indexNo = imItemSerialMax.getIndexNo().intValue();
	    }
	    log.info("indexNo = " + indexNo);

	    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, SERIAL_GRID_FIELD_NAMES);
	    // Get INDEX NO
	    if (upRecords != null) {
		for (Properties upRecord : upRecords) {
		    // 先載入HEAD_ID OR LINE DATA
		    String serial = upRecord.getProperty("serial");
		    if (StringUtils.hasText(serial)) {
			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
			log.info("lineId = " + lineId);
			ImItemSerial imItemSerial = (ImItemSerial)imItemSerialDAO.findFirstByProperty("ImItemSerial","and lineId = ?", new Object[]{lineId}) ;
			if (imItemSerial != null) {
			    AjaxUtils.setPojoProperties(imItemSerial,upRecord, SERIAL_GRID_FIELD_NAMES,SERIAL_GRID_FIELD_TYPES);
			    imItemSerial.setLastUpdatedBy(loginEmployeeCode);
			    imItemSerial.setLastUpdateDate(date);
			    imItemSerial.setMessage(null);
			    imItemSerialDAO.update(imItemSerial);
			} else {
			    ImItemSerial imItemSerialRepeat = (ImItemSerial)imItemSerialDAO.findFirstByProperty("ImItemSerial","and itemId = ? and serial = ?", new Object[]{itemId, serial}) ;
			    if(null == imItemSerialRepeat){ // 只存明細未重複的
				indexNo++;
				log.info("新增 indexNo = " + indexNo);
				imItemSerial = new ImItemSerial();
				AjaxUtils.setPojoProperties(imItemSerial,upRecord, SERIAL_GRID_FIELD_NAMES,SERIAL_GRID_FIELD_TYPES);

				imItemSerial.setItemId(itemId);
				imItemSerial.setBrandCode(brandCode);
				imItemSerial.setItemCode(itemCode);

				imItemSerial.setCreatedBy(loginEmployeeCode);
				imItemSerial.setCreationDate(date);
				imItemSerial.setLastUpdatedBy(loginEmployeeCode);
				imItemSerial.setLastUpdateDate(date);
				imItemSerial.setIndexNo(Long.valueOf(indexNo));

				imItemSerialDAO.save(imItemSerial);
			    }
			}

		    }
		}
	    }

	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新商品序號明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新商品序號明細失敗！" + ex.getMessage());
	}
    }

	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		try {

			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			resultMap.put("itemBrandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			BuEmployee buEmployee = buEmployeeDAO.findById(loginEmployeeCode);
			resultMap.put("costControl", buEmployee.getCostControl());
			// List<BuCommonPhraseLine> allItemCategory20
			// =buCommonPhraseService.getCommonPhraseLinesById("ItemCategory20",false);
			// List<BuCommonPhraseLine> allItemUnit =
			// buCommonPhraseService.getCommonPhraseLinesById("ItemUnit",false);
			// List<BuCommonPhraseLine> allPriceType =
			// buCommonPhraseService.getCommonPhraseLinesById("PriceType",false);

			List<ImItemCategory> allCategoryType = this.imItemCategoryDAO
					.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
			List<ImItemCategory> allCategory01 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY01");
			List<ImItemCategory> allCategory02 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY02");
			List<ImItemCategory> allCategory03 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY03");

			// List<ImItemCategory> allCategory07 =
			// this.imItemCategoryDAO.findByCategoryType(loginBrandCode,
			// "CATEGORY07");
			// List<ImItemCategory> allCategory12 =
			// this.imItemCategoryDAO.findByCategoryType(loginBrandCode,
			// "CATEGORY12");
			// List<ImItemCategory> allCategory13 =
			// this.imItemCategoryDAO.findByCategoryType(loginBrandCode,
			// "CATEGORY13");

			Map multiList = new HashMap(0);

			if (loginBrandCode.indexOf("T2") > -1) {
				List<ImItemCategory> allCategory05 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY05");
				List<ImItemCategory> allCategory06 = this.imItemCategoryDAO.findByCategoryType(loginBrandCode, "CATEGORY06");
				multiList.put("allCategory05",
						AjaxUtils.produceSelectorData(allCategory05, "categoryCode", "categoryName", false, true));
				multiList.put("allCategory06",
						AjaxUtils.produceSelectorData(allCategory06, "categoryCode", "categoryName", false, true));

			}

			HashMap allItemCategory = (HashMap) buCommonPhraseService.findCommonPhraseLinesById("ItemCategory");
			for (int i = 1; i <= 20; i++) {
				if (i < 10) {
					resultMap.put("category0" + i + "CName", allItemCategory.get("CATEGORY0" + i));
				} else {
					resultMap.put("category" + i + "CName", allItemCategory.get("CATEGORY" + i));
				}
			}

			multiList.put("allCategoryType",
					AjaxUtils.produceSelectorData(allCategoryType, "categoryCode", "categoryName", false, false));
			multiList.put("allCategory01",
					AjaxUtils.produceSelectorData(allCategory01, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory02",
					AjaxUtils.produceSelectorData(allCategory02, "categoryCode", "categoryName", true, true));
			multiList.put("allCategory03",
					AjaxUtils.produceSelectorData(allCategory03, "categoryCode", "categoryName", true, true));

			resultMap.put("multiList", multiList);

			// System.out.println("ssssssssssssssssss");////////
			// System.out.println(resultMap.toString());
			return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (Exception ex) {
			log.error("商品搜尋初始化失敗，原因：" + ex.toString());
			throw new Exception("商品搜尋初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 依品號取得組合商品的相關資料
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXComposeItemData(Properties httpRequest) throws Exception {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
			String brandCode = httpRequest.getProperty("brandCode");
			String composeItemCode = httpRequest.getProperty("composeItemCode").trim();
			String composeItemName = "";
			/*
			String composeItemUnit = null;
			String quantity = httpRequest.getProperty("quantity").trim();
			String remark = httpRequest.getProperty("remark").trim();
			String reserve1 = httpRequest.getProperty("reserve1").trim();
			String lotNo = httpRequest.getProperty("lotNo");
			Double localUnitCost = NumberUtils.getDouble(httpRequest.getProperty("localUnitCost"));
			Double difQuantity = NumberUtils.getDouble(httpRequest.getProperty("difQuantity"));
			*/
			String purchaseCurrencyCode = "";
			Double itemPrice = 0D;
			Double itemCost = 0D;
			System.out.println(brandCode + " =====> " + composeItemCode);
			if (StringUtils.hasText(composeItemCode)) {
				ImItem imItem = imItemDAO.findItem(brandCode, composeItemCode);
				if (null != imItem) {
					if(imItem.getItemCName()!=null && imItem.getItemCName()!=""){
						composeItemName = imItem.getItemCName();
					}					
					// composeItemUnit = imItem.getSalesUnit();
					// 新增幣別、定價、成本 update by Weichun 2012.02.23
					if(imItem.getPurchaseCurrencyCode()!=null && imItem.getPurchaseCurrencyCode()!=""){
						purchaseCurrencyCode = imItem.getPurchaseCurrencyCode();
					}				
					List<ImItemPrice> itemPriceLists = imItemPriceDAO.findByProperty("ImItemPrice",
							" AND itemId = ? AND enable = ? order by beginDate desc ", new Object[] { imItem.getItemId(), "Y" });
					if (itemPriceLists.size() > 0)
						itemPrice = ((ImItemPrice) itemPriceLists.get(0)).getUnitPrice(); // 定價
					itemCost = getLastUnitCost(composeItemCode); // 成本

				} else {
					composeItemName = "查無品號";
					purchaseCurrencyCode = "";
				}

				properties.setProperty("composeItemCode", composeItemCode);
				properties.setProperty("composeItemName", composeItemName);
				
				properties.setProperty("purchaseCurrencyCode", purchaseCurrencyCode);
				properties.setProperty("itemPrice", String.valueOf(itemPrice));
				properties.setProperty("itemCost", String.valueOf(itemCost));
	
			} else {
				properties.setProperty("composeItemCode", "");
				properties.setProperty("composeItemName", "");
			}
			result.add(properties);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("取得品號相關欄位資料發生錯誤，原因：" + ex.toString());
			throw new Exception("取得品號相關欄位資料失敗！");
		}
	}

    /**
     * 出錯則塞入message
     * @param brandCode
     * @param itemCode 品號
     * @param beforeEanCode 使用者輸入前
     * @param newEanCode	使用者輸入後
     * @param newEnable		使用者輸入後 的開關
     * @param imItemEancode
     * @param eanCodeMap
     * @throws UniqueConstraintException
     */
    private void writeErrorMessage(String brandCode, String beforeEanCode, String newEanCode, String newEnable, ImItemEancode imItemEancode, Map eanCodeMap)throws UniqueConstraintException{ // , FormException
	List<ImItemEancode> lines = imItemEancodeDAO.findEanCodeByProperty(brandCode, newEanCode );

	log.info("brandCode = " + brandCode);
	log.info("beforeEanCode = " + beforeEanCode);
	log.info("newEanCode = " + newEanCode);
	log.info("newEnable = " + newEnable);
	log.info("imItemEancode = " + imItemEancode);
	log.info("eanCodeMap = " + eanCodeMap);
	String otherItemCode = null;	// isWriteEanCodeMessage用
	String otherItemCode2 = null;	// isItemCodeMessage用

	boolean isWriteEanCodeMessage = false;    // 判斷有相同國際碼但品號不同用
	boolean isItemCodeMessage = false; 	  // 判斷有品號等於別的品號之國際碼用
	// 更新
//	if( beforeEanCode.equals(eanCode) ){ // 修改前與修改後一樣
	if(eanCodeMap.containsKey(newEanCode)){ // 檢查是否重複
	    log.info("檢查是否重複 ");
	    imItemEancode.setMessage("國際碼已存在");
	    throw new UniqueConstraintException("國際碼:" + newEanCode + "重複，請重新輸入");
	}else{ // 不重複的
	    log.info("不重複的");
//	    if( null != lines && lines.size() > 0 ){
	    // 查出是否有相同國際碼且啟用的
	    for (ImItemEancode imItemEancode2 : lines) {
		log.info("imItemEancode2.getEanCode() = " + imItemEancode2.getEanCode());
		log.info("newEanCode = " + newEanCode);
		log.info("imItemEancode.getItemCode() = " + imItemEancode.getItemCode());
		log.info("imItemEancode2.getItemCode() = " + imItemEancode2.getItemCode());
		if(imItemEancode2.getEanCode().equals(newEanCode) && !imItemEancode.getItemCode().equals(imItemEancode2.getItemCode())){
		    otherItemCode = imItemEancode2.getItemCode(); // 表示有國際碼相同但品號不同的
		    if("Y".equals(newEnable)){
			isWriteEanCodeMessage =true;
		    }
		    break;
		}
	    }

	    // 打啟用國際碼,查出是否有相同B品號
	    ImItem line = (ImItem)imItemDAO.findImItem(brandCode, newEanCode,"Y");
	    if(null != line && "Y".equalsIgnoreCase(newEnable)){
		isItemCodeMessage = true;
		otherItemCode2 = line.getItemCode();
	    }

	    // 表示 更新國際碼
	    if(null != beforeEanCode ){
		// 若修改前的國際碼與修改後的國際碼一樣或修改前的國際碼為空則正常操作
		if( beforeEanCode.equals(newEanCode) ){
		    // 表示欲修改的國際碼就是查出來的國際碼, 且品號相同
		    if(null == otherItemCode){

			if(!isItemCodeMessage){
			    // 正常存檔 可能將啟用改成停用
			    log.info("正常存檔 可能將啟用改成停用");
			    imItemEancode.setMessage("");
			    imItemEancode.setEanCode(newEanCode);
			    imItemEancode.setEnable(newEnable);
			}else{
			    log.info("表示入的eanCode 想啟用則不予啟用");
			    imItemEancode.setMessage("國際碼:"+newEanCode+"與已啟用的品號相同不得啟用");
			    imItemEancode.setEanCode(newEanCode);
			    imItemEancode.setEnable("N");
//			    throw new FormException("國際碼:"+newEanCode+"與已有存在的品號相同不得啟用");
			}

		    }else{ // 表示入的eanCode 想啟用則不予啟用 表示相同eanCode 有第2個想啟用
			if(isWriteEanCodeMessage){
			    log.info("表示入的eanCode 想啟用則不予啟用");
			    imItemEancode.setMessage("品號:"+otherItemCode+"已啟用此國際碼");
			    imItemEancode.setEanCode(newEanCode);
			    imItemEancode.setEnable("N");
			}else{// 相同eanCode 只有一個啟用
			    log.info("表示相同eanCode 只有一個啟用, 另一個設定為停用");
			    imItemEancode.setMessage("");
			    imItemEancode.setEanCode(newEanCode);
			    imItemEancode.setEnable(newEnable);
			}
		    }
		}else{
		    // 否則寫入 message表示說無法修改國際碼
		    imItemEancode.setMessage("已建立則無法修改國際碼");
		}
	    }else{
		// 表示 新增國際碼
		if(null == otherItemCode){ // 沒有相同國際碼且不同品號的

		    if(!isItemCodeMessage){
			// 正常存檔 可能將啟用改成停用
			log.info("正常存檔 可能將啟用改成停用");
			imItemEancode.setMessage("");
			imItemEancode.setEanCode(newEanCode);
			imItemEancode.setEnable(newEnable);
		    }else{
			log.info("表示入的eanCode 想啟用則不予啟用");
			imItemEancode.setMessage("國際碼:"+newEanCode+"與已啟用的品號相同不得啟用");
			imItemEancode.setEanCode(newEanCode);
			imItemEancode.setEnable("N");
//			 throw new FormException("國際碼:"+newEanCode+"與已有存在的品號相同不得啟用");
		    }

		}else {
		    if(isWriteEanCodeMessage){
			log.info("表示入的eanCode 想啟用則不予啟用");
			imItemEancode.setMessage("品號:"+otherItemCode+"已啟用此國際碼");
			imItemEancode.setEanCode(newEanCode);
			imItemEancode.setEnable("N");
		    }else{ // 相同eanCode 只有一個啟用
			log.info("表示相同eanCode 只有一個啟用, 另一個設定為停用");
			imItemEancode.setMessage("");
			imItemEancode.setEanCode(newEanCode);
			imItemEancode.setEnable(newEnable);
		    }
		}
	    }
//	    }else{ // 沒有相同品牌與國際馬啟用的
//	    log.info("正常存檔無使用過的國際碼或停用的國際碼");
//	    imItemEancode.setMessage("");
//	    imItemEancode.setEanCode(newEanCode);
//	    imItemEancode.setEnable(newEnable);
//	    }
	    eanCodeMap.put(newEanCode, newEanCode);
	}
//	}
//	else{ // 新增
//	if(eanCodeMap.containsKey(eanCode)){
//	imItemEancode.setMessage("國際碼已存在");
//	throw new UniqueConstraintException("國際碼:" + eanCode + "重複，請重新輸入");
//	}else{
//	if( null != line ){
//	if(line.equals(eanCode)){
//	}else{ // 表示入的eanCode 想啟用則不予啟用
//	imItemEancode.setMessage("品號:"+line.getItemCode()+"已啟用此國際碼");
//	imItemEancode.setEnable("N");
//	}
//	}else{
//	imItemEancode.setMessage("");
//	}
//	eanCodeMap.put(eanCode, eanCode);
//	}
//	}
    }

    /**
     * 透過傳遞過來的參數來做EanCode下傳
     * @param parameterMap
     * @throws Exception
     */
    public Long executeItemEanExport(HashMap parameterMap) throws Exception{
    	log.info("executeItemEanExport");
    	Long responseId = -1L;
    	Long numbers = 0L;

    	//一、解析程式需要排程下傳或是即時下傳
    	Long batchId = (Long)parameterMap.get("BATCH_ID");
		String uuId = posExportDAO.getDataId();

		//二、下傳程式至POS_ITEM_EANCODE (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			//輸入搜尋條件(排程)
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			List results = imItemEancodeDAO.findEanCodeListByProperty(parameterMap);
			if(results != null && results.size() > 0){
		        for (Object result : results) {
		        	ImItemEancode imItemEancode = (ImItemEancode)result;
			        PosItemEancode posItemEancode = new PosItemEancode();
			        BeanUtils.copyProperties(imItemEancode, posItemEancode);
			        posItemEancode.setDataId(uuId);
			        if("Y".equals(imItemEancode.getEnable()))
			        	posItemEancode.setAction("U");
			        else
			        	posItemEancode.setAction("D");
			        posExportDAO.save(posItemEancode);
		        }
			}else{
			        //查無資料
			        parameterMap.put("searchResult","noData");
			}
		}else{
			//非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			String dataId = (String)parameterMap.get("DATA_ID");
			//尋找PosCustomer中此dataID有哪些需求資料
			List<PosItemEancode> posItemEancodes = posExportDAO.findByProperty("PosItemEancode", new String[]{"dataId"}, new Object[]{dataId});
			for (Iterator iterator = posItemEancodes.iterator(); iterator.hasNext();) {
				PosItemEancode posItemEancode = (PosItemEancode) iterator.next();
				List results = imItemEancodeDAO.findEanCodeByProperty(posItemEancode.getBrandCode(), posItemEancode.getEanCode());
				if(results != null && results.size() >= 0){
			        	ImItemEancode imItemEancode = (ImItemEancode)results.get(0);
				        PosItemEancode newPosItemEancode = new PosItemEancode();
				        BeanUtils.copyProperties(imItemEancode, newPosItemEancode);
				        newPosItemEancode.setDataId(uuId);
				        if("Y".equals(imItemEancode.getEnable()))
				        	newPosItemEancode.setAction("U");
				        else
				        	newPosItemEancode.setAction("D");
				        posExportDAO.save(newPosItemEancode);
				}
			}
		}
		//更新新的DATA_ID做回傳
		parameterMap.put("DATA_ID", uuId);
		parameterMap.put("NUMBERS", numbers);
		responseId = posExportDAO.executeCommand(parameterMap);
		return responseId;
    }

    /**
	 * 將T1CO的商品資料更新到T6CO
	 *
	 * @return
	 */
	public String updateT6COItem() {
		System.out.println("=== 開始將T1CO的商品資料轉到T6CO ===");
		String msg = "";
		Connection conn = null;
		CallableStatement calStmt = null;
		try {
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.IM_ITEM_COPY}"); // 呼叫store procedure
			calStmt.execute();
			System.out.println("==== 呼叫資料庫procedure完成，資料下載成功！ ====");
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "轉換過程出現錯誤！";
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

    public Long executePosItemExport(HashMap parameterMap) throws Exception{
	log.info("executePosItemExport");
	Long responseId = -1L;
	Long numbers = 0L;
	// 一、解析程式需要排程下傳或是即時下傳
	Long batchId = (Long) parameterMap.get("BATCH_ID");
	String uuId = posExportDAO.getDataId();// 產生dataId

	// 二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
	if (null == batchId || batchId <= 0) {
		//輸入搜尋條件(排程)
	    	String brandCode = (String)parameterMap.get("BRAND_CODE");
	    	parameterMap.put("brandCode", brandCode);
		parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
		parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
		List<Object[]> results = imItemDAO.findbyCondition(parameterMap);
		if(results != null && results.size() >0){
		       
		    for (Object[] result : results) {
//		        	ImItem imItem = (ImItem)result[0];
//		        	ImItemPrice imItemPrice = (ImItemPrice)result[1];
//			        System.out.println("=======imItem===="+imItem);
//			        System.out.println("=======imItemPrice===="+imItemPrice);
			
				String itemCode = (String)result[1];
		                //  防止超出十四碼問題
		                if(itemCode.length() < 14 ){

//		                    Double unitPrice = ((BigDecimal)result[6]).doubleValue();
//		                    //VIP會員
//		                    String vipDiscount = (String)result[13];
//		                    Double PRICE_R1 = unitPrice;
//		                    if(!"T1BS".equals(brandCode) && !"N".equals(vipDiscount)){
//		                	ImPromotion vp = getVIPPromotion("VIPType", brandCode + "VIP", brandCode);
//		                	PRICE_R1 = getPromotionPrice(unitPrice, itemCode, vp);
//		                    }
		               
		                //準備update的資料
		                
		        	Object[] record = new Object[24];
		        	record[0] = "U";//Action
		        	record[1] = brandCode;//BrandCode
		        	record[2] = result[10];//Category01
		        	record[3] = imItemCategoryDAO.findByCategoryCode(result[10]);//Category01Name
		        	record[4] = result[11];//Category02
		        	record[5] = imItemCategoryDAO.findByCategoryCode(result[11]);//Category02Name
		        	record[6] = uuId;//data_id
		        	record[7] = result[14];//decl_ratio
		        	record[8] = "Y";//enable
		        	record[9] = "";//is_after_count
		        	record[10] = "";//is_before_count
		        	record[11] = "";//is_change_price
		        	record[12] = result[8];//is_service_item
		        	record[13] = result[15];//is_tax
		        	record[14] = result[16];//item_brand
		        	record[15] = imItemCategoryDAO.findByCategoryCode(result[16]);//item_brand_name
		        	record[16] = result[2];//item_c_name
		        	record[17] = result[1];//item_code
		        	record[18] = result[17];//lot_control
		        	record[19] = result[18];//min_ratio
		        	record[20] = result[19];//original_unit_price
		        	record[21] = result[5];//sales_unit
		        	record[22] = result[6];//sell_unit_price
		        	record[23] = result[13];//vip_discount
		        	
		                    imItemDAO.updatePosItem(record);
		                }
		    	}
		}else{
		    //查無資料
		    parameterMap.put("searchResult","noData");
		    
		}
	}else{
	    //非排程則是把DataId找出，再去POS_ITEM依據Data_Id把資訊船進去
	    String dataId = (String)parameterMap.get("DATA_ID");
	    List result = posExportDAO.findByDataId(dataId);
	    if(result !=null && result.size()>0){
		String itemCode;
		List<ImItem> itemList = null; 
		for(int i=0;i<result.size();i++){
		    itemCode = (String)result.get(i);
		    itemList.add(imItemDAO.findById(itemCode));
		}
		
	    }
	    
	}
	// 更新新的DATA_ID做回傳
	parameterMap.put("DATA_ID", uuId);
	parameterMap.put("NUMBERS", numbers);
	responseId = posExportDAO.executeCommand(parameterMap);
	return responseId;
	
    }


	private ImPromotion getVIPPromotion(String headCode, String lineCode, String brandCode) {
		log.info("POSExportData.getVIPPromotion headCode=" + headCode + ",lineCode=" + lineCode + ",brandCode=" + brandCode);
		ImPromotion imPromotion = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine(headCode, lineCode);
		if (null != buCommonPhraseLine) {
			String promotionCode = buCommonPhraseLine.getAttribute2();
			ImPromotionDAO imPromotionDAO = (ImPromotionDAO) SpringUtils.getApplicationContext().getBean("imPromotionDAO");
			imPromotion = imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
		}
		return imPromotion;
	}
	private Double getPromotionPrice(Double unitPrice, String itemCode, ImPromotion imPromotion) {
		log.info("POSExportData.getPromotionPrice unitPrice=" + unitPrice + ",itemCode=" + itemCode);
		Double re = new Double(0);
		// 0.HEAD DISCOUNT 比例
		if (null != imPromotion) {
			if (null != imPromotion.getDiscount()) {
				re = unitPrice * (100L - imPromotion.getDiscount()) / 100;
			} else {
				List<ImPromotionItem> imPromotionItems = imPromotion.getImPromotionItems();
				if (null != imPromotionItems) {
					for (ImPromotionItem imPromotionItem : imPromotionItems) {
						// 1.抓ITEM
						if ("itemCode".equalsIgnoreCase(imPromotionItem.getItemCode())) {
							// 2.type = 1 金額 / 2 比例
							String type = imPromotionItem.getDiscountType();
							if ("1".equals(type)) {
								re = unitPrice - imPromotionItem.getDiscount();
							} else if ("2".equals(type)) {
								re = unitPrice * (100D - imPromotionItem.getDiscount()) / 100;
							}
							break;
						}
					}
				}else{
				        re = unitPrice;
				}
			}
		}else{
		        re = unitPrice;
		}
		return CommonUtils.round(re, 0);
	}
	
	/*
	public void setPosItem(PosItem posItem, ImItem imItem, ImItemPrice imItemPrice) {
		posItem.setBrandCode(imItem.getBrandCode());
		posItem.setItemCode(imItem.getItemCode());
		posItem.setCategory01(imItem.getCategory01());
		posItem.setCategory02(imItem.getCategory02());
		posItem.setDeclRatio(imItem.getDeclRatio());
		posItem.setEnable(imItem.getEnable());
		posItem.setIsServiceItem(imItem.getIsServiceItem());
		posItem.setIsTax(imItem.getIsTax());
		posItem.setItemBrand(imItem.getItemBrand());
		posItem.setItemBrandName(imItem.getItemBrandName());
		posItem.setItemCName(imItem.getItemCName());
		posItem.setItemCode(imItem.getItemCode());
		posItem.setLotControl(imItem.getLotControl());
		posItem.setMinRatio(imItem.getMinRatio());
		//posItem.setOriginalUnitPrice(imItemPrice.getOriginalPrice());
		posItem.setSalesUnit(imItem.getSalesUnit());
		posItem.setSellUnitPrice(imItemPrice.getUnitPrice());
	}*/
	
	/**
	 * 組合商品匯入
	 * 
	 * @param itemId
	 * @param imItemComposes
	 * @throws Exception
	 */
	public void executeImportComposeItems(Long itemId, List imItemComposes) throws Exception {
		
		List<ImItemCompose> itemComposes = new ArrayList(0);
		try {
			// deleteMovementItems(headId);

			ImItem imItem = imItemDAO.findByItemId(itemId);
			if (imItem == null)
				throw new NoSuchObjectException("查商品ITEM_ID：" + itemId + "的資料");

			if (imItemComposes != null && imItemComposes.size() > 0) {
				for (int i = 0; i < imItemComposes.size(); i++) {
					ImItemCompose imItemCompose = (ImItemCompose) imItemComposes.get(i);
					imItemCompose.setIndexNo(i + 1L);
					imItemCompose.setItemCode(imItem.getItemCode());
					imItemCompose.setBrandCode(imItem.getBrandCode());
					itemComposes.add(imItemCompose);
				}
				imItem.setImItemComposes(itemComposes);
			} else {
				imItem.setImItemComposes(new ArrayList(0));
			}
			imItemDAO.update(imItem);
		} catch (NoSuchObjectException ns) {
			log.error("查詢商品明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			log.error("組合商品明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("組合商品明細匯入時發生錯誤，原因：" + ex.getMessage());
		}
		
	}
	
	/**
	 * 取得進貨成本
	 * 
	 * @param itemCode 品號
	 * @return
	 */
	public Double getLastUnitCost(String itemCode) {
		log.info("PoPurchaseOrderLineMainService.setLastUnitCost itemCode=" + itemCode);
		Double foreignUnitPrice = new Double(0);
		if (StringUtils.hasText(itemCode)) {
			foreignUnitPrice = imReceiveHeadDAO.getLastForeignUnitPrice(itemCode);
			if (null == foreignUnitPrice || foreignUnitPrice.equals(0D)) {
				ImItem imItem = imItemDAO.findById(itemCode);
				if (null != imItem && null != imItem.getSupplierQuotationPrice()) {
					foreignUnitPrice = imItem.getSupplierQuotationPrice();
				}
			}
		}
		return foreignUnitPrice.doubleValue();
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
    public SelectDataInfo getAJAXExportDataForT2B(HttpServletRequest httpRequest) throws Exception{
	StringBuffer sql = new StringBuffer();
	Object[] object = null;
	List rowData = new ArrayList();
	try {
	    String brandCode = httpRequest.getParameter("brandCode");
	    String startItemCode = httpRequest.getParameter("startItemCode");
	    String endItemCode = httpRequest.getParameter("endItemCode");
	    String enable = httpRequest.getParameter("enable");
	    String itemName = httpRequest.getParameter("itemName");

	    String supplierItemCode = httpRequest.getParameter("supplierItemCode");
	    String itemLevel = httpRequest.getParameter("itemLevel");
	    String category01 = httpRequest.getParameter("category01");
	    String category02 = httpRequest.getParameter("category02");
	    String category03 = httpRequest.getParameter("category03");

	    String category13 = httpRequest.getParameter("category13"); // 系列
	    String category17 = httpRequest.getParameter("category17"); // 製造商

	    log.info("brandCode = " + brandCode);
	    log.info("startItemCode = " + startItemCode);
	    log.info("endItemCode = " + endItemCode);
	    log.info("enable = " + enable);
	    log.info("itemName = " + itemName);
	    log.info("supplierItemCode = " + supplierItemCode);
	    log.info("itemLevel = " + itemLevel);
	    log.info("category01 = " + category01);
	    log.info("category02 = " + category02);
	    log.info("category03 = " + category03);
	    log.info("category13 = " + category13);
	    log.info("category17 = " + category17);

	    sql.append("SELECT I.ITEM_CODE,I.SUPPLIER_ITEM_CODE ,I.ITEM_C_NAME, I.ITEM_E_NAME, I.ITEM_BRAND, ")
	    .append("I.CATEGORY01, I.CATEGORY02, I.CATEGORY03, I.FOREIGN_CATEGORY, I.CATEGORY05, I.CATEGORY06, I.CATEGORY13, ")
	    .append("I.COLOR_CODE, I.CATEGORY10, I.CATEGORY08, I.MATERIAL, I.CATEGORY11, I.CATEGORY09, I.CATEGORY04, ")
	    .append("I.VALIDITY_DAY, I.RELEASE_STRING, I.EXPIRY_STRING, I.CATEGORY14, I.CATEGORY15, I.SPEC_LENGTH, ")
	    .append("I.SPEC_WIDTH, I.SPEC_HEIGHT, I.SPEC_WEIGHT, I.DESCRIPTION, I.CATEGORY07, I.CATEGORY12, I.CATEGORY16, ")
	    .append("I.CATEGORY18, I.CATEGORY19, I.CATEGORY17, I.SALES_UNIT, I.PURCHASE_UNIT, I.VIP_DISCOUNT, I.CATEGORY_TYPE, ")
	    .append("I.ITEM_CATEGORY, I.PURCHASE_CURRENCY_CODE, I.PURCHASE_AMOUNT, I.IS_TAX, I.FOREIGN_LIST_PRICE, I.SUPPIER_QUOTATION_PRICE, ")
	    .append("I.MARGEN, I.MAX_PURCHASE_QUANTITY, I.MIN_PURCHASE_QUANTITY, I.ITEM_LEVEL, I.REPLENISH_COEFFICIENT, I.IS_CONSIGN_SALE, ")
	    .append("I.BOX_CAPACITY, I.SALES_RATIO, I.PURCHASE_RATIO, I.IS_COMPOSE_ITEM, I.IS_SERVICE_ITEM, I.CATEGORY20, I.ENABLE, ")
	    .append("I.ITEM_TYPE, I.ALLOW_MINUS_STOCK, I.LOT_CONTROL, I.BRAND_CODE, I.ALCOLHO_PERCENT ")
	    .append("FROM IM_ITEM I ")
	    .append("WHERE 1=1 ");
//	    .append("AND I.CATEGORY17 = S.SUPPLIER_CODE(+) ")
//	    .append("AND S.ADDRESS_BOOK_ID = A.ADDRESS_BOOK_ID(+) ");

	    StringBuffer newCodes = new StringBuffer();
	    String itemCodes = httpRequest.getParameter("itemCodes");
	    log.info("New itemCodes="+itemCodes);
	    if(StringUtils.hasText(itemCodes)){
		String[] codes = itemCodes.split(",");
		for (String code : codes) {
		    newCodes.append("'").append(code).append("'").append(",");
		}
		if(codes.length > 0){
		    newCodes.delete(newCodes.length()-1, newCodes.length());
		}
		newCodes.insert(0, "AND I.ITEM_CODE in (").append(") ");
		sql.append(newCodes);
	    }else{
		if(StringUtils.hasText(startItemCode)){
		    sql.append("AND I.ITEM_CODE like '%").append(startItemCode).append("%' ");
		}
	    }

	    if(brandCode.indexOf("T2") > -1){

		// 商品品牌
		String itemBrand = httpRequest.getParameter("itemBrand");
		String category05 = httpRequest.getParameter("category05");
		String category06 = httpRequest.getParameter("category06");
		String itemEName = httpRequest.getParameter("itemEName");
		String category11 = httpRequest.getParameter("category11");
		String foreignCategory = httpRequest.getParameter("foreignCategory");

		if(StringUtils.hasText(itemEName)){
		    sql.append("AND I.ITEM_E_NAME = '").append(itemEName).append("' ");
		}
		if(StringUtils.hasText(category11)){
		    sql.append("AND I.CATEGORY11 like '%").append(category11).append("%' ");
		}

		if(StringUtils.hasText(foreignCategory)){
		    sql.append("AND I.FOREIGN_CATEGORY = '").append(foreignCategory).append("' ");
		}

		if(StringUtils.hasText(itemName)){
		    sql.append("AND I.ITEM_C_NAME LIKE '%").append(itemName).append("%' ");
		}
		if(StringUtils.hasText(itemBrand)){
		    sql.append("AND I.ITEM_BRAND = '").append(itemBrand).append("' ");
		}

		if(StringUtils.hasText(category05)){
		    sql.append("AND I.CATEGORY05 = '").append(category05).append("' ");
		}
		if(StringUtils.hasText(category06)){
		    sql.append("AND I.CATEGORY06 = '").append(category06).append("' ");
		}
		if(StringUtils.hasText(category13)){
		    sql.append("AND I.CATEGORY13 like '%").append(category13).append("%' ");
		}
	    }else{
		if(StringUtils.hasText(itemName)){
		    sql.append("AND I.ITEM_C_NAME = '").append(itemName).append("' ");
		}
		if(StringUtils.hasText(category13)){
		    sql.append("AND I.CATEGORY13 like '%").append(category13).append("%' ");
		}
	    }

	    sql.append("AND I.BRAND_CODE = '").append(brandCode).append("' ");

	    if(StringUtils.hasText(endItemCode)){
		sql.append("AND I.ITEM_CODE like '%").append(endItemCode).append("%' ");
	    }

	    if(StringUtils.hasText(enable)){
		sql.append("AND I.ENABLE = '").append(enable).append("' ");
	    }

	    if(StringUtils.hasText(supplierItemCode)){
		sql.append("AND I.SUPPLIER_ITEM_CODE = '").append(supplierItemCode).append("' ");
	    }

	    if(StringUtils.hasText(category01)){
		sql.append("AND I.CATEGORY01 = '").append(category01).append("' ");
	    }

	    if(StringUtils.hasText(category02)){
		sql.append("AND I.CATEGORY02 = '").append(category02).append("' ");
	    }

	    if(StringUtils.hasText(category03)){
		sql.append("AND I.CATEGORY03 = '").append(category03).append("' ");
	    }

	    if(StringUtils.hasText(category17)){
		sql.append("AND I.CATEGORY17 = '").append(category17).append("' ");
	    }

	    sql.append("ORDER BY I.ITEM_CODE ");

	    List lists = nativeQueryDAO.executeNativeSql(sql.toString());

	    object = new Object[] { "itemCode", 	"supplierItemCode", 		"itemCName", 		"itemEName", 	"itemBrand",
		    		"category01", 	    "category02",		"category03", 	"foreignCategory", 	"category05",
		    		"category06", 		"category13",		"colorCode",	"category10",	"category08",
		    		"material", 		"category11",		"category09",   "category04",	"validityDay",
		    		"releaseString",	"expiryString",	"category14",	"category15",		"specLength",
		    		"specWidth",		"specHeight",	"specWeight",	"description",		"category07",
		    		"category12",		"category16",	"category18",	"category19",		"category17",
		    		"salesUnit",		"purchaseUnit",	"vipDiscount",	"categoryType",		"itemCategory",
		    		"purchaseCurrencyCode",	"purchaseAmount",	"isTax",	"foreignListPrice","supplierQuotationPrice",
		    		"margen" ,"maxPurchaseQuantity",	"minPurchaseQuantity",	"itemLevel",	"replenishCoefficient",
		    		"isConsignSale" ,	"boxCapacity",	"salesRatio",	"purchaseRatio",	"isComposeItem",	
		    		"isServiceItem"	,	"category20",	"enable",		"itemType",			"allowMinusStock",
		    		"lotControl",		"brandCode",	"alcolhoPercent"
	    };

	    log.info(" lists.size() = " + lists.size() );
	    for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
		Object[] getObj = (Object[])lists.get(i);
		log.info("getObj="+getObj);
		Object[] dataObject = new Object[object.length];
		//Date priceLastUpdateDate = null;
		for (int j = 0; j < object.length; j++) {
		   
			dataObject[j] = getObj[j];
		    
		}
		log.info("dataObject"+dataObject);
		rowData.add(dataObject);
	    }

	    return new SelectDataInfo(object, rowData);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("匯出excel發生錯誤，原因：" + e.toString());
	    throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
	}
    }
    
  //return Map
    public Map getAJAXItemInfomation(String brandCode,String itemCode)throws ValidationErrorException {
    	Map returnMap = new HashMap();
    	List<Properties> result = new ArrayList();
 	    Properties properties = new Properties();
    	try {
    			// 查詢主檔
        		ImItem imItem = imItemDAO.findItem(brandCode, itemCode); 
        		System.out.println("find imItem:"+imItem);
        		returnMap.put("Item", imItem);
        		
        		if(StringUtils.hasText(itemCode)){
        			if(itemCode.toUpperCase().lastIndexOf("VP")>-1 || itemCode.toUpperCase().lastIndexOf("VF")>-1 ||
        					itemCode.toUpperCase().lastIndexOf("P")>-1 || itemCode.toUpperCase().lastIndexOf("F")>-1){
        				if(itemCode.toUpperCase().lastIndexOf("VP")==(itemCode.length()-1)){
        					itemCode = itemCode.trim().toUpperCase().substring(0,itemCode.toUpperCase().lastIndexOf("VP"));
        				}else if(itemCode.toUpperCase().lastIndexOf("VF")==(itemCode.length()-1)){
        					itemCode = itemCode.trim().toUpperCase().substring(0,itemCode.toUpperCase().lastIndexOf("VF"));
        				}else if(itemCode.toUpperCase().lastIndexOf("P")==(itemCode.length()-1)){
        					itemCode = itemCode.trim().toUpperCase().substring(0,itemCode.toUpperCase().lastIndexOf("P"));
        				}else if(itemCode.toUpperCase().lastIndexOf("F")==(itemCode.length()-1)){
        					itemCode = itemCode.trim().toUpperCase().substring(0,itemCode.toUpperCase().lastIndexOf("F"));
        				}else {
        					System.out.println("WTF? where are you into this server?");
        				}
        			}
        		
        		if(null != imItem){
        			Ftp_Test ftpTest = new Ftp_Test();
        			String rtnFile =  ftpTest.ftpGetOne(brandCode, imItem.getCategoryType(), imItem.getItemBrand(), itemCode);
        			System.out.println("file picture :"+rtnFile);
        			properties.setProperty("img", rtnFile );
        			returnMap.put("img", "images/"+rtnFile);
//        			System.out.println("find ftp Files:"+rtnFile.size());
//        			returnMap.put("imgs", rtnFile );
//        			for(String fileSrc : rtnFile){
//        				properties.setProperty("img", fileSrc );
//        			}
        			properties.setProperty("itemCName", imItem.getItemCName() );
        			properties.setProperty("supplierItemCode", null!=imItem.getSupplierItemCode()?imItem.getSupplierItemCode():"" );
        			properties.setProperty("categoryType", imItem.getCategoryType() );
        			properties.setProperty("itemBrand", imItem.getItemBrand() );
        			properties.setProperty("category01", imItem.getCategory01() );
        			properties.setProperty("category02", imItem.getCategory02() );
        			properties.setProperty("maxPurchaseAmount", String.valueOf(imItem.getMaxPurchaseAmount()) );
        		}
        		result.add(properties);
    		}
    		
    	    return returnMap;
    	} catch (Exception ex) {
    	    log.error("失敗，原因：" + ex.toString());
    	    ex.printStackTrace();
    	    throw new ValidationErrorException("失敗！");
    	}
        }
    
    public List<Map> getAJAXCategoryTypeInfomation(String brandCode,String categoryType,String loginUser)throws ValidationErrorException {
    	List<Map> promoteLists = new ArrayList();
    	List<Map> resultLists = new ArrayList();
    	Ftp_Test ftpTest = new Ftp_Test();
    	try {
    		//find vip promote cate gory
    		List<BuVipPromote> promotes = imItemCategoryDAO.findBuVipPromote(loginUser);
    		System.out.println("find promotes:"+promotes);
			
			for(BuVipPromote vp:promotes) {
				
					String categoryName = "";
					if(vp.getPromoteCategory().indexOf("1")>-1) {
						categoryName = "菸酒";
					}else if(vp.getPromoteCategory().indexOf("2")>-1) {
						categoryName = "精品";
					}else if(vp.getPromoteCategory().indexOf("3")>-1) {
						categoryName = "化妝品";
					}else if(vp.getPromoteCategory().indexOf("4")>-1) {
						categoryName = "台產";
					}else if(vp.getPromoteCategory().indexOf("5")>-1) {
						categoryName = "3C";
					}
					Map category = new HashMap();
					category.put("categoryCode", Long.valueOf(vp.getPromoteCategory()));
					category.put("categoryName", categoryName);
					System.out.println("find vp.getPromoteCategory():"+vp.getPromoteCategory());
					
					//NAS-picture 
		    		//String rtnFile =  ftpTest.ftpGetOne(brandCode, imItem.getCategoryType(), imItem.getItemBrand(), itemCode);
		    		List<Map> goods = ftpTest.ftpGetByCategoryType(brandCode, vp.getPromoteCategory());//itemCode,src
		    		
		    		//item information
		    		for(Map m:goods) {
		    			ImItemCurrentPriceView itemPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, String.valueOf(m.get("itemCode")), "1");
		    			//ImItem item = imItemDAO.findById(String.valueOf(m.get("itemCode")));
		    			System.out.println("find ImItemCurrentPriceView:"+itemPriceView);
		    			
		    			if(null!=itemPriceView) {
		    				System.out.println("find ImItemCurrentPriceView:"+((null!=itemPriceView.getUnitPrice())?itemPriceView.getUnitPrice():""));
		    				m.put("itemName", null!=itemPriceView.getItemCName()? itemPriceView.getItemCName():itemPriceView.getItemCode());
			    			m.put("unitPrice", null!=itemPriceView.getUnitPrice()? itemPriceView.getUnitPrice():0L);
		    			}else {
		    				m.put("itemName","查無此商品");
			    			m.put("unitPrice", "0.0");
		    			}
		    			
		    		}
		    		category.put("promoteList", goods);
		    		resultLists.add(category);
			}
    		//returnMap.put("resultLists", resultLists);
			System.out.println("search end");
    	    return resultLists;
    	} catch (Exception ex) {
    	    log.error("失敗，原因：" + ex.toString());
    	    ex.printStackTrace();
    	    throw new ValidationErrorException("失敗！");
    	}
        }
    
    public List<Map> getInfomation(String brandCode)throws ValidationErrorException {
    	Ftp_Test ftpTest = new Ftp_Test();
    	try {
			List<Map> goods = ftpTest.ftpGetByCategoryType(brandCode, "6");
		}catch(Exception e) {
			e.printStackTrace();
		}
    	return null;
    	
    }
    
    public List<Map> savelog(String loginUser,String itemCode,String functionCode,String categoryType)throws ValidationErrorException {
    	List<Map> resultLists = null;
    	try {
    		
    		SiSystemLog siLog = new SiSystemLog();
    		siLog.setMessage(loginUser);
    		siLog.setReserve1(itemCode);
    		siLog.setReserve2(functionCode);
    		siLog.setReserve3(DateUtils.getCurrentDateStr("YYYY/MM/dd hh:mm:ss"));
    		siLog.setReserve4(categoryType);
    		
    		System.out.println("loginUser:"+loginUser); 
    		System.out.println("itemCode:"+itemCode);
    		System.out.println("date:"+DateUtils.getCurrentDateStr("YYYY/MM/dd hh:mm:ss"));
    		System.out.println("functionCode:"+functionCode);
    		siSystemLogDAO.saveOrUpdate(siLog);
    		
    		//returnMap.put("resultLists", resultLists);
    	    return resultLists;
    	} catch (Exception ex) {
    	    log.error("失敗，原因：" + ex.toString());
    	    ex.printStackTrace();
    	    throw new ValidationErrorException("失敗！");
    	}
        }
    
    public void getLevelDataMax(ImItem imItem) throws Exception{
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	Long composeLevel = 0L;
    	try{
    		DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");    	
        	conn = dataSource.getConnection();
        	StringBuffer sql = new StringBuffer();
    		stmt = conn.createStatement();
    		
    		sql.append("select Max(IM.COMPOSE_LEVEL) AS COMPOSE_LEVEL FROM ERP.IM_ITEM IM, ERP.IM_ITEM_COMPOSE IC");
    		sql.append(" where IM.ITEM_CODE = IC.COMPOSE_ITEM_CODE");
    		sql.append(" and IC.ITEM_CODE = '"+imItem.getItemCode()+"'");
    		
    		rs = stmt.executeQuery(sql.toString());
    		while(rs.next()){
    			composeLevel = rs.getLong("COMPOSE_LEVEL");
    		}
    		
    		imItem.setComposeLevel(composeLevel+1);	
    		
    		imItemDAO.save(imItem);
    		log.info("ddddd"+composeLevel);
    	}catch(Exception ex){
    		System.out.println(ex.getMessage());
	    } finally {
	    	conn.close();
	    }    	
    }
    
}
