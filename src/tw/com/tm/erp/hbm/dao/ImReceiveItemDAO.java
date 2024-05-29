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
import tw.com.tm.erp.hbm.bean.ImReceiveItem;

public class ImReceiveItemDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImReceiveItemDAO.class);
	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<ImReceiveItem> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("ImReceiveItemDAO.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImReceiveItem> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImReceiveItem as model where 1=1 ");
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
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImReceiveItem findLine(Long headId, Long lineId) {
		log.info("ImReceiveItemDAO.findLine headId=" + headId + "lineId=" + lineId);
		final Long hId = headId;
		final Long lId = lineId;
		List<ImReceiveItem> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImReceiveItem as model where 1=1 ");
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
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	/*
	 * public PoPurchaseOrderLine updateLine(Long headId,PoPurchaseOrderLine
	 * line) { log.info("PoPurchaseOrderHeadDAO.updateLine headId=" + headId +
	 * "line=" + line ); Long hId = headId; PoPurchaseOrderHead
	 * poPurchaseOrderHead = new PoPurchaseOrderHead();
	 * poPurchaseOrderHead.setHeadId(headId); line.set this.save(line);
	 * 
	 * final Long lId = lineId; List<PoPurchaseOrderLine> re =
	 * getHibernateTemplate().executeFind(new HibernateCallback() { public
	 * Object doInHibernate(Session session) throws HibernateException,
	 * SQLException { StringBuffer hql = new StringBuffer("from
	 * PoPurchaseOrderLine as model where 1=1 "); if (null != hId) hql.append("
	 * and model.poPurchaseOrderHead.headId = :headId and model.lineId = :lineId
	 * "); Query query = session.createQuery(hql.toString()); if (null != hId)
	 * query.setLong("headId", hId); if (null != lId) query.setLong("lineId",
	 * lId); return query.list(); } }); if(( null != re ) && ( re.size() > 0 )){
	 * return re.get(0); } return null; }
	 */

	/**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("ImReceiveItemDAO.findPageLineMaxIndex" + headId);
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
			List<ImReceiveItem> imReceiveItems = re.get(0).getImReceiveItems();
			if (null != imReceiveItems && imReceiveItems.size() > 0)
				return imReceiveItems.get(imReceiveItems.size() - 1).getIndexNo();
		}
		return 0L;
	}
	
	public boolean isUsedByPo(String orderNo){
		log.debug("ImReceiveItemDAO isUsedByPo");
		try {
			StringBuffer biffer = new StringBuffer();
			biffer.append("from ImReceiveHead as model,ImReceiveItem as model2 \n")
			      .append("where model.status in ('SAVE','FINISH','CLOSE') \n")
			      .append("and model.headId = model2.imReceiveHead.headId \n")
			      .append("and model2.poOrderNo = ? \n")
			      .append("and model2.poOrderType = 'POL'");
			      Query queryObject = getSession().createQuery(biffer.toString());
			queryObject.setParameter(0, orderNo);
			int size = queryObject.list().size();
			if(size > 0){
				return true;
			}else{
				return false;
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			log.error("get failed", re);
			throw re;
		}	
	}
	
	/**
	 * 進貨單簽核
	 * 
	 * @param headId, greater
	 * @return
	 */
	public long getUnitPriceCount(long headId, double greater){
		log.debug("finding Count of ImReceiveItem with headId: " + headId + ", and localUnitPrice greater than: " + greater);
		try {
			String queryString = "select count(*) from ImReceiveItem where imReceiveHead.headId = ? and localUnitPrice > ?";
			return ((List<Long>)getHibernateTemplate().find(queryString, new Object[]{headId, greater})).get(0);
		}
		catch (RuntimeException re){
			log.error("finding count of ImReceiveItem localUnitPrice greater than " + greater + " failed", re);
			throw re;
		}
	}
}