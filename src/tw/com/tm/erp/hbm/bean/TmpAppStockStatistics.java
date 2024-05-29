package tw.com.tm.erp.hbm.bean;

/**
 * TmpAppStockStatistics entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpAppStockStatistics implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8320803661332233479L;
    // Fields
    private String brandCode;
    private String itemCode;
    private String itemName;
    private String warehouseCode;
    private Double unitPrice;
    private Double averageUnitCost;
    private Double beginOnHandQty;
    private Double purchaseQty;
    private Double purchaseAmt;
    private Double salesQty;
    private Double salesAmt;
    private Double movementQty;
    private Double movementAmt;
    private Double adjustmentQty;
    private Double adjustmentAmt;
    private Double otherQty;
    private Double otherAmt;
    private Double endOnHandQty;
    private Double posQty;
    private Double posAmt;

    // Constructors

    /** default constructor */
    public TmpAppStockStatistics() {
    }

    /** full constructor */
    public TmpAppStockStatistics(String brandCode, String itemCode,
	    String itemName, String warehouseCode, Double unitPrice,
	    Double averageUnitCost, Double beginOnHandQty, Double purchaseQty,
	    Double purchaseAmt, Double salesQty, Double salesAmt,
	    Double movementQty, Double movementAmt, Double adjustmentQty,
	    Double adjustmentAmt, Double otherQty, Double otherAmt,
	    Double endOnHandQty, Double posQty, Double posAmt) {
	this.brandCode = brandCode;
	this.itemCode = itemCode;
	this.itemName = itemName;
	this.warehouseCode = warehouseCode;
	this.unitPrice = unitPrice;
	this.averageUnitCost = averageUnitCost;
	this.beginOnHandQty = beginOnHandQty;
	this.purchaseQty = purchaseQty;
	this.purchaseAmt = purchaseAmt;
	this.salesQty = salesQty;
	this.salesAmt = salesAmt;
	this.movementQty = movementQty;
	this.movementAmt = movementAmt;
	this.adjustmentQty = adjustmentQty;
	this.adjustmentAmt = adjustmentAmt;
	this.otherQty = otherQty;
	this.otherAmt = otherAmt;
	this.endOnHandQty = endOnHandQty;
	this.posQty = posQty;
	this.posAmt = posAmt;
    }

    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getItemName() {
	return this.itemName;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public Double getUnitPrice() {
	return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
	this.unitPrice = unitPrice;
    }

    public Double getAverageUnitCost() {
	return this.averageUnitCost;
    }

    public void setAverageUnitCost(Double averageUnitCost) {
	this.averageUnitCost = averageUnitCost;
    }

    public Double getBeginOnHandQty() {
	return this.beginOnHandQty;
    }

    public void setBeginOnHandQty(Double beginOnHandQty) {
	this.beginOnHandQty = beginOnHandQty;
    }

    public Double getPurchaseQty() {
	return this.purchaseQty;
    }

    public void setPurchaseQty(Double purchaseQty) {
	this.purchaseQty = purchaseQty;
    }

    public Double getPurchaseAmt() {
	return this.purchaseAmt;
    }

    public void setPurchaseAmt(Double purchaseAmt) {
	this.purchaseAmt = purchaseAmt;
    }

    public Double getSalesQty() {
	return this.salesQty;
    }

    public void setSalesQty(Double salesQty) {
	this.salesQty = salesQty;
    }

    public Double getSalesAmt() {
	return this.salesAmt;
    }

    public void setSalesAmt(Double salesAmt) {
	this.salesAmt = salesAmt;
    }

    public Double getMovementQty() {
	return this.movementQty;
    }

    public void setMovementQty(Double movementQty) {
	this.movementQty = movementQty;
    }

    public Double getMovementAmt() {
	return this.movementAmt;
    }

    public void setMovementAmt(Double movementAmt) {
	this.movementAmt = movementAmt;
    }

    public Double getAdjustmentQty() {
	return this.adjustmentQty;
    }

    public void setAdjustmentQty(Double adjustmentQty) {
	this.adjustmentQty = adjustmentQty;
    }

    public Double getAdjustmentAmt() {
	return this.adjustmentAmt;
    }

    public void setAdjustmentAmt(Double adjustmentAmt) {
	this.adjustmentAmt = adjustmentAmt;
    }

    public Double getOtherQty() {
	return this.otherQty;
    }

    public void setOtherQty(Double otherQty) {
	this.otherQty = otherQty;
    }

    public Double getOtherAmt() {
	return this.otherAmt;
    }

    public void setOtherAmt(Double otherAmt) {
	this.otherAmt = otherAmt;
    }

    public Double getEndOnHandQty() {
	return this.endOnHandQty;
    }

    public void setEndOnHandQty(Double endOnHandQty) {
	this.endOnHandQty = endOnHandQty;
    }

    public Double getPosQty() {
	return this.posQty;
    }

    public void setPosQty(Double posQty) {
	this.posQty = posQty;
    }

    public Double getPosAmt() {
	return this.posAmt;
    }

    public void setPosAmt(Double posAmt) {
	this.posAmt = posAmt;
    }
}