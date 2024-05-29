package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SiGroupMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroupMenu implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -4963494596263023286L;
	private SiGroupMenuId id;
	private SiGroup siGroup;
	private String functionCode;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	private Long indexNo;
	private String enable;
	private List siGroupObjects = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SiGroupMenu() {
	}

	/** minimal constructor */
	public SiGroupMenu(SiGroupMenuId id, SiGroup siGroup) {
		this.id = id;
		this.siGroup = siGroup;
	}

	/** full constructor */
	public SiGroupMenu(SiGroupMenuId id, SiGroup siGroup, String functionCode,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String createdBy, Date creationDate,
			String updatedBy, Date updateDate, Long indexNo, List siGroupObjects) {
		this.id = id;
		this.siGroup = siGroup;
		this.functionCode = functionCode;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
		this.indexNo = indexNo;
		this.setSiGroupObjects(siGroupObjects);
	}

	// Property accessors

	public SiGroupMenuId getId() {
		return this.id;
	}

	public void setId(SiGroupMenuId id) {
		this.id = id;
	}

	public SiGroup getSiGroup() {
		return this.siGroup;
	}

	public void setSiGroup(SiGroup siGroup) {
		this.siGroup = siGroup;
	}

	public String getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
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

	public void setSiGroupObjects(List siGroupObjects) {
		this.siGroupObjects = siGroupObjects;
	}

	public List getSiGroupObjects() {
		return siGroupObjects;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
}