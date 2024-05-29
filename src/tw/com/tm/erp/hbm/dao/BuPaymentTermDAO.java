package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuPaymentTerm entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuPaymentTerm
 * @author MyEclipse Persistence Tools
 */

public class BuPaymentTermDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(BuPaymentTermDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String BASE_DATE_CODE = "baseDateCode";
	public static final String BILL_TYPE_CODE = "billTypeCode";
	public static final String PAYMENT_START_DATE = "paymentStartDate";
	public static final String PAYMENT_DAYS = "paymentDays";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}


	public boolean save(BuPaymentTerm transientInstance) {
		boolean result = false;
		log.debug("saving FiPaymentTerm instance");		
		try {
			getHibernateTemplate().save(transientInstance);
			//log.debug("save successful");
			result = true;
		} catch (RuntimeException re) {
			result = false;
			//log.error("save failed", re);
			//throw re;			
		} 
		return result;
	}
	/**
	 * update BuPaymentTerm
	 * @param BuPaymentTerm
	 */
	public boolean update(BuPaymentTerm transientInstance) {
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

	public void delete(BuPaymentTerm persistentInstance) {
		log.debug("deleting BuPaymentTerm instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuPaymentTerm findById(tw.com.tm.erp.hbm.bean.BuPaymentTermId id) {
		log.debug("getting BuPaymentTerm instance with id: " + id);
		try {
			BuPaymentTerm instance = (BuPaymentTerm) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuPaymentTerm", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuPaymentTerm instance) {
		log.debug("finding BuPaymentTerm instance by example");
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
		log.debug("finding BuPaymentTerm instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuPaymentTerm as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByBaseDateCode(Object baseDateCode) {
		return findByProperty(BASE_DATE_CODE, baseDateCode);
	}

	public List findByBillTypeCode(Object billTypeCode) {
		return findByProperty(BILL_TYPE_CODE, billTypeCode);
	}

	public List findByPaymentStartDate(Object paymentStartDate) {
		return findByProperty(PAYMENT_START_DATE, paymentStartDate);
	}

	public List findByPaymentDays(Object paymentDays) {
		return findByProperty(PAYMENT_DAYS, paymentDays);
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
		log.debug("finding all BuPaymentTerm instances");
		try {
			String queryString = "from BuPaymentTerm";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 依據組織代號、啟用狀態查詢出付款條件
	 * 
	 * @param organizationCode
	 * @param isEnable
	 * @return List
	 */
        public List findPaymentTermByOrganizationAndEnable(String organizationCode, String isEnable) {
//		因組織代號變更，因此將組織代號篩選條件移除 by Yao
	    StringBuffer hql = new StringBuffer("from BuPaymentTerm as model");
	    hql.append(" where 1=1");
//	    hql.append(" where model.id.organizationCode = ?");
	    if (StringUtils.hasText(isEnable)){
	        hql.append(" and model.enable = ?");
//	        return getHibernateTemplate().find(hql.toString(), new Object[]{organizationCode, isEnable});
	        return getHibernateTemplate().find(hql.toString(), new Object[]{isEnable});
            }
		    
//	    return getHibernateTemplate().find(hql.toString(), new Object[]{organizationCode});
	    return getHibernateTemplate().find(hql.toString());		
	}

	public BuPaymentTerm merge(BuPaymentTerm detachedInstance) {
		log.debug("merging BuPaymentTerm instance");
		try {
			BuPaymentTerm result = (BuPaymentTerm) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuPaymentTerm instance) {
		log.debug("attaching dirty BuPaymentTerm instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuPaymentTerm instance) {
		log.debug("attaching clean BuPaymentTerm instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuPaymentTermDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuPaymentTermDAO) ctx.getBean("buPaymentTermDAO");
	}
	
	/**
	 * 付款條件查詢作業
	 * 
	 * @param organizationCode
	 * @param paymentTermCode
	 * @param name
	 * @param baseDateCode
	 * @param billTypeCode
	 * @return List
	 */
	public List<BuPaymentTerm> findPaymentTermList(String organizationCode,
			String paymentTermCode, String name, String baseDateCode,
			String billTypeCode) {
		try {
			paymentTermCode = "%" + paymentTermCode.toUpperCase() + "%";
			name = "%" + name + "%";
			baseDateCode = "%" + baseDateCode + "%";
			billTypeCode = "%" + billTypeCode + "%";

			StringBuffer hql = new StringBuffer("from BuPaymentTerm as model ");
			hql.append("where upper(model.id.organizationCode) = ? ");
			hql.append("and upper(model.id.paymentTermCode) like ? ");
			hql.append("and model.name like ? ");
			hql.append("and model.baseDateCode like ? ");
			hql.append("and model.billTypeCode like ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(
					hql.toString(),
					new Object[] { organizationCode, paymentTermCode, name,
							baseDateCode, billTypeCode });

		} catch (RuntimeException re) {
			throw re;
		}
	}
}