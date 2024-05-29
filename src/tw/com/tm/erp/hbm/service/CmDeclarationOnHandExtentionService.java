package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class CmDeclarationOnHandExtentionService {

	private static final Log log = LogFactory.getLog(CmDeclarationOnHandExtentionService.class);

	/**
	 * spring Ioc
	 */
	private BaseDAO baseDAO;
	private BuBrandDAO buBrandDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImItemCategoryDAO imItemCategoryDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private NativeQueryDAO nativeQueryDAO;
	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
	private CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}
	
	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setCmDeclarationOnHandDAO(CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
		this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}

	public void setCmDeclarationOnHandViewDAO(CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO) {
		this.cmDeclarationOnHandViewDAO = cmDeclarationOnHandViewDAO;
	}

	/**
	 * 報關庫存單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "customsWarehouseCode", "declarationNo", "declarationSeq",
		"remainDays" ,"declDate", "importDate", "warehouseInDate", "customsItemCode", "currentOnHandQty", "originalDate", "qty",
			 "expiryDate", "itemCName", "unitPrice", "categoryType", "category01", "category01Name",
			"category02", "category02Name", "category03", "itemBrand", "itemBrandName", "supplierCode", "supplierName" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "" };

	/**
	 * 鎖報關庫存查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_BLOCK_FIELD_NAMES = { "declarationNo", "declarationSeq", "declDate", "orderNo",
			"customsItemCode", "currentOnHandQty", "blockOnHandQuantity", "description" };

	public static final String[] GRID_SEARCH_BLOCK_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "" };

	public static final int[] GRID_SEARCH_BLOCK_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING };

	/**
	 * 鎖報單回傳欄位包涵index
	 */
	public static final String[] GRID_RETURN_BLOCK_FIELD_NAMES = { "indexNo", "declarationNo", "declarationSeq", "declDate",
			"orderNo", "customsItemCode", "currentOnHandQty", "blockOnHandQuantity", "description" };

	/**
	 * 儲存所需欄位
	 */
	public static final String[] GRID_SAVE_BLOCK_FIELD_NAMES = { "declarationNo", "declarationSeq", "customsItemCode",
			"blockOnHandQuantity", "description" };

	public static final int[] GRID_SAVE_BLOCK_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING };

	/**
	 * 查詢報關單庫存初始化
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeSearchInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String warehouseCode = (String) PropertyUtils.getProperty(otherBean, "warehouseCode");
			String declarationNo = (String) PropertyUtils.getProperty(otherBean, "declarationNo");
			String declarationSeqStart = (String) PropertyUtils.getProperty(otherBean, "declarationSeqStart");
			String declarationSeqEnd = (String) PropertyUtils.getProperty(otherBean, "declarationSeqEnd");
			String customsItemCode = (String) PropertyUtils.getProperty(otherBean, "customsItemCode");
			//String customsWarehouseCode = (String) PropertyUtils.getProperty(otherBean, "customsWarehouseCode");

			log.info("declarationNo =" + declarationNo);
			log.info("declarationSeqStart =" + declarationSeqStart);
			log.info("declarationSeqEnd =" + declarationSeqEnd);
			log.info("customsItemCode =" + customsItemCode);
			//log.info("customsWarehouseCode =" + customsWarehouseCode);
			
			resultMap.put("declarationNo", declarationNo);
			resultMap.put("declarationSeqStart", declarationSeqStart);
			resultMap.put("declarationSeqEnd", declarationSeqEnd);
			resultMap.put("customsItemCode", customsItemCode);

			
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(loginBrandCode, warehouseCode, "Y");
			if (imWarehouse != null) {
				resultMap.put("customsWarehouseCode", imWarehouse.getCustomsWarehouseCode());
			} else {
				/*if(customsWarehouseCode != null)
				{
					resultMap.put("customsWarehouseCode", customsWarehouseCode);
				}
				else
				{*/
					log.info("查無此倉庫" + warehouseCode + "關別");
				// throw new Exception("查無此倉庫"+warehouseCode+"關別");
				//}
			}
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());

			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> allCustomsWarehouseCodes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "CustomsWarehouseCode", "Y" }, "indexNo");
			List<ImItemCategory> allItemCategorys = imItemCategoryDAO.findCategoryByBrandCode(loginBrandCode,
					ImItemCategoryDAO.CATEGORY01, "Y");

			multiList.put("allCustomsWarehouseCodes", AjaxUtils.produceSelectorData(allCustomsWarehouseCodes, "lineCode",
					"name", false, true));
			multiList.put("allItemCategorys", AjaxUtils.produceSelectorData(allItemCategorys, "categoryCode",
					"categoryName", false, true));

			resultMap.put("multiList", multiList);
		} catch (Exception ex) {
			log.error("查詢表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "查詢表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}

	/**
	 * ajax picker按檢視返回選取的資料
	 *
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("timeScope:::"+timeScope);
			log.info(" searchKeys.size() = " + searchKeys.size());
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			if (result.size() > 0) {
				pickerResult.put("cmOnHandResult", result);
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

	
	public List<Properties> updateDeclarationExtention(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = 0;//AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = 10;//AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode =(String) PropertyUtils.getProperty(otherBean, "loginBrandCode");// httpRequest.getProperty("loginBrandCode");// 品牌代號
			String taxType = (String) PropertyUtils.getProperty(otherBean, "taxType");//httpRequest.getProperty("taxType");// 稅別
			String declarationNo = (String) PropertyUtils.getProperty(formBindBean, "declarationNo");//httpRequest.getProperty("declarationNo");
			Long declarationSeqStart = NumberUtils.getLong((String) PropertyUtils.getProperty(formBindBean, "declarationSeqStart"));//httpRequest.getProperty("declarationSeqStart"));
			Long declarationSeqEnd = NumberUtils.getLong((String) PropertyUtils.getProperty(formBindBean, "declarationSeqEnd"));//httpRequest.getProperty("declarationSeqEnd"));
			String remainDays = (String) PropertyUtils.getProperty(formBindBean, "remainDays");// httpRequest.getProperty("remainDays"); // 剩餘天數
			String isOverZero = (String) PropertyUtils.getProperty(formBindBean, "isOverZero");//httpRequest.getProperty("isOverZero"); // 是否剩餘天數大於0
			String showZeroStock = (String) PropertyUtils.getProperty(formBindBean, "showZeroStock");//httpRequest.getProperty("showZeroStock"); // 是否顯示庫存量為0之紀錄
			String showNegativeStock = (String) PropertyUtils.getProperty(formBindBean, "showNegativeStock");//httpRequest.getProperty("showNegativeStock"); // 是否顯示庫存量為負數之紀錄

			String fileNo = (String) PropertyUtils.getProperty(formBindBean, "fileNo");
			String customsItemCodes = (String) PropertyUtils.getProperty(formBindBean, "customsItemCodes");//httpRequest.getProperty("customsItemCodes"); // 多品號

			String category01 = (String) PropertyUtils.getProperty(formBindBean, "category01");//httpRequest.getProperty("category01");// 大類
			String itemBrand = (String) PropertyUtils.getProperty(formBindBean, "itemBrand");//httpRequest.getProperty("itemBrand");// 商品品牌
			Date warehouseInDate = DateUtils.parseDate("yyyy/MM/dd", (String) PropertyUtils.getProperty(formBindBean, "warehouseInDate"));//httpRequest.getProperty("warehouseInDate")); // 進倉日期
			declarationSeqStart = declarationSeqStart != 0L ? declarationSeqStart : null;
			declarationSeqEnd = declarationSeqEnd != 0L ? declarationSeqEnd : null;
			String customsItemCode = (String) PropertyUtils.getProperty(formBindBean, "customsItemCode");//httpRequest.getProperty("customsItemCode");
			String customsWarehouseCode = (String) PropertyUtils.getProperty(formBindBean, "customsWarehouseCode");//httpRequest.getProperty("customsWarehouseCode");

			log.info("taxType = " + taxType);
			log.info("declarationNo = " + declarationNo);
			log.info("declarationSeqStart = " + declarationSeqStart);
			log.info("declarationSeqEnd = " + declarationSeqEnd);
			log.info("remainDays = " + remainDays);
			log.info("isOverZero = " + isOverZero);
			log.info("showZeroStock = " + showZeroStock);
			log.info("customsItemCode = " + customsItemCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			log.info("category01 = " + category01);
			log.info("itemBrand = " + itemBrand);
			log.info("warehouseInDate = " + warehouseInDate);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			StringBuffer customsItemCodesSQL = new StringBuffer();
			if (!StringUtils.hasText(customsItemCodes)) {
				findObjs.put(" and model.customsItemCode = :customsItemCode", customsItemCode);
			} else { // 處理多品號
				String[] codes = customsItemCodes.split(",");
				
				findObjs.put(" and model.customsItemCode in (:codes)", codes);
			}
			StringBuffer remainDaysSQL = new StringBuffer();
			if ("Y".equalsIgnoreCase(isOverZero)) { // 預設是否剩餘天數大於0
				remainDaysSQL.append(" and model.remainDays > 0 ");
			}

			// 若為0L
			if ("0".equalsIgnoreCase(remainDays)) {
				remainDaysSQL.append(" and model.remainDays <= 0 ");
			} else {
				remainDays = NumberUtils.getLong(remainDays) != 0L ? remainDays : null;
				findObjs.put(" and model.remainDays <= :remainDaysStart", remainDays);
			}

			findObjs.put(remainDaysSQL.toString() + " and model.brandCode = :brandCode", brandCode);
			

			String compareStock = "";
			if ("N".equalsIgnoreCase(showZeroStock)) { // 不等於0,反之全部查
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 只勾負庫存表示只查小於0
					compareStock = "<";
				} else {
					compareStock = "<>"; // 都不勾表示預設指查大於0及小於0
				}
			} else {
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 全部勾選表示都查
					compareStock = "";
				} else {
					compareStock = ">="; // 勾零庫存表示只查大於等於0
				}
			}

			if (StringUtils.hasText(compareStock)) {
				findObjs.put(" and model.currentOnHandQty " + compareStock + " :currentOnHandQty", 0L);
			}

			findObjs.put(" and model.declarationNo = :declarationNo", declarationNo);
			findObjs.put(" and model.declarationSeq >= :declarationSeqStart", declarationSeqStart);
			findObjs.put(" and model.declarationSeq <= :declarationSeqEnd", declarationSeqEnd);

			findObjs.put(" and model.customsWarehouseCode = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and model.category01 = :category01", category01);
			findObjs.put(" and model.itemBrand = :itemBrand", itemBrand);
			findObjs.put(" and model.warehouseInDate = :warehouseInDate", warehouseInDate);

			// ==============================================================
			Map cmDeclarationOnHandViewMap = cmDeclarationOnHandViewDAO.search("CmDeclarationOnHandView as model", findObjs,
					"order by brandCode,customsWarehouseCode,declarationNo,declarationSeq,customsItemCode", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_ALL);
			List<CmDeclarationOnHandView> cmDeclarationOnHandViews = (List<CmDeclarationOnHandView>) cmDeclarationOnHandViewMap
					.get(BaseDAO.TABLE_LIST);

			if(cmDeclarationOnHandViews != null && cmDeclarationOnHandViews.size() > 0){
				System.out.println("!!!cmDeclarationOnHandViews!!:"+cmDeclarationOnHandViews.size());
				
				List<CmDeclarationHead> heads = new ArrayList();
				for(CmDeclarationOnHandView cm : cmDeclarationOnHandViews){
					CmDeclarationHead cmHead = cmDeclarationHeadDAO.findOneCmDeclaration(cm.getDeclarationNo());
					System.out.println("!!cmHead:"+cmHead.getDeclNo());
					boolean flag = true;
					for(CmDeclarationHead head:heads){
						if(head.getDeclNo().equals(cmHead.getDeclNo())){
							flag = false;
						}
					}
					if(flag) heads.add(cmHead);
				}
				
				for(CmDeclarationHead cmDeclHead:heads){
					if(!cmDeclHead.getDeclType().equals("D7")){
						Date importdt = cmDeclHead.getImportDate();
						if(null != cmDeclHead.getIsExtention() && cmDeclHead.getIsExtention() != ""){
							Map messageMap = new HashMap();
							messageMap.put("type", "ALERT");
							messageMap.put("message", "此報單已展延!");
							messageMap.put("event1", null);
							messageMap.put("event2", null);
							resultMap.put("vatMessage", messageMap);
							throw new Exception("報單已展延!");
						}else{
							java.text.Format formatter=new java.text.SimpleDateFormat("yyyy-MM-dd");  
							java.util.Date todayDate=importdt; 
							long afterTime=(todayDate.getTime()/1000)+60*60*24*365;  
							todayDate.setTime(afterTime*1000);  
							String afterDate=formatter.format(todayDate);  
							System.out.println(afterDate);
							
							cmDeclHead.setImportDate(todayDate);
							cmDeclHead.setIsExtention("Y");
							cmDeclHead.setFileNo(fileNo);
							cmDeclHead.setOrgImportDate(importdt);
							cmDeclarationHeadDAO.update(cmDeclHead);
							System.out.println("!!update end!!");
						}
					}
				}
			}
			
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/" + searchKeys.toString());

			List<Properties> selectResult = AjaxUtils.getSelectedResults(timeScope, searchKeys, true);
			log.info("selectResult.selectResult:" + selectResult.size());
			log.info(selectResult);
			
			/*
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						cmDeclarationOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}*/
			
			
			
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
	
	public List<Properties> updateExtention(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....updateExtention...."+httpRequest);
		String timeScope = httpRequest.getProperty("timeScope");
		String[] searchKey = {"brandCode","customsWarehouseCode","declarationNo","declarationSeq","importDate"};
		List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKey);
		System.out.println("!!result!!:"+result);
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			String errorMsg = null;

			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					GRID_RETURN_BLOCK_FIELD_NAMES);
			System.out.println("!!upRecords!!:"+upRecords);
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					String declarationNo = upRecord.getProperty(GRID_SAVE_BLOCK_FIELD_NAMES[0]);
					if (StringUtils.hasText(declarationNo)) {
						CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(upRecord
								.getProperty(GRID_SEARCH_BLOCK_FIELD_NAMES[0]), Long.parseLong(upRecord
								.getProperty(GRID_SEARCH_BLOCK_FIELD_NAMES[1])), "FW", "T2");
						AjaxUtils.setPojoProperties(cmDeclarationOnHand, upRecord, GRID_SAVE_BLOCK_FIELD_NAMES,
								GRID_SAVE_BLOCK_FIELD_TYPES);
						cmDeclarationOnHandDAO.update(cmDeclarationOnHand);
					}
				}
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新鎖報單庫存發生錯誤，原因：" + ex.toString());
			throw new Exception("更新鎖報單庫存發生錯誤失敗！");
		}
	}
	
	/**
	 * ajax 取得報關單庫存search分頁
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

			String taxType = httpRequest.getProperty("taxType");// 稅別
			// String adjustmentType =
			// httpRequest.getProperty("adjustmentType");// 調整類別

			String declarationNo = httpRequest.getProperty("declarationNo");
			Long declarationSeqStart = NumberUtils.getLong(httpRequest.getProperty("declarationSeqStart"));
			Long declarationSeqEnd = NumberUtils.getLong(httpRequest.getProperty("declarationSeqEnd"));

			String remainDays = httpRequest.getProperty("remainDays"); // 剩餘天數
			String isOverZero = httpRequest.getProperty("isOverZero"); // 是否剩餘天數大於0
			String showZeroStock = httpRequest.getProperty("showZeroStock"); // 是否顯示庫存量為0之紀錄
			String showNegativeStock = httpRequest.getProperty("showNegativeStock"); // 是否顯示庫存量為負數之紀錄

			String customsItemCodes = httpRequest.getProperty("customsItemCodes"); // 多品號

			String category01 = httpRequest.getProperty("category01");// 大類
			String itemBrand = httpRequest.getProperty("itemBrand");// 商品品牌
			Date warehouseInDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("warehouseInDate")); // 進倉日期

			declarationSeqStart = declarationSeqStart != 0L ? declarationSeqStart : null;
			declarationSeqEnd = declarationSeqEnd != 0L ? declarationSeqEnd : null;

			String customsItemCode = httpRequest.getProperty("customsItemCode");
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");

			log.info("taxType = " + taxType);
			// log.info("adjustmentType = " + adjustmentType );

			log.info("declarationNo = " + declarationNo);
			log.info("declarationSeqStart = " + declarationSeqStart);
			log.info("declarationSeqEnd = " + declarationSeqEnd);
			log.info("remainDays = " + remainDays);
			log.info("isOverZero = " + isOverZero);
			log.info("showZeroStock = " + showZeroStock);
			log.info("customsItemCode = " + customsItemCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			log.info("category01 = " + category01);
			log.info("itemBrand = " + itemBrand);
			log.info("warehouseInDate = " + warehouseInDate);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			StringBuffer customsItemCodesSQL = new StringBuffer();
			if (!StringUtils.hasText(customsItemCodes)) {
				findObjs.put(" and model.customsItemCode = :customsItemCode", customsItemCode);
			} else { // 處理多品號
				String[] codes = customsItemCodes.split(",");
				/* 修改新的寫法 by Weichun 2011.12.06
				StringBuffer newCodes = new StringBuffer();
				for (String code : codes) {
					newCodes.append("'").append(code).append("'").append(",");
				}
				if (codes.length > 0) {
					newCodes.delete(newCodes.length() - 1, newCodes.length());
				}
				customsItemCodesSQL.append(" and model.customsItemCode in (").append(newCodes).append(")");
				*/
				findObjs.put(" and model.customsItemCode in (:codes)", codes);
			}
			StringBuffer remainDaysSQL = new StringBuffer();
			if ("Y".equalsIgnoreCase(isOverZero)) { // 預設是否剩餘天數大於0
				remainDaysSQL.append(" and model.remainDays > 0 ");
			}

			// 若為0L
			if ("0".equalsIgnoreCase(remainDays)) {
				remainDaysSQL.append(" and model.remainDays <= 0 ");
			} else {
				remainDays = NumberUtils.getLong(remainDays) != 0L ? remainDays : null;
				findObjs.put(" and model.remainDays <= :remainDaysStart", remainDays);
			}

			findObjs.put(remainDaysSQL.toString() + " and model.brandCode = :brandCode", brandCode);
			// if("F".equals(taxType) && ("12".equals(adjustmentType) ||
			// "41".equals(adjustmentType) || "51".equals(adjustmentType) ||
			// "61".equals(adjustmentType))){ // 保稅:盤虧,報廢,保稅轉完稅,領用除帳
			// findObjs.put(" and model.currentOnHandQty > :currentOnHandQty",0L);
			// }

			String compareStock = "";
			if ("N".equalsIgnoreCase(showZeroStock)) { // 不等於0,反之全部查
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 只勾負庫存表示只查小於0
					compareStock = "<";
				} else {
					compareStock = "<>"; // 都不勾表示預設指查大於0及小於0
				}
			} else {
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 全部勾選表示都查
					compareStock = "";
				} else {
					compareStock = ">="; // 勾零庫存表示只查大於等於0
				}
			}

			if (StringUtils.hasText(compareStock)) {
				findObjs.put(" and model.currentOnHandQty " + compareStock + " :currentOnHandQty", 0L);
			}

			findObjs.put(" and model.declarationNo = :declarationNo", declarationNo);
			findObjs.put(" and model.declarationSeq >= :declarationSeqStart", declarationSeqStart);
			findObjs.put(" and model.declarationSeq <= :declarationSeqEnd", declarationSeqEnd);

			findObjs.put(" and model.customsWarehouseCode = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and model.category01 = :category01", category01);
			findObjs.put(" and model.itemBrand = :itemBrand", itemBrand);
			findObjs.put(" and model.warehouseInDate = :warehouseInDate", warehouseInDate);

			// ==============================================================
			Map cmDeclarationOnHandViewMap = cmDeclarationOnHandViewDAO.search("CmDeclarationOnHandView as model", findObjs,
					"order by brandCode,customsWarehouseCode,declarationNo,declarationSeq,customsItemCode", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<CmDeclarationOnHandView> cmDeclarationOnHandViews = (List<CmDeclarationOnHandView>) cmDeclarationOnHandViewMap
					.get(BaseDAO.TABLE_LIST);

			if (cmDeclarationOnHandViews != null && cmDeclarationOnHandViews.size() > 0) {

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) cmDeclarationOnHandViewDAO.search("CmDeclarationOnHandView as model",
						"count(model.declarationNo) as rowCount", findObjs,
						"order by brandCode,customsWarehouseCode,declarationNo,declarationSeq,customsItemCode",
						BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
				// INDEX

				// 補上其他欄位
				setOtherColumn(brandCode, cmDeclarationOnHandViews);

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						cmDeclarationOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的報關單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的報關單查詢失敗！");
		}
	}

	/**
	 * sql 匯出excel
	 *
	 * @param httpRequest
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	public SelectDataInfo getAJAXExportData(HttpServletRequest httpRequest) throws Exception {
		StringBuffer sql = new StringBuffer();
		Object[] object = null;
		List rowData = new ArrayList();
		try {
			String brandCode = httpRequest.getParameter("brandCode");
			String customsWarehouseCode = httpRequest.getParameter("customsWarehouseCode");
			String declarationNo = httpRequest.getParameter("declarationNo");
			Long declarationSeqStart = NumberUtils.getLong(httpRequest.getParameter("declarationSeqStart"));
			Long declarationSeqEnd = NumberUtils.getLong(httpRequest.getParameter("declarationSeqEnd"));
			String customsItemCode = httpRequest.getParameter("customsItemCode");

			String remainDays = httpRequest.getParameter("remainDays"); // 剩餘天數
			String isOverZero = httpRequest.getParameter("isOverZero"); // 是否剩餘天數大於0
			String showZeroStock = httpRequest.getParameter("showZeroStock"); // 是否顯示庫存為0
			String showNegativeStock = httpRequest.getParameter("showNegativeStock"); // 是否顯示庫存為負數

			String customsItemCodes = httpRequest.getParameter("customsItemCodes");

			String category01 = httpRequest.getParameter("category01");
			String itemBrand = httpRequest.getParameter("itemBrand");
			String warehouseInDate = httpRequest.getParameter("warehouseInDate");

			log.info("brandCode = " + brandCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			log.info("declarationNo = " + declarationNo);
			log.info("declarationSeqStart = " + declarationSeqStart);
			log.info("declarationSeqEnd = " + declarationSeqEnd);
			log.info("customsItemCode = " + customsItemCode);
			log.info("remainDays = " + remainDays);
			log.info("isOverZero = " + isOverZero);
			log.info("showZeroStock = " + showZeroStock);
			log.info("showNegativeStock = " + showNegativeStock);
			log.info("customsItemCodes = " + customsItemCodes);
			log.info("category01 = " + category01);
			log.info("itemBrand = " + itemBrand);
			log.info("warehouseInDate = " + warehouseInDate);

			sql.append("SELECT CUSTOMS_WAREHOUSE_CODE, DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, IMPORT_DATE, ").append(
					"WAREHOUSE_IN_DATE, CUSTOMS_ITEM_CODE, CURRENT_ON_HAND_QTY, ORIGINAL_DATE, QTY, ").append(
					"REMAIN_DAYS, EXPIRY_DATE, ITEM_C_NAME, '售價', CATEGORY_TYPE, CATEGORY01, ").append(
					"CATEGORY01_NAME, CATEGORY02, CATEGORY02_NAME, CATEGORY03, ITEM_BRAND, ").append(
					"ITEM_BRAND_NAME, SUPPLIER_CODE, SUPPLIER_NAME ").append("FROM CM_DECLARATION_ON_HAND_VIEW ").append(
					"WHERE BRAND_CODE = '").append(brandCode).append("' ");

			String compareStock = "";
			if ("N".equalsIgnoreCase(showZeroStock)) { // 不等於0,反之全部查
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 只勾負庫存表示只查小於0
					compareStock = "<";
				} else {
					compareStock = "<>"; // 都不勾表示預設指查大於0及小於0
				}
			} else {
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 全部勾選表示都查
					compareStock = "";
				} else {
					compareStock = ">="; // 勾零庫存表示只查大於等於0
				}
			}

			if (StringUtils.hasText(compareStock)) {
				sql.append("AND CURRENT_ON_HAND_QTY " + compareStock + " 0 ");
			}

			if (StringUtils.hasText(declarationNo)) {
				sql.append("AND DECLARATION_NO = '").append(declarationNo).append("' ");
			}

			if (declarationSeqStart != 0L) {
				sql.append("AND DECLARATION_SEQ >= ").append(declarationSeqStart).append(" ");
			}

			if (declarationSeqEnd != 0L) {
				sql.append("AND DECLARATION_SEQ <= ").append(declarationSeqEnd).append(" ");
			}

			if (StringUtils.hasText(customsWarehouseCode)) {
				sql.append("AND CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("' ");
			}

			if (!StringUtils.hasText(customsItemCodes)) {
				if (StringUtils.hasText(customsItemCode)) {
					sql.append("AND CUSTOMS_ITEM_CODE = '").append(customsItemCode).append("' ");
				}
			} else {
				String[] codes = customsItemCodes.split(",");
				StringBuffer newCodes = new StringBuffer();
				for (String code : codes) {
					newCodes.append("'").append(code).append("'").append(",");
				}
				if (codes.length > 0) {
					newCodes.delete(newCodes.length() - 1, newCodes.length());
				}
				sql.append("AND CUSTOMS_ITEM_CODE in (").append(newCodes).append(")");
			}

			// 若為0L
			if ("0".equalsIgnoreCase(remainDays)) {
				sql.append("AND REMAIN_DAYS <= 0 ");
			} else {
				remainDays = NumberUtils.getLong(remainDays) != 0L ? remainDays : null;
				if (StringUtils.hasText(remainDays)) {
					sql.append("AND REMAIN_DAYS <= ").append(remainDays).append(" ");
				}
			}

			if (StringUtils.hasText(isOverZero)) {
				if ("Y".equalsIgnoreCase(isOverZero)) {
					sql.append("AND REMAIN_DAYS > ").append(0).append(" ");
				}
			}

			if (StringUtils.hasText(category01)) {
				sql.append("AND CATEGORY01 = '").append(category01).append("' ");
			}

			if (StringUtils.hasText(itemBrand)) {
				sql.append("AND ITEM_BRAND = '").append(itemBrand).append("' ");
			}

			if (StringUtils.hasText(warehouseInDate)) {
				sql.append("AND WAREHOUSE_IN_DATE = '").append(warehouseInDate).append("' ");
			}

			sql.append("ORDER BY BRAND_CODE, CUSTOMS_WAREHOUSE_CODE, DECLARATION_NO, DECLARATION_SEQ, CUSTOMS_ITEM_CODE ");

			List lists = nativeQueryDAO.executeNativeSql(sql.toString());

			object = new Object[] { "customsWarehouseCode", "declarationNo", "declarationSeq", "declDate", "importDate",
					"warehouseInDate", "customsItemCode", "currentOnHandQty", "originalDate", "qty", "remainDays",
					"expiryDate", "itemCName", "unitPrice", "categoryType", "category01", "category01Name", "category02",
					"category02Name", "category03", "itemBrand", "itemBrandName", "supplierCode", "supplierName" };

			log.info(" lists.size() = " + lists.size());
			for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
				Object[] getObj = (Object[]) lists.get(i);
				Object[] dataObject = new Object[object.length];
				for (int j = 0; j < object.length; j++) {
					if (j == 13) { // 額外塞售價
						String itemCode = String.valueOf(getObj[6]);
						// log.info(" itemCode = " +itemCode);
						List objs = (List) imItemCurrentPriceViewDAO.findByProperty("ImItemCurrentPriceView", "unitPrice",
								"and brandCode = ? and itemCode = ? and typeCode = ?", new Object[] { brandCode, itemCode,
										"1" }, "");
						if (null != objs && objs.size() > 0) {
							Object unitPrice = objs.get(0);
							// log.info(" unitPrice = " +unitPrice);
							dataObject[j] = NumberUtils.getDouble(String.valueOf(unitPrice));
						}// else{
						// dataObject[j] = "";
						// }
					} else {
						dataObject[j] = getObj[j];
					}
				}
				rowData.add(dataObject);
			}

			return new SelectDataInfo(object, rowData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("匯出excel發生錯誤，原因：" + e.toString());
			throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
		}
	}

	/**
	 * 將報關庫存單查詢結果存檔
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		System.out.println("======================================saveSearchResult====================================");
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * 設定其他欄位
	 *
	 * @param brandCode
	 * @param cmDeclarationOnHandViews
	 */
	private void setOtherColumn(String brandCode, List<CmDeclarationOnHandView> cmDeclarationOnHandViews) {
		for (CmDeclarationOnHandView cmDeclarationOnHandView : cmDeclarationOnHandViews) {
			String customsItemCode = cmDeclarationOnHandView.getCustomsItemCode();
			List objs = (List) imItemCurrentPriceViewDAO.findByProperty("ImItemCurrentPriceView", "unitPrice",
					"and brandCode = ? and itemCode = ? and typeCode = ?", new Object[] { brandCode, customsItemCode, "1" },
					"");
			log.info("brandCode = " + brandCode);
			log.info("itemCode = " + customsItemCode);
			log.info("objs = " + objs);
			if (null != objs && objs.size() > 0) {
				Object unitPrice = objs.get(0);
				cmDeclarationOnHandView.setUnitPrice(NumberUtils.getDouble(String.valueOf(unitPrice)));
			}
		}
	}

	/**
	 * ajax 取得鎖報單庫存search分頁
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchBlockPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

			String taxType = httpRequest.getProperty("taxType");// 稅別
			// String adjustmentType =
			// httpRequest.getProperty("adjustmentType");// 調整類別

			String declarationNo = httpRequest.getProperty("declarationNo");
			Long declarationSeqStart = NumberUtils.getLong(httpRequest.getProperty("declarationSeqStart"));
			Long declarationSeqEnd = NumberUtils.getLong(httpRequest.getProperty("declarationSeqEnd"));

			String remainDays = httpRequest.getProperty("remainDays"); // 剩餘天數
			String isOverZero = httpRequest.getProperty("isOverZero"); // 是否剩餘天數大於0
			String showZeroStock = httpRequest.getProperty("showZeroStock"); // 是否顯示庫存量為0之紀錄
			String showNegativeStock = httpRequest.getProperty("showNegativeStock"); // 是否顯示庫存量為負數之紀錄
			String showBlockStock = httpRequest.getProperty("showBlockStock"); // 顯示鎖報單之紀錄
			String customsItemCodes = httpRequest.getProperty("customsItemCodes"); // 多品號

			String category01 = httpRequest.getProperty("category01");// 大類
			String itemBrand = httpRequest.getProperty("itemBrand");// 商品品牌
			Date warehouseInDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("warehouseInDate")); // 進倉日期

			declarationSeqStart = declarationSeqStart != 0L ? declarationSeqStart : null;
			declarationSeqEnd = declarationSeqEnd != 0L ? declarationSeqEnd : null;

			String customsItemCode = httpRequest.getProperty("customsItemCode");
			String customsWarehouseCode = "FW"; // 只抓出FW關別的庫存
			String orderNo = httpRequest.getProperty("orderNo");

			log.info("taxType = " + taxType);
			// log.info("adjustmentType = " + adjustmentType );

			log.info("declarationNo = " + declarationNo);
			log.info("declarationSeqStart = " + declarationSeqStart);
			log.info("declarationSeqEnd = " + declarationSeqEnd);
			log.info("remainDays = " + remainDays);
			log.info("isOverZero = " + isOverZero);
			log.info("showZeroStock = " + showZeroStock);
			log.info("customsItemCode = " + customsItemCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			log.info("category01 = " + category01);
			log.info("itemBrand = " + itemBrand);
			log.info("warehouseInDate = " + warehouseInDate);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			StringBuffer customsItemCodesSQL = new StringBuffer();
			if (!StringUtils.hasText(customsItemCodes)) {
				findObjs.put(" and model.customsItemCode = :customsItemCode", customsItemCode);
			} else { // 處理多品號
				String[] codes = customsItemCodes.split(",");
				/* 修改新的寫法 by Weichun 2011.12.06
				StringBuffer newCodes = new StringBuffer();
				for (String code : codes) {
					newCodes.append("'").append(code).append("'").append(",");
				}
				if (codes.length > 0) {
					newCodes.delete(newCodes.length() - 1, newCodes.length());
				}
				customsItemCodesSQL.append(" and model.customsItemCode in (").append(newCodes).append(")");
				*/
				findObjs.put(" and model.customsItemCode in (:codes)", codes);
			}
			StringBuffer remainDaysSQL = new StringBuffer();
			if ("Y".equalsIgnoreCase(isOverZero)) { // 預設是否剩餘天數大於0
				remainDaysSQL.append(" and model.remainDays > 0 ");
			}

			// 若為0L
			if ("0".equalsIgnoreCase(remainDays)) {
				remainDaysSQL.append(" and model.remainDays <= 0 ");
			} else {
				remainDays = NumberUtils.getLong(remainDays) != 0L ? remainDays : null;
				findObjs.put(" and model.remainDays <= :remainDaysStart", remainDays);
			}

			findObjs.put(customsItemCodesSQL.toString() + remainDaysSQL.toString() + " and model.brandCode = :brandCode",
					brandCode);
			// if("F".equals(taxType) && ("12".equals(adjustmentType) ||
			// "41".equals(adjustmentType) || "51".equals(adjustmentType) ||
			// "61".equals(adjustmentType))){ // 保稅:盤虧,報廢,保稅轉完稅,領用除帳
			// findObjs.put(" and model.currentOnHandQty > :currentOnHandQty",0L);
			// }

			String compareStock = "";
			if ("N".equalsIgnoreCase(showZeroStock)) { // 不等於0,反之全部查
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 只勾負庫存表示只查小於0
					compareStock = "<";
				} else {
					compareStock = "<>"; // 都不勾表示預設指查大於0及小於0
				}
			} else {
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 全部勾選表示都查
					compareStock = "";
				} else {
					compareStock = ">="; // 勾零庫存表示只查大於等於0
				}
			}

			if (StringUtils.hasText(compareStock)) {
				findObjs.put(" and model.currentOnHandQty " + compareStock + " :currentOnHandQty", 0L);
			}

			findObjs.put(" and model.declarationNo = :declarationNo", declarationNo);
			findObjs.put(" and model.declarationSeq >= :declarationSeqStart", declarationSeqStart);
			findObjs.put(" and model.declarationSeq <= :declarationSeqEnd", declarationSeqEnd);

			findObjs.put(" and model.customsWarehouseCode = :customsWarehouseCode", customsWarehouseCode);
			findObjs.put(" and model.category01 = :category01", category01);
			findObjs.put(" and model.itemBrand = :itemBrand", itemBrand);
			findObjs.put(" and model.warehouseInDate = :warehouseInDate", warehouseInDate);

			if (StringUtils.hasText(orderNo)) {
				findObjs.put(" and model.orderNo = :orderNo", orderNo);
			}

			if ("Y".equalsIgnoreCase(showBlockStock)) // 指搜尋有被鎖住的報單庫存
				findObjs.put(" and model.blockOnHandQuantity > :blockOnHandQuantity", 0D);

			// ==============================================================
			log.info("條件:"+findObjs.toString());
			Map cmDeclarationOnHandViewMap = cmDeclarationOnHandViewDAO.search("CmDeclarationOnHandView as model", findObjs,
					"order by brandCode,customsWarehouseCode,declarationNo,declarationSeq,customsItemCode", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			List<CmDeclarationOnHandView> cmDeclarationOnHandViews = (List<CmDeclarationOnHandView>) cmDeclarationOnHandViewMap
					.get(BaseDAO.TABLE_LIST);

			if (cmDeclarationOnHandViews != null && cmDeclarationOnHandViews.size() > 0) {

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) cmDeclarationOnHandViewDAO.search("CmDeclarationOnHandView as model",
						"count(model.declarationNo) as rowCount", findObjs,
						"order by brandCode,customsWarehouseCode,declarationNo,declarationSeq,customsItemCode",
						BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
																						// INDEX

				// 補上其他欄位
				setOtherColumn(brandCode, cmDeclarationOnHandViews);

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_BLOCK_FIELD_NAMES,
						GRID_SEARCH_BLOCK_FIELD_DEFAULT_VALUES, cmDeclarationOnHandViews, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_BLOCK_FIELD_NAMES,
						GRID_SEARCH_BLOCK_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的報關單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的報關單查詢失敗！");
		}
	}

	/**
	 * 更新鎖報單明細資料
	 *
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....鎖報單明細....");
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			String errorMsg = null;

			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					GRID_RETURN_BLOCK_FIELD_NAMES);
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					String declarationNo = upRecord.getProperty(GRID_SAVE_BLOCK_FIELD_NAMES[0]);
					if (StringUtils.hasText(declarationNo)) {
						CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(upRecord
								.getProperty(GRID_SEARCH_BLOCK_FIELD_NAMES[0]), Long.parseLong(upRecord
								.getProperty(GRID_SEARCH_BLOCK_FIELD_NAMES[1])), "FW", "T2");
						AjaxUtils.setPojoProperties(cmDeclarationOnHand, upRecord, GRID_SAVE_BLOCK_FIELD_NAMES,
								GRID_SAVE_BLOCK_FIELD_TYPES);
						cmDeclarationOnHandDAO.update(cmDeclarationOnHand);
					}
				}
			}

			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新鎖報單庫存發生錯誤，原因：" + ex.toString());
			throw new Exception("更新鎖報單庫存發生錯誤失敗！");
		}
	}
	
	/**
	 * sql 匯出excel 報關單庫存查詢鎖定匯出資料
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public SelectDataInfo getAJAXExportData2(HttpServletRequest httpRequest) throws Exception {
		StringBuffer sql = new StringBuffer();
		Object[] object = null;
		List rowData = new ArrayList();
		try {
			// ======================帶入Head的值=========================
			
			String brandCode = httpRequest.getParameter("loginBrandCode");// 品牌代號

			String taxType = httpRequest.getParameter("taxType");// 稅別
			// String adjustmentType =
			// httpRequest.getProperty("adjustmentType");// 調整類別

			String declarationNo = httpRequest.getParameter("declarationNo"); //報關單號
			Long declarationSeqStart = NumberUtils.getLong(httpRequest.getParameter("declarationSeqStart")); //報關序號開始
			Long declarationSeqEnd = NumberUtils.getLong(httpRequest.getParameter("declarationSeqEnd")); //報關序號結束

			String remainDays = httpRequest.getParameter("remainDays"); // 剩餘天數
			String isOverZero = httpRequest.getParameter("isOverZero"); // 是否剩餘天數大於0
			String showZeroStock = httpRequest.getParameter("showZeroStock"); // 是否顯示庫存量為0之紀錄
			String showNegativeStock = httpRequest.getParameter("showNegativeStock"); // 是否顯示庫存量為負數之紀錄
			String showBlockStock = httpRequest.getParameter("showBlockStock"); // 顯示鎖報單之紀錄
			String customsItemCodes = httpRequest.getParameter("customsItemCodes"); // 多品號

			String category01 = httpRequest.getParameter("category01");// 大類
			String itemBrand = httpRequest.getParameter("itemBrand");// 商品品牌
			Date warehouseInDate = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getParameter("warehouseInDate")); // 進倉日期

			declarationSeqStart = declarationSeqStart != 0L ? declarationSeqStart : null;
			declarationSeqEnd = declarationSeqEnd != 0L ? declarationSeqEnd : null;

			String customsItemCode = httpRequest.getParameter("customsItemCode");
			String customsWarehouseCode = "FW"; // 只抓出FW關別的庫存
			String orderNo = httpRequest.getParameter("orderNo"); //進貨單號

			log.info("taxType = " + taxType);
			// log.info("adjustmentType = " + adjustmentType );

			log.info("declarationNo = " + declarationNo);
			log.info("declarationSeqStart = " + declarationSeqStart);
			log.info("declarationSeqEnd = " + declarationSeqEnd);
			log.info("remainDays = " + remainDays);
			log.info("isOverZero = " + isOverZero);
			log.info("showZeroStock = " + showZeroStock);
			log.info("customsItemCode = " + customsItemCode);
			log.info("customsWarehouseCode = " + customsWarehouseCode);
			log.info("category01 = " + category01);
			log.info("itemBrand = " + itemBrand);
			log.info("warehouseInDate = " + warehouseInDate);
			
			sql.append("SELECT DECLARATION_NO, DECLARATION_SEQ, DECL_DATE, ORDER_NO, ").append(
			"CUSTOMS_ITEM_CODE, CURRENT_ON_HAND_QTY, BLOCK_ON_HAND_QUANTITY,DESCRIPTION, QTY, ").append(
			"REMAIN_DAYS, EXPIRY_DATE, ITEM_C_NAME, '售價', CATEGORY01, ").append(
			"CATEGORY01_NAME, CATEGORY02, CATEGORY02_NAME,ITEM_BRAND, ").append(
			"ITEM_BRAND_NAME, SUPPLIER_CODE, SUPPLIER_NAME ").append("FROM CM_DECLARATION_ON_HAND_VIEW ").append(
			"WHERE BRAND_CODE = '").append(brandCode).append("' ");

			//HashMap map = new HashMap();
			//HashMap findObjs = new HashMap();
			//StringBuffer customsItemCodesSQL = new StringBuffer();
			if (!StringUtils.hasText(customsItemCodes)) {
				//sql.append(" and CUSTOMS_ITEM_CODE = '").append(customsItemCode).append("' ");
			} else { // 處理多品號
				String[] codes = customsItemCodes.split(",");
				StringBuffer newCodes = new StringBuffer();
				for (String code : codes) {
					newCodes.append("'").append(code).append("'").append(",");
				}
				if (codes.length > 0) {
					newCodes.delete(newCodes.length() - 1, newCodes.length());
					sql.append(" and CUSTOMS_ITEM_CODE in (").append(newCodes).append(")");
				}
				
				//findObjs.put(" and model.customsItemCode in (:codes)", codes);
			}
			
			if ("Y".equalsIgnoreCase(isOverZero)) { // 預設是否剩餘天數大於0
				sql.append(" and REMAIN_DAYS > 0 ");
			}

			// 若為0L
			if ("0".equalsIgnoreCase(remainDays)) {
				sql.append(" and REMAIN_DAYS <= 0 ");
			} else {
				remainDays = NumberUtils.getLong(remainDays) != 0L ? remainDays : null;
				if(remainDays !=null){
					sql.append(" and REMAIN_DAYS <= ").append(remainDays);
				}
			}

			//sql.append(" and model.brandCode = '").append(brandCode).append("' ");
			// if("F".equals(taxType) && ("12".equals(adjustmentType) ||
			// "41".equals(adjustmentType) || "51".equals(adjustmentType) ||
			// "61".equals(adjustmentType))){ // 保稅:盤虧,報廢,保稅轉完稅,領用除帳
			// findObjs.put(" and model.currentOnHandQty > :currentOnHandQty",0L);
			// }

			String compareStock = "";
			if ("N".equalsIgnoreCase(showZeroStock)) { // 不等於0,反之全部查
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 只勾負庫存表示只查小於0
					compareStock = "<";
				} else {
					compareStock = "<>"; // 都不勾表示預設指查大於0及小於0
				}
			} else {
				if ("Y".equalsIgnoreCase(showNegativeStock)) { // 全部勾選表示都查
					compareStock = "";
				} else {
					compareStock = ">="; // 勾零庫存表示只查大於等於0
				}
			}

			if (StringUtils.hasText(compareStock)) {
				sql.append(" AND CURRENT_ON_HAND_QTY " + compareStock + " 0 ");
			}

			if (StringUtils.hasText(declarationNo)) {
				sql.append(" AND DECLARATION_NO = '").append(declarationNo).append("' ");
			}
			//sql.append(" and model.declarationSeq >= " + declarationSeqStart);
			if (declarationSeqStart != null) {
				sql.append(" AND DECLARATION_SEQ >= ").append(declarationSeqStart).append(" ");
			}
			//sql.append(" and model.declarationSeq <= " + declarationSeqEnd);
			if (declarationSeqEnd != null) {
				sql.append(" AND DECLARATION_SEQ <= ").append(declarationSeqEnd).append(" ");
			}

			//sql.append(" and model.customsWarehouseCode = " + customsWarehouseCode);
			if (StringUtils.hasText(customsWarehouseCode)) {
				sql.append(" AND CUSTOMS_WAREHOUSE_CODE = '").append(customsWarehouseCode).append("' ");
			}
			//sql.append(" and model.category01 = " + category01);
			if (StringUtils.hasText(category01)) {
				sql.append(" AND CATEGORY01 = '").append(category01).append("' ");
			}
			//sql.append(" and model.itemBrand = " + itemBrand);
			if (StringUtils.hasText(itemBrand)) {
				sql.append(" AND ITEM_BRAND = '").append(itemBrand).append("' ");
			}
			if(warehouseInDate !=null){
				sql.append(" and WAREHOUSE_IN_DATE = " + warehouseInDate);
			}
			

			if (StringUtils.hasText(orderNo)) {
				sql.append(" and ORDER_NO = " + orderNo);
			}

			if ("Y".equalsIgnoreCase(showBlockStock)) // 指搜尋有被鎖住的報單庫存
				sql.append(" and BLOCK_ON_HAND_QUANTITY > 0");

			// ==============================================================
			log.info(sql.toString());
			List lists = nativeQueryDAO.executeNativeSql(sql.toString());

			object = new Object[] { "declarationNo", "declarationSeq", "declDate", "orderNo",
					"customsItemCode", "currentOnHandQty", "blockOnHandQuantity","description" ,"qty", "remainDays",
					"expiryDate", "itemCName", "unitPrice", "category01", "category01Name", "category02",
					"category02Name", "itemBrand", "itemBrandName", "supplierCode", "supplierName" };

			log.info(" lists.size() = " + lists.size());
			for (int i = 0; i < (lists.size() > 65535 ? 65535 : lists.size()); i++) {
				Object[] getObj = (Object[]) lists.get(i);
				Object[] dataObject = new Object[object.length];
				for (int j = 0; j < object.length; j++) {
					if (j == 12) { // 額外塞售價
						String itemCode = String.valueOf(getObj[4]);
						//log.info(" itemCode = " +itemCode);
						List objs = (List) imItemCurrentPriceViewDAO.findByProperty("ImItemCurrentPriceView", "unitPrice",
								"and brandCode = ? and itemCode = ? and typeCode = ?", new Object[] { brandCode, itemCode,
										"1" }, "");
						if (null != objs && objs.size() > 0) {
							Object unitPrice = objs.get(0);
							//log.info(" unitPrice = " +unitPrice);
							dataObject[j] = NumberUtils.getDouble(String.valueOf(unitPrice));
						}// else{
						// dataObject[j] = "";
						// }
					} else {
						dataObject[j] = getObj[j];
					}
				}
				rowData.add(dataObject);
			}
			return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("匯出excel發生錯誤，原因：" + ex.toString());
			throw new Exception("匯出excel發生錯誤，原因：" + ex.getMessage());
		}
	}

}
