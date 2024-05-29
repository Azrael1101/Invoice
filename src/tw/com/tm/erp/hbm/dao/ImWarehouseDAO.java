package tw.com.tm.erp.hbm.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
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
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;

public class ImWarehouseDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImWarehouseDAO.class);
    public List<ImWarehouse> find(HashMap findObjs) {

	final HashMap fos = findObjs;

	List<ImWarehouse> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer(
								"from ImWarehouse as model where 1=1 ");
						if (StringUtils.hasText((String) fos
								.get("warehouseCode")))
							hql
									.append(" and model.warehouseCode = :warehouseCode ");

						if (StringUtils.hasText("brandCode"))
							hql.append(" and model.brandCode = :brandCode ");

						if (StringUtils.hasText((String) fos
								.get("warehouseName")))
							hql
									.append(" and model.warehouseName = :warehouseName ");

						if (fos.get("locationId") != null
								&& (Long) fos.get("locationId") != 0)
							hql.append(" and model.locationId = :locationId ");

						if (fos.get("warehouseTypeId") != null
								&& (Long) fos.get("warehouseTypeId") != 0)
							hql
									.append(" and model.warehouseTypeId = :warehouseTypeId ");

						if (StringUtils.hasText((String) fos.get("storage")))
							hql.append(" and model.storage = :storage ");

						if (StringUtils
								.hasText((String) fos.get("storageArea")))
							hql
									.append(" and model.storageArea = :storageArea ");

						if (StringUtils.hasText((String) fos.get("storageBin")))
							hql.append(" and model.storageBin = :storageBin ");

						if (StringUtils.hasText((String) fos
								.get("categoryCode")))
							hql
									.append(" and model.categoryCode = :categoryCode ");

						if (StringUtils
								.hasText((String) fos.get("taxTypeCode")))
							hql
									.append(" and model.taxTypeCode = :taxTypeCode ");

						if (StringUtils.hasText((String) fos.get("enable")))
							hql.append(" and model.enable = :enable ");

						if (StringUtils.hasText((String) fos
								.get("warehouseManager")))
							hql
									.append(" and model.warehouseManager = :warehouseManager ");

						hql.append(" order by warehouseCode asc ");
						System.out.println(hql.toString());
						Query query = session.createQuery(hql.toString());
						query.setFirstResult(0);
						query.setMaxResults(SystemConfig.SEARCH_WAREHOUSE_MAX_COUNT);

						if (StringUtils.hasText((String) fos
								.get("warehouseCode")))
							query.setString("warehouseCode", (String) fos
									.get("warehouseCode"));

						if (StringUtils.hasText("brandCode"))
							query.setString("brandCode", (String) fos
									.get("brandCode"));

						if (StringUtils.hasText((String) fos
								.get("warehouseName")))
							query.setString("warehouseName", (String) fos
									.get("warehouseName"));

						if (fos.get("locationId") != null
								&& (Long) fos.get("locationId") != 0)
							query.setLong("locationId", (Long) fos
									.get("locationId"));

						if (fos.get("warehouseTypeId") != null
								&& (Long) fos.get("warehouseTypeId") != 0)
							query.setLong("warehouseTypeId", (Long) fos
									.get("warehouseTypeId"));

						if (StringUtils.hasText((String) fos.get("storage")))
							query.setString("storage", (String) fos
									.get("storage"));

						if (StringUtils
								.hasText((String) fos.get("storageArea")))
							query.setString("storageArea", (String) fos
									.get("storageArea"));

						if (StringUtils.hasText((String) fos.get("storageBin")))
							query.setString("storageBin", (String) fos
									.get("storageBin"));

						if (StringUtils.hasText((String) fos
								.get("categoryCode")))
							query.setString("categoryCode", (String) fos
									.get("categoryCode"));

						if (StringUtils
								.hasText((String) fos.get("taxTypeCode")))
							query.setString("taxTypeCode", (String) fos
									.get("taxTypeCode"));

						if (StringUtils.hasText((String) fos.get("enable")))
							query.setString("enable", (String) fos
									.get("enable"));

						if (StringUtils.hasText((String) fos
								.get("warehouseManager")))
							query.setString("warehouseManager", (String) fos
									.get("warehouseManager"));

						return query.list();
					}
		});

	return re;

    }

    public List<ImWarehouse> findWarehouseBelong2Employee(
	    final String employeeCode, HashMap findObjs) {

	final HashMap fos = findObjs;

	List<ImWarehouse> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from ImWarehouse as model where 1=1 ");

			if (StringUtils.hasText("brandCode"))
			    hql.append(" and model.brandCode = :brandCode ");

			if (StringUtils.hasText((String) fos.get("warehouseName")))
			    hql.append(" and model.warehouseName = :warehouseName ");

			if ((Long) fos.get("locationId") != 0 && (Long) fos.get("locationId") != null)
			    hql.append(" and model.locationId = :locationId ");

			if ((Long) fos.get("warehouseTypeId") != 0
				&& (Long) fos.get("warehouseTypeId") != null)
			    hql.append(" and model.warehouseTypeId = :warehouseTypeId ");

			if (StringUtils.hasText((String) fos.get("storage")))
			    hql.append(" and model.storage = :storage ");

			if (StringUtils.hasText((String) fos.get("storageArea")))
			    hql.append(" and model.storageArea = :storageArea ");

			if (StringUtils.hasText((String) fos.get("storageBin")))
			    hql.append(" and model.storageBin = :storageBin ");

			if (StringUtils.hasText((String) fos.get("categoryCode")))
			    hql.append(" and model.categoryCode = :categoryCode ");

			if (StringUtils.hasText((String) fos.get("taxTypeCode")))
			    hql.append(" and model.taxTypeCode = :taxTypeCode ");

			if (StringUtils.hasText((String) fos.get("enable")))
			    hql.append(" and model.enable = :enable ");

			if (StringUtils.hasText((String) fos.get("warehouseManager")))
			    hql.append(" and model.warehouseManager = :warehouseManager ");

			hql.append(" and model.warehouseCode = some( ");
			hql.append("select employee.id.warehouseCode ");
			hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
			hql.append("and employee.id.employeeCode = :employeeCode ");
			if (StringUtils.hasText((String) fos.get("warehouseCode")))
			    hql.append("and employee.id.warehouseCode = :warehouseCode ");
			hql.append(")");
			hql.append(" order by warehouseCode asc ");
			System.out.println(hql.toString());
			Query query = session.createQuery(hql.toString());

			// if(
			// StringUtils.hasText((String)fos.get("warehouseCode"))
			// )
			// query.setString("warehouseCode",
			// (String)fos.get("warehouseCode") );

			if (StringUtils.hasText("brandCode"))
			    query.setString("brandCode", (String) fos.get("brandCode"));

			if (StringUtils.hasText((String) fos.get("warehouseName")))
			    query.setString("warehouseName", (String) fos.get("warehouseName"));

			if ((Long) fos.get("locationId") != 0 && (Long) fos.get("locationId") != null)
			    query.setLong("locationId", (Long) fos.get("locationId"));

			if ((Long) fos.get("warehouseTypeId") != 0 && (Long) fos.get("warehouseTypeId") != null)
			    query.setLong("warehouseTypeId", (Long) fos.get("warehouseTypeId"));

			if (StringUtils.hasText((String) fos.get("storage")))
			    query.setString("storage", (String) fos.get("storage"));

			if (StringUtils.hasText((String) fos.get("storageArea")))
			    query.setString("storageArea", (String) fos.get("storageArea"));

			if (StringUtils.hasText((String) fos.get("storageBin")))
			    query.setString("storageBin", (String) fos.get("storageBin"));

			if (StringUtils.hasText((String) fos.get("categoryCode")))
			    query.setString("categoryCode", (String) fos.get("categoryCode"));

			if (StringUtils.hasText((String) fos.get("taxTypeCode")))
			    query.setString("taxTypeCode", (String) fos.get("taxTypeCode"));

			if (StringUtils.hasText((String) fos.get("enable")))
			    query.setString("enable", (String) fos.get("enable"));

			if (StringUtils.hasText((String) fos.get("warehouseManager")))
			    query.setString("warehouseManager", (String) fos.get("warehouseManager"));

			query.setString("employeeCode", employeeCode);
			if (StringUtils.hasText((String) fos.get("warehouseCode")))
			    query.setString("warehouseCode", (String) fos.get("warehouseCode"));

			return query.list();
		    }
		});

	return re;

    }

    public List<ImWarehouse> findMovementWarehouse(String type, HashMap findObjs) {
    	
    log.info("1ddddd-----findMovementWarehouse  start");
    List<ImWarehouse> result = new ArrayList(0);
    Object[] findArray = null;
    String brandCode     = (String) findObjs.get("brandCode");
    String warehouseCode = (String) findObjs.get("warehouseCode");
    String orderTypeKey  = brandCode + (String) findObjs.get("orderTypeCode");
    String employeeCode  = (String) findObjs.get("employeeCode");
    String itemCategoryMode = (String) findObjs.get("itemCategoryMode");
    String enable = (String)findObjs.get("enable");//jason
    StringBuffer hql = new StringBuffer("from ImWarehouse as model where 1=1 ");
	hql.append(" and model.brandCode = ? ");
	hql.append(" and model.warehouseCode = some( ");
	if (StringUtils.hasText((String) findObjs.get("orderTypeCode"))){
		BuCommonPhraseHead buCommonPhraseHead = new BuCommonPhraseHead();
		buCommonPhraseHead.setHeadCode("ImMovementOrderType");
		System.out.println("type:"+type+"/ itemCategoryMode:"+itemCategoryMode+
				"/ employeeCode:"+employeeCode+"/ orderTypeKey:"+orderTypeKey+"/ warehouseCode:"+warehouseCode);
		if("delivery".equals(type)){
			System.out.println("check employee");
			hql.append("select employee.id.warehouseCode ");
			hql.append("from ImWarehouseEmployee as employee, BuCommonPhraseLine as phrase where 1=1 ");
			if(StringUtils.hasText(employeeCode))
				hql.append("and employee.id.employeeCode = ? ");
			
			hql.append("and phrase.id.buCommonPhraseHead = ?" );
			hql.append("and phrase.id.lineCode = ?" );	
			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
			    hql.append("and employee.id.warehouseCode = ? ");
			hql.append("and phrase.attribute1 like '%'||warehouse_Code||'%'");
			hql.append(")");
			hql.append(" order by warehouseCode asc ");
			System.out.println("2ddddd-----: "+hql.toString());
			if (StringUtils.hasText(employeeCode)){
				if (StringUtils.hasText(warehouseCode))
					findArray = new Object[]{brandCode, employeeCode, buCommonPhraseHead, orderTypeKey,  warehouseCode};
				else
					findArray = new Object[]{brandCode, employeeCode, buCommonPhraseHead, orderTypeKey };
			}else{
				if (StringUtils.hasText(warehouseCode))
					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,  warehouseCode};
				else
					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey };
			}
		}else{
			System.out.println("don't check employee");
			hql.append("select warehouse.id.warehouseCode ");
			hql.append("from ImWarehouse as warehouse, BuCommonPhraseLine as phrase where 1=1 ");
			hql.append("and phrase.id.buCommonPhraseHead = ?" );
			hql.append("and phrase.id.lineCode = ?" );	
			hql.append("and warehouse.id.brandCode = ?" );
			//hql.append("and warehouse.id.enable = ?");
			hql.append("and phrase.attribute2 like '%'||warehouse_Code||'%'");
			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
			    hql.append("and warehouse.id.warehouseCode = ? ");
		
			hql.append(")");
			hql.append(" order by warehouseCode asc ");
			System.out.println("1ddddd-----hql:  "+hql.toString());

			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,warehouseCode};
			else
				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode};
			
		}

		result = getHibernateTemplate().find(hql.toString(),findArray);

	}else{
		hql.append("select employee.id.warehouseCode ");
		hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
		hql.append("and employee.id.employeeCode = :employeeCode ");
		if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
		    hql.append("and employee.id.warehouseCode = :warehouseCode ");
    }
    System.out.println("delvieryWarehouse.size"+result.size());
	return result;
	

    }
    
    public List<ImWarehouse> findMovementWarehouseByEmployee(String type, HashMap findObjs) {//by jason
    	
        log.info("1ddddd-----findMovementWarehouse  start");
        List<ImWarehouse> result = new ArrayList(0);
        Object[] findArray = null;
        String brandCode     = (String) findObjs.get("brandCode");
        String warehouseCode = (String) findObjs.get("warehouseCode");
        String orderTypeKey  = brandCode + (String) findObjs.get("orderTypeCode");
        String employeeCode  = (String) findObjs.get("employeeCode");
        String itemCategoryMode = (String) findObjs.get("itemCategoryMode");
        String enable = (String)findObjs.get("enable");//jason
        //log.info("1ddddd-----orderTypeKey:  "+orderTypeKey);
        //log.info("1ddddd-----enable:  "+enable);  
        StringBuffer hql = new StringBuffer("from ImWarehouse as model where 1=1 ");
    	hql.append(" and model.brandCode = ? ");
    	//log.info("1ddddd-----findMovementWarehouse  start   1");
    	hql.append(" and model.warehouseCode = some( ");
    	if (StringUtils.hasText((String) findObjs.get("orderTypeCode"))){
    		BuCommonPhraseHead buCommonPhraseHead = new BuCommonPhraseHead();
    		buCommonPhraseHead.setHeadCode("ImMovementOrderType");
    		System.out.println("type:"+type+"/ itemCategoryMode:"+itemCategoryMode+
    				"/ employeeCode:"+employeeCode+"/ orderTypeKey:"+orderTypeKey+"/ warehouseCode:"+warehouseCode);
    		if("delivery".equals(type)){
    			System.out.println("check employee");
    			hql.append("select employee.id.warehouseCode ");
    			hql.append("from ImWarehouseEmployee as employee, BuCommonPhraseLine as phrase where 1=1 ");
    			if(StringUtils.hasText(employeeCode))
    				hql.append("and employee.id.employeeCode = ? ");
    			
    			hql.append("and employee.enable = ?" );
    			hql.append("and phrase.id.buCommonPhraseHead = ?" );
    			hql.append("and phrase.id.lineCode = ?" );
	
    			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
    			    hql.append("and employee.id.warehouseCode = ? ");
    			hql.append("and phrase.attribute1 like '%'||warehouse_Code||'%'");
    			hql.append(")");
    			hql.append(" order by warehouseCode asc ");
    			System.out.println("2ddddd-----: "+hql.toString());
    			if (StringUtils.hasText(employeeCode)){
    				if (StringUtils.hasText(warehouseCode))
    					findArray = new Object[]{brandCode, employeeCode, "Y",buCommonPhraseHead, orderTypeKey,  warehouseCode};
    				else
    					findArray = new Object[]{brandCode, employeeCode,"Y", buCommonPhraseHead, orderTypeKey };
    			}else{
    				if (StringUtils.hasText(warehouseCode))
    					findArray = new Object[]{brandCode,"Y", buCommonPhraseHead, orderTypeKey,  warehouseCode};
    				else
    					findArray = new Object[]{brandCode,"Y", buCommonPhraseHead, orderTypeKey };
    			}
    			//log.info("1ddddd-----findMovementWarehouse  start   2");
    		}else{
    			System.out.println("don't check employee");
    			hql.append("select warehouse.id.warehouseCode ");
    			hql.append("from ImWarehouse as warehouse, BuCommonPhraseLine as phrase where 1=1 ");
    			hql.append("and phrase.id.buCommonPhraseHead = ?" );
    			hql.append("and phrase.id.lineCode = ?" );	
    			hql.append("and warehouse.id.brandCode = ?" );
    			//hql.append("and warehouse.id.enable = ?" );
    			hql.append("and phrase.attribute2 like '%'||warehouse_Code||'%'");
    			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
    			    hql.append("and warehouse.id.warehouseCode = ? ");
    			
    			//------
    			hql.append("and warehouse.id.warehouseCode = some(");
    			hql.append("select emp.id.warehouseCode ");
    			hql.append("from ImWarehouseEmployee as emp, BuCommonPhraseLine as phr where 1=1 ");

    			hql.append("and phr.id.buCommonPhraseHead = ?" );
    			hql.append("and phr.id.lineCode = ?" );	
    			if(StringUtils.hasText(employeeCode))
    				hql.append("and emp.id.employeeCode = ? ");//by jason
    			if(StringUtils.hasText(enable))//by jason
    				hql.append("and emp.enable = ? ");//by jason
    			hql.append("and phr.attribute2 like '%'||warehouse_Code||'%'");	
    			hql.append(")");		
    			//------
    			
    			hql.append(")");
    			hql.append(" order by warehouseCode asc ");
    			
    			//log.info("1ddddd-----findMovementWarehouse  start   3");
    			System.out.println("1ddddd-----hql:  "+hql.toString());
    			//--------
    			if (StringUtils.hasText((String) findObjs.get("warehouseCode"))){
    				if(StringUtils.hasText((String) findObjs.get("employeeCode"))){
    					if(StringUtils.hasText((String) findObjs.get("enable"))){
    						findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,warehouseCode,
    								buCommonPhraseHead, orderTypeKey,employeeCode,enable};
    					}						
    					else{
    						findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,
    								warehouseCode, buCommonPhraseHead, orderTypeKey,employeeCode};
    					}						
    				}else if(StringUtils.hasText((String) findObjs.get("enable")))
    					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,warehouseCode, buCommonPhraseHead, orderTypeKey,enable};
    				else
    					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,warehouseCode, buCommonPhraseHead, orderTypeKey};
    			}
    			else if(StringUtils.hasText((String) findObjs.get("employeeCode"))){
    				if(StringUtils.hasText((String) findObjs.get("enable")))
    					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode, buCommonPhraseHead, orderTypeKey,employeeCode,enable};
    				else
    					findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode, buCommonPhraseHead, orderTypeKey,employeeCode};
    			}
    			else if(StringUtils.hasText((String) findObjs.get("enable")))
    				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode, buCommonPhraseHead, orderTypeKey,enable};
    			else
    				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode, buCommonPhraseHead, orderTypeKey};
    			//----------
    			/*
    			if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
    				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode,warehouseCode};
    			else
    				findArray = new Object[]{brandCode, buCommonPhraseHead, orderTypeKey,brandCode};
    			*/
    		}
    		//log.info("1ddddd-----findMovementWarehouse  hql   **** "+hql.toString());
    		//log.info("1ddddd-----findMovementWarehouse  findArray   **** "+findArray.toString());
    		//log.info("1ddddd-----findMovementWarehouse  start   4");
    		result = getHibernateTemplate().find(hql.toString(),findArray);
    		
    		//log.info("1ddddd-----findMovementWarehouse  start   ****");

    	}else{
    		hql.append("select employee.id.warehouseCode ");
    		hql.append("from ImWarehouseEmployee as employee  where 1=1 ");
    		hql.append("and employee.id.employeeCode = :employeeCode ");
    		if (StringUtils.hasText((String) findObjs.get("warehouseCode")))
    		    hql.append("and employee.id.warehouseCode = :warehouseCode ");
        }
        System.out.println("delvieryWarehouse.size"+result.size());
    	return result;
    	

        }
    
    /**
     * 依據品牌代號、工號、啟用狀態等條件查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<ImWarehouse> getWarehouseByWarehouseEmployee(String brandCode, String employeeCode, String isEnable) {
	
	StringBuffer hql = new StringBuffer("select model from ImWarehouse as model, ImWarehouseEmployee as model2, BuEmployeeWithAddressView as model3");
	hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode");
	hql.append(" and model.brandCode = ?");
	hql.append(" and model2.id.employeeCode = ?");
	if (StringUtils.hasText(isEnable)){
            hql.append(" and model.enable = ?");
	}
	hql.append(" order by model.warehouseCode");
	
	Object[] parameterArray = null;
	if (StringUtils.hasText(isEnable)){
	    parameterArray = new Object[] {brandCode, employeeCode, isEnable};
	}else{
	    parameterArray = new Object[] {brandCode, employeeCode};
	}
	
	return getHibernateTemplate().find(hql.toString(), parameterArray);
    }
    
   
    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢
     * 
     * @param brandCode
     * @param warehouseCode
     * @param isEnable
     * @return ImWarehouse
     */
    public ImWarehouse findByBrandCodeAndWarehouseCode(String brandCode, String warehouseCode, String isEnable) {
		StringBuffer hql = new StringBuffer("select model from ImWarehouse as model");
		hql.append(" where model.brandCode = ?");
		hql.append(" and model.warehouseCode = ?");
		if (StringUtils.hasText(isEnable)){
            hql.append(" and model.enable = ?");
		}
		hql.append(" order by model.warehouseCode");
		
		Object[] parameterArray = null;
		if (StringUtils.hasText(isEnable)){
		    parameterArray = new Object[] {brandCode, warehouseCode, isEnable};
		}else{
		    parameterArray = new Object[] {brandCode, warehouseCode};
		}	
		List<ImWarehouse> result = getHibernateTemplate().find(hql.toString(), parameterArray);
		
		return (result != null && result.size() > 0 ? result.get(0) : null);
    }
    
    /**
     * 依據品牌代號、庫號、啟用狀態等條件查詢
     * 
     * @param brandCode
     * @param warehouseCode
     * @param isEnable
     * @return ImWarehouse
     */
    public List<ImWarehouse> findByBrandCode(String brandCode, String isEnable) {
	
	StringBuffer hql = new StringBuffer("select model from ImWarehouse as model");
	hql.append(" where model.brandCode = ?");
	if (StringUtils.hasText(isEnable)){
            hql.append(" and model.enable = ?");
	}
	log.info("ssssssql:"+brandCode+" "+isEnable);
	Object[] parameterArray = null;
	if (StringUtils.hasText(isEnable)){
	    parameterArray = new Object[] {brandCode, isEnable};
	}else{
	    parameterArray = new Object[] {brandCode};
	}	
	List<ImWarehouse> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	log.info("ssssssql:"+brandCode+" "+isEnable+" res.size:"+result.size());
	return result;
    }
    
    /**
	 * 依據品牌代號查詢庫別 add by Weichun 2010.06.10
	 * 
	 * @param brandCode
	 * @return ImWarehouse
	 */
	public List<ImWarehouse> findByBrandCodeOrder(String brandCode, String isEnable) {

		StringBuffer hql = new StringBuffer("select model from ImWarehouse as model");
		hql.append(" where model.brandCode = ?");
		if (StringUtils.hasText(isEnable)) {
			hql.append(" and model.enable = ?");
		}
		Object[] parameterArray = null;
		parameterArray = new Object[] { brandCode, isEnable };
		hql.append(" order by model.warehouseCode");

		List<ImWarehouse> result = getHibernateTemplate().find(hql.toString(), parameterArray);

		return result;
	}
	
	/**
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return
	 */
	public BuEmployeeWithAddressView findbyBrandCodeAndEmployeeCode(String brandCode, String employeeCode) 
	{
	    StringBuffer hql = new StringBuffer("select model from BuEmployeeWithAddressView as model, BuEmployeeBrand as model2");
	    hql.append(" where model.employeeCode = model2.id.employeeCode");
	    hql.append(" and model2.id.brandCode = ? ");
	    hql.append(" and model.employeeCode = ? ");
		
	    Object[] parameterArray = new Object[] {brandCode, employeeCode};
	    List<BuEmployeeWithAddressView> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	    return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	
	public ImWarehouse findByWarehouseId(String id) {
		System.out.println("getting ImWarehouse instance with id: " + id);
		Long warehouseId = Long.parseLong(id);
		try {
			ImWarehouse instance = (ImWarehouse) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.ImWarehouse", warehouseId);
			return instance;
		} catch (RuntimeException re) {
			System.out.println("get failed:"+ re);
			throw re;
		}
	}
	
	public ImWarehouse findByIdL(Long id) {
		log.debug("getting ImMovementHead instance with id: " + id);
		try {
			ImWarehouse instance = (ImWarehouse) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImWarehouse", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public ImWarehouse findById(String warehouseCode) {
		log.info("找店別測試1:"+warehouseCode);
		try {
			String queryString = "from ImWarehouse as model where model.warehouseCode = ?";
			List<ImWarehouse> houses = 	getHibernateTemplate().find(queryString, warehouseCode);	
			//return (ImWarehouse)houses.get(0);
			return (houses != null && houses.size() > 0 ? houses.get(0) : null);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	/**
	 * 查詢庫別
	 * @param brandCode 品牌
	 * @param storage 關別
	 * @param warehouseCode 庫別
	 * @return
	 */
	public List<ImWarehouse> findImWarehouse(String brandCode, String storage, String warehouseCode){
		Object[] parameterArray = null;
		StringBuffer hql = new StringBuffer("select iw from ImWarehouse as iw");
		hql.append(" where iw.brandCode = ? ");
		parameterArray = new Object[] {brandCode};
		//依關別查庫別
		if(!"".equals(storage)){
		    hql.append(" and iw.customsWarehouseCode = ? ");
		    parameterArray = new Object[] {brandCode, storage};
		}
		//單一庫別
		if(!"".equals(warehouseCode)){
			hql.append(" and iw.warehouseCode = ? ");
			parameterArray = new Object[] {brandCode, warehouseCode};
		}
	    
	    List<ImWarehouse> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	    return (result != null && result.size() > 0 ? result : null);
	}
	
	public Object findByPrimaryKey(Class findClass, String warehouseCode) {
		
		
		return this.findById(warehouseCode);
	}
}
