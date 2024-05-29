package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuPosBtnConfig;





public class BuPosBtnConfigDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuPosBtnConfigDAO.class);

/**利用品牌、函數名稱、啟用狀態尋找**/
    public BuPosBtnConfig findConfigByFunctionName(String brandCode, String functionCode, String enable) {
    	
    	StringBuffer hql = new StringBuffer("from BuPosBtnConfig as model ");
    	 	hql.append(" where 1=1 ");
    	 	hql.append(" and model.brandCode = ? ");
            hql.append(" and model.functionCode = ? ");
            hql.append(" and model.enable = ? ");
            List<BuPosBtnConfig> result = getHibernateTemplate().find(hql.toString(),
    		new Object[] { brandCode, functionCode,  enable});
    	return (result != null && result.size() > 0 ? result.get(0) : null);
	}
    /**主檔查詢**/
    public BuPosBtnConfig findById(Long headId) throws Exception {
		try {
			BuPosBtnConfig salesOrder = (BuPosBtnConfig)findByPrimaryKey(BuPosBtnConfig.class, headId);
			return salesOrder;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢主檔時發生錯誤，原因："+ ex.getMessage());
		}
	}
    
    public List<BuPosBtnConfig> findConfig(String brandCode, String enable){
    	
    	StringBuffer hql = new StringBuffer("from BuPosBtnConfig as model where model.brandCode = ?");
    	hql.append(" and model.enable = ?");    	
    	
    	Object[] objArray = null;
    	objArray = new Object[] { brandCode, enable};
    	
    	List<BuPosBtnConfig> result = getHibernateTemplate().find(hql.toString(), objArray);
    	
    	
    	return result;
    }

}