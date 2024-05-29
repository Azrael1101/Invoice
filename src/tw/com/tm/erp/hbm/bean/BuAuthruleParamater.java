package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuAuthruleParamater entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuAuthruleParamater implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -5281746498201194388L;

	private Long parameterId;

	private Long authId;

	private Long partyId;

	private Long parameterTypeId;

	private Long parameterNo;

	private String name;

	private String description;

	private String value;

	private Long createdBy;

	private Date creationDate;

	private Long lastUpdatedBy;

	private Date lastUpdateDate;

	// Constructors

	/** default constructor */
	public BuAuthruleParamater() {
	}

	/** minimal constructor */
	public BuAuthruleParamater(Long parameterId) {
		this.parameterId = parameterId;
	}

	/** full constructor */
	public BuAuthruleParamater(Long parameterId, Long authId, Long partyId,
			Long parameterTypeId, Long parameterNo, String name,
			String description, String value, Long createdBy,
			Date creationDate, Long lastUpdatedBy, Date lastUpdateDate) {
		this.parameterId = parameterId;
		this.authId = authId;
		this.partyId = partyId;
		this.parameterTypeId = parameterTypeId;
		this.parameterNo = parameterNo;
		this.name = name;
		this.description = description;
		this.value = value;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	// Property accessors

	public Long getParameterId() {
		return this.parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public Long getAuthId() {
		return this.authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public Long getPartyId() {
		return this.partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public Long getParameterTypeId() {
		return this.parameterTypeId;
	}

	public void setParameterTypeId(Long parameterTypeId) {
		this.parameterTypeId = parameterTypeId;
	}

	public Long getParameterNo() {
		return this.parameterNo;
	}

	public void setParameterNo(Long parameterNo) {
		this.parameterNo = parameterNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}