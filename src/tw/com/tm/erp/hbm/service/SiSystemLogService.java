package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.dao.SiSystemLogDAO;
import tw.com.tm.erp.utils.DateUtils;


/**
 * System Log Service
 * @author Mac8
 *
 */

public class SiSystemLogService {

	private static final Log log = LogFactory.getLog(SiSystemLogService.class);

	private SiSystemLogDAO siSystemLogDAO;

	/* Spring IoC */

	public void setSiSystemLogDAO(SiSystemLogDAO siSystemLogDAO) {
		this.siSystemLogDAO = siSystemLogDAO;
	}

	/**
	 * Create System Log 
	 * @param processName
	 * @param logLevel
	 * @param message
	 * @param executeDate
	 * @param processLotNo
	 * @param user
	 */

	public void createSystemLog(String processName, String logLevel, String message, Date executeDate, String processLotNo, String user) {
		try {
			SiSystemLog log = new SiSystemLog();
			log.setProcessName(processName);
			log.setLogLevel(logLevel);
			log.setMessage(message);
			log.setExecuteDate(DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			log.setProcessLotNo(processLotNo);
			log.setCreatedBy(user);
			log.setCreationDate(new Date());
			siSystemLogDAO.save(log);
		} catch (Exception ex) {
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}

	/**
	 * Create System Log 
	 * @param processName
	 * @param logLevel
	 * @param message
	 * @param executeDate
	 * @param processLotNo
	 * @param user
	 */
	public void createSystemLog(String processName, String logLevel, String message, Date executeDate, String processLotNo, String user, String brandCode) {
		try {
			SiSystemLog log = new SiSystemLog();
			log.setProcessName(processName);
			log.setLogLevel(logLevel);
			log.setMessage(message);
			log.setExecuteDate(DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			log.setProcessLotNo(processLotNo);
			log.setCreatedBy(user);
			log.setCreationDate(new Date());
			log.setBrandCode(brandCode);
			siSystemLogDAO.save(log);
		} catch (Exception ex) {
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	/**
	 * Create System Log 
	 * @param processName
	 * @param logLevel
	 * @param message
	 * @param executeDate
	 * @param processLotNo
	 * @param user
	 * @param brandCode
	 * @param action
	 * @param executeTime
	 */
	public void createSystemLog(String processName, String logLevel, String message, Date executeDate, String processLotNo, String user, String brandCode, String action, String executeTime) {
		try {
			SiSystemLog log = new SiSystemLog();
			log.setProcessName(processName);
			log.setLogLevel(logLevel);
			log.setMessage(message);
			log.setExecuteDate(DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD));
			log.setProcessLotNo(processLotNo);
			log.setCreatedBy(user);
			log.setCreationDate(new Date());
			log.setBrandCode(brandCode);
			log.setAction(action);
			log.setExecuteTime(executeTime);
			siSystemLogDAO.save(log);
		} catch (Exception ex) {
			log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
		}
	}
	
	
	/**
	 * Create System Log 
	 * @param sysLog
	 * @throws Exception
	 */
	public void createSystemLog(SiSystemLog sysLog) throws Exception {
		log.info("SiSystemLogService.createSystemLog");
		if (null != sysLog) {
			String processName = sysLog.getProcessName();
			try {
				sysLog.setCreationDate(new Date());
				siSystemLogDAO.save(sysLog);
			} catch (Exception ex) {
			    	ex.printStackTrace();
				log.error("process Name : " + processName + " 作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
				throw new Exception(processName + "作業於存入系統記錄檔失敗！");
			}
		}
	}
	
	public List<SiSystemLog> getSystemLog(String processName, String logLevel, String processLotNo){
	    return siSystemLogDAO.findByProperty("SiSystemLog", "and processName = ? and logLevel = ? and processLotNo = ?", new Object[]{processName,logLevel,processLotNo});
	}
	
	
	public void deleteSystemLog(String processName, String processLotNo){
	    List<SiSystemLog> siSystemLogs = siSystemLogDAO.findByProperty("SiSystemLog", "and processName = ? and processLotNo = ?", new Object[]{processName, processLotNo});
	    for (Iterator iterator = siSystemLogs.iterator(); iterator.hasNext();) {
		SiSystemLog siSystemLog = (SiSystemLog) iterator.next();
		siSystemLogDAO.delete(siSystemLog);
	    }
	}
	
}