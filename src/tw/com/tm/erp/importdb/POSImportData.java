package tw.com.tm.erp.importdb;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCompose;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.ImItemComposeDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SiSystemLogService;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.hbm.service.SoSalesOrderService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.ValidateUtil;

/**
 * POS 資料匯入 TODO : 要判斷是否已經關帳,是否重匯
 * 
 * @author T02049
 * 
 */
public class POSImportData implements ImportDataAbs {
    private static final Log log = LogFactory.getLog(POSImportData.class);
    public static final String split[] = { "," };

    public ImportInfo initial(HashMap uiProperties) {
	log.info("POSImportData.initial");
	ImportInfo imInfo = new ImportInfo();

	// set entity class name
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoSalesOrderHead.class.getName());
	imInfo.setSplit(split);

	// set key info
	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("S0");
	imInfo.setSaveKeyField(true);

	// S0 銷售單類型(固定) 銷貨類型 MDATE(銷售日期) MHCU_NO(客戶代號) MSNO(原訂單編號) 付款條件 國別代碼(固定)
	// 幣別代碼(固定) MSHOPNO(專櫃代號)
	imInfo.addFieldName("s0");
	imInfo.addFieldName("orderTypeCode");
	imInfo.addFieldName("vipTypeCode");
	imInfo.addFieldName("salesOrderDate");
	imInfo.addFieldName("customerCode");
	imInfo.addFieldName("customerPoNo");
	imInfo.addFieldName("paymentTermCode");
	imInfo.addFieldName("countryCode");
	imInfo.addFieldName("currencyCode");
	imInfo.addFieldName("shopCode");

	// MSALER_ID(負責人員) 發票類型(固定) 稅別(固定) 稅率(固定) 稅金(detail稅金加總) 預計付款日(等於銷售日期)
	// 預計出貨日(等於銷售日期) 原總銷售金額(detail 原銷售金額加總)
	imInfo.addFieldName("superintendentCode");
	imInfo.addFieldName("invoiceTypeCode");
	imInfo.addFieldName("taxType");
	imInfo.addFieldName("taxRate");
	imInfo.addFieldName("taxAmount");
	imInfo.addFieldName("scheduleCollectionDate");
	imInfo.addFieldName("scheduleShipDate");
	imInfo.addFieldName("totalOriginalSalesAmount");

	// 實際總銷售金額(detail實際銷售金額加總) MSEQNO(交易序號) 足額出貨(固定) MTRREM(備註)
	imInfo.addFieldName("totalActualSalesAmount");
	imInfo.addFieldName("transactionSeqNo");
	imInfo.addFieldName("sufficientQuantityDelivery");
	// imInfo.addFieldName("remark1");

	// 狀態(固定) 建立者=負責人員 最後更新人員=負責人員
	imInfo.addFieldName("status");
	imInfo.addFieldName("createdBy");
	imInfo.addFieldName("lastUpdatedBy");
	// 放置匯入的檔名
	imInfo.addFieldName("reserve4");
	// 20160928 maco add field tokens[13] 客戶年齡層(SO_SALES_ORDER_HEAD.SALS_TYPE)
	imInfo.addFieldName("salesType");
	// set field type
	imInfo.setFieldType("s0", "java.lang.String");
	imInfo.setFieldType("orderTypeCode", "java.lang.String");
	imInfo.setFieldType("vipTypeCode", "java.lang.String");
	imInfo.setFieldType("salesOrderDate", "java.util.Date");
	imInfo.setFieldType("customerCode", "java.lang.String");
	imInfo.setFieldType("customerPoNo", "java.lang.String");
	imInfo.setFieldType("paymentTermCode", "java.lang.String");
	imInfo.setFieldType("countryCode", "java.lang.String");
	imInfo.setFieldType("currencyCode", "java.lang.String");
	imInfo.setFieldType("shopCode", "java.lang.String");
	imInfo.setFieldType("superintendentCode", "java.lang.String");
	imInfo.setFieldType("invoiceTypeCode", "java.lang.String");
	imInfo.setFieldType("taxType", "java.lang.String");
	imInfo.setFieldType("pkgUnit", "java.lang.String");
	imInfo.setFieldType("taxRate", "java.lang.Double");
	imInfo.setFieldType("taxAmount", "java.lang.Double");
	imInfo.setFieldType("scheduleCollectionDate", "java.util.Date");
	imInfo.setFieldType("scheduleShipDate", "java.util.Date");
	imInfo.setFieldType("totalOriginalSalesAmount", "java.lang.Double");
	imInfo.setFieldType("totalActualSalesAmount", "java.lang.Double");
	imInfo.setFieldType("transactionSeqNo", "java.lang.String");
	imInfo.setFieldType("sufficientQuantityDelivery", "java.lang.String");
	// imInfo.setFieldType("remark1", "java.lang.String");
	imInfo.setFieldType("status", "java.lang.String");
	imInfo.setFieldType("createdBy", "java.lang.String");
	imInfo.setFieldType("lastUpdatedBy", "java.lang.String");
	imInfo.setFieldType("reserve4", "java.lang.String");
	// 20160928 maco add field tokens[13] 客戶年齡層(SO_SALES_ORDER_HEAD.SALS_TYPE)
	imInfo.setFieldType("salesType", "java.lang.String");
	// set date format
	imInfo.setFieldTypeFormat("java.util.Date", "yyyy/MM/dd");

	// set default value
	// HashMap defaultValue = new HashMap();
	// defaultValue.put("creationDate",new Date());
	// defaultValue.put("lastUpdateDate",new Date());
	// defaultValue.put("createdBy","");
	// defaultValue.put("lastUpdatedBy","");
	// defaultValue.put("fileName",uiProperties.get(ImportDBService.UPLOAD_FILE_NAME)
	// );

	// imInfo.setDefaultValue(defaultValue);

	// add detail
	imInfo.addDetailImportInfos(getSoSalesOrderItem());

	return imInfo;
    }

    /**
     * im item price detail config
     * 
     * @return
     */
    private ImportInfo getSoSalesOrderItem() {
	log.info("POSImportData.getSoSalesOrderItem");
	ImportInfo imInfo = new ImportInfo();

	imInfo.setKeyIndex(0);
	imInfo.setKeyValue("S1");
	imInfo.setSaveKeyField(true);
	imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.SoSalesOrderItem.class.getName());

	// S1 MGDSNO(品號) MPRICE(標準售價) MQTY(數量) 原銷售金額(C5*D5) MOFF(折扣)
	// MSPRICE(實際售價) MSLTOT(實際銷售金額) 預計出貨日(等於銷售日期) 實計出貨日(等於銷售日期) 是否已含稅(固定)
	// 稅別(固定) 稅率(固定) 稅金(實際銷售金額*5%) 狀態(固定) 建立者=負責人員 最後更新人員=負責人員

	imInfo.addFieldName("s1");
	imInfo.addFieldName("itemCode");
	imInfo.addFieldName("originalUnitPrice");
	imInfo.addFieldName("quantity");
	imInfo.addFieldName("originalSalesAmount");
	imInfo.addFieldName("discountRate");
	imInfo.addFieldName("actualUnitPrice");
	imInfo.addFieldName("actualSalesAmount");
	imInfo.addFieldName("scheduleShipDate");
	imInfo.addFieldName("shippedDate");
	imInfo.addFieldName("isTax");
	imInfo.addFieldName("taxType");
	imInfo.addFieldName("taxRate");
	imInfo.addFieldName("taxAmount");
	// imInfo.addFieldName("status");
	imInfo.addFieldName("createdBy");
	imInfo.addFieldName("lastUpdatedBy");
	imInfo.addFieldName("depositCode");


	imInfo.setFieldType("s1", "java.lang.String");
	imInfo.setFieldType("itemCode", "java.lang.String");
	imInfo.setFieldType("originalUnitPrice", "java.lang.Double");
	imInfo.setFieldType("quantity", "java.lang.Double");
	imInfo.setFieldType("originalSalesAmount", "java.lang.Double");
	imInfo.setFieldType("discountRate", "java.lang.Double");
	imInfo.setFieldType("actualUnitPrice", "java.lang.Double");
	imInfo.setFieldType("actualSalesAmount", "java.lang.Double");
	imInfo.setFieldType("scheduleShipDate", "java.util.Date");
	imInfo.setFieldType("shippedDate", "java.util.Date");
	imInfo.setFieldType("isTax", "java.lang.String");
	imInfo.setFieldType("taxType", "java.lang.String");
	imInfo.setFieldType("taxRate", "java.lang.Double");
	imInfo.setFieldType("taxAmount", "java.lang.Double");
	// imInfo.setFieldType("status", "java.lang.String");
	imInfo.setFieldType("createdBy", "java.lang.String");
	imInfo.setFieldType("lastUpdatedBy", "java.lang.String");
	imInfo.setFieldType("depositCode", "java.lang.String");

	
	return imInfo;
    }

    public String updateDB(List entityBeans, ImportInfo info) throws Exception {
	log.info("POSImportData.updateDB");
	StringBuffer reMsg = new StringBuffer();
	HashMap uiProperties = info.getUiProperties();
	// =================記錄system log使用===================
	String processName = (String) uiProperties.get(ImportDBService.PRCCESS_NAME);
	Date executeDate = (Date) uiProperties.get("actualExecuteDate");
	String uuid = (String) uiProperties.get("uuidCode");
	// =================上傳相關檔案路徑=======================
	String headFileName = (String) uiProperties.get(ImportDBService.UPLOAD_HEAD_FILE_NAME);
	String lineFileName = (String) uiProperties.get(ImportDBService.UPLOAD_LINE_FILE_NAME);
	String headFilePath = (String) uiProperties.get(ImportDBService.UPLOAD_HEAD_FILE_PATH);
	String lineFilePath = (String) uiProperties.get(ImportDBService.UPLOAD_LINE_FILE_PATH);
	String combineFilePath = (String) uiProperties.get(ImportDBService.COMBINE_FILE_PATH);
	String baseFilePath = (String) uiProperties.get(ImportDBService.BASE_FILE_PATH);
	String successFilePath = (String) uiProperties.get(ImportDBService.SUCCESS_FILE_PATH);
	File headFile = new File(headFilePath);
	File lineFile = new File(lineFilePath);
	File combineFile = new File(combineFilePath);
	File baseFile = new File(baseFilePath);
	File successFile = new File(successFilePath);
	// =====================================================
	SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils.getApplicationContext().getBean("siSystemLogService");
	String opUser = null;
	String errorMsg = null;
	try {
	    User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
	    opUser = user.getEmployeeCode();
	    String fileSalesDate = (String) uiProperties.get("fileSalesDate");
	    String fileShopCode = (String) uiProperties.get("fileShopCode");
	    Date actualSalesDate = null;
	    String actualShopCode = null;
	    String brandCode = null;
	    String msNo = (String) uiProperties.get("msNo");
	    if (user != null) {
		opUser = user.getEmployeeCode();
	    }
	    if (!StringUtils.hasText(msNo)) {
		throw new ValidationErrorException("原始單號為空值！");
	    }
	    // 解析銷貨日期
	    if (StringUtils.hasText(fileSalesDate)) {
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		    actualSalesDate = dateFormat.parse(fileSalesDate);
		} catch (Exception ex) {
		    throw new ValidationErrorException("解析銷貨日期失敗！");
		}
	    } else {
		throw new ValidationErrorException("銷貨日期為空值！");
	    }
	    // 取得對應的專櫃
	    if (StringUtils.hasText(fileShopCode)) {
		try {
		    BuShop newBuShop = OldSysMapNewSys.getNewShop(fileShopCode);
		    if (newBuShop != null) {
			actualShopCode = newBuShop.getShopCode();
			brandCode = newBuShop.getBrandCode();
			if (!StringUtils.hasText(brandCode)) {
			    throw new ValidationErrorException("新專櫃代號： " + actualShopCode + "所屬的品牌代號為空值！");
			}
		    } else {
			throw new ValidationErrorException("舊專櫃代號： " + fileShopCode + "查無對應的新專櫃代號！");
		    }
		} catch (Exception ex) {
		    log.error("查詢舊專櫃代號： " + fileShopCode + "對應的新專櫃發生錯誤，原因：" + ex.toString());
		    throw new ValidationErrorException("查詢舊專櫃代號： " + fileShopCode + "對應的新專櫃發生錯誤，原因：" + ex.getMessage());
		}
	    } else {
		throw new ValidationErrorException("專櫃代號為空值！");
	    }

	    // 執行檢核
	    HashMap parameterMap = new HashMap();
	    parameterMap.put("brandCode", brandCode);
	    parameterMap.put("actualShopCode", actualShopCode);
	    parameterMap.put("actualSalesDate", actualSalesDate);
	    parameterMap.put("msNo", msNo);
	    parameterMap.put("orderTypeCode", "SOP");
	    parameterMap.put("identification", "POS");
	    parameterMap.put("opUser", opUser);
	    doValidate(parameterMap);

	    for (int index = 0; index < entityBeans.size(); index++) {
		setData((SoSalesOrderHead) entityBeans.get(index));
	    }
	    SoSalesOrderService soSalesOrderService = (SoSalesOrderService) SpringUtils
		    .getApplicationContext().getBean("soSalesOrderService");
	    soSalesOrderService.saveNoValidate(entityBeans, parameterMap);
	    reMsg.append(headFileName + "的POS業績匯入成功！");

	    try {
		FileUtils.copyFileToDirectory(headFile, successFile);
		FileUtils.copyFileToDirectory(lineFile, successFile);
		headFile.delete();
		lineFile.delete();
	    } catch (Exception ex1) {
		log.error("刪除POS業績上傳原始檔案發生錯誤，原因：" + ex1.toString());
	    }
	} catch (Exception ex) {
	    errorMsg = "POS業績匯入" + headFileName + "、" + lineFileName + "失敗，原因：" + ex.toString();
	    log.error(errorMsg);
	    reMsg.append("POS業績檔匯入失敗！原因：" + ex.getMessage() + "<br>");
	    siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, opUser);
	} finally {
	    try {
		combineFile.delete();
		baseFile.delete();
	    } catch (Exception ex2) {
		log.error("刪除POS業績上傳組合檔案發生錯誤，原因：" + ex2.toString());
	    }
	}
	return reMsg.toString();
    }

    private void setData(SoSalesOrderHead head) throws Exception {
	log.info("POSImportData.setData");
	// BuShopDAO buShopDAO = (BuShopDAO)
	// SpringUtils.getApplicationContext().getBean("buShopDAO");
	BuOrderTypeDAO buOrderTypeDAO = (BuOrderTypeDAO) SpringUtils.getApplicationContext().getBean("buOrderTypeDAO");
	ImItemPriceDAO itemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");
	ImItemComposeDAO itemComposeDAO = (ImItemComposeDAO) SpringUtils.getApplicationContext().getBean("imItemComposeDAO");
	SoSalesOrderService salesOrderService = (SoSalesOrderService) SpringUtils.getApplicationContext().getBean("soSalesOrderService");
	// BuOrderTypeService buOrderTypeService = (BuOrderTypeService)
	// SpringUtils.getApplicationContext().getBean("buOrderTypeService");

	String oldShopCode = head.getShopCode();
	BuShop newBuShop = OldSysMapNewSys.getNewShop(oldShopCode);
	/*
	 * log.error("POSImportData.setData oldShopCode=" + oldShopCode); if
	 * ("RH81_1".equalsIgnoreCase(oldShopCode)) oldShopCode = "RH81"; List
	 * buShops = buShopDAO.findByProperty("BuShop", "reserve1",
	 * oldShopCode); log.error("POSImportData.setData buShops.size()=" +
	 * buShops.size()); for (int index = 0; index < buShops.size(); index++) {
	 * buShop = (BuShop) buShops.get(index); }
	 */
	// BuShop buShop = buShopDAO.findById();
	if (null != newBuShop) {
	    String brandCode = newBuShop.getBrandCode();
	    String defaultWarehouseCode = newBuShop.getSalesWarehouseCode();
	    String orderTypeCode = head.getOrderTypeCode();
	    Date salesOrderDate = head.getSalesOrderDate();// 銷售日期
	    String dateType = "銷貨日期";
	    // priceType
	    BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
	    buOrderTypeId.setBrandCode(brandCode);
	    buOrderTypeId.setOrderTypeCode(orderTypeCode);
	    BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
	    String priceType = buOrderType.getPriceType();
	    // 20090109 shan
	    head.setShopCode(newBuShop.getShopCode());
	    head.setDefaultWarehouseCode(defaultWarehouseCode);
	    head.setBrandCode(brandCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdateDate(new Date());
	    head.setReserve5("POS");
	    head.setSchedule("99");
	    // 檢核是否關帳
	    ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType,salesOrderDate,head.getSchedule());
	    // set detail
	    Double totalTaxAmount = 0D;
	    List<SoSalesOrderItem> actualSalesOrderItems = new ArrayList(0);
	    for (SoSalesOrderItem item : head.getSoSalesOrderItems()) {
		log.info("POSImportData.setData old itemCode="
			+ item.getItemCode());
		ImItem newItem = OldSysMapNewSys.getNewItemCode(brandCode, item.getItemCode());
		if (null != newItem) {
		    item.setItemCode(newItem.getItemCode());
		} else {
		    throw new Exception("舊ITEM CODE '" + item.getItemCode() + "' 查無對應新ITEM CODE");
		}
		/*
		 * // 新舊 ITEM CODE 對照 List<ImItem> newItem =
		 * imItemDAO.findOldItemList(brandCode, item.getItemCode()); if
		 * (null != newItem && newItem.size() > 0) {
		 * log.info("POSImportData.setData find findOldItemList
		 * itemCode=" + item.getItemCode()); if (null != newItem.get(0)) {
		 * item.setItemCode(newItem.get(0).getItemCode()); } }
		 */
		Double actualUnitPrice = item.getActualUnitPrice();
		Double quantity = item.getQuantity();
		if (actualUnitPrice == null) {
		    throw new Exception("品號：" + item.getItemCode() + "的實際售價為空值！");
		}
		if (quantity == null) {
		    throw new Exception("品號：" + item.getItemCode() + "的數量為空值！");
		}

		// 取出新itemCode組合性商品及服務性商品的值
		//String isComposeItem = newItem.getIsComposeItem();
		String isServiceItem = newItem.getIsServiceItem();
		// 組合性商品拆解出實際銷售商品
//		if ("Y".equals(isComposeItem)) {
//		    List itemComposes = itemComposeDAO.findByProperty("imItem", newItem);// 實際銷售商品集合
//		    if (itemComposes != null && itemComposes.size() > 0) {
//			List composeItemInfo = new ArrayList();
//			Double composeItemPriceAmount = 0D; // 組合商品售價加總(原售價)
//			// 取得組合商品於銷售日期的售價
//			for (int i = 0; i < itemComposes.size(); i++) {
//			    ImItemCompose itemCompose = (ImItemCompose) itemComposes.get(i);
//			    Double itemComposeQuantity = null; // 組合商品數量
//			    if (itemCompose.getQuantity() == null) {
//				throw new Exception("組合商品：" + itemCompose.getComposeItemCode() + "的數量為空值！");
//			    } else {
//				itemComposeQuantity = itemCompose.getQuantity().doubleValue();
//			    }
//			    List salesItemInfo = itemPriceDAO.getItemPriceByBeginDate(brandCode, itemCompose.getComposeItemCode(), "1", salesOrderDate, "Y");
//			    if (salesItemInfo != null
//				    && salesItemInfo.size() > 0) {
//				Object[] objArray = (Object[]) salesItemInfo.get(0);
//				Double salesItemPrice = null;
//				if (objArray[1] == null) {
//				    throw new Exception("組合商品：" + itemCompose.getComposeItemCode() + "的售價為空值！");
//				} else {
//				    salesItemPrice = ((BigDecimal) objArray[1]).doubleValue();
//				    SoSalesOrderItem actualSaveSalesOrderItem = new SoSalesOrderItem();
//				    // BeanUtils.copyProperties(item,
//				    // actualSaveSalesOrderItem);
//				    actualSaveSalesOrderItem.setItemCode(itemCompose.getComposeItemCode());
//				    actualSaveSalesOrderItem.setWarehouseCode(defaultWarehouseCode);
//				    actualSaveSalesOrderItem.setOriginalUnitPrice(salesItemPrice);
//				    actualSaveSalesOrderItem.setQuantity(itemComposeQuantity);
//				    actualSaveSalesOrderItem.setIsComposeItem((String) objArray[2]);
//				    actualSaveSalesOrderItem.setIsServiceItem((String) objArray[3]);
//				    composeItemInfo.add(actualSaveSalesOrderItem);
//				    composeItemPriceAmount += salesItemPrice * itemComposeQuantity;
//				}
//			    } else {
//				throw new Exception("查無組合商品：" + itemCompose.getComposeItemCode() + "的資料！");
//			    }
//			}
//			//==========================檢核組合性商品金額加總為零時無法試算====================
//			if(composeItemPriceAmount == 0D){
//			    throw new Exception("品號：" + item.getItemCode() + "的組合性商品金額加總為零！");
//			}			
//			// 組出實際銷售商品的bean
//			for (int j = 0; j < composeItemInfo.size(); j++) {
//			    SoSalesOrderItem actualSaveSalesOrderItem = (SoSalesOrderItem) composeItemInfo.get(j);
//			    Double saveItemOriginalUnitPrice = actualSaveSalesOrderItem.getOriginalUnitPrice();
//			    Double saveItemQuantity = actualSaveSalesOrderItem.getQuantity();
//			    Double saveItemActualUnitPrice = (actualUnitPrice * saveItemOriginalUnitPrice * saveItemQuantity) / composeItemPriceAmount;
//			    saveItemActualUnitPrice = CommonUtils.round(saveItemActualUnitPrice, 0);
//			    //actualSaveSalesOrderItem.setDeductionAmount(saveItemOriginalUnitPrice - saveItemActualUnitPrice); // 折讓
//			    actualSaveSalesOrderItem.setDiscountRate((saveItemOriginalUnitPrice != null && saveItemOriginalUnitPrice != 0D) 
//				    ? CommonUtils.round((saveItemActualUnitPrice / saveItemOriginalUnitPrice) * 100D, 2) : 100D);	    
//			    actualSaveSalesOrderItem.setActualUnitPrice(saveItemActualUnitPrice);
//			    saveItemQuantity = saveItemQuantity * quantity;// 商品數量
//			    // *
//	                    // 組合性商品數量
//			    actualSaveSalesOrderItem.setQuantity(saveItemQuantity);
//			    actualSaveSalesOrderItem.setOriginalSalesAmount(saveItemOriginalUnitPrice * saveItemQuantity);
//			    actualSaveSalesOrderItem.setActualSalesAmount(saveItemActualUnitPrice * saveItemQuantity);
//			    actualSaveSalesOrderItem.setScheduleShipDate(item.getScheduleShipDate());
//			    actualSaveSalesOrderItem.setIsTax("P");
//			    actualSaveSalesOrderItem.setTaxType(item.getTaxType());
//			    actualSaveSalesOrderItem.setTaxRate(item.getTaxRate());
//			    actualSaveSalesOrderItem.setDepositCode(item.getDepositCode());
//			    actualSaveSalesOrderItem.setIsUseDeposit(item.getIsUseDeposit());
//			    actualSaveSalesOrderItem.setWatchSerialNo(item.getWatchSerialNo());
//			    actualSaveSalesOrderItem.setTaxAmount(salesOrderService.calculateTaxAmount(item.getTaxType(), item.getTaxRate(), actualSaveSalesOrderItem.getActualSalesAmount()));
//			    actualSaveSalesOrderItem.setStatus(OrderStatus.SIGNING);
//			    actualSaveSalesOrderItem.setCreatedBy(item.getCreatedBy());
//			    actualSaveSalesOrderItem.setCreationDate(new Date());
//			    actualSaveSalesOrderItem.setLastUpdatedBy(item.getLastUpdatedBy());
//			    actualSaveSalesOrderItem.setLastUpdateDate(new Date());
//			    actualSalesOrderItems.add(actualSaveSalesOrderItem);
//			    totalTaxAmount = totalTaxAmount + actualSaveSalesOrderItem.getTaxAmount();
//			}
//		    } else {
//			throw new Exception("查無品號：" + newItem.getItemCode() + "相對應的組合商品資料！");
//		    }
//		} else {
		    // 組合性商品及服務性商品設值
		    //item.setIsComposeItem(isComposeItem);
		    item.setIsServiceItem(isServiceItem);
		    // 重新計算稅額
		    item.setTaxAmount(salesOrderService.calculateTaxAmount(item.getTaxType(), item.getTaxRate(), item.getActualSalesAmount()));
		    // PROMOTION 對照
		    // setPromotionData(head, item, priceType);
		    item.setCreationDate(new Date());
		    item.setLastUpdateDate(new Date());
		    item.setWarehouseCode(defaultWarehouseCode);
		    item.setIsTax("P"); //增加商品的稅別(完稅)
		    actualSalesOrderItems.add(item);
		    totalTaxAmount = totalTaxAmount + item.getTaxAmount();
		//}
	    }
	    head.setSoSalesOrderItems(actualSalesOrderItems);
	    Double actualTaxAmount = salesOrderService.calculateTaxAmount(head.getTaxType(), head.getTaxRate(), head.getTotalActualSalesAmount());
	    head.setTaxAmount(actualTaxAmount);
	    head.setDiscountRate(100D);
	    head.setExportExchangeRate(1D); //增加幣別的匯率
	    // 將差額補到最後一筆明細的稅額
	    Double balanceAmt = actualTaxAmount - totalTaxAmount;
	    List<SoSalesOrderItem> salesOrderItemList = head.getSoSalesOrderItems();
	    if (salesOrderItemList != null && salesOrderItemList.size() > 0) {
		SoSalesOrderItem salesOrderItem = (SoSalesOrderItem) salesOrderItemList.get(salesOrderItemList.size() - 1);
		Double lastItemTaxAmt = salesOrderItem.getTaxAmount();
		if (lastItemTaxAmt == null) {
		    lastItemTaxAmt = 0D;
		}
		salesOrderItem.setTaxAmount(lastItemTaxAmt + balanceAmt);
	    }
	} else {
	    throw new Exception("舊SHOP CODE '" + oldShopCode + "' 查無對應新SHOP CODE");
	}
    }

    /**
     * 設定Promotion的資料,VIP的資料
     * 
     * @param item
     */
    private void setPromotionData(SoSalesOrderHead head, SoSalesOrderItem item,
	    String priceType) {
	log.info("POSImportData.setPromotionData");
	String brandCode = head.getBrandCode();
	String customerCode = head.getCustomerCode();// customer or employee
	String vipTypeCode = head.getVipTypeCode(); // 1.vip type 2.birthday
	// String employeeCode = null;
	String promotionCode = null;
	String customerTypeCode = null;
	String itemCode = item.getItemCode();
	String shopCode = head.getShopCode();
	Date orderDate = head.getSalesOrderDate();
	BuCommonPhraseLine buCommonPhraseLine = null;
	boolean isVip = false;
	ImPromotionDAO imPromotionDAO = (ImPromotionDAO) SpringUtils
		.getApplicationContext().getBean("imPromotionDAO");
	BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
		.getApplicationContext().getBean("buCommonPhraseService");
	BuEmployeeDAO buEmployeeDAO = (BuEmployeeDAO) SpringUtils
		.getApplicationContext().getBean("buEmployeeDAO");
	BuCustomerDAO buCustomerDAO = (BuCustomerDAO) SpringUtils
		.getApplicationContext().getBean("buCustomerDAO");

	List promotions = imPromotionDAO.findPromotionInfo(orderDate,
		brandCode, shopCode, itemCode, priceType);
	if ((null != promotions) && (promotions.size() > 0)) {
	    log
		    .info("POSImportData.setPromotionData size="
			    + promotions.size());
	    Object hp[] = (Object[]) promotions.get(0);
	    promotionCode = (String) hp[0];
	} else if (StringUtils.hasText(customerCode)) {
	    // BSCMP=舊采盟會員 / BSEMP=員工 / BSGNR=一般(X) / BSVIP=Borsalini VIP
	    // 0=采盟,1=VIP,2=Birthday,EMPLOYEE
	    if ("2".equalsIgnoreCase(vipTypeCode)) {
		// 當日壽星的 PROMOTION
		buCommonPhraseLine = buCommonPhraseService
			.getBuCommonPhraseLine("BRDType", brandCode
				+ SystemConfig.BIRTHDAY_PROMOTION_KEY_WORLD);
		if (buCommonPhraseLine != null)
		    promotionCode = buCommonPhraseLine.getAttribute2();

		// 設定VIP
	    } /*
		 * else if ("1".equalsIgnoreCase(vipTypeCode)) { //BuEmployeeDAO
		 * buEmployeeDAO = (BuEmployeeDAO)
		 * SpringUtils.getApplicationContext().getBean("buEmployeeDAO");
		 * 
		 * BuCustomerId bcId = new BuCustomerId();
		 * bcId.setBrandCode(brandCode);
		 * bcId.setCustomerCode(customerCode); BuCustomer buCustomer =
		 * (BuCustomer) buCustomerDAO.findByPrimaryKey(BuCustomer.class,
		 * bcId); String customerVIPCode = buCustomer.getVipTypeCode();
		 * customerTypeCode = buCustomer.getCustomerTypeCode();
		 * buCommonPhraseLine =
		 * buCommonPhraseService.getBuCommonPhraseLine("VIPType",
		 * customerVIPCode); promotionCode =
		 * buCommonPhraseLine.getAttribute2(); }
		 */else {
		BuEmployee buEmployee = buEmployeeDAO.findById(customerCode);
		if (null != buEmployee) {
		    BuCustomerId bcId = new BuCustomerId();
		    bcId.setBrandCode(brandCode);
		    bcId.setCustomerCode(customerCode);
		    BuCustomer buCustomer = (BuCustomer) buCustomerDAO
			    .findByPrimaryKey(BuCustomer.class, bcId);
		    if (null != buCustomer) {
			String customerVIPCode = buCustomer.getVipTypeCode();
			customerTypeCode = buCustomer.getCustomerTypeCode();
			buCommonPhraseLine = buCommonPhraseService
				.getBuCommonPhraseLine("VIPType",
					customerVIPCode);
			if (buCommonPhraseLine != null)
			    promotionCode = buCommonPhraseLine.getAttribute2();
		    }
		}

	    }
	    isVip = true;
	}

	if (null != promotionCode) {
	    ImPromotion imPromotion = imPromotionDAO
		    .findByBrandCodeAndPromotionCode(brandCode, promotionCode);
	    if (null != imPromotion) {
		if (isVip) {
		    item.setVipPromotionCode(imPromotion.getPromotionCode());
		    item.setVipDiscount(new Double(imPromotion.getDiscount()));
		} else {
		    List<ImPromotionItem> pitems = imPromotion
			    .getImPromotionItems();
		    String discountType = null;
		    for (ImPromotionItem pitem : pitems) {
			if (item.getItemCode().equals(pitem.getItemCode())) {
			    discountType = pitem.getDiscountType();
			}
		    }
		    item.setDiscountType(discountType);
		}
	    }
	}

	// item.setDiscountRate(100 - item.getDiscountRate()); // discount rate
	// 100
	// percent
	// Double actualUnitPrice = item.getActualUnitPrice(); // promotion
	// 之後的價格
	// Double beforeDiscountPrice = actualUnitPrice /
	// item.getDiscountRate(); // 手動折扣前的金額
    }

    private void doValidate(HashMap parameterMap) throws Exception {

	String dateType = "銷貨日期";
	String brandCode = (String) parameterMap.get("brandCode");
	String actualShopCode = (String) parameterMap.get("actualShopCode");
	Date actualSalesDate = (Date) parameterMap.get("actualSalesDate");
	String orderTypeCode = (String) parameterMap.get("orderTypeCode");
	// 檢核是否關帳
	//ValidateUtil.isAfterClose(brandCode, orderTypeCode, dateType, actualSalesDate);
	// 檢核是否過帳
	SoPostingTallyService soPostingTallyService = (SoPostingTallyService) SpringUtils
		.getApplicationContext().getBean("soPostingTallyService");
	SoPostingTally soPostingTally = soPostingTallyService
		.findSoPostingTallyById(actualShopCode, actualSalesDate);
	if (soPostingTally != null
		&& "Y".equalsIgnoreCase(soPostingTally.getIsPosting())) {
	    throw new ValidationErrorException("專櫃代號：" + actualShopCode
		    + "、交易日期：" + DateUtils.format(actualSalesDate)
		    + "已完成過帳，無法執行匯入！");
	}
    }
}