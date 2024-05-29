package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BuPosCommonPhraseHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCommonPhraseHead implements java.io.Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	/**
	 * 
	 */
	// static final long serialVersionUID = 605286084395209497 8L;
	private String headCode;
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
	private List<BuCommonPhraseLine> buCommonPhraseLines = new ArrayList(0);

	// Constructors

	/** default constructor */
	public BuCommonPhraseHead() {
	}

	/** minimal constructor */
	public BuCommonPhraseHead(String headCode) {
		this.headCode = headCode;
	}

	/** full constructor */
	public BuCommonPhraseHead(String headCode, String name, String description,
			String type, String enable, String reserve1, String reserve2,
			String reserve3, String reserve4, String reserve5,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate, List buCommonPhraseLines) {
		this.headCode = headCode;
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
		this.buCommonPhraseLines = buCommonPhraseLines;
	}

	// Property accessors

	public String getHeadCode() {
		return this.headCode;
	}

	public void setHeadCode(String headCode) {
		this.headCode = headCode;
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

	public List<BuCommonPhraseLine> getBuCommonPhraseLines() {
		return this.buCommonPhraseLines;
	}

	public void setBuCommonPhraseLines(
			List<BuCommonPhraseLine> buCommonPhraseLines) {
		this.buCommonPhraseLines = buCommonPhraseLines;
	}

}