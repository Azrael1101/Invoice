package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionCustomer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionCustomer implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6067246326891095460L;
	private Long lineId;
	private ImPromotion imPromotion;
	private String vipTypeCode;
	private String vipTypeName;
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

	// Constructors

	/** default constructor */
	public ImPromotionCustomer() {
	}

	/** minimal constructor */
	public ImPromotionCustomer(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImPromotionCustomer(Long lineId, ImPromotion imPromotion,
			String vipTypeCode, String vipTypeName, String enable, String reserve1, 
			String reserve2, String reserve3, String reserve4, 
			String reserve5,String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, Long indexNo) {
		this.lineId = lineId;
		this.imPromotion = imPromotion;
		this.vipTypeCode = vipTypeCode;
		this.vipTypeName = vipTypeName;
		this.enable = enable;
		this.reserve1 =reserve1;
		this.reserve2 =reserve2;
		this.reserve3 =reserve3;
		this.reserve4 =reserve4;
		this.reserve5 =reserve5;
		this.createdBy = createdBy;
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

	public ImPromotion getImPromotion() {
		return this.imPromotion;
	}

	public void setImPromotion(ImPromotion imPromotion) {
		this.imPromotion = imPromotion;
	}

	public String getVipTypeCode() {
		return this.vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}

	public String getVipTypeName() {
		return vipTypeName;
	}

	public void setVipTypeName(String vipTypeName) {
		this.vipTypeName = vipTypeName;
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

}