package tw.com.tm.erp.tree;

import java.util.List;

public class FunctionNode {
	private String functionCode;
	private List<FunctionObject> functionObjects;
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public List<FunctionObject> getFunctionObjects() {
		return functionObjects;
	}
	public void setFunctionObjects(List<FunctionObject> functionObjects) {
		this.functionObjects = functionObjects;
	}
	public FunctionNode(String functionCode, List<FunctionObject> functionObjects) {
		super();
		this.functionCode = functionCode;
		this.functionObjects = functionObjects;
	}
	public FunctionNode() {
	}
}
