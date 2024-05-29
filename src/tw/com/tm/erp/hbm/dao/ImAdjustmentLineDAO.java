package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;


public class ImAdjustmentLineDAO extends BaseDAO<ImAdjustmentLine> {
	private static final Log log = LogFactory.getLog(ImAdjustmentLineDAO.class);
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<ImAdjustmentLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("ImAdjustmentLineDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImAdjustmentLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImAdjustmentLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.imAdjustmentHead.headId = :headId order by indexNo");
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
	 * @param startPage
	 * @param pageSize
	 * @return
	 */	
	public ImAdjustmentLine findLine(Long headId,Long lineId) {
		log.info("ImAdjustmentHeadDAO.findLine headId=" + headId + "lineId=" + lineId );
		final Long hId = headId;
		final Long lId = lineId;
		List<ImAdjustmentLine> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImAdjustmentLine as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.imAdjustmentHead.headId = :headId and model.lineId = :lineId ");
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
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("ImAdjustmentLineDAO.findPageLineMaxIndex" + headId);		
		final Long hId = headId;
		List<ImAdjustmentHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImAdjustmentHead as model where 1=1 ");
				if (null != hId)
					hql.append(" and model.headId = :headId ");
				Query query = session.createQuery(hql.toString());
				if (null != hId)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		if( null != re && re.size() > 0 ){
			List<ImAdjustmentLine> imAdjustmentLines = re.get(0).getImAdjustmentLines();
			if( null != imAdjustmentLines && imAdjustmentLines.size() > 0 )
				return imAdjustmentLines.get(imAdjustmentLines.size()-1).getIndexNo() ;
			log.info("adj_size = " + imAdjustmentLines.size());
		}
		return 0L;
	}	

}
