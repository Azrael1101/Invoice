package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ApprovalResult;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApprovalResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ApprovalResult
 * @author MyEclipse Persistence Tools
 */

public class ApprovalResultDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ApprovalResultDAO.class);

	// property constants
	public static final String PROCESS_ID = "processId";

	public static final String FORM_NAME = "formName";

	public static final String NODE_NAME = "nodeName";

	public static final String ACTIVITY_ID = "activityId";

	public static final String APPROVER = "approver";

	public static final String RESULT = "result";

	public static final String MEMO = "memo";

	public static final String APPROVAL_TYPE = "approvalType";

	protected void initDao() {
		// do nothing
	}
	
	public void saveOrUpdate(ApprovalResult result) {
		if(result.getId() == null || result.getId() == 0) {
			this.save(result);
		} else {
			this.update(result);
		}
	}

	public void save(ApprovalResult transientInstance) {
		log.debug("saving ApprovalResult instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(ApprovalResult result) {
		try {
			getHibernateTemplate().saveOrUpdate(result);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(ApprovalResult persistentInstance) {
		log.debug("deleting ApprovalResult instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApprovalResult findById(java.lang.Long id) {
		log.debug("getting ApprovalResult instance with id: " + id);
		try {
			ApprovalResult instance = (ApprovalResult) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ApprovalResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApprovalResult instance) {
		log.debug("finding ApprovalResult instance by example");
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
		log.debug("finding ApprovalResult instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ApprovalResult as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProcessId(Object processId) {
		return findByProperty(PROCESS_ID, processId);
	}

	public List findByFormName(Object formName) {
		return findByProperty(FORM_NAME, formName);
	}

	public List findByNodeName(Object nodeName) {
		return findByProperty(NODE_NAME, nodeName);
	}

	public List findByActivityId(Object activityId) {
		return findByProperty(ACTIVITY_ID, activityId);
	}

	public List findByApprover(Object approver) {
		return findByProperty(APPROVER, approver);
	}

	public List findByResult(Object result) {
		return findByProperty(RESULT, result);
	}

	public List findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List findByApprovalType(Object approvalType) {
		return findByProperty(APPROVAL_TYPE, approvalType);
	}

	public List findAll() {
		log.debug("finding all ApprovalResult instances");
		try {
			String queryString = "from ApprovalResult";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApprovalResult merge(ApprovalResult detachedInstance) {
		log.debug("merging ApprovalResult instance");
		try {
			ApprovalResult result = (ApprovalResult) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApprovalResult instance) {
		log.debug("attaching dirty ApprovalResult instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApprovalResult instance) {
		log.debug("attaching clean ApprovalResult instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ApprovalResultDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ApprovalResultDAO) ctx.getBean("approvalResultDAO");
	}
}