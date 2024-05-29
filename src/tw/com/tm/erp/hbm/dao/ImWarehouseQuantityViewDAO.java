package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImWarehouseQuantityView;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImWarehouseQuantityView entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImWarehouseQuantityView
 * @author MyEclipse Persistence Tools
 */

public class ImWarehouseQuantityViewDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(ImWarehouseQuantityViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(ImWarehouseQuantityView transientInstance) {
		log.debug("saving ImWarehouseQuantityView instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImWarehouseQuantityView persistentInstance) {
		log.debug("deleting ImWarehouseQuantityView instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImWarehouseQuantityView findById(java.lang.Long id) {
		log.debug("getting ImWarehouseQuantityView instance with id: " + id);
		try {
			ImWarehouseQuantityView instance = (ImWarehouseQuantityView) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImWarehouseQuantityView", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImWarehouseQuantityView instance) {
		log.debug("finding ImWarehouseQuantityView instance by example");
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
		log.debug("finding ImWarehouseQuantityView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImWarehouseQuantityView as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all ImWarehouseQuantityView instances");
		try {
			String queryString = "from ImWarehouseQuantityView";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImWarehouseQuantityView merge(
			ImWarehouseQuantityView detachedInstance) {
		log.debug("merging ImWarehouseQuantityView instance");
		try {
			ImWarehouseQuantityView result = (ImWarehouseQuantityView) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImWarehouseQuantityView instance) {
		log.debug("attaching dirty ImWarehouseQuantityView instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImWarehouseQuantityViewDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImWarehouseQuantityViewDAO) ctx
				.getBean("imWarehouseQuantityViewDAO");
	}
	/**
	 * 以 organizationId, locationId, itemId 為條件,查詢庫存產品相關資料
	 * @param organizationId is Organization Code
	 * @param locationId is Location ID
	 * @param itemId is Item Code
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findByOrganizationCodeAndLocationIdAndItemId(final String organizationCode,
			final long locationId, final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) {
				List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class)
					.add(Restrictions.eq("organizationCode", organizationCode))
					.add(Restrictions.eq("locationId", locationId))
					.add(Restrictions.eq("itemCode", itemCode))
					.addOrder(Order.asc("lotNo"))
					.list();
				return view;
			}
		});
		return list;
	}
	
	/**
	 * 以 organizationId, locationId, itemId 為條件,查詢庫存產品相關資料,<br>
	 * 並且 stockOnHandQty 需大於 uncommitQty (即有可用庫存)
	 * @param organizationId is Organization Code
	 * @param locationId is Location ID
	 * @param itemId is Item Code
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findAvailableQuantity(final String organizationCode,
			final long locationId, final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) {
				List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class)
					.add(Restrictions.eq("organizationCode", organizationCode))
					.add(Restrictions.eq("locationId", locationId))
					.add(Restrictions.eq("itemCode", itemCode))
					.add(Restrictions.gtProperty("stockOnHandQty", "uncommitQty"))
					.addOrder(Order.asc("lotNo"))
					.list();
				return view;
			}
		});
		return list;
	}
	
	/**
	 * 以  locationId, itemId 為條件,查詢庫存產品相關資料,<br>
	 * 並且 stockOnHandQty 需大於 uncommitQty (即有可用庫存)
	 * @param locationId is Location ID
	 * @param itemId is Item Code
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findAvailableQuantity(
			final long locationId, final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) {
				List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class)
					.add(Restrictions.eq("locationId", locationId))
					.add(Restrictions.eq("itemCode", itemCode))
					.add(Restrictions.gtProperty("stockOnHandQty", "uncommitQty"))
					.addOrder(Order.asc("lotNo"))
					.list();
				return view;
			}
		});
		return list;
	}
}