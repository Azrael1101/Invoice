package tw.com.tm.erp.hbm.dao;

import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;

public class SiGroupDAO extends BaseDAO {

	public boolean save(SiGroup transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().save(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			result = false;
			re.printStackTrace();
			throw re;
		} 
		return result;
	}

	public boolean saveOrUpdate(SiGroup transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().saveOrUpdate(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			result = false;
			re.printStackTrace();
			throw re;
		} 
		return result;
	}
	
	public SiGroup findById(SiGroupId id) {
		try {
			SiGroup instance = (SiGroup) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.SiGroup", id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
