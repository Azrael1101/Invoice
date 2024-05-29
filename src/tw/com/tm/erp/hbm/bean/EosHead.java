package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EosHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EosHead implements java.io.Serializable {

		private Long   headId;
		private String brandCode;
		private String orderTypeCode;
		private String orderNo;
		private String applicant;
		private Date requestDate;
		private String status;
		private String createdBy;
		private Date   creationDate;
		private String lastUpdatedBy;
		private Date   lastUpdateDate;
		private String shopCode;
		private String warehouseCode;
		public EosHead(){
			
		}
		
		
		public Long getHeadId() {
			return headId;
		}
		public void setHeadId(Long headId) {
			this.headId = headId;
		}
		public String getBrandCode() {
			return brandCode;
		}
		public void setBrandCode(String brandCode) {
			this.brandCode = brandCode;
		}
		public String getOrderTypeCode() {
			return orderTypeCode;
		}
		public void setOrderTypeCode(String orderTypeCode) {
			this.orderTypeCode = orderTypeCode;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public String getApplicant() {
			return applicant;
		}
		public void setApplicant(String applicant) {
			this.applicant = applicant;
		}
		public Date getRequestDate() {
			return requestDate;
		}
		public void setRequestDate(Date requestDate) {
			this.requestDate = requestDate;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
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


		public String getShopCode() {
			return shopCode;
		}


		public void setShopCode(String shopCode) {
			this.shopCode = shopCode;
		}


		public String getWarehouseCode() {
			return warehouseCode;
		}


		public void setWarehouseCode(String warehouseCode) {
			this.warehouseCode = warehouseCode;
		}
		
		
		
		
		


}