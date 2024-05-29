package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImPromotion entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotion implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -288726790301017844L;
	private Long headId;
	private String brandCode;
	private String brandName;
	private String orderTypeCode;
	private String orderNo;
	private String priceType;
	private String promotionName;
	private String promotionCode;
	private String promotionNo;
	private String description;
	private String inCharge;
	private String inChargeName;
	private String isAllItem;
	private String isAllShop;
	private String isAllCustomer;
	private String customerType;
	private Date beginDate;
	private Date endDate;
	private Long discount;
	private String status;
	private String statusName;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private String createdByName;
	private Date creationDate;
	private String lastUpdatedBy;
	private String lastUpdatedByName;
	private Date lastUpdateDate;
	private List imPromotionItems = new ArrayList(0);
	private List imPromotionCustomers = new ArrayList(0);
	private List imPromotionShops = new ArrayList(0);
	private List imPromotionFiles = new ArrayList(0);
	private List imPromotionReCombines = new ArrayList(0);
	private List imPromotionFulls = new ArrayList(0);
	private Double totalCostAmount;
	private Double totalPriceAmount;
	
	private Double totalAfterDiscountAmount;
	private Double averageDiscountRate;
	private String itemCategory;
	private String purchaseAssist;
	private String purchaseMember;
	private String purchaseMaster;
	
	private Double totalDiscountAmount;	// 促銷查詢暫存	
	private Double totalDiscountPercentage; // 促銷查詢暫存	      
	private Double originalPrice;           // 促銷查詢暫存
	
	private String enable;
	private Double buyAmount;
	
	// Constructors

	/** default constructor */
	public ImPromotion() {
	}

	/** minimal constructor */
	public ImPromotion(Long headId, String brandCode) {
		this.headId = headId;
		this.brandCode = brandCode;
	}

	/**
	 * @param headId
	 * @param brandCode
	 * @param brandName
	 * @param orderTypeCode
	 * @param orderNo
	 * @param priceType
	 * @param promotionName
	 * @param promotionCode
	 * @param promotionNo
	 * @param description
	 * @param inCharge
	 * @param inChargeName
	 * @param isAllItem
	 * @param isAllShop
	 * @param isAllCustomer
	 * @param customerType
	 * @param beginDate
	 * @param endDate
	 * @param discount
	 * @param status
	 * @param statusName
	 * @param reserve1
	 * @param reserve2
	 * @param reserve3
	 * @param reserve4
	 * @param reserve5
	 * @param createdBy
	 * @param createdByName
	 * @param creationDate
	 * @param lastUpdatedBy
	 * @param lastUpdatedByName
	 * @param lastUpdateDate
	 * @param imPromotionItems
	 * @param imPromotionCustomers
	 * @param imPromotionShops
	 * @param imPromotionFiles
	 * @param imPromotionReCombines
	 * @param imPromotionFulls
	 * @param totalCostAmount
	 * @param totalPriceAmount
	 * @param totalAfterDiscountAmount
	 * @param averageDiscountRate
	 * @param itemCategory
	 * @param purchaseAssist
	 * @param purchaseMember
	 * @param purchaseMaster
	 * @param totalDiscountAmount
	 * @param totalDiscountPercentage
	 * @param originalPrice
	 * @param enable
	 * @param buyAmount
	 */
	public ImPromotion(Long headId, String brandCode, String brandName,
			String orderTypeCode, String orderNo, String priceType,
			String promotionName, String promotionCode, String promotionNo,
			String description, String inCharge, String inChargeName,
			String isAllItem, String isAllShop, String isAllCustomer,
			String customerType, Date beginDate, Date endDate, Long discount,
			String status, String statusName, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, String createdByName, Date creationDate,
			String lastUpdatedBy, String lastUpdatedByName,
			Date lastUpdateDate, List imPromotionItems,
			List imPromotionCustomers, List imPromotionShops,
			List imPromotionFiles, List imPromotionReCombines,
			List imPromotionFulls, Double totalCostAmount,
			Double totalPriceAmount, Double totalAfterDiscountAmount,
			Double averageDiscountRate, String itemCategory,
			String purchaseAssist, String purchaseMember,
			String purchaseMaster, Double totalDiscountAmount,
			Double totalDiscountPercentage, Double originalPrice,
			String enable, Double buyAmount) {
		this.headId = headId;
		this.brandCode = brandCode;
		this.brandName = brandName;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.priceType = priceType;
		this.promotionName = promotionName;
		this.promotionCode = promotionCode;
		this.promotionNo = promotionNo;
		this.description = description;
		this.inCharge = inCharge;
		this.inChargeName = inChargeName;
		this.isAllItem = isAllItem;
		this.isAllShop = isAllShop;
		this.isAllCustomer = isAllCustomer;
		this.customerType = customerType;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.discount = discount;
		this.status = status;
		this.statusName = statusName;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.createdByName = createdByName;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedByName = lastUpdatedByName;
		this.lastUpdateDate = lastUpdateDate;
		this.imPromotionItems = imPromotionItems;
		this.imPromotionCustomers = imPromotionCustomers;
		this.imPromotionShops = imPromotionShops;
		this.imPromotionFiles = imPromotionFiles;
		this.imPromotionReCombines = imPromotionReCombines;
		this.imPromotionFulls = imPromotionFulls;
		this.totalCostAmount = totalCostAmount;
		this.totalPriceAmount = totalPriceAmount;
		this.totalAfterDiscountAmount = totalAfterDiscountAmount;
		this.averageDiscountRate = averageDiscountRate;
		this.itemCategory = itemCategory;
		this.purchaseAssist = purchaseAssist;
		this.purchaseMember = purchaseMember;
		this.purchaseMaster = purchaseMaster;
		this.totalDiscountAmount = totalDiscountAmount;
		this.totalDiscountPercentage = totalDiscountPercentage;
		this.originalPrice = originalPrice;
		this.enable = enable;
		this.buyAmount = buyAmount;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionNo() {
		return promotionNo;
	}

	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInCharge() {
		return inCharge;
	}

	public void setInCharge(String inCharge) {
		this.inCharge = inCharge;
	}

	public String getInChargeName() {
		return inChargeName;
	}

	public void setInChargeName(String inChargeName) {
		this.inChargeName = inChargeName;
	}

	public String getIsAllItem() {
		return isAllItem;
	}

	public void setIsAllItem(String isAllItem) {
		this.isAllItem = isAllItem;
	}

	public String getIsAllShop() {
		return isAllShop;
	}

	public void setIsAllShop(String isAllShop) {
		this.isAllShop = isAllShop;
	}

	public String getIsAllCustomer() {
		return isAllCustomer;
	}

	public void setIsAllCustomer(String isAllCustomer) {
		this.isAllCustomer = isAllCustomer;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List getImPromotionItems() {
		return imPromotionItems;
	}

	public void setImPromotionItems(List imPromotionItems) {
		this.imPromotionItems = imPromotionItems;
	}

	public List getImPromotionCustomers() {
		return imPromotionCustomers;
	}

	public void setImPromotionCustomers(List imPromotionCustomers) {
		this.imPromotionCustomers = imPromotionCustomers;
	}

	public List getImPromotionShops() {
		return imPromotionShops;
	}

	public void setImPromotionShops(List imPromotionShops) {
		this.imPromotionShops = imPromotionShops;
	}

	public List getImPromotionFiles() {
		return imPromotionFiles;
	}

	public void setImPromotionFiles(List imPromotionFiles) {
		this.imPromotionFiles = imPromotionFiles;
	}

	public List getImPromotionReCombines() {
		return imPromotionReCombines;
	}

	public void setImPromotionReCombines(List imPromotionReCombines) {
		this.imPromotionReCombines = imPromotionReCombines;
	}

	public List getImPromotionFulls() {
		return imPromotionFulls;
	}

	public void setImPromotionFulls(List imPromotionFulls) {
		this.imPromotionFulls = imPromotionFulls;
	}

	public Double getTotalCostAmount() {
		return totalCostAmount;
	}

	public void setTotalCostAmount(Double totalCostAmount) {
		this.totalCostAmount = totalCostAmount;
	}

	public Double getTotalPriceAmount() {
		return totalPriceAmount;
	}

	public void setTotalPriceAmount(Double totalPriceAmount) {
		this.totalPriceAmount = totalPriceAmount;
	}

	public Double getTotalAfterDiscountAmount() {
		return totalAfterDiscountAmount;
	}

	public void setTotalAfterDiscountAmount(Double totalAfterDiscountAmount) {
		this.totalAfterDiscountAmount = totalAfterDiscountAmount;
	}

	public Double getAverageDiscountRate() {
		return averageDiscountRate;
	}

	public void setAverageDiscountRate(Double averageDiscountRate) {
		this.averageDiscountRate = averageDiscountRate;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getPurchaseAssist() {
		return purchaseAssist;
	}

	public void setPurchaseAssist(String purchaseAssist) {
		this.purchaseAssist = purchaseAssist;
	}

	public String getPurchaseMember() {
		return purchaseMember;
	}

	public void setPurchaseMember(String purchaseMember) {
		this.purchaseMember = purchaseMember;
	}

	public String getPurchaseMaster() {
		return purchaseMaster;
	}

	public void setPurchaseMaster(String purchaseMaster) {
		this.purchaseMaster = purchaseMaster;
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

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public Double getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(Double buyAmount) {
		this.buyAmount = buyAmount;
	}
}