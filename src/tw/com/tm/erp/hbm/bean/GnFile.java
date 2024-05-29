package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * GnFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class GnFile implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7484468846510444410L;
	private Long headId;
	private String parentOrderType;
	private Long parentHeadId;
	private String fileName;
	private String physicalName;
	private String physicalPath;
	private String contentType;
	private Long contentSize;
	private String description;
	private String type;
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
	private String isDeleteRecord;
	private String ownerType;

	private String typeName;	// 非db欄位
	
	// Constructors

	/** default constructor */
	public GnFile() {
	}

	/** full constructor */
	public GnFile(String parentOrderType, Long parentHeadId, String fileName,
			String physicalName, String physicalPath, String contentType,
			Long contentSize, String description, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo,String isDeleteRecord , String ownerType) {
		this.parentOrderType = parentOrderType;
		this.parentHeadId = parentHeadId;
		this.fileName = fileName;
		this.physicalName = physicalName;
		this.physicalPath = physicalPath;
		this.contentType = contentType;
		this.contentSize = contentSize;
		this.description = description;
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
		this.isDeleteRecord = isDeleteRecord;
		this.ownerType = ownerType;
	}

	// Property accessors

	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getParentOrderType() {
		return this.parentOrderType;
	}

	public void setParentOrderType(String parentOrderType) {
		this.parentOrderType = parentOrderType;
	}

	public Long getParentHeadId() {
		return this.parentHeadId;
	}

	public void setParentHeadId(Long parentHeadId) {
		this.parentHeadId = parentHeadId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPhysicalName() {
		return this.physicalName;
	}

	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}

	public String getPhysicalPath() {
		return this.physicalPath;
	}

	public void setPhysicalPath(String physicalPath) {
		this.physicalPath = physicalPath;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getContentSize() {
		return this.contentSize;
	}

	public void setContentSize(Long contentSize) {
		this.contentSize = contentSize;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getIsDeleteRecord() {
		return isDeleteRecord;
	}

	public void setIsDeleteRecord(String isDeleteRecord) {
		this.isDeleteRecord = isDeleteRecord;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

	public String getTypeName() {
	    return typeName;
	}

	public void setTypeName(String typeName) {
	    this.typeName = typeName;
	}
	
	

}