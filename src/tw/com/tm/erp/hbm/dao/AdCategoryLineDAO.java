package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
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
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.AdCategoryLineId;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCommonPhraseLine entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCommonPhraseLine
 * @author MyEclipse Persistence Tools
 */

public class AdCategoryLineDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(AdCategoryLineDAO.class);
	// property constants
	public static final String CLASSNO = "classNo";
	public static final String CLASSNAME = "className";
	public static final String GROUPNO = "groupNo";
	public static final String DISPLAYSORT = "displaySort";
	public static final String ENABLE = "enable";


	
	protected void initDao() {
		// do nothing
	}

	public void save(AdCategoryLine transientInstance) {
		log.debug("saving AdCategoryLine instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(AdCategoryLine transientInstance) {
		log.debug("updating AdCategoryLine instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AdCategoryLine persistentInstance) {
		log.debug("deleting AdCategoryLine instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AdCategoryLine findById(
			tw.com.tm.erp.hbm.bean.AdCategoryLineId id) {
		log.debug("getting AdCategoryLine instance with id: " + id);
		try {
			AdCategoryLine instance = (AdCategoryLine) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.AdCategoryLine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public AdCategoryLine findById(String headCode, String lineCode){
		AdCategoryHead adCategoryHead = new AdCategoryHead();
		//adCategoryHead.setHeadCode(headCode);
		AdCategoryLineId id = new AdCategoryLineId();
		id.setAdCategoryHead(adCategoryHead);
		//id.setLineCode(lineCode);
		return this.findById(id);
	}
	
	public List findByExample(BuCommonPhraseLine instance) {
		log.debug("finding BuCommonPhraseLine instance by example");
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
		log.debug("finding BuCommonPhraseLine instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuCommonPhraseLine as model where model."
					+ propertyName + "= ? order by model.id.lineCode";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByClassNo(Object classNo) {
		return findByProperty(CLASSNO, classNo);
	}

	public List findByClassName(Object className) {
		return findByProperty(CLASSNAME, className);
	}

	public List findByGroupNo(Object groupNo) {
		return findByProperty(GROUPNO, groupNo);
	}

	public List findByDisplaySort(Object displaySort) {
		return findByProperty(DISPLAYSORT, displaySort);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}


	public List findAll() {
		log.debug("finding all AdCategoryLine instances");
		try {
			String queryString = "from AdCategoryLine";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findEnableLineById(String headCode) {
		try {
			AdCategoryHeadDAO headDAO = DAOFactory.getInstance().getAdCategoryHeadDAO();
			AdCategoryHead head = headDAO.findById(headCode);
			
			StringBuffer hql = new StringBuffer("from AdCategoryLine as line ");
			hql.append("where line.id.adCategoryHead = ? ");
			hql.append("and line.enable = ? ");
			hql.append("order by line.displaySort");
			return getHibernateTemplate().find(hql.toString(), new Object[]{head, "Y"});

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public BuCommonPhraseLine merge(BuCommonPhraseLine detachedInstance) {
		log.debug("merging BuCommonPhraseLine instance");
		try {
			BuCommonPhraseLine result = (BuCommonPhraseLine) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuCommonPhraseLine instance) {
		log.debug("attaching dirty BuCommonPhraseLine instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuCommonPhraseLine instance) {
		log.debug("attaching clean BuCommonPhraseLine instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuCommonPhraseLineDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuCommonPhraseLineDAO) ctx.getBean("buCommonPhraseLineDAO");
	}
	/**
	 * 依據查詢條件取得常用字彙資訊
	 * 
	 * @param headCode
	 * @param name
	 * @param description
	 * @return List
	 */
	public List<BuCommonPhraseLine> findCommonPhraseLineByAttribute(
			String headCode, String searchString) {
		try {
			// BuCommonPhraseHead head = new BuCommonPhraseHead(headCode);

			StringBuffer hql = new StringBuffer(
					"from BuCommonPhraseLine as model ");
			hql.append("where model.id.buCommonPhraseHead = '" + headCode
					+ "' ");
			hql.append("and model.enable = 'Y' ");
			hql.append((searchString != null
							&& !"".equals(searchString.trim()) ? "and ("
							+ searchString + ") " : ""));
			// hql.append("order by model.indexNo");

			System.out.println(hql.toString());
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(
					hql.toString());

		} catch (RuntimeException re) {
			throw re;
		}
			
	}
	/**
	 * 依據常用字彙主檔代碼取得啟用之明細檔資訊
	 * 
	 * @param headCode
	 * @return List
	 */
	public List<BuCommonPhraseLine> findCommonPhraseEnableLine(BuCommonPhraseHead head) {
		try {

			StringBuffer hql = new StringBuffer(
					"from BuCommonPhraseLine as line ");
			hql.append("where line.id.buCommonPhraseHead = ? ");
			hql.append("and line.enable = ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(
					hql.toString(), new Object[] { head, "Y" });

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據常用字彙主檔代碼取得啟用之明細檔資訊
	 * 
	 * @param headCode
	 * @return List
	 */
	public List<BuCommonPhraseLine> findCommonPhraseEnablePageLine(final String headCode , int iSPage, int iPSize) {
		try {
			final int startIndex = iSPage * iPSize;
			final int pSize = iPSize;
			System.out.println("start to find buCommonPhraseLine....");
			List result = getHibernateTemplate().executeFind(new HibernateCallback() {
  			public Object doInHibernate(Session session)throws HibernateException, SQLException {
  							
  			StringBuffer hql = new StringBuffer("from BuCommonPhraseLine as line ");
  			hql.append("where line.id.buCommonPhraseHead.headCode = '"+headCode+"' ");
  			hql.append("and line.enable = 'Y' order by indexNo");
  			Query query = session.createQuery(hql.toString());
  				query.setFirstResult(startIndex);
  				query.setMaxResults(pSize);
  				System.out.println("startIndex:"+startIndex);
  				System.out.println("pSize:"+pSize);
  			return query.list();
				}
			});
			log.info("buCommonPhraseLine.size : "+result.size());
			
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public Long findCommonPhraseEnablePageLineMaxIndex(final String headCode){
		Long lineMaxIndex = new Long(0);
		List<BuCommonPhraseLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from BuCommonPhraseLine as line ");
				    hql.append("where line.id.buCommonPhraseHead.headCode = :headCode order by indexNo");
			Query query = session.createQuery(hql.toString());
			    query.setString("headCode", headCode);
			return query.list();
		    }
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
		}
		return lineMaxIndex;
	}
    /**
     * 依據brandCode, categoryType, parentCategoryCode, isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param parentCategoryCode
     * @param enable
     * @return
     */
    public List<AdCategoryLine> findByParentCategoryCode( String classification, String enable) {
    	return findByProperty("AdCategoryLine", "classification = ? ","enable = ? ", 
    			new Object[]{ classification, enable} );
    }
	/**
	 * 只撈出一筆
	 * @param headCode
	 * @param lineCode
	 * @param enable
	 * @return
	 */
	public BuCommonPhraseLine findOneBuCommonPhraseLine(String headCode,String lineCode, String enable){
		return (BuCommonPhraseLine)findFirstByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ?", new Object[]{headCode, lineCode, enable});
		
	}
	
	public BuCommonPhraseLine findOneBuCommonPhraseLine(String headCode,String lineCode){
		return findOneBuCommonPhraseLine(headCode, lineCode, "Y");
		
	}
	
    /**
     * 依據brandCode, categoryType, categoryCode, isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param categoryCode
     * @param enable
     * @return
     */
    public AdCategoryLine findByCategoryCode(String deptNo, String groupNo, String classNo, String enable) {
    	return (AdCategoryLine)findFirstByProperty("AdCategoryLine", "and id.deptNo = ? and id.groupNo = ? and id.classNo = ? and enable = ?", 
    			new Object[]{deptNo, groupNo, classNo, enable} );
    }
	/**
	 * 依據品牌代號和工號進行查詢
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithAddressView
	 */
	public AdCategoryLine findbyInChargeCode(String categorySystem) throws Exception{
		log.info("findbyInChargeCodeqazsds~~~~~~~~~~~~~~~");
		try{	
		StringBuffer queryString = new StringBuffer("from AdCategoryLine");
		queryString.append(" as model where ");
		queryString.append(" model.classNo");
		queryString.append("= ? ");
		
		List<AdCategoryLine> result = getHibernateTemplate().find(queryString.toString(), categorySystem);
		
		log.info("result~~~~~~~~~~~~~~~"+result.size());
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
		}catch(Exception e){
			log.error("aaa，原因：" + e.toString());
			throw new Exception("bbb，原因：" + e.getMessage());
		}
	
	}
}