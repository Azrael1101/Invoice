package tw.com.tm.erp.hbm.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;

public class MonthlyBalanceService {
    
    private static final Log log = LogFactory.getLog(MonthlyBalanceService.class);
    
    private BuBrandService buBrandService;
    
    public void setBuBrandService(BuBrandService buBrandService) {
	this.buBrandService = buBrandService;
    }
    

    
    /**
     * 執行月結
     * 
     * @param brandCode
     * @param year
     * @param month
     * @param opUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String performAction(String brandCode, String year, String month, String opUser) throws FormException, Exception{
	
	//執行月結時先將bu_brand中欲關帳的brand，其MONTHLY_BALANCING設為Y
	String returnMessage = "";
        try{
            doValidate(year, month);       
            month =  CommonUtils.insertCharacterWithFixLength(month, 2, CommonUtils.ZERO);
            
            return returnMessage = "月結作業存檔成功！";
        }catch (FormException fe) {
	    log.error("執行月結作業失敗，原因：" + fe.toString());
	    returnMessage = "執行月結作業失敗，原因：" + fe.getMessage();
	    throw new FormException(returnMessage);
	}catch(Exception ex){
            log.error("執行月結作業時發生錯誤，原因：" + ex.toString());
            returnMessage = "執行月結作業時發生錯誤，原因：" + ex.getMessage();
            throw new Exception(returnMessage);
        }finally{
           //TODO:將工作紀錄到資料庫(returnMessage)、將bu_brand中欲月結的brand，其MONTHLY_BALANCING設為N
        }	
    }
    
    private void doValidate(String year, String month) throws FormException, Exception{
        /*
          1.月結月份不可大於currentMonth
          2.檢核執行月結的月份是否已完成關帳
        */
    }

}
