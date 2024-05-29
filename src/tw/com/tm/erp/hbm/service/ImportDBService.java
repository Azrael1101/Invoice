package tw.com.tm.erp.hbm.service;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.importdb.ImportDataAbs;
import tw.com.tm.erp.importdb.ImportInfo;
import tw.com.tm.erp.utils.FileTools;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.User;

/**
 * 檔案匯入 Service
 * @author Mac8
 *
 */
public class ImportDBService {
	// log
	private static final Log log = LogFactory.getLog(ImportDBService.class);
	// default date format
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	public static final String UPLOAD_FILE_NAME = "UPLOAD_FILE_NAME";
	public static final String UPLOAD_HEAD_FILE_NAME = "UPLOAD_HEAD_FILE_NAME";
	public static final String UPLOAD_LINE_FILE_NAME = "UPLOAD_LINE_FILE_NAME";
	public static final String UPLOAD_HEAD_FILE_PATH = "UPLOAD_HEAD_FILE_PATH";
	public static final String UPLOAD_LINE_FILE_PATH = "UPLOAD_LINE_FILE_PATH";
	public static final String COMBINE_FILE_PATH = "COMBINE_FILE_PATH";
	public static final String BASE_FILE_PATH = "BASE_FILE_PATH";
	public static final String SUCCESS_FILE_PATH = "SUCCESS_FILE_PATH";
	public static final String FAIL_FILE_PATH = "FAIL_FILE_PATH";
	public static final String UPLOAD_MACHINE_CODE = "UPLOAD_MACHINE_CODE";
	public static final String DEFAULT_KEY_HEAD = "T";
	public static final String EXCEL_CONFIG_SHEET_NAME = "CONFIG";
	public static final String PROCESS_LOG_NO = "PROCESS_LOG_NO" ;
	public static final String PRCCESS_NAME = "PRCCESS_NAME" ;

	public static final String SHOP_CODE = "SHOP_CODE" ;
	public static final String BRAND_CODE = "BRAND_CODE" ;
	public static final String BATCH_NO = "BATCH_NO";
	public static final String UPLOAD_DATE = "UPLOAD_DATE";
	public static final String COUNTS_ID = "COUNTS_ID";
	public static final int MAX_DECIMAL_POINT = 6 ;	// 最大小數點

	/**
	 * import source file to update db
	 *
	 * @param sourceFileName :
	 *            text file or excel file
	 * @param importDataClassStr :
	 *            implement ImportDataAbs interface class name
	 * @param uiProperties
	 *            畫面/排程 要帶入的參數 ()
	 * @return
	 * @throws Exception
	 */
	public String importDB(String sourceFileName, String importDataClassStr, HashMap uiProperties) throws FormException, Exception {
		log.info("ImportDBService.importDB " + sourceFileName + " " + importDataClassStr);

		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		if ((null != user) && StringUtils.hasText(sourceFileName) && StringUtils.hasText(importDataClassStr)) {
			Class importDataClass = Class.forName(importDataClassStr);
			ImportDataAbs importDataAbs = (ImportDataAbs) importDataClass.newInstance();
			// 指定上傳檔案的名稱到 HashMap 上面
			if (null == uiProperties)
				uiProperties = new HashMap();
			uiProperties.put(ImportDBService.UPLOAD_FILE_NAME, sourceFileName);

			// 呼叫實做的物件取得 ImportInfo
			ImportInfo info = importDataAbs.initial(uiProperties);

			// 指定UI帶入的參數
			if (null == info.getUiProperties())
				info.setUiProperties(uiProperties);

			List<List<String>> data = null;
			String subFileName = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
			if ("txt".equalsIgnoreCase(subFileName)) {
				data = loadTxtData(sourceFileName, info); // 讀取Text Data
			} else if ("csv".equalsIgnoreCase(subFileName)) {
				data = loadTxtData(sourceFileName, info); // 讀取CSV Data
			} else if ("xls".equalsIgnoreCase(subFileName)) {
				data = loadXlsData(sourceFileName, info); // 讀取Excel Data
			}
			//T2商品主檔匯入預設值:銷售單位/報關單位換算(65)	最小單位/銷貨單位的比例換算(66)	預算類別(67)
			if (importDataClassStr.indexOf("ImItemImportData") > -1 && "T2".equals(user.getBrandCode())) {
				String brandCode = user.getBrandCode();
				String fileBrandCode = "";
				for (int i = 0; i < data.size(); i++) {
					List<String> dataList = data.get(i);
					log.info(i+".  "+dataList.get(65)+"/"+dataList.get(66)+"/"+dataList.get(67));
					if(dataList.get(65) == null||dataList.get(65).toString().equals(""))
						dataList.set(65,"1");
					
					if(dataList.get(66) == null||dataList.get(66).toString().equals(""))
						dataList.set(66,"1");
					
					if(dataList.get(67) == null||dataList.get(67).toString().equals(""))
						dataList.set(67,"A");

				}

			}
			System.out.println(importDataClassStr + "============>" + user.getBrandCode());
			if (importDataClassStr.indexOf("ImMovementImportData") > -1 && !"T2B".equals(user.getBrandCode()) && !"T2".equals(user.getBrandCode()) && !"T2A".equals(user.getBrandCode()) ) {
				String brandCode = user.getBrandCode();
				String fileBrandCode = "";
				for (int i = 0; i < data.size(); i++) {
					List<String> dataList = data.get(i);
					for (int j = 0; j < dataList.size(); j++) {
						if (dataList.get(j).indexOf(brandCode) > -1) {
							fileBrandCode = dataList.get(j).substring(dataList.get(j).indexOf(brandCode), brandCode.length());
							break;
						}
					}
					if (!"".equals(fileBrandCode))
						break;
				}
				System.out.println("brandCode :: " + brandCode);
				System.out.println("fileBrandCode :: " + fileBrandCode);
				if (!brandCode.equals(fileBrandCode)) { // 調撥單匯入時，檢查品牌代號是否相符 Weichun 2010/10/05
					throw new Exception("匯入檔案的品牌代碼與登入的品牌代碼不符，請登出後，重新選擇品牌代碼！");
				}
			}

			System.out.println("data ============>"+ data);
			if (((null != data) && (data.size() > 0)) && (null != info)) {
				// 針對同一筆Record 有 Master Detail 資料處理
				if (null != info.getOneRecordDetailKeys() && info.getOneRecordDetailKeys().length > 0)
					data = createOneRecordMasterDetails(data, info);

				List entityBean = createEntityBean(data, info); // 載入Entity Bean
				return importDataAbs.updateDB(entityBean, info);// 呼叫實做的物件去更新資料庫
			} else {
				if ((null == data) || (data.size() == 0)) {
					throw new Exception("匯入資料為空,請再確認您的資料無誤");
				} else if (null == info) {
					throw new Exception("匯入檔案規格為空,請再確認規格內容無誤");
				}
			}
		} else {
			if (null == user) {
				throw new Exception("使用者資料為空白");
			} else if (!StringUtils.hasText(sourceFileName)) {
				throw new Exception("匯入檔案名稱為空白");
			} else if (!StringUtils.hasText(importDataClassStr)) {
				throw new Exception("匯入轉檔物件名稱為空白");
			}
		}
		return "匯入資料有問題,請再確認您的資料無誤";
	}

	/**
	 * 匯入資料
	 * @param data
	 * @param importDataClassStr
	 * @param uiProperties
	 * @return
	 * @throws Exception
	 */
	public String importDB(List<List<String>> data, String importDataClassStr, HashMap uiProperties) throws Exception {
		log.info("ImportDBService.importDB " + importDataClassStr);
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		if ((null != user) && StringUtils.hasText(importDataClassStr)) {
			Class importDataClass = Class.forName(importDataClassStr);
			ImportDataAbs importDataAbs = (ImportDataAbs) importDataClass.newInstance();
			// 指定上傳檔案的名稱到 HashMap 上面
			if (null == uiProperties)
				uiProperties = new HashMap();

			// 呼叫實做的物件取得 ImportInfo
			ImportInfo info = importDataAbs.initial(uiProperties);

			// 指定UI帶入的參數
			if (null == info.getUiProperties())
				info.setUiProperties(uiProperties);

			if (((null != data) && (data.size() > 0)) && (null != info)) {
				// 針對同一筆Record 有 Master Detail 資料處理
				if (null != info.getOneRecordDetailKeys() && info.getOneRecordDetailKeys().length > 0)
					data = createOneRecordMasterDetails(data, info);

				List entityBean = createEntityBean(data, info); // 載入Entity Bean
				return importDataAbs.updateDB(entityBean, info);// 呼叫實做的物件去更新資料庫
			} else {
				if ((null == data) || (data.size() == 0)) {
					throw new Exception("匯入資料為空,請再確認您的資料無誤");
				} else if (null == info) {
					throw new Exception("匯入檔案規格為空,請再確認規格內容無誤");
				}
			}
		} else {
			if (null == user) {
				throw new Exception("使用者資料為空白");
			} else if (!StringUtils.hasText(importDataClassStr)) {
				throw new Exception("匯入轉檔物件名稱為空白");
			}
		}
		return "匯入資料有問題,請再確認您的資料無誤";
	}

	/**
	 * 載入 txt file 分隔符號之間一定要有資料,最少也要保留一個空白
	 *
	 * @param fileName
	 * @param info
	 * @return
	 * @throws Exception
	 */
	private List<List<String>> loadTxtData(String fileName, ImportInfo info) throws Exception {
		log.info("ImportDBService.loadTxtData");
		String split[] = info.getSplit();
		List<List<String>> resultAllDatas = new ArrayList();
		File importFile = new File(fileName);
		if (importFile.exists()) {
			ArrayList<String> dataSource = FileTools.ReadFromFileToArrayList(importFile, "utf-8");
			for (String record : dataSource) {
				if (StringUtils.hasText(record)) {
					// Vector<String> fieldData = null;
					String[] fieldData = null;
					List<String> tempRecord = new ArrayList();
					if (split.length == 1) {
						record = record + " ";
						fieldData = StringTools.StringToken(record, split[0]);
					} else {
					}
					if (null != fieldData) {
						for (String data : fieldData) {
							tempRecord.add(data);
						}
					}
					resultAllDatas.add(tempRecord);
				}
			}
		} else {
			throw new Exception("匯入檔案找不到 " + fileName);
		}
		return resultAllDatas;
	}

	/**
	 * 載入 excel file not include space
	 *
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private List<List<String>> loadXlsData(String fileName, ImportInfo info) throws Exception {
		log.info("ImportDBService.loadXlsData");
		List<List<String>> resultAllDatas = new ArrayList();
		File importFile = new File(fileName);
		if (importFile.exists()) {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			setXlsImportInfo(wb, info);
			HSSFSheet sheet = wb.getSheetAt(0);
			if (null != sheet) {
				// Iterator rows = sheet.rowIterator();
				int rowNums = sheet.getPhysicalNumberOfRows();
				for (int rownum = info.getImportDataStartRecord(); rownum < rowNums; rownum++) {
					log.info("ImportDBService.loadXlsData rownum=" + rownum);
					HSSFRow row = (HSSFRow) sheet.getRow(rownum);
					if (null != row) {
						int cellNums = row.getLastCellNum();
						if (info.getXlsColumnLength() > 0)
							cellNums = info.getXlsColumnLength();
						List<String> tempRecord = new ArrayList();
						for (short cellnum = 0; cellnum < cellNums; cellnum++) {
							HSSFCell cell = row.getCell(cellnum);
							if (null != cell) {
								//如果是通用格式，或是文字格式
								if(0 == cell.getCellStyle().getDataFormat() || 49 == cell.getCellStyle().getDataFormat() || HSSFDateUtil.isCellDateFormatted(cell)){
									switch (cell.getCellType()) {
									case HSSFCell.CELL_TYPE_NUMERIC:
										if(HSSFDateUtil.isCellDateFormatted(cell)){
											DateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
											String tmp = ft.format(cell.getDateCellValue());
											tempRecord.add(tmp);
											break;
										}
										// 如果小數點後面是0,就會忽略小數點
										String tmp = String.valueOf(OperationUtils.roundToStr(cell.getNumericCellValue(),MAX_DECIMAL_POINT)).trim();
										if (tmp.indexOf(".") > 0) {
											String check = "0" + tmp.substring(tmp.indexOf("."));
											Double checkD = Double.valueOf(check);
											if (checkD == 0) {
												tmp = tmp.substring(0, tmp.indexOf("."));
											}
										}
										tempRecord.add(tmp);
										break;
									case HSSFCell.CELL_TYPE_STRING:
										tempRecord.add(cell.getStringCellValue().trim());
										break;
									case HSSFCell.CELL_TYPE_BLANK:
										tempRecord.add("");
										break;
									default:
										if (null == cell.getRichStringCellValue())
											tempRecord.add("");
										else
											tempRecord.add(cell.getStringCellValue().toString());
									}
								}else{
									throw new Exception("匯入的EXCELL第"+(rownum+1)+"列第"+(cellnum+1)+"欄儲存格格式錯誤!" +
											"<BR>目前匯入只支援通用/文字兩種儲存格格式");
								}
							} else {
								tempRecord.add("");
							}
							/*if (null != tempRecord && tempRecord.size() > 0) {
								int record = tempRecord.size() - 1;
								log.info("ImportDBService.loadXlsData record=" + record + " , tempRecord=" + tempRecord.get(record));
							}*/
						}
						resultAllDatas.add(tempRecord);
					}
				}
			} else {
				throw new Exception("Excel Sheet 是空白" + fileName);
			}
		} else {
			throw new Exception("匯入檔案找不到" + fileName);
		}
		return resultAllDatas;
	}

	/**
	 * 讀入XLS 依據 sheet 1 當成 Config data
	 *
	 * @param sheet
	 * @param info
	 * @throws Exception
	 */
	private void setXlsImportInfo(HSSFWorkbook wb, ImportInfo info) throws Exception {
		if (wb.getSheetIndex(EXCEL_CONFIG_SHEET_NAME) >= 0) {
			HSSFSheet configSheet = wb.getSheetAt(wb.getSheetIndex(EXCEL_CONFIG_SHEET_NAME));
			if (null != configSheet) {
				Iterator rows = configSheet.rowIterator();
				while (rows.hasNext()) {

				}
			}
		}
	}

	/**
	 * create master detail
	 * @param datas
	 * @param info
	 * @return
	 */
	private List<List<String>> createOneRecordMasterDetails(List<List<String>> datas, ImportInfo info) {
		log.info("ImportDBService.createOneRecordMasterDetails");
		List<List<String>> re = new ArrayList();
		int oneRecordDetailKeys[] = info.getOneRecordDetailKeys();
		for (List<String> oneRecord : datas) {
			String lastKey = DEFAULT_KEY_HEAD + "0";
			List<String> newRecord = new ArrayList();
			newRecord.add(lastKey);
			for (int index = 0; index < oneRecord.size(); index++) {

				String currentKey = getOneRecordMasterDetailsIndex(oneRecordDetailKeys, index);
				if (!lastKey.equalsIgnoreCase(currentKey)) {
					// 將舊的資料寫入re
					re.add(newRecord);
					// 建立新的record
					newRecord = new ArrayList();
					newRecord.add(currentKey);
				}
				newRecord.add(oneRecord.get(index));
			}
			re.add(newRecord);
		}
		return re;
	}

	/**
	 * 取得Master中Detail Index
	 * @param oneRecordDetailKeys
	 * @param index
	 * @return
	 */
	private String getOneRecordMasterDetailsIndex(int oneRecordDetailKeys[], int index) {
		log.info("ImportDBService.getOneRecordMasterDetailsIndex");
		for (int i = 0; i < oneRecordDetailKeys.length; i++) {
			if (index == oneRecordDetailKeys[i]) {
				i++;
				return DEFAULT_KEY_HEAD + i;
			}
		}
		return DEFAULT_KEY_HEAD + "0";
	}

	/**
	 * use data to create Master Entity Bean List
	 *
	 * 1. 如果有Master - Detail 一定要有 M & D Key
	 *
	 * @param data
	 * @param info
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ParseException
	 */
	private List createEntityBean(List<List<String>> data, ImportInfo info) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException,Exception {
		log.info("ImportDBService.createEntityBean");
		log.info("info.size()="+info.getFieldName().size());
		for(int x=0 ;x<info.getFieldName().size();x++ ){
			log.info("["+x+"]"+info.getFieldName().get(x));
		}
		List<String> fieldNames = info.getFieldName();
		String entityName = info.getEntityBeanClassName();
		Properties fieldTypes = info.getFieldType();
		Properties fieldTypeFormat = info.getFieldTypeFormat();
		Properties fieldNameFormat = info.getFieldNameFormat();
		HashMap<String, Object> defaultValue = info.getDefaultValue();

		String keyValue = null;
		int keyIndex = -1;
		List<ImportInfo> detailInfos = info.getDetailImportInfos();
		// check has detail
		if ((null != detailInfos) && (detailInfos.size() > 0)) {
			keyValue = info.getKeyValue();
			keyIndex = info.getKeyIndex();
		}

		List mansterEentityObjects = new ArrayList();

		// create Pojo Entity Bean Object
		if (StringUtils.hasText(entityName)) {

			Class entityClass = Class.forName(entityName);
			Object mansterEentityObject = null;

			// set Pojo Entity Bean Value
			for (int index = info.getStartRecord(); index < data.size(); index++) {
				List<String> oneRowValue = data.get(index);

				String dataKeyValue = null;
				if (null != keyValue)
					dataKeyValue = oneRowValue.get(keyIndex);

				// check detail info key value
				if ((null != keyValue) && (!keyValue.equalsIgnoreCase(dataKeyValue))) {
					for (int detailInfoIndex = 0; detailInfoIndex < detailInfos.size(); detailInfoIndex++) {
						ImportInfo detailImpInof = detailInfos.get(detailInfoIndex);
						if (dataKeyValue.equalsIgnoreCase(detailImpInof.getKeyValue())) {
							// add master entity if not have master entity
							// record
							if (null == mansterEentityObject)
								mansterEentityObject = entityClass.newInstance();
							setDetail(mansterEentityObject, detailImpInof, oneRowValue);
						}
					}
				} else { // master info
					// add last master data to maaster list
					if (null != mansterEentityObject)
						mansterEentityObjects.add(mansterEentityObject);
					mansterEentityObject = entityClass.newInstance();
					log.info("oneRowValue.size()=========================="+oneRowValue.size());
					for (int fieldIndex = 0; fieldIndex < oneRowValue.size(); fieldIndex++) {

						// 20081026 shan 給不要寫入的欄位用的

						if (checkNotImportField(info, fieldIndex)) {

							// 20080905 shan add !info.isSaveKeyField()
							if ((fieldIndex != keyIndex) || (info.isSaveKeyField())) {
								String fieldName = fieldNames.get(fieldIndex);
								log.info("fieldName="+fieldName);
								String fieldType = fieldTypes.getProperty(fieldName);
								log.info("fieldType="+fieldType);
								String fieldValue = oneRowValue.get(fieldIndex);
								log.info("fieldValue="+fieldValue);
								String fieldFormat = fieldNameFormat.getProperty(fieldName);
								log.info("fieldFormat="+fieldFormat);
								if (!StringUtils.hasText(fieldFormat)) {
									fieldFormat = fieldTypeFormat.getProperty(fieldType);
									log.info("fieldFormat="+fieldFormat);
								}
								log.info("index1="+fieldIndex);
								setEntityBeanPro(mansterEentityObject, fieldValue, fieldName, fieldType, fieldFormat, info
										.getFieldNameNotWrite());
							}
						}

					}
					log.info("1111111111");
					setEntityBeanDefaultValue(defaultValue, mansterEentityObject);
				}
			}

			if (null != mansterEentityObject)
				// add last master data to maaster list
				mansterEentityObjects.add(mansterEentityObject);
		}
		return mansterEentityObjects;
	}

	/**
	 * 新增 detail to detail list
	 *
	 * @param masterEntityObject
	 * @param detailInfo
	 * @param oneRowValue
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	private void setDetail(Object masterEntityObject, ImportInfo detailInfo, List<String> oneRowValue) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, ParseException, NoSuchMethodException, InvocationTargetException, Exception {
		log.info("ImportDBService.setDetail");
		List<String> fieldNames = detailInfo.getFieldName();
		String entityName = detailInfo.getEntityBeanClassName();
		Properties fieldTypes = detailInfo.getFieldType();
		Properties fieldTypeFormat = detailInfo.getFieldTypeFormat();
		HashMap<String, Object> defaultValue = detailInfo.getDefaultValue();
		String masterGetDetailListFunctionName = detailInfo.getMasterGetDetailListFunctionName();
		int keyIndex = detailInfo.getKeyIndex();
		Object entityObject = null;

		// create Pojo Entity Bean Object
		if (StringUtils.hasText(entityName)) {
			Class entityClass = Class.forName(entityName);
			entityObject = entityClass.newInstance();
			for (int fieldIndex = 0; fieldIndex < oneRowValue.size(); fieldIndex++) {
				if (checkNotImportField(detailInfo, fieldIndex)) {
					if ((keyIndex != fieldIndex) || (detailInfo.isSaveKeyField())) {
						String fieldValue = oneRowValue.get(fieldIndex);
						String fieldName = fieldNames.get(fieldIndex);
						String fieldType = fieldTypes.getProperty(fieldName);
						String fieldFormat = fieldTypeFormat.getProperty(fieldType);
						setEntityBeanPro(entityObject, fieldValue, fieldName, fieldType, fieldFormat, detailInfo.getFieldNameNotWrite());
					}
				}
			}

			setEntityBeanDefaultValue(defaultValue, entityObject);

			// get master detail list
			Object detailCollection = MethodUtils.invokeMethod(masterEntityObject, masterGetDetailListFunctionName, null);
			if (detailCollection instanceof List) {
				List detailList = (List) detailCollection;
				detailList.add(entityObject);
			} else if (detailCollection instanceof Set) {
				Set detailList = (Set) detailCollection;
				detailList.add(entityObject);
			}

		}
	}

	/**
	 * set entity bean properties
	 *
	 * @param entityObject
	 * @param fieldValue
	 * @param fieldName
	 * @param fieldType
	 * @param fieldFormat
	 * @throws ParseException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setEntityBeanPro(Object entityObject, String fieldValue, String fieldName, String fieldType, String fieldFormat,
			List<String> fieldNameNotWrites) throws ParseException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException, Exception {
		log.info("ImportDBService.setEntityBeanPro " + fieldName + " = " + fieldType + "," + fieldFormat + " [" + fieldValue + "]");
		Object valueOBj = null;

		if (fieldValue.length() > 1) // 修正去除欄位值空白 by Weichun 2011.01.25
			fieldValue = fieldValue.trim();

		// 20080928 shan add special field not write to db
		if (!checkFieldNameNotWrite(fieldName, fieldNameNotWrites)) {

			if (StringUtils.hasText(fieldValue)) {
				if (Date.class.getName().equals(fieldType)) {

					SimpleDateFormat dateFormat = null;
					if (!StringUtils.hasText(fieldFormat)) {
						dateFormat = simpleDateFormat;
					} else {
						dateFormat = new SimpleDateFormat(fieldFormat);
					}
					if (null != fieldValue) {
						try {
							valueOBj = dateFormat.parse(fieldValue);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else {
				    try {
					// VALUE 的 資料型態
					Class typeClass = Class.forName(fieldType);
					// 把String的 VALUE 轉成 ENTITY 需要的 Object
					valueOBj = ConstructorUtils.invokeConstructor(typeClass, fieldValue);
				    } catch (InvocationTargetException ite) {
					throw new Exception(fieldName+" 無法將 "+fieldValue + " 轉型為 " + fieldType + " , 請確認此欄位型態");
				    }
				}
			}
//			if(null == valueOBj){
//			    PropertyUtils.setProperty(entityObject, fieldName, valueOBj);
//			}else{
			    BeanUtils.setProperty(entityObject, fieldName, valueOBj);
//			}
		}
	}

	/**
	 * set Pojo Entity Bean Default Value
	 *
	 * @param defaultValue
	 * @param entityObject
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setEntityBeanDefaultValue(HashMap<String, Object> defaultValue, Object entityObject) throws IllegalAccessException,
			InvocationTargetException {
		log.info("ImportDBService.setEntityBeanDefaultValue");
		if (!defaultValue.isEmpty()) {
			Iterator keys = defaultValue.keySet().iterator();
			while (keys.hasNext()) {
				Object key = keys.next();
				Object value = defaultValue.get(key);
				BeanUtils.setProperty(entityObject, (String) key, value);
			}
		}
	}

	/**
	 * 新增一個欄位 not write to db
	 * @param fieldName
	 * @param fieldNameNotWrites
	 * @return
	 */
	private boolean checkFieldNameNotWrite(String fieldName, List<String> fieldNameNotWrites) {
		if ((null != fieldNameNotWrites) && (fieldNameNotWrites.size() > 0)) {
			for (String fieldNameNotWrite : fieldNameNotWrites) {
				if (fieldNameNotWrite.equalsIgnoreCase(fieldName))
					return true;
			}
		}
		return false;
	}

	/**
	 * 判斷哪些欄位不要寫入
	 * @param info
	 * @param fieldIndex
	 * @return
	 */
	private boolean checkNotImportField(ImportInfo info, int fieldIndex) {
		int[] notIF = info.getNotImportFieldIndex();
		if (null != notIF && notIF.length > 0) {
			for (int i = 0; i < notIF.length; i++) {
				if (fieldIndex == notIF[i]) {
					return false;
				}
			}
		}
		return true;
	}

}
