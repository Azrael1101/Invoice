package tw.com.tm.erp.test;

import java.util.Date;
import java.util.List;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;

public class createPrivilege {
	final String SYS_CREATER = "SYS";
	BuEmployeeDAO buEmployeeDAO = (BuEmployeeDAO)SpringUtils.getApplicationContext().getBean("buEmployeeDAO");
	BuEmployeeBrandDAO buEmployeeBrandDAO = (BuEmployeeBrandDAO)SpringUtils.getApplicationContext().getBean("buEmployeeBrandDAO");
	SiUsersGroupDAO siUsersGroupDAO = (SiUsersGroupDAO)SpringUtils.getApplicationContext().getBean("siUsersGroupDAO");
	
	//1.登入名稱,報表登入名稱,報表登入密碼(BU_EMPLOYEE(LOGIN_NAME、REPORT_LOGIN_NAME、REPORT_PASSWORD)),若要清除輸入空白即可.....
	public void updateBuEmployeeNameAndPwd(String employeeCode, String loginName, String reportLoginName , String reportPassword){//employeeCode: 員工編號, loginName: 登入名稱, reportLoginName: 報表登入名稱, reportPassword: 報表登入密碼  
		if(employeeCode == null || employeeCode.trim().length() == 0) return;
		BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
		if(buEmployee == null)
			System.out.println("此帳號不存在!");
		else{
			buEmployee.setLoginName(loginName);
			buEmployee.setReportLoginName(reportLoginName);
			buEmployee.setReportPassword(reportPassword);
			buEmployee.setLastUpdatedBy(SYS_CREATER);
			buEmployee.setLastUpdateDate(new Date());
		}
	}
	
	//2.新增可登入的品牌(如欲修改,先刪掉要改的再新增)
	public void insertOrDeleteBuEmployBrand(int action, String employeeCode, String[] brandCodes){//action --> 0: 新增, 3: 刪除, brandCodes: 可登入的品牌
		if(employeeCode == null || employeeCode.trim().length() == 0) return;
		if(action == 0){//新增
			if(brandCodes != null && brandCodes.length > 0){
				for(String brandCode : brandCodes){
					BuEmployeeBrandId buEmployeeBrandId = new BuEmployeeBrandId(employeeCode, brandCode);
					BuEmployeeBrand buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
					if(buEmployeeBrand == null){//null代表沒資料需要新增
						buEmployeeBrand = new BuEmployeeBrand();
						buEmployeeBrand.setId(buEmployeeBrandId);
						buEmployeeBrand.setCreatedBy(SYS_CREATER);
						buEmployeeBrand.setCreationDate(new Date());
						buEmployeeBrand.setLastUpdatedBy(SYS_CREATER);
						buEmployeeBrand.setLastUpdateDate(new Date());
					}
				}
			}
			System.out.println("新增登入品牌成功!");
		}
		else if(action == 3){//刪除
			if(brandCodes != null && brandCodes.length > 0){
				for(String brandCode : brandCodes){
					BuEmployeeBrandId buEmployeeBrandId = new BuEmployeeBrandId(employeeCode, brandCode);
					BuEmployeeBrand buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
					if(buEmployeeBrand != null) buEmployeeBrandDAO.delete(buEmployeeBrand);
				}
			}
			System.out.println("刪除登入品牌成功!");
		}
	}
	
	//3.新增選單權限(與某工號同)
	public void insertOrDeleteSiUsersGroup(String oldEmployeeCode, String newEmployeeCode, String[] brandCodes){//oldEmployeeCode:等同於誰, newEmployeeCode:新建立的帳號, brandCodes:要建立的品牌
		if(oldEmployeeCode == null || oldEmployeeCode.trim().length() == 0) return;
		if(newEmployeeCode == null || newEmployeeCode.trim().length() == 0) return;
		if(brandCodes != null && brandCodes.length > 0){
			for(String brandCode : brandCodes){
				List<SiUsersGroup> oldGroups = siUsersGroupDAO.findByProperty(brandCode, oldEmployeeCode);
				if(oldGroups != null && oldGroups.size() > 0){
					for(SiUsersGroup oldGroup : oldGroups){
						SiUsersGroup group = new SiUsersGroup();
						group.setBrandCode(oldGroup.getBrandCode());
						group.setEmployeeCode(newEmployeeCode);
						group.setGroupCode(oldGroup.getGroupCode());
						group.setIndexNo(oldGroup.getIndexNo());
						group.setCreatedBy(SYS_CREATER);
						group.setCreationDate(new Date());
						group.setUpdatedBy(SYS_CREATER);
						group.setUdpateDate(new Date());
						siUsersGroupDAO.save(group);
					}
				}	
			}	
		}
	}
	
	//4.刪除選單權限(刪除某個工號的某些品牌選單權限SiUsersGroup)
	public void deleteSiUsersGroup(String employeeCode, String[] brandCodes){//employeeCode:要刪誰, brandCodes:要刪哪些品牌
		if(employeeCode == null || employeeCode.trim().length() == 0) return;
		if(brandCodes != null && brandCodes.length > 0){
			for(String brandCode : brandCodes){
				List<SiUsersGroup> groups = siUsersGroupDAO.findByProperty(brandCode, employeeCode);
				if(groups != null && groups.size() > 0)
					for(SiUsersGroup group : groups)
						siUsersGroupDAO.delete(group);
			}	
		}
	}
	
	//5.關權限(將SI_USERS_GROUP GROUP_CODE 欄位值改成 "_CLOSE_ + 原GROUP_CODE"
	public void closeSiUsersGroupCode(String employeeCode){
		_setSiUsersGroupCode(employeeCode, "_CLOSE_");
	}
	
	public void backupSiUsersGroupCode(String employeeCode){//_BAK_ : 將來整理成每個人一個群組時備份用
		_setSiUsersGroupCode(employeeCode, "_BAK_");
	}
	
	private void _setSiUsersGroupCode(String employeeCode, String pre){//employeeCode: 要關閉誰的帳號, pre: 原GROUP_CODE前要加的字串(_CLOSE_ : 假刪除, _BAK_ : 將來整理成每個人一個群組時備份用)
		if(employeeCode == null || employeeCode.trim().length() == 0) return;
		if(pre == null || pre.trim().length() == 0) return;
		List<SiUsersGroup> groups = siUsersGroupDAO.findByProperty(employeeCode);
		for(SiUsersGroup group : groups){
			group.setGroupCode(pre + group.getGroupCode());
			siUsersGroupDAO.update(group);
		}
	}
	
	//6.重整選單群組(每人一個群組)
	
	//執行...
	public static void main(String[] args) throws Exception {
		
	}

}
