package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemPrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemPrice implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7936192119947202935L;
	private Long priceId;
	private Long itemId;
	private Long priceAdjustId;
	private String brandCode;
	private String itemCode;
	private String typeCode;
	private String currencyCode;
	private Double unitPrice;
	private String isTax;
	private String taxCode;
	private Date beginDate;
	private String enable;
	private String enableName;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String salesUnit;

	private String createdByName;	// 非db欄位, 修改人員
	// Constructors

	/** default constructor */
	public ImItemPrice() {
	}

	/** minimal constructor */
	public ImItemPrice(Long priceId) {
		this.priceId = priceId;
	}

	/** full constructor */
	public ImItemPrice(Long priceId, Long priceAdjustId, String itemCode,
			String typeCode, String currencyCode, Double unitPrice, String isTax,
			String taxCode, Date beginDate, String enable,String enableName, String createdBy,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.priceId = priceId;
		this.priceAdjustId = priceAdjustId;
		this.itemCode = itemCode;
		this.typeCode = typeCode;
		this.currencyCode = currencyCode;
		this.unitPrice = unitPrice;
		this.isTax = isTax;
		this.taxCode = taxCode;
		this.beginDate = beginDate;
		this.enable = enable;
		this.enableName = enableName;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
	}

	// Property accessors

	public Long getPriceId() {
		return this.priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public Long getPriceAdjustId() {
		return this.priceAdjustId;
	}

	public void setPriceAdjustId(Long priceAdjustId) {
		this.priceAdjustId = priceAdjustId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getIsTax() {
		return this.isTax;
	}

	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getEnableName() {
		return this.enableName;
	}

	public void setEnableName(String enableName) {
		this.enableName = enableName;
	}
	
	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	
	public String getReserve3() {
		return this.reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	
	public String getReserve4() {
		return this.reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	
	public String getReserve5() {
		return this.reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getSalesUnit() {
		return this.salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

}