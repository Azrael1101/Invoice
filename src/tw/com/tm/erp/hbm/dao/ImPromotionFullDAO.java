package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionFull;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.utils.DateUtils;


public class ImPromotionFullDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImPromotionDAO.class);
	// property constants
	public static final String BRAND_CODE = "brandCode";
	public static final String ORDER_TYPE_CODE = "orderTypeCode";
	public static final String ORDER_NO = "orderNo";
	public static final String PROMOTION_NAME = "promotionName";
	public static final String PROMOTION_CODE = "promotionCode";
	public static final String DESCRIPTION = "description";
	public static final String IN_CHARGE = "inCharge";
	public static final String IS_ALL_ITEM = "isAllItem";
	public static final String IS_ALL_SHOP = "isAllShop";
	public static final String IS_ALL_CUSTOMER = "isAllCustomer";
	public static final String DISCOUNT = "discount";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

    public List getPromotionFullDataByBeginAndEnd(Map parameterMap, String isEnable) {
//    	Date startDate = (Date)parameterMap.get("DATA_DATE_STRAT");
//		Date endDate = (Date)parameterMap.get("DATA_DATE_END");
	
    	final String brandCode = (String) parameterMap.get("brandCode");
		final String promotionDate = (String) parameterMap.get("dataDate");
		final String promotionDateEnd = (String) parameterMap.get("dataDateEnd");
		final String orderTypeCode = (String) parameterMap.get("orderTypeCode");
		final String enable = isEnable;
		
		log.info(brandCode);
		log.info(promotionDate);
		log.info(promotionDateEnd);
		log.info(orderTypeCode);
		
		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("select model, model2 FROM ImPromotionFullView as model ");
				hql.append("where 1=1 ");
				hql.append("and model.brandCode = :brandCode ");
				if("Y".equals(enable)){
    				hql.append("and to_char(model.beginDate, 'YYYYMMDD') = :dataDate ");
				}else{
					hql.append("and to_char(model.endDate, 'YYYYMMDD') = :dataDate ");
				}
				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				hql.append("and model.orderTypeCode = :orderTypeCode ");
				log.info("hql: "+hql.toString());
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("dataDate", promotionDate);
				query.setString("dataDateEnd", promotionDateEnd);
				query.setString("orderTypeCode", orderTypeCode);
					
				return query.list();
			}
		});
		return result ;
	}
    
    /**
	 * find page line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return List<ImPromotionItem>
	 */
	 public List<ImPromotionFull> findPageLine(Long headId, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final Long hId = headId;
		List<ImPromotionFull> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from ImPromotionFull as model where 1=1 ");
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
						 StringBuffer hql = new StringBuffer("select count(model.imPromotion) as rowCount from ImPromotionFull as model where 1=1");
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

	 public ImPromotionFull findItemByIdentification(Long headId, Long lineId){

		 StringBuffer hql = new StringBuffer("from ImPromotionFull as model where model.imPromotion.headId = ? and model.lineId = ?");
		 List<ImPromotionFull> result = getHibernateTemplate().find(hql.toString(), new Object[]{headId, lineId});
		 return (result != null && result.size() > 0 ? result.get(0) : null);
	 }
}