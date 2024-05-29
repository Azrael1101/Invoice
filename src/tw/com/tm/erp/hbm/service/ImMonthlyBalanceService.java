package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHead;
import tw.com.tm.erp.hbm.bean.ImMonthlyBalanceHeadId;
import tw.com.tm.erp.hbm.dao.ImMonthlyBalanceHeadDAO;

public class ImMonthlyBalanceService {
    
    private static final Log log = LogFactory.getLog(ImMonthlyBalanceService.class);
	private ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO ;
	public void setImMonthlyBalanceHeadDAO(ImMonthlyBalanceHeadDAO imMonthlyBalanceHeadDAO) {
		this.imMonthlyBalanceHeadDAO = imMonthlyBalanceHeadDAO;
	}

    public List<ImMonthlyBalanceHead> findImMonthlyBalanceHeadByItemCode(String brandCode, String itemCode, int maxRecord){
       return imMonthlyBalanceHeadDAO.findImMonthlyBalanceHeadByItemCode(brandCode, itemCode, maxRecord);
    }

    public ImMonthlyBalanceHead findById(String brandCode, String year, String month, String itemCode){
    	ImMonthlyBalanceHeadId id = new ImMonthlyBalanceHeadId(brandCode, itemCode , year, month);
        return (ImMonthlyBalanceHead) imMonthlyBalanceHeadDAO.findByPrimaryKey(ImMonthlyBalanceHead.class, id);
    }
}
