package tw.com.tm.erp.hbm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.PosCommand;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SiResendId;

public class SoParallelTestDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SoParallelTestDAO.class);
	
	private DataSource dataSource;
	private DataSource dataSource98101;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setDataSource98101(DataSource dataSource98101) {
		this.dataSource98101 = dataSource98101;
	}
	
	public boolean save(PosCommand transientInstance) {
		log.info("saving PosCommand instance");
		boolean result = false;
		try {
			getHibernateTemplate().save(transientInstance);
			//log.info("save successful");
			result = true;
		} catch (RuntimeException re) {
			//log.info("save failed", re);
			result = false;
			throw re;
		} 
		return result;
	}
	
	public boolean update(PosCommand transientInstance) {
		log.debug("updating PosCommand instance");
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			//log.info("update successful");
			result = true;
		} catch (RuntimeException re) {
			//log.info("update failed", re);
			result = false;
			throw re;
		}
		return result;
	}
	
	public PosCommand findById(Long id){
			log.debug("PosCommand findById:");
			try {
				PosCommand instance = (PosCommand)getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.PosCommand", id);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
	}
	
	public void getPOSInfo(HttpServletRequest request) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		//ResultSet rst = null;
		//CallableStatement calStmt = null;
		
		try {
			Long requestId = Long.parseLong(request.getParameter("requestId")); // REQUEST 序號
			log.info("requestId=="+requestId);
			conn = dataSource.getConnection();
			String strQuery = "UPDATE pos.pos_command set reserve2 = 'R' WHERE request_id = " + requestId + " and type = 'REQ' and data_type = 'SOP' ";
			stmt = conn.createStatement();
			stmt.executeUpdate(strQuery);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			/*if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}*/
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}		
	}
	
	public List<Long> getPosCommand() throws Exception {
		
		Connection conn = null;
		
		//Class.forName("oracle.jdbc.driver.OracleDriver");
		//conn = riverManager.getConnection("jdbc:oracle:thin:@10.1.95.100:1521:KWEDB1","KWE_ERP","1QAZ8IK,!@#$");
		Statement stat = null;//conn.createStatement();
		ResultSet rs = null;

		List<Long> s = new ArrayList();
		
		try {
			conn = dataSource98101.getConnection();
			stat = conn.createStatement();

			String strQuery = "SELECT * FROM pos.pos_command WHERE 1=1 and type = 'REQ' and data_type = 'SOP' and reserve2 = 'W' order by batch_id ";

			stat = conn.createStatement();
			stat.setMaxRows(130);
			rs = stat.executeQuery(strQuery);

			while(rs.next()) {
				s.add(rs.getLong("REQUEST_ID"));
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {;
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					log.error("關閉Statement時發生錯誤！");
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
		return s;
	}
	
	public void updatePOSInfo(Long reqId, String res) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		//CallableStatement calStmt = null;
		
		try {
			Long requestId = reqId; // REQUEST 序號
			log.info("requestId=="+requestId);
			conn = dataSource98101.getConnection();
			String strQuery = "UPDATE pos.pos_command set reserve2 = '"+res+"' WHERE request_id = " + requestId + " and type = 'REQ' and data_type = 'SOP' ";
			stmt = conn.createStatement();
			stmt.executeUpdate(strQuery);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("soParallelTestDAO.updatePOSInfo has error!!!");
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			/*if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}*/
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
