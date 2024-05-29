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
import tw.com.tm.erp.hbm.bean.BuGoalItemDiscount;

public class BuGoalItemDiscountDAO  extends BaseDAO{

	private static final Log log = LogFactory.getLog(BuGoalItemDiscountDAO.class);

	public List<BuGoalItemDiscount> findAll() {
		log.debug("finding all BuGoalItemDiscount instances");
		try {
			String queryString = "from BuGoalItemDiscount";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	public List<BuGoalItemDiscount> find(HashMap conditionMap) {

		final String itemdiscount = (String) conditionMap.get("itemdiscount");
		final String discount = (String) conditionMap.get("discount");
		


		List<BuGoalItemDiscount> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer("select model from BuGoalItemDiscount as model");
						hql.append(" where model.discountId != null");

						if (StringUtils.hasText(itemdiscount))
							hql.append(" and model.itemdiscount like :itemdiscount");

						if (StringUtils.hasText(discount))
							hql.append(" and model.discount like :discount");



						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						if (StringUtils.hasText(itemdiscount))
							query.setString("itemdiscount", "%" + itemdiscount + "%");

						if (StringUtils.hasText(discount))
							query.setString("discount", "%" + discount + "%");

			

						return query.list();
					}
				});

		return result;
	}





	public BuGoalItemDiscount findByitemdiscount(Double itemdiscount) {
		StringBuffer hql = new StringBuffer("from BuGoalItemDiscount as model ");
		hql.append("where model.itemdiscount = ? ");
		List<BuGoalItemDiscount> lists = getHibernateTemplate().find(hql.toString(),
				new Object[] { itemdiscount });
		return (lists != null && lists.size() > 0 ? lists.get(0) : null);
	}

	
}
