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

import tw.com.tm.erp.constants.SystemConfig;

import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.bean.SoShopDailyHeadId;

public class SoShopDailyHeadDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(SoShopDailyHeadDAO.class);

    /**
     * 依據專櫃每日資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<SoShopDailyHead> findShopDailyHeadList(HashMap conditionMap) {

	final String brandCode = (String) conditionMap.get("brandCode");
	final String employeeCode = (String) conditionMap.get("employeeCode");
	final String shopCode = (String) conditionMap.get("shopCode");
	final Date salesOrderStartDate = (Date) conditionMap
		.get("salesOrderStartDate");
	final Date salesOrderEndDate = (Date) conditionMap
		.get("salesOrderEndDate");
	final String isEnable = (String) conditionMap.get("isEnable");

	List<SoShopDailyHead> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"select model from SoShopDailyHead as model, BuShop as model2, BuShopEmployee as model3");
			hql.append(" where model.id.shopCode = model2.shopCode");
			hql.append(" and model2.shopCode = model3.id.shopCode");
			hql.append(" and model2.brandCode = :brandCode");
			hql.append(" and model3.id.employeeCode = :employeeCode");
			
			if(StringUtils.hasText(shopCode))
			    hql.append(" and model.id.shopCode = :shopCode");
			
			if (salesOrderStartDate != null)
			    hql.append(" and model.id.salesDate >= :salesOrderStartDate");

			if (salesOrderEndDate != null)
			    hql.append(" and model.id.salesDate <= :salesOrderEndDate");
			
			if(StringUtils.hasText(isEnable)){
			    hql.append(" and model2.enable = :isEnable");
			    hql.append(" and model3.enable = :isEnable");
			}

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

			query.setString("brandCode", brandCode);
			query.setString("employeeCode", employeeCode);
			
			if(StringUtils.hasText(shopCode))
			    query.setString("shopCode", shopCode);			

			if (salesOrderStartDate != null)
			    query.setDate("salesOrderStartDate",
				    salesOrderStartDate);

			if (salesOrderEndDate != null)
			    query.setDate("salesOrderEndDate",
				    salesOrderEndDate);
			
			if(StringUtils.hasText(isEnable)){
			    query.setString("isEnable", isEnable);
			}

			return query.list();
		    }
		});

	return result;
    }
    public List<SoShopDailyHead> findShopDailyHeadListT2(HashMap conditionMap) {

    	final String brandCode = (String) conditionMap.get("brandCode");
    	final String employeeCode = (String) conditionMap.get("employeeCode");
    	final String shopCode = (String) conditionMap.get("shopCode");
    	final Date salesOrderStartDate = (Date) conditionMap
    		.get("salesOrderStartDate");
    	final Date salesOrderEndDate = (Date) conditionMap
    		.get("salesOrderEndDate");
    	final String isEnable = (String) conditionMap.get("isEnable");


    	
    	
    	List<SoShopDailyHead> result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {
/*
model.shop_code = model4.pos_machine_code
and model4.shop_Code = model2.shop_Code
and model2.shop_Code = model3.shop_Code
and model2.brand_Code = 'T2'
and model3.employee_Code = 'T17888'
and model.shop_Code = '18'
and model.sales_Date >= to_date(20160124,'yyyymmdd')
and model.sales_Date <= to_date(20160124,'yyyymmdd')
and model2.enable = 'Y'
and model3.enable = 'Y'  
 * */
    			StringBuffer hql = new StringBuffer(
    				"select model from SoShopDailyHead as model, BuShop as model2, BuShopEmployee as model3,BuShopMachine as model4");
    			hql.append(" where model2.shopCode = model4.id.shopCode");
    			hql.append(" and model.id.shopCode = model4.id.posMachineCode");
    			hql.append(" and model2.shopCode = model3.id.shopCode");
    			hql.append(" and model2.brandCode = :brandCode");
    			hql.append(" and model3.id.employeeCode = :employeeCode");
   			    hql.append(" and model.id.shopCode = :shopCode");
   			    hql.append(" and model.id.salesDate >= :salesOrderStartDate");
   			    hql.append(" and model.id.salesDate <= :salesOrderEndDate");
   			    hql.append(" and model2.enable = :isEnable");
    			hql.append(" and model3.enable = :isEnable");


    			Query query = session.createQuery(hql.toString());
    			query.setFirstResult(0);
    			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

    			query.setString("brandCode", brandCode);
    			query.setString("employeeCode", employeeCode);
    			
    			    query.setString("shopCode", shopCode);			
    			    query.setDate("salesOrderStartDate", salesOrderStartDate);
    			    query.setDate("salesOrderEndDate", salesOrderEndDate);
    			    query.setString("isEnable", isEnable);

    			log.info("SQL:"+hql.toString());
    			return query.list();
    		    }
    		});

    	return result;
        }
    /**
     * 
     * @param findObjs
     * @param startPage
     * @param pageSize
     * @param searchType  
     * 	      1) get max record count 
     *        2) select data records according to startPage and pageSize 
     *        3) select all records 
     * @return
     */
    public HashMap findPageLine(HashMap findObjs, int startPage,  int pageSize, int searchType) {
		
	final HashMap fos = findObjs;
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final int type = searchType;
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	        public Object doInHibernate(Session session)throws HibernateException, SQLException {
	            	    	    
	    	    String shopCodes = (String)fos.get("shopCodes");
	    	    String batch = (String)fos.get("batch");
	    	    Date startSalesDate = (Date)fos.get("startSalesDate");
	    	    Date endSalesDate = (Date)fos.get("endSalesDate");	    	    	            
		    StringBuffer hql = new StringBuffer("");
		    if(BaseDAO.QUERY_RECORD_COUNT == type) {
		        hql.append("select count(*) as rowCount from SoShopDailyHead as model where 1=1 ");
		    }else if(BaseDAO.QUERY_SELECT_ALL == type) {
			hql.append("select model.id.shopCode, model.id.salesDate from SoShopDailyHead as model where 1=1 ");
		    }else{
			System.out.println("go here");
			hql.append("from SoShopDailyHead as model where 1=1 ");
		    }
		    
		    if (StringUtils.hasText(shopCodes))
		        hql.append(" and model.id.shopCode in (" +  shopCodes + ")");

		    if (StringUtils.hasText(batch))
		        hql.append(" and model.id.batch like :batch");
		    
		    if (startSalesDate != null)
			hql.append(" and model.id.salesDate >= :startSalesDate");

		    if (endSalesDate != null)
			hql.append(" and model.id.salesDate <= :endSalesDate");
		    
		    hql.append(" order by model.id.salesDate, model.id.shopCode");
		    System.out.println("hql=[" + hql.toString() + "]");
		    Query query = session.createQuery(hql.toString());
		    if(BaseDAO.QUERY_SELECT_RANGE == type) {
		        query.setFirstResult(startRecordIndexStar);
			query.setMaxResults(pSize);
		    }

		    if (startSalesDate != null)
			query.setDate("startSalesDate", startSalesDate);

		    if (endSalesDate != null)
			query.setDate("endSalesDate", endSalesDate);	
			
		    if (StringUtils.hasText(batch))
                query.setString("batch", "%" + batch + "%");		    
		    
		    return query.list();
		}
	});
	HashMap returnResult = new HashMap();		
	returnResult.put(BaseDAO.TABLE_LIST, BaseDAO.QUERY_SELECT_ALL == type || BaseDAO.QUERY_SELECT_RANGE == type? result: null);			
	if(result.size() == 0){
	    returnResult.put(BaseDAO.TABLE_RECORD_COUNT, 0L);
	}else{
	    returnResult.put(BaseDAO.TABLE_RECORD_COUNT, BaseDAO.QUERY_SELECT_ALL == type || BaseDAO.QUERY_SELECT_RANGE == type? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
    }
    public SoShopDailyHead findHeadByBatch(String shopCode,Date salesDate, String batch  
	) {
	log.info("findbatch"+shopCode+salesDate+batch);
	
	StringBuffer hql = new StringBuffer("from SoShopDailyHead as model ");
	hql.append("where model.id.shopCode = ? ");
	hql.append("and model.id.salesDate = ? ");
	hql.append("and model.id.batch = ? ");
	List<SoShopDailyHead> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { shopCode, salesDate ,batch});
	log.info("findbatch2222"+result.size());
	return (result != null && result.size() > 0 ? result.get(0) : null);
}
}