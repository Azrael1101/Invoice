package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImMonthlyBalanceLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDailyBalanceLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 523532343980897180L;
	private ImDailyBalanceLineId id;
	private Double beginningOnHandQuantity;
	private Double dailyPurchaseQuantity;
	private Double dailySalesQuantity;
	private Double dailyMovementQuantity;
	private Double dailyAdjustmentQuantity;
	private Double dailyOtherQuantity;
	private Double endingOnHandQuantity;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public ImDailyBalanceLine() {
	}

	/** minimal constructor */
	public ImDailyBalanceLine(ImDailyBalanceLineId id) {
		this.id = id;
	}

	/** full constructor */
	public ImDailyBalanceLine(ImDailyBalanceLineId id,
			Double beginningOnHandQuantity, 
			Double dailyPurchaseQuantity, 
			Double dailySalesQuantity, 
			Double dailyMovementQuantity, 
			Double dailyAdjustmentQuantity, 
			Double dailyOtherQuantity, 
			Double endingOnHandQuantity, 
			String createdBy, Date creationDate, String updatedBy,
			Date updateDate) {
		this.id = id;
		this.beginningOnHandQuantity = beginningOnHandQuantity;
		this.dailyPurchaseQuantity = dailyPurchaseQuantity;
		this.dailySalesQuantity = dailySalesQuantity;
		this.dailyMovementQuantity = dailyMovementQuantity;
		this.dailyAdjustmentQuantity = dailyAdjustmentQuantity;
		this.dailyOtherQuantity = dailyOtherQuantity;
		this.endingOnHandQuantity = endingOnHandQuantity;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
	}

	// Property accessors

	public ImDailyBalanceLineId getId() {
		return this.id;
	}

	public void setId(ImDailyBalanceLineId id) {
		this.id = id;
	}

	public Double getBeginningOnHandQuantity() {
		return this.beginningOnHandQuantity;
	}

	public void setBeginningOnHandQuantity(Double beginningOnHandQuantity) {
		this.beginningOnHandQuantity = beginningOnHandQuantity;
	}

	public Double getDailyPurchaseQuantity() {
		return this.dailyPurchaseQuantity;
	}

	public void setDailyPurchaseQuantity(Double dailyPurchaseQuantity) {
		this.dailyPurchaseQuantity = dailyPurchaseQuantity;
	}

	public Double getDailySalesQuantity() {
		return this.dailySalesQuantity;
	}

	public void setDailySalesQuantity(Double dailySalesQuantity) {
		this.dailySalesQuantity = dailySalesQuantity;
	}

	public Double getDailyMovementQuantity() {
		return this.dailyMovementQuantity;
	}

	public void setDailyMovementQuantity(Double dailyMovementQuantity) {
		this.dailyMovementQuantity = dailyMovementQuantity;
	}

	public Double getDailyAdjustmentQuantity() {
		return this.dailyAdjustmentQuantity;
	}

	public void setDailyAdjustmentQuantity(Double dailyAdjustmentQuantity) {
		this.dailyAdjustmentQuantity = dailyAdjustmentQuantity;
	}

	public Double getDailyOtherQuantity() {
		return this.dailyOtherQuantity;
	}

	public void setDailyOtherQuantity(Double dailyOtherQuantity) {
		this.dailyOtherQuantity = dailyOtherQuantity;
	}

	public Double getEndingOnHandQuantity() {
		return this.endingOnHandQuantity;
	}

	public void setEndingOnHandQuantity(Double endingOnHandQuantity) {
		this.endingOnHandQuantity = endingOnHandQuantity;
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

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}