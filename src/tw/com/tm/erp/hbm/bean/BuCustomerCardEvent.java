package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCustomerEventId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCustomerCardEvent implements java.io.Serializable {

    // Fields 

    private static final long serialVersionUID = -391581885943199241L;
    private Long lineId;
    private BuCustomerCard buCustomerCard;
    private String cardNo;
    private String suspendCode;
    private String reasonCode;
    private Date eventDate;
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

    // Constructors

    /** default constructor */
    public BuCustomerCardEvent() {
    }

    // Property accessors

    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public String getSuspendCode() {
	return this.suspendCode;
    }

    public void setSuspendCode(String suspendCode) {
	this.suspendCode = suspendCode;
    }

    public String getReasonCode() {
	return this.reasonCode;
    }

    public void setReasonCode(String reasonCode) {
	this.reasonCode = reasonCode;
    }

    public Date getEventDate() {
	return this.eventDate;
    }

    public void setEventDate(Date eventDate) {
	this.eventDate = eventDate;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BuCustomerCard getBuCustomerCard() {
        return buCustomerCard;
    }

    public void setBuCustomerCard(BuCustomerCard buCustomerCard) {
        this.buCustomerCard = buCustomerCard;
    }
    
}