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

import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPriceList entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPriceList
 * @author MyEclipse Persistence Tools
 */

public class ImPriceListDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImPriceListDAO.class);
	// property constants
	public static final String ITEM_CODE = "itemCode";
	public static final String PRICE_ID = "priceID";
	public static final String TYPE_CODE = "typeCode";
	public static final String CURRENCY_CODE = "currencyCode";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String IS_TAX = "isTax";
	public static final String TAX_CODE = "taxCode";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPriceList transientInstance) {
		log.debug("saving ImPriceList instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public boolean update(ImPriceList transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			//throw re;
			result = false;
		}
		return result;
	}
	
	public void delete(ImPriceList persistentInstance) {
		log.debug("deleting ImPriceList instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImPriceList findById(java.lang.Long id) {
		log.debug("getting ImPriceList instance with id: " + id);
		try {
			ImPriceList instance = (ImPriceList) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImPriceList", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImPriceList instance) {
		log.debug("finding ImPriceList instance by example");
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
		log.debug("finding ImPriceList instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ImPriceList as model where model."
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
	
	public List findByPriceId(Object priceId) {
		return findByProperty(PRICE_ID, priceId);
	}
	
	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}

	public List findByCurrencyCode(Object currencyCode) {
		return findByProperty(CURRENCY_CODE, currencyCode);
	}

	public List findByUnitPrice(Object unitPrice) {
		return findByProperty(UNIT_PRICE, unitPrice);
	}

	public List findByIsTax(Object isTax) {
		return findByProperty(IS_TAX, isTax);
	}

	public List findByTaxCode(Object taxCode) {
		return findByProperty(TAX_CODE, taxCode);
	}

	public List findByOriginalPrice(Object originalPrice) {
		return findByProperty(ORIGINAL_PRICE, originalPrice);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all ImPriceList instances");
		try {
			String queryString = "from ImPriceList";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImPriceList merge(ImPriceList detachedInstance) {
		log.debug("merging ImPriceList instance");
		try {
			ImPriceList result = (ImPriceList) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImPriceList instance) {
		log.debug("attaching dirty ImPriceList instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImPriceList instance) {
		log.debug("attaching clean ImPriceList instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImPriceListDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImPriceListDAO) ctx.getBean("imPriceListDAO");
	}
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<ImPriceList> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("ImPriceListDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImPriceList> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImPriceList as imPrice where 1=1 ");
				if (null != hId)
					hql.append(" and imPrice.imPriceAdjustment.headId = :headId order by indexNo");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (null != hId)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		return re;
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */	
	public ImPriceList findLine(Long headId,Long lineId) {
		log.info("ImPriceListDAO.findLine headId=" + headId + "lineId=" + lineId );
		final Long hId = headId;
		final Long lId = lineId;
		List<ImPriceList> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImPriceList as imPrice where 1=1 ");
				if (null != hId)
					hql.append(" and imPrice.imPriceAdjustment.headId = :headId and imPrice.lineId = :lineId ");
				Query query = session.createQuery(hql.toString());
				if (null != hId)
					query.setLong("headId", hId);
				if (null != lId)
					query.setLong("lineId", lId);				
				return query.list();
			}
		});
		if(( null != re ) && ( re.size() > 0 )){
			return re.get(0);
			
		}
		return null;
	}
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
	    log.info("ImPriceListDAO.findPageLineMaxIndex" + headId);		
	    final Long hId = headId;
	    List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    StringBuffer hql = new StringBuffer("select count(imPriceAdjustment.headId) from ImPriceList where 1=1 ");
		    if (null != hId)
			hql.append(" and imPriceAdjustment.headId = :headId ");
		    Query query = session.createQuery(hql.toString());
		    if (null != hId)
			query.setLong("headId", hId);
		    return query.list();
		}
	    });
	    if( null != result && result.size() > 0 ){
		return (Long)result.get(0);	
	    }
	    return 0L;
	}
}