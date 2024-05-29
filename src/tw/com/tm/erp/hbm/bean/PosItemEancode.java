package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosItemDiscount entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosItemEancode implements java.io.Serializable {

	// Fields

	private Long headId;
	private String dataId;
	private String action;
	private String brandCode;
	private String itemCode;
	private String eanCode;

	// Constructors

	/** default constructor */
	public PosItemEancode() {
	}

	// Property accessors
	
	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getEanCode() {
		return eanCode;
	}

	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}
}