package tw.com.tm.erp.batch;

import java.util.HashMap;
import java.util.Map;

import tw.com.tm.erp.exportdb.CheckExportData;
import tw.com.tm.erp.hbm.SpringUtils;

public class CheckDataJob {
    private String opUser;

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }
    
    public void execute(){
	Map parameterMap = new HashMap();
	
	parameterMap.put("OP_USER", opUser);
	
	CheckExportData checkExportData = (CheckExportData) SpringUtils.getApplicationContext().getBean("checkExportData");
	checkExportData.execute();
    }
}
