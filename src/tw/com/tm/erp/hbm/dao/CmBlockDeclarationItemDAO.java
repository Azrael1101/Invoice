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

import tw.com.tm.erp.hbm.bean.CmBlockDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmBlockDeclarationItem;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * CmBlockDeclarationItem entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.dao.CmBlockDeclarationItem
 * @author MyEclipse Persistence Tools
 */

public class CmBlockDeclarationItemDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(CmBlockDeclarationItemDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(CmBlockDeclarationItem transientInstance) {
		log.debug("saving CmBlockDeclarationItem instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CmBlockDeclarationItem persistentInstance) {
		log.debug("deleting CmBlockDeclarationItem instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CmBlockDeclarationItem findById(Long lineId) {
		log.debug("getting CmBlockDeclarationItem instance with id: " + lineId);
		try {
			CmBlockDeclarationItem instance = (CmBlockDeclarationItem) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.dao.CmBlockDeclarationItem", lineId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CmBlockDeclarationItem instance) {
		log.debug("finding CmBlockDeclarationItem instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding CmBlockDeclarationItem instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from CmBlockDeclarationItem as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all CmBlockDeclarationItem instances");
		try {
			String queryString = "from CmBlockDeclarationItem";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CmBlockDeclarationItem merge(CmBlockDeclarationItem detachedInstance) {
		log.debug("merging CmBlockDeclarationItem instance");
		try {
			CmBlockDeclarationItem result = (CmBlockDeclarationItem) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CmBlockDeclarationItem instance) {
		log.debug("attaching dirty CmBlockDeclarationItem instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CmBlockDeclarationItem instance) {
		log.debug("attaching clean CmBlockDeclarationItem instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CmBlockDeclarationItemDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CmBlockDeclarationItemDAO) ctx.getBean("CmBlockDeclarationItemDAO");
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
		List<CmBlockDeclarationHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from CmBlockDeclarationHead as model where 1=1 ");
				if (hId != null)
					hql.append(" and model.headId = :headId");
				Query query = session.createQuery(hql.toString());
				if (hId != null)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			List<CmBlockDeclarationItem> cmBlockDeclarationItems = result.get(0).getCmBlockDeclarationItems();
			if (cmBlockDeclarationItems != null && cmBlockDeclarationItems.size() > 0) {
				lineMaxIndex = cmBlockDeclarationItems.get(cmBlockDeclarationItems.size() - 1).getIndexNo();
			}
		}
		return lineMaxIndex;
	}

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<CmBlockDeclarationItem>
	 */
	public List<CmBlockDeclarationItem> findPageLine(Long headId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<CmBlockDeclarationItem> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from CmBlockDeclarationItem as model where 1=1 ");
				if (hId != null)
					hql.append(" and model.cmBlockDeclarationHead.headId = :headId order by indexNo");
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

}