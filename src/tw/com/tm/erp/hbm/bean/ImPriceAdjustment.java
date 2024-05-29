package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImPriceAdjustment entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPriceAdjustment implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5564171028226576400L;
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date enableDate;
	private String supplierCode;
	private String salesPeriod;
	private String currencyCode;
	private Double exchangeRate;
	private Double ratio;
	private String description;
	private String priceType;
	private String status;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String purchaseAssist;
	private String purchaseMember;
	private String purchaseMaster;
	private String itemCategory;
	
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long processId;
	private List<ImPriceList> imPriceLists = new ArrayList(0);
	private String supplierName ;	// 暫時欄位, 非db欄位
	// Constructors

	/** default constructor */
	public ImPriceAdjustment() {
	}

	/** minimal constructor */
	public ImPriceAdjustment(Long headId, String brandCode) {
		this.headId = headId;
		this.brandCode = brandCode;
	}

	/** full constructor */
	public ImPriceAdjustment(Long headId, String brandCode,
			String orderTypeCode, String orderNo, Date enableDate,
			String supplierCode, String salesPeriod, String currencyCode,
			Double exchangeRate, Double ratio, String description,
			String priceType, String status, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List<ImPriceList> imPriceLists,String supplierName) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.enableDate = enableDate;
		this.supplierCode = supplierCode;
		this.salesPeriod = salesPeriod;
		this.currencyCode = currencyCode;
		this.exchangeRate = exchangeRate;
		this.ratio = ratio;
		this.description = description;
		this.priceType = priceType;
		this.status = status;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.imPriceLists = imPriceLists;
		this.supplierName = supplierName;
	}


	


	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrderTypeCode() {
		return this.orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getEnableDate() {
		return this.enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSalesPeriod() {
		return salesPeriod;
	}

	public void setSalesPeriod(String salesPeriod) {
		this.salesPeriod = salesPeriod;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<ImPriceList> getImPriceLists() {
		return this.imPriceLists;
	}

	public void setImPriceLists(List<ImPriceList> imPriceLists) {
		this.imPriceLists = imPriceLists;
	}

	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName) {
	    this.supplierName = supplierName;
	}

	public String getPurchaseAssist() {
		return purchaseAssist;
	}

	public void setPurchaseAssist(String purchaseAssist) {
		this.purchaseAssist = purchaseAssist;
	}

	public String getPurchaseMember() {
		return purchaseMember;
	}

	public void setPurchaseMember(String purchaseMember) {
		this.purchaseMember = purchaseMember;
	}

	public String getPurchaseMaster() {
		return purchaseMaster;
	}

	public void setPurchaseMaster(String purchaseMaster) {
		this.purchaseMaster = purchaseMaster;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public Long getProcessId() {
	    return processId;
	}

	public void setProcessId(Long processId) {
	    this.processId = processId;
	}
	
}