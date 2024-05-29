package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosExcessivePromotionId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosExcessivePromotion implements java.io.Serializable {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 7446196205052754232L;
    private String dataId;
    private Long headId;
    private String action;
    private String itemCode;
    private Double promotionUnitPrice;
    private Date beginDate;

    // Constructors

    /** default constructor */
    public PosExcessivePromotion() {
    }

    /** full constructor */
    public PosExcessivePromotion(String dataId, Long headId, String action,
	    String itemCode, Double promotionUnitPrice, Date beginDate) {
	this.dataId = dataId;
	this.headId = headId;
	this.action = action;
	this.itemCode = itemCode;
	this.promotionUnitPrice = promotionUnitPrice;
	this.beginDate = beginDate;
    }

    // Property accessors

    public String getDataId() {
	return this.dataId;
    }

    public void setDataId(String dataId) {
	this.dataId = dataId;
    }

    public Long getHeadId() {
	return this.headId;
    }

    public void setHeadId(Long headId) {
	this.headId = headId;
    }

    public String getAction() {
	return this.action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getItemCode() {
	return this.itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public Double getPromotionUnitPrice() {
	return this.promotionUnitPrice;
    }

    public void setPromotionUnitPrice(Double promotionUnitPrice) {
	this.promotionUnitPrice = promotionUnitPrice;
    }

    public Date getBeginDate() {
	return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

}