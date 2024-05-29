package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoDeliveryInventoryLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryInventoryLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5887654428676107761L;
	private Long lineId;
	private Long headId;
	private String deliveryOrderType;
	private String deliveryOrderNo;
	private String storageCodeSys;
	private Long bagCountsSys;
	private String storageCode;
	private Long bagCounts;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdateBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String lastUpdateByName;

	// Constructors

	/** default constructor */
	public SoDeliveryInventoryLine() {
	}

	/** minimal constructor */
	public SoDeliveryInventoryLine(Long headId) {
		this.headId = headId;
	}

	/** full constructor */
	public SoDeliveryInventoryLine(Long headId, String deliveryOrderType,
			String deliveryOrderNo, String storageCodeSys, Long bagCountsSys,
			String storageCode, Long bagCounts, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdateBy,
			Date lastUpdateDate, Long indexNo, String isDeleteRecord,
			String isLockRecord, String message) {
		this.headId = headId;
		this.deliveryOrderType = deliveryOrderType;
		this.deliveryOrderNo = deliveryOrderNo;
		this.storageCodeSys = storageCodeSys;
		this.bagCountsSys = bagCountsSys;
		this.storageCode = storageCode;
		this.bagCounts = bagCounts;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdateBy = lastUpdateBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getDeliveryOrderType() {
		return this.deliveryOrderType;
	}

	public void setDeliveryOrderType(String deliveryOrderType) {
		this.deliveryOrderType = deliveryOrderType;
	}

	public String getDeliveryOrderNo() {
		return this.deliveryOrderNo;
	}

	public void setDeliveryOrderNo(String deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
	}

	public String getStorageCodeSys() {
		return this.storageCodeSys;
	}

	public void setStorageCodeSys(String storageCodeSys) {
		this.storageCodeSys = storageCodeSys;
	}

	public Long getBagCountsSys() {
		return this.bagCountsSys;
	}

	public void setBagCountsSys(Long bagCountsSys) {
		this.bagCountsSys = bagCountsSys;
	}

	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public Long getBagCounts() {
		return this.bagCounts;
	}

	public void setBagCounts(Long bagCounts) {
		this.bagCounts = bagCounts;
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

	public String getLastUpdateBy() {
		return this.lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
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
		return this.isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return this.isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getLastUpdateByName(){
		return lastUpdateByName;
	}
	
	public void setLastUpdateByName(String lastUpdate){
		lastUpdateByName = lastUpdate;
	}
}