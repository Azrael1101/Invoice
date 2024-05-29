package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuVipPromote;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItemCurrentPriceView entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView
 * @author MyEclipse Persistence Tools
 */

public class ImItemCurrentPriceViewDAO extends BaseDAO {
    private static final Log log = LogFactory
	    .getLog(ImItemCurrentPriceViewDAO.class);

    // property constants

    protected void initDao() {
	// do nothing
    }

    public void save(ImItemCurrentPriceView transientInstance) {
	log.debug("saving ImItemCurrentPriceView instance");
	try {
	    getHibernateTemplate().save(transientInstance);
	    log.debug("save successful");
	} catch (RuntimeException re) {
	    log.error("save failed", re);
	    throw re;
	}
    }

    public void delete(ImItemCurrentPriceView persistentInstance) {
	log.debug("deleting ImItemCurrentPriceView instance");
	try {
	    getHibernateTemplate().delete(persistentInstance);
	    log.debug("delete successful");
	} catch (RuntimeException re) {
	    log.error("delete failed", re);
	    throw re;
	}
    }

    public ImItemCurrentPriceView findById(Long id) {
	log.debug("getting ImItemCurrentPriceView instance with id: " + id);
	try {
	    ImItemCurrentPriceView instance = (ImItemCurrentPriceView) getHibernateTemplate()
		    .get("tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView", id);
	    return instance;
	} catch (RuntimeException re) {
	    log.error("get failed", re);
	    throw re;
	}
    }

    public List findByExample(ImItemCurrentPriceView instance) {
	log.debug("finding ImItemCurrentPriceView instance by example");
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
	log.debug("finding ImItemCurrentPriceView instance with property: "
		+ propertyName + ", value: " + value);
	try {
	    String queryString = "from ImItemCurrentPriceView as model where model."
		    + propertyName + "= ?";
	    return getHibernateTemplate().find(queryString, value);
	} catch (RuntimeException re) {
	    log.error("find by property name failed", re);
	    throw re;
	}
    }

    public List findAll() {
	log.debug("finding all ImItemCurrentPriceView instances");
	try {
	    String queryString = "from ImItemCurrentPriceView";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    public ImItemCurrentPriceView merge(ImItemCurrentPriceView detachedInstance) {
	log.debug("merging ImItemCurrentPriceView instance");
	try {
	    ImItemCurrentPriceView result = (ImItemCurrentPriceView) getHibernateTemplate()
		    .merge(detachedInstance);
	    log.debug("merge successful");
	    return result;
	} catch (RuntimeException re) {
	    log.error("merge failed", re);
	    throw re;
	}
    }

    public void attachDirty(ImItemCurrentPriceView instance) {
	log.debug("attaching dirty ImItemCurrentPriceView instance");
	try {
	    getHibernateTemplate().saveOrUpdate(instance);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public void attachClean(ImItemCurrentPriceView instance) {
	log.debug("attaching clean ImItemCurrentPriceView instance");
	try {
	    getHibernateTemplate().lock(instance, LockMode.NONE);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public ImItemCurrentPriceView findCurrentPriceByValue(String brandCode,
	    String itemCode, String priceType) {
	try {

	    StringBuffer hql = new StringBuffer(
		    "from ImItemCurrentPriceView as model ");
	    hql.append("where model.brandCode = '" + brandCode + "' ");
	    hql.append("and model.itemCode = '"
		    + itemCode.toUpperCase() + "' ");
	    hql.append("and model.typeCode = '" + priceType + "' ");

	    getHibernateTemplate().setMaxResults(
		    SystemConfig.SEARCH_PAGE_MAX_COUNT);
	    List<ImItemCurrentPriceView> lists = getHibernateTemplate().find(
		    hql.toString());
	    if (lists.size() > 0) {
		return lists.get(0);
	    } else {
		return null;
	    }

	} catch (RuntimeException re) {
	    throw re;
	}
    }
    
    public ImItemCurrentPriceView findCurrentPrice(String brandCode,
    	    String itemCode, String priceType) {
    	try {

    	    StringBuffer hql = new StringBuffer(
    		    "from ImItemCurrentPriceView as model ");
    	    hql.append("where model.brandCode = '" + brandCode + "' ");
    	    hql.append("and model.itemCode = '"
    		    + itemCode.toUpperCase() + "' ");
    	    hql.append("and model.typeCode = '" + priceType + "' ");

    	    getHibernateTemplate().setMaxResults(
    		    SystemConfig.SEARCH_PAGE_MAX_COUNT);
    	    List<ImItemCurrentPriceView> lists = getHibernateTemplate().find(
    		    hql.toString());
    	    if (lists.size() > 0) {
    		return lists.get(0);
    	    } else {
    		return null;
    	    }

    	} catch (RuntimeException re) {
    	    throw re;
    	}
        }
    
    public List<BuVipPromote> findBuVipPromote(String CustomerCode) {
    	try {

    	    StringBuffer hql = new StringBuffer(
    		    "from BuVipPromote as model ");
    	    hql.append("where model.customerCode = '" + CustomerCode + "' ");
    	    getHibernateTemplate().setMaxResults(
    		    SystemConfig.SEARCH_PAGE_MAX_COUNT);
    	    List<BuVipPromote> lists = getHibernateTemplate().find(
    		    hql.toString());
    	    if (lists.size() > 0) {
    		return lists;
    	    } else {
    		return null;
    	    }

    	} catch (RuntimeException re) {
    	    throw re;
    	}
        }
    
    
    /**
     * 
     * @param brandCode
     * @param itemCode
     * @param priceType
     * @return List<ImItemCurrentPriceView>
     */
    public List<ImItemCurrentPriceView> findCurrentPriceList(String brandCode,
    	    String itemCodes, String priceType) {
    	try {

    	    StringBuffer hql = new StringBuffer(
    		    "from ImItemCurrentPriceView as model ");
    	    hql.append("where model.brandCode = '" + brandCode + "' ");
    	    hql.append("and upper(model.itemCode) in ("
    		    + itemCodes + ") ");
    	    hql.append("and model.typeCode = '" + priceType + "' ");

    	    List<ImItemCurrentPriceView> lists = getHibernateTemplate().find(
    		    hql.toString());
    	    return lists;

    	} catch (RuntimeException re) {
    	    throw re;
    	}
        }
    public static ImItemCurrentPriceViewDAO getFromApplicationContext(
	    ApplicationContext ctx) {
	return (ImItemCurrentPriceViewDAO) ctx
		.getBean("imItemCurrentPriceViewDAO");
    }

    public List<ImItemCurrentPriceView> findCurrentPriceByValue(
	    String brandCode, String startItemCode, String endItemCode,
	    String priceType, String categorySearchString) {
	try {

	    StringBuffer hql = new StringBuffer(
		    "from ImItemCurrentPriceView as model ");
	    hql.append("where model.brandCode = '" + brandCode + "' ");

	    // 判斷針對ItemCode欄位是否要用模糊查詢(LIKE)的進行Query
	    // 當startItemCode或endItemCode有一參數為空白或Null時，即使用模糊查詢
	    if ((startItemCode == null || "".equals(startItemCode))
		    || (endItemCode == null || "".equals(endItemCode))) {
		hql
			.append((StringUtils.hasText(startItemCode) ? "and model.itemCode LIKE '%"
				+ startItemCode.toUpperCase() + "%' "
				: ""));
		hql
			.append((StringUtils.hasText(endItemCode) ? "and model.itemCode LIKE '%"
				+ endItemCode.toUpperCase() + "%' "
				: ""));
	    } else {
		hql
			.append((StringUtils.hasText(startItemCode) ? "and model.itemCode >= '"
				+ startItemCode.toUpperCase() + "' "
				: ""));
		hql
			.append((StringUtils.hasText(endItemCode) ? "and model.itemCode <= '"
				+ endItemCode.toUpperCase() + "' "
				: ""));
	    }
	    // ----價格類別
	    hql
		    .append((StringUtils.hasText(priceType) ? "and model.typeCode = '"
			    + priceType + "' "
			    : ""));

	    // ----類別組合字串
	    hql.append(StringUtils.hasText(categorySearchString) ? "and "
		    + categorySearchString : "");
	    getHibernateTemplate().setMaxResults(
		    SystemConfig.SEARCH_PAGE_MAX_COUNT);
	    System.out.println(hql.toString());
	    System.out
		    .println("start:"
			    + tw.com.tm.erp.utils.DateUtils
				    .getCurrentDateStr(tw.com.tm.erp.utils.DateUtils.C_TIME_PATTON_DEFAULT));

	    List<ImItemCurrentPriceView> imItemCurrentPriceView = getHibernateTemplate()
		    .find(hql.toString());
	    System.out
		    .println("end:"
			    + tw.com.tm.erp.utils.DateUtils
				    .getCurrentDateStr(tw.com.tm.erp.utils.DateUtils.C_TIME_PATTON_DEFAULT));
	    return imItemCurrentPriceView;

	} catch (RuntimeException re) {
	    throw re;
	}
    }

    public List<ImItemCurrentPriceView> findCurrentPriceByProperty(String brandCode, String priceType) {

	StringBuffer hql = new StringBuffer(
		"from ImItemCurrentPriceView as model ");
	hql.append("where model.brandCode = '" + brandCode + "' ");
	hql.append("and model.typeCode = '" + priceType + "' ");

	getHibernateTemplate()
		.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
	List<ImItemCurrentPriceView> lists = getHibernateTemplate().find(
		hql.toString());
	return lists;
    }
    
    /**
     * 依品牌,價格類別,商品條件,撈出一筆
     * @param brandCode
     * @param typeCode
     * @param itemCode
     * @return
     */
    public ImItemCurrentPriceView findOneCurrentPriceByProperty(String brandCode, String typeCode, String itemCode) {
    	return (ImItemCurrentPriceView) findFirstByProperty("ImItemCurrentPriceView", "and brandCode = ? and typeCode = ? and itemCode = ?", new Object[]{brandCode, typeCode, itemCode});
    }

    /**
     * 依品牌,稅別,商品條件,撈出一筆
     * @param brandCode
     * @param typeCode
     * @param itemCode
     * @return
     */
    public ImItemCurrentPriceView findOneCurrentPrice(String brandCode,String isTax, String itemCode) {
    	return (ImItemCurrentPriceView) findFirstByProperty("ImItemCurrentPriceView", "and brandCode = ? and isTax = ? and itemCode = ?", new Object[]{brandCode, isTax, itemCode});
    }
}