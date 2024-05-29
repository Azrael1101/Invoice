package tw.com.tm.erp.hbm.service;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tw.com.tm.erp.hbm.bean.MessageBox;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuCustomerMod;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.BuAdminSub;

import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerModDAO;
import tw.com.tm.erp.hbm.dao.BuAdminSubDAO;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuCustomerModService { 
	
	private static final Log log = LogFactory.getLog(BuCustomerModService.class);

	private ImItemCategoryDAO imItemCategoryDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private BuCurrencyDAO buCurrencyDAO;
	private BaseDAO baseDAO;
	private BuPaymentTerm buPaymentTerm;
	private BuCountryDAO buCountryDAO;
	private BuCustomerModDAO buCustomerModDAO;
	private BuCustomerMod buCustomerMod;
	private BuBrandService buBrandService;
	private BuCustomerDAO buCustomerDAO;
	private BuCustomer buCustomer;
	private BuCustomerId buCustomerId;
	private BuAddressBook buAddressBook;
	private BuAddressBookDAO buAddressBookDAO;
	private BuAdminSubDAO buAdminSubDAO;

	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
		this.buCurrencyDAO = buCurrencyDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO){
			this.baseDAO = baseDAO;
	}
	public void setBuPaymentTerm(BuPaymentTerm buPaymentTerm){
			this.buPaymentTerm = buPaymentTerm;
	}
	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}
	public void setBuCustomerModDAO(BuCustomerModDAO buCustomerModDAO) {
		this.buCustomerModDAO = buCustomerModDAO;
	}
	public void setBuCustomerMod(BuCustomerMod buCustomerMod) {
		this.buCustomerMod = buCustomerMod;
	}
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}	
	public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
		this.buCustomerDAO = buCustomerDAO;
	}	
	public void setBuCustomer(BuCustomer buCustomer) {
		this.buCustomer = buCustomer;
	}	
	public void setBuCustomerId(BuCustomerId buCustomerId) {
		this.buCustomerId = buCustomerId;
	}	
	public void setBuAddressBook(BuAddressBook buAddressBook) {
		this.buAddressBook = buAddressBook;
	}
	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}
	public void setBuAdminSubDAO(BuAdminSubDAO buAdminSubDAO) {
		this.buAdminSubDAO = buAdminSubDAO;
	}	
	
	

	 
	 public static final String[] GRID_SEARCH_CUSTOMER_FIELD_NAMES = {
		 "id.customerCode","vipTypeCode","identityCode","chineseName","englishName",
		 "vipStartDate","vipEndDate","applicationDate","enable"
	 };
	
	 public static final String[] GRID_SEARCH_CUSTOMER_FIELD_DEFAULT_VALUES = { 
		 "", "", "", "", "",
		 "", "", "", ""
	 };
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/**主檔頁面初始化**/
		public Map executeInitial(Map parameterMap) throws Exception {
			Map resultMap = new HashMap(0);
			Date day = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	//前端BEAN載入
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean  = parameterMap.get("vatBeanFormBind");
	//從BEAN中取資料
			String brandCode = (String)PropertyUtils.getProperty(otherBean,"brandCode");
			String category = (String)PropertyUtils.getProperty(otherBean,"category");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			String status;
			try {


				BuCustomerMod buCustomerMod = this.executeFindActualMod(parameterMap);

				status= buCustomerMod.getStatus();
				
				
				if(category.equals("master")){
					BuCustomer buCustomer = this.executeFindActual(parameterMap);
					BuAddressBook buAddressBook = buAddressBookDAO.findByAddressBookId(buCustomer.getAddressBookId());
					buCustomerMod = this.copyMasterToBuCustomer(buCustomer,buAddressBook,buCustomerMod);


				}
				Map multiList = new HashMap(0);
				
				List<ImItemCategory> allCategroyTypes = imItemCategoryDAO.findByCategoryType(brandCode, "ITEM_CATEGORY");
				multiList.put("allCategroyTypes", AjaxUtils.produceSelectorData(allCategroyTypes, "categoryCode", "categoryName", true, true));
				//發票種類
				List allInvoiceType = buCommonPhraseLineDAO.findEnableLineById("InvoiceType");
				multiList.put("allInvoiceType", AjaxUtils.produceSelectorData(allInvoiceType, "lineCode", "name", true, true));
				//交付方式
				List allInvoiceDeliveryType = buCommonPhraseLineDAO.findEnableLineById("InvoiceDeliveryType");
				multiList.put("allInvoiceDeliveryType", AjaxUtils.produceSelectorData(allInvoiceDeliveryType, "lineCode", "name", true, true));
				//使用幣別
				List allSourceCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
				multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
				//付款條件
				List <BuPaymentTerm> allpaymentTermCode = baseDAO.findByProperty( "BuPaymentTerm",new String[] { "enable" },new Object[]{"Y"},"BuPaymentTerm");
				//List <BuPaymentTerm> allpaymentTermCode = buPaymentTermDAO.findPaymentTermByOrganizationAndEnable("id.organizationCode", "Y");
				multiList.put("allpaymentTermCode", AjaxUtils.produceSelectorData(allpaymentTermCode, "paymentTermCode", "name", true, true));
				//價格條件
				List allTradeTeam = buCommonPhraseLineDAO.findEnableLineById("TradeTeam");
				multiList.put("allTradeTeam", AjaxUtils.produceSelectorData(allTradeTeam, "lineCode", "name", true, false));
				//稅別
				List allTaxType = buCommonPhraseLineDAO.findEnableLineById("TaxType");
				multiList.put("allTaxType", AjaxUtils.produceSelectorData(allTaxType, "lineCode", "name", true, true));
				//國別
				List allCountry = buCountryDAO.findCountryByEnable("Y");
				multiList.put("allCountry", AjaxUtils.produceSelectorData(allCountry, "countryCode", "countryCName", true, true));
				//專櫃
				List<BuShop> allShops	= baseDAO.findByProperty( "BuShop"  , new String[] { "brandCode","enable" },new Object[] {brandCode	,"Y" });
				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));

				List allYearIncomeType = buCommonPhraseLineDAO.findEnableLineById("YearIncomeType");
				multiList.put("allYearIncomeType", AjaxUtils.produceSelectorData(allYearIncomeType, "lineCode", "name", true, true));

				List allJobType = buCommonPhraseLineDAO.findEnableLineById("JobType");
				multiList.put("allJobType", AjaxUtils.produceSelectorData(allJobType, "lineCode", "name", true, true));
				
				List allEducsteType = buCommonPhraseLineDAO.findEnableLineById("EducsteType");
				multiList.put("allEducsteType", AjaxUtils.produceSelectorData(allEducsteType, "lineCode", "name", true, true));

				//VIP類別
				List<BuCommonPhraseLine> allVipType	= baseDAO.findByProperty( "BuCommonPhraseLine"  , new String[] { "id.buCommonPhraseHead.headCode","attribute1","enable" },new Object[] {"VIPType" ,brandCode	,"Y" });
				multiList.put("allVipType", AjaxUtils.produceSelectorData(allVipType, "lineCode", "name",true, true));

				
				List<BuAdminSub> allCity	= buAdminSubDAO.findCity();
				multiList.put("allCity", AjaxUtils.produceSelectorData(allCity, "city", "city",false, true));
				
				List<BuAdminSub> allArea = baseDAO.findByProperty( "BuAdminSub"  , new String[] {"enable" },new Object[] { "Y" });
				multiList.put("allArea", AjaxUtils.produceSelectorData(allArea, "area", "area",false, true));
				
				
				resultMap.put("creationDate", sdf.format(day));
				resultMap.put("createdBy", loginEmployeeCode);

				resultMap.put("form", buCustomerMod);
				resultMap.put("multiList", multiList);
				resultMap.put("statusName",OrderStatus.getChineseWord(status));	

				
				return resultMap;

			} catch (Exception ex) {
				log.error("初始化失敗，原因：" + ex.toString());
				ex.printStackTrace();
				throw new Exception("初始化失敗，原因：" + ex.toString());

			}
		}
		
	public Map executePosInitial(Map parameterMap) throws Exception {
			Map resultMap = new HashMap(0);
			Date day = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	//前端BEAN載入
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean  = parameterMap.get("vatBeanFormBind");
	//從BEAN中取資料
			String brandCode = (String)PropertyUtils.getProperty(otherBean,"brandCode");
			String category = (String)PropertyUtils.getProperty(otherBean,"category");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			String customerCode = (String)PropertyUtils.getProperty(otherBean,"customerCode");
			String status;
			try {


				BuCustomerMod buCustomerMod = this.executeFindActualMod(parameterMap);

				status= buCustomerMod.getStatus();
				
				
				if(category.equals("master")){
					BuCustomer buCustomer = this.executeFindActual(parameterMap);
					BuAddressBook buAddressBook = buAddressBookDAO.findByAddressBookId(buCustomer.getAddressBookId());
					buCustomerMod = this.copyMasterToBuCustomer(buCustomer,buAddressBook,buCustomerMod);


				}
				Map multiList = new HashMap(0);
				
				List<ImItemCategory> allCategroyTypes = imItemCategoryDAO.findByCategoryType(brandCode, "ITEM_CATEGORY");
				multiList.put("allCategroyTypes", AjaxUtils.produceSelectorData(allCategroyTypes, "categoryCode", "categoryName", true, true));
				//發票種類
				List allInvoiceType = buCommonPhraseLineDAO.findEnableLineById("InvoiceType");
				multiList.put("allInvoiceType", AjaxUtils.produceSelectorData(allInvoiceType, "lineCode", "name", true, true));
				//交付方式
				List allInvoiceDeliveryType = buCommonPhraseLineDAO.findEnableLineById("InvoiceDeliveryType");
				multiList.put("allInvoiceDeliveryType", AjaxUtils.produceSelectorData(allInvoiceDeliveryType, "lineCode", "name", true, true));
				//使用幣別
				List allSourceCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
				multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
				//付款條件
				List <BuPaymentTerm> allpaymentTermCode = baseDAO.findByProperty( "BuPaymentTerm",new String[] { "enable" },new Object[]{"Y"},"BuPaymentTerm");
				//List <BuPaymentTerm> allpaymentTermCode = buPaymentTermDAO.findPaymentTermByOrganizationAndEnable("id.organizationCode", "Y");
				multiList.put("allpaymentTermCode", AjaxUtils.produceSelectorData(allpaymentTermCode, "paymentTermCode", "name", true, true));
				//價格條件
				List allTradeTeam = buCommonPhraseLineDAO.findEnableLineById("TradeTeam");
				multiList.put("allTradeTeam", AjaxUtils.produceSelectorData(allTradeTeam, "lineCode", "name", true, false));
				//稅別
				List allTaxType = buCommonPhraseLineDAO.findEnableLineById("TaxType");
				multiList.put("allTaxType", AjaxUtils.produceSelectorData(allTaxType, "lineCode", "name", true, true));
				//國別
				List allCountry = buCountryDAO.findCountryByEnable("Y");
				multiList.put("allCountry", AjaxUtils.produceSelectorData(allCountry, "countryCode", "countryCName", true, true));
				//專櫃
				List<BuShop> allShops	= baseDAO.findByProperty( "BuShop"  , new String[] { "brandCode","enable" },new Object[] {brandCode	,"Y" });
				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));

				List allYearIncomeType = buCommonPhraseLineDAO.findEnableLineById("YearIncomeType");
				multiList.put("allYearIncomeType", AjaxUtils.produceSelectorData(allYearIncomeType, "lineCode", "name", true, true));

				List allJobType = buCommonPhraseLineDAO.findEnableLineById("JobType");
				multiList.put("allJobType", AjaxUtils.produceSelectorData(allJobType, "lineCode", "name", true, true));
				
				List allEducsteType = buCommonPhraseLineDAO.findEnableLineById("EducsteType");
				multiList.put("allEducsteType", AjaxUtils.produceSelectorData(allEducsteType, "lineCode", "name", true, true));

				//VIP類別
				List<BuCommonPhraseLine> allVipType	= baseDAO.findByProperty( "BuCommonPhraseLine"  , new String[] { "id.buCommonPhraseHead.headCode","attribute1","enable" },new Object[] {"VIPType" ,brandCode	,"Y" });
				multiList.put("allVipType", AjaxUtils.produceSelectorData(allVipType, "lineCode", "name",true, true));

				
				List<BuAdminSub> allCity	= buAdminSubDAO.findCity();
				multiList.put("allCity", AjaxUtils.produceSelectorData(allCity, "city", "city",false, true));
				
				List<BuAdminSub> allArea = baseDAO.findByProperty( "BuAdminSub"  , new String[] {"enable" },new Object[] { "Y" });
				multiList.put("allArea", AjaxUtils.produceSelectorData(allArea, "area", "area",false, true));
				
				
				resultMap.put("creationDate", sdf.format(day));
				resultMap.put("createdBy", loginEmployeeCode);
				//======================================================================
				buCustomerMod.setCategory07(loginEmployeeCode);
				buCustomerMod.setIdentityCode(customerCode);
				buCustomerMod.setVipTypeCode(brandCode+"VIP");
				//======================================================================
				resultMap.put("form", buCustomerMod);
				resultMap.put("multiList", multiList);
				resultMap.put("statusName",OrderStatus.getChineseWord(status));	

				
				return resultMap;

			} catch (Exception ex) {
				log.error("初始化失敗，原因：" + ex.toString());
				ex.printStackTrace();
				throw new Exception("初始化失敗，原因：" + ex.toString());

			}
		}
	
	public BuCustomerMod copyMasterToBuCustomer(BuCustomer buCustomer,BuAddressBook buAddressBook,BuCustomerMod buCustomerMod)throws FormException, Exception
	{
		log.info(buAddressBook.getChineseName()+"會員資料");
		log.info("會員編號:"+buCustomer.getId().getCustomerCode()+"  通訊錄編號:"+buCustomer.getAddressBookId()+"   ID:"+buAddressBook.getIdentityCode());
	
		try {
			buCustomerMod.setBrandCode(buCustomer.getId().getBrandCode());
			buCustomerMod.setEnable(buCustomer.getEnable());
			buCustomerMod.setCustomerCode(buCustomer.getId().getCustomerCode());
			buCustomerMod.setAddressBookId(buAddressBook.getAddressBookId());
			buCustomerMod.setIdentityCode(buAddressBook.getIdentityCode());
			
			buCustomerMod.setCustomerTypeCode(buCustomer.getCustomerTypeCode());
			buCustomerMod.setVipStartDate(buCustomer.getVipStartDate());
			buCustomerMod.setVipEndDate(buCustomer.getVipEndDate());
			buCustomerMod.setVipTypeCode(buCustomer.getVipTypeCode());
			buCustomerMod.setApplicationDate(buCustomer.getApplicationDate());
			buCustomerMod.setTaxRate(buCustomer.getTaxRate());
			buCustomerMod.setTaxType(buCustomer.getTaxType());
			buCustomerMod.setCurrencyCode(buCustomer.getCurrencyCode());
			buCustomerMod.setCategory07(buCustomer.getCategory07());
			//MACO 2016.11.29 鐘錶額度
/*			buCustomerMod.setStockCredits(buCustomer.getStockCredits());
			buCustomerMod.setAdjCredits(buCustomer.getAdjCredits());
			buCustomerMod.setTotalUncommitCredits(buCustomer.getTotalUncommitCredits());*/
			buCustomerMod.setCategory01(buCustomer.getCategory01());
			buCustomerMod.setCategory02(buCustomer.getCategory02());
			buCustomerMod.setCategory03(buCustomer.getCategory03());
			buCustomerMod.setPaymentTermCode(buCustomer.getPaymentTermCode());
			buCustomerMod.setRemark1(buCustomer.getRemark1());
			buCustomerMod.setRemark2(buCustomer.getRemark2());
			buCustomerMod.setChineseName(buAddressBook.getChineseName());
			buCustomerMod.setEnglishName(buAddressBook.getEnglishName());
			buCustomerMod.setType(buAddressBook.getType());
			buCustomerMod.setShortName(buAddressBook.getShortName());
			buCustomerMod.setGender(buAddressBook.getGender());
			buCustomerMod.setCountryCode(buAddressBook.getCountryCode());
			buCustomerMod.setBirthdayYear(buAddressBook.getBirthdayYear());
			String birthdayMonthString = !(null == buAddressBook.getBirthdayDay())?buAddressBook.getBirthdayMonth().toString():"";
			String birthdayDayString = !(null == buAddressBook.getBirthdayDay())?buAddressBook.getBirthdayDay().toString():"";
			buCustomerMod.setBirthdayMonthString(birthdayMonthString);
			buCustomerMod.setBirthdayDayString(birthdayDayString);
			buCustomerMod.setEMail(buAddressBook.getEMail());
			buCustomerMod.setTel1(buAddressBook.getTel1());
			buCustomerMod.setTel2(buAddressBook.getTel2());
			buCustomerMod.setMobilePhone(buAddressBook.getMobilePhone());
			buCustomerMod.setCity(buAddressBook.getCity());
			buCustomerMod.setArea(buAddressBook.getArea());
			buCustomerMod.setZipCode(buAddressBook.getZipCode());
			buCustomerMod.setAddress(buAddressBook.getAddress());
			buCustomerMod.setDeliveryAddress(buAddressBook.getDeliveryAddress());  //宅配地址
			
			//法人使用欄位 Maco 2017.02.15 Maco		
			buCustomerMod.setTaxType(	buCustomer.getTaxType());	
			buCustomerMod.setTaxRate(	buCustomer.getTaxRate());	
			buCustomerMod.setCurrencyCode(	buCustomer.getCurrencyCode());	
			buCustomerMod.setInvoiceTypeCode(	buCustomer.getInvoiceTypeCode());	
			buCustomerMod.setPaymentTermCode(	buCustomer.getPaymentTermCode());	
			buCustomerMod.setCity1(	buCustomer.getCity1());	
			buCustomerMod.setArea1(	buCustomer.getArea1());	
			buCustomerMod.setZipCode1(	buCustomer.getZipCode1());	
			buCustomerMod.setAddress1(	buCustomer.getAddress1());	
			buCustomerMod.setCity2(	buCustomer.getCity2());	
			buCustomerMod.setArea2(	buCustomer.getArea2());	
			buCustomerMod.setZipCode2(	buCustomer.getZipCode2());	
			buCustomerMod.setAddress2(	buCustomer.getAddress2());	
			buCustomerMod.setCity3(	buCustomer.getCity3());	
			buCustomerMod.setArea3(	buCustomer.getArea3());	
			buCustomerMod.setZipCode3(	buCustomer.getZipCode3());	
			buCustomerMod.setAddress3(	buCustomer.getAddress3());	
					
			buCustomerMod.setContractPerson(	buAddressBook.getContractPerson());	

			

			
			return buCustomerMod;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		} 
	}
	/**產生實體主檔**/
	public BuCustomerMod executeFindActualMod(Map parameterMap)throws FormException, Exception
	{

	//	Object formBindBean = parameterMap.get("vatBeanFormBind");
	//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		BuCustomerMod buCustomerMod = null;
	
		try {			
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			String brandCode    = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			BuBrand     buBrand     = buBrandService.findById( brandCode );
			buCustomerMod = null == formId ? this.executeNewCustomer(brandCode) : this.findById(formId);
	
			parameterMap.put("entityBean", buCustomerMod);
			log.info("executefind:"+buCustomerMod.getHeadId());
			
			return buCustomerMod;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		} 
	}
	public BuCustomerMod findById(Long headId) throws Exception
	{
		try
		{
			BuCustomerMod form = (BuCustomerMod) buCustomerModDAO.findByPrimaryKey(BuCustomerMod.class, headId);
			log.info("查詢會員資料:"+form.getHeadId());
			return form;
					
		}
		catch (Exception ex)
		{
			log.error("依據主鍵：" + headId +" id時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢headId錯誤，原因："+ ex.getMessage());
		}
	}
	/**產生實體主檔**/
	public BuCustomer executeFindActual(Map parameterMap)throws FormException, Exception
	{

	//	Object formBindBean = parameterMap.get("vatBeanFormBind");
	//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		BuCustomer buCustomer = null;
	
		try {			
			String customerCode = (String) PropertyUtils.getProperty(otherBean,"customerCode");
			String brandCode    = (String)PropertyUtils.getProperty(otherBean, "brandCode");

			BuCustomerId buCustomerId = new BuCustomerId(customerCode,brandCode);
			buCustomer = (BuCustomer) buCustomerDAO.findByPrimaryKey(BuCustomer.class, buCustomerId);
	

			log.info("查詢會員編號:"+buCustomer.getId().getCustomerCode());
			
			return buCustomer;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		} 
	}
	private BuCustomerMod executeNewCustomer(String brandCode) throws Exception
	{
		


		BuCustomerMod form = new BuCustomerMod();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.add(Calendar.YEAR, 2); 
		
    
		form.setBrandCode(brandCode);
		form.setEnable("Y");
		form.setStatus(OrderStatus.SAVE);
		form.setVipStartDate(date);
        form.setVipEndDate(cal.getTime());
		form.setApplicationDate(date);
		form.setCreationDate(date);
		form.setInvoiceTypeCode("2");
		form.setTaxType("3");
		form.setTaxRate(5D);
		form.setCountryCode("TW");
		form.setCurrencyCode("NTD");
		form.setInvoiceDeliveryCode("1");
		form.setPaymentTermCode("Z9");
		form.setType("1");
		form.setCustomerTypeCode("1");
		
		
		buCustomerModDAO.save(form);
		log.info("新建會員資料:"+form.getHeadId());
		return form;
	}
	public void validateHead(Map parameterMap) throws Exception {
		//MessageBox msgBox = new MessageBox();
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		String brandCode = (String) PropertyUtils.getProperty(otherBean,"brandCode");

		String customerCode = (String) PropertyUtils.getProperty(formBindBean,"customerCode");
		String enable = (String) PropertyUtils.getProperty(formBindBean,"enable");
		String identityCode = (String) PropertyUtils.getProperty(formBindBean,"identityCode");
		String vipTypeCode = (String) PropertyUtils.getProperty(formBindBean,"vipTypeCode");
		String chineseName = (String) PropertyUtils.getProperty(formBindBean,"chineseName");
		if(customerCode.equals("")||enable.equals("")||identityCode.equals("")||vipTypeCode.equals("")||chineseName.equals("")){
			log.info("必要資訊未填寫");
			throw new Exception("必要資訊未填寫");
		}

		if(!("T2".equals(brandCode))){

			String birthdayYear = (String) PropertyUtils.getProperty(formBindBean,"birthdayYear");
			String birthdayMonth = (String) PropertyUtils.getProperty(formBindBean,"birthdayMonthString");
			String birthdayDay = (String) PropertyUtils.getProperty(formBindBean,"birthdayDayString");
			String category07 = (String) PropertyUtils.getProperty(formBindBean,"category07");
			String mobilePhone = (String) PropertyUtils.getProperty(formBindBean,"mobilePhone");
			String address = (String) PropertyUtils.getProperty(formBindBean,"address");
			if(birthdayYear.equals("")||birthdayMonth.equals("")||birthdayDay.equals("")||category07.equals("")||mobilePhone.equals("")||address.equals("")){
				log.info("必要資訊未填寫");
				throw new Exception("必要資訊未填寫");
			}

		}
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
		

		
		
		
	//	String supplierCode = (String) PropertyUtils.getProperty(formBindBean, "supplierCode");

		log.info("formId = " + formId);
		log.info("validate");
		
	}
	public void posSysValidateHead(Map parameterMap) throws Exception {
		//MessageBox msgBox = new MessageBox();
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		String brandCode = (String) PropertyUtils.getProperty(otherBean,"brandCode");

		String customerCode = (String) PropertyUtils.getProperty(formBindBean,"customerCode");
		String enable = (String) PropertyUtils.getProperty(formBindBean,"enable");
		String identityCode = (String) PropertyUtils.getProperty(formBindBean,"identityCode");
		String vipTypeCode = (String) PropertyUtils.getProperty(formBindBean,"vipTypeCode");
		String chineseName = (String) PropertyUtils.getProperty(formBindBean,"chineseName");
		if(customerCode.equals("")||enable.equals("")||identityCode.equals("")||vipTypeCode.equals("")||chineseName.equals("")){
			log.info("必要資訊未填寫");
			throw new Exception("必要資訊未填寫");
		}


		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
		

		
		
		
	//	String supplierCode = (String) PropertyUtils.getProperty(formBindBean, "supplierCode");

		log.info("formId = " + formId);
		log.info("validate");
		
	}

	public void updateCustomerModBean(Map parameterMap) throws Exception {
		BuCustomerMod buCustomerMod = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");

			buCustomerMod = (BuCustomerMod) parameterMap.get("entityBean");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCustomerMod);
			parameterMap.put("entityBean", buCustomerMod);
		}
		catch (Exception ex) {

			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
		
	}
	public Map updateAJAXBuCustomerMod(Map parameterMap) throws Exception {

			MessageBox msgBox = new MessageBox();
			HashMap resultMap = new HashMap(0);
			String resultMsg = null;
			Date date = new Date();

			try {

				 Object formBindBean = parameterMap.get("vatBeanFormBind");
			//	 Object formLinkBean = parameterMap.get("vatBeanFormLink");
				 Object otherBean = parameterMap.get("vatBeanOther");
				 String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				//---------------------------------------------------------------------------
				//取單頭資料
				 BuCustomerMod buCustomerMod = (BuCustomerMod) parameterMap.get("entityBean");
				//更新異動檔
				 buCustomerMod.setBirthdayDay(null==buCustomerMod.getBirthdayDayString()?0L:Long.parseLong(buCustomerMod.getBirthdayDayString()));
				 buCustomerMod.setBirthdayMonth(null==buCustomerMod.getBirthdayMonthString()?0L:Long.parseLong(buCustomerMod.getBirthdayMonthString()));
				 if(buCustomerMod.getHeadId()==0||buCustomerMod.getHeadId()==null){
					 log.info("新增異動檔");
					 buCustomerMod.setCreationDate(date);
					 buCustomerMod.setCreatedBy(loginEmployeeCode);
					 buCustomerMod.setLastUpdateDate(date);
					 buCustomerMod.setLastUpdatedBy(loginEmployeeCode);
					 buCustomerModDAO.save(buCustomerMod);	 
				 }
				 else
				 {
					 log.info("更新異動檔");
					 buCustomerMod.setLastUpdateDate(date);
					 buCustomerMod.setLastUpdatedBy(loginEmployeeCode);
					 buCustomerModDAO.update(buCustomerMod);
				 }

				 
				 buCustomerMod.setStatus("FINISH");
				
				//---------------------------------------------------------------------------
				if(OrderStatus.FINISH.equals(buCustomerMod.getStatus())){
					 buCustomerMod = this.updateMaster(buCustomerMod,date,loginEmployeeCode);
				}
				resultMsg = "存檔成功！ 是否繼續新增？";

				resultMap.put("resultMsg", resultMsg);
				resultMap.put("entityBean", buCustomerMod);
				resultMap.put("vatMessage", msgBox);

				return resultMap;

			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
				throw new Exception("維護單存檔失敗，原因：" + ex.toString());
			}
		}

	public BuCustomerMod updateMaster(BuCustomerMod buCustomerMod,Date date,String loginEmployeeCode) throws Exception
	{
		BuCustomerId buCustomerId = new BuCustomerId();
		buCustomerId.setBrandCode(buCustomerMod.getBrandCode());
		buCustomerId.setCustomerCode(buCustomerMod.getCustomerCode());
		BuCustomer vip = (BuCustomer)buCustomerDAO.findById("BuCustomer", buCustomerId);//找主檔
		
//若有--->更新
		if(null!=vip){
			//回寫主檔
			log.info("更新主檔");
			vip.setLastUpdateDate(date);
			vip.setLastUpdatedBy(loginEmployeeCode);
			log.info(buCustomerMod.getAddressBookId());
			log.info("更新通訊錄主檔");
			List<BuAddressBook> buAddressBooks = buAddressBookDAO.findByIdentityCode(buCustomerMod.getIdentityCode());
			if(buAddressBooks != null && buAddressBooks.size() > 0){
				log.info("原ADDRESS_BOOK_ID:"+vip.getAddressBookId());
				buAddressBook = buAddressBooks.get(0);
				buAddressBook.setLastUpdateDate(date);
				buAddressBook.setLastUpdatedBy(loginEmployeeCode);
				buAddressBook = buAddressBookDAO.findByAddressBookId(vip.getAddressBookId());
				buCustomerMod.setAddressBookId(vip.getAddressBookId());
			}
			else{
				
				buAddressBook = new BuAddressBook();
				buAddressBook.setOrganizationCode("TM");
				buAddressBook.setType(buCustomerMod.getType());
				buAddressBook.setIdentityCode(buCustomerMod.getIdentityCode());
				buAddressBook.setCreationDate(date);
				buAddressBook.setCreatedBy(loginEmployeeCode);
				buAddressBook.setLastUpdateDate(date);
				buAddressBook.setLastUpdatedBy(loginEmployeeCode);
				buAddressBookDAO.save(buAddressBook);
				log.info("原ADDRESS_BOOK_ID:"+vip.getAddressBookId()+"/新ADDRESS_BOOK_ID:"+buAddressBook.getAddressBookId());
				vip.setAddressBookId(buAddressBook.getAddressBookId());
				buCustomerMod.setAddressBookId(buAddressBook.getAddressBookId());
			}

			buAddressBook.setLastUpdateDate(date);
			buAddressBook.setLastUpdatedBy(loginEmployeeCode);
			
			
			copyMasterData(vip,buAddressBook,buCustomerMod);					
			
			
			buCustomerDAO.update(vip);
			buAddressBookDAO.update(buAddressBook);
		}
//若無--->新增
		else
		{
			log.info("新增通訊錄主檔");
			List<BuAddressBook> buAddressBooks = buAddressBookDAO.findByIdentityCode(buCustomerMod.getIdentityCode());
			if(buAddressBooks != null && buAddressBooks.size() > 0){
				buAddressBook = buAddressBooks.get(0);
				buAddressBook.setLastUpdateDate(date);
				buAddressBook.setLastUpdatedBy(loginEmployeeCode);

			}
			else{
				buAddressBook = new BuAddressBook();
				buAddressBook.setOrganizationCode("TM");
				buAddressBook.setType(buCustomerMod.getType());
				buAddressBook.setIdentityCode(buCustomerMod.getIdentityCode());
				buAddressBook.setCreationDate(date);
				buAddressBook.setCreatedBy(loginEmployeeCode);
				buAddressBook.setLastUpdateDate(date);
				buAddressBook.setLastUpdatedBy(loginEmployeeCode);
				buAddressBookDAO.save(buAddressBook);
			}
			log.info("新增主檔");
			vip = new BuCustomer();
			vip.setId(buCustomerId);
			vip.setAddressBookId(buAddressBook.getAddressBookId());
			buCustomerMod.setAddressBookId(buAddressBook.getAddressBookId());
			vip.setCreationDate(date);
			vip.setCreatedBy(loginEmployeeCode);
			vip.setLastUpdateDate(date);
			vip.setLastUpdatedBy(loginEmployeeCode);
			buCustomerDAO.save(vip);
			
			copyMasterData(vip,buAddressBook,buCustomerMod);
			
			
			buAddressBookDAO.update(buAddressBook);
			buCustomerDAO.update(vip);
			
		}
		
		
		
		return buCustomerMod;
	}
	public static void copyMasterData(BuCustomer buCustomer,BuAddressBook buAddressBook,BuCustomerMod buCustomerMod) throws Exception
	{
		
		buCustomer.setEnable(buCustomerMod.getEnable());
		buCustomer.setCustomerTypeCode(buCustomerMod.getCustomerTypeCode());
		buCustomer.setVipTypeCode(buCustomerMod.getVipTypeCode());
		buCustomer.setVipStartDate(buCustomerMod.getVipStartDate());
		buCustomer.setVipEndDate(buCustomerMod.getVipEndDate());
		buCustomer.setApplicationDate(buCustomerMod.getApplicationDate());
		//buCustomer.setTaxType(buCustomerMod.getTaxType());
		//buCustomer.setTaxRate(buCustomerMod.getTaxRate());
		buCustomer.setCurrencyCode(buCustomerMod.getCurrencyCode());
		buCustomer.setCategory07(buCustomerMod.getCategory07());
		//buCustomer.setPaymentTermCode(buCustomerMod.getPaymentTermCode());
		//buCustomer.setInvoiceDeliveryCode(buCustomerMod.getInvoiceDeliveryCode());
		//buCustomer.setInvoiceTypeCode(buCustomerMod.getInvoiceTypeCode());
		buCustomer.setRemark1(buCustomerMod.getRemark1());
		buCustomer.setRemark2(buCustomerMod.getRemark2());
		buCustomer.setRemark3(buCustomerMod.getRemark3());
		
		
		
		buCustomer.setTaxType(buCustomerMod.getTaxType());
		buCustomer.setTaxRate(buCustomerMod.getTaxRate());
		buCustomer.setCurrencyCode(buCustomerMod.getCurrencyCode());
		buCustomer.setInvoiceTypeCode(buCustomerMod.getInvoiceTypeCode());
		
		buCustomer.setPaymentTermCode(buCustomerMod.getPaymentTermCode());
		buCustomer.setZipCode1(buCustomerMod.getZipCode1());
		buCustomer.setCity1(buCustomerMod.getCity1());
		buCustomer.setArea1(buCustomerMod.getArea1());
		buCustomer.setAddress1(buCustomerMod.getAddress1());
		buCustomer.setZipCode2(buCustomerMod.getZipCode2());
		buCustomer.setCity2(buCustomerMod.getCity2());
		buCustomer.setArea2(buCustomerMod.getArea2());
		buCustomer.setAddress2(buCustomerMod.getAddress2());
		buCustomer.setZipCode3(buCustomerMod.getZipCode3());
		buCustomer.setCity3(buCustomerMod.getCity3());
		buCustomer.setArea3(buCustomerMod.getArea3());
		buCustomer.setAddress3(buCustomerMod.getAddress3());

		
		
		//buCustomer.setCategory01(buCustomerMod.getCategory01());
		//buCustomer.setCategory02(buCustomerMod.getCategory02());
		//buCustomer.setCategory03(buCustomerMod.getCategory03());
		//MACO 2016.11.29 鐘錶額度
/*		buCustomer.setStockCredits(buCustomerMod.getStockCredits());
		buCustomer.setAdjCredits(buCustomerMod.getAdjCredits());*/
		//buCustomer.setTotalUncommitCredits(buCustomerMod.getTotalUncommitCredits());//商控僅更新期初額度與加減額度

		
		buAddressBook.setIdentityCode(buCustomerMod.getIdentityCode());
		buAddressBook.setChineseName(buCustomerMod.getChineseName());
		buAddressBook.setEnglishName(buCustomerMod.getEnglishName());
		buAddressBook.setShortName(buCustomerMod.getShortName());
		buAddressBook.setGender(buCustomerMod.getGender());
		buAddressBook.setCountryCode(buCustomerMod.getCountryCode());
		buAddressBook.setBirthdayYear(buCustomerMod.getBirthdayYear());
		buAddressBook.setBirthdayMonth(buCustomerMod.getBirthdayMonth());
		buAddressBook.setBirthdayDay(buCustomerMod.getBirthdayDay());
		buAddressBook.setEMail(buCustomerMod.getEMail());
		buAddressBook.setCity(buCustomerMod.getCity());
		buAddressBook.setArea(buCustomerMod.getArea());
		buAddressBook.setZipCode(buCustomerMod.getZipCode());
		buAddressBook.setAddress(buCustomerMod.getAddress());
		buAddressBook.setMobilePhone(buCustomerMod.getMobilePhone());
		buAddressBook.setTel1(buCustomerMod.getTel1());
		buAddressBook.setTel2(buCustomerMod.getTel2());
		buAddressBook.setType(buCustomerMod.getCustomerTypeCode());
		buAddressBook.setContractPerson(buCustomerMod.getContractPerson());
		buAddressBook.setDeliveryAddress(buCustomerMod.getDeliveryAddress()); //宅配地址
	}
//選擇城市
    public List<Properties> getAJAXCityCategory(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();

		String city     = httpRequest.getProperty("city1");
		List allArea = baseDAO.findByProperty( "BuAdminSub"  , new String[] {"city","enable" },new Object[] {city, "Y" });
		log.info("size:"+allArea.size());
		allArea = AjaxUtils.produceSelectorData(allArea, "area", "area",false, true);
		pro.setProperty("allArea", AjaxUtils.parseSelectorData(allArea));
		log.info("city:"+city);
		pro.setProperty("city",city);
		re.add(pro);
		return re;
	    }
 //選擇區域
    public List<Properties> getAJAXAreaCategory(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();

		String area     = httpRequest.getProperty("area1");
		String city     = httpRequest.getProperty("city1");
		List<BuAdminSub> buAdminSubs =  buAdminSubDAO.findByArea(city,area);
		BuAdminSub buAdminSub = buAdminSubs.get(0);
		log.info("headId"+buAdminSub.getHeadId());
		pro.setProperty("area",buAdminSub.getArea());
		pro.setProperty("zipCode",buAdminSub.getZipCode());
		re.add(pro);
		return re;
	    }
    public List<Properties> getAJAXFormDataByCustomer(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();

		String customerCode     = httpRequest.getProperty("customerCode");
		String brandCode        = httpRequest.getProperty("brandCode");
		String identityCode     = httpRequest.getProperty("identityCode");
		String changeData       = httpRequest.getProperty("changeData");
		try{
			log.info("customerCode:"+customerCode);
			log.info("brandCode:"+brandCode);
			log.info("identityCode:"+identityCode);
			log.info("changeData:"+changeData);
			if("customerCode".equals(changeData)){
				BuCustomerId buCustomerId = new BuCustomerId(customerCode,brandCode);
				BuCustomer buCustomer = (BuCustomer)buCustomerDAO.findById("BuCustomer",buCustomerId);
				if (null != buCustomer ) {
					log.info("addressBookId:"+buCustomer.getAddressBookId());
					BuAddressBook buAddressBook = buAddressBookDAO.findByAddressBookId(buCustomer.getAddressBookId());
					log.info("identityCode:"+buAddressBook.getIdentityCode());
					log.info("chineseName:"+buAddressBook.getChineseName());
					pro.setProperty("customerCode",AjaxUtils.getPropertiesValue(buCustomerId.getCustomerCode(),  ""));
				    pro.setProperty("brandCode",AjaxUtils.getPropertiesValue(buCustomerId.getBrandCode(),""));
				    pro.setProperty("identityCode",AjaxUtils.getPropertiesValue(buAddressBook.getIdentityCode(),""));
				    pro.setProperty("addressBookId",AjaxUtils.getPropertiesValue(buAddressBook.getAddressBookId(),""));
				    this.copyCustomerMain(buCustomer,pro);
				    this.copyAddressbook(buAddressBook,pro);
				    pro.setProperty("orderType","old");
		
				} /*else {
					pro.setProperty("customerCode",customerCode);
					pro.setProperty("brandCode",brandCode);
					pro.setProperty("orderType","new");
				}*/
			}
			else if("identityCode".equals(changeData))
			{
				List<BuAddressBook> buAddressBooks = buAddressBookDAO.findByIdentityCode(identityCode);
				if(buAddressBooks != null && buAddressBooks.size() > 0){
					BuAddressBook buAddressBook = buAddressBooks.get(0);
					pro.setProperty("identityCode",AjaxUtils.getPropertiesValue(buAddressBook.getIdentityCode(),""));
					pro.setProperty("addressBookId",AjaxUtils.getPropertiesValue(buAddressBook.getAddressBookId(),""));
					this.copyAddressbook(buAddressBook,pro);
					pro.setProperty("orderType","old");
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		log.info(pro.getProperty("vipStartDate"));
		re.add(pro);
		return re;
	}
	private void copyCustomerMain(BuCustomer buCustomer,Properties pro) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if(null!=buCustomer.getVipStartDate())
		{
			pro.setProperty("vipStartDate",AjaxUtils.getPropertiesValue(sdf.format(buCustomer.getVipStartDate()),  ""));
		}
		else
		{
			pro.setProperty("vipStartDate","");
		}
		if(null!=buCustomer.getVipStartDate())
		{
			pro.setProperty("vipEndDate",AjaxUtils.getPropertiesValue(sdf.format(buCustomer.getVipEndDate()),  ""));
		}
		else
		{
			pro.setProperty("vipEndDate","");
		}
		if(null!=buCustomer.getVipStartDate())
		{
			pro.setProperty("applicationDate",AjaxUtils.getPropertiesValue(sdf.format(buCustomer.getApplicationDate()),  ""));
		}
		else
		{
			pro.setProperty("applicationDate","");
		}
		//MACO 2016.11.29 鐘錶額度
/*		pro.setProperty("stockCredits",AjaxUtils.getPropertiesValue(buCustomer.getStockCredits(),  ""));
		pro.setProperty("adjCredits",AjaxUtils.getPropertiesValue(buCustomer.getAdjCredits(),  ""));
		pro.setProperty("totalUncommitCredits",AjaxUtils.getPropertiesValue(buCustomer.getTotalUncommitCredits(),  ""));*/
		pro.setProperty("customerTypeCode",AjaxUtils.getPropertiesValue(buCustomer.getCustomerTypeCode(),  ""));
		pro.setProperty("vipTypeCode",AjaxUtils.getPropertiesValue(buCustomer.getVipTypeCode(),  ""));
		pro.setProperty("taxRate",AjaxUtils.getPropertiesValue(buCustomer.getTaxRate(),  ""));
		pro.setProperty("taxType",AjaxUtils.getPropertiesValue(buCustomer.getTaxType(),  ""));
		pro.setProperty("currencyCode",AjaxUtils.getPropertiesValue(buCustomer.getCurrencyCode(),  ""));
		pro.setProperty("category07",AjaxUtils.getPropertiesValue(buCustomer.getCategory07(),  ""));
		pro.setProperty("category01",AjaxUtils.getPropertiesValue(buCustomer.getCategory01(),  ""));
		pro.setProperty("category02",AjaxUtils.getPropertiesValue(buCustomer.getCategory02(),  ""));
		pro.setProperty("category03",AjaxUtils.getPropertiesValue(buCustomer.getCategory03(),  ""));
		pro.setProperty("paymentTermCode",AjaxUtils.getPropertiesValue(buCustomer.getPaymentTermCode(),  ""));
		pro.setProperty("remark1",AjaxUtils.getPropertiesValue(buCustomer.getRemark1(),  ""));
		pro.setProperty("remark2",AjaxUtils.getPropertiesValue(buCustomer.getRemark2(),  ""));
		pro.setProperty("remark3",AjaxUtils.getPropertiesValue(buCustomer.getRemark3(),  ""));

		
		//法人使用欄位 Maco 2017.02.15 Maco
		pro.setProperty("taxType",AjaxUtils.getPropertiesValue(buCustomer.getTaxType(),  ""));
		pro.setProperty("taxRate",AjaxUtils.getPropertiesValue(buCustomer.getTaxRate(),  ""));
		pro.setProperty("currencyCode",AjaxUtils.getPropertiesValue(buCustomer.getCurrencyCode(),  ""));
		pro.setProperty("invoiceTypeCode",AjaxUtils.getPropertiesValue(buCustomer.getInvoiceTypeCode(),  ""));
		pro.setProperty("paymentTermCode",AjaxUtils.getPropertiesValue(buCustomer.getPaymentTermCode(),  ""));
		pro.setProperty("city1",AjaxUtils.getPropertiesValue(buCustomer.getCity1(),  ""));
		pro.setProperty("area1",AjaxUtils.getPropertiesValue(buCustomer.getArea1(),  ""));
		pro.setProperty("zipCode1",AjaxUtils.getPropertiesValue(buCustomer.getZipCode1(),  ""));
		pro.setProperty("address1",AjaxUtils.getPropertiesValue(buCustomer.getAddress1(),  ""));
		pro.setProperty("city2",AjaxUtils.getPropertiesValue(buCustomer.getCity2(),  ""));
		pro.setProperty("area2",AjaxUtils.getPropertiesValue(buCustomer.getArea2(),  ""));
		pro.setProperty("zipCode2",AjaxUtils.getPropertiesValue(buCustomer.getZipCode2(),  ""));
		pro.setProperty("address2",AjaxUtils.getPropertiesValue(buCustomer.getAddress2(),  ""));
		pro.setProperty("city3",AjaxUtils.getPropertiesValue(buCustomer.getCity3(),  ""));
		pro.setProperty("area3",AjaxUtils.getPropertiesValue(buCustomer.getArea3(),  ""));
		pro.setProperty("zipCode3",AjaxUtils.getPropertiesValue(buCustomer.getZipCode3(),  ""));
		pro.setProperty("address3",AjaxUtils.getPropertiesValue(buCustomer.getAddress3(),  ""));

	}
	private void copyAddressbook(BuAddressBook buAddressBook,Properties pro) {
		pro.setProperty("addressbookId",AjaxUtils.getPropertiesValue(buAddressBook.getAddressBookId(),  ""));
		pro.setProperty("chineseName",AjaxUtils.getPropertiesValue(buAddressBook.getChineseName(),  ""));
		pro.setProperty("englishName",AjaxUtils.getPropertiesValue(buAddressBook.getEnglishName(),  ""));
		pro.setProperty("type",AjaxUtils.getPropertiesValue(buAddressBook.getType(),  ""));
		pro.setProperty("shortName",AjaxUtils.getPropertiesValue(buAddressBook.getShortName(),  ""));
		pro.setProperty("gender",AjaxUtils.getPropertiesValue(buAddressBook.getGender(),  ""));
		pro.setProperty("countryCode",AjaxUtils.getPropertiesValue(buAddressBook.getCountryCode(),  ""));
		pro.setProperty("birthdayYear",AjaxUtils.getPropertiesValue(buAddressBook.getBirthdayYear(),  ""));
		pro.setProperty("birthdayMonth",AjaxUtils.getPropertiesValue(buAddressBook.getBirthdayMonth(),  ""));
		pro.setProperty("birthdayDay",AjaxUtils.getPropertiesValue(buAddressBook.getBirthdayDay(),  ""));
		pro.setProperty("tel1",AjaxUtils.getPropertiesValue(buAddressBook.getTel1(),  ""));
		pro.setProperty("tel2",AjaxUtils.getPropertiesValue(buAddressBook.getTel2(),  ""));
		pro.setProperty("mobilePhone",AjaxUtils.getPropertiesValue(buAddressBook.getMobilePhone(),  ""));
		pro.setProperty("city",AjaxUtils.getPropertiesValue(buAddressBook.getCity(),  ""));
		pro.setProperty("area",AjaxUtils.getPropertiesValue(buAddressBook.getArea(),  ""));
		pro.setProperty("zipCode",AjaxUtils.getPropertiesValue(buAddressBook.getZipCode(),  ""));
		pro.setProperty("address",AjaxUtils.getPropertiesValue(buAddressBook.getAddress(),  ""));
		pro.setProperty("EMail",AjaxUtils.getPropertiesValue(buAddressBook.getEMail(),  ""));

		//法人使用欄位 Maco 2017.02.15 Maco
		pro.setProperty("contractPerson",AjaxUtils.getPropertiesValue(buAddressBook.getContractPerson(),  ""));
	}

    	
	    /** 取得稅率
	     * @param headObj
	     */
	    private String getTaxRate(String taxType) {
		//log.info("getTaxRate");
		Double tmp = new Double(0);
		if (StringUtils.hasText(taxType)) {
		    if (PoPurchaseOrderHead.PURCHASE_ORDER_TAX.equalsIgnoreCase(taxType)) {	// 3
			tmp = new Double(5);
		    }
		}
		return tmp.toString();
	    }
		public Map executeSearchInitial(Map parameterMap) throws Exception {
			Map resultMap = new HashMap(0);
			Date day = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	//前端BEAN載入
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean  = parameterMap.get("vatBeanFormBind");
	//從BEAN中取資料
			String brandCode = (String)PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			try {
				Map multiList = new HashMap(0);
				
				List<ImItemCategory> allCategroyTypes = imItemCategoryDAO.findByCategoryType(brandCode, "ITEM_CATEGORY");
				multiList.put("allCategroyTypes", AjaxUtils.produceSelectorData(allCategroyTypes, "categoryCode", "categoryName", true, true));
				//發票種類
				List allInvoiceType = buCommonPhraseLineDAO.findEnableLineById("InvoiceType");
				multiList.put("allInvoiceType", AjaxUtils.produceSelectorData(allInvoiceType, "lineCode", "name", true, true));
				//交付方式
				List allInvoiceDeliveryType = buCommonPhraseLineDAO.findEnableLineById("InvoiceDeliveryType");
				multiList.put("allInvoiceDeliveryType", AjaxUtils.produceSelectorData(allInvoiceDeliveryType, "lineCode", "name", true, true));
				//使用幣別
				List allSourceCurrency = buCurrencyDAO.findCurrencyByEnable("Y");
				multiList.put("allSourceCurrency", AjaxUtils.produceSelectorData(allSourceCurrency, "currencyCode", "currencyCName", true, true));
				//付款條件
				List <BuPaymentTerm> allpaymentTermCode = baseDAO.findByProperty( "BuPaymentTerm",new String[] { "enable" },new Object[]{"Y"},"BuPaymentTerm");
				//List <BuPaymentTerm> allpaymentTermCode = buPaymentTermDAO.findPaymentTermByOrganizationAndEnable("id.organizationCode", "Y");
				multiList.put("allpaymentTermCode", AjaxUtils.produceSelectorData(allpaymentTermCode, "paymentTermCode", "name", true, true));
				//價格條件
				List allTradeTeam = buCommonPhraseLineDAO.findEnableLineById("TradeTeam");
				multiList.put("allTradeTeam", AjaxUtils.produceSelectorData(allTradeTeam, "lineCode", "name", true, false));
				//稅別
				List allTaxType = buCommonPhraseLineDAO.findEnableLineById("TaxType");
				multiList.put("allTaxType", AjaxUtils.produceSelectorData(allTaxType, "lineCode", "name", true, true));
				//國別
				List allCountry = buCountryDAO.findCountryByEnable("Y");
				multiList.put("allCountry", AjaxUtils.produceSelectorData(allCountry, "countryCode", "countryCName", true, true));
				//專櫃
				List<BuShop> allShops	= baseDAO.findByProperty( "BuShop"  , new String[] { "brandCode","enable" },new Object[] {brandCode	,"Y" });
				multiList.put("allShop", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName",true, true));

				List allYearIncomeType = buCommonPhraseLineDAO.findEnableLineById("YearIncomeType");
				multiList.put("allYearIncomeType", AjaxUtils.produceSelectorData(allYearIncomeType, "lineCode", "name", true, true));

				List allJobType = buCommonPhraseLineDAO.findEnableLineById("JobType");
				multiList.put("allJobType", AjaxUtils.produceSelectorData(allJobType, "lineCode", "name", true, true));
				
				List allEducsteType = buCommonPhraseLineDAO.findEnableLineById("EducsteType");
				multiList.put("allEducsteType", AjaxUtils.produceSelectorData(allEducsteType, "lineCode", "name", true, true));

				//VIP類別
				List<BuCommonPhraseLine> allVipType	= baseDAO.findByProperty( "BuCommonPhraseLine"  , new String[] { "id.buCommonPhraseHead.headCode","attribute1","enable" },new Object[] {"VIPType" ,brandCode	,"Y" });
				multiList.put("allVipType", AjaxUtils.produceSelectorData(allVipType, "lineCode", "name",true, true));

				
				List<BuAdminSub> allCity	= buAdminSubDAO.findCity();
				multiList.put("allCity", AjaxUtils.produceSelectorData(allCity, "city", "city",false, true));
				
				List<BuAdminSub> allArea = baseDAO.findByProperty( "BuAdminSub"  , new String[] {"enable" },new Object[] { "Y" });
				multiList.put("allArea", AjaxUtils.produceSelectorData(allArea, "area", "area",false, true));
				
				
				resultMap.put("creationDate", sdf.format(day));
				resultMap.put("createdBy", loginEmployeeCode);

				resultMap.put("form", buCustomerMod);
				resultMap.put("multiList", multiList);


				
				return resultMap;

			} catch (Exception ex) {
				log.error("初始化失敗，原因：" + ex.toString());
				ex.printStackTrace();
				throw new Exception("初始化失敗，原因：" + ex.toString());

			}
		}

		public List<Properties> getAJAXSearchPageData(Properties httpRequest)throws Exception
		{
			log.info("search1111111111111111");
			try {
				List<Properties> result = new ArrayList();
				List<Properties> gridDatas = new ArrayList();
				int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
				int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
				String brandCode = httpRequest.getProperty("brandCode");
				String customerCode = httpRequest.getProperty("customerCode");
				String vipTypeCode = httpRequest.getProperty("vipTypeCode");
				
				String enable = httpRequest.getProperty("enable");
				String identityCode = httpRequest.getProperty("identityCode");
				String gender = httpRequest.getProperty("gender");
				String vipStartDate = httpRequest.getProperty("vipStartDate");
				String vipEndDate = httpRequest.getProperty("vipEndDate");
				String applicationDate = httpRequest.getProperty("applicationDate");
				String chineseName = httpRequest.getProperty("chineseName");
				String englishName = httpRequest.getProperty("englishName");
				String birthdayYear = httpRequest.getProperty("birthdayYear");
				String birthdayMonth = httpRequest.getProperty("birthdayMonth");
				String birthdayDate = httpRequest.getProperty("birthdayDate");
				String countryCode = httpRequest.getProperty("countryCode");
				
	             HashMap map = new HashMap();
	             HashMap findObjs = new HashMap();
			     findObjs.put(" and model.id.brandCode = :brandCode",brandCode);
			     findObjs.put(" and model.id.customerCode = :customerCode",customerCode);
			     findObjs.put(" and model.vipTypeCode = :vipTypeCode",vipTypeCode);
			     
			     findObjs.put(" and model.enable = :enable",enable);
			     findObjs.put(" and model1.identityCode = :identityCode",identityCode);
			     findObjs.put(" and model1.gender = :gender",gender);
	             if(StringUtils.hasText(vipStartDate))
	             {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
					 Date date = sdf.parse(vipStartDate); 
					 findObjs.put(" and model.vipStartDate = :vipStartDate",date);
				}
	            if(StringUtils.hasText(vipEndDate))
	            {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
					 Date date = sdf.parse(vipEndDate); 
					 findObjs.put(" and model.vipEndDate = :vipEndDate",date);
				}
	            if(StringUtils.hasText(applicationDate))
	            {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
					 Date date = sdf.parse(applicationDate); 
					 findObjs.put(" and model.applicationDate = :applicationDate",date);
				} 
	             findObjs.put(" and model1.chineseName like :chineseName","%"+chineseName+"%");
	             findObjs.put(" and model1.englishName like :englishName","%"+englishName+"%");
			     //findObjs.put(" and model1.englishName = :englishName",englishName);
			     findObjs.put(" and model1.birthdayYear = :birthdayYear",birthdayYear);
			     findObjs.put(" and model1.birthdayMonth = :birthdayMonth",birthdayMonth);
			     findObjs.put(" and model1.birthdayDay = :birthdayDate",birthdayDate);
			     findObjs.put(" and model1.countryCode = :countryCode",countryCode);
			     
				 //findObjs.put(" and model.id.againstCurrency like :againstCurrency","%"+againstCurrency+"%");
				 //findObjs.put(" and model.exchangeRate = :exchangeRate",exchangeRate);
				 
				 


/*	             if(StringUtils.hasText(beginDate))
	             {
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
					 date = sdf.parse(beginDate); 
				}

				 findObjs.put(" and model.id.beginDate = :beginDate",date);*/


				 Map buCustomerMap = buCustomerDAO.search(" BuCustomer as model,BuAddressBook as model1", "model" , findObjs, " and model.addressBookId = model1.addressBookId order by model.id.customerCode ", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
				 List<BuCustomer> buCustomers = (List<BuCustomer>) buCustomerMap.get(BaseDAO.TABLE_LIST); 

				 log.info("buCustomers.size"+ buCustomers.size());	
				 if (buCustomers != null && buCustomers.size() > 0) {

					 // 設定額外欄位
					 for(BuCustomer buCustomer : buCustomers)
						{
							BuAddressBook addressbook = buAddressBookDAO.findByAddressBookId(buCustomer.getAddressBookId());
							if(addressbook!=null)
							{
								buCustomer.setIdentityCode(addressbook.getIdentityCode());
								buCustomer.setChineseName(addressbook.getChineseName());
								buCustomer.setEnglishName(addressbook.getEnglishName());
								log.info(buCustomer.getIdentityCode());
								log.info(buCustomer.getChineseName());
								log.info(buCustomer.getEnglishName());
							}
						}
					 
					 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 

					 Long maxIndex = (Long)buCustomerDAO.search(" BuCustomer as model,BuAddressBook as model1 ", "count(model.id.customerCode) as rowCount" ,findObjs, " and model.addressBookId = model1.addressBookId order by model.id.customerCode ", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

					 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_CUSTOMER_FIELD_NAMES, GRID_SEARCH_CUSTOMER_FIELD_DEFAULT_VALUES, buCustomers, gridDatas, firstIndex, maxIndex));

				 }else {
					 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_CUSTOMER_FIELD_NAMES, GRID_SEARCH_CUSTOMER_FIELD_DEFAULT_VALUES, map, gridDatas));
				 }
				 return result;
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
				throw new Exception("載入頁面顯示的功能查詢失敗！");
			}
		}
		public List<Properties> saveSearchResult(Properties httpRequest)throws Exception {
				String errorMsg = null;
				AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_CUSTOMER_FIELD_NAMES);
				return AjaxUtils.getResponseMsg(errorMsg);
		}
	    public List<Properties> getSearchSelection(Map parameterMap)throws FormException, Exception {
			Map resultMap = new HashMap(0);
			Map pickerResult = new HashMap(0);
			try {
			    log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			    Object pickerBean = parameterMap.get("vatBeanPicker");
			    String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
			    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty( pickerBean, AjaxUtils.SEARCH_KEY);
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
	    public void executeImportCustomer(String brandCode,String loginEmployeeCode,List customerLists) throws Exception{
    		try{

    			Date date = new Date();
    			Calendar cal = Calendar.getInstance();  
    	        cal.setTime(date);  
    	        cal.add(Calendar.YEAR, 2); 
    			if(customerLists != null && customerLists.size() > 0)
    			{
    				
    				for(int i = 0; i < customerLists.size(); i++)
    				{
    					
    					BuCustomerMod  buCustomerMod = this.executeNewCustomer(brandCode);
    					Long headId = buCustomerMod.getHeadId();
    					buCustomerMod  = (BuCustomerMod)customerLists.get(i);
    					buCustomerMod.setHeadId(headId);
      					buCustomerMod.setBrandCode(brandCode);
      					//百貨自動帶入VIP代碼
      					if(!"T2".equals(brandCode)){
      						buCustomerMod.setVipTypeCode(brandCode+"VIP");
      					}
      					else{
      						buCustomerMod.setVipTypeCode("T2");
      					}
      					if(buCustomerMod.getGender().equals("男"))
      					{
      						buCustomerMod.setGender("M");
      					}
      					else if(buCustomerMod.getGender().equals("女"))
      					{
      						buCustomerMod.setGender("F");
      					}
      					else
      					{
      						buCustomerMod.setGender("");
      					}
    					buCustomerMod.setEnable("Y");
    					buCustomerMod.setStatus(OrderStatus.FINISH);
    					buCustomerMod.setVipStartDate(date);
    					buCustomerMod.setVipEndDate(cal.getTime());
    					buCustomerMod.setApplicationDate(date);
    					buCustomerMod.setCreationDate(date);
    					buCustomerMod.setInvoiceTypeCode("2");
    					buCustomerMod.setTaxType("3");
    					buCustomerMod.setTaxRate(5D);
    					buCustomerMod.setCountryCode("TW");
    					buCustomerMod.setCurrencyCode("NTD");
    					buCustomerMod.setInvoiceDeliveryCode("1");
    					buCustomerMod.setPaymentTermCode("Z9");
    					buCustomerMod.setType("1");
    					buCustomerMod.setCustomerTypeCode("1");
    					String[] tokens = buCustomerMod.getBirthday().split("-|/|\\.");
    					buCustomerMod.setBirthdayYear(Long.parseLong(tokens[0]));
    					buCustomerMod.setBirthdayMonth(Long.parseLong(tokens[1]));
    					buCustomerMod.setBirthdayDay(Long.parseLong(tokens[2]));
    					buCustomerMod.setApplicationDate(DateUtils.parseDate("yyyy/MM/dd", buCustomerMod.getApplicationDateString()));
    					//主檔更新
    					buCustomerMod = this.updateMaster(buCustomerMod,date,loginEmployeeCode);
    					this.saveOrUpdateCustomer(buCustomerMod);
    					buCustomerModDAO.merge(buCustomerMod);

    				}

    			}
    		}
    		catch (Exception ex) 
    		{
    			log.error("調整單明細匯入時發生錯誤，原因：" + ex.toString());
    			ex.printStackTrace();
    			throw new Exception("調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
    		}
    	}
	    public void saveOrUpdateCustomer(BuCustomerMod buCustomerMod) throws Exception{
    		try{
    			log.info("==============="+buCustomerMod.getChineseName()+"===================");
    			log.info("流水號/HEAD_ID:"+buCustomerMod.getHeadId());
    			log.info("會員編號/CUSTOMER_CODE:"+buCustomerMod.getCustomerCode());
    			log.info("中文名稱/CHINESE_NAME:"+buCustomerMod.getChineseName());
    			log.info("統一編號/IDENTITY_CODE:"+buCustomerMod.getIdentityCode());
    			log.info("生日/BIRTHDAY"+buCustomerMod.getBirthdayYear()+"/"+buCustomerMod.getBirthdayMonth()+"/"+buCustomerMod.getBirthdayDay());
    			log.info("電話/TEL1:"+buCustomerMod.getTel1());
    			log.info("手機/MOBILE_PHONE:"+buCustomerMod.getMobilePhone());
    			log.info("城市/CITY:"+buCustomerMod.getCity());
    			log.info("區域/AREA:"+buCustomerMod.getArea());
    			log.info("郵遞區號/ZIP_CODE:"+buCustomerMod.getZipCode());
    			log.info("地址/ADDRESS:"+buCustomerMod.getAddress());
    			log.info("申請日/APPLICATION_DATE:"+buCustomerMod.getApplicationDate());
    			log.info("E-MAIL/EMAIL:"+buCustomerMod.getEMail());
    			log.info("櫃點/CATEGORY07:"+buCustomerMod.getCategory07());
    			log.info("===================================================");
    			
    			
    			
    		}
    		catch (Exception ex) 
    		{
    			log.error("調整單明細匯入時發生錯誤，原因：" + ex.toString());
    			ex.printStackTrace();
    			throw new Exception("調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
    		}
    	}
	    
}