package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.FiBank;

/**
 * A data access object (DAO) providing persistence and search support for
 * FiBank entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.FiBank
 * @author MyEclipse Persistence Tools
 */

public class FiBankDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(FiBankDAO.class);

	// property constants
	public static final String BRAND_ID = "brandId";

	public static final String SOURCE_ID = "sourceId";

	public static final String BANK_CODE = "bankCode";

	public static final String BANK_NAME = "bankName";

	public static final String ACCOUNT_CODE = "accountCode";

	public static final String ACCOUNT_NAME = "accountName";

	public static final String DEFAULT_BANK = "defaultBank";

	public static final String ENABLE = "enable";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(FiBank transientInstance) {
		log.debug("saving FiBank instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FiBank persistentInstance) {
		log.debug("deleting FiBank instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FiBank findById(java.lang.Long id) {
		log.debug("getting FiBank instance with id: " + id);
		try {
			FiBank instance = (FiBank) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.FiBank", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(FiBank instance) {
		log.debug("finding FiBank instance by example");
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
		log.debug("finding FiBank instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from FiBank as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBrandId(Object brandId) {
		return findByProperty(BRAND_ID, brandId);
	}

	public List findBySourceId(Object sourceId) {
		return findByProperty(SOURCE_ID, sourceId);
	}

	public List findByBankCode(Object bankCode) {
		return findByProperty(BANK_CODE, bankCode);
	}

	public List findByBankName(Object bankName) {
		return findByProperty(BANK_NAME, bankName);
	}

	public List findByAccountCode(Object accountCode) {
		return findByProperty(ACCOUNT_CODE, accountCode);
	}

	public List findByAccountName(Object accountName) {
		return findByProperty(ACCOUNT_NAME, accountName);
	}

	public List findByDefaultBank(Object defaultBank) {
		return findByProperty(DEFAULT_BANK, defaultBank);
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
		log.debug("finding all FiBank instances");
		try {
			String queryString = "from FiBank";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FiBank merge(FiBank detachedInstance) {
		log.debug("merging FiBank instance");
		try {
			FiBank result = (FiBank) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FiBank instance) {
		log.debug("attaching dirty FiBank instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FiBank instance) {
		log.debug("attaching clean FiBank instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static FiBankDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FiBankDAO) ctx.getBean("fiBankDAO");
	}
}