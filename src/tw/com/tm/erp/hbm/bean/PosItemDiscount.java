package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosItemDiscount entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosItemDiscount implements java.io.Serializable {

	// Fields

	private Long headId;
	private String dataId;
	private String action;
	private String brandCode;
	private String vipTypeCode;
	private String itemDiscountType;
	private Date beginDate;
	private Date endDate;
	private Long discount;
	private String identification;

	// Constructors

	/** default constructor */
	public PosItemDiscount() {
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

}