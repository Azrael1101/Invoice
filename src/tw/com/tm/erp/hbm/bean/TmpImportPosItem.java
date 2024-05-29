package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * TmpImportPosItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpImportPosItem implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -405553385086583007L;
    // Fields
    private TmpImportPosItemId id;
    private String shopCode;
    private String itemCode;
    private Double originalUnitPrice;
    private Double quantity;
    private Double discountRate;
    private Double actualSalesAmount;
    private Double discountAmount;
    private String superintendentCode;
    private String customerCode;
    private String customerPoNo;
    private String period;
    private String passportNo;
    private String flightNo;
    private String countryCode;
    private Date departureDate;
    private String vipTypeCode;
    private Double standardPrice;
    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;
    private String remark5;
    private String ladingNo;
    private String vipCardId;
    private String vipCardType;   
    private String promotionCardId;
    private String promotionCardType;
    private String managerCardId;
    private String managerCardType;   
    private Double vipCardDiscount;
    private Double promotionCardDiscount;
    private Double managerCardDiscount;   
    private String usedIdentification;
    private String usedCardId;
    private String usedCardType;
    private Double usedDiscountRate;
    private String itemDiscountType;   
    private String fileName;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String transType;
    private String islandsCode;
    private String storeCode;
    private String transactionId;
    private String buyerId;
    private String salesUnit;
    private Double importTax;
    private Double goodsTax;
    private Double cigarWineTax;
    private Double healthTax;
    private Double businessTax;
    private String declNo;
    private Long declSeq;
    private String cigarWineRemark;
    private Double CigarWineQty;
    private Double taxationPcent;
    private Double taxUnitQty;
    private String OdeclNo;
    private Long OdeclSeq;
    private String lotNo;
    private String combineCode;
    
    // Constructors

    /** default constructor */
    public TmpImportPosItem() {
    }

    /** minimal constructor */
    public TmpImportPosItem(TmpImportPosItemId id) {
	this.id = id;
    }

    public TmpImportPosItem(TmpImportPosItemId id, String shopCode,
	    String itemCode, Double originalUnitPrice, Double quantity,
	    Double discountRate, Double actualSalesAmount,
	    Double discountAmount, String superintendentCode,
	    String customerCode, String customerPoNo, String period,
	    String passportNo, String flightNo, String countryCode,
	    Date departureDate, String vipTypeCode, Double standardPrice,
	    String remark1, String remark2, String remark3, String remark4,
	    String remark5, String ladingNo, String vipCardId,
	    String vipCardType, String promotionCardId,
	    String promotionCardType, String managerCardId,
	    String managerCardType, Double vipCardDiscount,
	    Double promotionCardDiscount, Double managerCardDiscount,
	    String usedIdentification, String usedCardId, String usedCardType,
	    Double usedDiscountRate, String itemDiscountType, String fileName,
	    String reserve1, String reserve2, String reserve3, String reserve4,
	    String reserve5, String createdBy, Date creationDate, String combineCode) {
	this.id = id;
	this.shopCode = shopCode;
	this.itemCode = itemCode;
	this.originalUnitPrice = originalUnitPrice;
	this.quantity = quantity;
	this.discountRate = discountRate;
	this.actualSalesAmount = actualSalesAmount;
	this.discountAmount = discountAmount;
	this.superintendentCode = superintendentCode;
	this.customerCode = customerCode;
	this.customerPoNo = customerPoNo;
	this.period = period;
	this.passportNo = passportNo;
	this.flightNo = flightNo;
	this.countryCode = countryCode;
	this.departureDate = departureDate;
	this.vipTypeCode = vipTypeCode;
	this.standardPrice = standardPrice;
	this.remark1 = remark1;
	this.remark2 = remark2;
	this.remark3 = remark3;
	this.remark4 = remark4;
	this.remark5 = remark5;
	this.ladingNo = ladingNo;
	this.vipCardId = vipCardId;
	this.vipCardType = vipCardType;
	this.promotionCardId = promotionCardId;
	this.promotionCardType = promotionCardType;
	this.managerCardId = managerCardId;
	this.managerCardType = managerCardType;
	this.vipCardDiscount = vipCardDiscount;
	this.promotionCardDiscount = promotionCardDiscount;
	this.managerCardDiscount = managerCardDiscount;
	this.usedIdentification = usedIdentification;
	this.usedCardId = usedCardId;
	this.usedCardType = usedCardType;
	this.usedDiscountRate = usedDiscountRate;
	this.itemDiscountType = itemDiscountType;
	this.fileName = fileName;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.combineCode = combineCode;
    }

    // Property accessors

    public TmpImportPosItemId getId() {
	return this.id;
    }

    public void setId(TmpImportPosItemId id) {
	this.id = id;
    }

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public Double getOriginalUnitPrice() {
	return this.originalUnitPrice;
    }

    public void setOriginalUnitPrice(Double originalUnitPrice) {
	this.originalUnitPrice = originalUnitPrice;
    }

    public Double getQuantity() {
	return this.quantity;
    }

    public void setQuantity(Double quantity) {
	this.quantity = quantity;
    }

    public Double getDiscountRate() {
	return this.discountRate;
    }

    public void setDiscountRate(Double discountRate) {
	this.discountRate = discountRate;
    }

    public Double getActualSalesAmount() {
	return this.actualSalesAmount;
    }

    public void setActualSalesAmount(Double actualSalesAmount) {
	this.actualSalesAmount = actualSalesAmount;
    }

    public Double getDiscountAmount() {
	return this.discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
	this.discountAmount = discountAmount;
    }

    public String getSuperintendentCode() {
	return this.superintendentCode;
    }

    public void setSuperintendentCode(String superintendentCode) {
	this.superintendentCode = superintendentCode;
    }

    public String getCustomerCode() {
	return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
	this.customerCode = customerCode;
    }

    public String getCustomerPoNo() {
	return this.customerPoNo;
    }

    public void setCustomerPoNo(String customerPoNo) {
	this.customerPoNo = customerPoNo;
    }

    public String getPeriod() {
	return this.period;
    }

    public void setPeriod(String period) {
	this.period = period;
    }

    public String getPassportNo() {
	return this.passportNo;
    }

    public void setPassportNo(String passportNo) {
	this.passportNo = passportNo;
    }

    public String getFlightNo() {
	return this.flightNo;
    }

    public void setFlightNo(String flightNo) {
	this.flightNo = flightNo;
    }

    public String getCountryCode() {
	return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    public Date getDepartureDate() {
	return this.departureDate;
    }

    public void setDepartureDate(Date departureDate) {
	this.departureDate = departureDate;
    }

    public String getVipTypeCode() {
	return this.vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
	this.vipTypeCode = vipTypeCode;
    }
    
    public Double getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(Double standardPrice) {
        this.standardPrice = standardPrice;
    }
    
    public String getRemark1() {
	return this.remark1;
    }

    public void setRemark1(String remark1) {
	this.remark1 = remark1;
    }

    public String getRemark2() {
	return this.remark2;
    }

    public void setRemark2(String remark2) {
	this.remark2 = remark2;
    }

    public String getRemark3() {
	return this.remark3;
    }

    public void setRemark3(String remark3) {
	this.remark3 = remark3;
    }

    public String getRemark4() {
	return this.remark4;
    }

    public void setRemark4(String remark4) {
	this.remark4 = remark4;
    }
    
    public String getRemark5() {
        return remark5;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public String getLadingNo() {
	return this.ladingNo;
    }

    public void setLadingNo(String ladingNo) {
	this.ladingNo = ladingNo;
    }
    
    public String getVipCardId() {
        return vipCardId;
    }

    public void setVipCardId(String vipCardId) {
        this.vipCardId = vipCardId;
    }

    public String getVipCardType() {
        return vipCardType;
    }

    public void setVipCardType(String vipCardType) {
        this.vipCardType = vipCardType;
    }
    
    public String getPromotionCardId() {
        return promotionCardId;
    }

    public void setPromotionCardId(String promotionCardId) {
        this.promotionCardId = promotionCardId;
    }

    public String getPromotionCardType() {
        return promotionCardType;
    }

    public void setPromotionCardType(String promotionCardType) {
        this.promotionCardType = promotionCardType;
    }

    public String getManagerCardId() {
        return managerCardId;
    }

    public void setManagerCardId(String managerCardId) {
        this.managerCardId = managerCardId;
    }

    public String getManagerCardType() {
        return managerCardType;
    }

    public void setManagerCardType(String managerCardType) {
        this.managerCardType = managerCardType;
    }

    public Double getVipCardDiscount() {
        return vipCardDiscount;
    }

    public void setVipCardDiscount(Double vipCardDiscount) {
        this.vipCardDiscount = vipCardDiscount;
    }

    public Double getPromotionCardDiscount() {
        return promotionCardDiscount;
    }

    public void setPromotionCardDiscount(Double promotionCardDiscount) {
        this.promotionCardDiscount = promotionCardDiscount;
    }

    public Double getManagerCardDiscount() {
        return managerCardDiscount;
    }

    public void setManagerCardDiscount(Double managerCardDiscount) {
        this.managerCardDiscount = managerCardDiscount;
    }

    public String getUsedIdentification() {
        return usedIdentification;
    }

    public void setUsedIdentification(String usedIdentification) {
        this.usedIdentification = usedIdentification;
    }

    public String getUsedCardId() {
        return usedCardId;
    }

    public void setUsedCardId(String usedCardId) {
        this.usedCardId = usedCardId;
    }

    public String getUsedCardType() {
        return usedCardType;
    }

    public void setUsedCardType(String usedCardType) {
        this.usedCardType = usedCardType;
    }

    public Double getUsedDiscountRate() {
        return usedDiscountRate;
    }

    public void setUsedDiscountRate(Double usedDiscountRate) {
        this.usedDiscountRate = usedDiscountRate;
    }
    
    public String getItemDiscountType() {
        return itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
        this.itemDiscountType = itemDiscountType;
    }

    public String getFileName() {
	return this.fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getIslandsCode() {
		return islandsCode;
	}

	public void setIslandsCode(String islandsCode) {
		this.islandsCode = islandsCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSalesUnit() {
		return salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public Double getImportTax() {
		return importTax;
	}

	public void setImportTax(Double importTax) {
		this.importTax = importTax;
	}

	public Double getGoodsTax() {
		return goodsTax;
	}

	public void setGoodsTax(Double goodsTax) {
		this.goodsTax = goodsTax;
	}

	public Double getCigarWineTax() {
		return cigarWineTax;
	}

	public void setCigarWineTax(Double cigarWineTax) {
		this.cigarWineTax = cigarWineTax;
	}

	public Double getHealthTax() {
		return healthTax;
	}

	public void setHealthTax(Double healthTax) {
		this.healthTax = healthTax;
	}

	public Double getBusinessTax() {
		return businessTax;
	}

	public void setBusinessTax(Double businessTax) {
		this.businessTax = businessTax;
	}

	public String getDeclNo() {
		return declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public Long getDeclSeq() {
		return declSeq;
	}

	public void setDeclSeq(Long declSeq) {
		this.declSeq = declSeq;
	}

	public String getCigarWineRemark() {
		return cigarWineRemark;
	}

	public void setCigarWineRemark(String cigarWineRemark) {
		this.cigarWineRemark = cigarWineRemark;
	}

	public Double getCigarWineQty() {
		return CigarWineQty;
	}

	public void setCigarWineQty(Double cigarWineQty) {
		CigarWineQty = cigarWineQty;
	}

	public Double getTaxationPcent() {
		return taxationPcent;
	}

	public void setTaxationPcent(Double taxationPcent) {
		this.taxationPcent = taxationPcent;
	}

	public Double getTaxUnitQty() {
		return taxUnitQty;
	}

	public void setTaxUnitQty(Double taxUnitQty) {
		this.taxUnitQty = taxUnitQty;
	}

	public String getOdeclNo() {
		return OdeclNo;
	}

	public void setOdeclNo(String odeclNo) {
		OdeclNo = odeclNo;
	}

	public Long getOdeclSeq() {
		return OdeclSeq;
	}

	public void setOdeclSeq(Long odeclSeq) {
		OdeclSeq = odeclSeq;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getCombineCode() {
		return combineCode;
	}

	public void setCombineCode(String combineCode) {
		this.combineCode = combineCode;
	}
}