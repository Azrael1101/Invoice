package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCommonPhraseLine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCategoryLine implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7464410122030275708L;
	private AdCategoryLineId id;
	private String classNo;
	private String className;
	private String deptNo;
	private String enable;
	private String groupNo;
	private Long displaySort;
	private Long indexNo;
	private String inChargeCode;
	private String orderTypeCode;

	// Constructors

	/** default constructor */
	public AdCategoryLine() {
	}

	/** minimal constructor */
	public AdCategoryLine(AdCategoryLineId id) {
		this.id = id;
	}

	/** full constructor */
	public AdCategoryLine(AdCategoryLineId id, String classNo,
			String className,String deptNo, String enable, String groupNo, Long displaySort,Long indexNo) 
	{
		this.id = id;
		this.classNo = classNo;
		this.className = className;
		this.deptNo = deptNo;
		this.enable = enable;
		this.groupNo = groupNo;
		this.displaySort = displaySort;
		this.indexNo = indexNo;

	}

	// Property accessors

	public AdCategoryLineId getId() {
		return this.id;

	}

	public void setId(AdCategoryLineId id) {
		this.id = id;
	}

	public String getClassNo() {
		return this.classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getDeptNo() {
		return this.deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public Long getDisplaySort() {
		return this.displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}
	
	public Long getIndexNo() {
		return this.indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

	public String getInChargeCode() {
		return inChargeCode;
	}

	public void setInChargeCode(String inChargeCode) {
		this.inChargeCode = inChargeCode;
	}

	public String getOrderTypeCode() {
	    return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
	    this.orderTypeCode = orderTypeCode;
	}
	
	
}