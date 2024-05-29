package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * PosEmployee entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PosCurrency implements java.io.Serializable {

	// Fields
	private Long headId;
	private String dataId;
	private String action;
	private String currencyCode;
	private String currencyCName;
    private String currencyEName;
    private Double exchangeRate;
    private Date beginDate;
	
	// Constructors

	/** default constructor */
	public PosCurrency() {
	}

	
	// Property accessors
	
	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCName() {
		return currencyCName;
	}

	public void setCurrencyCName(String currencyCName) {
		this.currencyCName = currencyCName;
	}

	public String getCurrencyEName() {
		return currencyEName;
	}

	public void setCurrencyEName(String currencyEName) {
		this.currencyEName = currencyEName;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
}