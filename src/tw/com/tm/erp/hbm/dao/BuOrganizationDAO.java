package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.BuOrganization;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuOrganization entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuOrganization
 * @author MyEclipse Persistence Tools
 */

public class BuOrganizationDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(BuOrganizationDAO.class);
	// property constants
	public static final String ORGANIZATION_CNAME = "organizationCName";
	public static final String ORGANIZATION_ENAME = "organizationEName";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuOrganization transientInstance) {
		log.debug("saving BuOrganization instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuOrganization persistentInstance) {
		log.debug("deleting BuOrganization instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuOrganization findById(java.lang.String id) {
		log.debug("getting BuOrganization instance with id: " + id);
		try {
			BuOrganization instance = (BuOrganization) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuOrganization", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuOrganization instance) {
		log.debug("finding BuOrganization instance by example");
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
		log.debug("finding BuOrganization instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuOrganization as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOrganizationCName(Object organizationCName) {
		return findByProperty(ORGANIZATION_CNAME, organizationCName);
	}

	public List findByOrganizationEName(Object organizationEName) {
		return findByProperty(ORGANIZATION_ENAME, organizationEName);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuOrganization instances");
		try {
			String queryString = "from BuOrganization";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuOrganization merge(BuOrganization detachedInstance) {
		log.debug("merging BuOrganization instance");
		try {
			BuOrganization result = (BuOrganization) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuOrganization instance) {
		log.debug("attaching dirty BuOrganization instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuOrganization instance) {
		log.debug("attaching clean BuOrganization instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuOrganizationDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuOrganizationDAO) ctx.getBean("buOrganizationDAO");
	}
}