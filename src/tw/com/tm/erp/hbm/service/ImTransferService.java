package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
//import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImTransfer;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PosItemEancode;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImTransferDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.constants.*;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.utils.ArraysUtils;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.ImTransationDAO;



/**
 * 採購預算 Head Service
 * 
 * @author Mac8
 * 
 */


public class ImTransferService {
	public static final String PROGRAM_ID = "IM_RECEIVE_HEAD";
	private static final Log log = LogFactory.getLog(ImTransferService.class);
	private BaseDAO baseDAO;
	private ImTransferDAO imTransferDAO;
	private BuBrandDAO buBrandDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private ImItemDAO imItemDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private ImWarehouseService imWarehouseService;
	private ImItemService imItemService;
	private SiProgramLogAction   siProgramLogAction;
	private ImOnHandDAO imOnHandDAO;
	private ImItemOnHandViewDAO imItemOnHandViewDAO;
	private BuOrderTypeService  buOrderTypeService ;
	private Long lineId;
	private NativeQueryDAO  nativeQueryDAO ;
	private ImTransationDAO imTransationDAO;


	public ImTransationDAO getImTransationDAO() {
		return imTransationDAO;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}

	public NativeQueryDAO getNativeQueryDAO() {
		return nativeQueryDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public BuOrderTypeService getBuOrderTypeService() {
		return buOrderTypeService;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public ImItemOnHandViewDAO getImItemOnHandViewDAO() {
		return imItemOnHandViewDAO;
	}

	public void setImItemOnHandViewDAO(ImItemOnHandViewDAO imItemOnHandViewDAO) {
		this.imItemOnHandViewDAO = imItemOnHandViewDAO;
	}

	public ImTransferDAO getImTransferDAO() {
		return imTransferDAO;
	}

	public ImOnHandDAO getImOnHandDAO() {
		return imOnHandDAO;
	}

	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public SiProgramLogAction getSiProgramLogAction() {
		return siProgramLogAction;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public ImItemService getImItemService() {
		return imItemService;
	}

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	public ImWarehouseService getImWarehouseService() {
		return imWarehouseService;
	}

	public void setImWarehouseService(ImWarehouseService imWarehouseService) {
		this.imWarehouseService = imWarehouseService;
	}

	public BuBrandDAO getBuBrandDAO() {
		return buBrandDAO;
	}

	public ImWarehouseDAO getImWarehouseDAO() {
		return imWarehouseDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public BuCommonPhraseLineDAO getBuCommonPhraseLineDAO() {
		return buCommonPhraseLineDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setImTransferDAO(ImTransferDAO imTransferDAO) {
		this.imTransferDAO = imTransferDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	/**
	 * 明細欄位
	 */


	public static final String[] GRID_SEARCH_COUNTRY_FIELD_NAMES = {"indexNo", 
		"applyDate","warehouseCodeApply", "itemCode","itemName",
		"quantityApply",
		"stockSalesqty","stockOnHandqty","unCommitqty",
		"warehouseCodeProcess1","quantityProcess1","warehouseCodeProcess2","quantityProcess2",	
		"reasonApply", "remark1", "orderNo","lineId" };

	public static final String[] GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES = { "", 
		"","","","",
		"",
		"","","",
		"","","","",
		"","","","" };

	public static final int[] GRID_FIELD_DEFAULT_TYPES1 = {
		AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG };

	/**
	 * -----------------------------------------------------------------------------------------
	 */
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "applyDate",
		"itemCode", "itemName", "quantityApply", "applyBy", "reasonApply",
		"remark1", "lineId",
		"brandCode","createdBy","creationDate",
		"lastUpdateBy","lastUpdateDate","orderTypeCode"
	};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "", "", "",
		"", "", "", "", "", 
	    "","","",
    	"","",""
	};

	public static final int[] GRID_FIELD_DEFAULT_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG ,
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,
		AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING
	};

	public ImTransfer executeNewImTransfer() throws Exception {
		ImTransfer form = new ImTransfer();
		form.setLastUpdateDate(new Date());
		return form;
	}
	
	
	
	

	
	
	

	/**
	 * 前台使用新增一筆資料------勿動
	 */
	
	public List<Properties> executeInitial_transfer(Map parameterMap)
	throws Exception {
		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String applyBy = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			List<ImWarehouse> allWarehouse = new ArrayList();
			String warehouseCodeApply = (String) PropertyUtils.getProperty(otherBean,"warehouseCodeApply");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String createdBy= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String lastUpdateBy= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			allWarehouse = imWarehouseService.getWarehouseByWarehouseEmployee(loginBrandCode ,applyBy, null);
			List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseLineDAO.findEnableLineById("Apply_Reason");
			multiList.put("allReasons", AjaxUtils.produceSelectorData(buCommonPhraseLines, "lineCode", "name", true, true));
			multiList.put("warehouseCodeApply",AjaxUtils.produceSelectorData(allWarehouse,"warehouseCode","warehouseName", true,  true));
			
			
			// 登入人員
			// ================================================================
			
			returnMap.put("date", new Date());
			returnMap.put("warehouseCodeApply", warehouseCodeApply);
			returnMap.put("allWarehouse",AjaxUtils.produceSelectorData( allWarehouse,"warehouseCode","warehouseName", true,  true));
			returnMap.put("applyBy", applyBy);
			returnMap.put("loginEmployeeName", UserUtils.getUsernameByEmployeeCode(applyBy));
			returnMap.put("brandCode", brandCode);			
			returnMap.put("createdBy", createdBy);
			returnMap.put("lastUpdateBy", lastUpdateBy);
			
			
			// =================================================================
			returnMap.put("multiList", multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("追加單前台初始化失敗，原因：" + ex.toString());
			throw new Exception("追加單前台初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 前台使用新增一筆資料------勿動
	 * 
	 * @param parameterMap
	 * @return
	 */
	public Map updateAJAXImTransfer1(Map parameterMap) throws Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");			
			String employeeCode = (String) PropertyUtils.getProperty(otherBean,	"loginEmployeeCode");
			String brandCode     = (String) PropertyUtils.getProperty(formBindBean, "loginBrandCode");
			String warehouseCodeApply = (String) PropertyUtils.getProperty(formBindBean, "warehouseCodeApply");
			String itemCode = (String) PropertyUtils.getProperty(formBindBean, "itemCode");
			String quantityApply = (String) PropertyUtils.getProperty(formBindBean, "quantityApply");
			System.out.println("warehouseCodeApply : " + warehouseCodeApply);
			
			//檢查品號不可為空白
			if(!StringUtils.hasText(warehouseCodeApply))
				throw new Exception("店號不可為空白,請重新檢查店號!!");
			if(!StringUtils.hasText(itemCode))
				throw new Exception("品號不可為空白,請重新輸入品號!!");
			ImItem imItem = imItemService.findItem(brandCode, itemCode);
			if(null == imItem){
				throw new Exception("品號不存在,請重新檢查品號!!!!");
			}
			//檢查數量不可為空白
			if(!StringUtils.hasText(quantityApply))
				throw new Exception("追加數量,不可為空白,只能為數字(1~999)!!");
			List<ImTransfer> imTransfers = imTransferDAO.findByProperty(
					"ImTransfer", new String[] { "warehouseCodeApply" },
					new Object[] { warehouseCodeApply });
			System.out.println("imTransfers : " + imTransfers.size());
			if(!StringUtils.hasText(employeeCode))
				throw new Exception("員工代號不存在!!");
			// ----------------------------------------------------------
			// 取得欲更新的bean
			ImTransfer imtransfer = new ImTransfer();
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imtransfer);
			imtransfer.setIndexNo(Long.valueOf(imTransfers.size() + 1));
			imtransfer.setOrderTypeCode("ITF");
			imTransferDAO.save(imtransfer);
			resultMap.put("entityBean", imtransfer);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Service1:追加單資料塞入存檔發生錯誤，原因：" + ex.toString());
			throw new Exception("Service1:追加單資料存檔發生錯誤，原因：" + ex.getMessage());
		}
	}
	/**
	 * 前台 第一次載入追加單明細時,取得分頁
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest)
	throws Exception {
		try {
			
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			String brandCode = httpRequest.getProperty("brandCode");
			String warehouseCodeApply = httpRequest.getProperty("warehouseCodeApply");
			System.out.println("warehouseCodeApply =========> "	+ warehouseCodeApply);
			if (!warehouseCodeApply.equals("") && warehouseCodeApply != null) {
				int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
				int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
				log.info("iSPage = " + iSPage);
				log.info("iPSize = " + iPSize);
				HashMap findObjs = new HashMap();
				findObjs.put(" and model.warehouseCodeApply = :warehouseCodeApply",warehouseCodeApply);
				findObjs.put(" and model.status = :status","SAVE");
				findObjs.put(" and model.applyDate >= :applyDate",DateUtils.addDays(new Date(), -14));
				Map searchMap = baseDAO.search("ImTransfer as model", findObjs,	"order by lineId desc", iSPage, iPSize,	BaseDAO.QUERY_SELECT_RANGE);
				List<ImTransfer> imTransfers = (List<ImTransfer>) searchMap.get(BaseDAO.TABLE_LIST);
				System.out.println("imTransfers ::: " + imTransfers.size()
						+ " 筆資料");
				//品號簡稱 =============================================================
				for (int i = 0; i < imTransfers.size(); i++) {
					ImTransfer imTransfer = (ImTransfer) imTransfers.get(i);
					imTransfer.setIndexNo(iSPage * iPSize + i + 1L);
					ImItem imItem = imItemDAO.findItem(brandCode, imTransfer.getItemCode());
					//追加原因--------------------------------------
					BuCommonPhraseLine lineCodeName = buCommonPhraseLineDAO.findById("Apply_Reason", imTransfer.getReasonApply());
					if (null !=lineCodeName)
						imTransfer.setReasonApply(lineCodeName.getName());
					log.info("imTransfer.indexNo = " + imTransfer.getIndexNo());
					//--------------------------------------------
					//品號簡稱
					if (null != imItem)
						imTransfer.setItemName(imItem.getItemCName());
					log.info("imTransfer.indexNo = " + imTransfer.getIndexNo());
					
					//取99庫存量
					Double quantity1 = imOnHandDAO.getCurrentStockOnHandQty("TM", imTransfer.getItemCode(), "T1BS99", SystemConfig.LOT_NO, brandCode);
					imTransfer.setWarehouseCodeProcess1(quantity1);
					log.info("99倉庫存" + quantity1);
				}
				// =============================================================
				HashMap map = new HashMap();
				map.put("warehouseCodeApply", warehouseCodeApply);
				if (imTransfers != null && imTransfers.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = imTransfers.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search(
							"ImTransfer as model",
							"count(model.warehouseCodeApply) as rowCount",
							findObjs, BaseDAO.QUERY_RECORD_COUNT).get(
									BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
					result.add(AjaxUtils.getAJAXPageDataByType(httpRequest,
							GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
							GRID_FIELD_DEFAULT_TYPES, imTransfers, gridDatas,
							firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
							GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
							gridDatas));
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的追加單明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的追加單明細失敗！");
		}
	}	
	//-------------------------------------------------	
	  public void updateimTransferBean(Map parameterMap) throws FormException,
	    Exception {
		try {
		    Object formBindBean = parameterMap.get("vatBeanFormBind");
		    ImTransfer imTransfer = new ImTransfer();
		    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imTransfer);
		    parameterMap.put("entityBean", imTransfer);
		} catch (FormException fe) {
		    log.error("前端資料塞入bean失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    ex.printStackTrace();
		    log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("國別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	    }	
//====================================================================================================================
//======後台修改下面==============================================================================================================

	public List<Properties> executeInitial_transfer1(Map parameterMap)
	throws Exception {
		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String applyBy = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			List<ImWarehouse> allWarehouse = new ArrayList();
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String lastUpdateBy= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String processBy= (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderNo = (String) PropertyUtils.getProperty(otherBean, "orderNo");
		    
			 
			
			
			allWarehouse = imWarehouseService.getWarehouseByWarehouseEmployee(loginBrandCode ,applyBy, null);
			List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseLineDAO.findEnableLineById("Apply_Reason");
			multiList.put("allReasons", AjaxUtils.produceSelectorData(buCommonPhraseLines, "lineCode", "name", true, true));
			multiList.put("warehouseCodeApply",AjaxUtils.produceSelectorData(allWarehouse,"warehouseCode","warehouseName", true,  true));
			// 登入人員
			// ================================================================
			
			returnMap.put("date", new Date());
			returnMap.put("allWarehouse",AjaxUtils.produceSelectorData( allWarehouse,"warehouseCode","warehouseName", true,  true));
			returnMap.put("applyBy", applyBy);
			returnMap.put("loginEmployeeName", UserUtils.getUsernameByEmployeeCode(applyBy));
			returnMap.put("brandCode", brandCode);
			returnMap.put("orderNo", orderNo);
			returnMap.put("lastUpdateBy", lastUpdateBy);
			returnMap.put("processBy", processBy);
			returnMap.put("multiList", multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("追加單後台初始化失敗，原因：" + ex.toString());
			throw new Exception("追加單後台初始化失敗，原因：" + ex.toString());
		}
	}
	public ImTransfer findLine(Long lineId) {
		log.info("findLine lineId=" + lineId);
		return imTransferDAO.findLine(lineId);
	}

	public ImTransfer getfindId(Long lineId) {
		log.info("findLine lineId=" + lineId);
		
		return imTransferDAO.findLine(lineId);
	}	
	
	public ImTransfer getorderNo(Long orderNo) {
		log.info("findLine orderNo=" + orderNo);
		
		return imTransferDAO.findLine(orderNo);
	}	
	
	
	
	
	public List<Properties> updateAJAXImTransferlist(Properties httpRequest) 	throws NumberFormatException, FormException, Exception {
		String gridData        = httpRequest.getProperty(AjaxUtils.GRID_DATA);
		int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
		int gridRowCount       = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
		String errorMsg        = null;
		
		List<Properties> upRecords = AjaxUtils.getGridFieldValue( gridData, gridLineFirstIndex, gridRowCount, GRID_SEARCH_COUNTRY_FIELD_NAMES);{
			try{
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA
					Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
					log.info("***04***" + upRecord.getProperty("lineId"));
					//  DB去撈取
					ImTransfer imTransfer = (ImTransfer)imTransferDAO.findById("ImTransfer",lineId);
					// 如果沒有ITEM CODE 就不會新增或修改
					if (null != imTransfer) {
						AjaxUtils.setPojoProperties(imTransfer, upRecord, GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_FIELD_DEFAULT_TYPES1);
						imTransferDAO.update(imTransfer);	// UPDATE 
					} else {
					}
				}
			}catch (Exception ex) { 		
				log.error("updateAJAXImTransferlist ：" + ex.toString());
				throw new Exception("updateAJAXImTransferlist ：" + ex.toString());
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		}
	}
	/**
	 * 後台查詢頁面
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXImTransferSearchPageData(Properties httpRequest)
	throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			String brandCode = httpRequest.getProperty("loginBrandCode");
			String warehouseCodeApply = httpRequest.getProperty("warehouseCodeApply");
			String itemCode = httpRequest.getProperty("itemCode");
			String orderNo = httpRequest.getProperty("orderNo");	 
			String status = httpRequest.getProperty("status");
			String formId = httpRequest.getProperty("formId");
			
			
			System.out.println("選擇單號=" + httpRequest.getProperty("orderNo") + "選擇庫別=" + httpRequest.getProperty("warehouseCodeApply") + "單據狀態=" +  httpRequest.getProperty("status")  + "備註=" +  httpRequest.getProperty("reasonApply"));
			
			System.out.println("warehouseCodeApply =========> "+ warehouseCodeApply);
			
			//if (!warehouseCodeApply.equals("") && warehouseCodeApply != null) {
			
			
				int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
				int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
				log.info("iSPage = " + iSPage);
				log.info("iPSize = " + iPSize);
				
				HashMap findObjs = new HashMap();
				
				
				
					if(!StringUtils.hasText(orderNo)&& !StringUtils.hasText(formId)) {

					findObjs.put(" and model.applyDate >= :applyDate",DateUtils.addDays(new Date(), -7));
					findObjs.put(" and model.warehouseCodeApply LIKE :warehouseCodeApply",warehouseCodeApply);
					//findObjs.put(" and model.orderNo LIKE :orderNo",orderNo);
					findObjs.put(" and model.status = :status","SAVE");
				}
				else
				{
					findObjs.put(" and model.orderNo = :orderNo",orderNo);
					findObjs.put(" and model.lineId = :formId",formId);
					findObjs.put(" and model.status = :status","FINISH");

				}

				
				
				Map searchMap = baseDAO.search("ImTransfer as model", findObjs,"order by lineId desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
				log.info("查詢資料:" + searchMap );
				
				List<ImTransfer> imTransfers = (List<ImTransfer>) searchMap.get(BaseDAO.TABLE_LIST);
				log.info("查詢資料:" + searchMap );
				
			
				
				
				System.out.println("imTransfers : " + imTransfers.size()+ " 筆資料");
				//品號簡稱 =============================================================
				for (int i = 0; i < imTransfers.size(); i++) {
					ImTransfer imTransfer = (ImTransfer) imTransfers.get(i);
					imTransfer.setIndexNo(iSPage * iPSize + i + 1L);
					//檢查品號是否存在
					ImItem imItem = imItemDAO.findItem(brandCode, imTransfer.getItemCode());
					
					
					
					
					//追加原因--------------------------------------
					BuCommonPhraseLine lineCodeName = buCommonPhraseLineDAO.findById("Apply_Reason", imTransfer.getReasonApply());
					
					
					if (null !=lineCodeName)
						imTransfer.setReasonApply(lineCodeName.getName());
					log.info("imTransfer.indexNo = " + imTransfer.getIndexNo());
					//--------------------------------------------
					//品號簡稱
					if (null != imItem)
						imTransfer.setItemName(imItem.getItemCName());
					log.info("imTransfer.indexNo = " + imTransfer.getIndexNo());
					//log.info("***" + imTransfer.getWarehouseCodeApply()+ ":" + imTransfer.getItemCode() + ":" + quantity + "***" );
					//Double quantity = imOnHandDAO.getCurrentStockOnHandQty("TM", itemCode, imTransfer.getWarehouseCodeApply(), SystemConfig.LOT_NO, brandCode);
					
					//歷史銷售量
					String tmpSqlItem = "select i.brand_code, i.item_code, i.warehouse_code, sum(i.quantity) " +
					" from im_transation i "+
                    " where		"+
                    " i.order_type_code='IOP' "+
                    " and i.warehouse_code='" + imTransfer.getWarehouseCodeApply() + "'" +
                    " and i.item_code='" + imTransfer.getItemCode() + "'" + 
                    " group by  i.brand_code,i.item_code,i.warehouse_code ";
					Double stockSalesqty = 0D;
                    List itemList = nativeQueryDAO.executeNativeSql(tmpSqlItem);
                    if(null != itemList && itemList.size() > 0){
                    	Object[] obj = (Object[]) itemList.get(0);
                    	stockSalesqty = ((BigDecimal) obj[3]).doubleValue();
                    }
                      imTransfer.setStockSalesqty(stockSalesqty);
                    //目前店庫存
					Double quantity = imOnHandDAO.getCurrentStockOnHandQty("TM", imTransfer.getItemCode(), imTransfer.getWarehouseCodeApply(), SystemConfig.LOT_NO, brandCode);
					imTransfer.setStockOnHandqty(quantity);
					//目前99倉庫存
					Double quantity1 = imOnHandDAO.getCurrentStockOnHandQty("TM", imTransfer.getItemCode(), "T1BS99", SystemConfig.LOT_NO, brandCode);
					imTransfer.setWarehouseCodeProcess1(quantity1);
					//目前FG倉庫存
					Double quantity2 = imOnHandDAO.getCurrentStockOnHandQty("TM", imTransfer.getItemCode(), "T1BSFG", SystemConfig.LOT_NO, brandCode);
					imTransfer.setWarehouseCodeProcess2(quantity2);
					//目前WAIT_IN在途量
					String tmpSqlItem1 = "SELECT I.ITEM_CODE,SUM(I.ARRIVAL_QUANTITY) " +
					" FROM IM_MOVEMENT_HEAD H,IM_MOVEMENT_ITEM I " +
					" WHERE H.HEAD_ID = I.HEAD_ID "+
					" AND H.BRAND_CODE='T1BS' "+
					" AND H.STATUS='WAIT_IN' "+
					" AND H.ARRIVAL_WAREHOUSE_CODE='" + imTransfer.getWarehouseCodeApply() + "'" +
					" AND I.ITEM_CODE='" + imTransfer.getItemCode() + "'" +	
					" GROUP BY I.ITEM_CODE ";
					Double unCommitqty = 0D;
                    List itemList1 = nativeQueryDAO.executeNativeSql(tmpSqlItem1);
                    if(null != itemList1 && itemList1.size() > 0){
                    	Object[] obj = (Object[]) itemList1.get(0);
                    	unCommitqty = ((BigDecimal) obj[1]).doubleValue();
                    }
                      imTransfer.setUnCommitqty(unCommitqty);
                      log.info("在途量 = " + unCommitqty);
				}
				// =============================================================
				HashMap map = new HashMap();
				map.put("warehouseCodeApply", warehouseCodeApply);
				if (imTransfers != null && imTransfers.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = imTransfers.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = (Long) baseDAO.search("ImTransfer as model","count(model.warehouseCodeApply) as rowCount",findObjs, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
					result.add(AjaxUtils.getAJAXPageDataByType(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES,GRID_FIELD_DEFAULT_TYPES1, imTransfers, gridDatas,firstIndex, maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES, map,gridDatas));
				}
			//}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入後台頁面顯示的追加單明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入後台頁面顯示的追加單明細失敗！");
		}
	}
	
		//----後台專用---------------------------------------------		  
	public Map updateAJAXimTransfer(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;

		
		
		List<ImTransfer> imTransfers = baseDAO.findByProperty("ImTransfer", " and quantityProcess1 is not null and status = ? " , new Object[]{"SAVE"});
		String serialNo = buOrderTypeService.getOrderSerialNo(((ImTransfer)(imTransfers.get(0))).getBrandCode(), ((ImTransfer)(imTransfers.get(0))).getOrderTypeCode());
		log.info("單號:" + serialNo);
		
		for (Iterator iterator = imTransfers.iterator(); iterator.hasNext();) {
			ImTransfer imTransfer = (ImTransfer) iterator.next();
			
			log.info("imTransfer = " + imTransfer.getItemCode() + "核準量: = " 					   
					   + imTransfer.getQuantityProcess1() 
					   + "庫別: = "
					   + imTransfer.getWarehouseCodeApply()
					   +  "狀態: = "  
					   + imTransfer.getStatus() +  "lean_id: = "  + imTransfer.getLineId()
					   + "品牌:" + imTransfer.getBrandCode() 
					   + "單別:" + imTransfer.getOrderTypeCode() 	   
			         );
			
						imTransfer.setOrderTypeCode("ITF");
						imTransfer.setOrderNo(serialNo);				
						imTransfer.setStatus("FINISH");
						imTransfer.setLastUpdateDate(new Date());
						imTransfer.setProcessDate(new Date());						
						imTransferDAO.save(imTransfer);
		}
		try {
		    resultMsg = "imTransfer Name：" +  "存檔成功！ 是否繼續新增？";
		    
		    resultMap.put("resultMsg", resultMsg);
		    resultMap.put("entityBean", imTransfers);
		    resultMap.put("vatMessage", msgBox);
		    resultMap.put("orderNo",serialNo);
		    resultMap.put("Status","FINISH");
		    resultMap.put("orderType","ITF");
		    
		    
		    return resultMap;
		} catch (Exception ex) {
		    log.error("追加單存檔時發生錯誤，原因：" + ex.toString());
		    throw new Exception("追加單存檔失敗，原因：" + ex.toString());
		}
	    }
	
	    public Map updateCheckimTransfer(Map parameterMap) throws Exception {
		Map resultMap = new HashMap();
		try {
		    Object formBindBean = parameterMap.get("vatBeanFormBind");
		    List<ImTransfer> ImTransfer = imTransferDAO.findByProperty(
			    "ImTransfer", new String[] { "lineId" },
			    new Object[] { lineId });
		    System.out.println("ImTransfer : " + ImTransfer.size());
		    // 取得欲更新的bean
		    ImTransfer imTransfer = new ImTransfer();
		    AjaxUtils.copyJSONBeantoPojoBean(formBindBean,imTransfer);
		    resultMap.put("entityBean", lineId);
		    return resultMap;
		} catch (Exception ex) {
		    ex.printStackTrace();
		    log.error("追加單資料塞入bean發生錯誤，原因：" + ex.toString());
		    throw new Exception("公司資料存檔發生錯誤，原因：" + ex.getMessage());
		}
	    }	
	
	    /**------------報表------------------------------------------------------------------------
		 * 取得CC開窗URL字串
		 *
		 * @param parameterMap
		 * @return
		 * @throws Exception
		 */
		public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
			try{
				Object otherBean = parameterMap.get("vatBeanOther");
				String brandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
				String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
				String orderNo = (String)PropertyUtils.getProperty(otherBean, "orderNo");
				String reportFileName = (String)PropertyUtils.getProperty(otherBean, "reportFileName");
				String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				//String status = (String)PropertyUtils.getProperty(otherBean, "status");
				log.info("品牌:" + brandCode);
				log.info("單別:" + orderTypeCode);
				log.info("單別:" + orderNo);
				log.info("loginEmployeeCode:" + loginEmployeeCode);					
				log.info("報表名稱:" + reportFileName);
				
				System.out.println("報表單號-------- " + orderNo);
				Map returnMap = new HashMap(0);
				Map parameters = new HashMap(0);
				parameters.put("prompt0", brandCode);
				parameters.put("prompt1", orderTypeCode);
				parameters.put("prompt2", orderNo);
				//parameters.put("prompt3", status);
				String reportUrl = new String("");
				try{
					if(StringUtils.hasText(reportFileName))
						reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, reportFileName, parameters);
					else
						reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
				}catch(Exception ex){
					ex.printStackTrace();
					throw ex;
					//reportUrl = SystemConfig.getReportURL(brandCode, orderTypeCode, loginEmployeeCode, parameters);
				}
				returnMap.put("reportUrl", reportUrl);
				log.info("報表路徑:" + reportUrl);
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
//--------------------------------------------------	
	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
//**------------流程------------------------------------------------------------------------
	

	  /** 啟始流程
     * @param form
     * @return
     * @throws ProcessFailedException
     */
    public static Object[] startProcess(ImTransfer imTransfer) throws ProcessFailedException{       
        try{           
            String packageId = "Im_Transfer";
            String processId = "approval";  
            String sourceReferenceType = "Im_Transfer";
            String version   = "0102";
            
            
            //--黃點
            HashMap context = new HashMap();
            context.put("brandCode", imTransfer.getBrandCode());
            context.put("orderType", imTransfer.getOrderTypeCode());
            context.put("orderNo",   imTransfer.getOrderNo() );
            context.put("formId", imTransfer.getLineId());
           
            System.out.println("formId ::: " + imTransfer.getLineId() );
            return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
        }catch (Exception ex){
            log.error("進貨單流程啟動失敗，原因：" + ex.toString());
            ex.printStackTrace();
            throw new ProcessFailedException("進貨單流程啟動失敗！");
	}
    }
	
    public static Object[] completeAssignment(long assignmentId, boolean approveResult, ImTransfer imTransfer) 
	throws ProcessFailedException{
	try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	    context.put("form",imTransfer);
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成進貨單工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成進貨單工作任務失敗！");
	}
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
	    
	    System.out.println("=======assignmentId:"+assignmentId);

	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成移倉工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成移倉工作任務失敗！"+ ex.getMessage());
	}
    }
}






