package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryLine;
import tw.com.tm.erp.hbm.bean.SoDeliveryLog;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuFlightScheduleDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryBlackListDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryLogDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;

public class SoDeliveryService {
	public static final String PROGRAM_ID = "SO_DELIVERY";
	
	private static final Log log = LogFactory.getLog(SoDeliveryService.class);
	private ImItemCategoryDAO imItemCategoryDAO;
	private BuOrderTypeService buOrderTypeService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private SoDeliveryMoveService soDeliveryMoveService;
	private SoDeliveryLineDAO soDeliveryLineDAO;
	private SoDeliveryLogDAO soDeliveryLogDAO;
	private SoDeliveryBlackListDAO soDeliveryBlackListDAO;
	private SoSalesOrderItemDAO soSalesOrderItemDAO;
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	private BuBasicDataService buBasicDataService;
	private BuFlightScheduleDAO buFlightScheduleDAO;
	private SoSalesOrderMainService soSalesOrderMainService;
	
private SiUsersGroupDAO siUsersGroupDAO;	
	
	private BuBrandDAO buBrandDAO;
	private BuShopDAO buShopDAO;
	private ImItemDAO imItemDAO;
	private SiProgramLogAction siProgramLogAction;
	private static final String DELIVERY_STORE_AREA = "A";
	private static final String DELIVERY_STORE_AREA_HD = "W";
	public static final int SEARCH_NO_MIN_LENGTH = 3;
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "salesOrderDate", "posMachineCode", "customerPoNo", "transactionSeqNo",  
		"superintendentCode",	"superintendentName", "countryCode", "breakable", 
		"valuable", "status","salesOrderId",
		"lineId", "isLockRecord", "isDeleteRecord", "returnMessage" };

	public static final int[] GRID_FIELD_TYPES = { 
		    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		    "", "", "", "", "",
		    "", "", "", "", 
		    "", "", "", "",
			"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };	
	
	public static final String[] GRID_FIELD_LOG_NAMES = { 
		"indexNo", "logDate", "logType", "logAction", 
		"logLevel",	"message", "createrName","creationDate",
		"lineId", };
	
	public static final int[] GRID_FIELD_LOG_TYPES = { 
	    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATETIME,
	    AjaxUtils.FIELD_TYPE_LONG};
	    
    public static final String[] GRID_FIELD_DEFAULT_LOG_VALUES = { 
	    "", "", "", "", 
	    "", "", "","",
	    ""};	
    
	public static final String[] GRID_FIELD_SO_NAMES = { 
		"indexNo", "itemCode", "itemCName", "quantity",
		 "actualSalesAmount", "lineId" };
	
	public static final int[] GRID_FIELD_SO_TYPES = { 
	    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
	    AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_LONG};
	    
    public static final String[] GRID_FIELD_DEFAULT_SO_VALUES = { 
	    "", "", "", "", 
	    "", ""};	
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderNo", "flightDate","flightNo", "deliveryDate",
		"customerName", "passportNo", "orderDate", "storeAreaName", "storageCode",
		 "totalBagCounts","status", "customerPoNoString", "transactionSeqNoString",
		 "SFaultReasonStr","DFaultReasonStr", "headId","soDelUpdateHeadCode"};
	
    public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,
	    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING};

    public static final String[] GRID_FIELD_SEARCH_DEFAULT_VALUES = { 
	    "", "", "", "",
	    "", "", "", "", "", 
	    "", "", "", "", "",
	    "", "", ""};	
    
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
		this.soDeliveryHeadDAO = soDeliveryHeadDAO;
	}
	public void setSoDeliveryMoveService(SoDeliveryMoveService soDeliveryMoveService) {
		this.soDeliveryMoveService = soDeliveryMoveService;
	}
	public void setSoDeliveryLineDAO(SoDeliveryLineDAO soDeliveryLineDAO) {
		this.soDeliveryLineDAO = soDeliveryLineDAO;
	}
	public void setSoDeliveryBlackListDAO(SoDeliveryBlackListDAO soDeliveryBlackListDAO) {
		this.soDeliveryBlackListDAO = soDeliveryBlackListDAO;
	}
	public void setBuFlightScheduleDAO(BuFlightScheduleDAO buFlightScheduleDAO) {
		this.buFlightScheduleDAO = buFlightScheduleDAO;
	}
	public void setSoDeliveryLogDAO(SoDeliveryLogDAO soDeliveryLogDAO) {
		this.soDeliveryLogDAO = soDeliveryLogDAO;
	}
	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
	public void setSoSalesOrderItemDAO(SoSalesOrderItemDAO soSalesOrderItemDAO) {
		this.soSalesOrderItemDAO = soSalesOrderItemDAO;
	}
	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}
	public void SiUsersGroupDAO(SiUsersGroupDAO siUsersGroupDAO) {
		this.siUsersGroupDAO = siUsersGroupDAO;
	}	
    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
    	this.buBasicDataService = buBasicDataService;
    }
	public void setSoSalesOrderMainService(SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}
	/**
	 * 初始化畫面
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			Map multiList = new HashMap(0);

			SoDeliveryHead form = null == formId ? executeNewSoDelivery(otherBean, resultMap) : findSoDelivery(otherBean, resultMap);
		
				
			List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(form.getBrandCode(),"ITEM_CATEGORY");
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode(), "DZ");
			List<BuCommonPhraseLine> allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SoDeliveryReport", "attribute1='" + form.getBrandCode() + "'");
			List<BuCommonPhraseLine> allSalesFaultReason = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SalesFaultReason", "attribute1='" + form.getBrandCode() + "'");
			List<BuCommonPhraseLine> allDeliverFaultReason = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliverFaultReason", "attribute1='" + form.getBrandCode() + "'");		
			//List<BuCommonPhraseLine> allFlightArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
			//		"DeliveryFlightArea", "attribute1='" + form.getBrandCode() + "'");		
			List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStoreArea", "attribute1='" + form.getBrandCode() + "'");	
		
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allReportList", AjaxUtils.produceSelectorData(allReportList, "lineCode", "name", true, false));
			multiList.put("allSalesFaultReasons", AjaxUtils.produceSelectorData(allSalesFaultReason, "lineCode", "name", true, true));
			multiList.put("allDeliverFaultReasons", AjaxUtils.produceSelectorData(allDeliverFaultReason, "lineCode", "name", true, true));
			multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode","categoryName", true, false));
			//multiList.put("allFlightAreas", AjaxUtils.produceSelectorData(allFlightArea, "lineCode","name", true, false));
			multiList.put("allStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, false));
			
			resultMap.put("multiList", multiList);
			
			System.out.println("executeInitial.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			
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
	 * 建立新的入提單
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public SoDeliveryHead executeNewSoDelivery(Object otherBean, Map resultMap) throws Exception {
		log.info("executeNewSoDelivery....");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		log.info("executeNewSoDelivery...."+loginBrandCode+"/"+loginEmployeeCode+"/"+orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode, orderTypeCode));
		if (buOrderType != null) {
			SoDeliveryHead form = new SoDeliveryHead();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setStatus("SAVE");
			form.setCreatedBy(loginEmployeeCode);
			//form.setDeliveryDate(new Date());
			form.setBreakable("N");
			form.setValuable("N");
			form.setCustomerCode("");
			form.setContactInfo("");
			form.setCustomerName("");
			form.setDeliveryArea("");
			//form.setDeliveryDate(null);
			form.setDeliveryTerminal("");
			form.setDFaultReason("");
			form.setStoreArea("");
			form.setScheduleDeliveryDate(null);
			form.setFlightDate(null);
			form.setFlightNo("");
			form.setLockFlag("N");
			form.setShopCode("");
			form.setBagBarCode("");
			form.setBagCounts1(0L);
			form.setBagCounts2(0L);
			form.setBagCounts3(0L);
			form.setBagCounts4(0L);
			form.setBagCounts5(0L);
			form.setBagCounts6(0L);
			form.setBagCounts7(0L);
			form.setRemark1("");
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(null);
			form.setDeliveredBy("");
			
			//Steve
			form.setsoDelUpdateHeadCode("");
			form.setOriFlight("");
			form.setContactInfo_cs("");
			form.setOriDate(null);
			form.setUpdateType("");
			form.setUpdateContent("");
			form.setContactInfo_cs("");
			form.setCs_Note("");
			form.setContactOverTime("");
			//Steve
			
			//this.saveTmp(form);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			resultMap.put("deliveredByName", "");
			resultMap.put("lastUpdatedByName", "");
			resultMap.put("form", form);
			return form;
		} else {
			throw new NoSuchDataException("入提單別錯誤，請聯絡資訊單位！");
		}

	}

	public SoDeliveryHead findSoDelivery(Object otherBean, Map resultMap) throws Exception {
		log.info("findSoDelivery....");
	    
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

		SoDeliveryHead form = (SoDeliveryHead)soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, formId);
		if (null != form) {
			System.out.println("findSoDelivery.status:"+ form.getStatus());
			System.out.println("findSoDelivery.passportNo:"+ form.getPassportNo());
			System.out.println("findSoDelivery.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			BuShop shop = (BuShop) buShopDAO.findByPrimaryKey(BuShop.class,form.getShopCode());
			
			if (null == shop) 
				throw new ValidationErrorException("店別代號(" + form.getShopCode() + ")錯誤");
			else{
				form.setItemCategory(shop.getSystem());
				form.setShopName(shop.getShopCName());
			}
			
			if(StringUtils.hasText(form.getContactInfo())){
				form.setIsTelBlackList(soDeliveryBlackListDAO.isBlackListByTel(form.getBrandCode(), form.getContactInfo())?"Y":"N");
			}else{
				form.setIsTelBlackList("N");
			}
			if(StringUtils.hasText(form.getPassportNo())){
				form.setIsPassportBlackList(soDeliveryBlackListDAO.isBlackListByPassportNo(form.getBrandCode(), form.getPassportNo())?"Y":"N");
			}else{
				form.setIsPassportBlackList("N");
			}
			Long totalBagCounts =
						form.getBagCounts1()+
						form.getBagCounts2()+
						form.getBagCounts3()+
						form.getBagCounts4()+
						form.getBagCounts5()+
						form.getBagCounts6()+
						form.getBagCounts7();
			
			Date deliveryDate = form.getDeliveryDate();
			resultMap.put("totalBagCounts",totalBagCounts);
			log.info("form.Status:"+form.getStatus()+"/lockFlag:"+form.getLockFlag());
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("lockFlagName", OrderStatus.getChineseWord(form.getLockFlag()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			resultMap.put("createdByName", StringUtils.hasText(form.getCreatedBy()) ? UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()) : "");
			resultMap.put("deliveredByName", StringUtils.hasText(form.getDeliveredBy()) ? UserUtils.getUsernameByEmployeeCode(form.getDeliveredBy()) : "");
			resultMap.put("lastUpdatedByName", StringUtils.hasText(form.getLastUpdatedBy()) ? UserUtils.getUsernameByEmployeeCode(form.getLastUpdatedBy()) : "");
			resultMap.put("madeByName", StringUtils.hasText(form.getMadeBy()) ? UserUtils.getUsernameByEmployeeCode(form.getMadeBy()) : "");
			String status = form.getStatus();
			
			/*Steve 待提領不得出現實提日 Start*/
			if(status.equals("W_DELIVERY")){
				System.out.println("進入判斷是否為代提領");
				Date d = null;
				form.setDeliveryDate(d);
			}
			/*Steve 待提領不得出現實提日 End*/
			resultMap.put("form", form);

			return form;
		} else {
			throw new NoSuchDataException("查無此單號(" + formId + ")，於按下「確認」鍵後，將關閉本視窗！");

		}

	}	
	
	/**
	 * 取得暫時單號存檔
	 *
	 * @param salesOrderHead
	 * @throws Exception
	 */
	public void saveTmp(SoDeliveryHead soDeliveryHead) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			System.out.println("tmpOrderNo:" + tmpOrderNo);
			soDeliveryHead.setOrderNo(tmpOrderNo);
			soDeliveryHead.setLastUpdateDate(new Date());
			soDeliveryHead.setCreationDate(new Date());
			soDeliveryHeadDAO.save(soDeliveryHead);
			
		} catch (Exception ex) {
			log.error("取得暫時單號儲存入提單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存入提單發生錯誤，原因：" + ex.getMessage());
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
			log.info("headId:" + headId);
			HashMap map = new HashMap();
			if(null != headId && 0L != headId){
				SoDeliveryHead form = (SoDeliveryHead)soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, headId);
				//log.info("brandCode:"+form.getBrandCode());
				if(null != form){
					String brandCode = form.getBrandCode();// 品牌代號
					String orderTypeCode =form.getOrderTypeCode();// 單別
					String shopCode = form.getShopCode();// 店別
					map.put("brandCode", brandCode);
					map.put("orderTypeCode", orderTypeCode);
					map.put("shopCode", shopCode);
					log.info("brandCode : " + brandCode);
					log.info("orderTypeCode : " + orderTypeCode);
					log.info("shopCode : " + shopCode);
					// ==============================================================
		
					
					HashMap  returnResult = soDeliveryLineDAO.findPageLine(headId, iSPage,
							iPSize, soDeliveryHeadDAO.QUARY_TYPE_SELECT_RANGE);
					List<SoDeliveryLine> items = (List<SoDeliveryLine>) returnResult.get("form");
					
					if (items != null && items.size() > 0) {
						log.info("item.size:"+ items.size());
						Long firstIndex = items.get(0).getIndexNo(); // 取得第一筆的INDEX
						Long maxIndex = (Long)soDeliveryLineDAO.getMaxIndexNo(headId);
						refreshSoDeliveryLine(map, items);
						log.info("SoDelivery.AjaxUtils.getAJAXPageData ");
		
						result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
								items, gridDatas, firstIndex, maxIndex));
					} else {
						log.info("size of line is 0");
						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
								gridDatas));
		
					}
				}else{
					log.info("form is null ");
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
							gridDatas));
				}
			}else{
				log.info("headId("+headId+") is null ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
						gridDatas));			
			}
			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的入提明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提明細失敗！");
		}
	}
	
	
	public List<Properties> getAJAXPageSoData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils.getLong(httpRequest.getProperty("salesOrderId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String customerPoNo = httpRequest.getProperty("customerPoNo");// 品牌代號
			// ======================帶入Head的值=========================
			log.info("headId:" + headId);
			if(null != headId && 0L != headId){
				HashMap map = new HashMap();
				map.put("brandCode", brandCode);
				log.info("salesOrderHead.headId:" + headId);
				log.info("salesOrderHead.brandCode:" + brandCode);
				 List<SoSalesOrderItem> items =  soSalesOrderItemDAO.findPageLineByCustomerPoNo(brandCode,customerPoNo , iSPage, iPSize);
				if (items != null && items.size() > 0) {
					Long firstIndex = items.get(0).getIndexNo(); // 取得第一筆的INDEX					
       			    //Long maxIndex = (Long)returnResult.get("recordCount"); // 取得最後一筆// 取得最後一筆 INDEX
					Long maxIndex = (Long)soSalesOrderItemDAO.findPageLineMaxIndex(items.get(0).getSoSalesOrderHead().getHeadId());
					// assignItemDefaultValue( map, imMovementItems, true);
					refreshSoDetail(brandCode,items);
					log.info("AjaxUtils.getAJAXPageData ");
	
					result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_SO_NAMES, GRID_FIELD_DEFAULT_SO_VALUES,
							items, gridDatas, firstIndex, maxIndex));
				} else {
					log.info("AjaxUtils.getAJAXPageDataDefault ");
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_SO_NAMES, GRID_FIELD_DEFAULT_SO_VALUES, map,
							gridDatas));
	
				}
			}
			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的入提明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提明細失敗！");
		}
	}
	/**
	 * 更新SoItem相關資料(狀態為可編輯時)
	 *
	 * @param parameterMap
	 * @param salesOrderItems
	 */
	private void refreshSoDeliveryLine(HashMap parameterMap, List<SoDeliveryLine> items) throws Exception {
		log.info("refreshSoDelivery...");
		String brandCode = (String) parameterMap.get("brandCode");
		String orderTypeCode = (String) parameterMap.get("orderTypeCode");
		String shopCode = (String) parameterMap.get("shopCode");
		
		log.info("refreshSoDeliveryLine...."+brandCode+"/"+orderTypeCode+"/"+shopCode);
		BuBrand buBrand = buBrandDAO.findById(brandCode);
		/*
		BuShop shop = (BuShop) buShopDAO.findByPrimaryKey(BuShop.class,shopCode);
		
		if (null == shop) 
			throw new ValidationErrorException("店別代號(" + shopCode + ")錯誤，請重新輸入");
		*/
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		if (buOrderType == null)
			throw new ValidationErrorException("查無單別資料-OrderType(" + orderTypeCode + ")，請通知系統管理員");
		
		
		Map checkInfo = new HashMap();
		checkInfo.put("brandCode", brandCode);
		checkInfo.put("orderTypeCode", orderTypeCode);
		checkInfo.put("shopCode", (String) parameterMap.get("shopCode"));
		if (buBrand != null) {
			for (SoDeliveryLine item : items) {
				
				item.setSuperintendentName(UserUtils.getUsernameByEmployeeCode(item.getSuperintendentCode()));
				if(null != item.getCustomerPoNo()){
					List<SoSalesOrderHead> soHeads = soSalesOrderHeadDAO.findByCustomerPoNo(brandCode, item.getCustomerPoNo().substring(0,10));
					if(soHeads.size()>0){
						item.setStatus(OrderStatus.getChineseWord(soHeads.get(0).getStatus()));		
						item.setTransactionSeqNo(soHeads.get(0).getTransactionSeqNo());
					}else{
						item.setStatus("查無資料");
					}
				}
			}
			
		} else {
			throw new FormException("查詢品牌資訊(" + brandCode + ")請通知資訊人員");
		}
	}
	
	private void refreshSoDetail(String brandCode, List<SoSalesOrderItem> items) throws Exception {
		log.info("refreshSoDetail...");
		for (SoSalesOrderItem item : items) {
			ImItem imItem = imItemDAO.findItem(brandCode, item.getItemCode());
			if (imItem != null) {
				item.setItemCName(imItem.getItemCName());
			}
		}
	}
	public List<Properties> getAJAXPageLogData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			log.info("headId:" + headId);
			HashMap map = new HashMap();
			if(null != headId && 0L != headId){
				String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
				
				map.put("brandCode", brandCode);
				log.info("brandCode : " + brandCode);
				// ==============================================================
	
				log.info("SoDeliveryLog.headId:" + headId);
				HashMap  returnResult = soDeliveryLogDAO.findPageLine(headId, iSPage,
						iPSize, soDeliveryLogDAO.QUARY_TYPE_SELECT_RANGE);
				List<SoDeliveryLog> items = (List<SoDeliveryLog>) returnResult.get("form");
				
				if (items != null && items.size() > 0) {
	
					Long firstIndex = items.get(0).getIndexNo(); // 取得第一筆的INDEX
					//Long maxIndex = (Long)returnResult.get("recordCount"); // 取得最後一筆// 取得最後一筆 INDEX
					Long maxIndex = (Long)soDeliveryLogDAO.getMaxIndexNo(headId);
					// assignItemDefaultValue( map, imMovementItems, true);
					refreshSoDeliveryLog(map, items);
					log.info("AjaxUtils.getAJAXPageLogData ");
					
					result.add(AjaxUtils.getAJAXPageDataByType(httpRequest, GRID_FIELD_LOG_NAMES, GRID_FIELD_DEFAULT_LOG_VALUES,GRID_FIELD_LOG_TYPES,
							items, gridDatas, firstIndex, maxIndex));
				} else {
					log.info("AjaxUtils.getAJAXPageDataLogDefault ");
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_LOG_NAMES, GRID_FIELD_DEFAULT_LOG_VALUES, map,
							gridDatas));
	
				}
			}else{
				log.info("AjaxUtils.getAJAXPageDataLogDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_LOG_NAMES, GRID_FIELD_DEFAULT_LOG_VALUES, map,
						gridDatas));
			}
			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的入提Log發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提Log失敗！");
		}
	}
	private void refreshSoDeliveryLog(HashMap parameterMap, List<SoDeliveryLog> items) throws Exception {
		log.info("refreshSoDeliveryLog...");
			for (SoDeliveryLog item : items) {
				item.setCreaterName(UserUtils.getUsernameByEmployeeCode(item.getCreatedBy()));				
			}
		
	}
	
	public Long findHeadIdBySearchKey(String brandCode, String orderTypeCode, String searchNo, String searchStatus){
		System.out.println("orderTypeCode:"+orderTypeCode+",searchNo:"+searchNo+",searchStatus:"+searchStatus);
		return soDeliveryHeadDAO.findHeadIdByOrderNo(brandCode, orderTypeCode, searchNo, searchStatus);
	}
	public List<Properties> findHeadIdBySearchKey(Map parameterMap) throws Exception {
		
		Map resultMap = new HashMap(0);
		String formId        = new String("");
		String brandCode     = new String("");
		String orderTypeCode = new String("");
		String searchNo      = new String("");
		String searchStatus  = new String("");
		log.info("findDeliveryBySearchKey:" + parameterMap.keySet().toString());
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			searchNo = (String) PropertyUtils.getProperty(otherBean, "searchNo");
			searchStatus = (String) PropertyUtils.getProperty(otherBean, "searchStatus");
			log.info("brandCode:"+brandCode+" orderTypeCode:"+orderTypeCode+" searchNo:"+searchNo+" searchStatus:"+searchStatus);
			System.out.println("brandCode:"+brandCode+" orderTypeCode:"+orderTypeCode+" searchNo:"+searchNo+" searchStatus:"+searchStatus);
			if(StringUtils.hasText(brandCode)&& StringUtils.hasText(orderTypeCode)&&StringUtils.hasText(searchNo)&&searchNo.length()> SEARCH_NO_MIN_LENGTH){
				if("DZN".equals(searchNo.substring(0, 3) )|"DKP".equals(searchNo.substring(0, 3) )){
					formId = String.valueOf(soDeliveryHeadDAO.findHeadIdByOrderNo(brandCode, orderTypeCode, searchNo, searchStatus));
				}else if("TD".equals(searchNo.substring(0, 2))){
					formId = String.valueOf(soDeliveryHeadDAO.findHeadIdByTransactionSeqNo(brandCode, orderTypeCode,searchNo, searchStatus));
				}else{
					formId = String.valueOf(soDeliveryHeadDAO.findHeadIdByCustomerPoNo(brandCode, orderTypeCode,searchNo, searchStatus));
				}
			}else{
				throw new Exception("查詢條件不可為空白或長度不足("+"品牌:"+brandCode+
		                " 單別:"+orderTypeCode+
		                " 單號:"+searchNo+
		                " 狀態:"+searchStatus+")");
			}
			resultMap.put("formId", formId);
			//return AjaxUtils.parseReturnDataToJSON(resultMap);
		}catch (Exception ex) {
				log.error("查詢單頭代號失敗，原因：" + ex.toString());
				Map messageMap = new HashMap();
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查詢單頭代號失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}	
	
	public List<Properties> findShopByCode(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		String formId   = new String("");
		String brandCode= new String("");
		String shopCode = new String("");
		log.info("findShopByCode:" + parameterMap.keySet().toString());
		resultMap.put("shopName", "");
		resultMap.put("itemCategory", "");
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			shopCode = (String) PropertyUtils.getProperty(otherBean, "shopCode");
			log.info("brandCode:"+brandCode+" shopCode:"+shopCode);
			if(StringUtils.hasText(brandCode)&& StringUtils.hasText(shopCode)){
				BuShop shop = (BuShop) buShopDAO.findByPrimaryKey(BuShop.class,shopCode);
				if (null == shop) throw new ValidationErrorException("店別代號(" + shopCode + ")錯誤");
				resultMap.put("shopName", shop.getShopCName());
				resultMap.put("itemCategory", shop.getSystem());
			}else{
				throw new Exception("查詢條件不可為空白("+"品牌:"+brandCode+" 店別:"+shopCode+")");
			}
		}catch (Exception ex) {
				log.error("查詢店別資訊失敗，原因：" + ex.toString());
				Map messageMap = new HashMap();
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查詢店別資訊失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}	
	
	public List<Properties> updateStatus(Map parameterMap) throws Exception { 
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap();
		String status = new String("");
		String logLevel = new String("SUCCESS");
		String employeeCode = new String("");
		Long formId = new Long(0);
		log.info("updateStatus:" + parameterMap.keySet().toString());
		
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			log.info(formIdString);
			formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			log.info("formId:"+formId);
			status = (String) PropertyUtils.getProperty(otherBean, "statusFlag");
			soDeliveryHeadDAO.updateStatusByHeadId(formId, status);
			resultMap.put("status", status);
			resultMap.put("statusName", OrderStatus.getChineseWord(status));
			messageMap.put("type", "ALERT");
			messageMap.put("message", "單據"+OrderStatus.getChineseWord(status)+"成功!");
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
			logLevel = "SUCCESS";
		}catch (Exception ex) {
			log.error("單據狀態更新失敗，原因：" + ex.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", "單據"+OrderStatus.getChineseWord(status)+"失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			logLevel = "ERROR";
		}
		this.updateSoDeliveyLog(formId, "MANUAL", status, logLevel,(String) messageMap.get("message"), employeeCode);
		resultMap.put("vatMessage", messageMap);
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> updateLockFlag(Map parameterMap) throws Exception { 
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap();
		String lockFlag = new String("");
		String logLevel = new String("SUCCESS");
		String employeeCode = new String("");
		Long formId = new Long(0);
		log.info("updateLockFlag:" + parameterMap.keySet().toString());
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			log.info(formIdString);
			formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			log.info("formId:"+formId);
			lockFlag = (String) PropertyUtils.getProperty(otherBean, "lockFlag");
			soDeliveryHeadDAO.updateLockFlagByHeadId(formId, lockFlag);
			resultMap.put("lockFlag", lockFlag);
			resultMap.put("lockFlagName", OrderStatus.getChineseWord(lockFlag));
			messageMap.put("type", "ALERT");
			messageMap.put("message", "單據"+OrderStatus.getChineseWord(lockFlag)+"成功!");
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
			logLevel = "SUCCESS";
		}catch (Exception ex) {
			log.error("單據"+OrderStatus.getChineseWord(lockFlag)+"失敗，原因：" + ex.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", "單據"+OrderStatus.getChineseWord(lockFlag)+"失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			logLevel = "ERROR";
		}
		this.updateSoDeliveyLog(formId, "MANUAL", lockFlag, logLevel,(String) messageMap.get("message"), employeeCode);
		resultMap.put("vatMessage", messageMap);
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> findFlightSchedule(Map parameterMap) throws Exception { 
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap();
		String lockFlag = new String("");
		log.info("updateStatus:" + parameterMap.keySet().toString());
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String flightNo = (String) PropertyUtils.getProperty(otherBean, "flightNo");
			Date flightDate = (Date) PropertyUtils.getProperty(otherBean, "flightDate");
			HashMap findObjs = new HashMap(0);
			findObjs.put("flightNo", flightNo);
			findObjs.put("flightDate", flightDate);
			List<BuFlightSchedule> buFlightSchedules = buFlightScheduleDAO.findByMap(findObjs);
			
			//multiList.put("allStoreAreas", AjaxUtils.produceSelectorData(buFlightSchedules, "scheduleTime","name", true, false));
			
			//resultMap.put("multiList", multiList);
			
		}catch (Exception ex) {
				log.error("單據"+OrderStatus.getChineseWord(lockFlag)+"失敗，原因：" + ex.toString());
				messageMap.put("type", "ALERT");
				messageMap.put("message", "單據"+OrderStatus.getChineseWord(lockFlag)+"失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				
		}
		resultMap.put("vatMessage", messageMap);
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public Long getSoDeliveryHeadId(Object bean) throws FormException, Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的入提單主鍵為空值！");
		}
		return headId;
	}
	public SoDeliveryHead findById(Long headId){
		return (SoDeliveryHead) soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, headId);
	}
	
	public HashMap updateAJAXSoDelivery(Map parameterMap) throws FormException, Exception {

		System.out.println("4.updateAJAXSoDelivery");
		List errorMsgs = new ArrayList(0);

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");

			// System.out.println("employeeCode=" + employeeCode);
			// 取得欲更新的bean
			SoDeliveryHead po = getActualSoDelivery(formLinkBean);
			String identification = MessageStatus.getIdentification(po.getBrandCode(),
					po.getOrderTypeCode(), po.getOrderNo());
			/*---------入提單維護備註欄若空字串則該欄位設空值 --Steve 2012/11/2 Start----------*/
			if(!StringUtils.hasText((String) PropertyUtils.getProperty(formBindBean, "remark1"))){
				//System.out.println("======進入remark1判斷=========");
				po.setRemark1(""); 
			}

			if(!StringUtils.hasText((String) PropertyUtils.getProperty(formBindBean, "remark2"))){
				//System.out.println("======進入remark2判斷=========");
				po.setRemark2("");
			}
			
			if(!StringUtils.hasText((String) PropertyUtils.getProperty(formBindBean, "contactOverTime"))){
				//System.out.println("======進入contactOvertime判斷=========");
				po.setContactOverTime("");
			}
			
			if(!StringUtils.hasText((String) PropertyUtils.getProperty(formBindBean, "bagBarCode"))){
				//System.out.println("======進入bagBarCode判斷=========");
				po.setBagBarCode("");
			}
			/*----------End Steve----------*/	
			if(!StringUtils.hasText((String) PropertyUtils.getProperty(formBindBean, "updateContent"))){
				//System.out.println("======進入updateContent判斷=========");
				po.setUpdateContent("");
			}
			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			String beforeStatus = po.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);
			String formStatus = this.getFormStatus(beforeStatus);
			po.setStatus(formStatus);
			po.setLastUpdatedBy(employeeCode);

			if (OrderStatus.FORM_SUBMIT.equals(formAction)&& OrderStatus.W_CREATE.equalsIgnoreCase(beforeStatus)) {
				po.setMadeBy(employeeCode);
				po.setMakeDate(new Date());
			}
			String resultMsg = this.saveAjaxData(po, formAction, identification, errorMsgs, beforeStatus);
			log.info("資料儲存完畢...");
			resultMap.put("orderNo", po.getOrderNo());
			log.info("放入單號...");
			resultMap.put("status", formStatus);
			log.info("放入單據狀態...");
			resultMap.put("resultMsg", resultMsg);
			log.info("放入執行結果...");
			resultMap.put("entityBean", po);
			log.info("放入entityBean...");

		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("入提單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("入提單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}

		resultMap.put("vatMessage", msgBox);

		return resultMap;
	}
	private SoDeliveryHead getActualSoDelivery(Object bean) throws FormException, Exception {
		log.info("5.1 getActualSoDelivery");
		SoDeliveryHead soDeliveryHead = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualSoDelivery headId=" + id);

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			soDeliveryHead = findById(headId);
			if (soDeliveryHead == null) {
				throw new NoSuchObjectException("查無入提單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + soDeliveryHead.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的入提單主鍵為空值！");
		}

		return soDeliveryHead;
	}
	
	public String saveAjaxData(SoDeliveryHead modifyObj, String formAction, String identification, List errorMsgs,
			String beforeStatus) throws Exception {
		log.info("5.3 saveAjaxData");
		String returnResult = new String("");
		try {
			siProgramLogAction.deleteProgramLog("SO_DELIVERY", null, identification);
			
			if (null != modifyObj) {
				System.out.println("OrderNo=" + modifyObj.getOrderNo() + "/formAction=" + formAction + "/status="
						+ modifyObj.getStatus());
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					modifyObj.setLastUpdateDate(new Date());
					// 執行庫存異動
					log.info("beforeStatus:" + beforeStatus + " formStatus:" + modifyObj.getStatus());
					if(OrderStatus.W_CREATE.equals(beforeStatus)){
						Date today =  DateUtils.getShortDate(new Date());
						Date flightDate =  DateUtils.getShortDate(modifyObj.getFlightDate());
						if(flightDate.before(today)){
							messageHandle(MessageStatus.LOG_ERROR, identification, 
									"回程日期("+DateUtils.format(flightDate,DateUtils.C_DATE_PATTON_SLASH)+")必須小於今天("+DateUtils.format(today,DateUtils.C_DATE_PATTON_SLASH)+")，請重新輸入"
									, "", errorMsgs);
						}
					}
					if (errorMsgs.size() == 0) {
						System.out.println("start update soDelivery");
						soDeliveryHeadDAO.update(modifyObj);
						System.out.println("update soDelivery success");
						returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
					}

				} else {
					System.out.println("start update soDelivery");

					soDeliveryHeadDAO.update(modifyObj);
					System.out.println("update soDelivery success");
					returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
				}
			} else {
				messageHandle(MessageStatus.LOG_ERROR, identification, "入提單存檔時發生錯誤，原因：存檔物件為空值", "", errorMsgs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), modifyObj.getLastUpdatedBy(), errorMsgs);
		}
		log.info("errorMsgs.size():" + errorMsgs.size());
		if (errorMsgs.size() > 0) {
			log.info("error test log =====> 1");
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} else {
			log.info("error test log =====> 2");
			removeDeleteMarkLineForItem(modifyObj);
		}
		log.info("回傳結果...");
		return returnResult;

	}
	public void messageHandle(String messageStatus, String identification, String message, String user, List errorMsgs)
	throws Exception {

		siProgramLogAction.createProgramLog("SO_DELIVERY", MessageStatus.LOG_ERROR, identification, message, user);
		errorMsgs.add(message);
		log.error("ERROR:" + message);
	}
	/**
	 * remove delete mark record(item)
	 *
	 * @param promotion
	 */
	private void removeDeleteMarkLineForItem(SoDeliveryHead soDeliveryHead) throws Exception{
		log.info("移除有{刪除註記}的資料...");
		List soDeliveryLines = soDeliveryHead.getSoDeliveryLines();
		log.info("移除有{刪除註記}的資料 ====> 1");
		if (soDeliveryLines != null && soDeliveryLines.size() > 0) {
			log.info("移除有{刪除註記}的資料 ====> 2");
			for (int i = soDeliveryLines.size() - 1; i >= 0; i--) {
				log.info("移除有{刪除註記}的資料 ====> 3."+i);
				SoDeliveryLine soDeliveryLine = (SoDeliveryLine) soDeliveryLines.get(i);
				log.info("移除有{刪除註記}的資料 ====> 4");
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(soDeliveryLine.getIsDeleteRecord())) {
					log.info("移除有{刪除註記}的資料 ====> 5");
					soDeliveryLines.remove(soDeliveryLine);
					log.info("移除有{刪除註記}的資料 ====> final");
				}
			}
		}
	}
	

	public void updateSoDeliveyLog(Long headId, String type, String action, String level, String message, String employeeCode) throws Exception{
		log.info("start update log... "+headId);
		//TODO update log
		Long indexNo = 0L;
		try{
			if(StringUtils.hasText(message)){
				indexNo = soDeliveryLogDAO.getMaxIndexNo(headId)+1L;
				
				System.out.println(indexNo);
				
				SoDeliveryLog soDeliveryLog = new SoDeliveryLog();
				SoDeliveryHead soDeliveryHead = new SoDeliveryHead();
				soDeliveryHead.setHeadId(headId);
				soDeliveryLog.setSoDeliveryHead(soDeliveryHead);
				soDeliveryLog.setLogType(type);
				soDeliveryLog.setLogAction(action);
				soDeliveryLog.setLogLevel(level);
				soDeliveryLog.setLogDate(new Date());
				soDeliveryLog.setMessage(message.substring(0, message.length()>200?200:message.length()));
				//soDeliveryLog.setMessage("測試");
				soDeliveryLog.setCreatedBy(employeeCode);
				soDeliveryLog.setCreationDate(new Date());
				soDeliveryLog.setLastUpdatedBy(employeeCode);
				soDeliveryLog.setLastUpdateDate(new Date());
				soDeliveryLog.setIndexNo(indexNo);
				//soDeliveryLog.getSoDeliveryHead().getHeadId()
			
				soDeliveryLogDAO.save(soDeliveryLog);
				log.info("update log ... OK ");
			} else {
				//
				log.info("no log for" + action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("入提Log存檔失敗，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}
		
	  
	}
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}
	
	public String  updateDeliveryDataBySp(String spType, Map updateMap) throws Exception {
		log.info("updateDeliveryDataBySp..."+spType);
		try {
			return soDeliveryHeadDAO.updateDeliveryData(spType, updateMap);
		} catch (Exception ex) {
			log.error("更新入提資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提資料失敗！");
		}
		
	}
	
	public List<Properties> getEmployeeInfo(Map parameterMap) {
		Object otherBean    = parameterMap.get("vatBeanOther");
		String employeeCode = new String("");
		String loginEmployeeCode = "";
		String brandCode = "";
		
		Map resultMap = new HashMap(0);
		try {
			 brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			 employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			 loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			System.out.println("brandCode:"+brandCode+","+" loginemployeeCode:"+loginEmployeeCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("查詢員工資料時發生錯誤，原因：" + e.toString());
		}
		String name = UserUtils.getUsernameByEmployeeCode(employeeCode);
		
		
		System.out.println("Name:"+name);
		
		
		if("unknow".equals(name)) name ="";
		
		resultMap.put("employeeName", name);
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			Map multiList = new HashMap(0);
			
			List<ImItemCategory> allItemCategories = imItemCategoryDAO.findByCategoryType(loginBrandCode,"ITEM_CATEGORY");
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "DZN");
			List<BuCommonPhraseLine> allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SoDeliveryReport", "attribute1='" + loginBrandCode + "'", "attribute2='T2'");
			//高雄查詢畫面報表列表  Steve 7/7
			List<BuCommonPhraseLine> allReportListHD = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
				"SoDeliveryReport", "attribute1='" + loginBrandCode + "'", "attribute2='HD'");
			List<BuCommonPhraseLine> allSalesFaultReason = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SalesFaultReason", "attribute1='" + loginBrandCode + "'");
			List<BuCommonPhraseLine> allDeliverFaultReason = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliverFaultReason", "attribute1='" + loginBrandCode + "'");		
			//List<BuCommonPhraseLine> allFlightArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
			//		"DeliveryFlightArea", "attribute1='" + loginBrandCode + "'");		
			List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStoreArea", "attribute1='" + loginBrandCode + "'");	
		
		    List<BuShop> allShops = buShopDAO.findShopByBrand(loginBrandCode, "Y","FD");
		    
			//multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allReportList", AjaxUtils.produceSelectorData(allReportList, "lineCode", "name", true, false));
			multiList.put("allReportListHD", AjaxUtils.produceSelectorData(allReportListHD, "lineCode", "name", true, false));
			multiList.put("allSalesFaultReasons", AjaxUtils.produceSelectorData(allSalesFaultReason, "lineCode", "name", true, true));
			multiList.put("allDeliverFaultReasons", AjaxUtils.produceSelectorData(allDeliverFaultReason, "lineCode", "name", true, true));
			multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCName", true, true));
			//multiList.put("allItemCategories", AjaxUtils.produceSelectorData(allItemCategories, "categoryCode","categoryName", true, false));
			//multiList.put("allFlightAreas", AjaxUtils.produceSelectorData(allFlightArea, "lineCode","name", true, true));
			multiList.put("allStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, true));
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
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageData..."+httpRequest.getProperty("orderTypeCode"));
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			
			findObjs.put("cwCode", httpRequest.getProperty("cwCode"));
			findObjs.put("brandCode", httpRequest.getProperty("loginBrandCode"));
			findObjs.put("orderTypeCode", httpRequest.getProperty("orderTypeCode"));
			findObjs.put("orderNo",  httpRequest.getProperty("orderNo"));
			findObjs.put("startOrderDate", httpRequest.getProperty("startOrderDate"));
			findObjs.put("endOrderDate", httpRequest.getProperty("endOrderDate"));
			findObjs.put("customerName", httpRequest.getProperty("customerName"));
			findObjs.put("contactInfo", httpRequest.getProperty("contactInfo"));
			findObjs.put("passportNo", httpRequest.getProperty("passportNo"));
			findObjs.put("startDeliveryDate", httpRequest.getProperty("startDeliveryDate"));
			findObjs.put("endDeliveryDate", httpRequest.getProperty("endDeliveryDate"));
			findObjs.put("flightNo", httpRequest.getProperty("flightNo"));
			//findObjs.put("flightArea", httpRequest.getProperty("flightArea"));
			findObjs.put("startFlightDate", httpRequest.getProperty("startFlightDate"));
			findObjs.put("endFlightDate", httpRequest.getProperty("endFlightDate"));
			findObjs.put("storeArea", httpRequest.getProperty("storeArea"));
			findObjs.put("shopCode", httpRequest.getProperty("shopCode"));
			findObjs.put("status", httpRequest.getProperty("status"));
			findObjs.put("lockFlag", httpRequest.getProperty("lockFlag"));
			findObjs.put("valuable", httpRequest.getProperty("valuable"));
			findObjs.put("breakable", httpRequest.getProperty("breakable"));
			findObjs.put("startScheduleDeliveryDate", httpRequest.getProperty("startScheduleDeliveryDate"));
			findObjs.put("endScheduleDeliveryDate", httpRequest.getProperty("endScheduleDeliveryDate"));
			findObjs.put("startExpiryDate", httpRequest.getProperty("startExpiryDate"));
			findObjs.put("endExpiryDate", httpRequest.getProperty("endExpiryDate"));	
			findObjs.put("customerPoNo", httpRequest.getProperty("customerPoNo"));
			findObjs.put("transactionSeqNo", httpRequest.getProperty("transactionSeqNo"));
			findObjs.put("SFaultReason", httpRequest.getProperty("SFaultReason"));
			findObjs.put("DFaultReason", httpRequest.getProperty("DFaultReason"));
			findObjs.put("terminal", httpRequest.getProperty("terminal"));
			findObjs.put("affidavit", httpRequest.getProperty("affidavit"));
			findObjs.put("sortKey", httpRequest.getProperty("sortKey"));
			findObjs.put("sortSeq", httpRequest.getProperty("sortSeq"));
			findObjs.put("expiryReturnNo", httpRequest.getProperty("expiryReturnNo"));
			findObjs.put("storageCode", httpRequest.getProperty("storageCode"));
			findObjs.put("storageCode1", httpRequest.getProperty("storageCode1"));
			findObjs.put("soDelUpdateHeadCode", httpRequest.getProperty("soDelUpdateHeadCode"));
			findObjs.put("contactOvertime", httpRequest.getProperty("contactOvertime"));
			// ==============================================================
            log.info("storageCode:"+httpRequest.getProperty("storageCode")+" storage1:"+httpRequest.getProperty("storageCode1"));
			List<SoDeliveryHead> soDeliveryHeads = (List<SoDeliveryHead>) soDeliveryHeadDAO.findByMap(findObjs, iSPage,
					iPSize, soDeliveryHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("soDeliveryHeads.size" + soDeliveryHeads.size());
			
			if (soDeliveryHeads != null && soDeliveryHeads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) soDeliveryHeadDAO.findByMap(findObjs, -1, iPSize,
						soDeliveryHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆
				// INDEX
				
				for (SoDeliveryHead soDeliveryHead : soDeliveryHeads) {
				   
					if(StringUtils.hasText(soDeliveryHead.getSFaultReason())){
						BuCommonPhraseLine SFaultReason = buCommonPhraseLineDAO.findById("SalesFaultReason", soDeliveryHead.getSFaultReason());
						if(null != SFaultReason){
							soDeliveryHead.setSFaultReasonStr(soDeliveryHead.getSFaultReason()+"-"+SFaultReason.getName());
						}
					}
					
					if(StringUtils.hasText(soDeliveryHead.getDFaultReason())){

						BuCommonPhraseLine DFaultReason = buCommonPhraseLineDAO.findById("DeliverFaultReason", soDeliveryHead.getDFaultReason());
						if(null != DFaultReason){
							soDeliveryHead.setDFaultReasonStr(soDeliveryHead.getDFaultReason()+"-"+DFaultReason.getName());
						}
					}
					
					soDeliveryHead.setStatus(OrderStatus.getChineseWord(soDeliveryHead.getStatus()));
					List<SoDeliveryLine> items = soDeliveryHead.getSoDeliveryLines();
					StringBuffer customerPoNoString = new StringBuffer("");
					StringBuffer transactionSeqNoString = new StringBuffer("");
					for (SoDeliveryLine item : items) {
						customerPoNoString.append(item.getCustomerPoNo()+",");
						
						SoSalesOrderHead soSalesOrderHead = soSalesOrderMainService.findById(item.getSalesOrderId());
						transactionSeqNoString.append(soSalesOrderHead.getTransactionSeqNo()+",");
						
					}
					soDeliveryHead.setStoreAreaName(soDeliveryMoveService.getStoreArea(soDeliveryHead.getStoreArea()));
					
					if(StringUtils.hasText(customerPoNoString.toString()))
						soDeliveryHead.setCustomerPoNoString(customerPoNoString.toString().substring(0, customerPoNoString.length()-1));
					
					if(StringUtils.hasText(transactionSeqNoString.toString()))
						soDeliveryHead.setTransactionSeqNoString(transactionSeqNoString.toString().substring(0, transactionSeqNoString.length()-1));
				}

				log.info("AjaxUtils.getAJAXPageData ");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_FIELD_SEARCH_DEFAULT_VALUES,
						soDeliveryHeads, gridDatas, firstIndex, maxIndex));

			} else {
				log.info("AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_FIELD_SEARCH_DEFAULT_VALUES, map, gridDatas));

			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的入提查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提查詢失敗！");
		}
	}
	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
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
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	/*
	 * public HashMap findPageLine(HashMap findObjs, int startPage, int
	 * pageSize) { return imMovementHeadDAO.findPageLine(findObjs, startPage,
	 * pageSize); }
	 */
	public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {
		log.info("updateAllSearchData....");
		Map resultMap = new HashMap(0);
		log.info(parameterMap.keySet().toString());
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String timeScope = (String) PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			System.out.println(AjaxUtils.TIME_SCOPE + ":" + timeScope);
			String isAllClick = (String) PropertyUtils.getProperty(otherBean, "isAllClick");
			log.info("timeScope:" + timeScope);
			log.info("isAllClick:" + isAllClick);
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", PropertyUtils.getProperty(otherBean,"loginBrandCode"));
			findObjs.put("orderTypeCode", PropertyUtils.getProperty(otherBean,"orderTypeCode"));
			findObjs.put("orderNo",  PropertyUtils.getProperty(otherBean,"orderNo"));
			findObjs.put("startOrderDate", PropertyUtils.getProperty(otherBean,"startOrderDate"));
			findObjs.put("endOrderDate", PropertyUtils.getProperty(otherBean,"endOrderDate"));
			findObjs.put("customerName", PropertyUtils.getProperty(otherBean,"customerName"));
			findObjs.put("contactInfo", PropertyUtils.getProperty(otherBean,"contactInfo"));
			findObjs.put("passwordNo", PropertyUtils.getProperty(otherBean,"passwordNo"));
			findObjs.put("startDeliveryDate", PropertyUtils.getProperty(otherBean,"startDeliveryDate"));
			findObjs.put("endDeliveryDate", PropertyUtils.getProperty(otherBean,"endDeliveryDate"));
			findObjs.put("flightNo", PropertyUtils.getProperty(otherBean,"flightNo"));
			//findObjs.put("flightArea", PropertyUtils.getProperty(otherBean,"flightArea"));
			findObjs.put("startFlightDate", PropertyUtils.getProperty(otherBean,"startFlightDate"));
			findObjs.put("endFlightDate", PropertyUtils.getProperty(otherBean,"endFlightDate"));
			findObjs.put("storeArea", PropertyUtils.getProperty(otherBean,"storeArea"));
			findObjs.put("shopCode", PropertyUtils.getProperty(otherBean,"shopCode"));
			findObjs.put("startScheduleDeliveryDate", PropertyUtils.getProperty(otherBean,"startScheduleDeliveryDate"));
			findObjs.put("endScheduleDeliveryDate", PropertyUtils.getProperty(otherBean,"endScheduleDeliveryDate"));
			findObjs.put("customerPoNo", PropertyUtils.getProperty(otherBean,"customerPoNo"));
			findObjs.put("sortKey", "orderNo");
			findObjs.put("sortSeq", "Asc");

			List allDataList = (List) soDeliveryHeadDAO.findByMap(findObjs, 0, 100,
					soDeliveryHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");

			if (allDataList.size() > 0)
				AjaxUtils.updateAllResult(timeScope, isAllClick, allDataList);

			return AjaxUtils.parseReturnDataToJSON(resultMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("Exception:" + ex.getMessage());
		}
	}

	public  List<Properties> updateStatusByHeadId(Map parameterMap) throws Exception {
		
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap(0);
		String logLevel = new String("");
		Long formId = new Long(0);
		String status = new String("");
		String executeEmployee = new String("");
		String beforeStatus = new String("");
		String mode = new String("");
		String message = new String("");
		boolean checkResult = false;
		log.info("test log for 待提領 x-0");
		
		log.info("updateStatusByHeadId.parameterMap:" + parameterMap.keySet().toString());
		try {
			
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String deliveryNo = (String) PropertyUtils.getProperty(otherBean, "deliveryNo");
			executeEmployee = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
			status = (String) PropertyUtils.getProperty(otherBean, "status");
			mode = (String) PropertyUtils.getProperty(otherBean, "mode");
			String deliveryDate =  (String) PropertyUtils.getProperty(otherBean,"pDeliveryDate");
			log.info(formIdString);
			formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			SoDeliveryHead form = (SoDeliveryHead) soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, formId);
			HashMap updateObj = new HashMap(0);
			log.info("test log for RETURN");
			if(null != form){
				beforeStatus = form.getStatus();
				if(beforeStatus.equalsIgnoreCase(status)){
					logLevel = "ERROR";
					resultMap.put("status", status);
					resultMap.put("statusName", OrderStatus.getChineseWord(status));
					messageMap.put("type", "ALERT");
					messageMap.put("message", "此單據狀態已為「"+ OrderStatus.getChineseWord(status)+"」不需再次更新");	
					messageMap.put("event1", null);
					messageMap.put("event2", null);
				}else{
					if("SUPERVISOR".equalsIgnoreCase(mode)){
						checkResult =true;
					}else{
						if(OrderStatus.W_PICK.equalsIgnoreCase(beforeStatus)){
							if(OrderStatus.W_CREATE.equalsIgnoreCase(status) ||
								OrderStatus.CANCEL.equalsIgnoreCase(status)||
								OrderStatus.VOID.equalsIgnoreCase(status)){
								checkResult =true;	
							}
						}else if(OrderStatus.W_CREATE.equalsIgnoreCase(beforeStatus)){
							if(OrderStatus.W_DELIVERY.equalsIgnoreCase(status) ||
								OrderStatus.CANCEL.equalsIgnoreCase(status)||
								OrderStatus.VOID.equalsIgnoreCase(status)){
								checkResult =true;
							}
						}else if(OrderStatus.W_DELIVERY.equalsIgnoreCase(beforeStatus)){
							if(OrderStatus.CLOSE.equalsIgnoreCase(status) ||
								OrderStatus.W_CREATE.equalsIgnoreCase(status)||
								OrderStatus.CANCEL.equalsIgnoreCase(status)||
								OrderStatus.VOID.equalsIgnoreCase(status)||
								OrderStatus.RETURN.equalsIgnoreCase(status)||
								OrderStatus.W_RETURN.equalsIgnoreCase(status)){
								checkResult =true;
							}	
						}else if(OrderStatus.W_RETURN.equalsIgnoreCase(beforeStatus)){
							//增加||OrderStatus.CLOSE.equalsIgnoreCase(status)  Steve
							if(OrderStatus.REFUND.equalsIgnoreCase(status)||OrderStatus.CLOSE.equalsIgnoreCase(status)){
								checkResult =true;
							}	
						}else{
							checkResult =false;
						}
						
						
					}
					log.info("test log for RETURN 1:" + status + "/checkResult:" + checkResult);
					if(checkResult){

						if(OrderStatus.CLOSE.equalsIgnoreCase(status)){
						    if("C".equalsIgnoreCase(soDeliveryMoveService.getStoreAreaType(form.getStoreArea()))){
						    	checkResult = true;
						    }else{
						    	logLevel = "ERROR";
								messageMap.put("type", "ALERT");
								messageMap.put("message", "結案時存放庫別不可為「"+
								                            soDeliveryMoveService.getStoreArea(form.getStoreArea())+"」");
								messageMap.put("event1", null);
								messageMap.put("event2", null);	
								checkResult = false;
						    }
						}else{
							checkResult = true;
						}

						//checkResult = true;
						
					    if(checkResult){
					    	log.info("test log for RETURN 2:" + status + "/deliveryDate:" + deliveryDate);
							updateObj.put("status", status);
							updateObj.put("deliveryDate", deliveryDate);
							updateObj.put("employeeCode", executeEmployee);
							
							System.out.println("...........status................."+status);
							
							if(OrderStatus.CLOSE.equalsIgnoreCase(status))
								resultMap.put("storageCode", "");
							
							if(OrderStatus.W_CREATE.equalsIgnoreCase(status)&&OrderStatus.W_PICK.equalsIgnoreCase(beforeStatus)){
							   if("DZN".equals(form.getOrderNo().substring(0, 3) )){
					                        updateObj.put("storeArea", this.DELIVERY_STORE_AREA);
								resultMap.put("storeArea", this.DELIVERY_STORE_AREA);
								resultMap.put("storeAreaName", soDeliveryMoveService.getStoreArea(this.DELIVERY_STORE_AREA) ); 
							   }else if("DKP".equals(form.getOrderNo().substring(0, 3) )){
							        updateObj.put("storeArea", this.DELIVERY_STORE_AREA_HD);
								resultMap.put("storeArea", this.DELIVERY_STORE_AREA_HD);
								resultMap.put("storeAreaName", soDeliveryMoveService.getStoreArea(this.DELIVERY_STORE_AREA_HD) ); 
							   }
					                 }else
								updateObj.put("storeArea", null);
							
							soDeliveryHeadDAO.updateByHeadId(formId,updateObj);
							if(status.equalsIgnoreCase(OrderStatus.VOID))
								soDeliveryLineDAO.updateCustomerPoNo2Void(formId, executeEmployee,"AFTER");
							if(beforeStatus.equalsIgnoreCase(OrderStatus.VOID))
								soDeliveryLineDAO.updateCustomerPoNo2Void(formId, executeEmployee,"BEFORE");
							resultMap.put("status", status);
							resultMap.put("statusName", OrderStatus.getChineseWord(status));
							resultMap.put("deliveryDate", deliveryDate);
							logLevel = "SUCCESS";
							if("MANUAL".equalsIgnoreCase(mode) || "SUPERVISOR".equalsIgnoreCase(mode)){
								messageMap.put("type", "ALERT");
								messageMap.put("message", "單據:"+deliveryNo+"狀態("+OrderStatus.getChineseWord(beforeStatus)+"->"+
												OrderStatus.getChineseWord(status)+")更新成功!");
								messageMap.put("event1", null);
								messageMap.put("event2", null);
							}else{
								message = "單據:"+deliveryNo+"狀態("+OrderStatus.getChineseWord(beforeStatus)+"->"+
								OrderStatus.getChineseWord(status)+")更新成功!"; 
		                        
							}
					    }else{
					    	log.info("test log for 待提領 3");
					    }
					}else{
						messageMap.put("type", "ALERT");
						messageMap.put("message", "單據狀態為「"+OrderStatus.getChineseWord(beforeStatus)+"」,"+
						                           "不可變更為「"+OrderStatus.getChineseWord(status)+"」");
						messageMap.put("event1", null);
						messageMap.put("event2", null);	
					}
					
				}
			}else{
				logLevel = "ERROR";
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查無此單據資料("+formId+")");
				messageMap.put("event1", null);
				messageMap.put("event2", null);
			}	
			resultMap.put("vatMessage", messageMap);	
		} catch (Exception ex) {
			logLevel = "ERROR";
			log.error("執行失敗，原因：" + ex.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", "檢視失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}
		
		this.updateSoDeliveyLog(formId, "MANUAL", status, logLevel,
				StringUtils.hasText((String) messageMap.get("message"))?(String) messageMap.get("message") : message, executeEmployee);

		resultMap.put("logLevel", logLevel);	
		return AjaxUtils.parseReturnDataToJSON(resultMap);
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
			String reportUrl = new String("");
			Map returnMap = new HashMap(0);
			Object otherBean = parameterMap.get("vatBeanOther");
			String reportFunctionCode = (String) PropertyUtils.getProperty(otherBean, "reportFunctionCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployeeCode");
			Date sOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"startOrderDate"));
			Date eOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"endOrderDate"));		
			Date sDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"startDeliveryDate"));		
			Date eDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"endDeliveryDate"));		
			Date sScheduleDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"startScheduleDeliveryDate"));		
			Date eScheduleDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"endScheduleDeliveryDate"));		
			Date sFlightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"startFlightDate"));		
			Date eFlightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) PropertyUtils.getProperty(otherBean,"endFlightDate"));		
			
			log.info("A1");
			String brandCode= (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String orderNo= (String) PropertyUtils.getProperty(otherBean,"orderNo");

			String startOrderDate= null==sOrderDate?"":DateUtils.format(sOrderDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String endOrderDate= null==eOrderDate?"":DateUtils.format(eOrderDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String customerName= (String) PropertyUtils.getProperty(otherBean,"customerName");
			String contactInfo= (String) PropertyUtils.getProperty(otherBean,"contactInfo");
			String passportNo= (String) PropertyUtils.getProperty(otherBean,"passportNo");
			String startDeliveryDate=  null==sDeliveryDate?"":DateUtils.format(sDeliveryDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 
			String endDeliveryDate= null==eDeliveryDate?"":DateUtils.format(eDeliveryDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String flightNo= (String) PropertyUtils.getProperty(otherBean,"flightNo");
			//String flightArea= (String) PropertyUtils.getProperty(otherBean,"flightArea");
			String startFlightDate= null==sFlightDate?"":DateUtils.format(sFlightDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String endFlightDate= null==eFlightDate?"":DateUtils.format(eFlightDate, DateUtils.C_DATA_PATTON_YYYYMMDD);	
			String storeArea= (String) PropertyUtils.getProperty(otherBean,"storeArea");
			String shopCode= (String) PropertyUtils.getProperty(otherBean,"shopCode");		
			String status= (String) PropertyUtils.getProperty(otherBean,"status");	
			String lockFlag= (String) PropertyUtils.getProperty(otherBean,"lockFlag");	
			String valuable= (String) PropertyUtils.getProperty(otherBean,"valuable");
			String breakable= (String) PropertyUtils.getProperty(otherBean,"breakable");	
			String terminal= (String) PropertyUtils.getProperty(otherBean,"terminal");
			String sortKey= (String) PropertyUtils.getProperty(otherBean,"sortKey");
			String sortSeq= (String) PropertyUtils.getProperty(otherBean,"sortSeq");
			String startScheduleDeliveryDate= null==sScheduleDeliveryDate?"":DateUtils.format(sScheduleDeliveryDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 		
			String endScheduleDeliveryDate= null==eScheduleDeliveryDate?"":DateUtils.format(eScheduleDeliveryDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 
			String customerPoNo= (String) PropertyUtils.getProperty(otherBean,"customerPoNo");
			
			// CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			log.info("A2");
			if ("T2_W00SO1001".equals(reportFunctionCode)||
				"T2_W00SO1001_HD".equals(reportFunctionCode)||
				"T2_W00SO1006".equals(reportFunctionCode)||
				"T2_W00SO1008".equals(reportFunctionCode)) {
				parameters.put("prompt0", orderNo);
				parameters.put("prompt1", startOrderDate);
				parameters.put("prompt2", endOrderDate);
				parameters.put("prompt3", shopCode);
				parameters.put("prompt4", flightNo);
				parameters.put("prompt5", startFlightDate);
				parameters.put("prompt6", endFlightDate);
			    parameters.put("prompt7", "");  //flightArea
				parameters.put("prompt8", storeArea);
				parameters.put("prompt9", status);
				parameters.put("prompt10", startDeliveryDate);
				parameters.put("prompt11", endDeliveryDate);
				parameters.put("prompt12", startScheduleDeliveryDate);
				parameters.put("prompt13", endScheduleDeliveryDate);
				parameters.put("prompt14", valuable);
				parameters.put("prompt15", breakable);
				parameters.put("prompt16", customerName);
				parameters.put("prompt17", contactInfo);
				parameters.put("prompt18", passportNo);
				parameters.put("prompt19", terminal); 
				if ("T2_W00SO1001".equals(reportFunctionCode)||"T2_W00SO1001_HD".equals(reportFunctionCode)){ 
					parameters.put("prompt20", sortKey+",customer_po_no "+sortSeq);
				}
				//if(null!=cwCode&&cwCode.equals("HD")){
				   parameters.put("prompt21", "HD"); 
				//}
			} else if ("T2_W00SO1002".equals(reportFunctionCode)||"T2_W00SO1002_HD".equals(reportFunctionCode)) {
				parameters.put("prompt0", startOrderDate);
				parameters.put("prompt1", endOrderDate);
				parameters.put("prompt2", shopCode);
				//if(null!=cwCode&&cwCode.equals("HD")){
				    parameters.put("prompt3", "HD"); 
				//}
				    log.info("A3");
			} else if ("T2_W00SO1002_1".equals(reportFunctionCode) || "T2_W00SO1002_2".equals(reportFunctionCode)) {
				
				Calendar day = Calendar.getInstance(); 
				
				if ("T2_W00SO1002_1".equals(reportFunctionCode)){
					day.add(Calendar.DATE,-2); 
					parameters.put("prompt0", "19110101");
					parameters.put("prompt1", DateUtils.format(day.getTime(), DateUtils.C_DATA_PATTON_YYYYMMDD));}
				else{
					day.add(Calendar.DATE,-1); 
					String todayStr = DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD);
					parameters.put("prompt0", DateUtils.format(day.getTime(), DateUtils.C_DATA_PATTON_YYYYMMDD));
					parameters.put("prompt1", todayStr);
				}

				reportFunctionCode = "T2_W00SO1002";
				parameters.put("prompt2", shopCode);
			} else if("T2_W00SO1002_1_HD".equals(reportFunctionCode) || "T2_W00SO1002_2_HD".equals(reportFunctionCode)){
			       
			    Calendar day = Calendar.getInstance(); 
				
				if ("T2_W00SO1002_1_HD".equals(reportFunctionCode)){
					day.add(Calendar.DATE,-2); 
					parameters.put("prompt0", "19110101");
					parameters.put("prompt1", DateUtils.format(day.getTime(), DateUtils.C_DATA_PATTON_YYYYMMDD));}
				else{
					day.add(Calendar.DATE,-1); 
					String todayStr = DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD);
					parameters.put("prompt0", DateUtils.format(day.getTime(), DateUtils.C_DATA_PATTON_YYYYMMDD));
					parameters.put("prompt1", todayStr);
				}

				reportFunctionCode = "T2_W00SO1002_HD";
				parameters.put("prompt2", shopCode);
			    
			} else if ("T2_W00SO1003".equals(reportFunctionCode)||"T2_W00SO1003_HD".equals(reportFunctionCode)) {
				parameters.put("prompt0", startScheduleDeliveryDate);
				parameters.put("prompt1", endScheduleDeliveryDate);
				parameters.put("prompt2", storeArea);
				//if(null!=cwCode&&cwCode.equals("HD")){
				    parameters.put("prompt3", "HD"); 
				//}
			} else if ("T2_W00SO1004".equals(reportFunctionCode) ||"T2_W00SO1004_HD".equals(reportFunctionCode) || "T2_W00SO1010".equals(reportFunctionCode)) {
				parameters.put("prompt0", orderNo);
				//if(null!=cwCode&&cwCode.equals("HD")){
				   parameters.put("prompt1", "HD"); 
				//}
			}
	
			reportUrl = SystemConfig.getReportURLByFunctionCode(brandCode, reportFunctionCode, loginEmployeeCode,
						parameters);
log.info("提貨報表url:"+reportUrl);
			returnMap.put("reportUrl", reportUrl);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		}
	}
	
	public List<Properties> findDeliveryByDeliveryNo(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Long formId        = new Long(0);
		String brandCode     = new String("");
		String orderTypeCode = new String("");
		String searchNo      = new String("");
		String searchStatus  = new String("");
		log.info("findSalesOrderByCustomerPoNo:" + parameterMap.keySet().toString());
		Map messageMap = new HashMap();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			searchNo = (String) PropertyUtils.getProperty(otherBean, "searchNo");
			searchStatus = (String) PropertyUtils.getProperty(otherBean, "searchStatus");
			log.info("brandCode:"+brandCode+" orderTypeCode:"+orderTypeCode+" searchNo:"+searchNo+" searchStatus:"+searchStatus);
				if(StringUtils.hasText(brandCode)&& StringUtils.hasText(orderTypeCode)&&StringUtils.hasText(searchNo)&&searchNo.length()> SEARCH_NO_MIN_LENGTH){
					List<SoDeliveryHead> head = soDeliveryHeadDAO.findByOrderNo(brandCode, orderTypeCode,searchNo,null ,searchStatus);
					if(head.size()>0){
						resultMap.put("statusName", OrderStatus.getChineseWord(head.get(0).getStatus()));
						resultMap.put("lockFlagName", OrderStatus.getChineseWord(head.get(0).getLockFlag()));
						resultMap.put("formId",head.get(0).getHeadId());
					}else{
						resultMap.put("statusName", "");
						resultMap.put("lockFlagName","");
						resultMap.put("formId","");
					}

				}else{
					resultMap.put("statusName", "");
					resultMap.put("lockFlagName","");
					resultMap.put("formId","");
					throw new Exception("查詢條件不可為空白或長度不足("+"品牌:"+brandCode+
			                " 單別:"+orderTypeCode+
			                " 單號:"+searchNo+
			                " 狀態:"+searchStatus+")");
				}
			
			
		}catch (Exception ex) {
				log.error("查詢單頭代號失敗，原因：" + ex.toString());
				
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查詢單頭代號失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> findSalesOrderByCustomerPoNo(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Long formId        = new Long(0);
		String brandCode     = new String("");
		String orderTypeCode = new String("");
		String searchNo      = new String("");
		String searchStatus  = new String("");
		log.info("findSalesOrderByCustomerPoNo:" + parameterMap.keySet().toString());
		Map messageMap = new HashMap();
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			searchNo = (String) PropertyUtils.getProperty(otherBean, "searchNo");
			searchStatus = (String) PropertyUtils.getProperty(otherBean, "searchStatus");
			log.info("brandCode:"+brandCode+" orderTypeCode:"+orderTypeCode+" searchNo:"+searchNo+" searchStatus:"+searchStatus);
			List<SoSalesOrderHead> soHeads = soSalesOrderHeadDAO.findByCustomerPoNo(brandCode, searchNo);
			if(soHeads.size()>0){
				SoSalesOrderHead soHead = soHeads.get(0);
				if(StringUtils.hasText(brandCode)&& StringUtils.hasText(orderTypeCode)&&StringUtils.hasText(searchNo)&&searchNo.length()> SEARCH_NO_MIN_LENGTH){
					List<SoDeliveryHead> head = soDeliveryHeadDAO.findByCustomerPoNo(brandCode, orderTypeCode,searchNo, searchStatus);
					if(head.size()>0){
						resultMap.put("CustomerDeliveryNo", head.get(0).getOrderNo());
					}else{
						resultMap.put("CustomerDeliveryNo", "");
					}
					resultMap.put("salesOrderDate", DateUtils.format(soHead.getSalesOrderDate()));
					resultMap.put("shopCode", soHead.getShopCode());
					resultMap.put("posMachineCode", soHead.getPosMachineCode());
				}else{
					resultMap.put("CustomerDeliveryNo", "");
					throw new Exception("查詢條件不可為空白或長度不足("+"品牌:"+brandCode+
			                " 單別:"+orderTypeCode+
			                " 單號:"+searchNo+
			                " 狀態:"+searchStatus+")");
				}
			}else{
				resultMap.put("CustomerDeliveryNo", "");
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查無銷售單("+searchNo+")資料，請重新輸入");
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
			}
			
		}catch (Exception ex) {
				log.error("查詢單頭代號失敗，原因：" + ex.toString());
				
				messageMap.put("type", "ALERT");
				messageMap.put("message", "查詢單頭代號失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}	
	
	private String getFormStatus(String beforeStatus){
		String status = new String("");
		if(OrderStatus.W_PICK.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.W_CREATE;
		}else if(OrderStatus.W_CREATE.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.W_DELIVERY;
		}else if(OrderStatus.W_DELIVERY.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.W_DELIVERY;
		}else if(OrderStatus.CLOSE.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.CLOSE;
		}else if(OrderStatus.VOID.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.VOID;
		}else if(OrderStatus.CANCEL.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.CANCEL;		
		}else if(OrderStatus.RETURN.equalsIgnoreCase(beforeStatus)){
			status =OrderStatus.RETURN;		
		}else{
			status=beforeStatus;
		}
		return status;
	}
	
	public List<Properties> parseDate(Map parameterMap) throws Exception {
		log.info("parseDate....");
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap();
		String parseDate     = new String("");
		String dateFormat  = new String("");
		String checkType  = new String("");
		Object otherBean = parameterMap.get("vatBeanOther");
		parseDate = (String) PropertyUtils.getProperty(otherBean, "parseDate");
		checkType = (String) PropertyUtils.getProperty(otherBean, "checkType");
		if(parseDate.indexOf("/")>0){
			dateFormat = DateUtils.C_DATE_PATTON_SLASH;
		}else if(parseDate.indexOf("-")>0){
			dateFormat = DateUtils.C_DATE_PATTON_DEFAULT;
		}else{
			dateFormat = DateUtils.C_DATA_PATTON_YYYYMMDD;
		}
		Date today =  DateUtils.getShortDate(new Date());
		Date afterParseDate = DateUtils.parseDate(dateFormat, parseDate);
		log.info("checkType"+checkType+" afterParseDate:"+afterParseDate+" today:"+today);
		if(null!= afterParseDate){
			resultMap.put("afterParseDate", afterParseDate);
			if("AFTER_TODAY".equalsIgnoreCase(checkType)){
				if(afterParseDate.before(today)){
					log.error("日期("+DateUtils.format(afterParseDate,DateUtils.C_DATE_PATTON_SLASH)+")必須大於今天("+DateUtils.format(today));
					messageMap.put("type", "ALERT");
					messageMap.put("message", "日期("+DateUtils.format(afterParseDate,DateUtils.C_DATE_PATTON_SLASH)+")必須大於今天("+DateUtils.format(today,DateUtils.C_DATE_PATTON_SLASH)+")，請重新輸入");
					messageMap.put("event1", null);
					messageMap.put("event2", null);
					resultMap.put("vatMessage", messageMap);
				}
			}else if("BEFORE_TODAY".equalsIgnoreCase(checkType)){
				if(afterParseDate.after(today)){
					log.error("日期("+DateUtils.format(afterParseDate,DateUtils.C_DATE_PATTON_SLASH)+")必須小於今天("+DateUtils.format(today));
					messageMap.put("type", "ALERT");
					messageMap.put("message", "日期("+DateUtils.format(afterParseDate,DateUtils.C_DATE_PATTON_SLASH)+")必須小於今天("+DateUtils.format(today,DateUtils.C_DATE_PATTON_SLASH)+")，請重新輸入");
					messageMap.put("event1", null);
					messageMap.put("event2", null);
					resultMap.put("vatMessage", messageMap);
				}
			}
		}else{
			log.error("日期("+parseDate+")轉換錯誤");
			messageMap.put("type", "ALERT");
			messageMap.put("message", "日期("+parseDate+")轉換錯誤，請檢查輸入的日期是否正確!");
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	/**
	 * 取得訊息提示用
	 *
	 * @param headId
	 * @return
	 * @throws Exception
	 */
	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			SoDeliveryHead form = (SoDeliveryHead)soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, headId);
			if (form != null) {
				id = MessageStatus.getIdentification(form.getBrandCode(), form.getOrderTypeCode(),
						form.getOrderNo());
			} else {
				throw new NoSuchDataException("入提單主檔查無主鍵：" + headId + "的資料！");
			}
			log.info("Identification:"+id);
			return id;
		} catch (Exception ex) {
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public SelectDataInfo findViewByMap(HttpServletRequest httpRequest) throws Exception {
		log.info("findViewByMap...export excel");
		String beanName = (String)httpRequest.getParameter("exportBeanName");
		HashMap map = new HashMap();
		try {

			map.put("brandCode"        , (String)httpRequest.getParameter("loginBrandCode"));
			map.put("orderTypeCode"    , (String)httpRequest.getParameter("orderTypeCode"));
			map.put("orderNo"          , (String)httpRequest.getParameter("orderNo"));
			map.put("ifHd"          , (String)httpRequest.getParameter("ifHd"));
			map.put("startOrderDate"   , (String)httpRequest.getParameter("startOrderDate"));
			map.put("endOrderDate"     , (String)httpRequest.getParameter("endOrderDate"));
			map.put("customerName"     , java.net.URLDecoder.decode(httpRequest.getParameter("customerName"),"UTF-8"));
			map.put("contactInfo"      , (String)httpRequest.getParameter("contactInfo"));
			map.put("passportNo"       , (String)httpRequest.getParameter("passportNo"));
			map.put("startDeliveryDate", (String)httpRequest.getParameter("startDeliveryDate"));
			map.put("endDeliveryDate"  , (String)httpRequest.getParameter("endDeliveryDate"));
			map.put("flightNo"         , (String)httpRequest.getParameter("flightNo"));
			map.put("bagBarCode"         , (String)httpRequest.getParameter("bagBarCode"));
			map.put("startFlightDate"  , (String)httpRequest.getParameter("startFlightDate"));
			map.put("endFlightDate"    , (String)httpRequest.getParameter("endFlightDate"));
			map.put("storeArea"        , (String)httpRequest.getParameter("storeArea"));
			map.put("shopCode"         , (String)httpRequest.getParameter("shopCode"));
			map.put("status"           , (String)httpRequest.getParameter("status"));
			map.put("lockFlag"         , (String)httpRequest.getParameter("lockFlag"));
			map.put("valuable"         , (String)httpRequest.getParameter("valuable"));
			map.put("breakable"        , (String)httpRequest.getParameter("breakable"));
			map.put("startScheduleDeliveryDate", (String)httpRequest.getParameter("startScheduleDeliveryDate"));
			map.put("endScheduleDeliveryDate"  , (String)httpRequest.getParameter("endScheduleDeliveryDate"));
			map.put("customerPoNo"     , (String)httpRequest.getParameter("customerPoNo"));
			map.put("SFaultReason"     , (String)httpRequest.getParameter("SFaultReason"));
			map.put("DFaultReason"     , (String)httpRequest.getParameter("DFaultReason"));
			map.put("terminal"         , (String)httpRequest.getParameter("terminal"));
			map.put("startExpiryDate"  , (String)httpRequest.getParameter("startExpiryDate"));
			map.put("endExpiryDate"    , (String)httpRequest.getParameter("endExpiryDate"));	
			map.put("affidavit"        , (String)httpRequest.getParameter("affidavit"));
			map.put("expiryReturnNo"   , java.net.URLDecoder.decode((String)httpRequest.getParameter("expiryReturnNo"),"UTF-8"));
			map.put("storageCode"        , (String)httpRequest.getParameter("storageCode"));
			map.put("storageCode1"        , (String)httpRequest.getParameter("storageCode1"));
			map.put("QueryType"   	   , (String)httpRequest.getParameter("QueryType"));
			map.put("soDelUpdateHeadCode"   	   , (String)httpRequest.getParameter("soDelUpdateHeadCode"));
			map.put("contactInfo_cs"   	   , (String)httpRequest.getParameter("contactInfo_cs"));
			map.put("oriDate"   	   , (String)httpRequest.getParameter("oriDate"));
			map.put("oriFlight"   	   , (String)httpRequest.getParameter("oriFlight"));
			map.put("updateContent"   	   , (String)httpRequest.getParameter("updateContent"));
			map.put("updateContent"   	   , (String)httpRequest.getParameter("updateContent"));  	 
			map.put("bagBarCode"   	   , (String)httpRequest.getParameter("bagBarCode"));  	
			map.put("isSuperVisor"   	   , beanName);
			// 可用庫存excel表的欄位順序
			Object[] object = null;
			
			if(beanName.equals("SO_DELIVERY")){
				
				object = new Object[] {"orderNo","flightDate","flightNo","shopCode","deliveryDate","customerName","passportNo","contactInfo","orderDate","storeArea","storageCode",
						"totalBagCounts","status","customerPoNoString","SFaultReason","DFaultReason","affidavit","remark1","remark2","contactInfo_cs","expiryDate","soDelUpdateHeadCode","oriDate","oriFlight","updateContent","bagBarCode","cs_Note","contactOverTime"};
			}else{
				object = new Object[] {"orderNo","flightDate","flightNo","shopCode","deliveryDate","customerName","orderDate","storeArea","storageCode",
						"totalBagCounts","status","customerPoNoString","SFaultReason","DFaultReason","affidavit","remark1","remark2","contactInfo_cs","expiryDate","soDelUpdateHeadCode","oriDate","oriFlight","updateContent","bagBarCode","cs_Note","contactOverTime"};	
			}
			log.info("--- mac --- startDeliveryDate:" + (String)httpRequest.getParameter("startDeliveryDate") );
			/* mac test */
			log.info("before excel output customer:"+ map.get("customerName"));
					
			List<Object[]> soDeliveryHead = soDeliveryHeadDAO.findViewByMap(map);


		// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List

			log.info("soDeliveryHead.size:"+soDeliveryHead.size());
			List rowData = new ArrayList();
			String line = new String("");
			Long headId = new Long(0);
			for (int i = 0; i < soDeliveryHead.size(); i++) {
				line ="";
				Object[] dataObject = (Object[]) soDeliveryHead.get(i);
				for (int j = 0; j < object.length; j++) {
					String actualValue = null;
					if("storeArea".equalsIgnoreCase((String)object[j])){ // storeArea
						actualValue = soDeliveryMoveService.getStoreArea(String.valueOf(dataObject[j])) ;
					}else if("status".equalsIgnoreCase((String)object[j])){ // status
						actualValue = OrderStatus.getChineseWord(String.valueOf(dataObject[j]));
					}else if("customerPoNoString".equalsIgnoreCase((String)object[j])){ // status
						actualValue = soDeliveryLineDAO.getCustomerPoNoString(Long.valueOf(String.valueOf(dataObject[j])));
					}else if("SFaultReason".equalsIgnoreCase((String)object[j]) || 
						 	 "DFaultReason".equalsIgnoreCase((String)object[j])){ // faultReason
						if(null != dataObject[j]){
							String headCode = "SFaultReason".equalsIgnoreCase((String)object[j]) ?"SalesFaultReason":"DeliverFaultReason";
							BuCommonPhraseLine faultReason = buCommonPhraseLineDAO.findById(headCode, String.valueOf(dataObject[j]));
							if(null != faultReason){
								actualValue =String.valueOf(dataObject[j])+"-"+faultReason.getName();
							}
						}
					}else{
						if (object[j] != null) {
							actualValue = dataObject[j] != null ? String.valueOf(dataObject[j]) : null;
						}
					}
					log.info("excel output i:"+i+"/j:"+j+"/value:"+actualValue);
					dataObject[j] = actualValue;
					line = line +"/"+actualValue;
			}
			rowData.add(dataObject);
			log.info(line);
		}
		return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			// ex.printStackTrace();
			log.error("匯出入提資訊時發生錯誤，原因：" + ex.toString());
			throw new Exception("匯出入提資訊時失敗！");
		}
	}
	
	public List<Properties> updateStorageCode(Map parameterMap) throws Exception {
		
		Map resultMap = new HashMap(0);
		
		Object otherBean = parameterMap.get("vatBeanOther");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		String employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		
		soDeliveryHeadDAO.updateStoreAreaByHeadId(formId, "A", "XXXXX");
		updateSoDeliveyLog(formId,"MANUAL","UPDATE","SUCCESS","狀態：待建檔，設定異常儲位：XXXXX。",employeeCode);
		System.out.println("oooooooooooooooooooooooooo");

		resultMap.put("vatMessage", "異常儲位設定成功！");
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
}
