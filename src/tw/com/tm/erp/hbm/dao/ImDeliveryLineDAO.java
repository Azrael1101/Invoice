package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class ImDeliveryLineDAO extends BaseDAO {
    
    private static final Log log = LogFactory.getLog(ImDeliveryLineDAO.class);
    
    /**
     * find page line
     * 
     * @param headId
     * @param startPage
     * @param pageSize
     * @return List<ImDeliveryLine>
     */
    public List<ImDeliveryLine> findPageLine(Long headId, int startPage,
	    int pageSize) {

	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId = headId;
	List<ImDeliveryLine> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from ImDeliveryLine as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.imDeliveryHead.headId = :headId order by indexNo");
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
	List<ImDeliveryHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from ImDeliveryHead as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.headId = :headId");
			Query query = session.createQuery(hql.toString());
			if (hId != null)
			    query.setLong("headId", hId);
			return query.list();
		    }
		});
	if (result != null && result.size() > 0) {
	    List<ImDeliveryLine> imDeliveryLines = result.get(0).getImDeliveryLines();		    
	    if (imDeliveryLines != null && imDeliveryLines.size() > 0){
		lineMaxIndex = imDeliveryLines.get(imDeliveryLines.size() - 1).getIndexNo();	
	    }
	}
	return lineMaxIndex;
    }
    
    public ImDeliveryLine findItemByIdentification(Long headId, Long lineId){
	
	StringBuffer hql = new StringBuffer("from ImDeliveryLine as model where model.imDeliveryHead.headId = ? and model.lineId = ?");
	List<ImDeliveryLine> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public List<ImDeliveryLine> findByHeadId(Long headId){
	
        StringBuffer hql = new StringBuffer("from ImDeliveryLine as model where model.imDeliveryHead.headId = ? order by model.itemCode");
	List<ImDeliveryLine> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId});
	return result;
    }
}