package tw.com.tm.erp.tree;

import java.io.Serializable;
import java.util.List;

public class ExtNode implements Serializable{ 

	private static final long serialVersionUID = -4267957262795587655L;
	
	Long id;
	String text;
	boolean leaf;
	boolean hasChildren;
	boolean checked;
	Long parentMenuId;
	
	private List<ExtNode> children = new java.util.ArrayList<ExtNode>();
	
	private FunctionNode functionNode;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public List<ExtNode> getChildren() {
		return children;
	}
	public void setChildren(List<ExtNode> children) {
		this.children = children;
	}
	public Long getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public FunctionNode getFunctionNode() {
		return functionNode;
	}
	public void setFunctionNode(FunctionNode functionNode) {
		this.functionNode = functionNode;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "===========<start>=============\n");
		sb.append( "id          		= " + id + "\n");
		sb.append( "text        		= " + text + "\n");
		sb.append( "leaf        		= " + leaf + "\n");
		sb.append( "hasChildren 		= " + hasChildren + "\n"); 
		sb.append( "checked					= " + checked + "\n");  
		sb.append( "children.size   = " + children.size() + "\n");
		sb.append( "<FunctionCode>	= " + functionNode.getFunctionCode() + "\n");
		sb.append( "======<FunctionCode>======\n");
		List<FunctionObject> fos = functionNode.getFunctionObjects();
		for (FunctionObject fo : fos) {
			sb.append( "<ObjectCode>	= " + fo.getObjectCode() + "\n");
			sb.append( "<ObjectName>	= " + fo.getObjectName() + "\n");
			sb.append( "<ControlType>	= " + fo.getControlType() + "\n");
			sb.append( "<Checked>			= " + fo.isObjectChecked() + "\n");
		}
		sb.append( "======</FunctionCode>======\n");
		sb.append( "============</end>============\n");
		return sb.toString() ;
	}
	
}
