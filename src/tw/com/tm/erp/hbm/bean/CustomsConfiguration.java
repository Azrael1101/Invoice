package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ImMovementHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CustomsConfiguration implements java.io.Serializable {

	private static final long serialVersionUID = 7110936185841803525L;
	// Fields

	private Long headId;
	private String sourceField;
	private String targetField;
	private String sourceTable;
	private String orderTypeCode;
	private String destinationFunction;
	private String targetLevel;
	private String indexNo;
	private String customsType;
	private String targetSize;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	private String reserve4;

	// Constructors
	
	/** default constructor */
	public CustomsConfiguration() {
	}

	/** minimal constructor */
	public CustomsConfiguration(Long headId) {
		this.headId = headId;
	}
	
	public String getOrderTypeCode() {
		return orderTypeCode;
	}
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}
	public String getDestinationFunction() {
		return destinationFunction;
	}
	public void setDestinationFunction(String destinationFunction) {
		this.destinationFunction = destinationFunction;
	}
	public String getTargetLevel() {
		return targetLevel;
	}
	public void setTargetLevel(String targetLevel) {
		this.targetLevel = targetLevel;
	}
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getTargetSize() {
		return targetSize;
	}
	public void setTargetSize(String targetSize) {
		this.targetSize = targetSize;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	public Long getHeadId() {
		return headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public String getSourceField() {
		return sourceField;
	}

	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getCustomsType() {
		return customsType;
	}

	public void setCustomsType(String customsType) {
		this.customsType = customsType;
	}

}