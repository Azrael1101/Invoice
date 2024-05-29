package tw.com.tm.erp.hbm.bean;

/**
 * ImItemOnHandViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemOnHandViewId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8431913695744638259L;
	private String itemCode;
	private String brandCode;
	private String warehouseCode;
	private String lotNo;

	// Constructors

	/** default constructor */
	public ImItemOnHandViewId() {
	}

	/** minimal constructor */
	public ImItemOnHandViewId(String itemCode, String brandCode,
			String warehouseCode, String lotNo) {
		this.itemCode = itemCode;
		this.brandCode = brandCode;
		this.warehouseCode = warehouseCode;
		this.lotNo = lotNo;
	}

	// Property accessors

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImItemOnHandViewId))
			return false;
		ImItemOnHandViewId castOther = (ImItemOnHandViewId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getBrandCode() == castOther.getBrandCode()) || (this
						.getBrandCode() != null
						&& castOther.getBrandCode() != null && this
						.getBrandCode().equals(castOther.getBrandCode())))
				&& ((this.getWarehouseCode() == castOther.getWarehouseCode()) || (this
						.getWarehouseCode() != null
						&& castOther.getWarehouseCode() != null && this
						.getWarehouseCode()
						.equals(castOther.getWarehouseCode())))
				&& ((this.getLotNo() == castOther.getLotNo()) || (this
						.getLotNo() != null
						&& castOther.getLotNo() != null && this.getLotNo()
						.equals(castOther.getLotNo())));

	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37
				* result
				+ (getWarehouseCode() == null ? 0 : this.getWarehouseCode()
						.hashCode());
		result = 37 * result
				+ (getLotNo() == null ? 0 : this.getLotNo().hashCode());

		return result;
	}

}