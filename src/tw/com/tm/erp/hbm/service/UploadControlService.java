package tw.com.tm.erp.hbm.service;

import loadCustomsConfig.InitialCustomsConfig;
import tw.com.tm.erp.utils.NumberUtils;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.CustomsConfiguration;
import tw.com.tm.erp.hbm.bean.SiProgramLog;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CustomsContorl;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.CustomsContorlDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.hbm.dao.BuBatchConfigDAO;
import tw.com.tm.erp.hbm.bean.BuBatchConfig;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.UploadControl;
import tw.com.tm.erp.hbm.dao.UploadControlDAO;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.UploadControlList;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.dao.CmMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.SoDeliveryHead;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.hbm.service.ImMovementMainService;
import tw.com.tm.erp.hbm.service.ImGeneralAdjustmentService;
//import tw.gov.customs.www.outisld.OutisldStub;
public class UploadControlService {
    private static final Log log = LogFactory.getLog(CustmsControlService.class);
    
    
    private UploadControlList uploadControlList = new UploadControlList();
    private SiProgramLogAction siProgramLogAction;
    private IslandUploadService islandUploadService;
    private UploadControl uploadControl = new UploadControl();
    private UploadControlDAO uploadControlDAO = new UploadControlDAO();
    private BuBrandDAO buBrandDAO = new BuBrandDAO();
    private BuBatchConfigDAO buBatchConfigDAO = new BuBatchConfigDAO();
    private BuBatchConfig buBatchConfig = new BuBatchConfig();
    private CustomsContorl customsContorl = new CustomsContorl();
    private CustomsContorlDAO customsContorlDAO = new CustomsContorlDAO();
    private SoSalesOrderHead soSalesOrderHead = new SoSalesOrderHead();
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO = new SoSalesOrderHeadDAO();
    private CmMovementHead cmMovementHead = new CmMovementHead();
    private CmMovementHeadDAO cmMovementHeadDAO = new CmMovementHeadDAO();
    private ImAdjustmentHeadDAO imAdjustmentHeadDAO = new ImAdjustmentHeadDAO();
    private ImAdjustmentHead imAdjustmentHead = new ImAdjustmentHead();
    private ImMovementHead imMovementHead = new ImMovementHead();
    private ImMovementHeadDAO imMovementHeadDAO = new ImMovementHeadDAO();
    private SoDeliveryHead soDeliveryHead = new SoDeliveryHead();
    private SoDeliveryHeadDAO soDeliveryHeadDAO = new SoDeliveryHeadDAO();   
    private BuShopMachineDAO buShopMachineDAO = new BuShopMachineDAO();
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO = new BuCommonPhraseLineDAO();
    private BuCommonPhraseHeadDAO buCommonPhraseHeadDAO = new BuCommonPhraseHeadDAO();
    private ImDeliveryHeadDAO imDeliveryHeadDAO = new ImDeliveryHeadDAO();
	CmMovementService cmMovementService;
	ImMovementMainService imMovementMainService;
	ImGeneralAdjustmentService imGeneralAdjustmentService;

	


	public void setImMovementMainService(ImMovementMainService imMovementMainService) {
		this.imMovementMainService = imMovementMainService;
	}
	
	public void setCmMovementService(CmMovementService cmMovementService) {
		this.cmMovementService = cmMovementService;
	}
	
	public void setImGeneralAdjustmentService(ImGeneralAdjustmentService imGeneralAdjustmentService) {
		this.imGeneralAdjustmentService = imGeneralAdjustmentService;
	}
    public void seBuCommonPhraseHeadDAO(BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
        this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
    } 
    public void setSoDeliveryHeadDAO(SoDeliveryHeadDAO soDeliveryHeadDAO) {
        this.soDeliveryHeadDAO = soDeliveryHeadDAO;
    } 
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
        this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    } 
    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
        this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    } 
    public void setSoDeliveryHead(SoDeliveryHead soDeliveryHead) {
        this.soDeliveryHead = soDeliveryHead;
    } 
    public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
        this.imMovementHeadDAO = imMovementHeadDAO;
    }
    public void setImMovementHead(ImMovementHead imMovementHead) {
        this.imMovementHead = imMovementHead;
    }
    public void setImAdjustmentHead(ImAdjustmentHead imAdjustmentHead) {
        this.imAdjustmentHead = imAdjustmentHead;
    }
    public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
        this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
    }       

    public void setCmMovementHeadDAO(CmMovementHeadDAO cmMovementHeadDAO) {
        this.cmMovementHeadDAO = cmMovementHeadDAO;
    }    
    public void setCmMovementHead(CmMovementHead cmMovementHead) {
        this.cmMovementHead = cmMovementHead;
    }  
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
        this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }    
    public void setSoSalesOrderHead(SoSalesOrderHead soSalesOrderHead) {
        this.soSalesOrderHead = soSalesOrderHead;
    }  
    public void setCustomsContorlDAO(CustomsContorlDAO customsContorlDAO) {
        this.customsContorlDAO = customsContorlDAO;
    }   
    
    public void setCustomsContorl(CustomsContorl customsContorl) {
        this.customsContorl = customsContorl;
    } 
    public void setBuBatchConfig(BuBatchConfig buBatchConfig) {
        this.buBatchConfig = buBatchConfig;
    } 
    public void setBuBatchConfigDAO(BuBatchConfigDAO buBatchConfigDAO) {
        this.buBatchConfigDAO = buBatchConfigDAO;
    }  
    
    
    public void setUploadControlDAO(UploadControlDAO uploadControlDAO) {
        this.uploadControlDAO = uploadControlDAO;
    }
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;   
    } 
    public void setUploadControl(UploadControl uploadControl) {
        this.uploadControl = uploadControl;
    } 
    
    public void setUploadControlList(UploadControlList uploadControlList) {
        this.uploadControlList = uploadControlList;
    } 
    public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
    	this.buShopMachineDAO = buShopMachineDAO;
    }
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
        this.siProgramLogAction = siProgramLogAction;
    } 
    public void setIslandUploadService(IslandUploadService islandUploadService) {
        this.islandUploadService = islandUploadService;
    } 
    
    
    
    
    
    /*
     * 
     * 	private String orderTypeCode;
	private Long orderAmount;
	private Date schedualDate;
	private String schedual;
	private String lastUpdatedByBC;
	private Date lastBCUpdateDate;
	private String statusByBC;
	*/
    
    //public static final String[] GRID_FIELD_NAMES = {"index","orderTypeCode", "schedualDate", "schedual", "orderAmount","lastUpdatedByBC","lastBCUpdateDate","statusByBC"};
   // public static final int[] GRID_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING};
   // public static final String[] GRID_FIELD_DEFAULT_VALUES = {"","", "", "", "0.0","","",""};
    
    
    
/*    public static final String[] GRID_FIELD_NAMES = {"index","schedualDate","schedual","orderTypeCode", "orderAmount","statusByAC","lastUpdatedByAC","statusByED","lastUpdatedByED","statusByBC","lastUpdatedByBC"};
    public static final int[] GRID_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING};
    public static final String[] GRID_FIELD_DEFAULT_VALUES = {"","", "", "", "","","","","","",""};*/
    
    
    public static final String[] GRID_FIELD_NAMES = {"schedualDate","schedual","orderTypeCode", "orderAmount","statusByAC","button","lastUpdatedByAC","statusByED","button","lastUpdatedByED","statusByBC","button","lastUpdatedByBC"};
    public static final int[] GRID_FIELD_TYPES = {AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING};
    public static final String[] GRID_FIELD_DEFAULT_VALUES = {"", "", "", "","","","","","","","","",""};  
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
    	Map resultMap = new HashMap(0);
    	Date date = new Date();
    	String schedual;
    	//Map multiList = new HashMap(0);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	try{
    		System.out.println("executeDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitialexecuteDBFInitial");
    		Object otherBean = parameterMap.get("vatBeanOther");
    		String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    		String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
    		String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
    		String department = (String)PropertyUtils.getProperty(otherBean, "department");
    		String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
    		
    		
    		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("UploadControl","updateDelivery");
			String deliverySwitch = buCommonPhraseLine.getEnable();

//班次下拉
    		List<BuBatchConfig>  allBatch = buShopMachineDAO.findBatchTime();
    		resultMap.put("allBatch", AjaxUtils.produceSelectorData(allBatch, "batchId", "batchId", false, true));

//找到最後一次日關的時間
			List<BuBrand>  buBrands = buBrandDAO.findByProperty("BuBrand", "brandCode","T2");
			date = sdf.parse(buBrands.get(0).getDailyCloseDate());
			schedual=buBrands.get(0).getSchedule();
			log.info("現在的日關班次:"+date+"   第"+schedual+"班");
			log.info("department="+department);
//SHIFT下一次
			List<BuBatchConfig> buf = buBatchConfigDAO.findAll();
			String maxSchedule=Integer.toString(buf.size());
			if(department.equals("AC") || department.equals("ED"))
			{
				log.info("查核/入提");
/*				if(schedual.equals(maxSchedule))
				{
					schedual = "1";
					date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
				}
				else
				{
					schedual=String.valueOf(Integer.parseInt(schedual)+1);
				}*/
			}
			else if(department.equals("BC"))
			{
				log.info("商控");
			}
			else if(department.equals("SU"))
			{
				log.info("商控");
			}
			else
			{
				log.info("錯誤");
			}
			/*
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy/MM/dd");
			
			String dateString = sdf2.format(date);
			log.info("DATESTRING = "+dateString);
			List<UploadControl> uploadControls = uploadControlDAO.findThisSchedual("SOP",dateString,schedual,department);
			log.info("uploadControls.size() = "+uploadControls.size());
			if(uploadControls.size()>0)
			{		
//有資料
				uploadControl = uploadControls.get(0);
			}
			else
			{
//沒資料
				uploadControl = new UploadControl();
    			uploadControl.setSchedual(schedual);
    			uploadControl.setSchedualDate(date);
    			uploadControl.setStatusByAC("OFF");
    			uploadControl.setStatusByED("OFF");//-->新增
    			uploadControl.setOrderTypeCode("SOP");
			}*/

			

    		
    		//resultMap.put("statusByAC",uploadControl.getStatusByAC());
    		//resultMap.put("statusByED",uploadControl.getStatusByED());
    		//resultMap.put("orderTypeCode",uploadControl.getOrderTypeCode());
			resultMap.put("deliverySwitch",deliverySwitch);
    		resultMap.put("schedual",schedual);
    		resultMap.put("schedualDate",date);
    		resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
    		resultMap.put("employeeName", employeeName);
    		resultMap.put("employeeCode", loginEmployeeCode);
    		resultMap.put("brandCode", loginBrandCode);
			
    		return resultMap;
    	}catch(Exception ex){
    		log.error("匯出初始化失敗，原因：" + ex.toString());
    		ex.printStackTrace();
    		throw new Exception("匯出初始化失敗，原因：" + ex.toString());

    	}
    }
    
    
    
	public List<Properties> updateStatus(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			
			Date date = new Date();
			UploadControl uploadControl;
			String schedual = httpRequest.getProperty("schedual");
			log.info("schedual = " + schedual);
			String schedualDate = httpRequest.getProperty("schedualDate");
			log.info("schedualDate = " + schedualDate);
			String statusByAC = httpRequest.getProperty("statusByAC");
			log.info("statusByAC = " + statusByAC);
			String statusByED = httpRequest.getProperty("statusByED");
			log.info("statusByED = " + statusByED);
			String statusByBC = httpRequest.getProperty("statusByBC");
			log.info("statusByBC = " + statusByBC);
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			log.info("orderTypeCode = " + orderTypeCode);
			
			String department = httpRequest.getProperty("department");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			log.info("loginEmployeeCode = " + loginEmployeeCode);

			String flag = httpRequest.getProperty("flag");
			log.info("flag = " + flag);
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");			
		    log.info("=======================顯示明細==============================");
			log.info("schedual"+schedual);
			log.info("schedualDate"+sdf.parse(schedualDate));
			log.info("orderTypeCode"+orderTypeCode);
			log.info("=======================顯示明細==============================");		
			
			String[] fileName = {"schedual","schedualDate","orderTypeCode"};
			Object[] fileValue = {schedual,(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, schedualDate)),orderTypeCode};
		    List<UploadControl> uploadControls = uploadControlDAO.findByProperty("UploadControl", fileName, fileValue);
		    log.info("uploadControls.size()="+uploadControls.size());
		    log.info("=======================顯示明細==============================");
			log.info("getStatusByBC"+statusByBC);
			log.info("getStatusByED"+statusByED);
			log.info("getStatusByAC"+statusByAC);
			log.info("=======================顯示明細==============================");
	
			if(uploadControls.size() > 0)
		    {
		    	log.info("有資料");
		    	uploadControl = uploadControls.get(0);
		    	uploadControl.setSchedualDate(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, schedualDate));
		    	uploadControl.setOrderTypeCode(orderTypeCode);
		    	uploadControl.setStatusByAC(statusByAC);
		    	uploadControl.setStatusByED(statusByED);

		    	
		    	if(flag.equals("1"))
		    	{
		    		uploadControl.setLastUpdatedByAC(loginEmployeeCode);
			    	uploadControl.setLastACUpdateDate(date);
		    	}
		    	else if(flag.equals("2"))
		    	{
		    		uploadControl.setLastUpdatedByED(loginEmployeeCode);
			    	uploadControl.setLastEDUpdateDate(date);
		    	}
		    	
		    	log.info(flag + "  " + "AC:" +uploadControl.getStatusByAC() + "ED:" +uploadControl.getStatusByED() + "BC:" +uploadControl.getStatusByBC() );
		    	
		    	
		    	
		    	
		    	uploadControlDAO.update(uploadControl);
		    }
			else
			{
				log.info("執行錯誤，無資料");
			}
			//properties.setProperty("statusByAC", statusByAC);
			//properties.setProperty("statusByED", statusByED);
			//properties.setProperty("statusByBC", statusByBC);
			list.add(properties);
		}
		catch(Exception e)
		{
			log.error("取得指定的類別名稱發生問題，原因：" + e.toString());
			e.printStackTrace();
			throw new Exception("取得指定的類別名稱發生問題，原因：" + e.getMessage());
		}
		
		return list;
	}



	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception
	{
		try
		{ 
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			HashMap map = new HashMap();
			List<CustomsContorl> list = new ArrayList();
			//List<UploadControlList> allUploadControl = new ArrayList(); 
			List<UploadControlList> resultMap = new ArrayList();
			

			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// 要顯示的HEAD_ID
			Date todayDate = new Date();
			String schedual = httpRequest.getProperty("schedual");
			String schedualDate = httpRequest.getProperty("schedualDate");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String brandCode = httpRequest.getProperty("brandCode");
			String department = httpRequest.getProperty("department");
			String customsWarehouseCode = httpRequest.getProperty("customsWarehouseCode");
			
			
			log.info("department"+department);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
			//log.info("schedualDate"+sdf.parse(schedualDate));
			log.info("today"+sdf.format(todayDate));
			List<UploadControlList> uploadList = new ArrayList();
			HashMap conditionMap = new HashMap();
			conditionMap.put("deliveryControl","");
			conditionMap.put("schedualDate",DateUtils.format(todayDate, DateUtils.C_DATE_PATTON_SLASH));
			conditionMap.put("schedual",schedual);
			conditionMap.put("department",department);
			conditionMap.put("customsWarehouseCode",customsWarehouseCode);

			List<UploadControl> uploadControls = uploadControlDAO.findThisSchedual(conditionMap,iSPage,iPSize,"find");
			//maco 就是要改這裡!!!!
			for(UploadControl uc:uploadControls){
				int orderCount=0;
				HashMap conditionMap1 = new HashMap();
				String[] fileName = {"type","orderTypeCode"};
				Object[] fileValue = {"NF",uc.getOrderTypeCode()};
				list = customsContorlDAO.findByProperty("CustomsContorl", fileName, fileValue);
				
				conditionMap1.put("bean",list.get(0).getName());
				conditionMap1.put("orderType",uc.getOrderTypeCode());
				conditionMap1.put("orgOrderTypeCode",list.get(0).getCategory01());
				conditionMap1.put("findSchedual",uc.getSchedual());
				conditionMap1.put("findSchedualDateString",DateUtils.format(uc.getSchedualDate(),DateUtils.C_DATE_PATTON_SLASH));
				conditionMap1.put("statusByBC",uc.getStatusByBC());
				conditionMap1.put("department",department);
				conditionMap1.put("brandCode",brandCode);
				conditionMap1.put("customsWarehouseCode",list.get(0).getCustomsWarehouseCode());

				List<Object> bean = uploadControlDAO.findWaitToSend(conditionMap1);
				orderCount=bean.size();
				log.info("單據數量:"+orderCount);
				uc.setOrderAmount(orderCount+0L);
			}



			Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;
			List<UploadControl> a = uploadControlDAO.findThisSchedual(conditionMap,iSPage,iPSize,"count");
			Long maxIndex = a.size()+0L;
			

			result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, uploadControls, gridDatas,firstIndex, maxIndex));		    

			return result;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log.error("附件查詢時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢附件資料失敗！");
		}
	}




	 public List<Properties> updateperformTransaction (Properties httpRequest) throws Exception{
			List list = new ArrayList();
			Properties properties = new Properties();
			try{
				
				Date date = new Date();
				UploadControl uploadControl;
				log.info("=======================取得資料=======================");
				String schedual = httpRequest.getProperty("schedual");
				log.info("schedual = " + schedual);
				String brandCode = httpRequest.getProperty("brandCode");
				log.info("brandCode = " + brandCode);
				String department = httpRequest.getProperty("department");
				log.info("department = " + department);
				String schedualDate = httpRequest.getProperty("schedualDate");
				log.info("schedualDate = " + schedualDate);
				String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
				log.info("loginEmployeeCode = " + loginEmployeeCode);
				String statusByBC = httpRequest.getProperty("statusByBC");
				log.info("statusByBC = " + statusByBC);
				String orderTypeCode = httpRequest.getProperty("orderTypeCode");
				log.info("orderTypeCode = " + orderTypeCode);
				
				//statusByBC = "OFF";
				//orderTypeCode ="SOP";
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");	

			    
			    
//*==============================================抓修改單據清單===============================================================
			    HashMap conditionMap1 = new HashMap();
				String[] fileName1 = {"type","orderTypeCode"};
				Object[] fileValue1 = {"NF",orderTypeCode};
				List<CustomsContorl> cc = customsContorlDAO.findByProperty("CustomsContorl", fileName1, fileValue1);
				
				conditionMap1.put("bean",cc.get(0).getName());
				conditionMap1.put("orderType",orderTypeCode);
				conditionMap1.put("orgOrderTypeCode",cc.get(0).getCategory01());
				conditionMap1.put("findSchedual",schedual);
				conditionMap1.put("findSchedualDateString",schedualDate);
				conditionMap1.put("statusByBC",statusByBC);
				conditionMap1.put("loginEmployeeCode",loginEmployeeCode);
				conditionMap1.put("department",department);
				conditionMap1.put("brandCode",brandCode);
				conditionMap1.put("customsWarehouseCode",cc.get(0).getCustomsWarehouseCode());

				List<Object[]> bean = uploadControlDAO.findWaitToSend(conditionMap1);
				conditionMap1.put("beanList",bean);
			    log.info("=======================放行前資料檢核=======================");
				//checkList(conditionMap1);

				log.info("影響單據:"+bean.size()+"筆");
//*===========================================================================================================================
				String[] fileName = {"schedual","schedualDate","orderTypeCode"};
				Object[] fileValue = {schedual,sdf.parse(schedualDate),orderTypeCode};
			    List<UploadControl> uploadControls = uploadControlDAO.findByProperty("UploadControl", fileName, fileValue);
			    log.info("uploadControls.size()="+uploadControls.size());
			    log.info("=======================更新海關放行資訊=======================");
			    log.info(cc.get(0).getName()+"放行作業開始");
			    //更新UploadControl
			    if(uploadControls.size() == 0)
			    {
			    	log.info("無資料");
			    	UploadControl newUploadControl = new UploadControl();
			    	newUploadControl.setOrderTypeCode(orderTypeCode);
			    	newUploadControl.setSchedualDate(sdf.parse(schedualDate));
			    	newUploadControl.setSchedual(schedual);
			    	newUploadControl.setOrderTypeCode(orderTypeCode);
			    	newUploadControl.setStatusByBC(statusByBC);
			    	newUploadControl.setLastUpdatedByBC(loginEmployeeCode);
			    	newUploadControl.setLastBCUpdateDate(date);
			    	uploadControlDAO.save(newUploadControl);
			    	log.info("SAVE成功");
			    }
			    else
			    {
			    	log.info("有資料");
			    	uploadControl = uploadControls.get(0);
			    	uploadControl.setSchedualDate(sdf.parse(schedualDate));
			    	uploadControl.setOrderTypeCode(orderTypeCode);
			    	uploadControl.setLastUpdatedByBC(loginEmployeeCode);
			    	uploadControl.setStatusByBC(statusByBC);
				    uploadControl.setLastBCUpdateDate(date);
			    	uploadControlDAO.update(uploadControl);
			    	log.info("UPDATE成功");
			    }
						
			    
/*			    List<SiProgramLog> allLog = siProgramLogAction.findByIdentification("Upload", "ERROR", "IslandOrInternationUpload");
			    for(int i=0;i<allLog.size();i++){
					siProgramLogAction.deleteProgramLog(allLog.get(i).getProgramId(), allLog.get(i).getLevelType(),allLog.get(i).getIdentification());
				}*/
			    int count=1;
			    //更新單據
			    log.info("=======================回寫單據狀態=======================");
			    for(Object[] headBean:bean){
					siProgramLogAction.deleteProgramLog("Upload", "COUNT", "IslandOrInternationUpload");
					siProgramLogAction.createProgramLog("Upload", "COUNT", "IslandOrInternationUpload", count+"/"+bean.size(), loginEmployeeCode);
					//Thread.sleep(10L);
					Long headId = Long.parseLong(((BigDecimal)headBean[0]).toString());
					//log.info("HEAD_ID:"+headId);
			    	if(cc.get(0).getName().equals("SO_SALES_ORDER_HEAD"))
			    	{
			    		SoSalesOrderHead so = (SoSalesOrderHead)soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, headId);
			    		so.setCustomsStatus("A");
			    		so.setTranAllowUpload("I");
			    		soSalesOrderHeadDAO.update(so);
			    	}
			    	else if(cc.get(0).getName().equals("IM_ADJUSTMENT_HEAD"))
			    	{
			    		ImAdjustmentHead adj = (ImAdjustmentHead)imAdjustmentHeadDAO.findByPrimaryKey(ImAdjustmentHead.class, headId);
			    		adj.setCustomsStatus("A");
			    		adj.setTranAllowUpload("I");
			    		imAdjustmentHeadDAO.update(adj);
			    	}
			    	else if(cc.get(0).getName().equals("IM_DELIVERY_HEAD"))
			    	{
			    		ImDeliveryHead imD = (ImDeliveryHead)imDeliveryHeadDAO.findByPrimaryKey(ImDeliveryHead.class, headId);
			    		imD.setCustomsStatus("A");
			    		imD.setTranAllowUpload("I");
			    		imDeliveryHeadDAO.update(imD);
			    	}
			    	else if(cc.get(0).getName().equals("SO_DELIVERY_HEAD"))
			    	{
			    		SoDeliveryHead soD = (SoDeliveryHead)soDeliveryHeadDAO.findByPrimaryKey(SoDeliveryHead.class, headId);
			    		soD.setCustomsStatus("A");
			    		soD.setTranAllowUpload("I");
			    		soDeliveryHeadDAO.update(soD);
			    	}
			    	else if(cc.get(0).getName().equals("IM_MOVEMENT_HEAD"))
			    	{
			    		ImMovementHead im = (ImMovementHead)imMovementHeadDAO.findByPrimaryKey(ImMovementHead.class, headId);
			    		im.setCustomsStatus("A");
			    		im.setTranAllowUpload("I");
			    		imMovementHeadDAO.update(im);
			    	}
			    	else if(cc.get(0).getName().equals("CM_MOVEMENT_HEAD"))
			    	{
			    		CmMovementHead cm = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
			    		cm.setCustomsStatus("A");
			    		cm.setTranAllowUpload("I");
			    		cmMovementHeadDAO.update(cm);
			    	}
			    	else
			    	{
			    		throw new Exception("查無此張單據");
			    	}
			    	count++;
			    }
	    		properties.setProperty("message", "檢誤結束，放行成功");
			    log.info("共計"+bean.size()+"筆  時間:"+new Date());
	    		log.info("=======================放行成功=======================");
				//properties.setProperty("statusByBC", statusByBC);
				list.add(properties);
			}
			catch (Exception e)
			{
				properties.setProperty("message", "檢誤結束，放行失敗！");
				list.add(properties);
				log.error("執行維護單流程時發生錯誤，原因：" + e.toString());

			}
			return list;
	 }
	 public void checkList(HashMap conditionMap) throws Exception{
		 log.info("=======================checkList=======================");

			//載入總開關
			BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("CheckSwitch", "CheckSwitchInternation");
				List<String> exceptions = new ArrayList();
				Map returnMap = new HashMap();
			//載入設定檔	
				List<CustomsConfiguration> config = imAdjustmentHeadDAO.getCustomsConfiguration();
			

					List<Object> heads = uploadControlDAO.findWaitToSend(conditionMap);
					int count = 0;
					List<Object> dataList = new ArrayList();
					
					for(Object head:heads){
						try{
							count++;

							
							//若總開關開啟則放行
							if(buCommonPhraseLine.getAttribute1().equals("1")){
								
								
								
								
								List<CustomsConfiguration> DFconfigs =  getConfigs((String)conditionMap.get("uploadType"),config);
								dataList.add(head);
								returnMap = islandUploadService.dataBind(DFconfigs, "XML", dataList);
								try{
									log.info("印看看XML:"+(String)returnMap.get("XML"));
								}catch(Exception e){
										e.printStackTrace();
										log.info("印不出來");
								}
								
								
							}

						}catch(Exception e){
							e.printStackTrace();
							String exceptionMsg = e.getMessage();
							exceptions.add("[第 "+(count-1)+"筆] 異常:"+exceptionMsg);
						}
						if(null != exceptions && exceptions.size()>0){
							for(String s:exceptions){
								//siProgramLogAction.createProgramLog("Upload", "ERROR", "IslandOrInternationUpload", s, loginEmployeeCode);
							}
							throw new Exception("檢誤結束，發現異常：");
						}
						dataList = null;
			    	}

				
				
				/*
				if(orderTypeCode.toString().equals("WMF")){//NF13
					List<ImMovementHead> imMovementHeads = uploadControlDAO.findWaitToSend("ImMovementHead",orderTypeCode,schedual,schedualDate,statusByBC,department);
					int count = 0;
					List<ImMovementHead> dataList = new ArrayList();
					
					for(ImMovementHead im:imMovementHeads){
						try{
							count++;
							//紀錄LOG
							System.out.println("COUNT");
							siProgramLogAction.deleteProgramLog("Upload", "COUNT", "IslandOrInternationUpload");
							siProgramLogAction.createProgramLog("Upload", "COUNT", "IslandOrInternationUpload", count+"/"+imMovementHeads.size(), loginEmployeeCode);
							System.out.println("COUNT END");
							//延遲時間
							Thread.sleep(Long.valueOf(buCommonPhraseLine.getAttribute2()));
							//若總開關開啟則放行
							if(buCommonPhraseLine.getAttribute1().equals("1")){
								
								
								
								
								List<CustomsConfiguration> DFconfigs =  getConfigs("NF13",config);
								dataList.add(im);
								returnMap = islandUploadService.dataBind(DFconfigs, "XML", dataList);
								
								
								
							}

						}catch(Exception e){
							e.printStackTrace();
							String exceptionMsg = e.getMessage();
							exceptions.add("[第 "+(count-1)+"筆] 單號:"+imMovementHeads.get(count-1).getOrderNo()+" 異常:"+exceptionMsg);
						}
						if(null != exceptions && exceptions.size()>0){
							for(String s:exceptions){
								//siProgramLogAction.createProgramLog("Upload", "ERROR", "IslandOrInternationUpload", s, loginEmployeeCode);
							}
							throw new Exception("檢誤結束，發現異常：");
						}
						dataList = null;
			    	}
				}*/

	 }
	 
	 
	 public List<CustomsConfiguration> getConfigs(String DF,List<CustomsConfiguration> configs){
			List<CustomsConfiguration> returnConfigs = new ArrayList();
			try{
				if(configs == null){
					throw new Exception("configs == null");
				}else{
					for(CustomsConfiguration CusConfig:configs){
						String dstFunction = null==CusConfig.getDestinationFunction()? "":CusConfig.getDestinationFunction();
						if(!dstFunction.equals("")&&dstFunction.equals(DF)){
							returnConfigs.add(CusConfig);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return returnConfigs;
		}
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception
	{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	 public List<Properties> updateStatusTransaction(Properties httpRequest) throws Exception{

		List list = new ArrayList();
		String errorMsg = null;
		Date todayDate = new Date();
		String schedual = httpRequest.getProperty("schedual");
		String schedualDate = httpRequest.getProperty("schedualDate");
		String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
		String flag = httpRequest.getProperty("flag");
		String orderTypeCode = "SOP";
		String[] fileName = {"schedual","schedualDate","orderTypeCode"};
		Object[] fileValue = {schedual,(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, schedualDate)),orderTypeCode};
	    List<UploadControl> uploadControls = uploadControlDAO.findByProperty("UploadControl", fileName, fileValue);
	    log.info("uploadControls.size()="+uploadControls.size());

	    if(uploadControls.size() == 0)
	    {
	    	log.info("無資料");
	    	errorMsg="此班次無資料";
	    }
	    else
	    {
	    	UploadControl uploadControl = uploadControls.get(0);
	    	String statusAC = uploadControl.getStatusByAC();
	    	String statusED = uploadControl.getStatusByED();
	    	String statusBC = uploadControl.getStatusByBC();
	    	
	    	
	    	log.info("有資料");
	    	uploadControl = uploadControls.get(0);
	    	if(statusBC.equals("OFF"))
	    	{
		    	if(flag.equals("1"))
		    	{
		    		if(statusAC.equals("ON"));
		    		{
			    		uploadControl.setStatusByAC("OFF");
			    		uploadControl.setLastUpdatedByAC(loginEmployeeCode);
				    	uploadControl.setLastACUpdateDate(todayDate);
		    		}
		    	}
		    	else if(flag.equals("2"))
		    	{
		    		if(statusED.equals("ON"));
		    		{
			    		uploadControl.setStatusByED("OFF");
			    		uploadControl.setLastUpdatedByED(loginEmployeeCode);
				    	uploadControl.setLastEDUpdateDate(todayDate);
		    		}
		    	}
		    	uploadControlDAO.update(uploadControl);
	    	}
	    	else
	    	{
	    		log.info("此班銷售資料已上傳，無法執行反確認");
	    	}
	    }
		
		return list;
	}
	 
	 
	 
	 
	 
	 public List<Properties> updateDeliverySwitch(Properties httpRequest) throws Exception{

			List list = new ArrayList();
			String errorMsg = null;
			Date todayDate = new Date();
			String deliverySwitch = httpRequest.getProperty("deliverySwitch");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

	    	BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("UploadControl","updateDelivery");
    		buCommonPhraseLine.setEnable(deliverySwitch);
	    	buCommonPhraseLine.setLastUpdatedBy(loginEmployeeCode);
    		buCommonPhraseLine.setLastUpdateDate(todayDate);
    		buCommonPhraseLineDAO.update(buCommonPhraseLine);
		
			return list;
		}
	 /**取得申請書號碼入口**/
	 public List<Properties> updateApplicationNo(Properties httpRequest) throws Exception{
		 	
			List list = new ArrayList();
			String errorMsg = null;
			Date todayDate = new Date();
			String model = httpRequest.getProperty("model");
			Long headId = NumberUtils.getLong((String)httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");

			if(model.equals("CM"))
			{	CmMovementHead cmMovementPO = (CmMovementHead)cmMovementHeadDAO.findById("CmMovementHead", headId);
				cmMovementService.saveRpNo(cmMovementPO,loginEmployeeCode);
			}
			else if(model.equals("ADJ"))
			{
				ImAdjustmentHead imGeneralAdjustmentHead = imAdjustmentHeadDAO.findById(headId);
				imGeneralAdjustmentService.saveResNo(imGeneralAdjustmentHead);
			}
			else if(model.equals("IM"))
			{
				ImMovementHead imMovement = imMovementHeadDAO.findById(headId);
				imMovementMainService.saveRpNo(imMovement,loginEmployeeCode);
			}
			else
			{
				log.info("error");
			}

		
			return list;
		}		 
}