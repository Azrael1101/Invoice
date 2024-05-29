package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionShop entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionReCombineMod implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4586392534106745480L;
	private Long lineId;
	private ImPromotion imPromotion;
	private String combineCode;
	private String combineName;
	private Long combinePrice;
	private Long combineQuantity;
	private String itemBrand;
	private String category02;
	private Long unitPrice;	
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private Long reCombineId;
	private String foreignCategory;
	
	private String itemBrandName;
	private String category02Name;

	// Constructors

	/** default constructor */
	public ImPromotionReCombineMod() {
	}

	/** minimal constructor */
	public ImPromotionReCombineMod(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImPromotionReCombineMod(Long lineId, ImPromotion imPromotion, String combineCode,
			String combineName, Long combinePrice, Long combineQuantity,String itemBrand,
			String category02,Long unitPrice,String reserve1, 
			String reserve2, String reserve3, String reserve4, 
			String reserve5, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, Long indexNo,
			Long reCombineId, String itemBrandName,String category02Name, String foreignCategory) {
		this.lineId = lineId;
		this.imPromotion = imPromotion;
		this.combineCode = combineCode;
		this.combineName = combineName;
		this.combinePrice = combinePrice;
		this.combineQuantity = combineQuantity;
		this.itemBrand = itemBrand;
		this.category02 = category02;
		this.unitPrice = unitPrice;		
		this.reserve1 =reserve1;
		this.reserve2 =reserve2;
		this.reserve3 =reserve3;
		this.reserve4 =reserve4;
		this.reserve5 =reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
		this.reCombineId = reCombineId;
		this.itemBrandName = itemBrandName;
		this.category02Name = category02Name;
		this.foreignCategory = foreignCategory;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImPromotion getImPromotion() {
		return imPromotion;
	}

	public void setImPromotion(ImPromotion imPromotion) {
		this.imPromotion = imPromotion;
	}

	public String getCombineCode() {
		return combineCode;
	}

	public void setCombineCode(String combineCode) {
		this.combineCode = combineCode;
	}

	public String getCombineName() {
		return combineName;
	}

	public void setCombineName(String combineName) {
		this.combineName = combineName;
	}

	public Long getCombinePrice() {
		return combinePrice;
	}

	public void setCombinePrice(Long combinePrice) {
		this.combinePrice = combinePrice;
	}

	public Long getCombineQuantity() {
		return combineQuantity;
	}

	public void setCombineQuantity(Long combineQuantity) {
		this.combineQuantity = combineQuantity;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getCategory02() {
		return category02;
	}

	public void setCategory02(String category02) {
		this.category02 = category02;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
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

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getItemBrandName() {
		return itemBrandName;
	}

	public void setItemBrandName(String itemBrandName) {
		this.itemBrandName = itemBrandName;
	}

	public String getCategory02Name() {
		return category02Name;
	}

	public void setCategory02Name(String category02Name) {
		this.category02Name = category02Name;
	}

	public Long getReCombineId() {
		return reCombineId;
	}

	public void setReCombineId(Long reCombineId) {
		this.reCombineId = reCombineId;
	}

	public String getForeignCategory() {
		return foreignCategory;
	}

	public void setForeignCategory(String foreignCategory) {
		this.foreignCategory = foreignCategory;
	}

	
}