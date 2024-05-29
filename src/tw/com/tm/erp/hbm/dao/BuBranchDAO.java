package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.BuBranch;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuBranch entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuBranch
 * @author MyEclipse Persistence Tools
 */

public class BuBranchDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(BuBranchDAO.class);
	// property constants
	public static final String ORGANIZATION_CODE = "organizationCode";
	public static final String BRANCH_NAME = "branchName";
	public static final String DESCRIPTION = "description";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuBranch transientInstance) {
		log.debug("saving BuBranch instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuBranch persistentInstance) {
		log.debug("deleting BuBranch instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuBranch findById(java.lang.String id) {
		log.debug("getting BuBranch instance with id: " + id);
		try {
			BuBranch instance = (BuBranch) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuBranch", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuBranch instance) {
		log.debug("finding BuBranch instance by example");
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
		log.debug("finding BuBranch instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuBranch as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOrganizationCode(Object organizationCode) {
		return findByProperty(ORGANIZATION_CODE, organizationCode);
	}

	public List findByBranchName(Object branchName) {
		return findByProperty(BRANCH_NAME, branchName);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
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
		log.debug("finding all BuBranch instances");
		try {
			String queryString = "from BuBranch";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuBranch merge(BuBranch detachedInstance) {
		log.debug("merging BuBranch instance");
		try {
			BuBranch result = (BuBranch) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuBranch instance) {
		log.debug("attaching dirty BuBranch instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuBranch instance) {
		log.debug("attaching clean BuBranch instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuBranchDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuBranchDAO) ctx.getBean("buBranchDAO");
	}
}