package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;

public class PosItem implements Serializable {


    private static final long serialVersionUID = -2672523964104202326L;
    private Long headId;
    private String dataId;
    private String action;
    private String itemCode;
    private String brandCode;
    private String itemCName;
    private String lotControl;
    private String isServiceItem;
    private String category01;
    private String category02;
    private String category01Name;
    private String category02Name;
    private String enable;
    private String vipDiscount;
    private String isTax;
    private Double declRatio;
    private Double minRatio;
    private Double sellUnitPrice;
    private Double originalUnitPrice;
    private String itemBrand;
    private String itemBrandName;
    private Double salesUnit;
    private String isBeforeCount;
    private String isAfterCount;
    private String isChangePrice;
    public Long getHeadId() {
        return headId;
    }
    public void setHeadId(Long headId) {
        this.headId = headId;
    }
    public String getDataId() {
        return dataId;
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
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    public String getItemCName() {
        return itemCName;
    }
    public void setItemCName(String itemCName) {
        this.itemCName = itemCName;
    }
    public String getLotControl() {
        return lotControl;
    }
    public void setLotControl(String lotControl) {
        this.lotControl = lotControl;
    }
    public String getIsServiceItem() {
        return isServiceItem;
    }
    public void setIsServiceItem(String isServiceItem) {
        this.isServiceItem = isServiceItem;
    }
    public String getCategory01() {
        return category01;
    }
    public void setCategory01(String category01) {
        this.category01 = category01;
    }
    public String getCategory02() {
        return category02;
    }
    public void setCategory02(String category02) {
        this.category02 = category02;
    }
    public String getCategory01Name() {
        return category01Name;
    }
    public void setCategory01Name(String category01Name) {
        this.category01Name = category01Name;
    }
    public String getCategory02Name() {
        return category02Name;
    }
    public void setCategory02Name(String category02Name) {
        this.category02Name = category02Name;
    }
    public String getEnable() {
        return enable;
    }
    public void setEnable(String enable) {
        this.enable = enable;
    }
    public String getVipDiscount() {
        return vipDiscount;
    }
    public void setVipDiscount(String vipDiscount) {
        this.vipDiscount = vipDiscount;
    }
    public String getIsTax() {
        return isTax;
    }
    public void setIsTax(String isTax) {
        this.isTax = isTax;
    }
    public Double getDeclRatio() {
        return declRatio;
    }
    public void setDeclRatio(Double declRatio) {
        this.declRatio = declRatio;
    }
    public Double getMinRatio() {
        return minRatio;
    }
    public void setMinRatio(Double minRatio) {
        this.minRatio = minRatio;
    }
    public Double getSellUnitPrice() {
        return sellUnitPrice;
    }
    public void setSellUnitPrice(Double sellUnitPrice) {
        this.sellUnitPrice = sellUnitPrice;
    }
    public Double getOriginalUnitPrice() {
        return originalUnitPrice;
    }
    public void setOriginalUnitPrice(Double originalUnitPrice) {
        this.originalUnitPrice = originalUnitPrice;
    }
    public String getItemBrand() {
        return itemBrand;
    }
    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }
    public String getItemBrandName() {
        return itemBrandName;
    }
    public void setItemBrandName(String itemBrandName) {
        this.itemBrandName = itemBrandName;
    }
    public Double getSalesUnit() {
        return salesUnit;
    }
    public void setSalesUnit(Double salesUnit) {
        this.salesUnit = salesUnit;
    }
    public String getIsBeforeCount() {
        return isBeforeCount;
    }
    public void setIsBeforeCount(String isBeforeCount) {
        this.isBeforeCount = isBeforeCount;
    }
    public String getIsAfterCount() {
        return isAfterCount;
    }
    public void setIsAfterCount(String isAfterCount) {
        this.isAfterCount = isAfterCount;
    }
    public String getIsChangePrice() {
        return isChangePrice;
    }
    public void setIsChangePrice(String isChangePrice) {
        this.isChangePrice = isChangePrice;
    }
}
