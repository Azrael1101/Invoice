package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoShopDailyHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoShopDailyHead implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4594207602350558161L;
    // Fields
    private SoShopDailyHeadId id;
    private Long visitorCount;
    private Double totalActualSalesAmount;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String actualSalesAmount;
    private String lastUpdateTime;
    

    // Constructors

    /** default constructor */
    public SoShopDailyHead() {
    }

    /** minimal constructor */
    public SoShopDailyHead(SoShopDailyHeadId id) {
	this.id = id;
    }

    /** full constructor */
    public SoShopDailyHead(SoShopDailyHeadId id, Long visitorCount,
	    Double totalActualSalesAmount, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate) {
	this.id = id;
	this.visitorCount = visitorCount;
	this.totalActualSalesAmount = totalActualSalesAmount;
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

    public SoShopDailyHeadId getId() {
	return this.id;
    }

    public void setId(SoShopDailyHeadId id) {
	this.id = id;
    }

    public Long getVisitorCount() {
	return this.visitorCount;
    }

    public void setVisitorCount(Long visitorCount) {
	this.visitorCount = visitorCount;
    }

    public Double getTotalActualSalesAmount() {
	return this.totalActualSalesAmount;
    }

    public void setTotalActualSalesAmount(Double totalActualSalesAmount) {
	this.totalActualSalesAmount = totalActualSalesAmount;
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

    public String getActualSalesAmount() {
        return actualSalesAmount;
    }

    public void setActualSalesAmount(String actualSalesAmount) {
        this.actualSalesAmount = actualSalesAmount;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

	
}