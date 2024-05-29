package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemDiscountMod entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountMod implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6412841785471199789L;
	/**
     * 
     */
    
    // Fields
    private	Long headId;
    private String brandCode;
    private String vipTypeCode;
    private String itemDiscountType;
    private Date beginDate;
    private Date endDate;
    private Long discount;
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
	private String lastUpdatedByName;	// 暫時欄位 更新人員
	private String orderTypeCode;
    private String orderNo;
    private String status;

    // Constructors

    /** default constructor */
    public ImItemDiscountMod() {
    }

    /** full constructor */
    public ImItemDiscountMod(Long headId ,String brandCode, String vipTypeCode, String itemDiscountType, 
	    Date beginDate, Date endDate,Long discount, String enable, String reserve1, 
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, String lastUpdatedByName, String orderTypeCode, String orderNo,String status) {
	this.headId = headId;
    this.brandCode = brandCode;
	this.vipTypeCode = vipTypeCode;
	this.itemDiscountType = itemDiscountType;	
	this.beginDate = beginDate;
	this.endDate = endDate;
	this.discount = discount;
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
    
    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getVipTypeCode() {
	return this.vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
	this.vipTypeCode = vipTypeCode;
    }

    public String getItemDiscountType() {
	return this.itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
	this.itemDiscountType = itemDiscountType;
    }

    public Date getBeginDate() {
	return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public Date getEndDate() {
	return this.endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public Long getDiscount() {
	return this.discount;
    }

    public void setDiscount(Long discount) {
	this.discount = discount;
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