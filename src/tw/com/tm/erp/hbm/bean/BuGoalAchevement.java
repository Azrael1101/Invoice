package tw.com.tm.erp.hbm.bean;

import java.util.Date;


/**
 * BuLocation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuGoalAchevement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3704216153705219180L;
	// Fieprivate Double achevement;
	
	private Long achevement_Id;
	private Double achevement;
	private Double discount;
	private Double bonus;
	private String enable;
	private String lastUpdatedByName;	// 暫時欄位 更新人員
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String createdBy;
	private Date creationDate;


	// Constructors

	/** default constructor */
	public BuGoalAchevement() {
	}

	/** minimal constructor */
	public BuGoalAchevement(Long achevement_Id) {
		this.achevement_Id = achevement_Id;
	}

	/** full constructor */
	public BuGoalAchevement( Long achevement_Id ,Double achevement ,Double discount,Double bonus ) {
		//super();
		this.achevement_Id=achevement_Id;
		this.achevement = achevement;
		this.discount = discount;
		this.bonus = bonus;
		
	}

	// Property accessors
	
	public Long getachevement_Id() {
		return this.achevement_Id;
	}

	public void setachevement_Id(Long achevement_Id) {
		this.achevement_Id = achevement_Id;
	}

	public Double getachevement() {
		return this.achevement;
	}

	public void setachevement(Double achevement) {
		this.achevement = achevement;
	}

	public Double getdiscount() {
		return this.discount;
	}

	public void setdiscount(Double discount) {
		this.discount=discount;
	}

	public Double getbonus() {
		return this.bonus;
	}

	public void setbonus(Double bonus) {
		this.bonus = bonus;
	}
	
	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}














	
}