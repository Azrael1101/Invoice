package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImInventoryCountsHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImInventoryCountsHead implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6701878053801668239L;
    // Fields
    private Long headId;
    private String brandCode;
    private String orderTypeCode;
    private String orderNo;
    private Date countsDate;
    private Date inventoryDate;
    private String warehouseCode;
    private String warehouseName;
    private String countsId;
    private String countsType;
    private String countsLotNo;
    private String superintendentCode;
    private String superintendentName;
    private String actualSuperintendentCode;
    private String actualSuperintendentName;
    private Long notificationId;
    private Long adjustmentId;
    private String description;
    private String isImportedFile;
    private Long importedTimes;
    private String isPrinted;
    private Long printedTimes;
    private Date lastImportDate;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String status;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Date actualCountsDate;
    private List<ImInventoryCountsLine> imInventoryCountsLines = new ArrayList(
	    0);

    // Constructors

    /** default constructor */
    public ImInventoryCountsHead() {
    }

    /** full constructor */
    public ImInventoryCountsHead(String brandCode, String orderTypeCode,
	    String orderNo, Date countsDate, Date inventoryDate, String warehouseCode,
	    String warehouseName, String countsId, String countsType, String countsLotNo,
	    String superintendentCode, String superintendentName, String actualSuperintendentCode,
	    String actualSuperintendentName, Long notificationId, Long adjustmentId, String description,
	    String isImportedFile, Long importedTimes, String isPrinted,
	    Long printedTimes, Date lastImportDate, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String status, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, Date actualCountsDate,
	    List<ImInventoryCountsLine> imInventoryCountsLines) {
	this.brandCode = brandCode;
	this.orderTypeCode = orderTypeCode;
	this.orderNo = orderNo;
	this.countsDate = countsDate;
	this.inventoryDate = inventoryDate;
	this.warehouseCode = warehouseCode;
	this.warehouseName = warehouseName;
	this.countsId = countsId;
	this.countsType = countsType;
	this.countsLotNo = countsLotNo;
	this.superintendentCode = superintendentCode;
	this.superintendentName = superintendentName;
	this.actualSuperintendentCode = actualSuperintendentCode;
	this.actualSuperintendentName = actualSuperintendentName;	
	this.notificationId = notificationId;
	this.adjustmentId = adjustmentId;
	this.description = description;
	this.isImportedFile = isImportedFile;
	this.importedTimes = importedTimes;
	this.isPrinted = isPrinted;
	this.printedTimes = printedTimes;
	this.lastImportDate = lastImportDate;
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
	this.actualCountsDate = actualCountsDate;
	this.imInventoryCountsLines = imInventoryCountsLines;
    }

    // Property accessors

    public Long getHeadId() {
	return this.headId;
    }

    public void setHeadId(Long headId) {
	this.headId = headId;
    }

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getOrderTypeCode() {
	return this.orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
	this.orderTypeCode = orderTypeCode;
    }

    public String getOrderNo() {
	return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
    }

    public Date getCountsDate() {
	return this.countsDate;
    }

    public void setCountsDate(Date countsDate) {
	this.countsDate = countsDate;
    }
    
    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }
    
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getCountsId() {
	return countsId;
    }

    public void setCountsId(String countsId) {
	this.countsId = countsId;
    }

    public String getCountsType() {
	return this.countsType;
    }

    public void setCountsType(String countsType) {
	this.countsType = countsType;
    }

    public String getCountsLotNo() {
	return countsLotNo;
    }

    public void setCountsLotNo(String countsLotNo) {
	this.countsLotNo = countsLotNo;
    }

    public String getSuperintendentCode() {
	return superintendentCode;
    }

    public void setSuperintendentCode(String superintendentCode) {
	this.superintendentCode = superintendentCode;
    }
    
    public String getSuperintendentName() {
        return superintendentName;
    }

    public void setSuperintendentName(String superintendentName) {
        this.superintendentName = superintendentName;
    }

    public String getActualSuperintendentCode() {
	return actualSuperintendentCode;
    }

    public void setActualSuperintendentCode(String actualSuperintendentCode) {
	this.actualSuperintendentCode = actualSuperintendentCode;
    }
    
    public String getActualSuperintendentName() {
        return actualSuperintendentName;
    }

    public void setActualSuperintendentName(String actualSuperintendentName) {
        this.actualSuperintendentName = actualSuperintendentName;
    }
    
    public Long getNotificationId() {
	return this.notificationId;
    }

    public void setNotificationId(Long notificationId) {
	this.notificationId = notificationId;
    }

    public Long getAdjustmentId() {
	return this.adjustmentId;
    }

    public void setAdjustmentId(Long adjustmentId) {
	this.adjustmentId = adjustmentId;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getIsImportedFile() {
	return isImportedFile;
    }

    public void setIsImportedFile(String isImportedFile) {
	this.isImportedFile = isImportedFile;
    }

    public Long getImportedTimes() {
	return importedTimes;
    }

    public void setImportedTimes(Long importedTimes) {
	this.importedTimes = importedTimes;
    }

    public String getIsPrinted() {
	return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
	this.isPrinted = isPrinted;
    }

    public Long getPrintedTimes() {
	return printedTimes;
    }

    public void setPrintedTimes(Long printedTimes) {
	this.printedTimes = printedTimes;
    }

    public Date getLastImportDate() {
	return lastImportDate;
    }

    public void setLastImportDate(Date lastImportDate) {
	this.lastImportDate = lastImportDate;
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

    public List<ImInventoryCountsLine> getImInventoryCountsLines() {
	return imInventoryCountsLines;
    }

    public void setImInventoryCountsLines(
	    List<ImInventoryCountsLine> imInventoryCountsLines) {
	this.imInventoryCountsLines = imInventoryCountsLines;
    }

	public Date getActualCountsDate() {
		return actualCountsDate;
	}

	public void setActualCountsDate(Date actualCountsDate) {
		this.actualCountsDate = actualCountsDate;
	}
}