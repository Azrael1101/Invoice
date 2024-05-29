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

import tw.com.tm.erp.hbm.bean.AdDetail;
import tw.com.tm.erp.hbm.bean.ShopSet;
import tw.com.tm.erp.hbm.bean.ShopSetDetail;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;

 public class ShopSetDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuPurchaseHeadDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
    
	public ShopSet findById(Long id) {
		log.debug("getting ShopSet instance with id: " + id);
		try {
			ShopSet instance = (ShopSet) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ShopSet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List findPageLineShop(Long headId, int startPage, int pageSize) {
		log.info("BuPurchaseLineDAO.findPageLineWareHouse headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ShopSetDetail> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ShopSetDetail as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.shopSet.headId = :headId order by shopCode");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				log.info("hql:"+hql);
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
		log.info("PoPurchaseOrderLineDAO.findPageLineMaxIndex" + headId);		
		final Long hId = headId;
		List<ShopSet> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ShopSet as model where 1=1 ");
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
			List<ShopSetDetail> shopSetDetails = re.get(0).getShopSetDetails();
			log.info("babu4000:"+shopSetDetails.size());
			if( null != shopSetDetails && shopSetDetails.size() > 0 )
				return shopSetDetails.get(shopSetDetails.size()-1).getIndexNo() ;
		}
		return 0L;
	}
	
    public List<BuPurchaseHead> findAll() {
        log.debug("finding all BuPurchaseHead instances");
        try {
            String queryString = "from Ad_Task";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List<BuPurchaseHead> find(HashMap findObjs) {
    	
    	final HashMap fos = findObjs;
    	
        final String orderTypeCode = (String) findObjs.get("orderTypeCode");
        final String orderNo = (String) findObjs.get("orderNo");
        final String brandCode = (String) findObjs.get("brandCode");
        final String createdBy = (String) findObjs.get("createdBy");
        final String creationDate = (String) findObjs.get("creationDate");
        final String depManager = (String) findObjs.get("depManager");
        final String request = (String) findObjs.get("request");
        final Date requestDate = (Date) findObjs.get("requestDate");
        final String classification = (String) findObjs.get("classification");
        final String project = (String) findObjs.get("project");
        final String description = (String) findObjs.get("description");
        final String status = (String) findObjs.get("status");
        
        
        

        List<BuPurchaseHead> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from Ad_Task as model");
                        hql.append(" where model.HeadId != null");
                        
                        Date requestDate       = (Date)   fos.get("requestDate"); 
                        
                        if (StringUtils.hasText(orderTypeCode))
                            hql.append(" and model.orderTypeCode like :orderTypeCode");

                        if (StringUtils.hasText(orderNo))
                            hql.append(" and model.orderNo like :orderNo");

                        if (StringUtils.hasText(brandCode))
                            hql.append(" and model.brandCode like :brandCode");

                        if (StringUtils.hasText(createdBy))
                            hql.append(" and model.createdBy like :createdBy");

                        if (StringUtils.hasText(creationDate))
                            hql.append(" and model.creationDate like :creationDate");
                        
                        if (StringUtils.hasText(depManager))
                            hql.append(" and model.depManager like :depManager");

                        if (StringUtils.hasText(request))
                            hql.append(" and model.request like :request");

                		if (null != fos.get(requestDate))
        					hql.append(" and model.requestDate = :requestDate ");
                        
                        if (StringUtils.hasText(classification))
                            hql.append(" and model.classification like :classification");

                        if (StringUtils.hasText(project))
                            hql.append(" and model.project like :project");

                        if (StringUtils.hasText(description))
                            hql.append(" and model.description like :description");
                        
                        if (StringUtils.hasText(status))
                            hql.append(" and model.status like :status");

              
                

                        Query query = session.createQuery(hql.toString());
                        query.setFirstResult(0);
                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
                        

                        if (StringUtils.hasText(orderTypeCode))
                            query.setString("orderTypeCode", "%" + orderTypeCode + "%");
                        
                        if (StringUtils.hasText(orderNo))
                            query.setString("orderNo", "%" + orderNo + "%");
                        
                        if (StringUtils.hasText(brandCode))
                            query.setString("brandCode", "%" + brandCode + "%");
                        
                        if (StringUtils.hasText(request))
                            query.setString("departMent", "%" + request + "%");
                        
                        if (StringUtils.hasText(createdBy))
                            query.setString("createdBy", "%" + createdBy + "%");
                        
                        if (StringUtils.hasText(creationDate))
                            query.setString("creationDate", "%" + creationDate + "%");
                        
                        if (StringUtils.hasText(depManager))
                            query.setString("depManager", "%" + depManager + "%");
                        
                        if (StringUtils.hasText(request))
                            query.setString("request", "%" + request + "%");
                        
                        if (null != fos.get("requestDate"))
        					query.setDate("requestDate", (Date) fos.get("requestDate"));
                        
                        if (StringUtils.hasText(classification))
                            query.setString("classification", "%" + classification + "%");
                        
                        if (StringUtils.hasText(project))
                            query.setString("project", "%" + project + "%");
                        
                        if (StringUtils.hasText(description))
                            query.setString("description", "%" + description + "%");
                        
                        if (StringUtils.hasText(status))
                            query.setString("status", "%" + status + "%");
                

                        return query.list();
                    }
                });

        return result;
    }
    /*
    public List<BuPurchaseHead> findByOrderNo(String brandCode, String orderTypeCode, String orderNo, String storeArea,String status) {
		log.info("findByOrderNo..."+brandCode+"/"+orderTypeCode+"/"+orderNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchOrderNo = orderNo;
		final String searchStatus = status;
		final String searchStoreArea= storeArea;
		
		List<BuPurchaseHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryHead> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from BuPurchaseHead as model where 1=1 ");
				hql.append(" and model.brandCode     = :searchBrandCode");
				hql.append(" and model.orderTypeCode = :searchOrderTypeCode");
				hql.append(" and model.orderNo       = :searchOrderNo");
				if(StringUtils.hasText(searchStoreArea))
					hql.append(" and model.storeArea       = :searchStoreArea");
				if(StringUtils.hasText(searchStatus))
					hql.append(" and model.status       = :searchStatus");
				hql.append(" order by model.orderNo desc");
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
	}*/

    public BuPurchaseHead findByLocationName(Long orderTypeCode) {

            StringBuffer hql = new StringBuffer("from Ad_Task as model ");
            hql.append("where model.orderTypeCode = ? ");
            List<BuPurchaseHead> lists = getHibernateTemplate().find(hql.toString(),
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

    			//Date requestDate         = (Date) fos.get("requestDate");
    			//log.info("requestDate"+requestDate);
    			Date startDate           = (Date) fos.get("startDate");
    			Date endDate             = (Date) fos.get("endDate");
    			String brandCode   = (String) fos.get("brandCode");
    			String orderTypeCode     = (String) fos.get("orderTypeCode");
    			String orderNo        = (String) fos.get("orderNo");
    			String no         = (String) fos.get("no");	
    			String request   = (String) fos.get("request");
    			String requestCode   = (String) fos.get("requestCode");	
    			String department    = (String) fos.get("department");
    			String status        = (String) fos.get("status");
    			String createdBy           = (String) fos.get("createdBy");
    			String otherGroup          = (String) fos.get("otherGroup");
    			String rqInChargeCode      = (String) fos.get("rqInChargeCode");
    			String categoryGroup      = (String) fos.get("categoryGroup");
    			String isshowotherGroup    = (String) fos.get("isshowotherGroup");
    			String notshowotherGroup   = (String) fos.get("notshowotherGroup");
    			log.info("startDate~~~~~~~~~~~~~~"+startDate);
    			log.info("endDate~~~~~~~~~~~~~~"+endDate);
    			log.info("orderTypeCode~~~~~~~~~~~"+orderTypeCode);
    			log.info("brandCode~~~~~~~~~~~"+brandCode);
    			log.info("orderNo~~~~~~~~~~~"+orderNo);
    			    			   			
    			StringBuffer hql = new StringBuffer("");
    			log.info("findPageLine~~~~~~~~~~~~~~@@@@@@@@@@@@@@@@@@");
    			if(QUARY_TYPE_RECORD_COUNT.equals(type))
    				hql.append(" select count(model.headId) as rowCount from ShopSet as model where 1=1 ");
    			else if(QUARY_TYPE_SELECT_ALL.equals(type))
    				hql.append(" select model.headId from ShopSet as model where 1=1 ");
    			else
    				hql.append(" from ShopSet as model where 1=1 ");
    		
    			log.info("findPageLine~~~~~~~~~~~~~~++++++++++++++++++");

    			if (StringUtils.hasText(brandCode))
    				hql.append(" and model.brandCode = :brandCode");
    			
    			if (StringUtils.hasText(orderTypeCode))
    				hql.append(" and model.orderTypeCode = :orderTypeCode");
    			
    			if (StringUtils.hasText(orderNo))
    				hql.append(" and model.orderNo like :orderNo");
    				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
    			
    			if (startDate!= null)
    				hql.append(" and model.id.requestDate >= :startDate");
    			
    			if (endDate!= null)
    				hql.append(" and model.id.requestDate <= :endDate");

    			if (StringUtils.hasText(department))
    				hql.append(" and model.department = :department");

    			if (StringUtils.hasText(request))
    				hql.append(" and model.request = :request");

    			if (StringUtils.hasText(no))
    				hql.append(" and model.no like :no");

    			if (StringUtils.hasText(status))
    				hql.append(" and model.status = :status");	
    			
     			if (StringUtils.hasText(createdBy))
    				hql.append(" and model.createdBy = :createdBy");		
     			
     			if (StringUtils.hasText(requestCode))
    				hql.append(" and model.requestCode = :requestCode");
     			
     			if (StringUtils.hasText(otherGroup))
    				hql.append(" and model.otherGroup = :otherGroup");
     			
     			if (StringUtils.hasText(rqInChargeCode))
    				hql.append(" and model.rqInChargeCode = :rqInChargeCode");
     			
    			if (StringUtils.hasText(categoryGroup))
    				hql.append(" and model.categoryGroup = :categoryGroup");
    				
     			/*if ("N".equalsIgnoreCase((String) fos.get("isshowotherGroup"))
						&& "N".equalsIgnoreCase((String) fos.get("notshowotherGroup"))) // 只顯都顯示
     				hql.append(" and model.otherGroup = otherGroup ");*/

     			/*if (!"N".equalsIgnoreCase((String) fos.get("isshowotherGroup"))
						&& !"N".equalsIgnoreCase((String) fos.get("notshowotherGroup"))) // 只顯都顯示
     				hql.append(" and model.otherGroup = otherGroup ");*/
     			
     			else if (!"N".equalsIgnoreCase((String) fos.get("notshowotherGroup"))) // 只顯示有分派
					hql.append(" and model.otherGroup = null ");
     			
     			//else if ("N".equalsIgnoreCase((String) fos.get("otherGroup"))) // 只顯示有分派
					//hql.append(" and model.notshowotherGroup = null ");
     			/*else if (!"N".equalsIgnoreCase((String) fos.get("isshowotherGroup"))) // 只顯示未分派
					hql.append(" and model.otherGroup != null ");*/
     			
     			hql.append(" order by model.orderNo");
    			log.info("HHH:"+hql.toString());

    			Query query = session.createQuery(hql.toString());
    			if( QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
    				query.setFirstResult(startRecordIndexStar);
    				query.setMaxResults(pSize);
    			}
    		
    			if (startDate!= null)
    					query.setDate("startDate", startDate);

    			if (endDate!= null)
    					query.setDate("endDate", endDate);	
    			
    			if (StringUtils.hasText(brandCode))
    				query.setString("brandCode", brandCode);

    			if (StringUtils.hasText(orderTypeCode))
    				query.setString("orderTypeCode", orderTypeCode);

    			if (StringUtils.hasText(orderNo))
    				query.setString("orderNo", orderNo);

    			//if (requestDate != null)
    				//query.setDate("requestDate", requestDate);
    			
    			if (StringUtils.hasText(department))
    				query.setString("department", department);

    			if (StringUtils.hasText(request))
    				query.setString("request", request);

    			if (StringUtils.hasText(no))
    				query.setString("no", no);

    			if (StringUtils.hasText(status))
    				query.setString("status", status);		

    			if (StringUtils.hasText(createdBy))
    				query.setString("createdBy", createdBy);

    			if (StringUtils.hasText(requestCode))
    				query.setString("requestCode", requestCode);
    			
    			if (StringUtils.hasText(otherGroup))
    				query.setString("otherGroup", otherGroup);
    			
    			if (StringUtils.hasText(rqInChargeCode))
    				query.setString("rqInChargeCode", rqInChargeCode);
    			
    			if (StringUtils.hasText(categoryGroup))
    				query.setString("categoryGroup", categoryGroup);


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
    
    public List<BuPurchaseHead> findByemployee( HashMap findObjs ){
		final HashMap fos = findObjs;
		List<BuPurchaseHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from BuPurchaseHead as model ");
				hql.append(" where 1=1");

			
				if (StringUtils.hasText((String) fos.get("requestCode")))
					hql.append(" and model.requestCode ='").append((String) fos.get("requestCode")).append("'");
				log.info(hql.toString());
				Query query = session.createQuery(hql.toString());
				//query.setFirstResult(0);
				//query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				return query.list();
			}
		});
		return re;
	}

	
	public List<BuPurchaseHead> findByemployee(String rqInChargeCode) {

		final String employee_Code = rqInChargeCode;
	
		List<BuPurchaseHead> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    	//String hql = "select brandCode,brandName,sum(enable) as enable from (select brandCode, brandName,0 as enable from BuBrand where enable = 'Y' union select B.brandCode, B.brandName,1 AS brandName from BuBrand B inner join BuEmployeeBrand E ON B.brandCode = E.brandCode WHERE employeeCode = :employeeCode AND B.enable =:enable  ) A GROUP BY brandCode,brandName ORDER BY brandCode";
			    	
			    	String hql = " from BuPurchaseHead G where G.rqInChargeCode=:rqInChargeCode ";
					Query query = session.createQuery(hql);
		
					query.setParameter("rqInChargeCode", employee_Code);
					
					return query.list();
			    }
			}); 
		return result;
	}
	
	  public List<BuPurchaseHead> findByPropertyId(String entityBean, String fieldNames[], Object fieldValue[], String orderFieldName) {

			boolean second = false ;
			
			StringBuffer hql = new StringBuffer("from "); 
		    	hql.append(entityBean);
		    	hql.append(" as model where ");
				for( String fieldName : fieldNames){
					if(second){
						hql.append(" and ");
					}
					hql.append(" model.");
					hql.append(fieldName);
					hql.append(" = ? ");
					second = true ;
				}
				hql.append(" and model.status in ('SAVE','PLAN')");
				hql.append(" and model.orderNo not like 'TMP%'");
				hql.append(" order by ").append(orderFieldName);

				return getHibernateTemplate().find(hql.toString(), fieldValue);		
	  }
	  
	public ShopSetDetail findItemByIdentification(Long headId, Long lineId) {
			StringBuffer hql = new StringBuffer(
					"from ShopSetDetail as model where model.shopSet.headId = ? and model.lineId = ?");
			List<ShopSetDetail> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
			return (result != null && result.size() > 0 ? result.get(0) : null);
	}

}
