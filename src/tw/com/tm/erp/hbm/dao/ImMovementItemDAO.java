package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class ImMovementItemDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImMovementItemDAO.class);

	public List getDeliveryWarehouseManager(final Long headId) {

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				ImMovementHead imMovementHead = new ImMovementHead(headId);

				StringBuffer hql = new StringBuffer("Select warehouse.warehouseManager as warehouseManager ");

				hql.append("from ImMovementItem as item, ImWarehouse as warehouse where 1=1 ");

				hql.append("and item.imMovementHead = :imMovementHead ");
				hql.append("and item.deliveryWarehouseCode = warehouse.warehouseCode ");
				hql.append("group by warehouse.warehouseManager ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("imMovementHead", (ImMovementHead) imMovementHead);

				return query.list();
			}
		});

		return re;
	}

	public List getArrivalWarehouseManager(final Long headId) {

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				ImMovementHead imMovementHead = new ImMovementHead(headId);
				StringBuffer hql = new StringBuffer("Select warehouse.warehouseManager as warehouseManager ");

				hql.append("from ImMovementItem as item, ImWarehouse as warehouse where 1=1 ");

				hql.append("and item.imMovementHead = :imMovementHead ");
				hql.append("and item.arrivalWarehouseCode = warehouse.warehouseCode ");
				hql.append("group by warehouse.warehouseManager ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("imMovementHead", (ImMovementHead) imMovementHead);

				return query.list();
			}
		});

		return re;
	}

	/**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImMovementItem>
	 */
	public List<ImMovementItem> findPageLine(Long headId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImMovementItem> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImMovementItem as model where 1=1 ");
				if (hId != null)
					hql.append(" and model.imMovementHead.headId = :headId order by indexNo");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (hId != null)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		log.info("imMovementItems.headId:" + headId);
		log.info("imMovementItems.size:" + result.size());
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
		List<ImMovementHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImMovementHead as model where 1=1 ");
				if (hId != null)
					hql.append(" and model.headId = :headId");
				Query query = session.createQuery(hql.toString());
				if (hId != null)
					query.setLong("headId", hId);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			List<ImMovementItem> imMovementItems = result.get(0).getImMovementItems();
			if (imMovementItems != null && imMovementItems.size() > 0) {
				lineMaxIndex = imMovementItems.get(imMovementItems.size() - 1).getIndexNo();
			}
		}
		return lineMaxIndex;
	}

	public ImMovementItem findItemByIdentification(Long headId, Long lineId) {
		StringBuffer hql = new StringBuffer(
				"from ImMovementItem as model where model.imMovementHead.headId = ? and model.lineId = ?");
		List<ImMovementItem> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId, lineId });
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}

	public List<ImMovementItem> findByHeadId(Long headId, String sortString) {
		StringBuffer hql = new StringBuffer("from ImMovementItem as model where model.imMovementHead.headId = ? ");
		if (StringUtils.hasText(sortString)) {
			hql.append("order by " + sortString);
		}
		List<ImMovementItem> result = getHibernateTemplate().find(hql.toString(), new Object[] { headId });
		return result;
	}

	public Long countBoxNo(Long headId) {
		StringBuffer hql = new StringBuffer(
				"select count(distinct boxNo) from ImMovementItem as model where model.imMovementHead.headId = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] { headId });
		return 0 == result.size() ? 0L : Long.valueOf(result.get(0).toString());
	}

	public Long sumItemQuantity(Long headId, String fieldName) {
		StringBuffer hql = new StringBuffer("select sum(" + fieldName
				+ ") from ImMovementItem as model where model.imMovementHead.headId = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] { headId });

		return 0 == result.size() ? 0L : Long.valueOf(result.get(0).toString());

	}

	/**
	 * find page line include price
	 * 
	 * @param headId
	 * @param brandCode
	 * @return List<Object[]>
	 */
	public List<Object[]> findPageLineIncludePrice(Long headId,String brandCode, String exportBeanName) {

		final Long hId = headId;
		final String bCode = brandCode;
		final String beanName = exportBeanName;
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("T2_IM_MOVEMENT_ITEM_3".equals(beanName))
					hql.append("SELECT A.INDEX_NO, A.BOX_NO, A.ITEM_CODE, D.ITEM_C_NAME, C.UNIT_PRICE, A.DELIVERY_WAREHOUSE_CODE, A.LOT_NO, A.ORIGINAL_DELIVERY_QUANTITY, A.ARRIVAL_WAREHOUSE_CODE, A.ORIGINAL_DECLARATION_NO, A.ORIGINAL_DECLARATION_SEQ, A.ORIGINAL_DECLARATION_DATE ");
				else
					hql.append("SELECT A.INDEX_NO, A.BOX_NO, A.ITEM_CODE, D.ITEM_C_NAME, C.UNIT_PRICE, A.DELIVERY_WAREHOUSE_CODE, A.LOT_NO, A.DELIVERY_QUANTITY, A.ARRIVAL_WAREHOUSE_CODE, A.ORIGINAL_DECLARATION_NO, A.ORIGINAL_DECLARATION_SEQ, A.ORIGINAL_DECLARATION_DATE ");
				hql.append("FROM IM_MOVEMENT_ITEM A ");
				hql.append("JOIN IM_MOVEMENT_HEAD B ON A.HEAD_ID = B.HEAD_ID ");
				hql.append("JOIN IM_ITEM_CURRENT_PRICE_VIEW C ON A.ITEM_CODE = C.ITEM_CODE AND B.BRAND_CODE = C.BRAND_CODE ");
				hql.append("JOIN IM_ITEM D ON A.ITEM_CODE = D.ITEM_CODE AND B.BRAND_CODE = D.BRAND_CODE WHERE 1=1 ");
				
				if (hId != null)
					hql.append(" and A.HEAD_ID = :headId ");
				if (bCode != null)
					hql.append(" and B.brand_code = :brandCode ");
				hql.append(" order by INDEX_NO");
				//System.out.println("SQL ::: " + hql.toString());
				Query query = session.createSQLQuery(hql.toString());
				//query.setFirstResult(startRecordIndexStar);
				//query.setMaxResults(pSize);
				if (hId != null)
					query.setLong("headId", hId);
				if (bCode != null)
					query.setString("brandCode", bCode);
				return query.list();
			}
		});
		log.info("imMovementItems.headId:" + headId);
		log.info("imMovementItems.size:" + result.size());
		return result;
	}
}