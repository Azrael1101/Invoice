package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.utils.DateUtils;

public class ImReceiveHeadDAO extends BaseDAO<ImReceiveHead> { 
	private static final Log log = LogFactory.getLog(ImReceiveHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	

	public ImReceiveHead findById(Long headId) {
		ImReceiveHead instance = (ImReceiveHead) findByPrimaryKey(ImReceiveHead.class, headId);
		return instance;
	}

	public List<ImReceiveHead> find(HashMap findObjs) {
		log.info("ImReceiveHeadDAO.getLastForeignUnitPrice");
		final HashMap fos = findObjs;

		List<ImReceiveHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImReceiveHead as model where 1=1 and substring(model.orderNo,1,3) <> 'TMP' ");
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("declarationNo")))
					hql.append(" and model.declarationNo = :declarationNo ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.orderNo >= :startOrderNo ");

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.orderNo <= :endOrderNo ");

				if (null != fos.get("orderDateS"))
					hql.append(" and model.orderDate >= :orderDateS ");

				if (null != fos.get("orderDateE"))
					hql.append(" and model.orderDate <= :orderDateE ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				hql.append(" order by orderNo desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("declarationNo")))
					query.setString("declarationNo", (String) fos.get("declarationNo"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));

				if (null != fos.get("orderDateS"))
					query.setDate("orderDateS", (Date) fos.get("orderDateS"));

				if (null != fos.get("orderDateE"))
					query.setDate("orderDateE", (Date) fos.get("orderDateE"));

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));

				return query.list();
			}
		});

		return re;
	}

	/**
	 * 商品上次進貨價格
	 * 
	 * @param itemCode
	 * @return
	 */
	public Double getLastForeignUnitPrice(String itemCode) {
		log.info("ImReceiveHeadDAO.getLastForeignUnitPrice itemCode=" + itemCode);
		final String iCode = itemCode;
		Double re = new Double(0);
		if (StringUtils.hasText(iCode)) {
			List<ImReceiveItem> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {

					StringBuffer hql = new StringBuffer("from ImReceiveItem as item where ");
					hql.append(" imReceiveHead.status in ('").append(OrderStatus.FINISH).append("','").append(OrderStatus.CLOSE).append("')");
					hql.append(" and imReceiveHead.orderTypeCode not in ('EOF','EOP','RRF','RRL') ");
					hql.append(" and item.itemCode = '").append(iCode).append("'");
					hql.append(" order by imReceiveHead.orderDate desc ");
					//log.info("XXXXXXXXXXXXXXXXXXXX:"+hql.toString());
					Query query = session.createQuery(hql.toString());
					//query.setString("status", OrderStatus.FINISH);
					//query.setString("iCode", iCode);
					query.setFirstResult(0);
					query.setMaxResults(1);
					return query.list();
				}
			});
			if ((null != temp) && (temp.size() > 0)) {
				re = temp.get(0).getForeignUnitPrice();
			}
		}
		return re;
	}

	/**
	 * 商品上次進貨價格
	 * 
	 * @param itemCode
	 * @return
	 */
	public List<ImReceiveItem> getItemsByOrder(Long headId) {
		log.info("ImReceiveHeadDAO.getItemsByOrder headId=" + headId);
		final Long id = headId;
		if (null != id) {
			List<ImReceiveItem> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("from ImReceiveItem as item where ");
					hql.append(" imReceiveHead.headId = :headId ");
					hql.append(" order by item.itemCode ");
					Query query = session.createQuery(hql.toString());
					query.setLong("headId", id);
					return query.list();
				}
			});
			return temp;
		}
		return new ArrayList();
	}

	/**
	 * 已核銷 = 已核銷入庫單台幣合計(IM_RECEIVE VERIFICATION_STATUS = Y , ITEM_CODE ,
	 * LOCAL_AMOUNT )
	 * 
	 * @param brandCode
	 * @param itemCode
	 * @return
	 */
	public Double getVerificationAmount(String brandCode, String itemCode) {
		/*
		 * String status = "FINISH"; Double re = new Double(0); StringBuffer hql =
		 * new StringBuffer(); hql.append("select sum(imrI.local_amount) as
		 * total "); hql.append("from im_receive_head imrH , im_receive_item
		 * imrI "); hql.append("where imrH.status = ? and imrH.barndCode = ? and
		 * imrI.item_code = ? "); List result =
		 * getHibernateTemplate().find(hql.toString(), new Object[] { status ,
		 * brandCode , itemCode }); if ((null != result) && (result.size() > 0)) {
		 * re = (Double) result.get(0); } return re;
		 */

		log.info("ImReceiveHeadDAO.getVerificationAmount brandCode=" + brandCode + " itemCode=" + itemCode);
		final String fbrandCode = brandCode;
		final String fitemCode = itemCode;
		final String iCode = itemCode;
		Double re = new Double(0);
		if (StringUtils.hasText(iCode)) {
			List temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("select sum(imrI.local_amount) as total ");
					hql.append("from erp.im_receive_head imrH , erp.im_receive_item imrI ");
					hql.append("where imrH.status = :status and imrH.brand_Code = :brandCode and imrI.item_code = :itemCode ");
					Query query = session.createSQLQuery(hql.toString());
					query.setString("status", OrderStatus.FINISH);
					query.setString("brandCode", fbrandCode);
					query.setString("itemCode", fitemCode);
					return query.list();
				}
			});
			if ((null != temp) && (temp.size() > 0) && (null != (Double) temp.get(0))) {
				re = (Double) temp.get(0);
			}
		}
		return re;

	}

	/**
	 * 查詢關帳日之前還沒有完成關帳的單進貨單
	 * 
	 * @param brandCodeArray
	 * @param orderDate
	 * @param statusArray
	 * @return
	 */
	public List<ImReceiveHead> findImReceiveOrderByCriteria(final String[] brandCodeArray, final Date orderDate, final String[] statusArray) {
		log.info("ImReceiveHeadDAO.findImReceiveOrderByCriteria brandCodeArray=" + brandCodeArray + ",orderDate=" + orderDate
				+ ",statusArray=" + statusArray);
		final Date oDate = DateUtils.getShortDate(orderDate);
		List<ImReceiveHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria ctr = session.createCriteria(ImReceiveHead.class);
				ctr.add(Expression.le("orderDate", oDate));
				ctr.add(Expression.in("brandCode", brandCodeArray));
				ctr.add(Expression.in("status", statusArray));
				ctr.addOrder(Order.asc("brandCode"));
				ctr.addOrder(Order.asc("orderTypeCode"));
				ctr.addOrder(Order.asc("orderNo"));
				return ctr.list();
			}
		});

		return result;
	}

	/**
	 * 依據品牌及狀態查詢進貨單
	 * 
	 * @param brandCode
	 * @param status
	 * @return
	 */
	public List<ImReceiveHead> findReceiveByProperty(String brandCode, String status) {
		log.info("ImReceiveHeadDAO.findReceiveByProperty brandCode=" + brandCode + ",status=" + status);
		StringBuffer hql = new StringBuffer("from ImReceiveHead as model where model.brandCode = ?");
		hql.append(" and model.status = ?");
		List<ImReceiveHead> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, status });
		return result;
	}

	public List<ImReceiveHead> findReceiveByProperty(final String brandCode, final String status, final String orderType, final String orderTypeCode,  final Date startDate, final Date endDate) {
		log.info("findReceiveByProperty brandCode=" + brandCode+"/"+orderTypeCode +"/"+DateUtils.format(startDate)+ "/"+DateUtils.format(endDate));
			List<ImReceiveHead> temp = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("from ImReceiveHead as model where 1 = 1");
					hql.append(" and model.brandCode = :brandCode ");
					hql.append(" and model.status = :status ");
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
					
					if (null != startDate){
						if("T2".equalsIgnoreCase(brandCode)){
							if("RR".equalsIgnoreCase(orderType)){
								hql.append(" and model.warehouseInDate >= :startDate ");
							}else{
								hql.append(" and model.receiptDate >= :startDate ");
							}
						}else{
							hql.append(" and model.orderDate >= :startDate ");
						}
					}
					
					if (null != endDate){
						if("T2".equalsIgnoreCase(brandCode)){
							if("RR".equalsIgnoreCase(orderType)){
								hql.append(" and model.warehouseInDate <= :endDate ");
							}else{
								hql.append(" and model.receiptDate <= :endDate ");
							}
						}else{
							hql.append(" and model.orderDate <= :endDate ");
						}
					}
					
					if("T2".equalsIgnoreCase(brandCode)){
						if("RR".equalsIgnoreCase(orderType)){
							hql.append(" order by warehouseInDate asc ");
						}else{
							hql.append(" order by receiptDate asc ");
						}
					}else{
						hql.append(" order by orderDate asc ");
					}
					
					Query query = session.createQuery(hql.toString());
					query.setString("brandCode", brandCode);
					query.setString("status", status);
					query.setString("orderTypeCode", orderTypeCode);
					if (null != startDate){
						query.setDate("startDate", startDate);
					}
					if (null != endDate){
						query.setDate("endDate"  , endDate);
					}
					
					return query.list();
				}
			});
			return temp;
	}
	
	/**
	 * remove tmp order
	 */
	public void removeTmpOrder() {
		List<ImReceiveHead> removeOrders = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImReceiveHead as model where substring(model.orderNo,1,3) = 'TMP' ");
				Query query = session.createQuery(hql.toString());
				return query.list();
			}
		});
		getHibernateTemplate().deleteAll(removeOrders);
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
			
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final String type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)throws HibernateException, SQLException {
		            
		String brandCode     		= (String) fos.get("brandCode");
		String orderTypeCode 		= (String) fos.get("orderTypeCode");
		Date startDate       		= (Date) fos.get("startDate");
		Date endDate         		= (Date) fos.get("endDate");
		String status        		= (String) fos.get("status");
		String startOrderNo  		= (String) fos.get("startOrderNo");
		String endOrderNo    		= (String) fos.get("endOrderNo");
		String startSourceOrderNo  	= (String) fos.get("startSourceOrderNo");
		String endSourceOrderNo    	= (String) fos.get("endSourceOrderNo");
		String declarationNo 		= (String) fos.get("declarationNo");	    
		String itemCategory  		= (String) fos.get("itemCategory");
		String warehouseStatus    	= (String) fos.get("warehouseStatus");
		String expenseStatus    	= (String) fos.get("expenseStatus");
		String supplierCode    		= (String) fos.get("supplierCode");
		String startDateDiff    	= (String) fos.get("startDateDiff");
		String endDateDiff    		= (String) fos.get("endDateDiff");
		String itemCode    			= (String) fos.get("itemCode");
		Date startIDate       		= (Date) fos.get("startIDate");
		Date endIDate         		= (Date) fos.get("endIDate");
		Date startRDate       		= (Date) fos.get("startRDate");
		Date endRDate         		= (Date) fos.get("endRDate");

		
		StringBuffer hql = new StringBuffer("");
		
		if(QUARY_TYPE_RECORD_COUNT.equals(type)) {
		    hql.append("select count(model.headId) as rowCount from ImReceiveHead as model where 1=1 ");
		}else if(QUARY_TYPE_SELECT_ALL.equals(type)) {
		    hql.append("select model.headId from ImReceiveHead as model where 1=1 ");
		}else{
		    hql.append("from ImReceiveHead as model where 1=1 ");
		}

		hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
		
		if (StringUtils.hasText(brandCode))
		    hql.append(" and model.brandCode = :brandCode ");

		if (StringUtils.hasText(orderTypeCode))
		    hql.append(" and model.orderTypeCode = :orderTypeCode ");
			
		if (StringUtils.hasText(startOrderNo))
		    hql.append(" and model.orderNo >= :startOrderNo");

		if (StringUtils.hasText(endOrderNo))
		    hql.append(" and model.orderNo <= :endOrderNo");

		if (StringUtils.hasText(startSourceOrderNo))
		    hql.append(" and model.sourceOrderNo >= :startSourceOrderNo");

		if (StringUtils.hasText(endSourceOrderNo))
		    hql.append(" and model.sourceOrderNo <= :endSourceOrderNo");

		if (StringUtils.hasText(status))
		    hql.append(" and model.status = :status");

		if (StringUtils.hasText(declarationNo))
		    hql.append(" and model.declarationNo = :declarationNo");

		if (startDate != null)
		    hql.append(" and model.orderDate >= :startDate");

		if (endDate != null)
		    hql.append(" and model.orderDate <= :endDate");
		
		if (StringUtils.hasText(itemCategory))
		    hql.append(" and model.itemCategory = :itemCategory");
		
		if (StringUtils.hasText(supplierCode))
		    hql.append(" and model.supplierCode like :supplierCode");
		
		if (StringUtils.hasText(warehouseStatus))
		    hql.append(" and model.warehouseStatus = :warehouseStatus");
		
		if (StringUtils.hasText(expenseStatus))
		    hql.append(" and model.expenseStatus = :expenseStatus");
		
		if (StringUtils.hasText(itemCode))
			hql.append(" and model.headId IN(SELECT item.imReceiveHead FROM ImReceiveItem as item WHERE item.itemCode=:itemCode)");
	    
		if (startIDate != null)
		    hql.append(" and model.warehouseInDate >= :startIDate");

		if (endIDate != null)
		    hql.append(" and model.warehouseInDate <= :endIDate");
		
		if (startRDate != null)
		    hql.append(" and model.receiptDate >= :startRDate");

		if (endRDate != null)
		    hql.append(" and model.receiptDate <= :endRDate");
		
		if (StringUtils.hasText(startDateDiff))
			hql.append(" and model.receiptDate - model.warehouseInDate  >= :startDateDiff");
	    
		if (StringUtils.hasText(endDateDiff))
			hql.append(" and model.receiptDate - model.warehouseInDate  <= :endDateDiff");
		
		hql.append(" order by model.orderNo desc");
		
		log.info(hql.toString());
		
		Query query = session.createQuery(hql.toString());
		if( QUARY_TYPE_SELECT_RANGE.equals(type)) {
		    query.setFirstResult(startRecordIndexStar);
		    query.setMaxResults(pSize);
		}
							
		if (StringUtils.hasText(brandCode))
		    query.setString("brandCode", brandCode);

		if (StringUtils.hasText(orderTypeCode))
		    query.setString("orderTypeCode", orderTypeCode);
			    		    
		if (StringUtils.hasText(startOrderNo))
		    query.setString("startOrderNo", startOrderNo);

		if (StringUtils.hasText(endOrderNo))
		    query.setString("endOrderNo", endOrderNo);

		if (StringUtils.hasText(status))
		    query.setString("status", status);

		if (StringUtils.hasText(declarationNo))
		    query.setString("declarationNo", declarationNo);

		if (startDate != null)
		    query.setDate("startDate", startDate);

		if (endDate != null)
		    query.setDate("endDate", endDate);
		
		if (StringUtils.hasText(itemCategory))
		    query.setString("itemCategory", itemCategory);
		
		if (StringUtils.hasText(supplierCode))
		    query.setString("supplierCode", supplierCode);
							
		if (StringUtils.hasText(warehouseStatus))
		    query.setString("warehouseStatus", warehouseStatus);
		
		if (StringUtils.hasText(expenseStatus))
		    query.setString("expenseStatus", expenseStatus);
		
	    if (StringUtils.hasText(itemCode))
	        query.setString("itemCode", itemCode);	
	    
		if (startIDate != null)
		    query.setDate("startIDate", startIDate);

		if (endIDate != null)
		    query.setDate("endIDate", endIDate);
		
		if (startRDate != null)
		    query.setDate("startRDate", startRDate);

		if (endRDate != null)
		    query.setDate("endRDate", endRDate);
		
		if (StringUtils.hasText(startDateDiff))
	        query.setString("startDateDiff", startDateDiff);
		
		if (StringUtils.hasText(endDateDiff))
	        query.setString("endDateDiff", endDateDiff);
		
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
     
     
     /** 依傳入 itemCode 取得最近的 ImReceiveHead.orderNo
      * @param itemCode
      * @return
      */
     public Long findOrderNoByItemCode(final String brandCode, final String orderTypeCode, final String itemCode)
     		throws Exception{
	 try{
	     List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		 public Object doInHibernate(Session session)throws HibernateException, SQLException {

		     StringBuffer hql = new StringBuffer(
			"select h.headId from ImReceiveHead h, ImReceiveItem i " + 
		     	" where h.status in('FINISH','CLOSE')" );
		     hql.append(" and h.brandCode ='").append(brandCode).append("'");
		     hql.append(" and h.orderTypeCode ='").append(orderTypeCode).append("'");
		     hql.append(" and i.imReceiveHead = h.headId ");
		     hql.append(" and i.itemCode ='").append(itemCode).append("'");
		     hql.append(" order by h.lastUpdateDate desc ");
		     log.info(hql.toString());
		     Query query = session.createQuery(hql.toString());
		     return query.list();
		 }
	     });
	     if(result.size() == 0){
		 return null;
	     }else{
		 return (Long)result.get(0);
	     }
	 } catch (Exception e) {
	     log.info(e);
	     e.printStackTrace();
	 }
	 return null;
     }
     
     
     /**
	 * 依據品牌代號、單別、單號查詢調撥單
	 * 
	 * @param brandCode
	 * @param orderTypeCode
	 * @param orderNo
	 * @return ImMovementHead
	 */
	 public ImReceiveHead findByIdentification(String brandCode, String orderTypeCode, String orderNo) throws Exception{
		 try{
	     StringBuffer hql = new StringBuffer(
	         "from ImReceiveHead as model where model.brandCode = ?");
	     hql.append(" and model.orderTypeCode = ?");
           hql.append(" and model.orderNo = ?");
	     List<ImReceiveHead> result = getHibernateTemplate().find(
	         hql.toString(), new Object[] { brandCode, orderTypeCode, orderNo });
           return (result != null && result.size() > 0 ? result.get(0) : null);
		 }catch (Exception e){
			 throw new Exception("查無原單號");
		 }
	 }
	 
	 
	 /** 依傳入 itemCode 取得最近的 ImReceiveHead.orderDate
	  * @param itemCode
	  * @return
	  */
	 public Date findOrderDateByItemCode(final String brandCode, final String itemCode)
	 throws Exception{
		 try{
			 List result = getHibernateTemplate().executeFind(new HibernateCallback() {
				 public Object doInHibernate(Session session)throws HibernateException, SQLException {

					 StringBuffer hql = new StringBuffer(
							 "select h.orderDate from ImReceiveHead h, ImReceiveItem i " + 
					 " where h.status in('SIGNING','FINISH','CLOSE')" );
					 hql.append(" and h.brandCode ='").append(brandCode).append("'");
					 hql.append(" and i.imReceiveHead = h.headId ");
					 hql.append(" and i.itemCode ='").append(itemCode).append("'");
					 hql.append(" order by h.lastUpdateDate desc ");
					 //log.info(hql.toString());
					 Query query = session.createQuery(hql.toString());
					 return query.list();
				 }
			 });
			 if(result.size() == 0){
				 return null;
			 }else{
				 return (Date)result.get(0);
			 }
		 } catch (Exception e) {
			 log.info(e);
			 e.printStackTrace();
		 }
	 return null;
     }
	
	/**
	* * 進貨單簽核
	* 
	* @param headId, greater
	* @return
	*/
	public long getTotalLocalPurchaseAmountCount(long headId, double greater){
		log.debug("finding count of ImReceiveHead with headId: " + headId + ", and totalLocalPurchaseAmount greater than: " + greater);
		try {
			String queryString = "select count(*) from ImReceiveHead where headId = ? and totalLocalPurchaseAmount > ?";
			return ((List<Long>)getHibernateTemplate().find(queryString, new Object[]{headId, greater})).get(0);
		}
		catch (RuntimeException re){
			log.error("finding count of ImReceiveHead totalLocalPurchaseAmount greater than " + greater + " failed", re);
			throw re;
		}
	}
	public List<ImReceiveHead> findImReceiveHead(String brandCode, String orderTypeCode,
			String orderNo) {
		StringBuffer hql = new StringBuffer("from ImReceiveHead as model where model.brandCode = ?");
		hql.append(" and model.orderTypeCode = ?");
		hql.append(" and model.orderNo = ?");
		List<ImReceiveHead> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, orderTypeCode,orderNo });
		return result;
	}
	
	public List<ImReceiveHead> findImReceiveHeadBySheet(String brandCode, String budgetYear,
			String status) {
		StringBuffer hql = new StringBuffer("from ImReceiveHead as model where model.brandCode = ?");
		hql.append(" and model.budgetYear = ?");
		hql.append(" and model.status = ?");
		List<ImReceiveHead> result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, budgetYear,status });
		return result;
	}
	
	public ImReceiveHead findOneImReceive(String declNo){
		return (ImReceiveHead)findFirstByProperty("ImReceiveHead", "and declarationNo = ?", new Object[]{ declNo });
	}
}