package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BuOrganization entity. 
 * 
 * @author MyEclipse Persistence Tools
 */

public class UploadControlList implements java.io.Serializable {

	private String orderTypeCode;
	private Long orderAmount;
	private String schedualDate;
	private String schedual;
	private String lastUpdatedByBC;
	private String lastUpdatedByAC;
	private String lastUpdatedByED;
	private Date lastBCUpdateDate;
	private String statusByAC;
	private String statusByED;
	private String statusByBC;
	private String button;
	private Long index;

	/** default constructor */
	public UploadControlList() {
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}





	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}





	public Long getOrderAmount() {
		return orderAmount;
	}





	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}





	public String getSchedualDate() {
		return schedualDate;
	}





	public void setSchedualDate(String schedualDate) {
		this.schedualDate = schedualDate;
	}





	public String getSchedual() {
		return schedual;
	}





	public void setSchedual(String schedual) {
		this.schedual = schedual;
	}





	public String getLastUpdatedByBC() {
		return lastUpdatedByBC;
	}





	public void setLastUpdatedByBC(String lastUpdatedByBC) {
		this.lastUpdatedByBC = lastUpdatedByBC;
	}





	public Date getLastBCUpdateDate() {
		return lastBCUpdateDate;
	}





	public void setLastBCUpdateDate(Date lastBCUpdateDate) {
		this.lastBCUpdateDate = lastBCUpdateDate;
	}





	public String getStatusByBC() {
		return statusByBC;
	}





	public void setStatusByBC(String statusByBC) {
		this.statusByBC = statusByBC;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public String getStatusByAC() {
		return statusByAC;
	}

	public void setStatusByAC(String statusByAC) {
		this.statusByAC = statusByAC;
	}

	public String getStatusByED() {
		return statusByED;
	}

	public void setStatusByED(String statusByED) {
		this.statusByED = statusByED;
	}

	public String getLastUpdatedByAC() {
		return lastUpdatedByAC;
	}

	public void setLastUpdatedByAC(String lastUpdatedByAC) {
		this.lastUpdatedByAC = lastUpdatedByAC;
	}

	public String getLastUpdatedByED() {
		return lastUpdatedByED;
	}

	public void setLastUpdatedByED(String lastUpdatedByED) {
		this.lastUpdatedByED = lastUpdatedByED;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}





}