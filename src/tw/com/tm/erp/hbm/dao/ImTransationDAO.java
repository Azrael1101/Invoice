package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Hibernate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImTransation;

public class ImTransationDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(ImTransationDAO.class);
    
    /**
	 * 依據品牌代號、品號、庫號、盤點日查詢至盤點日的庫存量
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findCountsDateOnHandQty(HashMap conditionMap) throws Exception {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String itemCode = (String) conditionMap.get("itemCode");
		final String warehouseCode = (String) conditionMap.get("warehouseCode");
		//final Date countsDate = (Date) conditionMap.get("countsDate");		
		final String beginTransactionDate = (String)conditionMap.get("beginTransactionDate");
		final String endTransactionDate = (String)conditionMap.get("endTransactionDate");
		final int year = (Integer)conditionMap.get("year");
		final int month = (Integer)conditionMap.get("month");	
		
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"SELECT" +
						" CASE" +
						" WHEN b.item_code IS NULL" +
						" THEN t.item_code" +
						" ELSE b.item_code" +
						" END item_code," +
						" CASE" +
						" WHEN b.warehouse_code IS NULL" +
						" THEN t.warehouse_code" +
						" ELSE b.warehouse_code" +
						" END warehouse_code," +
						" CASE" +
						" WHEN b.brand_code IS NULL" +
						" THEN t.brand_code" +
						" ELSE b.brand_code" +
						" END brand_code," + 
						" MONTHLY_QTY ," +
						" TRANSACTION_QTY," +
						" (NVL(MONTHLY_QTY,0) + NVL(TRANSACTION_QTY,0)) AS ON_HAND_QTY FROM ");
			        hql.append("(SELECT b.item_code, b.warehouse_code, b.brand_code, SUM (ENDING_ON_HAND_QUANTITY) AS MONTHLY_QTY FROM IM_MONTHLY_BALANCE_LINE b" +
						" WHERE b.BRAND_CODE = :brandCode AND b.ITEM_CODE like :itemCode AND b.YEAR = :year AND b.MONTH = :month AND b.WAREHOUSE_CODE = :warehouseCode" +
						" GROUP BY b.item_code, b.warehouse_code, b.brand_code) b FULL JOIN ");
			        
			        hql.append("(SELECT t.item_code, t.warehouse_code, t.brand_code, SUM (quantity) AS TRANSACTION_QTY FROM IM_TRANSATION t" +
			        	        " WHERE t.BRAND_CODE = :brandCode AND t.ITEM_CODE like :itemCode AND t.WAREHOUSE_CODE = :warehouseCode AND TO_CHAR (t.transation_date, 'YYYY-MM-DD') BETWEEN :beginTransactionDate AND :endTransactionDate" + 
			        	        " GROUP BY t.item_code, t.warehouse_code, t.brand_code) t");
						
			        hql.append(" ON t.item_code = b.item_code AND t.warehouse_code = b.warehouse_code AND t.brand_code = b.brand_code ORDER BY item_code");		

				Query query = session.createSQLQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				query.setString("brandCode", brandCode);
				if(itemCode.equals("")){
					query.setString("itemCode", "%%");
				}
				else
				{
				query.setString("itemCode", itemCode);
				}
				query.setString("warehouseCode", warehouseCode);
				query.setString("beginTransactionDate", beginTransactionDate.toString());
				query.setString("endTransactionDate", endTransactionDate);
				query.setInteger("year", year);
				query.setInteger("month", month);
		
				return query.list();
			}
		});

		return result;
	}
	public List findTransactionCountsDateOnHandQty(HashMap conditionMap) throws Exception {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String itemCode = (String) conditionMap.get("itemCode");
		final String warehouseCode = (String) conditionMap.get("warehouseCode");
		//final Date countsDate = (Date) conditionMap.get("countsDate");		
		final String beginTransactionDate = (String)conditionMap.get("beginTransactionDate");
		final String endTransactionDate = (String)conditionMap.get("endTransactionDate");
		//final int year = (Integer)conditionMap.get("year");
		//final int month = (Integer)conditionMap.get("month");	
		
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"SELECT" +
						" CASE" +
						" WHEN b.item_code IS NULL" +
						" THEN t.item_code" +
						" ELSE b.item_code" +
						" END item_code," +
						" CASE" +
						" WHEN b.warehouse_code IS NULL" +
						" THEN t.warehouse_code" +
						" ELSE b.warehouse_code" +
						" END warehouse_code," +
						" CASE" +
						" WHEN b.brand_code IS NULL" +
						" THEN t.brand_code" +
						" ELSE b.brand_code" +
						" END brand_code," + 
						" MONTHLY_QTY ," +
						" TRANSACTION_QTY," +
						" (NVL(MONTHLY_QTY,0) + NVL(TRANSACTION_QTY,0)) AS ON_HAND_QTY FROM ");
			         
			        hql.append("(SELECT t.item_code, t.warehouse_code, t.brand_code, SUM (quantity) AS TRANSACTION_QTY FROM IM_TRANSATION t" +
			        	        " WHERE t.BRAND_CODE = :brandCode AND t.ITEM_CODE like :itemCode AND t.WAREHOUSE_CODE = :warehouseCode AND TO_CHAR (t.transation_date, 'YYYY-MM-DD') BETWEEN :beginTransactionDate AND :endTransactionDate" + 
			        	        " GROUP BY t.item_code, t.warehouse_code, t.brand_code) t");
						
			        hql.append(" ON t.item_code = b.item_code AND t.warehouse_code = b.warehouse_code AND t.brand_code = b.brand_code ORDER BY item_code");		

				Query query = session.createSQLQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				query.setString("brandCode", brandCode);
				if(itemCode.equals("")){
					query.setString("itemCode", "%%");
				}
				else
				{
				query.setString("itemCode", itemCode);
				}
				query.setString("warehouseCode", warehouseCode);
				query.setString("beginTransactionDate", beginTransactionDate.toString());
				query.setString("endTransactionDate", endTransactionDate);
		
				return query.list();
			}
		});

		return result;
	}
	
	public List findTransationByDate(final String brandCode,final String statisticDate,final String typeCode){
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer(
						"SELECT t.BRAND_CODE,t.TRANSATION_DATE,t.ITEM_CODE,t.WAREHOUSE_CODE,t.LOT_NO,sum(t.QUANTITY) as QUANTITY FROM ERP.IM_TRANSATION T INNER JOIN ERP.BU_ORDER_TYPE O ON " +
						"T.BRAND_CODE = O.BRAND_CODE AND "+
						"T.ORDER_TYPE_CODE = O.ORDER_TYPE_CODE "+
						"WHERE T.BRAND_CODE = :brandCode AND "+
						"TO_CHAR(T.TRANSATION_DATE,'YYYYMMDD') = :transactionDate AND "+
						"O.MONTHLY_BALANCE_TYPE_CODE = :typeCode "+
						"GROUP BY t.BRAND_CODE,t.TRANSATION_DATE,t.ITEM_CODE,t.WAREHOUSE_CODE,t.LOT_NO"
						);
				
				System.out.println("hql:"+hql.toString());
				
				Query query = session.createSQLQuery(hql.toString())
						.addScalar("BRAND_CODE", Hibernate.STRING)
						.addScalar("TRANSATION_DATE", Hibernate.DATE)
						.addScalar("ITEM_CODE", Hibernate.STRING)
						.addScalar("WAREHOUSE_CODE", Hibernate.STRING)
						.addScalar("LOT_NO", Hibernate.STRING)
						.addScalar("QUANTITY", Hibernate.LONG);
				
				query.setString("brandCode", brandCode);
				query.setString("transactionDate", statisticDate);
				query.setString("typeCode", typeCode);
				
				return query.list();
			}
		});

		return result;

	}

	public int deleteByLotNo(String lotNo) {
		final String finalLotNo = lotNo;
		int t = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("delete from im_transation where lot_no =").append(finalLotNo);
				Query query = session.createSQLQuery(hql.toString());
				return query.executeUpdate();
			}
		});
		return t;
	}
	
	public List<ImTransation> findTransationByIdentification(String brandCode, String orderTypeCode, String orderNo) {
		StringBuffer hql = new StringBuffer("from ImTransation as model where model.brandCode = ?");
	        hql.append(" and model.orderTypeCode = ?");
	        hql.append(" and model.orderNo = ?");
	        log.info("SQL:"+hql.toString());
	        List<ImTransation> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { brandCode, orderTypeCode,  orderNo});
	        
		return result;
	}
	
	/**
     * 砍掉交易記錄
     */
	public void deleteTransationByIdentification(final String brandCode, final String orderTypeCode, final String orderNo) throws Exception{
		getHibernateTemplate().execute(new HibernateCallback() {
		    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("delete from ImTransation as model where 1=1 ");
    			hql.append(" and model.brandCode = :brandCode");
    		    hql.append(" and model.orderTypeCode = :orderTypeCode");
    		    hql.append(" and model.orderNo = :orderNo");
    			Query query = session.createQuery(hql.toString());
    			query.setString("brandCode", brandCode);
    			query.setString("orderTypeCode", orderTypeCode);
    			query.setString("orderNo", orderNo);
    			return query.executeUpdate();
		    }
		});
	}
}