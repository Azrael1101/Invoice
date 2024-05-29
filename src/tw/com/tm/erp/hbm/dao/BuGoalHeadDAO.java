package tw.com.tm.erp.hbm.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import bsh.StringUtil;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuGoalHead;

public class BuGoalHeadDAO extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(BuGoalHeadDAO.class);
	
	/**
	 * 依據headId為查詢條件，取得目標主檔
	 * @param headId
	 * @return
	 * @throws Exception
	 */
	public BuGoalHead findById(Long headId) throws Exception {

		try {
			BuGoalHead head = (BuGoalHead) this.findByPrimaryKey(BuGoalHead.class,
					headId);
			return head;
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢目標主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢目標主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 依據品牌,櫃號,部門查出一筆
	 * @param brandCode
	 * @param shopCode
	 * @param department
	 * @return
	 */
	public BuGoalHead findOneByShopCodeAndDepartment(String brandCode, String shopCode, String department, String status){
		if(StringUtils.hasText(status)){
			return (BuGoalHead)findFirstByProperty("BuGoalHead", "and brandCode = ? and shopCode = ? and department = ? and status = ?", 
					new Object[]{brandCode, shopCode, department, status});
		}else{
			return (BuGoalHead)findFirstByProperty("BuGoalHead", "and brandCode = ? and shopCode = ? and department = ?", 
					new Object[]{brandCode, shopCode, department});
		}
	}
}
