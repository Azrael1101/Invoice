package tw.com.tm.erp.hbm.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.service.BuGoalService;

public class BuGoalDeployHeadDAO extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(BuGoalDeployHeadDAO.class);
	
	/**
	 * 依據headId為查詢條件，取得目標設定主檔
	 * @param headId
	 * @return
	 * @throws Exception
	 */
	public BuGoalDeployHead findById(Long headId) throws Exception {

		try {
			BuGoalDeployHead head = (BuGoalDeployHead) this.findByPrimaryKey(BuGoalDeployHead.class,
					headId);
			return head; 
		} catch (Exception ex) {
			log.error("依據主鍵：" + headId + "查詢目標主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + headId + "查詢目標主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 依據品牌,櫃號,部門,年,月查出一筆
	 * @param brandCode
	 * @param shopCode
	 * @param department
	 * @param year
	 * @param month
	 * @return
	 */
	public BuGoalDeployHead findOneByShopCodeAndDepartment(String brandCode, String shopCode, String department, Long year, Long month){
		return (BuGoalDeployHead)this.findFirstByProperty("BuGoalDeployHead", "and brandCode = ? and shopCode = ? and department = ? and year = ? and month = ?", 
				new Object[]{brandCode, shopCode, department, year, month });
	}
}
