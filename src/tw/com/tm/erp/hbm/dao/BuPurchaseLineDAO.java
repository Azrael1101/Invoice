package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import tw.com.tm.erp.utils.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;

import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.ImMovementItem;


public class BuPurchaseLineDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuPurchaseLineDAO.class);

    public List<BuPurchaseLine> findAll() {
        log.debug("finding all BuPurchaseLine instances");
        try {
            String queryString = "from PrDetail";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
	public BuPurchaseLine findEosItemByItemNo(Long headId, String itemNo) {
		StringBuffer hql = new StringBuffer(
				"from BuPurchaseLine as model where model.buPurchaseHead.headId = ? and model.itemNo = ?");
		List<BuPurchaseLine> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, itemNo });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
    public List<BuPurchaseLine> findEosRequestDetailAll(HashMap conditionMap){
    	
    	
    	final String orderTypeCode = (String)conditionMap.get("orderTypeCode");
    	final String orderNo = (String)conditionMap.get("orderNo");
    	final String status = (String)conditionMap.get("status");
    	final String enable = (String)conditionMap.get("enable");
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String shopCode = (String)conditionMap.get("shopCode");
		 final String warehouseCode = (String)conditionMap.get("warehouseCode");
		 final String startDate = (String)conditionMap.get("startDate");
		 final String endDate = (String)conditionMap.get("endDate");
		 final String categoryItem = (String)conditionMap.get("categoryItem");

		 List<BuPurchaseLine> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select line ");
	                        hql.append(" from BuPurchaseHead as head ,BuPurchaseLine as line ");
	                        hql.append(" where 1=1 ");
	                        hql.append(" and line.buPurchaseHead.headId = head.headId ");
	                        hql.append(" and line.lineId is not null ");
	                        hql.append(" and head.orderTypeCode = 'EOS' ");
	                        hql.append(" and head.orderNo not like 'TMP%' ");
	                        
	                        if (StringUtils.hasText(orderNo))
	                        	hql.append(" and head.orderNo = :orderNo ");
	                        
	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        
	                        if (StringUtils.hasText(shopCode))
	                        	hql.append(" and head.department = :shopCode " );
	                        
	                        if (StringUtils.hasText(status)){
	                        	hql.append(" and head.status = :status ");
	                        }
	                        else{
	                        	hql.append(" and head.status not in ('SAVE','VOID') ");
	                        }
	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and line.enable = :enable ");
	                        if (StringUtils.hasText(warehouseCode))
	                        	hql.append(" and line.supplier IN ( "+warehouseCode+" ) ");
	                        
	                        if (StringUtils.hasText(categoryItem))
	                        {
	                        	hql.append(" and head.categoryItem = SOME( " );
	                        	hql.append(" select category.id.categoryCode " );
	                        	hql.append(" from ImItemCategory category " );
	                        	hql.append(" where category.parentCategoryCode = :categoryItem ) " );
	                        }
	                        if (null!=startDate)
	                        {
	                        	hql.append(" and head.requestDate >= to_date( :startDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        if (null!=endDate)
	                        {
	                        	hql.append(" and head.requestDate <= to_date( :endDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        hql.append(" order by head.department " );
	                        Query query = session.createQuery(hql.toString());

	                        if (StringUtils.hasText(orderNo))
	                            query.setString("orderNo", orderNo);
	                        
	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        
	                        if (StringUtils.hasText(shopCode))
	                            query.setString("shopCode", shopCode);
	                        
	                        if (StringUtils.hasText(status))
	                            query.setString("status", status);
	                        if (StringUtils.hasText(categoryItem))
	                            query.setString("categoryItem", categoryItem);

	                        

	                        if (StringUtils.hasText(startDate))
	                            query.setString("startDate", startDate);                        

	                        if (StringUtils.hasText(endDate))
	                            query.setString("endDate", endDate);
	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);

	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });

	        return result;
   	
   	
   }
    
    
    public List<BuPurchaseLine> findEosRequestDetail(HashMap conditionMap,int iSPage,int iPSize){
    	
    	
    	 final String orderTypeCode = (String)conditionMap.get("orderTypeCode");
    	 final String orderNo = (String)conditionMap.get("orderNo");
		 final String status = (String)conditionMap.get("status");
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String shopCode = (String)conditionMap.get("shopCode");
		 final String warehouseCode = (String)conditionMap.get("warehouseCode");
		 final String startDate = (String)conditionMap.get("startDate");
		 final String endDate = (String)conditionMap.get("endDate");
		 final String enable = (String)conditionMap.get("enable");
		 final String categoryItem = (String)conditionMap.get("categoryItem");
		 final int startPage = iSPage*iPSize;
		 final int pageSize = iPSize;
    	
		 List<BuPurchaseLine> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select line ");
	                        hql.append(" from BuPurchaseHead as head ,BuPurchaseLine as line ");
	                        hql.append(" where 1=1 ");
	                        hql.append(" and line.buPurchaseHead.headId = head.headId ");
	                        hql.append(" and line.lineId is not null ");
	                        hql.append(" and head.orderTypeCode = 'EOS' ");
	                        hql.append(" and head.orderNo not like 'TMP%' ");
	                        //需求單號 2016.10.24 Maco
	                        if (StringUtils.hasText(orderNo))
	                        	hql.append(" and head.orderNo = :orderNo ");

	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        
	                        if (StringUtils.hasText(shopCode))
	                        	hql.append(" and head.department = :shopCode " );
	                        
	                        if (StringUtils.hasText(warehouseCode))
	                        	hql.append(" and line.supplier IN ( "+warehouseCode+" ) ");
	                        if (StringUtils.hasText(status)){
	                        	hql.append(" and head.status = :status ");
	                        }
	                        else{
	                        	hql.append(" and head.status not in ('SAVE','VOID') ");
	                        }
	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and line.enable = :enable ");
	                        if (StringUtils.hasText(categoryItem))
	                        {
	                        	hql.append(" and head.categoryItem = SOME( " );
	                        	hql.append(" select category.id.categoryCode " );
	                        	hql.append(" from ImItemCategory category " );
	                        	hql.append(" where category.parentCategoryCode = :categoryItem ) " );
	                        }
	                        if (null!=startDate)
	                        {
	                        	hql.append(" and head.requestDate >= to_date( :startDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        if (null!=endDate)
	                        {
	                        	hql.append(" and head.requestDate <= to_date( :endDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        hql.append(" order by head.department " );
	                        Query query = session.createQuery(hql.toString());
	                        query.setFirstResult(0);
	                        query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
	                        //需求單號 2016.10.24 Maco
	                        if (StringUtils.hasText(orderNo))
	                            query.setString("orderNo", orderNo);
	                        
	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        
	                        if (StringUtils.hasText(shopCode))
	                            query.setString("shopCode", shopCode);
	                        
	                        if (StringUtils.hasText(status))
	                            query.setString("status", status);
	                        if (StringUtils.hasText(categoryItem))
	                            query.setString("categoryItem", categoryItem);
	                        if (StringUtils.hasText(startDate))
	                            query.setString("startDate", startDate);                        
	                        if (StringUtils.hasText(endDate))
	                            query.setString("endDate", endDate);
	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);
	                        
	                        log.info(hql.toString());
	                        log.info(query.toString());
	        				query.setFirstResult(startPage);
	        				query.setMaxResults(pageSize);
	                        return query.list();
	                    }
	                });

	        return result;
    	
    	
    }
    
    public Long findEosRequestDetailMaxIndex(HashMap conditionMap){
    	
    	
		 final String orderTypeCode = (String)conditionMap.get("orderTypeCode");
		 final String orderNo = (String)conditionMap.get("orderNo");
		 final String enable = (String)conditionMap.get("enable");
		 final String status = (String)conditionMap.get("status");
		 final String brandCode = (String)conditionMap.get("brandCode");
		 final String shopCode = (String)conditionMap.get("shopCode");
		 final String warehouseCode = (String)conditionMap.get("warehouseCode");
		 final String startDate = (String)conditionMap.get("startDate");
		 final String endDate = (String)conditionMap.get("endDate");
		 final String categoryItem = (String)conditionMap.get("categoryItem");
   	
		 List<BuPurchaseLine> result = getHibernateTemplate().executeFind(
	                new HibernateCallback() {
	                    public Object doInHibernate(Session session)
	                            throws HibernateException, SQLException {

	                        StringBuffer hql = new StringBuffer(" select line ");
	                        hql.append(" from BuPurchaseHead as head ,BuPurchaseLine as line ");
	                        hql.append(" where 1=1 ");
	                        hql.append(" and line.buPurchaseHead.headId = head.headId ");
	                        hql.append(" and line.lineId is not null ");
	                        hql.append(" and head.orderTypeCode = 'EOS' ");
	                        hql.append(" and head.orderNo not like 'TMP%' ");
	                        
	                        if (StringUtils.hasText(orderNo))
	                        	hql.append(" and head.orderNo = :orderNo ");
	                        
	                        if (StringUtils.hasText(brandCode))
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        
	                        if (StringUtils.hasText(shopCode))
	                        	hql.append(" and head.department = :shopCode " );
	                        
	                        if (StringUtils.hasText(warehouseCode))
	                        	hql.append(" and line.supplier IN ( "+warehouseCode+" ) ");
	                        if (StringUtils.hasText(status)){
	                        	hql.append(" and head.status = :status ");
	                        }
	                        else{
	                        	hql.append(" and head.status not in ('SAVE','VOID') ");
	                        }
	                        if (StringUtils.hasText(enable))
	                        	hql.append(" and line.enable = :enable ");
	                        if (StringUtils.hasText(categoryItem))
	                        {
	                        	hql.append(" and head.categoryItem = SOME( " );
	                        	hql.append(" select category.id.categoryCode " );
	                        	hql.append(" from ImItemCategory category " );
	                        	hql.append(" where category.parentCategoryCode = :categoryItem ) " );
	                        }
	                        if (null!=startDate)
	                        {
	                        	hql.append(" and head.requestDate >= to_date( :startDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        if (null!=endDate)
	                        {
	                        	hql.append(" and head.requestDate <= to_date( :endDate " );
	                        	hql.append(",'yyyy/mm/dd')" );
	                        }
	                        hql.append(" order by head.department  " );
	                        Query query = session.createQuery(hql.toString());

	                        if (StringUtils.hasText(orderNo))
	                            query.setString("orderNo", orderNo);
	                        
	                        if (StringUtils.hasText(brandCode))
	                            query.setString("brandCode", brandCode);
	                        
	                        if (StringUtils.hasText(shopCode))
	                            query.setString("shopCode", shopCode);
	                        

	                        if (StringUtils.hasText(status))
	                            query.setString("status", status);
	                        if (StringUtils.hasText(categoryItem))
	                            query.setString("categoryItem", categoryItem);
	                        if (StringUtils.hasText(startDate))
	                            query.setString("startDate", startDate);                        
	                        if (StringUtils.hasText(endDate))
	                            query.setString("endDate", endDate);
	                        if (StringUtils.hasText(enable))
	                            query.setString("enable", enable);

	                        log.info(hql.toString());
	                        log.info(query.toString());

	                        return query.list();
	                    }
	                });

	        return result.size()+0L;
   	
   	
   }
    public List<BuPurchaseLine> findLinebyStatus(Long headId , String enable) {
    	final Long vHeadId = headId;
    	final String vEnable = enable;
    		
        List<BuPurchaseLine> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from BuPurchaseLine as model");
                        hql.append(" where 1=1 ");
                        hql.append(" and buPurchaseHead.headId = "+ vHeadId +" ");
                        hql.append(" and enable <> '"+ vEnable +"' ");

                        Query query = session.createQuery(hql.toString());

                        return query.list();
                    }
                });

        return result;
    }
    
    public List<BuPurchaseLine> find(HashMap conditionMap) {

        final String itemNo = (String) conditionMap.get("itemNo");
        final String itemName = (String) conditionMap.get("itemName");
        final String specInfo = (String) conditionMap.get("specInfo");
        final String quantity = (String) conditionMap.get("quantity");
        final String purUnitPrice = (String) conditionMap.get("purUnitPrice");
        final String purTotalAmount = (String) conditionMap.get("purTotalAmount");
        final String supplier = (String) conditionMap.get("supplier");
      
     

        
        

        List<BuPurchaseLine> result = getHibernateTemplate().executeFind(
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
	
    public  List<BuPurchaseLine>  findLineByDeliveryOrderNo(Long headId, String orderTypeCode, String deliveryOrderNo) {
		log.info("findLineByDeliveryheadId..."+headId);
		log.info("findLineByDeliveryorderTypeCode..."+orderTypeCode);
		log.info("findLineByDeliveryorderTypeCode..."+deliveryOrderNo);
		final Long hId = headId;
		final String typeCode = orderTypeCode;
		final String orderNo = deliveryOrderNo;
		List<BuPurchaseLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<BuPurchaseLine> doInHibernate(Session session) throws HibernateException, SQLException {
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
		log.info("BuPurchaseLineLine.form:" + result.size());
		return result;
	}

    public BuPurchaseLine findByLocationName(Long orderTypeCode) {

            StringBuffer hql = new StringBuffer("from PrDetail as model ");
            hql.append("where model.orderTypeCode = ? ");
            List<BuPurchaseLine> lists = getHibernateTemplate().find(hql.toString(),
                    new Object[] { orderTypeCode });
            return (lists != null && lists.size() > 0 ? lists.get(0) : null);
        }
    
    public List<BuPurchaseLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("BuPurchaseLineDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<BuPurchaseLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from BuPurchaseLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.buPurchaseHead.headId = :headId order by indexNo");
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
    
    public List findPageLineAdDetail(Long headId, int startPage, int pageSize) {
		log.info("BuPurchaseLineDAO.findPageLineAdDetail headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<BuPurchaseLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from AdDetail as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.buPurchaseHead.headId = :headId order by menuId");
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
    
    public List findPageLineWareHouse(Long headId, int startPage, int pageSize) {
		log.info("BuPurchaseLineDAO.findPageLineWareHouse headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<BuPurchaseLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from AdDetail as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.buPurchaseHead.headId = :headId order by warehouseCode");
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
		List<BuPurchaseHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from BuPurchaseHead as model where 1=1 ");
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
			List<BuPurchaseLine> buPurchaseLines = re.get(0).getBuPurchaseLines();
			log.info("babu4000:"+buPurchaseLines.size());
			if( null != buPurchaseLines && buPurchaseLines.size() > 0 )
				return buPurchaseLines.get(buPurchaseLines.size()-1).getIndexNo() ;
		}
		return 0L;
	}
	
	public BuPurchaseLine findItemByIdentification(Long headId, Long lineId) {
		StringBuffer hql = new StringBuffer(
				"from BuPurchaseLine as model where model.buPurchaseHead.headId = ? and model.lineId = ?");
		List<BuPurchaseLine> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	 public BuPurchaseLine findById(Long lineId) {

         StringBuffer hql = new StringBuffer("from BuPurchaseLine as model ");
         hql.append("where model.lineId = ? ");
         List<BuPurchaseLine> lists = getHibernateTemplate().find(hql.toString(),
                 new Object[] { lineId });
         return (lists != null && lists.size() > 0 ? lists.get(0) : null);
     }
	 
		public BuPurchaseLine findById1(Long headId) {
			log.debug("getting BuPurchaseLine instance with id: " + headId);
			try {
				BuPurchaseLine instance = (BuPurchaseLine) getHibernateTemplate().get(
						"tw.com.tm.erp.hbm.bean.BuPurchaseLine", headId);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
		}
		
		public int updateByHeadId(final Long headId) {
			//final String finalLotNo = lotNo;
			int t = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("update pr_detail set index_no = line_priority where head_id =").append(headId);
					Query query = session.createSQLQuery(hql.toString());
					return query.executeUpdate();
				}
			});
			return t;
		}
		 public Integer findItemOnShip(HashMap conditionMap){
		    	
		    	
			 final String orderTypeCode = (String)conditionMap.get("orderTypeCode");
			 final String status = (String)conditionMap.get("status");
			 final String brandCode = (String)conditionMap.get("brandCode");
			 final String warehouseCode = (String)conditionMap.get("warehouseCode");
			 final String enable = (String)conditionMap.get("enable");
			 final String itemNo = (String)conditionMap.get("itemNo");
	   	
			 List<Integer> result = getHibernateTemplate().executeFind(
		                new HibernateCallback() {
		                    public Object doInHibernate(Session session)
		                            throws HibernateException, SQLException {

		                        StringBuffer hql = new StringBuffer(" select sum(line.quantity) ");
		                        hql.append(" from BuPurchaseHead as head ,BuPurchaseLine as line ");
		                        hql.append(" where 1=1 ");
		                        hql.append(" and line.buPurchaseHead.headId = head.headId ");
		                        hql.append(" and line.lineId is not null ");
		                        hql.append(" and head.orderTypeCode = 'EOS' ");
		                        hql.append(" and head.orderNo not like 'TMP%' ");
	                        	hql.append(" and head.brandCode = :brandCode ");
	                        	hql.append(" and head.status = :status ");
	                        	hql.append(" and line.supplier = :warehouseCode " );
	                        	hql.append(" and line.itemNo = :itemNo " );
	                        	hql.append(" and line.enable = :enable " );
		                        hql.append(" order by head.department " );
		                        
		                        
		                        Query query = session.createQuery(hql.toString());

		                        query.setString("brandCode", brandCode);
		                        query.setString("status", status);
		                        query.setString("itemNo", itemNo);
		                        query.setString("warehouseCode", warehouseCode);
		                        query.setString("enable", enable);


		                        log.info(hql.toString());
		                        log.info(query.toString());

		                        return query.list();
		                    }
		                });
			 	log.info(result.get(0));
			 	if(result.get(0) != null)
			 		return Integer.parseInt(String.valueOf(result.get(0)));
			 	else
			 		return 0;
	   	
	   }
			public List<BuPurchaseLine> getItems(Long headId) {

				final Long id = headId;
				if (null != id) {
					List<BuPurchaseLine> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session) throws HibernateException, SQLException {
							StringBuffer hql = new StringBuffer("from BuPurchaseLine as item where ");
							hql.append(" buPurchaseHead.headId = :headId ");
							hql.append(" order by item.itemNo ");
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
