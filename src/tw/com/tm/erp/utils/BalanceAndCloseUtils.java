package tw.com.tm.erp.utils;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.SiSystemLogService;

public class BalanceAndCloseUtils {

    private static final Log log = LogFactory
	    .getLog(BalanceAndCloseUtils.class);

    private static ApplicationContext context = SpringUtils
	    .getApplicationContext();

    public static void createSystemLog(String processName, String logLevel,
	    String message, Date executeDate, String processLotNo, String user) {

	try {
	    SiSystemLogService logService = (SiSystemLogService) context
		    .getBean("siSystemLogService");
	    logService.createSystemLog(processName, logLevel, message,
		    executeDate, processLotNo, user);
	} catch (Exception ex) {
	    log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
	}
    }
    
    public static void createSystemLog(String processName, String logLevel,
    	    String message, Date executeDate, String processLotNo, String user, String brandCode, String action, String executeTime) {
    	try {
    	    SiSystemLogService logService = (SiSystemLogService) context
    		    .getBean("siSystemLogService");
    	    logService.createSystemLog(processName, logLevel, message,
    		    executeDate, processLotNo, user, brandCode, action, executeTime);
    	} catch (Exception ex) {
    	    log.error(processName + "作業於存入系統記錄檔時發生錯誤，原因：" + ex.toString());
    	}
    }
}
