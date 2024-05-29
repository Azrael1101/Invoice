package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombineMod;

import tw.com.tm.erp.hbm.bean.ImPromotionReCombine;


/**
 * A data access object (DAO) providing persistence and search support for
 * ImPromotionShop entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImPromotionShop
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionReCombineModDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ImPromotionReCombineModDAO.class);
	// property constants	
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImPromotionReCombineMod transientInstance) {		
		try {
			getHibernateTemplate().save(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(ImPromotionReCombineMod transientInstance) {
	        getHibernateTemplate().update(transientInstance);
	}

	public void delete(ImPromotionReCombineMod persistentInstance) {
		
		try {
			getHibernateTemplate().delete(persistentInstance);			
		} catch (RuntimeException re) {			
			throw re;
		}
	}

	
	
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImPromotionShop>
	 */
	 public List<ImPromotionReCombineMod> findPageLine(Long headId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImPromotionReCombineMod> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImPromotionReCombineMod as model where 1=1 ");
				if (hId != null)
				    hql.append(" and model.imPromotion.headId = :headId order by indexNo");
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
			    StringBuffer hql = new StringBuffer("select count(model.imPromotion) as rowCount from ImPromotionReCombineMod as model where 1=1");
			        if (hId != null)
			            hql.append(" and model.imPromotion.headId = :headId");
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
	
	public ImPromotionReCombineMod findReCombineByIdentification(Long headId, Long lineId){
		
		StringBuffer hql = new StringBuffer("from ImPromotionReCombineMod as model where model.imPromotion.headId = ? and model.lineId = ?");
		List<ImPromotionReCombineMod> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
}