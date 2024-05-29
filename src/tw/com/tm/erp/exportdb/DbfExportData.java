package tw.com.tm.erp.exportdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;
import tw.com.tm.erp.utils.dbf.DBFUtils;
import tw.com.tm.erp.utils.dbf.JDBFException;
import tw.com.tm.erp.utils.dbf.JDBField;

public class DbfExportData {
    private static final Log log = LogFactory.getLog(DbfExportData.class);
    
    private static Map<String, JDBField[]> templateFields = new HashMap();
    private static Properties iniProperties = null;
    private static String classPath = null;
    private Connection conn = null;
    
    private DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }
    
//    private Map configMap;
    private NativeQueryDAO nativeQueryDAO;
    
    private BuBrandDAO buBrandDAO;
    private BaseDAO baseDAO;
    private BuCommonPhraseService buCommonPhraseService;
    
    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
        this.nativeQueryDAO = nativeQueryDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

//    public void setConfigMap(Map configMap) {
//        this.configMap = configMap;
//    }

    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
        this.buCommonPhraseService = buCommonPhraseService;
    }

    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
        this.buBrandDAO = buBrandDAO;
    }

    /**
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map executeDBFInitial(Map parameterMap) throws Exception{
	Map resultMap = new HashMap(0);

	try{
	    Object otherBean = parameterMap.get("vatBeanOther");

	    String loginEmployeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    String loginBrandCode = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");

	    String employeeName = UserUtils.getUsernameByEmployeeCode(loginEmployeeCode);

	    Map multiList = new HashMap(0);
	    resultMap.put("brandName",buBrandDAO.findById(loginBrandCode).getBrandName());
	    resultMap.put("employeeName", employeeName);
	    resultMap.put("employeeCode", loginEmployeeCode);
	    resultMap.put("brandCode", loginBrandCode);

	    List<BuCommonPhraseLine> allBudgetYearLists = buCommonPhraseService.getCommonPhraseLinesById("BudgetYearList", false);
	    List<BuCommonPhraseLine> allMonths = buCommonPhraseService.getCommonPhraseLinesById("Month", false);
	    List<BuCommonPhraseLine> allExportTypes = buCommonPhraseService.getCommonPhraseLinesById("ExportDBFType", false);
	    
	    multiList.put("allBudgetYearLists"	, AjaxUtils.produceSelectorData(allBudgetYearLists, "lineCode", "name", false, true ));
	    multiList.put("allMonths"	, AjaxUtils.produceSelectorData(allMonths, "lineCode", "name", false, true ));
	    multiList.put("allExportTypes"	, AjaxUtils.produceSelectorData(allExportTypes, "lineCode", "name", false, true ));
	    
	    resultMap.put("multiList",multiList);
	    return resultMap;
	}catch(Exception ex){
	    log.error("匯出初始化失敗，原因：" + ex.toString());
	    throw new Exception("匯出初始化失敗，原因：" + ex.toString());

	}
    }
    
    /**
     * 執行匯出
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public Map exportDBF(Map parameterMap)throws Exception{
    	Map resultMap = new HashMap();
    	String resultMsg = null;
    	try {
    		DecimalFormat df = new DecimalFormat("00");

//  		Object otherBean = parameterMap.get("vatBeanOther");
    		Object formLinkBean = parameterMap.get("vatBeanFormLink");

    		String year = (String)PropertyUtils.getProperty(formLinkBean, "year");
    		String month = (String)PropertyUtils.getProperty(formLinkBean, "month");
    		String customerWarehouse = (String)PropertyUtils.getProperty(formLinkBean, "customerWarehouse");
    		String exportType = (String)PropertyUtils.getProperty(formLinkBean, "exportType");
    		String brandCode = (String)PropertyUtils.getProperty(formLinkBean, "brandCode");
    		String employeeCode = (String)PropertyUtils.getProperty(formLinkBean, "employeeCode");
    		Integer pageSize = 5000;

    		//log.info("DbfConfig = DbfConfig" );
    		//log.info("customerWarehouse = " + customerWarehouse);
    		// 撈資料庫 BU_COMMON_PHRASE_HEAD CustomsAuditConfig
    		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("DbfConfig", customerWarehouse);
    		if(null == buCommonPhraseLine){
    			throw new Exception("查 BuCommonPhraseLine 無 DbfConfig 配置 ");
    		}
    		String path = buCommonPhraseLine.getAttribute1();
    		pageSize = NumberUtils.getInt(buCommonPhraseLine.getAttribute2());

    		// 檔名
    		String dataArea = buCommonPhraseLine.getAttribute3();
    		String stock = buCommonPhraseLine.getAttribute4();
    		String i = buCommonPhraseLine.getAttribute5()+ year.substring(2)+df.format(NumberUtils.getInt(month)) + ".dbf";
    		String d = buCommonPhraseLine.getParameter1()+ year.substring(2)+df.format(NumberUtils.getInt(month)) + customerWarehouse + ".dbf";
    		String m = buCommonPhraseLine.getParameter2()+ year.substring(2)+df.format(NumberUtils.getInt(month)) + customerWarehouse + ".dbf";
    		String ds = buCommonPhraseLine.getParameter3() + ".dbf";
    		String de = buCommonPhraseLine.getParameter4() + ".dbf";

    		if( 0 == pageSize ){
    			pageSize = 5000;
    		}

    		if(!StringUtils.hasText(path)){
    			throw new Exception( "查無此路徑" + path );
    		}

    		// 建立路徑 年\月\關別
    		// 年
    		File folder_year = new File(path+"\\"+year);
    		if(!folder_year.isDirectory()){
    			folder_year.mkdir();
    		}

    		// 月
    		File folder_month = new File(path+"\\"+year+"\\"+month);
    		if(!folder_month.isDirectory()){
    			folder_month.mkdir();
    		}	 

    		// 關別
    		File folder_warehrouse = new File(path+"\\"+year+"\\"+month+"\\"+customerWarehouse);
    		if(!folder_warehrouse.isDirectory()){
    			folder_warehrouse.mkdir();
    		}

    		String basePath = path+"\\"+year+"\\"+month+"\\"+customerWarehouse;
    		Map conditionMap = new HashMap();
    		conditionMap.put("year", year);
    		conditionMap.put("month", month);
    		conditionMap.put("exportType", exportType);
    		conditionMap.put("customerWarehouse", customerWarehouse);
    		conditionMap.put("brandCode", brandCode);
    		conditionMap.put("employeeCode", employeeCode);
    		conditionMap.put("PAGE_SIZE", pageSize);
    		conditionMap.put("BASE_PATH", basePath);

    		conditionMap.put("DATA_AREA_FILE_NAME", dataArea);
    		conditionMap.put("STOCK_FILE_NAME", stock);
    		conditionMap.put("I_FILE_NAME", i);
    		conditionMap.put("D_FILE_NAME", d);
    		conditionMap.put("M_FILE_NAME", m);
    		conditionMap.put("DS_FILE_NAME", ds);
    		conditionMap.put("DE_FILE_NAME", de);

    		log.info("year = " + year);
    		log.info("month = " + month);
    		log.info("customerWarehouse = " + customerWarehouse);
    		log.info("exportType = " + exportType);
    		log.info("brandCode = " + brandCode);
    		log.info("employeeCode = " + employeeCode);
    		log.info("pageSize = " + pageSize);
    		log.info("basePath = " + basePath);
    		log.info("dataArea = " + dataArea);
    		log.info("stock = " + stock);
    		log.info("i = " + i);
    		log.info("d = " + d);
    		log.info("m = " + m);
    		log.info("ds = " + ds);
    		log.info("de = " + de);

    		setIniProperties();

    		// 依匯出類型撈資料
    		if("DataArea".equalsIgnoreCase(exportType)){
    			exportDataArea(conditionMap);
    		}else if("Stock".equalsIgnoreCase(exportType)){
    			exportStock(conditionMap);
    		}else if("I".equalsIgnoreCase(exportType)){
    			exportI(conditionMap);
    		}else if("D".equalsIgnoreCase(exportType)){
    			exportD(conditionMap);
    		}else if("M".equalsIgnoreCase(exportType)){
    			exportM(conditionMap);
    		}else if("DS".equalsIgnoreCase(exportType)){
    			exportDS(conditionMap);
    		}else if("DE".equalsIgnoreCase(exportType)){
    			exportDE(conditionMap);
    		}else if("HS".equalsIgnoreCase(exportType)){
    			exportHS(conditionMap);
    		}else if("HE".equalsIgnoreCase(exportType)){
    			exportHE(conditionMap);
    		}else if("MS".equalsIgnoreCase(exportType)){
    			exportMS(conditionMap);
    		}else if("ME".equalsIgnoreCase(exportType)){
    			exportME(conditionMap);
    		}
    		// 匯出DBF

    		// 顯示前端alert訊息
    		resultMsg = "年：" + year + " 月:" + month + " 關別:"+ customerWarehouse +" 匯出成功！ 是否繼續？";

    		resultMap.put("resultMsg", resultMsg);
    	} catch (Exception e) {
    		log.error("執行匯出DBF發生錯誤，原因：" + e.toString());
    		e.printStackTrace();
    		throw new Exception("執行匯出DBF發生錯誤，原因：" + e.getMessage());
    	}
    	return resultMap;
    }
    
    /**
     * 匯出
     * @param map
     */
    private void exportDataArea(Map conditionMap) throws IOException, JDBFException, Exception{
	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
	int year = NumberUtils.getInt((String)conditionMap.get("year"));
	int month = NumberUtils.getInt((String)conditionMap.get("month"));
	String employeeCode = (String)conditionMap.get("employeeCode"); // templete
	String basePath = (String)conditionMap.get("BASE_PATH");
	String dataAreaFileName = (String)conditionMap.get("DATA_AREA_FILE_NAME");
	
	String fileName = iniProperties.getProperty("dataareaTemp");
	String dataareaTemp = classPath + "/" + fileName;
	
	log.info("dataareaTemp = " + dataareaTemp);
	
	String strYear = String.valueOf(year);
	DecimalFormat df = new DecimalFormat("00");
	
	JDBField[] fields = templateFields.get(dataareaTemp); // templete
	if (null == fields) {
	    fields = DBFUtils.getJDBField(dataareaTemp);
	    templateFields.put(dataareaTemp, fields); // templete
	}
	List<Object[]> records = new ArrayList();
	
	log.info("fields = " + fields);
	
	Object[] record = new Object[fields.length];
	
	Date startDate = getDate(year, month, 1);
	Date endDate = getDate(year, month, DateUtils.getLastDayOfMonth(year, month));
	
	String customerWarehouseName = null;
	if("FW".equalsIgnoreCase(customerWarehouse)){
	    customerWarehouseName = "南崁倉";
	}else if("FD".equalsIgnoreCase(customerWarehouse)){
	    customerWarehouseName = "免稅店";
	}

	record[0] = trimString(customerWarehouse, fields[0].getLength()); 	//關別
	record[1] = trimString(customerWarehouseName, fields[1].getLength());		//關別名稱
	record[2] = startDate;	//日期起  
	record[3] = endDate;	//日期迄
	record[4] = trimString("M" + strYear.substring(2, strYear.length()) + df.format(month)+customerWarehouse, fields[4].getLength());;	//主檔
	record[5] = trimString("D" + strYear.substring(2, strYear.length()) + df.format(month)+customerWarehouse, fields[5].getLength());	//明細檔
	record[6] = trimString(UserUtils.getUsernameByEmployeeCode(employeeCode), fields[6].getLength());	//製作人
	record[7] = DateUtils.getCurrentDate(); //製作日期
	
	records.add(record);
	
	DBFUtils.writeDBF(basePath+"/"+dataAreaFileName, records, fields); // D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DataArea.dbf
    }
    

    /**
     * 匯出 期末庫存
     * @param map
     */
    private void exportStock(Map conditionMap) throws IOException, JDBFException{
//	String templete = (String)configMap.get(conditionMap.get("exportType"));
	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
	String year = (String)conditionMap.get("year");
	String month = (String)conditionMap.get("month");
	String employeeCode = (String)conditionMap.get("employeeCode");
	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
	String brandCode = (String)conditionMap.get("brandCode");
	String basePath = (String)conditionMap.get("BASE_PATH");
	String stockFileName = (String)conditionMap.get("STOCK_FILE_NAME");
	
	String fileName = iniProperties.getProperty("stockTemp");
	String stockTemp = classPath + "/" + fileName;
	
	DecimalFormat df = new DecimalFormat("00");
	month = df.format(NumberUtils.getInt(month));
	JDBField[] fields = templateFields.get(stockTemp);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(stockTemp);
	    templateFields.put(stockTemp, fields);
	}
	
	List<Object[]> records = new ArrayList();
	Double count = 0D;
	StringBuffer sqlMax = new StringBuffer();
	sqlMax.append("SELECT count(CUSTOMS_ITEM_CODE) FROM ( ")
	.append("SELECT CUSTOMS_ITEM_CODE, SUPPLIER_ITEM_CODE, ITEM_C_NAME, IS_TAX, SUM(ENDING_ON_HAND_QTY) AS TOTAL_ENDING_ON_HAND_QTY, SUM(totalCount) AS totalPriceCount, ITEM_BRAND, CATEGORY13, UNIT_PRICE FROM ( ")
	.append("SELECT CDV.CUSTOMS_ITEM_CODE, I.SUPPLIER_ITEM_CODE, I.ITEM_C_NAME, I.IS_TAX, CDV.ENDING_ON_HAND_QTY, NVL(CDV.ENDING_ON_HAND_QTY,0) * NVL(I.UNIT_PRICE,0) AS totalCount, I.ITEM_BRAND, I.CATEGORY13, NVL(I.UNIT_PRICE,0) AS UNIT_PRICE ")
	.append("FROM CM_DECLARATION_MONTHLY_BALNACE CDV, IM_ITEM_CURRENT_PRICE_VIEW I ")
	.append("WHERE CDV.BRAND_CODE = I.BRAND_CODE(+) ")
	.append("AND CDV.CUSTOMS_ITEM_CODE = I.ITEM_CODE(+) ")
	.append("AND CDV.BRAND_CODE = '").append(brandCode).append("' ")
	.append("AND CDV.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
	.append("AND CDV.YEAR = '").append(year).append("' ")
	.append("AND CDV.MONTH = '").append(month).append("' ");
	sqlMax.append(") GROUP BY CUSTOMS_ITEM_CODE, SUPPLIER_ITEM_CODE, ITEM_C_NAME, IS_TAX, ITEM_BRAND, CATEGORY13, UNIT_PRICE ")
	.append("HAVING SUM(ENDING_ON_HAND_QTY) <> 0 ) ");
	
	List countList = nativeQueryDAO.executeNativeSql(sqlMax.toString() );
	if(countList.size() > 0 ){
	    count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
	}
	log.info("countList = " + count);
	log.info("pageSize = " + pageSize);
	log.info("Math.ceil(count/pageSize) = " + Math.ceil(count/pageSize));
	for (int page = 0; page < Math.ceil(count/pageSize) ; page++) {
	    StringBuffer sql = new StringBuffer();
	    
	    sql.append("SELECT CUSTOMS_ITEM_CODE, SUPPLIER_ITEM_CODE, ITEM_C_NAME, IS_TAX, SUM(ENDING_ON_HAND_QTY) AS TOTAL_ENDING_ON_HAND_QTY, SUM(totalCount) AS totalPriceCount, ITEM_BRAND, CATEGORY13, UNIT_PRICE FROM ( ");
	    sql.append("SELECT CDV.CUSTOMS_ITEM_CODE, I.SUPPLIER_ITEM_CODE, I.ITEM_C_NAME, I.IS_TAX, CDV.ENDING_ON_HAND_QTY, NVL(CDV.ENDING_ON_HAND_QTY,0) * NVL(I.UNIT_PRICE,0) AS totalCount, I.ITEM_BRAND, I.CATEGORY13, NVL(I.UNIT_PRICE,0) AS UNIT_PRICE ")
	    .append("FROM CM_DECLARATION_MONTHLY_BALNACE CDV, IM_ITEM_CURRENT_PRICE_VIEW I ")
	    .append("WHERE CDV.BRAND_CODE = I.BRAND_CODE(+) ")
	    .append("AND CDV.CUSTOMS_ITEM_CODE = I.ITEM_CODE(+) ")
	    .append("AND CDV.BRAND_CODE = '").append(brandCode).append("' ")
	    .append("AND CDV.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
	    .append("AND CDV.YEAR = '").append(year).append("' ")
	    .append("AND CDV.MONTH = '").append(month).append("' ");
	    sql.append(") GROUP BY CUSTOMS_ITEM_CODE, SUPPLIER_ITEM_CODE, ITEM_C_NAME, IS_TAX, ITEM_BRAND, CATEGORY13, UNIT_PRICE ")
	    .append("HAVING SUM(ENDING_ON_HAND_QTY) <> 0 ");
	    
	    List list = nativeQueryDAO.executeNativeSql(sql.toString() ,page ,pageSize );
	    log.info("list = " + list.size());
	    for (Object obj : list) {
		Object[] record = new Object[fields.length];
//		log.info("((Object[])obj)[0] = " + ((Object[])obj)[0]);
//		log.info("((Object[])obj)[1] = " + ((Object[])obj)[1]);
//		log.info("((Object[])obj)[2] = " + ((Object[])obj)[2]);
//		log.info("((Object[])obj)[3] = " + ((Object[])obj)[3]);
//		log.info("((Object[])obj)[4] = " + ((Object[])obj)[4]);
//		log.info("((Object[])obj)[5] = " + ((Object[])obj)[5]);
//		log.info("((Object[])obj)[6] = " + ((Object[])obj)[6]);
//		log.info("((Object[])obj)[7] = " + ((Object[])obj)[7]);
//		log.info("((Object[])obj)[8] = " + ((Object[])obj)[8]);
		
		record[0] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[0], ""), fields[0].getLength()); 	// 品號
		record[1] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[1], ""), fields[1].getLength());	// 廠商貨號
		record[2] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[2], ""), fields[2].getLength());	// 品名
		record[3] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[3], ""), fields[3].getLength());   	// fp
		record[4] = (BigDecimal)((Object[])obj)[4];						// 數量
		record[5] = (BigDecimal)((Object[])obj)[5];						// 庫存金額
		record[6] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[6], ""), fields[5].getLength());	// 商品品牌
		record[7] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[7], ""), fields[6].getLength()); 	// 系列
		record[8] = (BigDecimal)((Object[])obj)[8]; 						// 訂價

		records.add(record);
	    }
	}
	
	DBFUtils.writeDBF(basePath+"/"+stockFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/Stock.dbf"
    }
    
    /**
     * 匯出 I
     * @param map
     */
    private void exportI(Map conditionMap) throws Exception, IOException, JDBFException{
	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
	String year = (String)conditionMap.get("year");
	String month = (String)conditionMap.get("month");
	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
	String brandCode = (String)conditionMap.get("brandCode");
	String basePath = (String)conditionMap.get("BASE_PATH");
	String iFileName = (String)conditionMap.get("I_FILE_NAME");
	
	DecimalFormat df = new DecimalFormat("00");
	
	String fileName = iniProperties.getProperty("iTemp");
	String iTemp = classPath + "/" + fileName;
	
	JDBField[] fields = templateFields.get(iTemp);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(iTemp);
	    templateFields.put(iTemp, fields);
	}
	
	String startDate = year+"-"+df.format(NumberUtils.getInt(month))+"-"+"01";
	
	String endDate = DateUtils.format(DateUtils.getLastDateOfMonth(DateUtils.parseDate(startDate)), DateUtils.C_DATE_PATTON_DEFAULT);
	log.info(" startDate = " + startDate);
	log.info(" endDate = " + endDate);
	
	List<Object[]> records = new ArrayList();
	
	List<String> deteList =  DateUtils.getDaysBetweenList(startDate, endDate);
	
	for (String date : deteList) {
	    StringBuffer sqlMax = new StringBuffer();
	    sqlMax.append("SELECT COUNT(ORDER_NO) FROM ( ");
	    if("FW".equals(customerWarehouse)){
		sqlMax.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO,  NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.DECLARATION_NO, I.SUPPLIER_ITEM_CODE ")
		.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		.append("AND T.ITEM_CODE = I.ITEM_CODE ")
		.append("AND T.BRAND_CODE = W.BRAND_CODE ")
		.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		.append("AND T.DECLARATION_NO IS NOT NULL ")
		.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND T.ORDER_TYPE_CODE IN ( 'ADF', 'MEF', 'EOF', 'EIF', 'ESF' ) ")
		.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
	    }else if("FD".equals(customerWarehouse)){
		sqlMax.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
		.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		.append("AND T.ITEM_CODE = I.ITEM_CODE ")
		.append("AND T.BRAND_CODE = W.BRAND_CODE ")
		.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
		.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		.append("AND I.IS_TAX = 'F' ")
		.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
	    }else if("HD".equals(customerWarehouse)){
		sqlMax.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
		.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		.append("AND T.ITEM_CODE = I.ITEM_CODE ")
		.append("AND T.BRAND_CODE = W.BRAND_CODE ")
		.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
		.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		.append("AND I.IS_TAX = 'F' ")
		.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
	    }else if("VD".equals(customerWarehouse)){
			sqlMax.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
			.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
			.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
			.append("AND T.ITEM_CODE = I.ITEM_CODE ")
			.append("AND T.BRAND_CODE = W.BRAND_CODE ")
			.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
			.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
			.append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
			.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
			.append("AND I.IS_TAX = 'F' ")
			.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		}
	    sqlMax.append(") ");
	    
	    Double count = 0D;
	    List countList = nativeQueryDAO.executeNativeSql(sqlMax.toString() );
	    if(countList.size() > 0 ){
		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
	    }
	    
	    log.info( " date = " + date + " count = " + count);
	    for (int page = 0; page < Math.ceil(count/pageSize) ; page++) {
		StringBuffer sql = new StringBuffer();
		
		if("FW".equals(customerWarehouse)){
		    sql.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO,  NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.DECLARATION_NO, I.SUPPLIER_ITEM_CODE ")
		    .append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    .append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    .append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    .append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    .append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    .append("AND T.DECLARATION_NO IS NOT NULL ")
		    .append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    .append("AND T.ORDER_TYPE_CODE IN ( 'ADF', 'MEF', 'EOF', 'EIF', 'ESF' ) ")
		    .append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    .append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		}else if("FD".equals(customerWarehouse)){
		    sql.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
			.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    .append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    .append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    .append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    .append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    //.append("AND T.BRAND_CODE = D.BRAND_CODE ")
		    //.append("AND T.ORDER_TYPE_CODE = D.ORDER_TYPE_CODE ")
		    //.append("AND T.ORDER_NO = D.ORDER_NO ")
		    //.append("AND T.TRANSATION_DATE = D.SHIP_DATE ")
		    //.append("AND T.CUSTOMER_PO_NO IS NOT NULL ")
		    .append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    .append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
		    .append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    .append("AND I.IS_TAX = 'F' ")
		    .append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		    //.append("UNION ")
		    //.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // , D.TRANSACTION_SEQ_NO
		    //.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    //.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    //.append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    //.append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    //.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    //.append("AND T.BRAND_CODE = D.BRAND_CODE ")
		    //.append("AND T.ORDER_TYPE_CODE = D.ORDER_TYPE_CODE ")
		    //.append("AND T.ORDER_NO = D.ORDER_NO ")
		    //.append("AND T.TRANSATION_DATE = D.SHIP_DATE ")
		    //.append("AND D.TRANSACTION_SEQ_NO IS NOT NULL ")
		    //.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    //.append("AND T.ORDER_TYPE_CODE IN ( 'IRP', 'IBT' ) ")
		    //.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    //.append("AND I.IS_TAX = 'F' ")
		    //.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		}else if("HD".equals(customerWarehouse)){
		    sql.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
			.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    .append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    .append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    .append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    .append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    //.append("AND T.BRAND_CODE = D.BRAND_CODE ")
		    //.append("AND T.ORDER_TYPE_CODE = D.ORDER_TYPE_CODE ")
		    //.append("AND T.ORDER_NO = D.ORDER_NO ")
		    //.append("AND T.TRANSATION_DATE = D.SHIP_DATE ")
		    //.append("AND T.CUSTOMER_PO_NO IS NOT NULL ")
		    .append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    .append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
		    .append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    .append("AND I.IS_TAX = 'F' ")
		    .append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		    //.append("UNION ")
		    //.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // , D.TRANSACTION_SEQ_NO
		    //.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    //.append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    //.append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    //.append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    //.append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    //.append("AND T.BRAND_CODE = D.BRAND_CODE ")
		    //.append("AND T.ORDER_TYPE_CODE = D.ORDER_TYPE_CODE ")
		    //.append("AND T.ORDER_NO = D.ORDER_NO ")
		    //.append("AND T.TRANSATION_DATE = D.SHIP_DATE ")
		    //.append("AND D.TRANSACTION_SEQ_NO IS NOT NULL ")
		    //.append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    //.append("AND T.ORDER_TYPE_CODE IN ( 'IRP', 'IBT' ) ")
		    //.append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    //.append("AND I.IS_TAX = 'F' ")
		    //.append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		}else if("VD".equals(customerWarehouse)){
		    sql.append("SELECT T.ORDER_TYPE_CODE || T.ORDER_NO AS ORDER_NO, NVL(T.LINE_ID, 0) AS LINE_ID, T.ITEM_CODE, NVL(T.QUANTITY, 0) AS QUANTITY, W.WAREHOUSE_CODE, T.CUSTOMER_PO_NO AS DECLARATION_NO, I.SUPPLIER_ITEM_CODE ") // T.ORDER_TYPE_CODE || T.ORDER_NO , D.TRANSACTION_SEQ_NO   
			.append("FROM IM_TRANSATION T, IM_ITEM I, IM_WAREHOUSE W ")
		    .append("WHERE T.BRAND_CODE = I.BRAND_CODE ")
		    .append("AND T.ITEM_CODE = I.ITEM_CODE ")
		    .append("AND T.BRAND_CODE = W.BRAND_CODE ")
		    .append("AND T.WAREHOUSE_CODE = W.WAREHOUSE_CODE ")
		    .append("AND T.BRAND_CODE = '").append(brandCode).append("' ")
		    .append("AND T.ORDER_TYPE_CODE IN ( 'IOP', 'IRP', 'IBT' ) ")
		    .append("AND W.CUSTOMS_WAREHOUSE_CODE = '").append(customerWarehouse).append("' ")
		    .append("AND I.IS_TAX = 'F' ")
		    .append("AND T.TRANSATION_DATE = TO_DATE( '").append(date).append("', 'YYYY-MM-DD') ");
		}
		sqlMax.append(") ");
		
		List list = nativeQueryDAO.executeNativeSql(sql.toString(),page ,pageSize );
		
		log.info(" date = " + date + " count = " + count + " page =" + page + " list.size = " +list.size());
		for (Object obj : list) {
//		    log.info("((Object[])obj)[0] = " + ((Object[])obj)[0]);
//		    log.info("((Object[])obj)[1] = " + ((Object[])obj)[1]);
//		    log.info("((Object[])obj)[2] = " + ((Object[])obj)[2]);
//		    log.info("((Object[])obj)[3] = " + ((Object[])obj)[3]);
//		    log.info("((Object[])obj)[4] = " + ((Object[])obj)[4]);
//		    log.info("((Object[])obj)[5] = " + ((Object[])obj)[5]);
//		    log.info("((Object[])obj)[6] = " + ((Object[])obj)[6]);
//		    log.info("((Object[])obj)[7] = " + ((Object[])obj)[7]);
		    
		    Object[] record = new Object[fields.length];
		    record[0] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, date); // 日期
		    record[1] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[0], ""), fields[1].getLength() );	  // 單別+單號
		    record[2] = (BigDecimal)((Object[])obj)[1];	  // 序號
		    record[3] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[2], ""), fields[3].getLength());   // 品號
		    record[4] = (BigDecimal)((Object[])obj)[3];	  // 數量	
		    record[5] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[4], ""), fields[5].getLength());	  // 庫別
		    record[6] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[5], ""), fields[6].getLength());	  // 報單單號 
		    record[7] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[6], ""), fields[7].getLength());	  // 廠商貨號
		//    record[8] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[7], ""), fields[7].getLength());	  // 交易序號 比對用

		    records.add(record);
		}

	    }
	    
	}
	log.info(" records.size = " + records.size());
	DBFUtils.writeDBF(basePath+"/"+iFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/I"+year.substring(2)+df.format(NumberUtils.getInt(month))+".dbf"
    }
    
    /**
     * 匯出 D
     * @param map
     */
    private void exportD(Map conditionMap) throws Exception, IOException, JDBFException{
    String brandCode = (String)conditionMap.get("brandCode");
	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
	String year = (String)conditionMap.get("year");
	String month = (String)conditionMap.get("month");
	if(month.length() == 1)
		month = "0"+month;
	log.info("年+月 = " + year + month);
	//更新BuBrand CM_MONTH
	BuBrand buBrand = buBrandDAO.findById(brandCode);
	buBrand.setCmMonth(year + month);
	buBrandDAO.update(buBrand);
	
	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
	String basePath = (String)conditionMap.get("BASE_PATH");
	String dFileName = (String)conditionMap.get("D_FILE_NAME");
	
	String fileName = iniProperties.getProperty("dTemp");
	String dTemp = classPath + "/" + fileName;
	
	DecimalFormat df = new DecimalFormat("00");
	
	JDBField[] fields = templateFields.get(dTemp);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(dTemp);
	    templateFields.put(dTemp, fields);
	}
	
	String startDate = year+"-"+df.format(NumberUtils.getInt(month))+"-"+"01";
	String endDate = DateUtils.format(DateUtils.getLastDateOfMonth(DateUtils.parseDate(startDate)), DateUtils.C_DATE_PATTON_DEFAULT); //year+"-"+df.format(NumberUtils.getInt(month))+"-"+ DateUtils.getLastDayOfMonth( NumberUtils.getInt(year), NumberUtils.getInt(month));
	
	List<Object[]> records = new ArrayList();
	
	List<String> deteList =  DateUtils.getDaysBetweenList(startDate, endDate);
	for (String date : deteList) {
	    
	    Double count = 0D;
	    List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM CM_MONTHLY_DETAIL_VIEW WHERE CUSTOMS_WAREHOUSE_CODE = '"+customerWarehouse+"' AND DECL_DATE = TO_DATE( '"+date+"', 'YYYY-MM-DD')" );
	    if(countList.size() > 0 ){
		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
	    }
	    
	    for (int page = 0; page < Math.ceil(count/pageSize) ; page++) {
		List list = nativeQueryDAO.executeNativeSql("SELECT * FROM CM_MONTHLY_DETAIL_VIEW WHERE CUSTOMS_WAREHOUSE_CODE = '"+customerWarehouse+"' AND DECL_DATE = TO_DATE( '"+date+"', 'YYYY-MM-DD')", page, pageSize); 
		for (Object obj : list) {
		    Object[] record = new Object[fields.length];
//		    log.info("((Object[])obj)[0] = " + ((Object[])obj)[0]);
//		    log.info("((Object[])obj)[1] = " + ((Object[])obj)[1]);
//		    log.info("((Object[])obj)[2] = " + ((Object[])obj)[2]);
//		    log.info("((Object[])obj)[3] = " + ((Object[])obj)[3]);
//		    log.info("((Object[])obj)[4] = " + ((Object[])obj)[4]);
//		    log.info("((Object[])obj)[5] = " + ((Object[])obj)[5]);
//		    log.info("((Object[])obj)[6] = " + ((Object[])obj)[6]);
//		    log.info("((Object[])obj)[7] = " + ((Object[])obj)[7]);
//		    log.info("((Object[])obj)[8] = " + ((Object[])obj)[8]);
//		    log.info("((Object[])obj)[9] = " + ((Object[])obj)[9]);
//		    log.info("((Object[])obj)[10] = " + ((Object[])obj)[10]);
//		    log.info("((Object[])obj)[11] = " + ((Object[])obj)[11]);
//		    log.info("((Object[])obj)[12] = " + ((Object[])obj)[12]);
//		    log.info("((Object[])obj)[13] = " + ((Object[])obj)[13]);
//		    log.info("((Object[])obj)[14] = " + ((Object[])obj)[14]);
//		    log.info("((Object[])obj)[15] = " + ((Object[])obj)[15]);
//		    log.info("((Object[])obj)[16] = " + ((Object[])obj)[16]);
//		    log.info("((Object[])obj)[17] = " + ((Object[])obj)[17]);
//		    log.info("((Object[])obj)[18] = " + ((Object[])obj)[18]);
//		    log.info("((Object[])obj)[19] = " + ((Object[])obj)[19]);
		    
		    
		    record[0] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[0], ""), fields[0].getLength());   	// 品號
		    record[1] = ((Object[])obj)[1];	// 日期
		    record[2] = trimString(AjaxUtils.getPropertiesValue(((Object[])obj)[2], ""), fields[2].getLength());	// 單別單號
		    record[3] = (BigDecimal)((Object[])obj)[3];   	// 序號
		    record[4] = (BigDecimal)((Object[])obj)[4];	// 進儲	
		    record[5] = (BigDecimal)((Object[])obj)[5]; 	// 進出
		    record[6] = (BigDecimal)((Object[])obj)[6];	// 移倉撥入
		    record[7] = (BigDecimal)((Object[])obj)[7];	// 移倉撥出
		    record[8] = (BigDecimal)((Object[])obj)[8];	// 銷售
		    record[9] = (BigDecimal)((Object[])obj)[9];	// 外運
		    record[10] = (BigDecimal)((Object[])obj)[10];  	// 補稅
		    record[11] = (BigDecimal)((Object[])obj)[11];  	// 報廢
		    record[12] = (BigDecimal)((Object[])obj)[12];  	// 調整
		    record[13] = (BigDecimal)((Object[])obj)[13];  	// 數量
		    record[14] = trimString(AjaxUtils.getPropertiesValue( ((Object[])obj)[14], ""), fields[14].getLength());  	// 庫別1
		    record[15] = trimString(AjaxUtils.getPropertiesValue( ((Object[])obj)[15], ""), fields[15].getLength());  	// 型態1
		    record[16] = trimString(AjaxUtils.getPropertiesValue( ((Object[])obj)[16], ""), fields[16].getLength());  	// io別1
		    record[17] = trimString(AjaxUtils.getPropertiesValue( ((Object[])obj)[17], ""), fields[17].getLength());  	// 報單1
		    record[18] = trimString(AjaxUtils.getPropertiesValue( ((Object[])obj)[18], ""), fields[18].getLength());  	// 備註1
		    record[19] = (BigDecimal)((Object[])obj)[19];  	// 移倉出庫
		    
		    records.add(record);
		}

	    }
	}
	
	DBFUtils.writeDBF(basePath+"/"+dFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/D"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 M
     * @param map
     */
    private void exportM(Map conditionMap) throws IOException, JDBFException, Exception{
    String brandCode = (String)conditionMap.get("brandCode");
	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
	String year = (String)conditionMap.get("year");
	String month = (String)conditionMap.get("month");
	if(month.length() == 1)
		month = "0"+month;
	log.info("年+月 = " + year + month);
	//更新BuBrand CM_MONTH
	BuBrand buBrand = buBrandDAO.findById(brandCode);
	buBrand.setCmMonth(year + month);
	buBrandDAO.update(buBrand);
	
	conn = dataSource.getConnection();
	CallableStatement calStmt = null;
	System.out.println("==== Call 海關光碟的SP ====");
	calStmt = conn.prepareCall("{call ERP.APP_CM_MONTH_CUSTOM_PACKAGE.insert_custom(?,?,?)}"); // 呼叫store procedure
	calStmt.setString(1, year + "01");
	calStmt.setString(2, year + month);
	calStmt.setNull(3, Types.NULL);
	calStmt.execute();
	
	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
	String basePath = (String)conditionMap.get("BASE_PATH");
	String mFileName = (String)conditionMap.get("M_FILE_NAME");
	
	String fileName = iniProperties.getProperty("mTemp");
	String mTemp = classPath + "/" + fileName;
	
	JDBField[] fields = templateFields.get(mTemp);
	if (null == fields) {
	    fields = DBFUtils.getJDBField(mTemp);
	    templateFields.put(mTemp, fields);
	}
	
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
	List<Object[]> records = new ArrayList();
	Double count = 0D;
	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM CM_M WHERE CUSTOMS_WAREHOUSE_CODE = '"+customerWarehouse+"'" ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
	if(countList.size() > 0 ){
	    count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
	}
	log.info("count = " + count);
	
	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
	    StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM CM_M where CUSTOMS_WAREHOUSE_CODE = '"+ customerWarehouse +"' )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
	    log.info(" sql = " + sql.toString());
	    final String fsql = sql.toString();

	    List rows = jdbcTemplate.queryForList(
		    fsql//,
//		    new Object[] {customerWarehouse},
//		    java.lang.String.class
	    );
	    log.info(" rows.size() = " + rows.size());
	    Iterator it = rows.iterator();
	    log.info(" 開始 ");
	    while(it.hasNext()) {
	        Map viewMap = (Map) it.next();
	        Object[] record = new Object[fields.length];
//	        log.info(" TYPE = " + viewMap.get("TYPE"));
//	        log.info(" ITEM_CODE = " + viewMap.get("ITEM_CODE"));
//	        log.info(" RECEIVE_IN_QTY = " + viewMap.get("RECEIVE_IN_QTY"));
//	        log.info(" RECEIVE_OUT_QTY = " + viewMap.get("RECEIVE_OUT_QTY"));
//	        log.info(" ARRIVAL_QTY = " + viewMap.get("ARRIVAL_QTY"));
//	        log.info(" DELIVERY_QTY = " + viewMap.get("DELIVERY_QTY"));
//	        log.info(" SHIP_QTY = " + viewMap.get("SHIP_QTY"));
//	        log.info(" SHIP_OUT_QTY = " + viewMap.get("SHIP_OUT_QTY"));
//	        log.info(" ADJ_QTYP = " + viewMap.get("ADJ_QTYP"));
//	        log.info(" ADJ_QTY2 = " + viewMap.get("ADJ_QTY2"));
//	        log.info(" ADJ_QTY = " + viewMap.get("ADJ_QTY"));
//	        log.info(" QTY = " + viewMap.get("QTY"));
//	        log.info(" BEGIN_QTY = " + viewMap.get("BEGIN_QTY"));
//	        log.info(" DELIVERY_QY = " + viewMap.get("DELIVERY_QY"));
//	        log.info(" END_QTY = " + viewMap.get("END_QTY"));
//	        log.info(" ITEM_C_NAME = " + viewMap.get("ITEM_C_NAME"));
//	        log.info(" SALES_UNIT = " + viewMap.get("SALES_UNIT"));
//	        log.info(" UNIT_PRICE = " + viewMap.get("UNIT_PRICE"));
//	        log.info(" IS_TAX = " + viewMap.get("IS_TAX"));
//	        log.info(" ITEM_BRAND = " + viewMap.get("ITEM_BRAND"));
//	        log.info(" CATEGORY02 = " + viewMap.get("CATEGORY02"));
//	        log.info(" CUSTOMS_WAREHOUSE_CODE = " + viewMap.get("CUSTOMS_WAREHOUSE_CODE"));
	        
	        record[0] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("TYPE"), ""), fields[0].getLength()); //trimString(, fields[0].getLength());   // type
			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength());	  // 品號
			record[2] = (BigDecimal)viewMap.get("PURCHASE_QTY");	  // 進儲
			record[3] = (BigDecimal)viewMap.get("PURCHASE_RETURN_QTY");   // 進出
			record[4] = (BigDecimal)viewMap.get("MOVEMENT_IN_QTY");	  // 移倉撥入	
			record[5] = (BigDecimal)viewMap.get("MOVEMENT_OUT_QTY");	  // 移倉撥出
			record[6] = (BigDecimal)viewMap.get("POS_QTY");	  // 銷售
			record[7] = (BigDecimal)viewMap.get("SALES_QTY");	  // 外運
			record[8] = (BigDecimal)viewMap.get("PAY_TAX_QTY");	  // 補稅
			record[9] = (BigDecimal)viewMap.get("DISCARD_QTY");	  // 報廢
			record[10] = (BigDecimal)viewMap.get("ADJ_QTY");  // 調整
			record[11] = (BigDecimal)viewMap.get("QTY");  // 數量
			record[12] = (BigDecimal)viewMap.get("BEGIN_QTY");  // 期初
			record[13] = (BigDecimal)viewMap.get("DELIVERY_QY");  // 移倉出庫
			record[14] = (BigDecimal)viewMap.get("END_QTY");  // 期末
			record[15] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[15].getLength());  // 品名
			record[16] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("SALES_UNIT"), ""), fields[16].getLength());  // 單位
			record[17] = (BigDecimal)viewMap.get("UNIT_PRICE");  // 零售價
			record[18] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("IS_TAX"), ""), fields[18].getLength());   // 稅別
			record[19] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_BRAND"), ""), fields[19].getLength());   // 品牌
			record[20] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("CATEGORY02"), ""), fields[20].getLength());   // 類別
			records.add(record);
	    }
	    log.info(" 結束 ");
	}
	
	DBFUtils.writeDBF(basePath+"/"+mFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/M"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 DS D7 剩下6個月的屆期品
     * @param map
     */
    private void exportDS(Map conditionMap) throws IOException, JDBFException, Exception{
//  	String brandCode = (String)conditionMap.get("brandCode");
//  	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
//  	String year = (String)conditionMap.get("year");
//  	String month = (String)conditionMap.get("month");
//  	if(month.length() == 1)
//  	month = "0"+month;
//  	log.info("年+月 = " + year + month);
//  	//更新BuBrand CM_MONTH
//  	BuBrand buBrand = buBrandDAO.findById(brandCode);
//  	buBrand.setCmMonth(year + month);
//  	buBrandDAO.update(buBrand);

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String deFileName = (String)conditionMap.get("DS_FILE_NAME");

    	String fileName = iniProperties.getProperty("dsTemp");
    	String dsTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(dsTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(dsTemp);
    		templateFields.put(dsTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_DS " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	//log.info("count = " + count);

    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_DS )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		//log.info(" sql = " + sql.toString());
    		final String fsql = sql.toString();

    		List rows = jdbcTemplate.queryForList(
    				fsql
    		);
    		//log.info(" rows.size() = " + rows.size());
    		Iterator it = rows.iterator();
    		//log.info(" 開始 ");
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
//  			log.info(" ITEM_CODE = " + viewMap.get("ITEM_CODE"));
//  			log.info(" ITEM_C_NAME = " + viewMap.get("ITEM_C_NAME"));
//  			log.info(" DECLARATION_NO = " + viewMap.get("DECLARATION_NO"));
//  			log.info(" O_DECL_NO = " + viewMap.get("O_DECL_NO"));
//  			log.info(" IMPORT_DATE = " + viewMap.get("IMPORT_DATE"));
//  			log.info(" EXPIRE_DATE = " + viewMap.get("EXPIRE_DATE"));
//  			log.info(" FA_SUM_QTY = " + viewMap.get("FA_SUM_QTY"));
//  			log.info(" FD_SUM_QTY = " + viewMap.get("FD_SUM_QTY"));
//  			log.info(" FW_SUM_QTY = " + viewMap.get("FW_SUM_QTY"));
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // D7報單
    			record[4] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("O_DECL_NO"), ""), fields[4].getLength());   // 原D8報單
    			record[5] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 --DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,	)
    			record[6] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[7] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[8] = (BigDecimal)viewMap.get("FD_SUM_QTY");	  // 免稅店庫存
    			record[9] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    		//log.info(" 結束 ");
    	}
    	DBFUtils.writeDBF(basePath+"/"+deFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DE"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 HS D7 剩下6個月的屆期品
     * @param map
     */
    private void exportHS(Map conditionMap) throws IOException, JDBFException, Exception{
//  	String brandCode = (String)conditionMap.get("brandCode");
//  	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
//  	String year = (String)conditionMap.get("year");
//  	String month = (String)conditionMap.get("month");
//  	if(month.length() == 1)
//  	month = "0"+month;
//  	log.info("年+月 = " + year + month);
//  	//更新BuBrand CM_MONTH
//  	BuBrand buBrand = buBrandDAO.findById(brandCode);
//  	buBrand.setCmMonth(year + month);
//  	buBrandDAO.update(buBrand);

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String deFileName = (String)conditionMap.get("DS_FILE_NAME");

    	String fileName = iniProperties.getProperty("dsTemp");
    	String dsTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(dsTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(dsTemp);
    		templateFields.put(dsTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_DS " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	//log.info("count = " + count);

    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_DS )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		//log.info(" sql = " + sql.toString());
    		final String fsql = sql.toString();

    		List rows = jdbcTemplate.queryForList(
    				fsql
    		);
    		//log.info(" rows.size() = " + rows.size());
    		Iterator it = rows.iterator();
    		//log.info(" 開始 ");
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
//  			log.info(" ITEM_CODE = " + viewMap.get("ITEM_CODE"));
//  			log.info(" ITEM_C_NAME = " + viewMap.get("ITEM_C_NAME"));
//  			log.info(" DECLARATION_NO = " + viewMap.get("DECLARATION_NO"));
//  			log.info(" O_DECL_NO = " + viewMap.get("O_DECL_NO"));
//  			log.info(" IMPORT_DATE = " + viewMap.get("IMPORT_DATE"));
//  			log.info(" EXPIRE_DATE = " + viewMap.get("EXPIRE_DATE"));
//  			log.info(" FA_SUM_QTY = " + viewMap.get("FA_SUM_QTY"));
//  			log.info(" FD_SUM_QTY = " + viewMap.get("FD_SUM_QTY"));
//  			log.info(" FW_SUM_QTY = " + viewMap.get("FW_SUM_QTY"));
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // D7報單
    			record[4] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("O_DECL_NO"), ""), fields[4].getLength());   // 原D8報單
    			record[5] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 --DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,	)
    			record[6] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[7] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[8] = (BigDecimal)viewMap.get("HD_SUM_QTY");	  // 高雄免稅店庫存
    			record[9] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    		//log.info(" 結束 ");
    	}
    	DBFUtils.writeDBF(basePath+"/"+deFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DE"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 MS D7 剩下6個月的屆期品
     * @param map
     */
    private void exportMS(Map conditionMap) throws IOException, JDBFException, Exception{

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String deFileName = (String)conditionMap.get("DS_FILE_NAME");

    	String fileName = iniProperties.getProperty("dsTemp");
    	String dsTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(dsTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(dsTemp);
    		templateFields.put(dsTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_MS " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	
    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_MS )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		final String fsql = sql.toString();
    		List rows = jdbcTemplate.queryForList(fsql);
    		Iterator it = rows.iterator();
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // D7報單
    			record[4] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("O_DECL_NO"), ""), fields[4].getLength());   // 原D8報單
    			record[5] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 --DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,	)
    			record[6] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[7] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[8] = (BigDecimal)viewMap.get("VD_SUM_QTY");	  // 高雄免稅店庫存
    			record[9] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    	}
    	DBFUtils.writeDBF(basePath+"/"+deFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DE"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 DE D8 剩下6個月的屆期品
     * @param map
     */
    private void exportDE(Map conditionMap) throws IOException, JDBFException, Exception{
//  	String brandCode = (String)conditionMap.get("brandCode");
//  	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
//  	String year = (String)conditionMap.get("year");
//  	String month = (String)conditionMap.get("month");
//  	if(month.length() == 1)
//  	month = "0"+month;
//  	log.info("年+月 = " + year + month);
//  	//更新BuBrand CM_MONTH
//  	BuBrand buBrand = buBrandDAO.findById(brandCode);
//  	buBrand.setCmMonth(year + month);
//  	buBrandDAO.update(buBrand);

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String dsFileName = (String)conditionMap.get("DE_FILE_NAME");

    	String fileName = iniProperties.getProperty("deTemp");
    	String deTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(deTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(deTemp);
    		templateFields.put(deTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_DE " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	//log.info("count = " + count);

    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_DE )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		//log.info(" sql = " + sql.toString());
    		final String fsql = sql.toString();

    		List rows = jdbcTemplate.queryForList(
    				fsql
    		);
    		//log.info(" rows.size() = " + rows.size());
    		Iterator it = rows.iterator();
    		//log.info(" 開始 ");
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
//  			log.info(" ITEM_CODE = " + viewMap.get("ITEM_CODE"));
//  			log.info(" ITEM_C_NAME = " + viewMap.get("ITEM_C_NAME"));
//  			log.info(" DECLARATION_NO = " + viewMap.get("DECLARATION_NO"));
//  			log.info(" IMPORT_DATE = " + viewMap.get("IMPORT_DATE"));
//  			log.info(" EXPIRE_DATE = " + viewMap.get("EXPIRE_DATE"));
//  			log.info(" FA_SUM_QTY = " + viewMap.get("FA_SUM_QTY"));
//  			log.info(" FD_SUM_QTY = " + viewMap.get("FD_SUM_QTY"));
//  			log.info(" FW_SUM_QTY = " + viewMap.get("FW_SUM_QTY"));
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // 原D8報單
    			record[4] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,)) 	 
    			record[5] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[6] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[7] = (BigDecimal)viewMap.get("FD_SUM_QTY");	  // 免稅店庫存
    			record[8] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    		//log.info(" 結束 ");
    	}
    	DBFUtils.writeDBF(basePath+"/"+dsFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DS"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 HE D8 剩下6個月的屆期品
     * @param map
     */
    private void exportHE(Map conditionMap) throws IOException, JDBFException, Exception{
//  	String brandCode = (String)conditionMap.get("brandCode");
//  	String customerWarehouse = (String)conditionMap.get("customerWarehouse");
//  	String year = (String)conditionMap.get("year");
//  	String month = (String)conditionMap.get("month");
//  	if(month.length() == 1)
//  	month = "0"+month;
//  	log.info("年+月 = " + year + month);
//  	//更新BuBrand CM_MONTH
//  	BuBrand buBrand = buBrandDAO.findById(brandCode);
//  	buBrand.setCmMonth(year + month);
//  	buBrandDAO.update(buBrand);

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String dsFileName = (String)conditionMap.get("DE_FILE_NAME");

    	String fileName = iniProperties.getProperty("deTemp");
    	String deTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(deTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(deTemp);
    		templateFields.put(deTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_HE " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	//log.info("count = " + count);

    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_HE )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		//log.info(" sql = " + sql.toString());
    		final String fsql = sql.toString();

    		List rows = jdbcTemplate.queryForList(
    				fsql
    		);
    		//log.info(" rows.size() = " + rows.size());
    		Iterator it = rows.iterator();
    		//log.info(" 開始 ");
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
//  			log.info(" ITEM_CODE = " + viewMap.get("ITEM_CODE"));
//  			log.info(" ITEM_C_NAME = " + viewMap.get("ITEM_C_NAME"));
//  			log.info(" DECLARATION_NO = " + viewMap.get("DECLARATION_NO"));
//  			log.info(" IMPORT_DATE = " + viewMap.get("IMPORT_DATE"));
//  			log.info(" EXPIRE_DATE = " + viewMap.get("EXPIRE_DATE"));
//  			log.info(" FA_SUM_QTY = " + viewMap.get("FA_SUM_QTY"));
//  			log.info(" FD_SUM_QTY = " + viewMap.get("FD_SUM_QTY"));
//  			log.info(" FW_SUM_QTY = " + viewMap.get("FW_SUM_QTY"));
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // 原D8報單
    			record[4] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,)) 	 
    			record[5] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[6] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[7] = (BigDecimal)viewMap.get("HD_SUM_QTY");	  // 高雄免稅店庫存
    			record[8] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    		//log.info(" 結束 ");
    	}
    	DBFUtils.writeDBF(basePath+"/"+dsFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DS"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * 匯出 ME D8 剩下6個月的屆期品
     * @param map
     */
    private void exportME(Map conditionMap) throws IOException, JDBFException, Exception{

    	Integer pageSize = (Integer)conditionMap.get("PAGE_SIZE");
    	String basePath = (String)conditionMap.get("BASE_PATH");
    	String dsFileName = (String)conditionMap.get("DE_FILE_NAME");

    	String fileName = iniProperties.getProperty("deTemp");
    	String deTemp = classPath + "/" + fileName;

    	JDBField[] fields = templateFields.get(deTemp);
    	if (null == fields) {
    		fields = DBFUtils.getJDBField(deTemp);
    		templateFields.put(deTemp, fields);
    	}

    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    	List<Object[]> records = new ArrayList();
    	Double count = 0D;
    	List countList = nativeQueryDAO.executeNativeSql("SELECT count(ITEM_CODE) FROM ERP.CM_ME " ); //  AND YEAR = '"+year+"' AND MONTH = '"+month+"'
    	if(countList.size() > 0 ){
    		count = NumberUtils.getDouble(((Object)countList.get(0)).toString());
    	}
    	
    	for (int page = 1; page <= Math.ceil(count/pageSize) ; page++) {
    		StringBuffer sql = new StringBuffer("select * from (SELECT ROWNUM rn, T.* FROM ( SELECT * FROM ERP.CM_ME )T WHERE ROWNUM <= " + pageSize * page+ " ) where rn >= " + (pageSize * (page-1) + 1));
    		final String fsql = sql.toString();

    		List rows = jdbcTemplate.queryForList(fsql);
    		Iterator it = rows.iterator();
    		while(it.hasNext()) {
    			Map viewMap = (Map) it.next();
    			Object[] record = new Object[fields.length];
    			record[0] = (BigDecimal) viewMap.get("rn");
    			record[1] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_CODE"), ""), fields[1].getLength()); //trimString(, fields[0].getLength());   // type
    			record[2] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("ITEM_C_NAME"), ""), fields[2].getLength());	  // 品名
    			record[3] = trimString( AjaxUtils.getPropertiesValue( viewMap.get("DECLARATION_NO"), ""), fields[3].getLength());   // 原D8報單
    			record[4] = DateUtils.format((Date)viewMap.get("IMPORT_DATE")); // 原進倉日 DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT,)) 	 
    			record[5] = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, DateUtils.format((Date)viewMap.get("EXPIRE_DATE"))); // 存倉期限
    			record[6] = (BigDecimal)viewMap.get("FA_SUM_QTY");	  // 一期店庫存
    			record[7] = (BigDecimal)viewMap.get("VD_SUM_QTY");	  // 馬祖國際免稅店庫存
    			record[8] = (BigDecimal)viewMap.get("FW_SUM_QTY");	  // 保稅倉庫存
    			records.add(record);
    		}
    	}
    	DBFUtils.writeDBF(basePath+"/"+dsFileName, records, fields); // "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/FTPUpLoad/backup/DS"+year.substring(2)+df.format(NumberUtils.getInt(month))+customerWarehouse+".dbf"
    }
    
    /**
     * int 轉 Date
     * @param year 
     * @param month
     * @param date
     * @return
     */
    private Date getDate(int year, int month, int date){
	return DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, year + "/"+ month + "/" + date);
    }
    
    private String trimString(String source, int length) {
//	log.info("source = " + source);
	String re = "";
	if (null != source && source.length() > 0) {
	    re = source;
	    if (source.length() > length) {
		re = source.substring(0, length);
	    }
	}
	return re;
    }
    
    private static void setIniProperties() throws IOException {
	log.info("DbfExportData.setIniProperties");
	URL url = tw.com.tm.erp.exportdb.DbfExportData.class.getResource("DbfExportData.properties");
	iniProperties = new Properties();
	iniProperties.load(new FileInputStream(url.getFile()));
	classPath = url.getFile().substring(0, url.getFile().lastIndexOf("/"));
	System.out.println("classPath =" + classPath);
	// }
    }
}
