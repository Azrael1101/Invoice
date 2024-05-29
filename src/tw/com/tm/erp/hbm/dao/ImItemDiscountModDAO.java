package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemDiscountId;
import tw.com.tm.erp.hbm.bean.ImItemDiscountMod;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;

public class ImItemDiscountModDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(ImItemDiscountModDAO.class);

	/**
	 * 依據品牌 、商品折扣卡別 、商品折扣類型查詢
	 * 
	 * @param conditionMap
	 * @return List<ImItemDiscount>
	 */
	
	 public ImItemDiscount findByvipType(String brandCode , String vipTypeCode ,String itemDiscountCode
			) {
		 
				StringBuffer hql = new StringBuffer("from ImItemDiscount as model where 1=1 ");
				hql.append(" and model.id.brandCode = ?");
				hql.append(" and model.id.vipTypeCode = ?");
				hql.append(" and model.id.itemDiscountType = ?");
									
			    log.info("HQL"+hql);
				
				List<ImItemDiscount> result = getHibernateTemplate().find(hql.toString(),
						new Object[] { brandCode, vipTypeCode , itemDiscountCode });
				
				log.info("Receive:"+result.size());
				
				return (result != null && result.size() > 0 ? result.get(0) : null);
				
				
			}
	
}
