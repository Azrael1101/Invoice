package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImStorageHead implements java.io.Serializable {

	private static final long serialVersionUID = 4631432349273021627L;
	private Long storageHeadId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private String storageTransactionType;
	private Date storageTransactionDate;
	private String deliveryWarehouseCode;
	private String arrivalWarehouseCode;
	private String sourceOrderTypeCode;
	private String sourceOrderNo;
	private String status;
	private String description;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long processId;
	private List<ImStorageItem> imStorageItems = new ArrayList();
	
	public ImStorageHead() {
	
	}

	public ImStorageHead(Long storageHeadId) {
		this.storageHeadId = storageHeadId;
	}
	
	public Long getStorageHeadId() {
		return storageHeadId;
	}

	public void setStorageHeadId(Long storageHeadId) {
		this.storageHeadId = storageHeadId;
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

	public String getStorageTransactionType() {
		return storageTransactionType;
	}

	public void setStorageTransactionType(String storageTransactionType) {
		this.storageTransactionType = storageTransactionType;
	}
	
	public Date getStorageTransactionDate() {
		return this.storageTransactionDate;
	}

	public void setStorageTransactionDate(Date storageTransactionDate) {
		this.storageTransactionDate = storageTransactionDate;
	}

	public String getDeliveryWarehouseCode() {
		return deliveryWarehouseCode;
	}

	public void setDeliveryWarehouseCode(String deliveryWarehouseCode) {
		this.deliveryWarehouseCode = deliveryWarehouseCode;
	}

	public String getArrivalWarehouseCode() {
		return arrivalWarehouseCode;
	}

	public void setArrivalWarehouseCode(String arrivalWarehouseCode) {
		this.arrivalWarehouseCode = arrivalWarehouseCode;
	}
	
	public String getSourceOrderTypeCode() {
		return this.sourceOrderTypeCode;
	}

	public void setSourceOrderTypeCode(String sourceOrderTypeCode) {
		this.sourceOrderTypeCode = sourceOrderTypeCode;
	}

	public String getSourceOrderNo() {
		return this.sourceOrderNo;
	}

	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public List<ImStorageItem> getImStorageItems() {
		return imStorageItems;
	}

	public void setImStorageItems(List<ImStorageItem> imStorageItems) {
		this.imStorageItems = imStorageItems;
	}

}