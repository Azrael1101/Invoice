package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class BumBusinessUnit implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3932151352438135164L;
	private Long sysSno;
	private String businessUnitType;
	private String businessUnitName;
	private String memberType;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;

	public BumBusinessUnit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BumBusinessUnit(Long sysSno, String businessUnitType, String businessUnitName, String memberType,
			String sysModifierAmail, Date sysLastUpdateTime) {
		super();
		this.sysSno = sysSno;
		this.businessUnitType = businessUnitType;
		this.businessUnitName = businessUnitName;
		this.memberType = memberType;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getBusinessUnitType() {
		return businessUnitType;
	}

	public void setBusinessUnitType(String businessUnitType) {
		this.businessUnitType = businessUnitType;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
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

}
