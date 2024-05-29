package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.Date;

public class OmmChannelTXFLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4338645084855643022L;
	private Long sysSno;
	private String columnCode;
	private String columnName;
	private Long columnIndex;
	private String enable;
	private String sysSign;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private String sysVerifyCode;
	private OmmChannelTXFHead ommChannelTXFHead;

	/**
	 * 
	 */
	public OmmChannelTXFLine() {
	}

	/**
	 * @param sysSno
	 * @param columnCode
	 * @param columnName
	 * @param enable
	 * @param sysSign
	 * @param sysModifierAmail
	 * @param sysLastUpdated
	 * @param sysVerifyCode
	 */
	public OmmChannelTXFLine(Long sysSno, String columnCode, String columnName, Long columnIndex, String enable,
			String sysSign, String sysModifierAmail, Date sysLastUpdateTime, String sysVerifyCode, OmmChannelTXFHead ommChannelTXFHead) {
		this.sysSno = sysSno;
		this.columnCode = columnCode;
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.enable = enable;
		this.sysSign = sysSign;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.sysVerifyCode = sysVerifyCode;
		this.ommChannelTXFHead = ommChannelTXFHead;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public Long getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Long columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getSysSign() {
		return sysSign;
	}

	public void setSysSign(String sysSign) {
		this.sysSign = sysSign;
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

	public String getSysVerifyCode() {
		return sysVerifyCode;
	}

	public void setSysVerifyCode(String sysVerifyCode) {
		this.sysVerifyCode = sysVerifyCode;
	}
	
	public OmmChannelTXFHead getOmmChannelTXFHead() {
		return ommChannelTXFHead;
	}

	public void setOmmChannelTXFHead(OmmChannelTXFHead ommChannelTXFHead) {
		this.ommChannelTXFHead = ommChannelTXFHead;
	}
	

}
