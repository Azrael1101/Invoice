package tw.com.tm.erp.hbm.dao;

import tw.com.tm.erp.hbm.SpringUtils;

public class DAOFactory {
	
	private static DAOFactory daoFactory;
	
	public static DAOFactory getInstance() {
		if(daoFactory == null) {
			daoFactory = new DAOFactory();
		}
		return daoFactory;
	}

	public BuShopDAO getBuShopDAO() {
		return BuShopDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuCurrencyDAO getBuCurrencyDAO() {
		return BuCurrencyDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuCountryDAO getBuCountryDAO() {
		return BuCountryDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	/*public BuAddressBookDAO getBuAddressBookDAO() {
		return BuAddressBookDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}*/
	
	public BuAuthruleParamaterDAO getBuAuthruleParamaterDAO() {
		return BuAuthruleParamaterDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuBranchDAO getBuBranchDAO() {
		return BuBranchDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuBrandDAO getBuBrandDAO() {
		return BuBrandDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuCommonPhraseHeadDAO getBuCommonPhraseHeadDAO() {
		return BuCommonPhraseHeadDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuCommonPhraseLineDAO getBuCommonPhraseLineDAO() {
		return BuCommonPhraseLineDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	/*public BuCustomerDAO getBuCustomerDAO() {
		return BuCustomerDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}*/
	
	public BuEmployeeDAO getBuEmployeeDAO() {
		return BuEmployeeDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuOrderTypeDAO getBuOrderTypeDAO() {
		return BuOrderTypeDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuOrderTypeApprovalDAO getBuOrderTypeApprovalDAO() {
		return BuOrderTypeApprovalDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuOrganizationDAO getBuOrganizationDAO() {
		return BuOrganizationDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	/*public BuSupplierDAO getBuSupplierDAO() {
		return BuSupplierDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}*/
	
	public FiBankDAO getFiBankDAO() {
		return FiBankDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public FiExchangeRateDAO getFiExchangeRateDAO() {
		return FiExchangeRateDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuPaymentTermDAO getBuPaymentTermDAO() {
		return BuPaymentTermDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImCategoryHeadDAO getImCategoryHeadDAO() {
		return ImCategoryHeadDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImCategoryLineDAO getImCategoryLineDAO() {
		return ImCategoryLineDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	/*public ImDeliveryHeadDAO getImDeliveryHeadDAO() {
		return ImDeliveryHeadDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImDeliveryLineDAO getImDeliveryLineDAO() {
		return ImDeliveryLineDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}*/
	

	public ImItemDAO getImItemDAO() {
		return ImItemDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
		
	public ImItemImageDAO getImItemImageDAO() {
		return ImItemImageDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImOnHandDAO getImOnHandDAO() {
		return ImOnHandDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	

	/*	使用上改由 SERVICE去做
	 * public ImWarehouseDAO ImWarehouseDAO() {
			return ImWarehouseDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
		}*/
	
	public ImWarehouseTypeDAO getImWarehouseTypeDAO() {
		return ImWarehouseTypeDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImPriceAdjustmentDAO getImPriceAdjustmentDAO() {
		return ImPriceAdjustmentDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImItemPriceViewDAO getImItemPriceViewDAO() {
		return ImItemPriceViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImItemCurrentPriceViewDAO getImItemCurrentPriceViewDAO() {
		return ImItemCurrentPriceViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImPriceListDAO getImPriceListDAO() {
		return ImPriceListDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImPromotionDAO getImPromotionDAO() {
		return ImPromotionDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
		
	public ImPromotionItemDAO getImPromotionItemDAO() {
		return ImPromotionItemDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImPromotionShopDAO getImPromotionShopDAO() {
		return ImPromotionShopDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}	
	
	public ImPromotionCustomerDAO getImPromotionCustomerDAO() {
		return ImPromotionCustomerDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImPromotionFileDAO getImPromotionFileDAO() {
		return ImPromotionFileDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	public ImOnHandViewDAO getImOnHandViewDAO() {
		return ImOnHandViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	/*public SoSalesOrderHeadDAO getSoSalesOrderHeadDAO() {
		return SoSalesOrderHeadDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public SoSalesOrderLineDAO getSoSalesOrderLineDAO() {
		return SoSalesOrderLineDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}*/
	
	public ApprovalResultDAO getApprovalResultDAO() {
		return ApprovalResultDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public ImWarehouseQuantityViewDAO getImWarehouseQuantityViewDAO() {
		return ImWarehouseQuantityViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuCustomerWithAddressViewDAO getBuCustomerWithAddressViewDAO() {
		return BuCustomerWithAddressViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuEmployeeWithAddressViewDAO getBuEmployeeWithAddressViewDAO() {
		return BuEmployeeWithAddressViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuSupplierWithAddressViewDAO getBuSupplierWithAddressViewDAO() {
		return BuSupplierWithAddressViewDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public WfApprovalResultDAO getWfApprovalResultDAO() {
		return WfApprovalResultDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}
	
	public BuEmployeeBrandDAO getBuEmployeeBrandDAO() {
		return BuEmployeeBrandDAO.getFromApplicationContext(SpringUtils.getApplicationContext());
	}

	public AdCategoryHeadDAO getAdCategoryHeadDAO() {
		// TODO Auto-generated method stub
		return AdCategoryHeadDAO.getFromApplicationContext(SpringUtils.getApplicationContext());

	}
}
