package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.utils.DateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImPromotion entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPromotion
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImPromotionDAO.class);
	// property constants
	public static final String BRAND_CODE = "brandCode";
	public static final String ORDER_TYPE_CODE = "orderTypeCode";
	public static final String ORDER_NO = "orderNo";
	public static final String PROMOTION_NAME = "promotionName";
	public static final String PROMOTION_CODE = "promotionCode";
	public static final String DESCRIPTION = "description";
	public static final String IN_CHARGE = "inCharge";
	public static final String IS_ALL_ITEM = "isAllItem";
	public static final String IS_ALL_SHOP = "isAllShop";
	public static final String IS_ALL_CUSTOMER = "isAllCustomer";
	public static final String DISCOUNT = "discount";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public boolean save(ImPromotion transientInstance) {
		boolean result = false;
		log.debug("saving ImPromotion instance");
		try {
			getHibernateTemplate().save(transientInstance);
			// log.debug("save successful");
			result = true;
		} catch (RuntimeException re) {
			result = false;
			// log.error("save failed", re);
			// throw re;
		}
		return result;
	}

	/**
	 * update ImPromotion
	 * 
	 * @param ImPromotion
	 */

	public boolean update(ImPromotion transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			// throw re;
			result = false;
		}
		return result;
	}

	public void delete(ImPromotion persistentInstance) {
		log.debug("deleting ImPromotion instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImPromotion findById(java.lang.Long id) {
		log.debug("getting ImPromotion instance with id: " + id);
		try {
			ImPromotion instance = (ImPromotion) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.ImPromotion", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImPromotion instance) {
		log.debug("finding ImPromotion instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImPromotion instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from ImPromotion as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBrandCode(Object brandCode) {
		return findByProperty(BRAND_CODE, brandCode);
	}

	public List findByOrderTypeCode(Object orderTypeCode) {
		return findByProperty(ORDER_TYPE_CODE, orderTypeCode);
	}

	public List findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}

	public List findByPromotionName(Object promotionName) {
		return findByProperty(PROMOTION_NAME, promotionName);
	}

	public List findByPromotionCode(Object promotionCode) {
		return findByProperty(PROMOTION_CODE, promotionCode);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByInCharge(Object inCharge) {
		return findByProperty(IN_CHARGE, inCharge);
	}

	public List findByIsAllItem(Object isAllItem) {
		return findByProperty(IS_ALL_ITEM, isAllItem);
	}

	public List findByIsAllShop(Object isAllShop) {
		return findByProperty(IS_ALL_SHOP, isAllShop);
	}

	public List findByIsAllCustomer(Object isAllCustomer) {
		return findByProperty(IS_ALL_CUSTOMER, isAllCustomer);
	}

	public List findByDiscount(Object discount) {
		return findByProperty(DISCOUNT, discount);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all ImPromotion instances");
		try {
			String queryString = "from ImPromotion";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImPromotion findByBrandCodeAndPromotionCode(String brandCode, String promotionCode) {
		try {
			StringBuffer hql = new StringBuffer("from ImPromotion as model");
			hql.append(" where model.brandCode = ?");
			hql.append(" and model.promotionCode = ?");

			List<ImPromotion> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, promotionCode });
			return (result.size() > 0 ? result.get(0) : null);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findPromotionInfo(Date orderDate,String brandCode,String shopCode,String itemCode,String priceType) {
		final Date oDate = orderDate;
		final String bCode = brandCode ;
		final String sCode = shopCode ; 
		final String iCode = itemCode ;
		final String pType = priceType ;
		//final String cType = customerType ;
		//final String vType = vipType ;
		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append(" SELECT h.promotion_code,i.discount_type,h.brand_code, h.promotion_name, h.in_charge,h.is_all_item, ");
				sql.append(" h.is_all_shop, h.is_all_customer, s.shop_code,s.begin_date, s.end_date, h.price_type, ");
				sql.append(" h.customer_type, i.item_code, i.discount, c.vip_type_code ");
				sql.append(" FROM erp.im_promotion h, ");
				sql.append(" 	       (SELECT h.head_id, i.item_code, i.discount_type, ");
				sql.append("            CASE ");
				sql.append(" 	                  WHEN i.discount IS NULL ");
				sql.append(" 	                     THEN h.discount ");
				sql.append(" 	                  ELSE i.discount ");
				sql.append(" 	               END discount ");
				sql.append(" 	          FROM erp.im_promotion h, erp.im_promotion_item i ");
				sql.append(" 	         WHERE h.head_id = i.head_id(+)) i, ");
				sql.append(" 	       (SELECT h.head_id, s.shop_code, ");
				sql.append(" 	               CASE ");
				sql.append(" 	                  WHEN s.begin_date IS NULL ");
				sql.append(" 	                     THEN h.begin_date ");
				sql.append(" 	                  ELSE s.begin_date ");
				sql.append(" 	               END begin_date, ");
				sql.append(" 	               CASE ");
				sql.append(" 	                  WHEN s.end_date IS NULL ");
				sql.append(" 	                     THEN h.end_date ");
				sql.append(" 	                  ELSE s.end_date ");
				sql.append(" 	               END end_date ");
				sql.append(" 	          FROM erp.im_promotion h, erp.im_promotion_shop s ");
				sql.append(" 	         WHERE h.head_id = s.head_id(+)) s, ");
				sql.append(" 	       (SELECT h.head_id, c.vip_type_code ");
				sql.append(" 	          FROM erp.im_promotion h, (SELECT c.head_id, c.vip_type_code ");
				sql.append(" 	                                  FROM erp.im_promotion_customer c ");
				sql.append(" 	                                 WHERE c.ENABLE = 'Y') c ");
				sql.append(" 	WHERE h.head_id = c.head_id(+)) c ");
				sql.append("	WHERE h.head_id = i.head_id ");
				sql.append(" 	   AND h.head_id = s.head_id ");
				sql.append(" 	   AND h.head_id = c.head_id ");
				sql.append(" 	   AND h.status = :status ");
				sql.append(" 	   AND :oDate BETWEEN s.begin_date AND s.end_date ");
				sql.append(" 	   AND h.brand_code = :bCode ");
				sql.append(" 	   AND (s.shop_code = :sCode OR s.shop_code is null ) ");
				sql.append(" 	   AND (i.item_code = :iCode OR i.item_code is null ) ");
				sql.append(" 	   AND h.price_type = :pType " );
				sql.append("       AND h.promotion_code not in " );
				sql.append("	   (select line.attribute2 from erp.BU_COMMON_PHRASE_LINE line where line.HEAD_CODE = 'VIPType' and line.attribute2 is not null ) ") ;
/*				if(StringUtils.hasText(cType)){
					sql.append("   AND (h.customer_type is NULL or h.customer_type = :cType ) ");
					sql.append("   AND (c.vip_type_code is NULL or c.vip_type_code = :vType) ");
				}	
				else{
					sql.append("   AND h.is_all_customer = 'Y' " );
				}*/
				Query query = session.createSQLQuery(sql.toString());
				query.setString("status", OrderStatus.FINISH);
				query.setDate("oDate", oDate );
				query.setString("bCode", bCode );				
				query.setString("sCode", sCode );
				query.setString("iCode", iCode );
				query.setString("pType", pType );
/*				if( StringUtils.hasText(cType) ){
					query.setString("cType", cType );
					query.setString("vType", vType );
				}	*/	
				return query.list();
			}
		});
		return re ;
	}

	public ImPromotion merge(ImPromotion detachedInstance) {
		log.debug("merging ImPromotion instance");
		try {
			ImPromotion result = (ImPromotion) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImPromotion instance) {
		log.debug("attaching dirty ImPromotion instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImPromotion instance) {
		log.debug("attaching clean ImPromotion instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public ImPromotion findImPromotion(String brandCode, String shopCode, String itemCode, Date orderDate) {
		log.debug("findImPromotion");

		StringBuffer hql = new StringBuffer("from ImPromotion as model , ImPromotionItem as item where");
		hql.append(" 	 model.brandCode = ? ");
		hql.append(" and model.beginDate >= ? ");
		hql.append(" and model.endDate <= ? ");
		hql.append(" and item.itemCode = ? ");
		getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
		List<ImPromotion> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, orderDate, orderDate, itemCode });
		return (result.size() > 0 ? result.get(0) : null);
	}

	public static ImPromotionDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ImPromotionDAO) ctx.getBean("imPromotionDAO");
	}

	/**
	 * 依據促銷單查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 */
        public List<ImPromotion> findPromotionList(HashMap conditionMap) {

            final String brandCode = (String) conditionMap.get("brandCode");
            final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	    final String startOrderNo = (String) conditionMap.get("startOrderNo");
	    final String endOrderNo = (String) conditionMap.get("endOrderNo");
	    final String status = (String) conditionMap.get("status");
	    final String employeeCode = (String) conditionMap.get("employeeCode");
	    final Date startDate = (Date) conditionMap.get("startDate");
	    final Date endDate = (Date) conditionMap.get("endDate");

	    List<ImPromotion> result = getHibernateTemplate().executeFind(
	        new HibernateCallback() {
		    public Object doInHibernate(Session session)
			throws HibernateException, SQLException {

		        StringBuffer hql = new StringBuffer("from ImPromotion as model ");
			hql.append("where model.brandCode = :brandCode and substring(model.orderNo,1,3) <> 'TMP'");
			if (StringUtils.hasText(orderTypeCode))
			    hql.append(" and model.orderTypeCode = :orderTypeCode");

			if (StringUtils.hasText(startOrderNo))
			    hql.append(" and model.orderNo >= :startOrderNo");

			if (StringUtils.hasText(endOrderNo))
			    hql.append(" and model.orderNo <= :endOrderNo");

			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");

			if (StringUtils.hasText(employeeCode))
			    hql.append(" and model.inCharge = :employeeCode");

			if (startDate != null)
		            hql.append(" and model.lastUpdateDate >= :startDate");

		        if (endDate != null)
			    hql.append(" and model.lastUpdateDate <= :endDate");
				
			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);

			if (StringUtils.hasText(orderTypeCode))
			    query.setString("orderTypeCode", orderTypeCode);

			if (StringUtils.hasText(startOrderNo))
			    query.setString("startOrderNo", startOrderNo);

			if (StringUtils.hasText(endOrderNo))
			    query.setString("endOrderNo", endOrderNo);

			if (StringUtils.hasText(status))
			    query.setString("status", status);

			if (StringUtils.hasText(employeeCode))
			    query.setString("employeeCode", employeeCode);
			 
			if (startDate != null)
			    query.setDate("startDate", startDate);

			if (endDate != null)
			    query.setDate("endDate", endDate);

			return query.list();
		    }
		});

	    return result;
	}
        
        /**
	 * 依據促銷單的條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 */
        public List<ImPromotion> findPromotionLists(HashMap conditionMap) {

            final String brandCode = (String) conditionMap.get("brandCode");
            final String orderTypeCode = (String) conditionMap.get("orderTypeCode");
	    final String startOrderNo = (String) conditionMap.get("startOrderNo");
	    final String endOrderNo = (String) conditionMap.get("endOrderNo");
	    final String status = (String) conditionMap.get("status");
	    final String employeeCode = (String) conditionMap.get("employeeCode");
	    final Date startDate = (Date) conditionMap.get("startDate");
	    final Date endDate = (Date) conditionMap.get("endDate");

	    List<ImPromotion> result = getHibernateTemplate().executeFind(
	        new HibernateCallback() {
		    public Object doInHibernate(Session session)
			throws HibernateException, SQLException {

		        StringBuffer hql = new StringBuffer("from ImPromotion as model ");
			hql.append("where model.brandCode = :brandCode and substring(model.orderNo,1,3) <> 'TMP'");
			if (StringUtils.hasText(orderTypeCode))
			    hql.append(" and model.orderTypeCode = :orderTypeCode");

			if (StringUtils.hasText(startOrderNo))
			    hql.append(" and model.orderNo >= :startOrderNo");

			if (StringUtils.hasText(endOrderNo))
			    hql.append(" and model.orderNo <= :endOrderNo");

			if (StringUtils.hasText(status))
			    hql.append(" and model.status = :status");

			if (StringUtils.hasText(employeeCode))
			    hql.append(" and model.inCharge = :employeeCode");

			if (startDate != null)
		            hql.append(" and model.lastUpdateDate >= :startDate");

		        if (endDate != null)
			    hql.append(" and model.lastUpdateDate <= :endDate");
				
			hql.append(" order by model.orderNo desc");
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);

			if (StringUtils.hasText(orderTypeCode))
			    query.setString("orderTypeCode", orderTypeCode);

			if (StringUtils.hasText(startOrderNo))
			    query.setString("startOrderNo", startOrderNo);

			if (StringUtils.hasText(endOrderNo))
			    query.setString("endOrderNo", endOrderNo);

			if (StringUtils.hasText(status))
			    query.setString("status", status);

			if (StringUtils.hasText(employeeCode))
			    query.setString("employeeCode", employeeCode);
			 
			if (startDate != null)
			    query.setDate("startDate", startDate);

			if (endDate != null)
			    query.setDate("endDate", endDate);

			return query.list();
		    }
		});

	    return result;
	}
        
        /**
         * 
         * @param findObjs
         * @param startPage
         * @param pageSize
         * @param searchType  
         * 	      1) get max record count 
         *        2) select data records according to startPage and pageSize 
         *        3) select all records 
         * @return
         */
        public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize, int searchType) {
    		
    	final HashMap fos = findObjs;
    	final int startRecordIndexStar = startPage * pageSize;
    	final int pSize = pageSize;
    	final int type = searchType;
    	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
    	        public Object doInHibernate(Session session)throws HibernateException, SQLException {
    	            
    	            String brandCode = (String)fos.get("brandCode");	    	    
    	    	    String orderTypeCode = (String)fos.get("orderTypeCode");
    	    	    String inCharge = (String)fos.get("inCharge");
    	    	    String status = (String)fos.get("status");
    	    	    String itemCategory = (String)fos.get("itemCategory");
    	    	    String startOrderNo = (String)fos.get("startOrderNo");
    	    	    String endOrderNo = (String)fos.get("endOrderNo");	    	    
    	    	    Date startDate = (Date)fos.get("startDate");
    	    	    Date endDate = (Date)fos.get("endDate");	    	    
    	    	    String itemCode = (String)fos.get("itemCode");
    	    	    String combineCode = (String)fos.get("combineCode");
    	    	    String foreignCategory = (String)fos.get("foreignCategory");
    	    	    //促銷增加搜尋欄位(wade)
    	    	    String itemBrand = fos.get("itemBrand") == null ? null : (String)fos.get("itemBrand");
    	    	    String category02 = fos.get("category02") == null ? null : (String)fos.get("category02");
    	    	    String category17 = fos.get("category17") == null ? null : (String)fos.get("category17");
    	    	    
    	    	    String startDateHql = "lastUpdateDate";
    	    	    String endDateHql = "lastUpdateDate";
    	    	    if("T2".equalsIgnoreCase(brandCode)){
	    	    		startDateHql = "beginDate"; 
	    	    		endDateHql = "endDate";
    	    	    }
    	    	    
    		    StringBuffer hql = new StringBuffer("");
    		    if(BaseDAO.QUERY_RECORD_COUNT == type) {
    		        hql.append("select count(model.headId) as rowCount from ImPromotion as model where 1=1 ");
    		    }else if(BaseDAO.QUERY_SELECT_ALL == type) {
    		    	hql.append("select model.headId from ImPromotion as model where 1=1 ");
    		    }else{
    		    	hql.append("from ImPromotion as model where 1=1 ");
    		    }
    		    
    		    hql.append("and model.brandCode=:brandCode and substring(model.orderNo,1,3) <> 'TMP' ");
    		    
    		    if (StringUtils.hasText(itemCode) || StringUtils.hasText(itemBrand) || StringUtils.hasText(category02) 
    		    	|| StringUtils.hasText(combineCode) || StringUtils.hasText(foreignCategory)){
    		    	//hql.append(" and model.headId IN (SELECT item.imPromotion FROM ImPromotionItem as item WHERE item.itemCode=:itemCode)");
    		    	int i = 0;
    		    	//hql.append(" and model.headId IN (SELECT item.imPromotion FROM ImPromotionItem as item WHERE item.itemCode=:itemCode or item.itemCode in (select itemCode from ImItem where 1=1 and itemBrand=:itemBrand and category02=:category02))");
    		    	
    		    	if(orderTypeCode.equals("PMC")){
    		    		hql.append("  and model.headId IN (SELECT item.imPromotion FROM ImPromotionReCombineMod as item WHERE 1=1 ");
    		    		if(StringUtils.hasText(itemBrand)) hql.append(" and itemBrand=:itemBrand");
    		    		if(StringUtils.hasText(category02)) hql.append(" and category02=:category02");
    		    		if(StringUtils.hasText(combineCode)) hql.append(" and combineCode=:combineCode");
    		    		if(StringUtils.hasText(foreignCategory)) hql.append(" and foreignCategory=:foreignCategory");
    		    		hql.append(")");
    		    	}else if(orderTypeCode.equals("PMS")){
    		    		hql.append("  and model.headId IN (SELECT item.imPromotion FROM ImPromotionGroupedProductMod as item WHERE 1=1 ");
    		    		if(StringUtils.hasText(combineCode)) hql.append(" and combineCode=:combineCode");
    		    		if(StringUtils.hasText(itemCode)) hql.append(" and itemCode=:itemCode");
    		    		hql.append(")");

    		    	}else{
    		    		hql.append("  and model.headId IN (SELECT item.imPromotion FROM ImPromotionItem as item WHERE ");
    		    		if(StringUtils.hasText(itemCode)){
        		    		hql.append(" item.itemCode=:itemCode");
        		    		i++;
        		    	} 
        		    	
        		    	if(StringUtils.hasText(itemBrand) || StringUtils.hasText(category02) || StringUtils.hasText(category17)){
        		    		if(i>0) hql.append(" or ");
        		    		hql.append(" item.itemCode in (select itemCode from ImItem where 1=1 ");
        		    		if(StringUtils.hasText(itemBrand)) hql.append(" and itemBrand=:itemBrand");
        		    		if(StringUtils.hasText(category02)) hql.append(" and category02=:category02");
        		    		if(StringUtils.hasText(category17)) hql.append(" and category17=:category17");
        		    		hql.append(")");
        		    	} 
        		    	hql.append(")");
    		    	}
    		    }
    		    
    		    if (StringUtils.hasText(orderTypeCode))
    		        hql.append(" and model.orderTypeCode = :orderTypeCode");
    		        
    		    if (StringUtils.hasText(inCharge))
    		        hql.append(" and model.inCharge = :inCharge"); 
    		        
    		    if (StringUtils.hasText(status))
    		        hql.append(" and model.status = :status");
    		        
    		    if (StringUtils.hasText(itemCategory))
    		        hql.append(" and model.itemCategory = :itemCategory");
    		        
    		    if (StringUtils.hasText(startOrderNo))
    		        hql.append(" and model.orderNo >= :startOrderNo");
    		        
    		    if (StringUtils.hasText(endOrderNo))
    		        hql.append(" and model.orderNo <= :endOrderNo");
    		    
    		    if(orderTypeCode.equals("PMC")){
    		    	if (startDate != null){
    		    		hql.append(" and model."+startDateHql+" = :startDate");
    		    	}
    		    }else{
    		    	if (startDate != null){
    		    		hql.append(" and model."+startDateHql+" >= :startDate");
    		    	}        		        
    		    }
    		    if (endDate != null)
    			hql.append(" and model."+endDateHql+" <= :endDate");
    		    
    		    hql.append(" order by model.orderNo desc");
    		    //System.out.println("hql=[" + hql.toString() + "]");
    		    Query query = session.createQuery(hql.toString());
    		    if(BaseDAO.QUERY_SELECT_RANGE == type) {
    		        query.setFirstResult(startRecordIndexStar);
    		        query.setMaxResults(pSize);
    		    }
    		    
    		    query.setString("brandCode", brandCode);
    		    
    		    if(StringUtils.hasText(combineCode))
    		    	query.setString("combineCode", combineCode);
    		    
    		    if(StringUtils.hasText(foreignCategory))
    		    	query.setString("foreignCategory", foreignCategory);
    		    
    		    if(StringUtils.hasText(itemBrand))
    		    	query.setString("itemBrand", itemBrand);
    		    
    		    if(StringUtils.hasText(category02))
    		    	query.setString("category02", category02);
    		    
    		    if(StringUtils.hasText(category17))
    		    	query.setString("category17", category17);
    		    	
    		    if (StringUtils.hasText(orderTypeCode))
    		        query.setString("orderTypeCode", orderTypeCode);
    		        
    		    if (StringUtils.hasText(inCharge))
    		        query.setString("inCharge", inCharge.trim().toUpperCase());
    		        
    		    if (StringUtils.hasText(status))
    		        query.setString("status", status);
    		        
    		    if (StringUtils.hasText(itemCategory))
    		        query.setString("itemCategory", itemCategory);
    		        
    		    if (StringUtils.hasText(startOrderNo))
    		        query.setString("startOrderNo", startOrderNo.trim());
    		        
    		    if (StringUtils.hasText(endOrderNo))
    		        query.setString("endOrderNo", endOrderNo.trim());        

    		    if (startDate != null)
    		        query.setDate("startDate", startDate);
    		    
    		    if (endDate != null)
    		    	query.setDate("endDate", endDate);
    		    
    		    if (StringUtils.hasText(itemCode))
    		        query.setString("itemCode", itemCode);	
    		    return query.list();
    		}
    	});
    	HashMap returnResult = new HashMap();		
    	returnResult.put(BaseDAO.TABLE_LIST, BaseDAO.QUERY_SELECT_ALL == type || BaseDAO.QUERY_SELECT_RANGE == type? result: null);			
    	if(result.size() == 0){
    	    returnResult.put(BaseDAO.TABLE_RECORD_COUNT, 0L);
    	}else{
    	    returnResult.put(BaseDAO.TABLE_RECORD_COUNT, BaseDAO.QUERY_SELECT_ALL == type || BaseDAO.QUERY_SELECT_RANGE == type? result.size() : Long.valueOf(result.get(0).toString()));
    	}
    	return returnResult;
        }
        
        public List getExcePromItemData(Map parameterMap) {
//    	Date startDate = (Date)parameterMap.get("DATA_DATE_STRAT");
//		Date endDate = (Date)parameterMap.get("DATA_DATE_END");
	
    	final String brandCode = (String) parameterMap.get("BRAND_CODE");
		final String promotionDate = (String) parameterMap.get("dataDate");
		final String promotionDateEnd = (String) parameterMap.get("dataDateEnd");
		
		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("select model, model2 FROM ImPromation as model, ImPromotionItem as model2 ");
				hql.append("where model.brandCode = :brandCode ");
				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				hql.append("and model.headId = model2.headId ");
				
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("promotionDate", promotionDate);
				query.setString("promotionDateEnd", promotionDateEnd);
					
				return query.list();
			}
		});
		return result ;
	}
       
        public List<ImPromotion> findImPromotionByProperty(Date endDate, String itemCategory) {
        	log.info("b1:"+endDate+" "+itemCategory);
    		StringBuffer hql = new StringBuffer("from ImPromotion as model where model.endDate = ?");
    		hql.append(" and model.itemCategory = ?");
    		List<ImPromotion> result = getHibernateTemplate().find(hql.toString(), new Object[] { endDate, itemCategory });
    		return result;
    	}
        
        public List<ImPromotion> findImPromotionByPropertySteve(String endDate, String itemCategory) {
        	
    		StringBuffer hql = new StringBuffer("from ImPromotion as model where model.endDate = to_Date(?,'yyyy/mm/dd')");
    		hql.append(" and model.itemCategory = ?");
    		log.info("b1:"+endDate+" "+itemCategory+" "+hql);
    		List<ImPromotion> result = getHibernateTemplate().find(hql.toString(), new Object[] { endDate, itemCategory });
    		return result;
    	}
        
        public List getPromotionDataByBeginAndEnd(Map parameterMap, String isEnable) {
//        	Date startDate = (Date)parameterMap.get("DATA_DATE_STRAT");
//    		Date endDate = (Date)parameterMap.get("DATA_DATE_END");
    	
        	final String brandCode = (String) parameterMap.get("brandCode");
    		final String promotionDate = (String) parameterMap.get("dataDate");
    		final String promotionDateEnd = (String) parameterMap.get("dataDateEnd");
    		final String orderTypeCode = (String) parameterMap.get("orderTypeCode");
    		final String enable = isEnable;
    		
    		log.info(brandCode);
    		log.info(promotionDate);
    		log.info(promotionDateEnd);
    		log.info(orderTypeCode);
    		
    		List result = getHibernateTemplate().executeFind(
    			new HibernateCallback() {
    			public Object doInHibernate(Session session) throws HibernateException, SQLException {
    				StringBuffer hql = new StringBuffer();
    				hql.append("select model FROM ImPromotion as model ");
    				hql.append("where model.status = :status ");
    				hql.append("and model.brandCode = :brandCode ");
    				if("Y".equals(enable)){
	    				hql.append("and to_char(model.beginDate, 'YYYYMMDD') = :dataDate ");
    				}else{
    					hql.append("and to_char(model.endDate, 'YYYYMMDD') = :dataDate ");
    				}
    				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :lastUpdateDate ");
    				hql.append("and model.orderTypeCode = :orderTypeCode ");
    				hql.append("order by headId ");
    				log.info("hql: "+hql.toString());
    				Query query = session.createQuery(hql.toString());
    				query.setString("status", "FINISH");
    				query.setString("brandCode", brandCode);
    				query.setString("dataDate", promotionDate);
    				query.setString("lastUpdateDate", promotionDateEnd);
    				query.setString("orderTypeCode", orderTypeCode);
    					
    				return query.list();
    			}
    		});
    		return result ;
    	}
        
        public List getPromotionDataItemByBeginAndEnd(Map parameterMap, String isEnable) {
//        	Date startDate = (Date)parameterMap.get("DATA_DATE_STRAT");
//    		Date endDate = (Date)parameterMap.get("DATA_DATE_END");
    	
        	final String brandCode = (String) parameterMap.get("brandCode");
    		final String promotionDate = (String) parameterMap.get("dataDate");
    		final String promotionDateEnd = (String) parameterMap.get("dataDateEnd");
    		final String orderTypeCode = (String) parameterMap.get("orderTypeCode");
    		final String enable = isEnable;
    		
    		log.info(brandCode);
    		log.info(promotionDate);
    		log.info(promotionDateEnd);
    		log.info(orderTypeCode);
    		
    		List result = getHibernateTemplate().executeFind(
    			new HibernateCallback() {
    			public Object doInHibernate(Session session) throws HibernateException, SQLException {
    				StringBuffer hql = new StringBuffer();
    				hql.append("select model, model2 FROM ImPromotion as model, ImPromotionFull as model2 ");
    				hql.append("where model.headId = model2.ImPromotion.headId ");
    				hql.append("and model.brandCode = :brandCode ");
    				if("Y".equals(enable)){
	    				hql.append("and to_char(model.beginDate, 'YYYYMMDD') = :dataDate ");
    				}else{
    					hql.append("and to_char(model.endDate, 'YYYYMMDD') = :dataDate ");
    				}
    				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
    				hql.append("and model.orderTypeCode = :orderTypeCode ");
    				log.info("hql: "+hql.toString());
    				Query query = session.createQuery(hql.toString());
    				query.setString("brandCode", brandCode);
    				query.setString("dataDate", promotionDate);
    				query.setString("dataDateEnd", promotionDateEnd);
    				query.setString("orderTypeCode", orderTypeCode);
    					
    				return query.list();
    			}
    		});
    		return result ;
    	}
        public List<ImPromotion> findPromotionDataByPromotionCodePD(String propertyName){
        	
        	try {
        		String queryString = "from ImPromotion as model where model." + propertyName + " like ? and model.enable = 'Y'";
        		log.info(queryString);
    			return getHibernateTemplate().find(queryString, "PD%");
    		} catch (RuntimeException re) {
    			log.error("find by PromotionCode with PD failed", re);
    			throw re;
    		}
        	
        }
}