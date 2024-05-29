package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmMovementLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmMovementLine implements java.io.Serializable{

	private static final long serialVersionUID = 7665459809082660531L;
	
	// Fields
	private Long lineId;
	private CmMovementHead cmMovementHead;
	private String orderTypeCode;
	private String orderNo;
	private Long indexNo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String isDeleteRecord;
	private String isLockRecord;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String statusName;				// 額外欄位配置用
	private String deliveryWarehouseName;	// 額外欄位配置用
	private String arrivalWarehouseName;	// 額外欄位配置用
	private String imCreatedByName; 		// 額外欄位配置用
	private Date imCreationDate;			// 額外欄位配置用
	private String imLastUpdatedByName;		// 額外欄位配置用
	private Date imLastUpdateDate;			// 額外欄位配置用
	// ?箱數
	
	// Constructors
	/** default constructor */
	public CmMovementLine() {
	}

	/** full constructor */
	public CmMovementLine(Long lineId, CmMovementHead cmMovementHead,
			String orderTypeCode, String orderNo, Long indexNo,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String isDeleteRecord, String isLockRecord,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, String statusName,
			String deliveryWarehouseName, String arrivalWarehouseName,
			String imCreatedByName, Date imCreationDate,
			String imLastUpdatedByName, Date imLastUpdateDate) {
		this.lineId = lineId;
		this.cmMovementHead = cmMovementHead;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.indexNo = indexNo;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.statusName = statusName;
		this.deliveryWarehouseName = deliveryWarehouseName;
		this.arrivalWarehouseName = arrivalWarehouseName;
		this.imCreatedByName = imCreatedByName;
		this.imCreationDate = imCreationDate;
		this.imLastUpdatedByName = imLastUpdatedByName;
		this.imLastUpdateDate = imLastUpdateDate;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
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

	public CmMovementHead getCmMovementHead() {
		return cmMovementHead;
	}

	public void setCmMovementHead(CmMovementHead cmMovementHead) {
		this.cmMovementHead = cmMovementHead;
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDeliveryWarehouseName() {
		return deliveryWarehouseName;
	}

	public void setDeliveryWarehouseName(String deliveryWarehouseName) {
		this.deliveryWarehouseName = deliveryWarehouseName;
	}

	public String getArrivalWarehouseName() {
		return arrivalWarehouseName;
	}

	public void setArrivalWarehouseName(String arrivalWarehouseName) {
		this.arrivalWarehouseName = arrivalWarehouseName;
	}

	public String getImCreatedByName() {
		return imCreatedByName;
	}

	public void setImCreatedByName(String imCreatedByName) {
		this.imCreatedByName = imCreatedByName;
	}

	public Date getImCreationDate() {
		return imCreationDate;
	}

	public void setImCreationDate(Date imCreationDate) {
		this.imCreationDate = imCreationDate;
	}

	public String getImLastUpdatedByName() {
		return imLastUpdatedByName;
	}

	public void setImLastUpdatedByName(String imLastUpdatedByName) {
		this.imLastUpdatedByName = imLastUpdatedByName;
	}

	public Date getImLastUpdateDate() {
		return imLastUpdateDate;
	}

	public void setImLastUpdateDate(Date imLastUpdateDate) {
		this.imLastUpdateDate = imLastUpdateDate;
	}

}