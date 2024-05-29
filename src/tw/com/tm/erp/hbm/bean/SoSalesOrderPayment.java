package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoSalesOrderPayment entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoSalesOrderPayment implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3763012315068678545L;
    // Fields
    private Long posPaymentId;
    private SoSalesOrderHead soSalesOrderHead;
    private String posPaymentType;
    private String localCurrencyCode;
    private Double localAmount;
    private String foreignCurrencyCode;
    private Double foreignAmount;
    private Double discountRate;
    private String remark1;
    private String remark2;
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
    private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
    private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
    private String message; // line 訊息的顯示
    private String payNo;
    private Double payQty;
    private Double exchangeRate;
    
    private String reCheckId;
    private String orderNo;
    private String userPayNo;
    
    private String appCustomerCode;
    
    // Constructors

    /** default constructor */
    public SoSalesOrderPayment() {
    }

    /** minimal constructor */
    public SoSalesOrderPayment(Long posPaymentId) {
	this.posPaymentId = posPaymentId;
    }

    /** full constructor */
    public SoSalesOrderPayment(SoSalesOrderHead soSalesOrderHead,
	    String posPaymentType, String localCurrencyCode,
	    Double localAmount, String foreignCurrencyCode,
	    Double foreignAmount, Double discountRate, String remark1, String remark2,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String status, String createdBy,
	    Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
	    Long indexNo, String isDeleteRecord, String isLockRecord, String message,
	    String payNo, Double payQty, Double exchangeRate,String appCustomerCode) {
	this.soSalesOrderHead = soSalesOrderHead;
	this.posPaymentType = posPaymentType;
	this.localCurrencyCode = localCurrencyCode;
	this.localAmount = localAmount;
	this.foreignCurrencyCode = foreignCurrencyCode;
	this.foreignAmount = foreignAmount;
	this.discountRate = discountRate;
	this.remark1 = remark1;
	this.remark2 = remark2;
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
	this.payNo = payNo;
	this.payQty = payQty;
	this.exchangeRate = exchangeRate;
	this.appCustomerCode = appCustomerCode;
    }

    // Property accessors

    public Long getPosPaymentId() {
	return this.posPaymentId;
    }

    public void setPosPaymentId(Long posPaymentId) {
	this.posPaymentId = posPaymentId;
    }

    public SoSalesOrderHead getSoSalesOrderHead() {
	return this.soSalesOrderHead;
    }

    public void setSoSalesOrderHead(SoSalesOrderHead soSalesOrderHead) {
	this.soSalesOrderHead = soSalesOrderHead;
    }

    public String getPosPaymentType() {
	return this.posPaymentType;
    }

    public void setPosPaymentType(String posPaymentType) {
	this.posPaymentType = posPaymentType;
    }

    public String getLocalCurrencyCode() {
	return this.localCurrencyCode;
    }

    public void setLocalCurrencyCode(String localCurrencyCode) {
	this.localCurrencyCode = localCurrencyCode;
    }

    public Double getLocalAmount() {
	return this.localAmount;
    }

    public void setLocalAmount(Double localAmount) {
	this.localAmount = localAmount;
    }

    public String getForeignCurrencyCode() {
	return this.foreignCurrencyCode;
    }

    public void setForeignCurrencyCode(String foreignCurrencyCode) {
	this.foreignCurrencyCode = foreignCurrencyCode;
    }

    public Double getForeignAmount() {
	return this.foreignAmount;
    }

    public void setForeignAmount(Double foreignAmount) {
	this.foreignAmount = foreignAmount;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }
    
    public String getRemark1() {
	return remark1;
    }

    public void setRemark1(String remark1) {
	this.remark1 = remark1;
    }

    public String getRemark2() {
	return remark2;
    }

    public void setRemark2(String remark2) {
	this.remark2 = remark2;
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
	return status;
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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Double getPayQty() {
		return payQty;
	}

	public void setPayQty(Double payQty) {
		this.payQty = payQty;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getReCheckId() {
		return reCheckId;
	}

	public void setReCheckId(String reCheckId) {
		this.reCheckId = reCheckId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserPayNo() {
		return userPayNo;
	}

	public void setUserPayNo(String userPayNo) {
		this.userPayNo = userPayNo;
	}

	public String getAppCustomerCode() {
		return appCustomerCode;
	}

	public void setAppCustomerCode(String appCustomerCode) {
		this.appCustomerCode = appCustomerCode;
	}
	
}