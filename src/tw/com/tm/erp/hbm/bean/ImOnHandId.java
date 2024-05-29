package tw.com.tm.erp.hbm.bean;

/**
 * ImOnHandId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImOnHandId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3415593051829978289L;
    // Fields
    private String organizationCode;
    private String itemCode;
    private String warehouseCode;
    private String lotNo;

    // Constructors

    /** default constructor */
    public ImOnHandId() {
    }

    /** full constructor */
    public ImOnHandId(String organizationCode, String itemCode,
	    String warehouseCode, String lotNo) {
	this.organizationCode = organizationCode;
	this.itemCode = itemCode;
	this.warehouseCode = warehouseCode;
	this.lotNo = lotNo;
    }

    // Property accessors

    public String getOrganizationCode() {
	return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
	this.organizationCode = organizationCode;
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

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof ImOnHandId))
	    return false;
	ImOnHandId castOther = (ImOnHandId) other;

	return ((this.getOrganizationCode() == castOther.getOrganizationCode()) || (this
		.getOrganizationCode() != null
		&& castOther.getOrganizationCode() != null && this
		.getOrganizationCode().equals(castOther.getOrganizationCode())))
		&& ((this.getItemCode() == castOther.getItemCode()) || (this
			.getItemCode() != null
			&& castOther.getItemCode() != null && this
			.getItemCode().equals(castOther.getItemCode())))
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

	result = 37
		* result
		+ (getOrganizationCode() == null ? 0 : this
			.getOrganizationCode().hashCode());
	result = 37 * result
		+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
	result = 37
		* result
		+ (getWarehouseCode() == null ? 0 : this.getWarehouseCode()
			.hashCode());
	result = 37 * result
		+ (getLotNo() == null ? 0 : this.getLotNo().hashCode());
	return result;
    }

}