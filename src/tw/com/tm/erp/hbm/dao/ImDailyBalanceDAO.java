package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.ImDailyBalanceLine;

public class ImDailyBalanceDAO extends BaseDAO {
	
	
	public int deleteDailyBalance(final String entityBean, final String brandCode, final String dailyDate)throws Exception{
		
		int result = (Integer)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("delete from ");
				hql.append(entityBean);
				hql.append(" as model where 1=1");
				hql.append(" and model.id.brandCode = :brandCode");
    		    hql.append(" and model.id.dailyDate = :dailyDate");
				
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
    			query.setString("dailyDate", dailyDate);
    			
				return query.executeUpdate();
			}
		});

		return result;
	}
	
	public int insertBeginningOnHandQty(final String sourceEntityBean, final String targetEntityBean, final String sourceColumn, final String targetColumn, final String fieldNames[], final String fieldValues[], final String groupFieldName)throws Exception{
		System.out.println("進入新增資料............");
		int result = (Integer)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("insert into ");
				hql.append(targetEntityBean);
				hql.append("(");
				hql.append(targetColumn);
				hql.append(")");
				hql.append(" select ");
				hql.append(sourceColumn);
				hql.append(" from ");
				hql.append(sourceEntityBean);
				hql.append(" model where 1=1");

				for( String fieldName : fieldNames){
					hql.append(" and ");
					hql.append(" model.");
					hql.append(fieldName);
					hql.append(" = :");
					hql.append(fieldName.replace("id.", ""));
				}
				
				if (groupFieldName!=""){hql.append(" group by " + groupFieldName);}
				
				Query query = session.createSQLQuery(hql.toString());
				
				for(int i=0;i<fieldValues.length;i++){	
					query.setString(fieldNames[i].replace("id.", ""), fieldValues[i]);
				}
				
				return query.executeUpdate();
			}
		});
		
		return result;
	}
	
	public List<List> getSummaryDailyBalance(final String entityBean, final String summaryColumn, final String fieldNames[], final String fieldValues[], final String groupFieldName)
	{
		//List<Object[]>
		List<List>  result = getHibernateTemplate().executeFind(new HibernateCallback(){
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer hql = new StringBuffer("select new list(");
				hql.append(groupFieldName);
				hql.append(",");
				hql.append(summaryColumn);
				hql.append(") from ");
				hql.append(entityBean);
				hql.append(" model where 1=1");
				
				for( String fieldName : fieldNames){
					hql.append(" and ");
					hql.append(" model.");
					hql.append(fieldName);
					hql.append(" = :");
					hql.append(fieldName.replace("id.", ""));
				}
				
				if (groupFieldName!=""){hql.append(" group by " + groupFieldName);}
				
				System.out.println("hql:"+hql.toString());
				
				Query query = session.createQuery(hql.toString());
				
				//query.setResultTransformer(arg0)
				
				for(int i=0;i<fieldValues.length;i++){	
					query.setString(fieldNames[i].replace("id.", ""), fieldValues[i]);
				}
				
				return query.list();
			}
			
			
		});
		
		return result;
	}
	
	
	
	public int findData(){
		
		int result = (Integer)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				
				//StringBuffer hql = new StringBuffer("insert into IM_DAILY_BALANCE_LINE(BRAND_CODE,ITEM_CODE,DAILY_DATE,WAREHOUSE_CODE,LOT_NO) ");
				//hql.append("select BRAND_CODE,ITEM_CODE,'20140601',WAREHOUSE_CODE,LOT_NO from IM_MONTHLY_BALANCE_LINE model where 1=1 and  model.BRAND_CODE = :brandCode and model.YEAR = :year and model.MONTH = :month and model.ITEM_CODE = :itemCode and model.WAREHOUSE_CODE = :warehouseCode");
				
				StringBuffer hql = new StringBuffer("insert into ImDailyBalanceLine(id.brandCode,id.itemCode,id.dailyDate,id.warehouseCode,id.lotNo) ");
				hql.append("select id.brandCode,id.itemCode,'20140601',id.warehouseCode,id.lotNo from ImMonthlyBalanceLine as model where 1=1 and  model.id.brandCode = :brandCode and model.id.year = :year and model.id.month = :month and model.id.itemCode = :itemCode and model.id.warehouseCode = :warehouseCode");
				//StringBuffer hql = new StringBuffer("insert into BuCurrency(currencyCode,currencyCName) select id.brandCode,'20140601' from ImMonthlyBalanceLine as model where 1=1 and  model.id.brandCode = :brandCode and model.id.year = :year and model.id.month = :month and model.id.itemCode = :itemCode and model.id.warehouseCode = :warehouseCode");
				Query query = session.createQuery(hql.toString());
				
				System.out.println("sql:"+query.toString());
				
				query.setString("brandCode", "T2");
				query.setString("year", "2014");
				query.setString("month", "05");
				query.setString("itemCode", "000159386722F");
				query.setString("warehouseCode", "21360");
				
				//return query.list();
				
				return query.executeUpdate();
				
			}
			
		});
		
		return result;
		
		
	}
	
	
}
