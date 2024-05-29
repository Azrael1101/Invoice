package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class BuGoalCommission implements java.io.Serializable {	
	private static final long serialVersionUID = -4058943519513088436L;
	private Long typeId;//
	private String commissionType;
	private String category02;
	private Double commissionRate;
	private String lastUpdatedBy;
	private String lastUpdatedByName;
	private Date lastUpdateDate;


	// Constructors

	/** default constructor */

	public BuGoalCommission(){

	}

	/** minimal constructor */
	public BuGoalCommission(Long typeId){
		this.typeId = typeId;
	}

	/** full constructor */
	public BuGoalCommission(Long headId, String commissionType, String category02, double commissionRate, 
			String lastUpdatedBy, Date lastUpdateDate, String lastUpdatedByName){
		super();
		this.typeId = headId;
		this.commissionType= commissionType;
		this.category02 = category02;
		this.commissionRate = commissionRate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedByName = lastUpdatedByName;
	}

	// getter...
	public Long getTypeId() {
		return typeId;
	}

	public String getCommissionType() {
		return commissionType;
	}

	public String getCategory02() {
		return category02;
	}

	public Double getCommissionRate() {
		return commissionRate;
	}



	// setter...
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public void setCategory02(String category02) {
		this.category02 = category02;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}



	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}
	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


}
