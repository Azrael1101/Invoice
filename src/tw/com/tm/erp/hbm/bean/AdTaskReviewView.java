package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeWithAddressView entity.
 * 
 * @author MyEclipse Persistence Tools
 */


public class AdTaskReviewView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8460982326474830711L;
	// Fields

	/**
	 * 
	 */

	private String no;
	private String specInfo;
	private String taskType;
	private String status;
	private Date countingDate;
	private String incharge;
	private Integer mon;
	private Integer tue;
	private Integer wed;
	private Integer thu;
	private Integer fri;
	private Integer sat;
	private Integer sun;
	private Long lineId;
	
	
	// Constructors

	/** default constructor */
	public AdTaskReviewView() {
	}

	/** minimal constructor */
	public AdTaskReviewView(Long lineId, String specInfo,
			String taskType, String incharge) {
		this.lineId = lineId;
		this.specInfo = specInfo;
		this.taskType = taskType;
		this.incharge = incharge;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getSpecInfo() {
		return specInfo;
	}

	public void setSpecInfo(String specInfo) {
		this.specInfo = specInfo;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCountingDate() {
		return countingDate;
	}

	public void setCountingDate(Date countingDate) {
		this.countingDate = countingDate;
	}

	public String getIncharge() {
		return incharge;
	}

	public void setIncharge(String incharge) {
		this.incharge = incharge;
	}

	public Integer getMon() {
		return mon;
	}

	public void setMon(Integer mon) {
		this.mon = mon;
	}

	public Integer getTue() {
		return tue;
	}

	public void setTue(Integer tue) {
		this.tue = tue;
	}

	public Integer getWed() {
		return wed;
	}

	public void setWed(Integer wed) {
		this.wed = wed;
	}

	public Integer getThu() {
		return thu;
	}

	public void setThu(Integer thu) {
		this.thu = thu;
	}

	public Integer getFri() {
		return fri;
	}

	public void setFri(Integer fri) {
		this.fri = fri;
	}

	public Integer getSat() {
		return sat;
	}

	public void setSat(Integer sat) {
		this.sat = sat;
	}

	public Integer getSun() {
		return sun;
	}

	public void setSun(Integer sun) {
		this.sun = sun;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/** full constructor */

	// Property accessors



	
}