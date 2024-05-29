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
import tw.com.tm.erp.hbm.bean.BuGoalItemDiscount;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuGoalItemDiscountDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuGoalItemDiscountService {

	private static final Log log = LogFactory.getLog(BuGoalItemDiscountService.class);

	private BuGoalItemDiscountDAO buGoalItemDiscountDAO;

	public void setBuGoalItemDiscountDAO(BuGoalItemDiscountDAO buGoalItemDiscountDAO) {
		this.buGoalItemDiscountDAO = buGoalItemDiscountDAO;}
	
	public static final Object[][] MASTER_DEFINITION = {
		{ "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
		{ "discountId", AjaxUtils.FIELD_TYPE_LONG, "0", "mode:HIDDEN", "" },
		{ "itemdiscount", AjaxUtils.FIELD_TYPE_DOUBLE, "0", "", "" },
		{ "discount", AjaxUtils.FIELD_TYPE_DOUBLE, "0", "", "" },
		{ "lastUpdatedBy", AjaxUtils.FIELD_TYPE_STRING, "", "", "" },
		{ "lastUpdateDate", AjaxUtils.FIELD_TYPE_DATE, "", "", "" }
	}; 
	
	/**
	 * 查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"itemdiscount", "discount",
		"lastUpdatedByName","lastUpdateDate","discountId"
	};

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
		"0", "0", 
		"", "", ""
	};
	
	
	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */

	public Map executeInitial(Map parameterMap) throws Exception{

		Map resultMap = new HashMap(0);

		try{

			BuGoalItemDiscount buGoalItemDiscount = this.executeFindActualBuGoalItemDiscount(parameterMap);

			Map multiList = new HashMap(0);
			resultMap.put("form", buGoalItemDiscount);
			resultMap.put("multiList",multiList);
			return resultMap;

		}catch(Exception ex){
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}

	/**
	 * 依formId取得實際地點主檔 in 送出
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuGoalItemDiscount executeFindActualBuGoalItemDiscount(Map parameterMap )throws FormException, Exception  {

//		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuGoalItemDiscount buGoalItemDiscount = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");

			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;

			buGoalItemDiscount = !StringUtils.hasText(formIdString)?  this.executeNewBuGoalItemDiscount(): this.findById(formId);

			parameterMap.put( "entityBean", buGoalItemDiscount);

			
			
			return buGoalItemDiscount;
		} catch (Exception e) {

			log.error("取得實際主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際主檔失敗,原因:"+e.toString());
		}
	}
	
	/**
	 * 產生一筆 BuGoalItemDiscount
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	private BuGoalItemDiscount executeNewBuGoalItemDiscount() throws Exception {
		BuGoalItemDiscount form = new BuGoalItemDiscount();
		form.setDiscount(null);
		form.setItemdiscount(null);
		return form;
	}

	private BuGoalItemDiscount findById(Long discountId)throws Exception {
		try {
			BuGoalItemDiscount goalitemdiscount = (BuGoalItemDiscount) buGoalItemDiscountDAO.findByPrimaryKey(
					BuGoalItemDiscount.class, discountId);
			return goalitemdiscount;
		} catch (Exception ex) {
			log.error("依據主鍵：" + discountId + "查詢資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + discountId + "查詢資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
	
	public List<BuGoalItemDiscount> find(BuGoalItemDiscount buGoalItemDiscount)
	throws IllegalAccessException, InvocationTargetException,
	IllegalArgumentException, SecurityException, NoSuchMethodException,
	ClassNotFoundException {
		log.info("BuGoalItemDiscountService.find");
		BuGoalItemDiscount searchObj = new BuGoalItemDiscount();
		BeanUtils.copyProperties(buGoalItemDiscount, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getDiscountId()) {
			temp.add(buGoalItemDiscountDAO.findByPrimaryKey(BuGoalItemDiscount.class,
					buGoalItemDiscount.getDiscountId()));
		} else {
			temp = buGoalItemDiscountDAO.findByExample(searchObj);
		}
		return temp;
	}
	
	
	/**
	 * 驗證主檔
	 * @param parameterMap
	 * @throws ValidationErrorException 
	 * @throws Exception 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception
	 */

	public void validateHead(Map parameterMap) throws  Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");

		//Long formId =  StringUtils.hasText(formIdString)?  Long.valueOf(formIdString):null;
		
		String itemdiscount = (String) PropertyUtils.getProperty(formBindBean, "itemdiscount");
		String discount = (String) PropertyUtils.getProperty(formBindBean, "discount");

		//log.info( "formId = " + formId );

		// 驗證名稱
		if(!StringUtils.hasText(itemdiscount)){
			throw new ValidationErrorException("請輸入商品折數！");
		}
		if(!StringUtils.hasText(discount)){
			throw new ValidationErrorException("請輸入降抽！");
		}
	/*	else{
			if( formId == null){
				BuGoalItemDiscount goalItemDiscountPO = buGoalItemDiscountDAO.findByitemdiscount(itemdiscount.trim().toUpperCase());
				if (goalItemDiscountPO != null) {
					throw new ValidationErrorException("名稱：" + itemdiscount + "已經存在，請勿重複建立！");
				}
			}
		}
*/
	}
	
	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateBuGoalItemDiscountBean(Map parameterMap)throws FormException, Exception {
		// TODO Auto-generated method stub

		BuGoalItemDiscount buGoalItemDiscount = null;
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			Object otherBean = parameterMap.get("vatBeanOther");

			buGoalItemDiscount = (BuGoalItemDiscount)parameterMap.get("entityBean");

			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalItemDiscount);
			

			parameterMap.put("entityBean", buGoalItemDiscount);
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
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalItemDiscount(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		HashMap resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		try {

//			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			BuGoalItemDiscount buGoalItemDiscount = (BuGoalItemDiscount)parameterMap.get("entityBean");

			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			//存檔
			buGoalItemDiscountDAO.saveOrUpdate(buGoalItemDiscount);



			if(OrderStatus.FORM_SUBMIT.equals(formAction)){

				//	log.info("buCommission.getEnable() = " + buCommission.getEnable());
				//	buCommission.setEnable(("N".equals(buCommission.getEnable()) ? "Y" : "N"));
				//buGoalItemDiscount.setItemdiscount(buGoalItemDiscount.getItemdiscount().trim().toUpperCase());
				//buGoalItemDiscount.setDiscount(buGoalItemDiscount.getDiscount().trim());

				buGoalItemDiscount.setLastUpdatedBy(loginEmployeeCode);
				buGoalItemDiscount.setLastUpdateDate(date);


				if( buGoalItemDiscount.getDiscountId() == null ){
					//buGoalItemDiscount.setCreatedBy(loginEmployeeCode);
					//buGoalItemDiscount.setCreationDate(date);
					buGoalItemDiscountDAO.save(buGoalItemDiscount);
				}else{
					buGoalItemDiscountDAO.update(buGoalItemDiscount);
				}
			}

			resultMsg = "itemdiscount：" + buGoalItemDiscount.getItemdiscount() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buGoalItemDiscount);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("維護單存檔失敗，原因：" + ex.toString());
		}
	}
	
	
	/**
	 * ajax 取得查詢的結果
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

			String itemdiscount = httpRequest.getProperty("itemdiscount");
			String discount = httpRequest.getProperty("discount");

			log.info("itemdiscount:"+itemdiscount);
			log.info("discount:"+discount);


			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put(" and model.itemdiscount = :itemdiscount",itemdiscount);
			findObjs.put(" and model.discount = :discount",discount);


			//==============================================================	    

			Map buGoalItemDiscountMap = buGoalItemDiscountDAO.search( "BuGoalItemDiscount as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
			List<BuGoalItemDiscount> buGoalItemDiscounts = (List<BuGoalItemDiscount>) buGoalItemDiscountMap.get(BaseDAO.TABLE_LIST); 

			log.info("BuGoalItemDiscount.size"+ buGoalItemDiscounts.size());	
			if (buGoalItemDiscounts != null && buGoalItemDiscounts.size() > 0) {

				// 設定額外欄位
				this.setLineOtherColumn(buGoalItemDiscounts);

				Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				Long maxIndex = (Long)buGoalItemDiscountDAO.search(" BuGoalItemDiscount as model", "count(model.discountId) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,buGoalItemDiscounts, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map,gridDatas));
			}

			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的功能查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的功能查詢失敗！");
		}	
	}

	private void setLineOtherColumn(List<BuGoalItemDiscount> buGoalItemDiscounts) {
		for (BuGoalItemDiscount buGoalItemDiscount : buGoalItemDiscounts) {
			buGoalItemDiscount.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buGoalItemDiscount.getLastUpdatedBy()));
		}
		
	}
	
	/**
	 * 將主檔查詢結果存檔
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
	
	
	
	
}
