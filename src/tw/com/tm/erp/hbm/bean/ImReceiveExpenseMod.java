package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class ImReceiveExpenseMod implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4547524780708017987L;
	private Long headId;
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
	private String orderTypeCode;
	private String orderNo;
	private String brandCode;
	private Double taxAmount = new Double(0);
	private Date billDate;
	public Long getHeadId() {
		return headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	public ImReceiveHead getImReceiveHead() {
		return imReceiveHead;
	}
	public void setImReceiveHead(ImReceiveHead imReceiveHead) {
		this.imReceiveHead = imReceiveHead;
	}
	public String getExpenseCode() {
		return expenseCode;
	}
	public void setExpenseCode(String expenseCode) {
		this.expenseCode = expenseCode;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Double getLocalAmount() {
		return localAmount;
	}
	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}
	public Double getForeignAmount() {
		return foreignAmount;
	}
	public void setForeignAmount(Double foreignAmount) {
		this.foreignAmount = foreignAmount;
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
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	
}
