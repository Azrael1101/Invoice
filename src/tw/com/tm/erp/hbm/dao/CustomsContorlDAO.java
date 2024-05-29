package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.BuSupplierMod;
import tw.com.tm.erp.hbm.bean.CustomsContorl;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;

public class CustomsContorlDAO extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(CustomsContorl.class);
	public CustomsContorl findById(Long id) {
		log.debug("getting ERP.CustomsContorl instance with id: " + id);
		try {
			CustomsContorl instance = (CustomsContorl) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.CustomsContorl", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public List findAll() {
		log.debug("finding all CustomsContorl instances");
		try {
			String queryString = "from CustomsContorl";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 */
	public List find() {

		StringBuffer hql = new StringBuffer("from ERP.CustomsContorl where 1 = 1 ");
		List<CustomsContorl> result = getHibernateTemplate().find(hql.toString(),
				new Object[] {  });

		return result;
	}
	
	public List findCC() throws Exception {
		final String nativeSql = "select * from ERP.CUSTOMS_CONTORL";
		List<CustomsContorl> result = (List<CustomsContorl>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(nativeSql);
				
				return query.list();
			}
		});
		return result;
	}
	
	public List<CustomsContorl> findByProperty(String orderTypeCode){
		log.debug("finding SiUsersGroup instance with orderTypeCode: " + orderTypeCode);
		try {
			return (List<CustomsContorl>)getHibernateTemplate().find("from ERP.CustomsContorl as model where model.orderTypeCode=?", orderTypeCode);
		} catch (RuntimeException re) {
			log.error("find by brandCode, employeeCode failed", re);
			throw re;
		}
	}
	
	
}
