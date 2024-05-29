package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2156991080619585096L;
    
    // Fields
    private Long id;
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
    private String vipTypeCode;
    private Double unitPrice;
    private Double promotionPrice;
    private Date lastUpdateDate;

    // Constructors

    /** default constructor */
    public ImPromotionView() {
    }

    /** minimal constructor */
    public ImPromotionView(String brandCode) {
	this.brandCode = brandCode;
    }

    /** full constructor */
    public ImPromotionView(Long id, String brandCode, String promotionCode,
	    String promotionName, String inCharge, String isAllItem,
	    String isAllShop, String isAllCustomer, String shopCode,
	    Date beginDate, Date endDate, String priceType,
	    String customerType, String itemCode, String discountType,
	    Double discount, String vipTypeCode, Double unitPrice, Double promotionPrice, Date lastUpdateDate) {
	this.id = id;
	this.brandCode = brandCode;
	this.promotionCode = promotionCode;
	this.promotionName = promotionName;
	this.inCharge = inCharge;
	this.isAllItem = isAllItem;
	this.isAllShop = isAllShop;
	this.isAllCustomer = isAllCustomer;
	this.shopCode = shopCode;
	this.beginDate = beginDate;
	this.endDate = endDate;
	this.priceType = priceType;
	this.customerType = customerType;
	this.itemCode = itemCode;
	this.discountType = discountType;
	this.discount = discount;
	this.vipTypeCode = vipTypeCode;
	this.unitPrice = unitPrice;
	this.promotionPrice = promotionPrice;
	this.lastUpdateDate = lastUpdateDate;
    }

    // Property accessors

    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }
    
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

    public String getVipTypeCode() {
	return this.vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
	this.vipTypeCode = vipTypeCode;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}