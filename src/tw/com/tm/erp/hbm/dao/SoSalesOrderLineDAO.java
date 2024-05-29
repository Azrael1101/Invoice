package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.SoSalesOrderLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * SoSalesOrderLine entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.SoSalesOrderLine
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderLineDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SoSalesOrderLineDAO.class);

	// property constants
	public static final String LINE_NO = "lineNo";

	public static final String SHIPMENT_NO = "shipmentNo";

	public static final String ITEM_ID = "itemId";

	public static final String QUANTITY = "quantity";

	public static final String WAREHOUSE_ID = "warehouseId";

	public static final String DISCOUNT_RATE = "discountRate";

	public static final String LOCAL_CURRENCY_CODE = "localCurrencyCode";

	public static final String LOCAL_UNIT_PRICE = "localUnitPrice";

	public static final String FOREIGN_CURRENCY_CODE = "foreignCurrencyCode";

	public static final String FOREIGN_UNIT_PRICE = "foreignUnitPrice";

	public static final String IS_TAX = "isTax";

	public static final String TAX_CODE = "taxCode";

	public static final String ACTIVITY_ID = "activityId";

	public static final String DEPOSIT_ID = "depositId";

	public static final String IS_USE_DEPOSIT = "isUseDeposit";

	public static final String WATCH_SERIAL_NO = "watchSerialNo";

	public static final String ATTRIBUTE1 = "attribute1";

	public static final String ATTRIBUTE2 = "attribute2";

	public static final String ATTRIBUTE3 = "attribute3";

	public static final String ATTRIBUTE4 = "attribute4";

	public static final String ATTRIBUTE5 = "attribute5";

	public static final String STATUS = "status";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(SoSalesOrderLine transientInstance) {
		log.debug("saving SoSalesOrderLine instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(SoSalesOrderLine line) {
		try {
			getHibernateTemplate().saveOrUpdate(line);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(SoSalesOrderLine persistentInstance) {
		log.debug("deleting SoSalesOrderLine instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SoSalesOrderLine findById(java.lang.Long id) {
		log.debug("getting SoSalesOrderLine instance with id: " + id);
		try {
			SoSalesOrderLine instance = (SoSalesOrderLine) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.SoSalesOrderLine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SoSalesOrderLine instance) {
		log.debug("finding SoSalesOrderLine instance by example");
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
		log.debug("finding SoSalesOrderLine instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SoSalesOrderLine as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLineNo(Object lineNo) {
		return findByProperty(LINE_NO, lineNo);
	}

	public List findByShipmentNo(Object shipmentNo) {
		return findByProperty(SHIPMENT_NO, shipmentNo);
	}

	public List findByItemId(Object itemId) {
		return findByProperty(ITEM_ID, itemId);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByWarehouseId(Object warehouseId) {
		return findByProperty(WAREHOUSE_ID, warehouseId);
	}

	public List findByDiscountRate(Object discountRate) {
		return findByProperty(DISCOUNT_RATE, discountRate);
	}

	public List findByLocalCurrencyCode(Object localCurrencyCode) {
		return findByProperty(LOCAL_CURRENCY_CODE, localCurrencyCode);
	}

	public List findByLocalUnitPrice(Object localUnitPrice) {
		return findByProperty(LOCAL_UNIT_PRICE, localUnitPrice);
	}

	public List findByForeignCurrencyCode(Object foreignCurrencyCode) {
		return findByProperty(FOREIGN_CURRENCY_CODE, foreignCurrencyCode);
	}

	public List findByForeignUnitPrice(Object foreignUnitPrice) {
		return findByProperty(FOREIGN_UNIT_PRICE, foreignUnitPrice);
	}

	public List findByIsTax(Object isTax) {
		return findByProperty(IS_TAX, isTax);
	}

	public List findByTaxCode(Object taxCode) {
		return findByProperty(TAX_CODE, taxCode);
	}

	public List findByActivityId(Object activityId) {
		return findByProperty(ACTIVITY_ID, activityId);
	}

	public List findByDepositId(Object depositId) {
		return findByProperty(DEPOSIT_ID, depositId);
	}

	public List findByIsUseDeposit(Object isUseDeposit) {
		return findByProperty(IS_USE_DEPOSIT, isUseDeposit);
	}

	public List findByWatchSerialNo(Object watchSerialNo) {
		return findByProperty(WATCH_SERIAL_NO, watchSerialNo);
	}

	public List findByAttribute1(Object attribute1) {
		return findByProperty(ATTRIBUTE1, attribute1);
	}

	public List findByAttribute2(Object attribute2) {
		return findByProperty(ATTRIBUTE2, attribute2);
	}

	public List findByAttribute3(Object attribute3) {
		return findByProperty(ATTRIBUTE3, attribute3);
	}

	public List findByAttribute4(Object attribute4) {
		return findByProperty(ATTRIBUTE4, attribute4);
	}

	public List findByAttribute5(Object attribute5) {
		return findByProperty(ATTRIBUTE5, attribute5);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all SoSalesOrderLine instances");
		try {
			String queryString = "from SoSalesOrderLine";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SoSalesOrderLine merge(SoSalesOrderLine detachedInstance) {
		log.debug("merging SoSalesOrderLine instance");
		try {
			SoSalesOrderLine result = (SoSalesOrderLine) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SoSalesOrderLine instance) {
		log.debug("attaching dirty SoSalesOrderLine instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SoSalesOrderLine instance) {
		log.debug("attaching clean SoSalesOrderLine instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SoSalesOrderLineDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (SoSalesOrderLineDAO) ctx.getBean("soSalesOrderLineDAO");
	}
}