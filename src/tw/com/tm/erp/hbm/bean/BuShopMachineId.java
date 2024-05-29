package tw.com.tm.erp.hbm.bean;

/**
 * BuShopMachineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuShopMachineId implements java.io.Serializable {

    // Fields

    /**
	 * 
	 */
	private static final long serialVersionUID = 6612388570693265803L;
	private String shopCode;
    private String posMachineCode;

    // Constructors

    /** default constructor */
    public BuShopMachineId() {
    }

    /** full constructor */
    public BuShopMachineId(String shopCode, String posMachineCode) {
	this.shopCode = shopCode;
	this.posMachineCode = posMachineCode;
    }

    // Property accessors

    public String getShopCode() {
	return this.shopCode;
    }

    public void setShopCode(String shopCode) {
	this.shopCode = shopCode;
    }

    public String getPosMachineCode() {
	return this.posMachineCode;
    }

    public void setPosMachineCode(String posMachineCode) {
	this.posMachineCode = posMachineCode;
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof BuShopMachineId))
	    return false;
	BuShopMachineId castOther = (BuShopMachineId) other;

	return ((this.getShopCode() == castOther.getShopCode()) || (this
		.getShopCode() != null
		&& castOther.getShopCode() != null && this.getShopCode()
		.equals(castOther.getShopCode())))
		&& ((this.getPosMachineCode() == castOther.getPosMachineCode()) || (this
			.getPosMachineCode() != null
			&& castOther.getPosMachineCode() != null && this
			.getPosMachineCode().equals(
				castOther.getPosMachineCode())));
    }

    public int hashCode() {
	int result = 17;

	result = 37 * result
		+ (getShopCode() == null ? 0 : this.getShopCode().hashCode());
	result = 37
		* result
		+ (getPosMachineCode() == null ? 0 : this.getPosMachineCode()
			.hashCode());
	return result;
    }

}