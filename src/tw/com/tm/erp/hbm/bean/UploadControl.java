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

public class UploadControl implements java.io.Serializable {

	// Fields

	//private static final long serialVersionUID = 7308297612877249187L;
	private Long headId;
	private String orderTypeCode;
	private String statusByAC;
	private String statusByBC;
	private String statusByED;
	private Date schedualDate;
	private String schedual;


	private String lastUpdatedByBC;
	private Date lastBCUpdateDate;
	private String lastUpdatedByED;
	private Date lastEDUpdateDate;
	private String lastUpdatedByAC;
	private Date lastACUpdateDate;

	private String button;
	private Long index;
	private Long orderAmount;

	
	
	//private Set<BuBranch> buBranchs = new HashSet(0);
	
	// Constructors

	/** default constructor */
	public UploadControl() {
	}

	/** minimal constructor */
	public UploadControl(Long headId) {
		this.headId = headId;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getStatusByAC() {
		return statusByAC;
	}

	public void setStatusByAC(String statusByAC) {
		this.statusByAC = statusByAC;
	}

	public String getStatusByBC() {
		return statusByBC;
	}

	public void setStatusByBC(String statusByBC) {
		this.statusByBC = statusByBC;
	}

	public String getStatusByED() {
		return statusByED;
	}

	public void setStatusByED(String statusByED) {
		this.statusByED = statusByED;
	}

	public Date getSchedualDate() {
		return schedualDate;
	}

	public void setSchedualDate(Date schedualDate) {
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

	public String getLastUpdatedByED() {
		return lastUpdatedByED;
	}

	public void setLastUpdatedByED(String lastUpdatedByED) {
		this.lastUpdatedByED = lastUpdatedByED;
	}

	public Date getLastEDUpdateDate() {
		return lastEDUpdateDate;
	}

	public void setLastEDUpdateDate(Date lastEDUpdateDate) {
		this.lastEDUpdateDate = lastEDUpdateDate;
	}

	public String getLastUpdatedByAC() {
		return lastUpdatedByAC;
	}

	public void setLastUpdatedByAC(String lastUpdatedByAC) {
		this.lastUpdatedByAC = lastUpdatedByAC;
	}

	public Date getLastACUpdateDate() {
		return lastACUpdateDate;
	}

	public void setLastACUpdateDate(Date lastACUpdateDate) {
		this.lastACUpdateDate = lastACUpdateDate;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}



}