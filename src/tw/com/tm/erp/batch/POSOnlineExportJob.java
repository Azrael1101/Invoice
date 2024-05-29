package tw.com.tm.erp.batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import tw.com.tm.erp.action.PosDUAction;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.dao.PosExportDAO;

public class POSOnlineExportJob {
	
	private String brandCode;

	private String dataType;
	
	private String shopCode;
	
	private String machineCode;
	
	private Date dataDateStart;

	private Date dataDateEnd;
	
	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public Date getDataDateStart() {
		return dataDateStart;
	}

	public void setDataDateStart(Date dataDateStart) {
		this.dataDateStart = dataDateStart;
	}

	public Date getDataDateEnd() {
		return dataDateEnd;
	}

	public void setDataDateEnd(Date dataDateEnd) {
		this.dataDateEnd = dataDateEnd;
	}

	private String programId = "PosDU";
	
	public void execute(){
	    Connection conn = null;
	    String uuid = UUID.randomUUID().toString();
	    PosDUAction posDUAction = (PosDUAction)SpringUtils.getApplicationContext().getBean("posDUAction");
	    PosExportDAO posExportDAO = (PosExportDAO)SpringUtils.getApplicationContext().getBean("posExportDAO");
	    try{
		
		posExportDAO.createProgramLog(programId, "INFO", "BATCH_DOWNLOAD", "localHost", "開始執行POS批次下傳", uuid, null,"MIS");
		System.out.println("Strat Job!!!");
		
		HashMap parameterMap = new HashMap();
		parameterMap.put("BRAND_CODE", brandCode);
		parameterMap.put("SHOP_CODE", shopCode);
		parameterMap.put("MACHINE_CODE", machineCode);
		if(null == dataDateStart)
		    dataDateStart = new Date();
		parameterMap.put("DATA_DATE_STRAT", dataDateStart);
		if(null == dataDateEnd)
		    dataDateEnd = new Date();
		parameterMap.put("DATA_DATE_END", dataDateEnd);
		
		
		/*
		//dataType = "VIP";
		parameterMap.put("DATA_TYPE", "VIP");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		
		//dataType = "ITEM";
		parameterMap.put("DATA_TYPE", "ITEM");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		//dataType = "EMP";
		parameterMap.put("DATA_TYPE", "EMP");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		//dataType = "DSC";
		parameterMap.put("DATA_TYPE", "DSC");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		//dataType = "EAN";
		parameterMap.put("DATA_TYPE", "EAN");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");

		//dataType = "PMOS";
		parameterMap.put("DATA_TYPE", "PMOS");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		//dataType = "RATE";
		parameterMap.put("DATA_TYPE", "RATE");
		posDUService.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		*/
		
		//dataType = "RATE";
		parameterMap.put("DATA_TYPE", "CLEAN");
		posDUAction.posOnlineExport(parameterMap);
		parameterMap.remove("DATA_TYPE");
		
		posExportDAO.createProgramLog(programId, "INFO", "BATCH_DOWNLOAD", "localHost", "執行POS批次下傳結束", uuid, null,"MIS");
	    }catch(Exception ex){
		posExportDAO.createProgramLog(programId, "INFO", "BATCH_DOWNLOAD", "localHost", "執行POS批次下傳發生錯誤，原因：" + ex.getMessage(), uuid, null,"MIS");
		System.out.println(ex.getMessage());
	    } finally {
		if (conn != null) {
		    try {
			conn.close();
		    } catch (SQLException e) {
			System.out.println("關閉Connection時發生錯誤！");
		    }
		}
	    }
	}
}
