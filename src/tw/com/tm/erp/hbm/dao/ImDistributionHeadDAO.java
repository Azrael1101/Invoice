package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;

public class ImDistributionHeadDAO extends BaseDAO {

	public List<ImDistributionHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImDistributionHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImDistributionHead as model where 1=1 ");
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.orderNo >= :startOrderNo ");

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.orderNo <= :endOrderNo ");
				
				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");				

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				hql.append(" order by orderTypeCode");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));
				
				if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));

				return query.list();
			}
		});

		return re;
	}
}
