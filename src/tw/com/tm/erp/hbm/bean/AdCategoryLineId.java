package tw.com.tm.erp.hbm.bean;

/**
 * BuCommonPhraseLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AdCategoryLineId implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8312241202038791427L;
	private AdCategoryHead adCategoryHead;
	private String lineID;

	// Constructors

	/** default constructor */
	public AdCategoryLineId() {
	}

	/** full constructor */
	public AdCategoryLineId(AdCategoryHead adCategoryHead,
			String lineID) {
		this.adCategoryHead = adCategoryHead;
		this.lineID = lineID;
	}

	// Property accessors

	public AdCategoryHead getAdCategoryHead() {
		return this.adCategoryHead;
	}

	public void setAdCategoryHead(AdCategoryHead adCategoryHead) {
		this.adCategoryHead = adCategoryHead;
	}

	public String getLineID() {
		return this.lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AdCategoryLineId))
			return false;
		AdCategoryLineId castOther = (AdCategoryLineId) other;

		return ((this.getAdCategoryHead() == castOther
				.getAdCategoryHead()) || (this.getAdCategoryHead() != null
				&& castOther.getAdCategoryHead() != null && this
				.getAdCategoryHead().equals(
						castOther.getAdCategoryHead())))
				&& ((this.getLineID() == castOther.getLineID()) || (this
						.getLineID() != null
						&& castOther.getLineID() != null && this
						.getLineID().equals(castOther.getLineID())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAdCategoryHead() == null ? 0 : this
						.getAdCategoryHead().hashCode());
		result = 37 * result
				+ (getLineID() == null ? 0 : this.getLineID().hashCode());
		return result;
	}

}