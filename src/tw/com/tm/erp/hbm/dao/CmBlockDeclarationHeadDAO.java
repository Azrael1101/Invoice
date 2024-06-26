package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.hbm.bean.CmBlockDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmBlockDeclarationItem;

/**
 * A data access object (DAO) providing persistence and search support for
 * CmBlockDeclarationHead entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 *
 * @see tw.com.tm.erp.hbm.dao.CmBlockDeclarationHead
 * @author MyEclipse Persistence Tools
 */

public class CmBlockDeclarationHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(CmBlockDeclarationHeadDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(CmBlockDeclarationHead transientInstance) {
		log.debug("saving CmBlockDeclarationHead instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CmBlockDeclarationHead persistentInstance) {
		log.debug("deleting CmBlockDeclarationHead instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CmBlockDeclarationHead findById(Long headId) {
		log.debug("getting CmBlockDeclarationHead instance with id: " + headId);
		try {
			CmBlockDeclarationHead instance = (CmBlockDeclarationHead) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.CmBlockDeclarationHead", headId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CmBlockDeclarationHead instance) {
		log.debug("finding CmBlockDeclarationHead instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding CmBlockDeclarationHead instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from CmBlockDeclarationHead as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all CmBlockDeclarationHead instances");
		try {
			String queryString = "from CmBlockDeclarationHead";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CmBlockDeclarationHead merge(CmBlockDeclarationHead detachedInstance) {
		log.debug("merging CmBlockDeclarationHead instance");
		try {
			CmBlockDeclarationHead result = (CmBlockDeclarationHead) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CmBlockDeclarationHead instance) {
		log.debug("attaching dirty CmBlockDeclarationHead instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CmBlockDeclarationHead instance) {
		log.debug("attaching clean CmBlockDeclarationHead instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CmBlockDeclarationHeadDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CmBlockDeclarationHeadDAO) ctx.getBean("CmBlockDeclarationHeadDAO");
	}

	public CmBlockDeclarationItem findExecuteByIdentification(Long headId, Long lineId) {
		StringBuffer hql = new StringBuffer(
				"from CmBlockDeclarationItem as model where model.cmBlockDeclarationHead.headId = ? and model.lineId = ?");
		List<CmBlockDeclarationItem> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}