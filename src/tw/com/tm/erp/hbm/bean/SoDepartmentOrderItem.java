package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoSalesOrderItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDepartmentOrderItem implements java.io.Serializable {

	private Long lineId;
    private SoDepartmentOrderHead soDepartmentOrderHead;
    private Long headId;
    private Long indexNo;
    private String itemCode;
    private Double originalUnitPrice;
    private Double quantity;
    private Double originalSalesAmount;
    private Double discountRate;

	private Double discountAmount;
    private Double actualUnitPrice;
    private Double actualSalesAmount;
    private String createdBy;
    private String lastUpdatedBy;
    private String reserve1;
    private String reserve2;
    private String itemCName;
    private String isLockRecord;
    private String isDeleteRecord;
    private String depositCode;
    // Constructors

	/** default constructor */
    public SoDepartmentOrderItem() {
    }

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public SoDepartmentOrderHead getSoDepartmentOrderHead() {
		return soDepartmentOrderHead;
	}

	public void setSoDepartmentOrderHead(SoDepartmentOrderHead soDepartmentOrderHead) {
		this.soDepartmentOrderHead = soDepartmentOrderHead;
	}
	
	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getOriginalUnitPrice() {
		return originalUnitPrice;
	}

	public void setOriginalUnitPrice(Double originalUnitPrice) {
		this.originalUnitPrice = originalUnitPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getOriginalSalesAmount() {
		return originalSalesAmount;
	}

	public void setOriginalSalesAmount(Double originalSalesAmount) {
		this.originalSalesAmount = originalSalesAmount;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getActualUnitPrice() {
		return actualUnitPrice;
	}

	public void setActualUnitPrice(Double actualUnitPrice) {
		this.actualUnitPrice = actualUnitPrice;
	}

	public Double getActualSalesAmount() {
		return actualSalesAmount;
	}

	public void setActualSalesAmount(Double actualSalesAmount) {
		this.actualSalesAmount = actualSalesAmount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getDepositCode() {
		return depositCode;
	}

	public void setDepositCode(String depositCode) {
		this.depositCode = depositCode;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}



}