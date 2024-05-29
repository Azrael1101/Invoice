package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CustomsProcessResponse;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.utils.DateUtils;

public class ImMovementHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImMovementHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public static final String IM_RECEIVE_DEFECT = "DEFECT";
	public static final String IM_RECEIVE_SAMPLE = "SAMPLE";
	public static final String IM_RECEIVE_ACCEPT = "ACCEPT";
	public static final String IM_RECEIVE_SHORT = "SHORT";

	public ImMovementHead findById(Long id) {
		log.debug("getting ImMovementHead instance with id: " + id);
		try {
			ImMovementHead instance = (ImMovementHead) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImMovementHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List getWhNoSq(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select IM_MOVEMENT_SEND_WH_SEQ.nextval from dual");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						return query.list();
					}
				}
		); 
	}
	
	public List getSendRqSq(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select ERP.IM_MOVEMENT_SEND_RQ_SEQ.nextval from dual");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						return query.list();
					}
				}
		); 
	}
	
	public Object findCustomsSended(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select count(HEAD.HEAD_ID) from IM_MOVEMENT_HEAD head where 1=1 and HEAD.TRAN_ALLOW_UPLOAD is not null and HEAD.CREATION_DATE >= TO_DATE('12/01/2014 00:00:00', 'MM/DD/YYYY HH24:MI:SS') and HEAD.ORDER_TYPE_CODE='RSF' ");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						System.out.println("query.list():"+query.list());
						return query.list();
					}
				}
		); 
	}
	
	public Object findCustomsWaitToSend(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select count(HEAD.HEAD_ID) from IM_MOVEMENT_HEAD head where 1=1 and (HEAD.TRAN_ALLOW_UPLOAD is null OR HEAD.TRAN_ALLOW_UPLOAD = '' ) and HEAD.CREATION_DATE >= TO_DATE('12/01/2014 00:00:00', 'MM/DD/YYYY HH24:MI:SS') and HEAD.ORDER_TYPE_CODE='RSF' and HEAD.STATUS not in ( 'SAVE' , 'VOID' ) ");
						//hql.append("select MAX(head.send_Rp_No) from erp.im_movement_head head where head.send_Rp_No is not null");	
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						System.out.println("query.list():"+query.list());
						return query.list();
					}
				}
		); 
	}

	public List<ImMovementHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImMovementHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from ImMovementHead as model where 1=1 ");
						if (StringUtils.hasText((String) fos.get("itemCode")))
							hql.append(" and model.headId IN(SELECT item.imMovementHead FROM ImMovementItem as item WHERE item.itemCode=:itemCode)");

						if (StringUtils.hasText((String) fos.get("brandCode")))
							hql.append(" and model.brandCode = :brandCode ");

						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							hql.append(" and model.orderTypeCode = :orderTypeCode ");

						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							hql.append(" and model.orderNo >= :startOrderNo ");

						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							hql.append(" and model.orderNo <= :endOrderNo ");

						if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
							hql.append(" and model.deliveryWarehouseCode = :deliveryWarehouseCode ");

						if (null != (java.util.Date) fos.get("startDeliveryDate") )
							hql.append(" and model.deliveryDate >= :startDeliveryDate ");

						if (null != (java.util.Date) fos.get("endDeliveryDate") )
							hql.append(" and model.deliveryDate <= :endDeliveryDate ");

						if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
							hql.append(" and model.arrivalWarehouseCode = :arrivalWarehouseCode");

						if (null != (java.util.Date) fos.get("startArrivalDate"))
							hql.append(" and model.arrivalDate >= :startArrivalDate ");

						if (null != (java.util.Date) fos.get("endArrivalDate"))
							hql.append(" and model.arrivalDate <= :endArrivalDate ");

						if (StringUtils.hasText((String) fos.get("originalOrderTypeCode")))
							hql.append(" and model.originalOrderTypeCode = :originalOrderTypeCode ");

						if (StringUtils.hasText((String) fos.get("originalStartOrderNo")))
							hql.append(" and model.originalOrderNo >= :originalStartOrderNo ");

						if (StringUtils.hasText((String) fos.get("originalEndOrderNo")))
							hql.append(" and model.originalOrderNo <= :originalEndOrderNo ");

						if (StringUtils.hasText((String) fos.get("status")))
							hql.append(" and model.status = :status ");

						hql.append(" order by lastUpdateDate desc ");

						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						if (StringUtils.hasText((String) fos.get("itemCode")))
							query.setString("itemCode", (String) fos.get("itemCode"));

						if (StringUtils.hasText("brandCode"))
							query.setParameter("brandCode", fos.get("brandCode"));

						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							query.setString("startOrderNo", (String) fos.get("startOrderNo"));

						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							query.setString("endOrderNo", (String) fos.get("endOrderNo"));

						if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
							query.setString("deliveryWarehouseCode",(String) fos.get("deliveryWarehouseCode"));

						if (null != fos.get("startDeliveryDate"))
							query.setDate("startDeliveryDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startDeliveryDate"))+" 00:00:00") );



						if (null != fos.get("endDeliveryDate"))
							query.setDate("endDeliveryDate", (java.util.Date)fos.get("endDeliveryDate"));

						//DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endDeliveryDate")))
						if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
							query.setString("arrivalWarehouseCode",(String) fos.get("arrivalWarehouseCode"));

						if (null != (java.util.Date)fos.get("startArrivalDate"))
							query.setDate("startArrivalDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startArrivalDate"))+" 00:00:00") );

						if (null != (java.util.Date)fos.get("endArrivalDate"))
							query.setDate("endArrivalDate", (java.util.Date)fos.get("endArrivalDate") );
						//DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endArrivalDate")))

						if (StringUtils.hasText((String) fos.get("originalOrderTypeCode")))
							query.setString("originalOrderTypeCode", (String) fos.get("originalOrderTypeCode"));

						if (StringUtils.hasText((String) fos.get("originalStartOrderNo")))
							query.setString("originalStartOrderNo", (String) fos.get("originalStartOrderNo"));

						if (StringUtils.hasText((String) fos.get("originalEndOrderNo")))
							query.setString("originalEndOrderNo", (String) fos.get("originalEndOrderNo"));


						if (StringUtils.hasText((String) fos.get("status")))
							query.setString("status", (String) fos.get("status"));

						return query.list();
					}
				});

		return re;
	}

	public List<ImMovementHead> findUnlimited(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImMovementHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from ImMovementHead as model where 1=1 ");

						if (StringUtils.hasText((String) fos.get("brandCode")))
							hql.append(" and model.brandCode = :brandCode ");

						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							hql.append(" and model.orderTypeCode = :orderTypeCode ");

						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							hql.append(" and model.orderNo >= :startOrderNo ");

						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							hql.append(" and model.orderNo <= :endOrderNo ");

						if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
							hql.append(" and model.deliveryWarehouseCode = :deliveryWarehouseCode ");

						if (null != (java.util.Date) fos.get("startDeliveryDate") )
							hql.append(" and model.deliveryDate >= :startDeliveryDate ");

						if (null != (java.util.Date) fos.get("endDeliveryDate") )
							hql.append(" and model.deliveryDate <= :endDeliveryDate ");

						if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
							hql.append(" and model.arrivalWarehouseCode = :arrivalWarehouseCode");

						if (null != (java.util.Date) fos.get("startArrivalDate"))
							hql.append(" and model.arrivalDate >= :startArrivalDate ");

						if (null != (java.util.Date) fos.get("endArrivalDate"))
							hql.append(" and model.arrivalDate <= :endArrivalDate ");

						if (StringUtils.hasText((String) fos.get("status")))
							hql.append(" and model.status = :status ");

						hql.append(" order by lastUpdateDate desc ");

						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());

						if (StringUtils.hasText("brandCode"))
							query.setParameter("brandCode", fos.get("brandCode"));

						if (StringUtils.hasText((String) fos.get("orderTypeCode")))
							query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

						if (StringUtils.hasText((String) fos.get("startOrderNo")))
							query.setString("startOrderNo", (String) fos.get("startOrderNo"));

						if (StringUtils.hasText((String) fos.get("endOrderNo")))
							query.setString("endOrderNo", (String) fos.get("endOrderNo"));

						if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
							query.setString("deliveryWarehouseCode",(String) fos.get("deliveryWarehouseCode"));

						if (null != fos.get("startDeliveryDate"))
							query.setDate("startDeliveryDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startDeliveryDate"))+" 00:00:00") );

						if (null != fos.get("endDeliveryDate"))
							query.setDate("endDeliveryDate", DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endDeliveryDate"))) );

						if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
							query.setString("arrivalWarehouseCode",(String) fos.get("arrivalWarehouseCode"));

						if (null != (java.util.Date)fos.get("startArrivalDate"))
							query.setDate("startArrivalDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startArrivalDate"))+" 00:00:00") );

						if (null != (java.util.Date)fos.get("endArrivalDate"))
							query.setDate("endArrivalDate", DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endArrivalDate"))) );

						if (StringUtils.hasText((String) fos.get("status")))
							query.setString("status", (String) fos.get("status"));

						return query.list();
					}
				});

		return re;
	}

	/**
	 * 依據品牌代號、單別、單號查詢調撥單
	 *
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return ImMovementHead
	 */
	public ImMovementHead findMovementByIdentification(String brandCode, String orderTypeCode, String orderNo) {

		StringBuffer hql = new StringBuffer("from ImMovementHead as model where model.brandCode = ?");
		hql.append(" and model.orderTypeCode = ?");
		hql.append(" and model.orderNo = ?");
		List<ImMovementHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, orderTypeCode, orderNo });

		return (result != null && result.size() > 0 ? result.get(0) : null);
	}

	 /**
	  * 依據品牌及狀態查詢調撥單
	  *
	  * @param brandCode
	  * @param status
	  * @return
	  */
	public List<ImMovementHead> findMovementByProperty(String brandCode, String status) {
		StringBuffer hql = new StringBuffer("from ImMovementHead as model where model.brandCode = ?");
		hql.append(" and model.status = ?");
		List<ImMovementHead> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, status });
		return result;
	}
	
	public List<ImMovementHead>findMovementByProperty(final String brandCode, final String status, final Date startDate, final Date endDate) {
		log.info("ImMovementHeadDAO.findMovementByProperty brandCode=" + brandCode+ "/"+DateUtils.format(startDate)+ "/"+DateUtils.format(endDate));
			List<ImMovementHead> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql =  new StringBuffer("from ImMovementHead as model where 1=1");
					hql.append(" and model.brandCode = :brandCode ");
					hql.append(" and model.status = :status ");
					if (null != startDate)
						hql.append(" and model.deliveryDate >= :startDate");
					
					if (null != endDate)
						hql.append(" and model.deliveryDate <= :endDate");
					hql.append(" order by deliveryDate asc");
					Query query = session.createQuery(hql.toString());
					query.setString("brandCode", brandCode);
					query.setString("status", status);
					if (null != startDate)
						query.setDate("startDate", startDate);
					
					if (null != endDate)
						query.setDate("endDate", endDate);
					return query.list();
				}
			});
			return temp;
	}

	/**
	 * 依據品牌代號、POS單別、POS單號查詢調撥單
	 *
	 * @param brandCode
	 * @param originalOrderTypeCode
	 * @param originalOrderNo
	 * @return
	 */
	public ImMovementHead findPOSMovementByIdentification(String brandCode, String originalOrderTypeCode,
			String originalOrderNo) {

		StringBuffer hql = new StringBuffer("from ImMovementHead as model where model.brandCode = ?");
		hql.append(" and model.originalOrderTypeCode = ?");
		hql.append(" and model.originalOrderNo = ?");
		List<ImMovementHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, originalOrderTypeCode, originalOrderNo });

		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	/**
	  * 依據品牌及狀態查詢調撥單
	  *
	  * @param brandCode
	  * @param status
	  * @return
	  */
	public List<ImMovementHead> findMovementList(String brandCode, String originalOrderTypeCode,
			String originalOrderNo) {
		StringBuffer hql = new StringBuffer("from ImMovementHead as model where model.brandCode = ?");
		hql.append(" and model.originalOrderTypeCode = ?");
		hql.append(" and model.originalOrderNo = ?");
		List<ImMovementHead> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, originalOrderTypeCode,originalOrderNo });
		return result;
	}

	public List<ImMovementHead> findPOSMovementByCondition(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		// final String orderTypeCode =
		// (String)conditionMap.get("orderTypeCode");
		final String[] origiOrderTypeArray = (String[]) conditionMap.get("origiOrderTypeArray");
		final Date deliveryDate = (Date) conditionMap.get("deliveryDate");
		final Date deliveryDateEnd = (Date) conditionMap.get("deliveryDateEnd");
		final String status = (String) conditionMap.get("status");

		List<ImMovementHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Criteria ctr = session.createCriteria(ImMovementHead.class);
				ctr.add(Expression.eq("brandCode", brandCode));
				// ctr.add(Expression.eq("orderTypeCode", orderTypeCode));
				ctr.add(Expression.ge("deliveryDate", deliveryDate));
				ctr.add(Expression.le("deliveryDate", deliveryDateEnd));
				ctr.add(Expression.in("originalOrderTypeCode", origiOrderTypeArray));
				ctr.add(Expression.eq("status", status));
				ctr.addOrder(Order.asc("brandCode"));
				ctr.addOrder(Order.asc("orderTypeCode"));
				ctr.addOrder(Order.asc("orderNo"));

				return ctr.list();
			}
		});

		return result;
	}

	/**
	 * 查詢月結日之前還沒有完成關帳的單調撥單
	 *
	 * @param brandCodeArray
	 * @param orderDate
	 * @param statusArray
	 * @return
	 */
	public List<ImMovementHead> findImMovementByCriteria(final String[] brandCodeArray, final Date orderDate,
			final String[] statusArray) {

		final Date oDate = DateUtils.getShortDate(orderDate);
		List<ImMovementHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria ctr = session.createCriteria(ImMovementHead.class);
				ctr.add(Expression.le("deliveryDate", oDate));
				ctr.add(Expression.in("brandCode", brandCodeArray));
				ctr.add(Expression.in("status", statusArray));
				ctr.addOrder(Order.asc("brandCode"));
				ctr.addOrder(Order.asc("orderTypeCode"));
				ctr.addOrder(Order.asc("orderNo"));
				return ctr.list();
			}
		});

		return result;
	}

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
	public HashMap findPageLine(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		System.out.println("start to find imMovementHead....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Date startDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos
						.get("startDeliveryDate"));
				Date endDeliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos
						.get("endDeliveryDate"));
				Date startArrivalDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos
						.get("startArrivalDate"));
				Date endArrivalDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endArrivalDate"));

				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from ImMovementHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from ImMovementHead as model where 1=1 ");
				} else {
					hql.append("from ImMovementHead as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("itemCode")))
					hql
							.append(" and model.headId IN(SELECT item.imMovementHead FROM ImMovementItem as item WHERE item.itemCode=:itemCode)");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");

				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.orderNo >= :startOrderNo ");

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.orderNo <= :endOrderNo ");

				if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
					hql.append(" and model.deliveryWarehouseCode = :deliveryWarehouseCode ");

				if (null != startDeliveryDate)
					hql.append(" and model.deliveryDate >= :startDeliveryDate ");

				if (null != endDeliveryDate)
					hql.append(" and model.deliveryDate <= :endDeliveryDate ");

				if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
					hql.append(" and model.arrivalWarehouseCode = :arrivalWarehouseCode");

				if (null != startArrivalDate)
					hql.append(" and model.arrivalDate >= :startArrivalDate ");

				if (null != endArrivalDate)
					hql.append(" and model.arrivalDate <= :endArrivalDate ");

				if (StringUtils.hasText((String) fos.get("originalOrderTypeCode")))
					hql.append(" and model.originalOrderTypeCode = :originalOrderTypeCode ");

				if (StringUtils.hasText((String) fos.get("originalStartOrderNo")))
					hql.append(" and model.originalOrderNo >= :originalStartOrderNo ");

				if (StringUtils.hasText((String) fos.get("originalEndOrderNo")))
					hql.append(" and model.originalOrderNo <= :originalEndOrderNo ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("packedBy")))
					hql.append(" and model.packedBy = :packedBy ");

				if (StringUtils.hasText((String) fos.get("comfirmedBy")))
					hql.append(" and model.comfirmedBy = :comfirmedBy ");

				if (StringUtils.hasText((String) fos.get("receiptedBy")))
					hql.append(" and model.receiptedBy = :receiptedBy ");

				if (StringUtils.hasText((String) fos.get("cmMovement")))
					hql.append(" and model.cmMovementNo = :cmMovement ");
				
				if(StringUtils.hasText((String) fos.get("customsWarehouseCode"))){
					hql.append(" and model.arrivalWarehouseCode in ");
					hql.append("(select warehouse.warehouseCode from ImWarehouse as warehouse where warehouse.customsWarehouseCode = :customsWarehouseCode)");
				}

				if (StringUtils.hasText((String) fos.get("cmMovementIsNotNull"))
						&& "Y".equalsIgnoreCase((String) fos.get("cmMovementIsNotNull")))
					hql.append(" and model.cmMovementNo is null ");

				// 備註欄位搜尋
				if (StringUtils.hasText((String) fos.get("remark")))
					hql.append(" and (model.remark1 LIKE :remark OR model.remark2 LIKE :remark) ");

				if (StringUtils.hasText((String) fos.get("sortKey")))
					hql.append(" order by " + (String) fos.get("sortKey"));
				else
					hql.append(" order by lastUpdateDate ");

				if (StringUtils.hasText((String) fos.get("sortSeq")))
					hql.append(" " + (String) fos.get("sortSeq"));
				else
					hql.append(" desc ");
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());

				if (QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
					System.out.println("type:" + type + " startFrom:" + startRecordIndexStar + " to " + pSize);
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}

				if (StringUtils.hasText((String) fos.get("itemCode")))
					query.setString("itemCode", (String) fos.get("itemCode"));

				if (StringUtils.hasText("brandCode"))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));

				if (StringUtils.hasText((String) fos.get("deliveryWarehouseCode")))
					query.setString("deliveryWarehouseCode", (String) fos.get("deliveryWarehouseCode"));

				if (null != startDeliveryDate)
					query.setDate("startDeliveryDate", DateUtils.parseDateTime(DateUtils.format(startDeliveryDate)
							+ " 00:00:00"));

				if (null != endDeliveryDate)
					query.setDate("endDeliveryDate", endDeliveryDate);

				if (StringUtils.hasText((String) fos.get("arrivalWarehouseCode")))
					query.setString("arrivalWarehouseCode", (String) fos.get("arrivalWarehouseCode"));

				if (null != startArrivalDate)
					query.setDate("startArrivalDate", DateUtils.parseDateTime(DateUtils.format(startArrivalDate)
							+ " 00:00:00"));

				if (null != endArrivalDate)
					query.setDate("endArrivalDate", endArrivalDate);

				if (StringUtils.hasText((String) fos.get("originalOrderTypeCode")))
					query.setString("originalOrderTypeCode", (String) fos.get("originalOrderTypeCode"));

				if (StringUtils.hasText((String) fos.get("originalStartOrderNo")))
					query.setString("originalStartOrderNo", (String) fos.get("originalStartOrderNo"));

				if (StringUtils.hasText((String) fos.get("originalEndOrderNo")))
					query.setString("originalEndOrderNo", (String) fos.get("originalEndOrderNo"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("packedBy")))
					query.setString("packedBy", (String) fos.get("packedBy"));

				if (StringUtils.hasText((String) fos.get("comfirmedBy")))
					query.setString("comfirmedBy", (String) fos.get("comfirmedBy"));

				if (StringUtils.hasText((String) fos.get("receiptedBy")))
					query.setString("receiptedBy", (String) fos.get("receiptedBy"));

				if (StringUtils.hasText((String) fos.get("cmMovement")))
					query.setString("cmMovement", (String) fos.get("cmMovement"));

				if (StringUtils.hasText((String) fos.get("remark"))) // 備註
					query.setString("remark", "%" + (String) fos.get("remark") + "%");

				if (StringUtils.hasText((String) fos.get("customsWarehouseCode"))) 
					query.setString("customsWarehouseCode", (String) fos.get("customsWarehouseCode"));

				return query.list();
			}
		});

		log.info("imMovementHeads.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("imMovementHeads.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}

	/**
	 * 調撥單明細依品號排序
	 *
	 * @param headId
	 * @return List
	 */
	public List<ImMovementItem> getItemsByOrder(Long headId) {
		log.info("ImMovementHeadDAO.getItemsByOrder headId=" + headId);
		final Long id = headId;
		if (null != id) {
			List<ImMovementItem> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("from ImMovementItem as item where ");
					hql.append(" imMovementHead.headId = :headId ");
					hql.append(" order by item.itemCode ");
					Query query = session.createQuery(hql.toString());
					query.setLong("headId", id);
					return query.list();
				}
			});
			return temp;
		}
		return new ArrayList();
	}
	
	
	/**
	 * 調撥單明細
	 *
	 * @param headId
	 * @return List
	 */
	public List<ImMovementItem> getItems(Long headId) {
		log.info("ImMovementHeadDAO.getItemsByOrder headId=" + headId);
		final Long id = headId;
		if (null != id) {
			List<ImMovementItem> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("from ImMovementItem as item where ");
					hql.append(" imMovementHead.headId = :headId ");
					hql.append(" order by item.boxNo ");
					Query query = session.createQuery(hql.toString());
					query.setLong("headId", id);
					return query.list();
				}
			});
			return temp;
		}
		return new ArrayList();
	}



	public  List checkMoveToReceive(ImMovementHead imMovementHead, final String type){
		log.info("DAO.checkMoveToReceive...");
		final ImMovementHead head = imMovementHead;
		List temp = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {


			StringBuffer hql = new StringBuffer("");
			hql.append("select   message ");
			hql.append("  from   (select   case ");
			hql.append("                      when move.move_item_code is null then ");
			hql.append("                            '商品' || receive.receive_item_code || '於調撥單中不存在' ");
			hql.append("                      when receive.receive_item_code is null then ");
			hql.append("                         '商品' || move.move_item_code || '於進貨單("+head.getOriginalOrderTypeCode()+head.getOriginalOrderNo()+")中不存在' ");
			hql.append("                      when nvl (move.delivery_quantity, 0) <> nvl (receive.receive_quantity, 0) then ");
			hql.append("                            '商品' || move.move_item_code|| '數量('|| nvl (move.delivery_quantity, 0)|| ");
			hql.append("                            ')與進貨單中的數量('|| nvl (receive.receive_quantity, 0)|| ')不符'  ");
			hql.append("                      else  null end as message ");
			hql.append("            from      (  select   l.item_code as move_item_code, ");
			hql.append("                                  sum (l.delivery_quantity) as delivery_quantity ");
			hql.append("                           from   im_movement_head h, im_movement_item l ");
			hql.append("                          where   h.head_id = l.head_id and h.head_id = :headId and l.is_delete_record = 0");
			//hql.append("                          where   h.head_id = l.head_id and h.head_id = 1429729");
			hql.append("                       group by   l.item_code) move  ");
			hql.append("                   full join  ");
			hql.append("                      (  select   l.item_code as receive_item_code, ");
			if(IM_RECEIVE_DEFECT.equalsIgnoreCase(type))
				hql.append("                                  sum (l.defect_quantity) as receive_quantity  ");
			else if(IM_RECEIVE_SAMPLE.equalsIgnoreCase(type))
				hql.append("                                  sum (l.sample_quantity) as receive_quantity  ");
			else if(IM_RECEIVE_ACCEPT.equalsIgnoreCase(type))
				hql.append("                                  sum (l.accept_quantity) as receive_quantity  ");
			else
				hql.append("                                  sum (l.short_quantity) as receive_quantity  ");
			hql.append("                           from   im_receive_head h, im_receive_item l ");
			hql.append("                          where       h.head_id = l.head_id  ");
			hql.append("                                  and h.brand_code = :brandCode ");
			hql.append("                                  and h.order_type_code = :receiveOrderTypeCode ");
			hql.append("                                  and h.order_no = :receiveOrderNo");
			if(IM_RECEIVE_DEFECT.equalsIgnoreCase(type))
				hql.append("                              and l.defect_quantity > 0");
			else if(IM_RECEIVE_SAMPLE.equalsIgnoreCase(type))
				hql.append("                              and l.sample_quantity > 0");
			else if(IM_RECEIVE_ACCEPT.equalsIgnoreCase(type))
				hql.append("                              and l.accept_quantity > 0");
			else
				hql.append("                              and l.short_quantity > 0");
			//hql.append("                                  and h.brand_code = 'T2'");
			//hql.append("                                  and h.order_type_code = 'EIF'");
			//hql.append("                                  and h.order_no = '200909100006'");
			hql.append("                       group by   l.item_code) receive");
			hql.append("                   on move.move_item_code = receive.receive_item_code)");
			hql.append(" where   message is not null ");
			System.out.println("compare receive hql:"+hql.toString());
			SQLQuery query = session.createSQLQuery(hql.toString());
			query.setLong("headId", head.getHeadId());
			query.setString("brandCode", head.getBrandCode());
			query.setString("receiveOrderTypeCode", head.getOriginalOrderTypeCode());
			query.setString("receiveOrderNo", head.getOriginalOrderNo());
			return query.list();
		}
		});
		return temp;
	}

	public List<ImMovementHead> findTransportDataForPOS(HashMap conditionMap) {

		final String brandCode = (String)conditionMap.get("brandCode");
		final String[] origiOrderTypeArray = (String[])conditionMap.get("origiOrderTypeArray");
		final String status = (String)conditionMap.get("status");
		final String transportStatus = (String)conditionMap.get("transportStatus");

		List<ImMovementHead> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				Criteria ctr = session.createCriteria(ImMovementHead.class);
				ctr.add(Expression.eq("brandCode", brandCode));
				ctr.add(Expression.in("originalOrderTypeCode", origiOrderTypeArray));
				ctr.add(Expression.eq("status", status));
				ctr.add(Expression.or(Expression.ne("transport", transportStatus), Expression.isNull("transport")));
				ctr.addOrder(Order.asc("brandCode"));
				ctr.addOrder(Order.asc("orderTypeCode"));
				ctr.addOrder(Order.asc("orderNo"));

				return ctr.list();
			    }
			});

		return result;
	}

	public List<ImMovementHead> findTransportDataById(final Long[] transportIdArray) {

		List<ImMovementHead> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				Criteria ctr = session.createCriteria(ImMovementHead.class);
				ctr.add(Expression.in("headId", transportIdArray));
				ctr.addOrder(Order.asc("headId"));

				return ctr.list();
			    }
			});

		return result;
	}

	/**
	 * 更新PROCESS_ID，避免重複起流程
	 *
	 * @param headId
	 * @param ProcessId
	 * @throws Exception
	 */  
	public int updateProcessId(Long headId, Long ProcessId) throws Exception {
		final String nativeSql = "UPDATE IM_MOVEMENT_HEAD SET PROCESS_ID = " + ProcessId + " WHERE HEAD_ID = " + headId;
		System.out.println("更新PROCESS_ID SQL ::: " + nativeSql);
		int result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(nativeSql);
				int iReturn = query.executeUpdate();
				return iReturn;
			}
		});
		return result;
	}


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
	public HashMap searchPageLine(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		System.out.println("start to find imMovementHead....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Date deliveryDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos
						.get("deliveryDate"));

				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from ImMovementHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from ImMovementHead as model where 1=1 ");
				} else {
					hql.append("from ImMovementHead as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("itemCode")))
					hql.append(" and model.headId IN(SELECT item.imMovementHead FROM ImMovementItem as item WHERE item.itemCode=:itemCode)");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("deliveryDate")))
					hql.append(" and model.deliveryDate in ( :deliveryDate )");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");

				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status in ( :status )");

				if (StringUtils.hasText((String) fos.get("sortKey")))
					hql.append(" order by " + (String) fos.get("sortKey"));
				else
					hql.append(" order by lastUpdateDate ");

				if (StringUtils.hasText((String) fos.get("sortSeq")))
					hql.append(" " + (String) fos.get("sortSeq"));
				else
					hql.append(" desc ");
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());

				if (QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
					System.out.println("type:" + type + " startFrom:" + startRecordIndexStar + " to " + pSize);
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}

				// ===============================================================

				if (StringUtils.hasText("brandCode"))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));;

				if (null != deliveryDate)
					query.setDate("deliveryDate", DateUtils.parseDateTime(DateUtils.format(deliveryDate)
							+ " 00:00:00"));
				return query.list();
			}
		});

		log.info("imMovementHeads.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("imMovementHeads.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}


	/**
	 * 依Brand_Code以及Export_Flag查詢調撥單
	 *
	 * @param findObjs
	 * @return
	 */
	public List<ImMovementHead> findForExport(HashMap findObjs) {

		final HashMap fos = findObjs;
		List<ImMovementHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImMovementHead as model where 1=1 ");
				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				hql.append(" AND (model.exportFlag <> 'Y' OR model.exportFlag IS NULL) ");
				hql.append(" AND (model.status = 'WAIT_IN' OR model.status = 'FINISH') ");
				hql.append(" order by lastUpdateDate desc ");

				//System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText("brandCode"))
					query.setParameter("brandCode", fos.get("brandCode"));

				return query.list();
			}
		});

		return re;
	}
	
		public List findScheduleOrders(String schedule,Date closeDate,String startHours,String endHours) throws Exception{
				
				
		        final String date = DateUtils.format(closeDate);
		        final String argStartHours = startHours;
		        final String argEndHours = endHours;
				System.out.println("im轉型後:"+DateUtils.format(closeDate));
				
				List<CmMovementHead> re = getHibernateTemplate().executeFind(
						new HibernateCallback() {
							public Object doInHibernate(Session session)
									throws HibernateException, SQLException {
		
								StringBuffer hql = new StringBuffer(
										"from ImMovementHead as model where 1=1 ");
								
		
								
									hql.append(" and model.status = 'FINISH' ");
		
								
		                       
									hql.append(" and model.lastUpdateDate between to_date('"+date+" "+argStartHours+"','yyyy-mm-dd HH24:MI:SS') and "+"to_date('"+date+" "+argEndHours+"','yyyy-mm-dd HH24:MI:SS')");
		
								
		                        hql.append(" and model.orderTypeCode = 'RSF' order by lastUpdateDate desc ");
		
								System.out.println("時間比較:"+hql.toString());
								Query query = session.createQuery(hql.toString());
								query.setFirstResult(0);
								//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
		
								
								return query.list();
							}
						});
		
				return re;
				
				
			}
}