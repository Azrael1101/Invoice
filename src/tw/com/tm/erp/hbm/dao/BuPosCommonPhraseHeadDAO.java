package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuPosCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuPosCommonPhraseLine;

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

public class BuPosCommonPhraseHeadDAO  extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(BuPosCommonPhraseHeadDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

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
	public boolean save(BuCommonPhraseHead transientInstance) {
		boolean result = false;
		log.debug("saving BuCommonPhraseHead instance");		
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

	public boolean update(BuCommonPhraseHead transientInstance) {
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
	
	public void delete(BuCommonPhraseHead persistentInstance) {
		log.debug("deleting BuCommonPhraseHead instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuCommonPhraseHead findById(java.lang.String id) {
		log.debug("getting BuCommonPhraseHead instance with id: " + id);
		try {
			BuCommonPhraseHead instance = (BuCommonPhraseHead) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuCommonPhraseHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BuCommonPhraseHead instance) {
		log.debug("finding BuCommonPhraseHead instance by example");
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

	public List<BuPosCommonPhraseHead> findByProperty(String propertyName, Object value) {
		log.debug("finding BuPosCommonPhraseHead instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuPosCommonPhraseHead as model where model."
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
		log.debug("finding all BuCommonPhraseHead instances");
		try {
			String queryString = "from BuCommonPhraseHead";
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
	public List<BuCommonPhraseHead> findBySearchValue(String headCode, String name, String description) {
		
	    headCode = "%" + headCode.toUpperCase() + "%";
	    name = "%" + name + "%";
	    description = "%" + description + "%";
			
	    StringBuffer hql = new StringBuffer("from BuCommonPhraseHead as model ");	
	    hql.append("where upper(model.id.headCode) like ? ");
	    hql.append("and model.name like ? ");
	    hql.append("and model.description like ? ");
	    getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			
            return getHibernateTemplate().find(hql.toString(), new String[]{headCode, name, description});		
	}
	public BuCommonPhraseHead merge(BuCommonPhraseHead detachedInstance) {
		log.debug("merging BuCommonPhraseHead instance");
		try {
			BuCommonPhraseHead result = (BuCommonPhraseHead) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuCommonPhraseHead instance) {
		log.debug("attaching dirty BuCommonPhraseHead instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuCommonPhraseHead instance) {
		log.debug("attaching clean BuCommonPhraseHead instance");
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
			StringBuffer hql = new StringBuffer("from BuCommonPhraseLine as line ");
			hql.append("where line.enable = ? ");
			return getHibernateTemplate().
					find(hql.toString(), new Object[]{"Y"});

		} catch (RuntimeException re) {
			throw re;
		}
	}	

	public static BuPosCommonPhraseHeadDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuPosCommonPhraseHeadDAO) ctx.getBean("buCommonPhraseHeadDAO");
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
	

}