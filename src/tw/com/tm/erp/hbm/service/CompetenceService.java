package tw.com.tm.erp.hbm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import com.inet.report.formula.ad;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.AdDetail;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuItemCategoryPrivilege;
import tw.com.tm.erp.hbm.bean.BuPurchaseHead;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployeeId;
import tw.com.tm.erp.hbm.bean.SiGroupMenuCtrl;
import tw.com.tm.erp.hbm.bean.SiGroupMenuIdCtrl;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuPurchaseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class CompetenceService {

	private static final Log log = LogFactory.getLog(CompetenceService.class);
	private ImWarehouseDAO imWarehouseDAO;
	public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
		this.imWarehouseDAO = imWarehouseDAO;
	}
	
	private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;
	public void setImWarehouseEmployeeDAO(ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
		this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
	}
	private BuPurchaseHeadDAO buPurchaseHeadDAO;
	public void setBuPurchaseHeadDAO(BuPurchaseHeadDAO buPurchaseHeadDAO) {
		this.buPurchaseHeadDAO = buPurchaseHeadDAO;
	}
	private BuShopDAO buShopDAO;
	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
	private BuEmployeeBrandDAO buEmployeeBrandDAO;
	public void setBuEmployeeBrandDAO(BuEmployeeBrandDAO buEmployeeBrandDAO) {
		this.buEmployeeBrandDAO = buEmployeeBrandDAO;
	}
	private ImItemCategoryDAO imItemCategoryDAO;
	public void setImItemCategoiyDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}
	private SiGroupMenuDAO siGroupMenuDAO;
	public void setSiGroupMenuDAO(SiGroupMenuDAO siGroupMenuDAO) {
		this.siGroupMenuDAO = siGroupMenuDAO;
	}
	private SiUsersGroupDAO usersGroupDAO;
	public void setSiUsersGroupDAO(SiUsersGroupDAO usersGroupDAO) {
		this.usersGroupDAO = usersGroupDAO;
	}
	private BuEmployeeDAO buEmployeeDAO;
	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}
	
	 public Long updateCompetenceData(Long headId,String employeeCode,String role,String brandCode)throws Exception{
		BuPurchaseHead buPurchase =  buPurchaseHeadDAO.findById(headId);
		List<AdDetail> adDetials = buPurchase.getAdDetails();
		//SiUsersGroup userGroup = (SiUsersGroup)usersGroupDAO.findByProperty(brandCode,employeeCode).get(0);
		ImWarehouseEmployeeId ids = null;
		
		try{	 
			System.out.print("Tty List");
			for(int i=0;i<adDetials.size();i++){
				if(adDetials.get(i).getType().equals("Menu")){
					siGroupMenuDAO.deleteSiGroupMenu(brandCode, role);
				}else if(adDetials.get(i).getType().equals("WareHouse")){
					imWarehouseEmployeeDAO.deleteImWarehouseEmployee(brandCode, role);
				}
			}
		for (Iterator iterator = adDetials.iterator(); iterator.hasNext();) {
			AdDetail adDetail = (AdDetail) iterator.next();
			
				ImWarehouseEmployee imWarehouseEmp = new ImWarehouseEmployee();
				BuShopEmployee buShopEmp = new BuShopEmployee();
				BuShopEmployeeId buShopEmpId = new BuShopEmployeeId();
				BuEmployeeBrand buEmployeeBrand = new BuEmployeeBrand();
				BuEmployeeBrandId buEmployeeBrandId =new BuEmployeeBrandId();
				BuItemCategoryPrivilege buItemCategoryPrivilege = new BuItemCategoryPrivilege();
				Long indexNo     = 1L;
				indexNo++;
				//----warehouse
				if("WareHouse".equals(adDetail.getType())){
					log.info("儲存庫別人員1");
				
					ImWarehouse imWarehouse = imWarehouseDAO.findById(adDetail.getWarehouseCode());
					Long WarehouseId =imWarehouse.getWarehouseId();
					ids = new ImWarehouseEmployeeId(imWarehouse.getWarehouseCode(),role);
					
					imWarehouseEmp.setId(ids);
					imWarehouseEmp.setWarehouseCode(adDetail.getWarehouseCode());
					imWarehouseEmp.setEmployeeCode(adDetail.getEmployeeCode());
					imWarehouseEmp.setLastUpdateDate(new Date());
					imWarehouseEmp.setWarehouseId(WarehouseId);
					imWarehouseEmp.setIndexNo(indexNo);
					imWarehouseEmp.setCreatedBy(buPurchase.getCreatedBy());
					imWarehouseEmp.setLastUpdatedBy(buPurchase.getLastUpdatedBy());
					
					if("Y".equals(adDetail.getEnable())){
						imWarehouseEmp.setEnable("Y");
					}else{
						//imWarehouseEmp.setEnable("N");
						continue;
					}
					imWarehouseDAO.save(imWarehouseEmp);
					log.info("儲存庫別人員2");
				
				 }else if("shop".equals(adDetail.getType())){
				//----shop{
				 System.out.print("Star Shop");
				buShopEmpId.setShopCode(adDetail.getShopCode());
				buShopEmpId.setEmployeeCode(adDetail.getEmployeeCode());
				buShopEmp.setId(buShopEmpId);
				buShopEmp.setEnable(adDetail.getEnable());
				buShopEmp.setLastUpdateDate(new Date());
				buShopEmp.setIndexNo(indexNo);
				buShopEmp.setCreatedBy(buPurchase.getCreatedBy());
				buShopEmp.setLastUpdatedBy(buPurchase.getLastUpdatedBy());
				buShopEmp.setCreationDate(buPurchase.getCreationDate());
				buShopDAO.saveOrUpdate(buShopEmp);
				 System.out.print("end Shop");
				 }else if("brand".equals(adDetail.getType())){
				//----brand
				 System.out.print("Star brand");
				buEmployeeBrandId.setBrandCode(adDetail.getBrandCode());
				buEmployeeBrandId.setEmployeeCode(adDetail.getEmployeeCode());
				buEmployeeBrand.setId(buEmployeeBrandId);
				buEmployeeBrand.setLastUpdateDate(new Date());
				buEmployeeBrand.setIndexNo(indexNo);
				buEmployeeBrand.setCreatedBy(buPurchase.getCreatedBy());
				buEmployeeBrand.setCreationDate(buPurchase.getCreationDate());
				buEmployeeBrand.setLastUpdatedBy(buPurchase.getLastUpdatedBy());
				buEmployeeBrandDAO.saveOrUpdate(buEmployeeBrand);
				 System.out.print("end brand");
				 }else if("Menu".equals(adDetail.getType())){
					log.info("更新groupMenu:"+brandCode+" "+adDetail.getMenuId());
				  			SiGroupMenuCtrl siGroupMenu = new SiGroupMenuCtrl();
				  			SiGroupMenuIdCtrl siGroupMenuId = new SiGroupMenuIdCtrl(brandCode, role, adDetail.getMenuId());
				  			siGroupMenu.setId(siGroupMenuId);
				  			siGroupMenu.setEnable(adDetail.getEnable()==null?"N":adDetail.getEnable());
				  			siGroupMenu.setIndexNo(indexNo);
				  			siGroupMenu.setCreatedBy(employeeCode);
				  			siGroupMenu.setCreationDate(new Date());
				  			siGroupMenu.setUpdatedBy(employeeCode);
				  			siGroupMenu.setUpdateDate(new Date());
				  			siGroupMenuDAO.save(siGroupMenu);
				  			
				 }else if("category".equals(adDetail.getType())){
					 //----ItemCategory
					 System.out.print("Star Category");
					 String categoryType = "ITEM_CATEGORY";
				//	 String brandCode = adDetail.getBrandCode();
					 String enable = "Y";
					 ImItemCategory imItemCategory = (ImItemCategory) imItemCategoryDAO.findCategoryByBrandCode(adDetail.getBrandCode(),categoryType,enable);
					// String itemCategory = imItemCategory.getCategoryCode();
					 buItemCategoryPrivilege.setCategoryType(adDetail.getCategoryCode());
					// buItemCategoryPrivilege.setItemCategory(itemCategory);
					 buItemCategoryPrivilege.setEmployeePosition("V");
					 buItemCategoryPrivilege.setEmployeeCode(adDetail.getEmployeeCode());
					 buItemCategoryPrivilege.setEmployeeDepartment(buPurchase.getDepartment());
					 System.out.print("end Category");
				 }
				
	//	AdDetail adDetial = adDetialDAO.findById(buPurchase.getHeadId());
		//BuPurchaseHead adTask = buPurchaseHeadDAO.findById(headId);
		//adTask.getAdDetail();
		 
		}}catch(Exception ex){
		 log.error("updateCompetenceData : 權限存檔時發生錯誤，原因：" + ex.toString());
 		throw new Exception("updateCompetenceData : 權限存檔時發生錯誤，原因：" + ex.getMessage());
	 	}
		return 1L;
	 }
	
}
