package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImPromotionItem;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPromotionItem entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPromotionItem
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionItemDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImPromotionItemDAO.class);
	// property constants
	public static final String ITEM_CODE = "itemCode";
	public static final String SALES_UNIT = "salesUnit";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String DISCOUNT_TYPE = "discountType";
	public static final String DISCOUNT = "discount";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPromotionItem transientInstance) {
		log.debug("saving ImPromotionItem instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void update(ImPromotionItem transientInstance) {
	        getHibernateTemplate().update(transientInstance);
	}
	
	public void delete(ImPromotionItem persistentInstance) {
		log.debug("deleting ImPromotionItem instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImPromotionItem findById(java.lang.Long id) {
		log.debug("getting ImPromotionItem instance with id: " + id);
		try {
			ImPromotionItem instance = (ImPromotionItem) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImPromotionItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImPromotionItem instance) {
		log.debug("finding ImPromotionItem instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImPromotionItem instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImPromotionItem as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByItemCode(Object itemCode) {
		return findByProperty(ITEM_CODE, itemCode);
	}

	public List findBySalesUnit(Object salesUnit) {
		return findByProperty(SALES_UNIT, salesUnit);
	}

	public List findByOriginalPrice(Object originalPrice) {
		return findByProperty(ORIGINAL_PRICE, originalPrice);
	}

	public List findByDiscountType(Object discountType) {
		return findByProperty(DISCOUNT_TYPE, discountType);
	}

	public List findByDiscount(Object discount) {
		return findByProperty(DISCOUNT, discount);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findByIndexNo(Object indexNo) {
		return findByProperty(INDEX_NO, indexNo);
	}

	public List findAll() {
		log.debug("finding all ImPromotionItem instances");
		try {
			String queryString = "from ImPromotionItem";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImPromotionItem merge(ImPromotionItem detachedInstance) {
		log.debug("merging ImPromotionItem instance");
		try {
			ImPromotionItem result = (ImPromotionItem) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImPromotionItem instance) {
		log.debug("attaching dirty ImPromotionItem instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImPromotionItem instance) {
		log.debug("attaching clean ImPromotionItem instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImPromotionItemDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImPromotionItemDAO) ctx.getBean("imPromotionItemDAO");
	}
	
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImPromotionItem>
	 */
	 public List<ImPromotionItem> findPageLine(Long headId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImPromotionItem> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImPromotionItem as model where 1=1 ");
				if (hId != null)
				    hql.append(" and model.imPromotion.headId = :headId order by indexNo");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (hId != null)
				    query.setLong("headId", hId);
				return query.list();
			    }
			});
		return result;
	}
	 
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long headId) {

		Long lineMaxIndex = new Long(0);
		final Long hId = headId;
		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select count(model.imPromotion) as rowCount from ImPromotionItem as model where 1=1");
				if (hId != null)
				    hql.append(" and model.imPromotion.headId = :headId");
				Query query = session.createQuery(hql.toString());
				if (hId != null)
				    query.setLong("headId", hId);
				return query.list();
			    }
			});
		if (result != null && result.size() > 0) {
		    Long rowCount = (Long)result.get(0);
		    if(rowCount != null)
			lineMaxIndex = rowCount.longValue();
		}
		return lineMaxIndex;
	}
	
	public ImPromotionItem findItemByIdentification(Long headId, Long lineId){
		
		StringBuffer hql = new StringBuffer("from ImPromotionItem as model where model.imPromotion.headId = ? and model.lineId = ?");
		List<ImPromotionItem> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}

	public ImPromotionItem findByLineId(Long lineId) {
		StringBuffer hql = new StringBuffer("from ImPromotionItem as model where model.lineId = ?");
		List<ImPromotionItem> result = getHibernateTemplate().find(hql.toString(), new Object[]{lineId});
		return (result != null && result.size() > 0 ? result.get(0) : null); 
	}
}