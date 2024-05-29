package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;

public class TmpAjaxSearchDataDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(TmpAjaxSearchDataDAO.class);
 
    
    public  TmpAjaxSearchData findByKey(final String timeScope, final String selectionData){   	
    	if (StringUtils.hasText(timeScope) && StringUtils.hasText(selectionData)){
    		List<TmpAjaxSearchData> result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("from TmpAjaxSearchData as model where 1=1 ");
		    			hql.append(" and timeScope = :timeScope");
		    			hql.append(" and selectionData = :selectionData");
		    			//log.info("findByKey.hql:"+	hql.toString());
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("timeScope", timeScope);
		    			query.setString("selectionData", selectionData);
		    			return query.list();		    
    		    	
    		    }
    		});
    		log.info("findByKey.size:"+	result.size());
    		if(result.size()>0)
    			return (TmpAjaxSearchData) result.get(0);
    		else
    			return null;
    	}else{
    		return null;
    	}
    }
    
    public List<TmpAjaxSearchData> find(final String timeScope, final String selectionData){
    	List<TmpAjaxSearchData> result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		    public Object doInHibernate(Session session)  throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("from TmpAjaxSearchData as model where 1=1 ");
    			if (StringUtils.hasText(timeScope))
    			    hql.append(" and timeScope = :timeScope");
    			
    			if (StringUtils.hasText(selectionData))
    			    hql.append(" and selectionData = :selectionData");
    			
    			hql.append(" order by id");
    			Query query = session.createQuery(hql.toString());

    			if (StringUtils.hasText(timeScope))
    			    query.setString("timeScope", timeScope);
    			
    			if (StringUtils.hasText(selectionData))
    				query.setString("selectionData", selectionData);
    			return query.list();
    		    }
    		});
    	log.info("findByTimeScope.timeScope:"+	timeScope);
    	log.info("findByTimeScope.size:"+result.size());
    	return result;
    }
    
    public List<TmpAjaxSearchData> findByTimeScope(final String timeScope){
    	List<TmpAjaxSearchData> result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		    public Object doInHibernate(Session session)  throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("from TmpAjaxSearchData as model where 1=1 ");
    			if (StringUtils.hasText(timeScope))
    			    hql.append(" and timeScope = :timeScope");
    			hql.append(" order by id");
    			Query query = session.createQuery(hql.toString());

    			if (StringUtils.hasText(timeScope))
    			    query.setString("timeScope", timeScope);
    			
    			return query.list();
    		    }
    		});
    	log.info("findByTimeScope.timeScope:"+	timeScope);
    	log.info("findByTimeScope.size:"+result.size());
    	return result;
    }
    
    
    public void deleteByTimeScope(final String timeScope){   	
    	if (StringUtils.hasText(timeScope)){
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		    			StringBuffer hql = new StringBuffer("delete from TmpAjaxSearchData as model where 1=1 ");
		    			hql.append(" and model.timeScope = :timeScope");
		    			Query query = session.createQuery(hql.toString());
		    			query.setString("timeScope", timeScope);
		    			
		    			return query.executeUpdate();
    		    	
    		    }
    		});
    	
    	}
		
    	
    }
    
    public void deleteByArray(final String[][] updateArray){   	
    	   	
    	if (updateArray.length >0){
    		
    		getHibernateTemplate().execute(new HibernateCallback() {
    		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		    	String timeScope =  updateArray[0][0];
	    			StringBuffer hql = new StringBuffer("delete from TmpAjaxSearchData as model where 1=1 ");
	    			hql.append(" and model.timeScope = :timeScope");
	    			Query query = session.createQuery(hql.toString());
	    			query.setString("timeScope", timeScope);
	    			
	    			return query.executeUpdate();
    		    	
    		    }
    		});
    	}
    }

}