package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemBarcodeLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
	
public class ImItemBarcodeLine implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -1807814089878774572L;
    private Long lineId;
    private ImItemBarcodeHead imItemBarcodeHead;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String isDeleteRecord;
    private String isLockRecord;
    private String message;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String itemCode;
    private Long paper;
    private Long indexNo;
    private String itemCName; // 暫時欄位
    private Double unitPrice; // 暫時欄位
    
    // Constructors

    public ImItemBarcodeLine(Long lineId, ImItemBarcodeHead imItemBarcodeHead,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String isDeleteRecord, String isLockRecord,
	    String message, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, String itemCode,
	    Long paper, Long indexNo) {
	super();
	this.lineId = lineId;
	this.imItemBarcodeHead = imItemBarcodeHead;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.isDeleteRecord = isDeleteRecord;
	this.isLockRecord = isLockRecord;
	this.message = message;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.itemCode = itemCode;
	this.paper = paper;
	this.indexNo = indexNo;
    }

    /** default constructor */
    public ImItemBarcodeLine() {
    }

    /** full constructor */

    
    // Property accessors

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
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

    public String getIsDeleteRecord() {
	return this.isDeleteRecord;
    }

    public void setIsDeleteRecord(String isDeleteRecord) {
	this.isDeleteRecord = isDeleteRecord;
    }

    public String getIsLockRecord() {
	return this.isLockRecord;
    }

    public void setIsLockRecord(String isLockRecord) {
	this.isLockRecord = isLockRecord;
    }

    public String getMessage() {
	return this.message;
    }

    public void setMessage(String message) {
	this.message = message;
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

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public Long getPaper() {
	return this.paper;
    }

    public void setPaper(Long paper) {
	this.paper = paper;
    }

    public ImItemBarcodeHead getImItemBarcodeHead() {
        return imItemBarcodeHead;
    }

    public void setImItemBarcodeHead(ImItemBarcodeHead imItemBarcodeHead) {
        this.imItemBarcodeHead = imItemBarcodeHead;
    }

    public Long getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Long indexNo) {
        this.indexNo = indexNo;
    }

    public void setItemCName(String itemCName) {
        this.itemCName = itemCName;
    }

    public String getItemCName() {
        return itemCName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}