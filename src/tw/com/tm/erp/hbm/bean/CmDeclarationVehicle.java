package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * CmDeclarationVehicle entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationVehicle implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4857039152587160837L;
	private Long itemId;
	private CmDeclarationHead cmDeclarationHead;
	private String t3;
	private String declNo;
	private Long itemNo;
	private String vehicleNo;
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
	public CmDeclarationVehicle() {
	}

	/** minimal constructor */
	public CmDeclarationVehicle(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */
	public CmDeclarationVehicle(Long itemId,
			CmDeclarationHead cmDeclarationHead, String t3, String declNo,
			Long itemNo, String vehicleNo, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.itemId = itemId;
		this.cmDeclarationHead = cmDeclarationHead;
		this.t3 = t3;
		this.declNo = declNo;
		this.itemNo = itemNo;
		this.vehicleNo = vehicleNo;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.indexNo = indexNo;
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

	public String getT3() {
		return this.t3;
	}

	public void setT3(String t3) {
		this.t3 = t3;
	}

	public String getDeclNo() {
		return this.declNo;
	}

	public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}

	public Long getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getVehicleNo() {
		return this.vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
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