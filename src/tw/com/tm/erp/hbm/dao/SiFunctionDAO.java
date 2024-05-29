package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;

public class SiFunctionDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(SiFunctionDAO.class);
	
	public SiFunction getById( String functionCode ){
		log.debug("getting SiFunction instance with functionCode: " + functionCode);
		try {
			SiFunction instance = (SiFunction) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.SiFunction", functionCode);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
  }
}
