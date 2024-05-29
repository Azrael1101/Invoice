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
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuOrderType entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuOrderType
 * @author MyEclipse Persistence Tools
 */

public class BuOrderTypeDAO extends BaseDAO<BuOrderType> {
	private static final Log log = LogFactory.getLog(BuOrderTypeDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String SEQUENCE_TYPE = "sequenceType";
	public static final String TYPE_CODE = "typeCode";
	public static final String LAST_ORDER_NO = "lastOrderNo";
	public static final String ENABLE = "enable";
	public static final String TAX_CODE = "taxCode";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuOrderType transientInstance) {
		log.debug("saving BuOrderType instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public boolean update(BuOrderType transientInstance) {
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
	
	public void delete(BuOrderType persistentInstance) {
		log.debug("deleting BuOrderType instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuOrderType findById(tw.com.tm.erp.hbm.bean.BuOrderTypeId id) {
		log.debug("getting BuOrderType instance with id: " + id);
		try {
			BuOrderType instance = (BuOrderType) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuOrderType", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuOrderType instance) {
		log.debug("finding BuOrderType instance by example");
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
		log.debug("finding BuOrderType instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuOrderType as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 列出相同單據類別可用的單別<br>
	 * 
	 * @param brandCode
	 *            品牌代號
	 * @param typeCode
	 *            單據類別
	 * @return BuOrderType List
	 */
	public List<BuOrderType> findOrderbyType(String brandCode, String typeCode) {

		StringBuffer hql = new StringBuffer("from BuOrderType as model ");
		hql.append("where model.id.brandCode = ? ");
		//20090202 shan modify
		if( StringUtils.hasText(typeCode) )
			hql.append("and model.typeCode = ? ");
		hql.append("and model.enable = ? ");

		String[] param = null ;
		if( StringUtils.hasText(typeCode) )
			param = new String[] { brandCode, typeCode, "Y" } ;
		else
			param = new String[] { brandCode, "Y" } ;
		
		return getHibernateTemplate().find(hql.toString(),param	);

	}	
	
	/**
	 * 列出相同單據類別可用的單別
	 * @param brandCode
	 * @param orderTypeCode
	 * @param typeCode
	 * @param enable
	 * @return
	 */
	public List<BuOrderType> findOrderbyTypeCode(String brandCode, String orderTypeCode, String typeCode, String enable) {
		StringBuffer hql = new StringBuffer();
		
		if(StringUtils.hasText(orderTypeCode)){
			hql.append(" and id.orderTypeCode = ? ");
		}
		hql.append("and id.brandCode = ? and typeCode = ? and enable = ? ");
		
		Object[] param = null ;
		if( StringUtils.hasText(orderTypeCode) ){
			param = new Object[] { orderTypeCode,brandCode, typeCode, enable };
		}else{
			param = new Object[] { brandCode, typeCode, enable } ;
		}
		return findByProperty("BuOrderType", hql.toString(), param);
	}
	
	/**
	 * 列出相同單據與稅別類別可用的單別
	 * @param brandCode
	 * @param typeCode
	 * @return
	 */
	public List<BuOrderType> findOrderbyType(String brandCode, String typeCode, String taxType, String enable) {
		return findByProperty("BuOrderType", "and id.brandCode = ? and typeCode = ? and taxCode = ? and enable = ? "
				, new Object[]{ brandCode, typeCode, taxType, enable });
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findBySequenceType(Object sequenceType) {
		return findByProperty(SEQUENCE_TYPE, sequenceType);
	}

	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}

	public List findByLastOrderNo(Object lastOrderNo) {
		return findByProperty(LAST_ORDER_NO, lastOrderNo);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByTaxCode(Object taxCode) {
		return findByProperty(TAX_CODE, taxCode);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuOrderType instances");
		try {
			String queryString = "from BuOrderType";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuOrderType merge(BuOrderType detachedInstance) {
		log.debug("merging BuOrderType instance");
		try {
			BuOrderType result = (BuOrderType) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuOrderType instance) {
		log.debug("attaching dirty BuOrderType instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuOrderType instance) {
		log.debug("attaching clean BuOrderType instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuOrderTypeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuOrderTypeDAO) ctx.getBean("buOrderTypeDAO");
	}
	
	public List findOrderTypeManagerMail(final String brandCode, final String orderTypeCode) {

		List re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						
						
						StringBuffer hql = new StringBuffer(
								"Select address.EMail as EMail ");						
						hql.append("from BuOrderType as result, " );
						hql.append("BuEmployeeWithAddressView as address where 1=1 ");		
						hql.append("and result.id.brandCode = :brandCode ");
						hql.append("and result.id.orderTypeCode = :orderTypeCode ");
						hql.append("and result.reserve5 = address.employeeCode ");
						hql.append("and address.EMail != null ");
						hql.append("group by address.EMail ");
					
						Query query = session.createQuery(hql.toString());
						query.setParameter("brandCode", brandCode);
						query.setParameter("orderTypeCode", orderTypeCode);
						return query.list();
					}
				});

		return re;
	}
	
	/**
	 * 依據brandCode、ORDER_TYPE_CODE查詢，並進行鎖定
	 * @param warehouseCode
	 * @param brandCode
	 * @return List
	 * @author T96640
	 */
	public List<BuOrderType> getLockedOrderNo(final BuOrderTypeId id) {
		List<BuOrderType> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from BuOrderType as model ");
				hql.append(" and model.id.orderTypeCode = :orderTypeCode");
				hql.append(" and model.brandCode = :brandCode");

				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("orderTypeCode", id.getOrderTypeCode());
				query.setString("brandCode", id.getBrandCode());
								
				return query.list();
			}
		});

		return result;
	}
	
}