package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
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

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuBranch;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuBrand entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuBrand
 * @author MyEclipse Persistence Tools
 */

public class BuBrandDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuBrandDAO.class);
	// property constants
	public static final String BRANCH_CODE = "branchCode";
	public static final String BRAND_NAME = "brandName";
	public static final String DESCRIPTION = "description";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuBrand transientInstance) {
		log.debug("saving BuBrand instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuBrand persistentInstance) {
		log.debug("deleting BuBrand instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuBrand findById(java.lang.String id) {
		log.debug("getting BuBrand instance with id: " + id);
		try {
			BuBrand instance = (BuBrand) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuBrand", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List findByExample(BuBrand instance) {
		log.debug("finding BuBrand instance by example");
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
		log.debug("finding BuBrand instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BuBrand as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBranchCode(Object branchCode) {
		return findByProperty(BRANCH_CODE, branchCode);
	}

	public List findByBrandName(Object brandName) {
		return findByProperty(BRAND_NAME, brandName);
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
		log.debug("finding all BuBrand instances");
		try {
			String queryString = "from BuBrand order by brandCode";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuBrand merge(BuBrand detachedInstance) {
		log.debug("merging BuBrand instance");
		try {
			BuBrand result = (BuBrand) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuBrand instance) {
		log.debug("attaching dirty BuBrand instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuBrand instance) {
		log.debug("attaching clean BuBrand instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuBrandDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuBrandDAO) ctx.getBean("buBrandDAO");
	}
	
	public List findByBranchCodes(final String[] branchCodes, final String organizationCode) {
	    
	   List<BuBrand> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
					"from BuBrand as model ");
				for(int i = 0; i < branchCodes.length; i++){
				    if(i == 0){				
			                hql.append("where model.buBranch =" + branchCodes[i]);
				    }else{
					hql.append("or model.buBranch = " + branchCodes[i]);
				    }
				}

				hql.append(" order by model.brandCode");
				Query query = session.createQuery(hql.toString());
				return query.list();
			    }
			});

	    return result;		
	}
	
	/**
	 * find page line
	 * 
	 * @param groupCode
	 * @param brandCode
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<BuBrand> findPageLineAll(int startPage, int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<BuBrand> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from BuBrand as model where 1=1");
					Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				return query.list();
			}
		});
		return re;
	}
	
	public Long findPageLineMaxIndex(){
		Long lineMaxIndex = new Long(0);
		List<BuBrand> result = getHibernateTemplate().executeFind(new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from BuBrand as line where 1=1 order by indexNo");
			Query query = session.createQuery(hql.toString());
			return query.list();
		    }
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
		}
		return lineMaxIndex;
	}
	/**
	 * 依據啟用狀態查詢出幣別
	 * 
	 * @param isEnable
	 * @return List
	 */
	public List findBrandByEnable(String isEnable) {
		
	    StringBuffer hql = new StringBuffer("from BuBrand as model");
	    if (StringUtils.hasText(isEnable)){
                hql.append(" where model.enable = ?");
                return getHibernateTemplate().find(hql.toString(), new Object[]{isEnable});
	    }
	    return getHibernateTemplate().find(hql.toString());		
	}

	//新增品牌(Henry)
	public List<SiMenu> findPageLine(int startPage, int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String enable = "Y";
	
		List<SiMenu> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    	String hql = " from BuBrand M where M.enable=:enable";
					Query query = session.createQuery(hql);
					if(startRecordIndexStar > 0) query.setFirstResult(startRecordIndexStar);
					if(pSize > 0) query.setMaxResults(pSize);
					query.setParameter("enable", enable);
					return query.list();
			    }
			}); 
		return result;
	}
	
	public Long findPageLineCount(){
		final String enable = "Y";
		String hql = " select count(*) from BuBrand M where M.enable=?";
		Object obj = getHibernateTemplate().find(hql, new Object[]{enable});
		return obj == null ? 0L : (Long)(((List)obj).get(0));
	}
	public BuBrand findBybraId(java.lang.String id) {
		log.info("getting BuBrand instance with id: " + id);
		try {
			BuBrand instance = (BuBrand) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuBrand", id.toUpperCase());
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}