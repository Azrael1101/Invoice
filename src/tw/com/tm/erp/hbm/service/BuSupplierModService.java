package tw.com.tm.erp.hbm.service;
 
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierModDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuSupplierModService {
	
	private static final Log log = LogFactory.getLog(BuSupplierModService.class);
	
	private BuSupplierModDAO buSupplierModDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	private BuCommonPhraseLine buCommonPhraseLine;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO){
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}
	public void setBuCommonPhraseLine(BuCommonPhraseLine buCommonPhraseLine){
		this.buCommonPhraseLine = buCommonPhraseLine;
	}
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	
	public void setBuSupplierModDAO(BuSupplierModDAO buSupplierModDAO) {
		this.buSupplierModDAO = buSupplierModDAO;
	}
	
	private BuSupplierDAO buSupplierDAO;

	public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
		this.buSupplierDAO = buSupplierDAO;
	}
	
	private BuCurrencyDAO buCurrencyDAO;
	
	public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
		this.buCurrencyDAO = buCurrencyDAO;
	}
	private BuCountryDAO buCountryDAO;
	
	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;
	
	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setBuCommonPhraseHeadDAO(
			BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
		this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
	}
	private BuPaymentTermDAO buPaymentTermDAO;
	
	public void setBuPaymentTermDAO(BuPaymentTermDAO buPaymentTermDAO){
		this.buPaymentTermDAO = buPaymentTermDAO;
	}
	private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;
	public void setBuSupplierWithAddressViewDAO(BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO){
		this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
	}
	
	
	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	public BuBrandService getBuBrandService() {
	    return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
	    this.buBrandService = buBrandService;
	}
	private BuSupplierWithAddressViewService buSupplierWithAddressViewService;
	
	public void setBuSupplierWithAddressViewService(BuSupplierWithAddressViewService buSupplierWithAddressViewService) {
		this.buSupplierWithAddressViewService = buSupplierWithAddressViewService;
	}
	
	private BuBasicDataService    buBasicDataService;
	
	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}
	
	private ImLetterOfCreditService      imLetterOfCreditService;
	
	public void setImLetterOfCreditService(ImLetterOfCreditService imLetterOfCreditService) {
		this.imLetterOfCreditService = imLetterOfCreditService;
	}
	
	private BuSupplierId buSupplierId;
	
	 public BaseDAO baseDAO;
	 public void setBaseDAO(BaseDAO baseDAO){
			this.baseDAO = baseDAO;
		}
	 public BuAddressBookDAO buAddressBookDAO;
	 public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO){
			this.buAddressBookDAO = buAddressBookDAO;
		}
	 
	 public BuEmployeeDAO buEmployeeDAO;
	 public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO){
			this.buEmployeeDAO = buEmployeeDAO;
		}
	 
	 public static final String[] GRID_SEARCH_SUPPLIERMOD_FIELD_NAMES = {
		 "supplierCode", "headId","status",
		 "lastUpdatedBy", "lastUpdateDate", "headId"
	 };
	
	 public static final String[] GRID_SEARCH_SUPPLIERMOD_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 "", "", "" 
	 };
	
	 public static final String[] GRID_SEARCH_SUPPLIER_FIELD_NAMES = {
		 "supplierCode", "identityCode", "chineseName",
		 "enable"
	 };
	
	 public static final String[] GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES = { 
		 "", "", "",
		 ""
	 };
	/**
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */

	public Map executeInitial(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		Object formBindBean  = parameterMap.get("vatBeanFormBind");
		
		Map resultMap = new HashMap(0);
		Date day = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String brandCode     = (String)PropertyUtils.getProperty(otherBean,     "brandCode");
		String loginEmployeeCode    = (String)PropertyUtils.getProperty(otherBean,     "loginEmployeeCode");
		String category08= (String)PropertyUtils.getProperty(otherBean,     "category");
		//String supplierCode  = (String)PropertyUtils.getProperty(formBindBean, "supplierCode");
		try {
			
			BuSupplierMod buSupplierMod = this.executeFindActual(parameterMap);
			
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
			//廠商類別
			List allSupplierType = buCommonPhraseLineDAO.findEnableLineById("SupplierType");
			multiList.put("allSupplierType", AjaxUtils.produceSelectorData(allSupplierType, "lineCode", "name", true, true));
			//廠商類型
			List allCategoryCode = buCommonPhraseLineDAO.findEnableLineById("SupplierClass");
			multiList.put("allCategoryCode", AjaxUtils.produceSelectorData(allCategoryCode, "lineCode", "name", true, true));
			//付款條件
			List <BuPaymentTerm> allpaymentTermCode = baseDAO.findByProperty( "BuPaymentTerm",new String[] { "enable" },new Object[]{"Y"},"id.paymentTermCode");
//			List <BuPaymentTerm> allpaymentTermCode = baseDAO.findByProperty( "BuPaymentTerm",new String[] { "enable" },new Object[]{"Y"},"BuPaymentTerm");
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
			//部門別
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			HashMap findObjs = new HashMap();
			findObjs.put("employeeCode", loginEmployeeCode);
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			employee = employees.get(0);
			
			resultMap.put("category08", category08);
			resultMap.put("creationDate", sdf.format(day));
			resultMap.put("createdBy", loginEmployeeCode);
			resultMap.put("department", employee.getEmployeeDepartment());
			resultMap.put("form", buSupplierMod);
			resultMap.put("multiList", multiList);
			resultMap.put("statusName",OrderStatus.getChineseWord(buSupplierMod.getStatus()));	
			BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode,buSupplierMod.getSupplierCode(),"");

			// Initial 帶出廠商名稱 英文名稱 負責人
    		if( StringUtils.hasText(buSupplierMod .getSupplierCode()) && StringUtils.hasText(buSupplierMod.getChineseName()) 
    				&& StringUtils.hasText(buSupplierMod.getEnglishName()) && StringUtils.hasText(buSupplierMod.getSuperintendent())){
    		if(null!=buSupplierMod)
    				resultMap.put("chineseName", buSupplierMod.getChineseName() );
    			resultMap.put("englishName", buSupplierMod.getEnglishName() );
    			resultMap.put("superintendent", buSupplierMod.getSuperintendent() );
    			resultMap.put("countryCode", buSupplierMod.getCountryCode() );
    		}
			
			return resultMap;

		} catch (Exception ex) {
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}
	

	/**
	 * 依formId取得實際地點主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuSupplierMod executeFindActual(Map parameterMap)
			throws FormException, Exception {

	//	Object formBindBean = parameterMap.get("vatBeanFormBind");
	//	Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		
	//	BuSupplierMod buSupplierMod=(BuSupplierMod)parameterMap.get("entityBean");
		BuSupplierMod buSupplierMod = null;
	
		try {			
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			String brandCode    = (String)PropertyUtils.getProperty(otherBean, "brandCode");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			BuBrand     buBrand     = buBrandService.findById( brandCode );
			buSupplierMod = null == formId ? this.executeNewSupplier(parameterMap,  buBrand) : this.findById(formId);
	
			parameterMap.put("entityBean", buSupplierMod);
			log.info("executefind:"+buSupplierMod.getHeadId());
			
			return buSupplierMod;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	
	/**
	 * 產生一筆 Busupplier
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	private BuSupplierMod executeNewSupplier(Map parameterMap, BuBrand buBrand) throws Exception {
		log.info("new222");
		BuSupplierMod form = new BuSupplierMod();
		Object otherBean     = parameterMap.get("vatBeanOther");
		String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
    
		form.setSupplierCode("");
		form.setBrandCode(brandCode);
		form.setEnable("Y");
		form.setSupplierTypeCode("");
		form.setStatus(OrderStatus.SAVE);
		return form;
	}
	/*
	private BuSupplier executeNew() throws Exception {
		log.info("new222");
		BuSupplier form = new BuSupplier();
		form.setId(null);
//		form.setBrandCode(null);
		form.setSupplierTypeCode(null);
		return form;
	}
	*/
	
	public BuSupplierMod findById(Long headId) throws Exception {
		try {log.info("find333");
			BuSupplierMod buSuppliers = (BuSupplierMod) buSupplierModDAO
					.findByPrimaryKey(BuSupplierMod.class, headId);
			//BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(buSuppliers.getBrandCode(),buSuppliers.getSupplierCode());
			//buSuppliers.setIdentityCode(buSWAV.getIdentityCode());
			log.info("find444" + buSuppliers);
					return buSuppliers;
					
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId +" id時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢headId錯誤，原因："
					+ ex.getMessage());
		}
	}
	
	 /**
	  * 依據primary key為查詢條件，取得資料檔
	  * 
	  * @param formId
	  * @return busupplier
	  * @throws Exception      
	  *///FindSupplierIdById PICKER回來的Id
	 public BuSupplier findBuSupplierById(BuSupplierId buSupplierId) throws Exception {
		 System.out.println("findBuSupplierById");
		 try {
			 BuSupplier buSupplier = (BuSupplier) buSupplierDAO.findByPrimaryKey(BuSupplier.class, buSupplierId);
			 return buSupplier;
		 } catch (Exception ex) {
			 log.error("依據主鍵查詢資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據主鍵查詢資料時發生錯誤，原因：" + ex.getMessage());
		 }
	 }
	
	
	public List<BuSupplierMod> find(BuSupplierMod buSupplierMod)
	throws IllegalAccessException, InvocationTargetException,
	IllegalArgumentException, SecurityException, NoSuchMethodException,
	ClassNotFoundException {
		log.info("BuSupplierMod.find");
		BuSupplierMod searchObj = new BuSupplierMod();
		BeanUtils.copyProperties(buSupplierMod, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getHeadId()) {
			temp.add(buSupplierModDAO.findByPrimaryKey(
					BuSupplierMod.class, buSupplierMod.getHeadId()));
		} else {
			temp = buSupplierModDAO.findByExample(searchObj);
		}
		return temp;
	}
	/**
	 * 驗證主檔
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	public void validateHead(Map parameterMap) throws Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : 0;
		
	//	String supplierCode = (String) PropertyUtils.getProperty(formBindBean, "supplierCode");

		log.info("formId = " + formId);
		log.info("validate");
		// 驗證名稱
		
	/*	if (!StringUtils.hasText(supplierCode)) {
			throw new ValidationErrorException("請輸入資料！");
		} else {log.info("elseif1111111111");
			if (formId.equals(0) ) {
				BuSupplierMod supplierPO = buSupplierModDAO
						.findBysupplierCode(supplierCode.trim()
								.toUpperCase()); log.info("22222222222222222222");				
				if (supplierPO != null) {
					throw new ValidationErrorException("名稱：" + supplierCode
							+ "已經存在，請勿重複建立！");
				}
			}
		}*/
	}
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateSupplierModBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub

		BuSupplierMod buSupplierMod = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			buSupplierMod = (BuSupplierMod) parameterMap.get("entityBean");
			//AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buSupplierMod);//此方法填入空值無法做更新，故不使用 MACO 2016.10.20
			copyNullJSONBeantoPojoBean(formBindBean, buSupplierMod);
			
			parameterMap.put("entityBean", buSupplierMod);
		} /*catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} */catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}
	
	public static void copyNullJSONBeantoPojoBean(Object jsonBean , Object pojoBean)throws Exception{

		try{
			List pojoDateFields = new ArrayList();
			Map pojoDescript = PropertyUtils.describe(pojoBean);
			JSONObject jsonObject = JSONObject.fromObject(jsonBean);
			for(Iterator iter = pojoDescript.keySet().iterator();iter.hasNext();){
				String keyName = (String)iter.next();
				Class typeClazz = PropertyUtils.getPropertyType(pojoBean, keyName);
				System.out.println(typeClazz);
				Object finalValue =null;
				String tmpData = (String)jsonObject.get(keyName);
				if("class java.util.Date".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					pojoDateFields.add(keyName);
					Date sTod =null;
				        String[] tmpArray = tmpData.split("/");
					int year = Integer.parseInt(tmpArray[0]);
					int month = Integer.parseInt(tmpArray[1])-1;
					int day = Integer.parseInt(tmpArray[2]);
					Calendar c = Calendar.getInstance();
					c.set(year, month , day , 0 , 0 , 0);
					sTod = c.getTime();
					finalValue = sTod;
				}else if("class java.lang.Integer".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					finalValue = new Integer(tmpData);
				}else if("class java.lang.Long".equals(typeClazz.toString())&& StringUtils.hasText(tmpData)){
					finalValue = new Long(tmpData);
				}else if("class java.lang.Double".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					String replaceS = tmpData.replaceAll(",", "");
					finalValue = new Double(replaceS);
				}else if("class java.lang.String".equals(typeClazz.toString()) && (" ".equals(tmpData) || "".equals(tmpData))){
					finalValue = "";//若是空值塞空字串
				}else if("class java.lang.String".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					finalValue = tmpData;
				}else{
					continue;
				}
				PropertyUtils.setProperty(pojoBean,keyName,finalValue);
			}

		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	//process
	/** 檢核 HeadId 是否有值 */
	public Long getSupplierModHeadId(Object bean) throws FormException, Exception {
		log.info("getmodheadid");
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的主鍵為空值！");
		}
		return headId;
	}
	
	
	/**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuSupplierMod(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();

		try {

			 Object formBindBean = parameterMap.get("vatBeanFormBind");
		//	 Object formLinkBean = parameterMap.get("vatBeanFormLink");
			 Object otherBean = parameterMap.get("vatBeanOther");

			 String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			 String identityCode = (String) PropertyUtils.getProperty(formBindBean, "identityCode");
			 String supplierTypeCode = (String) PropertyUtils.getProperty(formBindBean, "supplierTypeCode");
			 String enable = (String) PropertyUtils.getProperty(formBindBean, "enable");
			 String type = (String) PropertyUtils.getProperty(formBindBean, "type");

			 
			BuSupplierMod buSupplierMod = (BuSupplierMod) parameterMap.get("entityBean");

			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");

			//-----------------------------------------

			Long processId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean, "processId"));
			String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");

			String beforeStatus = buSupplierMod.getStatus();
			String nextStatus =  getStatus(formAction,buSupplierMod.getStatus(),loginEmployeeCode);
			log.info("XXXXXXXXXXXXXXXXXXXXXX單據狀態XXXXXXXXXXXXXXXXXXXXXX");
			log.info("formStatus="+formStatus+"beforeStatus="+beforeStatus+"nextStatus="+nextStatus);
			if(formStatus.equals(OrderStatus.REJECT))
				nextStatus = OrderStatus.REJECT;
			// OrderStatus.SIGNING.equals(beforeStatus) && 
			if(OrderStatus.FINISH.equals(nextStatus)){
				//回寫
				log.info("回寫----updateSupplier");
				String supplierCode = buSupplierMod.getSupplierCode();
				String brandCode = buSupplierMod.getBrandCode();

				BuSupplierId buSupplierId = new BuSupplierId(supplierCode,brandCode);
				log.info(buSupplierId.getBrandCode()+"   "+buSupplierId.getSupplierCode());
				//取得BuSupplier的pk 
				BuSupplier buSupplier = buSupplierDAO.findById(buSupplierId);
				BuAddressBook buAddress;
				log.info("SupplierCode="+ supplierCode + "brandCode="+brandCode);
				if(buSupplier==null)
				{
					log.info("NEW ADDRESSBOOK");
					buAddress  = new BuAddressBook();
					buAddress.setOrganizationCode("TM");
					buAddress.setType(type);
					buAddress.setIdentityCode(identityCode);
					buAddressBookDAO.save(buAddress);
					log.info("NEW SUPPLIER");
					buSupplier = new BuSupplier();
					buSupplier.setId(buSupplierId);
					buSupplier.setSupplierTypeCode(supplierTypeCode);
					buSupplier.setAddressBookId(buAddress.getAddressBookId());
					buSupplier.setEnable(enable);
					buSupplier.setCreatedBy(loginEmployeeCode);
					buSupplier.setCreationDate(date);
					buSupplierDAO.save(buSupplier);
					buSupplierMod.setEnable("Y");
					buSupplierMod.setAddressBookId(buAddress.getAddressBookId());
				}
				else
				{
					log.info("OLD SUPPLIER");
					Long addressBookId = buSupplierMod.getAddressBookId();
					buAddress = buAddressBookDAO.findByAddressBookId(addressBookId);
				}
				buSupplier.setSupplierTypeCode(buSupplierMod.getSupplierTypeCode());
				buSupplier.setCategoryCode(buSupplierMod.getCategoryCode());
				buSupplier.setInvoiceTypeCode(buSupplierMod.getInvoiceTypeCode());
				buSupplier.setTaxType(buSupplierMod.getTaxType());
				buSupplier.setTaxRate(buSupplierMod.getTaxRate());
				buSupplier.setCurrencyCode(buSupplierMod.getCurrencyCode());
				buSupplier.setInvoiceDeliveryCode(buSupplierMod.getInvoiceDeliveryCode());
				buSupplier.setPaymentTermCode(buSupplierMod.getPaymentTermCode());
				buSupplier.setBillStyleCode(buSupplierMod.getBillStyleCode());
				buSupplier.setGrade(buSupplierMod.getGrade());
				buSupplier.setCustomsBroker(buSupplierMod.getCustomsBroker());
				buSupplier.setAgent(buSupplierMod.getAgent());
				buSupplier.setCommissionRate(buSupplierMod.getCommissionRate());
				buSupplier.setPriceTermCode(buSupplierMod.getPriceTermCode());
				buSupplier.setBankName(buSupplierMod.getBankName());
				buSupplier.setBranchCode(buSupplierMod.getBranchCode());
				buSupplier.setAccountCode(buSupplierMod.getAccountCode());
				buSupplier.setAccepter(buSupplierMod.getAccepter());
				buSupplier.setContactPerson1(buSupplierMod.getContactPerson1());
				buSupplier.setContactPerson2(buSupplierMod.getContactPerson2());
				buSupplier.setContactPerson3(buSupplierMod.getContactPerson3());
				buSupplier.setContactPerson4(buSupplierMod.getContactPerson4());
				buSupplier.setCity1(buSupplierMod.getCity1());
				buSupplier.setCity2(buSupplierMod.getCity2());
				buSupplier.setCity3(buSupplierMod.getCity3());
				buSupplier.setCity4(buSupplierMod.getCity4());
				buSupplier.setZipCode1(buSupplierMod.getZipCode1());
				buSupplier.setZipCode2(buSupplierMod.getZipCode2());
				buSupplier.setZipCode3(buSupplierMod.getZipCode3());
				buSupplier.setZipCode4(buSupplierMod.getZipCode4());
				buSupplier.setArea1(buSupplierMod.getArea1());
				buSupplier.setArea2(buSupplierMod.getArea2());
				buSupplier.setArea3(buSupplierMod.getArea3());
				buSupplier.setArea4(buSupplierMod.getArea4());
				buSupplier.setTel1(buSupplierMod.getTel1());
				buSupplier.setTel2(buSupplierMod.getTel2());
				buSupplier.setTel3(buSupplierMod.getTel3());
				buSupplier.setTel4(buSupplierMod.getTel4());
				buSupplier.setFax1(buSupplierMod.getFax1());
				buSupplier.setFax2(buSupplierMod.getFax2());
				buSupplier.setFax3(buSupplierMod.getFax3());
				buSupplier.setFax4(buSupplierMod.getFax4());
				buSupplier.setAddress1(buSupplierMod.getAddress1());
				buSupplier.setAddress2(buSupplierMod.getAddress2());
				buSupplier.setAddress3(buSupplierMod.getAddress3());
				buSupplier.setAddress4(buSupplierMod.getAddress4());
				buSupplier.setEMail1(buSupplierMod.getEMail1());
				buSupplier.setEMail2(buSupplierMod.getEMail2());
				buSupplier.setEMail3(buSupplierMod.getEMail3());
				buSupplier.setEMail4(buSupplierMod.getEMail4());
				buSupplier.setRemark1(buSupplierMod.getRemark1());
				buSupplier.setRemark2(buSupplierMod.getRemark2());
				buSupplier.setRemark3(buSupplierMod.getRemark3());
				buSupplier.setLastUpdatedBy(loginEmployeeCode);
				buSupplier.setLastUpdateDate(new Date());
				buSupplier.setSuperintendent(buSupplierMod.getSuperintendent());
				buSupplier.setCategoryType(buSupplierMod.getCategoryType());
				buSupplier.setCategory01(buSupplierMod.getCategory01());
				buSupplier.setCategory02(buSupplierMod.getCategory02());
				buSupplier.setCategory03(buSupplierMod.getCategory03());
				buSupplier.setCategory04(buSupplierMod.getCategory04());
				buSupplier.setCategory05(buSupplierMod.getCategory05());
				buSupplier.setCategory06(buSupplierMod.getCategory06());
				buSupplier.setCategory07(buSupplierMod.getCategory07());
				buSupplier.setCategory08(buSupplierMod.getCategory08());
				buSupplier.setCategory09(buSupplierMod.getCategory09());
				buSupplier.setCategory10(buSupplierMod.getCategory10());
				buSupplier.setCategory11(buSupplierMod.getCategory11());
				buSupplier.setCategory12(buSupplierMod.getCategory12());
				buSupplier.setCategory13(buSupplierMod.getCategory13());
				buSupplier.setCategory14(buSupplierMod.getCategory14());
				buSupplier.setCategory15(buSupplierMod.getCategory15());
				buSupplier.setCategory16(buSupplierMod.getCategory16());
				buSupplier.setCategory17(buSupplierMod.getCategory17());
				buSupplier.setCategory18(buSupplierMod.getCategory18());
				buSupplier.setCategory19(buSupplierMod.getCategory19());
				buSupplier.setCategory20(buSupplierMod.getCategory20());
				buSupplier.setOther(buSupplierMod.getOther());
				buSupplier.setAcceptEMail(buSupplierMod.getAcceptEMail());
				buSupplier.setConsign(buSupplierMod.getConsign());
				buSupplier.setEnable(buSupplierMod.getEnable());
				buSupplierDAO.update(buSupplier);

				log.info("====update====");
				buAddress.setType(buSupplierMod.getType());
				buAddress.setCity(buSupplierMod.getCity());
				buAddress.setArea(buSupplierMod.getArea());
				buAddress.setChineseName(buSupplierMod.getChineseName());
				buAddress.setCompanyName(buSupplierMod.getCompanyName());
				buAddress.setEnglishName(buSupplierMod.getEnglishName());
				buAddress.setShortName(buSupplierMod.getShortName());
				buAddress.setCountryCode(buSupplierMod.getCountryCode());
				buAddress.setTel1(buSupplierMod.getTelDay());
				buAddress.setTel2(buSupplierMod.getTelNight());
				buAddress.setFax1(buSupplierMod.getCompanyFax1());
				buAddress.setFax2(buSupplierMod.getCompanyFax2());
				buAddress.setEMail(buSupplierMod.getCompanyEmail());
				buAddress.setZipCode(buSupplierMod.getZipCode());
				buAddress.setAddress(buSupplierMod.getCompanyAddress());
				buAddress.setContractPerson(buSupplierMod.getRemark3());
				buAddress.setIdentityCode(buSupplierMod.getIdentityCode());
				buAddressBookDAO.update(buAddress);
				log.info("====updateAdressBook=====");
			}
			
			buSupplierMod.setStatus(nextStatus);
			buSupplierMod.setLastUpdatedBy(loginEmployeeCode);
			buSupplierMod.setLastUpdateDate(new Date());
			
			//save or update
			if(buSupplierMod.getHeadId() == null){			
				buSupplierMod.setCreatedBy(loginEmployeeCode);
				buSupplierMod.setCreationDate(date);
				buSupplierModDAO.save(buSupplierMod);
			}else {
				buSupplierModDAO.update(buSupplierMod);
			}
					
			resultMsg = "供應商:["+ buSupplierMod.getChineseName() + "]存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buSupplierMod);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	//get下一個狀態
	private String getStatus(String formAction,String beforeStatus,String loginEmployeeCode) {
		
		log.info("取得下個狀態");
		// TODO Auto-generated method stub
	//	BuSupplierMod buSupplierMod = new BuSupplierMod();
		//Boolean isMenagerEnd = true;
		String status = null;

		BuEmployee bump = buEmployeeDAO.findById(loginEmployeeCode);
		//log.info("bump.getIsDepartmentManager().equals()~~~~~"+bump.getIsDepartmentManager().equals("Y"));
		//if(bump.getIsDepartmentManager()==null)
		//	isMenagerEnd=false;

		log.info("登入工號:"+loginEmployeeCode);
		log.info("是否為主管:"+bump.getIsDepartmentManager());
		log.info("送出前狀態:"+beforeStatus);
		//若動作為送出
		if(OrderStatus.FORM_SUBMIT.equals(formAction)){
			//若送出前狀態為暫存或駁回
			if(beforeStatus.equals(OrderStatus.SAVE)||beforeStatus.equals(OrderStatus.REJECT)){
				status = OrderStatus.SIGNING;
			}
			//若送出前狀態為簽核中且不為部門主管
			else if(beforeStatus.equals(OrderStatus.SIGNING)&& bump.getIsDepartmentManager()==null){
				status = OrderStatus.SIGNING;
			}
			//其他則為簽核完成
			else 
			{
				status = OrderStatus.FINISH;
			}
		}
		//動作為暫存
		else if(OrderStatus.FORM_SAVE.equals(formAction)){
			status = OrderStatus.SAVE;
		}
		//動作為作廢
		else if(OrderStatus.FORM_VOID.equals(formAction)){
			status = OrderStatus.VOID;
		}
		log.info("送出後狀態:"+status);
		return status;
	}
	//get下一個狀態
	private String getRStatus(String formAction,String beforeStatus) {
		// TODO Auto-generated method stub
	//	BuSupplierMod buSupplierMod = new BuSupplierMod();
		String status = null;
		//送出 存 狀態
		if(OrderStatus.FORM_SUBMIT.equals(formAction)){
			if(beforeStatus.equals(OrderStatus.SIGNING)){
				status = OrderStatus.SIGNING;
			}
		}
		return status;
	}
	/**
	 * ajax 取得查詢的結果
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			log.info("search1111111111111111");
			String supplierCode = httpRequest.getProperty("supplierCode");
			String supplierTypeCode = httpRequest.getProperty("supplierTypeCode");
			String categoryCode =httpRequest.getProperty("categoryCode");
			String headId = httpRequest.getProperty("headId");
			log.info("supplierCode:" + supplierCode);
			log.info("headId:" + headId);
			log.info("categoryCode:" + categoryCode);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.supplierCode like :supplierCode", "%"
					+ supplierCode + "%");
			findObjs.put(" and model.headId like :headId", "%"
					+ headId + "%");
			/*findObjs.put(" and model.categoryCode like :categoryCode", "%"
					+ categoryCode + "%");*/
			// ==============================================================
			//查最終TABLE
			Map buSupplierMap = buSupplierModDAO.search(" BuSupplierMod as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
			
			List<BuSupplierMod> buSuppliers = (List<BuSupplierMod>) buSupplierMap
					.get(BaseDAO.TABLE_LIST);
		
			
		
			log.info("BuSupplier.size" + buSuppliers.size());
			if (buSuppliers != null && buSuppliers.size() > 0) {
				log.info("searchhhhhhhhhhhhhh");
				// 設定額外欄位
				this.setLineOtherColumn(buSuppliers);
				
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) buSupplierModDAO.search("BuSupplierMod as model","count(model.headId) as rowCount", findObjs,
						"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); 
																												// 取得最後一筆 INDEX
				log.info("searchhhhhhh22222222222222222222");
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_SUPPLIERMOD_FIELD_NAMES,
						GRID_SEARCH_SUPPLIERMOD_FIELD_DEFAULT_VALUES, buSuppliers,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_SUPPLIERMOD_FIELD_NAMES,
						GRID_SEARCH_SUPPLIERMOD_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
			log.info("searchhhhhhhhhhhhh333333333333332");
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的功能查詢失敗！");
		}
	}
	public List<Properties> getAJAXSupplierPageData(Properties httpRequest)
	throws Exception {

try {
	List<Properties> result = new ArrayList();
	List<Properties> gridDatas = new ArrayList();
	int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	// ======================帶入Head的值=========================
	log.info("search1111111111111111");
	String supplierCode = httpRequest.getProperty("supplierCode");
	String identityCode = httpRequest.getProperty("identityCode");
	String brandCode = httpRequest.getProperty("brandCode");
	String enable = httpRequest.getProperty("enable");

	log.info("supplierCode:" + supplierCode);
	log.info("identityCode:" + identityCode);
	log.info("brandCode:" + brandCode);
	log.info("enable:" + enable);


	HashMap map = new HashMap();
	HashMap findObjs = new HashMap();
	findObjs.put("supplierCode", supplierCode);
	findObjs.put("identityCode", identityCode);
	findObjs.put("brandCode", brandCode);
	findObjs.put("enable", enable);
	
	
	List<BuSupplierWithAddressView> buSuppliers = buSupplierWithAddressViewDAO.findSupplierPageData(findObjs,iSPage,iPSize);

	

	log.info("BuSupplier.size" + buSuppliers.size());
	if (buSuppliers != null && buSuppliers.size() > 0) {

		// 設定額外欄位

		
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex =  buSupplierWithAddressViewDAO.findSupplierAll(findObjs).size()+0L;
																										// 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_SUPPLIER_FIELD_NAMES,GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES, buSuppliers,gridDatas, firstIndex, maxIndex));
	} else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_SUPPLIER_FIELD_NAMES,GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES, map, gridDatas));
	}

	return result;
} catch (Exception ex) {
	log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
	throw new Exception("載入頁面顯示的功能查詢失敗！");
}
}
	
	
	/**
	 * 設定額外欄位
	 * 
	 * @param busuppliers
	 */
	private void setLineOtherColumn(List<BuSupplierMod> buSuppliers) {
		for (BuSupplierMod buSupplierMod : buSuppliers) {
			buSupplierMod.setLastUpdatedBy(UserUtils.getUsernameByEmployeeCode(buSupplierMod.getLastUpdatedBy()));
		}
	}

	/**
	 * 將主檔查詢結果存檔
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_SUPPLIERMOD_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	/**
	 * ajax picker按檢視返回選取的資料
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List<Properties> getSearchSelection(Map parameterMap)
			throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);
			System.out.println("searchKeysssss:" + searchKeys.get(0));
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			if (result.size() > 0)
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
	 * 流程起始
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */ 
	public static Object[] startProcess(BuSupplierMod form) throws Exception {
		log.info("startProcess");
		try {
			String packageId = "Bu_SupplierMod";
			String processId = "process";
			
			// String version = "T2".equals(form.getBrandCode())?"3":"2";
			// String sourceReferenceType =
			// "T2".equals(form.getBrandCode())?"ImMovement (3)":"ImMovement
			// (2)";;
		//	String version = "20140411";
		//	String sourceReferenceType = "SupplierMod (1)";
			String version;
			String sourceReferenceType;
			
			if("new".equals(form.getCategory08()))
			{
				version = "20160705new";
				sourceReferenceType = "SupplierMod (1)";
			}
			else if("create".equals(form.getCategory08()))
			{
				version = "20131202";
				sourceReferenceType = "SupplierMod (1)";
			}
			else
			{
				version = "20131202";
				sourceReferenceType = "SupplierMod (1)";
			}
			log.info("Category08"+form.getCategory08()+"version"+version+"sourceReferenceType"+sourceReferenceType);
			HashMap context = new HashMap();
			 context.put("brandCode", form.getBrandCode());
		     context.put("formId",    form.getHeadId());
		
		//	context.put("isThreePointMoving", false);
		//	Object[] object = ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);

			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}

	}
	//process
	public String getProcessSubject(BuSupplierMod buSupplierMod){
		String subject = new String("");
		subject = MessageStatus.getJobManagerMsg(buSupplierMod.getBrandCode(), buSupplierMod.getSupplierCode(), buSupplierMod.getSupplierTypeCode());
//		subject = subject+ (StringUtils.hasText(buSupplierMod.getSupplierCode())? buSupplierMod.getSupplierCode():"");
//		subject = subject+ (StringUtils.hasText(buSupplierMod.getBrandCode())? buSupplierMod.getBrandCode():"");
		return subject;
	}
	//process 簽核
	public static Object[] completeAssignment(long assignmentId, boolean approveResult,BuSupplierMod buSupplierMod) throws Exception {
		try {

			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", buSupplierMod);
			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new Exception(e);
		}

	}
	 public Long getSupplierHeadId(Object bean) throws FormException, Exception{
			Long headId = null;
			String id = (String)PropertyUtils.getProperty(bean, "headId");
		        System.out.println("headId=" + id);
			if(StringUtils.hasText(id)){
		            headId = NumberUtils.getLong(id);
		        }else{
		            throw new ValidationErrorException("傳入的主鍵為空值！");
		        }
			return headId;
		    }
		    
	 private BuSupplierMod getActualBuSupplier(Long headId) throws FormException, Exception{
	        log.info("getActualBuSupplier");
	        BuSupplierMod buSupplier = null;
	        buSupplier = findById(headId);
		if(buSupplier  == null){
		     throw new NoSuchObjectException("查無主鍵：" + headId + "的資料！");
		}else{
		    
		}
	        return buSupplier;
	    }
	 private BuSupplierMod getActualBuSupplierMod(Object bean) throws FormException, Exception {
			log.info("5.1 getActualBuSupplierMod");
			BuSupplierMod buSupplierMod = null;
			String id = (String) PropertyUtils.getProperty(bean, "headId");
			log.info("getActualBuSupplierMod headId=" + id);

			if (StringUtils.hasText(id)) {
				Long headId = NumberUtils.getLong(id);
				buSupplierMod = findById(headId);
			} else {
				throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
			}

			return buSupplierMod;
		}
	 //12/20--------填SUPPLIER代號帶出資料 
	 /** 處理AJAX 供應商及相關資料 / 利用供應商代號設定 countryCode,currencyCode,exchangeRate
	     * @param httpRequest
	     * @return
	     */
	    public List<Properties> getAJAXFormDataBySupplier(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();
		Long   addressBookId    = NumberUtils.getLong(httpRequest.getProperty("addressBookId"));
		String supplierCode     = httpRequest.getProperty("supplierCode");
		String brandCode        = httpRequest.getProperty("brandCode");
		log.info("addressBookId:"+addressBookId);
		log.info("supplierCode:"+supplierCode);
		log.info("brandCode:"+brandCode);
		
		if( !StringUtils.hasText(supplierCode)) {
			log.info("supplierCode空的");
		    BuSupplierWithAddressView buSWAVaddressBookId = 
			buSupplierWithAddressViewService.findSupplierByAddressBookIdAndBrandCode(addressBookId, brandCode );
		    supplierCode = buSWAVaddressBookId.getSupplierCode();
		}
		//log.info("getFormDataBySupplierCode supplierCode=" + supplierCode + ",organizationCode=" + organizationCode
					//+ ",brandCode=" + brandCode + ",orderDate=" + orderDate);
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(brandCode, supplierCode,"");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		BuSupplierId buSupplierId = new BuSupplierId(supplierCode,brandCode);
		BuSupplier buSupplier = buSupplierDAO.findById(buSupplierId);
		BuAddressBook buAddressBook = buAddressBookDAO.findByAddressBookId(buSupplier.getAddressBookId());
		log.info(buSupplier.getAddressBookId()+"  /  "+buAddressBook.getZipCode()+"  /  "+buAddressBook.getCity()+"  /  "+buAddressBook.getArea());
		//supplierCode = buSupplierId.getSupplierCode();
		//pro.setProperty("BrandCode", 	   AjaxUtils.getPropertiesValue(buSupplierId.getBrandCode(), brandCode));
		if (null != buSupplier ) {
			log.info("null != buSupplier");
			pro.setProperty("SupplierCode",    AjaxUtils.getPropertiesValue(buSupplierId.getSupplierCode(),  ""));
		    //pro.setProperty("BrandCode", 	   AjaxUtils.getPropertiesValue(buSupplierId.getBrandCode(), ""));
		    pro.setProperty("SupplierTypeCode",AjaxUtils.getPropertiesValue(buSupplier.getSupplierTypeCode(),""));
		    pro.setProperty("IdentityCode",    AjaxUtils.getPropertiesValue(buSWAV.getIdentityCode(),  ""));
		    pro.setProperty("CategoryCode",    AjaxUtils.getPropertiesValue(buSupplier.getCategoryCode(),    ""));
			pro.setProperty("CurrencyCode",    AjaxUtils.getPropertiesValue(buSupplier.getCurrencyCode(),    ""));
			pro.setProperty("PaymentTermCode", AjaxUtils.getPropertiesValue(buSupplier.getPaymentTermCode(), ""));
			pro.setProperty("PriceTermCode",   AjaxUtils.getPropertiesValue(buSupplier.getPriceTermCode(),   ""));
			pro.setProperty("TaxType",         AjaxUtils.getPropertiesValue(buSupplier.getTaxType(),         ""));
			pro.setProperty("InvoiceTypeCode", AjaxUtils.getPropertiesValue(buSupplier.getInvoiceTypeCode(), ""));
			pro.setProperty("Grade",   		   AjaxUtils.getPropertiesValue(buSupplier.getGrade(), ""));
			pro.setProperty("BillStyleCode",   AjaxUtils.getPropertiesValue(buSupplier.getBillStyleCode(), ""));
			pro.setProperty("Enable",   	   AjaxUtils.getPropertiesValue(buSupplier.getEnable(),  ""));
			pro.setProperty("InvoiceDeliveryCode",AjaxUtils.getPropertiesValue(buSupplier.getInvoiceDeliveryCode(), ""));
			pro.setProperty("CustomsBroker",   AjaxUtils.getPropertiesValue(buSupplier.getCustomsBroker(), ""));
			pro.setProperty("Agent",   		   AjaxUtils.getPropertiesValue(buSupplier.getAgent(), ""));	
			pro.setProperty("CommissionRate",  AjaxUtils.getPropertiesValue(buSupplier.getCommissionRate(), ""));			
		//	pro.setProperty("Superintendent",  AjaxUtils.getPropertiesValue(buSupplier.getSuperintendent(), ""));			
			pro.setProperty("TaxRate",         AjaxUtils.getPropertiesValue(getTaxRate(buSupplier.getTaxType()), "0"));
			pro.setProperty("CreatedBy",   	   AjaxUtils.getPropertiesValue(buSupplier.getCreatedBy(), ""));
			pro.setProperty("CreationDate",    AjaxUtils.getPropertiesValue(sdf.format(buSupplier.getCreationDate()), ""));			
			pro.setProperty("AddressBookId",   AjaxUtils.getPropertiesValue(buSupplier.getAddressBookId(), ""));	
			pro.setProperty("BankName",  	   AjaxUtils.getPropertiesValue(buSupplier.getBankName(), ""));	
			pro.setProperty("BranchCode",  	   AjaxUtils.getPropertiesValue(buSupplier.getBranchCode(), ""));				
			pro.setProperty("AccountCode",     AjaxUtils.getPropertiesValue(buSupplier.getAccountCode(), ""));				
			pro.setProperty("Accepter",        AjaxUtils.getPropertiesValue(buSupplier.getAccepter(), ""));				
			pro.setProperty("ContactPerson1",  AjaxUtils.getPropertiesValue(buSupplier.getContactPerson1(), ""));
			pro.setProperty("Area",  AjaxUtils.getPropertiesValue(buAddressBook.getArea(), ""));
			pro.setProperty("City",  AjaxUtils.getPropertiesValue(buAddressBook.getCity(), ""));
			pro.setProperty("ZipCode",  AjaxUtils.getPropertiesValue(buAddressBook.getZipCode(), ""));
			pro.setProperty("Tel1",  AjaxUtils.getPropertiesValue(buSupplier.getTel1(), ""));	
			pro.setProperty("Type",  AjaxUtils.getPropertiesValue(buAddressBook.getType(), ""));
			pro.setProperty("Category09",  AjaxUtils.getPropertiesValue(buSupplier.getCategory09(), ""));
			pro.setProperty("Category10",  AjaxUtils.getPropertiesValue(buSupplier.getCategory10(), ""));
			pro.setProperty("Fax1",  AjaxUtils.getPropertiesValue(buSupplier.getFax1(), ""));
			pro.setProperty("Area1",  AjaxUtils.getPropertiesValue(buSupplier.getArea1(), ""));
			pro.setProperty("ContactPerson2",  AjaxUtils.getPropertiesValue(buSupplier.getContactPerson2(), ""));				
			pro.setProperty("Tel2",  AjaxUtils.getPropertiesValue(buSupplier.getTel2(), ""));						
			pro.setProperty("Fax2",  AjaxUtils.getPropertiesValue(buSupplier.getFax2(), ""));
			pro.setProperty("Area2",  AjaxUtils.getPropertiesValue(buSupplier.getArea2(), ""));
			pro.setProperty("ContactPerson3",  AjaxUtils.getPropertiesValue(buSupplier.getContactPerson3(), ""));				
			pro.setProperty("Tel3",  AjaxUtils.getPropertiesValue(buSupplier.getTel3(), ""));						
			pro.setProperty("Fax3",  AjaxUtils.getPropertiesValue(buSupplier.getFax3(), ""));	
			pro.setProperty("Area3",  AjaxUtils.getPropertiesValue(buSupplier.getArea3(), ""));
			pro.setProperty("ContactPerson4",  AjaxUtils.getPropertiesValue(buSupplier.getContactPerson4(), ""));				
			pro.setProperty("Tel4",  AjaxUtils.getPropertiesValue(buSupplier.getTel4(), ""));						
			pro.setProperty("Fax4",  AjaxUtils.getPropertiesValue(buSupplier.getFax4(), ""));	
			pro.setProperty("Area4",  AjaxUtils.getPropertiesValue(buSupplier.getArea4(), ""));	
			pro.setProperty("Address1",  AjaxUtils.getPropertiesValue(buSupplier.getAddress1(), ""));	
			pro.setProperty("Address2",  AjaxUtils.getPropertiesValue(buSupplier.getAddress2(), ""));	
			pro.setProperty("Address3",  AjaxUtils.getPropertiesValue(buSupplier.getAddress3(), ""));	
			pro.setProperty("Address4",  AjaxUtils.getPropertiesValue(buSupplier.getAddress4(), ""));				
			pro.setProperty("EMail1",  AjaxUtils.getPropertiesValue(buSupplier.getEMail1(), ""));
			pro.setProperty("EMail2",  AjaxUtils.getPropertiesValue(buSupplier.getEMail2(), ""));
			pro.setProperty("EMail3",  AjaxUtils.getPropertiesValue(buSupplier.getEMail3(), ""));
			pro.setProperty("EMail4",  AjaxUtils.getPropertiesValue(buSupplier.getEMail4(), ""));
			pro.setProperty("Remark1",  AjaxUtils.getPropertiesValue(buSupplier.getRemark1(), ""));	
			pro.setProperty("Remark2",  AjaxUtils.getPropertiesValue(buSupplier.getRemark2(), ""));
			pro.setProperty("Remark3",  AjaxUtils.getPropertiesValue(buSWAV.getContractPerson(), ""));
			pro.setProperty("ZipCode1",  AjaxUtils.getPropertiesValue(buSupplier.getZipCode1(), ""));
			pro.setProperty("ZipCode2",  AjaxUtils.getPropertiesValue(buSupplier.getZipCode2(), ""));
			pro.setProperty("ZipCode3",  AjaxUtils.getPropertiesValue(buSupplier.getZipCode3(), ""));
			pro.setProperty("ZipCode4",  AjaxUtils.getPropertiesValue(buSupplier.getZipCode4(), ""));
			pro.setProperty("City1",  AjaxUtils.getPropertiesValue(buSupplier.getCity1(), ""));	
			pro.setProperty("City2",  AjaxUtils.getPropertiesValue(buSupplier.getCity2(), ""));
			pro.setProperty("City3",  AjaxUtils.getPropertiesValue(buSupplier.getCity3(), ""));
			pro.setProperty("City4",  AjaxUtils.getPropertiesValue(buSupplier.getCity4(), ""));
			pro.setProperty("ChineseName",  AjaxUtils.getPropertiesValue(buSWAV.getChineseName(), ""));
			pro.setProperty("EnglishName",  AjaxUtils.getPropertiesValue(buSWAV.getEnglishName(), ""));
			pro.setProperty("Superintendent",  AjaxUtils.getPropertiesValue(buSupplier.getSuperintendent(), ""));
			pro.setProperty("ShortName",  AjaxUtils.getPropertiesValue(buSWAV.getShortName(), ""));
			pro.setProperty("CountryCode",  AjaxUtils.getPropertiesValue(buSWAV.getCountryCode(), ""));
			pro.setProperty("CateGory01",  AjaxUtils.getPropertiesValue(buSupplier.getCategory01(), ""));
			pro.setProperty("CateGory02",  AjaxUtils.getPropertiesValue(buSupplier.getCategory02(), ""));
			pro.setProperty("CateGory03",  AjaxUtils.getPropertiesValue(buSupplier.getCategory03(), ""));
			pro.setProperty("CateGory04",  AjaxUtils.getPropertiesValue(buSupplier.getCategory04(), ""));
			pro.setProperty("CateGory05",  AjaxUtils.getPropertiesValue(buSupplier.getCategory05(), ""));
			pro.setProperty("CateGory06",  AjaxUtils.getPropertiesValue(buSupplier.getCategory06(), ""));
			pro.setProperty("CateGory07",  AjaxUtils.getPropertiesValue(buSupplier.getCategory07(), ""));
			pro.setProperty("CateGory08",  AjaxUtils.getPropertiesValue(buSupplier.getCategory08(), ""));
			pro.setProperty("CateGory09",  AjaxUtils.getPropertiesValue(buSupplier.getCategory09(), ""));
			pro.setProperty("CateGory10",  AjaxUtils.getPropertiesValue(buSupplier.getCategory10(), ""));
			pro.setProperty("CateGory11",  AjaxUtils.getPropertiesValue(buSupplier.getCategory11(), ""));
			pro.setProperty("CateGory12",  AjaxUtils.getPropertiesValue(buSupplier.getCategory12(), ""));
			pro.setProperty("CateGory13",  AjaxUtils.getPropertiesValue(buSupplier.getCategory13(), ""));
			pro.setProperty("CateGory14",  AjaxUtils.getPropertiesValue(buSupplier.getCategory14(), ""));
			pro.setProperty("CateGory15",  AjaxUtils.getPropertiesValue(buSupplier.getCategory15(), ""));
			pro.setProperty("CateGory16",  AjaxUtils.getPropertiesValue(buSupplier.getCategory16(), ""));
			pro.setProperty("CateGory17",  AjaxUtils.getPropertiesValue(buSupplier.getCategory17(), ""));
			pro.setProperty("CateGory18",  AjaxUtils.getPropertiesValue(buSupplier.getCategory18(), ""));
			pro.setProperty("CateGory19",  AjaxUtils.getPropertiesValue(buSupplier.getCategory19(), ""));
			pro.setProperty("CateGory20",  AjaxUtils.getPropertiesValue(buSupplier.getCategory20(), ""));
			pro.setProperty("Consign",  AjaxUtils.getPropertiesValue(buSupplier.getConsign(), ""));
			pro.setProperty("AcceptEMail",  AjaxUtils.getPropertiesValue(buSupplier.getAcceptEMail(), ""));
			pro.setProperty("Other",  AjaxUtils.getPropertiesValue(buSupplier.getOther(), ""));
			pro.setProperty("CategoryType",  AjaxUtils.getPropertiesValue(buSupplier.getCategoryType(), ""));	
			pro.setProperty("CompanyAddress",  AjaxUtils.getPropertiesValue(buSWAV.getAAddress(), ""));
			pro.setProperty("TelDay",  AjaxUtils.getPropertiesValue(buSWAV.getATel1(), ""));
			pro.setProperty("TelNight",  AjaxUtils.getPropertiesValue(buSWAV.getATel2(), ""));
			pro.setProperty("CompanyFax1",  AjaxUtils.getPropertiesValue(buSWAV.getAFax1(), ""));
			pro.setProperty("CompanyFax2",  AjaxUtils.getPropertiesValue(buSWAV.getAFax2(), ""));
			pro.setProperty("CompanyEmail",  AjaxUtils.getPropertiesValue(buSWAV.getAEMail(), ""));
			pro.setProperty("companyName",  AjaxUtils.getPropertiesValue(buSWAV.getCompanyName(), ""));
			
		} else {
			pro.setProperty("SupplierCode",    "");
			pro.setProperty("CategoryCode",    "");
			pro.setProperty("SupplierTypeCode","");
			pro.setProperty("CurrencyCode",    "");
			pro.setProperty("PaymentTermCode", "");
			pro.setProperty("PriceTermCode",   "");
			pro.setProperty("TaxType",         "");
			pro.setProperty("TaxRate",        "0");
			pro.setProperty("InvoiceTypeCode", "");
			pro.setProperty("Grade", "");
			pro.setProperty("BillStyleCode", "");
			pro.setProperty("InvoiceDeliveryCode", "");
			pro.setProperty("CustomsBroker", "");
			pro.setProperty("Agent", "");
			pro.setProperty("CommissionRate", "");
			pro.setProperty("Superintendent", "");
			
		}
		re.add(pro);
		return re;
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
	   
		 //---------
	    /**
		 * 更新PROCESS_ID，避免重複起流程 2010.07.07
		 *
		 * @param headId
		 * @param ProcessId
		 * @return
		 * @throws Exception
		 */
		public int updateProcessId(Long headId, Long ProcessId) throws Exception {
			int result = 0;
			try {
				result = buSupplierModDAO.updateProcessId(headId, ProcessId);
				System.out.println("更新資料筆數 ::: " + result + "筆");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("供應商流程更新PROCESS_ID錯誤，原因：" + e.getMessage());
				throw new Exception("調撥單更新PROCESS_ID錯誤！");
			}
			return result;
		}
	    public List<Properties> getInfoAsTop(Properties httpRequest)throws Exception {	
	    	log.info("==============CopyValueWithTop==============");
			Properties pro = new Properties();
			List re = new ArrayList();
			
			String copyType    			  = httpRequest.getProperty("copyType");
			String sorceContactPerson     = httpRequest.getProperty("sorceContactPerson");
			String sorceTel    			  = httpRequest.getProperty("sorceTel");
			String sorceFax  			  = httpRequest.getProperty("sorceFax");
			String sorceEMail  			  = httpRequest.getProperty("sorceEMail");
			String sorceCity   			  = httpRequest.getProperty("sorceCity");
			String sorceArea    		  = httpRequest.getProperty("sorceArea");
			String sorceZipCode  		  = httpRequest.getProperty("sorceZipCode");
			String sorceAddress   		  = httpRequest.getProperty("sorceAddress");


			pro.setProperty("copyType"			 ,    AjaxUtils.getPropertiesValue(copyType,  ""));
			pro.setProperty("sorceContactPerson" ,    AjaxUtils.getPropertiesValue(sorceContactPerson,  ""));
			pro.setProperty("sorceTel"			 ,    AjaxUtils.getPropertiesValue(sorceTel,  ""));
			pro.setProperty("sorceFax"			 ,    AjaxUtils.getPropertiesValue(sorceFax,  ""));
			pro.setProperty("sorceEMail"		 ,    AjaxUtils.getPropertiesValue(sorceEMail,  ""));
			pro.setProperty("sorceCity"				 ,    AjaxUtils.getPropertiesValue(sorceCity,  ""));
			pro.setProperty("sorceArea"				 ,    AjaxUtils.getPropertiesValue(sorceArea,  ""));
			pro.setProperty("sorceZipCode"			 ,    AjaxUtils.getPropertiesValue(sorceZipCode,  ""));
			pro.setProperty("sorceAddress"		 ,    AjaxUtils.getPropertiesValue(sorceAddress,  ""));


			re.add(pro);
			return re;
		    }
}
