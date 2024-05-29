package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemDiscountMod entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PrSupplierMod implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6412841785471199789L;
	/**
     * 
     */
    
    // Fields
    private	Long headId;
    private String supplierNo;
    private String supplier;
    private String tel;
    private String fax;
    private String isAssessSup;
    private String invoiceTypeCode;
    private String unifiedUnmbering;
    private String name;
    private String executeInCharge;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
	private String lastUpdatedByName;	// 暫時欄位 更新人員
	private String orderTypeCode;
    private String orderNo;
    private String status;

    // Constructors

    /** default constructor */
    public PrSupplierMod() {
    }

    /** full constructor */
    public PrSupplierMod(Long headId ,String supplierNo, String supplier, String tel, 
	    String fax, String invoiceTypeCode, String unifiedUnmbering, String name, 
	    String executeInCharge, String enable, String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, String lastUpdatedByName, String orderTypeCode, String orderNo,String status) {
	this.headId = headId;
    this.supplierNo = supplierNo;
	this.supplier = supplier;
	this.tel = tel;	
	this.fax = fax;
	this.invoiceTypeCode = invoiceTypeCode;
	this.unifiedUnmbering = unifiedUnmbering;
	this.name = name;
	this.executeInCharge = executeInCharge;
	this.isAssessSup = isAssessSup;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.lastUpdatedByName = lastUpdatedByName;
	this.orderTypeCode =orderTypeCode;
	this.orderNo =orderNo;
	this.status = status;
    }

    // Property accessors
    
    public Long getHeadId() {
		return this.headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
    
    public String getSupplierNo() {
    	return this.supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
    	this.supplierNo = supplierNo;
    }

    public String getSupplier() {
    	return this.supplier;
    }

    public void setSupplier(String supplier) {
    	this.supplier = supplier;
    }

    public String getTel() {
    	return this.tel;
    }

    public void setTel(String tel) {
    	this.tel = tel;
    }

    public String getFax() {
    	return this.fax;
    }

    public void setFax(String fax) {
    	this.fax = fax;
    }

    public String getUnifiedUnmbering() {
    	return this.unifiedUnmbering;
    }

    public void setUnifiedUnmbering(String unifiedUnmbering) {
    	this.unifiedUnmbering = unifiedUnmbering;
    }

    public String getName() {
    	return this.name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getExecuteInCharge() {
    	return this.executeInCharge;
    }

    public void setExecuteInCharge(String executeInCharge) {
    	this.executeInCharge = executeInCharge;
    }
    
    public String getInvoiceTypeCode() {
    	return this.invoiceTypeCode;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
    	this.invoiceTypeCode = invoiceTypeCode;
    }
    
    public String getIsAssessSup() {
    	return this.isAssessSup;
    }
    
    public void setIsAssessSup(String isAssessSup) {
    	this.isAssessSup = isAssessSup;
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
    
	public String getOrderTypeCode() {
		return orderTypeCode;
	}
	
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}