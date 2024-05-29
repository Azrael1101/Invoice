package tw.com.tm.erp.hbm.dao;

import java.util.List;

import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;

public class ImMonthlyBalanceHeadDAO extends BaseDAO {
	 /**
	  * 依據品牌及狀態查詢調撥單
	  * @param brandCode
	  * @param status
	  * @return
	  */
	 public List<ImMonthlyBalanceHead> findImMonthlyBalanceHeadByItemCode(String brandCode, String itemCode, int maxRecord){
	     StringBuffer hql = new StringBuffer(
	         "from ImMonthlyBalanceHead as model where 1=1");
	     hql.append(" and model.id.brandCode = ?");
	     hql.append(" and model.id.itemCode = ?");
	     hql.append(" order by model.id.year, model.id.month desc");
	     if(maxRecord != 0)
	    	 getHibernateTemplate().setMaxResults(maxRecord);
	     
	     List<ImMonthlyBalanceHead> result = getHibernateTemplate().find(
	         hql.toString(), new Object[] { brandCode, itemCode});
	     return result;	    	
	 }
	 
	 public ImMonthlyBalanceHead findById(String brandCode, String itemCode, String year, String month){
		 System.out.println("brandCode:"+brandCode);
		 System.out.println("year:"+year);
		 System.out.println("month:"+month);
		 System.out.println("itemCode:"+itemCode);

		 StringBuffer hql = new StringBuffer(
         "from ImMonthlyBalanceHead as model where 1=1");
		 		hql.append(" and model.id.brandCode = ?");
		 		hql.append(" and model.id.itemCode = ?");
		 		hql.append(" and model.id.year = ?");
		 		hql.append(" and model.id.month = ?");

     	List<ImMonthlyBalanceHead> result = getHibernateTemplate().find(
         hql.toString(), new String[] { brandCode, itemCode , year, month});
     
	     return  (result.size()>0? result.get(0):null);	    	
	 }
}
