package tw.com.tm.erp.hbm.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Enumeration;

import java.text.DateFormat;

import javax.imageio.ImageIO;
import javax.sql.DataSource;


import tw.com.tm.erp.exceptions.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

//import tw.com.tm.erp.action.BuPurchaseAction;
import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;

import tw.com.tm.erp.exceptions.FormException;

import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.ShopSet;
import tw.com.tm.erp.hbm.bean.ShopSetDetail;

import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
//import tw.com.tm.erp.hbm.bean.BuPurchaseHead;


import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.AdDetail;
import tw.com.tm.erp.hbm.bean.AdTaskReviewView;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.BuItemCategoryPrivilege;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
//import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.GnFile;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.PoVerificationSheet;
import tw.com.tm.erp.hbm.bean.PosCurrency;
import tw.com.tm.erp.hbm.bean.PrItem;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiGroupMenuCtrl;
import tw.com.tm.erp.hbm.bean.SiGroupMenuIdCtrl;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
//import tw.com.tm.erp.hbm.bean.TampBuPurchaseLine;
import tw.com.tm.erp.hbm.dao.AdCategoryLineDAO;
import tw.com.tm.erp.hbm.dao.AdDetailDAO;
import tw.com.tm.erp.hbm.dao.AdTaskReviewViewDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCompanyDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopEmployeeDAO;
import tw.com.tm.erp.hbm.dao.DAOFactory;
import tw.com.tm.erp.hbm.dao.GnFileDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;
import tw.com.tm.erp.hbm.dao.PrItemDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
//**********YAO***********
import tw.com.tm.erp.hbm.dao.ShopSetDAO;
//************************
//import tw.com.tm.erp.hbm.dao.TampBuPurchaseLineDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ShopSetService{


	private static final Log log = LogFactory.getLog(ShopSetService.class);
	public static final String PROGRAM_ID= "Shop_Set";
	private BuPurchaseHeadDAO buPurchaseHeadDAO;
	private BuPurchaseLineDAO buPurchaseLineDAO;
	private BaseDAO baseDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	//private BuPurchaseAction buPurchaseAction;
	private BuPurchaseService buPurchaseService;
	private ImStorageAction imStorageAction;
	private ImItemCategoryDAO imItemCategoryDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private AdCategoryLineDAO adCategoryLineDAO;
	private PrItemDAO prItemDAO;
	private BuShopDAO buShopDAO;
	private BuAddressBookDAO buAddressBookDAO;
	private BuEmployeeDAO buEmployeeDAO;
	private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
	private BuBasicDataService buBasicDataService;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private AdTaskReviewViewDAO adTaskReviewViewDAO;
	private BuBrandDAO buBrandDAO;
	private BuEmployeeBrandDAO buEmployeeBrandDAO;
	private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;
	private AdDetailDAO adDetailDAO;
	private CompetenceService competenceService;
	//********lara memo-upload*********
	private GnFileDAO gnFileDAO;
	//********lara memo-upload*********
	private DataSource dataSource;
	private BuShopEmployeeDAO buShopEmployeeDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private SiUsersGroupDAO usersGroupDAO;
	private SiMenuDAO simenuDAO;
	private Date nextStartDay = null;
	private int appendNext = 0;
	private ImItemCategoryService imItemCategoryService;
	private NativeQueryDAO nativeQueryDAO;
	//*********YAO***********
	private ShopSetDAO shopSetDAO;
	//***********************
	public void setShopSetDAO(ShopSetDAO shopSetDAO) {
		this.shopSetDAO = shopSetDAO;
	    }
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	    }
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setSiMenuDAO(SiMenuDAO simenuDAO) {
		this.simenuDAO = simenuDAO;
	}

	public void setSiUsersGroupDAO(SiUsersGroupDAO usersGroupDAO) {
		this.usersGroupDAO = usersGroupDAO;
	}
	public void setCompetenceService(CompetenceService competenceService) {
		this.competenceService = competenceService;
	}
	public void setAdDetailDAO(AdDetailDAO adDetailDAO) {
		this.adDetailDAO = adDetailDAO;
	}
	public void setImWarehouseEmployeeDAO(ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
		this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
	}
	public void setBuEmployeeBrandDAO(BuEmployeeBrandDAO buEmployeeBrandDAO) {
		this.buEmployeeBrandDAO = buEmployeeBrandDAO;
	}
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	public void setAdTaskReviewViewDAO(AdTaskReviewViewDAO adTaskReviewViewDAO) {
		this.adTaskReviewViewDAO = adTaskReviewViewDAO;
	}
	public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}
	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}
	public void setBuEmployeeWithAddressViewService(BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
		this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
	}
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}
	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}
	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
	public void setAdCategoryLineDAO(AdCategoryLineDAO adCategoryLineDAO) {
		this.adCategoryLineDAO = adCategoryLineDAO;
	}
	public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
	}
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	public void setBuPurchaseLineDAO(BuPurchaseLineDAO buPurchaseLineDAO) {
		this.buPurchaseLineDAO = buPurchaseLineDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	//********lara memo-upload********
	public void setGnFileDAO(GnFileDAO gnFileDAO) {
		this.gnFileDAO = gnFileDAO;
	}
	//********lara memo-upload********
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public BuBrandService getBuBrandService() {
		return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	public void setPrItemDAO(PrItemDAO prItemDAO) {
		this.prItemDAO = prItemDAO;
	}
	public void setBuShopEmployeeDAO(BuShopEmployeeDAO buShopEmployeeDAO) {
		this.buShopEmployeeDAO = buShopEmployeeDAO;
	}

	//******lara memo-upload******
	public static final String[] GRID_SEARCH_SHOP_FIELD_NAMES = { 
		"indexNo","enable","shopCode","shopName","brandCode","lineId"
	};
	public static final String[] GRID_SEARCH_SHOP_FIELD_LINE_NAMES_VALUE = {
		"","","","","",""
	};
	
	public static final int[] GRID_SHOP_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};
	
	public static final String[] GNFILE_GRID_FIELD_NAMES = { 
		"indexNo","fileName", "description", "createdBy", "contentType",
		"typeName","physicalName", "physicalPath", "isDeleteRecord", "headId" };

	public static final String[] GNFILE_INCHARGE_GRID_FIELD_NAMES = {
		"indexNo", "fileName", "description", "createdBy", "contentType",
		"typeName","physicalName", "physicalPath", "isDeleteRecord", "headId" };

	public static final String[] GNFILE_GRID_FIELD_DEFAULT_VALUES = { "", "",
		"","", "", "", "", "", "", "" };

	public static final int[] GNFILE_GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG };
	//******lara memo-upload******

	public static final String[] GRID_FIELD_NAMES = { 
		"lineId","indexNo","categoryCode","itemNo", "itemName", "specInfo", 
		"quantity","taxEnable","reUnitPrice", "reTotalAmount","purUnitPrice", "purTotalAmount","supplier","yearBudget","isDeleteRecord"
		,"shopCode","posMachineCode","suppilerCode","suppilerName","assignMenuDateStart","assignMenuTimeStart","supportNo"
		,"executeDateStart","executeTimeStart","executeDateEnd","executeTimeEnd","executeMemo","executeInCharge"
		,"taskType","taskTypeNo","taskDate","execHours","executeTimeStart","status","taskInchargeCode","taskInchargeName"
	};
	public static final String[] GRID_FIELD_VALUES = { 
		"","","","","","", 
		"0","","0", "0","0", "0","","",""
		,"","","","","","","","","","","","",""
		,"","","","0.5","",""
		,"",""
	};

	//******權限**********
	public static final String[] GRID_FIELD_NAMES_KWE_BRAND = { 
		//品牌
		"indexNo","enable","brandCode","brandName","lineId"
	};
	public static final String[] GRID_FIELD_VALUES_KWE_BRAND = { 
		//品牌
		"","","","",""
	};
	public static final int[] GRID_FIELD_TYPES_KWE_BRAND = { 
		//品牌
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};

	public static final String[] GRID_FIELD_NAMES_KWE_WARE = { 
		//庫別
		"indexNo","enable","warehouseCode","lineId"
	};
	public static final String[] GRID_FIELD_VALUES_KWE_WARE = { 
		//庫別
		"","","",""
	};
	public static final int[] GRID_FIELD_TYPES_KWE_WARE = { 
		//庫別
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};

	public static final String[] GRID_FIELD_NAMES_KWE_SHOP = { 
		//店別
		"indexNo","enable","shopCode","shopName","lineId"
	};
	public static final String[] GRID_FIELD_VALUES_KWE_SHOP = { 
		//店別
		"","","","",""
	};
	public static final int[] GRID_FIELD_TYPES_KWE_SHOP = { 
		//店別
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};

	public static final String[] GRID_FIELD_NAMES_KWE_CATEGORY = { 
		//業種
		"indexNo","enable","categoryCode","lineId"
	};
	public static final String[] GRID_FIELD_VALUES_KWE_CATEGORY = { 
		//業種
		"","","",""
	};
	public static final int[] GRID_FIELD_TYPES_KWE_CATEGORY = { 
		//業種
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};
	//*****權限***********
	public static final int[] GRID_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING
		//派工
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING
		//任務
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_DOUBLE
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	};
	public static final Object[][] MASTER_DEFINITION = {
		{ "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
		{ "headId", 		AjaxUtils.FIELD_TYPE_LONG, 	 "0",   "mode:HIDDEN ", "" },
		{ "orderTypeCode",  AjaxUtils.FIELD_TYPE_STRING, "0.0", " ", ""},
		{ "orderNo", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "brandCode", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "request", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "mode:HIDDEN ", "" },
		{ "requestDate",    AjaxUtils.FIELD_TYPE_DATE, 	 "0.0", "", "" },
		{ "classification", AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "project", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "status", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "description", 	AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "depManager", 	AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "createdBy", 		AjaxUtils.FIELD_TYPE_STRING, "0.0", "", "" },
		{ "creationDate", 	AjaxUtils.FIELD_TYPE_DATE, 	 "0.0", "", "" },
	};
	/**
	 * 工作回報查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_LINE_NAMES = { 
		"projectId","incharge","specInfo","executeTimeStart","status","mon","tue","wed","thu","fri","sat","sun","lineId","headId"
	};
	/**
	 * 需求單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderTypeCode","orderNo","requestDate","department","request","contractTel","no","description","status","otherGroup","priority","estimateEndDare","totalHours","headId"
	};
	/**
	 * 工作回報 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_LINE_NAMES_VALUE = {
		"","","","", "","","","","","","","","","",""
	};
	/**
	 * 需求單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_A_FIELD_NAMES = { 
		"lineId","indexNo","enable","menuId","menuName","url","brandCode","cost","categoryCode","warehouseCode"
	};

	public static final int[] GRID_A_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};
	/**
	 * 工作回報 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_A_FIELD_LINE_NAMES_VALUE = {
		"","","","","","","","","",""
	};
	/**
	 * 需求單 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE = {
		"","","", "","","","","","","","","","",""
	};
	//******buBrand******
	public static final String[] GRID_NEW_PRIVILEGE_FIELD_NAMES = { 
		"indexNo", "enable" ,"id.brandCode"
	};

	public static final String[] GRID_NEW_PRIVILEGE_FIELD_DEFAULT_VALUES = {
		"0", "N" ,""
	};
	//******buBrand******

	//******warehouse******
	public static final String[] GRID_NEW_WAREHOSE_FIELD_NAMES = { 
		"indexNo", "enable" ,"warehouseCode","warehouseName"
	};

	public static final String[] GRID_NEW_WAREHOUSE_FIELD_DEFAULT_VALUES = {
		"0", "N" ,"",""
	};
	//******warehouse******
	public static final String[] GRID_SEARCH_FIELD_NAMESSS = {"indexNo","enable","brandCode","brandName"

	};
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUESSS = {"","",""
	};

	public static final String[] GRID_SEARCH_FIELD_NAMESSSHOP = {"indexNo","enable","shopCode","shopCName"

	};
	public static final String[] GRID_SEARCH_FIELD_NAMESSCATEGORY = {"indexNo","enable","id.categoryCode","categoryName"

	};

	/**
	 * 將buPurchase主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	/*public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}*/
	/**
	 * 將buPurchase主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	/*public List<Properties> saveSearchResult1(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_LINE_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}*/
	/**
	 * 產生一筆 BuPurchase
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	/*
	public BuPurchaseHead executeNewBuGoalAchevement() throws Exception {
		BuPurchaseHead form = new BuPurchaseHead();
		form.setOrderTypeCode(null);
		form.setOrderNo(null);
		form.setBrandCode(null);
		form.setRequest(null);
		form.setCreatedBy(null);
		form.setCreationDate(null);
		form.setDepManager(null);
		form.setRequestDate(null);
		form.setClassification(null);
		form.setProject(null);
		form.setDescription(null);
		form.setStatus(null);
		form.setCreatedBy(null);
		form.setCreationDate(null);
		form.setPriority(0.00);
		return form;
	}*/
	/**
	 * 產生一筆 BuPurchase
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	/*public BuPurchaseHead executeNewReturn(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean , "loginBrandCode");
		String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode  = "IRQ";
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart=buEmployee.getEmployeeDepartment();
		
		BuPurchaseHead form = new BuPurchaseHead();
		form.setOrderTypeCode(orderTypeCode);
		//form.setOrderNo(null);
		form.setBrandCode(brandCode);
		form.setRequest(UserUtils.getUsernameByEmployeeCode(employeeCode));
		form.setCreatedBy(employeeCode);
		form.setRequestCode(employeeCode);
		form.setDepartment(depart);		
		form.setRequestDate(DateUtils.parseDate(DateUtils.format(new Date())));
		//form.setClassification(null);
		//form.setProject(null);
		form.setDescription(null);
		form.setStatus("SAVE");		
		form.setCreationDate(DateUtils.parseDate(DateUtils.format(new Date())));
		form.setPriority(0.00);
		saveTmp(form);
		return form;
	}*/
	/**save tmp
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	
	public String saveTmp(ShopSet saveObj) throws FormException, Exception {
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		shopSetDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}
	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap();
		Map multiList = new HashMap(0);
		Object otherBean     = parameterMap.get("vatBeanOther");
		String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
		//String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		
		String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

		try{
			BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
			
			List<AdCategoryHead> allCategroyTypes = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"GROUP","Y"} , "displaySort");	
			List<AdCategoryHead> allproject = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"ITEM","Y" },"displaySort");
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "deptNo","enable" },new Object[] {"103","Y" },"displaySort");
			List<BuCommonPhraseLine> alldepartment = baseDAO.findByProperty( "BuCommonPhraseLine", new String[] {  "id.buCommonPhraseHead.headCode", "enable" },new Object[] {"EmployeeDepartment","Y" }, "indexNo");
			List<PrItem> allitemName = baseDAO.findByProperty( "PrItem", new String[] { "orderTypeCode","enable" },new Object[] {"PC_GROUP","Y"} , "itemId");
			log.info("ssssssssssssss");
			multiList.put("allCategroyTypes"	, AjaxUtils.produceSelectorData(allCategroyTypes, "groupNo", "groupName", false, false,"TASK_REQ"));
			multiList.put("allproject"			, AjaxUtils.produceSelectorData(allproject,"groupNo", "groupName", false, true));
			multiList.put("allsystem"			, AjaxUtils.produceSelectorData(allsystem,"classNo", "className", false, true));
			multiList.put("alldepartment"		, AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
			multiList.put("allitemName"			, AjaxUtils.produceSelectorData(allitemName,"itemName", "itemName", false, true));
			BuBrand     buBrand     = buBrandService.findById( brandCode );
			ShopSet shopSet = null;
			log.info("aaaaaaaaaaa");
			if(formId != null){
				shopSet = findById(formId);
			}else{
				shopSet = createNewShopSet(parameterMap, resultMap, buBrand, buOrderType );
			}
			log.info("BBBBBBBBBBBBBB");
			resultMap.put("form", shopSet);
			resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(shopSet.getCreatedBy()));
			resultMap.put("multiList",         multiList);
			resultMap.put("statusName",OrderStatus.getChineseWord(shopSet.getStatus()));
			resultMap.put("enable","Y");
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("需求初始化失敗，原因：" + ex.toString());         //修正錯誤訊息
			throw new Exception("需求單初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	/*
	public Map executeSearchInitial(Map parameterMap) throws Exception{
		HashMap resultMap    = new HashMap();
		Object otherBean     = parameterMap.get("vatBeanOther");
		String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		try{
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> alldepartment = baseDAO.findByProperty( "BuCommonPhraseLine", new String[] { "id.buCommonPhraseHead.headCode", "enable" },new Object[] {"EmployeeDepartment","Y" }, "indexNo");
			multiList.put("alldepartment", AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
			resultMap.put("multiList",multiList);
			BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
			String depart=buEmployee.getEmployeeDepartment();
			resultMap.put("department"     ,  depart);
			resultMap.put("requestCode",      employeeCode);
			resultMap.put("request",       UserUtils.getUsernameByEmployeeCode(employeeCode));;
			return resultMap;       	
		}catch (Exception ex) {
			log.error("需求單查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("需求單查詢初始化失敗，原因：" + ex.toString());
		}           
	}*/
	/*
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			//Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)||OrderStatus.PLAN.equals(status)||OrderStatus.REPLAN.equals(status)){
				BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				buPurchaseHead.setHeadId(headId);
				int indexNo = buPurchaseLineDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String itemName = upRecord.getProperty("itemName");
						String specInfo = upRecord.getProperty("specInfo");
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						if (StringUtils.hasText(itemName)||StringUtils.hasText(specInfo)){ 
							BuPurchaseLine buPurchaseLine = buPurchaseLineDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+buPurchaseLine);
							if ( null != buPurchaseLine ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(buPurchaseLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								buPurchaseLineDAO.update(buPurchaseLine);
							}else{
								indexNo++;
								BuPurchaseLine line1 = new BuPurchaseLine(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
								log.info("測試:"+buPurchaseHead.getHeadId());
								buPurchaseLineDAO.save(line1);
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}*/
	/** 產生一筆新的 buPurchase 
	 * @param argumentMap
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public ShopSet createNewShopSet(Map parameterMap, Map resultMap, BuBrand buBrand, BuOrderType buOrderType ) throws Exception {
		log.info("createNewPoPurchaseHead");
		Object otherBean      = parameterMap.get("vatBeanOther");
		String brandCode      = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
		String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode  = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		//String loginDepartment  = (String)PropertyUtils.getProperty(otherBean, "loginDepartment");
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart=buEmployee.getEmployeeDepartment();
		String exc=buEmployee.getExtension();
		try{
			ShopSet form = new ShopSet();

			form.setContractTel(           exc);
			form.setBrandCode(            brandCode);
			form.setOrderTypeCode(        orderTypeCode);
			form.setDepartment(           depart);
			form.setCreatedByName(        UserUtils.getUsernameByEmployeeCode(employeeCode));
			form.setRequestCode(          employeeCode);
			form.setCreatedBy(            employeeCode);
			form.setLastUpdatedBy(        employeeCode);
			form.setRequestDate(          DateUtils.parseDate(DateUtils.format(new Date())));
			form.setCreationDate(         DateUtils.parseDate(DateUtils.format(new Date())));
			form.setLastUpdateDate(       DateUtils.parseDate(DateUtils.format(new Date())));
			form.setRequest(              UserUtils.getUsernameByEmployeeCode(employeeCode));
			form.setStatus(               OrderStatus.SAVE);

			saveTmp(form);
			return form;
		}catch (Exception ex) {
			log.error("產生新需求單失敗，原因：" + ex.toString());
			throw new Exception("產生需求單發生錯誤！");
		}
	}
	
	public ShopSet executeFindActualShopSet(Map parameterMap)
	throws FormException, Exception {
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean    = parameterMap.get("vatBeanOther");
		//BuPurchaseHead buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");
		ShopSet shopSet = (ShopSet)parameterMap.get("entityBean");
		log.info("轉正式單號"+shopSet);
		try {
			String formIdString =(String) PropertyUtils.getProperty(formLinkBean, "headId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			log.info("取得formId"+formId);
			String loginEmployeeCode = (String) PropertyUtils.getProperty( otherBean, "loginEmployeeCode");
			shopSet = getActualShopSet(formLinkBean);
			if(shopSet==null){
				throw new NoSuchObjectException("查無專櫃權限設定單主鍵：" + formId + "的資料！");
			} else { // 取得正式的單號
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, shopSet);
				this.setOrderNo(shopSet);
			}
			shopSet.setLastUpdatedBy(loginEmployeeCode);
			shopSet.setLastUpdateDate(new Date());
			shopSetDAO.update(shopSet);
			shopSet = !StringUtils.hasText(formIdString)? this.executeNewReturn(parameterMap): this.findById(formId) ;
			parameterMap.put( "entityBean", shopSet);
			return shopSet;
		} catch (Exception e) {
			log.error("取得需求單主檔失敗,原因:"+e.toString());
			throw new Exception("取得需求單主檔失敗,原因:"+e.toString());
		}
	}
	
	/**
	 * 產生一筆 BuPurchase
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	public ShopSet executeNewReturn(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean , "loginBrandCode");
		String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode  = "SOA";
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart=buEmployee.getEmployeeDepartment();
		
		ShopSet form = new ShopSet();
		form.setOrderTypeCode(orderTypeCode);
		//form.setOrderNo(null);
		form.setBrandCode(brandCode);
		form.setRequest(UserUtils.getUsernameByEmployeeCode(employeeCode));
		form.setCreatedBy(employeeCode);
		form.setRequestCode(employeeCode);
		form.setDepartment(depart);		
		form.setRequestDate(DateUtils.parseDate(DateUtils.format(new Date())));
		//form.setClassification(null);
		//form.setProject(null);
		form.setDescription(null);
		form.setStatus("SAVE");		
		form.setCreationDate(DateUtils.parseDate(DateUtils.format(new Date())));
		form.setPriority(0.00);
		saveTmp(form);
		return form;
	}
	
	private ShopSet getActualShopSet(Object bean) throws FormException, Exception {
		ShopSet shopSet = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualMovement headId=" + id);
		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			shopSet = findById(headId);
			if (shopSet == null) {
				throw new NoSuchObjectException("查無需求單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + shopSet.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的需求單主鍵為空值！");
		}
		return shopSet;
	}
	
	/*
	private BuPurchaseHead getActualBuPurchase(Object bean) throws FormException, Exception {
		BuPurchaseHead buPurchase = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualMovement headId=" + id);
		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			buPurchase = findById(headId);
			if (buPurchase == null) {
				throw new NoSuchObjectException("查無需求單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + buPurchase.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的需求單主鍵為空值！");
		}
		return buPurchase;
	}*/
	
	public ShopSet findById(Long headId) throws Exception {
		try {
			ShopSet shopSet = (ShopSet)shopSetDAO.findByPrimaryKey(ShopSet.class, headId);
			return shopSet;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢資料時發生錯誤，原因：" + ex.toString()); //修正錯誤訊息
			throw new Exception("依據主鍵：" + headId + "查詢資料時發生錯誤，原因："+ ex.getMessage());
		}
	}
	
	public ShopSet executeFind(Long headId) throws Exception {
		try {
			ShopSet shopSet = (ShopSet)shopSetDAO.findByPrimaryKey(ShopSet.class, headId);
			return shopSet;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢資料時發生錯誤，原因：" + ex.toString()); //修正錯誤訊息
			throw new Exception("依據主鍵：" + headId + "查詢資料時發生錯誤，原因："+ ex.getMessage());
		}
	}
	/*
	public List<BuPurchaseHead> find(BuPurchaseHead buPurchase)
	throws IllegalAccessException, InvocationTargetException,
	IllegalArgumentException, SecurityException, NoSuchMethodException,
	ClassNotFoundException {
		BuPurchaseHead searchObj = new BuPurchaseHead();
		BeanUtils.copyProperties(buPurchase, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getHeadId()) {
			temp.add(buPurchaseHeadDAO.findByPrimaryKey(BuPurchaseHead.class,
					buPurchase.getHeadId()));
		} else {
			temp = buPurchaseHeadDAO.findByExample(searchObj);
		}
		return temp;
	}*/
	/**
	 * 驗證主檔
	 * @param parameterMap
	 * @throws Exception
	 */
	public void validateHead(Map parameterMap) throws Exception {   //驗證沒有功能
		Object formBindBean  = parameterMap.get("vatBeanFormBind");
		String contractTel   = (String) PropertyUtils.getProperty(formBindBean, "contractTel");
		String request       = (String) PropertyUtils.getProperty(formBindBean, "request");
		String no            = (String) PropertyUtils.getProperty(formBindBean, "no");
		String depManager    = (String) PropertyUtils.getProperty(formBindBean, "depManager");
		String categorySystem= (String) PropertyUtils.getProperty(formBindBean, "categorySystem");
		
		
		// 驗證
		if(!StringUtils.hasText(request)){
			throw new ValidationErrorException("請輸入需求人員");
		}
		if(!StringUtils.hasText(no)){
			throw new ValidationErrorException("請輸入主旨");
		}
		if(!StringUtils.hasText(contractTel)){
			throw new ValidationErrorException("請輸入分機");
		}
		if(!StringUtils.hasText(depManager)){
			throw new ValidationErrorException("請輸入需求主管");
		}
		if(!StringUtils.hasText(categorySystem)){
			throw new ValidationErrorException("請選擇系統項目");
		}
	}
	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	/*
	public void updateBuGoalAchevementBean(Map parameterMap)throws FormException, Exception {
		try{
			BuPurchaseHead buPurchase= null;
			buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");
			parameterMap.put("entityBean", buPurchase);
		}catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	*/
	/**
	 * 需求單存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalAchevement(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String  resultMsg  = null;
		Date date = new Date();
		String tmp = null ;
		Object otherBean = parameterMap.get("vatBeanOther");
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

		String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");//前端傳來的參數
		//BuPurchaseHead buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");
		ShopSet shopSet = (ShopSet)parameterMap.get("entityBean");
		String createBy = shopSet.getCreatedBy();
		String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");
		String role = (String) PropertyUtils.getProperty(formBindBean, "role");
		log.info("formStatus~~~~~~~~~"+formStatus);
		String nextStatus =  getStatus(orderTypeCode,formAction,shopSet.getStatus());
		log.info("nextStatus~~~~~~~~~"+nextStatus);
		String brandCode      = (String)PropertyUtils.getProperty(otherBean, "brandCode");	

		//發信用
		try {
			BuEmployee emp = buEmployeeDAO.findById((String) PropertyUtils.getProperty(formBindBean, "applicant"));
			
			
			log.info("測試role:"+emp.getEmployeeRole());
			log.info("formStatus: " + formStatus + "----------------");
			if("SIGNING".equals(formStatus)){
				Long a = this.updateCompetenceData(shopSet.getHeadId(),loginEmployeeCode,emp.getEmployeeRole(),brandCode);
				log.info(a);
			}
				

			tmp=(loginEmployeeCode+","+loginEmployeeCode+","+loginEmployeeCode+","+loginEmployeeCode);
			shopSet.setLastUpdatedBy(loginEmployeeCode);
			shopSet.setStatusLog(tmp);
			shopSet.setStatus(nextStatus);
			shopSet.setLastUpdateDate(date);
			//buPurchase.setProcessId(processId);
			log.info("deleteMarkLine");
			//removeAJAXLine(buPurchase);//刪除mark掉的Line
			shopSetDAO.merge(shopSet);

			resultMsg = "OrderTypeCode：" + shopSet.getOrderTypeCode() + "存檔成功！ 是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", shopSet);
			resultMap.put("vatMessage", msgBox);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("需求維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("需求維護單單存檔失敗，原因：" + ex.toString());
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
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			log.info("headId___"+headId);
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			List buPurchaseLines = buPurchaseLineDAO.findPageLine(headId, iSPage, iPSize);
			log.info("buPurchases.size"+ buPurchaseLines.size());	
			if (buPurchaseLines != null && buPurchaseLines.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				BuPurchaseLine a = (BuPurchaseLine)buPurchaseLines.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = buPurchaseLineDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_VALUES, buPurchaseLines, gridDatas, firstIndex, maxIndex));
			}else {
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_VALUES,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageDataAddetail(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String oriMenuName = "";
			String parMenuName = "";
			AdDetail ad = null;
			log.info("getAJAXPageDataAddetail.headId:"+headId);

			//Steve
			List buPurchaseLines = buPurchaseLineDAO.findPageLineAdDetail(headId, iSPage, iPSize);
			log.info("buPurchases.size"+ buPurchaseLines.size());	
			if (buPurchaseLines != null && buPurchaseLines.size() > 0) {
				/*	
				for(int i=0;i<buPurchaseLines.size();i++){
					ad = (AdDetail)buPurchaseLines.get(i);
					SiMenu siMenu = simenuDAO.findById(ad.getMenuId());
					if(siMenu!=null){
						oriMenuName = siMenu.getName();
						if(siMenu.getParentMenuId()!=null&&siMenu.getParentMenuId().toString()!=""){
							SiMenu siMenuPar = simenuDAO.findById(siMenu.getParentMenuId().toString());
							parMenuName = siMenuPar.getName();
							ad.setMenuName(parMenuName+"-->"+oriMenuName);
						}else{
							log.info("沒有此menu:"+ad.getMenuId());
							continue;
						}
					}
				}
				 */
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)buPurchaseLines.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_A_FIELD_NAMES, GRID_SEARCH_A_FIELD_LINE_NAMES_VALUE, buPurchaseLines, gridDatas, firstIndex, maxIndex));
			}else {
				log.info("跑Default:");
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_A_FIELD_NAMES, GRID_SEARCH_A_FIELD_LINE_NAMES_VALUE,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	/**
	 * ajax 取得BUPURCHASE查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXShopSetSearchPageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值=========================
			HashMap searchMap = getSearchMap(httpRequest);
			//==============================================================	    
			//List<BuPurchaseHead> puHeads = (List<BuPurchaseHead>) buPurchaseHeadDAO.findPageLine(searchMap, 
					//iSPage, iPSize, BuPurchaseHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			List<ShopSet> shopSets = (List<ShopSet>) shopSetDAO.findPageLine(searchMap, 
					iSPage, iPSize, ShopSetDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			
			log.info("shopSets.size"+ shopSets.size());	
			if (shopSets != null && shopSets.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseHeadLineOtherColumn(puHeads);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
				Long maxIndex = (Long)shopSetDAO.findPageLine(searchMap, -1, iPSize, 
						ShopSetDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, shopSets, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, gridDatas));
			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的shopSet發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的shopSet");
		}	
	}
	
	public HashMap getSearchMap(Properties httpRequest){
		HashMap searchMap = new HashMap();
		String startDate  = httpRequest.getProperty("startDate");
		String endDate    = httpRequest.getProperty("endDate");
		String notshowotherGroup    = httpRequest.getProperty("notshowotherGroup");
		String isshowotherGroup    = httpRequest.getProperty("isshowotherGroup");
		Date requestSDate = null;
		Date requestEDate = null;
		if(StringUtils.hasText(startDate))
			requestSDate = DateUtils.parseDate("yyyy/MM/dd", startDate);
		if(StringUtils.hasText(endDate))
			requestEDate = DateUtils.parseDate("yyyy/MM/dd", endDate);
		searchMap.put("brandCode",     		httpRequest.getProperty("brandCode"));
		searchMap.put("orderTypeCode", 		httpRequest.getProperty("orderTypeCode"));
		searchMap.put("startDate",     		requestSDate);
		searchMap.put("endDate",     		requestEDate);
		searchMap.put("rqInChargeCode",     httpRequest.getProperty("rqInChargeCode"));
		searchMap.put("otherGroup",         httpRequest.getProperty("otherGroup"));
		searchMap.put("status",        		httpRequest.getProperty("status"));
		searchMap.put("orderNo",  			httpRequest.getProperty("orderNo"));
		searchMap.put("request",    		httpRequest.getProperty("request")); 	   
		searchMap.put("no",                "%"+httpRequest.getProperty("no")+"%");
		searchMap.put("requestCode",	    httpRequest.getProperty("requestCode"));
		searchMap.put("department", 		httpRequest.getProperty("department"));  	    
		searchMap.put("createdBy",    	    httpRequest.getProperty("createdBy"));
		searchMap.put("categoryGroup",    	httpRequest.getProperty("categoryGroup"));
		searchMap.put("notshowotherGroup",  notshowotherGroup);
		searchMap.put("isshowotherGroup",   isshowotherGroup);
		return searchMap;
	}
	/**
	 * 設定bupurchase額外欄位
	 * @param buGoalAchevements
	 */
	/*
	private void setBuPurchaseHeadLineOtherColumn(List<BuPurchaseHead> buPurchases){
		for (BuPurchaseHead buPurchase : buPurchases) {
			buPurchase.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buPurchase.getLastUpdatedBy()));
		}
	}
	*/
	/*
	private void setBuPurchaseLineOtherColumn(List<BuPurchaseLine> buPurchases){
		for (BuPurchaseLine buPurchase : buPurchases) {
			buPurchase.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buPurchase.getLastUpdatedBy()));
		}
	}
	*/
	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(ShopSet head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
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
				ex.printStackTrace();
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	/**
	 * by headId 取得bean
	 */
	/*
	public BuPurchaseHead getBeanByHeadId(Long headId) throws Exception {
		try {
			BuPurchaseHead head = (BuPurchaseHead)buPurchaseHeadDAO.findByPrimaryKey(BuPurchaseHead.class,headId);
			return head;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	*/
	/**
	 * LINE 頁面顯示
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List findPageLine(Long headId, int startPage, int pageSize) {
		log.info("PoPurchaseOrderLineMainService.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return buPurchaseLineDAO.findPageLine(headId, startPage, pageSize);
	}
	/**
	 * 取得指定連動的類別下拉
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXCategory(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String categoryItem = httpRequest.getProperty("categoryItem");
			log.info("categoryItem = " + categoryItem);
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "groupNo" ,"enable" },new Object[] {categoryItem,"Y" }, "displaySort");
			allsystem = AjaxUtils.produceSelectorData(allsystem,"classNo", "className", false, true); 
			properties.setProperty("allsystem", AjaxUtils.parseSelectorData(allsystem));
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}
	
	public List<Properties> executeCopyOriShop(Properties httpRequest) throws Exception{
		log.info("executeCopyOriShop");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
    	PreparedStatement stmt = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rs = null;
    	ResultSet rs1 = null;
    	Long count = 0L;
    	Map resultMap = new HashMap(0);
		ShopSet shopSet = null;
		BuEmployee buEmp = null;
		BuEmployee buBoss = null;
		ArrayList bossList = new ArrayList();
		String costCtrl = "";
		String wareHouseCtrl = "";
		String empMenuId = null;
		try{
			//======================取得複製時所需的必要資訊========================
			
			
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");// 要顯示的HEAD_ID
			String employeeCode = httpRequest.getProperty("employeeCode");
			String bossCode = httpRequest.getProperty("bossCode");
			log.info("申請人工號:"+bossCode);
			this.deleteShopSetDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{
				  buBoss = (BuEmployee)buEmployeeDAO.findById(bossCode);	
				  buEmp = (BuEmployee)buEmployeeDAO.findById(employeeCode);
				  costCtrl = buEmp.getCostControl();
				  wareHouseCtrl = buEmp.getWarehouseControl();
			}
			
			
			
			//log.info("emp屬性:"+buBoss.getEmployeeRole());
			
			System.out.println("===========BEGIN============" + new Date());
    		conn = dataSource.getConnection();
    		
    		String sql = "SELECT * FROM ERP.BU_SHOP A , ERP.BU_SHOP_EMPLOYEE B WHERE A.SHOP_CODE = B.SHOP_CODE AND B.EMPLOYEE_CODE = ? and A.BRAND_CODE= ? and A.ENABLE='Y' ORDER BY A.SHOP_CODE";
    		             
    		log.info("sql:"+sql+" BABU:"+buEmp.getEmployeeRole());
    		stmt = conn.prepareStatement(sql);
    		log.info("取出庫別數量1:");
    		//stmt.setString(1, buEmp.getEmployeeRole());
    		stmt.setString(1, buEmp.getEmployeeRole());
    		log.info("取出庫別數量2:");
    		stmt.setString(2, brandCode);
    		log.info("取出庫別數量3:");
    		//stmt.setString(2, "TEST1");
    		//stmt.setString(3, "Y");
    		//log.info("sql塞參數ok:"+buBoss.getEmployeeRole());
    		rs = stmt.executeQuery();
    		
    		
    		//log.info("sql執行ok:"+headId);
    		
    			stmt1 = conn.prepareStatement(sql);
    			if(bossCode.equals("SUPER")){
    			   stmt1.setString(1,"T17888");	
    			}else{
        		   stmt1.setString(1, buBoss.getEmployeeRole());
    			}
        		   stmt1.setString(2, brandCode);
        		   rs1 = stmt1.executeQuery();
    		
        	if (rs != null) {
        		log.info("DDDD:");
        		while (rs.next()){
        			log.info("eeeee:");
        			bossList.add(rs.getString("SHOP_CODE"));
        		}
        	}
        	log.info("FFFF:");
        	
        	
        	//for(Object menuId:bossList){
    		//log.info("bossList:"+menuId.toString());
        	//}
    		if (rs1 != null) {
    			shopSet = this.findById(Long.parseLong(headId));
    			List<ShopSetDetail> shopSetDetails = shopSet.getShopSetDetails();
    			log.info("shopSetDetails的數量:"+shopSetDetails.size());
    			while (rs1.next()) {	
    				
    				
    				count++;
    				log.info("adDetailDAO:"+count+" "+headId);
    				ShopSetDetail ssd = new ShopSetDetail();
    				log.info("初始化shopSetDetail:");
    				ssd.setBrandCode(rs1.getString("BRAND_CODE"));
    				log.info("初始化1");
    				ssd.setBrandName("T2");
    				log.info("初始化2");
    				ssd.setCategoryCode("");
    				log.info("初始化3");
    				ssd.setCategoryName("");
    				log.info("初始化4");
    				ssd.setIndexNo(count);
    				log.info("初始化5");
    				//ad.setHeadId(Long.parseLong(headId));
    				for(Object shopCode:bossList){
    					if(rs1.getString("SHOP_CODE").equals(String.valueOf(shopCode))){
    						log.info("設定Enable:Y");
    						ssd.setEnable("Y");
    					    break;
    					}
    				}
    				log.info("初始化7");
    				ssd.setType("Shop");
    				log.info("初始化8");
    				ssd.setShopCode(rs1.getString("SHOP_CODE"));
    				log.info("初始化9");
    				ssd.setShopName(rs1.getString("SHOP_C_NAME"));
    				log.info("初始化10");
    				
    				//log.info("ppppppppppppppp:"+rs1.getString("SHOP_ID"));
    				
    				ssd.setEmployeeCode(employeeCode);
    				//ad.setBuPurchaseHead(buPurchaseHead);
    				//SiMenuCtrl simenuCtrl = simenuDAO.findById(rs1.getString("MENU_ID"));
    				//log.info("成本:"+simenuCtrl.getMenuId()+" "+simenuCtrl.getCostControl()+" "+costCtrl);
    				
    				//adDetailDAO.save(ad);
    				shopSetDetails.add(ssd);
    			  	
    			}
    			log.info("bbbbb adDetails:"+shopSetDetails.size());
    			shopSet.setShopSetDetails(shopSetDetails);
    			shopSetDAO.update(shopSet);
    		}
    		
    		
			
		}catch(Exception ex){
			log.error("複製權限申請單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製權限申請單別發生錯誤，原因：" + ex.getMessage());
			
			
			
		}finally {
    		if (rs != null) {
    			try {
    				rs.close();
    			} catch (SQLException e) {
    				System.out.println("關閉ResultSet時發生錯誤！");
    			}
    		}
    		if (stmt != null) {
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				System.out.println("關閉PreparedStatement時發生錯誤！");
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				System.out.println("關閉Connection時發生錯誤！");
    			}
    		}
    	}
		return result;
		
	}
	
	/**AJAX Line Change
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	/*public List<Properties> getAJAXLineData(Properties httpRequest) throws FormException, Exception {
		List re = new ArrayList();
		Properties pru = new Properties();
		try{
			String itemNo     = httpRequest.getProperty("itemNo");
			String itemName   = httpRequest.getProperty("itemName");
			String specInfo   = httpRequest.getProperty("specInfo");
			String supplier   = httpRequest.getProperty("supplier");
			if (StringUtils.hasText(itemNo)){
				PrItem prItem = prItemDAO.findItem(itemNo);
				if(null!=prItem){
					pru.setProperty("itemName",    AjaxUtils.getPropertiesValue(prItem.getItemName(),     itemName));
					//pru.setProperty("purUnitPrice",AjaxUtils.getPropertiesValue(prItem.getPurUnitPrice(), "purUnitPrice"));
					//pru.setProperty("reUnitPrice", AjaxUtils.getPropertiesValue(prItem.getReUnitPrice(),  "reUnitPrice"));
					pru.setProperty("specInfo",    AjaxUtils.getPropertiesValue(prItem.getSpecInfo(),     specInfo));
					//pru.setProperty("supplier",    AjaxUtils.getPropertiesValue(prItem.getSupplier(),     supplier));
					re.add(pru);
				}else
				{
					pru.setProperty("itemName",      " ");
					pru.setProperty("purUnitPrice",  " ");
					pru.setProperty("reUnitPrice",   " ");
					pru.setProperty("specInfo",      " ");
					pru.setProperty("supplier",      " ");
				}
			} 
		}catch(Exception e){
			log.error(e.toString());
		}
		return re;
	}*/
	/**AJAX Line Change
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	public List<Properties> getAJAXLineData1(Properties httpRequest) throws FormException, Exception {
		Properties pro = new Properties();
		List re = new ArrayList();
		String taskInchargeCode     = httpRequest.getProperty("taskInchargeCode");
		BuEmployeeWithAddressView buEmployeeWithAddressView = buEmployeeWithAddressViewService.findById(taskInchargeCode);
		String chname = buEmployeeWithAddressView.getChineseName();
		log.info("ChineseName"+chname);
		if (null != taskInchargeCode ) {
			pro.setProperty("taskInchargeCode",    AjaxUtils.getPropertiesValue(buEmployeeWithAddressView.getEmployeeCode(), " "));
			pro.setProperty("taskInchargeName",    AjaxUtils.getPropertiesValue(chname, "chname"));
		} else {
			pro.setProperty("taskInchargeCode",    " ");
			pro.setProperty("taskInchargeName",    " ");
		}
		re.add(pro);
		return re;
	}
	/**
	 * 把標記為刪除的資料刪除
	 * 
	 * @param salesOrderHead
	 */
	/*
	private void removeAJAXLine(BuPurchaseHead buPurchase) {
		List<BuPurchaseLine> lines = buPurchase.getBuPurchaseLines();
		if(lines != null && lines.size() > 0){
			for(int i = lines.size() - 1; i >= 0; i--){
				BuPurchaseLine line = lines.get(i);         
				if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(line.getIsDeleteRecord())){
					lines.remove(line);
				}
			}
		}
		for (int i = 0; i < lines.size(); i++) {
			BuPurchaseLine line = lines.get(i); 
			line.setIndexNo(i+1L);
		}
	}
	*/
	/** 處理AJAX 任務-代出執行人 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataBySupplier(Properties httpRequest)throws Exception {
		Properties pro = new Properties();
		List re = new ArrayList();
		String request     		= httpRequest.getProperty("request");
		String brandCode        = httpRequest.getProperty("brandCode");
		String employeeCode     = httpRequest.getProperty("employeeCode");
		String organizationCode = httpRequest.getProperty("organizationCode");
		if( StringUtils.hasText(request)) {
			BuEmployeeWithAddressView buEmployeeWithAddressView = 
				buEmployeeWithAddressViewService.findbyBrandCodeAndEmployeeCode(brandCode, employeeCode );
			employeeCode = buEmployeeWithAddressView.getChineseName();
		}
		BuEmployeeWithAddressView buSWAV = buBasicDataService.findEnableEmployeeById(organizationCode, employeeCode);
		if (null != request ) {
			pro.setProperty("requestCode",    AjaxUtils.getPropertiesValue(buSWAV.getEmployeeCode(), ""));
		} else {
			pro.setProperty("requestCode",    " ");
			pro.setProperty("request",        " ");
		}
		re.add(pro);
		return re;
	}
	
	/**建立人AJAX 需求人員 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByCreatedBy(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String createdBy = httpRequest.getProperty("createdBy");
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();

		if(StringUtils.hasText(createdBy)){
			List re = new ArrayList();
			findObjs.put("employeeCode", createdBy);
			findObjs.put("englishName", null);
			findObjs.put("chineseName", null);
			findObjs.put("tel1", null);
			//log.info("employeeCode="+createdBy);
			//log.info("englishName="+null);
			//log.info("chineseName="+null);
			//log.info("employee========="+createdBy);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//log.info("employee=========11111==="+employees.size());
			if(employees.size()==0){
				findObjs.put("employeeCode", null);
				findObjs.put("englishName", createdBy);
				findObjs.put("chineseName", null);
				findObjs.put("tel1", null);
				//找EN
				employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
				if(employees.size()==0){
					findObjs.put("employeeCode", null);
					findObjs.put("chineseName", createdBy);
					findObjs.put("englishName", null);
					findObjs.put("tel1", null);
					//找姓名
					employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

					if(employees.size()==0){
						findObjs.put("employeeCode", null);
						findObjs.put("chineseName", null);
						findObjs.put("englishName", null);
						findObjs.put("tel1", createdBy);
						//找電話
						employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

						if(employees.size()==0){
							inc.setProperty("createdBy", createdBy);
							inc.setProperty("createdByName", "unknow");
						}
						else
						{
							employee = employees.get(0);
							inc.setProperty("createdBy", employee.getEmployeeCode());
							inc.setProperty("createdByName", employee.getChineseName());
						}
					}
					else
					{
						employee = employees.get(0);
						inc.setProperty("createdBy", employee.getEmployeeCode());
						inc.setProperty("createdByName", employee.getChineseName());
					}
				}
				else
				{
					employee = employees.get(0);
					inc.setProperty("createdBy", employee.getEmployeeCode());
					inc.setProperty("createdByName", employee.getChineseName());
				}
			}
			else
			{
				employee = employees.get(0);
				inc.setProperty("createdBy", employee.getEmployeeCode());
				inc.setProperty("createdByName", employee.getChineseName());
			}
			re.add(inc);
			return re;
		}else
		{
			List re1 = new ArrayList();
			inc.setProperty("createdBy", null);
			inc.setProperty("createdByName", "unknow");
			re1.add(inc);
			return re1;
		}
	}
	/**處理AJAX 需求人員 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByRequest(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String requestCode = httpRequest.getProperty("requestCode"); 
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();	
		MessageFormat messageFormat = new MessageFormat("Data-{0,number,#}-{1}-{2}-{3,number,#}.xml");


		if(StringUtils.hasText(requestCode))
		{
			List re = new ArrayList();
			findObjs.put("employeeCode", requestCode);
			findObjs.put("englishName", null);
			findObjs.put("chineseName", null);
			findObjs.put("tel1", null);
			findObjs.put("employeeDepartment", null);
			//log.info("employeeCode="+requestCode);
			//log.info("englishName="+null);
			//log.info("chineseName="+null);
			//log.info("employee========="+requestCode);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//log.info("employee=========11111==="+employees.size());
			if(employees.size()==0){
				findObjs.put("employeeCode", null);
				findObjs.put("englishName", requestCode);
				findObjs.put("chineseName", null);
				findObjs.put("tel1", null);
				findObjs.put("employeeDepartment", null);
				//找EN
				employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
				if(employees.size()==0){
					findObjs.put("employeeCode", null);
					findObjs.put("chineseName", requestCode);
					findObjs.put("englishName", null);
					findObjs.put("tel1", null);
					findObjs.put("employeeDepartment", null);
					//找姓名
					employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

					if(employees.size()==0){
						findObjs.put("employeeCode", null);
						findObjs.put("chineseName", null);
						findObjs.put("englishName", null);
						findObjs.put("tel1", requestCode);
						findObjs.put("employeeDepartment", null);
						//找電話
						employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

						if(employees.size()==0){
							findObjs.put("employeeCode", null);
							findObjs.put("chineseName", null);
							findObjs.put("englishName", null);
							findObjs.put("tel1", null);
							findObjs.put("employeeDepartment", requestCode);
							//找電話
							employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

							if(employees.size()==0){
								inc.setProperty("requestCode", requestCode);
								inc.setProperty("request", "unknow");
							}
							else
							{
								employee = employees.get(0);
								inc.setProperty("requestCode", employee.getEmployeeCode());
								inc.setProperty("request", employee.getChineseName());
								inc.setProperty("department", employee.getEmployeeDepartment());
							}
						}
						else
						{
							employee = employees.get(0);
							inc.setProperty("requestCode", employee.getEmployeeCode());
							inc.setProperty("request", employee.getChineseName());
							inc.setProperty("department", employee.getEmployeeDepartment());
						}
					}
					else
					{
						employee = employees.get(0);
						inc.setProperty("requestCode", employee.getEmployeeCode());
						inc.setProperty("request", employee.getChineseName());
						inc.setProperty("department", employee.getEmployeeDepartment());
					}
				}
				else
				{
					employee = employees.get(0);
					inc.setProperty("requestCode", employee.getEmployeeCode());
					inc.setProperty("request",employee.getChineseName()+"O");
					inc.setProperty("department", employee.getEmployeeDepartment());
				}
			}
			else
			{
				employee = employees.get(0);
				inc.setProperty("requestCode", employee.getEmployeeCode());
				inc.setProperty("department",employee.getChineseName());
				inc.setProperty("request",employee.getChineseName());
			}
			re.add(inc);
			log.info("re~~~"+re);
			return re;
		}else{
			List re1 = new ArrayList();
			inc.setProperty("requestCode", null);
			inc.setProperty("request", "unknow");
			re1.add(inc);
			log.info("rel~~~"+re1);
			return re1;
		}

	}
	/**處理AJAX 採購負責人 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByRequestCode(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		Properties pro = new Properties();
		List re = new ArrayList();
		String request = httpRequest.getProperty("request");
		pro.setProperty("requestCode", UserUtils.getUsernameByEmployeeCode(request));
		re.add(pro);
		return re;
	}
	/**處理AJAX 需求主管 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByDepManager(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String depManager = httpRequest.getProperty("depManager");
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();
		if(StringUtils.hasText(depManager))
		{
			List re = new ArrayList();
			findObjs.put("employeeCode", depManager);
			findObjs.put("englishName", null);
			findObjs.put("chineseName", null);
			findObjs.put("tel1", null);
			//log.info("employeeCode="+depManager);
			//log.info("englishName="+null);
			//log.info("chineseName="+null);
			//log.info("employee========="+depManager);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//log.info("employee=========11111==="+employees.size());
			if(employees.size()==0){
				findObjs.put("employeeCode", null);
				findObjs.put("englishName", depManager);
				findObjs.put("chineseName", null);
				findObjs.put("tel1", null);
				//找EN
				employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
				if(employees.size()==0){
					findObjs.put("employeeCode", null);
					findObjs.put("chineseName", depManager);
					findObjs.put("englishName", null);
					findObjs.put("tel1", null);
					//找姓名
					employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

					if(employees.size()==0){
						findObjs.put("employeeCode", null);
						findObjs.put("chineseName", null);
						findObjs.put("englishName", null);
						findObjs.put("tel1", depManager);
						//找電話
						employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
						if(employees.size()==0){
							inc.setProperty("depManager", depManager);
							inc.setProperty("depManagerName", "unknow");
						}
						else
						{
							employee = employees.get(0);
							inc.setProperty("depManager", employee.getEmployeeCode());
							inc.setProperty("depManagerName", employee.getChineseName());
						}
					}
					else
					{
						employee = employees.get(0);
						inc.setProperty("depManager", employee.getEmployeeCode());
						inc.setProperty("depManagerName", employee.getChineseName());
					}
				}
				else
				{
					employee = employees.get(0);
					inc.setProperty("depManager", employee.getEmployeeCode());
					inc.setProperty("depManagerName", employee.getChineseName());
				}
			}
			else
			{
				employee = employees.get(0);
				BuEmployee emp = buEmployeeDAO.findById(depManager);
				inc.setProperty("depManager", employee.getEmployeeCode());
				inc.setProperty("depManagerName", employee.getChineseName());
				inc.setProperty("dept", emp.getEmployeeDepartment());
				log.info("確認是否同部門:"+emp.getEmployeeDepartment());
			}
			re.add(inc);
			return re;
		}else
		{
			List re1 = new ArrayList();
			inc.setProperty("depManager", null);
			inc.setProperty("depManagerName", "unknow");
			re1.add(inc);
			return re1;
		}
	}
	/**處理AJAX 處理人 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByrqInChargeCode(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String rqInChargeCode = httpRequest.getProperty("rqInChargeCode");
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();
		if(StringUtils.hasText(rqInChargeCode))
		{
			List re = new ArrayList();
			findObjs.put("employeeCode", rqInChargeCode);
			findObjs.put("englishName", null);
			findObjs.put("chineseName", null);
			findObjs.put("tel1", null);
			//log.info("employeeCode="+rqInChargeCode);
			//log.info("englishName="+null);
			//log.info("chineseName="+null);
			//log.info("employee========="+rqInChargeCode);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//log.info("employee=========11111"+employees.size());
			if(employees.size()==0){
				findObjs.put("employeeCode", null);
				findObjs.put("chineseName", null);
				findObjs.put("englishName", rqInChargeCode);
				findObjs.put("tel1", null);
				//找EN
				employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
				if(employees.size()==0){
					findObjs.put("employeeCode", null);
					findObjs.put("chineseName", rqInChargeCode);
					findObjs.put("englishName", null);
					findObjs.put("tel1", null);
					//找姓名
					employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
					if(employees.size()==0){
						findObjs.put("employeeCode", null);
						findObjs.put("chineseName", null);
						findObjs.put("englishName", null);
						findObjs.put("tel1", rqInChargeCode);
						//找電話
						employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

						if(employees.size()==0){
							inc.setProperty("rqInChargeCode", rqInChargeCode);
							inc.setProperty("otherGroup", "unknow");
						}
						else
						{
							employee = employees.get(0);
							inc.setProperty("rqInChargeCode", employee.getEmployeeCode());
							inc.setProperty("otherGroup", employee.getChineseName());
						}
					}
					else
					{
						employee = employees.get(0);
						inc.setProperty("rqInChargeCode", employee.getEmployeeCode());
						inc.setProperty("otherGroup", employee.getChineseName());
					}
				}
				else
				{
					employee = employees.get(0);
					inc.setProperty("rqInChargeCode", employee.getEmployeeCode());
					inc.setProperty("otherGroup", employee.getChineseName());
				}
			}
			else
			{
				employee = employees.get(0);
				inc.setProperty("rqInChargeCode", employee.getEmployeeCode());
				inc.setProperty("otherGroup", employee.getChineseName());
			}
			re.add(inc);
			return re;
		}else
		{
			List re1 = new ArrayList();
			inc.setProperty("rqInChargeCode", null);
			inc.setProperty("otherGroup", "unknow");
			re1.add(inc);
			return re1;
		}
	}
	/**
	 * 由EmployeeId來取得登入名稱
	 * 
	 * @param employeeId
	 * @return username
	 */
	public List<Properties> getUsernameByEmployeeCode(String rqInChargeCode) {
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();
		List re = new ArrayList();

		findObjs.put("employeeCode", rqInChargeCode);
		findObjs.put("chineseName", null);
		findObjs.put("englishName", null);
		BuEmployeeWithAddressView employee = null;

		//找工號
		employee = (BuEmployeeWithAddressView) getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		//找EN
		//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		//找姓名
		//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		//log.info("employee=========11111"+employee);
		if(employee.getEmployeeCode()==null){
			findObjs.put("employeeCode", null);
			findObjs.put("chineseName", null);
			findObjs.put("englishName", rqInChargeCode);
			//找EN
			employee = (BuEmployeeWithAddressView)  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);

			if(employee.getEmployeeCode()==null){
				findObjs.put("employeeCode", null);
				findObjs.put("chineseName", rqInChargeCode);
				findObjs.put("englishName", null);
				//找姓名
				employee = (BuEmployeeWithAddressView)  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);

				if(employee.getEmployeeCode()==null){

					inc.setProperty("otherGroup", rqInChargeCode);
					inc.setProperty("NAME", "unknow");
				}
				else
				{
					inc.setProperty("otherGroup", employee.getEmployeeCode());
					inc.setProperty("NAME", employee.getChineseName());

				}
			}
			else
			{
				inc.setProperty("otherGroup", employee.getEmployeeCode());
				inc.setProperty("NAME", employee.getChineseName());
			}	
		}
		else
		{
			inc.setProperty("otherGroup", employee.getEmployeeCode());
			inc.setProperty("NAME", employee.getChineseName());
		}

		re.add(inc);			
		return re;
	}
	private static BuEmployeeWithAddressViewDAO getBuEmployeeWithAddressViewDAO() {
		return DAOFactory.getInstance().getBuEmployeeWithAddressViewDAO();
	}
	/**處理AJAX 執行人 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataBytaskInchargeCode(Properties httpRequest) {
		log.info("getAJAXFormDataBySuperintendent");
		Properties inc = new Properties();
		List re = new ArrayList();
		String taskInchargeCode = httpRequest.getProperty("taskInchargeCode");
		inc.setProperty("taskInchargeName", UserUtils.getUsernameByEmployeeCode(taskInchargeCode));
		re.add(inc);
		return re;
	}
	/**
	 * ajax 取得工作回報查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXBuPurchaseSearchLinePageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值，製作Map=========================
			HashMap searchMap = getSearchLineMap(httpRequest);
			//==============================================================    	   
			List<AdTaskReviewView> adTaskReviewViews = 
				(List<AdTaskReviewView>)adTaskReviewViewDAO.findPageLine(searchMap, 
						iSPage, iPSize, AdTaskReviewViewDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			if (adTaskReviewViews != null && adTaskReviewViews.size() > 0) {    	    
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
				Long maxIndex = (Long)adTaskReviewViewDAO.findPageLine(searchMap, -1, iPSize, 
						AdTaskReviewViewDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_LINE_NAMES, GRID_SEARCH_FIELD_LINE_NAMES_VALUE, adTaskReviewViews, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_LINE_NAMES, GRID_SEARCH_FIELD_LINE_NAMES_VALUE, gridDatas));
			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的achevement發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的achevement");
		}	
	}
	public HashMap getSearchLineMap(Properties httpRequest){
		HashMap searchMap = new HashMap();
		String fristDate  = httpRequest.getProperty("startDate");
		String lastDate    = httpRequest.getProperty("endDate");

		Date countingFDate = null;
		Date countingLDate = null;
		if(StringUtils.hasText(fristDate))
			countingFDate = DateUtils.parseDate("yyyy/MM/dd", fristDate);
		if(StringUtils.hasText(lastDate))
			countingLDate = DateUtils.parseDate("yyyy/MM/dd", lastDate);
		log.info("httpRequest.getIncharge"+httpRequest.getProperty("incharge"));
		searchMap.put("startDate",     		countingFDate);
		searchMap.put("endDate",     		countingLDate);
		searchMap.put("status",        		httpRequest.getProperty("status"));
		searchMap.put("incharge",  			httpRequest.getProperty("incharge"));
		searchMap.put("projectId", 			httpRequest.getProperty("projectId"));

		return searchMap;
	}
	/**
	 * 流程起始
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */ 
	public static Object[] startProcess(ShopSet form, Map resultMap) throws Exception {
		log.info("startProcess");
		try {
			String packageId = "Shop_Set";
			String processId = "process";
			String version = "20170818";
			String sourceReferenceType = "Shop_Set(1)";
			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("orderTypeCode", form.getOrderTypeCode());
			context.put("formId",    form.getHeadId());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}
	}
	//process 簽核
	/*
	public static Object[] completeAssignment(long assignmentId, boolean approveResult,BuPurchaseHead buPurchaseHead) {
		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", buPurchaseHead);
			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new EJBException(e);
		}
	}
	*/
	//reprocess 簽核
	/*
	public static Object[] reAssignment(Long assignmentId, Long processId,String loginName,BuPurchaseHead buPurchaseHead) {
		try {
			log.info("~~~reAssignment~~~~");
			HashMap context = new HashMap();
			log.info("~~~processId~~~~"+processId);
			context.put("processId", processId);
			//log.info("~~~domain~~~~"+loginName);
			//context.put("domain", loginName);
			log.info("~~~form~~~~"+buPurchaseHead);
			context.put("form", buPurchaseHead);
			log.info("~~~~~~~~~~~~~~~~~~~");
			return ProcessHandling.reAssignment(assignmentId,loginName ,context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new EJBException(e);
		}
	}
	*/
	/**
	 * 轉派更新
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */
	/*
	public HashMap updateTask(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
		String no = (String) PropertyUtils.getProperty(otherBean, "no");
		log.info("1::"+no);
		//String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String rqInChargeCode = (String) PropertyUtils.getProperty(otherBean, "rqInChargeCode");
		log.info("2::"+rqInChargeCode);
		String otherGroup = (String) PropertyUtils.getProperty(otherBean, "otherGroup");
		log.info("3::"+otherGroup);
		Double priority = NumberUtils.getDouble((String)PropertyUtils.getProperty(otherBean, "priority"));
		log.info("4::"+priority);
		String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");//前端傳來的參數
		BuPurchaseHead buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");
		parameterMap.put("entityBean", buPurchase);
		log.info("buPurchase~~~~"+buPurchase);
		//log.info("orderTypeCode~~~~~~~~~~~~~~~~~"+orderTypeCode);
		//String taskInchargeName = (String) PropertyUtils.getProperty(otherBean, "taskInchargeName");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		log.info("1234556::"+formIdString);
		BuPurchaseHead form = buPurchaseHeadDAO.findById(formId);
		log.info("buPurchaseHeadformformform"+form);
		//String nextStatus =  getStatus(orderTypeCode,formAction,form.getStatus());
		//BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
		HashMap resultMap = new HashMap(0);
		try {
			form.setNo(no);
			form.setPriority(priority);
			form.setRqInChargeCode(rqInChargeCode);
			form.setOtherGroup(otherGroup);
			//form.setStatus(nextStatus);
			//forms.setSpecInfo(specInfo);
			//forms.setTaskInchargeName(taskInchargeName);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			buPurchaseHeadDAO.update(form);
			resultMap.put("entityBean", buPurchase);
			return resultMap;
		} catch (Exception ex) {
			log.error("需求維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("需求維護單單存檔失敗，原因：" + ex.toString());
		}
	}
	*/
	/**
	 * 轉派初始化
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */ 
	/*
	public List<Properties> executeTaskassign(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		//Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			//String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			//String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
			//String no = (String) PropertyUtils.getProperty(otherBean, "no");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			BuPurchaseHead form = buPurchaseHeadDAO.findById(formId);
			//BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
			String status =form.getStatus();
			Long assignmentId = form.getAssignmentId();
			Long processId = form.getProcessId();
			String no=form.getNo();
			String rqInChargeCode=form.getRqInChargeCode();
			String otherGroup=form.getOtherGroup();
			//String taskType=forms.getTaskType();
			resultMap.put("processId", processId);
			resultMap.put("assignmentId", assignmentId);
			resultMap.put("no", no);
			resultMap.put("rqInChargeCode", rqInChargeCode);
			resultMap.put("otherGroup", otherGroup);
			resultMap.put("status", status);

		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}*/

	/**
	 * 將工作回報資料更新至bupurchasehead,bupurchaseline單主檔及明細檔(AJAX)
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	
	/*
	public Map updateAJAXworkingcondition(Map parameterMap) throws FormException,
	Exception {
		HashMap resultMap = new HashMap();
		try {
			
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			//Long headId = NumberUtils.getLong((String) PropertyUtils
			//		.getProperty(formBindBean, "headId"));
			String formIdString =(String) PropertyUtils.getProperty(formLinkBean, "headId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			
			Long lineId = NumberUtils.getLong((String) PropertyUtils
					.getProperty(otherBean, "lineId"));
			BuPurchaseHead buPurchaseHead = findById(formId);
			log.info("lineId+"+lineId);log.info("headId+"+formId);
			// ====================取得條件資料======================
			BuPurchaseLine br = (BuPurchaseLine) buPurchaseLineDAO
			.findById("BuPurchaseLine", lineId);
			if (br == null) {
				br = new BuPurchaseLine();
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, br);
				Long maxIndex = buPurchaseLineDAO
				.findPageLineMaxIndex(formId);
				br.setIndexNo(maxIndex + 1);
				br.setBuPurchaseHead(buPurchaseHead);
				setOrderNo(buPurchaseHead);
				buPurchaseLineDAO.save(br);
				
			} else {
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, br);
				buPurchaseLineDAO.update(br);
			}
			return resultMap;
		} catch (FormException fe) {
			log.error("工作回報存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("工作回報存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("工作回報單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}*/

	/**
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	/*public Map executeInitialworking(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		try{
			List<BuCommonPhraseLine> alldepartment = baseDAO.findByProperty( "BuCommonPhraseLine", new String[] { "id.buCommonPhraseHead.headCode", "enable" },new Object[] {"EmployeeDepartment","Y" }, "indexNo");
			resultMap.put("alldepartment", AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
			List<AdCategoryHead> allproject = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"ITEM","Y" },"displaySort");
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "deptNo","enable" },new Object[] {"103","Y" },"displaySort");
			
			BuPurchaseHead buPurchaseHead = this.executeFindActualBuGoalAchevement(parameterMap);
			Map multiList = new HashMap(0);
			multiList.put("allproject"			, AjaxUtils.produceSelectorData(allproject,"groupNo", "groupName", false, true));
			multiList.put("allsystem"			, AjaxUtils.produceSelectorData(allsystem,"classNo", "className", false, true));
			resultMap.put("form", buPurchaseHead);
			resultMap.put("multiList",multiList);
			return resultMap;

		}catch(Exception ex){
			log.error("達成率初始化失敗，原因：" + ex.toString());
			throw new Exception("達成率單初始化失敗，原因：" + ex.toString());

		}

	}*/
	
	/*public BuPurchaseHead executeFindActualBuGoalAchevement(Map parameterMap)
	throws FormException, Exception {

		//Object formBindBean = parameterMap.get("vatBeanFormBind");
		//Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuPurchaseHead buPurchaseHead = null;
		try {			
			String formIdString = (String) PropertyUtils.getProperty(otherBean , "headId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;			
			log.info(" executeFind工作回報"+formId);
			log.info(" buPurchaseHead工作回報"+buPurchaseHead);

			buPurchaseHead = null == formId ? this.executeNewReturn(parameterMap):this.findById(formId) ;
				 

			parameterMap.put( "entityBean", buPurchaseHead);
			return buPurchaseHead;
		} catch (Exception e) {
			log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
	}   */


	/**
	 * ajax取得附件
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	/*
	public List<Properties> getAJAXAttachment(Properties httpRequest)
	throws Exception {
		log.info("==========getAJAXAttachment===========");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			Enumeration paramNames = httpRequest.propertyNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String value = httpRequest.getProperty(paramName);
				log.info(paramName + " = " +value );
			}

			String parent_order_type = httpRequest.getProperty("parentOrderType");
			Long parent_head_id = NumberUtils.getLong(httpRequest.getProperty("parentHeadId"));
			String owner_type = httpRequest.getProperty("ownerType");
			//String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			//String creatBy = httpRequest.getProperty("creatBy");
			//String requestBy = httpRequest.getProperty("requestBy");


//			log.info("parent_order_type = " + parent_order_type);
//			log.info("head_id = " + parent_head_id);
//			log.info("iSPage = " + iSPage);
//			log.info("iPSize = " + iPSize);
//			log.info("owner_type = " + owner_type);

			HashMap findObjs = new HashMap();
			findObjs.put(" and model.parentHeadId = :parentHeadId",parent_head_id);
			findObjs.put(" and model.parentOrderType = :parentOrderType",parent_order_type);
//			findObjs.put(" and model.creatBy in (:parentOrderType)",loginEmployeeCode+","+creatBy+","+requestBy);
			findObjs.put(" and model.ownerType = :ownerType", owner_type);
//			findObjs.put(" or model.creatBy = :loginEmployeeCode", loginEmployeeCode);

			Map searchMap = baseDAO.search("GnFile as model", findObjs,
					"order by indexNo", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<GnFile> lists = (List<GnFile>) searchMap.get(BaseDAO.TABLE_LIST);
			HashMap map = new HashMap();
			// map.put("headId", headId);
			// parent_order_type = httpRequest.getProperty("parent_order_type");
			// parent_order_type = parent_order_type.trim().toUpperCase();
			System.out.println("lists.size: = " + lists.size());
			if (lists != null && lists.size() > 0) {

				// 設定上傳類型名稱
				for (GnFile gnFile : lists) {
					String type = gnFile.getType();
					BuCommonPhraseLine name = (BuCommonPhraseLine)baseDAO.findFirstByProperty("BuCommonPhraseLine",
							"and id.buCommonPhraseHead.headCode = ? and id.lineCode = ?",
							new Object[] { "GN_TYPE",type });
					if (name != null)
						gnFile.setTypeName(name.getName());
				}

				log.info("gnFiles.size() = " + lists.size());
				// 取得第一筆的INDEX
				Long firstIndex = lists.get(0).getIndexNo();
				log.info("firstIndex = " + firstIndex);

				// 取得最後一筆 INDEX cmMovementLines.get( cmMovementLines.size() -

				Long maxIndex = (Long) baseDAO.search("GnFile as model",
						"count(model.headId) as rowCount", findObjs,
						BaseDAO.QUERY_RECORD_COUNT).get(
								BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				log.info("maxIndex = " + maxIndex);
				System.out.println("maxIndex = " + maxIndex);

				if (owner_type.equals("creator")) {
					result.add(AjaxUtils.getAJAXPageData(httpRequest,
							GNFILE_GRID_FIELD_NAMES,
							GNFILE_GRID_FIELD_DEFAULT_VALUES, lists,
							gridDatas, firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageData(httpRequest,
							GNFILE_INCHARGE_GRID_FIELD_NAMES,
							GNFILE_GRID_FIELD_DEFAULT_VALUES, lists,
							gridDatas, firstIndex, maxIndex));
				}

			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GNFILE_GRID_FIELD_NAMES,
						GNFILE_GRID_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("附件查詢時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢附件資料失敗！");
		}
	}*/

	/** AJAX 合計計算	call by JS display total PAGE
	 * @param headObj
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	/*  public List<Properties>updateAJAXHeadTotalAmount(Properties httpRequest)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {
			log.info("getAJAXHeadTotalAmount ");
			Properties pro      = new Properties();
			List       re       = new ArrayList();
			String     lineId   = httpRequest.getProperty("lineId");
			//Double exchangeRate = NumberUtils.getDouble(httpRequest.getProperty("exchangeRate"));

			if (StringUtils.hasText(lineId)) {
			    String tmpSqlLine = 
				" select sum(mon) " +
				"        sum(tue) " +
				" from AD_TASK_REVIEW_VIEW  LINE_ID=" + lineId ;
			    List lineList = nativeQueryDAO.executeNativeSql(tmpSqlLine);
			    Object[] obj = (Object[]) lineList.get(0);
			    Double foreignAmount = ((BigDecimal) obj[0]).doubleValue();
			    Double itemCount     = ((BigDecimal) obj[1]).doubleValue();
			    pro.setProperty("totalForeignInvoiceAmount", NumberUtils.roundToStr(foreignAmount, 4));
			    pro.setProperty("totalLocalInvoiceAmount",   NumberUtils.roundToStr(itemCount,     0));
			}else{
			    pro.setProperty("totalForeignInvoiceAmount", "0");
			    pro.setProperty("totalLocalInvoiceAmount",   "0");
			}
			re.add(pro);
			return re;

}*/
	/**處理AJAX 採購負責人 call by JS
	 * @param httpRequest
	 * @return
	 * @throws Exception 
	 */
	
	/*
	public List<Properties> getAJAXInChargeCode(Properties httpRequest) throws Exception {
		try{
			Properties pro = new Properties();
			//List re = new ArrayList();
			String categorySystem = httpRequest.getProperty("categorySystem");
			log.info("qqqq:"+categorySystem);
			pro.setProperty("rqInChargeCode", getInChargeCode(categorySystem));
			//re.add(pro);
			//String rqInChargeCode = getInCh0.InargeCode(categorySystem);
			//log.info("rqInChargeCode=="+rqInChargeCode);
			//pro.setProperty("otherGroup"	, getInChargeName(rqInChargeCode));

			return getAJAXFormDataByrqInChargeCode(pro);
		}catch(Exception e){
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
	}*/

	/**帶出負責人
	 * @param headObj
	 */
	/*
	private String getInChargeCode(String inChargeCode) throws Exception{
		try{
			log.info("iii:"+inChargeCode);
			if (!StringUtils.hasText(inChargeCode)) {
				return "unknow";
			} else {
				//找工號 
				log.info("yyyy:"+inChargeCode);
				AdCategoryLine inCharge = adCategoryLineDAO.findbyInChargeCode(inChargeCode);
				log.info("vvvvv:"+inCharge.getInChargeCode());
				if (null==inCharge ) {
					return inCharge.getInChargeCode();
				} else {
					//找名子
					//BuEmployeeWithAddressView employees =  buEmployeeWithAddressViewDAO.findbyInChargeCode(inChargeCode);
					return inCharge.getInChargeCode();
				}
			}}catch(Exception e){
				log.error("取得指定連動的類別下拉，原因：" + e.toString());
				throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
			}
	}*/


	/*
	public List<Properties> executeCopyOrigMenu(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long count = 0L;
		BuPurchaseHead buPurchaseHead = null;
		BuEmployee buEmp = null;
		try{
			//======================取得複製時所需的必要資訊========================


			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");
			String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID

			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{
				userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				buEmp = buEmployeeDAO.findById(employeeCode);
			}
			log.info(userGroup.getGroupCode()+" "+"emp屬性:"+buEmp);

			System.out.println("===========BEGIN============" + new Date());
			conn = dataSource.getConnection();

			String sql = "select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
			" from ERP.SI_GROUP_MENU M,CONTROL.SI_MENU S,ERP.SI_MENU_ALL A "+
			" where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
			" and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = A.MENU_ID ORDER BY MENU_ID";
			//" where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? and M.GROUP_CODE=? and M.ENABLE = ? ORDER BY MENU_ID";
			log.info("sql:"+sql);
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, brandCode);
			stmt.setString(2, userGroup.getGroupCode());
			//stmt.setString(2, "TEST1");
			stmt.setString(3, "Y");
			log.info("sql塞參數ok");
			rs = stmt.executeQuery();
			log.info("sql執行ok:"+headId);

			if (rs != null) {
				buPurchaseHead = this.findById(Long.parseLong(headId));
				List<AdDetail> adDetails = buPurchaseHead.getAdDetails();
				log.info("adDetails的數量:"+adDetails.size());
				while (rs.next()) {
					log.info("emp成本控管為:"+buEmp.getCostControl()+" menu成本控管為:"+rs.getString("COST_CONTROL"));
					log.info("emp庫別控管為:"+buEmp.getWarehouseControl()+" menu庫別控管為:"+rs.getString("WAREHOUSE_CONTROL"));
					if((buEmp.getCostControl().equals("Y")&&rs.getString("COST_CONTROL").equals("Y"))
							||(buEmp.getCostControl().equals("Y")&&rs.getString("COST_CONTROL").equals("N"))
							||(buEmp.getCostControl().equals("N")&&rs.getString("COST_CONTROL").equals("N"))
							||(buEmp.getWarehouseControl().equals("N")&&rs.getString("WAREHOUSE_CONTROL").equals("Y"))
							||(buEmp.getWarehouseControl().equals("Y")&&rs.getString("WAREHOUSE_CONTROL").equals("N"))
							||(buEmp.getWarehouseControl().equals("N")&&rs.getString("WAREHOUSE_CONTROL").equals("N"))){
						count++;
						log.info("adDetailDAO:"+count+" "+headId);
						AdDetail ad = new AdDetail();
						log.info("初始化adTail:"+buPurchaseHead);
						ad.setBrandCode(brandCode);
						ad.setBrandName("T2");
						ad.setCategoryCode("");
						ad.setCategoryName("");
						ad.setIndexNo(count);
						//ad.setHeadId(Long.parseLong(headId));
						ad.setType("Menu");
						ad.setUrl(rs.getString("URL")==null?"":(rs.getString("URL").toString()));
						ad.setWarehouseCode("");
						ad.setWarehouseName("");
						ad.setMenuId(rs.getString("MENU_ID"));
						ad.setMenuName(rs.getString("TITLE")==null?"":(rs.getString("TITLE").toString()));
						ad.setEnable(rs.getString("ENABLE").toString());
						ad.setEmployeeCode(employeeCode);
						//ad.setBuPurchaseHead(buPurchaseHead);
						adDetails.add(ad);
						log.info("塞值OK:"+ad.getBrandCode());
						log.info("存檔OK");
						//adDetailDAO.save(ad);
					}
				}
				buPurchaseHead.setAdDetails(adDetails);
				buPurchaseHeadDAO.update(buPurchaseHead);
			}


			return result;
		}catch(Exception ex){
			log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}*/

	/**
	 * 刪除adDetail明細
	 * 
	 * @param headId
	 * @throws Exception
	 */
	public void deleteShopSetDetail(String headId) throws Exception{
		try{
			log.info("刪除Addetail明細:");
			ShopSet shopSet = this.findById(Long.parseLong(headId));
			//BuPurchaseHead buPurchaseHead = this.findById(Long.parseLong(headId)) ;
			if(shopSet == null){
				throw new ValidationErrorException("查無BuPurchaseHead主鍵：" + headId + "的資料！");
			}
			shopSet.setShopSetDetails(new ArrayList<ShopSetDetail>(0));
			shopSetDAO.update(shopSet);
			log.info("Addetails數量:"+shopSet.getShopSetDetails().size());
		}catch (Exception ex){
			log.error("刪除AdDetail時發生錯誤，原因：" + ex.toString());
			throw new Exception("刪除AdDetail明細失敗！");
		}	
	}


	//權限品牌
	/*public List<Properties> getAJAXPageDataBrand(Properties httpRequest) throws Exception {
		log.info("getAJAXPageDataBrand");
		try{
		    List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    int startPage = AjaxUtils.getStartPage(httpRequest) + 1;// 取得起始頁面
		    int pageSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小	    
		    //======================帶入Head的值=========================
		    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		    String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
		    log.info("employeeCode"+employeeCode);
		    //String applicant = httpRequest.getProperty("applicant");// 申請人
		    //==============================================================	    
		    List<BuEmployeeBrand> buEmployeeBrands = buEmployeeBrandDAO.findPageLine(employeeCode, startPage, pageSize);
		    //List<Long> menuIds = new ArrayList<Long>();
		   if(applicant != null && applicant.trim().length() > 0){
		    	menuIds = siGroupMenuDAO.getEnableMenuIds(brandCode, applicant);//畫面上新勾選的項目
		    	menuIds.addAll(siGroupMenuDAO.getUserMenuIds(brandCode, applicant));//既有的項目
		    }
			log.info("12346985"+buEmployeeBrands);
		    if(buEmployeeBrands != null && buEmployeeBrands.size() > 0){
		    	// 取得第一筆的INDEX
		    	Long firstIndex = (startPage * pageSize) - (pageSize - 1L);
		    	// 取得最後一筆 INDEX
		    	//Long maxIndex = Long.parseLong(((startPage) * pageSize) + "");
		    	log.info("123461345678");
		    	Long maxIndex = buEmployeeBrandDAO.findPageLineCount(employeeCode);
		    	log.info("12346");
		    for(int i = 0; i<buEmployeeBrands.size(); i++){
		    		BuEmployeeBrand m = (BuEmployeeBrand)buEmployeeBrands.get(i);
		    		m.setIndexNo(Long.parseLong((firstIndex+i) + ""));



				}
		    log.info("1234687");
		    	result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_NEW_PRIVILEGE_FIELD_NAMES, GRID_NEW_PRIVILEGE_FIELD_DEFAULT_VALUES, buEmployeeBrands, gridDatas, firstIndex, maxIndex));
		    	  log.info("12346875675");
		    }
		    return result;
		}
		catch(Exception ex){
		    log.error("載入頁面顯示的申請明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的申請明細失敗！");
		}	
	}*/
	//權限庫別

	/*public List<Properties> getAJAXPageDataWarehouse(Properties httpRequest) throws Exception {
		log.info("getAJAXPageDataBrand");
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int startPage = AjaxUtils.getStartPage(httpRequest) + 1;// 取得起始頁面
			int pageSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小	    
			//======================帶入Head的值=========================
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String employeeCode = httpRequest.getProperty("employeeCode");// 員工代號
			log.info("employeeCode"+employeeCode);*/
	//String applicant = httpRequest.getProperty("applicant");// 申請人
	//==============================================================	    
	//List<ImWarehouseEmployee> imWarehouseEmployees = imWarehouseEmployeeDAO.findPageLine(employeeCode, startPage, pageSize);
	//List<Long> menuIds = new ArrayList<Long>();
	/* if(applicant != null && applicant.trim().length() > 0){
		    	menuIds = siGroupMenuDAO.getEnableMenuIds(brandCode, applicant);//畫面上新勾選的項目
		    	menuIds.addAll(siGroupMenuDAO.getUserMenuIds(brandCode, applicant));//既有的項目
		    }*/
	/*log.info("12346985"+imWarehouseEmployees);
			if(imWarehouseEmployees != null && imWarehouseEmployees.size() > 0){
				// 取得第一筆的INDEX
				Long firstIndex = (startPage * pageSize) - (pageSize - 1L);
				// 取得最後一筆 INDEX
				//Long maxIndex = Long.parseLong(((startPage) * pageSize) + "");
				log.info("123461345678");
				Long maxIndex = buEmployeeBrandDAO.findPageLineCount(employeeCode);
				log.info("12346");
				for(int i = 0; i<imWarehouseEmployees.size(); i++){
					ImWarehouseEmployee m = (ImWarehouseEmployee)imWarehouseEmployees.get(i);
					m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
					//System.out.print(""+m.getBrandCode());


				}
				log.info("1234687");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_NEW_WAREHOSE_FIELD_NAMES, GRID_NEW_WAREHOUSE_FIELD_DEFAULT_VALUES, imWarehouseEmployees, gridDatas, firstIndex, maxIndex));
				log.info("12346875675");
			}
			return result;
		}
		catch(Exception ex){
			log.error("載入頁面顯示的申請明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的申請明細失敗！");
		}	
	}*/
	/*public List<Properties> updateAJAXPageLinesDataKweBrand(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData="+gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_KWE_BRAND);
			log.info("upRecords======"+upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				//	BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				BuPurchaseHead buPurchaseHead = buPurchaseHeadDAO.findById(headId);
				//	buPurchaseHead.setHeadId(headId);;
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String brandCode = upRecord.getProperty("brandCode");
						String warehouseCode = upRecord.getProperty("warehouseCode");
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));		
						if (StringUtils.hasText(brandCode)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+adDetail);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_FIELD_NAMES_KWE_BRAND, GRID_FIELD_TYPES_KWE_BRAND);
								adDetailDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_FIELD_NAMES_KWE_BRAND, GRID_FIELD_TYPES_KWE_BRAND);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
								line1.setEmployeeCode(buPurchaseHead.getRequestCode());
								line1.setType("Brand");
								//line1.setWarehouseCode(String.valueOf(warehouseCode));
								log.info("測試:"+buPurchaseHead.getHeadId());
								adDetailDAO.save(line1);
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}*/
	/*public List<Properties> updateAJAXPageLinesDataKweWare(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData="+gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_KWE_WARE);
			log.info("upRecords======"+upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				//BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				BuPurchaseHead buPurchaseHead = buPurchaseHeadDAO.findById(headId);
				//buPurchaseHead.setHeadId(headId);
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String brandCode = upRecord.getProperty("brandCode");
						String warehouseCode = upRecord.getProperty("warehouseCode");
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));		
						if (StringUtils.hasText(warehouseCode)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+adDetail);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_FIELD_NAMES_KWE_WARE, GRID_FIELD_TYPES_KWE_WARE);
								adDetailDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_FIELD_NAMES_KWE_WARE, GRID_FIELD_TYPES_KWE_WARE);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
								//line1.setWarehouseCode(String.valueOf(warehouseCode));
								log.info("測試:"+buPurchaseHead.getHeadId());
								line1.setEmployeeCode(buPurchaseHead.getRequestCode());
								adDetailDAO.save(line1);
								line1.setType("Warehouse");
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}*/
	/*	public List<Properties> updateAJAXPageLinesDataKweShop(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData="+gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_KWE_SHOP);
			log.info("upRecords======"+upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				//BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				BuPurchaseHead buPurchaseHead = buPurchaseHeadDAO.findById(headId);
				//buPurchaseHead.setHeadId(headId);
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String shopCode = upRecord.getProperty("shopCode");
						log.info("shopCode~~~~~"+shopCode);
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));		
						if (StringUtils.hasText(shopCode)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+adDetail);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_FIELD_NAMES_KWE_SHOP, GRID_FIELD_TYPES_KWE_SHOP);
								adDetailDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_FIELD_NAMES_KWE_SHOP, GRID_FIELD_TYPES_KWE_SHOP);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
								line1.setEmployeeCode(buPurchaseHead.getRequestCode());
								log.info("getRequestCode()====="+buPurchaseHead.getRequestCode());
								//line1.setWarehouseCode(String.valueOf(warehouseCode));
								log.info("測試:"+buPurchaseHead.getHeadId());
								line1.setType("Shop");
								adDetailDAO.save(line1);
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}*/
	/*public List<Properties> updateAJAXPageLinesDataKweCategory(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData="+gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_KWE_CATEGORY);
			log.info("upRecords======"+upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				//BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				BuPurchaseHead buPurchaseHead = buPurchaseHeadDAO.findById(headId);
				//buPurchaseHead.setHeadId(headId);
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String categoryCode = upRecord.getProperty("categoryCode");
						log.info("categoryCode~~~~~~~"+categoryCode);
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));		
						if (StringUtils.hasText(categoryCode)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+adDetail);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_FIELD_NAMES_KWE_CATEGORY, GRID_FIELD_TYPES_KWE_CATEGORY);
								adDetailDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_FIELD_NAMES_KWE_CATEGORY, GRID_FIELD_TYPES_KWE_CATEGORY);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
								//line1.setWarehouseCode(String.valueOf(warehouseCode));
								log.info("測試:"+buPurchaseHead.getHeadId());
								line1.setType("ItemCategory");
								adDetailDAO.save(line1);
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}*/
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	/*public List<Properties> getAJAXPageDataBrand(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		log.info("ghjghj");
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			log.info("asdsd");
			String employeeCode = httpRequest.getProperty("depManager");// 員工代號

			log.info("adDetail.headId" + headId);
			List adDetail = adDetailDAO.findPageLine(headId, iSPage, iPSize);
			List buBrand  = buBrandDAO.findPageLine(employeeCode,iSPage, iPSize);
			HashMap findObjs = new HashMap();
			log.info("adDetail"+adDetail);
			log.info("buPurchases.size"+ adDetail.size());	
			if(buBrand != null && buBrand.size() > 0){
				Long indexNo = 1L;
				BuBrand b = (BuBrand)buBrand.get(0);
				//Long firstIndex = b.getIndexNo();
				Long maxIndex = buBrandDAO.findPageLineCount(employeeCode);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;  
				b.setIndexNo(indexNo);
				indexNo++;*/
	//b.getBrandCode();

	//HashMap map = new HashMap();
	//map.put("indexNo", a.getIndexNo());
	//map.put("brandCode", b.getBrandCode());
	//map.put("enable", b.getEnable());
	//log.info("IIIndex:"+firstIndex+","+maxIndex);
	// 取得最後一筆 INDEX
	/*BuBrand m = null;
				 HashMap map = new HashMap();
				for(int i = 0; i<buBrand.size(); i++){
					 m = (BuBrand)buBrand.get(i);
		    		m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
						//map.put("indexNo", m.getIndexNo());
					 	map.put("brandCode", m.getBrandCode());
						log.info("m.getBrandCode()~~~~"+m.getBrandCode());
						map.put("enable", m.getEnable());
					}*/
	/*	for(int i = 0; i<buBrand.size(); i++){
					BuBrand m = (BuBrand)buBrand.get(i);
					m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
				}
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMESSS, GRID_FIELD_VALUES_KWE_BRAND, buBrand, gridDatas,firstIndex,maxIndex));
			}
			if (adDetail != null && adDetail.size() > 0) {
				log.info("adDetail~~~~~~~~~~~~~~~~~~~~~~~~~"+ adDetail);
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)adDetail.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES_KWE_BRAND, GRID_FIELD_VALUES_KWE_BRAND, adDetail, gridDatas, firstIndex, maxIndex));

			}else {*/
	/*int startPage = AjaxUtils.getStartPage(httpRequest) + 1;// 取得起始頁面
			    int pageSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小   
				List buBrand  = buBrandDAO.findPageLineAll(iSPage, iPSize);
				//BuBrand a = (BuBrand)buBrand.get(0);
				//log.info("buBrand~~~~~!!!!!!@"+a);
				Long firstIndex = (startPage * pageSize) - (pageSize - 1L);
				Long maxIndex = Long.parseLong(buBrand.size() + "");
				//Long firstIndex = a.getIndexNo();
				//Long maxIndex = buBrandDAO.findPageLineMaxIndex();
				//log.info("IIIndex:"+firstIndex+","+maxIndex);
				for(int i = 0; i<buBrand.size(); i++){
					BuBrand m = (BuBrand)buBrand.get(i);
		    		m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
					}*/
	/*HashMap map = new HashMap();
				map.put("indexNo", a.getIndexNo());
				map.put("brandCode", m.getBrandCode());
				map.put("enable", m.getEnable());
			log.info("map~~~~~~"+map);
			log.info(" m.getBrandCode()~~~~~~~~~"+ m.getBrandCode());*/
	/*		re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES_KWE_BRAND, GRID_FIELD_VALUES_KWE_BRAND , gridDatas));
			}

			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}*/
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	/*public List<Properties> getAJAXPageDataWare(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		log.info("ghjghj");
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String employeeCode = httpRequest.getProperty("depManager");// 員工代號
			log.info("depManager=="+employeeCode);

			log.info("adDetail.headId" + headId);
			List adDetail = adDetailDAO.findPageLine(headId, iSPage, iPSize);
			List buShop  = imWarehouseDAO.findPageLine(employeeCode,iSPage, iPSize);
			log.info("adDetail"+adDetail);
			log.info("buPurchases.size"+ adDetail.size());	
			if(buShop != null && buShop.size() > 0){
				//Long indexNo = 1L;
				ImWarehouse b = (ImWarehouse)buShop.get(0);
				//Long firstIndex = b.getIndexNo();
				Long maxIndex = imWarehouseDAO.findPageLineCount(employeeCode);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;  */
	//b.setIndexNo(indexNo);
	//indexNo++;
	//b.getBrandCode();
	//HashMap map = new HashMap();
	//map.put("indexNo", a.getIndexNo());
	//map.put("brandCode", b.getBrandCode());
	//map.put("enable", b.getEnable());
	//log.info("IIIndex:"+firstIndex+","+maxIndex);
	// 取得最後一筆 INDEX
	/*BuBrand m = null;
				 HashMap map = new HashMap();
				for(int i = 0; i<buBrand.size(); i++){
					 m = (BuBrand)buBrand.get(i);
		    		m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
						//map.put("indexNo", m.getIndexNo());
					 	map.put("brandCode", m.getBrandCode());
						log.info("m.getBrandCode()~~~~"+m.getBrandCode());
						map.put("enable", m.getEnable());
					}*/
	/*	for(int i = 0; i<buShop.size(); i++){
					ImWarehouse m = (ImWarehouse)buShop.get(i);
					m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
				}
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_NEW_WAREHOSE_FIELD_NAMES, GRID_FIELD_VALUES_KWE_WARE, buShop, gridDatas,firstIndex,maxIndex));
			}
			if (adDetail != null && adDetail.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)adDetail.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_NEW_WAREHOSE_FIELD_NAMES, GRID_FIELD_VALUES_KWE_WARE, adDetail, gridDatas, firstIndex, maxIndex));
			}else {
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES_KWE_WARE, GRID_FIELD_VALUES_KWE_WARE,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}*/
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageDataShop(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			List parMenus = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String oriMenuName = null;
			String parMenuName = null;
			//AdDetail ad = null;
			log.info("getAJAXPageDataShop.headId:"+headId);
			log.info("getAJAXPageDataShop.httpRequest:"+httpRequest);
			//Steve
			List shopSetLines = shopSetDAO.findPageLineShop(headId, iSPage, iPSize);

			log.info("getAJAXPageDataShop.size"+ shopSetLines.size());	
			if (shopSetLines != null && shopSetLines.size() > 0) {
				
				for(int i=0;i<shopSetLines.size();i++){
					ShopSetDetail x = (ShopSetDetail)shopSetLines.get(i);
					log.info("babu warehouseId:"+x.getShopCode());
				
					
				}
				
				log.info("b1");
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				ShopSetDetail ssd = (ShopSetDetail)shopSetLines.get(0);
				log.info("b2:"+headId);
				Long firstIndex = ssd.getIndexNo();
				log.info("b3:"+firstIndex);
				Long maxIndex = shopSetDAO.findPageLineMaxIndex(headId);
				log.info("b5:"+firstIndex);
				log.info("b4:"+maxIndex);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_SHOP_FIELD_NAMES, GRID_SEARCH_SHOP_FIELD_LINE_NAMES_VALUE, shopSetLines, gridDatas, firstIndex, maxIndex));
			}else {
				log.info("跑Default:");
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_SHOP_FIELD_NAMES, GRID_SEARCH_SHOP_FIELD_LINE_NAMES_VALUE,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesDataShop....ABC");
		//throw new ValidationErrorException("有進來");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData="+gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			List<Properties> upRecords=null;
			if (headId == null) 
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_SEARCH_SHOP_FIELD_NAMES);
			log.info("upRecords======"+upRecords);
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				//BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				ShopSet shopSet = new ShopSet();
				//buPurchaseHead.setHeadId(headId);
				shopSet.setHeadId(headId);
				//int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				int indexNo = shopSetDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String shopCode = upRecord.getProperty("shopCode");
						
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						if (StringUtils.hasText(shopCode)){ 
							//AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							ShopSetDetail shopSetDetail = shopSetDAO.findItemByIdentification(shopSet.getHeadId(), lineId);
							log.info("buPurchaseLine======"+upRecord);
							if ( null != shopSetDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								log.info( "前端Enable = " + upRecord.getProperty("enable") + " adDetail Enable:"+ shopSetDetail.getEnable()  );
								if(!upRecord.getProperty("enable").equals(shopSetDetail.getEnable())){
									shopSetDetail.setUpdateDate(new Date());
								}
								AjaxUtils.setPojoProperties(shopSetDetail, upRecord, GRID_SEARCH_SHOP_FIELD_NAMES, GRID_SHOP_FIELD_TYPES);
								shopSetDAO.update(shopSetDetail);
							}else{
								indexNo++;
								ShopSetDetail line1 = new ShopSetDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_SEARCH_SHOP_FIELD_NAMES, GRID_SHOP_FIELD_TYPES);
								line1.setShopSet(shopSet);
								line1.setIndexNo(Long.valueOf(indexNo));
								log.info("測試:"+shopSet.getHeadId());
								adDetailDAO.save(line1);
								log.info("line1====="+line1);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新明細失敗！");
		}
	}
	
	/**設定權限
	 *
	 *
	 *
	*/
	
	
	public Long updateCompetenceData(Long headId,String employeeCode,String role,String brandCode)throws Exception{
		ShopSet shopSet = shopSetDAO.findById(headId);
		List<ShopSetDetail> shopSetDetails = shopSet.getShopSetDetails();
		
		try{	 
			System.out.print("Tty List");
			for(int i=0;i<shopSetDetails.size();i++){
					buShopEmployeeDAO.deleteBuEmployeeShop(brandCode, role);
			}
			for (Iterator iterator = shopSetDetails.iterator(); iterator.hasNext();) {
				ShopSetDetail shopSetDetail = (ShopSetDetail)iterator.next();
				
				BuShopEmployee buShopEmp = new BuShopEmployee();
				BuShopEmployeeId buShopEmpId = new BuShopEmployeeId();
				Long indexNo     = 1L;
				indexNo++;
				//----shop{
				System.out.print("Star Shop");
				buShopEmpId.setShopCode(shopSetDetail.getShopCode());
				buShopEmpId.setEmployeeCode(shopSetDetail.getEmployeeCode());
				buShopEmp.setId(buShopEmpId);
				buShopEmp.setEnable(shopSetDetail.getEnable());
				buShopEmp.setLastUpdateDate(new Date());
				buShopEmp.setIndexNo(indexNo);
				buShopEmp.setCreatedBy(shopSet.getCreatedBy());
				buShopEmp.setLastUpdatedBy(shopSet.getLastUpdatedBy());
				buShopEmp.setCreationDate(shopSet.getCreationDate());
				if("Y".equals(shopSetDetail.getEnable())){
					buShopEmp.setEnable("Y");
				}else{
					//imWarehouseEmp.setEnable("N");
					continue;
				}
				buShopDAO.save(buShopEmp);
				System.out.print("end Shop"); 
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("updateCompetenceData : 權限存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("updateCompetenceData : 權限存檔時發生錯誤，原因：" + ex.getMessage());
	 	}
		return 1L;
	 }
	
	/**AJAX Load Page Data
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	/*public List<Properties> getAJAXPageDataCategory(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
	NoSuchMethodException,Exception {
		log.info("ghjghj");
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String employeeCode = httpRequest.getProperty("depManager");// 員工代號
			List<Properties> re        = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			log.info("asdsd");

			log.info("adDetail.headId" + headId);
			List adDetail = adDetailDAO.findPageLine(headId, iSPage, iPSize);
			List imItemCategory = imItemCategoryDAO.findPageLine(employeeCode,iSPage, iPSize);
			log.info("adDetail"+adDetail);
			log.info("buPurchases.size"+ adDetail.size());
			if(imItemCategory != null && imItemCategory.size() > 0){
				//Long indexNo = 1L;
				ImItemCategory b = (ImItemCategory)imItemCategory.get(0);
				//Long firstIndex = b.getIndexNo();
				Long maxIndex = imItemCategoryDAO.findPageLineCount(employeeCode);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;  */
	//b.setIndexNo(indexNo);
	//indexNo++;
	//b.getBrandCode();
	//b.setIndexNo(indexNo);
	//HashMap map = new HashMap();
	//map.put("indexNo", a.getIndexNo());
	//map.put("brandCode", b.getBrandCode());
	//map.put("enable", b.getEnable());
	//log.info("IIIndex:"+firstIndex+","+maxIndex);
	// 取得最後一筆 INDEX
	/*BuBrand m = null;
				 HashMap map = new HashMap();
				for(int i = 0; i<buBrand.size(); i++){
					 m = (BuBrand)buBrand.get(i);
		    		m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
						//map.put("indexNo", m.getIndexNo());
					 	map.put("brandCode", m.getBrandCode());
						log.info("m.getBrandCode()~~~~"+m.getBrandCode());
						map.put("enable", m.getEnable());
					}*/
	/*	for(int i = 0; i<imItemCategory.size(); i++){
					ImItemCategory m = (ImItemCategory)imItemCategory.get(i);
					m.setIndexNo(Long.parseLong((firstIndex+i) + ""));
				}
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMESSCATEGORY, GRID_FIELD_VALUES_KWE_CATEGORY, imItemCategory, gridDatas,firstIndex,maxIndex));
			}
			if (adDetail != null && adDetail.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)adDetail.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES_KWE_CATEGORY, GRID_FIELD_VALUES_KWE_CATEGORY, adDetail, gridDatas, firstIndex, maxIndex));
			}else {
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES_KWE_CATEGORY, GRID_FIELD_VALUES_KWE_CATEGORY,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}*/

	//get下一個狀態
	private String getStatus(String orderTypeCode,String formAction,String beforeStatus) {
		// TODO Auto-generated method stub
		//	BuSupplierMod buSupplierMod = new BuSupplierMod();
		String status = null;
		//送出 存 狀態
		if(orderTypeCode.equalsIgnoreCase("IRQ"))
		{
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				if(beforeStatus.equals(OrderStatus.SAVE)){
					status = OrderStatus.SIGNING;
				}else if(beforeStatus.equals(OrderStatus.SIGNING)){
					status = OrderStatus.PLAN;
				}else if(beforeStatus.equals(OrderStatus.PLAN)){
					status = OrderStatus.FINISH;
				}

			}else if(OrderStatus.FORM_SAVE.equals(formAction)){
				status = OrderStatus.SAVE;
			}else if(OrderStatus.FORM_VOID.equals(formAction)){
				status = OrderStatus.VOID;
			}else if(OrderStatus.SUSPEND.equals(formAction)){
				status = OrderStatus.SUSPEND;
			}else if(OrderStatus.REPLAN.equals(formAction)){
				status = OrderStatus.REPLAN;
			}
		}else{
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				if(beforeStatus.equals(OrderStatus.SAVE)){
					status = OrderStatus.SIGNING;
				}else if(beforeStatus.equals(OrderStatus.SIGNING)){
					status = OrderStatus.PURCHASE;
				}else if(beforeStatus.equals(OrderStatus.PURCHASE)){
					status = OrderStatus.FINISH;
				}	
			}else if(OrderStatus.FORM_SAVE.equals(formAction)){
				status = OrderStatus.SAVE;
			}else if(OrderStatus.FORM_VOID.equals(formAction)){
				status = OrderStatus.VOID;
			}else if(OrderStatus.ORDER.equals(formAction)){
				status = OrderStatus.ORDER;
			}else if(OrderStatus.PURCHASE.equals(formAction)){
				status = OrderStatus.PURCHASE;
			}

		}
		return status;
	}

	/*public List<Properties> executeCopyOrigBrand(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long count = 0L;
		BuPurchaseHead buPurchaseHead = null;
		BuEmployeeBrand buEmp = null;
		try{
			//======================取得複製時所需的必要資訊========================


			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");
			String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
			log.info("申請人工號:"+employeeCode);
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{
				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				BuEmployeeBrandId buEmpId = new BuEmployeeBrandId(employeeCode,brandCode);
				buEmp = (BuEmployeeBrand)buEmployeeBrandDAO.findByempolyee(buEmpId);
			}
			log.info("emp屬性:"+buEmp.getId());

			System.out.println("===========BEGIN============" + new Date());
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM BU_EMPLOYEE_BRAND where EMPLOYEE_CODE = ? ";*/
	/*"select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
                         " from CONTROL.SI_GROUP_MENU M,CONTROL.SI_MENU S,CONTROL.SI_MENU_ALL A "+
    		             " where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
                         " and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = S.MENU_ID ORDER BY MENU_ID";*/

	/*log.info("sql:"+sql);
			stmt = conn.prepareStatement(sql);
			log.info("~~~~~~~~~~~~~~~~@@@@");
			stmt.setString(1, employeeCode);
			//stmt.setString(2, brandCode);
			log.info("~~~~~~~~~~~~~~~~=="+employeeCode);   
			//stmt.setString(2, "TEST1");
			//stmt.setString(3, "Y");
			log.info("sql塞參數ok");
			rs = stmt.executeQuery();
			log.info("sql執行ok:"+headId);
			System.out.println("===========END==manualReportJob==========");
			if (rs != null) {
				buPurchaseHead = this.findById(Long.parseLong(headId));
				//	List<AdDetail> adDetails = buPurchaseHead.getAdDetail();
				//	log.info("adDetails的數量:"+adDetails.size());
				while (rs.next()) {
					//log.info("emp成本控管為:"+buEmp.getCostControl()+" menu成本控管為:"+rs.getString("COST_CONTROL"));
					//log.info("emp庫別控管為:"+buEmp.getWarehouseControl()+" menu庫別控管為:"+rs.getString("WAREHOUSE_CONTROL"));
					{
						count++;
						log.info("adDetailDAO:"+count+" "+headId);
						AdDetail ad = new AdDetail();
						log.info("初始化adTail:"+buPurchaseHead);
						ad.setBrandCode(rs.getString("BRAND_CODE"));
						//ad.setBrandName("T2");
						//ad.setCategoryCode("");
						//ad.setCategoryName("");
						ad.setIndexNo(count);
						//ad.setHeadId(Long.parseLong(headId));
						ad.setType("BRAND");
						//ad.setUrl(rs.getString("URL")==null?"":(rs.getString("URL").toString()));
						//ad.setWarehouseCode("");
						//ad.setWarehouseName("");
						//ad.setMenuId(rs.getString("MENU_ID").toString());
						//ad.setMenuName(rs.getString("NAME")==null?"":(rs.getString("NAME").toString()));
						ad.setEnable("Y");
						ad.setEmployeeCode(employeeCode);
						//ad.setBuPurchaseHead(buPurchaseHead);
						//adDetails.add(ad);
						log.info("塞值OK:"+ad.getBrandCode());
						log.info("存檔OK");
						//adDetailDAO.save(ad);
					}
				}
				//	buPurchaseHead.setAdDetail(adDetails);
				buPurchaseHeadDAO.update(buPurchaseHead);
			}


			return result;
		}catch(Exception ex){
			log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}*/
	/*public List<Properties> executeCopyOrigWare(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long count = 0L;
		BuPurchaseHead buPurchaseHead = null;
		//ImWarehouseEmployee buEmp = null;
		ImWarehouseEmployee buEmp =null;
		try{
			//======================取得複製時所需的必要資訊========================

			//String shopCode = "21360";// 品牌代號
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");
			String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
			//Long lineId = Long.getLong("lineId");// 要顯示的HEAD_ID
			log.info("brandCode===="+brandCode);
			//log.info("lineId===="+lineId);
			log.info("employeeCode===="+employeeCode);
			log.info("headId===="+headId);
			log.info("申請人工號:"+employeeCode);
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{

				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				// ImWarehouseEmployeeId buEmpId = new ImWarehouseEmployeeId(null,employeeCode);
				//buEmp = imWarehouseEmployeeDAO.findByempolyee(employeeCode);
			}
			//log.info("emp屬性:"+buEmp.getId());

			System.out.println("===========BEGIN============" + new Date());
			conn = dataSource.getConnection();*/

	//String sql = "SELECT * FROM IM_WAREHOUSE_EMPLOYEE where EMPLOYEE_CODE = ? ";
	/*"select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
                         " from CONTROL.SI_GROUP_MENU M,CONTROL.SI_MENU S,CONTROL.SI_MENU_ALL A "+
    		             " where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
                         " and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = S.MENU_ID ORDER BY MENU_ID";*/

	//log.info("sql:"+sql);
	//stmt = conn.prepareStatement(sql);
	/*log.info("~~~~~~~~~~~~~~~~@@@@");
			//stmt.setString(1, buEmp.get(index);
			//stmt.setString(2, brandCode);
			log.info("~~~~~~~~~~~~~~~~=="+employeeCode);   
			//stmt.setString(2, "TEST1");
			//stmt.setString(3, "Y");
			log.info("sql塞參數ok");
			//rs = stmt.executeQuery();
			log.info("sql執行ok:"+headId);
			System.out.println("===========END==manualReportJob==========");
			if (rs != null) {
				buPurchaseHead = this.findById(Long.parseLong(headId));
				//	List<AdDetail> adDetails = buPurchaseHead.getAdDetail();
				//	log.info("adDetails的數量:"+adDetails.size());
				while (rs.next()) {
					//log.info("emp成本控管為:"+buEmp.getCostControl()+" menu成本控管為:"+rs.getString("COST_CONTROL"));
					//log.info("emp庫別控管為:"+buEmp.getWarehouseControl()+" menu庫別控管為:"+rs.getString("WAREHOUSE_CONTROL"));
					{
						count++;
						log.info("adDetailDAO:"+count+" "+headId);
						AdDetail ad = new AdDetail();
						log.info("初始化adTail:"+buPurchaseHead);
						ad.setWarehouseCode(rs.getString("WAREHOUSE_CODE"));
						//ad.setBrandName("T2");
						//ad.setCategoryCode("");
						//ad.setCategoryName("");
						ad.setIndexNo(count);
						//ad.setHeadId(Long.parseLong(headId));
						ad.setType("WAREHOUSE");
						//ad.setUrl(rs.getString("URL")==null?"":(rs.getString("URL").toString()));
						//ad.setWarehouseCode("");
						//ad.setWarehouseName("");
						//ad.setMenuId(rs.getString("MENU_ID").toString());
						//ad.setMenuName(rs.getString("NAME")==null?"":(rs.getString("NAME").toString()));
						ad.setEnable("Y");
						ad.setEmployeeCode(employeeCode);
						//ad.setBuPurchaseHead(buPurchaseHead);
						//adDetails.add(ad);
						log.info("塞值OK:"+ad.getShopCode());
						log.info("存檔OK");
						//adDetailDAO.save(ad);
					}
				}
				//	buPurchaseHead.setAdDetail(adDetails);
				buPurchaseHeadDAO.update(buPurchaseHead);
			}


			return result;
		}catch(Exception ex){
			log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}*/

	/*public List<Properties> executeCopyOrigShop(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long count = 0L;
		BuPurchaseHead buPurchaseHead = null;
		BuShopEmployee buEmp = null;
		try{
			//======================取得複製時所需的必要資訊========================

			//String shopCode = "21360";// 品牌代號
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");
			String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
			log.info("brandCode===="+brandCode);
			//log.info("shopCode===="+shopCode);
			log.info("employeeCode===="+employeeCode);
			log.info("headId===="+headId);
			log.info("申請人工號:"+employeeCode);
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{

				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				BuShopEmployeeId buEmpId = new BuShopEmployeeId(null,employeeCode);
				buEmp = (BuShopEmployee)buShopEmployeeDAO.findByempolyee(buEmpId);
			}
			//log.info("emp屬性:"+buEmp.getId());

			System.out.println("===========BEGIN============" + new Date());
			conn = dataSource.getConnection();

			String sql = "SELECT * FROM BU_SHOP_EMPLOYEE where EMPLOYEE_CODE = ? ";*/
	/*"select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
                         " from CONTROL.SI_GROUP_MENU M,CONTROL.SI_MENU S,CONTROL.SI_MENU_ALL A "+
    		             " where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
                         " and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = S.MENU_ID ORDER BY MENU_ID";*/

	/*log.info("sql:"+sql);
			stmt = conn.prepareStatement(sql);
			log.info("~~~~~~~~~~~~~~~~@@@@");
			stmt.setString(1, employeeCode);
			//stmt.setString(2, brandCode);
			log.info("~~~~~~~~~~~~~~~~=="+employeeCode);   
			//stmt.setString(2, "TEST1");
			//stmt.setString(3, "Y");
			log.info("sql塞參數ok");
			rs = stmt.executeQuery();
			log.info("sql執行ok:"+headId);
			System.out.println("===========END==manualReportJob==========");
			if (rs != null) {
				buPurchaseHead = this.findById(Long.parseLong(headId));
				//	List<AdDetail> adDetails = buPurchaseHead.getAdDetail();
				//	log.info("adDetails的數量:"+adDetails.size());
				while (rs.next()) {
					//log.info("emp成本控管為:"+buEmp.getCostControl()+" menu成本控管為:"+rs.getString("COST_CONTROL"));
					//log.info("emp庫別控管為:"+buEmp.getWarehouseControl()+" menu庫別控管為:"+rs.getString("WAREHOUSE_CONTROL"));
					{
						count++;
						log.info("adDetailDAO:"+count+" "+headId);
						AdDetail ad = new AdDetail();
						log.info("初始化adTail:"+buPurchaseHead);
						ad.setShopCode(rs.getString("SHOP_CODE"));
						//ad.setBrandName("T2");
						//ad.setCategoryCode("");
						//ad.setCategoryName("");
						ad.setIndexNo(count);
						//ad.setHeadId(Long.parseLong(headId));
						ad.setType("SHOP");
						//ad.setUrl(rs.getString("URL")==null?"":(rs.getString("URL").toString()));
						//ad.setWarehouseCode("");
						//ad.setWarehouseName("");
						//ad.setMenuId(rs.getString("MENU_ID").toString());
						//ad.setMenuName(rs.getString("NAME")==null?"":(rs.getString("NAME").toString()));
						ad.setEnable("Y");
						ad.setEmployeeCode(employeeCode);
						//ad.setBuPurchaseHead(buPurchaseHead);
						//adDetails.add(ad);
						log.info("塞值OK:"+ad.getShopCode());
						log.info("存檔OK");
						//adDetailDAO.save(ad);
					}
				}
				//	buPurchaseHead.setAdDetail(adDetails);
				buPurchaseHeadDAO.update(buPurchaseHead);
			}


			return result;
		}catch(Exception ex){
			log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}*/
	/*public List<Properties> executeCopyOrigItem(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long count = 0L;
		BuPurchaseHead buPurchaseHead = null;
		BuShopEmployee buEmp = null;
		try{
			//======================取得複製時所需的必要資訊========================

			//String shopCode = "21360";// 品牌代號
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String headId = httpRequest.getProperty("headId");
			String employeeCode = httpRequest.getProperty("employeeCode");// 要顯示的HEAD_ID
			log.info("brandCode===="+brandCode);
			//log.info("shopCode===="+shopCode);
			log.info("employeeCode===="+employeeCode);
			log.info("headId===="+headId);
			log.info("申請人工號:"+employeeCode);
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{

				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				BuShopEmployeeId buEmpId = new BuShopEmployeeId(null,employeeCode);
				buEmp = (BuShopEmployee)buShopEmployeeDAO.findByempolyee(buEmpId);
			}
			//log.info("emp屬性:"+buEmp.getId());

			System.out.println("===========BEGIN============" + new Date());
			conn = dataSource.getConnection();*/

	//String sql = "SELECT * FROM BU_SHOP_EMPLOYEE where EMPLOYEE_CODE = ? ";
	/*"select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
                         " from CONTROL.SI_GROUP_MENU M,CONTROL.SI_MENU S,CONTROL.SI_MENU_ALL A "+
    		             " where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
                         " and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = S.MENU_ID ORDER BY MENU_ID";*/

	//log.info("sql:"+sql);
	//stmt = conn.prepareStatement(sql);
	/*	log.info("~~~~~~~~~~~~~~~~@@@@");
			//stmt.setString(1, employeeCode);
			//stmt.setString(2, brandCode);
			log.info("~~~~~~~~~~~~~~~~=="+employeeCode);   
			//stmt.setString(2, "TEST1");
			//stmt.setString(3, "Y");
			log.info("sql塞參數ok");
			//rs = stmt.executeQuery();
			log.info("sql執行ok:"+headId);
			System.out.println("===========END==manualReportJob==========");
			if (rs != null) {
				buPurchaseHead = this.findById(Long.parseLong(headId));
				//	List<AdDetail> adDetails = buPurchaseHead.getAdDetail();
				//	log.info("adDetails的數量:"+adDetails.size());
				while (rs.next()) {
					//log.info("emp成本控管為:"+buEmp.getCostControl()+" menu成本控管為:"+rs.getString("COST_CONTROL"));
					//log.info("emp庫別控管為:"+buEmp.getWarehouseControl()+" menu庫別控管為:"+rs.getString("WAREHOUSE_CONTROL"));
					{
						count++;
						log.info("adDetailDAO:"+count+" "+headId);
						AdDetail ad = new AdDetail();
						log.info("初始化adTail:"+buPurchaseHead);
						ad.setCategoryCode(rs.getString("CATEGORY_CODE"));
						//ad.setBrandName("T2");
						//ad.setCategoryCode("");
						//ad.setCategoryName("");
						ad.setIndexNo(count);
						//ad.setHeadId(Long.parseLong(headId));
						ad.setType("SHOP");
						//ad.setUrl(rs.getString("URL")==null?"":(rs.getString("URL").toString()));
						//ad.setWarehouseCode("");
						//ad.setWarehouseName("");
						//ad.setMenuId(rs.getString("MENU_ID").toString());
						//ad.setMenuName(rs.getString("NAME")==null?"":(rs.getString("NAME").toString()));
						ad.setEnable("Y");
						ad.setEmployeeCode(employeeCode);
						//ad.setBuPurchaseHead(buPurchaseHead);
						//adDetails.add(ad);
						log.info("塞值OK:"+ad.getShopCode());
						log.info("存檔OK");
						//adDetailDAO.save(ad);
					}
				}
				//	buPurchaseHead.setAdDetail(adDetails);
				buPurchaseHeadDAO.update(buPurchaseHead);
			}


			return result;
		}catch(Exception ex){
			log.error("複製出貨單別發生錯誤，原因：" + ex.toString());
			throw new Exception("複製出貨單別發生錯誤，原因：" + ex.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}*/
	/*
	public List<Properties> updateOrderByNew(Properties httpRequest) throws Exception {

		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection conn = null;

		try{
			log.info("重排順序");
			//Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
			//String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String categoryGroup = httpRequest.getProperty("categoryGroup");
			String rqInChargeCode = httpRequest.getProperty("rqInChargeCode");
			log.info("categoryGroup~~"+categoryGroup);
			log.info("rqInChargeCode~~"+rqInChargeCode);
			//String[] checkEmp = {"T96085","T49674","T92457","P15042","T25046","T60343"};
			System.out.println("===========BEGIN============");
			conn = dataSource.getConnection();
			BuEmployee emp = null;
			//for(int i=0;i<checkEmp.length;i++){
			emp = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, rqInChargeCode);
			ArrayList headList = new ArrayList();
			String sql = "select * from ERP.AD_TASK WHERE STATUS = 'SAVE' AND RQ_IN_CHARGE_CODE = ?  AND ORDER_TYPE_CODE = 'IRQ' AND ORDER_NO NOT LIKE '%TMP%' AND RQ_IN_CHARGE_CODE IS NOT NULL and BRAND_CODE='T2' AND CATEGORY_GROUP = "+"'"+categoryGroup+"'"+" ORDER BY PRIORITY";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, rqInChargeCode);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()){
					log.info(rqInChargeCode+" 單號:"+rs.getString("HEAD_ID"));
					headList.add(rs.getString("HEAD_ID"));
				}
			}

			log.info(rqInChargeCode+" 共:"+headList.size());
			int k=1 ;
			for( int j=0;j<headList.size();j++){

				BuPurchaseHead task = buPurchaseHeadDAO.findById(Long.parseLong(headList.get(j).toString()));
				task.setPriority(Double.valueOf(k));
				k++;
				log.info("  task.getPriority();~~"+ task.getPriority());
				buPurchaseHeadDAO.update(task);
			} 
			//}
			return new  ArrayList();
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchass");
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}	
	}*/
	/*
	public void callCalTaskWork(Properties httpRequest){
		try
		{
			String rqInChargeCode = httpRequest.getProperty("rqInChargeCode");

			updateCalTaskWork(rqInChargeCode);
			System.out.println("==開始ReBulidIndex==");
			updateReBulidIndexNo(rqInChargeCode);
			System.out.println("==結束ReBulidIndex==");

		}catch(Exception ex)
		{
			System.out.println("Exception:"+ex.getMessage());
		}
	}*/
	/*
	private void updateReBulidIndexNo(String rqInChargeCode)throws Exception {

		List<BuEmployee> workers = null;

		if (rqInChargeCode.equalsIgnoreCase("ALL")){
			workers = buEmployeeDAO.findByPropertyWorkers("employeeDepartment","103");}
		else{
			workers = buEmployeeDAO.findByPropertyWorkers("employeeCode",rqInChargeCode);
		}

		List<BuPurchaseHead> buPurchaseHeads = null;

		for(BuEmployee worker:workers){
			buPurchaseHeads = buPurchaseHeadDAO.findByPropertyId(
					"BuPurchaseHead",
					new String[] { "rqInChargeCode","orderTypeCode","special" },
					new Object[] { worker.getEmployeeCode(),"IRQ","N" }, "priority");

			int iRow = 0;

			if (buPurchaseHeads.size()>0){
				for(BuPurchaseHead buPurchaseHead : buPurchaseHeads){
					iRow = buPurchaseLineDAO.updateByHeadId(buPurchaseHead.getHeadId());
				}

			}
		}

	}*/
	/*
	private void updateCalTaskWork(String rqInChargeCode)throws Exception {

		List<BuEmployee> workers = null;

		if (rqInChargeCode.equalsIgnoreCase("ALL")){
			workers = buEmployeeDAO.findByPropertyWorkers("employeeDepartment","103");
		}else{
			workers = buEmployeeDAO.findByPropertyWorkers("employeeCode",rqInChargeCode);
		}

		String[] checkType = {"TASK_REQ","TASK_PRJ","TASK_PRE"};//Test
		Long[] hours = {0L,0L,0L};
		String[] dayOffs;
		List<BuPurchaseHead> buPurchaseHeads = null;
		Long totalWorkHours = 0L;                //計算每筆單頭的工作時粓
		Long countWorkHours = 0L;                //計算每筆明細資料的工作時數
		Long rowCount = 0L;                      //每筆單頭工作時細
		Long dayWorkHours = 0L;                  //當日剩餘時數
		int  taskIndex = 0;                      //明細拆階段編號
		Date execWorkDay = null;                 //每階段的執行日期
		Date estimateStartDare = null;           //單頭預計日期(起)
		Date estimateEndDare = null;             //單頭預計日期(迄)
		HashMap resultValue = new HashMap();     //取得分配日期結果
		HashMap resultDateValue = new HashMap(); //取得日期比對結果
		boolean mark = false;                    //註記將固定分配時數改為當日可多餘時數
		boolean fromSource = false;              //判定是否來自原生物件
		double lineIndexNo = 0;                  //每張需求單筆數

		//員工配置
		for(BuEmployee worker:workers){

			hours[0] = (worker.getAdHours()==null?0:worker.getAdHours()); //getAdReqHours 需求
			hours[1] = (worker.getProjectHours()==null?0:worker.getProjectHours()); //getAdPrjHours 專案
			hours[2] = (worker.getAdPreHours()==null?0:worker.getAdPreHours()); //準備
			dayOffs = (worker.getDayOff()==null?"7,1":worker.getDayOff()).split(",");

			for(int i=0;i<checkType.length;i++){
				resultValue = null;
				execWorkDay = new Date();
				mark=false;
				dayWorkHours=0L;

				//判斷是否有配置需求時間
				if (hours[i]>0){
					//取重排需求
					buPurchaseHeads = buPurchaseHeadDAO.findByPropertyId(
							"BuPurchaseHead",
							new String[] { "categoryGroup","rqInChargeCode","orderTypeCode","special" },
							new Object[] { checkType[i].toString(),worker.getEmployeeCode(),"IRQ","N" }, "priority");	 

					if (buPurchaseHeads.size()>0){
						for(BuPurchaseHead buPurchaseHead : buPurchaseHeads){
							lineIndexNo    = 0L;  //需求單筆數reset
							totalWorkHours = 0L; //單頭總工時數reset 
							ArrayList<BuPurchaseLine> tmpBuPurchaseLines=new ArrayList(); //暫存任務明細

							estimateStartDare = null;    //取每張單據預計日期(起)--buPurchaseHead.getEstimateStartDare()
							estimateEndDare = null;      //取每張單據預計日期(迄)--buPurchaseHead.getEstimateEndDare()

							List<BuPurchaseLine> buPurchaseLines = buPurchaseHead.getBuPurchaseLines();

							rowCount = Long.valueOf(String.valueOf(buPurchaseLines.size())) ;

							for(BuPurchaseLine buPurchaseLine : buPurchaseLines){

								fromSource = true;

								lineIndexNo++;

								if (!"VOID".equals(buPurchaseLine.getStatus())){

									countWorkHours = (buPurchaseLine.getExecuteHours()==null?0:buPurchaseLine.getExecuteHours()); //明細工作執行時數                   
									totalWorkHours = totalWorkHours + countWorkHours;                                             //計算每張單總工時

									//狀態:暫存且未展過工作
									if ("SAVE".equals(buPurchaseLine.getStatus())){
										taskIndex=0;

										while(countWorkHours>0){
											resultValue = getCalWorkTime(countWorkHours,(mark?dayWorkHours:hours[i]),dayOffs,execWorkDay);

											if (resultValue.size()>0){

												countWorkHours = (Long)resultValue.get("RemainingHours");

												//來源資料變更設定
												if (fromSource){
													buPurchaseLine.setExecuteHours((Long)resultValue.get("ExecHours"));
													buPurchaseLine.setTaskDate((Date)resultValue.get("ExecDate"));
													buPurchaseLine.setLinePriority(lineIndexNo);
													//buPurchaseLine.setIndexNo(lineIndexNo);
													fromSource = false;
												}
												else
												{
													lineIndexNo++; //統計所有筆數
													taskIndex++;   //新增展開明細筆數
													rowCount++;    //indexNo
													//有新產生的工作項目  									
													BuPurchaseLine line = new BuPurchaseLine();
													line.setHeadId(buPurchaseLine.getHeadId());
													line.setSpecInfo(buPurchaseLine.getSpecInfo()+ "(" + String.valueOf(taskIndex) + ")");
													line.setTaskType(buPurchaseLine.getTaskType());
													line.setExecuteHours((Long)resultValue.get("ExecHours"));
													line.setTaskDate((Date)resultValue.get("ExecDate"));
													line.setCategoryCode(buPurchaseLine.getCategoryCode());
													line.setStatus("SAVE");
													line.setIndexNo(rowCount);
													line.setLinePriority(lineIndexNo);
													//line.setIndexNo(lineIndexNo);
													tmpBuPurchaseLines.add(line);
												}

												execWorkDay = (Date)resultValue.get("StartDate");

												resultDateValue = getStartOrEndDate(estimateStartDare,estimateEndDare,(Date)resultValue.get("ExecDate"));

												if (resultDateValue.size()>0){
													estimateStartDare = (Date)resultDateValue.get("StartDate");
													estimateEndDare = (Date)resultDateValue.get("EndDate");
												}
												else
												{
													//無日期回傳，記錄errLog
												}
											}
											else
											{
												//無回傳，記錄errLog
												countWorkHours = 0L;
												System.out.println("無回傳");
											}
											//在回圈跑在表工作還未分派完，要以固定分配時數為主
											mark=false;
											dayWorkHours=0L;
										}


										//處理當天還有可配置時數
										if (countWorkHours<0){
											//註記將固定分配時數改為當日可多餘時數
											mark=true;
											//當日剩餘時數
											dayWorkHours=-countWorkHours;
										}
									}
									else
									{
										//buPurchaseLine.setIndexNo(lineIndexNo); //完成重編號碼
										buPurchaseLine.setLinePriority(lineIndexNo);

										if (buPurchaseLine.getTaskDate()!=null){
											resultDateValue = getStartOrEndDate(estimateStartDare,estimateEndDare,buPurchaseLine.getTaskDate());

											if (resultDateValue.size()>0){
												estimateStartDare = (Date)resultDateValue.get("StartDate");
												estimateEndDare = (Date)resultDateValue.get("EndDate");
											}
											else
											{
												//無日期回傳，記錄errLog
												System.out.println("無日期回傳");
											}
										}
									}
								}
								else
								{
									//作廢重編號碼
									buPurchaseLine.setLinePriority(lineIndexNo);
								}
							}

							if (tmpBuPurchaseLines!=null){
								for(BuPurchaseLine tmpBuPurchaseLine : tmpBuPurchaseLines){
									buPurchaseLines.add(tmpBuPurchaseLine);
								}
							}

							buPurchaseHead.setTotalHours(Integer.valueOf(String.valueOf(totalWorkHours)));
							buPurchaseHead.setEstimateStartDare(estimateStartDare);
							buPurchaseHead.setEstimateEndDare(estimateEndDare);
							buPurchaseHead.setBuPurchaseLines(buPurchaseLines);
							buPurchaseHeadDAO.save(buPurchaseHead);
						}
					}
					else
					{
						//任務無資料
						System.out.println("任務無資料111");
					}

				}
				else{
					//無配罝時間記錄Log
					System.out.println("無配罝時間記錄");
				}

			}
		}

		System.out.println("====結束計算====");

	}*/
	/*
	private HashMap getStartOrEndDate(Date sDate,Date eDate,Date checkDate)
	{
		HashMap returnValue = new HashMap();

		if (sDate==null){sDate=checkDate;}
		if (eDate==null){eDate=checkDate;}

		if (checkDate.compareTo(sDate)<0){sDate=checkDate;}
		if (checkDate.compareTo(eDate)>=0){eDate=checkDate;}

		returnValue.put("StartDate", sDate);
		returnValue.put("EndDate", eDate);

		return returnValue;
	}*/
	/*
	private HashMap getCalWorkTime(Long workHours,Long settingHours,String[] dayOff,Date startDate)
	{
		HashMap returnValue = new HashMap();
		Long countHours = 0L;       //計算分配工時
		int dayOfWeek;              //DayWeek number
		int appendDay = 0;          //延展天數
		boolean IsWorkDay = false;  //是否工作日
		boolean IsHoliDay = false;  //是否假日

		// returnValue
		 //  1.RemainingHours:剩餘工時
		 //  2.StartDate:下一明細工作的開始日期
		 //  3.ExecDate:執行日期
		  // 4.ExecHours:執行工時


		Calendar c = Calendar.getInstance(); 
		c.setTime(startDate);

		countHours = workHours - settingHours;

		//檢核是否為工作日
		while(!IsWorkDay){
			IsHoliDay = false;     //每次進來即reset
			c.add(Calendar.DATE, appendDay);
			dayOfWeek = c.get(c.DAY_OF_WEEK);

			for(int d=0;d<dayOff.length;d++)
			{
				if (Integer.valueOf(dayOff[d]).equals(dayOfWeek))
				{
					IsHoliDay=true;
					appendDay=1;
					break;
				}
			}

			if (IsHoliDay){IsWorkDay=false;} else{IsWorkDay=true;}
		}

		returnValue.put("RemainingHours", countHours);
		returnValue.put("ExecDate", c.getTime());
		returnValue.put("ExecHours", (countHours>=0?settingHours:workHours));

		if (countHours>=0){
			c.add(Calendar.DATE, 1);
			startDate = c.getTime(); //取下一個工作的開始日期
		}

		returnValue.put("StartDate", startDate);

		return returnValue;

	}*/
//***********************************************************以下未知**********************************************************
	/**
	 * 暫存 商控 送出 商控
	 *
	 * @param subject
	 * @param templateFileName
	 * @param display
	 * @param mailAddress
	 * @param attachFiles
	 * @param cidMap
	 */
	/*
	public void getAccessMailList(BuPurchaseHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap,String action) throws Exception {
		log.info("getAccessMailList:"+action);
		try {

			String orderTypeCode = "LOA";
			//String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			StringBuffer html = new StringBuffer();
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			// 取得發信種類名稱-權限
			String itemCategory = "KweAccess";

			// #1 取得配置檔得到 寄信的報表與

			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListLOA","KweAccess"); // +orderTypeCode

			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Developer@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				if(action.equals("SIGNING")){
					String accAdmin = buCommonPhraseLine.getAttribute4();
					if (StringUtils.hasText(accAdmin)) { 
						html.append("權限申請單：LOA" + head.getOrderNo() + "申請調整權限");
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, accAdmin);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("cbr_0922@tasameng.com.tw");
							} else {
								emailError.append("查審核人工號：").append(accAdmin).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查審核人工號：").append(accAdmin).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				}else if(action.equals("SUBMIT")){
					html.append("權限申請單：IRQ" + head.getOrderNo() + "我們會盡快幫您處理");
					String createdBy = head.getCreatedBy();
					if (StringUtils.hasText(createdBy)) { // 起單人
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, createdBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("cbr_0922@tasameng.com.tw");
							} else {
								emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {

							emailError.append("查起單人無工號：").append(createdBy).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");


							emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");

						}
					}
				}else{
				}
				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}
			String orderNo = head.getOrderNo();
			subject = "KWE權限申請單:".concat(orderNo);
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}

			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			System.out.println("emailError.toString() = " + emailError.toString());
			if (StringUtils.hasText(emailError.toString())) {
				mailAddress.add("cbr_0922@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				MailUtils.sendMail(subjectError, templateFileName, display, mailAddress, attachFiles, cidMap);
			}
			System.out.println("subject = " + subject);
			System.out.println("html = " + html.toString());
			// 設定樣板的內容值
			display.put("display", html.toString());

			// 發信通知
			MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
			log.info("getAccessMailList8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}
	}*/
	/**
	 * 需求單 送出 自動發信
	 *
	 * @param subject
	 * @param templateFileName
	 * @param display
	 * @param mailAddress
	 * @param attachFiles
	 * @param cidMap
	 */
	/*
	public void getAutoMailList(BuPurchaseHead head, String subject, String templateFileName, Map display,
			List mailAddress, List attachFiles, Map cidMap,String action,String createBy) throws Exception {
		log.info("getAccessMailList:"+action);
		try {

			String orderTypeCode = "IRQ";
			//String itemCategory = AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			StringBuffer html = new StringBuffer();
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			// 取得發信種類名稱-權限
			String itemCategory = "KweIRQ";

			// #1 取得配置檔得到 寄信的報表與

			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("MailListIRQ","IRQ"); // +orderTypeCode

			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Henry_lee@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				description = "";
			} else {
				if(action.equals("PLAN")){
					String accAdmin = buCommonPhraseLine.getAttribute4();
					if (StringUtils.hasText(accAdmin)) { 
						log.info("testNMailList~~~");
						html.append("需求單：IRQ" + head.getOrderNo() + "資訊需求");
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, createBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("Henry_lee@tasameng.com.tw");
							} else {
								emailError.append("查審核人工號：").append(accAdmin).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查審核人工號：").append(accAdmin).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				}else if(action.equals("SUBMIT")){
					html.append("您建立一張需求單，單號為：IRQ" + head.getOrderNo() + "我們已收到您的需求單，會盡快幫您處理，謝謝，"+"處理人員為："+head.getOtherGroup());
					String createdBy = head.getCreatedBy();
					if (StringUtils.hasText(createdBy)) { // 起單人
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, createdBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany()==null?"":buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								//mailAddress.add("IT-Manager-DG@tasameng.com.tw");
							} else {
								emailError.append("查起單人工號：").append(createdBy).append(" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查起單人無工號：").append(createdBy).append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				}else{
				}
				// 前半部主旨
				description = buCommonPhraseLine.getDescription();
			}
			String orderNo = head.getOrderNo();
			subject = "KWE資訊需求單:".concat(orderNo);
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}

			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			System.out.println("emailError.toString() = " + emailError.toString());
			if (StringUtils.hasText(emailError.toString())) {
				//mailAddress.add("IT-Manager-DG@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory + "無配置檔異常 ";
				MailUtils.sendMail(subjectError, templateFileName, display, mailAddress, attachFiles, cidMap);
			}
			System.out.println("subject = " + subject);
			System.out.println("html = " + html.toString());
			// 設定樣板的內容值
			display.put("display", html.toString());

			// 發信通知
			MailUtils.sendMail(subject, templateFileName, display, mailAddress, attachFiles, cidMap);
			log.info("getAccessMailList8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}
	}*/
	
	public  List<Properties> getWeekDate(Properties httpRequest)throws Exception{
		try {
			Properties pro = new Properties();
			List<Properties> re = new ArrayList();
			log.info("進入getWeek");
			PreparedStatement stmt = null;			
			ResultSet rs = null;
			Connection conn = null;
			String nowDate = DateUtils.format(DateUtils.getCurrentDate(), "yyyy/MM/dd");			
			Date nextDate = DateUtils.addDays(new Date(), 7);
			String StrnextDate = DateUtils.format(nextDate, "yyyy/MM/dd");
			log.info("nowDate="+nowDate);
			log.info("StrnextDate="+StrnextDate);
			Calendar c2 = Calendar.getInstance();
			Calendar c3 = Calendar.getInstance();
			log.info("C3333 = "+c3.getTimeInMillis());
			Calendar c4 = Calendar.getInstance();
			Calendar c5 = Calendar.getInstance();
			log.info("year="+Integer.parseInt(nowDate.substring(0, 4))+"month="+ Integer.parseInt(nowDate.substring(5, 7))+"date="+ Integer.parseInt(nowDate.substring(8, 10)));
			c3.set(Integer.parseInt(nowDate.substring(0, 4)), Integer.parseInt(nowDate.substring(5, 7)), Integer.parseInt(nowDate.substring(8, 10)));
			c2.set(Calendar.YEAR, Integer.parseInt(nowDate.substring(0, 4)));
			c2.set(Calendar.MONTH, Integer.parseInt(nowDate.substring(5, 7)) -1);
			c2.set(Calendar.WEEK_OF_MONTH, c3.get(c3.WEEK_OF_MONTH));
			c2.set(Calendar.DAY_OF_WEEK, 2);
			Date d2 = new Date(c2.getTimeInMillis());
			String d2Date = DateUtils.format(d2, "yyyy/MM/dd");
			log.info("D2= " +d2Date);
			c5.set(Integer.parseInt(StrnextDate.substring(0, 4)), Integer.parseInt(StrnextDate.substring(5, 7)), Integer.parseInt(StrnextDate.substring(8, 10)));
			c4.set(Calendar.YEAR, Integer.parseInt(nowDate.substring(0, 4)));
			c4.set(Calendar.MONTH, Integer.parseInt(nowDate.substring(5, 7)) -1);
			c4.set(Calendar.WEEK_OF_MONTH, c5.get(c5.WEEK_OF_MONTH));
			c4.set(Calendar.DAY_OF_WEEK, 1);
			//Date d4 = new Date(c4);
			log.info("D4= " +c4.getTime());
			String d4Date = DateUtils.format(c4.getTime(), "yyyy/MM/dd");
			log.info("D4-2= " +d4Date);
			pro.setProperty("startDate",        AjaxUtils.getPropertiesValue(d2Date,    ""));
			pro.setProperty("endDate",        AjaxUtils.getPropertiesValue(d4Date,    ""));			
			re.add(pro);
						
			
				return re;						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
				
	}
	/*
	public List<Properties> updateTotaltimes(Properties httpRequest) throws Exception{
		try {
			log.info("updateTotaltimes :::: ");
			Properties pro = new Properties();
			List re = new ArrayList();
			Map resultMap = new HashMap();
			String brandCode     	 = httpRequest.getProperty("brandCode");			
			String startDate 		 = httpRequest.getProperty("startDate");
			String endDate 		 = httpRequest.getProperty("endDate");
			Date stDate = DateFormat.getDateInstance().parse(startDate);			
				//DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYMMDD, startDate);
			Date enDate = DateFormat.getDateInstance().parse(endDate);			
			String parseSdate = DateUtils.format(stDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String parseEdate = DateUtils.format(enDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			log.info("startDate"+startDate+"endDate"+endDate);
			log.info("parseSdate"+parseSdate+"parseEdate"+parseEdate);
			//Map Summary = adTaskReviewViewDAO.sumTotaltimes(parseSdate,parseEdate);
			//log.info("(String)Summay"+(String)Summary.get("MON"));
			//String SumMon = (Double)Summary.get("MON");
			//pro.setProperty("mon",SumMon);
			//pro.setProperty("mon",(String)Summary.get("MON"));
			String tmpSqlItem= "select sum( MON ) AS MON, sum( TUE ) AS TUE, sum( WED ) AS WED, sum( THU ) AS THU, sum( FRI ) AS FRI, sum( SAT ) AS SAT, sum( SUN ) AS SUN from AD_TASK_REVIEW_VIEW WHERE COUNTING_DATE >= "+"TO_DATE("+parseSdate+",'yyyyMMdd') and COUNTING_DATE <= "+"TO_DATE("+parseEdate+",'yyyyMMdd')";           
			//String tmpSqlItem= "select sum( MON ) AS MON from AD_TASK_REVIEW_VIEW WHERE COUNTING_DATE >= "+"TO_DATE("+parseSdate+",'yyyyMMdd') and COUNTING_DATE <= "+"TO_DATE("+parseEdate+",'yyyyMMdd')";
			log.info("tmpSqlItem"+tmpSqlItem);
			List List = nativeQueryDAO.executeNativeSql(tmpSqlItem);

			for (Object obj : List) {
				pro.setProperty("mon",String.valueOf((((Object[])obj)[0])==null?0:((Object[])obj)[0]));	
				pro.setProperty("tue",String.valueOf((((Object[])obj)[1])==null?0:((Object[])obj)[1]));	
				pro.setProperty("wed",String.valueOf((((Object[])obj)[2])==null?0:((Object[])obj)[2]));	
				pro.setProperty("thu",String.valueOf((((Object[])obj)[3])==null?0:((Object[])obj)[3]));	
				pro.setProperty("fri",String.valueOf((((Object[])obj)[4])==null?0:((Object[])obj)[4]));	
				pro.setProperty("sat",String.valueOf((((Object[])obj)[5])==null?0:((Object[])obj)[5]));	
				pro.setProperty("sun",String.valueOf((((Object[])obj)[6])==null?0:((Object[])obj)[6]));	
			}
			//for (Iterator iteratorV = List.iterator(); iteratorV.hasNext();) {
			//	log.info("have date:===");
			//	log.info(iteratorV.next().toString());
			//	Object[] obj = (Object[])iteratorV.next();
			//	log.info("obj:"+obj);
			//	if(null != obj ){
			//		log.info("obj"+(String)obj[0]);
			//		pro.setProperty("mon",(String)obj[0]);	
			//		resultMap.put("mon",(String)obj[0]);
			//		log.info("MapGet"+(String)obj[0]);
			//	}				
			//}
			
			re.add(pro);			
					
			return  re;
			
			//return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (Exception e) {
			throw new Exception("重新計算失敗，原因：" + e.toString());
		}		
	}
	*/
	/*
	public List<Properties> updateStartTime(Properties httpRequest) 	throws Exception{
		try {		
				HashMap resultMap = new HashMap();
				Properties pro = new Properties();
				List re = new ArrayList();
				try {
					
					Long lineId = NumberUtils.getLong(httpRequest.getProperty("lineId"));
					Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
					log.info("lineId+"+lineId);log.info("headId+"+headId);
					// ====================取得條件資料======================
					BuPurchaseLine br = (BuPurchaseLine) buPurchaseLineDAO
					.findById("BuPurchaseLine", lineId);
					if (br != null) {
						Date date = new Date();
						String StrTime = DateUtils.getFormatTime(date);
						log.info("StrTime"+StrTime);			
						br.setExecuteTimeStart(StrTime);
						buPurchaseLineDAO.update(br);
						pro.setProperty("executeTimeStart", StrTime);
						re.add(pro);
					} else {
						//AjaxUtils.copyJSONBeantoPojoBean(formBindBean, br);
						throw new Exception("找不到明細");
					}
					return re;
				} catch (Exception ex) {
					log.error("工作回報存檔時發生錯誤，原因：" + ex.toString());
					throw new Exception("工作回報單存檔時發生錯誤，原因：" + ex.getMessage());
				}
			}catch (Exception e) {
			throw new Exception("重新計算失敗，原因：" + e.toString());
		}		
	}*/
	
	/**AJAX Line Change
	 * @param headObj(brandCode,orderTypeCode,itemCode)
	 * @throws FormException
	 */
	/*
	public List<Properties> getAJAXLineDataItemName(Properties httpRequest) throws FormException, Exception {
		List re = new ArrayList();
		Properties pru = new Properties();
		try{
			//String itemNo     = httpRequest.getProperty("itemNo");
			String itemName   = httpRequest.getProperty("itemName");
			String specInfo   = httpRequest.getProperty("specInfo");
			String supplierNo   = httpRequest.getProperty("supplier");
			String purUnitPrice   = httpRequest.getProperty("purUnitPrice");
			String reUnitPric     = httpRequest.getProperty("reUnitPric");
			//String gruopNo  = httpRequest.getProperty("gruopNo");
			//log.info("gruopNo~~~"+gruopNo);
			log.info("itemNo~~~"+itemName);
			if (StringUtils.hasText(itemName)){
				PrItem prItem = prItemDAO.findItemName(itemName);
				log.info("~~~~~~~~~~~~~");
				if(null!=prItem){
					log.info("~~~@~~~~");
					//PrSupplier prSupplier = prSupplierDAO.findById(id);
					pru.setProperty("itemNo",    AjaxUtils.getPropertiesValue(prItem.getItemNo(),     "itemNo"));
					pru.setProperty("purUnitPrice",AjaxUtils.getPropertiesValue(prItem.getPurUnitPrice(), "purUnitPrice"));
					pru.setProperty("reUnitPrice", AjaxUtils.getPropertiesValue(prItem.getReUnitPrice(), "reUnitPric"));
					pru.setProperty("specInfo",    AjaxUtils.getPropertiesValue(prItem.getSpecInfo(),     "specInfo"));
					pru.setProperty("supplier",    AjaxUtils.getPropertiesValue(prItem.getSupplier(),     "supplier"));
					re.add(pru);
				}else
				{
					pru.setProperty("itemNo",      " ");
					pru.setProperty("purUnitPrice",  " ");
					pru.setProperty("reUnitPrice",   " ");
					pru.setProperty("specInfo",      " ");
					pru.setProperty("supplier",      " ");
				}
			} 
		}catch(Exception e){
			log.error(e.toString());
		}
		return re;
	}*/
}

