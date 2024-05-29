package tw.com.tm.erp.batch;

import java.util.Date;

import tw.com.tm.erp.close.DailyCloseMainManager;
import tw.com.tm.erp.utils.DateUtils;



public class DailyCloseMainJob {

	private String brandCode;
    
    private String opUser;
    
    private DailyCloseMainManager dailyCloseMainManager;

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

	public DailyCloseMainManager getDailyCloseMainManager() {
		return dailyCloseMainManager;
	}

	public void setDailyCloseMainManager(DailyCloseMainManager dailyCloseMainManager) {
		this.dailyCloseMainManager = dailyCloseMainManager;
	}
    
	Date closeDate = new Date();
	Date exeCuteDate = DateUtils.addDays(closeDate, -1);
	    
	public void execute(){

	  	dailyCloseMainManager.performActionMain("T2", null,"" ,"SYSTEM","Y","");

	 }
}
