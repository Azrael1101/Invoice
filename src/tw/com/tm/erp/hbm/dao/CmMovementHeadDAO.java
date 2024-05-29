package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.utils.DateUtils;

public class CmMovementHeadDAO extends BaseDAO<CmMovementHead>{
	
	public List getWhNoSq(){
		 return getHibernateTemplate().executeFind(
				new HibernateCallback(){
					public Object doInHibernate(Session session) 
						throws HibernateException,SQLException{
						StringBuffer hql = new StringBuffer();
						hql.append("select ERP.CM_MOVEMENT_WH_NO.nextval from dual");
						System.out.println(hql.toString());
						Query query = session.createSQLQuery(hql.toString());
						return query.list();
					}
				}
		); 
	}
	
	public List findScheduleOrders(String schedule,Date closeDate,String startHours,String endHours) throws Exception{
		
		
        final String date = DateUtils.format(closeDate);
        final String argStartHours = startHours;
        final String argEndHours = endHours;
		System.out.println("cm轉型後:"+DateUtils.format(closeDate));
		
		List<CmMovementHead> re = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from CmMovementHead as model where 1=1 ");
						

						
							hql.append(" and model.status = 'FINISH' ");

						
                       
							hql.append(" and model.lastUpdateDate between to_date('"+date+" "+argStartHours+"','yyyy-mm-dd HH24:MI:SS') and "+"to_date('"+date+" "+argEndHours+"','yyyy-mm-dd HH24:MI:SS')");

						
                        hql.append(" and model.orderTypeCode in ('RDW','RWD') order by lastUpdateDate desc ");

						System.out.println("時間比較:"+hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

						
						return query.list();
					}
				});

		return re;
		
		
	}

}
