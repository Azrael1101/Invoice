package tw.com.tm.erp.hbm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuDeleteLog;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCountry entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCountry
 * @author MyEclipse Persistence Tools
 */

public class BuDeleteLogDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuDeleteLogDAO.class);

	// property constants
	public static final String CLEAN_TABLE = "cleanTable";

	public static final String COUNTRY_ENAME = "countryEName";

	public static final String DESCRIPTION = "description";

	public static final String ENABLE = "enable";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuDeleteLog buDeleteLog) {
		log.debug("saving BuDeleteLog instance");
		try {
			getHibernateTemplate().save(buDeleteLog);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(BuDeleteLog buDeleteLog) {
		try {
			getHibernateTemplate().update(buDeleteLog);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(BuDeleteLog buDeleteLog) {
		log.debug("deleting BuDeleteLog instance");
		try {
			getHibernateTemplate().delete(buDeleteLog);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuDeleteLog findById(java.lang.Long id) {
		log.debug("getting BuCountry instance with id: " + id);
		try {
			BuDeleteLog buDeleteLog = (BuDeleteLog) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuDeleteLog", id);
			return buDeleteLog;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuDeleteLog buDeleteLog) {
		log.debug("finding BuDeleteLog instance by example");
		try {
			List results = getHibernateTemplate().findByExample(buDeleteLog);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding BuDeleteLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuDeleteLog as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDeleteLogCleanTable(Object cleanTable) {
		return findByProperty(CLEAN_TABLE, cleanTable);
	}

	public List findByCountryEName(Object countryEName) {
		return findByProperty(COUNTRY_ENAME, countryEName);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all BuDeleteLog instances");
		try {
			String queryString = "from BuDeleteLog";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 依據啟用狀態查詢
	 * 
	 * @param isEnable
	 * @return List
	 */
	public List findBuDeleteLogByEnable(String isEnable) {
		
	    StringBuffer hql = new StringBuffer("from BuDeleteLog as model");
	    if (StringUtils.hasText(isEnable)){
                hql.append(" where model.enable = ?");
                getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
                return getHibernateTemplate().find(hql.toString(), new Object[]{isEnable});
	    }
	    
	    return getHibernateTemplate().find(hql.toString());		
	}
	
	public BuDeleteLog merge(BuDeleteLog detachedInstance) {
		log.debug("merging BuDeleteLog instance");
		try {
			BuDeleteLog result = (BuDeleteLog) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuDeleteLog instance) {
		log.debug("attaching dirty BuDeleteLog instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuDeleteLog instance) {
		log.debug("attaching clean BuDeleteLog instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuDeleteLogDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuDeleteLogDAO) ctx.getBean("buDeleteLogDAO");
	}
	
	/**
	 * 依照輸入條件來搜尋
	 * 
	 * @return List
	 */
	public String findCleanDate(String cleanTable) throws Exception {
		
		Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
    	String cleanDate = "";
    	try{
    		conn = dataSource.getConnection();
			log.info("conn success!!");
			
			StringBuffer sql = new StringBuffer();
			stmt = conn.createStatement();
			String creationDate  = "CREATION_DATE";
			sql.append("select MIN(model."+creationDate+") as creationDate from "+cleanTable+" model ");
			rs = stmt.executeQuery(sql.toString());
			
			while (rs.next()){
				cleanDate = DateUtils.format(rs.getDate("creationDate"), DateUtils.C_DATA_PATTON_YYYYMMDD);
			}
			
			stmt.close();
    		conn.close();
    		return cleanDate;
    	}catch(Exception e){
    		stmt.close();
    		conn.close();
    		return cleanDate;
    	}	    
	}
}