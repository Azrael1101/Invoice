package tw.com.tm.erp.hbm.dao;

import tw.com.tm.erp.hbm.bean.ImReplenishBasicParameter;

public class ImReplenishHeadDAO extends BaseDAO{
    
    /**
     * 依品牌,參數類別,參數查詢出一筆補貨係數配置檔
     * @param brandCode
     * @param type
     * @param parameter
     * @return
     */
    public ImReplenishBasicParameter findOneParameter(String brandCode, String type, Double parameter ){
	return (ImReplenishBasicParameter)this.findFirstByProperty("ImReplenishBasicParameter", 
		"and id.brandCode = ? and id.type = ? and id.parameter = ? ", 
		new Object[] { brandCode, type , parameter });
    }
}
