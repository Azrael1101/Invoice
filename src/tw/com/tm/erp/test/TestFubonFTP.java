package tw.com.tm.erp.test;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exportdb.FTPFubonExportData;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.importdb.FTPFubonImportData;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;

public class TestFubonFTP {

    //匯入
    public static void  testImport(){
	FTPFubonImportData fTPFubonImportData = (FTPFubonImportData) SpringUtils.getApplicationContext().getBean("fTPFubonImportData");
	Map map = new HashMap();
	map.put("FILE_NAME_DATE", "20180520");
	fTPFubonImportData.execute(map);  
    }
    
    public static void testExport(){
	FTPFubonExportData fTPFubonExportData = (FTPFubonExportData) SpringUtils.getApplicationContext().getBean("fTPFubonExportData");
	fTPFubonExportData.execute();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {

	//下載
	testImport();
	
	//上傳
	//testExport();
	
    }

}
