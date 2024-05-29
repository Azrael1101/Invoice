package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;

public class OmmChannelConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504342915218588749L;

	private Long sysSno;
	private String columnCode;
	private String columnName;
	private Long columnIndex;
	private String enable;
	private OmmChannel ommChannel;

	/**
	 * 
	 */
	public OmmChannelConfig() {
	}

	/**
	 * @param sysSno
	 * @param pSysSno
	 * @param columnCode
	 * @param columnName
	 * @param enable
	 */
	public OmmChannelConfig(Long sysSno, String columnCode, String columnName, Long columnIndex, String enable, OmmChannel ommChannel) {
		this.sysSno = sysSno;
		this.columnCode = columnCode;
		this.columnName = columnName;
		this.columnIndex = columnIndex;
		this.enable = enable;
		this.ommChannel = ommChannel;
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
	
	public OmmChannel getOmmChannel() {
		return ommChannel;
	}

	public void setOmmChannel(OmmChannel ommChannel) {
		this.ommChannel = ommChannel;
	}

}
