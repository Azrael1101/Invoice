package tw.com.tm.erp.hbm.bean;

/**
 * BuCommonPhraseLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCommonPhraseLineId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5319916023508537289L;
	private BuCommonPhraseHead buCommonPhraseHead;
	private String headCode;
	public String getHeadCode() {
		return headCode;
	}

	public void setHeadCode(String headCode) {
		this.headCode = headCode;
	}

	private String lineCode;

	// Constructors

	/** default constructor */
	public BuCommonPhraseLineId() {
	}

	/** full constructor */
	public BuCommonPhraseLineId(BuCommonPhraseHead buCommonPhraseHead,
			String lineCode) {
		this.buCommonPhraseHead = buCommonPhraseHead;
		this.lineCode = lineCode;
	}

	// Property accessors

	public BuCommonPhraseHead getBuCommonPhraseHead() {
		return this.buCommonPhraseHead;
	}

	public void setBuCommonPhraseHead(BuCommonPhraseHead buCommonPhraseHead) {
		this.buCommonPhraseHead = buCommonPhraseHead;
	}

	public String getLineCode() {
		return this.lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BuCommonPhraseLineId))
			return false;
		BuCommonPhraseLineId castOther = (BuCommonPhraseLineId) other;

		return ((this.getBuCommonPhraseHead() == castOther
				.getBuCommonPhraseHead()) || (this.getBuCommonPhraseHead() != null
				&& castOther.getBuCommonPhraseHead() != null && this
				.getBuCommonPhraseHead().equals(
						castOther.getBuCommonPhraseHead())))
				&& ((this.getLineCode() == castOther.getLineCode()) || (this
						.getLineCode() != null
						&& castOther.getLineCode() != null && this
						.getLineCode().equals(castOther.getLineCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getBuCommonPhraseHead() == null ? 0 : this
						.getBuCommonPhraseHead().hashCode());
		result = 37 * result
				+ (getLineCode() == null ? 0 : this.getLineCode().hashCode());
		return result;
	}

}