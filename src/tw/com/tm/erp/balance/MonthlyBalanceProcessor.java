package tw.com.tm.erp.balance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MonthlyBalanceProcessor {

    private static final Log log = LogFactory.getLog(MonthlyBalanceProcessor.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }
  
    public void execBalanceTask(HashMap conditionMap) throws Exception {

	Connection conn = null;
	CallableStatement calStmt = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String brandCode = (String) conditionMap.get("brandCode");
//	Date balanceDate = (Date) conditionMap.get("balanceDate");
	String execBalanceYear = (String) conditionMap.get("execBalanceYear");//YYYY
	String execBalanceMonth = (String) conditionMap.get("execBalanceMonth");//MM
	String execOperator = (String) conditionMap.get("execOperator");  //Operator
    StringBuffer sqlString = new StringBuffer();
	
	try {
	    System.out.println("=========BEGIN========");
	    conn = dataSource.getConnection();
	    //===================DELETE IM_MONTHLY_BALANCE_LINE======================
//	    sqlString.append("DELETE FROM ERP.IM_MONTHLY_BALANCE_LINE WHERE BRAND_CODE = ? and YEAR = ? and MONTH = ?");	   
//	    stmt = conn.prepareStatement(sqlString.toString());
//	    stmt.setString(1, brandCode);
//	    stmt.setString(2, execBalanceYear);
//	    stmt.setString(3, execBalanceMonth);   
//	    stmt.execute();
	    //===================DELETE IM_MONTHLY_BALANCE_HEAD======================
//	    sqlString.delete(0, sqlString.length());
//	    sqlString.append("DELETE FROM ERP.IM_MONTHLY_BALANCE_HEAD WHERE BRAND_CODE = ? and YEAR = ? and MONTH = ?");	   
//	    stmt = conn.prepareStatement(sqlString.toString());
//	    stmt.setString(1, brandCode);
//	    stmt.setString(2, execBalanceYear);
//	    stmt.setString(3, execBalanceMonth);   
//	    stmt.execute();
	    //===================CALL APP_STOCK_STATISTIC_PACKAGE.REPORT_START======================
//	    sqlString.delete(0, sqlString.length());
	    sqlString.append("{call ERP.APP_MONTHLY_BALANCE_GENERATOR.STARTING(?,?,?,?)}");
	    calStmt = conn.prepareCall(sqlString.toString());
//            calStmt.registerOutParameter(1, OracleTypes.CURSOR);
            calStmt.setString(1, brandCode);
            calStmt.setString(2, execBalanceYear);
            calStmt.setString(3, execBalanceMonth);
            calStmt.setString(4, execOperator);
//            calStmt.setString(4, null);
            calStmt.executeQuery();
            
	    System.out.println("=========END========");
	    
	    
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    log.error("關閉ResultSet時發生錯誤！");
		}
	    }
	    if (calStmt != null) {
		try {
		    calStmt.close();
		} catch (SQLException e) {
		    log.error("關閉CallableStatement時發生錯誤！");
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
