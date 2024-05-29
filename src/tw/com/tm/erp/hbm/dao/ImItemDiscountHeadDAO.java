/*Local*/

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
import tw.com.tm.erp.hbm.bean.ImItemDiscountHead;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItem entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItem
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountHeadDAO extends BaseDAO {
	public List<ImItemDiscountHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImItemDiscountHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemDiscountHead as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("vipTypeCode")))
					hql.append(" and model.vipTypeCode = :vipTypeCode ");

				if (StringUtils.hasText((String) fos.get("itemDiscountType")))
					hql.append(" and model.itemDiscountType = :itemDiscountType ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("vipTypeCode")))
					query.setParameter("vipTypeCode", fos.get("vipTypeCode"));

				if (StringUtils.hasText((String) fos.get("itemDiscountType")))
					query.setParameter("itemDiscountType", fos.get("itemDiscountType"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				//System.out.println( hql.toString() );
				return query.list();
			}
		});

		return re;
	}
	
	public ImItemDiscountHead findById(Long headId) {
		return (ImItemDiscountHead) findByPrimaryKey(ImItemDiscountHead.class, headId);
	}
}
