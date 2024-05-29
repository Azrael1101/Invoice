package tw.com.tm.erp.hbm.bean;

import java.util.Date;

import tw.com.tm.erp.utils.CommonUtils;

/**
 * ImPriceList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPriceList implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 4331410841311886212L;
    private Long lineId;
    private ImPriceAdjustment imPriceAdjustment;
    private Long priceId;
    private String itemCode;
    private String itemName;
    private String itemCName;
    private String typeCode;
    private String currencyCode;
    private Double foreignCost;
    private Double localCost;

    private String category01;		// 非DB欄位,存放系列 category13
    private String category02;		// 非DB欄位,中類
    private String category02Name;	// 非DB欄位,中類名稱
    private String category03;		// 非DB欄位,
    private String salesUnit;		// 非DB欄位,

    private Double originalGrossRate; // 非DB欄位,毛利率(舊)  for T2 變價單
    private Double grossRate;		// 非DB欄位,毛利率(新) for T2 變價單, 在定價單叫毛利率 
    private Double totalStock;		// 非DB欄位, 總庫存數量
    private Double unitPrice;
    private Double standardPurchaseCost;
    private String isTax;
    private String taxCode;
    private Date beginDate;
    private Double originalPrice;	// 標準售價(舊)
    private Long composeItemQuantity;
    private String status;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;

    private String originalCurrencyCode; 	// 原幣幣別(舊)
    private Double originalForeignCost; 	// 原幣成本(舊)
    private Double originalQuotationPrice;
    private Double newQuotationPrice;
    private Double grossProfitRate;	// 毛利率差異
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
  
    private Double exchangeRate;		// 匯率(新)
    private Double originalExchangeRate;	// 匯率(舊)
    
    private Double costRate;
    // Constructors
    /** default constructor */
    public ImPriceList() {
    }

    /** minimal constructor */
    public ImPriceList(Long lineId) {
	this.lineId = lineId;
    }

    /** full constructor */
    public ImPriceList(Long lineId, ImPriceAdjustment imPriceAdjustment,
	    Long priceId, String itemCode, String itemName, String itemCName,
	    String typeCode, String currencyCode, Double foreignCost,
	    Double localCost, String category01, String category02,
	    String category03, String salesUnit, Double unitPrice,
	    Double standardPurchaseCost, String isTax, String taxCode,
	    Date beginDate, Double originalPrice, Long composeItemQuantity,
	    String status, String reserve1, String reserve2, String reserve3,
	    String reserve4, String reserve5, String createdBy,
	    Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
	    Long indexNo, String isDeleteRecord, String isLockRecord,
	    String message, Double costRate) {
	super();
	this.lineId = lineId;
	this.imPriceAdjustment = imPriceAdjustment;
	this.priceId = priceId;
	this.itemCode = itemCode;
	this.itemName = itemName;
	this.itemCName = itemCName;
	this.typeCode = typeCode;
	this.currencyCode = currencyCode;
	this.foreignCost = foreignCost;
	this.localCost = localCost;
	this.category01 = category01;
	this.category02 = category02;
	this.category03 = category03;
	this.salesUnit = salesUnit;
	this.unitPrice = unitPrice;
	this.standardPurchaseCost = standardPurchaseCost;
	this.isTax = isTax;
	this.taxCode = taxCode;
	this.beginDate = beginDate;
	this.originalPrice = originalPrice;
	this.composeItemQuantity = composeItemQuantity;
	this.status = status;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
	this.isDeleteRecord = isDeleteRecord;
	this.isLockRecord = isLockRecord;
	this.message = message;
	this.costRate = costRate;
    }

    // Property accessors

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public ImPriceAdjustment getImPriceAdjustment() {
	return this.imPriceAdjustment;
    }

    public void setImPriceAdjustment(ImPriceAdjustment imPriceAdjustment) {
	this.imPriceAdjustment = imPriceAdjustment;
    }

    public Long getPriceId() {
	return this.priceId;
    }

    public void setPriceId(Long priceId) {
	this.priceId = priceId;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public String getItemName() {
	return itemName;
    }

    public String getItemCName() {
	return itemCName;
    }

    public void setItemCName(String itemCName) {
	this.itemCName = itemCName;
    }

    public String getTypeCode() {
	return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
	this.typeCode = typeCode;
    }

    public String getCurrencyCode() {
	return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
    }

    public Double getForeignCost() {
	return foreignCost;
    }

    public void setForeignCost(Double foreignCost) {
	this.foreignCost = foreignCost;
    }

    public Double getLocalCost() {
	return localCost;
    }

    public void setLocalCost(Double localCost) {
	this.localCost = localCost;
    }

    public String getCategory01() {
	return category01;
    }

    public void setCategory01(String category01) {
	this.category01 = category01;
    }

    public String getCategory02() {
	return category02;
    }

    public void setCategory02(String category02) {
	this.category02 = category02;
    }

    public String getCategory03() {
	return category03;
    }

    public void setCategory03(String category03) {
	this.category03 = category03;
    }

    public void setSalesUnit(String salesUnit) {
	this.salesUnit = salesUnit;
    }

    public String getSalesUnit() {
	return salesUnit;
    }

    public Double getUnitPrice() {
	return this.unitPrice;
    }

    public void setUnitPrice(Double unitPriceitPrice) {
	this.unitPrice = unitPriceitPrice;
    }

    public void setStandardPurchaseCost(Double standardPurchaseCost) {
	this.standardPurchaseCost = standardPurchaseCost;
    }

    public Double getStandardPurchaseCost() {
	return standardPurchaseCost;
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

    public Long getComposeItemQuantity() {
	return this.composeItemQuantity;
    }

    public void setComposeItemQuantity(Long composeItemQuantity) {
	this.composeItemQuantity = composeItemQuantity;
    }

    public Double getOriginalPrice() {
	return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
	this.originalPrice = originalPrice;
    }

    public String getStatus() {
	return this.status;
    }

    public void setStatus(String status) {
	this.status = status;
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

    public Long getIndexNo() {
	return this.indexNo;
    }

    public void setIndexNo(Long indexNo) {
	this.indexNo = indexNo;
    }

    /*
     * public String getCostRate() { java.text.DecimalFormat df =new
     * java.text.DecimalFormat("#.00"); return
     * df.format(localCost/unitPrice*100)+"%"; }
     * 
     */

    public Double getCostRate() {
	if(unitPrice == null){
		setUnitPrice(0D);
	}	
	if( unitPrice == 0D){
	    return 100D;
	}else if(localCost != null && localCost != 0D && unitPrice != null && unitPrice != 0D){
	    return CommonUtils.round(localCost / unitPrice * 100, 2);
	}else{
	    return 0D;
	}
    }

    public void setCostRate(Double costRate) {
	this.costRate = costRate;
    }

    public Double getOriginalQuotationPrice() {
	return originalQuotationPrice;
    }

    public void setOriginalQuotationPrice(Double originalQuotationPrice) {
	this.originalQuotationPrice = originalQuotationPrice;
    }

    public Double getNewQuotationPrice() {
	return newQuotationPrice;
    }

    public void setNewQuotationPrice(Double newQuotationPrice) {
	this.newQuotationPrice = newQuotationPrice;
    }

    public Double getGrossProfitRate() {
	return grossProfitRate;
    }

    public void setGrossProfitRate(Double grossProfitRate) {
	this.grossProfitRate = grossProfitRate;
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

	public Double getGrossRate() {
		return grossRate;
	}

	public void setGrossRate(Double grossRate) {
		this.grossRate = grossRate;
	}

	public Double getOriginalForeignCost() {
		return originalForeignCost;
	}

	public void setOriginalForeignCost(Double originalForeignCost) {
		this.originalForeignCost = originalForeignCost;
	}

	public Double getOriginalGrossRate() {
		return originalGrossRate;
	}

	public void setOriginalGrossRate(Double originalGrossRate) {
		this.originalGrossRate = originalGrossRate;
	}

	public Double getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Double totalStock) {
		this.totalStock = totalStock;
	}

	public String getOriginalCurrencyCode() {
		return originalCurrencyCode;
	}

	public void setOriginalCurrencyCode(String originalCurrencyCode) {
		this.originalCurrencyCode = originalCurrencyCode;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCategory02Name() {
	    return category02Name;
	}

	public void setCategory02Name(String category02Name) {
	    this.category02Name = category02Name;
	}

	public Double getOriginalExchangeRate() {
	    return originalExchangeRate;
	}

	public void setOriginalExchangeRate(Double originalExchangeRate) {
	    this.originalExchangeRate = originalExchangeRate;
	}

}