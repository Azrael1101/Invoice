package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * PoBudgetHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PoBudgetHead implements java.io.Serializable {

	// Fields

	private Long headId;
	private String brandCode;
	private String budgetYear;
	private String description;
	private String budgetCheckType;
	private Double totalBudget;
	private Double totalAppliedBudget;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List poBudgetLines = new ArrayList();

	// Constructors

	/** default constructor */
	public PoBudgetHead() {
	}

	/** minimal constructor */
	public PoBudgetHead(Long headId) {
		this.headId = headId;
	}

	/** full constructor */
	public PoBudgetHead(Long headId, String brandCode, String budgetYear,
			String description, String budgetCheckType, Double totalBudget,
			Double totalAppliedBudget, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List poBudgetLines) {
		this.headId = headId;
		this.brandCode = brandCode;
		this.budgetYear = budgetYear;
		this.description = description;
		this.budgetCheckType = budgetCheckType;
		this.totalBudget = totalBudget;
		this.totalAppliedBudget = totalAppliedBudget;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.poBudgetLines = poBudgetLines;
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getBrandCode() {
		return this.brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBudgetYear() {
		return this.budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBudgetCheckType() {
		return this.budgetCheckType;
	}

	public void setBudgetCheckType(String budgetCheckType) {
		this.budgetCheckType = budgetCheckType;
	}

	public Double getTotalBudget() {
		return this.totalBudget;
	}

	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Double getTotalAppliedBudget() {
		return this.totalAppliedBudget;
	}

	public void setTotalAppliedBudget(Double totalAppliedBudget) {
		this.totalAppliedBudget = totalAppliedBudget;
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

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List getPoBudgetLines() {
		return this.poBudgetLines;
	}

	public void setPoBudgetLines(List poBudgetLines) {
		this.poBudgetLines = poBudgetLines;
	}

}