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
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

public class FiBudgetHeadDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(FiBudgetHeadDAO.class);
	public static final String QUARY_TYPE_SELECT_ALL = "selectAll";
	public static final String QUARY_TYPE_SELECT_RANGE = "selectRange";
	public static final String QUARY_TYPE_RECORD_COUNT = "recordCount";
	public static final String QUARY_TYPE_SELECT_SHIFT = "shiftQuantity";
	
	public List<FiBudgetHead> find(HashMap findObjs) {
		log.info("DAO_Find");
		final HashMap fos = findObjs;
		log.info("DAO_Find::"+fos.get("categoryType"));
		/*if(fos.get("categoryType").equals("D")){
			findObjs.put("categoryType",  "5");
		}log.info("transfer"+fos.get("categoryType"));*/
		List<FiBudgetHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from FiBudgetHead as model where 1=1 ");

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
	
	
	
	public List<FiBudgetHead> find_new(HashMap findObjs ,PoPurchaseOrderHead poHead) {
		log.info("DAO_Find");
		final HashMap fos = findObjs;
		log.info("DAO_Find::"+fos.get("categoryType")+"poHead??"+poHead.getBudgetLineId());
		//-----------舊版轉新版對照表---------
		/*if(null == poHead.getBudgetLineId() || poHead.getBudgetLineId() == 0)
		if(fos.get("categoryType").equals("D") || fos.get("categoryType").equals("B") || fos.get("categoryType").equals("K")){
			findObjs.put("categoryType",  "5");
		}
		log.info("transfer3C影音=D,B,K"+fos.get("categoryType"));
		if(fos.get("categoryType").equals("F")){
			findObjs.put("categoryType",  "4");
		}
		log.info("transfer台產=F");
		if(fos.get("categoryType").equals("C") || fos.get("categoryType").equals("M")){
			findObjs.put("categoryType",  "3");
		}
		log.info("transfer化妝品=C,M");
		if(fos.get("categoryType").equals("E") || fos.get("categoryType").equals("S")){
			findObjs.put("categoryType",  "2");
		}
		log.info("transfer精品=E,S");
		if(fos.get("categoryType").equals("T")){
			findObjs.put("categoryType",  "1");
		}
		log.info("transfer菸酒巧克力=T");
		if(fos.get("categoryType").equals("N")){
			findObjs.put("categoryType",  "0");
		}
		log.info("transfer非正貨類=N");		
		//---------------------------------
		log.info("對照完");*/
		List<FiBudgetHead> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from FiBudgetHead as model where 1=1 ");

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
		hql.append("FROM FiBudgetHead bugH , FiBudgetLine bugL " ) ;
		hql.append("where bugH.orderTypeCode = ? and bugH.brandCode = ? and bugH.budgetYear = ? and bugH.budgetCheckType = ? and bugL.month <= ? and bugL.itemCode = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] {orderTypeCode, brandCode, bugetYear, budgetCheckType, month, itemCode });
		if ((null != result) && (result.size() > 0) && (null != result.get(0)) ) {
			re = (Double) result.get(0);
		}		
		return re ;
	}	

	public FiBudgetHead findById(Long headId) {
		return (FiBudgetHead) findByPrimaryKey(FiBudgetHead.class, headId);
	}
	public FiBudgetHead findByHead (String brandCode ,String budgetYear ,String status){
		log.info("findByHeadBudget::"+brandCode+budgetYear+status);
		StringBuffer hql = new StringBuffer("from FiBudgetHead as model where model.brandCode = ?");
		hql.append(" and model.budgetYear = ?");
		hql.append(" and model.status = ?");
		List<FiBudgetHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, budgetYear , status  });
		log.info("Budget:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public FiBudgetHead findByHeadByCategory (String brandCode ,String budgetYear ,String status,String itemType ,String budgetMonth){
		log.info("findByHeadBudgetCategory::"+brandCode+budgetYear+status+itemType);
		StringBuffer hql = new StringBuffer("from FiBudgetHead as model where model.brandCode = ?");
		hql.append(" and model.budgetYear = ?");
		hql.append(" and model.status = ?");
		hql.append(" and model.itemType = ?");
		hql.append(" and model.budgetMonth = ?");
		
		List<FiBudgetHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, budgetYear , status ,itemType ,budgetMonth});
		log.info("BudgetByCategory:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public FiBudgetHead findByHeadByCategoryT1 (String brandCode ,String budgetYear ,String status){
		log.info("findByHeadBudgetCategoryT1T1::"+brandCode+budgetYear+status);
		StringBuffer hql = new StringBuffer("from FiBudgetHead as model where model.brandCode = ?");
		hql.append(" and model.budgetYear = ?");
		hql.append(" and model.status = ?");
		
		
		List<FiBudgetHead> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { brandCode, budgetYear , status });
		log.info("BudgetT1:"+result.size());
		return (result != null && result.size() > 0 ? result.get(0) : null);
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
					hql.append("select count(model.headId) as rowCount from FiBudgetHead as model where 1=1 ");
				} else if (QUARY_TYPE_SELECT_ALL.equals(type)) {
					hql.append("select model.id.brandCode, model.id.itemCode, model.id.warehouseCode, model.id.lotNo from FiBudgetHead as model where 1=1 ");
				} else {
					hql.append("from FiBudgetHead as model where 1=1 ");
				}

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
					hql.append(" (select line.fiBudgetHead.headId from FiBudgetLine as line where line.itemBrandCode = :itemBrandCode) ");
				}
				
				hql.append(" order by model.budgetYear ");
			
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
