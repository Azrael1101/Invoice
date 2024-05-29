package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuEmployeeAward;
import tw.com.tm.erp.hbm.bean.BuEmployeeAwardCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategory;

public class BuEmployeeAwardDAO extends BaseDAO{
    private static final Log log = LogFactory.getLog(BuEmployeeAwardDAO.class);
    
    public static final String CATEGORY01 = "CATEGORY01"; // 大類
    public static final String CATEGORY02 = "CATEGORY02"; // 中類
    
    /**
     * 依brandCode, categoryType, categoryCode, enable 條件撈出
     * @param brandCode
     * @param categoryType
     * @param categoryCode
     * @param enable
     * @return
     */
    public BuEmployeeAwardCategory findOneCategoryCode(String brandCode, String categoryType, String categoryCode, String enable) {
    	return (BuEmployeeAwardCategory)findFirstByProperty("BuEmployeeAwardCategory", "and brandCode = ? and categoryType = ? and categoryCode = ? and enable = ?", 
    			new Object[]{brandCode, categoryType, categoryCode, enable} );
    }
    
    /**
     * 依據brandCode, categoryType, categoryCode, isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param categoryCode
     * @param enable
     * @return
     */
    public BuEmployeeAwardCategory findByCategoryCode(String brandCode, String categoryType, String categoryCode, String parentCategoryCode, String enable) {
    	return (BuEmployeeAwardCategory)findFirstByProperty("BuEmployeeAwardCategory", "and brandCode = ? and categoryType = ? and categoryCode = ? and parentCategoryCode = ? and enable = ?", 
    			new Object[]{brandCode, categoryType, categoryCode, parentCategoryCode, enable} );
    }
    
    public List findEMPAbyCondition(HashMap conditionMap) {
    	final String employeeCode = (String) conditionMap.get("employeeCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		final String enable = (String) conditionMap.get("enable");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from BuEmployeeAward as model ");
				hql.append("where 1=1 ");
				if(StringUtils.hasText(employeeCode))
					hql.append(" and model.employeeCode = :employeeCode ");
				if(StringUtils.hasText(dataDate))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				if(StringUtils.hasText(dataDateEnd))
					hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				if(StringUtils.hasText(enable))
					hql.append(" and model.enable = :enable ");
				Query query = session.createQuery(hql.toString());
				if(StringUtils.hasText(employeeCode))
					query.setString("employeeCode", employeeCode);
				if(StringUtils.hasText(dataDate))
					query.setString("dataDate", dataDate);
				if(StringUtils.hasText(dataDateEnd))
					query.setString("dataDateEnd", dataDateEnd);
				if(StringUtils.hasText(enable))
					query.setString("enable", enable);
				return query.list();
			}
		});
		return result;
	}
    
    public List findEMPACbyCondition(HashMap conditionMap) {
    	final String brandCode = (String) conditionMap.get("brandCode");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model from BuEmployeeAwardCategory as model ");
				hql.append("where 1=1 ");
				if(StringUtils.hasText(brandCode))
					hql.append(" and model.brandCode = :brandCode ");
				Query query = session.createQuery(hql.toString());
				if(StringUtils.hasText(brandCode))
					query.setString("brandCode", brandCode);
				return query.list();
			}
		});
		return result;
	}
    
    public List findEMPARbyCondition(HashMap conditionMap) {
    	
    	String employeeDepartment = (String)conditionMap.get("employeeDepartment");
    	
    	final StringBuffer sql = new StringBuffer("");
            
    	sql.append(" SELECT   AAA.EMPLOYEE_DEPARTMENT, AAA.EMPLOYEE_CODE, AAA.CHINESE_NAME, AAA.TOTAL, ")
    		.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_A),0,'A',1,'A', ")
    		.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_B),0,'B',1,'B', ")
			.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_C),0,'C',1,'C', ")
			.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_D),0,'D',1,'D', ")
			.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_E),0,'E',1,'E', ")
			.append(" DECODE (SIGN (AAA.PERCENTRANK - RANK_F),0, 'F',1, 'F','G')))))) AS LEVELRANK ")
			.append(" FROM   (  SELECT   employee_code, CHINESE_NAME, EMPLOYEE_DEPARTMENT, total, ")
			.append(" PERCENT_RANK () OVER (ORDER BY pointRank DESC) AS percentRank ")
			.append(" FROM   (  SELECT   EMPLOYEE_CODE, CHINESE_NAME, EMPLOYEE_DEPARTMENT, SUM (POINT) AS total, ")
			.append(" RANK () OVER (ORDER BY SUM (point) DESC) AS pointRank ")
			.append(" FROM   (SELECT   AA.EMPLOYEE_CODE, AA.CHINESE_NAME, AA.EMPLOYEE_DEPARTMENT, BB.POINT ")
			.append(" FROM   (SELECT   * FROM   (SELECT   employee_code, CHINESE_NAME, EMPLOYEE_DEPARTMENT ")
			.append(" FROM   BU_EMPLOYEE_WITH_ADDRESS_VIEW WHERE   LEAVE_DATE IS NULL) A, ")
			.append(" (SELECT   line_code, name FROM   BU_COMMON_PHRASE_LINE WHERE   head_code = 'EmployeeDepartment') C ")
			.append(" WHERE   A.EMPLOYEE_DEPARTMENT = C.LINE_CODE(+) AND A.EMPLOYEE_DEPARTMENT = "+employeeDepartment+") AA, ")
			.append(" (SELECT   employee_code, point FROM   BU_EMPLOYEE_AWARD WHERE   enable = 'Y') BB WHERE   AA.EMPLOYEE_CODE = BB.EMPLOYEE_CODE) ")
			.append(" GROUP BY   EMPLOYEE_CODE, CHINESE_NAME, EMPLOYEE_DEPARTMENT ORDER BY   SUM (POINT) DESC) ORDER BY   percentRank DESC) AAA, ")
			.append(" (SELECT   * FROM BU_EMPLOYEE_AWARD_RANK) BBB WHERE   AAA.EMPLOYEE_DEPARTMENT = BBB.EMPLOYEE_DEPARTMENT ")
			.append(" ORDER BY AAA.TOTAL DESC , AAA.EMPLOYEE_CODE ASC ");                        
    	//log.info("sql.toString() = " + sql.toString());
    	List result = null;
		result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql.toString());
				if( null != query ){
					return query.list();
				}
				else
					return null ;
			}
		});
		return result;
	}
    
	/**
	 * 依據EmployeeAward查出其可用點數
	 * 
	 * @return Double available
	 */
	public Double getAvailablePoint(BuEmployeeAward importBuEmployeeAward) {
		String employeeCode = importBuEmployeeAward.getEmployeeCode();
		StringBuffer hql = new StringBuffer("select sum(model.point) from BuEmployeeAward as model");
		hql.append(" where model.employeeCode = ? and model.enable = ?");
		Object[] obj = new Object[] { employeeCode, "Y" };
		hql.append(" group by model.employeeCode");
		List result = getHibernateTemplate().find(hql.toString(), obj);
		return (result.size() > 0 ? (Double) result.get(0) : 0D);
	}
}
