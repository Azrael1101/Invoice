package tw.com.tm.erp.hbm.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Enumeration;
import java.sql.*; 
import javax.sql.DataSource;


import tw.com.tm.erp.exceptions.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.BuPurchaseAction;
import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import java.text.DateFormat;
import tw.com.tm.erp.exceptions.FormException;

import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;

import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;

import tw.com.tm.erp.hbm.bean.AdTaskReviewView;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.GnFile;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.ImSysLog;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.PrItem;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.bean.SiMenuCtrl;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
//import tw.com.tm.erp.hbm.bean.TampBuPurchaseLine;
import tw.com.tm.erp.hbm.dao.AdCategoryLineDAO;
import tw.com.tm.erp.hbm.dao.AdTaskReviewViewDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.DAOFactory;
import tw.com.tm.erp.hbm.dao.GnFileDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.PrItemDAO;
import tw.com.tm.erp.hbm.dao.TmpAjaxSearchDataDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
//Steve
import tw.com.tm.erp.hbm.bean.AdDetail;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.hbm.dao.AdDetailDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class BuPurchaseServiceSub extends BuPurchaseService{


	private static final Log log = LogFactory.getLog(BuPurchaseServiceSub.class);
	private BuPurchaseHeadDAO buPurchaseHeadDAO;
	private BuPurchaseLineDAO buPurchaseLineDAO;
	private BaseDAO baseDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	private BuPurchaseAction buPurchaseAction;
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
	//********lara memo-upload*********
	private GnFileDAO gnFileDAO;
	//********lara memo-upload*********
	//********Steve 取group以及groupMenu*********
	private AdDetailDAO adDetailDAO;
	private SiGroupMenuDAO groupMenuDAO;
	private SiUsersGroupDAO usersGroupDAO;
	private TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO;
	private SiMenuDAO simenuDAO;
	//********Steve 取group以及groupMenu*********
	private Date nextStartDay = null;
	private int appendNext = 0;
	
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
	public void ImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	public void setBuPurchaseLineDAO(BuPurchaseLineDAO buPurchaseLineDAO) {
		this.buPurchaseLineDAO = buPurchaseLineDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	//********lara memo-upload********
	public void setGnFileDAO(GnFileDAO gnFileDAO) {
		this.gnFileDAO = gnFileDAO;
	}
	//********lara memo-upload********
	//********Steve 取group以及groupMenu********
	public void setSiGroupMenuDAO(SiGroupMenuDAO groupMenuDAO) {
		this.groupMenuDAO = groupMenuDAO;
	}
	public void setSiUsersGroupDAO(SiUsersGroupDAO usersGroupDAO) {
		this.usersGroupDAO = usersGroupDAO;
	}
	public void setTmpAjaxSearchDataDAO(TmpAjaxSearchDataDAO tmpAjaxSearchDataDAO) {
        this.tmpAjaxSearchDataDAO = tmpAjaxSearchDataDAO;
    }              
	public void setAdDetailDAO(AdDetailDAO adDetailDAO) {
        this.adDetailDAO = adDetailDAO;
    }
	//********Steve 取group以及groupMenu********
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
	
	public void setSiMenuDAO(SiMenuDAO simenuDAO) {
		this.simenuDAO = simenuDAO;
	}

	//******lara memo-upload******
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
		"quantity","taxEnable","reUnitPrice", "reTotalAmount","purUnitPrice", "purTotalAmount","supplier","isDeleteRecord"
		,"shopCode","posMachineCode","suppilerCode","suppilerName","assignMenuDateStart","assignMenuTimeStart","supportNo"
		,"executeDateStart","executeTimeStart","executeDateEnd","executeTimeEnd","executeMemo","executeInCharge"
		,"taskType","finishDate","executeHours","executeTimeStart","status","taskInchargeCode","taskInchargeName"
	};
	public static final String[] GRID_FIELD_VALUES = { 
		"","","","","","", 
		"0","","0", "0","0", "0","",""
		,"","","","","","","","","","","","",""
		,"","","","",""
		,"",""
	};
	public static final int[] GRID_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT,AjaxUtils.FIELD_TYPE_INT, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
		//派工
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING
		//任務
		,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING
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
		"incharge","specInfo","status","mon","tue","wed","thu","fri","sat","sun","lineId"
	};
	/**
	 * 需求單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderTypeCode","orderNo","requestDate","department","request","contractTel","no","description","status","otherGroup","headId"
	};
	/**
	 * 工作回報 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_LINE_NAMES_VALUE = {
		"","","", "","","","","","","",""
	};
	/**
	 * 需求單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_A_FIELD_NAMES = { 
		"indexNo","enable","menuId","menuName","url","updateDate","brandCode","clickNumber","cost","categoryCode","warehouseCode","lineId"
	};
	
	public static final String[] GRID_SEARCH_WAREHOUSE_FIELD_NAMES = { 
		"indexNo","enable","warehouseCode","warehouseName","brandCode","lineId"
	};
	
	public static final int[] GRID_A_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};
	
	public static final int[] GRID_WAREHOUSE_FIELD_TYPES = { 
		//請採驗
		AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG
	};
	/**
	 * 工作回報 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_A_FIELD_LINE_NAMES_VALUE = {
		"","","","","","","","","","","","",""
	};
	
	public static final String[] GRID_SEARCH_WAREHOUSE_FIELD_LINE_NAMES_VALUE = {
		"","","","","",""
	};
	/**
	 * 需求單 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE = {
		"","","", "","","","","","","",""
	};
	/**
	 * 將buPurchase主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public List<Properties> saveSearchResult2(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_A_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	/**
	 * 將buPurchase主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult1(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_LINE_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	/**
	 * 產生一筆 BuPurchase
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
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
		return form;
	}
	/**save tmp
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(BuPurchaseHead saveObj) throws FormException, Exception {
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		buPurchaseHeadDAO.save(saveObj);
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
		String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		try{
			BuOrderType buOrderType = buOrderTypeService.findById( new BuOrderTypeId(brandCode, orderTypeCode) );
			List<AdCategoryHead> allCategroyTypes = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"GROUP","Y"} , "displaySort");	
			List<AdCategoryHead> allproject = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"ITEM","Y" },"displaySort");
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "deptNo","enable" },new Object[] {"103","Y" },"displaySort");
			List<BuCommonPhraseLine> alldepartment = baseDAO.findByProperty( "BuCommonPhraseLine", new String[] {  "id.buCommonPhraseHead.headCode", "enable" },new Object[] {"EmployeeDepartment","Y" }, "indexNo");
			List<BuPurchaseLine> isPartialShipments = baseDAO.findByProperty( "BuPurchaseLine", new String[] {  "taxEnable" },new Object[] {"Y" });
			List<BuShop> allshops = baseDAO.findByProperty( "BuShop", new String[] { "enable" },new Object[] {"Y"} );
			List<BuShopMachine> allmachine = baseDAO.findByProperty( "BuShopMachine", new String[] { "enable" },new Object[] {"Y"} );
			multiList.put("allmachine"			, AjaxUtils.produceSelectorData(allmachine, "shopCode", "posMachineCode", true, true));
			multiList.put("allshops"			, AjaxUtils.produceSelectorData(allshops, "shopCode", "shopCName", true, true));
			multiList.put("allCategroyTypes"	, AjaxUtils.produceSelectorData(allCategroyTypes, "groupNo", "groupName", false, true,"TASK_REQ"));
			multiList.put("allproject"			, AjaxUtils.produceSelectorData(allproject,"groupNo", "groupName", false, true));
			multiList.put("allsystem"			, AjaxUtils.produceSelectorData(allsystem,"classNo", "className", false, true));
			multiList.put("alldepartment"		, AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
			multiList.put("isPartialShipments"	, AjaxUtils.produceSelectorData(isPartialShipments,"lineId", "texEnable", false, false));
			BuBrand     buBrand     = buBrandService.findById( brandCode );
			BuPurchaseHead puHead = null;
			if(formId != null){
				puHead = findById(formId);
			}else{
				puHead = createNewPoPurchaseHead(parameterMap, resultMap, buBrand, buOrderType );
			}
			resultMap.put("form", puHead);
			resultMap.put("employeeCode",      employeeCode);        //為什麼要塞??
			//resultMap.put("status",            puHead.getStatus() );  //不是有了嗎???
			resultMap.put("branchCode",        buBrand.getBuBranch().getBranchCode());	// 2->T2     是否有用??
			resultMap.put("createdByName",     UserUtils.getUsernameByEmployeeCode(puHead.getCreatedBy()));
			resultMap.put("multiList",         multiList);
			resultMap.put("enable","Y");
		}catch(Exception ex){
			log.error("需求初始化失敗，原因：" + ex.toString());         //修正錯誤訊息
			throw new Exception("需求單初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
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
			resultMap.put("request",       UserUtils.getUsernameByEmployeeCode(employeeCode));
			//resultMap.put("createdByName"  ,UserUtils.getUsernameByEmployeeCode(employeeCode));
			//resultMap.put("createdBy"  ,       employeeCode);
			//resultMap.put("status"       ,  OrderStatus.SAVE);
			return resultMap;       	
		}catch (Exception ex) {
			log.error("需求單查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("需求單查詢初始化失敗，原因：" + ex.toString());
		}           
	}
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData......");
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
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			log.info("upRecords======"+upRecords);
			Map findObjs = new HashMap();
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
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
	}
	
	public List<Properties> updateAJAXPageLinesDataAdDetail(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesDataAdDetail....");
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
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_SEARCH_A_FIELD_NAMES);
			log.info("upRecords======"+upRecords);
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				buPurchaseHead.setHeadId(headId);
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String menuName = upRecord.getProperty("menuName");
						log.info("測試menuName是否為空值:"+menuName+" Url:"+upRecord.getProperty("url"));
						String url = upRecord.getProperty("url");
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						if (StringUtils.hasText(menuName)||StringUtils.hasText(url)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+upRecord);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								log.info( "前端Enable = " + upRecord.getProperty("enable") + " adDetail Enable:"+ adDetail.getEnable()  );
								if(!upRecord.getProperty("enable").equals(adDetail.getEnable())){
									adDetail.setUpdateDate(new Date());
								}
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_SEARCH_A_FIELD_NAMES, GRID_A_FIELD_TYPES);
								buPurchaseLineDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_SEARCH_A_FIELD_NAMES, GRID_A_FIELD_TYPES);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
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
	}
	
	public List<Properties> updateAJAXPageLinesDataWarehouse(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesDataWarehouse....");
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
			upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_SEARCH_WAREHOUSE_FIELD_NAMES);
			log.info("upRecords======"+upRecords);
			String status = httpRequest.getProperty("status");
			log.info("zzzz:"+status);
			if(OrderStatus.SAVE.equals(status) || OrderStatus.VOID.equals(status)){
				log.info("cccc");
				BuPurchaseHead buPurchaseHead = new BuPurchaseHead();
				buPurchaseHead.setHeadId(headId);
				int indexNo = adDetailDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						log.info("upRecord"+upRecord +"upRecords.size:"+upRecords.size());
						String warehouseCode = upRecord.getProperty("warehouseCode");
						
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						if (StringUtils.hasText(warehouseCode)){ 
							AdDetail adDetail = adDetailDAO.findItemByIdentification(buPurchaseHead.getHeadId(), lineId);
							log.info("buPurchaseLine======"+upRecord);
							if ( null != adDetail ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								log.info( "前端Enable = " + upRecord.getProperty("enable") + " adDetail Enable:"+ adDetail.getEnable()  );
								if(!upRecord.getProperty("enable").equals(adDetail.getEnable())){
									adDetail.setUpdateDate(new Date());
								}
								AjaxUtils.setPojoProperties(adDetail, upRecord, GRID_SEARCH_WAREHOUSE_FIELD_NAMES, GRID_WAREHOUSE_FIELD_TYPES);
								buPurchaseLineDAO.update(adDetail);
							}else{
								indexNo++;
								AdDetail line1 = new AdDetail(); 
								AjaxUtils.setPojoProperties(line1, upRecord, GRID_SEARCH_WAREHOUSE_FIELD_NAMES, GRID_WAREHOUSE_FIELD_TYPES);
								line1.setBuPurchaseHead(buPurchaseHead);
								line1.setIndexNo(Long.valueOf(indexNo));
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
	}
	
	/** 產生一筆新的 buPurchase 
	 * @param argumentMap
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public BuPurchaseHead createNewPoPurchaseHead(Map parameterMap, Map resultMap, BuBrand buBrand, BuOrderType buOrderType ) throws Exception {
		log.info("createNewPoPurchaseHead");
		Object otherBean      = parameterMap.get("vatBeanOther");
		String brandCode      = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
		String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode  = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String loginDepartment  = (String)PropertyUtils.getProperty(otherBean, "loginDepartment");
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart=buEmployee.getEmployeeDepartment();
		String exc=buEmployee.getExtension();
		log.info("loginDepartment==="+loginDepartment);
		try{
			BuPurchaseHead form = new BuPurchaseHead();

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
			//if(loginDepartment=="103")
			form.setStatus(               OrderStatus.SAVE); 

			saveTmp(form);
			return form;
		}catch (Exception ex) {
			log.error("產生新需求單失敗，原因：" + ex.toString());
			throw new Exception("產生需求單發生錯誤！");
		}
	}
	public BuPurchaseHead executeFindActualBuPurchase(Map parameterMap)
	throws FormException, Exception {
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean    = parameterMap.get("vatBeanOther");
		BuPurchaseHead buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");
		try {
			String formIdString =(String) PropertyUtils.getProperty(formLinkBean, "headId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			String loginEmployeeCode = (String) PropertyUtils.getProperty( otherBean, "loginEmployeeCode");
			buPurchase = getActualBuPurchase(formLinkBean);
			if(buPurchase==null){
				throw new NoSuchObjectException("查無請採驗單主鍵：" + formId + "的資料！");
			} else { // 取得正式的單號
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buPurchase);
				this.setOrderNo(buPurchase);
			}
			buPurchase.setLastUpdatedBy(loginEmployeeCode);
			buPurchase.setLastUpdateDate(new Date());
			buPurchaseHeadDAO.update(buPurchase);
			buPurchase = !StringUtils.hasText(formIdString)? this.executeNewBuGoalAchevement(): this.findById(formId) ;
			parameterMap.put( "entityBean", buPurchase);
			return buPurchase;
		} catch (Exception e) {
			log.error("取得需求單主檔失敗,原因:"+e.toString());
			throw new Exception("取得需求單主檔失敗,原因:"+e.toString());
		}
	}
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
	}
	public BuPurchaseHead findById(Long headId) throws Exception {
		try {
			BuPurchaseHead pur = (BuPurchaseHead) buPurchaseHeadDAO.findByPrimaryKey(
					BuPurchaseHead.class, headId);
			return pur;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢地點資料時發生錯誤，原因：" + ex.toString()); //修正錯誤訊息
			throw new Exception("依據主鍵：" + headId + "查詢地點資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
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
	}
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
	/**
	 * 需求單存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalAchevement(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg  = null;
		Date date = new Date();
		String tmp = null ;
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		BuPurchaseHead buPurchase = (BuPurchaseHead)parameterMap.get("entityBean");

		try {
			log.info("buPurchaseyy="+buPurchase.getOrderNo());
			tmp=(loginEmployeeCode+","+loginEmployeeCode+","+loginEmployeeCode+","+loginEmployeeCode);
			buPurchase.setLastUpdatedBy(loginEmployeeCode);
			buPurchase.setStatusLog(tmp);
			buPurchase.setStatus((String)PropertyUtils.getProperty(otherBean, "status"));
			buPurchase.setLastUpdateDate(date);
			removeAJAXLine(buPurchase);//刪除mark掉的Line
			buPurchaseHeadDAO.update(buPurchase);
			log.info("確認送單:"+buPurchase.getStatus()+" "+(String) PropertyUtils.getProperty(otherBean, "status").toString());
			if(buPurchase.getStatus()=="FINISH"){
				log.info("確認送單");
			}
			resultMsg = "OrderTypeCode：" + buPurchase.getOrderTypeCode() + "存檔成功！ 是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buPurchase);
			resultMap.put("vatMessage", msgBox);
			return resultMap;
		} catch (Exception ex) {
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
		log.info("service取資料");
		try{
			// 要顯示的HEAD_ID
			Long headId   = NumberUtils.getLong(httpRequest.getProperty("headId"));
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
	Long cnt=1L;
	public List<Properties> getAJAXPageDataAddetail(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
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
			log.info("getAJAXPageDataAddetail.headId:"+headId);
			log.info("getAJAXPageDataAddetail.httpRequest:"+httpRequest);
			//Steve
			List buPurchaseLines = buPurchaseLineDAO.findPageLineAdDetail(headId, iSPage, iPSize);
			//List buPurchaseLines = new ArrayList();
			/*
			for(int i=0;i<buPurchaseLines.size();i++){
				log.info("現在index:"+cnt);
				AdDetail b = (AdDetail)buPurchaseLines.get(i);
				b.setIndexNo(cnt);
				indexBulines.add(b);
				cnt++;
			}
			*/
			log.info("buPurchases.size"+ buPurchaseLines.size());	
			if (buPurchaseLines != null && buPurchaseLines.size() > 0) {
				
				for(int i=0;i<buPurchaseLines.size();i++){
					AdDetail ad = (AdDetail)buPurchaseLines.get(i);
					log.info("babu MenuId:"+ad.getMenuId());
					String mName = checkIfParent(ad.getMenuId());
					log.info("遞回目錄名稱:"+mName);
					SiMenuCtrl siMenu = simenuDAO.findById(ad.getMenuId());
					if(siMenu!=null){
						oriMenuName = siMenu.getName();
						log.info("原本目錄名稱:"+oriMenuName);
						if(siMenu.getParentMenuId()!=null||siMenu.getParentMenuId().toString()!=""){
							
						  try{	
							  log.info("123456");
							SiMenuCtrl siMenuPar = simenuDAO.findById(siMenu.getParentMenuId().toString());
							log.info("44444444");
							parMenuName = siMenuPar.getName();
							log.info("777777");
							ad.setMenuName("  "+parMenuName+" / "+oriMenuName);
							log.info("9999");
							parMenus.add(parMenuName);
						  }catch(Exception ex){
							  continue;
						  }
						}else{
							log.info("沒有此menu:"+ad.getMenuId());
							//continue;
						}
					}
					
				}
				
				log.info("b1");
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)buPurchaseLines.get(0);
				log.info("b2:"+headId);
				Long firstIndex = a.getIndexNo();
				log.info("b3:"+firstIndex);
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("b5:"+firstIndex);
				log.info("b4:"+maxIndex);
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
	
	public List<Properties> getAJAXPageDataWareHouse(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
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
			log.info("getAJAXPageDataWarehouse.headId:"+headId);
			log.info("getAJAXPageDataWarehouse.httpRequest:"+httpRequest);
			//Steve
			List buPurchaseLines = buPurchaseLineDAO.findPageLineWareHouse(headId, iSPage, iPSize);
			//List buPurchaseLines = new ArrayList();
			/*
			for(int i=0;i<buPurchaseLines.size();i++){
				log.info("現在index:"+cnt);
				AdDetail b = (AdDetail)buPurchaseLines.get(i);
				b.setIndexNo(cnt);
				indexBulines.add(b);
				cnt++;
			}
			*/
			log.info("getAJAXPageDataWarehouse.size"+ buPurchaseLines.size());	
			if (buPurchaseLines != null && buPurchaseLines.size() > 0) {
				
				for(int i=0;i<buPurchaseLines.size();i++){
					AdDetail ad = (AdDetail)buPurchaseLines.get(i);
					log.info("babu warehouseId:"+ad.getWarehouseCode());
					
					
					/*SiMenuCtrl siMenu = simenuDAO.findById(ad.getMenuId());
					if(siMenu!=null){
						oriMenuName = siMenu.getName();
						log.info("原本目錄名稱:"+oriMenuName);
						if(siMenu.getParentMenuId()!=null||siMenu.getParentMenuId().toString()!=""){
							
						  try{	
							  log.info("123456");
							SiMenuCtrl siMenuPar = simenuDAO.findById(siMenu.getParentMenuId().toString());
							log.info("44444444");
							parMenuName = siMenuPar.getName();
							log.info("777777");
							ad.setMenuName("  "+parMenuName+" / "+oriMenuName);
							log.info("9999");
							parMenus.add(parMenuName);
						  }catch(Exception ex){
							  continue;
						  }
						}else{
							log.info("沒有此menu:"+ad.getMenuId());
							//continue;
						}
					}*/
					
				}
				
				log.info("b1");
				// 設定額外欄位
				//this.setBuPurchaseLineOtherColumn(buPurchaseLines);
				// 取得第一筆 INDEX
				AdDetail a = (AdDetail)buPurchaseLines.get(0);
				log.info("b2:"+headId);
				Long firstIndex = a.getIndexNo();
				log.info("b3:"+firstIndex);
				Long maxIndex = adDetailDAO.findPageLineMaxIndex(headId);
				log.info("b5:"+firstIndex);
				log.info("b4:"+maxIndex);
				log.info("IIIndex:"+firstIndex+","+maxIndex);
				// 取得最後一筆 INDEX
				re.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_WAREHOUSE_FIELD_NAMES, GRID_SEARCH_WAREHOUSE_FIELD_LINE_NAMES_VALUE, buPurchaseLines, gridDatas, firstIndex, maxIndex));
			}else {
				log.info("跑Default:");
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_WAREHOUSE_FIELD_NAMES, GRID_SEARCH_WAREHOUSE_FIELD_LINE_NAMES_VALUE,  gridDatas));
			}
			return re;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	
	public String  checkIfParent(String menuId){
		log.info("進入checkIfParent:"+menuId);
		String parMenuName = "";
		SiMenuCtrl siMenu = simenuDAO.findById(menuId);
		if(siMenu.getParentMenuId().equals("00000000")){
			//parMenuName = "根目錄";
			log.info("根目錄");
		}else if(siMenu.getParentMenuId()!=null&&!siMenu.getParentMenuId().equals("00000000")){
			parMenuName = "非根目錄";
			log.info("非根目錄");
		}
		 log.info("parMenuName:"+parMenuName);	 
		 return parMenuName;
	}
	
	
	/**
	 * ajax 取得BUPURCHASE查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXBuGoalAchevementSearchPageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值=========================
			HashMap searchMap = getSearchMap(httpRequest);
			//==============================================================	    
			List<BuPurchaseHead> puHeads = (List<BuPurchaseHead>) buPurchaseHeadDAO.findPageLine(searchMap, 
					iSPage, iPSize, BuPurchaseHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			log.info("buPurchases.size"+ puHeads.size());	
			if (puHeads != null && puHeads.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseHeadLineOtherColumn(puHeads);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
				Long maxIndex = (Long)buPurchaseHeadDAO.findPageLine(searchMap, -1, iPSize, 
						BuPurchaseHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, puHeads, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, gridDatas));
			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	public HashMap getSearchMap(Properties httpRequest){
		HashMap searchMap = new HashMap();
		String startDate  = httpRequest.getProperty("startDate");
		String endDate    = httpRequest.getProperty("endDate");
		String notshowotherGroup    = httpRequest.getProperty("notshowotherGroup");
		String isshowotherGroup    = httpRequest.getProperty("isshowotherGroup");
		log.info("notshowotherGroup====="+notshowotherGroup);
		log.info("isshowotherGroup======"+isshowotherGroup);
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
		searchMap.put("notshowotherGroup",  notshowotherGroup);
		searchMap.put("isshowotherGroup",   isshowotherGroup);
		return searchMap;
	}
	/**
	 * 設定bupurchase額外欄位
	 * @param buGoalAchevements
	 */
	private void setBuPurchaseHeadLineOtherColumn(List<BuPurchaseHead> buPurchases){
		for (BuPurchaseHead buPurchase : buPurchases) {
			buPurchase.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buPurchase.getLastUpdatedBy()));
		}
	}
	private void setBuPurchaseLineOtherColumn(List<BuPurchaseLine> buPurchases){
		for (BuPurchaseLine buPurchase : buPurchases) {
			buPurchase.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buPurchase.getLastUpdatedBy()));
		}
	}
	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(BuPurchaseHead head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		log.info("3.setOrderNo...original_order=" + orderNo);
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
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	/**
	 * by headId 取得bean
	 */
	public BuPurchaseHead getBeanByHeadId(Long headId) throws Exception {
		try {
			BuPurchaseHead head = (BuPurchaseHead)buPurchaseHeadDAO.findByPrimaryKey(BuPurchaseHead.class,headId);
			return head;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
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
		log.info("getAJAXFormDataBySuperintendent");
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
			log.info("employeeCode="+createdBy);
			log.info("englishName="+null);
			log.info("chineseName="+null);
			log.info("employee========="+createdBy);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			log.info("employee=========11111==="+employees.size());
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
		log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String requestCode = httpRequest.getProperty("requestCode"); 
		HashMap findObjs = new HashMap();
		Properties inc = new Properties();	
		if(StringUtils.hasText(requestCode))
		{
			List re = new ArrayList();
			findObjs.put("employeeCode", requestCode);
			findObjs.put("englishName", null);
			findObjs.put("chineseName", null);
			findObjs.put("tel1", null);
			log.info("employeeCode="+requestCode);
			log.info("englishName="+null);
			log.info("chineseName="+null);
			log.info("employee========="+requestCode);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			log.info("employee=========11111==="+employees.size());
			if(employees.size()==0){
				findObjs.put("employeeCode", null);
				findObjs.put("englishName", requestCode);
				findObjs.put("chineseName", null);
				findObjs.put("tel1", null);
				//找EN
				employees =   buEmployeeWithAddressViewDAO.findByemployee(findObjs);
				if(employees.size()==0){
					findObjs.put("employeeCode", null);
					findObjs.put("chineseName", requestCode);
					findObjs.put("englishName", null);
					findObjs.put("tel1", null);
					//找姓名
					employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);

					if(employees.size()==0){
						findObjs.put("employeeCode", null);
						findObjs.put("chineseName", null);
						findObjs.put("englishName", null);
						findObjs.put("tel1", requestCode);
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
						}
					}
					else
					{
						employee = employees.get(0);
						inc.setProperty("requestCode", employee.getEmployeeCode());
						inc.setProperty("request", employee.getChineseName());
					}
				}
				else
				{
					employee = employees.get(0);
					inc.setProperty("requestCode", employee.getEmployeeCode());
					inc.setProperty("request", employee.getChineseName());
				}
			}
			else
			{
				employee = employees.get(0);
				inc.setProperty("requestCode", employee.getEmployeeCode());
				inc.setProperty("request",employee.getChineseName());
			}
			re.add(inc);
			return re;
		}else{
			List re1 = new ArrayList();
			inc.setProperty("requestCode", null);
			inc.setProperty("request", "unknow");
			re1.add(inc);
			return re1;
		}

	}
	/**處理AJAX 採購負責人 call by JS
	 * @param httpRequest
	 * @return
	 */
	public List<Properties> getAJAXFormDataByRequestCode(Properties httpRequest) {
		log.info("getAJAXFormDataBySuperintendent");
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
		log.info("getAJAXFormDataBySuperintendent");
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
			log.info("employeeCode="+depManager);
			log.info("englishName="+null);
			log.info("chineseName="+null);
			log.info("employee========="+depManager);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			log.info("employee=========11111==="+employees.size());
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
				inc.setProperty("depManager", employee.getEmployeeCode());
				inc.setProperty("depManagerName", employee.getChineseName());
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
		log.info("getAJAXFormDataBySuperintendent");
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
			log.info("employeeCode="+rqInChargeCode);
			log.info("englishName="+null);
			log.info("chineseName="+null);
			log.info("employee========="+rqInChargeCode);
			List<BuEmployeeWithAddressView> employees = null;
			BuEmployeeWithAddressView employee = null;
			//找工號
			employees =  buEmployeeWithAddressViewDAO.findByemployee(findObjs);
			//找EN
			//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			//找姓名
			//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
			log.info("employee=========11111"+employees.size());
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
		log.info("employeeCode="+rqInChargeCode);
		findObjs.put("chineseName", null);
		log.info("chineseName="+rqInChargeCode);
		findObjs.put("englishName", null);
		log.info("englishName="+null);
		log.info("employee========="+rqInChargeCode);
		BuEmployeeWithAddressView employee = null;

		//找工號
		employee = (BuEmployeeWithAddressView) getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		//找EN
		//List<BuEmployeeWithAddressView> englishname =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		//找姓名
		//List<BuEmployeeWithAddressView> chinesename =  getBuEmployeeWithAddressViewDAO().findByemployee(findObjs);
		log.info("employee=========11111"+employee);
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
		String fristDate  = httpRequest.getProperty("fristDate");
		String lastDate    = httpRequest.getProperty("lastDate");

		Date countingFDate = null;
		Date countingLDate = null;
		if(StringUtils.hasText(fristDate))
			countingFDate = DateUtils.parseDate("yyyy/MM/dd", fristDate);
		if(StringUtils.hasText(lastDate))
			countingLDate = DateUtils.parseDate("yyyy/MM/dd", lastDate);

		searchMap.put("fristDate",     		countingFDate);
		searchMap.put("lastDate",     		countingLDate);
		searchMap.put("status",        		httpRequest.getProperty("status"));
		searchMap.put("incharge",  			httpRequest.getProperty("incharge"));
		searchMap.put("specInfo",  			httpRequest.getProperty("specInfo"));

		return searchMap;
	}
	/**
	 * 流程起始
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */ 
	public static Object[] startProcess(BuPurchaseHead form) throws Exception {
		log.info("startProcess");
		try {
			String packageId = "Bu_Purchase";
			String processId = "process";
			String version = "20140310";
			String sourceReferenceType = "PurchaseHead (1)";
			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("formId",    form.getHeadId());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		} catch (Exception e) {
			log.error("單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}
	}
	//process 簽核
	public static Object[] completeAssignment(long assignmentId, boolean approveResult,BuPurchaseHead buPurchaseHead) throws Exception {
		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);
			context.put("form", buPurchaseHead);
			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception e) {
			log.error("完成任務時發生錯誤：" + e.getMessage());
			throw new Exception(e);
		}
	}
	/**
	 * 轉派更新
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public HashMap updateTask(Map parameterMap) throws Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
		String no = (String) PropertyUtils.getProperty(otherBean, "no");
		String rqInChargeCode = (String) PropertyUtils.getProperty(otherBean, "rqInChargeCode");
		String otherGroup = (String) PropertyUtils.getProperty(otherBean, "otherGroup");

		//String taskInchargeName = (String) PropertyUtils.getProperty(otherBean, "taskInchargeName");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		log.info("1234556");
		BuPurchaseHead form = buPurchaseHeadDAO.findById(formId);
		//BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
		log.info("123455678");
		HashMap resultMap = new HashMap(0);
		log.info("1234556789");
		try {
			form.setNo(no);
			form.setRqInChargeCode(rqInChargeCode);
			form.setOtherGroup(otherGroup);
			//forms.setSpecInfo(specInfo);
			//forms.setTaskInchargeName(taskInchargeName);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			buPurchaseHeadDAO.update(form);
			return resultMap;
		} catch (Exception ex) {
			log.error("需求維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("需求維護單單存檔失敗，原因：" + ex.toString());
		}
	}
	/**
	 * 轉派初始化
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */ 
	public List<Properties> executeTaskassign(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
			//String no = (String) PropertyUtils.getProperty(otherBean, "no");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			BuPurchaseHead form = buPurchaseHeadDAO.findById(formId);
			//BuPurchaseLine forms = buPurchaseLineDAO.findById1(formId);
			String no=form.getNo();
			String rqInChargeCode=form.getRqInChargeCode();
			String otherGroup=form.getOtherGroup();
			//String taskType=forms.getTaskType();
			resultMap.put("no", no);
			resultMap.put("rqInChargeCode", rqInChargeCode);
			resultMap.put("otherGroup", otherGroup);

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
	 * 將工作回報資料更新至bupurchasehead,bupurchaseline單主檔及明細檔(AJAX)
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map updateAJAXworkingcondition(Map parameterMap) throws FormException,
	Exception {
		HashMap resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = NumberUtils.getLong((String) PropertyUtils
					.getProperty(otherBean, "headId"));
			Long lineId = NumberUtils.getLong((String) PropertyUtils
					.getProperty(otherBean, "lineId"));
			BuPurchaseHead buPurchaseHead = findById(headId);
			// ====================取得條件資料======================
			BuPurchaseLine br = (BuPurchaseLine) buPurchaseLineDAO
			.findById("BuPurchaseLine", lineId);
			if (br == null) {
				br = new BuPurchaseLine();
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, br);
				Long maxIndex = buPurchaseLineDAO
				.findPageLineMaxIndex(headId);
				br.setIndexNo(maxIndex + 1);
				br.setBuPurchaseHead(buPurchaseHead);
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
	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitialworking(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		try{

			BuPurchaseHead buPurchaseHead = this.executeFindActualBuGoalAchevement(parameterMap);
			Map multiList = new HashMap(0);
			resultMap.put("form", buPurchaseHead);
			resultMap.put("multiList",multiList);
			return resultMap;

		}catch(Exception ex){
			log.error("達成率初始化失敗，原因：" + ex.toString());
			throw new Exception("達成率單初始化失敗，原因：" + ex.toString());

		}

	}
	public BuPurchaseHead executeFindActualBuGoalAchevement(Map parameterMap)
	throws FormException, Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuPurchaseHead buPurchaseHead = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean , "formId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			buPurchaseHead = !StringUtils.hasText(formIdString)? this.executeNewBuGoalAchevement(): this.findById(formId) ;
			parameterMap.put( "entityBean", buPurchaseHead);
			return buPurchaseHead;
		} catch (Exception e) {
			log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
	}   


	/**
	 * ajax取得附件
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
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
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String creatBy = httpRequest.getProperty("creatBy");
			String requestBy = httpRequest.getProperty("requestBy");


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
	}

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
	public List<Properties> getAJAXInChargeCode(Properties httpRequest) throws Exception {
		try{
			Properties pro = new Properties();
			//List re = new ArrayList();
			String categorySystem = httpRequest.getProperty("categorySystem");
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
	}

	/**帶出負責人
	 * @param headObj
	 */
	private String getInChargeCode(String inChargeCode) throws Exception{
		try{
			if (!StringUtils.hasText(inChargeCode)) {
				return "unknow";
			} else {
				//找工號 
				AdCategoryLine inCharge = adCategoryLineDAO.findbyInChargeCode(inChargeCode);
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
	}
	
	private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }
	
	public List<Properties> executeCopyOrigMenu(Properties httpRequest) throws Exception{
		log.info("executeCopyOrigMenu");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
    	PreparedStatement stmt = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rs = null;
    	ResultSet rs1 = null;
    	Long count = 0L;
    	Map resultMap = new HashMap(0);
		BuPurchaseHead buPurchaseHead = null;
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
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{
				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				
				  buBoss = (BuEmployee)buEmployeeDAO.findById(bossCode);	
				  buEmp = (BuEmployee)buEmployeeDAO.findById(employeeCode);
				  costCtrl = buEmp.getCostControl();
				  wareHouseCtrl = buEmp.getWarehouseControl();
			}
			
			
			
			//log.info("emp屬性:"+buBoss.getEmployeeRole());
			
			System.out.println("===========BEGIN============" + new Date());
    		conn = dataSource.getConnection();
    		
    		String sql = "select * from CONTROL.SI_MENU_TEST_VIEW WHERE EMPLOYEE_ROLE = ? and BRAND_CODE=?  ORDER BY MENU_ID";
    			         /*"select M.INDEX_NO , M.MENU_ID,A.TITLE,A.URL, M.ENABLE,S.COST_CONTROL , S.WAREHOUSE_CONTROL "+ 
                         " from CONTROL.SI_GROUP_MENU M,CONTROL.SI_MENU S,CONTROL.SI_MENU_ALL A "+
    		             " where M.MENU_ID = S.MENU_ID AND M.BRAND_CODE=? "+
                         " and M.GROUP_CODE=? and M.ENABLE = ? and M.MENU_ID = S.MENU_ID ORDER BY MENU_ID";*/
    		             
    		log.info("sql:"+sql+" BABU:"+buEmp.getEmployeeRole());
    		stmt = conn.prepareStatement(sql);
    		stmt.setString(1, buEmp.getEmployeeRole());
    		stmt.setString(2, brandCode);
    		
    		//stmt.setString(2, "TEST1");
    		//stmt.setString(3, "Y");
    		//log.info("sql塞參數ok:"+buBoss.getEmployeeRole());
    		rs = stmt.executeQuery();
    		//log.info("sql執行ok:"+headId);
    		
    			stmt1 = conn.prepareStatement(sql);
    			if(bossCode.equals("SUPER")){
    			   stmt1.setString(1,"SUPER");	
    			}else{
        		   stmt1.setString(1, buBoss.getEmployeeRole());
    			}
        		   stmt1.setString(2, brandCode);
        		   rs1 = stmt1.executeQuery();
    		
        	if (rs != null) {
        		while (rs.next()){
        			bossList.add(rs.getString("MENU_ID"));
        		}
        	}
        	//for(Object menuId:bossList){
    		//log.info("bossList:"+menuId.toString());
        	//}
    		if (rs1 != null) {
    			buPurchaseHead = this.findById(Long.parseLong(headId));
    			List<AdDetail> adDetails = buPurchaseHead.getAdDetails();
    			log.info("adDetails的數量:"+adDetails.size());
    			while (rs1.next()) {	
    				
    				
    				count++;
    				//log.info("adDetailDAO:"+count+" "+headId);
    				AdDetail ad = new AdDetail();
    				//log.info("初始化adTail:"+buPurchaseHead);
    				ad.setBrandCode(rs1.getString("BRAND_CODE"));
    				ad.setBrandName("T2");
    				ad.setCategoryCode("");
    				ad.setCategoryName("");
    				ad.setIndexNo(count);
    				ad.setClickNumber(rs1.getDouble("CLICK_NUMBER"));
    				//ad.setHeadId(Long.parseLong(headId));
    				ad.setCost(rs1.getString("COST_CONTROL"));
    				ad.setType("Menu");
    				ad.setUrl(rs1.getString("URL")==null?"":(rs1.getString("URL").toString()));
    				ad.setWarehouseCode("");
    				ad.setWarehouseName("");
    				ad.setMenuId(rs1.getString("MENU_ID"));
    				//log.info("ppppppppppppppp:"+rs1.getString("MENU_ID"));
    				ad.setMenuName(rs1.getString("NAME")==null?"":(rs1.getString("NAME").toString()));
    				
    				for(Object menuId:bossList){
    					if(rs1.getString("MENU_ID").equals(String.valueOf(menuId))){
    						log.info("設定Enable:Y");
    						ad.setEnable("Y");
    						break;
    					}else if(rs1.getString("URL")==null){
    						ad.setEnable("Y");
    						break;
    					}
    				}
    				
    				ad.setEmployeeCode(employeeCode);
    				//ad.setBuPurchaseHead(buPurchaseHead);
    				SiMenuCtrl simenuCtrl = simenuDAO.findById(rs1.getString("MENU_ID"));
    				//log.info("成本:"+simenuCtrl.getMenuId()+" "+simenuCtrl.getCostControl()+" "+costCtrl);
    				if(null!=simenuCtrl.getRptBrand()){
    					if(simenuCtrl.getRptBrand().equals("MIS")|simenuCtrl.getRptBrand().equals("ALL")){
    						
    					}else{
    						adDetails.add(ad);
    					}
    				}else{
    					adDetails.add(ad);
    				}
    				
    				log.info("塞值OK:"+ad.getBrandCode());
    				
    				//adDetailDAO.save(ad);
    				
    			  	
    			}
    			log.info("bbbbb adDetails:"+adDetails.size());
    			buPurchaseHead.setAdDetails(adDetails);
    			buPurchaseHeadDAO.update(buPurchaseHead);
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
	
	public List<Properties> executeCopyOriWarehouse(Properties httpRequest) throws Exception{
		log.info("executeCopyOriWarehouse");
		List<Properties> result = new ArrayList();
		SiUsersGroup userGroup = null;
		Connection conn = null;
    	PreparedStatement stmt = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rs = null;
    	ResultSet rs1 = null;
    	Long count = 0L;
    	Map resultMap = new HashMap(0);
		BuPurchaseHead buPurchaseHead = null;
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
			this.deleteAdDetail(headId);
			if(employeeCode == null){
				throw new ValidationErrorException("請填入申請人工號！");
			}else{
				//userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
				//buEmp = buEmployeeDAO.findById(employeeCode);
				
				  buBoss = (BuEmployee)buEmployeeDAO.findById(bossCode);	
				  buEmp = (BuEmployee)buEmployeeDAO.findById(employeeCode);
				  costCtrl = buEmp.getCostControl();
				  wareHouseCtrl = buEmp.getWarehouseControl();
			}
			
			
			
			//log.info("emp屬性:"+buBoss.getEmployeeRole());
			
			System.out.println("===========BEGIN============" + new Date());
    		conn = dataSource.getConnection();
    		
    		//String sql = "select * from IM_WAREHOUSE A , IM_WAREHOUSE_EMPLOYEE B  WHERE B.EMPLOYEE_CODE = ? and BRAND_CODE=?  ORDER BY WAREHOUSE_CODE";
    		String sql = "SELECT * FROM ERP.IM_WAREHOUSE A , ERP.IM_WAREHOUSE_EMPLOYEE B WHERE A.WAREHOUSE_ID = B.WAREHOUSE_ID AND B.EMPLOYEE_CODE = ? and A.BRAND_CODE= ? and A.ENABLE='Y' ORDER BY A.WAREHOUSE_CODE";
    		             
    		log.info("sql:"+sql+" BABU:"+buEmp.getEmployeeRole());
    		stmt = conn.prepareStatement(sql);
    		log.info("取出庫別數量1:");
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
        			bossList.add(rs.getString("WAREHOUSE_CODE"));
        		}
        	}
        	log.info("FFFF:");
        	
        	
        	//for(Object menuId:bossList){
    		//log.info("bossList:"+menuId.toString());
        	//}
    		if (rs1 != null) {
    			buPurchaseHead = this.findById(Long.parseLong(headId));
    			List<AdDetail> adDetails = buPurchaseHead.getAdDetails();
    			log.info("adDetails的數量:"+adDetails.size());
    			while (rs1.next()) {	
    				
    				
    				count++;
    				log.info("adDetailDAO:"+count+" "+headId);
    				AdDetail ad = new AdDetail();
    				log.info("初始化adTail:"+buPurchaseHead);
    				ad.setBrandCode(rs1.getString("BRAND_CODE"));
    				log.info("初始化1");
    				ad.setBrandName("T2");
    				log.info("初始化2");
    				ad.setCategoryCode("");
    				log.info("初始化3");
    				ad.setCategoryName("");
    				log.info("初始化4");
    				ad.setIndexNo(count);
    				log.info("初始化5");
    				//ad.setHeadId(Long.parseLong(headId));
    				for(Object warehouseCode:bossList){
    					if(rs1.getString("WAREHOUSE_CODE").equals(String.valueOf(warehouseCode))){
    						log.info("設定Enable:Y");
    						ad.setEnable("Y");
    					    break;
    					}
    				}
    				log.info("初始化7");
    				ad.setType("WareHouse");
    				log.info("初始化8");
    				ad.setWarehouseCode(rs1.getString("WAREHOUSE_CODE"));
    				log.info("初始化9");
    				ad.setWarehouseName(rs1.getString("WAREHOUSE_NAME"));
    				log.info("初始化10");
    				
    				log.info("ppppppppppppppp:"+rs1.getString("WAREHOUSE_ID"));
    				
    				ad.setEmployeeCode(employeeCode);
    				//ad.setBuPurchaseHead(buPurchaseHead);
    				//SiMenuCtrl simenuCtrl = simenuDAO.findById(rs1.getString("MENU_ID"));
    				//log.info("成本:"+simenuCtrl.getMenuId()+" "+simenuCtrl.getCostControl()+" "+costCtrl);
    				
    				//adDetailDAO.save(ad);
    				adDetails.add(ad);
    			  	
    			}
    			log.info("bbbbb adDetails:"+adDetails.size());
    			buPurchaseHead.setAdDetails(adDetails);
    			buPurchaseHeadDAO.update(buPurchaseHead);
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
	
	public List<Properties> updateAllMenus(Map parameterMap) throws IllegalAccessException,InvocationTargetException, NoSuchMethodException {
			log.info("selectAllMenus....");
			Map returnMap = new HashMap(0);
			int startLineId = 0;
			BuPurchaseHead buPurchaseHead = null;
			int cnt = 0;
			try {
				Object otherBean = parameterMap.get("vatBeanOther");
				String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
				log.info("formIdString:" + formIdString);
				Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null; 
				if (null != formId) {
					buPurchaseHead = this.findById(formId) ;
					List<AdDetail> adDetails = buPurchaseHead.getAdDetails();
					for(AdDetail adDetail:adDetails){
						log.info("前:"+cnt+" enable:"+adDetail.getEnable());
						adDetail.setEnable("Y");
						log.info("後:"+cnt+" enable:"+adDetail.getEnable());
						cnt++;
					}
					buPurchaseHead.setAdDetails(adDetails);
				}
				buPurchaseHeadDAO.merge(buPurchaseHead);
			
				return AjaxUtils.parseReturnDataToJSON(returnMap);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				throw new NoSuchMethodException("Exception:" + ex.getMessage());
			}
	}
	
	public List<Properties> updateAllHouses(Map parameterMap) throws IllegalAccessException,InvocationTargetException, NoSuchMethodException {
		log.info("selectAllHouses....");
		Map returnMap = new HashMap(0);
		int startLineId = 0;
		BuPurchaseHead buPurchaseHead = null;
		int cnt = 0;
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info("formIdString:" + formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null; 
			if (null != formId) {
				buPurchaseHead = this.findById(formId) ;
				List<AdDetail> adDetails = buPurchaseHead.getAdDetails();
				for(AdDetail adDetail:adDetails){
					log.info("前:"+cnt+" enable:"+adDetail.getEnable());
					adDetail.setEnable("Y");
					log.info("後:"+cnt+" enable:"+adDetail.getEnable());
					cnt++;
				}
				buPurchaseHead.setAdDetails(adDetails);
			}
			buPurchaseHeadDAO.merge(buPurchaseHead);
		
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("Exception:" + ex.getMessage());
		}
}
	
	/**
	 * 刪除adDetail明細
	 * 
	 * @param headId
	 * @throws Exception
	 */
	public void deleteAdDetail(String headId) throws Exception{
		try{
			log.info("刪除Addetail明細:");
	        BuPurchaseHead buPurchaseHead = this.findById(Long.parseLong(headId)) ;
	        if(buPurchaseHead == null){
				throw new ValidationErrorException("查無BuPurchaseHead主鍵：" + headId + "的資料！");
			}
	        buPurchaseHead.setAdDetails(new ArrayList<AdDetail>(0));
	        buPurchaseHeadDAO.update(buPurchaseHead);
			log.info("Addetails數量:"+buPurchaseHead.getAdDetails().size());
		}catch (Exception ex){
			log.error("刪除AdDetail時發生錯誤，原因：" + ex.toString());
			throw new Exception("刪除AdDetail明細失敗！");
		}	
	}
	
	public Map updateCalcWorkTime(long headId,long calcArg,String empCode,long totalHours,long totalHoursBefore,boolean isFirst){
		log.info("calcWorkTime:"+headId);
		int dayOri = 0;
		int appendTonext = 0;
		Date estimateStartDare = null;
		Date estimateEndDare = null;
		BuPurchaseHead task = buPurchaseHeadDAO.findById(headId);
		HashMap returnValue = new HashMap();
		Long totalHoursAll = totalHours;
		
		do{
			log.info("蝦");
			totalHoursAll = totalHoursAll - calcArg;
			dayOri++;
			log.info("魚魚魚魚魚魚魚魚魚魚:"+"總公時:"+totalHoursAll+" 天數:"+dayOri);
			
		}while(totalHoursAll>0);
		    if(!(totalHoursAll<0))
			    dayOri++;
			log.info("小魚0:"+empCode+" "+dayOri);
		
		if(isFirst==true){
			estimateStartDare = new Date();
		}else{
			estimateStartDare = nextStartDay;
			log.info("新的開始日期:"+estimateStartDare);
		}
			Calendar c = Calendar.getInstance(); 
			c.setTime(estimateStartDare); 
			c.add(Calendar.DATE, dayOri);
			estimateEndDare = c.getTime();
			log.info("estimateEndDare:"+estimateEndDare);
			log.info("estimateStartDare:"+estimateStartDare);
			task.setEstimateStartDare(estimateStartDare);
			task.setEstimateEndDare(estimateEndDare);
			task.setTotalHours((int)totalHoursBefore);
			c.setTime(estimateEndDare); 
			c.add(Calendar.DATE, 1);
			nextStartDay = c.getTime();
			appendNext = appendTonext;
			buPurchaseHeadDAO.update(task);
			
		return returnValue;
	}
	
public List<Properties> updateCalcWorkTime(Properties httpRequest) throws Exception {
		
		PreparedStatement stmt = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rs = null;
    	ResultSet rs1 = null;
    	Connection conn = null;
    	Long calcArg = 0L;
    	
		try{
			log.info("算公時");
			 Long headId = NumberUtils.getLong( httpRequest.getProperty("headId") );
			 String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			 List employeeList = buEmployeeWithAddressViewDAO.findByPropertyMis("department","資訊部");
			 log.info("算公時找人"+employeeList.size());
			 String[] checkType = {"TASK_REQ","TASK_PRJ","TASK_PRE"};//Test
			 String[] worker = {"T49674","T96085","T60343","T17888","T40857","T15042","T92457","T49674","T96085"};//,"T60343""T17888","T40857","T15042","T92457","T49674","T96085"
			 //List<BuPurchaseHead> allTasks = BuPurchaseHead.findByProperty("BuPurchaseHead", new String[]{"status"},  new Object[]{"SAVE"});
			 System.out.println("===========BEGIN============" + new Date());
	    	 conn = dataSource.getConnection();
	    	 BuEmployee emp = null;
	    	 DateFormat df=new java.text.SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
	    	 
	    	 for(int o = 0;o<worker.length;o++){
	    		 
	    		 emp = (BuEmployee) buEmployeeDAO.findByPrimaryKey(BuEmployee.class, worker[o]);
	    		 Long adHours = emp.getAdHours();
					Long prHours = emp.getAdHours();
					Long prjHours = emp.getAdHours();
	    		 
		    	 for(int i=0;i<checkType.length;i++){
		    		 int firstCnt = 0;
		    		 ArrayList headList = new ArrayList();
		    		 String sql = "select * from ERP.AD_TASK WHERE STATUS in ('SIGNING','SAVE','PLAN') AND CATEGORY_GROUP = '"+checkType[i]+"' AND RQ_IN_CHARGE_CODE = ? AND ORDER_TYPE_CODE = 'IRQ' AND ORDER_NO NOT LIKE '%TMP%' AND RQ_IN_CHARGE_CODE IS NOT NULL and BRAND_CODE='T2' ORDER BY PRIORITY";
			    	 log.info("執行的sql:"+sql);
		    		 stmt = conn.prepareStatement(sql);
			    	 stmt.setString(1, worker[o]);
			    	 
			    	 rs = stmt.executeQuery();
			    	 if (rs != null) {
			        		while (rs.next()){
			        			log.info(worker[o]+" 單號:"+rs.getString("HEAD_ID"));
			        			headList.add(rs.getString("HEAD_ID"));
			        		}
			         }
			    	 
		    		//log.info(worker[o]+" 共:"+headList.size());
		    		 
		    		 for(int j=0;j<headList.size();j++){
		    			 boolean isFirst = false;
		    			 Date startDate = null;
			    		 Map  reMap = null;
			    		 
			    		 BuPurchaseHead task = buPurchaseHeadDAO.findById(Long.parseLong(headList.get(j).toString()));
			    		
			    		 List<BuPurchaseLine> lines = task.getBuPurchaseLines();
			    		 Long totalHours = 0L;
			    		 Long totalHoursBefore = 0L;
			    		 log.info("明細數量:"+lines.size()+" "+task.getHeadId());
			    		 if(lines.size()>0&firstCnt==0){
					    	   isFirst = true;
					    	   firstCnt++;
			    		 }else{
			    			   isFirst = false;
			    		 }
			    		if(lines.size()>0){
			    		for(int k=0;k<lines.size();k++){	      
			    			
				    		    
				    		log.info("計算明細公時:"+lines.get(k).getExecuteHours());
			    			totalHours = totalHours+lines.get(k).getExecuteHours();
			    			totalHoursBefore = totalHours;
				    		log.info("此單總公時:"+totalHours+"計算公時結果:"+totalHours);
				    		
				    						    		   
						    	log.info("frfr");	   
				    		}   //task.getHeadId()+emp.getAdHours()+" "+emp.getEmployeeCode()+" "+totalHours+" "+isFirst+" "+(Date)reMap.get("nextStartDay"
			    		if(checkType[i].equals("TASK_REQ")){
			    			calcArg = 0L;
			    		}else if(checkType[i].equals("TASK_PRJ")){
			    			calcArg = 0L;
			    		}else if(checkType[i].equals("TASK_PRE")){
			    			calcArg = 0L;
			    		}
			    		log.info(" "+totalHours+" "+isFirst+" "+calcArg);
			    		if(appendNext!=0){
			    			totalHours = totalHours - appendNext;
			    			log.info("修正後公時:"+totalHours);
			    		}
			    		reMap = updateCalcWorkTime(task.getHeadId(),calcArg,emp.getEmployeeCode(),totalHours,totalHoursBefore,isFirst);	 
			    		
				    	 }
			    		      
			    		log.info("-------------------------------------");
			    			
			    		 
			    		 }
		    		 
		    		 }
	    	 }
	    	 
	    	 
			 
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
	}
}
