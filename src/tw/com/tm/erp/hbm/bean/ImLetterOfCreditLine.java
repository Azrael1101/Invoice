package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImLetterOfCreditLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImLetterOfCreditLine implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -267620181409538793L;
    // Fields
    private Long lineId;
    private ImLetterOfCreditHead imLetterOfCreditHead;
    private String receiveNo;
    private Double receiveAmount;
    private Date arriveDate;
    private Date dueDate;
    private Double returnAmount;
    private Double returnFees;
    private String remark1;
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
    private String brandCode ;
    private String orderTypeCode; // 單別
    private Double creditAmount;
    
    // Constructors

	/** default constructor */
    public ImLetterOfCreditLine() {
    }

    /** full constructor */
    
    public ImLetterOfCreditLine(Long lineId,
			ImLetterOfCreditHead imLetterOfCreditHead, String receiveNo,
			Double receiveAmount, Date arriveDate, Date dueDate,
			Double returnAmount, Double returnFees, String remark1,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String status, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
			Long indexNo, String brandCode, String orderTypeCode,
			Double creditAmount) {
		super();
		this.lineId = lineId;
		this.imLetterOfCreditHead = imLetterOfCreditHead;
		this.receiveNo = receiveNo;
		this.receiveAmount = receiveAmount;
		this.arriveDate = arriveDate;
		this.dueDate = dueDate;
		this.returnAmount = returnAmount;
		this.returnFees = returnFees;
		this.remark1 = remark1;
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
		this.brandCode = brandCode;
		this.orderTypeCode = orderTypeCode;
		this.creditAmount = creditAmount;
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
    
    public String getReceiveNo() {
	return this.receiveNo;
    }

    public void setReceiveNo(String receiveNo) {
	this.receiveNo = receiveNo;
    }

    public Double getReceiveAmount() {
	return this.receiveAmount;
    }

    public void setReceiveAmount(Double receiveAmount) {
	this.receiveAmount = receiveAmount;
    }

    public Date getArriveDate() {
	return this.arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
	this.arriveDate = arriveDate;
    }

    public Date getDueDate() {
	return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
    }

    public Double getReturnAmount() {
	return this.returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
	this.returnAmount = returnAmount;
    }

    public Double getReturnFees() {
	return this.returnFees;
    }

    public void setReturnFees(Double returnFees) {
	this.returnFees = returnFees;
    }

    public String getRemark1() {
	return this.remark1;
    }

    public void setRemark1(String remark1) {
	this.remark1 = remark1;
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

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}
}