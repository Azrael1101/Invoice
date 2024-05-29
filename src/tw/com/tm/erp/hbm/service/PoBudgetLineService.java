package tw.com.tm.erp.hbm.service;

import java.util.Date;

import tw.com.tm.erp.hbm.bean.PoBudgetHead;
import tw.com.tm.erp.hbm.bean.PoBudgetLine;
import tw.com.tm.erp.hbm.dao.PoBudgetHeadDAO;

public class PoBudgetLineService {
	private PoBudgetHeadDAO poBudgetHeadDAO ;
	
	public String create(PoBudgetLine modifyObj) {
		if (null != modifyObj) {
			if (modifyObj.getLineId() == null) {
				System.out.println("save 1 ......");
				return save(modifyObj);
			} else {
				System.out.println("update 1 ......");
				return update(modifyObj);
			}
		}
		return "ImWarehouse is null ..";
	}	
	
	public String save(PoBudgetLine saveObj) {
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		poBudgetHeadDAO.save(saveObj);
		return "success";
	}

	public String update(PoBudgetLine updateObj) {
		updateObj.setLastUpdateDate(new Date());
		poBudgetHeadDAO.update(updateObj);
		return "success";
	}

	public PoBudgetHead findById(Long headId) {
		PoBudgetHead re = (PoBudgetHead) poBudgetHeadDAO.findByPrimaryKey(
				PoBudgetHead.class, headId);
		if(null == re)
			re = new PoBudgetHead();
		return re ;
	}

	public void setPoBudgetHeadDAO(PoBudgetHeadDAO poBudgetHeadDAO) {
		this.poBudgetHeadDAO = poBudgetHeadDAO;
	}
	
	


}
