package tw.com.tm.erp.exportdb;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.dao.IslandExportDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.SiPosLogService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.SiPosLogUtil;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.TxtUtil;
import tw.com.tm.erp.utils.dbf.DBFUtils;
import tw.com.tm.erp.utils.dbf.JDBFException;
import tw.com.tm.erp.utils.dbf.JDBField;

/**
 * POS 資料匯出
 * 
 * @author T02049 生日是 國曆 yy/mm/dd
 */

public class POSIslandsExportData {
    private static final Log log = LogFactory
	    .getLog(POSIslandsExportData.class);
    private static SimpleDateFormat dateFromat = new SimpleDateFormat(
	    "yy/MM/dd");
    // private static SimpleDateFormat timeFromat = new
    // SimpleDateFormat("hh/mm/ss");
    private static SimpleDateFormat timeFromat = new SimpleDateFormat("hh:mm");
    private static Properties iniProperties = null;
    private static String classPath = null;
    private static Map<String, JDBField[]> templateFields = new HashMap();
    private static final String COMMON_SHOP_CODE = "All";
    private static final String COMMON_SHOP_MACHINE_CODE = "All";

    /**
     * export pos data
     * 
     * @param functionSwitch
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void doPOSExportData(int functionSwitch, String brandCode,
	    Date dataDate, Date dataDateEnd, String opUser,
	    String customsWarehouseCode) {
	log.info("POSExportData.doPOSExportData");
	String actualDataDate = null;
	String actualDataDateEnd = null;
	if (dataDate != null) {
	    actualDataDate = DateUtils.format(dataDate,
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	}
	if (dataDateEnd != null) {
	    actualDataDateEnd = DateUtils.format(dataDateEnd,
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	}
	switch (functionSwitch) {
	case 0:
	    exportCustomerData(brandCode, actualDataDate, actualDataDateEnd,
		    opUser);
	    break;
	case 1:
	    exportEcrsalesData(brandCode, actualDataDate, actualDataDateEnd,
		    opUser);
	    break;
	// case 2:
	// exportGdsqtyData(brandCode, actualDataDate, actualDataDateEnd,
	// opUser);
	// break;
	case 3:
	    exportGoodslstData(brandCode, dataDate, dataDateEnd, opUser,
		    customsWarehouseCode);
	    break;
	case 4:
	    exportProm01Data(brandCode, actualDataDate, actualDataDateEnd,
		    opUser);
	    break;
	// case 5:
	// exportMovementData(brandCode, dataDate, dataDateEnd, opUser);
	// break;
	case 6:
	    exportEanCodeData(brandCode, actualDataDate, actualDataDateEnd,
		    opUser, customsWarehouseCode);
	    break;
	case 7:
	    exportItemDiscountData(brandCode, actualDataDate,
		    actualDataDateEnd, opUser);
	    break;
	    
	// new exportOnHand by cmDeclarationOnHandView 2020.06.09
	case 98:
	    exportOnHandNew(brandCode, opUser, customsWarehouseCode, dataDate,
		    dataDateEnd);
	    break;
	case 99:
	    exportOnHand(brandCode, opUser, customsWarehouseCode, dataDate,
		    dataDateEnd);
	    break;
	}
    }

    /**
     * 調撥單資料匯出
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportMovementData(String brandCode, Date dataDate,
	    Date dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportMovementData");
	    /*
	     * Date actualDeliveryDate =
	     * DateUtils.addDays(DateUtils.getShortDate(new Date()), -1); Date
	     * actualDeliveryDateEnd = actualDeliveryDate; if(dataDate != null){
	     * actualDeliveryDate = dataDate; } if(dataDateEnd != null){
	     * actualDeliveryDateEnd = dataDateEnd; }
	     */
	    processName = brandCode + "_"
		    + SiPosLogService.PROCESS_NAME_MOVE_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode + "調撥資料下傳....",
		    currentDate, uuid, opUser);
	    // SiPosLogUtil.createPosLog(COMMON_SHOP_CODE,
	    // actualDeliveryDateEnd, processName, null, null,
	    // COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
	    setIniProperties();
	    String movetopFileName = iniProperties.getProperty("movetopTemp");
	    String movedetFileName = iniProperties.getProperty("movedetTemp");
	    String movetopTemp = classPath + "/" + movetopFileName;
	    String movedetTemp = classPath + "/" + movedetFileName;
	    String movetopOut = iniProperties.getProperty(brandCode
		    + ".movetopOut");
	    String movetopOuts[] = movetopOut.split(",");
	    String movedetOut = iniProperties.getProperty(brandCode
		    + ".movedetOut");
	    String movedetOuts[] = movedetOut.split(",");
	    Long[] transportIdArray = createMovementDBF(brandCode, movetopTemp,
		    movedetTemp, movetopOuts, movedetOuts, dataDate,
		    dataDateEnd);
	    // ========================將下傳的調撥資料標記為Y=====================
	    if (transportIdArray != null && transportIdArray.length > 0) {
		ImMovementService movementService = (ImMovementService) SpringUtils
			.getApplicationContext().getBean("imMovementService");
		movementService.updateTransportDataById(transportIdArray, "Y");
	    }
	    // SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
	    // actualDeliveryDateEnd, processName, COMMON_SHOP_MACHINE_CODE,
	    // movetopFileName, movedetFileName, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.MOVE_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.MOVE_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * 會員主檔
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportCustomerData(String brandCode, String dataDate,
	    String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportCustomerData");
	    String actualDataDate = null;
	    if (!"T2".equals(brandCode)) {
		actualDataDate = DateUtils.format(DateUtils.addDays(
			currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    } else {
		actualDataDate = DateUtils.format(currentDate,
			DateUtils.C_DATA_PATTON_YYYYMMDD);
	    }
	    String actualDataDateEnd = actualDataDate;
	    if (StringUtils.hasText(dataDate)) {
		actualDataDate = dataDate;
	    }
	    if (StringUtils.hasText(dataDateEnd)) {
		actualDataDateEnd = dataDateEnd;
	    }
	    Date transactionDate = DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_CUSTOMER_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands客戶資料下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = null;
	    // String customerTemp = null;
	    String customerOut = iniProperties.getProperty(brandCode
		    + "Islands.customerOut");
	    String[] customerOuts = customerOut.split(",");
	    fileName = iniProperties.getProperty("customerFile");
	    String[] actualOutPaths = getActualOutPaths(brandCode,
		    actualDataDate, fileName, customerOuts);
	    createT2Customer(brandCode, actualOutPaths, actualDataDate,
		    actualDataDateEnd);
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    transactionDate, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.CUSTOMER_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.CUSTOMER_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * 銷售員
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportEcrsalesData(String brandCode, String dataDate,
	    String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportEcrsalesData");
	    String actualDataDate = null;
	    if (!"T2".equals(brandCode)) {
		actualDataDate = DateUtils.format(DateUtils.addDays(
			currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    } else {
		actualDataDate = DateUtils.format(currentDate,
			DateUtils.C_DATA_PATTON_YYYYMMDD);
	    }
	    String actualDataDateEnd = actualDataDate;
	    if (StringUtils.hasText(dataDate)) {
		actualDataDate = dataDate;
	    }
	    if (StringUtils.hasText(dataDateEnd)) {
		actualDataDateEnd = dataDateEnd;
	    }
	    Date transactionDate = DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_SALES_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands專櫃人員資料下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = null;
	    // String ecrsalesTemp = null;
	    String ecrsales = iniProperties.getProperty(brandCode
		    + "Islands.ecrsalesOut");
	    String ecrsaless[] = ecrsales.split(",");	    
	    fileName = iniProperties.getProperty("ecrsalesFile");
	    String[] actualOutPaths = getActualOutPaths(brandCode,
		    actualDataDate, fileName, ecrsaless);	    
	    createT2Sales(brandCode, actualOutPaths, actualDataDate,
		    actualDataDateEnd);	   
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    transactionDate, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.SALES_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.SALES_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * 庫存
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportGdsqtyData(String brandCode, String dataDate,
	    String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportGdsqtyData");
	    String actualDataDate = null;
	    String actualDataDateEnd = DateUtils.format(currentDate,
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	    /*
	     * String actualDataDate =
	     * DateUtils.format(DateUtils.addDays(currentDate, -1),
	     * DateUtils.C_DATA_PATTON_YYYYMMDD);
	     * if(StringUtils.hasText(dataDate)){ actualDataDate = dataDate; }
	     * if(StringUtils.hasText(dataDateEnd)){ actualDataDateEnd =
	     * dataDateEnd; }
	     */
	    Date transactionDate = DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "_"
		    + SiPosLogService.PROCESS_NAME_GOODSQTY_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO,
		    "開始執行" + brandCode + "商品庫存資料下傳....", currentDate, uuid,
		    opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = iniProperties.getProperty("gdsqtyTemp");
	    String gdsqtyTemp = classPath + "/" + fileName;
	    String gdsqtyOut = iniProperties.getProperty(brandCode
		    + ".gdsqtyOut");
	    String gdsqtyOuts[] = gdsqtyOut.split(",");
	    createGdsqtyDBF(brandCode, gdsqtyTemp, gdsqtyOuts, actualDataDate,
		    actualDataDateEnd);
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    transactionDate, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.GOODSQTY_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.GOODSQTY_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * 商品
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportGoodslstData(String brandCode, Date dataDate,
	    Date dataDateEnd, String opUser, String customsWarehouseCode) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportGoodslstData");
	    Date actualDataDate = null;
	    if (!"T2".equals(brandCode)) {
		actualDataDate = DateUtils.addDays(DateUtils
			.getShortDate(currentDate), -1);
	    } else {
		actualDataDate = DateUtils.getShortDate(currentDate);
	    }
	    Date actualDataDateEnd = actualDataDate;
	    if (dataDateEnd != null) {
		actualDataDateEnd = dataDateEnd;
	    }
	    if (dataDate != null) {
		actualDataDate = dataDate;
	    }
	    // 如果都沒輸入時間，起迄就是今天
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_GOODSLIST_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands商品主檔下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, actualDataDateEnd,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = null;
	    // String goodslstTemp = null;
	    String goodslstOut = iniProperties.getProperty(brandCode
		    + "Islands.goodslstOut");
	    log.info("goodslstOut = " + goodslstOut);
	    String goodslstOuts[] = goodslstOut.split(",");
	    fileName = iniProperties.getProperty("goodslstFile");
	    String[] actualOutPaths = getActualOutPaths(brandCode, DateUtils
		    .format(actualDataDate, DateUtils.C_DATA_PATTON_YYYYMMDD),
		    fileName, goodslstOuts);
	    createT2Goodslst(brandCode, actualOutPaths, actualDataDate,
		    actualDataDateEnd, customsWarehouseCode);
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    actualDataDateEnd, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.GOODSLIST_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.GOODSLIST_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * PROMOTION
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportProm01Data(String brandCode, String dataDate,
	    String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
		log.info("POSExportData.exportProm01Data");
	    String actualDataDateEnd = null;
	    String finishedPromotionDate = null;
	    String isAutoSchedule = "Y";
	    if(!"T2".equals(brandCode))
		actualDataDateEnd = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    else
		actualDataDateEnd = DateUtils.format(DateUtils.addDays(currentDate, 1), DateUtils.C_DATA_PATTON_YYYYMMDD);

	    if(StringUtils.hasText(dataDateEnd)){
		isAutoSchedule = "N";
		actualDataDateEnd = dataDateEnd;
		finishedPromotionDate = dataDateEnd;
	    }
	    else
		finishedPromotionDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    
	    Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "Islands_"+ SiPosLogService.PROCESS_NAME_PROMOTION_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,MessageStatus.LOG_INFO, "開始執行" + brandCode + "Islands促銷資料下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,brandCode);
	    setIniProperties();
	    String fileName = null;
	    String prom01Temp = null;
	    String prom01Out = iniProperties.getProperty(brandCode+ "Islands.prom01Out");
	    String prom01Outs[] = prom01Out.split(",");
	    fileName = iniProperties.getProperty("prom01File");
	    String currentDateFormat = DateUtils.format(currentDate,DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String[] actualOutPaths = getActualOutPaths(brandCode,currentDateFormat, fileName, prom01Outs);
	    
	    // 取得後台配置檔
            Map configMap = getConfig(brandCode,"Islands.prom01Config");
	    createT2Promotion(brandCode, actualOutPaths, dataDate, actualDataDateEnd, finishedPromotionDate, currentDateFormat, isAutoSchedule,configMap);
	    
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,transactionDate, processName, COMMON_SHOP_MACHINE_CODE,fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,MessageStatus.LOG_INFO, MessageStatus.PROMOTION_DL_SUCCESS,currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.PROMOTION_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    public TreeMap getPOSExportLabel() throws Exception {
	log.info("POSExportData.getPOSExportLabel");
	setIniProperties();
	TreeMap re = new TreeMap();
	String functionSwitch = iniProperties.getProperty("functionSwitch");
	String functionLabel = iniProperties.getProperty("functionLabel");
	log.info(functionLabel);
	String functionSwitchs[] = StringTools.StringToken(functionSwitch, ",");
	String functionLabels[] = StringTools.StringToken(functionLabel, ",");
	for (int index = 0; index < functionSwitchs.length; index++) {
	    log.info(functionLabels[index]);
	    re.put(functionSwitchs[index], functionLabels[index]);
	}
	return re;
    }

    private static void setIniProperties() throws IOException {
	log.info("POSExportData.setIniProperties");
	// if (null == iniProperties) {
	URL url = tw.com.tm.erp.exportdb.POSIslandsExportData.class
		.getResource("POSExportData.properties");
	System.out.print("url==="+url);
	iniProperties = new Properties();
	iniProperties.load(new FileInputStream(url.getFile()));
	classPath = url.getFile().substring(0, url.getFile().lastIndexOf("/"));
	log.info("==POSExportData.setIniProperties===classPath =" + classPath);
	// }
    }

    public Long[] createMovementDBF(String brandCode, String templateTopFile,
	    String templateDetFile, String targetTopFiles[],
	    String targetDetFiles[], Date actualDeliveryDate,
	    Date actualDeliveryDateEnd) throws IOException, JDBFException {
	log.info("POSExportData.createMovementDBF");
	/*
	 * Date actualDeliveryDate =
	 * DateUtils.addDays(DateUtils.getShortDate(new Date()), -1); Date
	 * actualDeliveryDateEnd = actualDeliveryDate; if(dataDate != null){
	 * actualDeliveryDate = dataDate; } if(dataDateEnd != null){
	 * actualDeliveryDateEnd = dataDateEnd; }
	 */
	JDBField[] topFields = templateFields.get(templateTopFile);
	JDBField[] detFields = templateFields.get(templateDetFile);
	if (topFields == null) {
	    topFields = DBFUtils.getJDBField(templateTopFile);
	    templateFields.put(templateTopFile, topFields);
	    detFields = DBFUtils.getJDBField(templateDetFile);
	    templateFields.put(templateDetFile, detFields);
	}
	List<Object[]> topRecords = new ArrayList();
	List<Object[]> detRecords = new ArrayList();
	HashMap conditionMap = new HashMap();
	conditionMap.put("brandCode", brandCode);
	// conditionMap.put("orderTypeCode", "IMV");
	conditionMap.put("origiOrderTypeArray", new String[] { "TR" });
	conditionMap.put("deliveryDate", actualDeliveryDate);
	conditionMap.put("deliveryDateEnd", actualDeliveryDateEnd);
	conditionMap.put("status", OrderStatus.WAIT_IN);
	conditionMap.put("transportStatus", "Y");
	ImMovementHeadDAO imMovementHeadDAO = (ImMovementHeadDAO) SpringUtils
		.getApplicationContext().getBean("imMovementHeadDAO");
	List imMovementHeads = null;
	if (actualDeliveryDate != null && actualDeliveryDateEnd != null)
	    imMovementHeads = imMovementHeadDAO
		    .findPOSMovementByCondition(conditionMap);
	else
	    imMovementHeads = imMovementHeadDAO
		    .findTransportDataForPOS(conditionMap);

	Long[] transportIdArray = null;
	if (imMovementHeads != null && imMovementHeads.size() > 0) {
	    transportIdArray = new Long[imMovementHeads.size()];
	    for (int i = 0; i < imMovementHeads.size(); i++) {
		Object[] topRecord = new Object[topFields.length];
		ImMovementHead movementHead = (ImMovementHead) imMovementHeads
			.get(i);
		transportIdArray[i] = movementHead.getHeadId();
		List movementItems = movementHead.getImMovementItems();
		String deliveryWarehouseCode = OldSysMapNewSys
			.getOldWarehouse(movementHead
				.getDeliveryWarehouseCode());
		String arrivalWarehouseCode = OldSysMapNewSys
			.getOldWarehouse(movementHead.getArrivalWarehouseCode());
		String deliveryDate = DateUtils.formatToTWDate(movementHead
			.getDeliveryDate(), DateUtils.C_DATE_PATTON_DEFAULT);
		topRecord[0] = movementHead.getOriginalOrderNo();
		topRecord[1] = deliveryWarehouseCode;
		topRecord[2] = arrivalWarehouseCode;
		topRecord[3] = deliveryDate;
		topRecord[4] = movementHead.getOriginalOrderTypeCode();
		topRecord[5] = "OK";
		topRecord[6] = "OK";

		if (movementItems != null && movementItems.size() > 0) {
		    topRecord[7] = new Integer(movementItems.size())
			    .doubleValue();
		    Double deliveryQuantityAmt = 0D;
		    for (int j = 0; j < movementItems.size(); j++) {
			Object[] detRecord = new Object[detFields.length];
			ImMovementItem movementItem = (ImMovementItem) movementItems
				.get(j);
			String itemDeliveryWarehouseCode = OldSysMapNewSys
				.getOldWarehouse(movementItem
					.getDeliveryWarehouseCode());
			String itemArrivalWarehouseCode = OldSysMapNewSys
				.getOldWarehouse(movementItem
					.getArrivalWarehouseCode());
			detRecord[0] = movementHead.getOriginalOrderNo();
			detRecord[1] = itemDeliveryWarehouseCode;
			detRecord[2] = itemArrivalWarehouseCode;
			detRecord[3] = deliveryDate;
			detRecord[4] = "OK";
			detRecord[5] = 0D;
			detRecord[6] = new Integer(j + 1).doubleValue();
			detRecord[7] = OldSysMapNewSys.getOldItemCode(
				brandCode, movementItem.getItemCode())
				.getItemCode();
			Double deliveryQuantity = movementItem
				.getDeliveryQuantity();
			if (deliveryQuantity == null) {
			    deliveryQuantity = 0D;
			}
			deliveryQuantityAmt += deliveryQuantity;
			detRecord[8] = deliveryQuantity;
			detRecords.add(detRecord);
		    }
		    topRecord[8] = deliveryQuantityAmt;
		} else {
		    topRecord[7] = 0D;
		    topRecord[8] = 0D;
		}

		topRecord[9] = trimString(movementHead.getRemark1(),
			topFields[9].getLength());
		topRecords.add(topRecord);
	    }
	}
	// write to file
	for (String targetTopFile : targetTopFiles) {
	    DBFUtils.writeDBF(targetTopFile, topRecords, topFields);
	}
	for (String targetDetFile : targetDetFiles) {
	    DBFUtils.writeDBF(targetDetFile, detRecords, detFields);
	}
	return transportIdArray;
    }

    /**
     * 會員主檔
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws IOException
     * @throws JDBFException
     */
    public Date createCustomerDBF(String brandCode, String templateFile,
	    String targetFiles[], String actualDataDate,
	    String actualDataDateEnd) throws IOException, JDBFException {
	log.info("POSExportData.createCustomerDBF");
	/*
	 * String actualDataDate = DateUtils.format(DateUtils.addDays(new
	 * Date(), -1), DateUtils.C_DATA_PATTON_YYYYMMDD); String
	 * actualDataDateEnd = actualDataDate;
	 * if(StringUtils.hasText(dataDate)){ actualDataDate = dataDate; }
	 * if(StringUtils.hasText(dataDateEnd)){ actualDataDateEnd =
	 * dataDateEnd; }
	 */
	JDBField[] fields = templateFields.get(templateFile);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(templateFile);
	    templateFields.put(templateFile, fields);
	}
	List<Object[]> records = new ArrayList();

	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	searchKey.put("dataDate", actualDataDate);
	searchKey.put("dataDateEnd", actualDataDateEnd);
	BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils
		.getApplicationContext().getBean("buAddressBookDAO");
	BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO) SpringUtils
		.getApplicationContext().getBean("buCommonPhraseLineDAO");
	List<Object[]> results = buAddressBookDAO
		.findCustomerListNoLimit(searchKey);
	if (results != null) {
	    for (Object[] result : results) {
		Object[] record = new Object[fields.length];
		BuAddressBook buAddressBook = (BuAddressBook) result[0];
		BuCustomer buCustomer = (BuCustomer) result[1];
		BuCustomerId id = buCustomer.getId();
		String isEnable = buCustomer.getEnable();
		if ("Y".equals(isEnable)) {
		    record[0] = "U";
		} else {
		    record[0] = "D";
		}
		record[1] = id.getCustomerCode();
		record[2] = trimString(buAddressBook.getChineseName(),
			fields[2].getLength());
		record[3] = buAddressBook.getIdentityCode();
		if ((null != buAddressBook.getBirthdayYear())
			&& (null != buAddressBook.getBirthdayYear())
			&& (null != buAddressBook.getBirthdayYear())) {
		    record[4] = dateFormat(buAddressBook.getBirthdayYear()
			    .intValue(), buAddressBook.getBirthdayMonth()
			    .intValue(), buAddressBook.getBirthdayDay()
			    .intValue());
		} else {
		    record[4] = "";
		}
		record[5] = trimString(buAddressBook.getGender(), fields[5]
			.getLength());
		record[6] = trimString(buAddressBook.getTel2(), fields[6]
			.getLength());
		record[7] = trimString(buAddressBook.getMobilePhone(),
			fields[7].getLength());
		record[8] = trimString(buAddressBook.getEMail(), fields[8]
			.getLength());
		// =======================職業類別=======================
		record[10] = "";
		String jobType = buCustomer.getCategory01();
		BuCommonPhraseLineId commonPhraseLineId = new BuCommonPhraseLineId(
			new BuCommonPhraseHead("JobType"), jobType);
		BuCommonPhraseLine commonPhraseLine = buCommonPhraseLineDAO
			.findById(commonPhraseLineId);
		if (commonPhraseLine != null) {
		    record[10] = trimString(commonPhraseLine.getName(),
			    fields[10].getLength());
		}
		// ========================年收入========================
		record[11] = 0D;
		String yearIncomeType = buCustomer.getCategory03();
		if ("1".equals(yearIncomeType)) {
		    record[11] = 50D;
		} else if ("2".equals(yearIncomeType)) {
		    record[11] = 100D;
		} else if ("3".equals(yearIncomeType)) {
		    record[11] = 150D;
		} else if ("4".equals(yearIncomeType)) {
		    record[11] = 200D;
		}
		// =====================================================
		record[12] = trimString(buAddressBook.getCompanyName(),
			fields[12].getLength());
		record[13] = "";
		record[14] = trimString(buAddressBook.getTel1(), fields[14]
			.getLength());
		record[15] = trimString(buAddressBook.getFax1(), fields[15]
			.getLength());
		// ==================通訊地址=============================
		record[16] = trimString(StringTools
			.replaceNullToSpace(buAddressBook.getCity())
			+ StringTools.replaceNullToSpace(buAddressBook
				.getArea())
			+ StringTools.replaceNullToSpace(buAddressBook
				.getAddress()), fields[16].getLength());
		// ==================送貨地址=============================
		record[17] = trimString(StringTools
			.replaceNullToSpace(buCustomer.getCity3())
			+ StringTools.replaceNullToSpace(buCustomer.getArea3())
			+ StringTools.replaceNullToSpace(buCustomer
				.getAddress3()), fields[17].getLength());
		// ==================特惠價別=============================
		record[18] = "0";
		String vipTypeCode = buCustomer.getVipTypeCode();
		commonPhraseLineId = new BuCommonPhraseLineId(
			new BuCommonPhraseHead("VIPType"), vipTypeCode);
		commonPhraseLine = buCommonPhraseLineDAO
			.findById(commonPhraseLineId);
		if (commonPhraseLine != null) {
		    record[18] = commonPhraseLine.getAttribute3();
		}
		record[9] = record[18];
		// ===================會員起始日、會員到期日=================
		if (buCustomer.getVipStartDate() != null)
		    record[19] = dateFormat(buCustomer.getVipStartDate());
		else
		    record[19] = "";

		if (buCustomer.getVipEndDate() != null)
		    record[20] = dateFormat(buCustomer.getVipEndDate());
		else
		    record[20] = "";
		// =====================申請櫃別===========================
		record[21] = "";
		String applyShopCode = buCustomer.getCategory07();
		String oldShopCode = OldSysMapNewSys
			.getOldShopCode(applyShopCode);
		if (oldShopCode != null) {
		    record[21] = trimString(oldShopCode, fields[21].getLength());
		}
		// ====================鞋子、衣服尺寸=========================
		record[22] = trimString(buCustomer.getCategory08(), fields[22]
			.getLength());
		record[23] = trimString(buCustomer.getCategory09(), fields[23]
			.getLength());
		// ========================================================
		record[24] = "";
		record[25] = "";
		record[26] = "";
		record[27] = "";
		// ====================第一次消費金額=========================
		record[28] = trimString(buCustomer.getCategory10(), fields[28]
			.getLength());
		// ========================================================
		record[29] = "";
		record[30] = trimString(buCustomer.getCategory14(), fields[30]
			.getLength());
		record[31] = trimString(buCustomer.getCategory11(), fields[31]
			.getLength());
		record[32] = trimString(buCustomer.getCategory12(), fields[32]
			.getLength());
		record[33] = trimString(buCustomer.getCategory13(), fields[33]
			.getLength());
		record[34] = "";
		record[35] = "";
		record[36] = "";
		record[37] = "";
		record[38] = buCustomer.getTotalExpendAmount();
		record[39] = buCustomer.getPeriodExpendAmount();
		record[40] = "";
		record[41] = "";

		records.add(record);
	    }
	}
	for (String targetFile : targetFiles) {
	    DBFUtils.writeDBF(targetFile, records, fields);
	}
	return DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,
		actualDataDateEnd);
    }

    /**
     * 銷售員
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws IOException
     * @throws JDBFException
     */
    public void createEcrsalesDBF(String brandCode, String templateFile,
	    String targetFiles[], String dataDate, String dataDateEnd)
	    throws IOException, JDBFException {
	log.info("POSExportData.createEcrsalesDBF");
	JDBField[] fields = templateFields.get(templateFile);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(templateFile);
	    templateFields.put(templateFile, fields);
	}
	List<Object[]> records = new ArrayList();
	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	searchKey.put("isLeave", "N");
	searchKey.put("isShopEmployee", "Y");
	searchKey.put("isEnable", "Y");
	BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils
		.getApplicationContext().getBean("buAddressBookDAO");
	List<Object[]> results = buAddressBookDAO
		.findShopEmployeeList(searchKey);
	for (Object[] result : results) {
	    Object[] record = new Object[fields.length];
	    String employeeCode = (String) result[4];
	    String chineseName = (String) result[3];
	    String password = "";
	    String enable = "YYYYYYYYYYYYYYYYYYYY";
	    record[0] = employeeCode;
	    record[1] = trimString(chineseName, fields[1].getLength());
	    record[2] = password;
	    record[3] = enable;
	    records.add(record);
	}
	for (String targetFile : targetFiles) {
	    DBFUtils.writeDBF(targetFile, records, fields);
	}
    }

    /**
     * 庫存
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws IOException
     * @throws JDBFException
     */
    public void createGdsqtyDBF(String brandCode, String templateFile,
	    String targetFiles[], String actualDataDate,
	    String actualDataDateEnd) throws IOException, JDBFException {
	log.info("POSExportData.createGdsqtyDBF");
	String[] unSearchCodeArray = null;
	if ("T1BS".equals(brandCode)) {
	    unSearchCodeArray = new String[] { "00" };
	} else if ("T1CO".equals(brandCode)) {
	    unSearchCodeArray = new String[] { "00", "A9", "AA" };
	}
	StringBuffer assembly = new StringBuffer("");
	if (unSearchCodeArray != null) {
	    for (int i = 0; i < unSearchCodeArray.length; i++) {
		assembly.append("'");
		assembly.append(brandCode);
		assembly.append(unSearchCodeArray[i]);
		assembly.append("'");
		if (i < unSearchCodeArray.length - 1) {
		    assembly.append(",");
		}
	    }
	}
	JDBField[] fields = templateFields.get(templateFile);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(templateFile);
	    templateFields.put(templateFile, fields);
	}
	List<Object[]> records = new ArrayList();
	ImItemOnHandViewDAO imItemOnHandViewDAO = (ImItemOnHandViewDAO) SpringUtils
		.getApplicationContext().getBean("imItemOnHandViewDAO");
	List<Object[]> results = imItemOnHandViewDAO
		.getShopItemOnHandByCondition(brandCode, actualDataDate,
			actualDataDateEnd, "Y", assembly.toString());
	if (results != null) {
	    for (Object[] result : results) {
		Object[] record = new Object[fields.length];
		String itemCode = OldSysMapNewSys.getOldItemCode(brandCode,
			(String) result[0]).getItemCode();
		String warehouseCode = (String) result[1];
		BigDecimal onHand = (BigDecimal) result[2];
		Double currentOnHand = 0D;
		if (onHand != null) {
		    currentOnHand = onHand.doubleValue();
		}
		record[0] = itemCode;
		record[1] = warehouseCode;
		record[2] = currentOnHand;
		records.add(record);
	    }
	    Object[] lastRecord = new Object[fields.length];
	    lastRecord[0] = "ZZZZLASTDATE";
	    lastRecord[1] = DateUtils.formatToTWDate(DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd),
		    DateUtils.C_DATE_PATTON_DEFAULT);
	    lastRecord[2] = 0D;
	    records.add(lastRecord);
	}
	for (String targetFile : targetFiles) {
	    DBFUtils.writeDBF(targetFile, records, fields);
	}
    }

    /**
     * 商品
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws IOException
     * @throws JDBFException
     */
    public void createGoodslstDBF(String brandCode, String templateFile,
	    String targetFiles[], Date dataDate, Date dataDateEnd)
	    throws IOException, JDBFException {
	log.info("POSExportData.createGoodslstDBF");
	String actualDataDate = DateUtils.format(dataDate,
		DateUtils.C_DATA_PATTON_YYYYMMDD);
	String actualDataDateEnd = DateUtils.format(dataDateEnd,
		DateUtils.C_DATA_PATTON_YYYYMMDD);
	Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd),
		1);
	JDBField[] fields = templateFields.get(templateFile);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(templateFile);
	    templateFields.put(templateFile, fields);
	}
	String priceType = "1";
	ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils
		.getApplicationContext().getBean("imItemPriceDAO");
	List<Object[]> records = new ArrayList();
	List<Object[]> results = imItemPriceDAO.getItemPriceByCondition(
		brandCode, priceType, beginDate, actualDataDate,
		actualDataDateEnd, "Y", "Y");
	if (results != null) {
	    for (Object[] result : results) {
		String itemCode = (String) result[1];
		String itemName = (String) result[2];
		String CLA1NO = (String) result[10];
		String CLA2NO = (String) result[11];
		String CLA3NO = (String) result[12];
		String isServiceItem = (String) result[8];
		String salesUnit = (String) result[5];
		String taxMode = "Y";
		Double unitPrice = ((BigDecimal) result[6]).doubleValue();
		// VIP會員
		String vipDiscount = (String) result[13];
		Double PRICE_R1 = unitPrice;
		if (!"T1BS".equals(brandCode) && !"N".equals(vipDiscount)) {
		    ImPromotion vp = getVIPPromotion("VIPType", brandCode
			    + "VIP", brandCode);
		    PRICE_R1 = getPromotionPrice(unitPrice, itemCode, vp);
		}

		/*
		 * 生日 ImPromotion bp = getVIPPromotion("BRDType", brandCode +
		 * "BRD", brandCode); Double PRICE_R2 =
		 * getPromotionPrice(unitPrice, itemCode, bp);
		 */
		Object[] record = new Object[fields.length];
		record[0] = "U";
		record[1] = OldSysMapNewSys.getOldItemCode(brandCode, itemCode)
			.getItemCode();
		record[2] = "";
		record[3] = itemName;
		record[4] = brandCode;
		record[5] = trimString(CLA1NO, fields[5].getLength());
		record[6] = trimString(CLA2NO, fields[6].getLength());
		record[7] = trimString(CLA3NO, fields[7].getLength());
		if ("Y".equals(isServiceItem)) {
		    record[8] = true;
		} else {
		    record[8] = false;
		}
		record[9] = salesUnit;
		record[10] = new Double(0);
		record[11] = taxMode;
		record[12] = unitPrice;
		record[13] = unitPrice;
		record[14] = PRICE_R1;
		record[15] = new Double(0);
		record[16] = new Double(0);
		record[17] = new Double(0);
		record[18] = "";
		record[19] = "";
		record[20] = "";
		record[21] = "";
		record[22] = "";
		records.add(record);
	    }
	}
	for (String targetFile : targetFiles) {
	    DBFUtils.writeDBF(targetFile, records, fields);
	}
    }

    /**
     * 促銷
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws IOException
     * @throws JDBFException
     */
    public void createPromotionDBF(String brandCode, String templateFile,
	    String targetFiles[], String dataDate, String actualDataDateEnd)
	    throws IOException, JDBFException, Exception {
	log.info("POSExportData.createPromotionDBF");
	/*
	 * String actualDataDateEnd = DateUtils.format(DateUtils.addDays(new
	 * Date(), -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	 * if(StringUtils.hasText(dataDateEnd)){ actualDataDateEnd =
	 * dataDateEnd; }
	 */
	JDBField[] fields = templateFields.get(templateFile);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(templateFile);
	    templateFields.put(templateFile, fields);
	}
	TreeMap actualPromotionItemMap = new TreeMap();
	ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils
		.getApplicationContext().getBean("imPromotionViewDAO");
	// ==============================取得isAllShop不為Y的promotion======================
	TreeMap shopPromotionItemMap = new TreeMap();
	List shopPromotionItems = imPromotionViewDAO.getPromotionItemInfo(
		brandCode, "1", actualDataDateEnd, "N");
	if (shopPromotionItems != null) {
	    for (int i = 0; i < shopPromotionItems.size(); i++) {
		Object[] promotionInfo = (Object[]) shopPromotionItems.get(i);
		filterPromotionItem(shopPromotionItemMap, promotionInfo);
	    }
	}
	// ==============================取得isAllShop為Y的promotion========================
	TreeMap promotionItemMap = new TreeMap();
	List promotionItems = imPromotionViewDAO.getPromotionItemInfo(
		brandCode, "1", actualDataDateEnd, "Y");
	if (promotionItems != null) {
	    for (int i = 0; i < promotionItems.size(); i++) {
		Object[] promotionInfo = (Object[]) promotionItems.get(i);
		filterPromotionItem(promotionItemMap, promotionInfo);
	    }
	}
	// ==============================取得比對後的promotion
	// item========================
	getActualExportPromotionItem(brandCode, actualPromotionItemMap,
		shopPromotionItemMap, promotionItemMap);
	Set set = actualPromotionItemMap.entrySet();
	Map.Entry[] promotionItemEntry = (Map.Entry[]) set
		.toArray(new Map.Entry[set.size()]);
	List<Object[]> records = new ArrayList();
	for (Map.Entry entry : promotionItemEntry) {
	    ImPromotionView promotionView = (ImPromotionView) entry.getValue();
	    String tempShopCode = promotionView.getShopCode();
	    if (StringUtils.hasText(tempShopCode)) {
		tempShopCode = OldSysMapNewSys.getOldShopCode(tempShopCode);
	    }
	    Date tempBeginDate = promotionView.getBeginDate();
	    String actualBeginDate = DateUtils.formatToTWDate(tempBeginDate,
		    DateUtils.C_DATE_PATTON_DEFAULT);
	    Date tempEndDate = promotionView.getEndDate();
	    String actualEndDate = DateUtils.formatToTWDate(tempEndDate,
		    DateUtils.C_DATE_PATTON_DEFAULT);
	    Double unitPrice = promotionView.getUnitPrice();
	    String discountType = promotionView.getDiscountType();
	    Double discount = promotionView.getDiscount();
	    Double promotionPrice = unitPrice;
	    if (StringUtils.hasText(discountType) && discount != null) {
		if ("1".equals(discountType)) {
		    promotionPrice = promotionPrice - discount;
		} else if ("2".equals(discountType)) {
		    promotionPrice = promotionPrice * (100 - discount) / 100;
		}
		if (promotionPrice < 0D) {
		    promotionPrice = 0D;
		}
	    }
	    Object[] record = new Object[fields.length];
	    record[0] = tempShopCode;
	    record[1] = actualBeginDate;
	    record[2] = "00:00";
	    record[3] = actualEndDate;
	    record[4] = "23:59";
	    record[5] = "";
	    record[6] = "";
	    record[7] = OldSysMapNewSys.getOldItemCode(brandCode,
		    promotionView.getItemCode()).getItemCode();
	    record[8] = promotionPrice;
	    record[9] = promotionPrice;
	    record[10] = promotionPrice;
	    record[11] = promotionPrice;
	    records.add(record);
	}
	// ==============================================================================
	for (String targetFile : targetFiles) {
	    DBFUtils.writeDBF(targetFile, records, fields);
	}
    }

    private Double calculatePromotion(ImPromotion pm, String discountType,
	    Double unitPrice) {
	log.info("POSExportData.calculatePromotion discountType="
		+ discountType + ",unitPrice" + unitPrice);
	Double price = new Double(0);
	if (null != pm && null != unitPrice) {
	    if (null != pm.getDiscount() && pm.getDiscount() > 0) {
		if ("1".equals(discountType)) { // 1 金額
		    price = unitPrice - pm.getDiscount();
		} else if ("2".equals(discountType)) { // 2折扣
		    price = unitPrice - (unitPrice * (pm.getDiscount() / 100));
		}
	    }
	}
	return price;
    }

    private String timeFormat(Date date) {
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	return timeFromat.format(c.getTime());
    }

    private String dateFormat(Date date) {
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	return dateFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c
		.get(Calendar.DATE));
    }

    private String dateFormat(int year, int month, int day) {
	Calendar c = Calendar.getInstance();
	c.set(year - 1911, month - 1, day);
	return dateFromat.format(c.getTime());
    }

    private ImPromotion getVIPPromotion(String headCode, String lineCode,
	    String brandCode) {
	log.info("POSExportData.getVIPPromotion headCode=" + headCode
		+ ",lineCode=" + lineCode + ",brandCode=" + brandCode);
	ImPromotion imPromotion = null;
	BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
		.getApplicationContext().getBean("buCommonPhraseService");
	BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
		.getBuCommonPhraseLine(headCode, lineCode);
	if (null != buCommonPhraseLine) {
	    String promotionCode = buCommonPhraseLine.getAttribute2();
	    ImPromotionDAO imPromotionDAO = (ImPromotionDAO) SpringUtils
		    .getApplicationContext().getBean("imPromotionDAO");
	    imPromotion = imPromotionDAO.findByBrandCodeAndPromotionCode(
		    brandCode, promotionCode);
	}
	return imPromotion;
    }

    private Double getPromotionPrice(Double unitPrice, String itemCode,
	    ImPromotion imPromotion) {
	log.info("POSExportData.getPromotionPrice unitPrice=" + unitPrice
		+ ",itemCode=" + itemCode);
	Double re = new Double(0);
	// 0.HEAD DISCOUNT 比例
	if (null != imPromotion) {
	    if (null != imPromotion.getDiscount()) {
		re = unitPrice * (100L - imPromotion.getDiscount()) / 100;
	    } else {
		List<ImPromotionItem> imPromotionItems = imPromotion
			.getImPromotionItems();
		if (null != imPromotionItems) {
		    for (ImPromotionItem imPromotionItem : imPromotionItems) {
			// 1.抓ITEM
			if ("itemCode".equalsIgnoreCase(imPromotionItem
				.getItemCode())) {
			    // 2.type = 1 金額 / 2 比例
			    String type = imPromotionItem.getDiscountType();
			    if ("1".equals(type)) {
				re = unitPrice - imPromotionItem.getDiscount();
			    } else if ("2".equals(type)) {
				re = unitPrice
					* (100D - imPromotionItem.getDiscount())
					/ 100;
			    }
			    break;
			}
		    }
		} else {
		    re = unitPrice;
		}
	    }
	} else {
	    re = unitPrice;
	}
	return CommonUtils.round(re, 0);
    }

    private String trimString(String source, int length) {
	String re = "";
	if (null != source && source.length() > 0) {
	    re = source;
	    if (source.length() > length) {
		re = source.substring(0, length);
	    }
	}
	return re;
    }

    public static void setTemplateFields(Map<String, JDBField[]> templateFields) {
	POSIslandsExportData.templateFields = templateFields;
    }

    private void filterPromotionItem(TreeMap map, Object[] promotionItemInfo) {

	String brandCode = (String) promotionItemInfo[0];
	String promotionCode = (String) promotionItemInfo[1];
	String shopCode = (String) promotionItemInfo[2];
	if (shopCode == null) {
	    shopCode = "";
	}
	Date beginDate = (Date) promotionItemInfo[3];
	Date endDate = (Date) promotionItemInfo[4];
	String itemCode = (String) promotionItemInfo[5];
	String discountType = (String) promotionItemInfo[6];
	Double discount = ((BigDecimal) promotionItemInfo[7]).doubleValue();
	Double unitPrice = ((BigDecimal) promotionItemInfo[8]).doubleValue();
//	Date lastUpdateDate = (Date) promotionItemInfo[9];
	// sql結構為了顯示去比對時分秒去比對同一天促銷如何判斷，所以改成這樣的寫法 
	String updateDateString = (String)promotionItemInfo[9];
	log.info("ORIG promotionItemInfo[9] = " + promotionItemInfo[9]);
	Date lastUpdateDate = DateUtils.parseDate(DateUtils.C_TIME_PATTON_DEFAULT, updateDateString);
	   
	// ===============set promotionView bean================
	ImPromotionView promotionView = new ImPromotionView();
	promotionView.setBrandCode(brandCode);
	promotionView.setPromotionCode(promotionCode);
	promotionView.setShopCode(shopCode);
	promotionView.setBeginDate(beginDate);
	promotionView.setEndDate(endDate);
	promotionView.setItemCode(itemCode);
	promotionView.setDiscountType(discountType);
	promotionView.setDiscount(discount);
	promotionView.setUnitPrice(unitPrice);
	promotionView.setLastUpdateDate(lastUpdateDate);
	// ======================================================
	String key = itemCode + "#" + shopCode;
	ImPromotionView tempPromotionView = (ImPromotionView) map.get(key);
	if (tempPromotionView == null) {
	    map.put(key, promotionView);
	} else {
	    Date tempBeginDate = tempPromotionView.getBeginDate();
	    Date tempLastUpdateDate = tempPromotionView.getLastUpdateDate();
	    if (beginDate.after(tempBeginDate)) {
		map.put(key, promotionView);
	    } else if (beginDate.getTime() == tempBeginDate.getTime()) {
		// 活動日期相同時，依據更新日期作比對
		if (lastUpdateDate != null && tempLastUpdateDate != null) {
		    if (lastUpdateDate.after(tempLastUpdateDate)) {
			map.put(key, promotionView);
		    }
		}
	    }
	}
    }

    private void getActualExportPromotionItem(String brandCode,
	    TreeMap actualPromotionItemMap, TreeMap shopPromotionItemMap,
	    TreeMap promotionItemMap) throws Exception {

	TreeMap tempMap = new TreeMap();
	// =================取得此brand下啟用中的專櫃=========================
	BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext()
		.getBean("buShopDAO");
	List buShopInfos = buShopDAO.findShopByBrandAndEnable(brandCode, "Y");
	List<String> brandShops = new ArrayList();
	if (buShopInfos != null) {
	    for (int i = 0; i < buShopInfos.size(); i++) {
		BuShop buShop = (BuShop) buShopInfos.get(i);
		brandShops.add(buShop.getShopCode());
	    }
	}
	Set set = shopPromotionItemMap.entrySet();
	Map.Entry[] promotionItemEntry = (Map.Entry[]) set
		.toArray(new Map.Entry[set.size()]);
	for (Map.Entry entry : promotionItemEntry) {
	    String key = (String) entry.getKey();
	    // ===================單一專櫃促銷=====================
	    ImPromotionView shopPromotionView = (ImPromotionView) entry
		    .getValue();
	    Date ShopBeginDate = shopPromotionView.getBeginDate();
	    Date ShopLastUpdateDate = shopPromotionView.getLastUpdateDate();
	    String[] keyArray = StringTools.StringToken(key, "#");
	    String searchKey = keyArray[0] + "#";
	    // ====================全專櫃促銷======================
	    ImPromotionView promotionView = (ImPromotionView) promotionItemMap
		    .get(searchKey);
	    // ==================================================
	    if (promotionView == null) {
		actualPromotionItemMap.put(key, shopPromotionView);
	    } else {
		Date beginDate = promotionView.getBeginDate();
		Date lastUpdateDate = promotionView.getLastUpdateDate();
		String isAllShop = "Y";
		if (ShopBeginDate.after(beginDate)) {
		    actualPromotionItemMap.put(key, shopPromotionView);
		    isAllShop = "N";
		} else if (ShopBeginDate.getTime() == beginDate.getTime()) {
		    // 活動日期相同時，依據更新日期作比對
		    if (ShopLastUpdateDate != null && lastUpdateDate != null) {
			if (ShopLastUpdateDate.after(lastUpdateDate)) {
			    actualPromotionItemMap.put(key, shopPromotionView);
			    isAllShop = "N";
			} else if (ShopLastUpdateDate.getTime() == lastUpdateDate
				.getTime()) {
			    actualPromotionItemMap.put(key, shopPromotionView);
			    isAllShop = "N";
			}
		    }
		}
		// ===============自動新增出其他專櫃促銷資料=======================
		if ("N".equals(isAllShop)) {
		    tempMap.put(searchKey, searchKey);// 此map中的promotionItem不處理
		    for (int i = 0; i < brandShops.size(); i++) {
			String tempShop = brandShops.get(i);
			if (!keyArray[1].equals(tempShop)) {
			    ImPromotionView newPromotionView = new ImPromotionView();
			    BeanUtils.copyProperties(promotionView,
				    newPromotionView);
			    newPromotionView.setShopCode(tempShop);
			    actualPromotionItemMap.put(searchKey + tempShop,
				    newPromotionView);
			}
		    }
		}
	    }
	}
	// =========================處理全專櫃的promotion==========================
	Set allShopSet = promotionItemMap.entrySet();
	Map.Entry[] allShopPromotionItemEntry = (Map.Entry[]) allShopSet
		.toArray(new Map.Entry[allShopSet.size()]);
	for (Map.Entry entry : allShopPromotionItemEntry) {
	    String key = (String) entry.getKey();
	    ImPromotionView promotionView = (ImPromotionView) entry.getValue();
	    if (tempMap.get(key) == null) {
		actualPromotionItemMap.put(key, promotionView);
	    }
	}
    }

    /**
     * T2會員主檔
     * 
     * @param brandCode
     * @param targetFiles
     * @param actualDataDate
     * @param actualDataDateEnd
     * @throws Exception
     */
    public void createT2Customer(String brandCode, String[] targetFiles,
	    String actualDataDate, String actualDataDateEnd) throws Exception {

	log.info("POSExportData.createT2Customer");
	List<String> records = new ArrayList();
	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	searchKey.put("dataDate", actualDataDate);
	searchKey.put("dataDateEnd", actualDataDateEnd);
	BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils
		.getApplicationContext().getBean("buAddressBookDAO");
	List<Object[]> results = buAddressBookDAO
		.findCustomerListNoLimit(searchKey);
	if (results != null && results.size() > 0) {
	    BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
		    .getApplicationContext().getBean("buCommonPhraseService");
	    StringBuffer record = new StringBuffer();
	    for (Object[] result : results) {
		record.delete(0, record.length());
		BuAddressBook buAddressBook = (BuAddressBook) result[0];
		BuCustomer buCustomer = (BuCustomer) result[1];
		BuCustomerId id = buCustomer.getId();
		String isEnable = buCustomer.getEnable();
		if ("Y".equals(isEnable) || "E".equals(isEnable)) {
		    record.append("U");
		} else {
		    record.append("D");
		}
		String customerCode = id.getCustomerCode();
		String vipTypeCode = buCustomer.getVipTypeCode();
		if ("11".equals(vipTypeCode) || "21".equals(vipTypeCode)
			|| "31".equals(vipTypeCode) || "51".equals(vipTypeCode)) {
		    customerCode = ":"
			    + CommonUtils.insertCharacterWithLimitedLength(
				    customerCode, 9, 9, CommonUtils.SPACE, "R");
		} else {
		    customerCode = CommonUtils
			    .insertCharacterWithLimitedLength(customerCode, 10,
				    10, CommonUtils.SPACE, "R");
		}
		// ==================取得VipTypeCode的身份=====================
		String identification = "";
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
			.getBuCommonPhraseLine("VIPType", vipTypeCode);
		if (buCommonPhraseLine != null) {
		    identification = buCommonPhraseLine.getAttribute4();
		}

		record.append(customerCode);
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			buAddressBook.getChineseName(), 8, 8,
			CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			vipTypeCode, 2, 2, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			identification, 2, 2, CommonUtils.SPACE, "R"));
		records.add(record.toString());
	    }
	    for (String targetFile : targetFiles) {
		TxtUtil.exportTxt(targetFile, records);
	    }
	}
    }

    /**
     * T2銷售人員
     * 
     * @param brandCode
     * @param targetFiles
     * @param actualDataDate
     * @param actualDataDateEnd
     * @throws Exception
     */
    public void createT2Sales(String brandCode, String[] targetFiles,
	    String actualDataDate, String actualDataDateEnd) throws Exception {

	log.info("POSExportData.createT2Sales");
	List<String> records = new ArrayList();
	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	searchKey.put("dataDate", actualDataDate);
	searchKey.put("dataDateEnd", actualDataDateEnd);
	searchKey.put("leaveDate", actualDataDate);
	Date date2 = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDate);
	    
	Calendar  cal1 = Calendar.getInstance();
	cal1.setTime(date2);
	cal1.set(Calendar.HOUR_OF_DAY, 0);
	cal1.set(Calendar.MINUTE, 0);
	cal1.set(Calendar.SECOND, 0);
	
	BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils
		.getApplicationContext().getBean("buAddressBookDAO");
	List<Object[]> results = buAddressBookDAO
		.findEmployeeByCondition(searchKey);
	if (results != null && results.size() > 0) {
	    StringBuffer record = new StringBuffer();
	    for (Object[] result : results) {
		log.info("FOR迴圈++++++++++++++++");
		record.delete(0, record.length());
		BuAddressBook buAddressBook = (BuAddressBook) result[0];
		BuEmployee buEmployee = (BuEmployee) result[1];
		log.info("buEmployeeReamrk="+buEmployee.getRemark2());
		if ("Y".equals(buEmployee.getRemark2())) {
		    log.info("Y.equals(buEmployee.getRemark2");
		    Date leaveDate = buEmployee.getLeaveDate();
		    
		    if (leaveDate == null) {
			log.info("leaveDate == null");
			record.append("U");
		    } else {
			    Calendar leaveDateCal = Calendar.getInstance();			 
			    leaveDateCal.setTime(leaveDate);//set your date object1			    
			    leaveDateCal.set(Calendar.HOUR_OF_DAY, 0);			    
			    leaveDateCal.set(Calendar.MINUTE, 0);			    
			    leaveDateCal.set(Calendar.SECOND, 0);
			if(leaveDateCal.getTime().before(cal1.getTime()) || leaveDateCal.getTime().equals(cal1.getTime()) ){ // 離職日小於貨等於今日
			    record.append("D");
			}else{
			    record.append("U");
			}
		    }
		    record.append(CommonUtils.insertCharacterWithLimitedLength(
			    buEmployee.getEmployeeCode(), 8, 8,
			    CommonUtils.SPACE, "R"));
		    record.append(CommonUtils.insertCharacterWithLimitedLength(
			    buAddressBook.getChineseName(), 8, 15,
			    CommonUtils.SPACE, "R"));
		    records.add(record.toString());
		}
	    }
	    if (records.size() > 0) {
		 log.info("records="+records);
		for (String targetFile : targetFiles) {
		    TxtUtil.exportTxt(targetFile, records);
		}
	    }
	}else{
	    System.out.print("執行下傳失敗");
	}
    }

    /**
     * 國際條碼
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportEanCodeData(String brandCode, String dataDate,
	    String dataDateEnd, String opUser, String customsWarehouseCode) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportEanCodeData");
	    String actualDataDate = DateUtils.format(currentDate,
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String actualDataDateEnd = actualDataDate;
	    if (StringUtils.hasText(dataDate)) {
		actualDataDate = dataDate;
	    }
	    if (StringUtils.hasText(dataDateEnd)) {
		actualDataDateEnd = dataDateEnd;
	    }
	    Date transactionDate = DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_EANCODE_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands國際碼資料下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = iniProperties.getProperty("eanstoreFile");
	    String eanstoreOut = iniProperties.getProperty(brandCode
		    + "Islands.eanstoreOut");
	    String[] eanstoreOuts = eanstoreOut.split(",");
	    if ("T2".equals(brandCode)) {
		String[] actualOutPaths = getActualOutPaths(brandCode,
			actualDataDate, fileName, eanstoreOuts);
		createT2EanCode(brandCode, actualOutPaths, actualDataDate,
			actualDataDateEnd, customsWarehouseCode);
	    }
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    transactionDate, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.EANCODE_DL_SUCCESS,
		    currentDate, uuid, opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.EANCODE_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * T2國際碼
     * 
     * @param brandCode
     * @param targetFiles
     * @param actualDataDate
     * @param actualDataDateEnd
     * @throws Exception
     */
    public void createT2EanCode(String brandCode, String[] targetFiles,
	    String actualDataDate, String actualDataDateEnd,
	    String customsWarehouseCode) throws Exception {
	log.info("POSExportData.createT2EanCode");
	List<String> records = new ArrayList();
	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	searchKey.put("dataDate", actualDataDate);
	searchKey.put("dataDateEnd", actualDataDateEnd);
	ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils
		.getApplicationContext().getBean("imItemEancodeDAO");
	List<Object[]> results = imItemEancodeDAO
		.findIslandEanCodeListByProperty(brandCode, actualDataDate,
			actualDataDateEnd, customsWarehouseCode);
	if (results != null && results.size() > 0) {
	    StringBuffer record = new StringBuffer();
	    for (Object[] result : results) {
		record.delete(0, record.length());
		String isEnable = (String) result[2];
		if ("Y".equals(isEnable)) {
		    record.append("U");
		} else {
		    record.append("D");
		}
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			(String) result[1], 13, 13, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			(String) result[0], 13, 13, CommonUtils.SPACE, "R"));
		records.add(record.toString());
	    }
	    for (String targetFile : targetFiles) {
		TxtUtil.exportTxt(targetFile, records);
	    }
	}
	/*
	 * List<ImItemEancode> itemEancodes =
	 * imItemEancodeDAO.findEanCodeListByProperty(searchKey);
	 * if(itemEancodes != null && itemEancodes.size() > 0){ StringBuffer
	 * record = new StringBuffer(); for (ImItemEancode itemEancode :
	 * itemEancodes) { record.delete(0, record.length()); String isEnable =
	 * itemEancode.getEnable(); if("Y".equals(isEnable)){
	 * record.append("U"); }else{ record.append("D"); }
	 * record.append(CommonUtils.insertCharacterWithLimitedLength(itemEancode.getEanCode(),
	 * 13, 13, CommonUtils.SPACE, "R"));
	 * record.append(CommonUtils.insertCharacterWithLimitedLength(itemEancode.getItemCode(),
	 * 13, 13, CommonUtils.SPACE, "R")); records.add(record.toString()); }
	 * for (String targetFile : targetFiles) { TxtUtil.exportTxt(targetFile,
	 * records); } }
	 */
    }

    /**
     * T2促銷
     * 
     * @param brandCode
     * @param targetFile
     * @param dataDate
     * @param actualDataDateEnd
     * @param currentDate
     * @throws Exception
     */
    public void createT2Promotion(String brandCode, String targetFiles[], String dataDate, String actualDataDateEnd, String finishedPromotionDate,
    	    String currentDate, String isAutoSchedule, Map configMap)
	    throws Exception {

	log.info("POSExportData.createT2Promotion");
	ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
	// ==============================取得isAllShop為Y的promotion========================
	TreeMap promotionItemMap = new TreeMap();
	List promotionItems = imPromotionViewDAO.getPromotionItemData(brandCode, "1", actualDataDateEnd, "Y", "Y");
	if (promotionItems != null) {
	    for (int i = 0; i < promotionItems.size(); i++) {
		Object[] promotionInfo = (Object[]) promotionItems.get(i);
		filterPromotionItem(promotionItemMap, promotionInfo);
	    }
	}
	// ==============================取得促銷到期後依原價賣出的促銷商品=======================
	TreeMap finishedPromotionItemMap = new TreeMap();
	List finishedPromotionItems = imPromotionViewDAO.getPromotionChangeItemData(brandCode, "1", actualDataDateEnd, finishedPromotionDate
			, currentDate, isAutoSchedule);
	if (finishedPromotionItems != null) {
	    for (int i = 0; i < finishedPromotionItems.size(); i++) {
		Object[] finishedPromotionInfo = (Object[]) finishedPromotionItems
			.get(i);
		filterPromotionItem(finishedPromotionItemMap,finishedPromotionInfo);
	    }
	}
	// ==============================匯出promotion
	// item========================
	getActualExportPromotionItemForT2(brandCode, promotionItemMap,finishedPromotionItemMap);
	Set set = promotionItemMap.entrySet();
	Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
	if (promotionItemEntry != null && promotionItemEntry.length > 0) {
	    StringBuffer record = new StringBuffer();
	    for (String targetFile : targetFiles) {	 // 每個後台的轉出檔案
		List<String> records = new ArrayList();
		for (Map.Entry entry : promotionItemEntry) {
		    record.delete(0, record.length());
		    ImPromotionView promotionView = (ImPromotionView) entry.getValue();
		    Date tempBeginDate = promotionView.getBeginDate();
//		    String actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		    
		    String actualBeginDate = null;
		    int dateLen = 6;
		    // 從路徑字串找到對應的配置檔文字
		    Iterator it = configMap.keySet().iterator();
		    while (it.hasNext()) {
			String key = (String) it.next();
			if( targetFile.indexOf(key) > -1 ){
			    String value = (String)configMap.get(key);
			    if("C".equalsIgnoreCase(value)){
				actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				dateLen = 8;
			    }else if("M".equalsIgnoreCase(value)){
				actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			    }else{
				//預設M
				actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			    }
//			    log.info("actualBeginDate = " + actualBeginDate);
//			    log.info("key = " + key);
//			    log.info("type = " + value);

			    break;
			}else{
			    // 為了測試環境而加入此情況強制都西元年 
			    actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			    dateLen = 8;
			    break;
			}
		    }
		    
		    
		    Double unitPrice = promotionView.getUnitPrice();
		    String discountType = promotionView.getDiscountType();
		    Double discount = promotionView.getDiscount();
		    Double promotionPrice = unitPrice;
		    /*
		    if (StringUtils.hasText(discountType) && discount != null) {
			if ("1".equals(discountType)) {
			    promotionPrice = promotionPrice - discount;
			} else if ("2".equals(discountType)) {
			    promotionPrice = CommonUtils.round((promotionPrice
				    * (100 - discount) / 100), 0);
			}
			if (promotionPrice < 0D) {
			    promotionPrice = 0D;
			}
		    }
		    */
		    record.append("U");
		    record.append(CommonUtils.insertCharacterWithLimitedLength(promotionView.getItemCode(), 20, 20, CommonUtils.SPACE,"R"));
		    record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8,CommonUtils.SPACE, "L"));
		    record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
		    record.append(CommonUtils.insertCharacterWithLimitedLength(actualBeginDate, dateLen, dateLen, CommonUtils.SPACE, "R"));
		    records.add(record.toString());
		}
	   
		TxtUtil.exportTxt(targetFile, records);
	    }
	}
    }

    /**
     * 取得實際匯出的促銷資料(T2)
     * 
     * @param brandCode
     * @param promotionItemMap
     * @param finishedPromotionItemMap
     * @throws Exception
     */
    private void getActualExportPromotionItemForT2(String brandCode,
	    TreeMap promotionItemMap, TreeMap finishedPromotionItemMap)
	    throws Exception {

	Set finishedPromotionItemSet = finishedPromotionItemMap.entrySet();
	Map.Entry[] finishedPromotionItemEntry = (Map.Entry[]) finishedPromotionItemSet
		.toArray(new Map.Entry[finishedPromotionItemSet.size()]);
	for (Map.Entry entry : finishedPromotionItemEntry) {
	    String key = (String) entry.getKey();
	    ImPromotionView promotionView = (ImPromotionView) entry.getValue();
	    if (promotionItemMap.get(key) == null) {
		promotionView.setDiscountType(null);
		promotionView.setDiscount(null);
		promotionView.setBeginDate(promotionView.getEndDate());
		promotionItemMap.put(key, promotionView);
	    }
	}
    }

    /**
     * T2商品
     * 
     * @param brandCode
     * @param templateFile
     * @param targetFile
     * @throws Exception
     */
    public void createT2Goodslst(String brandCode, String targetFiles[],
	    Date dataDate, Date dataDateEnd, String customsWarehouseCode)
	    throws Exception {

	log.info("POSIslandsExportData.createT2Goodslst");
	String actualDataDate = DateUtils.format(dataDate,
		DateUtils.C_DATA_PATTON_YYYYMMDD);
	String actualDataDateEnd = DateUtils.format(dataDateEnd,
		DateUtils.C_DATA_PATTON_YYYYMMDD);
	Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd),
		1);
	String priceType = "1";
	List<String> records = new ArrayList();
	ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");
	ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
	
	List<Object[]> results = imItemPriceDAO.getIslandItemInfoByCondition(
		brandCode, priceType, beginDate, actualDataDate,
		actualDataDateEnd, customsWarehouseCode);
	if (results != null && results.size() > 0) {
	    log.info("results.size() = " + results.size());
	    ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean("imItemCategoryDAO");
	    ImItemCategoryId categoryId = new ImItemCategoryId();
	    categoryId.setBrandCode(brandCode);
	    StringBuffer record = new StringBuffer();
	    for (Object[] result : results) {
		record.delete(0, record.length());
		String itemCode = (String) result[1];
		String itemName = (String) result[2];
		Double unitPrice = ((BigDecimal) result[6]).doubleValue();
		
		//===============加入查詢料號是否有促銷價===============2011.12.30 Caspar
//		log.info(" 撈出的修改品號 = " + itemCode);
		List<Object[]> imPromotionList = imPromotionViewDAO.findImPromotionForPOS(brandCode, "Y", itemCode, actualDataDate);
//		log.info("imPromotionList.size() = " + imPromotionList.size());
		if(imPromotionList.size()>0 && imPromotionList.get(0) != null){
			Object[] rec = imPromotionList.get(0);
			unitPrice = ((BigDecimal)rec[3]).doubleValue();
		}

		String isEnable = (String) result[9];
		String itemBrand = (String) result[10]; // 商品品牌
		String itemBrandName = "";
		String category01 = (String) result[11]; // 大類代碼
		String category02 = (String) result[12]; // 中類代碼
		String isTax = (String) result[13]; // 稅別
		String actualTaxCode = "0"; // 預設稅別0
		String vipDiscount = (String) result[14]; // 折扣類型
		String category01Name = "";
		String category02Name = "";
		Double minRatio = ((BigDecimal) result[15]).doubleValue(); // 銷售單位/最小單位換算
		Double declRatio = ((BigDecimal) result[16]).doubleValue(); // 銷售單位/報關單位換算
		String lotContro = (String) result[17]; // 批號控管
		String salesUnit = (String) result[5]; // 銷售單位

		// =============查詢商品品牌名稱=======================
		categoryId.setCategoryType("ItemBrand");
		categoryId.setCategoryCode(itemBrand);
		ImItemCategory itemCatagory = (ImItemCategory) imItemCategoryDAO
			.findByPrimaryKey(ImItemCategory.class, categoryId);
		if (itemCatagory != null) {
		    itemBrandName = itemCatagory.getCategoryName();
		    if(StringUtils.hasText(itemBrandName)){
			itemBrandName = itemBrandName.replaceAll("'", "");
		    }
		}
		// =============查詢大類名稱=======================
		categoryId.setCategoryType("CATEGORY01");
		categoryId.setCategoryCode(category01);
		itemCatagory = (ImItemCategory) imItemCategoryDAO
			.findByPrimaryKey(ImItemCategory.class, categoryId);
		if (itemCatagory != null) {
		    category01Name = itemCatagory.getCategoryName();
		}
		// =============查詢中類名稱=======================
		categoryId.setCategoryType("CATEGORY02");
		categoryId.setCategoryCode(category02);
		itemCatagory = (ImItemCategory) imItemCategoryDAO
			.findByPrimaryKey(ImItemCategory.class, categoryId);
		if (itemCatagory != null) {
		    category02Name = itemCatagory.getCategoryName();
		}
		// =============含稅(1)未稅(0)=========================
		if ("P".equals(isTax)) {
		    actualTaxCode = "1";
		}
		// ===============將資料寫入===========================
		if ("Y".equals(isEnable)) {
		    record.append("U");
		} else {
		    record.append("D");
		}
		record.append(CommonUtils.insertCharacterWithLimitedLength(itemCode, 20, 20, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(itemBrandName, 24, 24, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(itemName, 24, 24, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(category02Name, 15, 15, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8,CommonUtils.SPACE, "L"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8,CommonUtils.SPACE, "L"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8,CommonUtils.SPACE, "L"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(itemBrand, 6, 6, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(category02, 6, 6, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(category01Name, 20, 20, CommonUtils.SPACE, "R"));
		record.append(actualTaxCode);
		record.append(CommonUtils.insertCharacterWithLimitedLength("110", 3, 3, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(vipDiscount, 2, 2, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(minRatio.toString(), 6, 6, CommonUtils.SPACE, "L"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(declRatio.toString(), 6, 6, CommonUtils.SPACE, "L"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(lotContro, 1, 1, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(salesUnit, 10, 10, CommonUtils.SPACE, "R"));
		records.add(record.toString());
	    }
	    for (String targetFile : targetFiles) {
		TxtUtil.exportTxt(targetFile, records);
	    }
	}
    }

    private String[] getActualOutPaths(String brandCode, String dataDate,
	    String fileName, String[] originalOutPaths) {

	String backupPath = iniProperties.getProperty(brandCode
		+ "Islands.backupPath");
	StringBuffer backupFile = new StringBuffer(backupPath);
	backupFile.append(dataDate);
	backupFile.append("/");
	backupFile.append(fileName);
	String[] actualOutPaths = new String[originalOutPaths.length + 1];
	System.arraycopy(originalOutPaths, 0, actualOutPaths, 0,
		originalOutPaths.length);
	actualOutPaths[actualOutPaths.length - 1] = backupFile.toString();
	return actualOutPaths;
    }

    /**
     * 取得配置檔
     * @param customerConfig
     * @return
     */
    private Map getConfig(String brandCode, String type){
	Map map = new HashMap();
	String config = iniProperties.getProperty(brandCode + type);  
	String[] configs = config.split(","); //分割第一層
	for (String cfg : configs) {
	    String[] cfgs = cfg.split(":");   //分割第二層
	    if(cfgs.length == 2){
		map.put(cfgs[0], cfgs[1]);   
	    }
	}
	return map;
    }
    
    /**
     * ITEM_DISCOUNT
     * 
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportItemDiscountData(String brandCode, String dataDate,
	    String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try {
	    log.info("POSExportData.exportItemDiscountData");
	    String actualDataDate = DateUtils.format(currentDate,
		    DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String actualDataDateEnd = DateUtils.format(DateUtils.addDays(
		    currentDate, 1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    if (StringUtils.hasText(dataDateEnd)) {
		actualDataDateEnd = dataDateEnd;
	    }
	    Date transactionDate = DateUtils.parseDate(
		    DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_ITEM_DISCOUNT_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands商品折扣資料下傳....", currentDate, uuid, opUser);
	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate,
		    processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser,
		    brandCode);
	    setIniProperties();
	    String fileName = iniProperties.getProperty("discountFile");
	    String discountOut = iniProperties.getProperty(brandCode
		    + "Islands.discountOut");
	    String[] discountOuts = discountOut.split(",");
	    String[] actualOutPaths = getActualOutPaths(brandCode,
		    actualDataDate, fileName, discountOuts);
	    createT2ItemDiscount(brandCode, actualOutPaths, actualDataDate,
		    actualDataDateEnd);
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE,
		    transactionDate, processName, COMMON_SHOP_MACHINE_CODE,
		    fileName, null, opUser);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO,
		    MessageStatus.ITEM_DISCOUNT_DL_SUCCESS, currentDate, uuid,
		    opUser);
	} catch (Exception ex) {
	    msg = MessageStatus.ITEM_DISCOUNT_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate,
		    uuid, opUser);
	}
    }

    /**
     * T2國際碼
     * 
     * @param brandCode
     * @param targetFiles
     * @param actualDataDate
     * @param actualDataDateEnd
     * @throws Exception
     */
    public void createT2ItemDiscount(String brandCode, String[] targetFiles,
	    String actualDataDate, String actualDataDateEnd) throws Exception {

	log.info("POSExportData.createT2ItemDiscount");
	List<String> records = new ArrayList();
	HashMap searchKey = new HashMap();
	searchKey.put("brandCode", brandCode);
	// searchKey.put("dataDate", actualDataDate);暫時先撈取全部
	ImItemDiscountDAO imItemDiscountDAO = (ImItemDiscountDAO) SpringUtils
		.getApplicationContext().getBean("imItemDiscountDAO");
	List<ImItemDiscount> itemDiscounts = imItemDiscountDAO
		.findItemDiscountListByProperty(searchKey);
	if (itemDiscounts != null && itemDiscounts.size() > 0) {
	    BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
		    .getApplicationContext().getBean("buCommonPhraseService");
	    StringBuffer record = new StringBuffer();
	    for (ImItemDiscount itemDiscount : itemDiscounts) {
		record.delete(0, record.length());
		String isEnable = itemDiscount.getEnable();
		if ("N".equals(isEnable)) {
		    record.append("D");
		} else {
		    record.append("U");
		}
		String vipTypeCode = itemDiscount.getId().getVipTypeCode(); // 卡別
		String identification = "";
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
			.getBuCommonPhraseLine("VIPType", vipTypeCode);
		if (buCommonPhraseLine != null) {
		    identification = buCommonPhraseLine.getAttribute4();
		}
		String itemDiscountType = itemDiscount.getId().getItemDiscountType();
		String beginDate = DateUtils.format(
			itemDiscount.getBeginDate(),
			DateUtils.C_DATE_PATTON_SLASH);
		String endDate = DateUtils.format(itemDiscount.getEndDate(),
			DateUtils.C_DATE_PATTON_SLASH);
		String discount = itemDiscount.getDiscount().toString();

		record.append(CommonUtils.insertCharacterWithLimitedLength(
			vipTypeCode, 2, 2, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			identification, 2, 2, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			itemDiscountType, 2, 2, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			beginDate, 10, 10, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			endDate, 10, 10, CommonUtils.SPACE, "R"));
		record.append(CommonUtils.insertCharacterWithLimitedLength(
			discount, 2, 2, CommonUtils.SPACE, "L"));
		records.add(record.toString());
	    }
	    for (String targetFile : targetFiles) {
		TxtUtil.exportTxt(targetFile, records);
	    }
	}
    }

    public void exportOnHand(String brandCode, String opUser,
	    String customsWarehouseCode, Date startDate, Date endDate) {
	String processName = null;
	String uuid = null;
	String msg = null;
	try {
	    CmDeclarationHeadDAO cmDeclarationHeadDAO = (CmDeclarationHeadDAO) SpringUtils
		    .getApplicationContext().getBean("cmDeclarationHeadDAO");
	    CmDeclarationItemDAO cmDeclarationItemDAO = (CmDeclarationItemDAO) SpringUtils
		    .getApplicationContext().getBean("cmDeclarationItemDAO");
	    CmDeclarationOnHandDAO cmDeclarationOnHandDAO = (CmDeclarationOnHandDAO) SpringUtils
		    .getApplicationContext().getBean("cmDeclarationOnHandDAO");
	    ImOnHandDAO imOnHandDAO = (ImOnHandDAO) SpringUtils
		    .getApplicationContext().getBean("imOnHandDAO");
	    IslandExportDAO islandExportDAO = (IslandExportDAO) SpringUtils
		    .getApplicationContext().getBean("islandExportDAO");
	    ImItemDAO imItemDAO = (ImItemDAO) SpringUtils
		    .getApplicationContext().getBean("imItemDAO");
	    processName = brandCode + "Islands_"
		    + SiPosLogService.PROCESS_NAME_ITEM_ON_HAND_DL;
	    uuid = UUID.randomUUID().toString();
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, "開始執行" + brandCode
			    + "Islands商品庫存資料下傳....", new Date(), uuid, opUser);
	    Date currentDate = new Date();
	    currentDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
	    if (!"T2".equals(brandCode)) {
		return;
	    } else {
		if (startDate == null) {
		    startDate = currentDate;
		}
		if (endDate == null) {
		    endDate = currentDate;
		}
	    }
	    List<CmDeclarationOnHand> cmDeclarationOnHands = cmDeclarationOnHandDAO
	    .getNoLockedOnHand(null, null, null, customsWarehouseCode, brandCode, null, null);
	    log.info("cmDeclarationOnHands.size() = " + cmDeclarationOnHands.size());
	    for (Iterator iterator = cmDeclarationOnHands.iterator(); iterator.hasNext();) {
		CmDeclarationOnHand cmDeclarationOnHand = (CmDeclarationOnHand) iterator.next();
		CmDeclarationItem cmDeclarationItem = cmDeclarationItemDAO
			.findOneCmDeclarationItem(cmDeclarationOnHand
				.getDeclarationNo(), cmDeclarationOnHand
				.getDeclarationSeq());
		if (null == cmDeclarationItem)
		    cmDeclarationItem = new CmDeclarationItem();
		CmDeclarationHead head = cmDeclarationHeadDAO
			.findOneCmDeclaration(cmDeclarationOnHand
				.getDeclarationNo());
		CmDeclarationHead Ohead = cmDeclarationHeadDAO
			.findOneCmDeclaration(cmDeclarationItem.getODeclNo());
		if (null == head)
		    head = new CmDeclarationHead();
		if (null == Ohead)
		    Ohead = new CmDeclarationHead();
		//判斷存倉期限兩年不下報單20140703
		log.info("判斷報單日期  報關單號="+cmDeclarationOnHand.getDeclarationNo()+"項次"+cmDeclarationOnHand.getDeclarationSeq());
	    //Calendar now = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date newDate = new Date();
	    Date OraginalDate =  Ohead.getDeclDate();
	    log.info("OraginalDate =" +OraginalDate );
	    sdf.format(OraginalDate.getTime()); 
	    Date finalDeclDate = DateUtils.addDays(OraginalDate, 730);  
		log.info("finalDeclDate =" +finalDeclDate );
		if(finalDeclDate.before(newDate)){
			log.info("此報單項次超過存倉期限");
		}else{
		ImItem item = imItemDAO.findItem(brandCode, cmDeclarationOnHand
			.getCustomsItemCode());
		islandExportDAO.updateCmOnHand(cmDeclarationOnHand,
			cmDeclarationItem, head, Ohead, item);
//		List<ImOnHand> imOnHands = imOnHandDAO.getNoLockedOnHand("TM",
//			cmDeclarationOnHand.getCustomsItemCode(), "20000",
//			null, brandCode);
//		islandExportDAO.updateImOnHand(imOnHands);
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_INFO, MessageStatus.ON_HAND_DL_SUCCESS,
		    new Date(), uuid, opUser);
	    	}
	    }
	} catch (Exception ex) {
	    msg = MessageStatus.ON_HAND_DL_FAIL + "原因：";
	    log.error(msg + ex.getMessage());
	    SiSystemLogUtils.createSystemLog(processName,
		    MessageStatus.LOG_ERROR, msg + ex.toString(), new Date(),
		    uuid, opUser);
	    //避免發mail時發生錯誤連log都沒寫所以將發信寫在執行緒中(wade)
	    final String processLotNo = uuid;
	    new Thread(){
	    	public void run(){
	    		MailUtils.systemErrorLogSendMail("T2Islands_ITEM_ON_HAND_DL", MessageStatus.ON_HAND_DL_FAIL, processLotNo);
	    	}
	    }.run();
	}
    }
    
    public void exportOnHandNew(String brandCode, String opUser,
    	    String customsWarehouseCode, Date startDate, Date endDate) {
    	String processName = null;
    	String uuid = null;
    	String msg = null;
    	try {
    		CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO = (CmDeclarationOnHandViewDAO) SpringUtils
		    	.getApplicationContext().getBean("cmDeclarationOnHandViewDAO");
    	    IslandExportDAO islandExportDAO = (IslandExportDAO) SpringUtils
    		    .getApplicationContext().getBean("islandExportDAO");
    	    
    	    processName = brandCode + "Islands_"
    		    + SiPosLogService.PROCESS_NAME_ITEM_ON_HAND_DL;
    	    uuid = UUID.randomUUID().toString();
    	    SiSystemLogUtils.createSystemLog(processName,
    		    MessageStatus.LOG_INFO, "開始執行" + brandCode
    			    + "Islands商品庫存資料下傳....", new Date(), uuid, opUser);
    	    Date currentDate = new Date();
    	    currentDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
    	    if (!"T2".equals(brandCode)) {
    		return;
    	    } else {
    		if (startDate == null) {
    		    startDate = currentDate;
    		}
    		if (endDate == null) {
    		    endDate = currentDate;
    		}
    	    }
    	    List<CmDeclarationOnHandView> cmDeclarationOnHandViews = cmDeclarationOnHandViewDAO
    	    .getNoLockedOnHand(null, null, null, customsWarehouseCode, brandCode, null, null);
    	    log.info("cmDeclarationOnHands.size() = " + cmDeclarationOnHandViews.size());
    	    for (Iterator iterator = cmDeclarationOnHandViews.iterator(); iterator.hasNext();) {
    	    	CmDeclarationOnHandView cmDeclarationOnHandView = (CmDeclarationOnHandView) iterator.next();
    		
    		//判斷存倉期限兩年不下報單20140703
    		log.info("判斷報單日期  報關單號="+cmDeclarationOnHandView.getDeclarationNo()+"項次"+cmDeclarationOnHandView.getDeclarationSeq());
    	    //Calendar now = Calendar.getInstance();
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
    	    Date newDate = new Date();
    	    
    	    Date expiryDate = cmDeclarationOnHandView.getExpiryDate();
    	    sdf.format(expiryDate.getTime());
    	    log.info("finalDeclDate =" +expiryDate );
    	    
    		if(expiryDate.before(newDate)){
    			log.info("此報單項次超過存倉期限");
    		}else{

    		islandExportDAO.updateCmOnHand(cmDeclarationOnHandView);
    	    SiSystemLogUtils.createSystemLog(processName,
    		    MessageStatus.LOG_INFO, MessageStatus.ON_HAND_DL_SUCCESS,
    		    new Date(), uuid, opUser);
    	    	}
    	    }
    	} catch (Exception ex) {
    	    msg = MessageStatus.ON_HAND_DL_FAIL + "原因：";
    	    log.error(msg + ex.getMessage());
    	    SiSystemLogUtils.createSystemLog(processName,
    		    MessageStatus.LOG_ERROR, msg + ex.toString(), new Date(),
    		    uuid, opUser);
    	    //避免發mail時發生錯誤連log都沒寫所以將發信寫在執行緒中(wade)
    	    final String processLotNo = uuid;
    	    new Thread(){
    	    	public void run(){
    	    		MailUtils.systemErrorLogSendMail("T2Islands_ITEM_ON_HAND_DL", MessageStatus.ON_HAND_DL_FAIL, processLotNo);
    	    	}
    	    }.run();
    	}
        }
}
