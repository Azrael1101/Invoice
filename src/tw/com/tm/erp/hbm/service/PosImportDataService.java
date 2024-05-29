package tw.com.tm.erp.hbm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.ImStorageAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuShopMachineId;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.bean.SnsBonusPointSetting;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.bean.SoShopDailyHeadId;
import tw.com.tm.erp.hbm.bean.TmpImportPosItem;
import tw.com.tm.erp.hbm.bean.TmpImportPosItemId;
import tw.com.tm.erp.hbm.bean.TmpImportPosPayment;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.SnsBonusPointSettingDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
import tw.com.tm.erp.utils.sp.AppExtendItemInfoService;


/**
 * 整批檔案轉檔 轉入ERP系統.
 * 
 * @author T96640
 * 
 */
public class PosImportDataService {
	private static final Log log = LogFactory.getLog(PosImportDataService.class);

	private DataSource dataSource;
	private BuShopMachineDAO buShopMachineDAO;
	private BuCommonPhraseService buCommonPhraseService;
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
	private SiProgramLogAction siProgramLogAction;
	private BuOrderTypeService buOrderTypeService;
	public static final String PROGRAM_ID = "SO_SALES_ORDER";
	private AppExtendItemInfoService appExtendItemInfoService;
	private BuShopDAO buShopDAO;
	private SoSalesOrderMainService soSalesOrderMainService;
	private ImItemDAO imItemDAO;
	private BuCountryDAO buCountryDAO;
	private ImItemPriceService imItemPriceService;
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private BaseDAO baseDAO;
	private SoPostingTallyDAO soPostingTallyDAO;
	private SoPostingTallyService soPostingTallyService;
	private ImStorageAction imStorageAction;
	private ImStorageService imStorageService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private DataSource dataSourceMySql;
	public void setDataSourceMySql(DataSource dataSourceMySql){
    	this.dataSourceMySql = dataSourceMySql;
    }

	/**
	 * 查詢POS_COMMAND的資料.
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 */
	public HashMap qryPOSCommand(Long requestID) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap posCommandMap = new HashMap();
		try {
			conn = dataSource.getConnection();

			String sql = " select * from POS_COMMAND where REQUEST_ID = ?  ";

			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, requestID);

			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					posCommandMap.put("BATCH_ID", rs.getString("BATCH_ID"));
					posCommandMap.put("BRAND_CODE", rs.getString("BRAND_CODE"));
					posCommandMap.put("DATA_ID", rs.getString("DATA_ID"));
					posCommandMap.put("MACHINE_CODE", rs.getString("MACHINE_CODE"));
					posCommandMap.put("NUMBERS", rs.getString("NUMBERS"));
					posCommandMap.put("DATA_TYPE", rs.getString("DATA_TYPE"));
				}
			}

		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
		return posCommandMap;
	}

	/**
	 * 建立SO的BEAN,目前同一次資料上傳為單一機台，但有可能為不同交易序號
	 * @throws Exception
	 */
	public List crtSoHeadBean(HashMap posCommandMap) throws Exception{
		//	posCommandMap.put("responseId", responseId);
		Long responseId = (Long)posCommandMap.get("responseId");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Date currentDateTime = new Date();
		List entityBeans = new ArrayList();
		String brandCode = (String) posCommandMap.get("BRAND_CODE");
		String dataId = (String) posCommandMap.get("DATA_ID");
		String dataType = (String) posCommandMap.get("DATA_TYPE");
		
		String dataBase = (String)posCommandMap.get("DATA_BASE");
		
		log.info("crtSoHeadBean dataId = " + dataId+"---dataBase = "+dataBase);

		posCommandMap.put("orderTypeCode", dataType);
		posCommandMap.put("identification", "POS");
		posCommandMap.put("opUser", "SYS");

		//Maco 班次 2015.09.30//Maco 統編 2016.08.24
		String posSoItemSql = " select distinct SALES_ORDER_DATE , DEPARTURE_DATE , TRANSACTION_SEQ_NO , ACTION , SCHEDULE , UNION_NO , CUSTOMS_NO  from POS.POS_SALES_ORDER_ITEM where DATA_ID = ?  ";
		
		String posSoHeadMysql = " select SALES_ORDER_DATE , DEPARTURE_DATE , TRANSACTION_SNO , TRAN_ALLOW_ACTION , SCHEDULE , GUI_CODE from pos_sales_order_head where DATA_ID = ?  ";

		try {

			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
				
				stmt = conn.prepareStatement(posSoHeadMysql);
				stmt.setString(1, dataId);
				rs = stmt.executeQuery();


				if (rs != null) {
					while (rs.next()) {
						// 組出so的head
						SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
						salesOrderHead.setBrandCode(brandCode);
						salesOrderHead.setOrderTypeCode("SOP");
						salesOrderHead.setPaymentTermCode("Z9");
						salesOrderHead.setCurrencyCode("NTD");
						salesOrderHead.setDiscountRate(100D);
						salesOrderHead.setExportExchangeRate(1D);
						salesOrderHead.setInvoiceTypeCode("2");
						salesOrderHead.setSufficientQuantityDelivery("Y");
						if("D".equals(rs.getString("TRAN_ALLOW_ACTION")))
							salesOrderHead.setStatus(OrderStatus.VOID);
						else	
							salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
						salesOrderHead.setReserve4((String)posCommandMap.get("DATA_ID"));
						salesOrderHead.setCreationDate(currentDateTime);
						salesOrderHead.setLastUpdateDate(currentDateTime);
						salesOrderHead.setCreatedBy("POS");
						salesOrderHead.setLastUpdatedBy("POS");
						
						Date salesOrderDate = rs.getDate("SALES_ORDER_DATE");
						Date departureDate = rs.getDate("DEPARTURE_DATE");
						String schdeule = rs.getString("SCHEDULE");
						String unionNo = "";
						try{
							unionNo = rs.getString("GUI_CODE");
						}
						catch(Exception e)
						{
							log.info("無統編");
							unionNo = "";
						}
						//if((交易日>離境日)&班次==2) 清機後銷售，設為隔天第一班 2016.01.28
						if((salesOrderDate.after(departureDate)) && schdeule.equals("2")){
							salesOrderHead.setSchedule("1");//Maco 班次 2015.09.30
						}else{
							salesOrderHead.setSchedule(schdeule);//Maco 班次 2015.09.30
						}
						salesOrderHead.setGuiCode(unionNo);//Maco 統編 2016.08.24
						//組出so的item
						
						qryPOSOrderItem(salesOrderHead, posCommandMap, rs.getString("TRANSACTION_SNO"));
						//組出so的payment
						qryPOSOrderPayment(salesOrderHead, posCommandMap, rs.getString("TRANSACTION_SNO"));

						entityBeans.add(salesOrderHead);
						responseId = 1L;
						posCommandMap.put("responseId", responseId);					
					}
				}else{
					responseId = -4L;
					posCommandMap.put("responseId", responseId);
					throw new Exception("查無DataId: "+ dataId +" 對應之銷售資料");
				}
				
				
			}else{
				conn = dataSource.getConnection();
				
				stmt = conn.prepareStatement(posSoItemSql);
				stmt.setString(1, dataId);
				rs = stmt.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						// 組出so的head
						SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
						salesOrderHead.setBrandCode(brandCode);
						salesOrderHead.setOrderTypeCode("SOP");
						salesOrderHead.setPaymentTermCode("Z9");
						salesOrderHead.setCurrencyCode("NTD");
						salesOrderHead.setDiscountRate(100D);
						salesOrderHead.setExportExchangeRate(1D);
						salesOrderHead.setInvoiceTypeCode("2");
						salesOrderHead.setSufficientQuantityDelivery("Y");
						if("D".equals(rs.getString("ACTION")))
							salesOrderHead.setStatus(OrderStatus.VOID);
						else	
							salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
						salesOrderHead.setReserve4((String)posCommandMap.get("DATA_ID"));
						salesOrderHead.setCreationDate(currentDateTime);
						salesOrderHead.setLastUpdateDate(currentDateTime);
						salesOrderHead.setCreatedBy("POS");
						salesOrderHead.setLastUpdatedBy("POS");
						
						Date salesOrderDate = rs.getDate("SALES_ORDER_DATE");
						Date departureDate = rs.getDate("DEPARTURE_DATE");
						String schdeule = rs.getString("SCHEDULE");
						String unionNo = "";
						try{
							unionNo = rs.getString("UNION_NO");
						}
						catch(Exception e)
						{
							log.info("無統編");
							unionNo = "";
						}
						//if((交易日>離境日)&班次==2) 清機後銷售，設為隔天第一班 2016.01.28
						if((salesOrderDate.after(departureDate)) && schdeule.equals("2")){
							salesOrderHead.setSchedule("1");//Maco 班次 2015.09.30
						}else{
							salesOrderHead.setSchedule(schdeule);//Maco 班次 2015.09.30
						}
						salesOrderHead.setGuiCode(unionNo);//Maco 統編 2016.08.24
						//組出so的item
						
						qryPOSOrderItem(salesOrderHead, posCommandMap, rs.getString("TRANSACTION_SEQ_NO"));
						//組出so的payment
						qryPOSOrderPayment(salesOrderHead, posCommandMap, rs.getString("TRANSACTION_SEQ_NO"));

						entityBeans.add(salesOrderHead);
						responseId = 1L;
						posCommandMap.put("responseId", responseId);					
					}
				}else{
					responseId = -4L;
					posCommandMap.put("responseId", responseId);
					throw new Exception("查無DataId: "+ dataId +" 對應之銷售資料");
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
		return entityBeans;
	}

	/**
	 * 由dataId與交易序號組出SO的ITEM
	 * @throws Exception
	 */
	private void qryPOSOrderItem(SoSalesOrderHead soSalesOrderHead, Map posCommandMap, String transactionSeqNo) throws Exception {
		//log.info("qryPOSOrderItem transactionSeqNo = " + transactionSeqNo);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dataId = (String) posCommandMap.get("DATA_ID");
		Long responseId = (Long)posCommandMap.get("responseId");
		String dataBase = (String)posCommandMap.get("DATA_BASE");

		String posSoItemSql = " select * from POS.POS_SALES_ORDER_ITEM where DATA_ID = ? and TRANSACTION_SEQ_NO = ? order by INDEX_NO ";
		try {
			List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
			
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}			
			
			stmt = conn.prepareStatement(posSoItemSql);
			stmt.setString(1, dataId);
			stmt.setString(2, transactionSeqNo);
			rs = stmt.executeQuery();
			if (rs != null) {
				//這邊是head的資訊
				String posMachineCode = null;
				Date salesOrderDate = null;
				String customerCode = null;
				String customerPoNo = null;
				String countryCode = null;
				//String shopCode = null;
				String superintendentCode = null;
				Double totalOriginalSalesAmount = 0D;
				Double totalActualSalesAmount = 0D;
				String transactionTime = null;
				String passportNo = null;
				String flightNo = null;
				String vipTypeCode = null;
				Date departureDate = null;
				String ladingNo = null;
				String usedIdentification = null;
				//String orderDiscountType = null;
				String taxType = null;
				Double taxRate = null;
				Double totalTaxAmount = 0D;
				String orderDiscountType = "A"; //無折扣
				String flightNoSales = null;
				String passportNoSales = null;
				String transactionMinute = null;//---03/14 AutoUD上傳分秒
				String transactionSecond = null;
				//String importDelcNo = null;
				String combineCode = null;
				String schdeule = null;
				String customsNo = null;

				while (rs.next()) {
					//Head的值
					schdeule = rs.getString("SCHEDULE");
					
					if (null != rs.getString("POS_MACHINE_CODE"))
						posMachineCode = rs.getString("POS_MACHINE_CODE");

					if(null != rs.getDate("SALES_ORDER_DATE"))
						salesOrderDate = rs.getDate("SALES_ORDER_DATE");

					if (null != rs.getString("CUSTOMER_CODE"))
						customerCode = rs.getString("CUSTOMER_CODE");

					if (null != rs.getString("CUSTOMER_PO_NO")){
						customerPoNo = rs.getString("CUSTOMER_PO_NO");
						customerPoNo = customerPoNo.toUpperCase();
					}

					if (null != rs.getString("COUNTRY_CODE")){
						countryCode = rs.getString("COUNTRY_CODE").trim();
						if(StringUtils.hasText(countryCode)){
							if(countryCode.length() > 2)
								countryCode = countryCode.substring(0, 2);
							List countrys = buCountryDAO.findByProperty("oldCountryCode", countryCode);
							if(countrys != null && countrys.size() > 0){
								countryCode = ((BuCountry)countrys.get(0)).getCountryCode();
							}
						}
					}

					if (null != rs.getString("SUPERINTENDENT_CODE"))
						superintendentCode = rs.getString("SUPERINTENDENT_CODE");

					if (null != rs.getString("VIP_TYPE_CODE"))
						vipTypeCode = rs.getString("VIP_TYPE_CODE");

					if (null != rs.getString("TRANSACTION_TIME"))
						transactionTime = rs.getString("TRANSACTION_TIME");

					if (null != rs.getString("PASSPORT_NO"))
						passportNo = rs.getString("PASSPORT_NO");

					if (null != rs.getString("FLIGHT_NO"))
						flightNo = rs.getString("FLIGHT_NO");

					if (null != rs.getDate("DEPARTURE_DATE"))
						departureDate = rs.getDate("DEPARTURE_DATE");

					if (null != rs.getString("LADING_NO"))
						ladingNo = rs.getString("LADING_NO");

					if (null != rs.getString("USED_IDENTIFICATION"))
						usedIdentification = rs.getString("USED_IDENTIFICATION");

					if (StringUtils.hasText(usedIdentification)) {
						if (!"C".equals(orderDiscountType) && ("02".equals(usedIdentification) || "03".equals(usedIdentification))) {
							orderDiscountType = "C";
						} else if ("A".equals(orderDiscountType) && "01".equals(usedIdentification)) {
							orderDiscountType = "B";
						}
					}

					if (null != rs.getString("TAX_TYPE"))
						taxType = rs.getString("TAX_TYPE");
					taxType = "1";

					if (null != rs.getString("TAX_RATE"))
						taxRate = NumberUtils.getDouble(rs.getString("TAX_RATE"));

					if (null != rs.getString("FLIGHT_NO_SALES"))
						flightNoSales = rs.getString("FLIGHT_NO_SALES");

					if (null != rs.getString("PASSPORT_NO_SALES"))
						passportNoSales = rs.getString("PASSPORT_NO_SALES");

					if (null != rs.getString("TRANSACTION_MINUTE"))
						transactionMinute = rs.getString("TRANSACTION_MINUTE");

					if (null != rs.getString("TRANSACTION_SECOND"))
						transactionSecond = rs.getString("TRANSACTION_SECOND");

					//Line的值

					String itemCode = rs.getString("ITEM_CODE");

					Double originalUnitPrice = 0D;
					//if (null != rs.getString("ORIGINAL_UNIT_PRICE"))
					//originalUnitPrice = NumberUtils.getDouble(rs.getString("ORIGINAL_UNIT_PRICE"));

					//原價必須再由KWE系統中抓出最貼近的價錢
					originalUnitPrice = imItemPriceService.getUnitPriceByDate(soSalesOrderHead.getBrandCode(), itemCode, salesOrderDate);

					Double quantity = 0D;
					if (null != rs.getString("QUANTITY"))
						quantity = NumberUtils.getDouble(rs.getString("QUANTITY"));

					Double originalSalesAmount = 0D;
					//if (null != rs.getString("ORIGINAL_SALES_AMOUNT"))
					//originalSalesAmount = NumberUtils.getDouble(rs.getString("ORIGINAL_SALES_AMOUNT"));
					originalSalesAmount = originalUnitPrice * quantity;

					Double actualSalesAmount = 0D;
					if (null != rs.getString("ACTUAL_SALES_AMOUNT"))
						actualSalesAmount = NumberUtils.getDouble(rs.getString("ACTUAL_SALES_AMOUNT"));

					Double discountRate = getDiscountRate(actualSalesAmount, originalSalesAmount);
					//if (null != rs.getString("DISCOUNT_RATE"))
					//discountRate = NumberUtils.getDouble(rs.getString("DISCOUNT_RATE"));

					Double actualUnitPrice = 0D;
					//if (null != rs.getString("ACTUAL_UNIT_PRICE"))
					//actualUnitPrice = NumberUtils.getDouble(rs.getString("ACTUAL_UNIT_PRICE"));
					if(quantity > 0)
						actualUnitPrice = actualSalesAmount / quantity;

					Double discountAmount = 0D;
					if (null != rs.getString("DISCOUNT_AMOUNT"))
						discountAmount = NumberUtils.getDouble(rs.getString("DISCOUNT_AMOUNT"));

					Double usedDiscountRate = 0D;
					if (null != rs.getString("USED_DISCOUNT_RATE"))
						usedDiscountRate = NumberUtils.getDouble(rs.getString("USED_DISCOUNT_RATE"));

					Double taxAmount = 0D;
					if (null != rs.getString("TAX_AMOUNT"))
						taxAmount = NumberUtils.getDouble(rs.getString("TAX_AMOUNT"));
					totalTaxAmount += taxAmount;

					totalOriginalSalesAmount = totalOriginalSalesAmount + originalSalesAmount;
					totalActualSalesAmount = totalActualSalesAmount + actualSalesAmount; 

					if (null != rs.getString("COMBINE_CODE")){
						combineCode = rs.getString("COMBINE_CODE");
					}else{
						combineCode = "";
					}
					
					if (null != rs.getString("CUSTOMS_NO")){
						customsNo = rs.getString("CUSTOMS_NO");
						customsNo = customsNo.toUpperCase();
					}else{
						customsNo = "";
					}

					SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
					//2014/03/21上傳報單

					String	importDelcNo = rs.getString("O_DECL_NO");
					//long importDelcSeq = (long) 0D;
					long importDelcSeq = NumberUtils.getLong(rs.getString("O_DECL_SEQ"));

					ImItem imItemPO = imItemDAO.findImItem(soSalesOrderHead.getBrandCode(), itemCode,"Y");
					if(imItemPO == null){
						responseId = 6L;
						posCommandMap.put("responseId", responseId);
						throw new Exception("查無品號(" + rs.getString("ITEM_CODE") + ")的相關資料！");
					}else{
						String isTax = imItemPO.getIsTax();
						if(!StringUtils.hasText(isTax)){
							throw new Exception("品號(" + rs.getString("ITEM_CODE") + ")的稅別為空值！");
						}
						salesOrderItem.setIsTax(imItemPO.getIsTax());
						salesOrderItem.setIsServiceItem(imItemPO.getIsServiceItem());
						salesOrderItem.setIsComposeItem(imItemPO.getIsComposeItem());
						salesOrderItem.setAllowMinusStock(imItemPO.getAllowMinusStock()); //是否允許負庫存
					}

					salesOrderItem.setItemCode(itemCode);
					salesOrderItem.setWarehouseCode(rs.getString("SHOP_CODE"));
					salesOrderItem.setOriginalForeignUnitPrice(originalUnitPrice);
					salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
					salesOrderItem.setQuantity(quantity);
					salesOrderItem.setOriginalForeignSalesAmt(originalSalesAmount);
					salesOrderItem.setOriginalSalesAmount(originalSalesAmount);
					salesOrderItem.setActualForeignSalesAmt(actualSalesAmount);
					salesOrderItem.setActualSalesAmount(actualSalesAmount);
					salesOrderItem.setPosActualSalesAmount(actualSalesAmount);// 放入POS檔案裏的實際銷售金額
					salesOrderItem.setActualForeignUnitPrice(actualUnitPrice);
					salesOrderItem.setActualUnitPrice(actualUnitPrice);
					salesOrderItem.setDiscount(discountAmount);
					salesOrderItem.setDiscountRate(discountRate);
					salesOrderItem.setVipPromotionCode(vipTypeCode);
					salesOrderItem.setScheduleShipDate(salesOrderDate);
					salesOrderItem.setShippedDate(salesOrderDate);
					salesOrderItem.setTaxType(taxType);
					salesOrderItem.setTaxRate(taxRate);
					salesOrderItem.setTaxAmount(taxAmount);
					salesOrderItem.setUsedIdentification(usedIdentification);
					salesOrderItem.setUsedCardId(rs.getString("USED_CARD_ID"));
					salesOrderItem.setUsedCardType(rs.getString("USED_CARD_TYPE"));
					salesOrderItem.setUsedDiscountRate(usedDiscountRate);
					salesOrderItem.setItemDiscountType(rs.getString("ITEM_DISCOUNT_TYPE"));
					salesOrderItem.setPosSeq(rs.getLong("INDEX_NO")); // 此交易明細序號
					salesOrderItem.setLotNo(rs.getString("LOT_NO"));
					salesOrderItem.setLastUpdatedBy("SYS");
					salesOrderItem.setLastUpdateDate(new Date());
					salesOrderItem.setCreatedBy("SYS");
					salesOrderItem.setCreationDate(new Date());
					//2014/03/21上傳報單
					salesOrderItem.setImportDeclNo(importDelcNo);
					salesOrderItem.setImportDeclSeq(importDelcSeq);
					//2014/03/26上傳報單日期
					List <CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", importDelcNo);
					if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
						CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
						salesOrderItem.setImportDeclDate(cmDeclarationHead.getImportDate());
						salesOrderItem.setImportDeclType(cmDeclarationHead.getDeclType());
					}

					salesOrderItem.setCombineCode(combineCode);

					soSalesOrderItems.add(salesOrderItem);

				}
				
				//增加清機後，班次未跳隔日的第一班，時間為00:00:00
				if((salesOrderDate.after(departureDate)) && schdeule.equals("2"))
				{
					transactionTime = "00";
					transactionMinute = "00";
					transactionSecond = "00";
				}

				soSalesOrderHead.setPosMachineCode(posMachineCode);
				soSalesOrderHead.setTransactionSeqNo("TD" + posMachineCode + transactionSeqNo);
				soSalesOrderHead.setCustomerCode(customerCode);
				soSalesOrderHead.setCustomerPoNo(customerPoNo);
				soSalesOrderHead.setCountryCode(countryCode);
				soSalesOrderHead.setSalesOrderDate(salesOrderDate);
				soSalesOrderHead.setScheduleCollectionDate(salesOrderDate);
				soSalesOrderHead.setScheduleShipDate(salesOrderDate);
				soSalesOrderHead.setNationalityCode(countryCode);
				soSalesOrderHead.setSuperintendentCode(superintendentCode);
				soSalesOrderHead.setVipTypeCode(vipTypeCode);
				soSalesOrderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmount);
				soSalesOrderHead.setOriginalTotalFrnSalesAmt(totalOriginalSalesAmount);
				soSalesOrderHead.setTotalActualSalesAmount(totalActualSalesAmount);
				soSalesOrderHead.setActualTotalFrnSalesAmt(totalActualSalesAmount);
				soSalesOrderHead.setExportExpense(0D);
				soSalesOrderHead.setExpenseForeignAmount(0D);
				soSalesOrderHead.setExpenseLocalAmount(0D);
				soSalesOrderHead.setExportCommissionRate(0D);
				soSalesOrderHead.setTaxType(taxType);
				soSalesOrderHead.setTaxRate(taxRate);
				soSalesOrderHead.setTaxAmount(totalTaxAmount);
				soSalesOrderHead.setTransactionTime(transactionTime);
				soSalesOrderHead.setPassportNo(passportNo);
				soSalesOrderHead.setFlightNo(flightNo);
				soSalesOrderHead.setDepartureDate(departureDate);
				soSalesOrderHead.setLadingNo(ladingNo);
				soSalesOrderHead.setCreatedBy(superintendentCode);
				soSalesOrderHead.setLastUpdatedBy(superintendentCode);
				soSalesOrderHead.setOrderDiscountType(orderDiscountType);
				soSalesOrderHead.setFlightNoSales(flightNoSales);
				soSalesOrderHead.setPassportNoSales(passportNoSales);
				soSalesOrderHead.setReserve3(DateUtils.format(salesOrderDate, DateUtils.C_DATA_PATTON_YYYYMMDD) + posMachineCode);
				soSalesOrderHead.setReserve5("POS");
				//--03/17--上傳新增分秒
				soSalesOrderHead.setTransactionMinute(transactionMinute);
				soSalesOrderHead.setTransactionSecond(transactionSecond);
				
				soSalesOrderHead.setCustomsNo(customsNo);
			}else{
				responseId = -4L;
				posCommandMap.put("responseId", responseId);
			}
			soSalesOrderHead.setSoSalesOrderItems(soSalesOrderItems);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}

	}

	private void qryPOSOrderPayment(SoSalesOrderHead soSalesOrderHead, Map posCommandMap, String transactionSeqNo) {
		//log.info("qryPOSOrderPayment transactionSeqNo = " + transactionSeqNo);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dataId = (String) posCommandMap.get("DATA_ID");
		Long responseId = (Long)posCommandMap.get("responseId");
		String dataBase = (String)posCommandMap.get("DATA_BASE");
		String posSoItemSql = " select * from POS.POS_SALES_ORDER_PAYMENT where DATA_ID = ? and TRANSACTION_SEQ_NO = ? ";
		try {
			List<SoSalesOrderPayment> soSalesOrderPayments = new ArrayList(0);
			
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}
			
			stmt = conn.prepareStatement(posSoItemSql);
			stmt.setString(1, dataId);
			stmt.setString(2, transactionSeqNo);
			rs = stmt.executeQuery();
			if (rs != null) {
				String casherCode = null;
				while (rs.next()) {
					//Head的值
					if (null != rs.getString("CASHER_CODE"))
						casherCode = rs.getString("CASHER_CODE");

					//Line的值
					Double payAmt = 0D;
					if (rs.getString("PAY_AMT") != null) {
						payAmt = NumberUtils.getDouble(rs.getString("PAY_AMT"));
					}

					Double payQty = 0D;
					if (rs.getString("PAY_QTY") != null) {
						payQty = NumberUtils.getDouble(rs.getString("PAY_QTY"));
					}

					Double exchangeRate = 0D;
					if (rs.getString("EXCHANGE_RATE") != null) {
						exchangeRate = NumberUtils.getDouble(rs.getString("EXCHANGE_RATE"));
					}

					Double payDue = 0D;
					if (rs.getString("PAY_DUE") != null) {
						payDue = NumberUtils.getDouble(rs.getString("PAY_DUE"));
					}

					Long indexNo = 0L;
					if (rs.getString("INDEX_NO") != null) {
						indexNo = NumberUtils.getLong(rs.getString("INDEX_NO"));
					}
					
					String reCheckId = "";
					if (rs.getString("RE_CHECK_ID") != null) {
						reCheckId = rs.getString("RE_CHECK_ID");
					}
					
					String orderNo = "";
					if (rs.getString("ORDER_NO") != null) {
						orderNo = rs.getString("ORDER_NO");
					}
					
					String userPayNo = "";
					if (rs.getString("USER_PAY_NO") != null) {
						userPayNo = rs.getString("USER_PAY_NO");
					}
					
					SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
					salesOrderPayment.setPosPaymentType(rs.getString("PAY_ID"));
					salesOrderPayment.setLocalCurrencyCode("NTD");
					salesOrderPayment.setLocalAmount(payDue);
					salesOrderPayment.setForeignCurrencyCode(rs.getString("CURRENCY_CODE"));
					salesOrderPayment.setForeignAmount(payAmt);
					salesOrderPayment.setExchangeRate(exchangeRate);
					salesOrderPayment.setPayNo(rs.getString("PAY_NO"));
					salesOrderPayment.setPayQty(payQty);
					salesOrderPayment.setReserve5("POS");
					salesOrderPayment.setIndexNo(indexNo);
					salesOrderPayment.setLastUpdatedBy("SYS");
					salesOrderPayment.setLastUpdateDate(new Date());
					salesOrderPayment.setCreatedBy("SYS");
					salesOrderPayment.setCreationDate(new Date());
					salesOrderPayment.setReCheckId(reCheckId);
					salesOrderPayment.setOrderNo(orderNo);
					salesOrderPayment.setUserPayNo(userPayNo);
					soSalesOrderPayments.add(salesOrderPayment);
				}
				soSalesOrderHead.setCasherCode(casherCode);
			}else{
				responseId = -4L;
				posCommandMap.put("responseId", responseId);
			}
			soSalesOrderHead.setSoSalesOrderPayments(soSalesOrderPayments);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}

	private Double getDiscountRate(Double actualSalesAmt, Double originalSalesAmt) {
		if (originalSalesAmt == 0D) {
			return 100D;
		} else if (actualSalesAmt != null && actualSalesAmt != 0D
				&& originalSalesAmt != null && originalSalesAmt != 0D) {
			return CommonUtils
			.round(actualSalesAmt / originalSalesAmt * 100, 2);
		} else {
			return 0D;
		}
	}

	/**
	 * POS上傳作業，取報單訊息、批號、檢核無誤後扣庫存並將狀態改為SIGNING.
	 * 
	 * @param entityBeans
	 * @param parameterMap
	 * @throws Exception
	 */
	public List<SoSalesOrderHead> updatePosData(HashMap posCommandMap) throws Exception {
		//log.info("===================updatePosData===================");
		Long responseId = (Long)posCommandMap.get("responseId");
		String errorMsg = null;
		String actualbrandCode = (String) posCommandMap.get("BRAND_CODE");
		List<SoSalesOrderHead> soSalesOrderHeads = null;
		String dateType = (String)posCommandMap.get("DATA_TYPE");
		try {
			soSalesOrderHeads = soSalesOrderHeadDAO.findOnlineSoBean(posCommandMap);
			if (soSalesOrderHeads != null && soSalesOrderHeads.size() > 0) {
				//log.info("===================updatePosData soSalesOrderHeads.size() : " + soSalesOrderHeads.size() + "===================");
				//List mailContents = new ArrayList(0);
				HashMap conditionMap = new HashMap();
				conditionMap.put("processObjectName", "soSalesOrderMainService");
				conditionMap.put("searchMethodName", "findSoSalesOrderHeadById");
				conditionMap.put("tableType", "SO_SALES_ORDER");
				conditionMap.put("subEntityBeanName", "soSalesOrderItems");
				conditionMap.put("itemFieldName", "itemCode");
				conditionMap.put("warehouseCodeFieldName", "warehouseCode");
				conditionMap.put("declTypeFieldName", "importDeclType");
				conditionMap.put("declNoFieldName", "importDeclNo");
				conditionMap.put("declSeqFieldName", "importDeclSeq");
				conditionMap.put("declDateFieldName", "importDeclDate");
				conditionMap.put("lotFieldName", "lotNo");
				conditionMap.put("qtyFieldName", "quantity");
				conditionMap.put("perUnitAmountFieldName", "perUnitAmount");
				//log.info("===================判斷是否展報單===================");
				if(!dateType.equals("SVP")){ //單別SVP不展報單
				for (SoSalesOrderHead soSalesOrderHead : soSalesOrderHeads) {
					//log.info("====================soSalesOrderHead.headId : " + soSalesOrderHead.getHeadId() + "====================");
					//log.info("====================soSalesOrderHead.status : " + soSalesOrderHead.getStatus() + "====================");
					//如果單據作廢或是已結帳 就不展報單了
					if(!OrderStatus.SIGNING.equals(soSalesOrderHead.getStatus()) 
							&& !OrderStatus.UNCONFIRMED.equals(soSalesOrderHead.getStatus())
							&& !OrderStatus.SAVE.equals(soSalesOrderHead.getStatus())
							&& !OrderStatus.VOID.equals(soSalesOrderHead.getStatus())){
						continue;
					}

					Long soSalesOrderHeadId = soSalesOrderHead.getHeadId();
					//取店別
					//log.info("====================soSalesOrderHead.headId : " + soSalesOrderHead.getHeadId() + "，shopCode : "+soSalesOrderHead.getShopCode()+"====================");
					BuShop buShop = buShopDAO.findById(soSalesOrderHead.getShopCode());
					if(null == buShop){
						responseId=99L;
						posCommandMap.put("responseId", responseId);
						throw new Exception("查無店號: "+soSalesOrderHead.getShopCode()+"之專櫃店號");
					}
					if(null == buShop.getShopSalesType())
					{
						responseId=99L;
						posCommandMap.put("responseId", responseId);
						throw new Exception("店號: "+soSalesOrderHead.getShopCode()+"尚未設定是否展報單");
					}
					//BuCommonPhraseLine line = buCommonPhraseService.getBuCommonPhraseLine("ShopSalesType", buShop.getShopSalesType());
						if(buShop.getShopSalesType().equals("2")){
							//log.info("===================展報單 soSalesOrderHead.headId : " + soSalesOrderHead.getHeadId() + "===================");
							conditionMap.put("searchKey", soSalesOrderHeadId);
							appExtendItemInfoService.executeExtendItemProcessor(conditionMap);
							soSalesOrderHead = (SoSalesOrderHead)soSalesOrderHeadDAO.findById("SoSalesOrderHead", soSalesOrderHead.getHeadId());
							Date date = new Date();
							BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("StraightUploadCustoms", "Switch");
							if("Y".equals(buCommonPhraseLine.getEnable()) && !(date.before(soSalesOrderHead.getSalesOrderDate()))){
							    soSalesOrderHead.setCustomsStatus("A");
								soSalesOrderHead.setTranAllowUpload("I");
							}
						}else if(buShop.getShopSalesType().equals("")){
							throw new Exception("店號: "+soSalesOrderHead.getShopCode()+" 之展報單設定: " + buShop.getShopSalesType() +" 查無對應功能");
						}
					String identification = MessageStatus.getIdentification(soSalesOrderHead.getBrandCode(), 
					soSalesOrderHead.getOrderTypeCode(), soSalesOrderHead.getOrderNo());
					List assemblyMsg = null;//soSalesOrderMainService.countTotalAmountForT2Pos(null, null, null, identification, "POS", soSalesOrderHead);
					if(assemblyMsg.size() > 0)
					{
						responseId=5L;
						posCommandMap.put("responseId", responseId);
						log.error("重新計算Item金額失敗");
						throw new Exception("重新計算Item金額失敗");
					}
					
				}
				}else{
					log.info("單別SVP不展報單");
				}
				// 寄送mail
				// soSalesOrderMainService.mailToPOSAdministratorForT2(
				// parameterMap, mailContents);
			}
		} catch (Exception ex) {
			errorMsg = "更新品牌代號(" + actualbrandCode + ")、POS機碼(" + (String) posCommandMap.get("MACHINE_CODE") + ")、DATA_ID(" + posCommandMap.get("DATA_ID") + ")的POS銷售資料時發生錯誤，原因：" + ex.getMessage();
			log.error(errorMsg);
			responseId=5L;
			posCommandMap.put("responseId", responseId);
			throw new Exception(errorMsg);
		}
		return soSalesOrderHeads;
	}

	/**
	 * 更新銷售單主單及明細檔、預訂可用庫存、產生出貨單明細檔、並取單號
	 * 
	 * @param parameterMap
	 * @return Map 
	 * @throws Exception
	 */
	public void updateSOToDelivery(SoSalesOrderHead soSalesOrderHead) throws  Exception {
		List errorMsgs = new ArrayList();
		String NotLockError = "";
		String identification = MessageStatus.getIdentification(soSalesOrderHead.getBrandCode(),
				soSalesOrderHead.getOrderTypeCode(), soSalesOrderHead.getOrderNo());
		try {
	/*測試進Exception用--BRIAN****************************/
			//if(true) throw new CannotAcquireLockException(""); 	 
	/*測試進Exception用--BRIAN****************************/
			// ===================扣庫存、轉出貨單========================================
//			Object[] objArray = null;
//			objArray = soSalesOrderMainService.bookAvailableQuantityForT2(soSalesOrderHead, "TM", "POS", errorMsgs);
			//soSalesOrderMainService.bookDeductStock(soSalesOrderHead, "TM", "POS", errorMsgs);
			if (errorMsgs.size() > 0){
				NotLockError = errorMsgs.toString() ;
				throw new FormException(MessageStatus.VALIDATION_FAILURE);
			}


			//for 儲位用
//			if(imStorageAction.isStorageExecute(soSalesOrderHead)){
//			//異動儲位庫存
//			imStorageService.updateStorageOnHandBySource(soSalesOrderHead, OrderStatus.FINISH, PROGRAM_ID, null, false);
//			}

			// 取轉出貨單明細
			//soSalesOrderMainService.salesOrderToDeliveryForT2(soSalesOrderHead, "POS");
			soSalesOrderHead.setStatus(OrderStatus.SIGNING);
			soSalesOrderHeadDAO.update(soSalesOrderHead);
			

		}catch (CannotAcquireLockException cale) {
			WriteToSiResend(soSalesOrderHead.getHeadId(), soSalesOrderHead.getOrderNo(), soSalesOrderHead.getOrderTypeCode(),"Y");
			log.error("銷貨單發生錯誤，原因如下：" + cale.getMessage());
			throw new FormException("資料已鎖定，請稍後再試！");
		}catch (Exception ex) {
			siProgramLogAction.createProgramLog(PROGRAM_ID,MessageStatus.LOG_ERROR, identification, NotLockError, "POS");

			log.error("銷貨單扣庫存時發生錯誤，原因：" + ex.getMessage());
			throw new Exception("銷貨單扣庫存時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 依據primary key為查詢條件，取得銷貨單主檔
	 * 
	 * @param headId
	 * @return SoSalesOrderHead
	 * @throws Exception
	 */
	public SoSalesOrderHead findById(Long headId) throws Exception {
		try {
			SoSalesOrderHead salesOrder = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, headId);
			return salesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public Double calculateTaxAmount(String brandCode, String taxType,
			Double taxRate, Double actualSalesAmount) {
		Double taxAmount = 0D;
		if ("3".equals(taxType) && taxRate != null && taxRate != 0D
				&& actualSalesAmount != null && actualSalesAmount != 0D) {
			if (!"T2".equals(brandCode)) {
				Double salesAmount = actualSalesAmount / (1 + taxRate / 100);
				taxAmount = actualSalesAmount - salesAmount;
			} else {
				taxAmount = (actualSalesAmount * taxRate) / 100;
			}
		}
		return CommonUtils.round(taxAmount, 2);
	}

	public Double calculateActualPrice(HashMap parameterMap) {
		Double originalUnitPrice = (Double) parameterMap.get("originalUnitPrice");
		String promotionCode = (String) parameterMap.get("promotionCode");
		String discountType = (String) parameterMap.get("discountType");
		Double discount = (Double) parameterMap.get("discount");
		String vipPromotionCode = (String) parameterMap.get("vipPromotionCode");
		String vipDiscountType = (String) parameterMap.get("vipDiscountType");
		Double vipDiscount = (Double) parameterMap.get("vipDiscount");
		Double discountRate = (Double) parameterMap.get("discountRate");
		Double deductionAmount = (Double) parameterMap.get("deductionAmount");
		Double actualUnitPrice = originalUnitPrice;

		if (originalUnitPrice != null) {
			if (StringUtils.hasText(promotionCode) && discount != null)
				actualUnitPrice = calculateDiscountedPrice(actualUnitPrice,
						discountType, discount);
			if (StringUtils.hasText(vipPromotionCode) && vipDiscount != null)
				actualUnitPrice = calculateDiscountedPrice(actualUnitPrice,
						vipDiscountType, vipDiscount);
			// 折扣率為正向扣除
			if (discountRate != null)
				actualUnitPrice = actualUnitPrice * discountRate / 100;
			// 扣除折讓金額
			if (deductionAmount != null && deductionAmount != 0D)
				actualUnitPrice -= deductionAmount;

			return CommonUtils.round(actualUnitPrice, 0);
		} else {
			return null;
		}
	}

	/**
	 * 計算折扣金額.
	 * 
	 * @param unitPrice
	 * @param discountType
	 * @param discount
	 * @return
	 */
	private Double calculateDiscountedPrice(Double unitPrice,
			String discountType, Double discount) {
		if ("1".equals(discountType))
			unitPrice = unitPrice - discount;
		else if (discountType == null || "2".equals(discountType))
			unitPrice = unitPrice * (100 - discount) / 100;
		return unitPrice;
	}

	/**
	 * T2的POS上傳作業，刪除原銷售、出貨資料並進行第一次存檔
	 * 
	 * @param entityBeans
	 * @param parameterMap
	 * @throws Exception
	 */
	public void saveT2PosNoValidate(List entityBeans, HashMap parameterMap) throws FormException,Exception{
		// ================POS重傳時刪除相關銷售、出貨資料=================
		String brandCode = (String) parameterMap.get("BRAND_CODE");
		String posMachineCode = (String) parameterMap.get("MACHINE_CODE");
		String action = (String)parameterMap.get("action");
		String organizationCode = "TM";
		boolean createNewSO = true;
		String customsStatus = null;
		String tranAllowUpload = null;
		boolean copyCustomsStatus = false;
		
		try {
			for (Iterator iterator = entityBeans.iterator(); iterator.hasNext();) {
				SoSalesOrderHead soSalesOrderHead = (SoSalesOrderHead) iterator.next();
				parameterMap.put("salesOrderDate", soSalesOrderHead.getSalesOrderDate());
				parameterMap.put("transactionSeqNo", soSalesOrderHead.getTransactionSeqNo());
				//撈出DB中同樣交易序號的單據
				List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findOnlineSoBean(parameterMap);

				//將DB裡面的SO撈出來做反確認，然後刪除
				if (salesOrderHeads != null) {
					for (SoSalesOrderHead salesOrderHead : salesOrderHeads) {
						String identification = MessageStatus.getIdentificationMsg(salesOrderHead.getBrandCode(),  salesOrderHead.getOrderTypeCode(), salesOrderHead.getOrderNo());
						String currentStatus = salesOrderHead.getStatus();
						customsStatus = salesOrderHead.getCustomsStatus();
						tranAllowUpload = salesOrderHead.getTranAllowUpload();
						
						try{
							//檢核這張單據 如果已經過結帳日，此次上傳就不存DB
							ValidateUtil.isAfterClose(brandCode, salesOrderHead.getOrderTypeCode(), "D", salesOrderHead.getSalesOrderDate(),salesOrderHead.getSchedule());
							if (!OrderStatus.SIGNING.equals(currentStatus) && !OrderStatus.UNCONFIRMED.equals(currentStatus)
									&& !OrderStatus.VOID.equals(currentStatus) && !OrderStatus.SAVE.equals(currentStatus)) {
								createNewSO = false;
								//throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
							} else if (OrderStatus.SIGNING.equals(currentStatus)) {
								soSalesOrderMainService.revertToOriginallyAvailableQuantity(salesOrderHead,organizationCode, "POS");

								//for 儲位用
								if(imStorageAction.isStorageExecute(salesOrderHead)){
									//異動儲位庫存
									imStorageService.updateStorageOnHandBySource(salesOrderHead, OrderStatus.SAVE, PROGRAM_ID, null, true);
								}

								siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
								//舊單據customsStatus為N02、N09、E25、E29且上傳單據U   需保留customsStatus與tranAllowUpload
								//舊單據customsStatus為N09、E29且作廢單據D   需保留customsStatus與tranAllowUpload
								if("N09".equals(customsStatus) || "E29".equals(customsStatus) || ("U".equals(action) && ("N02".equals(customsStatus) || "E25".equals(customsStatus)))){
									copyCustomsStatus = true; 
								}
								soSalesOrderHeadDAO.delete(salesOrderHead);
							} else if (OrderStatus.VOID.equals(currentStatus) || OrderStatus.UNCONFIRMED.equals(currentStatus) 
									|| OrderStatus.SAVE.equals(currentStatus)) {
								siProgramLogAction.deleteProgramLog(PROGRAM_ID, null,identification);
								//舊單據customsStatus為N02、N09、E25、E29且上傳單據U   需保留customsStatus與tranAllowUpload
								//舊單據customsStatus為N09、E29且作廢單據D   需保留customsStatus與tranAllowUpload
								if("N09".equals(customsStatus) || "E29".equals(customsStatus) || ("U".equals(action) && ("N02".equals(customsStatus) || "E25".equals(customsStatus)))){
									copyCustomsStatus = true;
								}
								soSalesOrderHeadDAO.delete(salesOrderHead);
							}
						}catch(FormException e){
							throw new FormException(e.getMessage());
						}catch(Exception e){
							e.printStackTrace();
							createNewSO = false;
						}
					}
				}

				//如果有刪除新的SO
				if(createNewSO){
					//取shopCode與warehouseCode
					BuShop buShop = buShopMachineDAO.getShopCodeByMachineCode(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getPosMachineCode(), "Y", "Y");
					if(null != buShop){
						if(copyCustomsStatus){
							soSalesOrderHead.setCustomsStatus(customsStatus);
							soSalesOrderHead.setTranAllowUpload(tranAllowUpload);
						}
						soSalesOrderHead.setShopCode(buShop.getShopCode());
						soSalesOrderHead.setDefaultWarehouseCode(buShop.getShopCode());
						List<SoSalesOrderItem> soSalesOrderItems = soSalesOrderHead.getSoSalesOrderItems();
						for (Iterator iterator2 = soSalesOrderItems.iterator(); iterator2.hasNext();) {
							SoSalesOrderItem soSalesOrderItem = (SoSalesOrderItem) iterator2.next();
							soSalesOrderItem.setWarehouseCode(buShop.getShopCode());
						}
					} else {
						throw new Exception("查無機台號碼："+soSalesOrderHead.getPosMachineCode()+"對應之店號");
					}

					//取單號
					String serialNo = "";
					if ("SOP".equals(soSalesOrderHead.getOrderTypeCode())) {
						serialNo = buOrderTypeService.getOrderNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode(),
								soSalesOrderHead.getShopCode(), soSalesOrderHead.getPosMachineCode());
					} else {
						serialNo = buOrderTypeService.getOrderSerialNo(soSalesOrderHead.getBrandCode(), soSalesOrderHead.getOrderTypeCode());
					}

					if (!serialNo.equals("unknow")) {
						soSalesOrderHead.setOrderNo(serialNo);
					} else {
						throw new ObtainSerialNoFailedException("取得" + soSalesOrderHead.getOrderTypeCode() + "單號失敗！");
					}
					soSalesOrderHeadDAO.save(soSalesOrderHead);
					
				}
			}
			parameterMap.remove("transactionSeqNo");
		} catch (Exception ex) {
			log.error("刪除品牌代號(" + brandCode + ")、pos機碼(" + posMachineCode + ")、的銷售、出貨資料時發生錯誤，原因：" + ex.getMessage());
			throw new Exception("刪除品牌代號(" + brandCode + ")、pos機碼(" + posMachineCode + ")、的銷售、出貨資料失敗！" + ex.getMessage());
		}
	}

	public void updateCleanSO(HashMap parameterMap) throws Exception{
		//撈出DB中同樣交易序號的單據
		String posMachineCode = (String)parameterMap.get("MACHINE_CODE");
		Date salesOrderDate = (Date)parameterMap.get("salesOrderDate");
		log.info("updateCleanSO posMachineCode = " + posMachineCode + " , salesOrderDate = " + salesOrderDate);
		List<SoSalesOrderHead> salesOrderHeads = soSalesOrderHeadDAO.findOnlineSoBean(parameterMap);
		log.info("salesOrderHeads.size = " + salesOrderHeads.size());
		//將DB裡面的SO撈出來做反確認，然後刪除
		if (salesOrderHeads != null) {
			for (SoSalesOrderHead salesOrderHead : salesOrderHeads) {
				//String identification = MessageStatus.getIdentificationMsg(salesOrderHead.getBrandCode(),  salesOrderHead.getOrderTypeCode(), salesOrderHead.getOrderNo());
				String currentStatus = salesOrderHead.getStatus();
				if (!OrderStatus.SIGNING.equals(currentStatus) && !OrderStatus.UNCONFIRMED.equals(currentStatus)
						&& !OrderStatus.VOID.equals(currentStatus) && !OrderStatus.SAVE.equals(currentStatus)) {
					//throw new ValidationErrorException(identification + "的狀態為" + OrderStatus.getChineseWord(salesOrderHead.getStatus()) + "不可執行反確認！");
				} else if (OrderStatus.SIGNING.equals(currentStatus)) {
					soSalesOrderMainService.revertToOriginallyAvailableQuantity(salesOrderHead,"TM", "POS");
					soSalesOrderHeadDAO.delete(salesOrderHead);
				}else if (OrderStatus.SAVE.equals(currentStatus) || OrderStatus.UNCONFIRMED.equals(currentStatus) || OrderStatus.VOID.equals(currentStatus)) {
					soSalesOrderHeadDAO.delete(salesOrderHead);
				}
			}
		}
	}

	public void executeDownloadTransfer(HashMap posDUMap) throws Exception{
		String dataId = (String)posDUMap.get("DATA_ID");
		log.info("DATA_ID = " + dataId);
		String transactionSeqNo = (String)posDUMap.get("TRANSACTION_SEQ_NO");
		log.info("TRANSACTION_SEQ_NO = " + transactionSeqNo);

		Connection conn = null;
		PreparedStatement stmt = null;
		String insertSoSql = " INSERT INTO POS.POS_SALES_ORDER_ITEM (DATA_ID, ACTION, TRANSACTION_SEQ_NO, INDEX_NO) VALUES ( ?, 'D', ?, 1) ";
		conn = dataSource.getConnection();
		stmt = conn.prepareStatement(insertSoSql);
		stmt.setString(1, dataId);
		stmt.setString(2, transactionSeqNo);
		stmt.execute();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setAppExtendItemInfoService(
			AppExtendItemInfoService appExtendItemInfoService) {
		this.appExtendItemInfoService = appExtendItemInfoService;
	}

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	public void setSoSalesOrderMainService(
			SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}

	public void setImItemPriceService(ImItemPriceService imItemPriceService) {
		this.imItemPriceService = imItemPriceService;
	}

	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
		this.soPostingTallyDAO = soPostingTallyDAO;
	}

	public void setSoPostingTallyService(SoPostingTallyService soPostingTallyService) {
		this.soPostingTallyService = soPostingTallyService;
	}

	public void setImStorageAction(ImStorageAction imStorageAction) {
		this.imStorageAction = imStorageAction;
	}

	public void setImStorageService(ImStorageService imStorageService) {
		this.imStorageService = imStorageService;
	}
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	// 整批POSLOG 轉入ERPLOG--------------------------------------	
	public Long executeUploadLog(HashMap posCommandMap) throws Exception{

		Long responseId = -1L;    	
		//	String uuId = posExportDAO.getDataId();// 產生dataId  	
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Date currentDateTime = new Date();
		List entityBeans = new ArrayList();
		String dataId = (String) posCommandMap.get("DATA_ID");
		log.info("dataId=" + dataId );
		//組出POSLOG	         在POSLOG把DataId找出
		String posSoItemSql = " select * from POS.POS_LOG where DATA_ID = ? ";
		try{    		
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(posSoItemSql);
			stmt.setString(1, dataId);
			rs = stmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					// rs(poslog)的資料複寫至SiSystemLog
					SiSystemLog sisystemlog = new SiSystemLog();
					sisystemlog.setProcessName("T2_POS_LOG");
					sisystemlog.setAction(rs.getString("EVENT"));
					sisystemlog.setMessage(rs.getString("CONTENT"));
					sisystemlog.setProcessLotNo(rs.getString("TRANSACTION_SEQ_NO"));
					sisystemlog.setMachineCode(rs.getString("POS_MACHINE_CODE"));
					sisystemlog.setExecuteDate (DateUtils.format( rs.getDate("SALES_ORDER_DATE"), DateUtils.C_DATA_PATTON_YYYYMMDD ) );
					sisystemlog.setExecuteTime (DateUtils.format( rs.getTime("SALES_ORDER_DATE"), "HHmmss" ) );
					sisystemlog.setCreationDate(rs.getDate("CREATION_DATE")); 
					sisystemlog.setCreatedBy(rs.getString("CREATED_BY"));
					sisystemlog.setBrandCode("T2");
					baseDAO.save(sisystemlog);
					log.info("=======save=======");
					responseId = 0L;

				}
			}else{
				throw new Exception("查無DataId: "+ dataId +" 對應之銷售資料");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
		return responseId;
	}


	// POS上傳POS_SHOP_Daily金額筆數 轉入SoShopDailyHead--------------------------------------	
	public Long executeUploadDaily(HashMap posCommandMap) throws Exception{

		Long responseId = -1L;    	
		//	String uuId = posExportDAO.getDataId();// 產生dataId  	
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Date currentDateTime = new Date();
		List entityBeans = new ArrayList();
		String dataId = (String) posCommandMap.get("DATA_ID");
		log.info("dataId=" + dataId );
		//組出POS_SHOP_DAILY       在POS_SHOP_DAILY把DataId找出
		String posSoItemSql = " select * from POS.POS_SHOP_DAILY where DATA_ID = ? ";
		try{    		
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(posSoItemSql);
			stmt.setString(1, dataId);
			rs = stmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					// rs(posShopDaily)的資料複寫至SoShopDailyHead的Visitor_count & totalActualSalesAmount
					SoShopDailyHeadId soShopDailyHeadId = new SoShopDailyHeadId();
					SoShopDailyHead  soShopDailyHead = new SoShopDailyHead();
					soShopDailyHeadId.setSalesDate(rs.getDate("SALES_ORDER_DATE"));
					soShopDailyHeadId.setShopCode(rs.getString("POS_MACHINE_CODE"));
					soShopDailyHead.setId(soShopDailyHeadId);
					log.info("soShopDailyHeadId=" + soShopDailyHeadId);
					soShopDailyHead.setVisitorCount(NumberUtils.getLong(rs.getString("VISITOR_COUNT")));
					soShopDailyHead.setTotalActualSalesAmount(NumberUtils.getDouble(rs.getString("TOTAL_ACTUAL_SALES_AMOUNT")));
					//	soShopDailyHead.setExecuteDate (DateUtils.format( rs.getDate("SALES_ORDER_DATE"), DateUtils.C_DATA_PATTON_YYYYMMDD ) );
					//	soShopDailyHead.setExecuteTime (DateUtils.format( rs.getTime("SALES_ORDER_DATE"), "HHmmss" ) );
					soShopDailyHead.setCreationDate(new Date());
					soShopDailyHead.setCreatedBy("SYSTEM");
					baseDAO.save(soShopDailyHead);
					log.info("=======save=======");

					SoPostingTallyId soPostingTallyId = new SoPostingTallyId(rs.getString("POS_MACHINE_CODE"), rs.getDate("SALES_ORDER_DATE"));
					SoPostingTally soPostingTally = (SoPostingTally) soPostingTallyDAO.findByPosingId(soPostingTallyId);
					if(null == soPostingTally){
						Date executeDate =  rs.getDate("SALES_ORDER_DATE");
						String brandCode = "T2";
						String loginUser = "SYSTEM";

						//soPostingTallyService.createPostingTallyWithRangeOfDate(executeDate , brandCode, loginUser);
						soPostingTallyService.createT2PostingTallyWithRangeOfDate(executeDate, brandCode, loginUser);
						/*	
    				soPostingTallyId.setShopCode(soShopDailyHeadId.getShopCode());
    				soPostingTallyId.setTransactionDate(soShopDailyHeadId.getSalesDate());
    				soPostingTally.setId(soPostingTallyId);
    				soPostingTally.setBrandCode("T2");
    				soPostingTally.setIsPosting("N");
    				soPostingTally.setCreatedBy("SYSTEM");
    				soPostingTally.setCreateDate(new Date());
    				baseDAO.save(soPostingTally);
						 */
						log.info("=======savePosting=======");


					}
					responseId = 0L;
				}
			}else{
				throw new Exception("查無DataId: "+ dataId +" 對應之銷售資料");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
		return responseId;
	}

	/**
	 * @param imMovementHead
	 * @param soSalesOrderHead
	 * @param IS_LOCK             是否為鎖定資料
	 * @param errorMsg            錯誤訊息
	 * @throws Exception
	 */
	public void WriteToSiResend(Long headId, String orderNO, String orderTypeCode, String IS_LOCK) throws Exception{
		log.info("WriteToSiResend....");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
		try{
			conn = dataSource.getConnection();
			log.info("conn success!!");
			StringBuffer sqlsel = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			stmt = conn.createStatement();
			//查詢SiResend
			sqlsel.append("select * from ERP.SI_RESEND where ORDER_NO='").append(orderNO).append("'")
			.append(" and ORDER_TYPE_CODE='").append(orderTypeCode).append("'");
			rs = stmt.executeQuery(sqlsel.toString());
			log.info(sqlsel.toString());

			if(!rs.next()){
				//insert
				sql.append("insert into ERP.SI_RESEND(HEAD_ID,IS_LOCK,ORDER_NO,ORDER_TYPE_CODE,STATUS) values(").append(headId)
				.append(",'").append(IS_LOCK).append("','").append(orderNO).append("','")
				.append(orderTypeCode).append("','NOTOK')");
				log.info(sql.toString());
				stmt.executeUpdate(sql.toString());				
				log.info("insert success!!");
			}
		}catch(Exception e){
			log.error(e);
			throw e;
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
	}
	
	/**
	 * 建立SO MYSQL 的BEAN,目前同一次資料上傳為單一機台，但有可能為不同交易序號
	 * @throws Exception
	 */
	public List crtSoHeadMySqlBean(HashMap posCommandMap) throws Exception{
		
		Long responseId = (Long)posCommandMap.get("responseId");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Date currentDateTime = new Date();
		List entityBeans = new ArrayList();
		String brandCode = (String) posCommandMap.get("BRAND_CODE");
		String dataId = (String) posCommandMap.get("DATA_ID");
		String dataType = (String) posCommandMap.get("DATA_TYPE");
		String dataBase = (String)posCommandMap.get("DATA_BASE");
		
		posCommandMap.put("orderTypeCode", dataType);
		posCommandMap.put("identification", "POS");
		posCommandMap.put("opUser", "SYS");

		String posSoHeadMysql = " select * from pos.DEM_SalesOrderHead where DATA_ID = ?  ";

		try {
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}						
			stmt = conn.prepareStatement(posSoHeadMysql);
			stmt.setString(1, dataId);
			rs = stmt.executeQuery();
			
			String posMachineCode = null;
			String shopCode = null;
			String customerCode = null;
			String customerPoNo = null;
			String countryCode = null;
			String superintendentCode = null;
			String transactionTime = null;
			String transactionMinute = null;//---03/14 AutoUD上傳分秒
			String transactionSecond = null;
			String passportNo = null;
			String flightNo = null;
			String vipTypeCode = null;
			Date salesOrderDate = null;
			Date departureDate = null;
			String transactionSeqNo = null;
			String ladingNo = null;
			String customsNo = null;
			String passportNoSales = null;
			String flightNoSales = null;
			String taxType = null;
			Double taxRate = null;
			String transactionSeqNo_new = null;
			String eventCode = null;
			String appCustomerCode = null;

			if (rs != null) {
				while (rs.next()) {
					
					// 組出so的head
					SoSalesOrderHead salesOrderHead = new SoSalesOrderHead();
					salesOrderHead.setBrandCode(brandCode);
					salesOrderHead.setOrderTypeCode(dataType); //SET 進datType
					salesOrderHead.setPaymentTermCode("Z9");
					salesOrderHead.setCurrencyCode("NTD");
					salesOrderHead.setDiscountRate(100D);
					salesOrderHead.setExportExchangeRate(1D);
					salesOrderHead.setInvoiceTypeCode("2");
					salesOrderHead.setSufficientQuantityDelivery("Y");
					if("D".equals(rs.getString("TRAN_ALLOW_ACTION"))){
						salesOrderHead.setStatus(OrderStatus.VOID);
					}else{
						salesOrderHead.setStatus(OrderStatus.UNCONFIRMED);
					}	
					salesOrderHead.setReserve4((String)posCommandMap.get("DATA_ID"));
					salesOrderHead.setCreationDate(currentDateTime);
					salesOrderHead.setLastUpdateDate(currentDateTime);
					salesOrderHead.setCreatedBy("POS");
					salesOrderHead.setLastUpdatedBy("POS");
					
					if (rs.getDate("SALES_ORDER_DATE") != null){
						salesOrderDate = rs.getDate("SALES_ORDER_DATE");
					}
					
					if (rs.getDate("DEPARTURE_DATE") != null){
						departureDate = rs.getDate("DEPARTURE_DATE");
					}
					
					String schdeule = rs.getString("SCHEDULE");
					
					String unionNo = "";
					try{
						unionNo = rs.getString("GUI_CODE");
					}catch(Exception e){
						log.info("無統編");
						unionNo = "";
					}
					
					//if((交易日>離境日)&班次==2) 清機後銷售，設為隔天第一班 2016.01.28
					if((salesOrderDate.after(departureDate)) && schdeule.equals("2")){
						salesOrderHead.setSchedule("1");//Maco 班次 2015.09.30
					}else{
						salesOrderHead.setSchedule(schdeule);//Maco 班次 2015.09.30
					}				
					
					if (null != rs.getString("POS_MACHINE_CODE")){
						posMachineCode = rs.getString("POS_MACHINE_CODE");
					}
					
					if (null != rs.getString("SHOP_CODE")){
						shopCode = rs.getString("SHOP_CODE");
					}
					
					if (null != rs.getString("CUSTOMER_CODE")){
						customerCode = rs.getString("CUSTOMER_CODE");
					}
					
					if (null != rs.getString("CUSTOMER_PO_NO")){
						customerPoNo = rs.getString("CUSTOMER_PO_NO");
						customerPoNo = customerPoNo.toUpperCase();
					}

					if (null != rs.getString("COUNTRY_CODE")){
						countryCode = rs.getString("COUNTRY_CODE").trim();
						if(StringUtils.hasText(countryCode)){
							if(countryCode.length() > 2)
								countryCode = countryCode.substring(0, 2);
							List countrys = buCountryDAO.findByProperty("oldCountryCode", countryCode);
							if(countrys != null && countrys.size() > 0){
								countryCode = ((BuCountry)countrys.get(0)).getCountryCode();
							}
						}
					}
					
					if (rs.getString("TRANSACTION_SNO") != null){
						transactionSeqNo = rs.getString("TRANSACTION_SNO");
						
						int transactionSeqNo_size = 8;
						int sourceLength = transactionSeqNo.length();
						transactionSeqNo_new = transactionSeqNo;
						
						if(sourceLength>=transactionSeqNo_size){
							transactionSeqNo_new = transactionSeqNo_new.substring(0,transactionSeqNo_size);
						}else{
							for(int i = sourceLength;i<transactionSeqNo_size;i++){
								transactionSeqNo_new = "0" + transactionSeqNo_new;
							}
						}
					}
					
					if (null != rs.getString("SUPERINTENDENT_CODE")){
						superintendentCode = rs.getString("SUPERINTENDENT_CODE");
					}
					
					if (null != rs.getString("VIP_TYPE_CODE")){
						vipTypeCode = rs.getString("VIP_TYPE_CODE");
					}
					
					if (null != rs.getString("TRANSACTION_TIME")){
						transactionTime = rs.getString("TRANSACTION_TIME");
					}
					
					if (null != rs.getString("TRANSACTION_MINUTE")){
						transactionMinute = rs.getString("TRANSACTION_MINUTE");
					}
					
					if (null != rs.getString("TRANSACTION_SECOND")){
						transactionSecond = rs.getString("TRANSACTION_SECOND");
					}
						
					if (null != rs.getString("PASSPORT_NO")){
						passportNo = rs.getString("PASSPORT_NO");
					}
					
					if (null != rs.getString("FLIGHT_NO")){
						flightNo = rs.getString("FLIGHT_NO");
					}
					
					//增加清機後，班次未跳隔日的第一班，時間為00:00:00
					if((salesOrderDate.after(departureDate)) && schdeule.equals("2"))
					{
						transactionTime = "00";
						transactionMinute = "00";
						transactionSecond = "00";
					}
					
					if (null != rs.getString("LADING_NO")){
						ladingNo = rs.getString("LADING_NO");
					}
					
					if (null != rs.getString("CUSTOMS_NO")){
						customsNo = rs.getString("CUSTOMS_NO");
						customsNo = customsNo.toUpperCase();
					}else{
						customsNo = "";
					}
					
					if (null != rs.getString("PASSPORT_NO_SALES")){
						passportNoSales = rs.getString("PASSPORT_NO_SALES");
					}
					
					if (null != rs.getString("FLIGHT_NO")){
						flightNoSales = rs.getString("FLIGHT_NO");
					}
					
					if (null != rs.getString("TAX_TYPE")){
						taxType = rs.getString("TAX_TYPE");
						if(taxType.equals("")){
							taxType = "1";
						}
					}else{
						taxType = "1";
					}

					if (null != rs.getString("TAX_RATE")){
						taxRate = NumberUtils.getDouble(rs.getString("TAX_RATE"));
					}
						
					
					if(null != rs.getString("EVENT_CODE")){
						eventCode = rs.getString("EVENT_CODE");
					}
					
					if(null != rs.getString("APP_CUSTOMER_CODE")){
						appCustomerCode = rs.getString("APP_CUSTOMER_CODE");
					}
					
					salesOrderHead.setSalesOrderDate(salesOrderDate);					
					salesOrderHead.setPosMachineCode(posMachineCode);
					salesOrderHead.setShopCode(shopCode);
					salesOrderHead.setTransactionSeqNo("TD" + posMachineCode + transactionSeqNo_new);
					salesOrderHead.setCustomerCode(customerCode);
					salesOrderHead.setCustomerPoNo(customerPoNo);
					salesOrderHead.setCountryCode(countryCode);				
					salesOrderHead.setNationalityCode(countryCode);
					salesOrderHead.setSuperintendentCode(superintendentCode);
					salesOrderHead.setVipTypeCode(vipTypeCode);
					salesOrderHead.setTransactionTime(transactionTime);
					salesOrderHead.setTransactionMinute(transactionMinute);
					salesOrderHead.setTransactionSecond(transactionSecond);
					salesOrderHead.setPassportNo(passportNo);
					salesOrderHead.setFlightNo(flightNo);
					salesOrderHead.setDepartureDate(departureDate);
					salesOrderHead.setScheduleCollectionDate(salesOrderDate);
					salesOrderHead.setScheduleShipDate(salesOrderDate);
					salesOrderHead.setReserve3(DateUtils.format(salesOrderDate, DateUtils.C_DATA_PATTON_YYYYMMDD) + posMachineCode);
					salesOrderHead.setReserve5("POS");
					salesOrderHead.setCreatedBy(superintendentCode);
					salesOrderHead.setLastUpdatedBy(superintendentCode);
					salesOrderHead.setGuiCode(unionNo);//Maco 統編 2016.08.24
					salesOrderHead.setLadingNo(ladingNo);
					salesOrderHead.setCustomsNo(customsNo);
					salesOrderHead.setPassportNoSales(passportNoSales);
					salesOrderHead.setFlightNoSales(flightNoSales);
					salesOrderHead.setTaxType(taxType);
					salesOrderHead.setTaxRate(taxRate);
					salesOrderHead.setExportExpense(0D);
					salesOrderHead.setExpenseForeignAmount(0D);
					salesOrderHead.setExpenseLocalAmount(0D);
					salesOrderHead.setExportCommissionRate(0D);
					salesOrderHead.setEventCode(eventCode);
					salesOrderHead.setAppCustomerCode(appCustomerCode);
					
					//組出so的item					
					qryMySqlPOSOrderItem(salesOrderHead, posCommandMap, transactionSeqNo);
					//組出so的payment
					qryMySqlPOSOrderPayment(salesOrderHead, posCommandMap, transactionSeqNo);

					entityBeans.add(salesOrderHead);
					responseId = 1L;
					posCommandMap.put("responseId", responseId);					
				}
			}else{
				responseId = -4L;
				posCommandMap.put("responseId", responseId);
				throw new Exception("查無DataId: "+ dataId +" 對應之銷售資料");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
		return entityBeans;
	}
	
	private SnsBonusPointSettingDAO snsBonusPointSettingDAO;
	public void setSnsBonusPointSettingDAO(SnsBonusPointSettingDAO snsBonusPointSettingDAO){
		this.snsBonusPointSettingDAO = snsBonusPointSettingDAO;
	}

	
	/**
	 * 由dataId與交易序號組出SO的ITEM
	 * @throws Exception
	 */
	private void qryMySqlPOSOrderItem(SoSalesOrderHead soSalesOrderHead, Map posCommandMap, String transactionSeqNo) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dataId = (String) posCommandMap.get("DATA_ID");
		Long responseId = (Long)posCommandMap.get("responseId");
		String dataBase = (String)posCommandMap.get("DATA_BASE");

		String posMySqlSoItemSql = " select * from pos.DEM_SalesOrderItem where DATA_ID = ? and TRANSACTION_SNO = ? order by INDEX_SNO ";
		try {
			
			List<SoSalesOrderItem> soSalesOrderItems = new ArrayList(0);
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}			
			stmt = conn.prepareStatement(posMySqlSoItemSql);
			stmt.setString(1, dataId);
			stmt.setString(2, transactionSeqNo);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				//這邊是head的資訊
				
				Double totalOriginalSalesAmount = 0D;
				Double totalActualSalesAmount = 0D;
				String usedIdentification = null;
				Double totalTaxAmount = 0D;
				String orderDiscountType = "A"; //無折扣			
				String combineCode = null;				
				Double totalBonusPointAmount =0D;

				while (rs.next()) {
					//Head的值
					
					if (null != rs.getString("USED_IDENTIFICATION"))
						usedIdentification = rs.getString("USED_IDENTIFICATION");

					if (StringUtils.hasText(usedIdentification)) {
						if (!"C".equals(orderDiscountType) && ("02".equals(usedIdentification) || "03".equals(usedIdentification))) {
							orderDiscountType = "C";
						} else if ("A".equals(orderDiscountType) && "01".equals(usedIdentification)) {
							orderDiscountType = "B";
						}
					}				

					//Line的值

					String itemCode = rs.getString("ITEM_CODE");

					Double originalUnitPrice = 0D;
					Double bonusPointAmount = 0D; //點數轉換
					
					//原價必須再由KWE系統中抓出最貼近的價錢
					originalUnitPrice = imItemPriceService.getUnitPriceByDate(soSalesOrderHead.getBrandCode(), itemCode, soSalesOrderHead.getSalesOrderDate());

					Double quantity = 0D;
					if (null != rs.getString("SALES_QTY"))
						quantity = NumberUtils.getDouble(rs.getString("SALES_QTY"));

					Double originalSalesAmount = 0D;
					
					originalSalesAmount = originalUnitPrice * quantity;

					Double actualSalesAmount = 0D;
					if (null != rs.getString("ACTUAL_SALES_AMOUNT"))
						actualSalesAmount = NumberUtils.getDouble(rs.getString("ACTUAL_SALES_AMOUNT"));

					Double discountRate = getDiscountRate(actualSalesAmount, originalSalesAmount);

					Double actualUnitPrice = 0D;

					if(quantity > 0)
						actualUnitPrice = actualSalesAmount / quantity;

					Double discountAmount = 0D;
					if (null != rs.getString("DISCOUNT_AMT"))
						discountAmount = NumberUtils.getDouble(rs.getString("DISCOUNT_AMT"));

					Double usedDiscountRate = 0D;
					if (null != rs.getString("USED_DISCOUNT_RATE"))
						usedDiscountRate = NumberUtils.getDouble(rs.getString("USED_DISCOUNT_RATE"));

					Double taxAmount = 0D;
					if (null != rs.getString("TAX_AMT"))
						taxAmount = NumberUtils.getDouble(rs.getString("TAX_AMT"));
					totalTaxAmount += taxAmount;

					totalOriginalSalesAmount = totalOriginalSalesAmount + originalSalesAmount;
					totalActualSalesAmount = totalActualSalesAmount + actualSalesAmount; 

					if (null != rs.getString("COMBINE_CODE")){
						combineCode = rs.getString("COMBINE_CODE");
					}else{
						combineCode = "";
					}
					
					

					SoSalesOrderItem salesOrderItem = new SoSalesOrderItem();
					//2014/03/21上傳報單

					String	importDelcNo = rs.getString("O_IMPORT_DECL_NO");
					//long importDelcSeq = (long) 0D;
					long importDelcSeq = NumberUtils.getLong(rs.getString("O_IMPORT_DECL_SNO"));

					ImItem imItemPO = imItemDAO.findImItem(soSalesOrderHead.getBrandCode(), itemCode,"Y");
					if(imItemPO == null){
						responseId = 6L;
						posCommandMap.put("responseId", responseId);
						throw new ValidationErrorException("查無品號(" + rs.getString("ITEM_CODE") + ")的相關資料！");
					}else{
						String isTax = imItemPO.getIsTax();
						if(!StringUtils.hasText(isTax)){
							throw new ValidationErrorException("品號(" + rs.getString("ITEM_CODE") + ")的稅別為空值！");
						}
						salesOrderItem.setIsTax(imItemPO.getIsTax());
						salesOrderItem.setIsServiceItem(imItemPO.getIsServiceItem());
						salesOrderItem.setIsComposeItem(imItemPO.getIsComposeItem());
						salesOrderItem.setAllowMinusStock(imItemPO.getAllowMinusStock()); //是否允許負庫存
					}

					salesOrderItem.setItemCode(itemCode);
					salesOrderItem.setWarehouseCode(soSalesOrderHead.getShopCode());
					salesOrderItem.setOriginalForeignUnitPrice(originalUnitPrice);
					salesOrderItem.setOriginalUnitPrice(originalUnitPrice);
					salesOrderItem.setQuantity(quantity);
					salesOrderItem.setOriginalForeignSalesAmt(originalSalesAmount);
					salesOrderItem.setOriginalSalesAmount(originalSalesAmount);
					salesOrderItem.setActualForeignSalesAmt(actualSalesAmount);
					salesOrderItem.setActualSalesAmount(actualSalesAmount);
					salesOrderItem.setPosActualSalesAmount(actualSalesAmount);// 放入POS檔案裏的實際銷售金額
					salesOrderItem.setActualForeignUnitPrice(actualUnitPrice);
					salesOrderItem.setActualUnitPrice(actualUnitPrice);
					salesOrderItem.setDiscount(discountAmount);
					salesOrderItem.setDiscountRate(discountRate);
					salesOrderItem.setVipPromotionCode(soSalesOrderHead.getVipTypeCode());
					salesOrderItem.setScheduleShipDate(soSalesOrderHead.getSalesOrderDate());
					salesOrderItem.setShippedDate(soSalesOrderHead.getSalesOrderDate());
					salesOrderItem.setTaxType(soSalesOrderHead.getTaxType());
					salesOrderItem.setTaxRate(soSalesOrderHead.getTaxRate());
					salesOrderItem.setTaxAmount(taxAmount);
					salesOrderItem.setUsedIdentification(usedIdentification);
					salesOrderItem.setUsedCardId(rs.getString("USED_CARD_ID"));
					salesOrderItem.setUsedCardType(rs.getString("USED_CARD_TYPE"));
					salesOrderItem.setUsedDiscountRate(usedDiscountRate);
					salesOrderItem.setItemDiscountType(rs.getString("ITEM_DISCOUNT_TYPE"));
					salesOrderItem.setPosSeq(rs.getLong("INDEX_SNO")); // 此交易明細序號
					salesOrderItem.setLotNo(rs.getString("LOT_NO"));
					salesOrderItem.setLastUpdatedBy("SYS");
					salesOrderItem.setLastUpdateDate(new Date());
					salesOrderItem.setCreatedBy("SYS");
					salesOrderItem.setCreationDate(new Date());
					//2014/03/21上傳報單
					salesOrderItem.setImportDeclNo(importDelcNo);
					salesOrderItem.setImportDeclSeq(importDelcSeq);
					//2014/03/26上傳報單日期
					List <CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", importDelcNo);
					if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
						CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
						salesOrderItem.setImportDeclDate(cmDeclarationHead.getImportDate());
						salesOrderItem.setImportDeclType(cmDeclarationHead.getDeclType());
					}
log.info("AppCustomerCode=="+soSalesOrderHead.getAppCustomerCode());
//					累積點數2020/03/31 Justin
					if (StringUtils.hasText(soSalesOrderHead.getAppCustomerCode())){
					log.info("=====累點開始=====");
					String categoryCode = salesOrderItem.getItemDiscountType(); //取出商品類別
					log.info("categoryCode="+categoryCode);
					//利用商品類別找出紅利率
					List<SnsBonusPointSetting> lists = snsBonusPointSettingDAO.getBonusRateByCategoryCode(categoryCode);
					Double bonusRate =null;
					if (lists.size() == 0 ) {						
						log.info("沒有在snsBonusPointSetting設定categoryCode");
						bonusRate = 0.0;
					} else {
						bonusRate = lists.get(0).getBonusRate();
					}
					//取出銷售金額
					Double exchangeableAmount = Math.floor(salesOrderItem.getPosActualSalesAmount());
log.info("exchangeableAmount==" + exchangeableAmount);
					//依照比例算可轉換點數金額
					bonusPointAmount = Math.floor(exchangeableAmount * bonusRate); 
					//累加算出總點數
					totalBonusPointAmount += bonusPointAmount;
log.info("totalBonusPointAmount=="+totalBonusPointAmount);
					//把點數存回去
					salesOrderItem.setBonusPointAmount(bonusPointAmount);  
					
					
					
					log.info("categoryCode:"+categoryCode+"bonusRate:"+bonusRate+"exchangeableAmount:"+exchangeableAmount+"bonusPointAmount:"+bonusPointAmount+"totalBonusPointAmount:"+totalBonusPointAmount);
					}
					
					salesOrderItem.setCombineCode(combineCode);

					soSalesOrderItems.add(salesOrderItem);

				}
				
				soSalesOrderHead.setTotalOriginalSalesAmount(totalOriginalSalesAmount);
				soSalesOrderHead.setOriginalTotalFrnSalesAmt(totalOriginalSalesAmount);
				soSalesOrderHead.setTotalActualSalesAmount(totalActualSalesAmount);
				soSalesOrderHead.setActualTotalFrnSalesAmt(totalActualSalesAmount);								
				soSalesOrderHead.setTaxAmount(totalTaxAmount);	
				soSalesOrderHead.setOrderDiscountType(orderDiscountType);
				soSalesOrderHead.setTotalBonusPointAmount(totalBonusPointAmount); //set回HEAD的總累積點數欄位

			}else{
				responseId = -4L;
				posCommandMap.put("responseId", responseId);
			}
			soSalesOrderHead.setSoSalesOrderItems(soSalesOrderItems);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}

	}
	
	private void qryMySqlPOSOrderPayment(SoSalesOrderHead soSalesOrderHead, Map posCommandMap, String transactionSeqNo) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dataId = (String) posCommandMap.get("DATA_ID");
		Long responseId = (Long)posCommandMap.get("responseId");
		String dataBase = (String)posCommandMap.get("DATA_BASE");
		
		String posMySqlSoItemSql = " select * from pos.DEM_SalesOrderPayment where DATA_ID = ? and TRANSACTION_SNO = ? ";
		try {
			List<SoSalesOrderPayment> soSalesOrderPayments = new ArrayList(0);
			
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}			
			stmt = conn.prepareStatement(posMySqlSoItemSql);
			stmt.setString(1, dataId);
			stmt.setString(2, transactionSeqNo);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				String casherCode = null;
				while (rs.next()) {
					
					//Head的值
					
					if (rs.getString("CASHER_CODE") != null ){
						casherCode = rs.getString("CASHER_CODE");
					}
					
					//Line的值
					Double payAmt = 0D;
					if (rs.getString("LOCAL_AMOUNT") != null) {
						payAmt = NumberUtils.getDouble(rs.getString("LOCAL_AMOUNT"));
					}

					Double payQty = 0D;
					if (rs.getString("PAY_QTY") != null) {
						payQty = NumberUtils.getDouble(rs.getString("PAY_QTY"));
					}

					Double exchangeRate = 0D;
					if (rs.getString("EXCHANGE_RATE") != null) {
						exchangeRate = NumberUtils.getDouble(rs.getString("EXCHANGE_RATE"));
					}

					Double payDue = 0D;
					if (rs.getString("LOCAL_TOTAL_AMOUNT") != null) {
						payDue = NumberUtils.getDouble(rs.getString("LOCAL_TOTAL_AMOUNT"));
					}

					Long indexNo = 0L;
					if (rs.getString("INDEX_SNO") != null) {
						indexNo = NumberUtils.getLong(rs.getString("INDEX_SNO"));
					}
					
					String reCheckId = "";
					if (rs.getString("RE_CHECK_ID") != null) {
						reCheckId = rs.getString("RE_CHECK_ID");
					}
					
					String orderNo = "";
					if (rs.getString("USER_ORDER_NO") != null) {
						orderNo = rs.getString("USER_ORDER_NO");
					}
					
					String userPayNo = "";
					if (rs.getString("USER_PAY_NO") != null) {
						userPayNo = rs.getString("USER_PAY_NO");
					}
					
					SoSalesOrderPayment salesOrderPayment = new SoSalesOrderPayment();
					salesOrderPayment.setPosPaymentType(rs.getString("POS_PAYMENT_TYPE"));
					salesOrderPayment.setLocalCurrencyCode("NTD");
					salesOrderPayment.setLocalAmount(payDue);
					salesOrderPayment.setForeignCurrencyCode(rs.getString("CURRENCY_CODE"));
					salesOrderPayment.setForeignAmount(payAmt);
					salesOrderPayment.setExchangeRate(exchangeRate);
					salesOrderPayment.setPayNo(rs.getString("PAY_NO"));
					salesOrderPayment.setPayQty(payQty);
					salesOrderPayment.setReserve5("POS");
					salesOrderPayment.setIndexNo(indexNo);
					salesOrderPayment.setLastUpdatedBy("SYS");
					salesOrderPayment.setLastUpdateDate(new Date());
					salesOrderPayment.setCreatedBy("SYS");
					salesOrderPayment.setCreationDate(new Date());
					salesOrderPayment.setReCheckId(reCheckId);
					salesOrderPayment.setOrderNo(orderNo);
					salesOrderPayment.setUserPayNo(userPayNo);
					salesOrderPayment.setAppCustomerCode(soSalesOrderHead.getAppCustomerCode());
					soSalesOrderPayments.add(salesOrderPayment);
				}
				soSalesOrderHead.setCasherCode(casherCode);
			}else{
				responseId = -4L;
				posCommandMap.put("responseId", responseId);
			}
			soSalesOrderHead.setSoSalesOrderPayments(soSalesOrderPayments);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("關閉ResultSet時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
}