package tw.com.tm.erp.batch;

import tw.com.tm.erp.utils.T2PosIslandParse;

public class T2PosImportJobMazu {
    
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
		T2PosIslandParse t2PosIslandParse = new 
			T2PosIslandParse(brandCode, opUser, function, transferFolder, autoJobControl); 
	    t2PosIslandParse.execute("POS");
	    t2PosIslandParse.execute("VOID");
	    t2PosIslandParse.execute("LAD");
	}catch(Exception ex){
	    System.out.println(ex.toString());
	}
    }
}
