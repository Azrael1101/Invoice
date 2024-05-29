package tw.com.tm.erp.utils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.utils.Function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import tw.com.tm.erp.hbm.dao.DAOFactory;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.service.SiMenuService;

public class ObtainUserInfomation {

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public static User getUserPermission(String brandCode, String employeeCode, String loginName) { // 權限呼叫entry;(brandCode,loginName(ex:T00000@OG00),"F","F")
	    User user = new User();
	    try{

		user.setBrandCode(brandCode);
		user.setLoginName(loginName);
		if (!StringUtils.hasText(employeeCode))
		    employeeCode = UserUtils.getEmployeeCodeByLoginName(loginName); // 由loginName取得工號
		UserProgramRight userProgramRight = getUserProgramRight(brandCode, employeeCode);
		// 取得User的Menu & Object權限
		user.setUserProgramRight(userProgramRight);
		BuEmployeeWithAddressViewDAO dao = DAOFactory.getInstance().getBuEmployeeWithAddressViewDAO(); // 取得User的其他屬性
		BuBrandDAO brandDao = DAOFactory.getInstance().getBuBrandDAO();
		BuEmployeeWithAddressView bue = dao.findById(employeeCode);
		user.setOrganizationCode(bue.getOrganizationCode());
		user.setReportLoginName(bue.getReportLoginName());
		user.setReportPassword(bue.getReportPassword());
		user.setBrandName(brandDao.findById(brandCode).getBrandName());
		user.setEmployeeCode(bue.getEmployeeCode());
		user.setIdentityCode(bue.getIdentityCode());
		user.setChineseName(bue.getChineseName());
		user.setEnglishName(bue.getEnglishName());
		user.setDepartment(bue.getDepartment());
		user.setKeyInMode("N"); //TODO 須於VIEW 中加入此欄位 S:快速 N:正常
		user.setEmployeePosition(bue.getEmployeePosition());
		user.setEMail(bue.getEMail());
	    }catch (Exception ex){
		ex.printStackTrace();
	    }
	    return user;
	}

	public static boolean isAbleToAccessFunction(String brandCode,
			String loginName, String systemType, String functionType,
			String functionCode) {// 檢視是否有權限使用該function
		String employeeCode = UserUtils.getEmployeeCodeByLoginName(loginName);
		SiMenuService siMenuService = (SiMenuService) SpringUtils
				.getApplicationContext().getBean("siMenuService");
		List menuResult = siMenuService.getBrandUserReportManager(brandCode,
				employeeCode, systemType, functionType, functionCode);
		if (menuResult.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private static UserProgramRight getUserProgramRight(String brandCode, String loginName) {
		
		// 取得User的menu&Object權限
		SiMenuService siMenuService = (SiMenuService) SpringUtils.getApplicationContext().getBean("siMenuService");
		SiMenuDAO siMenuDAO = (SiMenuDAO) SpringUtils.getApplicationContext().getBean("siMenuDAO");
		 
		//找出SiMenu中Parent為-1的Menu作為root
		List<SiMenu> menuTitle = siMenuDAO.findByProperty("SiMenu", "" , new String(" and parentMenuId = ?"), new Object[]{-1L}, " order by lineNo desc");
		
		List<JTree> menuTreeXml = new ArrayList();
		List<String> menuName = new ArrayList();
		List<String> menuUrl = new ArrayList();
		List<String> imgNameA = new ArrayList();
		List<String> imgNameB = new ArrayList();
		
		//依據不同root的SystemType找出所有屬於其SystemType下的Menu
		for (Iterator iterator = menuTitle.iterator(); iterator.hasNext();) {
			SiMenu siMenu = (SiMenu) iterator.next();
			//找跟-1所設定的meniId同樣的SystemType
			List menuResult = siMenuService.getBrandUserMenuManager(brandCode,loginName, siMenu.getSystemType(), null); // 取得menu權限List
			//下面這個不知道是做什麼的
			//SiGroupObjectService siGroupObjectService = (SiGroupObjectService) SpringUtils.getApplicationContext().getBean("siGroupObjectService");
			//HashMap rightResult = siGroupObjectService.getBrandUserObjectMapManager(brandCode, loginName); // 取得Object權限HashMap
			// 將所有的menu組成JTree
			
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new Function(setMenuName(siMenu), setMenuUrl(null, siMenu)));
			for (int i = 0; i < menuResult.size(); i++) {
				SiMenu sim = (SiMenu) menuResult.get(i);
				//如果此MENU的PARENT_MENU_ID為空值(null)
				if(null == sim.getParentMenuId())
					continue;
				DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(new Function(setMenuName(sim), setMenuUrl(siMenu, sim)));
				if (sim.getParentMenuId().equals(0L)) {
					List subTrees = addTree(sim, menuResult);
					if (subTrees != null) {
						for (int j = 0; j < subTrees.size(); j++) {
							moduleNode.add((DefaultMutableTreeNode) subTrees.get(j));
						}
					}
					if(moduleNode.getChildCount() > 0 || StringUtils.hasText(sim.getUrl()))
						rootNode.add(moduleNode);
				}
			}
			
			//如果該大圖下Menu或有URL才顯示
			if(rootNode.getChildCount() > 0 || StringUtils.hasText(siMenu.getUrl())){
				JTree menuTree = new JTree(rootNode);
				//Reserve2,3目前存圖檔名稱
				menuName.add(setMenuName(siMenu));
				menuUrl.add(setMenuUrl(null, siMenu));
				imgNameA.add(siMenu.getReserve2());
				imgNameB.add(siMenu.getReserve3());
				menuTreeXml.add(menuTree);
			}
		}
		
		UserProgramRight userProgramRight = new UserProgramRight();
		userProgramRight.setMenuTreeXml(menuTreeXml);
		userProgramRight.setMenuName(menuName);
		userProgramRight.setMenuUrl(menuUrl);
		userProgramRight.setImgNameA(imgNameA);
		userProgramRight.setImgNameB(imgNameB);
		return userProgramRight;
	}

	private static List<DefaultMutableTreeNode> addTree(SiMenu parentMenu, List menuResult) {// 加入子樹
		List<DefaultMutableTreeNode> moduleNodes = null;
		for (int i = 0; i < menuResult.size(); i++) {
			SiMenu sim = (SiMenu) menuResult.get(i);
			if (sim.getParentMenuId().equals(parentMenu.getMenuId())) {
				if (moduleNodes == null) {
					moduleNodes = new java.util.ArrayList();
				}
				DefaultMutableTreeNode subModuleNode = new DefaultMutableTreeNode(new Function(setMenuName(sim), setMenuUrl(parentMenu, sim)));
				
				List subTrees = addTree(sim, menuResult);
				if (subTrees != null) {
					for (int j = 0; j < subTrees.size(); j++) {
						subModuleNode.add((DefaultMutableTreeNode) subTrees.get(j));
					}
				}
				
				if(subModuleNode.getChildCount() > 0 || StringUtils.hasText(sim.getUrl()))
					moduleNodes.add(subModuleNode);
			}
		}
		return moduleNodes;
	}

	public static String getUserMenuString(JTree tree) {// 產生選單的html code
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		java.util.Enumeration enum1 = root.children();
		String menu = "<ul>";
		while (enum1.hasMoreElements()) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enum1.nextElement();
			menu = menu + addChildsString(childNode);// 產生子樹menu的code
		}
		return menu + "</ul>";
	}

	private static String addChildsString(DefaultMutableTreeNode parentNode) {
		String url = "";
		String menu = "";
		Function pFun = (Function) parentNode.getUserObject();
		if (pFun.getFunctionURL() != null) {
			url = ((Function) parentNode.getUserObject()).getFunctionURL();
			menu = "<li><font style='font-size:10pt;font-family:新細明體'><a href='"
					+ url
					+ "' onClick='return openwindow(this.href);'>"
					+ ((Function) parentNode.getUserObject()).getFunctionName()
					+ "</a></font>";
		} else {
			menu = "<li><font style='font-size:10pt;font-family:新細明體'>"
					+ ((Function) parentNode.getUserObject()).getFunctionName()
					+ "</font>";
		}
		java.util.Enumeration enum1 = parentNode.children();
		boolean flag = false;
		String tmpString = "";
		while (enum1.hasMoreElements()) {
			flag = true;
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enum1
					.nextElement();
			tmpString = tmpString + addChildsString(childNode);
		}
		if (flag) {
			menu = menu + "<ul>" + tmpString + "</ul>";
		} else {
			menu = menu + tmpString;
		}
		menu = menu + "</li>";
		return menu;
	}
	
	public static Vector getUserMenuStringArray(JTree tree) {// 動態產生選單的html code
		Vector menuArray = new Vector();
		String menu = "";
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		java.util.Enumeration enum1 = root.children();
		int i=0;
		int countOfRoot = 25;//預設每個按鈕第一層選項的個數
		while (enum1.hasMoreElements()) {
			if(i%countOfRoot == 0){
				menu = menu + "<ul>";
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enum1.nextElement();
				menu = menu + addChildsString(childNode);// 產生子樹menu的code
			}else if(i%countOfRoot == (countOfRoot-1)){
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enum1.nextElement();
				menu = menu + addChildsString(childNode);// 產生子樹menu的code
				menu = menu + "</ul>";
				menuArray.add(menu);
				menu = "";
			}else{
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) enum1.nextElement();
				menu = menu + addChildsString(childNode);// 產生子樹menu的code
			}
			i++;
		}
		if(i%countOfRoot != (countOfRoot-1)){
			menu = menu + "</ul>";
			menuArray.add(menu);
		}
		return menuArray;
	}
	
	/**
	 * 將SiMenu的Name重組，在後面加上owner的資訊
	 *
	 * @param SiMenu
	 */
	public static String setMenuName(SiMenu siMenu){
		if(StringUtils.hasText(siMenu.getName())){
			StringBuffer url = new StringBuffer("");
			if(StringUtils.hasText(siMenu.getOwner())){
				url.append(siMenu.getName() + " (" + siMenu.getOwner() + ")");
			}else{
				url.append(siMenu.getName());
			}
			return url.toString();
		}else{
			return null;
		}
	}
	
	/**
	 * 將SiMenu的Url重組
	 * 1.CC在前面加上reDirect
	 * 2.在後面加上name的資訊
	 * 3.276 278 280 掛上menuId
	 * @param SiMenu
	 */
	public static String setMenuUrl(SiMenu parentMenu, SiMenu siMenu){
		String menuName = "";
		if(null != parentMenu && null != parentMenu.getName())
			menuName = parentMenu.getName() + " - ";
		if(null != siMenu && null != siMenu.getName())
			menuName = menuName + siMenu.getName(); 
		
		if(StringUtils.hasText(siMenu.getUrl())){
			if("BI".equals(siMenu.getReserve1()))
				siMenu.setUrl("redirectBI.jsp?url=" + siMenu.getUrl() + "&menuId=" +siMenu.getMenuId());
			if("CC".equals(siMenu.getReserve1()))
				siMenu.setUrl("redirectCC.jsp?url=" + siMenu.getUrl() + "&menuId=" +siMenu.getMenuId());
			if(null==siMenu.getReserve1()){
				siMenu.setUrl("redirectForm.jsp?url=" + siMenu.getUrl() + "&menuId=" +siMenu.getMenuId());
			}
			StringBuffer url = new StringBuffer("");
			if(siMenu.getUrl().indexOf("?") > -1)
				url.append(siMenu.getUrl() + "&reportName=" + menuName);
			else
				url.append(siMenu.getUrl() + "?reportName=" + menuName);
			return url.toString();
		}else{
			return null;
		}
	}
}
