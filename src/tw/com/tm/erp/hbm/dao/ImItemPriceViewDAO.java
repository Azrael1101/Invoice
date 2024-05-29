package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItemPriceView entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItemPriceView
 * @author MyEclipse Persistence Tools
 */

public class ImItemPriceViewDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImItemPriceViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(ImItemPriceView transientInstance) {
		log.debug("saving ImItemPriceView instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ImItemPriceView persistentInstance) {
		log.debug("deleting ImItemPriceView instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImItemPriceView findById(java.lang.Long id) {
		log.debug("getting ImItemPriceView instance with id: " + id);
		try {
			ImItemPriceView instance = (ImItemPriceView) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.ImItemPriceView", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImItemPriceView instance) {
		log.debug("finding ImItemPriceView instance by example");
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
		log.debug("finding ImItemPriceView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ImItemPriceView as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all ImItemPriceView instances");
		try {
			String queryString = "from ImItemPriceView";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImItemPriceView merge(ImItemPriceView detachedInstance) {
		log.debug("merging ImItemPriceView instance");
		try {
			ImItemPriceView result = (ImItemPriceView) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImItemPriceView instance) {
		log.debug("attaching dirty ImItemPriceView instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImItemPriceView instance) {
		log.debug("attaching clean ImItemPriceView instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImItemPriceViewDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImItemPriceViewDAO) ctx.getBean("imItemPriceViewDAO");
	}
	
	public List<ImItemPriceView> findPriceViewByValue(String brandCode,
			String startItemCode, String endItemCode, String startDate,
			String endDate, String priceType, String priceEnable,
			String employeeCode, String categorySearchString) {
		try {

			StringBuffer hql = new StringBuffer(
					"from ImItemPriceView as model ");
			hql.append("where model.brandCode = '" + brandCode + "' ");

			// 判斷針對ItemCode欄位是否要用模糊查詢(LIKE)的進行Query
			// 當startItemCode或endItemCode有一參數為空白或Null時，即使用模糊查詢
			if ((startItemCode == null || "".equals(startItemCode))
					|| (endItemCode == null || "".equals(endItemCode))) {
				hql
						.append((startItemCode != null
								&& !"".equals(startItemCode) ? "and model.itemCode LIKE '%"
								+ startItemCode.toUpperCase() + "%' "
								: ""));
				hql
						.append((endItemCode != null && !"".equals(endItemCode) ? "and model.itemCode LIKE '%"
								+ endItemCode.toUpperCase() + "%' "
								: ""));
			} else {
				hql
						.append((startItemCode != null
								&& !"".equals(startItemCode) ? "and model.itemCode >= '"
								+ startItemCode.toUpperCase() + "' "
								: ""));
				hql
						.append((endItemCode != null && !"".equals(endItemCode) ? "and model.itemCode <= '"
								+ endItemCode.toUpperCase() + "' "
								: ""));
			}
			// ----價格類別
			hql
					.append((priceType != null && !"".equals(priceType) ? "and model.typeCode = '"
							+ priceType + "' "
							: ""));
			// ----價格狀態
			hql
					.append((priceEnable != null && !"".equals(priceEnable) ? "and model.priceEnable = '"
							+ priceEnable + "' "
							: ""));
			hql
					.append((employeeCode != null && !("".equals(employeeCode)) ? "and model.lastUpdatedBy = '"
							+ employeeCode + "' "
							: ""));
			// ----更新日期
			hql
					.append((startDate != null && !"".equals(startDate) ? "and model.lastUpdateDate between "
							+ "TO_DATE('"
							+ startDate
							+ "', 'YYYY/MM/DD') and "
							+ "TO_DATE('" + endDate + "', 'YYYY/MM/DD') "
							: ""));
			// ----類別組合字串
			hql
					.append((categorySearchString != null
							&& !"".equals(categorySearchString.trim()) ? categorySearchString
							: ""));
			System.out.println(hql.toString());
			//getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			getHibernateTemplate().setMaxResults(1500);
			return getHibernateTemplate().find(
					hql.toString());

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依品牌,價格類別,商品,價格enable,商品enable撈出一筆
	 * @param brandCode
	 * @param typeCode
	 * @param priceEnable
	 * @param itemEnable
	 * @return
	 */
	public ImItemPriceView findOneItemPriceView(String brandCode, String typeCode, String itemCode, String priceEnable, String itemEnable){
		return (ImItemPriceView)findFirstByProperty("ImItemPriceView", "and brandCode = ? and typeCode = ? and itemCode = ? and priceEnable = ? and itemEnable = ? ", new Object[]{ brandCode, typeCode, itemCode, priceEnable, itemEnable});
	}
	public ImItemPriceView findOneItemPriceView(String brandCode, String typeCode, String itemCode, String priceEnable, String itemEnable,String today){
		log.info(brandCode + typeCode + priceEnable + itemEnable + today);
				String fileName = "and brandCode = ? and typeCode = ? and itemCode = ? and priceEnable = ? and itemEnable = ? and beginDate <= to_date("+today+",'YYYYMMDD') ";
				return (ImItemPriceView)findFirstByProperty("ImItemPriceView","", fileName, new Object[]{ brandCode, typeCode, itemCode, priceEnable, itemEnable},"order by beginDate desc");
			}
	/**
	 * 依品牌,價格類別,商品撈出一筆
	 * @param brandCode
	 * @param typeCode
	 * @return
	 */
	public ImItemPriceView findOneItemPriceView(String brandCode, String typeCode, String itemCode){
		return (ImItemPriceView)findOneItemPriceView(brandCode, typeCode, itemCode, "N", "Y");
	}
}