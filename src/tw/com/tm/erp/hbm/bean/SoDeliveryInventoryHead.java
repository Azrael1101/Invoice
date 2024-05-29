package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryInventoryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoDeliveryInventoryHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3620749264231289659L;
	private Long headId;
	private String brandCode;
	private String orderTypeCode;
	private String orderNo;
	private Date countsDate;
	private String storeArea;
	private String range;
	private Date flightDate;
	private String storageCodeStart;
	private String storageCodeEnd;
	private String countsId;
	private String superintendentCode;
	private String isImportedFile;
	private Long importedTimes;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String status;
	private String createdBy;
	private Date creationDate;
	private String lastUpdateBy;
	private Date lastUpdateDate;
	private List<SoDeliveryInventoryLine> soDeliveryInventoryLines = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SoDeliveryInventoryHead() {
	}

	/** full constructor */
	public SoDeliveryInventoryHead(String brandCode, String orderTypeCode,
			String orderNo, Date countsDate, String storeArea, String range,
			Date flightDate, String storageCodeStart, String storageCodeEnd,
			String countsId, String superintendentCode, String isImportedFile,
			Long importedTimes, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, String status,
			String createdBy, Date creationDate, String lastUpdateBy,
			Date lastUpdateDate, List<SoDeliveryInventoryLine> soDeliveryInventoryLines) {
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.countsDate = countsDate;
		this.storeArea = storeArea;
		this.range = range;
		this.flightDate = flightDate;
		this.storageCodeStart = storageCodeStart;
		this.storageCodeEnd = storageCodeEnd;
		this.countsId = countsId;
		this.superintendentCode = superintendentCode;
		this.isImportedFile = isImportedFile;
		this.importedTimes = importedTimes;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.status = status;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdateBy = lastUpdateBy;
		this.lastUpdateDate = lastUpdateDate;
		this.soDeliveryInventoryLines = soDeliveryInventoryLines;
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

	public Date getCountsDate() {
		return this.countsDate;
	}

	public void setCountsDate(Date countsDate) {
		this.countsDate = countsDate;
	}

	public String getStoreArea() {
		return this.storeArea;
	}

	public void setStoreArea(String storeArea) {
		this.storeArea = storeArea;
	}

	public String getRange() {
		return this.range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Date getFlightDate() {
		return this.flightDate;
	}

	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	public String getStorageCodeStart() {
		return this.storageCodeStart;
	}

	public void setStorageCodeStart(String storageCodeStart) {
		this.storageCodeStart = storageCodeStart;
	}

	public String getStorageCodeEnd() {
		return this.storageCodeEnd;
	}

	public void setStorageCodeEnd(String storageCodeEnd) {
		this.storageCodeEnd = storageCodeEnd;
	}

	public String getCountsId() {
		return this.countsId;
	}

	public void setCountsId(String countsId) {
		this.countsId = countsId;
	}

	public String getSuperintendentCode() {
		return this.superintendentCode;
	}

	public void setSuperintendentCode(String superintendentCode) {
		this.superintendentCode = superintendentCode;
	}

	public String getIsImportedFile() {
		return this.isImportedFile;
	}

	public void setIsImportedFile(String isImportedFile) {
		this.isImportedFile = isImportedFile;
	}

	public Long getImportedTimes() {
		return this.importedTimes;
	}

	public void setImportedTimes(Long importedTimes) {
		this.importedTimes = importedTimes;
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
	
	public List<SoDeliveryInventoryLine> getSoDeliveryInventoryLines(){
		return this.soDeliveryInventoryLines;
	}

	public void setSoDeliveryInventoryLines(List<SoDeliveryInventoryLine> soDeliveryInventoryLines){
		this.soDeliveryInventoryLines = soDeliveryInventoryLines;
	}
}