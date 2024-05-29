package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.ImExcessivePromotionView;
import tw.com.tm.erp.hbm.bean.ImPromotionView;
import tw.com.tm.erp.utils.DateUtils;

public class ImPromotionViewDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(ImPromotionViewDAO.class);

	/**
	 * 依據品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型等條件查詢
	 * 
	 * @param conditionMap
	 * @return ImPromotionView
	 */
	public ImPromotionView findPromotionCodeByProperty(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String promotionCode = (String) conditionMap.get("promotionCode");
		final String priceType = (String) conditionMap.get("priceType");
		final String itemCode = (String) conditionMap.get("itemCode");
		final String shopCode = (String) conditionMap.get("shopCode");
		final String customerType = (String) conditionMap.get("customerType");
		final String vipType = (String) conditionMap.get("vipType");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model from ImPromotionView as model");
				hql.append(" where model.brandCode = :brandCode");
				hql.append(" and model.promotionCode = :promotionCode");
				hql.append(" and model.priceType = :priceType");
				hql.append(" and (model.shopCode is NULL or model.shopCode = :shopCode)");
				if (StringUtils.hasText(itemCode)) {
					hql.append(" and (model.itemCode is NULL or model.itemCode = :itemCode)");
				}
				if (StringUtils.hasText(customerType) && StringUtils.hasText(vipType)) {
					hql.append(" and (model.customerType is NULL or model.customerType = :customerType)");
					hql.append(" and (model.vipTypeCode is NULL or model.vipTypeCode = :vipType)");
				} else {
					hql.append(" and model.isAllCustomer = 'Y'");
				}

				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("promotionCode", promotionCode);
				query.setString("priceType", priceType);
				query.setString("shopCode", shopCode);
				if (StringUtils.hasText(itemCode)) {
					query.setString("itemCode", itemCode);
				}
				if (StringUtils.hasText(customerType) && StringUtils.hasText(vipType)) {
					query.setString("customerType", customerType);
					query.setString("vipType", vipType);
				}

				return query.list();
			}
		});

		return (result != null && result.size() > 0 ? (ImPromotionView) result.get(0) : null);
	}

	/**
	 * 依據促銷活動種類查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 */
	public List<ImPromotionView> findPromotionList(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String promotionCode_Start = (String) conditionMap.get("promotionCode_Start");
		final String promotionCode_End = (String) conditionMap.get("promotionCode_End");
		final String promotionName = (String) conditionMap.get("promotionName");
		final String shopCode = (String) conditionMap.get("shopCode");
		final String itemCode = (String) conditionMap.get("itemCode");
		final Date promotionDate_Begin = (Date) conditionMap.get("promotionDate_Begin");
		final Date promotionDate_End = (Date) conditionMap.get("promotionDate_End");
		final String discountType = (String) conditionMap.get("discountType");
		final String priceType = (String) conditionMap.get("priceType");
		final String customerType = (String) conditionMap.get("customerType");
		final String vipType = (String) conditionMap.get("vipType");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model from ImPromotionView as model");
				hql.append(" where model.brandCode = :brandCode");

				if (StringUtils.hasText(promotionCode_Start))
					hql.append(" and model.promotionCode >= :promotionCode_Start");

				if (StringUtils.hasText(promotionCode_End))
					hql.append(" and model.promotionCode <= :promotionCode_End");

				if (StringUtils.hasText(promotionName))
					hql.append(" and model.promotionName like :promotionName");

				if (StringUtils.hasText(shopCode))
					hql.append(" and (model.shopCode is NULL or model.shopCode = :shopCode)");

				if (StringUtils.hasText(itemCode))
					hql.append(" and (model.itemCode is NULL or model.itemCode = :itemCode)");

				if (StringUtils.hasText(discountType))
					hql.append(" and model.discountType = :discountType");

				if (StringUtils.hasText(priceType))
					hql.append(" and model.priceType = :priceType");

				if (StringUtils.hasText(customerType) && StringUtils.hasText(vipType)) {
					hql.append(" and (model.customerType is NULL or model.customerType = :customerType)");
					hql.append(" and (model.vipTypeCode is NULL or model.vipTypeCode = :vipType)");
				} else {
					hql.append(" and model.isAllCustomer = 'Y'");
				}

				if (promotionDate_Begin != null)
					hql.append(" and model.beginDate >= :promotionDate_Begin");

				if (promotionDate_End != null)
					hql.append(" and model.endDate <= :promotionDate_End");

				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				query.setString("brandCode", brandCode);

				if (StringUtils.hasText(promotionCode_Start))
					query.setString("promotionCode_Start", promotionCode_Start);

				if (StringUtils.hasText(promotionCode_End))
					query.setString("promotionCode_End", promotionCode_End);

				if (StringUtils.hasText(promotionName))
					query.setString("promotionName", "%" + promotionName + "%");

				if (StringUtils.hasText(shopCode))
					query.setString("shopCode", shopCode);

				if (StringUtils.hasText(itemCode))
					query.setString("itemCode", itemCode);

				if (StringUtils.hasText(discountType))
					query.setString("discountType", discountType);

				if (StringUtils.hasText(priceType))
					query.setString("priceType", priceType);

				if (StringUtils.hasText(customerType) && StringUtils.hasText(vipType)) {
					query.setString("customerType", customerType);
					query.setString("vipType", vipType);
				}

				if (promotionDate_Begin != null)
					query.setDate("promotionDate_Begin", promotionDate_Begin);

				if (promotionDate_End != null)
					query.setDate("promotionDate_End", promotionDate_End);

				return query.list();
			}
		});

		return result;
	}

	public List<ImPromotionView> findPromotionForPOS(HashMap conditionMap) {
		log.info("ImPromotionViewDAO.findPromotionForPOS" );
		final String brandCode = (String) conditionMap.get("brandCode");
		final Date promotionDate = (Date) conditionMap.get("promotionDate");

		log.info("ImPromotionViewDAO.findPromotionForPOS brandCode=" + brandCode + ",promotionDate=" + promotionDate );
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model from ImPromotionView as model");
				hql.append(" where model.brandCode = :brandCode");

				if (promotionDate != null)
					hql.append(" and model.beginDate >= :promotionDate and :promotionDate <= model.endDate ");

				Query query = session.createQuery(hql.toString());

				query.setString("brandCode", brandCode);

				if (promotionDate != null)
					query.setDate("promotionDate", promotionDate);

				return query.list();
			}
		});

		return result;
	}
	
	/**
	 * 依據銷售日期、品牌代號、活動代號、價格類別、品號、櫃號、客戶類型、會員類型等條件查詢
	 * 
	 * @param conditionMap
	 * @return ImPromotionView
	 */
        public Object[] findPromotionCodeByCondition(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String promotionCode = (String) conditionMap.get("promotionCode");
		final String priceType = (String) conditionMap.get("priceType");
		final String shopCode = (String) conditionMap.get("shopCode");
		final String customerType = (String) conditionMap.get("customerType");
		final String vipType = (String) conditionMap.get("vipType");
		final String salesDate = (String) conditionMap.get("salesDate");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("SELECT h.brand_code, h.promotion_code, h.promotion_name,");
				hql.append(" CASE WHEN h.is_all_shop = 'Y' THEN");
				hql.append(" CASE WHEN TO_DATE(:salesDate,'YYYYMMDD') BETWEEN h.begin_date AND h.end_date THEN :shopCode ELSE NULL END");
				hql.append(" ELSE (SELECT s.shop_code FROM im_promotion_shop s WHERE h.head_id = s.head_id and s.shop_code = :shopCode and  TO_DATE(:salesDate,'YYYYMMDD') BETWEEN s.begin_date AND s.end_date)");
				hql.append(" END shop_code,");
				hql.append(" CASE WHEN h.is_all_customer = 'Y' THEN 'VIP'");
				hql.append(" ELSE (SELECT c.vip_type_code FROM im_promotion_customer c WHERE h.head_id = c.head_id and c.vip_type_code = :vipType)");
				hql.append(" END vip_type_code");
				hql.append(" FROM im_promotion h");
				hql.append(" WHERE 1=1 AND h.brand_code = :brandCode AND h.promotion_code = :promotionCode");
				hql.append(" AND h.price_type = :priceType");		
				if (StringUtils.hasText(customerType)){
				    hql.append(" AND h.customer_type = :customerType");
				}

				Query query = session.createSQLQuery(hql.toString());
				query.setString("salesDate", salesDate);
				query.setString("shopCode", shopCode);
				query.setString("vipType", vipType);
				query.setString("brandCode", brandCode);
				query.setString("promotionCode", promotionCode);
				query.setString("priceType", priceType);
				if (StringUtils.hasText(customerType)){
				    query.setString("customerType", customerType);
				}

				return query.list();
			}
		});

		return (result != null && result.size() > 0 ? (Object[]) result.get(0) : null);
	}
        
        /**
         * 依據品牌代號、價格類型、活動日期、是否全部專櫃，查詢出下傳至POS的活動資料
         * 
         * @param brandCode
         * @param priceType
         * @param promotionDate
         * @param isAllShop
         * @return List
         */
        public List getPromotionItemInfo(final String brandCode, final String priceType, final String promotionDate, final String isAllShop) {

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH'");
				    hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') BETWEEN p.begin_date AND p.end_date ");
				}else{
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, price.unit_price,to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH'");
				    hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') BETWEEN s.begin_date AND s.end_date ");
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
				return query.list();
			}
		});
		return re ;
	}
        
        public List getPromotionItem(final String brandCode, final String priceType, final String promotionDate, final String isAllShop) {

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH'");
				    hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') BETWEEN p.begin_date AND p.end_date ");
				    System.out.println("=============Y==========");
				}else{
				    System.out.println("============N===========");
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, price.unit_price,to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH'");
				    hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') BETWEEN s.begin_date AND s.end_date ");
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
				return query.list();
			}
		});
		return re ;
	}
        /**
         * 依據品牌代號、價格類型、活動日期、是否全部專櫃、是否促銷起始日期，查詢出下傳至POS的活動資料
         * 
         * @param brandCode
         * @param priceType
         * @param promotionDate
         * @param isAllShop
         * @param isBeginDate
         * @return List
         */
        public List getPromotionItemData(final String brandCode, final String priceType, final String promotionDate, 
        	final String isAllShop, final String isBeginDate) {

		List re = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, i.total_discount_amount, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')"); // 
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode"); 
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH' " ); // and i.item_code='BR3463493F'
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.begin_date"); 			
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.end_date");
				    }
				}else{
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, i.total_discount_amount, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH' "); //and i.item_code='BR3463493F'  
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.begin_date");
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.end_date");
				    }
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
					
				return query.list();
			}
		});
//		List re = getHibernateTemplate().find(hql.toString(),new Object[]{priceType,promotionDate,brandCode,priceType,promotionDate,priceType,promotionDate,brandCode,brandCode,priceType,promotionDate });		
				
		return re ;
	}
        /**
         * 依據品牌代號、價格類型、活動日期、是否全部專櫃、是否促銷起始日期，查詢出下傳至POS的活動資料
         * 
         * @param brandCode
         * @param priceType
         * @param promotionDate
         * @param isAllShop
         * @param isBeginDate
         * @return List
         */
        public List getECPromotionItemData(final String brandCode, final String priceType, final String promotionDate, 
        	final String isAllShop, final String isBeginDate) {

		List re = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, i.total_discount_amount, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')"); // 
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode"); 
				    hql.append(" and item.EStore = 'Y'"); 
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH' " ); // and i.item_code='BR3463493F'
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.begin_date"); 			
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.end_date");
				    }
				}else{
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, i.total_discount_amount, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.EStore = 'Y'"); 
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH' "); //and i.item_code='BR3463493F'  
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.begin_date");
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.end_date");
				    }
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
					
				return query.list();
			}
		});
//		List re = getHibernateTemplate().find(hql.toString(),new Object[]{priceType,promotionDate,brandCode,priceType,promotionDate,priceType,promotionDate,brandCode,brandCode,priceType,promotionDate });		
				
		return re ;
	}
        
        /**
         * 依據品牌代號、價格類型、活動日期、是否全部專櫃、是否促銷起始日期，查詢出下傳至POS的活動資料
         * 
         * @param brandCode
         * @param priceType
         * @param promotionDate
         * @param isAllShop
         * @param isBeginDate
         * @return List
         */
        public List getExcessivePromotionItemData(Map parameterMap) {
//            	Date startDate = (Date)parameterMap.get("DATA_DATE_STRAT");
//    		Date endDate = (Date)parameterMap.get("DATA_DATE_END");
    	
            	final String brandCode = (String) parameterMap.get("BRAND_CODE");
            	final String priceType = "1"; 
            	final String promotionDate = DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD);
    		final String isAllShop = "Y";
    		final String isBeginDate = "Y";
    		
		List re = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, i.total_Discount_Amount as promotionPrice, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')"); // 
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode"); 
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH' " ); // and i.item_code='BR3463493F'
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.begin_date"); 			
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.end_date");
				    }
				}else{
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, i.totalDiscountAmount as promotionPrice,to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH' "); //and i.item_code='BR3463493F'  
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.begin_date");
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.end_date");
				    }
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
					
				return query.list();
			}
		});
				
		return re ;
	}
        
        /**
         * 依據品牌代號、價格類型、活動日期、是否全部專櫃、是否促銷起始日期，查詢出下傳至POS的活動資料
         * 
         * @param brandCode
         * @param priceType
         * @param promotionDate
         * @param isAllShop
         * @param isBeginDate
         * @return List
         */
        public List getPromotionItemData(final String brandCode, final String priceType, final String promotionDate, 
        	final String isAllShop, final String isBeginDate, final String itemCode) {

		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				if("Y".equals(isAllShop)){
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, p.last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH'");
				    hql.append(" and i.item_code = :itemCode ");
				    
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.begin_date");				
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = p.end_date");
				    }
				}else{
				    hql.append("select p.brand_code, p.promotion_code, s.shop_code, s.begin_date, s.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, p.last_update_date");
				    hql.append(" from im_promotion p, im_promotion_shop s, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");			    
				    hql.append(" where p.head_Id = s.head_id and p.head_Id = i.head_id and i.item_code = price.item_code");
				    hql.append(" and p.brand_code = :brandCode and p.price_type = :priceType and p.is_all_shop != 'Y' and p.status = 'FINISH'");
				    hql.append(" and i.item_code = :itemCode ");
				    if("Y".equals(isBeginDate)){
					hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.begin_date");
				    }else{
				        hql.append(" and TO_DATE(:promotionDate,'YYYYMMDD') = s.end_date");
				    }
				}
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				query.setString("promotionDate", promotionDate);
				query.setString("itemCode", itemCode);	
				return query.list();
			}
		});
		return re ;
	}
        
        
        /**
         * 取得促銷到期後依原價賣出的促銷商品.
         * 
         */
        public List getPromotionChangeItemData(final String brandCode, final String priceType, final String promotionDate, final String finishedPromotionDate
        	, final String currentDate, final String isAutoSchedule){
		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    if(isAutoSchedule.equals("Y")){
					hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    }else{
					hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:currentDate,'YYYYMMDD')");
				    }
				    
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH'");
			
				    hql.append(" and TO_DATE(:finishedPromotionDate,'YYYYMMDD') = p.end_date");
				
				
				
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				
				query.setString("finishedPromotionDate", finishedPromotionDate);
				if(isAutoSchedule.equals("Y")){
				    query.setString("promotionDate", promotionDate);
				}else{
				    query.setString("currentDate", currentDate);
				}
					
				return query.list();
			}
		});
		return re ;
        }
        
        /**
         * 取得促銷到期後依原價賣出的促銷商品.
         * 
         */
        public List getECPromotionChangeItemData(final String brandCode, final String priceType, final String promotionDate, final String finishedPromotionDate, final String currentDate, final String isAutoSchedule){
		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				
				    hql.append("select p.brand_code, p.promotion_code, null as shop_code, p.begin_date, p.end_date, i.item_code, i.discount_type, i.discount, price.unit_price, to_char(p.last_update_date,'YYYY-MM-DD hh24:mi:ss') as last_update_date");
				    hql.append(" from im_promotion p, im_promotion_item i,");
				    hql.append(" (select item.brand_code, price.item_code, price.unit_price from im_item_price price, im_item item,");
				    hql.append(" (select item_code, type_code, max(begin_date) as begin_date from im_item_price where enable = 'Y'");
				    if(isAutoSchedule.equals("Y")){
					hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:promotionDate,'YYYYMMDD')");
				    }else{
					hql.append(" and type_code = :priceType and begin_date <= TO_DATE(:currentDate,'YYYYMMDD')");
				    }
				    
				    hql.append(" group by item_code, type_code) actual_price");
				    hql.append(" where item.item_code = price.item_code");
				    hql.append(" and price.item_code = actual_price.item_code");
				    hql.append(" and price.type_code = actual_price.type_code");
				    hql.append(" and price.begin_date = actual_price.begin_date");
				    hql.append(" and item.brand_code = :brandCode");
				    hql.append(" and item.EStore = 'Y'");
				    hql.append(" and item.enable = 'Y') price");		    
				    hql.append(" where p.head_Id = i.head_id and i.item_code = price.item_code and p.brand_code = :brandCode");
				    hql.append(" and p.price_type = :priceType and p.is_all_shop = 'Y' and p.status = 'FINISH'");
			
				    hql.append(" and TO_DATE(:finishedPromotionDate,'YYYYMMDD') = p.end_date");
				
				
				
				hql.append(" order by i.item_code");
				Query query = session.createSQLQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("priceType", priceType);
				
				query.setString("finishedPromotionDate", finishedPromotionDate);
				if(isAutoSchedule.equals("Y")){
				    query.setString("promotionDate", promotionDate);
				}else{
				    query.setString("currentDate", currentDate);
				}
					
				return query.list();
			}
		});
		return re ;
        }
        
        
        /**         
         * 查詢有異動商品檔但仍為促銷價的品號ForPOS.
         * @param brandCode
         * @param shopCode
         * @param itemCode
         * @param orderDate
         * @return
         * @author 20100907 add by joeywu
         */
    	public List findImPromotionForPOS(final String brandCode, final String isAllShop, final String itemCode, final String actualDataDate) {
    		log.debug("findImPromotionForPOS");
    		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
    			public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		StringBuffer hql = new StringBuffer("select I.ITEM_CODE, H.BEGIN_DATE, H.END_DATE, I.TOTAL_DISCOUNT_AMOUNT,I.TOTAL_DISCOUNT_PERCENTAGE " +
    				" from IM_PROMOTION h, IM_PROMOTION_ITEM I, IM_ITEM t 	");
    		hql.append(" where h.head_id = i.head_id and h.status = 'FINISH' ");
    		hql.append(" and i.ITEM_CODE = t.ITEM_CODE ");
    	        hql.append(" and h.brand_Code = t.BRAND_CODE ");
    	        hql.append(" and h.brand_Code = :brandCode ");
    		hql.append(" and h.begin_Date <= TO_DATE(:actualDataDate,'YYYYMMDD') ");  // 日期帶系統日期可, 因為第二個促銷才會下下去真正的促銷, 這邊只考慮若商品還在促銷期間則以促銷最後修改後的單據第一筆為主
    		hql.append(" and h.end_Date >= TO_DATE(:actualDataDate,'YYYYMMDD')  ");
    		hql.append(" and i.item_Code = :itemCode ");
    		hql.append(" and h.is_all_shop = :isAllShop ");		
    		hql.append(" order by h.last_Update_Date desc ");			 // 防止同一天促銷無法辨識以誰為準	
    											
    		Query query = session.createSQLQuery(hql.toString());
    		query.setString("brandCode", brandCode);
    		query.setString("isAllShop", isAllShop);
    		query.setString("itemCode", itemCode);
    		query.setString("actualDataDate", actualDataDate);
    		return query.list();
    			}
    		});
    		return re ;
    	}
    	public List findImPromotionForEC(final String brandCode, final String isAllShop, final String itemCode, final String actualDataDate) {
    		log.debug("findImPromotionForPOS");
    		List re = getHibernateTemplate().executeFind(new HibernateCallback() {
    			public Object doInHibernate(Session session) throws HibernateException, SQLException {
    		StringBuffer hql = new StringBuffer("select I.ITEM_CODE, H.BEGIN_DATE, H.END_DATE, I.TOTAL_DISCOUNT_AMOUNT,I.TOTAL_DISCOUNT_PERCENTAGE " +
    				" from IM_PROMOTION h, IM_PROMOTION_ITEM I, IM_ITEM t 	");
    		hql.append(" where h.head_id = i.head_id and h.status = 'FINISH' ");
    		hql.append(" and i.ITEM_CODE = t.ITEM_CODE ");
    	        hql.append(" and h.brand_Code = t.BRAND_CODE ");
    	        hql.append(" and h.brand_Code = :brandCode ");
    		hql.append(" and h.begin_Date <= TO_DATE(:actualDataDate,'YYYYMMDD') ");  // 日期帶系統日期可, 因為第二個促銷才會下下去真正的促銷, 這邊只考慮若商品還在促銷期間則以促銷最後修改後的單據第一筆為主
    		hql.append(" and h.end_Date >= TO_DATE(:actualDataDate,'YYYYMMDD')  ");
    		hql.append(" and i.item_Code = :itemCode ");
    		hql.append(" and h.is_all_shop = :isAllShop ");		
    		hql.append(" and t.EStore = 'Y' ");	
    		hql.append(" order by h.last_Update_Date desc ");			 // 防止同一天促銷無法辨識以誰為準	
    											
    		Query query = session.createSQLQuery(hql.toString());
    		query.setString("brandCode", brandCode);
    		query.setString("isAllShop", isAllShop);
    		query.setString("itemCode", itemCode);
    		query.setString("actualDataDate", actualDataDate);
    		return query.list();
    			}
    		});
    		return re ;
    	}

	   public void updatePosExPromotion(HashMap paramterMap ,final ImExcessivePromotionView promotionView) {
	        final String brandCode = (String) paramterMap.get("brandCode");
		final String dataDateEnd = (String) paramterMap.get("dataDateEnd");
		final String uuId = (String)paramterMap.get("DATA_ID");
		
		getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
			    Double promotionPrice = promotionView.getOriginalPrice();
			        Double discount = promotionView.getDiscount();
			        String discountType = promotionView.getDiscountType();
			        if(StringUtils.hasText(discountType) && discount != null){
			    	log.info("discountType = " + discountType);
			    	log.info("promotionPrice = " + promotionPrice);
			    	log.info("discount = " + discount);
			    	
        			    	if("1".equals(discountType)){
        			    	    promotionPrice = promotionPrice - discount;
        			    	}else if("2".equals(discountType)){
        			    	    promotionPrice = promotionPrice * (100 - discount) / 100;
        			    	}
            			    	if(promotionPrice < 0D){
            			    	    promotionPrice = 0D;
            			    	}
			        }  	
			        String promtionDate = (DateUtils.format( (Date)promotionView.getBeginDate(), DateUtils.C_DATA_PATTON_YYYYMMDD)).toString();
                    		String sql = "INSERT INTO POS.POS_EXCESSIVE_PROMOTION(ACTION,BEGIN_DATE,DATA_ID,ITEM_CODE,PROMOTION_UNIT_PRICE) " +
                		"VALUES('U',TO_DATE('"+promtionDate+"','YYYYMMDD'),'"+uuId+"', '"+promotionView.getItemCode()+"'," +
               			"'"+promotionPrice+"')";
                		Query query = session.createSQLQuery(sql);
                		query.executeUpdate();
                		return null;
			}
		});
	        

	    }

}
