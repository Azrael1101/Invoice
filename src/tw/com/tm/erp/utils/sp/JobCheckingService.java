package tw.com.tm.erp.utils.sp;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.balance.DailyBalanceManager;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.dao.JobCheckingDAO;
import tw.com.tm.erp.utils.SiSystemLogUtils;

/**
 * @author T15394
 *
 */
/**
 * @author T15394
 *
 */
public class JobCheckingService {

    private static final Log log = LogFactory.getLog(JobCheckingService.class);
    
    private Properties config = new Properties();
    
    private static final String CONFIG_FILE = "/t2PosGroupMapping.properties";
    
    private final String POS_STATUS = "_STATUS";
    
    private final String POS_FINISH_DATE = "_FINISH_DATE";
    
    
    private JobCheckingDAO jobCheckingDAO;
    
    public void setJobCheckingDAO(JobCheckingDAO jobCheckingDAO) {
	this.jobCheckingDAO = jobCheckingDAO;
    }
    
    public void triggerReportJob(Date transactionDate, String programCode, Date posFinishDate, String processName, Date executeDate, String processLotNo,
		String user, String transferFolder, String autoJobControl){
        String msg = null;
        String statusFieldName = null;
        String finishDateFieldName = null;
	try {
	    loadConfig();
	    statusFieldName = config.getProperty(transferFolder + POS_STATUS);
	    finishDateFieldName = config.getProperty(transferFolder + POS_FINISH_DATE);
	    doValidate(transferFolder + POS_STATUS, statusFieldName);
	    doValidate(transferFolder + POS_FINISH_DATE, finishDateFieldName);
	    jobCheckingDAO.triggerReportJob(transactionDate, programCode, posFinishDate, autoJobControl, statusFieldName, finishDateFieldName);
	} catch (Exception ex) {
	    msg = "寫入JOB_CHECKING時發生錯誤，原因：" + ex.toString();
	    log.error(msg);
	    SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR,  msg, executeDate, processLotNo, user);
	}
    }
    
    public void loadConfig() throws Exception {
	try {
	    config.load(DailyBalanceManager.class
		    .getResourceAsStream(CONFIG_FILE));
	} catch (IOException ex) {
	    throw new Exception("讀取T2POS群組對應檔失敗！");
	}
    }
    
    private void doValidate(String key, String value) throws Exception {
        if(!StringUtils.hasText(value)){
	    throw new Exception("查無(" + key + ")相對應的欄位值！" );
	}
    }
}
