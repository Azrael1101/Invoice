package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImReplenishLimition entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReplenishLimition implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 7940232948969360252L;
    private Long lineId;
    private ImReplenishHead head;
    private String category00;
    private String category01;
    private String category02;
    private String itemBrand;
    private String itemCode;
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

    private String itemCName;		// 暫存欄位
    private String category00Name;	// 暫存欄位
    private String category01Name;	// 暫存欄位
    private String category02Name;	// 暫存欄位
    private String itemBrandName;	// 暫存欄位
    
    // Constructors

    /** default constructor */
    public ImReplenishLimition() {
    }

    /** full constructor */
    public ImReplenishLimition(String category00,
	    String category01, String category02, String itemBrand,
	    String itemCode, String reserve1, String reserve2, String reserve3,
	    String reserve4, String reserve5, String createdBy,
	    Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
	    Long indexNo, String isDeleteRecord, String isLockRecord,
	    String message) {
	this.category00 = category00;
	this.category01 = category01;
	this.category02 = category02;
	this.itemBrand = itemBrand;
	this.itemCode = itemCode;
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

    public String getCategory00() {
	return this.category00;
    }

    public void setCategory00(String category00) {
	this.category00 = category00;
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

    public String getItemBrand() {
	return this.itemBrand;
    }

    public void setItemBrand(String itemBrand) {
	this.itemBrand = itemBrand;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
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

    public String getItemCName() {
        return itemCName;
    }

    public void setItemCName(String itemCName) {
        this.itemCName = itemCName;
    }

    public String getCategory00Name() {
        return category00Name;
    }

    public void setCategory00Name(String category00Name) {
        this.category00Name = category00Name;
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

    public ImReplenishHead getHead() {
        return head;
    }

    public void setHead(ImReplenishHead head) {
        this.head = head;
    }

}