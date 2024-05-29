package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationContainer entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationContainer implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7416617429532282421L;
	private Long itemId;
	private CmDeclarationHead cmDeclarationHead;
	private String t4;
	private String declNo;
	private String contrNo;
	private String contrType;
	private String transMode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Long indexNo;

	// Constructors

	/** default constructor */
	public CmDeclarationContainer() {
	}

	/** minimal constructor */
	public CmDeclarationContainer(Long itemId) {
		this.itemId = itemId;
	}

	

	// Property accessors

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public CmDeclarationHead getCmDeclarationHead() {
		return this.cmDeclarationHead;
	}

	public void setCmDeclarationHead(CmDeclarationHead cmDeclarationHead) {
		this.cmDeclarationHead = cmDeclarationHead;
	}

	public String getT4() {
		return this.t4;
	}

	public void setT4(String t4) {
		this.t4 = t4;
	}

	public String getDeclNo() {
		return this.declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public String getContrNo() {
		return this.contrNo;
	}

	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}

	public String getContrType() {
		return this.contrType;
	}

	public void setContrType(String contrType) {
		this.contrType = contrType;
	}

	public String getTransMode() {
		return this.transMode;
	}

	public void setTransMode(String transMode) {
		this.transMode = transMode;
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

	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

}