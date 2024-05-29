package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImLetterOfCreditAlter;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;

public class ImLetterOfCreditAlterDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImLetterOfCreditAlterDAO.class);

	/**
	 * 取得分頁
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<ImLetterOfCreditAlter> findPageLine(Long headId, int startPage,
	    int pageSize) {

	  	final int startRecordIndexStar = startPage * pageSize;
	  	final int pSize = pageSize;
	  	final Long hId = headId;
	  	
	  	List<ImLetterOfCreditAlter> result = getHibernateTemplate().executeFind(
	  		new HibernateCallback() {
	  		    public Object doInHibernate(Session session)
	  			    throws HibernateException, SQLException {
	  			StringBuffer hql = new StringBuffer("from ImLetterOfCreditAlter as model where 1=1 ");
	  			if (hId != null)
	  			    hql.append(" and model.imLetterOfCreditHead.headId = :headId order by indexNo");
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
	   * @param hId
	   * @return Long
	   */
	public Long findPageLineMaxIndex(final Long hId) {

		Long lineMaxIndex = new Long(0);
		
	  	List<ImLetterOfCreditAlter> result = getHibernateTemplate().executeFind(new HibernateCallback() {
	  	    public Object doInHibernate(Session session)
	  		    throws HibernateException, SQLException {
		  		StringBuffer hql = new StringBuffer("from ImLetterOfCreditAlter as model where 1=1 ");
		  		if (hId != null)
		  		    hql.append(" and model.imLetterOfCreditHead.headId = :headId order by indexNo");
		  		Query query = session.createQuery(hql.toString());
		  		if (hId != null)
		  		    query.setLong("headId", hId);
		  		return query.list();
	  	    }
	  	});
	  	if (result != null && result.size() > 0) {
	  		lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
	  	}
	  	return lineMaxIndex;
	}
	
}