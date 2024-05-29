package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.BumOrganizationTree;
import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;

public class BumOrganizationTreeDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BumOrganizationTreeDAO.class);
	
	
	/**
	 * 查出所有組織
	 * @return
	 */
	public List<BumOrganizationTree> findAllBumOrganizationTree(){
		List<BumOrganizationTree> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from BumOrganizationTree as model where 1=1 order by sysSno");
				Query query = session.createQuery(hql.toString());
				return query.list();
			}
		});
		return result;
	}
}
