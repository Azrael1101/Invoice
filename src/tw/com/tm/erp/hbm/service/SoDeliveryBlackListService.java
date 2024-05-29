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
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoDeliveryBlackList;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuFlightScheduleDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryBlackListDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.SoDeliveryLogDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderItemDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SoDeliveryBlackListService {
	public static final String PROGRAM_ID = "SO_DELIVERY";
	private static final Log log = LogFactory.getLog(SoDeliveryService.class);

	private SoDeliveryBlackListDAO soDeliveryBlackListDAO;
	
	public static final int SEARCH_NO_MIN_LENGTH = 3;
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
		"passportNo", "name","tel","reason1", 
		"reason2", "remark1", "remark2","headId" };
	
    public static final int[] GRID_SEARCH_FIELD_TYPES = { 
	    AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,
	    AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_STRING,AjaxUtils.FIELD_TYPE_LONG};

    public static final String[] GRID_FIELD_SEARCH_DEFAULT_VALUES = { 
	    "", "", "", "", 
	    "", "", "", ""};	


    
	
	public void setSoDeliveryBlackListDAO(SoDeliveryBlackListDAO soDeliveryBlackListDAO) {
		this.soDeliveryBlackListDAO = soDeliveryBlackListDAO;
	}
	
	
	public String isBlackListByName(String brandCode, String name){
		boolean result=soDeliveryBlackListDAO.isBlackListByName(brandCode, name);
		return result?"Y":"N";
	}
	

	
	public String isBlackListByPassportNo(String brandCode, String passportNo){
		boolean result=soDeliveryBlackListDAO.isBlackListByPassportNo(brandCode, passportNo);
		return result?"Y":"N";
	}

	
	public List<Properties> executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("executeInitial:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			Map multiList = new HashMap(0);
			SoDeliveryBlackList form = null == formId ? executeNew(otherBean, resultMap) : findObj(otherBean, resultMap);
			
			resultMap.put("createdName", StringUtils.hasText(form.getCreatedBy()) ? UserUtils
					.getUsernameByEmployeeCode(form.getCreatedBy()) : "");
			resultMap.put("lastUpdatedName", StringUtils.hasText(form.getLastUpdatedBy()) ? UserUtils
					.getUsernameByEmployeeCode(form.getLastUpdatedBy()) : "");
			System.out.println("finish Initial");
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

	/**
	 * 建立新的入提單
	 *
	 * @param otherBean
	 * @param resultMap
	 * @return
	 * @throws Exception
	 */
	public SoDeliveryBlackList executeNew(Object otherBean, Map resultMap) throws Exception {
		log.info("executeNew....");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String customerName = (String) PropertyUtils.getProperty(otherBean, "customerName");
		String passportNo = (String) PropertyUtils.getProperty(otherBean, "passportNo");
		log.info("executeNew...."+loginBrandCode+"/"+loginEmployeeCode+"/");
			SoDeliveryBlackList form = new SoDeliveryBlackList();
			form.setName(customerName);
			form.setPassportNo(passportNo);
			form.setBrandCode(loginBrandCode);
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setLastUpdateDate(new Date());
			form.setCreationDate(new Date());

			resultMap.put("form", form);
			return form;
	}

	public SoDeliveryBlackList findObj(Object otherBean, Map resultMap) throws Exception {
		log.info("findObject....");
		String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
		Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
		String executeEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployeeCode");
		SoDeliveryBlackList form = (SoDeliveryBlackList)soDeliveryBlackListDAO.findByPrimaryKey(SoDeliveryBlackList.class, formId);
		if (null != form) {
		//	form.setCreatedBy(executeEmployeeCode);
		//	form.setCreationDate(new Date());
		//	form.setLastUpdatedBy(executeEmployeeCode);
		//	form.setLastUpdateDate(new Date());
			
			resultMap.put("form", form);
			return form;
		} else {
			throw new NoSuchDataException("查無此代號(" + formId + ")，於按下「確認」鍵後，將關閉本視窗！");

		}

	}	
	
public List<Properties> save(Map parameterMap) throws Exception {
		
    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		log.info("1.performTransaction");
		String beforeChangeStatus = new String("");
		String employeeCode = new String("");
		String logLevel = new String("SUCCESS");
		String logMessage = new String("");
		Long headId = new Long(0);
		Command cmd_bf = new Command();
		SoDeliveryBlackList po = null;

		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean    = parameterMap.get("vatBeanOther");

			String formIdString = (String) PropertyUtils.getProperty(otherBean, "formId");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String name = (String) PropertyUtils.getProperty(otherBean, "name");
			String passportNo = (String) PropertyUtils.getProperty(otherBean, "passportNo");
			String executeEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "executeEmployeeCode");
			log.info(formIdString);
			Long formId = StringUtils.hasText(formIdString) ? Long.valueOf(formIdString) : null;
			System.out.println("formId:" + formId);
			if(formId == null){
				HashMap findObjs = new HashMap(0);
				findObjs.put("brandCode", loginBrandCode);
				findObjs.put("name", null);
				findObjs.put("passportNo", passportNo);
				findObjs.put("customerCode", null);
				findObjs.put("reason1", null);
				findObjs.put("reason2", null);		
				List<SoDeliveryBlackList> actualForms = soDeliveryBlackListDAO.findBlackList(findObjs);
				if(actualForms.size() >0)
					po = actualForms.get(0);
				else
					po = new SoDeliveryBlackList();
			}else{
				SoDeliveryBlackList actualForm = (SoDeliveryBlackList)soDeliveryBlackListDAO.findByPrimaryKey(SoDeliveryBlackList.class, formId);
				po = null!=actualForm?actualForm : new SoDeliveryBlackList();
			}
			
			System.out.println("======Start copyJSONBeantoPojoBean=========");
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, po);
			System.out.println("======Finish copyJSONBeantoPojoBean=========");
			log.info("brandCode:"+po.getBrandCode()+"/name:"+po.getName()+"/passportNo:"+po.getPassportNo());
			po.setLastUpdatedBy(executeEmployeeCode);
			po.setLastUpdateDate(new Date());
			if(null == po.getHeadId()){
				po.setCreatedBy(executeEmployeeCode);
				po.setCreationDate(new Date());
				soDeliveryBlackListDAO.save(po);
			}else{
				soDeliveryBlackListDAO.update(po);
				//po.setLastUpdatedBy(employeeCode);
				po.setLastUpdateDate(new Date());
			}
			
			logLevel = "SUCCESS";
			logMessage=po.getName()+"己加入黑名單";
			msgBox.setMessage(logMessage);
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "", "" });
			msgBox.setOk(cmd_bf);
		} catch (FormException fex) {
			msgBox.setMessage("加入黑名單時發生錯誤");
			logLevel = "ERROR";
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("加入黑名單時發生錯誤，原因：" + ex.toString());
			log.error("加入黑名單時失敗，原因：" + ex.toString());
			msgBox.setMessage(ex.getMessage());
			logLevel = "ERROR";
			logMessage="加入黑名單時錯誤";
			cmd_bf.setCmd(Command.FUNCTION);
			cmd_bf.setParameters(new String[] { "showMessage()", "" });
			msgBox.setOk(cmd_bf);
		}
	
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
    }

	public List<Properties> isBlackListByPassportNo(Map parameterMap) throws Exception {
	Map resultMap = new HashMap(0);

	try {
		log.info("isBlackListByPassportNo:" + parameterMap.keySet().toString());
		Object otherBean = parameterMap.get("vatBeanOther");
		String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
		String passportNo = (String) PropertyUtils.getProperty(otherBean, "passportNo");
		
		if(soDeliveryBlackListDAO.isBlackListByPassportNo(brandCode, passportNo))
			resultMap.put("isBlackList", "Y");
		else
			resultMap.put("isBlackList", "N");
		System.out.println("finish isBlackListByPassportNo");
	} catch (Exception ex) {
		log.error("檢核黑名單失敗，原因：" + ex.toString());
		Map messageMap = new HashMap();
		messageMap.put("type", "ALERT");
		messageMap.put("message", "檢核黑名單失敗，原因：" + ex.toString());
		messageMap.put("event1", null);
		messageMap.put("event2", null);
		resultMap.put("vatMessage", messageMap); 

	}

	return AjaxUtils.parseReturnDataToJSON(resultMap);

	}
	
	
	public List<Properties> isBlackListByTel(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);

		try {
			log.info("isBlackListByTel:" + parameterMap.keySet().toString());
			Object otherBean = parameterMap.get("vatBeanOther");
			String brandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			String passportNo = (String) PropertyUtils.getProperty(otherBean, "customerInfo");
			
			if(soDeliveryBlackListDAO.isBlackListByPassportNo(brandCode, passportNo))
				resultMap.put("isTelBlackList", "Y");
			else
				resultMap.put("isTelBlackList", "N");
			System.out.println("finish isBlackListByTel");
		} catch (Exception ex) {
			log.error("檢核黑名單失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type", "ALERT");
			messageMap.put("message", "檢核黑名單失敗，原因：" + ex.toString());
			messageMap.put("event1", null);
			messageMap.put("event2", null);
			resultMap.put("vatMessage", messageMap); 

		}

		return AjaxUtils.parseReturnDataToJSON(resultMap);

		}
	
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageData...");
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String passportNo = httpRequest.getProperty("passportNo");
			String customerName = httpRequest.getProperty("customerName");
			String reason1 = httpRequest.getProperty("reason1");
			// ======================帶入Head的值=========================

			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", httpRequest.getProperty("brandCode"));
			findObjs.put("passportNo", httpRequest.getProperty("passportNo"));
			findObjs.put("customerName",  httpRequest.getProperty("customerName"));
			findObjs.put("reason1", httpRequest.getProperty("reason1"));
			
			// ==============================================================

			List<SoDeliveryBlackList> soDeliveryBlackLists = (List<SoDeliveryBlackList>) soDeliveryBlackListDAO.findByMap(findObjs, iSPage,
					iPSize, soDeliveryBlackListDAO.QUARY_TYPE_SELECT_RANGE).get("form");

			log.info("SoDeliveryBlackList.size" + soDeliveryBlackLists.size());

			if (soDeliveryBlackLists != null && soDeliveryBlackLists.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				Long maxIndex = (Long) soDeliveryBlackListDAO.findByMap(findObjs, -1, iPSize,
						soDeliveryBlackListDAO.QUARY_TYPE_RECORD_COUNT).get("recordCount"); // 取得最後一筆

				log.info("AjaxUtils.getAJAXPageData ");
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_FIELD_SEARCH_DEFAULT_VALUES,
						soDeliveryBlackLists, gridDatas, firstIndex, maxIndex));

			} else {
				log.info("AjaxUtils.getAJAXPageDataDefault ");
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES,
						GRID_FIELD_SEARCH_DEFAULT_VALUES, map, gridDatas));

			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的入提查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的入提查詢失敗！");
		}
	}

}
