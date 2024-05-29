package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdDetail;



public class AdDetialDAO extends BaseDAO{
	 
	  private static final Log log = LogFactory.getLog(AdDetialDAO.class);
	  
	  public AdDetail findById(java.lang.Long id) {
			log.debug("getting AdDetail instance with id: " + id);
			try {
				AdDetail instance = (AdDetail) getHibernateTemplate()
						.get("tw.com.tm.erp.hbm.bean.AdDetail", id);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
		}
	  
	 
}
