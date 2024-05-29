package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
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
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImMovementOrderTypeSetting;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImMovementOrderTypeSettingDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;



public class ImMovementOrderTypeSettingService {

	private static final Log log = LogFactory.getLog(ImMovementOrderTypeSettingService.class);

	private ImMovementOrderTypeSettingDAO imMovementOrderTypeSettingDAO;

	public void setImMovementOrderTypeSettingDAO(ImMovementOrderTypeSettingDAO imMovementOrderTypeSettingDAO) {
		this.imMovementOrderTypeSettingDAO = imMovementOrderTypeSettingDAO;
	}
	
	/**
	 * 查詢Exception用的欄位
	 */
	public static final String[] GRID_SEARCH_SETTING_FIELD_NAMES = { 
		 "orderTypeCode", "name", "deliveryWarehouses","arrivalWarehouses",
		 "lastUpdatedBy", "lastUpdateDate", "orderTypeCode"
	 }; 
	
	public static final String[] GRID_SEARCH_SETTING_DEFAULT_VALUES = { 
		"", "", "", "", 
		"", "", "" };
	
	/**
	 * 初始化immovementordertypesetting bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */

	public Map executeInitial(Map parameterMap) throws Exception {

		Map resultMap = new HashMap(0);

		try {

			ImMovementOrderTypeSetting imMovementOrderTypeSetting = this.executeFindActual(parameterMap);

			Map multiList = new HashMap(0);
			resultMap.put("form", imMovementOrderTypeSetting);
			resultMap.put("multiList", multiList);
			return resultMap;

		} catch (Exception ex) {
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}
	
	/**
	 * 依formId取得實際地點主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public ImMovementOrderTypeSetting executeFindActual(Map parameterMap)
			throws FormException, Exception {

		// Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		ImMovementOrderTypeSetting imMovementOrderTypeSetting = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

			String formId = StringUtils.hasText(formIdString) ? String.valueOf(formIdString) : null;

			imMovementOrderTypeSetting = null == formId ? this.executeNew() : this.findById(formId);

			parameterMap.put("entityBean", imMovementOrderTypeSetting);

			return imMovementOrderTypeSetting;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	
	/**
	 * 產生一筆 ImMovementOrderTypeSetting
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	private ImMovementOrderTypeSetting executeNew() throws Exception {
		ImMovementOrderTypeSetting form = new ImMovementOrderTypeSetting();
		form.setOrderTypeCode(null);
		form.setName(null);
		form.setDeliveryWarehouses(null);
		form.setArrivalWarehouses(null);
		form.setItemCategorymode(null);
		form.setOverOricount(null);
		form.setTypeOfIMV(null);
		form.setCheckItemcategory(null);
		form.setBevoid(null);
		form.setEnable(null);
		form.setShowOricount(null);
		return form;
	}

	private ImMovementOrderTypeSetting findById(String orderTypeCode) throws Exception {
		try {
			ImMovementOrderTypeSetting immovementordertype = (ImMovementOrderTypeSetting) imMovementOrderTypeSettingDAO
					.findByPrimaryKey(ImMovementOrderTypeSetting.class, orderTypeCode);
			return immovementordertype;
		} catch (Exception ex) {
			log.error("依據主鍵：" + orderTypeCode + "查詢資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + orderTypeCode + "查詢資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
	
	/**
	  * 依據primary key為查詢條件，取得國別資料
	  * 
	  * @param id
	  * @return BuCurrency
	  * @throws Exception
	  */
	 public ImMovementOrderTypeSetting findImMovementOrderTypeById(String orderTypeCode) throws Exception{
		 try {
			 orderTypeCode = orderTypeCode.trim().toUpperCase();
			 return imMovementOrderTypeSettingDAO.findById(orderTypeCode);
		 } catch (Exception ex) {
			 log.error("依據代碼：" + orderTypeCode + "查詢資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據代碼：" + orderTypeCode + "查詢資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	 }
	
	
	
	/**
	 * 驗證主檔
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	public void validateHead(Map parameterMap) throws Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

		String formId = StringUtils.hasText(formIdString) ? String.valueOf(formIdString) : null;

		String orderTypeCode = (String) PropertyUtils.getProperty(formBindBean, "orderTypeCode");
		

		log.info("formId = " + formId);

		// 驗證名稱
		if (!StringUtils.hasText(orderTypeCode)) {
			throw new ValidationErrorException("請輸入分類！");
		} else {
			if (formId == null) {
				ImMovementOrderTypeSetting movementordertypePO = imMovementOrderTypeSettingDAO
						.findByOrderTypeCode(orderTypeCode.trim()
								.toUpperCase());
				if (movementordertypePO != null) {
					throw new ValidationErrorException("名稱：" + orderTypeCode
							+ "已經存在，請勿重複建立！");
				}
			}
		}
	}
	
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateImMovementOrderTypeSettingBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub

		ImMovementOrderTypeSetting imMovementOrderTypeSetting = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");

			imMovementOrderTypeSetting = (ImMovementOrderTypeSetting) parameterMap.get("entityBean");

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, imMovementOrderTypeSetting);
			// log.info("buGoalCommission.getEnable"+
			// buGoalCommission.getEnable());

			parameterMap.put("entityBean", imMovementOrderTypeSetting);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}
	
	/**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXImMovementOrderSetting(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {

			// Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			ImMovementOrderTypeSetting imMovementOrderTypeSetting = (ImMovementOrderTypeSetting) parameterMap.get("entityBean");

			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			// 存檔
			//imMovementOrderTypeSettingDAO.saveOrUpdate(imMovementOrderTypeSetting);

			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {

				// log.info("imMovementOrderTypeSetting.getEnable() = " +
				// imMovementOrderTypeSetting.getEnable());
				// imMovementOrderTypeSetting.setEnable(("N".equals(imMovementOrderTypeSetting.getEnable())
				// ? "Y" : "N"));
				imMovementOrderTypeSetting.setOrderTypeCode(imMovementOrderTypeSetting
						.getOrderTypeCode().trim().toUpperCase());
				imMovementOrderTypeSetting.setName(imMovementOrderTypeSetting.getName()
						.trim());

				imMovementOrderTypeSetting.setLastUpdatedBy(loginEmployeeCode);
				imMovementOrderTypeSetting.setLastUpdateDate(date);
				
				ImMovementOrderTypeSetting imMovementOrderTypeSettingUpdate = this.findImMovementOrderTypeById(imMovementOrderTypeSetting.getOrderTypeCode());
				if (imMovementOrderTypeSettingUpdate == null) {
					imMovementOrderTypeSetting.setCreatedBy(loginEmployeeCode);
					imMovementOrderTypeSetting.setCreationDate(date);
					imMovementOrderTypeSettingDAO.save(imMovementOrderTypeSetting);
				} else {
					imMovementOrderTypeSettingDAO.update(imMovementOrderTypeSetting);
				}
			}

			resultMsg = "OrderTypeCode："
					+ imMovementOrderTypeSetting.getOrderTypeCode() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", imMovementOrderTypeSetting);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	/**
	 * ajax 取得查詢的結果
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

			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			String name = httpRequest.getProperty("name");
			String deliveryWarehouses = httpRequest.getProperty("deliveryWarehouses");
			String arrivalWarehouses = httpRequest.getProperty("arrivalWarehouses");
			

			log.info("orderTypeCode:" + orderTypeCode);
			log.info("name:" + name);
			log.info("deliveryWarehouses:" + deliveryWarehouses);
			log.info("arrivalWarehouses:" + arrivalWarehouses);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.orderTypeCode like :orderTypeCode", "%"
					+ orderTypeCode + "%");
			findObjs.put(" and model.name like :name", "%"
					+ name + "%");
			findObjs.put(" and model.deliveryWarehouses like :deliveryWarehouses", "%"
					+ deliveryWarehouses + "%");
			findObjs.put(" and model.arrivalWarehouses like :arrivalWarehouses", "%"
					+ arrivalWarehouses + "%");
			
			
			// ==============================================================

			Map imMovementOrderTypeSettingMap = imMovementOrderTypeSettingDAO.search("imMovementOrderTypeSetting as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
			
			List<ImMovementOrderTypeSetting> imMovementOrderTypeSettings = (List<ImMovementOrderTypeSetting>) imMovementOrderTypeSettingMap
					.get(BaseDAO.TABLE_LIST);
		
			
		
			log.info("imMovementOrderTypeSetting.size" + imMovementOrderTypeSettings.size());
			if (imMovementOrderTypeSettings != null && imMovementOrderTypeSettings.size() > 0) {

				// 設定額外欄位
				this.setLineOtherColumn(imMovementOrderTypeSettings);
				
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) imMovementOrderTypeSettingDAO.search("ImMovementOrderTypeSetting as model","count(model.orderTypeCode) as rowCount", findObjs,
						"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); 
																												// 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_SETTING_FIELD_NAMES,
						GRID_SEARCH_SETTING_DEFAULT_VALUES, imMovementOrderTypeSettings,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_SETTING_FIELD_NAMES,
						GRID_SEARCH_SETTING_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的功能查詢失敗！");
		}
	}
	private void setLineOtherColumn(List<ImMovementOrderTypeSetting> imMovementOrderTypeSettings) {
		for (ImMovementOrderTypeSetting imMovementOrderTypeSetting : imMovementOrderTypeSettings) {
			imMovementOrderTypeSetting.setLastUpdatedBy(UserUtils.getUsernameByEmployeeCode(imMovementOrderTypeSetting.getLastUpdatedBy()));
		}
	}

	
	
	
}
