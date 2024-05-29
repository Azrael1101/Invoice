package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImItemPriceOnHandView;

public class ImItemPriceOnHandViewDAO extends BaseDAO {
    private static final Log log = LogFactory
	    .getLog(ImItemPriceOnHandViewDAO.class);

    /**
     * 依據品牌代號、品號、價格類別等條件查詢
     * 
     * @param brandCode
     * @param itemCode
     * @param priceType
     * @return ImItemPriceOnHandView
     */
    public ImItemPriceOnHandView getItemByCondition(HashMap conditionMap) {

	String brandCode = (String) conditionMap.get("brandCode");
	String warehouseEmployee = (String) conditionMap.get("warehouseEmployee");
	String itemCode = (String) conditionMap.get("itemCode");
	String priceType = (String) conditionMap.get("priceType");
	String warehouseManager = (String) conditionMap.get("warehouseManager");

	StringBuffer hql = new StringBuffer("select model from ImItemPriceOnHandView as model, ImWarehouseEmployee as model2, BuEmployeeWithAddressView as model3");
	hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode");
	hql.append(" and model.brandCode = ?");
	hql.append(" and model.itemCode = ?");
	hql.append(" and model.typeCode = ?");
	hql.append(" and model2.id.employeeCode = ?");
	
	if (StringUtils.hasText(warehouseManager)){
	    hql.append(" and model.warehouseManager = ?");
	}
	
	Object[] parameterArray = null;

	if (StringUtils.hasText(warehouseManager)){
	    parameterArray = new Object[] {brandCode, itemCode, priceType, warehouseEmployee, warehouseManager};
	}else{
	    parameterArray = new Object[] {brandCode, itemCode, priceType, warehouseEmployee};
	}
	
	List<ImItemPriceOnHandView> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	return (result != null && result.size() > 0 ? result.get(0) : null);
    }

    /**
     * 依據品牌代號、庫別代號、價格類別等條件查詢
     * 
     * @param conditionMap
     * @return ImItemPriceOnHandView
     */
    public ImItemPriceOnHandView getWarehouseByCondition(HashMap conditionMap) {

	String brandCode = (String) conditionMap.get("brandCode");
	String warehouseEmployee = (String) conditionMap.get("warehouseEmployee");
	String warehouseCode = (String) conditionMap.get("warehouseCode");
	String priceType = (String) conditionMap.get("priceType");
	String warehouseManager = (String) conditionMap.get("warehouseManager");

	StringBuffer hql = new StringBuffer("select model from ImItemPriceOnHandView as model, ImWarehouseEmployee as model2, BuEmployeeWithAddressView as model3");
	hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode");
	hql.append(" and model.brandCode = ?");
	hql.append(" and model.warehouseCode = ?");
	hql.append(" and model.typeCode = ?");
	hql.append(" and model2.id.employeeCode = ?");
	
	if (StringUtils.hasText(warehouseManager)){
	    hql.append(" and model.warehouseManager = ?");
	}
	
	Object[] parameterArray = null;

	if (StringUtils.hasText(warehouseManager)){
	    parameterArray = new Object[] {brandCode, warehouseCode, priceType, warehouseEmployee, warehouseManager};
	}else{
	    parameterArray = new Object[] {brandCode, warehouseCode, priceType, warehouseEmployee};
	}
	
	List<ImItemPriceOnHandView> result = getHibernateTemplate().find(hql.toString(), parameterArray);
	return (result != null && result.size() > 0 ? result.get(0) : null);

    }

    /**
     * 依據品牌代號、品號、庫別代號、價格類別等條件查詢，回傳品號價格、庫存量等
     * 
     * @param conditionMap
     * @return Object[]
     */
    public Object[] getItemPriceOnHand(HashMap conditionMap) {

	String brandCode = (String) conditionMap.get("brandCode");
	String itemCode = (String) conditionMap.get("itemCode");
	String warehouseCode = (String) conditionMap.get("warehouseCode");
	String priceType = (String) conditionMap.get("priceType");
	String warehouseEmployee = (String) conditionMap.get("warehouseEmployee");
	String warehouseManager = (String) conditionMap.get("warehouseManager");

	StringBuffer hql = new StringBuffer(
		"select model.brandCode, model.itemCode, model.warehouseCode, " +
		"model.typeCode, model.isTax, model.unitPrice, sum(model.currentOnHandQty) " +
		"from ImItemPriceOnHandView as model, ImWarehouseEmployee as model2, BuEmployeeWithAddressView as model3");
	hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode");
	hql.append(" and model.brandCode = ?");
	hql.append(" and model.itemCode = ?");
	hql.append(" and model.warehouseCode = ?");
	hql.append(" and model.typeCode = ?");
	hql.append(" and model2.id.employeeCode = ?");
	hql.append(" and model.warehouseManager = ?");
	hql
		.append(" group by model.brandCode, model.itemCode, model.warehouseCode, "
			+ "model.typeCode, model.isTax, model.unitPrice");

	List result = getHibernateTemplate().find(hql.toString(),
		new Object[] { brandCode, itemCode, warehouseCode, priceType, warehouseEmployee, warehouseManager});

	return (result != null && result.size() > 0 ? (Object[]) result.get(0) : null);
    }

    /**
     * 依據商品價格及庫存查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List findItemPriceOnHandList(HashMap conditionMap) {
	
	final String brandCode = (String)conditionMap.get("brandCode");
	final String employeeCode = (String)conditionMap.get("employeeCode");
	final String priceType = (String)conditionMap.get("priceType");
	final String warehouseManager = (String)conditionMap.get("warehouseManager");	
	final String itemCode_Start = (String)conditionMap.get("itemCode_Start");
	final String itemCode_End = (String)conditionMap.get("itemCode_End");         
	final String itemName = (String)conditionMap.get("itemName");
	final String unitPrice_Start = (String)conditionMap.get("unitPrice_Start");   
	final String unitPrice_End = (String)conditionMap.get("unitPrice_End");
	final String warehouseCode_Start = (String)conditionMap.get("warehouseCode_Start");
	final String warehouseCode_End = (String)conditionMap.get("warehouseCode_End");
	final String lotNo = (String)conditionMap.get("lotNo");
	final String currentOnHandQty_Start = (String)conditionMap.get("currentOnHandQty_Start"); 
	final String currentOnHandQty_End = (String)conditionMap.get("currentOnHandQty_End");
	final String category1 = (String)conditionMap.get("category1"); 
	final String category2 = (String)conditionMap.get("category2"); 
	final String category3 = (String)conditionMap.get("category3"); 
	
	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"select model.itemCode, model.itemCName, model.unitPrice, model.warehouseCode, model.warehouseName, " +
				"model.lotNo, model.currentOnHandQty, model3.chineseName from ImItemPriceOnHandView as model, ImWarehouseEmployee as model2" +
				", BuEmployeeWithAddressView as model3, ImItem as model4");
			hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode and model.itemCode = model4.itemCode");
			hql.append(" and model.brandCode = :brandCode");
			hql.append(" and model.typeCode = :typeCode");
			hql.append(" and model2.id.employeeCode = :employeeCode");
			
			if (StringUtils.hasText(warehouseManager))
			    hql.append(" and model.warehouseManager = :warehouseManager");

			if (StringUtils.hasText(itemCode_Start))
			    hql.append(" and model.itemCode >= :itemCode_Start");

			if (StringUtils.hasText(itemCode_End))
			    hql.append(" and model.itemCode <= :itemCode_End");

			if (StringUtils.hasText(itemName))
			    hql.append(" and model.itemCName like :itemName");

			if (StringUtils.hasText(unitPrice_Start))
			    hql.append(" and model.unitPrice >= :unitPrice_Start");

			if (StringUtils.hasText(unitPrice_End))
			    hql.append(" and model.unitPrice <= :unitPrice_End");

			if (StringUtils.hasText(warehouseCode_Start))
			    hql.append(" and model.warehouseCode >= :warehouseCode_Start");

			if (StringUtils.hasText(warehouseCode_End))
			    hql.append(" and model.warehouseCode <= :warehouseCode_End");
			
			if (StringUtils.hasText(lotNo))
			    hql.append(" and model.lotNo = :lotNo");

			if (StringUtils.hasText(currentOnHandQty_Start))
			    hql.append(" and model.currentOnHandQty >= :currentOnHandQty_Start");
			
			if (StringUtils.hasText(currentOnHandQty_End))
			    hql.append(" and model.currentOnHandQty <= :currentOnHandQty_End");
			
			if (StringUtils.hasText(category1))
			    hql.append(" and model4.category01 = :category1");
			
			if (StringUtils.hasText(category2))
			    hql.append(" and model4.category02 = :category2");
			
			if (StringUtils.hasText(category3))
			    hql.append(" and model4.category03 = :category3");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			query.setString("typeCode", priceType);
			query.setString("employeeCode", employeeCode);

			if (StringUtils.hasText(warehouseManager))
			    query.setString("warehouseManager", warehouseManager);

			if (StringUtils.hasText(itemCode_Start))
			    query.setString("itemCode_Start", itemCode_Start);

			if (StringUtils.hasText(itemCode_End))
			    query.setString("itemCode_End", itemCode_End);
			
			if (StringUtils.hasText(itemName))
			    query.setString("itemName", "%" + itemName + "%");

			if (StringUtils.hasText(unitPrice_Start))
			    query.setDouble("unitPrice_Start", Double.parseDouble(unitPrice_Start));

			if (StringUtils.hasText(unitPrice_End))
			    query.setDouble("unitPrice_End", Double.parseDouble(unitPrice_End));

			if (StringUtils.hasText(warehouseCode_Start))
			    query.setString("warehouseCode_Start", warehouseCode_Start);

			if (StringUtils.hasText(warehouseCode_End))
			    query.setString("warehouseCode_End", warehouseCode_End);

			if (StringUtils.hasText(lotNo))
			    query.setString("lotNo", lotNo);
			
			if (StringUtils.hasText(currentOnHandQty_Start))
			    query.setDouble("currentOnHandQty_Start", Double.parseDouble(currentOnHandQty_Start));

			if (StringUtils.hasText(currentOnHandQty_End))
			    query.setDouble("currentOnHandQty_End", Double.parseDouble(currentOnHandQty_End));
			
			if (StringUtils.hasText(category1))
			    query.setString("category1", category1);
			
			if (StringUtils.hasText(category2))
			    query.setString("category2", category2);
			
			if (StringUtils.hasText(category3))
			    query.setString("category3", category3);

			return query.list();
		    }
		});

	return result;
    }
    
    public List<Object[]> findImItemPriceOnHandViewForExplortDB(String brandCode){
    	Object values[] = new Object[] {brandCode};
    	StringBuffer sql = new StringBuffer("select brandCode,itemCode,salesUnit,taxCode,unitPrice,currentOnHandQty from ImItemPriceOnHandView where brandCode = ? group by brandCode,itemCode,salesUnit,taxCode,unitPrice,currentOnHandQty");
    	return this.getHibernateTemplate().find(sql.toString(), values) ;    	
    }
    
    /**
     * 依據品牌代號、價格類別、warehouseEmployee等條件查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List getWarehouseForWarehouseEmployee(HashMap conditionMap) {

	String brandCode = (String) conditionMap.get("brandCode");
	String warehouseEmployee = (String) conditionMap.get("warehouseEmployee");
	String priceType = (String) conditionMap.get("priceType");
	
	StringBuffer hql = new StringBuffer("select model.warehouseCode, model.warehouseName, model.warehouseManager from ImItemPriceOnHandView as model, ImWarehouseEmployee as model2, BuEmployeeWithAddressView as model3");
	hql.append(" where model.warehouseCode = model2.id.warehouseCode and model.warehouseManager = model3.employeeCode");
	hql.append(" and model.brandCode = ?");
	hql.append(" and model.typeCode = ?");
	hql.append(" and model2.id.employeeCode = ?");
	hql.append(" group by model.warehouseCode, model.warehouseName, model.warehouseManager");
	hql.append(" order by model.warehouseCode");
	
	Object[] parameterArray = new Object[] {brandCode, priceType, warehouseEmployee};
	return getHibernateTemplate().find(hql.toString(), parameterArray);
    }
    
    /**
     * 依據商品價格及庫存查詢螢幕的輸入條件進行查詢不加入倉管人員及調渡人員條件
     * 
     * @param conditionMap
     * @return List
     */
    public List findItemPriceOnHandWithoutEmployee(HashMap conditionMap) {
	
	final String brandCode = (String)conditionMap.get("brandCode");	
	final String priceType = (String)conditionMap.get("priceType");	
	final String itemCode_Start = (String)conditionMap.get("itemCode_Start");
	final String itemCode_End = (String)conditionMap.get("itemCode_End");         
	final String itemName = (String)conditionMap.get("itemName");
	final String unitPrice_Start = (String)conditionMap.get("unitPrice_Start");   
	final String unitPrice_End = (String)conditionMap.get("unitPrice_End");
	final String warehouseCode_Start = (String)conditionMap.get("warehouseCode_Start");
	final String warehouseCode_End = (String)conditionMap.get("warehouseCode_End");
	final String lotNo = (String)conditionMap.get("lotNo");
	final String currentOnHandQty_Start = (String)conditionMap.get("currentOnHandQty_Start"); 
	final String currentOnHandQty_End = (String)conditionMap.get("currentOnHandQty_End");
	final String category1 = (String)conditionMap.get("category1"); 
	final String category2 = (String)conditionMap.get("category2"); 
	final String category3 = (String)conditionMap.get("category3"); 
	
	List result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {
		    System.out.println(brandCode);
			StringBuffer hql = new StringBuffer(
				"select model.itemCode, model.itemCName, model.unitPrice, model.warehouseCode, " +
				"model.warehouseName, model.lotNo, model.currentOnHandQty");
			hql.append(" from ImItemPriceOnHandView as model, ImItem as model4");
			hql.append(" where 1=1 " );
			hql.append(" and model.itemCode = model4.itemCode");
			hql.append(" and model.brandCode = :brandCode");
			hql.append(" and model.typeCode = :typeCode");
			
			if (StringUtils.hasText(itemCode_Start))
			    hql.append(" and model.itemCode >= :itemCode_Start");

			if (StringUtils.hasText(itemCode_End))
			    hql.append(" and model.itemCode <= :itemCode_End");

			if (StringUtils.hasText(itemName))
			    hql.append(" and model.itemCName like :itemName");

			if (StringUtils.hasText(unitPrice_Start))
			    hql.append(" and model.unitPrice >= :unitPrice_Start");

			if (StringUtils.hasText(unitPrice_End))
			    hql.append(" and model.unitPrice <= :unitPrice_End");

			if (StringUtils.hasText(warehouseCode_Start))
			    hql.append(" and model.warehouseCode >= :warehouseCode_Start");

			if (StringUtils.hasText(warehouseCode_End))
			    hql.append(" and model.warehouseCode <= :warehouseCode_End");
			
			if (StringUtils.hasText(lotNo))
			    hql.append(" and model.lotNo = :lotNo");

			if (StringUtils.hasText(currentOnHandQty_Start))
			    hql.append(" and model.currentOnHandQty >= :currentOnHandQty_Start");
			
			if (StringUtils.hasText(currentOnHandQty_End))
			    hql.append(" and model.currentOnHandQty <= :currentOnHandQty_End");
			
			if (StringUtils.hasText(category1))
			    hql.append(" and model4.category01 = :category1");
			
			if (StringUtils.hasText(category2))
			    hql.append(" and model4.category02 = :category2");
			
			if (StringUtils.hasText(category3))
			    hql.append(" and model4.category03 = :category3");
			System.out.println(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			query.setString("brandCode", brandCode);
			query.setString("typeCode", priceType);
			
			if (StringUtils.hasText(itemCode_Start))
			    query.setString("itemCode_Start", itemCode_Start);

			if (StringUtils.hasText(itemCode_End))
			    query.setString("itemCode_End", itemCode_End);
			
			if (StringUtils.hasText(itemName))
			    query.setString("itemName", "%" + itemName + "%");

			if (StringUtils.hasText(unitPrice_Start))
			    query.setDouble("unitPrice_Start", Double.parseDouble(unitPrice_Start));

			if (StringUtils.hasText(unitPrice_End))
			    query.setDouble("unitPrice_End", Double.parseDouble(unitPrice_End));

			if (StringUtils.hasText(warehouseCode_Start))
			    query.setString("warehouseCode_Start", warehouseCode_Start);

			if (StringUtils.hasText(warehouseCode_End))
			    query.setString("warehouseCode_End", warehouseCode_End);

			if (StringUtils.hasText(lotNo))
			    query.setString("lotNo", lotNo);
			
			if (StringUtils.hasText(currentOnHandQty_Start))
			    query.setDouble("currentOnHandQty_Start", Double.parseDouble(currentOnHandQty_Start));

			if (StringUtils.hasText(currentOnHandQty_End))
			    query.setDouble("currentOnHandQty_End", Double.parseDouble(currentOnHandQty_End));
			
			if (StringUtils.hasText(category1))
			    query.setString("category1", category1);
			
			if (StringUtils.hasText(category2))
			    query.setString("category2", category2);
			
			if (StringUtils.hasText(category3))
			    query.setString("category3", category3);

			return query.list();
		    }
		});

	return result;
    }
}