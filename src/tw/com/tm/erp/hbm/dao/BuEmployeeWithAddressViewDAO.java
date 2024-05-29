package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuItemCategoryPrivilege;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

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

public class BuEmployeeWithAddressViewDAO extends BaseDAO {
	private static final Log log = LogFactory
	.getLog(BuEmployeeWithAddressViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(BuEmployeeWithAddressView transientInstance) {
		log.debug("saving BuEmployeeWithAddressView instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuEmployeeWithAddressView persistentInstance) {
		log.debug("deleting BuEmployeeWithAddressView instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuEmployeeWithAddressView findById(String id) {
		log.debug("getting BuEmployeeWithAddressView instance with id: " + id);
		try {
			BuEmployeeWithAddressView instance = (BuEmployeeWithAddressView) getHibernateTemplate()
			.get("tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public BuEmployeeWithAddressView findById1(String inChargeName) {
		log.debug("getting BuEmployeeWithAddressView instance with id: " + inChargeName);
		try {
			BuEmployeeWithAddressView instance = (BuEmployeeWithAddressView) getHibernateTemplate()
			.get("tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView", inChargeName);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuEmployeeWithAddressView instance) {
		log.debug("finding BuEmployeeWithAddressView instance by example");
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
		log.debug("finding BuEmployeeWithAddressView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuEmployeeWithAddressView as model where model."
				+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all BuEmployeeWithAddressView instances");
		try {
			String queryString = "from BuEmployeeWithAddressView";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuEmployeeWithAddressView merge(
			BuEmployeeWithAddressView detachedInstance) {
		log.debug("merging BuEmployeeWithAddressView instance");
		try {
			BuEmployeeWithAddressView result = (BuEmployeeWithAddressView) getHibernateTemplate()
			.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuEmployeeWithAddressView instance) {
		log.debug("attaching dirty BuEmployeeWithAddressView instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuEmployeeWithAddressView instance) {
		log.debug("attaching clean BuEmployeeWithAddressView instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuEmployeeWithAddressViewDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuEmployeeWithAddressViewDAO) ctx
		.getBean("buEmployeeWithAddressViewDAO");
	}

	/**
	 * 依據品牌代號及員工代號，查詢啟用狀態之員工資料
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithAddressView
	 */
	public BuEmployeeWithAddressView findEnableEmployeeById(
			String organizationCode, String employeeCode) {
		try {
			StringBuffer hql = new StringBuffer(
			"from BuEmployeeWithAddressView as model ");
			hql.append("where model.organizationCode = ? ");
			hql.append("and model.employeeCode = ? ");
			hql.append("and model.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuEmployeeWithAddressView> lists = getHibernateTemplate()
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
	 * @return BuEmployeeWithAddressView
	 */
	public BuEmployeeWithAddressView findbyBrandCodeAndEmployeeCode(String brandCode, String employeeCode) {

		StringBuffer hql = new StringBuffer("select model from BuEmployeeWithAddressView as model, BuEmployeeBrand as model2");
		hql.append(" where model.employeeCode = model2.id.employeeCode");
		hql.append(" and model2.id.brandCode = ?");
		hql.append(" and model.employeeCode = ?");

		Object[] parameterArray = new Object[] {brandCode, employeeCode};
		List<BuEmployeeWithAddressView> result = getHibernateTemplate().find(hql.toString(), parameterArray);
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}


	/** 依照 BuEmployeeWithAddressView 業種, 部門, 職位, 尋找 employee 
	 * @param findObj
	 * @return
	 */
	public List<BuEmployeeWithAddressView> findByBuItemCategoryPrivilege( HashMap findObjs ){
		final HashMap fos = findObjs;
		List<BuEmployeeWithAddressView> re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model from BuEmployeeWithAddressView as model, BuItemCategoryPrivilege as model2");
				hql.append(" where model.employeeCode = model2.employeeCode");

				if (StringUtils.hasText((String) fos.get("itemCategory")))
					hql.append(" and model2.itemCategory ='").append((String) fos.get("itemCategory")).append("'");

				if (StringUtils.hasText((String) fos.get("employeeDepartment")))
					hql.append(" and model2.employeeDepartment ='").append((String) fos.get("employeeDepartment")).append("'");

				if (StringUtils.hasText((String) fos.get("employeePosition")))
					hql.append(" and model2.employeePosition ='").append((String) fos.get("employeePosition")).append("'");

				log.info(hql.toString());
				Query query = session.createQuery(hql.toString());
				//query.setFirstResult(0);
				//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				return query.list();
			}
		});
		return re;
	}

	/** 依照 BuEmployeeWithAddressView 工號, 英文名子, 中文名字, 尋找 employee 
	 * @param findObj
	 * @return
	 */
	public List<BuEmployeeWithAddressView> findByemployee( HashMap findObjs ){
		final HashMap fos = findObjs;
		List<BuEmployeeWithAddressView> re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from BuEmployeeWithAddressView as model, BuAddressBook as model2 ");
				hql.append(" where model.addressBookId = model2.addressBookId");

				log.info("employeeCode:"+StringUtils.hasText((String) fos.get("employeeCode")));
				if (StringUtils.hasText((String) fos.get("employeeCode")))
					hql.append(" and model.employeeCode ='").append((String) fos.get("employeeCode")).append("'");
				log.info("englishName:"+StringUtils.hasText((String) fos.get("englishName")));
				if (StringUtils.hasText((String) fos.get("englishName")))
					hql.append(" and model.englishName ='").append((String) fos.get("englishName")).append("'");
				log.info("chineseName:"+StringUtils.hasText((String) fos.get("chineseName")));
				if (StringUtils.hasText((String) fos.get("chineseName")))
					hql.append(" and model.chineseName ='").append((String) fos.get("chineseName")).append("'");
				log.info("tel1:"+StringUtils.hasText((String) fos.get("tel1")));
				if (StringUtils.hasText((String) fos.get("tel1")))
					hql.append(" and model2.tel1 ='").append((String) fos.get("tel1")).append("'");
				log.info(hql.toString());
				if (StringUtils.hasText((String) fos.get("employeeDepartment")))
					hql.append(" and model.employeeDepartment ='").append((String) fos.get("employeeDepartment")).append("'");
				log.info(hql.toString());
				Query query = session.createQuery(hql.toString());
				//query.setFirstResult(0);
				//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				return query.list();
			}
		});
		return re;
	}
	
	public List findByPropertyMis(String propertyName, Object value) {
		log.debug("finding BuEmployeeWithAddressView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuEmployeeWithAddressView as model where model."
				+ propertyName + "= ? and leaveDate = null";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}


	public List findCategoryPrivilegeByEmployeeCode(final String categoryType, final String itemCategory, 
			final String department, final String position, final String employeeCode){

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {		
				StringBuffer hql = new StringBuffer("select model from BuItemCategoryPrivilege as model where 1=1");
				if (StringUtils.hasText(categoryType))
					hql.append(" and model.categoryType = :categoryType");

				if (StringUtils.hasText(itemCategory))
					hql.append(" and model.itemCategory = :itemCategory");

				if (StringUtils.hasText(department))
					hql.append(" and model.employeeDepartment = :department");

				if (StringUtils.hasText(position))
					hql.append(" and model.employeePosition = :position");

				if (StringUtils.hasText(employeeCode))
					hql.append(" and model.employeeCode = :employeeCode");

				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText(categoryType))
					query.setString("categoryType", categoryType);

				if (StringUtils.hasText(itemCategory))
					query.setString("itemCategory", itemCategory);

				if (StringUtils.hasText(department))
					query.setString("department", department);

				if (StringUtils.hasText(position))
					query.setString("position", position);

				if (StringUtils.hasText(employeeCode))
					query.setString("employeeCode", employeeCode);		

				return query.list();
			}
		});
		return re;	
	}
}