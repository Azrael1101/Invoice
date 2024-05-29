package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImPromotion entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosPromotion implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -288726790301017844L;
	private Long headId;
	private String dataId;
	private String action;
	private String brandCode;
	private String orderNo;
	private String promotionCode;
	private String isAllItem;
	private String isAllShop;
	private String isAllCustomer;
	private Date beginDate;
	private Date endDate;

	private List posPromotionItems = new ArrayList(0);
	private List posPromotionCustomers = new ArrayList(0);
	private List posPromotionShops = new ArrayList(0);
	private List posPromotionCondition = new ArrayList(0);
//	private Double totalCostAmount;
//	private Double totalPriceAmount;
//	private Double totalDiscountAmount;
//	private Double totalAfterDiscountAmount;
//	private Double averageDiscountRate;
	
	private Double conditionTotalAmount;	// 總金額
	private Long promotionType;		// 促銷模式
	private String individualFigure;	// 個別計算
	// Constructors

	public String getDataId() {
	    return dataId;
	}

	public void setDataId(String dataId) {
	    this.dataId = dataId;
	}

	/** default constructor */
	public PosPromotion() {
	}

	/** minimal constructor */
	public PosPromotion(Long headId, String brandCode) {
		this.headId = headId;
		this.brandCode = brandCode;
	}

	/** minimal constructor */
	public PosPromotion(Long headId) {
		this.headId = headId;
	}
	
	
	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPromotionCode() {
		return this.promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
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


//	public List getImPromotionItems() {
//		return this.imPromotionItems;
//	}
//
//	public void setImPromotionItems(List imPromotionItems) {
//		this.imPromotionItems = imPromotionItems;
//	}
//
//	public List getImPromotionCustomers() {
//		return this.imPromotionCustomers;
//	}
//
//	public void setImPromotionCustomers(List imPromotionCustomers) {
//		this.imPromotionCustomers = imPromotionCustomers;
//	}
//
//	public List getImPromotionShops() {
//		return this.imPromotionShops;
//	}
//
//	public void setImPromotionShops(List imPromotionShops) {
//		this.imPromotionShops = imPromotionShops;
//	}
//
//	public List getImPromotionFiles() {
//		return this.imPromotionFiles;
//	}
//
//	public void setImPromotionFiles(List imPromotionFiles) {
//		this.imPromotionFiles = imPromotionFiles;
//	}

//	public Double getTotalCostAmount() {
//	    return totalCostAmount;
//	}
//
//	public void setTotalCostAmount(Double totalCostAmount) {
//	    this.totalCostAmount = totalCostAmount;
//	}
//
//	public Double getTotalPriceAmount() {
//	    return totalPriceAmount;
//	}
//
//	public void setTotalPriceAmount(Double totalPriceAmount) {
//	    this.totalPriceAmount = totalPriceAmount;
//	}
//
//	public Double getTotalDiscountAmount() {
//	    return totalDiscountAmount;
//	}
//
//	public void setTotalDiscountAmount(Double totalDiscountAmount) {
//	    this.totalDiscountAmount = totalDiscountAmount;
//	}
//
//	public Double getTotalAfterDiscountAmount() {
//	    return totalAfterDiscountAmount;
//	}
//
//	public void setTotalAfterDiscountAmount(Double totalAfterDiscountAmount) {
//	    this.totalAfterDiscountAmount = totalAfterDiscountAmount;
//	}
//
//	public Double getAverageDiscountRate() {
//	    return averageDiscountRate;
//	}

//	public void setAverageDiscountRate(Double averageDiscountRate) {
//	    this.averageDiscountRate = averageDiscountRate;
//	}
//
//	public String getItemCategory() {
//	    return itemCategory;
//	}
//
//	public void setItemCategory(String itemCategory) {
//	    this.itemCategory = itemCategory;
//	}
//
//	public String getPurchaseAssist() {
//	    return purchaseAssist;
//	}
//
//	public void setPurchaseAssist(String purchaseAssist) {
//	    this.purchaseAssist = purchaseAssist;
//	}
//
//	public String getPurchaseMember() {
//	    return purchaseMember;
//	}
//
//	public void setPurchaseMember(String purchaseMember) {
//	    this.purchaseMember = purchaseMember;
//	}
//
//	public String getPurchaseMaster() {
//	    return purchaseMaster;
//	}
//
//	public void setPurchaseMaster(String purchaseMaster) {
//	    this.purchaseMaster = purchaseMaster;
//	}
//
//	public List getImPromotionCondition() {
//	    return imPromotionCondition;
//	}
//
//	public void setImPromotionCondition(List imPromotionCondition) {
//	    this.imPromotionCondition = imPromotionCondition;
//	}

	public Long getPromotionType() {
	    return promotionType;
	}

	public void setPromotionType(Long promotionType) {
	    this.promotionType = promotionType;
	}

	public Double getConditionTotalAmount() {
	    return conditionTotalAmount;
	}

	public void setConditionTotalAmount(Double conditionTotalAmount) {
	    this.conditionTotalAmount = conditionTotalAmount;
	}

	public String getIndividualFigure() {
	    return individualFigure;
	}

	public void setIndividualFigure(String individualFigure) {
	    this.individualFigure = individualFigure;
	}

	public List getPosPromotionItems() {
	    return posPromotionItems;
	}

	public void setPosPromotionItems(List posPromotionItems) {
	    this.posPromotionItems = posPromotionItems;
	}

	public List getPosPromotionCustomers() {
	    return posPromotionCustomers;
	}

	public void setPosPromotionCustomers(List posPromotionCustomers) {
	    this.posPromotionCustomers = posPromotionCustomers;
	}

	public List getPosPromotionShops() {
	    return posPromotionShops;
	}

	public void setPosPromotionShops(List posPromotionShops) {
	    this.posPromotionShops = posPromotionShops;
	}

	public List getPosPromotionCondition() {
	    return posPromotionCondition;
	}

	public void setPosPromotionCondition(List posPromotionCondition) {
	    this.posPromotionCondition = posPromotionCondition;
	}

	public String getAction() {
	    return action;
	}

	public void setAction(String action) {
	    this.action = action;
	}
}