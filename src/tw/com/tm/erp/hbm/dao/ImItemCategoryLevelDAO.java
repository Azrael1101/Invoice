package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;
import tw.com.tm.erp.hbm.bean.ImItemCategoryNode;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;

public class ImItemCategoryLevelDAO extends BaseDAO{

	public List<ImItemCategoryNode> findPageLine(String categoryLevelCode, int startPage, int pageSize){
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (categoryLevelCode != null)
					hql.append(" and model.oImItemCategoryLevel.categoryLevelCode = :categoryLevelCode order by model.categoryNodeCode");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (categoryLevelCode != null)
					query.setString("categoryLevelCode", categoryLevelCode);
				return query.list();
			}
		});
		
		return result;
	}
	
	public List<ImItemCategoryLevel> findAllImItemCategoryLevelByBrandCode(String brandCode){
		List<ImItemCategoryLevel> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryLevel as model where 1=1 ");
				if (brandCode != null || "".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode order by model.sysSno");
				Query query = session.createQuery(hql.toString());
				if (brandCode != null || "".equals(brandCode))
					query.setString("brandCode", brandCode);
				return query.list();
			}
		});
		return result;
	}
	
	public ImItemCategoryLevel findImItemCategoryLevelByUnique(String brandCode, String categoryLevelCode) {
		
		StringBuffer hql = new StringBuffer("from ImItemCategoryLevel as model where 1 = 1");
		hql.append(" and model.brandCode = ?");
		hql.append(" and model.categoryLevelCode = ?");
		List<ImItemCategoryLevel> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, categoryLevelCode});

		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}
