package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosPromotionItem implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 4586392534106745480L;
	private Long lineId;
	private PosPromotion posPromotion;
	private String itemCode;
	private Double originalPrice;
	private String discountType;
	private Double discount;
	private Double discountAmount;
	private Double discountPercentage;
	private Double totalDiscountAmount;
	private Double totalDiscountPercentage;
	private Long indexNo;
	private Double quantity;
	private String itemGroup; // 商品群組
	
	public PosPromotion getPosPromotion() {
	    return posPromotion;
	}

	public void setPosPromotion(PosPromotion posPromotion) {
	    this.posPromotion = posPromotion;
	}

	// Constructors
	/** default constructor */
	public PosPromotionItem() {
	}

	/** minimal constructor */
	public PosPromotionItem(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
		

	public Double getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDiscountType() {
		return this.discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getTotalDiscountPercentage() {
		return totalDiscountPercentage;
	}

	public void setTotalDiscountPercentage(Double totalDiscountPercentage) {
		this.totalDiscountPercentage = totalDiscountPercentage;
	}


	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getItemGroup() {
	    return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
	    this.itemGroup = itemGroup;
	}
}