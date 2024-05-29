package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class CeapProcessDAO extends BaseDAO {

    private static final Log log = LogFactory
	    .getLog(CeapProcessDAO.class);

    private  DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }
    /*
    public  void updateCeapProcessSubject(Long processId, String subject) throws Exception {
	Connection conn = null;
	CallableStatement calStmt = null;
	try {
        log.info("UpdateCeapProcessSubject:"+processId +" Subject:"+subject);   
      
	    conn = dataSource.getConnection();
	    calStmt = conn.prepareCall("{call ERP.SET_CEAP_PROCESS_SUBJECT(?,?)}");
	    calStmt.setLong(1, processId);
	    calStmt.setString(2, subject); 
	    calStmt.executeQuery();
	    
	    log.info("UpdateCeapProcessSubject Finish...");   
	} catch (Exception ex) {
	    log.error("更新CEAP主題時發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException(ex.toString());
	} finally {
	    if (calStmt != null) {
	    	try {
	    		calStmt.close();
	    	} catch (SQLException e) {
	    		log.error("關閉CallableStatement時發生錯誤！");
	    	}
	    }
	    if (conn != null) {
	    	try {
	    		conn.close();
	    	} catch (SQLException e) {
	    		log.error("關閉Connection時發生錯誤！");
	    	}
	    }
	}
    }
    */
    public void updateCeapProcessSubject(final Long processId, final String subject){
    	if (null != processId){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		    	String processString = "UPDATE CEAP.TBCN_WF_PROCESS SET SUBJECT = :subject WHERE PROCESS_ID = :processId";
		    		Query  queryProcess = session.createSQLQuery(processString);
		    		queryProcess.setString("subject", subject);
		    		queryProcess.setLong("processId", processId);
		    		queryProcess.executeUpdate();
		    		
		    		String assignmentString = "UPDATE CEAP.TBCN_WF_ASSIGNMENT SET SUBJECT = :subject WHERE PROCESS_ID = :processId";
		    		Query  queryAssignment = session.createSQLQuery(assignmentString);
		    		queryAssignment.setString("subject", subject);
		    		queryAssignment.setLong("processId", processId);
		    		return queryAssignment.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
   
}