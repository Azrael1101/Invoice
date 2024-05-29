package tw.com.tm.erp.hbm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.utils.DateUtils;

public class CmDeclarationOnHandViewDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(CmDeclarationOnHandViewDAO.class);
	
	/**
	 * 依據declarationNo、declarationSeq、customsItemCode、customsWarehouseCode、brandCode查詢，並不進行鎖定
	 *
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param customsWarehouseCode
	 * @param brandCode
	 * @param startDate
	 * @param endDate
	 * @return List
	 */
	public List<CmDeclarationOnHandView> getNoLockedOnHand(final String declarationNo, final Long declarationSeq, final String customsItemCode,
			final String customsWarehouseCode, final String brandCode, final Date startDate, final Date endDate)throws Exception {
		log.info("declarationNo        = "+declarationNo);
		log.info("declarationSeq       = "+declarationSeq);
		log.info("customsItemCode      = "+customsItemCode);
		log.info("customsWarehouseCode = "+customsWarehouseCode); //KD
		log.info("brandCode            = "+brandCode); //T2
		log.info("startDate            = "+startDate);
		log.info("endDate               = "+endDate);
		List<CmDeclarationOnHandView> CmDeclarationOnHands = new ArrayList<CmDeclarationOnHandView>();		
		Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
		try{
			Date dateNew = new Date();
			Date finalDeclDate = DateUtils.addDays(dateNew, -5);
			String finalNewDate = DateUtils.format(finalDeclDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			conn = dataSource.getConnection();
//			StringBuffer sql = new StringBuffer();
			
			stmt = conn.createStatement();
//			sql.append("select * from ERP.CM_DECLARATION_ON_HAND CDO, ERP.CM_DECLARATION_HEAD CDH, ERP.CM_DECLARATION_ITEM CDI");
//			sql.append(" where 1=1 AND CDO.CUSTOMS_WAREHOUSE_CODE = '"+customsWarehouseCode+"'");
//			sql.append(" AND CDO.DECLARATION_NO = CDI.DECL_NO");
//			sql.append(" AND CDO.DECLARATION_SEQ = CDI.ITEM_NO");
//			sql.append(" AND CDI.O_DECL_NO = CDH.DECL_NO");
//			sql.append(" AND CDH.DECL_DATE >= TO_DATE('20090505','YYYYMMDD')");
//			sql.append(" AND CDO.LAST_UPDATE_DATE > TO_DATE('"+finalNewDate+"','YYYYMMDD')");
//			sql.append(" ORDER BY CDH.DECL_DATE");
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM ERP.CM_DECLARATION_ON_HAND_VIEW CDOHV");
			sql.append(" WHERE 1=1 AND CDOHV.CUSTOMS_WAREHOUSE_CODE = '"+customsWarehouseCode+"'");
			sql.append(" AND CDOHV.DECL_DATE >= TO_DATE('20090505','YYYYMMDD')");
			sql.append(" AND CDOHV.LAST_UPDATE_DATE > TO_DATE('"+finalNewDate+"','YYYYMMDD')");
			sql.append(" ORDER BY CDOHV.DECL_DATE");
			
			rs = stmt.executeQuery(sql.toString());
			
			while(rs.next()){
				CmDeclarationOnHandView cmDeclarationOnHand = new CmDeclarationOnHandView();
				cmDeclarationOnHand.setBrandCode(rs.getString("BRAND_CODE"));
				cmDeclarationOnHand.setDeclType(rs.getString("DECL_TYPE"));
				cmDeclarationOnHand.setDeclarationNo(rs.getString("DECLARATION_NO"));
				cmDeclarationOnHand.setDeclarationSeq(rs.getLong("DECLARATION_SEQ"));
				cmDeclarationOnHand.setDeclDate(rs.getDate("DECL_DATE"));
				
				cmDeclarationOnHand.setOriginalDate(rs.getDate("ORIGINAL_DATE"));
				cmDeclarationOnHand.setCustomsItemCode(rs.getString("CUSTOMS_ITEM_CODE"));
				cmDeclarationOnHand.setCustomsWarehouseCode(rs.getString("CUSTOMS_WAREHOUSE_CODE"));
				cmDeclarationOnHand.setItemCode(rs.getString("ITEM_CODE"));
				cmDeclarationOnHand.setWarehouseCode(rs.getString("WAREHOUSE_CODE"));
				
				cmDeclarationOnHand.setOnHandQuantity(rs.getDouble("ON_HAND_QUANTITY"));
				cmDeclarationOnHand.setOutUncommitQty(rs.getDouble("OUT_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setInUncommitQty(rs.getDouble("IN_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setMoveUncommitQty(rs.getDouble("MOVE_UNCOMMIT_QTY"));
				cmDeclarationOnHand.setOtherUncommitQty(rs.getDouble("OTHER_UNCOMMIT_QTY"));
				
				cmDeclarationOnHand.setCurrentOnHandQty(rs.getDouble("CURRENT_ON_HAND_QTY"));
				cmDeclarationOnHand.setStatus(rs.getString("STATUS"));
				cmDeclarationOnHand.setItemCName(rs.getString("ITEM_C_NAME"));
				cmDeclarationOnHand.setCategory01(rs.getString("CATEGORY01"));
				cmDeclarationOnHand.setCategory01Name(rs.getString("CATEGORY01_NAME"));
				
				cmDeclarationOnHand.setCategory02(rs.getString("CATEGORY02"));
				cmDeclarationOnHand.setCategory02Name(rs.getString("CATEGORY02_NAME"));
				cmDeclarationOnHand.setCategory03(rs.getString("CATEGORY03"));
				cmDeclarationOnHand.setItemBrand(rs.getString("ITEM_BRAND"));
				cmDeclarationOnHand.setItemBrandName(rs.getString("ITEM_BRAND_NAME"));
				
				cmDeclarationOnHand.setWarehouseInDate(rs.getDate("WAREHOUSE_IN_DATE"));
				cmDeclarationOnHand.setRemainDays(rs.getLong("REMAIN_DAYS"));
				cmDeclarationOnHand.setExpiryDate(rs.getDate("EXPIRY_DATE"));
				cmDeclarationOnHand.setImportDate(rs.getDate("IMPORT_DATE"));
				cmDeclarationOnHand.setCategoryType(rs.getString("CATEGORY_TYPE"));
				
				cmDeclarationOnHand.setQty(rs.getDouble("QTY"));
				cmDeclarationOnHand.setSupplierCode(rs.getString("SUPPLIER_CODE"));
				cmDeclarationOnHand.setSupplierName(rs.getString("SUPPLIER_NAME"));
				cmDeclarationOnHand.setBlockOnHandQuantity(rs.getDouble("BLOCK_ON_HAND_QUANTITY"));
				cmDeclarationOnHand.setOrderTypeCode(rs.getString("ORDER_TYPE_CODE"));
				
				cmDeclarationOnHand.setOrderNo(rs.getString("ORDER_NO"));
				cmDeclarationOnHand.setDescription(rs.getString("DESCRIPTION"));
				cmDeclarationOnHand.setIsExtention(rs.getString("IS_EXTENTION"));
				cmDeclarationOnHand.setOrDeclDate(rs.getDate("O_DECL_DATE"));
				cmDeclarationOnHand.setOrgImportDate(rs.getDate("ORG_IMPORT_DATE"));
				
				cmDeclarationOnHand.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
				cmDeclarationOnHand.setReserve1(rs.getString("RESERVE1"));
				cmDeclarationOnHand.setReserve2(rs.getString("RESERVE2"));
				cmDeclarationOnHand.setReserve3(rs.getString("RESERVE3"));
				cmDeclarationOnHand.setReserve4(rs.getString("RESERVE4"));
				cmDeclarationOnHand.setReserve5(rs.getString("RESERVE5"));
				cmDeclarationOnHand.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
				cmDeclarationOnHand.setOrDeclNo(rs.getString("O_DECL_NO"));
				cmDeclarationOnHand.setOrItemNo(rs.getLong("O_ITEM_NO"));
				cmDeclarationOnHand.setOrDeclType(rs.getString("O_DECL_TYPE"));
				
//				PER_UNIT_AMOUNT
//				CURRENT_ON_HAND_WITH_BLOCK_QTY
				
				
//				cmDeclarationOnHand.setCreatedBy(rs.getString("CREATED_BY"));
//				cmDeclarationOnHand.setCreationDate(rs.getDate("CREATION_DATE"));
//				cmDeclarationOnHand.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
//				cmDeclarationOnHand.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));		
//				cmDeclarationOnHand.setUnblockOnHandQuantity(rs.getDouble("UNBLOCK_ON_HAND_QUANTITY"));
				
				CmDeclarationOnHands.add(cmDeclarationOnHand);
			}			
			return CmDeclarationOnHands;
		}catch(Exception e){
			
		}finally{
    		if (stmt != null) {
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    	}
		return CmDeclarationOnHands;
		
	}
	
	public CmDeclarationOnHandView findByPk(String declarationNo, Long declarationSeq, String customsWarehouseCode){
		
		List<CmDeclarationOnHandView> cmDeclarationOnHandViews = null; 
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from CmDeclarationOnHandView as model ");
		hql.append(" where model.declarationNo = ? ");
		hql.append(" and model.declarationSeq = ? ");
		hql.append(" and model.customsWarehouseCode = ?");
		
		log.info(hql.toString());
		
		cmDeclarationOnHandViews = getHibernateTemplate()
			.find(hql.toString(), new Object[] { declarationNo, declarationSeq, customsWarehouseCode });
		
		return (cmDeclarationOnHandViews != null && cmDeclarationOnHandViews.size() > 0) ? cmDeclarationOnHandViews.get(0) : null ;
		
	}

}
