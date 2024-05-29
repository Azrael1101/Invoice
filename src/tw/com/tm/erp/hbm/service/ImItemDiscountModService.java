package tw.com.tm.erp.hbm.service;

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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.UniqueConstraintException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountModHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountModLineDAO;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.bean.BuGoalDeployLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.FiBudgetModLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModHead;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModLine;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class ImItemDiscountModService {
	private static final Log log = LogFactory
			.getLog(ImItemDiscountModService.class);
	private BaseDAO baseDAO;
	private BuBrandDAO buBrandDAO;
	private ImItemDiscountModHeadDAO imItemDiscountModHeadDAO;
	private ImItemDiscountModLineDAO imItemDiscountModLineDAO;
	private ImItemCategoryService imItemCategoryService;
	public static final String PROGRAM_ID = "IM_ITEM_DISCOUNT";

	private final String orderTypeCode = "IID"; // 單別IOC
	private final String orderNo = "";
	private SiProgramLogAction siProgramLogAction;
	private BuOrderTypeService buOrderTypeService;

	/**
	 * 商品折扣的查詢(Picker)欄位(Header)
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderTypeCode",
			"orderNo", "lastUpdateDate", "statusName", "headId", "brandCode" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "",
			"", "", "" };

	/**
	 * 商品折扣明細欄位
	 */
	public static final String[] GRID_FIELD_NAMES = {"indexNo", "vipTypeCode",
			"itemDiscountType", "beginDate", "endDate", "discount", "isLockRecord",
			"isDeleteRecord", "message" ,"lineId"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "", "", "",
			"", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,
			"",""};

	public static final int[] GRID_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG};

	public void setImItemDiscountModHeadDAO(
			ImItemDiscountModHeadDAO imItemDiscountModHeadDAO) {
		this.imItemDiscountModHeadDAO = imItemDiscountModHeadDAO;
	}

	public void setimItemDiscountModLineDAO(
			ImItemDiscountModLineDAO imItemDiscountModLineDAO) {
		this.imItemDiscountModLineDAO = imItemDiscountModLineDAO;
	}

	public void setImItemCategoryService(
			ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}


	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(ImItemDiscountModHead modifyObj) throws FormException,
			Exception {
		log.info("ImItemDiscountModService.create ");
		if (null != modifyObj) {
			// countHeadTotalAmount(modifyObj);
			if (modifyObj.getHeadId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		} else {
			throw new FormException("查無表單主檔資料");
		}
	}

	/**
	 * save tmp
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String saveTmp(ImItemDiscountModHead saveObj) throws FormException,
			Exception {
		log.info("ImItemDiscountModService.saveTmp");
		String tmpOrderNo = AjaxUtils.getTmpOrderNo();
		saveObj.setOrderNo(tmpOrderNo);
		// saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imItemDiscountModHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(ImItemDiscountModHead saveObj) throws FormException,
			Exception {
		log.info("ImItemDiscountModService.save ");
		doAllValidate(saveObj);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imItemDiscountModHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	public String save(ImItemDiscountModHead saveObj, String formAction,
			String loginUser) throws Exception {

		try {

			/* set enable value */
			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				System.out.println("1.check discount data");
			}
			System.out.println("2.save discount data");
			if (saveObj.getOrderNo() == null || "".equals(saveObj.getOrderNo())) {
				// 新增
				// 取得最新一筆流水號
				if (!(loginUser.equals(""))) {
					UserUtils.setUserAndDate(saveObj, loginUser);
				}
				System.out.println("3.get order No");
				String serialNo = ServiceFactory.getInstance().getBuOrderTypeService()
						.getOrderSerialNo(saveObj.getBrandCode(),
								saveObj.getOrderTypeCode());
				if (!serialNo.equals("unknow")) {
					saveObj.setOrderNo(serialNo);
					System.out.println("3.reset reserve");
					this.resetLineReserve(saveObj);
					imItemDiscountModHeadDAO.save(saveObj);
				} else {
					throw new ObtainSerialNoFailedException("取得"
							+ saveObj.getOrderTypeCode() + "單號失敗！");
				}
				return saveObj.getOrderTypeCode() + "-" + serialNo + "存檔成功！";
			} else {
				imItemDiscountModHeadDAO.update(saveObj);
				return saveObj.getOrderTypeCode() + "-" + saveObj.getOrderNo()
						+ "存檔成功！";
			}

		} catch (Exception ex) {
			log.error("商品折扣申請單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品折扣申請單存檔時發生錯誤，原因：" + ex.getMessage());
		}

	}

	public void resetLineReserve(ImItemDiscountModHead resetObj) {
		List<ImItemDiscountModLine> objItems = resetObj.getImItemDiscountModLines();
		for (ImItemDiscountModLine item : objItems) {
			item.setReserve1("Okay ");
			item.setReserve2("");
			item.setReserve3("");
			item.setReserve4("");
			item.setReserve5("");
		}
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(ImItemDiscountModHead updateObj) throws FormException,
			Exception {
		log.info("ImItemDiscountModService.update ");
		doAllValidate(updateObj);
		updateObj.setLastUpdateDate(new Date());
		imItemDiscountModHeadDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<ImItemDiscountModHead> find(HashMap findObjs) {
		log.info("ImItemDiscountModService.find ");
		return imItemDiscountModHeadDAO.find(findObjs);
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private boolean doAllValidate(ImItemDiscountModHead headObj)  //0130
			throws FormException, Exception {
		log.info("ImItemDiscountModHeadService.doAllValidate ");
		boolean isError = false;
		StringBuffer errorMessage = new StringBuffer();
		String identification = MessageStatus.getIdentification(headObj
				.getBrandCode(), headObj.getOrderTypeCode(), headObj.getOrderNo());
		if (checkDetailItemCode(headObj)) {
			errorMessage.append(MessageStatus.ERROR_NO_DETAIL + "<br>");
		}
		if (errorMessage.length() > 0) {
			isError = true;
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR,
					identification, errorMessage.toString(), headObj.getCreatedBy());
		}
		return isError;
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean checkDetailItemCode(ImItemDiscountModHead headObj) {
		log.info("ImItemDiscountModService.checkDetailItemCode ");
		List<ImItemDiscountModLine> lines = headObj.getImItemDiscountModLines();
		Iterator<ImItemDiscountModLine> it = lines.iterator();
		while (it.hasNext()) {
			ImItemDiscountModLine line = it.next();
			it.remove();
			lines.remove(line);
		}
		return lines.isEmpty();
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImItemDiscountModHead findById(Long headId) {
		return imItemDiscountModHeadDAO.findById(headId);
	}

	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
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
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean,AjaxUtils.SEARCH_KEY);
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

	public List<Properties> getAJAXPageData(Properties httpRequest)
			throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
			HashMap map = new HashMap();
			map.put("headId", headId);
			map.put("startPage", iSPage);
			map.put("pageSize", iPSize);
			// ======================取得頁面所需資料===========================
			List<ImItemDiscountModLine> imItemDiscountLines = imItemDiscountModLineDAO
					.findPageLine(headId, iSPage, iPSize);
			ImItemDiscountModHead form = findById(headId);
			if (imItemDiscountLines != null && imItemDiscountLines.size() > 0) {

				// 取得第一筆的INDEX
				Long firstIndex = imItemDiscountLines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = imItemDiscountModLineDAO.findPageLineMaxIndex(headId);
				// refreshItemData(map, cmDeclarationItems);
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES,
						GRID_FIELD_DEFAULT_VALUES, imItemDiscountLines, gridDatas,
						firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的商品折扣明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的商品折扣明細失敗！");
		}
	}

	/**
	 * ajax 取得商品折扣search分頁
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

			String loginBrandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

			String orderNo = httpRequest.getProperty("orderNo");
			String status = httpRequest.getProperty("status");
		
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.brandCode = :brandCode", loginBrandCode);
			findObjs.put(" and model.orderTypeCode = :orderTypeCode", orderTypeCode);
			findObjs.put(" and model.orderNo NOT LIKE :TMP", "TMP%");
			findObjs.put(" and model.orderNo = :orderNo", orderNo);
			findObjs.put(" and model.status = :status", status);
			// ==============================================================

			Map headMap = imItemDiscountModHeadDAO.search(
					"ImItemDiscountModHead as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<ImItemDiscountModHead> heads = (List<ImItemDiscountModHead>) headMap
					.get(BaseDAO.TABLE_LIST);

			if (heads != null && heads.size() > 0) {

				this.setStatusName(heads);

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imItemDiscountModHeadDAO.search(
						"ImItemDiscountModHead as model",
						"count(model.headId) as rowCount", findObjs,
						"order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT).get(
						BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, heads,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,
						gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的商品折扣查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的商品折扣查詢失敗！");
		}
	}

	/**
	 * 設定中文狀態名稱
	 * 
	 * @param imAdjustmentHeads
	 */
	private void setStatusName(List<ImItemDiscountModHead> heads) {
		for (ImItemDiscountModHead head : heads) {
			head.setStatusName(OrderStatus.getChineseWord(head.getStatus()));
		}
	}

	/*
	 * 
	 * headId 是由 hibernate 的 sequence 產生，參考 *.hbm.xml 。存檔之後才會產生 headId 在此取出。
	 */
	public Map executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,"loginEmployeeCode");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
	
			String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");

			Long formId = StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
			List<ImItemDiscountModHead> imItemDiscountModHead = formId != null ? imItemDiscountModHeadDAO.findByProperty( "ImItemDiscountModHead", "headId", formId) : null; 
			Map multiList = new HashMap(0);

			if(imItemDiscountModHead != null && imItemDiscountModHead.size() > 0){
				String dates[] ;
				ImItemDiscountModHead form = imItemDiscountModHead.get(0);
				String brandCode = form.getBrandCode();
				
				resultMap.put("form", form);
				resultMap.put("brandName",buBrandDAO.findById(brandCode).getBrandName());
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus())); // 跨 table 選取基本檔
			    String employeeName = UserUtils.getUsernameByEmployeeCode(form.getCreatedBy());
			    resultMap.put("createdBy",form.getCreatedBy());
			    resultMap.put("createdByName",employeeName);
			    resultMap.put("creationDate",form.getCreationDate());
			      if(form.getCreationDate() != null){
				      	dates = form.getCreationDate().toString().split("-");
				      	resultMap.put("creationDate",dates[0]+"/"+dates[1]+"/"+dates[2].substring(0,2));
							}
				List<BuCommonPhraseLine> allVipTypeCodes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
									"id.buCommonPhraseHead.headCode", "attribute1" }, new Object[] {"VIPType", brandCode }, "indexNo");
				multiList.put("allVipTypeCodes", AjaxUtils.produceSelectorData(allVipTypeCodes, "lineCode", "name", true, true));
			}else{
				ImItemDiscountModHead form = this.getActualHead(otherBean, resultMap);
				resultMap.put("form", form);
				resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus())); // 跨 table 選取基本檔
			    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
			    resultMap.put("createdBy",loginEmployeeCode);
			    resultMap.put("createdByName",employeeName);
			    resultMap.put("creationDate", new Date() );
				List<BuCommonPhraseLine> allVipTypeCodes = baseDAO.findByProperty(
						"BuCommonPhraseLine", new String[] {
								"id.buCommonPhraseHead.headCode", "attribute1" }, new Object[] {
								"VIPType", loginBrandCode }, "indexNo");
				multiList.put("allVipTypeCodes", AjaxUtils.produceSelectorData(allVipTypeCodes, "lineCode", "name", true, true));
			}


//			String orderType = imItemDiscountModHead.getOrderTypeCode();


			List<BuCommonPhraseLine> allItemDiscountTypes = baseDAO.findByProperty(
					"BuCommonPhraseLine", new String[] {
							"id.buCommonPhraseHead.headCode", "enable" }, new Object[] {
							"VipDiscount", "Y" }, "indexNo");
//			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(
//					imItemDiscountModHead.getBrandCode(), "IOC");

			multiList.put("allItemDiscountTypes", AjaxUtils.produceSelectorData(allItemDiscountTypes, "lineCode", "name", true, true));
//			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true,
//					orderType != null ? orderType : ""));

			resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("商品折扣初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 透過formId取得商品折扣 in initial
	 * 
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	private ImItemDiscountModHead getActualHead(Object otherBean, Map resultMap)
			throws Exception {
		ImItemDiscountModHead imItemDiscountModHead = null;
		try {
			Object formIdObj = PropertyUtils.getProperty(otherBean, "formId");
			String formIdString = "";
			if (formIdObj != null) {
				formIdString = (String) formIdObj;
			}
			log.info("getActualHead formIdString: " + formIdString);
			Long formId = null;
			if (!formIdString.equals("undefined")) {
				formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString): null;
			}
			log.info("getActualHead formId: " + formId);

			imItemDiscountModHead = null == formId ? this.executeNew(otherBean): this.findImItemDiscountModHeadById(formId);

		} catch (Exception e) {
			log.error("取得商品折扣主檔失敗,原因:" + e.toString());
			throw new Exception("取得商品折扣主檔失敗,原因:" + e.toString());
		}
		return imItemDiscountModHead;
	}

	/**
	 * 依據primary key為查詢條件，取得商品折扣主檔
	 * 
	 * @param headId
	 * @return ImItemDiscountModHead
	 * @throws Exception
	 */
	public ImItemDiscountModHead findImItemDiscountModHeadById(Long headId)
			throws Exception {

		try {
			ImItemDiscountModHead itemDiscountHead = (ImItemDiscountModHead) imItemDiscountModHeadDAO
					.findByPrimaryKey(ImItemDiscountModHead.class, headId);

			return itemDiscountHead;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢商品折扣主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢商品折扣主檔時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 取得新的商品折扣表頭
	 * 
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public ImItemDiscountModHead executeNew(Object otherBean) throws Exception {
		ImItemDiscountModHead form = new ImItemDiscountModHead();
		try {
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,
					"loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean,
					"loginEmployeeCode");

			form.setOrderTypeCode(orderTypeCode);
			form.setOrderNo(orderNo);
			form.setBrandCode(loginBrandCode);
			form.setStatus(OrderStatus.SAVE);
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());

			this.saveHead(form);

		} catch (Exception e) {
			log.error("建立新商品折扣主檔失敗,原因:" + e.toString());
			throw new Exception("建立新商品折扣主檔失敗,原因:" + e.toString());
		}
		return form;
	}

	/**
	 * 商品折扣存檔,取得暫存碼
	 * 
	 * @param imItemDiscountModHead
	 * @throws Exception
	 */
	public void saveHead(ImItemDiscountModHead imItemDiscountModHead)
			throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo(); // 產生暫存單號
			// imItemDiscountModHead.setReserve5(tmpOrderNo);
			imItemDiscountModHead.setOrderNo(tmpOrderNo); // 寫入暫存單號
			imItemDiscountModHead.setLastUpdateDate(new Date());
			imItemDiscountModHead.setCreationDate(new Date());
			imItemDiscountModHeadDAO.save(imItemDiscountModHead);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存商品折扣發生錯誤，原因：" + ex.getMessage());
		}
	}

	public ImItemDiscountModHead findImItemDiscountModHead(long formId,
			Map returnMap) throws FormException, Exception {
		Map multiList = new HashMap(0);
		try {
			ImItemDiscountModHead form = findById(formId);
			if (form != null) {
				returnMap.put("statusName", OrderStatus
						.getChineseWord(form.getStatus()));
				returnMap.put("lastUpdatedByName", UserUtils
						.getUsernameByEmployeeCode(form.getLastUpdatedBy()));
				returnMap.put("form", form);
				List<ImItemCategory> category01s = imItemCategoryService
						.findByCategoryType(form.getBrandCode(), "CATEGORY01");
				multiList.put("category01s", AjaxUtils.produceSelectorData(category01s,
						"categoryCode", "categoryName", false, true));
				List<ImItemCategory> category02s = imItemCategoryService
						.findByCategoryType(form.getBrandCode(), "CATEGORY02");
				multiList.put("category02s", AjaxUtils.produceSelectorData(category02s,
						"categoryCode", "categoryName", false, true));
				returnMap.put("multiList", multiList);
				return form;
			} else {
				throw new NoSuchObjectException("查無商品折扣主鍵：" + formId + "的資料！");
			}
		} catch (FormException fe) {
			log.error("查詢商品折扣失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("查詢商品折扣發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢商品折扣發生錯誤！");
		}
	}

	/**
	 * 判斷是否為新資料，並將商品折扣資料新增或更新至商品折扣主檔、明細檔
	 * 
	 * @param imItemDiscountModHead
	 * @param loginUser
	 * @return String
	 * @throws FormException
	 * @throws Exception
	 */
	private String insertOrUpdateImItemDiscount(ImItemDiscountModHead imItemDiscountModHead, String loginUser)
			throws FormException, Exception {

		UserUtils.setOpUserAndDate(imItemDiscountModHead, loginUser);
		UserUtils.setUserAndDate(imItemDiscountModHead.getImItemDiscountModLines(),loginUser);
		if (imItemDiscountModHead.getHeadId() == null) {
			insertImItemDiscount(imItemDiscountModHead);
		} else {
			modifyImItemDiscount(imItemDiscountModHead);
		}

		return "商品折扣：" + imItemDiscountModHead.getHeadId() + "存檔成功！";
	}

	/**
	 * 新增至商品折扣主檔、明細檔
	 * 
	 * @param saveObj
	 */
	private void insertImItemDiscount(Object saveObj) {
		imItemDiscountModHeadDAO.save(saveObj);
	}

	/**
	 * 更新至商品折扣主檔、明細檔
	 * 
	 * @param updateObj
	 */
	private void modifyImItemDiscount(Object updateObj) {
		imItemDiscountModHeadDAO.update(updateObj);
	}

	/**
	 * 更新商品折扣主檔、明細檔的Status
	 * 
	 * @param imItemDiscountModHeadId
	 * @param status
	 * @return String
	 * @throws Exception
	 */
	public String updateImItemDiscountStatus(Long imItemDiscountModHeadId,
			String status) throws Exception {

		try {
			ImItemDiscountModHead imItemDiscountModHead = (ImItemDiscountModHead) imItemDiscountModHeadDAO
					.findByPrimaryKey(ImItemDiscountModHead.class,
							imItemDiscountModHeadId);
			if (imItemDiscountModHead != null) {
				imItemDiscountModHead.setStatus(status);
				imItemDiscountModHead.setLastUpdateDate(new Date());
				List<ImItemDiscountModLine> lcLines = imItemDiscountModHead
						.getImItemDiscountModLines();
				if (lcLines != null && lcLines.size() > 0) {
					for (ImItemDiscountModLine lcLine : lcLines) {
						// lcLine.setStatus(status);
						lcLine.setLastUpdateDate(new Date());
					}
				}
				modifyImItemDiscount(imItemDiscountModHead);
				return "Success";
			} else {
				throw new NoSuchDataException("商品折扣主檔查無主鍵：" + imItemDiscountModHeadId
						+ "的資料！");
			}
		} catch (Exception ex) {
			log.error("更新商品折扣狀態時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新商品折扣狀態時發生錯誤，原因：" + ex.getMessage());

		}
	}

	/**
	 * 商品折扣依formAction取得下個狀態，狀態有2個來源，使用者按下按鈕，或表單狀態(從資料庫抓取)。所以， 
	 * formAction =OrderStatus.FORM_SAVE; head.getStatus = OrderStatus.SAVE 兩者的意義不同。
	 */
	public void setNextStatus(ImItemDiscountModHead head, String formAction,
			String approvalResult) {
		if (OrderStatus.FORM_SAVE.equals(formAction)) {
			head.setStatus(OrderStatus.SAVE);
		} else if ((OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction))) {
			if (OrderStatus.SAVE.equals(head.getStatus())|| OrderStatus.REJECT.equals(head.getStatus())) {
				head.setStatus(OrderStatus.SIGNING);
			} else if (OrderStatus.SIGNING.equals(head.getStatus())) {
				if("true".equals(approvalResult)){
//					head.setStatus(OrderStatus.FINISH);
				}else{
					head.setStatus(OrderStatus.REJECT);
				}
			}
		} else if (OrderStatus.FORM_VOID.equals(formAction)) {
			head.setStatus(OrderStatus.VOID);
		}
	}

	/**
	 * 依 formAction 取得商品折扣主檔下個狀態
	 * 
	 * @param head
	 * @param formAction
	 * @return
	 */
	private String setHeadStatus(ImItemDiscountModHead head, String formAction) {
		String status = OrderStatus.UNKNOW;

		if (OrderStatus.FORM_SAVE.equals(formAction)) {
			head.setStatus(OrderStatus.SAVE);
			status = OrderStatus.SAVE;
		} else if ((OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG"
				.equals(formAction))
				&& OrderStatus.SAVE.equals(head.getStatus())) {

			head.setStatus(OrderStatus.FINISH);
			status = OrderStatus.FINISH;

		} else if ((OrderStatus.CLOSE.equals(formAction) || "SUBMIT_BG"
				.equals(formAction))
				&& OrderStatus.FINISH.equals(head.getStatus())) {

			head.setStatus(OrderStatus.CLOSE);
			status = OrderStatus.CLOSE;

		} else if (OrderStatus.FORM_VOID.equals(formAction)) {
			head.setStatus(OrderStatus.VOID);
			status = OrderStatus.VOID;
		}

		log.info("formAction = " + formAction);
		log.info("head.getStatus() = " + head.getStatus());

		return status;
	}

	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap (
	 *          formLinkBean:設為 back: false 的值皆在此; beanOther:自訂變數，eg.頁碼…
	 *          formBindBean:真正有對應到資料庫欄位的變數 )
	 * @return
	 */
	public Map updateImItemDiscountBean(Map parameterMap) throws FormException,
			Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			log.info("updateImItemDiscount formBindBean:" + formBindBean);
			Object formLinkBean = parameterMap.get("vatBeanFormLink"); // 空值?
			log.info("updateImItemDiscount formLinkBean:" + formLinkBean);

			// 取得欲更新的bean
			ImItemDiscountModHead imItemDiscountModHeadPO = getActualImItemDiscount(formLinkBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imItemDiscountModHeadPO);

			resultMap.put("entityBean", imItemDiscountModHeadPO);

			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("前端資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 透過headId取得商品折扣
	 * 
	 * @param otherBean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public ImItemDiscountModHead getActualImItemDiscount(Object otherBean)
			throws FormException, Exception {
		log.info("getActualImItemDiscount otherBean:" + otherBean);

		ImItemDiscountModHead imItemDiscountModHead = null;
		log.info("getActualImItemDiscount headId:"
				+ PropertyUtils.getProperty(otherBean, "headId"));
		String id = (String) PropertyUtils.getProperty(otherBean, "headId");
		log.info("getActualImItemDiscount id:" + id);
		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			log.info("getActualImItemDiscount headId:"
					+ PropertyUtils.getProperty(otherBean, "headId"));
			imItemDiscountModHead = this.findImItemDiscountModHeadById(headId);
			if (imItemDiscountModHead == null) {
				throw new NoSuchObjectException("查無商品折扣主鍵：" + headId + "的資料！");
			}
		} else {
			throw new ValidationErrorException("傳入的商品折扣主鍵為空值！");
		}
		return imItemDiscountModHead;
	}

	/**
	 * 將商品折扣資料新增或更新至商品折扣主檔、明細檔
	 * 
	 * @param imItemDiscountModHead
	 * @param conditionMap
	 * @param loginUser
	 * @return String
	 * @throws Exception
	 */
	public String saveOrUpdateImItemDiscount(
			ImItemDiscountModHead imItemDiscountModHead, HashMap conditionMap,
			String loginUser) throws Exception {
		log.info("ImItemDiscountModService.saveOrUpdateImItemDiscount");
		try {
			return insertOrUpdateImItemDiscount(imItemDiscountModHead, loginUser);
		} catch (FormException fe) {
			log.error("商品折扣存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("商品折扣存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品折扣存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * ajax 更新商品折扣明細的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest)
			throws Exception {
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));		
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

			if (headId == null) {
				throw new ValidationErrorException("傳入的商品折扣主鍵為空值！");
			}
			String errorMsg = null;
			ImItemDiscountModHead imItemDiscountModHead = getActualHead(headId);
			// 將 JSON String 資料轉成 List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = 0;
			Object object = imItemDiscountModHeadDAO.findPageLineMaxIndex("ImItemDiscountModLine","imItemDiscountModHead.",headId);
			// 考慮狀態
			if(object != null)
				indexNo=((ImItemDiscountModLine)object).getIndexNo().intValue();	
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					String vipTypeCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);

					if (StringUtils.hasText(vipTypeCode)) { // 當 itemCode 不為 null
		    		ImItemDiscountModLine imItemDiscountLine = imItemDiscountModLineDAO.findById(lineId);			
						Date date = new Date();
						if (imItemDiscountLine != null ) { // 若有記錄就變更
							AjaxUtils.setPojoProperties(imItemDiscountLine, upRecord,
									GRID_FIELD_NAMES, GRID_FIELD_TYPES); // 寫回前端
							imItemDiscountLine.setLastUpdatedBy(loginEmployeeCode);
							imItemDiscountLine.setLastUpdateDate(date);
							imItemDiscountModLineDAO.update(imItemDiscountLine);

						} else { // 若無記錄就新增
							indexNo++;
							imItemDiscountLine = new ImItemDiscountModLine();

							AjaxUtils.setPojoProperties(imItemDiscountLine, upRecord,
									GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							imItemDiscountLine
									.setImItemDiscountModHead(imItemDiscountModHead);
							imItemDiscountLine.setCreateBy(loginEmployeeCode);
							imItemDiscountLine.setCreationDate(date);
							imItemDiscountLine.setLastUpdatedBy(loginEmployeeCode);
							imItemDiscountLine.setLastUpdateDate(date);
							imItemDiscountLine.setIndexNo(Long.valueOf(indexNo));
							imItemDiscountModLineDAO.save(imItemDiscountLine);
						}
					}
					
				}
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新商品折扣明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新商品折扣明細失敗！");
		}
	}

	/**
	 * 檢核,存取商品折扣主檔,明細檔,重新設定狀態
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXImItemDiscount( Map parameterMap, String formAction)
			throws Exception {
		Map resultMap = new HashMap();
		List errorMsgs = null;
		String resultMsg = null;
		ImItemDiscountModHead imItemDiscountModHead = null;
		try {

			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
					
			String employeeCode = (String) PropertyUtils.getProperty(otherBean,	"loginEmployeeCode");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean,"approvalResult");
			
			imItemDiscountModHead = this.getActualImItemDiscount(formLinkBean);
  		// 檢核
			if( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
				this.deleteLine(imItemDiscountModHead); 
				errorMsgs  = this.checkedImItemDiscountMod(parameterMap);
			}
			
			if (errorMsgs == null || errorMsgs.size() == 0) {
				
				// 設定單號
				this.setOrderNo(imItemDiscountModHead);
				// 成功則設定下個狀態
				this.setNextStatus(imItemDiscountModHead, formAction, approvalResult);
				// 存檔
				if( ( OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ) ){
					this.insertOrUpdateImItemDiscount(imItemDiscountModHead, employeeCode);
				}
				resultMsg = imItemDiscountModHead.getOrderTypeCode()+"-"+ imItemDiscountModHead.getOrderNo()+ "存檔成功！是否繼續新增？";
			} else if (errorMsgs.size() > 0) {
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
			}
			resultMap.put("entityBean", imItemDiscountModHead);
			resultMap.put("resultMsg", resultMsg);
		} catch (ValidationErrorException ve) {
			log.error("商品折扣檢核時發生錯誤，原因：" + ve.toString());
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} catch (Exception e) {
			log.error("商品折扣存檔時發生錯誤，原因：" + e.toString());
			throw new Exception("商品折扣存檔時發生錯誤，原因：" + e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 取單號後更新更新主檔
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws FormException
	 * @throws Exception
	 */
	public Map updateImItemDiscountWithActualOrderNO(Map parameterMap)
			throws FormException, Exception {

		Map resultMap = new HashMap();
		try {

			resultMap = this.updateImItemDiscountBean(parameterMap);
			ImItemDiscountModHead discountHead = (ImItemDiscountModHead) resultMap
					.get("entityBean");

			// 刪除於SI_PROGRAM_LOG的原識別碼資料
			String identification = MessageStatus.getIdentification(discountHead
					.getBrandCode(), discountHead.getOrderTypeCode(), discountHead
					.getOrderNo());
			siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

			this.setOrderNo(discountHead);
			String resultMsg = discountHead.getOrderNo() + "存檔成功！是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
		} catch (FormException fe) {
			log.error("商品折扣存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("商品折扣存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品折扣存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public SiProgramLogAction getSiProgramLogAction() {
		return siProgramLogAction;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 * 
	 * @param head
	 */
	private void setOrderNo(ImItemDiscountModHead head)
			throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head
						.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode()
							+ "-" + head.getOrderTypeCode() + "單號失敗！");
				else
					head.setOrderNo(serialNo);
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode()
						+ "單號失敗！");
			}
		}
	}

	/**
	 * 啟動流程
	 * 
	 * @param form
	 * @return result[0]流程代號, result[1]活動代號, result[2]活動名稱
	 * @throws ProcessFailedException
	 */
	public static Object[] startProcess(ImItemDiscountModHead form)
			throws ProcessFailedException {
		String packageId = null;
		String processId = null;
		String version = null;
		String sourceReferenceType = null;
		try {

			packageId = "Im_Item_Discount"; // 看 cEAP 的目錄名稱
			processId = "approval"; // 不是想像中的 process id ，這裡指的是 cEAP 中的路徑
			version = "20100106";
			sourceReferenceType = "(20100106)";

			HashMap context = new HashMap();
			context.put("formId", form.getHeadId());
			context.put("brandCode", form.getBrandCode());
			context.put("orderTypeCode", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());
			return ProcessHandling.start(packageId, processId, version,
					sourceReferenceType, context);
		} catch (Exception ex) {
			log.error("商品折扣流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("商品折扣流程啟動失敗！");
		}
	}

	/**
	 * 完成任務工作
	 * 
	 * @param assignmentId
	 * @param approveResult
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] completeAssignment(long assignmentId,
			boolean approveResult) throws ProcessFailedException {

		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);

			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception ex) {
			log.error("完成商品折扣任務失敗，原因：" + ex.toString());
			throw new ProcessFailedException("完成商品折扣任務失敗！");
		}
	}

	/**
	 * 查詢商品折扣初始化
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean,"orderTypeCode");

			List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode, "IOC");
			Map multiList = new HashMap(0);
			multiList.put("allOrderTypeCodes", AjaxUtils.produceSelectorData(
					allOrderTypeCodes, "orderTypeCode", "name", true, true,
					orderTypeCode != null ? orderTypeCode : ""));

			resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("查詢商品折扣初始化失敗，原因：" + ex.toString());
			throw new Exception("查詢商品折扣初始化失敗，原因：" + ex.toString());

		}
	}

	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			ImItemDiscountModHead head = findById(headId);
			if (head != null) {
				id = MessageStatus.getIdentification(head.getBrandCode(), head
						.getOrderTypeCode(), head.getOrderNo());
			} else {
				throw new NoSuchDataException("商品折扣檔查無主鍵：" + headId + " 的資料！");
			}
			return id;
		} catch (Exception ex) {
			log.error("查詢商品折扣時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢商品折扣時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * ajax 取得商品折扣search分頁
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXApartSearchPageData(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================
			String loginBrandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode"); // 單別
			String orderNo = httpRequest.getProperty("orderNo"); // 單號
			Date lastUpdateDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("lastUpdateDate"));
			String status = httpRequest.getProperty("status");


			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.brandCode = :brandCode", loginBrandCode);
			findObjs.put(" and model.orderTypeCode = :orderTypeCode", orderTypeCode);
			findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
			findObjs.put(" and model.orderNo like :orderNo", "%"+orderNo+"%");
			findObjs.put(" and model.lastUpdateDate like :lastUpdateDate",lastUpdateDate);
			findObjs.put(" and model.status = :status", status);

			// ==============================================================

			Map imItemDiscountModHeadMap = imItemDiscountModHeadDAO.search(
					"ImItemDiscountModHead as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<ImItemDiscountModHead> imItemDiscountModHeads = (List<ImItemDiscountModHead>) imItemDiscountModHeadMap
					.get(BaseDAO.TABLE_LIST);

			if (imItemDiscountModHeads != null && imItemDiscountModHeads.size() > 0) {

				this.setItemDiscountModStatusName(imItemDiscountModHeads);

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imItemDiscountModHeadDAO.search(
						"ImItemDiscountModHead as model",
						"count(model.headId) as rowCount", findObjs,
						"order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT).get(
						BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						imItemDiscountModHeads, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,
						gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的商品折扣查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的商品折扣查詢失敗！");
		}
	}

	/**
	 * 設定中文狀態名稱
	 * 
	 * @param imItemDiscountHeads
	 */
	private void setItemDiscountModStatusName(
			List<ImItemDiscountModHead> imItemDiscountModHeads) {
		for (ImItemDiscountModHead imItemDiscountModHead : imItemDiscountModHeads) {
			imItemDiscountModHead.setStatusName(OrderStatus
					.getChineseWord(imItemDiscountModHead.getStatus()));
		}
	}
	/**
	 * 將商品折扣明細表 mark 為刪除的刪掉
	 * @param head
	 */
	private void deleteLine(ImItemDiscountModHead head){
			
        List<ImItemDiscountModLine> imItemDiscountModLines = head.getImItemDiscountModLines();
       
            for(int i = imItemDiscountModLines.size() - 1; i >= 0; i--){
            	ImItemDiscountModLine imItemDiscountModLine = imItemDiscountModLines.get(i);         
            	if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(imItemDiscountModLine.getIsDeleteRecord())){
	        		imItemDiscountModLines.remove(imItemDiscountModLine);
	        		imItemDiscountModLineDAO.delete(imItemDiscountModLine);
	        	}
          }
        
	}
	/**
	 * 檢核商品折扣主檔,明細檔
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List checkedImItemDiscountMod(Map parameterMap)throws FormException, Exception{
		List errorMsgs = new ArrayList(0);
		String message = null;
		String identification = null;
		ImItemDiscountModHead imItemDiscountModHead = null;
    	try{
    	
	    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
	    	
	    	imItemDiscountModHead = this.getActualImItemDiscount(formLinkBean);
	    	
    	   String status = imItemDiscountModHead.getStatus();
    	   if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
		    	
    	  	 identification = MessageStatus.getIdentification(imItemDiscountModHead.getBrandCode(), 
    	  	 imItemDiscountModHead.getOrderTypeCode(), imItemDiscountModHead.getOrderNo());
		    	
    	  	 siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
	    	
    	  	 validateImItemDiscountMod(imItemDiscountModHead, PROGRAM_ID, identification, errorMsgs, formLinkBean ); 
		    	
			}
    	
    	}catch (Exception ex) {
    	    message = "商品折扣檢核失敗，原因：" + ex.toString();
			siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, imItemDiscountModHead.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);	
    	}
    	return errorMsgs;
	}
	private void validateImItemDiscountMod(ImItemDiscountModHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
		validteLine(head, programId, identification, errorMsgs);
	}
	/**
	 * 檢核商品折扣明細檔
	 * @param head
	 * @param programId
	 * @param identification
	 * @param errorMsgs
	 * @throws ValidationErrorException
	 * @throws NoSuchObjectException
	 */
	private void validteLine(ImItemDiscountModHead head, String programId,
		String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
		String message = null;
		String tabName = "主檔資料頁籤";
		try{
				List lines = head.getImItemDiscountModLines();
				int size = lines.size();
				if( size > 0 ){
						for (int i = 0; i < size; i++) {
							ImItemDiscountModLine line = (ImItemDiscountModLine) lines.get(i);
							String vipTypeCode = line.getVipTypeCode();
							String itemDiscountType = line.getItemDiscountType();
							Date beginDate = line.getBeginDate();
							Date endDate = line.getEndDate();
							Long discount = line.getDiscount();
						
							if(!StringUtils.hasText(vipTypeCode))
							{
								message="請選擇第"+(i+1)+"筆的折扣卡別";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
							if(!StringUtils.hasText(itemDiscountType))
							{
								message="請選擇第"+(i+1)+"筆的商品折扣類型";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
							if(beginDate == null)
							{
								message="請選擇第"+(i+1)+"筆的起始時間";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
							if(endDate == null)
							{
								message="請選擇第"+(i+1)+"筆的結束時間";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}else if (beginDate.compareTo(endDate)>0)
							{
								message="第"+(i+1)+"筆的起始時間不可以比結束時間晚";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
							if(discount == null)
							{
								message="請填入第"+(i+1)+"筆的折扣";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}else if(discount <= 0){
								message="請正確輸入第"+(i+1)+"筆資料的折扣數";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}else if(discount > 100){
								message="請正確輸入第"+(i+1)+"筆資料的折扣數";
								siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
								errorMsgs.add(message);
								log.error(message);
							}
						}
				}else{
					message = tabName + "中請至少輸入一筆商品折扣明細！";
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
				}
			} catch (Exception e) {
					message = "檢核商品折扣明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
					siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
					errorMsgs.add(message);
					log.error(message);
			}
	}
  public ImItemDiscountModHead getActualHead(Long headId) throws FormException, Exception{
  	ImItemDiscountModHead imItemDiscountModHead  = this.findById(headId);
      	if(imItemDiscountModHead == null){
      	    throw new NoSuchObjectException("查無商品折扣主鍵：" + headId + "的資料！");
      	}
  	return imItemDiscountModHead;
      }
				
}