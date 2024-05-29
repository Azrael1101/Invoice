package tw.com.tm.erp.batch;

import java.util.HashMap;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.utils.LCMSDoc;
import tw.com.tm.erp.utils.User;

public class POSImportJob {
    
    private String brandCode;
    
    private String opUser;
    
    private String function;

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
    
    public void execute(){
	try{
	    HashMap map  = new HashMap();
	    User userObj = new User();
	    userObj.setBrandCode(brandCode);
            userObj.setEmployeeCode(opUser);
	    map.put(SystemConfig.USER_SESSION_NAME, userObj);
	    map.put("autoJobControl", "Y");
	    String base = LCMSDoc.class.getResource("/").getPath();
	    LCMSDoc doc = new LCMSDoc(base, function);
	    doc.folderTransfer(map);
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
