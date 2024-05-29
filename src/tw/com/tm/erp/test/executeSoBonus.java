package tw.com.tm.erp.test;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.SoBonusService;

public class executeSoBonus {
	
	public static void main(String[] args) throws Exception {
		SoBonusService soBonusService = (SoBonusService) SpringUtils.getApplicationContext().getBean("soBonusService");
	
		String startDatte = "2014/01/01";
		String endDate = "2014/01/31";
		
		//soBonusService.calculateBonus(startDatte, endDate);
		
	}

}
