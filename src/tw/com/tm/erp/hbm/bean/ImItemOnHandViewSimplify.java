package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImItemOnHandViewSimplify implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private ImItemOnHandViewId id;
	private String organizationCode;
	private String itemCName;
	private String warehouseName;
	private Double stockOnHandQty;
	private Double outUncommitQty;
	private Double inUncommitQty;
	private Double moveUncommitQty;
	private Double otherUncommitQty;
	private Double currentOnHandQty;


	private String itemCategory;
	private String itemBrand; // 商品品牌
	private Double unitPrice;


	// Constructors


	/** default constructor */
	public ImItemOnHandViewSimplify() {
	}

	/** full constructor */
	public ImItemOnHandViewSimplify(ImItemOnHandViewId id) {
		this.id = id;
	}

	public ImItemOnHandViewId getId() {
		return this.id;
	}

	public void setId(ImItemOnHandViewId id) {
		this.id = id;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getItemCName() {
		return this.itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public Double getStockOnHandQty() {
		return this.stockOnHandQty;
	}

	public void setStockOnHandQty(Double stockOnHandQty) {
		this.stockOnHandQty = stockOnHandQty;
	}

	public Double getOutUncommitQty() {
		return this.outUncommitQty;
	}

	public void setOutUncommitQty(Double outUncommitQty) {
		this.outUncommitQty = outUncommitQty;
	}

	public Double getInUncommitQty() {
		return this.inUncommitQty;
	}

	public void setInUncommitQty(Double inUncommitQty) {
		this.inUncommitQty = inUncommitQty;
	}

	public Double getMoveUncommitQty() {
		return moveUncommitQty;
	}

	public void setMoveUncommitQty(Double moveUncommitQty) {
		this.moveUncommitQty = moveUncommitQty;
	}

	public Double getOtherUncommitQty() {
		return otherUncommitQty;
	}

	public void setOtherUncommitQty(Double otherUncommitQty) {
		this.otherUncommitQty = otherUncommitQty;
	}

	public void setCurrentOnHandQty(Double currentOnHandQty) {
		this.currentOnHandQty = currentOnHandQty;
	}

	public Double getCurrentOnHandQty() {
		return currentOnHandQty;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}