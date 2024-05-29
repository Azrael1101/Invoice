package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImItemOnHandView implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private ImItemOnHandViewId id;
	private String organizationCode;
	private String itemCName;
	private String itemEName;
	private String warehouseCode;
	private String warehouseName;
	private String warehouseManager;
	private Double stockOnHandQty;
	private Double outUncommitQty;
	private Double inUncommitQty;
	private Double moveUncommitQty;
	private Double otherUncommitQty;
	private Double currentOnHandQty;
	private String description;
	private String salesUnit;
	private Long salesRatio;
	private String purchaseUnit;
	private String isComposeItem;
	private String itemLevel;
	private String lotControl;
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
	private String category01; // 大類
	private String category01Name; // 大類名稱
	private String category02; // 中類
	private String category02Name; // 中類名稱
	private String category03; // 小類
	private String category03Name; // 小類名稱
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
	private String category17; // 廠商代號
	private String category18;
	private String category19;
	private String category20;
	private String categoryType;
	private String itemCategory;
	private String itemCategoryName;
	private String itemBrand; // 商品品牌
	private String itemBrandName; // 品牌名稱
	private String isTax;
	private Long boxCapacity;
	private int indexNo;
	private Double unitPrice; // 售價
	private String supplierName; // 廠商名稱
	private String unitPrice_format;// 列印條碼時價格不含小數點
	private String currentOnHandQty_format; // 列印條碼時數量不含小數點
	private String isMoveIn;//是否進貨中
	private String imageFileName;
	
	// Constructors

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/** default constructor */
	public ImItemOnHandView() {
	}

	/** full constructor */
	public ImItemOnHandView(ImItemOnHandViewId id) {
		this.id = id;
	}

	public ImItemOnHandViewId getId() {
		return this.id;
	}

	public void setId(ImItemOnHandViewId id) {
		this.id = id;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
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

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseManager(String warehouseManager) {
		this.warehouseManager = warehouseManager;
	}

	public String getWarehouseManager() {
		return warehouseManager;
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
		return moveUncommitQty;
	}

	public void setMoveUncommitQty(Double moveUncommitQty) {
		this.moveUncommitQty = moveUncommitQty;
	}

	public Double getOtherUncommitQty() {
		return otherUncommitQty;
	}

	public void setOtherUncommitQty(Double otherUncommitQty) {
		this.otherUncommitQty = otherUncommitQty;
	}

	public void setCurrentOnHandQty(Double currentOnHandQty) {
		this.currentOnHandQty = currentOnHandQty;
	}

	public Double getCurrentOnHandQty() {
		return currentOnHandQty;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getIsComposeItem() {
		return this.isComposeItem;
	}

	public void setIsComposeItem(String isComposeItem) {
		this.isComposeItem = isComposeItem;
	}

	public String getItemLevel() {
		return this.itemLevel;
	}

	public void setItemLevel(String itemLevel) {
		this.itemLevel = itemLevel;
	}

	public String getLotControl() {
		return lotControl;
	}

	public void setLotControl(String lotControl) {
		this.lotControl = lotControl;
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

	public int getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getIsTax() {
		return isTax;
	}

	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}

	public Long getBoxCapacity() {
		return boxCapacity;
	}

	public void setBoxCapacity(Long boxCapacity) {
		this.boxCapacity = boxCapacity;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getCategory01Name() {
		return category01Name;
	}

	public void setCategory01Name(String category01Name) {
		this.category01Name = category01Name;
	}

	public String getCategory02Name() {
		return category02Name;
	}

	public void setCategory02Name(String category02Name) {
		this.category02Name = category02Name;
	}


	public String getCategory03Name() {
		return category03Name;
	}

	public void setCategory03Name(String category03Name) {
		this.category03Name = category03Name;
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

	public String getUnitPrice_format() {
		return unitPrice_format;
	}

	public void setUnitPrice_format(String unitPrice_format) {
		this.unitPrice_format = unitPrice_format;
	}

	public String getCurrentOnHandQty_format() {
		return currentOnHandQty_format;
	}

	public void setCurrentOnHandQty_format(String currentOnHandQty_format) {
		this.currentOnHandQty_format = currentOnHandQty_format;
	}
	
	public String getIsMoveIn() {
		return isMoveIn;
	}

	public void setIsMoveIn(String isMoveIn) {
		this.isMoveIn = isMoveIn;
	}

	/**
	 * @return the itemCategoryName
	 */
	public String getItemCategoryName() {
		return itemCategoryName;
	}

	/**
	 * @param itemCategoryName the itemCategoryName to set
	 */
	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
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