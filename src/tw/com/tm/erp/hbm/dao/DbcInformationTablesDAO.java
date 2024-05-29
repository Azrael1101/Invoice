package tw.com.tm.erp.hbm.dao;

import java.util.List;

import tw.com.tm.erp.hbm.bean.DbcInformationTables;

public class DbcInformationTablesDAO extends BaseDAO{
	
	public DbcInformationTables findDbcInformationTablesByIdentification(String tableName) {
		
		StringBuffer hql = new StringBuffer("from DbcInformationTables as model where 1 = 1");
		hql.append(" and model.tableName = ?");
		List<DbcInformationTables> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { tableName});

		return (result != null && result.size() > 0 ? result.get(0) : null);
		
	}

}
