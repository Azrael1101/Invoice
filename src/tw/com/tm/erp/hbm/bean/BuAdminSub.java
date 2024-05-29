package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuCountry entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuAdminSub implements java.io.Serializable {


	private Long headId;
	private String countryCode;
	private String city;
	private String zipCode;
	private String area;
	private String enable;


	// Constructors

	/** default constructor */
	public BuAdminSub() {
	}

	/** minimal constructor */
	public BuAdminSub(Long headId) {
		this.headId = headId;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}


}