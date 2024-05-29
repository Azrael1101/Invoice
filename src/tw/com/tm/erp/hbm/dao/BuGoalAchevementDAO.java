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
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.BuLocation;

public class BuGoalAchevementDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuGoalAchevementDAO.class);

    public List<BuGoalAchevement> findAll() {
	log.debug("finding all BuGoalAchevement instances");
	try {
	    String queryString = "from BuGoalAchevement";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    public List<BuGoalAchevement> find(HashMap conditionMap) {

	final String achevement = (String) conditionMap.get("achevement");
	final String discount = (String) conditionMap.get("discount");
	final String bonus = (String) conditionMap.get("bonus");
	

	List<BuGoalAchevement> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from BuGoalAchevement as model");
			hql.append(" where model.achevement_Id != null");
			

			if (StringUtils.hasText(achevement))
			    hql.append(" and model.achevement like :achevement");

			if (StringUtils.hasText(discount))
			    hql.append(" and model.discount like :discount");

			if (StringUtils.hasText(bonus))
			    hql.append(" and model.bonus like :bonus");
			
		

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

			if (StringUtils.hasText(achevement))
			    query.setString("achevement", "%" + achevement + "%");
			
			if (StringUtils.hasText(discount))
			    query.setString("discount", "%" + discount + "%");
			
			if (StringUtils.hasText(bonus))
			    query.setString("bonus", "%" + bonus + "%");
		

			return query.list();
		    }
		});

	return result;
    }

    public BuGoalAchevement findByLocationName(Double achevement) {

    	StringBuffer hql = new StringBuffer("from BuGoalAchevement as model ");
    	hql.append("where model.achevement = ? ");
    	List<BuGoalAchevement> lists = getHibernateTemplate().find(hql.toString(),
    		new Object[] { achevement });
    	return (lists != null && lists.size() > 0 ? lists.get(0) : null);
        }
  
}