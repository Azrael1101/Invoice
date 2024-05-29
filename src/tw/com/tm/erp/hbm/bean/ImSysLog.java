package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * SoPostingTally entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ImSysLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1392344103340696432L;
    // Fields
    private Long lineId;
    private Long headId;
    private String module_name;
    private String action;
    private String identification;
    private String message;
    private Date create_date;
    private String created_by;
    
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	public Long getHeadId() {
		return headId;
	}
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
    
    


    


    

}