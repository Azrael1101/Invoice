package tw.com.tm.erp.batch;

import org.springframework.context.ApplicationContext;
import tw.com.tm.erp.action.SoSalesOrderMainAction;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;

public class RealTimeUploadsJob {
	
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	public void updateSalesOrderQty() throws Exception{
		System.out.println("馬祖即時上傳海關");		
		SoSalesOrderMainService soSalesOrderMainService = (SoSalesOrderMainService) context.getBean("soSalesOrderMainService");	
		soSalesOrderMainService.updateRealTimeUploads();		
	}
	
	public void updateSalesOrderAll() throws Exception{
		System.out.println("全部即時上傳海關");		
		SoSalesOrderMainService soSalesOrderMainService = (SoSalesOrderMainService) context.getBean("soSalesOrderMainService");	
		soSalesOrderMainService.updateRealTimeUploadsAll();		
	}
}
