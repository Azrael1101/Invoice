package tw.com.tm.erp.hbm.dao;

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.TmpAppStockStatistics;
import tw.com.tm.erp.hbm.view.Barcode;

public class TmpAppStockStatisticsDAO {

	private static final Log log = LogFactory.getLog(TmpAppStockStatisticsDAO.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List getStockStatistics(HashMap conditionMap) throws Exception {
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String brandCode = (String) conditionMap.get("brandCode");
		String onHandDate = (String) conditionMap.get("onHandDate");
		String warehouseCode = (String) conditionMap.get("warehouseCode");
		String overZero = (String) conditionMap.get("overZero");
		List tmpAppStockStatisticsBeans = null;
		try {
			conn = dataSource.getConnection();
			//如果overZero為Y，代表要查的現有庫存必須為非0
			if("Y".equals(overZero))
				calStmt = conn.prepareCall("{call ERP.RPT_IM0410_PACKAGE.STARTING_OVER_ZERO(?,?,?,?)}");
			else
				calStmt = conn.prepareCall("{call ERP.RPT_IM0410_PACKAGE.STARTING(?,?,?,?)}");
			calStmt.registerOutParameter(1, OracleTypes.CURSOR);
			calStmt.setString(2, brandCode);
			calStmt.setString(3, onHandDate);
			calStmt.setString(4, warehouseCode);
			calStmt.executeQuery();
			if("Y".equals(overZero))
				stmt = conn.prepareStatement("select * from ERP.TMP_RPT_IM0410 where END_ON_HAND_QTY <> 0 order by item_Code");
			else
				stmt = conn.prepareStatement("select * from ERP.TMP_RPT_IM0410 order by item_Code");
			rs = stmt.executeQuery();	    
			if(rs != null){
				tmpAppStockStatisticsBeans = produceTmpAppStockStatisticsBeans(rs);
			}
			return tmpAppStockStatisticsBeans;
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

	/**
	 * 將TmpAppStockStatistic資料庫的資料放進bean
	 * 
	 * @param rs
	 * @return List
	 * @throws Exception
	 */
	private List produceTmpAppStockStatisticsBeans(ResultSet rs) throws Exception {
		List assembly = new ArrayList(0);
		while (rs.next()) {
			String brandCode = rs.getString("BRAND_CODE");
			String itemCode = rs.getString("ITEM_CODE");
			String warehouseCode = rs.getString("WAREHOUSE_CODE");
			Double endOnHandQty = rs.getDouble("END_ON_HAND_QTY");
			Double unitPrice = rs.getDouble("UNIT_PRICE");
			String itemName = rs.getString("ITEM_NAME");
			TmpAppStockStatistics stockStatistic = new TmpAppStockStatistics();
			stockStatistic.setBrandCode(brandCode);
			stockStatistic.setItemCode(itemCode);
			stockStatistic.setWarehouseCode(warehouseCode);
			stockStatistic.setEndOnHandQty(endOnHandQty);
			stockStatistic.setUnitPrice(unitPrice);
			stockStatistic.setItemName(itemName);
			assembly.add(stockStatistic);
		}
		return assembly;
	}
	
	public List getStockStatisticsForBarcode(HashMap conditionMap) throws Exception {
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String brandCode = (String) conditionMap.get("brandCode");
		String onHandDate = (String) conditionMap.get("onHandDate");
		String warehouseCode = (String) conditionMap.get("warehouseCode");
		String overZero = (String) conditionMap.get("overZero");
		List barcodes = new ArrayList(0);
		try {
			conn = dataSource.getConnection();
			//如果overZero為Y，代表要查的現有庫存必須為非0
			if("Y".equals(overZero))
				calStmt = conn.prepareCall("{call ERP.RPT_IM0410_PACKAGE.STARTING_OVER_ZERO(?,?,?,?)}");
			else
				calStmt = conn.prepareCall("{call ERP.RPT_IM0410_PACKAGE.STARTING(?,?,?,?)}");
			calStmt.registerOutParameter(1, OracleTypes.CURSOR);
			calStmt.setString(2, brandCode);
			calStmt.setString(3, onHandDate);
			calStmt.setString(4, warehouseCode);
			calStmt.executeQuery();
			if("Y".equals(overZero))
				stmt = conn.prepareStatement("select * from ERP.TMP_RPT_IM0410 where END_ON_HAND_QTY <> 0 order by item_Code");
			else
				stmt = conn.prepareStatement("select * from ERP.TMP_RPT_IM0410 order by item_Code");
			rs = stmt.executeQuery();	    
			if(rs != null){
				while (rs.next()) {
					Barcode barcode = new Barcode();
					barcode.setItemCode(rs.getString("ITEM_CODE"));
					barcode.setQuantity(rs.getDouble("END_ON_HAND_QTY"));
					barcode.setUnitPrice(rs.getDouble("UNIT_PRICE"));
					barcode.setItemCName(rs.getString("ITEM_NAME"));
					barcodes.add(barcode);
				}
			}
			return barcodes;
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