package tw.com.tm.erp.hbm.dao;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.utils.DateUtils;

public class JobCheckingDAO {

    private static final Log log = LogFactory.getLog(JobCheckingDAO.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public void triggerReportJob(Date transactionDate, String programCode, Date posFinishDate, String autoJobControl, 
	    String statusFieldName, String finishDateFieldName) throws Exception {

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	boolean isEnhanceData = true;
	boolean isSPRun = false;
	try {
	    String transactionDateStr = DateUtils.format(transactionDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);
	    stmt = conn.prepareStatement("SELECT * FROM BIA.JOB_CHECKING WHERE TRANSACTION_DATE = ? AND PROGRAM_CODE = ?");
	    stmt.setDate(1, new java.sql.Date(transactionDate.getTime()));
	    stmt.setString(2, programCode);
	    rs = stmt.executeQuery();
	    if(rs != null){
		while (rs.next()) {
		    String currentStatus = rs.getString(statusFieldName);  
		    isEnhanceData = false;
		    if("R".equals(currentStatus)){
			isSPRun = true;
		    }
		    break;
		}
		StringBuffer sql = new StringBuffer();
		if(isEnhanceData){
		    sql.append("INSERT INTO BIA.JOB_CHECKING(TRANSACTION_DATE, PROGRAM_CODE, ");
		    sql.append(statusFieldName);
		    sql.append(", ");
		    sql.append(finishDateFieldName);
		    sql.append(") VALUES(?,?,?,?)");	    
		    stmt = conn.prepareStatement(sql.toString());
		    stmt.setDate(1, new java.sql.Date(transactionDate.getTime()));
		    stmt.setString(2, programCode);
		    if("Y".equals(autoJobControl)){
		        stmt.setString(3, "Y");
		    }else{
			stmt.setString(3, "M");
		    }
		    stmt.setTimestamp(4, new java.sql.Timestamp(posFinishDate.getTime()));
		    stmt.execute();
		    conn.commit();
		}else if(!isSPRun){
		    sql.append("UPDATE BIA.JOB_CHECKING SET ");
		    sql.append(statusFieldName + " = ?, ");
		    sql.append(finishDateFieldName + " = ? ");
		    sql.append("WHERE TRANSACTION_DATE = ? AND PROGRAM_CODE = ?");		    
		    stmt = conn.prepareStatement(sql.toString());
		    if("Y".equals(autoJobControl)){
		        stmt.setString(1, "Y");
		    }else{
			stmt.setString(1, "M");
		    }
		    stmt.setTimestamp(2, new java.sql.Timestamp(posFinishDate.getTime()));
		    stmt.setDate(3, new java.sql.Date(transactionDate.getTime()));
		    stmt.setString(4, programCode);
		    stmt.execute();
		    conn.commit();			
	        }
	    }else{
		throw new Exception("查詢交易日期(" + transactionDateStr + ")、程式代碼(" + programCode + ")失敗！");
	    }	    
	} catch(Exception ex){
	    if (conn != null && !conn.isClosed()) {
		conn.rollback();
	    }
	    throw new Exception(ex.getMessage());
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    log.error("關閉ResultSet時發生錯誤！");
		}
	    }
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		    log.error("關閉PreparedStatement時發生錯誤！");
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
}