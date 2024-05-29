package tw.com.tm.erp.hbm.bean;

/**
 * BuCommonPhraseLineId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPosCommonPhraseLineId implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5319916023508537289L;
	private BuPosCommonPhraseHead buPosCommonPhraseHead;
	private String sysSno; 
	public String getSysSno() {
		return sysSno;
	}

	public void setSysSno(String sysSno) {
		this.sysSno = sysSno;
	}

	private String lineCode;

	// Constructors

	/** default constructor */
	public BuPosCommonPhraseLineId() {
	}

	/** full constructor */
	public BuPosCommonPhraseLineId(BuPosCommonPhraseHead buPosCommonPhraseHead,
			String lineCode) {
		this.buPosCommonPhraseHead = buPosCommonPhraseHead;
		this.lineCode = lineCode;
	}

	// Property accessors

	public BuPosCommonPhraseHead getBuPosCommonPhraseHead() {
		return this.buPosCommonPhraseHead;
	}

	public void setBuPosCommonPhraseHead(BuPosCommonPhraseHead buPosCommonPhraseHead) {
		this.buPosCommonPhraseHead = buPosCommonPhraseHead;
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
		if (!(other instanceof BuPosCommonPhraseLineId))
			return false;
		BuPosCommonPhraseLineId castOther = (BuPosCommonPhraseLineId) other;

		return ((this.getBuPosCommonPhraseHead() == castOther
				.getBuPosCommonPhraseHead()) || (this.getBuPosCommonPhraseHead() != null
				&& castOther.getBuPosCommonPhraseHead() != null && this
				.getBuPosCommonPhraseHead().equals(
						castOther.getBuPosCommonPhraseHead())))
				&& ((this.getLineCode() == castOther.getLineCode()) || (this
						.getLineCode() != null
						&& castOther.getLineCode() != null && this
						.getLineCode().equals(castOther.getLineCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getBuPosCommonPhraseHead() == null ? 0 : this
						.getBuPosCommonPhraseHead().hashCode());
		result = 37 * result
				+ (getLineCode() == null ? 0 : this.getLineCode().hashCode());
		return result;
	}

}