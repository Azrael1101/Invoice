package tw.com.tm.erp.hbm.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;

/**
 * A data access object (DAO) providing persistence and search support for
 * ImItemPrice entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 *
 * @see tw.com.tm.erp.hbm.bean.ImItemPrice
 * @author MyEclipse Persistence Tools
 */

public class ImItemPriceDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImItemPriceDAO.class);
	// property constants
	public static final String PRICE_ADJUST_ID = "priceAdjustId";
	public static final String ITEM_CODE = "itemCode";
	public static final String TYPE_CODE = "typeCode";
	public static final String SALES_UNIT = "salesUnit";
	public static final String CURRENCY_CODE = "currencyCode";
	public static final String UNIT_PRICE = "unitPrice";
	public static final String IS_TAX = "isTax";
	public static final String TAX_CODE = "taxCode";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String INDEX_NO = "indexNo";

	protected void initDao() {
		// do nothing
	}

	public void save(ImItemPrice transientInstance) {
		log.debug("saving ImItemPrice instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * update ImItemPrice
	 *
	 * @param ImItemPrice
	 */
	public boolean update(ImItemPrice transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().update(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			throw re;

		}
		return result;
	}

	public void delete(ImItemPrice persistentInstance) {
		log.debug("deleting ImItemPrice instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ImItemPrice findById(java.lang.Long id) {
		log.debug("getting ImItemPrice instance with id: " + id);
		try {
			ImItemPrice instance = (ImItemPrice) getHibernateTemplate().get(
					"tw.com.tm.erp.hbm.bean.ImItemPrice", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ImItemPrice instance) {
		log.debug("finding ImItemPrice instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ImItemPrice instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ImItemPrice as model where model."
				+ propertyName + "= ? and model.enable = 'Y'";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public ImItemPrice findByItemTypeEnableDate(String brandCode, String itemCode, String typeCode) {
		try {
			/*
			SELECT ITEM_CODE, TYPE_CODE,MAX(BEGIN_DATE)
			from IM_ITEM_PRICE
			WHERE ITEM_CODE = 'CO8620SBKCB' AND TYPE_CODE='1'
			GROUP BY ITEM_CODE, TYPE_CODE

			String hsql = "select max(FiPaymentTerm0.paymentTermId) from tw.com.tm.value.FiPaymentTerm as FiPaymentTerm0";

			*/
			String queryString = "from ImItemPrice as model where model.brandCode= '" + brandCode + "' and model.itemCode= '" + itemCode + "' and model.typeCode='"+ typeCode +"' Order BY model.beginDate desc  " ;
			List<ImItemPrice> imItemPrices = getHibernateTemplate().find(queryString);
			if( imItemPrices.size() > 0 ){
				return imItemPrices.get(0);
			}
			return null ;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List findByPriceAdjustId(Object priceAdjustId) {
		return findByProperty(PRICE_ADJUST_ID, priceAdjustId);
	}

	public List findByItemCode(Object itemCode) {
		return findByProperty(ITEM_CODE, itemCode);
	}

	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}

	public List findBySalesUnit(Object salesUnit) {
		return findByProperty(SALES_UNIT, salesUnit);
	}

	public List findByCurrencyCode(Object currencyCode) {
		return findByProperty(CURRENCY_CODE, currencyCode);
	}

	public List findByUnitPrice(Object unitPrice) {
		return findByProperty(UNIT_PRICE, unitPrice);
	}

	public List findByIsTax(Object isTax) {
		return findByProperty(IS_TAX, isTax);
	}

	public List findByTaxCode(Object taxCode) {
		return findByProperty(TAX_CODE, taxCode);
	}

	public List findByOriginalPrice(Object originalPrice) {
		return findByProperty(ORIGINAL_PRICE, originalPrice);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByLastUpdatedBy(Object lastUpdatedBy) {
		return findByProperty(LAST_UPDATED_BY, lastUpdatedBy);
	}

	public List findByIndexNo(Object indexNo) {
		return findByProperty(INDEX_NO, indexNo);
	}

	public List findAll() {
		log.debug("finding all ImItemPrice instances");
		try {
			String queryString = "from ImItemPrice";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ImItemPrice merge(ImItemPrice detachedInstance) {
		log.debug("merging ImItemPrice instance");
		try {
			ImItemPrice result = (ImItemPrice) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ImItemPrice instance) {
		log.debug("attaching dirty ImItemPrice instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ImItemPrice instance) {
		log.debug("attaching clean ImItemPrice instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ImItemPriceDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ImItemPriceDAO) ctx.getBean("imItemPriceDAO");
	}

	/**
	 * 找出啟用日相同、品號相同的價格記錄
	 *
	 * @param brandCode
	 * @param itemCode
	 * @param priceType
	 * @param beginDate
	 * @return
	 */
	public ImItemPrice findEnablePrice(String itemCode, String priceType, java.util.Date beginDate, String brand) {
		System.out.println("品牌：" + brand + "，品號：" + itemCode + "，啟用日期：" + beginDate);

		if (StringUtils.hasText(itemCode)) {
			itemCode = itemCode.toUpperCase();
		}
		if (StringUtils.hasText(priceType)) {
			priceType = priceType.toUpperCase();
		}
		final String fItemCode = itemCode;
		final String fPriceType = priceType;
		final java.util.Date fBeginDate = beginDate;
		final String brandCode = brand;
		List<ImItemPrice> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("from ImItemPrice as model where 1=1 ");

				hql.append(" and model.id.itemCode  = :itemCode ");
				hql.append(" and model.typeCode  = :priceType ");
				hql.append(" and model.beginDate  = :beginDate ");
				hql.append(" and model.brandCode  = :brandCode "); // add by Weichun 2011.06.10
				Query query = session.createQuery(hql.toString());
				query.setLockMode("model", LockMode.UPGRADE_NOWAIT);

				query.setString("itemCode", fItemCode);
				query.setString("priceType", fPriceType);
				query.setDate("beginDate", fBeginDate);
				query.setString("brandCode", brandCode);// add by Weichun 2011.06.10

				return query.list();
			}
		});
		if (result.size() > 0)
			return result.get(0);
		else
			return null;

	}


	/**
	 * 依據品號、價格類型、開始日期、啟用狀態查詢出價格
	 *
	 * @param itemCode
	 * @param priceType
	 * @param beginDate
	 * @param enable
	 * @return List
	 */
/*	public List getItemPriceByBeginDate(final String itemCode, final String priceType,
	        final Date beginDate, final String enable){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select price.item_code, price.unit_price, item.is_compose_item, item.is_service_item from erp.im_item_price price, erp.im_item item, " +
						"(select item_code, type_code, max(begin_date) as begin_date from erp.im_item_price where item_code = :itemCode" +
						" and type_code = :priceType and begin_date <= :beginDate");
				if (StringUtils.hasText(enable)){
				    hql.append(" and enable = :actualPriceEnable");
				}
				hql.append(" group by item_code, type_code) actual_price");
				hql.append(" where item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				if (StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :itemEnable");
				}

				Query query = session.createSQLQuery(hql.toString());
				query.setString("itemCode", itemCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				if (StringUtils.hasText(enable)){
				    query.setString("actualPriceEnable", enable);
				    query.setString("itemEnable", enable);
				}
				return query.list();
			    }
			});

		return result;
	}*/

	/**
	 * 依據品號、價格類型、開始日期、啟用狀態查詢出價格
	 *
	 * @param itemCode
	 * @param priceType
	 * @param beginDate
	 * @param enable
	 * @return List
	 */
	public List getItemPriceByBeginDate(final String brandCode, final String itemCode, final String priceType,
	        final Date beginDate, final String enable){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select price.item_code, price.unit_price, item.is_compose_item, item.is_service_item from erp.im_item_price price, erp.im_item item, " +
						"(select item_code, type_code, max(begin_date) as begin_date " +
						"   from erp.im_item_price "+
						"  where brand_code = :brandCode "+
						"    and item_code = :itemCode" +
						"    and type_code = :priceType and begin_date <= :beginDate");
				if (StringUtils.hasText(enable)){
				    hql.append(" and enable = :actualPriceEnable");
				}
				hql.append(" group by item_code, type_code) actual_price");
				hql.append(" where item.item_code = price.item_code");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				if (StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :itemEnable");
				}

				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("itemCode", itemCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				if (StringUtils.hasText(enable)){
				    query.setString("actualPriceEnable", enable);
				    query.setString("itemEnable", enable);
				}
				return query.list();
			    }
			});

		return result;
	}

	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getItemPriceByCondition(final String brandCode, final String priceType,
	        final Date beginDate, final String dataDate, final String dataDateEnd, final String enable, final String isFindUpdate){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, price.last_update_date, item.category01, item.category02, item.category13, item.vip_discount from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, max(begin_date) as begin_date from erp.im_item_price where brand_code = :brandCode and enable = :enable" +
						" and type_code = :priceType and begin_date <= :beginDate");
				hql.append(" group by brand_code, item_code, type_code) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				hql.append(" and item.enable = :enable");
				if("Y".equals(isFindUpdate)){
				    hql.append(" and ((to_char(price.last_update_date, 'YYYYMMDD') >= :dataDate");
				    hql.append(" and to_char(price.last_update_date, 'YYYYMMDD') <= :dataDateEnd) or");
				    hql.append(" (to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate");
				    hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd) ");
				    hql.append(" or (actual_price.begin_date = :beginDate))");
				}
			        hql.append(" order by item.item_code");

				Query query = session.createSQLQuery(hql.toString());
				query.setString("enable", enable);
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				query.setDate("beginDate", beginDate);

				return query.list();
			    }
			});

		return result;
	}


	/**
	 * 依據品號、價格類型、開始日期、啟用狀態查詢出下一個調價日期
	 *
	 * @param itemCode
	 * @param priceType
	 * @param beginDate
	 * @param enable
	 * @return List
	 */
	public ImItemPrice getItemPriceByNextBeginDate( String itemCode, String priceType,
	        					Date beginDate,  String enable) {

	    if(StringUtils.hasText(itemCode)){
		itemCode = itemCode.toUpperCase();
	    }
	    if(StringUtils.hasText(priceType)){
		priceType = priceType.toUpperCase();
	    }
	    if(StringUtils.hasText(enable)){
		enable = enable.toUpperCase();
	    }
	    final String fItemCode = itemCode;
	    final String fPriceType = priceType;
	    final String fEnable = enable;
	    final Date fBeginDate = beginDate;

	    List<ImItemPrice> result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {

					StringBuffer hql = new StringBuffer(
							"from ImItemPrice as model where 1=1 ");

					hql.append(" and model.id.itemCode = :itemCode ");
					hql.append(" and model.typeCode   = :priceType ");
					hql.append(" and model.beginDate > :beginDate ");
					if (StringUtils.hasText(fEnable)){
					    hql.append(" and model.enable = :Enable");
					}
					hql.append(" order by model.beginDate ");
					Query query = session.createQuery(hql.toString());
					query.setString("itemCode",  fItemCode);
					query.setString("priceType", fPriceType);
					query.setDate("beginDate",   fBeginDate);
					if (StringUtils.hasText(fEnable)){
					    query.setString("Enable", fEnable);
					}
					return query.list();
				}
			});
	    if (result.size() > 0)
		return result.get(0);
	    else
		return null;
	}

	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getItemInfoByCondition(final String brandCode, final String priceType,
	        final Date beginDate, final String dataDate, final String dataDateEnd, final String enable, final String isFindUpdate){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, item.enable, item.item_brand, item.category01, item.category02, " +
						"item.is_tax, item.vip_discount, nvl(item.min_ratio, 1), nvl(item.decl_ratio, 1), item.lot_control, item.foreign_Category, item.PAY_OLINE "  +
					        "from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, item_id, max(begin_date) as begin_date from erp.im_item_price where enable = 'Y' " +
						" and brand_code = :brandCode and type_code = :priceType and begin_date <= :beginDate");
				hql.append(" group by brand_code, item_code, type_code, item_id) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and price.item_id = actual_price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				if(StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :enable");
				}
				hql.append(" and ( ( to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate ");
				hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd )  or price.begin_date = :beginDate  ) ");
			        hql.append(" order by item.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				if(StringUtils.hasText(enable)){
				    query.setString("enable", enable);
				}
				return query.list();
			    }
			});

		return result;
	}
	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getECTotalItemInfoByCondition(final String brandCode, final String priceType,
	        final Date beginDate, final String dataDate, final String dataDateEnd, final String enable, final String isFindUpdate){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, item.enable, item.item_brand, item.category01, item.category02, " +
						"item.is_tax, item.vip_discount, nvl(item.min_ratio, 1), nvl(item.decl_ratio, 1), item.lot_control, item.foreign_Category "  +
					        "from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, item_id, max(begin_date) as begin_date from erp.im_item_price where enable = 'Y' " +
						" and brand_code = :brandCode and type_code = :priceType and begin_date <= :beginDate");
				hql.append(" group by brand_code, item_code, type_code, item_id) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and price.item_id = actual_price.item_id"); 
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and item.EStore = 'Y' ");
				hql.append(" and price.brand_code = :brandCode");
				if(StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :enable");
				}
			    hql.append(" order by item.item_code");
			    Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
 
				if(StringUtils.hasText(enable)){
				    query.setString("enable", enable);
				}
				log.info("變價商品查詢:"+hql.toString());
				log.info("筆數:"+query.list().size());
				return query.list();
			    }
			});

		return result;
	}
	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getECItemInfoByCondition(final String brandCode, final String priceType,
	        final Date beginDate, final String dataDate, final String dataDateEnd, final String enable, final String isFindUpdate){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, item.enable, item.item_brand, item.category01, item.category02, " +
						"item.is_tax, item.vip_discount, nvl(item.min_ratio, 1), nvl(item.decl_ratio, 1), item.lot_control, item.foreign_Category "  +
					        "from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, item_id, max(begin_date) as begin_date from erp.im_item_price where enable = 'Y' " +
						" and brand_code = :brandCode and type_code = :priceType and begin_date <= :beginDate");
				hql.append(" group by brand_code, item_code, type_code, item_id) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and price.item_id = actual_price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and item.EStore = 'Y' ");
				hql.append(" and price.brand_code = :brandCode");
				if(StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :enable");
				}
				hql.append(" and ( ( to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate ");
				hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd )  or price.begin_date = :beginDate  ) ");
			        hql.append(" order by item.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				if(StringUtils.hasText(enable)){
				    query.setString("enable", enable);
				}
				log.info("變價商品查詢:"+hql.toString());
				log.info("筆數:"+query.list().size());
				return query.list();
			    }
			});

		return result;
	}
	public List getItemInfoByCondition1(final String brandCode, final String priceType,
	        final Date beginDate, final String dataDate, final String dataDateEnd, final String enable, final String isFindUpdate){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, price.unit_price, " +
						"item.is_compose_item, item.is_service_item, item.enable, item.item_brand, item.category01, item.category02, " +
						"item.is_tax, item.vip_discount, nvl(item.min_ratio, 1), nvl(item.decl_ratio, 1), item.lot_control, item.foreign_Category "  +
					        "from erp.im_item_price price, erp.im_item item, " +
						"(select brand_code, item_code, type_code, item_id, max(begin_date) as begin_date from erp.im_item_price where enable = 'Y'" +
						" and brand_code = :brandCode and type_code = :priceType and begin_date <= :beginDate");
				hql.append(" group by brand_code, item_code, type_code, item_id) actual_price");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and price.item_id = actual_price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and price.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				hql.append(" and item.EStore = 'Y'");
				if(StringUtils.hasText(enable)){
				    hql.append(" and item.enable = :enable");
				}
				hql.append(" and ( ( to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate ");
				hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd )  or price.begin_date = :beginDate  ) ");
			        hql.append(" order by item.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				if(StringUtils.hasText(enable)){
				    query.setString("enable", enable);
				}
				return query.list();
			    }
			});

		return result;
	}

	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getItemTagInfoByCondition(final String brandCode, final String dataDate, final String dataDateEnd){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("");
				
				hql.append(" SELECT TO_CHAR (price.begin_date, 'MMddhhmm'), ");
				hql.append(" NVL (ean.ean_code, item.item_code), ");
				hql.append(" price.item_code, ");
				hql.append(" item.category01, ");
				hql.append(" item.item_c_name, ");
				hql.append(" item.item_c_name, ");
				hql.append(" price.unit_price, ");
				hql.append(" ROUND (price.unit_price / rp1.exchange_rate, 2) AS exchange_usd, ");
				hql.append(" ROUND (price.unit_price / rp2.exchange_rate, 2) AS exchange_hdk, ");
				hql.append(" 0 AS exchange_d3, ");
				hql.append(" 0 AS exchange_d4, ");
				hql.append(" 0 AS exchange_d5 ");
				
				hql.append(" FROM im_item_price price, ");
				hql.append(" (  SELECT brand_code, ");
				hql.append(" item_code, ");
				hql.append(" type_code, ");
				hql.append(" item_id, ");
				hql.append(" MAX (begin_date) AS begin_date ");
				hql.append(" FROM erp.im_item_price ");
				hql.append(" WHERE     enable = 'Y' ");
				hql.append(" AND brand_code = :brandCode ");
				hql.append(" AND type_code = '1' ");
				//hql.append(" AND begin_date >= TO_DATE (:dataDate, 'yyyymmdd') ");
				hql.append(" AND begin_date <= TO_DATE (:dataDateEnd, 'yyyymmdd') ");
				hql.append(" GROUP BY brand_code, ");
				hql.append(" item_code, ");
				hql.append(" type_code, ");
				hql.append(" item_id) actual_price, ");
				hql.append(" erp.im_item item, ");
				hql.append(" erp.im_item_eancode ean, ");
				hql.append(" (  SELECT source_currency, ");
				hql.append(" against_currency, ");
				hql.append(" MAX (begin_date) AS begin_date ");
				hql.append(" FROM bu_exchange_rate ");
				hql.append(" WHERE begin_date <= TO_DATE (:dataDateEnd, 'yyyymmdd') ");
				hql.append(" AND organization_code = 'T2' ");
				hql.append(" AND source_currency = 'USD' ");
				hql.append(" AND against_currency = 'NTD' ");
				hql.append(" GROUP BY source_currency, against_currency) rd1, ");
				hql.append(" erp.bu_exchange_rate rp1, ");
				hql.append(" (  SELECT source_currency, ");
				hql.append(" against_currency, ");
				hql.append(" MAX (begin_date) AS begin_date ");
				hql.append(" FROM bu_exchange_rate ");
				hql.append(" WHERE begin_date <= TO_DATE (:dataDateEnd, 'yyyymmdd') ");
				hql.append(" AND organization_code = 'T2' ");
				hql.append(" AND source_currency = 'CNY' ");
				hql.append(" AND against_currency = 'NTD' ");
				hql.append(" GROUP BY source_currency, against_currency) rd2, ");
				hql.append(" erp.bu_exchange_rate rp2 ");
			       
				hql.append(" WHERE     item.item_id = price.item_id ");
				hql.append(" AND item.brand_code = :brandCode ");
				hql.append(" AND price.item_id = actual_price.item_id ");
				hql.append(" AND price.item_code = actual_price.item_code ");
				hql.append(" AND price.type_code = actual_price.type_code ");
				hql.append(" AND price.begin_date = actual_price.begin_date ");
				hql.append(" AND item.brand_code = price.brand_code(+) ");
				hql.append(" AND item.item_code = price.item_code(+) ");
				hql.append(" AND item.brand_code = ean.brand_code(+) ");
				hql.append(" AND item.item_code = ean.item_code(+) ");
				hql.append(" AND price.brand_code(+) = :brandCode ");
				hql.append(" AND ean.brand_code(+) = :brandCode ");
				hql.append(" AND ean.enable = 'Y' ");
				hql.append(" AND rp1.organization_code = 'T2' ");
				hql.append(" AND rd1.source_currency = rp1.source_currency ");
				hql.append(" AND rd1.against_currency = rp1.against_currency ");
				hql.append(" AND rd1.begin_date = rp1.begin_date ");
				hql.append(" AND rp2.organization_code = 'T2' ");
				hql.append(" AND rd2.source_currency = rp2.source_currency ");
				hql.append(" AND rd2.against_currency = rp2.against_currency ");
				hql.append(" AND rd2.begin_date = rp2.begin_date ");
				hql.append(" AND item.category01 = '09' ");
				log.info("hql = " + hql.toString());

				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				//query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				return query.list();
			    }
			});

		return result;
	}
	
	/**
	 * 依brandCode, itemCode 取得單價
	 * @param itemCode
	 * @return
	 * @throws ValidationErrorException
	 */
	public Double getOldestUnitPrice(String brandCode, String itemCode) throws ValidationErrorException, Exception{
		try {
			String queryString = "from ImItemPrice as model where brandCode = ? and item_code = ? and enable = 'Y' Order BY beginDate desc";
			List<ImItemPrice> imItemPrices = getHibernateTemplate().find(queryString, new Object[] { brandCode, itemCode } );
			if( imItemPrices.size() > 0 ){
				return imItemPrices.get(0).getUnitPrice();
			}
			return 0D;
		} catch (Exception ex) {
			log.error("依brandCode, itemCode 取得單價，原因：" + ex.toString());
			throw new Exception("依brandCode, itemCode 取得單價失敗！");
		}
	}

	/**
	 * 依據品牌、價格類型、開始日期、更新日期、啟用狀態查詢出離島商品資訊
	 *
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List getIslandItemInfoByCondition(final String brandCode, final String priceType, final Date beginDate,
		final String dataDate, final String dataDateEnd, final String customsWarehouseCode){

		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select item.brand_code, price.item_code, item.item_c_name, item.item_e_name, price.type_code, item.sales_unit, " +
														" price.unit_price, item.is_compose_item, item.is_service_item, item.enable, item.item_brand, item.category01, " +
														" item.category02, item.is_tax, item.vip_discount, nvl(item.min_ratio, 1), nvl(item.decl_ratio, 1), item.lot_control " +
														" from erp.im_item_price price, erp.im_item item,  " +
														" (select brand_code, item_code, type_code, item_id, max(begin_date) as begin_date from erp.im_item_price where enable = 'Y' " +
															" and brand_code = :brandCode and type_code = :priceType and begin_date <= :beginDate " +
															" group by brand_code, item_code, type_code, item_id) actual_price ");
				hql.append(" where item.item_id = price.item_id");
				hql.append(" and price.item_id = actual_price.item_id");
				hql.append(" and item.brand_code = price.brand_code");
				hql.append(" and item.item_code = price.item_code");
				hql.append(" and item.item_code = actual_price.item_code");
				hql.append(" and price.type_code = actual_price.type_code");
				hql.append(" and price.begin_date = actual_price.begin_date");
				hql.append(" and item.brand_code = :brandCode");
				hql.append(" and price.brand_code = :brandCode");
				//hql.append(" and item.item_code = warehouseItems.item_code");
				hql.append(" and ( (actual_price.begin_date = :beginDate) or ");
			    hql.append(" (to_char(price.last_update_date, 'YYYYMMDD') >= :dataDate");
			    hql.append(" and to_char(price.last_update_date, 'YYYYMMDD') <= :dataDateEnd) or");
			    hql.append(" (to_char(item.last_update_date, 'YYYYMMDD') >= :dataDate");
			    hql.append(" and to_char(item.last_update_date, 'YYYYMMDD') <= :dataDateEnd) )");
			    hql.append(" order by item.item_code");
			    //System.out.println("hql.toString() = " + hql.toString());
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setDate("beginDate", beginDate);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				//query.setString("customsWarehouseCode", customsWarehouseCode);
				return query.list();
			    }
			});

		return result;
	}

	public List getLatestByItemCode(final String itemCode) {

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {																																																															

				StringBuffer hql = new StringBuffer("select unit_price from erp.Im_Item_Price where item_Code = :itemCode order by last_Update_Date");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("itemCode", itemCode);
				System.out.println("======hql=="+query);
				try{
				System.out.println("======hql=="+query.list());
				}catch(Exception ex){System.out.println(ex.toString());}
				return query.list();
			}
		});

		return result;
	}
	
	public List<ImItemPriceView> getImItemPriceViews(String brandCode, List<ImReceiveItem> items){
		if(items == null || items.size() == 0) return null; 
		StringBuffer hql = new StringBuffer("from ImItemPriceView price where price.itemCode in (");
		for(int i=0; i<items.size(); i++)
			hql.append("'").append(items.get(i).getItemCode()).append("'").append((i<items.size()-1) ? " , " : ")");
		
		hql.append(" and price.brandCode=?");
		
		return (List<ImItemPriceView>)getHibernateTemplate().find(hql.toString(), new Object[]{brandCode});
	}
}