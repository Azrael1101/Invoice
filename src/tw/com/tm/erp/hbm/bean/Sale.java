package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.Date;

public class Sale implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long seqNo;
	private Date sdate;
	private String store;
	private String maNo;
	private String status;
	
	public Sale() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Sale(Long seqNo, Date sdate, String store, String maNo, String status) {
		super();
		this.seqNo = seqNo;
		this.sdate = sdate;
		this.store = store;
		this.maNo = maNo;
		this.status = status;
	}
	
	/**
	 * @return the seq_no
	 */
	public Long getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seq_no the seq_no to set
	 */
	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return the sdate
	 */
	public Date getSdate() {
		return sdate;
	}
	/**
	 * @param sdate the sdate to set
	 */
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}
	/**
	 * @return the maNo
	 */
	public String getMaNo() {
		return maNo;
	}
	/**
	 * @param maNo the maNo to set
	 */
	public void setMaNo(String maNo) {
		this.maNo = maNo;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
