package tw.com.tm.erp.batch;

import java.util.HashMap;
import java.util.Map;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.importdb.FTPFubonImportData;

public class FTPFubonDownLoadJob { // 富邦ftp下載
    public void execute(){
	try{
	    FTPFubonImportData fTPFubonImportData = (FTPFubonImportData) SpringUtils.getApplicationContext().getBean("fTPFubonImportData");
	    Map map = new HashMap();
	    fTPFubonImportData.execute(map);
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
