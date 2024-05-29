package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveExpense;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;


public class ImReceiveExpenseDAO extends BaseDAO {
    	private static final Log log = LogFactory.getLog(ImReceiveExpenseDAO.class);
    	
    /** find page line
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
     */
     public List<ImReceiveExpense> findPageLine(Long headId, int startPage, int pageSize) {
	log.info("findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId  = headId;
	List<ImReceiveExpense> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from ImReceiveExpense as model where 1=1 ");
		if (null != hId)
		    hql.append(" and model.imReceiveHead.headId = :headId order by indexNo");
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

     
    /**find page line 最後一筆 index
     * @param headId
     * @return
     */
    public Long findPageLineMaxIndex(Long headId) {
	log.info("findPageLineMaxIndex" + headId);
	final Long hId = headId;
	List<ImReceiveHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("from ImReceiveHead as model where 1=1 ");
		if (null != hId)
		    hql.append(" and model.headId = :headId ");
		Query query = session.createQuery(hql.toString());
		if (null != hId)
		query.setLong("headId", hId);
		return query.list();
	    }
	});
	if (null != re && re.size() > 0) {
	    List<ImReceiveExpense> imReceiveExpenses = re.get(0).getImReceiveExpenses();
	    if (null != imReceiveExpenses && imReceiveExpenses.size() > 0)
		return imReceiveExpenses.get(imReceiveExpenses.size() - 1).getIndexNo();
	}
	return 0L;
    }
    
    
    /** find line
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
     */
    public ImReceiveExpense findLine(Long headId, Long lineId) {
	log.info("findLine headId=" + headId + "lineId=" + lineId);
	final Long hId = headId;
	final Long lId = lineId;
	List<ImReceiveExpense> re = getHibernateTemplate().executeFind(new HibernateCallback() {
    	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		StringBuffer hql = new StringBuffer("from ImReceiveExpense as model where 1=1 ");
    		if (null != hId)
    		    hql.append(" and model.imReceiveHead.headId = :headId and model.lineId = :lineId ");
    		Query query = session.createQuery(hql.toString());
    		if (null != hId)
    		    query.setLong("headId", hId);
    		if (null != lId)
    		    query.setLong("lineId", lId);
    		return query.list();
    	    }
	});
	if ((null != re) && (re.size() > 0)) {
	    return re.get(0);
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
	 public ImReceiveHead findByIdentification(String brandCode, String orderTypeCode, String orderNo) {
	     StringBuffer hql = new StringBuffer(
	         "from ImReceiveHead as model where model.brandCode = ?");
	     hql.append(" and model.orderTypeCode = ?");
           hql.append(" and model.orderNo = ?");
	     List<ImReceiveHead> result = getHibernateTemplate().find(
	         hql.toString(), new Object[] { brandCode, orderTypeCode, orderNo });
           return (result != null && result.size() > 0 ? result.get(0) : null);
	 }
}