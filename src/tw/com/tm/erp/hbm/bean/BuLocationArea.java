package tw.com.tm.erp.hbm.bean;

/**
 * BuLocationArea entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuLocationArea implements java.io.Serializable {

	// Fields


	private static final long serialVersionUID = 3161908794134679974L;
	private Long locationId;
	private String zipCode;
	private String city;
	private String area;
	private String englishArea;

	// Constructors



	/** default constructor */
	public BuLocationArea() {
	}

	/** minimal constructor */
	public BuLocationArea(Long locationId) {
		this.locationId = locationId;
	}

	/** full constructor */

	public BuLocationArea(Long locationId, String zipCode, String city,
			String area, String englishArea) {
		this.locationId = locationId;
		this.zipCode = zipCode;
		this.city = city;
		this.area = area;
		this.englishArea = englishArea;
	}
	
	// Property accessors

	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getEnglishArea() {
		return this.englishArea;
	}

	public void setEnglishArea(String englishArea) {
		this.englishArea = englishArea;
	}

}