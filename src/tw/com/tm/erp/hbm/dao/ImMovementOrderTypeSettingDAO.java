package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.hbm.bean.ImMovementOrderTypeSetting;

public class ImMovementOrderTypeSettingDAO extends BaseDAO{

	private static final Log log = LogFactory.getLog(ImMovementOrderTypeSettingDAO.class);
	
	public static final String DESCRIPTION = "description";

	public static final String ENABLE = "enable";

	public static final String CREATED_BY = "createdBy";

	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	
	protected void initDao() {
	}
	
	public void save(ImMovementOrderTypeSetting imMovementOrderTypeSetting) {
		log.debug("saving ImMovementOrderTypeSetting instance");
		try {
			getHibernateTemplate().save(imMovementOrderTypeSetting);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(ImMovementOrderTypeSetting imMovementOrderTypeSetting) {
		try {
			getHibernateTemplate().update(imMovementOrderTypeSetting);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void saveOrUpdate(ImMovementOrderTypeSetting imMovementOrderTypeSetting) {
		if(imMovementOrderTypeSetting.getOrderTypeCode() == null || "".equals(imMovementOrderTypeSetting.getOrderTypeCode())) {
			save(imMovementOrderTypeSetting);
		} else {
			update(imMovementOrderTypeSetting);
		}
	}
	

	public List<ImMovementOrderTypeSetting> findAll() {
		log.debug("finding all ImMovementOrderTypeSetting instances");
		try {
			String queryString = "from ImMovementOrderTypeSetting";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public ImMovementOrderTypeSetting findById(java.lang.String orderTypeCode) {
		log.debug("getting ImMovementOrderTypeSetting instance with orderTypeCode: " + orderTypeCode);
		try {
			ImMovementOrderTypeSetting instance = (ImMovementOrderTypeSetting) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImMovementOrderTypeSetting", orderTypeCode);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImMovementOrderTypeSetting instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ImMovementOrderTypeSetting as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}
	
	public ImMovementOrderTypeSetting findByOrderTypeCode(String orderTypeCode) {
		StringBuffer hql = new StringBuffer("from ImMovementOrderTypeSetting as model ");
		hql.append("where model.orderTypeCode = ? ");
		List<ImMovementOrderTypeSetting> lists = getHibernateTemplate().find(hql.toString(),
				new Object[] { orderTypeCode });
		return (lists != null && lists.size() > 0 ? lists.get(0) : null);
	}
	
	
	
}
