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
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdCategory;
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.AdCustServiceLine;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.AdCategoryDAO;
import tw.com.tm.erp.hbm.dao.AdCustServiceDAO;
import tw.com.tm.erp.hbm.dao.AdCustServiceLineDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuGoalAchevementDAO;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DES;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class AdCustomerServiceService {

	private static final Log log = LogFactory
			.getLog(AdCustomerServiceService.class);

	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	private BuPurchaseHeadDAO buPurchaseHeadDAO;
	private BuEmployeeDAO buEmployeeDAO;
	private BaseDAO baseDAO;
	private AdCategoryDAO adCategoryDAO;
	private BuBasicDataService buBasicDataService;
	private ImWarehouseService imWarehouseService;
	private AdCustServiceDAO adCustServiceDAO;
	private AdCustServiceLineDAO adCustServiceLineDAO;
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	private SoSalesOrderItemDAO soSalesOrderItemDAO;
	private BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO;
	private ImItemDAO imItemDAO;
	private BuAddressBookDAO buAddressBookDAO;
	private BuCustomerDAO buCustomerDAO;
	private DataSource dataSource;
	private ImItemCategoryService imItemCategoryService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;

	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setImItemCategoryService(
			ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
		this.buCustomerDAO = buCustomerDAO;
	}

	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuCustomerWithAddressViewDAO(
			BuCustomerWithAddressViewDAO buCustomerWithAddressViewDAO) {
		this.buCustomerWithAddressViewDAO = buCustomerWithAddressViewDAO;
	}

	public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
		this.soSalesOrderItemDAO = soSalesOrderItemDAO;
	}

	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}

	public void setAdCustServiceLineDAO(
			AdCustServiceLineDAO adCustServiceLineDAO) {
		this.adCustServiceLineDAO = adCustServiceLineDAO;
	}

	public void setAdCustServiceDAO(AdCustServiceDAO adCustServiceDAO) {
		this.adCustServiceDAO = adCustServiceDAO;
	}

	public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	public void setAdCategoryDAO(AdCategoryDAO adCategoryDAO) {
		this.adCategoryDAO = adCategoryDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public BuBrandService getBuBrandService() {
		return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
	}

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public static final String[] GRID_FIELD_NAMES = { "indexNo",
			"recordDate", "memo", "isDeleteRecord" ,"txtCount" ,  "lineId" };

	public static final String[] GRID_FIELD_VALUES = { "", "", "", "", "" ,"" };

	
	public static final int[] GRID_FIELD_TYPES = {

	 AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING , AjaxUtils.FIELD_TYPE_LONG };
	
	/**
	 * 客服單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderTypeCode",
			"orderNo", "requestDate", "requestCode", "description", "status",
			"createdByName", "customerLastName", "customerFristName",
			"itemBrand", "isClose", "headId" };
	/**
	 * 客服單 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE = {
			"", "", "", "", "", "", "", "", "", "", "", "" };

	/**
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception {
		log.info("executeInitial...");
		Map resultMap = new HashMap();
		Map multiList = new HashMap(0);

		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean,
				"brandCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean,
				"orderTypeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean,
				"formId");
		String empolyeeCode = (String) PropertyUtils.getProperty(otherBean,
				"loginEmployeeCode");
		Long formId = StringUtils.hasText(formIdString) ? Long
				.valueOf(formIdString) : null;
		try {
			BuOrderType buOrderType = buOrderTypeService
					.findById(new BuOrderTypeId(brandCode, orderTypeCode));
			// List<ImWarehouse> allWarehouse =
			// imWarehouseService.getWarehouseByWarehouseEmployee(brandCode,empolyeeCode,
			// null);
			// List<BuShopMachine> allShopMachines =
			// buBasicDataService.getShopMachineForEmployee(brandCode,
			// empolyeeCode, "Y");
			// List<BuShopMachine> allShopMachines = baseDAO.findByProperty(
			// "BuShopMachine", new String[] { "enable" },new Object[] {"Y"} );
			List<AdCategory> alldiscount = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"DISCOUNT", "Y" }, "displaySort");
			List<AdCategory> alltype = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"TYPE", "Y" }, "displaySort");
			List<AdCategory> allpaymentType = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "PAYMENT", "Y" }, "displaySort");
			List<AdCategory> allvipType = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"VIP_TYPE", "Y" }, "displaySort");
			List<AdCategory> allmainTainExpense = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "FIX_EXPENSE", "Y" }, "displaySort");
			List<AdCategory> allnationality = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "NATIONALITY", "Y" }, "displaySort");
			List<AdCategory> allcustomerSex = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "SEX", "Y" }, "displaySort");
			List<AdCategory> allmaintainGiven = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "FIX_GIVEN", "Y" }, "displaySort");
			List<AdCategory> allmaintainReceive = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "FIX_RECEIVE", "Y" }, "displaySort");
			// List<AdCategory> allexceptional = baseDAO.findByProperty(
			// "AdCategory" , new String[] { "groupNo","enable" },new Object[]
			// {"IS_EXCEPTIONAL" ,"Y"} ,"displaySort");
			List<AdCategory> allcustomerRequest = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "COMPLAINT_CASES", "Y" }, "displaySort");
			// List<AdCategory> allrefound = baseDAO.findByProperty(
			// "AdCategory" , new String[] { "groupNo","enable" },new Object[]
			// {"REFUND_AMOUNT" ,"Y"} ,"displaySort");
			List<AdCategory> allclosed = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"CLOSED_MODE", "Y" }, "displaySort");
			List<AdCategory> allrequestSource = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "REQUEST_SOURCE", "Y" }, "displaySort");
			List<AdCategory> allCategroyTypes = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "SERVICE_TYPE", "Y" }, "displaySort");
			List<AdCategory> allproject = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"SERVICE_REASON", "Y" }, "displaySort");
			List<AdCategory> allsystem = baseDAO.findByProperty("AdCategory",
					new String[] { "groupNo", "enable" }, new Object[] {
							"ALERT_LEVEL", "Y" }, "displaySort");
			List<AdCategory> alldepartment = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "SALES_ DEPARTMEN", "Y" }, "displaySort");
			List<AdCategory> allcategoryType = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "ITEM_CATAGORY", "Y" }, "displaySort");
			// multiList.put("allWarehouse" ,
			// AjaxUtils.produceSelectorData(allWarehouse,"warehouseCode",
			// "warehouseName", true, true));
			// multiList.put("allShopMachines" ,
			// AjaxUtils.produceSelectorData(allShopMachines, "shopCode",
			// "posMachineCode", true, true));
			// multiList.put("allShopMachines" ,
			// AjaxUtils.produceSelectorData(allShopMachines, "posMachineCode",
			// "posMachineCode", false, true));
			multiList.put("alldiscount", AjaxUtils.produceSelectorData(
					alldiscount, "classNo", "className", false, true));
			multiList.put("alltype", AjaxUtils.produceSelectorData(alltype,
					"classNo", "className", false, true));
			multiList.put("allpaymentType", AjaxUtils.produceSelectorData(
					allpaymentType, "classNo", "className", false, true));
			multiList.put("allvipType", AjaxUtils.produceSelectorData(
					allvipType, "classNo", "className", false, true));
			multiList.put("allmainTainExpense", AjaxUtils.produceSelectorData(
					allmainTainExpense, "classNo", "className", false, true));
			multiList.put("allnationality", AjaxUtils.produceSelectorData(
					allnationality, "classNo", "className", false, true));
			multiList.put("allcustomerSex", AjaxUtils.produceSelectorData(
					allcustomerSex, "classNo", "className", false, true));
			multiList.put("allmaintainGiven", AjaxUtils.produceSelectorData(
					allmaintainGiven, "classNo", "className", false, true));
			multiList.put("allmaintainReceive", AjaxUtils.produceSelectorData(
					allmaintainReceive, "classNo", "className", false, true));
			// multiList.put("allexceptional" ,
			// AjaxUtils.produceSelectorData(allexceptional , "classNo" ,
			// "className", false, true));
			// multiList.put("allrefound" ,
			// AjaxUtils.produceSelectorData(allrefound , "classNo" ,
			// "className", false, true));
			multiList.put("allclosed", AjaxUtils.produceSelectorData(allclosed,
					"classNo", "className", false, true));
			multiList.put("allcustomerRequest", AjaxUtils.produceSelectorData(
					allcustomerRequest, "classNo", "className", false, true));
			multiList.put("allrequestSource", AjaxUtils.produceSelectorData(
					allrequestSource, "classNo", "className", false, true));
			multiList.put("allCategroyTypes", AjaxUtils.produceSelectorData(
					allCategroyTypes, "classNo", "className", false, true));
			multiList.put("allproject", AjaxUtils.produceSelectorData(
					allproject, "classNo", "className", false, true));
			multiList.put("allsystem", AjaxUtils.produceSelectorData(allsystem,
					"classNo", "className", false, true));
			multiList.put("alldepartment", AjaxUtils.produceSelectorData(
					alldepartment, "classNo", "className", false, true));
			multiList.put("allcategoryType", AjaxUtils.produceSelectorData(
					allcategoryType, "classNo", "className", false, true));
			BuBrand buBrand = buBrandService.findById(brandCode);
			AdCustServiceHead adHead = null;
			if (formId != null) {
				adHead = findById(formId);
			} else {
				adHead = createNewPoPurchaseHead(parameterMap, resultMap,
						buBrand, buOrderType);
			}
			resultMap.put("form", adHead);
			// resultMap.put("branchCode" , buBrand.getBranchCode());
			resultMap.put("createdByName", UserUtils
					.getUsernameByEmployeeCode(adHead.getCreatedBy()));
			resultMap.put("multiList", multiList);

		} catch (Exception ex) {
			log.error("客服初始化失敗，原因：" + ex.toString());
			throw new Exception("客服單初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}

	public Map executeSearchInitial(Map parameterMap) throws Exception {
		HashMap resultMap = new HashMap();
		Object otherBean = parameterMap.get("vatBeanOther");
		String employeeCode = (String) PropertyUtils.getProperty(otherBean,
				"loginEmployeeCode");
		try {
			Map multiList = new HashMap(0);
			List<AdCategory> alldepartment = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "SALES_ DEPARTMEN", "Y" }, "displaySort");
			List<AdCategory> allcustomerRequest = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "COMPLAINT_CASES", "Y" }, "displaySort");
			List<AdCategory> allcategoryType = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "ITEM_CATAGORY", "Y" }, "displaySort");
			List<AdCategory> allnationality = baseDAO.findByProperty(
					"AdCategory", new String[] { "groupNo", "enable" },
					new Object[] { "NATIONALITY", "Y" }, "displaySort");
			multiList.put("alldepartment", AjaxUtils.produceSelectorData(
					alldepartment, "classNo", "className", false, true));
			multiList.put("allcustomerRequest", AjaxUtils.produceSelectorData(
					allcustomerRequest, "classNo", "className", false, true));
			multiList.put("allcategoryType", AjaxUtils.produceSelectorData(
					allcategoryType, "classNo", "className", false, true));
			multiList.put("allnationality", AjaxUtils.produceSelectorData(
					allnationality, "classNo", "className", false, true));
			resultMap.put("multiList", multiList);
			BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
			String depart = buEmployee.getEmployeeDepartment();
			resultMap.put("department", depart);
			// resultMap.put("requestCode", employeeCode);
			resultMap.put("request", UserUtils
					.getUsernameByEmployeeCode(employeeCode));
			;
			return resultMap;
		} catch (Exception ex) {
			log.error("需求單查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("需求單查詢初始化失敗，原因：" + ex.toString());
		}
	}

	public AdCustServiceHead findById(Long headId) throws Exception {
		try {
			AdCustServiceHead ad = (AdCustServiceHead) adCustServiceDAO
					.findByPrimaryKey(AdCustServiceHead.class, headId);
			return ad;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢客服資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢客服資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	public AdCustServiceHead findByIdad(Long headId) throws Exception {
		try {
			AdCustServiceHead ad = (AdCustServiceHead) adCustServiceDAO
					.findByPrimaryKey(AdCustServiceHead.class, headId);
			return ad;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢客服資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢客服資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 產生一筆新的 buPurchase
	 * 
	 * @param argumentMap
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public AdCustServiceHead createNewPoPurchaseHead(Map parameterMap,
			Map resultMap, BuBrand buBrand, BuOrderType buOrderType)
			throws Exception {
		log.info("createNewPoPurchaseHead");
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean,
				"brandCode");
		String employeeCode = (String) PropertyUtils.getProperty(otherBean,
				"loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean,
				"orderTypeCode");
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart = buEmployee.getEmployeeDepartment();
		// log.info("loginDepartment==="+loginDepartment);
		try {
			AdCustServiceHead form = new AdCustServiceHead();
			form.setBrandCode(brandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setDepartment(depart);
			form.setCreatedByName(UserUtils
					.getUsernameByEmployeeCode(employeeCode));
			// form.setRequestCode( employeeCode);
			form.setCreatedBy(employeeCode);
			form.setLastUpdatedBy(employeeCode);
			form.setRequestDate(DateUtils.parseDate(DateUtils
					.format(new Date())));
			form.setCreationDate(DateUtils.parseDate(DateUtils
					.format(new Date())));
			form.setLastUpdatedDate(DateUtils.parseDate(DateUtils
					.format(new Date())));
			// form.setRequest(
			// UserUtils.getUsernameByEmployeeCode(employeeCode));
			form.setStatus(OrderStatus.SAVE);
			saveTmp(form);
			return form;
		} catch (Exception ex) {
			log.error("產生新需求單失敗，原因：" + ex.toString());
			throw new Exception("產生需求單發生錯誤！");
		}
	}

	/**
	 * save tmp
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(AdCustServiceHead saveObj) throws FormException,
			Exception {
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setLastUpdatedDate(new Date());
		saveObj.setCreationDate(new Date());
		adCustServiceDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * 驗證主檔
	 * 
	 * @param parameterMap
	 * @throws Exception
	 */
	public void validateHead(Map parameterMap) throws Exception {
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		String description = (String) PropertyUtils.getProperty(formBindBean,
				"description");
		String depManager = (String) PropertyUtils.getProperty(formBindBean,
				"depManager");
		// 驗證
		if (!StringUtils.hasText(description)) {
			throw new ValidationErrorException("請輸入案件描述");
		}
		if (!StringUtils.hasText(depManager)) {
			throw new ValidationErrorException("請輸入客服主管");
		}
	}

	public Map executeFindActualBuPurchase(Map parameterMap)
			throws FormException, Exception {
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		AdCustServiceHead adCust = (AdCustServiceHead) parameterMap
				.get("entityBean");
		// AdCustServiceHead adCust =
		// (AdCustServiceHead)parameterMap.get("entityBean");
		log.info("~~~adCust~~~" + adCust);
		log.info("formBindBean~~~~~~~~~~~~~~~" + formBindBean);
		try {
			String formIdString = (String) PropertyUtils.getProperty(
					formLinkBean, "headId");
			String autoMail = (String) PropertyUtils.getProperty(formBindBean,
					"autoMail");
			String saleOrderDate = (String) PropertyUtils.getProperty(
					formBindBean, "saleOrderDate");
			String categoryTypeOther = (String) PropertyUtils.getProperty(
					formBindBean, "categoryTypeOther");
			String customerLastName = (String) PropertyUtils.getProperty(
					formBindBean, "customerLastName");
			String title = (String) PropertyUtils.getProperty(formBindBean,
					"title");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils
					.getLong(formIdString) : null;
			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");
			String customerRequestOther = (String) PropertyUtils.getProperty(
					formBindBean, "customerRequestOther");
			String refoundOther = (String) PropertyUtils.getProperty(
					formBindBean, "refoundOther");
			String exceptionalOther = (String) PropertyUtils.getProperty(
					formBindBean, "exceptionalOther");
			String closedOther = (String) PropertyUtils.getProperty(
					formBindBean, "closedOther");
			String requestSourceOther = (String) PropertyUtils.getProperty(
					formBindBean, "requestSourceOther");		
			adCust = getActualBuPurchase(formLinkBean);
			// adCust = getActualAdCust(formLinkBean);
			if (adCust == null) {
				throw new NoSuchObjectException("查無請採驗單主鍵：" + formId + "的資料！");
			} else { // 取得正式的單號
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, adCust);
				this.setOrderNo(adCust);
			}
			adCust.setRequestCode(customerLastName + title);
			adCust.setSaleOrderDate(saleOrderDate);
			adCust.setAutoMail(autoMail);
			adCust.setCategoryTypeOther(categoryTypeOther);
			adCust.setLastUpdatedBy(loginEmployeeCode);
			adCust.setLastUpdatedDate(new Date());
			adCust.setCustomerRequestOther(customerRequestOther);
			adCust.setRefoundOther(refoundOther);
			adCust.setExceptionalOther(exceptionalOther);
			adCust.setClosedOther(closedOther);
			adCust.setRequestSourceOther(requestSourceOther);
			log.info("2222222222:" + adCust.getItemName());
			removeDetailItemCode(adCust);
			adCustServiceDAO.update(adCust);
			log.info("阿災:" + adCust.getSaleOrderDate());
			// adCust = !StringUtils.hasText(formIdString)?
			// this.executeNewBuGoalAchevement(): this.findById(formId);
			parameterMap.put("entityBean", adCust);
			return parameterMap;
		} catch (Exception e) {
			log.error("取得需求單主檔失敗,原因:" + e.toString());
			throw new Exception("取得需求單主檔失敗,原因:" + e.toString());
		}
	}

	private AdCustServiceHead getActualBuPurchase(Object bean)
			throws FormException, Exception {
		AdCustServiceHead adCust = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualMovement headId=" + id);
		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			adCust = findById(headId);
			if (adCust == null) {
				throw new NoSuchObjectException("查無需求單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + adCust.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的需求單主鍵為空值！");
		}
		return adCust;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 * 
	 * @param head
	 */
	private void setOrderNo(AdCustServiceHead head)
			throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		log.info("3.setOrderNo...original_order=" + orderNo);
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head
						.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得"
							+ head.getBrandCode() + "-"
							+ head.getOrderTypeCode() + "單號失敗！");
				else {
					head.setOrderNo(serialNo);
					log.info("the order no. is " + serialNo);
				}
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得"
						+ head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}

	/**
	 * 產生一筆 BuPurchase
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	public AdCustServiceHead executeNewBuGoalAchevement() throws Exception {
		AdCustServiceHead form = new AdCustServiceHead();
		form.setOrderTypeCode(null);
		form.setOrderNo(null);
		form.setBrandCode(null);
		form.setCreatedBy(null);
		form.setCreationDate(null);
		form.setDepManager(null);
		form.setRequestDate(null);
		form.setDescription(null);
		form.setStatus(null);
		form.setCreatedBy(null);
		form.setCreationDate(null);
		return form;
	}

	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateBuGoalAchevementBean(Map parameterMap)
			throws FormException, Exception {
		try {
			AdCustServiceHead adCust = null;
			adCust = (AdCustServiceHead) parameterMap.get("entityBean");
			parameterMap.put("entityBean", adCust);
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 客服單存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalAchevement(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Object otherBean = parameterMap.get("vatBeanOther");
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		String depManager = (String) PropertyUtils.getProperty(formBindBean,
				"depManager");
		String contactMobile = (String) PropertyUtils.getProperty(formBindBean,
				"contactMobile");
		String customerSex = (String) PropertyUtils.getProperty(formBindBean,
				"customerSex");
		String nationality = (String) PropertyUtils.getProperty(formBindBean,
				"nationality");
		String email = (String) PropertyUtils
				.getProperty(formBindBean, "email");
		String customerCode = (String) PropertyUtils.getProperty(formBindBean,
				"customerCode");
		String brandCode = (String) PropertyUtils.getProperty(formBindBean,
				"brandCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(formBindBean,
				"orderTypeCode");
		String status = (String) PropertyUtils.getProperty(formBindBean,
				"status");
		String autoMail = (String) PropertyUtils.getProperty(formBindBean,
				"autoMail");

		log.info("autoMail~~~~~" + autoMail);
		// Integer nationality =
		// NumberUtils.getInt((String)PropertyUtils.getProperty(formBindBean,
		// "nationality"));
		AdCustServiceHead adCust = (AdCustServiceHead) parameterMap
				.get("entityBean");
		log.info("oooooooooooooooooooooooooooooooooooo:"
				+ adCust.getSaleOrderDate());
		String formAction = (String) PropertyUtils.getProperty(otherBean,
				"formAction");// 前端傳來的參數
		// String formStatus = (String)
		// PropertyUtils.getProperty(otherBean,"formStatus");
		String beforeStatus = adCust.getStatus();
		String nextStatus = getStatus(formAction, adCust.getStatus());
		// 發信用
		String subject = null;
		String templateFileName = null;
		Map displayHteml = new HashMap();
		List mailAddress = new ArrayList();
		List attachFiles = new ArrayList();
		Map cidMap = new HashMap();
		String createBy = adCust.getCreatedBy();
		// 發信用
		try {
			//關閉發信功能 Steve 20170706
			/*
			if ("CSF".equals(orderTypeCode)) {
				getMailList(adCust, subject, templateFileName, displayHteml,
						mailAddress, attachFiles, cidMap, formAction, createBy,
						autoMail, depManager);
			}
            */
			// adCust.setStatus(nextStatus);
			adCustServiceDAO.update(adCust);
			resultMsg = "OrderTypeCode：" + adCust.getOrderTypeCode() + "  案號："
					+ adCust.getOrderNo() + "存檔成功！ 是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", adCust);
			resultMap.put("vatMessage", msgBox);
			return resultMap;
		} catch (Exception ex) {
			log.error("客服單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("客服單存檔失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 取得指定連動的類別下拉
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXCategory(Properties httpRequest)
			throws Exception {
		List list = new ArrayList();
		Properties properties = new Properties();
		try {
			String categoryGroup = httpRequest.getProperty("categoryGroup");
			AdCategory headIds = adCategoryDAO
					.findbyparentHeadId(categoryGroup);
			Long categoryGroupHeadId = headIds.getHeadID();
			Long categoryGroupPhId = headIds.getParentHeadId();
			log.info("headIds = " + categoryGroupHeadId);
			log.info("categoryGroup = " + categoryGroup);
			List<AdCategory> allproject = baseDAO.findByProperty("AdCategory",
					new String[] { "enable", "parentHeadId" }, new Object[] {
							"Y", categoryGroupHeadId }, "displaySort");
			List<AdCategory> allsystem = baseDAO.findByProperty("AdCategory",
					new String[] { "enable", "headID" }, new Object[] { "Y",
							categoryGroupPhId }, "displaySort");
			// List<AdCategory> alltype = baseDAO.findByProperty( "AdCategory" ,
			// new String[] { "enable" ,"parentHeadId"},new Object[]
			// {"Y",categoryGroupHeadId} ,"displaySort");
			allproject = AjaxUtils.produceSelectorData(allproject, "classNo",
					"className", false, true);
			allsystem = AjaxUtils.produceSelectorData(allsystem, "classNo",
					"className", false, false);
			// alltype = AjaxUtils.produceSelectorData(allproject,"classNo" ,
			// "className", false, true);
			properties.setProperty("allproject", AjaxUtils
					.parseSelectorData(allproject));
			properties.setProperty("allsystem", AjaxUtils
					.parseSelectorData(allsystem));
			// properties.setProperty("alltype",
			// AjaxUtils.parseSelectorData(alltype));
			list.add(properties);
		} catch (Exception e) {
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}

	/**
	 * 取得指定連動的類別下拉
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXDepCategory(Properties httpRequest)
			throws Exception {
		List list = new ArrayList();
		Properties properties = new Properties();
		try {
			String department = httpRequest.getProperty("department");
			log.info("department = " + department);
			AdCategory headIds = adCategoryDAO.findbyparentHeadId(department);
			Long categoryGroupHeadId = headIds.getHeadID();
			log.info("headIds = " + categoryGroupHeadId);
			// log.info("department = " + department);
			List<AdCategory> allcategoryType = baseDAO.findByProperty(
					"AdCategory", new String[] { "enable", "parentHeadId" },
					new Object[] { "Y", categoryGroupHeadId }, "displaySort");
			allcategoryType = AjaxUtils.produceSelectorData(allcategoryType,
					"classNo", "className", false, true);
			properties.setProperty("allcategoryType", AjaxUtils
					.parseSelectorData(allcategoryType));
			list.add(properties);
		} catch (Exception e) {
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}

	/**
	 * 取得指定連動的類別下拉
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXPayCategory(Properties httpRequest)
			throws Exception {
		List list = new ArrayList();
		Properties properties = new Properties();
		try {
			String paymentType = httpRequest.getProperty("paymentType");
			AdCategory headIds = adCategoryDAO.findbyparentHeadId(paymentType);
			Long categoryGroupHeadId = headIds.getHeadID();
			Long categoryGroupPhId = headIds.getParentHeadId();
			log.info("headIds = " + categoryGroupHeadId);
			log.info("categoryGroup = " + paymentType);
			List<AdCategory> alltype = baseDAO.findByProperty("AdCategory",
					new String[] { "enable", "parentHeadId" }, new Object[] {
							"Y", categoryGroupHeadId }, "displaySort");
			alltype = AjaxUtils.produceSelectorData(alltype, "classNo",
					"className", false, true);
			properties.setProperty("alltype", AjaxUtils
					.parseSelectorData(alltype));
			list.add(properties);
		} catch (Exception e) {
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}

	/**
	 * AJAX Load Page Data
	 * 
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, Exception {
		try {
			// 要顯示的HEAD_ID
			Long headId  = NumberUtils.getLong(httpRequest.getProperty("headId"));
			log.info("headId___" + headId);
			List<Properties> re = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			List adCustServices = adCustServiceLineDAO.findPageLine(headId,iSPage, iPSize);
			log.info("adCustServices.size" + adCustServices.size());
			if (adCustServices != null && adCustServices.size() > 0) {
				// 設定額外欄位
				// this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdCustServiceLine a = (AdCustServiceLine) adCustServices.get(0);
				Long firstIndex = a.getIndexNo();
				Long maxIndex = adCustServiceLineDAO.findPageLineMaxIndex(headId);
				log.info("IIIndex:" + firstIndex + "," + maxIndex);
				// 取得最後一筆 INDEX
				a.setTxtCount("0");
				re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES,GRID_FIELD_VALUES, adCustServices, gridDatas,firstIndex, maxIndex));
			} else {
				log.info("222222222222222222:"+httpRequest);
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_VALUES, gridDatas));
			}
			return re;
		} catch (Exception ex) {
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}
	}

	public List<Properties> updateAJAXPageLinesDataAdCust(Properties httpRequest)
			throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			log.info("gridData=" + gridData);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils
					.getLong(httpRequest.getProperty("headId"));
			// Long lineId =
			// NumberUtils.getLong(httpRequest.getProperty("lineId"));
			// log.info("lineId111122221"+lineId);
			List<Properties> upRecords = null;
			if (headId == null)
				throw new ValidationErrorException("傳入的明細主鍵為空值！");
			upRecords = AjaxUtils.getGridFieldValue(gridData,
					gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			log.info("upRecords======" + upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			if (OrderStatus.SAVE.equals(status)
					|| OrderStatus.VOID.equals(status)) {
				// BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				AdCustServiceHead adCust = adCustServiceDAO.findById(headId);
				// buPurchaseHead.setHeadId(headId);
				int indexNo = adCustServiceLineDAO.findPageLineMaxIndex(headId)
						.intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord" + upRecord + "upRecords.size:"
								+ upRecords.size());
						String memo = upRecord.getProperty("memo");
						Long lineId = NumberUtils.getLong(upRecord
								.getProperty("lineId"));
						
						log.info("memo:" + memo);
						log.info("lineId" + lineId);
						
						AdCustServiceLine adCustService = adCustServiceLineDAO
						.findItemByIdentification(adCust
								.getHeadId(), lineId);
						
						if(null!=adCust){
							
							if (StringUtils.hasText(memo)) {
								
								log.info("adCustService======" + adCustService);
								if (null != adCustService) {
									log.info("更新 = " + headId + " | " + headId);
									AjaxUtils.setPojoProperties(adCustService,
											upRecord, GRID_FIELD_NAMES,
											GRID_FIELD_TYPES);
									adCustServiceLineDAO.update(adCustService);
								} else {
									
									indexNo++;
									AdCustServiceLine line1 = new AdCustServiceLine();
									AjaxUtils.setPojoProperties(line1, upRecord,
											GRID_FIELD_NAMES, GRID_FIELD_TYPES);
									line1.setAdCustServiceHead(adCust);
									line1.setIndexNo(Long.valueOf(indexNo));
									// line1.setCategoryCode("MEMO");
									
									adCustServiceLineDAO.save(line1);
									log.info("line1=====" + line1);
								}
							}else{
//								log.info("4444444444444");
//								AjaxUtils.setPojoProperties(adCustService,
//										upRecord, GRID_FIELD_NAMES,
//										GRID_FIELD_TYPES);
//								adCustServiceLineDAO.update(adCustService);
							}
						}
					}
				}
				log.info("測試000");
				// deleteMarkLineForItem(adCust);
				log.info("測試111");
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}

	/**
	 * ajax 取得BUPURCHASE查詢的結果
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXBuAdCustSearchPageData(Properties httpRequest)
			throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
			// HashMap searchMap = getSearchMap(httpRequest);
			HashMap searchMap = new HashMap();

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String requestDate = httpRequest.getProperty("requestDate");
			String requestDate1 = httpRequest.getProperty("requestDate1");
			String orderNo = httpRequest.getProperty("orderNo");
			String createdBy = httpRequest.getProperty("createdBy");
			String status = httpRequest.getProperty("status");
			String requestCode = httpRequest.getProperty("requestCode");
			String itemBrand = httpRequest.getProperty("itemBrand");
			String customerLastName = httpRequest
					.getProperty("customerLastName");
			String customerFristName = httpRequest
					.getProperty("customerFristName");
			String createdByName = httpRequest.getProperty("createdByName");
			String categoryType = httpRequest.getProperty("categoryType");
			String department = httpRequest.getProperty("department");
			String saleOrderDate = httpRequest.getProperty("saleOrderDate");
			String warehuseCode = httpRequest.getProperty("warehuseCode");
			String customerRequest = httpRequest.getProperty("customerRequest");
			String exceptional = httpRequest.getProperty("exceptional");
			String nationality = httpRequest.getProperty("nationality");
			String itemCode = httpRequest.getProperty("itemCode");

			searchMap.put("brandCode", brandCode);
			searchMap.put("orderTypeCode", orderTypeCode);
			searchMap.put("requestDate", requestDate);
			searchMap.put("requestDate1", requestDate1);
			searchMap.put("orderNo", orderNo);
			searchMap.put("createdBy", createdBy);
			searchMap.put("status", status);
			searchMap.put("requestCode", requestCode);
			searchMap.put("itemBrand", itemBrand);
			searchMap.put("customerLastName", customerLastName);
			searchMap.put("customerFristName", customerFristName);
			searchMap.put("createdByName", createdByName);
			searchMap.put("department", department);
			searchMap.put("saleOrderDate", saleOrderDate);
			searchMap.put("categoryType", categoryType);
			searchMap.put("warehuseCode", warehuseCode);
			searchMap.put("customerRequest", customerRequest);
			searchMap.put("exceptional", exceptional);
			searchMap.put("nationality", nationality);
			searchMap.put("itemCode", itemCode);

			// ==============================================================
			List<AdCustServiceHead> puHeads = (List<AdCustServiceHead>) adCustServiceDAO
					.findPageLine(searchMap, iSPage, iPSize,
							AdCustServiceDAO.QUARY_TYPE_SELECT_RANGE).get(
							"form");
			// log.info("buPurchases.size"+ puHeads.size());
			if (puHeads != null && puHeads.size() > 0) {
				// 設定額外欄位
				// this.setBuPurchaseHeadLineOtherColumn(puHeads);
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) adCustServiceDAO.findPageLine(searchMap,
						-1, iPSize, AdCustServiceDAO.QUARY_TYPE_RECORD_COUNT)
						.get("recordCount"); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, puHeads,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}
	}

	public HashMap getSearchMap(Properties httpRequest) {
		HashMap searchMap = new HashMap();
		String orderTypeCode = httpRequest.getProperty("orderTypeCode");
		searchMap.put("brandCode", httpRequest.getProperty("brandCode"));
		searchMap
				.put("orderTypeCode", httpRequest.getProperty("orderTypeCode"));
		searchMap.put("status", httpRequest.getProperty("status"));
		searchMap.put("orderNo", httpRequest.getProperty("orderNo"));
		searchMap.put("requestCode", httpRequest.getProperty("requestCode"));
		searchMap.put("customerLastName", httpRequest
				.getProperty("customerLastName"));
		searchMap.put("customerFristName", httpRequest
				.getProperty("customerFristName"));
		searchMap
				.put("createdByName", httpRequest.getProperty("createdByName"));
		searchMap.put("itemBrand", httpRequest.getProperty("itemBrand"));
		return searchMap;
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
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			if (result.size() > 0)
				pickerResult.put("result", result);
			else {
				Map messageMap = new HashMap();
				messageMap.put("type", "ALERT");
				messageMap.put("message", "請選擇檢視項目！");
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
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
	 * 將buPurchase主檔查詢結果存檔
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	// 6/26-------填adcust代號帶出資料
	/**
	 * 處理AJAX 客服 / 利用銷貨單號號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByAdCust(Properties httpRequest)
			throws Exception {
		log.info("getFormDataBySupplier");
		HashMap findObjs = new HashMap();
		Properties pro = new Properties();
		List re = new ArrayList();
		// Long addressBookId =
		// NumberUtils.getLong(httpRequest.getProperty("addressBookId"));
		String customerPoNo = httpRequest.getProperty("customerPoNo");
		String brandCode = httpRequest.getProperty("brandCode");
		log.info("customerPoNo~~~" + customerPoNo);
		log.info("brandCode~~~" + brandCode);
		findObjs.put("customerPoNo", customerPoNo);

		if (StringUtils.hasText(customerPoNo)) {
			List<SoSalesOrderHead> soSalesOrder = soSalesOrderHeadDAO
					.findByCustomerPoNo(brandCode, customerPoNo);
			for (SoSalesOrderHead soSalesOrders : soSalesOrder) {
				SoSalesOrderItem soSalesOrderItem = soSalesOrderItemDAO
						.findByHead(soSalesOrders.getHeadId());
				// String cdate =
				// DateUtils.format(soSalesOrders.getSalesOrderDate(),
				// "yyyy/MM/dd");
				if (null != soSalesOrder) {
					pro.setProperty("salesOrderDate", AjaxUtils
							.getPropertiesValue(soSalesOrders
									.getSalesOrderDate(), ""));
					pro.setProperty("posMachineCode", AjaxUtils
							.getPropertiesValue(soSalesOrders
									.getPosMachineCode(), ""));
					pro.setProperty("superintendentCode", AjaxUtils
							.getPropertiesValue(soSalesOrders
									.getSuperintendentCode(), ""));
					pro.setProperty("warehuseCode", AjaxUtils
							.getPropertiesValue(soSalesOrderItem
									.getWarehouseCode(), ""));

				} else {
					pro.setProperty("salesOrderDate", "");
					pro.setProperty("posMachineCode", "");
					pro.setProperty("superintendentCode", "");
					pro.setProperty("warehuseCode", "");
				}
			}
		} else {
			System.out.print("無銷售資料");
		}
		re.add(pro);
		return re;
	}

	// 6/26-------填adcust代號帶出資料
	/**
	 * 處理AJAX 客服 / 利用銷貨單號號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByItemCode(Properties httpRequest)
			throws Exception {
		log.info("getFormDataBySupplier");
		HashMap findObjs = new HashMap();
		Properties pro = new Properties();
		List re = new ArrayList();
		String customerPoNo = httpRequest.getProperty("customerPoNo");
		String itemCode = httpRequest.getProperty("itemCode");
		String brandCode = httpRequest.getProperty("brandCode");
		log.info("itemCode~~~" + itemCode);
		log.info("brandCode~~~" + brandCode);
		log.info("customerPoNo~~~" + customerPoNo);
		findObjs.put("customerPoNo", customerPoNo);
		findObjs.put("itemCode", itemCode);

		if (StringUtils.hasText(itemCode)) {
			List<SoSalesOrderHead> soSalesOrder = soSalesOrderHeadDAO
					.findByCustomerPoNo(brandCode, customerPoNo);
			log.info("~~soSalesOrder~~" + soSalesOrder.size());
			log.info("5555555555555555");
			if (0 != soSalesOrder.size()) {
				for (SoSalesOrderHead soSalesOrders : soSalesOrder) {
					log.info("~~~~~~~~~~~~~~~~~~~" + soSalesOrders.getHeadId());
					SoSalesOrderItem soSalesOrderItem = soSalesOrderItemDAO
							.findByHeadWithitemCode(soSalesOrders.getHeadId(),
									itemCode);
					log.info("soSalesOrderItem~~~" + soSalesOrderItem);
					log.info("soSalesOrderItem.getItemCode()~~~"
							+ soSalesOrderItem.getItemCode());

					// SoSalesOrderItem soItemCode =
					// soSalesOrderItemDAO.findItem(soSalesOrderItem.getItemCode());

					ImItem imItem = imItemDAO.findItem(brandCode,
							soSalesOrderItem.getItemCode());
					log.info("soSalesOrder.getItemCode()~~~"
							+ soSalesOrderItem.getItemCode());

					if (null != soSalesOrder) {
						pro.setProperty("itemName", AjaxUtils
								.getPropertiesValue(imItem.getItemCName(), ""));
						pro.setProperty("itemBrand", AjaxUtils
								.getPropertiesValue(imItem.getItemBrand(), ""));
						pro.setProperty("monetaryAmount",
								AjaxUtils
										.getPropertiesValue(soSalesOrderItem
												.getActualUnitPrice(),
												"monetaryAmount"));
						log.info("soSalesOrder.getItemCode()~~~"
								+ soSalesOrderItem.getActualUnitPrice());
					}
					// else {
					// pro.setProperty("itemName", "");
					// pro.setProperty("itemBrand", "");
					// pro.setProperty("monetaryAmount", "");
					// }
				}
			} else if (soSalesOrder.size() == 0) {
				log.info("66666666666666666");
				ImItem imItem = imItemDAO.findItem(brandCode, itemCode);
				pro.setProperty("itemName", AjaxUtils.getPropertiesValue(imItem
						.getItemCName(), ""));
				pro.setProperty("itemBrand", AjaxUtils.getPropertiesValue(
						imItem.getItemBrand(), ""));
				log.info("77777777777777777");
			}
		} else {
			System.out.print("無商品資料");
		}

		re.add(pro);
		return re;
	}

	// 6/26-------填adcust代號帶出資料
	/**
	 * 處理AJAX 客服 / 利用銷貨單號號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByCustomCode(Properties httpRequest)
			throws Exception {
		log.info("getFormDataBySupplier");
		// HashMap findObjs = new HashMap();
		Properties pro = new Properties();
		List re = new ArrayList();
		// Long addressBookId =
		// NumberUtils.getLong(httpRequest.getProperty("addressBookId"));
		String customerCode = httpRequest.getProperty("customerCode");
		String brandCode = httpRequest.getProperty("brandCode");
		log.info("customerCode~~~" + customerCode);
		log.info("brandCode~~~" + brandCode);
		// findObjs.put("customerCode", customerCode);

		if (StringUtils.hasText(customerCode)) {

			BuCustomerWithAddressView buCustomer = buCustomerWithAddressViewDAO
					.findEnableCustomer(brandCode, customerCode);
			log
					.info("~~buCustomer~~" + buCustomer.getTel1() + "   "
							+ buCustomer.getEMail() + " "
							+ buCustomer.getMobilePhone());

			if (null != buCustomer) {
				pro.setProperty("email", AjaxUtils.getPropertiesValue(
						buCustomer.getEMail(), ""));
				pro.setProperty("contactMobile", AjaxUtils.getPropertiesValue(
						buCustomer.getMobilePhone(), ""));
				pro.setProperty("contactHome", AjaxUtils.getPropertiesValue(
						buCustomer.getTel1(), ""));

			} else {
				pro.setProperty("email", "");
				pro.setProperty("contactMobile", "");
				pro.setProperty("contactHome", "");

			}
		} else {
			System.out.print("無客戶資料");
		}
		re.add(pro);
		return re;
	}

	/**
	 * 流程起始
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static Object[] startProcess(AdCustServiceHead form)
			throws Exception {
		log.info("startProcess");
		try {
			String packageId = "Ad_CustomerService";
			String processId = "process";
			String version = "20140703";
			String sourceReferenceType = "AdCustServiceHead(1)";
			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("orderTypeCode", form.getOrderTypeCode());
			context.put("formId", form.getHeadId());
			return null;//ProcessHandling.start(packageId, processId, version,
					//sourceReferenceType, context);
		} catch (Exception e) {
			log.error("單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}
	}

	// process 簽核
	public static Object[] completeAssignment(long assignmentId,
			boolean approveResult, AdCustServiceHead adCustServiceHead) throws Exception {
		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", adCustServiceHead);
			return null;//ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new Exception(e);
		}
	}

	// get下一個狀態
	private String getStatus(String formAction, String beforeStatus) {
		// TODO Auto-generated method stub
		// BuSupplierMod buSupplierMod = new BuSupplierMod();
		String status = null;
		// 送出 存 狀態
		if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
			if (beforeStatus.equals(OrderStatus.SAVE)) {
				status = OrderStatus.FINISH;
				/*
				 * }else if(beforeStatus.equals(OrderStatus.SIGNING)){ status =
				 * OrderStatus.FINISH;
				 */
			}
		} else if (OrderStatus.FORM_SAVE.equals(formAction)) {
			status = OrderStatus.SAVE;
		} else if (OrderStatus.FORM_VOID.equals(formAction)) {
			status = OrderStatus.VOID;
		}
		return status;
	}

	/**
	 * 關檔初始化
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executedoClose(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(
					otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean,
					"headId");
			// String no = (String) PropertyUtils.getProperty(otherBean, "no");
			Long formId = StringUtils.hasText(formIdString) ? Long
					.valueOf(formIdString) : null;
			AdCustServiceHead form = adCustServiceDAO.findById(formId);
			// BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
			String status = form.getStatus();
			String createdBy = form.getCreatedBy();
			resultMap.put("status", status);
			resultMap.put("createdBy", createdBy);
			resultMap.put("createdByName", UserUtils
					.getUsernameByEmployeeCode(form.getCreatedBy()));

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
	 * 轉派更新
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public HashMap updatedoClose(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,
				"loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(
				otherBean, "loginEmployeeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean,
				"headId");
		String status = (String) PropertyUtils.getProperty(otherBean, "status");

		// String taskInchargeName = (String)
		// PropertyUtils.getProperty(otherBean, "taskInchargeName");
		Long formId = StringUtils.hasText(formIdString) ? Long
				.valueOf(formIdString) : null;
		log.info("1234556");
		AdCustServiceHead form = adCustServiceDAO.findById(formId);
		// BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
		log.info("123455678");
		HashMap resultMap = new HashMap(0);
		log.info("1234556789");
		try {
			if (StringUtils.hasText(status)) {
				log.info("12345567890");
				form.setStatus("CLOSE");
				log.info("1234556789011");
				// form.setPriority(priority);
				// form.setRqInChargeCode(rqInChargeCode);
				// form.setOtherGroup(otherGroup);
				// forms.setSpecInfo(specInfo);
				// forms.setTaskInchargeName(taskInchargeName);
				// form.setLastUpdatedBy(loginEmployeeCode);
				// form.setLastUpdateDate(new Date());
			}
			adCustServiceDAO.update(form);
			return resultMap;
		} catch (Exception ex) {
			log.error("需求維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("需求維護單單存檔失敗，原因：" + ex.toString());
		}
	}

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
	public void getAutoMailList(AdCustServiceHead head, String subject,
			String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap, String action, String createBy)
			throws Exception {
		log.info("getAccessMailList:" + action);
		try {

			String orderTypeCode = "IRQ";
			// String itemCategory =
			// AjaxUtils.getPropertiesValue(head.getItemCategory(), "");
			StringBuffer html = new StringBuffer();
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();

			// 取得發信種類名稱-權限
			String itemCategory = "KweIRQ";
			/*
			 * ImItemCategory imItemCategory =
			 * imItemCategoryService.findById(brandCode,
			 * ImItemCategoryDAO.ITEM_CATEGORY, itemCategory); if (null ==
			 * imItemCategory) { categoryName = ""; } else { categoryName =
			 * imItemCategory.getCategoryName().concat("-"); }
			 */
			// #1 取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO
					.findOneBuCommonPhraseLine("MailList" + "CSF", "CSF"); // +orderTypeCode

			// itemCategory
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("Henry_lee@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory
						+ "無配置檔異常 ";
				description = "";
			} else {
				if (action.equals("SAVE")) {
					String accAdmin = buCommonPhraseLine.getAttribute4();
					if (StringUtils.hasText(accAdmin)) {
						log.info("testNMailList~~~");
						html.append("需求單：IRQ" + head.getOrderNo() + "資訊需求");
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
								.findByPrimaryKey(BuEmployee.class, createBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								mailAddress.add("Henry_lee@tasameng.com.tw");
							} else {
								emailError
										.append("查審核人工號：")
										.append(accAdmin)
										.append(
												" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查審核人工號：").append(accAdmin)
									.append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				} else if (action.equals("SUBMIT")) {
					html.append("您建立一張需求單，單號為：IRQ" + head.getOrderNo()
							+ "我們已收到您的需求單，會盡快幫您處理，謝謝，" + "處理人員為："
							+ head.getCreatedByName());
					String createdBy = head.getCreatedBy();
					if (StringUtils.hasText(createdBy)) { // 起單人
						BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
								.findByPrimaryKey(BuEmployee.class, createdBy);
						if (null != buEmployee) {
							String EMailCompany = buEmployee.getEMailCompany() == null ? ""
									: buEmployee.getEMailCompany();
							if (null != EMailCompany) {
								if (!mailAddress.contains(EMailCompany)) // 判斷是否有重複的email
									mailAddress.add(EMailCompany);
								// mailAddress.add("IT-Manager-DG@tasameng.com.tw");
							} else {
								emailError
										.append("查起單人工號：")
										.append(createdBy)
										.append(
												" 無信箱設定，請聯絡資訊部人員與通知此人權限調整通知<br>");
							}
						} else {
							emailError.append("查起單人無工號：").append(createdBy)
									.append(" ，請聯絡資訊部人員與通知此人權限調整通知<br>");
						}
					}
				} else {
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
			System.out.println("emailError.toString() = "
					+ emailError.toString());
			if (StringUtils.hasText(emailError.toString())) {
				// mailAddress.add("IT-Manager-DG@tasameng.com.tw");
				subjectError = "MailList" + orderTypeCode + itemCategory
						+ "無配置檔異常 ";
				MailUtils.sendMail(subjectError, templateFileName, display,
						mailAddress, attachFiles, cidMap);
			}
			System.out.println("subject = " + subject);
			System.out.println("html = " + html.toString());
			// 設定樣板的內容值
			display.put("display", html.toString());

			// 發信通知
			MailUtils.sendMail(subject, templateFileName, display, mailAddress,
					attachFiles, cidMap);
			log.info("getAccessMailList8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}
	}

	/**
	 * 取得寄信清單與相關資訊
	 * 
	 * @param subject
	 * @param templateFileName
	 * @param display
	 * @param mailAddress
	 * @param attachFiles
	 * @param cidMap
	 */
	public void getMailList(AdCustServiceHead head, String subject,
			String templateFileName, Map display, List mailAddress,
			List attachFiles, Map cidMap, String action, String createBy,
			String autoMail, String depManager) throws Exception {
		log.info("babugetMailList");
		Object formBindBean = cidMap.get("vatBeanFormBind");
		// #1 取得寄件者與其他相關配置檔
		// #2 取得CC報表的URL
		// #3 用URL轉成PDF再轉jpg
		// #4 用URL轉成xls
		// #5 寄信
		try {
			String reportFileName = null;
			String brandCode = head.getBrandCode();
			String orderTypeCode = head.getOrderTypeCode();
			log.info("autoMail~~~~~" + autoMail);
			String categorySystem = AjaxUtils.getPropertiesValue(head
					.getCategorySystem(), "");
			String itemCategory = AjaxUtils.getPropertiesValue(head
					.getItemCategory(), "");
			String subjectError = null;
			String description = null;
			StringBuffer emailError = new StringBuffer();
			String encryText = new String("");
			String permissionInfo = new String("");
			permissionInfo = brandCode + "@@" + "T17888" + "@@" + "CSF" + "@@";
			encryText = new DES().encrypt(permissionInfo
					+ String.valueOf(new Date().getTime()));
			// 取得業種子類名稱
			String categoryName = null;

			ImItemCategory imItemCategory = imItemCategoryService.findById(
					brandCode, ImItemCategoryDAO.ITEM_CATEGORY, itemCategory);
			if (null == imItemCategory) {
				categoryName = "";
			} else {
				categoryName = imItemCategory.getCategoryName().concat("-");
			}

			// #1 取得配置檔得到 寄信的報表與
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO
					.findOneBuCommonPhraseLine("MailList" + "CSF", "CSF"); // +orderTypeCode
																			// itemCategory
			log.info("~~~~" + buCommonPhraseLine);
			if (null == buCommonPhraseLine) {
				// 寄給故障維謢人員
				mailAddress.add("henry_lee@tasameng.com.tw");
				reportFileName = "crm.rpt";
				subjectError = "MailList" + orderTypeCode + "CSF" + "無配置檔異常 ";
				description = "";
			} else {
				reportFileName = buCommonPhraseLine.getAttribute1();
				autoMail.trim();
				log.info("ccccccccccccccccc:" + autoMail);
				if (!(autoMail == "") && !autoMail.equals("")) {
					log.info("dddddddddddddddddddddddd");
					mailAddress.add(autoMail + "@tasameng.com.tw");
				}

				// 1.該單起單人、採助、採購人、採購主管 從 BU_EMPLOYEE 取得
				// String domain = "@tasameng.com.tw";
				String createdBy = head.getCreatedBy();

				// 促銷單不需此參數

				if (StringUtils.hasText(createdBy)) { // 起單人
					BuEmployee buEmployee = (BuEmployee) buEmployeeDAO
							.findByPrimaryKey(BuEmployee.class, createdBy);
					if (null != buEmployee) {
						String EMailCompany = buEmployee.getEMailCompany();
						if (null != EMailCompany) {

						} else {
							// emailError.append("查起單人工號：").append(createdBy).append("
							// 無信箱設定，請聯絡資訊部人員與通知此人促銷通知<br>");
						}
					} else {
						// emailError.append("查起單人無工號：").append(createdBy).append("
						// ，請聯絡資訊部人員與通知此人促銷通知<br>");
					}
				}
				// 2.營業 從 BU_COMMON_PHRASE_LINE 取得
				// 修改為寄給多個單位，以分號分隔email地址 by Weichun 2011.09.15
				String[] attribute2 = buCommonPhraseLine.getAttribute2().split(
						";");
				for (int i = 0; i < attribute2.length; i++) {
					if (StringUtils.hasText(attribute2[i])) {
						// System.out.println("通知email ==> " + attribute2[i]);
						mailAddress.add(attribute2[i]);

					}
					System.out.println("BaubmailAddress:" + mailAddress);
				}

				// 3.商品異動_副本 從 BU_COMMON_PHRASE_LINE 取得
				String attribute3 = buCommonPhraseLine.getAttribute3();
				if (StringUtils.hasText(attribute3)) {
					mailAddress.add(attribute3);

				}

				// 前半部主旨
				description = head.getCreatedByName() + "_" + head.getOrderNo()
						+ "_" + head.getCustomerFristName()
						+ head.getCustomerLastName();

			}

			description = head.getCreatedByName() + "_"
					+ head.getCustomerLastName() + head.getCustomerFristName();
			log.info("tttt");
			subject = "客服單通知_".concat(head.getOrderTypeCode()).concat(
					head.getOrderNo());
			if (StringUtils.hasText(subjectError)) {
				subject = subjectError + subject;
			}

			String orderNo = head.getOrderNo();
			String itemCode = head.getItemCode();
			String fileName = orderTypeCode.concat(orderNo);
			StringBuffer html = new StringBuffer();
			// String fileItem = orderTypeCode.concat(itemCode);
			// 設定範本
			templateFileName = "CommonTemplate.ftl";
			log.info("prompt0" + brandCode + "   prompt1" + orderTypeCode
					+ "    prompt2" + orderNo);
			// #2 取得cc連結
			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			parameters.put("prompt0", brandCode);
			parameters.put("prompt1", orderTypeCode);
			parameters.put("prompt2", orderNo);
			// parameters.put("prompt3", "");
			// parameters.put("prompt4", orderNo);
			// parameters.put("prompt5", orderNo);
			// String reportUrl = SystemConfig.getPureReportURL(brandCode,
			// orderTypeCode, head.getLastUpdatedBy(),reportFileName,
			// parameters);
			String reportUrl = "http://10.1.94.161:8080/crystal/t2/crm.rpt?crypto="
					+ encryText
					+ "&prompt1="
					+ orderTypeCode
					+ "&prompt2="
					+ orderNo + "&prompt0=" + brandCode;
			log.info("babureportUrl:" + reportUrl);
			// "http://10.1.99.161:8080/crystal/t2/SO0752.rpt?crypto=56557594c4cafe66e6f67ad5cebf7c2445a1727c95855b5469e88aea836ed3d62a509c34795a3452&prompt2=&prompt5=201104270004&prompt0=T2&prompt4=201104270004&prompt1=PAJ&prompt3=";

			List<File> png_files = new ArrayList<File>();

			PDDocument doc = null;
			try {
				// #3 取得pdf
				URL pdfUrl = new URL(reportUrl + "&init=pdf");
				URLConnection pdfConnection = pdfUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				pdfConnection.setRequestProperty("User-Agent", "");
				pdfConnection.setRequestProperty("accept-language", "");
				pdfConnection.setRequestProperty("cookie", "");
				pdfConnection.setDoOutput(true);

				BufferedInputStream pdfBis = new BufferedInputStream(
						pdfConnection.getInputStream());
				System.out.println("pdfBis = " + pdfBis);
				File pdfFile = File.createTempFile(fileName, ".pdf");
				FileOutputStream pdfFos = new FileOutputStream(pdfFile);
				byte[] data = new byte[1];
				while (pdfBis.read(data) != -1) {
					pdfFos.write(data);
				}
				pdfFos.flush();
				pdfFos.close();

				// #4 取得excel
				URL xlsUrl = new URL(reportUrl + "&init=rtf");
				URLConnection xlsConnection = xlsUrl.openConnection();

				// - 模擬client端的瀏覽器 避免被當作網頁機器人而被阻擋
				xlsConnection.setRequestProperty("User-Agent", "");
				xlsConnection.setRequestProperty("accept-language", "");
				// xlsConnection.setRequestProperty("cookie", "");
				xlsConnection.setDoOutput(true);

				BufferedInputStream xlsBis = new BufferedInputStream(
						xlsConnection.getInputStream());
				System.out.println("xlsBis = " + xlsBis);
				log.info("~~~~head.getCreatedByName()"
						+ head.getCreatedByName());
				String fName = head.getCreatedByName() + "_"
						+ head.getOrderNo() + "_" + head.getRequestCode() + "_";

				// String fName = "測試測";
				String attachFile = new String(fName.getBytes(), "ISO8859-1");
				File xlsFile = File.createTempFile(attachFile, ".rtf");
				FileOutputStream xlsFos = new FileOutputStream(xlsFile);
				byte[] xlsData = new byte[1];
				while (xlsBis.read(xlsData) != -1) {
					xlsFos.write(xlsData);
				}
				xlsFos.flush();
				xlsFos.close();

				attachFiles.add(xlsFile.getAbsolutePath());
				log.info("cccc");
				// pdf轉圖檔
				doc = PDDocument.load(pdfFile);
				log.info("ddd");
				List pages = doc.getDocumentCatalog().getAllPages();
				log.info("eee");
				for (int j = 0; j < pages.size(); j++) {
					log.info("fff");
					PDPage page = (PDPage) pages.get(j);
					log.info("ggg");
					BufferedImage bufferedImage = page.convertToImage();
					log.info("hhh");
					File imageFile = File.createTempFile(fileName + j, ".jpg"); // "new
																				// File("PA"+j+".png")
																				// File.createTempFile("PA"+j,".gif")
					log.info("iii");
					ImageIO.write(bufferedImage, "jpg", imageFile);
					log.info("jjj");
					png_files.add(imageFile);
					log.info("kkk");
					attachFiles.add(imageFile.getAbsolutePath());
					log.info("lll");
					cidMap.put(imageFile.getAbsolutePath(), png_files.get(j)
							.getName());
					log.info("mmm");
				}
				log.info("yyy");
				// StringBuffer html = new StringBuffer();
				System.out.println("emailError.toString() = "
						+ emailError.toString());
				if (StringUtils.hasText(emailError.toString())) {
					html.append(emailError.toString());
				}

				for (int i = 0; i < png_files.size(); i++) {
					html.append("<img src='cid:" + png_files.get(i).getName()
							+ "'/><br>");
				}
				System.out.println("html = " + html.toString());
				// 設定樣板的內容值
				display.put("display", html.toString());

				// #5
				MailUtils.sendMail(subject, templateFileName, display,
						mailAddress, attachFiles, cidMap);
				// 測試
				// List mailAddressList = new ArrayList();
				// mailAddressList.add("weichun_liao@tasameng.com.tw");
				// mailAddressList.add("MM-DG@tasameng.com.tw");
				// MailUtils.sendMail(subject, templateFileName, display,
				// mailAddressList, attachFiles, cidMap);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("取得寄件相關內容發生錯誤");
		}

	}

	public AdCustServiceHead removeDetailItemCode(AdCustServiceHead headObj) {
		log.info("進入removeDetailItemCode時間點" + new Date());
		System.out.println("移除空白的 imReceiveItem : checkDetailItemCode單別:"
				+ headObj.getOrderTypeCode() + " 單號:" + headObj.getOrderNo());
		List<AdCustServiceLine> items = adCustServiceDAO.getItems(headObj
				.getHeadId());
		List<AdCustServiceLine> isDelItems = new ArrayList();
		System.out.println("刪除前數量:" + items.size());
		for (AdCustServiceLine item : items) {
			if (item != null && "1".equals(item.getIsDeleteRecord())) {
				isDelItems.add(item);
			}
		}
		items.removeAll(isDelItems);
		System.out.println("刪除後數量:" + items.size());
		long indexNo = 1L;
		for (AdCustServiceLine item : items) {
			System.out.println("排序前index:" + item.getIndexNo());
			System.out.println("排序後index:" + indexNo);
			item.setIndexNo(indexNo++);
		}

		headObj.setAdCustServiceLines(items);
		return headObj;
	}
}
