package tw.com.tm.erp.importdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 匯入的資料格式設定
 * @author T02049
 *
 */

public class ImportInfo {
	private int keyIndex ; //分辨資料鍵的 位置  (option)
	private String keyValue ; //分辨資料鍵的 值 (option)
	private boolean saveKeyField = false ; //是否要將KEY FIELD 寫入DB	(option)
	private String masterGetDetailListFunctionName ; //get detail list function name in master class (option:可以不用設定,如果沒有設定會抓取 ClassName去組出 function name)
	private String entityBeanClassName ; //class full name include package	
	private List<String> fieldName = new ArrayList() ; // field name
	private List<String> fieldNameNotWrite = new ArrayList() ; // field name not write to db,通常代表的是 data 的 field > 要寫入的 field
	private Properties fieldType = new Properties() ;   // field type , each type must has String construct -> construct(String)  
	private Properties fieldTypeFormat = new Properties() ; // field format DataFormat ... (option)
	private Properties fieldNameFormat = new Properties(); // 針對 field name 做 Data format 會在 Type 之前做轉換 
	private String[] split = {","} ; // (option)
	private int startRecord = 0 ; //從0開始 (option)
	private int[] notImportFieldIndex ; // 哪幾個欄位不要被寫入
	private HashMap uiProperties ; //ui 畫面帶進來的參數
	private HashMap<String,Object> defaultValue = new HashMap() ; // entity default value (option)
	private List<ImportInfo> DetailImportInfos = new ArrayList() ;  // detail info if detail is null 表示沒有 Detail else 有 Detail	 (option)
	private int[] oneRecordDetailKeys ; //如果同一筆中有 Master Detail , 用來指定哪幾個 Column 是 DETAIL 的 KEY FIELD  來表示 Detail (option)
	private int importDataStartRecord = 0 ; //載入資料時從第幾筆開始讀
	private int xlsColumnLength = 0 ; //指定EXCEL一筆RECORD的長度,提供PIO讀近來的數量
	
	

	public int getImportDataStartRecord() {
		return importDataStartRecord;
	}

	public void setImportDataStartRecord(int importDataStartRecord) {
		this.importDataStartRecord = importDataStartRecord;
	}

	public boolean isSaveKeyField() {
		return saveKeyField;
	}

	public void setSaveKeyField(boolean saveKeyField) {
		this.saveKeyField = saveKeyField;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getEntityBeanClassName() {
		return entityBeanClassName;
	}

	public void setEntityBeanClassName(String entityBeanClassName) {
		this.entityBeanClassName = entityBeanClassName;
	}

	public List<String> getFieldName() {
		return fieldName;
	}

	public void setFieldName(List<String> fieldName) {
		this.fieldName = fieldName;
	}
	
	public void addFieldName(String fieldName){
		this.fieldName.add(fieldName);
	}

	public Properties getFieldType() {
		return fieldType;
	}

	public void setFieldType(Properties fieldType) {
		this.fieldType = fieldType;
	}
	
	public void setFieldType(String key,String value) {
		this.fieldType.setProperty(key, value);
	}

	public Properties getFieldTypeFormat() {
		return fieldTypeFormat;
	}

	public void setFieldTypeFormat(Properties fieldFormat) {
		this.fieldTypeFormat = fieldFormat;
	}
	
	public void setFieldTypeFormat(String key,String value) {
		this.fieldTypeFormat.setProperty(key, value);
	}
	

	public HashMap<String,Object> getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(HashMap<String,Object> defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void addDefaultValue(String key,Object value){
		this.defaultValue.put(key, value);
	}

	public List<ImportInfo> getDetailImportInfos() {
		return DetailImportInfos;
	}

	public void setDetailImportInfos(List<ImportInfo> detailImportInfos) {
		this.DetailImportInfos = detailImportInfos;
	} 
	
	public void addDetailImportInfos(ImportInfo detailImportInfos){
		this.DetailImportInfos.add(detailImportInfos);
	}

	public String getMasterGetDetailListFunctionName() {
		if(null == this.masterGetDetailListFunctionName){
			String className = entityBeanClassName.substring( entityBeanClassName.lastIndexOf('.') + 1 ); 
			this.masterGetDetailListFunctionName = "get" + className.substring(0,1).toUpperCase() + className.substring(1) + "s" ;
		}		
		return this.masterGetDetailListFunctionName;
	}

	public void setMasterGetDetailListFunctionName(String masterGetDetailListFunctionName) {
		this.masterGetDetailListFunctionName = masterGetDetailListFunctionName;
	}

	public String[] getSplit() {
		return split;
	}

	public void setSplit(String[] split) {
		this.split = split;
	}

	public HashMap getUiProperties() {
		return uiProperties;
	}

	public void setUiProperties(HashMap uiProperties) {
		this.uiProperties = uiProperties;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public int[] getOneRecordDetailKeys() {
		return oneRecordDetailKeys;
	}

	public void setOneRecordDetailKeys(int[] oneRecordDetailKeys) {
		this.oneRecordDetailKeys = oneRecordDetailKeys;
	}

	public List<String> getFieldNameNotWrite() {
		return fieldNameNotWrite;
	}

	public void setFieldNameNotWrite(List<String> fieldNameNotWrite) {
		this.fieldNameNotWrite = fieldNameNotWrite;
	}

	public int[] getNotImportFieldIndex() {
		return notImportFieldIndex;
	}

	public void setNotImportFieldIndex(int[] notImportFieldIndex) {
		this.notImportFieldIndex = notImportFieldIndex;
	}

	public Properties getFieldNameFormat() {
		return fieldNameFormat;
	}

	public void setFieldNameFormat(String key,String value) {
		this.fieldNameFormat.setProperty(key, value);
	}

	public int getXlsColumnLength() {
		return xlsColumnLength;
	}

	public void setXlsColumnLength(int xlsColumnLength) {
		this.xlsColumnLength = xlsColumnLength;
	}





}
