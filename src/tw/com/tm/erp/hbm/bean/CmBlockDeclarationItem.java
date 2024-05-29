package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class CmBlockDeclarationItem implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -972691635768860061L;
	private Long lineId;
	private Long headId;
	private String declarationNo;
	private Long declarationSeq;
	private String customsItemCode;
	private String customsItemName;
	private Double blockOnHandQuantity;
	private String description;
	private Long indexNo;
	private CmBlockDeclarationHead cmBlockDeclarationHead;

	private Date declDate;
	private String orderNo;
	private Double currentOnHandQty;

	/**
	 * @return the lineId
	 */
	public Long getLineId() {
		return lineId;
	}

	/**
	 * @param lineId
	 *            the lineId to set
	 */
	public void setLineId(Long lineId) {
		this.lineId = lineId;
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
	 * @return the declarationNo
	 */
	public String getDeclarationNo() {
		return declarationNo;
	}

	/**
	 * @param declarationNo
	 *            the declarationNo to set
	 */
	public void setDeclarationNo(String declarationNo) {
		this.declarationNo = declarationNo;
	}

	/**
	 * @return the declarationSeq
	 */
	public Long getDeclarationSeq() {
		return declarationSeq;
	}

	/**
	 * @param declarationSeq
	 *            the declarationSeq to set
	 */
	public void setDeclarationSeq(Long declarationSeq) {
		this.declarationSeq = declarationSeq;
	}

	/**
	 * @return the customsItemCode
	 */
	public String getCustomsItemCode() {
		return customsItemCode;
	}

	/**
	 * @param customsItemCode
	 *            the customsItemCode to set
	 */
	public void setCustomsItemCode(String customsItemCode) {
		this.customsItemCode = customsItemCode;
	}

	/**
	 * @return the blockOnHandQuantity
	 */
	public Double getBlockOnHandQuantity() {
		return blockOnHandQuantity;
	}

	/**
	 * @param blockOnHandQuantity
	 *            the blockOnHandQuantity to set
	 */
	public void setBlockOnHandQuantity(Double blockOnHandQuantity) {
		this.blockOnHandQuantity = blockOnHandQuantity;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the indexNo
	 */
	public Long getIndexNo() {
		return indexNo;
	}

	/**
	 * @param indexNo
	 *            the indexNo to set
	 */
	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	/**
	 * @return the cmBlockDeclarationHead
	 */
	public CmBlockDeclarationHead getCmBlockDeclarationHead() {
		return cmBlockDeclarationHead;
	}

	/**
	 * @param cmBlockDeclarationHead
	 *            the cmBlockDeclarationHead to set
	 */
	public void setCmBlockDeclarationHead(CmBlockDeclarationHead cmBlockDeclarationHead) {
		this.cmBlockDeclarationHead = cmBlockDeclarationHead;
	}

	/**
	 * @return the declDate
	 */
	public Date getDeclDate() {
		return declDate;
	}

	/**
	 * @param declDate
	 *            the declDate to set
	 */
	public void setDeclDate(Date declDate) {
		this.declDate = declDate;
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
	 * @return the currentOnHandQty
	 */
	public Double getCurrentOnHandQty() {
		return currentOnHandQty;
	}

	/**
	 * @param currentOnHandQty
	 *            the currentOnHandQty to set
	 */
	public void setCurrentOnHandQty(Double currentOnHandQty) {
		this.currentOnHandQty = currentOnHandQty;
	}

	/**
	 * @param lineId
	 * @param headId
	 * @param declarationNo
	 * @param declarationSeq
	 * @param customsItemCode
	 * @param blockOnHandQuantity
	 * @param description
	 */
	public CmBlockDeclarationItem(Long lineId, Long headId, String declarationNo, Long declarationSeq,
			String customsItemCode, Double blockOnHandQuantity, String description) {
		super();
		this.lineId = lineId;
		this.headId = headId;
		this.declarationNo = declarationNo;
		this.declarationSeq = declarationSeq;
		this.customsItemCode = customsItemCode;
		this.blockOnHandQuantity = blockOnHandQuantity;
		this.description = description;
	}

	/**
	 *
	 */
	public CmBlockDeclarationItem() {
	}

	/**
	 * @return the customsItemName
	 */
	public String getCustomsItemName() {
		return customsItemName;
	}

	/**
	 * @param customsItemName the customsItemName to set
	 */
	public void setCustomsItemName(String customsItemName) {
		this.customsItemName = customsItemName;
	}
	
}
