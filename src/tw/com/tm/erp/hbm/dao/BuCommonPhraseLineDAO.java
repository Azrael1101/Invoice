package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
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

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuPosUiConfigHead;
import tw.com.tm.erp.hbm.bean.BuPosUiConfigLine;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.TransferStatusInfo;

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

public class BuCommonPhraseLineDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(BuCommonPhraseLineDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ATTRIBUTE1 = "attribute1";
	public static final String ATTRIBUTE2 = "attribute2";
	public static final String ATTRIBUTE3 = "attribute3";
	public static final String ATTRIBUTE4 = "attribute4";
	public static final String ATTRIBUTE5 = "attribute5";
	public static final String PARAMETER1 = "parameter1";
	public static final String PARAMETER2 = "parameter2";
	public static final String PARAMETER3 = "parameter3";
	public static final String PARAMETER4 = "parameter4";
	public static final String PARAMETER5 = "parameter5";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void save(BuCommonPhraseLine transientInstance) {
		log.debug("saving BuCommonPhraseLine instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(BuCommonPhraseLine transientInstance) {
		log.debug("updating BuCommonPhraseLine instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuCommonPhraseLine persistentInstance) {
		log.debug("deleting BuCommonPhraseLine instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuCommonPhraseLine findById(
			tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId id) {
		log.debug("getting BuCommonPhraseLine instance with id: " + id);
		try {
			BuCommonPhraseLine instance = (BuCommonPhraseLine) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuCommonPhraseLine", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public BuCommonPhraseLine findById(String headCode, String lineCode){
		BuCommonPhraseHead buCommonPhraseHead = new BuCommonPhraseHead();
		buCommonPhraseHead.setHeadCode(headCode);
		BuCommonPhraseLineId id = new BuCommonPhraseLineId();
		id.setBuCommonPhraseHead(buCommonPhraseHead);
		id.setLineCode(lineCode);
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

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
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

	public List findByParameter1(Object parameter1) {
		return findByProperty(PARAMETER1, parameter1);
	}

	public List findByParameter2(Object parameter2) {
		return findByProperty(PARAMETER2, parameter2);
	}

	public List findByParameter3(Object parameter3) {
		return findByProperty(PARAMETER3, parameter3);
	}

	public List findByParameter4(Object parameter4) {
		return findByProperty(PARAMETER4, parameter4);
	}

	public List findByParameter5(Object parameter5) {
		return findByProperty(PARAMETER5, parameter5);
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
		log.debug("finding all BuCommonPhraseLine instances");
		try {
			String queryString = "from BuCommonPhraseLine";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List qqqq(String headCode) {
		try {
			BuCommonPhraseHeadDAO headDAO = DAOFactory.getInstance().getBuCommonPhraseHeadDAO();
			BuCommonPhraseHead head = headDAO.findById(headCode);
			
			StringBuffer hql = new StringBuffer("from BuCommonPhraseLine as line ");
			hql.append("where line.id.buCommonPhraseHead = ? ");
			hql.append("and line.enable = ? ");
			hql.append("order by line.indexNo");
			return getHibernateTemplate().find(hql.toString(), new Object[]{head, "Y"});

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<BuPosUiConfigLine> findBuPosUiConfigByHead() {
		log.debug("form BuPosUiConfigLine instance");
		try {
			String queryString = "from BuPosUiConfigLine   ";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
		
	}
	
	public List<TransferStatusInfo> findAllTransferStatus() {
		log.debug("form TransferStatusInfo instance");
		try {
			String queryString = "from TransferStatusInfo";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("merge failed", re);
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
	 * 依據查詢條件取得常用字彙資訊
	 * 
	 * @param headCode
	 * @param name
	 * @param description
	 * @return List
	 */
	//高雄報表列表 Steve 7/7
	public List<BuCommonPhraseLine> findCommonPhraseLineByAttribute(
			String headCode, String searchString, String ifHd) {
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
			hql.append((ifHd != null
				&& !"".equals(ifHd.trim()) ? "and ("
				+ ifHd + ") " : ""));
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
  			hql.append(" order by indexNo");
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
	 * 只撈出一筆
	 * @param headCode
	 * @param lineCode
	 * @param enable
	 * @return
	 */
	public BuCommonPhraseLine findOneBuCommonPhraseLine(String headCode,String lineCode, String enable){
		log.info("進入findOneBuCommonPhraseLine");
		return (BuCommonPhraseLine)findFirstByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ? and enable = ?", new Object[]{headCode, lineCode, enable});
		
	}
	
	public BuCommonPhraseLine findOneBuCommonPhraseLine(String headCode,String lineCode){
		log.info("進入findOneBuCommonPhraseLine"+ "---DeCCCCCCBug"+headCode + lineCode);
		return findOneBuCommonPhraseLine(headCode, lineCode, "Y");
	}
	
	public BuCommonPhraseLine findOneBuCommonPhraseLineForMarquee(String headCode,String lineCode){
		log.info("進入findOneBuCommonPhraseLine"+ "---DeCCCCCCBug"+headCode + lineCode);
		return findOneBuCommonPhraseLineForM(headCode, lineCode);
	}
	
	public BuCommonPhraseLine findOneBuCommonPhraseLineForM(String headCode,String lineCode){
		log.info("進入findOneBuCommonPhraseLine");
		return (BuCommonPhraseLine)findFirstByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? and id.lineCode = ?", new Object[]{headCode, lineCode});
		
	}

	public List findEnableLineById(String pos) {
		// TODO Auto-generated method stub
		return null;
	}
}