package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
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

import tw.com.tm.erp.hbm.bean.BuOrderTypeApproval;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuOrderTypeApproval entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuOrderTypeApproval
 * @author MyEclipse Persistence Tools
 */

public class BuOrderTypeApprovalDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(BuOrderTypeApprovalDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String ROLE_APPROVE_LEVEL01 = "roleApproveLevel01";
	public static final String ROLE_APPROVE_LEVEL02 = "roleApproveLevel02";
	public static final String ROLE_APPROVE_LEVEL03 = "roleApproveLevel03";
	public static final String ROLE_APPROVE_LEVEL04 = "roleApproveLevel04";
	public static final String ROLE_APPROVE_LEVEL05 = "roleApproveLevel05";
	public static final String ROLE_APPROVE_LEVEL06 = "roleApproveLevel06";
	public static final String ROLE_APPROVE_LEVEL07 = "roleApproveLevel07";
	public static final String ROLE_APPROVE_LEVEL08 = "roleApproveLevel08";
	public static final String ROLE_APPROVE_LEVEL09 = "roleApproveLevel09";
	public static final String ROLE_APPROVE_LEVEL10 = "roleApproveLevel10";
	public static final String ROLE_APPROVE_LEVEL11 = "roleApproveLevel11";
	public static final String ROLE_APPROVE_LEVEL12 = "roleApproveLevel12";
	public static final String ROLE_APPROVE_LEVEL13 = "roleApproveLevel13";
	public static final String ROLE_APPROVE_LEVEL14 = "roleApproveLevel14";
	public static final String ROLE_APPROVE_LEVEL15 = "roleApproveLevel15";
	public static final String ROLE_APPROVE_LEVEL16 = "roleApproveLevel16";
	public static final String ROLE_APPROVE_LEVEL17 = "roleApproveLevel17";
	public static final String ROLE_APPROVE_LEVEL18 = "roleApproveLevel18";
	public static final String ROLE_APPROVE_LEVEL19 = "roleApproveLevel19";
	public static final String ROLE_APPROVE_LEVEL20 = "roleApproveLevel20";
	public static final String RESERVE1 = "reserve1";
	public static final String RESERVE2 = "reserve2";
	public static final String RESERVE3 = "reserve3";
	public static final String RESERVE4 = "reserve4";
	public static final String RESERVE5 = "reserve5";
	public static final String INDEX_NO = "indexNo";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuOrderTypeApproval transientInstance) {
		log.debug("saving BuOrderTypeApproval instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public boolean update(BuOrderTypeApproval transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			// throw re;
			result = false;
		}
		return result;
	}

	public void delete(BuOrderTypeApproval persistentInstance) {
		log.debug("deleting BuOrderTypeApproval instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuOrderTypeApproval findById(java.lang.String id) {
		log.debug("getting BuOrderTypeApproval instance with id: " + id);
		try {
			BuOrderTypeApproval instance = (BuOrderTypeApproval) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuOrderTypeApproval", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuOrderTypeApproval instance) {
		log.debug("finding BuOrderTypeApproval instance by example");
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
		log.debug("finding BuOrderTypeApproval instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuOrderTypeApproval as model where model."
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

	public List findByRoleApproveLevel01(Object roleApproveLevel01) {
		return findByProperty(ROLE_APPROVE_LEVEL01, roleApproveLevel01);
	}

	public List findByRoleApproveLevel02(Object roleApproveLevel02) {
		return findByProperty(ROLE_APPROVE_LEVEL02, roleApproveLevel02);
	}

	public List findByRoleApproveLevel03(Object roleApproveLevel03) {
		return findByProperty(ROLE_APPROVE_LEVEL03, roleApproveLevel03);
	}

	public List findByRoleApproveLevel04(Object roleApproveLevel04) {
		return findByProperty(ROLE_APPROVE_LEVEL04, roleApproveLevel04);
	}

	public List findByRoleApproveLevel05(Object roleApproveLevel05) {
		return findByProperty(ROLE_APPROVE_LEVEL05, roleApproveLevel05);
	}

	public List findByRoleApproveLevel06(Object roleApproveLevel06) {
		return findByProperty(ROLE_APPROVE_LEVEL06, roleApproveLevel06);
	}

	public List findByRoleApproveLevel07(Object roleApproveLevel07) {
		return findByProperty(ROLE_APPROVE_LEVEL07, roleApproveLevel07);
	}

	public List findByRoleApproveLevel08(Object roleApproveLevel08) {
		return findByProperty(ROLE_APPROVE_LEVEL08, roleApproveLevel08);
	}

	public List findByRoleApproveLevel09(Object roleApproveLevel09) {
		return findByProperty(ROLE_APPROVE_LEVEL09, roleApproveLevel09);
	}

	public List findByRoleApproveLevel10(Object roleApproveLevel10) {
		return findByProperty(ROLE_APPROVE_LEVEL10, roleApproveLevel10);
	}

	public List findByRoleApproveLevel11(Object roleApproveLevel11) {
		return findByProperty(ROLE_APPROVE_LEVEL11, roleApproveLevel11);
	}

	public List findByRoleApproveLevel12(Object roleApproveLevel12) {
		return findByProperty(ROLE_APPROVE_LEVEL12, roleApproveLevel12);
	}

	public List findByRoleApproveLevel13(Object roleApproveLevel13) {
		return findByProperty(ROLE_APPROVE_LEVEL13, roleApproveLevel13);
	}

	public List findByRoleApproveLevel14(Object roleApproveLevel14) {
		return findByProperty(ROLE_APPROVE_LEVEL14, roleApproveLevel14);
	}

	public List findByRoleApproveLevel15(Object roleApproveLevel15) {
		return findByProperty(ROLE_APPROVE_LEVEL15, roleApproveLevel15);
	}

	public List findByRoleApproveLevel16(Object roleApproveLevel16) {
		return findByProperty(ROLE_APPROVE_LEVEL16, roleApproveLevel16);
	}

	public List findByRoleApproveLevel17(Object roleApproveLevel17) {
		return findByProperty(ROLE_APPROVE_LEVEL17, roleApproveLevel17);
	}

	public List findByRoleApproveLevel18(Object roleApproveLevel18) {
		return findByProperty(ROLE_APPROVE_LEVEL18, roleApproveLevel18);
	}

	public List findByRoleApproveLevel19(Object roleApproveLevel19) {
		return findByProperty(ROLE_APPROVE_LEVEL19, roleApproveLevel19);
	}

	public List findByRoleApproveLevel20(Object roleApproveLevel20) {
		return findByProperty(ROLE_APPROVE_LEVEL20, roleApproveLevel20);
	}

	public List findByReserve1(Object reserve1) {
		return findByProperty(RESERVE1, reserve1);
	}

	public List findByReserve2(Object reserve2) {
		return findByProperty(RESERVE2, reserve2);
	}

	public List findByReserve3(Object reserve3) {
		return findByProperty(RESERVE3, reserve3);
	}

	public List findByReserve4(Object reserve4) {
		return findByProperty(RESERVE4, reserve4);
	}

	public List findByReserve5(Object reserve5) {
		return findByProperty(RESERVE5, reserve5);
	}

	public List findByIndexNo(Object indexNo) {
		return findByProperty(INDEX_NO, indexNo);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuOrderTypeApproval instances");
		try {
			String queryString = "from BuOrderTypeApproval";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuOrderTypeApproval merge(BuOrderTypeApproval detachedInstance) {
		log.debug("merging BuOrderTypeApproval instance");
		try {
			BuOrderTypeApproval result = (BuOrderTypeApproval) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuOrderTypeApproval instance) {
		log.debug("attaching dirty BuOrderTypeApproval instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuOrderTypeApproval instance) {
		log.debug("attaching clean BuOrderTypeApproval instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuOrderTypeApprovalDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuOrderTypeApprovalDAO) ctx.getBean("buOrderTypeApprovalDAO");
	}

	public List<BuOrderTypeApproval> findApprovalLevel(String brandCode,
			String orderTypeCode, final String approvalLevelCode) {
	
		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"from BuOrderTypeApproval as model ");
				hql.append("and model.id.brandCode = :brandCode ");
				hql.append("and model.id.orderTypeCode = :orderTypeCode ");
				hql.append("and model.code =:approvalLevelCode ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("brandCode", approvalLevelCode);
				query.setParameter("orderTypeCode", approvalLevelCode);
				query.setParameter("approvalLevelCode", approvalLevelCode);

				return query.list();

			}
		});

		return re;		
	}

}