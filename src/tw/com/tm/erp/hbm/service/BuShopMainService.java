package tw.com.tm.erp.hbm.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCustomerMod;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuPurchaseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandView;
import tw.com.tm.erp.hbm.bean.ImItemOnHandViewId;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopEmployeeDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuShopMachineId;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
public class BuShopMainService {
	private final static String SAVE = "SAVE";
	private final static String UPDATE = "UPDATE";
	private static final Log log = LogFactory.getLog(BuShopMainService.class);
	private BuShopEmployeeId buShopEmployeeId;
	private ImWarehouseDAO imWarehouseDAO;
	public void setBuShopEmployeeId(BuShopEmployeeId buShopEmployeeId) {
		this.buShopEmployeeId = buShopEmployeeId;
	}
	
	private BuBrandService buBrandService;

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	private BaseDAO baseDAO;

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;

	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	private BuShopDAO buShopDAO;

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	private BuShopEmployeeDAO buShopEmployeeDAO;

	public void setBuShopEmployeeDAO(BuShopEmployeeDAO buShopEmployeeDAO) {
		this.buShopEmployeeDAO = buShopEmployeeDAO;
	}

	private BuShopMachineDAO buShopMachineDAO;

	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}
	private BuShopMachine buShopMachine;

	public void setBuShopMachine(BuShopMachine buShopMachine) {
		this.buShopMachine = buShopMachine;
	}
	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}
	
	
	
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
			"shopCode", "salesWarehouseCode","enable" };
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "","" };
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "enable",
			"employeeCode", "employeeName" };
	
	//public static final String[] GRID_MACHINE_FIELD_NAMES = { "indexNo","enable", "id.posMachineCode", "uploadType", "lastOrderNo" };
	//public static final String[] GRID_MACHINE_FIELD_DEFAULT_VALUES = {"","", "", "","" };

	public static final String[] GRID_MACHINE_FIELD_NAMES = {  "posMachineCode", "uploadType", "lastOrderNo","enable","printerId" };
	public static final String[] GRID_MACHINE_FIELD_DEFAULT_VALUES = {"", "", "","","" };


	
	public static final String[] GRID_EMPLOYEE_FIELD_NAMES = {  "employeeCode", "employeeName","enable" };
	public static final String[] GRID_EMPLOYEE_FIELD_DEFAULT_VALUES = {"","","" };


	public static final String[] GRID_MACHINE_FIELD_READ_NAMES = {  "indexNo","posMachineCode", "uploadType", "lastOrderNo","enable","printerId" };
	public static final String[] GRID_MACHINE_FIELD_READ_DEFAULT_VALUES = {"","", "", "","","" };


	
	public static final String[] GRID_EMPLOYEE_FIELD_READ_NAMES = {  "indexNo","employeeCode", "employeeName","enable" };
	public static final String[] GRID_EMPLOYEE_FIELD_READ_DEFAULT_VALUES = {"","","","" };
	
	
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "","", "" ,"N" };

	public Map executeInitial(Map parameterMap) throws Exception
	{
		Object otherBean = parameterMap.get("vatBeanOther");
		HashMap resultMap = new HashMap();
		// BuShop buShop = null;
		try
		{
			Map multiList = new HashMap(0);
			BuShop buShop = this.executeFindActualShop(parameterMap);
			String brandCode = (String) PropertyUtils.getProperty(otherBean,"brandCode");

//下拉式選單
			// 專櫃類別
			List allshopType = buCommonPhraseLineDAO.findEnableLineById("ShopType");
			multiList.put("allshopType", AjaxUtils.produceSelectorData(allshopType, "lineCode", "name", false, false));
			//專櫃類型
			List allshopStyle = buCommonPhraseLineDAO.findEnableLineById("ShopStyle");
			multiList.put("allshopStyle", AjaxUtils.produceSelectorData(allshopStyle, "lineCode", "name", false, false));
			//專櫃等級
			List allshopLevel = buCommonPhraseLineDAO.findEnableLineById("ShopLevel");
			multiList.put("allshopLevel", AjaxUtils.produceSelectorData(allshopLevel, "lineCode", "name", false, false));
			//帳單類別
			List allbillType = buCommonPhraseLineDAO.findEnableLineById("ShopBillType");
			multiList.put("allbillType", AjaxUtils.produceSelectorData(allbillType, "lineCode", "name", false, false));
			//獎金分配類型
			List allsalesBonusType = buCommonPhraseLineDAO.findEnableLineById("ShopSalesBounsType");
			multiList.put("allsalesBonusType", AjaxUtils.produceSelectorData(allsalesBonusType, "lineCode", "name", false, false));
			List<ImWarehouse> allWarehouses	= baseDAO.findByProperty( "ImWarehouse"  , new String[] { "brandCode","enable" },new Object[] {brandCode,"Y"});
			multiList.put("allWarehouse", AjaxUtils.produceSelectorData(allWarehouses, "warehouseCode", "warehouseName",true, true));

			parameterMap.put("entityBean", buShop);
			resultMap.put("multiList", multiList);
			resultMap.put("form",buShop);
			return resultMap;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}

	private BuShop executeNew(Map parameterMap, BuBrand buBrand)
			throws Exception {
		log.info("new222");
		BuShop form = new BuShop();
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean,
				"brandCode");

		form.setShopCode("");
		form.setBrandCode(brandCode);
		form.setShopCName("");
		form.setEnable("Y");
		form.setCreationDate(new Date());
		return form;
	}

	public BuShop findById(String id) throws Exception {
		try {
			log.info("find333");
			BuShop buShop = (BuShop) buShopDAO.findByPrimaryKey(BuShop.class,id);
			log.info("buShop===" + buShop);
			log.info("cNAME===" + buShop.getShopCName());
			return buShop;

		} catch (Exception ex) {
			log.error("依據主鍵：" + id + " id時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + id + "查詢shopCode錯誤，原因："
					+ ex.getMessage());
		}
	}

	/**
	 * 依formId取得實際國別主檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuShop executeFindActualShop(Map parameterMap) throws FormException,Exception {

		// Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuShop buShop = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			buShop = !StringUtils.hasText(formIdString) ? this.executeNewShop() : this.findById(formIdString);

			parameterMap.put("entityBean", buShop);
			return buShop;
		} catch (Exception e) {
			log.error("取得庫別主檔失敗,原因:" + e.toString());
			throw new Exception("取得庫別主檔失敗,原因:" + e.toString());
		}
	}

	/**
	 * 產生一筆 BuCountry
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	public BuShop executeNewShop() throws Exception {
		System.out.println("executeNewShop().........");
		BuShop form = new BuShop();

		form.setShopCode("");
		form.setShopCName("");
		form.setShopEName("");
		form.setPlace("");
		form.setDepartmentName("");
		form.setEnable("Y");
		form.setSalesWarehouseCode("");

		return form;
	}

	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateShopBean(Map parameterMap) throws FormException,
			Exception {
		// TODO Auto-generated method stub

		BuShop buShop = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");

			buShop = (BuShop) parameterMap.get("entityBean");

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buShop);

			parameterMap.put("entityBean", buShop);
		} /*
			 * catch (FormException fe) { log.error("前端資料塞入bean失敗，原因：" +
			 * fe.toString()); throw new FormException(fe.getMessage()); }
			 */catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}

	/**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 */
	public Map updateAJAXBuShop(Map parameterMap) throws Exception {
		System.out.println("======異動店別單頭資料=========");
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");

			BuShop buShop = (BuShop) parameterMap.get("entityBean");
			// String shopCode = (String) PropertyUtils.getProperty(otherBean,
			// "shopCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean,
					"formId");
			BuShop buShopUpdate = null;

			String formAction = (String) PropertyUtils.getProperty(otherBean,
					"formAction");

			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				buShop.setCreatedBy(loginEmployeeCode);
				buShop.setCreationDate(new Date());
				buShop.setLastUpdatedBy(loginEmployeeCode);
				buShop.setLastUpdateDate(new Date());

				System.out.println("======準備異動店別單頭資料=========:");
				if (!StringUtils.hasText(formIdString)) {
					buShop.setCreatedBy(loginEmployeeCode);
					buShop.setCreationDate(new Date());
					buShopDAO.save(buShop);
				} else {
					buShopDAO.update(buShop);
				}

			}

			resultMsg = "buShop ：" + buShop.getShopCode() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buShop);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("主檔維護作業存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * ajax 取得倉庫search分頁
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest)
			throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號

			// String warehouseCode =
			// httpRequest.getProperty("warehouseCode");// 倉庫代碼
			String enable = httpRequest.getProperty("enable");
			String shopCode = httpRequest.getProperty("shopCode");
			String salesWarehouseCode = httpRequest
					.getProperty("salesWarehouseCode");
			System.out.println("Y".equals(enable));

			// String trueEnable = ("Y".equals(enable)? "N" : "Y");
			log.info("getAJAXSearchPageData----enable = " + enable);
			log.info("getAJAXSearchPageData----httpRequest = " + httpRequest);
			// String warehouseName = httpRequest.getProperty("warehouseName");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			log.info("tttttttttttttttttttttttttt");
			// ==============================================================
			findObjs.put(" and model.brandCode = :brandCode", brandCode);
			// findObjs.put(" and model.warehouseCode like :warehouseCode", "%"
			// + warehouseCode.toUpperCase() + "%");
			findObjs.put(" and model.shopCode like :shopCode", "%"
					+ shopCode.toUpperCase() + "%");
			findObjs.put(" and model.enable = :enable", enable);
			findObjs.put(
					" and model.salesWarehouseCode like :salesWarehouseCode",
					"%" + salesWarehouseCode.toUpperCase() + "%");
			// ==============================================================

			Map shopMap = buShopDAO.search("BuShop as model", findObjs,
					"order by shopCode asc", iSPage, iPSize,
					BaseDAO.QUERY_SELECT_RANGE);
			log.info("ooooooooooooooooooooooooooo");
			List<BuShop> buShop = (List<BuShop>) shopMap
					.get(BaseDAO.TABLE_LIST);

			if (buShop != null && buShop.size() > 0) {
				log.info("aaaaaaaaaaaaaaaaaaaaaa");
				// this.setOtherColumns(buShop);

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) buShopDAO.search("BuShop as model",
						"count(model.id.shopCode) as rowCount", findObjs,
						"order by shopCode desc", BaseDAO.QUERY_RECORD_COUNT)
						.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				log.info("bbbbbbbbbbbbbbbbbbbbbbbbbb");
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, buShop, gridDatas,
						firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的店別查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的倉庫查詢失敗！");
		}
	}

	/**
	 * ajax 庫存資料檔載入時,取得明細分頁資料
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */


	/**
	 * ajax 取得倉庫search分頁
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXMachinePageData(Properties httpRequest) throws Exception
	{
		try
		{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			// ======================帶入Head的值=============================
			String formId = httpRequest.getProperty("formId");
			String shopCode = httpRequest.getProperty("shopCode");
			String div = httpRequest.getProperty("div");
			log.info("formId"+formId);
			log.info("shopCode"+shopCode);
			// ==============================================================
			// ==============================================================
			String[] fieldNames={"id.shopCode","enable"};
			Object[] fieldValue={shopCode,"Y"};
			if(!(""==shopCode||null==shopCode||shopCode.equals(null)))
			{
				log.info("ShopCode="+shopCode);
				if(div.equals("4"))
				{
					//List<BuShopMachine> buShopMachines = buShopMachineDAO.findByProperty("BuShopMachine", fieldNames, fieldValue);
					List<BuShopEmployee> buShopEmployees = buShopEmployeeDAO.findPageLine(shopCode,iSPage, iPSize);
					// ==============================================================
					log.info("buShopMachines.size:" + buShopEmployees.size());

					if (buShopEmployees.size() > 0)
					{
						
			 	// 取得明細裏各人員姓名
							for (BuShopEmployee buShopEmployee : buShopEmployees)
							{
								buShopEmployee.setShopCode(buShopEmployee.getId().getShopCode());
								buShopEmployee.setEmployeeCode(buShopEmployee.getId().getEmployeeCode());
								buShopEmployee.setEmployeeName(UserUtils.getUsernameByEmployeeCode(buShopEmployee.getId().getEmployeeCode().trim().toUpperCase()));
							}
						 

						Long firstIndex =iSPage * iPSize + 1L; // 取得第一筆的INDEX
						Long maxIndex = buShopEmployeeDAO.findPageLineMaxIndex(shopCode,"Y"); // 取得最後一筆 INDEX
						result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_EMPLOYEE_FIELD_NAMES, GRID_EMPLOYEE_FIELD_DEFAULT_VALUES,buShopEmployees, gridDatas, firstIndex, maxIndex));
					} 
					else 
					{
						log.info("NO EMPLOYEE");
						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_EMPLOYEE_FIELD_NAMES,GRID_EMPLOYEE_FIELD_DEFAULT_VALUES,map, gridDatas));

					}
				}
				if(div.equals("5"))
				{
					//List<BuShopMachine> buShopMachines = buShopMachineDAO.findByProperty("BuShopMachine", fieldNames, fieldValue);
					List<BuShopMachine> buShopMachines = buShopMachineDAO.findPageLine(shopCode,"Y", iSPage, iPSize);
					// ==============================================================
					log.info("buShopMachines.size:" + buShopMachines.size());


					if (buShopMachines.size() > 0)
					{
						
			 	// 取得明細裏各人員姓名
				for (BuShopMachine buShopMachine : buShopMachines)
				{
					buShopMachine.setPosMachineCode(buShopMachine.getId().getPosMachineCode());
					buShopMachine.setShopCode(buShopMachine.getId().getShopCode());
					// buShopEmployee.setEmployeeName(UserUtils.getUsernameByEmployeeCode(buShopEmployee.getEmployeeCode().trim().toUpperCase()));
				}
						

						Long firstIndex =iSPage * iPSize + 1L; // 取得第一筆的INDEX
						Long maxIndex = buShopMachineDAO.findPageLineNaxIndex(shopCode); // 取得最後一筆 INDEX						result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_MACHINE_FIELD_NAMES, GRID_MACHINE_FIELD_DEFAULT_VALUES,buShopMachines, gridDatas, firstIndex, maxIndex));
					} 
					else 
					{
						log.info("NO MACHINE");
						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_MACHINE_FIELD_NAMES,GRID_MACHINE_FIELD_DEFAULT_VALUES,map, gridDatas));
					}
				}
			}
			else
			{
				log.info("尚未填入ShopCode");
				throw new Exception("尚未填入店別");

			}
			return result;
		} 
		catch (Exception ex) 
		{
			log.error("載入頁面顯示的倉庫查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的倉庫查詢失敗！");
		}
	}
    public List<Properties> updateOrSaveAJAXMachinePageData(Properties httpRequest)throws Exception {
   	 
    	try {
	    Date date = new Date();
	 
	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    
	    String shopCode = httpRequest.getProperty("shopCode");
	    String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
	    String formId = httpRequest.getProperty("formId");
	    Long indexNo = 0L;
	    String errorMsg = null;

	    if (!(formId.equals(null)||formId == "")) 
	    {
		    log.info("formId="+formId);
	    	// 將STRING資料轉成List Properties record data
	    	List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_MACHINE_FIELD_READ_NAMES);
	    	

		    if (upRecords.size()>0) 
		    {
		    	for (Properties upRecord : upRecords)
		    	{
		    		
		    		String posMachineCode = upRecord.getProperty("id.posMachineCode");
		    		String uploadType = upRecord.getProperty("uploadType");
		    		if(null==posMachineCode||posMachineCode.equals(""))
		    		{
		    			log.info("無資料");
		    		}
		    		else
		    		{
		    			log.info("有資料 posMachineCode="+posMachineCode);
		    			log.info("有資料 uploadType="+uploadType);
		    			if(StringUtils.hasText(posMachineCode))
		    			{	
		    				String[] fieldNames = {"id.shopCode","id.posMachineCode"};
		    				Object[] fieldValue = {shopCode,posMachineCode};
				    		//List<BuShopMachine> buShopMachines = buShopMachineDAO.findByProperty("BuShopMachine", fieldNames, fieldValue);
				    		//List<BuShopMachine> buShopMachines = buShopMachineDAO.findItemByIdentification(shopCode,posMachineCode);
		    				List<BuShopMachine> buShopMachines = new ArrayList();
				    		BuShopMachineId id = new BuShopMachineId();
				    		id.setPosMachineCode(upRecord.getProperty(posMachineCode));
				    		id.setShopCode(shopCode);
				    		
				    		
				    		
				    		if (buShopMachines.size() > 0)
				    		{
					    		BuShopMachine buShopMachine = buShopMachines.get(0);
				    			log.info("更新機台");    
				    			buShopMachine.setId(id);
				    			buShopMachine.setEnable(upRecord.getProperty("enable"));
				    			buShopMachine.setUploadType(upRecord.getProperty("uploadType"));
			    		          
				    			buShopMachine.setLastUpdatedBy(loginEmployeeCode);
				    			buShopMachine.setLastUpdateDate(date);
								buShopMachineDAO.update(buShopMachine);
									
								//}
							} else {
								
								log.info("新增機台");  
								BuShopMachine newBuShopMachine = new BuShopMachine();
								newBuShopMachine.setId(id);
								newBuShopMachine.setEnable(upRecord.getProperty("enable"));
								newBuShopMachine.setUploadType(upRecord.getProperty("uploadType"));
			    		          
								newBuShopMachine.setLastUpdatedBy(loginEmployeeCode);
								newBuShopMachine.setLastUpdateDate(date);
								newBuShopMachine.setCreatedBye(loginEmployeeCode);
								newBuShopMachine.setCreationDate(date);
								buShopMachineDAO.save(newBuShopMachine);
								
							}
				    	}
		    		}		    		
		    	}	
		    }
	    }
	    else
	    {
	    	errorMsg = "NO ShopCode";
	    }
	    
	    return AjaxUtils.getResponseMsg(errorMsg);

	    //if (warehouseCode == null || warehouseCode=="") {
	    //	System.out.println("我進來了~~~");
		//throw new ValidationErrorException("傳入的倉庫維護明細資料的主鍵為空值！");
	    //}
	} catch (Exception ex) {
	    log.error("更新倉庫維護檔明細時發生錯誤，原因：" + ex.toString());
	    ex.printStackTrace();
	    throw new Exception("更新倉庫維護檔明細失敗！" + ex.getMessage());
	}
	
	  //return new ArrayList();
    }
	/**
	 * 將資料檔查詢結果存檔
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * ajax picker按檢視返回選取的資料
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public List<Properties> getSearchSelection(Map parameterMap)
			throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			System.out.println("size:::" + result.size());
			if (result.size() > 0) {
				pickerResult.put("result", result);
			}
			resultMap.put("vatBeanPicker", pickerResult);
			resultMap.put("topLevel", new String[] { "vatBeanPicker" });
		} catch (Exception ex) {
			log.error("檢視失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "檢視失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}
		return AjaxUtils.parseReturnDataToJSON(resultMap);
	}
	 /**
     * 更新明細
     * 
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateOrSaveAJAXPageLinesData(Properties httpRequest)throws Exception {

    	try {
    		Date date = new Date();
    		String errorMsg = null;
    		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    		int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    		String shopCode = httpRequest.getProperty("shopCode");
    		String div = httpRequest.getProperty("div");
    		
    		
     		if(div.equals("4"))
    		{    	
     			log.info("專櫃人員明細存檔");
    			BuShopEmployeeId shopEmployeeId = new BuShopEmployeeId();

    			log.info("shopCode~~~~~"+shopCode);
    			if (shopCode != "") 
    			{
    				// 將STRING資料轉成List Properties record data
    				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_EMPLOYEE_FIELD_READ_NAMES);

    				System.out.println("==========upRecords=============="+upRecords.size());	
    				if (upRecords.size() >0) 
    				{
    					for (Properties upRecord : upRecords) {

    						String employeeCode = upRecord.getProperty("employeeCode");
    						log.info("employeeCode:"+employeeCode);
    						log.info("shopCode:"+shopCode);		    		
    						if(employeeCode.equals("")){
    							log.info("無人員資料需儲存");
    						}else{
    							if(StringUtils.hasText(employeeCode)){	
    								//List<BuShopMachine> machines = buShopMachineDAO.findByProperty("BuShopMachine", "id.posMachineCode", posMachineCode);
    								BuShop bushop = buShopDAO.findById(shopCode);
    								BuShopEmployee shopEmployee = buShopEmployeeDAO.findItemByIdentification(shopCode,employeeCode);
    								if (null!=shopEmployee){
    									log.info("更新專櫃人員");  
    									log.info("工號"+employeeCode);
    									log.info("店別"+shopCode);
    									shopEmployeeId.setEmployeeCode(employeeCode);
    									shopEmployee.getId().setEmployeeCode(employeeCode);
    									shopEmployee.setEnable(upRecord.getProperty("enable"));
    									buShopEmployeeDAO.update(shopEmployee);
    									//}
    								} else {

    									log.info("新增專櫃人員");  
    									BuShopEmployee newShopEmployee1 = new BuShopEmployee();
    									;
    									log.info("機台"+employeeCode);
    									log.info("店號"+shopCode);

    									shopEmployeeId.setEmployeeCode(employeeCode);
    									shopEmployeeId.setShopCode(shopCode);
    									newShopEmployee1.setIndexNo(1L);
    									newShopEmployee1.setId(shopEmployeeId);
    									newShopEmployee1.setEnable(upRecord.getProperty("enable"));

    									buShopEmployeeDAO.save(newShopEmployee1);
    								}

    							}

    						}
    					}	
    				}
    			}
    			else
    			{
    				errorMsg = "請輸入專櫃代碼！";
    			}
    		}
    		if(div.equals("5"))
    		{
    			log.info("專櫃機台明細存檔");
    			BuShopMachineId shopMachineId = new BuShopMachineId(); 




    			//String posMachineCode = httpRequest.getProperty("posMachineCode");
    			//log.info("11posMachineCode:"+posMachineCode);
    			log.info("shopCode~~~~~"+shopCode);
    			if (shopCode != "") 
    			{
    				// 將STRING資料轉成List Properties record data
    				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,gridLineFirstIndex, gridRowCount, GRID_MACHINE_FIELD_READ_NAMES);

    				System.out.println("==========upRecords=============="+upRecords.size());	
    				if (upRecords.size() >0) 
    				{
    					for (Properties upRecord : upRecords) {
    						log.info("upRecords~~~~~~~"+upRecords);
    						String posMachineCode = upRecord.getProperty("posMachineCode");
    						log.info("posMachineCode:"+posMachineCode+"shopCode~~"+shopCode);		    		
    						if(posMachineCode.equals("")){
    							log.info("無資料");
    						}else{
    							if(StringUtils.hasText(posMachineCode)){	
    								//List<BuShopMachine> machines = buShopMachineDAO.findByProperty("BuShopMachine", "id.posMachineCode", posMachineCode);
    								BuShop bushop = buShopDAO.findById(shopCode);
    								BuShopMachine machines = buShopMachineDAO.findItemByIdentification(shopCode,posMachineCode);
    								if (null!=machines){
    									log.info("更新機台明細");  
    									log.info("機台"+posMachineCode);
    									log.info("店號"+shopCode);
    									shopMachineId.setPosMachineCode(posMachineCode);
    									machines.getId().setPosMachineCode(posMachineCode);
    									machines.setUploadType(upRecord.getProperty("uploadType"));
    									machines.setPrinterId(upRecord.getProperty("printerId"));
    									machines.setEnable(upRecord.getProperty("enable"));
    									buShopMachineDAO.update(machines);
    									//}
    								} else {

    									log.info("新增機台明細");  
    									BuShopMachine newMachine1 = new BuShopMachine();
    									;
    									log.info("機台"+posMachineCode);
    									log.info("店號"+shopCode);

    									shopMachineId.setPosMachineCode(posMachineCode);
    									shopMachineId.setShopCode(shopCode);
    									newMachine1.setId(shopMachineId);
    									newMachine1.setUploadType(upRecord.getProperty("uploadType"));
    									newMachine1.setPrinterId(upRecord.getProperty("printerId"));
    									newMachine1.setEnable(upRecord.getProperty("enable"));

    									buShopMachineDAO.save(newMachine1);
    								}

    							}

    						}
    					}	
    				}
    			}
    			else
    			{
    				errorMsg = "請輸入專櫃代碼！";
    			}
    		}
			return AjaxUtils.getResponseMsg(errorMsg);
    	}catch (Exception ex) {
    	    log.error("更新倉庫維護檔明細時發生錯誤，原因：" + ex.toString());
    	    ex.printStackTrace();
    	    throw new Exception("更新倉庫維護檔明細失敗！" + ex.getMessage());
    	}
    }
	public List<Properties> getWarehouseNameByCode(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String salesWarehouseCode = httpRequest.getProperty("salesWarehouseCode"); 
		String brandCode = httpRequest.getProperty("brandCode"); 
		Properties inc = new Properties();	
		List re = new ArrayList();




		if(StringUtils.hasText(salesWarehouseCode))
		{
			ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, salesWarehouseCode, "Y");
			if(null!=imWarehouse){
				log.info(imWarehouse.getWarehouseCode()+"/"+imWarehouse.getWarehouseName());
				inc.setProperty("salesWarehouseName", imWarehouse.getWarehouseName());
				inc.setProperty("salesWarehouseCode", imWarehouse.getWarehouseCode());
			}
			else{
				log.info(salesWarehouseCode+"庫別不存在，請確認是否輸入正確");
				inc.setProperty("salesWarehouseName", "庫別不存在，請確認");
			}
		}
		else
		{
			log.info(salesWarehouseCode+"庫別不存在，請確認是否輸入正確");
			inc.setProperty("salesWarehouseName", "庫別不存在，請確認");
		}
		re.add(inc);
		return re;
	}
	public List<Properties> getShopByCode(Properties httpRequest) {
		//log.info("getAJAXFormDataBySuperintendent");
		//List re = new ArrayList();
		String shopCode = httpRequest.getProperty("shopCode"); 
		String brandCode = httpRequest.getProperty("brandCode"); 
		Properties inc = new Properties();	
		List re = new ArrayList();




		if(StringUtils.hasText(shopCode))
		{
			BuShop buShop = buShopDAO.findByBrandCodeAndShopCode(brandCode, shopCode);
			if(null!=buShop){
				inc.setProperty("message", "店別已存在，請勿重複建置");
			}
		}

		re.add(inc);
		return re;
	}
	
   	public SelectDataInfo exportShopDetail(HttpServletRequest httpRequest) throws Exception {
		try {
	
			String brandCode = httpRequest.getParameter("brandCode");
			String shopCode = httpRequest.getParameter("shopCode");
			String function = httpRequest.getParameter("function");

			log.info("shopCode:"+shopCode);
			log.info("brandCode:"+brandCode);
   			log.info("function:"+function);

   			
   			HashMap map = new HashMap();
			List rowData = new ArrayList();


			
			Object[] object = null;
			if(function.equals("Employee"))
			{
	   			List<BuShopEmployee> buShopEmployees = buShopEmployeeDAO.findBuEmployeeByShop(shopCode);
				log.info("buShopEmployees.size"+ buShopEmployees.size());	
				object = new Object[] { "shopCode", "employeeCode","employeeName", "enable"};

				for (int i = 0; i < buShopEmployees.size(); i++)
				{
					Object[] dataObject = new Object[object.length];
					dataObject[0] = buShopEmployees.get(i).getId().getShopCode();
					dataObject[1] = buShopEmployees.get(i).getId().getEmployeeCode();
					dataObject[2] = UserUtils.getUsernameByEmployeeCode(buShopEmployees.get(i).getId().getEmployeeCode().trim().toUpperCase());
					dataObject[3] = buShopEmployees.get(i).getEnable();
					rowData.add(dataObject);
				}
				

			}
			else if(function.equals("Machine"))
			{
				List<BuShopMachine> buShopMachines = buShopMachineDAO.findByShopCode(shopCode);
				log.info("buShopMachines.size"+ buShopMachines.size());	
				object = new Object[] { "shopCode", "posMachineCode","uploadType", "enable"};

				for (int i = 0; i < buShopMachines.size(); i++)
				{
					Object[] dataObject = new Object[object.length];
					dataObject[0] = buShopMachines.get(i).getId().getShopCode();
					dataObject[1] = buShopMachines.get(i).getId().getPosMachineCode();
					dataObject[2] = buShopMachines.get(i).getUploadType();
					dataObject[3] = buShopMachines.get(i).getEnable();
					rowData.add(dataObject);
				}
			}
			else{
				
				log.info("輸入錯誤，無輸入功能:"+function);
			}
			return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			 ex.printStackTrace();
			log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的調撥明細失敗！");
		}
	}
   	
   	
   
   	public void executeImportShopDetail(String shopCode,String loginEmployeeCode,String function, List lineLists) throws Exception{
    	try{

    			Date date = new Date();
    			if(lineLists != null && lineLists.size() > 0)
    			{
    				
    				for(int i = 0; i < lineLists.size(); i++)
    				{
    					if(function.equals("Employee")){
	    					BuShopEmployee tmp = (BuShopEmployee)lineLists.get(i);
	    					if(shopCode.equals(tmp.getShopCode())){
		    					
		    					BuShopEmployeeId id = new BuShopEmployeeId();
		    					id.setShopCode(shopCode);
		    					id.setEmployeeCode(tmp.getEmployeeCode());
		    					
		    					BuShopEmployee buShopEmployee = buShopEmployeeDAO.findItemByIdentification(id.getShopCode(),id.getEmployeeCode());
		    					if(buShopEmployee==null)
		    					{
		    						BuShopEmployee newBuShopEmployee = new BuShopEmployee();
		    						log.info("新增ShopEmployee:"+id.getEmployeeCode());
		    						newBuShopEmployee.setId(id);
		    						newBuShopEmployee.setEnable(tmp.getEnable());
		    						newBuShopEmployee.setIndexNo(1L);
		    						newBuShopEmployee.setCreationDate(date);
		    						newBuShopEmployee.setCreatedBy(loginEmployeeCode);
		    						newBuShopEmployee.setLastUpdateDate(date);
		    						newBuShopEmployee.setLastUpdatedBy(loginEmployeeCode);
		    						buShopEmployeeDAO.save(newBuShopEmployee);
		    					}
		    					else
		    					{
		    						buShopEmployee.setEnable(tmp.getEnable());
		    						buShopEmployee.setLastUpdateDate(date);
		    						buShopEmployee.setLastUpdatedBy(loginEmployeeCode);
		    						buShopEmployeeDAO.update(buShopEmployee);
		    					}
	
	    					}
	    					else
	    					{
	    						log.info("專櫃代碼不符，將不匯入資訊");
	    					}
	    				}
    					else if(function.equals("Machine"))
    					{
	    					BuShopMachine tmp = (BuShopMachine)lineLists.get(i);
	    					if(shopCode.equals(tmp.getShopCode())){
		    					
	    						BuShopMachineId id = new BuShopMachineId();
		    					id.setShopCode(shopCode);
		    					id.setPosMachineCode(tmp.getPosMachineCode());
		    					
		    					BuShopMachine buShopMachine = buShopMachineDAO.findById(id);
		    					if(buShopMachine==null)
		    					{
		    						BuShopMachine newBuShopMachine = new BuShopMachine();
		    						log.info("新增ShopMachine:"+id.getPosMachineCode());
		    						newBuShopMachine.setId(id);
		    						newBuShopMachine.setEnable(tmp.getEnable());
		    						newBuShopMachine.setCreationDate(date);
		    						newBuShopMachine.setUploadType(tmp.getUploadType());
		    						newBuShopMachine.setCreatedBye(loginEmployeeCode);
		    						newBuShopMachine.setLastUpdateDate(date);
		    						newBuShopMachine.setLastUpdatedBy(loginEmployeeCode);
		    						buShopEmployeeDAO.save(newBuShopMachine);
		    					}
		    					else
		    					{
		    						buShopMachine.setEnable(tmp.getEnable());
		    						buShopMachine.setUploadType(tmp.getUploadType());
		    						buShopMachine.setLastUpdateDate(date);
		    						buShopMachine.setLastUpdatedBy(loginEmployeeCode);
		    						buShopEmployeeDAO.update(buShopMachine);
		    					}
	
	    					}
	    					else
	    					{
	    						log.info("專櫃代碼不符，將不匯入資訊");
	    					}
	    				
    						
    					}
    					else
    					{
    						log.info("功能代碼錯誤，將不匯入任何資訊");
    						
    					}
    				}

    			}
    		}
    		catch (Exception ex) 
    		{
    			log.error("調整單明細匯入時發生錯誤，原因：" + ex.toString());
    			ex.printStackTrace();
    			throw new Exception("調整單明細匯入時發生錯誤，原因：" + ex.getMessage());
    		}
    	}
}
