package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImOnHand entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImOnHand implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6735731664187169811L;
    // Fields
    private ImOnHandId id;
    private String brandCode;
    private Double stockOnHandQty = 0D;
    private Double outUncommitQty = 0D;
    private Double inUncommitQty = 0D;
    private Double moveUncommitQty = 0D;
    private Double otherUncommitQty = 0D;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;

    // Constructors

    /** default constructor */
    public ImOnHand() {
    }

    /** minimal constructor */
    public ImOnHand(ImOnHandId id) {
	this.id = id;
    }

	/** full constructor */
    public ImOnHand(ImOnHandId id, String brandCode, Double stockOnHandQty,
			Double outUncommitQty, Double inUncommitQty,
			Double moveUncommitQty, Double otherUncommitQty, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
		super();
		this.id = id;
		this.brandCode = brandCode;
		this.stockOnHandQty = stockOnHandQty;
		this.outUncommitQty = outUncommitQty;
		this.inUncommitQty = inUncommitQty;
		this.moveUncommitQty = moveUncommitQty;
		this.otherUncommitQty = otherUncommitQty;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

    // Property accessors

    public ImOnHandId getId() {
	return this.id;
    }

    public void setId(ImOnHandId id) {
	this.id = id;
    }

    public Double getStockOnHandQty() {
	return this.stockOnHandQty;
    }

    public void setStockOnHandQty(Double stockOnHandQty) {
	this.stockOnHandQty = stockOnHandQty;
    }

    public Double getOutUncommitQty() {
	return this.outUncommitQty;
    }

    public void setOutUncommitQty(Double outUncommitQty) {
	this.outUncommitQty = outUncommitQty;
    }

    public Double getInUncommitQty() {
	return this.inUncommitQty;
    }

    public void setInUncommitQty(Double inUncommitQty) {
	this.inUncommitQty = inUncommitQty;
    }

    public Double getMoveUncommitQty() {
	return this.moveUncommitQty;
    }

    public void setMoveUncommitQty(Double moveUncommitQty) {
	this.moveUncommitQty = moveUncommitQty;
    }

    public Double getOtherUncommitQty() {
	return this.otherUncommitQty;
    }

    public void setOtherUncommitQty(Double otherUncommitQty) {
	this.otherUncommitQty = otherUncommitQty;
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

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

}