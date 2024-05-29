package tw.com.tm.erp.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.utils.MailUtils;

public class TxtUtil {

    private static final Log log = LogFactory.getLog(TxtUtil.class);
    
    private static final String  DELIMITER = "\r\n";

    public static void exportTxt(OutputStream os, HashMap essentialInfoMap) throws Exception {
    	
	try {
	    List assembly = (List)essentialInfoMap.get("assembly");	
	    if (assembly != null) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < assembly.size(); i++) {
		    String rowData = (String) assembly.get(i);
		    result.append(rowData);
	        result.append(DELIMITER);
		}
		os.write(result.toString().getBytes());		
	    }   
	} catch (Exception ex) {
	    log.error("匯出TXT檔失敗！原因：" + ex.toString());
	    throw ex;
	}
    }
    
    public static void exportTxt(String exportFileRootPath, List<String> records) 
            throws Exception{
	
	FileWriter fw = null;
	try{
	    File file = new File(exportFileRootPath);
	    File parentFile = file.getParentFile();
	    if(!parentFile.exists()){
                FileUtils.forceMkdir(parentFile);
	    }
	    fw = new FileWriter(file);
	    for(String record : records){
		fw.write(record + DELIMITER);
	    }
	    fw.flush();
	}catch (Exception ex) {
	    log.error("匯出TXT檔失敗！原因：" + ex.toString());
	    MailUtils.sendMail("下傳文字檔失敗", "下傳失敗<br>"+exportFileRootPath, "ITMA-DG@tasameng.com.tw");
	    throw ex;
	}finally{
	    try {
		if(fw != null)
	            fw.close();
	    } catch (Exception ignore) {
		
	    }
	}
    }
    
    public static void exportTxt(String exportFileRootPath, List<String> records, boolean isAppend) throws Exception{

	FileWriter fw = null;
	try{
	    File file = new File(exportFileRootPath);
	    File parentFile = file.getParentFile();
	    if(!parentFile.exists()){
		FileUtils.forceMkdir(parentFile);
	    }
	    fw = new FileWriter(file,isAppend);
	    for(String record : records){
		fw.write(record + DELIMITER);
	    }
	    fw.flush();
	}catch (Exception ex) {
	    log.error("匯出TXT檔失敗！原因：" + ex.toString());
	    throw ex;
	}finally{
	    try {
		if(fw != null)
		    fw.close();
	    } catch (Exception ignore) {

	    }
	}
    }
}
