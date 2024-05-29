package tw.com.tm.erp.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.importdb.FTPFubonImportData;

public class TxtFormatUtil {
    private static final Log log = LogFactory.getLog(StandardIEUtils.class);

    private static Properties config = new Properties();
    
    public static final String SYMBOL = "{$}";
    private static final String ROWSYMBOL = "{#}";
    
    public static final String FILE_NAMES = "_FILE_NAMES";
    
    public static final String FIELD = "_FIELD";
    public static final String OTHER = "_OTHER";
    
    public static void loadConfig(String path) {
	try {
	    config.load(TxtFormatUtil.class.getResourceAsStream(path));
	} catch (IOException ex) {
	    log.error("無法讀取TXT匯入設定檔！");
	    throw new Error("無法讀取TXT匯入設定檔！");
	}
    }
    
    public static Properties getConfig() {
	return config;
    }
    
    /**
     * 取得txt檔的配置
     * @param name
     * @return
     * @throws Exception
     */
    public static LinkedMap getTxtInfo(String name) throws Exception {

	LinkedMap map = new LinkedMap();
	
	// 取得fileNames
	String fileNames = config.getProperty(name + FILE_NAMES);
	String[] fileNamesArray = null;
	if (fileNames != null) {
	    fileNamesArray = StringTools.StringToken(fileNames, SYMBOL);
	}
	
	// 取的每個檔的配置
	for (String fileName : fileNamesArray) {
	    // 取得field\bean\起始位置資訊\結束位置資訊
	    String field = config.getProperty(fileName + FIELD);
	    String[] fieldArray = null;
	    if (field != null) {
		
		fieldArray = StringTools.StringToken(field, ROWSYMBOL);
	    }
	    
	    map.put(fileName + FIELD, fieldArray);
	    
	}
	map.put(name + FILE_NAMES , fileNamesArray);
	 
	// 其他配置塞入 
	String others = config.getProperty(name + OTHER);
	String[] othersArray = null;
	if (others != null) {
	    othersArray = StringTools.StringToken(others, SYMBOL);
	    for (String other : othersArray) { // ex:_ACCOUNT{$}_PASSWORD{$}_URL{$}_PORT{$}_DOWNLOAD_ROOT
		String otherConfig = config.getProperty(name + other);
		map.put(name + other, otherConfig);
	    }
	}
	
	return map;
    }
    
    public static void main(String args[]){
	try {
	    System.out.println("開始...");
	    
	    Map configMap = getTxtInfo("FUBON_IMPORT");
	    
//	    Iterator it = configMap.keySet().iterator();
//	    while (it.hasNext()) {
//		Object key = (Object) it.next();
//		System.out.println( key + " = " + configMap.get(key));
//	    }
	    
	    String[] fileNames = (String[])configMap.get( "FUBON_IMPORT" + TxtFormatUtil.FILE_NAMES);
	    System.out.println( "fileNames.length = " + fileNames.length);
	    for (String fileName : fileNames) {	// 每個檔案
		System.out.println( "fileName = " + fileName);
		String[] fields = (String[])configMap.get(fileName+TxtFormatUtil.FIELD);
		System.out.println("fieldRow.size = " + fields.length);
		for (String fieldRow : fields) {
		    System.out.println("fieldRow = " + fieldRow);
		}
	    }
	    System.out.println("結束...");
	} catch (Exception e) {
	    System.out.println("e = " + e.toString());
	}
    }
}
