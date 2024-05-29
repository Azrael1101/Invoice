package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiProgramLog;
import tw.com.tm.erp.hbm.service.SiProgramLogService;
import tw.com.tm.erp.utils.AjaxUtils;

public class SiProgramLogAction {

    private static final Log log = LogFactory.getLog(SiProgramLogAction.class);

    private SiProgramLogService siProgramLogService;

    /* Spring IoC */

    public void setSiProgramLogService(SiProgramLogService siProgramLogService) {
	this.siProgramLogService = siProgramLogService;
    }

    /**
     * Create Program Log
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @param message
     * @param createdBy
     */
    public void createProgramLog(String programId, String levelType,
	    String identification, String message, String createdBy) {

	try {
	    siProgramLogService.createProgramLog(programId, levelType,
		    identification, message, createdBy);
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    log.error(programId + "程式的識別碼：" + identification
		    + "於寫入系統程式記錄檔時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * Create Program Log
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @param processId
     * @param activityId
     * @param message
     * @param createdBy
     */
    public void createProgramLog(String programId, String levelType,
	    String identification, Long processId, Long activityId,
	    String message, String createdBy) {

	try {
	    siProgramLogService.createProgramLog(programId, levelType,
		    identification, processId, activityId, message, createdBy);
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    log.error(programId + "程式的識別碼：" + identification
		    + "於寫入系統程式記錄檔時發生錯誤，原因：" + ex.toString());
	}
    }

    /**
     * Create Program Log(bean)
     * 
     * @param prgLog
     * @throws Exception
     */
    public void createProgramLog(SiProgramLog prgLog) throws Exception {

	String programId = prgLog.getProgramId();
	String identification = prgLog.getIdentification();
	try {
	    siProgramLogService.createProgramLog(prgLog);
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    log.error(programId + "程式的識別碼：" + identification
		    + "於寫入系統程式記錄檔時發生錯誤，原因：" + ex.toString());
	}
    }
    
    /**
     * delete Program Log
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @throws Exception
     */
    public void deleteProgramLog(String programId, String levelType,
	    String identification) throws Exception{
	
	try {
	    siProgramLogService.deleteProgramLog(programId, levelType, identification);
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    log.error(programId + "程式的識別碼：" + identification
		    + "自系統程式記錄檔刪除時發生錯誤，原因：" + ex.toString());
	}	
    }
    
    /**
     * find the SiprogramLog List
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @throws Exception
     */  
    public List<SiProgramLog> findByIdentification(String programId, String levelType, String identification)
		throws Exception{
        try{  	    
            return siProgramLogService.findByIdentification(programId, levelType, identification);  	
        }catch (Exception ex) {
            log.error("查詢系統程式記錄檔失敗，原因：" + ex.toString());
            throw new Exception("查詢系統程式記錄檔失敗，原因：" + ex.getMessage());
        }           
    }
    
}
