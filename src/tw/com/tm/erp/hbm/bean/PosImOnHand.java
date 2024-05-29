package tw.com.tm.erp.hbm.bean;

/**
 * PosImOnHand entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosImOnHand implements java.io.Serializable {

	// Fields

	private Long headId;
	private String dataId;
	private String action;
	private String brandCode;
	private String itemCode;
	private String warehouseCode;
	private String lotNo;
	private Double currentOnHandQty;

	// Constructors

	/** default constructor */
	public PosImOnHand() {
	}

	/** minimal constructor */
	public PosImOnHand(String dataId, String brandCode, String itemCode,
			String warehouseCode) {
		this.dataId = dataId;
		this.brandCode = brandCode;
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
	}

	/** full constructor */
	public PosImOnHand(String dataId, String action, String brandCode,
			String itemCode, String warehouseCode, String lotNo,
			Double currentOnHandQty) {
		this.dataId = dataId;
		this.action = action;
		this.brandCode = brandCode;
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
		this.lotNo = lotNo;
		this.currentOnHandQty = currentOnHandQty;
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Double getCurrentOnHandQty() {
		return this.currentOnHandQty;
	}

	public void setCurrentOnHandQty(Double currentOnHandQty) {
		this.currentOnHandQty = currentOnHandQty;
	}

}