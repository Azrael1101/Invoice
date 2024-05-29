package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
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

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuVipPromote;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;

public class ImItemCategoryDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(ImItemCategoryDAO.class);

    public static final String ITEM_BRAND = "ItemBrand";
    public static final String CATEGORY00 = "CATEGORY00";	// 業種
    public static final String ITEM_CATEGORY = "ITEM_CATEGORY"; // 業種子類
    public static final String CATEGORY01 = "CATEGORY01"; // 大類
    public static final String CATEGORY02 = "CATEGORY02"; // 中類
    public static final String CATEGORY03 = "CATEGORY03"; // 小類
    public static final String CATEGORY04 = "CATEGORY04"; // 尺寸
    public static final String CATEGORY05 = "CATEGORY05"; // 年份
    public static final String CATEGORY06 = "CATEGORY06"; // 季別
    public static final String CATEGORY07 = "CATEGORY07"; // 性別
    public static final String CATEGORY08 = "CATEGORY08"; // 材質
    public static final String CATEGORY09 = "CATEGORY09"; // 款式
    public static final String CATEGORY10 = "CATEGORY10"; // 顏色
    public static final String CATEGORY11 = "CATEGORY11"; // 款式編號 
    public static final String CATEGORY12 = "CATEGORY12"; // 屬性    
    public static final String CATEGORY13 = "CATEGORY13"; // 系列   
    public static final String CATEGORY14 = "CATEGORY14"; // 產地    
    public static final String CATEGORY15 = "CATEGORY15"; // 功能    
    public static final String CATEGORY16 = "CATEGORY16"; // 樣本編號
    public static final String CATEGORY17 = "CATEGORY17"; // 製造商  
    public static final String CATEGORY18 = "CATEGORY18"; // 其他1   
    public static final String CATEGORY19 = "CATEGORY19"; // 其他2   
    public static final String CATEGORY20 = "CATEGORY20"; // 分類20
    
    
    public List<ImItemCategory> findMainCategoryByBrandCode(String brandCode,
	    String categoryType) {
	log.debug("finding ImItemCategory by BrandCode");
	try {

	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
	    hql.append("where model.id.brandCode = ? ");
	    hql.append("and model.id.categoryType = ? ");
	    hql.append("and model.categoryLevel = ? ");

	    return getHibernateTemplate().find(hql.toString(),
		    new Object[] { brandCode, categoryType, 1L });
	} catch (RuntimeException re) {
	    log.error("find ImItemCategory by BrandCode failed", re);
	    throw re;
	}
    }
    
    public List<ImItemCategory> findByCategoryTypeAndCategory(String brandCode,
    	    String categoryType,String parentCategoryCode) {
    	log.debug("finding ImItemCategory by BrandCode");
    	try {

    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and model.id.categoryType = ? ");
    	    hql.append("and model.enable = ? ");
    	    hql.append("and model.parentCategoryCode = ? ");
    	    hql.append("order by category_Code");
    	    
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { brandCode, categoryType,"Y" ,parentCategoryCode });
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
        }
    
    public List<BuVipPromote> findBuVipPromote(String customerCode) {
    	log.debug("finding BuVipPromote by customerCode");
    	try {

    	    StringBuffer hql = new StringBuffer("from BuVipPromote  ");
    	    hql.append("where customerCode = ? ");
    	   
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { customerCode });
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
        }

    public List<ImItemCategory> findByCategoryType(String brandCode,
	    String categoryType) {
	log.debug("finding ImItemCategory by BrandCode");
	try {

	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
	    hql.append("where model.id.brandCode = ? ");
	    hql.append("and model.id.categoryType = ? ");
	    hql.append("and model.enable = ? ");
	    hql.append("order by category_Code");
	    
	    return getHibernateTemplate().find(hql.toString(),
		    new Object[] { brandCode, categoryType,"Y" });
	} catch (RuntimeException re) {
	    log.error("find ImItemCategory by BrandCode failed", re);
	    throw re;
	}
    }
    
    public List<ImItemCategory> findByCategoryCode(String brandCode,
    	    String categoryCode) {
    	log.debug("finding ImItemCategory by CategoryCode:"+categoryCode);
    	try {

    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and model.id.categoryCode = ? ");
    	    hql.append("and model.enable = ? ");
    	    
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { brandCode, categoryCode,"Y" });
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
        }
    
    public List<ImItemCategory> findByCategoryType(String brandCode,
    	    String categoryType, String order) {
    	log.debug("finding ImItemCategory by BrandCode");
    	try {

    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and model.id.categoryType = ? ");
    	    
    	    if(order.equalsIgnoreCase("ASC") ||  order.equalsIgnoreCase("DESC"))
    	    	hql.append(" order by model.id.categoryCode " + order);
    	    	
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { brandCode, categoryType});
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
    }

    public List<ImItemCategory> findByParentCategroyCode(String brandCode,
	    String parentCategroyCode) {
	log.debug("finding ImItemCategory by BrandCode and ParentCategroyCode");
	try {

	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
	    hql.append("where model.id.brandCode = ? ");
	    hql.append("and model.parentCategoryCode = ? ");

	    return getHibernateTemplate().find(hql.toString(),
		    new Object[] { brandCode, parentCategroyCode });
	} catch (RuntimeException re) {
	    log.error("find ImItemCategory by BrandCode failed", re);
	    throw re;
	}
    }

    public List<ImItemCategory> findByCategoryCodeParentCode(String brandCode,
	    String categoryCode, String parentCategroyCode) {
	log.debug("finding ImItemCategory by BrandCode and ParentCategroyCode");
	try {

	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
	    hql.append("where model.id.brandCode = ? ");
	    hql.append("and model.id.categoryCode = ? ");
	    hql.append("and model.parentCategoryCode = ? ");

	    return getHibernateTemplate()
		    .find(
			    hql.toString(),
			    new Object[] { brandCode, categoryCode,
				    parentCategroyCode });
	} catch (RuntimeException re) {
	    log.error("find ImItemCategory by BrandCode failed", re);
	    throw re;
	}
    }

   
    /**
     * 依據商品類別資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<ImItemCategory> findItemCategoryList(HashMap conditionMap) {

	final String brandCode = (String) conditionMap.get("brandCode");
	final String categoryType = (String) conditionMap.get("categoryType");
	final String categoryCode = (String) conditionMap.get("categoryCode");
	final String categoryName = (String) conditionMap.get("categoryName");
	final String enable = (String) conditionMap.get("enable");

	List<ImItemCategory> result = getHibernateTemplate().executeFind(
		new HibernateCallback() {
		    public Object doInHibernate(Session session)
			    throws HibernateException, SQLException {

			StringBuffer hql = new StringBuffer(
				"from ImItemCategory as model ");
			hql.append("where model.id.brandCode = :brandCode");

			if (StringUtils.hasText(categoryType))
			    hql.append(" and model.id.categoryType = :categoryType");

			if (StringUtils.hasText(categoryCode))
			    hql.append(" and model.id.categoryCode = :categoryCode");

			if (StringUtils.hasText(categoryName))
			    hql.append(" and model.categoryName = :categoryName");

			if (StringUtils.hasText(enable))
			    hql.append(" and model.enable = :enable");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(0);
			query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

			query.setString("brandCode", brandCode);

			if (StringUtils.hasText(categoryType))
			    query.setString("categoryType", categoryType);

			if (StringUtils.hasText(categoryCode))
			    query.setString("categoryCode", categoryCode);

			if (StringUtils.hasText(categoryName))
			    query.setString("categoryName", categoryName);

			if (StringUtils.hasText(enable))
			    query.setString("enable", enable);

			return query.list();
		    }
		});

	return result;
    }
    
    /**
     * 依據brandCode,categoryType,isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param isEnable
     * @return
     */
    public List<ImItemCategory> findCategoryByBrandCode(String brandCode,String categoryType, String isEnable) {
		try {

		    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
		    hql.append("where model.id.brandCode = ? ");
		    hql.append("and model.id.categoryType = ? ");
		    hql.append("and model.enable = ? ");

		    return getHibernateTemplate().find(hql.toString(),
			    new Object[] { brandCode, categoryType, isEnable });
		} catch (RuntimeException re) {
		    log.error("find ImItemCategory by BrandCode failed", re);
		    throw re;
		}
    }
    
    /**
     * 依據brandCode, categoryType, parentCategoryCode, isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param parentCategoryCode
     * @param enable
     * @return
     */
    public List<ImItemCategory> findByParentCategoryCode(String brandCode, String categoryType, String parentCategoryCode, String enable) {
    	return findByProperty("ImItemCategory", "and id.brandCode = ? and id.categoryType = ? and parentCategoryCode = ? and enable = ?", 
    			new Object[]{brandCode, categoryType, parentCategoryCode, enable} );
    }
    
    //所有商品品牌 
    public List<ImItemCategory> findAllBrand(String brandCode, String categoryType) {
    	
    	try {

    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and model.id.categoryType = ? ");
    	    hql.append("and model.enable = ? ");
    	    hql.append("order by model.id.categoryCode asc ");
    	    
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { brandCode, categoryType,"Y" });
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
    }
    
    /**
     * 依據brandCode, categoryType, categoryCode, isEnable撈出
     * @param brandCode
     * @param categoryType
     * @param categoryCode
     * @param enable
     * @return
     */
    public ImItemCategory findByCategoryCode(String brandCode, String categoryType, String categoryCode, String enable) {
    	return (ImItemCategory)findFirstByProperty("ImItemCategory", "and id.brandCode = ? and id.categoryType = ? and id.categoryCode = ? and enable = ?", 
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
    public ImItemCategory findByCategoryCode(String brandCode, String categoryType, String categoryCode, String parentCategoryCode, String enable) {
    	return (ImItemCategory)findFirstByProperty("ImItemCategory", "and id.brandCode = ? and id.categoryType = ? and id.categoryCode = ? and parentCategoryCode = ? and enable = ?", 
    			new Object[]{brandCode, categoryType, categoryCode, parentCategoryCode, enable} );
    }
    
    public List<ImItemCategory> findByParentCategroyCode(String brandCode, String categoryType,String parentCategroyCode) {
    	log.debug("finding ImItemCategory by BrandCode and ParentCategroyCode");
    	try {
    		List<ImItemCategory> result = new ArrayList(0);
    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and   model.id.categoryType = ? ");
    	    if(StringUtils.hasText(parentCategroyCode)){
    	    	hql.append("and model.parentCategoryCode = ? ");
    	    	hql.append("order by category_Code");
    	    	result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode,categoryType ,parentCategroyCode });
    	    }else{
    	    	hql.append("order by category_Code");
    	    	result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, categoryType });
    	    }
    	    log.info(categoryType+".size:"+result.size());
    	    log.info("brandCode:"+brandCode);
    	    log.info("parentCategroyCode:"+parentCategroyCode);
    	    
    	    return result;
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
        }

    public String findByCategoryCode(Object categoryCode) {
	StringBuffer hql = new StringBuffer("select categoryName from ImItemCategory as model ");
	hql.append("where category_code = ?");
	List result = getHibernateTemplate().find(hql.toString(),
		    new Object[] { categoryCode});
	System.out.println("======");
	String rtnVal = "";
	if(result.size() >0){
	    rtnVal =result.get(0) == null? "": (String)result.get(0);
	}
	return rtnVal;
    }
    //權限用()
    public List<ImItemCategory> findPageLine(int startPage, int pageSize) {
        final int startRecordIndexStar = startPage * pageSize;
        final int pSize = pageSize;
        final String enable = "Y";
    
        List<ImItemCategory> result = getHibernateTemplate().executeFind(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql = " from ImItemCategory M where M.enable=:enable";
                    Query query = session.createQuery(hql);
                    if(startRecordIndexStar > 0) query.setFirstResult(startRecordIndexStar);
                    if(pSize > 0) query.setMaxResults(pSize);
                    query.setParameter("enable", enable);
                    return query.list();
                }
            }); 
        return result;
    }
    
    public Long findPageLineCount(){
        final String enable = "Y";
        String hql = " select count(*) from ImItemCategory M where M.enable=?";
        Object obj = getHibernateTemplate().find(hql, new Object[]{enable});
        return obj == null ? 0L : (Long)(((List)obj).get(0));
    }
    
    public List<ImItemCategory> findByCategroyCode(String brandCode, String categoryCode) {
    	log.debug("finding ImItemCategory by BrandCode and categoryCode");
    	try {
    		List<ImItemCategory> result = new ArrayList(0);
    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and   model.id.categoryCode = ? ");
    	    
    	    	result = getHibernateTemplate().find(hql.toString(), new Object[] { brandCode, categoryCode });
    	    log.info(categoryCode+".size:"+result.size());
    	    log.info("brandCode:"+brandCode+"categoryCode:"+categoryCode);
    	    
    	    
    	    return result;
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode failed", re);
    	    throw re;
    	}
        }
    
    public List<ImItemCategory> findByCategoryTypeOrder(String brandCode, String categoryType, String orderBy) {
    	log.debug("finding ImItemCategory by BrandCode and categoryType order");
    	try {

    	    StringBuffer hql = new StringBuffer("from ImItemCategory as model ");
    	    hql.append("where model.id.brandCode = ? ");
    	    hql.append("and model.id.categoryType = ? ");
    	    hql.append("and model.enable = ? ");
    	    hql.append("order by " + orderBy);
    	    
    	    return getHibernateTemplate().find(hql.toString(),
    		    new Object[] { brandCode, categoryType,"Y" });
    	} catch (RuntimeException re) {
    	    log.error("find ImItemCategory by BrandCode and categoryType failed", re);
    	    throw re;
    	}
        }

}
