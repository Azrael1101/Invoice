package tw.com.tm.erp.hbm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.service.FiInvoiceHeadMainService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderLineMainService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.DeclarationDataParse;
import tw.com.tm.erp.utils.EmployeeDataParse;
import tw.com.tm.erp.utils.NumberUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class IslandExportDAO {
 
	private static ApplicationContext context = SpringUtils.getApplicationContext();

	private static final Log log = LogFactory.getLog(IslandExportDAO.class);
	
	private DataSource dataSourceMS;
	private DataSource dataSource;
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setDataSourceMS(DataSource dataSourceMS) {
		this.dataSourceMS = dataSourceMS;
	}
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void updateImOnHand(List<ImOnHand> imOnHands) throws Exception {
		conn = null;
		stmt = null;
		try {
			System.out.println("imOnHands.size() = " + imOnHands.size());
			conn = dataSourceMS.getConnection();
			for (Iterator iterator = imOnHands.iterator(); iterator.hasNext();) {
				ImOnHand imOnHand = (ImOnHand) iterator.next();
				Date lastDateTime = imOnHand.getLastUpdateDate();
				if(null == lastDateTime)
					lastDateTime = new Date();
				String sql =" select * from IM_ON_HAND where ORGANIZATION_CODE = ? and BRAND_CODE = ? " +
						"and ITEM_CODE = ? and WAREHOUSE_CODE = ? and LOT_NO = ? ";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,"TM");
				stmt.setString(2,imOnHand.getBrandCode());
				stmt.setString(3,imOnHand.getId().getItemCode());
				stmt.setString(4,imOnHand.getId().getWarehouseCode());
				stmt.setString(5,imOnHand.getId().getLotNo());
				rs = stmt.executeQuery();
				if(null != rs && rs.next()){
					sql = "UPDATE IM_ON_HAND set " +
						" STOCK_ON_HAND_QTY = ? , OUT_UNCOMMIT_QTY = ? , IN_UNCOMMIT_QTY = ? , MOVE_UNCOMMIT_QTY = ? ," +
						" OTHER_UNCOMMIT_QTY = ? , LAST_UPDATED_BY = ? , LAST_UPDATE_DATE = ? " +
						" where ORGANIZATION_CODE = ? and BRAND_CODE = ? and ITEM_CODE = ? and WAREHOUSE_CODE = ? and  LOT_NO = ? ";
					stmt = conn.prepareStatement(sql);
					stmt.setDouble(1, NumberUtils.getDouble(imOnHand.getStockOnHandQty()));
					stmt.setDouble(2, NumberUtils.getDouble(imOnHand.getOutUncommitQty()));
					stmt.setDouble(3, NumberUtils.getDouble(imOnHand.getInUncommitQty()));
					stmt.setDouble(4, NumberUtils.getDouble(imOnHand.getMoveUncommitQty()));
					stmt.setDouble(5, NumberUtils.getDouble(imOnHand.getOtherUncommitQty()));
					stmt.setString(6, imOnHand.getLastUpdatedBy());
					stmt.setDate(7, new java.sql.Date(lastDateTime.getTime()));
					stmt.setString(8,"TM");
					stmt.setString(9,imOnHand.getBrandCode());
					stmt.setString(10,imOnHand.getId().getItemCode());
					stmt.setString(11,imOnHand.getId().getWarehouseCode());
					stmt.setString(12,imOnHand.getId().getLotNo());
					stmt.executeUpdate();
					System.out.println("update OK");
				}else{
					sql = "INSERT INTO IM_ON_HAND VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,"TM");
				stmt.setString(2,imOnHand.getBrandCode());
				stmt.setString(3,imOnHand.getId().getItemCode());
				stmt.setString(4,imOnHand.getId().getWarehouseCode());
				stmt.setString(5,imOnHand.getId().getLotNo());
				stmt.setDouble(6, NumberUtils.getDouble(imOnHand.getStockOnHandQty()));
				stmt.setDouble(7, NumberUtils.getDouble(imOnHand.getOutUncommitQty()));
				stmt.setDouble(8, NumberUtils.getDouble(imOnHand.getInUncommitQty()));
				stmt.setDouble(9, NumberUtils.getDouble(imOnHand.getMoveUncommitQty()));
				stmt.setDouble(10, NumberUtils.getDouble(imOnHand.getOtherUncommitQty()));
				stmt.setString(11, imOnHand.getCreatedBy());
				stmt.setDate(12, new java.sql.Date(lastDateTime.getTime()));
				stmt.setString(13, imOnHand.getLastUpdatedBy());
				stmt.setDate(14, new java.sql.Date(lastDateTime.getTime()));
				stmt.executeUpdate();
				System.out.println("insert OK");
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}

	public void updateCmOnHand(CmDeclarationOnHand cmOnHand, CmDeclarationItem cmItem, CmDeclarationHead head, CmDeclarationHead Ohead, ImItem item) throws Exception {
		System.out.println("updateCmOnHand: cmOnHand.getDeclarationNo() = " + cmOnHand.getDeclarationNo() + " and cmOnHand.getDeclarationSeq() = " + cmOnHand.getDeclarationSeq() + " " +
				" and cmOnHand.getCustomsItemCode() = " + cmOnHand.getCustomsItemCode() + " and cmOnHand.getCustomsWarehouseCode() = " + cmOnHand.getCustomsWarehouseCode());
		conn = null;
		stmt = null;
		try {
			conn = dataSourceMS.getConnection();
				Date lastDateTime = cmOnHand.getLastUpdateDate();
				if(null == lastDateTime)
					lastDateTime = new Date();
				String sql =" select * from CM_DECLARATION_ON_HAND where DECLARATION_NO = ? and DECLARATION_SEQ = ? " +
						"and CUSTOMS_ITEM_CODE = ? and CUSTOMS_WAREHOUSE_CODE = ? and BRAND_CODE = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,cmOnHand.getDeclarationNo());
				stmt.setLong(2,cmOnHand.getDeclarationSeq());
				stmt.setString(3,cmOnHand.getCustomsItemCode());
				stmt.setString(4,cmOnHand.getCustomsWarehouseCode());
				stmt.setString(5,cmOnHand.getBrandCode());
				rs = stmt.executeQuery();
				if(null != rs && rs.next()){
					sql = "UPDATE CM_DECLARATION_ON_HAND set " +
						" ON_HAND_QUANTITY = ? , OUT_UNCOMMIT_QTY = ? , IN_UNCOMMIT_QTY = ? , MOVE_UNCOMMIT_QTY = ? ," +
						" OTHER_UNCOMMIT_QTY = ? , LAST_UPDATED_BY = ? , LAST_UPDATE_DATE = ? , GOOD_DESC = ? where " +
						" DECLARATION_NO = ? and DECLARATION_SEQ = ? and CUSTOMS_ITEM_CODE = ? and CUSTOMS_WAREHOUSE_CODE = ? and BRAND_CODE = ?";
					stmt = conn.prepareStatement(sql);
					stmt.setDouble(1, NumberUtils.getDouble(cmOnHand.getOnHandQuantity()));
					stmt.setDouble(2, NumberUtils.getDouble(cmOnHand.getOutUncommitQty()));
					stmt.setDouble(3, NumberUtils.getDouble(cmOnHand.getInUncommitQty()));
					stmt.setDouble(4, NumberUtils.getDouble(cmOnHand.getMoveUncommitQty()));
					stmt.setDouble(5, NumberUtils.getDouble(cmOnHand.getOtherUncommitQty()));
					stmt.setString(6, cmOnHand.getLastUpdatedBy());
					stmt.setDate(7, new java.sql.Date(lastDateTime.getTime()));
					stmt.setString(8,item.getItemCName());
					stmt.setString(9,cmOnHand.getDeclarationNo());
					stmt.setLong(10,cmOnHand.getDeclarationSeq());
					stmt.setString(11,cmOnHand.getCustomsItemCode());
					stmt.setString(12,cmOnHand.getCustomsWarehouseCode());
					stmt.setString(13,cmOnHand.getBrandCode());
					stmt.executeUpdate();
					System.out.println("Cm update OK");
				}else{
					sql = "INSERT INTO CM_DECLARATION_ON_HAND VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1,cmOnHand.getDeclarationNo());
					stmt.setLong(2,cmOnHand.getDeclarationSeq());
					stmt.setString(3,cmOnHand.getCustomsItemCode());
					stmt.setString(4,cmOnHand.getCustomsWarehouseCode());
					stmt.setString(5,cmOnHand.getCustomsItemCode());
					if(item.getItemCName()!=null){
						if(item.getItemCName().length()>48){
						stmt.setString(6,item.getItemCName().substring(0, 24));	
					    }else{
						stmt.setString(6,item.getItemCName());
					    }    
					}else{
						stmt.setString(6,item.getItemCName());
					}										
					stmt.setString(7,cmOnHand.getWarehouseCode());
					stmt.setDouble(8, NumberUtils.getDouble(cmOnHand.getOnHandQuantity()));
					stmt.setDouble(9, NumberUtils.getDouble(cmOnHand.getOutUncommitQty()));
					stmt.setDouble(10, NumberUtils.getDouble(cmOnHand.getInUncommitQty()));
					stmt.setDouble(11, NumberUtils.getDouble(cmOnHand.getMoveUncommitQty()));
					stmt.setDouble(12, NumberUtils.getDouble(cmOnHand.getOtherUncommitQty()));
					stmt.setDate(13, null == head.getDeclDate() ? null : new java.sql.Date(head.getDeclDate().getTime()));
					stmt.setString(14, head.getDeclType());//DECLTYPE
					stmt.setString(15, cmItem.getODeclNo());//ORIGINAL_DECL_NO
					stmt.setLong(16, NumberUtils.getLong(cmItem.getOItemNo()));//ORIGINAL_DECL_SEQ
					stmt.setDate(17, null == Ohead.getDeclDate() ? null : new java.sql.Date(Ohead.getDeclDate().getTime()));//"ORIGINAL_DECL_Date"
					stmt.setString(18, Ohead.getDeclType());//ORIGINAL_DECL_TYPE
					stmt.setString(19,cmOnHand.getReserve1());
					stmt.setString(20,cmOnHand.getReserve2());
					stmt.setString(21,cmOnHand.getReserve3());
					stmt.setString(22,cmOnHand.getReserve4());
					stmt.setString(23,cmOnHand.getReserve5());
					stmt.setString(24, cmOnHand.getLastUpdatedBy());
					stmt.setDate(25, new java.sql.Date(lastDateTime.getTime()));
					stmt.setString(26, cmOnHand.getLastUpdatedBy());
					stmt.setDate(27, new java.sql.Date(lastDateTime.getTime()));
					stmt.setString(28,cmOnHand.getStatus());
					stmt.setString(29,cmOnHand.getBrandCode());
					stmt.setDouble(30,0.0);
					stmt.executeUpdate();
					System.out.println("Cm insert OK");
				}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	//20200606,cmDeclarationOnHandView
	public void updateCmOnHand(CmDeclarationOnHandView cmDeclarationOnHandView) throws Exception {
		System.out.println(
				"++updateCmOnHand: cmDeclarationOnHandView.getDeclarationNo() = " + cmDeclarationOnHandView.getDeclarationNo() + 
				" and cmDeclarationOnHandView.getDeclarationSeq() = " + cmDeclarationOnHandView.getDeclarationSeq() + " " +
				" and cmDeclarationOnHandView.getCustomsItemCode() = " + cmDeclarationOnHandView.getCustomsItemCode() + 
				" and cmDeclarationOnHandView.getCustomsWarehouseCode() = " + cmDeclarationOnHandView.getCustomsWarehouseCode());
		
		conn = null;
		stmt = null;
		try {
			conn = dataSourceMS.getConnection();
			Date lastDateTime = cmDeclarationOnHandView.getLastUpdateDate();
			if(null == lastDateTime)
				lastDateTime = new Date();
			String sql =" select * from CM_DECLARATION_ON_HAND where DECLARATION_NO = ? and DECLARATION_SEQ = ? " +
					"and CUSTOMS_ITEM_CODE = ? and CUSTOMS_WAREHOUSE_CODE = ? and BRAND_CODE = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,cmDeclarationOnHandView.getDeclarationNo());
			stmt.setLong(2,cmDeclarationOnHandView.getDeclarationSeq());
			stmt.setString(3,cmDeclarationOnHandView.getCustomsItemCode());
			stmt.setString(4,cmDeclarationOnHandView.getCustomsWarehouseCode());
			stmt.setString(5,cmDeclarationOnHandView.getBrandCode());
			rs = stmt.executeQuery();
			if(null != rs && rs.next()){
				sql = "UPDATE CM_DECLARATION_ON_HAND set " +
				" ON_HAND_QUANTITY = ? , OUT_UNCOMMIT_QTY = ? , IN_UNCOMMIT_QTY = ? , MOVE_UNCOMMIT_QTY = ? ," +
				" OTHER_UNCOMMIT_QTY = ? , LAST_UPDATED_BY = ? , LAST_UPDATE_DATE = ? , GOOD_DESC = ? , ORIGINAL_DECL_DATE = ? where " +
				" DECLARATION_NO = ? and DECLARATION_SEQ = ? and CUSTOMS_ITEM_CODE = ? and CUSTOMS_WAREHOUSE_CODE = ? and BRAND_CODE = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setDouble(1, NumberUtils.getDouble(cmDeclarationOnHandView.getOnHandQuantity()));
				stmt.setDouble(2, NumberUtils.getDouble(cmDeclarationOnHandView.getOutUncommitQty()));
				stmt.setDouble(3, NumberUtils.getDouble(cmDeclarationOnHandView.getInUncommitQty()));
				stmt.setDouble(4, NumberUtils.getDouble(cmDeclarationOnHandView.getMoveUncommitQty()));
				stmt.setDouble(5, NumberUtils.getDouble(cmDeclarationOnHandView.getOtherUncommitQty()));
				stmt.setString(6, cmDeclarationOnHandView.getLastUpdatedBy());
				stmt.setDate(7, new java.sql.Date(lastDateTime.getTime()));
				stmt.setString(8,cmDeclarationOnHandView.getItemCName());
				stmt.setDate(9, new java.sql.Date(cmDeclarationOnHandView.getOriginalDate().getTime()));
				stmt.setString(10,cmDeclarationOnHandView.getDeclarationNo());
				stmt.setLong(11,cmDeclarationOnHandView.getDeclarationSeq());
				stmt.setString(12,cmDeclarationOnHandView.getCustomsItemCode());
				stmt.setString(13,cmDeclarationOnHandView.getCustomsWarehouseCode());
				stmt.setString(14,cmDeclarationOnHandView.getBrandCode());
				stmt.executeUpdate();
				System.out.println("Cm update OK");
			}else{
				
				sql = "INSERT INTO CM_DECLARATION_ON_HAND VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,cmDeclarationOnHandView.getDeclarationNo());
				stmt.setLong(2,cmDeclarationOnHandView.getDeclarationSeq());
				stmt.setString(3,cmDeclarationOnHandView.getCustomsItemCode());
				stmt.setString(4,cmDeclarationOnHandView.getCustomsWarehouseCode());
				stmt.setString(5,cmDeclarationOnHandView.getCustomsItemCode());
				if(cmDeclarationOnHandView.getItemCName()!=null){
					if(cmDeclarationOnHandView.getItemCName().length()>48){
					stmt.setString(6,cmDeclarationOnHandView.getItemCName().substring(0, 24));	
				    }else{
					stmt.setString(6,cmDeclarationOnHandView.getItemCName());
				    }    
				}else{
					stmt.setString(6,cmDeclarationOnHandView.getItemCName());
				}										
				stmt.setString(7,cmDeclarationOnHandView.getWarehouseCode());
				stmt.setDouble(8, NumberUtils.getDouble(cmDeclarationOnHandView.getOnHandQuantity()));
				stmt.setDouble(9, NumberUtils.getDouble(cmDeclarationOnHandView.getOutUncommitQty()));
				stmt.setDouble(10, NumberUtils.getDouble(cmDeclarationOnHandView.getInUncommitQty()));
				stmt.setDouble(11, NumberUtils.getDouble(cmDeclarationOnHandView.getMoveUncommitQty()));
				stmt.setDouble(12, NumberUtils.getDouble(cmDeclarationOnHandView.getOtherUncommitQty()));
				stmt.setDate(13, null == cmDeclarationOnHandView.getDeclDate() ? null : new java.sql.Date(cmDeclarationOnHandView.getDeclDate().getTime()));
				stmt.setString(14, cmDeclarationOnHandView.getDeclType());//DECLTYPE
				stmt.setString(15, cmDeclarationOnHandView.getOrDeclNo());//ORIGINAL_DECL_NO
				stmt.setLong(16, NumberUtils.getLong(cmDeclarationOnHandView.getOrItemNo()));//ORIGINAL_DECL_SEQ
				stmt.setDate(17, null == cmDeclarationOnHandView.getOriginalDate() ? null : new java.sql.Date(cmDeclarationOnHandView.getOriginalDate().getTime()));//"ORIGINAL_DECL_Date"
				stmt.setString(18, cmDeclarationOnHandView.getOrDeclType());//ORIGINAL_DECL_TYPE
				stmt.setString(19,cmDeclarationOnHandView.getReserve1());
				stmt.setString(20,cmDeclarationOnHandView.getReserve2());
				stmt.setString(21,cmDeclarationOnHandView.getReserve3());
				stmt.setString(22,cmDeclarationOnHandView.getReserve4());
				stmt.setString(23,cmDeclarationOnHandView.getReserve5());
				stmt.setString(24, cmDeclarationOnHandView.getLastUpdatedBy());
				stmt.setDate(25, new java.sql.Date(lastDateTime.getTime()));
				stmt.setString(26, cmDeclarationOnHandView.getLastUpdatedBy());
				stmt.setDate(27, new java.sql.Date(lastDateTime.getTime()));
				stmt.setString(28,cmDeclarationOnHandView.getStatus());
				stmt.setString(29,cmDeclarationOnHandView.getBrandCode());
				stmt.setDouble(30,0.0);
				stmt.executeUpdate();
				System.out.println("Cm insert OK");				
			}		
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}	
	}
	
	public void updateCmItemInfo(CmDeclarationOnHand cmOnHand, CmDeclarationItem cmItem, CmDeclarationHead head, CmDeclarationHead Ohead) throws Exception {
		conn = null;
		stmt = null;
		try {
			System.out.println("updateCmItemInfo");
			conn = dataSourceMS.getConnection();
				String sql =" select * from CM_ITEM_INFO where DECLARATION_NO = ? and DECLARATION_SEQ = ? " +
						"and ITEM_CODE = ? and BRAND_CODE = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,cmOnHand.getDeclarationNo());
				stmt.setLong(2,cmOnHand.getDeclarationSeq());
				stmt.setString(3,cmOnHand.getCustomsItemCode());
				stmt.setString(4,cmOnHand.getBrandCode());
				rs = stmt.executeQuery();
				if(null != rs && rs.next()){
					
				}else{
					sql = "INSERT INTO CM_ITEM_INFO VALUES (?,?,?,?,?,?,?,?,?,?,?)";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1,cmOnHand.getDeclarationNo());
					stmt.setDouble(2,cmOnHand.getDeclarationSeq());
					stmt.setString(3,cmOnHand.getCustomsItemCode());
					stmt.setString(4,cmItem.getDescrip());
					stmt.setDate(5, null == head.getDeclDate() ? null : new java.sql.Date(head.getDeclDate().getTime()));
					stmt.setString(6, head.getDeclType());//DECLTYPE
					stmt.setString(7, cmItem.getODeclNo());//ORIGINAL_DECL_NO
					stmt.setDouble(8, cmItem.getOItemNo());//ORIGINAL_DECL_SEQ
					stmt.setDate(9, null == Ohead.getDeclDate() ? null : new java.sql.Date(Ohead.getDeclDate().getTime()));//"ORIGINAL_DECL_Date"
					stmt.setString(10, head.getDeclType());//ORIGINAL_DECL_TYPE
					stmt.setString(11,cmOnHand.getBrandCode());
					stmt.executeUpdate();
					System.out.println("insert OK");
				}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	public void updateDF06Q(List<ImAdjustmentHead> imAdjustmentHeads) throws Exception {
		conn = null;
		stmt = null;
		try {
			System.out.println("imAdjustmentHeads.size() = " + imAdjustmentHeads.size());
			conn = dataSourceMS.getConnection();
			for (Iterator iterator = imAdjustmentHeads.iterator(); iterator.hasNext();) {
				ImAdjustmentHead imAdjustmentHead = (ImAdjustmentHead) iterator.next();
				String sql =" select DECLTAX.DeclarationNo, DF06Q.DeclarationNo from DECLTAX full join DF06Q " +
						" on DECLTAX.DeclarationNo = DF06Q.DeclarationNo " +
						" where DECLTAX.DeclarationNo = ? or DF06Q.DeclarationNo=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,imAdjustmentHead.getDeclarationNo());
				stmt.setString(2,imAdjustmentHead.getDeclarationNo());
				rs = stmt.executeQuery();
				if(null != rs && rs.next()){
					
				}else{
					sql = "Declare @id varchar(36) select @id=newid() ";
					sql += " INSERT INTO DF06Q VALUES (@id,?,?,?,?,?,?) ";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, "");
					stmt.setString(2, "");
					stmt.setString(3, "");
					stmt.setString(4, imAdjustmentHead.getDeclarationNo());
					stmt.setString(5, "");
					stmt.setString(6, "");
					stmt.executeUpdate();
					System.out.println("insert OK");
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	public void closeSQL() throws Exception {
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

	public void updateDF08(SoSalesOrderHead head) throws Exception {
		conn = null;
		stmt = null;
		try {
			conn = dataSourceMS.getConnection();
				String sql =" select * from DF08_S1T1 where SaleNo = ? and SaleDate = ? ";;
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,head.getCustomerPoNo());
				stmt.setDate(2,new java.sql.Date(head.getSalesOrderDate().getTime()));
				rs = stmt.executeQuery();
				if(null != rs && rs.next()){
					String msgId = rs.getString(1);
					//System.out.println("msgId = " + msgId);
					List<SoSalesOrderItem> items = head.getSoSalesOrderItems();
					for (Iterator iterator = items.iterator(); iterator.hasNext();) {
						SoSalesOrderItem soSalesOrderItem = (SoSalesOrderItem) iterator.next();
						sql =" update DF08_S1T2 set OrgDeclarationNo = ? , OrgItemNo = ? where MsgId = ? and ItemNo = ? ";;
						stmt = conn.prepareStatement(sql);
						stmt.setString(1,soSalesOrderItem.getImportDeclNo());
						stmt.setLong(2,soSalesOrderItem.getImportDeclSeq());
						stmt.setString(3,msgId);
						stmt.setLong(4,soSalesOrderItem.getIndexNo());
						stmt.executeUpdate();
						/*
						sql =" select * from DF08_S1T2 where MsgId = ? and ItemNo = ? ";;
						stmt = conn.prepareStatement(sql);
						stmt.setString(1,msgId);
						stmt.setLong(2,soSalesOrderItem.getIndexNo());
						rss = stmt.executeQuery();
						if(null != rss && rss.next()){
							System.out.println("declNo = " + rss.getObject(11));
							System.out.println("declSeq = " + rss.getObject(12));
						}
						*/
					}
					sql =" update DF08R set MsgExStatus = ? where QueryMsgId = ? ";;
					stmt = conn.prepareStatement(sql);
					stmt.setString(1,"3");
					stmt.setString(2,msgId);
					stmt.executeUpdate();
				}else{
					throw new Exception ("査無銷貨單號  " + head.getCustomerPoNo() + " ，銷售日期 " + head.getSalesOrderDate() + "對應之DF單");
				}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	public void INSERT_STORE01T1(List store01t1,Map store01t2) throws Exception {
		int seq=1;
		System.out.println("INSERT_STORE01T1_A1");
		String msgId = "";
		conn = null;
		stmt = null;
		PreparedStatement stmt_id = null;
		rs=null;
		ResultSet id = null;
		List returnList = null;
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T1_A2");
			conn = dataSourceMS.getConnection();
			
			
			String sql_getdate =" select replace(replace(replace(convert(varchar(19), getdate(), 126),'-',''),'T',''),':','') ";
			stmt = conn.prepareStatement(sql_getdate);
			
			rs = stmt.executeQuery();
			if(null != rs && rs.next()){
				sysdate = (String)rs.getString(1);
			}
			System.out.println("INSERT_STORE01T1_A3");
			    String sql_id = "select newid() ";
			    stmt_id = conn.prepareStatement(sql_id);
			    id = stmt_id.executeQuery();
			    if(null != id && id.next()){
			        msgId = (String)id.getString(1);
			    }
			    System.out.println("msgId:"+msgId);
			    
				String sql ="INSERT INTO STORE01T1(MsgId, MsgStatus, TransferTime, ProcessTime, MsgFun, BondNo, StrType, GdsType, RefBillNo, CtmCode, ReferCode, StoreUniqueID, OrderTypeCode, OrderNo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				//for(int i=0;i<store01t1.size();i++){
				 // System.out.println("aa:"+store01t1.get(i).toString());
				//}
				
				stmt.setString(1,msgId);
				stmt.setString(2,store01t1.get(0).toString());
				stmt.setString(3,sysdate);
				stmt.setString(4,"");
				stmt.setString(5,store01t1.get(3).toString());
				stmt.setString(6,store01t1.get(4).toString());
				stmt.setString(7,store01t1.get(5).toString());
				stmt.setString(8,store01t1.get(6).toString());
				stmt.setString(9,store01t1.get(7).toString());
				stmt.setString(10,store01t1.get(8).toString());
				stmt.setString(11,store01t1.get(9).toString());
				stmt.setString(12,store01t1.get(10).toString());
				stmt.setString(13,store01t1.get(11).toString());
				stmt.setString(14,store01t1.get(12).toString());
				
				int affectedRow = stmt.executeUpdate();

				if(affectedRow!=0){
					for(int i=0;i<store01t2.size();i++){
						List de = (List)store01t2.get(i);
						de.set(0, msgId);
						de.set(1, seq);
						//if(store01t1.get(13).toString().equals("0")){ //移倉
						   INSERT_STORE01T2(de);
						//}else if(store01t1.get(13).toString().equals("1")){ //調撥
						  // INSERT_STORE01T2_NOS(de);
						//}
						seq++;
					}
				}
				
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} //finally {
			//closeSQL();
		//}
	}
	
	public void INSERT_STORE01T2(List store01t2) throws Exception {
		System.out.println("INSERT_STORE01T2_A2");
		String msgId = "";
		conn = null;
		stmt = null;
		rs=null;
		
		
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T2_A2");
			conn = dataSourceMS.getConnection();
			
			 
			
			System.out.println("INSERT_STORE01T1_A3");
			    
				String sql = "INSERT INTO STORE01T2 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				System.out.println("babucbr0922");	
				for(int i=0;i<store01t2.size();i++){
				  System.out.println("vv:"+store01t2.get(i).toString());
				}
				
				stmt.setString(1,store01t2.get(0).toString());   
				System.out.println("aaaaaaaa1:"+store01t2.get(0).toString());
				stmt.setInt(2,(Integer)store01t2.get(1));
				System.out.println("aaaaaaaa2:"+(Integer)store01t2.get(1));
				stmt.setString(3,store01t2.get(2).toString());
				System.out.println("aaaaaaaa3:"+store01t2.get(2).toString());
				stmt.setDouble(4,(Double)store01t2.get(3));
				System.out.println("aaaaaaaa4:"+(Double)store01t2.get(3));
				stmt.setString(5,store01t2.get(4).toString());
				System.out.println("aaaaaaaa5:"+store01t2.get(4).toString());
				stmt.setString(6,store01t2.get(5).toString());
				System.out.println("aaaaaaaa6:"+store01t2.get(5).toString());
				stmt.setString(7,store01t2.get(6).toString());
				System.out.println("aaaaaaaa7:"+store01t2.get(6).toString());
				stmt.setString(8,store01t2.get(7).toString());
				System.out.println("aaaaaaaa8:"+store01t2.get(7).toString());
				stmt.setString(9,store01t2.get(9).toString());
				System.out.println("aaaaaaaa9:"+store01t2.get(8).toString());
				//stmt.setInt(10,(Integer)store01t2.get(1));
				stmt.setInt(10,Integer.parseInt(store01t2.get(1).toString()));
				System.out.println("aaaaaaaaa10:"+(Integer)store01t2.get(1));
				stmt.setString(11,store01t2.get(10).toString());
				System.out.println("aaaaaaaa11:"+store01t2.get(10).toString());
				stmt.setInt(12,12);
				System.out.println("aaaaaaaaaa12:"+"12");
				stmt.setString(13,store01t2.get(2).toString());
				System.out.println("aaaaaaaa13:"+store01t2.get(12).toString());
				stmt.setString(14,store01t2.get(13).toString());
				System.out.println("aaaaaaaa14:"+store01t2.get(13).toString());
				System.out.println("aaaaaaaaaa15:"+store01t2.get(14).toString());
				stmt.setInt(15,Integer.parseInt(store01t2.get(14).toString()));
				
				/*
				stmt.setString(1,store01t2.get(0).toString());
				stmt.setInt(2,1);
				stmt.setString(3,"3");
				stmt.setDouble(4,4);
				stmt.setString(5,"5");
				stmt.setString(6,"6");
				stmt.setString(7,"7");
				stmt.setString(8,"8");
				stmt.setString(9,"9");
				stmt.setInt(10,10);
				stmt.setString(11,"11");
				stmt.setInt(12,12);
				stmt.setString(13,"13");
				stmt.setString(14,"14");
				stmt.setInt(15,15);
				*/
				//stmt.setString(12,"22");
				
				
				stmt.executeUpdate();
				System.out.println("INSERT_STORE01T1_A16");
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	
	
	public void INSERT_STORE01T2_NOS(List store01t2) throws Exception {
		System.out.println("INSERT_STORE01T2_A1");
		String msgId = "";
		conn = null;
		stmt = null;
		rs=null;
		
		
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T2_A2");
			conn = dataSourceMS.getConnection();
			
			
			
			System.out.println("INSERT_STORE01T1_A3");
			    
				String sql = "INSERT INTO STORE01T2 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				System.out.println("babucbr0922");	
				//for(int i=0;i<store01t1.size();i++){
				  //System.out.println("aa:"+store01t1.get(i).toString());
				//}
				
				stmt.setString(1,store01t2.get(0).toString());
				stmt.setInt(2,(Integer)store01t2.get(1));
				System.out.println("INSERT_STORE01T2_NOS2:"+(Integer)store01t2.get(1));
				stmt.setString(3,store01t2.get(2).toString());
				System.out.println("INSERT_STORE01T2_NOS3:"+store01t2.get(2).toString());
				stmt.setDouble(4,(Double)store01t2.get(3));
				System.out.println("INSERT_STORE01T2_NOS4:"+(Double)store01t2.get(3));
				stmt.setString(5,store01t2.get(4).toString());
				System.out.println("INSERT_STORE01T2_NOS5:"+store01t2.get(4).toString());
				stmt.setString(6,store01t2.get(5).toString());
				System.out.println("INSERT_STORE01T2_NOS6:"+store01t2.get(5).toString());
				stmt.setString(7,store01t2.get(6).toString());
				System.out.println("INSERT_STORE01T2_NOS7:"+store01t2.get(6).toString());
				stmt.setString(8,store01t2.get(7).toString());
				System.out.println("INSERT_STORE01T2_NOS8:"+store01t2.get(7).toString());
				stmt.setString(9,store01t2.get(8).toString());
				System.out.println("INSERT_STORE01T2_NOS9:"+store01t2.get(8).toString());
				stmt.setInt(10,(Integer)store01t2.get(9));
				System.out.println("INSERT_STORE01T2_NOS10:"+(Integer)store01t2.get(9));
				stmt.setString(11,store01t2.get(10).toString());
				System.out.println("INSERT_STORE01T2_NOS11:"+store01t2.get(10).toString());
				stmt.setInt(12,12);
				System.out.println("INSERT_STORE01T2_NOS12:"+"12");
				stmt.setString(13,store01t2.get(12).toString());
				System.out.println("INSERT_STORE01T2_NOS13:"+store01t2.get(12).toString());
				stmt.setString(14,store01t2.get(13).toString());
				System.out.println("INSERT_STORE01T2_NOS14:"+store01t2.get(13).toString());
				stmt.setInt(15,(Integer)store01t2.get(14));
				System.out.println("INSERT_STORE01T2_NOS15:"+(Integer)store01t2.get(14));
				/*
				stmt.setString(1,store01t2.get(0).toString());
				stmt.setInt(2,1);
				stmt.setString(3,"3");
				stmt.setDouble(4,4);
				stmt.setString(5,"5");
				stmt.setString(6,"6");
				stmt.setString(7,"7");
				stmt.setString(8,"8");
				stmt.setString(9,"9");
				stmt.setInt(10,10);
				stmt.setString(11,"11");
				stmt.setInt(12,12);
				stmt.setString(13,"13");
				stmt.setString(14,"14");
				stmt.setInt(15,15);
				*/
				
				
				
				stmt.executeUpdate();
				System.out.println("INSERT_STORE01T2_NOS16--End");
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	public String checkIfInsert(String RefBillNo) throws Exception{
		
		//orderTypeCode = orderTypeCode.substring(1);
		//String RefBillNo = orderTypeCode+orderNo;
		String IsExist;
		
		conn = null;
		stmt = null;
		rs=null;
		try {
			conn = dataSourceMS.getConnection();
			String sql =" select * from STORE01T1 where RefBillNo = ?";;
			System.out.println("IslandExportDao.sql:"+sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,RefBillNo);
			
			rs = stmt.executeQuery();   
				if(null != rs && rs.next()){
					System.out.println("ooooooooooooooInsert ok!!");
					IsExist = "1";
				}else{
					IsExist = "0";
					
				}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
		
		
		return IsExist;
		
	}
	
	public String getSumMsgFun(String refBillNo) throws Exception{
			
			conn = null;
			stmt = null;
			rs=null;
			String msgFunSum="";
			try {
				conn = dataSourceMS.getConnection();
				String sql =" SELECT RefBillNo, SUM(case when MsgFun = 9 then 1 else -1 end) AS AA FROM STORE01T1 WHERE RefBillNo = '?' group by RefBillNo";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,refBillNo);
				
				rs = stmt.executeQuery();   
					if(null != rs && rs.next()){
						msgFunSum = rs.getString(1);
						System.out.println("msgfunsum:"+msgFunSum);
					}
			} catch (Exception ex) {
				log.error(ex.getMessage());
				throw new Exception(ex.getMessage());
			} finally {
				closeSQL();
			}
			
			
			return msgFunSum;
			
		}
	
	public String getStoreNo(String WareHseCode) throws Exception{
		List<BuCommonPhraseLine> allReportList;
		try {
			allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"EXPORTSTORE", "attribute1='" + WareHseCode + "'");
			System.out.println("attribute2:"+allReportList.get(0).getAttribute2());
				
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} 
		
		
		return allReportList.get(0).getAttribute2();
		
	}
	
	public String getStoreNo_ARR(String WareHseCode) throws Exception{
		List<BuCommonPhraseLine> allReportList;
		try {
			allReportList = buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(
					"EXPORTSTORE", "attribute1='" + WareHseCode + "'");
			System.out.println("attribute2:"+allReportList.get(0).getAttribute2());
				
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} 
		
		
		return allReportList.get(0).getAttribute2();
		
	}
	
	public void INSERT_DF12T1(List df12t1,Map df12t2, Map df12t3) throws Exception {
		//int seq=1;
		System.out.println("INSERT_STORE01T1_A1");
		String msgId = "";
		conn = null;
		stmt = null;
		PreparedStatement stmt_id = null;
		rs=null;
		ResultSet id = null;
		List returnList = null;
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T1_A2");
			conn = dataSourceMS.getConnection();
			
			
			String sql_getdate =" select replace(replace(replace(convert(varchar(19), getdate(), 126),'-',''),'T',''),':','') ";
			stmt = conn.prepareStatement(sql_getdate);
			
			rs = stmt.executeQuery();
			if(null != rs && rs.next()){
				sysdate = (String)rs.getString(1);
			}
			System.out.println("INSERT_STORE01T1_A3");
			    String sql_id = "select newid() ";
			    stmt_id = conn.prepareStatement(sql_id);
			    id = stmt_id.executeQuery();
			    if(null != id && id.next()){
			        msgId = (String)id.getString(1);
			    }
			    System.out.println("msgId:"+msgId);			    
				
				String sql ="INSERT INTO DF12T1(Msgid, MsgStatus, TransferTime, ProcessTime, StoreUniqueID, BCCCode, TotalResNo, TransType, ResNo, ResDateTime, ProcessCustCd, ResStoreCode, ResWay, AftTotalItems, BefTotalItems, Note, RSatausCode, RSatausDesc, ProcessResultCode, ProcessResultDesc, RAftTotalItems, RBefTotalItems, LogStatus) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				//for(int i=0;i<store01t1.size();i++){
				 // System.out.println("aa:"+store01t1.get(i).toString());
				//}
				
				stmt.setString(1,msgId);
				stmt.setString(2,df12t1.get(0).toString());
				stmt.setString(3,sysdate);
				stmt.setString(4,"");
				stmt.setString(5,df12t1.get(3).toString());
				stmt.setString(6,df12t1.get(4).toString());
				stmt.setString(7,df12t1.get(5).toString());
				stmt.setString(8,df12t1.get(6).toString());
				stmt.setString(9,df12t1.get(8).toString());
				stmt.setString(10,df12t1.get(9).toString());
				stmt.setString(11,df12t1.get(10).toString());
				stmt.setString(12,df12t1.get(11).toString());
				stmt.setString(13,df12t1.get(12).toString());
				stmt.setInt(14,(Integer)df12t1.get(13));
				stmt.setInt(15,(Integer)df12t1.get(14));
				stmt.setString(16,df12t1.get(15).toString());
				stmt.setString(17,df12t1.get(16).toString());
				stmt.setString(18,df12t1.get(17).toString());
				stmt.setString(19,df12t1.get(18).toString());
				stmt.setString(20,df12t1.get(19).toString());
				stmt.setInt(21,(Integer)df12t1.get(20));
				stmt.setInt(22,(Integer)df12t1.get(21));
				stmt.setString(23,df12t1.get(22).toString());
				
				int affectedRow = stmt.executeUpdate();

				if(affectedRow!=0){
					for(int i=0;i<df12t2.size();i++){
						List t2 = (List)df12t2.get(i);
						t2.set(0, msgId);
						//t2.set(1, seq);
						//if(store01t1.get(13).toString().equals("0")){ //移倉
						   INSERT_DF12T2(t2);
						//}else if(store01t1.get(13).toString().equals("1")){ //調撥
						  // INSERT_STORE01T2_NOS(de);
						//}
						//seq++;
					}
					for(int i=0;i<df12t3.size();i++){
						List t3 = (List)df12t3.get(i);
						t3.set(0, msgId);
						//t2.set(1, seq);
						//if(store01t1.get(13).toString().equals("0")){ //移倉
						   INSERT_DF12T3(t3);
						//}else if(store01t1.get(13).toString().equals("1")){ //調撥
						  // INSERT_STORE01T2_NOS(de);
						//}
						//seq++;
					}
				}
				
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} //finally {
			//closeSQL();
		//}
	}
	
	public void INSERT_DF12T2(List df12t2) throws Exception {
		System.out.println("INSERT_STORE01T2_A2");
		String msgId = "";
		conn = null;
		stmt = null;
		rs=null;
		
		
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T2_A2");
			conn = dataSourceMS.getConnection();
			
			 
			
			System.out.println("INSERT_STORE01T1_A3");
			    
				String sql = "INSERT INTO DF12T2 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				System.out.println("babucbr0922");	
				for(int i=0;i<df12t2.size();i++){
				  System.out.println("vv:"+df12t2.get(i).toString());
				}
				
				stmt.setString(1,df12t2.get(0).toString());   
				System.out.println("aaaaaaaa1:"+df12t2.get(0).toString());
				stmt.setInt(2,(Integer)df12t2.get(1));
				System.out.println("aaaaaaaa2:"+(Integer)df12t2.get(1));
				stmt.setString(3,df12t2.get(2).toString());
				System.out.println("aaaaaaaa3:"+df12t2.get(2).toString());
				stmt.setString(4,df12t2.get(3).toString());
				System.out.println("aaaaaaaa4:"+df12t2.get(3));
				stmt.setString(5,df12t2.get(4).toString());
				System.out.println("aaaaaaaa5:"+df12t2.get(4).toString());
				stmt.setString(6,df12t2.get(5).toString());
				System.out.println("aaaaaaaa6:"+df12t2.get(5).toString());
				stmt.setDouble(7,(Double)df12t2.get(6));
				System.out.println("aaaaaaaa7:"+df12t2.get(6).toString());
				stmt.setString(8,df12t2.get(7).toString());
				System.out.println("aaaaaaaa8:"+df12t2.get(7).toString());
				stmt.setDouble(9,(Double)df12t2.get(8));
				System.out.println("aaaaaaaa9:"+df12t2.get(8).toString());
				//stmt.setInt(10,(Integer)store01t2.get(1));
				stmt.setDouble(10,(Double)df12t2.get(9));
				System.out.println("aaaaaaaaa10:"+(Integer)df12t2.get(1));
				stmt.setDouble(11,(Double)df12t2.get(10));
				System.out.println("aaaaaaaa11:"+df12t2.get(10).toString());
				stmt.setDouble(12,(Double)df12t2.get(11));
				System.out.println("aaaaaaaaaa12:"+"12");
				stmt.setDouble(13,(Double)df12t2.get(12));
				System.out.println("aaaaaaaa13:"+df12t2.get(12).toString());
				stmt.setDouble(14,(Double)df12t2.get(13));
				System.out.println("aaaaaaaa14:"+df12t2.get(13).toString());
				System.out.println("aaaaaaaaaa15:"+df12t2.get(14).toString());
				stmt.setDouble(15,(Double)df12t2.get(14));
				System.out.println("aaaaaaaaaa15:"+df12t2.get(14).toString());
				stmt.setString(16,df12t2.get(15).toString());
				
				stmt.executeUpdate();
				System.out.println("INSERT_STORE01T1_A16");
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
	public void INSERT_DF12T3(List df12t3) throws Exception {
		System.out.println("INSERT_STORE01T2_A2");
		String msgId = "";
		conn = null;
		stmt = null;
		rs=null;
		
		
		String sysdate="";
		try {
			System.out.println("INSERT_STORE01T2_A2");
			conn = dataSourceMS.getConnection();
			
			 
			
			System.out.println("INSERT_STORE01T1_A3");
			    
				String sql = "INSERT INTO STORE01T2 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
					
				for(int i=0;i<df12t3.size();i++){
				  System.out.println("vv:"+df12t3.get(i).toString());
				}
				
				stmt.setString(1,df12t3.get(0).toString());   
				System.out.println("aaaaaaaa1:"+df12t3.get(0).toString());
				stmt.setInt(2,(Integer)df12t3.get(1));
				System.out.println("aaaaaaaa2:"+(Integer)df12t3.get(1));
				stmt.setString(3,df12t3.get(2).toString());
				System.out.println("aaaaaaaa3:"+df12t3.get(2).toString());
				stmt.setString(4,df12t3.get(3).toString());
				System.out.println("aaaaaaaa4:"+df12t3.get(3));
				stmt.setString(5,df12t3.get(4).toString());
				System.out.println("aaaaaaaa5:"+df12t3.get(4).toString());
				stmt.setString(6,df12t3.get(5).toString());
				System.out.println("aaaaaaaa6:"+df12t3.get(5).toString());
				stmt.setDouble(7,(Double)df12t3.get(6));
				System.out.println("aaaaaaaa7:"+df12t3.get(6).toString());
				stmt.setString(8,df12t3.get(7).toString());
				System.out.println("aaaaaaaa8:"+df12t3.get(7).toString());
				stmt.setDouble(9,(Double)df12t3.get(8));
				System.out.println("aaaaaaaa9:"+df12t3.get(8).toString());
				//stmt.setInt(10,(Integer)store01t2.get(1));
				stmt.setDouble(10,(Double)df12t3.get(9));
				System.out.println("aaaaaaaaa10:"+(Integer)df12t3.get(1));
				stmt.setDouble(11,(Double)df12t3.get(10));
				System.out.println("aaaaaaaa11:"+df12t3.get(10).toString());
				stmt.setDouble(12,(Double)df12t3.get(11));
				System.out.println("aaaaaaaaaa12:"+"12");
				stmt.setDouble(13,(Double)df12t3.get(12));
				System.out.println("aaaaaaaa13:"+df12t3.get(12).toString());
				stmt.setDouble(14,(Double)df12t3.get(13));
				System.out.println("aaaaaaaa14:"+df12t3.get(13).toString());
				System.out.println("aaaaaaaaaa15:"+df12t3.get(14).toString());
				stmt.setDouble(15,(Double)df12t3.get(14));
				System.out.println("aaaaaaaaaa15:"+df12t3.get(14).toString());
				stmt.setString(16,df12t3.get(15).toString());
				
				
				stmt.setString(17,df12t3.get(16).toString());				
				stmt.setInt(18,(Integer)df12t3.get(17));				
				stmt.setString(19,df12t3.get(18).toString());				
				stmt.setInt(20,(Integer)df12t3.get(19));
				
				stmt.executeUpdate();
				System.out.println("INSERT_STORE01T1_A16");
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
			closeSQL();
		}
	}
	
}
