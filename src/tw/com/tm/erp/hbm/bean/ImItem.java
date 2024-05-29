package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItem implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8517791456888631788L;
	private Long itemId;
	private String itemCode;
	private String brandCode;
	private String itemCName;
	private String itemEName;
	private String itemLevel;
	private String lotControl;
	private String supplierItemCode;	
	private Date releaseDate;
	private Date expiryDate;
	private Double foreignListPrice;
	private Double supplierQuotationPrice;
	private Double standardPurchaseCost;
	private String isComposeItem;
	private String isServiceItem;
	private String specLength;
	private String specWidth;
	private String specHeight;
	private String specWeight;
	private Long salesRatio;
	private String salesUnit;
	private String purchaseUnit;
	private Long purchaseRatio;
	private Long boxCapacity;
	private String description;
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
	private String enable;
	private String accountCode;
	private String categoryType;
	private String customsItemCode;
	private String isConsignSale;
	private String itemType;
	private String vipDiscount;
	private String lastCurrencyCode;
	private Date firstPurchaseDate;
	private Date lastPurchaseDate;
	private Double lastPurForeignAmount;
	private Double lastPurLocalAmount;
	private Double lastUnitCost;
	private Double maxPurchaseAmount;
	private Double minPurchaseAmount;
	private String itemBrand;
	private String bonusType;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Double margen;
	private Double maxPurchaseQuantity;
	private Double minPurchaseQuantity;
	private String budgetType;
	private String isTax;
	private String purchaseCurrencyCode;
	private String itemCategory;
	private Double purchaseAmount;
	private String status;
	private String taxRelativeItemCode;
	private String colorCode;	// 色碼
	private String material;	// 材質說明
	private String validityDay;	// 效期天數
	private String foreignCategory; // 國外類別
	private Double replenishCoefficient; // 補貨係數
	private String allowMinusStock; // 是否可負庫存
	private String releaseString;	// 上市日期字串
	private String expiryString;	// 下市日期字串
	private String importLotNo;	// 匯入批號
	private Double declRatio;	// 銷售單位/報關單號換算
	private Double minRatio;	// 最小單位/銷貨單位的比例換算
	private String show01;	// 表布1
	private String show02;	// 表布2
	private String show03;	// 表布3
	private String lininging01;	// 裡布1
	private String lininging02;	// 裡布2
	private String lininging03;	// 裡布3
	private String category21;	// 材質2
	private String category22;	// 材質1
	private Long composeLevel; //階層
	private String imageFileName; //圖片檔名
	
	private String washIconPath01;	// 洗標圖片路徑1
	private String washIconPath02;	// 洗標圖片路徑2
	private String washIconPath03;	// 洗標圖片路徑3
	private String washIconPath04;	// 洗標圖片路徑4
	private String washIconPath05;	// 洗標圖片路徑5
	private String washIconPath06;	// 洗標圖片路徑6
	private Long show01Percent; // 表布1百分比
	private Long show02Percent; // 表布2百分比
	private Long show03Percent; // 表布3百分比
	private Long lininging01Percent; // 裡布1百分比
	private Long lininging02Percent; // 裡布2百分比
	private Long lininging03Percent; // 裡布3百分比
	private Double alcolhoPercent; // 酒精百分比
	private String EStore;				// 網購註記
	private String EStoreReserve1;		// 網購註記保留欄位1
	private String EStoreReserve2;		// 網購註記保留欄位2
	private String EStoreReserve3;		// 網購註記保留欄位3
	private String priceAdjustFlag;		// 變價註記
	private String payOline;				// 電子支付註記
	
	
	private String category13Name;	// 非db欄位,系別名稱
	private String enableName; 	// 非db欄位, 名稱
	private Double unitPrice;	// 非db欄位, 價格
	private Date priceLastUpdateDate;// 非db欄位, 最近調價日
	private String itemBrandName;	// 非db欄位, 品牌名稱
	private String supplierName;	// 非db欄位, 供應商名稱
	private String department;	// 非db欄位, 登入人員部門

	
	private List<ImItemImage> imItemImages = new ArrayList(0);
	private List<ImItemCompose> imItemComposes = new ArrayList(0);
	private List<ImItemPrice> imItemPrices = new ArrayList(0);
	private List<ImItemEancode> imItemEancodes = new ArrayList(0);
	private List<ImItemSerial> imItemSerials = new ArrayList(0);

	// Constructors

	public List<ImItemSerial> getImItemSerials() {
	    return imItemSerials;
	}

	public void setImItemSerials(List<ImItemSerial> imItemSerials) {
	    this.imItemSerials = imItemSerials;
	}

	/** default constructor */
	public ImItem() {
	}

	/** minimal constructor */
	/** minimal constructor */
	public ImItem(Long itemId) {
		this.itemId = itemId;
	}
	
	public ImItem(String itemCode, String brandCode) {
		this.itemCode = itemCode;
		this.brandCode = brandCode;
	}
		
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */

	// Property accessors

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

	public String getItemLevel() {
		return this.itemLevel;
	}

	public void setItemLevel(String itemLevel) {
		this.itemLevel = itemLevel;
	}

	public void setLotControl(String lotControl) {
		this.lotControl = lotControl;
	}

	public String getLotControl() {
		return lotControl;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public Long getSalesRatio() {
		return this.salesRatio;
	}

	public void setSalesRatio(Long salesRatio) {
		this.salesRatio = salesRatio;
	}

	public String getSalesUnit() {
		return this.salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public String getPurchaseUnit() {
		return this.purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public void setPurchaseRatio(Long purchaseRatio) {
		this.purchaseRatio = purchaseRatio;
	}

	public Long getPurchaseRatio() {
		return purchaseRatio;
	}

	public Long getBoxCapacity() {
		return boxCapacity;
	}

	public void setBoxCapacity(Long boxCapacity) {
		this.boxCapacity = boxCapacity;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCustomsItemCode() {
		return customsItemCode;
	}

	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	public String getIsConsignSale() {
		return isConsignSale;
	}

	public void setIsConsignSale(String isConsignSale) {
		this.isConsignSale = isConsignSale;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getVipDiscount() {
		return vipDiscount;
	}

	public void setVipDiscount(String vipDiscount) {
		this.vipDiscount = vipDiscount;
	}

	public String getLastCurrencyCode() {
		return lastCurrencyCode;
	}

	public void setLastCurrencyCode(String lastCurrencyCode) {
		this.lastCurrencyCode = lastCurrencyCode;
	}

	public Date getFirstPurchaseDate() {
		return firstPurchaseDate;
	}

	public void setFirstPurchaseDate(Date firstPurchaseDate) {
		this.firstPurchaseDate = firstPurchaseDate;
	}

	public Date getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(Date lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	public Double getLastPurForeignAmount() {
		return lastPurForeignAmount;
	}

	public void setLastPurForeignAmount(Double lastPurForeignAmount) {
		this.lastPurForeignAmount = lastPurForeignAmount;
	}

	public Double getLastPurLocalAmount() {
		return lastPurLocalAmount;
	}

	public void setLastPurLocalAmount(Double lastPurLocalAmount) {
		this.lastPurLocalAmount = lastPurLocalAmount;
	}

	public Double getLastUnitCost() {
		return lastUnitCost;
	}

	public void setLastUnitCost(Double lastUnitCost) {
		this.lastUnitCost = lastUnitCost;
	}

	public Double getMaxPurchaseAmount() {
		return maxPurchaseAmount;
	}

	public void setMaxPurchaseAmount(Double maxPurchaseAmount) {
		this.maxPurchaseAmount = maxPurchaseAmount;
	}

	public Double getMinPurchaseAmount() {
		return minPurchaseAmount;
	}

	public void setMinPurchaseAmount(Double minPurchaseAmount) {
		this.minPurchaseAmount = minPurchaseAmount;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getBonusType() {
		return bonusType;
	}

	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
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

	public List getImItemImages() {
		return this.imItemImages;
	}

	public void setImItemImages(List<ImItemImage> imItemImages) {
		this.imItemImages = imItemImages;
	}

	public List<ImItemCompose> getImItemComposes() {
		return this.imItemComposes;
	}

	public void setImItemComposes(List<ImItemCompose> imItemComposes) {
		this.imItemComposes = imItemComposes;
	}

	public List<ImItemPrice> getImItemPrices() {
		return this.imItemPrices;
	}

	public void setImItemPrices(List<ImItemPrice> imItemPrices) {
		this.imItemPrices = imItemPrices;
	}

	public Double getMargen() {
	    return margen;
	}

	public void setMargen(Double margen) {
	    this.margen = margen;
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

	public String getBudgetType() {
	    return budgetType;
	}

	public void setBudgetType(String budgetType) {
	    this.budgetType = budgetType;
	}

	public String getIsTax() {
	    return isTax;
	}

	public void setIsTax(String isTax) {
	    this.isTax = isTax;
	}

	public List<ImItemEancode> getImItemEancodes() {
	    return imItemEancodes;
	}

	public void setImItemEancodes(List<ImItemEancode> imItemEancodes) {
	    this.imItemEancodes = imItemEancodes;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnableName() {
		return enableName;
	}

	public void setEnableName(String enableName) {
		this.enableName = enableName;
	}
	
	public String getPurchaseCurrencyCode() {
		return purchaseCurrencyCode;
	}

	public void setPurchaseCurrencyCode(String purchaseCurrencyCode) {
		this.purchaseCurrencyCode = purchaseCurrencyCode;
	}

	public Double getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public String getTaxRelativeItemCode() {
		return taxRelativeItemCode;
	}

	public void setTaxRelativeItemCode(String taxRelativeItemCode) {
		this.taxRelativeItemCode = taxRelativeItemCode;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getValidityDay() {
		return validityDay;
	}

	public void setValidityDay(String validityDay) {
		this.validityDay = validityDay;
	}

	public String getForeignCategory() {
		return foreignCategory;
	}

	public void setForeignCategory(String foreignCategory) {
		this.foreignCategory = foreignCategory;
	}

	public String getCategory13Name() {
		return category13Name;
	}

	public void setCategory13Name(String category13Name) {
		this.category13Name = category13Name;
	}

	public Double getReplenishCoefficient() {
		return replenishCoefficient;
	}

	public void setReplenishCoefficient(Double replenishCoefficient) {
		this.replenishCoefficient = replenishCoefficient;
	}

	public String getAllowMinusStock() {
		return allowMinusStock;
	}

	public void setAllowMinusStock(String allowMinusStock) {
		this.allowMinusStock = allowMinusStock;
	}

	public String getReleaseString() {
	    return releaseString;
	}

	public void setReleaseString(String releaseString) {
	    this.releaseString = releaseString;
	}

	public String getExpiryString() {
	    return expiryString;
	}

	public void setExpiryString(String expiryString) {
	    this.expiryString = expiryString;
	}

	public String getImportLotNo() {
	    return importLotNo;
	}

	public void setImportLotNo(String importLotNo) {
	    this.importLotNo = importLotNo;
	}

	public Double getDeclRatio() {
	    return declRatio;
	}

	public void setDeclRatio(Double declRatio) {
	    this.declRatio = declRatio;
	}

	public Double getMinRatio() {
	    return minRatio;
	}

	public void setMinRatio(Double minRatio) {
	    this.minRatio = minRatio;
	}

	public Double getUnitPrice() {
	    return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
	    this.unitPrice = unitPrice;
	}

	public Date getPriceLastUpdateDate() {
	    return priceLastUpdateDate;
	}

	public void setPriceLastUpdateDate(Date priceLastUpdateDate) {
	    this.priceLastUpdateDate = priceLastUpdateDate;
	}

	public String getItemBrandName() {
	    return itemBrandName;
	}

	public void setItemBrandName(String itemBrandName) {
	    this.itemBrandName = itemBrandName;
	}

	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName) {
	    this.supplierName = supplierName;
	}

	public String getCategory21() {
	    return category21;
	}

	public void setCategory21(String category21) {
	    this.category21 = category21;
	}

	public String getShow01() {
	    return show01;
	}

	public void setShow01(String show01) {
	    this.show01 = show01;
	}

	public String getShow02() {
	    return show02;
	}

	public void setShow02(String show02) {
	    this.show02 = show02;
	}

	public String getShow03() {
	    return show03;
	}

	public void setShow03(String show03) {
	    this.show03 = show03;
	}

	public String getLininging01() {
	    return lininging01;
	}

	public void setLininging01(String lininging01) {
	    this.lininging01 = lininging01;
	}

	public String getLininging02() {
	    return lininging02;
	}

	public void setLininging02(String lininging02) {
	    this.lininging02 = lininging02;
	}

	public String getLininging03() {
	    return lininging03;
	}

	public void setLininging03(String lininging03) {
	    this.lininging03 = lininging03;
	}

	public String getWashIconPath01() {
	    return washIconPath01;
	}

	public void setWashIconPath01(String washIconPath01) {
	    this.washIconPath01 = washIconPath01;
	}

	public String getWashIconPath02() {
	    return washIconPath02;
	}

	public void setWashIconPath02(String washIconPath02) {
	    this.washIconPath02 = washIconPath02;
	}

	public String getWashIconPath03() {
	    return washIconPath03;
	}

	public void setWashIconPath03(String washIconPath03) {
	    this.washIconPath03 = washIconPath03;
	}

	public String getWashIconPath04() {
	    return washIconPath04;
	}

	public void setWashIconPath04(String washIconPath04) {
	    this.washIconPath04 = washIconPath04;
	}

	public String getWashIconPath05() {
	    return washIconPath05;
	}

	public void setWashIconPath05(String washIconPath05) {
	    this.washIconPath05 = washIconPath05;
	}

	public String getWashIconPath06() {
	    return washIconPath06;
	}

	public void setWashIconPath06(String washIconPath06) {
	    this.washIconPath06 = washIconPath06;
	}

	public Long getShow01Percent() {
	    return show01Percent;
	}

	public void setShow01Percent(Long show01Percent) {
	    this.show01Percent = show01Percent;
	}

	public Long getShow02Percent() {
	    return show02Percent;
	}

	public void setShow02Percent(Long show02Percent) {
	    this.show02Percent = show02Percent;
	}

	public Long getShow03Percent() {
	    return show03Percent;
	}

	public void setShow03Percent(Long show03Percent) {
	    this.show03Percent = show03Percent;
	}

	public Long getLininging01Percent() {
	    return lininging01Percent;
	}

	public void setLininging01Percent(Long lininging01Percent) {
	    this.lininging01Percent = lininging01Percent;
	}

	public Long getLininging02Percent() {
	    return lininging02Percent;
	}

	public void setLininging02Percent(Long lininging02Percent) {
	    this.lininging02Percent = lininging02Percent;
	}

	public Long getLininging03Percent() {
	    return lininging03Percent;
	}

	public void setLininging03Percent(Long lininging03Percent) {
	    this.lininging03Percent = lininging03Percent;
	}
	
	public Double getAlcolhoPercent() {
	    return alcolhoPercent;
	}

	public void setAlcolhoPercent(Double alcolhoPercent) {
	    this.alcolhoPercent = alcolhoPercent;
	}

	public String getCategory22() {
	    return category22;
	}

	public void setCategory22(String category22) {
	    this.category22 = category22;
	}

	public Long getComposeLevel() {
		return composeLevel;
	}

	public void setComposeLevel(Long composeLevel) {
		this.composeLevel = composeLevel;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEStore() {
		return EStore;
	}

	public void setEStore(String eStore) {
		this.EStore = eStore;
	}

	public String getEStoreReserve1() {
		return EStoreReserve1;
	}

	public void setEStoreReserve1(String storeReserve1) {
		EStoreReserve1 = storeReserve1;
	}

	public String getEStoreReserve2() {
		return EStoreReserve2;
	}

	public void setEStoreReserve2(String storeReserve2) {
		EStoreReserve2 = storeReserve2;
	}

	public String getEStoreReserve3() {
		return EStoreReserve3;
	}

	public void setEStoreReserve3(String storeReserve3) {
		EStoreReserve3 = storeReserve3;
	}

	public String getPriceAdjustFlag() {
		return priceAdjustFlag;
	}

	public void setPriceAdjustFlag(String priceAdjustFlag) {
		this.priceAdjustFlag = priceAdjustFlag;
	}

	public String getPayOline() {
		return payOline;
	}

	public void setPayOline(String payOline) {
		this.payOline = payOline;
	}

	/**
	 * @return the imageFileName
	 */
	public String getImageFileName() {
		return imageFileName;
	}

	/**
	 * @param imageFileName the imageFileName to set
	 */
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	


	

}