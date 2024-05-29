package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.bean.ImWarehouseType;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImWarehouseType entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImWarehouseType
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouseTypeDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImWarehouseTypeDAO.class);
	// property constants
	public static final String TYPE_CODE = "typeCode";
	public static final String TYPE_LEVEL = "typeLevel";
	public static final String NAME = "name";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(ImWarehouseType transientInstance) {
		log.debug("saving ImWarehouseType instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImWarehouseType persistentInstance) {
		log.debug("deleting ImWarehouseType instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImWarehouseType findById(java.lang.Long id) {
		log.debug("getting ImWarehouseType instance with id: " + id);
		try {
			ImWarehouseType instance = (ImWarehouseType) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImWarehouseType", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImWarehouseType instance) {
		log.debug("finding ImWarehouseType instance by example");
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
		log.debug("finding ImWarehouseType instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImWarehouseType as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}

	public List findByTypeLevel(Object typeLevel) {
		return findByProperty(TYPE_LEVEL, typeLevel);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
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
		log.debug("finding all ImWarehouseType instances");
		try {
			String queryString = "from ImWarehouseType";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImWarehouseType merge(ImWarehouseType detachedInstance) {
		log.debug("merging ImWarehouseType instance");
		try {
			ImWarehouseType result = (ImWarehouseType) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImWarehouseType instance) {
		log.debug("attaching dirty ImWarehouseType instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImWarehouseType instance) {
		log.debug("attaching clean ImWarehouseType instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImWarehouseTypeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImWarehouseTypeDAO) ctx.getBean("imWarehouseTypeDAO");
	}
}