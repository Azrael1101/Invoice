package tw.com.tm.erp.batch;

import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.action.SoSalesOrderMainAction;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.ImPriceAdjustmentMainService;
import tw.com.tm.erp.hbm.service.SoParallelTestService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.action.SoParallelTestAction;

public class ResendJob {
	
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	public void updateSalesOrderQty() throws Exception{
		System.out.println("銷售重送排程啟用");		
		//SoSalesOrderMainService soSalesOrderMainService = (SoSalesOrderMainService) context.getBean("soSalesOrderMainService");	
		//soSalesOrderMainService.updateSalesOrderQty();
		SoSalesOrderMainAction soSalesOrderMainAction = (SoSalesOrderMainAction) context.getBean("soSalesOrderMainAction");
		soSalesOrderMainAction.bookDeductStockAction();
	}
	
	public void updatePriceAdjustmentPAJ() throws Exception{
		System.out.println("排程啟用變價");
		ImPriceAdjustmentMainService imPriceAdjustmentMainService = (ImPriceAdjustmentMainService) context.getBean("imPriceAdjustmentMainService");
		imPriceAdjustmentMainService.updatePriceAdjustmentPAJ();
	}
	
	public void deleteLOG()throws Exception{
		System.out.println("排程清除LOG作業");
		BuBasicDataService buBasicDataService = (BuBasicDataService) context.getBean("buBasicDataService");
		buBasicDataService.deleteLogSchedule();
	}
	
	public void copyEDI()throws Exception{
		System.out.println("排程複製EDI作業");
		BuBasicDataService buBasicDataService = (BuBasicDataService) context.getBean("buBasicDataService");
		buBasicDataService.copyEDISchedule();
	}
	
	
	public void updateSo()throws Exception{
		System.out.println("複製銷售單");
		SoParallelTestAction soParallelTestAction = (SoParallelTestAction) context.getBean("soParallelTestAction");
		soParallelTestAction.performInitial();
	}
}
