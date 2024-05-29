package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImLetterOfCreditAlter entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImLetterOfCreditAlter implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4460514586695553871L;
    // Fields
    private Long lineId;
    private ImLetterOfCreditHead imLetterOfCreditHead;
    private Date alterLcDate;
    private Double alterAmount;
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
    private Long indexNo;
    private String isDeleteRecord = "0";
    private String isLockRecord = "0";
    private String message;
    // Constructors

	/** default constructor */
    public ImLetterOfCreditAlter() {
    }

    /** full constructor */
    public ImLetterOfCreditAlter(Long lineId,
			ImLetterOfCreditHead imLetterOfCreditHead, Date alterLcDate,
			Double alterAmount, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, String status,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo, String isDeleteRecord,
			String isLockRecord, String message) {
		super();
		this.lineId = lineId;
		this.imLetterOfCreditHead = imLetterOfCreditHead;
		this.alterLcDate = alterLcDate;
		this.alterAmount = alterAmount;
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

    public ImLetterOfCreditHead getImLetterOfCreditHead() {
        return imLetterOfCreditHead;
    }

    public void setImLetterOfCreditHead(ImLetterOfCreditHead imLetterOfCreditHead) {
        this.imLetterOfCreditHead = imLetterOfCreditHead;
    }

    public Date getAlterLcDate() {
	return this.alterLcDate;
    }

    public void setAlterLcDate(Date alterLcDate) {
	this.alterLcDate = alterLcDate;
    }

    public Double getAlterAmount() {
	return this.alterAmount;
    }

    public void setAlterAmount(Double alterAmount) {
	this.alterAmount = alterAmount;
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

    public Long getIndexNo() {
	return this.indexNo;
    }

    public void setIndexNo(Long indexNo) {
	this.indexNo = indexNo;
    }

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}