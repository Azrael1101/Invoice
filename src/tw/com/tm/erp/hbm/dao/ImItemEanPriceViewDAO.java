package tw.com.tm.erp.hbm.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;

public class ImItemEanPriceViewDAO extends BaseDAO {
    
    private static final Log log = LogFactory.getLog(ImItemEanPriceViewDAO.class);
    
    /**
     * 依據品牌及品號查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return List<ImItemEanPriceView>
     */
    public List<ImItemEanPriceView> findByBrandCodeAndItemCode(String brandCode, String itemCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEanPriceView as model where model.brandCode = ? and model.itemCode = ? and eanEnable = ? ");
	List<ImItemEanPriceView> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, itemCode, "Y"});
	return result;
    }
    
    /**
     * 依據品牌及國際碼查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return ImItemEanPriceView
     */
    public ImItemEanPriceView findById(String brandCode, String eanCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEanPriceView as model where model.brandCode = ? and model.eanCode = ? and eanEnable = ? ");
	List<ImItemEanPriceView> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, eanCode, "Y"});
	return (result != null && result.size() > 0) ? result.get(0) : null;
    }

    /**
     * 依據品牌及(品號 or 國際碼)查詢
     * 
     * @param brandCode
     * @param itemCode
     * @return ImItemEanPriceView
     */
    public ImItemEanPriceView getItemInfoByProperty(String brandCode, String itemCode){
	
	StringBuffer hql = new StringBuffer("from ImItemEanPriceView as model where model.brandCode = ? and (model.itemCode = ? or model.eanCode = ?) and eanEnable = ? ");
	List<ImItemEanPriceView> result = getHibernateTemplate().find(hql.toString(), new Object[]{brandCode, itemCode, itemCode, "Y"});
	return (result != null && result.size() > 0) ? result.get(0) : null;
    }
   
    /**
     * 依據品牌及(品號 or 國際碼)稅別 查詢
     * @param brandCode
     * @param itemCode
     * @param isTax
     * @return
     */
    public ImItemEanPriceView getItemInfoByProperty(String brandCode, String itemCode, String isTax){
    	return (ImItemEanPriceView)this.findFirstByProperty("ImItemEanPriceView", "and brandCode = ? and (itemCode = ? or eanCode = ?) and isTax = ? and eanEnable = ? ", 
				new Object[]{brandCode, itemCode, itemCode, isTax, "Y" });
    	
    }
    
}