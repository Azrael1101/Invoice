package tw.com.tm.erp.hbm.service;

import tw.com.tm.erp.hbm.SpringUtils;

public class ServiceFactory {
	
	private static ServiceFactory serviceFactory;
	
	public static ServiceFactory getInstance() {
		if(serviceFactory == null) {
			serviceFactory = new ServiceFactory();
		}
		return serviceFactory;
	}
	
	public SoSalesOrderService getSoSalesOrderService() {
		return (SoSalesOrderService)SpringUtils.getApplicationContext().getBean("soSalesOrderService");
	}
	
	public BuCommonPhraseService getBuCommonPhraseService() {
		return (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
	}
	public BuBasicDataService getBuBasicDataService() {
		return (BuBasicDataService)SpringUtils.getApplicationContext().getBean("buBasicDataService");
	}
/*	public ImDeliveryService getImDeliveryService() {
		return (ImDeliveryService)SpringUtils.getApplicationContext().getBean("imDeliveryService");
	}*/
	public ImPriceAdjustmentService getImPriceAdjustmentService() {
		return (ImPriceAdjustmentService)SpringUtils.getApplicationContext().getBean("imPriceAdjustmentService");
	}
	public ImItemPriceService getImItemPriceService() {
		return (ImItemPriceService)SpringUtils.getApplicationContext().getBean("imItemPriceService");
	}
	public ImItemService getImItemService() {
		return (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
	}
	public ImOnHandService getImOnHandService() {
		return (ImOnHandService)SpringUtils.getApplicationContext().getBean("imOnHandService");
	}
	public ImPromotionService getImPromotionService() {
		return (ImPromotionService)SpringUtils.getApplicationContext().getBean("imPromotionService");
	}
	public WfApprovalResultService getWfApprovalResultService() {
		return (WfApprovalResultService)SpringUtils.getApplicationContext().getBean("wfApprovalResultService");
	} 
	public BuOrderTypeService getBuOrderTypeService() {
		return (BuOrderTypeService)SpringUtils.getApplicationContext().getBean("buOrderTypeService");
	}
}
