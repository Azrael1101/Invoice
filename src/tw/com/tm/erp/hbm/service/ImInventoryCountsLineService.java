package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;
import tw.com.tm.erp.hbm.dao.ImInventoryCountsHeadDAO;

public class ImInventoryCountsLineService {

    private static final Log log = LogFactory
	    .getLog(ImInventoryCountsLineService.class);
    
    private ImInventoryCountsHeadDAO imInventoryCountsHeadDAO;  

    public void setImInventoryCountsHeadDAO(ImInventoryCountsHeadDAO imInventoryCountsHeadDAO) {
	this.imInventoryCountsHeadDAO = imInventoryCountsHeadDAO;
    }

    public void deleteInventoryCountsLine(ImInventoryCountsHead inventoryCountsHead) throws ValidationErrorException{
    
        try{
            ImInventoryCountsHead inventoryCountsHeadPO = (ImInventoryCountsHead) imInventoryCountsHeadDAO.findByPrimaryKey(ImInventoryCountsHead.class, inventoryCountsHead.getHeadId());
            if(inventoryCountsHeadPO == null){
    	        throw new ValidationErrorException("查無盤點單主鍵：" + inventoryCountsHead.getHeadId() + "的資料！");
    	    }
            inventoryCountsHeadPO.setImInventoryCountsLines(new ArrayList<ImInventoryCountsLine>(0));
            imInventoryCountsHeadDAO.update(inventoryCountsHeadPO);
        }catch (Exception ex) {
	    log.error("刪除盤點明細時發生錯誤，原因：" + ex.toString());
	    throw new ValidationErrorException("刪除盤點明細失敗！");
	}
    }
}
