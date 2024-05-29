package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImReceiveExpense entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImReceiveExpense implements java.io.Serializable {
	/**
	 * 
	 */
	public static final String TABLE_NAME = "ERP.IM_RECEIVE_EXPENSE" ;	
	public static final String HEAD_ID = "HEAD_ID" ;	
	
	private static final long serialVersionUID = -1684924379526741290L;	
	private Long lineId;
	private ImReceiveHead imReceiveHead;
	private String expenseCode;  //費用代碼
	private String supplierCode;  //廠商代號
	private String supplierName;  //廠商名稱
	private String currencyCode;  //幣別代號
	private Double localAmount = new Double(0);  //外幣金額
	private Double foreignAmount = new Double(0);  //原幣金額
	private String reserve1;  //保留欄位1
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;
	private String isDeleteRecord = "0" ; //是否被刪除 1  表示要被移除
	private String isLockRecord = "0" ; //是否被鎖定 1  表示鎖定
	private String message ; //line 訊息的顯示	
	private String status ;

	private Double taxAmount = new Double(0);
	private Date billDate;

	// Constructors

	public Double getTaxAmount() {
	    return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
	    this.taxAmount = taxAmount;
	}

	public Date getBillDate() {
	    return billDate;
	}

	public void setBillDate(Date billDate) {
	    this.billDate = billDate;
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

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	/** default constructor */
	public ImReceiveExpense() {
	}

	/** minimal constructor */
	public ImReceiveExpense(Long lineId,
			ImReceiveHead imReceiveHead) {
		this.lineId = lineId;
		this.imReceiveHead = imReceiveHead;
	}

	

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImReceiveHead getImReceiveHead() {
		return this.imReceiveHead;
	}

	public void setImReceiveHead(
			ImReceiveHead imReceiveHead) {
		this.imReceiveHead = imReceiveHead;
	}

	public String getExpenseCode() {
		return this.expenseCode;
	}

	public void setExpenseCode(String expenseCode) {
		this.expenseCode = expenseCode;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Double getLocalAmount() {
		return this.localAmount;
	}

	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}

	public Double getForeignAmount() {
		return this.foreignAmount;
	}

	public void setForeignAmount(Double foreignAmount) {
		this.foreignAmount = foreignAmount;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}