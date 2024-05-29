package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImMovementItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImMovementItem implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 2507093282046304391L;
	private Long lineId;
	private ImMovementHead imMovementHead;
    private String itemCode;
	private String itemName;
	private String scanCode; //刷入的條碼
	private String deliveryWarehouseCode;
	private String lotNo;
	private String lotControl;
	private Double stockOnHandQuantity;
	private Double originalDeliveryQuantity;
	private Double deliveryQuantity;
	private String arrivalWarehouseCode;
	private Double arrivalQuantity;
	private String reserve1;//充當列印條碼時放品名之用
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
	private String exportFlag;
	private Date exportDate;
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
	private String returnMessage;
    private Double unitPrice;// 充當列印條碼時放價格之用
    private String improtItemCode;
    private String customsItemCode;
    private Long boxNo;
    private String originalDeclarationNo;
    private Long originalDeclarationSeq;
    private Date originalDeclarationDate;
    private String originalDeclarationItem;
    private Double weight;
    /*Steve 覆核*/
    private Double whComfirmedQuantity;
    private String whComfirmedBy;
    private Date whComfirmedDate;
    /*Steve 覆核*/
    private String unitPrice_format;// 列印條碼時價格不含小數點
    private String deliveryQuantity_format; // 列印條碼時數量不含小數點
    /*海關新增*/
	private String goodDesc;
	private String unit;
	private String specDesc;
	/*海關新增*/
	// Constructors

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

	/** default constructor */
	public ImMovementItem() {
	}

	/** minimal constructor */
	public ImMovementItem(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImMovementItem(Long lineId, ImMovementHead imMovementHead,
			String itemCode, String itemName, String scanCode, String deliveryWarehouseCode,
			String lotNo, String lotControl, Double stockOnHandQuantity,
			Double originalDeliveryQuantity, Double deliveryQuantity,
			String arrivalWarehouseCode, Double arrivalQuantity,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String status, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
			Long indexNo, String exportFlag, Date exportDate,
			String isDeleteRecord, String isLockRecord, String message,
			Double unitPrice, String improtItemCode, String customsItemCode,
			Long boxNo, String originalDeclarationNo,
			Long originalDeclarationSeq, Date originalDeclarationDate,
			String originalDeclarationItem) {
		this.lineId = lineId;
		this.imMovementHead = imMovementHead;
		//this.itemCode = itemCode;
		this.itemName = itemName;
		this.scanCode = scanCode;
		this.deliveryWarehouseCode = deliveryWarehouseCode;
		this.lotNo = lotNo;
		this.lotControl = lotControl;
		this.stockOnHandQuantity = stockOnHandQuantity;
		this.originalDeliveryQuantity = originalDeliveryQuantity;
		this.deliveryQuantity = deliveryQuantity;
		this.arrivalWarehouseCode = arrivalWarehouseCode;
		this.arrivalQuantity = arrivalQuantity;
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
		this.exportFlag = exportFlag;
		this.exportDate = exportDate;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
		this.unitPrice = unitPrice;
		this.improtItemCode = improtItemCode;
		this.customsItemCode = customsItemCode;
		this.boxNo = boxNo;
		this.originalDeclarationNo = originalDeclarationNo;
		this.originalDeclarationSeq = originalDeclarationSeq;
		this.originalDeclarationDate = originalDeclarationDate;
		this.originalDeclarationItem = originalDeclarationItem;
	}

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImMovementHead getImMovementHead() {
		return this.imMovementHead;
	}

	public void setImMovementHead(ImMovementHead imMovementHead) {
		this.imMovementHead = imMovementHead;
	}

	public String getScanCode() {
		return this.scanCode;
	}
	

	public void setScanCode(String scanCode) {
		this.scanCode = scanCode;
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

	public void setDeliveryWarehouseCode(String deliveryWarehouseCode) {
		this.deliveryWarehouseCode = deliveryWarehouseCode;
	}

	public String getDeliveryWarehouseCode() {
		return deliveryWarehouseCode;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getLotControl() {
		return lotControl;
	}

	public void setLotControl(String lotControl) {
		this.lotControl = lotControl;
	}

	public void setStockOnHandQuantity(Double stockOnHandQuantity) {
		this.stockOnHandQuantity = stockOnHandQuantity;
	}

	public Double getStockOnHandQuantity() {
		return stockOnHandQuantity;
	}



	public Double getOriginalDeliveryQuantity() {
		return originalDeliveryQuantity;
	}

	public void setOriginalDeliveryQuantity(Double originalDeliveryQuantity) {
		this.originalDeliveryQuantity = originalDeliveryQuantity;
	}

	public Double getDeliveryQuantity() {
		return this.deliveryQuantity;
	}

	public void setDeliveryQuantity(Double deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}

	public void setArrivalWarehouseCode(String arrivalWarehouseCode) {
		this.arrivalWarehouseCode = arrivalWarehouseCode;
	}

	public String getArrivalWarehouseCode() {
		return arrivalWarehouseCode;
	}

	public Double getArrivalQuantity() {
		return this.arrivalQuantity;
	}

	public void setArrivalQuantity(Double arrivalQuantity) {
		this.arrivalQuantity = arrivalQuantity;
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

	public String getExportFlag() {
		return exportFlag;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
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

	public String getImprotItemCode() {
		return improtItemCode;
	}

	public void setImprotItemCode(String improtItemCode) {
		this.improtItemCode = improtItemCode;
	}

	public String getCustomsItemCode() {
		return customsItemCode;
	}

	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	public Long getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(Long boxNo) {
		this.boxNo = boxNo;
	}

	public String getOriginalDeclarationNo() {
		return originalDeclarationNo;
	}

	public void setOriginalDeclarationNo(String originalDeclarationNo) {
		this.originalDeclarationNo = originalDeclarationNo;
	}

	public String getOriginalDeclarationItem() {
		return originalDeclarationItem;
	}

	public void setOriginalDeclarationItem(String originalDeclarationItem) {
		this.originalDeclarationItem = originalDeclarationItem;
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

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getUnitPrice_format() {
		return unitPrice_format;
	}

	public void setUnitPrice_format(String unitPrice_format) {
		this.unitPrice_format = unitPrice_format;
	}

	public String getDeliveryQuantity_format() {
		return deliveryQuantity_format;
	}

	public void setDeliveryQuantity_format(String deliveryQuantity_format) {
		this.deliveryQuantity_format = deliveryQuantity_format;
	}

	public Double getWhComfirmedQuantity() {
		return whComfirmedQuantity;
	}

	public void setWhComfirmedQuantity(Double whComfirmedQuantity) {
		this.whComfirmedQuantity = whComfirmedQuantity;
	}

	public String getWhComfirmedBy() {
		return whComfirmedBy;
	}

	public void setWhComfirmedBy(String whComfirmedBy) {
		this.whComfirmedBy = whComfirmedBy;
	}

	public Date getWhComfirmedDate() {
		return whComfirmedDate;
	}

	public void setWhComfirmedDate(Date whComfirmedDate) {
		this.whComfirmedDate = whComfirmedDate;
	}

	public String getGoodDesc() {
		return goodDesc;
	}

	public void setGoodDesc(String goodDesc) {
		this.goodDesc = goodDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}


	
}