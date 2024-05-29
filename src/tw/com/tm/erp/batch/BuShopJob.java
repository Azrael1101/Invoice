package tw.com.tm.erp.batch;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.BuShopService;

public class BuShopJob {

    public void execute(){
	try{
	    BuShopService buShopService = (BuShopService) SpringUtils.getApplicationContext().getBean("buShopService");
	    buShopService.executeAutoChangeEnableValue();
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
