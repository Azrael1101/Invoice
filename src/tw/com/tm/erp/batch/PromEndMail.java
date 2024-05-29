package tw.com.tm.erp.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.service.ImPromotionMainService;

public class PromEndMail {
	 public void execute() throws Exception{
		 try{
		 	Date endDate = new Date();
			String subject = null;
			String templateFileName =null;
			Map displayHteml = new HashMap();
			List mailAddress = new ArrayList();
			List attachFiles = new ArrayList();
			Map cidMap = new HashMap();
				
			ImPromotionMainService imPromotionMainService = (ImPromotionMainService) SpringUtils.getApplicationContext().getBean("imPromotionMainService");
			imPromotionMainService.getMailListbeforeSteve(endDate, subject, templateFileName, displayHteml, mailAddress, attachFiles, cidMap);
		 }catch(Exception ex){
				System.out.println(ex.toString());
			}
		 }
}
