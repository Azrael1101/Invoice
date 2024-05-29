package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;

public class EtlTransformLog implements Serializable{

	private static final long serialVersionUID = -85285735601509754L;
	
	private Long sysSno;
	private EtlEventViewTable etlEventViewTable;
	private String message;
	private String messageLevel;
	private Short indexNo;
	
	public EtlTransformLog() {
	}

	/**
	 * @param sysSno
	 * @param etlEventViewTable
	 * @param message
	 * @param messageLevel
	 * @param indexNo
	 */
	public EtlTransformLog(Long sysSno, EtlEventViewTable etlEventViewTable, String message, String messageLevel,
			Short indexNo) {
		this.sysSno = sysSno;
		this.etlEventViewTable = etlEventViewTable;
		this.message = message;
		this.messageLevel = messageLevel;
		this.indexNo = indexNo;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public EtlEventViewTable getEtlEventViewTable() {
		return etlEventViewTable;
	}

	public void setEtlEventViewTable(EtlEventViewTable etlEventViewTable) {
		this.etlEventViewTable = etlEventViewTable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageLevel() {
		return messageLevel;
	}

	public void setMessageLevel(String messageLevel) {
		this.messageLevel = messageLevel;
	}

	public Short getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Short indexNo) {
		this.indexNo = indexNo;
	}
}
