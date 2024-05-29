package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;
import tw.com.tm.erp.hbm.bean.ImItemCategoryNode;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;

public class ImItemCategoryNodeDAO extends BaseDAO{
	
	public ImItemCategoryNode findImItemCategoryNodeByCategoryUnique(String brandCode, String categoryLevelCode, String categoryNodeCode) {
		
		StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1 = 1");
		hql.append(" and model.brandCode = ?");
		hql.append(" and model.categoryLevelCode = ?");
		hql.append(" and model.categoryNodeCode = ?");
		List<ImItemCategoryNode> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, categoryLevelCode, categoryNodeCode});

		return (result != null && result.size() > 0 ? result.get(0) : null);
	
	}
	
	public List<ImItemCategoryNode> findAllImItemCategoryNodeByCategoryLevelCode(String brandCode, String categoryLevelCode){
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (brandCode != null && !"".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode");
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					hql.append(" and model.categoryLevelCode = :categoryLevelCode");
				hql.append(" order by model.sysSno");
				Query query = session.createQuery(hql.toString());
				if (brandCode != null && !"".equals(brandCode))
					query.setString("brandCode", brandCode);
				if (categoryLevelCode != null || "".equals(categoryLevelCode))
					query.setString("categoryLevelCode", categoryLevelCode);
				return query.list();
			}
		});
		return result;
	}

	public List<ImItemCategoryNode> findPageLineByParent(String brandCode, String categoryLevelCode, String categoryNodeCode, int startPage, int pageSize){
		
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (brandCode != null && !"".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode");
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					hql.append(" and model.pCategoryLevelCode = :categoryLevelCode");
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					hql.append(" and model.pCategoryNodeCode = :categoryNodeCode");
				hql.append(" order by model.columnIndex");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (brandCode != null && !"".equals(brandCode))
					query.setString("brandCode",brandCode);
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					query.setString("categoryLevelCode",categoryLevelCode);
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					query.setString("categoryNodeCode",categoryNodeCode);
				return query.list();
			}
		});
		
		return result;
	}
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param tableName
	 * @return Long
	 */
	public Long findPageLineMaxIndexByParent(String brandCode, String categoryLevelCode, String categoryNodeCode) {

		Long lineMaxIndex = new Long(0);
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (brandCode != null && !"".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode");
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					hql.append(" and model.pCategoryLevelCode = :categoryLevelCode");
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					hql.append(" and model.pCategoryNodeCode = :categoryNodeCode");
				hql.append(" order by model.columnIndex desc");
				Query query = session.createQuery(hql.toString());
				if (brandCode != null && !"".equals(brandCode))
					query.setString("brandCode",brandCode);
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					query.setString("categoryLevelCode",categoryLevelCode);
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					query.setString("categoryNodeCode",categoryNodeCode);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = (long) result.size();
		}
		return lineMaxIndex;
	}
	
public List<ImItemCategoryNode> findPageLineByOwn(String brandCode, String categoryLevelCode, String categoryNodeCode, int startPage, int pageSize){
		
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (brandCode != null && !"".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode");
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					hql.append(" and model.categoryLevelCode = :categoryLevelCode");
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					hql.append(" and model.categoryNodeCode = :categoryNodeCode");
				hql.append(" order by model.columnIndex");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (brandCode != null && !"".equals(brandCode))
					query.setString("brandCode",brandCode);
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					query.setString("categoryLevelCode",categoryLevelCode);
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					query.setString("categoryNodeCode",categoryNodeCode);
				return query.list();
			}
		});
		
		return result;
	}
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param tableName
	 * @return Long
	 */
	public Long findPageLineMaxIndexByOwn(String brandCode, String categoryLevelCode, String categoryNodeCode) {
		
		Long lineMaxIndex = new Long(0);
		List<ImItemCategoryNode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImItemCategoryNode as model where 1=1 ");
				if (brandCode != null && !"".equals(brandCode))
					hql.append(" and model.brandCode = :brandCode");
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					hql.append(" and model.categoryLevelCode = :categoryLevelCode");
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					hql.append(" and model.categoryNodeCode = :categoryNodeCode");
				hql.append(" order by model.columnIndex desc");
				Query query = session.createQuery(hql.toString());
				if (brandCode != null && !"".equals(brandCode))
					query.setString("brandCode",brandCode);
				if (categoryLevelCode != null && !"".equals(categoryLevelCode))
					query.setString("categoryLevelCode",categoryLevelCode);
				if (categoryNodeCode != null && !"".equals(categoryNodeCode))
					query.setString("categoryNodeCode",categoryNodeCode);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = (long) result.size();
		}
		return lineMaxIndex;
	}
	
}
