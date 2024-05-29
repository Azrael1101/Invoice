package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionItem implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 4586392534106745480L;
	private Long lineId;
	private ImPromotion imPromotion;
	private String itemCode;
	private String category02;	// 非DB欄位 中類
	private String category02Name;	// 非DB欄位 中類名稱
	private Long priceId;
	private String itemName;
	private String salesUnit;
	private Double standardPurchaseCost;
	private Double originalPrice;
	private String discountType;
	private Double discount;
	private Double discountAmount;
	private Double discountPercentage;
	private Double totalDiscountAmount;
	private Double totalDiscountPercentage;
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
	private Double quantity;
	private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
	private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
	private String message; // line 訊息的顯示
	private Double stockOnHandQty;
	private Double margenBeforeDisc; //非DB欄位
	private Double margenAfterDisc; //非DB欄位

	// Constructors
	/** default constructor */
	public ImPromotionItem() {
	}

	/** minimal constructor */
	public ImPromotionItem(Long lineId) {
		this.lineId = lineId;
	}

	public ImPromotionItem(Long lineId, ImPromotion imPromotion,
			String itemCode, Long priceId, String itemName, String salesUnit,
			Double originalPrice, String discountType, Double discount,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, Long indexNo,
			Double quantity, String isDeleteRecord, String isLockRecord, String message, Double stockOnHandQty) {
		this.lineId = lineId;
		this.imPromotion = imPromotion;
		this.itemCode = itemCode;
		this.priceId = priceId;
		this.itemName = itemName;
		this.salesUnit = salesUnit;
		this.originalPrice = originalPrice;
		this.discountType = discountType;
		this.discount = discount;
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
		this.quantity = quantity;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
		this.stockOnHandQty = stockOnHandQty;
	}

	/** full constructor */
	

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImPromotion getImPromotion() {
		return this.imPromotion;
	}

	public void setImPromotion(ImPromotion imPromotion) {
		this.imPromotion = imPromotion;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
		
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getPriceId() {
		return this.priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	
	public String getSalesUnit() {
		return this.salesUnit;
	}

	public Double getStandardPurchaseCost() {
		return standardPurchaseCost;
	}

	public void setStandardPurchaseCost(Double standardPurchaseCost) {
		this.standardPurchaseCost = standardPurchaseCost;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public Double getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDiscountType() {
		return this.discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getTotalDiscountPercentage() {
		return totalDiscountPercentage;
	}

	public void setTotalDiscountPercentage(Double totalDiscountPercentage) {
		this.totalDiscountPercentage = totalDiscountPercentage;
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
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCategory02() {
	    return category02;
	}

	public void setCategory02(String category02) {
	    this.category02 = category02;
	}

	public String getCategory02Name() {
	    return category02Name;
	}

	public void setCategory02Name(String category02Name) {
	    this.category02Name = category02Name;
	}

	public void setStockOnHandQty(Double stockOnHandQty) {
		this.stockOnHandQty = stockOnHandQty;
	}

	public Double getStockOnHandQty() {
		return stockOnHandQty;
	}
	
	public void setMargenBeforeDisc(Double margenBeforeDisc){
		this.margenBeforeDisc = margenBeforeDisc;
	}
	
	public Double getMargenBeforeDisc(){
		return margenBeforeDisc;
	}
	
	public void setMargenAfterDisc(Double margenAfterDisc){
		this.margenAfterDisc = margenAfterDisc;
	}
	
	public Double getMargenAfterDisc(){
		return margenAfterDisc;
	}
}