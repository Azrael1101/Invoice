package tw.com.tm.erp.hbm.bean;

/**
 * ImMovementHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CustomsProcessResponse implements java.io.Serializable {
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919347422114464738L;
	private Long headId;
	private String code;
	private String description;
	private String isUpload;
	
	// Constructors

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	/** default constructor */
	public CustomsProcessResponse() {
	}

	/** minimal constructor */
	public CustomsProcessResponse(Long headId) {
		this.headId = headId;
	}

	public CustomsProcessResponse(Long headId,String code, String description,String isUpload ) {
		super();
		this.headId = headId;
		this.code = code;
		this.description = description;
		this.isUpload = isUpload;
	}

	/** full constructor */

	// Property accessors
	public Long getHeadId() {
		return this.headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}
}