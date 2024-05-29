package tw.com.tm.erp.batch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tw.com.tm.erp.balance.DailyBalanceMainManager;
import tw.com.tm.erp.balance.DailyBalanceManager;
import tw.com.tm.erp.utils.DateUtils;

public class DailyBalanceMainJob {
	
private String brandCode;
    
    private String opUser;
    
    private DailyBalanceManager dailyBalanceManager;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }


	public void setDailyBalanceManager(DailyBalanceManager dailyBalanceManager) {
		this.dailyBalanceManager = dailyBalanceManager;
	}

	public DailyBalanceManager getDailyBalanceManager() {
        return dailyBalanceManager;
    }

   Date closeDate = new Date();
   Date exeCuteDate = DateUtils.addDays(closeDate, -1);
    
    public void execute(){
    	dailyBalanceManager.performAction("T2", "SYSTEM", null, null);
    }

	
}
