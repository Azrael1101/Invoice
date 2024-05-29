package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCurrency entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuCurrency implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5755882819719223238L;

	private String currencyCode;

	private String currencyCName;

	private String currencyEName;

	private String description;

	private String enable;

	private String createdBy;

	private Date creationDate;

	private String lastUpdatedBy;

	private Date lastUpdateDate;
	
	private String exChangeCode;
	
	private String orders;

	private String otherCurrency;
	
	private Long exChangeRate;
	
	private String NTD;
    private String USD;
    private String JPY;
    private String CNY;
    
	// Constructors

	public String getNTD() {
		return NTD;
	}

	public void setNTD(String nTD) {
		NTD = nTD;
	}

	public String getUSD() {
		return USD;
	}

	public void setUSD(String uSD) {
		USD = uSD;
	}

	public String getJPY() {
		return JPY;
	}

	public void setJPY(String jPY) {
		JPY = jPY;
	}

	public String getCNY() {
		return CNY;
	}

	public void setCNY(String cNY) {
		CNY = cNY;
	}

	public Long getExChangeRate() {
		return exChangeRate;
	}

	public void setExChangeRate(Long exChangeRate) {
		this.exChangeRate = exChangeRate;
	}

	/** default constructor */
	public BuCurrency() {
	}

	/** minimal constructor */
	public BuCurrency(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/** full constructor */
	public BuCurrency(String currencyCode, String currencyCName,
			String currencyEName, String description, String enable,
			String createdBy, Date creationDate, String lastUpdatedBy,
			Date lastUpdateDate,String exChangeCode,String orders) {
		this.currencyCode = currencyCode;
		this.currencyCName = currencyCName;
		this.currencyEName = currencyEName;
		this.description = description;
		this.enable = enable;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdateDate = lastUpdateDate;
		this.exChangeCode = exChangeCode;
		this.orders = orders;
		this.otherCurrency = otherCurrency;
	}

	// Property accessors

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCName() {
		return this.currencyCName;
	}

	public void setCurrencyCName(String currencyCName) {
		this.currencyCName = currencyCName;
	}

	public String getCurrencyEName() {
		return this.currencyEName;
	}

	public void setCurrencyEName(String currencyEName) {
		this.currencyEName = currencyEName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public String getExChangeCode() {
		return exChangeCode;
	}

	public void setExChangeCode(String exChangeCode) {
		this.exChangeCode = exChangeCode;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public String getOtherCurrency() {
	    return otherCurrency;
	}

	public void setOtherCurrency(String otherCurrency) {
	    this.otherCurrency = otherCurrency;
	}
	
}