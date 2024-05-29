package tw.com.tm.erp.batch;

import java.util.Date;
import java.util.HashMap;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exportdb.POSExportData;
import tw.com.tm.erp.exportdb.POSIslandsExportData;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.LCMSDoc;
import tw.com.tm.erp.utils.User;

public class POSExportJob {

	private String brandCode;

	private String opUser;

	private int functionCode;

	private Date dataDate;

	private Date dataDateEnd;

	private String customsWarehouseCode;

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

	public int getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public Date getDataDateEnd() {
		return dataDateEnd;
	}

	public void setDataDateEnd(Date dataDateEnd) {
		this.dataDateEnd = dataDateEnd;
	}

	public String getCustomsWarehouseCode() {
		return customsWarehouseCode;
	}

	public void setCustomsWarehouseCode(String customsWarehouseCode) {
		this.customsWarehouseCode = customsWarehouseCode;
	}

	public void execute(){
		try{
			POSExportData export = new POSExportData();
			export.doPOSExportData(functionCode, brandCode, dataDate, dataDateEnd, opUser);	  
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

	public void executeIslands(){
		try{
			POSIslandsExportData export = new POSIslandsExportData();
			export.doPOSExportData(functionCode, brandCode, dataDate, dataDateEnd, opUser, customsWarehouseCode);	  
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
}
