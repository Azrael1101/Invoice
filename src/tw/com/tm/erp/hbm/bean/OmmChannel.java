package tw.com.tm.erp.hbm.bean;

import java.io.Serializable;
import java.util.List;

public class OmmChannel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7834264740135957783L;
	private Long sysSno;
	private String channelType;
	private String categoryType;
	private String enable;
	private List<OmmChannelConfig> ommChannelConfigList;
	
	
	
	private String categoryTypeName;

	/**
	 * 
	 */
	public OmmChannel() {
	}

	/**
	 * @param sysSno
	 * @param channelType
	 * @param categoryType
	 * @param enable
	 */
	public OmmChannel(Long sysSno, String channelType, String categoryType, String enable, List<OmmChannelConfig> ommChannelConfigList) {
		this.sysSno = sysSno;
		this.channelType = channelType;
		this.categoryType = categoryType;
		this.enable = enable;
		this.ommChannelConfigList = ommChannelConfigList;
	}

	public Long getSysSno() {
		return sysSno;
	}

	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	public List<OmmChannelConfig> getOmmChannelConfigList() {
		return ommChannelConfigList;
	}

	public void setOmmChannelConfigList(List<OmmChannelConfig> ommChannelConfigList) {
		this.ommChannelConfigList = ommChannelConfigList;
	}

	public String getCategoryTypeName() {
		return categoryTypeName;
	}

	public void setCategoryTypeName(String categoryTypeName) {
		this.categoryTypeName = categoryTypeName;
	}
	
	

}
