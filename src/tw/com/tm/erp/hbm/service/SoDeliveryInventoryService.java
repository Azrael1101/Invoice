package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryInventoryLine;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryInventoryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryInventoryLineDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SoDeliveryInventoryService {
	private static final Log log = LogFactory.getLog(SoDeliveryInventoryService.class);
	
	private BuOrderTypeService buOrderTypeService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private SoDeliveryInventoryHead soDeliveryInventoryHead;
	private SoDeliveryInventoryLine soDeliveryInventoryLine;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private SoDeliveryInventoryHeadDAO soDeliveryInventoryHeadDAO;
	private SoDeliveryInventoryLineDAO soDeliveryInventoryLineDAO;
	private SiProgramLogAction siProgramLogAction;
	private BuBrandDAO buBrandDAO;
	
	public static final String PROGRAM_ID_IMPORT = "PROGRAM_ID_IMPORT";
	
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "deliveryOrderNo", "storageCodeSys", "bagCountsSys",
		"storageCode", "bagCounts","lastUpdateByName", "lineId", "isLockRecord", "isDeleteRecord", "message" };

	public static final int[] GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};
	
	public static final String[] GRID_DEFAULT_FIELD_NAMES = {
		"", "", "", "",
		"", "", "", "",
		AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" 
	};
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderTypeCode", "orderNo", "countsDate","countsId",
		"status","headId"};
	
	public static final int[] GRID_SEARCH_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING};
	
	public static final String[] GRID_SEARCH_DEFAULT_FIELD_NAMES = { 
		"", "", "","",
		"", ""};

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	
	public void setSoDeliveryInventoryHead(SoDeliveryInventoryHead soDeliveryInventoryHead) {
		this.soDeliveryInventoryHead = soDeliveryInventoryHead;
	}
	
	public void setSoDeliveryInventoryLine(SoDeliveryInventoryLine soDeliveryInventoryLine) {
		this.soDeliveryInventoryLine = soDeliveryInventoryLine;
	}
	
	public void setSoDeliveryInventoryHeadDAO(SoDeliveryInventoryHeadDAO soDeliveryInventoryHeadDAO) {
		this.soDeliveryInventoryHeadDAO = soDeliveryInventoryHeadDAO;
	}
	
	public void setSoDeliveryInventoryLineDAO(SoDeliveryInventoryLineDAO soDeliveryInventoryLineDAO) {
		this.soDeliveryInventoryLineDAO = soDeliveryInventoryLineDAO;
	}
	
	public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
		this.soDeliveryHeadDAO = soDeliveryHeadDAO;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
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
			System.out.println("executeInitial~~~~~~~~~");
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String loginBrandCode   = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode    = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			Map multiList = new HashMap(0);
			
			SoDeliveryInventoryHead form = null == formId ? executeNewSoDeliveryInventory(otherBean, resultMap) : findSoDeliveryInventory(otherBean, resultMap);
			
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode(), "DZ");
			List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStoreArea", "attribute1='" + form.getBrandCode() + "'");
			
			List<BuCommonPhraseLine> allRange = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStorageCheck", "attribute1='" + form.getBrandCode() + "'");
			
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allStoreArea", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, true));
			multiList.put("allRange", AjaxUtils.produceSelectorData(allRange, "lineCode","name", true, true));
			
			resultMap.put("multiList", multiList);
			resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
		}catch (Exception ex) {
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
	public SoDeliveryInventoryHead executeNewSoDeliveryInventory(Object otherBean, Map resultMap) throws Exception {
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode, orderTypeCode));

		if (buOrderType != null) {
			SoDeliveryInventoryHead form = new SoDeliveryInventoryHead();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setStatus("SAVE");
			form.setCreatedBy(loginEmployeeCode);
			form.setCountsDate(new Date());
			form.setLastUpdateBy(loginEmployeeCode);
			form.setLastUpdateDate(null);
			form.setStoreArea("");
			form.setRange("");
			form.setStorageCodeStart("");
			form.setStorageCodeEnd("");
			form.setCountsId("");
			form.setSuperintendentCode("");
			form.setIsImportedFile("N");
			form.setImportedTimes(0L);
	        this.saveTmp(form);
	        resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			resultMap.put("form", form);
			
			return form;
		} else {
			throw new NoSuchDataException("單別錯誤，請聯絡資訊單位！");
		}	
	}

	public void saveTmp(SoDeliveryInventoryHead soDeliveryInventoryHead) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			System.out.println("tmpOrderNo:" + tmpOrderNo);
			soDeliveryInventoryHead.setOrderNo(tmpOrderNo);
			soDeliveryInventoryHead.setLastUpdateDate(new Date());
			soDeliveryInventoryHead.setCreationDate(new Date());
			soDeliveryInventoryHeadDAO.save(soDeliveryInventoryHead);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存入提儲位盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存入提儲位盤點單發生錯誤，原因：" + ex.getMessage());
		}
	}

	public SoDeliveryInventoryHead findSoDeliveryInventory(Object otherBean, Map resultMap) throws Exception {
		log.info("findSoDeliveryInventory....");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		List errorMsgs = new ArrayList(0);
		SoDeliveryInventoryHead form = (SoDeliveryInventoryHead)soDeliveryInventoryHeadDAO.findByPrimaryKey(SoDeliveryInventoryHead.class, formId);
		if (null != form) {
			System.out.println("findSoDeliveryInventory.status"+ form.getStatus());
			System.out.println("findSoDeliveryInventory.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			String identification = this.getIdentification(formId);
			if(OrderStatus.SAVE.equalsIgnoreCase(form.getStatus())){
				this.checkDeliveryInventory(form, identification, errorMsgs, form.getStatus());
			}

			log.info("form.Status:"+form.getStatus());
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			resultMap.put("createdByName", StringUtils.hasText(form.getCreatedBy()) ? UserUtils
					.getUsernameByEmployeeCode(form.getCreatedBy()) : "");
			resultMap.put("superintendentName", StringUtils.hasText(form.getSuperintendentCode()) ? UserUtils
					.getUsernameByEmployeeCode(form.getSuperintendentCode()) : "");
			resultMap.put("form", form);

			return form;
		} else {
			throw new NoSuchDataException("查無此單號(" + formId + ")，於按下「確認」鍵後，將關閉本視窗！");
		}
	}
	
	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			SoDeliveryInventoryHead soDeliveryInventoryHead = (SoDeliveryInventoryHead) soDeliveryInventoryHeadDAO.findById(headId);
			if (soDeliveryInventoryHead != null) {
				id = MessageStatus.getIdentification(soDeliveryInventoryHead.getBrandCode(), soDeliveryInventoryHead.getOrderTypeCode(),
						soDeliveryInventoryHead.getOrderNo());
			} else {
				throw new NoSuchDataException("入提儲位盤點單主檔查無主鍵：" + headId + "的資料！");
			}

			return id;
		} catch (Exception ex) {
			log.error("查詢主鍵時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢主鍵時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public List<Properties> getEmployeeInfo(Map parameterMap) {
		Object otherBean    = parameterMap.get("vatBeanOther");
		String employeeCode = new String("");
		Map resultMap = new HashMap(0);
		try {
			employeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployee");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("查詢員工資料時發生錯誤，原因：" + e.toString());
		}
		String name = UserUtils.getUsernameByEmployeeCode(employeeCode);
		if("unknow".equals(name)) name ="";
		resultMap.put("employeeName", name);
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> saveLine(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Long indexNo = new Long(0);
		String message = new String("");
		
		// ======================帶入Head的值=========================
		
		Object otherBean = parameterMap.get("vatBeanOther");
	
		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "subOrderTypeCode");
		String flightDate = (String) PropertyUtils.getProperty(otherBean, "flightDate");
		String storeArea = (String) PropertyUtils.getProperty(otherBean, "storeArea");
		String range = (String) PropertyUtils.getProperty(otherBean, "range");
		String storageCodeStart = (String) PropertyUtils.getProperty(otherBean, "storageCodeStart");
		String storageCodeEnd = (String) PropertyUtils.getProperty(otherBean, "storageCodeEnd");
		String doAction = (String) PropertyUtils.getProperty(otherBean, "doAction");
		
		HashMap findObjs = new HashMap();
		
		findObjs.put("brandCode", brandCode);
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("orderNo",  "");
		findObjs.put("startOrderDate", "");
		findObjs.put("endOrderDate", "");
		findObjs.put("customerName", "");
		findObjs.put("contactInfo", "");
		findObjs.put("passportNo", "");
		findObjs.put("startDeliveryDate", "");
		findObjs.put("endDeliveryDate", "");
		findObjs.put("flightNo", "");
		findObjs.put("startFlightDate", flightDate);
		findObjs.put("endFlightDate", flightDate);
		findObjs.put("storeArea", storeArea);
		findObjs.put("shopCode", "");
		findObjs.put("status", "");
		findObjs.put("lockFlag", "");
		findObjs.put("valuable", "");
		findObjs.put("breakable", "");
		findObjs.put("startScheduleDeliveryDate", "");
		findObjs.put("endScheduleDeliveryDate", "");
		findObjs.put("startExpiryDate", "");
		findObjs.put("endExpiryDate", "");	
		findObjs.put("customerPoNo", "");
		findObjs.put("SFaultReason", "");
		findObjs.put("DFaultReason", "");
		findObjs.put("terminal", "");
		findObjs.put("affidavit", "");
		findObjs.put("sortKey", "");
		findObjs.put("sortSeq", "");
		findObjs.put("expiryReturnNo", "");
		findObjs.put("range", range);
		findObjs.put("storageCodeStart", storageCodeStart);
		findObjs.put("storageCodeEnd", storageCodeEnd);
		findObjs.put("soDeliveryType", "Y");
		
		
		String storageEmp = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		
		//檢查盤點明細是否已經下庫存
		List<SoDeliveryInventoryLine>lines =soDeliveryInventoryLineDAO.findLineByheadId(formId);
		
		//刪除資料
		if(lines.size()!=0){
			Long lineId = new Long(0);
		
			for (SoDeliveryInventoryLine line : lines) {
				 lineId = line.getLineId();
				 soDeliveryInventoryLineDAO.delete(line);
			}
		}
		
		message = "";
		
		System.out.println("action:"+doAction);
		System.out.println("ins status:"+"INS".equals(doAction));
		
		if ("INS".equals(doAction)){
			
			List<SoDeliveryHead> soDeliveryHeads = (List<SoDeliveryHead>) soDeliveryHeadDAO.findByMap(findObjs, 0,
					10, soDeliveryHeadDAO.QUARY_TYPE_GET_ALL).get("form");
			
			System.out.println("=====111+aa+111======"+soDeliveryHeads.size());
	
			if (soDeliveryHeads != null && soDeliveryHeads.size() > 0) {
				
				SoDeliveryInventoryLine appendLine = null;
	
				indexNo = 0L;
				
				try{
				for (SoDeliveryHead soDeliveryHead : soDeliveryHeads) {
					
					appendLine = new SoDeliveryInventoryLine();
					
					appendLine.setHeadId(formId);
					appendLine.setDeliveryOrderType(soDeliveryHead.getOrderTypeCode());
					appendLine.setDeliveryOrderNo(soDeliveryHead.getOrderNo());
					appendLine.setStorageCodeSys(soDeliveryHead.getStorageCode()==null?"":soDeliveryHead.getStorageCode());
					appendLine.setBagCountsSys(soDeliveryHead.getTotalBagCounts());
					appendLine.setStorageCode("");
					appendLine.setBagCounts(null);
					appendLine.setIsDeleteRecord("0");
					appendLine.setIsLockRecord("0");
					appendLine.setCreatedBy(storageEmp);
					appendLine.setCreationDate(new Date());
					appendLine.setLastUpdateBy(storageEmp);
					appendLine.setLastUpdateDate(new Date());
					System.out.println("入提單號1："+soDeliveryHead.getOrderNo());
					
					indexNo++;
					appendLine.setIndexNo(indexNo);
					
					System.out.println("indexNo111："+indexNo);
					
					soDeliveryInventoryLineDAO.save(appendLine);
					
					System.out.println("indexNo222："+indexNo);
				}
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("新增錯誤，原因：" + ex.toString());
					throw new Exception("新增失敗！");
				}
				
				
				message = "";
			}
			else{
				message = "無庫存資料可下載！";
				System.out.println("soDeliveryHeads is null~~~");
			}
		}
		
		resultMap.put("message", message);
		
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{

		try {
			HashMap map = new HashMap();
			
			List<Properties> result = new ArrayList();
	  	    List<Properties> gridDatas = new ArrayList();
	  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	  	    
	  	    Map soDeliveryInventoryLineMap = null;
	  	    List<SoDeliveryInventoryLine> soDeliveryInventoryLines = null;
	  	    
	  	    String formId = httpRequest.getProperty("headId"); 
	  	    Long formIdL = Long.parseLong(formId);
	  	    
	  	    HashMap findObjs = new HashMap();
	  	    
	  	    findObjs.put(" and model.headId = :headId", formIdL);
	  	    
	  	    soDeliveryInventoryLineMap = soDeliveryInventoryLineDAO.search("SoDeliveryInventoryLine as model",findObjs," order by indexNo ", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    
	  	    soDeliveryInventoryLines = (List<SoDeliveryInventoryLine>) soDeliveryInventoryLineMap.get(BaseDAO.TABLE_LIST);
	  	    
		    if (soDeliveryInventoryLines != null && soDeliveryInventoryLines.size() > 0) {
			  	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    
			  	Long maxIndex = (Long)soDeliveryInventoryLineDAO.search("SoDeliveryInventoryLine as model", "count(model.indexNo) as rowCount" ,findObjs, "", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
			  	
			  	for (SoDeliveryInventoryLine soDeliveryInventoryLine : soDeliveryInventoryLines) {
			  		soDeliveryInventoryLine.setLastUpdateByName(UserUtils.getUsernameByEmployeeCode(soDeliveryInventoryLine.getLastUpdateBy()));
			  	}

			  	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_DEFAULT_FIELD_NAMES, soDeliveryInventoryLines, gridDatas, firstIndex, maxIndex));
		  	}
		    else
		    {
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_DEFAULT_FIELD_NAMES, map,gridDatas));
		    }
	  	    
	  	    return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的入提儲位明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提儲位明細失敗！");
		}	
	}
	
	public Long getHeadId(Object bean) throws FormException, Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的入提儲位盤點單主鍵為空值！");
		}
		return headId;
	}
	
	/*
	 * 暫存單號取實際單號並更新至主檔
	 *
	 * @param headId @param loginUser @return ImPromotion @throws
	 * ObtainSerialNoFailedException @throws FormException @throws Exception
	 */
	public SoDeliveryInventoryHeadDAO saveActualOrderNo(Long headId, String loginUser) throws ObtainSerialNoFailedException,
			FormException, Exception {
		log.info("2.saveActualOrderNo...head_id=" + headId);
		SoDeliveryInventoryHead so = (SoDeliveryInventoryHead)soDeliveryInventoryHeadDAO.findByPrimaryKey(SoDeliveryInventoryHead.class, headId);
		if (so == null) {
			throw new NoSuchObjectException("查無盤點單主鍵：" + headId + "的資料！");
		} else { // 取得正式的單號
			this.setOrderNo(so);
		}
		so.setLastUpdateBy(loginUser);
		so.setLastUpdateDate(new Date());

		soDeliveryInventoryHeadDAO.update(so);
		// imMovementHeadDAO.merge(imMovementPO);

		return soDeliveryInventoryHeadDAO;
	}
	
	private void setOrderNo(SoDeliveryInventoryHead head) throws ObtainSerialNoFailedException {
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
	
	public HashMap updateAJAX(Map parameterMap) throws FormException, Exception {

		System.out.println("4.updateAJAXSoDeliveryInventory");
		List errorMsgs = new ArrayList(0);

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");

			// System.out.println("employeeCode=" + employeeCode);
			// 取得欲更新的bean
			SoDeliveryInventoryHead so = getActualSoDeliveryInventory(formLinkBean);
			String identification = MessageStatus.getIdentification(so.getBrandCode(),
					so.getOrderTypeCode(), so.getOrderNo());

			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, so);
			log.info(so.getSuperintendentCode());
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			String beforeStatus = so.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);
			String formStatus = getFormStatus(formAction,beforeStatus);
			System.out.println("afterStatus=" + formStatus);
			so.setStatus(formStatus);
			so.setLastUpdateBy(employeeCode);
			String resultMsg = this.saveAjaxData(so, formAction, identification, errorMsgs, beforeStatus);
			log.info("資料儲存完畢...");
			resultMap.put("orderNo", so.getOrderNo());
			log.info("放入單號...");
			resultMap.put("status", formStatus);
			log.info("放入單據狀態...");
			resultMap.put("resultMsg", resultMsg);
			log.info("放入執行結果...");
			resultMap.put("entityBean", so);
			log.info("放入entityBean...");

		} catch (FormException fe) {
			fe.printStackTrace();
			log.error("入提單盤點單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("入提盤點單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.toString());
		}

		resultMap.put("vatMessage", msgBox);

		return resultMap;
	}
	
	private SoDeliveryInventoryHead getActualSoDeliveryInventory(Object bean) throws FormException, Exception {
		log.info("5.1 getActualSoDelivery");
		SoDeliveryInventoryHead soDeliveryInventoryHead = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualSoDelivery headId=" + id);

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			soDeliveryInventoryHead = findById(headId);
			if (soDeliveryInventoryHead == null) {
				throw new NoSuchObjectException("查無入提盤點單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + soDeliveryInventoryHead.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的入提單主鍵為空值！");
		}

		return soDeliveryInventoryHead;
	}
	
	public SoDeliveryInventoryHead findById(Long headId){
		return (SoDeliveryInventoryHead) soDeliveryInventoryHeadDAO.findByPrimaryKey(SoDeliveryInventoryHead.class, headId);
	}
	
	private String getFormStatus(String formAction, String beforeStatus){

		if (OrderStatus.FORM_SUBMIT.equals(formAction)){
			if (OrderStatus.SAVE.equals(beforeStatus))
				return OrderStatus.COUNTING;
			else
				return OrderStatus.COUNT_FINISH;
		}else{
			return OrderStatus.SAVE;
		}
	}
	
	public String saveAjaxData(SoDeliveryInventoryHead modifyObj, String formAction, String identification, List errorMsgs,
			String beforeStatus) throws Exception {
		log.info("5.3 saveAjaxData");
		String returnResult = new String("");
		try {
			siProgramLogAction.deleteProgramLog("SO_DELIVERY_INVENTORY", null, identification);
			
			if (null != modifyObj) {
				
				boolean vsAllowSave = true;
				
				System.out.println("OrderNo=" + modifyObj.getOrderNo() + "/formAction=" + formAction + "/status="
						+ modifyObj.getStatus());
				
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					if (OrderStatus.FORM_SUBMIT.equals(formAction))
						checkDeliveryInventory(modifyObj, identification, errorMsgs,  beforeStatus);
					
					modifyObj.setLastUpdateDate(new Date());
						
					log.info("beforeStatus:" + beforeStatus + " formStatus:" + modifyObj.getStatus()+" error.size:"+errorMsgs.size());
					if (errorMsgs.size() > 0)
						vsAllowSave = false;

				}
				
				if (vsAllowSave){
					System.out.println("start update soDeliveryInventoryHead");
					log.info(modifyObj.getSuperintendentCode())	;	
					soDeliveryInventoryHeadDAO.update(modifyObj);
					System.out.println("update soDeliveryInventoryHead success");
					returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
				}
				
			} else {
				messageHandle(MessageStatus.LOG_ERROR, identification, "入提盤點單存檔時發生錯誤，原因：存檔物件為空值", "", errorMsgs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), modifyObj.getLastUpdateBy(), errorMsgs);
		}
		log.info("errorMsgs.size():" + errorMsgs.size());
		if (errorMsgs.size() > 0) {
			log.info("error test log =====> 1");
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} 
		log.info("回傳結果...");
		return returnResult;

	}
	
	public void checkDeliveryInventory(SoDeliveryInventoryHead checkObj, String identification, List errorMsgs,
			 String beforeStatus) throws Exception {
		log.info("5.3.1 checkDeliveryInventory...New");
		ArrayList removeArray = new ArrayList(0);
		String message = null;
		String user = checkObj.getLastUpdateBy();
		String waitingWarehouseCode = new String("");
		Double itemCount = new Double(0);
		Double maxBoxCount = new Double(0);
		Double nowBoxCount = new Double(0);
		String itemCountField = new String("");
	
		try {
			List<SoDeliveryInventoryLine> items = checkObj.getSoDeliveryInventoryLines();
			log.info("5.3.1.1 check brand....");
			BuBrand buBrand = buBrandDAO.findById(checkObj.getBrandCode());
			if (buBrand == null)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "品牌代號(" + checkObj.getBrandCode()
						+ ")錯誤，請通知系統管理員", user, errorMsgs);
			else {
				waitingWarehouseCode = buBrand.getDefaultWarehouseCode1() == null ? "" : buBrand.getDefaultWarehouseCode1();
			}
			log.info("5.3.1.2 check orderType....");
			BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(checkObj.getBrandCode(), checkObj
					.getOrderTypeCode()));
			if (buOrderType == null)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, "查無單別資料-OrderType("
						+ checkObj.getOrderTypeCode() + ")，請通知系統管理員", user, errorMsgs);

			if (items == null || items.size() == 0)
					this.messageHandle(MessageStatus.LOG_ERROR, identification, "尚未輸入明細資料，請重新確認", user, errorMsgs);
		
			System.out.println("before status:" + beforeStatus);
			System.out.println("current status:" + checkObj.getStatus());

		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), checkObj.getLastUpdateBy(), errorMsgs);

		}

	}
	
	public void messageHandle(String messageStatus, String identification, String message, String user, List errorMsgs)
	throws Exception {

		siProgramLogAction.createProgramLog("SO_DELIVERY_INVENTORY", MessageStatus.LOG_ERROR, identification, message, user);
		errorMsgs.add(message);
		log.error("ERROR:" + message);
	}
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		try{
			HashMap map = new HashMap();
			
			List<Properties> result = new ArrayList();
	  	    List<Properties> gridDatas = new ArrayList();
	  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	  	    
	  	    String brandCode = httpRequest.getProperty("loginBrandCode");
	  	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");
	  	    String startorderNo = httpRequest.getProperty("startorderNo");
	  	    String endorderNo = httpRequest.getProperty("endorderNo");
	  	    String status = httpRequest.getProperty("status");
	  	    String countsId = httpRequest.getProperty("countsId");
	  	    String startcountsDate = httpRequest.getProperty("startcountsDate");
	  	    String endcountsDate = httpRequest.getProperty("endcountsDate");
	  	    
	  	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	  	    HashMap findObjs = new HashMap();
	  	    
	  	    findObjs.put(" and model.headId = :brandCode", brandCode);
	  	    findObjs.put(" and model.orderTypeCode = :orderTypeCode", orderTypeCode);
	  	    findObjs.put(" and model.orderNo >= :startorderNo", startorderNo);
	  	    findObjs.put(" and model.orderNo <= :endorderNo", endorderNo);
	  	    findObjs.put(" and model.status = :status", status);
	  	    findObjs.put(" and model.countsId = :countsId", countsId);

	  	    if (!"".equals(startcountsDate)){
	  	    	Date dStart = dateFormat.parse(startcountsDate);
	  	    	findObjs.put(" and to_char(model.countsDate, 'YYYYMMDD') >= :dStart", dStart);}
	  	    	
	  	    if (!"".equals(endcountsDate)){
	  	    Date dEnd = dateFormat.parse(endcountsDate);
	  	    findObjs.put(" and to_char(model.countsDate, 'YYYYMMDD') <= :dEnd", dEnd);}
	  	    
	  	    Map soDeliveryInventoryHeadMap = soDeliveryInventoryHeadDAO.search("SoDeliveryInventoryHead as model",findObjs," and SUBSTR(model.orderNo,1,3) <> 'TMP' order by lastUpdateDate desc ", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	  	    
	  	    List<SoDeliveryInventoryHead> soDeliveryInventoryHeads = (List<SoDeliveryInventoryHead>) soDeliveryInventoryHeadMap.get(BaseDAO.TABLE_LIST);
	  	    
		  	  if (soDeliveryInventoryHeads != null && soDeliveryInventoryHeads.size() > 0) {
			  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    
				  	Long maxIndex = (Long)soDeliveryInventoryHeadDAO.search("SoDeliveryInventoryHead as model", "count(model.headId) as rowCount" ,findObjs, "", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				  	
				  	for (SoDeliveryInventoryHead soDeliveryInventoryHead : soDeliveryInventoryHeads) {
				  		soDeliveryInventoryHead.setStatus(OrderStatus.getChineseWord(soDeliveryInventoryHead.getStatus()));
				  	}
				  	
			  		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_DEFAULT_FIELD_NAMES, soDeliveryInventoryHeads, gridDatas, firstIndex, maxIndex));
			  }else{	  	  
			  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_DEFAULT_FIELD_NAMES, map,gridDatas));
			  }
		  	    
		
	  	    return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的入提儲位盤點資料發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提儲位盤點資料失敗！");
		}
	}
	
	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			Map multiList = new HashMap(0);
			
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "DZ");
			
			System.out.println("query size:"+allOrderTypes.size());
			
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true));

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
	
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
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
	
	public List<SoDeliveryInventoryHead> executeBatchImportT2(List<SoDeliveryInventoryHead> soDeliveryInventoryHeads, List assembly) throws Exception{
		try{
			String uuid = UUID.randomUUID().toString();
			log.info("executeBatchImportT2");
			
			if(soDeliveryInventoryHeads != null){
				for(int i = 0; i < soDeliveryInventoryHeads.size(); i++){
					SoDeliveryInventoryHead importHead = (SoDeliveryInventoryHead)soDeliveryInventoryHeads.get(i);
					SoDeliveryInventoryHead soDeliveryInventoryHead = soDeliveryInventoryHeadDAO.
					findInventoryByCountsId(importHead.getCountsId(), null);
					
					if(null == soDeliveryInventoryHead){
						throw new Exception("查無盤點代號 : " + importHead.getCountsId() + " 對應之盤點單");
					}
					
					if(OrderStatus.COUNT_FINISH.equals(soDeliveryInventoryHead.getStatus())){
						throw new Exception("盤點代號 : " + importHead.getCountsId() + " 的狀態為完成已無法匯入");
					}
					else if(OrderStatus.VOID.equals(soDeliveryInventoryHead.getStatus())){
						throw new Exception("盤點代號 : " + importHead.getCountsId() + " 的狀態為作廢已無法匯入");
					}
				}
				
				for(int i = 0; i < soDeliveryInventoryHeads.size(); i++){
					SoDeliveryInventoryHead importHead = (SoDeliveryInventoryHead)soDeliveryInventoryHeads.get(i);
					SoDeliveryInventoryHead soDeliveryInventoryHead = soDeliveryInventoryHeadDAO.
					findInventoryByCountsId(importHead.getCountsId(), null);
					
					//判別是否已有匯入資料
					if (soDeliveryInventoryHead.getImportedTimes() > 0){
						//刪除無系統待盤資料的盤點資料
						List<SoDeliveryInventoryLine> delLines = soDeliveryInventoryLineDAO.findDelOrUpdLine(soDeliveryInventoryHead.getHeadId(),"DEL");
						
						System.out.println("delete soDeliveryInventoryLines.size:"+delLines.size());
						
						if (delLines != null && delLines.size() > 0)
							soDeliveryInventoryLineDAO.delete(delLines);
						
						//修改有系統待盤資料的盤點資料
						List<SoDeliveryInventoryLine> updLines = soDeliveryInventoryLineDAO.findDelOrUpdLine(soDeliveryInventoryHead.getHeadId(),"UPD");
						
						if (updLines != null && updLines.size() > 0){
							for (Iterator iterator = updLines.iterator(); iterator.hasNext();) {
								SoDeliveryInventoryLine updLine = (SoDeliveryInventoryLine) iterator.next();
								updLine.setStorageCode("");
								updLine.setBagCounts(null);
								updLine.setLastUpdateBy(importHead.getSuperintendentCode());
								updLine.setLastUpdateDate(new Date());
								
								soDeliveryInventoryLineDAO.update(updLine);
							}
						}
					}
					
					List<SoDeliveryInventoryLine> soDeliveryInventoryLines = importHead.getSoDeliveryInventoryLines(); //盤點的明細資料
					
					Long indexNo = soDeliveryInventoryLineDAO.getMaxIndexNo(soDeliveryInventoryHead.getHeadId());

					for (Iterator iterator = soDeliveryInventoryLines.iterator(); iterator.hasNext();) {
						SoDeliveryInventoryLine soDeliveryInventoryLine = (SoDeliveryInventoryLine) iterator.next();
						//看盤點資料是否新增，是的話用修改
						
						SoDeliveryInventoryLine soDeliveryInventoryLineSys = soDeliveryInventoryLineDAO.findLineByheadIdAndOrderNo(soDeliveryInventoryHead.getHeadId(),soDeliveryInventoryLine.getDeliveryOrderNo());
						
						if (soDeliveryInventoryLineSys==null) //新增
						{
							System.out.println("....soDeliveryInventoryLineSys is add!");
							
							soDeliveryInventoryLineSys = new SoDeliveryInventoryLine();
							soDeliveryInventoryLineSys.setHeadId(soDeliveryInventoryHead.getHeadId());
							soDeliveryInventoryLineSys.setDeliveryOrderType(soDeliveryInventoryLine.getDeliveryOrderNo().substring(0, 3));
							soDeliveryInventoryLineSys.setDeliveryOrderNo(soDeliveryInventoryLine.getDeliveryOrderNo());
							soDeliveryInventoryLineSys.setStorageCodeSys("");
							soDeliveryInventoryLineSys.setBagCountsSys(null);
							soDeliveryInventoryLineSys.setStorageCode(soDeliveryInventoryLine.getStorageCode());
							soDeliveryInventoryLineSys.setBagCounts(soDeliveryInventoryLine.getBagCounts());
							soDeliveryInventoryLineSys.setCreatedBy(importHead.getSuperintendentCode());
							soDeliveryInventoryLineSys.setLastUpdateBy(importHead.getSuperintendentCode());
							soDeliveryInventoryLineSys.setCreationDate(new Date());
							soDeliveryInventoryLineSys.setLastUpdateDate(new Date());
							
							++indexNo;
							System.out.println("....soDeliveryInventoryLine indexNo:"+indexNo);
							
							soDeliveryInventoryLineSys.setIndexNo(indexNo);
						}
						else								  //修改
						{
							soDeliveryInventoryLineSys.setStorageCode(soDeliveryInventoryLine.getStorageCode());
							soDeliveryInventoryLineSys.setBagCounts(soDeliveryInventoryLine.getBagCounts());
							soDeliveryInventoryLineSys.setLastUpdateBy(importHead.getSuperintendentCode());
							soDeliveryInventoryLineSys.setLastUpdateDate(new Date());
						}
						
						soDeliveryInventoryLineDAO.merge(soDeliveryInventoryLineSys);
					}
					
					soDeliveryInventoryHead.setImportedTimes(NumberUtils.getLong(soDeliveryInventoryHead.getImportedTimes()) + 1);
					soDeliveryInventoryHead.setLastUpdateBy(importHead.getLastUpdateBy());
					soDeliveryInventoryHead.setLastUpdateDate(importHead.getLastUpdateDate());
					soDeliveryInventoryHead.setCountsDate(importHead.getCountsDate());
					soDeliveryInventoryHead.setReserve1(importHead.getReserve1());
					soDeliveryInventoryHead.setSuperintendentCode(importHead.getSuperintendentCode());
					soDeliveryInventoryHead.setIsImportedFile("Y");
					
					SiSystemLogUtils.createSystemLog(PROGRAM_ID_IMPORT, MessageStatus.LOG_INFO, 
							"盤點代號 : " + importHead.getCountsId() + " 匯入完成，匯入次數: " + soDeliveryInventoryHead.getImportedTimes() , new Date(), uuid, "SYS");
					
					
					assembly.add(soDeliveryInventoryHead);
					
				}
			}
			return assembly;
		}catch(Exception ex){
			log.error("執行盤點資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
	}
	
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		
		try {
			String errorMsg = null;
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新盤點明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新盤點明細失敗！");
		}
	}
	
	public List<Properties> getReportConfig(Map parameterMap) throws Exception {
		try {
			String reportUrl = new String("");
			Map returnMap = new HashMap(0);
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String brandCode= (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String startOrderNo= (String) PropertyUtils.getProperty(otherBean,"startOrderNo");
			String endOrderNo= (String) PropertyUtils.getProperty(otherBean,"endOrderNo");
			
			// CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			parameters.put("prompt0", startOrderNo);
			parameters.put("prompt1", endOrderNo);

			log.info("beging get url");
			reportUrl = SystemConfig.getReportURLByFunctionCode(brandCode, "T2_W00SO1009", loginEmployeeCode,
					parameters);
			log.info("url:"+reportUrl);
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
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("url產生時發生錯誤，原因："+ex.getMessage());
		}
	}
}




