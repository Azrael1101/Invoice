package tw.com.tm.erp.hbm.dao;

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.TmpExtendItemInfo;

public class TmpExtendItemInfoDAO {

	private static final Log log = LogFactory.getLog(TmpExtendItemInfoDAO.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List getExtendItemInfo(HashMap conditionMap) throws Exception {

		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String tableType = (String) conditionMap.get("tableType");
		Long searchKey = (Long) conditionMap.get("searchKey");
		List tmpExtendItemInfoBeans = null;
		try {
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.APP_EXTEND_ITEM_INFO.main(?,?)}");
			calStmt.setString(1, tableType);
			calStmt.setLong(2, searchKey);
			calStmt.executeQuery();

			stmt = conn.prepareStatement("SELECT * FROM ERP.TMP_EXTEND_ITEM_INFO ORDER BY LINE_ID");
			rs = stmt.executeQuery();
			if (rs != null) {
				tmpExtendItemInfoBeans = produceTmpExtendItemInfoBeans(rs);
			}

			return tmpExtendItemInfoBeans;
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
	 * 核銷報單（扣除被鎖住的報單庫存） by Weichun 2011.09.22
	 *
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List getExtendItemInfoWithBlock(HashMap conditionMap) throws Exception {

		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String tableType = (String) conditionMap.get("tableType");
		Long searchKey = (Long) conditionMap.get("searchKey");
		List tmpExtendItemInfoBeans = null;
		try {
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.APP_EXTEND_ITEM_INFO_BLOCK.main(?,?)}");
			calStmt.setString(1, tableType);
			calStmt.setLong(2, searchKey);
			calStmt.executeQuery();

			stmt = conn.prepareStatement("SELECT * FROM ERP.TMP_EXTEND_ITEM_INFO ORDER BY LINE_ID");
			rs = stmt.executeQuery();
			if (rs != null) {
				tmpExtendItemInfoBeans = produceTmpExtendItemInfoBeans(rs);
			}

			return tmpExtendItemInfoBeans;
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

	private List produceTmpExtendItemInfoBeans(ResultSet rs) throws Exception {

		List assembly = new ArrayList(0);
		while (rs.next()) {
			String brandCode = rs.getString("BRAND_CODE");
			String itemCode = rs.getString("ITEM_CODE");
			String warehouseCode = rs.getString("WAREHOUSE_CODE");
			String lotNo = rs.getString("LOT_NO");
			String declarationNo = rs.getString("DECLARATION_NO");
			Long declarationSeq = rs.getLong("DECLARATION_SEQ");
			Long originalLineId = rs.getLong("ORIGINAL_LINE_ID");
			String originalLotNo = rs.getString("ORIGINAL_LOT_NO");
			String originalDeclarationNo = rs.getString("ORIGINAL_DECLARATION_NO");
			Long originalDeclarationSeq = rs.getLong("ORIGINAL_DECLARATION_SEQ");
			Double originalQuantity = rs.getDouble("ORIGINAL_QUANTITY");
			Long lineId = rs.getLong("LINE_ID");
			Date declarationDate = rs.getDate("DECLARATION_DATE");
			Double quantity = rs.getDouble("QUANTITY");
			String message = rs.getString("MESSAGE");
			String status = rs.getString("STATUS");
			String declarationType = rs.getString("DECLARATION_TYPE");
			Double perUnitAmount = rs.getDouble("PER_UNIT_AMOUNT");
			
			System.out.println("lineId=[" + lineId + "]");
			System.out.println("brandCode=[" + brandCode + "]");
			System.out.println("itemCode=[" + itemCode + "]");
			System.out.println("warehouseCode=[" + warehouseCode + "]");
			System.out.println("lotNo=[" + lotNo + "]");
			System.out.println("declarationNo=[" + declarationNo + "]");
			System.out.println("declarationSeq=[" + declarationSeq + "]");
			System.out.println("originalLineId=[" + originalLineId + "]");
			System.out.println("originalLotNo=[" + originalLotNo + "]");
			System.out.println("originalDeclarationNo=[" +originalDeclarationNo + "]");
			System.out.println("originalDeclarationSeq=[" +originalDeclarationSeq + "]");
			System.out.println("originalQuantity=[" + originalQuantity +"]"); 
			System.out.println("declarationDate=[" + declarationDate +"]"); 
			System.out.println("quantity=[" + quantity + "]");
			System.out.println("message=[" + message + "]");
			System.out.println("status=[" + status + "]");
			System.out.println("declarationType=[" + declarationType + "]");
			System.out.println("perUnitAmount=[" + perUnitAmount + "]");
			

			TmpExtendItemInfo extendItemInfo = new TmpExtendItemInfo();
			extendItemInfo.setBrandCode(brandCode);
			extendItemInfo.setItemCode(itemCode);
			extendItemInfo.setWarehouseCode(warehouseCode);
			extendItemInfo.setLotNo(lotNo);
			extendItemInfo.setDeclarationNo(declarationNo);
			extendItemInfo.setDeclarationSeq(declarationSeq);
			extendItemInfo.setOriginalLineId(originalLineId);
			extendItemInfo.setOriginalLotNo(originalLotNo);
			extendItemInfo.setOriginalDeclarationNo(originalDeclarationNo);
			extendItemInfo.setOriginalDeclarationSeq(originalDeclarationSeq);
			extendItemInfo.setOriginalQuantity(originalQuantity);
			extendItemInfo.setLineId(lineId);
			extendItemInfo.setDeclarationDate(declarationDate);
			extendItemInfo.setQuantity(quantity);
			extendItemInfo.setMessage(message);
			extendItemInfo.setStatus(status);
			extendItemInfo.setDeclarationType(declarationType);
			if(perUnitAmount!=null && perUnitAmount != 0 && quantity != null && quantity != 0){
				extendItemInfo.setPerUnitAmount(perUnitAmount*quantity);
			}			
			assembly.add(extendItemInfo);
		}

		return assembly;
	}
}