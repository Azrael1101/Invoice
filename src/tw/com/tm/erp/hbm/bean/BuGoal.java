package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuGoalDeployHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoal implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 4258194136850366186L;
    private Long headId;
    private String brandCode;
    private String goalCode;
    private String department;
    private String descrption;
    private String year;
    private String month;
    private Double goal;
    private String status;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private List<BuGoalEmployee> buGoalEmployees = new ArrayList();
    private List<BuGoalTarget> buGoalTargets = new ArrayList();
    private List<BuGoalWork> buGoalWorks = new ArrayList();
    
    // Constructors
    public BuGoal() {
    }

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getGoalCode() {
		return goalCode;
	}

	public void setGoalCode(String goalCode) {
		this.goalCode = goalCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getGoal() {
		return goal;
	}

	public void setGoal(Double goal) {
		this.goal = goal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<BuGoalEmployee> getBuGoalEmployees() {
		return buGoalEmployees;
	}

	public void setBuGoalEmployees(List<BuGoalEmployee> buGoalEmployees) {
		this.buGoalEmployees = buGoalEmployees;
	}

	public List<BuGoalTarget> getBuGoalTargets() {
		return buGoalTargets;
	}

	public void setBuGoalTargets(List<BuGoalTarget> buGoalTargets) {
		this.buGoalTargets = buGoalTargets;
	}

	public List<BuGoalWork> getBuGoalWorks() {
		return buGoalWorks;
	}

	public void setBuGoalWorks(List<BuGoalWork> buGoalWorks) {
		this.buGoalWorks = buGoalWorks;
	}
}