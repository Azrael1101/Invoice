package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoDeliveryLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1224394755101177104L;
	private Long lineId;
	private SoDeliveryHead soDeliveryHead;
	private Long salesOrderId;
	private String posMachineCode;
	private Date salesOrderDate;
	private String customerPoNo;
	private String superintendentCode;
	private String superintendentName;
	private String countryCode;
	private String breakable;
	private String valuable;
	private String remark1;
	private String remare2;
	private String status;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Long indexNo;
	private Long isLockRecord;
	private Long isDeleteRecord;
	private Long returnMessage;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String transactionSeqNo;

	// Constructors

	/** default constructor */
	public SoDeliveryLine() {
	}

	/** minimal constructor */
	public SoDeliveryLine(SoDeliveryHead soDeliveryHead) {
		this.soDeliveryHead = soDeliveryHead;
	}

	/** full constructor */
	public SoDeliveryLine(SoDeliveryHead soDeliveryHead, Long salesOrderId,
			String posMachineCode, Date salesOrderDate, String customerPoNo,
			String superintendentCode, String remark1, String remare2,
			String status, String reserve1, String reserve2, String reserve3,
			String reserve4, String reserve5, Long indexNo, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate,String transactionSeqNo) {
		this.soDeliveryHead = soDeliveryHead;
		this.salesOrderId = salesOrderId;
		this.posMachineCode = posMachineCode;
		this.salesOrderDate = salesOrderDate;
		this.customerPoNo = customerPoNo;
		this.superintendentCode = superintendentCode;
		this.remark1 = remark1;
		this.remare2 = remare2;
		this.status = status;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.indexNo = indexNo;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.transactionSeqNo = transactionSeqNo;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public SoDeliveryHead getSoDeliveryHead() {
		return this.soDeliveryHead;
	}

	public void setSoDeliveryHead(SoDeliveryHead soDeliveryHead) {
		this.soDeliveryHead = soDeliveryHead;
	}

	public Long getSalesOrderId() {
		return this.salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public String getPosMachineCode() {
		return this.posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	public Date getSalesOrderDate() {
		return this.salesOrderDate;
	}

	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public String getCustomerPoNo() {
		return this.customerPoNo;
	}

	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}

	public String getSuperintendentCode() {
		return this.superintendentCode;
	}

	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemare2() {
		return this.remare2;
	}

	public void setRemare2(String remare2) {
		this.remare2 = remare2;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
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

	public String getBreakable() {
		return breakable;
	}

	public void setBreakable(String breakable) {
		this.breakable = breakable;
	}

	public String getValuable() {
		return valuable;
	}

	public void setValuable(String valuable) {
		this.valuable = valuable;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getSuperintendentName() {
		return superintendentName;
	}

	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}

	public Long getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(Long isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public Long getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(Long isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public Long getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(Long returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getTransactionSeqNo() {
		return transactionSeqNo;
	}

	public void setTransactionSeqNo(String transactionSeqNo) {
		this.transactionSeqNo = transactionSeqNo;
	}
	
	

}