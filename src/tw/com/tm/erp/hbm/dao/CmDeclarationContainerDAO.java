package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationContainer;


/**
 * CmDeclarationContainer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationContainerDAO extends BaseDAO {


    private static final Log log = LogFactory.getLog(CmDeclarationContainerDAO.class);

    /**
     * find page line
     * 
     * @param headId
     * @param startPage
     * @param pageSize
     * @return List<CmDeclarationItem>
     */
    public List<CmDeclarationContainer> findPageLine(Long headId, int startPage,
	    int pageSize) {

	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final Long hId = headId;
	List<CmDeclarationContainer> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from CmDeclarationContainer as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.cmDeclarationHead.headId = :headId order by indexNo");
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
	List<CmDeclarationHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("from CmDeclarationHead as model where 1=1 ");
			if (hId != null)
			    hql.append(" and model.headId = :headId");
			Query query = session.createQuery(hql.toString());
			if (hId != null)
			    query.setLong("headId", hId);
			return query.list();
		    }
		});
	if (result != null && result.size() > 0) {
	    List<CmDeclarationContainer> cmDeclarationContainers = result.get(0).getCmDeclarationContainers();		    
	    if (cmDeclarationContainers != null && cmDeclarationContainers.size() > 0){
		lineMaxIndex = cmDeclarationContainers.get(cmDeclarationContainers.size() - 1).getIndexNo();	
	    }
	}
	return lineMaxIndex;
    }
    
    public CmDeclarationContainer findItemByIdentification(Long headId, Long itemId){
	
	StringBuffer hql = new StringBuffer("from CmDeclarationContainer as model where model.cmDeclarationHead.headId = ? and model.itemId = ?");
	List<CmDeclarationContainer> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, itemId});
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    public List<CmDeclarationContainer> findByHeadId(Long headId){
	
	StringBuffer hql = new StringBuffer("from CmDeclarationContainer as model where model.cmDeclarationHead.headId = ? order by model.itemId");
	List<CmDeclarationContainer> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId});
	return result;
    }


	

}