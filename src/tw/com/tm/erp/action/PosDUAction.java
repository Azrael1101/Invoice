package tw.com.tm.erp.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.Command;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.PosControlDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.hbm.service.BuBasicDataService;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.BuEmployeeAwardService;
import tw.com.tm.erp.hbm.service.BuShopService;
import tw.com.tm.erp.hbm.service.ImItemDiscountService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImPromotionMainService;
import tw.com.tm.erp.hbm.service.MachineSaleService;
import tw.com.tm.erp.hbm.service.PoEosService;
import tw.com.tm.erp.hbm.service.PosDUService;
import tw.com.tm.erp.hbm.service.PosImportDataService;
import tw.com.tm.erp.hbm.service.SoParallelTestService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
 
public class PosDUAction {
	private static final Log log = LogFactory.getLog(PosDUAction.class);

    private String programId = "PosDU";
    
	private String uuId;

	private String ip;

	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private DataSource dataSourceMySql;
	public void setDataSourceMySql(DataSource dataSourceMySql){
    	this.dataSourceMySql = dataSourceMySql;
    }

	private PosDUService posDUService;
	public void setPosDUService(PosDUService posDUService) {
		this.posDUService = posDUService;
	}

	private PosExportDAO posExportDAO;
	public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}

	private BuAddressBookService buAddressBookService;
	public void setBuAddressBookService(BuAddressBookService buAddressBookService) {
		this.buAddressBookService = buAddressBookService;
	}

	private PosImportDataAction posImportDataAction;
	public void setPosImportDataAction(PosImportDataAction posImportDataAction) {
		this.posImportDataAction = posImportDataAction;
	}

	private PosImportDataService posImportDataService;
	public void setPosImportDataService(PosImportDataService posImportDataService) {
		this.posImportDataService = posImportDataService;
	}
	
	private ImItemDiscountService imItemDiscountService;
	public void setImItemDiscountService(ImItemDiscountService imItemDiscountService) {
		this.imItemDiscountService = imItemDiscountService;
	}

	private BuShopService buShopService;
	public void setBuShopService(BuShopService buShopService) {
		this.buShopService = buShopService;
	}

	private ImItemService imItemService;
	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	private BuBasicDataService buBasicDataService;
	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	private BuBrandService buBrandService;
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	private BuShopMachineDAO buShopMachineDAO;
	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}

	private BuCommonPhraseService buCommonPhraseService;
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	private BuEmployeeAwardService buEmployeeAwardService;

	public void setBuEmployeeAwardService(
			BuEmployeeAwardService buEmployeeAwardService) {
		this.buEmployeeAwardService = buEmployeeAwardService;
	}

	private ImPromotionMainService imPromotionMainService;
	public void setImPromotionMainService(
		ImPromotionMainService imPromotionMainService) {
	this.imPromotionMainService = imPromotionMainService;
	}
	
	private MachineSaleService machineSaleService;
	public void setMachineSaleService(
		MachineSaleService machineSaleService) {
	this.machineSaleService = machineSaleService;
	}
	
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	private SoParallelTestService soParallelTestService;
	public void setSoParallelTestService(SoParallelTestService soParallelTestService){
		this.soParallelTestService = soParallelTestService;
	}
	
	private PosControlDAO posControlDAO;
	public void setPosControlDAO(PosControlDAO posControlDAO){
		this.posControlDAO = posControlDAO;
	}
	
	public List<Properties> performInitial(Map parameterMap){
		Map returnMap = null;	
		try{
			returnMap = posDUService.executeInitial(parameterMap);
		}catch (Exception ex) {
			System.out.println("執行POS資料傳輸作業初始化時發生錯誤，原因：" + ex.getMessage());
			MessageBox msgBox = new MessageBox();
			msgBox.setMessage("執行POS資料傳輸作業初始化時發生錯誤，原因：" + ex.getMessage());
			returnMap = new HashMap();
			returnMap.put("vatMessage" ,msgBox);
		}
		return AjaxUtils.parseReturnDataToJSON(returnMap);	
	}
 
	public List<Properties> performDownloadTransaction(Map parameterMap) {
		log.info("===enter performDownloadTransaction===");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			log.info("BRAND_CODE = " + brandCode);
			String dataType = (String)PropertyUtils.getProperty(formBindBean, "downloadFunction");
			log.info("ITEM_TYPE = " + dataType);
			String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");
			log.info("SHOP_CODE = " + shopCode);
			String machineCode = (String)PropertyUtils.getProperty(formBindBean, "posMachineCode");
			log.info("MACHINE_CODE = " + machineCode);
			String dateStart = (String)PropertyUtils.getProperty(formBindBean, "dateStart");
			log.info("dateStart = " + dateStart);
			String dateEnd = (String)PropertyUtils.getProperty(formBindBean, "dateEnd");
			log.info("dateEnd = " + dateEnd);
			String priceDate = (String)PropertyUtils.getProperty(formBindBean, "priceDate");
			log.info("priceDate = " + priceDate);
			String lockDate = (String)PropertyUtils.getProperty(formBindBean, "LockDate");
			log.info("lockDate = " + lockDate);
			Date dataDateStart = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, dateStart);
			log.info("DATA_DATE_STRAT = " + dataDateStart);
			Date dataDateEnd = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, dateEnd);
			log.info("DATA_DATE_END = " + dataDateEnd);
			Date dataPriceDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, priceDate);
			log.info("DATA_PRICE_DATE = " + dataPriceDate);
			Date dataLockDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, lockDate);
			log.info("DATA_PRICE_DATE = " + dataPriceDate);
			
			HashMap posDUMap = new HashMap();
			posDUMap.put("BRAND_CODE", brandCode);
			posDUMap.put("DATA_TYPE", dataType);
			posDUMap.put("SHOP_CODE", shopCode);
			posDUMap.put("MACHINE_CODE", machineCode);

			if(null == dataDateStart)
				dataDateStart = new Date();
			posDUMap.put("DATA_DATE_STRAT", dataDateStart);
			if(null == dataDateEnd)
				dataDateEnd = new Date();
			posDUMap.put("DATA_DATE_END", dataDateEnd);
			
			if(null == dataLockDate){
			    	dataLockDate = new Date();
			    	posDUMap.put("DATA_LOCK_DATE", dataLockDate);
			}
			//if(null == PRICE_DATE)
			//PRICE_DATE = new Date();
			posDUMap.put("DATA_PRICE_DATE", dataPriceDate);
			posOnlineExport(posDUMap);

			if("noData".equals(posDUMap.get("searchResult"))){
			    msgBox.setMessage("此區間查無資料");
			}else{
			    msgBox.setMessage("POS資料下傳完成");
			}
		}  catch (Exception ex) {
			log.error("執行POS資料下傳失敗");
			msgBox.setMessage("執行POS資料下傳失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		log.info("===leave performDownloadTransaction===");
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	public List<Properties> performUploadTransaction(Map parameterMap) {
		log.info("===enter performUploadTransaction===");
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");

			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			log.info("BRAND_CODE = " + brandCode);
			String dataType = (String)PropertyUtils.getProperty(formBindBean, "uploadFunction");
			log.info("ITEM_TYPE = " + dataType);
			String createdBy = (String)PropertyUtils.getProperty(formBindBean, "createdBy");
			log.info("EMPLOYEE_CODE = " + createdBy);
			String transactionSeqNo = (String)PropertyUtils.getProperty(formBindBean, "transactionSeqNo");
			log.info("TRANSACTION_SEQ_NO = " + transactionSeqNo);
			String posMachineCode = (String)PropertyUtils.getProperty(formBindBean, "posMachineCode");
			log.info("MACHINE_CODE = " + posMachineCode);

			HashMap posDUMap = new HashMap();
			posDUMap.put("DATA_TYPE", dataType);
			posDUMap.put("BRAND_CODE", brandCode);
			posDUMap.put("MACHINE_CODE", posMachineCode);
			posDUMap.put("COMPANY", createdBy);
			posDUMap.put("NUMBERS", 1L);
			posDUMap.put("TRANSACTION_SEQ_NO", transactionSeqNo);
			posDUService.posOnlineImport(posDUMap);
			msgBox.setMessage("POS資料下傳完成");
		}  catch (Exception ex) {
			log.error("執行POS資料上傳失敗，原因" + ex.getMessage());
			msgBox.setMessage("執行POS資料上傳失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		log.info("===leave performDownloadTransaction===");
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}


	/**
	 * 統一將所有上下傳在這裡值型
	 *
	 * @param HttpServletRequest request
	 * @throws Exception
	 */
	public String executePosDU(HttpServletRequest request){
		String returnValue = "-2";
		uuId = UUID.randomUUID().toString();
		ip = request.getRemoteAddr();
		String actionInfo = request.getParameter("actionInfo"); // actionInfo 行動
		String company = request.getParameter("company"); // company 上傳者
		String batchId = request.getParameter("batchId"); // batchId 
		String requestId = request.getParameter("requestId"); // requestId
		
		String machineCode = request.getParameter("machineCode"); // batchId 
		String dataType = request.getParameter("dataType"); // requestId
		
		String dataBase = (request.getParameter("dataBase")==null)?"":request.getParameter("dataBase");
		boolean type = false;
		
		log.info("POS上傳作業:machineCode = "+machineCode+"<<<>>>dataType = "+dataType+"<<<>>>actionInfo = "+actionInfo);
		
		//一日遊宅配特定庫不扣庫存 Brian	=========
		boolean isSVP = false;
		try {
			BuShop shopCode = buShopMachineDAO.getShopCodeByMachineCode("T2", machineCode, "Y", "Y");
			log.info(shopCode.getShopCode());
			if(!StringUtils.hasText(shopCode.getShopCode()) || shopCode == null){
				log.info("找不到此庫");
			}
			String headCode = "SpecialWarehouse";
			List<String> specialWarehouseStringList = new ArrayList();
			List<BuCommonPhraseLine> specialWarehouse = buBasicDataService.findCommonPhraseEnableLine(headCode);
			for (BuCommonPhraseLine bcpl : specialWarehouse){
				specialWarehouseStringList.add(bcpl.getAttribute1());
			}
			if(specialWarehouseStringList.contains(shopCode.getShopCode())){
				isSVP = true;
			}
			//===========
		  BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("Switch", "content");
    		  //log.error("開關"+buCommonPhraseLine.getEnable());
    		  if(buCommonPhraseLine.getEnable().equals("N")){
    			//log.error("關閉中"+returnValue);
    			return returnValue;
    		  }else{
    			log.info("0 requestId：" + requestId + "，之uuId = " + uuId);
    			
			if(dataType.equals("SOP")||dataType.equals("TALLY")){
				log.info("不列入檢核");
			}else{
				log.info("及時下傳排程檢核");
				type = posControlDAO.checkDataTpye(dataType, machineCode);
				if(type){
					log.info("檢核完須做下傳");					
				}else{
					//無下傳資料須回應11給POS，POS才會不繼續取getDataId
					return returnValue = "11";
				}    				
			}
    		
			// 以狀態判斷後端要做的動作
			posExportDAO.createProgramLog(programId, "INFO", actionInfo, ip,
			"開始執行執行POS上下傳， batchId = " + batchId + " 、 requestId = " + requestId, 		uuId, null, company);

			// 取得requestId
			if ("getRequestId".equals(actionInfo)) {
				returnValue = String.valueOf(posDUService.getRequestId(dataBase));
				log.info("第一階段  " + actionInfo);
				// 取得dataId
			} else if ("getDataId".equals(actionInfo)) {
				returnValue = posDUService.getDataId(dataBase);
				log.info("第二階段  " + actionInfo);
				// POS讀取完資料後，更新狀態為Finish	
			} else if ("updateComplete".equals(actionInfo)) {
				posDUService.updateComplete(request);
				log.info("第三階段  " + actionInfo);
				// 取得回傳的RESPONSE_ID資料
			} else if ("getResponseId".equals(actionInfo)){
				returnValue = String.valueOf(getPOSInfo(request)); 
				//soParallelTestService.soParallelTest(returnValue, request);				
				log.info("第四階段  " + actionInfo);
				// 手動執行批次下傳
			} else if ("jobStart".equals(actionInfo)){
				HashMap parameterMap = new HashMap();
				parameterMap.put("BRAND_CODE", request.getParameter("brandCode"));
				parameterMap.put("DATA_TYPE", isSVP?"SVP":request.getParameter("dataType")); //isSVP變數判定datatype給值
				parameterMap.put("SHOP_CODE", request.getParameter("shopCode"));
				parameterMap.put("MACHINE_CODE", request.getParameter("machineCode"));
				String dataDateStart = request.getParameter("dataDateStart");
				parameterMap.put("DATA_DATE_STRAT", DateUtils.parseDate("yyyyMMdd",dataDateStart));
				String dataDateEnd = request.getParameter("dataDateEnd");
				parameterMap.put("DATA_DATE_END", DateUtils.parseDate("yyyyMMdd",dataDateEnd));
				parameterMap.put("COMPANY", request.getParameter("company"));
				returnValue = String.valueOf(posOnlineExport(parameterMap));
			} else{
				posExportDAO.createProgramLog(programId, "ERROR", actionInfo, ip,
				"執行POS上下傳發生錯誤，查無參數 actionInfo = " + actionInfo + " 對應之活動 ", uuId, null, company);
				returnValue = "-1";
			}
			log.info("1 requestId：" + requestId + "，之request已經處理完成，returnValue = " + returnValue);
			posExportDAO.createProgramLog(programId, "INFO", actionInfo, ip,
			"結束執行POS上下傳， returnValue = " + returnValue, uuId, null, company);
    		  }			
		}catch(Exception e){ 
			log.error("執行POS上下傳發生錯誤，原因：" + e.getMessage());
			posExportDAO.createProgramLog(programId, "ERROR", actionInfo, ip,
			"執行POS上下傳發生錯誤，原因：" + e.getMessage(), uuId, null, company);
		}
		log.info("2 requestId：" + requestId + "，之request已經處理完成，returnValue = " + returnValue);
		return returnValue;
	}

	/**
	 * Call store procedure取得回傳的RESPONSE_ID
	 *
	 * @param request
	 * @throws ValidationErrorException
	 */
	public Long getPOSInfo(HttpServletRequest request) throws Exception {
		Long returnValue = 99L;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		CallableStatement calStmt = null;
		HashMap parameterMap = new HashMap();
		try {
			parameterMap.put("COMPANY", request.getParameter("company"));
			Long requestId = Long.parseLong(request.getParameter("requestId")); // REQUEST 序號
			String company = request.getParameter("company"); // 公司名稱
			String machineCode = request.getParameter("machineCode");
			String dataBase = (request.getParameter("dataBase")==null)?"":request.getParameter("dataBase");
			log.info("company=="+company+"==========requestId"+requestId+"=======dataBase"+dataBase);
			if(dataBase.equals("MySql")){
				conn = dataSourceMySql.getConnection();
			}else{
				conn = dataSource.getConnection();
			}
			
			String strQuery = "SELECT * FROM pos.pos_command WHERE request_id = " + requestId + " and type = 'REQ' ";
			stmt = conn.createStatement();
			rst = stmt.executeQuery(strQuery);

			//如果POS_COMMAND中有找到此筆request
			if (rst.next()) {
				//如果此筆資料已經完成，回傳1
				if(!OrderStatus.FINISH.equals(rst.getString("STATUS")) && !OrderStatus.WAITING.equals(rst.getString("STATUS"))){
					//一日遊宅配特定庫不扣庫存 Brian	=========
					boolean isSVP = false;
					BuShop shopCode = buShopMachineDAO.getShopCodeByMachineCode("T2", machineCode, "Y", "Y");
					log.info(shopCode.getShopCode());
					if(!StringUtils.hasText(shopCode.getShopCode()) || shopCode == null){
						log.info("找不到此庫");
					}
					String headCode = "SpecialWarehouse";
					List<String> specialWarehouseStringList = new ArrayList();
					List<BuCommonPhraseLine> specialWarehouse = buBasicDataService.findCommonPhraseEnableLine(headCode);
					for (BuCommonPhraseLine bcpl : specialWarehouse){
						specialWarehouseStringList.add(bcpl.getAttribute1());
					}
					if(specialWarehouseStringList.contains(shopCode.getShopCode())){
						isSVP = true;
					}
					//===========
					//log.info("此筆request MACHINE_CODE = " + rst.getString("MACHINE_CODE") + " & REQUEST_ID = " + requestId + " & DATA_ID = " + rst.getString("DATA_ID"));
					parameterMap.put("COMPANY", company); // REQ/RES
					parameterMap.put("TYPE", rst.getString("TYPE")); // REQ/RES
					parameterMap.put("BATCH_ID", rst.getLong("BATCH_ID")); //
					parameterMap.put("BRAND_CODE", rst.getString("BRAND_CODE")); //
					parameterMap.put("REQUEST_ID", rst.getLong("REQUEST_ID")); //
					parameterMap.put("ACTION", rst.getString("ACTION")); // E2P,P2E
					parameterMap.put("DATA_TYPE", isSVP?"SVP":rst.getString("DATA_TYPE")); // 資料類型SOP/IMV/PMO/ITEM/VIP/ONH 必要  //SVP追加 20200713
					parameterMap.put("DATA_ID", rst.getString("DATA_ID")); // 塞入產生的UUID
					parameterMap.put("OPERATION", rst.getString("OPERATION")); // 作業(P上傳/C條件式取資料/T取異動資料/A取所有資料) 必要
					parameterMap.put("MACHINE_CODE", rst.getString("MACHINE_CODE")); // 指定機台
					parameterMap.put("UU_ID", uuId); // 本次上下傳的uuId
					parameterMap.put("IP", ip); // 本次上下傳的uuId
					parameterMap.put("DATA_BASE", dataBase);
					posExportDAO.updateWaiting(parameterMap);

					posExportDAO.createProgramLog(programId, "INFO", "getPOSInfo", ip, 
					"上下傳開始，傳輸類別 DATA_TYPE = " + rst.getString("DATA_TYPE"), uuId, rst.getString("DATA_ID"), company);

					//如果是排程已經寫好的資料，下判斷把ERP_COMMAND複製到POS_COMMAND
					if("T".equals(rst.getString("OPERATION"))){
						//System.out.println("如果是排程已經寫好的資料，下判斷把ERP_COMMAND複製到POS_COMMAND");
						returnValue = posExportDAO.createE2PCommand(parameterMap);
					}else{
						//System.out.println("如果是即時，上下傳回覆");
						returnValue = posOnlineExport(parameterMap); // 寫入回覆資料
					}
					posExportDAO.updateComplete(parameterMap);
				}
				else
				{
					returnValue=1L;
				}
			}
		} catch (SQLException e) {
			log.error("呼叫SQL發生錯誤，原因：" + e.getMessage());
			throw e;
		} catch (Exception ex) {
			log.error("呼叫資料庫proceduce發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
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
			}
		}
		return returnValue;
	}

	/**
	 * 實作上下傳、先把資料下傳至POS_SCHEMA下各TABLE，再寫入POS_COMMAND 或是 ERP_COMMAND
	 *
	 * @param request
	 * @return
	 * @throws
	 */
	public Long posOnlineExport(HashMap parameterMap) throws Exception {
		//System.out.println("posOnlineExport");
		Connection conn = null;
		CallableStatement calStmt = null;
		Long responseId = -2L;
		try {
			String data_type = (String)parameterMap.get("DATA_TYPE");
			System.out.println("posOnlineExport data_type = " + data_type);
			Long requestId = (Long)parameterMap.get("REQUEST_ID");
			//System.out.println("posOnlineExport requestId = " + requestId);
			String brandCode = (String)parameterMap.get("BRAND_CODE");
			System.out.println("posOnlineExport brandCode = " + brandCode);
			//String dataType = (String)parameterMap.get("DATA_TYPE");
			//System.out.println("posOnlineExport dataType = " + dataType);
			//String shopCode = (String)parameterMap.get("SHOP_CODE");
			//System.out.println("posOnlineExport shopCode = " + shopCode);
			String machineCode = (String)parameterMap.get("MACHINE_CODE");
			System.out.println("posOnlineExport machineCode = " + machineCode);
			//String company = (String)parameterMap.get("COMPANY");
			//System.out.println("posOnlineExport company = " + company);
			//Date dataDateStart = (Date)parameterMap.get("DATA_DATE_STRAT");
			//System.out.println("posOnlineExport dataDateStart = " + dataDateStart);
			//Date dataDateEnd = (Date)parameterMap.get("DATA_DATE_END");
			//System.out.println("posOnlineExport dataDateEnd = " + dataDateEnd);

			// 一、判斷資料型態執行不同寫入不同Table
			/*
			 * 程式部分分為排程下傳以及即時下傳
			 * 排程下傳除了塞data以外並無其他參數
			 * 即時下傳則透過Map把POS_COMMAND的內容傳至各自的method裡面
			 */
			if ("ITEM".equals(data_type)) {
			    responseId = imItemService.executePosItemExport(parameterMap);
//				conn = dataSource.getConnection();
//				calStmt = conn.prepareCall("{? = call POS.SP_POS_ITEM(?,?,?,?,?,?,?,?,?,?)}"); // 呼叫store procedure
//				 System.out.println("------------"+parameterMap.size());
//				calStmt.registerOutParameter(1, Types.BIGINT);
//				for(int i=0;i<parameterMap.size();i++){
//				    System.out.println(parameterMap.get(i));
//				}
//				if(parameterMap.containsKey("BATCH_ID"))
//					calStmt.setLong(2,(Long)parameterMap.get("BATCH_ID"));
//				else
//					calStmt.setNull(2, Types.NULL);
//
//				if(parameterMap.containsKey("BRAND_CODE"))
//					calStmt.setString(3, (String)parameterMap.get("BRAND_CODE"));
//				else
//					calStmt.setNull(3, Types.NULL);
//
//				if(parameterMap.containsKey("REQUEST_ID"))
//					calStmt.setLong(4, (Long)parameterMap.get("REQUEST_ID"));
//				else
//					calStmt.setNull(4, Types.NULL);
//
//				if(parameterMap.containsKey("OPERATION"))
//					calStmt.setString(5, (String)parameterMap.get("OPERATION"));
//				else
//					calStmt.setNull(5, Types.NULL);
//
//				if(parameterMap.containsKey("DATA_ID"))
//					calStmt.setString(6, (String)parameterMap.get("DATA_ID"));
//				else
//					calStmt.setNull(6, Types.NULL);
//
//				if(parameterMap.containsKey("SHOP_CODE"))
//					calStmt.setString(7, (String)parameterMap.get("SHOP_CODE"));
//				else
//					calStmt.setNull(7, Types.NULL);
//
//				if(parameterMap.containsKey("MACHINE_CODE"))
//					calStmt.setString(8, (String)parameterMap.get("MACHINE_CODE"));
//				else
//					calStmt.setNull(8, Types.NULL);
//
//				if(parameterMap.containsKey("DATA_DATE_STRAT"))
//					calStmt.setDate(9, new java.sql.Date(((Date)parameterMap.get("DATA_DATE_STRAT")).getTime()));
//				else
//					calStmt.setNull(9, Types.NULL);
//
//				if(parameterMap.containsKey("DATA_DATE_END"))
//					calStmt.setDate(10, new java.sql.Date(((Date)parameterMap.get("DATA_DATE_END")).getTime()));
//				else
//					calStmt.setNull(10, Types.NULL);
//				System.out.println("=============A=======");
//				if(parameterMap.containsKey("DATA_PRICE_DATE"))
//				    if(parameterMap.get("DATA_PRICE_DATE") != null){
//					calStmt.setDate(11, new java.sql.Date(((Date)parameterMap.get("DATA_PRICE_DATE")).getTime()));
//				    }
//				else
//					calStmt.setNull(11, Types.NULL);
//
//				System.out.println("=============B=======");
//				calStmt.execute();
//				responseId = calStmt.getLong(1);
			} else if ("PMO".equals(data_type)) {
//				imPromotionMainService.executePosPromotionExport(parameterMap);
			} else if ("PMOS".equals(data_type)) {
//				imPromotionMainService.executeExcessivePosPromotionExport(parameterMap);
			    responseId = imPromotionMainService.executePosPromotion(parameterMap);
			} else if ("SOP".equals(data_type) || "SVP".equals(data_type)) {
				BuShopMachine buShopMachine = (buShopMachineDAO.findByBrandCodeAndMachineCode(brandCode, machineCode));
				parameterMap.put("responseId", responseId);
				if(null != buShopMachine && ( "AUTO".equals(buShopMachine.getUploadType()) || "UPLOAD".equals(buShopMachine.getUploadType()) ))
					responseId = posImportDataAction.executeUploadTransfer(parameterMap);
				else
					responseId = 1L;
				///POSLOG copy to SiSystemLog
			} else if ("LOG".equals(data_type)) {
					responseId = posImportDataService.executeUploadLog(parameterMap);
			/*} else if ("SHOP_DAILY".equals(data_type)) {
				responseId = posImportDataService.executeUploadDaily(parameterMap);*/
			} else if ("VIP".equals(data_type)){
				responseId = buAddressBookService.executePosCustomerExport(parameterMap);
			} else if ("EMP".equals(data_type)){
				responseId = buAddressBookService.executePosEmployeeExport(parameterMap);
			} else if ("DSC".equals(data_type)){
				responseId = imItemDiscountService.executeItemDiscountExport(parameterMap);
			} else if ("EAN".equals(data_type)){
				responseId = imItemService.executeItemEanExport(parameterMap);
			} else if ("RATE".equals(data_type)){
				responseId = buBasicDataService.executeCurrencyRateExport(parameterMap);
			} else if ("SHOP".equals(data_type)){
				//responseId = buShopService.executePosExport(parameterMap);
			} else if ("IMO".equals(data_type)){
				//responseId = imOnHandService.executePosExport(parameterMap);
			} else if("BS".equals(data_type)){ // 下傳所有的倉庫、店別資料
				conn = dataSource.getConnection();
				String dataId = posDUService.getDataId("");
				responseId = posDUService.getResponseId();
				/*
				 * 下傳資料分別依不同的Datatype存放於不同的Table
				 * Datatype=BS : POS_BU_SHOP
				 * Datatype=BSM : POS_BU_SHOP_MACHINE
				 * Datatype=PW : POS_WAREHOUSE
				 */
				calStmt = conn.prepareCall("{call POS.POS_SHOP_WAREHOUSE.GET_ALL_SHOP(?,?,?,?)}"); // 呼叫store procedure
				calStmt.setString(1, (String) parameterMap.get("BRAND_CODE"));
				calStmt.setString(2, dataId);
				calStmt.setLong(3, requestId);
				calStmt.setLong(4, responseId);
				calStmt.execute();
			} else if ("CLEAN".equals(data_type)) {
				List result = buShopMachineDAO.findByBrandAndEnable(brandCode, null, null);
				if (result != null && result.size() > 0) {
					for (int i = 0; i < result.size(); i++) {
						Object[] objArray = (Object[]) result.get(i);
						BuShopMachine buShopMachine = (BuShopMachine) objArray[1];
						parameterMap.put("MACHINE_CODE", buShopMachine.getId().getPosMachineCode());
						parameterMap.put("salesOrderDate", DateUtils.getShortDate(new Date()));
						if(null != buShopMachine && "UPLOAD".equals(buShopMachine.getUploadType()))
							posImportDataAction.updateCleanSO(parameterMap);
						parameterMap.remove("MACHINE_CODE");
					}
				}
			} else if ("EMPA".equals(data_type)){
				responseId = buEmployeeAwardService.executePosEMPAExport(parameterMap);
			} else if ("EMPAC".equals(data_type)){
				responseId = buEmployeeAwardService.executePosEMPACExport(parameterMap);
			} else if ("EMPAR".equals(data_type)){
				responseId = buEmployeeAwardService.executePosEMPARExport(parameterMap);
			}else if("LOCK".equals(data_type)){
			    	responseId = machineSaleService.executeLockItemExport(parameterMap);
			}else if("TALLY".equals(data_type)){
			    	responseId = 1L;
			}
			log.info("requestId：" + requestId + "，之request已經處理完成，responseId = " + responseId);
		} catch (Exception ex) {
			log.error("呼叫posOnlineExport發生錯誤，原因：" + ex.getMessage());
			throw ex;
		} finally {
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
			}
		}
		return responseId;
	}
	public List<Properties> performSelectDownload(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			log.info("BRAND_CODE = " + brandCode);
			String dataType = (String)PropertyUtils.getProperty(formBindBean, "downloadFunction");
			log.info("ITEM_TYPE = " + dataType);
			String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");
			log.info("SHOP_CODE = " + shopCode);
			String machineCode = (String)PropertyUtils.getProperty(formBindBean, "posMachineCode");
			log.info("MACHINE_CODE = " + machineCode);
			String dateStart = (String)PropertyUtils.getProperty(formBindBean, "dateStart");
			log.info("dateStart = " + dateStart);
			String dateEnd = (String)PropertyUtils.getProperty(formBindBean, "dateEnd");
			log.info("dateEnd = " + dateEnd);
			String priceDate = (String)PropertyUtils.getProperty(formBindBean, "priceDate");
			log.info("priceDate = " + priceDate);
			Date dataDateStart = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, dateStart);
			log.info("DATA_DATE_STRAT = " + dataDateStart);
			Date dataDateEnd = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, dateEnd);
			log.info("DATA_DATE_END = " + dataDateEnd);
			Date dataPriceDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, priceDate);
			log.info("DATA_PRICE_DATE = " + dataPriceDate);
			String downloadCount = (String)PropertyUtils.getProperty(formBindBean, "downloadCount");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			HashMap posDUMap = new HashMap();
			posDUMap.put("BRAND_CODE", brandCode);
			posDUMap.put("DATA_TYPE", dataType);
			posDUMap.put("SHOP_CODE", shopCode);
			posDUMap.put("MACHINE_CODE", machineCode);
			posDUMap.put("vatBeanPicker", pickerBean);
			posDUMap.put("DOWNLOAD_COUNT", downloadCount);
			if("EAN".equals(dataType)){
			    posDUService.savePosDownEAN(posDUMap);
			}else if("PMOS".equals(dataType)){
			    posDUService.savePosDownPMOS(posDUMap);
			}else if("ITEM".equals(dataType)){
			    posDUService.savePosDownItem(posDUMap);
			}else if("VIP".equals(dataType)){
			    posDUService.savePosDownVIP(posDUMap);
			}else if("EMP".equals(dataType)){
			    posDUService.savePosDownEMP(posDUMap);
			}else if("DSC".equals(dataType)){
			    posDUService.savePosDownDSC(posDUMap);
			}else if("RATE".equals(dataType)){
			    posDUService.savePosDownRATE(posDUMap);
			}
			else if("RESEND".equals(dataType)){
				posDUService.savePosResend(posDUMap);
			}
		    wrappedMessageBox(msgBox, posDUService, false , true);
			msgBox.setMessage("POS資料下傳完成,是否繼續操作?");
		}  catch (Exception ex) {
			log.error("執行POS資料下傳失敗");
			msgBox.setMessage("執行POS資料下傳失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}

	private void wrappedMessageBox(MessageBox msgBox,PosDUService posDUService , boolean isStartProcess, boolean isExecFunction)
	{

		log.info("Message Box");

		String f = "createRefreshForm()";
		String g = "closeWindows()";


		
		Command cmd_ok = new Command();
		if(isExecFunction){
			msgBox.setType(MessageBox.CONFIRM);
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{f, ""});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}else{
			cmd_ok.setCmd(Command.FUNCTION);
			cmd_ok.setParameters(new String[]{g, "CONFIRM"});
			Command cmd_cancel = new Command();
			cmd_cancel.setCmd(Command.WIN_CLOSE);
			msgBox.setCancel(cmd_cancel);
		}

		msgBox.setOk(cmd_ok);

	}
	public List<Properties> performResend(Map parameterMap) {
		Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String brandCode = (String)PropertyUtils.getProperty(formBindBean, "brandCode");			
			String dataType = "RETS";			
			String shopCode = (String)PropertyUtils.getProperty(formBindBean, "shopCode");			
			String store = (String)PropertyUtils.getProperty(formBindBean, "store");			

			HashMap posDUMap = new HashMap();
			posDUMap.put("BRAND_CODE", brandCode);
			posDUMap.put("DATA_TYPE", dataType);
			posDUMap.put("SHOP_CODE", shopCode);
			posDUMap.put("STORE", store);
			posDUMap.put("vatBeanFormBind", formBindBean);
			
			if("RETS".equals(dataType)){
				posDUService.savePosResend(posDUMap);
			}
			msgBox.setMessage("POS資料下傳完成");
			
		}  catch (Exception ex) {
			log.error("執行POS資料下傳失敗");
			msgBox.setMessage("執行POS資料下傳失敗，原因" + ex.getMessage());
		}
		returnMap.put("vatMessage", msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
}
