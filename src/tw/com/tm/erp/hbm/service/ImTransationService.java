package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.ImInventoryCountsHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;

import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.SpringUtils;

/**
 * @author T15394
 *
 */
/**
 * @author T15394
 *
 */
public class ImTransationService {

    private static final Log log = LogFactory.getLog(ImTransationService.class);
    
    private ImTransationDAO imTransationDAO;
    
    private ImMovementService imMovementService;

    /* Spring IoC */
    
    public void setImTransationDAO(ImTransationDAO imTransationDAO) {
	this.imTransationDAO = imTransationDAO;
    }
    
    public void setImMovementService(ImMovementService imMovementService) {
        this.imMovementService = imMovementService;
    }

    /**
     * 依據品牌代號、品號、庫號、盤點日查詢至盤點日的庫存量
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findCountsDateOnHandQty(HashMap conditionMap) throws Exception {

	String endTransactionDate = null;
	try {
	    Date countsDate = (Date) conditionMap.get("countsDate");
            endTransactionDate = DateUtils.format(countsDate); //2008-10-03
	    String[] dateArray = StringTools.StringToken(endTransactionDate, "-");
            int year = Integer.parseInt(dateArray[0]); //2008
            int month = Integer.parseInt(dateArray[1]); //10
	        
	    if(month == 1){
		year -= 1;
                month = 12;
	    }else{
		month -= 1; 
	    }
	    StringBuffer beginTransactionDate = new StringBuffer(dateArray[0]);
	    beginTransactionDate.append("-");
	    beginTransactionDate.append(dateArray[1]);
	    beginTransactionDate.append("-");
	    beginTransactionDate.append("01");
	    
	    conditionMap.put("endTransactionDate", endTransactionDate);
	    BuBrandService buBrandService=(BuBrandService)SpringUtils.getApplicationContext().getBean("buBrandService");
	    BuBrand buBrand=buBrandService.findById((String) conditionMap.get("brandCode"));
	    if((buBrand.getMonthlyCloseMonth()==null)||(buBrand.getMonthlyCloseMonth().equals("")))
	    {
	    	conditionMap.put("beginTransactionDate", "1970-01-01");
		    return imTransationDAO.findTransactionCountsDateOnHandQty(conditionMap);
	    }
	    else{
	    int closeYear = Integer.parseInt(buBrand.getMonthlyCloseMonth().substring(0,4));
	    int closeMonth = Integer.parseInt(buBrand.getMonthlyCloseMonth().substring(4,6));
	    if((closeYear>year)||((closeYear==year)&&(closeMonth>month))){
	    conditionMap.put("beginTransactionDate", beginTransactionDate.toString());
	    conditionMap.put("year", year);
	    conditionMap.put("month", month);
	    }
	    else{
		    conditionMap.put("year", closeYear);
		    conditionMap.put("month", closeMonth);
		    conditionMap.put("beginTransactionDate", String.valueOf(closeYear)+"-"+String.valueOf(closeMonth+1)+"-01");
	    }
	    return imTransationDAO.findCountsDateOnHandQty(conditionMap);
	    }
	} catch (Exception ex) {
	    log.error("查詢盤點日" + endTransactionDate + "的庫存量時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢盤點日" + endTransactionDate + "的庫存量時發生錯誤，原因：" + ex.getMessage());
	}
    }
    public List<Properties> executeInitial(Map parameterMap){
	Map resultMap = new HashMap(0);
	
	try{
		Map multiList = new HashMap(0);
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
		
		List<ImWarehouse> allDeliveryWarehouses = imMovementService.getWarehouseInfo(brandCode, null);
		ImWarehouse imWarehouse = new ImWarehouse();
		imWarehouse.setWarehouseCode("");
		imWarehouse.setWarehouseName("請選擇");
		allDeliveryWarehouses.add(0,imWarehouse);
		multiList.put("allDeliveryWarehouses", AjaxUtils.produceSelectorData(allDeliveryWarehouses ,"warehouseCode" ,"warehouseName",  true,  false));	
		
		resultMap.put("multiList",multiList);
		
	}catch(Exception ex){
		log.error("表單初始化失敗，原因：" + ex.toString());
		Map messageMap = new HashMap();
		messageMap.put("type"   , "ALERT");
		messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
		messageMap.put("event1" , null);
		messageMap.put("event2" , null);
		resultMap.put("vatMessage",messageMap);
		
	}
	
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }
}
