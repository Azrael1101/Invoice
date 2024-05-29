package tw.com.tm.erp.hbm.dao;

import java.util.List;

public class PoBudgetHeadDAO extends BaseDAO {
	
	
	/**
	 * get item buget amount
	 * BRAND_CODE , BUDGET_YEAR , BUDGET_CHECK_TYPE , MONTH , ITEM_CODE
	 * @param brandCode
	 * @param bugetYear
	 * @param budgetCheckType
	 * @param month
	 * @param itmeCode
	 * @return
	 */
	public Double getItemBudgetAmount(String brandCode,String bugetYear,String budgetCheckType,Long month,String itemCode){
		Double re = new Double(0);
		if( null == budgetCheckType)
			budgetCheckType = "M" ;
		StringBuffer hql = new StringBuffer();
		hql.append("select sum( bugL.budgetAmount ) as total " ); 
		hql.append("FROM PoBudgetHead bugH , PoBudgetLine bugL " ) ;
		hql.append("where bugH.brandCode = ? and bugH.budgetYear = ? and bugH.budgetCheckType = ? and bugL.month <= ? and bugL.itemCode = ? ");
		List result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, bugetYear, budgetCheckType, month, itemCode });
		if ((null != result) && (result.size() > 0) && (null != result.get(0)) ) {
			re = (Double) result.get(0);
		}		
		return re ;
	}

}
