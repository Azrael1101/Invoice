/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Apr 11, 2008
 */
package tw.com.tm.erp.utils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.BuBranch;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrganization;
import tw.com.tm.erp.hbm.dao.BuBranchDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.DAOFactory;

/**
 * @author Dumars.Tsai
 */
public class UserUtils {

	private static String CREATED_BY = "createdBy";
	private static String CREATION_DATE = "creationDate";
	private static String LAST_UPDATED_BY = "lastUpdatedBy";
	private static String LAST_UPDATE_DATE = "lastUpdateDate";

	/**
	 * 將所要更新日期及人員的Collection以及登入者名稱丟入,即可更新Bean裡面的值
	 * 
	 * @param col
	 *            is a Collection, like java.util.List, java.util.Set..etc
	 * @param loginName
	 *            is is the login username, ex. jeff
	 */
	public static void setUserAndDate(Collection col, String username) {
	    if(username.indexOf("@") != -1){
	        for (Object obj : col) {
		    setUserAndDate(obj, username);
		}
	    }else{
		for (Object obj : col) {
		    setOpUserAndDate(obj, username);
		}		
	    }
	}

	/**
	 * 將所要更新日期及人員的JavaBean以及登入者名稱丟入,即可更新Bean裡面的值
	 * 
	 * @param Object
	 *            is your javaBean, ex. BuCurrency
	 * @param String
	 *            is the login username, ex. jeff
	 */
	public static void setUserAndDate(Object obj, String loginName) {
		String code = getEmployeeCodeByLoginName(loginName);
		try {
			Map<String, Object> map = BeanUtils.describe(obj);
			if (map.containsKey(LAST_UPDATED_BY)
					&& map.containsKey(LAST_UPDATE_DATE)) {
				if (map.containsKey(CREATED_BY) && map.get(CREATED_BY) == null) {
					BeanUtils.setProperty(obj, CREATED_BY, code);
				}
				if (map.containsKey(CREATION_DATE)
						&& map.get(CREATION_DATE) == null) {
					BeanUtils.setProperty(obj, CREATION_DATE, new Date());
				}

				BeanUtils.setProperty(obj, LAST_UPDATED_BY, code);
				BeanUtils.setProperty(obj, LAST_UPDATE_DATE, new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 將所要更新日期及人員的JavaBean以及登入者名稱丟入,即可更新Bean裡面的值
	 * 
	 * @param Object
	 *            is your javaBean, ex. BuCurrency
	 * @param String
	 *            is the login username, ex. jeff
	 */
	public static void setOpUserAndDate(Object obj, String opUser) {
		try {
			Map<String, Object> map = BeanUtils.describe(obj);
			if (map.containsKey(LAST_UPDATED_BY)
					&& map.containsKey(LAST_UPDATE_DATE)) {
				if (map.containsKey(CREATED_BY) && map.get(CREATED_BY) == null) {
					BeanUtils.setProperty(obj, CREATED_BY, opUser);
				}
				if (map.containsKey(CREATION_DATE)
						&& map.get(CREATION_DATE) == null) {
					BeanUtils.setProperty(obj, CREATION_DATE, new Date());
				}

				BeanUtils.setProperty(obj, LAST_UPDATED_BY, opUser);
				BeanUtils.setProperty(obj, LAST_UPDATE_DATE, new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 由登入名稱來取得EmployeeId
	 * 
	 * @param username
	 * @return employeeId
	 */
	public static String getEmployeeCodeByLoginName(String loginName) {
		BuEmployee e = getEmployeeByLoginName(loginName);
		if (e == null) {
			return "";
		} else {
			return e.getEmployeeCode();
		}
	}

	public static BuEmployee getEmployeeByLoginName(String loginName) {
		if (loginName == null) {
			return null;
		} else {
			List list = getBuEmployeeDAO().findByProperty(
					BuEmployeeDAO.LOGIN_NAME, loginName);
			// System.out.println(" getBuEmployeeDAO List is " + list.size() );
			if ((null != list) && (list.size() > 0)) {
				return (BuEmployee) list.get(0);
			}
			return null;
		}
	}

	/**
	 * 由工號來取得LoginName
	 * 
	 * @param EmployeeCode
	 * @return LoginName
	 */
	public static String getLoginNameByEmployeeCode(String employeeCode) {
		if (employeeCode == null) {
			return "unknow";
		} else {
			BuEmployee e = getBuEmployeeDAO().findById(employeeCode);
			if (e == null) {
				return "unknow";
			} else {
				return e.getLoginName();
			}
		}
	}

	/**
	 * 由EmployeeId來取得登入名稱
	 * 
	 * @param employeeId
	 * @return username
	 */
	public static String getUsernameByEmployeeCode(String employeeCode) {
		if (employeeCode == null) {
			return "unknow";
		} else {

			BuEmployeeWithAddressView employee = getBuEmployeeWithAddressView(employeeCode);
			if (employee == null) {
				return "unknow";
			} else {
				return employee.getChineseName();
			}
		}
	}

	/**
	 * 由EmployeeId來取得登入名稱
	 * 
	 * @param employeeId
	 * @return username
	 */
	public static BuEmployeeWithAddressView getUserByEmployeeCode(String employeeCode) {
		if (employeeCode == null) {
			return null;
		} else {
			BuEmployeeWithAddressView employee = getBuEmployeeWithAddressView(employeeCode);
			return employee;
		}
	}
	
	/**
	 * 由Login Name來取得登入名稱
	 * 
	 * @param loginName
	 * @return username 
	 */
	public static String getUsernameByLoginName(String loginName) {
		if (loginName == null) {
			return "unknow";
		} else {
			String employeeCode = getEmployeeCodeByLoginName(loginName);
			if (employeeCode != null) {
				BuEmployeeWithAddressView employee = getBuEmployeeWithAddressView(employeeCode);
				if (employee == null) {
					return "unknow";
				} else {
					return employee.getChineseName();
				}
			} else {
				return "unknow";
			}

		}
	}

	private static BuEmployeeDAO getBuEmployeeDAO() {
		return DAOFactory.getInstance().getBuEmployeeDAO();
	}

	private static BuEmployeeWithAddressViewDAO getBuEmployeeWithAddressViewDAO() {
		return DAOFactory.getInstance().getBuEmployeeWithAddressViewDAO();
	}

	/*
	 * private static BuEmployee getBuEmployee(String brandCode) { return
	 * getBuEmployeeDAO().findById(brandCode); }
	 */

	private static BuEmployeeWithAddressView getBuEmployeeWithAddressView(
			String employeeCode) {
		return getBuEmployeeWithAddressViewDAO().findById(employeeCode);
	}

	/**
	 * 取得登入人員的 BuOrganization Object
	 * 
	 * @param username
	 *            is login username
	 * @return BuOrganization
	 */
	public static BuOrganization getOrganizationByLoginName(String loginName) {
		if (StringUtils.hasText(loginName)) {
			BuEmployee e = getEmployeeByLoginName(loginName);
			if (null != e) {
				BuBrandDAO buBrandDAO = DAOFactory.getInstance().getBuBrandDAO();
				BuBrand brand = buBrandDAO.findById(e.getBrandCode());
				if(null != brand){
					BuBranchDAO buBranchDAO = DAOFactory.getInstance().getBuBranchDAO();
					BuBranch buBranch = buBranchDAO.findById(brand.getBranchCode());
					if(null != buBranch)
						return buBranch.getBuOrganization();
				}
			}
		}
		return null;
	}

	/**
	 * 取得登入人員的 Organization Code
	 * 
	 * @param username
	 *            is login username
	 * @return OrganizationCode
	 */
	public static String getOrganizationCodeByLoginName(String loginName) {
		BuOrganization buOrganization = getOrganizationByLoginName(loginName);
		if (buOrganization == null) {
			return null;
		} else {
			return buOrganization.getOrganizationCode();
		}
	}

	/**
	 * 由 Brand ID 來取得 Organization Object
	 * 
	 * @param brandId
	 *            is BuBrand.brandId
	 * @return BuOrganization Object
	 */
	public static BuOrganization getOrganizationByBrandCode(String brandCode) {
		if (StringUtils.hasText(brandCode)) {
			BuBrandDAO dao = DAOFactory.getInstance().getBuBrandDAO();
			BuBrand buBrand = dao.findById(brandCode);
			if (null != buBrand) {
				BuBranchDAO buBranchDAO = DAOFactory.getInstance().getBuBranchDAO();
				BuBranch buBranch = buBranchDAO.findById(buBrand.getBranchCode());
				if(null != buBranch)
					return buBranch.getBuOrganization();
			}
		}
		return null;
	}

	/**
	 * 由 Brand Code 來取得 Organization Code
	 * 
	 * @param brandId
	 *            is BuBrand.brandCode
	 * @return Organization Code
	 */
	public static String getOrganizationCodeByBrandCode(String brandCode) {
		return getOrganizationByBrandCode(brandCode).getOrganizationCode();
	}

	public static BuBrand getBrandByUsername(String loginName) {
		if (loginName == null) {
			return null;
		} else {
			BuEmployee e = getEmployeeByLoginName(loginName);
			if (e == null) {
				return null;
			} else {
				BuBrandDAO dao = DAOFactory.getInstance().getBuBrandDAO();
				BuBrand brand = dao.findById(e.getBrandCode());
				return brand;
			}
		}
	}
	

	/**
	 * 由employeeCode來取得登入名稱
	 * 
	 * @param employeeCode
	 * @return domain
	 */
	public static String getDomainByEmployeeCode(String employeeCode) {
		String domain = null;
		if (employeeCode != null) {

			BuEmployeeWithAddressView employee = getBuEmployeeWithAddressView(employeeCode);
			if (employee != null) {
				String loginName = employee.getLoginName();
				if(java.lang.reflect.Array.getLength((loginName.split("@")))>1){
					domain= loginName.split("@")[1];
				}
			}
		}
		return domain;
	}


}
