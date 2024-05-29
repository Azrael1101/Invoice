package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
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
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.utils.DateUtils;

 public class AdCustServiceDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(AdCustServiceDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
    
	public AdCustServiceHead findById(Long id) {
		log.debug("getting BuPurchaseHead instance with id: " + id);
		try {
			AdCustServiceHead instance = (AdCustServiceHead) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.AdCustServiceHead", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
    public List<AdCustServiceHead> findAll() {
        log.debug("finding all AdCustServiceHead instances");
        try {
            String queryString = "from AdCustServiceHead";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List<AdCustServiceHead> find(HashMap findObjs) {
    	
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
        
        
        

        List<AdCustServiceHead> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from AdCustServiceHead as model");
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
    public List<AdCustServiceHead> findByOrderNo(String brandCode, String orderTypeCode, String orderNo, String storeArea,String status) {
		log.info("findByOrderNo..."+brandCode+"/"+orderTypeCode+"/"+orderNo);
		final String searchBrandCode = brandCode;
		final String searchOrderTypeCode = orderTypeCode;
		final String searchOrderNo = orderNo;
		final String searchStatus = status;
		final String searchStoreArea= storeArea;
		
		List<AdCustServiceHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<SoDeliveryHead> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				hql.append("from AdCustServiceHead as model where 1=1 ");
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

    public AdCustServiceHead findByLocationName(Long orderTypeCode) {

            StringBuffer hql = new StringBuffer("from AdCustServiceHead as model ");
            hql.append("where model.orderTypeCode = ? ");
            List<AdCustServiceHead> lists = getHibernateTemplate().find(hql.toString(),
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
    			Date requestDate         = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String)fos.get("requestDate"));
    			Date requestDate1         = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String)fos.get("requestDate1"));
    			log.info("aaaaaaaaaaaaaab:"+requestDate);
    			String brandCode   		 = (String) fos.get("brandCode");
    			String orderTypeCode     = (String) fos.get("orderTypeCode");
    			String orderNo        	 = (String) fos.get("orderNo");
    			String requestCode   	 = (String) fos.get("requestCode");	
    			String status        	 = (String) fos.get("status");
    			String customerLastName  = (String) fos.get("customerLastName");
    			String customerFristName = (String) fos.get("customerFristName");
    			String createdByName     = "%"+(String) fos.get("createdByName")+"%";
    			String itemBrand     	 = (String) fos.get("itemBrand");
    			String categoryType     	 = (String) fos.get("categoryType");
    			String department     	 = (String) fos.get("department");
    			String warehuseCode     	 = (String) fos.get("warehuseCode");
    			String customerRequest     	 = (String) fos.get("customerRequest");
    			String exceptional     	 = (String) fos.get("exceptional");
    			String nationality     	 = (String) fos.get("nationality");
    			String saleOrderDate     	 = (String)fos.get("saleOrderDate");
    			String itemCode     	 = (String) fos.get("itemCode");
    			//Date requestDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, (String) fos.get("reqtDate"));
    			log.info("cccccccccc:"+saleOrderDate);
    			
    			StringBuffer hql = new StringBuffer("");
    			if(QUARY_TYPE_RECORD_COUNT.equals(type))
    				hql.append(" select count(model.headId) as rowCount from AdCustServiceHead as model where 1=1 ");
    			else if(QUARY_TYPE_SELECT_ALL.equals(type))
    				hql.append(" select model.headId from AdCustServiceHead as model where 1=1 ");
    			else
    				hql.append(" from AdCustServiceHead as model where 1=1 ");

    			if (StringUtils.hasText(categoryType))
    				hql.append(" and model.categoryType = :categoryType");
    			
    			if (StringUtils.hasText(warehuseCode))
    				hql.append(" and model.warehuseCode = :warehuseCode");
    			
    			if (StringUtils.hasText(customerRequest))
    				hql.append(" and model.customerRequest = :customerRequest");
    			
    			if (StringUtils.hasText(exceptional))
    				hql.append(" and model.exceptional = :exceptional");
    			
    			if (StringUtils.hasText(nationality))
    				hql.append(" and model.nationality = :nationality");
    			
    			if (StringUtils.hasText(department))
    				hql.append(" and model.department = :department");
    			
    			if (StringUtils.hasText(saleOrderDate))
    				hql.append(" and model.saleOrderDate = :saleOrderDate");
    			
    			if (StringUtils.hasText(brandCode))
    				hql.append(" and model.brandCode = :brandCode");
    			
    			if (StringUtils.hasText(orderTypeCode))
    				hql.append(" and model.orderTypeCode = :orderTypeCode");
    			
    			if (StringUtils.hasText(orderNo))
    				hql.append(" and model.orderNo like :orderNo");
    				hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
    				
    			if (null!=requestDate)
        			hql.append(" and model.requestDate >= :requestDate");
    			
    			if (null!=requestDate1)
        			hql.append(" and model.requestDate <= :requestDate1");

    			if (StringUtils.hasText(status))
    				hql.append(" and model.status = :status");	
    			
     			if (StringUtils.hasText(requestCode))
    				hql.append(" and model.requestCode = :requestCode");
     			
     			if (StringUtils.hasText(customerLastName))
    				hql.append(" and model.customerLastName = :customerLastName");
     			
     			if (StringUtils.hasText(customerFristName))
    				hql.append(" and model.customerFristName = :customerFristName");
     			
     			if (StringUtils.hasText(createdByName))
    				hql.append(" and model.createdByName like :createdByName");
     			
     			if (StringUtils.hasText(itemBrand))
    				hql.append(" and model.itemBrand = :itemBrand");
     			
     			if (StringUtils.hasText(itemCode))
    				hql.append(" and model.itemCode = :itemCode");
     		
     			hql.append(" order by model.orderNo");
    			log.info("HHH:"+hql.toString());

    			Query query = session.createQuery(hql.toString());
    			if( QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
    				query.setFirstResult(startRecordIndexStar);
    				query.setMaxResults(pSize);
    			}
    				
    			if (StringUtils.hasText(categoryType))
    				query.setString("categoryType", categoryType);
    			
    			if (StringUtils.hasText(warehuseCode))
    				query.setString("warehuseCode", warehuseCode);
    			
    			if (StringUtils.hasText(customerRequest))
    				query.setString("customerRequest", customerRequest);
    			
    			if (StringUtils.hasText(exceptional))
    				query.setString("exceptional", exceptional);
    				
    			if (StringUtils.hasText(nationality))
        			query.setString("nationality", nationality);
    			
    			if (StringUtils.hasText(department))
    				query.setString("department", department);
    			
    			if (StringUtils.hasText(saleOrderDate))
    				query.setString("saleOrderDate", saleOrderDate);
    			
    			if (StringUtils.hasText(brandCode))
    				query.setString("brandCode", brandCode);

    			if (StringUtils.hasText(orderTypeCode))
    				query.setString("orderTypeCode", orderTypeCode);

    			if (StringUtils.hasText(orderNo))
    				query.setString("orderNo", orderNo);

    			if (StringUtils.hasText(status))
    				query.setString("status", status);		

    			if (StringUtils.hasText(requestCode))
    				query.setString("requestCode", requestCode);
    			
    			if (StringUtils.hasText(customerLastName))
    				query.setString("customerLastName", customerLastName);		

    			if (StringUtils.hasText(customerFristName))
    				query.setString("customerFristName", customerFristName);
    			
    			if (StringUtils.hasText(createdByName))
    				query.setString("createdByName", createdByName);
    			
    			if (StringUtils.hasText(itemBrand))
    				query.setString("itemBrand", itemBrand);
    			
    			if (requestDate != null)
    				query.setDate("requestDate", requestDate);

    			if (requestDate1 != null)
    				query.setDate("requestDate1", requestDate1);		

    			if (StringUtils.hasText(itemCode))
    				query.setString("itemCode", itemCode);
    			
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
    public List<AdCustServiceHead> findByemployee( HashMap findObjs ){
		final HashMap fos = findObjs;
		List<AdCustServiceHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from AdCustServiceHead as model ");
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

	
	public List<AdCustServiceHead> findByemployee(String rqInChargeCode) {

		final String employee_Code = rqInChargeCode;
	
		List<AdCustServiceHead> result = getHibernateTemplate().executeFind(
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
	
	  public List<AdCustServiceHead> findByPropertyId(String entityBean, String fieldNames[], Object fieldValue[], String orderFieldName) {

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
	  
	  public List<AdCustServiceLine> getItems(Long headId) {
			log.info("AdCustServiceDAO headId=" + headId);
			final Long id = headId;
			if (null != id) {
				List<AdCustServiceLine> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from AdCustServiceLine as item where ");
						hql.append(" adCustServiceHead.headId = :headId ");
						hql.append(" order by item.lineId ");
						Query query = session.createQuery(hql.toString());
						query.setLong("headId", id);
						return query.list();
					}
				});
				return temp;
			}
			return new ArrayList();
		}

}
