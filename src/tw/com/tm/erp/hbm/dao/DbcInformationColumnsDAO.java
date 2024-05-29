package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.DbcInformationColumns;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.service.OmmChannelTXFService;

public class DbcInformationColumnsDAO extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(DbcInformationColumnsDAO.class);
	
	public List<DbcInformationColumns> findDbcInformationColumnsByTableName(String tableName){
		

		StringBuffer hql = new StringBuffer("from DbcInformationColumns as model where 1 = 1");
		hql.append(" and model.tableName = ?");
		List<DbcInformationColumns> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { tableName});

		return result;
	}
	
	public List<DbcInformationColumns> findPageLine(String tableName, int startPage, int pageSize){
		
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<DbcInformationColumns> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from DbcInformationColumns as model where 1=1 ");
				if (!"".equals(tableName))
					hql.append(" and model.tableName = :tableName order by columnIndex");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (!"".equals(tableName))
					query.setString("tableName", tableName);
				return query.list();
			}
		});
		log.info("dbcInformationColumns.tableName:" + tableName);
		log.info("dbcInformationColumns.size:" + result.size());
		
		return result;
	}
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param tableName
	 * @return Long
	 */
	public Long findPageLineMaxIndex(String tableName) {

		Long lineMaxIndex = new Long(0);
		List<DbcInformationTables> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from DbcInformationTables as model where 1=1 ");
				if (!"".equals(tableName))
					hql.append(" and model.tableName = :tableName");
				Query query = session.createQuery(hql.toString());
				if (!"".equals(tableName))
					query.setString("tableName", tableName);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			List<DbcInformationColumns> dbcInformationColumnsList = result.get(0).getDbcInformationColumnsList();
			if (dbcInformationColumnsList != null && dbcInformationColumnsList.size() > 0) {
				lineMaxIndex = dbcInformationColumnsList.get(dbcInformationColumnsList.size() - 1).getColumnIndex();
			}
		}
		return lineMaxIndex;
	}

}
