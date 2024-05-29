package tw.com.tm.erp.hbm.bean;

import tw.com.tm.erp.hbm.bean.ImMovementViewId; 

/**
 * ImMovementHead entity.   
 * 
 * @author MyEclipse Persistence Tools
 */  

public class ImMovementView implements java.io.Serializable {

	private static final long serialVersionUID = 7110936185841803525L;
	
	private ImMovementViewId imMovementViewId;
	private String brandCode;
	private String arrivalWarehouseCode;
	private String status;
	

	public ImMovementViewId getId() {
		return this.imMovementViewId;
	}
	
	public void setId(ImMovementViewId imMovementViewId) {
		this.imMovementViewId = imMovementViewId;
	}
	
	public String getBrandCode() {
		return this.brandCode;
	}
	
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getArrivalWarehouseCode() {
		return arrivalWarehouseCode;
	}

	public void setArrivalWarehouseCode(String arrivalWarehouseCode) {
		this.arrivalWarehouseCode = arrivalWarehouseCode;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}