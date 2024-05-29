package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import tw.com.tm.erp.hbm.bean.EtlEventViewTable;
import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;

public class EtlEventViewTableDAO extends BaseDAO {
	
	public List<EtlEventViewTable> findUnprocessedEtlEventViewTable(){
		List<EtlEventViewTable> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from EtlEventViewTable as model where 1=1 ");
				hql.append(" and model.status = :status order by model.sysSno");
				Query query = session.createQuery(hql.toString());
				query.setCharacter("status", '1');
				return query.list();
			}
		});
		return result;
	}

}
