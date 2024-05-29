package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;

public class ImInventoryCountsLineDAO extends BaseDAO {
    private static final Log log = LogFactory
	    .getLog(ImInventoryCountsLineDAO.class);
    
    public List<ImInventoryCountsLine> findByHeadId(Long headId){
	
	StringBuffer hql = new StringBuffer("from ImInventoryCountsLine as model where model.imInventoryCountsHead.headId = ? order by model.itemCode");
	List<ImInventoryCountsLine> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId});
	return result;
    }
    
    /**
     * find page line
     * 
     * @param headId
     * @param startPage
     * @param pageSize
     * @return List<ImInventoryCountsLine>
     */
    public List<ImInventoryCountsLine> findPageLine(Long headId, int startPage, int pageSize) {

        final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId = headId;
	List<ImInventoryCountsLine> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
		            throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from ImInventoryCountsLine as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.imInventoryCountsHead.headId = :headId order by indexNo");
			    Query query = session.createQuery(hql.toString());
			    query.setFirstResult(startRecordIndexStar);
			    query.setMaxResults(pSize);
			    if (hId != null)
			        query.setLong("headId", hId);
				return query.list();
			    }
			});
	return result;
    }
    
    /**
     * find page line 最後一筆 index
     * 
     * @param headId
     * @return Long
     */
    public Long findPageLineMaxIndex(Long headId) {

        Long lineMaxIndex = new Long(0);
	final Long hId = headId;
	List result = getHibernateTemplate().executeFind(
	        new HibernateCallback() {
		    public Object doInHibernate(Session session)
		            throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("select count(model.imInventoryCountsHead) as rowCount from ImInventoryCountsLine as model where 1=1");
			if (hId != null)
			    hql.append(" and model.imInventoryCountsHead.headId = :headId");
			Query query = session.createQuery(hql.toString());
			if (hId != null)
			    query.setLong("headId", hId);
			return query.list();
		    }
		});
	if (result != null && result.size() > 0) {
	    Long rowCount = (Long)result.get(0);
	    if(rowCount != null)
	        lineMaxIndex = rowCount.longValue();
	}
	return lineMaxIndex;
    }
    
    public ImInventoryCountsLine findItemByIdentification(Long headId, Long lineId){
	
	StringBuffer hql = new StringBuffer("from ImInventoryCountsLine as model where model.imInventoryCountsHead.headId = ? and model.lineId = ?");
	List<ImInventoryCountsLine> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
}