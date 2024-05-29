package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTally entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SoPostingTally implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392344103340696432L;
	// Fields
	private SoPostingTallyId id;
	private String brandCode;
	private Date postingDate;
	private String isPosting;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date createDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;    
	private String difference;  		// 差異
	private String postingStatus;		// 狀態
	private String unit;  			// 專櫃/機台代號
	private String salesUnitName;
	private String salesDate;			// 交易日期
	private String transactionAmountS;		// 交易筆數(POS轉入)筆數
	private String posImportAmtS;		// 交易筆數(POS轉入)金額
	private String transactionAmountD;		// 交易筆數(POS轉入)筆數
	private String posImportAmtD;		// 交易筆數(POS轉入)金額
	private String actualSalesAmt;		// 營收彙總金額(營業人員輸入)
	private String differenceAmt;  		// 差異金額
	private String actualTransactionAmount;	// 交易筆數(營業人員輸入)	
	private String schedule;
	// Constructors

	/** default constructor */
	public SoPostingTally() {
	}

	/** minimal constructor */
	public SoPostingTally(SoPostingTallyId id) {
		this.id = id;
	}

	/** full constructor */
	public SoPostingTally(SoPostingTallyId id, String brandCode, Date postingDate,
			String isPosting, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date createDate, String lastUpdatedBy,
			Date lastUpdateDate) {
		this.id = id;
		this.brandCode = brandCode;
		this.postingDate = postingDate;
		this.isPosting = isPosting;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public SoPostingTallyId getId() {
		return this.id;
	}

	public void setId(SoPostingTallyId id) {
		this.id = id;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public Date getPostingDate() {
		return this.postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public String getIsPosting() {
		return this.isPosting;
	}

	public void setIsPosting(String isPosting) {
		this.isPosting = isPosting;
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

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getPostingStatus() {
		return postingStatus;
	}

	public void setPostingStatus(String postingStatus) {
		this.postingStatus = postingStatus;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSalesUnitName() {
		return salesUnitName;
	}

	public void setSalesUnitName(String salesUnitName) {
		this.salesUnitName = salesUnitName;
	}

	public String getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}

	public String getTransactionAmountS() {
		return transactionAmountS;
	}

	public void setTransactionAmountS(String transactionAmountS) {
		this.transactionAmountS = transactionAmountS;
	}

	public String getPosImportAmtS() {
		return posImportAmtS;
	}

	public void setPosImportAmtS(String posImportAmtS) {
		this.posImportAmtS = posImportAmtS;
	}
	
	public String getTransactionAmountD() {
		return transactionAmountD;
	}

	public void setTransactionAmountD(String transactionAmountD) {
		this.transactionAmountD = transactionAmountD;
	}


	public String getPosImportAmtD() {
		return posImportAmtD;
	}

	public void setPosImportAmtD(String posImportAmtD) {
		this.posImportAmtD = posImportAmtD;
	}
	
	public String getActualSalesAmt() {
		return actualSalesAmt;
	}

	public void setActualSalesAmt(String actualSalesAmt) {
		this.actualSalesAmt = actualSalesAmt;
	}

	public String getDifferenceAmt() {
		return differenceAmt;
	}

	public void setDifferenceAmt(String differenceAmt) {
		this.differenceAmt = differenceAmt;
	}

	public String getActualTransactionAmount() {
		return actualTransactionAmount;
	}

	public void setActualTransactionAmount(String actualTransactionAmount) {
		this.actualTransactionAmount = actualTransactionAmount;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

}