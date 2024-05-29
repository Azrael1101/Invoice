package tw.com.tm.erp.tree;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MenuNode extends DefaultMutableTreeNode implements Serializable{
	
	private static final Log log = LogFactory.getLog(MenuNode.class);
	
	private static final long serialVersionUID = -8658109181078614183L;
	
	private Long id;
	private String text;
	private Long parentMenuId;
	private boolean checked;
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
	
	public MenuNode(Long id, String text, Long parentMenuId, Boolean checked, FunctionNode functionNode ) {
		super();
		this.id = id;
		this.text = text;
		this.parentMenuId = parentMenuId;
		this.checked = checked;
		this.functionNode = functionNode;
	}

	public List<ExtNode> getExtTree( MenuNode tree ){
//		log.info( "===========<getExtTree>===========");
		
		List<ExtNode> root = new ArrayList<ExtNode>();
		
		Enumeration en = tree.breadthFirstEnumeration();
		while (en.hasMoreElements()) {
			
			MenuNode menuNode = (MenuNode) en.nextElement();
			if( menuNode.getLevel() == 0 ){
				continue;
			}
			
			boolean isLeaf = menuNode.isLeaf();
			Long id = menuNode.getId();
			String text = menuNode.getText();
			Long parentMenuId = menuNode.getParentMenuId();
			Boolean checked = menuNode.isChecked();
			FunctionNode functionNode = menuNode.getFunctionNode();
			
			ExtNode extNode = new ExtNode();
			extNode.setId( id );
			extNode.setText( text );
			extNode.setLeaf( isLeaf ); 
			extNode.setHasChildren( !isLeaf );
			extNode.setParentMenuId( parentMenuId );
			extNode.setChecked(checked);
			extNode.setFunctionNode( functionNode ); 
			
			if( parentMenuId.equals( 0L ) ){
//				log.info( "插入 = " + id + " " + text + " " + parentMenuId + " " + isLeaf );
				root.add( extNode );
			} else { // 遞迴塞入
//				log.info( "等待插入 = " + id + " " + text + " " + parentMenuId + " " + isLeaf );
				root = findId( root , extNode );
				
			}
		}
//		log.info( "===========</getExtTree>===========");
		return root;
	}
	
	public List<ExtNode> findId( List<ExtNode> extChildren, ExtNode extNode ){
//		log.info( "===============<findId>===============" );
		for (int i = 0; i < extChildren.size(); i++) {
			ExtNode tmp = extChildren.get(i);
//			log.info( tmp.getId()+ " 比對 " + extNode.getParentMenuId() );
			if( tmp.getId().equals( extNode.getParentMenuId() ) ) {
//				log.info( "extNode " + extNode.getId()+ "是 tmp " + tmp.getId()+ " 的小孩 " );
				tmp.getChildren().add( extNode ); // extNode 是 tmp 的小孩
				return extChildren;
			}else { 
				// 找不到則向下遞迴找
				if( tmp.getChildren().size() > 0 ){
					findId( tmp.getChildren(), extNode );
				}	
			}
			
		}
//		log.info( "===============</findId>===============" );	
		return extChildren;
		
	}

}
