package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.List;

public class EtlEventViewTable implements Serializable{

	private static final long serialVersionUID = -7264534712147784469L;
	
	private Long sysSno;
	private Long pSysSno;//要刪掉
	private String module;
	private String orderTypeCode;
	private String orderNo;
	private Long currentEventCode;
	private Long targetEventCode;
	private String eventStatus;
	private Character status;
	private Integer numberOfExecutions;
	private List<EtlTransformLog> etlTransformLogList;

	public EtlEventViewTable() {
	}

	/**
	 * @param sysSno
	 * @param pSysSno
	 * @param module
	 * @param orderTypeCode
	 * @param orderNo
	 * @param currentEventCode
	 * @param targetEventCode
	 * @param eventStatus
	 * @param status
	 * @param numberOfExecutions
	 * @param etlTransformLogList
	 */
	public EtlEventViewTable(Long sysSno, Long pSysSno, String module, String orderTypeCode, String orderNo,
			Long currentEventCode, Long targetEventCode, String eventStatus, Character status,
			Integer numberOfExecutions, List<EtlTransformLog> etlTransformLogList) {
		this.sysSno = sysSno;
		this.pSysSno = pSysSno;
		this.module = module;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.currentEventCode = currentEventCode;
		this.targetEventCode = targetEventCode;
		this.eventStatus = eventStatus;
		this.status = status;
		this.numberOfExecutions = numberOfExecutions;
		this.etlTransformLogList = etlTransformLogList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public Long getpSysSno() {
		return pSysSno;
	}

	public void setpSysSno(Long pSysSno) {
		this.pSysSno = pSysSno;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getCurrentEventCode() {
		return currentEventCode;
	}

	public void setCurrentEventCode(Long currentEventCode) {
		this.currentEventCode = currentEventCode;
	}

	public Long getTargetEventCode() {
		return targetEventCode;
	}

	public void setTargetEventCode(Long targetEventCode) {
		this.targetEventCode = targetEventCode;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getNumberOfExecutions() {
		return numberOfExecutions;
	}

	public void setNumberOfExecutions(Integer numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}

	public List<EtlTransformLog> getEtlTransformLogList() {
		return etlTransformLogList;
	}

	public void setEtlTransformLogList(List<EtlTransformLog> etlTransformLogList) {
		this.etlTransformLogList = etlTransformLogList;
	}
}
