package tw.com.tm.erp.hbm.service;

import java.text.ParseException;
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
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.CmTransfer;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.CmTransferDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.UserUtils;

/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA : Weichun.Liao
 * PG : Weichun.Liao
 * Filename : CmTransferService.java
 * Function : 貨櫃（物）運送單
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	2011/4/6		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
public class CmTransferService {

	private static final Log log = LogFactory.getLog(CmTransferService.class);
	public static final String PROGRAM_ID = "CM_TRANSFER";

	/**
	 * spring Ioc
	 */
	private BaseDAO baseDAO;
	private BuBrandDAO buBrandDAO;
	private BuOrderTypeDAO buOrderTypeDAO;
	private ImWarehouseDAO imWarehouseDAO;
	private BuOrderTypeService buOrderTypeService;
	private CmMovementService cmMovementService;

	private SiProgramLogAction siProgramLogAction;
	private CmTransferDAO cmTransferDAO;

	/**
	 * 運送單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "transferOrderNo", "transfer", "startStation", "leaveTime",
			"toStation", "statusName", "lastUpdateDate", "transferOrderNo" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "" };

	public void setCmTransferDAO(CmTransferDAO cmTransferDAO) {
		this.cmTransferDAO = cmTransferDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
		this.buOrderTypeDAO = buOrderTypeDAO;
	}

	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setCmMovementService(CmMovementService cmMovementService) {
		this.cmMovementService = cmMovementService;
	}

	/**
	 * 運送單初始化欄位值
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

			CmTransfer cmTransfer = this.getActualMaster(otherBean, resultMap);

			Map multiList = new HashMap(0);
			resultMap.put("form", cmTransfer);
			resultMap.put("brandName", buBrandDAO.findById(loginBrandCode).getBrandName());
			resultMap.put("statusName", OrderStatus.getChineseWord(cmTransfer.getStatus()));

			// 顯示正確的建單人員
			String employeeName = "";
			if (cmTransfer.getCreatedBy() == null && !"".equals(cmTransfer.getCreatedBy()))
				employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
			else
				employeeName = UserUtils.getUsernameByEmployeeCode(cmTransfer.getCreatedBy());
			resultMap.put("createByName", employeeName);

			// 運送單資料
			if (cmTransfer != null) {
				if (cmTransfer.getTransferOrderNo() != null) {
					multiList.put("transferOrderNo", cmTransfer.getTransferOrderNo());
					multiList.put("transfer", cmTransfer.getTransfer());
					multiList.put("owner", cmTransfer.getOwner());
					multiList.put("startStation", cmTransfer.getStartStation());
					multiList.put("toStation", cmTransfer.getToStation());
					multiList.put("vehicleStation", cmTransfer.getVehicleStation());
					multiList.put("vehicleNo", cmTransfer.getVehicleNo());
					multiList.put("driverLicence", cmTransfer.getDriverLicence());
					multiList.put("track", cmTransfer.getTrack());

					// 初始化單據資料
					multiList.put("toStation", cmTransfer.getToStation());
					multiList.put("vehicleStation", cmTransfer.getVehicleStation());
					multiList.put("vehicleNo", cmTransfer.getVehicleNo());
					multiList.put("driverLicence", cmTransfer.getDriverLicence());
					multiList.put("track", cmTransfer.getTrack());

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
			log.error("運送單初始化失敗，原因：" + ex.toString());
			throw new Exception("運送單初始化失敗，原因：" + ex.toString());

		}
	}

	/**
	 * 依transferOrderNo取得實際運送單 in initial
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	private CmTransfer getActualMaster(Object otherBean, Map resultMap) throws Exception {
		CmTransfer cmTransfer = null;
		try {
			String OrderNo = (String) PropertyUtils.getProperty(otherBean, "formId");
			String orderTypeCode = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");
			String transferOrderNo = StringUtils.hasText(OrderNo) ? orderTypeCode + OrderNo : null;
			System.out.println("=========>" + transferOrderNo);
			cmTransfer = null == transferOrderNo ? this.executeNew(otherBean) : this.executeFind(transferOrderNo);

		} catch (Exception e) {
			log.error("取得實際移運送單檔失敗,原因:" + e.toString());
			throw new Exception("取得實際運送單失敗,原因:" + e.toString());
		}
		return cmTransfer;
	}

	/**
	 * 產生新的運送單
	 *
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	private CmTransfer executeNew(Object otherBean) throws Exception {
		CmTransfer cmTransfer = new CmTransfer();
		try {
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			cmTransfer.setStatus(OrderStatus.SAVE);
			cmTransfer.setCreatedBy(loginEmployeeCode);
			cmTransfer.setCreationDate(new Date());
			cmTransfer.setLastUpdatedBy(loginEmployeeCode);
			cmTransfer.setLastUpdateDate(new Date());

			this.saveTmpHead(cmTransfer);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立新運送單失敗,原因:" + e.toString());
			throw new Exception("建立新運送單失敗,原因:" + e.toString());
		}
		return cmTransfer;
	}

	/**
	 * 依據transferOrderNo為查詢條件，取得運送單
	 *
	 * @param transferOrderNo
	 * @return CmTransfer
	 * @throws Exception
	 */
	public CmTransfer executeFind(String transferOrderNo) throws Exception {

		try {
			CmTransfer cmTransfer = (CmTransfer) cmTransferDAO.findByPrimaryKey(CmTransfer.class, transferOrderNo);
			return cmTransfer;
		} catch (Exception ex) {
			log.error("依據運送單號：" + transferOrderNo + "查詢運送單時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據運送單號：" + transferOrderNo + "查詢運送單時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 運送單存檔,取得暫存碼
	 *
	 * @param CmTransfer
	 * @throws Exception
	 */
	private void saveTmpHead(CmTransfer cmTransfer) throws Exception {
		System.out.println("===== 儲存運送單 =====");
		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			cmTransfer.setTransferOrderNo(tmpOrderNo);
			baseDAO.save(cmTransfer);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("取得暫時單號儲存運送單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存運送單發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 透過transferOrderNo取得運送單
	 *
	 * @param bean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public CmTransfer getActualCmTransfer(Object formBean) throws FormException, Exception {

		CmTransfer cmTransfer = null;
		String transferOrderNo = (String) PropertyUtils.getProperty(formBean, "transferOrderNo");

		if (StringUtils.hasText(transferOrderNo)) {
			cmTransfer = (CmTransfer) cmTransferDAO.findByPrimaryKey(CmTransfer.class, transferOrderNo);
			if (cmTransfer == null) {
				throw new NoSuchObjectException("查無運送單主鍵：" + transferOrderNo + "的資料！");
			}
		} else {
			throw new ValidationErrorException("傳入的運送單主鍵為空值！");
		}
		return cmTransfer;

	}

	/**
	 * 前端資料塞入bean
	 *
	 * @param parameterMap
	 * @return
	 */
	public Map updateCmTransferBean(Map parameterMap) throws FormException, Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// 取得欲更新的bean
			CmTransfer cmTransfer = this.getActualCmTransfer(formBindBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, cmTransfer);
			resultMap.put("entityBean", cmTransfer);
			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("運送單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 檢核,存取運送單,重新設定狀態
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXCmTransfer(Map parameterMap, String formAction) throws Exception {
		Map resultMap = new HashMap();
		List errorMsgs = null;
		String resultMsg = null;
		try {

			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBean = parameterMap.get("vatBeanFormBind");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			CmTransfer cmTransfer = this.getActualCmTransfer(formBean);

			if (errorMsgs == null || errorMsgs.size() == 0) {
				// 刪除暫存的運送單
				if (cmTransfer.getTransferOrderNo().indexOf("TMP") > -1)
					cmTransferDAO.delete(cmTransfer);
				// 設定單號
				this.setOrderNo(cmTransfer);
				// 成功則設定下個狀態
				this.setNextStatus(cmTransfer, formAction);
				cmTransfer.setLastUpdatedBy(employeeCode);
				cmTransfer.setLastUpdateDate(new Date());
				resultMsg = cmTransfer.getTransferOrderNo() + "存檔成功！ 是否繼續新增？";

			} else if (errorMsgs.size() > 0) {
				if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
					throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
				}
			}
			resultMap.put("entityBean", cmTransfer);
			resultMap.put("resultMsg", resultMsg);
		} catch (ValidationErrorException ve) {
			log.error("運送單檢核時發生錯誤，原因：" + ve.toString());
			throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
		} catch (Exception e) {
			log.error("運送單存檔時發生錯誤，原因：" + e.toString());
			throw new Exception("運送單存檔時發生錯誤，原因：" + e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(CmTransfer cmTransfer) throws ObtainSerialNoFailedException {
		String transferOrderNo = cmTransfer.getTransferOrderNo();
		if (AjaxUtils.isTmpOrderNo(transferOrderNo)) {
			try {
				String orderNo = buOrderTypeService.getOrderNo("T2", "TMB", null, null);
				cmTransfer.setTransferOrderNo("TMB" + orderNo);
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得運送單單號失敗！");
			}
		}
	}

	/**
	 * 運送單依formAction取得下個狀態
	 *
	 * @param cmTransfer
	 * @param formAction
	 */
	public void setNextStatus(CmTransfer cmTransfer, String formAction) {

		if (OrderStatus.FORM_SAVE.equals(formAction)) {
			cmTransfer.setStatus(OrderStatus.SAVE);
		} else if (OrderStatus.FORM_SUBMIT.equals(formAction) || "SUBMIT_BG".equals(formAction)) {
			if (OrderStatus.SAVE.equals(cmTransfer.getStatus()) || OrderStatus.REJECT.equals(cmTransfer.getStatus())) {
				cmTransfer.setStatus(OrderStatus.FINISH);
			}
		} else if (OrderStatus.FORM_VOID.equals(formAction)) {
			cmTransfer.setStatus(OrderStatus.VOID);
		}

	}

	/**
	 * 更新bean
	 *
	 * @param head
	 */
	public void update(CmTransfer cmTransfer) {
		try {
			cmTransferDAO.saveOrUpdate(cmTransfer);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("e = " + e.toString());
		}
	}

	/**
	 * 啟動流程
	 *
	 * @param form
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] startProcess(CmTransfer form) throws ProcessFailedException {

		try {
			String packageId = "Cm_Movement";
			String processId = "cmTransfer";
			String version = "20110330";
			String sourceReferenceType = "ctProcess";
			HashMap context = new HashMap();
			context.put("formId", Long.parseLong(form.getTransferOrderNo().substring(3)));
			context.put("orderType", form.getTransferOrderNo().substring(0, 3));
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("運送單流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("運送單流程啟動失敗！" + ex.getMessage());
		}
	}

	/**
	 * 完成任務工作
	 *
	 * @param assignmentId
	 * @param approveResult
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException {

		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);

			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception ex) {
			log.error("完成工作任務失敗，原因：" + ex.toString());
			throw new ProcessFailedException("完成工作任務失敗！" + ex.getMessage());
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
			CmTransfer checkCmTransfer = cmTransferDAO.findById(cmTransfer.getTransferOrderNo());
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
			if (checkCmTransfer == null)
				cmTransferDAO.save(cmTransfer);
			else
				cmTransferDAO.merge(cmTransfer);
			return AjaxUtils.parseReturnDataToJSON(new HashMap());
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (ParseException pe) {
			pe.printStackTrace();
			log.error("時間格式輸入錯誤，請重新輸入！：" + pe.toString());
			throw new Exception("時間格式輸入錯誤，請重新輸入！：" + pe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("運送單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * ajax 取得運送單search分頁
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			String brandCode = httpRequest.getProperty("loginBrandCode");// 品牌代號
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 品牌代號

			String orderNo = httpRequest.getProperty("orderNo");
			Date deliveryDateStart = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("deliveryDateStart"));
			Date deliveryDateEnd = DateUtils.parseDate("yyyy/MM/dd", httpRequest.getProperty("deliveryDateEnd"));

			String status = httpRequest.getProperty("status");
			String transferOrderNo = orderTypeCode + orderNo; // 運送單號
			String orderBy = httpRequest.getProperty("orderBy") == null || "".equals(httpRequest.getProperty("orderBy")) ? "lastUpdateDate"
					: httpRequest.getProperty("orderBy");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			if (orderTypeCode == null || "".equals(orderTypeCode)) {
				findObjs.put(" and model.transferOrderNo NOT LIKE :TMP", "TMP%");
			} else {
				findObjs.put(" and model.transferOrderNo LIKE :orderTypeCode", orderTypeCode + "%");
			}
			if (orderNo != null && orderNo.length() > 0)
				findObjs.put(" and model.transferOrderNo = :transferOrderNo", transferOrderNo);
			findObjs.put(" and model.leaveTime >= :deliveryDateStart", deliveryDateStart);
			findObjs.put(" and model.leaveTime <= :deliveryDateEnd", deliveryDateEnd);
			findObjs.put(" and model.status = :status", status);

			Map cmTransferMap = cmTransferDAO.search("CmTransfer as model", findObjs, "order by " + orderBy + " desc",
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<CmTransfer> cmTransfers = (List<CmTransfer>) cmTransferMap.get(BaseDAO.TABLE_LIST);

			if (cmTransfers != null && cmTransfers.size() > 0) {
				this.setCmTransferStatusName(cmTransfers);

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) cmTransferDAO.search("CmTransfer as model",
						"count(model.transferOrderNo) as rowCount", findObjs, "order by " + orderBy + " desc",
						BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
				// INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						cmTransfers, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的運送單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的運送單查詢失敗！");
		}
	}

	/**
	 * 將運送單查詢結果存檔
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
		String errorMsg = null;
		System.out.println("====== 儲存明細資料 ======");
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
	public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception {
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
	 * 設定中文狀態名稱
	 *
	 * @param cmMovementHeads
	 */
	private void setCmTransferStatusName(List<CmTransfer> cmTransfers) {
		for (CmTransfer cmTransfer : cmTransfers) {
			cmTransfer.setStatusName(OrderStatus.getChineseWord(cmTransfer.getStatus()));
		}
	}

	public void executeReverterProcess(Object bean) throws Exception {
		this.startProcess((CmTransfer) bean);
	}

	/**
	 * 依據orderNo為查詢條件，取得移倉單主檔
	 *
	 * @param orderNo
	 * @return CmMovementHead
	 * @throws Exception
	 */
	public CmMovementHead executeFindCmMovement(String transferOrderNo) throws Exception {

		try {
			CmMovementHead head = (CmMovementHead) baseDAO.findFirstByProperty("CmMovementHead",
					" and transferOrderNo = ? ", new Object[] { transferOrderNo });
			return head;
		} catch (Exception ex) {
			log.error("依據運送單號：" + transferOrderNo + "查詢移倉單主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據運送單號：" + transferOrderNo + "查詢移倉單主檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

}
