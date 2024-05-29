package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class NativeQueryDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(NativeQueryDAO.class);

	/**
	 * 執行 Native SQL
	 * @param nativeSql
	 * @return
	 */
	public List executeNativeSql(String nativeSql) {
		log.info("NativeQueryDAO.executeNativeSql sql=" + nativeSql);
		final String sql = nativeSql;
		List result = null;
		result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				if( null != query ){
					return query.list();
				}
				else
					return null ;
			}
		});
		return result;
	}
	
	/**
	 * 執行 Native SQL 分頁
	 * @param nativeSql
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List executeNativeSql(String nativeSql, int startPage, int pageSize) {
		log.info("NativeQueryDAO.executeNativeSql sql=" + nativeSql);
		final String sql = nativeSql;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List result = null;
		result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if( null != query ){
					return query.list();
				}
				else
					return null ;
			}
		});
		return result;
	}
	
	/**
	 * 執行 Native Update SQL
	 * @param nativeSql
	 * @return
	 */
	public int executeNativeUpdateSql(String nativeSql) {
		log.info("NativeQueryDAO.executeNativeUpdateSql sql=" + nativeSql);
		final String sql = nativeSql;
		int result = (Integer)getHibernateTemplate().execute(new HibernateCallback() {
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				if(null != query){
				    return query.executeUpdate();
				}
				else
				    return 0 ;
			}
		});
		return result;
	}
}
