package tw.com.tm.erp.hbm.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.utils.ObtainUserInfomation;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.UserUtils;

public class SystemService {
	private static final Log log = LogFactory.getLog(SystemService.class);


	/**
	 * 取得 LOGIN USER
	 * 
	 * @param httpRequest
	 * @return
	 */

	public static User getLoginUser(HttpServletRequest httpRequest, String loginId) throws Exception {
		// 20080923 shan get db brandcode
		String employeeCode = UserUtils.getEmployeeCodeByLoginName(loginId);
		BuEmployeeDAO buEmployeeDAO = (BuEmployeeDAO) SpringUtils.getApplicationContext().getBean("buEmployeeDAO");
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		return getLoginUser(httpRequest, loginId, buEmployee.getBrandCode());
	}

	public static User getLoginUser(HttpServletRequest httpRequest, String loginId, String brandCode) throws Exception {
		/**
		 * 判斷是SystemMode D)Development P)Production if D use loginId read db and
		 * write to User Object and set BrandCode = CO else if P get session
		 * user object
		 */
		User userObj = (User) httpRequest.getSession().getAttribute(SystemConfig.USER_SESSION_NAME);
		if (null == userObj) {
			//Properties Properties = new Properties();
			//String propPath = Class.forName("tw.com.tm.erp.hbm.service.SystemService").getResource("/").getPath()
					//+ "system_config.properties";
			// 讀入properties
			// Properties.load(new FileInputStream(propPath));
			// String systemmode = (String) Properties.get("SystemMode");
			// String user_id = loginId.split("@")[0];
			// if (systemmode.equals("D")) {
			userObj = ObtainUserInfomation.getUserPermission(brandCode, null, loginId);
			httpRequest.getSession().setAttribute(SystemConfig.USER_SESSION_NAME, userObj);
			// }
		}
		return userObj;
	}


}
