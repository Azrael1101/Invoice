package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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

import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.ImWarehouse;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuEmployee entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuEmployee
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuEmployeeDAO.class);
	// property constants
	public static final String ADDRESS_BOOK_ID = "addressBookId";
	public static final String EMPLOYEE_POSITION = "employeePosition";
	public static final String LOGIN_NAME = "loginName";
	public static final String TEL1 = "tel1";
	public static final String FAX1 = "fax1";
	public static final String _EMAIL1 = "EMail1";
	public static final String TEL2 = "tel2";
	public static final String FAX2 = "fax2";
	public static final String _EMAIL2 = "EMail2";
	public static final String TEL3 = "tel3";
	public static final String FAX3 = "fax3";
	public static final String _EMAIL3 = "EMail3";
	public static final String TEL4 = "tel4";
	public static final String FAX4 = "fax4";
	public static final String _EMAIL4 = "EMail4";
	public static final String CITY1 = "city1";
	public static final String AREA1 = "area1";
	public static final String ZIP_CODE1 = "zipCode1";
	public static final String ADDRESS1 = "address1";
	public static final String CITY2 = "city2";
	public static final String AREA2 = "area2";
	public static final String ZIP_CODE2 = "zipCode2";
	public static final String ADDRESS2 = "address2";
	public static final String CITY3 = "city3";
	public static final String AREA3 = "area3";
	public static final String ZIP_CODE3 = "zipCode3";
	public static final String ADDRESS3 = "address3";
	public static final String CITY4 = "city4";
	public static final String AREA4 = "area4";
	public static final String ZIP_CODE4 = "zipCode4";
	public static final String ADDRESS4 = "address4";
	public static final String REMARK1 = "remark1";
	public static final String REMARK2 = "remark2";
	public static final String REMARK3 = "remark3";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuEmployee transientInstance) {
		log.debug("saving BuEmployee instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(BuEmployee persistentInstance) {
		
	    getHibernateTemplate().update(persistentInstance);		
	}

	public void delete(BuEmployee persistentInstance) {
		log.debug("deleting BuEmployee instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuEmployee findById(java.lang.String id) {
		log.debug("getting BuEmployee instance with id: " + id);
		try {
			BuEmployee instance = (BuEmployee) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuEmployee", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuEmployee instance) {
		log.debug("finding BuEmployee instance by example");
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
		log.debug("finding BuEmployee instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuEmployee as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByAddressBookId(Object addressBookId) {
		return findByProperty(ADDRESS_BOOK_ID, addressBookId);
	}

	public List findByEmployeePosition(Object employeePosition) {
		return findByProperty(EMPLOYEE_POSITION, employeePosition);
	}

	public List findByLoginName(Object loginName) {
		return findByProperty(LOGIN_NAME, loginName);
	}

	public List findByTel1(Object tel1) {
		return findByProperty(TEL1, tel1);
	}

	public List findByFax1(Object fax1) {
		return findByProperty(FAX1, fax1);
	}

	public List findByEMail1(Object EMail1) {
		return findByProperty(_EMAIL1, EMail1);
	}

	public List findByTel2(Object tel2) {
		return findByProperty(TEL2, tel2);
	}

	public List findByFax2(Object fax2) {
		return findByProperty(FAX2, fax2);
	}

	public List findByEMail2(Object EMail2) {
		return findByProperty(_EMAIL2, EMail2);
	}

	public List findByTel3(Object tel3) {
		return findByProperty(TEL3, tel3);
	}

	public List findByFax3(Object fax3) {
		return findByProperty(FAX3, fax3);
	}

	public List findByEMail3(Object EMail3) {
		return findByProperty(_EMAIL3, EMail3);
	}

	public List findByTel4(Object tel4) {
		return findByProperty(TEL4, tel4);
	}

	public List findByFax4(Object fax4) {
		return findByProperty(FAX4, fax4);
	}

	public List findByEMail4(Object EMail4) {
		return findByProperty(_EMAIL4, EMail4);
	}

	public List findByCity1(Object city1) {
		return findByProperty(CITY1, city1);
	}

	public List findByArea1(Object area1) {
		return findByProperty(AREA1, area1);
	}

	public List findByZipCode1(Object zipCode1) {
		return findByProperty(ZIP_CODE1, zipCode1);
	}

	public List findByAddress1(Object address1) {
		return findByProperty(ADDRESS1, address1);
	}

	public List findByCity2(Object city2) {
		return findByProperty(CITY2, city2);
	}

	public List findByArea2(Object area2) {
		return findByProperty(AREA2, area2);
	}

	public List findByZipCode2(Object zipCode2) {
		return findByProperty(ZIP_CODE2, zipCode2);
	}

	public List findByAddress2(Object address2) {
		return findByProperty(ADDRESS2, address2);
	}

	public List findByCity3(Object city3) {
		return findByProperty(CITY3, city3);
	}

	public List findByArea3(Object area3) {
		return findByProperty(AREA3, area3);
	}

	public List findByZipCode3(Object zipCode3) {
		return findByProperty(ZIP_CODE3, zipCode3);
	}

	public List findByAddress3(Object address3) {
		return findByProperty(ADDRESS3, address3);
	}

	public List findByCity4(Object city4) {
		return findByProperty(CITY4, city4);
	}

	public List findByArea4(Object area4) {
		return findByProperty(AREA4, area4);
	}

	public List findByZipCode4(Object zipCode4) {
		return findByProperty(ZIP_CODE4, zipCode4);
	}

	public List findByAddress4(Object address4) {
		return findByProperty(ADDRESS4, address4);
	}

	public List findByRemark1(Object remark1) {
		return findByProperty(REMARK1, remark1);
	}

	public List findByRemark2(Object remark2) {
		return findByProperty(REMARK2, remark2);
	}

	public List findByRemark3(Object remark3) {
		return findByProperty(REMARK3, remark3);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuEmployee instances");
		try {
			String queryString = "from BuEmployee";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuEmployee merge(BuEmployee detachedInstance) {
		log.debug("merging BuEmployee instance");
		try {
			BuEmployee result = (BuEmployee) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuEmployee instance) {
		log.debug("attaching dirty BuEmployee instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuEmployee instance) {
		log.debug("attaching clean BuEmployee instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuEmployeeDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuEmployeeDAO) ctx.getBean("buEmployeeDAO");
	}
	
	public List findDuplicateByPropertyAndId(String propertyName, Object value, String id) {
		
	    String queryString = "from BuEmployee as model where model."
					+ propertyName + "= ? and model.employeeCode != ?";
	    return getHibernateTemplate().find(queryString, new Object[]{value, id});		
	}
	public List<BuEmployee> findByEmployeeCode(String employeeDepartment) {

		StringBuffer hql = new StringBuffer(
			"from BuEmployee as model where 1=1 ");
		//hql.append("and model.warehouseCode = some(");
		//hql.append("select employee.id.warehouseCode ");
		//hql.append("from BuEmployee as employee  where 1=1 ");
		hql.append("and id.employeeCode = ? )");

		System.out.println(hql.toString());

		List buEmployees = getHibernateTemplate().find(hql.toString(),
			(String) employeeDepartment);

		return buEmployees;

	    }
	
	public List<BuEmployee> findByemployee( HashMap findObjs ){
		final HashMap fos = findObjs;
		List<BuEmployee> re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from BuEmployee as model, BuAddressBook as model2 ");
				hql.append(" where model.addressBookId = model2.addressBookId");

			
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
	public List<BuEmployee> findByPropertyWorkers(String propertyName, Object value) {
	    log.debug("finding BuEmployee instance with property: "
			+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuEmployee as model where model."
				+ propertyName + "= ? and leaveDate = null and nvl(adReqHours,0)+nvl(adPrjHours,0)+nvl(adPreHours,0) > 0";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property id failed", re);
			throw re;
		}
	}
}