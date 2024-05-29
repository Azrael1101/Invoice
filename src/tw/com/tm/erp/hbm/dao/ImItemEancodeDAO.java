package tw.com.tm.erp.hbm.dao;

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

import tw.com.tm.erp.hbm.bean.ImItemEancode;

public class ImItemEancodeDAO extends BaseDAO {
    
    private static final Log log = LogFactory.getLog(ImItemEancodeDAO.class);
    
    /**
     * 依據品牌及品號查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return List<ImItemEancode>
     */
    public List<ImItemEancode> findByBrandCodeAndItemCode(String brandCode, String itemCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEancode as model where model.brandCode = ? and model.itemCode = ?");
	List<ImItemEancode> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, itemCode});
	return result;
    }
    
    /**
     * 依據品牌及國際碼查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return ImItemEancode
     */
    public ImItemEancode findByBrandCodeAndEanCode(String brandCode, String eanCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEancode as model where model.brandCode = ? and model.eanCode = ?");
	List<ImItemEancode> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, eanCode});
	return (result != null && result.size() > 0) ? result.get(0) : null;
    }

    /**
     * 依據品牌及國際碼查詢多筆eanCode
     * 
     * @param brandCode
     * @param itemCode
     * @return ImItemEancode
     */
    public List<ImItemEancode> findByEanCodes(String brandCode, String eanCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEancode as model where model.brandCode = ? and model.eanCode = ?");
	return findByProperty("ImItemEancode", "and brandCode = ? and eanCode = ? ", new Object[]{brandCode, eanCode});
    }
    
    /**
     * 依據品牌及(品號 or 國際碼)查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return ImItemEancode
     */
    public ImItemEancode getItemInfoByProperty(String brandCode, String itemCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEancode as model where model.brandCode = ? and (model.itemCode = ? or model.eanCode = ?)");
	List<ImItemEancode> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, itemCode, itemCode});
	return (result != null && result.size() > 0) ? result.get(0) : null;
    }
    
    /**
     * 依據品牌、最後更新日期區間查詢
     * 
     * @param conditionMap
     * @return List<ImItemEancode>
     */
    public List<ImItemEancode> findEanCodeListByProperty(HashMap conditionMap) {
	final String brandCode = (String) conditionMap.get("brandCode");
	final String dataDate = (String) conditionMap.get("dataDate");
	final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
	List<ImItemEancode> result = getHibernateTemplate().executeFind(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			StringBuffer hql = new StringBuffer("select model from ImItemEancode as model ");
			hql.append("where model.brandCode = :brandCode ");
			hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
			hql.append("and to_char(model.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd");
			Query query = session.createQuery(hql.toString());
			query.setString("brandCode", brandCode);
			query.setString("dataDate", dataDate);
			query.setString("dataDateEnd", dataDateEnd);
			return query.list();
		}
	});

	return result;
    }
   
    /**
     * 依lineId,撈出一筆 
     * @param lineId
     * @return
     */
    public ImItemEancode findById(Long lineId){
    	return (ImItemEancode)findByPrimaryKey(ImItemEancode.class, lineId);
    }
    
    /**
     * 依據品牌及國際碼查詢多筆
     * @param brandCode
     * @param eanCode
     * @return
     */
    public List<ImItemEancode> findEanCodeByProperty(String brandCode, String eanCode){
    	return (List<ImItemEancode>)findByProperty("ImItemEancode", "and brandCode = ? and eanCode = ? and enable = ? ", new Object[]{brandCode, eanCode, "Y"} );
    }
    
    
    /**
     * 依據品牌及國際碼,品號查詢1筆
     * @param brandCode
     * @param eanCode 打品號,檢查有國際碼相同
     * @param itemCode 
     * @return
     */
    public ImItemEancode findOneEanCodeByProperty(String brandCode, String eanCode, String itemCode){
    	return (ImItemEancode)findFirstByProperty("ImItemEancode", "and brandCode = ? and eanCode = ? and itemCode != ? and enable = ?", new Object[]{brandCode, eanCode,itemCode,"Y"});
    }
    
    /**
     * 依據品牌,品號,國際碼查詢一筆
     * @param brandCode
     * @param eanCode
     * @return
     */
    public ImItemEancode findEanCodeByProperty(String brandCode, String itemCode, String eanCode){
    	return (ImItemEancode)findFirstByProperty("ImItemEancode", "and brandCode = ? and itemCode = ? and eanCode = ?", new Object[]{brandCode, itemCode, eanCode} );
    }

    /**
	 * 依據品牌、開始日期、更新日期、啟用狀態查詢出離島商品條碼資訊
	 * 
	 * @param brandCode
	 * @param priceType
	 * @param beginDate
	 * @param lastUpdateDate
	 * @param enable
	 * @param isFindUpdate
	 * @return List
	 */
	public List findIslandEanCodeListByProperty(final String brandCode, final String dataDate, final String dataDateEnd, final String customsWarehouseCode){
		
		List result = getHibernateTemplate().executeFind(
			new HibernateCallback() {
			    public Object doInHibernate(Session session)
				    throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(" select eanCode.item_Code , eanCode.ean_code, eanCode.enable from IM_ITEM_EANCODE eanCode," +
													" (SELECT   DISTINCT (item_code) FROM   im_warehouse, im_on_hand WHERE   " +
														" CUSTOMS_WAREHOUSE_CODE = :customsWarehouseCode and im_warehouse.WAREHOUSE_CODE = im_on_hand.WAREHOUSE_CODE) warehouseItems" +
													" where eanCode.brand_code = :brandCode AND warehouseItems.item_code = eanCode.item_code");
				Query query = session.createSQLQuery(hql.toString());				
				query.setString("brandCode", brandCode);
				query.setString("customsWarehouseCode", customsWarehouseCode);
				return query.list();
			    }
			});

		return result;
		/*
		SELECT   eanCode.item_Code , eanCode.ean_code, eanCode.enable
		  FROM   IM_ITEM_EANCODE eanCode,
		         (SELECT   DISTINCT (item_code)
		            FROM   im_warehouse, im_on_hand
		           WHERE   CUSTOMS_WAREHOUSE_CODE = 'FD'
		                   AND im_warehouse.WAREHOUSE_CODE = im_on_hand.WAREHOUSE_CODE) warehouseItems
		 WHERE eanCode.brand_code = 'T2'
		         AND warehouseItems.item_code = eanCode.item_code*/
	}
	
	/**
	 * 依據品牌、開始日國際碼查出對應商品品號
	 * @param brandCode
	 * @param eanCode
	 * @return Itemcode
	 */
	public String getOneItemCodeByProperty(String brandCode, String eanCode){
		return (String)findFirstByProperty("ImItemEancode", "itemCode", "and brandCode = ? and eanCode = ? and enable = ? ", new Object[]{brandCode, eanCode, "Y"}, "" );
	}  

}