package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoShopDailyCompetitor entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoShopDailyCompetitor implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7867221384874183908L;
    // Fields
    private SoShopDailyCompetitorId id;
    private Double competitorPerformance;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;

    // Constructors

    /** default constructor */
    public SoShopDailyCompetitor() {
    }

    /** minimal constructor */
    public SoShopDailyCompetitor(SoShopDailyCompetitorId id) {
	this.id = id;
    }

    /** full constructor */
    public SoShopDailyCompetitor(SoShopDailyCompetitorId id,
	    Double competitorPerformance, String reserve1, String reserve2,
	    String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate) {
	this.id = id;
	this.competitorPerformance = competitorPerformance;
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

    public SoShopDailyCompetitorId getId() {
	return this.id;
    }

    public void setId(SoShopDailyCompetitorId id) {
	this.id = id;
    }

    public Double getCompetitorPerformance() {
	return this.competitorPerformance;
    }

    public void setCompetitorPerformance(Double competitorPerformance) {
	this.competitorPerformance = competitorPerformance;
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

}