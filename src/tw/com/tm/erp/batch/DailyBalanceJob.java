package tw.com.tm.erp.batch;

import tw.com.tm.erp.balance.DailyBalanceManager;

public class DailyBalanceJob {
    
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

    public DailyBalanceManager getDailyBalanceManager() {
        return dailyBalanceManager;
    }

    public void setDailyBalanceManager(DailyBalanceManager dailyBalanceManager) {
        this.dailyBalanceManager = dailyBalanceManager;
    }
    
    public void execute(){
    	dailyBalanceManager.performAction(brandCode, opUser, null, null);
    }

}
