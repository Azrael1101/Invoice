package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImReplenishHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReplenishHead implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -7649023087112185783L;
    private Long headId;
    private String brandCode;
    private String warehouseCode;
    private String warehouseArea;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    
    private List<ImReplenishCalendar> imReplenishCalendarLines = new ArrayList();
    private List<ImReplenishDisplay> imReplenishDisplayLines = new ArrayList();
    private List<ImReplenishLimition> imReplenishLimitionLines = new ArrayList();
    
    private String warehouseName;	// 暫存欄位
    private String createdNameBy;	// 暫存欄位
    private String lastUpdatedNameBy;	// 暫存欄位

    // Constructors

    /** default constructor */
    public ImReplenishHead() {
    }
    
    public ImReplenishHead(Long headId) {
	this.headId = headId;
    }
    
    /** full constructor */
    public ImReplenishHead(String brandCode, String warehouseCode,
	    String warehouseArea, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate) {
	this.brandCode = brandCode;
	this.warehouseCode = warehouseCode;
	this.warehouseArea = warehouseArea;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
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

    public String getWarehouseCode() {
	return this.warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
	this.warehouseCode = warehouseCode;
    }

    public String getWarehouseArea() {
	return this.warehouseArea;
    }

    public void setWarehouseArea(String warehouseArea) {
	this.warehouseArea = warehouseArea;
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

    public String getCreatedNameBy() {
        return createdNameBy;
    }

    public void setCreatedNameBy(String createdNameBy) {
        this.createdNameBy = createdNameBy;
    }

    public String getLastUpdatedNameBy() {
        return lastUpdatedNameBy;
    }

    public void setLastUpdatedNameBy(String lastUpdatedNameBy) {
        this.lastUpdatedNameBy = lastUpdatedNameBy;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public List<ImReplenishCalendar> getImReplenishCalendarLines() {
        return imReplenishCalendarLines;
    }

    public void setImReplenishCalendarLines(
    	List<ImReplenishCalendar> imReplenishCalendarLines) {
        this.imReplenishCalendarLines = imReplenishCalendarLines;
    }

    public List<ImReplenishDisplay> getImReplenishDisplayLines() {
        return imReplenishDisplayLines;
    }

    public void setImReplenishDisplayLines(
    	List<ImReplenishDisplay> imReplenishDisplayLines) {
        this.imReplenishDisplayLines = imReplenishDisplayLines;
    }

    public List<ImReplenishLimition> getImReplenishLimitionLines() {
        return imReplenishLimitionLines;
    }

    public void setImReplenishLimitionLines(
    	List<ImReplenishLimition> imReplenishLimitionLines) {
        this.imReplenishLimitionLines = imReplenishLimitionLines;
    }
}