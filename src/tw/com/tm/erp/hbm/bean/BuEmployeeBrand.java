package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeBrand entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeBrand implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5334670183689457728L;
    // Fields
    private BuEmployeeBrandId id;
    private BuEmployee buEmployee;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;

    // Constructors

    /** default constructor */
    public BuEmployeeBrand() {
    }
    
    /** minimal constructor */
    public BuEmployeeBrand(BuEmployeeBrandId id) {
	this.id = id;
    }

    /** minimal constructor */
    public BuEmployeeBrand(BuEmployeeBrandId id, BuEmployee buEmployee) {
	this.id = id;
	this.buEmployee = buEmployee;
    }

    /** full constructor */
    public BuEmployeeBrand(BuEmployeeBrandId id, BuEmployee buEmployee,
	    String attribute1, String attribute2, String attribute3,
	    String attribute4, String attribute5, String createdBy,
	    Date creationDate, String lastUpdatedBy, Date lastUpdateDate,
	    Long indexNo) {
	this.id = id;
	this.buEmployee = buEmployee;
	this.attribute1 = attribute1;
	this.attribute2 = attribute2;
	this.attribute3 = attribute3;
	this.attribute4 = attribute4;
	this.attribute5 = attribute5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
    }

    // Property accessors

    public BuEmployeeBrandId getId() {
	return this.id;
    }

    public void setId(BuEmployeeBrandId id) {
	this.id = id;
    }

    public BuEmployee getBuEmployee() {
	return this.buEmployee;
    }

    public void setBuEmployee(BuEmployee buEmployee) {
	this.buEmployee = buEmployee;
    }

    public String getAttribute1() {
	return this.attribute1;
    }

    public void setAttribute1(String attribute1) {
	this.attribute1 = attribute1;
    }

    public String getAttribute2() {
	return this.attribute2;
    }

    public void setAttribute2(String attribute2) {
	this.attribute2 = attribute2;
    }

    public String getAttribute3() {
	return this.attribute3;
    }

    public void setAttribute3(String attribute3) {
	this.attribute3 = attribute3;
    }

    public String getAttribute4() {
	return this.attribute4;
    }

    public void setAttribute4(String attribute4) {
	this.attribute4 = attribute4;
    }

    public String getAttribute5() {
	return this.attribute5;
    }

    public void setAttribute5(String attribute5) {
	this.attribute5 = attribute5;
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

}