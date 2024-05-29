package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTally entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SiMenuLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1392344103340696432L;
    // Fields
    private Long logId;
    private String employeeCode;
    private String menuId;
    private String actionMode;
    private String apServerIp;
    private Date openDate;
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getActionMode() {
		return actionMode;
	}
	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}
	public String getApServerIp() {
		return apServerIp;
	}
	public void setApServerIp(String apServerIp) {
		this.apServerIp = apServerIp;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
    
    
	
    

}