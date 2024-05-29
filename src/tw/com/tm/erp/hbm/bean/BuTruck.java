package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuShop entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuTruck implements java.io.Serializable {

	
	//private Long truckSNo;
	private String truckCode;
	private String freightName;
	private String truckType;
	private String truckDriver;
	private String truckDriverId;
	private String truckNumber;
	private String enable;
	private String description;
	private String createdBy;
	private String lastUpDatedBy;
	private Date creationDate;
	private Date lastUpDateDate;
	public BuTruck(){
	}
/*
	public Long getTruckSNo() {
		return truckSNo;
	}

	public void setTruckSNo(Long truckSNo) {
		this.truckSNo = truckSNo;
	}
*/
	public String getTruckCode() {
		return truckCode;
	}

	public void setTruckCode(String truckCode) {
		this.truckCode = truckCode;
	}

	public String getFreightName() {
		return freightName;
	}

	public void setFreightName(String freightName) {
		this.freightName = freightName;
	}

	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}

	public String getTruckDriver() {
		return truckDriver;
	}

	public void setTruckDriver(String truckDriver) {
		this.truckDriver = truckDriver;
	}

	public String getTruckDriverId() {
		return truckDriverId;
	}

	public void setTruckDriverId(String truckDriverId) {
		this.truckDriverId = truckDriverId;
	}

	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpDatedBy() {
		return lastUpDatedBy;
	}
	public void setLastUpDatedBy(String lastUpDatedBy) {
		this.lastUpDatedBy = lastUpDatedBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpDateDate() {
		return lastUpDateDate;
	}
	public void setLastUpDateDate(Date lastUpDateDate) {
		this.lastUpDateDate = lastUpDateDate;
	}

}