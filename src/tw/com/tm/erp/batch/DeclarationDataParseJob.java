package tw.com.tm.erp.batch;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.service.ImAdjustmentHeadService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.utils.DeclarationDataParse;

public class DeclarationDataParseJob {
    
    public void execute(){
	try{
	    	System.out.println("DeclarationDataParseJob execute");
		//ImReceiveHeadMainService service = (ImReceiveHeadMainService) SpringUtils.getApplicationContext().getBean("imReceiveHeadMainService");
		//ImReceiveHead head = service.findById(1141010+0L);
		//ImReceiveHeadMainService.startProcess(head);
		//SoSalesOrderHead soSalesOrderHead = soSalesOrderMainService.findById(18157811+0L);
		//SoSalesOrderMainService.startProcess(soSalesOrderHead);
		//soSalesOrderHead = soSalesOrderMainService.findById(18114525+0L);
		//SoSalesOrderMainService.startProcess(soSalesOrderHead);
		//soSalesOrderHead = soSalesOrderMainService.findById(18114526+0L);
		//SoSalesOrderMainService.startProcess(soSalesOrderHead);
	    	DeclarationDataParse declarationDataParse = (DeclarationDataParse) SpringUtils.getApplicationContext().getBean("declarationDataParse");
	    	declarationDataParse.execute();
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
