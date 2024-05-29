package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;

public class CmMovementLineDAO extends BaseDAO<CmMovementLine>{ 
	public Long findPageLineMaxIndex(final Long hId) {
		Long lineMaxIndex = new Long(0);
		
	  	List<CmMovementLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
	  	    public Object doInHibernate(Session session)
	  		    throws HibernateException, SQLException {
		  		StringBuffer hql = new StringBuffer("from CmMovementLine as model where 1=1 ");
		  		if (hId != null)
		  		    hql.append(" and model.cmMovementHead.headId = :headId order by indexNo");
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
