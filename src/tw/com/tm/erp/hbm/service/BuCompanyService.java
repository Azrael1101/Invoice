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
import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuCompanyDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuCompanyService {

	private static final Log log = LogFactory.getLog(BuCompanyService.class);
	private BuCompanyDAO buCompanyDAO;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;

	public static final Object[][] MASTER_DEFINITION = {
		{ "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
		{ "companyCode", AjaxUtils.FIELD_TYPE_STRING, "0", "mode:HIDDEN ", "" },
		{ "companyName", AjaxUtils.FIELD_TYPE_STRING, "", " ", "" },
		{ "guiCode", AjaxUtils.FIELD_TYPE_STRING, "", "", "" } ,
		{ "businessMasterName", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "taxRegisterNo", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "registerMasterName", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "masterName", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "accreditee", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "address", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "tel", AjaxUtils.FIELD_TYPE_STRING, "", " ", "" },
		{ "reportTitle", AjaxUtils.FIELD_TYPE_STRING, " ", "", "" },
		{ "reserve1", AjaxUtils.FIELD_TYPE_STRING, " ", "", "" },
		{ "reserve2", AjaxUtils.FIELD_TYPE_STRING, " ", "", "" },
		{ "reserve3", AjaxUtils.FIELD_TYPE_STRING, " ", "", "" },
		{ "reserve4", AjaxUtils.FIELD_TYPE_STRING, " ", "", "" },
		{ "reserve5", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "createdBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "creationDate", AjaxUtils.FIELD_TYPE_DATE, "", "", "" },
		{ "lastUpdatedBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "lastUpdateDate", AjaxUtils.FIELD_TYPE_STRING, "", "", "" }};
	/**
	 * 公司查詢picker用的欄位
	 * 丟至前端的欄位
	 */
	public static final String[] GRID_SEARCH_COUNTRY_FIELD_NAMES = {
		"companyCode", "companyName", "businessMasterName", "guiCode",
		"lastUpdatedBy", "lastUpdateDate" };

	public static final String[] GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES = {
		"", "", "", "", "", "",};


	public void setBuCompanyDAO(BuCompanyDAO buCompanyDAO) {
		this.buCompanyDAO = buCompanyDAO;
	}
	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}
	/**
	 * 依據品牌代號及員工代號，查詢啟用狀態之員工資料
	 * 
	 * @param brandCode
	 * @param employeeCode
	 * @return BuEmployeeWithAddressView
	 */
	public BuEmployeeWithAddressView findEnableEmployeeById(
			String organizationCode, String employeeCode) {
		try {
			return buEmployeeWithAddressViewDAO.findEnableEmployeeById(
					organizationCode, employeeCode);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * ajaxpicker按檢視返回選取的資料
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
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);

			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,searchKeys);
			if (result.size() > 0) {
				pickerResult.put("result", result);
			}
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
	 * 初始化 bean 額外顯示欄位
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exceptio
	 * * get取得parameterMap元素
	 * 實作set的類別出來
	 */
	public Map executeBuCompanyInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		//Object otherBean = parameterMap.get("vatBeanOther");
		try {
			//String companyCode = (String) PropertyUtils.getProperty(otherBean, "formId");
			BuCompany buCompany = this.executeFindActualBuCompany(parameterMap);
			//如果沒有companyCode
			/*if(!StringUtils.hasText(companyCode)){
		log.info("沒有companyCode");
		buCompany = new BuCompany();
		buCompany.setCreationDate(new Date());

		String createdBy = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		buCompany.setCreatedBy(createdBy);
		resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(createdBy));


	    }else{
		log.info("有companyCode");

		String lastUpdatedBy = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		buCompany.setLastUpdatedBy(lastUpdatedBy);
		resultMap.put("lastUpdateByName", UserUtils.getUsernameByEmployeeCode(lastUpdatedBy));

		buCompany = (BuCompany)buCompanyDAO.findById("BuCompany", companyCode);
		resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(buCompany.getCreatedBy()));
		resultMap.put("lastUpdateByName", UserUtils.getUsernameByEmployeeCode(buCompany.getLastUpdatedBy()));
	    }*/
			//Map multiList = new HashMap(0);
			resultMap.put("form", buCompany);
			//resultMap.put("multiList", multiList);
			return resultMap;
		} catch (Exception ex) {
			log.error("公司初始化失敗，原因：" + ex.toString());
			throw new Exception("公司單初始化失敗，原因：" + ex.toString());
		}
	}
	/**
	 * 依companyCode取得實際公司主檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuCompany executeFindActualBuCompany(Map parameterMap)
	throws FormException, Exception {
		Object otherBean = parameterMap.get("vatBeanOther");
		BuCompany buCompany = null;
		try {
			String formIdString = (String) PropertyUtils.getProperty(otherBean , "formId");
			log.info(formIdString + "#####################");
			//String companyCode = StringUtils.hasText(formIdString) ? (String)(formIdString) : null;
			buCompany = !StringUtils.hasText(formIdString)? this.executeNewBuCompany(): this.findCompanyById(formIdString) ;
			parameterMap.put( "entityBean", buCompany);
			return buCompany;
		} catch (Exception e) {
			log.error("取得實際公司主檔失敗,原因:" + e.toString());
			throw new Exception("取得實際公司主檔失敗,原因:" + e.toString());
		}
	}
	
	public void updateBuCurrencyBean(Map parameterMap)throws FormException, Exception {
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			String currencyCode = (String) PropertyUtils.getProperty(formBindBean, "currencyCode");
			BuCompany buCompany = this.findCompanyById(currencyCode);
			if(buCompany == null) buCompany = new BuCompany();
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCompany);

			//log.info("buCountry.getCurrencyCode"+ buCompany.getCurrencyCode());
			//log.info("buLocation.getEnable"+ buCompany.getEnable());
			parameterMap.put("entityBean", buCompany);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("幣別資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public BuCompany findById(String companyCode) throws Exception {

		try {
			BuCompany com = (BuCompany) buCompanyDAO.findByPrimaryKey(
					BuCompany.class, companyCode);
			return com;
		} catch (Exception ex) {
			log.error("依據主鍵：" + companyCode + "查詢地點資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + companyCode + "查詢地點資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
	public List<BuCompany> find(BuCompany buCompany)
	throws IllegalAccessException, InvocationTargetException,
	IllegalArgumentException, SecurityException, NoSuchMethodException,
	ClassNotFoundException {
		log.info("BuCompanyService.find");
		BuCompany searchObj = new BuCompany();
		BeanUtils.copyProperties(buCompany, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getCompanyCode()) {
			temp.add(buCompanyDAO.findByPrimaryKey(BuCompany.class,
					buCompany.getCompanyCode()));
		} else {
			temp = buCompanyDAO.findByExample(searchObj);
		}
		return temp;
	}
	/**
	 * 產生一筆 BuCompany
	 * 
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 * get取得parameterMap元素
	 * 實作set的類別出來
	 */
	public BuCompany executeNewBuCompany() throws Exception {
		BuCompany form = new BuCompany();
		form.setCompanyCode(null);
		form.setCompanyName(null);
		form.setGuiCode(null);
		form.setBusinessMasterName(null);
		form.setTaxRegisterNo(null);
		form.setRegisterMasterName(null);
		form.setMasterName(null);
		form.setAccreditee(null);
		form.setAddress(null);
		form.setTel(null);
		form.setReportTitle(null);
		form.setReserve1(null);
		form.setReserve2(null);
		form.setReserve3(null);
		form.setReserve4(null);
		form.setReserve5(null);
		form.setCreatedBy(null);
		form.setCreationDate(null);
		form.setLastUpdatedBy(null);
		form.setLastUpdateDate(null);
		return form;
	}
	public List<Properties> executeInitial_transfer(Map parameterMap)
	throws Exception {

		HashMap returnMap = new HashMap();
		Map multiList = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String applyBy = (String) PropertyUtils.getProperty(otherBean,
			"loginEmployeeCode");
			// 登入人員
			// ================================================================
			returnMap.put("loginEmployeeName", UserUtils.getUsernameByEmployeeCode(applyBy));
			// =================================================================
			returnMap.put("multiList", multiList);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		} catch (Exception ex) {
			log.error("採購預算單初始化失敗，原因：" + ex.toString());
			throw new Exception("採購預算單初始化失敗，原因：" + ex.toString());
		}
	}    
	/**
	 * 公司主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 * @William說明
	 */
	public List<Properties> saveBuCompanySearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_COUNTRY_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	/**
	 * 前端資料塞入bean
	 * 
	 * @param parameterMap
	 * @return
	 */
	public void updateBuCompanyBean(Map parameterMap) throws FormException,
	Exception {
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String companyCode = (String) PropertyUtils.getProperty(formBindBean, "companyCode");
			BuCompany buCompany = this.findCompanyById(companyCode);
			if(buCompany == null) {
				buCompany = new BuCompany();
			}
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCompany);
			parameterMap.put("entityBean", buCompany);

		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("公司資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	/**
	 * 公司存檔
	 * 
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuCompany(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");

			BuCompany buCompany = (BuCompany)parameterMap.get("entityBean");
			log.info("Company：" + buCompany.getCompanyCode());


			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				BuCompany buCompanyUpdate = this.findCompanyById(buCompany.getCompanyCode());
				if( buCompanyUpdate == null ){
					log.info("save");
					buCompany.setCreatedBy(loginEmployeeCode);
					buCompany.setCreationDate(date);
					buCompany.setLastUpdatedBy(loginEmployeeCode);
					buCompany.setLastUpdateDate(date);
					buCompanyDAO.save(buCompany);
					resultMsg = "Company Name：" + buCompany.getCompanyName() + "存檔成功！ 是否繼續新增？";
					resultMap.put("isUpdate", "0");
				}else{
					log.info("update");
					buCompany.setLastUpdatedBy(loginEmployeeCode);
					buCompany.setLastUpdateDate(date);
					buCompanyDAO.merge(buCompany);
					resultMsg = "Company Name：" + buCompany.getCompanyName() + "更新成功";
					resultMap.put("isUpdate", "1");
				}

			//resultMsg = "Company Name：" + buCompany.getCompanyName()+ "存檔成功！ 是否繼續新增？";
			}
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buCompany);
			resultMap.put("vatMessage", msgBox);
			return resultMap;
		} catch (Exception ex) {
			log.error("公司維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("公司維護單單存檔失敗，原因：" + ex.toString());
		}
	}
	public Map updateCheckBuCompany(Map parameterMap) throws Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			// --------------------------------------------------------------
			String companyCode = (String) PropertyUtils.getProperty(formBindBean, "companyCode");
			String companyName = (String) PropertyUtils.getProperty(formBindBean, "companyName");
			String guiCode = (String) PropertyUtils.getProperty(formBindBean, "guiCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formID");
			System.out.println("companyCode : " + companyCode);
			//檢查公司代號不可為空白
			if(!StringUtils.hasText(companyCode))
				throw new Exception("公司代號不可為空白!!");
			System.out.println("companyName : " + companyName);
			//檢查公司名稱不可為空白
			if(!StringUtils.hasText(companyName))
				throw new Exception("公司名稱不可為空白!!");
			System.out.println("guiCode : " + guiCode);
			//檢查統一編號不可為空白
			if(!StringUtils.hasText(guiCode))
				throw new Exception("統一編號不可為空白!!");
			
			/**************
			List<BuCompany> BuCompany = buCompanyDAO.findByProperty(
					"BuCompany", new String[] { "companyCode" },
					new Object[] { companyCode });
			System.out.println("BuCompany : " + BuCompany.size());
			******************/
			
			if(!StringUtils.hasText(companyCode)){
				 throw new ValidationErrorException("請輸入國別代碼！");
			 }else{
				 if( !StringUtils.hasText(formIdString) ){
					 BuCompany buCompany = this.findCompanyById(companyCode);
					 if (buCompany != null) {
						 throw new ValidationErrorException("國別代碼：" + companyCode + "已經存在，請勿重複建立！");
					}
				}
			}
			
			//檢查公司代號不可為空白
			if(!StringUtils.hasText(companyCode))
				throw new Exception("公司代號不可為空白!!");
			System.out.println("companyName : " + companyName);
			//檢查公司名稱不可為空白
			if(!StringUtils.hasText(companyName))
				throw new Exception("公司名稱不可為空白!!");
			System.out.println("guiCode : " + guiCode);
			//檢查統一編號不可為空白
			if(!StringUtils.hasText(guiCode))
				throw new Exception("統一編號不可為空白!!");
			
			// 取得欲更新的bean
			BuCompany bucompany = new BuCompany();
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, bucompany);
			resultMap.put("entityBean", companyCode);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("追加單資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("公司資料存檔發生錯誤，原因：" + ex.getMessage());
		}
	}
	/*驗證資料
	 * 
	 */
	public Map validateBuCompanyHead(Map parameterMap) throws Exception {
		Map resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object otherBean = parameterMap.get("vatBeanOther");
			// --------------------------------------------------------------
			String companyCode = (String) PropertyUtils.getProperty(formBindBean, "companyCode");
			String companyName = (String) PropertyUtils.getProperty(formBindBean, "companyName");
			String guiCode = (String) PropertyUtils.getProperty(formBindBean, "guiCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			
			/**************
			System.out.println("companyCode : " + companyCode);
			//檢查公司代號不可為空白
			if(!StringUtils.hasText(companyCode))
				throw new Exception("公司代號不可為空白!!");
			System.out.println("companyName : " + companyName);
			//檢查公司名稱不可為空白
			if(!StringUtils.hasText(companyName))
				throw new Exception("公司名稱不可為空白!!");
			System.out.println("guiCode : " + guiCode);
			//檢查統一編號不可為空白
			if(!StringUtils.hasText(guiCode))
				throw new Exception("統一編號不可為空白!!");
			
			List<BuCompany> BuCompany = buCompanyDAO.findByProperty(
					"BuCompany", new String[] { "companyCode" },
					new Object[] { companyCode });
			System.out.println("BuCompany : " + BuCompany.size());
			******************/
			
			if(!StringUtils.hasText(companyCode)){
				 throw new ValidationErrorException("請輸入公司代碼！");
			 }else{
				 if( !StringUtils.hasText(formIdString) ){
					 BuCompany buCompany = this.findCompanyById(companyCode);
					 if (buCompany != null) {
						 throw new ValidationErrorException("公司代碼：" + companyCode + "已經存在，請勿重複建立！");
					}
				}
			}
			
			//檢查公司代號不可為空白
			if(!StringUtils.hasText(companyCode))
				throw new Exception("公司代號不可為空白!!");
			System.out.println("companyName : " + companyName);
			//檢查公司名稱不可為空白
			if(!StringUtils.hasText(companyName))
				throw new Exception("公司名稱不可為空白!!");
			System.out.println("guiCode : " + guiCode);
			//檢查統一編號不可為空白
			if(!StringUtils.hasText(guiCode))
				throw new Exception("統一編號不可為空白!!");
			
			// 取得欲更新的bean
			BuCompany bucompany = new BuCompany();
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, bucompany);
			resultMap.put("entityBean", companyCode);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("追加單資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("公司資料存檔發生錯誤，原因：" + ex.getMessage());
		}
	}
	/**
	 * ajax取得公司查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 * @William
	 * 接前端查詢欄位資料透由:arraylist放至httpRequest
	 */
	public List<Properties> getAJAXBuCompanySearchPageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值=========================
			String companyCode = httpRequest.getProperty("companyCode");
			String companyName = httpRequest.getProperty("companyName");
			String guiCode = httpRequest.getProperty("guiCode");	    
			//將欄位查尋資料組成like語法放進 findobjs Map中
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("and model.companyCode like :companyCode","%"+companyCode+"%");
			findObjs.put("and model.companyName like :companyName","%"+companyName+"%");
			findObjs.put("and model.guiCode like :guiCode","%"+guiCode+"%");	  
			//==============================================================	    
			//透由findobjs Map search放至出 buCompanyMap中並算出筆數及頁數
			Map buCompanyMap = buCompanyDAO.search( "BuCompany as model", findObjs, "order by companyCode desc", iSPage, iPSize, BuCompanyDAO.QUERY_SELECT_RANGE );
			List<BuCompany> buCompany = (List<BuCompany>) buCompanyMap.get(BuCompanyDAO.TABLE_LIST); 
			log.info("BuCompany.size"+ buCompany.size());	
			if (buCompany != null && buCompany.size() > 0) {
				// 設定額外欄位
				//this.setBuCurrencyLineOtherColumn(buCountrys);
				//將查出的資料GET到saveBuCompanySearchResult
				Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				Long maxIndex = (Long)buCompanyDAO.search("BuCompany as model", "count(model.companyCode) as rowCount" ,findObjs, "order by companyCode desc", iSPage, iPSize, BuCompanyDAO.QUERY_RECORD_COUNT ).get(BuCompanyDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES,buCompany, gridDatas, firstIndex, maxIndex));

			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_COUNTRY_FIELD_NAMES, GRID_SEARCH_COUNTRY_FIELD_DEFAULT_VALUES, map,gridDatas));

			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的公司功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的國別功能查詢失敗！");
		}	
	}
	
	public BuCompany findCompanyById(String id) throws Exception {
		 System.out.println("gggggggggggg:"+id);
		 try {
			 //id = id.trim().toUpperCase();
			 return buCompanyDAO.findById(id);
		 } catch (Exception ex) {
			 log.error("依據幣別代碼：" + id + "查詢幣別資料時發生錯誤，原因：" + ex.toString());
			 throw new Exception("依據幣別代碼：" + id + "查詢幣別資料時發生錯誤，原因："
					 + ex.getMessage());
		 }
	}
}
