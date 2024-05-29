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

import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.AdCustServiceLine;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;


public class AdCustServiceLineDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(AdCustServiceLineDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";

    public List<AdCustServiceLine> findAll() {
        log.debug("finding all AdCustService instances");
        try {
            String queryString = "from AdCustService";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    

    public List<AdCustServiceLine> find(HashMap conditionMap) {

        final String itemNo = (String) conditionMap.get("itemNo");
        final String itemName = (String) conditionMap.get("itemName");
        final String specInfo = (String) conditionMap.get("specInfo");
        final String quantity = (String) conditionMap.get("quantity");
        final String purUnitPrice = (String) conditionMap.get("purUnitPrice");
        final String purTotalAmount = (String) conditionMap.get("purTotalAmount");
        final String supplier = (String) conditionMap.get("supplier");
      
     

        
        

        List<AdCustServiceLine> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from PrDetail as model");
                        hql.append(" where model.LineId != null");
                        

                        if (StringUtils.hasText(itemNo))
                            hql.append(" and model.itemNo like :itemNo");

                        if (StringUtils.hasText(itemName))
                            hql.append(" and model.itemName like :itemName");

                        if (StringUtils.hasText(specInfo))
                            hql.append(" and model.specInfo like :specInfo");
                        
                        if (StringUtils.hasText(quantity))
                            hql.append(" and model.quantity like :quantity");

                        
                        if (StringUtils.hasText(purUnitPrice))
                            hql.append(" and model.purUnitPrice like :purUnitPrice");

                        if (StringUtils.hasText(purTotalAmount))
                            hql.append(" and model.purTotalAmount like :purTotalAmount");

                        if (StringUtils.hasText(supplier))
                            hql.append(" and model.supplier like :supplier");
                        

                        
            

              
                

                        Query query = session.createQuery(hql.toString());
                        query.setFirstResult(0);
                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

                        if (StringUtils.hasText(itemNo))
                            query.setString("itemNo", "%" + itemNo + "%");
                        
                        if (StringUtils.hasText(itemName))
                            query.setString("itemName", "%" + itemName + "%");
                        
                        if (StringUtils.hasText(specInfo))
                            query.setString("specInfo", "%" + specInfo + "%");
                        
                        if (StringUtils.hasText(quantity))
                            query.setString("quantity", "%" + quantity + "%");
                        
                        
                        if (StringUtils.hasText(purUnitPrice))
                            query.setString("purUnitPrice", "%" + purUnitPrice + "%");
                        
                        if (StringUtils.hasText(purTotalAmount))
                            query.setString("purTotalAmount", "%" + purTotalAmount + "%");
                        
                        if (StringUtils.hasText(supplier))
                            query.setString("supplier", "%" + supplier + "%");
                        
                

                        return query.list();
                    }
                });

        return result;
    }
    
	public Long getMaxIndexNo(Long headId) {
		log.info("getMaxIndexNo..."+headId);
		final Long searchHeadId = headId;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("select MAX(model.indexNo) as indexNo from PrDetail as model where 1=1 ");
				hql.append(" and model.buPurchaseHead.headId     = :searchHeadId");
				Query query = session.createQuery(hql.toString());
				query.setLong("searchHeadId"    , searchHeadId);
				return query.list();
			}
		});
		log.info("getMaxIndexNo.size:" + result.size());
		Long returnResult = new Long(0);
		if (null== result || result.size() == 0) {
			returnResult = 0L;
		} else {
			log.info("SoDeliveryLog.IndexNo:" + result.get(0));
			returnResult =(Long)result.get(0);
		}
		return (null == returnResult?0L:returnResult);
	}
	
    public  List<AdCustServiceLine>  findLineByDeliveryOrderNo(Long headId, String orderTypeCode, String deliveryOrderNo) {
		log.info("findLineByDeliveryheadId..."+headId);
		log.info("findLineByDeliveryorderTypeCode..."+orderTypeCode);
		log.info("findLineByDeliveryorderTypeCode..."+deliveryOrderNo);
		final Long hId = headId;
		final String typeCode = orderTypeCode;
		final String orderNo = deliveryOrderNo;
		List<AdCustServiceLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<AdCustServiceLine> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from BuPurchaseLineLine as model where 1=1 ");
				hql.append(" and model.buPurchaseHead.headId = :headId ");
				//hql.append(" and model.deliveryOrderType = :typeCode ");
				//hql.append(" and model.deliveryOrderNo = :orderNo ");
				Query query = session.createQuery(hql.toString());
				query.setLong("headId", hId);
				query.setString("typeCode", typeCode);
				query.setString("orderNo", orderNo);
				return query.list();
			}
		});
		log.info("AdCustService.form:" + result.size());
		return result;
	}

    public AdCustServiceLine findByLocationName(Long orderTypeCode) {

            StringBuffer hql = new StringBuffer("from AdCustService as model ");
            hql.append("where model.orderTypeCode = ? ");
            List<AdCustServiceLine> lists = getHibernateTemplate().find(hql.toString(),
                    new Object[] { orderTypeCode });
            return (lists != null && lists.size() > 0 ? lists.get(0) : null);
        }
    
    public List<AdCustServiceLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("AdCustServiceLineDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<AdCustServiceLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from AdCustServiceLine as model where 1=1 ");
				if (null != hId)							
					hql.append(" and model.adCustServiceHead.headId = :headId order by indexNo");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (null != hId)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		return re;
	}
    /**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("AdCustServicesDAO.findPageLineMaxIndex" + headId);		
		final Long hId = headId;
		List<AdCustServiceHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from AdCustServiceHead as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.headId = :headId ");
				Query query = session.createQuery(hql.toString());
				if (null != hId)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		 log.info("babu2000:"+re.size());
		if( null != re && re.size() > 0 ){
			log.info("babu3000:"+re.get(0));
			List<AdCustServiceLine> adCustServiceLines = re.get(0).getAdCustServiceLines();
			log.info("babu4000:"+adCustServiceLines.size());
			if( null != adCustServiceLines && adCustServiceLines.size() > 0 )
				return adCustServiceLines.get(adCustServiceLines.size()-1).getIndexNo() ;
		}
		return 0L;
	}
	
	public AdCustServiceLine findItemByIdentification(Long headId, Long lineId) {
		StringBuffer hql = new StringBuffer(
				"from AdCustServiceLine as model where model.adCustServiceHead.headId = ? and model.lineId = ?");
		List<AdCustServiceLine> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	 public AdCustServiceLine findById(Long lineId) {

         StringBuffer hql = new StringBuffer("from AdCustServiceLine as model ");
         hql.append("where model.lineId = ? ");
         List<AdCustServiceLine> lists = getHibernateTemplate().find(hql.toString(),
                 new Object[] { lineId });
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

	    			Date requestDate         = (Date) fos.get("requestDate");
	    			//log.info("requestDate"+requestDate);
	    			String brandCode   = (String) fos.get("brandCode");
	    			String orderTypeCode     = (String) fos.get("orderTypeCode");
	    			String orderNo        = (String) fos.get("orderNo");
	    			String requestCode   = (String) fos.get("requestCode");	
	    			String status        = (String) fos.get("status");
	    	
	    			
	    			log.info("orderTypeCode~~~~~~~~~~~"+orderTypeCode);
	    			log.info("brandCode~~~~~~~~~~~"+brandCode);
	    			log.info("orderNo~~~~~~~~~~~"+orderNo);
	    			    			   			
	    			StringBuffer hql = new StringBuffer("");
	    			log.info("findPageLine~~~~~~~~~~~~~~@@@@@@@@@@@@@@@@@@");
	    			if(QUARY_TYPE_RECORD_COUNT.equals(type))
	    				hql.append(" select count(model.headId) as rowCount from BuPurchaseHead as model where 1=1 ");
	    			else if(QUARY_TYPE_SELECT_ALL.equals(type))
	    				hql.append(" select model.headId from BuPurchaseHead as model where 1=1 ");
	    			else
	    				hql.append(" from BuPurchaseHead as model where 1=1 ");
	    		
	    			log.info("findPageLine~~~~~~~~~~~~~~++++++++++++++++++");

	    			if (StringUtils.hasText(brandCode))
	    				hql.append(" and model.brandCode = :brandCode");
	    			
	    			if (StringUtils.hasText(orderTypeCode))
	    				hql.append(" and model.orderTypeCode = :orderTypeCode");
	    			
	    			if (StringUtils.hasText(orderNo))
	    				hql.append(" and model.orderNo like :orderNo");
	    				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

	    			if (StringUtils.hasText(status))
	    				hql.append(" and model.status = :status");	
	    			
	     			if (StringUtils.hasText(requestCode))
	    				hql.append(" and model.requestCode = :requestCode");
	     		
	     			
	     			hql.append(" order by model.orderTypeCode");
	    			log.info("HHH:"+hql.toString());

	    			Query query = session.createQuery(hql.toString());
	    			if( QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
	    				query.setFirstResult(startRecordIndexStar);
	    				query.setMaxResults(pSize);
	    			}
	    		
	    			
	    			if (StringUtils.hasText(brandCode))
	    				query.setString("brandCode", brandCode);

	    			if (StringUtils.hasText(orderTypeCode))
	    				query.setString("orderTypeCode", orderTypeCode);

	    			if (StringUtils.hasText(orderNo))
	    				query.setString("orderNo", orderNo);

	    			if (requestDate != null)
	    				query.setDate("requestDate", requestDate);
	    			

	    			if (StringUtils.hasText(status))
	    				query.setString("status", status);		


	    			if (StringUtils.hasText(requestCode))
	    				query.setString("requestCode", requestCode);
	    			
	    		

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
