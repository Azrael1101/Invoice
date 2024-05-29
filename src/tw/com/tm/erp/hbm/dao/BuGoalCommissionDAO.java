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
import tw.com.tm.erp.hbm.bean.BuGoalCommission;

public class BuGoalCommissionDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(BuGoalCommissionDAO.class);

	public List<BuGoalCommission> findAll() {
		log.debug("finding all BuGoalCommission instances");
		try {
			String queryString = "from BuGoalCommission";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	public List<BuGoalCommission> find(HashMap conditionMap) {

		final String commissionType = (String) conditionMap.get("commissionType");
		final String category02 = (String) conditionMap.get("category02");
		final String commissionRate = (String) conditionMap.get("commissionRate");


		List<BuGoalCommission> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer("select model from BuGoalCommission as model");
						hql.append(" where model.typeId != null");

						if (StringUtils.hasText(commissionType))
							hql.append(" and model.commissionType like :commissionType");

						if (StringUtils.hasText(category02))
							hql.append(" and model.category02 like :category02");

						if (StringUtils.hasText(commissionRate))
							hql.append(" and model.commissionRate like :commissionRate");



						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						if (StringUtils.hasText(commissionType))
							query.setString("commissionType", "%" + commissionType + "%");

						if (StringUtils.hasText(category02))
							query.setString("category02", "%" + category02 + "%");

						if (StringUtils.hasText(commissionRate))
							query.setString("commissionRate", "%" + commissionRate + "%");



						return query.list();
					}
				});

		return result;
	}





	public BuGoalCommission findBycommissionType(String commissionType) {
		StringBuffer hql = new StringBuffer("from BuGoalCommission as model ");
		hql.append("where model.commissionType = ? ");
		List<BuGoalCommission> lists = getHibernateTemplate().find(hql.toString(),
				new Object[] { commissionType });
		return (lists != null && lists.size() > 0 ? lists.get(0) : null);
	}



}
