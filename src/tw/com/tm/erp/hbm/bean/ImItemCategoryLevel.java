package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.List;

public class ImItemCategoryLevel implements Serializable{
	private static final long serialVersionUID = 2333890521360811212L;
	
	private Long sysSno;
	private String brandCode;
	private String categoryLevelCode;
	private String categoryLevelName;
	private String pCategoryLevelCode;

	public ImItemCategoryLevel() {
	}

	/**
	 * @param sysSno
	 * @param brandCode
	 * @param categoryLevelCode
	 * @param categoryLevelName
	 * @param pCategoryLevelCode
	 */
	public ImItemCategoryLevel(Long sysSno, String brandCode, String categoryLevelCode, String categoryLevelName,
			String pCategoryLevelCode) {
		super();
		this.sysSno = sysSno;
		this.brandCode = brandCode;
		this.categoryLevelCode = categoryLevelCode;
		this.categoryLevelName = categoryLevelName;
		this.pCategoryLevelCode = pCategoryLevelCode;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getCategoryLevelCode() {
		return categoryLevelCode;
	}

	public void setCategoryLevelCode(String categoryLevelCode) {
		this.categoryLevelCode = categoryLevelCode;
	}

	public String getCategoryLevelName() {
		return categoryLevelName;
	}

	public void setCategoryLevelName(String categoryLevelName) {
		this.categoryLevelName = categoryLevelName;
	}

	public String getpCategoryLevelCode() {
		return pCategoryLevelCode;
	}

	public void setpCategoryLevelCode(String pCategoryLevelCode) {
		this.pCategoryLevelCode = pCategoryLevelCode;
	}
	
}
