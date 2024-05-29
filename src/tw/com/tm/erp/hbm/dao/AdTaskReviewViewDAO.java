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

import tw.com.tm.erp.hbm.bean.AdTaskReviewView;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;

 public class AdTaskReviewViewDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(AdTaskReviewViewDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
    
    public List<AdTaskReviewView> findAll() {
        log.debug("finding all AdTaskReviewView instances");
        try {
            String queryString = "from Ad_Task_Review_View";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    
    public List<AdTaskReviewView> find(HashMap findObjs) {
    	
    	final HashMap fos = findObjs;
    	
        final String no = (String) findObjs.get("no");
        final String specInfo = (String) findObjs.get("specInfo");
        final String taskType = (String) findObjs.get("taskType");
        final String status = (String) findObjs.get("status");
        final Date countingDate = (Date) findObjs.get("countingDate");
        final String incharge = (String) findObjs.get("incharge");
        final Integer mon = (Integer) findObjs.get("mon");
        final Integer tue = (Integer) findObjs.get("tue");
        final Integer wed = (Integer) findObjs.get("wed");
        final Integer thu = (Integer) findObjs.get("thu");
        final Integer fri = (Integer) findObjs.get("fri");
        final Integer sat = (Integer) findObjs.get("sat");
        final Integer sun = (Integer) findObjs.get("sun");
        final Integer lineId = (Integer) findObjs.get("lineId");

        

        List<AdTaskReviewView> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from Ad_Task_Review_View as model");
                        hql.append(" where model.lineId != null");
                        
                     //   Date countingDate       = (Date)   fos.get("countingDate"); 
                        
                        if (StringUtils.hasText(no))
                            hql.append(" and model.no like :no");

                        if (StringUtils.hasText(specInfo))
                            hql.append(" and model.specInfo like :specInfo");

                        if (StringUtils.hasText(taskType))
                            hql.append(" and model.taskType like :taskType");

                        if (StringUtils.hasText(status))
                            hql.append(" and model.status like :status");

                        if (StringUtils.hasText(incharge))
                            hql.append(" and model.incharge like :incharge");
                        
                        if (null!=(mon))
                            hql.append(" and model.mon like :mon");

                        if (null!=(tue))
                            hql.append(" and model.tue like :tue");

                		if (null != fos.get(countingDate))
        					hql.append(" and model.countingDate = :countingDate ");
                        
                        if (null!=(wed))
                            hql.append(" and model.wed like :wed");

                        if (null!=(thu))
                            hql.append(" and model.thu like :thu");
                        
                        if (null!=(fri))
                            hql.append(" and model.fri like :fri");
                        
                        if (null!=(fri))
                            hql.append(" and model.fri like :fri");
                        
                        if (null!=(sat))
                            hql.append(" and model.sat like :sat");
                        
                        if (null!=(sun))
                            hql.append(" and model.sun like :sun");

              
                

                        Query query = session.createQuery(hql.toString());
                        query.setFirstResult(0);
                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
                        

                        if (StringUtils.hasText(no))
                            query.setString("no", "%" + no + "%");
                        
                        if (StringUtils.hasText(specInfo))
                            query.setString("specInfo", "%" + specInfo + "%");
                        
                        if (StringUtils.hasText(taskType))
                            query.setString("taskType", "%" + taskType + "%");
                        
                        if (StringUtils.hasText(status))
                            query.setString("status", "%" + status + "%");
                        
                        if (null != fos.get("countingDate"))
        					query.setDate("countingDate", (Date) fos.get("countingDate"));
                        
                        if (StringUtils.hasText(incharge))
                            query.setString("incharge", "%" + incharge + "%");
                        
                        if (null!=(mon))
                            query.setString("mon", "%" + mon + "%");
                        
                        if (null!=(tue))
                            query.setString("tue", "%" + tue + "%");
                        
                        if (null!=(wed))
                            query.setString("wed", "%" + wed + "%");
                        
                        if (null!=(thu))
                            query.setString("thu", "%" + thu + "%");
                        
                        if (null!=(fri))
                            query.setString("fri", "%" + fri + "%");
                        
                        if (null!=(sat))
                            query.setString("sat", "%" + sat + "%");
                        
                        if (null!=(sun))
                            query.setString("sun", "%" + sun + "%");
                

                        return query.list();
                    }
                });

        return result;
    }
    public List<AdTaskReviewView> findByOrderNo(String brandCode, String orderTypeCode, String orderNo, String storeArea,String status) {
		log.info("findByOrderNo..."+brandCode+"/"+orderTypeCode+"/"+orderNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchOrderNo = orderNo;
		final String searchStatus = status;
		final String searchStoreArea= storeArea;
		
		List<AdTaskReviewView> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryHead> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from Ad_Task as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				hql.append(" and model.orderNo       = :searchOrderNo");
				if(StringUtils.hasText(searchStoreArea))
					hql.append(" and model.storeArea       = :searchStoreArea");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" order by model.orderDate desc");
				Query query = session.createQuery(hql.toString());
				query.setString("searchBrandCode"    , searchBrandCode);
				query.setString("searchOrderTypeCode", searchOrderTypeCode);
				query.setString("searchOrderNo"      , searchOrderNo);
				if(StringUtils.hasText(searchStoreArea))
					query.setString("searchStoreArea"      , searchStoreArea);	
				if(StringUtils.hasText(searchStatus))
					query.setString("searchStatus"      , searchStatus);	
				return query.list();
			}
		});

		return result;
	}

    public AdTaskReviewView findByLocationName(Long orderTypeCode) {

            StringBuffer hql = new StringBuffer("from Ad_Task as model ");
            hql.append("where model.orderTypeCode = ? ");
            List<AdTaskReviewView> lists = getHibernateTemplate().find(hql.toString(),
                    new Object[] { orderTypeCode });
            return (lists != null && lists.size() > 0 ? lists.get(0) : null);
        }
    
    /**
     * @param findObjs
     * @param startPage
     * @param pageSize
     * @param searchType  
     * 	      1) get max record count 
     *        2) select data records according to startPage and pageSize 
     *        3) select all records 
     * @return
     */
    public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize, String searchType) {
    		log.info("findPageLine");
    	final HashMap fos = findObjs;
    	final int startRecordIndexStar = startPage * pageSize;
    	final int pSize = pageSize;
    	final String type = searchType;
    	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
    		public Object doInHibernate(Session session)throws HibernateException, SQLException {
    			log.info("findPageLine~~~~~~~~~~~~~~");

    			Date startDate             = (Date) fos.get("startDate");
    			log.info("startDate=="+startDate);
    			Date endDate             = (Date) fos.get("endDate");
    			log.info("endDate=="+endDate);
    			String specInfo  = (String) fos.get("specInfo");
    			log.info("specInfo=="+specInfo);
    			//String no       = (String) fos.get("no");
    			//log.info("no=="+no);
    			String status         = (String) fos.get("status");	
    			log.info("status=="+status);
    			//String taskType  = (String) fos.get("taskType");
    			//log.info("taskType=="+taskType);
    			String incharge    = (String) fos.get("incharge");
    			log.info("incharge=="+incharge);
    			Integer mon = (Integer)fos.get("mon");
    			Integer tue = (Integer)fos.get("tue");	
    			Integer wed = (Integer)fos.get("wed");	
    			Integer thu = (Integer)fos.get("thu");
    			Integer fri = (Integer)fos.get("fri");	
    			Integer sat = (Integer)fos.get("sat");	
    			Integer sun = (Integer)fos.get("sun");	
    			
    			
    			
    			StringBuffer hql = new StringBuffer("");
    			if(QUARY_TYPE_RECORD_COUNT.equals(type))
    				hql.append(" select count(model.lineId) as rowCount from AdTaskReviewView as model where 1=1 ");
    			else if(QUARY_TYPE_SELECT_ALL.equals(type))
    				hql.append(" select model.lineId from AdTaskReviewView as model where 1=1 ");
    			else
    				hql.append(" from AdTaskReviewView as model where 1=1 ");
    		

    			if (mon!= null)
    				hql.append(" and model.id.mon = :mon");
    			
    			if (tue!= null)
    				hql.append(" and model.id.tue = :tue");
    			
    			if (wed!= null)
    				hql.append(" and model.id.wed = :wed");
    			
    			if (thu!= null)
    				hql.append(" and model.id.thu = :thu");
    			
    			if (fri!= null)
    				hql.append(" and model.id.fri = :fri");
    			
    			if (sat!= null)
    				hql.append(" and model.id.sat = :sat");
    			
    			if (sun!= null)
    				hql.append(" and model.id.sun = :sun");

    			if (startDate!= null)
    				hql.append(" and model.id.countingDate >= :startDate");
    			
    			if (endDate!= null)
    				hql.append(" and model.id.countingDate <= :endDate");
   

    			if (StringUtils.hasText(specInfo))
    				hql.append(" and model.specInfo = :specInfo");


    			//if (StringUtils.hasText(no))
    				//hql.append(" and model.no = :no");

    			if (StringUtils.hasText(status))
    				hql.append(" and model.status = :status");	
    			
     			//if (StringUtils.hasText(taskType))
    				//hql.append(" and model.taskType = :taskType");		
     			
     			if (StringUtils.hasText(incharge))
    				hql.append(" and model.incharge = :incharge ");
     				//hql.append(" select sum(mon) as total1 ");
     				//hql.append(" select SUM(mon),SUM(tue),SUM(wed),SUM(thu),SUM(fri),SUM(sat),SUM(sun) from AdTaskReviewView as model ");
     				//hql.append(" where model.incharge = '" + incharge + "'");
     				//hql.append("  from AdTaskReviewView model where incharge = :incharge ");
     				//hql.append("   and model.AdTaskReviewView.lineId = :lineId ");

     			hql.append(" order by model.lineId desc");
     			log.info("HHH:"+hql.toString());
    			log.info(hql.toString());

    			Query query = session.createQuery(hql.toString());
    			if( QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
    				query.setFirstResult(startRecordIndexStar);
    				query.setMaxResults(pSize);
    			}
    		
    			if (null!=mon)
    				query.setInteger("mon", mon);
    			
    			if (null!=tue)
    				query.setInteger("tue", tue);
    			
    			if (null!=wed)
    				query.setInteger("wed", wed);
    			
    			if (null!=thu)
    				query.setInteger("thu", thu);
    			
    			if (null!=fri)
    				query.setInteger("fri", fri);
    			
    			if (null!=sat)
    				query.setInteger("sat", sat);
    			
    			if (null!=sun)
    				query.setInteger("sun", sun);
    			
    			if (StringUtils.hasText(specInfo))
    				query.setString("specInfo", specInfo);

    			//if (StringUtils.hasText(no))
    				//query.setString("no", no);

    			if (StringUtils.hasText(status))
    				query.setString("status", status);		

    			//if (StringUtils.hasText(taskType))
    				//query.setString("taskType", taskType);

    			if (StringUtils.hasText(incharge))
    				query.setString("incharge", incharge);


    			return query.list();
    		}
    	});
    	HashMap returnResult = new HashMap();		
    	returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result: null);			
    	if(result.size() == 0){
    		returnResult.put("recordCount", 0L);
    	}else{
    		returnResult.put("recordCount", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result.size() : Long.valueOf(result.get(0).toString()));
    	}
    	return returnResult;
    }
  
}
