package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImPromotionFile;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPromotionFile entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPromotionFile
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionFileDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImPromotionFileDAO.class);
	// property constants
	public static final String FILE_ID = "fileId";
	public static final String FILE_NAME = "fileName";
	public static final String CONTENT = "content";
	public static final String CONTENT_TYPE = "contentType";
	public static final String CONTENT_SIZE = "contentSize";
	public static final String DESCRIPTION = "description";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPromotionFile transientInstance) {
		log.debug("saving ImPromotionFile instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public boolean update(ImPromotionFile transientInstance) {
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
	public void delete(ImPromotionFile persistentInstance) {
		log.debug("deleting ImPromotionFile instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImPromotionFile findById(java.lang.Long id) {
		log.debug("getting ImPromotionFile instance with id: " + id);
		try {
			ImPromotionFile instance = (ImPromotionFile) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImPromotionFile", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImPromotionFile instance) {
		log.debug("finding ImPromotionFile instance by example");
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
		log.debug("finding ImPromotionFile instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImPromotionFile as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFileId(Object fileId) {
		return findByProperty(FILE_ID, fileId);
	}

	public List findByFileName(Object fileName) {
		return findByProperty(FILE_NAME, fileName);
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
		log.debug("finding all ImPromotionFile instances");
		try {
			String queryString = "from ImPromotionFile";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImPromotionFile merge(ImPromotionFile detachedInstance) {
		log.debug("merging ImPromotionFile instance");
		try {
			ImPromotionFile result = (ImPromotionFile) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImPromotionFile instance) {
		log.debug("attaching dirty ImPromotionFile instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImPromotionFile instance) {
		log.debug("attaching clean ImPromotionFile instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImPromotionFileDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImPromotionFileDAO) ctx.getBean("imPromotionFileDAO");
	}
}