package tw.com.tm.erp.hbm.bean;

import java.util.Date;

public class BuGoalItemDiscount implements java.io.Serializable {

	private static final long serialVersionUID = -1011628938064843390L;
	private Long discountId;
	private Double itemdiscount;
	private Double discount;
	private String lastUpdatedBy;
	private String lastUpdatedByName;
	private Date lastUpdateDate;
	
	public BuGoalItemDiscount(){

	}

	/** minimal constructor */
	public BuGoalItemDiscount(Long discountId){
		this.discountId = discountId;
	}

	public BuGoalItemDiscount(Long discountId, Double itemdiscount, Double discount,
			String lastUpdatedBy, Date lastUpdateDate, String lastUpdatedByName){
		super();
		this.discountId = discountId;
		this.itemdiscount= itemdiscount;
		this.discount = discount;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedByName = lastUpdatedByName;
	}

	// getter...
	public Long getDiscountId() {
		return discountId;
	}

	public Double getItemdiscount() {
		return itemdiscount;
	}

	public Double getDiscount() {
		return discount;
	}



	// setter...
	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public void setItemdiscount(Double itemdiscount) {
		this.itemdiscount = itemdiscount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	
	//
	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}
	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	
}
