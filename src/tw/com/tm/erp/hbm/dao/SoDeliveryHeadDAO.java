package tw.com.tm.erp.hbm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.utils.DateUtils;


public class SoDeliveryHeadDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(SoDeliveryHeadDAO.class);
    public static final String QUARY_TYPE_GET_ALL        = "getAll";
	public static final String QUARY_TYPE_SELECT_ALL     = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE   = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT   = "recordCount";
	public static final String SP_TYPE_CUSTOMER_PO_NO    = "CustomerPoNo";
	public static final String SP_TYPE_SALES_ORDER_ID    = "SalesOrderId";
	public static final String SP_TYPE_SALES_ORDER_DATE  = "SalesOrderDate";
	public static final String SP_TYPE_LADING_NO         = "LadingNo";
	public static final String SP_TYPE_DELETE_LINE_DATA  = "DeleteLineData";
    private DataSource dataSource;
    /**
	 * 
	 * @param findObjs
	 * @param startPage
	 * @param pageSize
	 * @param searchType
	 *            1) get max record count 2) select data records according to
	 *            startPage and pageSize 3) select all records
	 * @return
	 */
	public HashMap findByMap(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		System.out.println("start to find soDeliveryHead....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Date startDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startDeliveryDate"));
				Date endDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endDeliveryDate"));
				Date startScheduleDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startScheduleDeliveryDate"));
				Date endScheduleDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endScheduleDeliveryDate"));		
				Date startOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startOrderDate"));
				Date endOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endOrderDate"));		
				Date startFlightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startFlightDate"));
				Date endFlightDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endFlightDate"));	
				Date startExpiryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startExpiryDate"));
				Date endExpiryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endExpiryDate"));
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from SoDeliveryHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from SoDeliveryHead as model where 1=1 ");
				} else {
					hql.append("from SoDeliveryHead as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("customerPoNo")))
					hql.append(" and model.headId IN(SELECT item.soDeliveryHead FROM SoDeliveryLine as item WHERE item.customerPoNo like '"+(String) fos.get("customerPoNo")+"%')");
				
				if (StringUtils.hasText((String) fos.get("transactionSeqNo"))){
					hql.append(" and model.headId = (");
					hql.append("SELECT item.soDeliveryHead FROM SoDeliveryLine as item , SoSalesOrderHead as so WHERE item.salesOrderId = so.headId ");
					hql.append("AND so.ladingNo is not null ");
					hql.append("AND so.brandCode = 'T2' ");
					hql.append("AND so.orderTypeCode = 'SOP' ");
					hql.append("AND so.transactionSeqNo = :transactionSeqNo)");
				}
					
				if (StringUtils.hasText((String) fos.get("terminal"))){
					hql.append(" and model.flightNo IN(SELECT item.flightNo FROM BuFlightSchedule as item " +
							"WHERE item.terminal=:terminal");
					if (null != startFlightDate)
						hql.append(" and item.flightDate >= :startFlightDate ");
					if (null != endFlightDate)
						hql.append(" and item.flightDate <= :endFlightDate ");		
					hql.append(")");
				}
				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
                                
				String cwCode =(String) fos.get("cwCode");
				if(fos.get("storeArea").equals("W")||fos.get("storeArea").equals("X")||fos.get("storeArea").equals("Z"))cwCode = "HD";//by jason
				if (StringUtils.hasText((String) fos.get("orderNo"))){
				   if(null!=cwCode&&cwCode.equals("HD")){
				       hql.append(" and model.orderNo like 'DKP%' and model.orderNo = :orderNo ");
				   }else if(null!=cwCode&&cwCode.equals("VD")){
				       hql.append(" and model.orderNo like 'DMP%' and model.orderNo = :orderNo ");
				   }else if(null==cwCode){
				       hql.append(" and model.orderNo like 'DZN%' and model.orderNo = :orderNo ");
				   }
				}
				
				if(null!=cwCode&&cwCode.equals("HD")){
				       hql.append(" and model.orderNo like 'DKP%' ");
				}else if(null!=cwCode&&cwCode.equals("VD")){
				       hql.append(" and model.orderNo like 'DMP%' ");
				 }else if(null==cwCode){
				       hql.append(" and model.orderNo like 'DZN%' ");
				 }
				
				if (StringUtils.hasText((String) fos.get("shopCode")))
					hql.append(" and model.shopCode = :shopCode ");

				if (null != startDeliveryDate)
					hql.append(" and model.deliveryDate >= :startDeliveryDate ");
				if (null != endDeliveryDate)
					hql.append(" and model.deliveryDate <= :endDeliveryDate ");

				if (null != startScheduleDeliveryDate)
					hql.append(" and model.scheduleDeliveryDate >= :startScheduleDeliveryDate ");
				if (null != endScheduleDeliveryDate)
					hql.append(" and model.scheduleDeliveryDate <= :endScheduleDeliveryDate ");
				
				if (null != startFlightDate)
					hql.append(" and model.flightDate >= :startFlightDate ");
				if (null != endFlightDate)
					hql.append(" and model.flightDate <= :endFlightDate ");		
				
				if (null != startExpiryDate)
					hql.append(" and model.expiryDate >= :startExpiryDate ");
				if (null != endExpiryDate)
					hql.append(" and model.expiryDate <= :endExpiryDate ");		
				
				if (null != startOrderDate)
					hql.append(" and model.orderDate >= :startOrderDate ");
				
				if (null != endOrderDate)
					hql.append(" and model.orderDate <= :endOrderDate ");		
				
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");
				
				if (StringUtils.hasText((String) fos.get("soDeliveryType")))//為入提盤點用含待建檔(異常儲位)、待銷退(提貨倉)、待提領
					hql.append(" and (model.status = 'W_DELIVERY' or (model.status = 'W_CREATE' and storageCode = 'XXXXX') or (model.status = 'W_RETURN' and model.storeArea = 'A'))");

				if (StringUtils.hasText((String) fos.get("customerName")))
					hql.append(" and model.customerName = :customerName ");

				if (StringUtils.hasText((String) fos.get("contactInfo")))
					hql.append(" and model.contactInfo = :contactInfo ");

				if (StringUtils.hasText((String) fos.get("passportNo")))
					hql.append(" and model.passportNo = :passportNo ");

				if (StringUtils.hasText((String) fos.get("flightNo")))
					hql.append(" and model.flightNo = :flightNo ");
				
				if (StringUtils.hasText((String) fos.get("flightArea")))
					hql.append(" and model.flightArea = :flightArea ");	

				if (StringUtils.hasText((String) fos.get("storeArea")))
					hql.append(" and model.storeArea = :storeArea ");	
				
				if (StringUtils.hasText((String) fos.get("lockFlag")))
					hql.append(" and model.lockFlag = :lockFlag ");	
				
				if (StringUtils.hasText((String) fos.get("valuable")))
					hql.append(" and model.valuable = :valuable ");		
				
				if (StringUtils.hasText((String) fos.get("breakable")))
					hql.append(" and model.breakable = :breakable ");	
				
				if (StringUtils.hasText((String) fos.get("SFaultReason")))
					hql.append(" and model.SFaultReason = :SFaultReason ");	
				
				if (StringUtils.hasText((String) fos.get("DFaultReason")))
					hql.append(" and model.DFaultReason = :DFaultReason ");	
				
				if (StringUtils.hasText((String) fos.get("expiryReturnNo")))
					hql.append(" and model.expiryReturnNo = :expiryReturnNo ");	
				
				if (StringUtils.hasText((String) fos.get("affidavit")))
					hql.append(" and model.affidavit = :affidavit ");	
				
				if (StringUtils.hasText((String) fos.get("storageCodeStart")))
					hql.append(" and model.storageCode >= :storageCodeStart ");
				if (StringUtils.hasText((String) fos.get("storageCodeEnd")))
					hql.append(" and model.storageCode <= :storageCodeEnd ");
				
				if (StringUtils.hasText((String) fos.get("storageCode")))
					hql.append(" and model.storageCode >= :storageCode ");
				if (StringUtils.hasText((String) fos.get("storageCode1")))
					hql.append(" and model.storageCode <= :storageCode1 ");
				
				if (StringUtils.hasText((String) fos.get("soDelUpdateHeadCode")))
					hql.append(" and model.soDelUpdateHeadCode = :soDelUpdateHeadCode ");
				
				if (StringUtils.hasText((String) fos.get("range")))
					hql.append(" and SUBSTR(model.storageCode,2,1) = :range ");
				
				if (StringUtils.hasText((String) fos.get("sortKey")))
					hql.append(" order by " + (String) fos.get("sortKey"));
				else
					hql.append(" order by lastUpdateDate ");

				if (StringUtils.hasText((String) fos.get("sortSeq")))
					hql.append(" " + (String) fos.get("sortSeq"));
				else
					hql.append(" desc ");
				log.info("============hql================"+hql.toString());
				Query query = session.createQuery(hql.toString());
				
				if (QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
					System.out.println("type:" + type + " startFrom:" + startRecordIndexStar + " to " + pSize);
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				
				if (StringUtils.hasText("brandCode"))
					{query.setParameter("brandCode", fos.get("brandCode"));}
				
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));
				
				if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));
				
				if (StringUtils.hasText((String) fos.get("shopCode")))
					query.setString("shopCode", (String) fos.get("shopCode"));
				
				if (null != startDeliveryDate)
					query.setDate("startDeliveryDate", DateUtils.parseDateTime(DateUtils.format(startDeliveryDate)+ " 00:00:00"));
				if (null != endDeliveryDate)
					query.setDate("endDeliveryDate", endDeliveryDate);
				
				if (null != startFlightDate)
					query.setDate("startFlightDate", DateUtils.parseDateTime(DateUtils.format(startFlightDate)+ " 00:00:00"));
				if (null != endFlightDate)
					query.setDate("endFlightDate", endFlightDate);
				
				if (null != startScheduleDeliveryDate)
					query.setDate("startScheduleDeliveryDate", DateUtils.parseDateTime(DateUtils.format(startScheduleDeliveryDate)
							+ " 00:00:00"));
				if (null != endScheduleDeliveryDate)
					query.setDate("endScheduleDeliveryDate", endScheduleDeliveryDate);	
				
				if (null != startExpiryDate)
					query.setDate("startExpiryDate", DateUtils.parseDateTime(DateUtils.format(startExpiryDate)
							+ " 00:00:00"));
				if (null != endExpiryDate)
					query.setDate("endExpiryDate", endExpiryDate);
				
				if (null != startOrderDate)
					query.setDate("startOrderDate", DateUtils.parseDateTime(DateUtils.format(startOrderDate)+ " 00:00:00"));
				
				if (null != endOrderDate)
					query.setDate("endOrderDate", endOrderDate);
				
			//	if (StringUtils.hasText((String) fos.get("customerPoNo")))
			//		query.setString("customerPoNo", (String) fos.get("customerPoNo"));

				if (StringUtils.hasText((String) fos.get("customerName")))
					query.setString("customerName", (String) fos.get("customerName"));
					log.info("excel output customer:"+(String) fos.get("customerName"));
				if (StringUtils.hasText((String) fos.get("contactInfo")))
					query.setString("contactInfo", (String) fos.get("contactInfo"));
				if (StringUtils.hasText((String) fos.get("passportNo")))
					query.setString("passportNo", (String) fos.get("passportNo"));
				
				if (StringUtils.hasText((String) fos.get("storeArea")))
					query.setString("storeArea", (String) fos.get("storeArea"));
				
				if (StringUtils.hasText((String) fos.get("flightNo")))
					query.setString("flightNo", (String) fos.get("flightNo"));
	
				if (StringUtils.hasText((String) fos.get("flightArea")))
					query.setString("flightArea", (String) fos.get("flightArea"));
				
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
				
				if (StringUtils.hasText((String) fos.get("lockFlag")))
					query.setString("lockFlag", (String) fos.get("lockFlag"));
				
				if (StringUtils.hasText((String) fos.get("valuable")))
					query.setString("valuable", (String) fos.get("valuable"));
				
				if (StringUtils.hasText((String) fos.get("breakable")))
					query.setString("breakable", (String) fos.get("breakable"));
				
				if (StringUtils.hasText((String) fos.get("SFaultReason")))
					query.setString("SFaultReason", (String) fos.get("SFaultReason"));
				if (StringUtils.hasText((String) fos.get("DFaultReason")))
					query.setString("DFaultReason", (String) fos.get("DFaultReason"));
				
				if (StringUtils.hasText((String) fos.get("expiryReturnNo")))
					query.setString("expiryReturnNo", (String) fos.get("expiryReturnNo"));		
				
				if (StringUtils.hasText((String) fos.get("terminal")))
					query.setString("terminal", (String) fos.get("terminal"));
				
				if (StringUtils.hasText((String) fos.get("affidavit")))
					query.setString("affidavit", (String) fos.get("affidavit"));
				
				if (StringUtils.hasText((String) fos.get("storageCodeStart")))
					query.setString("storageCodeStart",(String) fos.get("storageCodeStart"));
				
				if (StringUtils.hasText((String) fos.get("storageCodeEnd")))
					query.setString("storageCodeEnd",(String) fos.get("storageCodeEnd"));
				
				if (StringUtils.hasText((String) fos.get("storageCode")))
					query.setString("storageCode",(String) fos.get("storageCode"));
				
				if (StringUtils.hasText((String) fos.get("storageCode1")))
					query.setString("storageCode1",(String) fos.get("storageCode1"));
				
				if (StringUtils.hasText((String) fos.get("soDelUpdateHeadCode")))
					query.setString("soDelUpdateHeadCode",(String) fos.get("soDelUpdateHeadCode"));
				
				if (StringUtils.hasText((String) fos.get("range")))
					query.setString("range",(String) fos.get("range"));
				
				if (StringUtils.hasText((String) fos.get("transactionSeqNo")))
					query.setString("transactionSeqNo",(String) fos.get("transactionSeqNo"));

				/*
				if (StringUtils.hasText((String) fos.get("sortKey")))
					query.setString("sortKey", (String) fos.get("sortKey"));
				
				if (StringUtils.hasText((String) fos.get("sortSeq")))
					query.setString("sortSeq", (String) fos.get("sortSeq"));*/
				log.info("query: "+query);
				return query.list();
			}
		});

		log.info("soDeliveryHeads.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_GET_ALL.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("soDeliveryHeads.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_GET_ALL.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public List<SoDeliveryHead> findByOrderNo(String brandCode, String orderTypeCode, String orderNo, String storeArea,String status) {
		log.info("findByOrderNo11..."+brandCode+"/"+orderTypeCode+"/"+orderNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchOrderNo = orderNo;
		final String searchStatus = status;
		final String searchStoreArea= storeArea;
		
		List<SoDeliveryHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryHead> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from SoDeliveryHead as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				hql.append(" and model.orderNo       = :searchOrderNo");
				if(StringUtils.hasText(searchStoreArea))
					hql.append(" and model.storeArea       = :searchStoreArea");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchOrderNo"      , searchOrderNo);
				if(StringUtils.hasText(searchStoreArea))
					query.setString("searchStoreArea"      , searchStoreArea);	
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);	
				return query.list();
			}
		});

		return result;
	}
	
	
	public Long findHeadIdByOrderNo(String brandCode, String orderTypeCode, String orderNo, String status) {
		log.info("findHeadIdByOrderNo..."+brandCode+"/"+orderTypeCode+"/"+orderNo);
		if(orderTypeCode.equals("DKP")||orderTypeCode.equals("DMP")){
			orderTypeCode = "DZN";
		}
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchOrderNo = orderNo;
		final String searchStatus = status;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select model.headId from SoDeliveryHead as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				hql.append(" and model.orderNo       = :searchOrderNo");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchOrderNo"      , searchOrderNo);
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);	
				return query.list();
			}
		});
		log.info("SoDeliveryHead.size:" + result.size());
		Long returnResult = new Long(0);
		if (result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryHead.HeadId:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return returnResult;
	}
	
	public Long findHeadIdByCustomerPoNo(String brandCode, String orderTypeCode, String customerPoNo, String status) {
		log.info("findHeadIdByCustomerPoNo..."+customerPoNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchCustomerPoNo = customerPoNo;
		final String searchStatus = status;
		log.info("1111111111111111:"+searchBrandCode+" "+searchOrderTypeCode+" "+searchCustomerPoNo+" "+searchStatus);
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select model.headId from SoDeliveryHead as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" and model.headId in(");
				hql.append(" select line.soDeliveryHead from SoDeliveryLine as line where 1= 1");
				hql.append("    and line.customerPoNo= :searchCustomerPoNo");
				hql.append(" )");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchCustomerPoNo", searchCustomerPoNo);
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);
				log.info("hql:"+hql);
				return query.list();
			}
		});
		log.info("SoDeliveryHead.size:" + result.size());
		Long returnResult = new Long(0);
		if (result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryHead.HeadId:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return returnResult;
	}
	
	public Long findHeadIdByTransactionSeqNo(String brandCode, String orderTypeCode, String transactionSeqNo, String status) {
		log.info("findHeadIdByCustomerPoNo..."+transactionSeqNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchTransactionSeqNo = transactionSeqNo;
		final String searchStatus = status;
		log.info("findHeadIdByCustomerPoNo..."+searchBrandCode+" "+searchOrderTypeCode+" "+searchTransactionSeqNo+" "+searchStatus);
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select model.headId from SoDeliveryHead as model , SoDeliveryLine as line, SoSalesOrderHead as so where 1=1 ");
				hql.append(" and model.headId = line.soDeliveryHead.headId");
				hql.append(" and line.salesOrderId = so.headId");
				hql.append(" and so.ladingNo is not null ");
				hql.append(" and so.brandCode = 'T2' ");
				hql.append(" and so.orderTypeCode = 'SOP' ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" and so.transactionSeqNo = :searchTransactionSeqNo");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchTransactionSeqNo", searchTransactionSeqNo);
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);
				log.info("hql:"+hql);
				return query.list();
			}
		});
		log.info("SoDeliveryHead.size:" + result.size());
		Long returnResult = new Long(0);
		if (result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryHead.HeadId:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return returnResult;
	}
	
    public void updateStatusByHeadId(final Long headId, final String status){
    	if (headId != null && StringUtils.hasText(status)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("UPDATE SoDeliveryHead");
		    			hql.append(" SET status = :status");
		    			hql.append(", storageCode = '00000'");
		    			hql.append(" WHERE headId = :headId");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("status", status);
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
    
    public void updateByHeadId(final Long headId, HashMap updateMap){
    
    	final HashMap fos = updateMap;
    	if (headId != null && StringUtils.hasText((String)fos.get("status"))){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		    		Date deliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("deliveryDate"));
    		    		String status = (String) fos.get("status");
    		    		String employeeCode = (String) fos.get("employeeCode");
    		    		String storeArea = (String) fos.get("storeArea");
		    			StringBuffer hql = new StringBuffer("UPDATE SoDeliveryHead");
		    			hql.append(" SET status = :status");
		    			hql.append(", lastUpdatedBy = :lastUpdatedBy");
		    			hql.append(", lastUpdateDate = :today");
		    			if(OrderStatus.CLOSE.equalsIgnoreCase(status) || OrderStatus.W_DELIVERY.equalsIgnoreCase(status)  || OrderStatus.RETURN.equalsIgnoreCase(status) || OrderStatus.REFUND.equalsIgnoreCase(status)){
		    				hql.append(", deliveryDate = :deliveryDate");
		    				hql.append(", deliveredBy = :delivedBy");
		    			}
		    			
		    			if(StringUtils.hasText((String)fos.get("storeArea"))){
		    				hql.append(", storeArea = :storeArea");
		    			}
		    			
		    			if(OrderStatus.CLOSE.equalsIgnoreCase(status) || OrderStatus.RETURN.equalsIgnoreCase(status))
		    				hql.append(", storageCode = '' ");
		    			
		    			if(OrderStatus.W_CREATE.equalsIgnoreCase(status))
		    				hql.append(", storageCode = '00000' ");
		    			
		    			if(OrderStatus.VOID.equalsIgnoreCase(status))
		    				hql.append(", storageCode = null ");
		    			
		    			hql.append(" WHERE headId = :headId");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("status"       , status);
		    			query.setString("lastUpdatedBy", employeeCode);
		    			query.setTimestamp("today"     , DateUtils.getCurrentDate());
		    			if(OrderStatus.CLOSE.equalsIgnoreCase(status) || OrderStatus.W_DELIVERY.equalsIgnoreCase(status)  || OrderStatus.RETURN.equalsIgnoreCase(status) || OrderStatus.REFUND.equalsIgnoreCase(status)){
		    				query.setDate("deliveryDate",  deliveryDate);
		    				query.setString("delivedBy" ,  employeeCode);
		    			}
		    			
		    			if(StringUtils.hasText((String)fos.get("storeArea"))){
		    				query.setString("storeArea"   ,  storeArea);
		    			}
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
    
    public void updateStoreAreaByHeadId(final Long headId, final String storeArea){
    	if (headId != null && StringUtils.hasText(storeArea)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("UPDATE SoDeliveryHead");
		    			hql.append(" SET storeArea = :storeArea");
		    			hql.append(" WHERE headId = :headId");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("storeArea", storeArea);
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}	
    }
    
    public void updateStoreAreaByHeadId(final Long headId, final String storeArea,final String storageCode){
    	if (headId != null && StringUtils.hasText(storeArea)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("UPDATE SoDeliveryHead");
		    			hql.append(" SET storeArea = :storeArea");
		    			hql.append(" , storageCode = :storageCode");
		    			hql.append(" WHERE headId = :headId");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("storeArea", storeArea);
		    			query.setString("storageCode", storageCode);
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
    
    public void updateLockFlagByHeadId(final Long headId, final String status){
    	if (headId != null && StringUtils.hasText(status)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("UPDATE SoDeliveryHead");
		    			hql.append(" SET lockFlag = :status");
		    			hql.append(" WHERE headId = :headId");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("status", status);
		    			query.setLong("headId", headId);
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }


    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public String updateDeliveryData(String spType, Map updateMap) throws Exception {
    log.info("updateDelieryData..."+spType);
    CallableStatement calStmt = null;
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
	  
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false);
	    if( SP_TYPE_CUSTOMER_PO_NO.equals(spType)){
	    	log.info("spType=CusotmerPoNo "+ (String) updateMap.get("customerPoNo")+"/"+(String) updateMap.get("deliveryNo"));	
		    calStmt = conn.prepareCall("{?= call ERP.APP_SO_DELIVERY_TRANS.TRANS_BY_CUSTOMER_PO_NO(?,?,?,?)}");
		    calStmt.setString(2, (String) updateMap.get("customerPoNo"));
		    calStmt.setString(3, (String) updateMap.get("deliveryNo"));
		    calStmt.setString(4, (String) updateMap.get("employeeCode"));
		    calStmt.setString(5, (String) updateMap.get("logType"));
	    }else if(SP_TYPE_SALES_ORDER_ID.equals(spType)){
	    	log.info("spType=SalesOrderId");	
		    calStmt = conn.prepareCall("{?= call ERP.APP_SO_DELIVERY_TRANS.TRANS_BY_HEAD_ID(?,?,?)}");
		    calStmt.setLong  (2, (Long) updateMap.get("salesOrderId"));
		    calStmt.setString(3, (String) updateMap.get("employeeCode"));
		    calStmt.setString(4,  (String) updateMap.get("logType"));	
	    }else if(SP_TYPE_SALES_ORDER_DATE.equals(spType)){
	    	log.info("spType=SalesOrderDate");	
		    calStmt = conn.prepareCall("{?= call ERP.APP_SO_DELIVERY_TRANS.TRANS_BY_SALES_ORDER_DATE(?,?,?)}");
		    calStmt.setString(2, (String) updateMap.get("SalesOrderDate"));
		    calStmt.setString(3, (String) updateMap.get("employeeCode"));
		    calStmt.setString(4, (String) updateMap.get("logType"));
	    }else if(SP_TYPE_LADING_NO.equals(spType)){
	    	log.info("spType=LadingNo");	
		    calStmt = conn.prepareCall("{?= call ERP.APP_SO_DELIVERY_TRANS.TRANS_BY_LADING_NO(?,?,?)}");
		    calStmt.setString(2, (String) updateMap.get("deliveryNo"));
		    calStmt.setString(3, (String) updateMap.get("employeeCode"));
		    calStmt.setString(4, (String) updateMap.get("logType"));
	    }else if(SP_TYPE_DELETE_LINE_DATA.equals(spType)){
	       	log.info("spType=DeleteLineData");	
		    calStmt = conn.prepareCall("{?= call ERP.APP_SO_DELIVERY_TRANS.DELETE_DELIVERY_LINE(?,?,?,?)}");
		    calStmt.setString(2, (String) updateMap.get("deliveryNo"));
		    calStmt.setString(3, (String) updateMap.get("customerPoNo"));
		    calStmt.setString(4, (String) updateMap.get("employeeCode"));
		    calStmt.setString(5, (String) updateMap.get("logType"));    
	    }
	    log.info("execute sp....");	
	    calStmt.registerOutParameter(1,Types.VARCHAR);
	    
	    calStmt.executeQuery();
	    String result=calStmt.getString(1);
	    log.info(result);
	    log.info("finish sp....");	
	    conn.commit();
	    return result;
	   // rs = (ResultSet) calStmt.getObject(1);
	} catch(Exception ex){
	    if (conn != null && !conn.isClosed()) {
		conn.rollback();
	    }
	    throw new Exception(ex.getMessage());
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    log.error("關閉ResultSet時發生錯誤！");
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
	public List<SoDeliveryHead> findByCustomerPoNo(String brandCode, String orderTypeCode, String customerPoNo, String status) {
		log.info("findHeadIdByCustomerPoNo..."+customerPoNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchCustomerPoNo = customerPoNo;
		final String searchStatus = status;
		
		List<SoDeliveryHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryHead> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from SoDeliveryHead as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" and model.headId in(");
				hql.append(" select line.soDeliveryHead from SoDeliveryLine as line where 1= 1");
				hql.append("    and line.customerPoNo= :searchCustomerPoNo");
				hql.append(" )");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchCustomerPoNo", searchCustomerPoNo);
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);	
				return query.list();
			}
		});
		log.info("SoDeliveryHead.size:" + result.size());
	
		return result;
	}
	
	public List<Object[]> findViewByMap(HashMap findObjs) {
		log.info("findViewByMap...");
		final HashMap fos = findObjs;
		String ifHd = fos.get("ifHd").toString();
		log.info("是否為高雄明細匯出:"+ifHd);
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<BuFlightSchedule> doInHibernate(Session session) throws HibernateException, SQLException {
				Date startScheduleDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startScheduleDeliveryDate"));
				Date endScheduleDeliveryDate   = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endScheduleDeliveryDate"));
				Date startDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startDeliveryDate"));
				Date endDeliveryDate   = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endDeliveryDate"));
				Date startOrderDate    = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startOrderDate"));
				Date endOrderDate      = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endOrderDate"));		
				Date startFlightDate   = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startFlightDate"));
				Date endFlightDate     = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endFlightDate"));	
				Date startExpiryDate   = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startExpiryDate"));
				Date endExpiryDate     = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endExpiryDate"));
				String beanName     = (String) fos.get("isSuperVisor");
				
				StringBuffer hql = new StringBuffer("");
				if ("ExcelExport".equalsIgnoreCase((String) fos.get("QueryType"))){
				  if(beanName.equals("SO_DELIVERY")){	
					  
					hql.append("SELECT model.orderNo,model.flightDate,model.flightNo,model.shopCode,model.deliveryDate,model.customerName" +
									" ,model.passportNo,model.contactInfo,model.orderDate,model.storeArea,model.storageCode,model.totalBagCounts,model.status" +
									" ,model.headId,model.SFaultReason,model.DFaultReason" +
									" ,model.affidavit,model.remark1,model.remark2,model.contactInfo_cs,model.expiryDate,model.soDelUpdateHeadCode,model.oriDate,model.oriFlight"+
									",model.updateContent,model.bagBarCode,model.cs_Note,model.contactOverTime"/*line.superintendentCode"*/ +
								" from SoDeliveryHead as model" +
									/* "  SoDeliveryLine as line"  + */
									/*"  inner join SoDeliveryLine as line" + */
									/* " ,BuEmployeeWithAddressView as emp" + */
							   	/*	" ,bu_shop as shop, bu_shop_machine as m, im_item_category as item" +	*/
							   	" where 1=1"
								/* " and model.headId IN(SELECT item.soDeliveryHead.headId FROM SoDeliveryLine item WHERE item.customerPoNo=:customerPoNo)" */
							   	/*" and model.headId = line.soDeliveryHead"*/
							   	/*" and line.superintendentCode = emp.employeeCode"*/
							   	);
				  }else{
					  
					  hql.append("SELECT model.orderNo,model.flightDate,model.flightNo,model.shopCode,model.deliveryDate,model.customerName" +
								" ,model.orderDate,model.storeArea,model.storageCode,model.totalBagCounts,model.status" +
								" ,model.headId,model.SFaultReason,model.DFaultReason" +
								" ,model.affidavit,model.remark1,model.remark2,model.contactInfo_cs,model.expiryDate,model.soDelUpdateHeadCode,model.oriDate,model.oriFlight" /*line.superintendentCode"*/ +
								",model.updateContent,model.bagBarCode,model.cs_Note,model.contactOverTime"/*line.superintendentCode"*/ +
								" from SoDeliveryHead as model" +
								/* "  SoDeliveryLine as line"  + */
								/*"  inner join SoDeliveryLine as line" + */
								/* " ,BuEmployeeWithAddressView as emp" + */
						   	/*	" ,bu_shop as shop, bu_shop_machine as m, im_item_category as item" +	*/
						   	" where 1=1"
							/* " and model.headId IN(SELECT item.soDeliveryHead.headId FROM SoDeliveryLine item WHERE item.customerPoNo=:customerPoNo)" */
						   	/*" and model.headId = line.soDeliveryHead"*/
						   	/*" and line.superintendentCode = emp.employeeCode"*/
						   	);  
				  }
					
				}else{
					hql.append("SELECT orderNo,flightDate,flightNo,deliveryDate,customerName,passportNo,orderDate,storeArea,storageCode,totalBagCounts,status,headId,SFaultReason,DFaultReason");
					hql.append(" from SoDeliveryHead model where 1=1 ");
				}
				
				System.out.println("ccc");
				if (StringUtils.hasText((String) fos.get("customerPoNo"))){
					hql.append(" and model.headId IN(SELECT item.soDeliveryHead.headId FROM SoDeliveryLine item WHERE item.customerPoNo=:customerPoNo)");
				}
				if (StringUtils.hasText((String) fos.get("terminal"))){
					hql.append(" and model.flightNo IN(SELECT item.flightNo FROM BuFlightSchedule item " +
							"WHERE item.terminal=:terminal");
					if (null != startFlightDate)
						hql.append(" and item.flightDate >= :startFlightDate ");
					if (null != endFlightDate)
						hql.append(" and item.flightDate <= :endFlightDate ");		
					hql.append(")");
				}
				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
				    hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");
				
				if (StringUtils.hasText((String) fos.get("shopCode")))
					hql.append(" and model.shopCode = :shopCode ");

				if (null != startDeliveryDate)
					hql.append(" and model.deliveryDate >= :startDeliveryDate ");
				if (null != endDeliveryDate)
					hql.append(" and model.deliveryDate <= :endDeliveryDate ");

				if (null != startScheduleDeliveryDate)
					hql.append(" and model.scheduleDeliveryDate >= :startScheduleDeliveryDate ");
				if (null != endScheduleDeliveryDate)
					hql.append(" and model.scheduleDeliveryDate <= :endScheduleDeliveryDate ");
				
				if (null != startFlightDate)
					hql.append(" and model.flightDate >= :startFlightDate ");
				if (null != endFlightDate)
					hql.append(" and model.flightDate <= :endFlightDate ");		
				
				if (null != startOrderDate)
					hql.append(" and model.orderDate >= :startOrderDate ");
				
				if (null != endOrderDate)
					hql.append(" and model.orderDate <= :endOrderDate ");		
			
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("customerName")))
					hql.append(" and model.customerName = :customerName ");

				if (StringUtils.hasText((String) fos.get("contactInfo")))
					hql.append(" and model.contactInfo = :contactInfo ");

				if (StringUtils.hasText((String) fos.get("passportNo")))
					hql.append(" and model.passportNo = :passportNo ");

				if (StringUtils.hasText((String) fos.get("flightNo")))
					hql.append(" and model.flightNo = :flightNo ");
				
				if (StringUtils.hasText((String) fos.get("storeArea")))
					hql.append(" and model.storeArea = :storeArea ");	
				
				if (StringUtils.hasText((String) fos.get("lockFlag")))
					hql.append(" and model.lockFlag = :lockFlag ");	
				
				if (StringUtils.hasText((String) fos.get("valuable")))
					hql.append(" and model.valuable = :valuable ");		
				
				if (StringUtils.hasText((String) fos.get("breakable")))
					hql.append(" and model.breakable = :breakable ");	
				
				if (StringUtils.hasText((String) fos.get("SFaultReason")))
					hql.append(" and model.SFaultReason = :SFaultReason ");	
				
				if (StringUtils.hasText((String) fos.get("DFaultReason")))
					hql.append(" and model.DFaultReason = :DFaultReason ");	
				
				if (null != startExpiryDate)
					hql.append(" and model.expiryDate >= :startExpiryDate ");
				
				if (null != endExpiryDate)
					hql.append(" and model.expiryDate <= :endExpiryDate ");		
				
				if (StringUtils.hasText((String) fos.get("affidavit")))
					hql.append(" and model.affidavit = :affidavit ");	
				
				if (StringUtils.hasText((String) fos.get("expiryReturnNo")))
					hql.append(" and model.expiryReturnNo = :expiryReturnNo ");	
				
				//if (StringUtils.hasText((String) fos.get("storageCode")))
					//hql.append(" and model.storageCode = :storageCode ");
				
				if (StringUtils.hasText((String) fos.get("storageCode")))
					hql.append(" and model.storageCode >= :storageCode ");
				if (StringUtils.hasText((String) fos.get("storageCode1")))
					hql.append(" and model.storageCode <= :storageCode1 ");
				
				if (fos.get("ifHd").equals("Y")){
					hql.append(" and model.orderNo like 'DKP%' ");
				}else if(fos.get("ifHd").equals("N")){
				        hql.append(" and model.orderNo like 'DZN%' ");
				}
				
				if (StringUtils.hasText((String) fos.get("soDelUpdateHeadCode")))
					hql.append(" and model.soDelUpdateHeadCode = :soDelUpdateHeadCode ");
				
				if (StringUtils.hasText((String) fos.get("sortKey")))
					hql.append(" order by model." + (String) fos.get("sortKey"));
				else
					hql.append(" order by model.lastUpdateDate ");

				if (StringUtils.hasText((String) fos.get("sortSeq")))
					hql.append(" " + (String) fos.get("sortSeq"));
				else
					hql.append(" desc ");
				System.out.println("---soDeliveryHeadDAO.findViewByMap.QueryString:---" + hql.toString());
				Query query = session.createQuery(hql.toString());
				
				/* test for mac
				System.out.println("test for Mac");
				Query all = session.createQuery("from java.lang.Object");
				
				for (ListIterator iter = all.list().listIterator(); iter.hasNext();){
					System.out.println(iter.next());
				}				
				for (Iterator iter = all.iterate(); iter.hasNext();){
					System.out.println(iter.next());
				}
				*/

				
				if (StringUtils.hasText("brandCode"))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));
				
				if (StringUtils.hasText((String) fos.get("shopCode")))
					query.setString("shopCode", (String) fos.get("shopCode"));

//				if (null != startDeliveryDate)
//					query.setDate("startDeliveryDate", DateUtils.parseDateTime(DateUtils.format(startDeliveryDate)+ " 00:00:00"));

				if (null != startDeliveryDate)
					query.setDate("startDeliveryDate", startDeliveryDate);
				if (null != endDeliveryDate)
					query.setDate("endDeliveryDate", endDeliveryDate);
				
				if (null != startFlightDate)
					query.setDate("startFlightDate", DateUtils.parseDateTime(DateUtils.format(startFlightDate)+ " 00:00:00"));
				if (null != endFlightDate)
					query.setDate("endFlightDate", endFlightDate);
				
				if (null != startScheduleDeliveryDate)
					query.setDate("startScheduleDeliveryDate", DateUtils.parseDateTime(DateUtils.format(startScheduleDeliveryDate)
							+ " 00:00:00"));
				if (null != endScheduleDeliveryDate)
					query.setDate("endScheduleDeliveryDate", endScheduleDeliveryDate);
				
				if (null != startOrderDate)
					query.setDate("startOrderDate", DateUtils.parseDateTime(DateUtils.format(startOrderDate)+ " 00:00:00"));
				
				if (null != endOrderDate)
					query.setDate("endOrderDate", endOrderDate);				
				
				if (StringUtils.hasText((String) fos.get("customerPoNo")))
					query.setString("customerPoNo", (String) fos.get("customerPoNo"));

				if (StringUtils.hasText((String) fos.get("customerName")))
					query.setString("customerName", (String) fos.get("customerName"));
					/* mac test */
					log.info("excel output customer:"+(String) fos.get("customerName"));
				
				if (StringUtils.hasText((String) fos.get("contactInfo")))
					query.setString("contactInfo", (String) fos.get("contactInfo"));
				if (StringUtils.hasText((String) fos.get("passportNo")))
					query.setString("passportNo", (String) fos.get("passportNo"));
				
				if (StringUtils.hasText((String) fos.get("storeArea")))
					query.setString("storeArea", (String) fos.get("storeArea"));
				
				if (StringUtils.hasText((String) fos.get("flightNo")))
					query.setString("flightNo", (String) fos.get("flightNo"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
				
				if (StringUtils.hasText((String) fos.get("lockFlag")))
					query.setString("lockFlag", (String) fos.get("lockFlag"));
				
				if (StringUtils.hasText((String) fos.get("valuable")))
					query.setString("valuable", (String) fos.get("valuable"));
				
				if (StringUtils.hasText((String) fos.get("breakable")))
					query.setString("breakable", (String) fos.get("breakable"));
				
				if (StringUtils.hasText((String) fos.get("SFaultReason")))
					query.setString("SFaultReason", (String) fos.get("SFaultReason"));
				if (StringUtils.hasText((String) fos.get("DFaultReason")))
					query.setString("DFaultReason", (String) fos.get("DFaultReason"));
				
				if (StringUtils.hasText((String) fos.get("terminal")))
					query.setString("terminal", (String) fos.get("terminal"));			
				
				if (null != startExpiryDate)
					query.setDate("startExpiryDate", DateUtils.parseDateTime(DateUtils.format(startExpiryDate)+ " 00:00:00"));
				if (null != endExpiryDate)
					query.setDate("endExpiryDate", endExpiryDate);
				
				if (StringUtils.hasText((String) fos.get("affidavit")))
					query.setString("affidavit", (String) fos.get("affidavit"));
				if (StringUtils.hasText((String) fos.get("expiryReturnNo")))
					query.setString("expiryReturnNo", (String) fos.get("expiryReturnNo"));
				
				if (StringUtils.hasText((String) fos.get("storageCode")))
					query.setString("storageCode", (String) fos.get("storageCode"));
				
				if (StringUtils.hasText((String) fos.get("storageCode1")))
					query.setString("storageCode1", (String) fos.get("storageCode1"));
				
				if (StringUtils.hasText((String) fos.get("soDelUpdateHeadCode")))
					query.setString("soDelUpdateHeadCode", (String) fos.get("soDelUpdateHeadCode"));
				
				log.info("--- mac --- end of query"); 
				
				return query.list();
			}
		});

		return result;
	}
	

		public List findScheduleOrders(String schedule,Date closeDate,String startHours,String endHours) throws Exception{
			
			
	        final String date = DateUtils.format(closeDate);
	        final String argStartHours = startHours;
	        final String argEndHours = endHours;
			System.out.println("sod轉型後:"+DateUtils.format(closeDate));
			
			List<CmMovementHead> re = getHibernateTemplate().executeFind(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
	
							StringBuffer hql = new StringBuffer(
									"from SoDeliveryHead as model where 1=1 ");
							
	
							
								hql.append(" and model.status = 'CLOSE' ");
	
							
	                       
								hql.append(" and model.lastUpdateDate between to_date('"+date+" "+argStartHours+"','yyyy-mm-dd HH24:MI:SS') and "+"to_date('"+date+" "+argEndHours+"','yyyy-mm-dd HH24:MI:SS')");
	
							
	                        hql.append(" and model.orderTypeCode in ('DZN','DKP') order by lastUpdateDate desc ");
	
							System.out.println("時間比較:"+hql.toString());
							Query query = session.createQuery(hql.toString());
							query.setFirstResult(0);
							//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
	
							
							return query.list();
						}
					});
	
			return re;
			
			
		}

	public SoDeliveryHead findSalesOrderByIdentification(String brandCode, String orderTypeCode, String orderNo) {
		
		StringBuffer hql = new StringBuffer("from SoDeliveryHead as model where model.brandCode = ?");
	        hql.append(" and model.orderTypeCode = ?");
	        hql.append(" and model.orderNo = ?");
	        List<SoDeliveryHead> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { brandCode, orderTypeCode,  orderNo});
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	

}