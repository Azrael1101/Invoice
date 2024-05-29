package tw.com.tm.erp.hbm.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCustomerWithAddressView entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView
 * @author MyEclipse Persistence Tools
 */

public class BuCustomerWithAddressViewDAO extends HibernateDaoSupport {
    private static final Log log = LogFactory
	    .getLog(BuCustomerWithAddressViewDAO.class);

    // property constants

    protected void initDao() {
	// do nothing
    }

    public void save(BuCustomerWithAddressView transientInstance) {
	log.debug("saving BuCustomerWithAddressView instance");
	try {
	    getHibernateTemplate().save(transientInstance);
	    log.debug("save successful");
	} catch (RuntimeException re) {
	    log.error("save failed", re);
	    throw re;
	}
    }

    public void delete(BuCustomerWithAddressView persistentInstance) {
	log.debug("deleting BuCustomerWithAddressView instance");
	try {
	    getHibernateTemplate().delete(persistentInstance);
	    log.debug("delete successful");
	} catch (RuntimeException re) {
	    log.error("delete failed", re);
	    throw re;
	}
    }

    public BuCustomerWithAddressView findById(String id) {
	log.debug("getting BuCustomerWithAddressView instance with id: " + id);
	try {
	    BuCustomerWithAddressView instance = (BuCustomerWithAddressView) getHibernateTemplate()
		    .get("tw.com.tm.erp.hbm.bean.BuCustomerWithAddressView", id);
	    return instance;
	} catch (RuntimeException re) {
	    log.error("get failed", re);
	    throw re;
	}
    }

    public List findByExample(BuCustomerWithAddressView instance) {
	log.debug("finding BuCustomerWithAddressView instance by example");
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
	log.debug("finding BuCustomerWithAddressView instance with property: "
		+ propertyName + ", value: " + value);
	try {
	    String queryString = "from BuCustomerWithAddressView as model where model."
		    + propertyName + "= ?";
	    return getHibernateTemplate().find(queryString, value);
	} catch (RuntimeException re) {
	    log.error("find by property name failed", re);
	    throw re;
	}
    }

    public List findAll() {
	log.debug("finding all BuCustomerWithAddressView instances");
	try {
	    String queryString = "from BuCustomerWithAddressView";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    public BuCustomerWithAddressView findCustomerByType(String brandCode,
	    String customerCode, String type, String isEnable) {

	StringBuffer hql = new StringBuffer(
		"from BuCustomerWithAddressView as model ");
	hql.append("where model.brandCode = ? ");

	if ("customerCode".equals(type))
	    hql.append("and model.customerCode = ? ");
	else
	    hql.append("and model.identityCode = ? ");
	
	if (StringUtils.hasText(isEnable))
	    hql.append("and model.enable = ? ");
	
	Object[] objArray = null;
	if (StringUtils.hasText(isEnable)){
	    objArray = new Object[]{brandCode, customerCode, isEnable};
	}else{
	    objArray = new Object[]{brandCode, customerCode};
	}

	List<BuCustomerWithAddressView> result = getHibernateTemplate().find(
		hql.toString(), objArray);
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }

    public BuCustomerWithAddressView findCustomerByAddressBookIdAndBrandCode(
	    Long addressBookId, String brandCode) {

	StringBuffer hql = new StringBuffer(
		"from BuCustomerWithAddressView as model ");
	hql.append("where model.brandCode = ? ");
	hql.append("and model.addressBookId = ? ");

	List<BuCustomerWithAddressView> result = getHibernateTemplate().find(
		hql.toString(), new Object[] { brandCode, addressBookId });
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }

    public BuCustomerWithAddressView merge(
	    BuCustomerWithAddressView detachedInstance) {
	log.debug("merging BuCustomerWithAddressView instance");
	try {
	    BuCustomerWithAddressView result = (BuCustomerWithAddressView) getHibernateTemplate()
		    .merge(detachedInstance);
	    log.debug("merge successful");
	    return result;
	} catch (RuntimeException re) {
	    log.error("merge failed", re);
	    throw re;
	}
    }

    public void attachDirty(BuCustomerWithAddressView instance) {
	log.debug("attaching dirty BuCustomerWithAddressView instance");
	try {
	    getHibernateTemplate().saveOrUpdate(instance);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public void attachClean(BuCustomerWithAddressView instance) {
	log.debug("attaching clean BuCustomerWithAddressView instance");
	try {
	    getHibernateTemplate().lock(instance, LockMode.NONE);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public static BuCustomerWithAddressViewDAO getFromApplicationContext(
	    ApplicationContext ctx) {
	return (BuCustomerWithAddressViewDAO) ctx
		.getBean("buCustomerWithAddressViewDAO");
    }
	/**
	 * 依據品牌代號及客戶代號，查詢啟用狀態之客戶資料
	 * 
	 * @param brandCode
	 * @param customerCode
	 * @return BuCustomerWithAddressView
	 */
	public BuCustomerWithAddressView findEnableCustomerById(String brandCode,
			String customerCode) {
		try {
			StringBuffer hql = new StringBuffer(
					"from BuCustomerWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.customerCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuCustomerWithAddressView> lists = getHibernateTemplate().find(hql.toString(),
							new Object[] { brandCode, customerCode, "Y" });
			return (lists.size() > 0 ? lists.get(0) : null);

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據品牌代號及客戶代號，查詢啟用狀態之客戶資料
	 * 
	 * @param brandCode
	 * @param customerCode
	 * @return BuCustomerWithAddressView
	 */
	public BuCustomerWithAddressView findEnableCustomer(String brandCode,
			String customerCode) {
		log.info("findEnableCustomerById...........");
		log.info("brandCode~~"+brandCode);
		log.info("customerCode~~"+customerCode);
		try {
			StringBuffer hql = new StringBuffer(
					"from BuCustomerWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.customerCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuCustomerWithAddressView> lists = getHibernateTemplate().find(hql.toString(),
							new Object[] { brandCode, customerCode, "可用" });
			log.info("~~~~~~~~~~~~"+lists);
			return (lists.size() > 0 ? lists.get(0) : null);

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<BuCustomerWithAddressView> findVIPByCode(String brandCode,String customerCode) {

		try {
			StringBuffer hql = new StringBuffer(
					"from BuCustomerWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.customerCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuCustomerWithAddressView> lists = getHibernateTemplate().find(hql.toString(),new Object[] { brandCode, customerCode, "可用" });

			return lists;

		} catch (RuntimeException re) {
			throw re;
		}
	}
	public List<BuCustomerWithAddressView> findVIPByPhone(String brandCode,String mobilePhone) {

		try {
			StringBuffer hql = new StringBuffer(
					"from BuCustomerWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.mobilePhone = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuCustomerWithAddressView> lists = getHibernateTemplate().find(hql.toString(),new Object[] { brandCode, mobilePhone, "可用" });

			return lists;

		} catch (RuntimeException re) {
			throw re;
		}
	}
	public List<BuCustomerWithAddressView> findVIPByID(String brandCode,String identityCode) {

		try {
			StringBuffer hql = new StringBuffer(
					"from BuCustomerWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.identityCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuCustomerWithAddressView> lists = getHibernateTemplate().find(hql.toString(),new Object[] { brandCode, identityCode, "可用" });

			return lists;

		} catch (RuntimeException re) {
			throw re;
		}
	}
}