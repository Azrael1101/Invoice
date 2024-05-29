package tw.com.tm.erp.hbm.dao;

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AppSoPostingDAO {

    private static final Log log = LogFactory.getLog(AppSoPostingDAO.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public void execPosting(String brandCode, String orderTypeCode, String salesUnit, String transactionDate, String opUser, String batch) throws Exception {

	Connection conn = null;
	CallableStatement calStmt = null;
	try {
		log.info("execPosting"+batch);
	    conn = dataSource.getConnection();
	    calStmt = conn
		    .prepareCall("{call ERP.APP_SO_POSTING_PACKAGE.POSTING(?,?,?,?,?,?)}");
	    calStmt.setString(1, brandCode);
	    calStmt.setString(2, orderTypeCode);
	    calStmt.setString(3, salesUnit);
	    calStmt.setString(4, transactionDate);
	    calStmt.setString(5, opUser);
	    calStmt.setString(6, batch);
	    calStmt.execute();
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
}