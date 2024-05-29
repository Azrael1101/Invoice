package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImMovementFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImMovementFile implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4435603103698280808L;
	private Long lineId;
	private ImMovementHead imMovementHead;
	private Long fileId;
	private String fileName;
	private String content;
	private String contentType;
	private Long contentSize;
	private String description;
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
	public ImMovementFile() {
	}

	/** minimal constructor */
	public ImMovementFile(Long lineId) {
		this.lineId = lineId;
	}

	/** full constructor */
	public ImMovementFile(Long lineId, ImMovementHead imMovementHead,
			Long fileId, String fileName, String content, String contentType,
			Long contentSize, String description, String reserve1,
			String reserve2, String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, Long indexNo) {
		this.lineId = lineId;
		this.imMovementHead = imMovementHead;
		this.fileId = fileId;
		this.fileName = fileName;
		this.content = content;
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
	}

	// Property accessors

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public ImMovementHead getImMovementHead() {
		return this.imMovementHead;
	}

	public void setImMovementHead(ImMovementHead imMovementHead) {
		this.imMovementHead = imMovementHead;
	}

	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

}