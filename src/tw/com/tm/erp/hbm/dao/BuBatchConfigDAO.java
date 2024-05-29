package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuCountry entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuCountry
 * @author MyEclipse Persistence Tools
 */

public class BuBatchConfigDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuBatchConfigDAO.class);

	
	

	public List findAll() {
		log.debug("finding all BuBatch instances");
		try {
			String queryString = "from BuBatchConfig";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}