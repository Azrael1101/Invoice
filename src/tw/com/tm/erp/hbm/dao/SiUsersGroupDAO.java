package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;

public class SiUsersGroupDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SiUsersGroupDAO.class);
	
	public boolean save(SiUsersGroup transientInstance) {
		log.info("saving SiUsersGroup instance");
		boolean result = false;
		try {
			getHibernateTemplate().save(transientInstance);
			//log.info("save successful");
			result = true;
		} catch (RuntimeException re) {
			//log.info("save failed", re);
			result = false;
			throw re;
		} 
		return result;
	}
	
	public boolean update(SiUsersGroup transientInstance) {
		log.debug("updating SiUsersGroup instance");
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			//log.info("update successful");
			result = true;
		} catch (RuntimeException re) {
			//log.info("update failed", re);
			result = false;
			throw re;
		}
		return result;
	}
	
	public SiUsersGroup findById(Long id){
			log.debug("SiUsersGroupId findById:");
			try {
				SiUsersGroup instance = (SiUsersGroup)getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.SiUsersGroup", id);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
	}
	
	/**
	 * find page line
	 * 
	 * @param groupCode
	 * @param brandCode
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<SiUsersGroup> findPageLine(final String brandCode ,final String groupCode, int startPage, int pageSize) {
		log.info("SiUsersGroupDAO.findPageLine groupCode=" + brandCode + "brandCode="+ groupCode + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<SiUsersGroup> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from SiUsersGroup as model where ");
					hql.append("model.id.brandCode = '"+ brandCode +"' and model.id.groupCode = '"+groupCode+"' order by model.indexNo");
					Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				log.info( "Query Over");
				return query.list();
			}
		});
		return re;
	}
	
	public Long findPageLineMaxIndex(final String brandCode , final String groupCode){
		Long lineMaxIndex = new Long(0);
		List<SiUsersGroup> result = getHibernateTemplate().executeFind(new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from SiUsersGroup as line ");
				    hql.append("where line.id.brandCode = :brandCode and line.id.groupCode = :groupCode order by indexNo");
			Query query = session.createQuery(hql.toString());
			    query.setString("brandCode", brandCode);
			    query.setString("groupCode", groupCode);
			return query.list();
		    }
		});
		if (result != null && result.size() > 0) {
			lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
		}
		return lineMaxIndex;
	}
	
	public List<SiUsersGroup> findByProperty(String brandCode, String employeeCode){
		log.debug("finding SiUsersGroup instance with brandCode: " + brandCode + ", employeeCode: " + employeeCode);
		try {
			String queryString = "from SiUsersGroup as model where model.brandCode=? and model.employeeCode=?";
			return (List<SiUsersGroup>)getHibernateTemplate().find(queryString, new Object[]{brandCode, employeeCode});
		} catch (RuntimeException re) {
			log.error("find by brandCode, employeeCode failed", re);
			throw re;
		}
	}
	
	public List<SiUsersGroup> findByProperty(String employeeCode){
		log.debug("finding SiUsersGroup instance with employeeCode: " + employeeCode);
		try {
			return (List<SiUsersGroup>)getHibernateTemplate().find("from SiUsersGroup as model where model.employeeCode=?", employeeCode);
		} catch (RuntimeException re) {
			log.error("find by brandCode, employeeCode failed", re);
			throw re;
		}
	}
	
}
