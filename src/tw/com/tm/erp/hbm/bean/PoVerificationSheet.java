package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PoVerificationSheet entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoVerificationSheet implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7021568389183985518L;
	// Fields

	private Long headId;
	private String imReceiveOrderType;
	private String imReceiveOrderNo;
	private Long imReceiveLineId;
	private String itemCode;
	private String invoiceNo;
	private String inoviceLotNo;
	private String poOrderType;
	private String poOrderNo;
	private Long poOrderLineId;
	private Double quantity;
	private String adjustmentOrderType;
	private String adjustmentOrderNo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private String status;
	private String brandCode;  //品牌代號
	private Double poQuantity;
	private Double poUnitCost;
	private String budgetYear;
	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getImReceiveOrderType() {
		return this.imReceiveOrderType;
	}

	public void setImReceiveOrderType(String imReceiveOrderType) {
		this.imReceiveOrderType = imReceiveOrderType;
	}

	public String getImReceiveOrderNo() {
		return this.imReceiveOrderNo;
	}

	public void setImReceiveOrderNo(String imReceiveOrderNo) {
		this.imReceiveOrderNo = imReceiveOrderNo;
	}

	public Long getImReceiveLineId() {
		return this.imReceiveLineId;
	}

	public void setImReceiveLineId(Long imReceiveLineId) {
		this.imReceiveLineId = imReceiveLineId;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInoviceLotNo() {
		return this.inoviceLotNo;
	}

	public void setInoviceLotNo(String inoviceLotNo) {
		this.inoviceLotNo = inoviceLotNo;
	}

	public String getPoOrderType() {
		return this.poOrderType;
	}

	public void setPoOrderType(String poOrderType) {
		this.poOrderType = poOrderType;
	}

	public String getPoOrderNo() {
		return this.poOrderNo;
	}

	public void setPoOrderNo(String poOrderNo) {
		this.poOrderNo = poOrderNo;
	}

	public Long getPoOrderLineId() {
		return this.poOrderLineId;
	}

	public void setPoOrderLineId(Long poOrderLineId) {
		this.poOrderLineId = poOrderLineId;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getAdjustmentOrderType() {
		return this.adjustmentOrderType;
	}

	public void setAdjustmentOrderType(String adjustmentOrderType) {
		this.adjustmentOrderType = adjustmentOrderType;
	}

	public String getAdjustmentOrderNo() {
		return this.adjustmentOrderNo;
	}

	public void setAdjustmentOrderNo(String adjustmentOrderNo) {
		this.adjustmentOrderNo = adjustmentOrderNo;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public Double getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Double poQuantity) {
		this.poQuantity = poQuantity;
	}

	public Double getPoUnitCost() {
		return poUnitCost;
	}

	public void setPoUnitCost(Double poUnitCost) {
		this.poUnitCost = poUnitCost;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

}