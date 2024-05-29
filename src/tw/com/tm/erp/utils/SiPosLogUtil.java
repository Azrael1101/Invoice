package tw.com.tm.erp.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.SiPosLog;
import tw.com.tm.erp.hbm.bean.SiPosLogId;
import tw.com.tm.erp.hbm.service.SiPosLogService;

/**
 * SI POS LOG UTILs
 * 
 * @author Mac8
 * 
 */
public class SiPosLogUtil {
    private static final Log log = LogFactory.getLog(SiPosLogUtil.class);

    /**
     * 建立POS LOG 利用 TRAN DATE
     * 
     * @param transactionDate
     * @param processName
     * @param createdBy
     */
    public static void createPosLogByDate(Date transactionDate,
	    String processName, String createdBy) {
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
		.getApplicationContext().getBean("siPosLogService");
	try {
	    siPosLogService.createPosLogByDate(transactionDate, processName,
		    createdBy);
	} catch (Exception ex) {
	    log.error(processName + " POS建立預設LOG資料發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * update log when write log finish
     * 
     * @param shopCode
     * @param transactionDate
     * @param lastUpdatedBy
     * @param headFileName
     * @param lineFileName
     */
    public static void updatePosLogFinish(String shopCode,
	    Date transactionDate, String processName, String posMachineCode) {
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
		.getApplicationContext().getBean("siPosLogService");
	try {
	    siPosLogService.updatePosLogFinish(shopCode, transactionDate,
		    processName, posMachineCode);
	} catch (Exception ex) {
	    log.error(processName + " POS更新LOG資料發生錯誤，原因：" + ex.toString());
	}
    }
    
    /**
     * update log when write log finish
     * 
     * @param shopCode
     * @param transactionDate
     * @param processName
     * @param posMachineCode
     * @param headFileName
     * @param lineFileName
     */
    public static void updatePosLogFinish(String brandCode, String shopCode, Date transactionDate, String processName, 
	    String posMachineCode, String headFileName, String lineFileName, String lastUpdatedBy) {
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
		.getApplicationContext().getBean("siPosLogService");
	try {
	    siPosLogService.updatePosLogFinish(brandCode, shopCode, transactionDate,
		    processName, posMachineCode, headFileName, lineFileName, lastUpdatedBy);
	} catch (Exception ex) {
	    log.error(processName + " POS更新LOG資料發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 建立一筆 LOG
     * 
     * @param shopCode
     * @param transactionDate
     * @param processName
     * @param headFileName
     * @param lineFileName
     * @param posMachineCode
     * @param createdBy
     * @param brandCode
     */
    public static void createPosLog(String shopCode, Date transactionDate,
	    String processName, String headFileName, String lineFileName,
	    String posMachineCode, String createdBy, String brandCode) {
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
		.getApplicationContext().getBean("siPosLogService");
	try {
	    siPosLogService.createPosLog(shopCode, transactionDate,
		    processName, headFileName, lineFileName, posMachineCode,
		    createdBy, brandCode);
	} catch (Exception ex) {
	    log.error(processName + " POS建立LOG資料發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 更新 POS LOG HEAD_FILE_NAME , HEAD_LINE_NAME
     * 
     * @param shopCode
     * @param transactionDate
     * @param headFileName
     * @param lineFileName
     */
    public static void updatePosLogFile(String shopCode, Date transactionDate,
	    String processName, String headFileName, String lineFileName,
	    String posMachineCode) {
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
		.getApplicationContext().getBean("siPosLogService");
	try {
	    siPosLogService.updatePosLogFile(shopCode, transactionDate,
		    processName, headFileName, lineFileName, posMachineCode);
	} catch (Exception ex) {
	    log.error(processName + " POS更新LOG資料發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * 將 UPLOAD FILE NAME PARSER 成 R980311001
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Object[] parserUploadFileName(String fileName)
	    throws Exception {
	Object[] re = new Object[3];
	try {
	    if (StringUtils.hasText(fileName)) {
		String dateStr = fileName.substring(1, 7);
		log.info("拆解DATE[" + fileName + "]");
		Date tDate = DateUtils.parseTDate(DateUtils.C_DATA_PATTON_YYMMDD,dateStr);
		re[0] = tDate;

		re[1] = fileName.substring(7, 9);

		String oldShopCode = fileName.substring(
			fileName.indexOf(".") + 1, fileName.length());
		// 新舊庫別對照
		BuShop newBuShop = OldSysMapNewSys.getNewShop(oldShopCode);
		if (newBuShop != null) {
		    re[2] = newBuShop.getShopCode();
		} else {
		    throw new Exception("新舊庫別對照有問題 " + oldShopCode);
		}
	    }
	} catch (Exception ex) {
	    log.error("拆解匯入檔案[" + fileName + "]時發生錯誤，原因：" + ex.toString());
	}
	return re;
    }

    /**
     * 建立 SI POS LOG BEAN
     * @param posMachineCode
     * @param processName
     * @param shopCode
     * @param transactionDate
     * @param brandCode
     * @param createDate
     * @param createdBy
     * @param headFileName
     * @param lastUpdateDate
     * @param lastUpdatedBy
     * @param lineFileName
     * @param reserve1
     * @param reserve2
     * @param reserve3
     * @param reserve4
     * @param reserve5
     * @param status
     * @return
     */
    public static SiPosLog createSiPosLogBean(String posMachineCode,
	    String processName, String shopCode, Date transactionDate,
	    String brandCode, String createdBy,
	    String headFileName, String lastUpdatedBy,
	    String lineFileName, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5, String status) {
	
	SiPosLog siPosLog = new SiPosLog();

	SiPosLogId id = new SiPosLogId();
	id.setPosMachineCode(posMachineCode);
	id.setProcessName(processName);
	id.setShopCode(shopCode);
	id.setTransactionDate(transactionDate);

	siPosLog.setBrandCode(brandCode);

	siPosLog.setCreatedBy(createdBy);
	siPosLog.setHeadFileName(headFileName);
	siPosLog.setId(id);

	siPosLog.setLastUpdatedBy(lastUpdatedBy);
	siPosLog.setLineFileName(lineFileName);
	siPosLog.setReserve1(reserve1);
	siPosLog.setReserve2(reserve2);
	siPosLog.setReserve3(reserve3);
	siPosLog.setReserve4(reserve4);
	siPosLog.setReserve5(reserve5);
	siPosLog.setStatus(status);
	return siPosLog;
    }
    
    /**
     * save or update pos log 
     * @param posMachineCode
     * @param processName
     * @param shopCode
     * @param transactionDate
     * @param brandCode
     * @param createDate
     * @param createdBy
     * @param headFileName
     * @param lastUpdateDate
     * @param lastUpdatedBy
     * @param lineFileName
     * @param reserve1
     * @param reserve2
     * @param reserve3
     * @param reserve4
     * @param reserve5
     * @param status
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public static void createPosLog(String posMachineCode,
	    String processName, String shopCode, Date transactionDate,
	    String brandCode, String createdBy,
	    String headFileName, String lastUpdatedBy,
	    String lineFileName, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5, String status) throws IllegalAccessException, InvocationTargetException{
	
	SiPosLogService siPosLogService = (SiPosLogService) SpringUtils
	.getApplicationContext().getBean("siPosLogService");
	
	siPosLogService.createPosLog( createSiPosLogBean( posMachineCode,
		     processName,  shopCode,  transactionDate,
		     brandCode,  createdBy,
		     headFileName,  lastUpdatedBy,
		     lineFileName,  reserve1,  reserve2,
		     reserve3,  reserve4,  reserve5,  status) ) ;
    }
}
