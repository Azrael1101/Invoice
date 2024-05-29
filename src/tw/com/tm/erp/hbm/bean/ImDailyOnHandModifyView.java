package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImDailyOnHandModifyView implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private ImDailyOnHandModifyViewId id;
	private Date transationDate;
	private String dayTotal;
	private String warehouseCode;
	private String itemCode;
	//private String orderTypeCode;
	private String name;
	private Double beginningOnHandQuantity;
	private Double sum;
	private Double currentOnHand;



	
	public ImDailyOnHandModifyView() {

	}
	
	
	
	/**
	 * @param id
	 * @param transationDate
	 * @param dayTotal
	 * @param warehouseCode
	 * @param itemCode
	 * @param name
	 * @param beginningOnHandQuantity
	 * @param sum
	 * @param currentOnHand
	 */
	public ImDailyOnHandModifyView(ImDailyOnHandModifyViewId id,
			Date transationDate, String dayTotal, String warehouseCode,
			String itemCode, String name, Double beginningOnHandQuantity,
			Double sum, Double currentOnHand) {
		this.id = id;
		this.transationDate = transationDate;
		this.dayTotal = dayTotal;
		this.warehouseCode = warehouseCode;
		this.itemCode = itemCode;
		this.name = name;
		this.beginningOnHandQuantity = beginningOnHandQuantity;
		this.sum = sum;
		this.currentOnHand = currentOnHand;
	}




	/**
	 * @return the id
	 */
	public ImDailyOnHandModifyViewId getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(ImDailyOnHandModifyViewId id) {
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
	 * @return the dayTotal
	 */
	public String getDayTotal() {
		return dayTotal;
	}


	/**
	 * @param dayTotal the dayTotal to set
	 */
	public void setDayTotal(String dayTotal) {
		this.dayTotal = dayTotal;
	}

	
	
	/**
	 * @return the orderTypeCode
	 */
	//public String getOrderTypeCode() {
	//	return orderTypeCode;
	//}


	/**
	 * @param orderTypeCode the orderTypeCode to set
	 */
	//public void setOrderTypeCode(String orderTypeCode) {
	//	this.orderTypeCode = orderTypeCode;
	//}


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
	 * @return the beginningOnHandQuantity
	 */
	public Double getBeginningOnHandQuantity() {
		return beginningOnHandQuantity;
	}


	/**
	 * @param beginningOnHandQuantity the beginningOnHandQuantity to set
	 */
	public void setBeginningOnHandQuantity(Double beginningOnHandQuantity) {
		this.beginningOnHandQuantity = beginningOnHandQuantity;
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
	 * @return the currentOnHand
	 */
	public Double getCurrentOnHand() {
		return currentOnHand;
	}


	/**
	 * @param currentOnHand the currentOnHand to set
	 */
	public void setCurrentOnHand(Double currentOnHand) {
		this.currentOnHand = currentOnHand;
	}
}