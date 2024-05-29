package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCurrency;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCurrency entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCurrency
 * @author MyEclipse Persistence Tools
 */

public class BuCurrencyDAO extends BaseDAO {

	// property constants
	public static final String CURRENCY_CNAME = "currencyCName";
	public static final String CURRENCY_ENAME = "currencyEName";
	public static final String DESCRIPTION = "description";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
	}
	
	/**
	 * insert new BuCurrency record
	 * @param BuCurrency
	 */
	public void save(BuCurrency buCurrency) {
		try {
			getHibernateTemplate().save(buCurrency);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * update BuCurrency
	 * @param BuCurrency
	 */
	public void update(BuCurrency buCurrency) {
		try {
			getHibernateTemplate().update(buCurrency);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void saveOrUpdate(BuCurrency buCurrency) {
		if(buCurrency.getCurrencyCode() == null || "".equals(buCurrency.getCurrencyCode())) {
			save(buCurrency);
		} else {
			update(buCurrency);
		}
	}

	public void delete(BuCurrency curr) {
		try {
			getHibernateTemplate().delete(curr);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public BuCurrency findById(java.lang.String id) {
		try {
			BuCurrency instance = (BuCurrency) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.BuCurrency", id.toUpperCase());
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByExample(BuCurrency instance) {
		try {
			List results = getHibernateTemplate().findByExample(instance);
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		try {
			String queryString = "from BuCurrency as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	public List findByCurrencyCName(Object currencyCName) {
		return findByProperty(CURRENCY_CNAME, currencyCName);
	}

	public List findByCurrencyEName(Object currencyEName) {
		return findByProperty(CURRENCY_ENAME, currencyEName);
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
		try {
			String queryString = "from BuCurrency";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據啟用狀態查詢出幣別
	 * 
	 * @param isEnable
	 * @return List
	 */
	public List findCurrencyByEnable(String isEnable) {
		
	    StringBuffer hql = new StringBuffer("from BuCurrency as model");
	    if (StringUtils.hasText(isEnable)){
                hql.append(" where model.enable = ?");
                return getHibernateTemplate().find(hql.toString(), new Object[]{isEnable});
	    }
	    return getHibernateTemplate().find(hql.toString());		
	}

	public BuCurrency merge(BuCurrency detachedInstance) {
		try {
			BuCurrency result = (BuCurrency) getHibernateTemplate().merge(
					detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(BuCurrency instance) {
		try {
			getHibernateTemplate().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(BuCurrency instance) {
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public static BuCurrencyDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BuCurrencyDAO) ctx.getBean("buCurrencyDAO");
	}
	/**
	 * 依照輸入條件來搜尋
	 * 
	 * @param currencyCode
	 * @param currencyCName
	 * @param currencyEName
	 * @return List
	 */
	public List<BuCurrency> findCurrencyList(String currencyCode, String currencyCName, String currencyEName) {
		currencyCode = currencyCode.toUpperCase() + "%";
		currencyCName = "%" + currencyCName + "%";
		currencyEName = "%" + currencyEName.toUpperCase() + "%";
		try {
			StringBuffer hql = new StringBuffer("from BuCurrency as model ");
			hql.append("where model.currencyCode like ? ");
			hql.append("and model.currencyCName like ? ");
			hql.append("and upper(model.currencyEName) like ? ");
			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(hql.toString(),new String[] { currencyCode, currencyCName, currencyEName });
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 依據輸入的參數來查詢幣別與匯率
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findCurrencyExchangeRateList(HashMap conditionMap) {

//		final String beginDate = (String) conditionMap.get("beginDate");
	    	final String beginDate = (String) conditionMap.get("priceDate");
		final String currencyCode = (String) conditionMap.get("currencyCode");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer("");
				sql.append(" SELECT * FROM (SELECT CURRENCY_CODE, CURRENCY_C_NAME, CURRENCY_E_NAME, EXCHANGE_RATE, BEGIN_DATE "); 
				sql.append(" FROM BU_CURRENCY A, (SELECT R1.SOURCE_CURRENCY, R1.EXCHANGE_RATE, R1.BEGIN_DATE ");
				sql.append(" FROM BU_EXCHANGE_RATE R1, ( SELECT SOURCE_CURRENCY, MAX (BEGIN_DATE) AS BEGIN_DATE ");
//				sql.append(" FROM BU_EXCHANGE_RATE WHERE AGAINST_CURRENCY = 'NTD' AND BEGIN_DATE <= ");
				sql.append(" FROM BU_EXCHANGE_RATE WHERE AGAINST_CURRENCY = 'NTD' AND BEGIN_DATE = ");
				if(StringUtils.hasText(beginDate))
					sql.append(" TO_DATE('" + beginDate + "','yyyyMMdd') ");
				else
					sql.append(" CURRENT_DATE ");
				
				sql.append(" GROUP BY SOURCE_CURRENCY) R2 ");
				sql.append(" WHERE R1.SOURCE_CURRENCY = R2.SOURCE_CURRENCY AND R1.BEGIN_DATE = R2.BEGIN_DATE) B ");
				sql.append(" WHERE A.CURRENCY_CODE = B.SOURCE_CURRENCY ");
				if(StringUtils.hasText(currencyCode))
					sql.append(" AND CURRENCY_CODE = '" + currencyCode + "' ");
				sql.append(" ) ");
				System.out.println("sql.toString() = " + sql.toString());
				Query query = session.createSQLQuery(sql.toString());
				return query.list();
			}
		});

		return result;
	}
	
	public BuCurrency findByName(String currencyCode){
		StringBuffer hql = new StringBuffer("");
		hql.append("from BuCurrency as model where 1=1 ");
		hql.append(" and model.currencyCode = ?");
		
		Object[] objArray =  new Object[] {currencyCode};
		
		List<BuCurrency> result = getHibernateTemplate().find(
			hql.toString(), objArray);
		
		//log.info("BuCurrency.size:" + result.size());
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	/**
	 * 依據品牌、最後更新日期查詢
	 * 
	 * @param conditionMap
	 * @return List<BuCurrency>
	 */
	public List<BuCurrency> findBuCurrencyListByProperty(HashMap conditionMap) {
		
		final String dataDate = (String) conditionMap.get("actualDataDateEnd");
		List<BuCurrency> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    
			        StringBuffer hql = new StringBuffer("select model from BuCurrency as model ");
			        
			        if(StringUtils.hasText(dataDate)){
					//hql.append("and to_char(model.lastUpdateDate, 'YYYY/MM/DD') = :dataDate ");
					hql.append(" where to_char (model.lastUpdateDate, 'yyyymmdd') = :dataDate ");
				}
				
				Query query = session.createQuery(hql.toString());
				if(StringUtils.hasText(dataDate)){
					query.setString("dataDate", dataDate);
				}
				return query.list();
			}
		});

		return result;
	}
}