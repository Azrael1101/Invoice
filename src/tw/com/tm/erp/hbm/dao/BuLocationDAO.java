package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuLocation;

public class BuLocationDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuLocationDAO.class);

    public List<BuLocation> findAll() {
	log.debug("finding all BuLocation instances");
	try {
	    String queryString = "from BuLocation";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    public List<BuLocation> find(HashMap conditionMap) {

	final String locationName = (String) conditionMap.get("locationName");
	final String city = (String) conditionMap.get("city");
	final String area = (String) conditionMap.get("area");
	final String zipCode = (String) conditionMap.get("zipCode");

	List<BuLocation> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from BuLocation as model");
			hql.append(" where model.locationId != null");

			if (StringUtils.hasText(locationName))
			    hql.append(" and model.locationName like :locationName");

			if (StringUtils.hasText(city))
			    hql.append(" and model.city like :city");

			if (StringUtils.hasText(area))
			    hql.append(" and model.area like :area");
			
			if (StringUtils.hasText(zipCode))
			    hql.append(" and model.zipCode like :zipCode");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

			if (StringUtils.hasText(locationName))
			    query.setString("locationName", "%" + locationName + "%");
			
			if (StringUtils.hasText(city))
			    query.setString("city", "%" + city + "%");
			
			if (StringUtils.hasText(area))
			    query.setString("area", "%" + area + "%");
			
			if (StringUtils.hasText(zipCode))
			    query.setString("zipCode", "%" + zipCode + "%");

			return query.list();
		    }
		});

	return result;
    }

    /**
     * 依據品牌代號及專櫃代號，查詢啟用狀態之專櫃資料
     * 
     * @param locationName
     * @return BuShop
     */
    public BuLocation findByLocationName(String locationName) {

	StringBuffer hql = new StringBuffer("from BuLocation as model ");
	hql.append("where model.locationName = ? ");
	List<BuLocation> lists = getHibernateTemplate().find(hql.toString(),
		new Object[] { locationName });
	return (lists != null && lists.size() > 0 ? lists.get(0) : null);
    }
}