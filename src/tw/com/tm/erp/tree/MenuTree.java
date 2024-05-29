package tw.com.tm.erp.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.SiFunctionObject;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;
import tw.com.tm.erp.hbm.bean.SiGroupMenuId;
import tw.com.tm.erp.hbm.bean.SiGroupObject;
import tw.com.tm.erp.hbm.bean.SiGroupObjectId;
import tw.com.tm.erp.hbm.bean.SiMenu;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.SiFunctionObjectDAO;
import tw.com.tm.erp.hbm.dao.SiGroupMenuDAO;
import tw.com.tm.erp.hbm.dao.SiGroupObjectDAO;
import tw.com.tm.erp.hbm.dao.SiMenuDAO;
import tw.com.tm.erp.hbm.service.SiMenuService;
import tw.com.tm.erp.utils.AjaxUtils;

public class MenuTree extends BaseDAO{
	
	private static final Log log = LogFactory.getLog(MenuTree.class);
	
	private List<ExtNode> getTree( List<SiMenu> siMenu, String loginBrandCode, String groupCode ){
//		log.info( "===========<getTree>===========");
		MenuNode tree = new MenuNode( 0L , "root", 0L, false, new FunctionNode() );
		
		// find (loginBrandCode and groupCode) and insert map 
		Map siGroupMenuMap = new HashMap();
		SiGroupMenuDAO siGroupMenuDAO = (SiGroupMenuDAO) SpringUtils.getApplicationContext().getBean("siGroupMenuDAO");
		List<SiGroupMenu> siGroupMenus = siGroupMenuDAO.findByProperty( "SiGroupMenu" , new String[]{ "id.brandCode", "id.groupCode" }, new Object[]{loginBrandCode, groupCode} ); 
		for (SiGroupMenu siGroupMenu : siGroupMenus) {
			siGroupMenuMap.put( siGroupMenu.getId().getMenuId(), siGroupMenu.getFunctionCode() );
		}
		
		for (int i = 0; i < siMenu.size(); i++) {
			SiMenu sm = siMenu.get(i);
		
			Long parentMenuId = sm.getParentMenuId();
			Long menuId 			= sm.getMenuId();
			String text 			= sm.getName();
			Boolean checked 	= siGroupMenuMap.containsKey(menuId); // 判定這個節點是否要checked by map

			String functionCode = sm.getFunctionCode();
			
			FunctionNode functionNode = new FunctionNode();
			List<FunctionObject> functionObjects = new ArrayList<FunctionObject>(); 
			
		  // 查詢functionCode 的 si_function_object 撈出欄位 objectCode,objectName,controlType
			// 撈si_group_object 放入map , key = objectCode ,value = controlType
			// object_checked = siGroupObjectMap.containsKey(objectCode);
			// if (object_checked) control_type =  siGroupObjectMap.get(objectCode)
			Map siGroupObjectMap = new HashMap();
			SiFunctionObjectDAO siFunctionObjectDAO = (SiFunctionObjectDAO) SpringUtils.getApplicationContext().getBean("siFunctionObjectDAO");
			log.debug( " functionCode = " + functionCode );
			List<SiFunctionObject> siFunctionObjects = siFunctionObjectDAO.findByProperty( "SiFunctionObject" , new String[]{ "siFunction.functionCode", "isLockRecord" }, new Object[]{ functionCode, AjaxUtils.IS_LOCK_RECORD_FALSE } );
			log.debug( " 重要 siFunctionObjects.size() = " + siFunctionObjects.size() );
			
			log.debug( " loginBrandCode = " + loginBrandCode  );
			log.debug( " groupCode = " + groupCode  );
			log.debug( " functionCode = " + functionCode  );
			SiGroupObjectDAO siGroupObjectDAO = (SiGroupObjectDAO) SpringUtils.getApplicationContext().getBean("siGroupObjectDAO");
			
			List<SiGroupObject> siGroupObjects = siGroupObjectDAO.findByProperty( "SiGroupObject" , new String[]{ "id.brandCode", "id.groupCode", "id.functionCode" }, new Object[]{loginBrandCode, groupCode, functionCode} );
			log.debug( " 重要 siGroupObjects.size() = " + siGroupObjects.size()  );
			for (SiGroupObject siGroupObject : siGroupObjects) {
				log.info( " siGroupObject.getId().getObjectCode() = " + siGroupObject.getId().getObjectCode()  );
				log.info( " siGroupObject.getControlType() = " + siGroupObject.getControlType()  );
				siGroupObjectMap.put( siGroupObject.getId().getObjectCode(), siGroupObject.getControlType() );
			}
			
			for (int j = 0; j < siFunctionObjects.size(); j++) {
				SiFunctionObject sifo = siFunctionObjects.get(j);
				
				String objectCode = sifo.getObjectCode(); 
				boolean objectChecked = siGroupObjectMap.containsKey(objectCode);
				
				FunctionObject fo = new FunctionObject(); 
				fo.setObjectCode( objectCode );
				fo.setObjectName( sifo.getObjectName() );
				fo.setObjectChecked( objectChecked );
				fo.setControlType( objectChecked ? (String)siGroupObjectMap.get(objectCode) : sifo.getControlType() );
				
				functionObjects.add(fo);
			}
			functionNode.setFunctionCode(functionCode);
			functionNode.setFunctionObjects(functionObjects);
			
			Enumeration en = tree.breadthFirstEnumeration();
			while (en.hasMoreElements()) {
				MenuNode menuNode = (MenuNode) en.nextElement(); 
				
				if( menuNode.getId().equals( parentMenuId ) ){
//					log.info( menuNode.getId() + " 插入節點 = " + menuId + " " + text );
					menuNode.add( new MenuNode( menuId, text, parentMenuId, checked, functionNode ) ); 
				}
			}
		}
//		log.info( "===========</getTree>===========");
		return tree.getExtTree(tree);
	}
	
	
	public List<ExtNode> getMenuTree( String loginBrandCode, String groupCode ) { 
		log.info( "===========<getMenuTree>===========");
		log.info( "start = " + new Date() );
		log.info( "loginBrandCode = " + loginBrandCode );
		log.info( "groupCode = " + groupCode );
		
		SiMenuDAO siMenuDAO = (SiMenuDAO) SpringUtils.getApplicationContext().getBean("siMenuDAO");
//		log.info( "siMenuDAO = " + siMenuDAO );
		List siMenu = siMenuDAO.myfindByProperty( "SiMenu" ); 
		
		List list = getTree( siMenu , loginBrandCode, groupCode );
		log.info( "end = " + new Date() );
		log.info( "===========</getMenuTree>===========");
		return list;
	}

	public String saveMenuTree(List<ExtNode> extNodes){
		for (int i = 0; i < extNodes.size(); i++) {
			log.info( extNodes.get(i).toString() );
		}
		
		return extNodes.size() > 0 ? "大於0" : "等於0";
	}
	
	public boolean saveMenuNodes(String brandCode, String groupCode, String loginCode, String[] menuIds, List<FunctionNode> functionNodes ){
		
		boolean isSave = false;
		
		try {
		
  		SiGroupObjectDAO siGroupObjectDAO = (SiGroupObjectDAO) SpringUtils.getApplicationContext().getBean("siGroupObjectDAO");
  		
  		for (int i = 0; i < functionNodes.size(); i++) {
  			FunctionNode functionNode = functionNodes.get(i);
  			List<SiGroupObject> siGroupObjects = 
  				siGroupObjectDAO.findByProperty("SiGroupObject", new String[]{"id.brandCode","id.groupCode","id.functionCode"}, new Object[]{brandCode,groupCode,functionNode.getFunctionCode()});
  				for (SiGroupObject siGroupObject : siGroupObjects) {
  					siGroupObjectDAO.delete(siGroupObject);
  				}
  
  				List<FunctionObject> functionObjects = functionNode.getFunctionObjects();
  				int j = 0;
  				for (FunctionObject functionObject : functionObjects) {
  					SiGroupObjectId siGroupObjectId = new SiGroupObjectId(brandCode,groupCode,functionNode.getFunctionCode(),functionObject.getObjectCode());
  					SiGroupObject siGroupObject = new SiGroupObject(siGroupObjectId);
  					siGroupObject.setControlType(functionObject.getControlType());
  					siGroupObject.setCreatedBy(loginCode);
  					siGroupObject.setCreatationDate(new Date());
  					siGroupObject.setUpdatedBy(loginCode);
  					siGroupObject.setUpdateDate(new Date());
  					siGroupObject.setIndexNo( j + 1L );
  					siGroupObjectDAO.save(siGroupObject);
  					j++;
  				}
  		}
  		
  		
  		SiGroupMenuDAO siGroupMenuDAO = (SiGroupMenuDAO) SpringUtils.getApplicationContext().getBean("siGroupMenuDAO");
  		log.info( "brandCode = " + brandCode );
  		log.info( "groupCode = " + groupCode );
  		List<SiGroupMenu> siGroupMenusDel = siGroupMenuDAO.findByProperty( "SiGroupMenu" , new String[]{ "id.brandCode", "id.groupCode" }, new Object[]{ brandCode, groupCode } );
  		log.info( "siGroupMenusDel.size = " + siGroupMenusDel.size() );
  		int delSize = siGroupMenusDel.size();
  		if( siGroupMenusDel != null && delSize > 0 ){
  			for (SiGroupMenu siGroupMenu : siGroupMenusDel) {
  				siGroupMenuDAO.delete( siGroupMenu );
  			}
  		}
  		
  		StringBuilder sb = new StringBuilder(" from SiMenu where ");
  		int menuIdsSize = menuIds.length;
  		//	強制結束
  		if( menuIdsSize == 0 ){
  			return true;
  		}
  		
  		for (int i = 0; i < menuIdsSize; i++) {
  			sb.append( " menuId = " );
  			sb.append( menuIds[i] );
  			sb.append( " or " );
  		}
  		int sbLength = sb.length();
  		if( menuIdsSize > 0 ){
    		sb.delete( sbLength - 4, sbLength );
  		}else{
  			sb.delete( sbLength - 6, sbLength );
  		}
  		log.info( "hql = " + sb.toString() );
  		
  		SiMenuDAO siMenuDAO = (SiMenuDAO) SpringUtils.getApplicationContext().getBean("siMenuDAO");
  		List<SiMenu> siMenus = siMenuDAO.find( sb.toString() );
  		
  		log.info( "siMenus.size = " + siMenus.size() );
  		
  		SiGroupMenuId siGroupMenuId = new SiGroupMenuId(); 
  		siGroupMenuId.setBrandCode(brandCode);
  		siGroupMenuId.setGroupCode(groupCode);
  		// save siGroupMenus from siMenus
  		for (int i = 0; i < siMenus.size(); i++) {
  			SiMenu siMenu = siMenus.get(i);
  			
  			siGroupMenuId.setMenuId(siMenu.getMenuId());
  			
  			SiGroupMenu siGroupMenu = new SiGroupMenu();
  			siGroupMenu.setId( siGroupMenuId );
  			siGroupMenu.setFunctionCode(siMenu.getFunctionCode());
  			siGroupMenu.setCreationDate(new Date());
  			siGroupMenu.setCreatedBy( loginCode );
  			siGroupMenu.setUpdateDate(new Date());
  			siGroupMenu.setUpdatedBy( loginCode );
  			siGroupMenu.setIndexNo( i + 1L );
  			siGroupMenuDAO.save( siGroupMenu );
  		}
    	isSave = true;
		
		} catch (Exception e) {
			log.error( e.getStackTrace() );
		}
		
		return isSave;
	}
	
}
