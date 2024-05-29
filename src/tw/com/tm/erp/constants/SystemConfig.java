package tw.com.tm.erp.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.SiMenuService;
import tw.com.tm.erp.utils.DES;

public class SystemConfig {
	private static final Log log = LogFactory.getLog(SystemConfig.class);
	public static final String USER_SESSION_NAME = "userObj";
	public static final String SPRCIAL_PARAM = "specialParam";
	// 提供去COMMIN PHASE LINE 抓取 BRDType , LineCode = brandCode +
	// BIRTHDAY_PROMOTION_KEY_WORLD
	public static final String BIRTHDAY_PROMOTION_KEY_WORLD = "BRD";
	public static final String POS_UPLOAD_FOLDER[] = { "//192.168.66.12/PdcdataFp/BACKUP/", "//192.168.66.12/PdcdataFp/BACKUP/",
			"//192.168.66.12/PdcdataFp/BACKUP/", "//192.168.66.12/PdcdataFp/BACKUP/" };
	public static final String POS_DOWNLOAD_FOLDER[] = { "//192.168.66.12/PdcdataFp/DL/", "//192.168.66.12/PdcdataFp/DL/",
			"//192.168.66.12/PdcdataFp/DL/", "//192.168.66.12/PdcdataFp/DL/" };
	public static final int SEARCH_PAGE_MAX_COUNT = 100;
	public static final int SEARCH_WAREHOUSE_MAX_COUNT = 200;
	public static final String SEARCH_PAGE_MAX_COUNT_ALERT_MSG = "請增加查詢的條件讓結果更精確，以便於您的閱覽及維護。\r\n (過多的筆數會導致系統效率下降, 如果需要查詢大量的資料請使用報表或分析工具)";
	public static final String PROPERTIES_FILE_NAME = "system_config.properties";
	private static Properties SYS_PRO = null; // 從
	public static final String LOT_NO = "000000000000" ;

	/**
	 * 判斷SYS模式 D,P
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	public static boolean isProductionSystemMode() {
		log.info("SystemConfig.isProductionSystemMode");
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("SystemConfig", "SystemMode");
		if (null != buCommonPhraseLine) {
			String value = buCommonPhraseLine.getName();
			if (StringUtils.hasText(value)) {
				if ("D".equalsIgnoreCase(value)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 取得系統參數
	 * 
	 * @param propertiesName
	 * @return
	 */
	public static String getSystemConfigPro(String propertiesName) {
		log.info("SystemConfig.isProductionSystemMode");
		if (null == SystemConfig.SYS_PRO) {
			try {
				setSystemConfig();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != SystemConfig.SYS_PRO) {
			return SystemConfig.SYS_PRO.getProperty(propertiesName);
		}
		return null;
	}

	/**
	 * 指定系統參數
	 * 
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void setSystemConfig() throws ClassNotFoundException, FileNotFoundException, IOException {
		log.info("SystemConfig.setSystemConfig space");
		// 20081125 shan
		if (null == SystemConfig.SYS_PRO) {
			Properties properties = new Properties();
			URL classPath = SystemConfig.class.getResource("");
			log.info("SystemConfig.SystemService init " + classPath.getPath() + "-" + SystemConfig.PROPERTIES_FILE_NAME);
			File propPath = new File(classPath.getPath() + SystemConfig.PROPERTIES_FILE_NAME);
			properties.load(new FileInputStream(propPath));
			if (null == SystemConfig.SYS_PRO) {
				SystemConfig.SYS_PRO = new Properties();
				Enumeration keys = properties.keys();
				while (keys.hasMoreElements()) {
					Object key = keys.nextElement();
					Object value = properties.get(key);
					SystemConfig.SYS_PRO.put(key, value);
				}
			}
		}
	}

	public static String getReportURL(String orderTypeCode, String brandCode, String employeeCode, String orderNo) throws Exception {
		log.info("SystemConfig.getReportURL orderTypeCode=" + orderTypeCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode + ",orderNo=" + orderNo );
		String reportUrl = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
		if (buCommonPhraseLine != null) {
			reportUrl = buCommonPhraseLine.getAttribute1();
		}

		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
		BuOrderTypeId id = new BuOrderTypeId();
		id.setBrandCode(brandCode);
		id.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(id);

		String reportFunctionCode = buOrderType.getReportFunctionCode();
		String reportFileName = buOrderType.getReportFileName();
		String permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";

		String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		String re = "window.open('" + reportUrl + reportFileName + "?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1="
				+ orderTypeCode + "&prompt2=" + orderNo
				+ "','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0')";
		return re;
	}
	
	public static String getReportURL(String orderTypeCode, String brandCode, String employeeCode, String orderNo , String reportFileName ) throws Exception {
		log.info("SystemConfig.getReportURL orderTypeCode=" + orderTypeCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode + ",orderNo=" + orderNo + ",reportFileName=" + reportFileName);
		String reportUrl = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
		if (buCommonPhraseLine != null) {
			reportUrl = buCommonPhraseLine.getAttribute1();
		}

		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
		BuOrderTypeId id = new BuOrderTypeId();
		id.setBrandCode(brandCode);
		id.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(id);

		String reportFunctionCode = buOrderType.getReportFunctionCode();
		
		String permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
		String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		String re = "window.open('" + reportUrl + reportFileName + "?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1="
				+ orderTypeCode + "&prompt2=" + orderNo
				+ "','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0')";
		return re;
	}	
	
	/** 2009.11.18 for 3.0 arthur */
	public static String getReportURLString(String orderTypeCode, String brandCode, String employeeCode, String orderNo) throws Exception {
		log.info("SystemConfig.getReportURL orderTypeCode=" + orderTypeCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode + ",orderNo=" + orderNo );
		String reportUrl = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
		if (buCommonPhraseLine != null) {
			reportUrl = buCommonPhraseLine.getAttribute1();
		}

		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
		BuOrderTypeId id = new BuOrderTypeId();
		id.setBrandCode(brandCode);
		id.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(id);

		String reportFunctionCode = buOrderType.getReportFunctionCode();
		String reportFileName = buOrderType.getReportFileName();
		String permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";

		String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		String re = reportUrl + reportFileName + "?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1="
				+ orderTypeCode + "&prompt2=" + orderNo
				+ "','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0'";
		return re;
	}
	
	
	/** 2009.11.18 for 3.0 arthur */
	public static String getReportURLString(String orderTypeCode, String brandCode, String employeeCode, String orderNo , String reportFileName ) throws Exception {
		log.info("SystemConfig.getReportURL orderTypeCode=" + orderTypeCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode + ",orderNo=" + orderNo + ",reportFileName=" + reportFileName);
		String reportUrl = null;
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
		if (buCommonPhraseLine != null) {
			reportUrl = buCommonPhraseLine.getAttribute1();
		}

		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
		BuOrderTypeId id = new BuOrderTypeId();
		id.setBrandCode(brandCode);
		id.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(id);

		String reportFunctionCode = buOrderType.getReportFunctionCode();
		
		String permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
		String encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		String re = reportUrl + reportFileName + "?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1="
				+ orderTypeCode + "&prompt2=" + orderNo
				+ "','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0'";
		return re;
	}
	

	/**
	 * clear cache
	 */
	public static void clearSystemConfigPro() {
		SystemConfig.SYS_PRO = null;
	}
	
	
	public static String getReportURL(String brandCode, String orderTypeCode, String employeeCode, String reportFileName, Map parameters) throws Exception {
		log.info("SystemConfig.getReportURL orderTypeCode=" + orderTypeCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode  );
		String re = new String("");
		String reportUrl = new String("");
		String reportFunctionCode = new String("");
		//String reportFileName = new String("");
		String permissionInfo = new String("");
		String encryText = new String("");
		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
		BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode) );
		String commonPhraseLine = (brandCode.indexOf("T2") > -1) ?"T2CrystalReportURL":"CrystalReportURL";
		if(null != buOrderType){
			reportFunctionCode = buOrderType.getReportFunctionCode();
			if(null == reportFileName)
				reportFileName = buOrderType.getReportFileName();
			permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
			encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
			BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
			BuCommonPhraseLine reportConfig = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", commonPhraseLine);		
			reportUrl =  reportConfig != null? reportConfig.getAttribute1():"";
			
			Iterator it = parameters.keySet().iterator();
			StringBuffer paramaterString = new StringBuffer("");
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = (String) parameters.get(key);
				paramaterString.append("&"+key+"="+value);
			}			
			
			re = "window.open('" + reportUrl + reportFileName + "?crypto=" + encryText +paramaterString.toString()+
					"','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0')";
			
			log.info(re);
		}else{
			throw new NoSuchDataException("查無"+brandCode+"單別資訊("+orderTypeCode+")，於按下「確認」鍵後，將關閉本視窗！");
		}		
		return re;
	}
	
	
	public static String getReportURLByFunctionCode(String brandCode, String reportFunctionCode, String employeeCode, Map parameters) throws Exception {
		//log.info("SystemConfig.getReportURL reportFunctionCode=" + reportFunctionCode + ",brandCode=" + brandCode + ",employeeCode=" + employeeCode  );
		String re = new String("");
		String reportUrl = new String("");
		String permissionInfo = new String("");
		String encryText = new String("");
		
		permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
		encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		SiMenuService siMenuService = (SiMenuService)SpringUtils.getApplicationContext().getBean("siMenuService");	
		List<SiMenu>reportUrls = siMenuService.getFunctionURL(null, null, reportFunctionCode);
		if(null==reportUrls)
			throw new NoSuchDataException("查無"+brandCode+"報表資訊("+reportFunctionCode+")，於按下「確認」鍵後，將關閉本視窗！");
		else
			reportUrl = reportUrls.get(0).getUrl();
		
		Iterator it = parameters.keySet().iterator();
		StringBuffer paramaterString = new StringBuffer("");
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) parameters.get(key);
			paramaterString.append("&"+key+"="+value);
		}
		//System.out.println("reportUrl:"+reportUrl);
		int at = reportUrl.indexOf("?");
		at = reportUrl.indexOf("?")<0 ? reportUrl.length():at;
		//System.out.println("at:"+reportUrl.substring(0, at));
		re = "window.open('" + reportUrl.substring(0, at) + "?crypto=" + encryText +paramaterString.toString()+
					"','BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0')";
			
		log.info(re);
		return re;
	}
	
	public static String getReportURL(String brandCode, String orderTypeCode, String employeeCode, Map parameters) throws Exception {
		return getReportURL(brandCode, orderTypeCode, employeeCode, null, parameters);
	}
	
	/**
	 * 純粹取得URL
	 * @param brandCode
	 * @param orderTypeCode
	 * @param employeeCode
	 * @param reportFileName
	 * @param parameters
	 * @return
	 */
	public static String getPureReportURL(String brandCode, String orderTypeCode, String employeeCode, String reportFileName, Map parameters) throws Exception{
	    String re = new String("");
	    String reportUrl = new String("");
	    String reportFunctionCode = new String("");
	    //String reportFileName = new String("");
	    String permissionInfo = new String("");
	    String encryText = new String("");
	    BuOrderTypeService buOrderTypeService = (BuOrderTypeService) SpringUtils.getApplicationContext().getBean("buOrderTypeService");
	    BuOrderType buOrderType = buOrderTypeService.findById(new BuOrderTypeId(brandCode, orderTypeCode) );
	    String commonPhraseLine = (brandCode.indexOf("T2") > -1) ?"T2CrystalReportURL":"CrystalReportURL";
	    if(null != buOrderType){
		reportFunctionCode = buOrderType.getReportFunctionCode();
		if(null == reportFileName)
		    reportFileName = buOrderType.getReportFileName();
		permissionInfo = brandCode + "@@" + employeeCode + "@@" + reportFunctionCode + "@@";
		encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
		BuCommonPhraseLine reportConfig = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", commonPhraseLine);		
		reportUrl =  reportConfig != null? reportConfig.getAttribute1():"";

		Iterator it = parameters.keySet().iterator();
		StringBuffer paramaterString = new StringBuffer("");
		while (it.hasNext()) {
		    String key = (String) it.next();
		    String value = (String) parameters.get(key);
		    paramaterString.append("&"+key+"="+value);
		}	

		re = reportUrl + reportFileName + "?crypto=" + encryText +paramaterString.toString();
	    }

	    return re;
	}
}
