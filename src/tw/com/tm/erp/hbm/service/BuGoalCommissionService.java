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
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuGoalCommission;
import tw.com.tm.erp.hbm.bean.BuGoalCommissionException;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuGoalCommissionDAO;
import tw.com.tm.erp.hbm.dao.BuGoalCommissionExceptionDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuGoalCommissionService {

	private static final Log log = LogFactory.getLog(BuGoalCommissionService.class);

	private BuGoalCommissionDAO buGoalCommissionDAO;

	public void setBuGoalCommissionDAO(BuGoalCommissionDAO buGoalCommissionDAO) {
		this.buGoalCommissionDAO = buGoalCommissionDAO;
	}
	private BuGoalCommissionExceptionDAO buGoalCommissionExceptionDAO;

	public void setBuGoalCommissionExceptionDAO(BuGoalCommissionExceptionDAO buGoalCommissionExceptionDAO) {
		this.buGoalCommissionExceptionDAO = buGoalCommissionExceptionDAO;
	}

	public static final Object[][] MASTER_DEFINITION = {
			{ "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
			{ "typeId", AjaxUtils.FIELD_TYPE_LONG, "0", "mode:HIDDEN", "" },
			{ "commissionType", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
			{ "category02", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
			{ "commissionRate", AjaxUtils.FIELD_TYPE_DOUBLE, "0", "0", "0" },
			{ "lastUpdatedBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
			{ "lastUpdateDate", AjaxUtils.FIELD_TYPE_DATE, "", "", "" } };

	/**
	 * 查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { "commissionType",
			"category02", "commissionRate", "lastUpdatedByName",
			"lastUpdateDate", "typeId" };//

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
			"0", "", "", "" };

	/**
	 * 查詢Exception用的欄位
	 */
	public static final String[] GRID_SEARCH_EXCEPTION_FIELD_NAMES = { 
		 "commissionType", "shopCode", "itemBrand","commissionRate",
		 "lastUpdatedByName", "lastUpdateDate", "lineId"
	 }; 
	
	public static final String[] GRID_SEARCH_EXCEPTION_DEFAULT_VALUES = { 
		"", "", "", "", 
		"", "", "" };
	
	/**
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	//執行初始化
	public Map executeInitial(Map parameterMap) throws Exception {

		Map resultMap = new HashMap(0);

		try {
			
			BuGoalCommission buGoalCommission = this.executeFindActualBuGoalCommission(parameterMap);
			
			Map multiList = new HashMap(0);
			resultMap.put("form", buGoalCommission);
			resultMap.put("multiList", multiList);
			return resultMap;

		} catch (Exception ex) {
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}

	/**
	 * 依formId取得實際主檔 in 送出
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuGoalCommission executeFindActualBuGoalCommission(Map parameterMap)
			throws FormException, Exception {

		// Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuGoalCommission buGoalCommission = null;
		try {
			//接值 FormIdString  =  轉型     formId取值
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			//變數formId =  運算式(1) formIdString 有值(true) 就執行運算式(2)轉型long formIdString回傳變數  運算式(3) 運算式(1)為false 就執行NULL  回傳給變數 				
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			//buGoalCommission = null 就NEW一筆新的  如果有依據主鍵找到formId
			buGoalCommission = null == formId ? this.executeNewBuGoalCommission() : this.findById(formId);
			//Map塞值到entityBean
			parameterMap.put("entityBean", buGoalCommission);
			//回傳 
			return buGoalCommission;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}

	/**
	 * 產生一筆 BuCommission
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	private BuGoalCommission executeNewBuGoalCommission() throws Exception {
		//NEW一筆新的commission
		BuGoalCommission form = new BuGoalCommission();
		//將commission的commissionType設null
		form.setCommissionType(null);
		//將commission的Category02設null
		form.setCategory02(null);
		//將commission的CommissionRate設null
		form.setCommissionRate(null);
		return form;
	}
	
	private BuGoalCommission findById(Long typeId) throws Exception {
		try {//依據headId找出資料
			BuGoalCommission goalcommission = (BuGoalCommission) buGoalCommissionDAO//利用Class findClass, Serializable pk找出BuGoalCommission
					.findByPrimaryKey(BuGoalCommission.class, typeId);
			return goalcommission;
		} catch (Exception ex) {
			log.error("依據主鍵：" + typeId + "查詢資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + typeId + "查詢資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	public List<BuGoalCommission> find(BuGoalCommission buGoalCommission)
			throws IllegalAccessException, InvocationTargetException,
			IllegalArgumentException, SecurityException, NoSuchMethodException,
			ClassNotFoundException {
		log.info("BuGoalCommissionService.find");
		BuGoalCommission searchObj = new BuGoalCommission();
		BeanUtils.copyProperties(buGoalCommission, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getTypeId()) {
			temp.add(buGoalCommissionDAO.findByPrimaryKey(
					BuGoalCommission.class, buGoalCommission.getTypeId()));
		} else {
			temp = buGoalCommissionDAO.findByExample(searchObj);
		}
		return temp;
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
				//驗證單頭
	public void validateHead(Map parameterMap) throws Exception {
				
		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");
		//接值 FormIdString  =  轉型     根據KEY formId取出 otherbean的值
		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
		//formId變數 = formIdString有值 					執行formIdString轉型回傳		若沒值=null
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		//取得JS接收的commissionType
		String commissionType = (String) PropertyUtils.getProperty(formBindBean, "commissionType");
		//取得JS接收的category02
		String category02 = (String) PropertyUtils.getProperty(formBindBean,"category02");
		//取得JS接收的commissionRate
		String commissionRate = (String) PropertyUtils.getProperty(formBindBean,"commissionRate");

		log.info("formId = " + formId);

		// 驗證名稱 (如果commissionType沒值跳出ValidationErrorException )
		if (!StringUtils.hasText(commissionType)) {
			throw new ValidationErrorException("請輸入佣金分類！");
		} /*else {
			if (formId == null) {
				BuGoalCommission commissionPO = buGoalCommissionDAO
						.findBycommissionType(commissionType.trim()
								.toUpperCase());
				if (commissionPO != null) {
					throw new ValidationErrorException("名稱：" + commissionType
							+ "已經存在，請勿重複建立！");
				}
			}
		}*/
		if (!StringUtils.hasText(category02)) {
			throw new ValidationErrorException("請輸入類別！");
		}
		if (!StringUtils.hasText(commissionRate)) {
			throw new ValidationErrorException("請輸入抽成率！");
		}
	}

	/**
	 * 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalCommission(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {

			// Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			//otherBean取得登入傳入的值
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			
			BuGoalCommission buGoalCommission = (BuGoalCommission) parameterMap.get("entityBean");

			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			// 存檔or更新
	//		buGoalCommissionDAO.saveOrUpdate(buGoalCommission);
			//如果formAction = FORM_SUBMIT 
			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				//buGoalCommission存CommissionType                          回傳去除首尾空白符號的子字串   將字串中所有英文字母改為大寫
				buGoalCommission.setCommissionType(buGoalCommission.getCommissionType().trim().toUpperCase());
				buGoalCommission.setCategory02(buGoalCommission.getCategory02().trim());
				//如果buGoalCommission 取得typeId = NULL
				if (buGoalCommission.getTypeId() == null) {
					//buGoalCommission存最後更新人員(登入人員)、日期
					buGoalCommission.setLastUpdatedBy(loginEmployeeCode);
					buGoalCommission.setLastUpdateDate(date);
					//buGoalCommissionDAO 存檔
					buGoalCommissionDAO.save(buGoalCommission);
				} else {//如果有typeId則更新
					buGoalCommissionDAO.update(buGoalCommission);
				}
			}
			
			resultMsg = "commissionType："
					+ buGoalCommission.getCommissionType() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buGoalCommission);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateBuGoalCommissionBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub

		BuGoalCommission buGoalCommission = null;
		try {	  //(欄位輸入的值)
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");

			buGoalCommission = (BuGoalCommission) parameterMap.get("entityBean");
					//將JS的物件轉成JAVA的BEAN				    
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalCommission);
			
			//塞入的值傳入buGoalCommission
			parameterMap.put("entityBean", buGoalCommission);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

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
			System.out.println("searchKeysssss:" + searchKeys.get(0));
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			if (result.size() > 0)
				pickerResult.put("result", result);
			 else{
				 Map messageMap = new HashMap();
				 messageMap.put("type"   , "ALERT");
				 messageMap.put("message", "請選擇檢視項目！");
				 messageMap.put("event1" , null);
				 messageMap.put("event2" , null);
				 resultMap.put("vatMessage",messageMap);
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

			String commissionType = httpRequest.getProperty("commissionType");
			String category02 = httpRequest.getProperty("category02");

			log.info("commissionType:" + commissionType);
			log.info("category02:" + category02);
		

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.commissionType = :commissionType",commissionType);
			findObjs.put(" and model.category02 = :category02",category02 );
			
			// ==============================================================

			Map buGoalCommissionMap = buGoalCommissionDAO.search("BuGoalCommission as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
			
			List<BuGoalCommission> buGoalCommissions = (List<BuGoalCommission>) buGoalCommissionMap
					.get(BaseDAO.TABLE_LIST);
		
			
		
			log.info("BuGoalCommission.size" + buGoalCommissions.size());
			if (buGoalCommissions != null && buGoalCommissions.size() > 0) {

				// 設定額外欄位
				this.setLineOtherColumn(buGoalCommissions);
				
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) buGoalCommissionDAO.search("BuGoalCommission as model","count(model.typeId) as rowCount", findObjs,
						"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); 
																												// 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, buGoalCommissions,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的功能查詢失敗！");
		}
	}

	/**
	 * 設定額外欄位
	 * 
	 * @param buGoalCommissions
	 */
	private void setLineOtherColumn(List<BuGoalCommission> buGoalCommissions) {
		for (BuGoalCommission buGoalCommission : buGoalCommissions) {
			buGoalCommission.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buGoalCommission.getLastUpdatedBy()));
		}
	}

	/**
	 * 將主檔查詢結果存檔
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		//Exception存檔
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_EXCEPTION_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	//------------------------------------------------------- 
	/**
	 * 初始化Exception bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */

	public Map executeExInitial(Map parameterMap) throws Exception {

		Map resultMap = new HashMap(0);

		try {

			BuGoalCommissionException buGoalCommissionException = this.executeFindActualBuGoalCommissionEx(parameterMap);

			Map multiList = new HashMap(0);
			resultMap.put("form", buGoalCommissionException);
			resultMap.put("multiList", multiList);
			return resultMap;

		} catch (Exception ex) {
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}
	
	/**
	 * 依formId取得實際地點主檔 in 送出
	 * CommissionException
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuGoalCommissionException executeFindActualBuGoalCommissionEx(Map parameterMap)
			throws FormException, Exception {

		// Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuGoalCommissionException buGoalCommissionException = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;

			buGoalCommissionException = null == formId ? this.executeNewBuGoalCommissionEx() : this.findByExId(formId);

			parameterMap.put("entityBean", buGoalCommissionException);

			return buGoalCommissionException;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際主檔失敗,原因:" + e.toString());
		}
	}
	/**
	 * 產生一筆 CommissionException
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	private BuGoalCommissionException executeNewBuGoalCommissionEx() throws Exception {
		BuGoalCommissionException form = new BuGoalCommissionException();
		form.setCommissionType(null);
		form.setShopCode(null);
		form.setItemBrand(null);
		form.setCommissionRate(null);
		return form;
	}
	
	private BuGoalCommissionException findByExId(Long lineId) throws Exception {
		try {
			BuGoalCommissionException goalcommissionex = (BuGoalCommissionException) buGoalCommissionExceptionDAO
					.findByPrimaryKey(BuGoalCommissionException.class, lineId);
			return goalcommissionex;
		} catch (Exception ex) {
			log.error("依據主鍵：" + lineId + "查詢資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + lineId + "查詢資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
	
	

	
	
	/**
	 * CommissionException驗證主檔
	 * 
	 * @param parameterMap
	 * @throws ValidationErrorException
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	public void validateHeadEx(Map parameterMap) throws Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");

		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

		String commissionType = (String) PropertyUtils.getProperty(formBindBean, "commissionType");
		
		String shopCode =  (String) PropertyUtils.getProperty(formBindBean, "shopCode");
		
		String commissionRate =  (String) PropertyUtils.getProperty(formBindBean, "commissionRate");
		log.info("formId = " + formId);

		// 驗證名稱
		if (!StringUtils.hasText(commissionType)) {
			throw new ValidationErrorException("請輸入商品類別！");
		}
		if (!StringUtils.hasText(shopCode)) {
			throw new ValidationErrorException("請輸入店別！");
		}
		if (!StringUtils.hasText(commissionRate)) {
			throw new ValidationErrorException("請輸入抽成率！");
		}
	}	
	/**
	 * CommissionException前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateBuGoalCommissionExBean(Map parameterMap)
			throws FormException, Exception {
		// TODO Auto-generated method stub(???)

		BuGoalCommissionException buGoalCommissionException = null;
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			// Object otherBean = parameterMap.get("vatBeanOther");

			buGoalCommissionException = (BuGoalCommissionException) parameterMap.get("entityBean");

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalCommissionException);
			// log.info("buGoalCommission.getEnable"+
			// buGoalCommission.getEnable());

			parameterMap.put("entityBean", buGoalCommissionException);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}

	}
	/**
	 *CommissionException 存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalCommissionException(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {

			// Object formBindBean = parameterMap.get("vatBeanFormBind");
			// Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			BuGoalCommissionException buGoalCommissionException = (BuGoalCommissionException) parameterMap.get("entityBean");

			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");
			// 存檔
			buGoalCommissionExceptionDAO.saveOrUpdate(buGoalCommissionException);

			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {

				// log.info("buCommission.getEnable() = " +
				// buCommission.getEnable());
				// buCommission.setEnable(("N".equals(buCommission.getEnable())
				// ? "Y" : "N"));
				buGoalCommissionException.setCommissionType(buGoalCommissionException.getCommissionType().trim().toUpperCase());
				buGoalCommissionException.setShopCode(buGoalCommissionException.getShopCode().trim());

				buGoalCommissionException.setLastUpdatedBy(loginEmployeeCode);
				buGoalCommissionException.setLastUpdateDate(date);

				if (buGoalCommissionException.getLineId() == null) {
					// buCommission.setCreatedBy(loginEmployeeCode);
					// buCommission.setCreationDate(date);
					buGoalCommissionExceptionDAO.save(buGoalCommissionException);
				} else {
					buGoalCommissionExceptionDAO.update(buGoalCommissionException);
				}
			}

			resultMsg = "commissionType："
					+ buGoalCommissionException.getCommissionType() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buGoalCommissionException);
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
	public List<Properties> getAJAXSearchExPageData(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			// ======================帶入Head的值=========================

			String commissionType = httpRequest.getProperty("commissionType");
			String shopCode = httpRequest.getProperty("shopCode");

			log.info("commissionType:" + commissionType);
			log.info("shopCode:" + shopCode);
		

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.commissionType = :commissionType", commissionType);
			findObjs.put(" and model.shopCode = :shopCode", shopCode );
			
			// ==============================================================

			Map buGoalCommissionExMap = buGoalCommissionExceptionDAO.search("BuGoalCommissionException as model", findObjs,
					"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
			
			List<BuGoalCommissionException> buGoalCommissionsEx = (List<BuGoalCommissionException>) buGoalCommissionExMap
					.get(BaseDAO.TABLE_LIST);
		
			
		
			log.info("BuGoalCommissionException.size" + buGoalCommissionsEx.size());
			if (buGoalCommissionsEx != null && buGoalCommissionsEx.size() > 0) {

				// 設定額外欄位
				this.setLineOtherColumnEx(buGoalCommissionsEx);
				
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) buGoalCommissionExceptionDAO.search("BuGoalCommissionException as model","count(model.lineId) as rowCount", findObjs,
						"order by lastUpdateDate desc", iSPage, iPSize,BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT); 
																												// 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_EXCEPTION_FIELD_NAMES,
						GRID_SEARCH_EXCEPTION_DEFAULT_VALUES, buGoalCommissionsEx,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_EXCEPTION_FIELD_NAMES,
						GRID_SEARCH_EXCEPTION_DEFAULT_VALUES, map, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的功能查詢失敗！");
		}
	}
	/**
	 * 設定額外欄位
	 * 
	 * @param buGoalCommissions
	 */
	private void setLineOtherColumnEx(List<BuGoalCommissionException> buGoalCommissionsEx) {
		for (BuGoalCommissionException buGoalCommissionException : buGoalCommissionsEx) {
			buGoalCommissionException.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buGoalCommissionException.getLastUpdatedBy()));
		}
	}

	
	
}