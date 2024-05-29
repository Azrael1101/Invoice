package tw.com.tm.erp.balance;

import java.util.Date;
import java.util.List;

/**
 * 各單別日結時需實作此Interface
 * 
 * @author T15394
 * 
 */
public interface DailyBlanceInterface {

    public String performBalance(String[] brandCodeArray, Date balanceDate,
	    List errorMsgs, String processName, String processLotNo,
	    String opUser, Date startDate, Date endDate);
    
   
}
