package tw.com.tm.erp.test;

import java.text.SimpleDateFormat;

import tw.com.tm.erp.batch.DeclarationDataParseJob;
import tw.com.tm.erp.batch.POSExportJob;
import tw.com.tm.erp.exportdb.POSExportData;
import tw.com.tm.erp.utils.DateUtils;

//exportData to POSServer
public class Export2PosPluchg01 {

    private static SimpleDateFormat dateFromat = new SimpleDateFormat(
	    "yy/MM/dd");

    /**
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {

	try {	    
//	    POSExportData export = new POSExportData();
	    System.out.println("============start+++++++++++++++++++++++++++");
	    // 商品匯出 D:\POS\T2
//	    export.doPOSExportData(0, "T2", DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, "20200821"), DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, "20200823"), "T96085"); //DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, "20130822")
	    POSExportJob job = new POSExportJob();
	    job.setBrandCode("T2");
	    job.setFunctionCode(98);
	    job.setOpUser("SYSTEM");
	    job.setCustomsWarehouseCode("KD");
	    //job.executeIslands();
	    
	    System.out.println("============end+++++++++++++++++++++++++++");
	    System.exit(0);
	} catch (Exception ex) {
	    System.out.println(ex.toString());
	}
    }
}