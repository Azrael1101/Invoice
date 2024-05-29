package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SiGroupObject entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroupObject implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -7881243482802698743L;
	private SiGroupObjectId id;
	private SiGroupMenu siGroupMenu;
	private String controlType;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creatationDate;
	private String updatedBy;
	private Date updateDate;
	private Long indexNo;

	// Constructors

	/** default constructor */
	public SiGroupObject() {
	}

	/** minimal constructor */
	public SiGroupObject(SiGroupObjectId id) {
		this.id = id;
	}

	/** full constructor */
	public SiGroupObject(SiGroupObjectId id, SiGroupMenu siGroupMenu,
			String controlType, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creatationDate, String updatedBy,
			Date updateDate, Long indexNo) {
		this.id = id;
		this.setSiGroupMenu(siGroupMenu);
		this.controlType = controlType;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creatationDate = creatationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
		this.indexNo = indexNo;
	}

	// Property accessors

	public SiGroupObjectId getId() {
		return this.id;
	}

	public void setId(SiGroupObjectId id) {
		this.id = id;
	}

	public void setSiGroupMenu(SiGroupMenu siGroupMenu) {
		this.siGroupMenu = siGroupMenu;
	}

	public SiGroupMenu getSiGroupMenu() {
		return siGroupMenu;
	}

	public String getControlType() {
		return this.controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
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

	public Date getCreatationDate() {
		return this.creatationDate;
	}

	public void setCreatationDate(Date creatationDate) {
		this.creatationDate = creatationDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

}