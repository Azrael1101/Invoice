package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImLetterOfCreditHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImLetterOfCreditHead implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2661260059008924966L;
    // Fields
    private Long headId;
    private String brandCode;
    private String lcNo;
    private String orderNo;			// 額外欄位 for process  = reserve5欄位
    private String orderTypeCode;	// 額外欄位 for process  = LC靜態變數	
    private Date lcDate;
    private String supplierCode;
    private String supplierName; 	// 非DB欄位 
    private String openingBankCode;	// 開狀銀行代碼
    private String openingBank;
    private String poNo;
    private Date latestShipmentDate;
    private Date expiryDate;
    private String currencyCode;
    private Double openingAmount;
    private Double openingFees;
    private Double creditNoted;
    private String apportionedFeesReceiveNo;
    private Double totalAlterAmount;
    private Double totalLcAmount;
    private Double totalReceiveAmount;
    private Double restAmount;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String status;
    private String statusName; 	//額外欄位 中文狀態
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private List<ImLetterOfCreditLine> imLetterOfCreditLines = new ArrayList(0);
    private List<ImLetterOfCreditAlter> imLetterOfCreditAlters = new ArrayList(0);

    // Constructors

    /** default constructor */
    public ImLetterOfCreditHead() {
    }

    public ImLetterOfCreditHead(Long headId) {
		this.headId = headId;
	}
    
	/** full constructor */
	public ImLetterOfCreditHead(Long headId, String brandCode, String lcNo,
			String orderNo, String orderTypeCode, Date lcDate,
			String supplierCode, String openingBank, String poNo,
			Date latestShipmentDate, Date expiryDate, String currencyCode,
			Double openingAmount, Double openingFees, Double creditNoted,
			String apportionedFeesReceiveNo, Double totalAlterAmount,
			Double totalLcAmount, Double totalReceiveAmount, Double restAmount,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String status, String statusName,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate,
			List<ImLetterOfCreditLine> imLetterOfCreditLines,
			List<ImLetterOfCreditAlter> imLetterOfCreditAlters) {
		super();
		this.headId = headId;
		this.brandCode = brandCode;
		this.lcNo = lcNo;
		this.orderNo = orderNo;
		this.orderTypeCode = orderTypeCode;
		this.lcDate = lcDate;
		this.supplierCode = supplierCode;
		this.openingBank = openingBank;
		this.poNo = poNo;
		this.latestShipmentDate = latestShipmentDate;
		this.expiryDate = expiryDate;
		this.currencyCode = currencyCode;
		this.openingAmount = openingAmount;
		this.openingFees = openingFees;
		this.creditNoted = creditNoted;
		this.apportionedFeesReceiveNo = apportionedFeesReceiveNo;
		this.totalAlterAmount = totalAlterAmount;
		this.totalLcAmount = totalLcAmount;
		this.totalReceiveAmount = totalReceiveAmount;
		this.restAmount = restAmount;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.status = status;
		this.statusName = statusName;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.imLetterOfCreditLines = imLetterOfCreditLines;
		this.imLetterOfCreditAlters = imLetterOfCreditAlters;
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

    public String getLcNo() {
	return this.lcNo;
    }

    public void setLcNo(String lcNo) {
	this.lcNo = lcNo;
    }

    public Date getLcDate() {
	return this.lcDate;
    }

    public void setLcDate(Date lcDate) {
	this.lcDate = lcDate;
    }

    public String getSupplierCode() {
	return this.supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
	this.supplierCode = supplierCode;
    }

    public String getOpeningBank() {
	return this.openingBank;
    }

    public void setOpeningBank(String openingBank) {
	this.openingBank = openingBank;
    }

    public String getPoNo() {
	return this.poNo;
    }

    public void setPoNo(String poNo) {
	this.poNo = poNo;
    }

    public Date getLatestShipmentDate() {
	return this.latestShipmentDate;
    }

    public void setLatestShipmentDate(Date latestShipmentDate) {
	this.latestShipmentDate = latestShipmentDate;
    }

    public Date getExpiryDate() {
	return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
    }

    public String getCurrencyCode() {
	return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
    }

    public Double getOpeningAmount() {
	return this.openingAmount;
    }

    public void setOpeningAmount(Double openingAmount) {
	this.openingAmount = openingAmount;
    }

    public Double getOpeningFees() {
	return this.openingFees;
    }

    public void setOpeningFees(Double openingFees) {
	this.openingFees = openingFees;
    }

    public Double getCreditNoted() {
	return this.creditNoted;
    }

    public void setCreditNoted(Double creditNoted) {
	this.creditNoted = creditNoted;
    }

    public String getApportionedFeesReceiveNo() {
	return this.apportionedFeesReceiveNo;
    }

    public void setApportionedFeesReceiveNo(String apportionedFeesReceiveNo) {
	this.apportionedFeesReceiveNo = apportionedFeesReceiveNo;
    }

    public Double getTotalAlterAmount() {
	return this.totalAlterAmount;
    }

    public void setTotalAlterAmount(Double totalAlterAmount) {
	this.totalAlterAmount = totalAlterAmount;
    }

    public Double getTotalLcAmount() {
	return this.totalLcAmount;
    }

    public void setTotalLcAmount(Double totalLcAmount) {
	this.totalLcAmount = totalLcAmount;
    }

    public Double getTotalReceiveAmount() {
	return this.totalReceiveAmount;
    }

    public void setTotalReceiveAmount(Double totalReceiveAmount) {
	this.totalReceiveAmount = totalReceiveAmount;
    }

    public Double getRestAmount() {
	return this.restAmount;
    }

    public void setRestAmount(Double restAmount) {
	this.restAmount = restAmount;
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

    public List<ImLetterOfCreditLine> getImLetterOfCreditLines() {
        return imLetterOfCreditLines;
    }

    public void setImLetterOfCreditLines(
    	List<ImLetterOfCreditLine> imLetterOfCreditLines) {
        this.imLetterOfCreditLines = imLetterOfCreditLines;
    }

    public List<ImLetterOfCreditAlter> getImLetterOfCreditAlters() {
        return imLetterOfCreditAlters;
    }

    public void setImLetterOfCreditAlters(
    	List<ImLetterOfCreditAlter> imLetterOfCreditAlters) {
        this.imLetterOfCreditAlters = imLetterOfCreditAlters;
    }

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOpeningBankCode() {
		return openingBankCode;
	}

	public void setOpeningBankCode(String openingBankCode) {
		this.openingBankCode = openingBankCode;
	}

	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName) {
	    this.supplierName = supplierName;
	}

}