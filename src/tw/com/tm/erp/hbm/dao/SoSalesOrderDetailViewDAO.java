package tw.com.tm.erp.hbm.dao;


import java.sql.SQLException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;



public class SoSalesOrderDetailViewDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(SoSalesOrderHeadDAO.class);
    public static final String QUARY_TYPE_SELECT_ALL = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public static final String QUARY_TYPE_SELECT_SHIFT = "shiftQuantity";
	
    /**
     * 依據銷貨單查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public HashMap findPageLine(HashMap conditionMap, int startPage, int pageSize, String searchType) {
    	
    	final String itemCode = (String) conditionMap.get("itemCode");
		final String itemName = (String) conditionMap.get("itemName");
		final String customerCode = (String) conditionMap.get("customerCode");
		final Date salesOrderBeginDate = (Date) conditionMap.get("salesOrderBeginDate");
		final Date salesOrderEndDate = (Date) conditionMap.get("salesOrderEndDate");
		final String brandCode = (String) conditionMap.get("brandCode");
		final String type = searchType;
		final int startRecordIndexStar  = startPage * pageSize;;
		final int pSize = pageSize;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.id.brandCode) as rowCount  from SoSalesOrderSimpleView as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					hql.append(" from SoSalesOrderSimpleView as model where 1=1 ");
				} 
				hql.append(" and model.headStatus in ('FINISH', 'CLOSE')");
				hql.append(" and model.id.brandCode = :brandCode");
				hql.append(" and model.id.orderTypeCode = :orderTypeCode");
				hql.append(" and model.customerCode is not null");
				if (StringUtils.hasText(itemCode)){
				    hql.append(" and model.itemCode = :itemCode");
				}
				if (StringUtils.hasText(itemName)){
				    hql.append(" and model.itemCName like :itemName");
				}
				if (StringUtils.hasText(customerCode)){
				    hql.append(" and model.customerCode = :customerCode");
				}
				if (salesOrderBeginDate != null){
				    hql.append(" and model.salesOrderDate >= :salesOrderBeginDate");
				}
				if (salesOrderEndDate != null){
				    hql.append(" and model.salesOrderDate <= :salesOrderEndDate");
				}
				
				hql.append(" order by model.salesOrderDate desc");
				
				Query query = session.createQuery(hql.toString());
				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				query.setString("brandCode", brandCode);
				query.setString("orderTypeCode", "SOP");
				
				if (StringUtils.hasText(itemCode)){
				    query.setString("itemCode", itemCode);
				}
				if (StringUtils.hasText(itemName)){
				    query.setString("itemCName", "%"+itemName+"%");
				}
				if (StringUtils.hasText(customerCode)){
				    query.setString("customerCode", customerCode);
				}
				if (salesOrderBeginDate != null){
				    query.setDate("salesOrderBeginDate", salesOrderBeginDate);
				}
				if (salesOrderEndDate != null){
					query.setDate("salesOrderEndDate", salesOrderEndDate);
				};
				
				return query.list();
		    }
		});
		
		HashMap returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			log.info("result.get(0).toString()"+result.get(0).toString());
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}

		return returnResult;
    }
    
	
}