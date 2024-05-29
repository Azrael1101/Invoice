package tw.com.tm.erp.tree;

public class FunctionObject {
	private String objectCode;
	private String objectName;
	private String controlType;
	private boolean objectChecked;
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public boolean isObjectChecked() {
		return objectChecked;
	}
	public void setObjectChecked(boolean objectChecked) {
		this.objectChecked = objectChecked;
	}
	public FunctionObject(String objectCode, String objectName,
			String controlType, boolean objectChecked) {
		super();
		this.objectCode = objectCode;
		this.objectName = objectName;
		this.controlType = controlType;
		this.objectChecked = objectChecked;
	}	
	public FunctionObject() {
	}
}
