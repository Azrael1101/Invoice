package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuEmployeeAward entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuEmployeeAwardRank implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5755091482776685473L;
	// Fields

	private String employeeDepartment;
	private Double rankA;
	private Double rankB;
	private Double rankC;
	private Double rankD;
	private Double rankE;
	private Double rankF;
	private Double rankG;
	private Double rankH;
	private Double rankI;
	private Double rankJ;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;

	// Constructors
	/** default constructor */
	public BuEmployeeAwardRank() {
	}
	
	public String getEmployeeDepartment() {
		return employeeDepartment;
	}
	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}
	public Double getRankA() {
		return rankA;
	}
	public void setRankA(Double rankA) {
		this.rankA = rankA;
	}
	public Double getRankB() {
		return rankB;
	}
	public void setRankB(Double rankB) {
		this.rankB = rankB;
	}
	public Double getRankC() {
		return rankC;
	}
	public void setRankC(Double rankC) {
		this.rankC = rankC;
	}
	public Double getRankD() {
		return rankD;
	}
	public void setRankD(Double rankD) {
		this.rankD = rankD;
	}
	public Double getRankE() {
		return rankE;
	}
	public void setRankE(Double rankE) {
		this.rankE = rankE;
	}
	public Double getRankF() {
		return rankF;
	}
	public void setRankF(Double rankF) {
		this.rankF = rankF;
	}
	public Double getRankG() {
		return rankG;
	}
	public void setRankG(Double rankG) {
		this.rankG = rankG;
	}
	public Double getRankH() {
		return rankH;
	}
	public void setRankH(Double rankH) {
		this.rankH = rankH;
	}
	public Double getRankI() {
		return rankI;
	}
	public void setRankI(Double rankI) {
		this.rankI = rankI;
	}
	public Double getRankJ() {
		return rankJ;
	}
	public void setRankJ(Double rankJ) {
		this.rankJ = rankJ;
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
}