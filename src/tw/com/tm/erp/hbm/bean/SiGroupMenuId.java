package tw.com.tm.erp.hbm.bean;

/**
 * SiGroupMenuId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroupMenuId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 7985484364011815826L;
	private String brandCode;
	private String groupCode;
	private Long menuId;

	// Constructors

	/** default constructor */
	public SiGroupMenuId() {
	}

	/** full constructor */
	public SiGroupMenuId(String brandCode, String groupCode, Long menuId) {
		this.brandCode = brandCode;
		this.groupCode = groupCode;
		this.menuId = menuId;
	}

	// Property accessors

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SiGroupMenuId))
			return false;
		SiGroupMenuId castOther = (SiGroupMenuId) other;

		return ((this.getBrandCode() == castOther.getBrandCode()) || (this
				.getBrandCode() != null
				&& castOther.getBrandCode() != null && this.getBrandCode()
				.equals(castOther.getBrandCode())))
				&& ((this.getGroupCode() == castOther.getGroupCode()) || (this
						.getGroupCode() != null
						&& castOther.getGroupCode() != null && this
						.getGroupCode().equals(castOther.getGroupCode())))
				&& ((this.getMenuId() == castOther.getMenuId()) || (this
						.getMenuId() != null
						&& castOther.getMenuId() != null && this.getMenuId()
						.equals(castOther.getMenuId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrandCode() == null ? 0 : this.getBrandCode().hashCode());
		result = 37 * result
				+ (getGroupCode() == null ? 0 : this.getGroupCode().hashCode());
		result = 37 * result
				+ (getMenuId() == null ? 0 : this.getMenuId().hashCode());
		return result;
	}

}