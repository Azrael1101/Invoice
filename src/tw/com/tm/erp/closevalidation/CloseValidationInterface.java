package tw.com.tm.erp.closevalidation;

import java.util.Date;
import java.util.List;

/**
 * 各單別關帳檢核時需實作此Interface
 * 
 * @author T15394
 * 
 */
public interface CloseValidationInterface {

    public void executeValidate(String brandCode, Date closeDate,
	    List errorMsgs, String processName, Date executeDate,
	    String processLotNo, String opUser);

}
