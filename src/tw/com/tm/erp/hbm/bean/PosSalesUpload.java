package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCountry entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosSalesUpload implements java.io.Serializable {

			
	private Long headId;
	private String dataId;
	private String action;
	private String transactionNoStart;
	private String transactionNoEnd;
	private String store;
	private Date transactionDate;
	
	
	public PosSalesUpload() {

	}
	public Long getHeadId() {
		return headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTransactionNoStart() {
		return transactionNoStart;
	}
	public void setTransactionNoStart(String transactionNoStart) {
		this.transactionNoStart = transactionNoStart;
	}
	public String getTransactionNoEnd() {
		return transactionNoEnd;
	}
	public void setTransactionNoEnd(String transactionNoEnd) {
		this.transactionNoEnd = transactionNoEnd;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	


}