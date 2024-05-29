package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiProgramLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiProgramLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3483671180973100448L;
    // Fields
    private Long id;
    private String programId;
    private String levelType;
    private String identification;
    private Long processId;
    private Long activityId;
    private String message;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;

    // Constructors

    /** default constructor */
    public SiProgramLog() {
    }

    /** full constructor */
    public SiProgramLog(String programId, String levelType,
	    String identification, Long processId, Long activityId,
	    String message, String reserve1, String reserve2, String reserve3,
	    String reserve4, String reserve5, String createdBy,
	    Date creationDate) {
	this.programId = programId;
	this.levelType = levelType;
	this.identification = identification;
	this.processId = processId;
	this.activityId = activityId;
	this.message = message;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
    }

    // Property accessors

    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getProgramId() {
	return this.programId;
    }

    public void setProgramId(String programId) {
	this.programId = programId;
    }

    public String getLevelType() {
	return this.levelType;
    }

    public void setLevelType(String levelType) {
	this.levelType = levelType;
    }

    public String getIdentification() {
	return this.identification;
    }

    public void setIdentification(String identification) {
	this.identification = identification;
    }

    public Long getProcessId() {
	return this.processId;
    }

    public void setProcessId(Long processId) {
	this.processId = processId;
    }

    public Long getActivityId() {
	return this.activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public String getMessage() {
	return this.message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getReserve1() {
	return this.reserve1;
    }

    public void setReserve1(String reserve1) {
	this.reserve1 = reserve1;
    }

    public String getReserve2() {
	return this.reserve2;
    }

    public void setReserve2(String reserve2) {
	this.reserve2 = reserve2;
    }

    public String getReserve3() {
	return this.reserve3;
    }

    public void setReserve3(String reserve3) {
	this.reserve3 = reserve3;
    }

    public String getReserve4() {
	return this.reserve4;
    }

    public void setReserve4(String reserve4) {
	this.reserve4 = reserve4;
    }

    public String getReserve5() {
	return this.reserve5;
    }

    public void setReserve5(String reserve5) {
	this.reserve5 = reserve5;
    }

    public String getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreationDate() {
	return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

}