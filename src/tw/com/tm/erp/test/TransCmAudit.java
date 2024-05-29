package tw.com.tm.erp.test;

import java.util.HashMap;
import java.util.Map;

import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.SpringUtils;

public class TransCmAudit {

    /**
     * 轉保稅稽核
     * @param args
     */
    public static void main(String[] args) {
	CmTransactionExportData cmTransactionExportData = (CmTransactionExportData) SpringUtils.getApplicationContext().getBean("cmTransactionExportData");
	Map map = new HashMap();
	map.put("OP_USER", "SYSTEM");
	map.put("CUSTOMER_WAREHOUSE_CODE", "FD");//FD,FW
	map.put("START_DATE", "2014-12-31");  // 2010-11-12
	map.put("END_DATE", "2014-12-31");// 啟用日期~結束日期 會經過函數DateUtils.getDaysBetweenList取得轉檔日期出來
//	map.put("ORDER_TYPE_CODE","ADF");
//	map.put("DATE_TYPE","cm_cf_date");
//	map.put("ORDER_TYPE_CODE","AAF");
//	map.put("ORDER_TYPE_CODE","IBT");
//	map.put("ORDER_TYPE_CODE","EIF");
//	map.put("ORDER_TYPE_CODE","IOP");
//      map.put("ORDER_TYPE_CODE","AIF");
//	map.put("ORDER_TYPE_CODE","ESF");
//	map.put("ORDER_TYPE_CODE","WRF");
//	map.put("ORDER_TYPE_CODE","WTF");
//	map.put("ORDER_TYPE_CODE","WHF");
//	map.put("ORDER_TYPE_CODE","WFF");
//	map.put("ORDER_TYPE_CODE","AEG");
//	map.put("ORDER_TYPE_CODE","ADF");
//	map.put("ORDER_TYPE_CODE","AAF");
	map.put("ORDER_TYPE_CODE","APF");
//	map.put("ORDER_TYPE_CODE","AEF");
//	map.put("ORDER_TYPE_CODE","AIF");
//	map.put("ORDER_TYPE_CODE","MEF");
//	map.put("ORDER_TYPE_CODE","EOF");
//	map.put("ORDER_NO","201502160001");
//	map.put("DECLARATION_NO", "AW  02A9771014");
//	map.put("DECLARATION_SEQ", 21);
	cmTransactionExportData.executeCmTransaction(map);
    }
}