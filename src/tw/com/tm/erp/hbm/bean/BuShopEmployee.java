package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuShopEmployee entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopEmployee implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -6561528912267580584L;
    private BuShopEmployeeId id;
    private String employeeName;
    private BuShop buShop;
    private String enable;
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
	private String shopCode;
    private String employeeCode;

    // Constructors

    /** default constructor */
    public BuShopEmployee() {
    }

    /** minimal constructor */
    public BuShopEmployee(BuShopEmployeeId id, BuShop buShop) {
	this.id = id;
	this.buShop = buShop;
    }

    /** full constructor */
    public BuShopEmployee(BuShopEmployeeId id, BuShop buShop, String enable,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String createdBy, Date creationDate,
	    String lastUpdatedBy, Date lastUpdateDate, Long indexNo) {
	this.id = id;
	this.buShop = buShop;
	this.enable = enable;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
    }

    // Property accessors

    public BuShopEmployeeId getId() {
	return this.id;
    }

    public void setId(BuShopEmployeeId id) {
	this.id = id;
    }

    public String getEmployeeName() {
	return employeeName;
    }

    public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
    }
    
    public BuShop getBuShop() {
	return this.buShop;
    }

    public void setBuShop(BuShop buShop) {
	this.buShop = buShop;
    }

    public String getEnable() {
	return this.enable;
    }

    public void setEnable(String enable) {
	this.enable = enable;
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

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
}