package tw.com.tm.erp.hbm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuPosUiConfigHead {

	private Long sysSno;
	private String versionCode;
	private String versionName;
	private String version;
	private String posMachineCode;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private List<BuPosUiConfigLine> buPosUiConfigLines = new ArrayList(0);
	
	public List<BuPosUiConfigLine> getBuPosUiConfigLines() {
		return buPosUiConfigLines;
	}
	public void setBuPosUiConfigLines(List<BuPosUiConfigLine> buPosUiConfigLines) {
		this.buPosUiConfigLines = buPosUiConfigLines;
	}
	public Long getSysSno() {
		return sysSno;
	}
	public void setSysSno(Long sysSno) {
		this.sysSno = sysSno;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPosMachineCode() {
		return posMachineCode;
	}
	public void setPosMachineCode(String posMachineCode) {
		this.posMachineCode = posMachineCode;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	
}
