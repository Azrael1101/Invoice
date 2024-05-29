package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class ImStorageItem implements java.io.Serializable {

	private static final long serialVersionUID = -5539336114684617099L;
	private Long storageLineId;
	private ImStorageHead imStorageHead; 
	private String itemCode;
	private String itemCName;	// 暫存欄位
	private Double storageQuantity;
	private String storageLotNo;
    private String storageInNo;
	private String deliveryWarehouseCode;
	private String deliveryStorageCode;
	private String arrivalWarehouseCode;
	private String arrivalStorageCode;
	private String remark;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long pickLineId;

	public ImStorageItem() {
	
	}
	
	public Long getStorageLineId() {
		return storageLineId;
	}

	public void setStorageLineId(Long storageLineId) {
		this.storageLineId = storageLineId;
	}

	public ImStorageHead getImStorageHead() {
		return imStorageHead;
	}

	public void setImStorageHead(ImStorageHead imStorageHead) {
		this.imStorageHead = imStorageHead;
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

	public Double getStorageQuantity() {
		return storageQuantity;
	}

	public void setStorageQuantity(Double storageQuantity) {
		this.storageQuantity = storageQuantity;
	}

	public String getStorageLotNo() {
		return storageLotNo;
	}

	public void setStorageLotNo(String storageLotNo) {
		this.storageLotNo = storageLotNo;
	}

	public String getStorageInNo() {
		return storageInNo;
	}

	public void setStorageInNo(String storageInNo) {
		this.storageInNo = storageInNo;
	}
	
	public String getDeliveryWarehouseCode() {
		return deliveryWarehouseCode;
	}

	public void setDeliveryWarehouseCode(String deliveryWarehouseCode) {
		this.deliveryWarehouseCode = deliveryWarehouseCode;
	}

	public String getDeliveryStorageCode() {
		return deliveryStorageCode;
	}

	public void setDeliveryStorageCode(String deliveryStorageCode) {
		this.deliveryStorageCode = deliveryStorageCode;
	}

	public String getArrivalWarehouseCode() {
		return arrivalWarehouseCode;
	}

	public void setArrivalWarehouseCode(String arrivalWarehouseCode) {
		this.arrivalWarehouseCode = arrivalWarehouseCode;
	}

	public String getArrivalStorageCode() {
		return arrivalStorageCode;
	}

	public void setArrivalStorageCode(String arrivalStorageCode) {
		this.arrivalStorageCode = arrivalStorageCode;
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

	public Long getPickLineId() {
		return pickLineId;
	}

	public void setPickLineId(Long pickLineId) {
		this.pickLineId = pickLineId;
	}

}