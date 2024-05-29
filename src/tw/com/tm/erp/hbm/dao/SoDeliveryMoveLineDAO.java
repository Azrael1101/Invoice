package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoDeliveryMoveLine entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.SoDeliveryMoveLine
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryMoveLineDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SoDeliveryMoveLineDAO.class);

	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImMovementItem>
	 */
	public HashMap findPageLine(Long headId, int startPage, int pageSize, String searchType) {
		log.info("findPageLine..."+headId);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		final Long hId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from SoDeliveryMoveLine as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.headId from SoDeliveryMoveLine as model where 1=1 ");
				} else {
					hql.append("from SoDeliveryMoveLine as model where 1=1 ");
				}
				if (hId != null)
					hql.append(" and model.soDeliveryMoveHead.headId = :headId order by indexNo");
				
				
				//hql.append(" and model.soDeliveryHead.headId = :headId order by indexNo");
				
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (hId != null)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		log.info("SoDeliveryMoveLine.form:" + result.size());
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("SoDeliveryLine.size:" + result.get(0));
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long
							.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	
	public Long getMaxIndexNo(Long headId) {
		log.info("getMaxIndexNo..."+headId);
		final Long searchHeadId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select MAX(model.indexNo) as indexNo from SoDeliveryMoveLine as model where 1=1 ");
				hql.append(" and model.soDeliveryMoveHead.headId     = :searchHeadId");
				Query query = session.createQuery(hql.toString());
				query.setLong("searchHeadId"    , searchHeadId);
				return query.list();
			}
		});
		log.info("getMaxIndexNo.size:" + result.size());
		Long returnResult = new Long(0);
		if (null== result || result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryLog.IndexNo:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return (null == returnResult?0L:returnResult);
	}
	
    public SoDeliveryMoveLine findItemByIdentification(Long headId, Long lineId){
    	StringBuffer hql = new StringBuffer("from SoDeliveryMoveLine as model where model.soDeliveryMoveHead.headId = ? and model.lineId = ?");
    	List<SoDeliveryMoveLine> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
    	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public  List<SoDeliveryMoveLine>  findLineByDeliveryOrderNo(Long headId, String orderTypeCode, String deliveryOrderNo) {
		log.info("findLineByDeliveryheadId..."+headId);
		log.info("findLineByDeliveryorderTypeCode..."+orderTypeCode);
		log.info("findLineByDeliveryorderTypeCode..."+deliveryOrderNo);
		final Long hId = headId;
		final String typeCode = orderTypeCode;
		final String orderNo = deliveryOrderNo;
		List<SoDeliveryMoveLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryMoveLine> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from SoDeliveryMoveLine as model where 1=1 ");
				hql.append(" and model.soDeliveryMoveHead.headId = :headId ");
				hql.append(" and model.deliveryOrderType = :typeCode ");
				hql.append(" and model.deliveryOrderNo = :orderNo ");
				Query query = session.createQuery(hql.toString());
				query.setLong("headId", hId);
				query.setString("typeCode", typeCode);
				query.setString("orderNo", orderNo);
				return query.list();
			}
		});
		log.info("SoDeliveryMoveLine.form:" + result.size());
		return result;
	}
    
    public  List<Object[]>  getBagCounts(Long headId) {
		log.info("getBagCounts..."+headId);
		final Long hId = headId;
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select sum(bagCounts1),sum(bagCounts2),sum(bagCounts3),sum(bagCounts4) ,sum(bagCounts5),sum(bagCounts6),sum(bagCounts7)");
				hql.append("  from SoDeliveryMoveLine model where 1=1 ");
				hql.append("   and model.soDeliveryMoveHead.headId = :headId ");
				System.out.println(hql.toString());
				//Query query = session.createSQLQuery(hql.toString());
				Query query = session.createQuery(hql.toString());
				query.setLong("headId", hId);
				return query.list();
			}
		});
		log.info("SoDeliveryMoveLine.form:" + result.size());
		return result;
	}
    
    
    public void updateIndexNo(Long headId){
    	log.info("updateIndexNo..."+headId);
		final Long hId = headId;
		List<SoDeliveryMoveLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryMoveLine> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from SoDeliveryMoveLine as model where 1=1 ");
				hql.append(" and model.soDeliveryMoveHead.headId = :headId ");
				hql.append(" order by indexNo ");
				Query query = session.createQuery(hql.toString());
				query.setLong("headId", hId);
				List<SoDeliveryMoveLine> result = query.list();
				int indexNo= 0;
				for(SoDeliveryMoveLine line:result){
					indexNo++;
					StringBuffer updateHql = new StringBuffer("");
					updateHql.append("update SoDeliveryMoveLine");
					updateHql.append("   set indexNo = :indexNo");
					updateHql.append(" where 1=1 ");
					updateHql.append("   and lineId = :lineId ");
					System.out.println(updateHql.toString());
					Query updateQuery = session.createQuery(updateHql.toString());
					updateQuery.setLong("indexNo", indexNo);
					updateQuery.setLong("lineId", line.getLineId());
					updateQuery.executeUpdate();
				}
				return result;
			}
		});
		log.info("SoDeliveryMoveLine.form:" + result.size());

    }
}