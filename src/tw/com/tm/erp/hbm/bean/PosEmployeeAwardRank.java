package tw.com.tm.erp.hbm.bean;

/**
 * PosEmployee entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosEmployeeAwardRank implements java.io.Serializable {

	// Fields

	private String dataId;
	private Long headId;
	private String action;
	private String employeeDepartment;
	private String employeeCode;
	private String employeeName;
	private Double employeePoint;
	private String employeeRank;
	
	// Constructors

	/** default constructor */
	public PosEmployeeAwardRank() {
	}
	
	// Property accessors

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public Double getEmployeePoint() {
		return employeePoint;
	}

	public void setEmployeePoint(Double employeePoint) {
		this.employeePoint = employeePoint;
	}

	public String getEmployeeRank() {
		return employeeRank;
	}

	public void setEmployeeRank(String employeeRank) {
		this.employeeRank = employeeRank;
	}
}