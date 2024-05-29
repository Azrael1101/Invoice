package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SiGroup entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiGroup implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4189948495894228555L;
	private SiGroupId id;
	private String groupName;
	private String description;
	private String enable;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private String createdBy;
	private Date creationDate;
	private String updatedBy;
	private Date updateDate;
	private List siGroupMenus = new ArrayList(0);
	private List siUsersGroups = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SiGroup() {
	}

	/** minimal constructor */
	public SiGroup(SiGroupId id) {
		this.id = id;
	}

	/** full constructor */
	public SiGroup(SiGroupId id, String groupName, String description,
			String enable, String reserve1, String reserve2, String reserve3,
			String reserve4, String reserve5, String createdBy,
			Date creationDate, String updatedBy, Date updateDate,
			List siGroupMenus, List siUsersGroups) {
		this.id = id;
		this.groupName = groupName;
		this.description = description;
		this.enable = enable;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.updatedBy = updatedBy;
		this.updateDate = updateDate;
		this.siGroupMenus = siGroupMenus;
		this.siUsersGroups = siUsersGroups;
	}
	
	// Utils
	public String getBrandCode(){
		return id == null ? "" : id.getBrandCode();
	}
	
	public String getGroupCode(){
		return id == null ? "" : id.getGroupCode();
	}
	
	public String getPkString(){
		return id == null ? "" : id.getBrandCode() + "," + id.getGroupCode();
	}

	// Property accessors

	public SiGroupId getId() {
		return this.id;
	}

	public void setId(SiGroupId id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public List getSiGroupMenus() {
		return this.siGroupMenus;
	}

	public void setSiGroupMenus(List siGroupMenus) {
		this.siGroupMenus = siGroupMenus;
	}

	public List getsiUsersGroups() {
		return siUsersGroups;
	}

	public void setsiUsersGroups(List siUsersGroups) {
		this.siUsersGroups = siUsersGroups;
	}

}