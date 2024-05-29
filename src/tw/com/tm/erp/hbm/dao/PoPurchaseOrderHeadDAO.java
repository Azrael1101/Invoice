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

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;

public class PoPurchaseOrderHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(PoPurchaseOrderHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	
	public List<PoPurchaseOrderHead> find(HashMap findObjs) {
		log.info("PoPurchaseOrderHeadDAO.find");

		final HashMap fos = findObjs;
		
		List<PoPurchaseOrderHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where 1=1 and substring(model.orderNo,1,3) <> 'TMP' ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");
				
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");

				if (StringUtils.hasText((String) fos.get("orderNo")))
					hql.append(" and model.orderNo = :orderNo ");
				
				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("superintendentCode")))
					hql.append(" and model.superintendentCode = :superintendentCode ");

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					hql.append(" and model.orderNo >= :startOrderNo ");

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					hql.append(" and model.orderNo <= :endOrderNo ");

				if (null != fos.get("purchaseOrderDateS"))
					hql.append(" and model.purchaseOrderDate >= :purchaseOrderDateS ");

				if (null != fos.get("purchaseOrderDateE"))
					hql.append(" and model.purchaseOrderDate <= :purchaseOrderDateE ");

				if (StringUtils.hasText((String) fos.get("closeOrder")))
					hql.append(" and model.closeOrder = :closeOrder ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));
				
				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));

				if (StringUtils.hasText((String) fos.get("orderNo")))
					query.setString("orderNo", (String) fos.get("orderNo"));
				
				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("superintendentCode")))
					query.setString("superintendentCode", (String) fos.get("superintendentCode"));

				if (StringUtils.hasText((String) fos.get("startOrderNo")))
					query.setString("startOrderNo", (String) fos.get("startOrderNo"));

				if (StringUtils.hasText((String) fos.get("endOrderNo")))
					query.setString("endOrderNo", (String) fos.get("endOrderNo"));

				if (null != fos.get("purchaseOrderDateS"))
					query.setDate("purchaseOrderDateS", (Date) fos.get("purchaseOrderDateS"));

				if (null != fos.get("purchaseOrderDateE"))
					query.setDate("purchaseOrderDateE", (Date) fos.get("purchaseOrderDateE"));

				if (StringUtils.hasText((String) fos.get("closeOrder")))
					query.setParameter("closeOrder", fos.get("closeOrder"));

				return query.list();
			}
		});

		return re;
	}
	
	/**
	 * 取得PoPurchaseOrderHead ,符合Invoice,品號
	 * 
	 * @param invoiceNo
	 * @param itemCode
	 * @return
	 */
	public List<PoPurchaseOrderHead> getInvoiceItemCode(HashMap findObjs) {
		log.info("PoPurchaseOrderHeadDAO.getVerificationPOList");		
		final String finvoiceNo = (String) findObjs.get("invoiceNo");
		final String fitemCode = (String) findObjs.get("itemCode");
		final String status = (String) findObjs.get("status");
		final String brandCode = (String) findObjs.get("brandCode");
		// final String closeOrder = (String) findObjs.get("closeOrder");
		// final String useQuanty = (String) findObjs.get("useQuanty");
		List<PoPurchaseOrderHead> result = null;
		if (null != finvoiceNo && null != fitemCode) {
			result = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {

					StringBuffer hql = new StringBuffer("select DISTINCT {poHead.*} from ");
					hql.append(" erp.fi_invoice_head fiHead join erp.fi_invoice_line fiLine ON fihead.head_id = fiLine.head_id ");
					hql.append(" join erp.po_purchase_order_head poHead ON filine.po_purchase_order_head_id = pohead.head_id ");
					hql.append(" join erp.po_purchase_order_line poLine on pohead.head_id = poLine.head_id ");
					hql.append(" where ");
					//hql.append(" nvl(poHead.close_order,'N') = 'N' ");
					hql.append(" SUBSTR(poHead.order_no,1,3) <> 'TMP' ");
					hql.append(" and poHead.brand_code = :brandCode ");
					hql.append(" and fihead.invoice_no = :invoiceNo ");
					hql.append(" and poLine.item_code = :itemCode ");					
					
					if (StringUtils.hasText(status))
						hql.append(" and poHead.status = :status ");

					Query query = session.createSQLQuery(hql.toString()).addEntity("poHead", PoPurchaseOrderHead.class);
					query.setString("invoiceNo", finvoiceNo);
					query.setString("itemCode", fitemCode);
					query.setString("brandCode", brandCode);					

					if (StringUtils.hasText(status))
						query.setString("status", status);
					
					log.debug("PoPurchaseOrderHeadDAO.getVerificationPOList hql=" + hql);					
					return query.list();
				}
			});
		}
		return result;
	}	

	/**
	 * 取得PoPurchaseOrderHead ,符合Invoice,品號,還有核銷數量
	 * 
	 * @param invoiceNo
	 * @param itemCode
	 * @return
	 */
	public List<PoPurchaseOrderHead> getVerificationPOList(HashMap findObjs) {
		log.info("PoPurchaseOrderHeadDAO.getVerificationPOList");		
		final String finvoiceNo = (String) findObjs.get("invoiceNo");
		final String fitemCode = (String) findObjs.get("itemCode");
		final String status = (String) findObjs.get("status");
		final String brandCode = (String) findObjs.get("brandCode");
		List<PoPurchaseOrderHead> result = null;
		if (null != finvoiceNo && null != fitemCode) {
			result = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					StringBuffer hql = new StringBuffer("select DISTINCT {poHead.*} from ");
					hql.append(" erp.fi_invoice_head fiHead join erp.fi_invoice_line fiLine ON fihead.head_id = fiLine.head_id ");
					hql.append(" join erp.po_purchase_order_head poHead ON filine.po_purchase_order_head_id = pohead.head_id ");
					hql.append(" join erp.po_purchase_order_line poLine on pohead.head_id = poLine.head_id ");
					hql.append(" where ");
					hql.append(" SUBSTR(poHead.order_no,1,3) <> 'TMP' ");
					hql.append(" and poHead.brand_code = :brandCode ");					
					//hql.append(" and poLine.quantity - poLine.receipted_quantity > 0 ");
					hql.append(" and fihead.invoice_no = :invoiceNo ");
					hql.append(" and poLine.item_code = :itemCode ");
					
					if (StringUtils.hasText(status))
						hql.append(" and poHead.status = :status ");

					Query query = session.createSQLQuery(hql.toString()).addEntity("poHead", PoPurchaseOrderHead.class);
					query.setString("invoiceNo", finvoiceNo);
					query.setString("itemCode", fitemCode);
					query.setString("brandCode", brandCode);

					if (StringUtils.hasText(status))
						query.setString("status", status);
					
					//log.info("PoPurchaseOrderHeadDAO.getVerificationPOList hql=" + hql);					
					return query.list();
				}
			});
		}
		return result;
	}

	/**
	 * close po 
	 * @throws FormException
	 * @throws Exception
	 */
	public void closeAllOrder() throws FormException, Exception {
		log.info("PoPurchaseOrderHeadDAO.closeAllOrder");		
		List<PoPurchaseOrderHead> poPurchaseOrderHeads = findAllNoCloseHead(OrderStatus.FINISH);
		if ((null != poPurchaseOrderHeads) && (poPurchaseOrderHeads.size() > 0)) {
			for(PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads)
				closeOrder( poPurchaseOrderHead , true );
		} 
	}

	/**
	 * PO單 結案
	 * 
	 * @param orderNo :
	 *            PO單號
	 * @param autoClose :
	 *            true 自動結案 , 所有的ITEM 都已經核銷掉才結案
	 * @throws FormException
	 * @throws Exception
	 */
	public void closeOrder(String orderNo, boolean autoClose) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadDAO.closeOrder");		
		List<PoPurchaseOrderHead> poPurchaseOrderHeads = findByProperty("PoPurchaseOrderHead", "orderNo", orderNo);
		if ((null != poPurchaseOrderHeads) && (poPurchaseOrderHeads.size() > 0)) {
			closeOrder(poPurchaseOrderHeads.get(0), autoClose);
		} else {
			throw new FormException("查無採購單單號 " + orderNo);
		}
	}

	/**
	 * PO單 結案
	 * 
	 * @param poPurchaseOrderHead :
	 *            PO單號
	 * @param autoClose :
	 *            true 自動結案 , 所有的ITEM 都已經核銷掉才結案
	 * @throws FormException
	 * @throws Exception
	 */
	public void closeOrder(PoPurchaseOrderHead poPurchaseOrderHead, boolean autoClose) throws FormException, Exception {
		log.info("PoPurchaseOrderHeadDAO.closeOrder");		
		boolean allFinish = true;
		List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
		for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
			if ((poPurchaseOrderLine.getActualPurchaseQuantity() - poPurchaseOrderLine.getReceiptedQuantity()) > 0) {
				allFinish = false;
				break;
			}
		}
		if (autoClose && allFinish) {
			poPurchaseOrderHead.setStatus(OrderStatus.CLOSE);
		} else if (!autoClose) {
			poPurchaseOrderHead.setStatus(OrderStatus.CLOSE);
		}
	}

	/**
	 * 用來判斷 PO單 是否有包含 ITEM CODE
	 * 
	 * @param poOrderNo
	 * @param poItemCode
	 * @return
	 */
	public boolean checkItemInPurchaseOrder(String poOrderNo, String poItemCode) {
		log.info("PoPurchaseOrderHeadDAO.checkItemInPurchaseOrder");		
		boolean re = false;

		final String status = "";
		final String orderNo = poOrderNo;
		final String itemCode = poItemCode;

		List<PoPurchaseOrderHead> tmp = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead master , PoPurchaseOrderLine detail where 1=1 and substring(master.orderNo,1,3) <> 'TMP' ");
				if (StringUtils.hasText(status))
					hql.append(" and master.status = :status ");
                
				if (StringUtils.hasText(orderNo))
					hql.append(" and master.orderNo = :orderNo ");

				if (StringUtils.hasText(itemCode))
					hql.append(" and detail.itemCode = :itemCode ");

				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText(status))
					query.setString("status", status);

				if (StringUtils.hasText(orderNo))
					query.setString("orderNo", orderNo);

				if (StringUtils.hasText(itemCode))
					query.setString("itemCode", itemCode);

				return query.list();
			}
		});

		if (tmp.size() > 0) {
			re = true;
		}

		return re;
	}

	/**
	 * 未核銷數量合計
	 * 
	 * @return
	 */
	// 未核銷數量(PO.STATUS=FINISH , PO單數量 ACTUAL_PURCHASE_QUANTITY - 已核銷數量
	// RECEIPTED_QUANTITY ) * PO單 台幣採購單價 localUnitCost
	// select sum((pol.quantity - pol.RECEIPTED_QUANTITY) *
	// pol.foreign_unit_cost) as total FROM po_purchase_order_head poH join
	// po_purchase_order_line poL on poH.head_id = pol.head_id where poh.status
	// = 'FINISH' and poh.brand_code = '' and pol.item_code = 'AAA'
	public Double getNotVerificationAmount(String brandCode, String itemCode) {
		log.info("PoPurchaseOrderHeadDAO.getNotVerificationAmount");		
		String status = "FINISH";
		Double re = new Double(0);
		StringBuffer hql = new StringBuffer();
		hql.append("select sum((poL.actualPurchaseQuantity - poL.receiptedQuantity) * poL.foreignUnitCost) as total ");
		hql.append("FROM PoPurchaseOrderHead poH , PoPurchaseOrderLine poL ");
		hql.append("where substring(poH.orderNo,1,3) <> 'TMP' and poH.status = '" + status + "' and poH.brandCode = ? and poL.itemCode = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, itemCode });
		if ((null != result) && (result.size() > 0) && (null != result.get(0))) {
			re = (Double) result.get(0);
		}
		return re;
	}

	/**
	 * find not close head
	 * @param status
	 * @param brandCode
	 * @param orderNo
	 * @return
	 */
	public List<PoPurchaseOrderHead> findAllNoCloseHead(String status, String brandCode, String orderNo,String orderTypeCode) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM PoPurchaseOrderHead model ");
		hql.append("where substring(model.orderNo,1,3) <> 'TMP' and model.status = ? and model.brandCode = ? and model.orderNo = ? and model.orderTypeCode = ? ");
		return getHibernateTemplate().find(hql.toString(), new Object[] { status, brandCode, orderNo, orderTypeCode });
	}
	
	
	/**
	 * find not close head
	 * @param status
	 * @return
	 */
	public List<PoPurchaseOrderHead> findAllNoCloseHead(String status) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM PoPurchaseOrderHead model ");
		hql.append("where substring(model.orderNo,1,3) <> 'TMP' and model.status = ? ");
		return getHibernateTemplate().find(hql.toString(), new Object[] { status });
	}
	
	/**
	 * find not close head
	 * @param status
	 * @return
	 */
	public List<PoPurchaseOrderHead> findAllHead() {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM PoPurchaseOrderHead model ");
		hql.append("where substring(model.orderNo,1,3) <> 'TMP' order by model.brandCode,model.orderNo");
		return getHibernateTemplate().find(hql.toString());
	}	
	
	/**
	 * remove tmp order 
	 */
	public void removeTmpOrder() {
		List<ImReceiveHead> removeOrders = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where substring(model.orderNo,1,3) = 'TMP' ");
				Query query = session.createQuery(hql.toString());
				return query.list();
			}
		});
		getHibernateTemplate().deleteAll(removeOrders);
	}
	
	
    /**find page line 最後一筆 index
     *@param headId
     * @return
     */
    public List findPoWithLineForInvoice( HashMap findObjs ) {
	log.info("findWithLineForInvoice" );		
	final HashMap fos = findObjs;
	
	List re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {

		try{ 
		    StringBuffer hql = new StringBuffer(
				"select a.headId, a.brandCode, a.orderTypeCode, " +
				" a.orderNo, a.supplierCode, a.supplierName, " +
				" b.lineId, b.itemCode, b.itemCName, ( b.actualPurchaseQuantity - b.receiptedQuantity ), " +
				" a.status, a.lastUpdateDate " +
				" from  PoPurchaseOrderHead a,  PoPurchaseOrderLine b " +
				" where substring(a.orderNo,1,3) <> 'TMP' "+
				" and b.poPurchaseOrderHead.headId = a.headId "+
				" and a.status in('FINISH','CLOSE') ");

		    if (StringUtils.hasText((String) fos.get("brandCode")))
			hql.append(" and a.brandCode ='").append((String) fos.get("brandCode")).append("'");
			
		    if (StringUtils.hasText((String) fos.get("orderTypeCode")))
			hql.append(" and a.orderTypeCode ='").append((String) fos.get("orderTypeCode")).append("'");

		    if (StringUtils.hasText((String) fos.get("orderNo")))
			hql.append(" and a.orderNo ='").append((String) fos.get("orderNo")).append("'");

		    if (StringUtils.hasText((String) fos.get("startOrderNo")))
			hql.append(" and a.orderNo >='").append((String) fos.get("startOrderNo")).append("'");

		    if (StringUtils.hasText((String) fos.get("endOrderNo")))
			hql.append(" and a.orderNo <='").append((String) fos.get("endOrderNo")).append("'");
			
		    if (StringUtils.hasText((String) fos.get("hideClosedPoLine")) && "Y".equals((String) fos.get("hideClosedPoLine")) )
			hql.append(" and nvl(b.receiptedQuantity,0) < nvl(b.actualPurchaseQuantity,b.quantity) ");

		    if (null != fos.get("purchaseOrderDateS"))
			hql.append(" and a.purchaseOrderDate >= :purchaseOrderDateS ");

		    if (null != fos.get("purchaseOrderDateE"))
			hql.append(" and a.purchaseOrderDate <= :purchaseOrderDateE ");

		    hql.append(" order by a.lastUpdateDate desc, b.itemCode ");
		    log.info(hql.toString());
		    
		    Query query = session.createQuery(hql.toString());
		    query.setFirstResult(0);
		    query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

		    if (null != fos.get("purchaseOrderDateS"))
			query.setDate("purchaseOrderDateS", (Date) fos.get("purchaseOrderDateS"));

		    if (null != fos.get("purchaseOrderDateE"))
			query.setDate("purchaseOrderDateE", (Date) fos.get("purchaseOrderDateE"));

		    return query.list();
		    
		}catch (HibernateException ex) {
		    log.error(ex.toString());
		}
		return null;
	    }
	});

	log.info( re.size());
	return re;
    }
    
    
    /**@param findObjs
     * @param startPage
     * @param pageSize
     * @param searchType  
     * 	      1) get max record count 
     *        2) select data records according to startPage and pageSize 
     *        3) select all records 
     * @return
     */
    public HashMap findPoHeadPageLine(HashMap findObjs, int startPage,  int pageSize, String searchType ) {
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final String type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)throws HibernateException, SQLException {
			            
		String brandCode     = (String) fos.get("brandCode");
		String supplierCode  = (String) fos.get("supplierCode");
		String orderTypeCode = (String) fos.get("orderTypeCode");
		String startOrderNo  = (String) fos.get("startOrderNo");
		String endOrderNo    = (String) fos.get("endOrderNo");	  
		String isHideClose   = (String) fos.get("isHideClose");
		Date startDate       = (Date)   fos.get("startDate");
		Date endDate         = (Date)   fos.get("endDate");

		StringBuffer hql = new StringBuffer("");
		if(QUARY_TYPE_RECORD_COUNT.equals(type)) {
		    hql.append("select count(model.headId) as rowCount from PoPurchaseOrderHead as model where 1=1 ");
		}else if(QUARY_TYPE_SELECT_ALL.equals(type)) {
		    hql.append("select model.headId from PoPurchaseOrderHead as model where 1=1 ");
		}else{
		    hql.append("from PoPurchaseOrderHead as model where 1=1 ");
		}
		hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");
		
		if (StringUtils.hasText(brandCode))
		    hql.append(" and model.brandCode = :brandCode ");
		
		if (StringUtils.hasText(supplierCode))
		    hql.append(" and model.supplierCode = :supplierCode ");

		if (StringUtils.hasText(orderTypeCode))
		    hql.append(" and model.orderTypeCode = :orderTypeCode ");
				    		    
		if (StringUtils.hasText(startOrderNo))
		    hql.append(" and model.orderNo >= :startOrderNo");

		if (StringUtils.hasText(endOrderNo))
		    hql.append(" and model.orderNo <= :endOrderNo");

		if (startDate != null)
		    hql.append(" and model.purchaseOrderDate >= :startDate");

		if (endDate != null)
		    hql.append(" and model.purchaseOrderDate <= :endDate");
		
		if ( StringUtils.hasText(isHideClose) && "Y".equals(isHideClose) ){	// 隱藏已結案
		    hql.append(" and status = 'FINISH' ");
		}else{
		    hql.append(" and status in ('FINISH', 'CLOSE')");
		}
				    
		hql.append(" order by model.lastUpdateDate desc");
		log.info(hql.toString());
			
		Query query = session.createQuery(hql.toString());
		if( QUARY_TYPE_SELECT_RANGE.equals(type)) {
		    query.setFirstResult(startRecordIndexStar);
		    query.setMaxResults(pSize);
		}
								
		if (StringUtils.hasText(brandCode))
		    query.setString("brandCode", brandCode);
		
		if (StringUtils.hasText(supplierCode))
		    query.setString("supplierCode", supplierCode);

		if (StringUtils.hasText(orderTypeCode))
		    query.setString("orderTypeCode", orderTypeCode);
				    		    
		if (StringUtils.hasText(startOrderNo))
		    query.setString("startOrderNo", startOrderNo);

		if (StringUtils.hasText(endOrderNo))
		    query.setString("endOrderNo", endOrderNo);

		if (startDate != null)
		    query.setDate("startDate", startDate);

		if (endDate != null)
		    query.setDate("endDate", endDate);		
								
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
    
    
    /**@param findObjs
     * @param startPage
     * @param pageSize
     * @param searchType  
     * 	      1) get max record count 
     *        2) select data records according to startPage and pageSize 
     *        3) select all records 
     * @return
     */
    public HashMap findPoLinePageLine(HashMap findObjs, int startPage,  int pageSize, String searchType ) {
	log.info("findPoLinePageLine");
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize   = pageSize;
	final String type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)throws HibernateException, SQLException {
			            
		String brandCode     		= (String) fos.get("brandCode");
		String currencyCode  		= (String) fos.get("currencyCode");
		String supplierCode  		= (String) fos.get("supplierCode");
		String defaultWarehouseCode	= (String) fos.get("defaultWarehouseCode");
		String orderTypeCode 		= (String) fos.get("orderTypeCode");
		String startOrderNo  		= (String) fos.get("startOrderNo");
		String endOrderNo    		= (String) fos.get("endOrderNo");	  
		String isHideClose   		= (String) fos.get("isHideClose");
		String sortType      		= (String) fos.get("sortType");
		Date startDate       		= (Date)   fos.get("startDate");
		Date endDate         		= (Date)   fos.get("endDate");
		
		StringBuffer hql = new StringBuffer("");
		if(QUARY_TYPE_RECORD_COUNT.equals(type)) {
		    hql.append("select count(b.lineId) as rowCount " +
			    	" from  PoPurchaseOrderHead a,  PoPurchaseOrderLine b, ImItem c " +
				" where substring(a.orderNo,1,3) <> 'TMP' "+
				" and (b.actualPurchaseQuantity - b.receiptedQuantity) > 0 "+
				" and b.poPurchaseOrderHead.headId = a.headId"+
				" and c.brandCode = a.brandCode "+
				" and c.itemCode = b.itemCode ");
		    
		}else if(QUARY_TYPE_SELECT_ALL.equals(type)) {
		    hql.append("select b.lineId "+
			    	" from  PoPurchaseOrderHead a,  PoPurchaseOrderLine b, ImItem c  " +
				" where substring(a.orderNo,1,3) <> 'TMP' "+
				" and (b.actualPurchaseQuantity - b.receiptedQuantity) > 0 "+
				" and b.poPurchaseOrderHead.headId = a.headId"+
				" and c.brandCode = a.brandCode "+
				" and c.itemCode = b.itemCode ");
		    		//" and a.status in('FINISH','CLOSE') " );		    
		}else{
		    hql.append("select a.headId, a.brandCode, a.orderTypeCode " +
				", a.orderNo, a.supplierCode, a.supplierName " +
				", b.lineId, b.itemCode, b.itemCName, ( b.actualPurchaseQuantity - b.receiptedQuantity ) " +
				", a.status, a.lastUpdateDate, a.brandCode" +
				", c.supplierItemCode "+
				", ((b.actualPurchaseQuantity - b.receiptedQuantity) * b.foreignUnitCost) " +
				" from  PoPurchaseOrderHead a,  PoPurchaseOrderLine b, ImItem c " +
				" where substring(a.orderNo,1,3) <> 'TMP' "+
				" and (b.actualPurchaseQuantity - b.receiptedQuantity) > 0 "+
				" and b.poPurchaseOrderHead.headId = a.headId"+
				" and c.brandCode = a.brandCode "+
				" and c.itemCode = b.itemCode ");
				//" and a.status in('FINISH','CLOSE') " );
		}
				    
		if (StringUtils.hasText(brandCode))
		    hql.append(" and a.brandCode = :brandCode ");
		
		if (StringUtils.hasText(currencyCode))
		    hql.append(" and a.currencyCode = :currencyCode ");
		
		if (StringUtils.hasText(supplierCode))
		    hql.append(" and a.supplierCode = :supplierCode ");

		if (StringUtils.hasText(defaultWarehouseCode))
		    hql.append(" and a.defaultWarehouseCode = :defaultWarehouseCode ");
		
		if (StringUtils.hasText(orderTypeCode))
		    hql.append(" and a.orderTypeCode = :orderTypeCode ");
				
		hql.append(" and SUBSTR(a.orderNo,1,3) <> 'TMP' ");
				    		    
		if (StringUtils.hasText(startOrderNo))
		    hql.append(" and a.orderNo >= :startOrderNo");

		if (StringUtils.hasText(endOrderNo))
		    hql.append(" and a.orderNo <= :endOrderNo");

		if (startDate != null)
		    hql.append(" and a.purchaseOrderDate >= :startDate");

		if (endDate != null)
		    hql.append(" and a.purchaseOrderDate <= :endDate");
		
		if ( StringUtils.hasText(isHideClose) && "Y".equals(isHideClose) ){	// 隱藏已結案
		    hql.append(" and a.status = 'FINISH' ");
		}else{
		    hql.append(" and a.status in ('FINISH', 'CLOSE')");
		}

		if(!StringUtils.hasText(sortType)) {
		    hql.append(" order by a.lastUpdateDate desc, b.indexNo");
		}else if("ItemCode".equals(sortType)){
		    hql.append(" order by b.itemCode ");
		    
		}else if("SupplierCode".equals(sortType)){
		    hql.append(" order by c.supplierItemCode ");
		    
		}else if("Quantity".equals(sortType)){
		    hql.append(" order by (b.actualPurchaseQuantity - b.receiptedQuantity) ");
		    
		}else if("Amount".equals(sortType)){
		    hql.append(" order by ((b.actualPurchaseQuantity - b.receiptedQuantity) * b.foreignUnitCost) ");
		}

		//log.info("XXXXXXXXXXXXXXXXXXXX:"+hql.toString());
		Query query = session.createQuery(hql.toString());
		if( QUARY_TYPE_SELECT_RANGE.equals(type)) {
		    query.setFirstResult(startRecordIndexStar);
		    query.setMaxResults(pSize);
		}
								
		if (StringUtils.hasText(brandCode))
		    query.setString("brandCode", brandCode);
		
		if (StringUtils.hasText(currencyCode))
		    query.setString("currencyCode", currencyCode);

		if (StringUtils.hasText(supplierCode))
		    query.setString("supplierCode", supplierCode);
		
		if (StringUtils.hasText(defaultWarehouseCode))
		    query.setString("defaultWarehouseCode", defaultWarehouseCode);
		
		if (StringUtils.hasText(orderTypeCode))
		    query.setString("orderTypeCode", orderTypeCode);
				    		    
		if (StringUtils.hasText(startOrderNo))
		    query.setString("startOrderNo", startOrderNo);

		if (StringUtils.hasText(endOrderNo))
		    query.setString("endOrderNo", endOrderNo);

		if (startDate != null)
		    query.setDate("startDate", startDate);

		if (endDate != null)
		    query.setDate("endDate", endDate);
		
		log.info(hql.toString());
		return query.list();
	    }
	});
	HashMap returnResult = new HashMap();		
	returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result: null);
	log.info("findPoLinePageLine count:"+result.size());
	if(result.size() == 0){
	    returnResult.put("recordCount", 0L);
	}else{
	    returnResult.put("recordCount", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type)? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
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

    			String brandCode           = (String) fos.get("brandCode");
    			String orderTypeCode       = (String) fos.get("orderTypeCode");
    			String startOrderNo        = (String) fos.get("startOrderNo");
    			String endOrderNo          = (String) fos.get("endOrderNo");
    			String status              = (String) fos.get("status");
    			String superintendentCode  = (String) fos.get("superintendentCode");
    			String supplierCode        = (String) fos.get("supplierCode");
    			String categoryType        = (String) fos.get("categoryType");
    			Date startDate             = (Date) fos.get("startDate");
    			Date endDate               = (Date) fos.get("endDate");
    			String startSourceOrderNo  = (String) fos.get("startSourceOrderNo");
    			String endSourceOrderNo    = (String) fos.get("endSourceOrderNo");
    			String startItemCode       = (String) fos.get("startItemCode");
    			String endItemCode         = (String) fos.get("endItemCode");		
    			String startQuotationCode  = (String) fos.get("startQuotationCode");
    			String endQuotationCode    = (String) fos.get("endQuotationCode");	
    			String startPoOrderNo      = (String) fos.get("startPoOrderNo");
    			String endPoOrderNo        = (String) fos.get("endPoOrderNo");		
    			
    			StringBuffer hql = new StringBuffer("");
    			if(QUARY_TYPE_RECORD_COUNT.equals(type))
    				hql.append(" select count(model.headId) as rowCount from PoPurchaseOrderHead as model where 1=1 ");
    			else if(QUARY_TYPE_SELECT_ALL.equals(type))
    				hql.append(" select model.headId from PoPurchaseOrderHead as model where 1=1 ");
    			else
    				hql.append(" from PoPurchaseOrderHead as model where 1=1 ");
    			
    			if (StringUtils.hasText(brandCode))
    				hql.append(" and model.brandCode = :brandCode ");

    			if (StringUtils.hasText(orderTypeCode))
    				hql.append(" and model.orderTypeCode = :orderTypeCode ");

    			hql.append(" and SUBSTR(model.orderNo,1,3) <> 'TMP' ");

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

    			if (StringUtils.hasText(superintendentCode))
    				hql.append(" and model.superintendentCode = :superintendentCode");

    			if (StringUtils.hasText(supplierCode))
    				hql.append(" and model.supplierCode = :supplierCode");

    			if (StringUtils.hasText(categoryType))
    				hql.append(" and model.categoryType = :categoryType");

    			if (startDate != null)
    				hql.append(" and model.purchaseOrderDate >= :startDate");

    			if (endDate != null)
    				hql.append(" and model.purchaseOrderDate <= :endDate");

    			if (StringUtils.hasText(startItemCode))
    				hql.append(" and model.headId IN (SELECT item.poPurchaseOrderHead FROM PoPurchaseOrderLine as item WHERE item.itemCode >= :startItemCode)");

    			if (StringUtils.hasText(endItemCode))
    				hql.append(" and model.headId IN (SELECT item.poPurchaseOrderHead FROM PoPurchaseOrderLine as item WHERE item.itemCode <= :endItemCode)");		

    			if (StringUtils.hasText(startQuotationCode))
    				hql.append(" and model.quotationCode >= :startQuotationCode");

    			if (StringUtils.hasText(endQuotationCode))
    				hql.append(" and model.quotationCode <= :endQuotationCode");

    			if (StringUtils.hasText(startPoOrderNo))
    				hql.append(" and model.poOrderNo >= :startPoOrderNo");

    			if (StringUtils.hasText(endPoOrderNo))
    				hql.append(" and model.poOrderNo <= :endPoOrderNo");		

    			hql.append(" order by model.orderNo desc");
    			log.info(hql.toString());

    			Query query = session.createQuery(hql.toString());
    			if( QUARY_TYPE_SELECT_RANGE.equals(type) || QUARY_TYPE_SELECT_ALL.equals(type)) {
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

    			if (StringUtils.hasText(superintendentCode))
    				query.setString("superintendentCode", superintendentCode);

    			if (StringUtils.hasText(supplierCode))
    				query.setString("supplierCode", supplierCode);

    			if (StringUtils.hasText(categoryType))
    				query.setString("categoryType", categoryType);

    			if (startDate != null)
    				query.setDate("startDate", startDate);

    			if (endDate != null)
    				query.setDate("endDate", endDate);		

    			if (StringUtils.hasText(startSourceOrderNo))
    				query.setString("startSourceOrderNo", startSourceOrderNo);

    			if (StringUtils.hasText(endSourceOrderNo))
    				query.setString("endSourceOrderNo", endSourceOrderNo);

    			if (StringUtils.hasText(startItemCode))
    				query.setString("startItemCode", startItemCode);

    			if (StringUtils.hasText(endItemCode))
    				query.setString("endItemCode", endItemCode);		

    			if (StringUtils.hasText(startQuotationCode))
    				query.setString("startQuotationCode", startQuotationCode);

    			if (StringUtils.hasText(endQuotationCode))
    				query.setString("endQuotationCode", endQuotationCode);

    			if (StringUtils.hasText(startPoOrderNo))
    				query.setString("startPoOrderNo", startPoOrderNo);

    			if (StringUtils.hasText(endPoOrderNo))
    				query.setString("endPoOrderNo", endPoOrderNo);

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
    
    public PoPurchaseOrderHead findPoInvoice(String brandCode, String orderTypeCode ,String orderNo 
			) {
    	log.info("DAO1111"+brandCode+orderNo+orderTypeCode);
		StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where model.brandCode = ?");
		hql.append(" and model.orderTypeCode = ?");
		hql.append(" and model.orderNo = ?");
		List<PoPurchaseOrderHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, orderTypeCode , orderNo});
		log.info("DAO2222"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
    public List<PoPurchaseOrderHead> findPoInvoiceBySheet(String brandCode, String orderTypeCode ,String orderNo 
	) {
    	log.info("listDAO1111"+brandCode+orderNo+orderTypeCode);
    	StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where model.brandCode = ?");
    	hql.append(" and model.orderTypeCode = ?");
    	hql.append(" and model.orderNo = ?");
    	List<PoPurchaseOrderHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, orderTypeCode , orderNo});
    	log.info("listDAO2222"+result.size());
    	return result;
    }
    public List<PoPurchaseOrderHead> findPoByYear(String brandCode, String budgetYear ,String status
	) {
    	log.info("PoByYear"+brandCode+budgetYear);
    	StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where model.brandCode = ?");
    	hql.append(" and model.budgetYear = ?");
    	hql.append(" and model.status = ?");
    	List<PoPurchaseOrderHead> result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, budgetYear ,status});
    	log.info("PoByYear"+result.size());
    	return result;
    }
}
