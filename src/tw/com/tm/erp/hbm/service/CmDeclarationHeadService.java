package tw.com.tm.erp.hbm.service;

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
import tw.com.tm.erp.action.CmDeclarationAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.CmDeclarationContainer;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationLog;
import tw.com.tm.erp.hbm.bean.CmDeclarationVehicle;
import tw.com.tm.erp.hbm.bean.FiBudgetModHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImDistributionHead;
import tw.com.tm.erp.hbm.bean.ImDistributionItem;
import tw.com.tm.erp.hbm.bean.ImDistributionLine;
import tw.com.tm.erp.hbm.bean.ImDistributionShop;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationContainerDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationVehicleDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;

import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.WfApprovalResultUtils;

/**
 * 報單單頭 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class CmDeclarationHeadService {

	private static final Log log = LogFactory
			.getLog(CmDeclarationHeadService.class);
	private CmDeclarationHeadDAO cmDeclarationHeadDAO;
	private CmDeclarationItemDAO cmDeclarationItemDAO;
	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
	private CmDeclarationVehicleDAO cmDeclarationVehicleDAO;
	private CmDeclarationContainerDAO cmDeclarationContainerDAO;
	private SiProgramLogAction siProgramLogAction;
	private BuCommonPhraseService buCommonPhraseService;
	
	public static final String PROGRAM_ID = "CM_DECLARATION";
	
	/**
	 * 報單明細欄位
	 */
	public static final String[] T2_GRID_FIELD_NAMES = { 
		"indexNo", "itemNo", "buyCommNo", "descrip", "unit", 
		"spec", "brand", "model", "NWght", "qty", 
		"unitPrice", "ODeclNo", "OItemNo", "code", "produceCountry",
		"permitNo", "itemId" };
	
	public static final String[] T2_GRID_FIELD_DEFAULT_VALUES = { 
		"", "", "", "", "", 
		"", "", "", "", "", 
		"", "", "", "", "", 
		"", "" };
			

	public static final int[] T2_GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, 
		AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG };

	/**
	 * 車身號碼欄位
	 */
	public static final String[] T3_GRID_FIELD_NAMES = { "indexNo", "itemNo",
			"vehicleNo", "itemId" };

	public static final String[] T3_GRID_FIELD_DEFAULT_VALUES = { "", "", "",
			"" };

	public static final int[] T3_GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG 
	};

	/**
	 * 貨櫃資料欄位
	 */
	public static final String[] T4_GRID_FIELD_NAMES = { "indexNo", "contrNo",
			"contrType", "transMode", "itemId" };

	public static final String[] T4_GRID_FIELD_DEFAULT_VALUES = { "", "", "",
			"", "" };

	public static final int[] T4_GRID_FIELD_TYPES = {
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG 
	};

	/**
	 * 查詢欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "bondNo",
			"declNo", "importDate", "rlsTime", "stgPlace", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
			"", "", "", "" };

	/**
	 * executeInitial
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			HashMap argumentMap = getRequestParameter(parameterMap, false);
			Long formId = NumberUtils.getLong((Long) argumentMap.get("formId"));
			if(formId > 0)
				findCmDeclarationHead(argumentMap, resultMap);
			else{
				resultMap.put("statusName", OrderStatus.getChineseWord(OrderStatus.SAVE));
				resultMap.put("employeeName", UserUtils.getUsernameByEmployeeCode((String)argumentMap.get("loginEmployeeCode")));
				CmDeclarationHead form = new CmDeclarationHead();
				List <BuCommonPhraseLine> modifyTypes = buCommonPhraseService.findEnableLineById("ModifyTypes");
				multiList.put("modifyTypes" , AjaxUtils.produceSelectorData(modifyTypes  ,"lineCode" ,"name",  false,  true));
				resultMap.put("multiList",multiList);
				resultMap.put("form", form);
			}
			return resultMap;
		} catch (Exception ex) {
			log.error("盤點單初始化失敗，原因：" + ex.toString());
			throw new Exception("盤點單初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * create 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(CmDeclarationHead modifyObj) throws Exception {
		log.info("CmDeclarationHeadService.create");
		setDefault(modifyObj);
		if (null != modifyObj) {
			if (modifyObj.getHeadId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return MessageStatus.ERROR + " Can't find FiInvoiceHead";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(CmDeclarationHead saveObj) throws Exception {
		log.info("CmDeclarationHeadService save start " + saveObj);
		/*
		 * doAllValidate(saveObj);
		 * saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(),
		 * saveObj.getOrderTypeCode()));
		 * saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		 * saveObj.setLastUpdateDate(new Date()); saveObj.setCreationDate(new
		 * Date());
		 */
		cmDeclarationHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(CmDeclarationHead updateObj) throws Exception {
		log.info("CmDeclarationHeadService update start " + updateObj);
		/*
		 * doAllValidate(updateObj); updateObj.setLastUpdateDate(new Date());
		 */
		cmDeclarationHeadDAO.update(updateObj);
		cmDeclarationHeadDAO.deleteNullField("ERP.CM_DECLARATION_LOG", "IDENTIFY");
		return MessageStatus.SUCCESS;

	}

	/**
	 * 指定預設
	 * @param modifyObj
	 */
	private void setDefault(CmDeclarationHead modifyObj) {
		log.info("CmDeclarationHeadService.setDefault");
		List<CmDeclarationItem> cmDeclarationItems = modifyObj
				.getCmDeclarationItems();
		for (CmDeclarationItem cmDeclarationItem : cmDeclarationItems) {
			if (!StringUtils.hasText(cmDeclarationItem.getT2())) {
				cmDeclarationItem.setT2("T2");
			}
			if (!StringUtils.hasText(cmDeclarationItem.getDeclNo())) {
				cmDeclarationItem.setDeclNo(modifyObj.getDeclNo());
			}
		}
		List<CmDeclarationVehicle> cmDeclarationVehicles = modifyObj
				.getCmDeclarationVehicles();
		for (CmDeclarationVehicle cmDeclarationVehicle : cmDeclarationVehicles) {
			if (null != cmDeclarationVehicle) {
				if (!StringUtils.hasText(cmDeclarationVehicle.getT3())) {
					cmDeclarationVehicle.setT3("T3");
				}
				if (!StringUtils.hasText(cmDeclarationVehicle.getDeclNo())) {
					cmDeclarationVehicle.setDeclNo(modifyObj.getDeclNo());
				}
			}
		}
		List<CmDeclarationContainer> cmDeclarationContainers = modifyObj
				.getCmDeclarationContainers();
		for (CmDeclarationContainer cmDeclarationContainer : cmDeclarationContainers) {
			if (null != cmDeclarationContainer) {
				if (!StringUtils.hasText(cmDeclarationContainer.getT4())) {
					cmDeclarationContainer.setT4("T4");
				}
				if (!StringUtils.hasText(cmDeclarationContainer.getDeclNo())) {
					cmDeclarationContainer.setDeclNo(modifyObj.getDeclNo());
				}
			}
		}
	}

	public List<CmDeclarationHead> findByProperty(String fieldName,
			Object fieldValue) {
		return cmDeclarationHeadDAO.findByProperty(CmDeclarationHead.class
				.getName(), fieldName, fieldValue);
	}

	public List find(HashMap findObjs) {
		return cmDeclarationHeadDAO.find(findObjs);
	}

	public CmDeclarationHead findById(Long headId) {
		if(headId != 0)
			return (CmDeclarationHead) cmDeclarationHeadDAO.findByPrimaryKey(CmDeclarationHead.class, headId);
		else
			return null;
	}

	public void setCmDeclarationHeadDAO(
			CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}

	public void setCmDeclarationItemDAO(
			CmDeclarationItemDAO cmDeclarationItemDAO) {
		this.cmDeclarationItemDAO = cmDeclarationItemDAO;
	}

	public void setCmDeclarationVehicleDAO(
			CmDeclarationVehicleDAO cmDeclarationVehicleDAO) {
		this.cmDeclarationVehicleDAO = cmDeclarationVehicleDAO;
	}

	public void setCmDeclarationContainerDAO(
			CmDeclarationContainerDAO cmDeclarationContainerDAO) {
		this.cmDeclarationContainerDAO = cmDeclarationContainerDAO;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
	
	public void setimAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
		this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}
	// ******************************
	// ******************************

	public String getIdentification(Long headId) throws Exception {

		String id = null;
		try {
			CmDeclarationHead cmDeclarationHead = findById(headId);
			if (cmDeclarationHead != null) {
				id = headId + "";
			} else {
				throw new NoSuchDataException("主檔查無主鍵：" + headId + "的資料！");
			}

			return id;
		} catch (Exception ex) {
			log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 *  更新海關進出倉單主檔
	 * 
	 * @param parameterMap
	 * @throws FormException
	 * @throws Exception
	 */
	public Map updateAJAXDeclarationHead(Map parameterMap)
			throws FormException, Exception {

		HashMap resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			Long headId = getDeclarationHeadId(formLinkBean);
			String employeeCode = (String) PropertyUtils.getProperty(otherBean,
					"loginEmployeeCode");
			String formStatus = (String) PropertyUtils.getProperty(otherBean,
					"formAction");

			CmDeclarationHead cmDeclarationHeadPO = this.findById(headId);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, cmDeclarationHeadPO);
			//====================取得條件資料=====cmDeclarationHeadPO=================
			//刪除於SI_PROGRAM_LOG的原識別碼資料
			siProgramLogAction.deleteProgramLog(CmDeclarationAction.PROGRAM_ID,
					MessageStatus.LOG_ERROR, this.getIdentification(headId));

			if(!OrderStatus.WAIT_IN.equals(formStatus)){
				cmDeclarationHeadPO.setStatus(formStatus);	
			}
			String resultMsg = modifyAjaxDeclarationHead(cmDeclarationHeadPO,
					employeeCode);

			resultMap.put("entityBean", cmDeclarationHeadPO);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
		} catch (FormException fe) {
			log.error("海關進出倉單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("海關進出倉單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("海關進出倉單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	
    public static Object[] startProcess(CmDeclarationHead form) throws ProcessFailedException{       
	    try{           
		    String packageId = "Cm_Declaration";         
		    String processId = "process";           
		    String version = "20091021";
		    String sourceReferenceType = "Declaration";
		    HashMap context = new HashMap();	    
		    context.put("formId", form.getHeadId());
		    return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		}catch (Exception ex){
		    log.error("海關進出倉流程啟動失敗，原因：" + ex.toString());
		    throw new ProcessFailedException("海關進出倉流程啟動失敗！");
		}	      
    }
    
    public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
 	   
        try{           
	    HashMap context = new HashMap();
	    context.put("approveResult", approveResult);
	       
	    return ProcessHandling.completeAssignment(assignmentId, context);
	}catch (Exception ex){
	    log.error("完成海關進出倉工作任務失敗，原因：" + ex.toString());
	    throw new ProcessFailedException("完成海關進出倉工作任務失敗！");
	}
    }

	/**
	 *  檢查海關單號是否已經存在
	 * 
	 *  @param declNo
	 */

	public boolean isRepeatedDeclNo(long headId, String declNo) {
		List cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty(
				"CmDeclarationHead", "declNo", declNo);
		for (Object cmDeclarationHead : cmDeclarationHeads) {
			//System.out.println(((CmDeclarationHead) cmDeclarationHead).getHeadId()+"xxx"+((CmDeclarationHead) cmDeclarationHead).getDeclNo());////////////////
			if (((CmDeclarationHead) cmDeclarationHead).getHeadId() != headId) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 更新海關進出倉單狀態
	 * 
	 * @param updateObj
	 * @param loginUser
	 */
	public void modifyDeclarationHeadStatus(long headId, String status) {
		CmDeclarationHead updateObj = this.findById(headId);
		if (updateObj != null) {
			updateObj.setStatus(status);
			//updateObj.setLastUpdateDate(new Date());
			cmDeclarationHeadDAO.update(updateObj);
		}
	}

	/**
	 * 更新海關進出倉單主檔或明細檔
	 * 
	 * @param updateObj
	 * @param loginUser
	 */
	private String modifyAjaxDeclarationHead(CmDeclarationHead updateObj,
			String loginUser) {

		updateObj.setLastUpdatedBy(loginUser);
		updateObj.setLastUpdateDate(new Date());
		cmDeclarationHeadDAO.update(updateObj);
		return "更新成功";
	}

	public Long getDeclarationHeadId(Object bean) throws Exception {
		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("headId=" + id);
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的銷貨單主鍵為空值！");
		}
		return headId;
	}

	// create default CmDeclarationHead
	public CmDeclarationHead createNewCmDeclarationHead(Map argumentMap,Map resultMap) throws Exception {
		try {
			String loginEmployeeCode = (String) argumentMap.get("loginEmployeeCode");
			Date date = new Date();

			CmDeclarationHead form = new CmDeclarationHead();
			form.setCreatedBy(loginEmployeeCode);
			form.setCreationDate(date);
			form.setLastUpdateDate(date);
			form.setStatus(OrderStatus.SAVE);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setCreatedBy(loginEmployeeCode);
			save(form);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("employeeName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			resultMap.put("form", form);
			return form;
		} catch (Exception ex) {
			log.error("產生新報關單失敗，原因：" + ex.toString());
			throw new Exception("產生新報關單發生錯誤！");
		}
	}

	/**
	 * 取得暫時單號存檔
	 * 
	 * @param inventoryCountsHead
	 * @throws Exception
	 */
	public void saveTmp(CmDeclarationHead cmDeclarationHead) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			cmDeclarationHead.setReserve5(tmpOrderNo);
			//inventoryCountsHead.setLastUpdateDate(new Date());
			//inventoryCountsHead.setCreationDate(new Date());
			//imInventoryCountsHeadDAO.save(inventoryCountsHead);
			this.save(cmDeclarationHead);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存盤點單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存盤點單發生錯誤！");
		}
	}

	/**
	 * 更新PAGE的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateOrSaveAJAXVatT2PageLinesData(Properties httpRequest) throws Exception {
		try {
			String errorMsg = null;
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			int tab = NumberUtils.getInt(httpRequest.getProperty("tab"));
			log.info("headId = " + headId);
			if (headId > 0) {
				CmDeclarationHead cmDeclarationHead = this.findById(headId);
				//String status = cmDeclarationHead.getStatus();
				String[] GRID_FIELD_NAMES = {};
				//String[] GRID_FIELD_DEFAULT_VALUES = {};
				int[] GRID_FIELD_TYPES = {};
				int indexNo = 0;
				if (tab == 2) {
					GRID_FIELD_NAMES = T2_GRID_FIELD_NAMES;
					//GRID_FIELD_DEFAULT_VALUES = T2_GRID_FIELD_DEFAULT_VALUES;
					GRID_FIELD_TYPES = T2_GRID_FIELD_TYPES;
					indexNo = cmDeclarationItemDAO.findPageLineMaxIndex(headId).intValue();
				} else if (tab == 3) {
					GRID_FIELD_NAMES = T3_GRID_FIELD_NAMES;
					//GRID_FIELD_DEFAULT_VALUES = T3_GRID_FIELD_DEFAULT_VALUES;
					GRID_FIELD_TYPES = T3_GRID_FIELD_TYPES;
					indexNo = cmDeclarationVehicleDAO.findPageLineMaxIndex(headId).intValue();
				} else if (tab == 4) {
					GRID_FIELD_NAMES = T4_GRID_FIELD_NAMES;
					//GRID_FIELD_DEFAULT_VALUES = T4_GRID_FIELD_DEFAULT_VALUES;
					GRID_FIELD_TYPES = T4_GRID_FIELD_TYPES;
					indexNo = cmDeclarationContainerDAO.findPageLineMaxIndex(headId).intValue();
				}
				String declNo = cmDeclarationHead.getDeclNo();
					// 將STRING資料轉成List Properties record data
					List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,GRID_FIELD_NAMES);
					// Get INDEX NO
					if (upRecords != null) {
						for (Properties upRecord : upRecords) {
							// 先載入HEAD_ID OR LINE DATA
							Long itemId = NumberUtils.getLong(upRecord.getProperty("itemId"));
							if (tab == 2) {
								String prdtNo = upRecord.getProperty("prdtNo");
								if (StringUtils.hasText(prdtNo)) {
									CmDeclarationItem cmDeclarationItemPO = cmDeclarationItemDAO.findItemByIdentification(headId,itemId);
									if (cmDeclarationItemPO != null) {
										AjaxUtils.setPojoProperties(cmDeclarationItemPO, upRecord,GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationItemDAO.update(cmDeclarationItemPO);
									} else {
										indexNo++;
										CmDeclarationItem cmDeclarationItem = new CmDeclarationItem();
										AjaxUtils.setPojoProperties(cmDeclarationItem, upRecord,GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationItem.setIndexNo(indexNo+0L);
										cmDeclarationItem.setItemNo(indexNo+0L);
										cmDeclarationItem.setDeclNo(declNo);
										cmDeclarationItem.setCmDeclarationHead(cmDeclarationHead);
										cmDeclarationItemDAO.save(cmDeclarationItem);
									}
								}
							} else if (tab == 3) {
								String itemNo = upRecord.getProperty("itemNo");
								if (StringUtils.hasText(itemNo)) {
									CmDeclarationVehicle cmDeclarationVehiclePO = cmDeclarationVehicleDAO
											.findItemByIdentification(headId,itemId);
									if (cmDeclarationVehiclePO != null) {
										AjaxUtils.setPojoProperties(cmDeclarationVehiclePO, upRecord,
												GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationVehicleDAO.update(cmDeclarationVehiclePO);
									} else {
										indexNo++;
										CmDeclarationVehicle cmDeclarationVehicle = new CmDeclarationVehicle();
										AjaxUtils.setPojoProperties(cmDeclarationVehicle, upRecord,
												GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationVehicle.setIndexNo(Long.valueOf(indexNo));
										cmDeclarationVehicle.setDeclNo(declNo);
										cmDeclarationVehicle.setCmDeclarationHead(cmDeclarationHead);
										cmDeclarationVehicleDAO.save(cmDeclarationVehicle);
									}
								}
							} else if (tab == 4) {
								String contrNo = upRecord.getProperty("contrNo");
								if (StringUtils.hasText(contrNo)) {
									CmDeclarationContainer cmDeclarationContainerPO = cmDeclarationContainerDAO
											.findItemByIdentification(headId,
													itemId);
									if (cmDeclarationContainerPO != null) {
										AjaxUtils.setPojoProperties(cmDeclarationContainerPO, upRecord,
												GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationContainerDAO.update(cmDeclarationContainerPO);
									} else {
										indexNo++;
										CmDeclarationContainer cmDeclarationContainer = new CmDeclarationContainer();
										AjaxUtils.setPojoProperties(
												cmDeclarationContainer, upRecord,
												GRID_FIELD_NAMES, GRID_FIELD_TYPES);
										cmDeclarationContainer.setIndexNo(Long.valueOf(indexNo));
										cmDeclarationContainer.setDeclNo(declNo);
										cmDeclarationContainer.setCmDeclarationHead(cmDeclarationHead);
										cmDeclarationContainerDAO.save(cmDeclarationContainer);
									}
								}
							}
						}
					}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新海關進出倉明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新海關進出倉明細失敗！");
		}
	}

	/**
	 * 
	 * ajax 更新報單明細的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */

	public List<Properties> updateOrSaveAJAXVatT2PageLinesDataOldV(
			Properties httpRequest) throws Exception {
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_ROW_COUNT));

			Long headId = NumberUtils
					.getLong(httpRequest.getProperty("headId"));
			int tab = NumberUtils.getInt(httpRequest.getProperty("tab"));
			String loginEmployeeCode = httpRequest
					.getProperty("loginEmployeeCode");

			if (headId == null) {
				throw new ValidationErrorException("傳入的海關進出倉主鍵為空值！");
			}
			CmDeclarationHead cmDeclarationHead = this.findById(headId);
			String errorMsg = null;

			String[] GRID_FIELD_NAMES = T2_GRID_FIELD_NAMES;
			String[] GRID_FIELD_DEFAULT_VALUES = T2_GRID_FIELD_DEFAULT_VALUES;
			int[] GRID_FIELD_TYPES = T2_GRID_FIELD_TYPES;

			if (tab == 3) {
				GRID_FIELD_NAMES = T3_GRID_FIELD_NAMES;
				GRID_FIELD_DEFAULT_VALUES = T3_GRID_FIELD_DEFAULT_VALUES;
				GRID_FIELD_TYPES = T3_GRID_FIELD_TYPES;
			} else if (tab == 4) {
				GRID_FIELD_NAMES = T4_GRID_FIELD_NAMES;
				GRID_FIELD_DEFAULT_VALUES = T4_GRID_FIELD_DEFAULT_VALUES;
				GRID_FIELD_TYPES = T4_GRID_FIELD_TYPES;
			}

			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,
					gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);

			// Get INDEX NO
			//long indexNo = cmDeclarationHead.getMaxItemIndexNo();
			//int indexNo = imLetterOfCreditAlterDAO.findPageLineMaxIndex(headId).intValue();

			// 考慮狀態

			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					// 先載入HEAD_ID OR LINE DATA

					Long itemId = NumberUtils.getLong(upRecord
							.getProperty("itemId"));

					//if (StringUtils.hasText(itemCode)) {s

					//List<ImLetterOfCreditAlter> imLetterOfCreditAlters = imLetterOfCreditAlterDAO.findByProperty("ImLetterOfCreditAlter", new String[]{ "imLetterOfCreditHead.headId", "lineId" }, new Object[]{ headId, lineId } );
					Date date = new Date();

					if (itemId != 0) {

						if (tab == 2) {
							CmDeclarationItem item = getCmDeclarationItemByItemId(cmDeclarationHead , itemId);
							AjaxUtils.setPojoProperties(item, upRecord,
									GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							        updateCmDeclarationItem(cmDeclarationHead,item);
						} else if (tab == 3) {
							CmDeclarationVehicle item = getCmDeclarationVehicleByItemId(cmDeclarationHead,itemId);
							AjaxUtils.setPojoProperties(item, upRecord,
									GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							        updateCmDeclarationVehicle(cmDeclarationHead,item);
						} else if (tab == 4) {
							CmDeclarationContainer item = getCmDeclarationContainerByItemId(cmDeclarationHead,itemId);
							AjaxUtils.setPojoProperties(item, upRecord,
									GRID_FIELD_NAMES, GRID_FIELD_TYPES);
									updateCmDeclarationContainer(cmDeclarationHead,item);
						}

					}
					/*
					else{
					  if (StringUtils.hasText(upRecord.getProperty("prdtNo"))) {
					 
					    CmDeclarationItem item = new CmDeclarationItem();
					    AjaxUtils.setPojoProperties(item,upRecord, T2_GRID_FIELD_NAMES,T2_GRID_FIELD_TYPES);
					    indexNo++;
					    item.setIndexNo(indexNo);
					  
					    cmDeclarationHead.getCmDeclarationItems().add(item);
					  }
					}
					 */
					/*
					if (imLetterOfCreditAlters != null && imLetterOfCreditAlters.size() > 0 ) {
						log.info( "before isDeleteRecord(0) = " + imLetterOfCreditAlters.get(0).getIsDeleteRecord() );
						ImLetterOfCreditAlter imLetterOfCreditAlter = imLetterOfCreditAlters.get(0);
						AjaxUtils.setPojoProperties(imLetterOfCreditAlter,upRecord, ALT_GRID_FIELD_NAMES,ALT_GRID_FIELD_TYPES);
						log.info( "after isDeleteRecord(0) = " + imLetterOfCreditAlters.get(0).getIsDeleteRecord() );
						
						imLetterOfCreditAlter.setLastUpdatedBy(loginEmployeeCode);
						imLetterOfCreditAlter.setLastUpdateDate(date);
						imLetterOfCreditAlterDAO.update(imLetterOfCreditAlter);

					} 
					 */
					//else {
					/*
					indexNo++;
					ImLetterOfCreditAlter imLetterOfCreditAlter = new ImLetterOfCreditAlter(); 

					AjaxUtils.setPojoProperties(imLetterOfCreditAlter,upRecord, ALT_GRID_FIELD_NAMES,ALT_GRID_FIELD_TYPES);
					imLetterOfCreditAlter.setImLetterOfCreditHead(new ImLetterOfCreditHead(headId));
					imLetterOfCreditAlter.setCreatedBy(loginEmployeeCode);
					imLetterOfCreditAlter.setCreationDate(date);
					imLetterOfCreditAlter.setLastUpdatedBy(loginEmployeeCode);
					imLetterOfCreditAlter.setLastUpdateDate(date);
					imLetterOfCreditAlter.setIndexNo(Long.valueOf(indexNo));
					imLetterOfCreditAlterDAO.save(imLetterOfCreditAlter);
					
					 */
					//	}
					//	}
				}
			}
			this.update(cmDeclarationHead);
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			log.error("更新修狀明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新修狀明細失敗！");
		}
	}

	/**
	 * ajax 海關進出倉載入時,取得vat2 or 3 or 4 報單明細分頁 等資料
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXVatT2PageData(Properties httpRequest)
			throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils
					.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int tab = NumberUtils.getInt(httpRequest.getProperty("tab"));
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值=========================
			HashMap map = new HashMap();
			map.put("headId", headId);
			map.put("startPage", iSPage);
			map.put("pageSize", iPSize);
			//======================取得頁面所需資料===========================
			if (tab == 2) {
				List<CmDeclarationItem> cmDeclarationItems = cmDeclarationItemDAO
						.findPageLine(headId, iSPage, iPSize);
				if (cmDeclarationItems != null && cmDeclarationItems.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = cmDeclarationItems.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = cmDeclarationItemDAO
							.findPageLineMaxIndex(headId);
					//refreshItemData(map, cmDeclarationItems);
					result.add(AjaxUtils
							.getAJAXPageData(httpRequest, T2_GRID_FIELD_NAMES,
									T2_GRID_FIELD_DEFAULT_VALUES,
									cmDeclarationItems, gridDatas, firstIndex,
									maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
							T2_GRID_FIELD_NAMES, T2_GRID_FIELD_DEFAULT_VALUES,
							gridDatas));
				}
			} else if (tab == 3) {
				List<CmDeclarationVehicle> cmDeclarationVehicles = cmDeclarationVehicleDAO
						.findPageLine(headId, iSPage, iPSize);
				if (cmDeclarationVehicles != null
						&& cmDeclarationVehicles.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = cmDeclarationVehicles.get(0).getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = cmDeclarationVehicleDAO
							.findPageLineMaxIndex(headId);
					//refreshItemData(map, cmDeclarationItems);
					result.add(AjaxUtils.getAJAXPageData(httpRequest,
							T3_GRID_FIELD_NAMES, T3_GRID_FIELD_DEFAULT_VALUES,
							cmDeclarationVehicles, gridDatas, firstIndex,
							maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
							T3_GRID_FIELD_NAMES, T3_GRID_FIELD_DEFAULT_VALUES,
							gridDatas));
				}
			} else if (tab == 4) {
				List<CmDeclarationContainer> cmDeclarationContainers = cmDeclarationContainerDAO
						.findPageLine(headId, iSPage, iPSize);
				if (cmDeclarationContainers != null
						&& cmDeclarationContainers.size() > 0) {
					// 取得第一筆的INDEX
					Long firstIndex = cmDeclarationContainers.get(0)
							.getIndexNo();
					// 取得最後一筆 INDEX
					Long maxIndex = cmDeclarationContainerDAO
							.findPageLineMaxIndex(headId);
					//refreshItemData(map, cmDeclarationItems);
					result.add(AjaxUtils.getAJAXPageData(httpRequest,
							T4_GRID_FIELD_NAMES, T4_GRID_FIELD_DEFAULT_VALUES,
							cmDeclarationContainers, gridDatas, firstIndex,
							maxIndex));
				} else {
					result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
							T4_GRID_FIELD_NAMES, T4_GRID_FIELD_DEFAULT_VALUES,
							gridDatas));
				}
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的盤點明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的盤點明細失敗！");
		}
	}

	/**
	 * 更新盤點單商品明細資料
	 * 
	 * @param parameterMap
	 * @param inventoryCountsLines
	 */
	/*
	private void refreshItemData(HashMap parameterMap, List<CmDeclarationItem> cmDeclarationItems){
	    
	    for(CmDeclarationItem cmDeclarationItem : cmDeclarationItems){
	        getCmDeclarationItemRelationData(parameterMap, cmDeclarationItem);
	    }
	}
	 */
	/*
	private void getCmDeclarationItemRelationData(HashMap parameterMap, CmDeclarationItem cmDeclarationItem){
	      
	      String brandCode = (String)parameterMap.get("brandCode");
	      String itemName = "查無此商品資料";
	      String itemCode = inventoryCountsLine.getItemCode();
	//品名
	if(StringUtils.hasText(itemCode)){
	    ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
	    if(itemPO != null){
	              inventoryCountsLine.setItemName(itemPO.getItemCName());
	    }else{
		inventoryCountsLine.setItemName(itemName);
	    }      
	}else{
	    inventoryCountsLine.setItemName("");
	}
	}
	 */

	public CmDeclarationHead findCmDeclarationHead(Map argumentMap,
			Map resultMap) throws FormException, Exception {
		try {
			Long formId = (Long) argumentMap.get("formId");
			CmDeclarationHead form = findById(formId);
			if (form != null) {
				resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
				resultMap.put("EmployeeName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
				resultMap.put("form", form);
				return form;
			} else {
				throw new NoSuchObjectException("查無盤點單主鍵：" + formId + "的資料！");
			}
		} catch (FormException fe) {
			log.error("查詢報關單失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("查詢報關單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢報關單發生錯誤！");
		}
	}

	private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction)
			throws Exception {

		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,"loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		HashMap conditionMap = new HashMap();
		conditionMap.put("loginBrandCode", loginBrandCode);
		conditionMap.put("loginEmployeeCode", loginEmployeeCode);
		conditionMap.put("formId", formId);
		if (isSubmitAction) {
			String beforeChangeStatus = (String) PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
			String formStatus = (String) PropertyUtils.getProperty(otherBean,"formStatus");
			conditionMap.put("beforeChangeStatus", beforeChangeStatus);
			conditionMap.put("formStatus", formStatus);
		}
		return conditionMap;
	}

	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	public List<Properties> getSearchSelection(Map parameterMap)
			throws FormException, Exception {
		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			log.info("getSearchSelection.parameterMap:"
					+ parameterMap.keySet().toString());
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);
			log.info("getSearchSelection.picker_parameter:" + timeScope + "/"
					+ searchKeys.toString());

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			log.info("getSearchSelection.result:" + result.size());
			if (result.size() > 0)
				pickerResult.put("result", result);
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

	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			//======================帶入Head的值=========================

			String startOrderNo = httpRequest.getProperty("startOrderNo");
			String endOrderNo = httpRequest.getProperty("endOrderNo");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			if(StringUtils.hasText(startOrderNo))
				findObjs.put(" and declNo >= :startOrderNo", startOrderNo);
			if(StringUtils.hasText(endOrderNo))
				findObjs.put(" and declNo <= :endOrderNo", endOrderNo);
			findObjs.put(" and declNo != :nul", "null");

			//==============================================================	    
			Map cmDeclarationMap = cmDeclarationHeadDAO.search("CmDeclarationHead", findObjs, "order by importDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<CmDeclarationHead> cmDeclaration = (List<CmDeclarationHead>) cmDeclarationMap.get(BaseDAO.TABLE_LIST);
			log.info("cmDeclaration.size" + cmDeclaration.size());
			if (cmDeclaration != null && cmDeclaration.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX 
				Long maxIndex = (Long) cmDeclarationHeadDAO.search(
						"CmDeclarationHead", "count(DECL_NO) as rowCount",
						findObjs, " order by importDate desc ", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT)
						.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, cmDeclaration,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的選單查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的選單功能查詢失敗！");
		}

	}

	
	
	// deal with DeclarationItem
	public CmDeclarationItem getCmDeclarationItemByItemId(CmDeclarationHead cmDeclarationHead , long itemId){
		//CmDeclarationItem cmDeclarationItem = null;
		for (CmDeclarationItem item : cmDeclarationHead.getCmDeclarationItems()) {
			if(item.getItemId()==itemId){
				return item;
			}
		}
		return null;
	}
	
	
	public void updateCmDeclarationItem(CmDeclarationHead cmDeclarationHead,CmDeclarationItem item){
		CmDeclarationItem oItem = getCmDeclarationItemByItemId(cmDeclarationHead,item.getItemId());
		oItem = item;
	}
	
	
	// deal with DeclarationVehicle
	public CmDeclarationVehicle getCmDeclarationVehicleByItemId(CmDeclarationHead cmDeclarationHead,long itemId){
		for (CmDeclarationVehicle item : cmDeclarationHead.getCmDeclarationVehicles()) {
			if(item.getItemId()==itemId){
				return item;
			}
		}
		return null;
	}
	
	public void updateCmDeclarationVehicle(CmDeclarationHead cmDeclarationHead,CmDeclarationVehicle item){
		CmDeclarationVehicle oItem = getCmDeclarationVehicleByItemId(cmDeclarationHead,item.getItemId());
		oItem = item;
	}
	
	
	// deal with DeclarationContainer
	public CmDeclarationContainer getCmDeclarationContainerByItemId(CmDeclarationHead cmDeclarationHead,long itemId){
		for (CmDeclarationContainer item : cmDeclarationHead.getCmDeclarationContainers()) {
			if(item.getItemId()==itemId){
				return item;
			}
		}
		return null;
	}
	
	
	public void updateCmDeclarationContainer(CmDeclarationHead cmDeclarationHead,CmDeclarationContainer item){
		CmDeclarationContainer oItem = getCmDeclarationContainerByItemId(cmDeclarationHead,item.getItemId());
		oItem = item;
	}
	
	public CmDeclarationHead getCmDeclarationHeadByDeclNo(String declNo){
		CmDeclarationHead cmDeclarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(declNo);
		return cmDeclarationHead;
	}
	
	public Map updateParameter(Map parameterMap) throws FormException, Exception {
        HashMap resultMap = new HashMap();
        try{
            Object formBindBean = parameterMap.get("vatBeanFormBind");
    	    Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	    Object otherBean = parameterMap.get("vatBeanOther");
    	    Long headId = NumberUtils.getLong((String)PropertyUtils.getProperty(formLinkBean, "headId"));
    	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	    String formStatus = (String) PropertyUtils.getProperty(otherBean,"formAction");
    	    String declNo = (String) PropertyUtils.getProperty(formLinkBean, "declNo");
    	    parameterMap.put("loginEmployeeCode", loginEmployeeCode);
    	    Double custValueAmt = 0.0;
    	    Double fobValue = 0.0;
    	    Double qty = 0.0;
    	    Long dutiableValuePrice = 0L;
    	    
    	    //取得欲更新的bean
    	    CmDeclarationHead cmDeclarationHead = this.findById(headId);
    	    if(cmDeclarationHead != null){
    	    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, cmDeclarationHead);
    	    	log.info("更新明細開始");
    	    	List<CmDeclarationItem> oldCmDeclarationItems = cmDeclarationHead.getCmDeclarationItems();
    	    	List<CmDeclarationItem> cmDeclarationItems = new ArrayList();
    	    	log.info("dddddddd"+oldCmDeclarationItems.size());
    	    	for(CmDeclarationItem cmI:oldCmDeclarationItems){
    	    		if(cmI.getCustValueAmt()!=null){
    	    			custValueAmt = cmI.getCustValueAmt();
    	    		}
    	    		if(cmI.getFobValue()!=null){
    	    			fobValue = cmI.getFobValue();
    	    		}
    	    		if(cmI.getQty()!=null){
    	    			qty = cmI.getQty();
    	    		}    	    		
    	    		dutiableValuePrice = (Long) Math.round((custValueAmt+fobValue)/qty);
    	    		cmI.setDutiableValuePrice(dutiableValuePrice);
    	    		cmDeclarationItems.add(cmI);
    	    	}
    	    	cmDeclarationHead.setCmDeclarationItems(cmDeclarationItems);
    	    	log.info("更新明細結束");
    	    	cmDeclarationHead.setStatus(formStatus);
    	    	cmDeclarationHead.setLastUpdatedBy(loginEmployeeCode);
    	    	cmDeclarationHead.setLastUpdateDate(new Date());
    	    	update(cmDeclarationHead);
    	    }else{
    	    	cmDeclarationHead = this.createNewCmDeclarationHead(parameterMap, resultMap);
    	    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, cmDeclarationHead);
    	    	cmDeclarationHead.setDeclNo(declNo);
    	    	cmDeclarationHead.setStatus(formStatus);
    	    	cmDeclarationHead.setCreatedBy(loginEmployeeCode);
    	    	cmDeclarationHead.setCreationDate(new Date());
    	    	cmDeclarationHead.setLastUpdatedBy(loginEmployeeCode);
    	    	cmDeclarationHead.setLastUpdateDate(new Date());
    	    	save(cmDeclarationHead);
    	    }
	    	resultMap.put("entityBean", cmDeclarationHead);
   	    	return resultMap;      
        }catch (Exception ex) {
        	log.error("報關單存檔時發生錯誤，原因：" + ex.toString());
        	throw new Exception(ex.getMessage());
        }
    }
	
	/**
	 *  檢查HEAD必要欄位是否有值 
	 */

	public List checkDeclarationHeadInput(Map parameterMap) {
		Object otherBean = parameterMap.get("vatBeanOther");
		Object vatBeanFormBind = parameterMap.get("vatBeanFormBind");
		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		String[] checkItems = { "msgFun", "bondNo", "strType", "boxNo",
				"declType", "importDate", "declDate", "stgPlace", "rlsTime",
				"rlsPkg", "extraCond", "pkgUnit", "GWgt", "vesselSign",
				"voyageNo", "shipCode", "exporter", "clearType", "refBillNo",
				"inbondNo", "outbondNo" };
		String[] checkItemNames = { "異動別", "海關監管編號", "進出倉別", "報關行箱號", "報單類別",
				"進口日期", "報關日期", "存放處所", "放行時間", "放行件數", "放行附帶條件", "件數單位",
				"總重量", "船舶呼號", "航次", "船公司代碼", "貨物輸出人", "通關方式", "參考單號",
				"進倉保稅業者代碼", "出倉保稅業者代碼" };
		List errorMsgs = new ArrayList();
		String message = null;
		String tabName = "主檔資料";

		try {
			String lastUpdateBy = (String) PropertyUtils.getProperty(otherBean,
					"loginEmployeeCode");
			Long headId = getDeclarationHeadId(formLinkBean);
			String identification = headId + "";

			//siProgramLogAction.deleteProgramLog(PROGRAM_ID, MessageStatus.LOG_ERROR, identification);
			int index = 0;
			for (String checkItem : checkItems) {
				String input = (String) PropertyUtils.getProperty(
						vatBeanFormBind, checkItem);
				if (!StringUtils.hasText(input)) {
					message = "請選擇" + tabName + "的" + checkItemNames[index]
							+ "！";
					siProgramLogAction.createProgramLog(PROGRAM_ID,
							MessageStatus.LOG_ERROR, identification, message,
							lastUpdateBy);
					errorMsgs.add(message);
					log.error(message);
				}
				index++;
			}
		} catch (Exception ex) {
			message = "檢核海關進出倉單" + tabName + "時發生錯誤，原因：" + ex.toString();
			//siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, inventoryCountsHead.getLastUpdatedBy());
			errorMsgs.add(message);
			log.error(message);
		}
		return errorMsgs;
	}
	
	public List<Properties> executeFindCM(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String declarationNo = httpRequest.getProperty("declarationNo");
			List <CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declarationNo);
			if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
				CmDeclarationHead cmDeclarationHead = cmDeclarationHeads.get(0);
				properties.setProperty("ImportDate", AjaxUtils.getPropertiesValue(DateUtils.format(cmDeclarationHead.getImportDate(), "yyyy/MM/dd"), ""));
				properties.setProperty("DeclDate", AjaxUtils.getPropertiesValue(DateUtils.format(cmDeclarationHead.getDeclDate(), "yyyy/MM/dd"), ""));
				properties.setProperty("DeclType", cmDeclarationHead.getDeclType());
				properties.setProperty("ExchangeRate", cmDeclarationHead.getExchangeRate().toString());
			}
			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("查詢報關單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢報關單發生錯誤，原因：" + ex.getMessage());
		}
	}


	public List<Properties> findbyDecSeqForAJAX(Properties httpRequest) throws Exception{

		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		String originalDeclarationNo = null;
		Long originalDeclarationSeq = null;    
		String unit = null; 
		try {
			originalDeclarationNo = httpRequest.getProperty("originalDeclarationNo");
			originalDeclarationNo = originalDeclarationNo.trim().toUpperCase();
			originalDeclarationSeq = NumberUtils.getLong((String)httpRequest.getProperty("originalDeclarationSeq"));

			CmDeclarationItem item = cmDeclarationItemDAO.findOneCmDeclarationItem(originalDeclarationNo, originalDeclarationSeq);

			if(item != null){
				log.info(item.getDeclNo()+"   "+item.getItemNo());
				properties.setProperty("originalDeclarationNo", originalDeclarationNo);
				properties.setProperty("originalDeclarationSeq", originalDeclarationSeq.toString());
				properties.setProperty("unit", item.getUnit());         
			}else{
				properties.setProperty("originalDeclarationNo", originalDeclarationNo);
				properties.setProperty("originalDeclarationSeq", originalDeclarationSeq.toString());
				properties.setProperty("unit", "");   
			}
			result.add(properties);	

			return result;	        
		} catch (Exception ex) {
			log.error("報單號碼：" + originalDeclarationNo + "品項：" + originalDeclarationSeq + "查詢資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("商品單位查詢失敗！");
		}        
	}


        
                   
   





}