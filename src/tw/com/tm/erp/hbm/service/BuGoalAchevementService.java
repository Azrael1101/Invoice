package tw.com.tm.erp.hbm.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuGoalAchevement;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.CmMovementHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuGoalAchevementDAO;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.ValidateUtil;

public class BuGoalAchevementService {

	private static final Log log = LogFactory.getLog(BuGoalAchevementService.class);
	private BuGoalAchevementDAO buGoalAchevementDAO;

	public void setBuGoalAchevementDAO(BuGoalAchevementDAO buGoalAchevementDAO) {
		this.buGoalAchevementDAO = buGoalAchevementDAO;
	}

	public static final Object[][] MASTER_DEFINITION = {
		{ "NAME", "TYPE", "VALUE", "STYLE", "VALUE" },
		{ "achevement_Id", AjaxUtils.FIELD_TYPE_LONG, "0", "mode:HIDDEN ", "" },
		{ "achevement", AjaxUtils.FIELD_TYPE_DOUBLE, "0.0", " ", "" },
		{ "discount", AjaxUtils.FIELD_TYPE_DOUBLE, "0.0", "", "" },
		{ "bonus", AjaxUtils.FIELD_TYPE_DOUBLE, "0.0", "", "" } };

	/**
	 * achevement查詢picker用的欄位
	 */
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"achevement", "discount", "bonus",
		"lastUpdatedByName", "lastUpdateDate", "achevement_Id",

	};

	/**
	 * achevement picker用的欄位
	 */
	public static final String[] GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE = {
		"", "", "",
		"", "", "",
	};
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
	/**
	 * 產生一筆 BuGoalAchevement
	 * @param otherBean
	 * @param isSave
	 * @return
	 * @throws Exception
	 */
	public BuGoalAchevement executeNewBuGoalAchevement() throws Exception {

		BuGoalAchevement form = new BuGoalAchevement();
		form.setachevement(null);
		form.setdiscount(null);
		form.setbonus(null);
		return form;


	}

	/**
	 * 初始化 bean 額外顯示欄位
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);

		try{

			BuGoalAchevement buGoalAchevement = this.executeFindActualBuGoalAchevement(parameterMap);
			Map multiList = new HashMap(0);
			resultMap.put("form", buGoalAchevement);
			resultMap.put("multiList",multiList);
			return resultMap;

		}catch(Exception ex){
			log.error("達成率初始化失敗，原因：" + ex.toString());
			throw new Exception("達成率單初始化失敗，原因：" + ex.toString());

		}

	}

	public BuGoalAchevement executeFindActualBuGoalAchevement(Map parameterMap)
	throws FormException, Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuGoalAchevement buGoalAchevement = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean , "formId");
			Long formId = StringUtils.hasText(formIdString) ? NumberUtils.getLong(formIdString) : null;
			buGoalAchevement = !StringUtils.hasText(formIdString)? this.executeNewBuGoalAchevement(): this.findById(formId) ;
			parameterMap.put( "entityBean", buGoalAchevement);
			return buGoalAchevement;
		} catch (Exception e) {
			log.error("取得實際地點主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際地點主檔失敗,原因:"+e.toString());
		}
	}

	public BuGoalAchevement findById(Long achevement_Id) throws Exception {

		try {
			BuGoalAchevement ach = (BuGoalAchevement) buGoalAchevementDAO.findByPrimaryKey(
					BuGoalAchevement.class, achevement_Id);
			return ach;
		} catch (Exception ex) {
			log.error("依據主鍵：" + achevement_Id + "查詢地點資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據主鍵：" + achevement_Id + "查詢地點資料時發生錯誤，原因："
					+ ex.getMessage());
		}
	}
	public List<BuGoalAchevement> find(BuGoalAchevement buGoalAchevement)
	throws IllegalAccessException, InvocationTargetException,
	IllegalArgumentException, SecurityException, NoSuchMethodException,
	ClassNotFoundException {
		log.info("BuGoalAchevementService.find");
		BuGoalAchevement searchObj = new BuGoalAchevement();
		BeanUtils.copyProperties(buGoalAchevement, searchObj);
		BeanUtil.changeSpace2Null(searchObj);
		List temp = new ArrayList();
		if (null != searchObj.getachevement_Id()) {
			temp.add(buGoalAchevementDAO.findByPrimaryKey(BuGoalAchevement.class,
					buGoalAchevement.getachevement_Id()));
		} else {
			temp = buGoalAchevementDAO.findByExample(searchObj);
		}
		return temp;
	}

	/**
	 * 驗證主檔
	 * @param parameterMap
	 * @throws Exception
	 */
	public void validateHead(Map parameterMap) throws Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
//		Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		//Double formId =  StringUtils.hasText(formIdString)? NumberUtils.getDouble(formIdString):null

		String achevement = (String) PropertyUtils.getProperty(formBindBean, "achevement");
		String discount = (String) PropertyUtils.getProperty(formBindBean, "discount");
		String bonus = (String) PropertyUtils.getProperty(formBindBean, "bonus");

		// 驗證達成率
		if(!StringUtils.hasText(achevement)){
			throw new ValidationErrorException("請輸入達成率");
		}
		if(!StringUtils.hasText(discount)){
			throw new ValidationErrorException("請輸入降抽");
		}
		if(!StringUtils.hasText(bonus)){
			throw new ValidationErrorException("請輸入各達獎金");
		}
		/*
		// 驗證抽成
		if(StringUtils.hasText(discount)){
			if (!ValidateUtil.isNumber(discount)) {
				throw new ValidationErrorException("請輸入抽成");
			}
		}*/
	}
	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateBuGoalAchevementBean(Map parameterMap)throws FormException, Exception {


		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
//			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			BuGoalAchevement buGoalAchevement= null;
			//Double achevement = Double.parseDouble((String)PropertyUtils.getProperty(formBindBean, "achevement"));
			//BuGoalAchevement buGoalAchevement = this.findById(formId) ;
			//if(buGoalAchevement==null) buGoalAchevement =new BuGoalAchevement();
			buGoalAchevement = (BuGoalAchevement)parameterMap.get("entityBean");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buGoalAchevement);

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
	 * 地點存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuGoalAchevement(Map parameterMap) throws Exception {

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

			BuGoalAchevement buGoalAchevement = (BuGoalAchevement)parameterMap.get("entityBean");
			//log.info("achevement：" + buGoalAchevement.getachevement());
			buGoalAchevementDAO.saveOrUpdate(buGoalAchevement);//存檔
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				//log.info("buLocation.getEnable() = " + buGoalAchevement.getEnable());
				//buLocation.setEnable(("N".equals(buLocation.getEnable()) ? "Y" : "N"));
				//buLocation.setLocationName(buLocation.getLocationName().trim().toUpperCase());
				//buGoalAchevement.setdiscount(buGoalAchevement.getdiscount().trim());

				buGoalAchevement.setLastUpdatedBy(loginEmployeeCode);
				buGoalAchevement.setLastUpdateDate(date);

//				BuLocation buLocationUpdate = buLocationDAO.findByLocationName(buLocation.getLocationName());

				if( buGoalAchevement.getachevement_Id() == null ){
					buGoalAchevement.setCreatedBy(loginEmployeeCode);
					buGoalAchevement.setCreationDate(date);
					buGoalAchevementDAO.save(buGoalAchevement);
				}else{
					buGoalAchevementDAO.update(buGoalAchevement);
				}
			}


			resultMsg = "achevement：" + buGoalAchevement.getachevement() + "存檔成功！ 是否繼續新增？";
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buGoalAchevement);
			resultMap.put("vatMessage", msgBox);
			return resultMap;

		} catch (Exception ex) {
			log.error("地點維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("地點維護單單存檔失敗，原因：" + ex.toString());
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
	 * ajax 取得ACHEVEMENT查詢的結果
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXBuGoalAchevementSearchPageData(Properties httpRequest) throws Exception{
		
		try{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			//======================帶入Head的值=========================

			String achevement = httpRequest.getProperty("achevement");
			//String discount = httpRequest.getProperty("discount");
			String bonus = httpRequest.getProperty("bonus");

			log.info("achevement:"+achevement);
			//log.info("discount:"+discount);
			log.info("bonus:"+bonus);

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("and model.achevement = :achevement",achevement);
			//findObjs.put("and model.discount like :discount","%"+discount+"%");
			findObjs.put("and model.bonus = :bonus",bonus);

			//==============================================================	    

			Map buGoalAchevementMap = buGoalAchevementDAO.search( "BuGoalAchevement as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );

			List<BuGoalAchevement> buGoalAchevements = (List<BuGoalAchevement>) buGoalAchevementMap.get(BaseDAO.TABLE_LIST); 

			log.info("buGoalAchevements.size"+ buGoalAchevements.size());	
			if (buGoalAchevements != null && buGoalAchevements.size() > 0) {

				// 設定額外欄位
				this.setBuGoalAchevementLineOtherColumn(buGoalAchevements);
				Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				Long maxIndex = (Long)buGoalAchevementDAO.search("BuGoalAchevement as model", "count(model.achevement_Id) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); 
				// 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, buGoalAchevements, gridDatas, firstIndex, maxIndex));
			}else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_ACHEVEMENT_FIELD_NAMES_VALUE, map, gridDatas));
			}

			return result;
		}catch(Exception ex){
			log.error("載入頁面顯示的achevement發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的achevement");
		}	
	}

	/**
	 * 設定ACHEVEMENT額外欄位
	 * @param buGoalAchevements
	 */
	private void setBuGoalAchevementLineOtherColumn(List<BuGoalAchevement> buGoalAchevements){
		for (BuGoalAchevement buGoalAchevement : buGoalAchevements) {
			buGoalAchevement.setLastUpdatedByName(UserUtils.getUsernameByEmployeeCode(buGoalAchevement.getLastUpdatedBy()));
		}
	}  


}

