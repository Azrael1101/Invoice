/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : PosDUService.java
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Sep 28, 2010	Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Date;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.PosImportDataAction;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.PosCurrency;
import tw.com.tm.erp.hbm.bean.PosCustomer;
import tw.com.tm.erp.hbm.bean.PosEmployee;
import tw.com.tm.erp.hbm.bean.PosExcessivePromotion;
import tw.com.tm.erp.hbm.bean.PosItemDiscount;
import tw.com.tm.erp.hbm.bean.PosItemEancode;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.bean.PosSalesUpload;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerCardDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionItemDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.dao.BuCurrencyDAO;
import tw.com.tm.erp.hbm.dao.ImItemDiscountDAO;
import tw.com.tm.erp.hbm.dao.BuExchangeRateDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class PosDUService {

    private static final Log log = LogFactory.getLog(PosDUService.class);

    private String uuId;
    
    private String ip;
    
    private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
    }
	
    private PosExportDAO posExportDAO;
    public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}
    private ImItemCategoryDAO imItemCategoryDAO;
    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	this.imItemCategoryDAO = imItemCategoryDAO;
    }
    
    private ImItemDiscountDAO imItemDiscountDAO;
    public void setImItemDiscountDAO(ImItemDiscountDAO imItemDiscountDAO) {
	this.imItemDiscountDAO = imItemDiscountDAO;
    }
    
    private BuExchangeRateDAO buExchangeRateDAO;
    public void setBuExchangeRateDAO(BuExchangeRateDAO buExchangeRateDAO) {
	this.buExchangeRateDAO = buExchangeRateDAO;
    }
    
    private BuCurrencyDAO buCurrencyDAO;
    public void setBuCurrencyDAO(BuCurrencyDAO buCurrencyDAO) {
    	this.buCurrencyDAO = buCurrencyDAO;
        }
    
    private BuAddressBookService buAddressBookService;
	public void setBuAddressBookService(BuAddressBookService buAddressBookService) {
		this.buAddressBookService = buAddressBookService;
	}

    private PosImportDataAction posImportDataAction;
	public void setPosImportDataAction(PosImportDataAction posImportDataAction) {
		this.posImportDataAction = posImportDataAction;
	}

	private ImItemDiscountService imItemDiscountService;
	public void setImItemDiscountService(ImItemDiscountService imItemDiscountService) {
		this.imItemDiscountService = imItemDiscountService;
	}

	private ImPromotionMainService imPromotionMainService;
	public void setImPromotionMainService( ImPromotionMainService imPromotionMainService) {
		this.imPromotionMainService = imPromotionMainService;
	}

	private BuShopService buShopService;
	public void setBuShopService(BuShopService buShopService) {
		this.buShopService = buShopService;
	}

	private ImItemService imItemService;
	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}
	
	private BuBasicDataService buBasicDataService;
	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}
	
	private BuBrandService buBrandService;
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	
	private BuShopMachineDAO buShopMachineDAO;
	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}
	
	private BuCommonPhraseService buCommonPhraseService;
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	private BuEmployeeAwardService buEmployeeAwardService;
	
	public void setBuEmployeeAwardService(
			BuEmployeeAwardService buEmployeeAwardService) {
		this.buEmployeeAwardService = buEmployeeAwardService;
	}


	/**
	 * 提供POS機端更新完資料後，更新POS COMMAND的狀態
	 *
	 * @param request
	 * @throws ValidationErrorException
	 */
	public void updateComplete(HttpServletRequest request) throws Exception {
		Long batchId = Long.parseLong(request.getParameter("batchId")); // REQUEST 序號
		String company = request.getParameter("company"); // 公司名稱
		Map parameterMap = new HashMap();
		parameterMap.put("BATCH_ID", batchId);
		parameterMap.put("COMPANY", company);
		posExportDAO.updateComplete(parameterMap);
	}

	/**
     * Call store procedure取得DATA_ID
     *
     * @param request
     * @return
     * @throws ValidationErrorException
     */
	public String getDataId(String dataBase) throws Exception {
		return posExportDAO.getDataId(dataBase);
	}

	/**
	 * Call store procedure取得REQUEST_ID
	 *
	 * @param request
	 * @return
	 * @throws ValidationErrorException
	 */
	public Long getRequestId(String dataBase) throws Exception {
		return posExportDAO.getRequestId(dataBase);
	}

	/**
	 * Call store procedure取得RESPONSE_ID
	 *
	 * @param request
	 * @return
	 * @throws ValidationErrorException
	 */
	public Long getResponseId() throws Exception {
		return posExportDAO.getResponseId();
	}

	/**
	 * 設置回傳用的Command(ParameterMap)
	 *
	 * @param parameterMap
	 * @return
	 * @throws ValidationErrorException
	 */
	public static void setResponseCommand(HashMap parameterMap){
		parameterMap.put("TYPE", "RES");
    	parameterMap.put("ACTION", "E2P");
    	parameterMap.put("OPERATION", "P");
    	parameterMap.put("STATUS", "W_POS");
	}

	/**執行 POS Download
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
    	log.info("executeInitial");
        HashMap resultMap    = new HashMap();
        Object otherBean     = parameterMap.get("vatBeanOther");
        String brandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
        String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
        try{
        	BuBrand buBrand = buBrandService.findById( brandCode );
            resultMap.put("brandCode",	brandCode);
            resultMap.put("brandName",	buBrand.getBrandName());
            resultMap.put("createdBy",	employeeCode);
            resultMap.put("createdByName",	UserUtils.getUsernameByEmployeeCode(employeeCode));
	    	return resultMap;       	
        }catch (Exception ex) {
        	log.error("POS資料傳輸作業初始化失敗，原因：" + ex.getMessage());
	    	throw new Exception(ex.getMessage());
        }           
    }
    
    /**
     * 配至POS下傳的下拉選單
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findDownloadCommon(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    try{
	    	String brandCode =httpRequest.getProperty("brandCode");
	    	List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseService.getCommonPhraseLinesById("PosCommand",true);
	    	List<BuCommonPhraseLine> allDownloadFunction  = new ArrayList<BuCommonPhraseLine>(0);
	    	for (Iterator iterator = buCommonPhraseLines.iterator(); iterator.hasNext();) {
				BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine) iterator.next();
				if(buCommonPhraseLine.getAttribute1().indexOf("D") > -1)
					allDownloadFunction.add(buCommonPhraseLine);
			}
	    	
	    	List<BuShop> allShop = buShopService.findShopByBrandAndEnable(brandCode, "Y");
	    	
	    	List allBuShopMachines = buShopMachineDAO.findByBrandAndEnable(brandCode, "Y", "Y");
	    	List<BuShopMachine> allBuShopMachine  = new ArrayList<BuShopMachine>(0);
	    	if (allBuShopMachines != null && allBuShopMachines.size() > 0) {
	    		for (int i = 0; i < allBuShopMachines.size(); i++) {
	    			Object[] objArray = (Object[]) allBuShopMachines.get(i);
	    			BuShopMachine shopMachine = (BuShopMachine) objArray[1];
	    			allBuShopMachine.add(shopMachine);
	    		}
	    	}
	    	
	    	allDownloadFunction =  AjaxUtils.produceSelectorData(allDownloadFunction  ,"lineCode" ,"name",  false,  false);
	    	allShop =  AjaxUtils.produceSelectorData(allShop  ,"shopCode" ,"shopCName",  false,  true);
	    	allBuShopMachine =  AjaxUtils.produceSelectorData(allBuShopMachine  ,"posMachineCode" ,"posMachineCode",  false,  true);
	    	properties.setProperty("allDownloadFunction", AjaxUtils.parseSelectorData(allDownloadFunction));
		    properties.setProperty("allShop", AjaxUtils.parseSelectorData(allShop));
		    properties.setProperty("allBuShopMachine", AjaxUtils.parseSelectorData(allBuShopMachine));
		    
		    result.add(properties);
    		return result;
		}catch(Exception ex){
		    log.error("findDownloadCommon發生錯誤，原因：" + ex.getMessage());
		    throw new Exception("findDownloadCommon發生錯誤，原因：" + ex.getMessage());
		}
	}
    
    /**
     * 配至POS上傳的下拉選單
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> findUploadCommon(Properties httpRequest) throws Exception{
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
	    try{
	    	String brandCode =httpRequest.getProperty("brandCode");
	    	List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseService.getCommonPhraseLinesById("PosCommand",true);
	    	List<BuCommonPhraseLine> allUploadFunction  = new ArrayList<BuCommonPhraseLine>(0);
	    	for (Iterator iterator = buCommonPhraseLines.iterator(); iterator.hasNext();) {
				BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine) iterator.next();
				if(buCommonPhraseLine.getAttribute1().indexOf("U") > -1)
					allUploadFunction.add(buCommonPhraseLine);
			}
	    	
	    	List allBuShopMachines = buShopMachineDAO.findByBrandAndEnable(brandCode, "Y", "Y");
	    	List<BuShopMachine> allBuShopMachine  = new ArrayList<BuShopMachine>(0);
	    	if (allBuShopMachines != null && allBuShopMachines.size() > 0) {
	    		for (int i = 0; i < allBuShopMachines.size(); i++) {
	    			Object[] objArray = (Object[]) allBuShopMachines.get(i);
	    			BuShopMachine shopMachine = (BuShopMachine) objArray[1];
	    			allBuShopMachine.add(shopMachine);
	    		}
	    	}
	    	
	    	allBuShopMachine =  AjaxUtils.produceSelectorData(allBuShopMachine  ,"posMachineCode" ,"posMachineCode",  false,  false);
	    	allUploadFunction =  AjaxUtils.produceSelectorData(allUploadFunction  ,"lineCode" ,"name",  false,  false);
	    	
	    	properties.setProperty("allUploadFunction", AjaxUtils.parseSelectorData(allUploadFunction));
	    	properties.setProperty("allBuShopMachine", AjaxUtils.parseSelectorData(allBuShopMachine));
	    	
		    result.add(properties);
    		return result;
		}catch(Exception ex){
		    log.error("findUploadCommon發生錯誤，原因：" + ex.getMessage());
		    throw new Exception("findUploadCommon發生錯誤，原因：" + ex.getMessage());
		}
	}
    
    /**
     * 處理AJAX參數(查詢專櫃POS機號、預設庫別及庫別的倉管人員)
     * 
     * @param httpRequest
     * @return List<Properties>
     * @throws Exception
     */
    public List<Properties> getShopMachineForAJAX(Properties httpRequest) throws Exception {
    	List<Properties> result = new ArrayList();
    	Properties properties = new Properties();
    	String shopCode = "";
    	List allBuShopMachine = new ArrayList<BuShopMachine>(0);
    	try {
    		shopCode = httpRequest.getProperty("shopCode");
    		String brandCode = httpRequest.getProperty("brandCode");
    		
    		if(StringUtils.hasText(shopCode)){
    			allBuShopMachine = buShopMachineDAO.findByShopCode(shopCode);
    		}else{
    			List allBuShopMachines = buShopMachineDAO.findByBrandAndEnable(brandCode, "Y", "Y");
    	    	allBuShopMachine  = new ArrayList<BuShopMachine>(0);
    	    	if (allBuShopMachines != null && allBuShopMachines.size() > 0) {
    	    		for (int i = 0; i < allBuShopMachines.size(); i++) {
    	    			Object[] objArray = (Object[]) allBuShopMachines.get(i);
    	    			BuShopMachine shopMachine = (BuShopMachine) objArray[1];
    	    			allBuShopMachine.add(shopMachine);
    	    		}
    	    	}
    		}
    		
    		allBuShopMachine =  AjaxUtils.produceSelectorData(allBuShopMachine  ,"posMachineCode" ,"posMachineCode",  false,  true);
	    	properties.setProperty("allBuShopMachine", AjaxUtils.parseSelectorData(allBuShopMachine));
    		result.add(properties);
    		return result;
    	} catch (Exception ex) {
    		log.error("查詢專櫃代號 ：" + shopCode + "的POS機號時發生錯誤，原因：" + ex.getMessage());
    		throw new Exception("查詢POS機號失敗！");
    	}
    }
    
    public Long posOnlineImport(HashMap posDUMap) throws Exception{
    	Long responseId = -1L;
    	try{
    		String data_type = (String)posDUMap.get("DATA_TYPE");
    		posDUMap.put("DATA_ID", posExportDAO.getDataId(""));
    		
    		if("SOP".equals(data_type)){
    			posImportDataAction.executeDownloadTransfer(posDUMap);
    		}
    		posDUMap.put("TYPE", "REQ");
    		posDUMap.put("ACTION", "E2P");
    		posDUMap.put("OPERATION", "C");
			posExportDAO.createPosCommand(posDUMap);
    		responseId = 0L;
    	}catch (Exception ex){
    		log.error("執行POS資料上傳失敗，原因" + ex.getMessage());
    		throw ex;
    	}
    	return responseId;
    }
    
    private ImPromotionDAO imPromotionDAO;
    private ImPromotionItemDAO imPromotionItemDAO;
    private ImItemDAO imItemDAO;
    private ImItemEancodeDAO imItemEancodeDAO;
    private BuCustomerCardDAO buCustomerCardDAO;
    private BuCustomerDAO buCustomerDAO;
    private BuAddressBookDAO buAddressBookDAO;
    private ImItemPriceDAO imItemPriceDAO;	
    private BuEmployeeDAO buEmployeeDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}
	
	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
	}

    	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}
    
	public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
		this.imPromotionDAO = imPromotionDAO;
	}
	
	public void setImPromotionItemDAO(ImPromotionItemDAO imPromotionItemDAO) {
		this.imPromotionItemDAO = imPromotionItemDAO;
	}
	
	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}
	
	public void setImItemEancodeDAO(ImItemEancodeDAO imItemEancodeDAO) {
		this.imItemEancodeDAO = imItemEancodeDAO;
	}
	
	public void setBuCustomerCardDAO(BuCustomerCardDAO buCustomerCardDAO) {
		this.buCustomerCardDAO = buCustomerCardDAO;
	}
	
	public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
		this.buCustomerDAO = buCustomerDAO;
	}
	
	private static final String[] GRID_SEARCH_PMOS_FIELD_NAMES = {
	    "imPromotion.orderNo","itemCode","lastUpdatedBy","imPromotion.beginDate",
	    "lineId","imPromotion.orderNo"
	};
	
	private static final String[] GRID_SEARCH_PMOS_DEFAULT_FIELD_NAMES = {
	    "","","","","",""
	};
	
	private static final String[] GRID_SEARCH_ITEM_FIELD_NAMES = {
	    "itemCode","itemCName","unitPrice","lastUpdatedBy","lastUpdateDate",
	    "itemCode"
	};
	
	private static final String[] GRID_SEARCH_ITEM_DEFAULT_FIELD_NAMES = {
	    "","","","","",""
	};
	
	private static final String[] GRID_SEARCH_EAN_FIELD_NAMES = {
	    "eanCode","itemCode","lastUpdatedBy","lastUpdateDate",
	    "eanCode"
	};
	
	
	private static final String[] GRID_SEARCH_EAN_DEFAULT_FIELD_NAMES = {
	    "","","","",""
	};
	
	private static final String[] GRID_SEARCH_EMP_FIELD_NAMES = {
	    "employeeCode","employeeName","lastUpdatedBy","lastUpdateDate",
	    "employeeCode"
	};
	
	private static final String[] GRID_SEARCH_EMP_DEFAULT_FIELD_NAMES = {
	    "","","","",""
	};
	private static final String[] GRID_SEARCH_VIP_FIELD_NAMES = {
	    "cardNo","customerCode","lastUpdatedBy","lastUpdateDate",
	    "cardNo"
	};
	
	private static final String[] GRID_SEARCH_VIP_DEFAULT_FIELD_NAMES = {
	    "","","","",""
	};
	
	private static final String[] GRID_SEARCH_DSC_FIELD_NAMES = {
		"vipTypeCode", "reserve1", "itemDiscountType", "reserve2",
		"beginDate", "endDate", "discount", "lastUpdatedBy", "lastUpdateDate"
	};
	private static final String[] GRID_SEARCH_DSC_DEFAULT_FIELD_NAMES = {
		"","","","",
		"","","","",""
	};
	
	private static final String[] GRID_SEARCH_RATE_FIELD_NAMES = {
		"id.sourceCurrency", "currencyName", "exchangeRate", "id.beginDate", "lastUpdatedBy", "lastUpdateDate", "currencyEName", "id.organizationCode", "id.againstCurrency"
	};
	private static final String[] GRID_SEARCH_RATE_DEFAULT_FIELD_NAMES = {
		"","","","","","","","",""
	};
	
	
	public List<Properties> getPOSDownSearch(Properties httpRequest) throws Exception{
			Map resultMap = new HashMap(0);
			
			List<Properties> result = new ArrayList();
	  	    List<Properties> gridDatas = new ArrayList();
	  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	  	    
	  	    StringBuffer hql = new StringBuffer();
	  	    
	  	    String dataType = httpRequest.getProperty("dataType"); 
	  	    String dateStart = httpRequest.getProperty("dateStart");
	  	    String dateEnd = httpRequest.getProperty("dateEnd");
	  	    String priceDate = httpRequest.getProperty("priceDate");
	  	    String selectall = httpRequest.getProperty("selectall");
	  	    
	  	    
	  	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	  	    Date dStart = ("".equals(dateStart)?null:dateFormat.parse(dateStart));
	  	    Date dEnd = ("".equals(dateEnd)?null:dateFormat.parse(dateEnd));
	  	    Date dPrice = ("".equals(priceDate)?null:dateFormat.parse(priceDate));
	  	    
	  	    
	  	    hql.append(" ");
	  	  
	  	    if(dataType.equals("PMOS")){
		  	    String promotionCode = httpRequest.getProperty("promotionCode");
		  	    String promotionItemCode = httpRequest.getProperty("promotionItemCode");
		  	    String brandCode = httpRequest.getProperty("brandCode");
		  	    
		  	    HashMap map = new HashMap();
		  	    HashMap findObjs = new HashMap();
		  	    List<ImPromotion> imPmo = null;
		  	    
		  	    if(dateStart != null && !"".equals(dateStart)){
		  	    	hql.append(" and to_char(model.imPromotion.beginDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	if (dateEnd != null && !"".equals(dateEnd)){
		  	    		hql.append(" and to_char(model.imPromotion.endDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	}
		  	    }
		  	    
		  	    if("".equals(promotionCode) && !"".equals(promotionItemCode)){
		  		//用itemCode在impromotionitem中找出impromotion的單號
		  		findObjs.put(" and model.itemCode like :promotionItemCode", promotionItemCode);
		  	    }else if(!"".equals(promotionCode) && "".equals(promotionItemCode)){
		  		//用pormotionCode找出促銷單號之下所有商品
		  		imPmo = imPromotionDAO.findByOrderNo(promotionCode);
		  		Long headId = null;
		  		if(imPmo != null ||imPmo.size()>0){
		  		    headId = (imPmo.get(0)).getHeadId();
		  		}
		  		 findObjs.put(" and model.imPromotion.headId = :headId", headId);  
		  	    }else if(!"".equals(promotionCode) && !"".equals(promotionItemCode)){
	
		  		findObjs.put(" and model.itemCode like :promotionItemCode", promotionItemCode);
		  		findObjs.put(" and model.imPromotion.orderNo like :orderNo", promotionCode);
		  	    }
		  	    
		  	    	 findObjs.put(" and model.imPromotion.brandCode = :brandCode", brandCode);
		  	    	 	
		 		Map imPromotionItemMap = null;
		 		List<ImPromotionItem> imPmoItems = null;	
		 			
	 			if (selectall.equals("Y"))
	 			{
	 				imPromotionItemMap = imPromotionDAO.search("ImPromotionItem as model",findObjs,hql.toString() + " order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	 				imPmoItems = (List<ImPromotionItem>) imPromotionItemMap.get("lineId");
	 			}
	 			else
	 			{
			  	     imPromotionItemMap = imPromotionDAO.search("ImPromotionItem as model",findObjs,hql.toString() + " order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			  		 imPmoItems = (List<ImPromotionItem>) imPromotionItemMap.get(BaseDAO.TABLE_LIST);
	 			}
 
		  		if (imPmoItems != null && imPmoItems.size() > 0) {
				  	Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    
				  	Long maxIndex = (Long)imPromotionDAO.search("ImPromotionItem as model", "count(model.itemCode) as rowCount" ,findObjs, hql.toString() + "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX; 
				  	
		  			if (selectall.equals("Y"))
		  			{
		  				AjaxUtils.updateAllResult(AjaxUtils.TIME_SCOPE, selectall, imPmoItems);
		  			}
		  			else
		  			{
		  				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_PMOS_FIELD_NAMES, GRID_SEARCH_PMOS_DEFAULT_FIELD_NAMES, imPmoItems, gridDatas, firstIndex, maxIndex));
				  	}
			  	}else {
			  		if (selectall.equals("N"))
		  	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_PMOS_FIELD_NAMES, GRID_SEARCH_PMOS_DEFAULT_FIELD_NAMES, map,gridDatas));
			  	} 
	  	    }else if(dataType.equals("ITEM")){
	  		    String itemCode = httpRequest.getProperty("ItemCode");
	  		    String brandCode = httpRequest.getProperty("brandCode");
		  	    HashMap findObjs = new HashMap();
		  	    HashMap map = new HashMap();
		  	    
		  	    if(dateEnd != null && !"".equals(dateEnd)){
		  	    	hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	if (dateStart != null && !"".equals(dateStart)){
		  	    		hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	}
		  	    }
		  	    
		  	    if(!"".equals(itemCode)){
		  		findObjs.put(" and model.itemCode like :itemCode", "%"+itemCode+"%");
		  	    }
		  	    findObjs.put(" and model.brandCode = :brandCode", brandCode);
		  	    Map itemMap = imItemDAO.search("ImItem as model", findObjs, hql.toString() + " and model.enable = 'Y' order by model.lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	    List<ImItem> imItem = (List<ImItem>) itemMap.get(BaseDAO.TABLE_LIST);
		  	    List<ImItem> imItems = new ArrayList<ImItem>();
		  	    Double unitPrice =0D;
		  	    ImItem itemWithPrice = new ImItem();
		  	    List pList = new ArrayList();
		  	  System.out.println("=====SIZE===="+imItem.size());
		  	    if(imItem != null && imItem.size()>0){
		  		for(int i=0;i<imItem.size();i++){
		  		  System.out.println("----itemcode----"+(imItem.get(i)).getItemCode());
		  		  pList = imItemPriceDAO.getLatestByItemCode(imItem.get(i).getItemCode());
		  		itemWithPrice = imItem.get(i);
		  		  if(pList != null && pList.size()>0){
		  		      unitPrice = Double.valueOf(pList.get(0).toString());
		  		      System.out.println("====uniPrice====="+unitPrice);
		  		      itemWithPrice.setUnitPrice(unitPrice);	
		  		  }
		  		 
		  		  
		  		  imItems.add(itemWithPrice);
		  		}
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;// 取得第一筆的INDEX    	
		  		
		  	    	Long maxIndex = (Long) imItemDAO.search("ImItem as model","count(model.itemCode) as rowCount", findObjs, hql.toString() + " and model.enable = 'Y' order by itemCode", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
		  	    	
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_ITEM_FIELD_NAMES, GRID_SEARCH_ITEM_DEFAULT_FIELD_NAMES, imItems, gridDatas, firstIndex, maxIndex));
		  	    }else{
		  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_ITEM_FIELD_NAMES, GRID_SEARCH_ITEM_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }
	  	    }else if(dataType.equals("VIP")){
	  		String cardNo = httpRequest.getProperty("vipCode");
	  		String brandCode = httpRequest.getProperty("brandCode");
	  		HashMap map = new HashMap();
		  	HashMap findObjs = new HashMap();
		  	
	  	    if(dateEnd != null && !"".equals(dateEnd)){
	  	    	hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
	  	    	if (dateStart != null && !"".equals(dateStart)){
	  	    		hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
	  	    	}
	  	    }
		  	
		  	if(!"".equals(cardNo)){
		  	    findObjs.put(" and model.cardNo like :cardNo", cardNo);
		  	}
		  	findObjs.put(" and model.brandCode = :brandCode", brandCode);
		  	Map cusMap = buCustomerCardDAO.search("BuCustomerCard as model", findObjs, "and model.enable = 'Y' order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	List<BuCustomerCard> buCustomerCard = (List<BuCustomerCard>) cusMap.get(BaseDAO.TABLE_LIST);
		  	
		  	   if(buCustomerCard != null && buCustomerCard.size()>0){
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  	    	Long maxIndex = (Long) buCustomerCardDAO.search("BuCustomerCard as model","count(model.cardNo) as rowCount", findObjs, "order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
		  	    	
		  	    	String empName = "";
		  	    	BuAddressBook addrBook = new BuAddressBook();
		  	    	BuCustomer bCust = null;
		  	    	for(int i=0;i<buCustomerCard.size();i++){
		  	    		
		  	    		bCust = buCustomerDAO.findByCardNo(buCustomerCard.get(i).getCardNo());
		  	    	    addrBook = buAddressBookDAO.findByAddressBookId(bCust.getAddressBookId());
		  	    	    empName = addrBook.getChineseName();
		  	    	    buCustomerCard.get(i).setCustomerCode(buCustomerCard.get(i).getCustomerCode()+"－"+empName);
		  	    	}

		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_VIP_FIELD_NAMES, GRID_SEARCH_VIP_DEFAULT_FIELD_NAMES, buCustomerCard, gridDatas, firstIndex, maxIndex));
		  	    }else{
		  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_VIP_FIELD_NAMES, GRID_SEARCH_VIP_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }

	  	    }else if(dataType.equals("EAN")){
	  		String eanCode = httpRequest.getProperty("eanCode");
	  		String brandCode = httpRequest.getProperty("brandCode");
	  		HashMap map = new HashMap();
		  	HashMap findObjs = new HashMap();
		  	
	  	    if(dateEnd!=null && !"".equals(dateEnd)){
	  	    	hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
	  	    	if (dateStart!=null && !"".equals(dateStart)){
	  	    		hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
	  	    	}
	  	    }
	  	    
		  	if(!"".equals(eanCode)){
		  	    findObjs.put(" and model.eanCode like :itemCode", eanCode);
		  	}
		  	findObjs.put(" and model.brandCode = :brandCode", brandCode);
		  	Map itemMap = imItemEancodeDAO.search("ImItemEancode as model", findObjs, hql.toString() + " and model.enable = 'Y' order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	
		  	List<ImItemEancode> imItemEanCode = (List<ImItemEancode>) itemMap.get(BaseDAO.TABLE_LIST);
		  	
		  	   if(imItemEanCode != null && imItemEanCode.size()>0){
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  	    	Long maxIndex = (Long) imItemEancodeDAO.search("ImItemEancode as model","count(model.eanCode) as rowCount", findObjs, hql.toString() + " and model.enable = 'Y'order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);

		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_EAN_FIELD_NAMES, GRID_SEARCH_EAN_DEFAULT_FIELD_NAMES, imItemEanCode, gridDatas, firstIndex, maxIndex));
		  	    }else{
		  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_EAN_FIELD_NAMES, GRID_SEARCH_EAN_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }
	  	    }else if(dataType.equals("EMP")){
	  		String empNo =  httpRequest.getProperty("empNo");
	  		String brandCode = httpRequest.getProperty("brandCode");
	  		HashMap map = new HashMap();
		  	HashMap findObjs = new HashMap();
		  	if(!"".equals(empNo)){
		  	    findObjs.put(" and model.employeeCode like :employeeCode", empNo);
		  	}
		  	findObjs.put(" and model.brandCode = :brandCode", brandCode);
		  	Map empMap = buEmployeeDAO.search("BuEmployee as model", findObjs, " order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	
		  	List<BuEmployee> buEmp = (List<BuEmployee>) empMap.get(BaseDAO.TABLE_LIST);

		  	   if(buEmp != null && buEmp.size()>0){
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  	    	Long maxIndex = (Long) buEmployeeDAO.search("BuEmployee as model","count(model.employeeCode) as rowCount", findObjs, "order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
		  	    	String empName = "";
		  	    	BuAddressBook addrBook = new BuAddressBook();
		  	    	for(int i=0;i<buEmp.size();i++){
		  	    	   addrBook = buAddressBookDAO.findByAddressBookId(buEmp.get(i).getAddressBookId());
		  	    	   empName = addrBook.getChineseName();
		  	    	   buEmp.get(i).setEmployeeName(empName);
		  	    	}
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_EMP_FIELD_NAMES, GRID_SEARCH_EMP_DEFAULT_FIELD_NAMES, buEmp, gridDatas, firstIndex, maxIndex));
		  	    }else{
		  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_EMP_FIELD_NAMES, GRID_SEARCH_EMP_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }	
	  	   }else if(dataType.equals("DSC")){
		  		String brandCode = httpRequest.getProperty("brandCode");
		  		HashMap map = new HashMap();
			  	HashMap findObjs = new HashMap();
			  	
		  	    if(dateEnd!=null && !"".equals(dateEnd)){
		  	    	hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	if (dateStart!=null && !"".equals(dateStart)){
		  	    		hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	}
		  	    }
		  	    
		  	  findObjs.put(" and model.brandCode = :brandCode", brandCode);
		  	  
			  Map imItemDiscountMap = imItemDiscountDAO.search("ImItemDiscount as model", findObjs, hql.toString() + " and model.enable = 'Y' order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	  
			  List<ImItemDiscount> imItemDiscounts = (List<ImItemDiscount>) imItemDiscountMap.get(BaseDAO.TABLE_LIST); 
			  
			  if(imItemDiscounts != null && imItemDiscounts.size()>0){
				    Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  	    	Long maxIndex = (Long) imItemDiscountDAO.search("ImItemDiscount as model","count(model.discount) as rowCount", findObjs, hql.toString() + " and model.enable = 'Y'order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
				  
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_DSC_FIELD_NAMES, GRID_SEARCH_DSC_DEFAULT_FIELD_NAMES, imItemDiscounts, gridDatas, firstIndex, maxIndex));
			  }else{
			  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_DSC_FIELD_NAMES, GRID_SEARCH_DSC_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }
	  	   }else if(dataType.equals("RATE")){
	  		   
		  		String organizationCode = "TM";
		  		HashMap map = new HashMap();
			  	HashMap findObjs = new HashMap();
			  	
		  	    if(dateEnd!=null && !"".equals(dateEnd)){
		  	    	hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') >= '" + DateUtils.format( dStart, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	if (dateStart!=null && !"".equals(dateStart)){
		  	    		hql.append(" and to_char(model.lastUpdateDate, 'YYYYMMDD') <= '" + DateUtils.format( dEnd, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    	}
		  	    }
		  	    
		  	    if(priceDate!=null&&!"".equals(priceDate)){
		  	    	hql.append(" and to_char(model.id.beginDate, 'YYYYMMDD') = '" + DateUtils.format( dPrice, DateUtils.C_DATA_PATTON_YYYYMMDD) + "'");
		  	    }
		  	    
		  	    //findObjs.put(" and model.organizationCode = :organizationCode", organizationCode);

		  	    Map buExchangeRateMap = buExchangeRateDAO.search("BuExchangeRate as model", findObjs, hql.toString() + " and model.id.againstCurrency = 'NTD' order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	    
		  	    List<BuExchangeRate> buExchangeRates = (List<BuExchangeRate>) buExchangeRateMap.get(BaseDAO.TABLE_LIST); 
		  	    
		  	    if(buExchangeRates != null && buExchangeRates.size()>0){
				    Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  	    	Long maxIndex = (Long) buExchangeRateDAO.search("BuExchangeRate as model","count(*) as rowCount", findObjs, hql.toString() + " order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
		  	    	
		  	    	for (BuExchangeRate buExchangeRate : buExchangeRates) {
		  	    		if(StringUtils.hasText(buExchangeRate.getId().getSourceCurrency())){
		  	    			BuCurrency SCurrencyName = buCurrencyDAO.findByName(buExchangeRate.getId().getSourceCurrency());
		  	    			if(null != SCurrencyName){
		  	    				buExchangeRate.setCurrencyName(SCurrencyName.getCurrencyCName());
		  	    				buExchangeRate.setCurrencyEName(SCurrencyName.getCurrencyEName());
		  	    			}
		  	    		}
		  	    	}
		  	    	
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_RATE_FIELD_NAMES, GRID_SEARCH_RATE_DEFAULT_FIELD_NAMES, buExchangeRates, gridDatas, firstIndex, maxIndex));
		  	    }else{
			  		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_RATE_FIELD_NAMES, GRID_SEARCH_RATE_DEFAULT_FIELD_NAMES, map,gridDatas));
		  	    }
	  	   }
	  		  
	  	    
	  	  if (selectall.equals("Y"))
	  	  {
	  		  return AjaxUtils.parseReturnDataToJSON(resultMap);
	  	  }
	  	  else
	  	  {
	  		  return result;
	  	  }
	}
	
	public List<Properties> posDownPMOS(Map parameterMap) throws FormException, Exception{
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			if (result.size() > 0) {
				pickerResult.put("result", result);
			}
			
		} catch (Exception ex) {
			log.error("下傳失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "下傳失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}
	    return null;
	}
	
	public Long savePosDownEAN(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
		String uuId = posExportDAO.getDataId("");
		String brandCode = (String)parameterMap.get("BRAND_CODE");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		String eanCode = "";
		    List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		    
		    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue();  

		    if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    		for(int i=0;i<results.size();i++){
		    		    eanCode = results.get(i).getProperty("eanCode");
		    		    
		    		    System.out.println("result"+results.get(i).getProperty("eanCode")+"====brandc==="+brandCode);
		            	List<ImItemEancode> imItemEanList = imItemEancodeDAO.findByEanCodes(brandCode, eanCode);
		            	for(int j=0;j<imItemEanList.size();j++){
		            	    	ImItemEancode imItemEancode = (ImItemEancode)imItemEanList.get(j);
		    		        PosItemEancode posItemEancode = new PosItemEancode();
		    		        BeanUtils.copyProperties(imItemEancode, posItemEancode);
		    		        posItemEancode.setDataId(uuId);
		    		        if("Y".equals(imItemEancode.getEnable())){
		    		        	posItemEancode.setAction("U");
		    		        }else{
		    		        	posItemEancode.setAction("D");
		    		        }
		    		        posExportDAO.save(posItemEancode);
		            		}
		    		}
		    	}

			    parameterMap.put("DATA_ID", uuId);
			    parameterMap.put("NUMBERS", numbers);
			    responseId = posExportDAO.executeCommand(parameterMap);}
		   else{
			   throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
		   }
		return responseId;
	}
	
	    public List<Properties> saveEANSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_EAN_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }

	    public List<Properties> savePMOSSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_PMOS_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public List<Properties> saveItemSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_ITEM_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public List<Properties> saveVIPSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_VIP_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public List<Properties> saveEMPSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_EMP_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public List<Properties> saveDSCSearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_DSC_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public List<Properties> saveRATESearchResult(Properties httpRequest) throws Exception{
	        String errorMsg = null;
	    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_RATE_FIELD_NAMES);
	    	return AjaxUtils.getResponseMsg(errorMsg);
	    }
	    
	    public Long savePosDownPMOS(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
		String uuId = posExportDAO.getDataId("");
		String brandCode = (String)parameterMap.get("BRAND_CODE");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		Long lineId = 0L;
		
	    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
	    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
	    
		if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    		for(int i=0;i<results.size();i++){
		    		lineId = Long.valueOf(results.get(i).getProperty("lineId"));    
		    		ImPromotionItem imPmoItem = imPromotionItemDAO.findByLineId(lineId);
		    		PosExcessivePromotion pep = new PosExcessivePromotion();
		    		pep.setBeginDate(imPmoItem.getImPromotion().getBeginDate());
		    		pep.setHeadId(imPmoItem.getImPromotion().getHeadId());
		    		pep.setItemCode(imPmoItem.getItemCode());
		    		pep.setPromotionUnitPrice(imPmoItem.getDiscountAmount());
		    		pep.setAction("U");
		    		pep.setDataId(uuId);
		    		posExportDAO.save(pep);
		    		}
		    	}

		    parameterMap.put("DATA_ID", uuId);
		    parameterMap.put("NUMBERS", numbers);
		    responseId = posExportDAO.executeCommand(parameterMap);}
	   else{
		   throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
	   } 
		return responseId;
	}
	    
	    public Long savePosDownVIP(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
		String uuId = posExportDAO.getDataId("");
		String brandCode = (String)parameterMap.get("BRAND_CODE");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		String cardNo = "";
		    List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		    
		    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
		    
		    if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    	    for(int i=0;i<results.size();i++){
		    		cardNo = results.get(i).getProperty("cardNo");		    		
		    		BuCustomer buCustomer = buCustomerDAO.findByCardNo(cardNo);
		    		PosCustomer posCustomer = new PosCustomer();
		    		BuAddressBook buAddressBook = buAddressBookDAO.findByAddressBookId((Long)buCustomer.getAddressBookId());
		    		exportPosCustomer(buAddressBook, buCustomer, posCustomer);
		    		 BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService
				    .getBuCommonPhraseLine("VIPType", posCustomer
					    .getVipTypeCode());
			    if (null != buCommonPhraseLine) {
				posCustomer.setIdentification(buCommonPhraseLine
					.getAttribute4());
			    }
			    posCustomer.setDataId(uuId);
			    posExportDAO.save(posCustomer);
		    	    }
		    	}

		    parameterMap.put("DATA_ID", uuId);
		    parameterMap.put("NUMBERS", numbers);
		    responseId = posExportDAO.executeCommand(parameterMap);}
		   else{
			   throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
		   }   
		return responseId;
	}
	    
	    public Long savePosDownItem(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
		String uuId = posExportDAO.getDataId("");
		String brandCode = (String)parameterMap.get("BRAND_CODE");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		String itemCode = "";
		    List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		    
		    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
		    
		    if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    		for(int i=0;i<results.size();i++){
		    		    	itemCode = results.get(i).getProperty("itemCode");
		            		List<Object[]> result = imItemDAO.findbySelectedImItem(parameterMap,itemCode,brandCode);
		            		if(result != null && result.size()>0){
		            		    for(int l=0;l<result.size();l++){
		            		    }
			                //  防止超出十四碼問題
		            		    for (Object[] data : result) {
		            		    if(itemCode.length() < 14 ){

        			                //準備update的資料
        			                
        			        	Object[] record = new Object[24];
        			        	record[0] = "U";//Action
        			        	record[1] = brandCode;//BrandCode
        			        	record[2] = data[10];//Category01
        			        	record[3] = imItemCategoryDAO.findByCategoryCode(data[10]);//Category01Name
        			        	record[4] = data[11];//Category02
        			        	record[5] = imItemCategoryDAO.findByCategoryCode(data[11]);//Category02Name
        			        	record[6] = uuId;//data_id
        			        	record[7] = data[14];//decl_ratio
        			        	record[8] = "Y";//enable
        			        	record[9] = "";//is_after_count
        			        	record[10] = "";//is_before_count
        			        	record[11] = "";//is_change_price
        			        	record[12] = data[8];//is_service_item
        			        	record[13] = data[15];//is_tax
        			        	record[14] = data[16];//item_brand
        			        	record[15] = imItemCategoryDAO.findByCategoryCode(data[16]);//item_brand_name
        			        	record[16] = data[2];//item_c_name
        			        	record[17] = itemCode;//item_code
        			        	record[18] = data[17];//lot_control
        			        	record[19] = data[18];//min_ratio
        			        	record[20] = data[19];//original_unit_price
        			        	record[21] = data[5];//sales_unit
        			        	record[22] = data[6];//sell_unit_price
        			        	record[23] = data[13];//vip_discount
        			        	for(int k=0;k<record.length;k++){
        			        	    System.out.println("==rec==="+record[k]);
        			        	}
        			                imItemDAO.updatePosItem(record);
		    		        
//		    		        posItemEancode.setDataId(uuId);
//		    		        if("Y".equals(imItem.getEnable())){
//		    		        	posItemEancode.setAction("U");
//		    		        }else{
//		    		        	posItemEancode.setAction("D");
//		    		        }
//		    		        posExportDAO.save(posItemEancode);
		            		    }
		            		}
		            		}
		    		}
		    	}

		    parameterMap.put("DATA_ID", uuId);
		    parameterMap.put("NUMBERS", numbers);
		    responseId = posExportDAO.executeCommand(parameterMap);
		    
		    }else{
				   throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
			   }   
		return responseId;
	}
	    
	    public void exportPosCustomer(BuAddressBook buAddressBook,
		    BuCustomer buCustomer, PosCustomer posCustomer) {
		BeanUtils.copyProperties(buAddressBook, posCustomer);
		BeanUtils.copyProperties(buCustomer, posCustomer);
		posCustomer.setBrandCode(buCustomer.getId().getBrandCode());
		posCustomer.setCustomerCode(buCustomer.getId().getCustomerCode());
		posCustomer.setAction("U");
	    }


	    public Long savePosDownEMP(HashMap posDUMap)  throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
		String uuId = posExportDAO.getDataId("");
		Object pickerBean = posDUMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		String empNo = "";
		String deptCode = "";
		    List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		    List<BuEmployee> buEmp = new ArrayList<BuEmployee>();
		    
		    String strdownloadCount = (String)posDUMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
		    
		    if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    	    for(int i=0;i<results.size();i++){
		    		empNo = results.get(i).getProperty("employeeCode");		    		
		    		 buEmp = buEmployeeDAO.findByProperty("employeeCode", empNo);
		    		PosEmployee posEmp = new PosEmployee();
		    		BuAddressBook buAddressBook = new BuAddressBook();
		    		if(buEmp != null && buEmp.size()>0){
		    		    BuEmployee emp = buEmp.get(0);
		    		buAddressBook = buAddressBookDAO.findByAddressBookId(buEmp.get(0).getAddressBookId());
				
		    		if(emp.getArriveDate() != null){
				    posEmp.setArriveDate(emp.getArriveDate());
				}
				
				if(emp.getBrandCode() != null){
				    posEmp.setBrandCode(emp.getBrandCode());
				}

				if(buAddressBook.getChineseName() != null){
				    posEmp.setChineseName(buAddressBook.getChineseName());
				}

				posEmp.setDataId(uuId);
				
				if(emp.getEmployeeCode() != null){
				posEmp.setEmployeeCode(emp.getEmployeeCode());
				}
				
				if(buAddressBook.getDepartment() != null){
				    posEmp.setEmployeeDepartmentName(buAddressBook.getDepartment());    
				    List lineList = buCommonPhraseLineDAO.findByName(buAddressBook.getDepartment());
				    for (int j=0;j<lineList.size();j++){
					if("EmployeeDepartment".equals(((BuCommonPhraseLine)lineList.get(j)).getId().getBuCommonPhraseHead().getHeadCode())){
					    deptCode = ((BuCommonPhraseLine)lineList.get(j)).getId().getLineCode();
					}
				    }
				}
				
				posEmp.setEmployeeDepartment(deptCode);
				posEmp.setAction("U");
				posExportDAO.save(posEmp);
		    		}
		    	    }
		    	}
		    	posDUMap.put("DATA_ID", uuId);
		    	posDUMap.put("NUMBERS", numbers);
		    	System.out.println("==pos==");
		    responseId = posExportDAO.executeCommand(posDUMap);
		    System.out.println("===pos save===");}
			   else{
				   throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
			   }
		return responseId;
	}
	    
	    public Long savePosDownDSC(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
			String uuId = posExportDAO.getDataId("");
			String brandCode = (String)parameterMap.get("BRAND_CODE");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			String vipTypeCode = null;
			String itemDiscountType = null;
			
		    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
		    
		    if (results.size()==downloadCount){
		    	if(results != null && results.size()>0){
		    		for(int i=0;i<results.size();i++){
		    			vipTypeCode = String.valueOf(results.get(i).getProperty("vipTypeCode"));
		    			itemDiscountType = String.valueOf(results.get(i).getProperty("itemDiscountType"));
		    			
		    			ImItemDiscount imItemDiscount = imItemDiscountDAO.findById(brandCode, vipTypeCode, itemDiscountType);
				        PosItemDiscount posItemDiscount = new PosItemDiscount();
				        BeanUtils.copyProperties(imItemDiscount, posItemDiscount);
					    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("VIPType", imItemDiscount.getId().getVipTypeCode());
					    if(null != buCommonPhraseLine){
					        posItemDiscount.setIdentification(buCommonPhraseLine.getAttribute4());
					    }
				        posItemDiscount.setDataId(uuId);
				        posItemDiscount.setAction("U");
				        posExportDAO.save(posItemDiscount);
			    		
		    		}
		    		
				    parameterMap.put("DATA_ID", uuId);
				    parameterMap.put("NUMBERS", numbers);
				    responseId = posExportDAO.executeCommand(parameterMap);
				    System.out.println("responseId:"+responseId);
		    	}
		    }else{
				  throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
		    } 
				return responseId;
	    }
	    
	    public Long savePosDownRATE(HashMap parameterMap) throws FormException, Exception{
	    	Long responseId = -1L;
	    	Long numbers = 0L;
			String uuId = posExportDAO.getDataId("");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
			
			String sourceCurrency = null;
			String beginDate = null;
			
		    String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    int downloadCount = Integer.valueOf(strdownloadCount).intValue(); 
		    
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		    
		    System.out.println(results.size());
		    
		    if (results.size()==downloadCount){
		    	try{
		    	if(results != null && results.size()>0){
		    		for(int i=0;i<results.size();i++){
		    			
		    			parameterMap.put("currencyCode",results.get(i).getProperty("id.sourceCurrency"));
		    			parameterMap.put("priceDate", DateUtils.format(dateFormat.parse(results.get(i).getProperty("id.beginDate")), DateUtils.C_DATA_PATTON_YYYYMMDD));
		    			
		    			List resultsDetail = buCurrencyDAO.findCurrencyExchangeRateList(parameterMap);
		    			
		    			if(resultsDetail != null && resultsDetail.size() > 0){
		    				Iterator iterator = resultsDetail.iterator();
				    		while( iterator.hasNext()){
				    			Object[] obj = (Object[])iterator.next();
						        PosCurrency posCurrency = new PosCurrency();
						        posCurrency.setCurrencyCode(String.valueOf(obj[0]));
						        posCurrency.setCurrencyCName(String.valueOf(obj[1]));
						        posCurrency.setCurrencyEName(String.valueOf(obj[2]));
						        posCurrency.setExchangeRate(NumberUtils.round(((BigDecimal)obj[3]).doubleValue(),2));			        
						        posCurrency.setBeginDate((Date)obj[4]);
						        //將addressBook與customer複製到posCustomer
						        posCurrency.setDataId(uuId);
						        posCurrency.setAction("U");
						        						        
						        posExportDAO.save(posCurrency);
				    		}
		    			}
		    			else
		    			{
		    				throw new NoSuchDataException("查無下傳幣別資料！幣別:"+results.get(i).getProperty("id.sourceCurrency") + " 價格日期："+results.get(i).getProperty("id.beginDate"));
		    			}
		    		}
		    		
				    parameterMap.put("DATA_ID", uuId);
				    parameterMap.put("NUMBERS", numbers);
				    responseId = posExportDAO.executeCommand(parameterMap);
		    	}
		    	}catch (Exception ex) {
					log.error("savePosDownRATE.....................");
					ex.printStackTrace();
				}
		    }else{
				  throw new NoSuchDataException("勾選下傳筆數與輸入下傳筆教不同！");
		    } 
				return responseId;
	    }
	    
	    
	    public Long savePosResend(HashMap parameterMap) throws FormException, Exception
	    {
	    	Long responseId = -1L;
	    	Long numbers = 100L;
	    	String storeId = "001";
			String uuId = posExportDAO.getDataId("");
			try
			{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			
			String strdownloadCount = (String)parameterMap.get("DOWNLOAD_COUNT");
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		    //Date transactionDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, PropertyUtils.getProperty(formBindBean, "transactionDate").toString());
		    Date transactionDate = new Date();
		    String transactionNoStart = (String) PropertyUtils.getProperty(formBindBean, "transactionNoStart");
		    String transactionNoEnd = (String) PropertyUtils.getProperty(formBindBean, "transactionNoEnd");
		    String store = (String) PropertyUtils.getProperty(formBindBean, "store");
		    PosSalesUpload posSalesUpload = new PosSalesUpload();
		    
		    posSalesUpload.setDataId(uuId);
		    posSalesUpload.setAction("U");
		    posSalesUpload.setStore(storeId);
		    posSalesUpload.setTransactionDate(transactionDate);
		    posSalesUpload.setTransactionNoEnd(transactionNoEnd);
		    posSalesUpload.setTransactionNoStart(transactionNoStart);

						        						        
			posExportDAO.save(posSalesUpload);
		    parameterMap.put("DATA_ID", uuId);
		    parameterMap.put("NUMBERS", numbers);
		    log.info("DATA_ID = "+uuId);
		    responseId = posExportDAO.executeCommand(parameterMap);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return responseId;
	    }
	    
	    
	    public List<Properties> getPageSize(Properties httpRequest)throws Exception {	
	    	
			Properties pro = new Properties();
			List re = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小


			pro.setProperty("iPSize"			 ,    AjaxUtils.getPropertiesValue(iPSize,  ""));



			re.add(pro);
			return re;
		    }
		 
}
