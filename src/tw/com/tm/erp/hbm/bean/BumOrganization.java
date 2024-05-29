package tw.com.tm.erp.hbm.bean;

import java.util.Date;
import java.util.List;

public class BumOrganization implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -709997171500359867L;
	private Long sysSno;
	private String organizationType;
	private String organizationName;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private List<BumOrganizationTree> bumOrganizationTreeList;

	public BumOrganization() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BumOrganization(Long sysSno, String organizationType, String organizationName, String sysModifierAmail,
			Date sysLastUpdateTime, List<BumOrganizationTree> bumOrganizationTreeList) {
		super();
		this.sysSno = sysSno;
		this.organizationType = organizationType;
		this.organizationName = organizationName;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.bumOrganizationTreeList = bumOrganizationTreeList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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

	public List<BumOrganizationTree> getBumOrganizationTreeList() {
		return bumOrganizationTreeList;
	}

	public void setBumOrganizationTreeList(List<BumOrganizationTree> bumOrganizationTreeList) {
		this.bumOrganizationTreeList = bumOrganizationTreeList;
	}

}
