package tw.com.tm.erp.exportdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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

import org.apache.commons.collections.map.LinkedMap;
import tw.com.tm.erp.utils.NumberUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.taipeifubon.util.TfbZip;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionFull;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombine;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionReCombineDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionViewDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImPromotionReCombineMainService;
import tw.com.tm.erp.hbm.service.SiPosLogService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.FtpUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.OldSysMapNewSys;
import tw.com.tm.erp.utils.SiPosLogUtil;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.TxtFormatUtil;
import tw.com.tm.erp.utils.TxtUtil;
import tw.com.tm.erp.utils.dbf.DBFUtils;
import tw.com.tm.erp.utils.dbf.JDBFException;
import tw.com.tm.erp.utils.dbf.JDBField;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;


/**
 * POS 資料匯出
 *
 * @author T02049 生日是 國曆 yy/mm/dd
 */

public class POSExportData {
	private static final Log log = LogFactory.getLog(POSExportData.class);
	private static SimpleDateFormat dateFromat = new SimpleDateFormat("yy/MM/dd");
	private static SimpleDateFormat timeFromat = new SimpleDateFormat("hh:mm");
	private static Properties iniProperties = null;
	private static String classPath = null;
	private static Map<String, JDBField[]> templateFields = new HashMap();
	private static final String COMMON_SHOP_CODE = "All";
	private static final String COMMON_SHOP_MACHINE_CODE = "All";
	public static final String PROCESS_NAME = "ITEM_FLAG_EXPORT";
	public static final String EC_PROCESS_NAME = "EC_EXPORT";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_EXECUTE_DATE = "executeDate";
    private static final String KEY_SUFFIX_DATE = "SUFFIX_DATE";
    private static final String CONFIG_FILE = "../exportdb/DB2txt.properties";

	/**
	 * export pos data
	 *
	 * @param functionSwitch
	 * @param brandCode
	 * @param dataDate
	 * @param dataDateEnd
	 * @param opUser
	 */
	public void doPOSExportData(int functionSwitch, String brandCode, Date dataDate, Date dataDateEnd, String opUser){
		log.info("POSExportData.doPOSExportData");
		String actualDataDate = null;
		String actualDataDateEnd = null;
		
		if(dataDate != null){
			actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		}
		
		if(dataDateEnd != null){
			actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		}
		
		switch (functionSwitch) {
			case 0:
				exportCustomerData(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 1:
				exportEcrsalesData(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 2:
				exportGdsqtyData(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 3:
				exportGoodslstData(brandCode, dataDate, dataDateEnd, opUser);
				break;
			case 4:
				exportProm01Data(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 5:
				exportMovementData(brandCode, dataDate, dataDateEnd, opUser);
				break;
			case 6:
				exportEanCodeData(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 7:
				exportItemDiscountData(brandCode, actualDataDate, actualDataDateEnd, opUser);
				break;
			case 8:
			    exportItemTagData(brandCode, actualDataDate, actualDataDateEnd, opUser);
			    break;
			case 9:
			    exportCombineData(brandCode, dataDate, dataDateEnd, opUser);
			    exportFullData(brandCode, dataDate, dataDateEnd, opUser);
			    break;
			case 10:
				exportECGoodslstData(brandCode, dataDate, dataDateEnd, opUser);
				executeFTPUpload();
			    break;
			case 11:
				exportTotalECGoodslstData(brandCode, dataDate, dataDateEnd, opUser);
				//executeFTPUpload();
			    break; 
			case 12:
				Date currentDate = new Date();
				actualDataDate = DateUtils.format(DateUtils.addDays(currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);	
				actualDataDateEnd = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				exportProm01Data(brandCode, actualDataDate, actualDataDateEnd, opUser);
				//executeFTPUpload();
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
	public void exportMovementData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportMovementData");
			
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_MOVE_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "調撥資料下傳....", currentDate, uuid , opUser);
			setIniProperties();
			String movetopFileName = iniProperties.getProperty("movetopTemp");
			String movedetFileName = iniProperties.getProperty("movedetTemp");
			String movetopTemp = classPath + "/" + movetopFileName;
			String movedetTemp = classPath + "/" + movedetFileName;
			String movetopOut = iniProperties.getProperty(brandCode + ".movetopOut");
			String movetopOuts[] = movetopOut.split(",");
			String movedetOut = iniProperties.getProperty(brandCode + ".movedetOut");
			String movedetOuts[] = movedetOut.split(",");
			Long[] transportIdArray  = createMovementDBF(brandCode, movetopTemp,movedetTemp, movetopOuts , movedetOuts, dataDate, dataDateEnd);
			//========================將下傳的調撥資料標記為Y=====================
			if(transportIdArray != null && transportIdArray.length > 0){
				ImMovementService movementService = (ImMovementService) SpringUtils.getApplicationContext().getBean("imMovementService");
				movementService.updateTransportDataById(transportIdArray, "Y");
			}
			//SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, actualDeliveryDateEnd, processName, COMMON_SHOP_MACHINE_CODE, movetopFileName, movedetFileName, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.MOVE_DL_SUCCESS, currentDate, uuid , opUser);
		}catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.MOVE_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
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
	public void exportCustomerData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportCustomerData");
			String actualDataDate = null;
			if(!"T2".equals(brandCode))
				actualDataDate = DateUtils.format(DateUtils.addDays(currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
			else
				actualDataDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			
			String actualDataDateEnd = actualDataDate;
			if(StringUtils.hasText(dataDate))
				actualDataDate = dataDate;
			
			if(StringUtils.hasText(dataDateEnd))
				actualDataDateEnd = dataDateEnd;
			
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_CUSTOMER_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "客戶資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String customerTemp = null;
			String customerOut = iniProperties.getProperty(brandCode + ".customerOut");
			String[] customerOuts = customerOut.split(",");
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("customerTemp");
				customerTemp = classPath + "/" + fileName;
				createCustomerDBF(brandCode, customerTemp, customerOuts, actualDataDate, actualDataDateEnd);
			}
			else{
				fileName = iniProperties.getProperty("customerFile");
				String[] actualOutPaths = getActualOutPaths(brandCode, actualDataDate, fileName, customerOuts);
				createT2Customer(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
			}

			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.CUSTOMER_DL_SUCCESS, currentDate, uuid , opUser);
		}catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.CUSTOMER_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
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
	public void exportEcrsalesData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportEcrsalesData");
			String actualDataDate = null;
			if(!"T2".equals(brandCode))
				actualDataDate = DateUtils.format(DateUtils.addDays(currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
			else
				actualDataDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			
			String actualDataDateEnd = actualDataDate;
			if(StringUtils.hasText(dataDate))
				actualDataDate = dataDate;
			
			if(StringUtils.hasText(dataDateEnd))
				actualDataDateEnd = dataDateEnd;
			
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_SALES_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "專櫃人員資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String ecrsalesTemp = null;
			String ecrsales = iniProperties.getProperty(brandCode + ".ecrsalesOut");
			String ecrsaless[] = ecrsales.split(",");
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("ecrsalesTemp");
				ecrsalesTemp = classPath + "/" + fileName;
				createEcrsalesDBF(brandCode, ecrsalesTemp, ecrsaless, actualDataDate, actualDataDateEnd);
			}
			else{
				fileName = iniProperties.getProperty("ecrsalesFile");
				String[] actualOutPaths = getActualOutPaths(brandCode, actualDataDate, fileName, ecrsaless);
				createT2Sales(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.SALES_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.SALES_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
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
	public void exportGdsqtyData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try {
			log.info("POSExportData.exportGdsqtyData");
			String actualDataDate = null;
			String actualDataDateEnd = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			/*String actualDataDate = DateUtils.format(DateUtils.addDays(currentDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
			if(StringUtils.hasText(dataDate)){
			        actualDataDate = dataDate;
			}
			if(StringUtils.hasText(dataDateEnd)){
			        actualDataDateEnd = dataDateEnd;
			}*/
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_GOODSQTY_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "商品庫存資料下傳....", currentDate, uuid, opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = iniProperties.getProperty("gdsqtyTemp");
			String gdsqtyTemp = classPath + "/" + fileName;
			String gdsqtyOut = iniProperties.getProperty(brandCode + ".gdsqtyOut");
			String gdsqtyOuts[] = gdsqtyOut.split(",");
			createGdsqtyDBF(brandCode, gdsqtyTemp, gdsqtyOuts, actualDataDate, actualDataDateEnd);
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.GOODSQTY_DL_SUCCESS, currentDate, uuid, opUser);
		} 
		catch (Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.GOODSQTY_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid, opUser);
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
	public void exportGoodslstData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportGoodslstData");
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_GOODSLIST_DL;
			uuid = UUID.randomUUID().toString();
			Date actualDataDate = null;
			if(!"T2".equals(brandCode))
				actualDataDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), -1);
			else
				actualDataDate = DateUtils.getShortDate(currentDate);
			
			Date actualDataDateEnd = actualDataDate;
			if(dataDateEnd != null)
				actualDataDateEnd = dataDateEnd;
			
			if(dataDate != null)
				actualDataDate = dataDate;
			
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "商品主檔下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, actualDataDateEnd, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String goodslstTemp = null;
			String goodslstOut = iniProperties.getProperty(brandCode + ".goodslstOut");
			String goodslstOuts[] = goodslstOut.split(",");
			
			
			
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("goodslstTemp");
				goodslstTemp = classPath + "/" + fileName;
				createGoodslstDBF(brandCode, goodslstTemp, goodslstOuts, actualDataDate, actualDataDateEnd);
			}
			else{
				fileName = iniProperties.getProperty("goodslstFile");
				String[] actualOutPaths = getActualOutPaths(brandCode, DateUtils.format(actualDataDate, DateUtils.C_DATA_PATTON_YYYYMMDD), fileName, goodslstOuts);
				createT2Goodslst(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, actualDataDateEnd, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.GOODSLIST_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.GOODSLIST_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
			
			//下傳商品資料失敗mail,避免發mail時發生錯誤連log都沒寫所以將發信寫在執行緒中(wade)
			final String name = processName;
		    final String processLotNo = uuid;
		    new Thread(){
		    	public void run(){
		    		MailUtils.systemErrorLogSendMail(name, MessageStatus.LOG_ERROR, processLotNo);
		    	}
		    }.run();
		}
	}
	/**
	 * EC商品
	 *
	 * @param brandCode
	 * @param dataDate
	 * @param dataDateEnd
	 * @param opUser
	 */
	public void exportECGoodslstData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportECGoodslstData");
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_EC_GOODSLIST_DL;
			uuid = UUID.randomUUID().toString();
			Date actualDataDate = null;
			if(!"T2".equals(brandCode))
				actualDataDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), -1);//OTHER
			else
				actualDataDate = DateUtils.getShortDate(currentDate);//T2
			
			Date actualDataDateEnd = actualDataDate;
			if(dataDateEnd != null)
				actualDataDateEnd = dataDateEnd;
			
			if(dataDate != null)
				actualDataDate = dataDate;
//LOG紀錄:開始
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "網購商品主檔下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, actualDataDateEnd, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String goodslstTemp = null;
//設定檔:POSExportData.properties
			String goodslstOut = iniProperties.getProperty(brandCode + ".ecGoodslstOut");
			String goodslstOuts[] = goodslstOut.split(",");
			
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("ecGoodslstTemp");
				goodslstTemp = classPath + "/" + fileName;
				//createGoodslstDBF(brandCode, goodslstTemp, goodslstOuts, actualDataDate, actualDataDateEnd);
			}
			else{
				fileName = iniProperties.getProperty("ecGoodslstFile");
//文字檔產出路徑
				String[] actualOutPaths = getActualOutPaths(brandCode, DateUtils.format(actualDataDate, DateUtils.C_DATA_PATTON_YYYYMMDD), fileName, goodslstOuts);
				
				
				Map configMap = getConfig(brandCode,".prom01Config");
//撈下傳清單
				/* createT2Goodslst()	
				 * brandCode				String		品牌
				 * actualOutPaths			String[]	文字檔產出路徑
				 * actualDataDate			DATE		填入起始日期,預設為下傳當天
				 * actualDataDateEnd		DATE		填入結束日期,預設為下傳當天
				 */
				
				
				String dataDateEndString = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
				String dataDateString = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				String pmoActualDataDateEnd = null;
				String finishedPromotionDate = null;
				String isAutoSchedule = "Y";
				for(String ps:actualOutPaths){ 
					log.info("品牌:"+brandCode+"產出路徑:"+ps+"起始日期:"+actualDataDate+"結束日期:"+actualDataDateEnd);
					
				}
				
				if(!"T2".equals(brandCode))
					pmoActualDataDateEnd = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				else
					pmoActualDataDateEnd = DateUtils.format(DateUtils.addDays(currentDate, 1), DateUtils.C_DATA_PATTON_YYYYMMDD);
				
				if(StringUtils.hasText(dataDateEndString)){
					isAutoSchedule = "N";
					pmoActualDataDateEnd = dataDateEndString;
					finishedPromotionDate = dataDateEndString;
				}
				else
					finishedPromotionDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				
				
				log.info("===============createT2Goodslst Start===============");
/**產出下傳清單**/
				createT2ECGoodslst(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd,dataDateString, pmoActualDataDateEnd, finishedPromotionDate, DateUtils.C_DATA_PATTON_YYYYMMDD, isAutoSchedule,configMap);
				log.info("===============createT2Goodslst End===============");
				//log.info("createT2Promotion");

				//createT2Promotion(brandCode, actualOutPaths, dataDateString, pmoActualDataDateEnd, finishedPromotionDate, DateUtils.C_DATA_PATTON_YYYYMMDD, isAutoSchedule,configMap);
			}
//LOG紀錄:完成
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, actualDataDateEnd, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.EC_GOODSLIST_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
//LOG紀錄:失敗
			ex.printStackTrace();
			msg = MessageStatus.EC_GOODSLIST_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
			
			//下傳商品資料失敗mail,避免發mail時發生錯誤連log都沒寫所以將發信寫在執行緒中(wade)
			final String name = processName;
		    final String processLotNo = uuid;
		    new Thread(){
		    	public void run(){
		    		MailUtils.systemErrorLogSendMail(name, MessageStatus.LOG_ERROR, processLotNo);
		    	}
		    }.run();
		}
	}
	/**
	 * EC商品
	 *
	 * @param brandCode
	 * @param dataDate
	 * @param dataDateEnd
	 * @param opUser
	 */
	public void exportTotalECGoodslstData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportECGoodslstData");
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_EC_GOODSLIST_DL;
			uuid = UUID.randomUUID().toString();
			Date actualDataDate = null;
			if(!"T2".equals(brandCode))
				actualDataDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), -1);//OTHER
			else
				actualDataDate = DateUtils.getShortDate(currentDate);//T2
			
			Date actualDataDateEnd = actualDataDate;
			if(dataDateEnd != null)
				actualDataDateEnd = dataDateEnd;
			
			if(dataDate != null)
				actualDataDate = dataDate;
//LOG紀錄:開始
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "網購商品主檔下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, actualDataDateEnd, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String goodslstTemp = null;
//設定檔:POSExportData.properties
			String goodslstOut = iniProperties.getProperty(brandCode + ".ecGoodslstOut");
			String goodslstOuts[] = goodslstOut.split(",");
			
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("ecGoodslstTemp");
				goodslstTemp = classPath + "/" + fileName;
				//createGoodslstDBF(brandCode, goodslstTemp, goodslstOuts, actualDataDate, actualDataDateEnd);
			}
			else{
				fileName = iniProperties.getProperty("ecGoodslstFile");
//文字檔產出路徑
				String[] actualOutPaths = getActualOutPaths(brandCode, DateUtils.format(actualDataDate, DateUtils.C_DATA_PATTON_YYYYMMDD), fileName, goodslstOuts);
				
				
				Map configMap = getConfig(brandCode,".prom01Config");
//撈下傳清單
				/* createT2Goodslst()	
				 * brandCode				String		品牌
				 * actualOutPaths			String[]	文字檔產出路徑
				 * actualDataDate			DATE		填入起始日期,預設為下傳當天
				 * actualDataDateEnd		DATE		填入結束日期,預設為下傳當天
				 */
				
				
				String dataDateEndString = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
				String dataDateString = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				String pmoActualDataDateEnd = null;
				String finishedPromotionDate = null;
				String isAutoSchedule = "Y";
				for(String ps:actualOutPaths){ 
					log.info("品牌:"+brandCode+"產出路徑:"+ps+"起始日期:"+actualDataDate+"結束日期:"+actualDataDateEnd);
					
				}
				
				if(!"T2".equals(brandCode))
					pmoActualDataDateEnd = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				else
					pmoActualDataDateEnd = DateUtils.format(DateUtils.addDays(currentDate, 1), DateUtils.C_DATA_PATTON_YYYYMMDD);
				
				if(StringUtils.hasText(dataDateEndString)){
					isAutoSchedule = "N";
					pmoActualDataDateEnd = dataDateEndString;
					finishedPromotionDate = dataDateEndString;
				}
				else
					finishedPromotionDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				
				
				log.info("===============createT2Goodslst Start===============");
/**產出下傳清單**/
				createT2TotalECGoodslst(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd,dataDateString, pmoActualDataDateEnd, finishedPromotionDate, DateUtils.C_DATA_PATTON_YYYYMMDD, isAutoSchedule,configMap);
				log.info("===============createT2Goodslst End===============");
				//log.info("createT2Promotion");

				//createT2Promotion(brandCode, actualOutPaths, dataDateString, pmoActualDataDateEnd, finishedPromotionDate, DateUtils.C_DATA_PATTON_YYYYMMDD, isAutoSchedule,configMap);
			}
//LOG紀錄:完成
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, actualDataDateEnd, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.EC_GOODSLIST_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
//LOG紀錄:失敗
			ex.printStackTrace();
			msg = MessageStatus.EC_GOODSLIST_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
			
			//下傳商品資料失敗mail,避免發mail時發生錯誤連log都沒寫所以將發信寫在執行緒中(wade)
			final String name = processName;
		    final String processLotNo = uuid;
		    new Thread(){
		    	public void run(){
		    		MailUtils.systemErrorLogSendMail(name, MessageStatus.LOG_ERROR, processLotNo);
		    	}
		    }.run();
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
	public void exportProm01Data(String brandCode, String dataDate, String dataDateEnd, String opUser) {
		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
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
			
			//System.out.println("A1");
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_PROMOTION_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "促銷資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = null;
			String prom01Temp = null;
			//String customerConfig = null;
			String prom01Out = iniProperties.getProperty(brandCode + ".prom01Out");
			String prom01Outs[] = prom01Out.split(",");
			//System.out.println("A2");
			if(!"T2".equals(brandCode)){
				fileName = iniProperties.getProperty("prom01Temp");
				prom01Temp = classPath + "/" + fileName;
				createPromotionDBF(brandCode, prom01Temp, prom01Outs, dataDate, actualDataDateEnd);
			}
			else{
				// 取得轉出檔案的範例檔xxx.DAT
				fileName = iniProperties.getProperty("prom01File");
				String currentDateFormat = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				// 取得所有n個後台的轉出與1個備份的路徑
				String[] actualOutPaths = getActualOutPaths(brandCode, currentDateFormat, fileName, prom01Outs);
				// 取得後台配置檔
				Map configMap = getConfig(brandCode,".prom01Config");
				// 產生促銷檔
				/* createT2Promotion()	
				 * brandCode				String		品牌
				 * actualOutPaths			String[]	文字檔產出路徑
				 * dataDate					DATE		填入起始日期
				 * actualDataDate			DATE		填入起始日期,預設為下傳當天
				 * actualDataDateEnd		DATE		填入結束日期,預設為下傳當天
				 */
				createT2Promotion(brandCode, actualOutPaths, dataDate, actualDataDateEnd, finishedPromotionDate, currentDateFormat, isAutoSchedule,configMap);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.PROMOTION_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.PROMOTION_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
		}
	}

	public TreeMap getPOSExportLabel() throws Exception {
		log.info("POSExportData.getPOSExportLabel");
		setIniProperties();
		TreeMap re = new TreeMap();
		String functionSwitch = iniProperties.getProperty("functionSwitch");
		String functionLabel = iniProperties.getProperty("functionLabel");
		//System.out.println(functionLabel);
		String functionSwitchs[] = StringTools.StringToken(functionSwitch, ",");
		String functionLabels[] = StringTools.StringToken(functionLabel, ",");
		for (int index = 0; index < functionSwitchs.length; index++) {
			//System.out.println(functionLabels[index]);
			re.put(functionSwitchs[index], functionLabels[index]);
		}
		return re;
	}

	private static void setIniProperties() throws IOException {
		log.info("POSExportData.setIniProperties");
		// if (null == iniProperties) {
		URL url = tw.com.tm.erp.exportdb.POSExportData.class.getResource("POSExportData.properties");
		iniProperties = new Properties();
		iniProperties.load(new FileInputStream(url.getFile()));
		classPath = url.getFile().substring(0, url.getFile().lastIndexOf("/"));
		//System.out.println("classPath =" + classPath);
		// }
	}

	public Long[] createMovementDBF(String brandCode, String templateTopFile, String templateDetFile,
			String targetTopFiles[], String targetDetFiles[], Date actualDeliveryDate, Date actualDeliveryDateEnd)
	throws IOException, JDBFException {
		log.info("POSExportData.createMovementDBF");

		/*
		Date actualDeliveryDate = DateUtils.addDays(DateUtils.getShortDate(new Date()), -1);
		Date actualDeliveryDateEnd = actualDeliveryDate;
		if (dataDate != null) {
			actualDeliveryDate = dataDate;
		}
		if (dataDateEnd != null) {
			actualDeliveryDateEnd = dataDateEnd;
		}*/

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
		ImMovementHeadDAO imMovementHeadDAO = (ImMovementHeadDAO) SpringUtils.getApplicationContext().getBean(
		"imMovementHeadDAO");
		List imMovementHeads = null;
		if (actualDeliveryDate != null && actualDeliveryDateEnd != null)
			imMovementHeads = imMovementHeadDAO.findPOSMovementByCondition(conditionMap);
		else
			imMovementHeads = imMovementHeadDAO.findTransportDataForPOS(conditionMap);

		Long[] transportIdArray = null;
		if (imMovementHeads != null && imMovementHeads.size() > 0) {
			transportIdArray = new Long[imMovementHeads.size()];
			for (int i = 0; i < imMovementHeads.size(); i++) {
				Object[] topRecord = new Object[topFields.length];
				ImMovementHead movementHead = (ImMovementHead) imMovementHeads.get(i);
				transportIdArray[i] = movementHead.getHeadId();
				List movementItems = movementHead.getImMovementItems();
				String deliveryWarehouseCode = OldSysMapNewSys.getOldWarehouse(movementHead.getDeliveryWarehouseCode());
				String arrivalWarehouseCode = OldSysMapNewSys.getOldWarehouse(movementHead.getArrivalWarehouseCode());
				/** TODO TW2AD */
				String deliveryDate = DateUtils.formatToTWDate(movementHead.getDeliveryDate(),DateUtils.C_DATE_PATTON_DEFAULT);
				topRecord[0] = movementHead.getOriginalOrderNo();
				topRecord[1] = deliveryWarehouseCode;
				topRecord[2] = arrivalWarehouseCode;
				// 修改轉出日期為八碼 YY-MM-DD，超過100年，只取00 by Weichun 2010.12.24
				topRecord[3] = deliveryDate.length()> 8 ? deliveryDate.substring(1) : deliveryDate;
				topRecord[4] = movementHead.getOriginalOrderTypeCode();
				topRecord[5] = "OK";
				topRecord[6] = "OK";

				if (movementItems != null && movementItems.size() > 0) {
					topRecord[7] = new Integer(movementItems.size()).doubleValue();
					Double deliveryQuantityAmt = 0D;
					for (int j = 0; j < movementItems.size(); j++) {
						Object[] detRecord = new Object[detFields.length];
						ImMovementItem movementItem = (ImMovementItem) movementItems.get(j);
						String itemDeliveryWarehouseCode = OldSysMapNewSys.getOldWarehouse(movementItem
								.getDeliveryWarehouseCode());
						String itemArrivalWarehouseCode = OldSysMapNewSys.getOldWarehouse(movementItem
								.getArrivalWarehouseCode());
						detRecord[0] = movementHead.getOriginalOrderNo();
						detRecord[1] = itemDeliveryWarehouseCode;
						detRecord[2] = itemArrivalWarehouseCode;
						// 修改轉出日期為八碼 YY-MM-DD，超過100年，只取00 by Weichun 2010.12.24
						detRecord[3] = deliveryDate.length()> 8 ? deliveryDate.substring(1) : deliveryDate;
						detRecord[4] = "OK";
						detRecord[5] = 0D;
						detRecord[6] = new Integer(j + 1).doubleValue();
						detRecord[7] = OldSysMapNewSys.getOldItemCode(brandCode, movementItem.getItemCode()).getItemCode();
						Double deliveryQuantity = movementItem.getDeliveryQuantity();
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

				topRecord[9] = trimString(movementHead.getRemark1(), topFields[9].getLength());
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
	public Date createCustomerDBF(String brandCode, String templateFile, String targetFiles[], String actualDataDate, String actualDataDateEnd) throws IOException, JDBFException {
		log.info("POSExportData.createCustomerDBF");
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
		BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils.getApplicationContext().getBean("buAddressBookDAO");
		BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO) SpringUtils.getApplicationContext().getBean("buCommonPhraseLineDAO");
		List<Object[]> results = buAddressBookDAO.findCustomerListNoLimit(searchKey);
		if(results != null){
			for (Object[] result : results) {
				Object[] record = new Object[fields.length];
				BuAddressBook buAddressBook = (BuAddressBook) result[0];
				BuCustomer buCustomer = (BuCustomer) result[1];
				BuCustomerId id = buCustomer.getId();
				String isEnable = buCustomer.getEnable();
				String vipTypeCode = buCustomer.getVipTypeCode();
				String jobType = buCustomer.getCategory01();
				BuCommonPhraseLineId commonPhraseLineId = new BuCommonPhraseLineId(
						new BuCommonPhraseHead("JobType"), jobType);
				BuCommonPhraseLine commonPhraseLine = buCommonPhraseLineDAO.findById(commonPhraseLineId);

				if("Y".equals(isEnable)){
					record[0] = "U";
				}else{
					record[0] = "D";
				}
				record[1] = id.getCustomerCode();
				record[2] = trimString(buAddressBook.getChineseName(), fields[2].getLength());

				//如果是員工就把會員個人資訊給清空
				if(vipTypeCode.indexOf("EMP") == -1){
					record[3] = buAddressBook.getIdentityCode();
					/**TODO TW2AD*/
					if ((null != buAddressBook.getBirthdayYear()) && (null != buAddressBook.getBirthdayYear())
							&& (null != buAddressBook.getBirthdayYear())) {
						// 確認民國年yy-MM-dd by Weichun 2010.12.24
						record[4] = dateFormat(buAddressBook.getBirthdayYear().intValue(), buAddressBook.getBirthdayMonth().intValue(), buAddressBook.getBirthdayDay().intValue());
						record[4] = String.valueOf(record[4]).length() > 8 ? String.valueOf(record[4]).substring(1) : record[4];
						//record[4] = dateFormatYYYYMMDD(buAddressBook.getBirthdayYear().intValue(), buAddressBook.getBirthdayMonth().intValue(), buAddressBook.getBirthdayDay().intValue());
					} else {
						record[4] = "";
					}

					record[6] = trimString(buAddressBook.getTel2(), fields[6].getLength() );
					record[7] = trimString(buAddressBook.getMobilePhone(), fields[7].getLength() );
					record[8] = trimString(buAddressBook.getEMail(), fields[8].getLength() );
					//=======================職業類別=======================
					record[10] = "";
					if(commonPhraseLine != null){
						record[10] = trimString(commonPhraseLine.getName(), fields[10].getLength());
					}
					//========================年收入========================
					record[11] = 0D;
					String yearIncomeType = buCustomer.getCategory03();
					if("1".equals(yearIncomeType)){
						record[11] = 50D;
					}else if("2".equals(yearIncomeType)){
						record[11] = 100D;
					}else if("3".equals(yearIncomeType)){
						record[11] = 150D;
					}else if("4".equals(yearIncomeType)){
						record[11] = 200D;
					}
					//=====================================================
					record[12] = trimString(buAddressBook.getCompanyName(), fields[12].getLength());
					record[13] = "";
					record[14] = trimString(buAddressBook.getTel1(), fields[14].getLength());
					record[15] = trimString(buAddressBook.getFax1(), fields[15].getLength());
					//==================通訊地址=============================
					record[16] = trimString(StringTools.replaceNullToSpace(buAddressBook.getCity()) + StringTools.replaceNullToSpace(buAddressBook.getArea())
							+ StringTools.replaceNullToSpace(buAddressBook.getAddress()), fields[16].getLength());
					//==================送貨地址=============================
					record[17] = trimString(StringTools.replaceNullToSpace(buCustomer.getCity3()) + StringTools.replaceNullToSpace(buCustomer.getArea3())
							+ StringTools.replaceNullToSpace(buCustomer.getAddress3()), fields[17].getLength());
				}else{
					record[3] = "";
					record[4] = "";
					record[6] = "";
					record[7] = "";
					record[8] = "";
					record[10] = "";
					record[11] = 0D;
					record[12] = "";
					record[13] = "";
					record[14] = "";
					record[15] = "";
					record[16] = "";
					record[17] = "";
				}


				//==================性別=============================
				record[5] = trimString(buAddressBook.getGender(), fields[5].getLength() );

				//==================特惠價別=============================
				record[18] = "0";
				//String vipTypeCode = buCustomer.getVipTypeCode();
				commonPhraseLineId = new BuCommonPhraseLineId(
						new BuCommonPhraseHead("VIPType"), vipTypeCode);
				commonPhraseLine = buCommonPhraseLineDAO.findById(commonPhraseLineId);
				if(commonPhraseLine != null){
					record[18] = commonPhraseLine.getAttribute3();
				}
				record[9] = record[18];
				//===================會員起始日、會員到期日=================
				if (buCustomer.getVipStartDate() != null)
					/**TODO TW2AD*/
					//確認為民國年yy-MM-dd by Weichun 2010.12.24
					record[19] = dateFormat(buCustomer.getVipStartDate()).length() > 8 ? dateFormat(buCustomer.getVipStartDate()).substring(1) : dateFormat(buCustomer.getVipStartDate());
					//record[19] = dateFormatYYYYMMDD(buCustomer.getVipStartDate());
					else
						record[19] = "";

				if (buCustomer.getVipEndDate() != null)
					/**TODO TW2AD*/
					//確認為民國年yy-MM-dd by Weichun 2010.12.24
					record[20] = dateFormat(buCustomer.getVipEndDate()).length() > 8 ? dateFormat(buCustomer.getVipEndDate()).substring(1) : dateFormat(buCustomer.getVipEndDate());
					else
						record[20] = "";
				//=====================申請櫃別===========================
				record[21] = "";
				String applyShopCode = buCustomer.getCategory07();
				String oldShopCode = OldSysMapNewSys.getOldShopCode(applyShopCode);
				if(oldShopCode != null){
					record[21] = trimString(oldShopCode, fields[21].getLength());
				}
				//====================鞋子、衣服尺寸=========================
				record[22] = trimString(buCustomer.getCategory08(), fields[22].getLength());
				record[23] = trimString(buCustomer.getCategory09(), fields[23].getLength());
				//========================================================
				record[24] = "";
				record[25] = "";
				record[26] = "";
				record[27] = "";
				//====================第一次消費金額=========================
				record[28] = trimString(buCustomer.getCategory10(), fields[28].getLength());
				//========================================================
				record[29] = "";
				record[30] = trimString(buCustomer.getCategory14(), fields[30].getLength());
				record[31] = trimString(buCustomer.getCategory11(), fields[31].getLength());
				record[32] = trimString(buCustomer.getCategory12(), fields[32].getLength());
				record[33] = trimString(buCustomer.getCategory13(), fields[33].getLength());
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
		//產生Exception的檔案跳過以免影響其他檔案(wade)
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
			try{
			    DBFUtils.writeDBF(targetFiles[i], records, fields);
			}
			catch(Exception e){
			    if(i < targetFiles.length) continue;
			}
		    }
		}
		return DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
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
	public void createEcrsalesDBF(String brandCode, String templateFile, String targetFiles[], String dataDate, String dataDateEnd) throws IOException, JDBFException {
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
		BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils.getApplicationContext().getBean("buAddressBookDAO");
		List<Object[]> results = buAddressBookDAO.findShopEmployeeList(searchKey);
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
		
		//產生Exception的檔案跳過以免影響其他檔案(wade)
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
			try{
			    DBFUtils.writeDBF(targetFiles[i], records, fields);
			}
			catch(Exception e){
			    if(i < targetFiles.length) continue;
			}
		    }
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
	public void createGdsqtyDBF(String brandCode, String templateFile, String targetFiles[], String actualDataDate, String actualDataDateEnd) throws IOException, JDBFException {
		log.info("POSExportData.createGdsqtyDBF");
		String[] unSearchCodeArray = null;
		if("T1BS".equals(brandCode)){
			unSearchCodeArray = new String[]{"00"};
		}else if("T1CO".equals(brandCode)){
			unSearchCodeArray = new String[]{"00", "A9", "AA"};
		}
		StringBuffer assembly = new StringBuffer("");
		if(unSearchCodeArray != null){
			for(int i = 0; i < unSearchCodeArray.length; i++){
				assembly.append("'");
				assembly.append(brandCode);
				assembly.append(unSearchCodeArray[i]);
				assembly.append("'");
				if(i < unSearchCodeArray.length - 1){
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
		ImItemOnHandViewDAO imItemOnHandViewDAO = (ImItemOnHandViewDAO) SpringUtils.getApplicationContext().getBean("imItemOnHandViewDAO");
		List<Object[]> results = imItemOnHandViewDAO.getShopItemOnHandByCondition(brandCode, actualDataDate, actualDataDateEnd, "Y", assembly.toString());
		if(results != null){
			for (Object[] result : results) {
				Object[] record = new Object[fields.length];
				String itemCode = OldSysMapNewSys.getOldItemCode(brandCode, (String)result[0]).getItemCode();
				String warehouseCode = (String)result[1];
				BigDecimal onHand = (BigDecimal)result[2];
				Double currentOnHand = 0D;
				if(onHand != null){
					currentOnHand = onHand.doubleValue();
				}
				record[0] = itemCode;
				record[1] = warehouseCode;
				record[2] = currentOnHand;
				records.add(record);
			}
			Object[] lastRecord = new Object[fields.length];
			lastRecord[0] = "ZZZZLASTDATE";
			// 確認民國年的日期字串為yy-MM-dd by Weichun 2010.12.24
			lastRecord[1] = DateUtils.formatToTWDate(DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd), DateUtils.C_DATE_PATTON_DEFAULT);
			lastRecord[1] = String.valueOf(lastRecord[1]).length() > 8 ? String.valueOf(lastRecord[1]).substring(1) : lastRecord[1];
			lastRecord[2] = 0D;
			records.add(lastRecord);
		}
		//產生Exception的檔案跳過以免影響其他檔案(wade)
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
			try{
			    DBFUtils.writeDBF(targetFiles[i], records, fields);
			}
			catch(Exception e){
			    if(i < targetFiles.length) continue;
			}
		    }
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
	public void createGoodslstDBF(String brandCode, String templateFile, String targetFiles[], Date dataDate, Date dataDateEnd) throws IOException, JDBFException {
		log.info("POSExportData.createGoodslstDBF");
		String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd), 1);
		JDBField[] fields = templateFields.get(templateFile);
		if (null == fields) {
			fields = DBFUtils.getJDBField(templateFile);
			templateFields.put(templateFile, fields);
		}
		String priceType = "1";
		ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean(
		"imItemPriceDAO");
		List<Object[]> records = new ArrayList();
		List<Object[]> results = imItemPriceDAO.getItemPriceByCondition(brandCode, priceType, beginDate, actualDataDate, actualDataDateEnd, "Y", "Y");
		if(results != null){
			for (Object[] result : results) {
				String itemCode = (String)result[1];
				//  防止超出十四碼問題
				if(itemCode.length() < 14 ){
					String itemName = (String)result[2];
					String CLA1NO = (String)result[10];
					String CLA2NO = (String)result[11];
					String CLA3NO = (String)result[12];
					String isServiceItem = (String)result[8];
					String salesUnit = (String)result[5];
					String taxMode = "Y";
					Double unitPrice = ((BigDecimal)result[6]).doubleValue();
					//VIP會員
					String vipDiscount = (String)result[13];
					Double PRICE_R1 = unitPrice;
					if(!"T1BS".equals(brandCode) && !"N".equals(vipDiscount)){
						ImPromotion vp = getVIPPromotion("VIPType", brandCode + "VIP", brandCode);
						PRICE_R1 = getPromotionPrice(unitPrice, itemCode, vp);
					}

					/* 生日
			        ImPromotion bp = getVIPPromotion("BRDType", brandCode + "BRD", brandCode);
			        Double PRICE_R2 = getPromotionPrice(unitPrice, itemCode, bp);*/
					Object[] record = new Object[fields.length];
					record[0] = "U";
					record[1] = OldSysMapNewSys.getOldItemCode(brandCode, itemCode).getItemCode();
					record[2] = "";
					record[3] = itemName;
					record[4] = brandCode;
					record[5] = trimString(CLA1NO, fields[5].getLength());
					record[6] = trimString(CLA2NO, fields[6].getLength());
					record[7] = trimString(CLA3NO, fields[7].getLength());
					if("Y".equals(isServiceItem)){
						record[8] = true;
					}else{
						record[8] = false;
					}
					record[9] = trimString(salesUnit, fields[9].getLength());
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
		}
		//產生Exception的檔案跳過以免影響其他檔案(wade)
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
			try{
			    DBFUtils.writeDBF(targetFiles[i], records, fields);
			}
			catch(Exception e){
			    if(i < targetFiles.length) continue;
			}
		    }
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
	public void createPromotionDBF(String brandCode, String templateFile, String targetFiles[], String dataDate, String actualDataDateEnd) throws IOException, JDBFException, Exception {
		log.info("POSExportData.createPromotionDBF");
		/*String actualDataDateEnd = DateUtils.format(DateUtils.addDays(new Date(), -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
		if(StringUtils.hasText(dataDateEnd)){
		        actualDataDateEnd = dataDateEnd;
		}*/
		JDBField[] fields = templateFields.get(templateFile);
		if (null == fields) {
			fields = DBFUtils.getJDBField(templateFile);
			templateFields.put(templateFile, fields);
		}
		TreeMap actualPromotionItemMap = new TreeMap();
		ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
		//==============================取得isAllShop不為Y的promotion======================
		TreeMap shopPromotionItemMap = new TreeMap();
		List shopPromotionItems = imPromotionViewDAO.getPromotionItemInfo(brandCode, "1", actualDataDateEnd, "N");
		log.info("shopPromotionItems.size() = " + shopPromotionItems.size());
		if(shopPromotionItems != null){
			for(int i = 0; i < shopPromotionItems.size(); i++){
				Object[] promotionInfo = (Object[])shopPromotionItems.get(i);
				filterPromotionItem(shopPromotionItemMap, promotionInfo);
			}
		}
		//==============================取得isAllShop為Y的promotion========================
		TreeMap promotionItemMap = new TreeMap();
		List promotionItems = imPromotionViewDAO.getPromotionItemInfo(brandCode, "1", actualDataDateEnd, "Y");
		log.info("promotionItems.size() = " + promotionItems.size());
		if(promotionItems != null){
			for(int i = 0; i < promotionItems.size(); i++){
				Object[] promotionInfo = (Object[])promotionItems.get(i);
				filterPromotionItem(promotionItemMap, promotionInfo);
			}
		}
		//==============================取得比對後的promotion item========================
		getActualExportPromotionItem(brandCode, actualPromotionItemMap, shopPromotionItemMap, promotionItemMap);
		Set set = actualPromotionItemMap.entrySet();
		Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		List<Object[]> records = new ArrayList();
		for (Map.Entry entry : promotionItemEntry) {
			ImPromotionView promotionView = (ImPromotionView)entry.getValue();
			String tempShopCode = promotionView.getShopCode();
			if(StringUtils.hasText(tempShopCode)){
				tempShopCode = OldSysMapNewSys.getOldShopCode(tempShopCode);
			}
			Date tempBeginDate = promotionView.getBeginDate();
			/**TODO TW2AD*/
			// 修正日期字串為民國年格式yy-MM-dd by Weichun 2010.12.24
			String actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATE_PATTON_DEFAULT);
			actualBeginDate = actualBeginDate.length() > 8 ? actualBeginDate.substring(1) : actualBeginDate;
			Date tempEndDate = promotionView.getEndDate();
			/**TODO TW2AD*/
			// 修正日期字串為民國年格式yy-MM-dd by Weichun 2010.12.24
			String actualEndDate = DateUtils.formatToTWDate(tempEndDate, DateUtils.C_DATE_PATTON_DEFAULT);
			actualEndDate = actualEndDate.length() > 8 ? actualEndDate.substring(1) : actualEndDate;
			Double unitPrice = promotionView.getUnitPrice();
			String discountType = promotionView.getDiscountType();
			Double discount = promotionView.getDiscount();
			Double promotionPrice = unitPrice;
			if(StringUtils.hasText(discountType) && discount != null){
				log.info("discountType = " + discountType);
				log.info("promotionPrice = " + promotionPrice);
				log.info("discount = " + discount);

				if("1".equals(discountType)){
					promotionPrice = promotionPrice - discount;
				}else if("2".equals(discountType)){
					promotionPrice = promotionPrice * (100 - discount) / 100;
				}
				if(promotionPrice < 0D){
					promotionPrice = 0D;
				}
			}

			//防止超出十四碼問題
			if(promotionView.getItemCode().length() < 14){
				Object[] record = new Object[fields.length];
				record[0] = tempShopCode;
				record[1] = actualBeginDate;
				record[2] = "00:00";
				record[3] = actualEndDate;
				record[4] = "23:59";
				record[5] = "";
				record[6] = "";
				record[7] = OldSysMapNewSys.getOldItemCode(brandCode, promotionView.getItemCode()).getItemCode();
				record[8] = promotionPrice;
				record[9] = promotionPrice;
				record[10] = promotionPrice;
				record[11] = promotionPrice;
				records.add(record);
			}
		}
		//==============================================================================
		//產生Exception的檔案跳過以免影響其他檔案(wade)
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
			try{
			    DBFUtils.writeDBF(targetFiles[i], records, fields);
			}
			catch(Exception e){
			    if(i < targetFiles.length) continue;
			}
		    }
		}
	}

	private Double calculatePromotion(ImPromotion pm, String discountType, Double unitPrice) {
		log.info("POSExportData.calculatePromotion discountType=" + discountType + ",unitPrice" + unitPrice);
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
		return dateFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
	}

	private String dateFormatYYYYMMDD(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return dateFormatYYYYMMDD(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
	}

	private String dateFormat(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year - 1911, month - 1, day);
		return dateFromat.format(c.getTime());
	}

	private String dateFormatYYYYMMDD(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return dateFromat.format(c.getTime());
	}

	private ImPromotion getVIPPromotion(String headCode, String lineCode, String brandCode) {
		log.info("POSExportData.getVIPPromotion headCode=" + headCode + ",lineCode=" + lineCode + ",brandCode=" + brandCode);
		ImPromotion imPromotion = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
		"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine(headCode, lineCode);
		if (null != buCommonPhraseLine) {
			String promotionCode = buCommonPhraseLine.getAttribute2();
			ImPromotionDAO imPromotionDAO = (ImPromotionDAO) SpringUtils.getApplicationContext().getBean("imPromotionDAO");
			imPromotion = imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
		}
		return imPromotion;
	}

	private Double getPromotionPrice(Double unitPrice, String itemCode, ImPromotion imPromotion) {
		log.info("POSExportData.getPromotionPrice unitPrice=" + unitPrice + ",itemCode=" + itemCode);
		Double re = new Double(0);
		// 0.HEAD DISCOUNT 比例
		if (null != imPromotion) {
			if (null != imPromotion.getDiscount()) {
				re = unitPrice * (100L - imPromotion.getDiscount()) / 100;
			} else {
				List<ImPromotionItem> imPromotionItems = imPromotion.getImPromotionItems();
				if (null != imPromotionItems) {
					for (ImPromotionItem imPromotionItem : imPromotionItems) {
						// 1.抓ITEM
						if ("itemCode".equalsIgnoreCase(imPromotionItem.getItemCode())) {
							// 2.type = 1 金額 / 2 比例
							String type = imPromotionItem.getDiscountType();
							if ("1".equals(type)) {
								re = unitPrice - imPromotionItem.getDiscount();
							} else if ("2".equals(type)) {
								re = unitPrice * (100D - imPromotionItem.getDiscount()) / 100;
							}
							break;
						}
					}
				}else{
					re = unitPrice;
				}
			}
		}else{
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
		POSExportData.templateFields = templateFields;
	}

	private void filterPromotionItem(TreeMap map, Object[] promotionItemInfo){

		String brandCode = (String)promotionItemInfo[0];
		String promotionCode = (String)promotionItemInfo[1];
		String shopCode = (String)promotionItemInfo[2];
		if(shopCode == null){
			shopCode = "";
		}
		Date beginDate = (Date)promotionItemInfo[3];
		Date endDate = (Date)promotionItemInfo[4];
		String itemCode = (String)promotionItemInfo[5];
		String discountType = (String)promotionItemInfo[6];
		Double discount = ((BigDecimal)promotionItemInfo[7]).doubleValue();
		Double unitPrice = ((BigDecimal)promotionItemInfo[8]).doubleValue();

		//updated by anber 2010.12.29 change lastUpdateDate type to String
		//Date lastUpdateDate = (Date)promotionItemInfo[9];
//		java.sql.Date lastUpdateDate = (java.sql.Date)promotionItemInfo[9];
		String updateDateString = (String)promotionItemInfo[9];
		log.info("ORIG promotionItemInfo[9] = " + promotionItemInfo[9]);
		Date lastUpdateDate = DateUtils.parseDate(DateUtils.C_TIME_PATTON_DEFAULT, updateDateString);

		//===============set promotionView bean================
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
//		log.info("discountType = " + discountType);
//		log.info("discount = " + discount);
//		log.info("unitPrice = " + unitPrice);
		//======================================================
		String key = itemCode + "#" + shopCode;
//		log.info("itemCode # shopCode = " + itemCode + "#" + shopCode);
		ImPromotionView tempPromotionView = (ImPromotionView)map.get(key);
		if(tempPromotionView == null)
			map.put(key, promotionView);
		else{
			Date tempBeginDate = tempPromotionView.getBeginDate();
			Date tempLastUpdateDate = tempPromotionView.getLastUpdateDate();
			if(beginDate.after(tempBeginDate)){
				map.put(key, promotionView);
			}else if(beginDate.getTime() == tempBeginDate.getTime()){
				//活動日期相同時，依據更新日期作比對
				if(lastUpdateDate != null && tempLastUpdateDate != null){
					if(lastUpdateDate.after(tempLastUpdateDate)){
						map.put(key, promotionView);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param brandCode
	 * @param actualPromotionItemMap 放置真正促銷的map
	 * @param shopPromotionItemMap	 單一專櫃來源
	 * @param promotionItemMap	全部專櫃來源
	 * @throws Exception
	 */
	private void getActualExportPromotionItem(String brandCode, TreeMap actualPromotionItemMap,
			TreeMap shopPromotionItemMap, TreeMap promotionItemMap) throws Exception{

		TreeMap tempMap = new TreeMap();
		//=================取得此brand下啟用中的專櫃=========================
		BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext().getBean("buShopDAO");
		List buShopInfos = buShopDAO.findShopByBrandAndEnable(brandCode, "Y");
		List<String> brandShops = new ArrayList();
		if(buShopInfos != null){
			for(int i = 0; i < buShopInfos.size(); i++){
				BuShop buShop = (BuShop)buShopInfos.get(i);
				brandShops.add(buShop.getShopCode());
			}
		}
		Set set = shopPromotionItemMap.entrySet();
		Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		for (Map.Entry entry : promotionItemEntry) {
			String key = (String) entry.getKey();	// 品號#專櫃
			//===================單一專櫃促銷=====================
			ImPromotionView shopPromotionView = (ImPromotionView)entry.getValue();
			Date ShopBeginDate = shopPromotionView.getBeginDate();
			Date ShopLastUpdateDate = shopPromotionView.getLastUpdateDate();
			String[] keyArray = StringTools.StringToken(key, "#");
			String searchKey = keyArray[0] + "#";	// 品號#
			//====================全專櫃促銷======================
			ImPromotionView promotionView = (ImPromotionView)promotionItemMap.get(searchKey);
			//==================================================
			if(promotionView == null){ // 若品號# 沒有在全部專櫃找到則放置 實際促銷map
				actualPromotionItemMap.put(key, shopPromotionView);
			}else{
				Date beginDate = promotionView.getBeginDate();
				Date lastUpdateDate = promotionView.getLastUpdateDate();
				String isAllShop = "Y";
				if(ShopBeginDate.after(beginDate)){ // 單一專櫃的促銷起始日大於全部專櫃的起始日 
					actualPromotionItemMap.put(key, shopPromotionView);
					isAllShop = "N";
				}else if(ShopBeginDate.getTime() == beginDate.getTime()){	// 相等
					//活動日期相同時，依據更新日期作比對
					if(ShopLastUpdateDate != null && lastUpdateDate != null){
						if(ShopLastUpdateDate.after(lastUpdateDate)){
							actualPromotionItemMap.put(key, shopPromotionView);
							isAllShop = "N";
						}else if(ShopLastUpdateDate.getTime() == lastUpdateDate.getTime()){
							actualPromotionItemMap.put(key, shopPromotionView);
							isAllShop = "N";
						}
					}
				}
				//===============自動新增出其他專櫃促銷資料=======================
				if("N".equals(isAllShop)){
					tempMap.put(searchKey, searchKey);//此map中的promotionItem不處理
					for(int i = 0; i < brandShops.size(); i++){
						String tempShop = brandShops.get(i);
						if(!keyArray[1].equals(tempShop) ){ // 單一專櫃不等於掃出的專櫃，因為當全部品牌其一專櫃不等於單一專櫃的專櫃時就會覆蓋　如brandShops有T1CO24單一專櫃有T1CO24其他的就會蓋到成全部促銷
							if(!actualPromotionItemMap.containsKey(searchKey + tempShop)){ // 且不存在actualPromotionItemMap的key才不覆蓋, 因為已存在的是單一專櫃用   20110217 david
								ImPromotionView newPromotionView = new ImPromotionView();
								BeanUtils.copyProperties(promotionView, newPromotionView);
								newPromotionView.setShopCode(tempShop);
								actualPromotionItemMap.put(searchKey + tempShop, newPromotionView);
							}
						}
					}
				}
			}
		}
		//=========================處理全專櫃的promotion==========================
		Set allShopSet = promotionItemMap.entrySet();
		Map.Entry[] allShopPromotionItemEntry = (Map.Entry[]) allShopSet.toArray(new Map.Entry[allShopSet.size()]);
		for (Map.Entry entry : allShopPromotionItemEntry) {
			String key = (String) entry.getKey();
			ImPromotionView promotionView = (ImPromotionView)entry.getValue();
			if(tempMap.get(key) == null){ // 若單一專櫃查不到則將全部專櫃放置實際促銷MAP
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
	public void createT2Customer(String brandCode, String[] targetFiles, String actualDataDate, String actualDataDateEnd)
	throws Exception {

		log.info("POSExportData.createT2Customer");
		List<String> records = new ArrayList();
		HashMap searchKey = new HashMap();
		searchKey.put("brandCode", brandCode);
		searchKey.put("dataDate", actualDataDate);
		searchKey.put("dataDateEnd", actualDataDateEnd);
		BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils.getApplicationContext().getBean("buAddressBookDAO");
		List<Object[]> results = buAddressBookDAO.findCustomerListNoLimit(searchKey);
		if(results != null && results.size() > 0){
			BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
			StringBuffer record = new StringBuffer();
			for (Object[] result : results) {
				record.delete(0, record.length());
				BuAddressBook buAddressBook = (BuAddressBook) result[0];
				BuCustomer buCustomer = (BuCustomer) result[1];
				BuCustomerId id = buCustomer.getId();
				String isEnable = buCustomer.getEnable();
				if("Y".equals(isEnable) || "E".equals(isEnable)){
					record.append("U");
				}else{
					record.append("D");
				}
				String customerCode = id.getCustomerCode();
				String vipTypeCode = buCustomer.getVipTypeCode();
				if("11".equals(vipTypeCode) || "21".equals(vipTypeCode) || "31".equals(vipTypeCode) || "51".equals(vipTypeCode) || "98".equals(vipTypeCode)){
					customerCode = ":" + CommonUtils.insertCharacterWithLimitedLength(customerCode, 9, 9, CommonUtils.SPACE, "R");
				}else{
					customerCode = CommonUtils.insertCharacterWithLimitedLength(customerCode, 10, 10, CommonUtils.SPACE, "R");
				}
				//==================取得VipTypeCode的身份=====================
				String identification = "";
				BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("VIPType", vipTypeCode);
				if(buCommonPhraseLine != null){
					identification = buCommonPhraseLine.getAttribute4();
				}

				record.append(customerCode);
				record.append(CommonUtils.insertCharacterWithLimitedLength(buAddressBook.getChineseName(), 8, 8, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(vipTypeCode, 2, 2, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(identification, 2, 2, CommonUtils.SPACE, "R"));
				records.add(record.toString());
			}
			//產生Exception的檔案跳過以免影響其他檔案(wade)
			if(targetFiles != null && targetFiles.length > 0){
			    for(int i=0; i<targetFiles.length; i++){	
				try{
				    TxtUtil.exportTxt(targetFiles[i], records);
				}
				catch(Exception e){
				    if(i < targetFiles.length) continue;
				}
			    }
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
	public void createT2Sales(String brandCode, String[] targetFiles, String actualDataDate, String actualDataDateEnd)
	throws Exception {

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

		BuAddressBookDAO buAddressBookDAO = (BuAddressBookDAO) SpringUtils.getApplicationContext().getBean("buAddressBookDAO");
		List<Object[]> results = buAddressBookDAO.findEmployeeByCondition(searchKey);

		if(results != null && results.size() > 0){
			StringBuffer record = new StringBuffer();
			for (Object[] result : results) {
				record.delete(0, record.length());
				BuAddressBook buAddressBook = (BuAddressBook) result[0];
				BuEmployee buEmployee = (BuEmployee) result[1];
				Date leaveDate = buEmployee.getLeaveDate();

				if(leaveDate == null){
					record.append("U");
				}else{
					Calendar leaveDateCal = Calendar.getInstance();
					leaveDateCal.setTime(leaveDate);//set your date object1
					leaveDateCal.set(Calendar.HOUR_OF_DAY, 0);
					leaveDateCal.set(Calendar.MINUTE, 0);
					leaveDateCal.set(Calendar.SECOND, 0);

					if(leaveDateCal.getTime().before(cal1.getTime()) || leaveDateCal.getTime().equals(cal1.getTime()) ){ // 離職日小於或等於今日
						record.append("D");
					}else{
						record.append("U");
					}
				}
				record.append(CommonUtils.insertCharacterWithLimitedLength(buEmployee.getEmployeeCode(), 8, 8, CommonUtils.SPACE, "R"));
				//MACO 2016/11/04 解決編碼問題，下傳抓取CHINESE_NAME_BAK2
				record.append(CommonUtils.insertCharacterWithLimitedLength(buAddressBook.getChineseName(), 8, 15, CommonUtils.SPACE, "R"));
				records.add(record.toString());
			}
			//產生Exception的檔案跳過以免影響其他檔案(wade)
			if(targetFiles != null && targetFiles.length > 0){
			    for(int i=0; i<targetFiles.length; i++){	
					try{
				    	TxtUtil.exportTxt(targetFiles[i], records);
					}
					catch(Exception e){
				    	if(i < targetFiles.length) continue;
					}
			    }
			}
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
	public void exportEanCodeData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportEanCodeData");
			String actualDataDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String actualDataDateEnd = actualDataDate;
			if(StringUtils.hasText(dataDate)){
				actualDataDate = dataDate;
			}
			if(StringUtils.hasText(dataDateEnd)){
				actualDataDateEnd = dataDateEnd;
			}
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_EANCODE_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "國際碼資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = iniProperties.getProperty("eanstoreFile");
			String eanstoreOut = iniProperties.getProperty(brandCode + ".eanstoreOut");
			String[] eanstoreOuts = eanstoreOut.split(",");
			if("T2".equals(brandCode)){
				String[] actualOutPaths = getActualOutPaths(brandCode, actualDataDate, fileName, eanstoreOuts);
				createT2EanCode(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.EANCODE_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.EANCODE_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
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
	public void createT2EanCode(String brandCode, String[] targetFiles, String actualDataDate, String actualDataDateEnd)
	throws Exception {

		log.info("POSExportData.createT2EanCode");
		List<String> records = new ArrayList();
		HashMap searchKey = new HashMap();
		searchKey.put("brandCode", brandCode);
		searchKey.put("dataDate", actualDataDate);
		searchKey.put("dataDateEnd", actualDataDateEnd);
		ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) SpringUtils.getApplicationContext().getBean("imItemEancodeDAO");
		List<ImItemEancode> itemEancodes = imItemEancodeDAO.findEanCodeListByProperty(searchKey);
		if(itemEancodes != null && itemEancodes.size() > 0){
			StringBuffer record = new StringBuffer();
			for (ImItemEancode itemEancode : itemEancodes) {
				record.delete(0, record.length());
				String isEnable = itemEancode.getEnable();
				if("Y".equals(isEnable))
					record.append("U");
				else
					record.append("D");
				
				record.append(CommonUtils.insertCharacterWithLimitedLength( (itemEancode.getEanCode()).trim() , 20, 20, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemEancode.getItemCode(), 20, 20, CommonUtils.SPACE, "R"));
				records.add(record.toString());
			}
			//產生Exception的檔案跳過以免影響其他檔案(wade)
			if(targetFiles != null && targetFiles.length > 0){
			    for(int i=0; i<targetFiles.length; i++){	
					try{
				    	TxtUtil.exportTxt(targetFiles[i], records);
					}
					catch(Exception e){
				    	if(i < targetFiles.length) continue;
					}
			    }
			}
		}
	}

	/**
	 * T2促銷
	 *
	 * @param brandCode
	 * @param targetFile
	 * @param dataDate
	 * @param actualDataDateEnd 預設為給定日期，若傳入為null，則帶今日日期+1天
	 * @param currentDate
	 * @throws Exception
	 */
	public void createT2Promotion(String brandCode, String targetFiles[], String dataDate, String actualDataDateEnd, String finishedPromotionDate,
		String currentDate, String isAutoSchedule, Map configMap1) throws Exception {

		log.info("===============<createT2Promotion>===============");
		log.info("actualDataDateEnd = " + actualDataDateEnd);
		log.info("finishedPromotionDate = " + finishedPromotionDate);

		ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
		//==============================取得isAllShop為Y的promotion========================
		TreeMap promotionItemMap = new TreeMap();
//		log.info("===============<取得isAllShop為Y的promotion>===============");
//		log.info(brandCode+"\\1\\"+actualDataDateEnd+"\\Y\\Y\\");
		List promotionItems = imPromotionViewDAO.getPromotionItemData(brandCode, "1", actualDataDateEnd, "Y", "Y");
		log.info("promotionItems.size() = " +promotionItems.size());
		if(promotionItems != null){
			for(int i = 0; i < promotionItems.size(); i++){
				Object[] promotionInfo = (Object[])promotionItems.get(i);
				filterPromotionItem(promotionItemMap, promotionInfo);
			}
		}
//		log.info("===============</取得isAllShop為Y的promotion>===============");

		//==============================取得促銷到期後依原價賣出的促銷商品=======================
		TreeMap finishedPromotionItemMap = new TreeMap();
		List finishedPromotionItems = imPromotionViewDAO.getPromotionChangeItemData(brandCode, "1", actualDataDateEnd, finishedPromotionDate
				, currentDate, isAutoSchedule);
		log.info("finishedPromotionItems.size() = " +finishedPromotionItems.size());
		if(finishedPromotionItems != null){
			for(int i = 0; i < finishedPromotionItems.size(); i++){
				Object[] finishedPromotionInfo = (Object[])finishedPromotionItems.get(i);
				filterPromotionItem(finishedPromotionItemMap, finishedPromotionInfo);
			}
		}

		//==============================匯出promotion item========================
		getActualExportPromotionItemForT2(brandCode, promotionItemMap, finishedPromotionItemMap);


		Set set = promotionItemMap.entrySet();
		Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		log.info("promotionItemEntry.length = " + promotionItemEntry.length);
		if(promotionItemEntry != null && promotionItemEntry.length > 0){
			StringBuffer record = new StringBuffer();
			log.info("targetFiles 共 = " + targetFiles.length + " 檔案 "); 
			for(int i=0; i<targetFiles.length; i++){ // 每個後台的轉出檔案
				List<String> records = new ArrayList();
				for (Map.Entry entry : promotionItemEntry) {
					record.delete(0, record.length());
					ImPromotionView promotionView = (ImPromotionView)entry.getValue();
					Date tempBeginDate = promotionView.getBeginDate();

					String actualBeginDate = null;
					int dateLen = 6;
					// 從路徑字串找到對應的配置檔文字
					Iterator it = configMap1.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if( targetFiles[i].indexOf(key) > -1 ){
							String value = (String)configMap1.get(key);
							if("C".equalsIgnoreCase(value)){
								actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
								dateLen = 8;
							}else if("M".equalsIgnoreCase(value)){
								actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							}else{
								//預設M
								actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							}
							break;
						}else{
							// 為了測試環境而加入此情況強制都西元年 
							actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							dateLen = 8;
							break;
						}
					}

					Double unitPrice = promotionView.getUnitPrice();
					//String discountType = promotionView.getDiscountType();
					//Double discount = promotionView.getDiscount();
					Double promotionPrice = unitPrice;
					/*
					if(StringUtils.hasText(discountType) && discount != null){
						if("1".equals(discountType)){ // 直接輸入促銷價
							log.info("promotionPrice = " + promotionPrice + " - " + discount );
							promotionPrice = promotionPrice - discount;
						}else if("2".equals(discountType)){ // 輸入折扣率
							log.info("promotionPrice = " + promotionPrice + " * " + (100 - discount) + " / " + 100 );
							promotionPrice = CommonUtils.round((promotionPrice * (100 - discount) / 100), 0);
						}
						if(promotionPrice < 0D){
							promotionPrice = 0D;
						}
					}
					*/
					record.append("U");
					record.append(CommonUtils.insertCharacterWithLimitedLength(promotionView.getItemCode(), 20, 20, CommonUtils.SPACE, "R"));
					record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength(actualBeginDate, dateLen, dateLen, CommonUtils.SPACE, "R"));
					records.add(record.toString());
				}
				//產生Exception的檔案跳過以免影響其他檔案(wade)
		    		try{
		    		    TxtUtil.exportTxt(targetFiles[i], records);
		    		}
		    		catch(Exception e){
		    		    if(i < targetFiles.length) continue;
		    		}
			}
		}
		log.info("===============</createT2Promotion>===============");
	}

	/**
	 * 取得實際匯出的促銷資料(T2) 將結束的促銷蓋掉原本正在執行的促銷單
	 *
	 * @param brandCode
	 * @param promotionItemMap
	 * @param finishedPromotionItemMap
	 * @throws Exception
	 */
	private void getActualExportPromotionItemForT2(String brandCode, TreeMap promotionItemMap,
			TreeMap finishedPromotionItemMap) throws Exception{

		Set finishedPromotionItemSet = finishedPromotionItemMap.entrySet();
		Map.Entry[] finishedPromotionItemEntry = (Map.Entry[]) finishedPromotionItemSet.toArray(new Map.Entry[finishedPromotionItemSet.size()]);
		for (Map.Entry entry : finishedPromotionItemEntry) {
			String key = (String) entry.getKey();
			ImPromotionView promotionView = (ImPromotionView)entry.getValue();
			if(promotionItemMap.get(key) == null){
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
	public void createT2Goodslst(String brandCode, String targetFiles[], Date dataDate, Date dataDateEnd) throws Exception {

		log.info("POSExportData.createT2Goodslst");
		String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd), 1);
		String priceType = "1";
		List<String> records = new ArrayList();
		ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
		ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");

		log.info("brandCode = " + brandCode);
		log.info("priceType = " + priceType);
		log.info("beginDate = " + beginDate);
		log.info("actualDataDate = " + actualDataDate);
		log.info("actualDataDateEnd = " + actualDataDateEnd);

		List<Object[]> results = imItemPriceDAO.getItemInfoByCondition(brandCode, priceType, beginDate, actualDataDate, actualDataDateEnd, null, "Y");
		if(results != null && results.size() > 0){
			ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean("imItemCategoryDAO");
			ImItemCategoryId categoryId = new ImItemCategoryId();
			categoryId.setBrandCode(brandCode);
			StringBuffer record = new StringBuffer();
			for (Object[] result : results) {
				record.delete(0, record.length());
				String itemCode = (String)result[1];
				String itemName = (String)result[2];
				Double unitPrice = ((BigDecimal)result[6]).doubleValue();

				Double unitPriceOrPromotionPrice = unitPrice;

				log.info("原始售價 = " + unitPrice);

				//===============加入查詢料號是否有促銷價===============20100908 add by joeywu
				List<Object[]> imPromotionList = imPromotionViewDAO.findImPromotionForPOS(brandCode, "Y", itemCode, actualDataDate);

				if(imPromotionList.size()>0 && imPromotionList.get(0) != null){
					Object[] rec = imPromotionList.get(0);
					unitPriceOrPromotionPrice = ((BigDecimal)rec[3]).doubleValue();
				}

				String isEnable = (String)result[9];
				String itemBrand = (String)result[10];   //商品品牌
				String itemBrandName = "";
				String category01 = (String)result[11];  //大類代碼
				String category02 = (String)result[12];  //中類代碼
				String isTax = (String)result[13];  //稅別
				String actualTaxCode = "0"; //預設稅別0
				String vipDiscount = (String)result[14];  //折扣類型
				String category01Name = "";
				String category02Name = "";
				Double minRatio = ((BigDecimal) result[15]).doubleValue(); // 銷售單位/最小單位換算
				Double declRatio = ((BigDecimal) result[16]).doubleValue(); // 銷售單位/報關單位換算
				String lotContro = (String) result[17]; // 批號控管
				String salesUnit = (String) result[5]; // 銷售單位
				String foreignCategory = (String) result[18]; // 國外類別
				String payOnline = (String) result[19]; // LinePay
				if(foreignCategory != null){
				    foreignCategory = foreignCategory.replaceAll("'", "");
				}
				//=============查詢商品品牌名稱=======================
				categoryId.setCategoryType("ItemBrand");
				categoryId.setCategoryCode(itemBrand);
				ImItemCategory itemCatagory = (ImItemCategory)imItemCategoryDAO.findByPrimaryKey(ImItemCategory.class, categoryId);
				if(itemCatagory != null){
					itemBrandName = itemCatagory.getCategoryName();
					if(StringUtils.hasText(itemBrandName)){
						itemBrandName = itemBrandName.replaceAll("'", "");
					}
				}
				//=============查詢大類名稱=======================
				categoryId.setCategoryType("CATEGORY01");
				categoryId.setCategoryCode(category01);
				itemCatagory = (ImItemCategory)imItemCategoryDAO.findByPrimaryKey(ImItemCategory.class, categoryId);
				if(itemCatagory != null){
					category01Name = itemCatagory.getCategoryName();
				}
				//=============查詢中類名稱=======================
				categoryId.setCategoryType("CATEGORY02");
				categoryId.setCategoryCode(category02);
				itemCatagory = (ImItemCategory)imItemCategoryDAO.findByPrimaryKey(ImItemCategory.class, categoryId);
				if(itemCatagory != null){
					category02Name = itemCatagory.getCategoryName();
				}
				//=============含稅(1)未稅(0)=========================
				if("P".equals(isTax)){
					actualTaxCode = "1";
				}
				//===============LinePay欄位值確認=======================
				if(!StringUtils.hasText(payOnline)){
					payOnline = "N";
				}
				//===============將資料寫入===========================
				if("Y".equals(isEnable)){
					record.append("U");
				}else{
					record.append("D");
				}
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemCode, 20, 20, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemBrandName, 24, 24, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemName, 24, 24, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(category02Name, 15, 15, CommonUtils.SPACE, "R"));
				//2014/03/13第三個位置換到第一個位置
				record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
				// 若商品為促銷期間以下兩個價錢為促銷價第三個為原價
				record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPriceOrPromotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPriceOrPromotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));

			//	record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemBrand, 6, 6, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(category02, 6, 6, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(category01Name, 20, 20, CommonUtils.SPACE, "R"));
				record.append(actualTaxCode);
				record.append(CommonUtils.insertCharacterWithLimitedLength("110", 3, 3, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(vipDiscount, 2, 2, CommonUtils.SPACE, "R"));
				//record.append(CommonUtils.insertCharacterWithLimitedLength("", 23, 23, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(minRatio.toString(), 6, 6, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(declRatio.toString(), 6, 6, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(lotContro, 1, 1, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(salesUnit, 10, 10, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(foreignCategory, 20, 20, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(payOnline, 1, 1, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(payOnline, 1, 1, CommonUtils.SPACE, "R"));
				records.add(record.toString());
			}
			
			//產生Exception的檔案跳過以免影響其他檔案(wade)
	    		if(targetFiles != null && targetFiles.length > 0){
	    		    for(int i=0; i<targetFiles.length; i++){	
	    				try{
	    			    	TxtUtil.exportTxt(targetFiles[i], records);
	    				}
	    				catch(Exception e){
	    			    	if(i < targetFiles.length) continue;
	    				}
	    		    }
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
	public void createT2ECGoodslst(String brandCode, String targetFiles[], Date dataDate, Date dataDateEnd, String dataDateString, String pmoActualDataDateEnd, String finishedPromotionDate,String currentDate, String isAutoSchedule, Map configMap1) throws Exception {
		/**==============================================商品清單前置準備資料開始==============================================**/
		log.info("POSExportData.createT2ECGoodslst");
		String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd), 1);
		String priceType = "1";
		List<String> records = new ArrayList();
		ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
		ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");

		log.info("brandCode = " + brandCode);
		log.info("priceType = " + priceType);
		log.info("beginDate = " + beginDate);
		log.info("actualDataDate = " + actualDataDate);
		log.info("actualDataDateEnd = " + actualDataDateEnd);
		/**==============================================商品清單前置準備資料結束==============================================**/
		
		/**==============================================商品清單資料寫入開始==============================================**/
		List<Object[]> results = imItemPriceDAO.getECItemInfoByCondition(brandCode, priceType, beginDate, actualDataDate, actualDataDateEnd, null, "Y");
		if(results != null && results.size() > 0){
			ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean("imItemCategoryDAO");
			ImItemCategoryId categoryId = new ImItemCategoryId();
			categoryId.setBrandCode(brandCode);
			StringBuffer record = new StringBuffer();
			
			for (Object[] result : results) {
				record.delete(0, record.length());
				String itemCode = (String)result[1];

				Double unitPrice = ((BigDecimal)result[6]).doubleValue();

				Double unitPriceOrPromotionPrice = unitPrice;

				log.info("原始售價 = " + unitPrice);

				//===============加入查詢料號是否有促銷價===============20100908 add by joeywu
				List<Object[]> imPromotionList = imPromotionViewDAO.findImPromotionForEC(brandCode, "Y", itemCode, actualDataDate);

				if(imPromotionList.size()>0 && imPromotionList.get(0) != null){
					Object[] rec = imPromotionList.get(0);
					unitPriceOrPromotionPrice = ((BigDecimal)rec[3]).doubleValue();
				}

				String isEnable = (String)result[9];


				record.append(CommonUtils.insertCharacterWithLimitedLength(itemCode, 20, 20, CommonUtils.SPACE, "R"));

				log.info("insert: "+itemCode);
				record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPriceOrPromotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "L"));
				records.add(record.toString());
			}
		}
		/**==============================================商品清單資料寫入結束==============================================**/

		/**==============================================促銷商品清單前置準備資料開始==============================================**/
		log.info("actualDataDateEnd = " + pmoActualDataDateEnd);
		log.info("finishedPromotionDate = " + finishedPromotionDate);

		//==============================取得isAllShop為Y的promotion========================
		TreeMap promotionItemMap = new TreeMap();
//		log.info("===============<取得isAllShop為Y的promotion>===============");
//		log.info(brandCode+"\\1\\"+actualDataDateEnd+"\\Y\\Y\\");
		List promotionItems = imPromotionViewDAO.getECPromotionItemData(brandCode, "1", pmoActualDataDateEnd, "Y", "Y");
		log.info("promotionItems.size() = " +promotionItems.size());
		if(promotionItems != null){
			for(int i = 0; i < promotionItems.size(); i++){
				Object[] promotionInfo = (Object[])promotionItems.get(i);
				filterPromotionItem(promotionItemMap, promotionInfo);
			}
		}
//		log.info("===============</取得isAllShop為Y的promotion>===============");

		//==============================取得促銷到期後依原價賣出的促銷商品=======================
		TreeMap finishedPromotionItemMap = new TreeMap();
		List finishedPromotionItems = imPromotionViewDAO.getECPromotionChangeItemData(brandCode, "1", pmoActualDataDateEnd, finishedPromotionDate
				, currentDate, isAutoSchedule);
		log.info("finishedPromotionItems.size() = " +finishedPromotionItems.size());
		if(finishedPromotionItems != null){
			for(int i = 0; i < finishedPromotionItems.size(); i++){
				Object[] finishedPromotionInfo = (Object[])finishedPromotionItems.get(i);
				filterPromotionItem(finishedPromotionItemMap, finishedPromotionInfo);
			}
		}

		/**==============================================促銷商品清單前置準備資料結束==============================================**/
		//==============================匯出promotion item========================
		getActualExportPromotionItemForT2(brandCode, promotionItemMap, finishedPromotionItemMap);


		Set set = promotionItemMap.entrySet();
		Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		log.info("promotionItemEntry.length = " + promotionItemEntry.length);
		if(promotionItemEntry != null && promotionItemEntry.length > 0){
			StringBuffer record = new StringBuffer();
			log.info("targetFiles 共 = " + targetFiles.length + " 檔案 "); 
			for(String targetFile:targetFiles){
				log.info("FILE LIST:"+targetFile); 
			
			}


				/**==============================================促銷商品清單資料寫入開始==============================================**/
				for (Map.Entry entry : promotionItemEntry) {
					record.delete(0, record.length());
					ImPromotionView promotionView = (ImPromotionView)entry.getValue();
					Date tempBeginDate = promotionView.getBeginDate();

					String actualBeginDate = null;
					int dateLen = 6;
					// 從路徑字串找到對應的配置檔文字
					/*
					Iterator it = configMap1.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if( targetFiles[i].indexOf(key) > -1 ){
							String value = (String)configMap1.get(key);
							if("C".equalsIgnoreCase(value)){
								actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
								dateLen = 8;
							}else if("M".equalsIgnoreCase(value)){
								actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							}else{
								//預設M
								actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							}
							break;
						}else{
							// 為了測試環境而加入此情況強制都西元年 
							actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
							dateLen = 8;
							break;
						}
					}
				*/
					//ImItemPriceViewDAO imItemPriceViewDAO = (ImItemPriceViewDAO) SpringUtils.getApplicationContext().getBean("imItemPriceViewDAO");
					//ImItemPriceView ipv = imItemPriceViewDAO.findOneItemPriceView(brandCode, "1", promotionView.getItemCode(),"Y","Y");
					ImItemPriceViewDAO imItemPriceViewDAO = (ImItemPriceViewDAO) SpringUtils.getApplicationContext().getBean("imItemPriceViewDAO");
					ImItemPriceView ipv = imItemPriceViewDAO.findOneItemPriceView(brandCode, "1", promotionView.getItemCode(),"Y","Y",DateUtils.format(beginDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
					Double unitPrice = promotionView.getUnitPrice();
					Double promotionPrice = unitPrice;
					record.append(CommonUtils.insertCharacterWithLimitedLength(promotionView.getItemCode(), 20, 20, CommonUtils.SPACE, "R"));
					log.info("insert: "+promotionView.getItemCode());
					record.append(CommonUtils.insertCharacterWithLimitedLength(Long.toString(Math.round(ipv.getUnitPrice())), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "L"));
					records.add(record.toString());
				}
		}
		/**==============================================促銷商品清單資料寫入結束==============================================**/
		
		
		
		
		
		/**==============================================主檔資料寫入檔案開始==============================================**/
		if(targetFiles != null && targetFiles.length > 0){
		    for(int i=0; i<targetFiles.length; i++){	
				try{
			    	TxtUtil.exportTxt(targetFiles[i], records);
				}
				catch(Exception e){//產生Exception的檔案跳過以免影響其他檔案(wade)
			    	if(i < targetFiles.length) continue;
				}
		    }
		}
		/**==============================================主檔資料寫入檔案結束==============================================**/

	}

	private String[] getActualOutPaths(String brandCode, String dataDate, String fileName, String[] originalOutPaths){

		String backupPath = iniProperties.getProperty(brandCode + ".backupPath");
		StringBuffer backupFile = new StringBuffer(backupPath);
		backupFile.append(dataDate);
		backupFile.append("/");
		backupFile.append(fileName);
		String[] actualOutPaths = new String[originalOutPaths.length + 1];
		System.arraycopy(originalOutPaths, 0, actualOutPaths, 0, originalOutPaths.length);
		actualOutPaths[actualOutPaths.length - 1] = backupFile.toString();
		return actualOutPaths;
	}
	/**
	 * T2促銷
	 *
	 * @param brandCode
	 * @param targetFile
	 * @param dataDate
	 * @param actualDataDateEnd 預設為給定日期，若傳入為null，則帶今日日期+1天
	 * @param currentDate
	 * @throws Exception
	 */
	public void createT2Promotion1(String brandCode, String targetFiles[], String dataDateString, String pmoActualDataDateEnd, String finishedPromotionDate,
		String currentDate, String isAutoSchedule, Map configMap1) throws Exception {


		log.info("===============</createT2Promotion>===============");
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
	public void exportItemDiscountData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportItemDiscountData");
			String actualDataDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String actualDataDateEnd = DateUtils.format(DateUtils.addDays(currentDate, 1), DateUtils.C_DATA_PATTON_YYYYMMDD);
			if(StringUtils.hasText(dataDateEnd)){
				actualDataDateEnd = dataDateEnd;
			}
			Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_ITEM_DISCOUNT_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "商品折扣資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = iniProperties.getProperty("discountFile");
			String discountOut = iniProperties.getProperty(brandCode + ".discountOut");
			String[] discountOuts = discountOut.split(",");
			if("T2".equals(brandCode)){
				String[] actualOutPaths = getActualOutPaths(brandCode, actualDataDate, fileName, discountOuts);
				createT2ItemDiscount(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.ITEM_DISCOUNT_DL_SUCCESS, currentDate, uuid , opUser);
		}catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.ITEM_DISCOUNT_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
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
	public void createT2ItemDiscount(String brandCode, String[] targetFiles, String actualDataDate, String actualDataDateEnd)
	throws Exception {

		log.info("POSExportData.createT2ItemDiscount");
		List<String> records = new ArrayList();
		HashMap searchKey = new HashMap();
		searchKey.put("brandCode", brandCode);
		//searchKey.put("dataDate", actualDataDate);暫時先撈取全部
		ImItemDiscountDAO imItemDiscountDAO = (ImItemDiscountDAO) SpringUtils.getApplicationContext().getBean("imItemDiscountDAO");
		List<ImItemDiscount> itemDiscounts = imItemDiscountDAO.findItemDiscountListByProperty(searchKey);
		if(itemDiscounts != null && itemDiscounts.size() > 0){
			BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
			StringBuffer record = new StringBuffer();
			for (ImItemDiscount itemDiscount : itemDiscounts) {
				record.delete(0, record.length());
				String isEnable = itemDiscount.getEnable();
				if("N".equals(isEnable)){
					record.append("D");
				}else{
					record.append("U");
				}
				String vipTypeCode = itemDiscount.getId().getVipTypeCode(); //卡別
				String identification = "";
				BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("VIPType", vipTypeCode);
				if(buCommonPhraseLine != null){
					identification = buCommonPhraseLine.getAttribute4();
				}
				String itemDiscountType = itemDiscount.getId().getItemDiscountType();
				String beginDate = DateUtils.format(itemDiscount.getBeginDate(), DateUtils.C_DATE_PATTON_SLASH);
				String endDate = DateUtils.format(itemDiscount.getEndDate(), DateUtils.C_DATE_PATTON_SLASH);
				String discount = itemDiscount.getDiscount().toString();

				record.append(CommonUtils.insertCharacterWithLimitedLength(vipTypeCode, 2, 2, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(identification, 2, 2, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(itemDiscountType, 2, 2, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(beginDate, 10, 10, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(endDate, 10, 10, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(discount, 2, 2, CommonUtils.SPACE, "L"));
				records.add(record.toString());
			}
			 //產生Exception的檔案跳過以免影響其他檔案(wade)
	    		if(targetFiles != null && targetFiles.length > 0){
	    		    for(int i=0; i<targetFiles.length; i++){	
	    				try{
	    			    	TxtUtil.exportTxt(targetFiles[i], records);
	    				}
	    				catch(Exception e){
	    			    	if(i < targetFiles.length) continue;
	    				}
	    		    }
	    		}
		}
	}
	
	/**
     * 電子標籤
     *
     * @param brandCode
     * @param dataDate
     * @param dataDateEnd
     * @param opUser
     */
    public void exportItemTagData(String brandCode, String dataDate, String dataDateEnd, String opUser) {

	String processName = null;
	String uuid = null;
	String msg = null;
	Date currentDate = new Date();
	try{
	    log.info("POSExportData.exportItemTagData");
	    String actualDataDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    if(StringUtils.hasText(dataDate))
		actualDataDate = dataDate;
	    
	    String actualDataDateEnd = actualDataDate;
	    if(StringUtils.hasText(dataDateEnd)){
		actualDataDateEnd = dataDateEnd;
	    }

	    Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
	    
	    processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_ITEM_TAG_DL;

	    uuid = UUID.randomUUID().toString();

	    SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "電子標籤資料下傳....", currentDate, uuid , opUser);

	    SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, transactionDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);

	    setIniProperties();
	    String fileName = iniProperties.getProperty("itemTagFile");
	    String itemTagOut = iniProperties.getProperty(brandCode + ".itemTagOut");
	    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
	    Date current = new Date();
	   
	    itemTagOut = itemTagOut+"PLU"+sdFormat.format(current).toString()+".csv";
	    String[] itemTagOuts = itemTagOut.split(",");
	    String[] actualOutPaths = getActualOutPaths(brandCode, actualDataDate, fileName, itemTagOuts);
	    log.info("babu:"+fileName+" "+itemTagOut);
	    
	    createT2ItemTag(brandCode, actualOutPaths, actualDataDate, actualDataDateEnd);
	    
	    SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, transactionDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);

	    SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.ITEM_DISCOUNT_DL_SUCCESS, currentDate, uuid , opUser);
	}catch(Exception ex){
	    ex.printStackTrace();
	    msg = MessageStatus.ITEM_DISCOUNT_DL_FAIL + "原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
	}
    }
    
    /**
     * T2電子標籤
     *
     * @param brandCode
     * @param targetFiles
     * @param actualDataDate
     * @param actualDataDateEnd
     * @throws Exception
     */
    public void createT2ItemTag(String brandCode, String[] targetFiles, String actualDataDate, String actualDataDateEnd)
    throws Exception {

	log.info("POSExportData.createT2Goodslst");
	Date executeDate = new Date();
	String uuid = null;
	List<String> records = new ArrayList();
	ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");
	TxtFormatUtil.loadConfig(CONFIG_FILE);
	LinkedMap configMap = TxtFormatUtil.getTxtInfo(PROCESS_NAME);
	String yyyyMMddHHMMSS = DateUtils.format(executeDate, "yyyyMMddHHmmss"); 
	String suffixDate = DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 
	uuid = UUID.randomUUID().toString();
	
	Map parameterMap = new HashMap();
    	parameterMap.put(KEY_UUID, uuid);
    	parameterMap.put(KEY_EXECUTE_DATE, executeDate);
    	parameterMap.put(KEY_SUFFIX_DATE, suffixDate);
    	
	
	// 要上傳的目錄
	String targetRoot = null;
	  if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
		targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
	  }else{
		throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
	  }
	  
	  
	  
	  
	  
	log.info("brandCode = " + brandCode);
	log.info("actualDataDate = " + actualDataDate);
	log.info("actualDataDateEnd = " + actualDataDateEnd);
	
	List<Object[]> results = imItemPriceDAO.getItemTagInfoByCondition(brandCode, actualDataDate, actualDataDateEnd);
	if(results != null && results.size() > 0){
	    log.info("results.size = " + results.size());
	    
	    StringBuffer record = new StringBuffer();
	    records.add(record.toString());
	    for (Object[] result : results) {
		record.delete(0, record.length());
		
		record.append('"'+(String)result[0]+'"');
		record.append(",");
		String item = result[1].toString();
		log.info("字串名稱:"+item);
		int lastIndex = item.length();
		log.info("字串長度:"+lastIndex);
		//record.append((String)result[1]);
		if('F'==item.charAt(lastIndex-1)){
		    log.info("進入尾碼為F的字串");
		    String replaceAfter = item.replace('F', '0');
		    log.info("異動後字串:"+replaceAfter);
		    record.append('"'+replaceAfter+'"');
		}else if('P'==item.charAt(lastIndex-1)){
		    log.info("進入尾碼為P的字串");
		    String replaceAfter = item.replace('P', '1');
		    log.info("異動後字串:"+replaceAfter);
		    record.append('"'+replaceAfter+'"');
		}else{
		    log.info("進入國際碼的字串");
		    record.append('"'+(String)result[1]+'"');
		}
		record.append(",");
		record.append('"'+(String)result[2]+'"');
		record.append(",");
		record.append('"'+(String)result[3]+'"');
		record.append(",");
		record.append('"'+(String)result[4]+'"');
		record.append(",");
		record.append('"'+(String)result[5]+'"');
		record.append(",");
		Double d6 = ((BigDecimal)result[6]).doubleValue();
		record.append('"'+d6.toString()+'"');
		//record.append('"'+(String)result[6]+'"');
		record.append(",");
		Double d7 = ((BigDecimal)result[7]).doubleValue();
		record.append('"'+d7.toString()+'"');
		//record.append('"'+(String)result[7]+'"');
		record.append(",");
		Double d8 = ((BigDecimal)result[8]).doubleValue();
		record.append('"'+d8.toString()+'"');
		//record.append('"'+(String)result[8]+'"');
		record.append(",");
		Double d9 = ((BigDecimal)result[9]).doubleValue();
		record.append('"'+d9.toString()+'"');
		//record.append('"'+(String)result[9]+'"');
		record.append(",");
		Double d10 = ((BigDecimal)result[10]).doubleValue();
		record.append('"'+d10.toString()+'"');
		//record.append('"'+(String)result[10]+'"');
		record.append(",");
		Double d11 = ((BigDecimal)result[11]).doubleValue();
		record.append('"'+d11.toString()+'"');
		//record.append('"'+(String)result[11]+'"');
		record.append(",");
		record.append('"'+"0"+'"');
		
		records.add(record.toString());
	    }
	    
	    if(targetFiles != null && targetFiles.length > 0){
		for(int i=0; i<targetFiles.length; i++){	
		    try{
			log.info("babu3:"+targetFiles[i]);
			/*Steve 修正 2015/1/26*/
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(targetFiles[i]),"UTF-8");
			for(String rec : records){
			    out.write(rec + "\r\n");
			}
			out.flush();
			out.close();
			/*Steve 修正*/
			//TxtUtil.exportTxt(targetFiles[i], records);原本寫法2015/1/26
			
		    }
		    catch(IOException e){
			e.printStackTrace();
		    }
		    catch(Exception e){
			if(i < targetFiles.length) continue;
		    }
		}
	    }
	    File folder = new File(targetRoot);	
	    String[] list = folder.list();
	    String backupRoot = null;
	    String errorRoot = null;
	    log.info("babu2:"+targetRoot+" size:"+list.length);
	    	// 上傳存檔
	    backupRoot +=  "/"+yyyyMMddHHMMSS;
	    errorRoot +=  "/"+yyyyMMddHHMMSS;
	    File folder_bak = new File(backupRoot);	// 上傳備份
	    File folder_err = new File(errorRoot);	// 上傳err

	    if(!folder.isDirectory()){
		log.info("babu5:");
	 	folder.mkdir();
            }
	    if(!folder_bak.isDirectory()){
		log.info("babu6:");
		folder_bak.mkdir();
	    }	
	    if(!folder_err.isDirectory()){
		log.info("babu7:");
	 	folder_err.mkdir();
	    }
	    
	    
	    log.info("babu7:"+list.length);
	    //產生Exception的檔案跳過以免影響其他檔案(wade)
	    
	   
	    if(records.size() == 0){
		log.info("本次無匯出的檔案");
	    }else{
		upload(configMap,parameterMap);
	    }
	}
    }
    
    
    /**
     * 上傳
     * @param processName
     * @param targetPath
     * @param configMap
     * @return
     * @throws Exception
     */
    private boolean upload(LinkedMap configMap, Map parameterMap)throws Exception{
	boolean result = false;
	boolean hasUpload = false;
	Map fileMap = new HashMap();
	String fileNameUpload = null;
	log.info("babu的FTP");
	//String suffixDate = (String)parameterMap.get(KEY_SUFFIX_DATE);
	
	String account = null;
	if(configMap.containsKey(PROCESS_NAME + "_ACCOUNT" )){
	    account = (String)configMap.get(PROCESS_NAME + "_ACCOUNT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_ACCOUNT 配置");
	}
	
	String password = null;
	if(configMap.containsKey(PROCESS_NAME + "_PASSWORD")){
	    password = (String)configMap.get(PROCESS_NAME + "_PASSWORD" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_PASSWORD 配置");
	}
	
	String urls = null;
	String[] url = null;
	if(configMap.containsKey(PROCESS_NAME + "_URL")){
	    urls = (String)configMap.get(PROCESS_NAME + "_URL" );
	    url = urls.split(",");
	    log.info("多台ftp主機路徑:"+url.length);
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_URL 配置");
	}
	
	String port = null;
	if(configMap.containsKey(PROCESS_NAME + "_PORT")){
	    port = (String)configMap.get(PROCESS_NAME + "_PORT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_PORT 配置");
	}
	
	String upLoadRoot = null;
	if(configMap.containsKey(PROCESS_NAME + "_UPLOAD_ROOT")){
	    upLoadRoot = (String)configMap.get(PROCESS_NAME + "_UPLOAD_ROOT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_UPLOAD_ROOT 配置");
	}
	    
	// 要上傳的檔案
	String targetRoot = null;
	String targetBakRoot = null;
	if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
	    targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
	    targetBakRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_BACKUP" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
	}
	log.info("hhhhhhhh3:"+upLoadRoot);
	//不同FTP主機回圈開始
	for(int i=0;i<url.length;i++){
	FtpUtils ftp = new FtpUtils(account,password,url[i],NumberUtils.getInt(port),upLoadRoot);
    try{
	// 登入
	if(ftp.login()){
	    String[] ftpFiles = ftp.getFClient().listNames();
	    for (String fileStr : ftpFiles) {
		fileMap.put(fileStr, fileStr);
	    }
	    
	    File folder = new File(targetRoot);	// 存放地點存檔
	    File folderBak = new File(targetBakRoot);	// 備份地點存檔
	    if(folder.isDirectory()){
		String[] list = folder.list();
		if(list.length == 0){
		    log.info("無上傳的檔案");
		}else{
		    for (String fileName : list) {
			log.info("檔案 = " + fileName);
                        fileNameUpload = fileName;
			if(!fileMap.containsKey(fileName)){ //上傳的檔案不存在的話才上傳
			    log.info("1111111:"+fileName.lastIndexOf("csv")+" ");
			    // 上傳每個文件       //&& fileName.lastIndexOf(suffixDate) > -1
			    if(fileName.lastIndexOf("csv") > -1 ){ // 上傳zip 且是當天的日期
				log.info("222222");
				if(ftp.uploadFile(targetRoot+"/"+fileName, fileName)){	
				    log.info("上傳文件成功");
				    result = true;
				    
				}else{
				    log.info("33333");
				    log.info("上傳文件失敗");
				    result = false;
				}
			    }
			}else{
			    hasUpload = true;
			    log.info("檔案: "+fileName + "已曾經傳過");
			}
		    }
		}
	    }else{
		throw new Exception("此路徑不是目錄");
	    }
	    
	}else{
	    throw new Exception("登入失敗");
	}
    }catch(Exception ex){
    	log.info("登入主機"+url[i]+"失敗");
    	continue;
    }
	// 登出
	ftp.logout();
	log.info("FTP主機連線關閉");
	}
	this.copyFile("D:/POS/T2/itemTag/"+fileNameUpload, "D:/POS/T2/itemTagBakup/"+fileNameUpload);
	this.delFile("D:/POS/T2/itemTag/"+fileNameUpload);
	//不同FTP主機回圈結束
	return result;
    }
    /**
    * 複製單個檔
    * @param oldPath String原檔路徑 如：c:/fqf.txt
    * @param newPath String複製後路徑 如：f:/fqf.txt
    * @return boolean
    */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //檔存在時
            InputStream inStream = new FileInputStream(oldPath);//讀入原檔
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            int length;
            while ( (byteread = inStream.read(buffer)) != -1) {
            bytesum += byteread; //位元組數 檔案大小
            //System.out.println(bytesum);
            fs.write(buffer, 0, byteread);
            }
            inStream.close();
            }
        }catch(Exception e) {
            System.out.println("複製單個檔操作出錯");
            e.printStackTrace();
        }
    }
    /**
    * 刪除檔
    * @param filePathAndNameString 檔路徑及名稱 如c:/fqf.txt
    * @param fileContentString
    * @return boolean
    */
    public void delFile(String filePathAndName){
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            java.io.File myDelFile = new java.io.File(filePath);
            myDelFile.delete();
        }
        catch(Exception e) {
            System.out.println("刪除檔操作出錯");
            e.printStackTrace();
        }
    }
    
    /**
	 * 組合單下傳
	 *
	 * @param brandCode
	 * @param dataDate
	 * @param dataDateEnd
	 * @param opUser
	 */
	public void exportCombineData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {

		String processName = null;
		String uuid = null;
		String msg = null;
		Date currentDate = new Date();
		try{
			log.info("POSExportData.exportCombineData");
			Date actualDataDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), +1);
			//dataDate = DateUtils.addDays(DateUtils.getShortDate(dataDate), -1);
			//dataDateEnd = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd), -1);
			
			if(dataDate == null ){
				dataDate = actualDataDate;
			}
			if(dataDateEnd == null){
				dataDateEnd = actualDataDate;
			}
			//Date transactionDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, actualDataDateEnd);
			String fileDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_COMBINE_DL;
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "組合單資料下傳....", currentDate, uuid , opUser);
			SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, currentDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
			setIniProperties();
			String fileName = iniProperties.getProperty("combineFile");
			String combineOut = iniProperties.getProperty(brandCode + ".combineOut");
			String[] combineOuts = combineOut.split(",");
			if("T2".equals(brandCode)){
				String[] actualOutPaths = getActualOutPaths(brandCode, fileDate, fileName, combineOuts);
				jobChangeCombineEable(brandCode, dataDate, dataDateEnd);
				createT2Combine(brandCode, actualOutPaths, dataDate, dataDateEnd);
			}
			SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, currentDate, processName, COMMON_SHOP_MACHINE_CODE, fileName, null, opUser);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.COMBINE_DL_SUCCESS, currentDate, uuid , opUser);
		}
		catch(Exception ex){
			ex.printStackTrace();
			msg = MessageStatus.COMBINE_DL_FAIL + "原因：";
			log.error(msg + ex.toString());
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
		}
	}
	
	/**
	 * T2組合單主檔
	 *
	 * @param brandCode
	 * @param targetFiles
	 * @param actualDataDate
	 * @param actualDataDateEnd
	 * @throws Exception
	 */
	public void createT2Combine(String brandCode, String[] targetFiles, Date dataDate, Date dataDateEnd)
	throws Exception {
		
		log.info("POSExportData.createT2Combine");
		
		String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		
		List<String> records = new ArrayList();
		HashMap searchKey = new HashMap();
		searchKey.put("brandCode", brandCode);
		searchKey.put("dataDate", actualDataDate);
		searchKey.put("dataDateEnd", actualDataDateEnd);
		ImPromotionReCombineDAO imPromotionReCombineDAO = (ImPromotionReCombineDAO) SpringUtils.getApplicationContext().getBean("imPromotionReCombineDAO");
		List<ImPromotionReCombine> reCombines = imPromotionReCombineDAO.findCombineListByProperty(searchKey);
		if(reCombines != null && reCombines.size() > 0){
			StringBuffer record = new StringBuffer();
			for (ImPromotionReCombine reCombine : reCombines) {
				record.delete(0, record.length());
				String isEnable = reCombine.getEnable();
				if("Y".equals(isEnable))
					record.append("U");
				else
					record.append("D");
				
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getHeadId().toString(), 8, 8, CommonUtils.SPACE, "L"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getCombineCode(), 8, 8, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getCombinePrice().toString(), 8, 8, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getCombineQuantity().toString(), 8, 8, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getItemBrand(), 10, 10, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getCategory02(), 10, 10, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getUnitPrice().toString(), 8, 8, CommonUtils.SPACE, "R"));
				record.append(CommonUtils.insertCharacterWithLimitedLength(reCombine.getForeignCategory(), 20, 20, CommonUtils.SPACE, "R"));
				records.add(record.toString());
			}
			//產生Exception的檔案跳過以免影響其他檔案(wade)
			if(targetFiles != null && targetFiles.length > 0){
			    for(int i=0; i<targetFiles.length; i++){	
					try{
				    	TxtUtil.exportTxt(targetFiles[i], records);
					}
					catch(Exception e){
				    	if(i < targetFiles.length) continue;
					}
			    }
			}
		}
	}
	
	public void jobChangeCombineEable(String brandCode, Date dataDate, Date dataDateEnd){
		log.info("開始執行排程,把需停用狀態的組合單做停用");
		
		String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
		
		ImPromotionReCombineMainService imPromotionReCombineMainService = (ImPromotionReCombineMainService)SpringUtils.getApplicationContext().getBean("imPromotionReCombineMainService");
		imPromotionReCombineMainService.updateCombineEable(brandCode, actualDataDate, actualDataDateEnd);
	}
	
	
	 public void executeFTPUpload(){
	    	Date executeDate = new Date();
	    	String uuid = null;

	    	String yyyyMMddHHMMSS = DateUtils.format(executeDate, "yyyyMMddHHmmss"); 

	    	String suffixDate = DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 

	    	uuid = UUID.randomUUID().toString();
	    	StringBuffer record = new StringBuffer();
	    	List<String> records = new ArrayList();

	    	Map parameterMap = new HashMap();
	    	parameterMap.put(KEY_UUID, uuid);
	    	parameterMap.put(KEY_EXECUTE_DATE, executeDate);
	    	parameterMap.put(KEY_SUFFIX_DATE, suffixDate);
	    	try {
	    		SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME, MessageStatus.LOG_INFO, "網購商品 FTP上載開始", executeDate, uuid, "SYS");
	    		TxtFormatUtil.loadConfig(CONFIG_FILE);//設定檔
	    		LinkedMap configMap = TxtFormatUtil.getTxtInfo(EC_PROCESS_NAME);

	    		// 要上傳的目錄
	    		String targetRoot = null;
	    		if(configMap.containsKey(EC_PROCESS_NAME + "_TARGET_ROOT")){
	    			targetRoot = (String)configMap.get(EC_PROCESS_NAME + "_TARGET_ROOT" );
	    		}else{
	    			throw new Exception("查無此:"+EC_PROCESS_NAME + "_TARGET_ROOT 配置");
	    		}

	    		/**製作檔案**/
	    		
	    		// 上傳備份的地方
	    		String backupRoot = null;
	    		if(configMap.containsKey(EC_PROCESS_NAME + "_TARGET_BACKUP")){
	    			backupRoot = (String)configMap.get(EC_PROCESS_NAME + "_TARGET_BACKUP" );
	    		}else{
	    			throw new Exception("查無此:"+EC_PROCESS_NAME + "_TARGET_BACKUP 配置");
	    		}

	    		// 上傳error的地方
	    		String errorRoot = null;
	    		if(configMap.containsKey(EC_PROCESS_NAME + "_TARGET_ERROR")){
	    			errorRoot = (String)configMap.get(EC_PROCESS_NAME + "_TARGET_ERROR" );
	    		}else{
	    			throw new Exception("查無此:"+EC_PROCESS_NAME + "_TARGET_ERROR 配置");
	    		}

	    		File folder = new File(targetRoot);		// 上傳存檔
	    		backupRoot +=  "/"+yyyyMMddHHMMSS;
	    		errorRoot +=  "/"+yyyyMMddHHMMSS;
	    		File folder_bak = new File(backupRoot);	// 上傳備份
	    		File folder_err = new File(errorRoot);	// 上傳err

	    		if(!folder.isDirectory()){
	    			folder.mkdir();
	    		}
	    		if(!folder_bak.isDirectory()){
	    			folder_bak.mkdir();
	    		}	
	    		if(!folder_err.isDirectory()){
	    			folder_err.mkdir();
	    		}

	    		if(folder.isDirectory()){
	    			String[] list = folder.list();
	    			if(list.length == 0){
	    				log.info("本次無匯出的檔案");
	    			}else{
	    				for (int j = 0; j < list.length; j++) {
	    					log.info("此次上傳的檔案數為  = " + list.length);
	    					log.info("檔案  = " + folder + "/" + list[j]);
	    					log.info("上傳中...");
	    					try {
	    						// 關鍵
	    						uploadEcFTP(configMap, parameterMap);
	    						SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME, MessageStatus.LOG_INFO, "上傳檔案成功", executeDate, uuid, "SYS");

	    						// 更新上傳成功的
	    						//buAddressBookService.updateTxtBuCustomerCard(parameterMap);
	    						SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME, MessageStatus.LOG_INFO, "已上傳完畢", executeDate, uuid, "SYS");
	    						//log.info("上傳完...");
	    						File declarationFile = new File(targetRoot+ "/" + list[j]);
	    						moveFilesToDestination(declarationFile, backupRoot);
	    					} catch (Exception e) {
	    						SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME,MessageStatus.LOG_ERROR,"上傳發生錯誤，原因 ：" + e.getMessage(),executeDate, uuid, "SYS");
	    						log.error(e.getMessage());
	    						File declarationFile = new File(targetRoot+ "/" + list[j]);
	    						moveFilesToDestination(declarationFile, errorRoot);
	    					}
	    				}
	    			}
	    		}else{
	    			throw new Exception("此路徑應為路徑而不是路徑");
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME, MessageStatus.LOG_ERROR, "網購商品 FTP上載發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
	    		log.error(e.getMessage());
	    	} finally{
	    		SiSystemLogUtils.createSystemLog(EC_PROCESS_NAME, MessageStatus.LOG_INFO, "網購商品 FTP上載結束", executeDate, uuid, "SYS");
	    		// 寄給維護人員
	    		MailUtils.systemErrorLogSendMail(EC_PROCESS_NAME, MessageStatus.LOG_ERROR,uuid);
	    	}
	    }
	    /**
	     * 上傳
	     * @param processName
	     * @param targetPath
	     * @param configMap
	     * @return
	     * @throws Exception
	     */
	    private boolean uploadEcFTP(LinkedMap configMap, Map parameterMap)throws Exception{
		boolean result = false;
		boolean hasUpload = false;
		Map fileMap = new HashMap();
		
		String suffixDate = (String)parameterMap.get(KEY_SUFFIX_DATE);
		
		String account = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_ACCOUNT" )){
		    account = (String)configMap.get(EC_PROCESS_NAME + "_ACCOUNT" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_ACCOUNT 配置");
		}
		
		String password = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_PASSWORD")){
		    password = (String)configMap.get(EC_PROCESS_NAME + "_PASSWORD" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_PASSWORD 配置");
		}
		
		String url = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_URL")){
		    url = (String)configMap.get(EC_PROCESS_NAME + "_URL" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_URL 配置");
		}
		
		String port = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_PORT")){
		    port = (String)configMap.get(EC_PROCESS_NAME + "_PORT" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_PORT 配置");
		}
		
		String upLoadRoot = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_UPLOAD_ROOT")){
		    upLoadRoot = (String)configMap.get(EC_PROCESS_NAME + "_UPLOAD_ROOT" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_UPLOAD_ROOT 配置");
		}
		    
		// 要上傳的檔案
		String targetRoot = null;
		if(configMap.containsKey(EC_PROCESS_NAME + "_TARGET_ROOT")){
		    targetRoot = (String)configMap.get(EC_PROCESS_NAME + "_TARGET_ROOT" );
		}else{
		    throw new Exception("查無此:"+EC_PROCESS_NAME + "_TARGET_ROOT 配置");
		}
		
		FtpUtils ftp = new FtpUtils(account,password,url,NumberUtils.getInt(port),upLoadRoot);
		// 登入
		if(ftp.login()){
		    String[] ftpFiles = ftp.getFClient().listNames();
		    for (String fileStr : ftpFiles) {
			fileMap.put(fileStr, fileStr);
		    }
		    
		    File folder = new File(targetRoot);	// 存放地點存檔
		    if(folder.isDirectory()){
			String[] list = folder.list();
			if(list.length == 0){
			    log.info("無上傳的檔案");
			}else{
			    for (String fileName : list) {
				log.info("檔案 = " + fileName);

				if(!fileMap.containsKey(fileName)){ //上傳的檔案不存在的話才上傳
				    // 上傳每個文件
				    if(fileName.lastIndexOf("DAT") > -1 ){ // 上傳zip 且是當天的日期
				    	log.info("test");
					if(ftp.uploadFile(targetRoot+"/"+fileName, fileName)){	
					    log.info("上傳文件成功");
					    result = true;
					}else{
					    log.info("上傳文件失敗");
					    result = false;
					}
				    }
				}else{
				    hasUpload = true;
				    log.info("檔案: "+fileName + "已曾經傳過");
				}
			    }
			}
		    }else{
			throw new Exception("此路徑不是目錄");
		    }
		    
		}else{
		    throw new Exception("登入失敗");
		}
		// 登出
		ftp.logout();
		return result;
	    }

	    /**
	     * 將檔案移至目的地
	     * @param declarationFile
	     * @param moveRoot
	     */
	    private void moveFilesToDestination(File declarationFile, String moveRoot){
		try {
		    
		    FileUtils.copyFileToDirectory(declarationFile, new File(moveRoot));
//		    log.info("移檔完...");

		    declarationFile.delete();
//		    log.info("刪檔完...");
		} catch (Exception e) {
		    log.error("搬移並刪除檔案失敗，原因：" + e.toString());  
		}
	    }
	    public void createT2TotalECGoodslst(String brandCode, String targetFiles[], Date dataDate, Date dataDateEnd, String dataDateString, String pmoActualDataDateEnd, String finishedPromotionDate,String currentDate, String isAutoSchedule, Map configMap1) throws Exception {
			/**==============================================商品清單前置準備資料開始==============================================**/
			log.info("POSExportData.createT2ECGoodslst");
			String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
			Date beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDateEnd), 1);
			String priceType = "1";
			List<String> records = new ArrayList();
			ImPromotionViewDAO imPromotionViewDAO = (ImPromotionViewDAO) SpringUtils.getApplicationContext().getBean("imPromotionViewDAO");
			ImItemPriceDAO imItemPriceDAO = (ImItemPriceDAO) SpringUtils.getApplicationContext().getBean("imItemPriceDAO");
			ImItemDAO imItemDAO = (ImItemDAO) SpringUtils.getApplicationContext().getBean("imItemDAO");

			log.info("brandCode = " + brandCode);
			log.info("priceType = " + priceType);
			log.info("beginDate = " + beginDate);
			log.info("actualDataDate = " + actualDataDate);
			log.info("actualDataDateEnd = " + actualDataDateEnd);
			/**==============================================商品清單前置準備資料結束==============================================**/
			
			/**==============================================商品清單資料寫入開始==============================================**/
	
			List<Object[]> results = imItemPriceDAO.getECTotalItemInfoByCondition(brandCode, priceType, beginDate, actualDataDate, actualDataDateEnd, null, "Y");
			if(results != null && results.size() > 0){
				ImItemCategoryDAO imItemCategoryDAO = (ImItemCategoryDAO) SpringUtils.getApplicationContext().getBean("imItemCategoryDAO");
				ImItemCategoryId categoryId = new ImItemCategoryId();
				categoryId.setBrandCode(brandCode);
				StringBuffer record = new StringBuffer();
				
				for (Object[] result : results) {
					record.delete(0, record.length());
					String itemCode = (String)result[1];

					Double unitPrice = ((BigDecimal)result[6]).doubleValue();

					Double unitPriceOrPromotionPrice = unitPrice;

					log.info("原始售價 = " + unitPrice);

					//===============加入查詢料號是否有促銷價===============20100908 add by joeywu
					List<Object[]> imPromotionList = imPromotionViewDAO.findImPromotionForEC(brandCode, "Y", itemCode, actualDataDate);

					if(imPromotionList.size()>0 && imPromotionList.get(0) != null){
						Object[] rec = imPromotionList.get(0);
						unitPriceOrPromotionPrice = ((BigDecimal)rec[3]).doubleValue();
					}

					String isEnable = (String)result[9];


					record.append(CommonUtils.insertCharacterWithLimitedLength(itemCode, 20, 20, CommonUtils.SPACE, "R"));

					log.info("insert: "+itemCode);
					record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(unitPriceOrPromotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
					record.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "L"));
					records.add(record.toString());
				}
			}

			/**==============================================商品清單資料寫入結束==============================================**/

			/**==============================================促銷商品清單前置準備資料開始==============================================**/
			log.info("actualDataDateEnd = " + pmoActualDataDateEnd);
			log.info("finishedPromotionDate = " + finishedPromotionDate);

			//==============================取得isAllShop為Y的promotion========================
			TreeMap promotionItemMap = new TreeMap();
//			log.info("===============<取得isAllShop為Y的promotion>===============");
//			log.info(brandCode+"\\1\\"+actualDataDateEnd+"\\Y\\Y\\");
			List promotionItems = imPromotionViewDAO.getECPromotionItemData(brandCode, "1", pmoActualDataDateEnd, "Y", "Y");
			log.info("promotionItems.size() = " +promotionItems.size());
			if(promotionItems != null){
				for(int i = 0; i < promotionItems.size(); i++){
					Object[] promotionInfo = (Object[])promotionItems.get(i);
					filterPromotionItem(promotionItemMap, promotionInfo);
				}
			}
//			log.info("===============</取得isAllShop為Y的promotion>===============");

			//==============================取得促銷到期後依原價賣出的促銷商品=======================
			TreeMap finishedPromotionItemMap = new TreeMap();
			List finishedPromotionItems = imPromotionViewDAO.getECPromotionChangeItemData(brandCode, "1", pmoActualDataDateEnd, finishedPromotionDate
					, currentDate, isAutoSchedule);
			log.info("finishedPromotionItems.size() = " +finishedPromotionItems.size());
			if(finishedPromotionItems != null){
				for(int i = 0; i < finishedPromotionItems.size(); i++){
					Object[] finishedPromotionInfo = (Object[])finishedPromotionItems.get(i);
					filterPromotionItem(finishedPromotionItemMap, finishedPromotionInfo);
				}
			}

			/**==============================================促銷商品清單前置準備資料結束==============================================**/
			//==============================匯出promotion item========================
			getActualExportPromotionItemForT2(brandCode, promotionItemMap, finishedPromotionItemMap);


			Set set = promotionItemMap.entrySet();
			Map.Entry[] promotionItemEntry = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
			log.info("promotionItemEntry.length = " + promotionItemEntry.length);
			if(promotionItemEntry != null && promotionItemEntry.length > 0){
				StringBuffer record = new StringBuffer();
				log.info("targetFiles 共 = " + targetFiles.length + " 檔案 "); 
				for(String targetFile:targetFiles){
					log.info("FILE LIST:"+targetFile); 
				
				}


					/**==============================================促銷商品清單資料寫入開始==============================================**/
					for (Map.Entry entry : promotionItemEntry) {
						record.delete(0, record.length());
						ImPromotionView promotionView = (ImPromotionView)entry.getValue();
						Date tempBeginDate = promotionView.getBeginDate();

						String actualBeginDate = null;
						int dateLen = 6;
						// 從路徑字串找到對應的配置檔文字
						/*
						Iterator it = configMap1.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							if( targetFiles[i].indexOf(key) > -1 ){
								String value = (String)configMap1.get(key);
								if("C".equalsIgnoreCase(value)){
									actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
									dateLen = 8;
								}else if("M".equalsIgnoreCase(value)){
									actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
								}else{
									//預設M
									actualBeginDate = DateUtils.formatToTWDate(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
								}
								break;
							}else{
								// 為了測試環境而加入此情況強制都西元年 
								actualBeginDate = DateUtils.format(tempBeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
								dateLen = 8;
								break;
							}
						}
					*/
						ImItemPriceViewDAO imItemPriceViewDAO = (ImItemPriceViewDAO) SpringUtils.getApplicationContext().getBean("imItemPriceViewDAO");
						ImItemPriceView ipv = imItemPriceViewDAO.findOneItemPriceView(brandCode, "1", promotionView.getItemCode(),"Y","Y",DateUtils.format(beginDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
						Double unitPrice = promotionView.getUnitPrice();
						Double promotionPrice = unitPrice;
						record.append(CommonUtils.insertCharacterWithLimitedLength(promotionView.getItemCode(), 20, 20, CommonUtils.SPACE, "R"));
						log.info("insert: "+promotionView.getItemCode());
						record.append(CommonUtils.insertCharacterWithLimitedLength(Long.toString(Math.round(ipv.getUnitPrice())), 8, 8, CommonUtils.SPACE, "L"));
						record.append(CommonUtils.insertCharacterWithLimitedLength(new Long(promotionPrice.longValue()).toString(), 8, 8, CommonUtils.SPACE, "L"));
						record.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "L"));
						records.add(record.toString());
					}
			}
			/**==============================================促銷商品清單資料寫入結束==============================================**/
			
			
			
			
			
			/**==============================================主檔資料寫入檔案開始==============================================**/
			if(targetFiles != null && targetFiles.length > 0){
			    for(int i=0; i<targetFiles.length; i++){	
					try{
				    	TxtUtil.exportTxt(targetFiles[i], records);
					}
					catch(Exception e){//產生Exception的檔案跳過以免影響其他檔案(wade)
				    	if(i < targetFiles.length) continue;
					}
			    }
			}
			/**==============================================主檔資料寫入檔案結束==============================================**/

		}
	    
	    /**
		 * 組合單下傳
		 *
		 * @param brandCode
		 * @param dataDate
		 * @param dataDateEnd
		 * @param opUser
		 */
		public void exportFullData(String brandCode, Date dataDate, Date dataDateEnd, String opUser) {
			log.info("POSExportData.exportFullData");
			String processName = null;
			String uuid = null;
			String msg = null;
			Date currentDate = new Date();
			try{
				log.info("POSExportData.exportFullData");
				Date actualDataDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), +1);

				Date beginDate = null;
				Date endDate = null;
				if(dataDate == null ){
					beginDate = DateUtils.addDays(DateUtils.getShortDate(currentDate), +1);
					endDate = DateUtils.getShortDate(currentDate);
				}else{
					beginDate = DateUtils.addDays(DateUtils.getShortDate(dataDate), +1);
					endDate = DateUtils.getShortDate(dataDate);
				}
				
				if(dataDateEnd == null){
					dataDateEnd = DateUtils.getShortDate(currentDate);
				}
				
				String fileDate = DateUtils.format(currentDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
				processName = brandCode + "_" + SiPosLogService.PROCESS_NAME_FULL_DL;
				uuid = UUID.randomUUID().toString();
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "滿額贈資料下傳....", currentDate, uuid , opUser);
				SiPosLogUtil.createPosLog(COMMON_SHOP_CODE, currentDate, processName, null, null, COMMON_SHOP_MACHINE_CODE, opUser, brandCode);
				setIniProperties();
				String fileHName = iniProperties.getProperty("cardpromohFile");
				String fullHOut = iniProperties.getProperty(brandCode + ".cardpromohOut");
				String fileBName = iniProperties.getProperty("cardpromobFile");
				String fullBOut = iniProperties.getProperty(brandCode + ".cardpromobOut");
				String[] fullHOuts = fullHOut.split(",");
				String[] fullBOuts = fullBOut.split(",");
				if("T2".equals(brandCode)){
					String[] actualHeadOutPaths = getActualOutPaths(brandCode, fileDate, fileHName, fullHOuts);
					String[] actualBodyOutPaths = getActualOutPaths(brandCode, fileDate, fileBName, fullBOuts);
					createT2Full(brandCode, actualHeadOutPaths, actualBodyOutPaths, beginDate, dataDateEnd, endDate);
				}
				SiPosLogUtil.updatePosLogFinish(brandCode, COMMON_SHOP_CODE, currentDate, processName, COMMON_SHOP_MACHINE_CODE, fileHName, null, opUser);
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, MessageStatus.FULL_DL_SUCCESS, currentDate, uuid , opUser);
			}
			catch(Exception ex){
				ex.printStackTrace();
				msg = MessageStatus.FULL_DL_FAIL + "原因：";
				log.error(msg + ex.toString());
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), currentDate, uuid , opUser);
			}
		}
		
		/**
		 * T2組合單主檔
		 *
		 * @param brandCode
		 * @param targetFiles
		 * @param actualDataDate
		 * @param actualDataDateEnd
		 * @throws Exception
		 */
		public void createT2Full(String brandCode, String[] targetHeadFiles, String[] targetBodyFiles, Date dataDate, Date dataDateEnd, Date endDate)
		throws Exception {
			
			log.info("POSExportData.createT2Full");
			Long buyAmount = 0L;
			String actualDataDate = DateUtils.format(dataDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String actualEndDate = DateUtils.format(endDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			String actualDataDateEnd = DateUtils.format(dataDateEnd, DateUtils.C_DATA_PATTON_YYYYMMDD);
			ImPromotionDAO imPromotionDAO = (ImPromotionDAO) SpringUtils.getApplicationContext().getBean("imPromotionDAO");
			List<String> recordHs = new ArrayList();
			List<String> recordBs = new ArrayList();
			HashMap searchKey = new HashMap();
			boolean hasData = false;
			
			log.info("actualDataDate: " + actualDataDate + "++++++++++++");
			log.info("actualEndDate: " + actualEndDate + "++++++++++++");
			log.info("actualDataDateEnd: " + actualDataDateEnd + "++++++++++++");
			
			searchKey.clear();
			searchKey.put("brandCode", brandCode);
			searchKey.put("dataDate", actualEndDate);
			searchKey.put("dataDateEnd", actualDataDateEnd);
			searchKey.put("orderTypeCode", "PMF");
			
			List<ImPromotion> imPromotionDeletes = imPromotionDAO.getPromotionDataByBeginAndEnd(searchKey, "N");
			StringBuffer recordH = new StringBuffer();
			StringBuffer recordB = new StringBuffer();
			if(imPromotionDeletes != null && imPromotionDeletes.size() > 0){
				log.info("imPromotionDeletes.size()"+imPromotionDeletes.size());
				for (ImPromotion imPromotion : imPromotionDeletes) {
					buyAmount = imPromotion.getBuyAmount().longValue();
					recordH.delete(0, recordH.length());
					recordH.append("D");
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionName(), 30, 30, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(DateUtils.format(imPromotion.getBeginDate(),DateUtils.C_DATA_PATTON_YYYYMMDD), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(DateUtils.format(imPromotion.getEndDate(),DateUtils.C_DATA_PATTON_YYYYMMDD), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(buyAmount.toString(), 8, 8, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getEnable().toString(), 1, 1, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getDiscount().toString(), 8, 8, CommonUtils.SPACE, "R"));
					recordHs.add(recordH.toString());
					
					List<ImPromotionFull> imPromotionFulls = (List<ImPromotionFull>)imPromotion.getImPromotionFulls();
					if(imPromotionFulls != null && imPromotionFulls.size() > 0){
						recordB.delete(0, recordB.length());
						recordB.append("D");
						recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
						recordB.append(CommonUtils.insertCharacterWithLimitedLength("00", 30, 30, CommonUtils.SPACE, "R"));
						recordB.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "R"));
						recordBs.add(recordB.toString());
						for(ImPromotionFull imPromotionFull : imPromotionFulls){
							if(!"Y".equals(imPromotionFull.getIsJoin())){
								imPromotionFull.setDiscountRate(0L);
							}
							recordB.delete(0, recordB.length());
							recordB.append("D");
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotionFull.getVipDiscountCode(), 30, 30, CommonUtils.SPACE, "R"));
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotionFull.getDiscountRate().toString(), 8, 8, CommonUtils.SPACE, "R"));
							recordBs.add(recordB.toString());
						}
					}
				}
				hasData = true;
			}
			
			searchKey.put("dataDate", actualDataDate);
			
			List<ImPromotion> imPromotionUpsates = imPromotionDAO.getPromotionDataByBeginAndEnd(searchKey, "Y");
			if(imPromotionUpsates != null && imPromotionUpsates.size() > 0){
				log.info("imPromotionUpsates.size()"+imPromotionUpsates.size());
				StringBuffer record = new StringBuffer();
				for (ImPromotion imPromotion : imPromotionUpsates) {
					buyAmount = imPromotion.getBuyAmount().longValue();
					recordH.delete(0, recordH.length());
					recordH.append("U");
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionName(), 30, 30, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(DateUtils.format(imPromotion.getBeginDate(),DateUtils.C_DATA_PATTON_YYYYMMDD), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(DateUtils.format(imPromotion.getEndDate(),DateUtils.C_DATA_PATTON_YYYYMMDD), 20, 20, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(buyAmount.toString(), 8, 8, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getEnable().toString(), 1, 1, CommonUtils.SPACE, "R"));
					recordH.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getDiscount().toString(), 8, 8, CommonUtils.SPACE, "R"));
					recordHs.add(recordH.toString());
					
					List<ImPromotionFull> imPromotionFulls = (List<ImPromotionFull>)imPromotion.getImPromotionFulls();
					if(imPromotionFulls != null && imPromotionFulls.size() > 0){
						recordB.delete(0, recordB.length());
						recordB.append("U");
						recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
						recordB.append(CommonUtils.insertCharacterWithLimitedLength("00", 30, 30, CommonUtils.SPACE, "R"));
						recordB.append(CommonUtils.insertCharacterWithLimitedLength("0", 8, 8, CommonUtils.SPACE, "R"));
						recordBs.add(recordB.toString());
						for(ImPromotionFull imPromotionFull : imPromotionFulls){
							if(!"Y".equals(imPromotionFull.getIsJoin())){
								imPromotionFull.setDiscountRate(0L);
							}
							recordB.delete(0, recordB.length());
							recordB.append("U");
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotion.getPromotionCode(), 20, 20, CommonUtils.SPACE, "R"));
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotionFull.getVipDiscountCode(), 30, 30, CommonUtils.SPACE, "R"));
							recordB.append(CommonUtils.insertCharacterWithLimitedLength(imPromotionFull.getDiscountRate().toString(), 8, 8, CommonUtils.SPACE, "R"));
							recordBs.add(recordB.toString());
						}
					}
				}
				hasData = true;
			}
			
			if(hasData){
				//產生Exception的檔案跳過以免影響其他檔案(wade)
				if(targetHeadFiles != null && targetHeadFiles.length > 0){
					for(int i=0; i<targetHeadFiles.length; i++){	
						try{
							TxtUtil.exportTxt(targetHeadFiles[i], recordHs);
						}
						catch(Exception e){
							if(i < targetHeadFiles.length) continue;
						}
					}

					if(targetBodyFiles != null && targetBodyFiles.length > 0){
						for(int i=0; i<targetBodyFiles.length; i++){	
							try{
								TxtUtil.exportTxt(targetBodyFiles[i], recordBs);
							}
							catch(Exception e){
								if(i < targetBodyFiles.length) continue;
							}
						}
					}
				}
			}else{
				log.info("無活動代號資料");
			}
		}
}