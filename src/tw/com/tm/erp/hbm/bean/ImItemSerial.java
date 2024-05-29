package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemSerial entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemSerial implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 8437673914555097021L;
    private Long lineId;
    private Long itemId;
    private String itemCode;
    private String brandCode;
    private String serial;
    private String isUsed;
    private String isUsedName; 	// 非 DB欄位
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
    private String isDeleteRecord;
    private String isLockRecord;
    private String message;

    // Constructors

    public String getIsUsedName() {
        return isUsedName;
    }

    public void setIsUsedName(String isUsedName) {
        this.isUsedName = isUsedName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** default constructor */
    public ImItemSerial() {
    }

    /** full constructor */
    public ImItemSerial(Long itemId, String itemCode, String brandCode,
	    String serial, String isUsed, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5, String createdBy,
	    Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
	    Long indexNo, String isDeleteRecord, String isLockRecord,
	    String message) {
	this.itemId = itemId;
	this.itemCode = itemCode;
	this.brandCode = brandCode;
	this.serial = serial;
	this.isUsed = isUsed;
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
    }

    // Property accessors

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public Long getItemId() {
	return this.itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
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

    public String getSerial() {
	return this.serial;
    }

    public void setSerial(String serial) {
	this.serial = serial;
    }

    public String getIsUsed() {
	return this.isUsed;
    }

    public void setIsUsed(String isUsed) {
	this.isUsed = isUsed;
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

}