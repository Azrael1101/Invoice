package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
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

import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.TbcnWfAssignment;

 public class TbcnWfAssignmentDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuPurchaseHeadDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
    
	public TbcnWfAssignment findById(Long id) {
		log.debug("getting TbcnWfAssignment instance with id: " + id);
		try {
			TbcnWfAssignment instance = (TbcnWfAssignment) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.TbcnWfAssignment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/*public List<TbcnWfAssignment> findAssignmentId(long procId){
		
		String queryString = "from TBCN_WF_ASSIGNMENT";
	} */
	/*public List<TbcnWfAssignment> findAssignmentId(Long procId) {

		final Long process_Id = procId;
	
		List<TbcnWfAssignment> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    	String hql = " from TbcnWfAssignment G where G.processId=:processId ";
					Query query = session.createQuery(hql);
		
					query.setParameter("processId", process_Id);
					
					return query.list();
			    }
			}); 
		return result;
	}*/
    public TbcnWfAssignment findAssignmentId(Long procId) {
    	log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        StringBuffer hql = new StringBuffer("from TbcnWfAssignment as model ");
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        hql.append("where model.processId = ? ");
        hql.append("and model.currentStatus = ? ");
        log.info("============================");
        List<TbcnWfAssignment> lists = getHibernateTemplate().find(hql.toString(),
                new Object[] { procId ,"ACCEPTED"});
        return (lists != null && lists.size() > 0 ? lists.get(0) : null);
    }
	
    public List<TbcnWfAssignment> findAll() {
        log.debug("finding all TbcnWfAssignment instances");
        try {
            String queryString = "from TBCN_WF_ASSIGNMENT";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List<TbcnWfAssignment> find(HashMap findObjs) {
    	
    	final HashMap fos = findObjs;
    	
        final Long assignmentId = (Long) findObjs.get("assignmentId");
        final String activityId = (String) findObjs.get("activityId");
        final Long assigneeId = (Long) findObjs.get("assigneeId");
        final Date fromDate = (Date) findObjs.get("fromDate");
        final Date thruDate = (Date) findObjs.get("thruDate");

        
        

        List<TbcnWfAssignment> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from TBCN_WF_ASSIGNMENT as model");
                        //hql.append(" where model.HeadId != null");
                        
                        if (null!=(assignmentId))
                            hql.append(" and model.assignmentId like :assignmentId");

                        if (StringUtils.hasText(activityId))
                            hql.append(" and model.activityId like :activityId");

                        if (null!=(fromDate))
                            hql.append(" and model.fromDate like :fromDate");

                        if (null!=(thruDate))
                            hql.append(" and model.thruDate like :thruDate");

                    
                        Query query = session.createQuery(hql.toString());
                        query.setFirstResult(0);
                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
                        

                        if (null!=(assignmentId))
                            query.setString("assignmentId", "%" + assignmentId + "%");
                        
                        if (StringUtils.hasText(activityId))
                            query.setString("activityId", "%" + activityId + "%");
                        
                        if (null!=(fromDate))
                            query.setString("fromDate", "%" + fromDate + "%");
                        
                        if (null!=(thruDate))
                            query.setString("thruDate", "%" + thruDate + "%");
                 

                        return query.list();
                    }
                });

        return result;
    }
}
