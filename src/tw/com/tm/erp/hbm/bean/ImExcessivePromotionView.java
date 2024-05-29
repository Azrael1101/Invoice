package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImExcessivePromotionView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2170036678961269657L;
    /**
     * 
     */
    
    // Fields
    private String brandCode;
    private String promotionCode;
    private String promotionName;
    private String inCharge;
    private String isAllItem;
    private String isAllShop;
    private String isAllCustomer;
    private String shopCode;
    private Date beginDate;
    private Date endDate;
    private String priceType;
    private String customerType;
    private String itemCode;
    private String discountType;
    private Double discount;
    private Double originalPrice;  // 原價
    private Double promotionUnitPrice; // 促銷價
    private Date lastUpdateDate;

    // Constructors

    /** default constructor */
    public ImExcessivePromotionView() {
    }

    /** minimal constructor */
    public ImExcessivePromotionView(String brandCode) {
	this.brandCode = brandCode;
    }

    /** full constructor */

    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getPromotionCode() {
	return this.promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
	this.promotionCode = promotionCode;
    }

    public String getPromotionName() {
	return this.promotionName;
    }

    public void setPromotionName(String promotionName) {
	this.promotionName = promotionName;
    }

    public String getInCharge() {
	return this.inCharge;
    }

    public void setInCharge(String inCharge) {
	this.inCharge = inCharge;
    }

    public String getIsAllItem() {
	return this.isAllItem;
    }

    public void setIsAllItem(String isAllItem) {
	this.isAllItem = isAllItem;
    }

    public String getIsAllShop() {
	return this.isAllShop;
    }

    public void setIsAllShop(String isAllShop) {
	this.isAllShop = isAllShop;
    }

    public String getIsAllCustomer() {
	return this.isAllCustomer;
    }

    public void setIsAllCustomer(String isAllCustomer) {
	this.isAllCustomer = isAllCustomer;
    }

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
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

    public String getPriceType() {
	return this.priceType;
    }

    public void setPriceType(String priceType) {
	this.priceType = priceType;
    }

    public String getCustomerType() {
	return this.customerType;
    }

    public void setCustomerType(String customerType) {
	this.customerType = customerType;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
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

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getPromotionUnitPrice() {
        return promotionUnitPrice;
    }

    public void setPromotionUnitPrice(Double promotionUnitPrice) {
        this.promotionUnitPrice = promotionUnitPrice;
    }
}