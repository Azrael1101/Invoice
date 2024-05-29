package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemPriceOnHandView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemPriceOnHandView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3315241675112030712L;
    // Fields
    private Long id;
    private String itemCode;
    private String brandCode;
    private String itemCName;
    private String itemEName;
    private String isComposeItem;
    private String isServiceItem;
    private String description;
    private String typeCode;
    private String salesUnit;
    private Long salesRatio;
    private String purchaseUnit;
    private Double unitPrice;
    private String isTax;
    private String taxCode;
    private Date beginDate;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseManager;
    private String lotNo;
    private Double stockOnHandQty;
    private Double outUncommitQty;
    private Double inUncommitQty;
    private Double moveUncommitQty;
    private Double otherUncommitQty;
    private Double currentOnHandQty;

    // Constructors

    /** default constructor */
    public ImItemPriceOnHandView() {
    }

    /** minimal constructor */
    public ImItemPriceOnHandView(String brandCode, String warehouseCode,
	    String lotNo) {
	this.brandCode = brandCode;
	this.warehouseCode = warehouseCode;
	this.lotNo = lotNo;
    }

    /** full constructor */
    public ImItemPriceOnHandView(Long id, String itemCode, String brandCode,
	    String itemCName, String itemEName, String isComposeItem,
	    String isServiceItem, String description, String typeCode,
	    String salesUnit, Long salesRatio, String purchaseUnit,
	    Double unitPrice, String isTax, String taxCode, Date beginDate,
	    String warehouseCode, String warehouseName,
	    String warehouseManager, String lotNo, Double stockOnHandQty,
	    Double outUncommitQty, Double inUncommitQty,
	    Double moveUncommitQty, Double otherUncommitQty,
	    Double currentOnHandQty) {
	this.id = id;
	this.itemCode = itemCode;
	this.brandCode = brandCode;
	this.itemCName = itemCName;
	this.itemEName = itemEName;
	this.isComposeItem = isComposeItem;
	this.isServiceItem = isServiceItem;
	this.description = description;
	this.typeCode = typeCode;
	this.salesUnit = salesUnit;
	this.salesRatio = salesRatio;
	this.purchaseUnit = purchaseUnit;
	this.unitPrice = unitPrice;
	this.isTax = isTax;
	this.taxCode = taxCode;
	this.beginDate = beginDate;
	this.warehouseCode = warehouseCode;
	this.warehouseName = warehouseName;
	this.warehouseManager = warehouseManager;
	this.lotNo = lotNo;
	this.stockOnHandQty = stockOnHandQty;
	this.outUncommitQty = outUncommitQty;
	this.inUncommitQty = inUncommitQty;
	this.moveUncommitQty = moveUncommitQty;
	this.otherUncommitQty = otherUncommitQty;
	this.currentOnHandQty = currentOnHandQty;
    }

    // Property accessors

    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getItemCName() {
	return this.itemCName;
    }

    public void setItemCName(String itemCName) {
	this.itemCName = itemCName;
    }

    public String getItemEName() {
	return this.itemEName;
    }

    public void setItemEName(String itemEName) {
	this.itemEName = itemEName;
    }

    public String getIsComposeItem() {
	return this.isComposeItem;
    }

    public void setIsComposeItem(String isComposeItem) {
	this.isComposeItem = isComposeItem;
    }

    public String getIsServiceItem() {
	return isServiceItem;
    }

    public void setIsServiceItem(String isServiceItem) {
	this.isServiceItem = isServiceItem;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTypeCode() {
	return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
	this.typeCode = typeCode;
    }

    public String getSalesUnit() {
	return this.salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
	this.salesUnit = salesUnit;
    }

    public Long getSalesRatio() {
	return this.salesRatio;
    }

    public void setSalesRatio(Long salesRatio) {
	this.salesRatio = salesRatio;
    }

    public String getPurchaseUnit() {
	return this.purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
	this.purchaseUnit = purchaseUnit;
    }

    public Double getUnitPrice() {
	return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
	this.unitPrice = unitPrice;
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

    public Date getBeginDate() {
	return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
	return this.warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
	this.warehouseName = warehouseName;
    }

    public String getWarehouseManager() {
	return this.warehouseManager;
    }

    public void setWarehouseManager(String warehouseManager) {
	this.warehouseManager = warehouseManager;
    }

    public String getLotNo() {
	return this.lotNo;
    }

    public void setLotNo(String lotNo) {
	this.lotNo = lotNo;
    }

    public Double getStockOnHandQty() {
	return this.stockOnHandQty;
    }

    public void setStockOnHandQty(Double stockOnHandQty) {
	this.stockOnHandQty = stockOnHandQty;
    }

    public Double getOutUncommitQty() {
	return this.outUncommitQty;
    }

    public void setOutUncommitQty(Double outUncommitQty) {
	this.outUncommitQty = outUncommitQty;
    }

    public Double getInUncommitQty() {
	return this.inUncommitQty;
    }

    public void setInUncommitQty(Double inUncommitQty) {
	this.inUncommitQty = inUncommitQty;
    }

    public Double getMoveUncommitQty() {
	return this.moveUncommitQty;
    }

    public void setMoveUncommitQty(Double moveUncommitQty) {
	this.moveUncommitQty = moveUncommitQty;
    }

    public Double getOtherUncommitQty() {
	return this.otherUncommitQty;
    }

    public void setOtherUncommitQty(Double otherUncommitQty) {
	this.otherUncommitQty = otherUncommitQty;
    }

    public Double getCurrentOnHandQty() {
	return this.currentOnHandQty;
    }

    public void setCurrentOnHandQty(Double currentOnHandQty) {
	this.currentOnHandQty = currentOnHandQty;
    }
}