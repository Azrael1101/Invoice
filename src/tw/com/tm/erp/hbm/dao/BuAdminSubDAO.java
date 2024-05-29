package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuAdminSub;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuShop;

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

public class BuAdminSubDAO extends BaseDAO 
{
	private static final Log log = LogFactory.getLog(BuAdminSubDAO.class);

	public List<BuAdminSub> findCity() {
		try{
		 List<BuAdminSub> result = getHibernateTemplate().executeFind(
	             new HibernateCallback() {
	                 public Object doInHibernate(Session session)
	                         throws HibernateException, SQLException {

	                     StringBuffer hql = new StringBuffer("select b from BuAdminSub b where b.headId=(select max(headId) from BuAdminSub where city=b.city) order by city");
	                     Query query = session.createQuery(hql.toString());
	                     return query.list();
	                 }
	             });
		 log.info(result.size());
		 return result;
		}catch (RuntimeException re) {
				log.error("find by property name failed", re);
				throw re;
		}
		 
	}
	
    public List<BuAdminSub> findByArea(String city,String area) {

    	final String city_arg = city;
    	final String area_area = area;

    	List<BuAdminSub> result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {

    			StringBuffer hql = new StringBuffer(
    				"select model from BuAdminSub as model where 1=1 ");
    			if(StringUtils.hasText(city_arg))
    				hql.append(" and model.city = :city");
    			if(StringUtils.hasText(area_area))    
    				hql.append(" and model.area = :area");
    			
    			hql.append(" order by model.headId");

    			Query query = session.createQuery(hql.toString());

    			if(StringUtils.hasText(city_arg))
    				query.setString("city", city_arg);
    			if(StringUtils.hasText(area_area))    
    				query.setString("area", area_area);
    			return query.list();
    		    }
    		});
    	return result;
        }


}