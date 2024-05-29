package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SoDeliveryHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCustServiceLine implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5310168888042351026L;
	/**
	 * 
	 */
	
	private Long headId;
	private Long lineId;
	private Long indexNo;
	private Date executeDate;
	private String memo;
	private AdCustServiceHead adCustServiceHead;
	private String isDeleteRecord;
	private String recordDate;
	private String txtCount;
	

	/** default constructor */
	public AdCustServiceLine() {
		
	}

	/** full constructor */
	public AdCustServiceLine(Long headId,Long indexNo, Long lineId) {
		
    this.headId=headId;
    this.lineId=lineId;
  
		
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}


	public Date getExecuteDate() {
		return executeDate;
	}

	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public AdCustServiceHead getAdCustServiceHead() {
		return adCustServiceHead;
	}

	public void setAdCustServiceHead(AdCustServiceHead adCustServiceHead) {
		this.adCustServiceHead = adCustServiceHead;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getTxtCount() {
		return txtCount;
	}

	public void setTxtCount(String txtCount) {
		this.txtCount = txtCount;
	}
}

// Property accessors

