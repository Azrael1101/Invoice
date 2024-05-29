package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PoPurchaseOrderLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoPurchaseOrderLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7940654128506745916L;
	private Long lineId; // PK
	private PoPurchaseOrderHead poPurchaseOrderHead; // SO_SALES_ORDER_HEAD.HEAD_ID
	private Long lineNo; // 序號
	private String itemCode; // 商品代號
	private String warehouseCode; // 出貨倉庫代號

	private Double unitPrice = new Double(0); // 售價
	private Double lastForeignUnitCost = new Double(0); // 上次外幣進貨價格
	private Double lastLocalUnitCost = new Double(0); // 上次台幣進貨價格
	private Double foreignUnitCost = null; // 外幣進價
	private Double localUnitCost = new Double(0); // 標準單位成本
	private Double foreignPurchaseAmount = new Double(0); // 外幣進貨金額
	private Double localPurchaseAmount = new Double(0); // 本幣進貨金額

	private Double quantity = new Double(0); // 數量
	private Double receiptedQuantity = new Double(0); // 己到貨數量(已核銷數量)
	// 實際應到貨數量(預設值帶 採購數量,如果這個數量=RECEIPTED_QUANTITY就結案)
	private Double actualPurchaseQuantity = new Double(0);

	private Date scheduleReceiptDate; // 預計到貨日
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String status; // Line Status
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private Double diffQty = new Double(0);
	private String itemCName;
	private Double predictionQty = new Double(0);
	private Double stockOnHandQty = new Double(0);
	private Double unitPriceAmount = new Double(0);
	private String isDeleteRecord = "0" ; //是否被刪除 1  表示要被移除
	private String isLockRecord = "0" ; //是否被鎖定 1  表示鎖定
	private String message ; //line 訊息的顯示
	
	private String onHandQty = "0";
	private Date nextPriceAdjustDate; 	// 下次變價日 : 2009.09.23 arthur
	private Double nextAdjustPrice; 	// 下次變價日 : 2009.09.23 arthur
	private String remark;			// 2009.09.23 arthur
	private String itemBrand;		// 2009.09.23 arthur
	private String supplierItemCode;	// 2009.09.23 arthur
	private Double margin;			// 2009.09.24 arthur
	private Double itemMargin;			// 2009.09.24 arthur
	private Double maxPurchaseQuantity;	// 2009.09.24 arthur
	private Double minPurchaseQuantity;	// 2009.09.24 arthur
	private Long fiBuegetLineId;		// fiBudgetLine.lineId 註記扣除預算的 line, 預防 user 變更相關資料  20091022 arthur
    private String category02;		// 非DB欄位,中類
    private String category02Name;	// 非DB欄位,中類名稱
    private Double outstandQuantity; //未交量
    private Double outstandAmount; //未交量金額
    private Double returnedQuantity; //已退還量
    private Double returnedAmount; //已退還量金額
    private String purchaseUnit;		//單位
    
    
	public Double getMargin() {
	    return margin;
	}

	public void setMargin(Double margin) {
	    this.margin = margin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public Double getStockOnHandQty() {
		return stockOnHandQty;
	}

	public void setStockOnHandQty(Double stockOnHandQty) {
		this.stockOnHandQty = stockOnHandQty;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/** default constructor */
	public PoPurchaseOrderLine() {
	}

	/** minimal constructor */
	public PoPurchaseOrderLine(Long lineId) {
		this.lineId = lineId;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public PoPurchaseOrderHead getPoPurchaseOrderHead() {
		return this.poPurchaseOrderHead;
	}

	public void setPoPurchaseOrderHead(PoPurchaseOrderHead poPurchaseOrderHead) {
		this.poPurchaseOrderHead = poPurchaseOrderHead;
	}

	public Long getLineNo() {
		return this.lineNo;
	}

	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Double getLastForeignUnitCost() {
		return this.lastForeignUnitCost;
	}

	public void setLastForeignUnitCost(Double lastForeignUnitCost) {
		this.lastForeignUnitCost = lastForeignUnitCost;
	}

	public Double getLastLocalUnitCost() {
		return this.lastLocalUnitCost;
	}

	public void setLastLocalUnitCost(Double lastLocalUnitCost) {
		this.lastLocalUnitCost = lastLocalUnitCost;
	}

	public Double getForeignUnitCost() {
		return this.foreignUnitCost;
	}

	public void setForeignUnitCost(Double foreignUnitCost) {
		this.foreignUnitCost = foreignUnitCost;
	}

	public Double getLocalUnitCost() {
		return this.localUnitCost;
	}

	public void setLocalUnitCost(Double localUnitCost) {
		this.localUnitCost = localUnitCost;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getForeignPurchaseAmount() {
		return this.foreignPurchaseAmount;
	}

	public void setForeignPurchaseAmount(Double foreignPurchaseAmount) {
		this.foreignPurchaseAmount = foreignPurchaseAmount;
	}

	public Double getLocalPurchaseAmount() {
		return this.localPurchaseAmount;
	}

	public void setLocalPurchaseAmount(Double localPurchaseAmount) {
		this.localPurchaseAmount = localPurchaseAmount;
	}

	public Double getActualPurchaseQuantity() {
		return this.actualPurchaseQuantity;
	}

	public void setActualPurchaseQuantity(Double actualPurchaseQuantity) {
		this.actualPurchaseQuantity = actualPurchaseQuantity;
	}

	public Date getScheduleReceiptDate() {
		return this.scheduleReceiptDate;
	}

	public void setScheduleReceiptDate(Date scheduleReceiptDate) {
		this.scheduleReceiptDate = scheduleReceiptDate;
	}

	public Double getReceiptedQuantity() {
		return this.receiptedQuantity;
	}

	public void setReceiptedQuantity(Double receiptedQuantity) {
		this.receiptedQuantity = receiptedQuantity;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public Double getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(Double diffQty) {
		this.diffQty = diffQty;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public Double getPredictionQty() {
		return predictionQty;
	}

	public void setPredictionQty(Double predictionQty) {
		this.predictionQty = predictionQty;
	}

	public Double getUnitPriceAmount() {
		return unitPriceAmount;
	}

	public void setUnitPriceAmount(Double unitPriceAmount) {
		this.unitPriceAmount = unitPriceAmount;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(String onHandQty) {
		this.onHandQty = onHandQty;
	}
	
	public Date getNextPriceAdjustDate() {
		return this.nextPriceAdjustDate;
	}

	public void setNextPriceAdjustDate(Date nextPriceAdjustDate) {
		this.nextPriceAdjustDate = nextPriceAdjustDate;
	}
	
	public Double getNextAdjustPrice() {
		return nextAdjustPrice;
	}

	public void setNextAdjustPrice(Double nextAdjustPrice) {
		this.nextAdjustPrice = nextAdjustPrice;
	}
	
	public String getItemBrand() {
		return this.itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}
	
	public String getSupplierItemCode() {
		return this.supplierItemCode;
	}

	public void setSupplierItemCode(String supplierItemCode) {
		this.supplierItemCode = supplierItemCode;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getMaxPurchaseQuantity() {
	    return maxPurchaseQuantity;
	}

	public void setMaxPurchaseQuantity(Double maxPurchaseQuantity) {
	    this.maxPurchaseQuantity = maxPurchaseQuantity;
	}

	public Double getMinPurchaseQuantity() {
	    return minPurchaseQuantity;
	}

	public void setMinPurchaseQuantity(Double minPurchaseQuantity) {
	    this.minPurchaseQuantity = minPurchaseQuantity;
	}

	public Long getFiBuegetLineId() {
	    return fiBuegetLineId;
	}

	public void setFiBuegetLineId(Long fiBuegetLineId) {
	    this.fiBuegetLineId = fiBuegetLineId;
	}

	public String getCategory02() {
		return category02;
	}

	public void setCategory02(String category02) {
		this.category02 = category02;
	}

	public String getCategory02Name() {
		return category02Name;
	}

	public void setCategory02Name(String category02Name) {
		this.category02Name = category02Name;
	}

	public Double getItemMargin() {
		return itemMargin;
	}

	public void setItemMargin(Double itemMargin) {
		this.itemMargin = itemMargin;
	}

	public Double getOutstandAmount() {
		return outstandAmount;
	}

	public void setOutstandAmount(Double outstandAmount) {
		this.outstandAmount = outstandAmount;
	}

	public Double getOutstandQuantity() {
		return outstandQuantity;
	}

	public void setOutstandQuantity(Double outstandQuantity) {
		this.outstandQuantity = outstandQuantity;
	}

	public Double getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Double returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public Double getReturnedAmount() {
		return returnedAmount;
	}

	public void setReturnedAmount(Double returnedAmount) {
		this.returnedAmount = returnedAmount;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	/*
	 * public Double getDiffReason() { return diffReason; }
	 * 
	 * public void setDiffReason(Double diffReason) { this.diffReason =
	 * diffReason; }
	 */
}