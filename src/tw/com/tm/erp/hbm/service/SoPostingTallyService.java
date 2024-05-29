package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.dao.AppSoPostingDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SoShopDailyHeadDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class SoPostingTallyService {

    private static final Log log = LogFactory
	    .getLog(SoPostingTallyService.class);

    public static final String PROGRAM_ID = "SO_POSTING_TALLY";

    public static final String[] GRID_SEARCH_FIELD_NAMES = { 
    	"difference", "postingStatus", "unit", "salesDate","schedule",
    	"transactionAmountS", "posImportAmtS", "transactionAmountD", "posImportAmtD",
    	"actualTransactionAmount", "actualSalesAmt", "differenceAmt" };

    public static final int[] GRID_SEARCH_FIELD_TYPES = {
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };

    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
	    "images/empty.gif", "","", "", "",
	    "", "", "", "", 
	    "", "", "" };

    private static final String POSTED = "Y";

    private static final String UNPOST = "N";

    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private ImDeliveryLineDAO imDeliveryLineDAO;

    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;

    private SoPostingTallyDAO soPostingTallyDAO;

    private ImDeliveryMainService imDeliveryMainService;

    private SoSalesOrderMainService soSalesOrderMainService;

    private NativeQueryDAO nativeQueryDAO;

    private BuShopDAO buShopDAO;

    private BuShopMachineDAO buShopMachineDAO;

    private BuBasicDataService buBasicDataService;

    private SiProgramLogAction siProgramLogAction;

    private AppSoPostingDAO appSoPostingDAO;

    private BuBrandDAO buBrandDAO;

    private BuOrderTypeService buOrderTypeService;

    private SoShopDailyHead soShopDailyHead;
    
    private SoShopDailyHeadDAO soShopDailyHeadDAO;
    
    
    public void setSoShopDailyHead(SoShopDailyHead soShopDailyHead) {
    	this.soShopDailyHead = soShopDailyHead;
        }
    
    public void setSoShopDailyHeadDAO(SoShopDailyHeadDAO soShopDailyHeadDAO) {
    	this.soShopDailyHeadDAO = soShopDailyHeadDAO;
        }
    
    
    
    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }

    public void setImDeliveryLineDAO(ImDeliveryLineDAO imDeliveryLineDAO) {
	this.imDeliveryLineDAO = imDeliveryLineDAO;
    }

    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }

    public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
	this.soPostingTallyDAO = soPostingTallyDAO;
    }

    public void setImDeliveryMainService(
	    ImDeliveryMainService imDeliveryMainService) {
	this.imDeliveryMainService = imDeliveryMainService;
    }

    public void setSoSalesOrderMainService(SoSalesOrderMainService soSalesOrderMainService) {
	this.soSalesOrderMainService = soSalesOrderMainService;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
	this.nativeQueryDAO = nativeQueryDAO;
    }

    public void setBuShopDAO(BuShopDAO buShopDAO) {
	this.buShopDAO = buShopDAO;
    }

    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
	this.buShopMachineDAO = buShopMachineDAO;
    }

    public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
	this.buBasicDataService = buBasicDataService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }

    public void setAppSoPostingDAO(AppSoPostingDAO appSoPostingDAO) {
	this.appSoPostingDAO = appSoPostingDAO;
    }    

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }
    
    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
        this.buOrderTypeService = buOrderTypeService;
    }


    /**
     * 依據POS資料過帳螢幕輸入條件查詢
     * 
     * @param conditionMap
     * @return List
     * @throws Exception
     */
    public List findPostingDataList(HashMap conditionMap) throws Exception {
	try {
	    return soPostingTallyDAO.findPostingDataList(conditionMap);

	} catch (Exception ex) {
	    log.error("查詢POS過帳記錄檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢POS過帳記錄檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 執行過帳作業程序
     * 
     * @param conditionMap
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    /*
    public String executePosting(HashMap conditionMap, String loginUser) throws FormException, Exception {
    	try {
    		String brandCode = (String) conditionMap.get("brandCode");
    		String shopCode = (String) conditionMap.get("shopCode");
    		Date transactionDate = (Date) conditionMap.get("transactionDate");

    		ValidatePostingDate(conditionMap); // 檢核過帳資料
    		conditionMap.put("soStatus", OrderStatus.UNCONFIRMED);
    		ValidateUnconfirmedStatus(conditionMap); // 檢核SOP的狀態是否為未確認
    		conditionMap.put("soStatus", OrderStatus.SIGNING);
    		List salesOrderList = soSalesOrderService
    		.findSalesOrderByProperty(conditionMap);
    		if (salesOrderList != null && salesOrderList.size() > 0) {
    			produceDeliveryHeadAndChangeStatus(salesOrderList, loginUser);
    		}

    		modifyPostingTallyStatus(brandCode, shopCode, null, transactionDate, UNPOST, POSTED, loginUser, "N");

    		if (!StringUtils.hasText(shopCode)) {
    			return "過帳專櫃：全部，交易日期：" + DateUtils.format(transactionDate)
    			+ "之前所有未過帳資料皆已完成過帳！";
    		} else {
    			return "過帳專櫃：" + shopCode + "，交易日期："
    			+ DateUtils.format(transactionDate)
    			+ "之前所有未過帳資料皆已完成過帳！";
    		}
    	} catch (FormException fe) {
    		log.error("執行POS過帳作業時發生錯誤，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		log.error("執行POS過帳作業時發生錯誤，原因：" + ex.toString());
    		throw new Exception("執行POS過帳作業時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    */

    /**
     * 執行反過帳作業程序
     * 
     * @param conditionMap
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    /*
    public String executeAntiPosting(HashMap conditionMap, String loginUser) throws FormException, Exception {
	try {
	    // TODO:檢核是否可執行反過帳
	    String brandCode = (String) conditionMap.get("brandCode");
	    String shopCode = (String) conditionMap.get("shopCode");
	    Date transactionDate = (Date) conditionMap.get("transactionDate");
	    Date lastTransactionDate = DateUtils.getLastDateOfMonth(transactionDate);
	    conditionMap.put("lastTransactionDate", lastTransactionDate);
	    conditionMap.put("soStatus", "('" + OrderStatus.FINISH + "')");
	    List queryResult = soSalesOrderService.findSalesOrderAndDeliveryByProperty(conditionMap);
	    if (queryResult != null && queryResult.size() > 0) {
	    	modifySalesOrderAndDelivery(queryResult, OrderStatus.SIGNING, OrderStatus.SAVE, loginUser);
	    }
	    modifyPostingTallyStatus(brandCode, shopCode, transactionDate,
		    lastTransactionDate, POSTED, UNPOST, loginUser, "N");

	    if (!StringUtils.hasText(shopCode)) {
		return "反過帳專櫃：全部，交易日期：" + DateUtils.format(transactionDate)
			+ "至" + DateUtils.format(lastTransactionDate)
			+ "所有過帳資料皆已完成反過帳！";
	    } else {
		return "反過帳專櫃：" + shopCode + "，交易日期："
			+ DateUtils.format(transactionDate) + "至"
			+ DateUtils.format(lastTransactionDate)
			+ "所有過帳資料皆已完成反過帳！";
	    }
	} catch (FormException fe) {
	    log.error("執行POS反過帳作業時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("執行POS反過帳作業時發生錯誤，原因：" + ex.toString());
	    throw new Exception("執行POS反過帳作業時發生錯誤，原因：" + ex.getMessage());
	}
    }
*/
    /**
     * 檢核過帳資料(過帳)
     * 
     * @param conditionMap
     * @throws ValidationErrorException
     */
    private void ValidatePostingDate(HashMap conditionMap)
	    throws ValidationErrorException {

	String shopCode = (String) conditionMap.get("shopCode");
	Date transactionDate = (Date) conditionMap.get("transactionDate");
	List queryResult = soPostingTallyDAO.findPostingDataList(conditionMap);
	if (queryResult != null && queryResult.size() > 0) {
	    for (int i = 0; i < queryResult.size(); i++) {
		Object[] objArray = (Object[]) queryResult.get(i);
		if (objArray[2] == null) {
		    objArray[2] = new BigDecimal(0D);
		}
		if (objArray[3] == null) {
		    throw new ValidationErrorException("過帳專櫃：" + objArray[0]
			    + "，其交易日期：" + DateUtils.format((Date) objArray[1])
			    + "的營收彙總金額未輸入！");
		} else if (Double.parseDouble(objArray[2].toString()) != Double
			.parseDouble(objArray[3].toString())) {
		    throw new ValidationErrorException("過帳專櫃：" + objArray[0]
			    + "，其交易日期：" + DateUtils.format((Date) objArray[1])
			    + "的交易金額與營收彙總金額不相等！");
		}
	    }
	} else {
	    if (!StringUtils.hasText(shopCode)) {
		throw new ValidationErrorException("過帳專櫃：全部，其交易日期："
			+ DateUtils.format(transactionDate) + "已無未過帳資料！");
	    } else {
		throw new ValidationErrorException("過帳專櫃：" + shopCode
			+ "，其交易日期：" + DateUtils.format(transactionDate)
			+ "已無未過帳資料！");
	    }
	}
    }

    /**
     * 檢核SOP的狀態是否為未確認(過帳)
     * 
     * @param conditionMap
     * @throws ValidationErrorException
     */
    private void ValidateUnconfirmedStatus(HashMap conditionMap)
	    throws ValidationErrorException, Exception {

	Date transactionDate = (Date) conditionMap.get("transactionDate");
	List salesOrderList = soSalesOrderHeadDAO.findSalesOrderByProperty(conditionMap);
	if (salesOrderList != null && salesOrderList.size() > 0) {
	    throw new ValidationErrorException("交易日期："
		    + DateUtils.format(transactionDate) + "之前，尚有狀態為"
		    + OrderStatus.getChineseWord(OrderStatus.UNCONFIRMED)
		    + "的POS銷售單！");
	}
    }

    /**
     * 產生出貨單主檔與明細單建立關連、更新銷貨單及出貨單狀態至FINISH(過帳)
     * 
     * @param salesOrderList
     * @throws Exception
     */
    private void produceDeliveryHeadAndChangeStatus(List salesOrderList, String loginUser) throws Exception {
	for (int i = 0; i < salesOrderList.size(); i++) {
	    SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) salesOrderList.get(i);
	    ImDeliveryHead deliveryHead = imDeliveryMainService.produceDeliveryHeadAndSetRelation(salesOrderHead.getHeadId());
	    modifySalesOrder(salesOrderHead, OrderStatus.FINISH, POSTED, loginUser);
	    modifyImDeliveryStatusAndShipData(deliveryHead, OrderStatus.FINISH);
	}
    }

    /**
     * 更新過帳記錄檔過帳狀態(過帳、反過帳)
     * 
     * @param brandCode
     * @param shopCode
     * @param transactionDate
     * @param lastTransactionDate
     * @param origIsPosting
     * @param isPosting
     * @param loginUser
     * @param isDesignate
     * @throws Exception
     */
    private void modifyPostingTallyStatus(String brandCode, String shopCode,
    		Date transactionDate, Date lastTransactionDate,
    		String origIsPosting, String isPosting, String loginUser,
    		String isDesignate,String batch) throws Exception {

    	log.info("===modifyPostingTallyStatus== ");
	log.info("brandCode = "+brandCode);
	log.info("shopCode = "+shopCode);
	log.info("origIsPosting = "+origIsPosting);
	log.info("isPosting = "+isPosting);
	log.info("loginUser = "+loginUser);
	log.info("isDesignate = "+isDesignate);
	log.info("batch = "+batch);
    	 
    	try {
    		List<SoPostingTally> postingTallyList = soPostingTallyDAO
    		.findPostingTallyByProperty(brandCode, shopCode,
    				transactionDate, lastTransactionDate,
    				origIsPosting, isDesignate , loginUser ,batch);
    		if (postingTallyList != null && postingTallyList.size() > 0) {
    			for (int i = 0; i < postingTallyList.size(); i++) {
    				SoPostingTally postingTally = (SoPostingTally) postingTallyList.get(i);
    				postingTally.setIsPosting(isPosting);
    				if (POSTED.equals(isPosting)) {
    					postingTally.setPostingDate(DateUtils.getShortDate(new Date()));
    				} else {
    					postingTally.setPostingDate(null);
    				}
    				postingTally.setLastUpdatedBy(loginUser);
    				postingTally.setLastUpdateDate(new Date());
    				soPostingTallyDAO.update(postingTally);
    			}
    		}
    	} catch (Exception ex) {
    		log.error("更新過帳記錄檔過帳狀態時發生錯誤，原因：" + ex.toString());
    		throw new Exception("更新過帳記錄檔過帳狀態時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    //MACOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    private void modifyPostingTallyAmount(String brandCode, String shopCode,
    		Date transactionDate,String origIsPosting, String isPosting,String loginUser,String isDesignate,String batch,int saleCount,double saleAmount) throws Exception {
    	HashMap conditionMap = new HashMap();
    	log.info("===modifyPostingTallyStatus== ");
    	log.info("brandCode = "+brandCode);
    	log.info("shopCode = "+shopCode);
    	log.info("loginUser = "+loginUser);
    	log.info("isDesignate = "+isDesignate);
    	log.info("batch = "+batch);
    	log.info("saleCount = "+saleCount);
    	log.info("saleAmount = "+saleAmount);    	 
    	try {
    		
    		conditionMap.put("brandCode",brandCode);
    		conditionMap.put("employeeCode",loginUser);
    		conditionMap.put("shopCode",shopCode);
    		conditionMap.put("salesOrderStartDate",transactionDate);
    		conditionMap.put("salesOrderEndDate",transactionDate);
    		conditionMap.put("isEnable","Y");
    		
    		
    		log.info("查每日輸入");
    		List<SoShopDailyHead> soShopDailyHeads = soShopDailyHeadDAO.findShopDailyHeadListT2(conditionMap);
    		log.info("soShopDailyHeads.size():"+soShopDailyHeads.size());
    		if (soShopDailyHeads != null && soShopDailyHeads.size() > 0) {
    			for (SoShopDailyHead soShopDailyHead:soShopDailyHeads)
    			{
    				log.info(soShopDailyHead.getTotalActualSalesAmount()+"XXXXXXX"+soShopDailyHead.getVisitorCount());
    				double totalSaleAmount = soShopDailyHead.getTotalActualSalesAmount();
    				Long totalVisitorCount = soShopDailyHead.getVisitorCount();
    				log.info(totalSaleAmount+"/////"+totalVisitorCount);
    				soShopDailyHead.setTotalActualSalesAmount(totalSaleAmount-saleAmount);
    				soShopDailyHead.setVisitorCount(soShopDailyHead.getVisitorCount()-saleCount);
    				soShopDailyHead.setLastUpdatedBy(loginUser);
    				soShopDailyHead.setLastUpdateDate(new Date());
    				soShopDailyHeadDAO.update(soShopDailyHead);
    			}
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		log.error("更新過帳記錄檔過帳狀態時發生錯誤，原因：" + ex.toString());
    		throw new Exception("更新過帳記錄檔過帳狀態時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    /**
     * 更新出貨單狀態及出貨資料(過帳)
     * 
     * @param deliveryHead
     * @param status
     * @throws Exception
     */
    private void modifyImDeliveryStatusAndShipData(ImDeliveryHead deliveryHead,
	    String status) throws Exception {

	deliveryHead.setShipDate(deliveryHead.getScheduleShipDate());
	deliveryHead.setTotalOriginalShipAmount(deliveryHead.getTotalOriginalSalesAmount());
	deliveryHead.setTotalActualShipAmount(deliveryHead.getTotalActualSalesAmount());
	deliveryHead.setOriginalTotalFrnShipAmt(deliveryHead.getOriginalTotalFrnSalesAmt());
	deliveryHead.setActualTotalFrnShipAmt(deliveryHead.getActualTotalFrnSalesAmt());
	deliveryHead.setShipTaxAmount(deliveryHead.getTaxAmount());
	deliveryHead.setStatus(status);
	deliveryHead.setLastUpdateDate(new Date());
	imDeliveryHeadDAO.update(deliveryHead);
	List deliveryLines = imDeliveryLineDAO.findByProperty("ImDeliveryLine",
		"salesOrderId", deliveryHead.getSalesOrderId());
	if (deliveryLines != null && deliveryLines.size() > 0) {
	    for (int i = 0; i < deliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) deliveryLines.get(i);
		deliveryLine.setShipQuantity(deliveryLine.getSalesQuantity());
		deliveryLine.setOriginalShipAmount(deliveryLine.getOriginalSalesAmount());
		deliveryLine.setActualShipAmount(deliveryLine.getActualSalesAmount());
		deliveryLine.setShipTaxAmount(deliveryLine.getTaxAmount());
		deliveryLine.setOriginalForeignShipAmt(deliveryLine.getOriginalForeignSalesAmt());
		deliveryLine.setActualForeignShipAmt(deliveryLine.getActualForeignSalesAmt());
		imDeliveryLineDAO.update(deliveryLine);
	    }
	} else {
	    throw new NoSuchDataException("依據銷貨單主檔主鍵："
		    + deliveryHead.getSalesOrderId() + "查無相關出貨單明細檔資料！");
	}
    }

    /**
     * 更新銷貨單、出貨單資料(反過帳)
     * 
     * @param result
     * @param soStatus
     * @param ioStatus
     */
    private void modifySalesOrderAndDelivery(List result, String soStatus, String ioStatus, String loginUser) throws Exception{
    	for (int i = 0; i < result.size(); i++) {
    		Object[] objArray = (Object[]) result.get(i);
    		SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) objArray[0];
    		ImDeliveryHead deliveryHead = (ImDeliveryHead) objArray[1];
    		//如果單據已日結，執行反日結

    		if(OrderStatus.CLOSE.equals(deliveryHead.getStatus()))
    		{

    			imDeliveryMainService.executeDailyBalanceRevert(deliveryHead, soStatus, loginUser);
    		}

    		modifySalesOrder(salesOrderHead, soStatus, UNPOST, loginUser);

    		produceDeliveryLineAndDeleteOrigDelivery(salesOrderHead, deliveryHead, ioStatus, loginUser);

    	}

    }

    /**
     * 更新銷貨單資料(過帳、反過帳)
     * 
     * @param salesOrderHead
     * @param status
     * @param isPosting
     */
    private void modifySalesOrder(SoSalesOrderHead salesOrderHead, String status, String isPosting, String loginUser) {
    	salesOrderHead.setStatus(status);
    	salesOrderHead.setLastUpdatedBy(loginUser);
    	salesOrderHead.setLastUpdateDate(new Date());
    	List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
    	if (salesOrderItems != null && salesOrderItems.size() > 0) {
    		for (SoSalesOrderItem orderItem : salesOrderItems) {
    			if (UNPOST.equals(isPosting)) {
    				orderItem.setDeliveryId(null);
    				orderItem.setShippedQuantity(null);
    			}
    		}
    	}
    	soSalesOrderHeadDAO.update(salesOrderHead);
    }

    /**
     * 更新出貨單明細檔、刪除出貨單主檔(反過帳)
     * 
     * @param deliveryHead
     * @param status
     */
    private void produceDeliveryLineAndDeleteOrigDelivery(SoSalesOrderHead salesOrderHead, ImDeliveryHead deliveryHead, String status, String loginUser) {
    	imDeliveryHeadDAO.delete(deliveryHead);

    	soSalesOrderMainService.salesOrderToDeliveryForT2(salesOrderHead, loginUser);

    }

    /**
     * 依據專櫃代號、交易日期查詢
     * 
     * @param shopCode
     * @param transactionDate
     * @return SoPostingTally
     * @throws Exception
     */
    public SoPostingTally findSoPostingTallyById(String shopCode, Date transactionDate) throws Exception {
    	try {
    		SoPostingTally postingTally = (SoPostingTally) soPostingTallyDAO
    		.findByPrimaryKey(SoPostingTally.class, new SoPostingTallyId(shopCode, transactionDate));
    		return postingTally;
    	} catch (Exception ex) {
    		log.error("依據專櫃代號：" + shopCode + "、交易日期："
    				+ DateUtils.format(transactionDate) + "查詢過帳記錄檔時發生錯誤，原因：" + ex.toString());
    		throw new Exception("依據專櫃代號：" + shopCode + "、交易日期："
    				+ DateUtils.format(transactionDate) + "查詢過帳記錄檔時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    public void createPostingTallyWithRangeOfDate(Date transactionDate,
	    String brandCode, String opUser) throws Exception {

	try {
	    List<BuShop> buShops = buShopDAO.findShopByBrandAndScheduleDate(brandCode, transactionDate);
	    if (buShops != null) {
		for (int i = 0; i < buShops.size(); i++) {
		    BuShop shop = (BuShop) buShops.get(i);
		    if ("1".equals(shop.getShopType())) {
			String shopCode = shop.getShopCode();
			List<Date> notCreateDates = new ArrayList();
			String queryLastDateSql = "SELECT MAX(TRANSACTION_DATE) FROM ERP.SO_POSTING_TALLY S WHERE S.SHOP_CODE = '"
				+ shopCode + "'";
			List queryLastDateResult = nativeQueryDAO
				.executeNativeSql(queryLastDateSql);
			if (queryLastDateResult == null) {
			    throw new ValidationErrorException("查詢專櫃："
				    + shopCode + "於過帳記錄檔的最大交易日期失敗！");
			} else {
			    Date lastDate = (Date) queryLastDateResult.get(0);
			    if (lastDate == null) {
				notCreateDates.add(transactionDate);
			    } else {
				int dates = DateUtils.daysBetweenWithoutTime(
					lastDate, transactionDate);
				for (int index = 0; index < dates; index++) {
				    notCreateDates.add(DateUtils.addDays(
					    lastDate, index + 1));
				}
			    }
			}
			for (int j = 0; j < notCreateDates.size(); j++) {
			    Date actualSaveDate = (Date) notCreateDates.get(j);
			    actualSaveDate = DateUtils
				    .getShortDate(actualSaveDate);
			    SoPostingTallyId soPostingTallyId = new SoPostingTallyId();
			    SoPostingTally soPostingTally = new SoPostingTally();
			    soPostingTallyId.setShopCode(shop.getShopCode());
			    soPostingTallyId.setTransactionDate(actualSaveDate);
			    if(!brandCode.equals("T2")){
			    	soPostingTallyId.setBatch("99");
			    }
			    soPostingTally.setBrandCode(brandCode);
			    soPostingTally.setCreateDate(new Date());
			    soPostingTally.setCreatedBy(opUser);
			    soPostingTally.setIsPosting("N");
			    soPostingTally.setLastUpdateDate(new Date());
			    soPostingTally.setLastUpdatedBy(opUser);
			    soPostingTally.setId(soPostingTallyId);
			    soPostingTallyDAO.save(soPostingTally);
			}
		    }
		}
	    } else {
		throw new ValidationErrorException("查無" + brandCode + "的專櫃資訊！");
	    }
	} catch (Exception ex) {
	    log.error("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 執行過帳作業程序(補過帳)
     * 
     * @param salesOrderList
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public void executePosting(Long soHeadId, String loginUser) throws FormException, Exception {
    	try {
    		SoSalesOrderHead salesOrderHead = soSalesOrderMainService.findSoSalesOrderHeadById(soHeadId);
    		if (salesOrderHead != null) {
    			List assembly = new ArrayList();
    			assembly.add(salesOrderHead);
    			produceDeliveryHeadAndChangeStatus(assembly, loginUser);
    		}
    	} catch (FormException fe) {
    		log.error("執行POS過帳作業時發生錯誤，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		log.error("執行POS過帳作業時發生錯誤，原因：" + ex.toString());
    		throw new Exception("執行POS過帳作業時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    /**
     * 執行多筆過帳作業程序(補過帳).
     * @param soOrderHeadList
     * @param loginUser
     * @throws Exception
     */
    public void executePosting(List<SoSalesOrderHead> soOrderHeadList, String loginUser) throws Exception{
    	int n = soOrderHeadList.size();
    	for(int i=0;i<n;i++){
    		SoSalesOrderHead orderHead = soOrderHeadList.get(i);
    		try {		    
    			this.executePosting(orderHead.getHeadId(), loginUser);
    		} catch (FormException fe) {
    			log.error("執行POS過帳作業時發生錯誤，原因：" + fe.toString());
    			throw new FormException(fe.getMessage());
    		} catch (Exception ex) {
    			log.error("執行POS過帳作業時發生錯誤，原因：" + ex.toString());
    			throw new Exception("執行POS過帳作業時發生錯誤，原因：" + ex.getMessage());
    		}
    	}
    }
    
    public void createT2PostingTallyWithRangeOfDate(Date transactionDate, String brandCode, String opUser) throws Exception {
    	try {
    		List result = buShopMachineDAO.findByBrandAndScheduleDate(brandCode, transactionDate);
    		if (result != null && result.size() > 0) {
    			for (int i = 0; i < result.size(); i++) {
    				Object[] objArray = (Object[]) result.get(i);
    				BuShop shop = (BuShop) objArray[0];
    				if ("1".equals(shop.getShopType())) {
    					BuShopMachine shopMachine = (BuShopMachine) objArray[1];
    					String posMachineCode = shopMachine.getId().getPosMachineCode();
    					List<Date> notCreateDates = new ArrayList();
    					String queryLastDateSql = "SELECT MAX(TRANSACTION_DATE) FROM ERP.SO_POSTING_TALLY S WHERE S.SHOP_CODE = '"
    						+ posMachineCode + "'";
    					List queryLastDateResult = nativeQueryDAO.executeNativeSql(queryLastDateSql);
    					if (queryLastDateResult == null) {
    						throw new ValidationErrorException("查詢專櫃機台：" + posMachineCode + "於過帳記錄檔的最大交易日期失敗！");
    					} else {
    						Date lastDate = (Date) queryLastDateResult.get(0);
    						if (lastDate == null) {
    							notCreateDates.add(transactionDate);
    						} else {
    							int dates = DateUtils.daysBetweenWithoutTime(
    									lastDate, transactionDate);
    							for (int index = 0; index < dates; index++) {
    								notCreateDates.add(DateUtils.addDays(
    										lastDate, index + 1));
    							}
    						}
    					}
    					for (int j = 0; j < notCreateDates.size(); j++) {
    						Date actualSaveDate = (Date) notCreateDates.get(j);
    						actualSaveDate = DateUtils.getShortDate(actualSaveDate);
    						SoPostingTallyId soPostingTallyId = new SoPostingTallyId();
    						SoPostingTally soPostingTally = new SoPostingTally();
    						soPostingTallyId.setShopCode(posMachineCode);
    						soPostingTallyId.setTransactionDate(actualSaveDate);
    						soPostingTally.setBrandCode(brandCode);
    						soPostingTally.setCreateDate(new Date());
    						soPostingTally.setCreatedBy(opUser);
    						soPostingTally.setIsPosting("N");
    						soPostingTally.setLastUpdateDate(new Date());
    						soPostingTally.setLastUpdatedBy(opUser);
    						soPostingTally.setId(soPostingTallyId);
    						soPostingTallyDAO.save(soPostingTally);
    					}
    				}
    			}
    		} else {
    			throw new ValidationErrorException("查無" + brandCode
    					+ "的專櫃及機台資訊！");
    		}
    	} catch (Exception ex) {
    		log.error("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.toString());
    		throw new Exception("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    //--對帳後建立批次
    public void createT2PostingTallyWithSoDailyHead(Date transactionDate, String brandCode, String opUser,String batch) throws Exception {
    	try {
    		List result = buShopMachineDAO.findByBrandAndScheduleDate(brandCode, transactionDate);
    		Long nextBatch = 1L;    		
    		Long k = 1L;
    		/*if(batch.equals("1")){
    		nextBatch = Long.valueOf(batch);
    		}else{    		
    		nextBatch = Long.valueOf(batch)+k;    		    		
    		}
    		String insertBatch = String.valueOf(nextBatch);
    		log.info("insertBatch="+insertBatch);*/
    		log.info("nextBatch="+batch);
    		if (result != null && result.size() > 0) {
    			for (int i = 0; i < result.size(); i++) {
    				Object[] objArray = (Object[]) result.get(i);
    				BuShop shop = (BuShop) objArray[0];
    				if ("1".equals(shop.getShopType())) {
    					BuShopMachine shopMachine = (BuShopMachine) objArray[1];
    					String posMachineCode = shopMachine.getId().getPosMachineCode();
    					List<Date> notCreateDates = new ArrayList();
    					String queryLastDateSql = "SELECT MAX(TRANSACTION_DATE) FROM ERP.SO_POSTING_TALLY S WHERE S.SHOP_CODE = '"
    						+ posMachineCode + "' and S.BATCH = '" + batch +"'";
    					List queryLastDateResult = nativeQueryDAO.executeNativeSql(queryLastDateSql);
    					if (queryLastDateResult == null) {
    						throw new ValidationErrorException("查詢專櫃機台：" + posMachineCode + "於過帳記錄檔的最大交易日期失敗！");
    					} else {
    						Date lastDate = (Date) queryLastDateResult.get(0);
    						if (lastDate == null) {
    							notCreateDates.add(transactionDate);
    						} else {
    							int dates = DateUtils.daysBetweenWithoutTime(
    									lastDate, transactionDate);
    							for (int index = 0; index < dates; index++) {
    								notCreateDates.add(DateUtils.addDays(
    										lastDate, index + 1));
    							}
    						}
    					}    					
    					for (int j = 0; j < notCreateDates.size(); j++) {
    						Date actualSaveDate = (Date) notCreateDates.get(j);
    						actualSaveDate = DateUtils.getShortDate(actualSaveDate);
    						SoPostingTallyId soPostingTallyId = new SoPostingTallyId();
    						SoPostingTally soPostingTally = new SoPostingTally();
    						soPostingTallyId.setShopCode(posMachineCode);
    						soPostingTallyId.setTransactionDate(actualSaveDate);
    						soPostingTallyId.setBatch(batch);
    						soPostingTally.setBrandCode(brandCode);
    						soPostingTally.setCreateDate(new Date());
    						soPostingTally.setCreatedBy(opUser);
    						soPostingTally.setIsPosting("N");    						
    						soPostingTally.setLastUpdateDate(new Date());
    						soPostingTally.setLastUpdatedBy(opUser);
    						soPostingTally.setId(soPostingTallyId);
    						soPostingTallyDAO.save(soPostingTally);
    					}
    				}
    			}
    		} else {
    			throw new ValidationErrorException("查無" + brandCode
    					+ "的專櫃及機台資訊！");
    		}
    	} catch (Exception ex) {
    		log.error("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.toString());
    		throw new Exception("依據日期區間建立過帳記錄時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    /**
     * 更新過帳記錄檔
     * 
     * @param soPostingTally
     * @throws Exception
     */
    public void updateSoPostingTally(SoPostingTally soPostingTally) throws Exception {
	try {
	    soPostingTallyDAO.update(soPostingTally);
	} catch (Exception ex) {
	    log.error("更新過帳記錄時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新過帳記錄時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 執行POS資料過帳查詢初始化
     * 
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception {
    	HashMap resultMap = new HashMap();
    	try {
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String loginBrandCode = (String) PropertyUtils.getProperty(
    				otherBean, "loginBrandCode");
    		String loginEmployeeCode = (String) PropertyUtils.getProperty(
    				otherBean, "loginEmployeeCode");
    		Map multiList = new HashMap(0);
    		if (!"T2".equals(loginBrandCode)) {
    			List<BuShop> allShops = buBasicDataService.getShopForEmployee(
    					loginBrandCode, loginEmployeeCode, "Y");
    			multiList.put("allShops", AjaxUtils.produceSelectorData(
    					allShops, "shopCode", "shopCode", false, true));
    		} else {
    			List<BuShopMachine> allShopMachines = buBasicDataService
    			.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
    			multiList.put("allShops", AjaxUtils.produceSelectorData(
    					allShopMachines, "posMachineCode", "posMachineCode", false, true));
    			List<BuBatchConfig>  allBatch = buShopMachineDAO.findBatchTime();
    	        multiList.put("allBatch", AjaxUtils.produceSelectorData(allBatch, "batchId", "batchId", false, true));
    		}
    		String currentDate = DateUtils.format(new Date(), DateUtils.C_DATE_PATTON_SLASH);
    		resultMap.put("multiList", multiList);
    		resultMap.put("currentDate", currentDate);
    		return resultMap;
    	} catch (Exception ex) {
    		log.error("POS資料過帳查詢初始化失敗，原因：" + ex.toString());
    		throw new Exception("POS資料過帳查詢初始化失敗，原因：" + ex.toString());
    	}
    }

    /**
     * 顯示查詢頁面的line
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
    	try {
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
    		// ======================帶入查詢的值=========================
    		String brandCode = httpRequest.getProperty("brandCode");
    		String transactionBeginDate = httpRequest.getProperty("transactionBeginDate"); // 交易起始日期
    		String transactionEndDate = httpRequest.getProperty("transactionEndDate"); // 交易迄止日期
    		String salesUnit = httpRequest.getProperty("salesUnit"); // 營業單位
    		String status = httpRequest.getProperty("status"); // 狀態
    		String employeeCode = httpRequest.getProperty("employeeCode"); // 登入人員
    		String batch = httpRequest.getProperty("batch"); // 班次
    		Date actualTransactionBeginDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, transactionBeginDate);
    		Date actualTransactionEndDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, transactionEndDate);
    		List wrappedBeans = new ArrayList(0);

    		HashMap conditionMap = new HashMap();
    		conditionMap.put("orderTypeCodeS", "SOP");
    		conditionMap.put("orderTypeCodeD", "IOP");
    		conditionMap.put("brandCode", brandCode);
    		conditionMap.put("transactionBeginDate", actualTransactionBeginDate);
    		conditionMap.put("transactionEndDate", actualTransactionEndDate);
    		conditionMap.put("status", status);
    		conditionMap.put("salesUnit", salesUnit);
    		conditionMap.put("batch", batch);
    		conditionMap.put("soStatus", "('SIGNING', 'FINISH', 'CLOSE')");
    		conditionMap.put("employeeCode", employeeCode);
    		// =======================執行查詢==============================
    		if (!"T2".equals(brandCode)) {
    			conditionMap.put("salesUnitType", "S");
    		} else {
    			conditionMap.put("salesUnitType", "M");
    		}
    		HashMap searchResultMap = soPostingTallyDAO.findPostingDataListBySalesUnit(conditionMap, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
    		List resultData = (List) searchResultMap.get(BaseDAO.TABLE_LIST);
    		if (resultData != null && resultData.size() > 0) {
    			Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
    			searchResultMap = soPostingTallyDAO.findPostingDataListBySalesUnit(conditionMap, -1, iPSize, BaseDAO.QUERY_RECORD_COUNT);
    			Long maxIndex = (Long) searchResultMap.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
    			produceReturnWrappedBeans(resultData, wrappedBeans);
    			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES,
    					GRID_SEARCH_FIELD_DEFAULT_VALUES, wrappedBeans, gridDatas, firstIndex, maxIndex));
    		} else {
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, 
    					GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
    		}

    		return result;
    	} catch (Exception ex) {
    		
    		log.info("載入頁面顯示的POS資料過帳查詢發生錯誤，原因：" + ex.toString());
    		log.error("載入頁面顯示的POS資料過帳查詢發生錯誤，原因：" + ex.toString());
    		throw new Exception("載入頁面顯示的POS資料過帳查詢失敗！");
    	}
    }

    /**
     * sql 匯出excel
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws Exception
     */
    public SelectDataInfo getAJAXExportData(HttpServletRequest httpRequest) throws Exception{
    	String sql = null;
    	Object[] object = null;
    	List rowData = new ArrayList();
    	try {
    		String brandCode = httpRequest.getParameter("brandCode");
    		Properties properties = new Properties();
    		Enumeration paramNames = httpRequest.getParameterNames();
    		while (paramNames.hasMoreElements()) {
    			String name = (String) paramNames.nextElement();
    			String[] values = httpRequest.getParameterValues(name);
    			if ((null != values) && (values.length > 0)) {
    				String value = values[0];
    				properties.setProperty(name, value);
    			}
    		}
    		Map map = getSQLByBarcode(properties);
    		sql = (String)map.get("sql");
    		List lists = nativeQueryDAO.executeNativeSql(sql);

    		// 可用庫存excel表的欄位順序  pos過帳匯出
    		if(!brandCode.equalsIgnoreCase("T2")){
    			object = new Object[] { "salesDate", "unit", "unitName", "posImportAmt", "actualSalesAmt", "differenceAmt","","","" };
    		}else{
    			object = new Object[] { "salesDate", "unit", "unitName", "transactionAmount", "posImportAmt", "actualTransactionAmount","actualSalesAmt", "differenceAmt","actualTransactionAmount","","","" };
    		}

    		log.info(" lists.size() = " + lists.size() );
    		for (int i = 0; i < lists.size(); i++) {
    			Object[] getObj = (Object[])lists.get(i);
    			Object[] dataObject = new Object[object.length];
    			for (int j = 0; j < object.length; j++) {
    				dataObject[j] = getObj[j];
    			}
    			rowData.add(dataObject);
    		}

    		return new SelectDataInfo(object, rowData);
    	} catch (Exception e) {
    		log.error("匯出excel發生錯誤，原因：" + e.toString());
    		throw new Exception("匯出excel發生錯誤，原因：" + e.getMessage());
    	}
    }
    
    /**
     * 產生sql by httpRequest
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public Map getSQLByBarcode(Properties httpRequest) throws Exception{
    	StringBuffer sql = new StringBuffer();
    	StringBuffer sqlMax = new StringBuffer();
    	Map map = new HashMap();
    	//String salesUnitType = null;
    	String sqlSalesUnitType = null; 
    	//String machineCode = null;
    	String shop_name = null;
    	String soStatus = "('SIGNING', 'FINISH', 'CLOSE')";
    	String orderTypeCode = "SOP";
    	try {
    		String batch = httpRequest.getProperty("batch");
    		String brandCode = httpRequest.getProperty("brandCode");
    		String transactionBeginDate = httpRequest.getProperty("transactionBeginDate"); // 交易起始日期
    		String transactionEndDate = httpRequest.getProperty("transactionEndDate"); // 交易迄止日期
    		String salesUnit = httpRequest.getProperty("salesUnit"); // 營業單位
    		String status = httpRequest.getProperty("status"); // 狀態
    		String employeeCode = httpRequest.getProperty("employeeCode"); // 登入人員
    		String actualTransactionBeginDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, transactionBeginDate),DateUtils.C_DATA_PATTON_YYYYMMDD);
    		String actualTransactionEndDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, transactionEndDate),DateUtils.C_DATA_PATTON_YYYYMMDD);

    		log.info(" batch = " + batch );
    		log.info(" orderTypeCode = " + orderTypeCode );
    		log.info(" brandCode = " + brandCode );
    		log.info(" transactionBeginDate = " + transactionBeginDate );
    		log.info(" transactionEndDate = " + transactionEndDate );
    		log.info(" salesUnit = " + salesUnit );
    		log.info(" status = " + status );
    		log.info(" employeeCode = " + employeeCode );
    		log.info(" actualTransactionBeginDate = " + actualTransactionBeginDate );
    		log.info(" actualTransactionEndDate = " + actualTransactionEndDate );

    		// =======================執行查詢==============================
    		if (!"T2".equals(brandCode)) {
    			sqlSalesUnitType = "shop_code";
    			shop_name = "shop_c_name";
    		} else {
    			sqlSalesUnitType = "pos_machine_code";
    			shop_name = "shop_code";
    		} 

    		sql.append("select p.transaction_date, p.shop_code, s.shop_name,");

    		sql.append("NVL(s.actual_sales_amount,0) as actual_sales_amount,");
    		if ("T2".equals(brandCode)) {
    			sql.append("NVL(s.head_amount,0), ");	// POS匯入筆數
    		}

    		sql.append("NVL(d.total_actual_sales_amount,0), ");
    		if ("T2".equals(brandCode)) {
    			sql.append("NVL(d.visitor_count,0), "); // 營業人員輸入筆數 
    		}
    		sql.append("(NVL (s.actual_sales_amount, 0) - NVL (d.total_actual_sales_amount, 0)) as differenceAmt, ");
    		if ("T2".equals(brandCode)) {
    			sql.append("(NVL (s.head_amount, 0) - NVL (d.visitor_count, 0)) as actualTransactionAmount,"); 		// 筆數差異
    		}
    		sql.append(" '','' as 資訊,'' as 備註 ");
    		sql.append("from erp.so_posting_tally p, erp.so_shop_daily_head d, ");

    		sql.append("(select h.").append(sqlSalesUnitType).append(", m.").append(shop_name).append(" as shop_name ")
    		.append(", h.sales_order_date, sum(h.total_actual_sales_amount) as actual_sales_amount, count(h.head_id) as head_amount ");
    		sql.append("from erp.so_sales_order_head h, ");
    		if(!"T2".equals(brandCode)){
    			sql.append("erp.bu_shop m where "); 
    			sql.append("h.shop_code = m.shop_code(+) "); 
    		}else{
    			sql.append("erp.bu_shop_machine m where ");
    			
    			sql.append("h.shop_code = m.shop_code(+) ");
    			sql.append("and h.pos_machine_code = m.pos_machine_code(+) ");
    		}
    		sql.append("and h.brand_code = '").append(brandCode).append("' ");
    		sql.append("and h.order_type_code = '").append(orderTypeCode).append("' ");
    		sql.append("and h.status in ").append(soStatus);
    		sql.append(" and h.schedule = '").append(batch).append("' ");
    		if(transactionBeginDate != null){
    			sql.append(" and h.sales_order_date >= TO_DATE('").append(actualTransactionBeginDate).append("', 'YYYYMMDD')");
    		}
    		sql.append(" and h.sales_order_date <= TO_DATE('").append(actualTransactionEndDate).append("', 'YYYYMMDD') group by h.").append(sqlSalesUnitType).append(", m.").append(shop_name).append(", h.sales_order_date) s ");               

    		sql.append("where p.shop_code = s.").append(sqlSalesUnitType).append("(+)");

    		sql.append(" and p.shop_code = d.shop_code(+) ")
    		.append("and p.transaction_date = s.sales_order_date(+) ")
    		.append("and p.transaction_date = d.sales_date(+) ");
    		//MACO
    		sql.append(" and p.batch = d.batch ");
    		sql.append(" and p.batch = '").append(batch).append("' ");
    		sql.append(" and p.brand_code = '").append(brandCode).append("' ");

    		if(transactionBeginDate != null){
	    	sql.append(" and p.transaction_date >= TO_DATE('").append(actualTransactionBeginDate).append("', 'YYYYMMDD')");
	    }
	    sql.append(" and p.transaction_date <= TO_DATE('").append(actualTransactionEndDate).append("', 'YYYYMMDD')");
	    if(StringUtils.hasText(status)){
	    	sql.append(" and p.is_posting = '").append(status).append("'");
	    }
	    if(StringUtils.hasText(salesUnit)){
	    	sql.append(" and p.shop_code = '").append(salesUnit).append("'");
	    }else{
	    	if("T2".equals(brandCode)){
	    		StringBuffer shopMachinesByEMP = new StringBuffer("");
	    		List<BuShopMachine> allShopMachines = new ArrayList();
	    		try {
	    			allShopMachines = buBasicDataService.getShopMachineForEmployee(brandCode, employeeCode, "Y");
	    		} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		if(allShopMachines != null && allShopMachines.size()>0){
	    			for(int i=0;i<allShopMachines.size();i++){
	    				shopMachinesByEMP.append("'" + (allShopMachines.get(i)).getId().getPosMachineCode() + "', ");		    
	    			}
	    		}	
	    		final String allShopMachineByEMP = shopMachinesByEMP.replace(shopMachinesByEMP.lastIndexOf(", "), shopMachinesByEMP.length(), "").toString();
	    		//log.info("888==qry--SoPostingTally===allShopMachineByEMP = " + allShopMachineByEMP);
	    		sql.append(" and p.shop_code in (" + allShopMachineByEMP + ")");			    
	    	}
	    }
	    sql.append(" order by p.transaction_date, p.shop_code");
	    sqlMax.append("select count(p.shop_code) AS rowCount FROM(").append(sql).append(") ");

	    //log.info(" sql.toString() =  " + sql.toString());
	    //log.info(" sqlMax.toString() = " + sqlMax.toString());
	    map.put("sql", sql.toString());
	    map.put("sqlMax", sqlMax.toString());

	    return map;
	} catch (Exception e) {
		log.error("產生SQL發生錯誤，原因:" + e.toString());
		throw new Exception("產生SQL發生錯誤，原因:" + e.getMessage());
	}
    }
    
    /**
     * 取得CC開窗URL字串
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    
	    String reportFileName = (String)PropertyUtils.getProperty(otherBean, "reportFileName"); 
	    
	    String startDate = (String)PropertyUtils.getProperty(otherBean, "startDate");
	    log.info("before startDate = " + startDate);
	    if(StringUtils.hasText(startDate)){
		startDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		startDate = startDate.replace("/", "");
	    }
	    String endDate = (String)PropertyUtils.getProperty(otherBean, "endDate");
	    log.info("before endDate = " + endDate);
	    if(StringUtils.hasText(endDate)){
		endDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate), DateUtils.C_DATA_PATTON_YYYYMMDD);
		endDate = endDate.replace("/", "");
	    }
	    log.info("startDate = " + startDate);
	    log.info("endDate = " + endDate);
	    String category01 = (String)PropertyUtils.getProperty(otherBean, "category01");
	    String category02 = (String)PropertyUtils.getProperty(otherBean, "category02");
	    String warehouseCode = (String)PropertyUtils.getProperty(otherBean, "warehouseCode");
	    String taxType = (String)PropertyUtils.getProperty(otherBean, "taxType");
	    String showZero = (String)PropertyUtils.getProperty(otherBean, "showZero");
	    String samePrice = (String)PropertyUtils.getProperty(otherBean, "samePrice");
	    String supplierCode = (String)PropertyUtils.getProperty(otherBean, "supplierCode");
	    String customsWarehouseCode = (String)PropertyUtils.getProperty(otherBean, "customsWarehouseCode");
	    
	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞			
	    Map parameters = new HashMap(0);
	    parameters.put("prompt0", brandCode);
	    parameters.put("prompt1", orderTypeCode);
	    parameters.put("prompt2", orderNo);
	    parameters.put("prompt3", startDate);
	    parameters.put("prompt4", endDate);
	    parameters.put("prompt5", category01);
	    parameters.put("prompt6", category02);
	    parameters.put("prompt7", warehouseCode);
	    parameters.put("prompt8", taxType);
	    parameters.put("prompt9", showZero);
	    parameters.put("prompt10", samePrice);
	    parameters.put("prompt11", supplierCode);
	    parameters.put("prompt12", customsWarehouseCode);

	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName, parameters);
	    returnMap.put("reportUrl", reportUrl);
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	}catch(IllegalAccessException iae){
	    System.out.println(iae.getMessage());
	    throw new IllegalAccessException(iae.getMessage());
	}catch(InvocationTargetException ite){
	    System.out.println(ite.getMessage());
	    throw new InvocationTargetException(ite, ite.getMessage());
	}catch(NoSuchMethodException nse){
	    System.out.println(nse.getMessage());
	    throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());
	}
    }
     
    private void produceReturnWrappedBeans(List searchResult, List wrappedBeans) {

    	if (searchResult != null && searchResult.size() > 0) {
    		DecimalFormat df = new DecimalFormat("###,###,###");
    		for (int i = 0; i < searchResult.size(); i++) {
    			Object[] objs = (Object[]) searchResult.get(i);
    			String unit = (String) objs[0];
    			Date transactionDate = (Date) objs[1];
    			String schedule = (String) objs[2];
    			BigDecimal posImportAmtS = (BigDecimal) objs[3];
    			BigDecimal posImportAmtD = (BigDecimal) objs[4];
    			BigDecimal actualSalesAmt = (BigDecimal) objs[5];
    			String postingStatus = (String) objs[6];
    			BigDecimal transactionAmtS = (BigDecimal) objs[7];
    			BigDecimal transactionAmtD = (BigDecimal) objs[8];
    			BigDecimal actualTransactionAmount = (BigDecimal) objs[9];

    			SoPostingTally postingTally = new SoPostingTally();
    			postingTally.setUnit(unit);
    			postingTally.setSalesDate(DateUtils.format(transactionDate,DateUtils.C_DATE_PATTON_SLASH));
    			postingTally.setSchedule(schedule);
    			postingTally.setPostingStatus(("Y".equals(postingStatus)) ? "已過帳" : "未過帳");
    			postingTally.setTransactionAmountS(df.format(NumberUtils.getDouble(transactionAmtS)));
    			postingTally.setPosImportAmtS(df.format(NumberUtils.getDouble(posImportAmtS)));
    			postingTally.setTransactionAmountD(df.format(NumberUtils.getDouble(transactionAmtD)));
    			postingTally.setPosImportAmtD(df.format(NumberUtils.getDouble(posImportAmtD)));
    			postingTally.setActualSalesAmt(df.format(NumberUtils.getDouble(actualSalesAmt)));
    			postingTally.setActualTransactionAmount(df.format(NumberUtils.getDouble(actualTransactionAmount)));

    			postingTally.setDifferenceAmt(df.format(NumberUtils.getDouble(posImportAmtS) - NumberUtils.getDouble(actualSalesAmt)));
    			if (NumberUtils.getDouble(posImportAmtS) == NumberUtils.getDouble(actualSalesAmt) && NumberUtils.getDouble(transactionAmtS) == NumberUtils.getDouble(actualTransactionAmount)) {
    				postingTally.setDifference("images/ok.gif");
    			} else {
    				postingTally.setDifference("images/error.gif");
    			}
    			wrappedBeans.add(postingTally);
    		}
    	}
    }

    public List<Properties> saveSearchResult(Properties httpRequest)
	    throws Exception {
	String errorMsg = null;
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 執行過帳作業程序(AJAX)
     * 
     * @param conditionMap
     * @param postingDatas
     * @return Map
     * @throws Exception
     */
    public Map executePosting(HashMap conditionMap, List postingDatas) throws Exception {
    	//log.info("enter===executePosting");
    	HashMap resultMap = new HashMap();
    	String resultMsg = null;
    	String message = null;
    	String unitLabel = "過帳專櫃";
    	String identification = (String) conditionMap.get("timeScope");
    	boolean isAllPosting = true;
    	try {
    		String brandCode = (String) conditionMap.get("brandCode");
    		if ("T2".equals(brandCode)) {
    			unitLabel = "過帳機台";
    		}
    		String postingSalesUnitBegin = (String) conditionMap.get("postingSalesUnitBegin");
    		String postingSalesUnitEnd = (String) conditionMap.get("postingSalesUnitEnd");
    		String employeeCode = (String) conditionMap.get("employeeCode");
    		Date transactionDate = (Date) conditionMap.get("transactionDate");
    		String batch = (String) conditionMap.get("batch");
    		log.info("batch"+batch);
    		log.info("batch" + Integer.parseInt(batch));
    		conditionMap.put("soStatus", OrderStatus.SIGNING);
    		int batchMax = Integer.parseInt(batch);
log.info("postingDatasddddd"+postingDatas.size());
    			for (int i = 0; i < postingDatas.size(); i++) {
	    			String[] postingDataArray = null;
	    			try {
	    				postingDataArray = (String[]) postingDatas.get(i);
	    				appSoPostingDAO.execPosting(brandCode, "IOP", postingDataArray[0], postingDataArray[1], employeeCode ,batch);
	    			} catch (Exception ex) {
	    				message = "執行" + unitLabel + "(" + postingDataArray[0] + ")、過帳日期(" + postingDataArray[1] + ")的過帳程序失敗，原因：";
	    				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message + ex.getMessage(), employeeCode);
	    				log.error(message + ex.toString());
	    				isAllPosting = false;
	    			}
	    		}

    		if("T2".equals(brandCode)){
    			if (isAllPosting)
    				resultMsg = unitLabel + "(" + postingSalesUnitBegin + "~" + postingSalesUnitEnd + ")、"
    				+ "交易日期("+ DateUtils.format(transactionDate, DateUtils.C_DATE_PATTON_SLASH) + ")之前所有無差異的資料皆已完成過帳！";
    			else
    				resultMsg = unitLabel + "(" + postingSalesUnitBegin + "~" + postingSalesUnitEnd + ")、交易日期("
    				+ DateUtils.format(transactionDate, DateUtils.C_DATE_PATTON_SLASH) + ")之前部分無差異的資料已完成過帳，但仍有部分無差異"
    				+ unitLabel + "過帳失敗， 請點選訊息提示按鈕檢視錯誤訊息！";
    		}else{
    			if (isAllPosting)
    				resultMsg = unitLabel + "(全部)、交易日期("+ DateUtils.format(transactionDate, DateUtils.C_DATE_PATTON_SLASH) + ")之前所有無差異的資料皆已完成過帳！";
    			else
    				resultMsg = unitLabel + "(全部)、交易日期("+ DateUtils.format(transactionDate,DateUtils.C_DATE_PATTON_SLASH)
    				+ ")之前部分無差異的資料已完成過帳，但仍有部分無差異" + unitLabel + "過帳失敗， 請點選訊息提示按鈕檢視錯誤訊息！";
    		}
    		
    		resultMap.put("resultMsg", resultMsg);
    		//log.info("leave===executePosting");
    		return resultMap;
    	} catch (Exception ex) {
    		log.error("執行POS過帳作業時發生錯誤，原因：" + ex.toString());
    		ex.printStackTrace();
    		throw new Exception("執行POS過帳作業時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    /**
     * 檢核過帳資料(AJAX)
     * 
     * @param conditionMap
     * @param errorMsgs
     * @param postingDatas
     * @throws FormException
     */
    public void validatePostingDateForAjax(HashMap conditionMap, List errorMsgs, List postingDatas) throws FormException {

    	String brandCode = (String) conditionMap.get("brandCode");
    	String postingSalesUnitBegin = (String) conditionMap.get("postingSalesUnitBegin");
    	String postingSalesUnitEnd = (String) conditionMap.get("postingSalesUnitEnd");
    	Date transactionDate = (Date) conditionMap.get("transactionDate");
    	String identification = (String) conditionMap.get("timeScope");
    	String employeeCode = (String) conditionMap.get("employeeCode");
    	String message = null;
    	String unitLabel = "過帳專櫃";
    	try {
    		if ("T2".equals(brandCode)) {
    			unitLabel = "過帳機台";
    		}
    		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);
    		List resultData = soPostingTallyDAO.findPostingDataListBySalesUnit(conditionMap);//(List) searchResultMap.get(BaseDAO.TABLE_LIST);
    		//log.info("resultData="+resultData);
    		if (resultData != null && resultData.size() > 0 ) {
    			//log.info("validatePostingDateForAjax===resultData.size() > 0");
    			for (int i = 0; i < resultData.size(); i++) {
    				Object[] objArray = (Object[]) resultData.get(i);
    				if (objArray[2] == null) {
    					objArray[2] = 0D;
    				}
    				if (objArray[5] == null) {
    					objArray[5] = 0D;
    				}
    				if (objArray[6] == null) {
    					objArray[6] = 0D;
    				}
    				if (objArray[3] != null && Double.parseDouble(objArray[2].toString()) == Double.parseDouble(objArray[3].toString()) && Double.parseDouble(objArray[5].toString()) == Double.parseDouble(objArray[6].toString())) {
    					postingDatas.add(new String[] {(String) objArray[0], DateUtils.format((Date) objArray[1], DateUtils.C_DATA_PATTON_YYYYMMDD) });
    				}
    			}
    		} else {
    			//log.info("validatePostingDateForAjax===resultData.size() <= 0");
    			if (StringUtils.hasText(postingSalesUnitBegin) && StringUtils.hasText(postingSalesUnitEnd)) {
    				throw new ValidationErrorException(unitLabel + "(" + postingSalesUnitBegin + "~" + postingSalesUnitEnd + ")其交易日期("
    						+ DateUtils.format(transactionDate) + ")已無未過帳資料！");
    			}else{
    				throw new ValidationErrorException(unitLabel + "(全部)其交易日期(" + DateUtils.format(transactionDate) + ")已無未過帳資料！");
    			}
    		}
    		log.info("leave=====validatePostingDateForAjax===");
    	} catch (FormException ex) {
    		message = "檢核過帳資料時發生錯誤，原因：" + ex.toString();
    		log.error(message);
    		throw new FormException(ex.getMessage());
    	} catch (Exception ex) {
    		message = "檢核過帳資料時發生錯誤，原因：" + ex.toString();
    		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, employeeCode);
    		errorMsgs.add(message);
    		log.error(message);
    	}
    }

    /**
     * 執行反過帳作業程序(AJAX)
     * 
     * @param conditionMap
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public Map executeAntiPosting(HashMap conditionMap) throws FormException, Exception {
    	log.info("executeAntiPosting"+"反過帳班次:"+(String) conditionMap.get("batch"));
    	HashMap resultMap = new HashMap();
    	String resultMsg = null;
    	String unitLabel = "反過帳專櫃";
    	try {
    		String brandCode = (String) conditionMap.get("brandCode");
    		if ("T2".equals(brandCode)) {
    			unitLabel = "反過帳機台";
    		}

    		String postingSalesUnitBegin = (String) conditionMap.get("postingSalesUnitBegin");
    		String postingSalesUnitEnd = (String) conditionMap.get("postingSalesUnitEnd");
    		String employeeCode = (String) conditionMap.get("employeeCode");
    		String batch = (String) conditionMap.get("batch");
    		Date transactionDate = (Date) conditionMap.get("transactionDate");
    		String salesUnitType = (String) conditionMap.get("salesUnitType");
    		log.info("1");
    		//依據品牌代號、單別、專櫃代號、銷售日期、狀態查詢出貨單及銷貨單
    		conditionMap.put("soStatus", "('" + OrderStatus.FINISH + "' , '" + OrderStatus.CLOSE + "')");
    		conditionMap.put("isDesignate", "Y");
    		List queryResult = soSalesOrderHeadDAO.findSalesOrderAndDeliveryByPropertyForPOS(conditionMap);
    		if (queryResult != null && queryResult.size() > 0) {
    			modifySalesOrderAndDelivery(queryResult, OrderStatus.SIGNING, OrderStatus.SAVE, employeeCode);
    		}
    		
    		//查詢需反過的所有機台/專櫃
    		List<BuShopMachine> allShopMachines = new ArrayList();
    		log.info("2");
    			allShopMachines = buShopMachineDAO.getShopMachineForEmployee(brandCode, 
    					employeeCode, "Y", salesUnitType, postingSalesUnitBegin, postingSalesUnitEnd);
    		if(allShopMachines != null && allShopMachines.size()>0){
    			for (int i = 0; i < allShopMachines.size(); i++) {
    				log.info("3-"+i);
    				BuShopMachine buShopMachine = allShopMachines.get(i);
    				if(!"T2".equals(brandCode)){
    					modifyPostingTallyStatus(brandCode, buShopMachine.getId().getShopCode(), null,
    							transactionDate, POSTED, UNPOST, employeeCode, POSTED ,batch);
    				}else{
    					modifyPostingTallyStatus(brandCode, buShopMachine.getId().getPosMachineCode(), null,
    							transactionDate, POSTED, UNPOST, employeeCode, POSTED, batch);    					
    				}
    			}
    			log.info("4");
    		}

    		if (StringUtils.hasText(postingSalesUnitBegin) && StringUtils.hasText(postingSalesUnitEnd)) {
    			resultMsg = unitLabel + "(" + postingSalesUnitBegin + "~" + postingSalesUnitEnd + ")、交易日期("
    			+ DateUtils.format(transactionDate) + ")所有過帳資料皆已完成反過帳！";
    		} else {
    			resultMsg = unitLabel + "(全部)、交易日期(" + DateUtils.format(transactionDate) + ")所有過帳資料皆已完成反過帳！";
    		}
    		resultMap.put("resultMsg", resultMsg);
    		return resultMap;
    	} catch (FormException fe) {
    		log.error("執行POS反過帳作業時發生錯誤，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		log.error("執行POS反過帳作業時發生錯誤，原因：" + ex.toString());
    		throw new Exception("執行POS反過帳作業時發生錯誤，原因：" + ex.getMessage());
    	}
    }

    /**
     * 初始化
     * 
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public HashMap executeInitial(Map parameterMap) throws Exception {
	//log.info("============<executeInitial>===============");
	HashMap resultMap = new HashMap();
	Map multiList = new HashMap();
	//String identification = "";
	String loginBrandCode = "";
	String loginEmployeeCode = "";
	try {
	    Object otherBean = parameterMap.get("vatBeanOther");
	    loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginEmployeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
	    
	    resultMap.put("lastUpdatedByName", loginEmployeeName);
	    resultMap.put("lastUpdateDate", new Date());
	    resultMap.put("createByName", loginEmployeeName);
	    resultMap.put("createDate", new Date());

	    resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());

	    // 取得下拉List<BuOrderType>   
	    List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode ,"SO");
	    multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, true, "SOP"));

	    // 取得下拉List<BuShop>  
	    if(!"T2".equals(loginBrandCode)){ 
		List<BuShop> allShops = buBasicDataService.getShopForEmployee(loginBrandCode, loginEmployeeCode, "Y");
		multiList.put("allShops", AjaxUtils.produceSelectorData(allShops, "shopCode", "shopCode", false, true));
	    }else{
		List<BuShopMachine> allShopMachines = buBasicDataService.getShopMachineForEmployee(loginBrandCode, loginEmployeeCode, "Y");
		multiList.put("allShops", AjaxUtils.produceSelectorData(allShopMachines, "posMachineCode", "posMachineCode", false, true));		
	    }
	    
	    resultMap.put("multiList", multiList);

	    //log.info("executeInitial resultMap = " + resultMap);
	} catch (Exception ex) {
	    log.error("執行單筆過帳作業初始化失敗，原因：" + ex.toString());
	    throw new Exception("執行單筆過帳作業初始化失敗，原因：" + ex.toString());   
	}
	return resultMap;
    }

    /**
     * 檢核補過帳的相關資料是否符合.
     * @param conditionMap
     * @throws Exception 
     */
    public List<SoSalesOrderHead> validPostingData(Map conditionMap) throws Exception{
    	Object otherBean = conditionMap.get("vatBeanOther");
    	Object formBindBean = conditionMap.get("vatBeanFormBind");
    	List<SoSalesOrderHead> soOrderHeadList = new ArrayList();
    	String loginEmployeeCode = "";
    	String identification = "";
    	try{
    		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
    		loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");		
    		String orderNo = (String)PropertyUtils.getProperty(formBindBean, "orderNo");
    		String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");		
    		String salesDate = (String)PropertyUtils.getProperty(formBindBean, "salesDate");

    		//======check orderTypeCode+orderNo 或 shopCode+salesDate 必須有一組不為空值========
    		if(!StringUtils.hasText(orderTypeCode) && !StringUtils.hasText(orderNo)
    				&& !StringUtils.hasText(shopCode) && !StringUtils.hasText(salesDate)){
    			if("T2".equals(loginBrandCode)){
    				throw new Exception("單別：" + orderTypeCode + " + 單號：" + orderNo + " 或 機台：" + shopCode + " + 銷售日期：" + salesDate + "不可均為空值。");
    			}else{
    				throw new Exception("單別：" + orderTypeCode + " + 單號：" + orderNo + " 或 專櫃：" + shopCode + " + 銷售日期：" + salesDate + "不可均為空值。");
    			}		
    		}        	    
    		//=====check status=signing的才可補過帳
    		if(StringUtils.hasText(orderTypeCode) && StringUtils.hasText(orderNo)){
    			soOrderHeadList = this.soSalesOrderHeadDAO.findByProperty("SoSalesOrderHead", new String[]{"brandCode", "orderTypeCode", "orderNo", "status"}
    			, new Object[]{loginBrandCode, orderTypeCode, orderNo, OrderStatus.SIGNING});
    			identification = loginBrandCode + "_" + orderTypeCode + "_" + orderNo;
    			if(soOrderHeadList.size() == 0){
    				throw new Exception("品牌：" + loginBrandCode + "、單別：" + orderTypeCode + "、單號：" + orderNo + "的狀態必須為SIGNING才可執行過帳作業！");
    			}		
    		}
    		else if(StringUtils.hasText(shopCode) && StringUtils.hasText(salesDate)){
    			//identification = loginBrandCode + orderTypeCode + orderNo;
    			if("T2".equals(loginBrandCode)){
    				soOrderHeadList = this.soSalesOrderHeadDAO.findByProperty("SoSalesOrderHead", new String[]{"brandCode", "posMachineCode", "salesOrderDate", "status"}
    				, new Object[]{loginBrandCode, shopCode, DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, salesDate), OrderStatus.SIGNING});
    				if(soOrderHeadList.size() == 0){
    					throw new Exception("品牌：" + loginBrandCode + "、機台：" + shopCode + "、銷售日期：" + salesDate + "裏沒有狀態為SIGNING的銷售單可執行過帳作業！");
    				}

    			}else{
    				soOrderHeadList = this.soSalesOrderHeadDAO.findByProperty("SoSalesOrderHead", new String[]{"brandCode", "shopCode", "salesOrderDate", "status"}
    				, new Object[]{loginBrandCode, shopCode, DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, salesDate), OrderStatus.SIGNING});
    				if(soOrderHeadList.size() == 0){
    					throw new Exception("品牌：" + loginBrandCode + "、專櫃：" + shopCode + "、銷售日期：" + salesDate + "裏沒有狀態為SIGNING的銷售單可執行過帳作業！");
    				}
    			}        	     	    
    		}else{
    			if("T2".equals(loginBrandCode)){
    				throw new Exception("單別：" + orderTypeCode + " + 單號：" + orderNo + " 或 機台：" + shopCode + " + 銷售日期：" + salesDate + "必須擇一輸入。");
    			}else{
    				throw new Exception("單別：" + orderTypeCode + " + 單號：" + orderNo + " 或 專櫃：" + shopCode + " + 銷售日期：" + salesDate + "必須擇一輸入。");
    			}
    		}

    		for(int i=0;i<soOrderHeadList.size();i++){
    			SoSalesOrderHead orderHead = soOrderHeadList.get(i);
    			//=====check 銷售日期必須大於關帳年月
    			ValidateUtil.isAfterClose(loginBrandCode, orderTypeCode, "銷售日期", orderHead.getSalesOrderDate(),orderHead.getSchedule());
    			List<SoPostingTally> soPostingTallyList = new ArrayList();
    			if("T2".equals(loginBrandCode)){
    				soPostingTallyList = this.soPostingTallyDAO.findByProperty("SoPostingTally"
    						, new String[]{"brandCode", "id.shopCode", "id.transactionDate"}
    						, new Object[]{loginBrandCode, orderHead.getPosMachineCode(), orderHead.getSalesOrderDate()});
    			}else{
    				soPostingTallyList = this.soPostingTallyDAO.findByProperty("SoPostingTally"
    						, new String[]{"brandCode", "id.shopCode", "id.transactionDate"}
    						, new Object[]{loginBrandCode, orderHead.getShopCode(), orderHead.getSalesOrderDate()});
    			}

    			//=====check 該日該機台/專櫃是否已過帳，已過帳的才可補過帳
    			if(soPostingTallyList.size() == 0){
    				if("T2".equals(loginBrandCode)){
    					throw new Exception("機台：" + orderHead.getPosMachineCode() + " + 銷售日期：" + orderHead.getSalesOrderDate() + " 未有過帳記錄。");     
    				}else{
    					throw new Exception("專櫃：" + orderHead.getShopCode() + " + 銷售日期：" + orderHead.getSalesOrderDate() + " 未有過帳記錄。");
    				}
    			}
    		}
    	}catch (Exception ex) {	    
    		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR
    				, identification, ex.toString(), loginEmployeeCode);
    		log.error("執行單筆過帳作業失敗，原因：" + ex.toString());
    		throw new Exception("執行單筆過帳作業失敗，原因：" + ex.toString());   
    	}
    	return soOrderHeadList;
    }
    
    public void validRollBackPostingData(Map conditionMap) throws Exception{
    	Object otherBean = conditionMap.get("vatBeanOther");
    	Object formBindBean = conditionMap.get("vatBeanFormBind");
    	SoSalesOrderHead orderHead;
    	List<SoSalesOrderHead> soOrderHeadList = new ArrayList();
    	String loginEmployeeCode = "";
    	String identification = "";
    	try{
    		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");		
    		loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String orderTypeCode = (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");		
    		String orderNo = (String)PropertyUtils.getProperty(formBindBean, "orderNo");
    		String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");		
    		String salesDate = (String)PropertyUtils.getProperty(formBindBean, "salesDate");

//檢核前端輸入資料
    		if(!StringUtils.hasText(orderTypeCode))
    		{
    			throw new Exception("單別不可為空值。");
    		}
    		if(!StringUtils.hasText(orderNo))
    		{
    			throw new Exception("單號不可為空值。");
    		}
    		if(!StringUtils.hasText(shopCode))
    		{
    			throw new Exception("機台號碼不可為空值。");
    		}
    		if(!StringUtils.hasText(salesDate))
    		{
    			throw new Exception("銷售日期不可為空值。");
    		}
//根據單別單號找單據
    		orderHead = soSalesOrderHeadDAO.findSalesOrderByIdentification(loginBrandCode, orderTypeCode, orderNo); 

    		if(null == orderHead.getHeadId() )
	    	{
    			throw new Exception("單據：" + orderTypeCode + "-" + orderHead.getOrderNo() + "查無資料" );
	    	}
    		else
    		{
//單據狀態必須為FINISH或CLOSE才可反過帳
    			if(orderHead.getStatus().equals(OrderStatus.FINISH) || orderHead.getStatus().equals(OrderStatus.CLOSE))
    			{
	    			ValidateUtil.isAfterClose(loginBrandCode, orderTypeCode, "銷售日期", orderHead.getSalesOrderDate(),orderHead.getSchedule());
    			}
    			else
    			{
    				throw new Exception("單據：" + orderTypeCode + "-" + orderHead.getOrderNo() + "狀態非FINISH或CLOSE" );     
    			}
    		}
    	}
    	catch (Exception ex)
    	{	    
    		siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, ex.toString(), loginEmployeeCode);
    		log.error("執行單筆反過帳作業失敗，原因：" + ex.toString());
    		throw new Exception("執行單筆反過帳作業失敗,輸入錯誤");   
    	}

    }
    
    
    
    
    
    
    
    
    
    public Map executeSingleAntiPosting(HashMap conditionMap) throws FormException, Exception {
    	log.info("反過帳作業開始");
    	HashMap resultMap = new HashMap();
    	String resultMsg = null;

    	try {
    		String brandCode = (String) conditionMap.get("brandCode");
    		String employeeCode = (String) conditionMap.get("employeeCode");	
    		String orderTypeCode = (String) conditionMap.get("orderTypeCode");
    		String orderNo = (String) conditionMap.get("orderNo");
    		log.info("找銷售單與進貨單");
    		List queryResult = soSalesOrderHeadDAO.findSalesOrderAndDeliveryByPropertyForOrderNo(conditionMap);
    		log.info("單據個數="+queryResult.size()+",執行反過帳");
    		if (queryResult != null && queryResult.size() > 0)
    		{
    			modifySalesOrderAndDelivery(queryResult, OrderStatus.SIGNING, OrderStatus.SAVE, employeeCode);
    		}
    		log.info("反過帳完成");
    		//查詢需反過的所有機台/專櫃
    		
    		

/*
 			//修改專櫃過帳維護資料
      		for (int i = 0; i < queryResult.size(); i++)
    		{

        		
        		Object[] objArray = (Object[]) queryResult.get(i);
        		SoSalesOrderHead salesOrderHead = (SoSalesOrderHead) objArray[0];
            	List<SoSalesOrderItem> salesOrderItems = salesOrderHead.getSoSalesOrderItems();
            	int saleCount = salesOrderItems.size();
            	double saleAmount = salesOrderHead.getTotalActualSalesAmount();
        		
	    		String posMachineCode = salesOrderHead.getPosMachineCode();
				BuShopMachine buShopMachine = buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode,posMachineCode);
				Date transactionDate = salesOrderHead.getSalesOrderDate();
				String batch = salesOrderHead.getSchedule();
				log.info("POS:"+posMachineCode);
				log.info("DATE:"+transactionDate);
				log.info("BATCH:"+batch);
				if("T2".equals(brandCode))
				{
					modifyPostingTallyAmount(brandCode, posMachineCode, transactionDate, POSTED, UNPOST, employeeCode, POSTED, batch,saleCount,saleAmount);
				}
				else
				{
					//modifyPostingTallyAmount(brandCode, buShopMachine.getId().getPosMachineCode(), null,transactionDate, POSTED, UNPOST, employeeCode, POSTED, batch);    					
				}
        	}*/



    		resultMsg = "銷售單"+ orderTypeCode +orderNo +"已完成反過帳！";

    		resultMap.put("resultMsg", resultMsg);
    		return resultMap;
    	} catch (FormException fe) {
    		log.error("執行POS反過帳作業時發生錯誤，原因：" + fe.toString());
    		throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    		log.error("執行POS反過帳作業時發生錯誤，原因：" + ex.toString());
    		throw new Exception("執行POS反過帳作業時發生錯誤，原因：" + ex.getMessage());
    	}
    }
    
    
    
    
    
}