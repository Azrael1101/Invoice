package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmBlockDeclarationHead implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2829743124578135910L;
	private Long headId;
	private String orderTypeCode;
	private String orderNo;
	private String status;
	private String statusName;
	private String createdBy;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<CmBlockDeclarationItem> cmBlockDeclarationItems = new ArrayList();

	/**
	 * @return the cmBlockDeclarationItems
	 */
	public List<CmBlockDeclarationItem> getCmBlockDeclarationItems() {
		return cmBlockDeclarationItems;
	}

	/**
	 * @param cmBlockDeclarationItems the cmBlockDeclarationItems to set
	 */
	public void setCmBlockDeclarationItems(List<CmBlockDeclarationItem> cmBlockDeclarationItems) {
		this.cmBlockDeclarationItems = cmBlockDeclarationItems;
	}

	/**
	 * @return the headId
	 */
	public Long getHeadId() {
		return headId;
	}

	/**
	 * @param headId
	 *            the headId to set
	 */
	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	/**
	 * @return the orderTypeCode
	 */
	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	/**
	 * @param orderTypeCode
	 *            the orderTypeCode to set
	 */
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy
	 *            the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate
	 *            the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * @param headId
	 * @param orderTypeCode
	 * @param orderNo
	 * @param status
	 * @param createdBy
	 * @param creationDate
	 * @param lastUpdatedBy
	 * @param lastUpdateDate
	 */
	public CmBlockDeclarationHead(Long headId, String orderTypeCode, String orderNo, String status, String createdBy,
			Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
		super();
		this.headId = headId;
		this.orderTypeCode = orderTypeCode;
		this.orderNo = orderNo;
		this.status = status;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 *
	 */
	public CmBlockDeclarationHead() {
	}

	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
