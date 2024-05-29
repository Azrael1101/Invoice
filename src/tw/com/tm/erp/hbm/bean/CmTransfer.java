package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmTransfer entity. @author MyEclipse Persistence Tools
 */

public class CmTransfer implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8170484368835447607L;
	private String transferOrderNo;
	private String transfer;
	private String startStation;
	private Date leaveTime;
	private String leaveTimeT; // 前後端傳值：出站時間
	private Date arriveTime;
	private String arriveTimeT; // 前後端傳值：到站時間
	private String toStation;
	private String owner;
	private Integer leaveBox;
	private Integer leaveQuantity;
	private Integer truckBox;
	private Integer truckQuantity;
	private String orderNo;
	private String sealNo;
	private String vehicleStation;
	private String vehicleNo;
	private String driverCode;
	private String driverLicence;
	private String carNo;
	private String track;

	private String status;
	private String statusName; // 額外欄位 狀態中文

	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	private String airplaneNo;
	private String masterNo;
	private String secondNo;
	private String clearance;
	private String release;

	private String leaveBoxNote;
	private String leaveQuantityNote;
	private String truckBoxNote;
	private String truckQuantityNote;

	public String getLeaveBoxNote() {
		return leaveBoxNote;
	}

	public void setLeaveBoxNote(String leaveBoxNote) {
		this.leaveBoxNote = leaveBoxNote;
	}

	public String getLeaveQuantityNote() {
		return leaveQuantityNote;
	}

	public void setLeaveQuantityNote(String leaveQuantityNote) {
		this.leaveQuantityNote = leaveQuantityNote;
	}

	public String getTruckBoxNote() {
		return truckBoxNote;
	}

	public void setTruckBoxNote(String truckBoxNote) {
		this.truckBoxNote = truckBoxNote;
	}

	public String getTruckQuantityNote() {
		return truckQuantityNote;
	}

	public void setTruckQuantityNote(String truckQuantityNote) {
		this.truckQuantityNote = truckQuantityNote;
	}

	public CmTransfer(String transferOrderNo) {
		this.transferOrderNo = transferOrderNo;
	}

	public String getTransferOrderNo() {
		return transferOrderNo;
	}

	public void setTransferOrderNo(String transferOrderNo) {
		this.transferOrderNo = transferOrderNo;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getLeaveBox() {
		return leaveBox;
	}

	public void setLeaveBox(Integer leaveBox) {
		this.leaveBox = leaveBox;
	}

	public Integer getLeaveQuantity() {
		return leaveQuantity;
	}

	public void setLeaveQuantity(Integer leaveQuantity) {
		this.leaveQuantity = leaveQuantity;
	}

	public Integer getTruckBox() {
		return truckBox;
	}

	public void setTruckBox(Integer truckBox) {
		this.truckBox = truckBox;
	}

	public Integer getTruckQuantity() {
		return truckQuantity;
	}

	public void setTruckQuantity(Integer truckQuantity) {
		this.truckQuantity = truckQuantity;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}

	public String getVehicleStation() {
		return vehicleStation;
	}

	public void setVehicleStation(String vehicleStation) {
		this.vehicleStation = vehicleStation;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriverCode() {
		return driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	public String getDriverLicence() {
		return driverLicence;
	}

	public void setDriverLicence(String driverLicence) {
		this.driverLicence = driverLicence;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getAirplaneNo() {
		return airplaneNo;
	}

	public void setAirplaneNo(String airplaneNo) {
		this.airplaneNo = airplaneNo;
	}

	public String getMasterNo() {
		return masterNo;
	}

	public void setMasterNo(String masterNo) {
		this.masterNo = masterNo;
	}

	public String getSecondNo() {
		return secondNo;
	}

	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}

	public String getClearance() {
		return clearance;
	}

	public void setClearance(String clearance) {
		this.clearance = clearance;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLeaveTimeT() {
		return leaveTimeT;
	}

	public void setLeaveTimeT(String leaveTimeT) {
		this.leaveTimeT = leaveTimeT;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getArriveTimeT() {
		return arriveTimeT;
	}

	public void setArriveTimeT(String arriveTimeT) {
		this.arriveTimeT = arriveTimeT;
	}

	public CmTransfer() {

	}

	public CmTransfer(String transferOrderNo, String transfer, String startStation, Date leaveTime, String toStation,
			String owner, Integer leaveBox, Integer leaveQuantity, Integer truckBox, Integer truckQuantity, String orderNo,
			String sealNo, String vehicleStation, String vehicleNo, String driverCode, String driverLicence, String carNo,
			String track, String status, String createdBy, Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
			String airplaneNo, String masterNo, String secondNo, String clearance, String release, String leaveBoxNote,
			String leaveQuantityNote, String truckBoxNote, String truckQuantityNote) {
		super();
		this.transferOrderNo = transferOrderNo;
		this.transfer = transfer;
		this.startStation = startStation;
		this.leaveTime = leaveTime;
		this.toStation = toStation;
		this.owner = owner;
		this.leaveBox = leaveBox;
		this.leaveQuantity = leaveQuantity;
		this.truckBox = truckBox;
		this.truckQuantity = truckQuantity;
		this.orderNo = orderNo;
		this.sealNo = sealNo;
		this.vehicleStation = vehicleStation;
		this.vehicleNo = vehicleNo;
		this.driverCode = driverCode;
		this.driverLicence = driverLicence;
		this.carNo = carNo;
		this.track = track;
		this.status = status;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.airplaneNo = airplaneNo;
		this.masterNo = masterNo;
		this.secondNo = secondNo;
		this.clearance = clearance;
		this.release = release;
		this.leaveBoxNote = leaveBoxNote;
		this.leaveQuantityNote = leaveQuantityNote;
		this.truckBoxNote = truckBoxNote;
		this.truckQuantityNote = truckQuantityNote;
	}

}