package tw.com.tm.erp.hbm.bean;

/**
 * TmpAjaxSearchData entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TmpAjaxSearchData implements java.io.Serializable {

	// Fields

	private Long id;
	private String timeScope;
	private String selectionData;
	private String checked;
	private Long rowId;

	// Constructors

	/** default constructor */
	public TmpAjaxSearchData() {
	}

	/** minimal constructor */
	public TmpAjaxSearchData(Long id) {
		this.id = id;
	}

	/** full constructor */
	public TmpAjaxSearchData(Long id, String timeScope, String selectionData,
			String checked) {
		this.id = id;
		this.timeScope = timeScope;
		this.selectionData = selectionData;
		this.checked = checked;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTimeScope() {
		return this.timeScope;
	}

	public void setTimeScope(String timeScope) {
		this.timeScope = timeScope;
	}

	public String getSelectionData() {
		return this.selectionData;
	}

	public void setSelectionData(String selectionData) {
		this.selectionData = selectionData;
	}

	public String getChecked() {
		return this.checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

}