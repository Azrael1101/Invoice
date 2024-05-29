package tw.com.tm.erp.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.CmMovementService;
import tw.com.tm.erp.standardie.DataInfo;

public class StandardIEUtils {

	private static final Log log = LogFactory.getLog(StandardIEUtils.class);

	private static Properties config = new Properties();

	private static final String SYMBOL = "{$}";

	private static final String CONFIG_FILE = "/standard_ie.properties";

	private static final String CLASS_NAME = "_CLASS_NAME";

	private static final String SHEET_NAME = "_SHEET_NAME";

	private static final String FIELD = "_FIELD";

	private static final String FIELD_NOT_DISPLAY = "_FIELD_NOT_DISPLAY";

	private static final String COMMENT = "_COMMENT";

	private static final String DELIMITER = "_DELIMITER";

	private static final String ALIGN = "_ALIGN";

	private static final String FIELD_LENGTH = "_FIELD_LENGTH";

	private static final String FILL_ELEMENT = "_FILL_ELEMENT";

	private static final String END_SYMBOL = "_END_SYMBOL";

	private static final String DATA_INFO = "_DATA_INFO";

	private static final String EXTRA_CLASS = "_EXTRA_CLASS";

	private static final String EXTRA_METHOD = "_EXTRA_METHOD";

	private static final String PATTERM = "_PATTERM"; // 儲存格格式

	static {
		loadConfig();
	}

	public static void loadConfig() {
		try {
			config.load(StandardIEUtils.class.getResourceAsStream(CONFIG_FILE));
		} catch (IOException ex) {
			log.error("無法讀取標準匯出匯入設定檔！");
			throw new Error("無法讀取標準匯出匯入設定檔！");
		}
	}

	public static Properties getConfig() {
		return config;
	}

	public static void executeExport(OutputStream os, String beanName, String fileType, List entityBeans, DataInfo info,String function) throws Exception {
		try {
			if ("XLS".equalsIgnoreCase(fileType)) {
				HashMap essentialInfoMap = getXlsExportInfo(beanName, fileType, entityBeans, info);
				
				//新功能XLSS匯出時不帶comment
				if("IM_PICK_SQL".equals(beanName)) 
				    essentialInfoMap.put("noComment", "Y");
				//新功能XLSS匯出時使用整頁模式
				if("onePage".equals(function))
					essentialInfoMap.put("function", function);
				ExcelBeanUtil.exportExcel(os, essentialInfoMap);

			} else if ("TXT".equalsIgnoreCase(fileType)) {
				HashMap essentialInfoMap = getTxtExportInfo(beanName, fileType, entityBeans, info);
				TxtUtil.exportTxt(os, essentialInfoMap);
			}
		} catch (Exception ex) {
			log.error("匯出檔案時發生錯誤！原因：" + ex.toString());
			throw new Exception("匯出檔案時發生錯誤！原因：" + ex.getMessage());
		}
	}
	
	public static void executeTxtExport(OutputStream os,Long headId,String brandCode,String status) throws Exception {
		try {
				ApplicationContext context = SpringUtils.getApplicationContext();
				CmMovementService cmMovementService = (CmMovementService) context.getBean("cmMovementService");
				HashMap essentialInfoMap = cmMovementService.executeTxtExport(headId,brandCode,status);
				TxtUtil.exportTxt(os, essentialInfoMap);
		} catch (Exception ex) {
			log.error("匯出檔案時發生錯誤！原因：" + ex.toString());
			throw new Exception("匯出檔案時發生錯誤！原因：" + ex.getMessage());
		}
	}

	public static List executeImport(String importFileType, String fileName, String beanName, byte[] buf) throws Exception {
		try {
			String fileType = validateImportInfo(importFileType, fileName, beanName, buf);
			List entityBeans = new ArrayList(0);
			if ("XLS".equalsIgnoreCase(fileType)) {
				HashMap essentialInfoMap = getXLSImportInfo(beanName, fileType);
				entityBeans = ExcelBeanUtil.importExcel(essentialInfoMap, buf);
			} else if ("TXT".equalsIgnoreCase(fileType)) {
				// TODO:實作匯入明細至UI
			}

			return entityBeans;
		} catch (FormException fe) {
			log.error("匯入檔案時發生錯誤！原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("匯入檔案時發生錯誤！原因：" + ex.toString());
			throw new Exception("匯入檔案時發生錯誤！原因：" + ex.getMessage());
		}
	}

	public static HashMap getXlsExportInfo(String beanName, String fileType, List entityBeans, DataInfo info)
			throws Exception {

		try {
			// 取得匯出所需資訊
			String[] fieldArray = getFieldInfo(beanName, fileType);
			String[] fieldNotDisplayArray = getFieldNotDisplayInfo(beanName, fileType);
			HashMap essentialInfoMap = getXlsEssentialInfo(beanName, fileType);
			String[] commentArray = (String[]) essentialInfoMap.get("comment");
			String[] alignArray = (String[]) essentialInfoMap.get("align");
			
			if (fieldArray.length != commentArray.length) {
				throw new ValidationErrorException("設定檔中欄位參數與欄位註釋參數數量不相等！");
			} else if (fieldNotDisplayArray != null && fieldNotDisplayArray.length > 0) {
				Object[] objArray = getActualFieldAndCommentInfo(fieldNotDisplayArray, new Object[] { fieldArray,
						commentArray });
				fieldArray = (String[]) objArray[0];
				commentArray = (String[]) objArray[1];
			}
			essentialInfoMap.put("field", fieldArray);
			essentialInfoMap.put("comment", commentArray);
			if (alignArray != null && alignArray.length > 0)
				essentialInfoMap.put("align", alignArray);

			// 若無實作的DataInfo，則使用標準匯出
			List assembly = null;
			if (info == null) {
				assembly = executeStandardParsing(fieldArray, entityBeans);
			} else {
				assembly = info.generateOutputData(essentialInfoMap);
			}

			essentialInfoMap.put("assembly", assembly);
			return essentialInfoMap;
		} catch (Exception ex) {
			log.error("擷取XLS匯出資訊時發生錯誤，原因：" + ex.toString());
			throw ex;
		}
	}

	/**
	 * 將UI匯出的EntityBean轉換成實際輸出資料
	 * 
	 * @param fieldArray
	 * @param entityBeans
	 * @return List
	 * @throws Exception
	 */
	private static List executeStandardParsing(String[] fieldArray, List entityBeans) throws Exception {

		List assembly = null;
		if (fieldArray != null && fieldArray.length > 0 && entityBeans != null && entityBeans.size() > 0) {
			assembly = new ArrayList(0);
			for (int i = 0; i < entityBeans.size(); i++) {
				Object obj = entityBeans.get(i);
				List rowData = new ArrayList(0);
				for (int j = 0; j < fieldArray.length; j++) {
					Object vo = PropertyUtils.getProperty(obj, fieldArray[j]);
					String actualValue = null;
					if (vo != null) {
						if (vo instanceof Date) {
							actualValue = DateUtils.format((Date) vo);
						} else {
							actualValue = vo.toString();
						}
					}
					rowData.add(actualValue);
				}
				assembly.add(rowData);
			}
		}

		return assembly;
	}

	public static HashMap getXlsEssentialInfo(String beanName, String fileType) throws Exception {

		// 取得欄位註釋資訊
		String comment = config.getProperty(beanName + COMMENT);
		String[] commentArray = null;
		if (comment == null) {
			throw new ValidationErrorException(beanName + "的欄位註釋設定資訊為空值！");
		} else {
			commentArray = StringTools.StringToken(comment, SYMBOL);
			if (commentArray == null) {
				throw new ValidationErrorException("無法拆解" + beanName + "的欄位註釋設定資訊！");
			}
		}
		// 取得sheetName
		String sheetName = config.getProperty(beanName + SHEET_NAME);
		if (sheetName == null) {
			sheetName = beanName;
		}

		// 取得格式化資訊
		String align = config.getProperty(beanName + ALIGN);
		String[] alignArray = null;
		if (align != null) {
			alignArray = StringTools.StringToken(align, SYMBOL);
		}

		// 取得儲存格格式資訊
		String patterm = config.getProperty(beanName + PATTERM);
		String[] pattermArray = null;
		if (patterm != null) {
			pattermArray = StringTools.StringToken(patterm, SYMBOL);
		}

		HashMap map = new HashMap();
		map.put("fileType", fileType);
		map.put("comment", commentArray);
		map.put("sheetName", sheetName);
		map.put("align", alignArray);
		map.put("patterm", pattermArray);

		return map;
	}

	public static String[] getFieldInfo(String beanName, String fileType) throws Exception {

		// 取得field
		String field = config.getProperty(beanName + FIELD);
		String[] fieldArray = null;
		if (field == null) {
			throw new ValidationErrorException(beanName + "的欄位設定資訊為空值！");
		} else {
			fieldArray = StringTools.StringToken(field, SYMBOL);
			if (fieldArray == null) {
				throw new ValidationErrorException("無法拆解" + beanName + "的欄位設定資訊！");
			}
		}

		return fieldArray;
	}

	public static String[] getClassNameInfo(String beanName, String fileType) throws Exception {

		// 取得className
		String className = config.getProperty(beanName + CLASS_NAME);
		String[] classNameArray = null;
		if (className == null) {
			throw new ValidationErrorException(beanName + "的類別名稱設定資訊為空值！");
		} else {
			classNameArray = StringTools.StringToken(className, SYMBOL);
			if (classNameArray == null) {
				throw new ValidationErrorException("無法拆解" + beanName + "的類別名稱設定資訊！");
			}
		}

		return classNameArray;
	}

	public static String[] getFieldNotDisplayInfo(String beanName, String fileType) throws Exception {

		// 取得不顯示的欄位資訊
		String fieldNotDisplay = config.getProperty(beanName + FIELD_NOT_DISPLAY);
		String[] fieldNotDisplayArray = null;
		if (fieldNotDisplay != null) {
			fieldNotDisplayArray = StringTools.StringToken(fieldNotDisplay, SYMBOL);
		}

		return fieldNotDisplayArray;
	}

	public static String[] getExtraInfo(String beanName, String fileType) throws Exception {

		// 取得不顯示的欄位資訊
		String extraClass = config.getProperty(beanName + EXTRA_CLASS);
		String extraMethod = config.getProperty(beanName + EXTRA_METHOD);
		String[] extraInfoArray = new String[2];
		extraInfoArray[0] = extraClass;
		extraInfoArray[1] = extraMethod;
		return extraInfoArray;
	}

	/**
	 * 取得實際欄位及欄位註解
	 * 
	 * @param excludeFieldArray
	 * @param origInfoArrays
	 * @return Object[]
	 */
	public static Object[] getActualFieldAndCommentInfo(String[] excludeFieldArray, Object[] origInfoArrays) {

		String[] origFieldArray = (String[]) origInfoArrays[0];
		String[] origCommentArray = (String[]) origInfoArrays[1];
		for (int i = 0; i < origFieldArray.length; i++) {
			for (int j = 0; j < excludeFieldArray.length; j++) {
				if (origFieldArray[i].equals(excludeFieldArray[j])) {
					origFieldArray[i] = null;
					origCommentArray[i] = null;
					break;
				}
			}
		}
		List actualFields = new ArrayList();
		List actualComments = new ArrayList();
		for (int index = 0; index < origFieldArray.length; index++) {
			if (origFieldArray[index] != null) {
				actualFields.add(origFieldArray[index]);
				actualComments.add(origCommentArray[index]);
			}
		}

		return new Object[] { (String[]) actualFields.toArray(new String[actualFields.size()]),
				(String[]) actualComments.toArray(new String[actualComments.size()]) };
	}

	private static String validateImportInfo(String importFileType, String fileName, String beanName, byte[] buf)
			throws ValidationErrorException, Exception {

		if (!StringUtils.hasText(fileName)) {
			throw new ValidationErrorException("請選擇欲上傳的檔案！");
		} else if (!StringUtils.hasText(importFileType)) {
			throw new ValidationErrorException("無法取得匯入的檔案類型參數！");
		} else if (!"XLS".equalsIgnoreCase(importFileType) && !"TXT".equalsIgnoreCase(importFileType)
				&& !"ALL".equalsIgnoreCase(importFileType)) {
			throw new ValidationErrorException("匯入的檔案類型參數設定錯誤！");
		}

		int index = fileName.trim().lastIndexOf(".");
		String fileType = "";
		if (index >= 0) {
			fileType = fileName.trim().substring(index + 1);
		}

		if ("XLS".equalsIgnoreCase(importFileType) && !"XLS".equalsIgnoreCase(fileType)) {
			throw new ValidationErrorException("匯入的檔案類型錯誤，請匯入XLS檔案！");
		} else if ("TXT".equalsIgnoreCase(importFileType) && !"TXT".equalsIgnoreCase(fileType)) {
			throw new ValidationErrorException("匯入的檔案類型錯誤，請匯入TXT檔案！");
		} else if ("All".equalsIgnoreCase(importFileType) && !"XLS".equalsIgnoreCase(fileType)
				&& !"TXT".equalsIgnoreCase(fileType)) {
			throw new ValidationErrorException("匯入的檔案類型錯誤，請匯入XLS或TXT檔案！");
		} else if (!StringUtils.hasText(beanName)) {
			throw new ValidationErrorException("無法取得匯入的實體名稱參數！");
		} else if (buf.length == 0) {
			throw new ValidationErrorException("請勿匯入空的檔案！");
		}

		return fileType;
	}

	public static HashMap getXLSImportInfo(String beanName, String fileType) throws Exception {

		try {
			// 取得匯出所需資訊
			String[] classNameArray = getClassNameInfo(beanName, fileType);
			String[] fieldArray = getFieldInfo(beanName, fileType);
			String[] fieldNotDisplayArray = getFieldNotDisplayInfo(beanName, fileType);
			String[] extraInfoArray = getExtraInfo(beanName, fileType);
			HashMap essentialInfoMap = getXlsEssentialInfo(beanName, fileType);
			String[] commentArray = (String[]) essentialInfoMap.get("comment");

			if (fieldArray.length != commentArray.length) {
				throw new ValidationErrorException("設定檔中欄位參數與欄位註釋參數數量不相等！");
			} else if (fieldNotDisplayArray != null && fieldNotDisplayArray.length > 0) {
				Object[] objArray = getActualFieldAndCommentInfo(fieldNotDisplayArray, new Object[] { fieldArray,
						commentArray });
				fieldArray = (String[]) objArray[0];
				commentArray = (String[]) objArray[1];
			}

			essentialInfoMap.put("className", classNameArray);
			essentialInfoMap.put("field", fieldArray);
			essentialInfoMap.put("fieldNotDisplay", fieldNotDisplayArray);
			essentialInfoMap.put("extraInfoArray", extraInfoArray);
			essentialInfoMap.put("comment", commentArray);
			return essentialInfoMap;
		} catch (Exception ex) {
			log.error("擷取XLS匯入資訊時發生錯誤，原因：" + ex.toString());
			throw ex;
		}
	}

	public static HashMap getTxtExportInfo(String beanName, String fileType, List entityBeans, DataInfo info)
			throws Exception {
		
		try {
			// 取得匯出所需資訊
			String[] fieldNotDisplayArray = getFieldNotDisplayInfo(beanName, fileType);
			HashMap essentialInfoMap = getTxtEssentialInfo(beanName, fileType);
			essentialInfoMap.put("beanName", beanName);
			essentialInfoMap.put("fieldNotDisplay", fieldNotDisplayArray);

			// 若無實作的DataInfo，則使用標準匯出
			List assembly = null;
			if (info == null) {
				assembly = executeTxtStandardParsing(essentialInfoMap, entityBeans);
			} else {
				assembly = info.generateOutputData(essentialInfoMap);
			}
			
			essentialInfoMap.put("assembly", assembly);
			return essentialInfoMap;
		} catch (Exception ex) {
			log.error("擷取TXT匯出資訊時發生錯誤，原因：" + ex.toString());
			throw ex;
		}
	}

	public static HashMap getTxtEssentialInfo(String beanName, String fileType) throws Exception {

		// 取得field
		String field = config.getProperty(beanName + FIELD);
		String[] fieldArray = null;
		if (field != null) {
			fieldArray = StringTools.StringToken(field, SYMBOL);
		}

		// 取得欄位註釋資訊
		String comment = config.getProperty(beanName + COMMENT);
		String[] commentArray = null;
		if (comment != null) {
			commentArray = StringTools.StringToken(comment, SYMBOL);
		}

		// 取得分隔符號資訊
		String delimiter = config.getProperty(beanName + DELIMITER);

		// 取得格式化資訊
		String align = config.getProperty(beanName + ALIGN);
		String[] alignArray = null;
		if (align != null) {
			alignArray = StringTools.StringToken(align, SYMBOL);
		}

		// 取得欄位長度資訊
		String fieldLength = config.getProperty(beanName + FIELD_LENGTH);
		String[] fieldLengthArray = null;
		if (fieldLength != null) {
			fieldLengthArray = StringTools.StringToken(fieldLength, SYMBOL);
		}

		// 取得填補元素資訊
		String fillElement = config.getProperty(beanName + FILL_ELEMENT);
		String[] fillElementArray = null;
		if (fillElement != null) {
			fillElementArray = StringTools.StringToken(fillElement, SYMBOL);
		}

		// 取得是否加入結束符號資訊
		String endSymbol = config.getProperty(beanName + END_SYMBOL);

		HashMap map = new HashMap();
		map.put("fileType", fileType);
		map.put("field", fieldArray);
		map.put("comment", commentArray);
		map.put("delimiter", delimiter);
		map.put("align", alignArray);
		map.put("fieldLength", fieldLengthArray);
		map.put("fillElement", fillElementArray);
		map.put("endSymbol", endSymbol);

		return map;
	}

	/**
	 * 將UI匯出的EntityBean轉換成實際輸出資料(TXT)
	 * 
	 * @param essentialInfoMap
	 * @param entityBeans
	 * @return List
	 * @throws Exception
	 */
	private static List executeTxtStandardParsing(HashMap essentialInfoMap, List entityBeans) throws Exception {

		String beanName = (String) essentialInfoMap.get("beanName");
		String[] fieldArray = (String[]) essentialInfoMap.get("field");
		String[] fieldNotDisplayArray = (String[]) essentialInfoMap.get("fieldNotDisplay");
		String delimiter = (String) essentialInfoMap.get("delimiter");
		String endSymbol = (String) essentialInfoMap.get("endSymbol");

		if (fieldArray == null) {
			throw new ValidationErrorException(beanName + "的欄位設定資訊為空值！");
		} else if (!StringUtils.hasText(delimiter)) {
			throw new ValidationErrorException(beanName + "的分隔符號設定資訊為空值！");
		}
		if (fieldNotDisplayArray != null && fieldNotDisplayArray.length > 0) {
			fieldArray = getActualField(fieldNotDisplayArray, fieldArray);
		}

		List assembly = null;
		if (fieldArray != null && fieldArray.length > 0 && entityBeans != null && entityBeans.size() > 0) {
			assembly = new ArrayList(0);
			for (int i = 0; i < entityBeans.size(); i++) {
				Object obj = entityBeans.get(i);
				StringBuffer rowData = new StringBuffer();
				for (int j = 0; j < fieldArray.length; j++) {
					Object vo = PropertyUtils.getProperty(obj, fieldArray[j]);
					StringBuffer actualValue = new StringBuffer();
					if (vo != null) {
						if (vo instanceof Date) {
							actualValue.append(DateUtils.format((Date) vo));
						} else {
							actualValue.append(vo.toString());
						}
					}
					if (j != (fieldArray.length - 1) || (endSymbol != null && "Y".equalsIgnoreCase(endSymbol))) {
						actualValue.append(delimiter);
					}
					rowData.append(actualValue.toString().trim());
				}

				assembly.add(rowData.toString());
			}
		}

		return assembly;
	}

	/**
	 * 取得實際欄位
	 * 
	 * @param excludeFieldArray
	 * @param origFieldArray
	 * @return String[]
	 */
	public static String[] getActualField(String[] excludeFieldArray, String[] origFieldArray) {

		for (int i = 0; i < origFieldArray.length; i++) {
			for (int j = 0; j < excludeFieldArray.length; j++) {
				if (origFieldArray[i].equals(excludeFieldArray[j])) {
					origFieldArray[i] = null;
					break;
				}
			}
		}
		List actualFields = new ArrayList();
		for (int index = 0; index < origFieldArray.length; index++) {
			if (origFieldArray[index] != null) {
				actualFields.add(origFieldArray[index]);
			}
		}

		return (String[]) actualFields.toArray(new String[actualFields.size()]);
	}

	/**
	 * 資料型別轉換
	 * 
	 * @param argumentsArray
	 * @param parameterTypesArray
	 * @return Object[]
	 * @throws Exception
	 */
	public static Object[] parameterTypeConvert(String[] argumentsArray, String[] parameterTypesArray) throws Exception {

		Object[] args = new Object[argumentsArray.length];
		try {
			for (int i = 0; i < argumentsArray.length; i++) {
				if ("INT".equals(parameterTypesArray[i])) {
					args[i] = Integer.valueOf(argumentsArray[i]);
				} else if ("LONG".equals(parameterTypesArray[i])) {
					args[i] = Long.valueOf(argumentsArray[i]);
				} else if ("DOUBLE".equals(parameterTypesArray[i])) {
					args[i] = Double.valueOf(argumentsArray[i]);
				} else if ("DATE".equals(parameterTypesArray[i])) {
					args[i] = DateUtils.parseDate("yyyy/MM/dd", argumentsArray[i]);
				} else {
					args[i] = argumentsArray[i];
				}
			}
		} catch (Exception ex) {
			throw new Exception("資料型態轉換發生錯誤，原因：" + ex.getMessage());
		}

		return args;
	}

	public static String[] getDataInfo(String beanName, String fileType) throws Exception {

		// 取得dataInfo
		String dataInfo = config.getProperty(beanName + DATA_INFO);
		String[] dataInfoArray = null;
		if (dataInfo == null) {
			throw new ValidationErrorException(beanName + "的資料來源設定資訊為空值！");
		} else {
			dataInfoArray = StringTools.StringToken(dataInfo, SYMBOL);
			if (dataInfoArray == null) {
				throw new ValidationErrorException("無法拆解" + beanName + "的資料來源設定資訊！");
			}
		}

		return dataInfoArray;
	}
}
