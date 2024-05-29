package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.BuAuthruleParamater;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuAuthruleParamater entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuAuthruleParamater
 * @author MyEclipse Persistence Tools
 */

public class BuAuthruleParamaterDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(BuAuthruleParamaterDAO.class);

	// property constants
	public static final String AUTH_ID = "authId";

	public static final String PARTY_ID = "partyId";

	public static final String PARAMETER_TYPE_ID = "parameterTypeId";

	public static final String PARAMETER_NO = "parameterNo";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	public static final String VALUE = "value";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuAuthruleParamater transientInstance) {
		log.debug("saving BuAuthruleParamater instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuAuthruleParamater persistentInstance) {
		log.debug("deleting BuAuthruleParamater instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuAuthruleParamater findById(java.lang.Long id) {
		log.debug("getting BuAuthruleParamater instance with id: " + id);
		try {
			BuAuthruleParamater instance = (BuAuthruleParamater) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuAuthruleParamater", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuAuthruleParamater instance) {
		log.debug("finding BuAuthruleParamater instance by example");
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
		log.debug("finding BuAuthruleParamater instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuAuthruleParamater as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByAuthId(Object authId) {
		return findByProperty(AUTH_ID, authId);
	}

	public List findByPartyId(Object partyId) {
		return findByProperty(PARTY_ID, partyId);
	}

	public List findByParameterTypeId(Object parameterTypeId) {
		return findByProperty(PARAMETER_TYPE_ID, parameterTypeId);
	}

	public List findByParameterNo(Object parameterNo) {
		return findByProperty(PARAMETER_NO, parameterNo);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByValue(Object value) {
		return findByProperty(VALUE, value);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuAuthruleParamater instances");
		try {
			String queryString = "from BuAuthruleParamater";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuAuthruleParamater merge(BuAuthruleParamater detachedInstance) {
		log.debug("merging BuAuthruleParamater instance");
		try {
			BuAuthruleParamater result = (BuAuthruleParamater) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuAuthruleParamater instance) {
		log.debug("attaching dirty BuAuthruleParamater instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuAuthruleParamater instance) {
		log.debug("attaching clean BuAuthruleParamater instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuAuthruleParamaterDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuAuthruleParamaterDAO) ctx.getBean("buAuthruleParamaterDAO");
	}
}