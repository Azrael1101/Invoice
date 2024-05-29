package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;

public class SoSalesOrderPaymentDAO extends BaseDAO {

    private static final Log log = LogFactory.getLog(SoSalesOrderPaymentDAO.class);
    
    /**
     * find page line
     * 
     * @param headId
     * @param startPage
     * @param pageSize
     * @return List<SoSalesOrderPayment>
     */
    public List<SoSalesOrderPayment> findPageLine(Long headId, int startPage,
	    int pageSize) {

	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId = headId;
	List<SoSalesOrderPayment> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from SoSalesOrderPayment as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.soSalesOrderHead.headId = :headId order by indexNo");
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
	List<SoSalesOrderHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from SoSalesOrderHead as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.headId = :headId");
			Query query = session.createQuery(hql.toString());
			if (hId != null)
			    query.setLong("headId", hId);
			return query.list();
		    }
		});
	if (result != null && result.size() > 0) {
	    List<SoSalesOrderPayment> soSalesOrderPayments = result.get(0).getSoSalesOrderPayments();		    
	    if (soSalesOrderPayments != null && soSalesOrderPayments.size() > 0){
		lineMaxIndex = soSalesOrderPayments.get(soSalesOrderPayments.size() - 1).getIndexNo();	
	    }
	}
	return lineMaxIndex;
    }
    
    public SoSalesOrderPayment findPaymentByIdentification(Long headId, Long lineId){
	
	StringBuffer hql = new StringBuffer("from SoSalesOrderPayment as model where model.soSalesOrderHead.headId = ? and model.posPaymentId = ?");
	List<SoSalesOrderPayment> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public List<SoSalesOrderPayment> findPaymentTypeByHeadID(Long headID){
    	List<SoSalesOrderPayment>payments = new ArrayList<SoSalesOrderPayment>();
    	StringBuffer hql = new StringBuffer("from SoSalesOrderPayment as model where model.soSalesOrderHead.headId = ?");	
    	payments = getHibernateTemplate().find(hql.toString(), new Object[] { headID });
    	return payments;
    }
}