package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImMonthlyBalanceLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImMonthlyBalanceLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 523532343980897180L;
	private ImMonthlyBalanceLineId id;
	private ImMonthlyBalanceHead imMonthlyBalanceHead;
	private Double beginningOnHandQuantity;
	private Double beginningOnHandAmount;
	private Double periodPurchaseQuantity;
	private Double periodPurchaseAmount;
	private Double periodSalesQuantity;
	private Double periodSalesAmount;
	private Double periodMovementQuantity;
	private Double periodMovementAmount;
	private Double periodAdjustmentQuantity;
	private Double periodAdjustmentAmount;
	private Double periodOtherQuantity;
	private Double periodOtherAmount;
	private Double periodPosSalesQuantity;
	private Double periodPosSalesOriginalAmt;
	private Double periodPosSalesActualAmt;
	private Double endingOnHandQuantity;
	private Double endingOnHandAmount;
	private Double averageUnitCost;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	private String taxType;
	private Double periodAdjustTaxQuantity;
	private Double periodAdjustTaxAmount;
	private Double systemAdjustAmount;
	private Double periodAdjustCostQuantity;
	private Double periodAdjustCostAmount;

	// Constructors

	/** default constructor */
	public ImMonthlyBalanceLine() {
	}

	/** minimal constructor */
	public ImMonthlyBalanceLine(ImMonthlyBalanceLineId id,
			ImMonthlyBalanceHead imMonthlyBalanceHead) {
		this.id = id;
		this.imMonthlyBalanceHead = imMonthlyBalanceHead;
	}

	/** full constructor */
	public ImMonthlyBalanceLine(ImMonthlyBalanceLineId id,
			ImMonthlyBalanceHead imMonthlyBalanceHead,
			Double beginningOnHandQuantity, Double beginningOnHandAmount,
			Double periodPurchaseQuantity, Double periodPurchaseAmount,
			Double periodSalesQuantity, Double periodSalesAmount,
			Double periodMovementQuantity, Double periodMovementAmount,
			Double periodAdjustmentQuantity, Double periodAdjustmentAmount,
			Double periodOtherQuantity, Double periodOtherAmount,
			Double periodPosSalesQuantity, Double periodPosSalesOriginalAmt,
			Double periodPosSalesActualAmt, Double endingOnHandQuantity,
			Double endingOnHandAmount, Double averageUnitCost, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String updatedBy,
			Date updateDate, String taxType,
			Double periodAdjustTaxQuantity, Double periodAdjustTaxAmount,
			Double systemAdjustAmount, Double periodAdjustCostQuantity,
			Double periodAdjustCostAmount) {
		this.id = id;
		this.imMonthlyBalanceHead = imMonthlyBalanceHead;
		this.beginningOnHandQuantity = beginningOnHandQuantity;
		this.beginningOnHandAmount = beginningOnHandAmount;
		this.periodPurchaseQuantity = periodPurchaseQuantity;
		this.periodPurchaseAmount = periodPurchaseAmount;
		this.periodSalesQuantity = periodSalesQuantity;
		this.periodSalesAmount = periodSalesAmount;
		this.periodMovementQuantity = periodMovementQuantity;
		this.periodMovementAmount = periodMovementAmount;
		this.periodAdjustmentQuantity = periodAdjustmentQuantity;
		this.periodAdjustmentAmount = periodAdjustmentAmount;
		this.periodOtherQuantity = periodOtherQuantity;
		this.periodOtherAmount = periodOtherAmount;
		this.periodPosSalesQuantity = periodPosSalesQuantity;
		this.periodPosSalesOriginalAmt = periodPosSalesOriginalAmt;
		this.periodPosSalesActualAmt = periodPosSalesActualAmt;
		this.endingOnHandQuantity = endingOnHandQuantity;
		this.endingOnHandAmount = endingOnHandAmount;
		this.averageUnitCost = averageUnitCost;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
		this.taxType = taxType;
		this.periodAdjustTaxQuantity = periodAdjustTaxQuantity;
		this.periodAdjustTaxAmount = periodAdjustTaxAmount;
		this.systemAdjustAmount = systemAdjustAmount;
		this.periodAdjustCostQuantity = periodAdjustCostQuantity;
		this.periodAdjustCostAmount = periodAdjustCostAmount;
	}

	// Property accessors

	public ImMonthlyBalanceLineId getId() {
		return this.id;
	}

	public void setId(ImMonthlyBalanceLineId id) {
		this.id = id;
	}

	public ImMonthlyBalanceHead getImMonthlyBalanceHead() {
		return this.imMonthlyBalanceHead;
	}

	public void setImMonthlyBalanceHead(
			ImMonthlyBalanceHead imMonthlyBalanceHead) {
		this.imMonthlyBalanceHead = imMonthlyBalanceHead;
	}

	public Double getBeginningOnHandQuantity() {
		return this.beginningOnHandQuantity;
	}

	public void setBeginningOnHandQuantity(Double beginningOnHandQuantity) {
		this.beginningOnHandQuantity = beginningOnHandQuantity;
	}

	public Double getBeginningOnHandAmount() {
		return this.beginningOnHandAmount;
	}

	public void setBeginningOnHandAmount(Double beginningOnHandAmount) {
		this.beginningOnHandAmount = beginningOnHandAmount;
	}

	public Double getPeriodPurchaseQuantity() {
		return this.periodPurchaseQuantity;
	}

	public void setPeriodPurchaseQuantity(Double periodPurchaseQuantity) {
		this.periodPurchaseQuantity = periodPurchaseQuantity;
	}

	public Double getPeriodPurchaseAmount() {
		return this.periodPurchaseAmount;
	}

	public void setPeriodPurchaseAmount(Double periodPurchaseAmount) {
		this.periodPurchaseAmount = periodPurchaseAmount;
	}

	public Double getPeriodSalesQuantity() {
		return this.periodSalesQuantity;
	}

	public void setPeriodSalesQuantity(Double periodSalesQuantity) {
		this.periodSalesQuantity = periodSalesQuantity;
	}

	public Double getPeriodSalesAmount() {
		return this.periodSalesAmount;
	}

	public void setPeriodSalesAmount(Double periodSalesAmount) {
		this.periodSalesAmount = periodSalesAmount;
	}

	public Double getPeriodMovementQuantity() {
		return this.periodMovementQuantity;
	}

	public void setPeriodMovementQuantity(Double periodMovementQuantity) {
		this.periodMovementQuantity = periodMovementQuantity;
	}

	public Double getPeriodMovementAmount() {
		return this.periodMovementAmount;
	}

	public void setPeriodMovementAmount(Double periodMovementAmount) {
		this.periodMovementAmount = periodMovementAmount;
	}

	public Double getPeriodAdjustmentQuantity() {
		return this.periodAdjustmentQuantity;
	}

	public void setPeriodAdjustmentQuantity(Double periodAdjustmentQuantity) {
		this.periodAdjustmentQuantity = periodAdjustmentQuantity;
	}

	public Double getPeriodAdjustmentAmount() {
		return this.periodAdjustmentAmount;
	}

	public void setPeriodAdjustmentAmount(Double periodAdjustmentAmount) {
		this.periodAdjustmentAmount = periodAdjustmentAmount;
	}

	public Double getPeriodOtherQuantity() {
		return this.periodOtherQuantity;
	}

	public void setPeriodOtherQuantity(Double periodOtherQuantity) {
		this.periodOtherQuantity = periodOtherQuantity;
	}

	public Double getPeriodOtherAmount() {
		return this.periodOtherAmount;
	}

	public void setPeriodOtherAmount(Double periodOtherAmount) {
		this.periodOtherAmount = periodOtherAmount;
	}

	public Double getPeriodPosSalesQuantity() {
		return this.periodPosSalesQuantity;
	}

	public void setPeriodPosSalesQuantity(Double periodPosSalesQuantity) {
		this.periodPosSalesQuantity = periodPosSalesQuantity;
	}

	public Double getPeriodPosSalesOriginalAmt() {
		return this.periodPosSalesOriginalAmt;
	}

	public void setPeriodPosSalesOriginalAmt(Double periodPosSalesOriginalAmt) {
		this.periodPosSalesOriginalAmt = periodPosSalesOriginalAmt;
	}

	public Double getPeriodPosSalesActualAmt() {
		return this.periodPosSalesActualAmt;
	}

	public void setPeriodPosSalesActualAmt(Double periodPosSalesActualAmt) {
		this.periodPosSalesActualAmt = periodPosSalesActualAmt;
	}

	public Double getEndingOnHandQuantity() {
		return this.endingOnHandQuantity;
	}

	public void setEndingOnHandQuantity(Double endingOnHandQuantity) {
		this.endingOnHandQuantity = endingOnHandQuantity;
	}

	public Double getEndingOnHandAmount() {
		return this.endingOnHandAmount;
	}

	public void setEndingOnHandAmount(Double endingOnHandAmount) {
		this.endingOnHandAmount = endingOnHandAmount;
	}

	public Double getAverageUnitCost() {
		return this.averageUnitCost;
	}

	public void setAverageUnitCost(Double averageUnitCost) {
		this.averageUnitCost = averageUnitCost;
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

	public void setTaxType(String taxType){
		this.taxType = taxType;
	}
	
	public String getTaxType(){
		return this.taxType;
	}
	
	public void setPeriodAdjustTaxQuantity(Double periodAdjustTaxQuantity){
		this.periodAdjustTaxQuantity = periodAdjustTaxQuantity;
	} 
	
	public Double getPeriodAdjustTaxQuantity(){
		return this.periodAdjustTaxQuantity;
	}
	
	public void setPeriodAdjustTaxAmount(Double periodAdjustTaxAmount){
		this.periodAdjustTaxAmount = periodAdjustTaxAmount;
	}
	
	public Double getPeriodAdjustTaxAmount(){
		return this.periodAdjustTaxAmount;
	}
	
	public void setSystemAdjustAmount(Double systemAdjustAmount){
		this.systemAdjustAmount = systemAdjustAmount;
	}
	
	public Double getSystemAdjustAmount(){
		return this.systemAdjustAmount;
	}
	
	public void setPeriodAdjustCostQuantity(Double periodAdjustCostQuantity){
		this.periodAdjustCostQuantity = periodAdjustCostQuantity;
	}
	
	public Double getPeriodAdjustCostQuantity(){
		return this.periodAdjustCostQuantity;
	}
	
	public void setPeriodAdjustCostAmount(Double periodAdjustCostAmount){
		this.periodAdjustCostAmount = periodAdjustCostAmount;
	}
	
	public Double getPeriodAdjustCostAmount(){
		return this.periodAdjustCostAmount;
	}
}