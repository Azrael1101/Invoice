package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTallyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiPosLogId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3325398012106651249L;
	// Fields
	private String processName;
	private String shopCode;
	private Date transactionDate;
        private String posMachineCode ;

	public String getShopCode() {
		return this.shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getPosMachineCode() {
		return posMachineCode;
	}

	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}

}