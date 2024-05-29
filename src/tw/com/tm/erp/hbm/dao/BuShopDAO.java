package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;

import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;

/**
 * A data access object (DAO) providing persistence and search support for
 * BuShop entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuShop
 * @author MyEclipse Persistence Tools
 */

public class BuShopDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(BuShopDAO.class);
    // property constants
    public static final String SHOP_CNAME = "shopCName";
    public static final String SHOP_ENAME = "shopEName";
    public static final String PLACE = "place";
    public static final String SUPPLIER_CODE = "supplierCode";
    public static final String SHOP_TYPE = "shopType";
    public static final String SHOP_LEVEL = "shopLevel";
    public static final String BRAND_CODE = "brandCode";
    public static final String INCHARGE = "incharge";
    public static final String CONTRACT_PERSON = "contractPerson";
    public static final String TEL = "tel";
    public static final String TSUBO = "tsubo";
    public static final String AVERAGE_EMPLOYEE = "averageEmployee";
    public static final String SHOP_STYLE = "shopStyle";
    public static final String BILL_DAY = "billDay";
    public static final String INVOICE_DAY = "invoiceDay";
    public static final String COLLECTION_DAY = "collectionDay";
    public static final String BILL_TYPE = "billType";
    public static final String BILL_AMOUNT = "billAmount";
    public static final String BILL_TEL = "billTel";
    public static final String BILL_ADDRESS = "billAddress";
    public static final String SHOP_ADDRESS = "shopAddress";
    public static final String SALES_BONUS_TYPE = "salesBonusType";
    public static final String LOCATION_CODE = "locationCode";
    public static final String SALES_WAREHOUSE_CODE = "salesWarehouseCode";
    public static final String ENABLE = "enable";
    public static final String CREATED_BY = "createdBy";
    public static final String LAST_UPDATED_BY = "lastUpdatedBy";

    public BuShop findById(String shopCode) {
	return (BuShop) findByPrimaryKey(BuShop.class, shopCode);
    }

    public List findAll() {
	log.debug("finding all BuShop instances");
	try {
	    String queryString = "from BuShop";
	    return getHibernateTemplate().find(queryString);
	} catch (RuntimeException re) {
	    log.error("find all failed", re);
	    throw re;
	}
    }

    /**
     * 依據品牌代號、員工編號查詢出員工隸屬的專櫃代號
     * 
     * @param brandCode
     * @param employeeCode
     * @param isEnable
     * @return List
     */
    public List<BuShop> getShopForEmployee(String brandCode,
	    String employeeCode, String isEnable) {

	final String brandCode_arg = brandCode;
	final String employeeCode_arg = employeeCode;
	final String isEnable_arg = isEnable;

	List<BuShop> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"select model from BuShop as model, BuShopEmployee as model2");
			hql
				.append(" where model.shopCode = model2.id.shopCode");
			hql.append(" and model.brandCode = :brandCode");
			hql
				.append(" and model2.id.employeeCode = :employeeCode");
			if(StringUtils.hasText(isEnable_arg)){
			    hql.append(" and model.enable = :enable");
			    hql.append(" and model2.enable = :enable");
			}
			hql.append(" order by model.shopCode");

			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode_arg);
			query.setString("employeeCode", employeeCode_arg);
			if(StringUtils.hasText(isEnable_arg)){
			    query.setString("enable", isEnable_arg);
			}

			return query.list();
		    }
		});
	return result;
    }

    /**
     * 依據品牌代號、啟用狀態查詢出專櫃代號
     * 
     * @param brandCode
     * @param isEnable
     * @return List
     */
    public List findShopByBrandAndEnable(String brandCode, String isEnable) {

    	StringBuffer hql = new StringBuffer("from BuShop as model");
    	hql.append(" where model.brandCode = ?");
    	if (StringUtils.hasText(isEnable)) {
    		hql.append(" and model.enable = ?");
    		hql.append(" order by model.shopCode ");
    		return getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, isEnable });
    	}else{
    		hql.append(" order by model.shopCode ");
    		return getHibernateTemplate().find(hql.toString(), new Object[] { brandCode });
    	}
    }
    public List findShopListWithOrder(String brandCode, String shopSalesType, String isEnable) {

    	StringBuffer hql = new StringBuffer("from BuShop as model");
    	hql.append(" where model.brandCode = ?");
    	hql.append(" and model.shopSalesType = ?");
    	if (StringUtils.hasText(isEnable)) {
    		hql.append(" and model.enable = ?");
    		hql.append(" order by model.shopCode ");
    		return getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, isEnable, shopSalesType });
    	}else{
    		hql.append(" order by model.shopCode ");
    		return getHibernateTemplate().find(hql.toString(), new Object[] { brandCode,shopSalesType });
    	}
    }
    /**
     * 依據品牌代號、啟用日期查詢出已啟用專櫃代號
     * 
     * @param brandCode
     * @param isEnable
     * @return List
     */
    public List findShopByBrandAndScheduleDate(String brandCode, Date scheduleDate) {
    	StringBuffer hql = new StringBuffer("from BuShop as model where 1=1 ");
    	hql.append(" and model.brandCode = ?");
    	hql.append(" and model.enable = ?");
		hql.append(" and model.scheduleDate <= ?");
		hql.append(" order by model.shopCode ");
		return getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, "Y", scheduleDate });
    }
    
    public BuShop merge(BuShop detachedInstance) {
	log.debug("merging BuShop instance");
	try {
	    BuShop result = (BuShop) getHibernateTemplate().merge(
		    detachedInstance);
	    log.debug("merge successful");
	    return result;
	} catch (RuntimeException re) {
	    log.error("merge failed", re);
	    throw re;
	}
    }

    public void attachDirty(BuShop instance) {
	log.debug("attaching dirty BuShop instance");
	try {
	    getHibernateTemplate().saveOrUpdate(instance);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    public void attachClean(BuShop instance) {
	log.debug("attaching clean BuShop instance");
	try {
	    getHibernateTemplate().lock(instance, LockMode.NONE);
	    log.debug("attach successful");
	} catch (RuntimeException re) {
	    log.error("attach failed", re);
	    throw re;
	}
    }

    /**
     * 依照輸入條件來搜尋專櫃資料
     * 
     * @param findObjs
     * @return
     */
    public List<BuShop> find(HashMap conditionMap) {

	final String brandCode = (String) conditionMap.get("brandCode");
	final String shopCode = (String) conditionMap.get("shopCode");
	final String shopCName = (String) conditionMap.get("shopCName");
	final String shopType = (String) conditionMap.get("shopType");
	final String place = (String) conditionMap.get("place");

	List<BuShop> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("select model from BuShop as model");
			hql.append(" where model.brandCode = :brandCode");

			if (StringUtils.hasText(shopCode))
			    hql.append(" and model.shopCode = :shopCode");

			if (StringUtils.hasText(shopCName))
			    hql.append(" and model.shopCName like :shopCName");

			if (StringUtils.hasText(shopType))
			    hql.append(" and model.shopType = :shopType");

			if (StringUtils.hasText(place))
			    hql.append(" and model.place = :place");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

			query.setString("brandCode", brandCode);
			if (StringUtils.hasText(shopCode))
			    query.setString("shopCode", shopCode);

			if (StringUtils.hasText(shopCName))
			    query.setString("shopCName", "%" + shopCName + "%");

			if (StringUtils.hasText(shopType))
			    query.setString("shopType", shopType);

			if (StringUtils.hasText(place))
			    query.setString("place", place);

			return query.list();
		    }
		});

	return result;
    }

    /**
     * 利用ID 取得多個 SHOP
     * 
     * @param ids
     * @return
     */
    public List<BuShop> find(String ids) {
	final String id = ids;
	List<BuShop> re = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from BuShop as model where 1=1 ");
			hql.append(" and model.shopCode in ( ");
			hql.append(id);
			hql.append(" )");

			Query query = session.createQuery(hql.toString());
			return query.list();
		    }
		});
	return re;
    }

    public static BuShopDAO getFromApplicationContext(ApplicationContext ctx) {
	return (BuShopDAO) ctx.getBean("buShopDAO");
    }

    /**
     * 依據品牌代號及專櫃代號，查詢啟用狀態之專櫃資料
     * 
     * @param brandCode
     * @param shopCode
     * @return BuShop
     */
    public BuShop findEnableShopById(String brandCode, String shopCode) {
	try {
	    StringBuffer hql = new StringBuffer("from BuShop as model ");
	    hql.append("where model.brandCode = ? ");
	    hql.append("and model.shopCode = ? ");
	    hql.append("and model.enable = ? ");
	    List<BuShop> lists = getHibernateTemplate().find(hql.toString(),
		    new Object[] { brandCode, shopCode, "Y" });
	    return (lists.size() > 0 ? lists.get(0) : null);

	} catch (RuntimeException re) {
	    throw re;
	}
    }

    /**
     * 依照輸入條件來搜尋專櫃資料
     * 
     * @param brandCode
     * @param shopCode
     * @param shopCName
     * @param shopType
     * @param place
     * @param SearchString
     * @return List
     */
    public List<BuShop> findShopList(String brandCode, String shopCode,
	    String shopCName, String shopType, String place, String searchString) {
	try {

	    StringBuffer hql = new StringBuffer("from BuShop as model ");
	    hql.append("where model.brandCode = '" + brandCode + "' ");
	    hql
		    .append((shopCode != null && !"".equals(shopCode) ? "and upper(model.shopCode) LIKE '%"
			    + shopCode.toUpperCase() + "%' "
			    : ""));
	    hql
		    .append((shopCName != null && !"".equals(shopCName) ? "and upper(model.shopCName) LIKE "
			    + "'%" + shopCName.toUpperCase() + "%'" + " "
			    : ""));
	    hql
		    .append((shopType != null && !"".equals(shopType) ? "and model.shopType = '"
			    + shopType + "' "
			    : ""));
	    hql
		    .append((place != null && !"".equals(place) ? "and upper(model.place) LIKE "
			    + "'%" + place.toUpperCase() + "%'" + " "
			    : ""));
	    hql
		    .append((searchString != null
			    && !"".equals(searchString.trim()) ? "and ("
			    + searchString + ") " : ""));

	    System.out.println(hql.toString());
	    getHibernateTemplate().setMaxResults(
		    SystemConfig.SEARCH_PAGE_MAX_COUNT);
	    return getHibernateTemplate().find(hql.toString());

	} catch (RuntimeException re) {
	    throw re;
	}
    }
    
    /**
     * 依據品牌代號及專櫃代號，查詢啟用狀態之專櫃資料
     * 
     * @param brandCode
     * @param shopCode
     * @return BuShop
     */
    public BuShop findShopByBrandCodeAndShopCode(String brandCode, String shopCode) {
	
        StringBuffer hql = new StringBuffer("from BuShop as model ");
	hql.append("where model.brandCode = ? ");
	hql.append("and model.shopCode = ? ");
	
	List<BuShop> lists = getHibernateTemplate().find(hql.toString(),
	    new Object[] { brandCode, shopCode});
	return (lists.size() > 0 ? lists.get(0) : null);
    }

    /**
     * 依品牌、部門、enable，取得專櫃下拉
     * @param brandCode
     * @param department
     * @param enable
     * @return
     */
    public List<BuShop> findShopByProperty(String brandCode, String department, String enable){
    	return findByProperty( "BuShop", "and brandCode = ? and departmentName like '%" + department + "%' and enable = ? ", new Object[]{brandCode, "Y"});
    }
    
    /**
     * 依品牌、起迄日期，取得專櫃下拉
     * @param HashMap conditionMap
     * @return
     */
    public List findShopByCondition(HashMap conditionMap) {
	final String brandCode = (String) conditionMap.get("brandCode");
	final String dataDate = (String) conditionMap.get("dataDate");
	final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException, SQLException {
		StringBuffer hql = new StringBuffer("select model from BuShop as model where 1=1 ");
		hql.append(" and model.brandCode  = :brandCode ");
		if(StringUtils.hasText(dataDate))
		    hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
		if(StringUtils.hasText(dataDateEnd))
		    hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
		log.info("findShopByCondition hql = " + hql.toString());
		Query query = session.createQuery(hql.toString());
		query.setString("brandCode", brandCode);
		if(StringUtils.hasText(dataDate))
		    query.setString("dataDate", dataDate);
		if(StringUtils.hasText(dataDateEnd))
		    query.setString("dataDateEnd", dataDateEnd);
		return query.list();
	    }
	});

	return result;
    }
    
    /**
     * 依品牌與shopCode條件撈取
     * @param brandCode
     * @param ShopCode
     * @return
     */
    public BuShop findByBrandCodeAndShopCode(String brandCode, String shopCode){
	return (BuShop)findFirstByProperty("BuShop", "and brandCode = ? and shopCode = ?", new Object[]{brandCode,shopCode});
    }
    /**
     * 依品牌、部門、enable，取得專櫃下拉
     * @param brandCode
     * @param department
     * @param enable
     * @return
     */
    public List<BuShop> findShopByBrand(String brandCode, String enable, String place){
    	return findByProperty( "BuShop", "and brandCode = ?  and enable = ? and place = ? order by shopCode", new Object[]{brandCode, "Y", place});
    }
    
    /**
     * posting 範例
     * 
     * @param conditionMap
     * @param startPage
     * @param pageSize
     * @param searchType
     * @return HashMap
     */
    public HashMap findDataListBy(HashMap conditionMap, int startPage, int pageSize, int searchType){
	
	final String shopCode = (String) conditionMap.get("shopCode");
	final String enable = (String) conditionMap.get("enable");
	final String employeeCode = (String) conditionMap.get("employeeCode");
	final int startRecordIndexStar = startPage * pageSize;
	final int pSize = pageSize;
	final int type = searchType;
	log.info("shopCode"+shopCode);
	List result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer("");
			if(BaseDAO.QUERY_RECORD_COUNT == type) {log.info("11111111");
				hql.append("select count(*) as rowCount ");
			}else{log.info("22222222");
				hql.append("select shop_code, enable ");
			}log.info("3333333333");
			hql.append("from erp.bu_shop_employee where shop_code = :shopCode and enable = :enable ");
		//	hql.append(" and s.shop_code = :shopCode");
			
		//	hql.append(" order by s.shop_code");
			log.info("hql = " + hql.toString());
			log.info("4444444444444");
			Query query = session.createSQLQuery(hql.toString());
			if(BaseDAO.QUERY_SELECT_RANGE == type) {log.info("5555555555555");
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
			}
			log.info("666666666666"+shopCode + enable );
			query.setString("shopCode", shopCode);
			query.setString("enable", enable);
		//	query.setString("employee_code", employeeCode);
			log.info("777777777");
			return query.list();
		}
	});

	HashMap returnResult = new HashMap();		
	returnResult.put(BaseDAO.TABLE_LIST, (BaseDAO.QUERY_SELECT_RANGE == type || BaseDAO.QUERY_SELECT_ALL == type)? result : null);			
	if(result.size() == 0){
		returnResult.put(BaseDAO.TABLE_RECORD_COUNT, 0L);
	}else{
		returnResult.put(BaseDAO.TABLE_RECORD_COUNT, (BaseDAO.QUERY_SELECT_RANGE == type || BaseDAO.QUERY_SELECT_ALL == type)? result.size() : Long.valueOf(result.get(0).toString()));
	}
	return returnResult;
    }
    
    //權限用()
    public List<BuShop> findPageLine(String employeeCode,int startPage, int pageSize) {
        final int startRecordIndexStar = startPage * pageSize;
        final int pSize = pageSize;
        final String enable = "Y";
        final String employee_Code = employeeCode;
    
        List<BuShop> result = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
               // String hql = " from BuShop M where M.enable=:enable";
		    	String hql = "from BuShop M where M.id.shopCode in (select G.id.shopCode from BuShopEmployee G where G.id.employeeCode=:employeeCode ) order by M.id.shopCode";
                    Query query = session.createQuery(hql);
                    if(startRecordIndexStar > 0) query.setFirstResult(startRecordIndexStar);
                    if(pSize > 0) query.setMaxResults(pSize);
                    //query.setParameter("enable", enable);
                    query.setParameter("employeeCode", employee_Code);
                    return query.list();
                }
            }); 
        return result;
    }
    
    public Long findPageLineCount(String employeeCode){
        final String enable = "Y";
        String hql = " select count(*) from BuShop M where M.id.shopCode in (select G.id.shopCode from BuShopEmployee G where G.id.employeeCode=? ) order by M.id.shopCode";
        Object obj = getHibernateTemplate().find(hql, new Object[]{employeeCode});
        return obj == null ? 0L : (Long)(((List)obj).get(0));
    }
    
    public BuShop findShopCode( String shopCode) {

    	StringBuffer hql = new StringBuffer("from BuShop as model ");

    	hql.append("where model.shopCode = ? ");

    	List<BuShop> lists = getHibernateTemplate().find(hql.toString(),
    			new Object[] {  shopCode});
    	return (lists.size() > 0 ? lists.get(0) : null);
    }
  
}