package tw.com.tm.erp.hbm.bean;

/**
 * SiGroupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiMenuViewId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 3697564177565910622L;
	private String brandCode;
	private String employeeRole;
	private String menuId;

	// Constructors

	/** default constructor */
	public SiMenuViewId() {
	}

	/** full constructor */
	public SiMenuViewId(String brandCode, String employeeRole, String menuId) {
		this.brandCode = brandCode;
		this.employeeRole = employeeRole;
		this.menuId = menuId;
	}

	// Property accessors
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(String employeeRole) {
		this.employeeRole = employeeRole;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}