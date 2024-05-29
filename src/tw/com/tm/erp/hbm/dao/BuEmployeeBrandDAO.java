package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuEmployeeBrand entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can bOe augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuEmployeeBrand
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeBrandDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuEmployeeBrandDAO.class);
	// property constants
	public static final String ATTRIBUTE1 = "attribute1";
	public static final String ATTRIBUTE2 = "attribute2";
	public static final String ATTRIBUTE3 = "attribute3";
	public static final String ATTRIBUTE4 = "attribute4";
	public static final String ATTRIBUTE5 = "attribute5";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BYE = "lastUpdatedBye";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(BuEmployeeBrand transientInstance) {
		log.debug("saving BuEmployeeBrand instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuEmployeeBrand persistentInstance) {
		log.debug("deleting BuEmployeeBrand instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuEmployeeBrand findById(tw.com.tm.erp.hbm.bean.BuEmployeeBrandId id) {
		log.debug("getting BuEmployeeBrand instance with id: " + id);
		try {
			BuEmployeeBrand instance = (BuEmployeeBrand) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuEmployeeBrand", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuEmployeeBrand instance) {
		log.debug("finding BuEmployeeBrand instance by example");
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
		log.debug("finding BuEmployeeBrand instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuEmployeeBrand as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByAttribute1(Object attribute1) {
		return findByProperty(ATTRIBUTE1, attribute1);
	}

	public List findByAttribute2(Object attribute2) {
		return findByProperty(ATTRIBUTE2, attribute2);
	}

	public List findByAttribute3(Object attribute3) {
		return findByProperty(ATTRIBUTE3, attribute3);
	}

	public List findByAttribute4(Object attribute4) {
		return findByProperty(ATTRIBUTE4, attribute4);
	}

	public List findByAttribute5(Object attribute5) {
		return findByProperty(ATTRIBUTE5, attribute5);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBye(Object lastUpdatedBye) {
		return findByProperty(LAST_UPDATED_BYE, lastUpdatedBye);
	}

	public List findByIndexNo(Object indexNo) {
		return findByProperty(INDEX_NO, indexNo);
	}

	public List findAll() {
		log.debug("finding all BuEmployeeBrand instances");
		try {
			String queryString = "from BuEmployeeBrand";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuEmployeeBrand merge(BuEmployeeBrand detachedInstance) {
		log.debug("merging BuEmployeeBrand instance");
		try {
			BuEmployeeBrand result = (BuEmployeeBrand) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuEmployeeBrand instance) {
		log.debug("attaching dirty BuEmployeeBrand instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuEmployeeBrand instance) {
		log.debug("attaching clean BuEmployeeBrand instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuEmployeeBrandDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuEmployeeBrandDAO) ctx.getBean("buEmployeeBrandDAO");
	}
}