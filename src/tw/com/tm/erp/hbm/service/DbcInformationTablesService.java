package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.DbcInformationColumns;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.DbcInformationColumnsDAO;
import tw.com.tm.erp.hbm.dao.DbcInformationTablesDAO;
import tw.com.tm.erp.hbm.dao.OmmChannelTXFHeadDAO;

public class DbcInformationTablesService {
	
private static final Log log = LogFactory.getLog(DbcInformationTablesService.class);
	
	// spring IOC DAO
	private DbcInformationTablesDAO dbcInformationTablesDAO;
	private DbcInformationColumnsDAO dbcInformationColumnsDAO;
	
	public void setDbcInformationColumnsDAO(DbcInformationColumnsDAO dbcInformationColumnsDAO) {
		this.dbcInformationColumnsDAO = dbcInformationColumnsDAO;
	}

	public void setDbcInformationTablesDAO(DbcInformationTablesDAO dbcInformationTablesDAO) {
		this.dbcInformationTablesDAO = dbcInformationTablesDAO;
	}
	
	

	/**
	 * 依據表單名稱查詢欄位資訊主檔
	 *
	 * @param tableName
	 * @return ImMovementHead
	 * @throws Exception
	 */
	public DbcInformationTables findDbcInformationTablesByIdentification(String tableName) throws Exception {
		try {
			return dbcInformationTablesDAO.findDbcInformationTablesByIdentification(tableName);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("依據表單名稱：" + tableName + "查詢表格欄位資訊時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據表單名稱：" + tableName + "查詢表格欄位資訊時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public List<DbcInformationColumns> findPageLine(String tableName, int iSPage, int iPSize){
		return dbcInformationColumnsDAO.findPageLine(tableName, iSPage, iPSize);
	}
	
	public Long findPageLineMaxIndex(String tableName) {
		return dbcInformationColumnsDAO.findPageLineMaxIndex(tableName);
	}
	
//	public List<DbcInformationColumns> findDbcInformationColumnsByTableName(String tableName) throws Exception {
//		try {
//			return dbcInformationColumnsDAO.findDbcInformationColumnsByTableName(tableName);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			log.error("依據表單名稱：" + tableName + "查詢表格欄位資訊時發生錯誤，原因：" + ex.toString());
//			throw new Exception("依據表單名稱：" + tableName + "查詢表格欄位資訊時發生錯誤，原因：" + ex.getMessage());
//		}
//	}

}
