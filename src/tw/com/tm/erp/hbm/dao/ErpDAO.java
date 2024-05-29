package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CustomsConfiguration;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
public class ErpDAO  extends HibernateDaoSupport{

	
	
	
	public List getCustomsConfiguration(final String DF){
		
		return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer("from CustomsConfiguration as model where model.destinationFunction = :destinationFunction");
						Query query = session.createQuery(hql.toString());
						query.setString("destinationFunction", DF);
						return query.list();
					}
				}
		);
	}
	
	public List<ImMovementHead> findById(final Long id) {
		try {
			
			return getHibernateTemplate().executeFind(
					new HibernateCallback(){
						public Object doInHibernate(Session session) 
							throws HibernateException,SQLException{
							StringBuffer hql = new StringBuffer("from ImMovementHead as model where model.headId = :headId");
							Query query = session.createQuery(hql.toString());
							query.setLong("headId", id);
							return query.list();
						}
					}
			);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List findByBeanNId(String bean,final Long id) {
		try {
			String head = "";
			if(bean.indexOf("Head")==-1){
				head = bean.substring(0,bean.indexOf("Line")==-1?bean.indexOf("Item"):bean.indexOf("Line"));
				head = head+"Head";
				head = head.substring(0,1).toLowerCase() + head.substring(1);
			}else{
				head = "headId";
			}
			
			//System.out.println("!!!!!!!!!!!!!!!:"+head);
			final StringBuffer hql = new StringBuffer("from "+bean+" as model where model."+head+" = :head");
			//StringBuffer hql = new StringBuffer("from "+(bean.toString())+" as model where model.headId = :headId");
			System.out.println(hql.toString());
			
			return getHibernateTemplate().executeFind(
					new HibernateCallback(){
						public Object doInHibernate(Session session) 
							throws HibernateException,SQLException{
							Query query = session.createQuery(hql.toString());
							query.setLong("headId", id);
							return query.list();
						}
					}
			);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<ImMovementHead> findByTbNOrderNo(String tb,final String orderTypeCode,final String orderNo) {
		try {
			return getHibernateTemplate().executeFind(
					new HibernateCallback(){
						public Object doInHibernate(Session session) 
							throws HibernateException,SQLException{
							StringBuffer hql = new StringBuffer("from ImMovementHead where orderTypeCode = :orderTypeCode and orderNo = :orderNo");
							Query query = session.createQuery(hql.toString());
							query.setString("orderTypeCode", orderTypeCode);
							query.setString("orderNo", orderNo);
							return query.list();
						}
					}
			);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}