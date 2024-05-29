package tw.com.tm.erp.hbm.bean;

// 給補條碼暫存匯入用的bean
public class BuPurchaseLineTmp {
	
	//序號	品號	品名	來源倉	 來源庫存	基本量	需求量	箱數	 店別	店庫存

    private String indexNo;
    private String itemNo;
    private String itemName;
    private String supplier;
    private Long purTotalAmount;
    private Long boxCapacity;
    private Long box;
    private int quantity;
    private String shopCode;
    private Long reTotalAmount;
    
    public BuPurchaseLineTmp(){}
    
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public Long getPurTotalAmount() {
		return purTotalAmount;
	}
	public void setPurTotalAmount(Long purTotalAmount) {
		this.purTotalAmount = purTotalAmount;
	}
	public Long getBoxCapacity() {
		return boxCapacity;
	}
	public void setBoxCapacity(Long boxCapacity) {
		this.boxCapacity = boxCapacity;
	}
	public Long getBox() {
		return box;
	}
	public void setBox(Long box) {
		this.box = box;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public Long getReTotalAmount() {
		return reTotalAmount;
	}
	public void setReTotalAmount(Long reTotalAmount) {
		this.reTotalAmount = reTotalAmount;
	}

}