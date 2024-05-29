package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImCategoryLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImCategoryLine entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImCategoryLine
 * @author MyEclipse Persistence Tools
 */

public class ImCategoryLineDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImCategoryLineDAO.class);

	// property constants
	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	public static final String ENABLE = "enable";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(ImCategoryLine transientInstance) {
		log.debug("saving ImCategoryLine instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImCategoryLine persistentInstance) {
		log.debug("deleting ImCategoryLine instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImCategoryLine findById(java.lang.Long id) {
		log.debug("getting ImCategoryLine instance with id: " + id);
		try {
			ImCategoryLine instance = (ImCategoryLine) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImCategoryLine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImCategoryLine instance) {
		log.debug("finding ImCategoryLine instance by example");
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
		log.debug("finding ImCategoryLine instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImCategoryLine as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
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
		log.debug("finding all ImCategoryLine instances");
		try {
			String queryString = "from ImCategoryLine";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImCategoryLine merge(ImCategoryLine detachedInstance) {
		log.debug("merging ImCategoryLine instance");
		try {
			ImCategoryLine result = (ImCategoryLine) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImCategoryLine instance) {
		log.debug("attaching dirty ImCategoryLine instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImCategoryLine instance) {
		log.debug("attaching clean ImCategoryLine instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImCategoryLineDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImCategoryLineDAO) ctx.getBean("imCategoryLineDAO");
	}
}