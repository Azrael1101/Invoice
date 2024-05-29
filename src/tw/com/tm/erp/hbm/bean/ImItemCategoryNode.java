package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.List;

public class ImItemCategoryNode implements Serializable{
	private static final long serialVersionUID = 8343888738620534754L;
	
	private Long sysSno;
	private ImItemCategoryNode pImItemCategoryNode;
	private String brandCode;
	private String categoryLevelCode;
	private String categoryNodeCode;
	private String categoryNodeName;
	private String pCategoryLevelCode;
	private String pCategoryNodeCode;
	private Long columnIndex;
	private List<ImItemCategoryNode> cImItemCategoryNodeList;
	
	public ImItemCategoryNode() {
	}

	/**
	 * @param sysSno
	 * @param pImItemCategoryNode
	 * @param brandCode
	 * @param categoryLevelCode
	 * @param categoryNodeCode
	 * @param categoryNodeName
	 * @param pCategoryLevelCode
	 * @param pCategoryNodeCode
	 * @param columnIndex
	 * @param cImItemCategoryNodeList
	 */
	public ImItemCategoryNode(Long sysSno, ImItemCategoryNode pImItemCategoryNode, String brandCode,
			String categoryLevelCode, String categoryNodeCode, String categoryNodeName, String pCategoryLevelCode,
			String pCategoryNodeCode, Long columnIndex, List<ImItemCategoryNode> cImItemCategoryNodeList) {
		this.sysSno = sysSno;
		this.pImItemCategoryNode = pImItemCategoryNode;
		this.brandCode = brandCode;
		this.categoryLevelCode = categoryLevelCode;
		this.categoryNodeCode = categoryNodeCode;
		this.categoryNodeName = categoryNodeName;
		this.pCategoryLevelCode = pCategoryLevelCode;
		this.pCategoryNodeCode = pCategoryNodeCode;
		this.columnIndex = columnIndex;
		this.cImItemCategoryNodeList = cImItemCategoryNodeList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public ImItemCategoryNode getpImItemCategoryNode() {
		return pImItemCategoryNode;
	}

	public void setpImItemCategoryNode(ImItemCategoryNode pImItemCategoryNode) {
		this.pImItemCategoryNode = pImItemCategoryNode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getCategoryLevelCode() {
		return categoryLevelCode;
	}

	public void setCategoryLevelCode(String categoryLevelCode) {
		this.categoryLevelCode = categoryLevelCode;
	}

	public String getCategoryNodeCode() {
		return categoryNodeCode;
	}

	public void setCategoryNodeCode(String categoryNodeCode) {
		this.categoryNodeCode = categoryNodeCode;
	}

	public String getCategoryNodeName() {
		return categoryNodeName;
	}

	public void setCategoryNodeName(String categoryNodeName) {
		this.categoryNodeName = categoryNodeName;
	}

	public String getpCategoryLevelCode() {
		return pCategoryLevelCode;
	}

	public void setpCategoryLevelCode(String pCategoryLevelCode) {
		this.pCategoryLevelCode = pCategoryLevelCode;
	}

	public String getpCategoryNodeCode() {
		return pCategoryNodeCode;
	}

	public void setpCategoryNodeCode(String pCategoryNodeCode) {
		this.pCategoryNodeCode = pCategoryNodeCode;
	}

	public Long getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Long columnIndex) {
		this.columnIndex = columnIndex;
	}

	public List<ImItemCategoryNode> getcImItemCategoryNodeList() {
		return cImItemCategoryNodeList;
	}

	public void setcImItemCategoryNodeList(List<ImItemCategoryNode> cImItemCategoryNodeList) {
		this.cImItemCategoryNodeList = cImItemCategoryNodeList;
	}
}
