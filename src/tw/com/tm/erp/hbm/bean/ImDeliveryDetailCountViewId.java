package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImDeliveryDetailCountViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImDeliveryDetailCountViewId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8431913695744638259L;
	private String itemCode;
	private String warehouseCode;
	private String shipDay;
	private String shipMonth;
	private String shipYear;

	// Constructors

	/** default constructor */
	public ImDeliveryDetailCountViewId() {
	}

	/** minimal constructor */
	public ImDeliveryDetailCountViewId(String itemCode, String warehouseCode, String shipDay, String shipMonth, String shipYear) {
		this.itemCode = itemCode;
		this.warehouseCode = warehouseCode;
		this.shipDay = shipDay;
		this.shipMonth = shipMonth;
		this.shipYear = shipYear;
	}

	// Property accessors

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
	
	public void setShipDay(String shipDay) {
		this.shipDay = shipDay;
	}

	public String getShipDay() {
		return this.shipDay;
	}
	
	public void setShipMonth(String shipMonth) {
		this.shipMonth = shipMonth;
	}

	public String getShipMonth() {
		return this.shipMonth;
	}
	
	public void setShipYear(String shipYear) {
		this.shipYear = shipYear;
	}

	public String getShipYear() {
		return this.shipYear;
	}
	
	

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImDeliveryDetailCountViewId))
			return false;
		ImDeliveryDetailCountViewId castOther = (ImDeliveryDetailCountViewId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this.getItemCode() != null && castOther.getItemCode() != null && this.getItemCode().equals(castOther.getItemCode())))
				&& ((this.getWarehouseCode() == castOther.getWarehouseCode()) || (this.getWarehouseCode() != null && castOther.getWarehouseCode() != null && this.getWarehouseCode().equals(castOther.getWarehouseCode())))
				&& ((this.getShipDay() == castOther.getShipDay()) || (this.getShipDay() != null && castOther.getShipDay() != null && this.getShipDay().equals(castOther.getShipDay())))
				&& ((this.getWarehouseCode() == castOther.getShipMonth()) || (this.getShipMonth() != null && castOther.getShipMonth() != null && this.getShipMonth().equals(castOther.getShipMonth())))
				&& ((this.getWarehouseCode() == castOther.getShipYear()) || (this.getShipYear() != null && castOther.getShipYear() != null && this.getShipYear().equals(castOther.getShipYear())));

	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37
				* result
				+ (getWarehouseCode() == null ? 0 : this.getWarehouseCode()
						.hashCode());
		result = 37
		* result
		+ (getShipDay() == null ? 0 : this.getShipDay().hashCode());
		result = 37
		* result
		+ (getShipMonth() == null ? 0 : this.getShipMonth().hashCode());
		result = 37
		* result
		+ (getShipYear() == null ? 0 : this.getShipYear().hashCode());

		return result;
	}

}