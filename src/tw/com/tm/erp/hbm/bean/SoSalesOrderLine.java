package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoSalesOrderLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderLine implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1826543926645506895L;

	private Long lineId;

	private SoSalesOrderHead soSalesOrderHead;

	private Long lineNo;

	private Long shipmentNo;

	private String itemCode;

	private Float quantity;

	private Long locationId;

	private Float discountRate;

	private String localCurrencyCode;

	private Float localUnitPrice;

	private String foreignCurrencyCode;

	private Float foreignUnitPrice;

	private Date scheduleShipDate;

	private Date shippedDate;

	private String isTax;

	private String taxCode;

	private Long activityId;

	private Long depositId;

	private String isUseDeposit;

	private String watchSerialNo;
	
	private Long indexNo;

	private String attribute1;

	private String attribute2;

	private String attribute3;

	private String attribute4;

	private String attribute5;

	private String status;

	private String createdBy;

	private Date creationDate;

	private String lastUpdatedBy;

	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public SoSalesOrderLine() {
	}

	/** minimal constructor */
	public SoSalesOrderLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public SoSalesOrderLine(Long lineId, SoSalesOrderHead soSalesOrderHead,
			Long lineNo, Long shipmentNo, String itemCode, Float quantity,
			Long locationId, Float discountRate, String localCurrencyCode,
			Float localUnitPrice, String foreignCurrencyCode,
			Float foreignUnitPrice, Date scheduleShipDate, Date shippedDate,
			String isTax, String taxCode, Long activityId, Long depositId,
			String isUseDeposit, String watchSerialNo, String attribute1,
			String attribute2, String attribute3, String attribute4,
			String attribute5, String status, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
		this.lineId = lineId;
		this.soSalesOrderHead = soSalesOrderHead;
		this.lineNo = lineNo;
		this.shipmentNo = shipmentNo;
		this.itemCode = itemCode;
		this.quantity = quantity;
		this.locationId = locationId;
		this.discountRate = discountRate;
		this.localCurrencyCode = localCurrencyCode;
		this.localUnitPrice = localUnitPrice;
		this.foreignCurrencyCode = foreignCurrencyCode;
		this.foreignUnitPrice = foreignUnitPrice;
		this.scheduleShipDate = scheduleShipDate;
		this.shippedDate = shippedDate;
		this.isTax = isTax;
		this.taxCode = taxCode;
		this.activityId = activityId;
		this.depositId = depositId;
		this.isUseDeposit = isUseDeposit;
		this.watchSerialNo = watchSerialNo;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.attribute3 = attribute3;
		this.attribute4 = attribute4;
		this.attribute5 = attribute5;
		this.status = status;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public SoSalesOrderHead getSoSalesOrderHead() {
		return this.soSalesOrderHead;
	}

	public void setSoSalesOrderHead(SoSalesOrderHead soSalesOrderHead) {
		this.soSalesOrderHead = soSalesOrderHead;
	}

	public Long getLineNo() {
		return this.lineNo;
	}

	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}

	public Long getShipmentNo() {
		return this.shipmentNo;
	}

	public void setShipmentNo(Long shipmentNo) {
		this.shipmentNo = shipmentNo;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Float getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Float getDiscountRate() {
		return this.discountRate;
	}

	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}

	public String getLocalCurrencyCode() {
		return this.localCurrencyCode;
	}

	public void setLocalCurrencyCode(String localCurrencyCode) {
		this.localCurrencyCode = localCurrencyCode;
	}

	public Float getLocalUnitPrice() {
		return this.localUnitPrice;
	}

	public void setLocalUnitPrice(Float localUnitPrice) {
		this.localUnitPrice = localUnitPrice;
	}

	public String getForeignCurrencyCode() {
		return this.foreignCurrencyCode;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	public Float getForeignUnitPrice() {
		return this.foreignUnitPrice;
	}

	public void setForeignUnitPrice(Float foreignUnitPrice) {
		this.foreignUnitPrice = foreignUnitPrice;
	}

	public Date getScheduleShipDate() {
		return this.scheduleShipDate;
	}

	public void setScheduleShipDate(Date scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}

	public Date getShippedDate() {
		return this.shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public String getIsTax() {
		return this.isTax;
	}

	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getDepositId() {
		return this.depositId;
	}

	public void setDepositId(Long depositId) {
		this.depositId = depositId;
	}

	public String getIsUseDeposit() {
		return this.isUseDeposit;
	}

	public void setIsUseDeposit(String isUseDeposit) {
		this.isUseDeposit = isUseDeposit;
	}

	public String getWatchSerialNo() {
		return this.watchSerialNo;
	}

	public void setWatchSerialNo(String watchSerialNo) {
		this.watchSerialNo = watchSerialNo;
	}
	
	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getAttribute1() {
		return this.attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return this.attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return this.attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return this.attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return this.attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
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
}