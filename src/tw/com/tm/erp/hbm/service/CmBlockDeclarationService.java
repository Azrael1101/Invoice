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

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.CmBlockDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmBlockDeclarationItem;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHand;
import tw.com.tm.erp.hbm.bean.CmDeclarationOnHandView;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.CmBlockDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.CmBlockDeclarationItemDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class CmBlockDeclarationService {

	private static final Log log = LogFactory.getLog(CmBlockDeclarationService.class);

	public static final String PROGRAM_ID = "CM_BLOCK_DECLARATION";

	private CmBlockDeclarationHeadDAO cmBlockDeclarationHeadDAO;
	private CmBlockDeclarationItemDAO cmBlockDeclarationItemDAO;
	private SiProgramLogAction siProgramLogAction;

	private BuOrderTypeService buOrderTypeService;
	private BaseDAO baseDAO;
	private CmDeclarationOnHandDAO cmDeclarationOnHandDAO;
	private CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO;
	private ImItemDAO imItemDAO;

	// 單據明細
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "declarationNo", "declarationSeq", "declDate", "orderNo",
			"customsItemCode", "currentOnHandQty", "blockOnHandQuantity", "description", "lineId" };
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "", "", "", "", "" };

	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG };

	/**
	 * 報單鎖定作業查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "orderTypeCode", "orderNo", "statusName", "lastUpdatedBy",
			"lastUpdateDate", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "", "", "", "", "" };
	
	/**
	 * @param baseDAO
	 *            the baseDAO to set
	 */
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	/**
	 * @param buOrderTypeService
	 *            the buOrderTypeService to set
	 */
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	/**
	 * @param cmBlockDeclarationHeadDAO
	 *            the cmBlockDeclarationHeadDAO to set
	 */
	public void setCmBlockDeclarationHeadDAO(CmBlockDeclarationHeadDAO cmBlockDeclarationHeadDAO) {
		this.cmBlockDeclarationHeadDAO = cmBlockDeclarationHeadDAO;
	}

	/**
	 * @param cmBlockDeclarationItemDAO
	 *            the cmBlockDeclarationItemDAO to set
	 */
	public void setCmBlockDeclarationItemDAO(CmBlockDeclarationItemDAO cmBlockDeclarationItemDAO) {
		this.cmBlockDeclarationItemDAO = cmBlockDeclarationItemDAO;
	}

	/**
	 * @param siProgramLogAction
	 *            the siProgramLogAction to set
	 */
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	/**
	 * @param cmDeclarationOnHandDAO the cmDeclarationOnHandDAO to set
	 */
	public void setCmDeclarationOnHandDAO(CmDeclarationOnHandDAO cmDeclarationOnHandDAO) {
		this.cmDeclarationOnHandDAO = cmDeclarationOnHandDAO;
	}

	/**
	 * @param cmDeclarationOnHandViewDAO the cmDeclarationOnHandViewDAO to set
	 */
	public void setCmDeclarationOnHandViewDAO(CmDeclarationOnHandViewDAO cmDeclarationOnHandViewDAO) {
		this.cmDeclarationOnHandViewDAO = cmDeclarationOnHandViewDAO;
	}

	/**
	 * @param imItemDAO the imItemDAO to set
	 */
	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	/**
	 * executeInitial
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

			CmBlockDeclarationHead cmBlockDeclarationHead = this.getActualMaster(otherBean, resultMap);
			// 顯示正確的建單人員
			String employeeName = "";
			if (cmBlockDeclarationHead.getCreatedBy() == null && !"".equals(cmBlockDeclarationHead.getCreatedBy()))
				employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);
			else {
				employeeName = UserUtils.getUsernameByEmployeeCode(cmBlockDeclarationHead.getCreatedBy());
			}
			resultMap.put("statusName", OrderStatus.getChineseWord(cmBlockDeclarationHead.getStatus()));
			resultMap.put("createdBy", loginEmployeeCode);
			resultMap.put("createdByName", employeeName);

			// 單別
			List<BuOrderType> allOrderTypeCodes = buOrderTypeService.findOrderbyType("T2", "CMB");
			multiList.put("allOrderTypeCodes", AjaxUtils.produceSelectorData(allOrderTypeCodes, "orderTypeCode", "name",
					true, false));

			resultMap.put("form", cmBlockDeclarationHead);
			resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("鎖報單單據初始化失敗，原因：" + ex.toString());
			throw new Exception("鎖報單單據初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 依headId取得實際的鎖報單作業的單據 in initial
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	private CmBlockDeclarationHead getActualMaster(Object otherBean, Map resultMap) throws Exception {
		CmBlockDeclarationHead cmBlockDeclarationHead = null;
		try {
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			cmBlockDeclarationHead = null == formId ? this.executeNew(otherBean) : this.executeFind(formId);

		} catch (Exception e) {
			log.error("取得實際鎖報單單據失敗,原因:" + e.toString());
			throw new Exception("取得實際鎖報單單據失敗,原因:" + e.toString());
		}
		return cmBlockDeclarationHead;
	}

	/**
	 * 產生新的鎖報單作業單據
	 *
	 * @param otherBean
	 * @return
	 * @throws Exception
	 */
	private CmBlockDeclarationHead executeNew(Object otherBean) throws Exception {
		CmBlockDeclarationHead cmBlockDeclarationHead = new CmBlockDeclarationHead();
		try {
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String orderType = (String) PropertyUtils.getProperty(otherBean, "orderTypeCode");

			cmBlockDeclarationHead.setOrderTypeCode(orderType);
			cmBlockDeclarationHead.setStatus(OrderStatus.SAVE);
			cmBlockDeclarationHead.setCreatedBy(loginEmployeeCode);
			cmBlockDeclarationHead.setCreationDate(new Date());
			cmBlockDeclarationHead.setLastUpdatedBy(loginEmployeeCode);
			cmBlockDeclarationHead.setLastUpdateDate(new Date());

			this.saveTmpHead(cmBlockDeclarationHead);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("建立新鎖報單作業單據失敗,原因:" + e.toString());
			throw new Exception("建立新鎖報單作業單據失敗,原因:" + e.toString());
		}
		return cmBlockDeclarationHead;
	}

	/**
	 * 依據headId為查詢條件，取得鎖報單作業單據
	 *
	 * @param transferOrderNo
	 * @return CmTransfer
	 * @throws Exception
	 */
	public CmBlockDeclarationHead executeFind(Long headId) throws Exception {

		try {
			CmBlockDeclarationHead cmBlockDeclarationHead = (CmBlockDeclarationHead) cmBlockDeclarationHeadDAO
					.findById(headId);
			return cmBlockDeclarationHead;
		} catch (Exception ex) {
			log.error("依據鎖報單作業單據主鍵：" + headId + "查詢鎖報單作業單據時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據鎖報單作業單據主鍵：" + headId + "查詢鎖報單作業單據時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 鎖報單作業單據存檔,取得暫存單號
	 *
	 * @param CmTransfer
	 * @throws Exception
	 */
	private void saveTmpHead(CmBlockDeclarationHead cmBlockDeclarationHead) throws Exception {
		System.out.println("===== 鎖報單作業單據存檔 =====");
		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			cmBlockDeclarationHead.setOrderNo(tmpOrderNo);
			baseDAO.save(cmBlockDeclarationHead);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("取得暫時單號，儲存鎖報單作業單據發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號，儲存鎖報單作業單據發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 暫存/送出
	 *
	 * @param parameterMap
	 * @return
	 */
	public Map saveCmBlockDeclarationBean(Map parameterMap, String formAction) throws FormException, Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			// 取得欲更新的bean
			CmBlockDeclarationHead cmBlockDeclarationHead = this.getActualCmBlockDeclaration(formLinkBean);
			this.setOrderNo(cmBlockDeclarationHead);
			if ("SUBMIT".equals(formAction)) {
				System.out.println("============= 扣除報單庫存 =============");
				if (cmBlockDeclarationHead.getCmBlockDeclarationItems() == null
						|| cmBlockDeclarationHead.getCmBlockDeclarationItems().size() == 0)
					throw new FormException("請先新增一筆資料再執行送出！");
				
				List<CmBlockDeclarationItem> cmBlockDeclarationItems = cmBlockDeclarationHead.getCmBlockDeclarationItems();
				// 單據送出後，更新鎖定報單的庫存量
				for (int i = 0; i < cmBlockDeclarationItems.size(); i++) {
					CmBlockDeclarationItem cmBlockDeclarationItem = (CmBlockDeclarationItem) cmBlockDeclarationItems.get(i);
					CmDeclarationOnHand cmDeclarationOnHand = cmDeclarationOnHandDAO.getOnHandById(
							cmBlockDeclarationItem.getDeclarationNo(), cmBlockDeclarationItem.getDeclarationSeq(), "FW",
							"T2");
					if (null == cmDeclarationOnHand)
						throw new FormException("報單單號：" + cmBlockDeclarationItem.getDeclarationNo() + "，報單項次："
								+ cmBlockDeclarationItem.getDeclarationSeq() + "，品號："
								+ cmBlockDeclarationItem.getCustomsItemCode() + "：庫存不存在，請重新確認！");

					String errorMsg = null;
					if (cmBlockDeclarationItem.getBlockOnHandQuantity() == 0D
							|| null == cmBlockDeclarationItem.getBlockOnHandQuantity()) {
						errorMsg = "報單號碼：" + cmBlockDeclarationItem.getDeclarationNo() + "，報單項次："
								+ cmBlockDeclarationItem.getDeclarationSeq() + " 的鎖定庫存量不得為0或空值！";
						throw new FormException(errorMsg);
					}

					List<CmDeclarationOnHandView> cmDeclarationOnHandViews = cmDeclarationOnHandViewDAO
							.findByProperty(
									"CmDeclarationOnHandView",
									" AND declarationNo = ? AND declarationSeq = ?  AND customsItemCode = ? AND customsWarehouseCode = ? AND brandCode = ? ",
									new Object[] { cmBlockDeclarationItem.getDeclarationNo(),
											cmBlockDeclarationItem.getDeclarationSeq(),
											cmBlockDeclarationItem.getCustomsItemCode(), "FW", "T2" });
					CmDeclarationOnHandView cmDeclarationOnHandView = (CmDeclarationOnHandView) cmDeclarationOnHandViews
							.get(0);
					if (cmBlockDeclarationItem.getBlockOnHandQuantity() > cmDeclarationOnHandView.getCurrentOnHandQty()
							- cmDeclarationOnHandView.getBlockOnHandQuantity()) {
						errorMsg = "報單號碼：" + cmBlockDeclarationItem.getDeclarationNo() + "，報單項次："
								+ cmBlockDeclarationItem.getDeclarationSeq() + " 的鎖定庫存量不得大於目前有效庫存量！";
						throw new FormException(errorMsg);
					}
					
					if (cmBlockDeclarationItem.getBlockOnHandQuantity() < 0D
							&& (cmBlockDeclarationItem.getBlockOnHandQuantity() + cmDeclarationOnHandView
									.getBlockOnHandQuantity()) < 0D) {
						errorMsg = "解除鎖定報單號碼：" + cmBlockDeclarationItem.getDeclarationNo() + "，報單項次："
								+ cmBlockDeclarationItem.getDeclarationSeq() + " 的數量不得大於目前鎖定庫存量！";
						throw new FormException(errorMsg);
					}
					
					cmDeclarationOnHand.setBlockOnHandQuantity(cmDeclarationOnHand.getBlockOnHandQuantity()
							+ cmBlockDeclarationItem.getBlockOnHandQuantity());
					cmDeclarationOnHand.setDescription(cmBlockDeclarationItem.getDescription());
					cmDeclarationOnHand.setLastUpdatedBy(employeeCode);
					cmDeclarationOnHand.setLastUpdateDate(new Date());
					cmDeclarationOnHandDAO.update(cmDeclarationOnHand);
				}
				// 修改報單鎖定之庫存
				cmBlockDeclarationHead.setStatus(OrderStatus.FINISH);
			}
			if("VOID".equals(formAction))
				cmBlockDeclarationHead.setStatus(OrderStatus.VOID);
			
			resultMap.put("entityBean", cmBlockDeclarationHead);
			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("報單鎖定作業單據資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 透過headId取得報單鎖定作業單據
	 *
	 * @param formBean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public CmBlockDeclarationHead getActualCmBlockDeclaration(Object formBean) throws FormException, Exception {

		CmBlockDeclarationHead cmBlockDeclarationHead = null;
		String id = (String) PropertyUtils.getProperty(formBean, "headId");
		System.out.println("headId ::: " + id);
		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			cmBlockDeclarationHead = (CmBlockDeclarationHead) cmBlockDeclarationHeadDAO.findById(headId);
			if (cmBlockDeclarationHead == null) {
				throw new NoSuchObjectException("查無報單鎖定作業單據主鍵：" + headId + "的資料！");
			}
		} else {
			throw new ValidationErrorException("傳入的報單鎖定作業單據主鍵為空值！");
		}
		return cmBlockDeclarationHead;
	}

	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(CmBlockDeclarationHead cmBlockDeclarationHead) throws ObtainSerialNoFailedException {
		String tempOrderNo = cmBlockDeclarationHead.getOrderNo();
		if (AjaxUtils.isTmpOrderNo(tempOrderNo)) {
			try {
				String orderNo = buOrderTypeService.getOrderNo("T2", cmBlockDeclarationHead.getOrderTypeCode(), null, null);
				cmBlockDeclarationHead.setOrderNo(orderNo);
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得報單鎖定作業單據單號失敗！");
			}
		}
	}

	/**
	 * 更新報單鎖定作業單據明細資料
	 *
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception {
		log.info("updateAJAXPageLinesData....報單鎖定作業單據明細....");
		try {
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
			if (headId == null) {
				throw new ValidationErrorException("傳入的報單鎖定作業單據明細主鍵為空值！");
			}
			String status = httpRequest.getProperty("status");
			String errorMsg = null;

			log.info("===== updateAJAXPageLinesData.status:" + status + " =====");
			if (OrderStatus.SAVE.equals(status)) {
				CmBlockDeclarationHead cmBlockDeclarationHead = new CmBlockDeclarationHead();
				cmBlockDeclarationHead.setHeadId(headId);
				// 將STRING資料轉成List Properties record data
				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
						GRID_FIELD_NAMES);
				// Get INDEX NO
				int indexNo = cmBlockDeclarationItemDAO.findPageLineMaxIndex(headId).intValue();
				if (upRecords != null) {
					for (Properties upRecord : upRecords) {
						// 先載入HEAD_ID OR LINE DATA
						Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
						log.info("updateAJAXPageLinesData.lineId:" + lineId);
						String declNo = upRecord.getProperty(GRID_FIELD_NAMES[1]);
						String declSeq = upRecord.getProperty(GRID_FIELD_NAMES[2]);
						
						// System.out.println("place ::::: " + place);
						if (StringUtils.hasText(declNo) && StringUtils.hasText(declSeq)) {
							Double blockOnHandQuantity = Double.parseDouble(upRecord.getProperty(GRID_FIELD_NAMES[7]) == null
									|| "".equals(upRecord.getProperty(GRID_FIELD_NAMES[7])) ? "0" : upRecord
									.getProperty(GRID_FIELD_NAMES[7]));

							CmBlockDeclarationItem cmBlockDeclarationItem = cmBlockDeclarationHeadDAO
									.findExecuteByIdentification(cmBlockDeclarationHead.getHeadId(), lineId);
							if (cmBlockDeclarationItem != null) {
								System.out.println("------------ 更新 -----------");
								AjaxUtils.setPojoProperties(cmBlockDeclarationItem, upRecord, GRID_FIELD_NAMES,
										GRID_FIELD_TYPES);
								cmBlockDeclarationHeadDAO.update(cmBlockDeclarationItem);
							} else {
								System.out.println("------------ 新增 -----------");
								indexNo++;
								CmBlockDeclarationItem newCmBlockDeclarationItem = new CmBlockDeclarationItem();
								AjaxUtils.setPojoProperties(newCmBlockDeclarationItem, upRecord, GRID_FIELD_NAMES,
										GRID_FIELD_TYPES);
								newCmBlockDeclarationItem.setIndexNo(Long.valueOf(indexNo));
								newCmBlockDeclarationItem.setCmBlockDeclarationHead(cmBlockDeclarationHead);
								cmBlockDeclarationHeadDAO.save(newCmBlockDeclarationItem);
							}
						}
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("更新報單鎖定作業單據發生錯誤，原因：" + ex.toString());
			throw new Exception("更新報單鎖定作業單據失敗！");
		}
	}
	
	/**
	 * 匯入鎖定報單明細資料
	 *
	 * @param headId
	 * @param promotionItems
	 * @throws Exception
	 */
	public void executeImportMovementItems(Long headId, List cmBlockDeclarationItems) throws Exception {

		log.info("cmBlockDeclarationItem HEAD_ID：" + headId);

		List<CmBlockDeclarationItem> newCmBlockDeclarationItems = new ArrayList(0);
		try {
			CmBlockDeclarationHead cmBlockDeclarationHead = cmBlockDeclarationHeadDAO.findById(headId);
			if (cmBlockDeclarationHead == null)
				throw new NoSuchObjectException("查無鎖報單單據主鍵：" + headId + "的資料");

			log.info("cmBlockDeclarationItem.size()" + cmBlockDeclarationHead.getCmBlockDeclarationItems().size());
			if (cmBlockDeclarationItems != null && cmBlockDeclarationItems.size() > 0) {
				for (int i = 0; i < cmBlockDeclarationItems.size(); i++) {
					CmBlockDeclarationItem cmBlockDeclarationItem = (CmBlockDeclarationItem) cmBlockDeclarationItems.get(i);
					cmBlockDeclarationItem.setIndexNo(i + 1L);
					newCmBlockDeclarationItems.add(cmBlockDeclarationItem);
				}
				cmBlockDeclarationHead.setCmBlockDeclarationItems(newCmBlockDeclarationItems);
			} else {
				cmBlockDeclarationHead.setCmBlockDeclarationItems(new ArrayList(0));
			}
			cmBlockDeclarationHeadDAO.update(cmBlockDeclarationHead);
		} catch (NoSuchObjectException ns) {
			log.error("匯入鎖定報單明細失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (Exception ex) {
			log.error("鎖定報單明細匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("鎖定報單明細匯入時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 載入鎖定報單明細資料
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			log.info("CmBlockDeclarationItem.headId ======> " + headId);

			List<CmBlockDeclarationItem> cmBlockDeclarationItems = cmBlockDeclarationItemDAO.findPageLine(headId, iSPage,
					iPSize);
			if (cmBlockDeclarationItems != null && cmBlockDeclarationItems.size() > 0) {
		
				// 取得第一筆的INDEX
				Long firstIndex = cmBlockDeclarationItems.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = cmBlockDeclarationItemDAO.findPageLineMaxIndex(headId);
				StringBuffer sbMsg = new StringBuffer();
				for (int i = 0; i < cmBlockDeclarationItems.size(); i++) {
					CmBlockDeclarationItem cmBlockDeclarationItem = (CmBlockDeclarationItem)cmBlockDeclarationItems.get(i);
					List<CmDeclarationOnHandView> cmDeclarationOnHandViews = cmDeclarationOnHandViewDAO.findByProperty(
							"CmDeclarationOnHandView",
							" AND declarationNo = ? AND declarationSeq = ?  AND customsItemCode = ? AND brandCode = ? AND customsWarehouseCode = ? ",
							new Object[] { cmBlockDeclarationItem.getDeclarationNo(),
									cmBlockDeclarationItem.getDeclarationSeq(), cmBlockDeclarationItem.getCustomsItemCode(),
									"T2", "FW" }); // 僅抓出FW關別的資料 update by Weichun 2012.03.23
					if (cmDeclarationOnHandViews.size() > 0) {
						CmDeclarationOnHandView cmDeclarationOnHandView = (CmDeclarationOnHandView) cmDeclarationOnHandViews
								.get(0);
						cmBlockDeclarationItem.setDeclDate(cmDeclarationOnHandView.getDeclDate()); // 報關日期
						cmBlockDeclarationItem.setOrderNo(cmDeclarationOnHandView.getOrderNo()); // 進貨單號
						cmBlockDeclarationItem.setCurrentOnHandQty(cmDeclarationOnHandView.getCurrentOnHandQty()
								- cmDeclarationOnHandView.getBlockOnHandQuantity()); // 可被鎖定的庫存量
					} else {
						if (sbMsg.length() > 0)
							sbMsg.append("\n");
						sbMsg.append("查無報單單號：" + cmBlockDeclarationItem.getDeclarationNo() + "，報單項次："
								+ cmBlockDeclarationItem.getDeclarationSeq() + "明細失敗！");
					}
				}
				if (sbMsg.length() > 0) // 有錯誤訊息 throw exception
					throw new Exception(sbMsg.toString());
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
						cmBlockDeclarationItems, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("CmBlockDeclaration.AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
						gridDatas));

			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("載入頁面顯示的鎖報單明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的鎖報單明細失敗！");
		}
	}
	
	
	/**
	 * 依報單號碼、報單項次、品號取得進貨日期、進貨單號、庫存 
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXItemData(Properties httpRequest) throws Exception {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
			String brandCode = httpRequest.getProperty("brandCode");
			String declarationNo = httpRequest.getProperty("declarationNo").trim();
			String declarationSeq = httpRequest.getProperty("declarationSeq").trim();
			String customsItemCode = httpRequest.getProperty("customsItemCode").trim();
			
			String declDate = null;
			String currentOnHandQty = null;
			String orderNo = null;
			System.out.println(brandCode + " 報單號碼<" + declarationNo + ">，報單項次<" + declarationSeq + ">，品號：<"
					+ customsItemCode + ">");
			if (StringUtils.hasText(declarationNo) && StringUtils.hasText(declarationSeq)
					&& StringUtils.hasText(customsItemCode)) {
				List<CmDeclarationOnHandView> cmDeclarationOnHandViews = cmDeclarationOnHandViewDAO.findByProperty(
						"CmDeclarationOnHandView",
						" AND declarationNo = ? AND declarationSeq = ?  AND customsItemCode = ? AND brandCode = ? AND customsWarehouseCode = ? ",
						new Object[] { declarationNo, Long.parseLong(declarationSeq), customsItemCode, "T2", "FW" }); // 僅抓出FW關別的資料 update by Weichun 2012.03.23
				if (null != cmDeclarationOnHandViews && cmDeclarationOnHandViews.size() > 0) {
					CmDeclarationOnHandView cmDeclarationOnHandView = (CmDeclarationOnHandView) cmDeclarationOnHandViews
							.get(0);
					declDate = DateUtils.format(cmDeclarationOnHandView.getDeclDate(), DateUtils.C_DATE_PATTON_SLASH);
					currentOnHandQty = String.valueOf(cmDeclarationOnHandView.getCurrentOnHandQty()
							- cmDeclarationOnHandView.getBlockOnHandQuantity());
					orderNo = cmDeclarationOnHandView.getOrderNo() == null ? "" : cmDeclarationOnHandView.getOrderNo();
				} else {
					declDate = "";
					currentOnHandQty = "";
					orderNo = "";
				}
				properties.setProperty("declDate", declDate);
				properties.setProperty("currentOnHandQty", currentOnHandQty);
				properties.setProperty("orderNo", orderNo);
				
			} else {
				properties.setProperty("declDate", "");
				properties.setProperty("currentOnHandQty", "");
				properties.setProperty("orderNo", "");
			}
			result.add(properties);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("取得報單相關欄位資料發生錯誤，原因：" + ex.toString());
			throw new Exception("取得報單相關欄位資料失敗！");
		}
	}
	
	/**
	 * 啟動流程
	 *
	 * @param form
	 * @return
	 * @throws ProcessFailedException
	 */
	public static Object[] startProcess(CmBlockDeclarationHead form) throws ProcessFailedException {

		try {
			String packageId = "Cm_Declaration";
			String processId = "block";
			String version = "20120302";
			String sourceReferenceType = "ctProcess";
			HashMap context = new HashMap();
			context.put("formId", form.getHeadId());
			context.put("orderType", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());
			return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("報單鎖定作業流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("報單鎖定作業流程啟動失敗！" + ex.getMessage());
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
	 * ajax 取得報單鎖定作業 search分頁
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
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");// 單別
			String orderNo = httpRequest.getProperty("orderNo"); // 單號
			String status = httpRequest.getProperty("status"); // 單據狀態
			String orderBy = httpRequest.getProperty("orderBy") == null || "".equals(httpRequest.getProperty("orderBy")) ? "lastUpdateDate"
					: httpRequest.getProperty("orderBy");

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			if (orderTypeCode != null && "".equals(orderTypeCode)) {
				findObjs.put(" and model.orderTypeCode LIKE :orderTypeCode", orderTypeCode + "%");
			}
			if (orderNo != null && orderNo.length() > 0)
				findObjs.put(" and model.orderNo = :orderNo", orderNo);
			else
				findObjs.put(" and model.orderNo NOT LIKE :TMP", "TMP%");
			
			findObjs.put(" and model.status = :status", status);

			Map cmBlockDeclarationHeadMap = cmBlockDeclarationHeadDAO.search("CmBlockDeclarationHead as model", findObjs, "order by " + orderBy + " desc",
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<CmBlockDeclarationHead> cmBlockDeclarationHeads = (List<CmBlockDeclarationHead>) cmBlockDeclarationHeadMap.get(BaseDAO.TABLE_LIST);

			if (cmBlockDeclarationHeads != null && cmBlockDeclarationHeads.size() > 0) {
				this.CmBlockDeclarationHeadStatusName(cmBlockDeclarationHeads);

				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) cmBlockDeclarationHeadDAO.search("CmBlockDeclarationHead as model",
						"count(model.orderNo) as rowCount", findObjs, "order by " + orderBy + " desc",
						BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆
				// INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						cmBlockDeclarationHeads, gridDatas, firstIndex, maxIndex));
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
	 * 將單據查詢結果存檔
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
	 * 設定中文狀態名稱
	 *
	 * @param cmMovementHeads
	 */
	private void CmBlockDeclarationHeadStatusName(List<CmBlockDeclarationHead> cmBlockDeclarationHeads) {
		for (CmBlockDeclarationHead cmBlockDeclarationHead : cmBlockDeclarationHeads) {
			cmBlockDeclarationHead.setStatusName(OrderStatus.getChineseWord(cmBlockDeclarationHead.getStatus()));
		}
	}
	
	/**
	 * 取得單據內容
	 * 
	 * @param headId
	 * @return
	 */
	public CmBlockDeclarationHead findById(Long headId) {
		CmBlockDeclarationHead cmBlockDeclarationHead = cmBlockDeclarationHeadDAO.findById(headId);
		List<CmBlockDeclarationItem> cmBlockDeclarationItems = cmBlockDeclarationHead.getCmBlockDeclarationItems();
		for (int i = 0; i < cmBlockDeclarationItems.size(); i++) {
			CmBlockDeclarationItem cmBlockDeclarationItem = (CmBlockDeclarationItem)cmBlockDeclarationItems.get(i);
			ImItem imItem = imItemDAO.findItem("T2", cmBlockDeclarationItem.getCustomsItemCode()); 
			cmBlockDeclarationItem.setCustomsItemName(imItem.getItemCName());
		}
		return cmBlockDeclarationHead;
	}
}
