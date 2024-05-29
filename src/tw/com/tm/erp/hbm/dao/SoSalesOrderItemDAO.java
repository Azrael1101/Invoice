package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.PrItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class SoSalesOrderItemDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(SoSalesOrderItemDAO.class);

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<SoSalesOrderItem>
	 */
	public List<SoSalesOrderItem> findPageLine(Long headId, int startPage,
			int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<SoSalesOrderItem> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where 1=1 ");
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
			List<SoSalesOrderItem> soSalesOrderItems = result.get(0).getSoSalesOrderItems();		    
			if (soSalesOrderItems != null && soSalesOrderItems.size() > 0){
				lineMaxIndex = soSalesOrderItems.get(soSalesOrderItems.size() - 1).getIndexNo();	
			}
		}
		return lineMaxIndex;
	}

	public SoSalesOrderItem findItemByIdentification(Long headId, Long lineId){

		StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where model.soSalesOrderHead.headId = ? and model.lineId = ?");
		List<SoSalesOrderItem> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}

	public List<SoSalesOrderItem> findByHeadId(Long headId){

		StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where model.soSalesOrderHead.headId = ? order by model.itemCode");
		List<SoSalesOrderItem> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId});
		return result;
	}

	public List<SoSalesOrderItem> findPageLineByCustomerPoNo(String brandCode, String customerPoNo, int startPage,
			int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String  brand = brandCode;
		final String poNo = customerPoNo;
		List<SoSalesOrderItem> result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where 1=1 ");
						hql.append(" and model.soSalesOrderHead.headId = ");
						hql.append(" (select max(head.headId) from SoSalesOrderHead as head where head.brandCode = :brand and head.customerPoNo= :poNo)");
						hql.append(" order by indexNo");
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(startRecordIndexStar);
						query.setMaxResults(pSize);

						query.setString("brand", brand);
						query.setString("poNo", poNo);
						return query.list();
					}
				});
		return result;
	}
	public List<SoSalesOrderItem> findByCustomerItem( String itemCode) {
		log.info("findByCustomerItem...........");
		StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where model.itemCode = ?");	
		//hql.append(" and model.itemCode = ?");
		Object[] objArray = null;
		objArray = new Object[] { itemCode };		
		List<SoSalesOrderItem> result = getHibernateTemplate().find(hql.toString(), objArray);

		return result;
	}
	
	public SoSalesOrderItem findItem( String itemCode) {
		log.info("findItem...........");
    	if(null != itemCode ){
    		itemCode = itemCode.toUpperCase();
    	}
    
	StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where 1=1 ");
	//hql.append(" and model.brandCode = ? ");
	hql.append(" and model.itemCode  = ? ");
	log.info("QQQQQQQQ");
	List<SoSalesOrderItem> lists = getHibernateTemplate().find(hql.toString(), new String[] { itemCode });
	log.info("...........");
	if ((null != lists) && (lists.size() > 0))
		return lists.get(0);
	else
		return null;

}
	
	public SoSalesOrderItem findByHead(Long headId){

		StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where model.soSalesOrderHead.headId = ? ");
		List<SoSalesOrderItem> lists = getHibernateTemplate().find(hql.toString(), new Object[]{headId});
		if ((null != lists) && (lists.size() > 0))
			return lists.get(0);
		else
			return null;
	}
	
	public SoSalesOrderItem findByHeadWithitemCode(Long headId,String itemCode){

		StringBuffer hql = new StringBuffer("from SoSalesOrderItem as model where model.soSalesOrderHead.headId = ? and model.itemCode = ?");
		List<SoSalesOrderItem> lists = getHibernateTemplate().find(hql.toString(), new Object[]{headId,itemCode});
		if ((null != lists) && (lists.size() > 0))
			return lists.get(0);
		else
			return null;
	}
}