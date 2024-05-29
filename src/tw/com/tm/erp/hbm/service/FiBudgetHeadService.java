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
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.CmDeclarationContainer;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationVehicle;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetHeadTemp;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.FiBudgetModLine;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetHeadDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

/**
 * 採購預算 Head Service
 * 
 * @author Mac8
 * 
 */
public class FiBudgetHeadService {
	private static final Log log = LogFactory.getLog(FiBudgetHeadService.class);
	private FiBudgetHeadDAO fiBudgetHeadDAO;
	private FiBudgetLineDAO fiBudgetLineDAO;
	private ImItemCategoryService imItemCategoryService;
	private BuBrandDAO buBrandDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	
	/**
	 * 查詢欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "itemTypeName", "budgetYear", "budgetMonth", 
			"brandCode", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "",
			"", "" };

	/**
	 * 報單明細欄位
	 */
	public static final String[] T2_GRID_FIELD_NAMES = { 
	"indexNo", "itemBrandCode", "itemBrandName" ,"categoryTypeCode1" ,"categoryTypeCode2", 
	"budgetAmount", "forecastAmount", "signingAmount", "poActualAmount", "receiveActualAmount", 
	"adjustActualAmount", "poReturnAmount", "reserve1"};

	public static final String[] T2_GRID_FIELD_DEFAULT_VALUES = { 
	"", "", "", "", "", 
	"", "", "", "", "", 
	"", "", "" };

	public static final int[] T2_GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, 
	AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING};

	public void setFiBudgetHeadDAO(FiBudgetHeadDAO fiBudgetHeadDAO) {
		this.fiBudgetHeadDAO = fiBudgetHeadDAO;
	}

	public void setFiBudgetLineDAO(FiBudgetLineDAO fiBudgetLineDAO) {
		this.fiBudgetLineDAO = fiBudgetLineDAO;
	}
	
	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	
	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}
	
	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}

	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(FiBudgetHead modifyObj) throws FormException,
			Exception {
		log.info("FiBudgetHeadService.create ");
		if (null != modifyObj) {
			countHeadTotalAmount(modifyObj);
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
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(FiBudgetHead saveObj) throws FormException, Exception {
		log.info("FiBudgetHeadService.save ");
		doAllValidate(saveObj);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		fiBudgetHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(FiBudgetHead updateObj) throws FormException,
			Exception {
		log.info("FiBudgetHeadService.update ");
		doAllValidate(updateObj);
		updateObj.setLastUpdateDate(new Date());
		fiBudgetHeadDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<FiBudgetHead> find(HashMap findObjs) {
		log.info("FiBudgetHeadService.find ");
		return fiBudgetHeadDAO.find(findObjs);
	}

	
	public List<FiBudgetHead> find_new(HashMap findObjs,PoPurchaseOrderHead poHead) {
		log.info("FiBudgetHeadService.find_new ");
		return fiBudgetHeadDAO.find_new(findObjs,poHead);
	}
	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private void doAllValidate(FiBudgetHead headObj) throws FormException,
			Exception {
		log.info("FiBudgetHeadService.doAllValidate ");
		try {
			int budgetYear = Integer.valueOf(headObj.getBudgetYear());
			if (budgetYear <= 2000 || budgetYear >= 2100) {
				throw new FormException("預算年度格式資料有誤!");
			}
		} catch (Exception ex) {
			throw new FormException("預算年度格式資料有誤!");
		}
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private boolean checkDetailItemCode(FiBudgetHead headObj) {
		log.info("FiBudgetHeadService.checkDetailItemCode ");
		headObj.getFiBudgetLines();
		List<FiBudgetLine> items = headObj.getFiBudgetLines();
		Iterator<FiBudgetLine> it = items.iterator();
		while (it.hasNext()) {
			FiBudgetLine line = it.next();
			if (!StringUtils.hasText(line.getItemCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 計算預算合計
	 * 
	 * @param countObj
	 */
	public void countHeadTotalAmount(FiBudgetHead countObj) {
		log.info("FiBudgetHeadService.countHeadTotalAmount ");
		Double totalBudget = new Double(0);
		List<FiBudgetLine> fiBudgetLines = countObj.getFiBudgetLines();
		for (FiBudgetLine fiBudgetLine : fiBudgetLines) {
			totalBudget = totalBudget + fiBudgetLine.getBudgetAmount();
		}
		countObj.setTotalBudget(totalBudget);
	}

	/**
	 * 計算月份預算合計
	 * 
	 * @param month
	 *            月份(包含)
	 * @param countObj
	 * @return
	 */
	public Double getHeadTotalAmount(int month, FiBudgetHead countObj) {
		log.info("FiBudgetHeadService.getHeadTotalAmount ");
		Double totalBudget = new Double(0);
		List<FiBudgetLine> fiBudgetLines = countObj.getFiBudgetLines();
		for (int index = 0; index < month; index++) {
			totalBudget = totalBudget
					+ fiBudgetLines.get(index).getBudgetAmount();
		}
		return totalBudget;
	}

	/**
	 * 計算月份剩餘預算
	 * 
	 * @param month
	 *            月份(包含)
	 * @param countObj
	 * @return
	 */
	public Double getMonthTotalAppliedBudget(int month, FiBudgetHead countObj) {
		log.info("FiBudgetHeadService.getMonthTotalAppliedBudget ");
		Double totalBudget = new Double(0);
		List<FiBudgetLine> fiBudgetLines = countObj.getFiBudgetLines();
		for (int index = 0; index < month; index++) {
			totalBudget = totalBudget
					+ fiBudgetLines.get(index).getBudgetAmount();
		}
		return totalBudget - countObj.getTotalAppliedBudget();
	}

	/**
	 * 加總已使用預算
	 * 
	 * @param brandCode
	 * @param budgetYear
	 * @param orderTypeCode
	 * @param appliedBudget
	 */
	public void setTotalAppliedBudget(String brandCode, String budgetYear,
			String orderTypeCode, Double appliedBudget) {
		log.info("FiBudgetHeadService.setTotalAppliedBudget ");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("budgetYear", budgetYear);
		List<FiBudgetHead> list = fiBudgetHeadDAO.find(findObjs);
		if ((null != list) && (list.size() > 0)) {
			FiBudgetHead fiBudgetHead = list.get(0);
			fiBudgetHead.setTotalAppliedBudget(fiBudgetHead
					.getTotalAppliedBudget()
					+ appliedBudget);
		}
	}

	/**
	 * 查詢是否有重覆
	 * 
	 * @param fiBudgetHead
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public FiBudgetHead findDuplicate(FiBudgetHead fiBudgetHead)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		log.info("FiBudgetHeadService.findDuplicate ");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", fiBudgetHead.getBrandCode());
		findObjs.put("orderTypeCode", fiBudgetHead.getOrderTypeCode());
		findObjs.put("budgetYear", fiBudgetHead.getBudgetYear());
		List<FiBudgetHead> list = fiBudgetHeadDAO.find(findObjs);
		if ((null != list) && (list.size() > 0)) {
			return list.get(0);
		} else {
			FiBudgetHead newFiBudgetHead = new FiBudgetHead();
			PropertyUtils.copyProperties(newFiBudgetHead, fiBudgetHead);
			// 建立 head & line
			List<FiBudgetLine> fiBudgetLine = newFiBudgetHead
					.getFiBudgetLines();
			fiBudgetLine.clear();
			for (int index = 1; index < 13; index++) {
				System.out.println("line " + index);
				FiBudgetLine line = new FiBudgetLine();
				line.setMonth(new Long(index));
				line.setBudgetAmount(0d);
				// line.setFiBudgetHead(fiBudgetHead);
				fiBudgetLine.add(line);
			}
			return newFiBudgetHead;
		}
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public FiBudgetHead findById(Long headId) {
		return fiBudgetHeadDAO.findById(headId);
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
			log.info("getSearchSelection.parameterMap:"
					+ parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/"
					+ searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
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

	public List<Properties> getAJAXSearchPageData(Properties httpRequest)
			throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String year = httpRequest.getProperty("budgetYear");
			String month = httpRequest.getProperty("budgetMonth");
			String brandCode = httpRequest.getProperty("brandCode");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and BUDGET_YEAR  = :year", year);
			findObjs.put(" and BUDGET_MONTH = :month", month);
			findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);


			// ==============================================================
			Map fiBudgetMap = fiBudgetHeadDAO.search("FiBudgetHead", findObjs," ORDER BY BUDGET_YEAR DESC",
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<FiBudgetHead> fiBudget = (List<FiBudgetHead>) fiBudgetMap
					.get(BaseDAO.TABLE_LIST);
			if (fiBudget != null && fiBudget.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) fiBudgetHeadDAO.search("FiBudgetHead",
						"count(HEAD_ID) as rowCount", findObjs, iSPage, iPSize,
						BaseDAO.QUERY_RECORD_COUNT).get(
						BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, fiBudget, gridDatas,
						firstIndex, maxIndex));

			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}

	}
	
	public List<Properties> getAJAXSearchPageDataNew(Properties httpRequest) throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			List<FiBudgetHeadTemp> fiBudgetTemps = new ArrayList();
		
			// ======================帶入Head的值=========================
		
			String year = httpRequest.getProperty("budgetYear");
			String month = httpRequest.getProperty("budgetMonth");
			String brandCode = httpRequest.getProperty("brandCode");
			String itemType = httpRequest.getProperty("itemType");
			String status = httpRequest.getProperty("status");
			String itemBrandCode = httpRequest.getProperty("itemBrandCode");
		
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("year", year);
			findObjs.put("month", month);
			findObjs.put("brandCode", brandCode);
			findObjs.put("itemType", itemType);
			findObjs.put("status", status);
			findObjs.put("itemBrandCode", itemBrandCode);
		
			// ==============================================================
			List<FiBudgetHead> fiBudgets = (List<FiBudgetHead>) fiBudgetHeadDAO.findPageLine(findObjs, iSPage, iPSize, FiBudgetHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			log.info("fiBudget.size: " + fiBudgets.size());
			for(int i = 0; i < fiBudgets.size(); i++){
				FiBudgetHead fiBudget = fiBudgets.get(i);
				log.info("fiBudget.getItemType(): "+fiBudget.getItemType());
				log.info("fiBudget.getHeadId(): "+fiBudget.getHeadId());

				ImItemCategoryId id = new ImItemCategoryId();
				id.setBrandCode(brandCode);
				id.setCategoryCode((String)fiBudgets.get(i).getItemType());
				id.setCategoryType("ITEM_CATEGORY");
				FiBudgetHeadTemp fiBudgetHeadTemp = new FiBudgetHeadTemp();
				BeanUtils.copyProperties(fiBudgets.get(i), fiBudgetHeadTemp);
				ImItemCategory imItemCategory = (ImItemCategory)imItemCategoryDAO.findById("ImItemCategory", id);
				if(imItemCategory != null){
					fiBudgetHeadTemp.setItemTypeName(imItemCategory.getCategoryName());
				}
				fiBudgetTemps.add(fiBudgetHeadTemp);

			}
			if (fiBudgets != null && fiBudgets.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) fiBudgetHeadDAO.findPageLine(findObjs, -1, -1, FiBudgetHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, fiBudgetTemps, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
		
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}
	}

	public List<Properties> executeInitial(Map parameterMap) throws Exception {

		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			if (formId == null) {
				BuBrand buBrand = buBrandDAO.findById(loginBrandCode);
				returnMap.put("brandCode",loginBrandCode);
				returnMap.put("brandName",buBrand.getBrandName());
			} else {
				findFiBudgetHead(formId, returnMap);
			}
			List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(loginBrandCode, "CATEGORY01");
		    multiList.put("category01s" , AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  false,  true));
		    List <ImItemCategory> category02s = imItemCategoryService.findByCategoryType(loginBrandCode, "CATEGORY02");
		    multiList.put("category02s" , AjaxUtils.produceSelectorData(category02s  ,"categoryCode" ,"categoryName",  false,  true));
		    List <ImItemCategory> itemTypes = imItemCategoryService.findByCategoryType(loginBrandCode, "CATEGORY00");
		    multiList.put("itemTypes" , AjaxUtils.produceSelectorData(itemTypes  ,"categoryCode" ,"categoryName",  false,  false));
		    returnMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("採購預算單初始化失敗，原因：" + ex.toString());
			throw new Exception("採購預算單初始化失敗，原因：" + ex.toString());
		}
	}

	public FiBudgetHead findFiBudgetHead(long formId, Map returnMap)
			throws FormException, Exception {
		Map multiList = new HashMap(0);
		try {
			FiBudgetHead form = findById(formId);
			if (form != null) {
				BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
				ImItemCategory itemType = imItemCategoryService.findById(form.getBrandCode(), "ITEM_CATEGORY", form.getItemType());
				returnMap.put("brandName", buBrand.getBrandName());
				if(itemType != null)
					returnMap.put("itemTypeName", itemType.getCategoryName());
				returnMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				returnMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(form.getLastUpdatedBy()));
				returnMap.put("form", form);
				List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(form.getBrandCode(), "CATEGORY01");
			    multiList.put("category01s" , AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  false,  true));
			    List <ImItemCategory> category02s = imItemCategoryService.findByCategoryType(form.getBrandCode(), "CATEGORY02");
			    multiList.put("category02s" , AjaxUtils.produceSelectorData(category02s  ,"categoryCode" ,"categoryName",  false,  true));
			    returnMap.put("multiList",multiList);
				return form;
			} else {
				throw new NoSuchObjectException("查無採購預算單主鍵：" + formId + "的資料！");
			}
		} catch (FormException fe) {
			log.error("查詢採購預算單失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("查詢採購預算單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢採購預算單發生錯誤！");
		}
	}

	public List<Properties> getAJAXLinePageData(Properties httpRequest)
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
			List<FiBudgetLine> fiBudgetLines = fiBudgetLineDAO.findPageLine(headId, iSPage, iPSize);
			FiBudgetHead form = findById(headId);
			if (fiBudgetLines != null && fiBudgetLines.size() > 0) {
				for (Iterator iterator = fiBudgetLines.iterator(); iterator.hasNext();) {
					FiBudgetLine fiBudgetLine = (FiBudgetLine) iterator.next();
					ImItemCategory imItemCategory = imItemCategoryService.findById(form.getBrandCode(), "ItemBrand", fiBudgetLine.getItemBrandCode());
					if(imItemCategory!= null)
						fiBudgetLine.setItemBrandName(imItemCategory.getCategoryName());
					else if(StringUtils.hasText(fiBudgetLine.getItemBrandCode()))
						fiBudgetLine.setItemBrandName("查無此商品品牌");
				}
				// 取得第一筆的INDEX
				Long firstIndex = fiBudgetLines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = fiBudgetLineDAO.findPageLineMaxIndex(headId);
				// refreshItemData(map, cmDeclarationItems);
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						T2_GRID_FIELD_NAMES, T2_GRID_FIELD_DEFAULT_VALUES,
						fiBudgetLines, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						T2_GRID_FIELD_NAMES, T2_GRID_FIELD_DEFAULT_VALUES,
						gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的預算明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的預算明細失敗！");
		}
	}

	/**
	 * 更新PAGE的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateOrSaveAJAXPageLinesData(
			Properties httpRequest) throws Exception {
		try {
			String errorMsg = null;
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新海關進出倉明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新海關進出倉明細失敗！");
		}
	}
	public Map executeSearchInitial(Map parameterMap) throws Exception{
  		Map resultMap = new HashMap(0);
  		try{
  			Map multiList = new HashMap(0);
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			List<ImItemCategory> allItemTypes = imItemCategoryDAO.findByCategoryType(brandCode, "CATEGORY00");
			List<ImItemCategory> allItemBrands = imItemCategoryDAO.findByCategoryType(brandCode, "ItemBrand");
			multiList.put("allItemTypes", AjaxUtils.produceSelectorData(allItemTypes, "categoryCode", "categoryName", true, true));
			multiList.put("allItemBrands", AjaxUtils.produceSelectorData(allItemBrands, "categoryCode", "categoryName", true, true));
			resultMap.put("multiList", multiList);
  		}catch(Exception e){
  			e.printStackTrace();
  			log.error("表單初始化失敗，原因：" + e.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+e.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
  		}
  		return resultMap;
	}
}
