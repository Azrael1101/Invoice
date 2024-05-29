package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCompanyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCompany implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 1860447158807212169L;
    private String companyCode;
    private String companyName;
    private String guiCode;
    private String businessMasterName;
    private String taxRegisterNo;
    private String registerMasterName;
    private String masterName;
    private String accreditee;
    private String address;
    private String tel;
    private String reportTitle;
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
    public BuCompany() {
    }

    /** full constructor */
    public BuCompany(String companyCode, String companyName, String guiCode,
	    String businessMasterName, String taxRegisterNo,
	    String registerMasterName, String masterName, String accreditee,
	    String address, String tel, String reportTitle, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate) {
	this.companyCode = companyCode;
	this.companyName = companyName;
	this.guiCode = guiCode;
	this.businessMasterName = businessMasterName;
	this.taxRegisterNo = taxRegisterNo;
	this.registerMasterName = registerMasterName;
	this.masterName = masterName;
	this.accreditee = accreditee;
	this.address = address;
	this.tel = tel;
	this.reportTitle = reportTitle;
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

    public String getCompanyCode() {
	return this.companyCode;
    }
    public void setCompanyCode(String companyCode) {
	this.companyCode = companyCode;
    }
    public String getCompanyName() {
	return this.companyName;
    }
    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }
    public String getGuiCode() {
	return this.guiCode;
    }
    public void setGuiCode(String guiCode) {
	this.guiCode = guiCode;
    }
    public String getBusinessMasterName() {
	return this.businessMasterName;
    }
    public void setBusinessMasterName(String businessMasterName) {
	this.businessMasterName = businessMasterName;
    }
    public String getTaxRegisterNo() {
	return this.taxRegisterNo;
    }
    public void setTaxRegisterNo(String taxRegisterNo) {
	this.taxRegisterNo = taxRegisterNo;
    }
    public String getRegisterMasterName() {
	return this.registerMasterName;
    }
    public void setRegisterMasterName(String registerMasterName) {
	this.registerMasterName = registerMasterName;
    }
    public String getMasterName() {
	return this.masterName;
    }
    public void setMasterName(String masterName) {
	this.masterName = masterName;
    }
    public String getAccreditee() {
	return this.accreditee;
    }
    public void setAccreditee(String accreditee) {
	this.accreditee = accreditee;
    }
    public String getAddress() {
	return this.address;
    }
    public void setAddress(String address) {
	this.address = address;
    }
    public String getTel() {
	return this.tel;
    }
    public void setTel(String tel) {
	this.tel = tel;
    }
    public String getReportTitle() {
	return this.reportTitle;
    }
    public void setReportTitle(String reportTitle) {
	this.reportTitle = reportTitle;
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