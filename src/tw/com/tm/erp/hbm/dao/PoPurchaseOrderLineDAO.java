package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;

public class PoPurchaseOrderLineDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(PoPurchaseOrderLineDAO.class);
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<PoPurchaseOrderLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("PoPurchaseOrderHeadDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.poPurchaseOrderHead.headId = :headId order by indexNo");
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
	 * find line
	 * 
	 * @param headId
	 * @param lineId
	 * @return
	 */	
	public PoPurchaseOrderLine findLine(Long headId, Long lineId) {
		log.info("PoPurchaseOrderHeadDAO.findLine headId=" + headId + "lineId=" + lineId );
		final Long hId = headId;
		final Long lId = lineId;
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.poPurchaseOrderHead.headId = :headId and model.lineId = :lineId ");
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
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */	
	public List<PoPurchaseOrderLine> findByItemCode(final Long headId, final String itemCode) {
		log.info("PoPurchaseOrderHeadDAO.findLine headId=" + headId + "itemCode=" + itemCode );
		
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if (null != headId)
					hql.append(" and model.poPurchaseOrderHead.headId =").append(headId);
				if (null != itemCode)
					hql.append(" and model.itemCode ='").append(itemCode).append("'");
				//System.out.println(hql.toString());
				Query query = session.createQuery(hql.toString());
				/*
				if (null != headId)
					query.setLong("headId", headId);
				if (null != itemCode)
					query.setString("itemCode", itemCode);	
				*/			
				return query.list();
			}
		});
		if(( null != re ) && ( re.size() > 0 )){
		    return re;
		    //return re.get(0);
		}
		return null;
	}
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */	
	public List<PoPurchaseOrderLine> findByItemCode(final Long headId, final String itemCode, final String lineId) {
		log.info("PoPurchaseOrderHeadDAO.findLine headId=" + headId + "itemCode=" + itemCode );
		
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if (null != headId)
					hql.append(" and model.poPurchaseOrderHead.headId =").append(headId);
				if (null != itemCode)
					hql.append(" and model.itemCode ='").append(itemCode).append("'");
				if (null != lineId)
					hql.append(" and model.lineId ='").append(lineId).append("'");//採購欄位回寫增poLineId-Jerome
				
				Query query = session.createQuery(hql.toString());
							
				return query.list();
			}
		});
		if(( null != re ) && ( re.size() > 0 )){
		    return re;
		}
		return null;
	}
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param itemCode
	 * @param isDelete
	 * @return
	 */	
	public List<PoPurchaseOrderLine> findPoPurchaseOrderLines(final Long headId, final String itemCode, final String isDelete) {
		log.info("PoPurchaseOrderHeadDAO.findLine headId=" + headId + "itemCode=" + itemCode );
		
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if(null != headId) hql.append(" and model.poPurchaseOrderHead.headId = :headId");
				if(null != itemCode) hql.append(" and model.itemCode = :itemCode");
				if(null != isDelete) hql.append(" and model.isDeleteRecord = :isDeleteRecord");
				
				Query query = session.createQuery(hql.toString());
				if(null != headId) query.setLong("headId", headId);
				if(null != itemCode) query.setString("itemCode", itemCode);
				if(null != isDelete) query.setString("isDeleteRecord", isDelete);
							
				return query.list();
			}
		});
		if((null != re) && (re.size() > 0)) return re;
		
		return null;
	}
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */	
	/*
	public PoPurchaseOrderLine updateLine(Long headId,PoPurchaseOrderLine line) {
		log.info("PoPurchaseOrderHeadDAO.updateLine headId=" + headId + "line=" + line );
		Long hId = headId;
		PoPurchaseOrderHead poPurchaseOrderHead = new PoPurchaseOrderHead();
		poPurchaseOrderHead.setHeadId(headId);
		line.set
		this.save(line);
		
		final Long lId = lineId;
		List<PoPurchaseOrderLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.poPurchaseOrderHead.headId = :headId and model.lineId = :lineId ");
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
	*/
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("PoPurchaseOrderLineDAO.findPageLineMaxIndex" + headId);		
		final Long hId = headId;
		List<PoPurchaseOrderHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from PoPurchaseOrderHead as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.headId = :headId ");
				Query query = session.createQuery(hql.toString());
				if (null != hId)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		if( null != re && re.size() > 0 ){
			List<PoPurchaseOrderLine> poPurchaseOrderLines = re.get(0).getPoPurchaseOrderLines();
			if( null != poPurchaseOrderLines && poPurchaseOrderLines.size() > 0 )
				return poPurchaseOrderLines.get(poPurchaseOrderLines.size()-1).getIndexNo() ;
		}
		return 0L;
	}
	
    /**
     * @param headId
     * @return
     */
    public Map findTotalAmount(long  headId) {
		log.info("PoPurchaseOrderLineDAO.findTotalAmount" + headId );
		Map resultMap = new HashMap();;
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" sum(nvl(A.quantity,0) as totalProductCounts,   ");
		hql.append(" round( sum(nvl(A.quantity,0) * nvl(A.localUnitCost,0)),   2) as taxAmount, ");
		hql.append(" round( sum(nvl(A.quantity,0) * nvl(A.localUnitCost,0)),   2) as localCost,    ");
		hql.append(" round( sum(nvl(A.quantity,0) * nvl(A.unitPrice,0)),       2) as localAmount,  ");
		hql.append(" round( sum(nvl(A.quantity,0) * nvl(A.foreignUnitCost,0)), 4) as foreignCost,  ");
		hql.append(" round( sum(nvl(A.quantity,0) * nvl(A.foreignUnitCost,0)), 4) as foreignAmount ");
		hql.append("FROM PoPurchaseOrderLine A");
		hql.append("WHERE A.poPurchaseOrderHead.headId=" ).append(headId);
		//System.out.println( hql.toString() );
		List result = getHibernateTemplate().find(hql.toString(), null);	    
		if(result != null){
		    Object[] item = (Object[])result.get(0);
		    resultMap.put("totalProductCounts", (Double)item[0]);
		    resultMap.put("taxAmount",          (Double)item[1]);
		    resultMap.put("localCost",          (Double)item[2]);
		    resultMap.put("localAmount",        (Double)item[3]);
		    resultMap.put("foreignCost",        (Double)item[4]);
		    resultMap.put("foreignAmoount",     (Double)item[5]);
		    //resultMap.put("adjustActualAmount",  (Double)item[5]);
		}
		return resultMap;
    }
	
}
