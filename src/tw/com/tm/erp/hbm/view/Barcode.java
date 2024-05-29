package tw.com.tm.erp.hbm.view;

/**
 * braCode View Object
 * @author T02049
 *
 */

public class Barcode implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3295053638055561292L;
	
	private String itemCode;
	private String itemCName;
	private String category14;
	private String category08;
	private String date;
	private Double unitPrice;
	private Double quantity ;
	
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
	public String getCategory14() {
		return category14;
	}
	public void setCategory14(String category14) {
		this.category14 = category14;
	}
	public String getCategory08() {
		return category08;
	}
	public void setCategory08(String category08) {
		this.category08 = category08;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
}
