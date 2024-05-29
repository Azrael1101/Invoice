package tw.com.tm.erp.hbm.bean;



/**
 * ImMovementViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */  

public class ImMovementViewId implements java.io.Serializable {

	private static final long serialVersionUID = 7110936185841803525L;

	private Long headId;
	private String itemCode;


	public Long getHeadId() {
		return headId;
	}
	
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ImMovementViewId))
			return false;
		ImMovementViewId castOther = (ImMovementViewId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getHeadId() == castOther.getHeadId()) || (this
						.getHeadId() != null
						&& castOther.getHeadId() != null && this
						.getHeadId().equals(castOther.getHeadId())));

	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getHeadId() == null ? 0 : this.getHeadId().hashCode());

		return result;
	}


	

}