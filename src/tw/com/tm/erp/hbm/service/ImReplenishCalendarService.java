package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.Imformation;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuGoalEmployeeLine;
import tw.com.tm.erp.hbm.bean.BuGoalHead;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeHead;
import tw.com.tm.erp.hbm.bean.ImItemBarcodeLine;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameter;
import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameterId;
import tw.com.tm.erp.hbm.bean.ImReplenishCalendar;
import tw.com.tm.erp.hbm.bean.ImReplenishDisplay;
import tw.com.tm.erp.hbm.bean.ImReplenishHead;
import tw.com.tm.erp.hbm.bean.ImReplenishLimition;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishCalendarDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishDisplayDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReplenishLimitionDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImReplenishCalendarService {
    private static final Log log = LogFactory.getLog(ImReplenishCalendarService.class);
    
    private ImReplenishHeadDAO imReplenishHeadDAO;
    private ImReplenishCalendarDAO imReplenishCalendarDAO;
    
    public void setImReplenishHeadDAO(ImReplenishHeadDAO imReplenishHeadDAO) {
        this.imReplenishHeadDAO = imReplenishHeadDAO;
    }

    public void setImReplenishCalendarDAO(
    	ImReplenishCalendarDAO imReplenishCalendarDAO) {
        this.imReplenishCalendarDAO = imReplenishCalendarDAO;
    }

    /**
     * 刪除日曆明細
     * @param headId
     * @throws Exception
     */
    public void deleteCalendarLine(Long headId)throws Exception{
	try{
	    ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
	    if( head == null){
		throw new NoSuchObjectException("查無自動補貨單主鍵：" + headId + "的資料");
	    }
	    head.setImReplenishCalendarLines(new ArrayList(0));
	    imReplenishHeadDAO.update(head);
	} catch (Exception ex) {
	    log.error("刪除自動補貨單日曆明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除自動補貨單日曆時發生錯誤，原因：" + ex.getMessage());
	} 	
    }
    
    /**
     * 刪除陳列明細
     * @param headId
     * @throws Exception
     */
    public void deleteDisplayLine(Long headId)throws Exception{
	try{
	    ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
	    if( head == null){
		throw new NoSuchObjectException("查無自動補貨單主鍵：" + headId + "的資料");
	    }
	    head.setImReplenishDisplayLines(new ArrayList(0));
	    imReplenishHeadDAO.update(head);
	} catch (Exception ex) {
	    log.error("刪除自動補貨單陳列明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除自動補貨單陳列時發生錯誤，原因：" + ex.getMessage());
	} 	
    }
    
    /**
     * 刪除限制明細
     * @param headId
     * @throws Exception
     */
    public void deleteLimitionLine(Long headId)throws Exception{
	try{
	    ImReplenishHead head = (ImReplenishHead)imReplenishHeadDAO.findByPrimaryKey(ImReplenishHead.class, headId);
	    if( head == null){
		throw new NoSuchObjectException("查無自動補貨單主鍵：" + headId + "的資料");
	    }
	    head.setImReplenishLimitionLines(new ArrayList(0));
	    imReplenishHeadDAO.update(head);
	} catch (Exception ex) {
	    log.error("刪除自動補貨單限制明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除自動補貨單限制時發生錯誤，原因：" + ex.getMessage());
	} 	
    }
    
}
