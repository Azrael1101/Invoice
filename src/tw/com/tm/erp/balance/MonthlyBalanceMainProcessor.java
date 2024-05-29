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

public class MonthlyBalanceMainProcessor {

	private static final Log log = LogFactory.getLog(MonthlyBalanceMainProcessor.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void execBalanceTask(HashMap conditionMap) throws Exception {
		log.info("execBalanceTask");
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String brandCode = (String) conditionMap.get("brandCode");
		String execBalanceYear = (String) conditionMap.get("execBalanceYear");//YYYY
		String execBalanceMonth = (String) conditionMap.get("execBalanceMonth");//MM
		String execOperator = (String) conditionMap.get("execOperator");  //Operator
		String execMode = (String) conditionMap.get("execMode");  //Operator
		StringBuffer sqlString = new StringBuffer();
		log.info("brandCode = " + brandCode);
		log.info("execBalanceYear = " + execBalanceYear);
		log.info("execBalanceMonth = " + execBalanceMonth);
		log.info("execOperator = " + execOperator);
		log.info("execMode = " + execMode);
		try {
			System.out.println("=========BEGIN========");
			conn = dataSource.getConnection();
			//===================DELETE IM_MONTHLY_BALANCE_LINE======================
//			sqlString.append("DELETE FROM ERP.IM_MONTHLY_BALANCE_LINE WHERE BRAND_CODE = ? and YEAR = ? and MONTH = ?");	   
//			stmt = conn.prepareStatement(sqlString.toString());
//			stmt.setString(1, brandCode);
//			stmt.setString(2, execBalanceYear);
//			stmt.setString(3, execBalanceMonth);   
//			stmt.execute();
			//===================DELETE IM_MONTHLY_BALANCE_HEAD======================
//			sqlString.delete(0, sqlString.length());
//			sqlString.append("DELETE FROM ERP.IM_MONTHLY_BALANCE_HEAD WHERE BRAND_CODE = ? and YEAR = ? and MONTH = ?");	   
//			stmt = conn.prepareStatement(sqlString.toString());
//			stmt.setString(1, brandCode);
//			stmt.setString(2, execBalanceYear);
//			stmt.setString(3, execBalanceMonth);   
//			stmt.execute();
			//===================CALL APP_STOCK_STATISTIC_PACKAGE.REPORT_START======================
//			sqlString.delete(0, sqlString.length());
			sqlString.append("{call ERP.APP_MONTHLY_BALANCE_GEN_T2.Starting(?,?,?,?,?)}");
			calStmt = conn.prepareCall(sqlString.toString());
			calStmt.setString(1, brandCode);
			calStmt.setString(2, execBalanceYear);
			calStmt.setString(3, execBalanceMonth);
			calStmt.setString(4, execOperator);
			calStmt.setString(5, execMode);
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

	public void execBalanceTaskCM(HashMap conditionMap) throws Exception {
		log.info("execBalanceTaskCM");
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String brandCode = (String) conditionMap.get("brandCode");
		String execBalanceYear = (String) conditionMap.get("execBalanceYear");//YYYY
		String execBalanceMonth = (String) conditionMap.get("execBalanceMonth");//MM
		String execOperator = (String) conditionMap.get("execOperator");  //Operator
		StringBuffer sqlString = new StringBuffer();
		log.info("brandCode = " + brandCode);
		log.info("execBalanceYear = " + execBalanceYear);
		log.info("execBalanceMonth = " + execBalanceMonth);
		log.info("execOperator = " + execOperator);
		try {
			System.out.println("=========BEGIN========");
			conn = dataSource.getConnection();
			sqlString.append("{call ERP.APP_CM_MONTHLY_BALANCE_GEN.Starting(?,?,?,?)}");
			calStmt = conn.prepareCall(sqlString.toString());
			calStmt.setString(1, brandCode);
			calStmt.setString(2, execBalanceYear);
			calStmt.setString(3, execBalanceMonth);
			calStmt.setString(4, execOperator);
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
