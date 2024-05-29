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

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetHeadTemp;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.FiBudgetModLine;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetHeadDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetModHeadDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetModLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;

/**
 * 採購預算 Head Service
 * 
 * @author Mac8
 * 
 */
public class FiBudgetModHeadService {
	private static final Log log = LogFactory.getLog(FiBudgetModHeadService.class);
	private FiBudgetModHeadDAO fiBudgetModHeadDAO;
	private FiBudgetModLineDAO fiBudgetModLineDAO;
	private ImItemCategoryService imItemCategoryService;
	private SiProgramLogAction siProgramLogAction;
	private BuCommonPhraseService buCommonPhraseService;
	private BuOrderTypeService buOrderTypeService;
	private FiBudgetHeadDAO fiBudgetHeadDAO;
	private BuBrandDAO buBrandDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	
	public static final String PROGRAM_ID_MOD= "FI_BUDGET_MOD";
	
	/**
	 * 查詢欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "budgetYear",
			"budgetMonth", "orderNo", "itemTypeName", "status", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
			"", "", "", "" };

	/**
	 * 預算明細欄位
	 */
	public static final String[] GRID_FIELD_NAMES = {
	"indexNo", "modifyType", "itemBrandCode", "itemBrandName", "categoryTypeCode1", 
	"categoryTypeCode2","budgetAmount",	"budgetAdjustAmount", "forecastAmount", "forecastAdjustAmount", 
	"reserve1", "lineId", "isLockRecord", "isDeleteRecord", "message"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
	"", "N", "", "", "", 
	"", "0", "0", "0", "0", 
	"", "", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE,""};

	public static final int[] GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};

	public void setFiBudgetModHeadDAO(FiBudgetModHeadDAO fiBudgetModHeadDAO) {
		this.fiBudgetModHeadDAO = fiBudgetModHeadDAO;
	}

	public void setFiBudgetModLineDAO(FiBudgetModLineDAO fiBudgetModLineDAO) {
		this.fiBudgetModLineDAO = fiBudgetModLineDAO;
	}

	public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	public void setFiBudgetHeadDAO(FiBudgetHeadDAO fiBudgetHeadDAO) {
		this.fiBudgetHeadDAO = fiBudgetHeadDAO;
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
	public String create(FiBudgetModHead modifyObj) throws FormException,
			Exception {
		log.info("FiBudgetModHeadService.create ");
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
	public String save(FiBudgetModHead saveObj) throws FormException, Exception {
		log.info("FiBudgetModHeadService.save ");
		doAllValidate(saveObj);
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		fiBudgetModHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(FiBudgetModHead updateObj) throws FormException,Exception {
		log.info("FiBudgetModHeadService.update ");
		updateObj.setLastUpdateDate(new Date());
		fiBudgetModHeadDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<FiBudgetModHead> find(HashMap findObjs) {
		log.info("FiBudgetModHeadService.find ");
		return fiBudgetModHeadDAO.find(findObjs);
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private void doAllValidate(FiBudgetModHead headObj) throws FormException,
			Exception {
		log.info("FiBudgetModHeadService.doAllValidate ");
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
	private boolean checkDetailItemCode(FiBudgetModHead headObj) {
		log.info("FiBudgetModHeadService.checkDetailItemCode ");
		headObj.getFiBudgetModLines();
		List<FiBudgetModLine> items = headObj.getFiBudgetModLines();
		Iterator<FiBudgetModLine> it = items.iterator();
		while (it.hasNext()) {
			FiBudgetModLine line = it.next();
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
	public void countHeadTotalAmount(FiBudgetModHead countObj) {
		log.info("FiBudgetModHeadService.countHeadTotalAmount ");
		Double totalBudget = new Double(0);
		List<FiBudgetModLine> fiBudgetLines = countObj.getFiBudgetModLines();
		for (FiBudgetModLine fiBudgetLine : fiBudgetLines) {
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
	public Double getHeadTotalAmount(int month, FiBudgetModHead countObj) {
		log.info("FiBudgetModHeadService.getHeadTotalAmount ");
		Double totalBudget = new Double(0);
		List<FiBudgetModLine> fiBudgetLines = countObj.getFiBudgetModLines();
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
	public Double getMonthTotalAppliedBudget(int month, FiBudgetModHead countObj) {
		log.info("FiBudgetModHeadService.getMonthTotalAppliedBudget ");
		Double totalBudget = new Double(0);
		List<FiBudgetModLine> fiBudgetLines = countObj.getFiBudgetModLines();
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
		log.info("FiBudgetModHeadService.setTotalAppliedBudget ");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", brandCode);
		findObjs.put("orderTypeCode", orderTypeCode);
		findObjs.put("budgetYear", budgetYear);
		List<FiBudgetModHead> list = fiBudgetModHeadDAO.find(findObjs);
		if ((null != list) && (list.size() > 0)) {
			FiBudgetModHead fiBudgetHead = list.get(0);
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
	public FiBudgetModHead findDuplicate(FiBudgetModHead fiBudgetHead)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		log.info("FiBudgetModHeadService.findDuplicate ");
		HashMap findObjs = new HashMap();
		findObjs.put("brandCode", fiBudgetHead.getBrandCode());
		findObjs.put("orderTypeCode", fiBudgetHead.getOrderTypeCode());
		findObjs.put("budgetYear", fiBudgetHead.getBudgetYear());
		List<FiBudgetModHead> list = fiBudgetModHeadDAO.find(findObjs);
		if ((null != list) && (list.size() > 0)) {
			return list.get(0);
		} else {
			FiBudgetModHead newFiBudgetModHead = new FiBudgetModHead();
			PropertyUtils.copyProperties(newFiBudgetModHead, fiBudgetHead);
			// 建立 head & line
			List<FiBudgetModLine> fiBudgetLine = newFiBudgetModHead
					.getFiBudgetModLines();
			fiBudgetLine.clear();
			for (int index = 1; index < 13; index++) {
				System.out.println("line " + index);
				FiBudgetModLine line = new FiBudgetModLine();
				line.setMonth(new Long(index));
				line.setBudgetAmount(0d);
				// line.setFiBudgetModHead(fiBudgetHead);
				fiBudgetLine.add(line);
			}
			return newFiBudgetModHead;
		}
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public FiBudgetModHead findById(Long headId) {
		return fiBudgetModHeadDAO.findById(headId);
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
			String cName = httpRequest.getProperty("itemCName");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and BUDGET_YEAR  = :year", year);
			findObjs.put(" and BUDGET_MONTH = :month", month);
			findObjs.put(" and BRAND_CODE  = :brandCode", brandCode);
			findObjs.put(" and ORDER_NO NOT LIKE :TMP","TMP%");
			
			// String nul = "null";
			// findObjs.put(" and DECL_NO != :nul", nul);

			// ==============================================================
			Map fiBudgetMap = fiBudgetModHeadDAO.search("FiBudgetModHead", findObjs," ORDER BY BUDGET_YEAR",
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<FiBudgetModHead> fiBudget = (List<FiBudgetModHead>) fiBudgetMap
					.get(BaseDAO.TABLE_LIST);
			// System.out.println(cmDeclarationMap.toString());////
			// log.info("cmDeclaration.size" + cmDeclaration.size());
			if (fiBudget != null && fiBudget.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) fiBudgetModHeadDAO.search("FiBudgetModHead",
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
			String orderNo = httpRequest.getProperty("orderNo");
		
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("year", year);
			findObjs.put("month", month);
			findObjs.put("brandCode", brandCode);
			findObjs.put("itemType", itemType);
			findObjs.put("status", status);
			findObjs.put("itemBrandCode", itemBrandCode);
			findObjs.put("orderNo", orderNo);
		
			// ==============================================================
			List<FiBudgetModHead> fiBudgets = (List<FiBudgetModHead>) fiBudgetModHeadDAO.findPageLine(findObjs, iSPage, iPSize, FiBudgetHeadDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			log.info("fiBudget.size: " + fiBudgets.size());
			for(int i = 0; i < fiBudgets.size(); i++){
				FiBudgetModHead fiBudget = fiBudgets.get(i);
				log.info("fiBudget.getItemType(): "+fiBudget.getItemType());
				log.info("fiBudget.getHeadId(): "+fiBudget.getHeadId());

				ImItemCategoryId id = new ImItemCategoryId();
				id.setBrandCode(brandCode);
				id.setCategoryCode((String)fiBudget.getItemType());
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
				Long maxIndex = (Long) fiBudgetModHeadDAO.findPageLine(findObjs, -1, -1, FiBudgetHeadDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆INDEX
				log.info("maxIndex: "+maxIndex);
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
			Long formId = NumberUtils.getLong((String) PropertyUtils.getProperty(otherBean,"formId"));
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
			BuBrand buBrand = buBrandDAO.findById(loginBrandCode);
			FiBudgetModHead fiBudgetModHead = this.findFiBudgetModHead(formId, otherBean, returnMap);

			List <BuCommonPhraseLine> budgetYearLists = buCommonPhraseService.findEnableLineById("BudgetYearList");
			multiList.put("budgetYearLists" , AjaxUtils.produceSelectorData(budgetYearLists  ,"lineCode" ,"name",  false,  true));
			List <BuCommonPhraseLine> months = buCommonPhraseService.findEnableLineById("Month");
			if(!"2".equals(buBrand.getBuBranch().getBranchCode()))
				months = months.subList(0, 1);
			multiList.put("months" , AjaxUtils.produceSelectorData(months  ,"lineCode" ,"attribute1",  false,  false));
			List <BuCommonPhraseLine> modifyTypes = buCommonPhraseService.findEnableLineById("ModifyTypes");
			multiList.put("modifyTypes" , AjaxUtils.produceSelectorData(modifyTypes  ,"lineCode" ,"name",  false,  true));
			//List <ImItemCategory> itemTypes = imItemCategoryService.findByCategoryType(fiBudgetModHead.getBrandCode(), "CATEGORY00");修改為業種子類
			List <ImItemCategory> itemTypes = imItemCategoryService.findByCategoryType(fiBudgetModHead.getBrandCode(), "ITEM_CATEGORY");
		    multiList.put("itemTypes" , AjaxUtils.produceSelectorData(itemTypes  ,"categoryCode" ,"categoryName",  false,  false));
		    List <ImItemCategory> itemBrands = imItemCategoryService.findByCategoryType(fiBudgetModHead.getBrandCode(), "ItemBrand");
		    multiList.put("itemBrands" , AjaxUtils.produceSelectorData(itemBrands  ,"categoryCode" ,"categoryName",  false,  true));
		    List <ImItemCategory> category01s = imItemCategoryService.findByCategoryType(fiBudgetModHead.getBrandCode(), "CATEGORY01");
		    multiList.put("category01s" , AjaxUtils.produceSelectorData(category01s  ,"categoryCode" ,"categoryName",  false,  true));
		    List <ImItemCategory> category02s = imItemCategoryService.findByCategoryType(fiBudgetModHead.getBrandCode(), "CATEGORY02");
		    multiList.put("category02s" , AjaxUtils.produceSelectorData(category02s  ,"categoryCode" ,"categoryName",  false,  true));
		    returnMap.put("branch",buBrand.getBuBranch().getBranchCode());
		    returnMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("採購預算單初始化失敗，原因：" + ex.toString());
			throw new Exception("採購預算單初始化失敗，原因：" + ex.getMessage());
		}
	}

	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String)PropertyUtils.getProperty(otherBean,"loginBrandCode");

			/**預算年度**/
			List <BuCommonPhraseLine> budgetYearLists = buCommonPhraseService.findEnableLineById("BudgetYearList");					
			multiList.put("budgetYearLists" , AjaxUtils.produceSelectorData(budgetYearLists  ,"lineCode" ,"name",  false,  true));			
			
			/**預算月份**/
			List <BuCommonPhraseLine> months = buCommonPhraseService.findEnableLineById("Month");									
			multiList.put("months" , AjaxUtils.produceSelectorData(months  ,"lineCode" ,"name",  false,  true));
			
			/**業種**/
			List <ImItemCategory> itemTypes = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
		    multiList.put("itemTypes" , AjaxUtils.produceSelectorData(itemTypes  ,"categoryCode" ,"categoryName",  false,  true));
			
			/**商品品牌**/
		    List <ImItemCategory> imItemCategoryList = imItemCategoryService.findByCategoryType(brandCode, "ItemBrand");
		    multiList.put("itemBrandCodes" , AjaxUtils.produceSelectorData(imItemCategoryList  ,"categoryCode" ,"categoryCode",  false,  true));
		    
		    BuBrand buBrand = buBrandDAO.findById(brandCode);
		    String brandName = buBrand.getBrandName();
			
			returnMap.put("brandCode",brandCode);
			returnMap.put("brandName",brandName);
		    returnMap.put("multiList",multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("採購預算單初始化失敗，原因：" + ex.toString());
			throw new Exception("採購預算單初始化失敗，原因：" + ex.getMessage());
		}
	}
	
	public FiBudgetModHead findFiBudgetModHead(long formId, Object otherBean, Map returnMap) throws FormException, Exception {
			FiBudgetModHead form = formId>0?findById(formId):executeNew(otherBean);
			if (form != null) {
				BuBrand buBrand = buBrandDAO.findById(form.getBrandCode());
				returnMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				returnMap.put("brandName", buBrand.getBrandName());
				returnMap.put("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode(form.getLastUpdatedBy()));
				returnMap.put("form", form);
				return form;
			} else {
				throw new FormException("查無採購預算修改單主鍵：" + formId + "的資料！");
			}
	}

	public List<Properties> getAJAXLinePageData(Properties httpRequest) throws Exception {
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
			FiBudgetModHead form = findById(headId);
			List<FiBudgetModLine> fiBudgetLines = fiBudgetModLineDAO.findPageLine(
					headId, iSPage, iPSize);
			if (fiBudgetLines != null && fiBudgetLines.size() > 0) {
				for (Iterator iterator = fiBudgetLines.iterator(); iterator.hasNext();) {
					FiBudgetModLine fiBudgetModLine = (FiBudgetModLine) iterator.next();
					ImItemCategory imItemCategory = imItemCategoryService.findById(form.getBrandCode(), "ItemBrand", fiBudgetModLine.getItemBrandCode());
					if(imItemCategory!= null)
						fiBudgetModLine.setItemBrandName(imItemCategory.getCategoryName());
					else if(StringUtils.hasText(fiBudgetModLine.getItemBrandCode()))
						fiBudgetModLine.setItemBrandName("查無此商品品牌");
				}
				// 取得第一筆的INDEX
				Long firstIndex = fiBudgetLines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = fiBudgetModLineDAO.findPageLineMaxIndex(headId);
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
						fiBudgetLines, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
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
	public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest) throws Exception {
		try{
		    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		    if(headId <= 0){
		    	throw new ValidationErrorException("傳入的預算修改單主鍵為空值！");
		    }
		    String errorMsg = null;
			FiBudgetModHead fiBudgetModHead = getActualHead(headId);
			BuBrand buBrand = buBrandDAO.findById(fiBudgetModHead.getBrandCode());
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = 0 ;
			Object object = fiBudgetModHeadDAO.findPageLineMaxIndex("FiBudgetModLine","fiBudgetModHead.",headId);
			if(object != null)
				indexNo = ((FiBudgetModLine)object).getIndexNo().intValue();
			if (upRecords != null) {
			    for (Properties upRecord : upRecords) {
			        // 先載入HEAD_ID OR LINE DATA
				Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
				String modifyType = upRecord.getProperty(GRID_FIELD_NAMES[1]);
				Double adjustAmount = NumberUtils.getDouble(upRecord.getProperty(GRID_FIELD_NAMES[7]));
				Double forecastAdjustAmount = NumberUtils.getDouble(upRecord.getProperty(GRID_FIELD_NAMES[9]));
				if (adjustAmount != 0 || forecastAdjustAmount != 0) {
					FiBudgetModLine fiBudgetModLine = fiBudgetModLineDAO.findById(lineId);
				    if(fiBudgetModLine != null){
				    	if(!"M".equals(modifyType)){
				    		AjaxUtils.setPojoProperties(fiBudgetModLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
				    	}else{
				    		fiBudgetModLine.setBudgetAdjustAmount(adjustAmount);
				    		fiBudgetModLine.setForecastAdjustAmount(forecastAdjustAmount);
				    	}
				    	if(!"2".equals(buBrand.getBuBranch().getBranchCode())){
			    			fiBudgetModLine.setItemBrandCode("ALL");
			    			fiBudgetModLine.setCategoryTypeCode1("ALL");
			    			fiBudgetModLine.setCategoryTypeCode2("ALL");
			    		}
						fiBudgetModLineDAO.update(fiBudgetModLine);
				    }else{
						indexNo++;
						fiBudgetModLine = new FiBudgetModLine();
						AjaxUtils.setPojoProperties(fiBudgetModLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
						fiBudgetModLine.setIndexNo(Long.valueOf(indexNo));
						fiBudgetModLine.setFiBudgetModHead(fiBudgetModHead);
			    		if(!"2".equals(buBrand.getBuBranch().getBranchCode())){
			    			fiBudgetModLine.setItemBrandCode("ALL");
			    			fiBudgetModLine.setCategoryTypeCode1("ALL");
			    			fiBudgetModLine.setCategoryTypeCode2("ALL");
			    		}
						fiBudgetModLineDAO.save(fiBudgetModLine);
				    }
				}
			    }
			}
		    return AjaxUtils.getResponseMsg(errorMsg);
        }catch(Exception ex){
            log.error("更新預算修改單時發生錯誤，原因：" + ex.toString());
            throw new Exception("更新預算修改單失敗！"); 
        }
	}
	
	/**
	 * 產生新的預算修改單
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	public FiBudgetModHead executeNew(Object otherBean)throws Exception{
		FiBudgetModHead form = new FiBudgetModHead();
		try {
			String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
			form.setOrderTypeCode(orderTypeCode);
			form.setBrandCode(loginBrandCode);
			form.setStatus( OrderStatus.SAVE );
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			form.setCreationDate(new Date());
			this.saveHead(form);
			
		} catch (Exception e) {
			log.error("建立預算修改主檔失敗,原因:"+e.toString());
			throw new Exception("建立新預算修改主檔失敗,原因:"+e.toString());
		}
		return form;
	}
	
	/**
	 * 預算修改存檔,取得暫存碼
	 * @param imLetterOfCreditHead
	 * @throws Exception
	 */ 
    public void saveHead(FiBudgetModHead fiBudgetModHead) throws Exception{
    	try{
    	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
    	    fiBudgetModHead.setOrderNo(tmpOrderNo);
    	    fiBudgetModHeadDAO.save(fiBudgetModHead);	  
    	}catch(Exception ex){
    	    log.error("取得暫時單號預算修改單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("取得暫時單號預算修改單發生錯誤，原因：" + ex.getMessage());
    	}	
    }

    
    public Map updateParameter(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = getHeadId(formLinkBean); 
    	    String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
    	    String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
    	    //取得欲更新的bean
    	    FiBudgetModHead fiBudgetModHead = this.getActualHead(headId);
		    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, fiBudgetModHead);
		    if(OrderStatus.SIGNING.equals(formAction)){
		    	//log.info("更新不取單號");
		    	fiBudgetModHead.setLastUpdatedBy(employeeCode);
		    	this.update(fiBudgetModHead);
		    }else{
		    	//log.info("更新並取單號");
		    	this.modifyAjaxOrderNo(fiBudgetModHead,employeeCode);
		    }
	    	resultMap.put("entityBean", fiBudgetModHead);
   	    	return resultMap;      
        }catch (Exception ex) {
        	log.error("預算修改單存檔時發生錯誤，原因：" + ex.toString());
        	throw new Exception(ex.getMessage());
        }
    }
    
    public Long getHeadId(Object bean) throws FormException, Exception{
    	Long headId = null;
    	String id = (String)PropertyUtils.getProperty(bean, "headId");
    	if(StringUtils.hasText(id)){
                headId = NumberUtils.getLong(id);
            }else{
        	    throw new ValidationErrorException("傳入的預算修改單主鍵為空值！");
            }
    	return headId;
    }
  
    public FiBudgetModHead getActualHead(Long headId) throws FormException, Exception{
    	FiBudgetModHead fiBudgetModHead  = this.findById(headId);
        	if(fiBudgetModHead == null){
        	    throw new NoSuchObjectException("查無預算修改單主鍵：" + headId + "的資料！");
        	}
    	return fiBudgetModHead;
        }
    
    /**
     * 更新檢核後的預算修改單
     * 
     * @param parameterMap
     * @return List
     * @throws Exception
     */
    public Map updateCheckedParameter(Map parameterMap) throws FormException , Exception {
    	HashMap resultMap = new HashMap();        
    	List errorMsgs = new ArrayList(0);
    	String message = "";
    	try{
            Object formLinkBean = parameterMap.get("vatBeanFormLink");
            Object otherBean = parameterMap.get("vatBeanOther");
            Long headId = getHeadId(formLinkBean);          
            FiBudgetModHead fiBudgetModHead = getActualHead(headId);
            String identification = MessageStatus.getIdentification(fiBudgetModHead.getBrandCode(),fiBudgetModHead.getOrderTypeCode(), fiBudgetModHead.getOrderNo());
            siProgramLogAction.deleteProgramLog(PROGRAM_ID_MOD, null, identification);
            String formAction = (String)PropertyUtils.getProperty(otherBean, "formAction");
            
            if(!OrderStatus.SAVE.equals(formAction) && !OrderStatus.REJECT.equals(formAction)){
	            this.deleteLines(fiBudgetModHead);
	            log.info("這邊開始檢查欄位");
	            if(!StringUtils.hasText(fiBudgetModHead.getItemType())){
	        		message = "業種為必填";
	                siProgramLogAction.createProgramLog(PROGRAM_ID_MOD, MessageStatus.LOG_ERROR, identification, message, fiBudgetModHead.getLastUpdatedBy());
	                errorMsgs.add(message);
	            }if(!StringUtils.hasText(fiBudgetModHead.getBudgetYear())){
	        		message = "預算修改單年度為必填";
	                siProgramLogAction.createProgramLog(PROGRAM_ID_MOD, MessageStatus.LOG_ERROR, identification, message, fiBudgetModHead.getLastUpdatedBy());
	                errorMsgs.add(message);
	            }if(!StringUtils.hasText(fiBudgetModHead.getBudgetMonth())){
	            	message = "預算修改單月份為必填";
	                siProgramLogAction.createProgramLog(PROGRAM_ID_MOD, MessageStatus.LOG_ERROR, identification, message, fiBudgetModHead.getLastUpdatedBy());
	                errorMsgs.add(message);
	            }
	            this.checkLines(fiBudgetModHead,errorMsgs,identification);
            }
            
            if(errorMsgs.size() > 0)
            	throw new FormException("未通過頁面檢核");
            this.modifyAjaxOrderNo(fiBudgetModHead, null);
            resultMap.put("entityBean", fiBudgetModHead);
            
		    return resultMap;
		}catch(FormException fe){
			log.error("預算修改單檢核後存檔失敗，原因：" + fe.toString());
			throw new FormException("預算修改單檢核後存檔失敗，原因：" + fe.getMessage());
		}catch (Exception ex) {
		    log.error("預算修改單檢核後存檔失敗，原因：" + ex.toString());
		    throw new Exception("預算修改單檢核後存檔失敗，原因：" + ex.getMessage());
		}
    }
    
    public static Object[] startProcess(FiBudgetModHead form) throws ProcessFailedException{       
        try{           
		    String packageId = "Fi_Budget_Mod";         
		    String processId = "process";           
		    String version = "20091107";
		    String sourceReferenceType = "FiBudgetMod";
		    HashMap context = new HashMap();	    
		    context.put("formId", form.getHeadId());
		    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
		    log.error("預算修改單流程啟動失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("預算修改單流程啟動失敗！");
		}	      
    }
    
    /**
     * 暫存單號取實際單號並更新至預算修改單
     * 
     * @param fiBudgetModHead
     * @param loginUser
     * @return String
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    private String modifyAjaxOrderNo(FiBudgetModHead fiBudgetModHead, String loginUser)throws ObtainSerialNoFailedException, FormException, Exception {
		if (AjaxUtils.isTmpOrderNo(fiBudgetModHead.getOrderNo())) {
		    String serialNo = buOrderTypeService.getOrderSerialNo(fiBudgetModHead.getBrandCode(), fiBudgetModHead.getOrderTypeCode());
		    if (!serialNo.equals("unknow")) {
		    	fiBudgetModHead.setOrderNo(serialNo);
		    } else {
		    	throw new ObtainSerialNoFailedException("取得"+ fiBudgetModHead.getOrderTypeCode() + "單號失敗！");
		    }
		}
		if(StringUtils.hasText(loginUser))
			fiBudgetModHead.setLastUpdatedBy(loginUser);
		this.update(fiBudgetModHead);
		return fiBudgetModHead.getOrderTypeCode() + "-" + fiBudgetModHead.getOrderNo() + "存檔成功！";
    }
    
    
    public String getIdentification(Long headId) throws Exception{
        String id = null;
		try{
		    FiBudgetModHead fiBudgetModHead = findById(headId);
		    if(fiBudgetModHead != null){
	                id = MessageStatus.getIdentification(fiBudgetModHead.getBrandCode(),fiBudgetModHead.getOrderTypeCode(), fiBudgetModHead.getOrderNo());
		    }else{
		        throw new NoSuchDataException("預算修改單主檔查無主鍵：" + headId + "的資料！");
		    }
		    return id;
		}catch(Exception ex){
		    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
		}	   
    }
    
    public List<Properties> executeFindBudget(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	    //======================取得複製時所需的必要資訊========================
    	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    	    if(headId == 0L){
    	    	throw new ValidationErrorException("無法取得預算修改單的主鍵值！");
    	    }
    	    String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
    	    String budgetYear = httpRequest.getProperty("budgetYear");
    	    String budgetMonth = httpRequest.getProperty("budgetMonth");
    	    String itemType = httpRequest.getProperty("itemType");

    	    FiBudgetModHead fiBudgetModHead = this.findById(headId);
    	    //======================get current return object==================
    	    List<FiBudgetHead> fiBudgetHeads = fiBudgetHeadDAO.findByProperty("FiBudgetHead", new String[]{"brandCode","budgetYear","budgetMonth","itemType"}, new String[]{brandCode,budgetYear,budgetMonth,itemType});
    	    if(fiBudgetHeads == null || fiBudgetHeads.size()==0){
    	    	log.info("查無此預算單的資料！");
    	    }else{
		    //================copy Line Data==================
	    	    FiBudgetHead fiBudgetHead = fiBudgetHeads.get(0);
	    	    List<FiBudgetModLine> newLines = new ArrayList(0);
	    	    List<FiBudgetLine> fiBudgetLines = fiBudgetHead.getFiBudgetLines();
	    	    Long indexNo = 1L;
	    	    for (FiBudgetLine fiBudgetLine : fiBudgetLines) {
	    	    	FiBudgetModLine fiBudgetModLine = new FiBudgetModLine();
	    	    	fiBudgetModLine.setIndexNo(indexNo++);
	    	    	fiBudgetModLine.setMonth(fiBudgetLine.getMonth());
	    	    	fiBudgetModLine.setCheckType(fiBudgetLine.getCheckType());
	    	    	fiBudgetModLine.setItemCode(fiBudgetLine.getItemCode());
	    	    	fiBudgetModLine.setCategoryTypeCode1(fiBudgetLine.getCategoryTypeCode1());
	    	    	fiBudgetModLine.setCategoryTypeCode2(fiBudgetLine.getCategoryTypeCode2());
	    	    	fiBudgetModLine.setBudgetAmount(fiBudgetLine.getBudgetAmount());
	    	    	fiBudgetModLine.setForecastAmount(fiBudgetLine.getForecastAmount());
	    	    	fiBudgetModLine.setItemBrandCode(fiBudgetLine.getItemBrandCode());
	    	    	fiBudgetModLine.setSigningAmount(fiBudgetLine.getSigningAmount());
	    	    	fiBudgetModLine.setPoActualAmount(fiBudgetLine.getPoActualAmount());
	    	    	fiBudgetModLine.setAdjustActualAmount(fiBudgetLine.getAdjustActualAmount());
	    	    	fiBudgetModLine.setReserve1(fiBudgetLine.getReserve1());
	    	    	fiBudgetModLine.setModifyType("M");
	    	    	//fiBudgetModLine.setItemBrandCode("OTHER");
	    	    	newLines.add(fiBudgetModLine);
				}
	    	    fiBudgetModHead.setFiBudgetModLines(newLines);
	    	    fiBudgetModHead.setTotalBudget(NumberUtils.getDouble(fiBudgetHead.getTotalBudget()));
	    	    fiBudgetModHead.setTotalForecastAmount(NumberUtils.getDouble(fiBudgetHead.getTotalForecastAmount()));
	    	    fiBudgetModHeadDAO.update(fiBudgetModHead);
    	    }
    	    
    	    properties.setProperty("TotalBudget", OperationUtils.roundToStr(fiBudgetModHead.getTotalBudget(),0));
    	    properties.setProperty("TotalForecastAmount", OperationUtils.roundToStr(fiBudgetModHead.getTotalForecastAmount(),0));
        	//properties.setProperty("TotalAppliedBudget", AjaxUtils.getPropertiesValue(fiBudgetModHead.getTotalAppliedBudget(),"0"));
        	//properties.setProperty("TotalSigningBudget", AjaxUtils.getPropertiesValue(fiBudgetModHead.getTotalSigningBudget(),"0"));
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
    	}
	}
   
    public List<Properties> executeDisableBudget(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	    //======================取得複製時所需的必要資訊========================
    	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    	    if(headId == 0L){
    	    	throw new ValidationErrorException("無法取得預算修改單的主鍵值！");
    	    }
    	    FiBudgetModHead fiBudgetModHead = this.findById(headId);
    	    List<FiBudgetModLine> newLines = new ArrayList(0);
    	    fiBudgetModHead.setFiBudgetModLines(newLines);
    	    fiBudgetModHeadDAO.update(fiBudgetModHead);
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
    	}
	}
    
    public List<Properties> executeBudgetTotal(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	    //======================取得複製時所需的必要資訊========================
    	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    	    if(headId == 0L){
    	    	throw new ValidationErrorException("無法取得預算修改單的主鍵值！");
    	    }
    	    FiBudgetModHead fiBudgetModHead = this.findById(headId);
    	    List<FiBudgetModLine> newLines = fiBudgetModHead.getFiBudgetModLines();
    	    Double totalBudget = 0D;
    	    Double totalForecastAmount = 0D;
    	    log.info("executeBudgetTotal");
    	    for (Iterator iterator = newLines.iterator(); iterator.hasNext();) {
    	    		FiBudgetModLine fiBudgetModLine = (FiBudgetModLine) iterator.next();
	    		if(!(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(fiBudgetModLine.getIsDeleteRecord())&&"N".equals(fiBudgetModLine.getModifyType()))){
	    			totalBudget += NumberUtils.getDouble(fiBudgetModLine.getBudgetAmount());
	    			totalBudget += NumberUtils.getDouble(fiBudgetModLine.getBudgetAdjustAmount());
	    			totalForecastAmount += NumberUtils.getDouble(fiBudgetModLine.getForecastAmount());
	    			totalForecastAmount += NumberUtils.getDouble(fiBudgetModLine.getForecastAdjustAmount());
	    		}
			}
    	    
    	    properties.setProperty("TotalBudget", OperationUtils.roundToStr(totalBudget, 0));
    	    properties.setProperty("TotalForecastAmount", OperationUtils.roundToStr(totalForecastAmount, 0));
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
    	}
	}
    
    public List<Properties> executeFindItemBrandName(Properties httpRequest) throws Exception{
    	
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	try{
    	    String brandCode = httpRequest.getProperty("brandCode");
    	    String itemBrandCode = httpRequest.getProperty("itemBrandCode");
    	    
    	    ImItemCategory imItemCategory = imItemCategoryService.findById(brandCode, "ItemBrand", itemBrandCode);
    	    if(imItemCategory!= null)
    	    	properties.setProperty("ItemBrandName", imItemCategory.getCategoryName());
    	    else if(StringUtils.hasText(itemBrandCode))
    	    	properties.setProperty("ItemBrandName", "查無此商品品牌");
    	    else
    	    	properties.setProperty("ItemBrandName", "");
    	    result.add(properties);
    		return result;
    	}catch(Exception ex){
    	    log.error("查詢報關單發生錯誤，原因：" + ex.toString());
    	    throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
    	}
	}
    
    public void saveFinishBudget(Long headId){
    	
    	FiBudgetModHead fiBudgetModHead = this.findById(headId);
    	fiBudgetModHead.setStatus(OrderStatus.FINISH);
	    //======================get current return object==================
    	log.info("find fiBudgetHeads start");
	    List<FiBudgetHead> fiBudgetHeads = fiBudgetHeadDAO.findByProperty("FiBudgetHead", 
	    		new String[]{"brandCode","budgetYear","budgetMonth","itemType"}, 
	    		new String[]{fiBudgetModHead.getBrandCode(),fiBudgetModHead.getBudgetYear(),
	    					 fiBudgetModHead.getBudgetMonth(),fiBudgetModHead.getItemType()});
	    log.info("find fiBudgetHeads finish");
	    FiBudgetHead fiBudgetHead;
	    if(fiBudgetHeads == null || fiBudgetHeads.size()==0){
	    	fiBudgetHead = new FiBudgetHead();
	    	fiBudgetHead.setBrandCode(fiBudgetModHead.getBrandCode());
	    	fiBudgetHead.setBudgetYear(fiBudgetModHead.getBudgetYear());
	    	fiBudgetHead.setDescription(fiBudgetModHead.getDescription());
	    	fiBudgetHead.setBudgetCheckType(fiBudgetModHead.getBudgetCheckType());
	    	fiBudgetHead.setTotalBudget(fiBudgetModHead.getTotalBudget());
	    	fiBudgetHead.setTotalForecastAmount((fiBudgetModHead.getTotalForecastAmount()));
	    	//fiBudgetHead.setTotalAppliedBudget(fiBudgetModHead.getTotalAppliedBudget());
	    	fiBudgetHead.setOrderTypeCode("PO");
	    	fiBudgetHead.setStatus(fiBudgetModHead.getStatus());
	    	//fiBudgetHead.setTotalSigningBudget(fiBudgetModHead.getTotalSigningBudget());
	    	fiBudgetHead.setBudgetMonth(fiBudgetModHead.getBudgetMonth());
	    	fiBudgetHead.setItemType(fiBudgetModHead.getItemType());
	    	fiBudgetHead.setCreatedBy(fiBudgetModHead.getCreatedBy());
	    	fiBudgetHead.setCreationDate(fiBudgetModHead.getCreationDate());
	    	fiBudgetHead.setLastUpdatedBy(fiBudgetModHead.getLastUpdatedBy());
	    	fiBudgetHead.setLastUpdateDate(fiBudgetModHead.getLastUpdateDate());
	    	fiBudgetHead.setFiBudgetLines(new ArrayList<FiBudgetLine> (0));
	    }else{
	    	fiBudgetHead = fiBudgetHeads.get(0);
	    	fiBudgetHead.setOrderTypeCode("PO");
	    	fiBudgetHead.setTotalBudget(fiBudgetModHead.getTotalBudget());
	    	fiBudgetHead.setTotalForecastAmount(fiBudgetModHead.getTotalForecastAmount());
	    	fiBudgetHead.setDescription(fiBudgetModHead.getDescription());
	    	//fiBudgetHead.setFiBudgetLines(new ArrayList<FiBudgetLine> (0));
	    }
	    //log.info("set budget Lines");
	    List<FiBudgetModLine> fiBudgetModLines = fiBudgetModHead.getFiBudgetModLines();
	    //log.info("fiBudgetModLines.size = " + fiBudgetModLines.size());
	    List<FiBudgetLine> fiBudgetLines = fiBudgetHead.getFiBudgetLines();
	    //log.info("fiBudgetLines.size = " + fiBudgetLines.size());
	    for (int i=0; i<fiBudgetModLines.size() ; i++) {
	    	FiBudgetModLine fiBudgetModLine = fiBudgetModLines.get(i);
	    	if("M".equals(fiBudgetModLine.getModifyType())){
	    		log.info("In New");
	    		FiBudgetLine fiBudgetLine = fiBudgetLines.get(i);
	    		fiBudgetLine.setBudgetAmount(NumberUtils.getDouble(fiBudgetModLine.getBudgetAmount())+
	    				NumberUtils.getDouble(fiBudgetModLine.getBudgetAdjustAmount()));
	    		fiBudgetLine.setForecastAmount(NumberUtils.getDouble(fiBudgetModLine.getForecastAmount())+
	    				NumberUtils.getDouble(fiBudgetModLine.getForecastAdjustAmount()));
	    		fiBudgetLine.setReserve1(fiBudgetModLine.getReserve1());
	    		if(null == fiBudgetModLine.getItemBrandCode()){
	    			fiBudgetLine.setItemBrandCode("OTHER");
	    			}else{
	    			fiBudgetLine.setItemBrandCode(fiBudgetModLine.getItemBrandCode());
	    			}
	    	}else{
	    		FiBudgetLine fiBudgetLine = new FiBudgetLine();
	    		fiBudgetLine.setIndexNo(Long.valueOf((i+1)));
	    		fiBudgetLine.setCategoryTypeCode1(fiBudgetModLine.getCategoryTypeCode1());
	    		fiBudgetLine.setCategoryTypeCode2(fiBudgetModLine.getCategoryTypeCode2());
	    		if(null == fiBudgetModLine.getItemBrandCode()){
	    			fiBudgetLine.setItemBrandCode("OTHER");
	    			}else{	   
	    			fiBudgetLine.setItemBrandCode(fiBudgetModLine.getItemBrandCode());	    		
	    			}
	    		fiBudgetLine.setBudgetAmount(NumberUtils.getDouble(fiBudgetModLine.getBudgetAmount())+
	    				NumberUtils.getDouble(fiBudgetModLine.getBudgetAdjustAmount()));
	    		fiBudgetLine.setForecastAmount(NumberUtils.getDouble(fiBudgetModLine.getForecastAmount())+
	    				NumberUtils.getDouble(fiBudgetModLine.getForecastAdjustAmount()));
	    		fiBudgetLine.setReserve1(fiBudgetModLine.getReserve1());
	    		fiBudgetLines.add(fiBudgetLine);
	    	}
		}
	    fiBudgetHead.setFiBudgetLines(fiBudgetLines);
	    fiBudgetHeadDAO.saveOrUpdate(fiBudgetHead);
	    fiBudgetModHeadDAO.update(fiBudgetModHead);
    }
    
    public void deleteLines(FiBudgetModHead head){
    	List<FiBudgetModLine> fiBudgetModLines = head.getFiBudgetModLines();
	    for (int i = fiBudgetModLines.size()-1 ; i>=0 ; i--) {
    		FiBudgetModLine fiBudgetModLine = fiBudgetModLines.get(i);
    		if("N".equals(fiBudgetModLine.getModifyType()) && AjaxUtils.IS_DELETE_RECORD_TRUE.equals(fiBudgetModLine.getIsDeleteRecord())){
    			fiBudgetModLines.remove(fiBudgetModLine);
    			fiBudgetModLineDAO.delete(fiBudgetModLine);
    		}
		}
    }
    
    public void sortLines(FiBudgetModHead head) throws Exception{
    	List<FiBudgetModLine> fiBudgetModLines = head.getFiBudgetModLines();
    	fiBudgetModLines = StringTools.setBeanValue(fiBudgetModLines, "indexNo", null);
    	head.setFiBudgetModLines(fiBudgetModLines);
    }
    
    public void checkLines(FiBudgetModHead fiBudgetModHead,List errorMsgs,String identification){
    	List<FiBudgetModLine> fiBudgetModLines = fiBudgetModHead.getFiBudgetModLines();
    	if(fiBudgetModLines == null || fiBudgetModLines.size() == 0){
			String message = "必須至少要有一筆明細資料";
            siProgramLogAction.createProgramLog(PROGRAM_ID_MOD, MessageStatus.LOG_ERROR, identification, message, fiBudgetModHead.getLastUpdatedBy());
            errorMsgs.add(message);	
    	}
    	for (Iterator iterator = fiBudgetModLines.iterator(); iterator.hasNext();) {
    		try{
    			FiBudgetModLine fiBudgetModLine = (FiBudgetModLine) iterator.next();
				/*ImItemCategory imItemCategory = imItemCategoryService.findById(fiBudgetModHead.getBrandCode(), 
						"ItemBrand", fiBudgetModLine.getItemBrandCode());
				BuBrand buBrand = buBrandDAO.findById(fiBudgetModHead.getBrandCode());
				if(imItemCategory== null && !"1".equals(buBrand.getBranchCode()))
					throw new Exception("明細第"+fiBudgetModLine.getIndexNo()+"項查無此商品品牌");*/
				if(NumberUtils.getDouble(fiBudgetModLine.getBudgetAmount())+NumberUtils.getDouble(fiBudgetModLine.getBudgetAdjustAmount()) < 0)
					throw new Exception("明細第"+fiBudgetModLine.getIndexNo()+"項預算金額加調整金額不得為負數");
				if(NumberUtils.getDouble(fiBudgetModLine.getForecastAmount())+NumberUtils.getDouble(fiBudgetModLine.getForecastAdjustAmount()) < 0)
					throw new Exception("明細第"+fiBudgetModLine.getIndexNo()+"項預測金額加調整金額不得為負數");
				/*if(!StringUtils.hasText(fiBudgetModLine.getCategoryTypeCode1()))
					throw new Exception("明細第"+fiBudgetModLine.getIndexNo()+"項請選擇商品大類");
				if(!StringUtils.hasText(fiBudgetModLine.getCategoryTypeCode1()))
					throw new Exception("明細第"+fiBudgetModLine.getIndexNo()+"項請選擇商品小類");*/
    		}catch(Exception e){
    			String message = e.getMessage();
                siProgramLogAction.createProgramLog(PROGRAM_ID_MOD, MessageStatus.LOG_ERROR, identification, message, fiBudgetModHead.getLastUpdatedBy());
                errorMsgs.add(message);
    		}
		}
    }
    
    /**
     * 目標由業種過濾大類
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXItemType(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    try{
	    	String brandCode = httpRequest.getProperty("brandCode");
	    	String itemType = httpRequest.getProperty("itemType");
	    	List <ImItemCategory> category01s = imItemCategoryService.findByParentCategroyCode(brandCode, itemType);
		    category01s = AjaxUtils.produceSelectorData(category01s, "categoryCode", "categoryName", true, true);
	    	properties.setProperty("Category01s", AjaxUtils.parseSelectorData(category01s));
    	    result.add(properties);
    		return result;
		}catch(Exception ex){
		    log.error("查詢大類發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢業種大類發生錯誤，原因：" + ex.getMessage());
		}
	}
    
    /**
     * 目標由大類過濾中類
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findAJAXCategoryType(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    try{
	    	String brandCode = httpRequest.getProperty("brandCode");
	    	String category01 = httpRequest.getProperty("category01");
	    	List <ImItemCategory> category02s = imItemCategoryService.findByParentCategroyCode(brandCode, category01);
	    	category02s = AjaxUtils.produceSelectorData(category02s, "categoryCode", "categoryName", true, true);
	    	properties.setProperty("Category02s", AjaxUtils.parseSelectorData(category02s));
    	    result.add(properties);
    		return result;
		}catch(Exception ex){
		    log.error("查詢中類發生錯誤，原因：" + ex.toString());
		    throw new Exception("查詢業種中類發生錯誤，原因：" + ex.getMessage());
		}
	}
    
    public List<Properties> getItemBrandAJAX(Properties httpRequest) throws Exception{
    	log.info("getItemBrandAJAX");
    	List<Properties> result = new ArrayList();
    	Properties properties   = new Properties();
    	
		try{
			String brandCode     = httpRequest.getProperty("brandCode");
			String itemType     = httpRequest.getProperty("itemType");
			List<ImItemCategory> imItemCategorys = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.ITEM_BRAND, itemType);
//			if(imItemCategorys == null || imItemCategorys.size() == 0){
//				imItemCategorys = imItemCategoryDAO.findByParentCategroyCode(brandCode, imItemCategoryDAO.ITEM_BRAND, null);
//			}
			imItemCategorys = AjaxUtils.produceSelectorData(imItemCategorys, "categoryCode", "categoryName", true, true, "");
			properties.setProperty("allItemBrands", AjaxUtils.parseSelectorData(imItemCategorys));
			result.add(properties);
			return result;
		}catch (Exception ex) {
			log.info(ex.getMessage());
			throw new Exception(ex.getMessage());
		}
    }
    
}
