package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;

public class SaleitemTable implements java.io.Serializable  {

	private static final long serialVersionUID = -4312303996853625355L;
	private Long lineSno;
	private Long headSno;
	private Long transactionSno;
	private String posMachineCode;
	private Date salesOrderDate;
	private String shopCode;
	private String itemCode;
	private String indexSno;
	private String supplierItemCode;
	private String salesType;
	private Long salesQty;
	public Long getLineSno() {
		return lineSno;
	}
	public void setLineSno(Long lineSno) {
		this.lineSno = lineSno;
	}
	public Long getHeadSno() {
		return headSno;
	}
	public void setHeadSno(Long headSno) {
		this.headSno = headSno;
	}
	public Long getTransactionSno() {
		return transactionSno;
	}
	public void setTransactionSno(Long transactionSno) {
		this.transactionSno = transactionSno;
	}
	public String getPosMachineCode() {
		return posMachineCode;
	}
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	public Date getSalesOrderDate() {
		return salesOrderDate;
	}
	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getIndexSno() {
		return indexSno;
	}
	public void setIndexSno(String indexSno) {
		this.indexSno = indexSno;
	}
	public String getSupplierItemCode() {
		return supplierItemCode;
	}
	public void setSupplierItemCode(String supplierItemCode) {
		this.supplierItemCode = supplierItemCode;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public Long getSalesQty() {
		return salesQty;
	}
	public void setSalesQty(Long salesQty) {
		this.salesQty = salesQty;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
