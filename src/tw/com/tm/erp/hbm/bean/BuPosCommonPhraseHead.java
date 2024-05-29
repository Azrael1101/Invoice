package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuCommonPhraseHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuPosCommonPhraseHead implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	// static final long serialVersionUID = 605286084395209497 8L;
	private String sysSno;
	private String name;
	private String description;
	private String enable;
	private String type;
	private String createdBy;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;
	private String reserve5;
	private Date creationDate;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<BuPosCommonPhraseLine> buPosCommonPhraseLines = new ArrayList(0);

	// Constructors

	/** default constructor */
	public BuPosCommonPhraseHead() {
	}

//	/** minimal constructor */
//	public BuPosCommonPhraseHead(String headCode) {
//		this.headCode = headCode;
//	}

	/** full constructor */
	public BuPosCommonPhraseHead(String sysSno, String name, String description,
			String type, String enable, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List buCommonPhraseLines) {
//		this.headCode = headCode;
		this.sysSno = sysSno;
		this.name = name;
		this.description = description;
		this.type =type;
		this.enable = enable;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
		this.reserve3 = reserve3;
		this.reserve4 = reserve4;
		this.reserve5 = reserve5;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.buPosCommonPhraseLines = buPosCommonPhraseLines;
	}

	// Property accessors

//	public String getHeadCode() {
//		return this.headCode;
//	}
//
//	public void setHeadCode(String headCode) {
//		this.headCode = headCode;
//	}
	public String getSysSno() {
		return sysSno;
	}

	public void setSysSno(String sysSno) {
		this.sysSno = sysSno;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	
	public String getReserve3() {
		return this.reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	
	public String getReserve4() {
		return this.reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	
	public String getReserve5() {
		return this.reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<BuPosCommonPhraseLine> getBuPosCommonPhraseLines() {
		return this.buPosCommonPhraseLines;
	}

	public void setBuPosCommonPhraseLines(
			List<BuPosCommonPhraseLine> buPosCommonPhraseLines) {
		this.buPosCommonPhraseLines = buPosCommonPhraseLines;
	}

}