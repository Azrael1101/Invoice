package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImInventoryCountsLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImInventoryCountsLine implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4673336613084873754L;
    // Fields
    private Long lineId;
    private ImInventoryCountsHead imInventoryCountsHead;
    private String itemCode;
    private String itemName;
    private Double onHandQty;
    private Double countsQty;
    private String description;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String status;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
    private String eanCode;
    private Double countsQtyFinal;
    
    // Constructors

    /** default constructor */
    public ImInventoryCountsLine() {
    }

    /** full constructor */
    public ImInventoryCountsLine(ImInventoryCountsHead imInventoryCountsHead, String itemCode, String itemName,
	    Double onHandQty, Double countsQty,String description, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String status, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, Long indexNo,
	    String isDeleteRecord, String isLockRecord, String message,
	    String eanCode) {
	this.imInventoryCountsHead = imInventoryCountsHead;
	this.itemCode = itemCode;
	this.itemName = itemName;
	this.onHandQty = onHandQty;
	this.countsQty = countsQty;
	this.description = description;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.status = status;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
	this.isDeleteRecord = isDeleteRecord;
	this.isLockRecord = isLockRecord;
	this.message =message;
	this.eanCode = eanCode;
    }

    // Property accessors

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public ImInventoryCountsHead getImInventoryCountsHead() {
        return imInventoryCountsHead;
    }

    public void setImInventoryCountsHead(ImInventoryCountsHead imInventoryCountsHead) {
        this.imInventoryCountsHead = imInventoryCountsHead;
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

    public Double getOnHandQty() {
        return onHandQty;
    }

    public void setOnHandQty(Double onHandQty) {
        this.onHandQty = onHandQty;
    }
    
    public Double getCountsQty() {
	return this.countsQty;
    }

    public void setCountsQty(Double countsQty) {
	this.countsQty = countsQty;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    public String getStatus() {
	return this.status;
    }

    public void setStatus(String status) {
	this.status = status;
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

	public String getEanCode() {
		return eanCode;
	}

	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}

	public Double getCountsQtyFinal() {
		return countsQtyFinal;
	}

	public void setCountsQtyFinal(Double countsQtyFinal) {
		this.countsQtyFinal = countsQtyFinal;
	}
}