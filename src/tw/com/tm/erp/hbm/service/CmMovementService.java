package tw.com.tm.erp.hbm.service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.ImMovementMainAction;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuTruck;
import tw.com.tm.erp.hbm.bean.BuTruckMod;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CmMovementLine;
import tw.com.tm.erp.hbm.bean.CmTransfer;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImTransation;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.BuTruckDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.CmMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.CmMovementLineDAO;
import tw.com.tm.erp.hbm.dao.CmTransferDAO;
import tw.com.tm.erp.hbm.dao.CustomsProcessResponseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;
//for 儲位用
import tw.com.tm.erp.action.ImStorageAction;

public class CmMovementService {

    private static final Log log = LogFactory.getLog(CmMovementService.class);
    public static final String PROGRAM_ID= "CM_MOVEMENT";

    /**
     * spring Ioc
     */ 
    private BaseDAO baseDAO;
    private BuBrandDAO buBrandDAO;
    private ImMovementHeadDAO imMovementHeadDAO;
    private CmMovementHeadDAO cmMovementHeadDAO;
    private CmMovementLineDAO cmMovementLineDAO;
    private BuOrderTypeDAO buOrderTypeDAO;
    private ImWarehouseDAO imWarehouseDAO;
    private ImOnHandDAO imOnHandDAO;
    private ImItemDAO imItemDAO;
    private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    private BuOrderTypeService buOrderTypeService;
    private ReverterService reverterService;
    private ImMovementMainService imMovementMainService;
    private SiProgramLogAction siProgramLogAction;
    private ImMovementMainAction imMovementMainAction;
    private CmTransferDAO cmTransferDAO;
    private BuTruckDAO buTruckDAO;
    private BuTruck buTruck;
    private BuTruckMod buTruckMod;
    //for  儲位用
    private ImStorageAction				 imStorageAction;
    private ImStorageService			 imStorageService;
    
    public void setBuTruckDAO(BuTruckDAO buTruckDAO){
    	this.buTruckDAO = buTruckDAO;
    }
    public void setBuTruck(BuTruck buTruck){
    	this.buTruck = buTruck;
    }    
    public void setBuTruckMod(BuTruckMod buTruckMod){
    	this.buTruckMod = buTruckMod;
    }
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
    	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }
    public void setCmTransferDAO(CmTransferDAO cmTransferDAO) {
    	this.cmTransferDAO = cmTransferDAO;
    }
    public void setImMovementMainService(ImMovementMainService imMovementMainService) {
    	this.imMovementMainService = imMovementMainService;
    }
    public void setReverterService(ReverterService reverterService) {
    	this.reverterService = reverterService;
    }
    public void setImMovementMainAction(ImMovementMainAction imMovementMainAction) {
    	this.imMovementMainAction = imMovementMainAction;
    }
    public void setBaseDAO(BaseDAO baseDAO) {
    	this.baseDAO = baseDAO;
    }
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
    	this.buBrandDAO = buBrandDAO;
    }
    public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
    	this.imMovementHeadDAO = imMovementHeadDAO;
    }
    public void setCmMovementHeadDAO(CmMovementHeadDAO cmMovementHeadDAO) {
    	this.cmMovementHeadDAO = cmMovementHeadDAO;
    }
    public void setCmMovementLineDAO(CmMovementLineDAO cmMovementLineDAO) {
    	this.cmMovementLineDAO = cmMovementLineDAO;
    }
    public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
    	this.buOrderTypeDAO = buOrderTypeDAO;
    }
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
    	this.imWarehouseDAO = imWarehouseDAO;
    }
    public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
    	this.imOnHandDAO = imOnHandDAO;
    }
    public void setCmDeclarationOnHandDAO(
    		CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
    	this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
    }
    public void setBuEmployeeWithAddressViewDAO(
    		BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
    	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
    	this.buOrderTypeService = buOrderTypeService;
    }

    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }

    public void setImStorageAction(ImStorageAction imStorageAction) {
    	this.imStorageAction = imStorageAction;
    }

    public void setImStorageService(ImStorageService imStorageService) {
    	this.imStorageService = imStorageService;
    }

    /**
     * 移倉單明細欄位
     */
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "orderTypeCode", "orderNo", "reserve5", "statusName",
			"deliveryWarehouseName", "arrivalWarehouseName", "imCreatedByName", "imCreationDate", "imLastUpdatedByName",
			"imLastUpdateDate", "lineId", "isLockRecord", "isDeleteRecord"// ,
																			// "message"
	};

    public static final String[] GRID_FIELD_DEFAULT_VALUES = {
	"", "", "",
	"", "", "",
	"", "", "",
	"", "",
	"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE//, ""
    };

    public static final int[] GRID_FIELD_TYPES = {
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE,
	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING//, AjaxUtils.FIELD_TYPE_STRING
    };

	/**
	 * 移倉單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderNo", "deliveryDate", "passNo", "customsArea",
			"transferOrderNo", "statusName", "customsStatusName","moveWhNo", "lastUpdateDate", "orderTypeCode", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "","", "", "" };

    private List addErrorMsgs(String message, List errorMsgs, String programId, String identification, String lastUpdatedBy ){
	siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, lastUpdatedBy);
	errorMsgs.add(message);
	log.error(message);
	return errorMsgs;
    }

    /**
     * 1.將相同的商品、庫別、批號集合
     * 2.將相同的報單號碼、報單項次、商品、關別集合
     *
     * @param salesOrderHead
     * @param isServiceItemMap
     * @return Set[]
     */
    private Set[] aggregateOrderItemsQty(CmMovementHead cmMovementHead, ImMovementHead imMovementHead, String employeeCode)
    throws FormException, Exception{

	StringBuffer key = new StringBuffer();
	StringBuffer cmKey = new StringBuffer();
	HashMap imMap = new HashMap();
	HashMap cmMap = new HashMap();

	String brandCode = cmMovementHead.getBrandCode();
	String taxType = cmMovementHead.getTaxType();

	List<ImMovementItem> imMovementItems = imMovementHead.getImMovementItems();
	log.info( "imMovementItems.size() = " + imMovementItems.size() );
	for (ImMovementItem imMovementItem : imMovementItems) {
	    imMovementItem.setLastUpdateDate( new Date() );
	    imMovementItem.setLastUpdatedBy( employeeCode );

	    Double quantity = imMovementItem.getArrivalQuantity();
	    String arrivalWarehouseCode = imMovementItem.getArrivalWarehouseCode();
	    String declarationNo = imMovementItem.getOriginalDeclarationNo();
	    Long declarationSeq = imMovementItem.getOriginalDeclarationSeq();
	    String customsItemCode = imMovementItem.getItemCode();
	    String lotNo = imMovementItem.getLotNo();

	    //數量不可為null
	    if(quantity == null){
		quantity = 0D;
		imMovementItem.setArrivalQuantity(quantity);
	    }

	    log.info( "imMovementItem.quantity = " + quantity );
	    log.info( "imMovementItem.getOriginalDeclarationNo = " + declarationNo );
	    log.info( "imMovementItem.getOriginalDeclarationSeq = " + declarationSeq );
	    log.info( "imMovementItem.getItemCode = " + customsItemCode );
	    log.info( "imMovementItem.getArrivalWarehouseCode = " + arrivalWarehouseCode );

	    // 保稅商品集合
	    if("F".equals(taxType)){
		ImWarehouse imWarehouse = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, arrivalWarehouseCode, null);
		String customsWarehouseCode = null;
		if( imWarehouse != null ){
		    // 入庫倉屬於的關 cmMoveWarehouseTo
		    customsWarehouseCode = imWarehouse.getCustomsWarehouseCode();
		    if(!StringUtils.hasText(arrivalWarehouseCode)){
			throw new ValidationErrorException("庫別(" + arrivalWarehouseCode + ")的海關關別未設定！");
		    }
		}else{
		    throw new NoSuchObjectException("依據品牌(" + brandCode + ")、庫別(" + arrivalWarehouseCode + ")查無庫別相關資料！");
		}

		cmKey.delete(0, cmKey.length());
		cmKey.append(declarationNo).append("{$}");
		cmKey.append(declarationSeq).append("{$}");
		cmKey.append(customsItemCode).append("{$}");
		cmKey.append(customsWarehouseCode);
		if (cmMap.get(cmKey.toString()) == null) {
		    cmMap.put(cmKey.toString(), quantity);
		} else {
		    cmMap.put(cmKey.toString(), quantity + ((Double) cmMap.get(cmKey.toString())));
		}
	    }

	    key.delete(0, key.length());
	    key.append(customsItemCode).append("{$}");
	    key.append(arrivalWarehouseCode).append("{$}");
	    key.append(lotNo);
	    if (imMap.get(key.toString()) == null) {
		imMap.put(key.toString(), quantity);
	    } else {
		imMap.put(key.toString(), quantity + ((Double) imMap.get(key.toString())));
	    }

	}

	return new Set[]{imMap.entrySet(), cmMap.entrySet()};
    }

    /**
     * 檢核移倉單主檔,明細檔
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List checkedCmMovement(Map parameterMap)throws FormException, Exception{
	List errorMsgs = new ArrayList(0);
	String message = null;
	String identification = null;
	CmMovementHead cmMovementHead = null;
	try{

	    Object formLinkBean = parameterMap.get("vatBeanFormLink");

	    cmMovementHead = this.getActualCmMovement(formLinkBean);

	    String status = cmMovementHead.getStatus();
	    if (OrderStatus.SAVE.equals(status)) {

		identification = MessageStatus.getIdentification(cmMovementHead.getBrandCode(),
			cmMovementHead.getOrderTypeCode(), cmMovementHead.getOrderNo());

		siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

		validateCmMovement( cmMovementHead, PROGRAM_ID, identification, errorMsgs, formLinkBean );

	    }

	}catch (Exception ex) {
	    message = "移倉單檢核失敗，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, message, cmMovementHead.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	return errorMsgs;
    }

    /**
     * 完成任務工作
     * @param assignmentId
     * @param approveResult
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{

	try{
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);

	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成移倉工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成移倉工作任務失敗！"+ ex.getMessage());
	}
    }

	/**
	 * 移倉單初始化 bean 額外顯示欄位
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");

			CmMovementHead cmMovementHead = this.getActualHead(otherBean, resultMap);

			// 顯示正確的建單人
			String employeeName = "";
			if(cmMovementHead.getCreatedBy()==null && !"".equals(cmMovementHead.getCreatedBy()))
				employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
			else
				employeeName = UserUtils.getUsernameByEmployeeCode(cmMovementHead.getCreatedBy());

			Map multiList = new HashMap(0);
			
			BuCommonPhraseLine HD = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("CmMovement","HD");
			BuCommonPhraseLine FD = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("CmMovement","FD");
			BuCommonPhraseLine Other = buCommonPhraseLineDAO.findOneBuCommonPhraseLine("CmMovement","Other");
			if (cmMovementHead.getApplicantCode() == null || "".equals(cmMovementHead.getApplicantCode()))
			{	
				if (cmMovementHead.getDeliveryCustomsWarehouse().equals("HD")){				
					cmMovementHead.setApplicantCode(HD.getAttribute2());
					cmMovementHead.setDeliverymanCode(HD.getAttribute3());
				}else if(cmMovementHead.getDeliveryCustomsWarehouse().equals("FD")){
					cmMovementHead.setApplicantCode(FD.getAttribute2());
					cmMovementHead.setDeliverymanCode(FD.getAttribute3());
				}else{
					cmMovementHead.setApplicantCode(Other.getAttribute2());
					cmMovementHead.setDeliverymanCode(Other.getAttribute3());				
				}
			}else{
				cmMovementHead.setApplicantCode(cmMovementHead.getApplicantCode());
				cmMovementHead.setDeliverymanCode(cmMovementHead.getDeliverymanCode());
			}
			
			// 如果是空值，帶預設值
			/*
			cmMovementHead.setApplicantCode(cmMovementHead.getApplicantCode() == null
					|| "".equals(cmMovementHead.getApplicantCode()) ? "T73141" : cmMovementHead.getApplicantCode());
			cmMovementHead.setDeliverymanCode(cmMovementHead.getDeliverymanCode() == null
					|| "".equals(cmMovementHead.getDeliverymanCode()) ? "T53859" : cmMovementHead.getDeliverymanCode());
			*/
			
			// 如果是空值，帶預設值
			/*
			cmMovementHead.setApplicantCode(cmMovementHead.getApplicantCode() == null
					|| "".equals(cmMovementHead.getApplicantCode()) ? "T73141" : cmMovementHead.getApplicantCode());
			cmMovementHead.setDeliverymanCode(cmMovementHead.getDeliverymanCode() == null
					|| "".equals(cmMovementHead.getDeliverymanCode()) ? "T53859" : cmMovementHead.getDeliverymanCode());
			*/
			
			// 保卡號碼預設值
			if (cmMovementHead.getOrderTypeCode().equals("RWD")) 
				cmMovementHead.setCarNo(cmMovementHead.getCarNo() == null || "".equals(cmMovementHead.getCarNo()) ? "保車號碼"
						: cmMovementHead.getCarNo());
			else if (cmMovementHead.getOrderTypeCode().equals("RMK"))
				cmMovementHead.setCarNo(cmMovementHead.getCarNo() == null || "".equals(cmMovementHead.getCarNo()) ? "保箱號碼"
						: cmMovementHead.getCarNo());

			cmMovementHead.setExpenseNo(cmMovementHead.getExpenseNo() == null || "".equals(cmMovementHead.getExpenseNo()) ? "N/A"
							: cmMovementHead.getExpenseNo());

			resultMap.put("form", cmMovementHead);
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			resultMap.put("statusName", OrderStatus.getChineseWord(cmMovementHead.getStatus()));
			resultMap.put("createByName", employeeName);
			// resultMap.putAll( this.getOtherColumn(cmMovementHead) );

			List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode, "CM");

			List<BuOrderType> allBuOrderTypes = baseDAO.findByProperty("BuOrderType",
					"and id.brandCode = ? and typeCode = ? and cmMoveWarehouseFrom like ? and cmMoveWarehouseTo like ? ",
					new Object[] { cmMovementHead.getBrandCode(), "IM", "%" + cmMovementHead.getDeliveryCustomsWarehouse() + "%",
							"%" + cmMovementHead.getArrivalCustomsWarehouse() + "%" });

			List<BuCommonPhraseLine> allTaxTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "TaxCatalog", "Y" }, "indexNo");
			List<BuCommonPhraseLine> allCustomsAreas = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "CustomsArea", "Y" }, "indexNo");
			List<BuCommonPhraseLine> allSealTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "SealType", "Y" }, "indexNo");
			List<BuCommonPhraseLine> allCarTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "CarType", "Y" }, "indexNo");
			List<BuCommonPhraseLine> allExpenseTypes = baseDAO.findByProperty("BuCommonPhraseLine", new String[] {
					"id.buCommonPhraseHead.headCode", "enable" }, new Object[] { "ExpenseType", "Y" }, "indexNo");
			//List<BuTruck> buTrucks = baseDAO.findByProperty("BuTruck", new String[] {"enable" }, new Object[] { "Y" }, "truckCode");//2016 by jason 保卡
			// List<BuCommonPhraseLine> allCarNos =
			// baseDAO.findByProperty("BuCommonPhraseLine", new
			// String[]{"id.buCommonPhraseHead.headCode", "enable"}, new
			// Object[]{"CarNo", "Y"}, "indexNo" ); // 保卡號碼
			// log.info("allCarNos = " + allCarNos.size());
			String taxType = cmMovementHead.getTaxType();
			// String customsArea = cmMovementHead.getCustomsArea(); // 關別
			// String sealType = cmMovementHead.getSealType(); // 封條
			// String carType = cmMovementHead.getCarType(); // 保卡類別
			// String expenseType = cmMovementHead.getExpenseType(); // 規費類別

			String defaultCustomsArea = "";
			String defaultSealType = "";
			String defaultExpenseType = "";
			if ("F".equals(taxType)) {
				defaultCustomsArea = "北";
				defaultSealType = "T";
				defaultExpenseType = "A";
			}
			String defaultCarType = "";
			if (cmMovementHead.getOrderTypeCode().equals("RMK") || cmMovementHead.getOrderTypeCode().equals("RMW")) {
				defaultCustomsArea = "高";
				defaultCarType = "B";
			} else {
				defaultCarType = "C";
			}

			multiList.put("allBuOrderTypes", AjaxUtils.produceSelectorData(allBuOrderTypes, "orderTypeCode", "name", true,
					false));
			multiList.put("allOrderTypeCodes", AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name",
					true, false));

			multiList.put("allTaxTypes", AjaxUtils.produceSelectorData(allTaxTypes, "lineCode", "name", false, true,
					taxType != null ? taxType : ""));
			multiList.put("allCustomsAreas", AjaxUtils.produceSelectorData(allCustomsAreas, "lineCode", "name", false, true,
					defaultCustomsArea));
			multiList.put("allSealTypes", AjaxUtils.produceSelectorData(allSealTypes, "lineCode", "name", false, true,
					defaultSealType));
			multiList.put("allCarTypes", AjaxUtils.produceSelectorData(allCarTypes, "lineCode", "name", false, true,
					defaultCarType));
			multiList.put("allExpenseTypes", AjaxUtils.produceSelectorData(allExpenseTypes, "lineCode", "name", false, true,
					defaultExpenseType));
			// multiList.put("allCarNos", AjaxUtils.produceSelectorData(allCarNos, "lineCode", "name", false, true ));
			//保卡資料
			//multiList.put("allCarNos", AjaxUtils.produceSelectorData(buTrucks, "truckCode", "truckCode", false, true));//20160414 by jason 	保卡

			// 運送單資料
			if (cmMovementHead.getTransferOrderNo() != null) {
				CmTransfer cmTransfer = cmTransferDAO.findById(cmMovementHead.getTransferOrderNo());
				if (cmTransfer != null) {
					// multiList.put("transferOrderNo",
					// cmTransfer.getTransferOrderNo());
					multiList.put("transfer", cmTransfer.getTransfer());
					multiList.put("owner", cmTransfer.getOwner());
					multiList.put("startStation", cmTransfer.getStartStation());
					multiList.put("toStation", cmTransfer.getToStation());
					multiList.put("vehicleStation", cmTransfer.getVehicleStation());
					multiList.put("vehicleNo", cmTransfer.getVehicleNo());
					multiList.put("driverLicence", cmTransfer.getDriverLicence());
					multiList.put("track", cmTransfer.getTrack());
					multiList.put("airplaneNo", cmTransfer.getAirplaneNo());
					multiList.put("masterNo", cmTransfer.getMasterNo());
					multiList.put("secondNo", cmTransfer.getSecondNo());
					multiList.put("clearance", cmTransfer.getClearance());
					multiList.put("release", cmTransfer.getRelease());

					Date leaveTime = cmTransfer.getLeaveTime();
					Date arriveTime = cmTransfer.getArriveTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
					String leaveTimeT = leaveTime == null || leaveTime.equals("") ? "" : (Integer.parseInt(sdf.format(
							leaveTime).substring(0, 4)) - 1911)
							+ sdf.format(leaveTime).substring(4);
					String arriveTimeT = arriveTime == null || arriveTime.equals("") ? "" : (Integer.parseInt(sdf.format(
							arriveTime).substring(0, 4)) - 1911)
							+ sdf.format(arriveTime).substring(4);
					multiList.put("leaveTimeT", leaveTimeT);
					multiList.put("arriveTimeT", arriveTimeT);
				}
			}
			resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("移倉單初始化失敗，原因：" + ex.toString());
			throw new Exception("移倉單初始化失敗，原因：" + ex.toString());

		}
	}

    /**
     * 移倉單查詢初始化
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeSearchInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);
	Map multiList = new HashMap(0);
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    resultMap.put("orderTypeCode", orderTypeCode);

	    List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType(loginBrandCode ,"CM");
	    multiList.put("allOrderTypeCodes"	, AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name", true, true ));

	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("移倉單查詢初始化失敗，原因：" + ex.toString());
	    throw new Exception("移倉單查詢初始化失敗，原因：" + ex.toString());

	}
    }

    /**
     * 產生新的移倉單主檔
     * @param otherBean
     * @return
     * @throws Exception
     */
    private CmMovementHead executeNew(Object otherBean)throws Exception{
	CmMovementHead head = new CmMovementHead();
	try {
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");

	    head.setOrderTypeCode(orderTypeCode);
	    head.setBrandCode(loginBrandCode);
	    head.setStatus( OrderStatus.SAVE );
	    head.setDeliveryDate( DateUtils.parseDate( DateUtils.C_DATE_PATTON_SLASH, DateUtils.getCurrentDateStr(DateUtils.C_DATE_PATTON_SLASH)) );
	    head.setPassNo("");
	    head.setCustomsArea("");

	    setOtherColumn(head);

	    head.setSealType("");
	    head.setSealNo("");
	    head.setCarType("");
	    head.setCarNo("");
	    head.setExpenseType("");
	    head.setExpenseNo("");

	    head.setDriverCode("");
	    head.setApplicantCode("");
	    head.setDeliverymanCode("");
	    head.setRemark1("");

	    head.setCreatedBy(loginEmployeeCode);
	    head.setCreationDate(new Date());
	    head.setLastUpdatedBy(loginEmployeeCode);
	    head.setLastUpdateDate(new Date());
	    
	    this.saveTmpHead(head);

	} catch (Exception e) {
	    log.error("建立新移倉單主檔失敗,原因:"+e.toString());
	    throw new Exception("建立新移倉單主檔失敗,原因:"+e.toString());
	}
	return head;
    }

    /**
     * 依據headId為查詢條件，取得移倉單主檔
     *
     * @param headId
     * @return CmMovementHead
     * @throws Exception
     */
    public CmMovementHead executeFind(Long headId) throws Exception {

	try {
	    CmMovementHead head = (CmMovementHead) baseDAO.findByPrimaryKey(CmMovementHead.class,
		    headId);
	    return head;
	} catch (Exception ex) {
	    log.error("依據主鍵：" + headId + "查詢移倉單主檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據主鍵：" + headId + "查詢移倉單主檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 反轉
     * @param headId
     */
    public void executeReverse(Long headId) {
	try {
	    CmMovementHead head = executeFind(headId);
	    List<String> errorMsgs = new ArrayList();
	    String orderTypeCode = head.getOrderTypeCode(); // 單別
	    String taxType = head.getTaxType(); // 稅別
	    List<CmMovementLine> lines = head.getCmMovementLines();
	    log.info( "CmMovementLines.size() = " + lines.size());
	    for (CmMovementLine cmMovementLine : lines) {
		ImMovementHead imMovementHead = this.findOneImMovement(cmMovementLine.getOrderNo(), head.getBrandCode(), cmMovementLine.getOrderTypeCode(), head.getTaxType(), false);
		log.info( "imMovementHead = " + imMovementHead );
		if( imMovementHead != null ){
		    if(OrderStatus.FINISH.equals(head.getStatus())){
			updateImMovement(cmMovementLine, head, imMovementHead, "T93014", errorMsgs);
		    }else  if(OrderStatus.SIGNING.equals(head.getStatus())){
			imMovementHead.setCmMovementNo(null);
		    }
		}else{
		    throw new FormException("移倉單狀態:" + head.getStatus() + "品牌:" + head.getBrandCode() + "單別" + cmMovementLine.getOrderTypeCode() + "調撥單號:" + cmMovementLine.getOrderNo() + "稅別:"+ head.getTaxType() + "資料異常， 無法查出調撥單資訊");
		}
	    }
	    head.setStatus(OrderStatus.SAVE);
	    head.setLastUpdateDate(new Date());

	} catch (Exception e) {
	    log.error("反轉移倉單時發生錯誤，原因：" + e.getMessage());
	}
    }

    /**
     * 自動起調撥單
     * @param cmMovementHead
     */
    public void executeAutoCreateImMovement(CmMovementHead cmMovementHead) throws Exception{
	if(OrderStatus.FINISH.equals(cmMovementHead.getStatus())){
	    List<CmMovementLine> cmMovementLines = cmMovementHead.getCmMovementLines();
	    for (CmMovementLine cmMovementLine : cmMovementLines) {
		imMovementMainAction.autoCreateImMovement(cmMovementHead.getBrandCode(), cmMovementLine.getOrderTypeCode(),cmMovementLine.getOrderNo(), cmMovementHead.getDeliveryDate());
	    }
	}
    }


    /**
     * 依移倉單明細的orderNo, 移倉主檔的taxType
     *
     * @param cmMovementLineOrderNo
     * @param cmMovementHeadTaxType
     * @return
     */
    private ImMovementHead findOneImMovement(String cmMovementLineOrderNo, String brandCode, String orderTypeCode, String cmMovementHeadTaxType){
	return findOneImMovement(cmMovementLineOrderNo, brandCode, orderTypeCode, cmMovementHeadTaxType, true);

    }
    /**
     * 依移倉單明細的orderNo, 移倉主檔的taxType
     * @param cmMovementLineOrderNo
     * @param cmMovementHeadTaxType
     * @return
     */
    private ImMovementHead findOneImMovement(String cmMovementLineOrderNo, String brandCode, String orderTypeCode, String cmMovementHeadTaxType, boolean isNull){
	String conditiion = null;
	if(isNull){
	    conditiion = "cmMovementNo IS NULL";

	    return (ImMovementHead)imMovementHeadDAO.findFirstByProperty(
		    "ImMovementHead","and orderNo = ? and brandCode = ? and orderTypeCode = ? and taxType = ? and " + conditiion + " and status = ?",
		    new Object[] { cmMovementLineOrderNo, brandCode, orderTypeCode, cmMovementHeadTaxType, OrderStatus.WAIT_IN });
	}else{
	    conditiion = "cmMovementNo IS NOT NULL";
	    return (ImMovementHead)imMovementHeadDAO.findFirstByProperty(
		    "ImMovementHead","and orderNo = ? and brandCode = ? and orderTypeCode = ? and taxType = ? and " + conditiion,
		    new Object[] { cmMovementLineOrderNo, brandCode, orderTypeCode, cmMovementHeadTaxType });
	}


    }

    /**
     * 透過headId取得移倉單
     * @param bean
     * @return
     * @throws FormException
     * @throws Exception
     */
    public CmMovementHead getActualCmMovement(Object otherBean ) throws FormException, Exception{

	CmMovementHead cmMovementHead = null;
	String id = (String)PropertyUtils.getProperty(otherBean, "headId");

	if(StringUtils.hasText(id)){
	    Long headId = NumberUtils.getLong(id);
	    cmMovementHead = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
	    if(cmMovementHead  == null){
		throw new NoSuchObjectException("查無移倉單主鍵：" + headId + "的資料！");
	    }
	}else{
	    throw new ValidationErrorException("傳入的移倉單主鍵為空值！");
	}
	return cmMovementHead;

    }
    
    public void saveRpNo(CmMovementHead cmMovementPO, String loginUser) throws NoSuchObjectException{
		System.out.println("取移倉申請書號碼開始");
//MACO 2016.01.30 superviseCode
		String superviseCodeT2=buCommonPhraseLineDAO.findById("SuperviseCode","T2Warehouse").getName();
		String superviseCodeT2Bak=buCommonPhraseLineDAO.findById("SuperviseCode","T2WarehouseBak").getName();
		String superviseCodeMS=buCommonPhraseLineDAO.findById("SuperviseCode","MSWarehouse").getName();
		String superviseCodeHDBak=buCommonPhraseLineDAO.findById("SuperviseCode","HDWarehouseBak").getName();
		String superviseCodeVDBak=buCommonPhraseLineDAO.findById("SuperviseCode","VDWarehouseBak").getName();

		Long seq = Long.valueOf(cmMovementHeadDAO.getWhNoSq().get(0).toString());
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		int year=cl.get(Calendar.YEAR)-1911;
		
		String RpNo = null;
		try{
			if(cmMovementPO.getOrderTypeCode().equals("RMK") || cmMovementPO.getOrderTypeCode().equals("RMM") || cmMovementPO.getOrderTypeCode().equals("RMW") || cmMovementPO.getOrderTypeCode().equals("RAP") || cmMovementPO.getOrderTypeCode().equals("RPA")){
				RpNo = "R"+ superviseCodeMS + String.valueOf(year).substring(1) + String.format("%06d",seq) ;	
			}else if(cmMovementPO.getOrderTypeCode().equals("RWD")||cmMovementPO.getOrderTypeCode().equals("RWK")||cmMovementPO.getOrderTypeCode().equals("RMV")){
				log.info("保倉移倉至T2");
				RpNo = "R" + superviseCodeT2 + String.valueOf(year).substring(1) + String.format("%06d",seq) ;	
			}else if(cmMovementPO.getOrderTypeCode().equals("RDW")){
				log.info("T2退倉至保倉");
				RpNo = "R" + superviseCodeT2Bak + String.valueOf(year).substring(1) + String.format("%06d",seq) ;	
			}else if(cmMovementPO.getOrderTypeCode().equals("RKW")){
				log.info("高雄退倉至保倉");
				RpNo = "R" + superviseCodeHDBak + String.valueOf(year).substring(1) + String.format("%06d",seq) ;	
			}else if(cmMovementPO.getOrderTypeCode().equals("RVM")){
				log.info("VD退倉至保倉");
				RpNo = "R" + superviseCodeVDBak + String.valueOf(year).substring(1) + String.format("%06d",seq) ;	
			}
			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLL:"+RpNo);
			cmMovementPO.setMoveWhNo(RpNo);
			cmMovementPO.setLastUpdatedBy(loginUser);//更新異動人員
			cmMovementPO.setLastUpdateDate(new Date());//更新異動日期
			System.out.println("取移倉申請書號碼結束");
			System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員開始");
		
			cmMovementHeadDAO.merge(cmMovementPO);//更新成正式單號
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("更新移倉申請書號碼開始:更新單號以及最後更新日期及人員結束");
	}
    
    public void updateCustomsStatus(Properties httpRequest) throws Exception {
    	try{
    	//Object otherBean = parameterMap.get("vatBeanOther");
    	
	    Long headId1 = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    //Long headId =  StringUtils.hasText(headId1)? Long.valueOf(headId1):null;
	    log.info("headId=="+headId1);
	    String tranRecordStatus = httpRequest.getProperty("status");
	    
	    
	    String DF = null;
	    Date now = new Date();
	    String actualDataDate = DateUtils.format(new Date(now.getTime()+3*60*1000), DateUtils.C_TIME_PATTON_DEFAULT);
	    
	    CmMovementHead cm = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId1);
	    log.info("cm申請書編號1："+cm.getMoveWhNo());
	    if(cm.getOrderTypeCode().equals("RMK")||cm.getOrderTypeCode().equals("RMM")||cm.getOrderTypeCode().equals("RMW")||cm.getOrderTypeCode().equals("RAP")||cm.getOrderTypeCode().equals("RPA")){
	    	if(tranRecordStatus.equals("NF14O")){
	    		tranRecordStatus = "DF15O";
	    	}else if(tranRecordStatus.equals("NF14I")){
	    		tranRecordStatus = "DF15I";
	    	}
	    }
	    log.info("tranRecordStatus"+tranRecordStatus);
	    /*if (cm.getMoveWhNo()==null){
			Long seq = Long.valueOf(cmMovementHeadDAO.getWhNoSq().get(0).toString());
			Calendar cl = Calendar.getInstance();
			cl.setTimeInMillis(System.currentTimeMillis());
			int year=cl.get(Calendar.YEAR)-1911;
			String RpNo = "RCG084" + String.valueOf(year).substring(1) + String.format("%06d",seq) ;
			cm.setMoveWhNo(RpNo);
	    }*/
	    if(tranRecordStatus.equals("cancel")){
	      String orderTypeCode = cm.getOrderTypeCode();
	      if(orderTypeCode.equals("RWD")||orderTypeCode.equals("RDW")||orderTypeCode.equals("RWK")||orderTypeCode.equals("RKW")||orderTypeCode.equals("RMV")){
	    	  if(cm.getCustomsStatus().equals("N13")||cm.getCustomsStatus().equals("N23")||cm.getCustomsStatus().equals("E80")){
	    		  cm.setTranAllowUpload("D");
	  	    	  cm.setCustomsStatus("A");
	  	    	  cmMovementHeadDAO.update(cm);//更新上傳
	    	  }else{
	    		  throw new Exception("此單海關尚未回應,目前不可註銷!!");
	    	  }
	      }else{
	    	    String recordStatus = cm.getTranRecordStatus();
		    	if(!recordStatus.equals("DF10")){
		    		cm.setTranRecordStatus("DF15");
		    	}
		    	System.out.println("cm.getCustomsStatus():"+cm.getCustomsStatus());
		    	if(cm.getCustomsStatus().equals("N24") || cm.getCustomsStatus().equals("N13")){
		    		cm.setTranRecordStatus("DF10");
		    	}
		    	cm.setTranAllowUpload("D");
		    	cm.setCustomsStatus("A");
		    	cmMovementHeadDAO.update(cm);//更新上傳
	      }
	    }else{
	    	log.info("saveStatus");
		    cm.setCustomsStatus("A");
		    cm.setTranAllowUpload("I");
		    if(tranRecordStatus.equals("NF14O")){
		    	 log.info("點驗出倉");
 		    	 cm.setTranRecordStatus("NF14");
 		    	 cm.setcStatus("O");
 		    	 cm.setCustomsOutTime(actualDataDate);
		    }else if(tranRecordStatus.equals("NF14I")){
		    	log.info("點驗進倉");
		    	cm.setTranRecordStatus("NF14");
		    	cm.setcStatus("I");
		    	cm.setCustomsInTime(actualDataDate);
		    }else if(tranRecordStatus.equals("DF15O")){
		    	log.info("MS點驗出倉");
		    	cm.setTranRecordStatus("DF15");
		    	cm.setcStatus("O");
		    	cm.setCustomsOutTime(actualDataDate);
		    }else if(tranRecordStatus.equals("DF15I")){
		    	log.info("點驗進倉");
		    	cm.setTranRecordStatus("DF15");
		    	cm.setcStatus("I");
		    	cm.setCustomsInTime(actualDataDate);
		    }
		    cmMovementHeadDAO.update(cm);//更新上傳
		    DF = "NF14";
		    //callSoapJsp(headId1, DF, cm.getOrderTypeCode());		    
	    }
	    
	    log.info("updateFinish");
    	/*}catch(IllegalAccessException iae){
    	    System.out.println(iae.getMessage());
    	    iae.printStackTrace();
    	    throw new IllegalAccessException(iae.getMessage());
    	}catch(InvocationTargetException ite){
    	    System.out.println(ite.getMessage());
    	    ite.printStackTrace();
    	    throw new InvocationTargetException(ite, ite.getMessage());
    	}catch(NoSuchMethodException nse){
    	    System.out.println(nse.getMessage());
    	    nse.printStackTrace();
    	    throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());*/
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    }

	public CmTransfer getCmTransfer(Object otherBean) throws FormException, Exception {

		CmTransfer cmTransfer = null;
		String id = (String) PropertyUtils.getProperty(otherBean, "transferOrderNo");

		if (StringUtils.hasText(id)) {
			cmTransfer = (CmTransfer) cmTransferDAO.findByPrimaryKey(CmTransfer.class, id);
			if (cmTransfer == null) {
				throw new NoSuchObjectException("查無運送單：" + id + "的資料！");
			}
		} else {
			throw new ValidationErrorException("傳入的運送單主鍵為空值！");
		}
		return cmTransfer;

	}

    /**
     * 依formId取得實際移倉單 in initial
     * @param otherBean
     * @param resultMap
     * @return
     * @throws Exception
     */
    private CmMovementHead getActualHead(Object otherBean, Map resultMap) throws Exception{
	CmMovementHead cmMovementHead = null;
	try {
	    String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
	    Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;

	    cmMovementHead = null == formId ? this.executeNew(otherBean) : this.executeFind(formId);

	} catch (Exception e) {
	    log.error("取得實際移倉單主檔失敗,原因:"+e.toString());
	    throw new Exception("取得實際移倉單主檔失敗,原因:"+e.toString());
	}
	return cmMovementHead;
    }
    /**
     * 取得訊息提示用
     * @param headId
     * @return
     * @throws Exception
     */
    public String getIdentification(Long headId) throws Exception{

	String id = null;
	try{
	    CmMovementHead cmMovementHead = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
	    if(cmMovementHead != null){
		id = MessageStatus.getIdentification(cmMovementHead.getBrandCode(),
			cmMovementHead.getOrderTypeCode(), cmMovementHead.getOrderNo());
	    }else{
		throw new NoSuchDataException("移倉單主檔查無主鍵：" + headId + "的資料！");
	    }

	    return id;
	}catch(Exception ex){
	    log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 第一次載入移倉單明細時,取得分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
	try {
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();

	    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
	    String brandCode = httpRequest.getProperty("brandCode");
	    String status = httpRequest.getProperty("status");
	    String taxType = httpRequest.getProperty("taxType");

	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    HashMap findObjs = new HashMap();
	    findObjs.put("and model.cmMovementHead.headId = :headId", headId);

	    Map searchMap = baseDAO.search("CmMovementLine as model", findObjs, "order by indexNo", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
	    List<CmMovementLine> cmMovementLines = (List<CmMovementLine>)searchMap.get(BaseDAO.TABLE_LIST);

	    HashMap map = new HashMap();
	    map.put("headId", headId);

	    if (cmMovementLines != null && cmMovementLines.size() > 0) {

		cmMovementLines = this.setLinesOtherColumn(brandCode, status, taxType, cmMovementLines);
		// 取得第一筆的INDEX
		Long firstIndex = cmMovementLines.get(0).getIndexNo();
		// 取得最後一筆 INDEX cmMovementLines.get( cmMovementLines.size() - 1).getIndexNo();//
		Long maxIndex = (Long)baseDAO.search("CmMovementLine as model", "count(model.cmMovementHead.headId) as rowCount" ,findObjs, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,cmMovementLines, gridDatas, firstIndex, maxIndex));
	    } else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	} catch (Exception ex) {
	    log.error("載入頁面顯示的移倉單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的移倉單明細失敗！");
	}
    }

    /**
     * ajax 取得調撥單主檔其他欄位
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXImMovementData(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String deliveryCWC = null;
	String arrivalCWC = null;
	try {
	    String orderNo = httpRequest.getProperty("orderNo").trim().toUpperCase();
	    String brandCode = httpRequest.getProperty("brandCode");
	    String deliveryCustomsWarehouse = httpRequest.getProperty("deliveryCustomsWarehouse");
	    String arrivalCustomsWarehouse = httpRequest.getProperty("arrivalCustomsWarehouse");
	    String taxType = httpRequest.getProperty("taxType");
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");

	    log.info( "取得調撥單主檔其他欄位");
	    log.info( "orderNo = " + orderNo );
	    log.info( "brandCode = " + brandCode );
	    log.info( "deliveryCustomsWarehouse = " + deliveryCustomsWarehouse );
	    log.info( "arrivalCustomsWarehouse = " + arrivalCustomsWarehouse );
	    log.info( "taxType = " + taxType );
	    log.info( "orderTypeCode = " + orderTypeCode );
	    // 撈調撥單
	    ImMovementHead imMovementHead = this.findOneImMovement(orderNo, brandCode, orderTypeCode, taxType);

	    if(imMovementHead != null){
		ImWarehouse imWarehouse1 = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, imMovementHead.getDeliveryWarehouseCode(), null);
		ImWarehouse imWarehouse2 = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, imMovementHead.getArrivalWarehouseCode(), null);
		if( imWarehouse1 != null && imWarehouse2 != null  ){
		    deliveryCWC = imWarehouse1.getCustomsWarehouseCode();
		    arrivalCWC = imWarehouse2.getCustomsWarehouseCode();
		}

		if( deliveryCWC.equals(deliveryCustomsWarehouse) && arrivalCWC.equals(arrivalCustomsWarehouse) ){
		    // 塞回
		    properties.setProperty("orderNo", orderNo);
		    properties.setProperty("orderTypeCode", imMovementHead.getOrderTypeCode());
		    properties.setProperty("statusName", OrderStatus.getChineseWord(imMovementHead.getStatus()));
		    properties.setProperty("deliveryWarehouseName", imMovementHead.getDeliveryWarehouseCode() + "-" + imWarehouse1.getWarehouseName());
		    properties.setProperty("arrivalWarehouseName", imMovementHead.getArrivalWarehouseCode() + "-" + imWarehouse2.getWarehouseName());
		    properties.setProperty("imCreatedByName", UserUtils.getUsernameByEmployeeCode(imMovementHead.getCreatedBy()) );
		    properties.setProperty("imCreationDate", String.valueOf(imMovementHead.getCreationDate()));
		    properties.setProperty("imLastUpdatedByName", UserUtils.getUsernameByEmployeeCode(imMovementHead.getLastUpdatedBy()));
		    properties.setProperty("imLastUpdateDate", String.valueOf(imMovementHead.getLastUpdateDate()));

		} else {
		    properties.setProperty("orderNo", orderNo);
		    properties.setProperty("statusName", "查無此調撥單");
		}

	    } else {
		properties.setProperty("orderNo", orderNo);
		properties.setProperty("statusName", "查無此調撥單");
	    }
	    result.add(properties);
	    return result;

	} catch (Exception ex) {
	    log.error("取得調撥單資料發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得調撥單資料失敗！");
	}

    }

    /**
     * ajax 取得移倉單search分頁
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception{

	try{
	    List<Properties> result = new ArrayList();
	    List<Properties> gridDatas = new ArrayList();
	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

	    //======================帶入Head的值=========================

	    String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
	    String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

	    String orderNo = httpRequest.getProperty("orderNo" );
	    Date deliveryDateStart = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("deliveryDateStart") );
	    Date deliveryDateEnd = DateUtils.parseDate( "yyyy/MM/dd",httpRequest.getProperty("deliveryDateEnd") );

	    String status = httpRequest.getProperty("status");
	    String passNo = httpRequest.getProperty("passNo");
	    String customsArea = httpRequest.getProperty("customsArea");
	    String sealNo = httpRequest.getProperty("sealNo");
	    String transferOrderNo = httpRequest.getProperty("transferOrderNo");

	    HashMap map = new HashMap();
	    HashMap findObjs = new HashMap();
	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
	    findObjs.put(" and model.orderTypeCode = :orderTypeCode",orderTypeCode);
	    findObjs.put(" and model.orderNo NOT LIKE :TMP","TMP%");
	    findObjs.put(" and model.orderNo = :orderNo",orderNo);
	    findObjs.put(" and model.deliveryDate >= :deliveryDateStart",deliveryDateStart);
	    findObjs.put(" and model.deliveryDate <= :deliveryDateEnd",deliveryDateEnd);
	    findObjs.put(" and model.status = :status",status);
	    findObjs.put(" and model.passNo = :passNo",passNo);
	    findObjs.put(" and model.customsArea like :customsArea","%" + customsArea + "%");
	    findObjs.put(" and model.sealNo = :sealNo",sealNo);
	    findObjs.put(" and model.transferOrderNo = :transferOrderNo",transferOrderNo);

	    //==============================================================

	    Map cmMovementHeadMap = cmMovementHeadDAO.search( "CmMovementHead as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
	    List<CmMovementHead> cmMovementHeads = (List<CmMovementHead>) cmMovementHeadMap.get(BaseDAO.TABLE_LIST);

	    if (cmMovementHeads != null && cmMovementHeads.size() > 0) {

		this.setCmMovementStatusName(cmMovementHeads);
		
		this.setCustomsStatusName(cmMovementHeads);
		
		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		Long maxIndex = (Long)cmMovementHeadDAO.search("CmMovementHead as model", "count(model.headId) as rowCount" ,findObjs, "order by lastUpdateDate desc", BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

		result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,cmMovementHeads, gridDatas, firstIndex, maxIndex));
	    }else {
		result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
	    }

	    return result;
	}catch(Exception ex){
	    log.error("載入頁面顯示的移倉單查詢發生錯誤，原因：" + ex.toString());
	    throw new Exception("載入頁面顯示的移倉單查詢失敗！");
	}
    }

    /**
     * ajax picker按檢視返回選取的資料
     * @param parameterMap
     * @return
     * @throws FormException
     * @throws Exception
     */
    public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
	Map resultMap = new HashMap(0);
	Map pickerResult = new HashMap(0);
	try{
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
	    ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

	    List<Properties> result = AjaxUtils.getSelectedResults(timeScope, searchKeys);
	    if(result.size() > 0 ){
		pickerResult.put("result", result);
	    }
	    resultMap.put("vatBeanPicker", pickerResult);
	    resultMap.put("topLevel",  new String[]{"vatBeanPicker"});
	}catch(Exception ex){
	    log.error("檢視失敗，原因：" + ex.toString());
	    Map messageMap = new HashMap();
	    messageMap.put("type"   , "ALERT");
	    messageMap.put("message", "檢視失敗，原因："+ex.toString());
	    messageMap.put("event1" , null);
	    messageMap.put("event2" , null);
	    resultMap.put("vatMessage",messageMap);

	}
	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }

    /**
     * ajax 取得員工名字
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> getAJAXEmployName(Properties httpRequest) throws Exception{
	List<Properties> result = new ArrayList();
	Properties properties = new Properties();
	String brandCode = null;
	String applicantCode = null;
	String deliverymanCode = null;
	try {
	    brandCode = httpRequest.getProperty("brandCode");
	    applicantCode = httpRequest.getProperty("applicantCode").trim().toUpperCase();
	    deliverymanCode = httpRequest.getProperty("deliverymanCode").trim().toUpperCase();

	    if( applicantCode != "" ){

		properties.setProperty("applicantCode", applicantCode);

		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, applicantCode);
		if(employeeWithAddressView != null){
		    properties.setProperty("applicantName", employeeWithAddressView.getChineseName());
		} else {
		    properties.setProperty("applicantName", "查無此申請人");
		}
	    }else if( deliverymanCode != "" ){

		properties.setProperty("deliverymanCode", deliverymanCode);

		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, deliverymanCode);
		if(employeeWithAddressView != null){
		    properties.setProperty("deliverymanName", employeeWithAddressView.getChineseName());
		} else {
		    properties.setProperty("deliverymanName", "查無此發貨人");
		}
	    }

	    result.add(properties);

	    return result;
	} catch (Exception ex) {
	    log.error("依據品牌代號：" + brandCode + "、申請人：" + applicantCode + "或發貨人：" + deliverymanCode + "查詢時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢員工代號資料失敗！");
	}
    }

    /**
     * 取得CC開窗URL字串
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

	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞
	    Map parameters = new HashMap(0);
	    parameters.put("prompt0", "[\""+orderNo+"\",\"000\"]");
//	    parameters.put("prompt1", orderTypeCode);
//	    parameters.put("prompt2", "");
//	    parameters.put("prompt3", "");
//	    parameters.put("prompt4", orderNo);
//	    parameters.put("prompt5", orderNo);
	    String reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
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

    /**
     * 取得CC開窗URL字串(縮小清單)
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public List<Properties> getReduceReportConfig(Map parameterMap) throws Exception  {
	try{
	    Object otherBean = parameterMap.get("vatBeanOther");
	    String brandCode = (String)PropertyUtils.getProperty(otherBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

	    Map returnMap = new HashMap(0);
	    //CC後面要代的參數使用parameters傳遞
	    Map parameters = new HashMap(0);
	    parameters.put("prompt0", "");
	    parameters.put("prompt1", "");
	    parameters.put("prompt2", orderTypeCode);
	    parameters.put("prompt3", orderNo);
	    parameters.put("prompt4", orderNo);

	    String reportUrl = new String();
	    try{
		String reportFileName = (String)PropertyUtils.getProperty(otherBean, "reportFileName");
		reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName, parameters);
	    }catch(Exception ex){
		reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
	    }

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

    /**
     * 將移倉單明細表 mark 為刪除的刪掉
     * @param head
     */
    private void deleteLine(CmMovementHead head){

	List<CmMovementLine> cmMovementLines = head.getCmMovementLines();
	if(cmMovementLines != null && cmMovementLines.size() > 0){
	    for(int i = cmMovementLines.size() - 1; i >= 0; i--){
		CmMovementLine cmMovementLine = cmMovementLines.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(cmMovementLine.getIsDeleteRecord())){
		    cmMovementLines.remove(cmMovementLine);
		    cmMovementLineDAO.delete(cmMovementLine);
		}
	    }
	}
    }


    /**
     * 移倉單存檔,取得暫存碼
     * @param imLetterOfCreditHead
     * @throws Exception
     */
    private void saveTmpHead(CmMovementHead head) throws Exception{

	try{
	    String tmpOrderNo = AjaxUtils.getTmpOrderNo();
	    head.setOrderNo(tmpOrderNo);
	    baseDAO.save(head);
	}catch(Exception ex){
	    log.error("取得暫時單號儲存移倉單發生錯誤，原因：" + ex.toString());
	    throw new Exception("取得暫時單號儲存移倉單發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 將調撥單主檔查詢結果存檔
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
	String errorMsg = null;
	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
	return AjaxUtils.getResponseMsg(errorMsg);
    }

    /**
     * 設定移倉單主檔其他欄位
     * @param head
     * @return
     * @throws Exception
     */
    private void setOtherColumn(CmMovementHead head)throws Exception{
	BuOrderType buOrderType = null;
	try{

	    String orderTypeCode = head.getOrderTypeCode();
	    String brandCode = head.getBrandCode();
	    if(StringUtils.hasText( orderTypeCode ) && StringUtils.hasText( brandCode ) ){

		buOrderType = buOrderTypeDAO.findFirstByProperty("BuOrderType" , "and id.orderTypeCode = ? and id.brandCode = ?", new Object[]{orderTypeCode, brandCode } );
		if(buOrderType != null){
		    head.setDeliveryCustomsWarehouse( buOrderType.getCmMoveWarehouseFrom() );
		    head.setArrivalCustomsWarehouse( buOrderType.getCmMoveWarehouseTo() );
		    head.setTaxType( buOrderType.getTaxCode() );

		    if( "F".equals(head.getTaxType()) ){
			head.setExpenseAmount(NumberUtils.getLong(buOrderType.getReserve1())); //  規費 依單別拉出規費
		    }
		}else {
		    throw new Exception("請確認 BuOrderType table 有無此:" + orderTypeCode + "與" + brandCode);
		}

	    }

	}catch(Exception ex){
	    log.error("設定移倉單主檔其他欄位時發生錯誤，原因：" + ex.toString());
	    throw new Exception("設定移倉單主檔其他欄位時發生錯誤，原因：" + ex.toString());

	}
    }

    /**
     * 設定移倉單明細檔額外欄位
     * @param cmMovementLines
     */
    private List<CmMovementLine> setLinesOtherColumn(String brandCode, String status, String taxType, List<CmMovementLine> cmMovementLines){
	ImMovementHead head = null;

	for (CmMovementLine cmMovementLine : cmMovementLines) {

	    String orderTypeCode = cmMovementLine.getOrderTypeCode();

	    log.info("OrderNo() = " + cmMovementLine.getOrderNo());
	    log.info("brandCode = " + brandCode);
	    log.info("orderTypeCode = " + orderTypeCode);
	    log.info("taxType = " + taxType);

	    if( OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)  ){
		head = this.findOneImMovement(cmMovementLine.getOrderNo(), brandCode, orderTypeCode, taxType);
	    }else if( OrderStatus.SIGNING.equals(status) ){
		head = (ImMovementHead)imMovementHeadDAO.findFirstByProperty(
			"ImMovementHead","and orderNo = ? and brandCode = ? and orderTypeCode = ? and taxType = ? and cmMovementNo IS NOT NULL " + " and status = ?",
			new Object[] { cmMovementLine.getOrderNo(), brandCode, orderTypeCode, taxType, OrderStatus.WAIT_IN });
	    }else{
		head = this.findOneImMovement(cmMovementLine.getOrderNo(), brandCode, orderTypeCode, taxType, false);
	    }

	    log.info("head = " + head );

	    if(head != null ){
		log.info( "ImMovementHead.getOrderNo = " + head.getOrderNo() );
		String deliveryWarehouseCode = head.getDeliveryWarehouseCode();
		String arrivalWarehouseCode = head.getArrivalWarehouseCode();
		ImWarehouse imWarehouseDelivery = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, deliveryWarehouseCode, null);
		ImWarehouse imWarehouseArrival = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, arrivalWarehouseCode, null );

		if(imWarehouseDelivery != null && imWarehouseArrival != null){

		    log.info( "DeliveryWarehouseCode.getWarehouseName = " + imWarehouseDelivery.getWarehouseName() );
		    log.info( "ArrivalWarehouseCode.getWarehouseName = " + imWarehouseArrival.getWarehouseName() );

		    cmMovementLine.setStatusName( OrderStatus.getChineseWord(head.getStatus()) );

		    cmMovementLine.setDeliveryWarehouseName( deliveryWarehouseCode + "-" + imWarehouseDelivery.getWarehouseName()  );
		    cmMovementLine.setArrivalWarehouseName( arrivalWarehouseCode + "-" + imWarehouseArrival.getWarehouseName() );
		    cmMovementLine.setImCreatedByName( UserUtils.getUsernameByEmployeeCode(head.getCreatedBy()) );
		    cmMovementLine.setImCreationDate( head.getCreationDate() );
		    cmMovementLine.setImLastUpdatedByName( UserUtils.getUsernameByEmployeeCode(head.getLastUpdatedBy()) );
		    cmMovementLine.setImLastUpdateDate( head.getLastUpdateDate() );

		}else{
		    cmMovementLine.setStatusName( "查無此調撥單" );
		    cmMovementLine.setDeliveryWarehouseName( "" );
		    cmMovementLine.setArrivalWarehouseName( "" );
		}
	    }else{
		cmMovementLine.setStatusName( "查無此調撥單" );
		cmMovementLine.setDeliveryWarehouseName( "" );
		cmMovementLine.setArrivalWarehouseName( "" );
	    }

	}
	return cmMovementLines;
    }

    /**
     * 移倉單依formAction取得下個狀態
     */
    public void setNextStatus(CmMovementHead head, String formAction, String approvalResult){

	if(OrderStatus.FORM_SAVE.equals(formAction)){
	    head.setStatus(OrderStatus.SAVE);
	}else if(OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction) ){
	    if(OrderStatus.SAVE.equals( head.getStatus() ) || OrderStatus.REJECT.equals( head.getStatus() ) ){
		head.setStatus(OrderStatus.SIGNING);
	    } else if(OrderStatus.SIGNING.equals( head.getStatus() )){
		if("true".equals(approvalResult)){
		    head.setStatus(OrderStatus.FINISH);
		}else{
		    head.setStatus(OrderStatus.REJECT);
		}
	    }
	}else if(OrderStatus.FORM_VOID.equals(formAction)){
	    head.setStatus(OrderStatus.VOID);
	}

    }

	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(CmMovementHead head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo(head.getBrandCode(), head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得" + head.getBrandCode() + "-" + head.getOrderTypeCode()
							+ "單號失敗！");
				else {
					head.setCreationDate(new Date());
					head.setCreatedBy(head.getCreatedBy());
					head.setOrderNo(serialNo);
				}
				// 運送單號
				if (head.getTransferOrderNo() == null || head.getTransferOrderNo().length() == 0) {
					if ("RVM".equals(head.getOrderTypeCode()) || 
							"RWK".equals(head.getOrderTypeCode()) || 
							"RWD".equals(head.getOrderTypeCode()) || 
							"RMK".equals(head.getOrderTypeCode()) || 
							"RMV".equals(head.getOrderTypeCode()) || 
							"RMM".equals(head.getOrderTypeCode()) || 
							"RAP".equals(head.getOrderTypeCode()) || 
							"RPA".equals(head.getOrderTypeCode())) { // RWD/RMK 需要增加運送單號碼
						String transferOrderNo = buOrderTypeService.getOrderNo(head.getBrandCode(), "TMA", null, null);
						head.setTransferOrderNo("TMA" + transferOrderNo);
					}
				}
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}

    /**
     * 設定中文狀態名稱
     * @param cmMovementHeads
     */
    private void setCmMovementStatusName(List<CmMovementHead> cmMovementHeads){
	for (CmMovementHead cmMovementHead : cmMovementHeads) {
	    cmMovementHead.setStatusName(OrderStatus.getChineseWord(cmMovementHead.getStatus()));
	}
    }
    
    /**
     * 設定中文狀態名稱
     * @param cmMovementHeads
     * @throws Exception 
     */
    private void setCustomsStatusName(List<CmMovementHead> cmMovementHeads) throws Exception{
    	CustomsProcessResponseDAO cusDAO = new CustomsProcessResponseDAO();
		for (CmMovementHead cmMovementHead : cmMovementHeads) {
		    cmMovementHead.setCustomsStatusName(cusDAO.getProcessResponseByCode(cmMovementHead.getCustomsStatus()));
		}
    }

    /**
     * 啟動流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(CmMovementHead form) throws ProcessFailedException{

	try{
	    String packageId = "Cm_Movement";
	    String processId = "process";
	    String version = "20091001";
	    String sourceReferenceType = "CmMovement (1)";
	    HashMap context = new HashMap();
	    context.put("formId", form.getHeadId());
	    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	}catch (Exception ex){
	    ex.printStackTrace();
	    log.error("移倉單流程啟動失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("移倉單流程啟動失敗！"+ ex.getMessage());
	}
    }

    /**
     * 更新bean
     * @param head
     */
    public void update(CmMovementHead head) {
	try {
	    cmMovementHeadDAO.update(head);
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("e = " + e.toString());
	}
    }


    /**
     * ajax  更新移倉單明細
     * @param httpRequest
     * @return
     * @throws Exception
     */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		String errorMsg = null;
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));

			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String status = httpRequest.getProperty("status");

			if (headId == null) {
				throw new ValidationErrorException("傳入的移倉單主鍵為空值！");
			}

			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					GRID_FIELD_NAMES);
			// Get INDEX NO
			int indexNo = cmMovementLineDAO.findPageLineMaxIndex(headId).intValue();
			log.info("MaxIndexNo = " + indexNo);

			Map findObjs = new HashMap();
			findObjs.put("and model.cmMovementHead.headId = :headId", headId);
			// int indexNo = ((Long) baseDAO.search("CmMovementLine as model", "count(model.cmMovementHead.headId) as rowCount", findObjs,
			// BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT)).intValue(); // 取得最後一筆 INDEX

			if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
				// 考慮狀態
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						// 先載入HEAD_ID OR LINE DATA

						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[2]);

						if (StringUtils.hasText(itemCode)) {

							CmMovementLine cmMovementLine = cmMovementLineDAO.findFirstByProperty("CmMovementLine",
									"and cmMovementHead.headId = ? and lineId = ?", new Object[] { headId, lineId });
							log.info("cmMovementLine = " + cmMovementLine + "\nlineId = " + lineId);
							Date date = new Date();
							if (cmMovementLine != null) {
								log.info("更新 = " + headId + " | " + lineId);
								AjaxUtils.setPojoProperties(cmMovementLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								cmMovementLine.setLastUpdatedBy(loginEmployeeCode);
								cmMovementLine.setLastUpdateDate(date);
								cmMovementLineDAO.update(cmMovementLine);

							} else {
								indexNo++;
								log.info("新增 = " + headId + " | " + indexNo);
								cmMovementLine = new CmMovementLine();

								AjaxUtils.setPojoProperties(cmMovementLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
								cmMovementLine.setCmMovementHead(new CmMovementHead(headId));
								cmMovementLine.setCreatedBy(loginEmployeeCode);
								cmMovementLine.setCreationDate(date);
								cmMovementLine.setLastUpdatedBy(loginEmployeeCode);
								cmMovementLine.setLastUpdateDate(date);
								cmMovementLine.setIndexNo(Long.valueOf(indexNo));
								cmMovementLineDAO.save(cmMovementLine);

							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);

		} catch (Exception ex) {
			log.error("更新移倉單明細資料發生錯誤，原因：" + ex.toString());
			throw new Exception("更新移倉單明細資料失敗！");
		}
	}

    /**
     * 檢核,存取移倉單主檔,移倉單明細檔,重新設定狀態
     * @param parameterMap
     * @return
     * @throws Exception
     */
	public Map updateAJAXCmMovement(Map parameterMap, String formAction) throws Exception {
		Map resultMap = new HashMap();
		List errorMsgs = null;
		String resultMsg = null;
		try {

			Object otherBean = parameterMap.get("vatBeanOther");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");

			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String approvalResult = (String) PropertyUtils.getProperty(otherBean, "approvalResult");

			CmMovementHead head = this.getActualCmMovement(formLinkBean);

			// 檢核
			if (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)) {
				this.deleteLine(head);
				errorMsgs = this.checkedCmMovement(parameterMap);
			}

			if (errorMsgs == null || errorMsgs.size() == 0) {
				// 設定單號
				this.setOrderNo(head);

				// 成功則設定下個狀態
				this.setNextStatus(head, formAction, approvalResult);

				// 將明細依單號排序
				updateDetailOrderByOrderNo(head);

				// 更新移倉單主檔明細檔,im和cm庫存
				if ((OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction))) {
					this.updateCmAndIm(head, employeeCode, errorMsgs);
				}
				
				
				if(OrderStatus.FORM_SUBMIT.equals(formAction)){

					if((head.getOrderTypeCode().equals("RMV")|| 
							head.getOrderTypeCode().equals("RWD")||
							head.getOrderTypeCode().equals("RDW")||
							head.getOrderTypeCode().equals("RWK")||
							head.getOrderTypeCode().equals("RKW")||
							head.getOrderTypeCode().equals("RAP")||
							head.getOrderTypeCode().equals("RPA"))){

                        resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 將此單據送簽海關?";
					}else{
						
						resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";
					}
				}else{
				    resultMsg = head.getOrderTypeCode() + "-" + head.getOrderNo() + "存檔成功！ 是否繼續新增？";
				}
				
			} else if (errorMsgs.size() > 0) {
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
			}
			resultMap.put("entityBean", head);
			resultMap.put("resultMsg", resultMsg);
		} catch (ValidationErrorException ve) {
			log.error("移倉單檢核時發生錯誤，原因：" + ve.toString());
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} catch (Exception e) {
			log.error("移倉單存檔時發生錯誤，原因：" + e.toString());
			throw new Exception("移倉單存檔時發生錯誤，原因：" + e.getMessage());
		}

		return resultMap;
	}

    /**
     * 前端資料塞入bean
     * @param parameterMap
     * @return
     */
	public Map updateCmMovementBean(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// 取得欲更新的bean
			CmMovementHead cmMovementHead = this.getActualCmMovement(formLinkBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, cmMovementHead);
			resultMap.put("entityBean", cmMovementHead);
			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("移倉單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
    }

    /**
     * 存檔移倉單,存取移倉單on_hand,報關單on_hand
     * @param head
     * @param employeeCode
     * @return
     * @throws FormException
     * @throws Exception
     */
    public void updateCmAndIm(CmMovementHead head, String employeeCode, List errorMsgs ) throws FormException, Exception {
	log.info("======開始存取庫存=====");
	// 存檔移倉單
	this.updateCmMovement(head, employeeCode );

	// 存取庫存 cm_on_hand 和 im_on_hand ,更新每筆調撥單
	this.updateStockAndImMovement(head, employeeCode, errorMsgs);

	log.info("======存取庫存結束=====");
    }

    /**
     *  取單號後更新更新主檔
     *
     * @param parameterMap
     * @return Map
     * @throws FormException
     * @throws Exception
     */
    public Map updateCmMovementWithActualOrderNO(Map parameterMap) throws FormException, Exception {

	Map resultMap = new HashMap();
	try{

	    resultMap = this.updateCmMovementBean(parameterMap);
	    CmMovementHead cmMovementHead = (CmMovementHead) resultMap.get("entityBean");

	    //刪除於SI_PROGRAM_LOG的原識別碼資料
	    String identification = MessageStatus.getIdentification(cmMovementHead.getBrandCode(),
		    cmMovementHead.getOrderTypeCode(), cmMovementHead.getOrderNo());
	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);

	    this.setOrderNo(cmMovementHead);
	    String resultMsg = cmMovementHead.getOrderNo() + "存檔成功！是否繼續新增？"; // modifyAjaxSoSalesOrder(salesOrderHeadPO, employeeCode);  !!! 移倉單編碼等過來
	    resultMap.put("resultMsg", resultMsg);

	    return resultMap;
	} catch (FormException fe) {
	    log.error("移倉單存檔失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("移倉單存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("移倉單存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * ajax 更新line by picker
     * @param httpRequest
     * @return
     * @throws Exception
     */
    public List<Properties> updateCmMovementLine(Map parameterMap) throws Exception{
	List<Properties> result = new ArrayList();
	Map returnMap = new HashMap(0);
	try {
	    Object pickerBean = parameterMap.get("vatBeanPicker");
	    Object otherBean = parameterMap.get("vatBeanOther");

	    List<Object> pickResults 	=(List<Object>)PropertyUtils.getProperty(pickerBean,"result");
	    Long headId 				= NumberUtils.getLong((String)PropertyUtils.getProperty(otherBean,"headId"));
	    Integer lineId 				= (Integer)PropertyUtils.getProperty(otherBean, "lineId");

	    log.info("pickResults = " + pickResults);
	    log.info("pickResults.size() = " + pickResults.size());
	    log.info("headId = " + headId);
	    log.info("lineId = " + lineId);

	    if(null != headId && null != pickResults && pickResults.size()> 0){
		CmMovementHead head = (CmMovementHead)cmMovementHeadDAO.findById("CmMovementHead",headId);
		Long id = 0L;
		if( null != head ){
		    List<CmMovementLine> cmMovementLines = head.getCmMovementLines();
		    log.info("cmMovementLines.size() = " + cmMovementLines.size());
		    // 取得點選的line 更新
//		    if( cmMovementLines.size() > 0 && lineId -1 <= cmMovementLines.size()){
//		    id = NumberUtils.getLong((String)PropertyUtils.getProperty(pickResults.get(0),"headId"));
//		    log.info("id = " + id);

//		    CmMovementLine cmMovementLine = cmMovementLines.get(lineId);

//		    cmMovementLine.setOrderNo(cmMovementLine.getOrderNo());
//		    cmMovementLine.setOrderTypeCode(cmMovementLine.getOrderTypeCode());
//		    }

		    // append 後面的 line
		    for(Object pickResult: pickResults){
			id = NumberUtils.getLong((String)PropertyUtils.getProperty(pickResult,"headId"));
			log.info("for id = " + id);
			ImMovementHead imMovementHead = imMovementHeadDAO.findById(id);
			log.info("imMovementHead = " + imMovementHead);
			CmMovementLine cmMovementLine = new CmMovementLine();
			cmMovementLine.setCmMovementHead(new CmMovementHead(headId));
			cmMovementLine.setOrderTypeCode(imMovementHead.getOrderTypeCode());
			cmMovementLine.setOrderNo(imMovementHead.getOrderNo());
			cmMovementLines.add(cmMovementLine);
		    }
		    head.setCmMovementLines(cmMovementLines);
		    cmMovementHeadDAO.update(head);
		}
	    }
	    log.info("end = end" );
	    return AjaxUtils.parseReturnDataToJSON(returnMap);
	} catch (Exception ex) {
	    log.error("更新移倉單明細發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新移倉單明細發生錯誤，原因：" + ex.getMessage());
	}
    }


    /**
     * 明細依單別單號排序
     * @param head
     */
	public void updateDetailOrderByOrderNo(CmMovementHead head) {
		// List<CmMovementLine> lines = new ArrayList<CmMovementLine>();
		List<CmMovementLine> newLines = cmMovementLineDAO.findByProperty("CmMovementLine", "",
				"and cmMovementHead.headId = ?", new Object[] { head.getHeadId() }, "order by orderTypeCode, orderNo");
		Long i = 1L;
		for (CmMovementLine cmMovementLine : newLines) {
			cmMovementLine.setIndexNo(i);
			i++;
		}
		head.setCmMovementLines(newLines);
		cmMovementHeadDAO.update(head);
	}
	
	
	public String updateMoveWhNo(CmMovementHead cmMovementHead, String moveWhNo) throws FormException , Exception{
		
		
		 
		    String msg = null;
			if(!StringUtils.hasText(moveWhNo)){
				log.info("99999:"+moveWhNo);
				 throw new FormException("移倉申請書號碼為空值!!");
			 }else{
				 cmMovementHead.setMoveWhNo(moveWhNo);
				 cmMovementHeadDAO.update(cmMovementHead);
				 msg = "移倉申請書儲存成功";
			 }
		
		 log.info("222222");
		 return msg;
	}

	/**
	 * 新增或是更新運送單資料
	 *
	 * @param cmMovementHead
	 * @param formBean
	 * @throws FormException
	 * @throws Exception
	 */
	public void updateCmTransfer(CmMovementHead cmMovementHead, Object formBean, String employeeCode) throws FormException,
			Exception {
		try {
			log.info("===== 寫入運送單資料！！ =====:"+cmMovementHead.getTransferOrderNo());
			CmTransfer cmTransfer = new CmTransfer();
			AjaxUtils.copyJSONBeantoPojoBean(formBean, cmTransfer);

			cmTransfer.setOrderNo(cmMovementHead.getOrderNo());
			cmTransfer.setStatus(cmMovementHead.getStatus());
			List<CmMovementLine> cmMovementLines = cmMovementHead.getCmMovementLines();
			if (cmMovementLines != null && cmMovementLines.size() > 0) {
				Double boxCount = 0.0;
				Double itemCount = 0.0;
				for (int i = 0; i < cmMovementLines.size(); i++) {
					ImMovementHead imHead = imMovementHeadDAO.findMovementByIdentification(cmMovementHead.getBrandCode(),
							((CmMovementLine) cmMovementLines.get(i)).getOrderTypeCode(), ((CmMovementLine) cmMovementLines
									.get(i)).getOrderNo());
					boxCount += imHead.getBoxCount();
					itemCount += imHead.getItemCount();
				}
				cmTransfer.setLeaveBox(boxCount.intValue());
				cmTransfer.setLeaveQuantity(itemCount.intValue());
				cmTransfer.setTruckBox(boxCount.intValue());
				cmTransfer.setTruckQuantity(itemCount.intValue());
			} else {
				cmTransfer.setLeaveBox(0);
				cmTransfer.setLeaveQuantity(0);
				cmTransfer.setTruckBox(0);
				cmTransfer.setTruckQuantity(0);
			}
			// 運送單保卡號碼為關別+保卡號碼
			cmTransfer.setCarNo(cmMovementHead.getCustomsArea() + " " + cmMovementHead.getCarNo());
			cmTransfer.setTransferOrderNo(cmMovementHead.getTransferOrderNo());
			cmTransfer.setCreatedBy(cmMovementHead.getCreatedBy());
			cmTransfer.setCreationDate(cmMovementHead.getCreationDate());
			cmTransfer.setLastUpdatedBy(employeeCode);
			cmTransfer.setLastUpdateDate(new Date());
			cmTransfer.setTruckBoxNote("CTN");
			cmTransfer.setTruckQuantityNote("PCS");
			cmTransfer.setLeaveBoxNote("CTN");
			cmTransfer.setLeaveQuantityNote("PCS");
			String leaveTimeT = (String) PropertyUtils.getProperty(formBean, "leaveTimeT");
			String arriveTimeT = (String) PropertyUtils.getProperty(formBean, "arriveTimeT");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			if (leaveTimeT.equals(""))
				cmTransfer.setLeaveTime(new Date());
			else {
				String leaveDateString = (Integer.parseInt(leaveTimeT.substring(0, 3)) + 1911) + "/"
						+ leaveTimeT.substring(3, 5) + "/" + leaveTimeT.substring(5, 7) + " " + leaveTimeT.substring(7, 9)
						+ ":" + leaveTimeT.substring(9) + ":00";
				Date dateL = sdf.parse(leaveDateString);
				cmTransfer.setLeaveTime(dateL);
			}
			if (!arriveTimeT.equals("")) {
				String arriveDateString = (Integer.parseInt(arriveTimeT.substring(0, 3)) + 1911) + "/"
						+ arriveTimeT.substring(3, 5) + "/" + arriveTimeT.substring(5, 7) + " "
						+ arriveTimeT.substring(7, 9) + ":" + arriveTimeT.substring(9) + ":00";
				Date dateA = sdf.parse(arriveDateString);
				cmTransfer.setArriveTime(dateA);
			}
			CmTransfer checkCmTransfer = cmTransferDAO.findById(cmMovementHead.getTransferOrderNo());
			if (checkCmTransfer == null)
				cmTransferDAO.save(cmTransfer);
			else
				cmTransferDAO.merge(cmTransfer);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("運送單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 更新運送單的列印日期
	 *
	 * @param parameterMap
	 * @throws FormException
	 * @throws Exception
	 */
	public List<Properties> updateCmTransferPrintTime(Map parameterMap) throws FormException, Exception {

		Object formBean = parameterMap.get("vatBeanFormBind");
		Object otherBean = parameterMap.get("vatBeanOther");
		String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		try {
			log.info("===== 寫入列印運送單的時間！！ =====");

			CmTransfer cmTransfer = new CmTransfer();
			AjaxUtils.copyJSONBeantoPojoBean(formBean, cmTransfer);
			cmTransfer.setTransferOrderNo(cmTransfer.getTransferOrderNo());
			CmTransfer checkCmTransfer = cmTransferDAO.findById(cmTransfer.getTransferOrderNo());
			String leaveTimeT = (String) PropertyUtils.getProperty(formBean, "leaveTimeT");
			if (leaveTimeT == "")
				cmTransfer.setLeaveTime(new Date());
			else
				cmTransfer.setLeaveTime(checkCmTransfer.getLeaveTime());
			cmTransfer.setCarNo(checkCmTransfer.getCarNo());
			cmTransfer.setLeaveBox(checkCmTransfer.getLeaveBox());
			cmTransfer.setLeaveQuantity(checkCmTransfer.getLeaveQuantity());
			cmTransfer.setTruckBox(checkCmTransfer.getTruckBox());
			cmTransfer.setTruckQuantity(checkCmTransfer.getTruckQuantity());
			cmTransfer.setCreatedBy(checkCmTransfer.getCreatedBy());
			cmTransfer.setCreationDate(checkCmTransfer.getCreationDate());
			cmTransfer.setLastUpdatedBy(employeeCode);
			cmTransfer.setLastUpdateDate(new Date());
			if (checkCmTransfer.getTransferOrderNo().indexOf("TMA") > -1) {
				cmTransfer.setTruckBoxNote("CTN");
				cmTransfer.setTruckQuantityNote("PCS");
				cmTransfer.setLeaveBoxNote("CTN");
				cmTransfer.setLeaveQuantityNote("PCS");
			}
			if (!cmTransfer.getStatus().equals("FINISH")) {
				if (checkCmTransfer == null)
					cmTransferDAO.save(cmTransfer);
				else
					cmTransferDAO.merge(cmTransfer);
			}
			return AjaxUtils.parseReturnDataToJSON(new HashMap());
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("運送單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 取得CC開窗URL字串(貨櫃物運送單列印)
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getTransferReduceReportConfig(Map parameterMap) throws Exception {
		try {
			System.out.println("===== 取得報表連結設定 =====");
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "brandCode");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String transferOrderNo = (String) PropertyUtils.getProperty(otherBean, "transferOrderNo");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			Map returnMap = new HashMap(0);
			// CC後面要代的參數使用parameters傳遞
			Map parameters = new HashMap(0);
			parameters.put("prompt0", transferOrderNo);

			String reportUrl = new String();
			try {
				String reportFileName = (String) PropertyUtils.getProperty(otherBean, "reportFileName");
				reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName,
						parameters);
			} catch (Exception ex) {
				reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
			}

			returnMap.put("reportUrl", reportUrl);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (IllegalAccessException iae) {
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		} catch (InvocationTargetException ite) {
			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		} catch (NoSuchMethodException nse) {
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" + nse.getMessage());
		}
	}

    /**
     * 更新移倉單主檔明細檔
     * @param head
     * @param employeeCode
     */
    private void updateCmMovement(CmMovementHead head, String employeeCode ){
	Date date = new Date();
	if (head.getHeadId() != null) {
	    head.setLastUpdateDate(date);
	    head.setLastUpdatedBy(employeeCode);

	    List<CmMovementLine> lines = head.getCmMovementLines();
	    for (CmMovementLine cmMovementLine : lines) {
		cmMovementLine.setLastUpdateDate(date);
		cmMovementLine.setLastUpdatedBy(employeeCode);
	    }

	    cmMovementHeadDAO.update(head);
	}
    }

    /**
     * 更新cm_on_hand 和 im_on_hand 庫存和更新調撥單主檔(狀態,移倉單,修改日,修改人)
     * @param head
     */
    private void updateStockAndImMovement(CmMovementHead cmMovementHead, String employeeCode, List errorMsgs )throws FormException, Exception {
	log.info( "======updateStockAndImMovement開始=======");
	List<CmMovementLine> lines = cmMovementHead.getCmMovementLines();
	log.info( "CmMovementLines.size() = " + lines.size());
	for (CmMovementLine cmMovementLine : lines) {
	    if(OrderStatus.FINISH.equals(cmMovementHead.getStatus()) || OrderStatus.REJECT.equals(cmMovementHead.getStatus()) || OrderStatus.VOID.equals(cmMovementHead.getStatus())){

		ImMovementHead imMovementHead = this.findOneImMovement(cmMovementLine.getOrderNo(), cmMovementHead.getBrandCode(), cmMovementLine.getOrderTypeCode(), cmMovementHead.getTaxType(), false);
		log.info( "imMovementHead = " + imMovementHead );
		if( imMovementHead != null ){
		    if(OrderStatus.FINISH.equals(cmMovementHead.getStatus())){
			this.updateImMovement(cmMovementLine, cmMovementHead, imMovementHead, employeeCode, errorMsgs);
			
				//for 儲位用
	    		if(imStorageAction.isStorageExecute(imMovementHead)){
	    			//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
	    			imStorageService.updateStorageOnHandBySource(imMovementHead, cmMovementHead.getStatus(), null, null, false);
	    		}
    		
		    }else if(OrderStatus.REJECT.equals(cmMovementHead.getStatus()) || OrderStatus.VOID.equals(cmMovementHead.getStatus())){
			imMovementHead.setCmMovementNo(null);
			
				//for 儲位用
	    		if(imStorageAction.isStorageExecute(imMovementHead)){
	    			//異動儲位庫存，目前只能送出增加庫存，若為駁回isReject應為true
	    			imStorageService.updateStorageOnHandBySource(imMovementHead, cmMovementHead.getStatus(), null, null, true);
	    		}
		    }
		}else{
		    throw new FormException("移倉單狀態:" + cmMovementHead.getStatus() + "品牌:" + cmMovementHead.getBrandCode() + "單別" + cmMovementLine.getOrderTypeCode() + "調撥單號:" + cmMovementLine.getOrderNo() + "稅別:"+ cmMovementHead.getTaxType() + "資料異常， 無法查出調撥單資訊");
		}
	    }else if(OrderStatus.SIGNING.equals(cmMovementHead.getStatus())){
		ImMovementHead imMovementHead = this.findOneImMovement(cmMovementLine.getOrderNo(), cmMovementHead.getBrandCode(), cmMovementLine.getOrderTypeCode(), cmMovementHead.getTaxType());
		log.info( "imMovementHead = " + imMovementHead );
		if( imMovementHead != null ){
		    imMovementHead.setCmMovementNo( cmMovementHead.getOrderNo() );
		}else{
		    throw new FormException("移倉單狀態:" + cmMovementHead.getStatus() + "品牌:" + cmMovementHead.getBrandCode() + "單別" + cmMovementLine.getOrderTypeCode() + "調撥單號:" + cmMovementLine.getOrderNo() + "稅別:"+ cmMovementHead.getTaxType() + "資料異常， 無法查出調撥單資訊");
		}
	    }
	}
	log.info( "======updateStockAndImMovement結束=======");
    }

    /**
     * 更新調撥單主檔(狀態,移倉單,修改日,修改人),明細檔(修改日,修改人), 存取庫存
     * @param head
     */
    private void updateImMovement(CmMovementLine cmMovementLine, CmMovementHead cmMovementHead, ImMovementHead imMovementHead, String employeeCode, List errorMsgs)
    throws FormException, Exception {
    	log.info( "======updateImMovement開始=======");

    	String errorMsg = null;
    	String organizationCode = UserUtils.getOrganizationCodeByBrandCode(imMovementHead.getBrandCode());
    	String brandCode = cmMovementHead.getBrandCode();
    	String identification = MessageStatus.getIdentification(brandCode, cmMovementHead.getOrderTypeCode(), cmMovementHead.getOrderNo());
    	//==============更新調撥單=====================
    	imMovementHead.setStatus(OrderStatus.FINISH);
//  	imMovementHead.setCmMovementNo( cmMovementHead.getOrderNo() ); 移到狀態是簽核中就寫入
    	imMovementHead.setLastUpdateDate( new Date() );
    	imMovementHead.setLastUpdatedBy( employeeCode );
    	imMovementHead.setArrivalDate(cmMovementHead.getDeliveryDate());  // 移倉日期寫入調撥單轉入日期

    	imMovementHeadDAO.update( imMovementHead );
    	//==============相同的商品、庫別、批號產生產生集合、相同的報單號碼、報單項次、商品、關別集合============
		Set[] aggregateResult = aggregateOrderItemsQty(cmMovementHead, imMovementHead, employeeCode);
		Iterator imIt = aggregateResult[0].iterator(); //ImOnHand扣庫存用
		Iterator cmIt = aggregateResult[1].iterator(); //CmOnHand扣庫存用
		//======================================存取報單庫存量=======================================================
		while(cmIt.hasNext()){
			try{
				Map.Entry cmEntry = (Map.Entry) cmIt.next();
				Double moveUnCommitQty = (Double) cmEntry.getValue();
				String[] cmkeyArray = StringUtils.delimitedListToStringArray((String)cmEntry.getKey(), "{$}");
				cmDeclarationOnHandDAO.updateMoveUncommitQuantity(cmkeyArray[0], Long.valueOf(cmkeyArray[1]), cmkeyArray[2], cmkeyArray[3],
						brandCode, moveUnCommitQty, employeeCode);
			}catch(Exception ex){
				errorMsg = identification + "的報單庫存量時發生錯誤，原因：";
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg + ex.getMessage(), employeeCode);
				errorMsgs.add(errorMsg);
			}
		}
		//======================================存取實體庫別庫存量=======================================================
		while (imIt.hasNext()) {
			try{
				Map.Entry entry = (Map.Entry) imIt.next();
				Double moveUnCommitQty = (Double) entry.getValue();
				String[] keyArray = StringUtils.delimitedListToStringArray((String) entry.getKey(), "{$}");
				imOnHandDAO.updateMoveUncommitQuantity(organizationCode, keyArray[0], keyArray[1], keyArray[2], moveUnCommitQty, employeeCode, brandCode);
			}catch(Exception ex){
				errorMsg = identification + "庫別庫存量時發生錯誤，原因：";
				log.error(errorMsg + ex.toString());
				siProgramLogAction.createProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification, errorMsg + ex.getMessage(), employeeCode);
				errorMsgs.add(errorMsg);
			}
		}

		log.info( "======updateImMovement結束=======");
    }

    /**
     * 更新庫存(cm_on_hand, im_on_hand)
     * @param organizationCode
     * @param quantity
     * @param imMovementItem
     * @param brandCode
     * @param loginUser
     * @param cmMoveWarehouseTo
     * @throws FormException
     */
    private void updateStock(String organizationCode, Double quantity, ImMovementItem imMovementItem, String brandCode, String loginUser, String cmMoveWarehouseTo, String taxType )
    throws FormException {
	log.info( "======updateStock開始=======");
	// for cm_on_hand
	String declarationNo = imMovementItem.getOriginalDeclarationNo();
	Long declarationSeq = imMovementItem.getOriginalDeclarationSeq();
	String customsItemCode = imMovementItem.getItemCode();

	// for im_on_hand
	String lotNo = imMovementItem.getLotNo();
	String warehouseCode = imMovementItem.getArrivalWarehouseCode();

	log.info("cm_on_hand 開始");
	log.info("declarationNo = " + declarationNo );
	log.info("declarationSeq = " + declarationSeq );
	log.info("customsItemCode = " + customsItemCode );
	log.info("cmMoveWarehouseTo = " + cmMoveWarehouseTo );
	log.info("brandCode = " + brandCode );
	log.info("quantity = " + quantity );
	log.info("loginUser = " + loginUser );
	// cm_on_hand 加庫存
	if("F".equals(taxType)){
	    cmDeclarationOnHandDAO.updateMoveUncommitQuantity(declarationNo, declarationSeq,
		    customsItemCode, cmMoveWarehouseTo, brandCode, quantity, loginUser );
	}
	log.info("cm_on_hand 結束");

	log.info("im_on_hand 開始");
	log.info("organizationCode = " + organizationCode );
	log.info("ItemCode = " + imMovementItem.getItemCode() );
	log.info("warehouseCode = " + warehouseCode );
	log.info("lotNo = " + lotNo );
	log.info("quantity = " + quantity );
	log.info("loginUser = " + loginUser );
	// im_on_hand 加庫存
	imOnHandDAO.updateMoveUncommitQuantity( organizationCode, imMovementItem.getItemCode(),
		warehouseCode, lotNo, quantity, loginUser, brandCode);
	log.info("im_on_hand 結束");

	log.info( "======updateStock結束=======");
    }

    /**
     * 檢核移倉單主檔,明細檔
     * @param Head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validateCmMovement(CmMovementHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	validteHead(head, programId, identification, errorMsgs, formLinkBean);
	validteLine(head, programId, identification, errorMsgs);
    }

    /**
     * 檢核移倉單主檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteHead(CmMovementHead head, String programId, String identification, List errorMsgs, Object formLinkBean) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "主檔資料";
	try {
	    Date deliveryDate = head.getDeliveryDate();
	    String passNo = head.getPassNo();
	    String customsArea = head.getCustomsArea();
	    String applicantCode = head.getApplicantCode();
	    String sealType = head.getSealType();
	    String sealNo = head.getSealNo();
	    String deliverymanCode = head.getDeliverymanCode();
	    String carType = head.getCarType();
	    String carNo = head.getCarNo();
	    String driverCode = head.getDriverCode();
	    String expenseType = head.getExpenseType();
	    String expenseNo = head.getExpenseNo();
//	    String expenseAmount = (String)PropertyUtils.getProperty(formLinkBean, "expenseAmount");
	    String expenseAmount = String.valueOf( NumberUtils.getLong(head.getExpenseAmount()) );

	    boolean isF = "F".equals(head.getTaxType()) ? true : false;

	    if( deliveryDate == null ){
		message = "請輸入" + tabName + "的移倉日期！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);

	    }
	    if( !StringUtils.hasText(customsArea) && true == isF  ){
		message = "請輸入" + tabName + "的關別！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if(!StringUtils.hasText(passNo) && true == isF ){
		message = "請輸入" + tabName + "的放行單號！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(applicantCode) ){
		message = "請輸入" + tabName + "的申請人！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else{
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(head.getBrandCode(), applicantCode);
		if(employeeWithAddressView == null){
		    message = "查無" + tabName + "的申請人！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	    if( !StringUtils.hasText(sealType) && true == isF ){
		message = "請輸入" + tabName + "的封條類別！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(sealNo) && true == isF ){
		message = "請輸入" + tabName + "的封條號碼！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(deliverymanCode) ){
		message = "請輸入" + tabName + "的主管！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else{
		BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(head.getBrandCode(), deliverymanCode);
		if(employeeWithAddressView == null){
		    message = "查無" + tabName + "的主管！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	    }

	    if( !StringUtils.hasText(carType) && true == isF ){
		message = "請輸入" + tabName + "的保卡類別！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(carNo) && true == isF ){
		message = "請輸入" + tabName + "的保卡號碼！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(driverCode) ){
		message = "請輸入" + tabName + "的司機人！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }

	    if( !StringUtils.hasText(expenseType) && true == isF ){
		message = "請輸入" + tabName + "的規費類型！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(expenseNo) && true == isF ){
		message = "請輸入" + tabName + "的規費單號！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    if( !StringUtils.hasText(expenseAmount) && true == isF ){
		message = "請輸入" + tabName + "的規費金額！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else if( !ValidateUtil.isNumber(expenseAmount) && true == isF ){
		message = tabName + "的規費金額必須為整數！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }else{
		head.setExpenseAmount(NumberUtils.getLong(expenseAmount));
	    }

	} catch (Exception e) {
	    message = "檢核移倉單主檔單" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }

    /**
     * 檢核移倉單明細檔
     * @param head
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void validteLine(CmMovementHead head, String programId,
	    String identification, List errorMsgs) throws ValidationErrorException, NoSuchObjectException {
	String message = null;
	String tabName = "明細資料頁籤";
	Map orderNoMap = new HashMap();
	String brandCode = null;
	try{
	    String deliveryCustomsWarehouse = head.getDeliveryCustomsWarehouse();
	    String arrivalCustomsWarehouse = head.getArrivalCustomsWarehouse();
	    brandCode = head.getBrandCode();
	    List lines = head.getCmMovementLines();
	    int size = lines.size();
	    if( size > 0 ){
		for (int i = 0; i < size; i++) {
		    CmMovementLine line = (CmMovementLine) lines.get(i);
		    String orderNo = line.getOrderNo();
		    String indexNo = String.valueOf( line.getIndexNo() );
		    String orderTypeCode = line.getOrderTypeCode();

		    if( orderNoMap.containsKey( orderTypeCode + orderNo ) ){
//			orderNoMap.put( indexNo+orderNo ,  indexNo );
			message = tabName + "中第" + (i + 1) + "項明細的調撥單號" + orderNo + "重複！";  // "與第" + (orderNoMap.get(orderNo))+
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    }else{
			orderNoMap.put( orderTypeCode + orderNo ,  indexNo );
		    }



		    if (!StringUtils.hasText(orderNo)) {
			message = "請輸入" + tabName + "中第" + (i + 1) + "項明細的調撥單號！";
			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		    } else {
			orderNo = orderNo.trim().toUpperCase();
			line.setOrderNo(orderNo);

			// 檢核移倉單號是否存在
			ImMovementHead imMovementHead = this.findOneImMovement(orderNo, brandCode, line.getOrderTypeCode(), head.getTaxType());

			if( imMovementHead == null ){
			    message = tabName + "中第" + (i + 1) + "項明細的調撥單號不存在！";
			    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
			    errorMsgs.add(message);
			    log.error(message);
			} else {

			    // 驗證移倉轉入日期與轉出日期一致
			    if(!head.getDeliveryDate().equals(imMovementHead.getDeliveryDate())){
				message = tabName + "中第" + (i + 1) + "項明細的調撥單號的轉出日期與移倉日期:" + head.getDeliveryDate() + "不符！";
				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				errorMsgs.add(message);
				log.error(message);
			    }

			    // 驗證是否出庫關別,入庫關別與單別的出庫關別,入庫關別一樣
			    ImWarehouse imWarehouse1 = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, imMovementHead.getDeliveryWarehouseCode(), null);
			    ImWarehouse imWarehouse2 = imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode, imMovementHead.getArrivalWarehouseCode(), null);
			    if( imWarehouse1 != null && imWarehouse2 != null  ){
				String deliveryCWC = imWarehouse1.getCustomsWarehouseCode();
				String arrivalCWC = imWarehouse2.getCustomsWarehouseCode();
                
				//若轉出庫為PGS99 則設定出關關別為PW Start  Steve  2014/2/20
				log.info("deliveryCustomsWarehouse:"+deliveryCustomsWarehouse);
                log.info("imMovementHead.getDeliveryWarehouseCode():"+imMovementHead.getDeliveryWarehouseCode());
                if(imMovementHead.getDeliveryWarehouseCode().equals("PGS99")){
                	deliveryCWC = "PW";
                }else if(imMovementHead.getDeliveryWarehouseCode().equals("V0500")){
                	deliveryCWC = "MT";
                }
				//若轉出庫為PGS99 則設定出關關別為PW Start  Steve  2014/2/20 End
				log.info("deliveryCWC:"+deliveryCWC+" "+"deliveryCustomsWarehouse:"+deliveryCustomsWarehouse);
				if( !(deliveryCWC.equals(deliveryCustomsWarehouse) && arrivalCWC.equals(arrivalCustomsWarehouse)) ){
				    message = tabName + "中第" + (i + 1) + "項明細的調撥單號"+ orderNo + "與 品牌" + brandCode + "的出入庫關別不符";
				    //+ "的出庫倉關別(" + deliveryCWC+ ")與入庫倉關別(" + arrivalCWC + ")與單別"
				    //		+ brandCode + "的出庫關別("+ deliveryCustomsWarehouse + ")與入庫關別("+ arrivalCustomsWarehouse + ")不一樣！";
				    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
				    errorMsgs.add(message);
				    log.error(message);
				}

			    }else{
				log.error(tabName + "中第" + (i + 1) + "項明細的調撥單號"+orderNo + " 出庫倉"+ imMovementHead.getDeliveryWarehouseCode()+" 或入庫倉"+imMovementHead.getArrivalWarehouseCode()+" 不存在");
			    }


			}
		    }

		}
	    }else{
		message = tabName + "中請至少輸入一筆移倉單明細！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	} catch (Exception e) {
	    message = "檢核移倉單明細檔" + tabName + "時發生錯誤，原因：" + e.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, head.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
    }


    /**
	 * 匯入移倉單明細
	 *
	 * @param headId
	 * @param cMmovementItems
	 * @throws Exception
	 */
	public void executeImportCmMovementLines(Long headId, List cmMovementLines) throws Exception {
		log.info("executeImportCmMovementLines..." + headId);
		log.info("executeImportCmMovementLines... size ::: " + cmMovementLines.size());
		List<CmMovementLine> items = new ArrayList(0);
		try {
			// deleteMovementItems(headId);

			CmMovementHead cmMovementHead = (CmMovementHead) cmMovementHeadDAO.findById("CmMovementHead", headId);
			if (cmMovementHead == null)
				throw new NoSuchObjectException("查無移倉單主鍵：" + headId + "的資料");

			log.info("deleteCmMovementLines..size" + cmMovementHead.getCmMovementLines().size());
			if (cmMovementLines != null && cmMovementLines.size() > 0) {
				for (int i = 0; i < cmMovementLines.size(); i++) {
					CmMovementLine cmMovementLine = (CmMovementLine) cmMovementLines.get(i);
					cmMovementLine.setIndexNo(i + 1L);
					items.add(cmMovementLine);
				}
				cmMovementHead.setCmMovementLines(items);
			} else {
				cmMovementHead.setCmMovementLines(new ArrayList(0));
			}
			cmMovementHeadDAO.update(cmMovementHead);
		} catch (NoSuchObjectException ns) {
			log.error("刪除移倉單明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("移倉單明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("移倉單匯入時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 移倉單匯出明細 add by Weichun 2011.04.26
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public SelectDataInfo exportExcelDetail(HttpServletRequest httpRequest) throws Exception {
		try {

			Long headId = NumberUtils.getLong(httpRequest.getParameter("headId")); // 要顯示的HEAD_ID
			String brandCode = httpRequest.getParameter("brandCode"); // 品牌代號
			String status = httpRequest.getParameter("status"); // 單據狀態
			String taxType = httpRequest.getParameter("taxType"); // 單據狀態

			// 可用庫存excel表的欄位順序
			Object[] objectName, objectType = null;
			objectName = new Object[] { "indexNo", "orderTypeCode", "orderNo", "statusName", "deliveryWarehouseName",
					"arrivalWarehouseName", "imCreatedByName", "imCreationDate", "imLastUpdatedByName", "imLastUpdateDate" };
			objectType = new Object[] { Long.class, String.class, String.class, String.class, String.class, String.class,
					String.class, Date.class, String.class, Date.class };
			List<CmMovementLine> cmMovementLines = cmMovementLineDAO.findByProperty("CmMovementLine",
					" and cmMovementHead.headId = ? ORDER by indexNo ", new Object[] { headId });

			cmMovementLines = this.setLinesOtherColumn(brandCode, status, taxType, cmMovementLines);

			// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
			List rowData = new ArrayList();
			for (int i = 0; i < cmMovementLines.size(); i++) {
				CmMovementLine cmMovementLine = (CmMovementLine) cmMovementLines.get(i);
				Object[] dataObject = new Object[objectName.length];
				for (int j = 0; j < objectName.length; j++) {
					if (objectName[j] != null) {
						dataObject[j] = MethodUtils.invokeMethod(cmMovementLine, "get"
								+ ((String) objectName[j]).substring(0, 1).toUpperCase()
								+ ((String) objectName[j]).substring(1), null);
						//System.out.println( j + " ::: " + dataObject[j] );
					}
				}
				rowData.add(dataObject);
			}
			return new SelectDataInfo(objectName, rowData);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的移倉單明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的移倉單明細失敗！");
		}
	}
	
	/**
	 * 反轉
	 *
	 * @param headId
	 * @param owner
	 * @return
	 * @throws Exception
	 */
	public Object executeReverter(Long headId, String owner, Map parameterMap) throws Exception {
		log.info("reviveCmMovementFromClose...");
		//移倉單反確
		CmMovementHead cmMovementHead = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
		cmMovementHead.setStatus("SAVE");
		cmMovementHead.setCreatedBy(owner);
		cmMovementHead.setProcessId(null); // 反轉時，清空PROCESS_ID
		cmMovementHeadDAO.update(cmMovementHead);
		
		List<CmMovementLine> cmMovementLines = cmMovementHead.getCmMovementLines();
		log.info("cmMovementLines.size():"+cmMovementLines.size());
		Map parameterMapIM = parameterMap;
		Object formBindBean = parameterMapIM.get("vatBeanFormBind");
		PropertyUtils.setProperty(formBindBean, "typeCode", "IM");
		parameterMapIM.put("vatBeanFormBind", formBindBean);
		
		for(CmMovementLine cmMovementLine:cmMovementLines){
			ImMovementHead imMovementHead = imMovementHeadDAO.findMovementByIdentification(cmMovementHead.getBrandCode(), cmMovementLine.getOrderTypeCode(), cmMovementLine.getOrderNo());
			System.out.println("imMovementHead:"+imMovementHead.getHeadId()+","+owner+","+imMovementMainService);
			imMovementMainService.executeReverter(imMovementHead.getHeadId(),owner);
			reverterService.executeProcess(parameterMapIM, imMovementHead);
		}
		
		PropertyUtils.setProperty(formBindBean, "typeCode", "CM");
		parameterMapIM.put("vatBeanFormBind", formBindBean);
		return cmMovementHead;
		// return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	public void executeReverterProcess(Object bean) throws Exception {
		this.startProcess((CmMovementHead) bean);
	}
	
	public void callSoapJsp(Long headId, String DF, String orderTypeCod) throws Exception{
		HttpURLConnection connection = null;
		URL myUrl = new URL("http://10.1.171.147:8080/SOAP_Web/CMS.jsp");
        //取得 URLConnection    
        HttpURLConnection conn = (HttpURLConnection)myUrl.openConnection();
       
        conn.setDoInput(true);  //設定為可從伺服器讀取資料
        conn.setDoOutput(true);  //設定為可寫入資料至伺服器
        conn.setRequestMethod("GET");  //設定請求方式為 GET
        //以下是設定 MIME 標頭中的 Content-type
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded"); 
        conn.connect();  //開始連接
        //透過 URLConnection 的 getOutputStream() 取的 OutputStream, 並建立以UTF-8 為編碼的 OutputStreamWriter
        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"), true);
        //username和password為請求參數的名稱, args[0]和args[1]為參數值
        out.print("headId=" + headId +"&DFXX=" + DF +"&orderTypeCod=" + orderTypeCod);
        out.flush();   //記得要清除緩衝區或關閉
        
        //印出伺服器回應訊息
        System.out.println("Response status : " + conn.getResponseMessage());
        System.out.println("************************************");
        String inputLine = "";
        //利用 URLConnection 的 getInputStream() 取得 InputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        while ((inputLine = reader.readLine()) != null){
            System.out.println(inputLine);  //印出結果  
        }
        reader.close();
        conn.disconnect();
    }
	//by 20160414 jason  保卡
	 public List<Properties> changeTruckCode(Map parameterMap) throws Exception{
			log.info("11111ddd----changeTruckCode");
			Map returnMap = new HashMap(0);  
			try{
				Object otherBean  = parameterMap.get("vatBeanOther"); 
				String truckCode = (String)PropertyUtils.getProperty(otherBean, "carNo");
				String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
				String str = "保卡號碼";
				if(truckCode.length()>4 && truckCode.substring(0, 4).equals("保卡號碼")){	
				    	str = "保卡號碼";
					truckCode = truckCode.substring(4, truckCode.length());
				}
				else if((truckCode.length()>4 && truckCode.substring(0, 4).equals("保車號碼"))&& orderTypeCode.equals("RWD")){
				    	str = "保車號碼";
					truckCode = truckCode.substring(4, truckCode.length());
				}
					
				BuTruck buTruck = buTruckDAO.findByTruckCode(truckCode);
				returnMap.put("truckCode", str+truckCode);
				returnMap.put("truckNumber", buTruck.getTruckNumber());
				returnMap.put("truckDriver", buTruck.getTruckDriver());
				returnMap.put("truckDriverId", buTruck.getTruckDriverId());
				returnMap.put("freightName", buTruck.getFreightName());
				return AjaxUtils.parseReturnDataToJSON(returnMap);
			}catch(IllegalAccessException iae){
				System.out.println(iae.getMessage());
				throw new IllegalAccessException(iae.getMessage());
			}catch(InvocationTargetException ite){
				System.out.println(ite.getMessage());
				throw new InvocationTargetException(ite, ite.getMessage());
			}catch(NoSuchMethodException nse){
				System.out.println(nse.getMessage());
				throw new NoSuchMethodException(nse.getMessage());
			}
		} 
			
	 
	 public List<Properties> txtExport(Properties httpRequest) throws Exception {
		 List<Properties> result = new ArrayList();
		 Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
		 String brandCode = httpRequest.getProperty("brandCode");
		 try{
	    	System.out.println(httpRequest);
	    	CmMovementHead cmMovementHead = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
	    	List<CmMovementLine> items = cmMovementHead.getCmMovementLines();
	    	int indx = 1;
	    	FileWriter fw = new FileWriter(cmMovementHead.getOrderTypeCode()+cmMovementHead.getOrderNo()+".txt");
	    	fw.write("CG074;CDF01\r\n");
	    	for(CmMovementLine cm:items){
	    		ImMovementHead imMovementHead = imMovementHeadDAO.findMovementByIdentification(brandCode, cm.getOrderTypeCode(), cm.getOrderNo());
	    		List<ImMovementItem> imItems = imMovementHead.getImMovementItems();
	    		for(ImMovementItem imItem:imItems){
	    			String wr = "";
	    			wr+=indx;
	    			wr+=";";
	    			wr+=imItem.getItemCode();
	    			wr+=";";
	    			wr+=imItemDAO.findById(imItem.getItemCode()).getItemCName(); 
	    			wr+=";";
	    			wr+=imItem.getDeliveryQuantity();
	    			wr+=";";
	    			wr+=imItemDAO.findById(imItem.getItemCode()).getPurchaseUnit();
	    			wr+=";";
	    			fw.write(wr+"\r\n");
	    			indx++;
	    		}
	    	}
	    	fw.flush();
	    	fw.close();

	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return result;
	}
	 
	 public HashMap executeTxtExport(Long headId,String brandCode,String status) throws Exception {
		 HashMap essentialInfoMap = new HashMap();
		 List assembly = new ArrayList();
		 
		 try{
	    	CmMovementHead cmMovementHead = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, headId);
	    	List<CmMovementLine> items = cmMovementHead.getCmMovementLines();
	    	
	    	String customsCode = "";
	    	/*if(status.toUpperCase().equals("OUT")){
	    		status = "EX";
	    		customsCode = cmMovementHead.getDeliveryCustomsWarehouse();
	    	}else{
	    		status = "IM";
	    		customsCode = cmMovementHead.getArrivalCustomsWarehouse();
	    	}
	    	if(customsCode.toUpperCase().equals("PW")){
	    		customsCode = "CD198";
	    	}else if(customsCode.toUpperCase().equals("HD")){
	    		customsCode = "BDF06";
	    	}else if(customsCode.toUpperCase().equals("FD")){
	    		customsCode = "CDF05";
	    	}*/
	    	
	    	if(cmMovementHead.getDeliveryCustomsWarehouse().equals("PW")&&cmMovementHead.getArrivalCustomsWarehouse().equals("FD")){
	    		log.info("111");
	    		customsCode = "CDF05";
	    		status = "IM";
	    	}else if(cmMovementHead.getDeliveryCustomsWarehouse().equals("FD")&&cmMovementHead.getArrivalCustomsWarehouse().equals("PW")){
	    		log.info("222");
	    		customsCode = "CDF05";
	    		status = "EX";
	    	}else if(cmMovementHead.getDeliveryCustomsWarehouse().equals("PW")&&cmMovementHead.getArrivalCustomsWarehouse().equals("HD")){
	    		log.info("333");
	    		customsCode = "BDF06";
	    		status = "IM";
	    	}else if(cmMovementHead.getDeliveryCustomsWarehouse().equals("HD")&&cmMovementHead.getArrivalCustomsWarehouse().equals("PW")){
	    		log.info("444");
	    		customsCode = "BDF06";
	    		status = "EX";
	    	}
	    	
	    	String customsNo = "CD198;"+customsCode+";"+status+";";  //FileWriter fw = new FileWriter(cmMovementHead.getOrderTypeCode()+cmMovementHead.getOrderNo()+".txt");
	    	assembly.add(customsNo);  //fw.write("CG074;CDF01\r\n");
	    	for(CmMovementLine cm:items){
	    		int indx = 1;
	    		ImMovementHead imMovementHead = imMovementHeadDAO.findMovementByIdentification(brandCode, cm.getOrderTypeCode(), cm.getOrderNo());
	    		List<ImMovementItem> imItems = imMovementHead.getImMovementItems();
	    		for(ImMovementItem imItem:imItems){
	    			String rowData = "";
	    			rowData+= (imMovementHead.getOrderTypeCode() + imMovementHead.getOrderNo());
	    			rowData+=";";
	    			rowData+=indx;
	    			rowData+=";";
	    			rowData+=imItem.getItemCode();
	    			rowData+=";";
	    			rowData+=imItemDAO.findById(imItem.getItemCode()).getItemCName(); 
	    			rowData+=";";
	    			rowData+=imItem.getDeliveryQuantity().toString().substring(0,imItem.getDeliveryQuantity().toString().indexOf("."));
	    			rowData+=";";
	    			rowData+=imItemDAO.findById(imItem.getItemCode()).getPurchaseUnit();
	    			rowData+=";";
	    			rowData+="";
	    			rowData+=";";
	    			assembly.add(rowData);//fw.write(wr+"\r\n");
	    			indx++;
	    		}
	    	}
	    	
	    	essentialInfoMap.put("assembly", assembly);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return essentialInfoMap;
	}
	 
	 public List<Properties> executeModifyInitial(Map parameterMap) throws Exception {
			Map resultMap = new HashMap(0);
			Map multiList = new HashMap(0);
			try {
				Object otherBean = parameterMap.get("vatBeanOther");
				String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
				String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
				Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
				CmMovementHead form = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, formId);
				List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(loginBrandCode, "CM");
				multiList.put("allOrderTypes", AjaxUtils
						.produceSelectorData(allOrderTypes, "orderTypeCode", "name", true, false));
				if (null != form) {
					resultMap.put("orderTypeCode", form.getOrderTypeCode());
					resultMap.put("orderNo", form.getOrderNo());
					resultMap.put("moveWhNo", form.getMoveWhNo());
					
				}
				resultMap.put("multiList", multiList);

			} catch (Exception ex) {
				log.error("表單初始化失敗，原因：" + ex.toString());
				Map messageMap = new HashMap();
				messageMap.put("type", "ALERT");
				messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
				messageMap.put("event1", null);
				messageMap.put("event2", null);
				resultMap.put("vatMessage", messageMap);

			}

			return AjaxUtils.parseReturnDataToJSON(resultMap);

		}
	 
	 public HashMap updateReqNo(Map parameterMap) throws Exception {
			log.info("cmservice.updateReqNo...");
			HashMap resultMap = new HashMap(0);
			try {
				Object otherBean = parameterMap.get("vatBeanOther");
				String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				String formIdString = (String) PropertyUtils.getProperty(otherBean, "headId");
				Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
				String moveWhNo = (String) PropertyUtils.getProperty(otherBean, "moveWhNo");
				CmMovementHead form = (CmMovementHead)cmMovementHeadDAO.findByPrimaryKey(CmMovementHead.class, formId);
                
				if(form.getOrderTypeCode().equals("RHD")||form.getOrderTypeCode().equals("RDH")||form.getOrderTypeCode().equals("RHK")||form.getOrderTypeCode().equals("RKH")
				   ||form.getOrderTypeCode().equals("RHM")||form.getOrderTypeCode().equals("RMH")){
				  
				      if (null != form) {
					
						    form.setMoveWhNo(moveWhNo);
							form.setLastUpdatedBy(loginEmployeeCode);
							form.setLastUpdateDate(new Date());

							this.update(form);
					  } else {
					      throw new Exception("查無移倉單資料(" + formIdString + ")");
				      }
			    }else{
			    	Map messageMap = new HashMap();
					messageMap.put("type", "ALERT");
					messageMap.put("message", "此單別不可修改移倉申請書號碼");
					resultMap.put("vatMessage", messageMap);
			    	
			    }

			} catch (FormException ex) {
				throw new Exception(ex.toString());
			} 
			return resultMap;

		} 
	 
}
