package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap; //import tw.com.tm.erp.constants.MessageStatus;
//import tw.com.tm.erp.hbm.bean.SiGroupObject;
//import tw.com.tm.erp.hbm.SpringUtils;
//import tw.com.tm.erp.hbm.bean.SiGroupObject;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.dao.SiGroupObjectDAO;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;
import tw.com.tm.erp.utils.PageAccessRight;

public class SiGroupObjectService {
	private SiGroupObjectDAO siGroupObjectDAO;

	public List getBrandUserObjectManager(String brandCode, String loginName) {// 由User身分找全部資訊
		return siGroupObjectDAO.getBrandUserObjectManager(brandCode, loginName);
	}

	public List getBrandUserObject(String brandCode, String loginName) {// 由User身分找groupCode,functionCode,objectCode組
		return siGroupObjectDAO.getBrandUserObject(brandCode, loginName);
	}

	public List getUserGroup(String brandCode, String loginName) {// 由User身分找Group
		return siGroupObjectDAO.getUserGroup(brandCode, loginName);
	}

	public List getFunctionObject(final String brandCode, final String groupCode) {// 由brand,group找functionCode,objectCode組
		return siGroupObjectDAO.getFunctionObject(brandCode, groupCode);
	}

	public List getFunctionList(final String brandCode, final List groupCode) {// 由brand,group
		// list找functionCode
		// List
		return siGroupObjectDAO.getFunctionList(brandCode, groupCode);
	}

	public List getObjectControlType(final String brandCode,
			final List groupCode, final String functionCode,
			final String objectCode) {// 找同一group,functionCode,objectCode組的ControlType
		return siGroupObjectDAO.getObjectControlType(brandCode, groupCode,
				functionCode, objectCode);
	}

	public List getGroupObjectSize(final String brandCode,
			final String loginName, final String functionCode,
			final String objectCode) {// 由User與FunctionObject找group size
		return siGroupObjectDAO.getGroupObjectSize(brandCode, loginName,
				functionCode, objectCode);
	}

	public HashMap getBrandUserObjectMapManager(String brandCode,
			String loginName) {// Object權限HshMap{functionCode,HashMap{objectCode,value}}
		HashMap map = new HashMap();
		List result = siGroupObjectDAO.getBrandUserObjectManager(brandCode,
				loginName);		//[groupCode,functionCode,objectCode,controlType,SiFunctionObject]
		if (result.size() > 0) {
			List brandObjects = siGroupObjectDAO.getBrandUserObject(brandCode,
					loginName);	//[groupCode,functionCode,objectCode]
			List groups = siGroupObjectDAO.getUserGroup(brandCode, loginName);
			List functions = siGroupObjectDAO
					.getFunctionList(brandCode, groups);
			List finalResult = new ArrayList();
			List tmp = new ArrayList(brandObjects);
			int count = 0;
			for (int i = 0; i < brandObjects.size(); i++) {
				Object[] res = (Object[]) brandObjects.get(i);
				List g_size = siGroupObjectDAO.getGroupObjectSize(brandCode,
						loginName, (String) res[1], (String) res[2]);
				if (g_size != null) {
					if (groups.size() - g_size.size() > 0) { // 若權限數目<User群組數目(表示有群組不控管)=>Remove
						tmp.remove(i - count);
						count = count + 1;
					}
				}
			}

			for (int i = 0; i < tmp.size(); i++) {
				Object[] res = (Object[]) tmp.get(i);
				List t_size = siGroupObjectDAO.getObjectControlType(brandCode,
						groups, (String) res[1], (String) res[2]);
				if (t_size != null) {
					String controlType = "";
					if (t_size.size() == 1) {
						controlType = (String) t_size.get(0);
					} else {// 若有多種權限,D>H
						if (t_size.contains("D")) {
							controlType = "D";
						} else {
							controlType = "H";
						}
					}

					for (int j = 0; j < result.size(); j++) {
						Object[] rst = (Object[]) result.get(j);
						if (((String) res[0]).equals((String) rst[0])
								&& ((String) res[1]).equals((String) rst[1])
								&& ((String) res[2]).equals((String) rst[2])) {
							SiFunctionObject sifo = (SiFunctionObject) rst[4];
							String[] src = new String[5];
							src[0] = (String) rst[1];
							src[1] = (String) rst[2];
							src[2] = controlType;
							src[3] = sifo.getObjectName();
							src[4] = sifo.getObjectType();
							finalResult.add(src);
						}
					}
				}
			}
			for (int i = 0; i < functions.size(); i++) {
				HashMap map1 = new HashMap();
				for (int j = 0; j < finalResult.size(); j++) {
					String[] src = (String[]) finalResult.get(j);
					if (src[0].equals((String) functions.get(i))) {
						map1.put(src[1].toString(), new PageAccessRight(src[1],
								src[3], src[4], src[2]));
					}

				}
				map.put((String) functions.get(i), map1);
			}
		}
		return map;
	}

	public void setSiGroupObjectDAO(SiGroupObjectDAO siGroupObjectDAO) {
		this.siGroupObjectDAO = siGroupObjectDAO;
	}

	public static void main(String[] args) {
		/**
		 * SiGroupObjectService siGroupObjectService = (SiGroupObjectService)
		 * SpringUtils .getApplicationContext().getBean("siGroupObjectService");
		 * siGroupObjectService.getBrandUserObjectMapManager("CO", "T93014");
		 */
	}
}