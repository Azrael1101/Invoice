package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * FiInvoiceLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FiInvoiceLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4043721749617951427L;
	private Long lineId;
	private FiInvoiceHead fiInvoiceHead;
	private String brandCode;
	private Long poPurchaseOrderHeadId ;
	private String purchaseOrderType;
	private String purchaseOrderNo; //這個不是DB欄位,考慮DB加上FIELD
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
	private Long poPurchaseOrderLineId;
	private Double quantity;
	private Double foreignAmount;
	private Double localAmount;
	private Double originalForeignAmount;
	private Double originalLocalAmount;
	private Double foreignUnitPrice;
	private Double localUnitPrice;
	private String originalDeclarationNo;
	private Long originalDeclarationSeq;
	private String shippingMark;
	private Double weight;
	private String unit;
	private String itemCode;
	private String itemCName;
	private String sourceOrderTypeCode;
	private String sourceOrderNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private Long   customSeq;
	private String remark;
	private String supplierItemCode;
	private String purchaseUnit;
	private String category15;
	private String specWeight;
	
	
	// Constructors

	public String getRemark() {
	    return remark;
	}

	public void setRemark(String remark) {
	    this.remark = remark;
	}

	/** default constructor */
	public FiInvoiceLine() {
	}

	/** minimal constructor */
	public FiInvoiceLine(Long lineId) {
		this.lineId = lineId;
	}

	

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public FiInvoiceHead getFiInvoiceHead() {
		return this.fiInvoiceHead;
	}

	public void setFiInvoiceHead(FiInvoiceHead fiInvoiceHead) {
		this.fiInvoiceHead = fiInvoiceHead;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getPurchaseOrderType() {
		return this.purchaseOrderType;
	}

	public void setPurchaseOrderType(String purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}

	public String getPurchaseOrderNo() {
		return this.purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
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

	public Long getPoPurchaseOrderHeadId() {
		return poPurchaseOrderHeadId;
	}

	public void setPoPurchaseOrderHeadId(Long poPurchaseOrderHeadId) {
		this.poPurchaseOrderHeadId = poPurchaseOrderHeadId;
	}

	public Long getPoPurchaseOrderLineId() {
		return poPurchaseOrderLineId;
	}

	public void setPoPurchaseOrderLineId(Long poPurchaseOrderLineId) {
		this.poPurchaseOrderLineId = poPurchaseOrderLineId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getForeignAmount() {
		return foreignAmount;
	}

	public void setForeignAmount(Double foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public Double getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}

	public Double getOriginalForeignAmount() {
		return originalForeignAmount;
	}

	public void setOriginalForeignAmount(Double originalForeignAmount) {
		this.originalForeignAmount = originalForeignAmount;
	}

	public Double getOriginalLocalAmount() {
		return originalLocalAmount;
	}

	public void setOriginalLocalAmount(Double originalLocalAmount) {
		this.originalLocalAmount = originalLocalAmount;
	}

	public Double getForeignUnitPrice() {
		return foreignUnitPrice;
	}

	public void setForeignUnitPrice(Double foreignUnitPrice) {
		this.foreignUnitPrice = foreignUnitPrice;
	}

	public Double getLocalUnitPrice() {
		return localUnitPrice;
	}

	public void setLocalUnitPrice(Double localUnitPrice) {
		this.localUnitPrice = localUnitPrice;
	}

	public String getOriginalDeclarationNo() {
		return originalDeclarationNo;
	}

	public void setOriginalDeclarationNo(String originalDeclarationNo) {
		this.originalDeclarationNo = originalDeclarationNo;
	}

	public Long getOriginalDeclarationSeq() {
		return originalDeclarationSeq;
	}

	public void setOriginalDeclarationSeq(Long originalDeclarationSeq) {
		this.originalDeclarationSeq = originalDeclarationSeq;
	}

	public String getShippingMark() {
		return shippingMark;
	}

	public void setShippingMark(String shippingMark) {
		this.shippingMark = shippingMark;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getItemCName() {
	    return itemCName;
	}

	public void setItemCName(String itemCName) {
	    this.itemCName = itemCName;
	}

	public String getSourceOrderTypeCode() {
		return sourceOrderTypeCode;
	}

	public void setSourceOrderTypeCode(String sourceOrderTypeCode) {
		this.sourceOrderTypeCode = sourceOrderTypeCode;
	}

	public String getSourceOrderNo() {
		return sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
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

	public Long getCustomSeq() {
	    return customSeq;
	}

	public void setCustomSeq(Long customSeq) {
	    this.customSeq = customSeq;
	}

	public String getSupplierItemCode() {
	    return supplierItemCode;
	}

	public void setSupplierItemCode(String supplierItemCode) {
	    this.supplierItemCode = supplierItemCode;
	}

	public String getPurchaseUnit() {
	    return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
	    this.purchaseUnit = purchaseUnit;
	}

	public String getCategory15() {
		return category15;
	}

	public void setCategory15(String category15) {
		this.category15 = category15;
	}

	public String getSpecWeight() {
		return specWeight;
	}

	public void setSpecWeight(String specWeight) {
		this.specWeight = specWeight;
	}

}