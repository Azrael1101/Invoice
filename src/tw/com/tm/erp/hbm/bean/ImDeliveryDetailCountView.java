package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * ImItemOnHandView entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class ImDeliveryDetailCountView implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5722210655309197538L;
	private ImDeliveryDetailCountViewId id;
	private Date shipDate;
	//private String itemCode;
	//private String warehouseCode;
	//private String shipDay;
	//private String shipMonth;
	//private String shipYear;
	private Long totalD;
	private Long totalM;
	private Long totalY;
	

	// Constructors

	/** default constructor */
	public ImDeliveryDetailCountView() {
	}

	/** full constructor */
	public ImDeliveryDetailCountView(ImDeliveryDetailCountViewId id) {
		this.id = id;
	}

	public void setId(ImDeliveryDetailCountViewId id) {
		this.id = id;
	}
	
	public ImDeliveryDetailCountViewId getId() {
		return this.id;
	}
	
	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}
	
	public Date getShipDate() {
		return this.shipDate;
	}
/*	
	public void setShipDay(String shipDay) {
		this.shipDay = shipDay;
	}

	public String getShipDay() {
		return this.shipDay;
	}
	
	public void setShipMonth(String shipMonth) {
		this.shipMonth = shipMonth;
	}

	public String getShipMonth() {
		return this.shipMonth;
	}
	
	public void setShipYear(String shipYear) {
		this.shipYear = shipYear;
	}

	public String getShipYear() {
		return this.shipYear;
	}*/

	public Long getTotalD() {
		return totalD;
	}

	public void setTotalD(Long totalD) {
		this.totalD = totalD;
	}

	public Long getTotalM() {
		return totalM;
	}

	public void setTotalM(Long totalM) {
		this.totalM = totalM;
	}

	public Long getTotalY() {
		return totalY;
	}

	public void setTotalY(Long totalY) {
		this.totalY = totalY;
	}
	

	
}