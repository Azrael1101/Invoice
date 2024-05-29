package tw.com.tm.erp.hbm.bean;

import java.util.Date;


/**
 * BuExchangeRate entity. @author MyEclipse Persistence Tools
 */

public class BuExchangeRate  implements java.io.Serializable {


    // Fields    

     private BuExchangeRateId id;
     private Double exchangeRate;
     private String createdBy;
     private Date creationDate;
     private Date beginDate;
     private String lastUpdatedBy;
     private Date lastUpdateDate;
     private String organizationCode;
     private String sourceCurrency;
     private String againstCurrency;
     private String currencyName;
     private String currencyEName;
     private String lastUpdatedByName;
     
     /** default constructor */
 	public BuExchangeRate() {
 	}
    
    
    /** full constructor */
    public BuExchangeRate(BuExchangeRateId id, Double exchangeRate, String createdBy, Date creationDate,Date beginDate ,String lastUpdatedBy, Date lastUpdateDate,String organizationCode,String sourceCurrency,String againstCurrency) {
        this.id = id;
        this.exchangeRate = exchangeRate;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.beginDate = beginDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
        this.organizationCode = organizationCode;
        this.sourceCurrency = sourceCurrency;
        this.againstCurrency = againstCurrency;    
    }




	public Double getExchangeRate() {
		return exchangeRate;
	}




	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}




	public String getCreatedBy() {
		return createdBy;
	}




	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}




	public Date getCreationDate() {
		return creationDate;
	}




	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}




	public Date getBeginDate() {
		return beginDate;
	}




	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}




	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}




	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}




	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}




	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}




	public String getOrganizationCode() {
		return organizationCode;
	}




	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}




	public String getSourceCurrency() {
		return sourceCurrency;
	}




	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}




	public String getAgainstCurrency() {
		return againstCurrency;
	}




	public void setAgainstCurrency(String againstCurrency) {
		this.againstCurrency = againstCurrency;
	}




	public BuExchangeRateId getId() {
		return id;
	}




	public void setId(BuExchangeRateId id) {
		this.id = id;
	}


	public String getCurrencyName() {
		return currencyName;
	}


	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}


	public String getCurrencyEName() {
		return currencyEName;
	}


	public void setCurrencyEName(String currencyEName) {
		this.currencyEName = currencyEName;
	}

	public String getLastUpdatedByName() {
		return lastUpdatedByName;
	}

	public void setLastUpdatedByName(String lastUpdatedByName) {
		this.lastUpdatedByName = lastUpdatedByName;
	}
   



}