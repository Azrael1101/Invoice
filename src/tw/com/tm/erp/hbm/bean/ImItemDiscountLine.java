package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemDiscountLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountLine implements java.io.Serializable {

	// Fields

	private Long lineId;
	private ImItemDiscountHead imItemDiscountHead;
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
	private String createBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;

	// Constructors

	/** default constructor */
	public ImItemDiscountLine() {
	}

	/** minimal constructor */
	public ImItemDiscountLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImItemDiscountLine(Long lineId, Long headId, String brandCode,
			String vipTypeCode, String itemDiscountType, Date beginDate,
			Date endDate, Long discount, String enable, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.lineId = lineId;
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
		this.createBy = createBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImItemDiscountHead getImItemDiscountHead() {
		return imItemDiscountHead;
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

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

	public void setImItemDiscountHead(ImItemDiscountHead imItemDiscountHead) {
		this.imItemDiscountHead = imItemDiscountHead;
	}

}