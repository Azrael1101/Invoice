package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemDiscountMod entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PrSupplier implements java.io.Serializable {

 
    
    // Fields
    private String supplierNo;
    private String supplier;
    private String tel;
    private String fax;
    private String isAssessSup;
    private String invoiceTypeCode;
    private String unifiedUnmbering;
    private String name;
    private String excuteInCharge;
    private String orderNo;

    // Constructors

    /** default constructor */
    public PrSupplier() {
    }

    /** full constructor */
    public PrSupplier(String supplierNo, String supplier, String tel, 
	    String fax, String invoiceTypeCode, String unifiedUnmbering, String name, 
	    String excuteInCharge, String isAccessSup, String orderNo) {
    	this.supplierNo = supplierNo;
		this.supplier = supplier;
		this.tel = tel;	
		this.fax = fax;
		this.invoiceTypeCode = invoiceTypeCode;
		this.unifiedUnmbering = unifiedUnmbering;
		this.name = name;
		this.excuteInCharge = excuteInCharge;
		this.isAssessSup = isAccessSup;
		this.orderNo =orderNo;
    }

    // Property accessors
    
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

    public String getExcuteInCharge() {
    	return this.excuteInCharge;
    }

    public void setExcuteInCharge(String excuteInCharge) {
    	this.excuteInCharge = excuteInCharge;
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

	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}