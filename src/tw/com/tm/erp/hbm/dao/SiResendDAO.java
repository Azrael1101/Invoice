package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SiResendId;

public class SiResendDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(SiUsersGroupDAO.class);
	
	public boolean save(SiResend transientInstance) {
		log.info("saving SiResend instance");
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
	
	public boolean update(SiResend transientInstance) {
		log.debug("updating SiResend instance");
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
	
	public SiResend findById(SiResendId id){
			log.debug("SiResend findById:");
			try {
				SiResend instance = (SiResend)getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.SiResend", id);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
	}	
}
