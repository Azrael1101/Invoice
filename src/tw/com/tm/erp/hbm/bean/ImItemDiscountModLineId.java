package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemDiscountModLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemDiscountModLineId implements java.io.Serializable {

	// Fields

	private Long lineId;
	private Long headId;
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
	public ImItemDiscountModLineId() {
	}

	/** minimal constructor */
	public ImItemDiscountModLineId(Long lineId) {
		this.lineId = lineId;
	}
	


	/** full constructor */
	public ImItemDiscountModLineId(Long lineId, Long headId, String brandCode,
			String vipTypeCode, String itemDiscountType, Date beginDate,
			Date endDate, Long discount, String enable, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.lineId = lineId;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImItemDiscountModLineId))
			return false;
		ImItemDiscountModLineId castOther = (ImItemDiscountModLineId) other;

		return ((this.getLineId() == castOther.getLineId()) || (this
				.getLineId() != null
				&& castOther.getLineId() != null && this.getLineId().equals(
				castOther.getLineId())))
				&& ((this.getHeadId() == castOther.getHeadId()) || (this
						.getHeadId() != null
						&& castOther.getHeadId() != null && this.getHeadId()
						.equals(castOther.getHeadId())))
				&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
						.getBrandCode() != null
						&& castOther.getBrandCode() != null && this
						.getBrandCode().equals(castOther.getBrandCode())))
				&& ((this.getVipTypeCode() == castOther.getVipTypeCode()) || (this
						.getVipTypeCode() != null
						&& castOther.getVipTypeCode() != null && this
						.getVipTypeCode().equals(castOther.getVipTypeCode())))
				&& ((this.getItemDiscountType() == castOther
						.getItemDiscountType()) || (this.getItemDiscountType() != null
						&& castOther.getItemDiscountType() != null && this
						.getItemDiscountType().equals(
								castOther.getItemDiscountType())))
				&& ((this.getBeginDate() == castOther.getBeginDate()) || (this
						.getBeginDate() != null
						&& castOther.getBeginDate() != null && this
						.getBeginDate().equals(castOther.getBeginDate())))
				&& ((this.getEndDate() == castOther.getEndDate()) || (this
						.getEndDate() != null
						&& castOther.getEndDate() != null && this.getEndDate()
						.equals(castOther.getEndDate())))
				&& ((this.getDiscount() == castOther.getDiscount()) || (this
						.getDiscount() != null
						&& castOther.getDiscount() != null && this
						.getDiscount().equals(castOther.getDiscount())))
				&& ((this.getEnable() == castOther.getEnable()) || (this
						.getEnable() != null
						&& castOther.getEnable() != null && this.getEnable()
						.equals(castOther.getEnable())))
				&& ((this.getReserve1() == castOther.getReserve1()) || (this
						.getReserve1() != null
						&& castOther.getReserve1() != null && this
						.getReserve1().equals(castOther.getReserve1())))
				&& ((this.getReserve2() == castOther.getReserve2()) || (this
						.getReserve2() != null
						&& castOther.getReserve2() != null && this
						.getReserve2().equals(castOther.getReserve2())))
				&& ((this.getReserve3() == castOther.getReserve3()) || (this
						.getReserve3() != null
						&& castOther.getReserve3() != null && this
						.getReserve3().equals(castOther.getReserve3())))
				&& ((this.getReserve4() == castOther.getReserve4()) || (this
						.getReserve4() != null
						&& castOther.getReserve4() != null && this
						.getReserve4().equals(castOther.getReserve4())))
				&& ((this.getReserve5() == castOther.getReserve5()) || (this
						.getReserve5() != null
						&& castOther.getReserve5() != null && this
						.getReserve5().equals(castOther.getReserve5())))
				&& ((this.getCreateBy() == castOther.getCreateBy()) || (this
						.getCreateBy() != null
						&& castOther.getCreateBy() != null && this
						.getCreateBy().equals(castOther.getCreateBy())))
				&& ((this.getCreationDate() == castOther.getCreationDate()) || (this
						.getCreationDate() != null
						&& castOther.getCreationDate() != null && this
						.getCreationDate().equals(castOther.getCreationDate())))
				&& ((this.getLastUpdatedBy() == castOther.getLastUpdatedBy()) || (this
						.getLastUpdatedBy() != null
						&& castOther.getLastUpdatedBy() != null && this
						.getLastUpdatedBy()
						.equals(castOther.getLastUpdatedBy())))
				&& ((this.getLastUpdateDate() == castOther.getLastUpdateDate()) || (this
						.getLastUpdateDate() != null
						&& castOther.getLastUpdateDate() != null && this
						.getLastUpdateDate().equals(
								castOther.getLastUpdateDate())))
				&& ((this.getIndexNo() == castOther.getIndexNo()) || (this
						.getIndexNo() != null
						&& castOther.getIndexNo() != null && this.getIndexNo()
						.equals(castOther.getIndexNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getLineId() == null ? 0 : this.getLineId().hashCode());
		result = 37 * result
				+ (getHeadId() == null ? 0 : this.getHeadId().hashCode());
		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getVipTypeCode() == null ? 0 : this.getVipTypeCode()
						.hashCode());
		result = 37
				* result
				+ (getItemDiscountType() == null ? 0 : this
						.getItemDiscountType().hashCode());
		result = 37 * result
				+ (getBeginDate() == null ? 0 : this.getBeginDate().hashCode());
		result = 37 * result
				+ (getEndDate() == null ? 0 : this.getEndDate().hashCode());
		result = 37 * result
				+ (getDiscount() == null ? 0 : this.getDiscount().hashCode());
		result = 37 * result
				+ (getEnable() == null ? 0 : this.getEnable().hashCode());
		result = 37 * result
				+ (getReserve1() == null ? 0 : this.getReserve1().hashCode());
		result = 37 * result
				+ (getReserve2() == null ? 0 : this.getReserve2().hashCode());
		result = 37 * result
				+ (getReserve3() == null ? 0 : this.getReserve3().hashCode());
		result = 37 * result
				+ (getReserve4() == null ? 0 : this.getReserve4().hashCode());
		result = 37 * result
				+ (getReserve5() == null ? 0 : this.getReserve5().hashCode());
		result = 37 * result
				+ (getCreateBy() == null ? 0 : this.getCreateBy().hashCode());
		result = 37
				* result
				+ (getCreationDate() == null ? 0 : this.getCreationDate()
						.hashCode());
		result = 37
				* result
				+ (getLastUpdatedBy() == null ? 0 : this.getLastUpdatedBy()
						.hashCode());
		result = 37
				* result
				+ (getLastUpdateDate() == null ? 0 : this.getLastUpdateDate()
						.hashCode());
		result = 37 * result
				+ (getIndexNo() == null ? 0 : this.getIndexNo().hashCode());
		return result;
	}

}