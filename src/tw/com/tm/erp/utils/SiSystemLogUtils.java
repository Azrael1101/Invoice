package tw.com.tm.erp.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.service.SiSystemLogService;

public class SiSystemLogUtils {

	private static final Log log = LogFactory.getLog(SiSystemLogUtils.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public static void createSystemLog(String processName, String logLevel, String message, Date executeDate, String processLotNo, String user) {
		log.info("SiSystemLogUtils.addSystemLog " + processName + "," +  logLevel + "," +  message + "," +  executeDate + "," +  processLotNo );
		SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");
		try {
			logService.createSystemLog(processName, logLevel, message, executeDate, processLotNo, user);
		} catch (Exception ex) {
		    	ex.printStackTrace();
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	public static void createSystemLog(String processName, String logLevel, String message, Date executeDate, String processLotNo, String user, String brandCode) {
		log.info("SiSystemLogUtils.addSystemLog " + processName + "," +  logLevel + "," +  message + "," +  executeDate + "," +  processLotNo );
		SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");
		try {
			logService.createSystemLog(processName, logLevel, message, executeDate, processLotNo, user, brandCode);
		} catch (Exception ex) {
		    	ex.printStackTrace();
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	public static void deleteSystemLog(String processName, String processLotNo) {
		log.info("SiSystemLogUtils.deleteSystemLog " + processName + ","  +  processLotNo );
		SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");
		try {
			logService.deleteSystemLog(processName, processLotNo);
		} catch (Exception ex) {
		    	ex.printStackTrace();
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	/*
	public static void addSystemLog(String processName, String logLevel, String message, String executeDate, String processLotNo,
			String reserve1 , String reserve2 , String reserve3 , String reserve4 , String reserve5 , String createdBy , Date creationDate , String brandCode , String status , String fromEmployeeCode , String toEmployeeCode ) {
	    
		log.info("SiSystemLogUtils.addSystemLog " + processName + "," +  logLevel + "," +  message + "," +  executeDate + "," +  processLotNo + "," +
			 reserve1 + "," +  reserve2 + "," +  reserve3 + "," +  reserve4 + "," +  reserve5 + "," +  createdBy + "," +  creationDate + "," +  brandCode + "," + status + "," +  fromEmployeeCode + "," + toEmployeeCode);
		SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");		
		try {
			SiSystemLog sysLog = new SiSystemLog();
			sysLog.setBrandCode(brandCode);
			sysLog.setCreatedBy(createdBy);
			sysLog.setCreationDate(creationDate);
			sysLog.setExecuteDate(executeDate);
			sysLog.setFromEmployeeCode(fromEmployeeCode);
			sysLog.setLogLevel(logLevel);
			sysLog.setMessage(message);
			sysLog.setProcessLotNo(processLotNo);
			sysLog.setProcessName(processName);
			sysLog.setReserve1(reserve1);
			sysLog.setReserve2(reserve2);
			sysLog.setReserve3(reserve3);
			sysLog.setReserve4(reserve4);
			sysLog.setReserve5(reserve5);
			sysLog.setStatus(status);
			sysLog.setToEmployeeCode(toEmployeeCode);
			logService.createSystemLog(sysLog);
		} catch (Exception ex) {
		    	ex.printStackTrace();
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	public static void addSystemLog(String processName, String logLevel, String message, String processLotNo,
			String reserve1 , String reserve2 , String reserve3 , String reserve4 , String reserve5 , String createdBy , String brandCode , String status , String fromEmployeeCode , String toEmployeeCode ) {
	    	log.info("SiSystemLogUtils.addSystemLog " + processName + "," +  logLevel + "," +  message + "," +  processLotNo + "," +
			 reserve1 + "," +  reserve2 + "," +  reserve3 + "," +  reserve4 + "," +  reserve5 + "," +  createdBy + "," +  brandCode + "," + status + "," +  fromEmployeeCode + "," + toEmployeeCode);
		
		SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");		
		try {
			Calendar now = Calendar.getInstance();
			Date nowD = now.getTime();
			String executeDate = "" ;
			try{
			    executeDate = DateUtils.format(nowD,DateUtils.C_DATA_PATTON_YYYYMMDD);
			}catch(Exception ex){
			    ex.printStackTrace();
			    log.error("addSystemLog error :" + ex.getMessage() );
			}
			SiSystemLog sysLog = new SiSystemLog();
			sysLog.setBrandCode(brandCode);
			sysLog.setCreatedBy(createdBy);
			sysLog.setCreationDate(nowD);
			sysLog.setExecuteDate(executeDate);
			sysLog.setFromEmployeeCode(fromEmployeeCode);
			sysLog.setLogLevel(logLevel);
			sysLog.setMessage(message);
			sysLog.setProcessLotNo(processLotNo);
			sysLog.setProcessName(processName);
			sysLog.setReserve1(reserve1);
			sysLog.setReserve2(reserve2);
			sysLog.setReserve3(reserve3);
			sysLog.setReserve4(reserve4);
			sysLog.setReserve5(reserve5);
			sysLog.setStatus(status);
			sysLog.setToEmployeeCode(toEmployeeCode);
			logService.createSystemLog(sysLog);
		} catch (Exception ex) {
		    	ex.printStackTrace();
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}	
	*/
	
	public static List<SiSystemLog> getSystemLog(String processName, String logLevel, String processLotNo) {

	    log.info("SiSystemLogUtils.addSystemLog " + processName + "," +  logLevel + "," +  processLotNo );

	    SiSystemLogService logService = (SiSystemLogService) context.getBean("siSystemLogService");
	    List<SiSystemLog> siSystemLogs= null;
	    try {
		
		 siSystemLogs = logService.getSystemLog(processName, logLevel, processLotNo);
	    } catch (Exception ex) {
		log.error(processName + "作業於系統記錄檔時發生錯誤，原因：" + ex.toString());
		ex.printStackTrace();
	    }
	    return siSystemLogs;
	}
}
