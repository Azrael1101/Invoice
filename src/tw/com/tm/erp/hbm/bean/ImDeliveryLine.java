package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImDeliveryLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDeliveryLine implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6795530781852871180L;
    // Fields
    private Long lineId;
    private ImDeliveryHead imDeliveryHead;
    private Long salesOrderId;
    private String itemCode;
    private String itemCName;
    private String warehouseCode;
    private String warehouseName;
    private String lotNo;
    private Double salesQuantity;
    private String originalWarehouseCode;
    private String originalLotNo;
    private Double originalSalesQuantity;
    private Double shipQuantity;
    private Double returnQuantity;
    private Double returnedQuantity;
    private Double returnableQuantity;
    private Double originalUnitPrice;
    private Double originalSalesAmount;
    private Double originalShipAmount;
    private Double originalReturnAmount;
    private String promotionCode;
    private String discountType;
    private Double discount;
    private String vipPromotionCode;
    private String vipDiscountType;
    private Double vipDiscount;
    private Double originalOnHandQty;
    private Double actualUnitPrice;
    private Double actualSalesAmount;
    private Double actualShipAmount;
    private Double actualReturnAmount;
    private Double discountRate;
    private Date scheduleShipDate;
    private String isTax;
    private String taxType;
    private Double taxRate;
    private Double taxAmount;
    private Double shipTaxAmount;
    private Double returnTaxAmount;
    private String depositCode;
    private String isUseDeposit;
    private String watchSerialNo;
    private String watchSerialNoPicker; //假欄位...給picker使用
    private String isComposeItem;
    private String isServiceItem;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String status;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
    private String importDeclNo;
    private Date importDeclDate;
    private String importDeclItemNo;
    private String importDeclType;
    private String importItemSn;
    private String importChooseItemNo;
    private String importBoxNo;
    private Double importNWght;
    private String importCurrencyCode;
    private Double importCost;
    private String importTaxType;
    private Double importExchangeRate;
    private Long importDeclSeq;
    private String packingList;
    private String boxVolume;
    private Double originalForeignUnitPrice;
    private Double actualForeignUnitPrice;
    private Double originalForeignSalesAmt;
    private Double actualForeignSalesAmt;
    private Double originalForeignShipAmt;
    private Double actualForeignShipAmt;
    private Double deductionForeignAmount;
    private Double deductionAmount;
    private String usedIdentification;
    private String usedCardId;
    private String usedCardType;
    private Double usedDiscountRate;
    private String itemDiscountType;
    private String allowMinusStock;
    private String allowWholeSale;
    private String salesUnit;
    private Double importTax;
    private Double goodsTax;
    private Double cigarWineTax;
    private Double healthTax;
    private Double businessTax;
    private String cigarWineRemark;
    private Double cigarWineQty;
    private Double taxationPcent;
    private Double taxUnitQty;
    private String OImportDeclNo;
    private Long OImportDeclSeq;
    private Double posActualSalesAmount;//記錄由POS轉入的實際銷售金額
    private String combineCode; //組合代號
    
    // Constructors
    
    /** default constructor */
    public ImDeliveryLine() {
    }

    // Property accessors

    public ImDeliveryLine(Long lineId, ImDeliveryHead imDeliveryHead,
	    Long salesOrderId, String itemCode, String itemCName,
	    String warehouseCode, String warehouseName, String lotNo,
	    Double salesQuantity, String originalWarehouseCode,
	    String originalLotNo, Double originalSalesQuantity,
	    Double shipQuantity, Double returnQuantity,
	    Double returnedQuantity, Double returnableQuantity,
	    Double originalUnitPrice, Double originalSalesAmount,
	    Double originalShipAmount, Double originalReturnAmount,
	    String promotionCode, String discountType, Double discount,
	    String vipPromotionCode, String vipDiscountType,
	    Double vipDiscount, Double originalOnHandQty,
	    Double actualUnitPrice, Double actualSalesAmount,
	    Double actualShipAmount, Double actualReturnAmount,
	    Double discountRate, Date scheduleShipDate, String isTax,
	    String taxType, Double taxRate, Double taxAmount,
	    Double shipTaxAmount, Double returnTaxAmount, String depositCode,
	    String isUseDeposit, String watchSerialNo, String isComposeItem,
	    String isServiceItem, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5, String status,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, Long indexNo, String isDeleteRecord,
	    String isLockRecord, String message, String importDeclNo,
	    Date importDeclDate, String importDeclItemNo,
	    String importDeclType, String importItemSn,
	    String importChooseItemNo, String importBoxNo, Double importNWght,
	    String importCurrencyCode, Double importCost, String importTaxType,
	    Double importExchangeRate, Long importDeclSeq, String packingList,
	    String boxVolume, Double originalForeignUnitPrice,
	    Double actualForeignUnitPrice, Double originalForeignSalesAmt,
	    Double actualForeignSalesAmt, Double originalForeignShipAmt,
	    Double actualForeignShipAmt, Double deductionForeignAmount,
	    Double deductionAmount, String usedIdentification,
	    String usedCardId, String usedCardType, Double usedDiscountRate,
	    String itemDiscountType, String combineCode) {
	super();
	this.lineId = lineId;
	this.imDeliveryHead = imDeliveryHead;
	this.salesOrderId = salesOrderId;
	this.itemCode = itemCode;
	this.itemCName = itemCName;
	this.warehouseCode = warehouseCode;
	this.warehouseName = warehouseName;
	this.lotNo = lotNo;
	this.salesQuantity = salesQuantity;
	this.originalWarehouseCode = originalWarehouseCode;
	this.originalLotNo = originalLotNo;
	this.originalSalesQuantity = originalSalesQuantity;
	this.shipQuantity = shipQuantity;
	this.returnQuantity = returnQuantity;
	this.returnedQuantity = returnedQuantity;
	this.returnableQuantity = returnableQuantity;
	this.originalUnitPrice = originalUnitPrice;
	this.originalSalesAmount = originalSalesAmount;
	this.originalShipAmount = originalShipAmount;
	this.originalReturnAmount = originalReturnAmount;
	this.promotionCode = promotionCode;
	this.discountType = discountType;
	this.discount = discount;
	this.vipPromotionCode = vipPromotionCode;
	this.vipDiscountType = vipDiscountType;
	this.vipDiscount = vipDiscount;
	this.originalOnHandQty = originalOnHandQty;
	this.actualUnitPrice = actualUnitPrice;
	this.actualSalesAmount = actualSalesAmount;
	this.actualShipAmount = actualShipAmount;
	this.actualReturnAmount = actualReturnAmount;
	this.discountRate = discountRate;
	this.scheduleShipDate = scheduleShipDate;
	this.isTax = isTax;
	this.taxType = taxType;
	this.taxRate = taxRate;
	this.taxAmount = taxAmount;
	this.shipTaxAmount = shipTaxAmount;
	this.returnTaxAmount = returnTaxAmount;
	this.depositCode = depositCode;
	this.isUseDeposit = isUseDeposit;
	this.watchSerialNo = watchSerialNo;
	this.isComposeItem = isComposeItem;
	this.isServiceItem = isServiceItem;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.status = status;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
	this.isDeleteRecord = isDeleteRecord;
	this.isLockRecord = isLockRecord;
	this.message = message;
	this.importDeclNo = importDeclNo;
	this.importDeclDate = importDeclDate;
	this.importDeclItemNo = importDeclItemNo;
	this.importDeclType = importDeclType;
	this.importItemSn = importItemSn;
	this.importChooseItemNo = importChooseItemNo;
	this.importBoxNo = importBoxNo;
	this.importNWght = importNWght;
	this.importCurrencyCode = importCurrencyCode;
	this.importCost = importCost;
	this.importTaxType = importTaxType;
	this.importExchangeRate = importExchangeRate;
	this.importDeclSeq = importDeclSeq;
	this.packingList = packingList;
	this.boxVolume = boxVolume;
	this.originalForeignUnitPrice = originalForeignUnitPrice;
	this.actualForeignUnitPrice = actualForeignUnitPrice;
	this.originalForeignSalesAmt = originalForeignSalesAmt;
	this.actualForeignSalesAmt = actualForeignSalesAmt;
	this.originalForeignShipAmt = originalForeignShipAmt;
	this.actualForeignShipAmt = actualForeignShipAmt;
	this.deductionForeignAmount = deductionForeignAmount;
	this.deductionAmount = deductionAmount;
	this.usedIdentification = usedIdentification;
	this.usedCardId = usedCardId;
	this.usedCardType = usedCardType;
	this.usedDiscountRate = usedDiscountRate;
	this.itemDiscountType = itemDiscountType;
	this.combineCode = combineCode;
    }

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public ImDeliveryHead getImDeliveryHead() {
	return this.imDeliveryHead;
    }

    public void setImDeliveryHead(ImDeliveryHead imDeliveryHead) {
	this.imDeliveryHead = imDeliveryHead;
    }

    public Long getSalesOrderId() {
	return this.salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
	this.salesOrderId = salesOrderId;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getItemCName() {
	return itemCName;
    }

    public void setItemCName(String itemCName) {
	this.itemCName = itemCName;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
	return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
	this.warehouseName = warehouseName;
    }

    public String getLotNo() {
	return this.lotNo;
    }

    public void setLotNo(String lotNo) {
	this.lotNo = lotNo;
    }

    public Double getSalesQuantity() {
	return this.salesQuantity;
    }

    public void setSalesQuantity(Double salesQuantity) {
	this.salesQuantity = salesQuantity;
    }

    public String getOriginalWarehouseCode() {
	return originalWarehouseCode;
    }

    public void setOriginalWarehouseCode(String originalWarehouseCode) {
	this.originalWarehouseCode = originalWarehouseCode;
    }

    public String getOriginalLotNo() {
	return originalLotNo;
    }

    public void setOriginalLotNo(String originalLotNo) {
	this.originalLotNo = originalLotNo;
    }

    public Double getOriginalSalesQuantity() {
	return originalSalesQuantity;
    }

    public void setOriginalSalesQuantity(Double originalSalesQuantity) {
	this.originalSalesQuantity = originalSalesQuantity;
    }

    public Double getShipQuantity() {
	return this.shipQuantity;
    }

    public void setShipQuantity(Double shipQuantity) {
	this.shipQuantity = shipQuantity;
    }

    public Double getReturnQuantity() {
	return returnQuantity;
    }

    public void setReturnQuantity(Double returnQuantity) {
	this.returnQuantity = returnQuantity;
    }

    public Double getReturnedQuantity() {
	return returnedQuantity;
    }

    public void setReturnedQuantity(Double returnedQuantity) {
	this.returnedQuantity = returnedQuantity;
    }

    public Double getReturnableQuantity() {
	return returnableQuantity;
    }

    public void setReturnableQuantity(Double returnableQuantity) {
	this.returnableQuantity = returnableQuantity;
    }

    public Double getOriginalUnitPrice() {
	return this.originalUnitPrice;
    }

    public void setOriginalUnitPrice(Double originalUnitPrice) {
	this.originalUnitPrice = originalUnitPrice;
    }

    public Double getOriginalSalesAmount() {
	return this.originalSalesAmount;
    }

    public void setOriginalSalesAmount(Double originalSalesAmount) {
	this.originalSalesAmount = originalSalesAmount;
    }

    public Double getOriginalShipAmount() {
	return originalShipAmount;
    }

    public void setOriginalShipAmount(Double originalShipAmount) {
	this.originalShipAmount = originalShipAmount;
    }

    public Double getOriginalReturnAmount() {
	return originalReturnAmount;
    }

    public void setOriginalReturnAmount(Double originalReturnAmount) {
	this.originalReturnAmount = originalReturnAmount;
    }

    public String getPromotionCode() {
	return this.promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
	this.promotionCode = promotionCode;
    }

    public String getDiscountType() {
	return this.discountType;
    }

    public void setDiscountType(String discountType) {
	this.discountType = discountType;
    }

    public Double getDiscount() {
	return this.discount;
    }

    public void setDiscount(Double discount) {
	this.discount = discount;
    }

    public String getVipPromotionCode() {
	return vipPromotionCode;
    }

    public void setVipPromotionCode(String vipPromotionCode) {
	this.vipPromotionCode = vipPromotionCode;
    }

    public String getVipDiscountType() {
	return vipDiscountType;
    }

    public void setVipDiscountType(String vipDiscountType) {
	this.vipDiscountType = vipDiscountType;
    }

    public Double getVipDiscount() {
	return vipDiscount;
    }

    public void setVipDiscount(Double vipDiscount) {
	this.vipDiscount = vipDiscount;
    }

    public Double getOriginalOnHandQty() {
	return originalOnHandQty;
    }

    public void setOriginalOnHandQty(Double originalOnHandQty) {
	this.originalOnHandQty = originalOnHandQty;
    }

    public Double getActualUnitPrice() {
	return this.actualUnitPrice;
    }

    public void setActualUnitPrice(Double actualUnitPrice) {
	this.actualUnitPrice = actualUnitPrice;
    }

    public Double getActualSalesAmount() {
	return this.actualSalesAmount;
    }

    public void setActualSalesAmount(Double actualSalesAmount) {
	this.actualSalesAmount = actualSalesAmount;
    }

    public Double getActualShipAmount() {
	return actualShipAmount;
    }

    public void setActualShipAmount(Double actualShipAmount) {
	this.actualShipAmount = actualShipAmount;
    }

    public Double getActualReturnAmount() {
	return actualReturnAmount;
    }

    public void setActualReturnAmount(Double actualReturnAmount) {
	this.actualReturnAmount = actualReturnAmount;
    }

    public Double getDiscountRate() {
	return this.discountRate;
    }

    public void setDiscountRate(Double discountRate) {
	this.discountRate = discountRate;
    }

    public Date getScheduleShipDate() {
	return this.scheduleShipDate;
    }

    public void setScheduleShipDate(Date scheduleShipDate) {
	this.scheduleShipDate = scheduleShipDate;
    }

    public String getIsTax() {
	return this.isTax;
    }

    public void setIsTax(String isTax) {
	this.isTax = isTax;
    }

    public String getTaxType() {
	return this.taxType;
    }

    public void setTaxType(String taxType) {
	this.taxType = taxType;
    }

    public Double getTaxRate() {
	return this.taxRate;
    }

    public void setTaxRate(Double taxRate) {
	this.taxRate = taxRate;
    }

    public Double getTaxAmount() {
	return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
	this.taxAmount = taxAmount;
    }

    public Double getShipTaxAmount() {
	return shipTaxAmount;
    }

    public void setShipTaxAmount(Double shipTaxAmount) {
	this.shipTaxAmount = shipTaxAmount;
    }

    public Double getReturnTaxAmount() {
	return returnTaxAmount;
    }

    public void setReturnTaxAmount(Double returnTaxAmount) {
	this.returnTaxAmount = returnTaxAmount;
    }

    public String getDepositCode() {
	return this.depositCode;
    }

    public void setDepositCode(String depositCode) {
	this.depositCode = depositCode;
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

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getImportDeclNo() {
	return importDeclNo;
    }

    public void setImportDeclNo(String importDeclNo) {
	this.importDeclNo = importDeclNo;
    }

    public Date getImportDeclDate() {
	return importDeclDate;
    }

    public void setImportDeclDate(Date importDeclDate) {
	this.importDeclDate = importDeclDate;
    }

    public String getImportDeclItemNo() {
	return importDeclItemNo;
    }

    public void setImportDeclItemNo(String importDeclItemNo) {
	this.importDeclItemNo = importDeclItemNo;
    }

    public String getImportDeclType() {
	return importDeclType;
    }

    public void setImportDeclType(String importDeclType) {
	this.importDeclType = importDeclType;
    }

    public String getImportItemSn() {
	return importItemSn;
    }

    public void setImportItemSn(String importItemSn) {
	this.importItemSn = importItemSn;
    }

    public String getImportChooseItemNo() {
	return importChooseItemNo;
    }

    public void setImportChooseItemNo(String importChooseItemNo) {
	this.importChooseItemNo = importChooseItemNo;
    }

    public String getImportBoxNo() {
	return importBoxNo;
    }

    public void setImportBoxNo(String importBoxNo) {
	this.importBoxNo = importBoxNo;
    }

    public Double getImportNWght() {
	return importNWght;
    }

    public void setImportNWght(Double importNWght) {
	this.importNWght = importNWght;
    }

    public String getImportCurrencyCode() {
	return importCurrencyCode;
    }

    public void setImportCurrencyCode(String importCurrencyCode) {
	this.importCurrencyCode = importCurrencyCode;
    }

    public Double getImportCost() {
	return importCost;
    }

    public void setImportCost(Double importCost) {
	this.importCost = importCost;
    }

    public String getImportTaxType() {
	return importTaxType;
    }

    public void setImportTaxType(String importTaxType) {
	this.importTaxType = importTaxType;
    }

    public Double getImportExchangeRate() {
	return importExchangeRate;
    }

    public void setImportExchangeRate(Double importExchangeRate) {
	this.importExchangeRate = importExchangeRate;
    }

    public Long getImportDeclSeq() {
	return importDeclSeq;
    }

    public void setImportDeclSeq(Long importDeclSeq) {
	this.importDeclSeq = importDeclSeq;
    }

    public String getPackingList() {
	return packingList;
    }

    public void setPackingList(String packingList) {
	this.packingList = packingList;
    }

    public String getBoxVolume() {
	return boxVolume;
    }

    public void setBoxVolume(String boxVolume) {
	this.boxVolume = boxVolume;
    }

    public Double getOriginalForeignUnitPrice() {
	return originalForeignUnitPrice;
    }

    public void setOriginalForeignUnitPrice(Double originalForeignUnitPrice) {
	this.originalForeignUnitPrice = originalForeignUnitPrice;
    }

    public Double getActualForeignUnitPrice() {
	return actualForeignUnitPrice;
    }

    public void setActualForeignUnitPrice(Double actualForeignUnitPrice) {
	this.actualForeignUnitPrice = actualForeignUnitPrice;
    }

    public Double getOriginalForeignSalesAmt() {
	return originalForeignSalesAmt;
    }

    public void setOriginalForeignSalesAmt(Double originalForeignSalesAmt) {
	this.originalForeignSalesAmt = originalForeignSalesAmt;
    }

    public Double getActualForeignSalesAmt() {
	return actualForeignSalesAmt;
    }

    public void setActualForeignSalesAmt(Double actualForeignSalesAmt) {
	this.actualForeignSalesAmt = actualForeignSalesAmt;
    }

    public Double getOriginalForeignShipAmt() {
	return originalForeignShipAmt;
    }

    public void setOriginalForeignShipAmt(Double originalForeignShipAmt) {
	this.originalForeignShipAmt = originalForeignShipAmt;
    }

    public Double getActualForeignShipAmt() {
	return actualForeignShipAmt;
    }

    public void setActualForeignShipAmt(Double actualForeignShipAmt) {
	this.actualForeignShipAmt = actualForeignShipAmt;
    }

    public Double getDeductionForeignAmount() {
	return deductionForeignAmount;
    }

    public void setDeductionForeignAmount(Double deductionForeignAmount) {
	this.deductionForeignAmount = deductionForeignAmount;
    }

    public Double getDeductionAmount() {
	return deductionAmount;
    }

    public void setDeductionAmount(Double deductionAmount) {
	this.deductionAmount = deductionAmount;
    }
    
    public String getUsedIdentification() {
	return usedIdentification;
    }

    public void setUsedIdentification(String usedIdentification) {
	this.usedIdentification = usedIdentification;
    }

    public String getUsedCardId() {
	return usedCardId;
    }

    public void setUsedCardId(String usedCardId) {
	this.usedCardId = usedCardId;
    }

    public String getUsedCardType() {
	return usedCardType;
    }

    public void setUsedCardType(String usedCardType) {
	this.usedCardType = usedCardType;
    }

    public Double getUsedDiscountRate() {
	return usedDiscountRate;
    }

    public void setUsedDiscountRate(Double usedDiscountRate) {
	this.usedDiscountRate = usedDiscountRate;
    }

    public String getItemDiscountType() {
	return itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
	this.itemDiscountType = itemDiscountType;
    }

	public String getAllowMinusStock() {
		return allowMinusStock;
	}

	public void setAllowMinusStock(String allowMinusStock) {
		this.allowMinusStock = allowMinusStock;
	}

	public String getAllowWholeSale() {
		return allowWholeSale;
	}

	public void setAllowWholeSale(String allowWholeSale) {
		this.allowWholeSale = allowWholeSale;
	}

	public String getWatchSerialNoPicker() {
		return watchSerialNoPicker;
	}

	public void setWatchSerialNoPicker(String watchSerialNoPicker) {
		this.watchSerialNoPicker = watchSerialNoPicker;
	}

	public String getSalesUnit() {
		return salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public Double getImportTax() {
		return importTax;
	}

	public void setImportTax(Double importTax) {
		this.importTax = importTax;
	}

	public Double getGoodsTax() {
		return goodsTax;
	}

	public void setGoodsTax(Double goodsTax) {
		this.goodsTax = goodsTax;
	}

	public Double getCigarWineTax() {
		return cigarWineTax;
	}

	public void setCigarWineTax(Double cigarWineTax) {
		this.cigarWineTax = cigarWineTax;
	}

	public Double getHealthTax() {
		return healthTax;
	}

	public void setHealthTax(Double healthTax) {
		this.healthTax = healthTax;
	}

	public Double getBusinessTax() {
		return businessTax;
	}

	public void setBusinessTax(Double businessTax) {
		this.businessTax = businessTax;
	}

	public String getCigarWineRemark() {
		return cigarWineRemark;
	}

	public void setCigarWineRemark(String cigarWineRemark) {
		this.cigarWineRemark = cigarWineRemark;
	}

	public Double getCigarWineQty() {
		return cigarWineQty;
	}

	public void setCigarWineQty(Double cigarWineQty) {
		this.cigarWineQty = cigarWineQty;
	}

	public Double getTaxationPcent() {
		return taxationPcent;
	}

	public void setTaxationPcent(Double taxationPcent) {
		this.taxationPcent = taxationPcent;
	}

	public Double getTaxUnitQty() {
		return taxUnitQty;
	}

	public void setTaxUnitQty(Double taxUnitQty) {
		this.taxUnitQty = taxUnitQty;
	}

	public String getOImportDeclNo() {
		return OImportDeclNo;
	}

	public void setOImportDeclNo(String importDeclNo) {
		OImportDeclNo = importDeclNo;
	}

	public Long getOImportDeclSeq() {
		return OImportDeclSeq;
	}

	public void setOImportDeclSeq(Long importDeclSeq) {
		OImportDeclSeq = importDeclSeq;
	}

	public Double getPosActualSalesAmount() {
	    return posActualSalesAmount;
	}

	public void setPosActualSalesAmount(Double posActualSalesAmount) {
	    this.posActualSalesAmount = posActualSalesAmount;
	}

	public String getCombineCode() {
		return combineCode;
	}

	public void setCombineCode(String combineCode) {
		this.combineCode = combineCode;
	}
}