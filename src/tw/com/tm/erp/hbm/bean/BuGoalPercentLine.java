package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuGoalPercentLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalPercentLine implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4108543382653913420L;
	private Long lineId;
	private BuGoalHead buGoalHead;
	private String itemCategory;
	private String itemSubcategory;
	private String itemBrand;
	private Double itemBrandPercent;
	private String workType;
	private Double itemCategoryPercent;
	private Double itemSubcategoryPercent;
	private Double workTypePercent;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Long indexNo;
	private String isDeleteRecord;
	private String isLockRecord;
	private String message;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	private String itemBrandName; 	// 非DB欄位,顯示商品品牌名稱用
	
	// Constructors
	/** default constructor */
	public BuGoalPercentLine() {
	}

	/** minimal constructor */
	public BuGoalPercentLine(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public BuGoalPercentLine(Long lineId, BuGoalHead buGoalHead,
			String itemCategory, String itemSubcategory, String workType,
			Double itemCategoryPercent, Double itemSubcategoryPercent,
			Double workTypePercent, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5, Long indexNo,
			String isDeleteRecord, String isLockRecord, String message,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate) {
		super();
		this.lineId = lineId;
		this.buGoalHead = buGoalHead;
		this.itemCategory = itemCategory;
		this.itemSubcategory = itemSubcategory;
		this.workType = workType;
		this.itemCategoryPercent = itemCategoryPercent;
		this.itemSubcategoryPercent = itemSubcategoryPercent;
		this.workTypePercent = workTypePercent;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.indexNo = indexNo;
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getItemCategory() {
		return this.itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemSubcategory() {
		return this.itemSubcategory;
	}

	public void setItemSubcategory(String itemSubcategory) {
		this.itemSubcategory = itemSubcategory;
	}

	public String getWorkType() {
		return this.workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getIsDeleteRecord() {
		return this.isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return this.isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public BuGoalHead getBuGoalHead() {
		return buGoalHead;
	}

	public void setBuGoalHead(BuGoalHead buGoalHead) {
		this.buGoalHead = buGoalHead;
	}

	public Double getItemCategoryPercent() {
		return itemCategoryPercent;
	}

	public void setItemCategoryPercent(Double itemCategoryPercent) {
		this.itemCategoryPercent = itemCategoryPercent;
	}

	public Double getItemSubcategoryPercent() {
		return itemSubcategoryPercent;
	}

	public void setItemSubcategoryPercent(Double itemSubcategoryPercent) {
		this.itemSubcategoryPercent = itemSubcategoryPercent;
	}

	public Double getWorkTypePercent() {
		return workTypePercent;
	}

	public void setWorkTypePercent(Double workTypePercent) {
		this.workTypePercent = workTypePercent;
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

	public Double getItemBrandPercent() {
		return itemBrandPercent;
	}

	public void setItemBrandPercent(Double itemBrandPercent) {
		this.itemBrandPercent = itemBrandPercent;
	}

}