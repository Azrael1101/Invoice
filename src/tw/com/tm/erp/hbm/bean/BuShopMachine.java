package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuShopMachine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopMachine implements java.io.Serializable {

    // Fields

    /**
	 * 
	 */
    private static final long serialVersionUID = -4223748842232926834L;
    private BuShopMachineId id;
    private String enable;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBye;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;
    private String lastOrderNo;
    private String uploadType;
	private String shopCode;
    private String posMachineCode;
    private String printerId;
    // Constructors

    /** default constructor */
    public BuShopMachine() {
    }

    /** minimal constructor */
    public BuShopMachine(BuShopMachineId id) {
	this.id = id;
    }

    /** full constructor */
    public BuShopMachine(BuShopMachineId id, String enable, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBye, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, Long indexNo) {
	this.id = id;
	this.enable = enable;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBye = createdBye;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
    }

    // Property accessors

    public BuShopMachineId getId() {
	return this.id;
    }

    public void setId(BuShopMachineId id) {
	this.id = id;
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

    public String getCreatedBye() {
	return this.createdBye;
    }

    public void setCreatedBye(String createdBye) {
	this.createdBye = createdBye;
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

    public String getLastOrderNo() {
        return lastOrderNo;
    }

    public void setLastOrderNo(String lastOrderNo) {
        this.lastOrderNo = lastOrderNo;
    }

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getPosMachineCode() {
		return posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

	public String getPrinterId() {
		return printerId;
	}

	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}

}