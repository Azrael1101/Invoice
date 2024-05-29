package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuGoalDeployHead;
import tw.com.tm.erp.hbm.bean.BuGoalDeployLine;

public class BuGoalDeployLineDAO extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(BuGoalDeployLineDAO.class);
	
	/**
	 * 依 headId 撈出所有明細
	 * @param headId
	 * @return
	 */
	public List findBuGoalDeployLine(Long headId){
		return this.findByProperty("BuGoalDeployLine", "", "and buGoalDeployHead.headId = ?", new Object[]{ headId }, "order by indexNo");
	}
	
	/**
	 * 依headId,lineId 撈出一筆明細
	 * @param headId
	 * @param lineId
	 * @return
	 */
	public BuGoalDeployLine findOneBuGoalDeployLine(Long headId , Long lineId){
		return (BuGoalDeployLine)this.findFirstByProperty("BuGoalDeployLine", "and buGoalDeployHead.headId = ? and lineId = ?", 
				new Object[]{headId, lineId});
	}
}
