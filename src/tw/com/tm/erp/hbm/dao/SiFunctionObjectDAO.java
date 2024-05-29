package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;

public class SiFunctionObjectDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(SiFunctionObjectDAO.class);
	
	public List<SiFunctionObject> findPageLine(String functionCode, int startPage,
	    int pageSize) {
log.info("Jason functionCode in findPageLine in Si_FunctionObjectDAO = " + functionCode);

  	final int startRecordIndexStar = startPage * pageSize;
  	final int pSize = pageSize;
  	final String hId = functionCode;
  	
  	List<SiFunctionObject> result = getHibernateTemplate().executeFind(
  		new HibernateCallback() {
  		    public Object doInHibernate(Session session)
  			    throws HibernateException, SQLException {
  			StringBuffer hql = new StringBuffer("from SiFunctionObject as model where 1=1 ");
  			if (hId != null)
  			    hql.append(" and model.siFunction.functionCode = :functionCode order by indexNo");
  			Query query = session.createQuery(hql.toString());
  			query.setFirstResult(startRecordIndexStar);
  			query.setMaxResults(pSize);
  			if (hId != null)
  			    query.setString("functionCode", hId);
  			return query.list();
  		    }
  		});
    	log.info("SiFunctionObject.functionCode:"+	functionCode);
    	log.info("SiFunctionObject.size:"+result.size());
    	return result;
	}
	
	/**
   * find page line 最後一筆 index
   * 
   * @param headId
   * @return Long
   */
  public Long findPageLineMaxIndex(String functionCode) {

  	Long lineMaxIndex = new Long(0);
  	final String hId = functionCode;
  	List<SiFunctionObject> result = getHibernateTemplate().executeFind(new HibernateCallback() {
  	    public Object doInHibernate(Session session)
  		    throws HibernateException, SQLException {
  		StringBuffer hql = new StringBuffer("from SiFunctionObject as model where 1=1 ");
  		if (hId != null)
  		    hql.append(" and model.siFunction.functionCode = :functionCode order by indexNo");
  		Query query = session.createQuery(hql.toString());
  		if (hId != null)
  		    query.setString("functionCode", hId);
  		return query.list();
  	    }
  	});
  	if (result != null && result.size() > 0) {
  		lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
  	}
  	return lineMaxIndex;
  }
  
  public SiFunctionObject findById(String functionCode, Long lineId){
  	StringBuffer hql = new StringBuffer("from SiFunctionObject as model where model.siFunction.functionCode= ? and model.lineId = ?");
  	List<SiFunctionObject> result = getHibernateTemplate().find(hql.toString(), new Object[]{functionCode, lineId});
  	return (result != null && result.size() > 0 ? result.get(0) : null);
  }
  
}
