package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionConditionId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosPromotionCondition implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = -2302438521536578701L;
    private PosPromotion posPromotion;
    private Long lineId;
    private Long conditionQuantity;
    private Long conditionMoney;
    private Long discountPecent;
    private Long discountMoney;
    private String presentGroup;
    private Long presentQuantity;
    private String itemGroup;
    private Long presentAddPrice;
    private Long presentDiscountPecent;
    private Long presentDiscountMoney;
    private Long indexNo;

    // Constructors

    public PosPromotion getPosPromotion() {
        return posPromotion;
    }


    public void setPosPromotion(PosPromotion posPromotion) {
        this.posPromotion = posPromotion;
    }


    /** default constructor */
    public PosPromotionCondition() {
    }


    public Long getLineId() {
	return this.lineId;
    }

    public void setLineId(Long lineId) {
	this.lineId = lineId;
    }

    public Long getConditionQuantity() {
	return this.conditionQuantity;
    }

    public void setConditionQuantity(Long conditionQuantity) {
	this.conditionQuantity = conditionQuantity;
    }

    public Long getConditionMoney() {
	return this.conditionMoney;
    }

    public void setConditionMoney(Long conditionMoney) {
	this.conditionMoney = conditionMoney;
    }

    public Long getDiscountPecent() {
	return this.discountPecent;
    }

    public void setDiscountPecent(Long discountPecent) {
	this.discountPecent = discountPecent;
    }

    public Long getDiscountMoney() {
	return this.discountMoney;
    }

    public void setDiscountMoney(Long discountMoney) {
	this.discountMoney = discountMoney;
    }

    public String getPresentGroup() {
	return this.presentGroup;
    }

    public void setPresentGroup(String presentGroup) {
	this.presentGroup = presentGroup;
    }

    public Long getPresentQuantity() {
	return this.presentQuantity;
    }

    public void setPresentQuantity(Long presentQuantity) {
	this.presentQuantity = presentQuantity;
    }

    public String getItemGroup() {
	return this.itemGroup;
    }

    public void setItemGroup(String itemGroup) {
	this.itemGroup = itemGroup;
    }

    public Long getPresentAddPrice() {
	return this.presentAddPrice;
    }

    public void setPresentAddPrice(Long presentAddPrice) {
	this.presentAddPrice = presentAddPrice;
    }

    public Long getPresentDiscountPecent() {
	return this.presentDiscountPecent;
    }

    public void setPresentDiscountPecent(Long presentDiscountPecent) {
	this.presentDiscountPecent = presentDiscountPecent;
    }

    public Long getPresentDiscountMoney() {
	return this.presentDiscountMoney;
    }

    public void setPresentDiscountMoney(Long presentDiscountMoney) {
	this.presentDiscountMoney = presentDiscountMoney;
    }


    public Long getIndexNo() {
        return indexNo;
    }


    public void setIndexNo(Long indexNo) {
        this.indexNo = indexNo;
    }

}