package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuSupplierWithAddressView entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView
 * @author MyEclipse Persistence Tools
 */

public class BuSupplierWithAddressViewDAO extends BaseDAO {
	private static final Log log = LogFactory
			.getLog(BuSupplierWithAddressViewDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(BuSupplierWithAddressView transientInstance) {
		log.debug("saving BuSupplierWithAddressView instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(BuSupplierWithAddressView persistentInstance) {
		log.debug("deleting BuSupplierWithAddressView instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BuSupplierWithAddressView findById(String id) {
		log.debug("getting BuSupplierWithAddressView instance with id: " + id);
		try {
			BuSupplierWithAddressView instance = (BuSupplierWithAddressView) getHibernateTemplate()
					.get("tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public BuSupplierWithAddressView findById(final String supplierCode, final String brandCode) {		
		
		List<BuSupplierWithAddressView> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from BuSupplierWithAddressView as model where 1=1 ");
				hql.append(" and model.brandCode = :brandCode ");
				hql.append(" and model.supplierCode = :supplierCode ");
				System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());				
			
				query.setString("brandCode", brandCode );
				query.setString("supplierCode", supplierCode );				
				return  query.list();		
			}
		});
		
		if(re.size() >0)			
			return re.get(0);
		else
			return null;
	}
	
	public List findByExample(BuSupplierWithAddressView instance) {
		log.debug("finding BuSupplierWithAddressView instance by example");
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
		log.debug("finding BuSupplierWithAddressView instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from BuSupplierWithAddressView as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all BuSupplierWithAddressView instances");
		try {
			String queryString = "from BuSupplierWithAddressView";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findRp() {
		log.debug("finding all BuSupplierWithAddressView instances");
		try {
			String queryString = " from BuSupplierWithAddressView where brandCode='T2' and supplierTypeCode='1' and categoryCode='3'  order by supplierCode";
			log.info("廠商數量:"+getHibernateTemplate().find(queryString).size());
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BuSupplierWithAddressView merge(
			BuSupplierWithAddressView detachedInstance) {
		log.debug("merging BuSupplierWithAddressView instance");
		try {
			BuSupplierWithAddressView result = (BuSupplierWithAddressView) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BuSupplierWithAddressView instance) {
		log.debug("attaching dirty BuSupplierWithAddressView instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BuSupplierWithAddressView instance) {
		log.debug("attaching clean BuSupplierWithAddressView instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BuSupplierWithAddressViewDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (BuSupplierWithAddressViewDAO) ctx
				.getBean("buSupplierWithAddressViewDAO");
	}
	/**
	 * 依據品牌代號及廠商代號，查詢啟用狀態之廠商資料
	 * 
	 * @param brandCode
	 * @param supplierCode
	 * @return BuSupplierWithAddressView
	 */
	public BuSupplierWithAddressView findEnableSupplierById(String brandCode,
			String supplierCode ,String enable) {
		try {
			StringBuffer hql = new StringBuffer(
					"from BuSupplierWithAddressView as model ");
			hql.append("where model.brandCode = ? ");
			hql.append("and model.supplierCode = ? ");
			if(!"".equals(enable)){
				hql.append("and model.enable = ? ");
			}
			Object[] allArg;
			if(!"".equals(enable)){
				allArg = new Object[] { brandCode, supplierCode, enable };
			}
			else
			{
				allArg = new Object[] { brandCode, supplierCode };
			}

			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			List<BuSupplierWithAddressView> lists = getHibernateTemplate().find(hql.toString(),allArg);
			return (lists.size() > 0 ? lists.get(0) : null);

		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public BuSupplierWithAddressView findSupplierByAddressBookIdAndBrandCode(
		    Long addressBookId, String brandCode) {

		StringBuffer hql = new StringBuffer(
			"from BuSupplierWithAddressView as model ");
		hql.append("where model.brandCode = ? ");
		hql.append("and model.addressBookId = ? ");

		List<BuSupplierWithAddressView> result = getHibernateTemplate().find(
			hql.toString(), new Object[] { brandCode, addressBookId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public BuSupplierWithAddressView findByBrandCodeAndSupplierCode(
		    String brandCode, String supplierCode) {

		StringBuffer hql = new StringBuffer(
			"from BuSupplierWithAddressView as model ");
		hql.append("where model.brandCode = ? ");
		hql.append("and model.supplierCode = ? ");

		List<BuSupplierWithAddressView> result = getHibernateTemplate().find(
			hql.toString(), new Object[] { brandCode, supplierCode });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public List<BuSupplierWithAddressView> findByBrandCodeAndSupplierCodes(
    		String brandCode, String supplierCode) {
    	return findByProperty("BuSupplierWithAddressView", "and id.brandCode = ? and supplierCode = ?", new Object[]{brandCode, supplierCode});
   }
	
	/**
	 * 依據供應商資料查詢螢幕的輸入條件進行查詢
	 * 
	 * @param findObj
	 * @param startpage
	 * @param pageSize
	 * @return Map
	 * @throws Exception
	 */
	public Map findPageLine(Map findObj, int startPage, int pageSize) {

		final String brandCode = (String) findObj.get("brandCode");
		final String type = (String) findObj.get("type");
		final String identityCode = (String) findObj.get("identityCode");
		final String chineseName = (String) findObj.get("chineseName");
		final String supplierTypeCode = (String) findObj.get("supplierTypeCode");
		final String supplierCode = (String) findObj.get("supplierCode");
		final String categoryCode = (String) findObj.get("categoryCode");
		final String customsBroker = (String) findObj.get("customsBroker");
		final String agent = (String) findObj.get("agent");
		final String commissionRate_Start = (String) findObj.get("commissionRateStart");
		final String commissionRate_End = (String) findObj.get("commissionRateEnd");
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from BuSupplierWithAddressView as model ");
				if( startRecordIndexStar < 0 ) {
					hql=new StringBuffer("select count(model.addressBookId) as rowCount from BuSupplierWithAddressView as model ");
				}
				hql.append("where  model.brandCode = :brandCode");

				if (StringUtils.hasText(type))
					hql.append(" and model.type = :type");

				if (StringUtils.hasText(identityCode))
					hql.append(" and model.identityCode = :identityCode");

				if (StringUtils.hasText(chineseName))
					hql.append(" and model.chineseName like :chineseName");

				if (StringUtils.hasText(supplierTypeCode))
					hql.append(" and model.supplierTypeCode = :supplierTypeCode");

				if (StringUtils.hasText(supplierCode))
					hql.append(" and model.supplierCode = :supplierCode");

				if (StringUtils.hasText(categoryCode))
					hql.append(" and model.categoryCode = :categoryCode");

				if (StringUtils.hasText(customsBroker))
					hql.append(" and model.customsBroker = :customsBroker");

				if (StringUtils.hasText(agent))
					hql.append(" and model.agent = :agent");

				if (StringUtils.hasText(commissionRate_Start))
					hql.append(" and model.commissionRate >= :commissionRate_Start");

				if (StringUtils.hasText(commissionRate_End))
					hql.append(" and model.commissionRate <= :commissionRate_End");

				Query query = session.createQuery(hql.toString());
				if( startRecordIndexStar >= 0 ) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
					System.out.println("startRecordIndexStar:"+startRecordIndexStar);
					System.out.println("pSize:"+pSize);
				}
				query.setString("brandCode", brandCode);

				if (StringUtils.hasText(type))
					query.setString("type", type);

				if (StringUtils.hasText(identityCode))
					query.setString("identityCode", identityCode);

				if (StringUtils.hasText(chineseName))
					query.setString("chineseName", "%" + chineseName + "%");

				if (StringUtils.hasText(supplierTypeCode))
					query.setString("supplierTypeCode", supplierTypeCode);

				if (StringUtils.hasText(supplierCode))
					query.setString("supplierCode", supplierCode);

				if (StringUtils.hasText(categoryCode))
					query.setString("categoryCode", categoryCode);

				if (StringUtils.hasText(customsBroker))
					query.setString("customsBroker", customsBroker);

				if (StringUtils.hasText(agent))
					query.setString("agent", agent);

				if (StringUtils.hasText(commissionRate_Start))
					query.setDouble("commissionRate_Start", Double.parseDouble(commissionRate_Start));

				if (StringUtils.hasText(commissionRate_End))
					query.setDouble("commissionRate_End", Double.parseDouble(commissionRate_End));

				return query.list();
			}
		});
		log.info("buSupplierWithAddressBook.form:"+result.size());
		Map returnResult = new HashMap();		
		returnResult.put("form", startRecordIndexStar >= 0 ? result: null);			
		if(result.size() == 0){
			returnResult.put("recordCount", 0L);
		}else{
			
			returnResult.put("recordCount",startRecordIndexStar >= 0? result.size() : Long.valueOf(result.get(0).toString()));
		}

		return returnResult;
	}
	 public List<BuSupplierWithAddressView> findSupplierPageData(HashMap conditionMap,int iSPage,int iPSize){
		 	
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String identityCode = (String)conditionMap.get("identityCode");
		 final String supplierCode = (String)conditionMap.get("supplierCode");
		 final String enable = (String)conditionMap.get("enable");
		 
		 final int startPage = iSPage*iPSize;
		 final int pageSize = iPSize;
    	
		 List<BuSupplierWithAddressView> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select head ");
	                        hql.append(" from BuSupplierWithAddressView as head ");
	                        hql.append(" where 1=1 ");
                        
	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        
	                        if (StringUtils.hasText(identityCode))
	                        	hql.append(" and head.identityCode like :identityCode " );
	                        if (StringUtils.hasText(supplierCode))
	                        	hql.append(" and head.supplierCode like :supplierCode " );
	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and head.enable = :enable " );
	                        hql.append(" order by head.supplierCode " );
	                        
	                        Query query = session.createQuery(hql.toString());
	                        query.setFirstResult(0);
	                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        
	                        if (StringUtils.hasText(identityCode))
	                            query.setString("identityCode", "%"+identityCode+"%");
	                        
	                        if (StringUtils.hasText(supplierCode))
	                            query.setString("supplierCode", "%"+supplierCode+"%");
	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);


	                        log.info(hql.toString());
	                        log.info(query.toString());
	        				query.setFirstResult(startPage);
	        				query.setMaxResults(pageSize);
	                        return query.list();
	                    }
	                });

	        return result;
    	
    	
    }
	 public List<BuSupplierWithAddressView> findSupplierAll(HashMap conditionMap){
		 	
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String identityCode = (String)conditionMap.get("identityCode");
		 final String supplierCode = (String)conditionMap.get("supplierCode");
		 final String enable = (String)conditionMap.get("enable");

    	
		 List<BuSupplierWithAddressView> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select head ");
	                        hql.append(" from BuSupplierWithAddressView as head ");
	                        hql.append(" where 1=1 ");
                        
	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        
	                        if (StringUtils.hasText(identityCode))
	                        	hql.append(" and head.identityCode like :identityCode " );
	                        if (StringUtils.hasText(supplierCode))
	                        	hql.append(" and head.supplierCode like :supplierCode " );
	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and head.enable = :enable " );
	                        hql.append(" order by head.supplierCode " );
	                        
	                        Query query = session.createQuery(hql.toString());
	                        query.setFirstResult(0);
	                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        
	                        if (StringUtils.hasText(identityCode))
	                            query.setString("identityCode", "%"+identityCode+"%");
	                        
	                        if (StringUtils.hasText(supplierCode))
	                            query.setString("supplierCode", "%"+supplierCode+"%");
	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);


	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });

	        return result;
    	
    	
    }
}