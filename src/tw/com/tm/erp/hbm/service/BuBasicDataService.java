package tw.com.tm.erp.hbm.service;

import java.io.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.POSExportData;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuDeleteLog;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuExchangeRateId;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPaymentTermId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PosCurrency;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuDeleteLogDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuExchangeRateDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;


public class BuBasicDataService {

	private static final Log log = LogFactory.getLog(BuBasicDataService.class);
	BuShopDAO buShopDAO;
	BuEmployeeDAO buEmployeeDAO;
	BuCurrencyDAO buCurrencyDAO;
	BuCountryDAO buCountryDAO;
	BuBrandDAO buBrandDAO;
	BuPaymentTermDAO buPaymentTermDAO;
	BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;
	BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;
	BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
	BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	BuExchangeRateDAO buExchangeRateDAO;
	BuShopMachineDAO buShopMachineDAO;
	BuCommonPhraseService buCommonPhraseService;
	PosExportDAO posExportDAO;
	BuBrandService buBrandService;
	BuDeleteLogDAO buDeleteLogDAO;
	SiSystemLogService siSystemLogService;

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setBuCommonPhraseHeadDAO(
			BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}

	public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
		this.buCurrencyDAO = buCurrencyDAO;
	}
	
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

	public void setBuPaymentTermDAO(BuPaymentTermDAO buPaymentTermDAO) {
		this.buPaymentTermDAO = buPaymentTermDAO;
	}

	public void setBuCustomerWithAddressViewDAO(
			BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO) {
		this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
	}

	public void setBuSupplierWithAddressViewDAO(
			BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
		this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
	}

	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	public void setBuExchangeRateDAO(BuExchangeRateDAO buExchangeRateDAO) {
		this.buExchangeRateDAO = buExchangeRateDAO;
	}

	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}
	

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	
	public void setBuDeleteLogDAO(BuDeleteLogDAO buDeleteLogDAO) {
		this.buDeleteLogDAO = buDeleteLogDAO;
	}
	
	public void setSiSystemLogService(SiSystemLogService siSystemLogService){
		this.siSystemLogService = siSystemLogService;
	}


	/**
	 * 國別查詢picker用的欄位
	 */
	 public static final String[] GRID_SEARCH_COUNTRY_FIELD_NAMES = { 
		"countryCode", "countryCName", "countryEName",
		"description", "lastUpdatedByName", "lastUpdateDate", 
		"countryCode"
	 };

	 /**
	  * 幣別查詢picker用的欄位
	  */
	 public static final String[] GRID_SEARCH_CURRENCY_FIELD_NAMES = {
		 "currencyCode", "currencyCName", "currencyEName","exChangeCode","orders","description",
		 "lastUpdatedBy", "lastUpdateDate"
	 };

	 /**
	  * 匯率查詢picker用的欄位
	  */
	 public static final String[] GRID_SEARCH_EXCHANGE_RATE_FIELD_NAMES = {
		 "id.organizationCode" ,"id.sourceCurrency", "id.againstCurrency",
		 "exchangeRate", "id.beginDate","lastUpdatedByName" ,"lastUpdateDate"
	 };

	 public static final String[] GRID_SEARCH_BRAND_FIELD_NAMES = { 
		 "brandCode", "brandName", "branchCode",
		 "description", "lastUpdatedBy", "lastUpdateDate", 
		 "brandCode"
	 };

//	 public static final String[] GRID_SEARCH_BRAND_FIELD_NAMES_1 = { 
//	 "indexNo","brandCode", "brandName", "branchCode",
//	 "description", "lastUpdatedBy", "lastUpdateDate", 
//	 "brandCode"
//	 };

	 public static final String[] GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 "", "", "", 
		 ""
	 };

	 public static final String[] GRID_SEARCH_CURRENCY_FIELD_DEFAULT_VALUES = { 
		 "", "", "", "", "", "",
		 "", ""
	 };

	 public static final String[] GRID_SEARCH_EXCHANGE_RATE_FIELD_DEFAULT_VALUES = { 
		 "","", "",
		 "", "","",""
	 };

	 public static final String[] GRID_SEARCH_BRAND_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 "", "", "", 
		 ""
	 };

//	 public static final String[] GRID_SEARCH_BRAND_FIELD_DEFAULT_VALUES_1 = { 
//	 "", "", "",
//	 "", "", "", 
//	 "",""
//	 };
	 /**
	  * 依照輸入條件來搜尋國別資料
	  * 
	  * @param countryCode
	  * @param countryCName
	  * @param countryEName
	  * @return List
	  * @throws Exception
	  */
	 public List<BuCountry> findCountryList(String countryCode,
			 String countryCName, String countryEName) throws Exception{

		 try {
			 countryCode = countryCode.trim().toUpperCase();
			 countryCName = countryCName.trim();
			 countryEName = countryEName.trim();
			 return buCountryDAO.findCountryList(countryCode, countryCName,
					 countryEName);
		 } catch (Exception ex) {
			 log.error("查詢國別資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢國別資料時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依據啟用狀態查詢出國別
	  * 
	  * @param enable
	  * @return List
	  * @throws Exception
	  */
	 public List findCountryByEnable(String enable) throws Exception {

		 try {
			 List buCountryList = buCountryDAO.findCountryByEnable(enable);
			 if (buCountryList == null || buCountryList.size() == 0) {
				 if (StringUtils.hasText(enable)) {
					 if ("Y".equals(enable))
						 throw new NoSuchDataException("國別檔查無啟用中的資料！");
					 else
						 throw new NoSuchDataException("國別檔查無未啟用中的資料！");
				 } else {
					 throw new NoSuchDataException("國別檔查無任何資料！");
				 }
			 } else {
				 return buCountryList;
			 }
		 } catch (Exception ex) {
			 log.error("查詢國別檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢國別檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依照輸入條件查詢幣別資料
	  * 
	  * @param currencyCode
	  * @param currencyCName
	  * @param currencyEName
	  * @return List
	  * @throws Exception
	  */
	 public List<BuCurrency> findCurrencyList(String currencyCode,
			 String currencyCName, String currencyEName) throws Exception {

		 try {
			 currencyCode = currencyCode.trim().toUpperCase();
			 currencyCName = currencyCName.trim();
			 currencyEName = currencyEName.trim();
			 return buCurrencyDAO.findCurrencyList(currencyCode, currencyCName,
					 currencyEName);
		 } catch (Exception ex) {
			 log.error("查詢幣別資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢幣別資料時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依據啟用狀態查詢出幣別
	  * 
	  * @param enable
	  * @return List
	  * @throws Exception
	  */
	 public List findCurrencyByEnable(String enable) throws Exception {

		 try {
			 List buCurrencyList = buCurrencyDAO.findCurrencyByEnable(enable);
			 if (buCurrencyList == null || buCurrencyList.size() == 0) {
				 if (StringUtils.hasText(enable)) {
					 if ("Y".equals(enable))
						 throw new NoSuchDataException("幣別檔查無啟用中的資料！");
					 else
						 throw new NoSuchDataException("幣別檔查無未啟用中的資料！");
				 } else {
					 throw new NoSuchDataException("幣別檔查無任何資料！");
				 }
			 } else {
				 return buCurrencyList;
			 }
		 } catch (Exception ex) {
			 log.error("查詢幣別檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢幣別檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依據primary key為查詢條件，取得國別資料
	  * 
	  * @param id
	  * @return BuCurrency
	  * @throws Exception
	  */
	 public BuCountry findCountryById(String id) throws Exception{
		 try {
			 id = id.trim().toUpperCase();
			 return buCountryDAO.findById(id);
		 } catch (Exception ex) {
			 log.error("依據國別代碼：" + id + "查詢國別資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據國別代碼：" + id + "查詢國別資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	 }

	 /**
	  * 依據primary key為查詢條件，取得幣別代碼資料
	  * 
	  * @param id
	  * @return BuCurrency
	  * @throws Exception
	  */
	 public BuCurrency findCurrencyById(String id) throws Exception {
		 System.out.println("gggggggggggg:"+id);
		 try {
			 id = id.trim().toUpperCase();
			 return buCurrencyDAO.findById(id);
		 } catch (Exception ex) {
			 log.error("依據幣別代碼：" + id + "查詢幣別資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據幣別代碼：" + id + "查詢幣別資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	 }

	 /**
	  * 依據primary key為查詢條件，取得幣別代碼資料
	  * 
	  * @param id
	  * @return BuCurrency
	  * @throws Exception
	  */
	 public BuExchangeRate findExchangeRateById(String id) throws Exception {

		 System.out.println("匯率findExchangeRateById:"+id);
		 try {
			 // id = id.trim().toUpperCase();

			 BuExchangeRateId ids = new BuExchangeRateId("T2","qq","測試幣03",new Date(2013/7/19));
			 return this.findBuExchangeRateById(ids);
			 //return buExchangeRateDAO.findById("T2","qq","測試幣03",new Date(2013/7/19));
			 //return this.findBuExchangeRateById();
		 } catch (Exception ex) {
			 log.error("依據匯率代碼：" + id + "查詢匯率資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據匯率代碼：" + id + "查詢匯率資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	 }

	 /**
	  * 付款條件查詢作業
	  * 
	  * @param organizationCode
	  * @param paymentTermCode
	  * @param name
	  * @param baseDateCode
	  * @param billTypeCode
	  * @return List
	  */
	 public List<BuPaymentTerm> findPaymentTermList(String organizationCode,
			 String paymentTermCode, String name, String baseDateCode,
			 String billTypeCode) {
		 return buPaymentTermDAO.findPaymentTermList(organizationCode,
				 paymentTermCode, name, baseDateCode, billTypeCode);

	 }

	 /**
	  * 依據primary key為查詢條件，取得付款條件資料
	  * 
	  * @param id
	  * @return BuCurrency
	  */
	 public BuPaymentTerm findPaymentTermById(BuPaymentTermId id) {
		 id.setPaymentTermCode(id.getPaymentTermCode().trim().toUpperCase());
		 return buPaymentTermDAO.findById(id);
	 }

	 /**
	  * 依據組織代號、啟用狀態查詢出付款條件
	  * 
	  * @param organizationCode
	  * @param enable
	  * @return List
	  * @throws Exception
	  */
	 public List findPaymentTermByOrganizationAndEnable(String organizationCode,
			 String enable) throws Exception {

		 try {
			 List buPaymentTermList = buPaymentTermDAO
			 .findPaymentTermByOrganizationAndEnable(organizationCode,
					 enable);
			 if (buPaymentTermList == null || buPaymentTermList.size() == 0) {
				 if (StringUtils.hasText(enable)) {
					 if ("Y".equals(enable))
						 throw new NoSuchDataException("付款條件資料檔查無啟用中的資料！");
					 else
						 throw new NoSuchDataException("付款條件資料檔查無未啟用中的資料！");
				 } else {
					 throw new NoSuchDataException("付款條件資料檔查無任何資料！");
				 }
			 } else {
				 return buPaymentTermList;
			 }
		 } catch (Exception ex) {
			 log.error("查詢付款條件資料檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢付款條件資料檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 取得常用字彙明細檔之字彙名稱
	  * 
	  * @param headCode
	  * @param lineCode
	  * @return String Name
	  */
	 public String getCommonPhraseLineName(String headCode, String lineCode) {
		 String lineName = "";
		 try {
			 BuCommonPhraseLineId id = new BuCommonPhraseLineId(
					 new BuCommonPhraseHead(headCode), lineCode);
			 lineName = buCommonPhraseLineDAO.findById(id).getName();
		 } catch (Exception e) {
			 if (e.getCause() == null) {
				 lineName = "unknow";
			 }
		 }
		 return lineName;

	 }

	 /**
	  * 依據查詢條件取得常用字彙資訊
	  * 
	  * @param headCode
	  * @param name
	  * @param description
	  * @return List
	  */
	 public List<BuCommonPhraseHead> findCommonPhraseList(String headCode,
			 String name, String description) {
		 try {
			 return buCommonPhraseHeadDAO.findCommonPhraseList(headCode, name,
					 description);
		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據查詢條件取得常用字彙資訊
	  * 
	  * @param headCode
	  * @param name
	  * @param description
	  * @return List
	  */
	 public List<BuCommonPhraseLine> findCommonPhraseLineByAttribute(
			 String headCode, String searchString) {
		 try {
			 return buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					 headCode, searchString);

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據常用字彙主檔代碼取得啟用之明細檔資訊
	  * 
	  * @param headCode
	  * @return List
	  */
	 public List<BuCommonPhraseLine> findCommonPhraseEnableLine(String headCode) {
		 try {

			 BuCommonPhraseHead head = buCommonPhraseHeadDAO.findById(headCode);
			 return buCommonPhraseLineDAO.findCommonPhraseEnableLine(head);

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據品牌代號及客戶代號，查詢啟用狀態之客戶資料
	  * 
	  * @param brandCode
	  * @param customerCode
	  * @return BuCustomerWithAddressView
	  */
	 public BuCustomerWithAddressView findEnableCustomerById(String brandCode,
			 String customerCode) {
		 try {
			 return buCustomerWithAddressViewDAO.findEnableCustomerById(
					 brandCode, customerCode);

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據品牌代號及廠商代號，查詢啟用狀態之廠商資料
	  * 
	  * @param brandCode
	  * @param supplierCode
	  * @return BuSupplierWithAddressView
	  */
	 public BuSupplierWithAddressView findEnableSupplierById(String brandCode,
			 String supplierCode) {
		 try {
			 return buSupplierWithAddressViewDAO.findEnableSupplierById(
					 brandCode, supplierCode, "Y");

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }
	 /**
	  * 依據品牌代號及廠商代號，查詢啟用狀態之廠商資料
	  * 
	  * @param brandCode
	  * @param supplierCode
	  * @return BuSupplierWithAddressView
	  */
	 public BuSupplierWithAddressView findEnableSupplierById(String brandCode,
			 String supplierCode,String enable) {
		 try {
			 return buSupplierWithAddressViewDAO.findEnableSupplierById(
					 brandCode, supplierCode, enable);

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據品牌代號及員工代號，查詢啟用狀態之員工資料
	  * 
	  * @param brandCode
	  * @param employeeCode
	  * @return BuEmployeeWithAddressView
	  */
	 public BuEmployeeWithAddressView findEnableEmployeeById(
			 String organizationCode, String employeeCode) {
		 try {
			 return buEmployeeWithAddressViewDAO.findEnableEmployeeById(
					 organizationCode, employeeCode);

		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據品牌代號及專櫃代號，查詢啟用狀態之專櫃資料
	  * 
	  * @param brandCode
	  * @param shopCode
	  * @return BuShop
	  */
	 public BuShop findEnableShopById(String brandCode, String shopCode) {
		 return buShopDAO.findEnableShopById(brandCode, shopCode);
	 }

	 /**
	  * 依照輸入條件來搜尋專櫃資料
	  * 
	  * @param brandCode
	  * @param shopCode
	  * @param shopCName
	  * @param shopType
	  * @param place
	  * @param SearchString
	  * @return List
	  */
	 public List<BuShop> findShopList(String brandCode, String shopCode,
			 String shopCName, String shopType, String place, String searchString) {
		 try {
			 return buShopDAO.findShopList(brandCode, shopCode, shopCName,
					 shopType, place, searchString);
		 } catch (RuntimeException re) {
			 throw re;
		 }
	 }

	 /**
	  * 依據品牌代號、員工編號查詢出員工隸屬的專櫃代號
	  * 
	  * @param brandCode
	  * @param employeeCode
	  * @param isEnable
	  * @return List
	  * @throws Exception
	  */
	 public List<BuShop> getShopForEmployee(String brandCode,
			 String employeeCode, String isEnable) throws Exception {
		 try {
			 List buShops = buShopDAO.getShopForEmployee(brandCode,
					 employeeCode, isEnable);
			 if (buShops != null && buShops.size() > 0) {
				 return buShops;
			 } else {
				 throw new NoSuchDataException("查無品牌代號：" + brandCode + "、工號："
						 + employeeCode + "隸屬的專櫃資料！");
			 }
		 } catch (Exception ex) {
			 ex.printStackTrace();
			 log.error("查詢員工隸屬專櫃時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢員工隸屬專櫃時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依據品牌代號、啟用狀態查詢出專櫃代號
	  * 
	  * @param brandCode
	  * @param enable
	  * @return List
	  * @throws Exception
	  */
	 public List findShopByBrandAndEnable(String brandCode, String enable)
	 throws Exception {

		 try {
			 List buShopList = buShopDAO.findShopByBrandAndEnable(brandCode,
					 enable);
			 if (buShopList == null || buShopList.size() == 0) {
				 if (StringUtils.hasText(enable)) {
					 if ("Y".equals(enable))
						 throw new NoSuchDataException("專櫃資料檔查無啟用中的資料！");
					 else
						 throw new NoSuchDataException("專櫃資料檔查無未啟用中的資料！");
				 } else {
					 throw new NoSuchDataException("專櫃資料檔查無任何資料！");
				 }
			 } else {
				 return buShopList;
			 }
		 } catch (Exception ex) {
			 log.error("查詢專櫃資料檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢專櫃資料檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }


	 /**
	  * AJAX利用幣別指定匯率
	  * 
	  * @param headObj
	  */
	 public List<Properties> getAJAXExchangeRateByCurrencyCode(Properties httpRequest) {
		 List re = new ArrayList();
		 String currencyCode = httpRequest.getProperty("currencyCode");
		 String organizationCode = httpRequest.getProperty("organizationCode");
		 Properties pro = new Properties();
		 if (StringUtils.hasText(currencyCode) && StringUtils.hasText(organizationCode)) {
			 String exchangeRate = getExchangeRateByCurrencyCode(organizationCode, currencyCode);
			 pro.setProperty("exchangeRate", exchangeRate);
			 re.add(pro);
		 } else {
			 pro.setProperty("exchangeRate", "1");
			 re.add(pro);
		 }
		 return re;

	 }

	 /**
	  * 利用幣別指定匯率
	  * 
	  * @param headObj
	  */
	 public String getExchangeRateByCurrencyCode(String organizationCode, String currencyCode) {
		 log.info("BuBasicDataService.getExchangeRateByCurrencyCode organizationCode=" + organizationCode + ",currencyCode="
				 + currencyCode);
		 Double tmp = new Double(1);
		 if (StringUtils.hasText(organizationCode) && (StringUtils.hasText(currencyCode))) {
			 BuExchangeRate bxr = getLastExchangeRate(organizationCode, currencyCode, buCommonPhraseService
					 .getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
			 if (null != bxr) {
				 tmp = bxr.getExchangeRate();
			 }
		 }
		 return tmp.toString();
	 }

	 /**
	  * 取得最新匯率
	  * 
	  * @param organizationCode
	  * @param sourceCurrency
	  * @param againstCurrency
	  * @return
	  */
	 public BuExchangeRate getLastExchangeRate(String organizationCode,
			 String sourceCurrency, String againstCurrency) {
		 return buExchangeRateDAO.getLastExchangeRate(organizationCode,
				 sourceCurrency, againstCurrency);
	 }

	 /**
	  * 取得最新匯率
	  * 
	  * @param organizationCode
	  * @param sourceCurrency
	  * @param againstCurrency
	  * @param currenceDate
	  * @return
	  */
	 public BuExchangeRate getLastExchangeRate(String organizationCode,
			 String sourceCurrency, String againstCurrency , Date currenceDate ) {
		 return buExchangeRateDAO.getLastExchangeRate(organizationCode,
				 sourceCurrency, againstCurrency, currenceDate);
	 }    

	 /**
	  * 判斷是否為新資料，並將匯率資料新增或更新至匯率資料檔
	  * 
	  * @param buExchangeRate
	  * @param loginUser
	  * @param isNew
	  * @return String
	  */
	 public String saveOrUpdateBuExchangeRate(BuExchangeRate buExchangeRate,
			 String loginUser, boolean isNew) throws FormException, Exception {
		 System.out.println("saveOrUpdateBuExchangeRate");
		 try {
			 checkBuExchangeRate(buExchangeRate, loginUser);
			 BuExchangeRateId id = buExchangeRate.getId();
			 String sourceCurrency = id.getSourceCurrency();
			 String againstCurrency = id.getAgainstCurrency();
			 Date beginDate = id.getBeginDate();

			 BuExchangeRate exchangeRatePO = findBuExchangeRateById(id);
			 if (exchangeRatePO == null) {
				 saveBuExchangeRate(buExchangeRate);
			 } else if (isNew) {
				 throw new ValidationErrorException("來源幣別：" + sourceCurrency
						 + "、目標幣別：" + againstCurrency + "、啟用日期："
						 + DateUtils.format(beginDate) + "已經存在，請勿重覆建立！");
			 } else {
				 BeanUtils.copyProperties(buExchangeRate, exchangeRatePO);
				 updateBuExchangeRate(exchangeRatePO);
			 }

			 return "來源幣別：" + sourceCurrency + "、目標幣別：" + againstCurrency
			 + "、啟用日期：" + DateUtils.format(beginDate) + "存檔成功！";
		 } catch (FormException fe) {
			 log.error("匯率資料存檔時發生錯誤，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("匯率資料存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("匯率資料存檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 檢核匯率資料
	  * 
	  * @param currency
	  * @param loginUser
	  * @throws ValidationErrorException
	  */
	 private void checkBuExchangeRate(BuExchangeRate buExchangeRate,
			 String loginUser) throws ValidationErrorException {

		 BuExchangeRateId id = buExchangeRate.getId();
		 if (!StringUtils.hasText(id.getSourceCurrency())) {
			 throw new ValidationErrorException("請選擇來源幣別！");
		 } else if (!StringUtils.hasText(id.getAgainstCurrency())) {
			 throw new ValidationErrorException("請選擇目標幣別！");
		 } else if (id.getBeginDate() == null) {
			 throw new ValidationErrorException("請選擇啟用日期！");
		 } else if (buExchangeRate.getExchangeRate() == null) {
			 throw new ValidationErrorException("請輸入匯率！");
		 }

		 UserUtils.setOpUserAndDate(buExchangeRate, loginUser);
	 }

	 /**
	  * 新增至匯率資料檔
	  * 
	  * @param saveObj
	  */
	 public void saveBuExchangeRate(BuExchangeRate saveObj) {
		 buExchangeRateDAO.save(saveObj);
	 }

	 /**
	  * 更新至匯率資料檔
	  * 
	  * @param updateObj
	  */
	 public void updateBuExchangeRate(BuExchangeRate updateObj) {
		 buExchangeRateDAO.update(updateObj);
	 }

	 /**
	  * 依據primary key為查詢條件，取得匯率資料檔
	  * 
	  * @param buExchangeRateId
	  * @return BuExchangeRate
	  * @throws Exception
	  */
	 public BuExchangeRate findBuExchangeRateById(BuExchangeRateId buExchangeRateId) throws Exception {
		 System.out.println("findBuExchangeRateById");
		 try {
			 BuExchangeRate buExchangeRate = (BuExchangeRate) buExchangeRateDAO.findByPrimaryKey(BuExchangeRate.class, buExchangeRateId);
			 return buExchangeRate;
		 } catch (Exception ex) {
			 log.error("依據主鍵查詢匯率資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據主鍵查詢匯率資料時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依照輸入條件來搜尋匯率資料
	  * 
	  * @param organizationCode
	  * @param sourceCurrency
	  * @param againstCurrency
	  * @param exchangeRate
	  * @param beginDate
	  * @return List
	  * @throws Exception
	  */
	 public List<BuExchangeRate> findExchangeRateList(String organizationCode,
			 String sourceCurrency, String againstCurrency, Double exchangeRate,
			 Date beginDate) throws Exception {
		 try {
			 return buExchangeRateDAO.findExchangeRateList(organizationCode,
					 sourceCurrency, againstCurrency, exchangeRate, beginDate);
		 } catch (Exception ex) {
			 log.error("查詢匯率資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢匯率資料時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 依據currencyCode以及日期為查詢條件，取得匯率
	  * 
	  * @param currencyCode
	  * @return ExchangeRate
	  * @throws Exception
	  */  
	 public List<Properties> getLastExchangeRateAJAX(Properties httpRequest) throws Exception{
		 List<Properties> result = new ArrayList();
		 Properties properties = new Properties();
		 Double exportExchangeRate = 1D;
		 String currencyCode = "";
		 Date currencyDate = null;
		 BuExchangeRate buExchangeRate;
		 try{
			 currencyCode = httpRequest.getProperty("currencyCode");
			 currencyDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("currencyDate"));
			 buExchangeRate = buExchangeRateDAO.getLastExchangeRate("TM", currencyCode, "NTD", currencyDate);
			 if(buExchangeRate!=null)
				 exportExchangeRate = buExchangeRate.getExchangeRate();
			 properties.setProperty("ExportExchangeRate", String.valueOf(exportExchangeRate));	    
			 result.add(properties);
			 return result;
		 }catch (Exception ex) {
			 log.error("查詢匯率代號 ："+ currencyCode + "時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢匯率代號 ："+ currencyCode + "時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 判斷是否為新資料，並將幣別資料新增或更新至幣別檔
	  * 
	  * @param currency
	  * @param loginUser
	  * @param isNew
	  * @return String
	  * @throws FormException
	  * @throws Exception
	  */
	 public String saveOrUpdateBuCurrency(BuCurrency currency, String loginUser,
			 boolean isNew) throws FormException, Exception {
		 try {
			 checkBuCurrency(currency, loginUser);
			 currency.setEnable(("N".equals(currency.getEnable()) ? "N" : "Y"));
			 BuCurrency currencyPO = findCurrencyById(currency.getCurrencyCode());
			 if (currencyPO == null) {
				 buCurrencyDAO.save(currency);
			 } else if (isNew) {
				 throw new ValidationErrorException("幣別代碼："
						 + currency.getCurrencyCode() + "已經存在，請勿重覆建立！");
			 } else {
				 BeanUtils.copyProperties(currency, currencyPO);
				 buCurrencyDAO.update(currencyPO);
			 }

			 return "幣別代碼：" + currency.getCurrencyCode() + "存檔成功！";
		 } catch (FormException fe) {
			 log.error("幣別資料存檔時發生錯誤，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("幣別資料存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("幣別資料存檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 檢核幣別資料
	  * 
	  * @param currency
	  * @param loginUser
	  * @throws ValidationErrorException
	  */
	 private void checkBuCurrency(BuCurrency currency, String loginUser)
	 throws ValidationErrorException {

		 if (!StringUtils.hasText(currency.getCurrencyCode())) {
			 throw new ValidationErrorException("請輸入幣別代碼！");
		 } else {
			 currency.setCurrencyCode(currency.getCurrencyCode().trim()
					 .toUpperCase());
		 }

		 if (!StringUtils.hasText(currency.getCurrencyCName())) {
			 throw new ValidationErrorException("請輸入中文名稱！");
		 } else if (!StringUtils.hasText(currency.getCurrencyEName())) {
			 throw new ValidationErrorException("請輸入英文名稱！");
		 }

		 UserUtils.setOpUserAndDate(currency, loginUser);
	 }

	 /**
	  * 判斷是否為新資料，並將資料新增或更新至付款條件資料檔
	  * 
	  * @param paymentTerm
	  * @param loginUser
	  * @param isNew
	  * @return String
	  * @throws FormException
	  * @throws Exception
	  */
	 public String saveOrUpdateBuPaymentTerm(BuPaymentTerm paymentTerm,
			 String loginUser, boolean isNew) throws FormException, Exception {
		 try {
			 checkBuPaymentTerm(paymentTerm, loginUser);
			 BuPaymentTermId id = paymentTerm.getId();
			 paymentTerm.setEnable(("N".equals(paymentTerm.getEnable()) ? "N"
					 : "Y"));
			 BuPaymentTerm paymentTermPO = findPaymentTermById(id);
			 if (paymentTermPO == null) {
				 buPaymentTermDAO.save(paymentTerm);
			 } else if (isNew) {
				 throw new ValidationErrorException("付款條件代碼："
						 + id.getPaymentTermCode() + "已經存在，請勿重覆建立！");
			 } else {
				 BeanUtils.copyProperties(paymentTerm, paymentTermPO);
				 buPaymentTermDAO.update(paymentTermPO);
			 }

			 return "付款條件代碼：" + id.getPaymentTermCode() + "存檔成功！";
		 } catch (FormException fe) {
			 log.error("付款條件資料存檔時發生錯誤，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("付款條件資料存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("付款條件資料存檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 檢核付款條件資料
	  * 
	  * @param paymentTerm
	  * @param loginUser
	  * @throws ValidationErrorException
	  */
	 private void checkBuPaymentTerm(BuPaymentTerm paymentTerm, String loginUser)
	 throws ValidationErrorException {

		 BuPaymentTermId id = paymentTerm.getId();
		 if (!StringUtils.hasText(id.getPaymentTermCode())) {
			 throw new ValidationErrorException("請輸入付款條件代碼！");
		 } else {
			 id.setPaymentTermCode(id.getPaymentTermCode().trim().toUpperCase());
		 }

		 if (!StringUtils.hasText(paymentTerm.getName())) {
			 throw new ValidationErrorException("請輸入付款條件名稱！");
		 } else {
			 paymentTerm.setName(paymentTerm.getName().trim());
		 }

		 UserUtils.setOpUserAndDate(paymentTerm, loginUser);
	 }

	 /**
	  * 判斷是否為新資料，並將國別資料新增或更新至國別檔
	  * 
	  * @param country
	  * @param loginUser
	  * @param isNew
	  * @return String
	  * @throws FormException
	  * @throws Exception
	  */
	 public String saveOrUpdateBuCountry(BuCountry country, String loginUser,
			 boolean isNew) throws FormException, Exception {
		 try {
			 checkBuCountry(country, loginUser);
			 country.setEnable(("N".equals(country.getEnable()) ? "N" : "Y"));
			 BuCountry countryPO = findCountryById(country.getCountryCode());
			 if (countryPO == null) {
				 buCountryDAO.save(country);
			 } else if (isNew) {
				 throw new ValidationErrorException("國別代碼："
						 + country.getCountryCode() + "已經存在，請勿重覆建立！");
			 } else {
				 BeanUtils.copyProperties(country, countryPO);
				 buCountryDAO.update(countryPO);
			 }

			 return "國別代碼：" + country.getCountryCode() + "存檔成功！";
		 } catch (FormException fe) {
			 log.error("國別資料存檔時發生錯誤，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("國別資料存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("國別資料存檔時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 檢核國別資料
	  * 
	  * @param country
	  * @param loginUser
	  * @throws ValidationErrorException
	  */
	 private void checkBuCountry(BuCountry country, String loginUser)
	 throws ValidationErrorException {

		 if (!StringUtils.hasText(country.getCountryCode())) {
			 throw new ValidationErrorException("請輸入國別代碼！");
		 } else {
			 country.setCountryCode(country.getCountryCode().trim()
					 .toUpperCase());
		 }

		 if (!StringUtils.hasText(country.getCountryCName())) {
			 throw new ValidationErrorException("請輸入中文名稱！");
		 } else if (!StringUtils.hasText(country.getCountryEName())) {
			 throw new ValidationErrorException("請輸入英文名稱！");
		 }

		 UserUtils.setOpUserAndDate(country, loginUser);
	 }

	 /**
	  * ajax 取得國別查詢的結果
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
	 public List<Properties> getAJAXBuCountryageData(Properties httpRequest) throws Exception{

		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String countryCode = httpRequest.getProperty("countryCode");
			 String countryCName= httpRequest.getProperty("ccountryName");
			 String countryEName = httpRequest.getProperty("ccountryName");

			 log.info("countryCode:"+countryCode);
			 log.info("countryCName:"+countryCName);
			 log.info("countryEName:"+countryEName);

			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
			 findObjs.put("and model.countryCode like :countryCode","%"+countryCode+"%");
			 findObjs.put("and model.countryCName like :countryCName","%"+countryCName+"%");
			 findObjs.put("and model.countryEName like :countryEName","%"+countryEName+"%");

			 //==============================================================	    

			 Map buCountryMap = buCountryDAO.search( "BuCountry as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			 List<BuCountry> buCountrys = (List<BuCountry>) buCountryMap.get(BaseDAO.TABLE_LIST); 

			 log.info("BuCountry.size"+ buCountrys.size());	
			 if (buCountrys != null && buCountrys.size() > 0) {

				 // 設定額外欄位
				 this.setBuCountryLineOtherColumn(buCountrys);

				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buCountryDAO.search("BuCountry as model", "count(model.countryCode) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES,buCountrys, gridDatas, firstIndex, maxIndex));
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES, map,gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的國功能查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的國別功能查詢失敗！");
		 }	
	 }

	 public List<Properties> getAJAXBuBrandSearchPageDatandSearchPageData(Properties httpRequest) throws Exception{

		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String brandCode = httpRequest.getProperty("brandCode");
			 String brandName = httpRequest.getProperty("brandName");
			 String branchCode = httpRequest.getProperty("branchCode");
			 log.info("brandCode:"+brandCode);
			 log.info("brandName:"+brandName);
			 log.info("branchCode:"+branchCode);  
			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
			 findObjs.put("and model.brandCode like :brandCode","%"+brandCode+"%");
			 findObjs.put("and model.brandName like :brandName","%"+brandName+"%");
			 findObjs.put("and model.branchCode like :branchCode","%"+branchCode+"%");

			 //==============================================================	    

			 Map buBrandMap = buBrandDAO.search("BuBrand as model",findObjs,"order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE ); 
			 List<BuBrand> buBrands = (List<BuBrand>) buBrandMap.get(BaseDAO.TABLE_LIST); 

			 log.info("BuBrand.size"+ buBrands.size());	
			 System.out.println("====BuBrand.size ="+ buBrands.size()+"=====");

			 if (buBrands != null && buBrands.size() > 0) {
				 // 設定額外欄位
				 this.setBuCurrencyLineOtherColumnBuBrand(buBrands);
				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buBrandDAO.search("BuBrand as model", "count(model.brandCode) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_BRAND_FIELD_NAMES, GRID_SEARCH_BRAND_FIELD_DEFAULT_VALUES, buBrands, gridDatas, firstIndex, maxIndex));
			 }else {
				 System.out.println("==NO DATA==");
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_BRAND_FIELD_NAMES, GRID_SEARCH_BRAND_FIELD_DEFAULT_VALUES, map,gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的品牌功能查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的品牌功能查詢功能查詢失敗！");
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
			 System.out.println("searchKeysssss:"+searchKeys.get(0));
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

	 /**
	  * 初始化 bean 額外顯示欄位
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map executeBuCountryInitial(Map parameterMap) throws Exception{
		 Map resultMap = new HashMap(0);

		 try{
			 BuCountry buCountry = this.executeFindActualBuCountry(parameterMap);

			 Map multiList = new HashMap(0);
			 resultMap.put("form", buCountry);

			 resultMap.put("multiList",multiList);
			 return resultMap;
		 }catch(Exception ex){
			 log.error("國別初始化失敗，原因：" + ex.toString());
			 throw new Exception("國別單初始化失敗，原因：" + ex.toString());

		 }
	 }

	 /**
	  * 依formId取得實際國別主檔
	  * @param parameterMap
	  * @return
	  * @throws FormException
	  * @throws Exception
	  */
	 public BuCountry executeFindActualBuCountry(Map parameterMap)
	 throws FormException, Exception {

//		 Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 BuCountry buCountry = null;
		 try {

			 String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
//			 Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			 buCountry = !StringUtils.hasText(formIdString) ? this.executeNewBuCountry(): this.findCountryById(formIdString);
             parameterMap.put( "entityBean", buCountry);
			 return buCountry;
		 } catch (Exception e) {
			 log.error("取得實際國別主檔失敗,原因:"+e.toString());
			 throw new Exception("取得實際國別主檔失敗,原因:"+e.toString());
		 }
	 }

	 /**
	  * 產生一筆 BuCountry
	  * @param otherBean
	  * @param isSave
	  * @return
	  * @throws Exception
	  */
	 public BuCountry executeNewBuCountry() throws Exception {

		 BuCountry form = new BuCountry();
		 form.setCountryCode("");
		 form.setCountryCName("");
		 form.setDescription("");
		 form.setEnable("Y");
		 form.setCountryEName("");

		 return form;

	 }

	 /**
	  * 將國別查詢結果存檔
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
	 public List<Properties> saveBuCountrySearchResult(Properties httpRequest) throws Exception{
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_COUNTRY_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }

	 /**
	  * 將幣別查詢結果存檔
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
	 public List<Properties> saveBuCurrencySearchResult(Properties httpRequest) throws Exception{
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_CURRENCY_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }

	 public List<Properties> saveBuExchangeRateSearchResult(Properties httpRequest) throws Exception{
		 System.out.println("oooooooooooooooooooooo");
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_EXCHANGE_RATE_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }

	 public List<Properties> saveBuBrandSearchResult(Properties httpRequest) throws Exception{
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_BRAND_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }

	 /**
	  * 設定國別額外欄位
	  * @param buCountrys
	  */
	 private void setBuCountryLineOtherColumn(List<BuCountry> buCountrys){
		 for (BuCountry buCountry : buCountrys) {
			 buCountry.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buCountry.getLastUpdatedBy()));
		 }
	 }

	 /**
	  * 設定幣別額外欄位
	  * @param buCurrencys
	  */
	 private void setBuCurrencyLineOtherColumn(List<BuCurrency> buCurrencys){
		 for (BuCurrency buCurrency : buCurrencys) {
			 buCurrency.setLastUpdatedBy(UserUtils.getUsernameByEmployeeCode(buCurrency.getLastUpdatedBy()));
		 }
	 }

	 /**
	  * 設定匯率額外欄位
	  * @param buCurrencys
	  */
	 private void setBuExchangeRateLineOtherColumn(List<BuExchangeRate> buBuExchangeRates){
		 for (BuExchangeRate buExchangeRate : buBuExchangeRates) {
			 buExchangeRate.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buExchangeRate.getLastUpdatedBy()));
		 }
	 }

	 private void setBuCurrencyLineOtherColumnBuBrand(List<BuBrand> buBrands){
		 for (BuBrand buBrand : buBrands) {
			 buBrand.setLastUpdatedBy(UserUtils.getUsernameByEmployeeCode(buBrand.getLastUpdatedBy()));
		 }
	 }
	 /**
	  * 前端資料塞入bean
	  * @param parameterMap
	  * @return
	  */
	 public void updateBuCountryBean(Map parameterMap)throws FormException, Exception {
		 try{
			 
			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			 Object otherBean = parameterMap.get("vatBeanOther");
			 String countryCode = (String) PropertyUtils.getProperty(formBindBean, "countryCode");
			 BuCountry buCountry = this.findCountryById(countryCode);
			 if(buCountry == null) buCountry = new BuCountry();
			 AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCountry);
			 
             log.info("buCountry.getCountryCode"+ buCountry.getCountryCode());
			 log.info("buLocation.getEnable"+ buCountry.getEnable());
			 parameterMap.put("entityBean", buCountry);
		 }  catch (Exception ex) {
			 log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			 throw new Exception("國別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 國別存檔
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map updateAJAXBuCountry(Map parameterMap) throws Exception {

		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 Date date = new Date();
		 try {
//			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean = parameterMap.get("vatBeanOther");

			 BuCountry buCountry = (BuCountry)parameterMap.get("entityBean");
			 String loginEmployeeCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginEmployeeCode");
			 String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			 if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				 log.info("buCountry.getEnable() = " + buCountry.getEnable());

				 buCountry.setEnable(("N".equals(buCountry.getEnable()) ? "Y" : "N"));
				 buCountry.setCountryCode(buCountry.getCountryCode().trim().toUpperCase());

				 buCountry.setLastUpdatedBy(loginEmployeeCode);
				 buCountry.setLastUpdateDate(date);
				 
				 // 若國別不存在,則SAVE 反之UPDATE
				 BuCountry buCountryUpdate = this.findCountryById(buCountry.getCountryCode());
				 if( buCountryUpdate == null ){
					 log.info("save");
					 buCountry.setCreatedBy(loginEmployeeCode);
					 buCountry.setCreationDate(date);
					 buCountryDAO.save(buCountry);
					 resultMsg = "Country Name：" + buCountry.getCountryCName() + "存檔成功！ 是否繼續新增？";
					 resultMap.put("isUpdate", "0");
				 }else{
					 log.info("update");
					 buCountryDAO.merge(buCountry);
					 resultMsg = "Country Name：" + buCountry.getCountryCName() + "更新成功！ 是否繼續新增？";
					 resultMap.put("isUpdate", "1");
				 }
			 }


			 resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buCountry);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 ex.printStackTrace();
			 log.error("國別維護單存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("國別維護單單存檔失敗，原因：" + ex.toString());
		 }
	 }
	 
	 public List<Properties> getAJAXBuCountrySearchPageData(Properties httpRequest) throws Exception{
		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String countryCode = httpRequest.getProperty("countryCode");
			 String countryCName = httpRequest.getProperty("countryCName");
			 String countryEName = httpRequest.getProperty("countryEName");

			 log.info("countryCode:"+countryCode);
			 log.info("countryCName:"+countryCName);
			 log.info("countryEName:"+countryEName);

			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
			 findObjs.put(" and model.countryCode like :countryCode","%"+countryCode+"%");
			 findObjs.put(" and model.countryCName like :countryCName","%"+countryCName+"%");
			 findObjs.put(" and model.countryEName like :countryEName","%"+countryEName+"%");


			 //==============================================================	    

			 Map buCountryMap = buCountryDAO.search( "BuCountry as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			 List<BuCountry> buCountrys = (List<BuCountry>) buCountryMap.get(BaseDAO.TABLE_LIST); 

			 log.info("buCountrys.size"+ buCountrys.size());	
			 if (buCountrys != null && buCountrys.size() > 0) {

				 // 設定額外欄位
				 this.setBuCountryLineOtherColumn(buCountrys);

				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buCountryDAO.search("BuCountry as model", "count(model.countryCode) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES, buCountrys, gridDatas, firstIndex, maxIndex));
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的國別功能查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的國別功能查詢發生錯誤，原因：" + ex.toString());
		 }	
	 }
	 

	 /**
	  * 驗證國別主檔
	  * @param parameterMap
	  * @throws Exception
	  */
	 public void validateBuCountryHead(Map parameterMap) throws Exception {

		 Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//		 Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

		 String countryCode = (String) PropertyUtils.getProperty(formBindBean, "countryCode");
		 String countryCName = (String) PropertyUtils.getProperty(formBindBean, "countryCName");
		 String countryEName = (String) PropertyUtils.getProperty(formBindBean, "countryEName");

		 // 驗證地點名稱
		 if(!StringUtils.hasText(countryCode)){
			 throw new ValidationErrorException("請輸入國別代碼！");
		 }else{
			 if( !StringUtils.hasText(formIdString) ){
				 BuCountry buCountry = this.findCountryById(countryCode);
				 if (buCountry != null) {
					 throw new ValidationErrorException("國別代碼：" + countryCode + "已經存在，請勿重複建立！");
				 }
			 }
		 }
		 // 驗證 中文與英文
		 if (!StringUtils.hasText(countryCName)) {
			 throw new ValidationErrorException("請輸入中文名稱！");
		 } else if (!StringUtils.hasText(countryEName)) {
			 throw new ValidationErrorException("請輸入英文名稱！");
		 }
	 }

	 /**
	  * 依據品牌代號、員工編號查詢出員工隸屬的POS機台
	  * 
	  * @param brandCode
	  * @param employeeCode
	  * @param isEnable
	  * @return List
	  * @throws Exception
	  */
	 public List<BuShopMachine> getShopMachineForEmployee(String brandCode,
			 String employeeCode, String isEnable) throws Exception {
		 try {
			 List<BuShopMachine> shopMachines = buShopMachineDAO.getShopMachineForEmployee(brandCode, employeeCode, isEnable);
			 if (shopMachines != null && shopMachines.size() > 0) {
				 return shopMachines;
			 } else {
				 throw new NoSuchDataException("查無品牌代號(" + brandCode + ")、工號("
						 + employeeCode + ")隸屬的POS機台資料！");
			 }
		 } catch (Exception ex) {
			 log.error("查詢員工隸屬POS機台時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢員工隸屬POS機台時發生錯誤，原因：" + ex.getMessage());
		 }
	 }
	 /**
	  * 依據品牌代號、員工編號查詢出員工隸屬的POS機台
	  * 
	  * @param brandCode
	  * @param employeeCode
	  * @param isEnable
	  * @return List
	  * @throws Exception
	  */
	 public List<BuShopMachine> getShopMachineForEmployee(String brandCode,
			 String employeeCode, String isEnable, String incharge) throws Exception {
		 try {
			 List<BuShopMachine> shopMachines = buShopMachineDAO.getShopMachineForEmployee(brandCode, employeeCode, isEnable, incharge);
			 if (shopMachines != null && shopMachines.size() > 0) {
				 return shopMachines;
			 } else {
				 throw new NoSuchDataException("查無品牌代號(" + brandCode + ")、工號("
						 + employeeCode + ")隸屬的POS機台資料！");
			 }
		 } catch (Exception ex) {
			 log.error("查詢員工隸屬POS機台時發生錯誤，原因：" + ex.toString());
			 throw new Exception("查詢員工隸屬POS機台時發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 透過傳遞過來的參數來做Currency與ExchangeRate下傳
	  * @param parameterMap
	  * @throws Exception
	  */
	 public Long executeCurrencyRateExport(HashMap parameterMap) throws Exception{
		 log.info("executeCurrencyRateExport");
		 Long responseId = -1L;
		 Long numbers = 0L;

		 //一、解析程式需要排程下傳或是即時下傳
		 Long batchId = (Long)parameterMap.get("BATCH_ID");
		 String uuId = posExportDAO.getDataId();// 產生dataId


		 //二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
		 if(null == batchId || batchId <= 0){
			 //輸入搜尋條件(排程)
			 //parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			 //parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			 //parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			 parameterMap.put("priceDate", DateUtils.format( (Date)parameterMap.get("DATA_PRICE_DATE"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			 List results = buCurrencyDAO.findCurrencyExchangeRateList(parameterMap);
			 if(results != null && results.size() > 0){
				 Iterator iterator = results.iterator();
				 while( iterator.hasNext()){

					 Object[] obj = (Object[])iterator.next();
					 PosCurrency posCurrency = new PosCurrency();
					 posCurrency.setCurrencyCode(String.valueOf(obj[0]));
					 posCurrency.setCurrencyCName(String.valueOf(obj[1]));
					 posCurrency.setCurrencyEName(String.valueOf(obj[2]));
					 posCurrency.setExchangeRate(NumberUtils.round(((BigDecimal)obj[3]).doubleValue(),2));			        
					 posCurrency.setBeginDate((Date)obj[4]);
					 //將addressBook與customer複製到posCustomer
					 posCurrency.setDataId(uuId);
					 posCurrency.setAction("U");
					 posExportDAO.save(posCurrency);
				 }
			 }else{
				 //查無資料
				 parameterMap.put("searchResult","noData");
			 }
		 }else{
			 //非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			 String dataId = (String)parameterMap.get("DATA_ID");
			 //尋找PosCustomer中此dataID有哪些需求資料
			 List<PosCurrency> posCurrencys = posExportDAO.findByProperty("PosCurrency", new String[]{"dataId"}, new Object[]{dataId});
			 for (Iterator iterator = posCurrencys.iterator(); iterator.hasNext();) {
				 PosCurrency posCurrency = (PosCurrency) iterator.next();
				 parameterMap.put("currencyCode", posCurrency.getCurrencyCode());
				 parameterMap.put("beginDate", DateUtils.format( posCurrency.getBeginDate(), DateUtils.C_DATA_PATTON_YYYYMMDD ) );
				 List results = buCurrencyDAO.findCurrencyExchangeRateList(parameterMap);
				 if(results != null && results.size() >= 0){
					 for (Iterator iterator1 = results.iterator(); iterator1.hasNext();) {
						 Object[] obj = (Object[])iterator1.next();
						 PosCurrency newPosCurrency = new PosCurrency();
						 newPosCurrency.setCurrencyCode(String.valueOf(obj[0]));
						 newPosCurrency.setCurrencyCName(String.valueOf(obj[1]));
						 newPosCurrency.setCurrencyEName(String.valueOf(obj[2]));
						 newPosCurrency.setExchangeRate(NumberUtils.round(((BigDecimal)obj[3]).doubleValue(),2));
						 newPosCurrency.setBeginDate((Date)obj[4]);
						 //將addressBook與customer複製到posCustomer
						 newPosCurrency.setDataId(uuId);
						 newPosCurrency.setAction("U");
						 posExportDAO.save(newPosCurrency);
					 }
				 }
			 }

		 }

		 //更新新的DATA_ID做回傳
		 parameterMap.put("DATA_ID", uuId);
		 parameterMap.put("NUMBERS", numbers);
		 responseId = posExportDAO.executeCommand(parameterMap);
		 return responseId;
	 }

	 public void updateBuBrandBean(Map parameterMap) throws Exception{
		 try{
			 Object formBindBean = parameterMap.get("vatBeanFormBind");

			 BuBrand buBrand = new BuBrand();
			 AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buBrand);

			 parameterMap.put("entityBean", buBrand);
		 } catch (FormException fe) {
			 log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			 throw new Exception("國別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 public Map updateAJAXBuBrand(Map parameterMap) throws Exception {

		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 Date date = new Date();
		 try {

			 Object otherBean = parameterMap.get("vatBeanOther");
			 String loginEmployeeCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginEmployeeCode");
			 BuBrand buBrand = (BuBrand)parameterMap.get("entityBean");
			 String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			 //當按下送出時將該筆資料寫入DB
			 if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				 buBrand.setLastUpdatedBy(loginEmployeeCode);
				 buBrand.setLastUpdateDate(date);
				 buBrandDAO.save(buBrand);
			 }
			 resultMsg = "Brand Name：" + buBrand.getBrandName()+ "存檔成功！ 是否繼續新增？";

			 resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buBrand);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 log.error("品牌維護單存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("品牌維護單單存檔失敗，原因：" + ex.toString());
		 }
	 }

	 /**
	  * 初始化 bean 額外顯示欄位
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map executeBuCurrencyInitial(Map parameterMap) throws Exception{
		 Map resultMap = new HashMap(0);

		 try{
			 List<BuCurrency> buCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
			 //BuCurrency buCurrency = this.executeFindActualBuCurrency(parameterMap);
			 BuCurrency rtnCurrency = new BuCurrency();
			 rtnCurrency.setCreationDate(DateUtils.getCurrentDate());
			 for(BuCurrency currency:buCurrency) {
				 if(currency.getCurrencyCode().equals("CNY")) {
					 rtnCurrency.setCNY(String.valueOf(currency.getExChangeRate()));
				 }else if(currency.getCurrencyCode().equals("JPY")) {
					 rtnCurrency.setJPY(String.valueOf(currency.getExChangeRate()));
				 }else if(currency.getCurrencyCode().equals("NTD")) {
					 rtnCurrency.setNTD(String.valueOf(currency.getExChangeRate()));
				 }else if(currency.getCurrencyCode().equals("USD")) {
					 rtnCurrency.setUSD(String.valueOf(currency.getExChangeRate()));
				 }
			 }

			 Map multiList = new HashMap(0);
			 System.out.println("allCurrency:"+buCurrency.size());

			 multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(buCurrency, "currencyCName", "currencyCode", true, true));
			 resultMap.put("form", rtnCurrency);
			 resultMap.put("multiList",multiList);
			 return resultMap;
		 }catch(Exception ex){
			 log.error("幣別初始化失敗，原因：" + ex.toString());
			 throw new Exception("幣別單初始化失敗，原因：" + ex.toString());

		 }
	 }

	 /**
	  * 初始化 bean 額外顯示欄位
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map executeBuExchangeInitial(Map parameterMap) throws Exception{
		 Map resultMap = new HashMap(0);
		 System.out.println("匯率Service");

		 try{

			 BuExchangeRate buExchangeRate = this.executeFindActualBuExchange(parameterMap);

			 Map multiList = new HashMap(0);
			 List<BuCommonPhraseLine> allExchangeRateType = buCommonPhraseLineDAO.findEnableLineById("ExchangeRateType");
			 List allSourceCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
			 multiList.put("allExchangeRateType", AjaxUtils.produceSelectorData(allExchangeRateType, "lineCode", "name", true, true));
			 multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
			 multiList.put("allAgainstCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
			 System.out.println("ExchangeRateType:"+allExchangeRateType.size());
			 System.out.println("allSourceCurrency:"+allSourceCurrency.size());

			 resultMap.put("form", buExchangeRate);
			 resultMap.put("multiList",multiList);
			 return resultMap;
		 } catch(Exception ex){
			 log.error("幣別初始化失敗，原因：" + ex.toString());
			 throw new Exception("幣別單初始化失敗，原因：" + ex.toString());

		 }
	 }

	 /**
	  * 依formId取得實際幣別主檔
	  * @param parameterMap
	  * @return
	  * @throws FormException
	  * @throws Exception
	  */
	 public BuCurrency executeFindActualBuCurrency(Map parameterMap)
	 throws FormException, Exception {

//		 Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 BuCurrency buCurrency = null;
		 try {

			 String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			 System.out.println("CurrencyFormId:"+formIdString);
//			 Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			 buCurrency = !StringUtils.hasText(formIdString) ? this.executeNewBuCurrency(): this.findCurrencyById(formIdString);


			 parameterMap.put( "entityBean", buCurrency);
			 return buCurrency;
		 } catch (Exception e) {
			 log.error("取得實際幣別主檔失敗,原因:"+e.toString());
			 throw new Exception("取得實際幣別主檔失敗,原因:"+e.toString());
		 }
	 }

	 /**
	  * 依formId取得實際匯率主檔
	  * @param parameterMap
	  * @return
	  * @throws FormException
	  * @throws Exception
	  */
	 public BuExchangeRate executeFindActualBuExchange(Map parameterMap)
	 throws FormException, Exception {
		 System.out.println("匯率executeFindActualBuExchange");
		 //Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 BuExchangeRate buExchangeRate = null;
		 BuExchangeRateId ids = null;
		 try {

			 String organizationCode = (String)PropertyUtils.getProperty(otherBean, "organizationCode");
			 String sourceCurrency = (String)PropertyUtils.getProperty(otherBean, "sourceCurrency");
			 String againstCurrency = (String)PropertyUtils.getProperty(otherBean, "againstCurrency");
			 String beginDate = (String)PropertyUtils.getProperty(otherBean, "beginDate");	    
			 System.out.println("匯率executeFindActualBuExchange1:"+beginDate);
			 Date beginDateMod = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,beginDate);
			 System.out.println("匯率executeFindActualBuExchange2:"+organizationCode);
			 System.out.println("匯率executeFindActualBuExchange3:"+sourceCurrency);
			 System.out.println("匯率executeFindActualBuExchange4:"+againstCurrency);
			 System.out.println("匯率executeFindActualBuExchange5:"+beginDateMod);
			 if( StringUtils.hasText(organizationCode) && StringUtils.hasText(sourceCurrency)
					 && StringUtils.hasText(againstCurrency) && (null != beginDateMod) )
				 ids= new BuExchangeRateId(organizationCode,sourceCurrency,againstCurrency,beginDateMod);
			 System.out.println("abcde:"+ids);
			 buExchangeRate = null == ids ? this.executeNewBuExchangeRate(): this.findBuExchangeRateById(ids);

			 parameterMap.put( "entityBean", buExchangeRate);
			 return buExchangeRate;
		 } catch (Exception e) {
			 log.error("取得實際匯率主檔失敗,原因:"+e.toString());
			 throw new Exception("取得實際匯率主檔失敗,原因:"+e.toString());
		 }
	 }

	 /**
	  * 產生一筆 BuExchangeRate
	  * @param otherBean
	  * @param isSave
	  * @return
	  * @throws Exception
	  */
	 public BuExchangeRate executeNewBuExchangeRate() throws Exception {
		 System.out.println("匯率executeNewBuExchangerate");
		 BuExchangeRate form = new BuExchangeRate();
		 form.setId(null);
		 form.setAgainstCurrency(null); 
		 form.setBeginDate(null);
		 form.setCreatedBy(null);
		 form.setCreationDate(null);
		 form.setExchangeRate(null);
		 form.setLastUpdateDate(null);
		 form.setLastUpdatedBy(null);
		 form.setOrganizationCode(null);  
		 form.setSourceCurrency(null);   
		 return form;
	 }

	 /**
	  * 產生一筆 BuCurrency
	  * @param otherBean
	  * @param isSave
	  * @return
	  * @throws Exception
	  */
	 public BuCurrency executeNewBuCurrency() throws Exception {

		 BuCurrency form = new BuCurrency();
		 form.setCurrencyCode("");
		 form.setCurrencyCName("");
		 form.setDescription("");
		 form.setEnable("Y");
		 form.setCurrencyEName("");
		 return form;
	 }

	 /**
	  * 前端資料塞入bean
	  * @param parameterMap
	  * @return
	  */
	 public void updateBuCurrencyBean(Map parameterMap)throws FormException, Exception {
		 try{
			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			 Object otherBean = parameterMap.get("vatBeanOther");

			 String currencyCode = (String) PropertyUtils.getProperty(formBindBean, "currencyCode");
			 BuCurrency buCurrency = this.findCurrencyById(currencyCode);
			 if(buCurrency == null) buCurrency = new BuCurrency();
			 AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCurrency);

			 log.info("buCountry.getCurrencyCode"+ buCurrency.getCurrencyCode());
			 log.info("buLocation.getEnable"+ buCurrency.getEnable());
			 parameterMap.put("entityBean", buCurrency);
		 } catch (FormException fe) {
			 log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			 throw new Exception("幣別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 前端資料塞入bean
	  * @param parameterMap
	  * @return
	  */
	 public void updateBuExchangeRateBean(Map parameterMap)throws FormException, Exception {
		 try{
			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			 Object otherBean = parameterMap.get("vatBeanOther");

			 String sourceCurrency = (String) PropertyUtils.getProperty(formBindBean, "sourceCurrency");
			 String organizationCode = (String) PropertyUtils.getProperty(formBindBean, "organizationCode");
			 String againstCurrency = (String) PropertyUtils.getProperty(formBindBean, "againstCurrency");
			 String beginDate = (String) PropertyUtils.getProperty(formBindBean, "beginDate");
			 Date beginDateMod = DateUtils.parseDate(beginDate);
			 BuExchangeRateId exChangeRateId = new BuExchangeRateId(organizationCode,sourceCurrency,againstCurrency,beginDateMod);

			 BuExchangeRate buExchangeRate = this.findBuExchangeRateById(exChangeRateId);

			 if(buExchangeRate == null) buExchangeRate = new BuExchangeRate();
			 AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buExchangeRate);

			 log.info("buExchangeRate.getExchangeRate"+ buExchangeRate.getExchangeRate());

			 parameterMap.put("entityBean", buExchangeRate);
		 } catch (FormException fe) {
			 log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			 throw new Exception("幣別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		 }
	 }

	 /**
	  * 幣別存檔
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map updateAJAXBuCurrency(Map parameterMap) throws Exception {
		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 Date date = new Date();
		 try {
//			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean = parameterMap.get("vatBeanOther");

			 String loginEmployeeCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginEmployeeCode");

			 BuCurrency buCurrency = (BuCurrency)parameterMap.get("entityBean");//什麼時候加進parameterMap裡面的?

			 String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			 if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				 log.info("buCountry.getEnable() = " + buCurrency.getEnable());

				 buCurrency.setEnable(("N".equals(buCurrency.getEnable()) ? "Y" : "N"));
				 buCurrency.setCurrencyCode(buCurrency.getCurrencyCode().trim().toUpperCase());

				 buCurrency.setLastUpdatedBy(loginEmployeeCode);
				 buCurrency.setLastUpdateDate(date);

				 // 若幣別不存在,則SAVE 反之UPDATE
				 BuCurrency buCurrencyUpdate = this.findCurrencyById(buCurrency.getCurrencyCode());
				 if( buCurrencyUpdate == null ){
					 log.info("save");
					 buCurrency.setCreatedBy(loginEmployeeCode);
					 buCurrency.setCreationDate(date);
					 buCurrencyDAO.save(buCurrency);
					 resultMsg = "Currency Name：" + buCurrency.getCurrencyCName() + "存檔成功！ 是否繼續新增？";
					 resultMap.put("isUpdate", "0");
				 }else{
					 log.info("update");
					 buCurrencyDAO.merge(buCurrency);
					 resultMsg = "Currency Name：" + buCurrency.getCurrencyCName() + "更新成功";
					 resultMap.put("isUpdate", "1");
				 }
			 }
			 //resultMsg = "Currency Name：" + buCurrency.getCurrencyCName() + "存檔成功！ 是否繼續新增？";

			 resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buCurrency);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 log.error("幣別維護單存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("幣別維護單單存檔失敗，原因：" + ex.toString());
		 }
	 }
	 
	 /**
	  * 更換Menu存檔
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map updateAJAXbuChangeMenu(Map parameterMap) throws Exception {
		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 log.info("20170317存檔");
		 try {
			 Object formBindBean = parameterMap.get("vatBeanFormBind");
			 
			 Object otherBean = parameterMap.get("vatBeanOther");
			 
			 String employeeRole = (String) PropertyUtils.getProperty(formBindBean, "employeeRole");
			 
			 String loginEmployeeCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginEmployeeCode");
			
			 BuEmployee buEmployee = buEmployeeDAO.findById(loginEmployeeCode);
			 
			 log.info("20170317存檔" + employeeRole +  "---" + loginEmployeeCode);
			 
			 
			 buEmployee.setEmployeeRole(employeeRole);
			 buEmployeeDAO.update(buEmployee);

			 resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buEmployee);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 log.error("更換Menu維護單存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("更換Menu維護單單存檔失敗，原因：" + ex.toString());
		 }
	 }

	 public Map updateAJAXBuExchangeRate(Map parameterMap) throws Exception {
		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 Date date = new Date();
		 try {
//			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean = parameterMap.get("vatBeanOther");

			 String loginEmployeeCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginEmployeeCode");
			 String loginBrandCode = (String) PropertyUtils.getProperty(
					 otherBean, "loginBrandCode");

			 BuExchangeRate buExchangeRate = (BuExchangeRate)parameterMap.get("entityBean");//什麼時候加進parameterMap裡面的?

			 String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			 if(OrderStatus.FORM_SUBMIT.equals(formAction)){

				 System.out.println("88888:"+buExchangeRate.getOrganizationCode());
				 System.out.println("77777:"+buExchangeRate.getSourceCurrency());
				 System.out.println("66666:"+buExchangeRate.getAgainstCurrency());
				 System.out.println("55555:"+buExchangeRate.getBeginDate()); 	


				 Date beginDateMod = DateUtils.getDateWithoutTime(buExchangeRate.getBeginDate());
				 BuExchangeRateId id = new BuExchangeRateId(buExchangeRate.getOrganizationCode(),buExchangeRate.getSourceCurrency(),buExchangeRate.getAgainstCurrency(),beginDateMod);
				 BuExchangeRate buExchangeRateUpdate = this.findBuExchangeRateById(id);

				 System.out.println("cdefg:"+buExchangeRateUpdate);
				 buExchangeRate.setId(id);
				 buExchangeRate.setOrganizationCode(loginBrandCode);
				 buExchangeRate.setAgainstCurrency(buExchangeRate.getAgainstCurrency());
				 buExchangeRate.setSourceCurrency(buExchangeRate.getSourceCurrency());
				 buExchangeRate.setExchangeRate(buExchangeRate.getExchangeRate());
				 buExchangeRate.setBeginDate(buExchangeRate.getBeginDate());
				 buExchangeRate.setLastUpdatedBy(loginEmployeeCode);
				 buExchangeRate.setLastUpdateDate(date);

				 buExchangeRate.setCreatedBy(loginEmployeeCode);
				 buExchangeRate.setCreationDate(date);
				 //buExchangeRateDAO.save(buExchangeRate);

				 // 若幣別不存在,則SAVE 反之UPDATE


				 if( buExchangeRateUpdate == null ){
					 log.info("save");
					 buExchangeRate.setCreatedBy(loginEmployeeCode);
					 buExchangeRate.setCreationDate(date);
					 buExchangeRateDAO.save(buExchangeRate);
				 }else{
					 log.info("update");
					 buExchangeRateDAO.merge(buExchangeRate);
				 }
			 }
			 resultMsg = "存檔成功！ 是否繼續新增？";

			 resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buExchangeRate);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 log.error("匯率維護單存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("匯率維護單單存檔失敗，原因：" + ex.toString());
		 }
	 }


	 /**
	  * 驗證幣別主檔
	  * @param parameterMap
	  * @throws Exception
	  */
	 public void validateBuCurrencyHead(Map parameterMap) throws Exception {//parameterMap包含哪些內容?在網頁端如何產生?

		 Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//		 Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

		 String currencyCode = (String) PropertyUtils.getProperty(formBindBean, "currencyCode");
		 String currencyCName = (String) PropertyUtils.getProperty(formBindBean, "currencyCName");
		 String currencyEName = (String) PropertyUtils.getProperty(formBindBean, "currencyEName");
		 String exChangeCode = (String) PropertyUtils.getProperty(formBindBean, "exChangeCode");
		 String orders = (String) PropertyUtils.getProperty(formBindBean, "orders");

		 // 驗證幣別名稱
		 if(!StringUtils.hasText(currencyCode)){
			 throw new ValidationErrorException("請輸入幣別代碼！");
		 }else{
			 if( !StringUtils.hasText(formIdString) ){
				 BuCurrency buCurrency = this.findCurrencyById(currencyCode);
				 if (buCurrency != null) {
					 throw new ValidationErrorException("幣別代碼：" + currencyCode + "已經存在，請勿重複建立！");
				 }
			 }
		 }
		 // 驗證 中文與英文
		 if(!StringUtils.hasText(currencyCName)){
			 throw new ValidationErrorException("請輸入中文名稱！");
		 }else if (!StringUtils.hasText(currencyEName)){
			 throw new ValidationErrorException("請輸入英文名稱！");
		 }else if (!StringUtils.hasText(exChangeCode)){
			 throw new ValidationErrorException("請輸入找零幣別！");
		 }
	 }

	 /**
	  * 驗證幣別主檔
	  * @param parameterMap
	  * @throws Exception
	  */
	 public void validateBuExchangeRateHead(Map parameterMap) throws Exception {//parameterMap包含哪些內容?在網頁端如何產生?

		 Object formBindBean = parameterMap.get("vatBeanFormBind");
//		 Object formLinkBean = parameterMap.get("vatBeanFormLink");
		 Object otherBean = parameterMap.get("vatBeanOther");

		 String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
//		 Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

		 String organizationCode = (String) PropertyUtils.getProperty(formBindBean, "organizationCode");
		 String sourceCurrency = (String) PropertyUtils.getProperty(formBindBean, "sourceCurrency");
		 String againstCurrency = (String) PropertyUtils.getProperty(formBindBean, "againstCurrency");
		 if(sourceCurrency.equals(againstCurrency)){
			 throw new ValidationErrorException("來原幣別與目標幣別不可相同!!");
		 }

		 /*
				// 驗證幣別名稱
				if(!StringUtils.hasText(currencyCode)){
					throw new ValidationErrorException("請輸入幣別代碼！");
				}else{
					if( !StringUtils.hasText(formIdString) ){
						BuCurrency buCurrency = this.findCurrencyById(currencyCode);
						if (buCurrency != null) {
						    throw new ValidationErrorException("幣別代碼：" + currencyCode + "已經存在，請勿重複建立！");
						}
					}
				}
				// 驗證 中文與英文
				if(!StringUtils.hasText(currencyCName)){
				    throw new ValidationErrorException("請輸入中文名稱！");
				}else if (!StringUtils.hasText(currencyEName)){
				    throw new ValidationErrorException("請輸入英文名稱！");
				}
		  */
	 }


	 /**
	  * ajax 取得幣別查詢的結果
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
	 public List<Properties> getAJAXBuCurrencySearchPageData(Properties httpRequest) throws Exception{

		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String currencyCode = httpRequest.getProperty("currencyCode");
			 String currencyCName = httpRequest.getProperty("currencyCName");
			 String currencyEName = httpRequest.getProperty("currencyEName");
			 String exChangeCode = httpRequest.getProperty("exChangeCode");
			 String orders = httpRequest.getProperty("orders");

			 log.info("currencyCode:"+currencyCode);
			 log.info("currencyCName:"+currencyCName);
			 log.info("currencyEName:"+currencyEName);
			 log.info("exChangeCode:"+exChangeCode);
			 log.info("orders:"+orders);

			 HashMap map = new HashMap();
			 HashMap findObjs = new HashMap();
			 findObjs.put(" and model.currencyCode like :currencyCode","%"+currencyCode+"%");
			 findObjs.put(" and model.currencyCName like :currencyCName","%"+currencyCName+"%");
			 findObjs.put(" and model.currencyEName like :currencyEName","%"+currencyEName+"%");
			 findObjs.put(" and model.exChangeCode like :exChangeCode","%"+exChangeCode+"%");
			 findObjs.put(" and model.orders like :orders","%"+orders+"%");

			 //==============================================================	    

			 Map buCurrencyMap = buCurrencyDAO.search( "BuCurrency as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			 List<BuCurrency> buCurrencys = (List<BuCurrency>) buCurrencyMap.get(BaseDAO.TABLE_LIST); 

			 log.info("buCurrencys.size"+ buCurrencys.size());	
			 if (buCurrencys != null && buCurrencys.size() > 0) {

				 // 設定額外欄位
				 this.setBuCurrencyLineOtherColumn(buCurrencys);

				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buCurrencyDAO.search("BuCurrency as model", "count(model.currencyCode) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_CURRENCY_FIELD_NAMES, GRID_SEARCH_CURRENCY_FIELD_DEFAULT_VALUES, buCurrencys, gridDatas, firstIndex, maxIndex));
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_CURRENCY_FIELD_NAMES, GRID_SEARCH_CURRENCY_FIELD_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的幣別功能查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的幣別功能");
		 }	
	 }

	 /**
	  * ajax 取得幣別查詢的結果
	  * @param httpRequest
	  * @return
	  * @throws Exception
	  */
	 public List<Properties> getAJAXBuExchangeRateSearchPageData(Properties httpRequest) throws Exception{
		 System.out.println("3333333333333333333333");
		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String organizationCode = httpRequest.getProperty("organizationCode");
			 String sourceCurrency = httpRequest.getProperty("sourceCurrency");
			 String againstCurrency = httpRequest.getProperty("againstCurrency");
			 String exchangeRate = httpRequest.getProperty("exchangeRate");
			 String beginDate = httpRequest.getProperty("beginDate");
			 String lastUpdatedBy = httpRequest.getProperty("lastUpdatedBy");
			 String lastUpdateDate = httpRequest.getProperty("lastUpdateDate");
			 Date date = null;
			 System.out.println("44444444444:"+beginDate);
			 log.info("organizationCode:"+organizationCode);
			 log.info("sourceCurrency:"+sourceCurrency);
			 log.info("againstCurrency:"+againstCurrency);
			 log.info("exchangeRate:"+exchangeRate);
			 log.info("beginDate:"+beginDate);
			 log.info("lastUpdatedBy:"+lastUpdatedBy);
			 log.info("lastUpdateDate:"+lastUpdateDate);
             
             HashMap map = new HashMap();
             HashMap findObjs = new HashMap();
		     findObjs.put(" and model.id.organizationCode = :organizationCode",organizationCode);

			 findObjs.put(" and model.id.sourceCurrency = :sourceCurrency",sourceCurrency);

			 findObjs.put(" and model.id.againstCurrency like :againstCurrency","%"+againstCurrency+"%");

			 findObjs.put(" and model.exchangeRate = :exchangeRate",exchangeRate);
             
			 if(StringUtils.hasText(beginDate)){
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
				 date = sdf.parse(beginDate); 
			}

			 findObjs.put(" and model.id.beginDate = :beginDate",date);

			 //==============================================================	    
			 System.out.println("eeeeeeeeeeeeeeeeeeeee");	

			 Map buExchangeRateMap = buExchangeRateDAO.search(" BuExchangeRate as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );

			 System.out.println("uuuuuuuuuuuuuuuuuuu");		  	    
			 List<BuExchangeRate> buBuExchangeRates = (List<BuExchangeRate>) buExchangeRateMap.get(BaseDAO.TABLE_LIST); 
			 System.out.println("tttttttttttt");
			 log.info("buBuExchangeRates.size"+ buBuExchangeRates.size());	
			 if (buBuExchangeRates != null && buBuExchangeRates.size() > 0) {

				 // 設定額外欄位
				 this.setBuExchangeRateLineOtherColumn(buBuExchangeRates);
				 System.out.println("rrrrrrrrrrrrrrr");
				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 System.out.println("pppppppppppppppppp");
				 Long maxIndex = (Long)buExchangeRateDAO.search(" BuExchangeRate as model", "count(model.id.organizationCode) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				 System.out.println("aaaaaaaaaaaaaaaaaa");
				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_EXCHANGE_RATE_FIELD_NAMES, GRID_SEARCH_EXCHANGE_RATE_FIELD_DEFAULT_VALUES, buBuExchangeRates, gridDatas, firstIndex, maxIndex));
				 System.out.println("zzzzzzzzzzzzzzzzz");
			 }else {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_EXCHANGE_RATE_FIELD_NAMES, GRID_SEARCH_EXCHANGE_RATE_FIELD_DEFAULT_VALUES, map, gridDatas));
			 }
			 return result;
		 }catch(Exception ex){
			 log.error("載入頁面顯示的匯率功能查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的幣別功能");
		 }	
	 }

	 public List<Properties> executeExSearchInitial(Map parameterMap) throws Exception {
		 Map resultMap = new HashMap(0);

		 try {
			 Object otherBean = parameterMap.get("vatBeanOther");
			 String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			 String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			 Map multiList = new HashMap(0);
			 List<BuCommonPhraseLine> allExchangeRateType = buCommonPhraseLineDAO.findEnableLineById("ExchangeRateType");
			 List allSourceCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
			 multiList.put("allExchangeRateType", AjaxUtils.produceSelectorData(allExchangeRateType, "lineCode", "name", true, true));
			 multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
			 multiList.put("allAgainstCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
			 System.out.println("ExchangeRateType:"+allExchangeRateType.size());
			 System.out.println("allSourceCurrency:"+allSourceCurrency.size());


			 resultMap.put("multiList", multiList);
		 } catch (Exception ex) {
			 log.error("查詢作業初始化失敗，原因：" + ex.toString());
			 Map messageMap = new HashMap();
			 messageMap.put("type", "ALERT");
			 messageMap.put("message", "查詢作業初始化失敗，原因：" + ex.toString());
			 messageMap.put("event1", null);
			 messageMap.put("event2", null);
			 resultMap.put("vatMessage", messageMap);

		 }
		 return AjaxUtils.parseReturnDataToJSON(resultMap);
	 }
	 
	 /**
	  * 清除LOG 初始化 bean 額外顯示欄位
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map executeBuDeleteLogInitial(Map parameterMap) throws Exception{
		 Map resultMap = new HashMap(0);

		 try{
			 List allBuDeleteLog = buDeleteLogDAO.findBuDeleteLogByEnable("Y");
			 //BuDeleteLog buDeleteLog = this.executeFindActualBuCurrency(parameterMap);
			 
			 Object otherBean = parameterMap.get("vatBeanOther");

		    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

		    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

		    Map multiList = new HashMap(0);
		    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
		    resultMap.put("employeeName", employeeName);
		    resultMap.put("employeeCode", loginEmployeeCode);
		    resultMap.put("brandCode", loginBrandCode);
		    //resultMap.put("form", buCurrency);
		    resultMap.put("multiList",multiList);
		    return resultMap;
		    
		 }catch(Exception ex){
			 log.error("清除LOG初始化失敗，原因：" + ex.toString());
			 throw new Exception("清除LOG初始化失敗，原因：" + ex.toString());

		 }
	 }
	 
	 /**
	  * 更換Menu 初始化 bean 額外顯示欄位
	  * @param parameterMap
	  * @return
	  * @throws Exception
	  */
	 public Map executeChangeMenuInitial(Map parameterMap) throws Exception{
		 Map resultMap = new HashMap(0);
		 try{
			
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			 
			//String employeeRole = (String) PropertyUtils.getProperty(formBindBean, "employeeRole");
			 
		    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		    //String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		    
		    BuEmployee buEmployee = buEmployeeDAO.findById(loginEmployeeCode);
		    Map multiList = new HashMap(0);
		    		 
		   // resultMap.put("employeeCode", loginEmployeeCode);
		   // resultMap.put("brandCode", loginBrandCode);
		    
		    resultMap.put("employeeRole", "");
		    resultMap.put("form", buEmployee);
		    resultMap.put("multiList",multiList);
		    return resultMap;
		    
		 }catch(Exception ex){
			 log.error("更換Menu始化失敗，原因：" + ex.toString());
			 throw new Exception("更換Menu初始化失敗，原因：" + ex.toString());

		 }
	 }
	 
	 public Map saveBuDeleteLog(Map parameterMap) throws Exception{
    	Map returnMap = new HashMap(0);
    	MessageBox msgBox = new MessageBox();
    	Map resultMap = new HashMap();
    	//DateFormat df = new SimpleDateFormat("yyyymmdd");
    	
    	try {			
			Object formBean = parameterMap.get("vatBeanFormBind");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		Object formLink = parameterMap.get("vatBeanFormLink");
    		
    		//String exportType = (String)PropertyUtils.getProperty(formLink, "exportType");
    		
		    String cleanTable = (String)PropertyUtils.getProperty(formBean ,"cleanTable");
		    //String reportDate = df.format(startDate);
		    String cleanDay = (String)PropertyUtils.getProperty(formBean, "cleanDay");
		    String cleanDate = (String)PropertyUtils.getProperty(formLink, "cleanDate");
		    String enable = (String)PropertyUtils.getProperty(formBean, "enable");
		    String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
		    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		    List data = buDeleteLogDAO.findByDeleteLogCleanTable(cleanTable);
		    if(data==null || data.size() == 0){
		    	log.info("建立新資料");
		    	BuDeleteLog buDeleteLog = new BuDeleteLog();
		    	buDeleteLog.setCleanTable(cleanTable);
		    	buDeleteLog.setCleanDay(Double.parseDouble(cleanDay));
		    	buDeleteLog.setCleanDate(DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, cleanDate));
		    	buDeleteLog.setEnable(enable);
		    	buDeleteLog.setCreatedBy(loginEmployeeCode);
		    	buDeleteLog.setCreationDate(new Date());
		    	buDeleteLog.setLastUpdatedBy(loginEmployeeCode);
		    	buDeleteLog.setLastUpdateDate(new Date());
		    	buDeleteLogDAO.save(buDeleteLog);
		    }else{
		    	log.info("舊資料更新");
		    	BuDeleteLog buDeleteLog = (BuDeleteLog) data.get(0);
		    	log.info("buDeleteLog="+ buDeleteLog);
		    	buDeleteLog.setCleanDay(Double.parseDouble(cleanDay));
		    	buDeleteLog.setEnable(enable);
		    	buDeleteLog.setLastUpdatedBy(loginEmployeeCode);
		    	buDeleteLog.setLastUpdateDate(new Date());
		    	buDeleteLogDAO.update(buDeleteLog);
		    }
		    
			log.info("cleanTable:"+cleanTable);
			log.info("cleanDay:"+cleanDay);
			log.info("cleanDate:"+cleanDate);			
		    		    
		    msgBox.setMessage("儲存成功");	    
			
		} catch (Exception ex) {
		    log.error("取得檔案錯誤，原因：" + ex.toString());
		    msgBox.setMessage(MessageStatus.VALIDATION_FAILURE);
		    throw new Exception("取得檔案錯誤！");
		}
		returnMap.put("vatMessage", msgBox);
    	return resultMap;
	}
	 
	 /**
	 * 處理AJAX參數(依據清除資料Table抓取初始日期)
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> findCleanDateByPropertyForAJAX(Properties httpRequest) throws Exception{
	
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	try{
        String cleanTable = httpRequest.getProperty("cleanTable");
        String cleanDate ="查無資料";
        cleanTable = cleanTable.trim().toUpperCase();
        cleanDate= buDeleteLogDAO.findCleanDate(cleanTable);
            
        properties.setProperty("cleanDate", cleanDate);
        result.add(properties);
    
        return result;
	}catch (Exception ex) {
	    log.error("依據清除資料Table抓取初始日期，原因：" + ex.toString());
	    throw new Exception("查詢清除資料Table抓取初始日期失敗！");
		}
	}
	
	public void deleteLogSchedule()throws Exception{
		log.info("開始執行刪除LOG作業");
		
		String uuid = UUID.randomUUID().toString();
		
		
		List buDeleteLogList = buDeleteLogDAO.findBuDeleteLogByEnable("Y");
		
		for(int i=0; i<buDeleteLogList.size();i++){
			long StartTime = System.currentTimeMillis(); // 取出目前時間
			BuDeleteLog buDeleteLog = (BuDeleteLog)buDeleteLogList.get(i);
			System.out.println(buDeleteLogList.get(i));
			System.out.println(buDeleteLog.getCleanTable());
			System.out.println(buDeleteLog.getCleanDate());
			this.deleteLogMethod(buDeleteLog);			
			long ProcessTime = System.currentTimeMillis() - StartTime; // 計算處理時間			
			buDeleteLog.setExecutionTime(String.valueOf(ProcessTime));
			buDeleteLogDAO.save(buDeleteLog);
			System.out.println("計算處理時間:"+ProcessTime);
			siSystemLogService.createSystemLog("BU_DELETE_LOG", MessageStatus.LOG_INFO,
			"開始執行"+buDeleteLog.getCleanTable()+"刪除LOG作業,清除筆數:"+buDeleteLog.getDeleteItems()+",執行多久時間(毫秒):"+buDeleteLog.getExecutionTime() , new Date(), uuid, "SYS");
		}		
	}
	
	public void deleteLogMethod(BuDeleteLog buDeleteLog) throws Exception{
		Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
    	String DataDateEnd = null;
    	Date cleanDate = null;  //清除的初始日期
    	int cleanDay = buDeleteLog.getCleanDay().intValue(); //清除的天數 轉換int
    	int deleteData = 0; //紀錄成功刪除筆數
    	try{
    		cleanDate = buDeleteLog.getCleanDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			sdf.format(cleanDate.getTime());
			Date finalDeclDate = DateUtils.addDays(cleanDate, cleanDay);  
			System.out.println("finalDeclDate =" +finalDeclDate );
			DataDateEnd = DateUtils.format(finalDeclDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			System.out.println("finalDeclDate =" +DataDateEnd );
			
    		conn = dataSource.getConnection();
			log.info("conn success!!");
			
			StringBuffer sql = new StringBuffer();
			stmt = conn.createStatement();
			//刪除LOG檔
			sql.append("DELETE FROM "+buDeleteLog.getCleanTable()+" where CREATION_DATE < to_date('"+DataDateEnd+"','yyyymmdd')");
			
			deleteData = stmt.executeUpdate(sql.toString());
			System.out.println("紀錄成功刪除筆數:"+deleteData);
			buDeleteLog.setCleanDate(finalDeclDate);
			buDeleteLog.setLastUpdateDate(new Date());
			buDeleteLog.setDeleteItems(Long.valueOf(deleteData));
			stmt.close();
    		conn.close();
    	}catch(Exception e){
    		stmt.close();
    		conn.close();
    	}
	}
	
	public void copyEDISchedule()throws Exception{
		log.info("開始執行EDI排程作業");
		
		String srFile = "//10.1.98.250/edi/Receive";
		//String dtFile = "D:/test2/";
		
		File f = new File(srFile);
	    File [] filesName = f.listFiles();
	    for(int i = 0 ; i < filesName.length ; i++){
	      
	      if(filesName[i].isFile()){
	    	  copyFile(filesName[i]);
	      }else if(filesName[i].isDirectory()){
	        System.out.println("無檔案");
	      }
	      
	    }		
	}
	
	private void copyFile(File name){
		
	String destination = "//10.1.98.250/edi_test/Receive";
    byte[] temp = new byte [4096]; 
    FileInputStream in = null;
    FileOutputStream out = null;
		try {
			in = new FileInputStream(name);
			out = new FileOutputStream(destination + "/" + name.getName());
			int end = in.read(temp,0,temp.length);
			while(end != -1){
			out.write(temp,0,temp.length);
			end = in.read(temp,0,temp.length);
			}
			System.out.println("複製成功");  
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
