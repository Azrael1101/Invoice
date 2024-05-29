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
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;

/**FiInvoiceLine entity.
 * @author MyEclipse Persistence Tools
 */

public class FiInvoiceLineDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(FiInvoiceLineDAO.class);

	public boolean isUsedByPo(Long poPurchaseOrderHeadId) {
		log.debug("FiInvoiceLineDAO isUsedByPo");
		try {
			StringBuffer biffer = new StringBuffer();
			biffer.append("from FiInvoiceHead as model, FiInvoiceLine as model2 \n")
				  .append("where model.headId = model2.fiInvoiceHead.headId \n")
				  .append("and model.status <> 'VOID' \n")
			      .append("and model2.poPurchaseOrderHeadId = ?");
			      Query queryObject = getSession().createQuery(biffer.toString());
			queryObject.setParameter(0, poPurchaseOrderHeadId);
			int size = queryObject.list().size();
			if(size > 0){
				return true;
			}else{
				return false;
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			log.error("get failed", re);
			throw re;
		}		
	}
	
	
    /**find page line
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<FiInvoiceLine> findPageLine(Long headId, int startPage, int pageSize) {
	log.info("FiInvoiceLineDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId = headId;
	List<FiInvoiceLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from FiInvoiceLine as model where 1=1 ");
		if (null != hId){
		    //hql.append(" and model.fiInvoiceHead.headId = :headId order by customSeq");
		    hql.append(" and model.fiInvoiceHead.headId = :headId order by indexNo");
		}
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
    
    
    /*find page line 最後一筆 index
     *@param headId
     * @return
     */
    public Long findPageLineMaxIndex(Long headId) {
	log.info("FiInvoiceLineDAO.findPageLineMaxIndex" + headId);		
	final Long hId = headId;
	List<FiInvoiceHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from FiInvoiceHead as model where 1=1 ");
		if (null != hId)
		    hql.append(" and model.headId = :headId ");
		Query query = session.createQuery(hql.toString());
		if (null != hId)
		    query.setLong("headId", hId);
		return query.list();
	    }
	});
	if( null != re && re.size() > 0 ){
	    List<FiInvoiceLine> fiInvoiceLines = re.get(0).getFiInvoiceLines();
	    if( null != fiInvoiceLines && fiInvoiceLines.size() > 0 )
		return fiInvoiceLines.get(fiInvoiceLines.size()-1).getIndexNo() ;
	}
	return 0L;
    }
    

    /**find line
     * @param headId
     * @param lineId
     * @return FiInvoiceLine
     */	
    public FiInvoiceLine findLine(Long headId,Long lineId) {
	log.info("FiInvoiceLineDAO.findLine headId=" + headId + "lineId=" + lineId );
	final Long hId = headId;
	final Long lId = lineId;
	List<FiInvoiceLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from FiInvoiceLine as model where 1=1 ");
		if (null != hId)
		    hql.append(" and model.fiInvoiceHead.headId = :headId and model.lineId = :lineId ");
		Query query = session.createQuery(hql.toString());
		if (null != hId)
		    query.setLong("headId", hId);
		if (null != lId)
		    query.setLong("lineId", lId);				
		return query.list();
	    }
	});
	if(( null != re ) && ( re.size() > 0 )){
	    return re.get(0);		
	}
	return null;
    }
    
    
    /*find page line 最後一筆 custom_seq
     *@param headId
     * @return
     */
    public Long findPageLineMaxCustomSeq(Long headId) {
	log.info("findPageLineMaxCustomSeq" + headId);		
	final Long hId = headId;
	Long maxCustomSeq ;
	List<FiInvoiceHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from FiInvoiceHead as model where 1=1 ");
		if (null != hId)
		    hql.append(" and model.headId = :headId ");
		Query query = session.createQuery(hql.toString());
		if (null != hId)
		    query.setLong("headId", hId);
		return query.list();
	    }
	});
	if( null != re && re.size() > 0 ){
	    List<FiInvoiceLine> fiInvoiceLines = re.get(0).getFiInvoiceLines();
	    if( null != fiInvoiceLines && fiInvoiceLines.size() > 0 ){
		maxCustomSeq = fiInvoiceLines.get(fiInvoiceLines.size()-1).getCustomSeq();
		for ( int i=0; i< fiInvoiceLines.size(); i++ ){
		    if ( maxCustomSeq < fiInvoiceLines.get(i).getCustomSeq())
			maxCustomSeq = fiInvoiceLines.get(i).getCustomSeq();
		}
		return maxCustomSeq;
	    }
	}
	return 0L;
    }
    
    
    /**find fiInvoice line by Object
     *@param object
     * @return FiInvoiceLine
     */
    public List<FiInvoiceLine> find(HashMap findObjs) {

	final HashMap fos = findObjs;

	List<FiInvoiceLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {

		StringBuffer hql = new StringBuffer("from FiInvoiceLine as model where 1=1 ");
		if ( null!=(Long)fos.get("headId") && 0!=(Long)fos.get("headId"))
		    hql.append(" and model.fiInvoiceHead.headId =").append( (Long) fos.get("headId"));
		if ( null!=(Long)fos.get("poPurchaseOrderHeadId") && 0!=(Long)fos.get("poPurchaseOrderHeadId"))
		    hql.append(" and model.poPurchaseOrderHeadId =").append( (Long) fos.get("poPurchaseOrderHeadId"));
		
		if ( null!=(Long)fos.get("poPurchaseOrderLineId") && 0!=(Long)fos.get("poPurchaseOrderLineId"))
		    hql.append(" and model.poPurchaseOrderLineId =").append( (Long) fos.get("poPurchaseOrderLineId"));
		
		if ( StringUtils.hasText((String)fos.get("orderNo") ))
		    hql.append(" and model.orderNo ='").append((String)fos.get("orderNo")).append("'");
		
		if ( StringUtils.hasText((String)fos.get("itemCode") ))
		    hql.append(" and model.itemCode ='").append((String)fos.get("itemCode")).append("'");
		if ( null!=(Long)fos.get("customSeq") && 0!=(Long)fos.get("customSeq"))
		    hql.append(" and model.customSeq ='").append((Long)fos.get("customSeq")).append("'");
		hql.append(" and nvl(model.isDeleteRecord,'0') <> '1' order by customSeq ");
		log.info(hql.toString());
		
		Query query = session.createQuery(hql.toString());
		query.setFirstResult(0);
		query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
		return query.list();
		}
	});
	if ( null!= re && re.size() > 0 ) {
	    return re;	//return re.get(0);
	} else {
	    return null;
	}
    }
    
    
    /**find line by declarationNo from iinvocieHead
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
     */
    //public List<FiInvoiceLine> findBydDclarationNo( String declarationNo ) {
    public List findBydDclarationNo( String declarationNo ) {
	log.info("findBydDclarationNo declarationNo=" + declarationNo );
	final String declarNO = declarationNo;
	//List<FiInvoiceLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	List re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		// 2010.01.29 for 船務需要以相同 fiInvoiceLine.customSeq 合併於同一筆報單
		StringBuffer hql = new StringBuffer("select min(L.lineId), H.headId, L.customSeq, H.invoiceDate, H.customsSeq, H.invoiceNo ");
		//StringBuffer hql = new StringBuffer("select min(L.lineId) from FiInvoiceLine as L, FiInvoiceHead as H ");
		hql.append(" from FiInvoiceLine as L,  FiInvoiceHead as H ");
		hql.append(" where L.fiInvoiceHead = H.headId " );
		hql.append(" and H.customsDeclarationNo = '" ).append( declarNO ).append("'");
		hql.append(" and H.status in ('FINISH','CLOSE')" );
		hql.append(" group by H.headId, L.customSeq, H.invoiceDate, H.customsSeq, H.invoiceNo ");
		hql.append(" order by H.customsSeq, H.invoiceNo, L.customSeq ");
			   //" order by H.customsSeq, H.invoiceDate, L.customSeq "); 	//2010.02.13 by ellen
		Query query = session.createQuery(hql.toString());

		log.info(hql.toString());
		return query.list();
	    }
	});
	return re;
    } 
    
}