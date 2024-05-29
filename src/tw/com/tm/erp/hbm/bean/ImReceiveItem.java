package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImReceiveItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReceiveItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4256373026777165431L;

	private Long lineId; // pk
	private ImReceiveHead imReceiveHead;
	private Long declarationItemNo; 			// 報單項次
	private String declarationItemCode; 			// 報單-品號
	private String declarationItemName; 			// 報單-品名
	private String declarationBrand; 			// 報單-廠牌
	private String declarationModel;			// 報單-型號
	private String declarationSpecification; 		// 報單-規格
	private Double declarationNetWeight = new Double(0); 	// 報單-淨重
	private Double declarationQty = new Double(0); 		// 報單-數量
	private String declarationUnit; 			// 報單-單位
	private Double foreignUnitPrice = new Double(0); 	// 原幣單價
	private Double localUnitPrice = new Double(0); 		// 本幣單價
	private Double foreignAmount = new Double(0); 		// 原幣金額合計
	private Double localAmount = new Double(0); 		// 本幣金額合計
	private String invoiceNo; 				// Invoice No.
	private String reserve1;				// 借用存放 cmDeclarationItem.itemId 2009.12.13 arthur
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String itemCode; // 品名
	private Double quantity = new Double(0);
	private String poOrderType = PoPurchaseOrderHead.PURCHASE_ORDER_LOCAL; // 預設是國內採購;
	private String poOrderNo;
	private Double expenseApportionmentAmount = new Double(0);
	// private Double receiveQuantityDif = new Double(0) ;
	private Double diffQty = new Double(0);
	private String itemCName;
	private Double barcodeCount = new Double(0);
	private String isDeleteRecord = "0" ; //是否被刪除 1  表示要被移除
	private String isLockRecord = "0" ; //是否被鎖定 1  表示鎖定
	private String message ; //line 訊息的顯示	
	private String status ;
	private Double unitPrice = new Double(0); // 售價
	private Double receiptQuantity; // 驗收數量(入帳量)
	private Double acceptQuantity;  // 良品數量
	private Double defectQuantity;  // 不良品數量
	private Double shortQuantity;   // 短到數量
	private String originalDeclarationNo;	// 原報單單號
	private Long originalDeclarationSeq;	// 原報單序號
	private Date originalDeclarationDate;	// 原報單日期
	private String originalDeclarationItem;	// 
	private Double sampleQuantity;		// 抽樣數量
	private Double weight;			// 重量
	private String shippingMark;		// 挑貨單號
	private String lotNo; 		// 批號
	private Double foreignUnitPriceOri = new Double(0); 	// 原幣單價
	private String receiveItemType; // 虛擬欄位，用於盤點機匯入進貨商品之類別(D不良品、F短溢到品、S樣品、G良品)
	private Double lastForeignUnitCost = new Double(0); // 上次外幣進貨價格
	private String poNo;
	private Double standardPurchaseCost; //期初平均成本
	private String isConsignSale; //寄賣品
	
	private int lineIndexNo; // 商品查詢用
	private Date orgImportDate; //原報單日期
	
	// Constructors

	public Double getLastForeignUnitCost() {
	    return lastForeignUnitCost;
	}

	public void setLastForeignUnitCost(Double lastForeignUnitCost) {
	    this.lastForeignUnitCost = lastForeignUnitCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPoOrderType() {
		return poOrderType;
	}

	public void setPoOrderType(String poOrderType) {
		this.poOrderType = poOrderType;
	}

	public String getPoOrderNo() {
		return poOrderNo;
	}

	public void setPoOrderNo(String poOrderNo) {
		this.poOrderNo = poOrderNo;
	}

	/** default constructor */
	public ImReceiveItem() {
	}

	/** minimal constructor */
	public ImReceiveItem(Long lineId, ImReceiveHead imReceiveHead, Long declarationItemNo) {
		this.lineId = lineId;
		this.imReceiveHead = imReceiveHead;
		this.declarationItemNo = declarationItemNo;
	}
	
	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImReceiveHead getImReceiveHead() {
		return this.imReceiveHead;
	}

	public void setImReceiveHead(ImReceiveHead imReceiveHead) {
		this.imReceiveHead = imReceiveHead;
	}

	public Long getDeclarationItemNo() {
		return this.declarationItemNo;
	}

	public void setDeclarationItemNo(Long declarationItemNo) {
		this.declarationItemNo = declarationItemNo;
	}

	public String getDeclarationItemCode() {
		return this.declarationItemCode;
	}

	public void setDeclarationItemCode(String declarationItemCode) {
		this.declarationItemCode = declarationItemCode;
	}

	public String getDeclarationItemName() {
		return this.declarationItemName;
	}

	public void setDeclarationItemName(String declarationItemName) {
		this.declarationItemName = declarationItemName;
	}

	public String getDeclarationBrand() {
		return this.declarationBrand;
	}

	public void setDeclarationBrand(String declarationBrand) {
		this.declarationBrand = declarationBrand;
	}

	public String getDeclarationModel() {
		return this.declarationModel;
	}

	public void setDeclarationModel(String declarationModel) {
		this.declarationModel = declarationModel;
	}

	public String getDeclarationSpecification() {
		return this.declarationSpecification;
	}

	public void setDeclarationSpecification(String declarationSpecification) {
		this.declarationSpecification = declarationSpecification;
	}

	public Double getDeclarationNetWeight() {
		return this.declarationNetWeight;
	}

	public void setDeclarationNetWeight(Double declarationNetWeight) {
		this.declarationNetWeight = declarationNetWeight;
	}

	public Double getDeclarationQty() {
		return this.declarationQty;
	}

	public void setDeclarationQty(Double declarationQty) {
		this.declarationQty = declarationQty;
	}

	public String getDeclarationUnit() {
		return this.declarationUnit;
	}

	public void setDeclarationUnit(String declarationUnit) {
		this.declarationUnit = declarationUnit;
	}

	public Double getLocalUnitPrice() {
		return this.localUnitPrice;
	}

	public void setLocalUnitPrice(Double localUnitPrice) {
		this.localUnitPrice = localUnitPrice;
	}

	public Double getForeignAmount() {
		return this.foreignAmount;
	}

	public void setForeignAmount(Double foreignAmount) {
		this.foreignAmount = foreignAmount;
	}

	public Double getLocalAmount() {
		return this.localAmount;
	}

	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getForeignUnitPrice() {
		return foreignUnitPrice;
	}

	public void setForeignUnitPrice(Double foreignUnitPrice) {
		this.foreignUnitPrice = foreignUnitPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getExpenseApportionmentAmount() {
		return expenseApportionmentAmount;
	}

	public void setExpenseApportionmentAmount(Double expenseApportionmentAmount) {
		this.expenseApportionmentAmount = expenseApportionmentAmount;
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

	public Double getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(Double barcodeCount) {
		this.barcodeCount = barcodeCount;
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

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getReceiptQuantity() {
		return receiptQuantity;
	}

	public void setReceiptQuantity(Double receiptQuantity) {
		this.receiptQuantity = receiptQuantity;
	}

	public Double getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Double acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Double getDefectQuantity() {
		return defectQuantity;
	}

	public void setDefectQuantity(Double defectQuantity) {
		this.defectQuantity = defectQuantity;
	}

	public Double getShortQuantity() {
		return shortQuantity;
	}

	public void setShortQuantity(Double shortQuantity) {
		this.shortQuantity = shortQuantity;
	}

	public String getOriginalDeclarationNo() {
		return originalDeclarationNo;
	}

	public void setOriginalDeclarationNo(String originalDeclarationNo) {
		this.originalDeclarationNo = originalDeclarationNo;
	}

	public Long getOriginalDeclarationSeq() {
		return originalDeclarationSeq;
	}

	public void setOriginalDeclarationSeq(Long originalDeclarationSeq) {
		this.originalDeclarationSeq = originalDeclarationSeq;
	}

	public Date getOriginalDeclarationDate() {
		return originalDeclarationDate;
	}

	public void setOriginalDeclarationDate(Date originalDeclarationDate) {
		this.originalDeclarationDate = originalDeclarationDate;
	}

	public String getOriginalDeclarationItem() {
		return originalDeclarationItem;
	}

	public void setOriginalDeclarationItem(String originalDeclarationItem) {
		this.originalDeclarationItem = originalDeclarationItem;
	}

	public Double getSampleQuantity() {
		return sampleQuantity;
	}

	public void setSampleQuantity(Double sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}

	public Double getWeight() {
	    return weight;
	}

	public void setWeight(Double weight) {
	    this.weight = weight;
	}

	public String getShippingMark() {
	    return shippingMark;
	}

	public void setShippingMark(String shippingMark) {
	    this.shippingMark = shippingMark;
	}

	public String getLotNo() {
	    return lotNo;
	}

	public void setLotNo(String lotNo) {
	    this.lotNo = lotNo;
	}

	public Double getForeignUnitPriceOri() {
	    return foreignUnitPriceOri;
	}

	public void setForeignUnitPriceOri(Double foreignUnitPriceOri) {
	    this.foreignUnitPriceOri = foreignUnitPriceOri;
	}

	public String getReceiveItemType() {
		return receiveItemType;
	}

	public void setReceiveItemType(String receiveItemType) {
		this.receiveItemType = receiveItemType;
	}

	public int getLineIndexNo() {
	    return lineIndexNo;
	}

	public void setLineIndexNo(int lineIndexNo) {
	    this.lineIndexNo = lineIndexNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Double getStandardPurchaseCost() {
		return standardPurchaseCost;
	}

	public void setStandardPurchaseCost(Double standardPurchaseCost) {
		this.standardPurchaseCost = standardPurchaseCost;
	}

	public String getIsConsignSale() {
		return isConsignSale;
	}

	public void setIsConsignSale(String isConsignSale) {
		this.isConsignSale = isConsignSale;
	}
	
	public Date getOrgImportDate() {
		return orgImportDate;
	}

	public void setOrgImportDate(Date orgImportDate) {
		this.orgImportDate = orgImportDate;
	}
}