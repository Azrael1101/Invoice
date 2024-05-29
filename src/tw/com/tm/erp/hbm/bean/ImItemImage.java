package tw.com.tm.erp.hbm.bean;

import java.sql.Blob;
import java.util.Date;

/**
 * ImItemImage entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImItemImage implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -5388556193310051300L;
	private Long imageId;
	private ImItem imItem;
	private String imageName;
	private Blob content;
	private String contentType;
	private Long contentSize;
	private String description;
	private String enable;
	private String isDefault;
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
	public ImItemImage() {
	}

	/** minimal constructor */
	public ImItemImage(Long imageId) {
		this.imageId = imageId;
	}

	/** full constructor */
	public ImItemImage(Long imageId, ImItem imItem, String imageName,
			Blob content, String contentType, Long contentSize,
			String description, String enable, String isDefault,
			String reserve1, String reserve2, String reserve3, String reserve4,
			String reserve5, String createdBy, Date creationDate,
			String lastUpdatedBy, Date lastUpdateDate, Long indexNo) {
		this.imageId = imageId;
		this.imItem = imItem;
		this.imageName = imageName;
		this.content = content;
		this.contentType = contentType;
		this.contentSize = contentSize;
		this.description = description;
		this.enable = enable;
		this.isDefault = isDefault;
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

	public Long getImageId() {
		return this.imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public ImItem getImItem() {
		return this.imItem;
	}

	public void setImItem(ImItem imItem) {
		this.imItem = imItem;
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Blob getContent() {
		return this.content;
	}

	public void setContent(Blob content) {
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

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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