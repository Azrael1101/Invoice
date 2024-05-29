package tw.com.tm.erp.test;

import tw.com.tm.erp.batch.DailyBalanceMainJob;
import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.SpringUtils;

public class T2_BALANCE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyBalanceMainJob dbm = (DailyBalanceMainJob) SpringUtils.getApplicationContext().getBean("dailyBalanceManager"); 
		dbm.execute();
	}

}
