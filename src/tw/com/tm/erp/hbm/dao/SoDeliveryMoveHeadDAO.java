package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoDeliveryMoveHead entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryMoveHeadDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(SoDeliveryMoveHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL     = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE   = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT   = "recordCount";

	
	public SoDeliveryMoveHead findById(java.lang.Long id) {
		log.debug("getting SoDeliveryMoveHead instance with id: " + id);
		try {
			SoDeliveryMoveHead instance = (SoDeliveryMoveHead) getSession()
					.get("tw.com.tm.erp.hbm.bean.SoDeliveryMoveHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public SoDeliveryMoveHead findById(java.lang.String orderNo) {
		log.info("orderNo: " + orderNo);
		StringBuffer hql = new StringBuffer(
		"from SoDeliveryMoveHead as model where model.orderNo = ?");
		
		Object[] objArray = new Object[] {orderNo};

		List<SoDeliveryMoveHead> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public SoDeliveryMoveHead findById(java.lang.String orderNo, java.lang.String orderType) {
		log.info("orderNo: " + orderNo +"===== orderType"+ orderType);
		StringBuffer hql = new StringBuffer(
		"from SoDeliveryMoveHead as model where model.orderNo = ? and model.orderTypeCode = ?");
		
		Object[] objArray = new Object[] {orderNo, orderType};

		List<SoDeliveryMoveHead> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public HashMap findByMap(HashMap findObjs, int startPage, int pageSize, String searchType) {
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		System.out.println("start to find soDeliveryMoveHead....");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    Date startOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("startOrderDate"));
				Date endOrderDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("endOrderDate"));
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from SoDeliveryMoveHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from SoDeliveryMoveHead as model where 1=1 ");
				} else {
					hql.append("from SoDeliveryMoveHead as model where 1=1 ");
				}

				if (StringUtils.hasText((String) fos.get("deliveryOrderNo")))
					hql.append(" and model.headId IN(SELECT item.soDeliveryMoveHead FROM SoDeliveryMoveLine as item WHERE item.deliveryOrderNo=:deliveryOrderNo)");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
				//hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.orderNo >= :startOrderNo ");
				
				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.orderNo <= :endOrderNo ");
				
				if (null != startOrderDate)
					hql.append(" and model.orderDate >= :startOrderDate ");
				
				if (null != endOrderDate)
					hql.append(" and model.orderDate <= :endOrderDate ");		

				if (StringUtils.hasText((String) fos.get("deliveryStoreArea")))
					hql.append(" and model.deliveryStoreArea = :deliveryStoreArea ");	

				if (StringUtils.hasText((String) fos.get("arrivalStoreArea")))
					hql.append(" and model.arrivalStoreArea = :arrivalStoreArea ");	
				
				if (StringUtils.hasText((String) fos.get("moveEmployee")))
					hql.append(" and model.moveEmployee = :moveEmployee ");
				
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");	
			
				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

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

				if (StringUtils.hasText("brandCode"))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));
				
				if (StringUtils.hasText((String) fos.get("deliveryOrderNo")))
					query.setString("deliveryOrderNo", (String) fos.get("deliveryOrderNo"));

				if (StringUtils.hasText((String) fos.get("moveEmployee")))
					query.setString("moveEmployee", (String) fos.get("moveEmployee"));

				if (null != startOrderDate)
					query.setDate("startOrderDate", DateUtils.parseDateTime(DateUtils.format(startOrderDate)+ " 00:00:00"));
				
				if (null != endOrderDate)
					query.setDate("endOrderDate", endOrderDate);		
				
				
				if (StringUtils.hasText((String) fos.get("moveEmployee")))
					query.setString("moveEmployee", (String) fos.get("moveEmployee"));

				if (StringUtils.hasText((String) fos.get("deliveryStoreArea")))
					query.setString("deliveryStoreArea", (String) fos.get("deliveryStoreArea"));

				if (StringUtils.hasText((String) fos.get("arrivalStoreArea")))
					query.setString("arrivalStoreArea", (String) fos.get("arrivalStoreArea"));			
				
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));
			
				
				return query.list();
			}
		});

		log.info("soDeliveryHeads.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("soDeliveryHeads.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}

	/**
	 * 更新PROCESS_ID，避免重複起流程
	 *
	 * @param headId
	 * @param ProcessId
	 * @throws Exception
	 */
	public int updateProcessId(Long headId, Long ProcessId) throws Exception {
		final String nativeSql = "UPDATE SO_DELIVERY_MOVE_HEAD SET PROCESS_ID = " + ProcessId + " WHERE HEAD_ID = " + headId;
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
	
}