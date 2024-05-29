package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import tw.com.tm.erp.utils.DateUtils;

public class OmmChannelTXFHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3656689703873685702L;
	private Long sysSno;
	private String channelType;
	private String categoryType;
	private String orderTypeCode;
	private String orderNo;
	private String status;
	private String enable;
	private String sysModifierAmail;
	private Date sysLastUpdateTime;
	private String sysSign;
	private List<OmmChannelTXFLine> ommChannelTXFLineList;
	
	/**
	 * DB不儲存的欄位
	 */
	private String sysLastUpdateTimeStr;

	public void setSysLastUpdateTimeStr(String sysLastUpdateTimeStr) {
		this.sysLastUpdateTimeStr = sysLastUpdateTimeStr;
	}

	public String getSysLastUpdateTimeStr() {
		return sysLastUpdateTimeStr;
	}

	/**
	 * 
	 */
	public OmmChannelTXFHead() {
	}

	/**
	 * @param sysSno
	 * @param channelType
	 * @param categoryType
	 * @param orderTypeCode
	 * @param orderNo
	 * @param status
	 * @param enable
	 * @param sysModifierAmail
	 * @param sysLastUpdated
	 */
	public OmmChannelTXFHead(Long sysSno, String channelType, String categoryType, String orderTypeCode, String orderNo,
			String status, String enable, String sysModifierAmail, Date sysLastUpdateTime, String sysSign ,
			List<OmmChannelTXFLine> ommChannelTXFLineList) {
		this.sysSno = sysSno;
		this.channelType = channelType;
		this.categoryType = categoryType;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.status = status;
		this.enable = enable;
		this.sysModifierAmail = sysModifierAmail;
		this.sysLastUpdateTime = sysLastUpdateTime;
		this.ommChannelTXFLineList = ommChannelTXFLineList;
		this.sysSign = sysSign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
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

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

		if (sysLastUpdateTime != null)
			this.setSysLastUpdateTimeStr(DateUtils.formatTime(sysLastUpdateTime));
	}

	public String getSysSign() {
		return sysSign;
	}

	public void setSysSign(String sysSign) {
		this.sysSign = sysSign;
	}

	public List<OmmChannelTXFLine> getOmmChannelTXFLineList() {
		return ommChannelTXFLineList;
	}

	public void setOmmChannelTXFLineList(List<OmmChannelTXFLine> ommChannelTXFLineList) {
		this.ommChannelTXFLineList = ommChannelTXFLineList;
	}

}
