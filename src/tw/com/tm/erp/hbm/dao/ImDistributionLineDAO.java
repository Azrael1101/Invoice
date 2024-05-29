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
import tw.com.tm.erp.hbm.bean.ImDistributionLine;

public class ImDistributionLineDAO extends BaseDAO {
	public List<ImDistributionLine> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImDistributionLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Long headId = (Long) fos.get("headId");

				StringBuffer hql = new StringBuffer("from ImDistributionLine as model where 1=1 ");
				if (StringUtils.hasText((String) fos.get("itemCode")))
					hql.append(" and model.itemCode = :itemCode ");

				if (StringUtils.hasText((String) fos.get("shopCode")))
					hql.append(" and model.shopCode = :shopCode ");

				if (null != headId)
					hql.append(" and model.imDistributionHead.headId = :headId ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("itemCode")))
					query.setString("itemCode", (String) fos.get("itemCode"));

				if (StringUtils.hasText((String) fos.get("shopCode")))
					query.setString("shopCode", (String) fos.get("shopCode"));

				if (null != headId)
					query.setLong("headId", headId);

				return query.list();
			}
		});

		return re;
	}
}
