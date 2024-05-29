package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemCurrentPriceView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemCurrentPriceView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1423460300005178148L;
    // Fields

    private Long priceId;
    private String itemCode;
    private String brandCode;
    private String itemCName;
    private String itemEName;
    private String description;
    private String typeCode;
    private String salesUnit;
    private Double salesRatio;
    private String purchaseUnit;
    private String currencyCode;
    private Double unitPrice;
    private String isTax;
    private String taxCode;
    private Date beginDate;
    private Double originalPrice;
    private String isComposeItem;
    private String isServiceItem;
    private String itemLevel;
    private String supplierItemCode;
    private Date releaseDate;
    private Date expiryDate;
    private Double foreignListPrice;
    private Double supplierQuotationPrice;
    private Double standardPurchaseCost;
    private String specLength;
    private String specWidth;
    private String specHeight;
    private String specWeight;
    private String category01;
    private String category02;
    private String category03;
    private String category04;
    private String category05;
    private String category06;
    private String category07;
    private String category08;
    private String category09;
    private String category10;
    private String category11;
    private String category12;
    private String category13;
    private String category14;
    private String category15;
    private String category16;
    private String category17;
    private String category18;
    private String category19;
    private String category20;
    
    private Double purchaseAmount;			// 進貨價格(t2成本) david 20100105 
    private String itemCategory;			// 業種子類 david 20100105 
    private String itemBrand;				// 商品品牌 david 20100105
    private String purchaseCurrencyCode;		// 採購幣別  david 20100106
    private Date priceLastUpdateDate;			// 最近調價日 david 20100715
    
    private Long indexNo;
    private String enableName;

    private String itemUnit;	// 非db欄位, 顯示單位中文
    private String priceType;	// 非db欄位, 顯示價格類別中文
    
    // Constructors

    /** default constructor */
    public ImItemCurrentPriceView() {
    }

    /** minimal constructor */
    public ImItemCurrentPriceView(Long priceId, String brandCode) {
	this.priceId = priceId;
	this.brandCode = brandCode;
    }

    /** full constructor */
    public ImItemCurrentPriceView(Long priceId, String itemCode,
	    String brandCode, String itemCName, String itemEName,
	    String description, String typeCode, String salesUnit,
	    Double salesRatio, String purchaseUnit, String currencyCode,
	    Double unitPrice, String isTax, String taxCode, Date beginDate,
	    Double originalPrice, String isComposeItem, String isServiceItem,
	    String itemLevel, String supplierItemCode, Date releaseDate,
	    Date expiryDate, Double foreignListPrice,
	    Double supplierQuotationPrice, Double standardPurchaseCost,
	    String specLength, String specWidth, String specHeight,
	    String specWeight, String category01, String category02,
	    String category03, String category04, String category05,
	    String category06, String category07, String category08,
	    String category09, String category10, String category11,
	    String category12, String category13, String category14,
	    String category15, String category16, String category17,
	    String category18, String category19, String category20) {
	super();
	this.priceId = priceId;
	this.itemCode = itemCode;
	this.brandCode = brandCode;
	this.itemCName = itemCName;
	this.itemEName = itemEName;
	this.description = description;
	this.typeCode = typeCode;
	this.salesUnit = salesUnit;
	this.salesRatio = salesRatio;
	this.purchaseUnit = purchaseUnit;
	this.currencyCode = currencyCode;
	this.unitPrice = unitPrice;
	this.isTax = isTax;
	this.taxCode = taxCode;
	this.beginDate = beginDate;
	this.originalPrice = originalPrice;
	this.isComposeItem = isComposeItem;
	this.isServiceItem = isServiceItem;
	this.itemLevel = itemLevel;
	this.supplierItemCode = supplierItemCode;
	this.releaseDate = releaseDate;
	this.expiryDate = expiryDate;
	this.foreignListPrice = foreignListPrice;
	this.supplierQuotationPrice = supplierQuotationPrice;
	this.standardPurchaseCost = standardPurchaseCost;
	this.specLength = specLength;
	this.specWidth = specWidth;
	this.specHeight = specHeight;
	this.specWeight = specWeight;
	this.category01 = category01;
	this.category02 = category02;
	this.category03 = category03;
	this.category04 = category04;
	this.category05 = category05;
	this.category06 = category06;
	this.category07 = category07;
	this.category08 = category08;
	this.category09 = category09;
	this.category10 = category10;
	this.category11 = category11;
	this.category12 = category12;
	this.category13 = category13;
	this.category14 = category14;
	this.category15 = category15;
	this.category16 = category16;
	this.category17 = category17;
	this.category18 = category18;
	this.category19 = category19;
	this.category20 = category20;
    }

    // Property accessors

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

    public Double getSalesRatio() {
	return this.salesRatio;
    }

    public void setSalesRatio(Double salesRatio) {
	this.salesRatio = salesRatio;
    }

    public String getPurchaseUnit() {
	return this.purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
	this.purchaseUnit = purchaseUnit;
    }

    public String getCurrencyCode() {
	return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
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

    public Double getOriginalPrice() {
	return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
	this.originalPrice = originalPrice;
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

    public String getItemLevel() {
	return this.itemLevel;
    }

    public void setItemLevel(String itemLevel) {
	this.itemLevel = itemLevel;
    }

    public String getSupplierItemCode() {
	return supplierItemCode;
    }

    public void setSupplierItemCode(String supplierItemCode) {
	this.supplierItemCode = supplierItemCode;
    }

    public Date getReleaseDate() {
	return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
	this.releaseDate = releaseDate;
    }

    public Date getExpiryDate() {
	return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
    }

    public Double getForeignListPrice() {
	return foreignListPrice;
    }

    public void setForeignListPrice(Double foreignListPrice) {
	this.foreignListPrice = foreignListPrice;
    }

    public Double getSupplierQuotationPrice() {
	return supplierQuotationPrice;
    }

    public void setSupplierQuotationPrice(Double supplierQuotationPrice) {
	this.supplierQuotationPrice = supplierQuotationPrice;
    }

    public Double getStandardPurchaseCost() {
	return standardPurchaseCost;
    }

    public void setStandardPurchaseCost(Double standardPurchaseCost) {
	this.standardPurchaseCost = standardPurchaseCost;
    }

    public String getSpecLength() {
	return this.specLength;
    }

    public void setSpecLength(String specLength) {
	this.specLength = specLength;
    }

    public String getSpecWidth() {
	return this.specWidth;
    }

    public void setSpecWidth(String specWidth) {
	this.specWidth = specWidth;
    }

    public String getSpecHeight() {
	return this.specHeight;
    }

    public void setSpecHeight(String specHeight) {
	this.specHeight = specHeight;
    }

    public String getSpecWeight() {
	return this.specWeight;
    }

    public void setSpecWeight(String specWeight) {
	this.specWeight = specWeight;
    }

    public String getCategory01() {
	return this.category01;
    }

    public void setCategory01(String category01) {
	this.category01 = category01;
    }

    public String getCategory02() {
	return this.category02;
    }

    public void setCategory02(String category02) {
	this.category02 = category02;
    }

    public String getCategory03() {
	return this.category03;
    }

    public void setCategory03(String category03) {
	this.category03 = category03;
    }

    public String getCategory04() {
	return this.category04;
    }

    public void setCategory04(String category04) {
	this.category04 = category04;
    }

    public String getCategory05() {
	return this.category05;
    }

    public void setCategory05(String category05) {
	this.category05 = category05;
    }

    public String getCategory06() {
	return this.category06;
    }

    public void setCategory06(String category06) {
	this.category06 = category06;
    }

    public String getCategory07() {
	return this.category07;
    }

    public void setCategory07(String category07) {
	this.category07 = category07;
    }

    public String getCategory08() {
	return this.category08;
    }

    public void setCategory08(String category08) {
	this.category08 = category08;
    }

    public String getCategory09() {
	return this.category09;
    }

    public void setCategory09(String category09) {
	this.category09 = category09;
    }

    public String getCategory10() {
	return this.category10;
    }

    public void setCategory10(String category10) {
	this.category10 = category10;
    }

    public String getCategory11() {
	return this.category11;
    }

    public void setCategory11(String category11) {
	this.category11 = category11;
    }

    public String getCategory12() {
	return this.category12;
    }

    public void setCategory12(String category12) {
	this.category12 = category12;
    }

    public String getCategory13() {
	return this.category13;
    }

    public void setCategory13(String category13) {
	this.category13 = category13;
    }

    public String getCategory14() {
	return this.category14;
    }

    public void setCategory14(String category14) {
	this.category14 = category14;
    }

    public String getCategory15() {
	return this.category15;
    }

    public void setCategory15(String category15) {
	this.category15 = category15;
    }

    public String getCategory16() {
	return this.category16;
    }

    public void setCategory16(String category16) {
	this.category16 = category16;
    }

    public String getCategory17() {
	return this.category17;
    }

    public void setCategory17(String category17) {
	this.category17 = category17;
    }

    public String getCategory18() {
	return this.category18;
    }

    public void setCategory18(String category18) {
	this.category18 = category18;
    }

    public String getCategory19() {
	return this.category19;
    }

    public void setCategory19(String category19) {
	this.category19 = category19;
    }

    public String getCategory20() {
	return this.category20;
    }

    public void setCategory20(String category20) {
	this.category20 = category20;
    }

    public Long getIndexNo(){
    	return indexNo;
    }
    
    public void setIndexNo(Long indexNo){
    	this.indexNo = indexNo;
    }
    
    public String getEnableName(){
    	return this.enableName;
    }
    
    public void setEnableName(String enableName){
    	this.enableName = enableName;
    }

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public Double getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getPurchaseCurrencyCode() {
		return purchaseCurrencyCode;
	}

	public void setPurchaseCurrencyCode(String purchaseCurrencyCode) {
		this.purchaseCurrencyCode = purchaseCurrencyCode;
	}

	public Date getPriceLastUpdateDate() {
	    return priceLastUpdateDate;
	}

	public void setPriceLastUpdateDate(Date priceLastUpdateDate) {
	    this.priceLastUpdateDate = priceLastUpdateDate;
	}

}