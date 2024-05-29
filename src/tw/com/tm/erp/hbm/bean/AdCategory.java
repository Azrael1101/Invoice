package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuCommonPhraseHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCategory implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7427639611983197353L;
	private Long headID;
	private String deptNo;
	private String groupNo;
	private String groupName;
	private String enable;
	private Long displaySort;
	private String orderTypeCode;
	private String classNo;
	private String className;
	private String inChargeCode;
	private Long parentHeadId;
	private Long indexNo;

	//private List<AdCategoryLine> adCategoryLines = new ArrayList(0);

	// Constructors

	/** default constructor */
	public AdCategory() {
	}

	/** minimal constructor */
	public AdCategory(Long headID) {
		this.headID = headID;
	}

	/** full constructor */
	public AdCategory(Long headID,String deptNo, String groupNo, String groupName,
			 String enable,Long displaySort, List adCategoryLines) {
		this.headID = headID;
		this.deptNo = deptNo;
		this.groupNo = groupNo;
		this.groupName = groupName;
		this.enable = enable;
		this.displaySort =displaySort;
	
		//this.adCategoryLines = adCategoryLines;
	}

	// Property accessors
	
	public Long getHeadID() {
		return this.headID;
	}

	public void setHeadID(Long headID) {
		this.headID = headID;
	}

	public String getDeptNo() {
		return this.deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getDisplaySort() {
		return this.displaySort;
	}


	
	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}
	
	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	/*public List<AdCategoryLine> getAdCategoryLines() {
		return this.adCategoryLines;
	}

	public void setAdCategoryLines(
			List<AdCategoryLine> adCategoryLines) {
		this.adCategoryLines = adCategoryLines;
	}*/
	
	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getInChargeCode() {
		return inChargeCode;
	}

	public void setInChargeCode(String inChargeCode) {
		this.inChargeCode = inChargeCode;
	}

	public Long getParentHeadId() {
		return parentHeadId;
	}

	public void setParentHeadId(Long parentHeadId) {
		this.parentHeadId = parentHeadId;
	}

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

}