package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
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
import tw.com.tm.erp.hbm.bean.BuGoalCommissionException;

public class BuGoalCommissionExceptionDAO extends BaseDAO{
	private static final Log log = LogFactory.getLog(BuGoalCommissionExceptionDAO.class);

	public List<BuGoalCommissionException> findAll() {
		log.debug("finding all BuGoalCommissionException instances");
		try {
			String queryString = "from BuGoalCommissionException";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	public List<BuGoalCommissionException> find(HashMap conditionMap) {

		final String commissionType = (String) conditionMap.get("commissionType");
		final String shopCode = (String) conditionMap.get("shopCode");
		final String itemBrand = (String) conditionMap.get("itemBrand");
		final String commissionRate = (String) conditionMap.get("commissionRate");


		List<BuGoalCommissionException> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer("select model from BuGoalCommissionException as model");
						hql.append(" where model.exceptionId != null");

						if (StringUtils.hasText(commissionType))
							hql.append(" and model.commissionType like :commissionType");

						if (StringUtils.hasText(shopCode))
							hql.append(" and model.shopCode like :shopCode");
						
						if (StringUtils.hasText(itemBrand))
							hql.append(" and model.itemBrand like :itemBrand");
						
						if (StringUtils.hasText(commissionRate))
							hql.append(" and model.commissionRate like :commissionRate");



						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						if (StringUtils.hasText(commissionType))
							query.setString("commissionType", "%" + commissionType + "%");

						if (StringUtils.hasText(shopCode))
							query.setString("shopCode", "%" + shopCode + "%");
						
						if (StringUtils.hasText(itemBrand))
							query.setString("itemBrand", "%" + itemBrand + "%");

						if (StringUtils.hasText(commissionRate))
							query.setString("commissionRate", "%" + commissionRate + "%");



						return query.list();
					}
				});

		return result;
	}





	public BuGoalCommissionException findBycommissionType(String commissionType , String shopCode) {
		StringBuffer hql = new StringBuffer("from BuGoalCommissionException as model ");
		hql.append("where model.commissionType = ? ");
		List<BuGoalCommissionException> lists = getHibernateTemplate().find(hql.toString(),
				new Object[] { commissionType });
		return (lists != null && lists.size() > 0 ? lists.get(0) : null);
	}

}
