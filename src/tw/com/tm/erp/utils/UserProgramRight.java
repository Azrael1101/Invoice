package tw.com.tm.erp.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTree;

public class UserProgramRight implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8665902954581632592L;

    private List<JTree> menuTree;  
    private HashMap<String,HashMap<String,PageAccessRight>> pageRightManager;
    private List<String> menuName;
    private List<String> menuUrl;
    private List<String> imgNameA;
    private List<String> imgNameB;
    
    
    public List<JTree> getMenuTreeXml() {
        return menuTree;
    }
    
    public void setMenuTreeXml(List<JTree> menuTree) {
        this.menuTree = menuTree;
    }
    
    public HashMap<String,HashMap<String,PageAccessRight>> getPageRightManager() {
        return pageRightManager;
    }
    
    public void setPageRightManager(HashMap<String,HashMap<String,PageAccessRight>> pageRightManager) {
        this.pageRightManager = pageRightManager;
    }

	public List<String> getMenuName() {
		return menuName;
	}

	public void setMenuName(List<String> menuName) {
		this.menuName = menuName;
	}
	
	public List<String> getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(List<String> menuUrl) {
		this.menuUrl = menuUrl;
	}
	
	public List<String> getImgNameA() {
		return imgNameA;
	}

	public void setImgNameA(List<String> imgNameA) {
		this.imgNameA = imgNameA;
	}

	public List<String> getImgNameB() {
		return imgNameB;
	}

	public void setImgNameB(List<String> imgNameB) {
		this.imgNameB = imgNameB;
	}

}
