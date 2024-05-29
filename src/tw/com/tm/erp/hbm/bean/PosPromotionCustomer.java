package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionCustomer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosPromotionCustomer implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6067246326891095460L;
	private Long lineId;
	private PosPromotion posPromotion;
	private String vipTypeCode;
	private Long indexNo;
	// Constructors

	public PosPromotion getPosPromotion() {
	    return posPromotion;
	}

	public void setPosPromotion(PosPromotion posPromotion) {
	    this.posPromotion = posPromotion;
	}

	/** default constructor */
	public PosPromotionCustomer() {
	}

	/** minimal constructor */
	public PosPromotionCustomer(Long lineId) {
		this.lineId = lineId;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getVipTypeCode() {
		return this.vipTypeCode;
	}

	public void setVipTypeCode(String vipTypeCode) {
		this.vipTypeCode = vipTypeCode;
	}

	public Long getIndexNo() {
	    return indexNo;
	}

	public void setIndexNo(Long indexNo) {
	    this.indexNo = indexNo;
	}

}