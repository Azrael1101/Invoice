package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;

public class BumOrganizationTree implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2746092876594820776L;
	private Long sysSno;
	private BumOrganizationTree parentNode;
	private String businessUnitName;
	private Long indexNo;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private List<BumOrganizationTree> childNodeList;

	public BumOrganizationTree() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sysSno
	 * @param parentNode
	 * @param businessUnitName
	 * @param indexNo
	 * @param sysModifierAmail
	 * @param sysLastUpdateTime
	 * @param childNodeList
	 */
	public BumOrganizationTree(Long sysSno, BumOrganizationTree parentNode, String businessUnitName, Long indexNo,
			String sysModifierAmail, Date sysLastUpdateTime, List<BumOrganizationTree> childNodeList) {
		this.sysSno = sysSno;
		this.parentNode = parentNode;
		this.businessUnitName = businessUnitName;
		this.indexNo = indexNo;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.childNodeList = childNodeList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public BumOrganizationTree getParentNode() {
		return parentNode;
	}

	public void setParentNode(BumOrganizationTree parentNode) {
		this.parentNode = parentNode;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getSysModifierAmail() {
		return sysModifierAmail;
	}

	public void setSysModifierAmail(String sysModifierAmail) {
		this.sysModifierAmail = sysModifierAmail;
	}

	public Date getSysLastUpdateTime() {
		return sysLastUpdateTime;
	}

	public void setSysLastUpdateTime(Date sysLastUpdateTime) {
		this.sysLastUpdateTime = sysLastUpdateTime;
	}

	public List<BumOrganizationTree> getChildNodeList() {
		return childNodeList;
	}

	public void setChildNodeList(List<BumOrganizationTree> childNodeList) {
		this.childNodeList = childNodeList;
	}
}
