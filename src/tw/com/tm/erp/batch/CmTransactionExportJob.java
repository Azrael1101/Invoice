package tw.com.tm.erp.batch;

import java.util.HashMap;
import java.util.Map;

import tw.com.tm.erp.exportdb.CmTransactionExportData;
import tw.com.tm.erp.hbm.SpringUtils;

public class CmTransactionExportJob {
    private String customsWarehouseCode;
    
    private String opUser;

    public String getCustomsWarehouseCode() {
        return customsWarehouseCode;
    }

    public void setCustomsWarehouseCode(String customsWarehouseCode) {
        this.customsWarehouseCode = customsWarehouseCode;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }
    
    public void execute(){
	Map parameterMap = new HashMap();
	
	parameterMap.put("CUSTOMER_WAREHOUSE_CODE", customsWarehouseCode);
	parameterMap.put("OP_USER", opUser);
	
	CmTransactionExportData cmTransactionExportData = (CmTransactionExportData) SpringUtils.getApplicationContext().getBean("cmTransactionExportData");
	cmTransactionExportData.executeCmTransaction(parameterMap);
    }
}
