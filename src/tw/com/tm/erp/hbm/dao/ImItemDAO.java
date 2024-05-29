/*Local*/

package tw.com.tm.erp.hbm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
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
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;


/**
 * A data access object (DAO) providing persistence and search support for
 * ImItem entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.ImItem
 * @author MyEclipse Persistence Tools
 */

public class ImItemDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImItemDAO.class);
	// property constants
	public static final String BRAND_CODE = "brandCode";
	public static final String ITEM_CNAME = "itemCName";
	public static final String ITEM_ENAME = "itemEName";
	public static final String ITEM_LEVEL = "itemLevel";
	public static final String IS_COMPOSE_ITEM = "isComposeItem";
	public static final String SPEC_LENGTH = "specLength";
	public static final String SPEC_WIDTH = "specWidth";
	public static final String SPEC_HEIGHT = "specHeight";
	public static final String SPEC_WEIGHT = "specWeight";
	public static final String SALES_RATIO = "salesRatio";
	public static final String SALES_UNIT = "salesUnit";
	public static final String PURCHASE_UNIT = "purchaseUnit";
	public static final String DESCRIPTION = "description";
	public static final String CATEGORY01 = "category01";
	public static final String CATEGORY02 = "category02";
	public static final String CATEGORY03 = "category03";
	public static final String CATEGORY04 = "category04";
	public static final String CATEGORY05 = "category05";
	public static final String CATEGORY06 = "category06";
	public static final String CATEGORY07 = "category07";
	public static final String CATEGORY08 = "category08";
	public static final String CATEGORY09 = "category09";
	public static final String CATEGORY10 = "category10";
	public static final String CATEGORY11 = "category11";
	public static final String CATEGORY12 = "category12";
	public static final String CATEGORY13 = "category13";
	public static final String CATEGORY14 = "category14";
	public static final String CATEGORY15 = "category15";
	public static final String CATEGORY16 = "category16";
	public static final String CATEGORY17 = "category17";
	public static final String CATEGORY18 = "category18";
	public static final String CATEGORY19 = "category19";
	public static final String CATEGORY20 = "category20";
	public static final String ENABLE = "enable";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";

	protected void initDao() {
		// do nothing
	}

	public void delete(ImItem persistentInstance) {
		log.debug("deleting ImItem instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImItem findById(java.lang.String id) {
		if(StringUtils.hasText(id)){
		    id = id.toUpperCase();
		}
		final String itemCode = id;
		List<ImItem> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItem as model where 1=1 ");
				hql.append(" and model.itemCode  = :itemCode ");

				Query query = session.createQuery(hql.toString());
				query.setString("itemCode", itemCode);

				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	public ImItem findByItemId(java.lang.Long id) {
		log.debug("getting ImItem instance with id: " + id);
		try {
			ImItem instance = (ImItem) getHibernateTemplate().get("tw.com.tm.erp.hbm.bean.ImItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List findByExample(ImItem instance) {
		log.debug("finding ImItem instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImItem instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from ImItem as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBrandCode(Object brandCode) {
		return findByProperty(BRAND_CODE, brandCode);
	}

	public List findByItemCName(Object itemCName) {
		return findByProperty(ITEM_CNAME, itemCName);
	}

	public List findByItemEName(Object itemEName) {
		return findByProperty(ITEM_ENAME, itemEName);
	}

	public List findByItemLevel(Object itemLevel) {
		return findByProperty(ITEM_LEVEL, itemLevel);
	}

	public List findByIsComposeItem(Object isComposeItem) {
		return findByProperty(IS_COMPOSE_ITEM, isComposeItem);
	}

	public List findBySpecLength(Object specLength) {
		return findByProperty(SPEC_LENGTH, specLength);
	}

	public List findBySpecWidth(Object specWidth) {
		return findByProperty(SPEC_WIDTH, specWidth);
	}

	public List findBySpecHeight(Object specHeight) {
		return findByProperty(SPEC_HEIGHT, specHeight);
	}

	public List findBySpecWeight(Object specWeight) {
		return findByProperty(SPEC_WEIGHT, specWeight);
	}

	public List findBySalesRatio(Object salesRatio) {
		return findByProperty(SALES_RATIO, salesRatio);
	}

	public List findBySalesUnit(Object salesUnit) {
		return findByProperty(SALES_UNIT, salesUnit);
	}

	public List findByPurchaseUnit(Object purchaseUnit) {
		return findByProperty(PURCHASE_UNIT, purchaseUnit);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByCategory01(Object category01) {
		return findByProperty(CATEGORY01, category01);
	}

	public List findByCategory02(Object category02) {
		return findByProperty(CATEGORY02, category02);
	}

	public List findByCategory03(Object category03) {
		return findByProperty(CATEGORY03, category03);
	}

	public List findByCategory04(Object category04) {
		return findByProperty(CATEGORY04, category04);
	}

	public List findByCategory05(Object category05) {
		return findByProperty(CATEGORY05, category05);
	}

	public List findByCategory06(Object category06) {
		return findByProperty(CATEGORY06, category06);
	}

	public List findByCategory07(Object category07) {
		return findByProperty(CATEGORY07, category07);
	}

	public List findByCategory08(Object category08) {
		return findByProperty(CATEGORY08, category08);
	}

	public List findByCategory09(Object category09) {
		return findByProperty(CATEGORY09, category09);
	}

	public List findByCategory10(Object category10) {
		return findByProperty(CATEGORY10, category10);
	}

	public List findByCategory11(Object category11) {
		return findByProperty(CATEGORY11, category11);
	}

	public List findByCategory12(Object category12) {
		return findByProperty(CATEGORY12, category12);
	}

	public List findByCategory13(Object category13) {
		return findByProperty(CATEGORY13, category13);
	}

	public List findByCategory14(Object category14) {
		return findByProperty(CATEGORY14, category14);
	}

	public List findByCategory15(Object category15) {
		return findByProperty(CATEGORY15, category15);
	}

	public List findByCategory16(Object category16) {
		return findByProperty(CATEGORY16, category16);
	}

	public List findByCategory17(Object category17) {
		return findByProperty(CATEGORY17, category17);
	}

	public List findByCategory18(Object category18) {
		return findByProperty(CATEGORY18, category18);
	}

	public List findByCategory19(Object category19) {
		return findByProperty(CATEGORY19, category19);
	}

	public List findByCategory20(Object category20) {
		return findByProperty(CATEGORY20, category20);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findAll() {
		log.debug("finding all ImItem instances");
		try {
			String queryString = "from ImItem";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImItem merge(ImItem detachedInstance) {
		log.debug("merging ImItem instance");
		try {
			ImItem result = (ImItem) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImItem instance) {
		log.debug("attaching dirty ImItem instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImItem instance) {
		log.debug("attaching clean ImItem instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImItemDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ImItemDAO) ctx.getBean("imItemDAO");
	}

	/**
	 * ????資????詢作業
	 * 
	 * @param brandId
	 * @param startItemCode
	 * @param endItemCode
	 * @param itemName
	 * @param enable
	 * @param categorySearchString
	 * @return List
	 */
	public List<ImItem> findItemList(String brandCode, String startItemCode, String endItemCode, String itemName, String enable,
			String categorySearchString) {
		try {

			StringBuffer hql = new StringBuffer("from ImItem as model ");
			hql.append("where model.brandCode = '" + brandCode + "' ");

			// ??斷????ItemCode欄????否要用模????詢(LIKE)??進??Query
			// ??startItemCode??endItemCode??????數??空????Null??????使??模糊查?
			if (StringUtils.hasText(startItemCode) || StringUtils.hasText(endItemCode)) {
				hql.append(StringUtils.hasText(startItemCode) ? "and model.itemCode LIKE '%" + startItemCode.toUpperCase() + "%' "
						: "");
				hql.append(StringUtils.hasText(endItemCode) ? "and model.itemCode LIKE '%" + endItemCode.toUpperCase() + "%' " : "");
			} else {
				hql.append(StringUtils.hasText(startItemCode) ? "and model.itemCode >= '" + startItemCode.toUpperCase() + "' " : "");
				hql.append(StringUtils.hasText(endItemCode) ? "and model.itemCode <= '" + endItemCode.toUpperCase() + "' " : "");
			}

			hql
					.append(StringUtils.hasText(itemName) ? "and model.itemCName like " + "'%" + itemName.toUpperCase() + "%'" + " "
							: "");
			hql.append(StringUtils.hasText(enable) ? "and model.enable = '" + enable + "' " : "");
			hql.append((categorySearchString != null && !"".equals(categorySearchString.trim()) ? "and (" + categorySearchString + ") "
					: ""));
			hql.append(" order by itemCode");
			System.out.println(hql.toString());
			// getHibernateTemplate().setFirstResult(0);

			getHibernateTemplate().setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
			return getHibernateTemplate().find(hql.toString());

		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * ????資????詢作業
	 * 
	 * @param brandId
	 * @param startItemCode
	 * @param endItemCode
	 * @param itemName
	 * @param enable
	 * @param categorySearchString
	 * @return List
	 */
	public List<ImItem> findOldItemList(String brandCode, String reserve5) {
		final String bCode = brandCode;
		final String res5 = reserve5;
		List<ImItem> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				System.out.println(res5 + "-" + bCode);
				StringBuffer hql = new StringBuffer("from ImItem as model where 1=1 ");
				if (StringUtils.hasText(res5))
					hql.append(" and ( model.itemCode = :itemCode or model.reserve5 = :reserve5 ) ");

				if (StringUtils.hasText(bCode))
					hql.append(" and model.brandCode = :brandCode ");

				hql.append(" and model.enable = 'Y' ");
				
				Query query = session.createQuery(hql.toString());

				if (StringUtils.hasText(res5)) {
					query.setString("reserve5", res5);
					query.setString("itemCode", res5);
				}

				if (StringUtils.hasText(bCode))
					query.setString("brandCode", bCode);

				return query.list();
			}
		});
		return re;
	}

	public ImItem findItem(String brandCode, String itemCode) {

	    	if(StringUtils.hasText(itemCode)){
	    	    itemCode = itemCode.toUpperCase();
	    	}
	    
		StringBuffer hql = new StringBuffer("from ImItem as model where 1=1 ");
		hql.append(" and model.brandCode = ? ");
		hql.append(" and model.itemCode  = ? ");

		List<ImItem> lists = getHibernateTemplate().find(hql.toString(), new String[] { brandCode, itemCode });
		if ((null != lists) && (lists.size() > 0))
			return lists.get(0);
		else
			return null;

	}

	public ImItem getLockedImItem(String brandCode, String itemCode) {
	    	if(StringUtils.hasText(itemCode)){
	    	    itemCode = itemCode.toUpperCase();
	    	}
	    	final String fBrandCode = brandCode;
	    	final String fItemCode = itemCode;
	    	
		List<ImItem> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItem as model where 1=1 ");
				hql.append(" and model.brandCode = :brandCode ");
				hql.append(" and model.itemCode  = :itemCode ");

				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);
				query.setString("brandCode", fBrandCode);
				query.setString("itemCode", fItemCode);

				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	/**
	 * ??用ID ????多??ITEM 20081007 shan
	 * 
	 * @param ids
	 * @return
	 */
	public List<ImItem> find(String ids) {
		if (ids.indexOf("'") <= 0) {

		}

		final String id = ids;
		List<ImItem> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItem as model where 1=1 ");
				hql.append(" and model.itemCode in ( ");
				hql.append(id);
				hql.append(" )");

				Query query = session.createQuery(hql.toString());
				return query.list();
			}
		});
		return re;
	}

	/**
	 * ??詢??用?????????????
	 * 
	 * @param beginDate
	 * @param brandCode
	 * @return
	 */
	public List findByBeginDate(Date beginDate, String brandCode) {
		if (null != beginDate && StringUtils.hasText(brandCode)) {
			log.debug("finding ImItemPrice.findByBeginDate " + beginDate);
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, beginCal.get(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, beginCal.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date sDate = cal.getTime();

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date eDate = cal.getTime();

			Object values[] = new Object[3];
			values[0] = brandCode;
			values[1] = sDate;
			values[2] = eDate;
			// String queryString = "select
			// item.itemCode,item.itemCName,price.unitPrice from ImItem as
			// item,ImItemPrice as price where price.enable='Y' and
			// item.brandCode = ? and price.beginDate >= ? and price.beginDate
			// <= ? order by price.lastUpdateDate";
			String queryString = "select item.itemCode,item.itemCName,item.category14,item.category08,price.unitPrice from ImItem as item,ImItemPrice as price where price.itemCode = item.itemCode and price.enable='Y' and item.brandCode = ? and price.beginDate >= ? and price.beginDate <= ? order by item.itemCode";
			// System.out.println("sDate:" + sDate + " eDate:" + eDate + "
			// findByBeginDate:" + queryString);
			return getHibernateTemplate().find(queryString, values);
		} else {
			return new ArrayList();
		}
	}

	private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    
	public void changeLocalCost(String headId) throws Exception {

		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.UPDATE_LOCALCOST(?)}");
			calStmt.setString(1, headId);
			calStmt.executeQuery();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("關閉ResultSet時發生錯誤！");
				}
			}
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
	}
	public Map getItemCNameByItemCodes(String itemCodes,String brandCode) {
	    	Map resultMap = new HashMap();
		try {
			
		    	StringBuffer hql = new StringBuffer();
		    	hql.append("select im.itemCode,im.itemCName");
			hql.append("FROM ImItem im ");
			hql.append(" where im.brandCode ='").append(brandCode).append("'");
			hql.append(" and im.itemCode in (").append(itemCodes).append(")") ;
			List result = getHibernateTemplate().find(hql.toString(), null);
			if(result != null){
			    for(int index = 0 ; index < result.size(); index++ ){
				Object[] item = (Object[])result.get(index);
				resultMap.put((String)item[0], (String)item[1]);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 依品牌,品號,稅別(完稅/保稅)查詢出一筆
	 * @param brandCode
	 * @param itemCode
	 * @param taxType
	 * @return
	 */
	public ImItem findOneImItem(String brandCode, String itemCode, String taxType ){
		return (ImItem)this.findFirstByProperty("ImItem", "and brandCode = ? and itemCode = ? and isTax = ? ", new Object[] { brandCode, itemCode , taxType });
	}
	
	public ImItem findOneImItem2(String brandCode, String itemCode, String category02 ){
		return (ImItem)this.findFirstByProperty("ImItem", "and brandCode = ? and itemCode = ? and category02 = ? ", new Object[] { brandCode, itemCode , category02 });
	}
	/**
	 * 依品牌品號啟用狀態
	 * @param brandCode
	 * @param itemCode
	 * @param enable
	 * @return
	 */
	public ImItem findImItem(String brandCode, String itemCode, String enable ){
	    return (ImItem)findFirstByProperty("ImItem", "and brandCode = ? and itemCode = ? and enable = ?", new Object[]{brandCode, itemCode,enable});
	}

	public List findbyCondition(HashMap parameterMap) {
	        final String brandCode = (String) parameterMap.get("brandCode");
		final String dataDate = (String) parameterMap.get("dataDate");
		final String dataDateEnd = (String) parameterMap.get("dataDateEnd");
		final String beginDate = dataDateEnd;
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {																																																															
//				StringBuffer hql = new StringBuffer("select model , model2 from ImItem as model, ImItemPrice as model2 ");			
//				hql.append("where model.brandCode = :brandCode ");
//				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
//				hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
//				hql.append("and model.itemCode = model2.itemCode ");
//
//				Query query = session.createQuery(hql.toString());
//				query.setString("brandCode", brandCode);
//				query.setString("dataDate", dataDate);
//				query.setString("dataDateEnd", dataDateEnd);
			    //item.is_tax, item.is_decl_ratio
				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, price.last_update_date, item.category01, item.category02, item.category13, item.vip_discount, item.decl_ratio, item.is_tax, item.item_brand, item.lot_control, " +
						"item.min_ratio, price.original_price " +
						"from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, max(begin_date) as begin_date from erp.im_item_price where brand_code = :brandCode and enable = 'Y'" +
						" and type_code = '1' and begin_date <= to_date(:beginDate,'YYYYMMDD')");
				hql.append(" group by brand_code, item_code, type_code) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				hql.append(" and item.enable = 'Y'");
				hql.append(" and ((to_char(price.last_update_date, 'YYYYMMDD') >= :dataDate");
				hql.append(" and to_char(price.last_update_date, 'YYYYMMDD') <= :dataDateEnd) or");
				hql.append(" (to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate");
				hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd))");
			        hql.append(" order by item.item_code");

				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				query.setString("beginDate", beginDate);
				
				return query.list();
			}
		});

		return result;
	}

	public List findbySelectedImItem(HashMap parameterMap,final String itemCode,final String brandCode) {

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {																																																															

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, price.last_update_date, item.category01, item.category02, item.category13, item.vip_discount, item.decl_ratio, item.is_tax, item.item_brand, item.lot_control, " +
						"item.min_ratio, price.original_price " +
						"from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, max(begin_date) as begin_date from erp.im_item_price where brand_code = :brandCode and enable = 'Y'" +
						" and type_code = '1' ");
				hql.append(" group by brand_code, item_code, type_code) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				hql.append(" and item.enable = 'Y'");
				hql.append(" and item.item_code = :itemCode");
			        hql.append(" order by item.item_code");
			        System.out.println("==brand=="+brandCode);
			        System.out.println("==itemCode=="+itemCode);
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("itemCode", itemCode);
				System.out.println("======hql=="+query);
				return query.list();
			}
		});

		return result;
	}
	public void updatePosItem(final Object[] result) {
	    
		getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    String itemCName = result[16] == null ? "":(String)result[16];
			    if(itemCName.contains("'")){
				itemCName = itemCName.replace("'","''");
			    }
			    String sql = "INSERT INTO POS.POS_ITEM (ACTION, BRAND_CODE, CATEGORY01, CATEGORY01_NAME, " +
		    		" CATEGORY02, CATEGORY02_NAME, DATA_ID, DECL_RATIO, " +
		    		" ENABLE, IS_AFTER_COUNT, IS_BEFORE_COUNT, IS_CHANGE_PRICE, " +
		    		" IS_SERVICE_ITEM, IS_TAX, ITEM_BRAND, ITEM_BRAND_NAME, " +
		    		" ITEM_C_NAME, ITEM_CODE, LOT_CONTROL, MIN_RATIO, ORIGINAL_UNIT_PRICE, " +
		    		" SALES_UNIT, SELL_UNIT_PRICE, VIP_DISCOUNT,HEAD_ID) " +
		    		"VALUES('"+result[0]+"','"+result[1]+"','"+result[2]+"','"+result[3]+"','"+result[4]+"'," +
    				"'"+result[5]+"','"+result[6]+"',"+result[7]+",'"+result[8]+"','"+result[9]+"'," +
    				"'"+result[10]+"','"+result[11]+"','"+result[12]+"','"+result[13]+"'," +
    				"'"+result[14]+"','"+result[15]+"','"+itemCName+"','"+result[17]+"'," +
    				"'"+result[18]+"',"+result[19]+","+result[20]+",'"+result[21]+"'," +
    				"'"+result[22]+"','"+result[23]+"',POS.POS_ITEM_SEQ.NEXTVAL)";
			    Query query = session.createSQLQuery(sql);
//			    for(int i=0;i<result.length;i++){
//				System.out.println("=====PRINT======="+result[i]);
//				query.setParameter(i, result[i]);
//			    }
			    query.executeUpdate();
			    return null;
			}
		});

	    
	}

	public List findItemBrandListByCategory02(final String category) {
	
		List<ImItem> re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model.itemBrand from ImItem as model where ");
				hql.append(" model.category02 = :category02");
				hql.append(" group by model.itemBrand");
				hql.append(" order by model.itemBrand asc");
				Query query = session.createQuery(hql.toString());
				query.setString("category02", category);
				return query.list();
			}
		});
		return re;
	}
}
