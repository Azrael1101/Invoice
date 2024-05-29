package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImStorageItemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPickItem implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5539336114684617099L;
	private Long lineId;
	private ImPickHead imPickHead; 
	private String itemCode;
	private String itemCName;
	private Double blockQuantity;
	private Double commitQuantity;
    private String storageInNo;
	private String storageLotNo;
	private String warehouseCode;
	private String storageCode;
	private String remark;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Double arrivalWarehouse1;
	private Double arrivalWarehouse2;
	private Double arrivalWarehouse3;
	private Double arrivalWarehouse4;
	private Double arrivalWarehouse5;
	private Double arrivalWarehouse6;
	private Double arrivalWarehouse7;
	private Double arrivalWarehouse8;
	private Double arrivalWarehouse9;
	private Double arrivalWarehouse10;
	
	// Constructors

	/** default constructor */
	public ImPickItem() {
	}

	// Property accessors
	
	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImPickHead getImPickHead() {
		return imPickHead;
	}

	public void setImPickHead(ImPickHead imPickHead) {
		this.imPickHead = imPickHead;
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

	public Double getBlockQuantity() {
		return blockQuantity;
	}

	public void setBlockQuantity(Double blockQuantity) {
		this.blockQuantity = blockQuantity;
	}

	public Double getCommitQuantity() {
		return commitQuantity;
	}

	public void setCommitQuantity(Double commitQuantity) {
		this.commitQuantity = commitQuantity;
	}

	public String getStorageInNo() {
		return storageInNo;
	}

	public void setStorageInNo(String storageInNo) {
		this.storageInNo = storageInNo;
	}

	public String getStorageLotNo() {
		return storageLotNo;
	}

	public void setStorageLotNo(String storageLotNo) {
		this.storageLotNo = storageLotNo;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getStorageCode() {
		return storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getIndexNo() {
		return indexNo;
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

	public Double getArrivalWarehouse1() {
		return arrivalWarehouse1;
	}

	public void setArrivalWarehouse1(Double arrivalWarehouse1) {
		this.arrivalWarehouse1 = arrivalWarehouse1;
	}

	public Double getArrivalWarehouse2() {
		return arrivalWarehouse2;
	}

	public void setArrivalWarehouse2(Double arrivalWarehouse2) {
		this.arrivalWarehouse2 = arrivalWarehouse2;
	}

	public Double getArrivalWarehouse3() {
		return arrivalWarehouse3;
	}

	public void setArrivalWarehouse3(Double arrivalWarehouse3) {
		this.arrivalWarehouse3 = arrivalWarehouse3;
	}

	public Double getArrivalWarehouse4() {
		return arrivalWarehouse4;
	}

	public void setArrivalWarehouse4(Double arrivalWarehouse4) {
		this.arrivalWarehouse4 = arrivalWarehouse4;
	}

	public Double getArrivalWarehouse5() {
		return arrivalWarehouse5;
	}

	public void setArrivalWarehouse5(Double arrivalWarehouse5) {
		this.arrivalWarehouse5 = arrivalWarehouse5;
	}

	public Double getArrivalWarehouse6() {
		return arrivalWarehouse6;
	}

	public void setArrivalWarehouse6(Double arrivalWarehouse6) {
		this.arrivalWarehouse6 = arrivalWarehouse6;
	}

	public Double getArrivalWarehouse7() {
		return arrivalWarehouse7;
	}

	public void setArrivalWarehouse7(Double arrivalWarehouse7) {
		this.arrivalWarehouse7 = arrivalWarehouse7;
	}

	public Double getArrivalWarehouse8() {
		return arrivalWarehouse8;
	}

	public void setArrivalWarehouse8(Double arrivalWarehouse8) {
		this.arrivalWarehouse8 = arrivalWarehouse8;
	}

	public Double getArrivalWarehouse9() {
		return arrivalWarehouse9;
	}

	public void setArrivalWarehouse9(Double arrivalWarehouse9) {
		this.arrivalWarehouse9 = arrivalWarehouse9;
	}

	public Double getArrivalWarehouse10() {
		return arrivalWarehouse10;
	}

	public void setArrivalWarehouse10(Double arrivalWarehouse10) {
		this.arrivalWarehouse10 = arrivalWarehouse10;
	}
}