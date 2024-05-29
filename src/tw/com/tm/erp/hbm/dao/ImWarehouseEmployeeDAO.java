package tw.com.tm.erp.hbm.dao;

import java.io.Serializable;
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

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.hbm.bean.BuPaymentTerm;
import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.service.ImReceiveAdjustmentService;
import tw.com.tm.erp.utils.DateUtils;

public class ImWarehouseEmployeeDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImWarehouseEmployeeDAO.class);
	
    public List<ImWarehouse> findByEmployeeCode(String employeeCode) {

	StringBuffer hql = new StringBuffer(
		"from ImWarehouse as model where 1=1 ");
	hql.append("and model.warehouseCode = some(");
	hql.append("select employee.id.warehouseCode ");
	hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
	hql.append("and employee.id.employeeCode = ? )");

	System.out.println(hql.toString());

	List imWarehouseEmployees = getHibernateTemplate().find(hql.toString(),
		(String) employeeCode);

	return imWarehouseEmployees;

    }
    
    /**
     * 依據品牌, 員工代碼撈出可選的倉庫
     * @param brandCode
     * @param employeeCode
     * @return
     */
    public List<ImWarehouse> findByEmployeeCode(String brandCode, String employeeCode ) {

    	StringBuffer hql = new StringBuffer(
    		"from ImWarehouse as model where 1=1 ");
    	hql.append("and model.brandCode = ? ");
    	hql.append("and model.warehouseCode = some(");
    	hql.append("select employee.id.warehouseCode ");
    	hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
    	hql.append("and employee.id.employeeCode = ? ) ");

    	log.info(hql.toString());

    	List imWarehouseEmployees = getHibernateTemplate().find(hql.toString(),
    			new String[] { brandCode, employeeCode });

    	return imWarehouseEmployees;

	}
    
    public List findWarehouseCodeByEmployeeCode(String warehouseCode,
	    String employeeCode) {

	StringBuffer hql = new StringBuffer("");

	hql.append("select employee.warehouseCode ");
	hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
	hql.append("and employee.employeeCode = ? ");
	hql.append("and employee.warehouseCode = ? )");
	System.out.println(hql.toString());

	List imWarehouseEmployees = getHibernateTemplate().find(hql.toString(),
		new String[] { employeeCode, warehouseCode });

	return imWarehouseEmployees;

    }

    public List<ImWarehouseEmployee> findByWarehouseCode(String warehouseCode) {
    log.info("參數houseCode:"+warehouseCode);
	StringBuffer hql = new StringBuffer(" from ImWarehouseEmployee as model");
	hql.append(" where model.id.warehouseCode = ? order by model.indexNo");
	List<ImWarehouseEmployee> imWarehouseEmployees = getHibernateTemplate()
		.find(hql.toString(), new String[] { warehouseCode });

	return imWarehouseEmployees;
    }
    
    /**
     * 依據品牌,員工代碼撈出可選的倉庫
     * @param brandCode
     * @param employeeCode
     * @return
     */
    public List<ImWarehouse> findByEmployeeCode(String brandCode, String employeeCode, String warehouseCodes ) {

    	StringBuffer hql = new StringBuffer(
    		"from ImWarehouse as model where 1=1 ");
    	hql.append("and model.brandCode = ? ");
    	hql.append("and model.warehouseCode = some(");
    	hql.append("select employee.id.warehouseCode ");
    	hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
    	hql.append("and employee.id.employeeCode = ? and employee.id.warehouseCode in ("+warehouseCodes+") ) ");

    	log.info(hql.toString());

    	List imWarehouseEmployees = getHibernateTemplate().find(hql.toString(),
    			new String[] { brandCode, employeeCode });

    	return imWarehouseEmployees;

	}
    
    public ImWarehouseEmployee findById(tw.com.tm.erp.hbm.bean.ImWarehouseEmployee id) {
		log.debug("getting BuPaymentTerm instance with id: " + id);
		try {
			ImWarehouseEmployee instance = (ImWarehouseEmployee) getHibernateTemplate()
					.get(ImWarehouseEmployee.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
    
    public ImWarehouseEmployee findByEmpCode(String employeeCode,String warehouseCode){
	      log.info("employeeCode:"+employeeCode+" "+"warehouseCode:"+warehouseCode);
	    /*
	      ImWarehouseEmployee emp = null;
	             emp = (ImWarehouseEmployee)this.findFirstByProperty(
  			    "ImWarehouseEmployee","and employeeCode = ? and warehouseId = ? ",
			    new Object[] { employeeCode, Long.parseLong(warehouseId) });
	       return emp;
    	
    	return (ImWarehouseEmployee)findFirstByProperty("ImWarehouseEmployee", "and employeeCode = ? ","and warehouseId = ?", new Object[]{employeeCode,warehouseId});
	    */
	      try {  
	    StringBuffer hql = new StringBuffer(" from ImWarehouseEmployee as model");
	  	hql.append(" where model.id.warehouseCode = ? and model.id.employeeCode = ? ");
	  	log.info("庫別人員hql:"+hql);
	  	List<ImWarehouseEmployee> result = getHibernateTemplate()
	  		.find(hql.toString(), new String[] { warehouseCode , employeeCode });
	  	return (result != null && result.size() > 0 ? result.get(0) : null);
	  	//return imWarehouseEmployees;
	 } catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
    }
    
    /**
	 * find page line 最後一筆 index
	 * 
	 * @param headId
	 * @return Long
	 */
	public Long findPageLineMaxIndex(final String warehouseId) {

		
			Long lineMaxIndex = 0L;
			
		  	List<ImWarehouseEmployee> result = getHibernateTemplate().executeFind(new HibernateCallback() {
		  	    public Object doInHibernate(Session session)
		  		    throws HibernateException, SQLException {
			  		StringBuffer hql = new StringBuffer("from ImWarehouseEmployee as model where 1=1 ");			  		
			  		if (warehouseId != null){
			  			hql.append(" and model.warehouseId = :warehouseId order by indexNo");			  		    
			  		    //query.setDouble("warehouseId", Double.parseDouble(warehouseId));
			  		}			  		
			  		Query query = session.createQuery(hql.toString());
			  		query.setLong("warehouseId", Long.parseLong(warehouseId));
			  		
			  		return query.list();
		  	    }
		  	});
		  	if (result != null && result.size() > 0) {
		  		lineMaxIndex = result.get(result.size() - 1).getIndexNo();	
		  	}
		  	
		  	return lineMaxIndex;
		
	}
	
	public List<Object[]> findViewByMap(HashMap findObjs) {
		log.info("findViewByMap...");
		final HashMap fos = findObjs;
		List<Object[]> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public List<BuFlightSchedule> doInHibernate(Session session) throws HibernateException, SQLException {
				
				
				String warehouseCode = fos.get("warehouseCode").toString();
				String beanName     = (String) fos.get("isSuperVisor");
				StringBuffer hql = new StringBuffer("");
				if ("ExcelExport".equalsIgnoreCase((String) fos.get("QueryType"))){
				 
					  
					hql.append("SELECT model.enable,model.id.employeeCode" +
								" from ImWarehouseEmployee as model where 1=1 and model.id.warehouseCode='"+warehouseCode+"'"
								);
				}
				
				System.out.println("---ImWarehouseEmployee.findViewByMap.QueryString:---" + hql.toString());
				Query query = session.createQuery(hql.toString());
				
				return query.list();
			}
		});

		return result;
	}
    
	//新增權限(henry)
	public List<ImWarehouseEmployee> findPageLine(String employeeCode, int startPage, int pageSize) {
		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		final String employee_Code = employeeCode;
		//final String brand_Code = brandCode;
		List<ImWarehouseEmployee> result = (List<ImWarehouseEmployee>)getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    	//String hql = "from BuEmployeeBrand M where M.empolyeeCode = ? order by M.brandCode";
			    	String hql = " from ImWarehouseEmployee where employeeCode = :employeeCode order by warehouseCode";
			    
					Query query = session.createQuery(hql);
					//if(startRecordIndexStar > 0) query.setFirstResult(startRecordIndexStar);
					//if(pSize > 0) query.setMaxResults(pSize);
					query.setString("employeeCode", employee_Code);
					//query.setString("brandCode", brand_Code);
					return query.list();
			    }
			}); 
		return result;
	}
	public Long findPageLineCount(String employeeCode){
		String hql = "select count(*) from ImWarehouseEmployee where employeeCode = ? order by warehouseCode";
		Object obj = getHibernateTemplate().find(hql, new Object[]{employeeCode});
		return obj == null ? 0L : (Long)(((List)obj).get(0));
	}
	public ImWarehouseEmployee findById(Long lineId) {
		log.debug("getting ImWarehouseEmployee instance with id: " + lineId);
		try {
			ImWarehouseEmployee instance = (ImWarehouseEmployee) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImWarehouseEmployee", lineId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	  public List<ImWarehouseEmployee> findByEmpCode(String employeeCode){
	    
	    /*
	      ImWarehouseEmployee emp = null;
	             emp = (ImWarehouseEmployee)this.findFirstByProperty(
  			    "ImWarehouseEmployee","and employeeCode = ? and warehouseId = ? ",
			    new Object[] { employeeCode, Long.parseLong(warehouseId) });
	       return emp;
    	
    	return (ImWarehouseEmployee)findFirstByProperty("ImWarehouseEmployee", "and employeeCode = ? ","and warehouseId = ?", new Object[]{employeeCode,warehouseId});
	    */
	      try {  
	    StringBuffer hql = new StringBuffer(" from ImWarehouseEmployee as model");
	  	hql.append(" where model.employeeCode = ? ");
	  	log.info("庫別人員hql:"+hql);
	  	List<ImWarehouseEmployee> imWarehouseEmployees = getHibernateTemplate()
	  		.find(hql.toString(), new String[] { employeeCode });

	  	//return (ImWarehouseEmployee)imWarehouseEmployees.get(0);  
	  	return imWarehouseEmployees;
	 } catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
    }
		public ImWarehouseEmployee findByempolyee(String employeeCode) {
			//log.debug("getting BuEmployeeBrand instance with id: " + employeeCode);
			try {
				ImWarehouseEmployee instance = (ImWarehouseEmployee) getHibernateTemplate().get(
						"tw.com.tm.erp.hbm.bean.ImWarehouseEmployee", employeeCode);
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}
		}
		
		public int deleteImWarehouseEmployee(String brandCode, String employeeCode){
			log.info("刪除buEmployeeDAO");
			String hql = "delete ImWarehouseEmployee where id.employeeCode= ? and id.warehouseCode in (select warehouseCode from ImWarehouse where brandCode = ? )";
			return getHibernateTemplate().bulkUpdate(hql, new Object[]{employeeCode, brandCode});
		}
		
		public ImWarehouseEmployee findItemByIdentification(Long warehouseId, Long indexNo) {
			StringBuffer hql = new StringBuffer(
					"from ImWarehouseEmployee as model where model.id.warehouseCode = ? and model.indexNo = ?");
			List<ImWarehouseEmployee> result = getHibernateTemplate().find(hql.toString(), new Object[] { warehouseId, indexNo });
			return (result != null && result.size() > 0 ? result.get(0) : null);
		}
}
