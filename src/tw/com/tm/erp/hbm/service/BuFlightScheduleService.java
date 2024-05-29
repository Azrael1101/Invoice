package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuFlightSchedule;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuFlightScheduleDAO;
import tw.com.tm.erp.standardie.SelectDataInfo;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuFlightScheduleService {
	public static final String PROGRAM_ID = "BU_FLIGHT_SCHEDULE";
	private static final Log log = LogFactory.getLog(BuFlightScheduleService.class);

	
	private BuFlightScheduleDAO buFlightScheduleDAO;
	public static final int SEARCH_NO_MIN_LENGTH = 3;
	
	
	public static final String[] GRID_FIELD_NAMES = { 
		"flightDate", "flightNo", "flightCompany", 
		"terminal",	 "departure", "scheduleTime", 
		"actualTime", "gate","flightType","status",
		"headId", "isLockRecord", "isDeleteRecord", "returnMessage" };

	public static final int[] GRID_FIELD_TYPES = { 
		    AjaxUtils.FIELD_TYPE_DATE,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
		    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING };
	
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
		    "", "", "", 
		    "", "", "",
		    "", "", "", "",
			"", AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, "" };	
	

	public void setBuFlightScheduleDAO(BuFlightScheduleDAO buFlightScheduleDAO) {
		this.buFlightScheduleDAO = buFlightScheduleDAO;
	}


	/**
	 * 初始化畫面
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String flightDate = (String) PropertyUtils.getProperty(otherBean, "flightDate");
			log.info("flightDate:"+flightDate);
			if(!StringUtils.hasText(flightDate)){
				resultMap.put("flightDate",DateUtils.getCurrentDateStr(DateUtils.C_DATE_PATTON_SLASH));
			}
		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}
	
	public List<Properties> executeCreateInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			BuFlightSchedule form = null == formId || 0 == formId? executeNewFlightSchedule(otherBean, resultMap) : findFlightSchedule(otherBean, resultMap);
			resultMap.put("form", form);
		} catch (Exception ex) {
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "表單初始化失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap);

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception {
		HashMap map = new HashMap();
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			String flightDateString = (String) httpRequest.getProperty("flightDate");
			//log.info("flightDateString:"+flightDateString);
			String flightNo = (String) httpRequest.getProperty("flightNo");
			//log.info("flightNo:"+flightNo);
			String flightCompany = (String) httpRequest.getProperty("flightCompany");
			//log.info("flightCompany:"+flightCompany);
			String terminal = (String) httpRequest.getProperty("terminal");
			//log.info("terminal:"+terminal);
			String flightType = (String) httpRequest.getProperty("flightType");
			//log.info("flightType:"+flightType);
			String departure = (String) httpRequest.getProperty("departure");
			//log.info("departure:"+departure);
			String scheduleTime = (String) httpRequest.getProperty("scheduleTime");
			//log.info("scheduleTime:"+scheduleTime);
			String actualTime = (String) httpRequest.getProperty("actualTime");
			//log.info("actualTime:"+actualTime);
			String gate = (String) httpRequest.getProperty("gate");
			//log.info("gate:"+gate);
			String status = (String) httpRequest.getProperty("status");
			//log.info("status:"+status);
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			map.put("flightDateString", flightDateString);
			map.put("flightNo", flightNo);
			map.put("flightCompany", flightCompany);
			map.put("terminal", terminal);
			map.put("flightType", flightType);
			map.put("departure", departure);
			map.put("scheduleTime", scheduleTime);
			map.put("actualTime", actualTime);
			map.put("gate", gate);
			map.put("status", status);
			// ==============================================================
			log.info("iSPage:"+iSPage+" iPSize:"+iPSize);
		    HashMap  returnResult = buFlightScheduleDAO.findPageLine(map, iSPage,
							iPSize, buFlightScheduleDAO.QUARY_TYPE_SELECT_RANGE);
			List<BuFlightSchedule> items = (List<BuFlightSchedule>) returnResult.get("form");
			if (items != null && items.size() > 0) {
				log.info("item.size:"+ items.size());
				Long firstIndex =  Long.valueOf(iSPage * iPSize) + 1;  ; // 取得第一筆的INDEX
				Long maxIndex = (Long)buFlightScheduleDAO.findPageLine(map, iSPage,
						iPSize, buFlightScheduleDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount");
				log.info("buFlightSchedule.AjaxUtils.getAJAXPageData ");

				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,
						items, gridDatas, firstIndex, maxIndex));
			} else {
				log.info("size of line is 0");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,
						gridDatas));

			}
		
			return result;
		} catch (Exception ex) {
			//ex.printStackTrace();
			log.error("載入頁面顯示的航班時間發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的航班時間失敗！");
		}
	}
	
	/**
	 * 建立新的入提單
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public BuFlightSchedule executeNewFlightSchedule(Object otherBean, Map resultMap) throws Exception {
		log.info("executeNewFlightSchedule....");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String flightType = (String) PropertyUtils.getProperty(otherBean, "flightType");
		log.info("fligthType:"+flightType);
		BuFlightSchedule form = new BuFlightSchedule();
		form.setFlightDate(new Date());
		form.setFlightType(flightType);
		form.setFlightNo("");
		form.setFlightCompany("");
		form.setTerminal("2");
		form.setDestination("");
		form.setDeparture("");
		form.setScheduleTime("");
		form.setActualTime("");
		form.setGate("");
		form.setStatus("準時");
		form.setCreatedBy(loginEmployeeCode);
		form.setCreationDate(new Date());
		form.setLastUpdatedBy(loginEmployeeCode);
		form.setLastUpdateDate(new Date());
		resultMap.put("form", form);
		return form;


	}

	public BuFlightSchedule findFlightSchedule(Object otherBean, Map resultMap) throws Exception {
		log.info("findFlightSchedule....");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;

		BuFlightSchedule form = (BuFlightSchedule)buFlightScheduleDAO.findByPrimaryKey(BuFlightSchedule.class, formId);
		if (null != form) {
			System.out.println("findFlightSchedule.status"+ form.getStatus());
			resultMap.put("form", form);
			return form;
		} else {
			throw new NoSuchDataException("查無此航班資料，於按下「確認」鍵後，將關閉本視窗！");

		}

	}	
	
	public HashMap save(Map parameterMap) throws Exception {
			
		    HashMap returnMap = new HashMap(0);
			MessageBox msgBox = new MessageBox();
			log.info("1.performTransaction");
			String employeeCode = new String("");
			String logLevel = new String("SUCCESS");
			String logMessage = new String("");
			Command cmd_bf = new Command();
			BuFlightSchedule po = null;

			try {
				Object formBindBean = parameterMap.get("vatBeanFormBind");
				Object formLinkBean = parameterMap.get("vatBeanFormLink");
				Object otherBean    = parameterMap.get("vatBeanOther");
				String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
				log.info(formIdString);
				Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
				System.out.println("formId:" + formId);
				employeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
				if(formId == null || 0== formId){
					po = new BuFlightSchedule();
				}else{
					po = this.getActualObjcet(formLinkBean);
					po = null!=po?po : new BuFlightSchedule();
				}
				
				System.out.println("======Start copyJSONBeantoPojoBean=========");
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
				System.out.println("======Finish copyJSONBeantoPojoBean=========");
				log.info("FlightDate:"+DateUtils.format(po.getFlightDate())+"/FlightNo:"+po.getFlightNo());
				po.setLastUpdatedBy(employeeCode);
				po.setLastUpdateDate(new Date());
				if(null == po.getHeadId()){
					po.setCreatedBy(employeeCode);
					po.setCreationDate(new Date());
					buFlightScheduleDAO.save(po);
					log.info("insert successful...");
					logMessage=DateUtils.format(po.getFlightDate()) +" "+po.getFlightCompany()+po.getFlightNo()+"班機己加入清單中";	
				}else{
					po.setLastUpdatedBy(employeeCode);
					po.setLastUpdateDate(new Date());
					buFlightScheduleDAO.update(po);
					log.info("update successful...");
					logMessage=DateUtils.format(po.getFlightDate()) +" "+po.getFlightCompany()+po.getFlightNo()+"班機資料已修改";	
				}
				returnMap.put("entityBean", po);
				
				logLevel = "SUCCESS";
							
			} catch (FormException fex) {
				msgBox.setMessage("加入航班清單時發生錯誤");
				logLevel = "ERROR";				
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("加入航班清單時發生錯誤，原因：" + ex.toString());
				log.error("加入航班清單時發生錯誤，原因：" + ex.toString());				
				logLevel = "ERROR";
				logMessage="加入航班清單時發生錯誤";			
			}		
			log.info(logMessage);
			returnMap.put("logLevel", logLevel);
			returnMap.put("resultMsg", logMessage);
			return returnMap;
	    }
	
	private BuFlightSchedule getActualObjcet(Object bean) throws FormException, Exception {
		log.info("5.1 getActualObjcet");
		BuFlightSchedule buFlightSchedule = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		log.info("getActualObjcet headId=" + id);

		if (StringUtils.hasText(id)) {
			Long headId = NumberUtils.getLong(id);
			buFlightSchedule = (BuFlightSchedule)buFlightScheduleDAO.findByPrimaryKey(BuFlightSchedule.class, headId);
			if (buFlightSchedule == null) {
				throw new NoSuchObjectException("查無航班主鍵：" + headId + "的資料！");
			}
			log.info("FlightDate:" + buFlightSchedule.getFlightDate());
		} else {
			throw new ValidationErrorException("傳入的航班主鍵為空值！");
		}

		return buFlightSchedule;
	}
	/**
	 * 匯出明細 add by Weichun 2010.06.22
	 *
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public SelectDataInfo findViewByMap(HttpServletRequest httpRequest) throws Exception {
		log.info("findViewByMap...export excel");
		HashMap map = new HashMap();
		try {
			String flightDateString = (String) httpRequest.getParameter("flightDate");
			//log.info("flightDateString:"+flightDateString);
			String flightNo = (String) httpRequest.getParameter("flightNo");
			//log.info("flightNo:"+flightNo);
			String flightCompany = (String) httpRequest.getParameter("flightCompany");
			//log.info("flightCompany:"+flightCompany);
			String terminal = (String) httpRequest.getParameter("terminal");
			//log.info("terminal:"+terminal);
			String flightType = (String) httpRequest.getParameter("flightType");
			//log.info("flightType:"+flightType);
			String departure = (String) httpRequest.getParameter("departure");
			//log.info("departure:"+departure);
			String scheduleTime = (String) httpRequest.getParameter("scheduleTime");
			//log.info("scheduleTime:"+scheduleTime);
			String actualTime = (String) httpRequest.getParameter("actualTime");
			//log.info("actualTime:"+actualTime);
			String gate = (String) httpRequest.getParameter("gate");
			//log.info("gate:"+gate);
			String status = (String) httpRequest.getParameter("status");
			//log.info("status:"+status);
			map.put("flightDateString", flightDateString);
			map.put("flightNo", flightNo);
			map.put("flightCompany", flightCompany);
			map.put("terminal", terminal);
			map.put("flightType", flightType);
			map.put("departure", departure);
			map.put("scheduleTime", scheduleTime);
			map.put("actualTime", actualTime);
			map.put("gate", gate);
			map.put("status", status);
			// 可用庫存excel表的欄位順序
			Object[] object = null;
			object = new Object[] { "flightDate", "flightNo", "flightCompany", "terminal", "departure",
					"scheduleTime", "actualTime", "gate","status","action"};
			List<Object[]> buFlightSchedules= buFlightScheduleDAO.findViewByMap(map);


		// 按excel表的欄位順序將資料放入Object[]，再一筆筆放到List
			log.info("buFlightSchedules.size:"+buFlightSchedules.size());
			List rowData = new ArrayList();
			for (int i = 0; i < buFlightSchedules.size(); i++) {
				Object[] dataObject = (Object[]) buFlightSchedules.get(i);
				for (int j = 0; j < object.length; j++) {
					String actualValue = null;
					if (object[j] != null) {
						actualValue = dataObject[j] != null ? String.valueOf(dataObject[j]) : null;
					}
					dataObject[j] = actualValue;
			}
			rowData.add(dataObject);
		}
		return new SelectDataInfo(object, rowData);
		} catch (Exception ex) {
			// ex.printStackTrace();
			log.error("匯出班機時刻表時發生錯誤，原因：" + ex.toString());
			throw new Exception("匯出班機時刻表時失敗！");
		}
	}
	
	/**
	 * 匯入明細
	 *
	 * @param flightType
	 * @param promotionItems
	 * @throws Exception
	 */
	public void executeImport(String flightType, List<BuFlightSchedule> importDatas) throws Exception {
		log.info("executeImport.." + flightType);
		log.info("executeImport..size" + importDatas.size());
		String timeStamp= DateUtils.getCurrentDateStr(DateUtils.C_TIME_PATTON_STAMP);
		log.info("timeStamp:" + timeStamp);
		Map collectMaps = new HashMap(0);
		try {
			for (int i = 0; i < importDatas.size(); i++) {
				BuFlightSchedule buFlightSchedule = (BuFlightSchedule) importDatas.get(i);
				buFlightSchedule.setFlightNo(buFlightSchedule.getFlightNo().toUpperCase());
				if(this.existFlightData(buFlightSchedule, collectMaps)){
					log.error(DateUtils.format(buFlightSchedule.getFlightDate())+ " "+ buFlightSchedule.getScheduleTime() + "/" +buFlightSchedule.getFlightNo()+"航班資料重覆");
				}else{
					log.info("FlightDate:"+DateUtils.format(buFlightSchedule.getFlightDate())+"/FlightNo:"+buFlightSchedule.getFlightNo()+"/ Action:"+buFlightSchedule.getAction());
					if(null != buFlightSchedule.getFlightDate()&& StringUtils.hasText(buFlightSchedule.getFlightNo())){
						
						deleteFlightSchedule(flightType, buFlightSchedule.getFlightDate(), buFlightSchedule.getFlightNo());					
						if(null ==buFlightSchedule.getAction() || !"D".equalsIgnoreCase(buFlightSchedule.getAction())){
							buFlightSchedule.setFlightType(flightType);
							//log.info(buFlightSchedule.getScheduleTime().indexOf(":"));
							if(1==buFlightSchedule.getScheduleTime().indexOf(":")){
								buFlightSchedule.setScheduleTime("0"+buFlightSchedule.getScheduleTime());
							}
							if(1==buFlightSchedule.getActualTime().indexOf(":")){
								buFlightSchedule.setActualTime("0"+buFlightSchedule.getActualTime());
							}
							buFlightSchedule.setReserve1(timeStamp);
							buFlightScheduleDAO.save(buFlightSchedule);
						}
					}else
						throw new NoSuchObjectException("第"+(i+1)+"行，日期或班機代號為空白");
				}
			}
		} catch (NoSuchObjectException ns) {
			log.error("刪除班機資訊時失敗，原因：" + ns.toString());
			throw new FormException(ns.getMessage());
		} catch (FormException fe) {
			log.error("刪除班機資訊時失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("班機資訊匯入時發生錯誤，原因：" + ex.toString());
			throw new Exception("班機資訊匯入時發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	public void deleteFlightSchedule(String flightType, Date flightDate, String flightNo){
		buFlightScheduleDAO.deleteFlightSchedule(flightType, flightDate, flightNo);
	}


	
	public List<Properties> deleteFlightScheduleByHeadId(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		Map messageMap = new HashMap();

		try {
			log.info("getSearchSelection.parameterMap:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			buFlightScheduleDAO.deleteFlightScheduleByHeadId(formId);
			messageMap.put("type", "ALERT");
			messageMap.put("message", "刪除航班資料成功");
			messageMap.put("event1", null);
			messageMap.put("event2", null);
		} catch (Exception ex) {
			log.error("刪除航班資失敗，原因：" + ex.toString());
			messageMap.put("type", "ALERT");
			messageMap.put("message", "刪除航班資料失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
		}
		resultMap.put("vatMessage", messageMap);
		return AjaxUtils.parseReturnDataToJSON(resultMap);

	}
	
	public List<Properties> getReportConfig(Map parameterMap) throws Exception  {
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String flightType = (String) PropertyUtils.getProperty(otherBean,"flightType");
			String flightDateString = (String) PropertyUtils.getProperty(otherBean,"flightDate");
			String flightNo = (String) PropertyUtils.getProperty(otherBean,"flightNo");
			String flightCompany = (String) PropertyUtils.getProperty(otherBean,"flightCompany");
			String terminal = (String) PropertyUtils.getProperty(otherBean,"terminal");
			String departure = (String) PropertyUtils.getProperty(otherBean,"departure");
			String scheduleTime = (String) PropertyUtils.getProperty(otherBean,"scheduleTime");
			String actualTime = (String) PropertyUtils.getProperty(otherBean,"actualTime");
			String gate = (String) PropertyUtils.getProperty(otherBean,"gate");
			String status = (String) PropertyUtils.getProperty(otherBean,"status");

			Map returnMap = new HashMap(0);
			Map parameters = new HashMap(0);
			log.info("flightDateString:"+flightDateString);
			log.info(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, flightDateString));
			log.info(DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, flightDateString), DateUtils.C_DATA_PATTON_YYYYMMDD));
			String flightDate = DateUtils.format(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, flightDateString), DateUtils.C_DATA_PATTON_YYYYMMDD);
			parameters.put("prompt0", flightType);
			parameters.put("prompt1", null ==flightDate? "":flightDate);
			parameters.put("prompt2", flightNo);
			parameters.put("prompt3", flightCompany);
			parameters.put("prompt4", terminal);
			parameters.put("prompt5", departure);
			parameters.put("prompt6", scheduleTime);
			parameters.put("prompt7", actualTime);
			parameters.put("prompt8", gate);
			parameters.put("prompt9", status);
			
			String reportUrl = SystemConfig.getReportURLByFunctionCode(loginBrandCode, "T2_W00SO1007", loginEmployeeCode, parameters);
			returnMap.put("reportUrl", reportUrl);
			return AjaxUtils.parseReturnDataToJSON(returnMap);
		}catch(IllegalAccessException iae){
			System.out.println(iae.getMessage());
			throw new IllegalAccessException(iae.getMessage());
		}catch(InvocationTargetException ite){

			System.out.println(ite.getMessage());
			throw new InvocationTargetException(ite, ite.getMessage());
		}catch(NoSuchMethodException nse){
			System.out.println(nse.getMessage());
			throw new NoSuchMethodException("NoSuchMethodException:" +nse.getMessage());
		}
	}
	
	private boolean existFlightData(BuFlightSchedule buFlightSchedule, Map collectMaps) throws Exception {
		log.info("existFlightData...." + DateUtils.format(buFlightSchedule.getFlightDate())+ " "+ buFlightSchedule.getScheduleTime() + "/" +buFlightSchedule.getFlightNo());
		String key = DateUtils.format(buFlightSchedule.getFlightDate()) + buFlightSchedule.getFlightNo() +buFlightSchedule.getFlightType();
		boolean result=true;
		try {
			if (!collectMaps.containsKey(key)) {
				collectMaps.put(key, key);
				result = false;
			}else{
				result=true;
			}
		} catch (Exception ex) {
			throw new Exception("航班資料重覆性確認時發生錯誤，原因：" + ex.getMessage());

		}
		return result;
	}
	
}
