package tw.com.tm.erp.hbm.dao;

import java.util.List;
import java.util.HashMap;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
//import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
//import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
//import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
//import tw.com.tm.erp.hbm.bean.SiGroupObject;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.SiGroupObject;
import tw.com.tm.erp.hbm.service.SiGroupObjectService;

public class SiGroupObjectDAO extends BaseDAO {
protected void initDao() {
		// do nothing
	}

public List getBrandUserObjectManager(final String brandCode,final String loginName) { //由User身分找全部資訊	
	List re = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    StringBuffer hql = new StringBuffer("select distinct sigo.id.groupCode,sigo.id.functionCode,sigo.id.objectCode,sigo.controlType,sifo ");                        
                    hql.append("from SiGroupObject as sigo,SiUsersGroup as siug,SiFunctionObject as sifo ");
                	hql.append("where sigo.id.brandCode=siug.id.brandCode and sigo.id.groupCode=siug.id.groupCode ");
                    //hql.append("and siug.id.brandCode='"+brandCode+"' ");                        
					//hql.append("and siug.id.employeeCode='"+loginName+"' ");
                    hql.append("and siug.id.brandCode=:brandCode ");                        
					hql.append("and siug.id.employeeCode=:loginName ");
					hql.append("and sigo.id.functionCode=sifo.siFunction.functionCode "); 
					hql.append("and sigo.id.objectCode=sifo.objectCode "); 
                    hql.append("order by sigo.id.functionCode,sigo.id.objectCode");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameter("loginName",  loginName);
                    return query.list();
                }
            });
    return re;
}

public List getUserGroup(final String brandCode,final String loginName) {//由User身分找Group
	List groups = getHibernateTemplate().executeFind(
	        new HibernateCallback() {
	            public Object doInHibernate(Session session)
	                    throws HibernateException, SQLException {
	            	StringBuffer hql = new StringBuffer("select siug.id.groupCode ");                        
	                hql.append("from SiUsersGroup as siug ");      
	                hql.append("where siug.id.brandCode=:brandCode ");                        
					hql.append("and siug.id.employeeCode=:loginName ");
	                Query query = session.createQuery(hql.toString());
	                query.setParameter("brandCode",  brandCode);
	                query.setParameter("loginName",  loginName);
	                return query.list();
	            }
	        });
	return groups;
	}

public List getBrandUserObject(final String brandCode,final String loginName) {	//由User身分找groupCode,functionCode,objectCode組
	List re = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    StringBuffer hql = new StringBuffer("select distinct sigo.id.groupCode,sigo.id.functionCode,sigo.id.objectCode ");                        
                    hql.append("from SiGroupObject as sigo,SiUsersGroup as siug ");
                	hql.append("where sigo.id.brandCode=siug.id.brandCode and sigo.id.groupCode=siug.id.groupCode ");                     
                    hql.append("and siug.id.brandCode=:brandCode ");                        
					hql.append("and siug.id.employeeCode=:loginName ");					
                    hql.append("order by sigo.id.functionCode,sigo.id.objectCode");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameter("loginName",  loginName);
                    return query.list();
                }
            });
    return re;
}	
public List getFunctionObject(final String brandCode,final String groupCode) {//由brand,group找functionCode,objectCode組
	List objects = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                	StringBuffer hql = new StringBuffer("select distinct sigo.id.functionCode,sigo.id.objectCode ");                        
                    hql.append("from SiGroupObject as sigo ");      
                    hql.append("where sigo.id.brandCode=:brandCode ");                        
					hql.append("and sigo.id.groupCode=:groupCode ");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameter("groupCode",  groupCode);
                    return query.list();
                }
            });
	 return objects;
}	
public List getFunctionList(final String brandCode,final List groupCode) {//由brand,group list找functionCode,objectCode組
	List functions = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                	StringBuffer hql = new StringBuffer("select distinct sigo.id.functionCode ");                        
                    hql.append("from SiGroupObject as sigo ");      
                    hql.append("where sigo.id.brandCode=:brandCode ");                        
					hql.append("and sigo.id.groupCode in (:groupCode) ");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameterList("groupCode",  groupCode);
                    return query.list();
                }
            });
	 return functions;
}	
public List getObjectControlType(final String brandCode,final List groupCode,final String functionCode,final String objectCode) {//找同一group,functionCode,objectCode組的ControlType
	List objects = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                	StringBuffer hql = new StringBuffer("select distinct sigo.controlType ");                        
                    hql.append("from SiGroupObject as sigo ");      
                    hql.append("where sigo.id.brandCode=:brandCode ");                        
					hql.append("and sigo.id.groupCode in (:groupCode) ");
					hql.append("and sigo.id.functionCode=:functionCode ");
					hql.append("and sigo.id.objectCode=:objectCode ");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameterList("groupCode",  groupCode);
                    query.setParameter("functionCode",  functionCode);
                    query.setParameter("objectCode",  objectCode);                 
                    return query.list();
                }
            });
	 return objects;
}
public List getGroupObjectSize(final String brandCode,final String loginName,final String functionCode,final String objectCode) { //由User與FunctionObject找group size
	List objects = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                	StringBuffer hql = new StringBuffer("select distinct sigo.id.groupCode ");                        
					hql.append("from SiGroupObject as sigo,SiUsersGroup as siug ");
                	hql.append("where sigo.id.brandCode=siug.id.brandCode and sigo.id.groupCode=siug.id.groupCode ");                   
                    hql.append("and siug.id.brandCode=:brandCode ");                        
					hql.append("and siug.id.employeeCode=:loginName ");
					hql.append("and sigo.id.functionCode=:functionCode ");                        
					hql.append("and sigo.id.objectCode=:objectCode ");
                    Query query = session.createQuery(hql.toString());
                    query.setParameter("brandCode",  brandCode);
                    query.setParameter("loginName",  loginName);
					query.setParameter("functionCode",  functionCode);
					query.setParameter("objectCode",  objectCode);
                    return query.list();
                }
            });
	 return objects;
}	
/**public List getBrandUserObjectManager(final String brandCode,final String loginName) {
        List re = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        //StringBuffer hql = new StringBuffer("select distinct sigo.id.functionCode,sigo.id.objectCode,sifo ");                        
                        //hql.append("from SiGroupObject as sigo,SiUsersGroup as siug,SiFunctionObject as sifo ");
                    	StringBuffer hql = new StringBuffer("select distinct sigo.id.functionCode,sigo.id.objectCode,sigo.controlType ");                        
                        hql.append("from SiGroupObject as sigo,SiUsersGroup as siug ");      
                        hql.append("where sigo.id.brandCode=siug.id.brandCode and sigo.id.groupCode=siug.id.groupCode ");
                        //hql.append("and siug.id.brandCode='"+brandCode+"' ");                        
						//hql.append("and siug.id.employeeCode='"+loginName+"' ");
                        hql.append("and siug.id.brandCode=:brandCode ");                        
						hql.append("and siug.id.employeeCode=:loginName ");
						//hql.append("and sigo.id.functionCode=sifo.id.functionCode "); 
						//hql.append("and sigo.id.objectCode=sifo.id.objectCode "); 
                        hql.append("order by sigo.id.functionCode,sigo.id.objectCode");
                        Query query = session.createQuery(hql.toString());
                        query.setParameter("brandCode",  brandCode);
                        query.setParameter("loginName",  loginName);
                        return query.list();
                    }
                });
        return re;
    }*/
}
