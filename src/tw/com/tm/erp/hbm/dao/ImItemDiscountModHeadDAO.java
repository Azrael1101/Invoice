/*Local*/

package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemDiscountModHead;

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

public class ImItemDiscountModHeadDAO extends BaseDAO {
	public List<ImItemDiscountModHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<ImItemDiscountModHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemDiscountModHead as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("vipTypeCode")))
					hql.append(" and model.vipTypeCode = :vipTypeCode ");

				if (StringUtils.hasText((String) fos.get("itemDiscountType")))
					hql.append(" and model.itemDiscountType = :itemDiscountType ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");
				
				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");
				
				if (StringUtils.hasText((String) fos.get("lastUpdateDate")))
					hql.append(" and model.lastUpdateDate = :lastUpdateDate ");



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
				
				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");
				
				if (StringUtils.hasText((String) fos.get("lastUpdateDate")))
					hql.append(" and model.lastUpdateDate = :lastUpdateDate ");


				//System.out.println( hql.toString() );
				return query.list();
			}
		});

		return re;
	}
	
	public ImItemDiscountModHead findById(Long headId) {
		return (ImItemDiscountModHead) findByPrimaryKey(ImItemDiscountModHead.class, headId);
	}
	
	public ImItemDiscountModHead findById(java.lang.String status) {
		
		final String itemCode1 = status;
		List<ImItemDiscountModHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemDiscountModHead as model where 1=1 ");
				hql.append(" and upper(model.status)  = :itemCode1 ");

				Query query = session.createQuery(hql.toString());
				query.setString("itemCode1", itemCode1);
				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
		}

	public static ImItemDiscountModHeadDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImItemDiscountModHeadDAO) ctx.getBean("imItemDiscountModHeadDAO");
	}
	public void update(Object updateObj) {
		// getHibernateTemplate().merge(updateObj);
		getHibernateTemplate().update(updateObj);
	}

}
