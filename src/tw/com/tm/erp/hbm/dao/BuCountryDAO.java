package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCountry;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCountry entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCountry
 * @author MyEclipse Persistence Tools
 */

public class BuCountryDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuCountryDAO.class);

	// property constants
	public static final String COUNTRY_CNAME = "countryCName";

	public static final String COUNTRY_ENAME = "countryEName";

	public static final String DESCRIPTION = "description";

	public static final String ENABLE = "enable";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuCountry buCountry) {
		log.debug("saving BuCountry instance");
		try {
			getHibernateTemplate().save(buCountry);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(BuCountry buCountry) {
		try {
			getHibernateTemplate().update(buCountry);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void saveOrUpdate(BuCountry buCountry) {
		if(buCountry.getCountryCode() == null || "".equals(buCountry.getCountryCode())) {
			save(buCountry);
		} else {
			update(buCountry);
		}
	}

	public void delete(BuCountry persistentInstance) {
		log.debug("deleting BuCountry instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuCountry findById(java.lang.String id) {
		log.debug("getting BuCountry instance with id: " + id);
		try {
			BuCountry instance = (BuCountry) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuCountry", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuCountry instance) {
		log.debug("finding BuCountry instance by example");
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
		log.debug("finding BuCountry instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuCountry as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCountryCName(Object countryCName) {
		return findByProperty(COUNTRY_CNAME, countryCName);
	}

	public List findByCountryEName(Object countryEName) {
		return findByProperty(COUNTRY_ENAME, countryEName);
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
		log.debug("finding all BuCountry instances");
		try {
			String queryString = "from BuCountry";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 依據啟用狀態查詢出國別
	 * 
	 * @param isEnable
	 * @return List
	 */
	public List findCountryByEnable(String isEnable) {
		
	    StringBuffer hql = new StringBuffer("from BuCountry as model");
	    if (StringUtils.hasText(isEnable)){
                hql.append(" where model.enable = ?");
                getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
                return getHibernateTemplate().find(hql.toString(), new Object[]{isEnable});
	    }
	    
	    return getHibernateTemplate().find(hql.toString());		
	}
	
	public BuCountry merge(BuCountry detachedInstance) {
		log.debug("merging BuCountry instance");
		try {
			BuCountry result = (BuCountry) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuCountry instance) {
		log.debug("attaching dirty BuCountry instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuCountry instance) {
		log.debug("attaching clean BuCountry instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuCountryDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuCountryDAO) ctx.getBean("buCountryDAO");
	}
	
	/**
	 * 依照輸入條件來搜尋
	 * 
	 * @param countryCode
	 * @param countryCName
	 * @param countryEName
	 * @return List
	 */
	public List<BuCountry> findCountryList(String countryCode,
			String countryCName, String countryEName) {
	    countryCode = countryCode.toUpperCase() + "%";
	    countryCName = "%" + countryCName + "%";
	    countryEName = "%" + countryEName.toUpperCase() + "%";
		
	    StringBuffer hql = new StringBuffer("from BuCountry as model ");
	    hql.append("where model.countryCode like ? ");
	    hql.append("and model.countryCName like ? ");
            hql.append("and upper(model.countryEName) like ? ");
	    getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
	    return getHibernateTemplate().find(hql.toString(),new String[] { countryCode, countryCName, countryEName });		
	}
}