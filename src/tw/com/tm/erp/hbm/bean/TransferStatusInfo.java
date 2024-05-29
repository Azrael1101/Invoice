package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class TransferStatusInfo implements java.io.Serializable {

	private static final long serialVersionUID = 3699940356722417340L;
	private long transferId;
	private Date transferDate;
	private String transferName;
	private String transferStatus;
	private String transferTable;
	
	public long getTransferId() {
		return transferId;
	}
	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public String getTransferName() {
		return transferName;
	}
	public void setTransferName(String transferName) {
		this.transferName = transferName;
	}
	public String getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	public String getTransferTable() {
		return transferTable;
	}
	public void setTransferTable(String transferTable) {
		this.transferTable = transferTable;
	}
	
	
}
