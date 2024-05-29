package tw.com.tm.erp.batch;

import java.util.HashMap;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.utils.LCMSDoc;
import tw.com.tm.erp.utils.User;

public class T2PosImportJob {

	private String brandCode;

	private String opUser;

	private String function;

	private String transferFolder;

	private String autoJobControl;

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOpUser() {
		return opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getTransferFolder() {
		return transferFolder;
	}

	public void setTransferFolder(String transferFolder) {
		this.transferFolder = transferFolder;
	}

	public String getAutoJobControl() {
		return autoJobControl;
	}

	public void setAutoJobControl(String autoJobControl) {
		this.autoJobControl = autoJobControl;
	}

	public void execute(){
		try{
			HashMap map  = new HashMap();
			User userObj = new User();
			userObj.setBrandCode(brandCode);
			userObj.setEmployeeCode(opUser);
			map.put(SystemConfig.USER_SESSION_NAME, userObj);
			map.put("autoJobControl", autoJobControl);
			map.put("transferFolder", transferFolder);
			map.put("isPreviousDay", false);//馬祖完稅修正: 備份日期是否前一天(wade)
			String base = LCMSDoc.class.getResource("/").getPath();
			LCMSDoc doc = new LCMSDoc(base, function);
			doc.folderTransfer(map);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
}