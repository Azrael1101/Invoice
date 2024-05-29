package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuShop entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShop implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -5403224324251860076L;
	private String shopCode;
	private String shopCName;
	private String shopEName;
	private String place;
	private String supplierCode;
	private String shopType;
	private String shopLevel;
	private String brandCode;
	private String area;
	private String system;
	private String marketArea;
	private String departmentName;
	private String incharge;
	private String contractPerson;
	private String tel;
	private Date contractBeginDate;
	private Date contractEndDate;
	private Double tsubo;
	private Long averageEmployee;
	private String shopStyle;
	private Long billDay;
	private Long invoiceDay;
	private Long collectionDay;
	private String billType;
	private Long billAmount;
	private String billTel;
	private String billAddress;
	private String shopAddress;
	private String salesBonusType;
	private Long locationId;
	private String salesWarehouseCode;
	private String enable;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<BuShopEmployee> buShopEmployees = new ArrayList(0);
	private List<BuShopTarget> buShopTarget = new ArrayList(0);
	private String guiCode;
	private Date scheduleDate;
	private String shopSalesType;
	private String migrationType;
	
	public BuShop() {

	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getShopCName() {
		return shopCName;
	}
	public void setShopCName(String shopCName) {
		this.shopCName = shopCName;
	}
	public String getShopEName() {
		return shopEName;
	}
	public void setShopEName(String shopEName) {
		this.shopEName = shopEName;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getShopType() {
		return shopType;
	}
	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	public String getShopLevel() {
		return shopLevel;
	}
	public void setShopLevel(String shopLevel) {
		this.shopLevel = shopLevel;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getMarketArea() {
		return marketArea;
	}
	public void setMarketArea(String marketArea) {
		this.marketArea = marketArea;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getIncharge() {
		return incharge;
	}
	public void setIncharge(String incharge) {
		this.incharge = incharge;
	}
	public String getContractPerson() {
		return contractPerson;
	}
	public void setContractPerson(String contractPerson) {
		this.contractPerson = contractPerson;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getContractBeginDate() {
		return contractBeginDate;
	}
	public void setContractBeginDate(Date contractBeginDate) {
		this.contractBeginDate = contractBeginDate;
	}
	public Date getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public Double getTsubo() {
		return tsubo;
	}
	public void setTsubo(Double tsubo) {
		this.tsubo = tsubo;
	}
	public Long getAverageEmployee() {
		return averageEmployee;
	}
	public void setAverageEmployee(Long averageEmployee) {
		this.averageEmployee = averageEmployee;
	}
	public String getShopStyle() {
		return shopStyle;
	}
	public void setShopStyle(String shopStyle) {
		this.shopStyle = shopStyle;
	}
	public Long getBillDay() {
		return billDay;
	}
	public void setBillDay(Long billDay) {
		this.billDay = billDay;
	}
	public Long getInvoiceDay() {
		return invoiceDay;
	}
	public void setInvoiceDay(Long invoiceDay) {
		this.invoiceDay = invoiceDay;
	}
	public Long getCollectionDay() {
		return collectionDay;
	}
	public void setCollectionDay(Long collectionDay) {
		this.collectionDay = collectionDay;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public Long getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Long billAmount) {
		this.billAmount = billAmount;
	}
	public String getBillTel() {
		return billTel;
	}
	public void setBillTel(String billTel) {
		this.billTel = billTel;
	}
	public String getBillAddress() {
		return billAddress;
	}
	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	public String getSalesBonusType() {
		return salesBonusType;
	}
	public void setSalesBonusType(String salesBonusType) {
		this.salesBonusType = salesBonusType;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getSalesWarehouseCode() {
		return salesWarehouseCode;
	}
	public void setSalesWarehouseCode(String salesWarehouseCode) {
		this.salesWarehouseCode = salesWarehouseCode;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	public String getReserve5() {
		return reserve5;
	}
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public List<BuShopEmployee> getBuShopEmployees() {
		return buShopEmployees;
	}
	public void setBuShopEmployees(List<BuShopEmployee> buShopEmployees) {
		this.buShopEmployees = buShopEmployees;
	}
	public List<BuShopTarget> getBuShopTarget() {
		return buShopTarget;
	}
	public void setBuShopTarget(List<BuShopTarget> buShopTarget) {
		this.buShopTarget = buShopTarget;
	}
	public String getGuiCode() {
		return guiCode;
	}
	public void setGuiCode(String guiCode) {
		this.guiCode = guiCode;
	}
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getShopSalesType() {
		return shopSalesType;
	}
	public void setShopSalesType(String shopSalesType) {
		this.shopSalesType = shopSalesType;
	}
	public String getMigrationType() {
		return migrationType;
	}
	public void setMigrationType(String migrationType) {
		this.migrationType = migrationType;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}	
	
	// Constructors
	

}