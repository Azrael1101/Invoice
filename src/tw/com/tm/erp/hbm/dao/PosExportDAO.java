package tw.com.tm.erp.hbm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.PosProgramLog;
import tw.com.tm.erp.hbm.service.PosDUService;


public class PosExportDAO extends BaseDAO {
	
    private static final Log log = LogFactory.getLog(PosExportDAO.class);
	
    private DataSource dataSource = null;
    private DataSource dataSourceMySql;

    public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
    }
    
    public void setDataSourceMySql(DataSource dataSourceMySql){
    	this.dataSourceMySql = dataSourceMySql;
    }
    public String getDataId(){
    	return "";
    }
    /**
     * Call store procedure取得DATA_ID
     *
     * @param request
     * @return
     * @throws ValidationErrorException
     */
	public String getDataId(String dataBase) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		String data_id = "-1";
		try {
    	    
			if(dataBase.equals("MySql")){
    	    	
				conn = dataSourceMySql.getConnection();
    	    	log.info("取得MySql連線");    	    	
    	    	Statement st = conn.createStatement();
    	    	ResultSet rs = st.executeQuery("SELECT GET_DATA_ID()");
    	    	
    	    	while(rs.next()){    	    		
    	    		data_id = rs.getObject(1).toString();    	    		
    	    	}   	    	
    	    }else{
    	    	conn = dataSource.getConnection();
    			calStmt = conn.prepareCall("{? = call POS.GET_DATA_ID()}"); // 呼叫store procedure
    			calStmt.registerOutParameter(1, Types.VARCHAR);
    			calStmt.execute();
    			data_id = calStmt.getString(1); // 回傳新產生的 DATA_ID	
    	    }			
		} catch (SQLException e) {
			log.error("getDataId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("getDataId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("getDataId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("getDataId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("getDataId() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("getDataId() 關閉Connection時發生錯誤！");
				}
			}
		}
		return data_id;
	}

	/**
	 * Call store procedure取得REQUEST_ID
	 *
	 * @param request
	 * @return
	 * @throws ValidationErrorException
	 */
	public Long getRequestId(String dataBase) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		Long request_id = -1L;
		log.info("88888888888");
		try {
    	    if(dataBase.equals("MySql")){
    	    	conn = dataSourceMySql.getConnection();
    	    	log.info("取得MySql連線");
    	    	String req = "";
    	    	Statement st = conn.createStatement();
    	    	ResultSet rs = st.executeQuery("SELECT nextval('GET_REQUEST_ID')");
    	    	
    	    	while(rs.next()){
    	    		
    	    		req = rs.getObject(1).toString();
    	    		request_id = Long.parseLong(req);
    	    	}   	    	
    	    }else{
    	    	conn = dataSource.getConnection();
    	    	log.info("取得Oracle連線");
    	    	calStmt = conn.prepareCall("{? = call POS.GET_REQUEST_ID()}"); // 呼叫store procedure
    	    	calStmt.registerOutParameter(1, Types.BIGINT);
    			calStmt.execute();
    			request_id = calStmt.getLong(1); // 回傳新產生的 REQUEST_ID    			
    	    }
			
		} catch (SQLException e) {
			log.error("getRequestId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("getRequestId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("getRequestId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("getRequestId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("getRequestId() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("getRequestId() 關閉Connection時發生錯誤！");
				}
			}
		}
		return request_id;
	}

	/**
	 * Call store procedure取得RESPONSE_ID
	 *
	 * @param request
	 * @return
	 * @throws ValidationErrorException
	 */
	public Long getResponseId() throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		Long response_id = -1L;
		try {
			conn = dataSource.getConnection();
			//System.out.println("==== 取回RESPONSE_ID ====");
			calStmt = conn.prepareCall("{? = call POS.GET_RESPONSE_ID()}"); // 呼叫store procedure
			calStmt.registerOutParameter(1, Types.BIGINT);
			//System.out.println("==== call POS.GET_RESPONSE_ID() ====");
			calStmt.execute();
			response_id = calStmt.getLong(1); // 回傳新產生的 RESPONSE_ID
			//System.out.println("==== 呼叫資料庫procedure成功！ 取得RESPONSE_ID : " + response_id + " ====");
		} catch (SQLException e) {
			log.error("getResponseId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("getResponseId() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("getResponseId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("getResponseId() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("getResponseId() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("getResponseId() 關閉Connection時發生錯誤！");
				}
			}
		}
		return response_id;
	}
	
	/**
	 * 提供POS機端更新完資料後，調整POS COMMAND的狀態
	 *
	 * @param request
	 * @throws ValidationErrorException
	 */
	public void updateWaiting(Map parameterMap) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		String dataBase = (String)parameterMap.get("DATA_BASE");
		PreparedStatement stmt = null;
		try {
			if(dataBase.equals("MySql")){
				log.info("取得MySql---updateWaiting");				
				String sql = "SELECT POS.WAITING_COMMAND(?,?)";				
				Long batch_Id =  (Long)parameterMap.get("BATCH_ID");
				String company =  (String)parameterMap.get("COMPANY");				
				conn = dataSourceMySql.getConnection();
				stmt = conn.prepareStatement(sql);
				stmt.setLong(1, batch_Id);
				stmt.setString(2, company);
				stmt.execute();				
			}else{
				conn = dataSource.getConnection();
				Long batch_Id =  (Long)parameterMap.get("BATCH_ID");
				String company =  (String)parameterMap.get("COMPANY");
				calStmt = conn.prepareCall("{call POS.POS_TRANS_SP.WAITING_COMMAND(?,?)}"); // 呼叫store procedure
				calStmt.setLong(1, batch_Id);
				calStmt.setString(2, company);
				calStmt.execute();
			}
		} catch (SQLException e) {
			log.error("updateWaiting() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("updateWaiting() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("updateWaiting() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("updateWaiting() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("updateWaiting() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("updateWaiting() 關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
	/**
	 * 提供POS機端更新完資料後，更新POS COMMAND的狀態
	 *
	 * @param request
	 * @throws ValidationErrorException
	 */
	public void updateComplete(Map parameterMap) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		String dataBase = (String)parameterMap.get("DATA_BASE");
		PreparedStatement stmt = null;		
		try {
			if(dataBase.equals("MySql")){
				log.info("取得MySql---updateComplete");				
				String sql = "SELECT pos.COMPLETE_COMMAND(?,?)";				
				Long batch_Id =  (Long)parameterMap.get("BATCH_ID");
				String company =  (String)parameterMap.get("COMPANY");				
				conn = dataSourceMySql.getConnection();
				stmt = conn.prepareStatement(sql);
				stmt.setLong(1, batch_Id);
				stmt.setString(2, company);
				stmt.execute();
			}else{
				conn = dataSource.getConnection();
				Long batch_Id =  (Long)parameterMap.get("BATCH_ID");
				String company =  (String)parameterMap.get("COMPANY");
				calStmt = conn.prepareCall("{call POS.POS_TRANS_SP.COMPLETE_COMMAND(?,?)}"); // 呼叫store procedure
				calStmt.setLong(1, batch_Id);
				calStmt.setString(2, company);
				calStmt.execute();
			}
		} catch (SQLException e) {
			log.error("updateComplete() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("updateComplete() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("updateComplete() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("updateComplete() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("updateComplete() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("updateComplete() 關閉Connection時發生錯誤！");
				}
			}
		}
	}

    /**
	 * 製作POS_COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public void createPosCommand(Map parameterMap) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			//製作POS_COMMAND
			calStmt = conn.prepareCall("{call POS.POS_TRANS_SP.POS_COMMAND_GEN(?,?,?,?,?,?,?,?,?,?,?)}"); // 呼叫store procedure
			
			log.info("TYPE = " + parameterMap.get("TYPE"));
			calStmt.setString(1, (String)parameterMap.get("TYPE"));
			
			log.info("REQUEST_ID = " + parameterMap.get("REQUEST_ID"));
			if(null == parameterMap.get("REQUEST_ID"))
				calStmt.setNull(2, Types.NULL);
			else
				calStmt.setLong(2, (Long)parameterMap.get("REQUEST_ID"));
			
			log.info("RESPONSE_ID = " + parameterMap.get("RESPONSE_ID"));
			if(null == parameterMap.get("RESPONSE_ID"))
				calStmt.setNull(3, Types.NULL);
			else
				calStmt.setLong(3, (Long)parameterMap.get("RESPONSE_ID"));
			
			log.info("BRAND_CODE = " + parameterMap.get("BRAND_CODE"));
			calStmt.setString(4, (String)parameterMap.get("BRAND_CODE"));
			
			log.info("ACTION = " + parameterMap.get("ACTION"));
			calStmt.setString(5, (String)parameterMap.get("ACTION"));
			
			log.info("DATA_TYPE = " + parameterMap.get("DATA_TYPE"));
			calStmt.setString(6, (String)parameterMap.get("DATA_TYPE"));
			
			log.info("DATA_ID = " + parameterMap.get("DATA_ID"));
			calStmt.setString(7, (String)parameterMap.get("DATA_ID"));
			
			log.info("OPERATION = " + parameterMap.get("OPERATION"));
			calStmt.setString(8, (String)parameterMap.get("OPERATION"));
			
			log.info("MACHINE_CODE = " + parameterMap.get("MACHINE_CODE"));
			calStmt.setString(9, (String)parameterMap.get("MACHINE_CODE"));
			
			log.info("NUMBERS = " + parameterMap.get("NUMBERS"));
			calStmt.setLong(10, (Long)parameterMap.get("NUMBERS"));
			
			log.info("COMPANY = " + parameterMap.get("COMPANY"));
			calStmt.setString(11, (String)parameterMap.get("COMPANY"));
			
			calStmt.execute();
		}catch (SQLException e) {
			log.error("createPosCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("createPosCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("createPosCommand() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("createPosCommand() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("createPosCommand() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("createPosCommand() 關閉Connection時發生錯誤！");
				}
			}
		}
	}

	/**
	 * 製作POS_COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public void createErpCommand(Map parameterMap) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			//製作ERP_COMMAND
			calStmt = conn.prepareCall("{call POS.ERP_EXPORT_SP.DISTRIBUTE_ERP_COMMAND(?,?,?,?,?,?)}"); // 呼叫store procedure
			calStmt.setString(1, (String)parameterMap.get("BRAND_CODE"));
			calStmt.setString(2, (String)parameterMap.get("DATA_TYPE"));
			calStmt.setString(3, (String)parameterMap.get("DATA_ID"));
			calStmt.setString(4, (String)parameterMap.get("SHOP_CODE"));
			calStmt.setString(5, (String)parameterMap.get("MACHINE_CODE"));
			calStmt.setString(6, (String)parameterMap.get("COMPANY"));
			calStmt.execute();
		}catch (SQLException e) {
			log.error("createErpCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("createErpCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("createErpCommand() 呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw new Exception("createErpCommand() 呼叫SQL發生錯誤，原因：" + ex.getMessage());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("createErpCommand() 關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("createErpCommand() 關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
	/**
	 * 複製ERP_COMMAND 到 POS_COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public Long createE2PCommand(Map parameterMap) throws Exception {
		CallableStatement calStmt = null;
		Connection conn = null;
		Long response_id = -1L;
		try {
			conn = dataSource.getConnection();
			//依照一個POS的request把預先下傳的response寫到POS_COMMAND中
			calStmt = conn.prepareCall("{?=call POS.POS_TRANS_SP.E2P_COMMAND(?,?,?,?,?,?,?,?)}"); // 呼叫store procedure
			calStmt.registerOutParameter(1, Types.BIGINT);
			calStmt.setLong(2, (Long)parameterMap.get("BATCH_ID"));
			calStmt.setString(3, (String)parameterMap.get("BRAND_CODE"));
			calStmt.setLong(4, (Long)parameterMap.get("REQUEST_ID"));
			calStmt.setString(5, (String)parameterMap.get("DATA_TYPE"));
			calStmt.setString(6, (String)parameterMap.get("OPERATION"));
			calStmt.setString(7, (String)parameterMap.get("DATA_ID"));
			calStmt.setString(8, (String)parameterMap.get("MACHINE_CODE"));
			calStmt.setString(9, (String)parameterMap.get("COMPANY"));
			calStmt.execute();
			response_id = calStmt.getLong(1); // 回傳新產生的 RESPONSE_ID
		}catch (SQLException e) {
			log.error("createE2PCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw new Exception("createE2PCommand() 呼叫SQL發生錯誤，原因：" + e.getMessage());
		} catch (Exception ex) {
			log.error("createE2PCommand() 呼叫createE2PCommand發生錯誤，原因：" + ex.getMessage());
			throw new Exception("createE2PCommand() 呼叫SQL發生錯誤，原因：" + ex.getMessage());
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
		return response_id;
	}
	
	/**
	 * 建立POS下的COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public Long executeCommand(HashMap parameterMap) throws Exception {
    	Long responseId = 0L;
    	Long batchId = (Long)parameterMap.get("BATCH_ID");
    	Long numbers = (Long)parameterMap.get("NUMBERS");
    	//依照batchID來判斷是寫入POS_COMMAND 或是 ERP_COMMAND
		if(null == batchId || batchId <= 0){
			//排程下資料必須寫入ERP_COMMAND
			//log.info("posExportDAO.createErpCommand");
		    if(!"noData".equals(parameterMap.get("searchResult"))){
			createErpCommand(parameterMap);
		    }
		}else{
			//即時回傳資料，必須補一筆POS_COMMAND
			//log.info("posExportDAO.createPosCommand");
	    	PosDUService.setResponseCommand(parameterMap);
	    	responseId = getResponseId(); // 產生RESPONSE_ID
			parameterMap.put("RESPONSE_ID", responseId);
			parameterMap.put("NUMBERS", numbers); // 產生NUMBERS
			//response資料寫入POS_COMMAND
			createPosCommand(parameterMap);
		}
		return responseId;
	}
	
	/**
	 * 建立POS下的ProgramLog
	 *
	 * @param
	 * @return
	 * @throws
	 */
	public void createProgramLog(String programId, String levelType, String identification, String ip, String message, String uuId, String dataId, String createdBy) {
		PosProgramLog posProgramLog = new PosProgramLog();
		posProgramLog.setProgramId(programId);
		posProgramLog.setLevelType(levelType);
		posProgramLog.setIdentification(identification);
		posProgramLog.setIp(ip);
		posProgramLog.setMessage(message);
		posProgramLog.setUuId(uuId);
		posProgramLog.setDataId(dataId);
		posProgramLog.setCreatedBy(createdBy);
		posProgramLog.setCreationDate(new java.util.Date());
		this.save(posProgramLog);
	}
	
	/*
	 * 取得該data_id的所有Item
	 * 
	 *
	 */
	public List findByDataId(final String dataId) throws Exception{	    
		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

//				StringBuffer sql = new StringBuffer("SELECT ITEM_CODE FROM POS.POS_ITEM ");
//				sql.append(" WHERE DATA_ID =  '");
//				sql.append(dataId);
//				sql.append("'");
			    	StringBuffer hql = new StringBuffer("from PosItem where dataId = :dataId ");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("dataId", dataId);
				return query.list();
			}
		});
		for(int i=0;i<re.size();i++){
		    System.out.println("============"+re.get(i));
		}
	    return re;
	}
}