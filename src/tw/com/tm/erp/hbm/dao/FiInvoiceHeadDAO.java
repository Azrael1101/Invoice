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
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

/**
 * FiInvoiceHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiInvoiceHeadDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(ImReceiveHeadDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL   = "selectAll";
    public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
    public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";

	public List<FiInvoiceHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<FiInvoiceHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from FiInvoiceHead as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("supplierCode")))
					hql.append(" and model.supplierCode = :supplierCode ");

				/*
				 * if( StringUtils.hasText((String)fos.get("invoiceNo")) )
				 * hql.append(" and model.invoiceNo >= :invoiceNo ");
				 */

				if (StringUtils.hasText((String) fos.get("invoiceNo")))
					hql.append(" and model.invoiceNo = :invoiceNo ");

				if (null != fos.get("invoiceDateS"))
					hql.append(" and model.invoiceDate >= :invoiceDateS ");

				if (null != fos.get("invoiceDateE"))
					hql.append(" and model.invoiceDate <= :invoiceDateE ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("supplierCode")))
					query.setString("supplierCode", (String) fos.get("supplierCode"));

				if (StringUtils.hasText((String) fos.get("invoiceNo")))
					query.setString("invoiceNo", (String) fos.get("invoiceNo"));

				if (null != fos.get("invoiceDateS"))
					query.setDate("invoiceDateS", (Date) fos.get("invoiceDateS"));

				if (null != fos.get("invoiceDateE"))
					query.setDate("invoiceDateE", (Date) fos.get("invoiceDateE"));

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));

				return query.list();
			}
		});

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
    public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize, String searchType) {
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final String type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session)throws HibernateException, SQLException {
			            
		String brandCode      = (String) fos.get("brandCode");
		String orderTypeCode  = (String) fos.get("orderTypeCode");
		String startInvoiceNo = (String) fos.get("startInvoiceNo");
		String endInvoiceNo   = (String) fos.get("endInvoiceNo");
		String supplierCode   = (String) fos.get("supplierCode");
		String status         = (String) fos.get("status");	    
		Date startDate        = (Date) fos.get("startDate");
		Date endDate          = (Date) fos.get("endDate");

		StringBuffer hql = new StringBuffer("");
		if(QUARY_TYPE_RECORD_COUNT.equals(type)) {
		    hql.append("select count(model.headId) as rowCount from FiInvoiceHead as model where 1=1 ");
		}else if(QUARY_TYPE_SELECT_ALL.equals(type)) {
		    hql.append("select model.headId from FiInvoiceHead as model where 1=1 ");
		}else{
		    hql.append("from FiInvoiceHead as model where 1=1 ");
		}
				    
		if (StringUtils.hasText(brandCode))
		    hql.append(" and model.brandCode = :brandCode ");

		if (StringUtils.hasText(orderTypeCode))
		    hql.append(" and model.orderTypeCode = :orderTypeCode ");
				
		hql.append(" and model.invoiceNo is not null ");
				    		    
		if (StringUtils.hasText(startInvoiceNo))
		    hql.append(" and model.invoiceNo >= :startInvoiceNo");

		if (StringUtils.hasText(endInvoiceNo))
		    hql.append(" and model.invoiceNo <= :endInvoiceNo");

		if (StringUtils.hasText(supplierCode))
		    hql.append(" and model.supplierCode = :supplierCode");
		
		if (StringUtils.hasText(status))
		    hql.append(" and model.status = :status");

		if (startDate != null)
		    hql.append(" and model.invoiceDate >= :startDate");

		if (endDate != null)
		    hql.append(" and model.oinvoiceDate <= :endDate");
				    
		//hql.append(" order by model.invoiceNo desc");
		hql.append(" order by model.lastUpdateDate desc");
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
				    		    
		if (StringUtils.hasText(startInvoiceNo))
		    query.setString("startInvoiceNo", startInvoiceNo);

		if (StringUtils.hasText(endInvoiceNo))
		    query.setString("endInvoiceNo", endInvoiceNo);

		if (StringUtils.hasText(supplierCode))
		    query.setString("supplierCode", supplierCode);
		
		if (StringUtils.hasText(status))
		    query.setString("status", status);

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
    
    public FiInvoiceHead findPoInvoice(String brandCode, String invoiceNo
    ) {

    	StringBuffer hql = new StringBuffer("from FiInvoiceHead as model where model.brandCode = ?");
    	hql.append(" and model.invoiceNo = ?");
    	List<FiInvoiceHead> result = getHibernateTemplate().find(hql.toString(),
    			new Object[] { brandCode, invoiceNo });

    	return (result != null && result.size() > 0 ? result.get(0) : null);
}
    
    
    
}