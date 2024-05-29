package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionShop entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosPromotionShop implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4619095590950320747L;
	private Long lineId;
	private PosPromotion posPromotion;
	private String shopCode;
	private String shopName;
	private Date beginDate;
	private Date endDate;
	private Long indexNo;
	// Constructors

	public PosPromotion getPosPromotion() {
	    return posPromotion;
	}

	public void setPosPromotion(PosPromotion posPromotion) {
	    this.posPromotion = posPromotion;
	}

	/** default constructor */
	public PosPromotionShop() {
	}

	/** minimal constructor */
	public PosPromotionShop(Long lineId) {
		this.lineId = lineId;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public Long getIndexNo() {
	    return indexNo;
	}

	public void setIndexNo(Long indexNo) {
	    this.indexNo = indexNo;
	}
}