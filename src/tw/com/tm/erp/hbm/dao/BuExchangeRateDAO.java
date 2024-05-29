package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.utils.DateUtils;

public class BuExchangeRateDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuExchangeRateDAO.class);

	/**
	 * 依照輸入條件來搜尋匯率資料
	 *
	 * @param organizationCode
	 * @param sourceCurrency
	 * @param againstCurrency
	 * @param exchangeRate
	 * @param beginDate
	 * @return List
	 */
	public List<BuExchangeRate> findExchangeRateList(String organizationCode, String sourceCurrency, String againstCurrency,
			Double exchangeRate, Date beginDate) {

		final String organizationCode_arg = organizationCode.trim().toUpperCase();
		final String sourceCurrency_arg = "%" + sourceCurrency.trim().toUpperCase() + "%";
		final String againstCurrency_arg = "%" + againstCurrency.trim().toUpperCase() + "%";
		final Double exchangeRate_arg = exchangeRate;
		final Date beginDate_arg = beginDate;

		List<BuExchangeRate> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from BuExchangeRate as model ");
				hql.append("where model.id.organizationCode = :organizationCode");
				hql.append(" and model.id.sourceCurrency like :sourceCurrency");
				hql.append(" and model.id.againstCurrency like :againstCurrency");

				if (exchangeRate_arg != null)
					hql.append(" and model.exchangeRate = :exchangeRate");
				if (beginDate_arg != null)
					hql.append(" and model.id.beginDate = :beginDate");

				hql.append(" ORDER BY model.id.beginDate DESC"); // 預設依啟用日期由近而遠排序

				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				query.setString("organizationCode", organizationCode_arg);
				query.setString("sourceCurrency", sourceCurrency_arg);
				query.setString("againstCurrency", againstCurrency_arg);

				if (exchangeRate_arg != null)
					query.setDouble("exchangeRate", exchangeRate_arg);
				if (beginDate_arg != null)
					query.setDate("beginDate", beginDate_arg);

				return query.list();
			}
		});

		return result;
	}

	/**
	 * 取得最新匯率
	 *
	 * @param organizationCode_var
	 * @param sourceCurrency_var
	 * @param againstCurrency_var
	 * @return
	 */
	public BuExchangeRate getLastExchangeRate(String organizationCode, String sourceCurrency, String againstCurrency) {
		return getLastExchangeRate(organizationCode, sourceCurrency, againstCurrency, null);
	}

	/**
	 * 取得最新匯率
	 *
	 * @param organizationCode
	 * @param sourceCurrency
	 * @param againstCurrency
	 * @param currencyDate
	 * @return
	 */
	@SuppressWarnings(value = "unchecked")
	public BuExchangeRate getLastExchangeRate(String organizationCode, String sourceCurrency, String againstCurrency, Date currencyDate) {
		final String orgCode = organizationCode;
		final String srcCurr = sourceCurrency;
		final String agaCurr = againstCurrency;
		final Date dateCurr = currencyDate;
		BuExchangeRate bxr = null;
		List<BuExchangeRate> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer();
				hql.append(" select * from erp.bu_exchange_rate where ");
				hql.append(" organization_code = '" + orgCode + "'");
				hql.append(" and against_currency = '" + agaCurr + "'");
				hql.append(" and source_currency = '" + srcCurr + "'");

				if (null == dateCurr) {
					hql.append(" and begin_date <= ( ");
					hql.append(" select max(begin_date) from erp.bu_exchange_rate where ");
					hql.append(" organization_code = '" + orgCode + "'");
					hql.append(" and against_currency = '" + agaCurr + "'");
					hql.append(" and source_currency = '" + srcCurr + "'");
					hql.append(" and begin_Date <= sysdate ) ");
				} else {
					String bDate = DateUtils.format(dateCurr, DateUtils.C_DATA_PATTON_YYYYMMDD);
					hql.append("and begin_date <= To_Date('");
					hql.append(bDate);
					hql.append("','");
					hql.append(DateUtils.C_DATA_PATTON_YYYYMMDD);
					hql.append(" ') ");
				}
				hql.append(" Order by begin_date desc ");
				return session.createSQLQuery(hql.toString()).addEntity(BuExchangeRate.class).list();
			}
		});
		if ((null != result) && (result.size() > 0))
			bxr = result.get(0);
		return bxr;
	}
	
	/**
	 * 依據品牌、最後更新日期查詢
	 * 
	 * @param conditionMap
	 * @return List<BuExchangeRate>
	 */
	public List<BuExchangeRate> findBuExchangeRateListByProperty(HashMap conditionMap) {
		
		final String dataDate = (String) conditionMap.get("actualDataDateEnd");
		List<BuExchangeRate> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    
			        StringBuffer hql = new StringBuffer("select model from BuExchangeRate as model ");
			        
			        if(StringUtils.hasText(dataDate)){
					//hql.append("and to_char(model.lastUpdateDate, 'YYYY/MM/DD') = :dataDate ");
					hql.append(" where to_char (model.lastUpdateDate, 'yyyymmdd') = :dataDate ");
				}
				log.info("ExchangeRate Hql:"+hql);
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