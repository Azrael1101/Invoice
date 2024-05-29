package tw.com.tm.erp.hbm.bean;

/**
 * ImOnHandViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImOnHandView implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 6288202453854677060L;
	private String itemCode;
	private String organizationCode;
	private String warehouseCode;
	private String itemCName;
	private String itemEName;
	private Long onHand;
	private Long uncommit;

	// Constructors

	/** default constructor */
	public ImOnHandView() {
	}

	/** minimal constructor */
	public ImOnHandView(String itemCode, String organizationCode,
			String warehouseCode) {
		this.itemCode = itemCode;
		this.organizationCode = organizationCode;
		this.warehouseCode = warehouseCode;
	}

	/** full constructor */
	public ImOnHandView(String itemCode, String organizationCode,
			String warehouseCode, String itemCName, String itemEName,
			Long onHand, Long uncommit) {
		this.itemCode = itemCode;
		this.organizationCode = organizationCode;
		this.warehouseCode = warehouseCode;
		this.itemCName = itemCName;
		this.itemEName = itemEName;
		this.onHand = onHand;
		this.uncommit = uncommit;
	}

	// Property accessors

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getItemCName() {
		return this.itemCName;
	}

	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}

	public String getItemEName() {
		return this.itemEName;
	}

	public void setItemEName(String itemEName) {
		this.itemEName = itemEName;
	}

	public Long getOnHand() {
		return this.onHand;
	}

	public void setOnHand(Long onHand) {
		this.onHand = onHand;
	}

	public Long getUncommit() {
		return this.uncommit;
	}

	public void setUncommit(Long uncommit) {
		this.uncommit = uncommit;
	}
}