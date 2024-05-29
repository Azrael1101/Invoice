package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImItemCompose;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItemCompose entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItemCompose
 * @author MyEclipse Persistence Tools
 */

public class ImItemComposeDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImItemComposeDAO.class);
	// property constants
	public static final String COMPOSE_ITEM_ID = "composeItemId";
	public static final String QUANTITY = "quantity";
	public static final String STATUS = "status";
	public static final String REMARK = "remark";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";
	public static final String IM_ITEM = "imItem";

	protected void initDao() {
		// do nothing
	}

	public void save(ImItemCompose transientInstance) {
		log.debug("saving ImItemCompose instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImItemCompose persistentInstance) {
		log.debug("deleting ImItemCompose instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImItemCompose findById(java.lang.Long id) {
		log.debug("getting ImItemCompose instance with id: " + id);
		try {
			ImItemCompose instance = (ImItemCompose) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImItemCompose", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImItemCompose instance) {
		log.debug("finding ImItemCompose instance by example");
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
		log.debug("finding ImItemCompose instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImItemCompose as model where model."
					+ propertyName + "= ? order by model.indexNo ";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByComposeItemId(Object composeItemId) {
		return findByProperty(COMPOSE_ITEM_ID, composeItemId);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findByIndexNo(Object indexNo) {
		return findByProperty(INDEX_NO, indexNo);
	}
	
	public List findByItem(Object imItem) {
		return findByProperty(IM_ITEM, imItem);
	}

	public List findAll() {
		log.debug("finding all ImItemCompose instances");
		try {
			String queryString = "from ImItemCompose";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImItemCompose merge(ImItemCompose detachedInstance) {
		log.debug("merging ImItemCompose instance");
		try {
			ImItemCompose result = (ImItemCompose) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImItemCompose instance) {
		log.debug("attaching dirty ImItemCompose instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImItemCompose instance) {
		log.debug("attaching clean ImItemCompose instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImItemComposeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImItemComposeDAO) ctx.getBean("imItemComposeDAO");
	}
}