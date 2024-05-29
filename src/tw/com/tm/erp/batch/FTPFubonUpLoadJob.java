package tw.com.tm.erp.batch;

import tw.com.tm.erp.exportdb.FTPFubonExportData;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.importdb.FTPFubonImportData;
import tw.com.tm.erp.utils.EmployeeDataParse;

public class FTPFubonUpLoadJob {	// 富邦ftp上傳	
    public void execute(){	
	try{
	    FTPFubonExportData fTPFubonExportData = (FTPFubonExportData) SpringUtils.getApplicationContext().getBean("fTPFubonExportData");
	    fTPFubonExportData.execute();
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
