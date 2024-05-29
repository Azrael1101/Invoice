package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImItemImage;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItemImage entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItemImage
 * @author MyEclipse Persistence Tools
 */

public class ImItemImageDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImItemImageDAO.class);
	// property constants
	public static final String IMAGE_NAME = "imageName";
	public static final String CONTENT = "content";
	public static final String CONTENT_TYPE = "contentType";
	public static final String CONTENT_SIZE = "contentSize";
	public static final String DESCRIPTION = "description";
	public static final String ENABLE = "enable";
	public static final String IS_DEFAULT = "isDefault";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImItemImage transientInstance) {
		log.debug("saving ImItemImage instance");
		try {
			if(transientInstance.getContent()== null) System.out.println("aaaaa");
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public boolean update(ImItemImage transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			//throw re;
			result = false;
		}
		return result;
	}
	public void delete(ImItemImage persistentInstance) {
		log.debug("deleting ImItemImage instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImItemImage findById(java.lang.Long id) {
		log.debug("getting ImItemImage instance with id: " + id);
		try {
			ImItemImage instance = (ImItemImage) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImItemImage", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImItemImage instance) {
		log.debug("finding ImItemImage instance by example");
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
		log.debug("finding ImItemImage instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ImItemImage as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByImageName(Object imageName) {
		return findByProperty(IMAGE_NAME, imageName);
	}

	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List findByContentType(Object contentType) {
		return findByProperty(CONTENT_TYPE, contentType);
	}

	public List findByContentSize(Object contentSize) {
		return findByProperty(CONTENT_SIZE, contentSize);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByIsDefault(Object isDefault) {
		return findByProperty(IS_DEFAULT, isDefault);
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

	public List findAll() {
		log.debug("finding all ImItemImage instances");
		try {
			String queryString = "from ImItemImage";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImItemImage merge(ImItemImage detachedInstance) {
		log.debug("merging ImItemImage instance");
		try {
			ImItemImage result = (ImItemImage) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImItemImage instance) {
		log.debug("attaching dirty ImItemImage instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImItemImage instance) {
		log.debug("attaching clean ImItemImage instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImItemImageDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImItemImageDAO) ctx.getBean("imItemImageDAO");
	}
}