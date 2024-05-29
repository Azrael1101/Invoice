package tw.com.tm.erp.hbm.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;


import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuTruck;
import tw.com.tm.erp.hbm.bean.BuTruckMod;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuTruckDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;



public class BuTruckService
{
	private static final Log log = LogFactory.getLog(BuTruckService.class);
	//DAO
	BuTruckDAO buTruckDAO;
	public void setBuTruckDAO(BuTruckDAO buTruckDAO)
	{
		this.buTruckDAO = buTruckDAO;
	}
//	Bean
	BuTruckMod buTruckMod;
	public void setBuTruckMod(BuTruckMod buTruckMod)
	{
		this.buTruckMod = buTruckMod;
	}	
	
	 public static final String[] GRID_SEARCH_TRUCK_FIELD_NAMES = 
	 {
		 "headId","truckCode" ,"truckDriver", "freightName","statusName"
		 ,"lastUpDatedByName","lastUpDateDate"
	 };

	 public static final String[] GRID_SEARCH_TRUCK_FIELD_DEFAULT_VALUES = 
	 { 
		 "","","","",""
		 ,"",""
	 };
	
	public Map executeBuTruckInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		Object otherBean = parameterMap.get("vatBeanOther");
		BuTruckMod buTruck = null;
		//BuTruck buTruck = new BuTruck();
		try{
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(" executeBuTruckInitial  formIdString:  "+formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			buTruck = !StringUtils.hasText(formIdString) ? this.executeNewBuTruck(otherBean,resultMap): this.findTruckById(formId,resultMap);
			parameterMap.put( "entityBean", buTruck);

			Map multiList = new HashMap(0);
			resultMap.put("form", buTruck);
			resultMap.put("multiList",multiList);	
			return resultMap;
		}
		catch(Exception ex)
		{
			log.error("初始化失敗，原因：" + ex.toString());
			throw new Exception("初始化失敗，原因：" + ex.toString());

		}
	}
	public BuTruckMod executeNewBuTruck(Object otherBean,Map resultMap) throws Exception {
		
		String loginEmployeeCode =  (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		BuTruckMod form = new BuTruckMod();
		form.setEnable("Y");
		form.setStatus("SAVE");
		form.setCreatedBy(loginEmployeeCode);
		form.setCreationDate(new Date());
		form.setLastUpDatedBy(loginEmployeeCode);
		form.setLastUpDateDate(new Date());
		buTruckDAO.save(form);
		resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
		resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
		resultMap.put("lastUpdateDate", new Date());
		resultMap.put("form", form);
		
		return form;

	}
	public BuTruckMod findTruckById(Long id,Map resultMap) throws Exception{
		try {
			SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy/MM/dd");

			BuTruckMod form = buTruckDAO.findModById(id);
			resultMap.put("statusName", OrderStatus.getChineseWord(form.getStatus()));
			resultMap.put("createdByName", UserUtils.getUsernameByEmployeeCode(form.getCreatedBy()));
			resultMap.put("lastUpdateDate", sdfmt.format(form.getLastUpDateDate()));
			resultMap.put("form", form);
			return form;
		} catch (Exception ex) {
			log.error("依據保卡代碼：" + id + "查詢保卡資料時發生錯誤，原因：" + ex.toString());
			throw new Exception("依據保卡代碼：" + id + "查詢保卡資料時發生錯誤，原因：" + ex.getMessage());
		}
	}



	public void validateBuTruckHead(Map parameterMap) throws Exception {

		Object formBindBean = parameterMap.get("vatBeanFormBind");
		Object otherBean = parameterMap.get("vatBeanOther");
		String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		String truckCode = (String) PropertyUtils.getProperty(formBindBean, "truckCode");


		// 驗證地點名稱
		if(!StringUtils.hasText(truckCode)){
			throw new ValidationErrorException("請輸入保卡代碼！");
		}else{
			if( !StringUtils.hasText(formIdString) ){
				List<BuTruckMod> buTruckMods = buTruckDAO.findByProperty("BuTruckMod", "truckCode", truckCode);
				if (buTruckMods.size() > 0) {
					throw new ValidationErrorException("保卡代碼：" + truckCode + "已經存在，請勿重複建立！");
				}
			}
		}
	}

	/**
	 * 依formId取得實際國別主檔
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public BuTruckMod executeFindActualBuTruck(Map parameterMap)
	throws FormException, Exception {

		Object otherBean = parameterMap.get("vatBeanOther");

		BuTruckMod buTruck = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			Long formId = formIdString.length()>0 ? Long.valueOf(formIdString) : null;
			log.info("111ddd---executeFindActualBuTruck 主檔:  "+formIdString);
			//buTruck = !StringUtils.hasText(formIdString) ? this.executeNewBuTruck(otherBean,parameterMap): this.findTruckById(formId,parameterMap) ;
			if(!StringUtils.hasText(formIdString)){
				buTruck = this.executeNewBuTruck(otherBean,parameterMap);
				log.info("newnewnew");
			}
				
			else{
				buTruck = this.findTruckById(formId,parameterMap);
				log.info("loadloadload  :  "+buTruck.getHeadId());
			}
				
			parameterMap.put( "entityBean", buTruck);
			return buTruck;
		} catch (Exception e) {
			log.error("取得實際保卡主檔失敗,原因:"+e.toString());
			throw new Exception("取得實際保卡主檔失敗,原因:"+e.toString());
		}
	}



	/**
	 * 前端資料塞入bean
	 * @param parameterMap
	 * @return
	 */
	public void updateBuTruckBean(Map parameterMap)throws FormException, Exception {
		try{
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			BuTruckMod buTruck = (BuTruckMod)parameterMap.get("entityBean");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buTruck);
			parameterMap.put("entityBean", buTruck);
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("保卡資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
	}
	/**
	 * 保卡存檔
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map updateAJAXBuTruck(Map parameterMap) throws Exception {

		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
		try {
			Object formBindBean 	 =   parameterMap.get("vatBeanFormBind");
			Object otherBean 		 =   parameterMap.get("vatBeanOther");
			String loginEmployeeCode =  (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String truckCode 		 =  (String)PropertyUtils.getProperty(formBindBean, "truckCode");//保卡號碼
			String enable			 =	(String)PropertyUtils.getProperty(formBindBean, "enable");
			//String truckType		 =	(String)PropertyUtils.getProperty(formBindBean, "truckType");//車型
			String truckNumber		 =	(String)PropertyUtils.getProperty(formBindBean, "truckNumber");//車號
			String truckDriver		 =	(String)PropertyUtils.getProperty(formBindBean, "truckDriver");//司機
			String truckDriverId	 =	(String)PropertyUtils.getProperty(formBindBean, "truckDriverId");//駕照
			String freightName		 =	(String)PropertyUtils.getProperty(formBindBean, "freightName");//貨運公司
			String status			 =  (String)PropertyUtils.getProperty(formBindBean, "status");
			String headId			 =  (String)PropertyUtils.getProperty(otherBean, "formId");
			log.info("111ddd---存檔 headId: "+headId);
			if(truckCode == null 	||truckCode.length()<=0)
				throw new Exception("請輸入保卡號碼");
			if(truckNumber == null	||truckNumber.length()<=0) 
				throw new Exception("請輸入"+"車號");
			if(truckDriver == null	||truckDriver.length()<=0) 
				throw new Exception("請輸入"+"司機");
			if(truckDriverId == null||truckDriverId.length()<=0) 
				throw new Exception("請輸入"+"司機駕照");
			if(freightName == null	||freightName.length()<=0) 
				throw new Exception("請輸入"+"貨運公司");
			BuTruckMod buTruckMod = (BuTruckMod)parameterMap.get("entityBean");
			String formAction = (String) PropertyUtils.getProperty(otherBean, "formAction");
			log.info("31111111111dddd---: "+formAction);
			if(OrderStatus.FORM_SAVE.equals(formAction)){
				if(!status.equals("FINISH"))
				{
					List<BuTruckMod> btm = buTruckDAO.findModByIdTruckCode(Long.valueOf(headId), truckCode);				
					BuTruckMod nbuTruckMod = null;
					if(btm.size()>0){
						for(int i = 0 ; i<btm.size() ; i++){
							nbuTruckMod = btm.get(i);
							log.info("111ddd---buTruckModsupdate");
							//nbuTruckMod.setCreatedBy(loginEmployeeCode);
							//nbuTruckMod.setCreationDate(new Date());
							nbuTruckMod.setLastUpDatedBy(loginEmployeeCode);
							nbuTruckMod.setLastUpDateDate(new Date());
							nbuTruckMod.setHeadId(Long.valueOf(headId));
							nbuTruckMod.setStatus("SAVE");
							buTruckDAO.update(nbuTruckMod);
							
						}
					}
					else
					{
						log.info("111ddd---buTruckModssave");
						nbuTruckMod = buTruckDAO.findModById(Long.valueOf(headId));
						nbuTruckMod.setEnable(enable);
						nbuTruckMod.setTruckCode(truckCode);
						//nbuTruckMod.setTruckType(truckType);
						nbuTruckMod.setFreightName(freightName);
						nbuTruckMod.setTruckNumber(truckNumber);
						nbuTruckMod.setTruckDriver(truckDriver);
						nbuTruckMod.setTruckDriverId(truckDriverId);
						nbuTruckMod.setCreatedBy(loginEmployeeCode);
						nbuTruckMod.setCreationDate(new Date());
						nbuTruckMod.setLastUpDatedBy(loginEmployeeCode);
						nbuTruckMod.setLastUpDateDate(new Date());
						nbuTruckMod.setHeadId(Long.valueOf(headId));
						nbuTruckMod.setStatus("SAVE");
						buTruckDAO.save(nbuTruckMod);
						
					}
				}
			}
			
			
			
			if(OrderStatus.FORM_SUBMIT.equals(formAction)){
				if(!status.equals("FINISH"))
				{
					List<BuTruckMod> btm = buTruckDAO.findModByIdTruckCode(Long.valueOf(headId), truckCode);
					BuTruckMod nbuTruckMod = null;
					if(btm.size()>0){
						for(int i = 0 ; i<btm.size() ; i++){
							nbuTruckMod = btm.get(i);
							log.info("111ddd---buTruckModsupdate");
							//nbuTruckMod.setCreatedBy(loginEmployeeCode);
							//nbuTruckMod.setCreationDate(new Date());
							nbuTruckMod.setLastUpDatedBy(loginEmployeeCode);
							nbuTruckMod.setLastUpDateDate(new Date());
							nbuTruckMod.setHeadId(Long.valueOf(headId));
							if(OrderStatus.SAVE.equals(status))
								nbuTruckMod.setStatus("FINISH");
							/*
							if(OrderStatus.SAVE.equals(status))
								nbuTruckMod.setStatus("SIGNING");
							else if(OrderStatus.SIGNING.equals(status))
								nbuTruckMod.setStatus("FINISH");
								*/
							buTruckDAO.update(nbuTruckMod);
							
						}
					}
					else
					{
						nbuTruckMod = buTruckDAO.findModById(Long.valueOf(headId));
						log.info("111ddd---buTruckModssave");
						nbuTruckMod.setEnable(enable);
						nbuTruckMod.setTruckCode(truckCode);
						//nbuTruckMod.setTruckType(truckType);
						nbuTruckMod.setFreightName(freightName);
						nbuTruckMod.setTruckNumber(truckNumber);
						nbuTruckMod.setTruckDriver(truckDriver);
						nbuTruckMod.setTruckDriverId(truckDriverId);
						nbuTruckMod.setCreatedBy(loginEmployeeCode);
						nbuTruckMod.setCreationDate(new Date());
						nbuTruckMod.setLastUpDatedBy(loginEmployeeCode);
						nbuTruckMod.setLastUpDateDate(new Date());
						nbuTruckMod.setHeadId(Long.valueOf(headId));
						if(OrderStatus.SAVE.equals(status))
							nbuTruckMod.setStatus("FINISH");
						/*
						if(OrderStatus.SAVE.equals(status))
							nbuTruckMod.setStatus("SIGNING");
						else if(OrderStatus.SIGNING.equals(status))
							nbuTruckMod.setStatus("FINISH");
							*/
						buTruckDAO.save(nbuTruckMod);
						
					}
				}

					//if(status.equals("SIGNING")){
					log.info("buTruckssave");
					BuTruck newBuTruck = new BuTruck();
					newBuTruck.setDescription(buTruckMod.getDescription());
					newBuTruck.setEnable(enable);
					newBuTruck.setTruckCode(truckCode);
					//newBuTruck.setTruckType(truckType);
					newBuTruck.setFreightName(freightName);
					newBuTruck.setTruckNumber(truckNumber);
					newBuTruck.setTruckDriver(truckDriver);
					newBuTruck.setTruckDriverId(truckDriverId);
					newBuTruck.setCreatedBy(loginEmployeeCode);
					newBuTruck.setCreationDate(new Date());
					newBuTruck.setLastUpDatedBy(loginEmployeeCode);
					newBuTruck.setLastUpDateDate(new Date());
					buTruckDAO.merge(newBuTruck);
					//}
			}
			resultMsg = "Truck Code：" + buTruckMod.getTruckCode() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buTruckMod);
			resultMap.put("vatMessage", msgBox);
			return resultMap;

		} catch (Exception ex) {
			log.error("保卡維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("保卡維護單單存檔失敗，原因：" + ex.toString());
		}
	}
	
	
	
	
	
	
	
//Search
	 public List<Properties> getAJAXBuTruckSearchPageData(Properties httpRequest) throws Exception{
		 	log.info("=====getAJAXBuTruckSearchPageData====");
		 try{
			 List<Properties> result = new ArrayList();
			 List<Properties> gridDatas = new ArrayList();
			 int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			 int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

			 //======================帶入Head的值=========================

			 String truckCode 	= httpRequest.getProperty("truckCode");
			 String truckDriver = httpRequest.getProperty("truckDriver");
			 String freightName = httpRequest.getProperty("freightName");
			 String headId		= httpRequest.getProperty("headId");
			 String status		= httpRequest.getProperty("status");
             HashMap map = new HashMap();
             HashMap findObjs = new HashMap();
             findObjs.put("where 1=:", 1);
             if(StringUtils.hasText(headId))
             {
            	 findObjs.put(" and model.headId =:headId",headId);
             }
             if(StringUtils.hasText(truckCode))
             {
            	 findObjs.put(" and model.truckCode =:truckCode",truckCode);
             }
             if(StringUtils.hasText(truckDriver))
             {
            	 findObjs.put(" and model.truckDriver =:truckDriver",truckDriver);
             }
             if(StringUtils.hasText(freightName))
             {
            	 findObjs.put(" and model.freightName =:freightName",freightName);
             }
             if(StringUtils.hasText(status))
             {
            	 findObjs.put(" and model.status =:status",status);
             }
             
			 //==============================================================	    

             //List<BuTruckMod> buTrucks = buTruckDAO.findTruckCode(headId, truckCode, truckDriver, freightName,status);
             Map buTruckMap = buTruckDAO.search("BuTruckMod as model",findObjs, "and model.truckCode is not null order by headId desc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);
             List<BuTruckMod> buTrucks = (List<BuTruckMod>) buTruckMap.get(BaseDAO.TABLE_LIST);
             List<BuTruckMod> allBuTrucks = new ArrayList();
			 if (buTrucks != null && buTrucks.size() > 0)
			 {
				 for(int i = 0; i<buTrucks.size();i++){
					 
					 if(buTrucks.get(i).getTruckCode()!=null){
						 buTrucks.get(i).setLastUpDatedByName(UserUtils.getUsernameByEmployeeCode(buTrucks.get(i).getLastUpDatedBy()));
						 buTrucks.get(i).setStatusName(OrderStatus.getChineseWord(buTrucks.get(i).getStatus()));
						 //allBuTrucks.add(buTrucks.get(i));
					 }
					
						
				 }
				 Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX 
				 Long maxIndex = (Long)buTruckDAO.search(" BuTruckMod as model", "count(model.headId) as rowCount" ,findObjs, " and model.truckCode is not null order By headId ", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				 result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_TRUCK_FIELD_NAMES, GRID_SEARCH_TRUCK_FIELD_DEFAULT_VALUES, buTrucks, gridDatas, firstIndex, maxIndex));
			 }
			 else
			 {
				 result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_TRUCK_FIELD_NAMES, GRID_SEARCH_TRUCK_FIELD_DEFAULT_VALUES, map, gridDatas));
			 }
			 
			 return result;
		 }catch(Exception ex){
			 ex.printStackTrace();
			 log.error("載入頁面顯示的保卡查詢發生錯誤，原因：" + ex.toString());
			 throw new Exception("載入頁面顯示的保卡功能");
		 }	
	 }
	 
	 
	 
	 public List<Properties> getSearchSelection(Map parameterMap) throws FormException, Exception{
		 Map resultMap = new HashMap(0);
		 Map pickerResult = new HashMap(0);
		 try{
			 Object pickerBean = parameterMap.get("vatBeanPicker");
			 String timeScope = (String)PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
			 ArrayList searchKeys = (ArrayList)PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
			 System.out.println("searchKeysssss:"+searchKeys.get(0));
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
	 public List<Properties> saveBuTruckSearchResult(Properties httpRequest) throws Exception{
		 String errorMsg = null;
		 AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_TRUCK_FIELD_NAMES);
		 return AjaxUtils.getResponseMsg(errorMsg);
	 }
	 
		public List<Properties> saveSearchResult(Properties httpRequest) throws Exception {
			String errorMsg = null;
			AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_TRUCK_FIELD_NAMES);
			return AjaxUtils.getResponseMsg(errorMsg);
		}
	 
}