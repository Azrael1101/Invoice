package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

public class FiBudgetModHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(FiBudgetModHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public static final String QUARY_TYPE_SELECT_SHIFT = "shiftQuantity";
	
	public List<FiBudgetModHead> find(HashMap findObjs) {

		final HashMap fos = findObjs;

		List<FiBudgetModHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from FiBudgetModHead as model where 1=1 ");

				if (StringUtils.hasText((String) fos.get("brandCode")))
					hql.append(" and model.brandCode = :brandCode ");

				if (StringUtils.hasText((String) fos.get("status")))
					hql.append(" and model.status = :status ");

				if (StringUtils.hasText((String) fos.get("budgetYear")))
					hql.append(" and model.budgetYear = :budgetYear ");
				
				if (StringUtils.hasText((String) fos.get("budgetMonth")))
					hql.append(" and model.budgetMonth = :budgetMonth ");

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					hql.append(" and model.orderTypeCode = :orderTypeCode ");
				
				if (StringUtils.hasText((String) fos.get("categoryType")))	// 業種
					hql.append(" and model.itemType = :categoryType ");

				hql.append(" order by lastUpdateDate desc ");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText((String) fos.get("brandCode")))
					query.setParameter("brandCode", fos.get("brandCode"));

				if (StringUtils.hasText((String) fos.get("status")))
					query.setString("status", (String) fos.get("status"));

				if (StringUtils.hasText((String) fos.get("budgetYear")))
					query.setString("budgetYear", (String) fos.get("budgetYear"));
				
				if (StringUtils.hasText((String) fos.get("budgetMonth")))
					query.setString("budgetMonth", (String) fos.get("budgetMonth"));

				if (StringUtils.hasText((String) fos.get("orderTypeCode")))
					query.setString("orderTypeCode", (String) fos.get("orderTypeCode"));
				
				if (StringUtils.hasText((String) fos.get("categoryType")))
					query.setString("categoryType", (String) fos.get("categoryType"));
				
				//System.out.println( hql.toString() );
				return query.list();
			}
		});

		return re;
	}
	
	/**
	 * get item budget amount
	 * BRAND_CODE , BUDGET_YEAR , BUDGET_CHECK_TYPE , MONTH , ITEM_CODE
	 * @param brandCode
	 * @param bugetYear
	 * @param budgetCheckType
	 * @param month
	 * @param itmeCode
	 * @return
	 */
	public Double getItemBudgetAmount(String orderTypeCode,String brandCode,String bugetYear,String budgetCheckType,Long month,String itemCode){
		Double re = new Double(0);
		if( null == budgetCheckType)
			budgetCheckType = "M" ;
		StringBuffer hql = new StringBuffer();
		hql.append("select sum( bugL.budgetAmount ) as total " ); 
		hql.append("FROM FiBudgetModHead bugH , FiBudgetModLine bugL " ) ;
		hql.append("where bugH.orderTypeCode = ? and bugH.brandCode = ? and bugH.budgetYear = ? and bugH.budgetCheckType = ? and bugL.month <= ? and bugL.itemCode = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] {orderTypeCode, brandCode, bugetYear, budgetCheckType, month, itemCode });
		if ((null != result) && (result.size() > 0) && (null != result.get(0)) ) {
			re = (Double) result.get(0);
		}		
		return re ;
	}	

	public FiBudgetModHead findById(Long headId) {
		return (FiBudgetModHead) findByPrimaryKey(FiBudgetModHead.class, headId);
	}
	
	//檢核該年度月份預算是否尚在簽核-Jerome
	public List<FiBudgetModHead> getDiffBudget(HashMap findObjs) {
		
		final String brandCode = (String) findObjs.get("brandCode");
		final String budgetYear = (String) findObjs.get("budgetYear");
		final String budgetMonth = (String) findObjs.get("budgetMonth");
		final String itemType = (String) findObjs.get("itemType");
		final Long formId = (Long)findObjs.get("formId");
				
		List<FiBudgetModHead> result = null;
		result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from FiBudgetModHead as model where 1=1");
				hql.append(" and model.brandCode = :brandCode ");
				hql.append(" and model.budgetYear = :budgetYear ");
				hql.append(" and model.budgetMonth = :budgetMonth ");
				hql.append(" and model.itemType = :itemType  ");
				hql.append(" and model.status = 'SIGNING' ");
				hql.append(" and model.headId <> :formId ");
								
				Query query = session.createQuery(hql.toString());
				
				query.setString("brandCode", brandCode);
				query.setString("budgetYear", budgetYear);
				query.setString("budgetMonth", budgetMonth);
				query.setString("itemType", itemType);					
				query.setLong("formId", formId);
									
				log.debug("FiBudgetModHeadDAO.getDiffBudget hql=" + hql);	
				return query.list();
		}
			});
		
		return result;
		
		
		
	}
	
	public Map findPageLine(HashMap findObjs, int startPage, int pageSize, String searchType){
		final HashMap fos = findObjs;
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String type = searchType;
		
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				if (QUARY_TYPE_RECORD_COUNT.equals(type)) {
					hql.append("select count(model.headId) as rowCount from FiBudgetModHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.id.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo from FiBudgetModHead as model where 1=1 ");
				} else {
					hql.append("from FiBudgetModHead as model where 1=1 ");
				}
				
				hql.append(" and model.orderNo not like 'TMP%' ");

				if (StringUtils.hasText((String) fos.get("year"))){
					hql.append(" and model.budgetYear = :year ");
				}
				if (StringUtils.hasText((String) fos.get("month"))){
					hql.append(" and model.budgetMonth = :month ");
				}
				if (StringUtils.hasText((String) fos.get("brandCode"))){
					hql.append(" and model.brandCode = :brandCode ");
				}
				if (StringUtils.hasText((String) fos.get("itemType"))){
					hql.append(" and model.itemType = :itemType ");
				}
				if (StringUtils.hasText((String) fos.get("status"))){
					hql.append(" and model.status = :status ");
				}
				if (StringUtils.hasText((String) fos.get("itemBrandCode"))){
					hql.append(" and model.headId in ");
					hql.append(" (select line.fiBudgetModHead.headId  ");
					hql.append(" from FiBudgetModLine as line ");
					hql.append(" where 1=1 ");
//					hql.append(" and line.budgetAdjustAmount > 0 ");    //20200821 財務不需要此查詢條件故刪除
					hql.append(" and line.itemBrandCode = :itemBrandCode ");
					hql.append(" ) ");
				}
				if (StringUtils.hasText((String) fos.get("orderNo"))){
					hql.append(" and model.orderNo = :orderNo ");
				}
				
				hql.append(" order by model.orderNo desc ");
			
				log.info("hql = " + hql.toString());

				Query query = session.createQuery(hql.toString());

				if (QUARY_TYPE_SELECT_RANGE.equals(type)) {
					query.setFirstResult(startRecordIndexStar);
					query.setMaxResults(pSize);
				}
				
				if (StringUtils.hasText((String) fos.get("year"))){
					query.setString("year", (String)fos.get("year"));
				}
				if (StringUtils.hasText((String) fos.get("month"))){
					query.setString("month", (String)fos.get("month"));
				}
				if (StringUtils.hasText((String) fos.get("brandCode"))){
					query.setString("brandCode", (String)fos.get("brandCode"));
				}
				if (StringUtils.hasText((String) fos.get("itemType"))){
					query.setString("itemType", (String)fos.get("itemType"));
				}
				if (StringUtils.hasText((String) fos.get("status"))){
					query.setString("status", (String)fos.get("status"));
				}
				if (StringUtils.hasText((String) fos.get("itemBrandCode"))){
					query.setString("itemBrandCode", (String)fos.get("itemBrandCode"));
				}
				if (StringUtils.hasText((String) fos.get("orderNo"))){
					query.setString("orderNo", (String)fos.get("orderNo"));
				}
				return query.list();
			}
		});
		Map returnResult = new HashMap();
		returnResult.put("form", QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result : null);
		if (result.size() == 0) {
			returnResult.put("recordCount", 0L);
		} else {
			returnResult.put("recordCount",
					QUARY_TYPE_SELECT_ALL.equals(type) || QUARY_TYPE_SELECT_RANGE.equals(type) ? result.size() : Long.valueOf(result.get(0).toString()));
		}
		return returnResult;
	}
	

}
