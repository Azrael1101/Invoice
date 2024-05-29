package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationOnHand entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationOnHandView implements java.io.Serializable {

	private static final long serialVersionUID = 4071704017669881648L;
	// Fields
	private String brandCode;
	private String declType;
	private String declarationNo;
	private Long declarationSeq;
	private Date declDate;
	private Date originalDate;
	private String customsItemCode;
	private String customsWarehouseCode;
	private String itemCode;
	private String warehouseCode;
	private Double onHandQuantity;
	private Double outUncommitQty;
	private Double inUncommitQty;
	private Double moveUncommitQty;
	private Double otherUncommitQty;
	private Double currentOnHandQty;
	private String status;

	private String itemCName; // 品名
	private String category01; // 大類
	private String category01Name; // 大類名稱
	private String category02; // 中類
	private String category02Name; // 中類名稱
	private String category03; // 小類
	private String itemBrand; // 商品品牌
	private String itemBrandName; // 商品品牌名稱
	private Date warehouseInDate; // 進倉日
	private Long remainDays; // 剩餘天數
	private Date expiryDate; // 屆期日
	private Date importDate; // 進口日期
	private String categoryType; // 業種
	private Double qty; // 進貨數量
	private String supplierCode; // 廠商代號
	private String supplierName; // 廠商名稱

	private Double unitPrice; // 暫時欄位 目前售價
	private Double blockOnHandQuantity; // 鎖住報單庫存數量
	private String orderTypeCode; // 進貨單別
	private String orderNo; // 進貨單號
	private String description; // 備註
	private String isExtention; //展延註記
	private Date orDeclDate; //原DB進貨日
	private Date orgImportDate; //原DB進貨日
	
	private Date lastUpdateDate;//最後更新日期
	private String reserve1;//保留欄位1
	private String reserve2;//保留欄位2
	private String reserve3;//保留欄位3
	private String reserve4;//保留欄位4
	private String reserve5;//保留欄位5
	private String lastUpdatedBy;//最後更新人
	private String orDeclNo;//原始報單號碼
	private Long orItemNo;//原始報單項次
	private String orDeclType;//原始報單類別

	
	public CmDeclarationOnHandView(String brandCode, String declType,
			String declarationNo, Long declarationSeq, Date declDate,
			Date originalDate, String customsItemCode,
			String customsWarehouseCode, String itemCode, String warehouseCode,
			Double onHandQuantity, Double outUncommitQty, Double inUncommitQty,
			Double moveUncommitQty, Double otherUncommitQty,
			Double currentOnHandQty, String status, String itemCName,
			String category01, String category01Name, String category02,
			String category02Name, String category03, String itemBrand,
			String itemBrandName, Date warehouseInDate, Long remainDays,
			Date expiryDate, Date importDate, String categoryType, Double qty,
			String supplierCode, String supplierName, Double unitPrice,
			Double blockOnHandQuantity, String orderTypeCode, String orderNo,
			String description, String isExtention, Date orDeclDate,
			Date orgImportDate, Date lastUpdateDate, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String lastUpdatedBy, String orDeclNo, Long orItemNo, String orDeclType) {
		super();
		this.brandCode = brandCode;
		this.declType = declType;
		this.declarationNo = declarationNo;
		this.declarationSeq = declarationSeq;
		this.declDate = declDate;
		this.originalDate = originalDate;
		this.customsItemCode = customsItemCode;
		this.customsWarehouseCode = customsWarehouseCode;
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
		this.onHandQuantity = onHandQuantity;
		this.outUncommitQty = outUncommitQty;
		this.inUncommitQty = inUncommitQty;
		this.moveUncommitQty = moveUncommitQty;
		this.otherUncommitQty = otherUncommitQty;
		this.currentOnHandQty = currentOnHandQty;
		this.status = status;
		this.itemCName = itemCName;
		this.category01 = category01;
		this.category01Name = category01Name;
		this.category02 = category02;
		this.category02Name = category02Name;
		this.category03 = category03;
		this.itemBrand = itemBrand;
		this.itemBrandName = itemBrandName;
		this.warehouseInDate = warehouseInDate;
		this.remainDays = remainDays;
		this.expiryDate = expiryDate;
		this.importDate = importDate;
		this.categoryType = categoryType;
		this.qty = qty;
		this.supplierCode = supplierCode;
		this.supplierName = supplierName;
		this.unitPrice = unitPrice;
		this.blockOnHandQuantity = blockOnHandQuantity;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.description = description;
		this.isExtention = isExtention;
		this.orDeclDate = orDeclDate;
		this.orgImportDate = orgImportDate;
		this.lastUpdateDate = lastUpdateDate;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.lastUpdatedBy = lastUpdatedBy;
		this.orDeclNo = orDeclNo;
		this.orItemNo = orItemNo;
		this.orDeclType = orDeclType;
	}

	// Constructors
	/** default constructor */
	public CmDeclarationOnHandView() {
	}

	/** full constructor */
	

	// Property accessors

	public String getDeclarationNo() {
		return this.declarationNo;
	}

	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}

	public Long getDeclarationSeq() {
		return this.declarationSeq;
	}

	public void setDeclarationSeq(Long declarationSeq) {
		this.declarationSeq = declarationSeq;
	}

	public String getCustomsItemCode() {
		return this.customsItemCode;
	}

	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	public String getCustomsWarehouseCode() {
		return this.customsWarehouseCode;
	}

	public void setCustomsWarehouseCode(String customsWarehouseCode) {
		this.customsWarehouseCode = customsWarehouseCode;
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

	public Double getOnHandQuantity() {
		return this.onHandQuantity;
	}

	public void setOnHandQuantity(Double onHandQuantity) {
		this.onHandQuantity = onHandQuantity;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public Double getCurrentOnHandQty() {
		return currentOnHandQty;
	}

	public void setCurrentOnHandQty(Double currentOnHandQty) {
		this.currentOnHandQty = currentOnHandQty;
	}

	public String getDeclType() {
		return declType;
	}

	public void setDeclType(String declType) {
		this.declType = declType;
	}

	public Date getDeclDate() {
		return declDate;
	}

	public void setDeclDate(Date declDate) {
		this.declDate = declDate;
	}

	public Date getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(Date originalDate) {
		this.originalDate = originalDate;
	}

	public String getCategory01() {
		return category01;
	}

	public void setCategory01(String category01) {
		this.category01 = category01;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public Date getWarehouseInDate() {
		return warehouseInDate;
	}

	public void setWarehouseInDate(Date warehouseInDate) {
		this.warehouseInDate = warehouseInDate;
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

	public Long getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(Long remainDays) {
		this.remainDays = remainDays;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
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

	public String getItemBrandName() {
		return itemBrandName;
	}

	public void setItemBrandName(String itemBrandName) {
		this.itemBrandName = itemBrandName;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the blockOnHandQuantity
	 */
	public Double getBlockOnHandQuantity() {
		return blockOnHandQuantity;
	}

	/**
	 * @param blockOnHandQuantity
	 *            the blockOnHandQuantity to set
	 */
	public void setBlockOnHandQuantity(Double blockOnHandQuantity) {
		this.blockOnHandQuantity = blockOnHandQuantity;
	}

	/**
	 * @return the orderTypeCode
	 */
	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	/**
	 * @param orderTypeCode the orderTypeCode to set
	 */
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the isExtention
	 */
	public String getIsExtention() {
		return isExtention;
	}

	/**
	 * @param isExtention the isExtention to set
	 */
	public void setIsExtention(String isExtention) {
		this.isExtention = isExtention;
	}

	public Date getOrDeclDate() {
		return orDeclDate;
	}

	public void setOrDeclDate(Date orDeclDate) {
		this.orDeclDate = orDeclDate;
	}
	
	public Date getOrgImportDate() {
		return orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}
	
	public Date getLastUpdateDate(){
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(Date lastUpdateDate){
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	
	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getOrDeclNo() {
		return orDeclNo;
	}

	public void setOrDeclNo(String orDeclNo) {
		this.orDeclNo = orDeclNo;
	}

	public Long getOrItemNo() {
		return orItemNo;
	}

	public void setOrItemNo(Long orItemNo) {
		this.orItemNo = orItemNo;
	}

	public String getOrDeclType() {
		return orDeclType;
	}

	public void setOrDeclType(String orDeclType) {
		this.orDeclType = orDeclType;
	}
}