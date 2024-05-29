package tw.com.tm.erp.hbm.bean;

/**
 * ImItemEanPriceView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemEanPriceView implements java.io.Serializable {

    private static final long serialVersionUID = -9019603366420324086L;
    // Fields
    private String brandCode;
    private String eanCode;
    private String itemCode;
    private String itemCName;
    private String itemEName;
    private String salesUnit;
    private String currencyCode;
    private Double unitPrice;
    private String isServiceItem;
    private String itemEnable;
    private String eanEnable;
    private String priceEnable;
    private String isTax;

    // Constructors
    /** default constructor */
    public ImItemEanPriceView() {
    }

    /** full constructor */
    public ImItemEanPriceView(String brandCode, String eanCode,
	    String itemCode, String itemCName, String itemEName,
	    String salesUnit, String currencyCode, Double unitPrice,
	    String isServiceItem, String itemEnable, String eanEnable,
	    String priceEnable, String isTax) {
	super();
	this.brandCode = brandCode;
	this.eanCode = eanCode;
	this.itemCode = itemCode;
	this.itemCName = itemCName;
	this.itemEName = itemEName;
	this.salesUnit = salesUnit;
	this.currencyCode = currencyCode;
	this.unitPrice = unitPrice;
	this.isServiceItem = isServiceItem;
	this.itemEnable = itemEnable;
	this.eanEnable = eanEnable;
	this.priceEnable = priceEnable;
	this.isTax = isTax;
    }
    // Property accessors

    public String getBrandCode() {
	return this.brandCode;
    }

    public void setBrandCode(String brandCode) {
	this.brandCode = brandCode;
    }

    public String getEanCode() {
	return this.eanCode;
    }

    public void setEanCode(String eanCode) {
	this.eanCode = eanCode;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getItemCName() {
	return this.itemCName;
    }

    public void setItemCName(String itemCName) {
	this.itemCName = itemCName;
    }

    public String getItemEName() {
	return this.itemEName;
    }

    public void setItemEName(String itemEName) {
	this.itemEName = itemEName;
    }

    public String getSalesUnit() {
	return this.salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
	this.salesUnit = salesUnit;
    }

    public String getCurrencyCode() {
	return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
    }

    public Double getUnitPrice() {
	return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
	this.unitPrice = unitPrice;
    }

    public String getIsServiceItem() {
	return this.isServiceItem;
    }

    public void setIsServiceItem(String isServiceItem) {
	this.isServiceItem = isServiceItem;
    }

    public String getItemEnable() {
	return this.itemEnable;
    }

    public void setItemEnable(String itemEnable) {
	this.itemEnable = itemEnable;
    }

    public String getPriceEnable() {
	return this.priceEnable;
    }

    public void setPriceEnable(String priceEnable) {
	this.priceEnable = priceEnable;
    }

    public String getIsTax() {
	return isTax;
    }

    public void setIsTax(String isTax) {
	this.isTax = isTax;
    }

    public String getEanEnable() {
        return eanEnable;
    }

    public void setEanEnable(String eanEnable) {
        this.eanEnable = eanEnable;
    }

}