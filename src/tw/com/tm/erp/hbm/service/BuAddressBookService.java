package tw.com.tm.erp.hbm.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.UniqueConstraintException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.FTPFubonExportData;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuCustomerCardEvent;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPaymentTermId;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.PosCustomer;
import tw.com.tm.erp.hbm.bean.PosEmployee;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerCardDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerCardEventDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuPaymentTermDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierDAO;
import tw.com.tm.erp.hbm.dao.BuSupplierWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.importdb.FTPFubonImportData;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class BuAddressBookService {

    private static final Log log = LogFactory
	    .getLog(BuAddressBookService.class);

    private BuAddressBookDAO buAddressBookDAO;

    private BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO;

    private BuCustomerDAO buCustomerDAO;

    private BuCustomerCardDAO buCustomerCardDAO;

    private BuCustomerCardEventDAO buCustomerCardEventDAO;

    private BuCurrencyDAO buCurrencyDAO;

    private BuEmployeeDAO buEmployeeDAO;

    private BuPaymentTermDAO buPaymentTermDAO;

    private BuSupplierDAO buSupplierDAO;

    private BuBrandDAO buBrandDAO;

    private BuCommonPhraseService buCommonPhraseService;

    private BaseDAO baseDAO;

    private PosExportDAO posExportDAO;

    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
    
    public void setBuCustomerCardDAO(BuCustomerCardDAO buCustomerCardDAO) {
	this.buCustomerCardDAO = buCustomerCardDAO;
    }

    public void setBuCustomerCardEventDAO(
	    BuCustomerCardEventDAO buCustomerCardEventDAO) {
	this.buCustomerCardEventDAO = buCustomerCardEventDAO;
    }

    /* Spring IoC */
    public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
	this.buAddressBookDAO = buAddressBookDAO;
    }

    public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
	this.buCustomerDAO = buCustomerDAO;
    }

    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
	this.buCurrencyDAO = buCurrencyDAO;
    }

    public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
	this.buEmployeeDAO = buEmployeeDAO;
    }

    public void setBuPaymentTermDAO(BuPaymentTermDAO buPaymentTermDAO) {
	this.buPaymentTermDAO = buPaymentTermDAO;
    }

    public void setBuSupplierDAO(BuSupplierDAO buSupplierDAO) {
	this.buSupplierDAO = buSupplierDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    public void setBuCommonPhraseService(
	    BuCommonPhraseService buCommonPhraseService) {
	this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuSupplierWithAddressViewDAO(
	    BuSupplierWithAddressViewDAO buSupplierWithAddressViewDAO) {
	this.buSupplierWithAddressViewDAO = buSupplierWithAddressViewDAO;
    }

    public void setPosExportDAO(PosExportDAO posExportDAO) {
	this.posExportDAO = posExportDAO;
    }

    public static final String[] GRID_SEARCH_FIELD_NAMES = { "type",
	    "identityCode", "chineseName", "supplierTypeCode", "supplierCode",
	    "categoryCode", "customsBroker", "lastUpdateDate", "addressBookId" };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
	    "", "", "", "", "", "", "0" };

    /**
     * 客戶資料查詢
     */
    public static final String[] CUSTOMER_FIELD_NAMES = { "type", "typeName",
	    "identityCode", "chineseName", "customerCode", "vipTypeCode",
	    "vipTypeName", "vipStartDate", "vipEndDate", "lastUpdateDate",
	    "addressBookId" };

    public static final String[] CUSTOMER_FIELD_DEFAULT_VALUES = { "", "", "",
	    "", "", "", "", "", "", "", "0" };

    /**
     * 供應商查詢
     */
    public static final String[] GRID_SEARCH_SUPPLIER_FIELD_NAMES = {
	    "typeName", "identityCode", "chineseName", "supplierTypeCodeName",
	    "supplierCode", "categoryCodeName", "customsBroker",
	    "lastUpdateDate", "addressBookId" };

    public static final String[] GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES = {
	    "", "", "", "", "", "", "", "", "0" };

    /**
     * 新增或更新整個通訊錄(BuAddressBook、BuCustomer、BuSupplier)
     * 
     * @param buAddressBook
     * @param buCustomer
     * @param buSupplier
     * @param buEmployee
     * @param loginUser
     * @return String
     * @throws Exception
     */
    public String saveOrUpdateBuAddressBook(BuAddressBook buAddressBook,
	    BuCustomer buCustomer, BuSupplier buSupplier,
	    BuEmployee buEmployee, String reportPassword, List employeeBrands,
	    String loginUser, String maintainType, HashMap permissionMap)
	    throws FormException, Exception {

	try {
	    String hiddenCustomer = (String) permissionMap
		    .get("hiddenCustomer");
	    String hiddenSupplier = (String) permissionMap
		    .get("hiddenSupplier");
	    String hiddenEmployee = (String) permissionMap
		    .get("hiddenEmployee");

	    doBuAddressBookValidate(buAddressBook, loginUser);
	    if (StringUtils.hasText(buCustomer.getId().getCustomerCode())
		    && ("customer".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenCustomer)) {
		doBuCustomerValidate(buAddressBook, buCustomer, loginUser);
	    }
	    if (StringUtils.hasText(buSupplier.getId().getSupplierCode())
		    && ("supplier".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenSupplier)) {
		doBuSupplierValidate(buAddressBook, buSupplier, loginUser);
	    }
	    if (StringUtils.hasText(buEmployee.getEmployeeCode())
		    && ("employee".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenEmployee)) {
		doBuEmployeeValidate(buEmployee, reportPassword,
			employeeBrands, loginUser);
	    }

	    Long addressBookId = insertOrUpdateBuAddressBook(buAddressBook);
	    if (StringUtils.hasText(buCustomer.getId().getCustomerCode())
		    && ("customer".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenCustomer)) {
		buCustomer.setEnable(("N".equals(buCustomer.getEnable()) ? "N"
			: "Y"));
		insertOrUpdateBuCustomer(buCustomer, addressBookId);
	    }
	    if (StringUtils.hasText(buSupplier.getId().getSupplierCode())
		    && ("supplier".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenSupplier)) {
		buSupplier.setEnable(("N".equals(buSupplier.getEnable()) ? "N"
			: "Y"));
		insertOrUpdateBuSupplier(buSupplier, addressBookId);
	    }
	    if (StringUtils.hasText(buEmployee.getEmployeeCode())
		    && ("employee".equals(maintainType) || "all"
			    .equals(maintainType))
		    && "N".equals(hiddenEmployee)) {
		insertOrUpdateBuEmployee(buEmployee, addressBookId);
	    }
	    return "存檔成功！";
	} catch (FormException fe) {
	    log.error("通訊錄存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("通訊錄存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("通訊錄存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增或更新BuAddressBook
     * 
     * @param buAddressBook
     * @return Long
     */
    private Long insertOrUpdateBuAddressBook(BuAddressBook buAddressBook) {

	if (buAddressBook.getAddressBookId() == null) {
	    buAddressBookDAO.save(buAddressBook);
	    return buAddressBook.getAddressBookId();
	} else {
	    buAddressBookDAO.update(buAddressBook);
	    return buAddressBook.getAddressBookId();
	}
    }

    /**
     * 新增或更新BuCustomer
     * 
     * @param buCustomer
     * @param addressBookId
     */
    private void insertOrUpdateBuCustomer(BuCustomer buCustomer,
	    Long addressBookId) {

	if (buCustomer.getAddressBookId() == null) {
	    buCustomer.setAddressBookId(addressBookId);
	    buCustomerDAO.save(buCustomer);
	} else {
	    buCustomerDAO.update(buCustomer);
	}
    }

    /**
     * 新增或更新BuSupplier
     * 
     * @param buSupplier
     * @param addressBookId
     */
    private void insertOrUpdateBuSupplier(BuSupplier buSupplier,
	    Long addressBookId) {

	if (buSupplier.getAddressBookId() == null) {
	    buSupplier.setAddressBookId(addressBookId);
	    buSupplierDAO.save(buSupplier);
	} else {
	    buSupplierDAO.update(buSupplier);
	}
    }

    /**
     * 新增或更新BuSupplier
     * 
     * @param buEmployee
     * @param addressBookId
     */
    private void insertOrUpdateBuEmployee(BuEmployee buEmployee,
	    Long addressBookId) {

	buEmployee
		.setRemark2(("Y".equals(buEmployee.getRemark2()) ? "Y" : "N"));
	if (buEmployee.getAddressBookId() == null) {
	    buEmployee.setAddressBookId(addressBookId);
	    buEmployeeDAO.save(buEmployee);
	} else {
	    buEmployeeDAO.update(buEmployee);
	}
    }

    /**
     * 檢核BuAddressBook
     * 
     * @param buAddressBook
     * @param loginUser
     */
    private void doBuAddressBookValidate(BuAddressBook buAddressBook,
	    String loginUser) throws ValidationErrorException,
	    UniqueConstraintException {

	String tabName = "基本資料頁籤";
	if (!StringUtils.hasText(buAddressBook.getIdentityCode())) {
	    throw new ValidationErrorException("請輸入身份證明代號！");
	}
	// 將UI輸入的IdentityCode前後去空白
	if (buAddressBook.getAddressBookId() == null) {
	    buAddressBook.setIdentityCode(buAddressBook.getIdentityCode()
		    .trim());
	}
	if (!ValidateUtil.isUpperCaseOrNumber(buAddressBook.getIdentityCode())) {
	    throw new ValidationErrorException("身份證明代號必須為A~Z或0~9！");
	} else if (buAddressBook.getAddressBookId() == null) {
	    List queryResult = buAddressBookDAO.findByProperty("BuAddressBook",
		    "identityCode", buAddressBook.getIdentityCode());
	    if (queryResult != null && queryResult.size() > 0)
		throw new UniqueConstraintException("身份證明代號："
			+ buAddressBook.getIdentityCode() + "重複！");
	}

	if (!StringUtils.hasText(buAddressBook.getChineseName())) {
	    throw new ValidationErrorException("請輸入姓名！");
	}

	// if(StringUtils.hasText(buAddressBook.getEnglishName()) &&
	// !ValidateUtil.isEnglishAlphabet(buAddressBook.getEnglishName())) {
	// throw new ValidationErrorException(tabName + "的英文名稱必須為A~Z或a~z！");
	// }
	// 暫時將檢核關閉 2008-9-30
	/*
	 * if(!StringUtils.hasText(buAddressBook.getCountryCode())) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的國別！"); }else{
	 * buAddressBook.setCountryCode(buAddressBook.getCountryCode().trim().toUpperCase());
	 * BuCountry country =
	 * buCountryDAO.findById(buAddressBook.getCountryCode()); if(country ==
	 * null) throw new ValidationErrorException(tabName + "的國別：" +
	 * buAddressBook.getCountryCode() + "不存在，請重新輸入！"); }
	 * 
	 * if(buAddressBook.getType().equals("1")){
	 * if(!StringUtils.hasText(buAddressBook.getGender())) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的性別！"); }
	 * if(buAddressBook.getBirthdayYear() == null ||
	 * buAddressBook.getBirthdayYear() == 0L ||
	 * buAddressBook.getBirthdayMonth() == null ||
	 * buAddressBook.getBirthdayMonth() == 0L ||
	 * buAddressBook.getBirthdayDay() == null ||
	 * buAddressBook.getBirthdayDay() == 0L) { throw new
	 * ValidationErrorException("請完整輸入" + tabName + "的生日！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getBirthdayYear().toString())){
	 * throw new ValidationErrorException(tabName + "的生日必須為0~9數字！"); }else
	 * if(!ValidateUtil.isAfterInitialYear(buAddressBook.getBirthdayYear())){
	 * throw new ValidationErrorException(tabName + "的生日年份需大於" +
	 * ValidateUtil.INITIAL_YEAR + "年！"); }
	 * 
	 * String message =
	 * ValidateUtil.validateDate(buAddressBook.getBirthdayYear(),
	 * buAddressBook.getBirthdayMonth(), buAddressBook.getBirthdayDay());
	 * if(!MessageStatus.SUCCESS.equals(message)){ throw new
	 * ValidationErrorException(tabName + "的生日輸入不正確，" + message + "！"); }
	 * }else if(buAddressBook.getType().equals("2")){
	 * 
	 * if((buAddressBook.getBirthdayYear() != null &&
	 * buAddressBook.getBirthdayYear() != 0L) ||
	 * (buAddressBook.getBirthdayMonth() != null &&
	 * buAddressBook.getBirthdayMonth() != 0L) ||
	 * (buAddressBook.getBirthdayDay() != null &&
	 * buAddressBook.getBirthdayDay() != 0L)){
	 * if(buAddressBook.getBirthdayYear() == null ||
	 * buAddressBook.getBirthdayYear() == 0L ||
	 * buAddressBook.getBirthdayMonth() == null ||
	 * buAddressBook.getBirthdayMonth() == 0L ||
	 * buAddressBook.getBirthdayDay() == null ||
	 * buAddressBook.getBirthdayDay() == 0L){ throw new
	 * ValidationErrorException("請完整輸入" + tabName + "的生日！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getBirthdayYear().toString())){
	 * throw new ValidationErrorException(tabName + "的生日必須為0~9數字！"); }else
	 * if(!ValidateUtil.isAfterInitialYear(buAddressBook.getBirthdayYear())){
	 * throw new ValidationErrorException(tabName + "的生日年份需大於" +
	 * ValidateUtil.INITIAL_YEAR + "年！"); } String message =
	 * ValidateUtil.validateDate(buAddressBook.getBirthdayYear(),
	 * buAddressBook.getBirthdayMonth(), buAddressBook.getBirthdayDay());
	 * if(!MessageStatus.SUCCESS.equals(message)){ throw new
	 * ValidationErrorException(tabName + "的生日輸入不正確，" + message + "！"); } }
	 * 
	 * if(buAddressBook.getBirthdayYear() != null &&
	 * buAddressBook.getBirthdayYear() == 0L){
	 * buAddressBook.setBirthdayYear(null); }
	 * if(buAddressBook.getBirthdayMonth() != null &&
	 * buAddressBook.getBirthdayMonth() == 0L){
	 * buAddressBook.setBirthdayMonth(null); }
	 * if(buAddressBook.getBirthdayDay() != null &&
	 * buAddressBook.getBirthdayDay() == 0L){
	 * buAddressBook.setBirthdayDay(null); }
	 * if(!StringUtils.hasText(buAddressBook.getCompanyName())) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的公司名稱！"); }
	 * if(!StringUtils.hasText(buAddressBook.getIndustryCode())) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的產業別！"); }
	 * if(buAddressBook.getCapitalization() == null) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的資本額！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getCapitalization().toString())){
	 * throw new ValidationErrorException(tabName + "的資本額必須為0~9數字！"); }
	 * 
	 * if(buAddressBook.getIncome() == null) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的營業額！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getIncome().toString())){
	 * throw new ValidationErrorException(tabName + "的營業額必須為0~9數字！"); }
	 * 
	 * if(buAddressBook.getEmployees() == null) { throw new
	 * ValidationErrorException("請輸入" + tabName + "的員工數！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getEmployees().toString())){
	 * throw new ValidationErrorException(tabName + "的員工數必須為0~9數字！"); }
	 *  }
	 * 
	 * if(!StringUtils.hasText(buAddressBook.getTel1()) &&
	 * !StringUtils.hasText(buAddressBook.getTel2()) &&
	 * !StringUtils.hasText(buAddressBook.getMobilePhone())) { throw new
	 * ValidationErrorException(tabName + "的電話(日)、電話(夜)、行動電話至少需輸入一組電話！");
	 * }else if(StringUtils.hasText(buAddressBook.getTel1()) &&
	 * !ValidateUtil.isNumber(buAddressBook.getTel1())){ throw new
	 * ValidationErrorException(tabName + "的電話(日)必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buAddressBook.getTel2()) &&
	 * !ValidateUtil.isNumber(buAddressBook.getTel2())){ throw new
	 * ValidationErrorException(tabName + "的電話(夜)必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buAddressBook.getMobilePhone()) &&
	 * !ValidateUtil.isNumber(buAddressBook.getMobilePhone())){ throw new
	 * ValidationErrorException(tabName + "的行動電話必須為0~9數字！"); }
	 * 
	 * if(StringUtils.hasText(buAddressBook.getFax1()) &&
	 * !ValidateUtil.isNumber(buAddressBook.getFax1())){ throw new
	 * ValidationErrorException(tabName + "的傳真一必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buAddressBook.getFax2()) &&
	 * !ValidateUtil.isNumber(buAddressBook.getFax2())){ throw new
	 * ValidationErrorException(tabName + "的傳真二必須為0~9數字！"); }
	 * 
	 * if(StringUtils.hasText(buAddressBook.getEMail()) &&
	 * !ValidateUtil.isEmailFormat(buAddressBook.getEMail())){ throw new
	 * ValidationErrorException(tabName + "的電子信箱格式不正確！"); }
	 * 
	 * if(!StringUtils.hasText(buAddressBook.getCity())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的城市！"); }else
	 * if(!StringUtils.hasText(buAddressBook.getArea())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的鄉鎮區別！"); }else
	 * if(!StringUtils.hasText(buAddressBook.getZipCode())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buAddressBook.getZipCode())){ throw new
	 * ValidationErrorException(tabName + "的郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buAddressBook.getAddress())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的地址！"); }
	 */

	UserUtils.setOpUserAndDate(buAddressBook, loginUser);
    }

    /**
     * 檢核BuCustomer
     * 
     * @param buAddressBook
     * @param buCustomer
     * @param loginUser
     */
    private void doBuCustomerValidate(BuAddressBook buAddressBook,
	    BuCustomer buCustomer, String loginUser)
	    throws ValidationErrorException, UniqueConstraintException {

	String tabName = "客戶資料頁籤";

	// 將UI輸入的CustomerCode前後去空白
	if (buCustomer.getAddressBookId() == null) {
	    buCustomer.getId().setCustomerCode(
		    buCustomer.getId().getCustomerCode().trim());
	}
	if (!ValidateUtil.isUpperCaseOrNumber(buCustomer.getId()
		.getCustomerCode())) {
	    throw new ValidationErrorException(tabName + "的客戶代號必須為A~Z或0~9！");
	} else if (buCustomer.getAddressBookId() == null) {
	    BuCustomerId id = new BuCustomerId();
	    id.setBrandCode(buCustomer.getId().getBrandCode());
	    id.setCustomerCode(buCustomer.getId().getCustomerCode());
	    BuCustomer customer = (BuCustomer) buCustomerDAO.findByPrimaryKey(
		    BuCustomer.class, id);
	    if (customer != null)
		throw new UniqueConstraintException(tabName + "的客戶代號："
			+ buCustomer.getId().getCustomerCode() + "重複！");
	}
	// 暫時將檢核關閉 2008-9-30
	/*
	 * if (!StringUtils.hasText(buCustomer.getTaxType())) { throw new
	 * ValidationErrorException("請選擇" + tabName + "的稅別！"); }else
	 * if(("1".equals(buCustomer.getTaxType()) ||
	 * "2".equals(buCustomer.getTaxType())) && (buCustomer.getTaxRate() !=
	 * null && buCustomer.getTaxRate() != 0D)){ throw new
	 * ValidationErrorException(tabName + "的稅別為免稅或零稅時，其稅率應為0%！"); }else
	 * if("3".equals(buCustomer.getTaxType()) && (buCustomer.getTaxRate() !=
	 * null && buCustomer.getTaxRate() <= 0D)){ throw new
	 * ValidationErrorException(tabName + "的稅別為應稅時，其稅率不可小於或等於0%！"); }
	 */

	if (!StringUtils.hasText(buCustomer.getCurrencyCode())) {
	    throw new ValidationErrorException("請輸入" + tabName + "的使用幣別！");
	} else {
	    buCustomer.setCurrencyCode(buCustomer.getCurrencyCode().trim()
		    .toUpperCase());
	    BuCurrency currency = buCurrencyDAO.findById(buCustomer
		    .getCurrencyCode());
	    if (currency == null)
		throw new ValidationErrorException(tabName + "的使用幣別："
			+ buCustomer.getCurrencyCode() + "不存在，請重新輸入！");
	}

	if (!StringUtils.hasText(buCustomer.getPaymentTermCode())) {
	    throw new ValidationErrorException("請輸入" + tabName + "的付款條件！");
	} else {
	    buCustomer.setPaymentTermCode(buCustomer.getPaymentTermCode()
		    .trim().toUpperCase());
	    BuPaymentTermId paymentTermId = new BuPaymentTermId();
	    paymentTermId.setOrganizationCode(buAddressBook
		    .getOrganizationCode());
	    paymentTermId.setPaymentTermCode(buCustomer.getPaymentTermCode());
	    BuPaymentTerm paymentTerm = buPaymentTermDAO
		    .findById(paymentTermId);
	    if (paymentTerm == null)
		throw new ValidationErrorException(tabName + "的付款條件："
			+ buCustomer.getPaymentTermCode() + "不存在，請重新輸入！");
	}

	if (!StringUtils.hasText(buCustomer.getVipTypeCode())) {
	    throw new ValidationErrorException("請選擇" + tabName + "的VIP類別！");
	}
	// 暫時將檢核關閉 2008-9-30
	/*
	 * if(buCustomer.getVipStartDate() == null){ throw new
	 * ValidationErrorException("請選擇" + tabName + "的會員啟始日！"); }else
	 * if(buCustomer.getVipEndDate() == null){ throw new
	 * ValidationErrorException("請選擇" + tabName + "的會員到期日！"); }else
	 * if(buCustomer.getApplicationDate() == null){ throw new
	 * ValidationErrorException("請選擇" + tabName + "的申請日期！"); }else
	 * if(buCustomer.getVipEndDate().before(buCustomer.getVipStartDate()) ){
	 * throw new ValidationErrorException(tabName + "的會員啟始日不可大於會員到期日！"); }
	 * 
	 * 
	 * if(StringUtils.hasText(buCustomer.getTel1()) &&
	 * !ValidateUtil.isNumber(buCustomer.getTel1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getFax1()) &&
	 * !ValidateUtil.isNumber(buCustomer.getFax1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getEMail1()) &&
	 * !ValidateUtil.isEmailFormat(buCustomer.getEMail1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buCustomer.getTel2()) &&
	 * !ValidateUtil.isNumber(buCustomer.getTel2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getFax2()) &&
	 * !ValidateUtil.isNumber(buCustomer.getFax2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getEMail2()) &&
	 * !ValidateUtil.isEmailFormat(buCustomer.getEMail2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buCustomer.getTel3()) &&
	 * !ValidateUtil.isNumber(buCustomer.getTel3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getFax3()) &&
	 * !ValidateUtil.isNumber(buCustomer.getFax3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getEMail3()) &&
	 * !ValidateUtil.isEmailFormat(buCustomer.getEMail3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buCustomer.getTel4()) &&
	 * !ValidateUtil.isNumber(buCustomer.getTel4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getFax4()) &&
	 * !ValidateUtil.isNumber(buCustomer.getFax4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buCustomer.getEMail4()) &&
	 * !ValidateUtil.isEmailFormat(buCustomer.getEMail4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口電子信箱格式不正確！"); }
	 * 
	 * if(!StringUtils.hasText(buCustomer.getCity1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址城市！"); }else
	 * if(!StringUtils.hasText(buCustomer.getArea1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址區域！"); }else
	 * if(!StringUtils.hasText(buCustomer.getZipCode1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buCustomer.getZipCode1())){ throw new
	 * ValidationErrorException(tabName + "的通訊地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buCustomer.getAddress1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址！"); }else
	 * if(!StringUtils.hasText(buCustomer.getCity2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址城市！"); }else
	 * if(!StringUtils.hasText(buCustomer.getArea2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址區域！"); }else
	 * if(!StringUtils.hasText(buCustomer.getZipCode2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buCustomer.getZipCode2())){ throw new
	 * ValidationErrorException(tabName + "的發票地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buCustomer.getAddress2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址！"); }else
	 * if(!StringUtils.hasText(buCustomer.getCity3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址城市！"); }else
	 * if(!StringUtils.hasText(buCustomer.getArea3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址區域！"); }else
	 * if(!StringUtils.hasText(buCustomer.getZipCode3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buCustomer.getZipCode3())){ throw new
	 * ValidationErrorException(tabName + "的送貨地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buCustomer.getAddress3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址！"); }
	 * 
	 * 
	 * if(StringUtils.hasText(buCustomer.getCity4()) ||
	 * StringUtils.hasText(buCustomer.getArea4()) ||
	 * StringUtils.hasText(buCustomer.getZipCode4()) ||
	 * StringUtils.hasText(buCustomer.getAddress4())){
	 * if(!StringUtils.hasText(buCustomer.getCity4()) ||
	 * !StringUtils.hasText(buCustomer.getArea4()) ||
	 * !StringUtils.hasText(buCustomer.getZipCode4()) ||
	 * !StringUtils.hasText(buCustomer.getAddress4())) throw new
	 * ValidationErrorException("請完整輸入" + tabName + "的其他地址！"); else
	 * if(!ValidateUtil.isNumber(buCustomer.getZipCode4())) throw new
	 * ValidationErrorException(tabName + "的其他地址郵遞區號必須為0~9數字！"); }
	 */

	UserUtils.setOpUserAndDate(buCustomer, loginUser);
    }

    /**
     * 檢核BuSupplier
     * 
     * @param buAddressBook
     * @param buSupplier
     * @param loginUser
     */
    private void doBuSupplierValidate(BuAddressBook buAddressBook,
	    BuSupplier buSupplier, String loginUser)
	    throws ValidationErrorException, UniqueConstraintException {

	String tabName = "供應商資料頁籤";

	// 將UI輸入的SupplierCode前後去空白
	if (buSupplier.getAddressBookId() == null) {
	    buSupplier.getId().setSupplierCode(
		    buSupplier.getId().getSupplierCode().trim());
	}
	if (!ValidateUtil.isUpperCaseOrNumber(buSupplier.getId()
		.getSupplierCode())) {
	    throw new ValidationErrorException(tabName + "的廠商代號必須為A~Z或0~9！");
	} else if (buSupplier.getAddressBookId() == null) {
	    BuSupplierId id = new BuSupplierId();
	    id.setBrandCode(buSupplier.getId().getBrandCode());
	    id.setSupplierCode(buSupplier.getId().getSupplierCode());
	    BuSupplier supplier = (BuSupplier) buSupplierDAO.findByPrimaryKey(
		    BuSupplier.class, id);
	    if (supplier != null)
		throw new UniqueConstraintException(tabName + "的廠商代號："
			+ buSupplier.getId().getSupplierCode() + "重複！");
	}

	if (!StringUtils.hasText(buSupplier.getSupplierTypeCode())) {
	    throw new ValidationErrorException("請選擇" + tabName + "的廠商類別！");
	}
	// 暫時將檢核關閉 2008-9-30
	/*
	 * if(!StringUtils.hasText(buSupplier.getSuperintendent())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的負責人！"); }
	 * 
	 * if (!StringUtils.hasText(buSupplier.getTaxType())) { throw new
	 * ValidationErrorException("請選擇" + tabName + "的稅別！"); }else
	 * if(("1".equals(buSupplier.getTaxType()) ||
	 * "2".equals(buSupplier.getTaxType())) && (buSupplier.getTaxRate() !=
	 * null && buSupplier.getTaxRate() != 0D)){ throw new
	 * ValidationErrorException(tabName + "的稅別為免稅或零稅時，其稅率應為0%！"); }else
	 * if("3".equals(buSupplier.getTaxType()) && (buSupplier.getTaxRate() !=
	 * null && buSupplier.getTaxRate() == 0D)){ throw new
	 * ValidationErrorException(tabName + "的稅別為應稅時，其稅率不可為0%！"); }
	 * 
	 * if(!StringUtils.hasText(buSupplier.getCurrencyCode())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的使用幣別！"); }else{
	 * buSupplier.setCurrencyCode(buSupplier.getCurrencyCode().trim().toUpperCase());
	 * BuCurrency currency =
	 * buCurrencyDAO.findById(buSupplier.getCurrencyCode()); if(currency ==
	 * null) throw new ValidationErrorException(tabName + "的使用幣別：" +
	 * buSupplier.getCurrencyCode() + "不存在，請重新輸入！"); }
	 * 
	 * if(!StringUtils.hasText(buSupplier.getPaymentTermCode())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的付款條件！"); }else{
	 * buSupplier.setPaymentTermCode(buSupplier.getPaymentTermCode().trim().toUpperCase());
	 * BuPaymentTermId paymentTermId = new BuPaymentTermId();
	 * paymentTermId.setOrganizationCode(buAddressBook.getOrganizationCode());
	 * paymentTermId.setPaymentTermCode(buSupplier.getPaymentTermCode());
	 * BuPaymentTerm paymentTerm = buPaymentTermDAO.findById(paymentTermId);
	 * if(paymentTerm == null) throw new ValidationErrorException(tabName +
	 * "的付款條件：" + buSupplier.getPaymentTermCode() + "不存在，請重新輸入！"); }
	 * 
	 * if(!StringUtils.hasText(buSupplier.getBankCode())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的收款銀行！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getBankCode())){ throw new
	 * ValidationErrorException(tabName + "的收款銀行必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAccountCode())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的帳號！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getAccountCode())){ throw new
	 * ValidationErrorException(tabName + "的帳號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAccepter())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的受款人！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAcceptTel())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的受款人電話！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getAcceptTel())){ throw new
	 * ValidationErrorException(tabName + "的受款人電話必須為0~9數字！"); }
	 * 
	 * if(StringUtils.hasText(buSupplier.getTel1()) &&
	 * !ValidateUtil.isNumber(buSupplier.getTel1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getFax1()) &&
	 * !ValidateUtil.isNumber(buSupplier.getFax1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getEMail1()) &&
	 * !ValidateUtil.isEmailFormat(buSupplier.getEMail1())){ throw new
	 * ValidationErrorException(tabName + "的業務窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buSupplier.getTel2()) &&
	 * !ValidateUtil.isNumber(buSupplier.getTel2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getFax2()) &&
	 * !ValidateUtil.isNumber(buSupplier.getFax2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getEMail2()) &&
	 * !ValidateUtil.isEmailFormat(buSupplier.getEMail2())){ throw new
	 * ValidationErrorException(tabName + "的財務窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buSupplier.getTel3()) &&
	 * !ValidateUtil.isNumber(buSupplier.getTel3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getFax3()) &&
	 * !ValidateUtil.isNumber(buSupplier.getFax3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getEMail3()) &&
	 * !ValidateUtil.isEmailFormat(buSupplier.getEMail3())){ throw new
	 * ValidationErrorException(tabName + "的倉庫窗口電子信箱格式不正確！"); }else
	 * if(StringUtils.hasText(buSupplier.getTel4()) &&
	 * !ValidateUtil.isNumber(buSupplier.getTel4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口電話必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getFax4()) &&
	 * !ValidateUtil.isNumber(buSupplier.getFax4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口傳真必須為0~9數字！"); }else
	 * if(StringUtils.hasText(buSupplier.getEMail4()) &&
	 * !ValidateUtil.isEmailFormat(buSupplier.getEMail4())){ throw new
	 * ValidationErrorException(tabName + "的其他窗口電子信箱格式不正確！"); }
	 * 
	 * if(!StringUtils.hasText(buSupplier.getCity1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址城市！"); }else
	 * if(!StringUtils.hasText(buSupplier.getArea1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址區域！"); }else
	 * if(!StringUtils.hasText(buSupplier.getZipCode1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getZipCode1())){ throw new
	 * ValidationErrorException(tabName + "的通訊地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAddress1())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的通訊地址！"); }else
	 * if(!StringUtils.hasText(buSupplier.getCity2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址城市！"); }else
	 * if(!StringUtils.hasText(buSupplier.getArea2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址區域！"); }else
	 * if(!StringUtils.hasText(buSupplier.getZipCode2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getZipCode2())){ throw new
	 * ValidationErrorException(tabName + "的發票地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAddress2())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的發票地址！"); }else
	 * if(!StringUtils.hasText(buSupplier.getCity3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址城市！"); }else
	 * if(!StringUtils.hasText(buSupplier.getArea3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址區域！"); }else
	 * if(!StringUtils.hasText(buSupplier.getZipCode3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址郵遞區號！"); }else
	 * if(!ValidateUtil.isNumber(buSupplier.getZipCode3())){ throw new
	 * ValidationErrorException(tabName + "的送貨地址郵遞區號必須為0~9數字！"); }else
	 * if(!StringUtils.hasText(buSupplier.getAddress3())){ throw new
	 * ValidationErrorException("請輸入" + tabName + "的送貨地址！"); }
	 * 
	 * 
	 * if(StringUtils.hasText(buSupplier.getCity4()) ||
	 * StringUtils.hasText(buSupplier.getArea4()) ||
	 * StringUtils.hasText(buSupplier.getZipCode4()) ||
	 * StringUtils.hasText(buSupplier.getAddress4())){
	 * if(!StringUtils.hasText(buSupplier.getCity4()) ||
	 * !StringUtils.hasText(buSupplier.getArea4()) ||
	 * !StringUtils.hasText(buSupplier.getZipCode4()) ||
	 * !StringUtils.hasText(buSupplier.getAddress4())) throw new
	 * ValidationErrorException("請完整輸入" + tabName + "的其他地址！"); else
	 * if(!ValidateUtil.isNumber(buSupplier.getZipCode4())) throw new
	 * ValidationErrorException(tabName + "的其他地址郵遞區號必須為0~9數字！"); }
	 */

	UserUtils.setOpUserAndDate(buSupplier, loginUser);
    }

    /**
     * 檢核BuEmployee
     * 
     * @param buAddressBook
     * @param buEmployee
     * @param loginUser
     */
    private void doBuEmployeeValidate(BuEmployee buEmployee,
	    String reportPassword, List employeeBrands, String loginUser)
	    throws ValidationErrorException, UniqueConstraintException {

	String tabName = "員工資料頁籤";

	// 將UI輸入的EmployeeCode前後去空白
	if (buEmployee.getAddressBookId() == null) {
	    buEmployee.setEmployeeCode(buEmployee.getEmployeeCode().trim());
	}
	if (!ValidateUtil.isUpperCaseOrNumber(buEmployee.getEmployeeCode())) {
	    throw new ValidationErrorException(tabName + "的工號必須為A~Z或0~9！");
	} else if (buEmployee.getAddressBookId() == null) {
	    BuEmployee employee = (BuEmployee) buEmployeeDAO
		    .findById(buEmployee.getEmployeeCode());
	    if (employee != null)
		throw new UniqueConstraintException(tabName + "的工號："
			+ buEmployee.getEmployeeCode() + "重複！");
	}

	if (!StringUtils.hasText(buEmployee.getEmployeePosition())) {
	    throw new ValidationErrorException("請輸入" + tabName + "的職稱！");
	} else if (!StringUtils.hasText(buEmployee.getIsDepartmentManager())) {
	    throw new ValidationErrorException("請點選" + tabName + "的部門主管！");
	} else if (StringUtils.hasText(buEmployee.getLoginName())
		&& !ValidateUtil.isERPAccountPattern(buEmployee.getLoginName())) {
	    throw new ValidationErrorException(tabName
		    + "的系統登入帳號必須為-、_、A~Z、a~z、0~9！");
	}

	List employeeList = buEmployeeDAO.findDuplicateByPropertyAndId(
		"loginName", buEmployee.getLoginName(), buEmployee
			.getEmployeeCode());
	if (employeeList != null && employeeList.size() > 0) {
	    throw new ValidationErrorException(tabName + "的系統登入帳號："
		    + buEmployee.getLoginName() + "重複！");
	} else if (buEmployee.getArriveDate() != null
		&& buEmployee.getLeaveDate() != null
		&& buEmployee.getLeaveDate().before(buEmployee.getArriveDate())) {
	    throw new ValidationErrorException(tabName + "的到職日不可大於離職日！");
	} else if (StringUtils.hasText(buEmployee.getReportLoginName())
		&& !StringUtils.hasText(reportPassword)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的BI登入密碼！");
	} else if (!StringUtils.hasText(buEmployee.getReportLoginName())
		&& StringUtils.hasText(reportPassword)) {
	    throw new ValidationErrorException("請輸入" + tabName + "的BI登入帳號！");
	} else if (StringUtils.hasText(buEmployee.getReportLoginName())
		&& !ValidateUtil.isBIAccountPattern(buEmployee
			.getReportLoginName())) {
	    throw new ValidationErrorException(tabName
		    + "的BI登入帳號必須為-、_、A~Z、a~z、0~9！");
	} else if ((buEmployee.getAddressBookId() == null || (buEmployee
		.getAddressBookId() != null && !StringUtils.hasText(buEmployee
		.getReportPassword())))
		&& StringUtils.hasText(reportPassword)
		&& "*****".equals(reportPassword)) {
	    throw new ValidationErrorException(tabName + "的BI登入密碼不合法，請重新輸入！");
	}

	// =======================將BI登入密碼加密=====================
	if (buEmployee.getAddressBookId() == null
		|| (buEmployee.getAddressBookId() != null && !StringUtils
			.hasText(buEmployee.getReportPassword()))) {
	    if (StringUtils.hasText(reportPassword)) {
		String encodePwd = Base64.encode(reportPassword.trim()
			.getBytes());
		buEmployee.setReportPassword(encodePwd);
	    }
	} else if (buEmployee.getAddressBookId() != null
		&& StringUtils.hasText(buEmployee.getReportPassword())) {
	    if (!"*****".equals(reportPassword)) {
		String encodePwd = null;
		if (StringUtils.hasText(reportPassword))
		    encodePwd = Base64.encode(reportPassword.trim().getBytes());
		buEmployee.setReportPassword(encodePwd);
	    }
	}

	// ========================建立員工所屬品牌=====================
	Set<BuEmployeeBrand> buEmployeeBrands = buEmployee
		.getBuEmployeeBrands();
	buEmployeeBrands.clear();
	for (int i = 0; i < employeeBrands.size(); i++) {
	    BuEmployeeBrandId employeeBrandId = new BuEmployeeBrandId();
	    employeeBrandId.setEmployeeCode(buEmployee.getEmployeeCode());
	    employeeBrandId.setBrandCode((String) employeeBrands.get(i));
	    BuEmployeeBrand employeeBrand = new BuEmployeeBrand();
	    employeeBrand.setId(employeeBrandId);
	    buEmployeeBrands.add(employeeBrand);
	}

	UserUtils.setOpUserAndDate(buEmployee, loginUser);
	UserUtils.setUserAndDate(buEmployee.getBuEmployeeBrands(), loginUser);
    }

    /**
     * 依據客戶資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findCustomerList(HashMap conditionMap) throws Exception {

	try {
	    String identityCode = (String) conditionMap.get("identityCode");
	    String chineseName = (String) conditionMap.get("chineseName");
	    String englishName = (String) conditionMap.get("englishName");
	    String customerCode = (String) conditionMap.get("customerCode");

	    conditionMap.put("identityCode", identityCode.trim().toUpperCase());
	    conditionMap.put("chineseName", chineseName.trim());
	    conditionMap.put("englishName", englishName.trim());
	    conditionMap.put("customerCode", customerCode.trim().toUpperCase());

	    return buAddressBookDAO.findCustomerList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢客戶資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢客戶資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據通訊錄查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findAddressBookList(HashMap conditionMap) throws Exception {

	try {
	    String identityCode = (String) conditionMap.get("identityCode");
	    String chineseName = (String) conditionMap.get("chineseName");
	    String englishName = (String) conditionMap.get("englishName");
	    String tel1 = (String) conditionMap.get("tel1");
	    String tel2 = (String) conditionMap.get("tel2");
	    String mobilePhone = (String) conditionMap.get("mobilePhone");

	    conditionMap.put("identityCode", identityCode.trim().toUpperCase());
	    conditionMap.put("chineseName", chineseName.trim());
	    conditionMap.put("englishName", englishName.trim());
	    conditionMap.put("tel1", tel1.trim());
	    conditionMap.put("tel2", tel2.trim());
	    conditionMap.put("mobilePhone", mobilePhone.trim());

	    return buAddressBookDAO.findAddressBookList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢通訊錄時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢通訊錄時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據供應商資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findSupplierList(HashMap conditionMap) throws Exception {

	try {
	    String identityCode = (String) conditionMap.get("identityCode");
	    String chineseName = (String) conditionMap.get("chineseName");
	    String supplierCode = (String) conditionMap.get("supplierCode");
	    String customsBroker = (String) conditionMap.get("customsBroker");
	    String agent = (String) conditionMap.get("agent");

	    conditionMap.put("identityCode", identityCode.trim().toUpperCase());
	    conditionMap.put("chineseName", chineseName.trim());
	    conditionMap.put("supplierCode", supplierCode.trim().toUpperCase());
	    conditionMap.put("customsBroker", customsBroker.trim());
	    conditionMap.put("agent", agent.trim());

	    return buAddressBookDAO.findSupplierList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢供應商資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢供應商資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據員工資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findEmployeeList(HashMap conditionMap) throws Exception {

	try {
	    String identityCode = (String) conditionMap.get("identityCode");
	    String chineseName = (String) conditionMap.get("chineseName");
	    String englishName = (String) conditionMap.get("englishName");
	    String employeeCode = (String) conditionMap.get("employeeCode");
	    String employeePosition = (String) conditionMap
		    .get("employeePosition");

	    conditionMap.put("identityCode", identityCode.trim().toUpperCase());
	    conditionMap.put("chineseName", chineseName.trim());
	    conditionMap.put("englishName", englishName.trim());
	    conditionMap.put("employeeCode", employeeCode.trim().toUpperCase());
	    conditionMap.put("employeePosition", employeePosition.trim());

	    return buAddressBookDAO.findEmployeeList(conditionMap);
	} catch (Exception ex) {
	    log.error("查詢員工資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢員工資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據addressBookId和brandCode查出AddressBook、Customer、Supplier資料
     * 
     * @param addressBookId
     * @param brandCode
     * @return Object[]
     */
    public Object[] findAddressBookMembers(Long addressBookId, String brandCode)
	    throws Exception {

	try {
	    BuAddressBook addressBook = (BuAddressBook) buAddressBookDAO
		    .findByPrimaryKey(BuAddressBook.class, addressBookId);
	    BuCustomer customer = buCustomerDAO
		    .findCustomerByAddressBookIdAndBrandCode(addressBookId,
			    brandCode);
	    BuSupplier supplier = buSupplierDAO
		    .findSupplierByAddressBookIdAndBrandCode(addressBookId,
			    brandCode);
	    List employeeList = buEmployeeDAO.findByProperty("addressBookId",
		    addressBookId);
	    BuEmployee employee = employeeList != null
		    && employeeList.size() > 0 ? (BuEmployee) employeeList
		    .get(0) : null;

	    return new Object[] { addressBook, customer, supplier, employee };
	} catch (Exception ex) {
	    log
		    .error("依據addressBookId和brandCode查詢通訊錄時發生錯誤，原因："
			    + ex.toString());
	    throw new Exception("依據addressBookId和brandCode查詢通訊錄時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    public List<Properties> executeSearchInitial(Map parameterMap)
	    throws Exception {
	Map resultMap = new HashMap(0);

	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginBrandCode");
	    Map multiList = new HashMap(0);

	    List<BuCommonPhraseLine> allSupplierType = buCommonPhraseService
		    .getCommonPhraseLinesById("SupplierType", false);
	    List<BuCommonPhraseLine> allSupplierClass = buCommonPhraseService
		    .getCommonPhraseLinesById("SupplierClass", false);

	    multiList.put("allSupplierType", AjaxUtils.produceSelectorData(
		    allSupplierType, "lineCode", "name", true, true));
	    multiList.put("allSupplierClass", AjaxUtils.produceSelectorData(
		    allSupplierClass, "lineCode", "name", true, true));
	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode)
		    .getBrandName());
	    resultMap.put("multiList", multiList);

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

    }

    /**
     * 執行查詢供應商初始化
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchSupplierInitial(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);

	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginBrandCode");

	    Map multiList = new HashMap(0);

	    List<BuCommonPhraseLine> allType = baseDAO.findByProperty(
		    "BuCommonPhraseLine", new String[] {
			    "id.buCommonPhraseHead.headCode", "enable" },
		    new Object[] { "HumanTypeOfLaw", "Y" }, "indexNo");
	    List<BuCommonPhraseLine> allSupplierType = baseDAO.findByProperty(
		    "BuCommonPhraseLine", new String[] {
			    "id.buCommonPhraseHead.headCode", "enable" },
		    new Object[] { "SupplierType", "Y" }, "indexNo");
	    List<BuCommonPhraseLine> allSupplierClass = baseDAO.findByProperty(
		    "BuCommonPhraseLine", new String[] {
			    "id.buCommonPhraseHead.headCode", "enable" },
		    new Object[] { "SupplierClass", "Y" }, "indexNo");

	    multiList.put("allType", AjaxUtils.produceSelectorData(allType,
		    "lineCode", "name", false, true, "2")); // 預設自然人
	    multiList.put("allSupplierType", AjaxUtils.produceSelectorData(
		    allSupplierType, "lineCode", "name", false, true));
	    multiList.put("allSupplierClass", AjaxUtils.produceSelectorData(
		    allSupplierClass, "lineCode", "name", false, true));
	    resultMap.put("multiList", multiList);

	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode)
		    .getBrandName());

	    return resultMap;
	} catch (Exception ex) {
	    log.error("供應商查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("供應商查詢初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 執行查詢供應商初始化
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchCustomerInitial(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);

	try {
	    Map multiList = new HashMap(0);
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String) PropertyUtils.getProperty(
		    otherBean, "loginBrandCode");
	    List<BuCommonPhraseLine> allCustomerType = buCommonPhraseService
		    .getCommonPhraseLinesById("CustomerType", false);
	    List<BuCommonPhraseLine> allMonth = buCommonPhraseService
		    .getCommonPhraseLinesById("Month", false);
	    List<BuCommonPhraseLine> allDay = buCommonPhraseService
		    .getCommonPhraseLinesById("Day", false);
	    List<BuCommonPhraseLine> vipTypeList = buCommonPhraseService
		    .getCommonPhraseLinesById("VIPType", false);
	    List<BuCommonPhraseLine> allVIPType = new ArrayList<BuCommonPhraseLine>(
		    0);
	    for (int i = 0; i < vipTypeList.size(); i++) {
		BuCommonPhraseLine vipType = (BuCommonPhraseLine) vipTypeList
			.get(i);
		String brand = vipType.getAttribute1();
		if (brand != null && brand.equals(loginBrandCode)) {
		    allVIPType.add(vipType);
		}
	    }
	    multiList.put("allCustomerType", AjaxUtils.produceSelectorData(
		    allCustomerType, "lineCode", "name", false, true));
	    multiList.put("allMonth", AjaxUtils.produceSelectorData(allMonth,
		    "lineCode", "name", false, true));
	    multiList.put("allDay", AjaxUtils.produceSelectorData(allDay,
		    "lineCode", "name", false, true));
	    multiList.put("allVIPType", AjaxUtils.produceSelectorData(
		    allVIPType, "lineCode", "name", false, true));
	    resultMap.put("multiList", multiList);

	    return resultMap;
	} catch (Exception ex) {
	    log.error("供應商查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("供應商查詢初始化失敗，原因：" + ex.toString());

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

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String type = httpRequest.getProperty("type");// 單別
	    String identityCode = httpRequest.getProperty("identityCode");
	    String chineseName = httpRequest.getProperty("chineseName");
	    String supplierTypeCode = httpRequest
		    .getProperty("supplierTypeCode");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    String categoryCode = httpRequest.getProperty("categoryCode");
	    String customsBroker = httpRequest.getProperty("customsBroker");
	    String agent = httpRequest.getProperty("agent");
	    String commissionRateStart = httpRequest
		    .getProperty("commissionRateStart");
	    String commissionRateEnd = httpRequest
		    .getProperty("commissionRateEnd");
	    HashMap map = new HashMap();
	    Map findObjs = new HashMap();
	    findObjs.put("brandCode", brandCode);
	    findObjs.put("type", type);
	    findObjs.put("identityCode", identityCode);
	    findObjs.put("chineseName", chineseName);
	    findObjs.put("supplierTypeCode", supplierTypeCode);
	    findObjs.put("supplierCode", supplierCode);
	    findObjs.put("categoryCode", categoryCode);
	    findObjs.put("customsBroker", customsBroker);
	    findObjs.put("agent", agent);
	    findObjs.put("commissionRateStart", commissionRateStart);
	    findObjs.put("commissionRateEnd", commissionRateEnd);

	    Map tmpResultMap = buSupplierWithAddressViewDAO.findPageLine(
		    findObjs, iSPage, iPSize);
	    List<BuSupplierWithAddressView> buSupplierWithAddressViews = null;
	    if (!tmpResultMap.isEmpty()) {
		buSupplierWithAddressViews = (List<BuSupplierWithAddressView>) tmpResultMap
			.get("form");
	    }

	    if (buSupplierWithAddressViews != null
		    && buSupplierWithAddressViews.size() > 0) {
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = (Long) (buSupplierWithAddressViewDAO
			.findPageLine(findObjs, -1, iPSize)).get("recordCount"); // 取得最後一筆
										    // INDEX

		log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageData ");
		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES,
			buSupplierWithAddressViews, gridDatas, firstIndex,
			maxIndex));
	    } else {
		log.info("ImPriceAdjustment.AjaxUtils.getAJAXPageDataDefault ");
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			GRID_SEARCH_FIELD_NAMES,
			GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));

	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的調撥查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的調撥查詢失敗！");
	}
    }

    /**
     * ajax 取得通訊錄供應商search分頁
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchSupplierPageData(Properties httpRequest)
	    throws Exception {

	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

	    String type = httpRequest.getProperty("type");
	    String identityCode = httpRequest.getProperty("identityCode");
	    String chineseName = httpRequest.getProperty("chineseName");
	    String supplierTypeCode = httpRequest
		    .getProperty("supplierTypeCode");
	    String supplierCode = httpRequest.getProperty("supplierCode");
	    String categoryCode = httpRequest.getProperty("categoryCode");
	    String customsBroker = httpRequest.getProperty("customsBroker");
	    String agent = httpRequest.getProperty("agent");
	    String commissionRateStart = httpRequest
		    .getProperty("commissionRateStart");
	    String commissionRateEnd = httpRequest
		    .getProperty("commissionRateEnd");
	    String orderBy = httpRequest.getProperty("orderBy");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();

	    findObjs.put(" and model.brandCode = :brandCode", brandCode);
	    findObjs.put(" and model.type = :type", type);
	    findObjs.put(" and model.identityCode = :identityCode",
		    identityCode);
	    findObjs.put(" and model.chineseName like :chineseName", "%"
		    + chineseName + "%");
	    findObjs.put(" and model.supplierTypeCode = :supplierTypeCode",
		    supplierTypeCode);
	    findObjs.put(" and model.supplierCode like :supplierCode", "%"
		    + supplierCode + "%");
	    findObjs.put(" and model.categoryCode = :categoryCode",
		    categoryCode);
	    findObjs.put(" and model.customsBroker like :customsBroker", "%"
		    + customsBroker + "%");
	    findObjs.put(" and model.agent like :agent", "%" + agent + "%");
	    findObjs.put(" and model.commissionRate >= :commissionRateStart",
		    commissionRateStart);
	    findObjs.put(" and model.commissionRate <= :commissionRateEnd",
		    commissionRateEnd);

	    if (StringUtils.hasText(orderBy)) {
		orderBy = "order by " + orderBy;
	    } else {
		orderBy = "";
	    }
	    // ==============================================================

	    Map buSupplierWithAddressViewMap = baseDAO.search(
		    "BuSupplierWithAddressView as model", findObjs, orderBy,
		    iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List<BuSupplierWithAddressView> buSupplierWithAddressViews = (List<BuSupplierWithAddressView>) buSupplierWithAddressViewMap
		    .get(BaseDAO.TABLE_LIST);

	    if (buSupplierWithAddressViews != null
		    && buSupplierWithAddressViews.size() > 0) {

		// 注入代碼的中文
		this.setSupplierChineseColumn(buSupplierWithAddressViews);

		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = (Long) baseDAO.search(
			"BuSupplierWithAddressView as model",
			"count(model.addressBookId) as rowCount", findObjs,
			orderBy, BaseDAO.QUERY_RECORD_COUNT).get(
			BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			GRID_SEARCH_SUPPLIER_FIELD_NAMES,
			GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES,
			buSupplierWithAddressViews, gridDatas, firstIndex,
			maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			GRID_SEARCH_SUPPLIER_FIELD_NAMES,
			GRID_SEARCH_SUPPLIER_FIELD_DEFAULT_VALUES, map,
			gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的通訊錄供應商查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的通訊錄供應商查詢失敗！");
	}
    }

    /**
     * ajax 取得通訊錄供應商search分頁
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchCustomerPageData(Properties httpRequest)
	    throws Exception {

	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    // ======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String type = httpRequest.getProperty("type");
	    String identityCode = httpRequest.getProperty("identityCode");
	    String chineseName = httpRequest.getProperty("chineseName");
	    String englishName = httpRequest.getProperty("englishName");
	    String gender = httpRequest.getProperty("gender");
	    String birthdayYear = httpRequest.getProperty("birthdayYear");
	    String birthdayMonth = httpRequest.getProperty("birthdayMonth");
	    String birthdayDay = httpRequest.getProperty("birthdayDay");
	    String customerTypeCode = httpRequest
		    .getProperty("customerTypeCode");
	    String customerCode = httpRequest.getProperty("customerCode");
	    String vipTypeCode = httpRequest.getProperty("vipTypeCode");
	    String vipStart = httpRequest.getProperty("vipStartDate");
	    String vipEnd = httpRequest.getProperty("vipEndDate");
	    String application = httpRequest.getProperty("applicationDate");
	    Date vipStartDate = null;
	    Date vipEndDate = null;
	    Date applicationDate = null;
	    if (StringUtils.hasText(vipStart))
		vipStartDate = DateUtils.parseDate("yyyy/MM/dd", vipStart);

	    if (StringUtils.hasText(vipEnd))
		vipEndDate = DateUtils.parseDate("yyyy/MM/dd", vipEnd);

	    if (StringUtils.hasText(application))
		applicationDate = DateUtils
			.parseDate("yyyy/MM/dd", application);

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();

	    findObjs.put(" and model.brandCode = :brandCode", brandCode);
	    findObjs.put(" and model.type = :type", type);
	    findObjs.put(" and model.identityCode = :identityCode",
		    identityCode);
	    findObjs.put(" and model.chineseName like :chineseName", "%"
		    + chineseName + "%");
	    findObjs.put(" and model.englishName like :englishName", "%"
		    + englishName + "%");
	    findObjs.put(" and model.gender = :gender", gender);
	    findObjs.put(" and model.birthdayYear = :birthdayYear",
		    birthdayYear);
	    findObjs.put(" and model.birthdayMonth = :birthdayMonth",
		    birthdayMonth);
	    findObjs.put(" and model.birthdayDay = :birthdayDay", birthdayDay);
	    findObjs.put(" and model.customerTypeCode = :customerTypeCode",
		    customerTypeCode);
	    findObjs.put(" and model.customerCode like :customerCode", "%"
		    + customerCode + "%");
	    findObjs.put(" and model.vipTypeCode = :vipTypeCode", vipTypeCode);
	    findObjs.put(" and model.vipStartDate >= :vipStartDate",
		    vipStartDate);
	    findObjs.put(" and model.vipEndDate <= :vipEndDate", vipEndDate);
	    findObjs.put(" and model.applicationDate = :applicationDate",
		    applicationDate);

	    // ==============================================================

	    Map buCustomerWithAddressViewMap = baseDAO.search(
		    "BuCustomerWithAddressView as model", findObjs,
		    "order by lastUpdateDate", iSPage, iPSize,
		    BaseDAO.QUERY_SELECT_RANGE);
	    List<BuCustomerWithAddressView> buCustomerWithAddressViews = (List<BuCustomerWithAddressView>) buCustomerWithAddressViewMap
		    .get(BaseDAO.TABLE_LIST);
	    if (buCustomerWithAddressViews != null
		    && buCustomerWithAddressViews.size() > 0) {
		// 注入代碼的中文
		// this.setSupplierChineseColumn(buSupplierWithAddressViews);
		Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
		Long maxIndex = (Long) baseDAO.search(
			"BuCustomerWithAddressView as model",
			"count(model.addressBookId) as rowCount", findObjs,
			"order by lastUpdateDate", BaseDAO.QUERY_RECORD_COUNT)
			.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest,
			CUSTOMER_FIELD_NAMES, CUSTOMER_FIELD_DEFAULT_VALUES,
			buCustomerWithAddressViews, gridDatas, firstIndex,
			maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
			CUSTOMER_FIELD_NAMES, CUSTOMER_FIELD_DEFAULT_VALUES,
			map, gridDatas));
	    }
	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的通訊錄供應商查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的通訊錄供應商查詢失敗！");
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
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String) PropertyUtils.getProperty(otherBean,
		    "loginBrandCode");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,
		    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
		    pickerBean, AjaxUtils.SEARCH_KEY);
	    log.info("getSearchSelection.picker_parameter:" + timeScope + "/"
		    + searchKeys.toString());

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
		    searchKeys);
	    log.info("getSearchSelection.result:" + result.size());
	    String chineseName = "";
	    if (result.size() > 0) {
		pickerResult.put("result", result);
		String supplierCode = ((Properties) result.get(0))
			.getProperty("supplierCode");
		BuSupplierWithAddressView buSupplierWithAddressView = buSupplierWithAddressViewDAO
			.findById(supplierCode, brandCode);
		chineseName = buSupplierWithAddressView.getChineseName();
		pickerResult.put("chineseName", chineseName);
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
     * 通訊錄供應商 按檢視返回選取的資料
     * 
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public Map getSearchSupplierSelection(Map parameterMap)
	    throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,
		    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
		    pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
		    searchKeys);
	    if (result.size() > 0) {
		pickerResult.put("result", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel", new String[] { "vatBeanPicker" });

	    return resultMap;
	} catch (Exception ex) {
	    log.error("執行通訊錄供應商檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行通訊錄供應商檢視失敗，原因：" + ex.getMessage());
	}
    }

    /**
     * 通訊錄供應商 按檢視返回選取的資料
     * 
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public Map getSearchCustomerSelection(Map parameterMap)
	    throws FormException, Exception {
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try {
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String) PropertyUtils.getProperty(pickerBean,
		    AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
		    pickerBean, AjaxUtils.SEARCH_KEY);
	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
		    searchKeys);
	    if (result.size() > 0) {
		pickerResult.put("customerResult", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel", new String[] { "vatBeanPicker" });
	    return resultMap;
	} catch (Exception ex) {
	    log.error("執行客戶資料檢視失敗，原因：" + ex.toString());
	    throw new Exception("執行客戶資料商檢視失敗，原因：" + ex.getMessage());
	}
    }

    /**
     * 卡號對應檔 存檔 buAddressBook、buCustomer、buCustomerCard
     * 
     * @param buAddressBook
     * @param buCustomerCard
     * @throws Exception
     */
    public void saveTxtCardMap(BuAddressBook buAddressBook,
    		BuCustomerCard buCustomerCard, Map parameterMap) throws Exception {

    	// #1.若身分證不存在在buAddressBook新增則反之更新
    	// #2.存檔buCustomer，注意若為員工VIP_TYPE_CODE = E1
    	// #3.存檔buCustomerCard，注意塞indexNo，因為1個人可能有多個卡號

    	String uuid = (String) parameterMap.get(FTPFubonImportData.KEY_UUID);
    	Date executeDate = (Date) parameterMap
    			.get(FTPFubonImportData.KEY_EXECUTE_DATE);

    	BuCommonPhraseLine ftpConfig = (BuCommonPhraseLine) baseDAO
    			.findFirstByProperty("BuCommonPhraseLine",
    					"and id.buCommonPhraseHead.headCode = ? and enable =?",
    					new Object[] { "FTPFubonConfig", "Y" });
    	if (ftpConfig == null) {
    		throw new Exception("查BuCommonPhraseLine 無 FTPFubonConfig 配置");
    	}
    	String customerCodeConfigStart = ftpConfig.getAttribute1(); // customerCode
    	String customerCodeConfigEnd = ftpConfig.getAttribute2(); // customerCode
    	Long addressBookIdConfig = NumberUtils.getLong(ftpConfig
    			.getAttribute3()); // addressBookId/
    	String vipTypeCodeConfig = ftpConfig.getAttribute4(); // vipTypeCode
    	String enableConfig = ftpConfig.getAttribute5(); // enable

    	Map findMap = new HashMap();
    	findMap.put(" and id.brandCode = :brandCode", "T2");
    	findMap.put(" and id.customerCode >= :customerCodeStart",
    			customerCodeConfigStart); // range ? OR LIKE "90000000"
    	findMap.put(" and id.customerCode <= :customerCodeEnd",
    			customerCodeConfigEnd); // range ? OR LIKE "90099999"
    	findMap.put(" and addressBookId = :addressBookId", addressBookIdConfig); // 99999999L
    	findMap.put(" and vipTypeCode = :vipTypeCode", vipTypeCodeConfig); // "99"
    	findMap.put(" and enable = :enable", enableConfig); // E

    	// #1
    	BuAddressBook buAddressBookCompare = (BuAddressBook) buAddressBookDAO
    			.findFirstByProperty("BuAddressBook", "and identityCode = ?",
    					new Object[] { buAddressBook.getIdentityCode() });
    	if (null == buAddressBookCompare) { // 若通訊錄找不到身分證表示是新客戶也不是采盟會員
    		// 新增 buAddressBook, buCustomer
    		buAddressBook.setLastUpdatedBy("SYS");
    		buAddressBook.setLastUpdateDate(new Date());
    		buAddressBook.setCreatedBy("SYS");
    		buAddressBook.setCreationDate(new Date());
    		buAddressBook.setOrganizationCode("TM");
    		buAddressBookDAO.save(buAddressBook);

    		SiSystemLogUtils.createSystemLog(FTPFubonImportData.PROCESS_NAME,
    				MessageStatus.LOG_INFO, "新增 BuAddressBook 身分証:"
    						+ buAddressBook.getIdentityCode() + " 成功",
    						executeDate, uuid, "SYS");

    		// #2
    		// FIND range的那筆
    		List<BuCustomer> buCustomers = (List<BuCustomer>) buCustomerDAO
    				.search("BuCustomer", "", findMap,
    						"order by id.customerCode", 0, 1,
    						BaseDAO.QUERY_SELECT_RANGE).get(BaseDAO.TABLE_LIST);
    		if (buCustomers == null || buCustomers.size() == 0) {
    			SiSystemLogUtils.createSystemLog(
    					FTPFubonImportData.PROCESS_NAME, MessageStatus.ERROR,
    					"查品牌T2無可用聯名卡客戶資料", executeDate, uuid, "SYS");
    			throw new Exception("查品牌T2無可用聯名卡客戶資料");
    		}
    		BuCustomer buCustomer = buCustomers.get(0);
    		String customerCodeRange = buCustomer.getId().getCustomerCode();

    		buCustomer.setId(new BuCustomerId(customerCodeRange, "T2")); // 要找到對的RANGE
    		buCustomer.setAddressBookId(buAddressBook.getAddressBookId());
    		buCustomer.setCurrencyCode("NTD");
    		buCustomer.setEnable("N");
    		buCustomer.setIsCurrentUse("N");
    		buCustomer.setLastUpdatedBy("SYS");
    		buCustomer.setLastUpdateDate(new Date());
    		buCustomer.setCreatedBy("SYS");
    		buCustomer.setCreationDate(new Date());
    		buCustomerDAO.update(buCustomer);

    		// log.info("更新一筆 buCustomer 到資料庫");
    		SiSystemLogUtils.createSystemLog(FTPFubonImportData.PROCESS_NAME,
    				MessageStatus.LOG_INFO, "更新 BuCustomer customerCode:"
    						+ customerCodeRange + " 成功", executeDate, uuid,
    				"SYS");

    		// #3
    		// 取得相同 vip卡號 的最大索引值
    		List<BuCustomerCard> buCustomerCardCompare = buCustomerCardDAO
    				.findByProperty("BuCustomerCard",
    						"and customerCode = ? and brandCode = ?",
    						new Object[] { customerCodeRange, "T2" });
    		Long indexNo = 0L;
    		if (buCustomerCardCompare == null
    				|| buCustomerCardCompare.size() == 0) {
    			// log.info("1 = 3.2");
    		} else {
    			// log.info("1 = 3.3");
    			indexNo = buCustomerCardCompare.get(
    					buCustomerCardCompare.size() - 1).getIndexNo();
    		}
    		buCustomerCard.setLastUpdatedBy("SYS");
    		buCustomerCard.setLastUpdateDate(new Date());
    		buCustomerCard.setCreatedBy("SYS");
    		buCustomerCard.setCreationDate(new Date());
    		buCustomerCard.setBrandCode("T2");
    		buCustomerCard.setCustomerCode(customerCodeRange);
    		buCustomerCard.setEnable("Y");
    		buCustomerCard.setIsTransmission("N");
    		buCustomerCard.setIndexNo(indexNo + 1);
    		buCustomerCardDAO.save(buCustomerCard);

    		SiSystemLogUtils.createSystemLog(FTPFubonImportData.PROCESS_NAME,
    				MessageStatus.LOG_INFO, "新增 BuCustomerCard headId="
    						+ buCustomerCard.getHeadId() + "customerCode:"
    						+ customerCodeRange + " 成功", executeDate, uuid,
    				"SYS");
    	} else {
    		// #1
    		// 找到相同身分証表示可能為采盟的會員\廠商則更新資料
    		log.info("更新一筆 buAddressBookCompare");
    		buAddressBookCompare.setGender(buAddressBook.getGender()); // 性別
    		buAddressBookCompare.setEnglishName(buAddressBook.getEnglishName()); // 英文名稱
    		buAddressBookCompare.setBirthdayYear(buAddressBook
    				.getBirthdayYear()); // 出生年
    		buAddressBookCompare.setBirthdayMonth(buAddressBook
    				.getBirthdayMonth()); // 出生月
    		buAddressBookCompare.setBirthdayDay(buAddressBook.getBirthdayDay()); // 出生日
    		buAddressBookCompare.setEMail(buAddressBook.getEMail()); // email
    		buAddressBookCompare.setMobilePhone(buAddressBook.getMobilePhone()); // 行動電話
    		buAddressBookCompare.setChineseName(buAddressBook.getChineseName()); // 中文名稱

    		buAddressBookCompare.setLastUpdatedBy("SYS");
    		buAddressBookCompare.setLastUpdateDate(new Date());
    		buAddressBookDAO.update(buAddressBookCompare);

    		SiSystemLogUtils.createSystemLog(FTPFubonImportData.PROCESS_NAME,
    				MessageStatus.LOG_INFO, "更新 BuAddressBook 身分証:"
    						+ buAddressBook.getIdentityCode() + " 成功",
    						executeDate, uuid, "SYS");
    		// log.info("buCustomerCard = " + buCustomerCard);

    		// #2
    		// 查是否addressBookId已存在在buCustomer
    		List<BuCustomer> buCustomer = buCustomerDAO
    				.findByProperty(
    						"BuCustomer",
    						"and id.brandCode = ? and addressBookId = ? and isCurrentUse = ? and vipTypeCode not in ( '99', 'E1' ) ",
    						new Object[] { "T2",
    								buAddressBookCompare.getAddressBookId(),
    						"Y" });
    		if (null != buCustomer && buCustomer.size() > 0) { // 曾是vip某類型
    			// 將查到所有的BuCustomer非聯名卡的vipTypeCode:99 isCurrentUse 改N
    			for (BuCustomer buCustomerOld : buCustomer) {
    				if (!"E".equalsIgnoreCase(buCustomerOld.getVipTypeCode())) {
    					buCustomerOld.setIsCurrentUse("N");
    					buCustomerOld.setLastUpdatedBy("SYS");
    					buCustomerOld.setLastUpdateDate(new Date());
    				}
    			}

    			// 查是否為員工且離職日為空
    			BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
    					.findFirstByProperty("BuEmployee",
    							"and addressBookId = ? and leaveDate is null ",
    							new Object[] { buAddressBookCompare
    							.getAddressBookId() });
    			boolean isEmployee = false;
    			if (null != buEmployee) {
    				isEmployee = true;
    			}

    			// FIND range的那筆
    			List<BuCustomer> buCustomers = (List<BuCustomer>) buCustomerDAO
    					.search("BuCustomer", "", findMap,
    							"order by id.customerCode", 0, 1,
    							BaseDAO.QUERY_SELECT_RANGE).get(
    									BaseDAO.TABLE_LIST);
    			if (buCustomers == null || buCustomers.size() == 0) {
    				SiSystemLogUtils.createSystemLog(
    						FTPFubonImportData.PROCESS_NAME,
    						MessageStatus.ERROR, "查品牌T2無可用聯名卡客戶資料",
    						executeDate, uuid, "SYS");
    				throw new Exception("查品牌T2無可用聯名卡客戶資料");
    			}
    			BuCustomer buCustomerNew = buCustomers.get(0);
    			String customerCodeRange = buCustomerNew.getId()
    					.getCustomerCode();

    			// 額外加一筆 buCustomer
    			buCustomerNew.setId(new BuCustomerId(customerCodeRange, "T2")); // 要找到對的RANGE
    			buCustomerNew.setAddressBookId(buAddressBookCompare
    					.getAddressBookId());
    			buCustomerNew.setCurrencyCode("NTD");

    			if (isEmployee) {
    				buCustomerNew.setVipTypeCode("E1"); // 查是員工則99改成E1
    			} else {
    				// 非員工可以修改公司名稱
    				buAddressBookCompare.setCompanyName(buAddressBook
    						.getCompanyName()); // 公司名稱
    			}

    			buCustomerNew.setEnable("Y");
    			buCustomerNew.setLastUpdatedBy("SYS");
    			buCustomerNew.setLastUpdateDate(new Date());
    			buCustomerNew.setCreatedBy("SYS");
    			buCustomerNew.setCreationDate(new Date());
    			buCustomerDAO.update(buCustomerNew);

    			SiSystemLogUtils.createSystemLog(
    					FTPFubonImportData.PROCESS_NAME,
    					MessageStatus.LOG_INFO, "新增  buCustomer:"
    							+ customerCodeRange + " 成功", executeDate, uuid,
    					"SYS");

    			List<BuCustomerCard> buCustomerCardCompare = buCustomerCardDAO
    					.findByProperty("BuCustomerCard",
    							"and customerCode = ? and brandCode = ?",
    							new Object[] { customerCodeRange, "T2" }); // buCustomer.id.
    			// buCustomer.id.
    			Long indexNo = 0L;
    			if (buCustomerCardCompare.size() > 0) {
    				indexNo = buCustomerCardCompare.get(
    						buCustomerCardCompare.size() - 1).getIndexNo();
    			}

    			// #3
    			// 新增一筆 buCustomerCard
    			buCustomerCard.setLastUpdatedBy("SYS");
    			buCustomerCard.setLastUpdateDate(new Date());
    			buCustomerCard.setCreatedBy("SYS");
    			buCustomerCard.setCreationDate(new Date());
    			buCustomerCard.setEnable("Y");
    			buCustomerCard.setIsTransmission("N");
    			buCustomerCard.setIndexNo(indexNo + 1);
    			buCustomerCard.setBrandCode("T2");
    			buCustomerCard.setCustomerCode(customerCodeRange);
    			buCustomerCardDAO.save(buCustomerCard);

    			SiSystemLogUtils.createSystemLog(
    					FTPFubonImportData.PROCESS_NAME,
    					MessageStatus.LOG_INFO, "新增 BuCustomerCard headId="
    							+ buCustomerCard.getHeadId() + " 成功",
    							executeDate, uuid, "SYS");
    		} else {
    			// #1
    			// 有buAddbookId 沒有 buCustomer = 沒有vip某類型聯名卡
    			BuCustomer buCustomerSelect = (BuCustomer) buCustomerDAO
    					.findFirstByProperty(
    							"BuCustomer",
    							"and id.brandCode = ? and addressBookId = ? and isCurrentUse = ? and vipTypeCode IN ( '99', 'E1' ) ",
    							new Object[] {
    									"T2",
    									buAddressBookCompare.getAddressBookId(),
    							"Y" });
    			if (null != buCustomerSelect) {
    				SiSystemLogUtils.createSystemLog(
    						FTPFubonImportData.PROCESS_NAME,
    						MessageStatus.LOG_INFO, "查品牌T2 addreebookId"
    								+ buAddressBookCompare.getAddressBookId()
    								+ "已建立聯名卡", executeDate, uuid, "SYS");
    				throw new Exception("查品牌T2 addreebookId"
    						+ buAddressBookCompare.getAddressBookId()
    						+ "已建立聯名卡");
    			}

    			// FIND range的那筆
    			List<BuCustomer> buCustomers = (List<BuCustomer>) buCustomerDAO
    					.search("BuCustomer", "", findMap,
    							"order by id.customerCode", 0, 1,
    							BaseDAO.QUERY_SELECT_RANGE).get(
    									BaseDAO.TABLE_LIST);
    			if (buCustomers == null || buCustomers.size() == 0) {
    				SiSystemLogUtils.createSystemLog(
    						FTPFubonImportData.PROCESS_NAME,
    						MessageStatus.ERROR, "查品牌T2無可用聯名卡客戶資料",
    						executeDate, uuid, "SYS");
    				throw new Exception("查品牌T2無可用聯名卡客戶資料");
    			}
    			BuCustomer buCustomerNew = buCustomers.get(0);
    			String customerCodeRange = buCustomerNew.getId()
    					.getCustomerCode();

    			// 查是否為員工且離職日為空
    			BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
    					.findFirstByProperty("BuEmployee",
    							"and addressBookId = ? and leaveDate is null ",
    							new Object[] { buAddressBookCompare
    							.getAddressBookId() });
    			boolean isEmployee = false;
    			if (null != buEmployee) {
    				isEmployee = true;
    			}

    			// 新增一筆 buCustomer
    			buCustomerNew.setId(new BuCustomerId(customerCodeRange, "T2")); // 要找到對的RANGE
    			buCustomerNew.setAddressBookId(buAddressBookCompare
    					.getAddressBookId());
    			buCustomerNew.setCurrencyCode("NTD");

    			if (isEmployee) {
    				buCustomerNew.setVipTypeCode("E1"); // 查是員工則99改成E1
    			}

    			buCustomerNew.setEnable("Y");
    			buCustomerNew.setLastUpdatedBy("SYS");
    			buCustomerNew.setLastUpdateDate(new Date());
    			buCustomerNew.setCreatedBy("SYS");
    			buCustomerNew.setCreationDate(new Date());
    			buCustomerDAO.update(buCustomerNew);

    			SiSystemLogUtils.createSystemLog(
    					FTPFubonImportData.PROCESS_NAME,
    					MessageStatus.LOG_INFO, "新增  buCustomer:"
    							+ customerCodeRange + " 成功", executeDate, uuid,
    					"SYS");

    			List<BuCustomerCard> buCustomerCardCompare = buCustomerCardDAO
    					.findByProperty("BuCustomerCard",
    							"and customerCode = ? and brandCode = ?",
    							new Object[] { customerCodeRange, "T2" }); // buCustomer.id.
    			// buCustomer.id.
    			Long indexNo = 0L;
    			if (buCustomerCardCompare.size() > 0) {
    				indexNo = buCustomerCardCompare.get(
    						buCustomerCardCompare.size() - 1).getIndexNo();
    			}

    			// #2
    			// 新增一筆 buCustomerCard
    			buCustomerCard.setLastUpdatedBy("SYS");
    			buCustomerCard.setLastUpdateDate(new Date());
    			buCustomerCard.setCreatedBy("SYS");
    			buCustomerCard.setCreationDate(new Date());
    			buCustomerCard.setEnable("Y");
    			buCustomerCard.setIsTransmission("N");
    			buCustomerCard.setIndexNo(indexNo + 1);
    			buCustomerCard.setBrandCode("T2");
    			buCustomerCard.setCustomerCode(customerCodeRange);
    			buCustomerCardDAO.save(buCustomerCard);

    			SiSystemLogUtils.createSystemLog(
    					FTPFubonImportData.PROCESS_NAME,
    					MessageStatus.LOG_INFO, "新增 BuCustomerCard headId="
    							+ buCustomerCard.getHeadId() + " 成功",
    							executeDate, uuid, "SYS");
    		}
    	}
    }

    /**
     * 基本檔 存檔 AddressBook 和 BuCustomer
     * 
     * @param buAddressBook
     * @param buCustomer
     * @throws Exception
     */
    public void saveTxtAddressBookAndBuCustomerCardBean(
	    BuAddressBook buAddressBook, BuCustomerCard buCustomerCard)
	    throws Exception {
	// #1 更新buAddressBook
	// #2 更新BuCustomerCard
	// #3 更新BuCustomer，
	// 依BuCustomerCard的IssueDate、ExpirationDate回寫BuCustomer的VipStartDate、VipEndDate
	// #3.1
	// 注意若BuCustomerCard依CustomerCode、CardNo查不到表示此人申請第二張卡，會新增BuCustomer、BuCustomerCard
	// 20110518-david

	// #1
	BuAddressBook buAddressBookCompare = (BuAddressBook) buAddressBookDAO
		.findFirstByProperty("BuAddressBook", "and identityCode = ?",
			new Object[] { buAddressBook.getIdentityCode() });
	if (null == buAddressBookCompare) { // 若通訊錄找不到身分證表示有問題, 之前卡號對應檔未傳
	    throw new Exception("查BuAddressBook不存在此身分證:"
		    + buAddressBook.getIdentityCode());
	} else {
	    // 只有ADDRESS_BOOK_ID 為KEY
	    // 無法撈唯一值，目前暫時用customerCode+brandCode撈預設第一筆(漏洞)
	    BuCustomer buCustomer = (BuCustomer) buCustomerDAO
		    .findFirstByProperty("BuCustomer",
			    "and id.customerCode = ? and id.brandCode = ? ",
			    new Object[] { buCustomerCard.getCustomerCode(),
				    "T2" });
	    if (null == buCustomer) {
		throw new Exception("查無品牌:T2 在buCustomer客戶代碼:"
			+ buCustomerCard.getCustomerCode() + "的資料");
	    }

	    // 找到相同身分証表示可能為采盟會員, 再確認, 更新buCustomerCard
	    // log.info("更新一筆 buAddressBook.getIdentityCode() = " +
	    // buAddressBook.getIdentityCode());
	    buAddressBookCompare.setGender(buAddressBook.getGender()); // 性別
	    buAddressBookCompare.setEnglishName(buAddressBook.getEnglishName()); // 英文名稱
	    buAddressBookCompare.setBirthdayYear(buAddressBook.getBirthdayYear()); // 出生年
	    buAddressBookCompare.setBirthdayMonth(buAddressBook.getBirthdayMonth()); // 出生月
	    buAddressBookCompare.setBirthdayDay(buAddressBook.getBirthdayDay()); // 出生日
	    buAddressBookCompare.setEMail(buAddressBook.getEMail()); // email
	    buAddressBookCompare.setMobilePhone(buAddressBook.getMobilePhone()); // 行動電話
	    buAddressBookCompare.setChineseName(buAddressBook.getChineseName()); // 中文名稱

	    // 查是否為員工且離職日為空
	    BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
		    .findFirstByProperty("BuEmployee",
			    "and addressBookId = ? and leaveDate is null ",
			    new Object[] { buAddressBookCompare
				    .getAddressBookId() });
	    // 非員工才會更新公司名稱
	    if (null == buEmployee) {
		buAddressBookCompare.setCompanyName(buAddressBook
			.getCompanyName()); // 公司名稱
	    }

	    buAddressBookCompare.setAddress(buAddressBook.getAddress()); // 地址
	    buAddressBookCompare.setZipCode(buAddressBook.getZipCode()); // 郵遞區號
	    buAddressBookCompare.setLastUpdatedBy("SYS");
	    buAddressBookCompare.setLastUpdateDate(new Date());
	    buAddressBookDAO.update(buAddressBookCompare);

	    // #2
	    BuCustomerCard hasBuCustomerCard = (BuCustomerCard) buCustomerCardDAO
		    .findFirstByProperty("BuCustomerCard",
			    "and customerCode = ? ",
			    new Object[] { buCustomerCard.getCustomerCode() });
	    if (null != hasBuCustomerCard) {
		// log.info("查客戶卡 hasBuCustomerCard: " +
		// hasBuCustomerCard.getCustomerCode() + "更新聯名卡");
		setBuCustomerCard(buCustomerCard, hasBuCustomerCard);
		buCustomerCardDAO.update(hasBuCustomerCard);

	    }else {
	    /*
		Caspar Mark 2012/11/08
		// #3.1
		// 表示有另一種卡(卡類別(MP--MasterCard白金卡;MT--MasterCard鈦金卡))
		// 新增 BuCustomer

		BuCommonPhraseLine ftpConfig = (BuCommonPhraseLine) baseDAO
			.findFirstByProperty(
				"BuCommonPhraseLine",
				"and id.buCommonPhraseHead.headCode = ? and enable =?",
				new Object[] { "FTPFubonConfig", "Y" });
		if (ftpConfig == null) {
		    throw new Exception(
			    "查BuCommonPhraseLine 無 FTPFubonConfig 配置");
		}

		String customerCodeConfigStart = ftpConfig.getAttribute1(); // customerCode
		String customerCodeConfigEnd = ftpConfig.getAttribute2(); // customerCode
		Long addressBookIdConfig = NumberUtils.getLong(ftpConfig
			.getAttribute3()); // addressBookId/
		String vipTypeCodeConfig = ftpConfig.getAttribute4(); // vipTypeCode
		String enableConfig = ftpConfig.getAttribute5(); // enable

		Map findMap = new HashMap();
		findMap.put(" and id.brandCode = :brandCode", "T2");
		findMap.put(" and id.customerCode >= :customerCodeStart",
			customerCodeConfigStart); // range ? OR LIKE
						    // "90000000"
		findMap.put(" and id.customerCode <= :customerCodeEnd",
			customerCodeConfigEnd); // range ? OR LIKE "90099999"
		findMap.put(" and addressBookId = :addressBookId",
			addressBookIdConfig); // 99999999L
		findMap.put(" and vipTypeCode = :vipTypeCode",
			vipTypeCodeConfig); // "99"
		findMap.put(" and enable = :enable", enableConfig); // E

		// FIND range的那筆
		List<BuCustomer> buCustomers = (List<BuCustomer>) buCustomerDAO
			.search("BuCustomer", "", findMap,
				"order by id.customerCode", 0, 1,
				BaseDAO.QUERY_SELECT_RANGE).get(
				BaseDAO.TABLE_LIST);
		if (buCustomers == null || buCustomers.size() == 0) {
		    throw new Exception("查品牌T2無可用聯名卡客戶資料");
		}
		BuCustomer addBuCustomer = buCustomers.get(0);
		String customerCodeRange = addBuCustomer.getId()
			.getCustomerCode();

		BeanUtils.copyProperties(buCustomer, addBuCustomer);
		addBuCustomer.setId(new BuCustomerId(customerCodeRange, "T2"));
		*/

		// 新增 BuCustomerCard
		hasBuCustomerCard = new BuCustomerCard();
		hasBuCustomerCard.setCustomerCode(buCustomerCard.getCustomerCode());
		hasBuCustomerCard.setCreatedBy("SYS");
		hasBuCustomerCard.setCreationDate(new Date());
		hasBuCustomerCard.setBrandCode("T2");
		hasBuCustomerCard.setEnable("Y");
		hasBuCustomerCard.setIsTransmission("Y"); // 第二張卡不用再下傳
		hasBuCustomerCard.setIndexNo(1l); // 由於依CARD_NO、CUSTOMER_CODE、BRAND_CODE為key，目前都皆為唯一，包含有兩張卡也是
		setBuCustomerCard(buCustomerCard, hasBuCustomerCard);
		buCustomerCardDAO.save(hasBuCustomerCard);

	    }
		// #3
		// 更新原 buCustomer 的 vip起迄日期
		setBuCustomer(hasBuCustomerCard, buCustomer);
		buCustomerDAO.update(buCustomer);
	}
    }

    /**
     * 回寫BuCustomer
     * 
     * @param buCustomerCard
     * @param buCustomer
     * @throws Exception
     */
    private void setBuCustomer(BuCustomerCard buCustomerCard,
	    BuCustomer buCustomer) throws Exception {
	DecimalFormat df = new DecimalFormat("00");

	buCustomer.setLastUpdatedBy("SYS");
	buCustomer.setLastUpdateDate(new Date());
	buCustomer.setEnable("Y");
	buCustomer.setIsCurrentUse("Y");
	if (StringUtils.hasText(buCustomerCard.getIssueDate())) {
	    buCustomer.setVipStartDate(DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, buCustomerCard
			    .getIssueDate()));
	} else {
	    throw new Exception("查品牌:T2 客戶代碼:"
		    + buCustomerCard.getCustomerCode() + "無 IssueDate 發卡日");
	}

	String expirationDate = buCustomerCard.getExpirationDate(); // YYMM格式四碼
	if (StringUtils.hasText(expirationDate)) {
	    // 取得最後一天
	    int day = DateUtils.getLastDayOfMonth(NumberUtils.getInt("20"
		    + expirationDate.substring(0, 2)), NumberUtils
		    .getInt(expirationDate.substring(2)));
	    buCustomer.setVipEndDate(DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, "20" + expirationDate
			    + df.format(day)));
	}
    }

    /**
     * copy
     * 
     * @param buCustomerCard
     * @param hasBuCustomerCard
     * @throws Exception
     */
    private void setBuCustomerCard(BuCustomerCard buCustomerCard,
	    BuCustomerCard hasBuCustomerCard) throws Exception {
	hasBuCustomerCard.setCardNo(buCustomerCard.getCardNo());
	hasBuCustomerCard.setCardType(buCustomerCard.getCardType());
	hasBuCustomerCard.setMaritalStatus(buCustomerCard.getMaritalStatus());
	hasBuCustomerCard.setEducationalBackground(buCustomerCard
		.getEducationalBackground());
	hasBuCustomerCard.setNationality(buCustomerCard.getNationality());
	hasBuCustomerCard.setBusinessCode(buCustomerCard.getBusinessCode());
	hasBuCustomerCard.setClassCode(buCustomerCard.getClassCode());
	hasBuCustomerCard.setYearIncome(buCustomerCard.getYearIncome());
	hasBuCustomerCard.setSeniority(buCustomerCard.getSeniority());
	hasBuCustomerCard.setIsNeedDm(buCustomerCard.getIsNeedDm());
	hasBuCustomerCard.setHomeRegionCode(buCustomerCard.getHomeRegionCode());
	hasBuCustomerCard.setHomeTel1(buCustomerCard.getHomeTel1());
	hasBuCustomerCard.setCompanyRegionCode(buCustomerCard
		.getCompanyRegionCode());
	hasBuCustomerCard.setCompanyTel1(buCustomerCard.getCompanyTel1());
	hasBuCustomerCard.setCompanyExtension(buCustomerCard
		.getCompanyExtension());
	hasBuCustomerCard.setGender1(buCustomerCard.getGender1());
	hasBuCustomerCard.setBirthday1(buCustomerCard.getBirthday1());
	hasBuCustomerCard.setGender2(buCustomerCard.getGender2());
	hasBuCustomerCard.setBirthday2(buCustomerCard.getBirthday2());
	hasBuCustomerCard.setGender3(buCustomerCard.getGender3());
	hasBuCustomerCard.setBirthday3(buCustomerCard.getBirthday3());
	hasBuCustomerCard.setCardCategory(buCustomerCard.getCardCategory());
	hasBuCustomerCard.setIssueDate(buCustomerCard.getIssueDate());
	hasBuCustomerCard.setExpirationDate(buCustomerCard.getExpirationDate());
	hasBuCustomerCard.setProfessionalTitle(buCustomerCard
		.getProfessionalTitle());
	hasBuCustomerCard.setName1(buCustomerCard.getName1());
	hasBuCustomerCard.setName2(buCustomerCard.getName2());
	hasBuCustomerCard.setName3(buCustomerCard.getName3());
	hasBuCustomerCard.setRecommended(buCustomerCard.getRecommended());

	hasBuCustomerCard.setLastUpdatedBy("SYS");
	hasBuCustomerCard.setLastUpdateDate(new Date());
    }

    /**
     * 存檔 BuCustomerEvent
     * 
     * @param buAddressBook
     * @param buCustomer
     * @throws Exception
     */
    public void saveTxtBuCustomerCardEventBean(
    		BuCustomerCardEvent buCustomerCardEvent) throws Exception {

    	// #1
    	// 更新BuCustomerCard，注意若buCustomerCardEvent的SuspendCode=00且ReasonCode為null或空值則buCustomerCard.setEnable("Y");
    	// 反之N
    	// #2 新增BuCustomerCardEvent
    	// #3 更新BuCustomer，承#1 若BuCustomerCard為啟用則BuCustomer也啟用反之都停用

    	log.info("BuCustomerCardEvent = " + buCustomerCardEvent);
    	BuCustomerCard buCustomerCard = (BuCustomerCard) buCustomerCardDAO
    			.findFirstByProperty("BuCustomerCard", "and cardNo = ?",
    					new Object[] { buCustomerCardEvent.getCardNo() });
    	log.info("buCustomerCard = " + buCustomerCard);
    	log.info("CardNo = " + buCustomerCardEvent.getCardNo());

    	// #1
    	if (null == buCustomerCard) {
    		throw new Exception("查無客戶資料 CardNo:"
    				+ buCustomerCardEvent.getCardNo());
    	} else {
    		// log.info("1 = 1" );
    		if (buCustomerCardEvent.getSuspendCode().equals("00")
    				&& (buCustomerCardEvent.getReasonCode() == null || buCustomerCardEvent
    				.getReasonCode().equals(""))) {
    			// 表示恢復可使用的卡
    			buCustomerCard.setEnable("Y");
    		} else {
    			buCustomerCard.setEnable("N");
    		}
    		// log.info("1 = 2" );
    		buCustomerCard.setLastUpdatedBy("SYS");
    		buCustomerCard.setLastUpdateDate(new Date());
    		// buCustomerCard.setIsTransmission("N");
    		buCustomerCardDAO.update(buCustomerCard);

    		// log.info("1 = 3" );
    		List<BuCustomerCardEvent> buCustomerCardEventMax = buCustomerCardEventDAO
    				.findByProperty("BuCustomerCardEvent", "and cardNo = ?",
    						new Object[] { buCustomerCardEvent.getCardNo() });
    		Long indexNo = 0L;
    		if (buCustomerCardEventMax.size() > 0) {
    			indexNo = buCustomerCardEventMax.get(
    					buCustomerCardEventMax.size() - 1).getIndexNo();
    		}
    		// log.info("1 = 4" );
    		// #2
    		BuCustomerCard buCustomerCardtmp = new BuCustomerCard();
    		buCustomerCardtmp.setHeadId(buCustomerCard.getHeadId());
    		buCustomerCardEvent.setBuCustomerCard(buCustomerCardtmp);
    		buCustomerCardEvent.setLastUpdatedBy("SYS");
    		buCustomerCardEvent.setLastUpdateDate(new Date());
    		buCustomerCardEvent.setCreatedBy("SYS");
    		buCustomerCardEvent.setCreationDate(new Date());
    		buCustomerCardEvent.setIndexNo(indexNo + 1);
    		// log.info("1 = 5" );
    		buCustomerCardEventDAO.save(buCustomerCardEvent);
    		// log.info("1 = 6" );
    	}

    	// #3
    	// log.info("1 = 7" );
    	// 若有一個enable為Y的buCustomerCard則更新buCustomer為Y,反之buCustomerCard皆為N時,更新buCustomer為N
    	List<BuCustomerCard> buCustomerCardUpdateEnable = buCustomerCardDAO
    			.findByProperty("BuCustomerCard",
    					"and customerCode = ? and brandCode = ?", new Object[] {
    					buCustomerCard.getCustomerCode(),
    					buCustomerCard.getBrandCode() });
    	if (null == buCustomerCardUpdateEnable) {
    		throw new Exception("查無客戶資料 customerCode:"
    				+ buCustomerCard.getCustomerCode() + " brandCode:"
    				+ buCustomerCard.getBrandCode());
    	}
    	BuCustomer buCustomer = (BuCustomer) buCustomerDAO.findFirstByProperty(
    			"BuCustomer", "and id.customerCode = ? and id.brandCode = ?",
    			new Object[] { buCustomerCard.getCustomerCode(),
    					buCustomerCard.getBrandCode() });
    	buCustomer.setLastUpdateDate(new Date());
    	boolean isEnable = false;
    	for (BuCustomerCard buCustomerCard2 : buCustomerCardUpdateEnable) {
    		if (buCustomerCard2.getEnable().equals("Y")) {
    			buCustomer.setEnable("Y");
    			buCustomer.setIsCurrentUse("Y");
    			isEnable = true;
    			buCustomerDAO.update(buCustomer);
    			break;
    		}
    	}
    	if (!isEnable) {
    		buCustomer.setEnable("N");
    		buCustomer.setIsCurrentUse("N");
    		buCustomerDAO.update(buCustomer);
    	}
    }

    /**
     * 存取 BuCustomerCard
     * 
     * @throws Exception
     */
    public void updateTxtBuCustomerCard(Map parameterMap) throws Exception {
	String uuid = (String) parameterMap.get(FTPFubonExportData.KEY_UUID);
	Date executeDate = (Date) parameterMap
		.get(FTPFubonExportData.KEY_EXECUTE_DATE);

	List<BuCustomerCard> updateBuCustomerCards = buCustomerDAO
		.findByProperty(
			"BuCustomerCard",
			"and brandCode = ? and enable = ? and cardNo is null and isTransmission = ?",
			new Object[] { "T2", "Y", "N" });
	for (BuCustomerCard buCustomerCard : updateBuCustomerCards) {
	    buCustomerCard.setIsTransmission("Y");
	    buCustomerCard.setLastUpdateDate(new Date());
	    buCustomerCard.setLastUpdatedBy("SYS");
	    buCustomerDAO.update(buCustomerCard);
	    log.info("更新 cardNo = " + buCustomerCard.getCardNo()
		    + " 的IsTransmission欄位為 Y");
	    SiSystemLogUtils.createSystemLog(FTPFubonExportData.PROCESS_NAME,
		    MessageStatus.LOG_INFO, "更新 cardNo = "
			    + buCustomerCard.getCardNo()
			    + " 的IsTransmission欄位為 Y成功", executeDate, uuid,
		    "SYS");
	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest)
	    throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 通訊錄供應商查詢結果存檔
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchSupplierResult(Properties httpRequest)
	    throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest,
		GRID_SEARCH_SUPPLIER_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 客戶資料查詢結果存檔
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchCustomerResult(Properties httpRequest)
	    throws Exception {
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, CUSTOMER_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 設定供應商類型的中文,廠商類別的中文,廠商類型的中文
     * 
     * @param lines
     * @throws Exception
     */
    private void setSupplierChineseColumn(List<BuSupplierWithAddressView> lines)
	    throws Exception {

	final String TYPE_HEAD_CODE = "HumanTypeOfLaw";
	final String SUPPLIER_TYPE_HEAD_CODE = "SupplierType";
	final String SUPPLIER_CLASS_HEAD_CODE = "SupplierClass";

	List<BuCommonPhraseLine> allType = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { TYPE_HEAD_CODE, "Y" }, "indexNo");
	List<BuCommonPhraseLine> allSupplierType = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { SUPPLIER_TYPE_HEAD_CODE, "Y" }, "indexNo");
	List<BuCommonPhraseLine> allSupplierClass = baseDAO.findByProperty(
		"BuCommonPhraseLine", new String[] {
			"id.buCommonPhraseHead.headCode", "enable" },
		new Object[] { SUPPLIER_CLASS_HEAD_CODE, "Y" }, "indexNo");

	Map map = new HashMap();

	for (BuCommonPhraseLine type : allType) {
	    map.put(type.getId().getBuCommonPhraseHead().getHeadCode()
		    + type.getId().getLineCode(), type.getName());
	}

	for (BuCommonPhraseLine supplierType : allSupplierType) {
	    map.put(supplierType.getId().getBuCommonPhraseHead().getHeadCode()
		    + supplierType.getId().getLineCode(), supplierType
		    .getName());
	}

	for (BuCommonPhraseLine supplierClass : allSupplierClass) {
	    map.put(supplierClass.getId().getBuCommonPhraseHead().getHeadCode()
		    + supplierClass.getId().getLineCode(), supplierClass
		    .getName());
	}

	for (BuSupplierWithAddressView buSupplierWithAddressView : lines) {

	    String type = buSupplierWithAddressView.getType();
	    String supplierTypeCode = buSupplierWithAddressView
		    .getSupplierTypeCode();
	    String categoryCode = buSupplierWithAddressView.getCategoryCode();

	    if (map.containsKey(TYPE_HEAD_CODE + type)) {
		buSupplierWithAddressView.setTypeName((String) map
			.get(TYPE_HEAD_CODE + type));
	    }

	    if (map.containsKey(SUPPLIER_TYPE_HEAD_CODE + supplierTypeCode)) {
		buSupplierWithAddressView.setSupplierTypeCodeName((String) map
			.get(SUPPLIER_TYPE_HEAD_CODE + supplierTypeCode));
	    }

	    if (map.containsKey(SUPPLIER_CLASS_HEAD_CODE + categoryCode)) {
		buSupplierWithAddressView.setCategoryCodeName((String) map
			.get(SUPPLIER_CLASS_HEAD_CODE + categoryCode));
	    }

	}
    }

    /**
     * 透過傳遞過來的參數來做Customer下傳
     * 
     * @param parameterMap
     * @throws Exception
     */
    public Long executePosCustomerExport(HashMap parameterMap) throws Exception {
	log.info("executePosCustomerExport");
	Long responseId = 0L;
	Long numbers = 0L;

	// 一、解析程式需要排程下傳或是即時下傳
	Long batchId = (Long) parameterMap.get("BATCH_ID");
	String uuId = posExportDAO.getDataId();// 產生dataId

	// 二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
	if (null == batchId || batchId <= 0) {
	    // 輸入搜尋條件(排程)
	    parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
	    parameterMap.put("dataDate", DateUtils.format((Date) parameterMap
		    .get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
	    parameterMap.put("dataDateEnd", DateUtils.format(
		    (Date) parameterMap.get("DATA_DATE_END"),
		    DateUtils.C_DATA_PATTON_YYYYMMDD));
	    List<Object[]> results = buAddressBookDAO
		    .findCustomerListNoLimit(parameterMap);
	    if (results != null && results.size() > 0) {
		for (Object[] result : results) {
		    BuAddressBook buAddressBook = (BuAddressBook) result[0];
		    BuCustomer buCustomer = (BuCustomer) result[1];
		    PosCustomer posCustomer = new PosCustomer();
		    // 將addressBook與customer複製到posCustomer
		    exportPosCustomer(buAddressBook, buCustomer, posCustomer);
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
			    .getBuCommonPhraseLine("VIPType", posCustomer
				    .getVipTypeCode());
		    if (null != buCommonPhraseLine) {
			posCustomer.setIdentification(buCommonPhraseLine
				.getAttribute4());
		    }
		    posCustomer.setDataId(uuId);
		    posExportDAO.save(posCustomer);
		}
	    }else{
	        //查無資料
	        parameterMap.put("searchResult","noData");
	    }
	} else {
	    // 非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
	    String dataId = (String) parameterMap.get("DATA_ID");
	    // 尋找PosCustomer中此dataID有哪些需求資料
	    List<PosCustomer> posCustomers = posExportDAO.findByProperty(
		    "PosCustomer", new String[] { "dataId" },
		    new Object[] { dataId });
	    for (Iterator iterator = posCustomers.iterator(); iterator
		    .hasNext();) {
		PosCustomer posCustomer = (PosCustomer) iterator.next();
		HashMap conditionMap = new HashMap();
		conditionMap.put("brandCode", posCustomer.getBrandCode());
		conditionMap.put("customerCode", posCustomer.getCustomerCode());
		// 將每一筆資料進資料庫查，再新建立一筆資料
		List<Object[]> results = buAddressBookDAO
			.findCustomerList(conditionMap);
		if (results != null && results.size() >= 0) {
		    Object[] result = results.get(0);
		    BuAddressBook buAddressBook = (BuAddressBook) result[0];
		    BuCustomer buCustomer = (BuCustomer) result[1];
		    // 將addressBook與customer複製到newPosCustomer
		    PosCustomer newPosCustomer = new PosCustomer();
		    exportPosCustomer(buAddressBook, buCustomer, newPosCustomer);
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
			    .getBuCommonPhraseLine("VIPType", newPosCustomer
				    .getVipTypeCode());
		    if (null != buCommonPhraseLine) {
			newPosCustomer.setIdentification(buCommonPhraseLine
				.getAttribute4());
		    }
		    newPosCustomer.setDataId(uuId);
		    posExportDAO.save(newPosCustomer);
		}
	    }
	}

	// 更新新的DATA_ID做回傳
	parameterMap.put("DATA_ID", uuId);
	parameterMap.put("NUMBERS", numbers);
	responseId = posExportDAO.executeCommand(parameterMap);
	return responseId;
    }

    /**
     * 將Customer下傳複製到PosCustomer
     * 
     * @param parameterMap
     * @throws Exception
     */
    public void exportPosCustomer(BuAddressBook buAddressBook,
	    BuCustomer buCustomer, PosCustomer posCustomer) {
	BeanUtils.copyProperties(buAddressBook, posCustomer);
	BeanUtils.copyProperties(buCustomer, posCustomer);
	posCustomer.setBrandCode(buCustomer.getId().getBrandCode());
	posCustomer.setCustomerCode(buCustomer.getId().getCustomerCode());
	posCustomer.setAction("U");
    }

    /**
     * 透過傳遞過來的參數來做Employee下傳
     * 
     * @param parameterMap
     * @throws Exception
     */
    public Long executePosEmployeeExport(HashMap parameterMap) throws Exception {
    	log.info("executePosEmployeeExport");
    	Long responseId = -1L;
    	Long numbers = 0L;

    	// 一、解析程式需要排程下傳或是即時下傳
    	Long batchId = (Long) parameterMap.get("BATCH_ID");
    	String uuId = posExportDAO.getDataId();// 產生dataId

    	// 二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
    	if (null == batchId || batchId <= 0) {
    		//輸入搜尋條件(排程)
    		parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
    		parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
    		parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
    		List<Object[]> results = buAddressBookDAO.findEmpByCondition(parameterMap);
    		if (results != null && results.size() > 0) {
    			for (Object[] result : results) {
    				BuAddressBook buAddressBook = (BuAddressBook) result[0];
    				BuEmployee buEmployee = (BuEmployee) result[1];
    				PosEmployee posEmp = new PosEmployee();
					posEmp.setChineseName(buAddressBook.getChineseName());
    				BeanUtils.copyProperties(buEmployee, posEmp);
    				
					BuCommonPhraseLine line = buCommonPhraseService.getBuCommonPhraseLine("EmployeeDepartment", posEmp.getEmployeeDepartment());
    				if (null != line)
    					posEmp.setEmployeeDepartmentName(line.getName());
    				
    				posEmp.setAction("U");
    				posEmp.setDataId(uuId);
    				posExportDAO.save(posEmp);
    			}
    		}else{
    			//查無資料
    			parameterMap.put("searchResult","noData");
    		}
    	} else {
    		// 非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
    		String dataId = (String) parameterMap.get("DATA_ID");
    		// 尋找PosCustomer中此dataID有哪些需求資料
    		List<PosEmployee> posEmployees = posExportDAO.findByProperty(
    				"PosEmployee", new String[] { "dataId" }, new Object[] { dataId });
    		for (Iterator iterator = posEmployees.iterator(); iterator.hasNext();) {
    			PosEmployee posEmployee = (PosEmployee) iterator.next();
    			// 將每一筆資料進資料庫查，再補全資料
    			BuEmployeeWithAddressView buEmployeeWithAddressView = (BuEmployeeWithAddressView) baseDAO
    					.findById("BuEmployeeWithAddressView", posEmployee.getEmployeeCode());
    			
    			//如果有E-mail，則更新E-mail
    			if (null != buEmployeeWithAddressView) {
    				if(StringUtils.hasText(posEmployee.getEMailCompany())){
    					BuEmployee buEmployee = (BuEmployee)baseDAO.findById("BuEmployee", posEmployee.getEmployeeCode());
    					buEmployee.setEMailCompany(posEmployee.getEMailCompany());
    					baseDAO.update(buEmployee);
    					responseId = 0L;
    				}else if(StringUtils.hasText(posEmployee.getPassword())){
    					BuEmployee buEmployee = (BuEmployee)baseDAO.findById("BuEmployee", posEmployee.getEmployeeCode());
    					buEmployee.setPassword(posEmployee.getPassword());
    					baseDAO.update(buEmployee);
    					responseId = 0L;
    				}else{
    					PosEmployee newPosEmployee = new PosEmployee();
    					BeanUtils.copyProperties(buEmployeeWithAddressView, newPosEmployee);
    					BuCommonPhraseLine line = buCommonPhraseService.getBuCommonPhraseLine("EmployeeDepartment", newPosEmployee.getEmployeeDepartment());
    					if (null != line)
    						newPosEmployee.setEmployeeDepartmentName(line.getName());

    					newPosEmployee.setAction("U");
    					newPosEmployee.setDataId(uuId);
    					posExportDAO.save(newPosEmployee);
        			    	// 更新新的DATA_ID做回傳
        			    	parameterMap.put("DATA_ID", uuId);
        			    	parameterMap.put("NUMBERS", numbers);
        			    	responseId = posExportDAO.executeCommand(parameterMap);
    				}
    			}
    		}
    	}
    	return responseId;
    }
    
    public BuAddressBook findIdentityCodeByProperty(String identityCode){
    	return buAddressBookDAO.findIdentityCodeByProperty(identityCode);
    }
    
    public void update(BuAddressBook buAddressBook){
    	buAddressBookDAO.update(buAddressBook);
    }

}
