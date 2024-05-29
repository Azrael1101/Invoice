package tw.com.tm.erp.hbm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import net.sf.hibernate.expression.Order;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.InsufficientQuantityException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBranch;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemSerial;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.Sequence;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SoReceiptHead;
import tw.com.tm.erp.hbm.bean.SoReceiptLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.bean.TmpImportPosItem;
import tw.com.tm.erp.hbm.bean.TmpImportPosItemId;
import tw.com.tm.erp.hbm.bean.TmpImportPosPayment;
import tw.com.tm.erp.hbm.bean.TmpImportPosPaymentId;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuExchangeRateDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemSerialDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.SiResendDAO;
import tw.com.tm.erp.hbm.dao.SoDepartmentOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderPaymentDAO;
import tw.com.tm.erp.importdb.T2PosImportData;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.FileTools;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.updateOnhand;
import tw.com.tm.erp.utils.sp.AppGetSaleItemInfoService;

/**
 * @author T15394
 * 
 */
public class SoSalesOrderMainService {

    private static final Log log = LogFactory.getLog(SoSalesOrderMainService.class);

    private BuCommonPhraseService buCommonPhraseService;

    private BuBasicDataService buBasicDataService;

    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;

    private SoSalesOrderItemDAO soSalesOrderItemDAO;

    private BuBrandDAO buBrandDAO;

    private BuOrderTypeService buOrderTypeService;

    private BuCustomerWithAddressViewService buCustomerWithAddressViewService;

    private ImWarehouseService imWarehouseService;

    private BuShopMachineService buShopMachineService;

    private BuExchangeRateDAO buExchangeRateDAO;

    private BuShopService buShopService;

    private AppGetSaleItemInfoService appGetSaleItemInfoService;

    private ImItemService imItemService;

    private ImItemEanPriceViewService imItemEanPriceViewService;

    private SiProgramLogAction siProgramLogAction;

    private BuBrandService buBrandService;

    private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;

    private ImItemPriceOnHandViewService imItemPriceOnHandViewService;

    private ImPromotionViewDAO imPromotionViewDAO;

    private ImItemDAO imItemDAO;

    private ImOnHandDAO imOnHandDAO;

    private ImDeliveryLineDAO imDeliveryLineDAO;

    private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;

    private SoSalesOrderPaymentDAO soSalesOrderPaymentDAO;

    private FiInvoiceHeadDAO fiInvoiceHeadDAO;

    private ImDeliveryMainService imDeliveryMainService;

    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private ImItemCategoryService imItemCategoryService;

    private BuShopMachineDAO buShopMachineDAO;
    
    private SoDepartmentOrderHeadDAO soDepartmentOrderHeadDAO;

    private ImItemSerialDAO imItemSerialDAO;

    private CmDeclarationHeadDAO cmDeclarationHeadDAO;
    
    private BuCountryDAO buCountryDAO;

    private TmpImportPosItemService tmpImportPosItemService;

    private TmpImportPosPaymentService tmpImportPosPaymentService;
    
    private ImStorageAction imStorageAction;
    
    private ImStorageService imStorageService;
    
    private BuCustomerDAO buCustomerDAO;
    
    public static final String PROGRAM_ID = "SO_SALES_ORDER";

    private static final String POSTYPECODE = "SOP";

    private final static String MAIL_SKIN = "CommonTemplate.ftl";

    private final static String T1_POS_ADMIN = "T1POS_Administrator";

    private final static String T2_POS_ADMIN = "T2POS_Administrator";
    
    public static final String STATUS = "Y";
    
    private SiResendDAO siResendDAO;
    
    private ImTransationDAO imTransationDAO;
    
    private BuOrderTypeDAO buOrderTypeDAO;

    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;//SoSalesOrderMainService.java整併 Maco 2016.12.08

    private ImPromotionDAO imPromotionDAO; //17_app brian 20200818

    public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG,              
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,                    	     
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_STRING,                    	                                   
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,                      	       
    	AjaxUtils.FIELD_TYPE_LONG, 	 AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,                       	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,                      	       
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,                    	       
    	AjaxUtils.FIELD_TYPE_STRING };                                               	       



    public static final String[] GRID_FIELD_NAMES = { "indexNo"            
    	, "itemCode",	    "itemCName"                                             
    	, "warehouseCode", "warehouseName",                                         
    	"originalForeignUnitPrice","originalUnitPrice","bonusPointAmount", "discountRate"
    	,"actualForeignUnitPrice",  "actualUnitPrice"                        
    	, "currentOnHandQty",	    "quantity",                                 
    	"originalForeignSalesAmt", "originalSalesAmount",                     
    	"actualForeignSalesAmt", "actualSalesAmount",                         
    	"deductionForeignAmount", "deductionAmount",                          
    	"taxType",                                                          		
    	"taxRate", "isTax",                                                   
    	"promotionCode", "discountType",                                      
    	"discount", "vipPromotionCode",                                       
    	"vipDiscountType", "vipDiscount",                                     
    	"watchSerialNo", "watchSerialNoPicker",                               
    	"depositCode",	    "isUseDeposit",                                   
    	"isServiceItem", "taxAmount",                                         
    	"importCost",	    "importCurrencyCode",                               
    	"importDeclNo", "reserve4",                                           
    	"importDeclType",	    "importDeclDate",                               
    	"importDeclSeq", "perUnitAmount", "lotNo",                                             
    	"usedIdentification",	    "usedCardId",                               
    	"usedCardType", "usedDiscountRate",                                   
    	"itemDiscountType", "combineCode", "allowMinusStock"   ,                            
    	"allowWholeSale", "lineId",                                           
    	"isLockRecord", "isDeleteRecord",                                     
    	"message" 
    };                                                          

    public static final String[] GRID_FIELD_DEFAULT_VALUES = { "",      
    	"", "",                                                               
    	"",	    "",                                                           
    	"0.0","0.0","0.0", "100.0" ,                                                
    	"0.0", "0.0",                                                         
    	"", "",                                                               
    	"", "",                                                               
    	"", "",                                                               
    	"0.00",	    "0.00",                                                   
    	"3",                                                         	      
    	"5", "",                                                              
    	"", "",                                                               
    	"", "",                                                               
    	"", "",                                                               
    	"", "",                                                               
    	"",	    "",                                                           
    	"", "0.0",                                                            
    	"0.0", "",                                                            
    	"", "",                                                               
    	"", "",                                                               
    	"", "", "",                                                               
    	"", "",                                                               
    	"",	    "100.0",                                                      
    	"", "", "",                                                               
    	"", "",                                                               
    	AjaxUtils.IS_LOCK_RECORD_FALSE,	    AjaxUtils.IS_DELETE_RECORD_FALSE, 
    	"" 
    };  

    /**
     * 查詢欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
    	"orderTypeCode", "orderNo", "salesOrderDate","schedule", "customerPoNo",
    	"transactionSeqNo","customsNo","superintendentName", "defaultWarehouseCode", "totalActualSalesAmount",
    	"posMachineCode", "statusName", "customerName","eventCode", "lastUpdatedBy",
    	"lastUpdateDate", "headId" };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", "", 
    	"", "", "", "", "", 
    	"", "", "", "", "",
    	"", "" };

    /**
     * customerPoNo查詢欄位
     */
    public static final String[] GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO = {
    	"orderTypeCode", "orderNo", "salesOrderDate", "customerPoNo",
    	"defaultWarehouseCode", "posMachineCode", "statusName",
    	"lastUpdatedBy", "lastUpdateDate", "headId" };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO = {
    	"", "", "", "", "", "", "", "", "", ""
    };

    public static final String[] GRID_FIELD_NAMES_PAYMENT = { "indexNo",
    	"posPaymentType", "foreignCurrencyCode", "foreignAmount",
    	"exchangeRate", "localCurrencyCode", "localAmount", "discountRate",
    	"payNo", "remark1", "posPaymentId", "isLockRecord",
    	"isDeleteRecord", "message" };

    public static final int[] GRID_FIELD_TYPES_PAYMENT = {
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE,
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

    public static final String[] GRID_FIELD_DEFAULT_VALUES_PAYMENT = { "", "",
    	"NTD", "", "1.0", "NTD", "", "", "", "", "",
    	AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };

    public String save(SoSalesOrderHead saveObj) throws FormException,
    Exception {
    	soSalesOrderHeadDAO.save(saveObj);
    	return MessageStatus.SUCCESS;
    }

    public void setBuCommonPhraseService(
    		BuCommonPhraseService buCommonPhraseService) {
    	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
    	this.buBasicDataService = buBasicDataService;
    }

    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }

    public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
    	this.soSalesOrderItemDAO = soSalesOrderItemDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
    	this.buBrandDAO = buBrandDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
    	this.buOrderTypeService = buOrderTypeService;
    }

    public void setBuCustomerWithAddressViewService(
    		BuCustomerWithAddressViewService buCustomerWithAddressViewService) {
    	this.buCustomerWithAddressViewService = buCustomerWithAddressViewService;
    }

    public void setImWarehouseService(ImWarehouseService imWarehouseService) {
    	this.imWarehouseService = imWarehouseService;
    }
    
    public void setBuShopMachineService(
    		BuShopMachineService buShopMachineService) {
    	this.buShopMachineService = buShopMachineService;
    }
    
    public void setSoDepartmentOrderHeadDAO(
    		SoDepartmentOrderHeadDAO soDepartmentOrderHeadDAO) {
    	this.soDepartmentOrderHeadDAO = soDepartmentOrderHeadDAO;
    }

    public void setBuExchangeRateDAO(BuExchangeRateDAO buExchangeRateDAO) {
    	this.buExchangeRateDAO = buExchangeRateDAO;
    }

    public void setBuShopService(BuShopService buShopService) {
    	this.buShopService = buShopService;
    }

    public void setAppGetSaleItemInfoService(
    		AppGetSaleItemInfoService appGetSaleItemInfoService) {
    	this.appGetSaleItemInfoService = appGetSaleItemInfoService;
    }

    public void setImItemService(ImItemService imItemService) {
    	this.imItemService = imItemService;
    }

    public void setImItemEanPriceViewService(
    		ImItemEanPriceViewService imItemEanPriceViewService) {
    	this.imItemEanPriceViewService = imItemEanPriceViewService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }

    public void setBuBrandService(BuBrandService buBrandService) {
    	this.buBrandService = buBrandService;
    }

    public void setBuEmployeeWithAddressViewService(
    		BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
    	this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
    }

    public void setImItemPriceOnHandViewService(
    		ImItemPriceOnHandViewService imItemPriceOnHandViewService) {
    	this.imItemPriceOnHandViewService = imItemPriceOnHandViewService;
    }

    public void setImPromotionViewDAO(ImPromotionViewDAO imPromotionViewDAO) {
    	this.imPromotionViewDAO = imPromotionViewDAO;
    }

    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }

    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
    	this.imOnHandDAO = imOnHandDAO;
    }

    public void setImDeliveryLineDAO(ImDeliveryLineDAO imDeliveryLineDAO) {
    	this.imDeliveryLineDAO = imDeliveryLineDAO;
    }

    public void setCmDeclarationOnHandDAO(
    		CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
    	this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
    }

    public void setSoSalesOrderPaymentDAO(
    		SoSalesOrderPaymentDAO soSalesOrderPaymentDAO) {
    	this.soSalesOrderPaymentDAO = soSalesOrderPaymentDAO;
    }

    public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
    	this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
    }

    public void setImDeliveryMainService(
    		ImDeliveryMainService imDeliveryMainService) {
    	this.imDeliveryMainService = imDeliveryMainService;
    }

    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
    	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }

    public void setImItemCategoryService(
    		ImItemCategoryService imItemCategoryService) {
    	this.imItemCategoryService = imItemCategoryService;
    }

    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
    	this.buShopMachineDAO = buShopMachineDAO;
    }

    public void setImItemSerialDAO(ImItemSerialDAO imItemSerialDAO) {
    	this.imItemSerialDAO = imItemSerialDAO;
    }

    public void setCmDeclarationHeadDAO(
    		CmDeclarationHeadDAO cmDeclarationHeadDAO) {
    	this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
    }

    public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
    	this.buCountryDAO = buCountryDAO;
    }

    public void setTmpImportPosItemService(
    		TmpImportPosItemService tmpImportPosItemService) {
    	this.tmpImportPosItemService = tmpImportPosItemService;
    }

    public void setTmpImportPosPaymentService(
    		TmpImportPosPaymentService tmpImportPosPaymentService) {
    	this.tmpImportPosPaymentService = tmpImportPosPaymentService;
    }

    public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}
    
    public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}
    
    public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
		this.buCustomerDAO = buCustomerDAO;
	}
    
    public void setSiResendDAO(SiResendDAO siResendDAO){
    	this.siResendDAO = siResendDAO;
    }
    
    public void setImTransationDAO(ImTransationDAO imTransationDAO){
    	this.imTransationDAO = imTransationDAO;
    }
//SoSalesOrderMainService.java整併 Maco 2016.12.08
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
        }
//SoSalesOrderMainService.java整併 Maco 2016.12.08
    public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
    	this.buOrderTypeDAO = buOrderTypeDAO;
        }
//17_app brian 20200818    
    public void setImPromotionDAO(ImPromotionDAO imPromotionDAO){
    	this.imPromotionDAO = imPromotionDAO;
    }
    /**
     * 初始化 bean 額外顯示欄位
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception {
    	Map resultMap = new HashMap(0);
    	Map multiList = new HashMap(0);
    	try {
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		Long formId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "formId"));
    		List orderTypes = buOrderTypeService.findOrderbyType(brandCode, "SO");
    		if (orderTypes != null) {
//    			multiList.put("orderTypes", AjaxUtils.produceSelectorData(
//    					orderTypes, "orderTypeCode", "name", true, false));
    		} else {
    			throw new Exception("查無品牌代號：" + brandCode + "的銷售單別資訊！");
    		}
    		SoSalesOrderHead head = this.findSoSalesOrderHead(formId, otherBean, resultMap);
    		//this.getParameterValues(head, resultMap);
    		resultMap.put("form", head);
    		//resultMap.put("multiList", multiList);
    		
    		//for 儲位用 
//    		if(imStorageAction.isStorageExecute(head)){
//    			//建立儲位單
//    			Map storageMap = new HashMap();
//    			storageMap.put("storageTransactionDate", "salesOrderDate");
//    			storageMap.put("storageTransactionType", ImStorageService.OUT);
//    			//轉出或轉入
//    			//if("IMR".equals(buOrderType.getTypeCode())){
//    				storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
//    				//storageMap.put("arrivalWarehouseCode", "defaultWarehouseCode");
//    			//}else{
//    				//storageMap.put("deliveryWarehouseCode", "defaultWarehouseCode");
//    				//storageMap.put("arrivalWarehouseCode", "");
//    			//}
//    			
//    			ImStorageHead imStorageHead = imStorageAction.executeImStorageHead(storageMap, head);
//
//    			resultMap.put("storageHeadId", imStorageHead.getStorageHeadId());
//    			resultMap.put("beanHead", "SoSalesOrderHead");
//    			resultMap.put("beanItem", "soSalesOrderItems");
//    			resultMap.put("quantity", "quantity");
//    			resultMap.put("storageTransactionType", ImStorageService.OUT);
//    			resultMap.put("storageStatus", "#F.status");
//    			//轉出或轉入
//    			//if("IMR".equals(buOrderType.getTypeCode())){
//    				resultMap.put("deliveryWarehouse", "#F.defaultWarehouseCode");
//    				//resultMap.put("arrivalWarehouse", "#F.defaultWarehouseCode");
//    			//}else{
//    				//resultMap.put("deliveryWarehouse", "#F.defaultWarehouseCode");
//    				//resultMap.put("arrivalWarehouse", "");
//    			//}
//    		}
    		
    		return resultMap;
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("銷貨單初始化失敗，原因：" + ex.toString());
    		throw new Exception("銷貨單初始化失敗，原因：" + ex.getMessage());
    	}
    }

    /**
     * 初始化 bean 額外顯示欄位
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeCustomerPoNoInitial(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	int maxPoNoLength = 10;
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginEmployeeCode");

	    resultMap.put("brandCode", loginBrandCode);
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode)
		    .getBrandName());
	    resultMap.put("maxPoNoLength",maxPoNoLength); // 預設值
	    resultMap.put("loginEmployeeCode", loginEmployeeCode);
	    resultMap.put("loginEmployeeName", UserUtils
		    .getUsernameByEmployeeCode(loginEmployeeCode));
	    resultMap.put("multiList", multiList);
	    return resultMap;
	} catch (Exception ex) {
	    log.error("銷貨售貨單更新初始化失敗，原因：" + ex.toString());
	    throw new Exception("銷貨售貨單更新初始化失敗，原因：" + ex.getMessage());
	}
    }

    /**
     * 查詢配貨單並且使畫面初使化
     * 
     * @return FiBudgetModHead
     * @throws Exception
     */
    public SoSalesOrderHead findSoSalesOrderHead(long formId, Object otherBean,
	    Map resultMap) throws FormException, Exception {
	SoSalesOrderHead form = formId > 0 ? findById(formId) : executeNew(otherBean);
	if (form != null) {
	    BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
	    resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
	    resultMap.put("brandName", buBrand.getBrandName());
	    resultMap.put("branchCode", "T2(BRANCH)");//buBrand.getBuBranch().getBranchCode());              
//	    ImWarehouse warehousePO = (ImWarehouse) imWarehouseService.findById(form.getDefaultWarehouseCode());
//	    resultMap.put("warehouseManager", warehousePO.getWarehouseManager());
	    resultMap.put("superintendentName", "SuperintendentCode");//UserUtils.getUsernameByEmployeeCode(form.getSuperintendentCode()));
	    resultMap.put("createdByName", "CreatedBy");//UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
	    return form;
	} else {
	    throw new FormException("查無配貨單單主鍵：" + formId + "的資料！");
	}
    }

    /**
     * 依據primary key為查詢條件，取得銷貨單主檔
     * 
     * @param headId
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public SoSalesOrderHead findById(Long headId) throws Exception {
	try {
	    SoSalesOrderHead salesOrder = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, headId);
	    return salesOrder;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 產生新的銷貨單
     * 
     * @param otherBean
     * @return
     * @throws Exception
     */                                                                                                                                
    public SoSalesOrderHead executeNew(Object otherBean) {
    	SoSalesOrderHead form = new SoSalesOrderHead();
    	try {
    		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
    		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
    		String localCurrencyCode = "NTD";//buCommonPhraseService.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency");
    		
    		System.out.println(" bu common phrase line ");
    		
    		Sequence seq =  soDepartmentOrderHeadDAO.findSeqByName("soDepartmentOrderHead");
    		Long nextVal = seq.getCurrent_value() + seq.getIncrement();
    		seq.setCurrent_value(nextVal);
    		soDepartmentOrderHeadDAO.save(seq);
    		form.setHeadId(nextVal);
    		System.out.println(" Sequence ");
    		
    		SoReceiptHead receiptHead = soDepartmentOrderHeadDAO.findReceiptHeadByYearMonth("T1GS", "110", "05-06", "25");
    		SoReceiptLine receiptLine = soDepartmentOrderHeadDAO.findReceiptLineByh(receiptHead.getHeadId());
    		String invoicePrefix = receiptLine.getReceiptPrefix();
    		Long nextInv = Long.valueOf(receiptLine.getReceiptSerialNumberNextVal()) ;
    		Long nextNextInv = (nextInv+1);
    		
    		System.out.println("nextNextInv:"+nextNextInv);
    		
    		receiptLine.setReceiptSerialNumberNextVal(String.valueOf(nextNextInv+1));
    		soDepartmentOrderHeadDAO.save(receiptLine);
    		form.setGuiCode(invoicePrefix+nextNextInv);
    		System.out.println(" receiptLine ");
    		
    		
    		form.setOrderNo(AjaxUtils.getTmpOrderNo());
    		form.setOrderTypeCode(orderTypeCode);
    		form.setBrandCode(loginBrandCode);
    		form.setSalesOrderDate(DateUtils.parseDate(DateUtils.format(DateUtils.getCurrentDate(), "yyyy-MM-dd")));
    		form.setCurrencyCode(localCurrencyCode);
    		form.setSuperintendentCode(loginEmployeeCode);
    		form.setScheduleShipDate(DateUtils.parseDate(DateUtils.format(DateUtils.getCurrentDate(), "yyyy-MM-dd")));
    		form.setExportCommissionRate(0D);
    		form.setExportExchangeRate(1.0D);
    		form.setCurrencyCode("NTD");
    		form.setVerificationStatus("N");
    		form.setStatus(OrderStatus.SAVE);
    		form.setCreatedBy(loginEmployeeCode);
    		form.setCreationDate(DateUtils.parseDate(DateUtils.format(DateUtils.getCurrentDate(), "yyyy-MM-dd")));
    		form.setLastUpdatedBy(loginEmployeeCode);
    		form.setLastUpdateDate(DateUtils.parseDate(DateUtils.format(DateUtils.getCurrentDate(), "yyyy-MM-dd")));
    		if(!"SOP".equals(orderTypeCode)||!("T2".equals(form.getBrandCode())))
    		{
    			form.setSchedule("99");
    		}
    		if ("SOF".equals(form.getOrderTypeCode())) {
    			form.setDiscountRate(0D);
    		}else if ("T2".equals(form.getBrandCode()) && "SOE".equals(form.getOrderTypeCode())){
    			form.setDiscountRate(70D);
    			form.setInvoiceTypeCode("2");
    		} else {
    			form.setDiscountRate(100D);
    		}

    		if ("T3CO".equals(loginBrandCode) || "T3CU".equals(loginBrandCode)) {
    			SoSalesOrderPayment payment1 = new SoSalesOrderPayment();
    			payment1.setPosPaymentType("5");
    			payment1.setForeignCurrencyCode("NTD");
    			payment1.setExchangeRate(1D);
    			payment1.setForeignAmount(0D);
    			payment1.setLocalAmount(0D);
    			payment1.setPosPaymentType("NT");
    			SoSalesOrderPayment payment2 = new SoSalesOrderPayment();
    			payment2.setPosPaymentType("6");
    			payment2.setForeignCurrencyCode("NTD");
    			payment2.setExchangeRate(1D);
    			payment2.setForeignAmount(0D);
    			payment2.setLocalAmount(0D);
    			payment2.setPosPaymentType("NT");
    			List soSalesOrderPayments = new ArrayList();
    			soSalesOrderPayments.add(payment1);
    			soSalesOrderPayments.add(payment2);
    			form.setSoSalesOrderPayments(soSalesOrderPayments);
    		}

    		BuOrderTypeId buOrderTypeId = new BuOrderTypeId(form.getBrandCode(), form.getOrderTypeCode());
    		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    		if (buOrderType == null)
    			throw new Exception("查無單別" + form.getOrderTypeCode() + "之資訊，請連絡系統管理人員");
    		
    		if ("F".equals(buOrderType.getTaxCode())) {
    			form.setTaxType("1");
    			form.setTaxRate(0D);
    		} else {
    			form.setTaxType("3");
    			form.setTaxRate(5D);
    		}

//    		List<BuShop> shopForEmployee = buBasicDataService.getShopForEmployee(form.getBrandCode(), form.getCreatedBy(), "Y");
//    		if (shopForEmployee == null || shopForEmployee.size() == 0)
//    			throw new Exception("查無使用者可使用之店櫃");
//    		
//    		String defaultShop = "";
//    		if ("T2".equals(form.getBrandCode()) && "SOE".equals(form.getOrderTypeCode())){
//    			defaultShop = "P0700";
//    		}else{
//    			BuShop firstShop = (BuShop) shopForEmployee.get(0);
//    			defaultShop = firstShop.getShopCode();
//    		}
    		
    		String defaultShop = "F0000";
			form.setShopCode(defaultShop );
    		
//    		List<ImWarehouse> allAvailableWarehouse = imWarehouseService.getWarehouseByWarehouseEmployee(form.getBrandCode(), form.getCreatedBy(), null);
//    		if (null == allAvailableWarehouse || allAvailableWarehouse.size() == 0)
//    				throw new Exception("查無使用者可使用之庫別");
    		
    		String defaultWarehouseCode = "";
    		
    		if ("T2".equals(form.getBrandCode()) && "SOE".equals(form.getOrderTypeCode())){
    			defaultWarehouseCode = "P0700";
    		}else{
//    			ImWarehouse defaultWarehouse = (ImWarehouse) allAvailableWarehouse.get(0);
    			String defaultWarehouseName = "";
    			defaultWarehouseCode = defaultWarehouseName;
    		}
    		form.setDefaultWarehouseCode("F0000");
    		
    		//invoiceNo 12371287
    		form.setReserve1("12371287");
    		form.setPosMachineCode("99");
    		form.setCustomerPoNo(invoicePrefix+nextNextInv);//先用發票號碼
    		this.save(form);
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error("建立銷售單失敗,原因:" + e.toString());
    		//throw new Exception("建立銷售單失敗,原因:" + e.getMessage());
    	}
    	return form;
    }

    /**
     * 取得頁面上面的值客戶資料等名稱
     * 
     * @param
     * @return
     * @throws Exception
     */
    public void getParameterValues(SoSalesOrderHead head, Map resultMap) throws Exception {
    	if (StringUtils.hasText(head.getCustomerCode())) {
    		resultMap.put("customerCode_var", head.getCustomerCode());
    		BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewService
    		.findCustomerByType(head.getBrandCode(), head.getCustomerCode(), "customerCode", null);
    		if (buCustomerWithAddressView != null) {
    			head.setCustomerName(buCustomerWithAddressView.getShortName());
    			head.setCustomerType(buCustomerWithAddressView.getCustomerTypeCode());
    			head.setVipTypeCode(buCustomerWithAddressView.getVipTypeCode());
    			if (head.getOrderTypeCode() != null && !"SOP".equals(head.getOrderTypeCode())) {
    				head.setVipPromotionCode(buCustomerWithAddressView.getPromotionCode());
    			} else {
    				head.setVipPromotionCode(null);
    			}
    			resultMap.put("vipTypeCode", buCustomerWithAddressView.getVipTypeCode());
    		} else {
    			head.setCustomerName("查無此客戶資料");
    		}
    	} else {
    		resultMap.put("customerCode_var", "");
    	}
    	if (StringUtils.hasText(head.getDefaultWarehouseCode())) {
    		ImWarehouse imWarehouse = imWarehouseService.findById(head
    				.getDefaultWarehouseCode());
    		if (imWarehouse != null) {
    			String warehouseManager = imWarehouse.getWarehouseManager();
    			if (!StringUtils.hasText(warehouseManager)) {
    				throw new Exception("銷貨單號：" + head.getOrderNo() + "其主檔庫別-"
    						+ head.getDefaultWarehouseCode()
    						+ "的倉管人員為空值，請聯絡系統管理人員處理！");
    			}
    		}
    	}
    	BuOrderTypeId buOrderTypeId = new BuOrderTypeId(head.getBrandCode(), head.getOrderTypeCode());
    	BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    	resultMap.put("priceType", buOrderType.getPriceType());
    	resultMap.put("orderCondition", buOrderType.getOrderCondition());
    }

    /**
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findInitialCommon(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {

	    Long headId = NumberUtils
		    .getLong(httpRequest.getProperty("headId"));
	    SoSalesOrderHead head = findById(headId);
	    String status = head.getStatus();
	    List<BuShop> shopForEmployee = new ArrayList();
	    List<ImWarehouse> allAvailableWarehouse = new ArrayList();
	    BuShop firstShop = null;

	    // 如果是POS全秀
	    if ("SOP".equals(head.getOrderTypeCode())) {
		shopForEmployee = buBasicDataService.findShopByBrandAndEnable(
			head.getBrandCode(), "Y");
		allAvailableWarehouse = imWarehouseService.findByBrandCode(head
			.getBrandCode(), "Y");
		// 如果是可編輯狀態的時候撈出該使用者的下拉
	    } else if (OrderStatus.SAVE.equals(status)
		    || OrderStatus.REJECT.equals(status)
		    || OrderStatus.UNCONFIRMED.equals(status)) {
		shopForEmployee = buBasicDataService.getShopForEmployee(head
			.getBrandCode(), head.getCreatedBy(), "Y");
		allAvailableWarehouse = imWarehouseService
			.getWarehouseByWarehouseEmployee(head.getBrandCode(),
				head.getCreatedBy(), null);
		// 如果是不可編輯直接撈取資料
	    } else {
		BuShop shop = buShopService.findById(head.getShopCode());
		shopForEmployee.add(shop);
		ImWarehouse warehouse = imWarehouseService.findById(head
			.getDefaultWarehouseCode());
		allAvailableWarehouse.add(warehouse);
	    }

	    if (null == shopForEmployee || shopForEmployee.size() == 0)
		throw new Exception("查無使用者可使用之店櫃");
	    if (null == allAvailableWarehouse
		    || allAvailableWarehouse.size() == 0)
		throw new Exception("查無使用者可使用之庫別");

	    firstShop = (BuShop) shopForEmployee.get(0);
	    shopForEmployee = AjaxUtils.produceSelectorData(shopForEmployee,
		    "shopCode", "shopCName", true, false);
	    properties.setProperty("WarehouseManager",
		    ((ImWarehouse) allAvailableWarehouse.get(0))
			    .getWarehouseManager());
	    allAvailableWarehouse = AjaxUtils.produceSelectorData(
		    allAvailableWarehouse, "warehouseCode", "warehouseName",
		    true, false);
	    
	    List allshopMachine;
	    if("SOP".equals(head.getOrderTypeCode()))
	    allshopMachine= buShopMachineService.findByShopCode(head.getShopCode());
	    else
	    allshopMachine= buShopMachineService.findByShopCode(firstShop
		    .getShopCode());

	    allshopMachine = AjaxUtils.produceSelectorData(allshopMachine,
		    "posMachineCode", "posMachineCode", false, true);
	    List allCountry = buBasicDataService.findCountryByEnable(null);
	    allCountry = AjaxUtils.produceSelectorData(allCountry,
		    "countryCode", "countryCName", false, true);
	    List allCurrency = buBasicDataService.findCurrencyByEnable(null);
	    allCurrency = AjaxUtils.produceSelectorData(allCurrency,
		    "currencyCode", "currencyCName", false, false);
	    List allDeliveryType = buCommonPhraseService
		    .getCommonPhraseLinesById("DeliveryType", false);
	    allDeliveryType = AjaxUtils.produceSelectorData(allDeliveryType,
		    "lineCode", "name", false, true);
	    List allPaymentTerm = buBasicDataService
		    .findPaymentTermByOrganizationAndEnable("TM", null);
	    allPaymentTerm = AjaxUtils.produceSelectorData(allPaymentTerm,
		    "paymentTermCode", "name", false, true);
	    List allInvoiceType = buCommonPhraseService
		    .getCommonPhraseLinesById("InvoiceType", false);
	    allInvoiceType = AjaxUtils.produceSelectorData(allInvoiceType,
		    "lineCode", "name", false, true);
	    List allTaxType = buCommonPhraseService.getCommonPhraseLinesById(
		    "TaxType", false);
	    allTaxType = AjaxUtils.produceSelectorData(allTaxType, "lineCode",
		    "name", false, false);
	    List allDiscountType = buCommonPhraseService
		    .getCommonPhraseLinesById("OrderDiscountType", false);
	    allDiscountType = AjaxUtils.produceSelectorData(allDiscountType,
		    "lineCode", "name", false, true);
	    List allPaymentCategory = buCommonPhraseService
		    .getCommonPhraseLinesById("PaymentCategory", false);
	    allPaymentCategory = AjaxUtils.produceSelectorData(
		    allPaymentCategory, "lineCode", "name", false, true);
	    List allItemCategory = imItemCategoryService.findByCategoryType(
		    head.getBrandCode(), "ITEM_CATEGORY");
	    allItemCategory = AjaxUtils.produceSelectorData(allItemCategory,
		    "categoryCode", "categoryName", false, false);
	    List allPaymentType = buCommonPhraseService
		    .getCommonPhraseLinesById("PaymentType", false);
	    allPaymentType = AjaxUtils.produceSelectorData(allPaymentType,
		    "lineCode", "name", false, true);
	    properties.setProperty("shopForEmployee", AjaxUtils
		    .parseSelectorData(shopForEmployee));
	    properties.setProperty("allAvailableWarehouse", AjaxUtils
		    .parseSelectorData(allAvailableWarehouse));
	    properties.setProperty("allshopMachine", AjaxUtils
		    .parseSelectorData(allshopMachine));
	    properties.setProperty("allCountry", AjaxUtils
		    .parseSelectorData(allCountry));
	    properties.setProperty("allCurrency", AjaxUtils
		    .parseSelectorData(allCurrency));
	    properties.setProperty("allDeliveryType", AjaxUtils
		    .parseSelectorData(allDeliveryType));
	    properties.setProperty("allPaymentTerm", AjaxUtils
		    .parseSelectorData(allPaymentTerm));
	    properties.setProperty("allInvoiceType", AjaxUtils
		    .parseSelectorData(allInvoiceType));
	    properties.setProperty("allTaxType", AjaxUtils
		    .parseSelectorData(allTaxType));
	    properties.setProperty("allDiscountType", AjaxUtils
		    .parseSelectorData(allDiscountType));
	    properties.setProperty("allPaymentCategory", AjaxUtils
		    .parseSelectorData(allPaymentCategory));
	    properties.setProperty("allItemCategory", AjaxUtils
		    .parseSelectorData(allItemCategory));
	    properties.setProperty("allPaymentType", AjaxUtils
		    .parseSelectorData(allPaymentType));
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("初始Master發生錯誤，原因：" + ex.toString());
	    throw new Exception("初始Master發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據currencyCode以及日期為查詢條件，取得匯率
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getExchangeRate(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	Double exportExchangeRate = 1D;
	String currencyCode = "";
	Date currencyDate = null;
	BuExchangeRate buExchangeRate;
	try {
	    currencyCode = httpRequest.getProperty("currencyCode");
	    currencyDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest
		    .getProperty("currencyDate"));
	    buExchangeRate = buExchangeRateDAO.getLastExchangeRate("TM",
		    currencyCode, "NTD", currencyDate);
	    if (buExchangeRate != null)
		exportExchangeRate = buExchangeRate.getExchangeRate();
	    properties.setProperty("ExportExchangeRate", String
		    .valueOf(exportExchangeRate));
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("查詢匯率代號 ：" + currencyCode + "時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢匯率代號 ：" + currencyCode + "時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 處理AJAX參數(查詢專櫃POS機號、預設庫別及庫別的倉管人員)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getShopMachineForAJAX(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String shopCode = "";
	String defaultWarehouseCode = "";
	String warehouseManager = "";
	try {
	    shopCode = httpRequest.getProperty("shopCode").trim().toUpperCase();
	    defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode").trim().toUpperCase();
	    //BuShop shopPO = buShopService.findById(shopCode);
	    //if (shopPO != null) {
		//String salesWarehouseCode = shopPO.getSalesWarehouseCode();
    	if (StringUtils.hasText(defaultWarehouseCode)) {
    		ImWarehouse warehousePO = (ImWarehouse) imWarehouseService.findById(defaultWarehouseCode);
    		if (warehousePO != null) {
    			defaultWarehouseCode = AjaxUtils.getPropertiesValue(defaultWarehouseCode, "");
    			warehouseManager = AjaxUtils.getPropertiesValue(warehousePO.getWarehouseManager(), "");
    		}
    	}
	    //}
	    List allshopMachine = buShopMachineService.findByShopCode(shopCode);
	    allshopMachine = AjaxUtils.produceSelectorData(allshopMachine, "posMachineCode", "posMachineCode", false, true);
	    properties.setProperty("allshopMachine", AjaxUtils.parseSelectorData(allshopMachine));
	    properties.setProperty("DefaultWarehouseCode", defaultWarehouseCode);
	    properties.setProperty("WarehouseManager", warehouseManager);
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("查詢專櫃代號 ：" + shopCode + "的POS機號時發生錯誤，原因："
		    + ex.getMessage());
	    throw new Exception("查詢POS機號失敗！");
	}
    }

    /**
     * 依據currencyCode為查詢條件，取得匯率
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getPostPayment(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String posPaymentType = "";
	BuCommonPhraseLine buCommonPhraseLine;
	try {
	    posPaymentType = httpRequest.getProperty("posPaymentType");
	    buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine(
		    "PaymentType", posPaymentType);
	    properties.setProperty("PaymentType", buCommonPhraseLine
		    .getAttribute1());
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("查詢付款幣別 ：" + posPaymentType + "時發生錯誤，原因："
		    + ex.getMessage());
	    throw new Exception("查詢付款幣別 ：" + posPaymentType + "時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 取得memo
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXMemo(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	StringBuffer memo = new StringBuffer();
	StringBuffer errorMemo = new StringBuffer();
	try {
	    String brandCode = httpRequest.getProperty("brandCode");
	    String salesOrderDate = httpRequest.getProperty("salesOrderDate");
	    String posMachineCode = httpRequest.getProperty("posMachineCode");
	    String customerPoNoStart = httpRequest
		    .getProperty("customerPoNoStart");
	    String customerPoNoEnd = httpRequest.getProperty("customerPoNoEnd");

	    int split = getPrefixIndex(customerPoNoStart);
	    
	    // 使用者輸入共有幾筆,銷售單資料庫共幾筆, 出貨單資料庫共幾筆
	    Long number1 = compareNumber(customerPoNoStart, customerPoNoEnd, split);
	    memo.append("資料區間共").append(number1).append("筆，銷售售貨單資料庫共");

	    Map findMap = new HashMap();
	    findMap.put("brandCode", brandCode);
	    findMap.put("salesOrderDate", salesOrderDate);
	    findMap.put("posMachineCode", posMachineCode);
	    findMap.put("customerPoNoStart", customerPoNoStart);
	    findMap.put("customerPoNoEnd", customerPoNoEnd);

	    List<SoSalesOrderHead> soSalesOrderHeads = soSalesOrderHeadDAO
		    .findCustomerPoNo(findMap);
	    int sOsize = soSalesOrderHeads.size();
	    memo.append(sOsize).append("筆，出貨單資料庫共");

	    int imDeliveryHeadSize = 0;
	    for (SoSalesOrderHead soSalesOrderHead : soSalesOrderHeads) {
		ImDeliveryHead imDeliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
			.findFirstByProperty("ImDeliveryHead",
				"and salesOrderId = ?",
				new Object[] { soSalesOrderHead.getHeadId() });
		if (imDeliveryHead != null) {
		    imDeliveryHeadSize = imDeliveryHeadSize + 1;
		}
	    }
	    memo.append(imDeliveryHeadSize).append("筆");

	    // 檢核銷貨與出貨的數量一致
	    if (sOsize == 0) {
		errorMemo.append("查無銷貨單售貨單資料");
	    } else if (number1 < sOsize) {
		errorMemo.append("查輸入的資料區間:" + number1 + "小於更新銷貨單資料:" + sOsize);
	    } else if (number1 < imDeliveryHeadSize) {
		errorMemo.append("查輸入的資料區間:" + number1 + "小於更新出貨單資料:" + sOsize);
	    }

	    properties.setProperty("memo", memo.toString());
	    properties.setProperty("errorMemo", errorMemo.toString());

	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("增減數字時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("增減數字時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 增減數字
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getAJAXCustomerPoNo(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    String customerPoNoStart = httpRequest
		    .getProperty("customerPoNoStart");
	    String customerPoNoEnd = httpRequest.getProperty("customerPoNoEnd");
	    Long accessNumber = NumberUtils.getLong(httpRequest
		    .getProperty("accessNumber"));
	    int maxPoNoLength = NumberUtils.getLong(httpRequest.getProperty("maxPoNoLength")).intValue();
	    
	    
	    // 區間一起加數字轉換
	    int split = getPrefixIndex(customerPoNoStart); //2
	    String prefix = customerPoNoStart.substring(0, split);
	    Long start = NumberUtils.getLong(customerPoNoStart.substring(split,
		    customerPoNoStart.length()));
	    Long end = NumberUtils.getLong(customerPoNoEnd.substring(split,
		    customerPoNoEnd.length()));
	    Long newStart = start + accessNumber;
	    Long newEnd = end + accessNumber;
	    
	    // 取得數字格式補0 例如: 00000000
	    DecimalFormat df = doDecimalFormat(maxPoNoLength - split);

	    properties.setProperty("newCustomerPoNoStart", prefix
		    + df.format(newStart));
	    properties.setProperty("newCustomerPoNoEnd", prefix
		    + df.format(newEnd));

	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("增減數字時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("增減數字時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依傳入的整數產生formatLength個長度的字串0
     * @param formatLength
     * @return
     */
    private DecimalFormat doDecimalFormat(int formatLength){
	StringBuffer formatStr = new StringBuffer();
	for (int i = 0; i < formatLength; i++) {
	    formatStr.append("0");
	}
	return new DecimalFormat(formatStr.toString());
    }
    
    /**
     * 換字軌
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getAJAXPrefixCustomerPoNo(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	DecimalFormat df = null;
	int zeroNumber = 0;
	try {
	    String newCustomerPoNoStart = httpRequest
		    .getProperty("newCustomerPoNoStart");
	    String newCustomerPoNoEnd = httpRequest
		    .getProperty("newCustomerPoNoEnd");
	    String prefixCustomerPoNo = httpRequest
		    .getProperty("prefixCustomerPoNo");
	    String customerPoNoStart = httpRequest
		    .getProperty("customerPoNoStart");
	    String customerPoNoEnd = httpRequest.getProperty("customerPoNoEnd");
	    String accessNumber = httpRequest.getProperty("accessNumber");
	    
	    int maxPoNoLength = NumberUtils.getLong(httpRequest.getProperty("maxPoNoLength")).intValue();
	    int split = getPrefixIndex(customerPoNoStart); //2
	    zeroNumber = maxPoNoLength - split;
	    
	    df = doDecimalFormat(zeroNumber);
	    
	    if (StringUtils.hasText(prefixCustomerPoNo)) { //有設定前字軌
		df = doDecimalFormat(maxPoNoLength-prefixCustomerPoNo.length());
		
		if (StringUtils.hasText(newCustomerPoNoStart)
			&& StringUtils.hasText(newCustomerPoNoEnd)) {
		    properties.setProperty("newCustomerPoNoStart",		// 新字軌+原本數字格式化後用
			    prefixCustomerPoNo
				    + df.format(NumberUtils.getLong(newCustomerPoNoStart.substring(split))));
		    properties.setProperty("newCustomerPoNoEnd",
			    prefixCustomerPoNo
				    + df.format(NumberUtils.getLong(newCustomerPoNoEnd.substring(split))));
		} else {
		    properties.setProperty("newCustomerPoNoStart",
			    prefixCustomerPoNo
				    + df.format(NumberUtils.getLong(customerPoNoStart.substring(split))));
		    properties.setProperty("newCustomerPoNoEnd",
			    prefixCustomerPoNo
				    + df.format(NumberUtils.getLong(customerPoNoEnd.substring(split))));
		}
	    } else {	// 無字軌
		if(StringUtils.hasText(accessNumber)){
		    properties.setProperty("newCustomerPoNoStart",
			    customerPoNoStart.substring(0, split)
			    + df.format(NumberUtils.getLong(newCustomerPoNoStart.substring(split))));
		    properties.setProperty("newCustomerPoNoEnd", customerPoNoEnd
			    .substring(0, split)
			    + df.format(NumberUtils.getLong(newCustomerPoNoEnd.substring(split))));
		}else{
		    properties.setProperty("newCustomerPoNoStart",
			    customerPoNoStart.substring(0, split)
			    + df.format(NumberUtils.getLong(customerPoNoStart.substring(split))));
		    properties.setProperty("newCustomerPoNoEnd", customerPoNoEnd
			    .substring(0, split)
			    + df.format(NumberUtils.getLong(customerPoNoEnd.substring(split))));
		}
	    }

	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("換字軌時發生錯誤，原因：" + ex.getMessage());
	    throw new Exception("換字軌時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 從後面數來找到不等於數字的索引
     * @param doNumber
     * @return
     */
    private int getPrefixIndex(String customerPoNo){
	int split = 0;
	for (int i = customerPoNo.length()-1 ; i > 0; i--) {
	    try {
		Double.valueOf(customerPoNo.substring(i-1, i));
		
	    } catch (NumberFormatException e) {
		split = i;
		break;
	    }
	}
	log.info("分割  = " + split);	
	return split;
    }
    
    /**
     * 依據currencyCode以及日期為查詢條件，取得匯率
     * 
     * @param currencyCode
     * @return ExchangeRate
     * @throws Exception
     */
    public List<Properties> getItemCategory(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    Long headId = NumberUtils
		    .getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");
	    String itemCode = httpRequest.getProperty("itemCode");
	    ImItem item = null;
	    if (StringUtils.hasText(itemCode)) {
		item = imItemService.findItem(brandCode, itemCode);
	    } else {
		SoSalesOrderHead head = findById(headId);
		if (head != null && head.getSoSalesOrderItems() != null
			&& head.getSoSalesOrderItems().size() > 0) {
		    SoSalesOrderItem soSalesOrderItem = head
			    .getSoSalesOrderItems().get(0);
		    item = imItemService.findItem(brandCode, soSalesOrderItem
			    .getItemCode());
		}
	    }
	    if (item != null) {
		properties.setProperty("ItemCategory", item.getItemCategory());
		// log.info("item.getItemCategory() = " +
		// item.getItemCategory());
	    }
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("查詢商品業種時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢商品業種時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {
    	try {
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
    		// ======================帶入Head的值=========================
    		String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
    		String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號
    		String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
    		String status = httpRequest.getProperty("status");// 狀態
    		String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");// 預設倉別
    		String taxType = httpRequest.getProperty("taxType");// 稅別
    		String taxRate = httpRequest.getProperty("taxRate");// 稅率
    		String discountRate = httpRequest.getProperty("discountRate");// 折扣比率
    		String vipPromotionCode = httpRequest.getProperty("vipPromotionCode");// vip類別代號
    		String promotionCode = httpRequest.getProperty("promotionCode");// 活動代號
    		String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
    		String warehouseManager = httpRequest.getProperty("warehouseManager");
    		String customerType = httpRequest.getProperty("customerType");
    		String vipType = httpRequest.getProperty("vipType");
    		String priceType = httpRequest.getProperty("priceType");
    		String salesDate = httpRequest.getProperty("salesOrderDate");
    		salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
    		String exportExchangeRate = httpRequest.getProperty("exportExchangeRate");
    		Double exchangeRate = NumberUtils.getDouble(exportExchangeRate) == 0D ? 1D : NumberUtils.getDouble(exportExchangeRate);
    		Double bonusPointAmount = NumberUtils.getDouble("bonusPointAmount");
    		HashMap map = new HashMap();
    		map.put("brandCode", brandCode);
    		map.put("orderTypeCode", orderTypeCode);
    		map.put("shopCode", shopCode);
    		map.put("warehouseCode", defaultWarehouseCode);
    		map.put("taxType", taxType);
    		map.put("taxRate", taxRate);
    		map.put("discountRate", discountRate);
    		map.put("vipPromotionCode", vipPromotionCode);
    		map.put("promotionCode", promotionCode);
    		map.put("warehouseEmployee", warehouseEmployee);
    		map.put("warehouseManager", warehouseManager);
    		map.put("customerType", customerType);
    		map.put("vipType", vipType);
    		map.put("priceType", priceType);
    		map.put("salesDate", salesDate);
    		map.put("exchangeRate", exchangeRate);
    		map.put("bonusPointAmount", bonusPointAmount);
    		getDefaultParameter(map);
    		// ==============================================================
    		List<SoSalesOrderItem> soSalesOrderItems = soSalesOrderItemDAO.findPageLine(headId, iSPage, iPSize);
    		if (soSalesOrderItems != null && soSalesOrderItems.size() > 0) {
    			// 取得第一筆的INDEX
    			Long firstIndex = soSalesOrderItems.get(0).getIndexNo();
    			// 取得最後一筆 INDEX
    			Long maxIndex = soSalesOrderItemDAO.findPageLineMaxIndex(headId);
    			if (!"SOP".equals(orderTypeCode) && 
    					(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status) || OrderStatus.UNCONFIRMED.equals(status))) {
    				// 可編輯狀態
    				refreshSoItemDataForModify(map, soSalesOrderItems);
    			} else {
    				// 不可編輯狀態
    				refreshSoItemDataForReadOnly(map, soSalesOrderItems);
    			}
    			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, soSalesOrderItems, gridDatas, firstIndex, maxIndex));
    		} else {
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map, gridDatas));
    		}
    		return result;
    	} catch (Exception ex) {
    		log.error("載入頁面顯示的銷貨明細發生錯誤，原因：" + ex.toString());
    		throw new Exception("載入頁面顯示的銷貨明細失敗！");
    	}
    }

    /**
     * item帶入head的預設值
     * 
     * @param parameterMap
     */
    private void getDefaultParameter(HashMap parameterMap) throws Exception {
	String discountRate = (String) parameterMap.get("discountRate");
	// 折扣比率預設值
	try {
	    Double.parseDouble(discountRate);
	} catch (NumberFormatException nfe) {
	    parameterMap.put("discountRate", "100.0");
	}/*
	     * //預設庫別名稱 if(StringUtils.hasText(warehouseCode)){ ImWarehouse
	     * warehousePO =
	     * imWarehouseService.findByBrandCodeAndWarehouseCode(brandCode,
	     * warehouseCode, null); if(warehousePO != null){
	     * parameterMap.put("warehouseName",
	     * warehousePO.getWarehouseName()); }else{
	     * parameterMap.put("warehouseName", "查無資料"); } }
	     */
    }

    /**
     * 更新SoItem相關資料(狀態為可編輯時)
     * 
     * @param parameterMap
     * @param salesOrderItems
     */
    private void refreshSoItemDataForModify(HashMap parameterMap, List<SoSalesOrderItem> salesOrderItems) throws Exception {
    	String brandCode = (String) parameterMap.get("brandCode");
    	String orderTypeCode = (String) parameterMap.get("orderTypeCode");
    	String shopCode = (String) parameterMap.get("shopCode");
    	String warehouseEmployee = (String) parameterMap.get("warehouseEmployee");
    	String warehouseManager = (String) parameterMap.get("warehouseManager");
    	String priceType = (String) parameterMap.get("priceType");
    	String customerType = (String) parameterMap.get("customerType");
    	String vipType = (String) parameterMap.get("vipType");
    	String salesDate = (String) parameterMap.get("salesDate");
    	String defaultVipPromotionCode = (String) parameterMap.get("vipPromotionCode");
    	String warehouseCode = (String) parameterMap.get("warehouseCode");
    	Double exchangeRate = (Double) parameterMap.get("exchangeRate");

    	BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    	
    	for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
    		String itemCode = salesOrderItem.getItemCode();
    		Double quantity = salesOrderItem.getQuantity();
    		Double discountRate = salesOrderItem.getDiscountRate();
    		if (discountRate == null) {
    			discountRate = 100D;
    			salesOrderItem.setDiscountRate(discountRate);
    		}

    		Double deductionAmount = salesOrderItem.getDeductionAmount();
    		String promotionCode = salesOrderItem.getPromotionCode();
    		// 依據customer的VipPromotionCode帶入line中
    		salesOrderItem.setVipPromotionCode(defaultVipPromotionCode);
    		Double originalUnitPrice = NumberUtils.getDouble(salesOrderItem.getOriginalUnitPrice());
    		String taxType = salesOrderItem.getTaxType();
    		Double taxRate = salesOrderItem.getTaxRate();
    		HashMap conditionMap = new HashMap();
    		conditionMap.put("brandCode", brandCode);
    		conditionMap.put("orderTypeCode", orderTypeCode);
    		conditionMap.put("shopCode", shopCode);
    		conditionMap.put("warehouseEmployee", warehouseEmployee);
    		conditionMap.put("warehouseManager", warehouseManager);
    		conditionMap.put("priceType", priceType);
    		conditionMap.put("customerType", customerType);
    		conditionMap.put("vipType", vipType);
    		conditionMap.put("salesDate", salesDate);
    		conditionMap.put("itemCode", itemCode);
    		conditionMap.put("warehouseCode", warehouseCode);
    		conditionMap.put("quantity", quantity);
    		conditionMap.put("discountRate", discountRate);
    		conditionMap.put("deductionAmount", deductionAmount);
    		conditionMap.put("promotionCode", promotionCode);
    		conditionMap.put("vipPromotionCode", defaultVipPromotionCode);
    		conditionMap.put("originalUnitPrice", originalUnitPrice);
    		conditionMap.put("taxType", taxType);
    		conditionMap.put("taxRate", taxRate);
    		// 查詢後的銷售明細
    		SoSalesOrderItem soItemInfo = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, setSoItemBean(conditionMap));
    		// copy查詢後的銷售明細
    		this.setSoItemInfo(salesOrderItem, soItemInfo);
    		this.refreshItemPriceExchangeData(salesOrderItem, exchangeRate);
    		this.refreshItemPriceRelationData(buOrderType, salesOrderItem, exchangeRate);
    		ImItem item = imItemService.findItem(brandCode, itemCode);
    		if (item != null) {
    			salesOrderItem.setIsTax(item.getIsTax());
    			salesOrderItem.setAllowMinusStock(item.getAllowMinusStock());
    		}
    	}
    }

    /**
     * 更新SoItem相關資料(狀態為不可編輯時)
     * 
     * @param parameterMap
     * @param salesOrderItems
     */
    private void refreshSoItemDataForReadOnly(HashMap parameterMap, List<SoSalesOrderItem> salesOrderItems) throws Exception {
    	String brandCode = (String) parameterMap.get("brandCode");
    	for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
    		String itemCode = salesOrderItem.getItemCode();
    		// 品名
    		ImItem itemPO = imItemService.findItem(brandCode, itemCode);
    		if (itemPO != null) {
    			salesOrderItem.setItemCName(itemPO.getItemCName());
    		}
    	}
    }

    private SoSalesOrderItem setSoItemBean(HashMap conditionMap) {
	String itemCode = (String) conditionMap.get("itemCode");
	String warehouseCode = (String) conditionMap.get("warehouseCode");
	Double quantity = (Double) conditionMap.get("quantity");
	Double discountRate = (Double) conditionMap.get("discountRate");
	Double deductionAmount = (Double) conditionMap.get("deductionAmount");
	String promotionCode = (String) conditionMap.get("promotionCode");
	String vipPromotionCode = (String) conditionMap.get("vipPromotionCode");
	Double originalUnitPrice = (Double) conditionMap.get("originalUnitPrice");
	String taxType = (String) conditionMap.get("taxType");
	Double taxRate = (Double) conditionMap.get("taxRate");

	SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
	if (StringUtils.hasText(itemCode)) {
	    itemCode = itemCode.trim().toUpperCase();
	}
	if (StringUtils.hasText(warehouseCode)) {
	    warehouseCode = warehouseCode.trim().toUpperCase();
	}
	if (StringUtils.hasText(promotionCode)) {
	    promotionCode = promotionCode.trim().toUpperCase();
	}
	salesOrderItem.setItemCode(itemCode);
	salesOrderItem.setWarehouseCode(warehouseCode);
	salesOrderItem.setQuantity(quantity);
	salesOrderItem.setDiscountRate(discountRate);
	salesOrderItem.setDeductionAmount(deductionAmount);
	salesOrderItem.setPromotionCode(promotionCode);
	salesOrderItem.setVipPromotionCode(vipPromotionCode);
	salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
	salesOrderItem.setTaxType(taxType);
	salesOrderItem.setTaxRate(taxRate);
	return salesOrderItem;
    }

    private void setSoItemInfo(SoSalesOrderItem origiSalesOrderItem,
	    SoSalesOrderItem salesOrderItem) {
	origiSalesOrderItem.setItemCName(salesOrderItem.getItemCName());
	origiSalesOrderItem.setCurrentOnHandQty(salesOrderItem
		.getCurrentOnHandQty());
	origiSalesOrderItem.setOriginalUnitPrice(salesOrderItem
		.getOriginalUnitPrice());
	origiSalesOrderItem.setVipPromotionName(salesOrderItem
		.getVipPromotionName());
	origiSalesOrderItem.setVipDiscount(salesOrderItem.getVipDiscount());
	origiSalesOrderItem.setVipDiscountType(salesOrderItem
		.getVipDiscountType());
	origiSalesOrderItem.setFoundVipPromotion(salesOrderItem
		.getFoundVipPromotion());
	origiSalesOrderItem.setPromotionName(salesOrderItem.getPromotionName());
	origiSalesOrderItem.setDiscount(salesOrderItem.getDiscount());
	origiSalesOrderItem.setDiscountType(salesOrderItem.getDiscountType());
	origiSalesOrderItem.setFoundPromotion(salesOrderItem
		.getFoundPromotion());
	origiSalesOrderItem.setIsServiceItem(salesOrderItem.getIsServiceItem());
	origiSalesOrderItem.setIsComposeItem(salesOrderItem.getIsComposeItem());
	origiSalesOrderItem.setIsTax(salesOrderItem.getIsTax());
	origiSalesOrderItem.setTaxType(salesOrderItem.getTaxType());
	origiSalesOrderItem.setTaxRate(salesOrderItem.getTaxRate());
    }

    /**
     * 將Head的VipPromotion、折扣率、稅別、稅額、幣別、預設庫別替換line的相關欄位
     * 
     * @param httpRequest
     * @return
     * @throws ValidationErrorException
     */
    public List<Properties> updateItemRelationData(Properties httpRequest)
	    throws ValidationErrorException {

	try {
	    String formStatus = httpRequest.getProperty("status");// 狀態
	    if (OrderStatus.SAVE.equals(formStatus)
		    || OrderStatus.REJECT.equals(formStatus)
		    || OrderStatus.UNCONFIRMED.equals(formStatus)) {
		Long headId = NumberUtils.getLong(httpRequest
			.getProperty("headId"));// 要顯示的HEAD_ID
		SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(headId);
		if (salesOrderHeadPO == null) {
		    throw new ValidationErrorException("查無銷貨單主鍵：" + headId
			    + "的資料！");
		}
		// ======================帶入Head的值=========================
		String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
		String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
		String defaultWarehouseCode = httpRequest
			.getProperty("defaultWarehouseCode");// 預設倉別
		String taxType = httpRequest.getProperty("taxType");// 稅別
		String taxRate = httpRequest.getProperty("taxRate");// 稅率
		String discountRate = httpRequest.getProperty("discountRate");// 折扣比率
		String vipPromotionCode = httpRequest
			.getProperty("vipPromotionCode");// vip類別代號
		String promotionCode = httpRequest.getProperty("promotionCode");// 活動代號
		String warehouseEmployee = httpRequest
			.getProperty("warehouseEmployee");
		String warehouseManager = httpRequest
			.getProperty("warehouseManager");
		String customerType = httpRequest.getProperty("customerType");
		String vipType = httpRequest.getProperty("vipType");
		String priceType = httpRequest.getProperty("priceType");
		String salesDate = httpRequest.getProperty("salesDate");
		salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd",
			salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		String exportExchangeRate = httpRequest
			.getProperty("exportExchangeRate");
		Double exchangeRate = NumberUtils.getDouble(exportExchangeRate) == 0D ? 1D
			: NumberUtils.getDouble(exportExchangeRate);
		HashMap map = new HashMap();
		map.put("brandCode", brandCode);
		map.put("orderTypeCode", orderTypeCode);
		map.put("shopCode", shopCode);
		map.put("warehouseCode", defaultWarehouseCode);
		map.put("taxType", taxType);
		map.put("taxRate", taxRate);
		map.put("discountRate", discountRate);
		map.put("vipPromotionCode", vipPromotionCode);
		map.put("promotionCode", promotionCode);
		map.put("warehouseEmployee", warehouseEmployee);
		map.put("warehouseManager", warehouseManager);
		map.put("customerType", customerType);
		map.put("vipType", vipType);
		map.put("priceType", priceType);
		map.put("salesDate", salesDate);
		map.put("exchangeRate", exchangeRate);
		getDefaultArgument(map);
		getItemPriceRelationData(salesOrderHeadPO, map, true);// 呼叫ERP.APP_GET_SALE_ITEM_INFO.GETDATA
		countTotalAmount(salesOrderHeadPO);
		soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    }
	    return AjaxUtils.getResponseMsg(null);
	} catch (Exception ex) {
	    log.error("更新銷貨明細相關欄位發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException("更新銷貨明細相關欄位失敗！");
	}
    }

    /**
     * item帶入head的預設值
     * 
     * @param parameterMap
     */
    private void getDefaultArgument(HashMap parameterMap) {
	String taxType = (String) parameterMap.get("taxType");
	String discountRate = (String) parameterMap.get("discountRate");
	String promotionCode = (String) parameterMap.get("promotionCode");
	// taxType、taxRate預設值
	if ("1".equals(taxType) || "2".equals(taxType)) {
	    parameterMap.put("taxRate", 0D);
	} else if ("3".equals(taxType)) {
	    parameterMap.put("taxRate", 5D);
	} else {
	    parameterMap.put("taxType", "3");
	    parameterMap.put("taxRate", 5D);
	}
	// 折扣比率預設值
	try {
	    parameterMap.put("discountRate", Double.parseDouble(discountRate));
	} catch (NumberFormatException nfe) {
	    parameterMap.put("discountRate", 100D);
	}
	// 促銷活動
	if (StringUtils.hasText(promotionCode)) {
	    promotionCode = promotionCode.trim().toUpperCase();
	    parameterMap.put("promotionCode", promotionCode);
	}
    }

    /**
     * 更新折扣後單價、重新計算Item的原銷售金額、實際售價、實際銷售金額、稅金
     * 
     * @param salesOrderItem
     */
    private void refreshItemPriceRelationData(BuOrderType buOrderType, SoSalesOrderItem salesOrderItem, Double exchangeRate) {
    	log.info("refreshItemPriceRelationData");
    	String isServiceItem = salesOrderItem.getIsServiceItem();
    	
    	if (!"Y".equals(isServiceItem)) {
    		// 計算折扣後單價
    		HashMap parameterMap = new HashMap();
    		parameterMap.put("originalUnitPrice", salesOrderItem.getOriginalUnitPrice());
    		parameterMap.put("promotionCode", salesOrderItem.getPromotionCode());
    		parameterMap.put("discountType", salesOrderItem.getDiscountType());
    		parameterMap.put("discount", salesOrderItem.getDiscount());
    		parameterMap.put("vipPromotionCode", salesOrderItem.getVipPromotionCode());
    		parameterMap.put("vipDiscountType", salesOrderItem.getVipDiscountType());
    		parameterMap.put("vipDiscount", salesOrderItem.getVipDiscount());
    		Double discountRate = salesOrderItem.getDiscountRate();
    		if (null == discountRate) {
    			discountRate = 100D;
    			salesOrderItem.setDiscountRate(discountRate);
    		}
    		parameterMap.put("discountRate", discountRate);
    		parameterMap.put("deductionAmount", salesOrderItem.getDeductionAmount());
    		salesOrderItem.setActualUnitPrice(this.calculateActualPrice(parameterMap));
    	} else {
    		salesOrderItem.setActualUnitPrice(salesOrderItem.getOriginalUnitPrice());
    	}

    	if (!"F".equals(buOrderType.getOrderCondition()))
    		salesOrderItem.setActualForeignUnitPrice(NumberUtils.round(NumberUtils.getDouble(salesOrderItem.getActualUnitPrice()) / exchangeRate, 6));
    	
    	this.refreshItemRelationAmount(buOrderType, salesOrderItem, exchangeRate);
    }

    /**
     * 更新原幣所有金額 OriginalForeignUnitPrice , ActualForeignUnitPrice ,
     * OriginalForeignSalesAmt , ActualForeignSalesAmt
     * 
     * @param salesOrderItem
     */
    private void refreshItemPriceExchangeData(SoSalesOrderItem salesOrderItem, Double exchangeRate) {
	
    if (NumberUtils.getDouble(salesOrderItem.getOriginalUnitPrice()) == 0)
	    salesOrderItem.setOriginalForeignUnitPrice(0D);
	else
	    salesOrderItem.setOriginalForeignUnitPrice(NumberUtils.round(
		    NumberUtils.getDouble(salesOrderItem.getOriginalUnitPrice())/ exchangeRate, 6));
	
	if (NumberUtils.getDouble(salesOrderItem.getActualUnitPrice()) == 0)
	    salesOrderItem.setActualForeignUnitPrice(0D);
	else if (NumberUtils.getDouble(salesOrderItem.getActualForeignUnitPrice()) == 0)
	    salesOrderItem.setActualForeignUnitPrice(NumberUtils.round(
		    NumberUtils.getDouble(salesOrderItem.getActualUnitPrice())/ exchangeRate, 6));
		salesOrderItem.setOriginalForeignSalesAmt(NumberUtils.round(
			NumberUtils.getDouble(salesOrderItem.getOriginalForeignUnitPrice()* salesOrderItem.getQuantity()), 2));
		salesOrderItem.setActualForeignSalesAmt(NumberUtils.round(
			NumberUtils.getDouble(salesOrderItem.getActualForeignUnitPrice()* salesOrderItem.getQuantity()), 2));
    }

    public Double calculateActualPrice(HashMap parameterMap) {
    	log.info("calculateActualPrice");
    	Double originalUnitPrice = (Double) parameterMap.get("originalUnitPrice");
    	String promotionCode = (String) parameterMap.get("promotionCode");
    	String discountType = (String) parameterMap.get("discountType");
    	Double discount = (Double) parameterMap.get("discount");
    	String vipPromotionCode = (String) parameterMap.get("vipPromotionCode");
    	String vipDiscountType = (String) parameterMap.get("vipDiscountType");
    	Double vipDiscount = (Double) parameterMap.get("vipDiscount");
    	Double discountRate = (Double) parameterMap.get("discountRate");
    	Double deductionAmount = (Double) parameterMap.get("deductionAmount");
    	Double actualUnitPrice = originalUnitPrice;
    	if (originalUnitPrice != null) {
    		if (StringUtils.hasText(promotionCode) && discount != null)
    			actualUnitPrice = calculateDiscountedPrice(actualUnitPrice, discountType, discount);
    		
    		if (StringUtils.hasText(vipPromotionCode) && vipDiscount != null)
    			actualUnitPrice = calculateDiscountedPrice(actualUnitPrice, vipDiscountType, vipDiscount);
    		
    		// 折扣率為正向扣除
    		if (discountRate != null)
    			actualUnitPrice = actualUnitPrice * discountRate / 100;
    		
    		// 扣除折讓金額
    		if (deductionAmount != null && deductionAmount != 0D)
    			actualUnitPrice -= deductionAmount;
    		
    		return CommonUtils.round(actualUnitPrice, 0);
    	} else {
    		return null;
    	}
    }

    /**
     * 重新計算Item的原銷售金額、實際售價、實際銷售金額、稅金
     * 
     * @param orderItem
     */
    private void refreshItemRelationAmount(BuOrderType buOrderType, SoSalesOrderItem orderItem, Double exchangeRate) {
    	log.info("refreshItemRelationAmount");
    	Double originalSalesAmount = 0D;
    	Double actualSalesAmount = 0D;
    	Double taxAmount = 0D;
    	Double actualForeignSalesAmt = 0D;
    	Double originalForeignUnitPrice = NumberUtils.getDouble(orderItem.getOriginalForeignUnitPrice()); // 原始售價
    	Double actualForeignUnitPrice = NumberUtils.getDouble(orderItem.getActualForeignUnitPrice()); // 實際售價
    	Double deductionAmount = NumberUtils.getDouble(orderItem.getDeductionAmount());
    	Double originalUnitPrice = NumberUtils.round(originalForeignUnitPrice* exchangeRate, 6); // 原始售價
    	Double actualUnitPrice = NumberUtils.round(actualForeignUnitPrice* exchangeRate, 6); // 實際售價
    	Double quantity = NumberUtils.getDouble(orderItem.getQuantity()); // 數量
    	String taxType = orderItem.getTaxType(); // 稅別
    	Double taxRate = orderItem.getTaxRate(); // 稅率
    	originalSalesAmount = NumberUtils.round(originalForeignUnitPrice* exchangeRate * quantity, 0); // 原銷售金額
    	if (orderItem.getPosActualSalesAmount() != null
    			&& orderItem.getPosActualSalesAmount() != 0) {
    		actualForeignSalesAmt = orderItem.getActualSalesAmount();// 實際外幣銷售金額
    		actualSalesAmount = orderItem.getActualSalesAmount(); // 實際銷售金額
    	} else {
    		actualForeignSalesAmt = NumberUtils.round(actualForeignUnitPrice * quantity, 2);// 實際外幣銷售金額
    		actualSalesAmount = NumberUtils.round(actualForeignUnitPrice * exchangeRate * quantity, 2); // 實際銷售金額
    	}

    	taxAmount = this.calculateTaxAmount(buOrderType, taxType, taxRate, actualSalesAmount);
    	orderItem.setOriginalUnitPrice(originalUnitPrice);
    	orderItem.setActualUnitPrice(actualUnitPrice);
    	orderItem.setOriginalForeignSalesAmt(NumberUtils.round(originalForeignUnitPrice * quantity, 2));
    	orderItem.setActualForeignSalesAmt(actualForeignSalesAmt);
    	orderItem.setOriginalSalesAmount(originalSalesAmount);
    	orderItem.setActualSalesAmount(actualSalesAmount);
    	orderItem.setTaxAmount(taxAmount);
    	
    	if (actualUnitPrice != 0 && originalUnitPrice != 0)
    			orderItem.setDiscountRate(getDiscountRate(buOrderType, actualUnitPrice + deductionAmount, originalUnitPrice));
    }

    /**
     * 計算折扣金額.
     * 
     * @param unitPrice
     * @param discountType
     * @param discount
     * @return
     */
    private Double calculateDiscountedPrice(Double unitPrice, String discountType, Double discount) {
	if ("1".equals(discountType))
	    unitPrice = unitPrice - discount;
	else if (discountType == null || "2".equals(discountType))
	    unitPrice = unitPrice * (100 - discount) / 100;
	return unitPrice;
    }

    public Double calculateTaxAmount(BuOrderType buOrderType, String taxType, Double taxRate, Double actualSalesAmount) {
    	//BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
		//BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    	Double taxAmount = 0D;
    	if ("3".equals(taxType) && taxRate != null && taxRate != 0D
    			&& actualSalesAmount != null && actualSalesAmount != 0D) {
    		//如果是F 則稅外加
    		if ("F".equals(buOrderType.getOrderCondition())) {
    			taxAmount = (actualSalesAmount * taxRate) / 100;
    		} else {
    			Double salesAmount = actualSalesAmount / (1 + taxRate / 100);
    			taxAmount = actualSalesAmount - salesAmount;
    		}
    	}
    	return CommonUtils.round(taxAmount, 2);
    }

    public List<Properties> updateAJAXPageLinesData(Properties httpRequest)
	    throws Exception {
	System.out.println("updateAJAXPageLinesData , httpRequest= " + httpRequest + "");
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest
		    .getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest
		    .getProperty(AjaxUtils.GRID_ROW_COUNT));
	    Long headId = NumberUtils
		    .getLong(httpRequest.getProperty("headId"));
	    if (headId == null) {
		throw new ValidationErrorException("傳入的銷貨單主鍵為空值！");
	    }
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;
	    if (OrderStatus.SAVE.equals(status)
		    || OrderStatus.REJECT.equals(status)
		    || OrderStatus.UNCONFIRMED.equals(status)) {
		SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
		salesOrderHead.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(
			gridData, gridLineFirstIndex, gridRowCount,
			GRID_FIELD_NAMES);
		// Get INDEX NO
		int indexNo = soSalesOrderItemDAO.findPageLineMaxIndex(headId)
			.intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long lineId = NumberUtils.getLong(upRecord
				.getProperty("lineId"));
			String itemCode = upRecord
				.getProperty(GRID_FIELD_NAMES[1]);
			if (StringUtils.hasText(itemCode)) {
			    System.out.println("updateAJAXPageLinesData , upRecord= "+ upRecord + "");
			    SoSalesOrderItem salesOrderItemPO = soSalesOrderItemDAO.findItemByIdentification(salesOrderHead.getHeadId(), lineId);
			    if (salesOrderItemPO != null) {
			    	AjaxUtils.setPojoProperties(salesOrderItemPO,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
			    	soSalesOrderItemDAO.update(salesOrderItemPO);
			    } else {
			    	indexNo++;
			    	SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
			    	AjaxUtils.setPojoProperties(salesOrderItem,upRecord, GRID_FIELD_NAMES,GRID_FIELD_TYPES);
			    	salesOrderItem.setIndexNo(Long.valueOf(indexNo));
			    	salesOrderItem.setSoSalesOrderHead(salesOrderHead);
			    	soSalesOrderItemDAO.save(salesOrderItem);
			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新銷貨明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨明細失敗！");
	}
    }

    /**
     * 載入頁面顯示付款資料明細
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXPageDataForPayment(Properties httpRequest)
	    throws Exception {
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    Long headId = NumberUtils
		    .getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	    // ======================帶入Head的值=========================
	    // String formStatus = httpRequest.getProperty("formStatus");// 狀態
	    // HashMap map = new HashMap();
	    // map.put("formStatus", formStatus);
	    // ======================取得頁面所需資料===========================
	    List<SoSalesOrderPayment> salesOrderPayments = soSalesOrderPaymentDAO
		    .findPageLine(headId, iSPage, iPSize);
	    if (salesOrderPayments != null && salesOrderPayments.size() > 0) {
		// 取得第一筆的INDEX
		Long firstIndex = salesOrderPayments.get(0).getIndexNo();
		// 取得最後一筆 INDEX
		Long maxIndex = soSalesOrderPaymentDAO
			.findPageLineMaxIndex(headId);
		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_FIELD_NAMES_PAYMENT,
			GRID_FIELD_DEFAULT_VALUES_PAYMENT, salesOrderPayments,
			gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			GRID_FIELD_NAMES_PAYMENT,
			GRID_FIELD_DEFAULT_VALUES_PAYMENT, gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的付款資料明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的付款資料明細失敗！");
	}
    }

    /**
     * 更新PAYMENT PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> updateAJAXPaymentPageLinesData(
	    Properties httpRequest) throws Exception {
	try {
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest
		    .getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest
		    .getProperty(AjaxUtils.GRID_ROW_COUNT));
	    Long headId = NumberUtils
		    .getLong(httpRequest.getProperty("headId"));
	    String status = httpRequest.getProperty("status");
	    String errorMsg = null;

	    if (OrderStatus.SAVE.equals(status)
		    || OrderStatus.REJECT.equals(status)
		    || OrderStatus.UNCONFIRMED.equals(status)) {
		SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
		salesOrderHead.setHeadId(headId);
		// 將STRING資料轉成List Properties record data
		List<Properties> upRecords = AjaxUtils.getGridFieldValue(
			gridData, gridLineFirstIndex, gridRowCount,
			GRID_FIELD_NAMES_PAYMENT);
		// Get INDEX NO
		int indexNo = soSalesOrderPaymentDAO.findPageLineMaxIndex(
			headId).intValue();
		if (upRecords != null) {
		    for (Properties upRecord : upRecords) {
			// 先載入HEAD_ID OR LINE DATA
			Long posPaymentId = NumberUtils.getLong(upRecord
				.getProperty("posPaymentId"));
			String posPaymentType = upRecord
				.getProperty(GRID_FIELD_NAMES_PAYMENT[1]);
			if (StringUtils.hasText(posPaymentType)) {
			    SoSalesOrderPayment salesOrderPaymentPO = soSalesOrderPaymentDAO
				    .findPaymentByIdentification(salesOrderHead
					    .getHeadId(), posPaymentId);
			    if (salesOrderPaymentPO != null) {
				AjaxUtils.setPojoProperties(
					salesOrderPaymentPO, upRecord,
					GRID_FIELD_NAMES_PAYMENT,
					GRID_FIELD_TYPES_PAYMENT);
				soSalesOrderPaymentDAO
					.update(salesOrderPaymentPO);
			    } else {
				indexNo++;
				SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
				AjaxUtils.setPojoProperties(salesOrderPayment,
					upRecord, GRID_FIELD_NAMES_PAYMENT,
					GRID_FIELD_TYPES_PAYMENT);
				salesOrderPayment.setIndexNo(Long
					.valueOf(indexNo));
				salesOrderPayment
					.setSoSalesOrderHead(salesOrderHead);
				soSalesOrderPaymentDAO.save(salesOrderPayment);
			    }
			}
		    }
		}
	    }
	    return AjaxUtils.getResponseMsg(errorMsg);
	} catch (Exception ex) {
	    log.error("更新銷貨付款資料明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨付款資料失敗！");
	}
    }

    /**
     * 處理AJAX參數(line品號、庫別、數量變動時計算)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws ValidationErrorException
     */
    public List<Properties> getAJAXItemData(Properties httpRequest) throws ValidationErrorException {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String itemIndexNo = null;
	try {
		itemIndexNo = httpRequest.getProperty("itemIndexNo");
		String brandCode = httpRequest.getProperty("brandCode");
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		String priceType = httpRequest.getProperty("priceType");// 價格類型
		String shopCode = httpRequest.getProperty("shopCode");
		String customerType = httpRequest.getProperty("customerType");
		String vipType = httpRequest.getProperty("vipType");
		String itemCode = httpRequest.getProperty("itemCode");
		String warehouseCode = httpRequest.getProperty("warehouseCode");
		String quantity = httpRequest.getProperty("quantity");
		String deductionAmount = httpRequest.getProperty("deductionAmount");
		String discountRate = httpRequest.getProperty("discountRate");
		String exportExchangeRate = httpRequest.getProperty("exportExchangeRate");
		ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewService.findById(brandCode, itemCode);
		// 用國際碼查詢品號
		if (imItemEanPriceView != null) {
			itemCode = imItemEanPriceView.getItemCode();
		}
		Double exchangeRate = NumberUtils.getDouble(exportExchangeRate) == 0D ? 1D : NumberUtils.getDouble(exportExchangeRate);
		if (!StringUtils.hasText(discountRate)) {
			discountRate = "100";
		}
		String promotionCode = httpRequest.getProperty("promotionCode");// 活動代號
		String vipPromotionCode = httpRequest.getProperty("vipPromotionCode");// vip類別代號
		String warehouseManager = httpRequest.getProperty("warehouseManager");
		String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
		String originalUnitPrice = httpRequest.getProperty("originalUnitPrice");
		String salesDate = httpRequest.getProperty("salesDate");
		salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		String taxType = httpRequest.getProperty("taxType");
		String taxRate = httpRequest.getProperty("taxRate");
		String actionId = httpRequest.getProperty("actionId");
		Double actualForeignUnitPrice = NumberUtils.getDouble(httpRequest.getProperty("actualForeignUnitPrice"));

		ImItem imItem = imItemService.findItem(brandCode, itemCode);
		// 查詢相關參數
		HashMap conditionMap = new HashMap();
		conditionMap.put("brandCode", brandCode);
		conditionMap.put("priceType", priceType);
		conditionMap.put("warehouseEmployee", warehouseEmployee);
		conditionMap.put("warehouseManager", warehouseManager);
		conditionMap.put("shopCode", shopCode);
		conditionMap.put("customerType", customerType);
		conditionMap.put("vipType", vipType);
		conditionMap.put("itemCode", itemCode);
		conditionMap.put("warehouseCode", warehouseCode);
		conditionMap.put("quantity", quantity);
		conditionMap.put("discountRate", discountRate);
		conditionMap.put("deductionAmount", deductionAmount);
		conditionMap.put("promotionCode", promotionCode);
		conditionMap.put("vipPromotionCode", vipPromotionCode);
		conditionMap.put("originalUnitPrice", originalUnitPrice);
		conditionMap.put("salesDate", salesDate);
		conditionMap.put("taxType", taxType);
		conditionMap.put("taxRate", taxRate);
		SoSalesOrderItem salesOrderItem = setSoItemData(conditionMap); // 原銷售明細
		SoSalesOrderItem actualSalesOrderItem = appGetSaleItemInfoService.getSaleItemInfo(conditionMap, salesOrderItem);// 查詢後銷售明細
		
		if ("Y".equals(actualSalesOrderItem.getIsServiceItem()) && "1".equals(actionId)) {
			actualSalesOrderItem.setOriginalUnitPrice(null);
		}

		BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
		
		// price相關資料更新
		refreshItemPriceExchangeData(actualSalesOrderItem, exchangeRate);
		refreshItemPriceRelationData(buOrderType, actualSalesOrderItem, exchangeRate);

		// 如果為自訂輸入單價
		if ("3".equals(actionId)) {
			actualSalesOrderItem.setActualForeignUnitPrice(actualForeignUnitPrice);
			actualSalesOrderItem.setActualForeignSalesAmt(actualForeignUnitPrice * NumberUtils.getDouble(quantity));
			actualSalesOrderItem.setActualUnitPrice(actualForeignUnitPrice * exchangeRate);
			actualSalesOrderItem.setActualSalesAmount(actualForeignUnitPrice * exchangeRate * NumberUtils.getDouble(quantity));
		}
		
		ImWarehouse imWarehouse = imWarehouseService.findByBrandCodeAndWarehouseCode(
				brandCode, actualSalesOrderItem.getWarehouseCode(), null);
		if (imWarehouse != null)
			actualSalesOrderItem.setWarehouseName(imWarehouse.getWarehouseName());

		properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getItemCode(), ""));
		properties.setProperty("WarehouseCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getWarehouseCode(), ""));
		properties.setProperty("WarehouseName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getWarehouseName(), "查無資料"));
		properties.setProperty("OriginalUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalUnitPrice(), "0.0"));
		properties.setProperty("ActualUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualUnitPrice(), "0.0"));
		properties.setProperty("CurrentOnHandQty", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getCurrentOnHandQty(), ""));
		properties.setProperty("Quantity", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getQuantity(), ""));
		properties.setProperty("OriginalSalesAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalSalesAmount(), ""));
		properties.setProperty("ActualSalesAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualSalesAmount(), ""));
		properties.setProperty("DeductionAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDeductionAmount(), "0.00"));
		properties.setProperty("DiscountRate", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscountRate(),"100.0"));
		properties.setProperty("IsTax", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getIsTax(), "1"));
		properties.setProperty("PromotionCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getPromotionCode(), ""));
		properties.setProperty("PromotionName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getPromotionName(), "查無資料"));
		properties.setProperty("DiscountType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscountType(), ""));
		properties.setProperty("Discount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getDiscount(), ""));
		properties.setProperty("VipPromotionCode", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipPromotionCode(), ""));
		properties.setProperty("VipPromotionName", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipPromotionName(), "查無資料"));
		properties.setProperty("VipDiscountType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipDiscountType(), ""));
		properties.setProperty("VipDiscount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getVipDiscount(), ""));
		properties.setProperty("TaxType", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxType(), "3"));
		properties.setProperty("TaxRate", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxRate(), "5"));
		properties.setProperty("IsServiceItem", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getIsServiceItem(), ""));
		properties.setProperty("TaxAmount", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getTaxAmount(), "0.0"));
		// 原幣區
		properties.setProperty("OriginalForeignUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalForeignUnitPrice(), "0.0"));
		properties.setProperty("ActualForeignUnitPrice", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualForeignUnitPrice(), "0.0"));
		properties.setProperty("OriginalForeignSalesAmt", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getOriginalForeignSalesAmt(), "0.0"));
		properties.setProperty("ActualForeignSalesAmt", AjaxUtils.getPropertiesValue(actualSalesOrderItem.getActualForeignSalesAmt(), "0.0"));
		properties.setProperty("DeductionForeignAmount", AjaxUtils.getPropertiesValue(
				NumberUtils.roundToStr(NumberUtils.getDouble(actualSalesOrderItem.getDeductionAmount())/ exchangeRate, 2), "0.00"));

		if (imItem != null) {
			properties.setProperty("ItemCName", AjaxUtils.getPropertiesValue(imItem.getItemCName(), "商品未命名"));
			properties.setProperty("ImportCost", AjaxUtils.getPropertiesValue(imItem.getLastUnitCost(), ""));
			properties.setProperty("ImportCurrencyCode", AjaxUtils.getPropertiesValue(imItem.getLastCurrencyCode(), ""));
			properties.setProperty("SupplierItemCode", AjaxUtils.getPropertiesValue(imItem.getSupplierItemCode(), ""));
			properties.setProperty("StandardPurchaseCost", AjaxUtils.getPropertiesValue(imItem.getStandardPurchaseCost(), ""));
			properties.setProperty("ItemDiscountType", AjaxUtils.getPropertiesValue(imItem.getVipDiscount(), ""));
			properties.setProperty("AllowMinusStock", AjaxUtils.getPropertiesValue(imItem.getAllowMinusStock(), ""));
		} else {
			properties.setProperty("ItemCName", AjaxUtils.getPropertiesValue(null, "查無資料"));
			properties.setProperty("AllowMinusStock", "N");
		}

		result.add(properties);
		return result;
	} catch (Exception ex) {
		log.error("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
		throw new ValidationErrorException("更新明細資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	}
    }

    private SoSalesOrderItem setSoItemData(HashMap conditionMap) {
    log.info("setSoItemData");
	String itemCode = (String) conditionMap.get("itemCode");
	String warehouseCode = (String) conditionMap.get("warehouseCode");
	String quantity = (String) conditionMap.get("quantity");
	String discountRate = (String) conditionMap.get("discountRate");
	String deductionAmount = (String) conditionMap.get("deductionAmount");
	String promotionCode = (String) conditionMap.get("promotionCode");
	String vipPromotionCode = (String) conditionMap.get("vipPromotionCode");
	String originalUnitPrice = (String) conditionMap.get("originalUnitPrice");
	String taxType = (String) conditionMap.get("taxType");
	String taxRate = (String) conditionMap.get("taxRate");
	SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();

	if (StringUtils.hasText(promotionCode)) {
	    promotionCode = promotionCode.trim().toUpperCase();
	}
	
	salesOrderItem.setItemCode(itemCode);
	salesOrderItem.setWarehouseCode(warehouseCode);
	salesOrderItem.setPromotionCode(promotionCode);
	salesOrderItem.setVipPromotionCode(vipPromotionCode);
	salesOrderItem.setTaxType(taxType);
	
	// 數量
	Double salesQuantity = NumberUtils.getDouble(quantity);
	salesOrderItem.setQuantity(salesQuantity);
	conditionMap.put("quantity", salesQuantity);

	Double actualDiscountRate = NumberUtils.getDouble(discountRate);
	salesOrderItem.setDiscountRate(actualDiscountRate);
	conditionMap.put("discountRate", actualDiscountRate);

	Double actualDeductionAmount = NumberUtils.getDouble(deductionAmount);
	salesOrderItem.setDeductionAmount(actualDeductionAmount);
	conditionMap.put("deductionAmount", actualDeductionAmount);

	Double origiUnitPrice = NumberUtils.getDouble(originalUnitPrice);
	salesOrderItem.setOriginalUnitPrice(origiUnitPrice);
	conditionMap.put("originalUnitPrice", origiUnitPrice);

	Double actualTaxRate = NumberUtils.getDouble(taxRate);
	salesOrderItem.setTaxRate(actualTaxRate);
	conditionMap.put("taxRate", actualTaxRate);

	return salesOrderItem;
    }

    /**
     * 依據primary key為查詢條件，取得銷貨單主檔
     * 
     * @param headId
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public SoSalesOrderHead findSoSalesOrderHeadById(Long headId)
	    throws Exception {
	try {
	    SoSalesOrderHead salesOrder = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, headId);
	    return salesOrder;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * 將有刪除註記的item移除，並合計所有Item的金額，包括原總銷售金額、總實際銷售金額、稅金總額
     * 
     * @param orderHead
     * @param conditionMap
     * @param event
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> executeCountTotalAmount(Properties httpRequest) throws ValidationErrorException {
    log.info("executeCountTotalAmount");
	try {
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    // ===================取得傳遞的的參數===================
	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    if (salesOrderHeadPO == null) {
		throw new ValidationErrorException("查無銷貨單主鍵：" + headId + "的資料！");
	    }
	    String status = httpRequest.getProperty("status");// 狀態
	    Double totalItemQuantity = 0D;
	    Double commissionRate = 0D;
	    Double exchangeRate = 1D;
	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
	    if (OrderStatus.SAVE.equals(status)
		    || OrderStatus.REJECT.equals(status)
		    || OrderStatus.UNCONFIRMED.equals(status)) {
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
		String shopCode = httpRequest.getProperty("shopCode");// 品牌代號
		String warehouseEmployee = httpRequest.getProperty("warehouseEmployee");
		String warehouseManager = httpRequest.getProperty("warehouseManager");
		String customerCode = httpRequest.getProperty("customerCode");
		String searchCustomerType = httpRequest.getProperty("searchCustomerType");
		String priceType = httpRequest.getProperty("priceType");
		String salesDate = httpRequest.getProperty("salesDate");
		String exportCommissionRate = httpRequest.getProperty("exportCommissionRate");
		String taxType = httpRequest.getProperty("taxType");
		Double taxRate = NumberUtils.getDouble(httpRequest.getProperty("taxRate"));
		commissionRate = NumberUtils.getDouble(exportCommissionRate);
		String exportExchangeRate = httpRequest.getProperty("exportExchangeRate");
		exchangeRate = NumberUtils.getDouble(exportExchangeRate) == 0D ? 1D : NumberUtils.getDouble(exportExchangeRate);
		Double exportExpense = NumberUtils.getDouble(httpRequest.getProperty("exportExpense"));// 其他費用
		salesOrderHeadPO.setExportCommissionRate(commissionRate);
		salesOrderHeadPO.setExportExchangeRate(exchangeRate);
		salesOrderHeadPO.setTaxType(taxType);
		salesOrderHeadPO.setTaxRate(taxRate);
		salesOrderHeadPO.setExportExpense(exportExpense);
		salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd", salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		// ================取得customerType、vipType、vipPromotionCode======================
		HashMap customerInfoMap = getCustomerInfo(brandCode, orderTypeCode, customerCode, searchCustomerType, null);
		String customerType = (String) customerInfoMap.get("customerType");
		String vipType = (String) customerInfoMap.get("vipType");
		String vipPromotionCode = (String) customerInfoMap.get("vipPromotionCode");
		// ================================================================================
		HashMap conditionMap = new HashMap();
		conditionMap.put("brandCode", brandCode);
		conditionMap.put("shopCode", shopCode);
		conditionMap.put("warehouseEmployee", warehouseEmployee);
		conditionMap.put("warehouseManager", warehouseManager);
		conditionMap.put("customerType", customerType);
		conditionMap.put("vipType", vipType);
		conditionMap.put("vipPromotionCode", vipPromotionCode);
		conditionMap.put("priceType", priceType);
		conditionMap.put("salesDate", salesDate);
		conditionMap.put("beforeChangeStatus", status);
		conditionMap.put("exchangeRate", exchangeRate);
		getItemPriceRelationData(salesOrderHeadPO, conditionMap, false);
	    } else {
		// =========================計算商品總數================
		List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
		if (salesOrderItems != null && salesOrderItems.size() > 0) {
			for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
				if (!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderItem.getIsDeleteRecord())) {
					Double itemQuantity = salesOrderItem.getQuantity();
					Double actualUnitPrice = salesOrderItem.getActualUnitPrice();
					if (itemQuantity != null && actualUnitPrice != null) {
						totalItemQuantity += itemQuantity;
					}
				}
			}
		}
	    }

	    Map countResultMap = countTotalAmount(salesOrderHeadPO);
	    totalItemQuantity = (Double) countResultMap.get("totalItemQuantity");

	    soSalesOrderHeadDAO.update(salesOrderHeadPO);

	    // ===================================================
	    Double totalOriginalSalesAmount = NumberUtils.getDouble(salesOrderHeadPO.getTotalOriginalSalesAmount());
	    Double originalTotalFrnSalesAmt = NumberUtils.getDouble(salesOrderHeadPO.getOriginalTotalFrnSalesAmt());
	    Double totalActualSalesAmount = NumberUtils.getDouble(salesOrderHeadPO.getTotalActualSalesAmount());
	    Double actualTotalFrnSalesAmt = NumberUtils.getDouble(salesOrderHeadPO.getActualTotalFrnSalesAmt());
	    Double taxAmount = (Double) countResultMap.get("taxAmount");
	    Double taxFrnAmount = (Double) countResultMap.get("taxFrnAmount");
	    Double totalOtherExpense = NumberUtils.getDouble(salesOrderHeadPO.getTotalOtherExpense());
	    Double totalDeductionAmount = 0D;
	    Double totalDeductionFrnAmount = 0D;
	    Double totalNoneTaxSalesAmount = 0D;
	    Double totalNoneTaxFrnSalesAmount = 0D;
	    Double expenseForeignAmount = NumberUtils.getDouble(salesOrderHeadPO.getExpenseForeignAmount());
	    Double expenseLocalAmount = NumberUtils.getDouble(salesOrderHeadPO.getExpenseLocalAmount());
	    Double totalAmount = totalActualSalesAmount + expenseLocalAmount + totalOtherExpense + taxAmount;
	    Double totalForeignAmount = actualTotalFrnSalesAmt + expenseForeignAmount + salesOrderHeadPO.getExportExpense() + taxFrnAmount;

	    BuOrderTypeId buOrderTypeId = new BuOrderTypeId(salesOrderHeadPO.getBrandCode(), salesOrderHeadPO.getOrderTypeCode());
    	BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    	
    	if ("F".equals(buOrderType.getOrderCondition())){
    		totalNoneTaxSalesAmount = totalAmount - taxAmount;
	    	totalNoneTaxFrnSalesAmount = totalForeignAmount - taxFrnAmount;
	    } else {
	    	totalAmount -= taxAmount;
	    	totalForeignAmount -= taxFrnAmount;
	    	totalNoneTaxSalesAmount = totalActualSalesAmount - taxAmount;
	    	totalNoneTaxFrnSalesAmount = totalActualSalesAmount - taxFrnAmount;	    	
	    }

	    totalDeductionAmount = totalOriginalSalesAmount - totalActualSalesAmount;
	    totalDeductionFrnAmount = originalTotalFrnSalesAmt - actualTotalFrnSalesAmt;
	    
	    Double totalBonusPointAmount =((SoSalesOrderHead)soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, headId)).getTotalBonusPointAmount();//可轉換點數金額
	    properties.setProperty("totalBonusPointAmount", OperationUtils.roundToStr(totalBonusPointAmount, 2));
	    // 本幣
	    properties.setProperty("TotalOriginalSalesAmount", OperationUtils.roundToStr(totalOriginalSalesAmount, 2));
	    properties.setProperty("TotalDeductionAmount", OperationUtils.roundToStr(totalDeductionAmount, 2));
	    properties.setProperty("TaxAmount", OperationUtils.roundToStr(taxAmount, 2));
	    properties.setProperty("TotalOtherExpense", OperationUtils.roundToStr(totalOtherExpense, 2));
	    properties.setProperty("ExpenseLocalAmount", OperationUtils.roundToStr(expenseLocalAmount, 2));
	    properties.setProperty("TotalActualSalesAmount", OperationUtils.roundToStr(totalActualSalesAmount, 2));
	    properties.setProperty("TotalForeignAmount", OperationUtils.roundToStr(totalForeignAmount, 2));
	    properties.setProperty("TotalNoneTaxSalesAmount", OperationUtils.roundToStr(totalNoneTaxSalesAmount, 2));

	    // 原幣
	    properties.setProperty("OriginalTotalFrnSalesAmt", OperationUtils.roundToStr(originalTotalFrnSalesAmt, 2));
	    properties.setProperty("TotalDeductionFrnAmount", OperationUtils.roundToStr(totalDeductionFrnAmount, 2));
	    properties.setProperty("TaxFrnAmount", OperationUtils.roundToStr(taxFrnAmount, 2));
	    properties.setProperty("TotalOtherFrnExpense", OperationUtils.roundToStr(salesOrderHeadPO.getExportExpense(), 2));
	    properties.setProperty("ExpenseForeignAmount", OperationUtils.roundToStr(expenseForeignAmount, 2));
	    properties.setProperty("ActualTotalFrnSalesAmt", OperationUtils.roundToStr(actualTotalFrnSalesAmt, 2));
	    properties.setProperty("TotalAmount", OperationUtils.roundToStr(totalAmount, 2));
	    properties.setProperty("TotalNoneTaxFrnSalesAmount", OperationUtils.roundToStr(totalNoneTaxFrnSalesAmount, 2));

	    properties.setProperty("TotalItemQuantity", OperationUtils.roundToStr(totalItemQuantity, 0));
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("銷售單金額統計失敗，原因：" + ex.toString());
	    throw new ValidationErrorException("銷售單金額統計失敗！");
	}
    }
    public List<Properties> executeCheckAmountAfterExtend(Properties httpRequest) throws ValidationErrorException {
        log.info("executeCountTotalAmount");
    	try {
    	    List<Properties> result = new ArrayList();
    	    Properties properties = new Properties();
    	    // ===================取得傳遞的的參數===================
    	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
    	    if (salesOrderHeadPO == null) {
    		throw new ValidationErrorException("查無銷貨單主鍵：" + headId + "的資料！");
    	    }
    	    String status = httpRequest.getProperty("status");// 狀態
    	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
    	    if (OrderStatus.SAVE.equals(status)
    		    || OrderStatus.REJECT.equals(status)
    		    || OrderStatus.UNCONFIRMED.equals(status)) {

    		HashMap conditionMap = new HashMap();
    		List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
    		for(SoSalesOrderItem item:salesOrderItems){
    			//item.setActualForeignUnitPrice(actualForeignUnitPrice);
    			item.setActualForeignSalesAmt(item.getActualForeignUnitPrice() * NumberUtils.getDouble(item.getQuantity()));
    			//item.setActualUnitPrice(item.getActualForeignUnitPrice() * exchangeRate);
    			item.setActualSalesAmount(item.getActualUnitPrice() * NumberUtils.getDouble(item.getQuantity()));
    		}
    		//getItemPriceRelationData(salesOrderHeadPO, conditionMap, false);
    	    }

    	    Map countResultMap = countTotalAmount(salesOrderHeadPO);
    	    soSalesOrderHeadDAO.update(salesOrderHeadPO);

    	    result.add(properties);
    	    return result;
    	} catch (Exception ex) {
    	    log.error("銷售單金額統計失敗，原因：" + ex.toString());
    	    throw new ValidationErrorException("銷售單金額統計失敗！");
    	}
        }
    
    
    /**
     * 取得客戶資料
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param tmpCustomerCode
     * @param searchCustomerType
     * @param isEnable
     * @return HashMap
     */
    private HashMap getCustomerInfo(String brandCode, String orderTypeCode,
	    String tmpCustomerCode, String searchCustomerType, String isEnable)
	    throws Exception {
	HashMap result = new HashMap();
	if (StringUtils.hasText(tmpCustomerCode)) {
	    tmpCustomerCode = tmpCustomerCode.trim().toUpperCase();
	    BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewService
		    .findCustomerByType(brandCode, tmpCustomerCode,
			    searchCustomerType, isEnable);
	    if (customerWithAddressView != null) {
		result.put("actualCustomerCode", customerWithAddressView
			.getCustomerCode());
		result.put("customerType", customerWithAddressView
			.getCustomerTypeCode());
		result.put("vipType", customerWithAddressView.getVipTypeCode());
		if (!POSTYPECODE.equals(orderTypeCode)) {
		    result.put("vipPromotionCode", customerWithAddressView
			    .getPromotionCode());
		}
	    }
	}
	return result;
    }

    /**
     * 重新計算Line的金額
     * 
     * @param orderHead
     * @param conditionMap
     * @throws Exception
     */
    private void getItemPriceRelationData(SoSalesOrderHead orderHead,
	    HashMap conditionMap, boolean isReplace) throws Exception {

	String vipPromotionCode = (String) conditionMap.get("vipPromotionCode");
	String defaultPromotionCode = (String) conditionMap
		.get("promotionCode");
	String defaultWarehouseCode = (String) conditionMap
		.get("warehouseCode");
	String defaultTaxType = (String) conditionMap.get("taxType");
	Double defaultTaxRate = (Double) conditionMap.get("taxRate");
	Double defaultDiscountRate = (Double) conditionMap.get("discountRate");
	Double exchangeRate = (Double) conditionMap.get("exchangeRate");
	List soSalesOrderItems = orderHead.getSoSalesOrderItems();
	if (soSalesOrderItems != null) {
	    for (int i = 0; i < soSalesOrderItems.size(); i++) {
		SoSalesOrderItem salesOrderItem = (SoSalesOrderItem) soSalesOrderItems
			.get(i);
		String itemCode = salesOrderItem.getItemCode();
		String warehouseCode = salesOrderItem.getWarehouseCode();
		Double quantity = salesOrderItem.getQuantity();
		if (quantity != null && quantity != 0D) {
		    quantity = CommonUtils.round(quantity, 2);
		    salesOrderItem.setQuantity(quantity);
		}
		salesOrderItem.setVipPromotionCode(vipPromotionCode);// 依據head的vipPromotionCode將item的替換掉
		String promotionCode = salesOrderItem.getPromotionCode();
		Double discountRate = salesOrderItem.getDiscountRate();
		if (discountRate == null) {
		    discountRate = 100D;
		    salesOrderItem.setDiscountRate(discountRate);
		}
		Double deductionAmount = salesOrderItem.getDeductionAmount();
		Double originalUnitPrice = salesOrderItem
			.getOriginalUnitPrice();
		String taxType = salesOrderItem.getTaxType();
		Double taxRate = salesOrderItem.getTaxRate();
		conditionMap.put("itemCode", itemCode);
		conditionMap.put("quantity", quantity);
		conditionMap.put("deductionAmount", deductionAmount);
		conditionMap.put("originalUnitPrice", originalUnitPrice);
		if (isReplace) {
		    salesOrderItem.setWarehouseCode(defaultWarehouseCode);
		    salesOrderItem.setDiscountRate(defaultDiscountRate);
		    salesOrderItem.setPromotionCode(defaultPromotionCode);
		    salesOrderItem.setTaxType(defaultTaxType);
		    salesOrderItem.setTaxRate(defaultTaxRate);
		} else {
		    conditionMap.put("warehouseCode", warehouseCode);
		    conditionMap.put("discountRate", discountRate);
		    conditionMap.put("promotionCode", promotionCode);
		    conditionMap.put("taxType", taxType);
		    conditionMap.put("taxRate", taxRate);
		}
		Double actualForeignUnitPrice = NumberUtils
			.getDouble(salesOrderItem.getActualForeignUnitPrice());
		// 查詢後的每一筆銷售明細
		SoSalesOrderItem soItemInfo = appGetSaleItemInfoService
			.getSaleItemInfo(conditionMap,
				setSoItemBean(conditionMap));
		// copy查詢後的每一筆銷售明細
		setSoItemInfo(salesOrderItem, soItemInfo);
		if (!isReplace) {
		    salesOrderItem
			    .setActualForeignUnitPrice(actualForeignUnitPrice);
		    salesOrderItem
			    .setActualForeignSalesAmt(actualForeignUnitPrice
				    * NumberUtils.getDouble(quantity));
		    salesOrderItem.setActualUnitPrice(actualForeignUnitPrice
			    * exchangeRate);
		    salesOrderItem.setActualSalesAmount(actualForeignUnitPrice
			    * exchangeRate * NumberUtils.getDouble(quantity));
		}
	    }
	}
    }

    /**
     * 合計所有Item的金額，包括原總銷售金額、總實際銷售金額、稅金總額
     * 
     * @param orderHead
     * @param conditionMap
     * @param event
     * @throws FormException
     * @throws Exception
     */
    public Map countTotalAmount(SoSalesOrderHead orderHead) throws FormException, Exception {
	try {
	    HashMap returnMap = new HashMap();
	    List<SoSalesOrderItem> soSalesOrderItems = orderHead.getSoSalesOrderItems();
	    log.info("SIZE:"+soSalesOrderItems.size());
	    Double totalOriginalSalesAmount = 0D;
	    Double totalOriginalForeignSalesAmount = 0D;
	    Double totalActualSalesAmount = 0D;
	    Double totalActualForeignSalesAmount = 0D;
	    Double taxFrnAmount = 0D;
	    Double totalItemQuantity = 0D;
	    Double exchangeRate = NumberUtils.getDouble(orderHead.getExportExchangeRate());
	    Double commissionRate = NumberUtils.getDouble(orderHead.getExportCommissionRate());
	    Double expenseForeignAmount = 0D;
	    Double totalOtherFrnExpense = NumberUtils.getDouble(orderHead.getExportExpense());
	    
		BuOrderTypeId buOrderTypeId = new BuOrderTypeId(orderHead.getBrandCode(), orderHead.getOrderTypeCode());
		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
		
	    if (soSalesOrderItems != null && soSalesOrderItems.size() > 0) {
	    	for (SoSalesOrderItem soSalesOrderItem : soSalesOrderItems) {
	    		log.info("ITEM/QTY:"+soSalesOrderItem.getItemCode()+"/"+soSalesOrderItem.getQuantity());
	    		refreshItemPriceExchangeData(soSalesOrderItem, exchangeRate);
	    		refreshItemPriceRelationData(buOrderType, soSalesOrderItem, exchangeRate);
	    		if (!AjaxUtils.IS_DELETE_RECORD_TRUE.equals(soSalesOrderItem.getIsDeleteRecord())) {
	    			totalOriginalSalesAmount += NumberUtils.getDouble(soSalesOrderItem.getOriginalSalesAmount());
	    			totalOriginalForeignSalesAmount += NumberUtils.getDouble(soSalesOrderItem.getOriginalForeignSalesAmt());
	    			totalActualSalesAmount += NumberUtils.getDouble(soSalesOrderItem.getActualSalesAmount());
	    			totalActualForeignSalesAmount += NumberUtils.getDouble(soSalesOrderItem.getActualForeignSalesAmt());
	    			// taxAmount += soSalesOrderItem.getTaxAmount();
	    			Double itemQuantity = soSalesOrderItem.getQuantity();
	    			Double actualUnitPrice = soSalesOrderItem.getActualUnitPrice();
	    			if (itemQuantity != null && actualUnitPrice != null) {
	    				totalItemQuantity += itemQuantity;
	    			}
	    		}// END IF IS_DELETE_RECORD_TRUE
	    	}// END FOR
	    }

	    // 小數點位數
	    int dec = 2;
	    // 無條件進位
	    boolean cellUp = false;
	    // 如果POS銷售單以整數計
	    if ("SOP".equals(orderHead.getOrderTypeCode())) {
	    	dec = 0;
	    	cellUp = true;
	    }

	    // 手續費
	    expenseForeignAmount = NumberUtils.round(totalActualForeignSalesAmount * commissionRate / 100, dec, cellUp);
	    orderHead.setExpenseForeignAmount(expenseForeignAmount);
	    orderHead.setExpenseLocalAmount(expenseForeignAmount * exchangeRate);

	    // 原始金額
	    orderHead.setOriginalTotalFrnSalesAmt(NumberUtils.round(totalOriginalForeignSalesAmount, dec, cellUp));
	    orderHead.setTotalOriginalSalesAmount(NumberUtils.round(totalOriginalForeignSalesAmount * exchangeRate, 0, cellUp));

	    // 實際金額
	    orderHead.setActualTotalFrnSalesAmt(NumberUtils.round(totalActualForeignSalesAmount, dec, cellUp));
	    orderHead.setTotalActualSalesAmount(NumberUtils.round(totalActualForeignSalesAmount * exchangeRate, dec, cellUp));

	    // 其他費用
	    orderHead.setExportExpense(totalOtherFrnExpense);
	    orderHead.setTotalOtherExpense(totalOtherFrnExpense * exchangeRate);
	    taxFrnAmount = calculateTaxAmount(buOrderType, orderHead.getTaxType(), orderHead.getTaxRate(), totalActualForeignSalesAmount + expenseForeignAmount + totalOtherFrnExpense);
	    orderHead.setTaxAmount(NumberUtils.round(taxFrnAmount * exchangeRate, dec, cellUp));

	    returnMap.put("taxFrnAmount", NumberUtils.round(taxFrnAmount, dec, cellUp));
	    returnMap.put("taxAmount", NumberUtils.round(taxFrnAmount * exchangeRate, dec, cellUp));
	    returnMap.put("totalItemQuantity", totalItemQuantity);
	    return returnMap;
	} catch (Exception ex) {
	    log.error("銷售單金額統計時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單金額統計時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新銷貨單主檔
     * 
     * @param parameterMap
     * @throws FormException
     * @throws Exception
     */
    public void updateSalesOrder(Map parameterMap) throws FormException, Exception {
    log.info("updateSalesOrder");
	try {
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    // 取得欲更新的bean
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);
	    modifySoSalesOrder(salesOrderHeadPO, loginEmployeeCode);
	} catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 取單號後更新銷貨單主檔
     * 
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateSalesOrderWithActualOrderNO(Map parameterMap)
	    throws FormException, Exception {
	HashMap resultMap = new HashMap();
	try {
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Long headId = NumberUtils.getLong((String) PropertyUtils
		    .getProperty(formLinkBean, "headId"));
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginEmployeeCode");
	    // 取得欲更新的bean
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);
	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO,
		    loginEmployeeCode);
	    resultMap.put("entityBean", salesOrderHeadPO);
	    resultMap.put("resultMsg", resultMsg);
	    return resultMap;
	} catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新檢核後的銷貨單資料
     * 
     * @param salesOrderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @return List
     * @throws ValidationErrorException
     */
    public List updateCheckedSalesOrderData(Map parameterMap) throws FormException {
    log.info("updateCheckedSalesOrderData");
	List errorMsgs = new ArrayList(0);
	try {
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    String identification = MessageStatus.getIdentification(
		    salesOrderHeadPO.getBrandCode(), salesOrderHeadPO
			    .getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
	    HashMap conditionMap = getConditionData(parameterMap);
	    // 清除ProgramLog
	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    checkSalesOrderHead(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);// 檢核銷貨主檔資料(SalesOrderHead)

	    checkSOItems(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);
	    checkSalesOrderPayments(salesOrderHeadPO, conditionMap, PROGRAM_ID, identification, errorMsgs);
	    soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    return errorMsgs;
	} catch (Exception ex) {
	    log.error("銷貨單檢核後存檔失敗，原因：" + ex.toString());
	    throw new FormException("銷貨單檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }

    private HashMap getConditionData(Map parameterMap) throws FormException,
	    Exception {
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	Object otherBean = parameterMap.get("vatBeanOther");
	HashMap conditionMap = new HashMap();
	// 取出參數
	String shopCode = (String) PropertyUtils.getProperty(formBindBean,
		"shopCode");
	String salesDate = (String) PropertyUtils.getProperty(formBindBean,
		"salesOrderDate");
	String customsNo = (String) PropertyUtils.getProperty(formBindBean, "customsNo");
	salesDate = DateUtils.format(DateUtils.parseDate("yyyy/MM/dd",
		salesDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
	String brandCode = (String) PropertyUtils.getProperty(formLinkBean,
		"brandCode");
	String orderTypeCode = (String) PropertyUtils.getProperty(formLinkBean,
		"orderTypeCode");
	String customerCode_var = (String) PropertyUtils.getProperty(
		formLinkBean, "customerCode_var");
	String searchCustomerType = (String) PropertyUtils.getProperty(
		formLinkBean, "searchCustomerType");
	String beforeChangeStatus = (String) PropertyUtils.getProperty(
		otherBean, "beforeChangeStatus");
	String formAction = (String) PropertyUtils.getProperty(otherBean,
		"formAction");
	String loginEmployeeCode = (String) PropertyUtils.getProperty(
		otherBean, "loginEmployeeCode");
	String priceType = (String) PropertyUtils.getProperty(otherBean,
		"priceType");
	String warehouseEmployee = (String) PropertyUtils.getProperty(
		otherBean, "warehouseEmployee");
	String warehouseManager = (String) PropertyUtils.getProperty(otherBean,
		"warehouseManager");
	String organizationCode = (String) PropertyUtils.getProperty(otherBean,
		"organizationCode");

	conditionMap.put("shopCode", shopCode);
	conditionMap.put("salesDate", salesDate);
	conditionMap.put("customsNo", customsNo);
	conditionMap.put("brandCode", brandCode);
	conditionMap.put("orderTypeCode", orderTypeCode);
	conditionMap.put("customerCode_var", customerCode_var);
	conditionMap.put("searchCustomerType", searchCustomerType);
	conditionMap.put("beforeChangeStatus", beforeChangeStatus);
	conditionMap.put("formAction", formAction);
	conditionMap.put("loginEmployeeCode", loginEmployeeCode);
	conditionMap.put("priceType", priceType);
	conditionMap.put("warehouseEmployee", warehouseEmployee);
	conditionMap.put("warehouseManager", warehouseManager);
	conditionMap.put("organizationCode", organizationCode);
	return conditionMap;
    }

    /**
     * 檢核銷貨主檔資料(SalesOrderHead)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSalesOrderHead(SoSalesOrderHead orderHead,
	    HashMap conditionMap, String programId, String identification,
	    List errorMsgs) {
	String tabName = "主檔資料頁籤";
	String posTabName = "POS資料頁籤";
	String dateType = "銷貨日期";
	try {
	    String brandCode = orderHead.getBrandCode();
	    String orderTypeCode = orderHead.getOrderTypeCode();
	    String customerCode = orderHead.getCustomerCode();
	    String superintendentCode = orderHead.getSuperintendentCode();
	    String defaultWarehouseCode = orderHead.getDefaultWarehouseCode();
	    String promotionCode = orderHead.getPromotionCode();
	    String priceType = (String) conditionMap.get("priceType");
	    String customerCode_var = (String) conditionMap
		    .get("customerCode_var");
	    String searchCustomerType = (String) conditionMap
		    .get("searchCustomerType");
	    String salesDate = DateUtils.format(orderHead.getSalesOrderDate(),
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String customerType = null;
	    String vipType = null;
	    String vipPromotionCode = null;
	    String customsNo = (String) conditionMap.get("customsNo");
	    	    BuBrand buBrand = buBrandService.findById(brandCode);

	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType,
		    orderHead.getSalesOrderDate(),orderHead.getSchedule());
	    if (StringUtils.hasText(customerCode_var)) {
		customerCode_var = customerCode_var.trim().toUpperCase();
		BuCustomerWithAddressView customerWithAddressView = buCustomerWithAddressViewService
			.findCustomerByType(brandCode, customerCode_var,
				searchCustomerType, null);// 依據品牌代號及客戶代號或身份ID，查詢啟用狀態之客戶通訊資料
		if (customerWithAddressView == null) {
		    orderHead.setCustomerCode(customerCode_var);
		    this.createProgramLog("查無" + tabName + "的客戶代號！",
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		} else {
		    customerCode = customerWithAddressView.getCustomerCode();
		    orderHead.setCustomerCode(customerCode);
		    customerType = customerWithAddressView
			    .getCustomerTypeCode();
		    vipType = customerWithAddressView.getVipTypeCode();
		    vipPromotionCode = customerWithAddressView
			    .getPromotionCode();
		    // 將查詢到的客戶資料，customerType、vipType放置conditionMap
		    conditionMap.put("customerType", customerType);
		    conditionMap.put("vipType", vipType);
		    if (POSTYPECODE.equals(orderTypeCode)) {
			conditionMap.put("vipPromotionCode", null);
		    } else {
			conditionMap.put("vipPromotionCode", vipPromotionCode);
		    }
		}
	    } else {
		orderHead.setCustomerCode(null);
	    }
	    if (!StringUtils.hasText(orderHead.getShopCode())) {
		this
			.createProgramLog("請選擇" + tabName + "的專櫃代號！",
				identification, errorMsgs, orderHead
					.getLastUpdatedBy());
	    }
	    if (StringUtils.hasText(superintendentCode)) {
		superintendentCode = superintendentCode.trim().toUpperCase();
		orderHead.setSuperintendentCode(superintendentCode);
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewService
			.findbyBrandCodeAndEmployeeCode(brandCode,
				superintendentCode);
		if (employeeWithAddressView == null) {
		    this.createProgramLog("查無" + tabName + "的訂單負責人！",
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		}
	    } else {
		orderHead.setSuperintendentCode(null);
	    }
	    if (orderHead.getScheduleShipDate() == null) {
		this
			.createProgramLog("請輸入" + tabName + "的預計出貨日期！",
				identification, errorMsgs, orderHead
					.getLastUpdatedBy());
	    }
	    if (!StringUtils.hasText(orderHead.getTaxType())) {
		this.createProgramLog("請選擇" + tabName + "的稅別！", identification,
			errorMsgs, orderHead.getLastUpdatedBy());
	    } else if (("1".equals(orderHead.getTaxType()) || "2"
		    .equals(orderHead.getTaxType()))
		    && (orderHead.getTaxRate() == null || orderHead
			    .getTaxRate() != 0D)) {
		this
			.createProgramLog(tabName + "的稅別為免稅或零稅時，其稅率應為0%！",
				identification, errorMsgs, orderHead
					.getLastUpdatedBy());
	    } else if ("3".equals(orderHead.getTaxType())
		    && (orderHead.getTaxRate() == null || orderHead
			    .getTaxRate() != 5D)) {
		this
			.createProgramLog(tabName + "的稅別為應稅時，其稅率應為5%！",
				identification, errorMsgs, orderHead
					.getLastUpdatedBy());
	    }

	    if (!StringUtils.hasText(defaultWarehouseCode)) {
		this.createProgramLog("請選擇" + tabName + "的庫別！", identification,
			errorMsgs, orderHead.getLastUpdatedBy());
	    } else if (!POSTYPECODE.equals(orderTypeCode)) {
		defaultWarehouseCode = defaultWarehouseCode.trim()
			.toUpperCase();
		orderHead.setDefaultWarehouseCode(defaultWarehouseCode);
		conditionMap.put("warehouseCode", defaultWarehouseCode);
		if (imItemPriceOnHandViewService
			.getWarehouseByCondition(conditionMap) == null) {
		    this.createProgramLog("查無" + tabName + "的庫別！",
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		}
	    }

	    // 活動代號檢核
	    if (StringUtils.hasText(promotionCode)) {
		promotionCode = promotionCode.trim().toUpperCase();
		orderHead.setPromotionCode(promotionCode);
		HashMap parameterMap = new HashMap();
		parameterMap.put("brandCode", brandCode);
		parameterMap.put("promotionCode", promotionCode);
		parameterMap.put("priceType", priceType);
		parameterMap.put("shopCode", orderHead.getShopCode());
		parameterMap.put("customerType", customerType);
		parameterMap.put("vipType", vipType);
		parameterMap.put("salesDate", salesDate);
		Object[] promotionInfo = imPromotionViewDAO
			.findPromotionCodeByCondition(parameterMap);
		if (promotionInfo == null || promotionInfo[3] == null
			|| promotionInfo[4] == null) {
		    this.createProgramLog("查無" + tabName + "的活動代號！",
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		}
	    }

	    if (orderHead.getDiscountRate() != null
		    && orderHead.getDiscountRate() < 0D) {
		this.createProgramLog(tabName + "的折扣比率不可小於0%！", identification,
			errorMsgs, orderHead.getLastUpdatedBy());
	    }

	    // 單別非SOP，將POS相關欄位清除
	    if (!POSTYPECODE.equals(orderTypeCode)) {
			orderHead.setPosMachineCode(null);
			orderHead.setCasherCode(null);
			orderHead.setDepartureDate(null);
			orderHead.setFlightNo(null);
			orderHead.setPassportNo(null);
			orderHead.setLadingNo(null);
			orderHead.setTransactionSeqNo(null);
			orderHead.setSalesInvoicePage(null);
			orderHead.setTransactionTime(null);
		} else {
			if ("2".equals(buBrand.getBuBranch().getBranchCode())
				&& !StringUtils.hasText(orderHead.getPosMachineCode())) {
			    this.createProgramLog("請選擇POS機台！", identification,
				    errorMsgs, orderHead.getLastUpdatedBy());
			}
			if (StringUtils.hasText(orderHead.getCasherCode())) {
			    orderHead.setCasherCode(orderHead.getCasherCode().trim()
				    .toUpperCase());
			    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewService
				    .findbyBrandCodeAndEmployeeCode(brandCode,
					    orderHead.getCasherCode());
			    if (employeeWithAddressView == null) {
				this.createProgramLog("查無" + posTabName + "的收銀員代號！",
					identification, errorMsgs, orderHead
						.getLastUpdatedBy());
			    }
			} else {
			    orderHead.setCasherCode(null);
			}
			if (StringUtils.hasText(orderHead.getFlightNo())
				&& !ValidateUtil.isUpperCaseOrNumber(orderHead
					.getFlightNo())) {
			    this.createProgramLog(posTabName + "的班機代碼必須為A~Z、0~9！",
				    identification, errorMsgs, orderHead
					    .getLastUpdatedBy());
			}
			if (StringUtils.hasText(orderHead.getPassportNo())
				&& !ValidateUtil.isUpperCaseOrNumber(orderHead
					.getPassportNo())) {
			    this.createProgramLog(posTabName + "的護照號碼必須為A~Z、0~9！",
				    identification, errorMsgs, orderHead
					    .getLastUpdatedBy());
			}
			if (StringUtils.hasText(orderHead.getLadingNo())
				&& !ValidateUtil.isUpperCaseOrNumber(orderHead
					.getLadingNo())) {
			    this.createProgramLog(posTabName + "的提貨單號必須為A~Z、0~9！",
				    identification, errorMsgs, orderHead
					    .getLastUpdatedBy());
			}
			if (StringUtils.hasText(orderHead.getTransactionSeqNo())
				&& !ValidateUtil.isUpperCaseOrNumber(orderHead
					.getTransactionSeqNo())) {
			    this.createProgramLog(posTabName + "的交易序號必須為A~Z、0~9！",
				    identification, errorMsgs, orderHead
					    .getLastUpdatedBy());
			}
			if ("2".equals(buBrand.getBuBranch().getBranchCode()) && !StringUtils.hasText(customsNo)) {
				this.createProgramLog("請輸入海關上傳單號！", identification, errorMsgs, orderHead.getLastUpdatedBy());
			}
		}
	} catch (Exception ex) {
	    this.createProgramLog("檢核銷貨單" + tabName + "時發生錯誤，原因："
		    + ex.getMessage(), identification, errorMsgs, orderHead
		    .getLastUpdatedBy());
		}
    }

    /**
     * 檢核銷貨明細資料(AJAX)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSOItems(SoSalesOrderHead orderHead, HashMap conditionMap,
	    String programId, String identification, List errorMsgs) {

	String tabName = "明細資料頁籤";
	String brandCode = orderHead.getBrandCode();
	try {
	    BuBranch buBranch = buBrandService.findBranchByBrandCode(brandCode);
	    String vipPromotionCode = (String) conditionMap
		    .get("vipPromotionCode");
	    List soSalesOrderItems = orderHead.getSoSalesOrderItems();
	    BuOrderTypeId buOrderTypeId = new BuOrderTypeId(brandCode,
		    orderHead.getOrderTypeCode());
	    BuOrderType buOrderType = buOrderTypeService
		    .findById(buOrderTypeId);
	    if (buOrderType == null) {
		throw new ValidationErrorException("查無"
			+ orderHead.getOrderTypeCode() + "之銷貨單號");
	    }

	    if (soSalesOrderItems != null && soSalesOrderItems.size() > 0) {
		int intactRecordCount = 0;
		for (int i = 0; i < soSalesOrderItems.size(); i++) {
		    try {
			SoSalesOrderItem salesOrderItem = (SoSalesOrderItem) soSalesOrderItems
				.get(i);
			if (!"1".equals(salesOrderItem.getIsDeleteRecord())) {
			    salesOrderItem
				    .setVipPromotionCode(vipPromotionCode);// 依據head的vipPromotionCode將item的替換掉
			    salesOrderItem.setWarehouseCode(orderHead
				    .getDefaultWarehouseCode());// 依據head的預設庫別將item的替換掉
			    salesOrderItem.setTaxType(orderHead.getTaxType());// 依據head的taxType將item的替換掉
			    salesOrderItem.setTaxRate(orderHead.getTaxRate());// 依據head的taxRate將item的替換掉
			    String itemCode = salesOrderItem.getItemCode();
			    String warehouseCode = salesOrderItem
				    .getWarehouseCode();
			    Double quantity = NumberUtils
				    .getDouble(salesOrderItem.getQuantity());
			    if (quantity != null && quantity != 0D) {
				quantity = CommonUtils.round(quantity, 2);
				salesOrderItem.setQuantity(quantity);
			    }
			    String promotionCode = salesOrderItem
				    .getPromotionCode();
			    if (!StringUtils.hasText(itemCode)) {
				throw new ValidationErrorException("請輸入"
					+ tabName + "中第" + (i + 1) + "項明細的品號！");
			    } else if (!StringUtils.hasText(warehouseCode)) {
				throw new ValidationErrorException("請輸入"
					+ tabName + "中第" + (i + 1) + "項明細的庫別！");
			    } else if (!ValidateUtil
				    .isEnglishAlphabetOrNumber(itemCode)) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1)
					+ "項明細的品號必須為A~Z、a~z、0~9！");
			    } else if (!ValidateUtil
				    .isEnglishAlphabetOrNumber(warehouseCode)) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1)
					+ "項明細的庫別必須為A~Z、a~z、0~9！");
			    }

			    Double discountRate = salesOrderItem.getDiscountRate();
			    if (discountRate == null) {
				discountRate = 100D;
				salesOrderItem.setDiscountRate(discountRate);
			    }
			    Double deductionAmount = salesOrderItem
				    .getDeductionAmount();
			    Double originalUnitPrice = salesOrderItem
				    .getOriginalUnitPrice();
			    conditionMap.put("itemCode", itemCode);
			    conditionMap.put("warehouseCode", warehouseCode);
			    conditionMap.put("quantity", quantity);
			    conditionMap.put("discountRate", discountRate);
			    conditionMap
				    .put("deductionAmount", deductionAmount);
			    conditionMap.put("promotionCode", promotionCode);
			    conditionMap.put("originalUnitPrice",
				    originalUnitPrice);
			    conditionMap.put("taxType", salesOrderItem
				    .getTaxType());
			    conditionMap.put("taxRate", salesOrderItem
				    .getTaxRate());
			    // 查詢後的銷售明細
			    SoSalesOrderItem soItemInfo = appGetSaleItemInfoService
				    .getSaleItemInfo(conditionMap,
					    setSoItemBean(conditionMap));
			    // copy查詢後的銷售明細
			    setSoItemInfo(salesOrderItem, soItemInfo);
			    salesOrderItem.setScheduleShipDate(orderHead
				    .getScheduleShipDate()); // 將head的預計出貨日放置Item中
			    if (soItemInfo.getItemCName() == null) {
				throw new NoSuchObjectException("查無" + tabName
					+ "中第" + (i + 1) + "項明細的品號！");
			    } else if ("Y"
				    .equals(soItemInfo.getIsServiceItem())
				    && salesOrderItem.getOriginalUnitPrice() == null) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1)
					+ "項明細的品號為服務性商品，請輸入單價！");
			    } else if (StringUtils.hasText(soItemInfo
				    .getPromotionCode())
				    && soItemInfo.getDiscountType() == null
				    && soItemInfo.getDiscount() == null) {
				throw new ValidationErrorException("查無"
					+ tabName + "中第" + (i + 1)
					+ "項明細的活動代號！");
			    }

			    // 數量不可以是零
			    if (quantity == null || quantity == 0D) {
				throw new ValidationErrorException("請輸入"
					+ tabName + "中第" + (i + 1) + "項明細的數量！");
			    } else if (!POSTYPECODE.equals(orderHead
				    .getOrderTypeCode())
				    && !"Y".equals(salesOrderItem
					    .getIsServiceItem())
				    && quantity != null
				    && NumberUtils.getDouble(salesOrderItem
					    .getCurrentOnHandQty()) < quantity) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1) + "項明細的數量不可大於庫存量！");
				// 只有T1的POS才能輸入負數
			    } else if (!(POSTYPECODE.equals(orderHead
				    .getOrderTypeCode()) && !"2"
				    .equals(buBranch.getBranchCode()))
				    && !"Y".equals(salesOrderItem
					    .getIsServiceItem())
				    && quantity != null
				    && quantity < 0D
				    && !"1".equals(buBranch.getBranchCode())) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1) + "項明細的數量不可小於零！");
			    } else if ("Y".equals(salesOrderItem
				    .getIsServiceItem())
				    && quantity != null
				    && quantity != 1D
				    && quantity != -1D) {
				throw new ValidationErrorException(tabName
					+ "中第" + (i + 1)
					+ "項明細的品號為服務性商品，數量請輸入1或-1！");
			    }

			    // 如果有輸入手錶序號
			    if (StringUtils.hasText(salesOrderItem
				    .getWatchSerialNo())) {
				ImItemSerial imItemSerial = (ImItemSerial) imItemSerialDAO
					.findFirstByProperty(
						"ImItemSerial",
						"and brandCode = ? and itemCode = ? and serial = ?",
						new Object[] {
							brandCode,
							salesOrderItem
								.getItemCode(),
							salesOrderItem
								.getWatchSerialNo() });
				if (null == imItemSerial
					|| null == imItemSerial.getIsUsed()
					|| "Y".equals(imItemSerial.getIsUsed()))
				    throw new ValidationErrorException(tabName
					    + "中第" + (i + 1)
					    + "項明細的手錶序號不存在或是為已使用中！");
			    }

			    // 批號不得為空值 2010.05.11批號空值就先塞預設值
			    if ("2".equals(buBranch.getBranchCode())
				    && null == salesOrderItem.getLotNo()) {
				// if(POSTYPECODE.equals(orderHead.getOrderTypeCode()))
				salesOrderItem.setLotNo(SystemConfig.LOT_NO);
				// else
				// throw new ValidationErrorException(tabName +
				// "中第" + (i + 1) + "項明細的批號為空值！");
			    }

			    if (!POSTYPECODE.equals(orderHead
				    .getOrderTypeCode())
				    && "2".equals(buBranch.getBranchCode())) {
				ImItem imItem = imItemDAO.findItem(brandCode,
					itemCode);
				// 稅別欄位如果有空值
				if (imItem.getIsTax() == null
					|| !imItem.getIsTax().equals(
						buOrderType.getTaxCode())) {
				    throw new ValidationErrorException(tabName
					    + "中第" + (i + 1) + "項明細的稅別不符！");
				}
			    }
			    intactRecordCount++;
			}
		    } catch (Exception ex) {
			this.createProgramLog(ex.getMessage(), identification,
				errorMsgs, orderHead.getLastUpdatedBy());
		    }
		}
		if (intactRecordCount == 0) {
		    this.createProgramLog(tabName + "中至少需輸入一筆完整的資料！",
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		}
	    } else {
		this.createProgramLog(tabName + "中至少需輸入一筆資料！", identification,
			errorMsgs, orderHead.getLastUpdatedBy());
	    }
	} catch (Exception ex) {
	    this.createProgramLog("檢核" + tabName + "時發生錯誤，原因："
		    + ex.getMessage(), identification, errorMsgs, orderHead
		    .getLastUpdatedBy());
	}
    }

    /**
     * 檢核銷貨付款資料(SoSalesOrderPayment)
     * 
     * @param orderHead
     * @param conditionMap
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkSalesOrderPayments(SoSalesOrderHead orderHead,
	    HashMap conditionMap, String programId, String identification,
	    List errorMsgs) {

	String tabName = "付款資料頁籤";
	try {
	    List soSalesOrderPayments = orderHead.getSoSalesOrderPayments();
	    for (int i = 0; i < soSalesOrderPayments.size(); i++) {
		try {
		    SoSalesOrderPayment salesOrderPayment = (SoSalesOrderPayment) soSalesOrderPayments
			    .get(i);
		    if (!"1".equals(salesOrderPayment.getIsDeleteRecord())) {
			String posPaymentType = salesOrderPayment
				.getPosPaymentType();
			Double localAmount = NumberUtils
				.getDouble(salesOrderPayment.getLocalAmount());
			Double discountRate = NumberUtils
				.getDouble(salesOrderPayment.getDiscountRate());
			String foreignCurrencyCode = salesOrderPayment
				.getForeignCurrencyCode();
			Double exchangeRate = NumberUtils
				.getDouble(salesOrderPayment.getExchangeRate());
			Double foreignAmount = NumberUtils
				.getDouble(salesOrderPayment.getForeignAmount());
			// 金額取整數
			if (localAmount != null && localAmount != 0D) {
			    salesOrderPayment.setLocalAmount(new Double(
				    localAmount.intValue()));
			    localAmount = salesOrderPayment.getLocalAmount();
			}
			// 如果原幣幣別未選取
			if (!StringUtils.hasText(foreignCurrencyCode)) {
			    throw new ValidationErrorException("請選擇" + tabName
				    + "中第" + (i + 1) + "項明細的原幣幣別！");
			} else if (!StringUtils.hasText(posPaymentType)) {
			    throw new ValidationErrorException("請選擇" + tabName
				    + "中第" + (i + 1) + "項明細的付款類型！");
			    // }else if(foreignAmount < 0D){
			    // throw new ValidationErrorException(tabName + "中第"
			    // + (i + 1) + "項明細的原幣金額不可為負數！");
			    // }else if(localAmount < 0D){
			    // throw new ValidationErrorException(tabName + "中第"
			    // + (i + 1) + "項明細的本幣金額不可為負數！");
			} else if ((localAmount != null && localAmount != 0D)
				&& (discountRate != null && discountRate != 0D)) {
			    throw new ValidationErrorException(tabName + "中第"
				    + (i + 1) + "項明細的金額與折扣率只可擇一輸入！");
			} else if (exchangeRate <= 0D) {
			    throw new ValidationErrorException(tabName + "中第"
				    + (i + 1) + "項的匯率必須為正數！");
			}

			if (localAmount == 0D
				&& (discountRate != null && discountRate != 0D)) {
			    salesOrderPayment.setLocalAmount(null);
			}
			if (discountRate == 0D
				&& (localAmount != null && localAmount != 0D)) {
			    salesOrderPayment.setDiscountRate(null);
			}
		    }
		} catch (Exception ex) {
		    this.createProgramLog(ex.getMessage() + ex.getMessage(),
			    identification, errorMsgs, orderHead
				    .getLastUpdatedBy());
		}
	    }
	} catch (Exception ex) {
	    this.createProgramLog("檢核" + tabName + "時發生錯誤，原因："
		    + ex.getMessage(), identification, errorMsgs, orderHead
		    .getLastUpdatedBy());
	}
    }

    /**
     * 更新銷售單主單及明細檔、預訂可用庫存、產生出貨單明細檔、並取單號
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSOToDelivery(Map parameterMap, List errorMsgs) throws FormException, Exception {
    log.info("executeStorage");
	HashMap resultMap = new HashMap();
	try {
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    // ============remove delete mark record=============
	    removeAJAXLine(salesOrderHeadPO);
	    
	    // ====================取得條件資料======================
	    HashMap conditionMap = getConditionData(parameterMap);
	    String loginEmployeeCode = (String) conditionMap.get("loginEmployeeCode");
	    String organizationCode = (String) conditionMap.get("organizationCode");
	    log.info("---loginEmployeeCode---"+loginEmployeeCode);
	    log.info("---organizationCode----"+organizationCode);
	    // 如果是T2就不要重排
	    if (!"T2".equals(salesOrderHeadPO.getBrandCode()))
		sortSalesOrderItem(salesOrderHeadPO);
	    List<SoSalesOrderItem> salesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();

	    Map countResultMap = countTotalAmount(salesOrderHeadPO);
	    Double lineTaxAmount = (Double) countResultMap.get("taxAmount");
	    Double actualTaxAmount = salesOrderHeadPO.getTaxAmount();
	    // 將差額補到最後一筆明細的稅額
	    if (salesOrderItems.size() > 0) {
		Double balanceAmt = actualTaxAmount - lineTaxAmount;
		SoSalesOrderItem salesOrderItem = salesOrderItems
			.get(salesOrderItems.size() - 1);
		Double lastItemTaxAmt = salesOrderItem.getTaxAmount();
		if (lastItemTaxAmt == null) {
		    lastItemTaxAmt = 0D;
		}
		salesOrderItem.setTaxAmount(lastItemTaxAmt + balanceAmt);
	    }
	  //2016.11.22 Maco 鐘錶額度-送出扣預算
	    // ===================扣預算========================================
	    /*
		if("SOW".equals(salesOrderHeadPO.getOrderTypeCode())&&
				"T3CO".equals(salesOrderHeadPO.getBrandCode())&&
					"SAVE".equals(salesOrderHeadPO.getOrderTypeCode())){
			BuCustomerId id = new BuCustomerId(salesOrderHeadPO.getCustomerCode(),salesOrderHeadPO.getBrandCode());
			BuCustomer buCustomer = buCustomerDAO.findById(id);
			Long balanceCredits = buCustomer.getStockCredits()+buCustomer.getAdjCredits()+buCustomer.getTotalUncommitCredits();
			Long useCredits = 500000L;
			if(balanceCredits>=useCredits)
			{
				salesOrderHeadPO.setUsedCredits(useCredits);
				salesOrderHeadPO.setUncommitCredits(useCredits);
				buCustomer.setTotalUncommitCredits(useCredits);
			}
			else{
				throw new Exception("信用額度不足，剩餘額度:"+balanceCredits);
			}
			
		}
	    */
	    salesOrderHeadPO.setStatus(OrderStatus.SIGNING);
	    String errorOrderNo = salesOrderHeadPO.getOrderNo();
	    // ===================扣庫存、轉出貨單========================================
	    Object[] objArray = null;
	    // 非T2扣庫存
	    if (!"T2".equals(salesOrderHeadPO.getBrandCode())) {
		objArray = bookAvailableQuantity(salesOrderHeadPO,
			organizationCode, loginEmployeeCode, errorOrderNo,
			errorMsgs);
		if (errorMsgs.size() > 0)
		    throw new FormException(MessageStatus.VALIDATION_FAILURE);
		for (Iterator iterator = salesOrderItems.iterator(); iterator
			.hasNext();) {
		    SoSalesOrderItem soSalesOrderItem = (SoSalesOrderItem) iterator.next();
		    log.info("1 soSalesOrderItem.index = " + soSalesOrderItem.getIndexNo());
		    if (StringUtils.hasText(soSalesOrderItem.getWatchSerialNo())) {
			ImItemSerial imItemSerial = (ImItemSerial) imItemSerialDAO
				.findFirstByProperty( "ImItemSerial", "and brandCode = ? and itemCode = ? and serial = ?",
					new Object[] { salesOrderHeadPO.getBrandCode(), soSalesOrderItem.getItemCode(),
						soSalesOrderItem.getWatchSerialNo() });
			imItemSerial.setIsUsed("Y");
			imItemSerial.setLastUpdateDate(new Date());
			imItemSerialDAO.update(imItemSerial);
		    }
		}

		// 取轉出貨單明細
		salesOrderToDelivery(salesOrderHeadPO, salesOrderItems,
			(HashMap) objArray[1], (String) objArray[0],
			loginEmployeeCode);
		if (POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode()))
		    mailToPOSAdministrator(salesOrderHeadPO, (List) objArray[2]);
		// T2扣庫存
	    } else {
		objArray = bookAvailableQuantityForT2(salesOrderHeadPO,
			organizationCode, loginEmployeeCode, errorMsgs);
		if (errorMsgs.size() > 0)
		    throw new FormException(MessageStatus.VALIDATION_FAILURE);
		// 取轉出貨單明細
		salesOrderToDeliveryForT2(salesOrderHeadPO, loginEmployeeCode);
		// 保稅單才有產生發票
		String taxCode = buOrderTypeService.getTaxCode(salesOrderHeadPO.getBrandCode(), salesOrderHeadPO.getOrderTypeCode());
		if (!POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode()) && "F".equals(taxCode))
		    produceFiInvoice(salesOrderHeadPO);
	    }
	    
	  	//for 儲位用
		if(imStorageAction.isStorageExecute(salesOrderHeadPO)){
			//異動儲位庫存
			imStorageService.updateStorageOnHandBySource(salesOrderHeadPO, OrderStatus.FINISH, PROGRAM_ID, null, false);
		}
	    
	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO, loginEmployeeCode);
	    
	    
	    resultMap.put("entityBean", salesOrderHeadPO);
	    resultMap.put("resultMsg", resultMsg);
	    return resultMap;
	} catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 把標記為刪除的資料刪除
     * 
     * @param salesOrderHead
     */
    private void removeAJAXLine(SoSalesOrderHead salesOrderHead) {
	List<SoSalesOrderItem> salesOrderItems = salesOrderHead
		.getSoSalesOrderItems();
	List<SoSalesOrderPayment> salesOrderPayments = salesOrderHead
		.getSoSalesOrderPayments();
	if (salesOrderItems != null && salesOrderItems.size() > 0) {
	    for (int i = salesOrderItems.size() - 1; i >= 0; i--) {
		SoSalesOrderItem salesOrderItem = salesOrderItems.get(i);
		if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderItem
			.getIsDeleteRecord())) {
		    salesOrderItems.remove(salesOrderItem);
		}
	    }
	}
	if (salesOrderPayments != null && salesOrderPayments.size() > 0) {
	    for (int i = salesOrderPayments.size() - 1; i >= 0; i--) {
		SoSalesOrderPayment salesOrderPayment = salesOrderPayments
			.get(i);
		if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(salesOrderPayment
			.getIsDeleteRecord())) {
		    salesOrderItems.remove(salesOrderPayment);
		}
	    }
	}
    }

    private void sortSalesOrderItem(SoSalesOrderHead salesOrderHead)
	    throws Exception {
	// 只要不是T2，就需依品號排序，所以第一步是清空indexNo, 填入indexNo
	List<SoSalesOrderItem> salesOrderItems = soSalesOrderItemDAO
		.findByProperty("SoSalesOrderItem", "",
			"and soSalesOrderHead.headId = ? ",
			new Object[] { salesOrderHead.getHeadId() },
			"order by itemCode ");
	Long i = 1L;
	for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
	    salesOrderItem.setIndexNo(i);
	    i++;
	}
	salesOrderHead.setSoSalesOrderItems(salesOrderItems);
	soSalesOrderHeadDAO.update(salesOrderHead);
	// 以下為原code
	// List<SoSalesOrderItem> salesOrderItems = soSalesOrderItemDAO
	// .findByHeadId(salesOrderHead.getHeadId());
	// salesOrderItems = StringTools.setBeanValue(salesOrderItems,
	// "indexNo",
	// null);

    }

    private Object[] bookAvailableQuantity(SoSalesOrderHead soSalesOrderHead,
	    String organizationCode, String loginUser, String errorOrderNo,
	    List errorMsgs) throws NoSuchDataException, FormException {

	String identification = MessageStatus.getIdentification(
		soSalesOrderHead.getBrandCode(), soSalesOrderHead
			.getOrderTypeCode(), errorOrderNo);
	BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
	buOrderTypeId.setOrderTypeCode(soSalesOrderHead.getOrderTypeCode());
	buOrderTypeId.setBrandCode(soSalesOrderHead.getBrandCode());

	BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
	if (buOrderType == null) {
	    throw new NoSuchDataException("依據單據代號："
		    + soSalesOrderHead.getOrderTypeCode() + ",品牌代號："
		    + soSalesOrderHead.getBrandCode() + "查無相關單別資料！");
	}
	String stockControl = buOrderType.getStockControl(); // 庫存控制方法
	HashMap onHandsMap = new HashMap(); // 出貨單派發批號之來源
	HashMap isServiceItemMap = new HashMap(); // 是否為服務性商品集合
	HashMap isComposeItemMap = new HashMap(); // 是否為組合性商品集合

	List mailContents = new ArrayList(0);

	Set entrySet = aggregateOrderItemQuantity(soSalesOrderHead
		.getSoSalesOrderItems(), isServiceItemMap, isComposeItemMap);
	Iterator it = entrySet.iterator();
	// Long i = 1L;
	while (it.hasNext()) {
	    try {
		Map.Entry entry = (Map.Entry) it.next();
		String[] keyArray = StringUtils.delimitedListToStringArray(
			(String) entry.getKey(), "#");
		List<ImOnHand> lockedOnHands = null;
		try {
		    lockedOnHands = imOnHandDAO.getLockedOnHand(
			    organizationCode, keyArray[0], keyArray[1], null);
		} catch (CannotAcquireLockException cale) {
		    throw new FormException("品號：" + keyArray[0] + ",庫別："
			    + keyArray[1] + "已鎖定，請稍後再試！");
		}
		// 非服務性商品且數量大於零時檢核庫存量是否足夠
		if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))
			&& (Double) entry.getValue() > 0D) {
		    Double availableQuantity = imOnHandDAO
			    .getCurrentStockOnHandQty(organizationCode,
				    keyArray[0], keyArray[1]);
		    if (availableQuantity == null) {
			// 非SOP單需檢核是否有庫存
			if (!POSTYPECODE.equals(soSalesOrderHead
				.getOrderTypeCode())) {
			    throw new ValidationErrorException("查無品號："
				    + keyArray[0] + ",庫別：" + keyArray[1]
				    + "的庫存量！");
			}
		    } else if ((Double) entry.getValue() > availableQuantity) {
			if (!POSTYPECODE.equals(soSalesOrderHead
				.getOrderTypeCode())) {
			    throw new ValidationErrorException("品號："
				    + keyArray[0] + ",庫別：" + keyArray[1]
				    + "可用庫存量不足！");
			} else {
			    // POS庫存不足不丟出例外，改以mail通知
			    mailContents.add(keyArray[0] + "#" + keyArray[1]
				    + "#" + (Double) entry.getValue());
			}
		    }
		}

		// ==========非服務性商品預訂可用庫存==========
		if (lockedOnHands != null && lockedOnHands.size() > 0) {
		    getRequiredPropertyConvertToMap(lockedOnHands, onHandsMap); // 從ImOnHand資料中取得必要資訊，放置到HashMap
		    if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
			if (!POSTYPECODE.equals(soSalesOrderHead
				.getOrderTypeCode())) {
			    imOnHandDAO.bookAvailableQuantity(lockedOnHands,
				    (Double) entry.getValue(), stockControl,
				    loginUser);
			} else {
			    imOnHandDAO.bookQuantity(lockedOnHands,
				    (Double) entry.getValue(), stockControl,
				    loginUser);
			}
		    }
		} else {
		    if (!POSTYPECODE
			    .equals(soSalesOrderHead.getOrderTypeCode())) {
			throw new ValidationErrorException("查無品號："
				+ keyArray[0] + ",庫別：" + keyArray[1] + "的庫存資料！");
		    } else {
			// ==========================SOP單查無onHand時新增一筆====================================
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
				.getBuCommonPhraseLine("SystemConfig",
					"DefaultLotNo");
			String defaultLotNo = buCommonPhraseLine != null ? buCommonPhraseLine
				.getName()
				: "000000000000";
			ImOnHandId id = new ImOnHandId();
			ImOnHand newOnHand = new ImOnHand();
			id.setOrganizationCode(organizationCode);
			id.setItemCode(keyArray[0]);
			id.setWarehouseCode(keyArray[1]);
			id.setLotNo(defaultLotNo);
			newOnHand.setId(id);
			newOnHand.setBrandCode(soSalesOrderHead.getBrandCode());
			newOnHand.setStockOnHandQty(0D);
			if (!"Y".equals((String) isServiceItemMap
				.get(keyArray[0]))) {
			    newOnHand.setOutUncommitQty((Double) entry
				    .getValue());
			} else {
			    newOnHand.setOutUncommitQty(0D);
			}
			newOnHand.setInUncommitQty(0D);
			newOnHand.setMoveUncommitQty(0D);
			newOnHand.setOtherUncommitQty(0D);
			newOnHand.setCreatedBy(loginUser);
			newOnHand.setCreationDate(new Date());
			newOnHand.setLastUpdatedBy(loginUser);
			newOnHand.setLastUpdateDate(new Date());
			imOnHandDAO.save(newOnHand);
			lockedOnHands = new ArrayList();
			lockedOnHands.add(newOnHand);
			getRequiredPropertyConvertToMap(lockedOnHands,
				onHandsMap);
		    }
		}
	    } catch (Exception ex) {
		this.createProgramLog(ex.getMessage(), identification,
			errorMsgs, soSalesOrderHead.getLastUpdatedBy());
	    }
	}
	return new Object[] { stockControl, onHandsMap, mailContents };
    }

    /**
     * 將相同itemCode、warehouseCode的數量合計
     * 
     * @param organization
     * @param originalOrderItems
     * @param isServiceItemMap
     * @param isComposeItemMap
     * @return Set
     */
    private Set aggregateOrderItemQuantity(
	    List<SoSalesOrderItem> originalOrderItems,
	    HashMap isServiceItemMap, HashMap isComposeItemMap) {
	StringBuffer key = new StringBuffer();
	HashMap map = new HashMap();
	for (SoSalesOrderItem originalOrderItem : originalOrderItems) {
	    Double quantity = originalOrderItem.getQuantity();
	    String itemCode = originalOrderItem.getItemCode();
	    // 數量不可為null
	    if (quantity == null) {
		quantity = 0D;
		originalOrderItem.setQuantity(quantity);
	    }
	    key.delete(0, key.length());
	    key.append(itemCode + "#");
	    key.append(originalOrderItem.getWarehouseCode());

	    if (map.get(key.toString()) == null) {
		map.put(key.toString(), quantity);
	    } else {
		map.put(key.toString(), quantity
			+ ((Double) map.get(key.toString())));
	    }
	    // 將itemCode是否為服務性商品、組合性商品放入map中
	    if (isServiceItemMap.get(itemCode) == null)
		isServiceItemMap.put(itemCode, originalOrderItem
			.getIsServiceItem());
	    if (isComposeItemMap.get(itemCode) == null)
		isComposeItemMap.put(itemCode, originalOrderItem
			.getIsComposeItem());
	}
	return map.entrySet();
    }

    /**
     * 從ImOnHand資料中取得必要資訊，放置到HashMap
     * 
     * @param lockedOnHands
     * @param onHandsMap
     */
    private void getRequiredPropertyConvertToMap(List<ImOnHand> lockedOnHands,
	    HashMap onHandsMap) {

	StringBuffer key = new StringBuffer();
	StringBuffer subKey = new StringBuffer();
	TreeMap lotNoMap = new TreeMap();
	for (ImOnHand onHand : lockedOnHands) {
	    Double availableQuantity = onHand.getStockOnHandQty()
		    - onHand.getOutUncommitQty() + onHand.getInUncommitQty()
		    + onHand.getMoveUncommitQty()
		    + onHand.getOtherUncommitQty();
	    key.delete(0, key.length());
	    subKey.delete(0, subKey.length());
	    key.append(onHand.getId().getItemCode());
	    key.append(onHand.getId().getWarehouseCode());
	    subKey.append(onHand.getId().getLotNo());
	    lotNoMap.put(subKey.toString(), availableQuantity);
	}
	onHandsMap.put(key.toString(), lotNoMap);
    }

    /**
     * 將SalesOrderItem轉換成DeliveryLine(派發批號至DeliveryLine)
     * 
     * @param salesOrderItems
     * @param onHandsMap
     * @param stockControl
     * @param loginUser
     */
    private void salesOrderToDelivery(SoSalesOrderHead salesOrderHead,
	    List<SoSalesOrderItem> salesOrderItems, HashMap onHandsMap,
	    String stockControl, String loginUser) {

	Long salesOrderHeadId = salesOrderHead.getHeadId();
	StringBuffer key = new StringBuffer();
	for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
	    log.info("2 soSalesOrderItem.index = "
		    + salesOrderItem.getIndexNo());
	    Double orderQuantity = salesOrderItem.getQuantity();
	    key.delete(0, key.length());
	    key.append(salesOrderItem.getItemCode());
	    key.append(salesOrderItem.getWarehouseCode());
	    TreeMap lotNoMap = (TreeMap) onHandsMap.get(key.toString());
	    Set set = lotNoMap.entrySet();
	    Map.Entry[] lotNoEntry = (Map.Entry[]) set
		    .toArray(new Map.Entry[set.size()]);

	    if ("FIFO".equals(stockControl)) {
		for (Map.Entry entry : lotNoEntry) {
		    String lotNo = (String) entry.getKey();
		    if (!POSTYPECODE.equals(salesOrderHead.getOrderTypeCode())) {
			if (!"Y".equals(salesOrderItem.getIsServiceItem())) {
			    Double availableQuantity = (Double) entry
				    .getValue();
			    if (availableQuantity > 0D && orderQuantity > 0D) {
				if (orderQuantity > availableQuantity) {
				    imDeliveryLineDAO
					    .save(copySalesOrderItemToDeliveryLine(
						    salesOrderHeadId,
						    salesOrderItem, lotNo,
						    availableQuantity,
						    loginUser));
				    entry.setValue(0D);
				    orderQuantity -= availableQuantity;
				} else if (orderQuantity <= availableQuantity) {
				    imDeliveryLineDAO
					    .save(copySalesOrderItemToDeliveryLine(
						    salesOrderHeadId,
						    salesOrderItem, lotNo,
						    orderQuantity, loginUser));
				    entry.setValue(availableQuantity
					    - orderQuantity);
				    break;
				}
			    }
			} else {
			    imDeliveryLineDAO
				    .save(copySalesOrderItemToDeliveryLine(
					    salesOrderHeadId, salesOrderItem,
					    lotNo, orderQuantity, loginUser));
			    break;
			}
		    } else {
			imDeliveryLineDAO
				.save(copySalesOrderItemToDeliveryLine(
					salesOrderHeadId, salesOrderItem,
					lotNo, orderQuantity, loginUser));
			break;
		    }
		}
	    } else {
		for (int i = lotNoEntry.length - 1; i >= 0; i--) {
		    Map.Entry entry = (Map.Entry) lotNoEntry[i];
		    String lotNo = (String) entry.getKey();
		    if (!POSTYPECODE.equals(salesOrderHead.getOrderTypeCode())) {
			if (!"Y".equals(salesOrderItem.getIsServiceItem())) {
			    Double availableQuantity = (Double) entry
				    .getValue();
			    if (availableQuantity > 0D && orderQuantity > 0D) {
				if (orderQuantity > availableQuantity) {
				    imDeliveryLineDAO
					    .save(copySalesOrderItemToDeliveryLine(
						    salesOrderHeadId,
						    salesOrderItem, lotNo,
						    availableQuantity,
						    loginUser));
				    entry.setValue(0D);
				    orderQuantity -= availableQuantity;
				} else if (orderQuantity <= availableQuantity) {
				    imDeliveryLineDAO
					    .save(copySalesOrderItemToDeliveryLine(
						    salesOrderHeadId,
						    salesOrderItem, lotNo,
						    orderQuantity, loginUser));
				    entry.setValue(availableQuantity
					    - orderQuantity);
				    break;
				}
			    }
			} else {
			    imDeliveryLineDAO
				    .save(copySalesOrderItemToDeliveryLine(
					    salesOrderHeadId, salesOrderItem,
					    lotNo, orderQuantity, loginUser));
			    break;
			}
		    } else {
			imDeliveryLineDAO
				.save(copySalesOrderItemToDeliveryLine(
					salesOrderHeadId, salesOrderItem,
					lotNo, orderQuantity, loginUser));
			break;
		    }
		}
	    }
	}
    }

    /**
     * 將SalesOrderItem內容複製到ImDeliveryLine
     * 
     * @param salesOrderItem
     * @param lotNo
     * @param loginUser
     * @return ImDeliveryLine
     */
    private ImDeliveryLine copySalesOrderItemToDeliveryLine(
	    Long salesOrderHeadId, SoSalesOrderItem salesOrderItem,
	    String lotNo, Double quantity, String loginUser) {

	ImDeliveryLine deliveryLine = new ImDeliveryLine();
	BeanUtils.copyProperties(salesOrderItem, deliveryLine);
	deliveryLine.setSalesOrderId(salesOrderHeadId);
	deliveryLine.setLotNo(lotNo);
	deliveryLine.setSalesQuantity(quantity);
	deliveryLine.setShipQuantity(quantity);
	deliveryLine
		.setOriginalWarehouseCode(salesOrderItem.getWarehouseCode());
	deliveryLine.setOriginalLotNo(lotNo);
	deliveryLine.setOriginalSalesQuantity(quantity);
	deliveryLine.setOriginalSalesAmount(salesOrderItem
		.getOriginalUnitPrice()
		* quantity);
	deliveryLine.setOriginalOnHandQty(salesOrderItem.getCurrentOnHandQty());// 原庫存量
	deliveryLine.setActualSalesAmount(salesOrderItem.getActualUnitPrice()
		* quantity);
	// deliveryLine.setTaxAmount(calculateTaxAmount(deliveryLine.getTaxType(),
	// deliveryLine.getTaxRate(), deliveryLine.getActualSalesAmount()));
	deliveryLine.setStatus(OrderStatus.SAVE);
	UserUtils.setOpUserAndDate(deliveryLine, loginUser);

	return deliveryLine;
    }

    /**
     * 寄發E-mail至POS管理人員
     * 
     * @param salesOrderHead
     * @param mailContents
     */
    private void mailToPOSAdministrator(SoSalesOrderHead salesOrderHead,
	    List mailContents) {
	try {
	    if (mailContents != null && mailContents.size() > 0) {
		String brandCode = salesOrderHead.getBrandCode();
		String orderTypeCode = salesOrderHead.getOrderTypeCode();
		String orderNo = salesOrderHead.getOrderNo();
		StringBuffer display = new StringBuffer();
		Map root = new HashMap();
		for (int i = 0; i < mailContents.size(); i++) {
		    String content = (String) mailContents.get(i);
		    String[] actualInfoArray = StringTools.StringToken(content,
			    "#");
		    display.append("品牌代號：" + brandCode);
		    display.append("、單別：" + orderTypeCode);
		    display.append("、單號：" + orderNo);
		    display.append("，扣除品號：" + actualInfoArray[0]);
		    display.append("、倉儲代號：" + actualInfoArray[1]);
		    display.append("、數量：" + actualInfoArray[2]);
		    display.append("時發生庫存不足情形！<br>");
		}
		root.put("display", display.toString());
		String posAdministrator = SystemConfig
			.getSystemConfigPro(T1_POS_ADMIN);
		String[] posAdministratorArray = null;
		if (posAdministrator == null) {
		    throw new ValidationErrorException("無法取得POS管理人員電子郵件信箱資訊！");
		} else {
		    posAdministratorArray = StringTools.StringToken(
			    posAdministrator, ";");
		    if (posAdministratorArray == null) {
			throw new ValidationErrorException(
				"無法拆解POS管理人員電子郵件信箱資訊！");
		    } else {
			List mailAddress = new ArrayList(0);
			for (int j = 0; j < posAdministratorArray.length; j++) {
			    mailAddress.add(posAdministratorArray[j]);
			}
			MailUtils.sendMail("品牌代號：" + brandCode + "、單別："
				+ orderTypeCode + "、單號：" + orderNo
				+ "扣庫存量時發生不足情形！", MAIL_SKIN, root, mailAddress);
		    }
		}
	    }
	} catch (Exception ex) {
	    log.error("寄發E-mail至POS管理人員時發生錯誤！原因：" + ex.toString());
	}
    }

    /**
     * 寄發E-mail至POS管理人員(T2)
     * 
     * @param parameterMap
     * @param mailContents
     */
    public void mailToPOSAdministratorForT2(HashMap parameterMap,
	    List mailContents) {
	try {
	    if (mailContents != null && mailContents.size() > 0) {
		String brandCode = (String) parameterMap.get("brandCode");
		String shopCode = (String) parameterMap.get("actualShopCode");
		String posMachineCode = (String) parameterMap
			.get("posMachineCode");
		String transactionDate = (String) parameterMap
			.get("transactionDate");
		StringBuffer display = new StringBuffer();
		Map root = new HashMap();
		for (int i = 0; i < mailContents.size(); i++) {
		    String content = (String) mailContents.get(i);
		    display.append(content + "<br>");
		}
		root.put("display", display.toString());
		String posAdministrator = SystemConfig
			.getSystemConfigPro(T2_POS_ADMIN);
		String[] posAdministratorArray = null;
		if (posAdministrator == null) {
		    throw new ValidationErrorException("無法取得POS管理人員電子郵件信箱資訊！");
		} else {
		    posAdministratorArray = StringTools.StringToken(
			    posAdministrator, ";");
		    if (posAdministratorArray == null) {
			throw new ValidationErrorException(
				"無法拆解POS管理人員電子郵件信箱資訊！");
		    } else {
			List mailAddress = new ArrayList(0);
			for (int j = 0; j < posAdministratorArray.length; j++) {
			    mailAddress.add(posAdministratorArray[j]);
			}
			MailUtils.sendMail("品牌(" + brandCode + ")、銷貨日期("
				+ transactionDate + ")、專櫃(" + shopCode + ")、"
				+ "機台(" + posMachineCode + ")待確認的POS銷貨清單！",
				MAIL_SKIN, root, mailAddress);
		    }
		}
	    }
	} catch (Exception ex) {
	    log.error("寄發E-mail至POS管理人員時發生錯誤！原因：" + ex.toString());
	}
    }

    
    public Set[] bookAvailableQuantityForT2(SoSalesOrderHead soSalesOrderHead,
    	    String organizationCode, String loginUser, List errorMsgs) throws FormException, Exception{
    	Set[] aggregateResult = bookAvailableQuantityForT2(soSalesOrderHead,organizationCode,
    			loginUser,errorMsgs , false);
    	return aggregateResult;
    }
    
    /**
     * 預扣報單及實體庫別庫存量(T2)
     * 
     * @param soSalesOrderHead
     * @param organizationCode
     * @param loginUser
     * @param errorMsgs
     * @return Set[]
     * @throws FormException
     * @throws Exception
     */
    public Set[] bookAvailableQuantityForT2(SoSalesOrderHead soSalesOrderHead,
	    String organizationCode, String loginUser, List errorMsgs,boolean isResend)
	    throws FormException, Exception {
    log.info("kkkk---");
    log.info("kkkk---brandCode---"+soSalesOrderHead.getBrandCode());
    log.info("kkkk---User---"+soSalesOrderHead.getLastUpdatedBy());
    
	String errorMsg = null;
	String brandCode = soSalesOrderHead.getBrandCode();
	String orderTypeCode = soSalesOrderHead.getOrderTypeCode();
	String identification = MessageStatus.getIdentification(brandCode,
		orderTypeCode, soSalesOrderHead.getOrderNo());
	
	String transactionType = "OUT";
	boolean isClose = false;
	boolean isRevert = false;
	boolean allowImStockMinus = false;
	boolean allowCmStockMinus = false;
	String User = soSalesOrderHead.getLastUpdatedBy();
	
	ImMovementHead imMovementHead = null;
	
	updateOnhand upOnhand = new updateOnhand();
	
	HashMap isServiceItemMap = new HashMap(); // 是否為服務性商品集合
	HashMap isComposeItemMap = new HashMap(); // 是否為組合性商品集合
	HashMap allowWholeSale = new HashMap(); // 是否為組合性商品集合
	// ==============相同的商品、庫別、批號產生產生集合、相同的報單號碼、報單項次、商品、關別集合============
	
	Map map = new HashMap();
	Map cmMap = new HashMap();
	
	Set[] aggregateResult = aggregateOrderItemsQtyForT2(soSalesOrderHead,
		isServiceItemMap, isComposeItemMap, allowWholeSale);
	log.info("isServiceItemMap------"+isServiceItemMap);
	log.info("cmMap-----"+cmMap);
	log.info("aggregateResult-----"+aggregateResult[1]);
	
	/*upOnhand.updateOnhandData(map, cmMap, transactionType, brandCode, allowImStockMinus
				, allowCmStockMinus, organizationCode, isClose, isRevert, User, identification, errorMsgs, isServiceItemMap, allowWholeSale, soSalesOrderHead, aggregateResult );*/
	upOnhand.updateOnhandData2(map,cmMap,transactionType ,brandCode,allowImStockMinus , allowCmStockMinus, organizationCode,isClose
    		,isRevert,User, identification,errorMsgs, imMovementHead, isServiceItemMap,allowWholeSale,soSalesOrderHead, aggregateResult,isResend);
	/*
	Iterator it = aggregateResult[0].iterator(); // ImOnHand扣庫存用
	Iterator cmIt = aggregateResult[1].iterator(); // CmOnHand扣庫存用
	
	// ======================================預扣報單庫存量=======================================================
	while (cmIt.hasNext()) {
	    try {
		Map.Entry cmEntry = (Map.Entry) cmIt.next();		
		Double outUnCommitQty = (Double) cmEntry.getValue();		
		String[] cmkeyArray = StringUtils.delimitedListToStringArray(
			(String) cmEntry.getKey(), "{$}");
		if (!StringUtils.hasText(cmkeyArray[0])
			|| NumberUtils.getLong((cmkeyArray[1])) == 0
			|| !StringUtils.hasText(cmkeyArray[2])
			|| !StringUtils.hasText(cmkeyArray[3]))
		    throw new Exception("品牌(" + brandCode + ")、報關單號("
			    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
			    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
			    + cmkeyArray[3] + ") 其中有一項並未填寫");
		if (!"Y".equals((String) isServiceItemMap.get(cmkeyArray[2]))) {
		    cmDeclarationOnHandDAO.updateOutUncommitQty(cmkeyArray[0],
			    NumberUtils.getLong(cmkeyArray[1]), cmkeyArray[2],
			    cmkeyArray[3], brandCode, outUnCommitQty,
			    loginUser, (String) allowWholeSale
				    .get(cmkeyArray[2]));
		}
	    } catch (Exception ex) {
		errorMsg = "預扣 " + identification + " 的報單庫存量時發生錯誤，原因： ";
		log.error(errorMsg + ex.toString());
		siProgramLogAction.createProgramLog(PROGRAM_ID,
			MessageStatus.LOG_ERROR, identification, errorMsg
				+ ex.getMessage(), loginUser);
		errorMsgs.add(errorMsg);
	    }
	}
	// ======================================預扣實體庫別庫存量=======================================================
	while (it.hasNext()) {
	    try {
		Map.Entry entry = (Map.Entry) it.next();
		// Double outUnCommitQty = (Double) entry.getValue();
		String[] keyArray = StringUtils.delimitedListToStringArray(
			(String) entry.getKey(), "{$}");
		if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
		    List<ImOnHand> lockedOnHands = null;
		    try {
			lockedOnHands = imOnHandDAO.getLockedOnHand(
				organizationCode, keyArray[0], keyArray[1],
				keyArray[2], brandCode);
		    } catch (CannotAcquireLockException cale) {
			throw new FormException("品牌(" + brandCode + ")、品號("
				+ keyArray[0] + ")、庫別(" + keyArray[1] + ")、批號("
				+ keyArray[2] + ")已鎖定，請稍後再試！");
		    }
		    if (lockedOnHands != null && lockedOnHands.size() > 0) {
			ImOnHand onHandPO = (ImOnHand) lockedOnHands.get(0);
			Double availableQuantity = onHandPO.getStockOnHandQty()
				- onHandPO.getOutUncommitQty()
				+ onHandPO.getInUncommitQty()
				+ onHandPO.getMoveUncommitQty()
				+ onHandPO.getOtherUncommitQty();
			// 如果數量超過庫存 且不是POS以及允許全賣出
			if ((Double) entry.getValue() > availableQuantity
				&& (!(POSTYPECODE.equals(soSalesOrderHead
					.getOrderTypeCode())))) {
			    throw new ValidationErrorException("品牌("
				    + brandCode + ")、品號(" + keyArray[0]
				    + ")、庫別(" + keyArray[1] + ")、批號("
				    + keyArray[2] + ")可用庫存量不足！");
			} else {
			    if (!POSTYPECODE.equals(soSalesOrderHead
				    .getOrderTypeCode())) {
				imOnHandDAO.bookAvailableQuantity(
					lockedOnHands, (Double) entry
						.getValue(), "FIFO", loginUser);
			    } else {
				imOnHandDAO.bookQuantity(lockedOnHands,
					(Double) entry.getValue(), "FIFO",
					loginUser);
			    }
			}
		    } else {
			// 如果是T2POS 且 不允許全賣出
			if (!(POSTYPECODE.equals(soSalesOrderHead
				.getOrderTypeCode()))) {
			    throw new ValidationErrorException("查無品牌("
				    + brandCode + ")、品號(" + keyArray[0]
				    + ")、庫別(" + keyArray[1] + ")、批號("
				    + keyArray[2] + ")的庫存資料！");
			} else {
			    // ==========================SOP單查無onHand時新增一筆====================================
			    ImOnHandId id = new ImOnHandId();
			    ImOnHand newOnHand = new ImOnHand();
			    id.setOrganizationCode(organizationCode);
			    id.setItemCode(keyArray[0]);
			    id.setWarehouseCode(keyArray[1]);
			    id.setLotNo(keyArray[2]);
			    newOnHand.setId(id);
			    newOnHand.setBrandCode(brandCode);
			    newOnHand.setStockOnHandQty(0D);
			    newOnHand.setOutUncommitQty((Double) entry
				    .getValue());
			    newOnHand.setInUncommitQty(0D);
			    newOnHand.setMoveUncommitQty(0D);
			    newOnHand.setOtherUncommitQty(0D);
			    newOnHand.setCreatedBy(loginUser);
			    newOnHand.setCreationDate(new Date());
			    newOnHand.setLastUpdatedBy(loginUser);
			    newOnHand.setLastUpdateDate(new Date());
			    imOnHandDAO.save(newOnHand);
			}
		    }
		}
	    } catch (Exception ex) {
		errorMsg = "預扣 " + identification + " 的庫存量時發生錯誤，原因： ";
		log.error(errorMsg + ex.toString());
		siProgramLogAction.createProgramLog(PROGRAM_ID,
			MessageStatus.LOG_ERROR, identification, errorMsg
				+ ex.getMessage(), loginUser);
		errorMsgs.add(errorMsg);
	    }
	}
	*/
	return aggregateResult;
    }
    

    /**
     * 1.將相同的商品、庫別、批號集合 2.將相同的報單號碼、報單項次、商品、關別集合
     * 
     * @param salesOrderHead
     * @param isServiceItemMap
     * @param isComposeItemMap
     * @return Set[]
     */
    private Set[] aggregateOrderItemsQtyForT2(SoSalesOrderHead salesOrderHead,
	    HashMap isServiceItemMap, HashMap isComposeItemMap,
	    HashMap allowWholeSale) throws FormException, Exception {

	String brandCode = salesOrderHead.getBrandCode();
	List<SoSalesOrderItem> originalOrderItems = salesOrderHead
		.getSoSalesOrderItems();
	StringBuffer key = new StringBuffer();
	StringBuffer cmKey = new StringBuffer();
	HashMap map = new HashMap();
	HashMap cmMap = new HashMap();
	for (SoSalesOrderItem originalOrderItem : originalOrderItems) {
	    String itemCode = originalOrderItem.getItemCode();
	    String warehouseCode = originalOrderItem.getWarehouseCode();
	    String lotNo = originalOrderItem.getLotNo();
	    Double quantity = originalOrderItem.getQuantity();
	    String declNo = originalOrderItem.getImportDeclNo(); // 報單號碼
	    Long declSeq = originalOrderItem.getImportDeclSeq(); // 報單項次
	    String itemTaxCode = originalOrderItem.getIsTax();
	    String customsWarehouseCode = null;
	    // 數量不可為null
	    if (quantity == null) {
		quantity = 0D;
		originalOrderItem.setQuantity(quantity);
	    }
	    // 保稅商品集合
	    if ("F".equals(itemTaxCode)) {
		ImWarehouse warehousePO = imWarehouseService
			.findByBrandCodeAndWarehouseCode(brandCode,
				warehouseCode, null);
		if (warehousePO == null) {
		    throw new NoSuchObjectException("依據品牌(" + brandCode
			    + ")、庫別(" + warehouseCode + ")查無庫別相關資料！");
		} else {
		    customsWarehouseCode = warehousePO
			    .getCustomsWarehouseCode();
		    if (!StringUtils.hasText(customsWarehouseCode))
			throw new ValidationErrorException("庫別("
				+ warehouseCode + ")的海關關別未設定！");
		}
		cmKey.delete(0, cmKey.length());
		cmKey.append(declNo + "{$}");
		cmKey.append(declSeq + "{$}");
		cmKey.append(itemCode + "{$}");
		cmKey.append(customsWarehouseCode);
		if (cmMap.get(cmKey.toString()) == null) {
		    cmMap.put(cmKey.toString(), quantity);
		} else {
		    cmMap.put(cmKey.toString(), quantity
			    + ((Double) cmMap.get(cmKey.toString())));
		}
	    }

	    key.delete(0, key.length());
	    key.append(itemCode + "{$}");
	    key.append(warehouseCode + "{$}");
	    key.append(lotNo + "{$}");
	    key.append(itemTaxCode);
	    if (map.get(key.toString()) == null) {
		map.put(key.toString(), quantity);
	    } else {
		map.put(key.toString(), quantity
			+ ((Double) map.get(key.toString())));
	    }
	    // 將itemCode是否為服務性商品、組合性商品放入map中
	    if (isServiceItemMap.get(itemCode) == null)
		isServiceItemMap.put(itemCode, originalOrderItem
			.getIsServiceItem());
	    if (isComposeItemMap.get(itemCode) == null)
		isComposeItemMap.put(itemCode, originalOrderItem
			.getIsComposeItem());
	    // 20100818 modify by joeywu for 明細裏含相同料號時，可以只勾選部份負庫存的品號
	    if (allowWholeSale.get(itemCode) == null) {
		allowWholeSale.put(itemCode, originalOrderItem
			.getAllowWholeSale());
	    } else if ("Y".equals(originalOrderItem.getAllowWholeSale())) {
		allowWholeSale.put(itemCode, originalOrderItem
			.getAllowWholeSale());
	    }
	    //System.out.println("allowWholeSale.get(" + itemCode + ") = "
		    //+ allowWholeSale.get(itemCode));
	}
	
	return new Set[] { map.entrySet(), cmMap.entrySet() };
    }

    /**
     * 將SalesOrderItem轉換成DeliveryLine
     * 
     * @param salesOrderHead
     * @param loginUser
     */
    public void salesOrderToDeliveryForT2(SoSalesOrderHead salesOrderHead,
    	    String loginUser) {
    	Long salesOrderHeadId = salesOrderHead.getHeadId();
    	
    	List imDeliveryLines = imDeliveryLineDAO.findByProperty(
    			"ImDeliveryLine", "salesOrderId", salesOrderHeadId);
    	if(imDeliveryLines !=null){
    		for (int i = 0; i < imDeliveryLines.size(); i++) {
    			ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines.get(i);
    			imDeliveryLineDAO.delete(deliveryLine);
    		}
    	}
    	
    	List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
    	Long indexNo = 1L;
    	if (salesOrderItems != null && salesOrderItems.size() > 0) {
    	    for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
    		salesOrderItem.setIndexNo(indexNo++);

    		imDeliveryLineDAO.save(copySalesOrderItemToDeliveryLineForT2(
    			salesOrderHeadId, salesOrderItem, loginUser));
    	    }
    	}
        }

    /**
     * 將SalesOrderItem內容複製到ImDeliveryLine(T2)
     * 
     * @param salesOrderItem
     * @param lotNo
     * @param loginUser
     * @return ImDeliveryLine
     */
    private ImDeliveryLine copySalesOrderItemToDeliveryLineForT2(
	    Long salesOrderHeadId, SoSalesOrderItem salesOrderItem,
	    String loginUser) {
	ImDeliveryLine deliveryLine = new ImDeliveryLine();
	BeanUtils.copyProperties(salesOrderItem, deliveryLine);
	deliveryLine.setSalesOrderId(salesOrderHeadId);
	deliveryLine.setSalesQuantity(salesOrderItem.getQuantity());
	deliveryLine.setShipQuantity(salesOrderItem.getQuantity());
	deliveryLine.setOriginalWarehouseCode(salesOrderItem.getWarehouseCode());
	deliveryLine.setOriginalLotNo(salesOrderItem.getLotNo());
	deliveryLine.setOriginalSalesQuantity(salesOrderItem.getQuantity());
	deliveryLine.setOriginalOnHandQty(salesOrderItem.getCurrentOnHandQty());// 原庫存量
	//deliveryLine.setCombineCode(salesOrderItem.getCombineCode());// 組合代號
	deliveryLine.setStatus(OrderStatus.SAVE);
	deliveryLine.setCreatedBy(loginUser);
	deliveryLine.setCreationDate(new Date());
	deliveryLine.setLastUpdatedBy(loginUser);
	deliveryLine.setLastUpdateDate(new Date());
	return deliveryLine;
    }

    /**
     * 將銷貨資料更新至銷貨單主檔及明細檔(AJAX)
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSalesOrder(Map parameterMap) throws FormException, Exception {
    log.info("updateAJAXSalesOrder");
	HashMap resultMap = new HashMap();
	try {
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Long headId = NumberUtils.getLong((String) PropertyUtils
		    .getProperty(formLinkBean, "headId"));
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    // ====================取得條件資料======================
	    HashMap conditionMap = getConditionData(parameterMap);
	    String formAction = (String) conditionMap.get("formAction");
	    String loginEmployeeCode = (String) conditionMap.get("loginEmployeeCode");
	    String organizationCode = (String) conditionMap.get("organizationCode");
	    if (OrderStatus.SAVE.equals(formAction)) {
		// 如果是T2就不要重排
		if (!"T2".equals(salesOrderHeadPO.getBrandCode()))
		    sortSalesOrderItem(salesOrderHeadPO);
		String brandCode = (String) conditionMap.get("brandCode");
		String orderTypeCode = (String) conditionMap.get("orderTypeCode");
		String customerCode_var = (String) conditionMap.get("customerCode_var");
		String searchCustomerType = (String) conditionMap.get("searchCustomerType");
		// ================取得customerType、vipType、vipPromotionCode======================
		HashMap customerInfoMap = getCustomerInfo(brandCode, orderTypeCode, 
				customerCode_var, searchCustomerType, null);
		String actualCustomerCode = (String) customerInfoMap.get("actualCustomerCode");
		salesOrderHeadPO.setCustomerCode(actualCustomerCode);
		AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);
	    }
	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, salesOrderHeadPO);

	    salesOrderHeadPO.setStatus(formAction);
	    String resultMsg = modifyAjaxSoSalesOrder(salesOrderHeadPO, loginEmployeeCode);
	    if (OrderStatus.REJECT.equals(formAction)) {
	    	revertToOriginallyAvailableQuantity(salesOrderHeadPO, organizationCode, loginEmployeeCode);

	    	//for 儲位用
	    	if(imStorageAction.isStorageExecute(salesOrderHeadPO)){
	    		//異動儲位庫存
	    		imStorageService.updateStorageOnHandBySource(salesOrderHeadPO, OrderStatus.SAVE, PROGRAM_ID, null, true);
	    	}

	    	String taxCode = buOrderTypeService.getTaxCode(salesOrderHeadPO.getBrandCode(), salesOrderHeadPO.getOrderTypeCode());
	    	if (!POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode())
	    			&& "F".equals(taxCode))
	    		deleteFiInvoice(salesOrderHeadPO);
	    }
	    resultMap.put("entityBean", salesOrderHeadPO);
	    resultMap.put("resultMsg", resultMsg);
	    return resultMap;
	} catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 將銷貨資料更新至銷貨單主檔及明細檔(AJAX)
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateAJAXSalesOrderItem(Map parameterMap) throws FormException,
	    Exception {
	HashMap resultMap = new HashMap();
	try {
	    Object formBindBean = parameterMap.get("vatBeanFormBind");
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Long headId = NumberUtils.getLong((String) PropertyUtils
		    .getProperty(otherBean, "headId"));
	    Long lineId = NumberUtils.getLong((String) PropertyUtils
		    .getProperty(otherBean, "lineId"));
	    SoSalesOrderHead soSalesOrderHead = findById(headId);
	    // ====================取得條件資料======================
	    SoSalesOrderItem item = (SoSalesOrderItem) soSalesOrderItemDAO
		    .findById("SoSalesOrderItem", lineId);
	    if (item == null) {
		item = new SoSalesOrderItem();
		AjaxUtils.copyJSONBeantoPojoBean(formBindBean, item);
		Long maxIndex = soSalesOrderItemDAO
			.findPageLineMaxIndex(headId);
		item.setIndexNo(maxIndex + 1);
		item.setSoSalesOrderHead(soSalesOrderHead);
		soSalesOrderItemDAO.save(item);
	    } else {
		AjaxUtils.copyJSONBeantoPojoBean(formBindBean, item);
		soSalesOrderItemDAO.update(item);
	    }
	    return resultMap;
	} catch (FormException fe) {
	    log.error("銷貨單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新銷貨售貨單
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map updateCustomerPoNo(Map parameterMap) throws FormException,
	    Exception {
	HashMap resultMap = new HashMap();
	int originalPrefix = 0;
	int newPrefix = 0;
	try {
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    String uuid = (String) parameterMap.get("UUID");
	    String processName = (String) parameterMap.get("PROCESS_NAME");
	    Date executeDate = (Date) parameterMap.get("EXECUTE_DATE");
	    String brandCode = (String) PropertyUtils.getProperty(formLinkBean,
		    "brandCode");
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(
		    formLinkBean, "loginEmployeeCode");
	    String salesOrderDate = (String) PropertyUtils.getProperty(
		    formLinkBean, "salesOrderDate");
	    String posMachineCode = (String) PropertyUtils.getProperty(
		    formLinkBean, "posMachineCode");
	    String customerPoNoStart = (String) PropertyUtils.getProperty(
		    formLinkBean, "customerPoNoStart");
	    String customerPoNoEnd = (String) PropertyUtils.getProperty(
		    formLinkBean, "customerPoNoEnd");
	    String accessNumber = (String) PropertyUtils.getProperty(
		    formLinkBean, "accessNumber");
	    String newCustomerPoNoStart = (String) PropertyUtils.getProperty(
		    formLinkBean, "newCustomerPoNoStart");
	    String newCustomerPoNoEnd = (String) PropertyUtils.getProperty(
		    formLinkBean, "newCustomerPoNoEnd");
	    String prefixCustomerPoNo = (String) PropertyUtils.getProperty(
		    formLinkBean, "prefixCustomerPoNo");

	    int maxPoNoLength = NumberUtils.getLong(((String)(PropertyUtils.getProperty(formLinkBean,"maxPoNoLength")))).intValue();
	    
	    originalPrefix = getPrefixIndex(customerPoNoStart);
	    if(StringUtils.hasText(prefixCustomerPoNo)){   // 若有輸入字軌改成以此為分隔點
		newPrefix = prefixCustomerPoNo.length();
	    }else{
		newPrefix = getPrefixIndex(customerPoNoStart);   // 分隔點
	    }
	    
	    int zeroNumber = maxPoNoLength - newPrefix; // 取得剩餘的數字字串長度
	    
	    log.info("brandCode = " + brandCode);
	    log.info("loginEmployeeCode = " + loginEmployeeCode);
	    log.info("salesOrderDate = " + salesOrderDate);
	    log.info("posMachineCode = " + posMachineCode);
	    log.info("customerPoNoStart = " + customerPoNoStart);
	    log.info("customerPoNoEnd = " + customerPoNoEnd);
	    log.info("accessNumber = " + accessNumber);
	    log.info("newCustomerPoNoStart = " + newCustomerPoNoStart);
	    log.info("newCustomerPoNoEnd = " + newCustomerPoNoEnd);
	    log.info("prefixCustomerPoNo = " + prefixCustomerPoNo);
	    log.info("newPrefix = " + newPrefix);
	    log.info("originalPrefix = " + originalPrefix);
	    log.info("zeroNumber = " + zeroNumber);
	    
	    StringBuffer exceptionMsg = new StringBuffer();
	    if (!StringUtils.hasText(salesOrderDate)) {
		exceptionMsg.append("請輸入交易日期");
	    }
	    if (!StringUtils.hasText(posMachineCode)) {
		exceptionMsg.append("\n請輸入機台");
	    }
	    if (!StringUtils.hasText(customerPoNoStart)) {
		exceptionMsg.append("\n請輸入售貨單起");
	    }
	    if (!StringUtils.hasText(customerPoNoEnd)) {
		exceptionMsg.append("\n請輸入售貨單迄");
	    }
	    if (!StringUtils.hasText(accessNumber)
		    && !StringUtils.hasText(prefixCustomerPoNo)) {
		exceptionMsg.append("\n請輸入增減數字");
	    }

	    if (!Pattern.matches("[A-Z]{"+newPrefix+"}\\d{"+zeroNumber+"}", newCustomerPoNoStart)) {  // [A-Z]{2}\\d{8}
		exceptionMsg.append("\n售貨單起前").append(newPrefix).append("碼必須為英文字母，後").append(zeroNumber).append("碼為數字");
	    }
	    if (!Pattern.matches("[A-Z]{"+newPrefix+"}\\d{"+zeroNumber+"}", newCustomerPoNoEnd)) {
		exceptionMsg.append("\n售貨單迄前").append(newPrefix).append("碼必須為英文字母，後").append(zeroNumber).append("碼為數字");
	    }

	    if (StringUtils.hasText(customerPoNoStart)
		    && StringUtils.hasText(customerPoNoEnd)) {
		if (!customerPoNoStart.substring(0, newPrefix).equals(
			customerPoNoEnd.substring(0, newPrefix))) {
		    exceptionMsg.append("\n售貨單起迄前").append(newPrefix).append("碼必須相同");
		}
		
	    }
	    
	    
	    
	    if (exceptionMsg.toString().length() != 0) {
		throw new Exception(exceptionMsg.toString());
	    }

	    Map findMap = new HashMap();
	    findMap.put("brandCode", brandCode);
	    findMap.put("salesOrderDate", salesOrderDate);
	    findMap.put("posMachineCode", posMachineCode);
	    findMap.put("customerPoNoStart", customerPoNoStart);
	    findMap.put("customerPoNoEnd", customerPoNoEnd);
	    findMap.put("newCustomerPoNoStart", newCustomerPoNoStart);
	    findMap.put("newCustomerPoNoEnd", newCustomerPoNoEnd);

	    // 新舊售貨單起迄字串比較
	    Long number1 = compareNumber(customerPoNoStart, customerPoNoEnd,
		    originalPrefix);
	    Long number2 = compareNumber(newCustomerPoNoStart,
		    newCustomerPoNoEnd, newPrefix);

	    log.info("number1 = " + number1);
	    log.info("number2 = " + number2);

	    // if(number1 != number2){
	    // if(number1 > number2){
	    // throw new Exception("舊售貨單起:" + customerPoNoStart + " 迄:" +
	    // customerPoNoEnd + " 大於新售貨單起:" + newCustomerPoNoStart + " 迄:" +
	    // newCustomerPoNoEnd + "個數:" + (number1 - number2 ));
	    // }else if( number1 < number2 ){
	    // throw new Exception("新售貨單起:" + newCustomerPoNoStart + " 迄:" +
	    // newCustomerPoNoEnd + " 個數大於舊售貨單起:" + customerPoNoStart + " 迄:" +
	    // customerPoNoEnd + "個數:" + (number2 - number1 ));
	    // }
	    // }

	    // 查詢舊的筆數
	    List<SoSalesOrderHead> soSalesOrderHeads = soSalesOrderHeadDAO
		    .findCustomerPoNo(findMap);
	    int size = soSalesOrderHeads.size();
	    log.info("size = " + size);
	    // if(size != number1){
	    // if( size > number1){
	    // throw new Exception("資料庫資料大於舊售貨單起:" + customerPoNoStart + " 迄:" +
	    // customerPoNoEnd + "個數:" + (size - number1));
	    // }else if( number1 > size){
	    // throw new Exception("舊售貨單起:" + customerPoNoStart + " 迄:" +
	    // customerPoNoEnd + " 大於資料庫資料個數:" + (number1 - size));
	    // }
	    // }
	    // 轉成舊新對應的map
	    LinkedHashMap linkedHashMap = stringBetweenToMap(customerPoNoStart,
		    customerPoNoEnd, newCustomerPoNoStart, newPrefix, originalPrefix, maxPoNoLength);

	    log.info("linkedHashMap = " + linkedHashMap);

	    // 確認出貨單與銷貨單的數量一致
	    int imDeliveryHeadSize = 0;
	    for (SoSalesOrderHead soSalesOrderHead : soSalesOrderHeads) {
		ImDeliveryHead imDeliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
			.findFirstByProperty("ImDeliveryHead",
				"and salesOrderId = ?",
				new Object[] { soSalesOrderHead.getHeadId() });
		if (imDeliveryHead != null) {
		    imDeliveryHeadSize = imDeliveryHeadSize + 1;
		}
	    }

	    // 檢核銷貨與出貨的數量一致
	    if (size == 0) {
		throw new Exception("查無銷貨單資料");
	    } else if (number2 < size) {
		throw new Exception("查輸入的資料區間:" + number2 + "小於更新銷貨單資料:" + size);
	    } else if (number2 < imDeliveryHeadSize) {
		throw new Exception("查輸入的資料區間:" + number2 + "小於更新出貨單資料:"
			+ imDeliveryHeadSize);
	    }

	    // 一致更新 SoSalesOrderHead
	    for (SoSalesOrderHead soSalesOrderHead : soSalesOrderHeads) {
		if (linkedHashMap.containsKey(soSalesOrderHead
			.getCustomerPoNo())) {
		    soSalesOrderHead.setCustomerPoNo((String) linkedHashMap
			    .get(soSalesOrderHead.getCustomerPoNo()));
		    soSalesOrderHead.setLastUpdatedBy(loginEmployeeCode);
		    soSalesOrderHead.setLastUpdateDate(new Date());
		    soSalesOrderHeadDAO.update(soSalesOrderHead);
		} else {
		    log.error("查無資料" + soSalesOrderHead.getCustomerPoNo());
		}
		// 撈 IM_DELIVERY_HEAD 更新 IM_DELIVERY_HEAD
		ImDeliveryHead imDeliveryHead = (ImDeliveryHead) imDeliveryHeadDAO
			.findFirstByProperty("ImDeliveryHead",
				"and salesOrderId = ?",
				new Object[] { soSalesOrderHead.getHeadId() });
		if (imDeliveryHead != null) {
		    if (linkedHashMap.containsKey(imDeliveryHead
			    .getCustomerPoNo())) {
			imDeliveryHead.setCustomerPoNo((String) linkedHashMap
				.get(imDeliveryHead.getCustomerPoNo()));
			imDeliveryHead.setLastUpdatedBy(loginEmployeeCode);
			imDeliveryHead.setLastUpdateDate(new Date());
			imDeliveryHeadDAO.update(imDeliveryHead);
		    } else {
			log.error("查無資料" + imDeliveryHead.getCustomerPoNo());
		    }
		} else {
		    // log.error("查無資料出貨單:" + soSalesOrderHead.getHeadId() );
		    // throw new Exception("查無資料出貨單:" +
		    // soSalesOrderHead.getHeadId());
		}
	    }

	    String successMsg = "銷售售貨單更新成功" + size + "筆和出貨單更新成功"
		    + imDeliveryHeadSize + "筆";
	    resultMap.put("resultMsg", successMsg);

	    StringBuffer msg = new StringBuffer("品牌:");
	    msg.append(" ").append(brandCode).append(" 交易日期:").append(
		    salesOrderDate).append(" 機台:").append(posMachineCode)
		    .append(" 售貨單(舊):").append(customerPoNoStart).append("~")
		    .append(customerPoNoEnd).append(" 增減數字:").append(
			    accessNumber);
	    msg.append("售貨單(新):").append(newCustomerPoNoStart).append("~")
		    .append(newCustomerPoNoEnd).append(" ").append(successMsg);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, msg.toString(), executeDate, uuid,
		    loginEmployeeCode);

	    return resultMap;
	} catch (Exception ex) {
	    log.error("銷貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception(ex.getMessage());
	}
    }

    /**
     * 比較兩字串個數
     * 
     * @param str1
     * @param str2
     * @return
     */
    private Long compareNumber(String str1, String str2, int subStringIndex) {
	Long long1 = NumberUtils.getLong(str1.substring(subStringIndex));
	Long long2 = NumberUtils.getLong(str2.substring(subStringIndex));
	Long temp = null;
	if (long1 > long2) {
	    temp = long1;
	    long1 = long2;
	    long2 = temp;
	}

	return long2 - long1 + 1;
    }

    /**
     * 將兩兩新舊字串區間轉map
     * 
     * @param str1
     * @param str2
     * @return
     */
    private LinkedHashMap stringBetweenToMap(String str1, String str2,
	    String newString, int pNewPrefix, int originalPrefix, int maxPoNoLength) {
	Long number1 = compareNumber(str1, str2, originalPrefix);
	String prefix = str1.substring(0, originalPrefix);
	String newPrefix = newString.substring(0, pNewPrefix);
	Long long1 = NumberUtils.getLong(str1.substring(originalPrefix));
	Long long2 = NumberUtils.getLong(newString.substring(pNewPrefix));

	
	DecimalFormat originalDf = doDecimalFormat(maxPoNoLength - originalPrefix);
	DecimalFormat newDf = doDecimalFormat(maxPoNoLength - pNewPrefix);

	LinkedHashMap map = new LinkedHashMap();
	for (int i = 0; i < number1; i++) {
	    map.put(prefix + originalDf.format((long1 + i)), newPrefix
		    + newDf.format((long2 + i)));
	}

	return map;
    }

    /**
     * 回復至原始可用庫存，並將出貨單明細刪除
     * 
     * @param salesOrderHead
     * @param organizationCode
     * @param loginUser
     * @throws FormException
     * @throws NoSuchDataException
     */
    public void revertToOriginallyAvailableQuantity(
	    SoSalesOrderHead salesOrderHead, String organizationCode,
	    String loginUser) throws FormException, Exception {
	log.info("revertToOriginallyAvailableQuantity");
	String brandCode = salesOrderHead.getBrandCode();
	Long SalesOrderHeadId = salesOrderHead.getHeadId();
	List imDeliveryLines = imDeliveryLineDAO.findByProperty(
		"ImDeliveryLine", "salesOrderId", SalesOrderHeadId);
	if (imDeliveryLines != null && imDeliveryLines.size() > 0) {
	    for (int i = 0; i < imDeliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines
			.get(i);
		// 非服務性商品補回庫存
		if (!"Y".equals(deliveryLine.getIsServiceItem())) {
		    String itemTaxCode = deliveryLine.getIsTax();
		    String itemCode = deliveryLine.getItemCode();
		    String warehouseCode = deliveryLine.getWarehouseCode();
		    String customsWarehouseCode = null;
		    Double salesQuantity = deliveryLine.getSalesQuantity();
		    if (!StringUtils.hasText(itemTaxCode)) {
			ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
			if (itemPO == null) {
			    throw new NoSuchObjectException("依據品牌(" + brandCode
				    + ")、品號(" + itemCode + ")查無商品相關資料！");
			} else {
			    itemTaxCode = itemPO.getIsTax();
			    if (!StringUtils.hasText(itemTaxCode))
				throw new ValidationErrorException("品牌("
					+ brandCode + ")、品號(" + itemCode
					+ ")的稅別未設定！");
			}
		    }
		    log.info("這邊開始退貨 itemTaxCode=" + itemTaxCode);
		    if ("F".equals(itemTaxCode)) {
			ImWarehouse warehousePO = imWarehouseService
				.findByBrandCodeAndWarehouseCode(brandCode,
					warehouseCode, null);
			if (warehousePO == null) {
			    throw new NoSuchObjectException("依據品牌(" + brandCode
				    + ")、庫別(" + warehouseCode + ")查無庫別相關資料！");
			} else {
			    customsWarehouseCode = warehousePO
				    .getCustomsWarehouseCode();
			    if (!StringUtils.hasText(customsWarehouseCode))
				throw new ValidationErrorException("庫別("
					+ warehouseCode + ")的海關關別未設定！");
			}
			cmDeclarationOnHandDAO.updateOutUncommitQuantity(
				deliveryLine.getImportDeclNo(), deliveryLine
					.getImportDeclSeq(), itemCode,
				customsWarehouseCode, brandCode, salesQuantity,
				loginUser);
		    }
		    imOnHandDAO.updateOutUncommitQuantity(organizationCode,
			    itemCode, warehouseCode, deliveryLine.getLotNo(),
			    salesQuantity, loginUser, brandCode);
		}
		imDeliveryLineDAO.delete(deliveryLine);
	    }
	} else {
	    throw new NoSuchObjectException("依據銷貨單主檔主鍵：" + SalesOrderHeadId
		    + "查無相關出貨單明細檔資料！");
	}
    }

    public void createProgramLog(String message, String identification,
	    List errorMsgs, String user) {
	siProgramLogAction.createProgramLog(PROGRAM_ID,
		MessageStatus.LOG_ERROR, identification, message, user);
	errorMsgs.add(message);
	log.error(message);
    }

    private void modifySoSalesOrder(SoSalesOrderHead updateObj, String loginUser) {
	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());
	soSalesOrderHeadDAO.update(updateObj);
    }

    /**
     * 暫存單號取實際單號並更新至銷貨單主檔及明細檔
     * 
     * @param soSalesOrderHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxSoSalesOrder(SoSalesOrderHead soSalesOrderHead,
	    String loginUser) throws ObtainSerialNoFailedException,
	    FormException, Exception {
	String serialNo = "";
	if (AjaxUtils.isTmpOrderNo(soSalesOrderHead.getOrderNo())) {
	    if ("SOP".equals(soSalesOrderHead.getOrderTypeCode())) {
		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead
			.getBrandCode(), soSalesOrderHead.getOrderTypeCode(),
			soSalesOrderHead.getShopCode(), soSalesOrderHead
				.getPosMachineCode());
	    } else {
		serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead
			.getBrandCode(), soSalesOrderHead.getOrderTypeCode());
	    }

	    //for 儲位用
		if(imStorageAction.isStorageExecute(soSalesOrderHead)){
			//取得儲位單正式的單號 2011.11.11 by Caspar
			ImStorageHead imStorageHead = imStorageAction.updateOrderNo(soSalesOrderHead);

			//更新儲位單SOURCE ORDER_NO
			imStorageHead.setSourceOrderNo(serialNo);
			imStorageService.updateHead(imStorageHead, loginUser);
		}
		
	    if (!serialNo.equals("unknow"))
		soSalesOrderHead.setOrderNo(serialNo);
	    else
		throw new ObtainSerialNoFailedException("取得"
			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
	}
	modifySoSalesOrder(soSalesOrderHead, loginUser);
	return soSalesOrderHead.getOrderTypeCode() + "-"
		+ soSalesOrderHead.getOrderNo() + "存檔成功！";
    }

    /**
     * SOP執行反確認
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeAJAXAntiConfirm(Map parameterMap) throws FormException,
	    Exception {
    	log.info("executeAJAXAntiConfirm");
	HashMap resultMap = new HashMap();
	try {
	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    Long headId = NumberUtils.getLong((String) PropertyUtils.getProperty(formLinkBean, "headId"));
	    SoSalesOrderHead salesOrderHeadPO = findById(headId);
	    // ====================取得條件資料======================
	    HashMap conditionMap = getConditionData(parameterMap);
	    String employeeCode = (String) conditionMap.get("employeeCode");
	    String organizationCode = (String) conditionMap.get("organizationCode");
	    if (!POSTYPECODE.equals(salesOrderHeadPO.getOrderTypeCode())) {
		throw new ValidationErrorException("單別："
			+ salesOrderHeadPO.getOrderTypeCode() + "不可執行反確認！");
	    } else if (!OrderStatus.SIGNING
		    .equals(salesOrderHeadPO.getStatus())) {
		throw new ValidationErrorException("狀態："
			+ OrderStatus.getChineseWord(salesOrderHeadPO
				.getStatus()) + "不可執行反確認！");
	    }
	    revertToOriginallyAvailableQuantity(salesOrderHeadPO, organizationCode, employeeCode);
	    salesOrderHeadPO.setStatus(OrderStatus.UNCONFIRMED);
	    
	    //for 儲位用
		if(imStorageAction.isStorageExecute(salesOrderHeadPO)){
			//異動儲位庫存
			imStorageService.updateStorageOnHandBySource(salesOrderHeadPO, OrderStatus.SAVE, PROGRAM_ID, null, true);
		}
		
	    modifySoSalesOrder(salesOrderHeadPO, employeeCode);
	    
	    String resultMsg = salesOrderHeadPO.getOrderTypeCode() + "-"
		    + salesOrderHeadPO.getOrderNo() + "執行反確認成功！";
	    resultMap.put("entityBean", salesOrderHeadPO);
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;
	} catch (FormException fe) {
	    log.error("執行反確認失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("執行反確認時發生錯誤，原因：" + ex.toString());
	    throw new Exception("執行反確認時發生錯誤，原因：" + ex.getMessage());
	}
    }

    private FiInvoiceHead produceFiInvoice(SoSalesOrderHead salesOrderHead) {
	FiInvoiceHead fiInvoiceHead = new FiInvoiceHead();
	fiInvoiceHead.setBrandCode(salesOrderHead.getBrandCode());
	fiInvoiceHead.setInvoiceNo(salesOrderHead.getOrderTypeCode()
		+ salesOrderHead.getOrderNo());
	fiInvoiceHead.setInvoiceDate(salesOrderHead.getSalesOrderDate());
	fiInvoiceHead.setCurrencyCode(salesOrderHead.getCurrencyCode());
	fiInvoiceHead.setCustomsDeclarationNo(salesOrderHead.getExportDeclNo());
	fiInvoiceHead.setCustomsDeclarationType(salesOrderHead
		.getExportDeclType());
	fiInvoiceHead.setExchangeRate(salesOrderHead.getExportExchangeRate());
	fiInvoiceHead.setTotalLocalInvoiceAmount(salesOrderHead
		.getTotalActualSalesAmount());
	fiInvoiceHead.setTotalForeignInvoiceAmount(salesOrderHead
		.getActualTotalFrnSalesAmt());
	fiInvoiceHead
		.setReceiveOrderTypeCode(salesOrderHead.getOrderTypeCode());
	fiInvoiceHead.setReceiveOrderNo(salesOrderHead.getOrderNo());
	fiInvoiceHead.setOrderTypeCode(null);
	fiInvoiceHead.setTotalBoxNo(1L);
	fiInvoiceHead.setWeightUnit("KGS");
	fiInvoiceHead.setLastUpdateDate(new Date());
	fiInvoiceHead.setLastUpdatedBy(salesOrderHead.getCreatedBy());
	fiInvoiceHead.setCreationDate(new Date());
	fiInvoiceHead.setCreatedBy(salesOrderHead.getCreatedBy());

	List<FiInvoiceLine> fiInvoiceLines = new ArrayList(0);
	List<SoSalesOrderItem> soSalesOrderItems = salesOrderHead
		.getSoSalesOrderItems();
	for (int i = 0; i < soSalesOrderItems.size(); i++) {
	    SoSalesOrderItem soSalesOrderItem = soSalesOrderItems.get(i);
	    FiInvoiceLine fiInvoiceLine = new FiInvoiceLine();
	    fiInvoiceLine.setFiInvoiceHead(fiInvoiceHead);
	    fiInvoiceLine.setBrandCode(salesOrderHead.getBrandCode());
	    fiInvoiceLine.setQuantity(soSalesOrderItem.getQuantity());
	    fiInvoiceLine.setItemCode(soSalesOrderItem.getItemCode());
	    fiInvoiceLine.setLastUpdateDate(new Date());
	    fiInvoiceLine.setLastUpdatedBy(salesOrderHead.getCreatedBy());
	    fiInvoiceLine.setCreationDate(new Date());
	    fiInvoiceLine.setCreatedBy(salesOrderHead.getCreatedBy());
	    fiInvoiceLines.add(fiInvoiceLine);
	}
	fiInvoiceHead.setFiInvoiceLines(fiInvoiceLines);
	fiInvoiceHeadDAO.save(fiInvoiceHead);
	return fiInvoiceHead;
    }

    private void deleteFiInvoice(SoSalesOrderHead salesOrderHead) {
	List<FiInvoiceHead> fiInvoiceHeads = fiInvoiceHeadDAO.findByProperty(
		"FiInvoiceHead", "and brandCode = ? and invoiceNo = ?",
		new String[] {
			salesOrderHead.getBrandCode(),
			salesOrderHead.getOrderTypeCode()
				+ salesOrderHead.getOrderNo() });
	if (fiInvoiceHeads != null && fiInvoiceHeads.size() > 0)
	    fiInvoiceHeadDAO.delete(fiInvoiceHeads.get(0));
    }

    public static Object[] startProcess(SoSalesOrderHead form)
	    throws ProcessFailedException {
	try {
	    String packageId = "So_SalesOrder";
	    String processId = "SalesOrder";
	    String version = "20090723";
	    String sourceReferenceType = "SoSalesOrder (1)";
	    HashMap context = new HashMap();
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version,
		    sourceReferenceType, context);
	} catch (Exception ex) {
	    log.error("銷貨流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("銷貨流程啟動失敗！");
	}
    }

    public static Object[] startProcessT2(SoSalesOrderHead form)
	    throws ProcessFailedException {
	try {
	    String packageId = "So_SalesOrder";
	    String processId = "SalesOrder";
	    String version = "20140710";
	    String sourceReferenceType = "SoSalesOrder (1)";
	    HashMap context = new HashMap();
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version,
		    sourceReferenceType, context);
	} catch (Exception ex) {
	    log.error("銷貨流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("銷貨流程啟動失敗！");
	}
    }

    public String getIdentification(Long headId) throws Exception {
	String id = null;
	try {
	    SoSalesOrderHead salesOrderHead = findById(headId);
	    if (salesOrderHead != null) {
		id = MessageStatus.getIdentification(salesOrderHead
			.getBrandCode(), salesOrderHead.getOrderTypeCode(),
			salesOrderHead.getOrderNo());
	    } else {
		throw new NoSuchDataException("銷貨單主檔查無主鍵：" + headId + "的資料！");
	    }
	    return id;
	} catch (Exception ex) {
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public List<Properties> executeSearchInitial(Map parameterMap)
	    throws Exception {
	HashMap returnMap = new HashMap();
	Map multiList = new HashMap(0);
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String) PropertyUtils.getProperty(otherBean,
		    "loginBrandCode");
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginEmployeeCode");
	    BuBrand buBrand = buBrandDAO.findById(brandCode);
	    List<BuCommonPhraseLine> allStatus = buCommonPhraseService
		    .findEnableLineById("FormStatus");
	    multiList.put("allStatus", AjaxUtils.produceSelectorData(allStatus,
		    "lineCode", "name", false, true));

	    List<BuShop> shopForEmployee = buBasicDataService
		    .getShopForEmployee(brandCode, loginEmployeeCode, "Y");
	    if (shopForEmployee == null || shopForEmployee.size() == 0)
		throw new Exception("查無使用者可使用之店櫃");
	    multiList.put("shopForEmployee", AjaxUtils.produceSelectorData(
		    shopForEmployee, "shopCode", "shopCName", true, false));

	    List<ImWarehouse> allAvailableWarehouse = imWarehouseService
		    .getWarehouseByWarehouseEmployee(brandCode,
			    loginEmployeeCode, null);
	    multiList.put("allAvailableWarehouse", AjaxUtils
		    .produceSelectorData(allAvailableWarehouse,
			    "warehouseCode", "warehouseName", true, true));

	    List allShopMachine = buShopMachineDAO.getShopMachineForEmployee(
		    brandCode, loginEmployeeCode, null);
	    multiList.put("allShopMachine", AjaxUtils.produceSelectorData(
		    allShopMachine, "posMachineCode", "posMachineCode", false,
		    true));

	    returnMap.put("brandCode", brandCode);
	    returnMap.put("brandName", buBrand.getBrandName());
	    returnMap.put("multiList", multiList);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	} catch (Exception ex) {
	    log.error("採購預算單初始化失敗，原因：" + ex.toString());
	    throw new Exception("採購預算單初始化失敗，原因：" + ex.getMessage());
	}
    }

    public List<Properties> getAJAXSearchPageData(Properties httpRequest)
	    throws Exception {
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	    String incharge = httpRequest.getProperty("incharge");
	    String startOrderNo = httpRequest.getProperty("startOrderNo");
	    String endOrderNo = httpRequest.getProperty("endOrderNo");
	    String customsNo = httpRequest.getProperty("customsNo");
	    String customsNo1 = httpRequest.getProperty("customsNo1");
	    String status = httpRequest.getProperty("status");
	    String customerCode = httpRequest.getProperty("customerCode");
	    Date salesOrderStartDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("salesOrderStartDate"));
	    Date salesOrderEndDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("salesOrderEndDate"));
	    Date scheduleShipDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("scheduleShipDate"));
	    String superintendentCode = httpRequest.getProperty("superintendentCode");
	    String defaultWarehouseCode = httpRequest.getProperty("defaultWarehouseCode");
	    Date scheduleCollectionStartDate = DateUtils.parseDate( "yyyy/MM/dd", httpRequest.getProperty("scheduleCollectionStartDate"));
	    Date scheduleCollectionEndDate = DateUtils.parseDate("yyyy/MM/dd",httpRequest.getProperty("scheduleCollectionEndDate"));
	    String transactionSeqNo = httpRequest.getProperty("transactionSeqNo");
	    String transactionSeqNo1 = httpRequest.getProperty("transactionSeqNo1");
	    String customerPoNo = httpRequest.getProperty("customerPoNo");
	    String customerPoNo1 = httpRequest.getProperty("customerPoNo1");
	    String posMachineCode = httpRequest.getProperty("posMachineCode");
//排序 Maco 2015.08.11
	    String orderByType = httpRequest.getProperty("orderByType");
	    String orderBySeq = httpRequest.getProperty("orderBySeq");
	    String schedule = httpRequest.getProperty("schedule");
	    String eventCode = httpRequest.getProperty("eventCode");
	    log.info("orderByType:"+orderByType);
	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and s.brandCode  = :brandCode", brandCode);
	    findObjs.put(" and s.orderTypeCode  = :orderTypeCode", orderTypeCode);
	    findObjs.put(" and b.incharge  = :incharge", incharge);
	    findObjs.put(" and s.orderNo NOT LIKE :TMP", "TMP%");
	    findObjs.put(" and s.orderNo  >= :startOrderNo", startOrderNo);
	    findObjs.put(" and s.orderNo  <= :endOrderNo", endOrderNo);
	    findObjs.put(" and s.status  = :status", status);
	    if(!customsNo.equals("")||!customsNo1.equals(""))
	    {
	    	if(customsNo.equals("")){
	    		findObjs.put(" and s.customsNo = :customsNo1",customsNo);
	    	}
	    	else if(customsNo1.equals("")){
	    		findObjs.put(" and s.customsNo = :customsNo",customsNo);
	    	}
	    	else{
	    	    findObjs.put(" and s.customsNo  >= :customsNo", customsNo);
	    	    findObjs.put(" and s.customsNo  <= :customsNo1", customsNo1);
	    	}
	    }

	    findObjs.put(" and s.customerCode  = :customerCode", customerCode);
	    findObjs.put(" and s.salesOrderDate  >= :salesOrderStartDate",salesOrderStartDate);
	    findObjs.put(" and s.salesOrderDate  <= :salesOrderEndDate",salesOrderEndDate);
	    findObjs.put(" and s.scheduleShipDate  = :scheduleShipDate",scheduleShipDate);
	    findObjs.put(" and s.superintendentCode  = :superintendentCode",superintendentCode);
	    findObjs.put(" and s.defaultWarehouseCode  = :defaultWarehouseCode",defaultWarehouseCode);
	    findObjs.put(" and s.scheduleCollectionDate  >= :scheduleCollectionStartDate",scheduleCollectionStartDate);
	    findObjs.put(" and s.scheduleCollectionDate  <= :scheduleCollectionEndDate",scheduleCollectionEndDate);
	    //findObjs.put(" and s.transactionSeqNo = :transactionSeqNo",transactionSeqNo);
	    if(!transactionSeqNo.equals("")||!transactionSeqNo1.equals(""))
	    {
	    	if(transactionSeqNo.equals("")){
	    		findObjs.put(" and s.transactionSeqNo = :transactionSeqNo1",transactionSeqNo1);
	    	}
	    	else if(transactionSeqNo1.equals("")){
	    		findObjs.put(" and s.transactionSeqNo = :transactionSeqNo",transactionSeqNo);
	    	}
	    	else{
	    		findObjs.put(" and s.transactionSeqNo  >= :transactionSeqNo", transactionSeqNo);
	    		findObjs.put(" and s.transactionSeqNo  <= :transactionSeqNo1", transactionSeqNo1);
	    	}
	    }
	    if(!customerPoNo.equals("")||!customerPoNo1.equals(""))
	    {
	    	if(customerPoNo.equals("")){
	    		findObjs.put(" and s.customerPoNo = :customerPoNo1",customerPoNo1);
	    	}
	    	else if(customerPoNo1.equals("")){
	    		findObjs.put(" and s.customerPoNo = :customerPoNo",customerPoNo);
	    	}
	    	else{
	    	    findObjs.put(" and s.customerPoNo >= :customerPoNo", customerPoNo);
	    	    findObjs.put(" and s.customerPoNo <= :customerPoNo1", customerPoNo1);
	    	}
	    }

	    findObjs.put(" and s.posMachineCode = :posMachineCode",posMachineCode);
	    findObjs.put(" and s.schedule = :schedule",schedule);
	    findObjs.put(" and s.eventCode = :eventCode",eventCode);
	    // ==============================================================
	    
	    
	    
	    String orderByKey = "";

	    //排序 Maco 2015.08.11  "orderNo","salesOrderDate","lastUpdateDate","status
	    if(!orderByType.equals(""))
	    {
		    	orderByKey = "order by s."+orderByType;
		    	//asc 遞增 / desc 遞減
		    	if(orderBySeq.equals("desc"))
		    	{
		    		orderByKey =  orderByKey+" "+ orderBySeq;
		    	}
	    }
	    else
	    {
	    	//原有預設排序
		    if ("T2".equals(brandCode))
		    {
		    	orderByKey = "order by s.customerPoNo, s.lastUpdateDate desc";
		    }
		    else 
		    {
		    	orderByKey = "order by s.lastUpdateDate desc";
		    }
	    }
	    log.info("排序方式:"+orderByKey);
	    
//	    Map headsMap = soSalesOrderHeadDAO.search("SoSalesOrderHead",
//		    findObjs, orderByKey, iSPage, iPSize,
//		    BaseDAO.QUERY_SELECT_RANGE);
//	    List<SoSalesOrderHead> heads = (List<SoSalesOrderHead>) headsMap
//		    .get(BaseDAO.TABLE_LIST);
	    List<SoSalesOrderHead> heads = soSalesOrderHeadDAO.getSoListForIncharge(findObjs, orderByKey, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List SoListSize = soSalesOrderHeadDAO.getSoListForIncharge(findObjs, "", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT); //取得總筆數
	    if (heads != null && heads.size() > 0) {
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = Long.valueOf(SoListSize.get(0).toString()); // 取得最後一筆 INDEX
		for (Iterator iterator = heads.iterator(); iterator.hasNext();) {
		    SoSalesOrderHead head = (SoSalesOrderHead) iterator.next();
		    this.setHeadNames(head);
		}
		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES, heads, gridDatas,
			firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的選單功能查詢失敗！");
	}
    }

    /**
     * customerPoNo查詢
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchCustomerPoNoPageData(
	    Properties httpRequest) throws Exception {
	DecimalFormat df = null;
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================
	    String brandCode = httpRequest.getProperty("brandCode");
	    Date salesOrderDate = DateUtils.parseDate(
		    DateUtils.C_DATE_PATTON_SLASH, httpRequest
			    .getProperty("salesOrderDate"));
	    String posMachineCode = httpRequest.getProperty("posMachineCode");
	    String customerPoNoStart = httpRequest
		    .getProperty("customerPoNoStart");
	    String customerPoNoEnd = httpRequest.getProperty("customerPoNoEnd");
	    String isChangeNumber = httpRequest.getProperty("isChangeNumber");
	    Long accessNumber = NumberUtils.getLong(httpRequest
		    .getProperty("accessNumber"));
	    String prefixCustomerPoNo = httpRequest
		    .getProperty("prefixCustomerPoNo");
	    int maxPoNoLength = NumberUtils.getLong(httpRequest.getProperty("maxPoNoLength")).intValue();
	    int split = getPrefixIndex(customerPoNoStart);
	    
	    df = doDecimalFormat(maxPoNoLength - split);
	    if(StringUtils.hasText(prefixCustomerPoNo)){
		df = doDecimalFormat(maxPoNoLength - prefixCustomerPoNo.length());
	    }
	    
	    log.info("brandCode = " + brandCode);
	    log.info("salesOrderDate = " + salesOrderDate);
	    log.info("posMachineCode = " + posMachineCode);
	    log.info("customerPoNoStart = " + customerPoNoStart);
	    log.info("customerPoNoEnd = " + customerPoNoEnd);
	    log.info("isChangeNumber = " + isChangeNumber);
	    log.info("accessNumber = " + accessNumber);
	    log.info("prefixCustomerPoNo = " + prefixCustomerPoNo);
	    log.info("split = " + split);
	    log.info("maxPoNoLength = " + maxPoNoLength);
	    
	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
	    findObjs.put(" and ORDER_NO NOT LIKE :TMP", "TMP%");
	    findObjs.put(" and SALES_ORDER_DATE  = :salesOrderStartDate",
		    salesOrderDate);
	    findObjs.put(" and POS_MACHINE_CODE = :posMachineCode",
		    posMachineCode);
	    findObjs.put(" and CUSTOMER_PO_NO >= :customerPoNoStart",
		    customerPoNoStart);
	    findObjs.put(" and CUSTOMER_PO_NO <= :customerPoNoEnd",
		    customerPoNoEnd);
	    // ==============================================================
	    
	    if(salesOrderDate!=null){
	    	Map headsMap = soSalesOrderHeadDAO.search("SoSalesOrderHead",
	    		    findObjs, "order by customerPoNo ", iSPage, iPSize,
	    		    BaseDAO.QUERY_SELECT_RANGE);
	    	    List<SoSalesOrderHead> heads = (List<SoSalesOrderHead>) headsMap
	    		    .get(BaseDAO.TABLE_LIST);
	    	    if (heads != null && heads.size() > 0) {
	    		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
	    		Long maxIndex = (Long) soSalesOrderHeadDAO.search(
	    			"SoSalesOrderHead", "count(HEAD_ID) as rowCount",
	    			findObjs, "order by customerPoNo ", iSPage, iPSize,
	    			BaseDAO.QUERY_RECORD_COUNT).get(
	    			BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
	    		for (SoSalesOrderHead head : heads) {
	    		    this.setHeadNames(head);

	    		    if ("true".equals(isChangeNumber)) {
	    			log.info("head.getHeadId() = " + head.getHeadId());
	    			log.info("head.getCustomerPoNo() = "
	    				+ head.getCustomerPoNo());
	    			log.info("head.getCustomerPoNo().substring(split) = "
	    				+ head.getCustomerPoNo().substring(split));
	    			Long customerCode = NumberUtils.getLong(head
	    				.getCustomerPoNo().substring(split));
	    			log.info("head.getCustomerPoNo().substring(0,split) = "
	    				+ head.getCustomerPoNo().substring(0, split));
	    			String prefix = null;
	    			if (StringUtils.hasText(prefixCustomerPoNo)) {
	    			    prefix = prefixCustomerPoNo;
	    			} else {
	    			    prefix = head.getCustomerPoNo().substring(0, split);
	    			}
	    			log.info("prefix = " + prefix);
	    			log
	    				.info("prefix+(df.format(customerCode+accessNumber)) = "
	    					+ prefix
	    					+ (df.format(customerCode
	    						+ accessNumber)));
	    			head.setCustomerPoNo(prefix
	    				+ (df.format(customerCode + accessNumber)));
	    			log.info("end");
	    		    }
	    		}

	    		result.add(AjaxUtils.getAJAXPageData(httpRequest,
	    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
	    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, heads,
	    			gridDatas, firstIndex, maxIndex));
	    	    } else {
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
	    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
	    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, map,
	    			gridDatas));
	    	    }
	    } else {
	    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
	    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
	    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, map,
	    			gridDatas));
	    	    }	    
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的選單功能查詢失敗！");
	}
    }

    public void setHeadNames(SoSalesOrderHead head) throws Exception {
	if (StringUtils.hasText(head.getCustomerCode())) {
	    BuCustomerWithAddressView buCustomerWithAddressView = buCustomerWithAddressViewService
		    .findCustomerByType(head.getBrandCode(), head
			    .getCustomerCode(), "customerCode", null);
	    if (buCustomerWithAddressView != null) {
		head.setCustomerName(buCustomerWithAddressView.getShortName());
	    }
	}
	if (StringUtils.hasText(head.getSuperintendentCode())) {
	    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewService
		    .findbyBrandCodeAndEmployeeCode(head.getBrandCode(), head
			    .getSuperintendentCode());
	    if (employeeWithAddressView != null) {
		head.setSuperintendentName(employeeWithAddressView
			.getChineseName());
	    }
	}
	if (StringUtils.hasText(head.getStatus())) {
	    head.setStatusName(OrderStatus.getChineseWord(head.getStatus()));
	}
	if (StringUtils.hasText(head.getLastUpdatedBy())) {
	    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewService
		    .findbyBrandCodeAndEmployeeCode(head.getBrandCode(), head
			    .getLastUpdatedBy());
	    if (employeeWithAddressView != null) {
		head.setLastUpdatedBy(employeeWithAddressView.getChineseName());
	    }
	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception 
    {
    	String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }

    public List<Properties> getSearchSelection(Map parameterMap)
    throws FormException, Exception {
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
     * 銷貨單轉出貨單(不跑流程)
     * 
     * @param soHeadId
     * @throws Exception
     */
    public void executeConvertToDelivery(Long soHeadId) throws Exception {
	log.info("executeConvertToDelivery");
	try {
	    SoSalesOrderHead salesOrderHeadPO = findSoSalesOrderHeadById(soHeadId);
	    if (salesOrderHeadPO == null) {
		throw new ValidationErrorException("查無銷貨單主鍵：" + soHeadId
			+ "的資料！");
	    }
	    ImDeliveryHead deliveryHead = imDeliveryMainService
		    .produceDeliveryHeadAndSetRelation(soHeadId);
	    salesOrderHeadPO.setStatus(OrderStatus.FINISH);
	    soSalesOrderHeadDAO.update(salesOrderHeadPO);
	    modifyImDeliveryStatusAndShipData(deliveryHead, OrderStatus.FINISH);
	} catch (Exception ex) {
	    log.error("銷售單轉出貨單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷售單轉出貨單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 更新出貨單狀態及出貨資料(不跑流程)
     * 
     * @param deliveryHead
     * @param status
     * @throws Exception
     */
    private void modifyImDeliveryStatusAndShipData(ImDeliveryHead deliveryHead,
	    String status) throws Exception {
	log.info("modifyImDeliveryStatusAndShipData");
	deliveryHead.setShipDate(deliveryHead.getScheduleShipDate());
	deliveryHead.setTotalOriginalShipAmount(deliveryHead
		.getTotalOriginalSalesAmount());
	deliveryHead.setTotalActualShipAmount(deliveryHead
		.getTotalActualSalesAmount());
	deliveryHead.setShipTaxAmount(deliveryHead.getTaxAmount());
	deliveryHead.setStatus(status);
	deliveryHead.setLastUpdateDate(new Date());
	imDeliveryHeadDAO.update(deliveryHead);
	List deliveryLines = imDeliveryLineDAO.findByProperty("ImDeliveryLine",
		"salesOrderId", deliveryHead.getSalesOrderId());
	if (deliveryLines != null && deliveryLines.size() > 0) {
	    for (int i = 0; i < deliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines
			.get(i);
		deliveryLine.setShipQuantity(deliveryLine.getSalesQuantity());
		deliveryLine.setOriginalShipAmount(deliveryLine
			.getOriginalSalesAmount());
		deliveryLine.setActualShipAmount(deliveryLine
			.getActualSalesAmount());
		deliveryLine.setShipTaxAmount(deliveryLine.getTaxAmount());
		imDeliveryLineDAO.update(deliveryLine);
	    }
	} else {
	    throw new NoSuchDataException("依據銷貨單主檔主鍵："
		    + deliveryHead.getSalesOrderId() + "查無相關出貨單明細檔資料！");
	}
    }

    /**
     * 更新銷貨單主檔及明細檔的Status
     * 
     * @param SalesOrderHeadId
     * @param status
     * @return String
     * @throws Exception
     */
    public String updateSoSalesOrderStatus(Long salesOrderHeadId,
	    String status, String loginUser) throws Exception {
	try {
	    SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) soSalesOrderHeadDAO
		    .findByPrimaryKey(SoSalesOrderHead.class, salesOrderHeadId);
	    if (salesOrderHead != null) {
		salesOrderHead.setStatus(status);
		if (!StringUtils.hasText(loginUser)) {
		    loginUser = salesOrderHead.getLastUpdatedBy();
		}
		modifySoSalesOrder(salesOrderHead, loginUser);
		return "Success";
	    } else {
		throw new NoSuchDataException("銷貨單主檔查無主鍵：" + salesOrderHeadId
			+ "的資料！");
	    }
	} catch (Exception ex) {
	    log.error("更新銷貨單狀態時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷貨單狀態時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * T2的POS上傳作業，刪除原銷售、出貨資料並進行第一次存檔
     * 
     * @param entityBeans
     * @param parameterMap
     * @throws Exception
     */
    public void saveT2PosNoValidate(List entityBeans, HashMap parameterMap)
	    throws Exception {
	log.info("====ENTER soSalesOrderMainService.saveT2PosNoValidate===== POS重傳時刪除相關銷售、出貨資料 =========");
	// ================POS重傳時刪除相關銷售、出貨資料=================
	String actualbrandCode = (String) parameterMap.get("brandCode");
	String posMachineCode = (String) parameterMap.get("posMachineCode");
	Date actualSalesDate = (Date) parameterMap.get("actualSalesDate");
	String opUser = (String) parameterMap.get("opUser");
	
	System.out.println(actualbrandCode+","+posMachineCode+","+actualSalesDate);
	
	String organizationCode = "TM";
	try {
	    organizationCode = UserUtils.getOrganizationCodeByBrandCode(actualbrandCode);
	    if (!StringUtils.hasText(organizationCode)) {
		throw new ValidationErrorException("品牌代號(" + actualbrandCode + ")所屬的組織代號為空值！");
	    }
	    parameterMap.put("organizationCode", organizationCode);
	    List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findT2SOPByProperty(parameterMap);
	    if (salesOrderHeads != null) {
		for (SoSalesOrderHead salesOrderHead : salesOrderHeads) {
		    String identification = MessageStatus.getIdentificationMsg(
			    salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), salesOrderHead.getOrderNo());
		    String currentStatus = salesOrderHead.getStatus();
		    if (!OrderStatus.SIGNING.equals(currentStatus)
			    && !OrderStatus.UNCONFIRMED.equals(currentStatus)
			    && !OrderStatus.VOID.equals(currentStatus)
			    && !OrderStatus.SAVE.equals(currentStatus)) {
			throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
		    } else if (OrderStatus.SIGNING.equals(currentStatus)) {
			revertToOriginallyAvailableQuantity(salesOrderHead, organizationCode, opUser);
		    }
		    soSalesOrderHeadDAO.delete(salesOrderHead);
		}
	    }
	} catch (Exception ex) {
	    log.error("刪除品牌代號(" + actualbrandCode + ")、pos機碼(" + posMachineCode
		    + ")、銷售日期(" + DateUtils.format(actualSalesDate)
		    + ")的銷售、出貨資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除品牌代號(" + actualbrandCode + ")、pos機碼("
		    + posMachineCode + ")、銷售日期("
		    + DateUtils.format(actualSalesDate) + ")的銷售、出貨資料失敗！"
		    + ex.toString());
	}
	// ========================================================================
	log
		.error("==== saveT2PosNoValidate===== 取單別序號，insert至SO ========= START FOR LOOP");
	for (int index = 0; index < entityBeans.size(); index++) {
	    SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) entityBeans
		    .get(index);
	    // 取單別序號，insert至SO
	    String serialNo = "";
	    if ("SOP".equals(soSalesOrderHead.getOrderTypeCode())) {
		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead
			.getBrandCode(), soSalesOrderHead.getOrderTypeCode(),
			soSalesOrderHead.getShopCode(), soSalesOrderHead
				.getPosMachineCode());
	    } else {
		serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead
			.getBrandCode(), soSalesOrderHead.getOrderTypeCode());
	    }


	    if (!serialNo.equals("unknow")) {
		soSalesOrderHead.setOrderNo(serialNo);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
	    }

	    soSalesOrderHeadDAO.save(soSalesOrderHead);
	    
	}

	log
		.info("====LEAVE soSalesOrderMainService.saveT2PosNoValidate===== POS重傳時刪除相關銷售、出貨資料 =========");
    }

    /**
     * T2的POS上傳作業，將作廢的單子庫存歸還
     * 
     * @param entityBeans
     * @param parameterMap
     * @throws Exception
     */
    public void saveT2Pos2Void(LinkedHashSet customerPoNos,
	    LinkedHashSet salesOrderDates, String loginUser) throws Exception {
	try {
	    String[] customerPoNo = new String[customerPoNos.size()];
	    Date[] salesOrderDate = new Date[salesOrderDates.size()];

	    int it = 0;
	    for (Iterator iterator = customerPoNos.iterator(); iterator
		    .hasNext();) {
		customerPoNo[it++] = (String) iterator.next();
	    }

	    it = 0;
	    for (Iterator iterator = salesOrderDates.iterator(); iterator
		    .hasNext();) {
		salesOrderDate[it++] = (Date) iterator.next();
	    }

	    for (int i = 0; i < customerPoNo.length; i++) {
		List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO
			.findByProperty("SoSalesOrderHead", null,
				" and customerPoNo = ? and salesOrderDate = ?",
				new Object[] { customerPoNo[i],
					salesOrderDate[i] }, null);
		if (salesOrderHeads != null) {
		    for (SoSalesOrderHead salesOrderHead : salesOrderHeads) {
			String identification = MessageStatus
				.getIdentificationMsg(salesOrderHead
					.getBrandCode(), salesOrderHead
					.getOrderTypeCode(), salesOrderHead
					.getOrderNo());
			String currentStatus = salesOrderHead.getStatus();
			if (!OrderStatus.SIGNING.equals(currentStatus)
				&& !OrderStatus.UNCONFIRMED
					.equals(currentStatus)
				&& !OrderStatus.VOID.equals(currentStatus)) {
			    throw new ValidationErrorException(identification
				    + "的狀態為"
				    + OrderStatus.getChineseWord(salesOrderHead
					    .getStatus()) + "不可執行反確認！");
			} else if (OrderStatus.SIGNING.equals(currentStatus)) {
			    revertToOriginallyAvailableQuantity(salesOrderHead, "TM", loginUser);
			}
			salesOrderHead.setStatus(OrderStatus.VOID);
			salesOrderHead.setLastUpdatedBy(loginUser);
			salesOrderHead.setLastUpdateDate(new Date());
			soSalesOrderHeadDAO.update(salesOrderHead);
		    }
		}
	    }
	} catch (Exception ex) {
	    log.error("作廢銷售、出貨資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("作廢銷售、出貨資料時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * T2的POS上傳作業，取商品資訊並執行檢核
     * 
     * @param entityBeans
     * @param parameterMap
     * @throws Exception
     */
    public List updateT2PosData(String processName, Date executeDate,
	    String uuid, Long salesHeadId, HashMap parameterMap,
	    List mailContents) throws Exception {
	log.info("============enter updateT2PosData=======================T2的POS上傳作業，取商品資訊並執行檢核===========");
	String opUser = (String) parameterMap.get("opUser");
	// 重新計算單身，檢核重新計算後的金額與原實際銷售金額是否相同、檢核保稅商品是否有完整報單、批號資訊；完稅商品有批號資訊
	SoSalesOrderHead salesOrderHeadPO = findById(salesHeadId);
	if (salesOrderHeadPO == null) {
	    throw new NoSuchObjectException("查無主鍵(" + salesHeadId + ")的銷貨單資料！");
	}
	String identification = MessageStatus.getIdentificationMsg(
		salesOrderHeadPO.getBrandCode(), salesOrderHeadPO.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
	String identification2 = MessageStatus.getIdentification(
		salesOrderHeadPO.getBrandCode(), salesOrderHeadPO.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
	
	log.info("============enter countTotalAmountForT2Pos=======================");
	List assemblyMsg = countTotalAmountForT2Pos(processName, executeDate, uuid, identification2, opUser, salesOrderHeadPO);
	log.info("============leave countTotalAmountForT2Pos=======================");
	soSalesOrderHeadDAO.update(salesOrderHeadPO);
	// 若單據有任何錯誤時
	if (assemblyMsg.size() != 0) {
	    mailContents.add(identification);
	}
	log.info("============LEAVE soSalesOrderMainService.updateT2PosData======T2的POS上傳作業，取商品資訊並執行檢核===========");
	return assemblyMsg;
    }

    /**
     * 計算所有Item的金額並檢核保稅商品是否有完整報單、批號資訊；完稅商品有批號資訊(T2)
     * 
     * @param processName
     * @param executeDate
     * @param uuid
     * @param identification
     * @param opUser
     * @param orderHead
     * @return List
     * @throws FormException
     * @throws Exception
     */
    public List countTotalAmountForT2Pos(String processName, Date executeDate, String uuid, String identification, String opUser, SoSalesOrderHead orderHead) throws FormException, Exception {
    	log.info("==========enter countTotalAmountForT2Pos================計算所有Item的金額並檢核保稅商品是否有完整報單、批號資訊；完稅商品有批號資訊(T2)=========");
    	String errorMsg = null;
    	List assemblyMsg = new ArrayList(0);
    	try {
    		Double totalActualSalesAmount = (orderHead.getTotalActualSalesAmount() == null) ? 0D : orderHead.getTotalActualSalesAmount();
    		List<SoSalesOrderItem> salesOrderItems = orderHead.getSoSalesOrderItems();
    		Double latestTotalActualSalesAmount = 0D;
    		
    		BuOrderTypeId buOrderTypeId = new BuOrderTypeId(orderHead.getBrandCode(), orderHead.getOrderTypeCode());
    		BuOrderType buOrderType = buOrderTypeService.findById(buOrderTypeId);
    		
    		if (salesOrderItems != null && salesOrderItems.size() > 0) {
    			//===========記錄pos轉入的金額=========
    			HashMap posAmountMap = recordPOSAmount(salesOrderItems);
    			// 計算展完報單後，相同品號+pos_seq的筆數有多少
    			HashMap posCountMap = calculatePOSCount(salesOrderItems);
    			for (SoSalesOrderItem salesOrderItem : salesOrderItems) {
    				// 重新計算各明細的金額，若有差異則更新到最後一筆同品號+同pos_seq的金額欄位裏
    				if ("34".equals(orderHead.getPosMachineCode())) {
    					updateOrderItemActualAmount(orderHead, salesOrderItem, posAmountMap, posCountMap);
    				}
    				refreshItemRelationAmountForT2POS(buOrderType, salesOrderItem, orderHead); // 重新計算單身與單頭的金額是否相同
    				salesOrderItem.setDiscountRate( getDiscountRate(buOrderType, salesOrderItem.getActualSalesAmount(), salesOrderItem.getOriginalSalesAmount()));
    				salesOrderItem.setOriginalForeignUnitPrice(salesOrderItem.getOriginalUnitPrice());
    				salesOrderItem.setActualForeignUnitPrice(salesOrderItem.getActualUnitPrice());
    				salesOrderItem.setOriginalForeignSalesAmt(salesOrderItem.getOriginalSalesAmount());
    				salesOrderItem.setActualForeignSalesAmt(salesOrderItem.getActualSalesAmount());
    				latestTotalActualSalesAmount += salesOrderItem.getActualSalesAmount();
    				ImItem imItemPO = imItemDAO.findItem(orderHead.getBrandCode(), salesOrderItem.getItemCode());
    				if (imItemPO != null) {
    					salesOrderItem.setIsTax(imItemPO.getIsTax());
    					salesOrderItem.setIsServiceItem(imItemPO.getIsServiceItem());
    					salesOrderItem.setIsComposeItem(imItemPO.getIsComposeItem());
    					salesOrderItem.setAllowMinusStock(imItemPO.getAllowMinusStock()); // 是否允許負庫存
    					if (!StringUtils.hasText(salesOrderItem.getIsTax())) {
    						errorMsg = "POS銷貨單(" + identification + ")其品號("
    						+ salesOrderItem.getItemCode() + ")的稅別為空值！";
    						assemblyMsg.add(errorMsg);
    						siProgramLogAction.createProgramLog(PROGRAM_ID,
    								MessageStatus.LOG_ERROR, identification,
    								errorMsg, opUser);
    					} else {
    						// 檢核保稅商品是否有完整報單、批號資訊；完稅商品有批號資訊
    						if (!StringUtils.hasText(salesOrderItem.getLotNo())) {
    							errorMsg = "POS銷貨單(" + identification + ")其品號("
    							+ salesOrderItem.getItemCode()
    							+ ")的批號資訊不完整！";
    							assemblyMsg.add(errorMsg);
    							siProgramLogAction.createProgramLog(PROGRAM_ID,
    									MessageStatus.LOG_ERROR,
    									identification, errorMsg, opUser);
    						} else if ("F".equalsIgnoreCase(salesOrderItem
    								.getIsTax())) {
    							if (!StringUtils.hasText(salesOrderItem
    									.getImportDeclNo())
    									|| salesOrderItem.getImportDeclSeq() == null) {
    								errorMsg = "POS銷貨單(" + identification
    								+ ")其品號("
    								+ salesOrderItem.getItemCode()
    								+ ")的報單資訊不完整！";
    								assemblyMsg.add(errorMsg);
    								siProgramLogAction.createProgramLog(
    										PROGRAM_ID,
    										MessageStatus.LOG_ERROR,
    										identification, errorMsg, opUser);
    							}
    						}
    					}
    				} else {
    					errorMsg = "POS銷貨單(" + identification + ")其品號("
    					+ salesOrderItem.getItemCode() + ")不存在！";
    					assemblyMsg.add(errorMsg);
    					siProgramLogAction.createProgramLog(PROGRAM_ID,
    							MessageStatus.LOG_ERROR, identification,
    							errorMsg, opUser);
    				}
    			}
    			// ===========檢核重新計算後的金額與原實際銷售金額是否相同=====================
    			if (assemblyMsg.size() == 0
    					&& latestTotalActualSalesAmount.doubleValue() != totalActualSalesAmount
    					.doubleValue()) {
    				errorMsg = "POS銷貨單(" + identification + ")擷取報單並重新計算後的金額("
    				+ latestTotalActualSalesAmount.doubleValue()
    				+ ")與原金額(" + totalActualSalesAmount.doubleValue()
    				+ ")不符！";
    				assemblyMsg.add(errorMsg);
    				siProgramLogAction.createProgramLog(PROGRAM_ID,
    						MessageStatus.LOG_ERROR, identification, errorMsg,
    						opUser);
    			}
    		}
    		log.info("====LEAVE soSalesOrderMainService.countTotalAmountForT2Pos====計算所有Item的金額並檢核保稅商品是否有完整報單、批號資訊；完稅商品有批號資訊(T2)=========");
    		return assemblyMsg;
    	} catch (Exception ex) {
    		log.error("POS銷售單金額統計時發生錯誤，原因：" + ex.toString());
    		throw new Exception("POS銷售單金額統計時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    /**
     * 匯入銷貨商品明細
     * 
     * @param headId
     * @param vipPromotionCode
     * @param taxType
     * @param taxRate
     * @param salesOrderItems
     * @throws Exception
     */
    public void executeImportItems(Long headId, String vipPromotionCode,
	    String taxType, Double taxRate, Double exchangeRate,
	    String warehouseCode, List salesOrderItems) throws Exception {
	try {
	    SoSalesOrderHead salesOrderHead = findSoSalesOrderHeadById(headId);
	    if (salesOrderHead == null) {
		throw new NoSuchObjectException("查無銷貨單主鍵：" + headId + "的資料");
	    }

	    if (salesOrderItems != null && salesOrderItems.size() > 0) {
		for (int i = 0; i < salesOrderItems.size(); i++) {
		    SoSalesOrderItem salesOrderItem = (SoSalesOrderItem) salesOrderItems
			    .get(i);
		    if (StringUtils.hasText(vipPromotionCode)) {
			salesOrderItem.setVipPromotionCode(vipPromotionCode);
		    }
		    if (StringUtils.hasText(taxType)) {
			salesOrderItem.setTaxType(taxType);
		    }
		    if (taxRate != null) {
			salesOrderItem.setTaxRate(taxRate);
		    }
		    salesOrderItem.setWarehouseCode(warehouseCode);
		    salesOrderItem.setActualUnitPrice(NumberUtils
			    .getDouble(salesOrderItem
				    .getActualForeignUnitPrice())
			    * exchangeRate);
		}
		salesOrderHead.setSoSalesOrderItems(salesOrderItems);
	    } else {
		salesOrderHead.setSoSalesOrderItems(new ArrayList(0));
	    }
	    soSalesOrderHeadDAO.update(salesOrderHead);
	} catch (Exception ex) {
	    log.error("銷貨商品明細匯入時發生錯誤，原因：" + ex.toString());
	    throw new Exception("銷貨商品明細匯入時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 初始化 bean 額外顯示欄位
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitialAdvance(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    Long lineId = NumberUtils.getLong((String) PropertyUtils
		    .getProperty(otherBean, "lineId"));
	    // Long headId =
	    // NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean,
	    // "headId"));
	    String brandCode = (String) PropertyUtils.getProperty(otherBean,
		    "loginBrandCode");
	    String warehouseCode = (String) PropertyUtils.getProperty(
		    otherBean, "warehouseCode");
	    String taxType = (String) PropertyUtils.getProperty(otherBean,
		    "taxType");
	    Double taxRate = NumberUtils.getDouble((String) PropertyUtils
		    .getProperty(otherBean, "taxRate"));
	    Double discountRate = NumberUtils.getDouble((String) PropertyUtils
		    .getProperty(otherBean, "discountRate"));

	    SoSalesOrderItem item = (SoSalesOrderItem) soSalesOrderItemDAO
		    .findById("SoSalesOrderItem", lineId);
	    if (item == null) {
		item = new SoSalesOrderItem();
		item.setWarehouseCode(warehouseCode);
		item.setTaxType(taxType);
		item.setTaxRate(taxRate);
		item.setDiscountRate(discountRate);
	    } else {
		ImItem imItem = imItemService.findById(item.getItemCode());
		item.setTaxType(taxType);
		item.setTaxRate(taxRate);
		if (imItem != null) {
		    resultMap.put("itemCName", AjaxUtils.getPropertiesValue(
			    imItem.getItemCName(), ""));
		    resultMap.put("supplierItemCode", AjaxUtils
			    .getPropertiesValue(imItem.getSupplierItemCode(),
				    ""));
		    resultMap.put("standardPurchaseCost", AjaxUtils
			    .getPropertiesValue(imItem
				    .getStandardPurchaseCost(), ""));
		    resultMap.put("itemDiscountType", AjaxUtils
			    .getPropertiesValue(imItem.getVipDiscount(), ""));
		} else {
		    if (StringUtils.hasText(item.getItemCode())) {
			resultMap.put("itemCName", "查無資料");
		    }
		}
		if (StringUtils.hasText(item.getWarehouseCode())) {
		    ImWarehouse imWarehouse = imWarehouseService
			    .findByBrandCodeAndWarehouseCode(brandCode, item
				    .getWarehouseCode(), null);
		    if (imWarehouse != null)
			resultMap.put("warehouseName", AjaxUtils
				.getPropertiesValue(imWarehouse
					.getWarehouseName(), ""));
		    else
			resultMap.put("warehouseName", "查無資料");
		}
	    }

	    resultMap.put("form", item);
	    return resultMap;
	} catch (Exception ex) {
	    log.error("銷貨單進階輸入初始化失敗，原因：" + ex.toString());
	    throw new Exception("銷貨單進階輸入始化失敗，原因：" + ex.getMessage());
	}
    }

    public List<Properties> findInitialCommonAdvance(Properties httpRequest)
	    throws Exception {
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    List allTaxType = buCommonPhraseService.getCommonPhraseLinesById(
		    "TaxType", false);
	    allTaxType = AjaxUtils.produceSelectorData(allTaxType, "lineCode",
		    "name", false, false);
	    properties.setProperty("allTaxType", AjaxUtils
		    .parseSelectorData(allTaxType));
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("初始Master發生錯誤，原因：" + ex.toString());
	    throw new Exception("初始Master發生錯誤，原因：" + ex.getMessage());
	}
    }

    private Double getDiscountRate(BuOrderType buOrderType, Double actual, Double original) {
    	if (original == 0D) {
    		return 100D;
    	} else if (actual != null && actual != 0D && original != null && original != 0D) {
    		if ("F".equals(buOrderType.getOrderCondition()))
    			return CommonUtils.round(actual / original * 100, 2);
    		else
    			return CommonUtils.round(actual / original * 100, 1);
    	} else {
    		return 0D;
    	}
    }

    /**
     * T2的POS上傳作業，扣庫存並將狀態改為SIGNING
     * 
     * @param entityBeans
     * @param parameterMap
     * @throws Exception
     */
    public void updateT2PosOnHand(String processName, Date executeDate,
	    String uuid, Long salesHeadId, HashMap parameterMap,
	    List mailContents, List assemblyMsg) throws Exception {
	log.info("====enter updateT2PosOnHand=====");
	String opUser = (String) parameterMap.get("opUser");
	String organizationCode = (String) parameterMap.get("organizationCode");
	SoSalesOrderHead salesOrderHeadPO = findById(salesHeadId);
	if (salesOrderHeadPO == null) {
	    throw new NoSuchObjectException("查無主鍵(" + salesHeadId + ")的銷貨單資料！");
	}
	String identification = MessageStatus.getIdentification(
		salesOrderHeadPO.getBrandCode(), salesOrderHeadPO
			.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
	// 扣庫存轉出貨明細
	bookAvailableQuantityForT2(salesOrderHeadPO, organizationCode, opUser, assemblyMsg);
	
	//for 儲位用
    if(imStorageAction.isStorageExecute(salesOrderHeadPO)){
	//異動儲位庫存
	imStorageService.updateStorageOnHandBySource(salesOrderHeadPO, OrderStatus.FINISH, PROGRAM_ID, null, false);
    }
    
	if (assemblyMsg.size() != 0) {
	    throw new Exception("POS銷貨單(" + identification + ")預扣庫存量失敗！");
	} else {
	    salesOrderToDeliveryForT2(salesOrderHeadPO, opUser);
	    salesOrderHeadPO.setStatus(OrderStatus.SIGNING);
	    soSalesOrderHeadDAO.update(salesOrderHeadPO);
	}
	log.info("====leave updateT2PosOnHand=====");
    }

    public List<Properties> executeFindCM(Properties httpRequest)
	    throws Exception {
	System.out.println("==executeFindCM==");
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try {
	    // ======================取得複製時所需的必要資訊========================
	    String exportDeclNo = httpRequest.getProperty("exportDeclNo");

	    // ================find CMData==================
	    List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", exportDeclNo);
	    if (cmDeclarationHeads != null && cmDeclarationHeads.size() > 0) {
		CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
		// properties.setProperty("ExportDeclDate", AjaxUtils
		// .getPropertiesValue(DateUtils.format(cmDeclarationHead
		// .getDeclDate(), "yyyy/MM/dd"), ""));
		properties.setProperty("ExportDeclDate", AjaxUtils.getPropertiesValue(DateUtils.format(cmDeclarationHead.getImportDate(), "yyyy/MM/dd"), ""));
		properties.setProperty("ExportDeclType", cmDeclarationHead.getDeclType());
	    }
	    result.add(properties);
	    return result;
	} catch (Exception ex) {
	    log.error("查詢報關單發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 重新計算Item的原銷售金額、實際售價、實際銷售金額、稅金
     * 
     * @param orderItem
     */
    private void refreshItemRelationAmountForT2POS(BuOrderType buOrderType, SoSalesOrderItem orderItem, SoSalesOrderHead orderHead) {
    	Double originalSalesAmount = 0D;
    	Double actualSalesAmount = 0D;
    	Double taxAmount = 0D;

    	Double originalUnitPrice = NumberUtils.getDouble(orderItem.getOriginalUnitPrice()); // 原始售價
    	Double actualUnitPrice = NumberUtils.getDouble(orderItem.getActualUnitPrice()); // 實際售價
    	Double quantity = orderItem.getQuantity(); // 數量
    	String taxType = orderItem.getTaxType(); // 稅別
    	Double taxRate = orderItem.getTaxRate(); // 稅率

		originalSalesAmount = CommonUtils.round(originalUnitPrice* quantity, 0); // 原銷售金額
		if ("34".equals(orderHead.getPosMachineCode())) {
    		actualSalesAmount = orderItem.getActualSalesAmount();
    	}else if (!"34".equals(orderHead.getPosMachineCode())) {
			actualSalesAmount = CommonUtils.round(actualUnitPrice * quantity, 0); // 實際銷售金額
		}
		
		taxAmount = calculateTaxAmount(buOrderType, taxType, taxRate, actualSalesAmount);
    	orderItem.setOriginalSalesAmount(originalSalesAmount);
    	if (!"34".equals(orderHead.getPosMachineCode())) {
    		orderItem.setActualSalesAmount(actualSalesAmount);
    	}
    	orderItem.setTaxAmount(taxAmount);
    }

    /**
     * 取得CC開窗URL字串
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReportConfig(Map parameterMap) throws Exception {
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String) PropertyUtils.getProperty(otherBean,
		    "brandCode");
	    String orderTypeCode = (String) PropertyUtils.getProperty(
		    otherBean, "orderTypeCode");
	    String orderNo = (String) PropertyUtils.getProperty(otherBean,
		    "orderNo");
	    String startOrderNo = (String) PropertyUtils.getProperty(otherBean,
	    	"startOrderNo");
	    String endOrderNo = (String) PropertyUtils.getProperty(otherBean,
    		"endOrderNo");
	    String loginEmployeeCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginEmployeeCode");
	    Map returnMap = new HashMap(0);
	    Map parameters = new HashMap(0);
	    if ("T2".equals(brandCode)) {
		// CC後面要代的參數使用parameters傳遞
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", "");
		parameters.put("prompt3", "");
		parameters.put("prompt4", startOrderNo);
		parameters.put("prompt5", endOrderNo);
	    }else {
		parameters.put("prompt0", brandCode);
		parameters.put("prompt1", orderTypeCode);
		parameters.put("prompt2", orderNo);
		parameters.put("prompt3", orderNo);
	    }

	    String reportUrl = SystemConfig.getReportURL(brandCode,
		    orderTypeCode, loginEmployeeCode, parameters);
	    returnMap.put("reportUrl", reportUrl);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	} catch (IllegalAccessException iae) {
	    log.info(iae.getMessage());
	    throw new IllegalAccessException(iae.getMessage());
	} catch (InvocationTargetException ite) {
	    log.info(ite.getMessage());
	    throw new InvocationTargetException(ite, ite.getMessage());
	} catch (NoSuchMethodException nse) {
	    log.info(nse.getMessage());
	    throw new NoSuchMethodException("NoSuchMethodException:"
		    + nse.getMessage());
	}
    }

    /**
     * 記錄POS明細金額
     */
    private HashMap recordPOSAmount(List<SoSalesOrderItem> soSalesOrderItem){	
    	log.info("====ENTER====記錄POS金額======recordPOSAmount====");
    	HashMap orderItemAmountMap = new HashMap();
    	for(int i = 0; i < soSalesOrderItem.size(); i++){
    		SoSalesOrderItem orderItem = soSalesOrderItem.get(i);
    		if(orderItemAmountMap.get(orderItem.getItemCode() + "#" + orderItem.getPosSeq()) == null){
    			orderItemAmountMap.put(orderItem.getItemCode() + "#" + orderItem.getPosSeq(), orderItem.getPosActualSalesAmount());
    		}
    		log.info("品號#POS_SEQ = " + orderItem.getItemCode() + "#" + orderItem.getPosSeq() + "," 
    				+ "POS金額:" + orderItemAmountMap.get(orderItem.getItemCode() + "#" + orderItem.getPosSeq()));
    	}
    	log.info("====LEAVE====記錄POS金額======recordPOSAmount====");
    	return orderItemAmountMap;
    }
    
    /**
     * 計算展完報單後，相同品號+pos_seq的筆數有多少
     */
    private HashMap calculatePOSCount(List<SoSalesOrderItem> soSalesOrderItems) {
    	log.info("enter========計算展完報單後，相同品號+pos_seq的筆數有多少==========calculateOrderItemCnt=====================");
    	HashMap orderItemCntMap = new HashMap();
    	if (soSalesOrderItems != null && soSalesOrderItems.size() > 0) {
    		log.info("enter========計算展完報單後，相同品號+pos_seq的筆數有多少==========soSalesOrderItems.size()= " + soSalesOrderItems.size());
    		for (int i = 0; i < soSalesOrderItems.size(); i++) {
    			SoSalesOrderItem orderItem = soSalesOrderItems.get(i);
    			String mapKey = orderItem.getItemCode() + "#" + orderItem.getPosSeq();
    			if (orderItemCntMap.get(mapKey) == null) {
    				orderItemCntMap.put(mapKey, 1);
    			} else {
    				orderItemCntMap.put(mapKey, Integer.parseInt(""+ orderItemCntMap.get(mapKey)) + 1);
    			}
    			log.info("===so_line_id = " + orderItem.getLineId() + ",品號#POS_SEQ = " + mapKey + "," + "筆數:" + orderItemCntMap.get(mapKey));
    		}
    	}
    	log.info("leave=====計算展完報單後，相同品號+pos_seq的筆數有多少====calculateOrderItemCnt=====orderItemCntMap.size() = " + orderItemCntMap.size());
    	return orderItemCntMap;
    }

    
    /**
     * 重新計算明細的金額
     */
    private void updateOrderItemActualAmount(SoSalesOrderHead salesOrderHead, SoSalesOrderItem orderItem, HashMap posAmountMap, HashMap posCountMap) {
    	log.info("==enter=====重新計算明細的金額====soSalesOrderMainService.updateOrderItemActualAmount=====");

    	if (posAmountMap == null)
    		return;
    	if (posCountMap == null)
    		return;
    	
    	String mapKey = orderItem.getItemCode() + "#" + orderItem.getPosSeq();
    	Double posActuralAmt = 0D;
    	if (posAmountMap.get(mapKey) != null && !"".equals(posAmountMap.get(mapKey))) {
    		posActuralAmt = Double.parseDouble("" + posAmountMap.get(mapKey));
    	}

    	if (posCountMap.get(mapKey) != null) {
    		if (!posCountMap.get(mapKey).equals(1)) {
    			posCountMap.put(mapKey, Integer.parseInt("" + posCountMap.get(mapKey)) - 1);
    			Double originalSalesAmt = CommonUtils.round(orderItem.getActualUnitPrice() * orderItem.getQuantity(), 0);
    			posAmountMap.put(mapKey, posActuralAmt - originalSalesAmt);
    			orderItem.setActualSalesAmount(originalSalesAmt);
    		} else {
    			orderItem.setActualSalesAmount(posActuralAmt);
    		}
    	}

    	if (orderItem.getActualSalesAmount() == null) {
    		orderItem.setActualSalesAmount(0D);
    	}
    	
    	orderItem.setActualForeignSalesAmt(orderItem.getActualSalesAmount());
    	if(null != orderItem.getLineId())
    		soSalesOrderItemDAO.update(orderItem);

    	log.info("--SO_Head_id = " + salesOrderHead.getHeadId()
    			+ "--SO_Order_Item_LineId = " + orderItem.getLineId()
    			+ "---posActuralAmt = " + posActuralAmt + "==posAmountMap====="
    			+ posAmountMap.toString() + "posAmountMap.get(" + mapKey + ") = "
    			+ posAmountMap.get(mapKey) + "==posCountMap=====" + posCountMap.toString()
    			+ "posCountMap.get(" + mapKey + ") = " + posCountMap.get(mapKey));
    	log.info("==LEAVE=====重新計算明細的金額====soSalesOrderMainService.updateOrderItemActualAmount=====");
    }

    /**
     * 手動轉帳時，自動執行report的JOB.
     * 
     * @throws Exception
     */
    public void manualReportJob() throws Exception {

	System.out.println("===========BEGIN==manualReportJob==========");
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);

	    StringBuffer sql = new StringBuffer();

	    sql.append("{call BIA.JOB_RUNNING_PACKAGE.MANUAL_JOB}");
	    stmt = conn.prepareStatement(sql.toString());
	    stmt.execute();
	    conn.commit();
	    // Date currentDate = new Date();
	    // String actualDataDate = DateUtils.format(currentDate,
	    // DateUtils.C_DATA_PATTON_YYYYMMDD);
	    // System.out.println("actualDataDate = "+actualDataDate);
	    // stmt.setString(1, actualDataDate);
	    // stmt.setString(2, actualDataDate);
	    System.out.println("===========END==manualReportJob==========");

	} catch (Exception ex) {
	    if (conn != null && !conn.isClosed()) {
		conn.rollback();
	    }
	    throw new Exception(ex.getMessage());
	} finally {
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
    }

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    /**
     * 更新bean
     * 
     * @param head
     */
    public void update(SoSalesOrderHead head) {
	try {
	    soSalesOrderHeadDAO.update(head);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("e = " + e.toString());
	}
    }

    /**
     * 查詢POS_COMMAND的資料.
     * 
     * @throws Exception
     * 
     * @throws Exception
     */
    public Map qryPOSCommand(Long requestID) throws Exception {
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	Map posCommandMap = new HashMap();
    	try {

    		System.out.println("===========BEGIN============" + new Date());
    		conn = dataSource.getConnection();
    		String sql = " select * from POS_COMMAND where REQUEST_ID = ?  ";
    		stmt = conn.prepareStatement(sql);
    		stmt.setLong(1, requestID);
    		rs = stmt.executeQuery();
    		if (rs != null) {
    			// qry POS_SALES_ORDER_ITEM & POS_SALES_ORDER_PAYMENT
    			while (rs.next()) {
    				posCommandMap.put("batchId", rs.getString("BATCH_ID"));
    				posCommandMap.put("brandCode", rs.getString("BRAND_CODE"));
    				posCommandMap.put("dataId", rs.getString("DATA_ID"));
    				posCommandMap.put("machineCode", rs.getString("MACHINE_CODE"));
    				posCommandMap.put("numbers", rs.getString("NUMBERS"));
    				posCommandMap.put("dataType", rs.getString("DATA_TYPE"));
    			}
    		}
    		System.out.println("=========END========" + new Date());
    	} catch (Exception ex) {
    		throw new Exception(ex.getMessage());
    	} finally {
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
    	return posCommandMap;
    }

    private List qryPOSOrderItem(Map posCommandMap, String transactionSeqNo) {
	List posOrderItemList = new ArrayList(); 
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;  
	String dataId = (String) posCommandMap.get("dataId");
	HashMap paraM = new HashMap();
	String posMachineCode = "";
	Date salesOrderDate = new Date();
	
	String posSoItemSql = " select * from POS_SALES_ORDER_ITEM where DATA_ID = ? and TRANSACTION_SEQ_NO = ? ";
	try {
	    conn = dataSource.getConnection();
	    stmt = conn.prepareStatement(posSoItemSql);
	    stmt.setString(1, dataId);
	    stmt.setString(2, transactionSeqNo);
	    rs = stmt.executeQuery();
	    
	    if (rs != null) {
		while (rs.next()) {
		    Double originalUnitPrice = 0D;
		    if(rs.getString("ORIGINAL_UNIT_PRICE") != null){
			originalUnitPrice = Double.parseDouble(rs.getString("ORIGINAL_UNIT_PRICE"));
		    }
		    
		    Double quantity = 0D;
		    if(rs.getString("QUANTITY") != null){
			quantity = Double.parseDouble(rs.getString("QUANTITY"));
		    }
		    
		    Double discountRate = 0D;
		    if(rs.getString("DISCOUNT_RATE") != null){
			discountRate = Double.parseDouble(rs.getString("DISCOUNT_RATE"));
		    }

		    Double actualSalesAmount = 0D;
		    if(rs.getString("ACTUAL_SALES_AMOUNT") != null){
			actualSalesAmount = Double.parseDouble(rs.getString("ACTUAL_SALES_AMOUNT"));
		    }

		    Double discountAmount = 0D;
		    if(rs.getString("DISCOUNT_AMOUNT") != null){
			discountAmount = Double.parseDouble(rs.getString("DISCOUNT_AMOUNT"));
		    }

		    Double standardPrice = 0D;
		    if(rs.getString("STANDARD_PRICE") != null){
			standardPrice = Double.parseDouble(rs.getString("STANDARD_PRICE"));
		    }
		    
		    Double usedDiscountRate = 0D;
		    if(rs.getString("USED_DISCOUNT_RATE") != null){
			usedDiscountRate = Double.parseDouble(rs.getString("USED_DISCOUNT_RATE"));
		    }
		    posMachineCode = rs.getString("POS_MACHINE_CODE");	
		    salesOrderDate = rs.getDate("SALES_ORDER_DATE");
		    System.out.println("=====qryPOSOrderItem=====salesOrderDate = "+salesOrderDate);
		    TmpImportPosItemId tmpImportPosItemId = new TmpImportPosItemId();
		    TmpImportPosItem tmpImportPosItem = new TmpImportPosItem();
		    tmpImportPosItemId.setFileId(rs.getString("FILE_ID"));
		    tmpImportPosItemId.setSalesOrderDate(salesOrderDate);
		    tmpImportPosItemId.setPosMachineCode(posMachineCode);
		    tmpImportPosItemId.setTransactionSeqNo(rs.getString("TRANSACTION_SEQ_NO"));
		    tmpImportPosItemId.setSeq(rs.getLong("SEQ"));
		    tmpImportPosItem.setId(tmpImportPosItemId);	
		    tmpImportPosItem.setShopCode(rs.getString("SHOP_CODE"));
		    tmpImportPosItem.setItemCode(rs.getString("ITEM_CODE"));
		    tmpImportPosItem.setOriginalUnitPrice(originalUnitPrice);
		    tmpImportPosItem.setQuantity(quantity);
		    tmpImportPosItem.setDiscountRate(discountRate);
		    tmpImportPosItem.setActualSalesAmount(actualSalesAmount);
		    tmpImportPosItem.setDiscountAmount(discountAmount);
		    tmpImportPosItem.setSuperintendentCode(rs.getString("SUPERINTENDENT_CODE"));
		    tmpImportPosItem.setCustomerCode(rs.getString("CUSTOMER_CODE"));
		    tmpImportPosItem.setCustomerPoNo(rs.getString("CUSTOMER_PO_NO"));
		    tmpImportPosItem.setPeriod(rs.getString("PERIOD"));
		    tmpImportPosItem.setPassportNo(rs.getString("PASSPORT_NO"));
		    tmpImportPosItem.setFlightNo(rs.getString("FLIGHT_NO"));
		    tmpImportPosItem.setCountryCode(rs.getString("COUNTRY_CODE"));
		    tmpImportPosItem.setDepartureDate(rs.getDate("DEPARTURE_DATE"));
		    tmpImportPosItem.setVipTypeCode(rs.getString("VIP_TYPE_CODE"));
		    tmpImportPosItem.setStandardPrice(standardPrice);
		    tmpImportPosItem.setLadingNo(rs.getString("LADING_NO"));
//		    待加入
//		    + "," + rs.getString("VIP_CARD_ID")
//		    + "," + rs.getString("VIP_CARD_TYPE")
//		    + "," + rs.getString("PROMOTION_CARD_ID")
//		    + "," + rs.getString("PROMOTION_CARD_TYPE")
//		    + "," + rs.getString("MANAGER_CARD_ID")
//		    + "," + rs.getString("MANAGER_CARD_TYPE")
//		    + "," + rs.getString("VIP_CARD_DISCOUNT")
//		    + "," + rs.getString("PROMOTION_CARD__DISCOUNT")
//		    + "," + rs.getString("MANAGER_CARD_DISCOUNT")		    
		    tmpImportPosItem.setUsedIdentification(rs.getString("USED_IDENTIFICATION"));
		    tmpImportPosItem.setUsedCardId(rs.getString("USED_CARD_ID"));
		    tmpImportPosItem.setUsedCardType(rs.getString("USED_CARD_TYPE"));
		    tmpImportPosItem.setUsedDiscountRate(usedDiscountRate);
		    tmpImportPosItem.setItemDiscountType(rs.getString("ITEM_DISCOUNT_TYPE"));		    
		    tmpImportPosItem.setFileName(rs.getString("FILE_NAME"));
//		    tmpImportPosItem.setReserve1(fileKey);
		    tmpImportPosItem.setCreatedBy("SYS");
		    tmpImportPosItem.setCreationDate(new Date());	

		    tmpImportPosItemService.savePosItem(tmpImportPosItem);
		    
		    paraM.put("actualSalesDate", salesOrderDate);
		    paraM.put("posMachineCode", posMachineCode);
		    posOrderItemList.add(tmpImportPosItem);

		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
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

	paraM.put("posMachineCode", posMachineCode);
	paraM.put("actualSalesDate", salesOrderDate);
	List returnList = new ArrayList();
	returnList.add(0, posOrderItemList);
	returnList.add(1, paraM);
	return returnList;
    }
    

    
    private List qryPOSOrderPayment(Map posCommandMap , String transactionSeqNo) {
	List posOrderPaymentList = new ArrayList(); 
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String dataId = (String) posCommandMap.get("dataId");
	
	String posSoItemSql = " select * from POS_SALES_ORDER_PAYMENT where DATA_ID = ? and TRANSACTION_SEQ_NO = ? ";
	try {
	    conn = dataSource.getConnection();
	    stmt = conn.prepareStatement(posSoItemSql);
	    stmt.setString(1, dataId);
	    stmt.setString(2, transactionSeqNo);
	    rs = stmt.executeQuery();
	    
	    if (rs != null) {
		while (rs.next()) {
		    Double payAMT = 0D;
		    if(rs.getString("PAY_AMT") != null){
			payAMT = Double.parseDouble(rs.getString("PAY_AMT"));
		    }
		    
		    Double payQTY = 0D;
		    if(rs.getString("PAY_QTY") != null){
			payQTY = Double.parseDouble(rs.getString("PAY_QTY"));
		    }
		    		    
		    Double exchangeRate = 0D;
		    if(rs.getString("EXCHANGE_RATE") != null){
			exchangeRate = Double.parseDouble(rs.getString("EXCHANGE_RATE"));
		    }		    
		    
		    Double payDUE = 0D;
		    if(rs.getString("PAY_DUE") != null){
			payDUE = Double.parseDouble(rs.getString("PAY_DUE"));
		    }
		    
		    
		    TmpImportPosPaymentId tmpImportPosPaymentId = new TmpImportPosPaymentId();
		    TmpImportPosPayment tmpImportPosPayment = new TmpImportPosPayment();
		    tmpImportPosPaymentId.setSalesOrderDate(rs.getDate("SALES_ORDER_DATE"));
		    tmpImportPosPaymentId.setPosMachineCode(rs.getString("POS_MACHINE_CODE"));
		    tmpImportPosPaymentId.setTransactionSeqNo(rs.getString("TRANSACTION_SEQ_NO"));
		    tmpImportPosPaymentId.setPaySeq(rs.getLong("PAY_SEQ"));
		    tmpImportPosPayment.setId(tmpImportPosPaymentId);
		    tmpImportPosPayment.setShopCode(rs.getString("SHOP_CODE"));
		    tmpImportPosPayment.setStoreId(rs.getString("STORE_ID"));
		    tmpImportPosPayment.setPayId(rs.getString("PAY_ID"));
		    tmpImportPosPayment.setPayAmt(payAMT);
		    tmpImportPosPayment.setPayNo(rs.getString("PAY_NO"));
		    tmpImportPosPayment.setPayQty(payQTY);
		    tmpImportPosPayment.setExchangeRate(exchangeRate);
		    tmpImportPosPayment.setPayDue(payDUE);
		    tmpImportPosPayment.setOrderFlag(rs.getString("ORDER_FLAG"));
		    tmpImportPosPayment.setOrderId(rs.getString("ORDER_ID"));
		    tmpImportPosPayment.setCasherCode(rs.getString("CASHER_CODE"));
		    tmpImportPosPayment.setFileName(rs.getString("FILE_NAME"));
		    tmpImportPosPayment.setCreatedBy("SYS");
		    tmpImportPosPayment.setCreationDate(new Date());	  
//		    tmpImportPosPaymentService.deletePosPaymentByIdentification(rs.getDate("SALES_ORDER_DATE"), rs.getString("POS_MACHINE_CODE"));
		    tmpImportPosPaymentService.savePosPayment(tmpImportPosPayment);
		    
		    posOrderPaymentList.add(tmpImportPosPayment);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
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
	return posOrderPaymentList;
    }
	
    public List crtSoHeadBean(Map posCommandMap) {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null; 
	Date currentDateTime = new Date();
	List returnBeans = new ArrayList();
	List entityBeans = new ArrayList();
	String brandCode = (String) posCommandMap.get("brandCode");
	String dataId = (String) posCommandMap.get("dataId");
	String dataType = (String) posCommandMap.get("dataType");

	HashMap parameterMap = new HashMap();
	    parameterMap.put("brandCode", brandCode);
//	    parameterMap.put("fileKey", fileKey);
//	    parameterMap.put("posMachineCode", posMachineCode);
//	    parameterMap.put("actualSalesDate", posDate);
//	    parameterMap.put("transactionDate", transactionDate);
	    parameterMap.put("orderTypeCode", dataType);
	    parameterMap.put("identification", "POS");
	    parameterMap.put("opUser", "SYS");
	    
	//=============================執行解析轉檔==========================
	T2PosImportData t2POSImportData = (T2PosImportData) SpringUtils.getApplicationContext().getBean("t2PosImportData");
	String posSoItemSql = " select distinct TRANSACTION_SEQ_NO from POS_SALES_ORDER_ITEM where DATA_ID = ?  ";
	try {
	    conn = dataSource.getConnection();
	    stmt = conn.prepareStatement(posSoItemSql);
	    stmt.setString(1, dataId);
	    rs = stmt.executeQuery();
	    
	    if (rs != null) {
		while (rs.next()) {
	            //組出so_sales_order_head
		    SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
		    salesOrderHead.setBrandCode(brandCode);
		    salesOrderHead.setOrderTypeCode("SOP");
//		    salesOrderHead.setSalesOrderDate(posDate);
		    salesOrderHead.setPaymentTermCode("Z9");
		    salesOrderHead.setCurrencyCode("NTD");
		    salesOrderHead.setDiscountRate(100D);
		    salesOrderHead.setExportExchangeRate(1D);
		    salesOrderHead.setInvoiceTypeCode("2");
		    salesOrderHead.setTaxType("1");
		    salesOrderHead.setTaxRate(0D);
		    salesOrderHead.setTaxAmount(0D);
//		    salesOrderHead.setScheduleCollectionDate(posDate);
//		    salesOrderHead.setScheduleShipDate(posDate);
//		    salesOrderHead.setPosMachineCode(posMachineCode);
//		    salesOrderHead.setTransactionSeqNo("TD" + posMachineCode + rs.getShort("TRANSACTION_SEQ_NO"));
		    salesOrderHead.setSufficientQuantityDelivery("Y");
		    salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
//		    salesOrderHead.setReserve3(fileKey);
		    salesOrderHead.setReserve5("POS");
		    salesOrderHead.setCreationDate(currentDateTime);
		    salesOrderHead.setLastUpdateDate(currentDateTime);
		    
		    List tmpImportPosItems = this.qryPOSOrderItem(posCommandMap, rs.getString("TRANSACTION_SEQ_NO"));
		    parameterMap.put("posMachineCode", ((HashMap)tmpImportPosItems.get(1)).get("posMachineCode"));
		    parameterMap.put("actualSalesDate", ((HashMap)tmpImportPosItems.get(1)).get("actualSalesDate"));
		    salesOrderHead.setPosMachineCode((String) ((HashMap)tmpImportPosItems.get(1)).get("posMachineCode"));

		    //產生銷貨交易明細檔
		    produceSoItem(salesOrderHead, (List)tmpImportPosItems.get(0), parameterMap);
		    List tmpImportPosPayments = this.qryPOSOrderPayment(posCommandMap, rs.getString("TRANSACTION_SEQ_NO"));
		    //產生銷貨付款明細檔
		    produceSoPayment(salesOrderHead, tmpImportPosPayments);
		    entityBeans.add(salesOrderHead);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ValidationErrorException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
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
	
	returnBeans.add(0, entityBeans);
	returnBeans.add(1, parameterMap);
//	    parameterMap.put("posMachineCode", posMachineCode);
//	    parameterMap.put("actualSalesDate", posDate);
	System.out.println("leave=====crtSoHeadBean==="+parameterMap.get("posMachineCode")+","+parameterMap.get("actualSalesDate"));
	return returnBeans;
    }
    
    private void produceSoItem(SoSalesOrderHead salesOrderHead, List tmpImportPosItems, HashMap parameterMap) throws ValidationErrorException, Exception{
	System.out.println("======enter============produceSoItem=====");
	List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
	String brandCode = salesOrderHead.getBrandCode();
	String orderTypeCode = salesOrderHead.getOrderTypeCode();
	String posMachineCode = salesOrderHead.getPosMachineCode();
	String actualShopCode = null;
	String defaultWarehouseCode = null;
	String superintendentCode = null;
	String customerCode = null;
	String customerPoNo = null;
	String headFileName = null;
	String countryCode = null;
	String period = null;
	String passportNo = null;
	String flightNo = null;
	Date departureDate = null;
	String ladingNo = null;
	Double totalOriginalSalesAmt = 0D;
	Double totalActualSalesAmt = 0D;
	String orderDiscountType = "A"; //無折扣
	
	
	BuOrderTypeId id = new BuOrderTypeId(brandCode, orderTypeCode);
	BuOrderType buOrderType = buOrderTypeService.findById(id);
	
	if(tmpImportPosItems != null && tmpImportPosItems.size() > 0){
	    for(int i = 0; i < tmpImportPosItems.size(); i++){
		TmpImportPosItem importPosItem = (TmpImportPosItem)tmpImportPosItems.get(i);
		if(actualShopCode == null){
		    BuShop newBuShop = buShopMachineDAO.getShopCodeByMachineCode(brandCode, posMachineCode, null, null);
		    if(newBuShop != null){
			actualShopCode = newBuShop.getShopCode();
			parameterMap.put("actualShopCode", actualShopCode);
			defaultWarehouseCode = newBuShop.getSalesWarehouseCode();
			if(!StringUtils.hasText(defaultWarehouseCode)){
			    throw new ValidationErrorException("專櫃代碼(" + actualShopCode + ")並未設定預設的庫別！");
			}
		    }else{
			throw new ValidationErrorException("依據品牌(" + brandCode + ")、POS機碼(" + posMachineCode + ")查無對應的專櫃代碼！");
		    }
		}
		
		//銷貨員
		if(superintendentCode == null){
		    superintendentCode = importPosItem.getSuperintendentCode();
		}
		//客戶代碼
		if(customerCode == null){
		    customerCode = importPosItem.getCustomerCode();
		}
		//customerPoNo
		if(customerPoNo == null){
		    customerPoNo = importPosItem.getCustomerPoNo();
		}
		//headFileName
		if(headFileName == null){
		    headFileName = importPosItem.getFileName();
		}
		//國別
		if(countryCode == null){		   
		    List countrys = buCountryDAO.findByProperty("oldCountryCode", importPosItem.getCountryCode());
		    if(countrys != null && countrys.size() > 0){
			countryCode = null;//((BuCountry)countrys.get(0)).getCountryCode();
		    }
		}
		//時段
		if(period == null){
		    period = importPosItem.getPeriod();
		}
		//護照號碼
		if(passportNo == null){
		    passportNo = importPosItem.getPassportNo();
		}
		//班機號碼
		if(flightNo == null){
		    flightNo = importPosItem.getFlightNo();
		}
		//出境日期
		if(departureDate == null){
		    departureDate = importPosItem.getDepartureDate();
		}
		//提貨單號
		if(ladingNo == null){
		    ladingNo = importPosItem.getLadingNo();
		}
		
		Date salesOrderDate = importPosItem.getId().getSalesOrderDate();
		String itemCode = importPosItem.getItemCode();
		Double originalUnitPrice = importPosItem.getStandardPrice();
		Double quantity = importPosItem.getQuantity();
		String vipTypeCode = importPosItem.getVipTypeCode();
		Double originalSalesAmt = CommonUtils.round(originalUnitPrice * quantity, 0);
		Double actualSalesAmt = importPosItem.getActualSalesAmount();
		Double actualUnitPrice = getActualUnitPrice(actualSalesAmt, quantity);
		Double discountRate = getDiscountRate(buOrderType, actualSalesAmt, originalSalesAmt);
		String isTax = null;
		String usedIdentification = importPosItem.getUsedIdentification();
		String usedCardId = importPosItem.getUsedCardId();
		String usedCardType = importPosItem.getUsedCardType();
		Double usedDiscountRate = importPosItem.getUsedDiscountRate();
		String itemDiscountType = importPosItem.getItemDiscountType();		
		if(StringUtils.hasText(usedIdentification)){
		    if(!"C".equals(orderDiscountType) && ("02".equals(usedIdentification) || "03".equals(usedIdentification))){
			orderDiscountType = "C";
		    }else if("A".equals(orderDiscountType) && "01".equals(usedIdentification)){
			orderDiscountType = "B";
		    }
		}		
		//正式上線打開
		ImItem imItemPO = imItemDAO.findItem(brandCode, itemCode);
		if(imItemPO == null){
		    throw new ValidationErrorException("查無品號(" + itemCode + ")的相關資料！");
		}
		isTax = imItemPO.getIsTax();
		if(!StringUtils.hasText(isTax)){
		    throw new ValidationErrorException("品號(" + itemCode + ")的稅別為空值！");
		}		
		String fileName = importPosItem.getFileName();
		totalOriginalSalesAmt += originalSalesAmt;
		totalActualSalesAmt += actualSalesAmt;
		
		    parameterMap.put("posMachineCode", posMachineCode);
		    parameterMap.put("actualSalesDate", importPosItem.getId().getSalesOrderDate());
		    parameterMap.put("transactionDate", importPosItem.getCreationDate());
		
		SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
		salesOrderItem.setItemCode(itemCode);
		salesOrderItem.setWarehouseCode(defaultWarehouseCode);
		salesOrderItem.setOriginalForeignUnitPrice(originalUnitPrice);
		salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
		salesOrderItem.setQuantity(quantity);
		salesOrderItem.setOriginalForeignSalesAmt(originalSalesAmt);
		salesOrderItem.setOriginalSalesAmount(originalSalesAmt);
		salesOrderItem.setActualForeignSalesAmt(actualSalesAmt);
		salesOrderItem.setActualSalesAmount(actualSalesAmt);
		salesOrderItem.setPosActualSalesAmount(actualSalesAmt);//放入POS檔案裏的實際銷售金額
		salesOrderItem.setActualForeignUnitPrice(actualUnitPrice);
		salesOrderItem.setActualUnitPrice(actualUnitPrice);
		salesOrderItem.setDiscountRate(discountRate);		
		salesOrderItem.setVipPromotionCode(vipTypeCode);
		salesOrderItem.setScheduleShipDate(salesOrderDate);
		salesOrderItem.setShippedDate(salesOrderDate);
		salesOrderItem.setIsTax(isTax);
		salesOrderItem.setTaxType("1");
		salesOrderItem.setTaxRate(0D);
		salesOrderItem.setTaxAmount(0D);
		//salesOrderItem.setIsServiceItem(imItemPO.getIsServiceItem());
		//salesOrderItem.setIsComposeItem(imItemPO.getIsComposeItem());
		salesOrderItem.setReserve4(fileName);
		salesOrderItem.setReserve5("POS");
		salesOrderItem.setUsedIdentification(usedIdentification);
		salesOrderItem.setUsedCardId(usedCardId);
		salesOrderItem.setUsedCardType(usedCardType);
		salesOrderItem.setUsedDiscountRate(usedDiscountRate);
		salesOrderItem.setItemDiscountType(itemDiscountType);
		//salesOrderItem.setAllowMinusStock(imItemPO.getAllowMinusStock()); //是否允許負庫存
		salesOrderItem.setPosSeq(i + 1L); //此交易明細序號
		soSalesOrderItems.add(salesOrderItem);
		
		salesOrderHead.setPosMachineCode(posMachineCode);
		salesOrderHead.setSalesOrderDate(salesOrderDate);

	        tmpImportPosItemService.deletePosIetmByIdentification(importPosItem.getId().getSalesOrderDate(), posMachineCode);
	    }

	    salesOrderHead.setCustomerCode(customerCode);
	    salesOrderHead.setCustomerPoNo(customerPoNo);
	    salesOrderHead.setCountryCode(countryCode);
	    salesOrderHead.setNationalityCode(countryCode);
	    salesOrderHead.setShopCode(actualShopCode);
	    salesOrderHead.setDefaultWarehouseCode(defaultWarehouseCode);
	    salesOrderHead.setSuperintendentCode(superintendentCode);
	    salesOrderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmt);
	    salesOrderHead.setOriginalTotalFrnSalesAmt(totalOriginalSalesAmt);
	    salesOrderHead.setTotalActualSalesAmount(totalActualSalesAmt);
	    salesOrderHead.setActualTotalFrnSalesAmt(totalActualSalesAmt);
	    salesOrderHead.setExpenseForeignAmount(0D);
	    salesOrderHead.setExpenseLocalAmount(0D);
	    salesOrderHead.setExportCommissionRate(0D);	    
	    salesOrderHead.setTransactionTime(period);
	    salesOrderHead.setPassportNo(passportNo);
	    salesOrderHead.setFlightNo(flightNo);
	    salesOrderHead.setDepartureDate(departureDate);
	    salesOrderHead.setLadingNo(ladingNo);
	    salesOrderHead.setCreatedBy(superintendentCode);
	    salesOrderHead.setLastUpdatedBy(superintendentCode);
	    salesOrderHead.setReserve4(headFileName);
	    salesOrderHead.setOrderDiscountType(orderDiscountType);
	    salesOrderHead.setSoSalesOrderItems(soSalesOrderItems);
	    System.out.println("produceSoItem=====actualShopCode = "+actualShopCode);

	}	
	    
    }
    
    public void produceSoPayment(SoSalesOrderHead salesOrderHead, List tmpImportPosPayments) throws ValidationErrorException, Exception{
	
	List<SoSalesOrderPayment> soSalesOrderPayments = new ArrayList(0);
	String casherCode = null;
	if(tmpImportPosPayments != null && tmpImportPosPayments.size() > 0){
	    for(int i = 0; i < tmpImportPosPayments.size(); i++){
		TmpImportPosPayment importPosPayment = (TmpImportPosPayment)tmpImportPosPayments.get(i);		
		String posPaymentType = importPosPayment.getPayId(); //付款類別
		Double foreignAmount = importPosPayment.getPayAmt(); //原幣金額
		Double localAmount = importPosPayment.getPayDue();   //本幣金額
		Double exchangeRate = importPosPayment.getExchangeRate();//匯率
		String payNo = importPosPayment.getPayNo();//付款登記
		Double payQty = importPosPayment.getPayQty();//付款張數
		//收銀員
		if(casherCode == null){
		    casherCode = importPosPayment.getCasherCode();
		}
		String fileName = importPosPayment.getFileName();
		String foreignCurrencyCode = null; //原幣幣別
		if(StringUtils.hasText(posPaymentType)){
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("PaymentType", posPaymentType);
		    if(buCommonPhraseLine != null){
			foreignCurrencyCode = buCommonPhraseLine.getAttribute1();
		    }
		}
		SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
		salesOrderPayment.setPosPaymentType(posPaymentType);
		salesOrderPayment.setLocalCurrencyCode("NTD");
		salesOrderPayment.setLocalAmount(localAmount);
		salesOrderPayment.setForeignCurrencyCode(foreignCurrencyCode);
		salesOrderPayment.setForeignAmount(foreignAmount);
		salesOrderPayment.setExchangeRate(exchangeRate);
		salesOrderPayment.setPayNo(payNo);
		salesOrderPayment.setPayQty(payQty);
		salesOrderPayment.setReserve4(fileName);
		salesOrderPayment.setReserve5("POS");
		soSalesOrderPayments.add(salesOrderPayment);
		
		tmpImportPosPaymentService.deletePosPaymentByIdentification(importPosPayment.getId().getSalesOrderDate(), salesOrderHead.getPosMachineCode());
	    }	   
	    salesOrderHead.setCasherCode(casherCode);
	    salesOrderHead.setSoSalesOrderPayments(soSalesOrderPayments);
	}	
    }
    
    private Double getActualUnitPrice(Double actualSalesAmt, Double quantity){
	
	if(quantity == 0D){
	    return actualSalesAmt;
	}else if(actualSalesAmt != null && actualSalesAmt != 0D && quantity != null && quantity != 0D){
	    return CommonUtils.round(actualSalesAmt / quantity, 1);
	}else{
	    return 0D;
	}	
    }
    
    public String saveBatchHead(SoSalesOrderHead saveObj) throws Exception {
		StringBuffer reMsg = new StringBuffer();
		try {
			String serialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
			if ("unknow".equals(serialNo))
				throw new Exception("取得" + saveObj.getBrandCode() + "-" + saveObj.getOrderTypeCode() + "單號失敗！");
			
			saveObj.setOrderNo(serialNo);
			saveObj.setStatus(OrderStatus.SAVE);
			saveObj.setCountryCode("TW");
			saveObj.setCurrencyCode("NTD");
			saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
			saveObj.setCreationDate(saveObj.getLastUpdateDate());
			
			soSalesOrderHeadDAO.save(saveObj);
			
			return serialNo;
			
		} catch (Exception e) {
			reMsg.append("銷售資料匯入失敗！原因：" + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
    
    public List<SoSalesOrderHead> updateBatchLine(SoSalesOrderHead externalOrderHead,String salesNo) throws Exception{
		
    	Long indexNo = 0L;
    	
    	StringBuffer reMsg = new StringBuffer();
		
		List<SoSalesOrderHead> newSoSalesOrderHeads = new ArrayList<SoSalesOrderHead>(0);	
		
		try {
			SoSalesOrderHead orderHead = soSalesOrderHeadDAO.findSalesOrderByIdentification(externalOrderHead.getBrandCode(), externalOrderHead.getOrderTypeCode(),salesNo);
			if (orderHead == null) {
				throw new NoSuchDataException("查無品牌代號：" + externalOrderHead.getBrandCode() + "、單別：" + externalOrderHead.getOrderTypeCode() + "、單號：" + salesNo + "的銷售資料！");
			}
			else
			{
				List<SoSalesOrderItem> externalOrderItems = externalOrderHead.getSoSalesOrderItems();
				
				for (Iterator iterator = externalOrderItems.iterator(); iterator.hasNext();) {
					SoSalesOrderItem soSalesOrderItem = (SoSalesOrderItem) iterator.next();

					++indexNo;
					soSalesOrderItem.setSoSalesOrderHead(orderHead);
					soSalesOrderItem.setWarehouseCode(orderHead.getDefaultWarehouseCode());
					soSalesOrderItem.setTaxType(orderHead.getTaxType());
					soSalesOrderItem.setTaxRate(orderHead.getTaxRate());
					soSalesOrderItem.setStatus(orderHead.getStatus());
					soSalesOrderItem.setIsComposeItem("N");
					soSalesOrderItem.setIsServiceItem("N");
					soSalesOrderItem.setCreatedBy(orderHead.getCreatedBy());
					soSalesOrderItem.setCreationDate(new Date());
					soSalesOrderItem.setLastUpdatedBy(orderHead.getCreatedBy());
					soSalesOrderItem.setLastUpdateDate(new Date());
					soSalesOrderItem.setIsDeleteRecord("0");
					soSalesOrderItem.setIndexNo(indexNo);
					
					ImItemEanPriceView imItemEanPriceView = imItemEanPriceViewService.findById(orderHead.getBrandCode(), soSalesOrderItem.getItemCode());

				    // 用國際碼查詢品號
				    if (imItemEanPriceView != null) {
				    	soSalesOrderItem.setOriginalUnitPrice(imItemEanPriceView.getUnitPrice());
				    	soSalesOrderItem.setOriginalSalesAmount(soSalesOrderItem.getQuantity()*imItemEanPriceView.getUnitPrice());
				    	soSalesOrderItem.setActualUnitPrice(imItemEanPriceView.getUnitPrice()*(orderHead.getDiscountRate()*0.01));
				    	soSalesOrderItem.setActualSalesAmount(imItemEanPriceView.getUnitPrice()*(orderHead.getDiscountRate()*0.01)*soSalesOrderItem.getQuantity());
				    }
				    
					soSalesOrderItemDAO.merge(soSalesOrderItem);
					//externalOrderItems.add(soSalesOrderItem);
					newSoSalesOrderHeads.add(orderHead);
				}
			}
		}catch (Exception ex) {
			reMsg.append("銷售明細資料匯入失敗！原因：" + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
		
		return newSoSalesOrderHeads;
	}
    
  //排程抓資料被鎖定的未確認單 再送一次
    public void updateSalesOrderQty(SoSalesOrderHead soSalesOrderHead, List errorMsgs, String identification, String opUser) throws Exception{
    	
    			
			try{
				updateT2SchedulerOnHand(soSalesOrderHead.getHeadId(), errorMsgs);
				//log.info("kkkk:"+soSalesOrderHead.getHeadId());
			}catch(Exception ex){
				siProgramLogAction.createProgramLog(PROGRAM_ID,
						MessageStatus.LOG_ERROR, identification, errorMsgs.toString(),
						opUser);
				throw new Exception("銷售單錯誤");
			}	    
		
    }
    
    /**
     * T2的排程，扣庫存並將狀態改為SIGNING
     * 
     * @param entityBeans
     * @param parameterMap
     * @throws Exception
     */
    public void updateT2SchedulerOnHand(Long salesHeadId, List assemblyMsg) throws Exception {
	log.info("====enter updateT2SchedulerOnHand=====");
	    
		String opUser = "POS";
		String organizationCode = "TM";
		String errorMsg = null;
		
		SoSalesOrderHead salesOrderHeadPO = findById(salesHeadId);
		log.info("2 SoSalesOrderHead.index = " + salesOrderHeadPO.getHeadId());
		
		if (salesOrderHeadPO == null) {
		    throw new NoSuchObjectException("查無主鍵(" + salesHeadId + ")的銷貨單資料！");
		}
		String identification = MessageStatus.getIdentification(
			salesOrderHeadPO.getBrandCode(), salesOrderHeadPO
				.getOrderTypeCode(), salesOrderHeadPO.getOrderNo());
		
		try{	
		 //扣庫存轉出貨明細
			bookAvailableQuantityForT2(salesOrderHeadPO, organizationCode, opUser, assemblyMsg ,true);
			assemblyMsg.clear();
		}catch(Exception ex){
			errorMsg = "資料已鎖定";			
			assemblyMsg.add(errorMsg);
		}
		//for 儲位用
	    //if(imStorageAction.isStorageExecute(salesOrderHeadPO)){
		//異動儲位庫存
		//imStorageService.updateStorageOnHandBySource(salesOrderHeadPO, OrderStatus.FINISH, PROGRAM_ID, null, false);
	   // }
	    log.info("Mark--------:"+assemblyMsg.size());
		if (assemblyMsg.size() != 0) {
		    throw new Exception("POS銷貨單(" + identification + ")預扣庫存量失敗！");
		} else {
			salesOrderToDeliveryForT2(salesOrderHeadPO, opUser);
		    salesOrderHeadPO.setStatus(OrderStatus.SIGNING);
		    soSalesOrderHeadDAO.update(salesOrderHeadPO);
		}
	
	log.info("====leave updateT2SchedulerOnHand=====");
    }
    
    public void bookDeductStock( SoSalesOrderHead soSalesOrderHead,String organizationCode
    		, String loginUser, List errorMsgs ) throws FormException, Exception{
    	
        String errorMsg = null;
    	String brandCode = soSalesOrderHead.getBrandCode();
    	String orderTypeCode = soSalesOrderHead.getOrderTypeCode();
    	String identification = MessageStatus.getIdentification(brandCode,
    		orderTypeCode, soSalesOrderHead.getOrderNo());
    	
    	//updateOnhand upOnhand = new updateOnhand();
    	
    	HashMap isServiceItemMap = new HashMap(); // 是否為服務性商品集合
    	HashMap isComposeItemMap = new HashMap(); // 是否為組合性商品集合
    	HashMap allowWholeSale = new HashMap(); // 是否為組合性商品集合
    	    	
    	Set[] aggregateResult = aggregateOrderItemsQtyForT2New(soSalesOrderHead,
    			isServiceItemMap, isComposeItemMap, allowWholeSale, errorMsgs);
    	
    	Iterator it = aggregateResult[0].iterator(); // ImOnHand扣庫存用
    	Iterator cmIt = aggregateResult[1].iterator(); // CmOnHand扣庫存用
    	
    	// ======================================預扣報單庫存量=======================================================
    	while (cmIt.hasNext()) {
    	    
    		Map.Entry cmEntry = (Map.Entry) cmIt.next();		
    		Double outUnCommitQty = (Double) cmEntry.getValue();		
    		String[] cmkeyArray = StringUtils.delimitedListToStringArray(
    			(String) cmEntry.getKey(), "{$}");
    		
    		if(StringUtils.hasText(cmkeyArray[0])
	    			|| NumberUtils.getLong((cmkeyArray[1])) != 0
	    			|| StringUtils.hasText(cmkeyArray[2])
	    			|| StringUtils.hasText(cmkeyArray[3])){
    			
    			if (!"Y".equals((String) isServiceItemMap.get(cmkeyArray[2]))) {
	    		    cmDeclarationOnHandDAO.updateOutUncommitQtyNew(cmkeyArray[0],
	    			    NumberUtils.getLong(cmkeyArray[1]), cmkeyArray[2],
	    			    cmkeyArray[3], brandCode, outUnCommitQty,
	    			    loginUser, (String) allowWholeSale
	    				    .get(cmkeyArray[2]), errorMsgs);
	    		}
    			
    		}else{
    			errorMsgs.add("品牌(" + brandCode + ")、報關單號("
	    			    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
	    			                     	    			    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
	    			                     	    			    + cmkeyArray[3] + ") 其中有一項並未填寫");
    		}	    	
    	}
    	// ======================================預扣實體庫別庫存量=======================================================
    	while (it.hasNext()) {
    	    
    		Map.Entry entry = (Map.Entry) it.next();
    		// Double outUnCommitQty = (Double) entry.getValue();
    		String[] keyArray = StringUtils.delimitedListToStringArray(
    			(String) entry.getKey(), "{$}");
    		if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
    		    List<ImOnHand> lockedOnHands = null;
    		    
    			lockedOnHands = imOnHandDAO.getLockedOnHand(
    				organizationCode, keyArray[0], keyArray[1],
    				keyArray[2], brandCode);
    		    
    		    if (lockedOnHands != null && lockedOnHands.size() > 0) {
	    			ImOnHand onHandPO = (ImOnHand) lockedOnHands.get(0);
	    			Double availableQuantity = onHandPO.getStockOnHandQty()
	    				- onHandPO.getOutUncommitQty()
	    				+ onHandPO.getInUncommitQty()
	    				+ onHandPO.getMoveUncommitQty()
	    				+ onHandPO.getOtherUncommitQty();
	    			// 如果數量超過庫存 且不是POS以及允許全賣出
					if ((Double) entry.getValue() > availableQuantity
						&& (!(POSTYPECODE.equals(soSalesOrderHead
							.getOrderTypeCode())))) {
						errorMsgs.add("品牌("
		    				    + brandCode + ")、品號(" + keyArray[0]
		    				         	    				    + ")、庫別(" + keyArray[1] + ")、批號("
		    				         	    				    + keyArray[2] + ")可用庫存量不足！");
					} else {
					    if (!POSTYPECODE.equals(soSalesOrderHead
						    .getOrderTypeCode())) {
						imOnHandDAO.bookAvailableQuantity(
							lockedOnHands, (Double) entry
								.getValue(), "FIFO", loginUser);
					    } else {
						imOnHandDAO.bookQuantity(lockedOnHands,
							(Double) entry.getValue(), "FIFO",
							loginUser);
					    }
					}	    			
	    			
    		    } else {
	    			// 如果是T2POS 且 不允許全賣出
	    			if ((POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode()))) {
	    				// ==========================SOP單查無onHand時新增一筆====================================
	    			    ImOnHandId id = new ImOnHandId();
	    			    ImOnHand newOnHand = new ImOnHand();
	    			    id.setOrganizationCode(organizationCode);
	    			    id.setItemCode(keyArray[0]);
	    			    id.setWarehouseCode(keyArray[1]);
	    			    id.setLotNo(keyArray[2]);
	    			    newOnHand.setId(id);
	    			    newOnHand.setBrandCode(brandCode);
	    			    newOnHand.setStockOnHandQty(0D);
	    			    newOnHand.setOutUncommitQty((Double) entry
	    				    .getValue());
	    			    newOnHand.setInUncommitQty(0D);
	    			    newOnHand.setMoveUncommitQty(0D);
	    			    newOnHand.setOtherUncommitQty(0D);
	    			    newOnHand.setCreatedBy(loginUser);
	    			    newOnHand.setCreationDate(new Date());
	    			    newOnHand.setLastUpdatedBy(loginUser);
	    			    newOnHand.setLastUpdateDate(new Date());
	    			    imOnHandDAO.save(newOnHand);
	    			} else {	    				
	    				errorMsgs.add("查無品牌(" + brandCode + ")、品號(" + keyArray[0]
		    				         	+ ")、庫別(" + keyArray[1] + ")、批號(" + keyArray[2] + ")的庫存資料！");	    			    
	    			}
    		    }
    		}    	    
    	}
    }
    
    /**
     * 1.將相同的商品、庫別、批號集合 2.將相同的報單號碼、報單項次、商品、關別集合
     * 
     * @param salesOrderHead
     * @param isServiceItemMap
     * @param isComposeItemMap
     * @return Set[]
     */
    private Set[] aggregateOrderItemsQtyForT2New(SoSalesOrderHead salesOrderHead,
	    HashMap isServiceItemMap, HashMap isComposeItemMap,
	    HashMap allowWholeSale, List errorMsgs) throws FormException, Exception {
    	
    	String brandCode = salesOrderHead.getBrandCode();
    	List<SoSalesOrderItem> originalOrderItems = salesOrderHead.getSoSalesOrderItems();
		StringBuffer key = new StringBuffer();
		StringBuffer cmKey = new StringBuffer();
		HashMap map = new HashMap();
		HashMap cmMap = new HashMap();
    	
    	for (SoSalesOrderItem originalOrderItem : originalOrderItems) {
		    String itemCode = originalOrderItem.getItemCode();
		    String warehouseCode = originalOrderItem.getWarehouseCode();
		    String lotNo = originalOrderItem.getLotNo();
		    Double quantity = originalOrderItem.getQuantity();
		    String declNo = originalOrderItem.getImportDeclNo(); // 報單號碼
		    Long declSeq = originalOrderItem.getImportDeclSeq(); // 報單項次
		    String itemTaxCode = originalOrderItem.getIsTax();
		    String customsWarehouseCode = null;
		    // 數量不可為null
		    if (quantity == null) {
				quantity = 0D;
				originalOrderItem.setQuantity(quantity);
		    }
		    // 保稅商品集合
		    if ("F".equals(itemTaxCode)) {
		    	
		    	ImWarehouse warehousePO = imWarehouseService
					.findByBrandCodeAndWarehouseCode(brandCode,
						warehouseCode, null);
		    	
		    	if (warehousePO != null){
		    		customsWarehouseCode = warehousePO.getCustomsWarehouseCode();
		    		
				    if (StringUtils.hasText(customsWarehouseCode)){
				    	cmKey.delete(0, cmKey.length());
						cmKey.append(declNo + "{$}");
						cmKey.append(declSeq + "{$}");
						cmKey.append(itemCode + "{$}");
						cmKey.append(customsWarehouseCode);
						if (cmMap.get(cmKey.toString()) == null) {
						    cmMap.put(cmKey.toString(), quantity);
						} else {
						    cmMap.put(cmKey.toString(), quantity
							    + ((Double) cmMap.get(cmKey.toString())));
						}
				    }else{
				    	errorMsgs.add("庫別("
								+ warehouseCode + ")的海關關別未設定！");
				    }					
		    	}else{
		    		errorMsgs.add("依據品牌(" + brandCode + ")、庫別(" + warehouseCode + ")查無庫別相關資料！");
		    	}
		    }
	
		    key.delete(0, key.length());
		    key.append(itemCode + "{$}");
		    key.append(warehouseCode + "{$}");
		    key.append(lotNo + "{$}");
		    key.append(itemTaxCode);
		    if (map.get(key.toString()) == null) {
		    	map.put(key.toString(), quantity);
		    } else {
		    	map.put(key.toString(), quantity + ((Double) map.get(key.toString())));
		    }
		    // 將itemCode是否為服務性商品、組合性商品放入map中
		    if (isServiceItemMap.get(itemCode) == null){
		    	isServiceItemMap.put(itemCode, originalOrderItem.getIsServiceItem());
		    }			
		    if (isComposeItemMap.get(itemCode) == null){
		    	isComposeItemMap.put(itemCode, originalOrderItem.getIsComposeItem());
		    }			
		    // 20100818 modify by joeywu for 明細裏含相同料號時，可以只勾選部份負庫存的品號
		    if (allowWholeSale.get(itemCode) == null) {
		    	allowWholeSale.put(itemCode, originalOrderItem.getAllowWholeSale());
		    } else if ("Y".equals(originalOrderItem.getAllowWholeSale())) {
		    	allowWholeSale.put(itemCode, originalOrderItem.getAllowWholeSale());
		    }else{
		    	
		    }		    
		}
    	
    	return new Set[] { map.entrySet(), cmMap.entrySet() };
    }
    
    public List<SoSalesOrderHead> updatesiResendSO(){
    	List<SiResend> SiResend = soSalesOrderHeadDAO.findByIsLock(STATUS);
    	List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findSalesOrderByIsLock(SiResend);
    	return salesOrderHeads;
    }
    
    public List<Properties> updateCustomsStatus(Map parameterMap) throws Exception{    	
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	String uuid = UUID.randomUUID().toString();
    	try{
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    
    	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
    	    
    	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    	    String id = (String)PropertyUtils.getProperty(otherBean, "headId");    	    
    	    Long headId = NumberUtils.getLong(id);
    	    
    	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
    	    
    	    SoSalesOrderHead soSalesOrderHead = soSalesOrderHeadDAO.findSalesOrderByIdentification(loginBrandCode,orderTypeCode,orderNo);
    	    
    	    
    	    String tranStatus = (String)PropertyUtils.getProperty(otherBean, "tranStatus");
    	    if(tranStatus.equals("cancel")){
    	    	soSalesOrderHead.setTranAllowUpload("D");
    	    }else{
    	    	soSalesOrderHead.setTranAllowUpload("I");
    	    }
    	    
    	    soSalesOrderHead.setCustomsStatus("A");
    	    
    	    if(orderTypeCode.equals("SOP")){
    	    	soSalesOrderHead.setTranRecordStatus("NF08");  
    	    }else if(orderTypeCode.equals("SWF")){
    	    	soSalesOrderHead.setTranRecordStatus("NF14");
    	    }else{
    	    	throw new Exception("上傳失敗，原因：");
    	    }
    	    SiSystemLogUtils.createSystemLog("update_customs_status",
					MessageStatus.LOG_INFO, "銷售單海關單點上傳 銷售單號 = "+orderTypeCode+","+soSalesOrderHead.getCustomerPoNo()+" 上傳狀態為"+soSalesOrderHead.getTranAllowUpload()+" user = "+loginEmployeeCode, soSalesOrderHead.getSalesOrderDate(),
					uuid, "SYS");
    	    soSalesOrderHeadDAO.save(soSalesOrderHead);
    	    
    	    msgBox.setMessage("已將單據送簽海關");
    	    
    	}catch(Exception ex){
    	    log.error("上傳海關失敗，原因：" + ex.toString());
    	    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
    	    throw new Exception("上傳失敗，原因：" + ex.toString());

    	}
    	returnMap.put("vatMessage", msgBox);
    	return AjaxUtils.parseReturnDataToJSON(returnMap);
    }
    
    /**
     * posModifyDeclaration查詢
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPosModifyDeclarationPageData(Properties httpRequest) throws Exception {
		DecimalFormat df = null;
		try {
		    List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	
		    // ======================帶入Head的值=========================
		    String brandCode = httpRequest.getProperty("brandCode");
		    Date salesOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, httpRequest.getProperty("salesOrderDate"));
		    String posMachineCode = httpRequest.getProperty("posMachineCode");
		    String oldImportDeclNo = httpRequest.getProperty("oldImportDeclNo");
		    int oldImportDeclSeq = NumberUtils.getLong(httpRequest.getProperty("oldImportDeclSeq")).intValue();
		    
		    log.info("brandCode = " + brandCode);
		    log.info("salesOrderDate = " + salesOrderDate);
		    log.info("posMachineCode = " + posMachineCode);
		    log.info("oldImportDeclNo = " + oldImportDeclNo);
		    log.info("oldImportDeclSeq = " + oldImportDeclSeq);
		    
		    HashMap map = new HashMap();
		    HashMap findObjs = new HashMap();
		    findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
		    findObjs.put(" and ORDER_NO NOT LIKE :TMP", "TMP%");
		    findObjs.put(" and SALES_ORDER_DATE  = :salesOrderStartDate", salesOrderDate);
		    findObjs.put(" and POS_MACHINE_CODE = :posMachineCode", posMachineCode);
		    
		    
		    // ==============================================================
		    
		    if(salesOrderDate!=null){
		    	Map headsMap = soSalesOrderHeadDAO.search("SoSalesOrderHead",
		    		    findObjs, "order by customerPoNo ", iSPage, iPSize,
		    		    BaseDAO.QUERY_SELECT_RANGE);
		    	    List<SoSalesOrderHead> heads = (List<SoSalesOrderHead>) headsMap
		    		    .get(BaseDAO.TABLE_LIST);
		    	    if (heads != null && heads.size() > 0) {
		    		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		    		Long maxIndex = (Long) soSalesOrderHeadDAO.search(
		    			"SoSalesOrderHead", "count(HEAD_ID) as rowCount",
		    			findObjs, "order by customerPoNo ", iSPage, iPSize,
		    			BaseDAO.QUERY_RECORD_COUNT).get(
		    			BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		    		
		    		for (SoSalesOrderHead head : heads) {
		    		    this.setHeadNames(head);
	
		    		    
		    		}
	
		    		result.add(AjaxUtils.getAJAXPageData(httpRequest,
		    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
		    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, heads,
		    			gridDatas, firstIndex, maxIndex));
		    	    } else {
		    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
		    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
		    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, map,
		    			gridDatas));
		    	    }
		    } else {
		    		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
		    			GRID_SEARCH_FIELD_NAMES_CUSTOMER_PO_NO,
		    			GRID_SEARCH_FIELD_DEFAULT_VALUES_CUSTOMER_PO_NO, map,
		    			gridDatas));
		    	    }	    
		    return result;
		} catch (Exception ex) {
		    log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}
    }
    
    //SoSalesOrderMainService.java整併 Maco 2016.12.08
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
 	   
        try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	       
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成銷貨工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成銷貨工作任務失敗！");
	}
    }
    public void saveNoValidate(List entityBeans, HashMap parameterMap) throws Exception{
    	
    	//================POS重傳時刪除相關銷售、出貨、過帳記錄資料=================
    	String actualbrandCode = (String)parameterMap.get("brandCode");
    	String actualShopCode = (String)parameterMap.get("actualShopCode");
    	Date actualSalesDate = (Date)parameterMap.get("actualSalesDate");
    	String opUser = (String)parameterMap.get("opUser");
    	try{    
    	    String organization = UserUtils.getOrganizationCodeByBrandCode(actualbrandCode);
    	    if(!StringUtils.hasText(organization)){
    	        throw new ValidationErrorException("品牌代號：" + actualbrandCode + "所屬的組織代號為空值！");
    	    }	   
    	    List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findSOPByProperty(parameterMap);
    	    if(salesOrderHeads != null){
                    for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
    	            String identification = MessageStatus.getIdentificationMsg(
    		    salesOrderHead.getBrandCode(), salesOrderHead.getOrderTypeCode(), 
    		    salesOrderHead.getOrderNo());
    	            if(!OrderStatus.SIGNING.equals(salesOrderHead.getStatus())){
    	                throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
    	            }
    		    revertToOriginallyAvailableQuantity(salesOrderHead.getHeadId(), organization, opUser);
    		    soSalesOrderHeadDAO.delete(salesOrderHead);
    		}
    	    }
    	    /*SoPostingTally postingTally = (SoPostingTally)soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, new SoPostingTallyId(actualShopCode, actualSalesDate));
    	    if(postingTally != null){
    	        soPostingTallyDAO.delete(postingTally);
                }*/
    	}catch(Exception ex){
    	    log.error("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料時發生錯誤，原因：" + ex.toString());
                throw new Exception("刪除品牌代號：" + actualbrandCode + "、專櫃代號：" + actualShopCode + "、銷售日期：" + DateUtils.format(actualSalesDate) + "的銷售、出貨資料失敗！");
    	}	    
    	//========================================================================
    	String organizationCode = null;
    	String shopCode = null;
    	Date salesDate = null;
    	String brandCode = null;
    	for (int index = 0; index < entityBeans.size(); index++) {			
    	    SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead)entityBeans.get(index);
    	    if(organizationCode == null){
    		organizationCode = UserUtils.getOrganizationCodeByBrandCode(soSalesOrderHead.getBrandCode());
    		if(organizationCode == null)
    		    throw new NoSuchDataException("查無" + soSalesOrderHead.getBrandCode() + "的組織代號！");
    	    }
    	    if(shopCode == null){
    		shopCode = soSalesOrderHead.getShopCode();
    	    }
    	    if(salesDate == null){
    		salesDate = soSalesOrderHead.getSalesOrderDate();
    	    }
    	    if(brandCode == null){
    		brandCode = soSalesOrderHead.getBrandCode();
    	    }
    		
                //取單別序號，insert至SO
    	    String serialNo = "";
    	    if("SOP".equals(soSalesOrderHead.getOrderTypeCode())){
    		serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode()
    			, soSalesOrderHead.getShopCode(), soSalesOrderHead.getPosMachineCode());
    	    }else{
    		serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead.getBrandCode(),soSalesOrderHead.getOrderTypeCode());
    	    }
    	    
    	    if (!serialNo.equals("unknow")) {
    	        soSalesOrderHead.setOrderNo(serialNo);	
    	    }else {
    		throw new ObtainSerialNoFailedException("取得"
    			+ soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
    	    }
    	    soSalesOrderHeadDAO.save(soSalesOrderHead);
    	    //扣庫存
    	    Object[] objArray = bookAvailableQuantity(soSalesOrderHead, organizationCode, soSalesOrderHead.getCreatedBy());
    	    //產生Delivery Line
    	    salesOrderToDelivery(soSalesOrderHead, soSalesOrderHead.getSoSalesOrderItems(), (HashMap)objArray[1], (String)objArray[0], soSalesOrderHead.getCreatedBy());
    	    if(POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
    	        mailToPOSAdministrator(soSalesOrderHead, (List)objArray[2]);
    	    }
    	}	
    	//寫入過帳記錄檔 
    	/*SoPostingTallyId postingTallyId = new SoPostingTallyId(shopCode, salesDate);
    	if(soPostingTallyDAO.findByPrimaryKey(SoPostingTally.class, postingTallyId) == null){
    	    SoPostingTally postingTally = new SoPostingTally();
    	    postingTally.setId(postingTallyId);
    	    postingTally.setBrandCode(brandCode);
    	    postingTally.setIsPosting("N");
    	    postingTally.setCreatedBy(opUser);
    	    postingTally.setCreateDate(new Date());
    	    postingTally.setLastUpdatedBy(opUser);
    	    postingTally.setLastUpdateDate(new Date());
    	    soPostingTallyDAO.save(postingTally);
    	}*/
        }
    private void revertToOriginallyAvailableQuantity(Long SalesOrderHeadId,
    	    String organizationCode, String loginUser) throws FormException,
    	    NoSuchDataException {

    	List imDeliveryLines = imDeliveryLineDAO.findByProperty(
    		"ImDeliveryLine", "salesOrderId", SalesOrderHeadId);
    	if (imDeliveryLines != null && imDeliveryLines.size() > 0) {
    	    for (int i = 0; i < imDeliveryLines.size(); i++) {
    		ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines.get(i);
    		//非服務性商品補回庫存
    		if(!"Y".equals(deliveryLine.getIsServiceItem())){	  
    		    imOnHandDAO.updateOutUncommitQuantity(organizationCode, deliveryLine.getItemCode()
    			    , deliveryLine.getWarehouseCode(), deliveryLine.getLotNo(), 
    			    deliveryLine.getSalesQuantity(), loginUser);
    		}

    		imDeliveryLineDAO.delete(deliveryLine);
    	    }
    	}else {
    	    throw new NoSuchDataException("依據銷貨單主檔主鍵："
    		    + SalesOrderHeadId + "查無相關出貨單明細檔資料！");
    	}
        }
    private Object[] bookAvailableQuantity(SoSalesOrderHead soSalesOrderHead, String organizationCode, String loginUser) throws NoSuchDataException, FormException{
    	
    	BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
    	buOrderTypeId.setOrderTypeCode(soSalesOrderHead.getOrderTypeCode());
    	buOrderTypeId.setBrandCode(soSalesOrderHead.getBrandCode());

    	BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
    	if (buOrderType == null) {
    	    throw new NoSuchDataException("依據單據代號："
    	        + soSalesOrderHead.getOrderTypeCode() + ",品牌代號："
    		+ soSalesOrderHead.getBrandCode() + "查無相關單別資料！");
    	}
    	String stockControl = buOrderType.getStockControl(); // 庫存控制方法
    	HashMap onHandsMap = new HashMap(); // 出貨單派發批號之來源
    	HashMap isServiceItemMap = new HashMap(); // 是否為服務性商品集合
    	HashMap isComposeItemMap = new HashMap(); // 是否為組合性商品集合

    	List mailContents = new ArrayList(0);
    	Set entrySet = aggregateOrderItemQuantity(soSalesOrderHead.getSoSalesOrderItems(), isServiceItemMap, isComposeItemMap);
    	Iterator it = entrySet.iterator();
    	while (it.hasNext()) {
    	    Map.Entry entry = (Map.Entry) it.next();
    	    String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "#");
    	    List<ImOnHand> lockedOnHands = null;
    	    try {
    	        lockedOnHands = imOnHandDAO.getLockedOnHand(
    		organizationCode, keyArray[0], keyArray[1], null);
    	    } catch (CannotAcquireLockException cale) {
    	        throw new FormException("品號：" + keyArray[0] + ",庫別：" + keyArray[1] + "已鎖定，請稍後再試！");
    	    }
    	    //非服務性商品且數量大於零時檢核庫存量是否足夠
    	    if(!"Y".equals((String)isServiceItemMap.get(keyArray[0])) && (Double) entry.getValue() > 0D){
    	        Double availableQuantity = imOnHandDAO.getCurrentStockOnHandQty(organizationCode, keyArray[0], keyArray[1]);

    	        if (availableQuantity == null) {
    	            //非SOP單需檢核是否有庫存
    	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
    	                throw new NoSuchObjectException("查無品號：" + keyArray[0]
    		            + ",庫別：" + keyArray[1] + "的庫存量！");
    	            }
    	        } else if ((Double) entry.getValue() > availableQuantity) {
    	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
    		        throw new InsufficientQuantityException("品號：" + keyArray[0]
    		            + ",庫別：" + keyArray[1] + "可用庫存量不足！");
    		    }else{
    		        //POS庫存不足不丟出例外，改以mail通知
    		        mailContents.add(keyArray[0] + "#" + keyArray[1] + "#" + (Double)entry.getValue());
    		    }
    	        }
    	    }
    	    // ==========非服務性商品預訂可用庫存==========
    	    if (lockedOnHands != null && lockedOnHands.size() > 0) {
    	        getRequiredPropertyConvertToMap(lockedOnHands, onHandsMap); // 從ImOnHand資料中取得必要資訊，放置到HashMap
    	        if(!"Y".equals((String)isServiceItemMap.get(keyArray[0]))){
    	            if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
    		        imOnHandDAO.bookAvailableQuantity(lockedOnHands, (Double) entry.getValue(), stockControl, loginUser);
    	            }else{
    	                imOnHandDAO.bookQuantity(lockedOnHands, (Double) entry.getValue(), stockControl, loginUser);
    	            }
    	        }
    	    } else {
    		if(!POSTYPECODE.equals(soSalesOrderHead.getOrderTypeCode())){
    		    throw new NoSuchObjectException("查無品號：" + keyArray[0] + ",庫別：" + keyArray[1] + "的庫存資料！");
    		}else{
    		    //==========================SOP單查無onHand時新增一筆====================================
    		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("SystemConfig", "DefaultLotNo");		
    	            String defaultLotNo = buCommonPhraseLine != null? buCommonPhraseLine.getName():"000000000000";
    	            ImOnHandId id = new ImOnHandId();
    	            ImOnHand newOnHand = new ImOnHand();
    		    id.setOrganizationCode(organizationCode);
    		    id.setItemCode(keyArray[0]);
    		    id.setWarehouseCode(keyArray[1]);
    		    id.setLotNo(defaultLotNo);
    		    newOnHand.setId(id);
    		    newOnHand.setBrandCode(soSalesOrderHead.getBrandCode());  //增加庫存的品牌
    		    newOnHand.setStockOnHandQty(0D);
    		    if(!"Y".equals((String)isServiceItemMap.get(keyArray[0]))){
    		        newOnHand.setOutUncommitQty((Double) entry.getValue());
    		    }else{
    			newOnHand.setOutUncommitQty(0D);
    		    }
    		    newOnHand.setInUncommitQty(0D);
    		    newOnHand.setMoveUncommitQty(0D);
    		    newOnHand.setOtherUncommitQty(0D);
    		    newOnHand.setCreatedBy(loginUser);
    		    newOnHand.setCreationDate(new Date());
    		    newOnHand.setLastUpdatedBy(loginUser);
    		    newOnHand.setLastUpdateDate(new Date());
    		    imOnHandDAO.save(newOnHand);
    		    lockedOnHands = new ArrayList();
    		    lockedOnHands.add(newOnHand);
    		    getRequiredPropertyConvertToMap(lockedOnHands, onHandsMap);
    		}
    	    }	    
    	}
    	return new Object[]{stockControl, onHandsMap, mailContents};
        }
    public Double calculateTaxAmount(String taxType, Double taxRate, Double actualSalesAmount){
    	
    	Double taxAmount = 0D;
    	if("3".equals(taxType) && taxRate != null && taxRate != 0D && actualSalesAmount != null && actualSalesAmount != 0D){
    	    Double salesAmount = actualSalesAmount /(1 + taxRate/100);
    	    taxAmount = actualSalesAmount - salesAmount;
    	}
    	
    	return CommonUtils.round(taxAmount, 0);
        }
    /**
     * 依據品牌代號、單別、單號查詢銷貨單
     * 
     * @param brandCode
     * @param orderTypeCode
     * @param orderNo
     * @return SoSalesOrderHead
     * @throws Exception
     */
    public SoSalesOrderHead findSalesOrderByIdentification(String brandCode, String orderTypeCode, String orderNo) throws Exception{
	
	try{
	    return soSalesOrderHeadDAO.findSalesOrderByIdentification(brandCode, orderTypeCode, orderNo);
	}catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢銷貨單時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據品牌代號：" + brandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "查詢銷貨單時發生錯誤，原因：" + ex.getMessage());
	}
    }
    /**
     * 透過匯入更新銷貨單主檔或明細檔
     * 
     * @param salesOrderHeads
     * @throws Exception
     */
    public void updateSalesOrderForImportTask(List<SoSalesOrderHead> salesOrderHeads) throws Exception{
	
	for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
	    modifySoSalesOrder(salesOrderHead, salesOrderHead.getLastUpdatedBy());
	}	
    }
    
    public void updateRealTimeUploads() throws Exception{
    	List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findRealTimeData();
    	
    	for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
    		log.info(salesOrderHead.getHeadId()+"---"+salesOrderHead.getStatus());
    		if(salesOrderHead.getStatus().equals(OrderStatus.UNCONFIRMED)){
    			log.info("未確單不做上傳");
    		}else{
    			salesOrderHead.setCustomsStatus("A");
    			salesOrderHead.setTranAllowUpload("I");
    			soSalesOrderHeadDAO.save(salesOrderHead);
    		}
    	}
    	
    }
    
    public void updateRealTimeUploadsAll() throws Exception{
    	
    	int startTime = 0;
    	int endTime = 24;
    	
    	int beforeTime = 2;
    	
    	BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("Switch", "realTime");    	
    	if(buCommonPhraseLine.getEnable().equals("Y")){
    		
    		if(buCommonPhraseLine.getAttribute1()==null||buCommonPhraseLine.getAttribute1().equals("")){
        		 log.info("沒有設開始時間");     		
        	}else{
        		startTime = Integer.parseInt(buCommonPhraseLine.getAttribute1());  
        	}
        	
        	if(buCommonPhraseLine.getAttribute2()==null||buCommonPhraseLine.getAttribute2().equals("")){
        		log.info("沒有設結束時間");
        	}else{
        		endTime = Integer.parseInt(buCommonPhraseLine.getAttribute2());
        		if(endTime==0){
        			endTime = 24;
        		}
        	}
        	
        	if(buCommonPhraseLine.getAttribute4()==null||buCommonPhraseLine.getAttribute4().equals("")){
	       		 log.info("沒有設傳多久以前時間");     		
	       	}else{
	       		beforeTime = Integer.parseInt(buCommonPhraseLine.getAttribute4());
	       		if(beforeTime<=0){
	       			beforeTime = 2;
	       		}
	       	}
        	
        	Date date = new Date();
        	Calendar cal = Calendar.getInstance();
    		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
    		if(hourOfDay == 0){
    			hourOfDay = endTime;
    		}
    		
    		if(hourOfDay>=startTime && hourOfDay<=endTime ){
    			
    			List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findRealTimeDataAll(beforeTime);     	
            	for(SoSalesOrderHead salesOrderHead : salesOrderHeads){
            		log.info(salesOrderHead.getHeadId()+"---"+salesOrderHead.getStatus());
            		if(salesOrderHead.getStatus().equals(OrderStatus.UNCONFIRMED)){
            			log.info("未確單不做上傳");
            		}else{
            			salesOrderHead.setCustomsStatus("A");
            			salesOrderHead.setTranAllowUpload("I");
            			soSalesOrderHeadDAO.save(salesOrderHead);
            		}
            	}
    		}
    	}
    }
    /**
     * 將抵用券、銷售資料轉文字檔到FTP
     * 
     * @param soSalesOrderHead
     * @throws Exception
     */
//將資料匯出到FTP 17app Brian 20200818    
    public void appTransferToFTP(SoSalesOrderHead soSalesOrderHead)throws Exception{
    	log.info("進入appTransferToFTP");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //日期轉換格式
        String posMachineCode = soSalesOrderHead.getPosMachineCode(); //取得POS號碼
        /****************
         *以下是給17的資料 *
         *****************/
        String appCustomerCode = soSalesOrderHead.getAppCustomerCode();                               //APP代號
        String orderNo = soSalesOrderHead.getOrderNo();                                               //POS銷售單號
        String salesOrderDate = sdf.format(soSalesOrderHead.getSalesOrderDate());                     //銷售日期YYYY/MM/DD
        String ladingNo = soSalesOrderHead.getLadingNo();                                             //路提單號
        String status = soSalesOrderHead.getStatus();												 //單據狀態
        String customsNo = soSalesOrderHead.getCustomsNo();											//客戶訂單編號
        String transactionTime = soSalesOrderHead.getTransactionTime().replaceAll("[^0-9]", "");	//銷售時分秒 hh:mm:ss
        if(ladingNo ==null || ladingNo.trim().length()==0){
        	ladingNo="X";
        }
        String totalActualSalesAmount = null;																		//實際消費金額	            
        String totalBonusPointAmount = null;   																		//可轉換點數
        String fileName = "";
        
        if(status.equals("VOID")){	            	
        	totalActualSalesAmount = String.valueOf(Math.floor(soSalesOrderHead.getTotalActualSalesAmount()*-1));   //狀態作廢實際消費金額為負值
            totalActualSalesAmount = totalActualSalesAmount.substring(0,totalActualSalesAmount.lastIndexOf("."));
            totalBonusPointAmount = String.valueOf(Math.floor(soSalesOrderHead.getTotalBonusPointAmount()*-1));		//狀態作廢可轉換點數為負值 
            totalBonusPointAmount = totalBonusPointAmount.substring(0,totalBonusPointAmount.lastIndexOf("."));
            fileName = customsNo+"D"; 																				//檔案名稱:機台編號+客戶訂單編號+D
        }else{	
        	totalActualSalesAmount = String.valueOf(Math.floor(soSalesOrderHead.getTotalActualSalesAmount())); 		//實際消費金額為正值
            totalActualSalesAmount = totalActualSalesAmount.substring(0,totalActualSalesAmount.lastIndexOf("."));
            totalBonusPointAmount = String.valueOf(Math.floor(soSalesOrderHead.getTotalBonusPointAmount()));		//可轉換點數為正值
            totalBonusPointAmount = totalBonusPointAmount.substring(0,totalBonusPointAmount.lastIndexOf("."));
            fileName = customsNo; 																					//檔案名稱:機台編號+客戶訂單編號
        }
        
        //增加滿額贈抵用券Brian 20200620
        String promotionCode = null; 	//抵用券
        List<String> promotionCodeStringList = new ArrayList();
        
        String eventCode = soSalesOrderHead.getEventCode(); //取得活動代號
        log.info("eventCode=="+eventCode);
        
        List<ImPromotion> promotionCodes = imPromotionDAO.findPromotionDataByPromotionCodePD("promotionCode");  //取得所有PD開頭/啟用的促銷代號 
        for(ImPromotion pC:promotionCodes){
        	//log.info(pC.getPromotionCode());
        	promotionCodeStringList.add(pC.getPromotionCode());
        }
        
        if(promotionCodeStringList.contains(eventCode)){         //將所有PD與該筆促銷代號比較
        	promotionCode = eventCode;
        }else{
        	log.info("找不到"+promotionCode+"給予值 X ");
        	promotionCode="X";
        }
        
        if(!StringUtils.hasText(eventCode)){   //沒有使用抵用券就給"X"
        	log.info("未使用"+promotionCode+"給予值 X ");
        	promotionCode="X";
        }
        
        //取得BuCommonPhraseLine路徑設定 Brian 20200612 ========	            
        //判斷作業系統為Windows、Linux等
        String headCode = "CsvPath";
        String lineCode = "";
        File file = null; // new File(File.separator + "home" + File.separator + "romankooo" + File.separator + "work" + File.separator + "txt" + File.separator);
        String content = appCustomerCode+","+orderNo+","+salesOrderDate+transactionTime+","+ladingNo+","+totalActualSalesAmount+","+totalBonusPointAmount+","+promotionCode+"\r\n";
        if(System.getProperty("os.name").toLowerCase().startsWith("win")){
        	lineCode = "CsvPathForWindows";
        	log.info("進入了"+lineCode+"路徑");
        	BuCommonPhraseLine path = (BuCommonPhraseLine)buCommonPhraseLineDAO.findById(headCode, lineCode);
        	String windowsPath = path.getAttribute1();
        	FileTools.WriteToFile(windowsPath + fileName + "log.csv", content);//寫入本機D槽
        }else{
        	lineCode = "CsvPathForLinux";
        	log.info("進入了"+lineCode+"路徑");
        	BuCommonPhraseLine path = (BuCommonPhraseLine)buCommonPhraseLineDAO.findById(headCode, lineCode);
        	String linuxPath = path.getAttribute1();
        	FileTools.WriteToFile(linuxPath + fileName + "log.csv", content);//寫入本機home
        }     
        log.info("寫入本機17LOG成功!");
        //============
      //把檔案丟到FTP (17在此FTP拿取)
        String ftpPath = "//192.168.66.71/upload/"; //正式環境TM路徑
        String ftpPath2 = "//192.168.66.72/upload/"; //正式環境17路徑
//        String ftpPath3 = "//192.168.66.73/shared/"; // 測試環境用
        File filelog =FileTools.WriteToFile(ftpPath + fileName, content);
        log.info("寫入"+ftpPath+"成功!");
        File filelog2 = FileTools.WriteToFile(ftpPath2 + fileName, content);
        log.info("寫入"+ftpPath2+"成功!");
//        File filelog3 =  FileTools.WriteToFile(ftpPath3 + fileName, content);
//        log.info("寫入"+ftpPath3+"成功!");
        if(!filelog.exists() || !filelog2.exists() /*|| !filelog3.exists()*/){
        	throw new Exception("檔案不存在!");
        }
    }
}