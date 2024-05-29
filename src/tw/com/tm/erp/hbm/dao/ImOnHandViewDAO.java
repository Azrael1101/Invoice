package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImOnHandView;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImOnHandView entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImOnHandView
 * @author MyEclipse Persistence Tools
 */

public class ImOnHandViewDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImOnHandViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(ImOnHandView transientInstance) {
		log.debug("saving ImOnHandView instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImOnHandView persistentInstance) {
		log.debug("deleting ImOnHandView instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImOnHandView findById(java.lang.Long id) {
		log.debug("getting ImOnHandView instance with id: " + id);
		try {
			ImOnHandView instance = (ImOnHandView) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImOnHandView", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImOnHandView instance) {
		log.debug("finding ImOnHandView instance by example");
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
		log.debug("finding ImOnHandView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImOnHandView as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all ImOnHandView instances");
		try {
			String queryString = "from ImOnHandView";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImOnHandView merge(ImOnHandView detachedInstance) {
		log.debug("merging ImOnHandView instance");
		try {
			ImOnHandView result = (ImOnHandView) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImOnHandView instance) {
		log.debug("attaching dirty ImOnHandView instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImOnHandView instance) {
		log.debug("attaching clean ImOnHandView instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImOnHandViewDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImOnHandViewDAO) ctx.getBean("imOnHandViewDAO");
	}
}