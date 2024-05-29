package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImPromotionShop entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImPromotionShop implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4619095590950320747L;
	private Long lineId;
	private ImPromotion imPromotion;
	private String shopCode;
	private String shopName;
	private Date beginDate;
	private Date endDate;
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
	private String isDeleteRecord = "0"; // 是否被刪除 1 表示要被移除
	private String isLockRecord = "0"; // 是否被鎖定 1 表示鎖定
	private String message; // line 訊息的顯示

	// Constructors

	/** default constructor */
	public ImPromotionShop() {
	}

	/** minimal constructor */
	public ImPromotionShop(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImPromotionShop(Long lineId, ImPromotion imPromotion, String shopCode,
			String shopName, Date beginDate, Date endDate, String reserve1, 
			String reserve2, String reserve3, String reserve4, 
			String reserve5, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, Long indexNo,
			String isDeleteRecord, String isLockRecord, String message) {
		this.lineId = lineId;
		this.imPromotion = imPromotion;
		this.shopCode = shopCode;
		this.shopName = shopName;
		this.beginDate = beginDate;
		this.endDate = endDate;
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
		this.isDeleteRecord = isDeleteRecord;
		this.isLockRecord = isLockRecord;
		this.message = message;
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImPromotion getImPromotion() {
		return this.imPromotion;
	}

	public void setImPromotion(ImPromotion imPromotion) {
		this.imPromotion = imPromotion;
	}

	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}
	
	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getIsLockRecord() {
		return isLockRecord;
	}

	public void setIsLockRecord(String isLockRecord) {
		this.isLockRecord = isLockRecord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}