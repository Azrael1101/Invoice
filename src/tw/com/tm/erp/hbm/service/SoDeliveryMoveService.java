package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryMoveHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryMoveLineDAO;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SoDeliveryMoveService {
	public static final String PROGRAM_ID = "SO_DELIVERY";
	private static final Log log = LogFactory.getLog(SoDeliveryMoveService.class);
	private BuOrderTypeService buOrderTypeService;
	private BaseDAO baseDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private SoDeliveryMoveHeadDAO soDeliveryMoveHeadDAO;
	private SoDeliveryMoveLineDAO soDeliveryMoveLineDAO;
	private SoDeliveryHeadDAO soDeliveryHeadDAO;
	private BuBrandDAO buBrandDAO;
	private SiProgramLogAction siProgramLogAction;
	public static final int SEARCH_NO_MIN_LENGTH = 3;
	public final static String SO_DELIVERY_IMPORT = "SoDeliveryImportDataT2";
	public static final String[] GRID_FIELD_NAMES = { 
		"indexNo", "deliveryOrderType", "deliveryOrderNo", "bagCounts1", 
		"bagCounts2",	"bagCounts3", "bagCounts7","bagCounts4", "bagCounts5", 
		"bagCounts6", "deliveryStoreArea", "deliveryStoreCode", "arrivalStoreArea", "arrivalStoreCode", "lineId",
		 "isLockRecord", "isDeleteRecord", "returnMessage" };

	public static final int[] GRID_FIELD_TYPES = { 
		    AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,
		    AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
		    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		    "", "DZN", "", "", 
		    "", "", "", "", "", 
		    "", "", "", "", "", "", 
			 AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };	
	

	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"orderNo", "orderDate","deliveryStoreName", "arrivalStoreName", 
		"moveEmployeeName", "status",  "headId" };
	
    public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

    public static final String[] GRID_FIELD_SEARCH_DEFAULT_VALUES = { 
	    "", "", "", "", 
	    "", "", ""};	



	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	public void setSoDeliveryMoveHeadDAO(SoDeliveryMoveHeadDAO soDeliveryMoveHeadDAO) {
		this.soDeliveryMoveHeadDAO = soDeliveryMoveHeadDAO;
	}
	public void setSoDeliveryMoveLineDAO(SoDeliveryMoveLineDAO soDeliveryMoveLineDAO) {
		this.soDeliveryMoveLineDAO = soDeliveryMoveLineDAO;
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
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String loginBrandCode   = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode    = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			Map multiList = new HashMap(0);

			SoDeliveryMoveHead form = null == formId ? executeNewSoDeliveryMove(otherBean, resultMap) : findSoDeliveryMove(otherBean, resultMap);

			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(form.getBrandCode(), orderTypeCode);
			List<BuCommonPhraseLine> allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SoDeliveryMoveReport", "attribute1='" + form.getBrandCode() + "'");
	
			List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStoreArea", "attribute1='" + form.getBrandCode() + "'");	
		
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allReportList", AjaxUtils.produceSelectorData(allReportList, "lineCode", "name", true, false));
			multiList.put("allDeliveryStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, false));
			multiList.put("allArrivalStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, false));
			resultMap.put("multiList", multiList);
			resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
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
	public SoDeliveryMoveHead executeNewSoDeliveryMove(Object otherBean, Map resultMap) throws Exception {
		log.info("executeNewSoDeliveryMove....");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
		log.info("executeNewSoDeliveryMove...."+loginBrandCode+"/"+loginEmployeeCode+"/"+orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(loginBrandCode, orderTypeCode));
		List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
				"DeliveryStoreArea", "attribute1='" + loginBrandCode + "'");	
		if (buOrderType != null) {
			SoDeliveryMoveHead form = new SoDeliveryMoveHead();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setStatus("SAVE");
			form.setCreatedBy(loginEmployeeCode);
			form.setOrderDate(new Date());
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(null);
			form.setBagCounts1(0L);
			form.setBagCounts2(0L);
			form.setBagCounts3(0L);
			form.setBagCounts4(0L);
			form.setBagCounts5(0L);
			form.setBagCounts6(0L);
			form.setBagCounts7(0L); 
			form.setTotalBagCounts(0L);
			form.setDeliveryStoreArea(allStoreArea.get(0).getId().getLineCode());
			form.setArrivalStoreArea(allStoreArea.get(0).getId().getLineCode());
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

	public SoDeliveryMoveHead findSoDeliveryMove(Object otherBean, Map resultMap) throws Exception {
		log.info("findSoDeliveryMove....");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		List errorMsgs = new ArrayList(0);
		SoDeliveryMoveHead form = (SoDeliveryMoveHead)soDeliveryMoveHeadDAO.findByPrimaryKey(SoDeliveryMoveHead.class, formId);
		if (null != form) {
			System.out.println("findSoDeliveryMove.status"+ form.getStatus());
			System.out.println("findSoDeliveryMove.OrderTypeCode:" + form.getOrderTypeCode() + " OrderNo:" + form.getOrderNo());
			String identification = this.getIdentification(formId);
			if(OrderStatus.SAVE.equalsIgnoreCase(form.getStatus())){
				siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
				this.checkDeliveryMove(form, identification, errorMsgs, form.getStatus());
			}
			log.info("from.bagCounts1:"+form.getBagCounts1());
			log.info("from.bagCounts2:"+form.getBagCounts2());
			log.info("from.bagCounts3:"+form.getBagCounts3());
			log.info("from.bagCounts4:"+form.getBagCounts4());
			log.info("from.bagCounts5:"+form.getBagCounts5());
			log.info("from.bagCounts6:"+form.getBagCounts6());
			log.info("from.bagCounts7:"+form.getBagCounts7());
			log.info("form.Status:"+form.getStatus());
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("brandName", buBrandDAO.findById(form.getBrandCode()).getBrandName());
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			//resultMap.put("createdByName", StringUtils.hasText(form.getCreatedBy()) ? UserUtils
			//		.getUsernameByEmployeeCode(form.getCreatedBy()) : "");
			resultMap.put("moveEmployeeName", UserUtils.getUsernameByEmployeeCode(form.getMoveEmployee()));
			resultMap.put("moveEmployeeName", UserUtils.getUsernameByEmployeeCode(form.getMoveEmployee()));
			resultMap.put("form", form);

			return form;
		} else {
			throw new NoSuchDataException("查無此單號(" + formId + ")，於按下「確認」鍵後，將關閉本視窗！");

		}

	}	
	
	public void saveTmp(SoDeliveryMoveHead soDeliveryMoveHead) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			System.out.println("tmpOrderNo:" + tmpOrderNo);
			soDeliveryMoveHead.setOrderNo(tmpOrderNo);
			soDeliveryMoveHead.setLastUpdateDate(new Date());
			soDeliveryMoveHead.setCreationDate(new Date());
			soDeliveryMoveHeadDAO.save(soDeliveryMoveHead);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存入提庫存移轉單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存入提庫存移轉單發生錯誤，原因：" + ex.getMessage());
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
				SoDeliveryMoveHead form = (SoDeliveryMoveHead)soDeliveryMoveHeadDAO.findByPrimaryKey(SoDeliveryMoveHead.class, headId);
				//log.info("brandCode:"+form.getBrandCode());
				if(null != form){
					String brandCode = form.getBrandCode();// 品牌代號
					String orderTypeCode =form.getOrderTypeCode();// 單別
					map.put("brandCode", brandCode);
					map.put("orderTypeCode", orderTypeCode);
					log.info("brandCode : " + brandCode);
					log.info("orderTypeCode : " + orderTypeCode);
					// ==============================================================
		
					
					HashMap  returnResult = soDeliveryMoveLineDAO.findPageLine(headId, iSPage,
							iPSize, soDeliveryMoveLineDAO.QUARY_TYPE_SELECT_RANGE);
					List<SoDeliveryMoveLine> items = (List<SoDeliveryMoveLine>) returnResult.get("form");
					
					HashMap parameterMap = new HashMap();
					parameterMap.put("headId", headId);
					
					if(null != form){
						parameterMap.put("brandCode", form.getBrandCode());
						parameterMap.put("deliveryStoreArea", form.getDeliveryStoreArea());
						parameterMap.put("arrivalStoreArea", form.getArrivalStoreArea());
					}
					
					if (items != null && items.size() > 0) {
						log.info("item.size:"+ items.size());
						Long firstIndex = items.get(0).getIndexNo(); // 取得第一筆的INDEX
						Long maxIndex = (Long)soDeliveryMoveLineDAO.getMaxIndexNo(headId);
						refreshSoDeliveryMoveLine(map, items);
						log.info("SoDeliveryMove.AjaxUtils.getAJAXPageData ");
		
						result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
								items, gridDatas, firstIndex, maxIndex));
					} else {
						log.info("size of line is 0");
						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, parameterMap,
								gridDatas));
		
					}
				}else{
					log.info("form("+headId+") is null ");
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
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
	private void refreshSoDeliveryMoveLine(HashMap parameterMap, List<SoDeliveryMoveLine> items) throws Exception {
		log.info("refreshSoDelivery...");
		String brandCode = (String) parameterMap.get("brandCode");
		String orderTypeCode = (String) parameterMap.get("orderTypeCode");
		
		log.info("refreshSoDeliveryLine...."+brandCode+"/"+orderTypeCode+"/"+orderTypeCode);
		BuBrand buBrand = buBrandDAO.findById(brandCode);

		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode));
		if (buOrderType == null)
			throw new ValidationErrorException("查無單別資料-OrderType(" + orderTypeCode + ")，請通知系統管理員");
		
		
		Map checkInfo = new HashMap();
		checkInfo.put("brandCode", brandCode);
		checkInfo.put("orderTypeCode", orderTypeCode);
		if (buBrand != null) {
			//for (SoDeliveryMoveLine item : items) {
				//this.reflashImMovementItem(checkInfo, imMovementItem);
			//}
			
		} else {
			throw new FormException("查詢品牌資訊(" + brandCode + ")請通知資訊人員");
		}
	}
	
	public SoDeliveryMoveHead findById(Long headId){
		return (SoDeliveryMoveHead) soDeliveryMoveHeadDAO.findByPrimaryKey(SoDeliveryMoveHead.class, headId);
	}
	/*
	public HashMap updateAJAXSoDelivery(Map parameterMap) throws FormException, Exception {

		System.out.println("4.updateAJAXSoDelivery");
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
			SoDeliveryMoveHead po = getActualSoDeliveryMove(formLinkBean);
			String identification = MessageStatus.getIdentification(po.getBrandCode(),
					po.getOrderTypeCode(), po.getOrderNo());

			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			String beforeStatus = po.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);
			String formStatus = this.getNextStatus(beforeStatus);
			po.setStatus(formStatus);
			po.setLastUpdatedBy(employeeCode);
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
	*/
	private String getNextStatus(String beforeStatus){
		return OrderStatus.CLOSE;
	}
	private SoDeliveryMoveHead getActualSoDeliveryMove(Object bean) throws FormException, Exception {
		log.info("5.1 getActualSoDelivery");
		SoDeliveryMoveHead soDeliveryMoveHead = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualSoDelivery headId=" + id);

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			soDeliveryMoveHead = findById(headId);
			if (soDeliveryMoveHead == null) {
				throw new NoSuchObjectException("查無入提單主鍵：" + headId + "的資料！");
			}
			log.info("order_no:" + soDeliveryMoveHead.getOrderNo());
		} else {
			throw new ValidationErrorException("傳入的入提單主鍵為空值！");
		}

		return soDeliveryMoveHead;
	}
	
	public String saveAjaxData(SoDeliveryMoveHead modifyObj, String formAction, String identification, List errorMsgs,
			String beforeStatus) throws Exception {
		log.info("5.3 saveAjaxData");
		String returnResult = new String("");
		try {
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
			
			if (null != modifyObj) {
				System.out.println("OrderNo=" + modifyObj.getOrderNo() + "/formAction=" + formAction + "/status="
						+ modifyObj.getStatus());
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					if (OrderStatus.FORM_SUBMIT.equals(formAction)){
						checkDeliveryMove(modifyObj, identification, errorMsgs,  beforeStatus);}
					
					modifyObj.setLastUpdateDate(new Date());
						
					// 執行庫存異動
					log.info("beforeStatus:" + beforeStatus + " formStatus:" + modifyObj.getStatus()+" error.size:"+errorMsgs.size());
					if (errorMsgs.size() == 0) {
						if(OrderStatus.FORM_SUBMIT.equals( formAction))
							updateDeliveryStoreArea(modifyObj);  //更新存放地點
						System.out.println("start update soDeliveryMove");
						log.info(modifyObj.getMoveEmployee())	;	
						soDeliveryMoveHeadDAO.update(modifyObj);
						System.out.println("update soDeliveryMove success");
						returnResult = modifyObj.getOrderTypeCode() + "-" + modifyObj.getOrderNo() + "存檔成功";
					}

				} else {
					System.out.println("start update soDeliveryMove");

					soDeliveryMoveHeadDAO.update(modifyObj);

					System.out.println("update soDeliveryMove success");
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

		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, user);
		
		errorMsgs.add(message);
		log.error("ERROR:" + message);
	}
	/**
	 * remove delete mark record(item)
	 *
	 * @param promotion
	 */
	private void removeDeleteMarkLineForItem(SoDeliveryMoveHead soDeliveryMoveHead) throws Exception{
		log.info("移除有{刪除註記}的資料...");
		List<SoDeliveryMoveLine> soDeliveryMoveLines = soDeliveryMoveHead.getSoDeliveryMoveLines();
		log.info("移除有{刪除註記}的資料 ====> 1");
		if (soDeliveryMoveLines != null && soDeliveryMoveLines.size() > 0) {
			log.info("移除有{刪除註記}的資料 ====> 2");
			for (int i = soDeliveryMoveLines.size() - 1; i >= 0; i--) {
				log.info("移除有{刪除註記}的資料 ====> 3."+i);
				SoDeliveryMoveLine soDeliveryMoveLine = (SoDeliveryMoveLine) soDeliveryMoveLines.get(i);
				log.info("移除有{刪除註記}的資料 ====> 4");
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(soDeliveryMoveLine.getIsDeleteRecord())) {
					log.info("移除有{刪除註記}的資料 ====> 5");
					soDeliveryMoveLines.remove(soDeliveryMoveLine);
					log.info("移除有{刪除註記}的資料 ====> final");
				}
			}
		}
	}
	
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....");
		try {
			String errorMsg = null;
			int indexNo = 0;
			
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String status = httpRequest.getProperty("status");
			
			if (headId == null) 
				throw new ValidationErrorException("傳入的移轉單主鍵為空值！");
			
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			
			
			SoDeliveryMoveLine soDeliveryMoveLineMax = ((SoDeliveryMoveLine)baseDAO.findLastByProperty("SoDeliveryMoveLine","", " and soDeliveryMoveHead.headId = ? ", new Object[]{headId}, "order by indexNo"));
			if(null != soDeliveryMoveLineMax){
				indexNo = soDeliveryMoveLineMax.getIndexNo().intValue();
			}
			
			SoDeliveryMoveHead head = getBeanByHeadId(headId);
			if(null == head)
				throw new ValidationErrorException("查無移轉單主鍵之單頭！");
			
			Map findObjs = new HashMap();
			findObjs.put("and model.soDeliveryMoveHead.headId = :headId", headId);
			
			if(OrderStatus.SAVE.equals(head.getStatus()) || OrderStatus.VOID.equals(head.getStatus())){
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						
						log.info("lineId = " + lineId);
						String deliveryOrderNo = upRecord.getProperty("deliveryOrderNo");
						String arrivalStoreCode = upRecord.getProperty("arrivalStoreCode");
						if (StringUtils.hasText(deliveryOrderNo)) {
							SoDeliveryMoveLine soDeliveryMoveLine = (SoDeliveryMoveLine)baseDAO.findFirstByProperty("SoDeliveryMoveLine", "and soDeliveryMoveHead.headId = ? and lineId = ?", new Object[]{ headId, lineId } );
							Date date = new Date();
							if ( soDeliveryMoveLine != null ) {
								log.info( "更新 = " + headId + " | "+ headId  );
								AjaxUtils.setPojoProperties(soDeliveryMoveLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								soDeliveryMoveLine.setLastUpdatedBy(loginEmployeeCode);
								soDeliveryMoveLine.setLastUpdateDate(date);
								soDeliveryMoveLine.setArrivalStoreCode(arrivalStoreCode.toUpperCase());
								baseDAO.update(soDeliveryMoveLine);
							}else{
								log.info( "新增 = " + headId + " | "+ lineId  );
								indexNo++;
								soDeliveryMoveLine = new SoDeliveryMoveLine(); 
								AjaxUtils.setPojoProperties(soDeliveryMoveLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								soDeliveryMoveLine.setSoDeliveryMoveHead(head);
								soDeliveryMoveLine.setCreatedBy(loginEmployeeCode);
								soDeliveryMoveLine.setCreationDate(date);
								soDeliveryMoveLine.setLastUpdatedBy(loginEmployeeCode);
								soDeliveryMoveLine.setLastUpdateDate(date);
								soDeliveryMoveLine.setArrivalStoreCode(arrivalStoreCode.toUpperCase());
								soDeliveryMoveLine.setIndexNo(Long.valueOf(indexNo));
								baseDAO.save(soDeliveryMoveLine);
							}
						}
					}
				}
			}
			
			//AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
			
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新入提明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新入提明細失敗！");
		}
	}
	
	/**
     * 更新PAGE的LINE
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
	/*
    public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
    	log.info("updateAJAXPageLinesData....");
        try{
    	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    	    if(headId == null){
    	    	throw new ValidationErrorException("傳入的調撥單主鍵為空值！");
    	    }
    	    String status = httpRequest.getProperty("status");
    	    String errorMsg = null;
    	    

    	    log.info("updateAJAXPageLinesData.status:"+status);
    	    if (OrderStatus.SAVE.equals(status) ) {
    	    	SoDeliveryMoveHead soDeliveryMoveHead = new SoDeliveryMoveHead();
    	    	soDeliveryMoveHead.setHeadId(headId);
	    		// 將STRING資料轉成List Properties record data
	    		List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
	    		// Get INDEX NO
	    		int indexNo = soDeliveryMoveLineDAO.getMaxIndexNo(headId).intValue();
	    		log.info("updateAJAXPageLinesData.maxIndexNo:"+indexNo);
	    		if (upRecords != null) {
	    		    for (Properties upRecord : upRecords) {	
	    		        // 先載入HEAD_ID OR LINE DATA
		    			Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
		    			log.info("updateAJAXPageLinesData.lineId:"+lineId);
		    			String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[2]);
		    			if (StringUtils.hasText(itemCode)) {
		    				SoDeliveryMoveLine itemUp = soDeliveryMoveLineDAO.findItemByIdentification(soDeliveryMoveHead.getHeadId(), lineId);
		    			    if(itemUp != null){
			    				AjaxUtils.setPojoProperties(itemUp, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
			    				soDeliveryMoveLineDAO.update(itemUp);
		    			    }else{
		    			    	indexNo++;
			    				SoDeliveryMoveLine item = new SoDeliveryMoveLine();
			    				AjaxUtils.setPojoProperties(item, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
			    				item.setIndexNo(Long.valueOf(indexNo));
			    				item.setSoDeliveryMoveHead(soDeliveryMoveHead);
			    				soDeliveryMoveLineDAO.save(item);
			    				
		    			    }
		    			}
	    		    }
	    		}
    	    }
    	    
    	    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新調撥明細時發生錯誤，原因：" + ex.toString());
            throw new Exception("更新銷貨明細失敗！"); 
        }	
    }
    
	*/

	
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
	
	
	public List<Properties> getSoDeliveryInfo(Map parameterMap) {
		Object otherBean    = parameterMap.get("vatBeanOther");
		String brandCode = new String("");
		String orderTypeCode = new String("");
		String orderNo = new String("");
		String storeArea = new String("");
		String message = new String("");
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap(0);
		resultMap.put("lineBagCounts1", 0);
		resultMap.put("lineBagCounts2", 0);
		resultMap.put("lineBagCounts3", 0);
		resultMap.put("lineBagCounts4", 0);
		resultMap.put("lineBagCounts5", 0);
		resultMap.put("lineBagCounts6", 0);
		resultMap.put("lineBagCounts7", 0);
		try {
			brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "deliveryOrderType");
			orderNo = (String) PropertyUtils.getProperty(otherBean, "deliveryOrderNo");
			storeArea = (String) PropertyUtils.getProperty(otherBean, "deliveryStoreArea");
			Map formResult = this.getDelivery(brandCode, orderTypeCode, orderNo, storeArea);
			SoDeliveryHead form = (SoDeliveryHead) formResult.get("form");
			message = (String) formResult.get("message");
			if(null != form){
				resultMap.put("lineBagCounts1", String.valueOf(form.getBagCounts1()));
				resultMap.put("lineBagCounts2", form.getBagCounts2());
				resultMap.put("lineBagCounts3", form.getBagCounts3());
				resultMap.put("lineBagCounts4", form.getBagCounts4());
				resultMap.put("lineBagCounts5", form.getBagCounts5());
				resultMap.put("lineBagCounts6", form.getBagCounts6());
				resultMap.put("lineBagCounts7", form.getBagCounts7());
				
			}else{
				resultMap.put("message", message);
				messageMap.put("type", "ALERT");
				messageMap.put("message", message);
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);
			}
		} catch (Exception e) {
			message = "入提單查詢失敗，原因：" + e.toString();
			log.error("入提單查詢失敗，原因：" + e.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", message);
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);
		}

		resultMap.put("message", message);
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	
	private Map getDelivery(String brandCode, String orderTypeCode, String orderNo, String storeArea){
		Map result = new HashMap(0);
		String message = new String("");
		List<SoDeliveryHead> forms = soDeliveryHeadDAO.findByOrderNo(brandCode, orderTypeCode, orderNo, storeArea ,null);
		log.info("================findByOrderNo.size:==============:"+forms.size());
		if(forms.size() != 0){
			SoDeliveryHead form = forms.get(0);
			log.info("findByOrderNo.status:"+form.getStatus());
			if(OrderStatus.W_DELIVERY.equalsIgnoreCase(form.getStatus()) || OrderStatus.W_RETURN.equalsIgnoreCase(form.getStatus())){
				result.put("form", form);
				message ="";
			}else{
				message = "入提單(" + orderNo+")狀態為「"+OrderStatus.getChineseWord(form.getStatus())+"」不可移轉";
				result.put("form", null);
			}
			
		}else{
			result.put("form", null);
			
			message = "於庫別("+storeArea+"-"+this.getStoreArea(storeArea)+")中，查無入提單資訊(" + orderNo+")";
		}
		result.put("message", message);
		return result;
	}
	
	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			Map multiList = new HashMap(0);
			
			
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "DZM");
			List<BuCommonPhraseLine> allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"SoDeliveryMoveReport", "attribute1='" + loginBrandCode + "'");
		
			List<BuCommonPhraseLine> allFlightArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryFlightArea", "attribute1='" + loginBrandCode + "'");		
			List<BuCommonPhraseLine> allStoreArea = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"DeliveryStoreArea", "attribute1='" + loginBrandCode + "'");	
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true));
			multiList.put("allReportList", AjaxUtils.produceSelectorData(allReportList, "lineCode", "name", true, false));
			multiList.put("allDeliveryStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, true));
			multiList.put("allArrivalStoreAreas", AjaxUtils.produceSelectorData(allStoreArea, "lineCode","name", true, true));
            resultMap.put("multiList", multiList);
			resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
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
		log.info("getAJAXSearchPageData...");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", httpRequest.getProperty("loginBrandCode"));
			findObjs.put("orderTypeCode", httpRequest.getProperty("orderTypeCode"));
			findObjs.put("startOrderNo",  httpRequest.getProperty("startOrderNo"));
			findObjs.put("endOrderNo",  httpRequest.getProperty("endOrderNo"));
			findObjs.put("deliveryOrderNo", httpRequest.getProperty("deliveryOrderNo"));
			findObjs.put("startOrderDate", httpRequest.getProperty("startOrderDate"));
			findObjs.put("endOrderDate", httpRequest.getProperty("endOrderDate"));
			findObjs.put("moveEmployee", httpRequest.getProperty("moveEmployee"));
			findObjs.put("deliveryStoreArea", httpRequest.getProperty("deliveryStoreArea"));
			findObjs.put("arrivalStoreArea", httpRequest.getProperty("arrivalStoreArea"));
			findObjs.put("status", httpRequest.getProperty("status"));			
			findObjs.put("sortKey", "orderNo");
			findObjs.put("sortSeq", "Desc");
			
			// ==============================================================

			List<SoDeliveryMoveHead> soDeliveryMoveHeads = (
					List<SoDeliveryMoveHead>) soDeliveryMoveHeadDAO.findByMap(findObjs, iSPage,iPSize, soDeliveryHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("soDeliveryMoveHeads.size" + soDeliveryMoveHeads.size());

			if (soDeliveryMoveHeads != null && soDeliveryMoveHeads.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) soDeliveryMoveHeadDAO.findByMap(findObjs, -1, iPSize,
						soDeliveryHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆
				// INDEX
				String moveEmployeeName = new String("");
				for (SoDeliveryMoveHead head : soDeliveryMoveHeads) {
					head.setStatus(OrderStatus.getChineseWord(head.getStatus()));
					//List<SoDeliveryMoveLine> items = head.getSoDeliveryMoveLines();
					StringBuffer customerPoNoString = new StringBuffer("");
					//for (SoDeliveryMoveLine item : items) {
					//	customerPoNoString.append(item.getDeliveryOrderNo()+",");
					//}
					
					//if(StringUtils.hasText(customerPoNoString.toString()))
					//	soDeliveryHead.setCustomerPoNoString(customerPoNoString.toString().substring(0, customerPoNoString.length()-1));
					head.setDeliveryStoreName(this.getStoreArea(head.getDeliveryStoreArea()));
					head.setArrivalStoreName(this.getStoreArea(head.getArrivalStoreArea()));
					moveEmployeeName = UserUtils.getUsernameByEmployeeCode(head.getMoveEmployee());
					head.setMoveEmployeeName("unknow".equals(moveEmployeeName)?"":moveEmployeeName);
					
				}

				log.info("AjaxUtils.getAJAXPageData ");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_FIELD_SEARCH_DEFAULT_VALUES,
						soDeliveryMoveHeads, gridDatas, firstIndex, maxIndex));

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

	public List<Properties> updateStoreArea(Properties httpRequest) throws FormException, Exception {
		log.info("updateDeliveryStoreArea...");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String deliveryStoreArea = httpRequest.getProperty("deliveryStoreArea");
			String arrivalStoreArea = httpRequest.getProperty("arrivalStoreArea");
			
			System.out.println("deliveryStoreArea:"+deliveryStoreArea);
			System.out.println("arrivalStoreArea:"+arrivalStoreArea);
			
			SoDeliveryMoveHead head = getBeanByHeadId(headId);
			head.setDeliveryStoreArea(deliveryStoreArea);
			head.setArrivalStoreArea(arrivalStoreArea);
			List<SoDeliveryMoveLine> items = head.getSoDeliveryMoveLines();
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				SoDeliveryMoveLine item = (SoDeliveryMoveLine) iterator.next();
				item.setDeliveryStoreArea(deliveryStoreArea);
				item.setArrivalStoreArea(arrivalStoreArea);
			}
			baseDAO.update(head);
			result.add(properties);
			return result;
		} catch (Exception ex) {
			log.error("更新明細庫別發生錯誤，原因：" + ex.toString());
			throw new Exception("更新明細庫別失敗！");
		}
		
		//Map resultMap = new HashMap(0);
		//try{
		//	Object otherBean = parameterMap.get("vatBeanOther");
		//	String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		//	log.info("formId:"+formIdString);
		//	Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		//	SoDeliveryMoveHead form = (SoDeliveryMoveHead)soDeliveryMoveHeadDAO.findByPrimaryKey(SoDeliveryMoveHead.class, formId);
		//	if(null!=form){
		//		List<SoDeliveryMoveLine> soDeliveryMoveLines = new ArrayList(0);				
		//		form.setSoDeliveryMoveLines(soDeliveryMoveLines);
		//		soDeliveryMoveHeadDAO.save(form);
		//	}
		//} catch (Exception ex) {
		//	log.error("，原因：" + ex.toString());
		//	Map messageMap = new HashMap();
		//	messageMap.put("type", "ALERT");
		//	messageMap.put("message", "更新轉出庫別失敗，原因：" + ex.toString());
		//	messageMap.put("event1", null);
		//	messageMap.put("event2", null);
		//	resultMap.put("vatMessage", messageMap);
		//}
		//return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	public Long getHeadId(Object bean) throws FormException, Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的入提庫存移轉單主鍵為空值！");
		}
		return headId;
	}
	public HashMap updateAJAX(Map parameterMap) throws FormException, Exception {

		System.out.println("4.updateAJAXSoDeliveryMove");
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
			SoDeliveryMoveHead po = getActualSoDeliveryMove(formLinkBean);
			String identification = MessageStatus.getIdentification(po.getBrandCode(),
					po.getOrderTypeCode(), po.getOrderNo());
			
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
			log.info(po.getMoveEmployee());
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			String beforeStatus = po.getStatus();
			System.out.println("beforeStatus=" + beforeStatus);
			String formStatus = getFormStatus(formAction,beforeStatus);
			System.out.println("afterStatus=" + formStatus);
			po.setStatus(formStatus);
			po.setLastUpdatedBy(employeeCode);
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
	
	/*
	 * 暫存單號取實際單號並更新至Movement主檔
	 *
	 * @param headId @param loginUser @return ImPromotion @throws
	 * ObtainSerialNoFailedException @throws FormException @throws Exception
	 */
	public SoDeliveryMoveHeadDAO saveActualOrderNo(Long headId, String loginUser) throws ObtainSerialNoFailedException,
			FormException, Exception {
		log.info("2.saveActualOrderNo...head_id=" + headId);
		SoDeliveryMoveHead po = (SoDeliveryMoveHead)soDeliveryMoveHeadDAO.findByPrimaryKey(SoDeliveryMoveHead.class, headId);
		if (po == null) {
			throw new NoSuchObjectException("查無調撥單主鍵：" + headId + "的資料！");
		} else { // 取得正式的單號
			this.setOrderNo(po);
		}
		po.setLastUpdatedBy(loginUser);
		po.setLastUpdateDate(new Date());

		soDeliveryMoveHeadDAO.update(po);
		// imMovementHeadDAO.merge(imMovementPO);

		return soDeliveryMoveHeadDAO;
	}
	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(SoDeliveryMoveHead head) throws ObtainSerialNoFailedException {
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
	
	public void checkDeliveryMove(SoDeliveryMoveHead checkObj, String identification, List errorMsgs,
			 String beforeStatus) throws Exception {
		log.info("5.3.1 checkDeliveryMove...New");
		ArrayList removeArray = new ArrayList(0);
		String message = new String("");
		String user = checkObj.getLastUpdatedBy();
		String waitingWarehouseCode = new String("");
		Double itemCount = new Double(0);
		Double maxBoxCount = new Double(0);
		Double nowBoxCount = new Double(0);
		String itemCountField = new String("");
	
		try {
			List<SoDeliveryMoveLine> items = checkObj.getSoDeliveryMoveLines();
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
			int i = 0;
			System.out.println("before status:" + beforeStatus);
			System.out.println("current status:" + checkObj.getStatus());

			Map checkInfo = new HashMap();
			Map deliveryNo = new HashMap();
			Map collectMaps = new HashMap();
			String sameLineId = null;
			String sameStoreCode = null;
			Long bagCounts1= 0L;
			Long bagCounts2= 0L;
			Long bagCounts3= 0L;
			Long bagCounts4= 0L;
			Long bagCounts5= 0L;
			Long bagCounts6= 0L;
			Long bagCounts7= 0L;
			for (SoDeliveryMoveLine item : items) {
				
				i++;
				log.info("index_no:"+i);
				if (StringUtils.hasText(item.getDeliveryOrderNo())
						&& StringUtils.hasText(item.getIsDeleteRecord())
						&& AjaxUtils.IS_DELETE_RECORD_FALSE.equals(item.getIsDeleteRecord())) {
					// check

					sameLineId=this.collectDeliveryNo(item.getDeliveryOrderNo(), i, collectMaps);

					if( null == sameLineId){
						checkInfo = this.getDelivery(checkObj.getBrandCode(), "DZN",  item.getDeliveryOrderNo(), checkObj.getDeliveryStoreArea());
						message = (String) checkInfo.get("message");	
					}else{
						message = "入提單("+item.getDeliveryOrderNo()+")與第"+sameLineId+"筆資料相同";
					}
					
					if(StringUtils.hasText(message))
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					
					message="";
					
					if(null == item.getArrivalStoreCode() || "".equals(item.getArrivalStoreCode()))
						message = "入提單("+item.getDeliveryOrderNo()+")轉入儲位為空白";
					
					if(StringUtils.hasText(message))
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					
					message="";
					
					if (item.getDeliveryStoreCode()==item.getArrivalStoreCode())
						message = "入提單("+item.getDeliveryOrderNo()+")轉入儲位不得與轉出儲位相同";
					
					if(StringUtils.hasText(message))
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					
					//檢核庫存代號	
					if (checkObj.getDeliveryStoreArea()!=null){
						message="";
						
						if (item.getDeliveryStoreArea()!=null){
							if(!checkObj.getDeliveryStoreArea().equals(item.getDeliveryStoreArea())){
								message = "入提單("+item.getDeliveryOrderNo()+")不在"+checkObj.getDeliveryStoreArea()+":"+checkObj.getDeliveryStoreName()+"。轉出庫別有誤！";
							}
						}
						
						if(StringUtils.hasText(message))
							this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					}
					
					message="";
					log.info("轉入儲區:"+item.getArrivalStoreArea());;
					if (item.getArrivalStoreArea()!=null){
					    if(item.getDeliveryOrderNo().substring(0, 3).equals("DZN")){
						sameStoreCode = this.getStorageCheck(item.getArrivalStoreCode().substring(0, 1),"DeliveryStorageCheck");
					    }else if(item.getDeliveryOrderNo().substring(0, 3).equals("DKP")){
						sameStoreCode = this.getStorageCheck(item.getArrivalStoreCode().substring(0, 1),"DeliveryStorageCheck_HD");
					    }
						
						if (!item.getArrivalStoreArea().equals(sameStoreCode))
							message = "入提單("+item.getDeliveryOrderNo()+")轉入儲位（第1碼）與轉入庫別不相同";
					}
					else{
						message = "請選擇轉入庫別";
					}
						
					if(StringUtils.hasText(message))
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					
					message="";
					
					if (!item.getArrivalStoreArea().equals("E") & !item.getArrivalStoreArea().equals("D")){
						if (item.getArrivalStoreCode().length() != 5)
							message="入提單("+item.getDeliveryOrderNo()+")轉入儲位應為5碼";
					}
					
					if(StringUtils.hasText(message))
						this.messageHandle(MessageStatus.LOG_ERROR, identification, "第" + i + "項," + message, user, errorMsgs);
					
					item.setReturnMessage(message);
					
					bagCounts1= bagCounts1 + item.getBagCounts1();
					bagCounts2= bagCounts2 + item.getBagCounts2();
					bagCounts3= bagCounts3 + item.getBagCounts3();
					bagCounts4= bagCounts4 + item.getBagCounts4();
					bagCounts5= bagCounts5 + item.getBagCounts5();
					bagCounts6= bagCounts6 + item.getBagCounts6();
					bagCounts7= bagCounts7 + item.getBagCounts7();
	

				} else {
					if (OrderStatus.SAVE.equals(beforeStatus) || OrderStatus.REJECT.equals(beforeStatus)
							|| OrderStatus.UNCONFIRMED.equals(beforeStatus)) {
						removeArray.add(item);

					}
				}

			}
			
			log.info("bagCounts1:"+bagCounts1);
			log.info("bagCounts2:"+bagCounts2);
			log.info("bagCounts3:"+bagCounts3);
			log.info("bagCounts4:"+bagCounts4);
			log.info("bagCounts5:"+bagCounts5);
			log.info("bagCounts6:"+bagCounts6);
			log.info("bagCounts7:"+bagCounts7);
			
			checkObj.setBagCounts1(bagCounts1);
			checkObj.setBagCounts2(bagCounts2);
			checkObj.setBagCounts3(bagCounts3);
			checkObj.setBagCounts4(bagCounts4);
			checkObj.setBagCounts5(bagCounts5);
			checkObj.setBagCounts6(bagCounts6);
			checkObj.setBagCounts7(bagCounts7);
			Long totalBagCounts = checkObj.getBagCounts1()+checkObj.getBagCounts2()+checkObj.getBagCounts3()+
			                      checkObj.getBagCounts4()+checkObj.getBagCounts5()+checkObj.getBagCounts6()+
			                      checkObj.getBagCounts7();
			if (0L == totalBagCounts)
				this.messageHandle(MessageStatus.LOG_ERROR, identification, itemCountField + "袋數總數不可為零", user, errorMsgs);
			log.info("before remove size:" + items.size());
			for (int j = removeArray.size(); 0 < j; j--) {
				items.remove(removeArray.get(j - 1));
			}

			System.out.println("after remove size:" + items.size());
		} catch (Exception ex) {
			messageHandle(MessageStatus.LOG_ERROR, identification, ex.getMessage(), checkObj.getLastUpdatedBy(), errorMsgs);

		}

	}
	private String collectDeliveryNo(String deliveryNo, int lineId, Map collectMaps) throws Exception {
		log.info("collectDeliveryNo...." + deliveryNo);
		String key = deliveryNo;
		String sameLineId = null;
		log.info(key);
		try {
			if (collectMaps.containsKey(key)) {
				sameLineId = (String) collectMaps.get(key);
			} else {
				collectMaps.put(key, String.valueOf(lineId));
				sameLineId = null;
			}
		} catch (Exception ex) {
			throw new Exception("入提單確認是否重覆時發生錯誤，原因：" + ex.getMessage());

		}
		log.info(sameLineId);
		return sameLineId;
	}
	
	private void updateDeliveryStoreArea(SoDeliveryMoveHead modifyObj){
		log.info("updateDeliveryStoreArea...");
		String brandCode = modifyObj.getBrandCode();
		String deliveryStoreArea = modifyObj.getDeliveryStoreArea();
		String arrivalStoreArea = modifyObj.getArrivalStoreArea();
		List<SoDeliveryMoveLine> lines = modifyObj.getSoDeliveryMoveLines();
		for(SoDeliveryMoveLine line:lines){
			log.info("更新庫別:"+line.getDeliveryOrderNo());
			List<SoDeliveryHead> soDeliveryHeads = soDeliveryHeadDAO.findByOrderNo(brandCode, "DZN", line.getDeliveryOrderNo(), deliveryStoreArea, null);
			if(soDeliveryHeads.size() >0 ){
				SoDeliveryHead soDeliveryHead = soDeliveryHeads.get(0);
				soDeliveryHeadDAO.updateStoreAreaByHeadId(soDeliveryHead.getHeadId(), arrivalStoreArea,line.getArrivalStoreCode());
			}
		}
	}
	public String getStoreArea(String storeArea){
		BuCommonPhraseHead head = new BuCommonPhraseHead("DeliveryStoreArea");
		BuCommonPhraseLineId id = new BuCommonPhraseLineId(head,storeArea);
		BuCommonPhraseLine line = buCommonPhraseLineDAO.findById(id);
		if(null == line){
			return "";
		}else{
			return line.getName();
		}
	}
	
	public String getStoreAreaType(String storeArea){
		BuCommonPhraseHead head = new BuCommonPhraseHead("DeliveryStoreArea");
		BuCommonPhraseLineId id = new BuCommonPhraseLineId(head,storeArea);
		BuCommonPhraseLine line = buCommonPhraseLineDAO.findById(id);
		if(null == line){
			return "";
		}else{
			return line.getAttribute2();
		}
	}
	
	public String getStorageCheck(String FirstStorage,String headCode){
		BuCommonPhraseLine storeArea = buCommonPhraseLineDAO.findById(headCode, FirstStorage);
		
		if(null != storeArea){
			return storeArea.getAttribute2();
		}
		else{
			return "";
		}
	}
	
	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			SoDeliveryMoveHead soDeliveryMoveHead = (SoDeliveryMoveHead) soDeliveryMoveHeadDAO.findById(headId);
			if (soDeliveryMoveHead != null) {
				id = MessageStatus.getIdentification(soDeliveryMoveHead.getBrandCode(), soDeliveryMoveHead.getOrderTypeCode(),
						soDeliveryMoveHead.getOrderNo());
			} else {
				throw new NoSuchDataException("入提庫存移轉單主檔查無主鍵：" + headId + "的資料！");
			}

			return id;
		} catch (Exception ex) {
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	private String getFormStatus(String formAction, String beforeStatus){
		if (OrderStatus.FORM_SUBMIT.equals(formAction)){
			return OrderStatus.CLOSE;
		}else if (OrderStatus.VOID.equals(formAction)){
			return OrderStatus.VOID;
		}else{
			return OrderStatus.SAVE;
		}
	}
	  public List<Properties> updateAllSearchData(Map parameterMap) throws Exception {
		   log.info("updateAllSearchData....");
	   		Map resultMap = new HashMap(0);
	   		log.info(parameterMap.keySet().toString());
	   		
	   	    Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			System.out.println("aa");
			String timeScope = (String)PropertyUtils.getProperty(otherBean, AjaxUtils.TIME_SCOPE);
			System.out.println(AjaxUtils.TIME_SCOPE+":"+timeScope);
			String isAllClick = (String)PropertyUtils.getProperty(otherBean, "isAllClick");
			log.info("timeScope:"+timeScope);
			log.info("isAllClick:"+isAllClick);

		    String brandCode =(String)PropertyUtils.getProperty(otherBean,"loginBrandCode");// 品牌代號
		    String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean,"orderTypeCode");// 單別
		    String startOrderNo = (String)PropertyUtils.getProperty(formBindBean,"startOrderNo");
		    String endOrderNo = (String)PropertyUtils.getProperty(formBindBean,"endOrderNo");
		    String deliveryOrderNo = (String)PropertyUtils.getProperty(formBindBean,"deliveryOrderNo");// 出貨庫別
		    String startOrderDate = (String)PropertyUtils.getProperty(formBindBean,"startOrderDate");
		    String endOrderDate = (String)PropertyUtils.getProperty(formBindBean,"endOrderDate");
		    String moveEmployee = (String)PropertyUtils.getProperty(formBindBean,"moveEmployee");// 轉入庫別
		    String deliveryStoreArea = (String)PropertyUtils.getProperty(formBindBean,"deliveryStoreArea");
		    String arrivalStoreArea = (String)PropertyUtils.getProperty(formBindBean,"arrivalStoreArea");
		    String status = (String)PropertyUtils.getProperty(formBindBean,"status");

		    HashMap findObjs = new HashMap();
		    findObjs.put("brandCode",brandCode);
		    findObjs.put("orderTypeCode",orderTypeCode) ;
		    findObjs.put("startOrderNo",startOrderNo) ;
		    findObjs.put("endOrderNo",endOrderNo) ;
		    findObjs.put("deliveryOrderNo",deliveryOrderNo) ;
		    findObjs.put("startOrderDate",startOrderDate) ;
		    findObjs.put("endOrderDate",endOrderDate) ;
		    findObjs.put("moveEmployee",moveEmployee) ;
		    findObjs.put("deliveryStoreArea",deliveryStoreArea) ;
		    findObjs.put("arrivalStoreArea",arrivalStoreArea) ;
		    findObjs.put("status",status) ;
		    List allDataList = (List) soDeliveryMoveHeadDAO.findByMap(findObjs, -1,  -1, 
		    		soDeliveryMoveHeadDAO.QUARY_TYPE_SELECT_ALL).get("form");
		    
		    if(allDataList.size()>0)
		    	AjaxUtils.updateAllResult(timeScope,isAllClick,allDataList);

		    return AjaxUtils.parseReturnDataToJSON(resultMap);
	   }
	  public List<Properties> getReportConfig(Map parameterMap) throws Exception {
	  	log.info("getReportConfig...");
		try {
			String reportUrl = new String("");
			Map returnMap = new HashMap(0);
			Object otherBean = parameterMap.get("vatBeanOther");
			String reportFunctionCode = (String) PropertyUtils.getProperty(otherBean, "reportFunctionCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployeeCode");
			log.info("reportFunctionCode:"+reportFunctionCode);
			String brandCode= (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String startOrderNo= (String) PropertyUtils.getProperty(otherBean,"startOrderNo");
			String endOrderNo= (String) PropertyUtils.getProperty(otherBean,"endOrderNo");
			String sOrderDate = (String) PropertyUtils.getProperty(otherBean,"startOrderDate");
			String eOrderDate = (String) PropertyUtils.getProperty(otherBean,"startOrderDate");
			String startOrderDate= new String("");
			String endOrderDate= new String("");
			
			if(StringUtils.hasText(sOrderDate))
				startOrderDate= DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,sOrderDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
			else
				startOrderDate="";
			
			if(StringUtils.hasText(eOrderDate)) 
				endOrderDate= DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH,eOrderDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
			else
				endOrderDate="";	 
			
			String deliveryOrderNo= (String) PropertyUtils.getProperty(otherBean,"deliveryOrderNo");
			String deliveryStoreArea= (String) PropertyUtils.getProperty(otherBean,"deliveryStoreArea");
			String arrivalStoreArea= (String) PropertyUtils.getProperty(otherBean,"arrivalStoreArea");
			String moveEmployee= (String) PropertyUtils.getProperty(otherBean,"moveEmployee");
			String status= (String) PropertyUtils.getProperty(otherBean,"status");
			// CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			log.info("finish receipt data:"+brandCode);
			if ("T2_W00SO1005".equals(reportFunctionCode)) {
				log.info("reportFunctionCode:"+reportFunctionCode);
				parameters.put("prompt0", startOrderNo);
				parameters.put("prompt1", endOrderNo);
				parameters.put("prompt2", startOrderDate);
				parameters.put("prompt3", endOrderDate);
				parameters.put("prompt4", deliveryOrderNo);
				parameters.put("prompt5", moveEmployee);
				parameters.put("prompt6", deliveryStoreArea);
				parameters.put("prompt7", arrivalStoreArea);
				parameters.put("prompt8", status);
			}
			log.info("beging get url");
			reportUrl = SystemConfig.getReportURLByFunctionCode(brandCode, reportFunctionCode, loginEmployeeCode,
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
	  
	public void saveMoveLine(Map parameterMap, Map saveMap) throws Exception {
		log.info("saveMoveLine...");
		String message = new String("");
		Long indexNo = new Long(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = "DZN";//(String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String orderNo = (String) PropertyUtils.getProperty(otherBean, "deliveryOrderNo");
			String deliveryStoreArea = (String) PropertyUtils.getProperty(otherBean, "deliveryStoreArea");
			String arrivalStoreArea = (String) PropertyUtils.getProperty(otherBean, "arrivalStoreArea");
			String moveEmployee = (String) PropertyUtils.getProperty(otherBean, "moveEmployee");
			Map formResult = this.getDelivery(brandCode, orderTypeCode, orderNo, deliveryStoreArea);
			SoDeliveryHead form = (SoDeliveryHead) formResult.get("form");
			
			if(null != form){
				List<SoDeliveryMoveLine>lines =soDeliveryMoveLineDAO.findLineByDeliveryOrderNo(formId,orderTypeCode,orderNo);
				 if(lines.size()==0){
					 SoDeliveryMoveLine appendLine = new SoDeliveryMoveLine();
					 SoDeliveryMoveHead appendHead = new SoDeliveryMoveHead();
					 appendHead.setHeadId(formId);
					 appendLine.setDeliveryOrderType(orderTypeCode);
					 appendLine.setDeliveryOrderNo(orderNo);
					 appendLine.setSoDeliveryMoveHead(appendHead);
					 appendLine.setBagCounts1(form.getBagCounts1());
					 appendLine.setBagCounts2(form.getBagCounts2());
					 appendLine.setBagCounts3(form.getBagCounts3());
					 appendLine.setBagCounts4(form.getBagCounts4());
					 appendLine.setBagCounts5(form.getBagCounts5());
					 appendLine.setBagCounts6(form.getBagCounts6());
					 appendLine.setBagCounts7(form.getBagCounts7());
					 appendLine.setIsDeleteRecord("0");
					 appendLine.setIsLockRecord("0");
					 appendLine.setCreatedBy(moveEmployee);
					 appendLine.setCreationDate(new Date());
					 appendLine.setLastUpdatedBy(moveEmployee);
					 appendLine.setLastUpdateDate(new Date());
					 appendLine.setDeliveryStoreArea(deliveryStoreArea);
					 appendLine.setArrivalStoreArea(arrivalStoreArea);
					 appendLine.setDeliveryStoreCode(form.getStorageCode());
					 
					 indexNo =  soDeliveryMoveLineDAO.getMaxIndexNo(formId);
					 appendLine.setIndexNo(indexNo+1);					 
					 soDeliveryMoveLineDAO.save(appendLine);
					 message = "";
		
				 }else{
					 message = "此單號"+orderNo+"已存在於第"+lines.get(0).getIndexNo()+"筆資料，請重新輸入!";
					 indexNo = 0L;
				 }
			}else{
				message = (String) formResult.get("message");
				indexNo = 0L;
			}
			saveMap.put("message", message);
			saveMap.put("indexNo", indexNo);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("新增入提移轉單身時發生錯誤，原因："+ex.getMessage());
		}
		
	}
	
	public void deleteMoveLine(Map parameterMap, Map saveMap) throws Exception {
		log.info("deleteMoveLine...");
		String message = new String("");
		Long indexNo = new Long(0);
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = "DZN";//(String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String orderNo = (String) PropertyUtils.getProperty(otherBean, "deliveryOrderNo");
			String deliveryStoreArea = (String) PropertyUtils.getProperty(otherBean, "deliveryStoreArea");
			String moveEmployee = (String) PropertyUtils.getProperty(otherBean, "moveEmployee");
			Map formResult = this.getDelivery(brandCode, orderTypeCode, orderNo, deliveryStoreArea);

				 List<SoDeliveryMoveLine>lines =soDeliveryMoveLineDAO.findLineByDeliveryOrderNo(formId,orderTypeCode,orderNo);
				 Long lineId = new Long(0);
				 if(lines.size()!=0){
					 for(SoDeliveryMoveLine line :lines){
						 lineId = line.getLineId();
						 soDeliveryMoveLineDAO.delete(line);
					 }
					 message = "";
		
				 }else{
					 message = "此單號"+orderNo+"不存在，請重新輸入!";
					 indexNo = 0L;
				 }
				 soDeliveryMoveLineDAO.updateIndexNo(formId);
			saveMap.put("message", message);
			saveMap.put("indexNo", indexNo);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new NoSuchMethodException("新增入提移轉單身時發生錯誤，原因："+ex.getMessage());
		}
		
	}
	public List<Object[]> getBagCounts(Long formId) throws Exception {
		return soDeliveryMoveLineDAO.getBagCounts(formId);
	}
	
	public List<SoDeliveryMoveHead> executeBatchImportT2(List<SoDeliveryMoveHead> soDeliveryMoveHeads) throws Exception {
		
		List<SoDeliveryMoveHead> newSoDeliveryMoveHeads = new ArrayList<SoDeliveryMoveHead>(0);
		String orderType = "";
		try {
			log.info("executeBatchImportT2");
			
			if(soDeliveryMoveHeads != null){
				
				for (int i = 0; i < soDeliveryMoveHeads.size(); i++) {
					SoDeliveryMoveHead soDeliveryMoveHead = (SoDeliveryMoveHead) soDeliveryMoveHeads.get(i);
					
					//先檢查盤點入提單是否存在系統
					String strNoData = "";
					Long itemNo = 1L; 
					
					List<SoDeliveryMoveLine> soDeliveryMoveLines = soDeliveryMoveHead.getSoDeliveryMoveLines(); //盤點資料
					
					for (Iterator iterator = soDeliveryMoveLines.iterator(); iterator.hasNext();) {
						SoDeliveryMoveLine soDeliveryMoveLine = (SoDeliveryMoveLine) iterator.next();
						orderType = soDeliveryMoveLine.getDeliveryOrderNo().substring(0, 3);                                                                     
                                                //Steve 2015/6/23 for高雄入提移轉 原來
						//Map formResult = this.getDelivery(soDeliveryMoveHead.getBrandCode(), soDeliveryMoveLine.getDeliveryOrderNo().substring(0, 3), soDeliveryMoveLine.getDeliveryOrderNo(), "");
						Map formResult = this.getDelivery(soDeliveryMoveHead.getBrandCode(), "DZN", soDeliveryMoveLine.getDeliveryOrderNo(), "");
						SoDeliveryHead form = (SoDeliveryHead) formResult.get("form");
						
						if (form==null){
							if ("".equals(strNoData))
								strNoData = "第"+itemNo+"筆入提單號："+soDeliveryMoveLine.getDeliveryOrderNo()+ "</br>";
							else
								strNoData = strNoData + "第"+itemNo+"筆入提單號："+soDeliveryMoveLine.getDeliveryOrderNo()+ "</br>";
						}
						itemNo++;
					}
					
					if (!"".equals(strNoData))
						throw new Exception("查無入提單資料！</br>"+strNoData);					

					if(orderType.equals("DKP")){
						soDeliveryMoveHead.setOrderTypeCode("DHM");
					}else{
						soDeliveryMoveHead.setOrderTypeCode("DZM");
					}
					BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(soDeliveryMoveHead.getBrandCode(), soDeliveryMoveHead.getOrderTypeCode()));
					if (null == buOrderType)
						throw new Exception("取得 " + soDeliveryMoveHead.getBrandCode() + " " + soDeliveryMoveHead.getOrderTypeCode() + " 的單別失敗！");
					
					if (!StringUtils.hasText(soDeliveryMoveHead.getOrderNo())) {
						log.info("insertBatchHead");
						String sNo = insertBatchHead(soDeliveryMoveHead, buOrderType);
						updateBatchLine(soDeliveryMoveHead,sNo);
						newSoDeliveryMoveHeads.add(soDeliveryMoveHead);
					}
				}
				
			}
		} catch (Exception ex) {
			log.error("執行入提單資料批次匯入失敗，原因：" + ex.toString());
			throw ex;
		}
		return newSoDeliveryMoveHeads;
	}
	
	public String insertBatchHead(SoDeliveryMoveHead saveObj, BuOrderType buOrderType) throws Exception {
		
		try {
			String serialNo = buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode());
			if ("unknow".equals(serialNo))
				throw new Exception("取得" + saveObj.getBrandCode() + "-" + saveObj.getOrderTypeCode() + "單號失敗！");
			
			saveObj.setOrderNo(serialNo);
			saveObj.setStatus(OrderStatus.SAVE);
			saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
			saveObj.setCreationDate(saveObj.getLastUpdateDate());
			
			soDeliveryHeadDAO.save(saveObj);
			
			return serialNo;
			
		} catch (Exception e) {
			siProgramLogAction.createProgramLog(SO_DELIVERY_IMPORT, MessageStatus.LOG_ERROR, "SoDeliveryMoveImportDataT2", e.getMessage(), saveObj.getLastUpdatedBy());
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void updateBatchLine(SoDeliveryMoveHead saveObj,String orderNo)throws Exception {
		try {
			Long indexNo = 0L;

			SoDeliveryMoveHead soDeliveryMoveHead = soDeliveryMoveHeadDAO.findById(orderNo,saveObj.getOrderTypeCode());
			
			List<SoDeliveryMoveLine> soDeliveryMoveLines = saveObj.getSoDeliveryMoveLines(); //盤點資料
			
			Long bagCounts1 = 0L;
			Long bagCounts2 = 0L;
			Long bagCounts3 = 0L;
			Long bagCounts4 = 0L;
			Long bagCounts5 = 0L;
			Long bagCounts6 = 0L;
			Long bagCounts7 = 0L;
			
			for (Iterator iterator = soDeliveryMoveLines.iterator(); iterator.hasNext();) {
				
				SoDeliveryMoveLine soDeliveryMoveLine = (SoDeliveryMoveLine) iterator.next();
				                                                                     //Steve 2015/6/23 for高雄入提移轉 原來
				                                                                     //soDeliveryMoveLine.getDeliveryOrderNo().substring(0, 3)
				Map formResult = this.getDelivery(soDeliveryMoveHead.getBrandCode(), "DZN", soDeliveryMoveLine.getDeliveryOrderNo(), "");
				SoDeliveryHead form = (SoDeliveryHead) formResult.get("form");
				
				if (form!=null){
					 ++indexNo;
					 soDeliveryMoveLine.setSoDeliveryMoveHead(soDeliveryMoveHead);
					 soDeliveryMoveLine.setDeliveryOrderType(soDeliveryMoveLine.getDeliveryOrderNo().substring(0, 3));
					 soDeliveryMoveLine.setBagCounts1(form.getBagCounts1());
					 soDeliveryMoveLine.setBagCounts2(form.getBagCounts2());
					 soDeliveryMoveLine.setBagCounts3(form.getBagCounts3());
					 soDeliveryMoveLine.setBagCounts4(form.getBagCounts4());
					 soDeliveryMoveLine.setBagCounts5(form.getBagCounts5());
					 soDeliveryMoveLine.setBagCounts6(form.getBagCounts6());
					 soDeliveryMoveLine.setBagCounts7(form.getBagCounts7());
					 soDeliveryMoveLine.setDeliveryStoreArea(form.getStoreArea());
					 soDeliveryMoveLine.setDeliveryStoreCode(form.getStorageCode());
					 soDeliveryMoveLine.setArrivalStoreArea("A");
					 soDeliveryMoveLine.setIsDeleteRecord("0");
					 soDeliveryMoveLine.setIsLockRecord("0");
					 soDeliveryMoveLine.setCreatedBy(soDeliveryMoveHead.getMoveEmployee());
					 soDeliveryMoveLine.setCreationDate(new Date());
					 soDeliveryMoveLine.setLastUpdatedBy(soDeliveryMoveHead.getMoveEmployee());
					 soDeliveryMoveLine.setLastUpdateDate(new Date());
					 soDeliveryMoveLine.setIndexNo(indexNo); 
					 soDeliveryMoveLineDAO.save(soDeliveryMoveLine);
					 
					 bagCounts1 = bagCounts1 + form.getBagCounts1();
					 bagCounts2 = bagCounts2 + form.getBagCounts2();
					 bagCounts3 = bagCounts3 + form.getBagCounts3();
					 bagCounts4 = bagCounts4 + form.getBagCounts4();
					 bagCounts5 = bagCounts5 + form.getBagCounts5();
					 bagCounts6 = bagCounts6 + form.getBagCounts6();
					 bagCounts7 = bagCounts7 + form.getBagCounts7();
				}
			}
			
			soDeliveryMoveHead.setBagCounts1(bagCounts1);
			soDeliveryMoveHead.setBagCounts2(bagCounts2);
			soDeliveryMoveHead.setBagCounts3(bagCounts3);
			soDeliveryMoveHead.setBagCounts4(bagCounts4);
			soDeliveryMoveHead.setBagCounts5(bagCounts5);
			soDeliveryMoveHead.setBagCounts6(bagCounts6);
			soDeliveryMoveHead.setBagCounts7(bagCounts7);
			
			soDeliveryMoveHeadDAO.save(soDeliveryMoveHead);

		} catch (Exception e) {
			siProgramLogAction.createProgramLog(SO_DELIVERY_IMPORT, MessageStatus.LOG_ERROR, "SoDeliveryMoveImportDataT2", "明細匯入:"+e.getMessage(), saveObj.getLastUpdatedBy());
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * by headId 取得bean
	 */
	public SoDeliveryMoveHead getBeanByHeadId(Long headId) throws Exception {
		//Long headId = null;
		try {
			//headId =  StringUtils.hasText(headIdString)? Long.valueOf(headIdString):null;
			SoDeliveryMoveHead head = (SoDeliveryMoveHead)baseDAO.findByPrimaryKey(SoDeliveryMoveHead.class,headId);
			return head;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 流程起始
	 *
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static Object[] startProcess(SoDeliveryMoveHead form) throws Exception {
		log.info("startProcess");
		try {
			String packageId = "So_Delivery";
			String processId = "approval";
			// String version = "T2".equals(form.getBrandCode())?"3":"2";
			// String sourceReferenceType =
			// "T2".equals(form.getBrandCode())?"ImMovement (3)":"ImMovement
			// (2)";;

			String version = "20120312";
			String sourceReferenceType = "So_Delivery (1)";

			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("formId", form.getHeadId());
			context.put("orderType", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());

			Object[] object = ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);

			return object;
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("移轉單流程執行時發生錯誤，原因：" + e.toString());
			throw new ProcessFailedException(e.getMessage());
		}
	}
	
	/**
	 * 更新PROCESS_ID，避免重複起流程
	 *
	 * @param headId
	 * @param ProcessId
	 * @return
	 * @throws Exception
	 */
	public int updateProcessId(Long headId, Long ProcessId) throws Exception {
		int result = 0;
		try {
			result = soDeliveryMoveHeadDAO.updateProcessId(headId, ProcessId);
			System.out.println("更新資料筆數 ::: " + result + "筆");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("移轉單更新PROCESS_ID錯誤，原因：" + e.getMessage());
			throw new Exception("移轉單更新PROCESS_ID錯誤！");
		}
		return result;
	}
	
    /**
     * 完成任務工作
     * @param assignmentId
     * @param approveResult
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
		try{
		    HashMap context = new HashMap();
		    context.put("approveResult", approveResult);
	
		    return ProcessHandling.completeAssignment(assignmentId, context);
		}catch (Exception ex){
		    log.error("完成移轉工作任務失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("完成移轉工作任務失敗！"+ ex.getMessage());
		}
    }
}
