package tw.com.tm.erp.hbm.bean;

/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : TmpBarcodeExcel.java
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	2011/3/4		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
public class TmpBarcodeExcel {

	private String timescope;
	private String itemCode;
	private String itemCName;
	private String categroy02;
	private String categroy02Name;
	private Long boxCapacity;
	private Long quantity;
	private String supplierItemCode;
	private String declarationDate;

	public String getTimescope() {
		return timescope;
	}

	public void setTimescope(String timescope) {
		this.timescope = timescope;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemCName() {
		return itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public String getCategroy02() {
		return categroy02;
	}

	public void setCategroy02(String categroy02) {
		this.categroy02 = categroy02;
	}

	public String getCategroy02Name() {
		return categroy02Name;
	}

	public void setCategroy02Name(String categroy02Name) {
		this.categroy02Name = categroy02Name;
	}

	public Long getBoxCapacity() {
		return boxCapacity;
	}

	public void setBoxCapacity(Long boxCapacity) {
		this.boxCapacity = boxCapacity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getSupplierItemCode() {
		return supplierItemCode;
	}

	public void setSupplierItemCode(String supplierItemCode) {
		this.supplierItemCode = supplierItemCode;
	}

	public String getDeclarationDate() {
		return declarationDate;
	}

	public void setDeclarationDate(String declarationDate) {
		this.declarationDate = declarationDate;
	}
}
