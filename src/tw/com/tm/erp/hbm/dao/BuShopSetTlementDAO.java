package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuShopSetTlement;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuShopSetTlement entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuShopSetTlement
 * @author MyEclipse Persistence Tools
 */

public class BuShopSetTlementDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuShopSetTlementDAO.class);

    protected void initDao() {
	// do nothing
    }

    public void save(BuShopSetTlement transientInstance) {
	log.debug("saving BuShopSetTlement instance");
	try {
	    getHibernateTemplate().save(transientInstance);
	    log.debug("save successful");
	} catch (RuntimeException re) {
	    log.error("save failed", re);
	    throw re;
	}
    }
    public boolean update(BuShopSetTlement transientInstance) {
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
    public void delete(BuShopSetTlement persistentInstance) {
	log.debug("deleting BuShopSetTlement instance");
	try {
	    getHibernateTemplate().delete(persistentInstance);
	    log.debug("delete successful");
	} catch (RuntimeException re) {
	    log.error("delete failed", re);
	    throw re;
	}
    }

    public BuShopSetTlement findById(java.lang.Long id) {
	log.debug("getting BuShopSetTlement instance with id: " + id);
	try {
	    BuShopSetTlement instance = (BuShopSetTlement) getHibernateTemplate()
	    .get("tw.com.tm.erp.hbm.bean.BuShopSetTlement", id);
	    return instance;
	} catch (RuntimeException re) {
	    log.error("get failed", re);
	    throw re;
	}
    }

    public List findByExample(BuShopSetTlement instance) {
	log.debug("finding BuShopSetTlement instance by example");
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
	log.debug("finding BuShopSetTlement instance with property: "
		+ propertyName + ", value: " + value);
	try {
	    String queryString = "from BuShopSetTlement as model where model."
		+ propertyName + "= ?";
	    return getHibernateTemplate().find(queryString, value);
	} catch (RuntimeException re) {
	    log.error("find by property name failed", re);
	    throw re;
	}
    }



    public List findAll() {
	log.debug("finding all BuShopSetTlement instances");
	try {
	    String queryString = "from BuShopSetTlement";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }



    public static BuShopSetTlementDAO getFromApplicationContext(
	    ApplicationContext ctx) {
	return (BuShopSetTlementDAO) ctx.getBean("buShopSettlementDAO");
    }

    public  List<BuShopSetTlement>  findBuShopSetTlementByValue(HashMap findObjs) {
	final HashMap fos = findObjs ; 
	List<BuShopSetTlement> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)
	    throws HibernateException, SQLException {

		StringBuffer hql = new StringBuffer(
			"from BuShopSetTlement as model where 1=1 ");

		// 專櫃代號
		if (StringUtils.hasText((String)fos.get("shopCode"))) 
		    hql.append(" and model.shopCode = :shopCode");	

		// ----更新日期

		if ((java.util.Date)fos.get("startDate")!= null) 
		    hql.append(" and model.lastUpdateDate >= :startDate");	

		if ((java.util.Date)fos.get("endDate")!= null) 
		    hql.append(" and model.lastUpdateDate <= :endDate");	

		hql.append(" ORDER BY model.shopCode ");	

		Query query = session.createQuery(hql.toString());

		// 狀態
		if (StringUtils.hasText((String)fos.get("status"))) 
		    query.setString("status", (String)fos.get("status") );	

		// 專櫃代號
		if (StringUtils.hasText((String)fos.get("shopCode"))) 
		    query.setString("shopCode", (String)fos.get("shopCode") );	

		// ----更新日期

		if ((java.util.Date)fos.get("startDate")!= null) {		
		    query.setDate("startDate", DateUtils.parseDateTime( DateUtils.format((java.util.Date)fos.get("startDate"))+" 00:00:00") );	
		}	
		if ((java.util.Date)fos.get("endDate")!= null){
		    query.setDate("endDate", DateUtils.addDate(DateUtils.format((java.util.Date)fos.get("endDate"))) );
		}

		return query.list();

	    }
	});

	return re;		
    }
    public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize) {
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	System.out.println("start to find BuShopSetTlement....");
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)throws HibernateException, SQLException {

		StringBuffer hql = new StringBuffer("");
		
		// 專櫃代號
		if (StringUtils.hasText((String)fos.get("shopCode"))) 
		    hql.append(" and model.shopCode = :shopCode");
		
		if( startRecordIndexStar >= 0 ) {
		    hql.append("from BuShopSetTlement as model where 1=1 ");
		}else{
		    hql.append("select count(model.lineId) as rowCount from BuShopSetTlement as model where 1=1 ");
		}		


//		hql.append(" order by lastUpdateDate desc ");

		System.out.println(hql.toString());
		Query query = session.createQuery(hql.toString());
		if( startRecordIndexStar >= 0 ) {
		    query.setFirstResult(startRecordIndexStar);
		    query.setMaxResults(pSize);
		    System.out.println("startRecordIndexStar:"+startRecordIndexStar);
		    System.out.println("pSize:"+pSize);
		}

		if (StringUtils.hasText((String)fos.get("shopCode"))) 
		    query.setString("shopCode", (String) fos.get("shopCode"));
		
		return query.list();
	    }
	});

	log.info("BuShopSetTlement.form:"+result.size());
	HashMap returnResult = new HashMap();		
	returnResult.put("form", startRecordIndexStar >= 0 ? result: null);			
	if(result.size() == 0){
	    returnResult.put("recordCount", 0L);
	}else{
	    log.info("BuShopSetTlement.size:"+result.get(0));
	    returnResult.put("recordCount",startRecordIndexStar >= 0? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
    }
}