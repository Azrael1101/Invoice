package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemCompose entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemCompose implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -5136817754451084039L;
	private Long composeId;
	private ImItem imItem;
	private String brandCode;
	private String itemCode;
	private Long itemId;
	private String composeItemCode;
	private String composeItemName; // 組合品號中文名稱
	private String composeItemUnit;
	private Long quantity;
	private String status;
	private String remark;
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

	private String purchaseCurrencyCode; // 採購幣別
	private Double itemPrice; // 定價
	private Double itemCost; // 成本
	
	// Constructors

	/** default constructor */
	public ImItemCompose() {
	}

	/** minimal constructor */
	public ImItemCompose(Long composeId) {
		this.composeId = composeId;
	}

	/** full constructor */
	public ImItemCompose(Long composeId, ImItem imItem, String composeItemCode,
			String composeItemName, String composeItemUnit, Long quantity,
			String status, String remark, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		super();
		this.composeId = composeId;
		this.imItem = imItem;
		this.composeItemCode = composeItemCode;
		this.composeItemName = composeItemName;
		this.composeItemUnit = composeItemUnit;
		this.quantity = quantity;
		this.status = status;
		this.remark = remark;
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

	public Long getComposeId() {
		return this.composeId;
	}

	public void setComposeId(Long composeId) {
		this.composeId = composeId;
	}

	public ImItem getImItem() {
		return this.imItem;
	}

	public void setImItem(ImItem imItem) {
		this.imItem = imItem;
	}

	public String getComposeItemCode() {
		return this.composeItemCode;
	}

	public void setComposeItemCode(String composeItemCode) {
		this.composeItemCode = composeItemCode;
	}

	public String getComposeItemName() {
		return composeItemName;
	}

	public void setComposeItemName(String composeItemName) {
		this.composeItemName = composeItemName;
	}

	public String getComposeItemUnit() {
		return composeItemUnit;
	}

	public void setComposeItemUnit(String composeItemUnit) {
		this.composeItemUnit = composeItemUnit;
	}

	public Long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the purchaseCurrencyCode
	 */
	public String getPurchaseCurrencyCode() {
		return purchaseCurrencyCode;
	}

	/**
	 * @param purchaseCurrencyCode the purchaseCurrencyCode to set
	 */
	public void setPurchaseCurrencyCode(String purchaseCurrencyCode) {
		this.purchaseCurrencyCode = purchaseCurrencyCode;
	}

	/**
	 * @return the itemPrice
	 */
	public Double getItemPrice() {
		return itemPrice;
	}

	/**
	 * @param itemPrice the itemPrice to set
	 */
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	/**
	 * @return the itemCost
	 */
	public Double getItemCost() {
		return itemCost;
	}

	/**
	 * @param itemCost the itemCost to set
	 */
	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
	}

}