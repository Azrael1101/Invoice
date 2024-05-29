package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;


import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdCategory;
import tw.com.tm.erp.hbm.bean.AdCategoryHead;
import tw.com.tm.erp.hbm.bean.AdCategoryLine;
import tw.com.tm.erp.hbm.bean.AdCustServiceHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PrItem;
import tw.com.tm.erp.hbm.dao.AdCategoryDAO;
import tw.com.tm.erp.hbm.dao.AdCustServiceDAO;
import tw.com.tm.erp.hbm.dao.AdCustServiceLineDAO;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;

import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.PrItemDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class PrItemService {

	private static final Log log = LogFactory.getLog(PrItemService.class);

	private BuBrandService buBrandService;
	private BuEmployeeDAO buEmployeeDAO;
	private BaseDAO baseDAO;
	private PrItemDAO prItemDAO;


	public void setPrItemDAO(PrItemDAO prItemDAO) {
		this.prItemDAO = prItemDAO;
	}

	public BuBrandService getBuBrandService() {
		return buBrandService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	/**
	 * 客服單查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"itemNo","itemName","specInfo","supplier","itemId"
	};
	/**
	 * 客服單 picker用的欄位
	 */
	public static final String[] GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE = {
		"","","","",""
	};

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{
		log.info("executeInitial...");
		Map resultMap = new HashMap();
		Map multiList = new HashMap(0);

		Object otherBean     = parameterMap.get("vatBeanOther");
		//String brandCode     = (String)PropertyUtils.getProperty(otherBean, "brandCode");
		//String orderTypeCode = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		String formIdString  = (String)PropertyUtils.getProperty(otherBean, "formId");
		String empolyeeCode  = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

		Long formId          =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		try{
			List<BuCommonPhraseLine> alldepartment = baseDAO.findByProperty( "BuCommonPhraseLine", new String[] {  "id.buCommonPhraseHead.headCode", "enable" },new Object[] {"EmployeeDepartment","Y" }, "indexNo");
			multiList.put("alldepartment"		, AjaxUtils.produceSelectorData(alldepartment,"lineCode", "name", false, true));
			PrItem prItem   = null;
			if(formId != null){
				prItem = findById(formId);
			}else{
				prItem = createNewPoPurchaseHead(parameterMap, resultMap );
			}
			List<AdCategoryHead> allproject = baseDAO.findByProperty( "AdCategoryHead", new String[] { "orderTypeCode","enable" },new Object[] {"PRITEM","Y" },"displaySort");
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "deptNo","enable" },new Object[] {"103","Y" },"displaySort");
			multiList.put("allproject"			, AjaxUtils.produceSelectorData(allproject,"groupNo", "groupName", false, true));
			multiList.put("allsystem"			, AjaxUtils.produceSelectorData(allsystem ,"classNo", "className", false, true));
			resultMap.put("form"			, 	  prItem);
			//resultMap.put("branchCode"		,     buBrand.getBranchCode());	
			//resultMap.put("createdByName"	,     UserUtils.getUsernameByEmployeeCode(prItem.getCreatedBy()));
			resultMap.put("multiList"		,     multiList);

		}catch(Exception ex){
			log.error("商品主檔初始化失敗，原因：" + ex.toString());       
			throw new Exception("商品主檔初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	public PrItem findById(Long itemId) throws Exception {
		try {
			PrItem pr= (PrItem) prItemDAO.findByPrimaryKey(
					PrItem.class, itemId);
			return pr;
		} catch (Exception ex) {
			log.error("依據主鍵：" + itemId + "查詢商品主檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + itemId + "查詢商品資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}

	/** 產生一筆新的 buPurchase 
	 * @param argumentMap
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public PrItem createNewPoPurchaseHead(Map parameterMap, Map resultMap) throws Exception {
		log.info("createNewPoPurchaseHead");
		Object otherBean      = parameterMap.get("vatBeanOther");
		//String brandCode      = (String)PropertyUtils.getProperty(otherBean, "brandCode");		
		String employeeCode   = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		//String orderTypeCode  = (String)PropertyUtils.getProperty(otherBean, "orderTypeCode");
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		String depart=buEmployee.getEmployeeDepartment();
		//log.info("loginDepartment==="+loginDepartment);
		try{
			PrItem form = new PrItem();
			form.setDepartment(           depart);
			//form.setCreatedByName(        UserUtils.getUsernameByEmployeeCode(employeeCode));
			//form.setRequestCode(          employeeCode);
			form.setCreatedBy(            employeeCode);
			//form.setLastUpdatedBy(        employeeCode);
			//form.setRequestDate(          DateUtils.parseDate(DateUtils.format(new Date())));
			form.setCreationDate(         DateUtils.parseDate(DateUtils.format(new Date())));
			//form.setLastUpdateDate(       DateUtils.parseDate(DateUtils.format(new Date())));
			form.setCreatedByName(        UserUtils.getUsernameByEmployeeCode(employeeCode));
			//form.setRequest(              UserUtils.getUsernameByEmployeeCode(employeeCode));
			//form.setStatus(               OrderStatus.SAVE);
			//saveTmp(form);
			return form;
		}catch (Exception ex) {
			log.error("產生新需求單失敗，原因：" + ex.toString());
			throw new Exception("產生需求單發生錯誤！");
		}
	}
	/**
	 * 驗證主檔
	 * @param parameterMap
	 * @throws Exception
	 */
	public void validateHead(Map parameterMap) throws Exception {
		Object formBindBean  = parameterMap.get("vatBeanFormBind");
		String isTax   = (String) PropertyUtils.getProperty(formBindBean, "isTax");
		String itemNo    = (String) PropertyUtils.getProperty(formBindBean, "itemNo");
		String itemBrand    = (String) PropertyUtils.getProperty(formBindBean, "itemBrand");
		// 驗證
		if(!StringUtils.hasText(itemNo)){
			throw new ValidationErrorException("請輸入品號");
		}
		if(!StringUtils.hasText(isTax)){
			throw new ValidationErrorException("請選擇稅別");
		}
		if(!StringUtils.hasText(itemBrand)){
			throw new ValidationErrorException("請輸入商品品牌");
		}
	}
	/**
	 * 地點存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXPrItem(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {
//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");

			PrItem prItem = (PrItem)parameterMap.get("entityBean");
			//log.info("achevement：" + buGoalAchevement.getachevement());
			prItem.setOrderTypeCode("PC_GROUP");
			prItemDAO.saveOrUpdate(prItem);//存檔



			resultMsg = "商品：" + prItem.getItemNo() + "存檔成功！ 是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", prItem);
			resultMap.put("vatMessage", msgBox);
			return resultMap;

		} catch (Exception ex) {
			log.error("地點維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("地點維護單單存檔失敗，原因：" + ex.toString());
		}
	}
	public Long getPromotionHeadId(Object bean) throws FormException, Exception{

		Long itemCode = null;
		String id = (String)PropertyUtils.getProperty(bean, "itemCode");
		if(StringUtils.hasText(id)){
			itemCode = NumberUtils.getLong(id);
		}else{
			throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
		}

		return itemCode;
	}

	public PrItem executeFindActualPrItem(Map parameterMap)
	throws FormException, Exception {
		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		PrItem pritem = null;
		try {
			String formIdString = (String) PropertyUtils.getProperty(otherBean , "formId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			pritem = !StringUtils.hasText(formIdString)? this.executeNewBuGoalAchevement(): this.findById(formId) ;
			parameterMap.put( "entityBean", pritem);
			return pritem;
		} catch (Exception e) {
			log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
	}

	/**
	 * 產生一筆 BuGoalAchevement
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	public PrItem executeNewBuGoalAchevement() throws Exception {

		PrItem form = new PrItem();
		//form.setItemCode(null);
		return form;


	}
	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updatePrItemBean(Map parameterMap)throws FormException, Exception {


		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			//BuGoalAchevement buGoalAchevement= null;
			//Double achevement = Double.parseDouble((String)PropertyUtils.getProperty(formBindBean, "achevement"));
			//BuGoalAchevement buGoalAchevement = this.findById(formId) ;
			//if(buGoalAchevement==null) buGoalAchevement =new BuGoalAchevement();
			PrItem prItem = (PrItem)parameterMap.get("entityBean");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, prItem);
			//log.info("buGoalAchevement.getEnable"+ buGoalAchevement.getEnable());
			//parameterMap.put("entityBean", buGoalAchevement);

		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("地點資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * 取得指定連動的類別下拉
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXCategory(Properties httpRequest)throws Exception{
		List list = new ArrayList();
		Properties properties = new Properties();
		try{
			String category01= httpRequest.getProperty("category01");
			log.info("categoryItem = " + category01);
			List<AdCategoryLine> allsystem = baseDAO.findByProperty( "AdCategoryLine", new String[] { "groupNo" ,"enable" },new Object[] {category01,"Y" }, "displaySort");
			allsystem = AjaxUtils.produceSelectorData(allsystem,"classNo", "className", false, true); 
			log.info("category02 = " + allsystem);
			properties.setProperty("allsystem", AjaxUtils.parseSelectorData(allsystem));
			list.add(properties);
		}catch(Exception e){
			log.error("取得指定連動的類別下拉，原因：" + e.toString());
			throw new Exception("取得指定連動的類別下拉，原因：" + e.getMessage());
		}
		return list;
	}

	/**
	 * ajax 取得BUPURCHASE查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXBuAdCustSearchPageData(Properties httpRequest) throws Exception{
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			//======================帶入Head的值=========================
			HashMap searchMap = getSearchMap(httpRequest);
			//==============================================================	    
			List<PrItem> prItems = (List<PrItem>) prItemDAO.findPageLine(searchMap, 
					iSPage, iPSize, PrItemDAO.QUARY_TYPE_SELECT_RANGE).get("form");
			//log.info("buPurchases.size"+ puHeads.size());	
			if (prItems != null && prItems.size() > 0) {
				// 設定額外欄位
				//this.setBuPurchaseHeadLineOtherColumn(puHeads);
				Long firstIndex =Long.valueOf(iSPage * iPSize)+ 1;    // 取得第一筆的INDEX 	
				Long maxIndex = (Long)prItemDAO.findPageLine(searchMap, -1, iPSize, 
						PrItemDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");	// 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, prItems, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, gridDatas));
			}
			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的bupurchase發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的bupurchase");
		}	
	}
	public HashMap getSearchMap(Properties httpRequest){
		HashMap searchMap = new HashMap();
		//String orderTypeCode  = httpRequest.getProperty("orderTypeCode");
		searchMap.put("itemCode",     		httpRequest.getProperty("itemCode"));
		searchMap.put("itemName", 			httpRequest.getProperty("itemName"));
		//searchMap.put("itemDefault",        httpRequest.getProperty("itemDefault"));

		return searchMap;
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
			if(result.size() > 0)
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
	 * 將ACHEVEMENT主檔查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> saveSearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
}

