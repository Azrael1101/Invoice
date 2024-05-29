package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.SiProgramLog;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;

public class SiProgramLogService {

    private static final Log log = LogFactory.getLog(SiProgramLogService.class);

    private SiProgramLogDAO siProgramLogDAO;

    /* Spring IoC */

    public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
	this.siProgramLogDAO = siProgramLogDAO;
    }

    /**
     * Create Program Log
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @param message
     * @param createdBy
     * @throws Exception
     */
    public void createProgramLog(String programId, String levelType,
	    String identification, String message, String createdBy)
	    throws Exception {

	SiProgramLog prgLog = new SiProgramLog();
	prgLog.setProgramId(programId);
	prgLog.setLevelType(levelType);
	prgLog.setIdentification(identification);
	prgLog.setMessage(message);
	prgLog.setCreatedBy(createdBy);
	prgLog.setCreationDate(new Date());
	siProgramLogDAO.save(prgLog);
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
     * @throws Exception
     */
    public void createProgramLog(String programId, String levelType,
	    String identification, Long processId, Long activityId,
	    String message, String createdBy) throws Exception {

	SiProgramLog prgLog = new SiProgramLog();
	prgLog.setProgramId(programId);
	prgLog.setLevelType(levelType);
	prgLog.setIdentification(identification);
	prgLog.setProcessId(processId);
	prgLog.setActivityId(activityId);
	prgLog.setMessage(message);
	prgLog.setCreatedBy(createdBy);
	prgLog.setCreationDate(new Date());
	siProgramLogDAO.save(prgLog);
    }

    /**
     * Create Program Log(bean)
     * 
     * @param prgLog
     * @throws Exception
     */
    public void createProgramLog(SiProgramLog prgLog) throws Exception {

	prgLog.setCreationDate(new Date());
	siProgramLogDAO.save(prgLog);
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

	List<SiProgramLog> programLogs = siProgramLogDAO.findByIdentification(
		programId, levelType, identification);
	if (programLogs != null && programLogs.size() > 0) {
	    for (int i = 0; i < programLogs.size(); i++) {
		SiProgramLog programLog = (SiProgramLog) programLogs.get(i);
		siProgramLogDAO.delete(programLog);
	    }
	}
    }
    
    public List<SiProgramLog> findByIdentification(Map parameterMap) throws Exception{
        
        try{  	    
    	    String programId = (String)parameterMap.get("programId");
    	    String levelType = (String)parameterMap.get("levelType");
    	    String identification = (String)parameterMap.get("identification");
    	    List<SiProgramLog> programLogs = siProgramLogDAO.findByIdentification(
	            programId, levelType, identification); 	    
		
	    return programLogs;       	
        }catch (Exception ex) {
	    log.error("查詢系統程式記錄檔失敗，原因：" + ex.toString());
	    throw new Exception("查詢系統程式記錄檔失敗，原因：" + ex.getMessage());
	}           
    }

    public List<SiProgramLog> findByIdentification(String programId, String levelType, String identification)
    	throws Exception{
        try{  	    
    	    List<SiProgramLog> programLogs = siProgramLogDAO.findByIdentification(
	            programId, levelType, identification); 	    
	    return programLogs;       	
        }catch (Exception ex) {
	    log.error("查詢系統程式記錄檔失敗，原因：" + ex.toString());
	    throw new Exception("查詢系統程式記錄檔失敗，原因：" + ex.getMessage());
	}           
    }
    
}
