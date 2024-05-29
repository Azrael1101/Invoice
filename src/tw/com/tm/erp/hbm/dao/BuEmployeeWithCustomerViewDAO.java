package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuEmployeeWithAddressView entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeWithCustomerViewDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(BuEmployeeWithCustomerViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public BuEmployeeWithCustomerViewDAO findById(String id) {
		log.debug("getting BuEmployeeWithCustomerViewDAO instance with id: " + id);
		try {
			BuEmployeeWithCustomerViewDAO instance = (BuEmployeeWithCustomerViewDAO) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuEmployeeWithCustomerViewDAO", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuEmployeeWithCustomerViewDAO instance) {
		log.debug("finding BuEmployeeWithCustomerViewDAO instance by example");
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
		log.debug("finding BuEmployeeWithCustomerViewDAO instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuEmployeeWithCustomerViewDAO as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all BuEmployeeWithCustomerViewDAO instances");
		try {
			String queryString = "from BuEmployeeWithCustomerViewDAO";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuEmployeeWithCustomerViewDAO merge(
			BuEmployeeWithCustomerViewDAO detachedInstance) {
		log.debug("merging BuEmployeeWithCustomerViewDAO instance");
		try {
			BuEmployeeWithCustomerViewDAO result = (BuEmployeeWithCustomerViewDAO) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuEmployeeWithCustomerViewDAO instance) {
		log.debug("attaching dirty BuEmployeeWithCustomerViewDAO instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuEmployeeWithCustomerViewDAO instance) {
		log.debug("attaching clean BuEmployeeWithCustomerViewDAO instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	
	/**
	 * 依據品牌代號及員工代號，查詢啟用狀態之員工資料
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithCustomerViewDAO
	 */
	public BuEmployeeWithCustomerViewDAO findEnableEmployeeById(
			String organizationCode, String employeeCode) {
		try {
			StringBuffer hql = new StringBuffer(
					"from BuEmployeeWithCustomerViewDAO as model ");
			hql.append("where model.organizationCode = ? ");
			hql.append("and model.employeeCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuEmployeeWithCustomerViewDAO> lists = getHibernateTemplate()
					.find(
							hql.toString(),
							new Object[] { organizationCode, employeeCode, "Y" });
			return (lists.size() > 0 ? lists.get(0) : null);

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	 * 依據品牌代號和工號進行查詢
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithCustomerViewDAO
	 */
	public BuEmployeeWithCustomerViewDAO findbyAddressBookIdAndBrandCode(Long addressBookId, String brandCode) {

	    StringBuffer hql = new StringBuffer("select model from BuEmployeeWithCustomerViewDAO as model");
	    hql.append(" where 1 = 1");
	    hql.append(" and model.addressBookId = ?");
	    hql.append(" and model.brandCode = ?");
		
	    Object[] parameterArray = new Object[] {addressBookId, brandCode};
	    List<BuEmployeeWithCustomerViewDAO> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	    return (result != null && result.size() > 0 ? result.get(0) : null);
        }
}