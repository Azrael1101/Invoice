package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.AdCategory;
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCommonPhraseHead entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCommonPhraseHead
 * @author MyEclipse Persistence Tools
 */

public class AdCategoryDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(AdCategoryHeadDAO.class);
	// property constants
	public static final String DEPTNO = "deptNo";
	public static final String GROUPNO = "groupNo";
	public static final String ENABLE = "enable";
	public static final String GROUPNAME = "groupName";
	public static final String DISPLAYSORT = "displaySort";

	protected void initDao() {
		// do nothing
	}
	/*
	public boolean save(BuCommonPhraseHead transientInstance) {
		log.debug("saving BuCommonPhraseHead instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}*/
	public boolean save(AdCategory transientInstance) {
		boolean result = false;
		log.debug("saving AdCategory instance");		
		try {
			getHibernateTemplate().save(transientInstance);
			//log.debug("save successful");
			result = true;
		} catch (RuntimeException re) {
			result = false;
			re.printStackTrace();
			//log.error("save failed", re);
			//throw re;			
		} 
		return result;
	}

	public boolean update(AdCategory transientInstance) {
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
	
	public void delete(AdCategory persistentInstance) {
		log.debug("deleting AdCategory instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AdCategory findById(java.lang.String id) {
		log.debug("getting AdCategory instance with id: " + id);
		try {
			AdCategory instance = (AdCategory) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.AdCategory", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(AdCategory instance) {
		log.debug("finding AdCategory instance by example");
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
		log.debug("finding AdCategory instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from AdCategory as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDeptNo(Object deptNo) {
		return findByProperty(DEPTNO, deptNo);
	}

	public List findByGroupNo(Object groupNo) {
		return findByProperty(GROUPNO, groupNo);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByGroupName(Object groupName) {
		return findByProperty(GROUPNAME, groupName);
	}

	public List findByDisplaySort(Object displaySort) {
		return findByProperty(DISPLAYSORT, displaySort);
	}

	public List findAll() {
		log.debug("finding all AdCategory instances");
		try {
			String queryString = "from AdCategory";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * �̷ӿ�J���ӷj�M
	 * @param headCode
	 * @param name
	 * @param description
	 * @return List
	 */
	public List<AdCategory> findBySearchValue(String headCode, String name, String description) {
		
	    headCode = "%" + headCode.toUpperCase() + "%";
	    name = "%" + name + "%";
	    description = "%" + description + "%";
			
	    StringBuffer hql = new StringBuffer("from AdCategory as model ");	
	    hql.append("where upper(model.id.headCode) like ? ");
	    hql.append("and model.name like ? ");
	    hql.append("and model.description like ? ");
	    getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			
            return getHibernateTemplate().find(hql.toString(), new String[]{headCode, name, description});		
	}
	public AdCategory merge(AdCategory detachedInstance) {
		log.debug("merging BuCommonPhraseHead instance");
		try {
			AdCategory result = (AdCategory) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AdCategory instance) {
		log.debug("attaching dirty BuCommonPhraseHead instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AdCategory instance) {
		log.debug("attaching clean AdCategory instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List findEnableLine() {
		try {
			StringBuffer hql = new StringBuffer("from AdCategoryLine as line ");
			hql.append("where line.enable = ? ");
			return getHibernateTemplate().
					find(hql.toString(), new Object[]{"Y"});

		} catch (RuntimeException re) {
			throw re;
		}
	}	

	public static AdCategoryDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (AdCategoryDAO) ctx.getBean("adCategoryDAO");
	}
	
	public List<BuCommonPhraseLine> findEnableLineById(String headCode) {
		try {
			BuCommonPhraseHeadDAO headDAO = DAOFactory.getInstance()
					.getBuCommonPhraseHeadDAO();
			BuCommonPhraseHead head = headDAO.findById(headCode);

			StringBuffer hql = new StringBuffer(
					"from BuCommonPhraseLine as line ");
			hql.append("where line.id.buCommonPhraseHead = ? ");
			hql.append("and line.enable = ? ");
			return getHibernateTemplate().find(
					hql.toString(), new Object[] { head, "Y" });

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據查詢條件取得常用字彙資訊
	 * 
	 * @param headCode
	 * @param name
	 * @param description
	 * @return List
	 */
	public List<BuCommonPhraseHead> findCommonPhraseList(String headCode,
			String name, String description) {
		try {
			headCode = "%" + headCode.toUpperCase() + "%";
			name = "%" + name + "%";
			description = "%" + description + "%";

			StringBuffer hql = new StringBuffer(
					"from BuCommonPhraseHead as model ");
			hql.append("where upper(model.id.headCode) like ? ");
			hql.append("and model.name like ? ");
			hql.append("and model.description like ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(
					hql.toString(),
					new String[] { headCode, name, description });

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據品牌代號和工號進行查詢
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithAddressView
	 */
	public AdCategory findbyparentHeadId(String categoryGroup) throws Exception{
		log.info("findbyInChargeCodeqazsds~~~~~~~~~~~~~~~");
		try{	
		StringBuffer queryString = new StringBuffer("from AdCategory");
		queryString.append(" as model where ");
		queryString.append(" model.classNo");
		queryString.append("= ? ");
		
		List<AdCategory> result = getHibernateTemplate().find(queryString.toString(), categoryGroup);
		
		log.info("result~~~~~~~~~~~~~~~"+result.size());
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
		}catch(Exception e){
			log.error("aaa，原因：" + e.toString());
			throw new Exception("bbb，原因：" + e.getMessage());
		}
	
	}
}