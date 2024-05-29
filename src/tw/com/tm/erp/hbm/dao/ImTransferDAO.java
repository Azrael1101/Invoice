package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.ImTransfer;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImTransfer entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.dao.ImTransfer
 * @author MyEclipse Persistence Tools
 */

public class ImTransferDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImTransferDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(ImTransfer transientInstance) {
		log.debug("saving ImTransfer instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImTransfer persistentInstance) {
		log.debug("deleting ImTransfer instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImTransfer findLine(Long lineId) {
		log.debug("getting ImTransfer instance with lineId: " + lineId);
		try {
			ImTransfer instance = (ImTransfer) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImTransfer", lineId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	
	
	
	public ImTransfer findId(Long lineId) {
		log.debug("getting ImTransfer instance with lineId: " + lineId);
		try {
			ImTransfer instance = (ImTransfer) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImTransfer", lineId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	
	
	
	public ImTransfer orderNo(Long orderNo) {
		log.debug("getting ImTransfer instance with orderNo: " + orderNo);
		try {
			ImTransfer instance = (ImTransfer) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImTransfer", orderNo);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}	

	
	
	
	
	
	public List findByExample(ImTransfer instance) {
		log.debug("finding ImTransfer instance by example");
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
		log.debug("finding ImTransfer instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ImTransfer as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all ImTransfer instances");
		try {
			String queryString = "from ImTransfer";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImTransfer merge(ImTransfer detachedInstance) {
		log.debug("merging ImTransfer instance");
		try {
			ImTransfer result = (ImTransfer) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImTransfer instance) {
		log.debug("attaching dirty ImTransfer instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImTransfer instance) {
		log.debug("attaching clean ImTransfer instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImTransferDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ImTransferDAO) ctx.getBean("ImTransferDAO");
	}
}