package tw.com.tm.erp.hbm.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.action.PosImportDataAction;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.PosCommand;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.dao.SoParallelTestDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.utils.DateUtils;

public class SoParallelTestService {

    private static final Log log = LogFactory.getLog(SoParallelTestService.class);
    private String programId = "PosDU";
    private String uuId;
	private String ip;
    OutputStream output = null;
	InputStream is = null;
	final String CONFIG_FILE = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/controller.properties";
	
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private DataSource dataSource98101;
	public void setDataSource98101(DataSource dataSource98101) {
		this.dataSource98101 = dataSource98101;
	}
    
    private SoParallelTestDAO soParallelTestDAO;
    public void setSoParallelTestDAO(SoParallelTestDAO soParallelTestDAO) {
		this.soParallelTestDAO = soParallelTestDAO;
	}
    
    private SoSalesOrderMainService soSalesOrderMainService;
    public void setSoSalesOrderMainService(
			SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}
    
    private PosImportDataAction posImportDataAction;
	public void setPosImportDataAction(PosImportDataAction posImportDataAction) {
		this.posImportDataAction = posImportDataAction;
	}
	
	private PosExportDAO posExportDAO;
	public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}
    
	private BuShopMachineDAO buShopMachineDAO;
	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}
	
    public void soParallelTest(String returnValue, HttpServletRequest request){
    	int returnTestValue = 0;
    	log.info("進入了~~~~~~~");
    	
    	try {
			returnTestValue = Integer.parseInt(returnValue);
			log.info("進入了~~~~~~~"+returnTestValue);
			if(returnTestValue>=0){
				//log.error("銷售上傳成功回寫狀態W");
				soParallelTestDAO.getPOSInfo(request);	
			}else{
				log.error("銷售上傳失敗 requestId = "+request.getParameter("requestId"));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception ex){
			ex.printStackTrace();
		}
    }
    
    public void updateCopySo(Long l, String res)throws Exception{
    	soParallelTestDAO.updatePOSInfo(l, res);
	}
    
    public void loadConfig(Properties config) throws Exception {
		try {
			File f = new File(CONFIG_FILE);
	        is = new FileInputStream( f );
	        config.load( is );
	        is.close();
			System.out.println("讀取 "+CONFIG_FILE+" 設定檔完成！");
		} catch (IOException ex) {
			throw new Exception("讀取 "+CONFIG_FILE+" 設定檔失敗！");
		}
	}
	
    FileOutputStream out = null;
    
	public void clsConfig(Properties config) throws Exception {
		try {
			File f = new File(CONFIG_FILE);
			out = new FileOutputStream( f );
	        config.store(out, "0");
	        out.close();
			System.out.println("儲存 "+CONFIG_FILE+" 設定檔完成！");
		} catch (IOException ex) {
			throw new Exception("儲存 "+CONFIG_FILE+" 設定檔失敗！");
		}
	}
	
	public void updateTestPOSInfo(String company_old, Long requestId_old ) throws Exception {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		CallableStatement calStmt = null;
		HashMap parameterMap = new HashMap();
		try {
			parameterMap.put("COMPANY", company_old);
			Long requestId = requestId_old; // REQUEST 序號
			String company = company_old; // 公司名稱
			log.info("company=="+company+"==========requestId"+requestId);
			conn = dataSource98101.getConnection();
			String strQuery = "SELECT * FROM pos.pos_command WHERE request_id = " + requestId + " and type = 'REQ' ";
			stmt = conn.createStatement();
			rst = stmt.executeQuery(strQuery);

			//如果有找到此筆request
			if (rst.next()) {
				log.info("此筆request MACHINE_CODE = " + rst.getString("MACHINE_CODE") + " & REQUEST_ID = " + requestId + " & DATA_ID = " + rst.getString("DATA_ID"));
				parameterMap.put("COMPANY", company); // REQ/RES
				parameterMap.put("TYPE", rst.getString("TYPE")); // REQ/RES
				parameterMap.put("BATCH_ID", rst.getLong("BATCH_ID")); //
				parameterMap.put("BRAND_CODE", rst.getString("BRAND_CODE")); //
				parameterMap.put("REQUEST_ID", rst.getLong("REQUEST_ID")); //
				parameterMap.put("ACTION", rst.getString("ACTION")); // E2P,P2E
				parameterMap.put("DATA_TYPE", rst.getString("DATA_TYPE")); // 資料類型SOP/IMV/PMO/ITEM/VIP/ONH 必要
				parameterMap.put("DATA_ID", rst.getString("DATA_ID")); // 塞入產生的UUID
				parameterMap.put("OPERATION", rst.getString("OPERATION")); // 作業(P上傳/C條件式取資料/T取異動資料/A取所有資料) 必要
				parameterMap.put("MACHINE_CODE", rst.getString("MACHINE_CODE")); // 指定機台
				parameterMap.put("UU_ID", uuId); // 本次上下傳的uuId
				parameterMap.put("IP", ip); // 本次上下傳的uuId
				this.posOnlineExport(parameterMap); // 寫入回覆資料				
			}
		} catch (SQLException e) {
			log.error("呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw e;
		} catch (Exception ex) {
			log.error("呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} finally {
			log.info("end updateTestPOSInfo 111");
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			
			if (stmt != null) {
				try {
					stmt = null;
				} catch (Exception e) {
					log.error("關閉rst = null時發生錯誤！");
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
			
			log.info("end updateTestPOSInfo 222");
		}
		
		
	}
	
	/**
	 * 實作上下傳、先把資料下傳至POS_SCHEMA下各TABLE，再寫入POS_COMMAND 或是 ERP_COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public void posOnlineExport(HashMap parameterMap) throws Exception {
		String data_type = (String)parameterMap.get("DATA_TYPE");
		Long requestId = (Long)parameterMap.get("REQUEST_ID");
		String brandCode = (String)parameterMap.get("BRAND_CODE");
		String machineCode = (String)parameterMap.get("MACHINE_CODE");
		System.out.println("posOnlineExport machineCode = " + machineCode);
		String res = "";
		Long responseId = -1L;
		//Connection conn = null;
		//CallableStatement calStmt = null;
		try {
			if ("SOP".equals(data_type)) {
				BuShopMachine buShopMachine = (buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode, machineCode));
				parameterMap.put("responseId", responseId);
				if(null != buShopMachine && ( "AUTO".equals(buShopMachine.getUploadType()) || "UPLOAD".equals(buShopMachine.getUploadType()) ))
					responseId = posImportDataAction.executeUploadTransfer(parameterMap);
				else
					responseId = -1L;
				///POSLOG copy to SiSystemLog
			}
			
			if (responseId>=0){
				res = "X";
				this.updateCopySo(requestId,res);
			}else{
				log.info("待查證responseId"+ requestId + "== machineCode = "+machineCode+ "== dataId"+parameterMap.get("DATA_ID"));
				res = "E";
				this.updateCopySo(requestId,res);
			}

			log.info("requestId：" + requestId + "，之request已經處理完成，responseId = " + responseId);
		} catch (Exception ex) {
			log.error("呼叫posOnlineExport發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} finally {
			log.info("===xxxxxxxxxxxxxxxx======");
			/*
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}*/
		}
	}
}
