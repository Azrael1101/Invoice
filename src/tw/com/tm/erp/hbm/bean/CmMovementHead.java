package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CmMovementHeadId entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class CmMovementHead implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 250014986000187158L;
	// Fields
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date deliveryDate;
	private String taxType;
	private String deliveryCustomsWarehouse;
	private String arrivalCustomsWarehouse;
	private String passNo;
	private String sealNo;
	private String customsArea;
	private String carType;
	private String carNo;
	private String expenseType;
	private String expenseNo;
	private Long expenseAmount;
	private String driverCode;
	private String applicantCode;
	private String deliverymanCode;
	private String sealType;
	private String remark1;
	private String remark2;
	private String status;
	private String statusName; // 額外欄位 狀態中文
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
	private String cStatus;
	private String moveWhNo;
	private String customsStatus;
	private String tranRecordStatus;
	private String tranAllowUpload;
	private String customsStatusName;
	private String schedule;
	
	private String customsInTime;
	private String customsOutTime;

	private List cmMovementLines = new ArrayList(0);

	// 運送單新增欄位
	private String transferOrderNo;

	// Constructors
	/** default constructor */
	public CmMovementHead() {
	}

	public CmMovementHead(Long headId) {
		this.headId = headId;
	}

	/** minimal constructor */
	public CmMovementHead(String brandCode, String orderTypeCode,
			String orderNo) {
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
	}

	/** full constructor */
	public CmMovementHead(Long headId, String brandCode, String orderTypeCode,
			String orderNo, Date deliveryDate, String taxType,
			String deliveryCustomsWarehouse, String arrivalCustomsWarehouse,
			String passNo, String sealNo, String customsArea, String carType,
			String carNo, String expenseType, String expenseNo,
			Long expenseAmount, String driverCode, String applicantCode,
			String deliverymanCode, String sealType, String remark1,
			String remark2, String status, String statusName, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List cmMovementLines) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.deliveryDate = deliveryDate;
		this.taxType = taxType;
		this.deliveryCustomsWarehouse = deliveryCustomsWarehouse;
		this.arrivalCustomsWarehouse = arrivalCustomsWarehouse;
		this.passNo = passNo;
		this.sealNo = sealNo;
		this.customsArea = customsArea;
		this.carType = carType;
		this.carNo = carNo;
		this.expenseType = expenseType;
		this.expenseNo = expenseNo;
		this.expenseAmount = expenseAmount;
		this.driverCode = driverCode;
		this.applicantCode = applicantCode;
		this.deliverymanCode = deliverymanCode;
		this.sealType = sealType;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.status = status;
		this.statusName = statusName;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.cmMovementLines = cmMovementLines;
		
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
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

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getTaxType() {
		return this.taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getDeliveryCustomsWarehouse() {
		return this.deliveryCustomsWarehouse;
	}

	public void setDeliveryCustomsWarehouse(String deliveryCustomsWarehouse) {
		this.deliveryCustomsWarehouse = deliveryCustomsWarehouse;
	}

	public String getArrivalCustomsWarehouse() {
		return this.arrivalCustomsWarehouse;
	}

	public void setArrivalCustomsWarehouse(String arrivalCustomsWarehouse) {
		this.arrivalCustomsWarehouse = arrivalCustomsWarehouse;
	}

	public String getPassNo() {
		return this.passNo;
	}

	public void setPassNo(String passNo) {
		this.passNo = passNo;
	}

	public String getSealNo() {
		return this.sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}

	public String getCustomsArea() {
		return this.customsArea;
	}

	public void setCustomsArea(String customsArea) {
		this.customsArea = customsArea;
	}

	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getExpenseType() {
		return this.expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

	public String getExpenseNo() {
		return this.expenseNo;
	}

	public void setExpenseNo(String expenseNo) {
		this.expenseNo = expenseNo;
	}

	public Long getExpenseAmount() {
		return this.expenseAmount;
	}

	public void setExpenseAmount(Long expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getDriverCode() {
		return this.driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	public String getApplicantCode() {
		return this.applicantCode;
	}

	public void setApplicantCode(String applicantCode) {
		this.applicantCode = applicantCode;
	}

	public String getDeliverymanCode() {
		return this.deliverymanCode;
	}

	public void setDeliverymanCode(String deliverymanCode) {
		this.deliverymanCode = deliverymanCode;
	}

	public String getSealType() {
		return this.sealType;
	}

	public void setSealType(String sealType) {
		this.sealType = sealType;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
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

	public List getCmMovementLines() {
		return cmMovementLines;
	}

	public void setCmMovementLines(List cmMovementLines) {
		this.cmMovementLines = cmMovementLines;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CmMovementHead))
			return false;
		CmMovementHead castOther = (CmMovementHead) other;

		return ((this.getHeadId() == castOther.getHeadId()) || (this
				.getHeadId() != null
				&& castOther.getHeadId() != null && this.getHeadId().equals(
				castOther.getHeadId())))
				&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
						.getBrandCode() != null
						&& castOther.getBrandCode() != null && this
						.getBrandCode().equals(castOther.getBrandCode())))
				&& ((this.getOrderTypeCode() == castOther.getOrderTypeCode()) || (this
						.getOrderTypeCode() != null
						&& castOther.getOrderTypeCode() != null && this
						.getOrderTypeCode()
						.equals(castOther.getOrderTypeCode())))
				&& ((this.getOrderNo() == castOther.getOrderNo()) || (this
						.getOrderNo() != null
						&& castOther.getOrderNo() != null && this.getOrderNo()
						.equals(castOther.getOrderNo())))
				&& ((this.getDeliveryDate() == castOther.getDeliveryDate()) || (this
						.getDeliveryDate() != null
						&& castOther.getDeliveryDate() != null && this
						.getDeliveryDate().equals(castOther.getDeliveryDate())))
				&& ((this.getTaxType() == castOther.getTaxType()) || (this
						.getTaxType() != null
						&& castOther.getTaxType() != null && this.getTaxType()
						.equals(castOther.getTaxType())))
				&& ((this.getDeliveryCustomsWarehouse() == castOther
						.getDeliveryCustomsWarehouse()) || (this
						.getDeliveryCustomsWarehouse() != null
						&& castOther.getDeliveryCustomsWarehouse() != null && this
						.getDeliveryCustomsWarehouse().equals(
								castOther.getDeliveryCustomsWarehouse())))
				&& ((this.getArrivalCustomsWarehouse() == castOther
						.getArrivalCustomsWarehouse()) || (this
						.getArrivalCustomsWarehouse() != null
						&& castOther.getArrivalCustomsWarehouse() != null && this
						.getArrivalCustomsWarehouse().equals(
								castOther.getArrivalCustomsWarehouse())))
				&& ((this.getPassNo() == castOther.getPassNo()) || (this
						.getPassNo() != null
						&& castOther.getPassNo() != null && this.getPassNo()
						.equals(castOther.getPassNo())))
				&& ((this.getSealNo() == castOther.getSealNo()) || (this
						.getSealNo() != null
						&& castOther.getSealNo() != null && this.getSealNo()
						.equals(castOther.getSealNo())))
				&& ((this.getCustomsArea() == castOther.getCustomsArea()) || (this
						.getCustomsArea() != null
						&& castOther.getCustomsArea() != null && this
						.getCustomsArea().equals(castOther.getCustomsArea())))
				&& ((this.getCarType() == castOther.getCarType()) || (this
						.getCarType() != null
						&& castOther.getCarType() != null && this.getCarType()
						.equals(castOther.getCarType())))
				&& ((this.getCarNo() == castOther.getCarNo()) || (this
						.getCarNo() != null
						&& castOther.getCarNo() != null && this.getCarNo()
						.equals(castOther.getCarNo())))
				&& ((this.getExpenseType() == castOther.getExpenseType()) || (this
						.getExpenseType() != null
						&& castOther.getExpenseType() != null && this
						.getExpenseType().equals(castOther.getExpenseType())))
				&& ((this.getExpenseNo() == castOther.getExpenseNo()) || (this
						.getExpenseNo() != null
						&& castOther.getExpenseNo() != null && this
						.getExpenseNo().equals(castOther.getExpenseNo())))
				&& ((this.getExpenseAmount() == castOther.getExpenseAmount()) || (this
						.getExpenseAmount() != null
						&& castOther.getExpenseAmount() != null && this
						.getExpenseAmount()
						.equals(castOther.getExpenseAmount())))
				&& ((this.getDriverCode() == castOther.getDriverCode()) || (this
						.getDriverCode() != null
						&& castOther.getDriverCode() != null && this
						.getDriverCode().equals(castOther.getDriverCode())))
				&& ((this.getApplicantCode() == castOther.getApplicantCode()) || (this
						.getApplicantCode() != null
						&& castOther.getApplicantCode() != null && this
						.getApplicantCode()
						.equals(castOther.getApplicantCode())))
				&& ((this.getDeliverymanCode() == castOther
						.getDeliverymanCode()) || (this.getDeliverymanCode() != null
						&& castOther.getDeliverymanCode() != null && this
						.getDeliverymanCode().equals(
								castOther.getDeliverymanCode())))
				&& ((this.getSealType() == castOther.getSealType()) || (this
						.getSealType() != null
						&& castOther.getSealType() != null && this
						.getSealType().equals(castOther.getSealType())))
				&& ((this.getRemark1() == castOther.getRemark1()) || (this
						.getRemark1() != null
						&& castOther.getRemark1() != null && this.getRemark1()
						.equals(castOther.getRemark1())))
				&& ((this.getRemark2() == castOther.getRemark2()) || (this
						.getRemark2() != null
						&& castOther.getRemark2() != null && this.getRemark2()
						.equals(castOther.getRemark2())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null
						&& castOther.getStatus() != null && this.getStatus()
						.equals(castOther.getStatus())))
				&& ((this.getReserve1() == castOther.getReserve1()) || (this
						.getReserve1() != null
						&& castOther.getReserve1() != null && this
						.getReserve1().equals(castOther.getReserve1())))
				&& ((this.getReserve2() == castOther.getReserve2()) || (this
						.getReserve2() != null
						&& castOther.getReserve2() != null && this
						.getReserve2().equals(castOther.getReserve2())))
				&& ((this.getReserve3() == castOther.getReserve3()) || (this
						.getReserve3() != null
						&& castOther.getReserve3() != null && this
						.getReserve3().equals(castOther.getReserve3())))
				&& ((this.getReserve4() == castOther.getReserve4()) || (this
						.getReserve4() != null
						&& castOther.getReserve4() != null && this
						.getReserve4().equals(castOther.getReserve4())))
				&& ((this.getReserve5() == castOther.getReserve5()) || (this
						.getReserve5() != null
						&& castOther.getReserve5() != null && this
						.getReserve5().equals(castOther.getReserve5())))
				&& ((this.getCreatedBy() == castOther.getCreatedBy()) || (this
						.getCreatedBy() != null
						&& castOther.getCreatedBy() != null && this
						.getCreatedBy().equals(castOther.getCreatedBy())))
				&& ((this.getCreationDate() == castOther.getCreationDate()) || (this
						.getCreationDate() != null
						&& castOther.getCreationDate() != null && this
						.getCreationDate().equals(castOther.getCreationDate())))
				&& ((this.getLastUpdatedBy() == castOther.getLastUpdatedBy()) || (this
						.getLastUpdatedBy() != null
						&& castOther.getLastUpdatedBy() != null && this
						.getLastUpdatedBy()
						.equals(castOther.getLastUpdatedBy())))
				&& ((this.getLastUpdateDate() == castOther.getLastUpdateDate()) || (this
						.getLastUpdateDate() != null
						&& castOther.getLastUpdateDate() != null && this
						.getLastUpdateDate().equals(
								castOther.getLastUpdateDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getHeadId() == null ? 0 : this.getHeadId().hashCode());
		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getOrderTypeCode() == null ? 0 : this.getOrderTypeCode()
						.hashCode());
		result = 37 * result
				+ (getOrderNo() == null ? 0 : this.getOrderNo().hashCode());
		result = 37
				* result
				+ (getDeliveryDate() == null ? 0 : this.getDeliveryDate()
						.hashCode());
		result = 37 * result
				+ (getTaxType() == null ? 0 : this.getTaxType().hashCode());
		result = 37
				* result
				+ (getDeliveryCustomsWarehouse() == null ? 0 : this
						.getDeliveryCustomsWarehouse().hashCode());
		result = 37
				* result
				+ (getArrivalCustomsWarehouse() == null ? 0 : this
						.getArrivalCustomsWarehouse().hashCode());
		result = 37 * result
				+ (getPassNo() == null ? 0 : this.getPassNo().hashCode());
		result = 37 * result
				+ (getSealNo() == null ? 0 : this.getSealNo().hashCode());
		result = 37
				* result
				+ (getCustomsArea() == null ? 0 : this.getCustomsArea()
						.hashCode());
		result = 37 * result
				+ (getCarType() == null ? 0 : this.getCarType().hashCode());
		result = 37 * result
				+ (getCarNo() == null ? 0 : this.getCarNo().hashCode());
		result = 37
				* result
				+ (getExpenseType() == null ? 0 : this.getExpenseType()
						.hashCode());
		result = 37 * result
				+ (getExpenseNo() == null ? 0 : this.getExpenseNo().hashCode());
		result = 37
				* result
				+ (getExpenseAmount() == null ? 0 : this.getExpenseAmount()
						.hashCode());
		result = 37
				* result
				+ (getDriverCode() == null ? 0 : this.getDriverCode()
						.hashCode());
		result = 37
				* result
				+ (getApplicantCode() == null ? 0 : this.getApplicantCode()
						.hashCode());
		result = 37
				* result
				+ (getDeliverymanCode() == null ? 0 : this.getDeliverymanCode()
						.hashCode());
		result = 37 * result
				+ (getSealType() == null ? 0 : this.getSealType().hashCode());
		result = 37 * result
				+ (getRemark1() == null ? 0 : this.getRemark1().hashCode());
		result = 37 * result
				+ (getRemark2() == null ? 0 : this.getRemark2().hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37 * result
				+ (getReserve1() == null ? 0 : this.getReserve1().hashCode());
		result = 37 * result
				+ (getReserve2() == null ? 0 : this.getReserve2().hashCode());
		result = 37 * result
				+ (getReserve3() == null ? 0 : this.getReserve3().hashCode());
		result = 37 * result
				+ (getReserve4() == null ? 0 : this.getReserve4().hashCode());
		result = 37 * result
				+ (getReserve5() == null ? 0 : this.getReserve5().hashCode());
		result = 37 * result
				+ (getCreatedBy() == null ? 0 : this.getCreatedBy().hashCode());
		result = 37
				* result
				+ (getCreationDate() == null ? 0 : this.getCreationDate()
						.hashCode());
		result = 37
				* result
				+ (getLastUpdatedBy() == null ? 0 : this.getLastUpdatedBy()
						.hashCode());
		result = 37
				* result
				+ (getLastUpdateDate() == null ? 0 : this.getLastUpdateDate()
						.hashCode());
		return result;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getProcessId() {
	    return processId;
	}

	public void setProcessId(Long processId) {
	    this.processId = processId;
	}

	public String getTransferOrderNo() {
		return transferOrderNo;
	}

	public void setTransferOrderNo(String transferOrderNo) {
		this.transferOrderNo = transferOrderNo;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String status) {
		cStatus = status;
	}

	public String getMoveWhNo() {
		return moveWhNo;
	}

	public void setMoveWhNo(String moveWhNo) {
		this.moveWhNo = moveWhNo;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getTranRecordStatus(){
		return tranRecordStatus;
	}
	
	public void setTranRecordStatus(String tranRecordStatus){
		this.tranRecordStatus = tranRecordStatus;
	}

	public String getTranAllowUpload() {
		return tranAllowUpload;
	}

	public void setTranAllowUpload(String tranAllowUpload) {
		this.tranAllowUpload = tranAllowUpload;
	}

	public String getCustomsStatusName() {
		return customsStatusName;
	}

	public void setCustomsStatusName(String customsStatusName) {
		this.customsStatusName = customsStatusName;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getCustomsInTime() {
		return customsInTime;
	}

	public void setCustomsInTime(String customsInTime) {
		this.customsInTime = customsInTime;
	}

	public String getCustomsOutTime() {
		return customsOutTime;
	}

	public void setCustomsOutTime(String customsOutTime) {
		this.customsOutTime = customsOutTime;
	}

	

	
}