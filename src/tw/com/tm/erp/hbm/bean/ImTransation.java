package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImTransation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImTransation implements java.io.Serializable {

    // Fields

    private Long transationId;
    private String brandCode;
    private Date transationDate;
    private String orderTypeCode;
    private String orderNo;
    private Long lineId;
    private String itemCode;
    private String warehouseCode;
    private String lotNo;
    private Double quantity;
    private String reverseWarehouseCode;
    private Double averageUnitCost;
    private Double costAmount;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdatedDate;
    
    private String declarationNo;
    private Date   declarationDate;
    private String originalDeclarationNo;	// 原報單單號
    private Long   originalDeclarationSeq;	// 原報單序號
    private Date   originalDeclarationDate;	// 原報單日期
    private String originalDeclarationItem;	// 
    private String cmMovementNo;
    private Double originalPriceAmount;
    private String affectCost;
    private String declarationType;        
    private String originalDeclarationType;
    private String adjustmentType;           
    private Double orderAmount; 
    private String customerPoNo;



    // Constructors

    public Date getDeclarationDate() {
        return declarationDate;
    }

    public void setDeclarationDate(Date declarationDate) {
        this.declarationDate = declarationDate;
    }

    public String getCmMovementNo() {
        return cmMovementNo;
    }

    public void setCmMovementNo(String cmMovementNo) {
        this.cmMovementNo = cmMovementNo;
    }

    /** default constructor */
    public ImTransation() {
    }

    /** full constructor */
    public ImTransation(String brandCode, Date transationDate,
	    String orderTypeCode, String orderNo, Long lineId, String itemCode,
	    String warehouseCode, String lotNo, Double quantity,
	    String reverseWarehouseCode, Double averageUnitCost,
	    Double costAmount, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdatedDate, String customerPoNo) {
	this.brandCode = brandCode;
	this.transationDate = transationDate;
	this.orderTypeCode = orderTypeCode;
	this.orderNo = orderNo;
	this.lineId = lineId;
	this.itemCode = itemCode;
	this.warehouseCode = warehouseCode;
	this.lotNo = lotNo;
	this.quantity = quantity;
	this.reverseWarehouseCode = reverseWarehouseCode;
	this.averageUnitCost = averageUnitCost;
	this.costAmount = costAmount;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdatedDate = lastUpdatedDate;
	this.customerPoNo = customerPoNo;
    }

    // Property accessors

    public Long getTransationId() {
	return this.transationId;
    }

    public void setTransationId(Long transationId) {
	this.transationId = transationId;
    }

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public Date getTransationDate() {
	return this.transationDate;
    }

    public void setTransationDate(Date transationDate) {
	this.transationDate = transationDate;
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

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public String getLotNo() {
	return this.lotNo;
    }

    public void setLotNo(String lotNo) {
	this.lotNo = lotNo;
    }

    public Double getQuantity() {
	return this.quantity;
    }

    public void setQuantity(Double quantity) {
	this.quantity = quantity;
    }

    public String getReverseWarehouseCode() {
	return this.reverseWarehouseCode;
    }

    public void setReverseWarehouseCode(String reverseWarehouseCode) {
	this.reverseWarehouseCode = reverseWarehouseCode;
    }

    public Double getAverageUnitCost() {
	return this.averageUnitCost;
    }

    public void setAverageUnitCost(Double averageUnitCost) {
	this.averageUnitCost = averageUnitCost;
    }

    public Double getCostAmount() {
	return this.costAmount;
    }

    public void setCostAmount(Double costAmount) {
	this.costAmount = costAmount;
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

    public Date getLastUpdatedDate() {
	return this.lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
	this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getDeclarationNo() {
        return declarationNo;
    }

    public void setDeclarationNo(String declarationNo) {
        this.declarationNo = declarationNo;
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

    public Date getOriginalDeclarationDate() {
        return originalDeclarationDate;
    }

    public void setOriginalDeclarationDate(Date originalDeclarationDate) {
        this.originalDeclarationDate = originalDeclarationDate;
    }

    public String getOriginalDeclarationItem() {
        return originalDeclarationItem;
    }

    public void setOriginalDeclarationItem(String originalDeclarationItem) {
        this.originalDeclarationItem = originalDeclarationItem;
    }

    public Double getOriginalPriceAmount() {
        return originalPriceAmount;
    }

    public void setOriginalPriceAmount(Double originalPriceAmount) {
        this.originalPriceAmount = originalPriceAmount;
    }

	public String getAffectCost() {
		return affectCost;
	}

	public void setAffectCost(String affectCost) {
		this.affectCost = affectCost;
	}

	public String getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType(String declarationType) {
		this.declarationType = declarationType;
	}

	public String getOriginalDeclarationType() {
		return originalDeclarationType;
	}

	public void setOriginalDeclarationType(String originalDeclarationType) {
		this.originalDeclarationType = originalDeclarationType;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public Double getOrderAmount() {
	    return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
	    this.orderAmount = orderAmount;
	}

	public String getCustomerPoNo() {
	    return customerPoNo;
	}

	public void setCustomerPoNo(String customerPoNo) {
	    this.customerPoNo = customerPoNo;
	}

}