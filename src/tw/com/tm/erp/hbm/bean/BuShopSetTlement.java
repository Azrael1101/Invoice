package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuShopSettlementId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopSetTlement implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long lineId;
    private String shopCode;
    private String discountName;
    private Double discountRate;
    private Double setTlementRate;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private String lastUpdatedByName;	// 暫時欄位 更新人員
    private String brandCode;
    
    // Constructors
    public BuShopSetTlement(){
	super(); 
    }
    
    public BuShopSetTlement(Long lineId, String shopCode,
	    String discountName, Double discountRate, Double setTlementRate,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, String lastUpdatedByName) {
	super();
	this.lineId = lineId;
	this.shopCode = shopCode;
	this.discountName = discountName;
	this.discountRate = discountRate;
	this.setTlementRate = setTlementRate;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.lastUpdatedByName = lastUpdatedByName;
    }

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getDiscountName() {
	return this.discountName;
    }

    public void setDiscountName(String discountName) {
	this.discountName = discountName;
    }

    public Double getDiscountRate() {
	return this.discountRate;
    }

    public void setDiscountRate(Double discountRate) {
	this.discountRate = discountRate;
    }

    public Double getSetTlementRate() {
	return this.setTlementRate;
    }

    public void setSetTlementRate(Double setTlementRate) {
	this.setTlementRate = setTlementRate;
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

    public String getLastUpdatedByName() {
        return lastUpdatedByName;
    }

    public void setLastUpdatedByName(String lastUpdatedByName) {
        this.lastUpdatedByName = lastUpdatedByName;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }



}