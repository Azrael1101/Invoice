package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImOrderOnHandModifyView implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private ImOrderOnHandModifyViewId id;
	private Date transationDate;
	private String warehouseCode;
	private String itemCode;
	//private String orderTypeCode;
	private String name;
	private Double quantity;
	private Double sum;
	private String year;
	private String month;
	private String day;
	private String customerPoNo;
	private String transactionSeqNo;



	
	public ImOrderOnHandModifyView() {

	}









	/**
	 * @param id
	 * @param transationDate
	 * @param warehouseCode
	 * @param itemCode
	 * @param name
	 * @param quantity
	 * @param sum
	 * @param year
	 * @param month
	 * @param day
	 * @param customerPoNo
	 * @param transactionSeqNo
	 */
	public ImOrderOnHandModifyView(ImOrderOnHandModifyViewId id,
			Date transationDate, String warehouseCode, String itemCode,
			String name, Double quantity, Double sum, String year,
			String month, String day, String customerPoNo,
			String transactionSeqNo) {
		super();
		this.id = id;
		this.transationDate = transationDate;
		this.warehouseCode = warehouseCode;
		this.itemCode = itemCode;
		this.name = name;
		this.quantity = quantity;
		this.sum = sum;
		this.year = year;
		this.month = month;
		this.day = day;
		this.customerPoNo = customerPoNo;
		this.transactionSeqNo = transactionSeqNo;
	}









	/**
	 * @return the id
	 */
	public ImOrderOnHandModifyViewId getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(ImOrderOnHandModifyViewId id) {
		this.id = id;
	}




	/**
	 * @return the transationDate
	 */
	public Date getTransationDate() {
		return transationDate;
	}




	/**
	 * @param transationDate the transationDate to set
	 */
	public void setTransationDate(Date transationDate) {
		this.transationDate = transationDate;
	}




	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}




	/**
	 * @param warehouseCode the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}




	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}




	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}




	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}




	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}




	/**
	 * @return the sum
	 */
	public Double getSum() {
		return sum;
	}




	/**
	 * @param sum the sum to set
	 */
	public void setSum(Double sum) {
		this.sum = sum;
	}




	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}




	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}




	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}




	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}




	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}




	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}




	/**
	 * @return the customerPoNo
	 */
	public String getCustomerPoNo() {
		return customerPoNo;
	}




	/**
	 * @param customerPoNo the customerPoNo to set
	 */
	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}




	/**
	 * @return the transactionSeqNo
	 */
	public String getTransactionSeqNo() {
		return transactionSeqNo;
	}




	/**
	 * @param transactionSeqNo the transactionSeqNo to set
	 */
	public void setTransactionSeqNo(String transactionSeqNo) {
		this.transactionSeqNo = transactionSeqNo;
	}
	
	
	
	
	
}