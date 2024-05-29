package tw.com.tm.erp.hbm.bean;

/**
 * ImPromotionFull entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionFull implements java.io.Serializable {

	// Fields

	private Long lineId;
	private ImPromotion imPromotion;
	private String vipDiscountCode;
	private Long discountRate;
	private Long indexNo;
	private String isJoin;

	// Constructors

	/** default constructor */
	public ImPromotionFull() {
	}

	/** minimal constructor */
	public ImPromotionFull(Long lineId) {
		this.lineId = lineId;
	}

	

	// Property accessors

	/**
	 * @param lineId
	 * @param imPromotion
	 * @param vipDiscountCode
	 * @param discountRate
	 * @param indexNo
	 */
	public ImPromotionFull(Long lineId, ImPromotion imPromotion,
			String vipDiscountCode, Long discountRate, Long indexNo) {
		this.lineId = lineId;
		this.imPromotion = imPromotion;
		this.vipDiscountCode = vipDiscountCode;
		this.discountRate = discountRate;
		this.indexNo = indexNo;
	}

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	/**
	 * @return the imPromotion
	 */
	public ImPromotion getImPromotion() {
		return imPromotion;
	}

	/**
	 * @param imPromotion the imPromotion to set
	 */
	public void setImPromotion(ImPromotion imPromotion) {
		this.imPromotion = imPromotion;
	}

	public String getVipDiscountCode() {
		return this.vipDiscountCode;
	}

	public void setVipDiscountCode(String vipDiscountCode) {
		this.vipDiscountCode = vipDiscountCode;
	}

	public Long getDiscountRate() {
		return this.discountRate;
	}

	public void setDiscountRate(Long discountRate) {
		this.discountRate = discountRate;
	}

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	/**
	 * @return the isJoin
	 */
	public String getIsJoin() {
		return isJoin;
	}

	/**
	 * @param isJoin the isJoin to set
	 */
	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}
	

}