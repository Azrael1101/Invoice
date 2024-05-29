package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
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
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;


public class AdDetailDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(AdDetailDAO.class);

    public List<AdDetail> findAll() {
        log.debug("finding all AdDetail instances");
        try {
            String queryString = "from AdDetail";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    

    public List<AdDetail> find(HashMap conditionMap) {

        final String itemNo = (String) conditionMap.get("itemNo");
        final String itemName = (String) conditionMap.get("itemName");
        final String specInfo = (String) conditionMap.get("specInfo");
        final String quantity = (String) conditionMap.get("quantity");
        final String purUnitPrice = (String) conditionMap.get("purUnitPrice");
        final String purTotalAmount = (String) conditionMap.get("purTotalAmount");
        final String supplier = (String) conditionMap.get("supplier");
      
     

        
        

        List<AdDetail> result = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        StringBuffer hql = new StringBuffer("select model from AdDetail as model");
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
	
    public  List<AdDetail>  findLineByDeliveryOrderNo(Long headId, String orderTypeCode, String deliveryOrderNo) {
		log.info("findLineByDeliveryheadId..."+headId);
		log.info("findLineByDeliveryorderTypeCode..."+orderTypeCode);
		log.info("findLineByDeliveryorderTypeCode..."+deliveryOrderNo);
		final Long hId = headId;
		final String typeCode = orderTypeCode;
		final String orderNo = deliveryOrderNo;
		List<AdDetail> result = getHibernateTemplate().executeFind(new HibernateCallback() {
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
    
    public List<AdDetail> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("BuPurchaseLineDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<AdDetail> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from AdDetail as model where 1=1 ");
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
			List<AdDetail> adDetails = re.get(0).getAdDetails();
			log.info("babu4000:"+adDetails.size());
			if( null != adDetails && adDetails.size() > 0 )
				return adDetails.get(adDetails.size()-1).getIndexNo() ;
		}
		return 0L;
	}
	
	public AdDetail findItemByIdentification(Long headId, Long lineId) {
		StringBuffer hql = new StringBuffer(
				"from AdDetail as model where model.buPurchaseHead.headId = ? and model.lineId = ?");
		List<AdDetail> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	 public AdDetail findById(Long lineId) {

         StringBuffer hql = new StringBuffer("from BuPurchaseLine as model ");
         hql.append("where model.lineId = ? ");
         List<AdDetail> lists = getHibernateTemplate().find(hql.toString(),
                 new Object[] { lineId });
         return (lists != null && lists.size() > 0 ? lists.get(0) : null);
     }
	 

}
