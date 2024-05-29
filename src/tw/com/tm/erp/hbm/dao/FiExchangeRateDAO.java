package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.FiExchangeRate;

/**
 * A data access object (DAO) providing persistence and search support for
 * FiExchangeRate entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.FiExchangeRate
 * @author MyEclipse Persistence Tools
 */

public class FiExchangeRateDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(FiExchangeRateDAO.class);

	// property constants
	public static final String BRANCH_ID = "branchId";

	public static final String SOURCE_CURRENCY = "sourceCurrency";

	public static final String AGAINST_CURRENCY = "againstCurrency";

	public static final String EXCHANGE_RATE = "exchangeRate";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(FiExchangeRate transientInstance) {
		log.debug("saving FiExchangeRate instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FiExchangeRate persistentInstance) {
		log.debug("deleting FiExchangeRate instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FiExchangeRate findById(java.lang.Long id) {
		log.debug("getting FiExchangeRate instance with id: " + id);
		try {
			FiExchangeRate instance = (FiExchangeRate) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.FiExchangeRate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(FiExchangeRate instance) {
		log.debug("finding FiExchangeRate instance by example");
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
		log.debug("finding FiExchangeRate instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from FiExchangeRate as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBranchId(Object branchId) {
		return findByProperty(BRANCH_ID, branchId);
	}

	public List findBySourceCurrency(Object sourceCurrency) {
		return findByProperty(SOURCE_CURRENCY, sourceCurrency);
	}

	public List findByAgainstCurrency(Object againstCurrency) {
		return findByProperty(AGAINST_CURRENCY, againstCurrency);
	}

	public List findByExchangeRate(Object exchangeRate) {
		return findByProperty(EXCHANGE_RATE, exchangeRate);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all FiExchangeRate instances");
		try {
			String queryString = "from FiExchangeRate";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FiExchangeRate merge(FiExchangeRate detachedInstance) {
		log.debug("merging FiExchangeRate instance");
		try {
			FiExchangeRate result = (FiExchangeRate) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FiExchangeRate instance) {
		log.debug("attaching dirty FiExchangeRate instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FiExchangeRate instance) {
		log.debug("attaching clean FiExchangeRate instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static FiExchangeRateDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (FiExchangeRateDAO) ctx.getBean("fiExchangeRateDAO");
	}
}