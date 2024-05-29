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
import tw.com.tm.erp.hbm.bean.BuShopTarget;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

public class BuShopTargetDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuShopTargetDAO.class);

	public List isRepeat(String shopCode, String year, String month) {
		log.debug("isRepeat BuShopTarget instance ");
		try {
			StringBuffer biffer = new StringBuffer();
			biffer.append("from BuShopTarget as model \n").append("where model.buShop.shopCode = ? \n").append("and model.year = ? \n")
					.append("and model.month = ? ");
			Query queryObject = getSession().createQuery(biffer.toString());
			queryObject.setParameter(0, shopCode);
			queryObject.setParameter(1, year);
			queryObject.setParameter(2, month);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<BuShopTarget> find(HashMap findObjs) {
		log.info("BuShopTarget.find");

		final HashMap fos = findObjs;

		List<BuShopTarget> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from BuShopTarget as model ");
				if (StringUtils.hasText((String) fos.get("shopCode")))
					hql.append(" where model.buShop.shopCode = :shopCode ");

				if (StringUtils.hasText((String) fos.get("year")))
					hql.append(" and model.year = :year ");

//				if (StringUtils.hasText((String) fos.get("month")))
//					hql.append(" and model.month = :month ");

				hql.append(" order by TO_NUMBER(model.month)");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("shopCode")))
					query.setString("shopCode", (String) fos.get("shopCode"));

				if (StringUtils.hasText((String) fos.get("year")))
					query.setString("year", (String) fos.get("year"));

//				if (StringUtils.hasText((String) fos.get("month")))
//					query.setString("month", (String) fos.get("month"));

				return query.list();
			}
		});

		return re;
	}
}
