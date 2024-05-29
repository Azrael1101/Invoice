package tw.com.tm.erp.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.TmpAjaxSearchData;
import tw.com.tm.erp.hbm.service.TmpAjaxSearchDataService;

/**
 * AJAX Util
 * @author Mac8
 *
 */
public class AjaxUtils {
	private static final Log log = LogFactory.getLog(AjaxUtils.class);
	public static final String FORM_ID = "formId";
	public static final String HEAD_ID = "headId";
	public static final String STATUS = "status";
	public static final String PROCESS_IS_END = "processIsEnd";
	public static final String PROCESS_ID = "processId";
	public static final String PACKAGE_ID = "packageId";
	public static final String VERSION = "version";
	public static final String SOURCE_REFERENCE_TYPE = "sourceReferenceType";
	
	public static final String CLASS_NAME = "className";
	
	public static final String AJAX_FORM = "form";
	public static final String AJAX_MULTILIST = "multiList";
	
	public static final String IDENTIFICATION = "identification";
	
	public static final String VAT_BEAN_OTHER = "vatBeanOther";
	public static final String VAT_BEAN_FORM_BIND = "vatBeanFormBind";
	public static final String VAT_BEAN_FORM_LINK = "vatBeanFormLink";
	
	public static final String RESPONSE_TAG_START = "<";
	public static final String RESPONSE_TAG_END = ">";
	public static final String START_PAGE = "startPage";
	public static final String PAGE_SIZE = "pageSize";

	public static final String GRID_FIELD_NAMES = "fieldNames";
	public static final String FIELD_NAME_INDEX_NO = "indexNo"; // 用來定義INDEX的欄位名稱
	// 回傳 ARRAY TAG 開始
	public static final String RESPONSE_GRID_ARRAY_TAG_START = "[";
	// 回傳 ARRAY TAG 結束
	public static final String RESPONSE_GRID_ARRAY_TAG_END = "]";
	public static final String GRID_DATA = "gridData";
	public static final int DEFAULT_PAGE_RECORD_COUNT = 10; // 預設筆數
	// LINE 目前最大的筆數
	public static final String GRID_LINE_MAX_INDEX = "gridLineMaxIndex";
	public static final String GRID_LINE_FIRST_INDEX = "gridLineFirstIndex";
	public static final String GRID_ROW_COUNT = "gridRowCount";

	// 回傳到頁面上的狀態欄位名稱
	public static final String RETURN_STATUS_MESSAGE_FIELD_NAMES[] = { "status", "msg" };

	// RECORD END TAG
	public static final String GRID_ROW_SPLIT = "{S}";

	// public static final String GRID_ROW_END = "{R_END}";

	// public static final String GRID_COL_COUNT = "gridColCount";

	public static final String IS_DELETE_RECORD_TRUE = "1"; // 要移除的RECORD
	public static final String IS_LOCK_RECORD_TRUE = "1"; // 被鎖定的RECORD
	public static final String IS_LOCK_RECORD_CHECK = "2"; // 被鎖定的RECORD

	public static final String IS_DELETE_RECORD_FALSE = "0"; // 不要移除的RECORD
	public static final String IS_LOCK_RECORD_FALSE = "0"; // 不被鎖定的RECORD

	public static final String TMP_ORDER = "TMP_"; // 暫存用的單號

	public static final String CHECK_OK ="OK";

	public static final int FIELD_TYPE_INT = 0;
	public static final int FIELD_TYPE_LONG = 1;
	public static final int FIELD_TYPE_DOUBLE = 2;
	public static final int FIELD_TYPE_STRING = 3;
	public static final int FIELD_TYPE_DATE = 4;
	public static final int FIELD_TYPE_DATETIME = 5;

	//--udpated by Anber----//
	public static final String TIME_SCOPE = "timeScope";
	public static final String INDEX_TYPE = "indexType";
	public static final String INDEX_TYPE_DEFAULT = "NONE";
	public static final String INDEX_TYPE_AUTO = "AUTO";
	public static final String INDEX_TYPE_NONE = "NONE";
	public static final String SELECTION_TYPE = "selectionType";
	public static final String SELECTION_TYPE_DEFAULT = "NONE";
	public static final String SELECTION_TYPE_CHECK = "CHECK";
	public static final String SELECTION_TYPE_RADIO = "RADIO";
	public static final String SELECTION_TYPE_NONE  = "NONE";
	public static final String SELECTION_Y      = "Y";
	public static final String SELECTION_N      = "N";
	public static final String SEARCH_KEY = "searchKey";
	public static final String SEARCH_KEY_DELIMITER = ",";
	private static TmpAjaxSearchDataService tmpAjaxSearchDataService;

	private static final CycleDetectionStrategy CycleDetectionStrategy = null;
	 
	/**
	 * 依據傳進來的欄位名稱順序組出欄位名稱字串
	 *
	 * @param gridFieldNames
	 * @return
	 */
	public static String getFieldNameOrder(Properties httpRequest, String[] gridFieldNames) {
		log.info("AjaxUtils.getFieldNameOrder");
		StringBuffer re = new StringBuffer();
		String selectionType = AjaxUtils.getSelectionType(httpRequest);
		String indexType = AjaxUtils.getIndexType(httpRequest);
		String prefixStirng = new String("");
		for (String fieldName : gridFieldNames) {
			re.append(fieldName);
			re.append(GRID_ROW_SPLIT);
		}
		if (re.length() > 0)
			re.delete(re.length() - GRID_ROW_SPLIT.length(), re.length());

		if(!SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)){
			prefixStirng = prefixStirng + SELECTION_TYPE + GRID_ROW_SPLIT;
		}

		if(INDEX_TYPE_AUTO.equalsIgnoreCase(indexType)){
			prefixStirng = prefixStirng + FIELD_NAME_INDEX_NO+ GRID_ROW_SPLIT;
		}


		return prefixStirng + re.toString();
	}

	/**
	 * 取得AJAX GRID DATA To Client Page
	 *
	 * @param records :
	 *            data
	 * @param gridFieldNames :
	 *            欄位名稱
	 * @return [record index][field index]
	 */
	public static String getGridFieldValue(Properties httpRequest, List<Properties> records, String[] gridFieldNames, Long firstIndex) {
		log.info("AjaxUtils.getGridFieldValue");
		//System.out.println("GRID_DATA:"+httpRequest.getProperty(AjaxUtils.GRID_DATA));
		//log.info(httpRequest.keySet().toString());
		StringBuffer re = new StringBuffer();
		int recordNum = 0;
		int startY = 0;
		//System.out.println("firstIndex:"+firstIndex);
		String selectionType = AjaxUtils.getSelectionType(httpRequest);
		String indexType = AjaxUtils.getIndexType(httpRequest);
		String timeScope = AjaxUtils.getTimeScope(httpRequest);
		String[] searchKey = AjaxUtils.getSearchKey(httpRequest);
		String x = new String("");  // 第幾筆
		String y = new String("");  // 第幾個欄位
		String id = new String("");  // 第幾筆
		for (Properties record : records) {
			x = String.valueOf(firstIndex + recordNum++);
			y = "0";
			startY = 0;
			/* Updated by anber 2009/7/22
			 * 1.The following program will add a value in the first field,
			 *   when selectionType isn't NONE.
			 * 2.The following program will add a value(serial no.) of the indexNo in return string automatically,
			 *   when the first field name of gridFieldName isn't indexNo.
			 */ //TODO
			
			if (!AjaxUtils.SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)){
				//log.info(x);
				AjaxUtils.combineGridString(re,x,y, AjaxUtils.getSearchStatus(timeScope, record, searchKey));
				startY ++;
			}
			
			if (AjaxUtils.INDEX_TYPE_AUTO.equalsIgnoreCase(indexType)){
				if(!AjaxUtils.SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)){
					y ="1";
					startY++;
				}else{
				        startY++;
				}
				AjaxUtils.combineGridString(re, x, y, x);
			}else{
				id = record.getProperty(AjaxUtils.FIELD_NAME_INDEX_NO);
				x = (StringUtils.hasText(id) ? id : x);
			}

			//-------------------------
			for (int index = 0; index < gridFieldNames.length; index++) {
				String fieldName = gridFieldNames[index];
				//log.info("fieldName before = " + fieldName);
				y = String.valueOf(index+ startY);// 第幾欄
				//if(fieldName.indexOf(".") > 0)
					//fieldName = fieldName.substring(fieldName.indexOf(".") + 1 );
				//log.info("fieldName after = " + fieldName);
				String value = record.getProperty(fieldName);
				//log.info("value = " + value);
				//log.info("x = " + x);
				AjaxUtils.combineGridString(re,x,y,value);
			}
			// re.append(GRID_ROW_END);
		}
		log.info("AjaxUtils.getGridFieldValue result=" + re.toString());
		return re.toString();
	}
	/**
	 * 組合回傳前端的陣列字串，格式範例如下
	 * [1][0]=IMV{S}[1][1]=200904270035{S}[1][2]=2009/4/27{S}[1][3]=T1BS99{S}[1][4]=2009/4/28{S}
	 * @param result
	 * @param x
	 * @param y
	 * @param value
	 */

	private static void combineGridString(StringBuffer result, String x, String y, String value ){
		result.append(RESPONSE_GRID_ARRAY_TAG_START);
		result.append(x);
		result.append(RESPONSE_GRID_ARRAY_TAG_END);
		result.append(RESPONSE_GRID_ARRAY_TAG_START);
		result.append(y);
		result.append(RESPONSE_GRID_ARRAY_TAG_END);
		result.append("=");
		result.append(value);
		result.append(GRID_ROW_SPLIT);
	}

	/**
	 * 取得AJAX GRID DATA到SERVER
	 *
	 * @param records
	 * @param gridFieldNames
	 * @return
	 */
	public static List<Properties> getGridFieldValue(String gridData, int startRow, int row, String[] gridFieldNames) {
		log.info("AjaxUtils.getGridFieldValue startRow=" + startRow + ",row=" + row + ",gridData=" + gridData);
		List<Properties> records = new ArrayList();
		int startPos = 0;
		int endPos = 0;
		int rowCount = startRow + row;
		for (int i_row = startRow; i_row < rowCount; i_row++) {
			Properties oneRecord = new Properties();
			for (int i_column = 0; i_column < gridFieldNames.length; i_column++) {
				String searchKey = "[" + i_row + "][" + i_column + "]=";
				startPos = gridData.indexOf(searchKey) + searchKey.length();
				endPos = gridData.indexOf(GRID_ROW_SPLIT, startPos);
				if (endPos <= 0)
					endPos = gridData.length();
				String key = gridFieldNames[i_column];
				String value = gridData.substring(startPos, endPos);
				oneRecord.setProperty(key, value);
				//System.out.println("gridData.length()=" + gridData.length() + ",searchKey=" + searchKey + "startPos=" + startPos
						//+ ",endPos=" + endPos + ",key=" + key + ",value=" + value);
				if (endPos + GRID_ROW_SPLIT.length() >= gridData.length())
					break;
			}
			records.add(oneRecord);
			if (endPos + GRID_ROW_SPLIT.length() >= gridData.length())
				break;
		}
		return records;
	}

	/**
	 * 取得回傳訊息
	 *
	 * @param reMessageProperties :
	 *            要回傳訊息的值
	 * @return 回傳訊息
	 */
	public static String getReturnMessage(Properties reMessageProperties) {
		log.info("AjaxUtils.getReturnMessage reMessageProperties=" + reMessageProperties.toString());
		StringBuffer returnMsg = new StringBuffer();
		String fieldNames[] = AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES;
		for (String fieldName : fieldNames) {
			returnMsg.append(fieldName);
			returnMsg.append("=");
			returnMsg.append(reMessageProperties.get(fieldName));
			returnMsg.append(GRID_ROW_SPLIT);
		}
		if (returnMsg.length() > 0)
			returnMsg.delete(returnMsg.length() - GRID_ROW_SPLIT.length(), returnMsg.length());
		return returnMsg.toString();
	}

	/**
	 * get properties value not null
	 *
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertiesValue(Object obj, String defaultValue) {
		String re = defaultValue;
		if (null != obj) {
			// log.info("AjaxUtils.getPropertiesValue obj=" + obj.toString() +
			// ",defaultValue=" + defaultValue);
			try {
				re = obj.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return re;
	}

	/**
	 * 取得暫存的單號
	 *
	 * @return
	 */
	public static synchronized String getTmpOrderNo() {
		log.info("AjaxUtils.getTmpOrderNo ");
		StringBuffer sb = new StringBuffer(AjaxUtils.TMP_ORDER);
		sb.append(System.currentTimeMillis());
		return sb.toString();
	}

	/**
	 * 用來判斷暫存單號
	 *
	 * @param orderNo
	 * @return
	 */
	public static boolean isTmpOrderNo(String orderNo) {
		log.info("AjaxUtils.isTmpOrderNo orderNo=" + orderNo);
		if (null != orderNo) {
			if (orderNo.indexOf(AjaxUtils.TMP_ORDER) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 取得 2 Dinam array String
	 * [1][0]=show{S}[2][0]=value{S}[1][1]=show{S}[2][1]=value{S}[1][2]=show{S}[2][2]=value{S}[1][3]=show{S}[2][3]=value
	 *
	 * @param display
	 * @param values
	 * @return
	 */
	public static String getHtmlInput2DString(List<String> displays, List<String> values) {
		log.info("AjaxUtils.getHtmlInput2DString displays=" + displays.toString() + ",values=" + values.toString());
		StringBuffer re = new StringBuffer();
		for (int index = 0; index < displays.size(); index++) {
			String display = displays.get(index);
			String value = values.get(index);

			// display
			re.append(RESPONSE_GRID_ARRAY_TAG_START);
			re.append(1);
			re.append(RESPONSE_GRID_ARRAY_TAG_END);
			re.append(RESPONSE_GRID_ARRAY_TAG_START);
			re.append(index);
			re.append(RESPONSE_GRID_ARRAY_TAG_END);
			re.append("=");
			re.append(display);
			re.append(GRID_ROW_SPLIT);
			// value
			re.append(RESPONSE_GRID_ARRAY_TAG_START);
			re.append(2);
			re.append(RESPONSE_GRID_ARRAY_TAG_END);
			re.append(RESPONSE_GRID_ARRAY_TAG_START);
			re.append(index);
			re.append(RESPONSE_GRID_ARRAY_TAG_END);
			re.append("=");
			re.append(value);
			re.append(GRID_ROW_SPLIT);

		}

		if (re.length() > 0)
			re.delete(re.length() - GRID_ROW_SPLIT.length(), re.length());
		return re.toString();
	}

	/**
	 * response message
	 *
	 * @param errorMsg
	 * @return
	 */
	public static List getResponseMsg(String errorMsg) {
		log.info("AjaxUtils.getResponseMsg ");
		// 訊息處理
		List reMessageList = new ArrayList();
		Properties reMessageProperties = new Properties();
		if (null != errorMsg) {
			// 回傳成功的訊息
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[0], MessageStatus.ERROR);
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[1], errorMsg);
		} else {
			// 回傳成功的訊息
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[0], MessageStatus.SUCCESS);
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[1], "存檔成功");
		}
		reMessageList.add(reMessageProperties);
		return reMessageList;
	}

	/**
	 * set Object Properties
	 *
	 * @param setObj
	 * @param upRecord
	 * @param fieldNames
	 * @param fieldTypes
	 * @throws Exception
	 */
	public static void setPojoProperties(Object setObj, Properties upRecord, String[] fieldNames, int[] fieldTypes) throws Exception {
		log.info("AjaxUtils.setPojoProperties ");
		if (setObj == null)
			throw new Exception("Set Obj is Null ");
		if (fieldNames == null)
			throw new Exception("Field Name is Null ");
		if (fieldTypes == null)
			throw new Exception("Field Type is Null ");
		if (fieldNames.length != fieldTypes.length)
			throw new Exception("Field Name Length != Field Type Length ");

		for (int index = 0; index < fieldNames.length; index++) {
			String fieldName = fieldNames[index];
			String strValue = upRecord.getProperty(fieldName);
			Object objValue = null;
			switch (fieldTypes[index]) {
			case FIELD_TYPE_INT: // int
				objValue = StringUtils.hasText(strValue) ? NumberUtils.getInt(strValue) : null;
				break;
			case FIELD_TYPE_LONG: // long
				objValue = StringUtils.hasText(strValue) ? NumberUtils.getLong(strValue) : null;
				break;
			case FIELD_TYPE_DOUBLE: // double
				objValue = StringUtils.hasText(strValue) ? NumberUtils.getDouble(strValue) : null;
				break;
			case FIELD_TYPE_DATE: // date
				objValue = StringUtils.hasText(strValue) ? DateUtils.parseDate("yyyy/MM/dd", strValue) : null;
				break;
			case FIELD_TYPE_DATETIME: // datetime by Weichun 2011.02.15
				objValue = StringUtils.hasText(strValue) ? DateUtils.parseDate("yyyy/MM/dd hh:mm:ss", strValue) : null;
				break;
			default: // string
				objValue = strValue;
				break;
			}
			if (null != objValue) {
				//log.info("fieldName before= " + fieldName);
				//log.info("fieldName.indexOf = " + (fieldName.indexOf(".") + 1) );
				if(fieldName.indexOf(".") > 0)
					fieldName = fieldName.substring(fieldName.indexOf(".") + 1 );
				//log.info("fieldName after= " + fieldName);
				StringBuffer methodName = new StringBuffer();
				methodName.append("set");
				methodName.append(fieldName.substring(0, 1).toUpperCase());
				methodName.append(fieldName.substring(1));
				MethodUtils.invokeMethod(setObj, methodName.toString(), objValue);
			}
		}
	}

	/**
	 * 取得起始頁面的 Index
	 * @param httpRequest
	 * @return
	 */
	public static int getStartPage(Properties httpRequest) {
		log.info("AjaxUtils.getStartPage ");
		String startPage = httpRequest.getProperty(AjaxUtils.START_PAGE);
		int iSPage = NumberUtils.getInt(startPage);
		if (iSPage > 0)
			iSPage = iSPage - 1;
		return iSPage;
	}

	/**
	 * 要顯示的頁面大小
	 * @param httpRequest
	 * @return
	 */
	public static int getPageSize(Properties httpRequest) {
		log.info("AjaxUtils.getPageSize ");
		String pageSize = httpRequest.getProperty(AjaxUtils.PAGE_SIZE);
		int iPSize = NumberUtils.getInt(pageSize);
		return iPSize;
	}

	/**
	 * 要顯示的流水號型式
	 * @param httpRequest
	 * @return
	 */
	public static String getIndexType(Properties httpRequest) {
		String indexType = (String) httpRequest.getProperty(AjaxUtils.INDEX_TYPE);
		log.info("AjaxUtils.getIndexType :"+indexType);
		return indexType;
	}

	/**
	 * 要顯示的Form的id
	 * @param httpRequest
	 * @return
	 */
	public static String getTimeScope(Properties httpRequest) {
		String timeScope = (String) httpRequest.getProperty(AjaxUtils.TIME_SCOPE);
		if(null == timeScope)
			timeScope = INDEX_TYPE_DEFAULT;
		log.info("AjaxUtils.getTimeScope :" + timeScope);
		return timeScope;
	}

	/**
	 * 要顯示的Grid中選則類型
	 * @param httpRequest
	 * @return
	 */
	public static String getSelectionType(Properties httpRequest) {
		String selectionType = (String)httpRequest.getProperty(AjaxUtils.SELECTION_TYPE);
		if(null==selectionType)
			selectionType = SELECTION_TYPE_DEFAULT;
		log.info("AjaxUtils.getSelectionType :"+ selectionType);
		return selectionType;
	}


	/**
	 * 要顯示的查詢key
	 * @param httpRequest
	 * @return
	 */
	public static String[] getSearchKey(Properties httpRequest) {
		String searchKeyString = (String)httpRequest.getProperty(AjaxUtils.SEARCH_KEY);
		String[] searchKeyArray = StringUtils.commaDelimitedListToStringArray(searchKeyString);
		log.info("AjaxUtils.getSearchKey :" +searchKeyArray.length);
		return searchKeyArray;

	}



	/**
	 * 取得AJAX Page Data Default
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param gridDatas
	 * @return
	 */
	public static Properties getAJAXPageDataDefault(Properties httpRequest, String[] fieldNames, String[] defaultValues ,
			List<Properties> gridDatas) {
		return getAJAXPageDataDefault(httpRequest,fieldNames,defaultValues,null,gridDatas);
	}

	/**
	 * 取得AJAX Page Data Default
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param gridDatas
	 *            回傳 PAGE 的內容
	 * @return Properties
	 */
	public static Properties getAJAXPageDataDefault(Properties httpRequest, String[] fieldNames, String[] defaultValues , Map defaultValueMaps ,
			List<Properties> gridDatas) {
		//log.info("AjaxUtils.getAJAXPageDataDefault ");
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);
		//log.info("iSPage:"+ iSPage + "/iPSize:"+ iPSize);

		Long re[] = { 0L, 0L };
		Long maxIndex = 0L;
		Long firstIndex = 0L;
		// 指定預設筆數資料
		int startRecordIndexStar = iSPage * iPSize;
		int startRecordIndexEnd = startRecordIndexStar + iPSize;
		//if (0 == startRecordIndexStar) {
		if (0 == startRecordIndexStar || 0 == (startRecordIndexStar % iPSize)) { // modified by Weichun 2010.10.18
			startRecordIndexStar++;
			startRecordIndexEnd++;
			maxIndex++;
		} else {
			// 20090114 shan 如果是最後一頁 要取得最大筆數
			maxIndex = Long.valueOf(startRecordIndexEnd);
		}
		int startFieldIndex = 0;
		for (int index = startRecordIndexStar; index < startRecordIndexEnd; index++) {
			Properties pro = new Properties();
			// updater by anber 0720 for fieldNames without indexNo
			if(FIELD_NAME_INDEX_NO.equalsIgnoreCase(fieldNames[0])){
				pro.setProperty(fieldNames[0], String.valueOf(index));
				startFieldIndex  = 1;
			}
			for (int fIndex = startFieldIndex; fIndex < fieldNames.length; fIndex++) {
				String fieldName = fieldNames[fIndex];
				if(fieldName.indexOf(".") > 0)
					fieldName = fieldName.substring(fieldName.indexOf(".") + 1 );
				String defValue = defaultValues[fIndex] ;
				//System.out.println("fieldName:"+fieldNames[fIndex]);
				if( null != defaultValueMaps && null != defaultValueMaps.get(fieldName) ){
					//System.out.println("defaultValue:"+defaultValueMaps.get(fieldNames[fIndex]));
					defValue = defaultValueMaps.get(fieldName).toString() ;
				}
				pro.setProperty( fieldName, defValue );
			}
			gridDatas.add(pro);
		}

		// 取得第一筆的INDEX
		System.out.println("============ " + startRecordIndexStar + " ============");
		firstIndex = Long.valueOf(startRecordIndexStar);
		re[0] = firstIndex;
		re[1] = maxIndex;

		// 要回傳的DATA
		Properties gridPro = new Properties();
		// 指定GRID FIELD NAME ORDER
		gridPro.setProperty(AjaxUtils.GRID_FIELD_NAMES, getFieldNameOrder(httpRequest,fieldNames));
		// 指定GRID DATA
		gridPro.setProperty(AjaxUtils.GRID_DATA, getGridFieldValue(httpRequest, gridDatas, fieldNames, firstIndex)); // updated by anber 7/20 TODO
		// 指定GRID 最後一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_MAX_INDEX, String.valueOf(maxIndex.longValue()));
		// 指定GRID 第一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_FIRST_INDEX, String.valueOf(firstIndex));
		// 指定ROW COUNT & COL COUNT
		gridPro.setProperty(AjaxUtils.GRID_ROW_COUNT, String.valueOf(gridDatas.size()));

		return gridPro;
	}

	/**
	 * 取得 AJAX 頁面資料
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param lines
	 * @param gridDatas
	 * @param firstIndex
	 * @param maxIndex
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Properties getAJAXPageData(Properties httpRequest, String[] fieldNames, String[] defaultValues, List lines,
			List<Properties> gridDatas, Long firstIndex, Long maxIndex) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception{
		return getAJAXPageData(httpRequest,fieldNames,defaultValues,null,lines,gridDatas,firstIndex,maxIndex);
	}

	/**
	 * 取得 AJAX 頁面資料
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param defaultValueMaps
	 * @param lines
	 * @param gridDatas
	 * @param firstIndex
	 * @param maxIndex
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Properties getAJAXPageData(Properties httpRequest, String[] fieldNames, String[] defaultValues, Map defaultValueMaps , List lines,
			List<Properties> gridDatas, Long firstIndex, Long maxIndex) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
		//log.info("AjaxUtils.getAJAXPageData.firstIndex= " + firstIndex);
		if (null != lines && lines.size() > 0) {
			//log.info("--------------------------------------");
			for (int index = 0; index < lines.size(); index++) {
				Object getObj = lines.get(index);
				Properties pro = new Properties();
				for (int indexfName = 0; indexfName < fieldNames.length; indexfName++) {
					String fieldName = fieldNames[indexfName];
					//if(fieldName.indexOf(".") > 0)
						//fieldName = fieldName.substring(fieldName.indexOf(".") + 1 );
					String defValue = defaultValues[indexfName] ;
					if( null != defaultValueMaps && null != defaultValueMaps.get(fieldNames[indexfName]) ){
						defValue = defaultValueMaps.get(fieldNames[indexfName]).toString() ;
					}
					String value = BeanUtils.getNestedProperty(getObj, fieldName);
					if(StringUtils.hasText(value)){
						try{
							Class cls = PropertyUtils.getPropertyType(getObj, fieldName);
							if ("class java.util.Date".equals(cls.toString())) {
								if("dateTime".equals(fieldName)){ // 簽核頁簽的簽核日期顯示時分秒，修改為java.sql.Timestamp by Weichun 2011.02.14
									Date date = DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", value);
									value = DateUtils.format(date, "yyyy/MM/dd HH:mm:ss");
								}else{
									Date date = DateUtils.parseDate(value);
									value = DateUtils.shortDateConverter(DateUtils.format(date, "yyyy/MM/dd"));
								}
							} else if ("class java.lang.Double".equals(cls.toString())) {
								value = NumberUtils.roundToStr(new Double(value), 6);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					//log.info("lines:"+index+"/"+indexfName+"."+fieldName +"/" + value);
					pro.setProperty(fieldName, AjaxUtils.getPropertiesValue(value, defValue) );
				}
				//log.info("--------------------------------------");
				gridDatas.add(pro);
			}
		}
		// 要回傳的DATA
		Properties gridPro = new Properties();
		// 指定GRID FIELD NAME ORDER
		gridPro.setProperty(AjaxUtils.GRID_FIELD_NAMES, getFieldNameOrder(httpRequest, fieldNames));
		// 指定GRID DATA
		gridPro.setProperty(AjaxUtils.GRID_DATA, getGridFieldValue(httpRequest, gridDatas, fieldNames, firstIndex)); // updated by anber TODO
		// 指定GRID 最後一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_MAX_INDEX, String.valueOf(maxIndex.longValue()));
		// 指定GRID 第一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_FIRST_INDEX, String.valueOf(firstIndex));
		// 指定ROW COUNT & COL COUNT
		gridPro.setProperty(AjaxUtils.GRID_ROW_COUNT, String.valueOf(gridDatas.size()));
		return gridPro;
	}


	/**
	 * 取得 AJAX 頁面資料 join table
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param lines
	 * @param gridDatas
	 * @param firstIndex
	 * @param maxIndex
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	public static Properties getAJAXJoinTablePageData(Properties httpRequest, String[] fieldNames, String[] defaultValues, List lines,
			List<Properties> gridDatas, Long firstIndex, Long maxIndex, Integer valuesColumn) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
		return getAJAXJoinTablePageData(httpRequest,fieldNames,defaultValues,null,lines,gridDatas,firstIndex,maxIndex, valuesColumn);
	}
	/**
	 * 取得 AJAX 頁面資料
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param defaultValueMaps
	 * @param lines
	 * @param gridDatas
	 * @param firstIndex
	 * @param maxIndex
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Properties getAJAXJoinTablePageData(Properties httpRequest, String[] fieldNames, String[] defaultValues, Map defaultValueMaps , List lines,
		List<Properties> gridDatas, Long firstIndex, Long maxIndex, Integer valuesColumn) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
	    log.info("AjaxUtils.getAJAXPageData.firstIndex= " + firstIndex);

	    if (null != lines && lines.size() > 0) {

		//log.info("--------------------------------------");
		for (int index = 0; index < lines.size(); index++) {
		    //log.info("index = " + index);
		    Object[] getObj = (Object[])lines.get(index);
		    //log.info("index2 = index2");
		    Properties pro = new Properties();

		    if(valuesColumn == null){
			for (int indexfName = 0; indexfName < fieldNames.length; indexfName++) {

			    String fieldName = fieldNames[indexfName];
			    String defValue = defaultValues[indexfName] ;
			    if( null != defaultValueMaps && null != defaultValueMaps.get(fieldNames[indexfName]) ){
				defValue = defaultValueMaps.get(fieldNames[indexfName]).toString() ;
			    }
			    String value = getObj[indexfName] != null ? getObj[indexfName].toString() : "";

			    //log.info("lines:"+index+"/"+indexfName+"."+fieldName +"/" + value);

			    pro.setProperty(fieldName, AjaxUtils.getPropertiesValue(value, defValue) );
			}
		    }else{
			//log.info("valuesColumn = " + valuesColumn.toString());
			for (int indexfName = 0; indexfName < fieldNames.length; indexfName++) {

			    String fieldName = fieldNames[indexfName];
			    String defValue = defaultValues[indexfName] ;
			    if( null != defaultValueMaps && null != defaultValueMaps.get(fieldNames[indexfName]) ){
				defValue = defaultValueMaps.get(fieldNames[indexfName]).toString() ;
			    }
			    String valueString = getObj[valuesColumn.intValue()] != null ? getObj[valuesColumn.intValue()].toString() : "";
			    //log.info("valueString = " + valueString);
			    String[] values = valueString.split(AjaxUtils.SEARCH_KEY_DELIMITER);

			    //log.info("lines:"+index+"/"+indexfName+"."+fieldName +"/" + values[indexfName]);

			    pro.setProperty(fieldName, AjaxUtils.getPropertiesValue(values[indexfName], defValue) );
			}
		    }
		    //log.info("--------------------------------------");
		    gridDatas.add(pro);
		}
	    }

	    // 要回傳的DATA
	    Properties gridPro = new Properties();
	    // 指定GRID FIELD NAME ORDER
	    gridPro.setProperty(AjaxUtils.GRID_FIELD_NAMES, getFieldNameOrder(httpRequest, fieldNames));
	    // 指定GRID DATA
	    gridPro.setProperty(AjaxUtils.GRID_DATA, getGridFieldValue(httpRequest, gridDatas, fieldNames, firstIndex)); // updated by anber TODO
	    // 指定GRID 最後一筆
	    gridPro.setProperty(AjaxUtils.GRID_LINE_MAX_INDEX, String.valueOf(maxIndex.longValue()));
	    // 指定GRID 第一筆
	    gridPro.setProperty(AjaxUtils.GRID_LINE_FIRST_INDEX, String.valueOf(firstIndex));
	    // 指定ROW COUNT & COL COUNT
	    gridPro.setProperty(AjaxUtils.GRID_ROW_COUNT, String.valueOf(gridDatas.size()));
	    return gridPro;
	}

	/**
	 * remove delete mark record
	 *
	 * @param updateObj
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static void removeAJAXLine(List lineObjs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		log.info("AjaxUtils.removeAJAXLine");
		Iterator it = lineObjs.iterator();
		while (it.hasNext()) {
			Object removeObj = it.next();
			String delRecord = null ;
			if (null != removeObj) {
				StringBuffer methodName = new StringBuffer();
				methodName.append("getIsDeleteRecord");
				delRecord = (String)MethodUtils.invokeMethod(removeObj, methodName.toString(),null);
			}
			if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(delRecord)) {
				it.remove();
				lineObjs.remove(removeObj);
			}
		}
	}

	/**
	 * parseBeanValueToJSON
	 *
	 * @param
	 * @return
	 */
	public static String parseBeanToJSON(Object pojoBean){
		JSONObject jsonO = JSONObject.fromObject(pojoBean);
		for(Iterator iter = jsonO.keys();iter.hasNext();){
			String keyName = (String)iter.next();
			String keyValue = (String)jsonO.get(keyName);
			if(keyValue == null || "null".equals(keyValue)){
				jsonO.remove(keyName);
			}
		}
		return jsonO.toString();
	}


	public static void copyJSONBeantoPojoBean(Object jsonBean , Object pojoBean)throws Exception{

		try{
			List pojoDateFields = new ArrayList();
			Map pojoDescript = PropertyUtils.describe(pojoBean);
			JSONObject jsonObject = JSONObject.fromObject(jsonBean);
			for(Iterator iter = pojoDescript.keySet().iterator();iter.hasNext();){
				String keyName = (String)iter.next();
				Class typeClazz = PropertyUtils.getPropertyType(pojoBean, keyName);
				System.out.println(typeClazz);
				Object finalValue =null;
				String tmpData = (String)jsonObject.get(keyName);
				if("class java.util.Date".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					pojoDateFields.add(keyName);
					Date sTod =null;
				        String[] tmpArray = tmpData.split("/");
					int year = Integer.parseInt(tmpArray[0]);
					int month = Integer.parseInt(tmpArray[1])-1;
					int day = Integer.parseInt(tmpArray[2]);
					Calendar c = Calendar.getInstance();
					c.set(year, month , day , 0 , 0 , 0);
					sTod = c.getTime();
					finalValue = sTod;
				}else if("class java.lang.Integer".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					finalValue = new Integer(tmpData);
				}else if("class java.lang.Long".equals(typeClazz.toString())&& StringUtils.hasText(tmpData)){
					finalValue = new Long(tmpData);
				}else if("class java.lang.Double".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					String replaceS = tmpData.replaceAll(",", "");
					finalValue = new Double(replaceS);
				}else if("class java.lang.String".equals(typeClazz.toString()) && " ".equals(tmpData)){
					//finalValue = tmpData;
				}else if("class java.lang.String".equals(typeClazz.toString()) && StringUtils.hasText(tmpData)){
					finalValue = tmpData;
				}else{
					continue;
				}
				PropertyUtils.setProperty(pojoBean,keyName,finalValue);
			}

		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	public static List produceSelectorData(List dataList,String tv,String tn,boolean isCombine,boolean isDefaultValue){
		List tmpList = new ArrayList();
		List afterCombineValueList = new ArrayList();
		List afterCombineNameList = new ArrayList();
		List isDefaultList = new ArrayList();
		isDefaultList.add("");//加入下拉選單第一項為請選擇
		isDefaultList.add(" ");
		isDefaultList.add(new Boolean(isDefaultValue));
		try{
			for(Iterator iter = dataList.iterator(); iter.hasNext();){
				Object dataObj = (Object)iter.next();
				Map allDcMap = PropertyUtils.describe(dataObj);
				if(allDcMap.isEmpty()){
					throw new Exception("下拉選單傳入資料，無值");
				}
				String tmpValue = "";
				String tmpName = "";
				if(allDcMap.get(tv)==null){
					Object tmpO = allDcMap.get("id");
					tmpValue = (String)PropertyUtils.getProperty(tmpO, tv);
				}else{
					tmpValue = String.valueOf(PropertyUtils.getProperty(dataObj, tv));
				}
				if(allDcMap.get(tn)==null){
					Object tmpO = allDcMap.get("id");
					tmpName = (String)PropertyUtils.getProperty(tmpO, tn);
				}else{
					tmpName = String.valueOf(PropertyUtils.getProperty(dataObj, tn));
				}
				if(isCombine){
					afterCombineNameList.add(tmpValue+"-"+tmpName);
				}else{
					afterCombineNameList.add(tmpName);
				}
				afterCombineValueList.add(tmpValue);
			}

			tmpList.add(isDefaultList);
			tmpList.add(afterCombineNameList);
			tmpList.add(afterCombineValueList);
		}catch(Exception ex){
			log.error(ex.getMessage());
		}

		return tmpList;
	}

	public static List produceSelectorData(List dataList,String tv,String tn,boolean isCombine,boolean isDefaultValue,String defaultValue){
		List tmpList = new ArrayList();
		List afterCombineValueList = new ArrayList();
		List afterCombineNameList = new ArrayList();
		List isDefaultList = new ArrayList();
		isDefaultList.add("");//加入下拉選單第一項為請選擇
		isDefaultList.add(defaultValue);
		isDefaultList.add(new Boolean(isDefaultValue));
		try{
			for(Iterator iter = dataList.iterator(); iter.hasNext();){
				Object dataObj = (Object)iter.next();
				Map allDcMap = PropertyUtils.describe(dataObj);
				if(allDcMap.isEmpty()){
					throw new Exception("下拉選單傳入資料，無值");
				}
				String tmpValue = "";
				String tmpName = "";
				if(allDcMap.get(tv)==null){
					Object tmpO = allDcMap.get("id");
					tmpValue = (String)PropertyUtils.getProperty(tmpO, tv);
				}else{
					tmpValue = String.valueOf(PropertyUtils.getProperty(dataObj, tv));
				}
				if(allDcMap.get(tn)==null){
					Object tmpO = allDcMap.get("id");
					tmpName = (String)PropertyUtils.getProperty(tmpO, tn);
				}else{
					tmpName = String.valueOf(PropertyUtils.getProperty(dataObj, tn));
				}
				if(isCombine){
					afterCombineNameList.add(tmpValue+"-"+tmpName);
				}else{
					afterCombineNameList.add(tmpName);
				}
				afterCombineValueList.add(tmpValue);
			}

			tmpList.add(isDefaultList);
			tmpList.add(afterCombineNameList);
			tmpList.add(afterCombineValueList);
		}catch(Exception ex){
			log.error(ex.getMessage());
		}

		return tmpList;
	}

	public static String parseSelectorData(List selectList){
		StringBuffer returnString = new StringBuffer();
		List subList = (List)selectList.get(0);
		returnString.append("[[\""+subList.get(0)+"\",");
		returnString.append("\""+subList.get(1)+"\",");
		returnString.append(subList.get(2)+"]");
		for (int i = 1; i < selectList.size(); i++) {
			subList = (List)selectList.get(i);
			returnString.append(",[");
			for(int j=0 ; j<subList.size() ; j++){
				returnString.append("\""+subList.get(j)+"\"");
				if(j+1 == subList.size())
					returnString.append("]");
				else
					returnString.append(",");
			}
			if(subList.size() == 0)
				returnString.append("]");
		}
		returnString.append("]");
		return returnString.toString();
	}

	public static List parseReturnDataToJSON(Map otherMap){
		List tmpList = new ArrayList();
		Properties pro = new Properties();
		Object obj = otherMap.get("form");
		Map fMap = new HashMap();

		if(null != otherMap.get("topLevel")){//取出是不是有java bean是要產生JSON字串時和vatBeanOther同一層
			String[] top = (String[])otherMap.get("topLevel");
			for(int i=0; i< top.length; i++){//把topLevel裏的定義的bean取出在放到fMap
				String tmpTopName = top[i];
				fMap.put(tmpTopName, otherMap.get(tmpTopName));
				otherMap.remove(tmpTopName);
			}
		};
		fMap.put("vatBeanOther", otherMap);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessor(){
			public Object processArrayValue(Object value, JsonConfig jsonC){return new Object();}
			public Object processObjectValue(String key, Object value, JsonConfig jsonC){
				Calendar c = Calendar.getInstance();
				c.setTime((Date)value);
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONDAY)+1;
				int day = c.get(Calendar.DAY_OF_MONTH);
				return year+"/"+month+"/"+day+"";}
		});
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor(){
			public Object processArrayValue(Object value, JsonConfig jsonC){return new Object();}
			public Object processObjectValue(String key, Object value, JsonConfig jsonC){
				Calendar c = Calendar.getInstance();
				c.setTime((Date)value);
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONDAY)+1;
				int day = c.get(Calendar.DAY_OF_MONTH);
				return year+"/"+month+"/"+day+"";}
		});
		//避掉List的干擾
		jsonConfig.registerJsonValueProcessor(List.class, new JsonValueProcessor(){
			public Object processArrayValue(Object value, JsonConfig jsonC){return new Object();}
			public Object processObjectValue(String key, Object value, JsonConfig jsonC){
				value = null;
				return null;
			}
		});
		//避掉Set的干擾
		jsonConfig.registerJsonValueProcessor(Set.class, new JsonValueProcessor(){
			public Object processArrayValue(Object value, JsonConfig jsonC){return new Object();}
			public Object processObjectValue(String key, Object value, JsonConfig jsonC){
				value = null;
				return null;
			}
		});
		jsonConfig.setJsonPropertyFilter(new PropertyFilter(){
			 public boolean apply( Object source, String name, Object value ) {
			      if( value == null ){
			         return true;
			      }
			      return false;
			   }
		});
		try{
			if(obj == null){
				otherMap.remove("form");
			}
			JSONObject jsonObj = new JSONObject().fromObject(fMap, jsonConfig);
			String rtnJSONString = jsonObj.toString();
			System.out.println(rtnJSONString);
			pro.setProperty("vatBean", rtnJSONString);
			tmpList.add(pro);
		}catch(Exception ex){
			log.error(ex.getMessage());
		}

		return tmpList;
	}

	/**
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @throws Exception
	 */
	public static void updateSearchResult(Properties httpRequest, String[] fieldNames) throws Exception{
		log.info("AjaxUtils.updateSearchResult");
		String timeScope  = AjaxUtils.getTimeScope(httpRequest);
		String selectionType = AjaxUtils.getSelectionType(httpRequest);
		try{
			if (!AjaxUtils.SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)){
				String[][] searchArray = getSearchList(httpRequest,  fieldNames) ;
				//log.info("searchArray.length:"+ searchArray.length);
				if(searchArray.length >0){
					TmpAjaxSearchDataService service =
						(TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
					service.update(timeScope, searchArray, selectionType);
				}

			}
		}catch(Exception ex){
			throw new Exception("查詢資料更新時發生錯誤:"+ex.toString());
		}
	}

	/**
	 * for 可更新 selectionData
	 * @param httpRequest
	 * @param fieldNames
	 * @param pkIndex
	 * @throws Exception
	 */
	public static void updateSearchResult(Properties httpRequest, String[] fieldNames, Integer pkIndex) throws Exception{
		log.info("AjaxUtils.updateSearchResult");
		String timeScope  = AjaxUtils.getTimeScope(httpRequest);
		String selectionType = AjaxUtils.getSelectionType(httpRequest);
		try{
			if (!AjaxUtils.SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)){
				String[][] searchArray = getSearchList(httpRequest,  fieldNames) ;
				//log.info("searchArray.length:"+ searchArray.length);
				if(searchArray.length >0){
					TmpAjaxSearchDataService service =
						(TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
					service.update(timeScope, searchArray, selectionType, pkIndex);
				}

			}
		}catch(Exception ex){
			throw new Exception("查詢資料更新時發生錯誤:"+ex.toString());
		}
	}


	/**
	 * 取得更新TmpAjaxSearchData的array
	 * @author Anber Liu
	 * @param timeScope
	 * @param searchKey
	 * @param httpRequest
	 * @param gridFieldNames
	 * @return
	 */
	public static String[][] getSearchList(Properties httpRequest,  String[] gridFieldNames) {
		log.info("AjaxUtils.getSearchList");
		//String timeScope  = AjaxUtils.getTimeScope(httpRequest);
		String[] searchKey = AjaxUtils.getSearchKey(httpRequest);
		List<Properties> gridProperties = getGridFieldValue(httpRequest, gridFieldNames);
		int rowId = AjaxUtils.getStartPage(httpRequest) * AjaxUtils.getPageSize(httpRequest) + 1;

		String[][] searchArray = new String[gridProperties.size()][3];
		int i=0;
		for(Properties gridPropertiy: gridProperties){
			//searchArray[i][0] = timeScope;
			searchArray[i][0] = getSearchKeyString(gridPropertiy, searchKey);
			searchArray[i][1] = (String)gridPropertiy.get(AjaxUtils.SELECTION_TYPE);
			searchArray[i][2] = String.valueOf(rowId)  ;

			//log.info("getSearchList:"+ "timeScope:"+ searchArray[i][0] + " searchKey:"+ searchArray[i][1] + " status:"+ searchArray[i][2]);
			rowId++;
			i++;
		}

		return searchArray;
	}

	/**
	 * 取得AJAX GRID DATA到SERVER
	 * 可取得checkbox、radio及系統產生indexNo的值
	 * @author Anber Liu
	 * @param records
	 * @param gridFieldNames
	 * @return
	 */
	public static List<Properties> getGridFieldValue(Properties httpRequest, String[] gridFieldNames) {
		String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
	    int startRow = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
	    int row = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
	    String selectionType = AjaxUtils.getSelectionType(httpRequest);
	    String indexType = AjaxUtils.getIndexType(httpRequest);
		log.info("getGridFieldValue startRow=" + startRow + ",row=" + row + ",gridData=" + gridData);

		List<Properties> records = new ArrayList();
		int startPos = 0;
		int endPos = 0;
		int rowCount = startRow + row;

		String key = new String();
		String value =  new String();

		String newGridFieldName[] = new String[0];
		if (!AjaxUtils.SELECTION_TYPE_NONE.equalsIgnoreCase(selectionType)) {
			newGridFieldName  = ArraysUtils.append(newGridFieldName, AjaxUtils.SELECTION_TYPE);
		}
		if (AjaxUtils.INDEX_TYPE_AUTO.equalsIgnoreCase(indexType)){
			newGridFieldName  = ArraysUtils.append(newGridFieldName, AjaxUtils.INDEX_TYPE);
		}
		newGridFieldName = ArraysUtils.merge(newGridFieldName, gridFieldNames);

		for (int i_row = startRow; i_row < rowCount; i_row++) {
			Properties oneRecord = new Properties();

			for (int i_column = 0; i_column < newGridFieldName.length; i_column++) {
				String searchMatrix = "[" + i_row + "][" + i_column + "]=";
				startPos = gridData.indexOf(searchMatrix) + searchMatrix.length();
				endPos = gridData.indexOf(GRID_ROW_SPLIT, startPos);
				if (endPos <= 0)
					endPos = gridData.length();
				key = newGridFieldName[i_column];
				value = gridData.substring(startPos, endPos);
				oneRecord.setProperty(key, value);

				//log.info("getGridFieldValue gridDataLength=" + gridData.length() + ",searchMatrix=" + searchMatrix + "startPos=" + startPos
				//		+ ",endPos=" + endPos + ",key=" + key + ",value=" + value);

				if (endPos + GRID_ROW_SPLIT.length() >= gridData.length())
					break;
			}
			records.add(oneRecord);
			if (endPos + GRID_ROW_SPLIT.length() >= gridData.length())
				break;
		}
		log.info("getGridFieldValue.size:"+ records.size());
		return records;
	}



	public void setTmpAjaxSearchDataService(TmpAjaxSearchDataService tmpAjaxSearchDataService) {
		this.tmpAjaxSearchDataService = tmpAjaxSearchDataService;
	}
	/**
	 * 取得更新TmpAjaxSearchData的array
	 * 依據資料的Key值(如:headId)，是否於TmpAjaxSearchData中存在，如果存在回傳SELECTION_Y
	 * @author Anber Liu
	 * @param timeScope
	 * @param searchKey
	 * @param httpRequest
	 * @param gridFieldNames
	 * @return
	 */
	public static String getSearchStatus(String timeScope,  Properties record, String[] searchKey) {
		String result = new String();
		String searchString = getSearchKeyString(record, searchKey);
		//log.info("AjaxUtils.getSearchStatus timeScope:"+ timeScope + " searchKey:"+ searchString.toString() );
		TmpAjaxSearchDataService service = (TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
		TmpAjaxSearchData tmpAjaxSearchData = (TmpAjaxSearchData) service.findByKey(timeScope, searchString);


		if(null == tmpAjaxSearchData)
			result = AjaxUtils.SELECTION_N;
		else
			result = AjaxUtils.SELECTION_Y;
		log.info(" getSearchStatus.timeScope:"+ timeScope + " searchKey:"+ searchString + " status:"+ result);
		return result;
	}

	/**
	 * 清除TmpAjaxSearchData的資料
	 * @param timeScope
	 * @return
	 * @throws Exception
	 */
	//TODO
	public static void clearSearchData(String timeScope) throws Exception {
		try{
			TmpAjaxSearchDataService service = (TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
			service.deleteByTimeScope(timeScope);
		}catch(Exception ex){
			throw new Exception("清除查詢資料失敗，原因"+ex.toString());
		}
	}


	public static List<Properties> getSelectedResults(String timeScope, ArrayList searchKeys) throws Exception {
		String[] searchArray = new String[searchKeys.size()];
		for(int i= 0 ; i < searchKeys.size(); i++){
			searchArray[i] = (String) searchKeys.get(i);
		}
		return getSelectedResults( timeScope,searchArray);

	}

	public static List<Properties> getSelectedResults(String timeScope, ArrayList searchKeys, boolean convertSearchKey) throws Exception {
		String[] searchArray = new String[searchKeys.size()];
		for(int i= 0 ; i < searchKeys.size(); i++){
			if(convertSearchKey){
				String sKey =((String) searchKeys.get(i)).replace(".", "_");
				System.out.println(sKey);
				searchArray[i] = sKey;
			}else
				searchArray[i] = (String) searchKeys.get(i);
		}
		return getSelectedResults( timeScope,searchArray);

	}
	/**
	 * 將儲存於TmpAjaxSearchData的資料取出
	 * @param timeScope
	 * @param searchKey
	 * @return
	 * @throws Exception
	 */
	public static List<Properties> getSelectedResults(String timeScope, String[] searchKey) throws Exception {
		try{
			TmpAjaxSearchDataService service = (TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
			List<TmpAjaxSearchData> tmpAjaxSearchDatas = service.findByTimeScope(timeScope);
			List<Properties> result = new ArrayList(0);
			log.info("getSelectedResults.tmpAjaxSearchDatas.size"+ tmpAjaxSearchDatas.size());
			for(TmpAjaxSearchData tmpAjaxSearchData : tmpAjaxSearchDatas){
				Properties record = new Properties();
				String keyString = tmpAjaxSearchData.getSelectionData();
				//log.info("getSelectedResults.keyString:"+keyString);
				if(searchKey.length < 2){
					record.put(searchKey[0],keyString);
				}else{
					String[] searchData =  StringTools.StringToken(keyString, SEARCH_KEY_DELIMITER);
					//log.info("getSelectedResults.searchData.length:"+searchData.length);
					for(int i=0 ; i< searchKey.length; i++){
						record.put(searchKey[i], searchData[i]);
					}
				}
				result.add(record);
			}
			return result;
		}catch(Exception ex){
			throw new Exception("取得查詢資料失敗，原因"+ex.toString());
		}
	}

	/**
	 * 取得查詢Key的組合字串
	 * 如：123456,T1BS
	 * @param record
	 * @param searchKey
	 * @return
	 */
	public static String getSearchKeyString(Properties record, String[] searchKey){
		StringBuffer searchString = new StringBuffer();
		for(int j=0; j< searchKey.length ;j++){
			searchString.append((String)record.get(searchKey[j]));
			if (j < searchKey.length -1)
				searchString.append(SEARCH_KEY_DELIMITER);
		}
		return searchString.toString();

	}
	/**
	 * 將查詢條件所查出之Key List 儲存到TmpAjaxSearchData中
	 *
	 * @param timeScope
	 * @param allDataList 只存在儲存到TmpAjaxSearchData中的Key value 如:headId 其餘欄位不需存在
	 * @throws Exception
	 */
	public static void updateAllResult(String timeScope, String isAllClick, List allDataList) throws Exception{
		TmpAjaxSearchDataService service =
			(TmpAjaxSearchDataService) SpringUtils.getApplicationContext().getBean("tmpAjaxSearchDataService");
		if ("Y".equals(isAllClick)){
			Iterator results = allDataList.iterator();
			String[][] searchArray = new String[allDataList.size()][3];
			int i =0;
			//log.info("allDataList.size:"+allDataList.size());
			while ( results.hasNext() ) {
				StringBuffer keyString = new StringBuffer();
				Object rowObj = (Object) results.next();
				if(rowObj.getClass().isArray()){
					Object[] row = (Object[]) rowObj;
				    for(int j = 0; j< row.length ; j++){
				    	keyString.append(row[j].toString());
				    	if (j < row.length -1)
				    		keyString.append(SEARCH_KEY_DELIMITER);
				    }
				}else{
					keyString.append( rowObj.toString());
				}
				//log.info(i+".keyString:"+keyString.toString());
			    searchArray[i][0] = keyString.toString();
			    searchArray[i][1] = AjaxUtils.SELECTION_Y;
			    searchArray[i][2] = String.valueOf(i);
			    i++;
			}

			if(searchArray.length >0){
				service.update(timeScope, searchArray, AjaxUtils.SELECTION_TYPE_CHECK);
			}
		}else{
			service.deleteByTimeScope(timeScope);
		}

	}

	/** 
	 * Created by Anber 2011/05/06
	 * 取得 AJAX 頁面資料
	 *
	 * @param httpRequest
	 * @param fieldNames
	 * @param defaultValues
	 * @param defaultValueMaps
	 * @param lines
	 * @param gridDatas
	 * @param firstIndex
	 * @param maxIndex
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Properties getAJAXPageDataByType(Properties httpRequest, String[] fieldNames, String[] defaultValues, int[] defaultTypes, List lines,
			List<Properties> gridDatas, Long firstIndex, Long maxIndex) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
		log.info("AjaxUtils.getAJAXPageData.firstIndex= " + firstIndex);

		if (null != lines && lines.size() > 0) {

			//log.info("--------------------------------------");
			for (int index = 0; index < lines.size(); index++) {
				Object getObj = lines.get(index);
				Properties pro = new Properties();

				for (int indexfName = 0; indexfName < fieldNames.length; indexfName++) {

					String fieldName = fieldNames[indexfName];
					//String defaultValue = defaultValues[indexfName];
					String defValue = defaultValues[indexfName] ;
					int defType = defaultTypes[indexfName] ;
		
					String value = BeanUtils.getNestedProperty(getObj, fieldName);
					if(StringUtils.hasText(value)){
						try{
						    Class cls = PropertyUtils.getPropertyType(getObj, fieldName);
							if ("class java.util.Date".equals(cls.toString())) {
								System.out.println("===== java.util.Date =====");
								if(AjaxUtils.FIELD_TYPE_DATETIME == defType){ // 簽核頁簽的簽核日期顯示時分秒，修改為java.sql.Timestamp by Weichun 2011.02.14
									Date date = DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", value);
									value = DateUtils.format(date, "yyyy/MM/dd HH:mm:ss");
								}else{
									Date date = DateUtils.parseDate(value);
									value = DateUtils.shortDateConverter(DateUtils.format(date, "yyyy/MM/dd"));
								}
							} else if ("class java.lang.Double".equals(cls.toString())) {
								value = NumberUtils.roundToStr(new Double(value), 6);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					//log.info("lines:"+index+"/"+indexfName+"."+fieldName +"/" + value);
					pro.setProperty(fieldName, AjaxUtils.getPropertiesValue(value, defValue) );
				}
				//log.info("--------------------------------------");
				gridDatas.add(pro);
			}
		}

		// 要回傳的DATA
		Properties gridPro = new Properties();
		// 指定GRID FIELD NAME ORDER
		gridPro.setProperty(AjaxUtils.GRID_FIELD_NAMES, getFieldNameOrder(httpRequest, fieldNames));
		// 指定GRID DATA
		gridPro.setProperty(AjaxUtils.GRID_DATA, getGridFieldValue(httpRequest, gridDatas, fieldNames, firstIndex)); // updated by anber TODO
		// 指定GRID 最後一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_MAX_INDEX, String.valueOf(maxIndex.longValue()));
		// 指定GRID 第一筆
		gridPro.setProperty(AjaxUtils.GRID_LINE_FIRST_INDEX, String.valueOf(firstIndex));
		// 指定ROW COUNT & COL COUNT
		gridPro.setProperty(AjaxUtils.GRID_ROW_COUNT, String.valueOf(gridDatas.size()));
		return gridPro;
	}
	
	public static void setSourceObjectValueToDestnation(Object sourceObj, String[] sourceFieldNames, 
			Object destObj, String[] destFieldNames) throws Exception{
		if(sourceFieldNames.length != destFieldNames.length)
			throw new Exception("來源與目的欄位不相符");
		for (int i = 0; i < sourceFieldNames.length; i++) {
			String objFieldName = sourceFieldNames[i];
			String headFieldName = destFieldNames[i];
			if(!StringUtils.hasText(objFieldName))
				continue;
			String value = BeanUtils.getNestedProperty(sourceObj, objFieldName);
			StringBuffer methodName = new StringBuffer();
			methodName.append("set");
			methodName.append(headFieldName.substring(0, 1).toUpperCase());
			methodName.append(headFieldName.substring(1));
			if(StringUtils.hasText(value)){
				try{
					Class cls = PropertyUtils.getPropertyType(destObj, headFieldName);
					if ("class java.util.Date".equals(cls.toString())) {
						Object date = DateUtils.parseDate(value.substring(0,10));
						MethodUtils.invokeMethod(destObj, methodName.toString(), date);
					} else if ("class java.lang.Double".equals(cls.toString())) {
						MethodUtils.invokeMethod(destObj, methodName.toString(), new Double(value));
					}else{
						MethodUtils.invokeMethod(destObj, methodName.toString(), value);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}