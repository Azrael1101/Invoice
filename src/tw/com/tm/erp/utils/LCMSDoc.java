package tw.com.tm.erp.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SiPosLogService;
import tw.com.tm.erp.hbm.service.SiSystemLogService;
import tw.com.tm.erp.hbm.service.SoPostingTallyService;
import tw.com.tm.erp.hbm.service.TmpImportPosItemService;
import tw.com.tm.erp.hbm.service.TmpImportPosPaymentService;
import tw.com.tm.erp.importdb.T2PosImportData;
import tw.com.tm.erp.utils.dbf.DBFUtils;
import tw.com.tm.erp.utils.sp.JobCheckingService;

/**
 * 外部檔案匯入 ERP 系統
 * 
 * @author SAM,T02049
 * 
 */
public class LCMSDoc {

	private static final Log log = LogFactory.getLog(LCMSDoc.class);
	private static String baseFolder = "D:/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes";
	private String srcFolder = baseFolder + "/ORIGINAL"; // 上傳檔案原始放置目錄
	private String destFolder = baseFolder + "/PROCESS"; // 上傳檔案處理放置目錄
	private String batchFolder = baseFolder + "/PROCESS"; // 上傳檔案批次放置目錄
	private String successFolder = baseFolder + "/SUCCESS"; // 上傳檔案匯入DB後成功放置目錄
	private String failFolder = baseFolder + "/FAIL"; // 上傳檔案匯入DB後失敗放置目錄
	private String module = "";
	private String soFolder; //業績目錄
	private String msFolder; //調撥MS目錄
	private String rtFolder; //調撥RT目錄
	private static final String UP_FOLDER = "up";
	private static final String BACKUP_FOLDER = "BACKUP";

	/*
	 * private static String PdcdataFp = ""; private static String PdcdataRc =
	 * ""; private static String PdcdataRh = ""; private static String
	 * PdcdataRhA = ""; private static final int
	 * POS_MOV_HEAD_DBF_FILE_FIELD_LENGTH[] = { 11, 6, 8, 8, 6, 19 };
	 */

	// 預設建構子
	public LCMSDoc() {
	}

	// 建構方式完整基礎路徑,模組名稱(含package)
	public LCMSDoc(String basePath, String moduleName) throws Exception {
		log.info("LCMSDoc.LCMSDoc moduleName=" + moduleName + ",basePath=" + basePath);
		baseFolder = basePath;
		module = moduleName;
		Vector type = StringTools.SubStrings(moduleName, ".");
		String mod = (String) type.get(type.size() - 1);
		moduleName = mod;
		File file0 = new File(baseFolder + "/" + moduleName);
		if (!file0.exists()) { // baseFolder/moduleName not exists,建dir
			log.info("file0.mkdir()=" + file0.mkdir());
		}
		String baseModelFolder = baseFolder + "/" + moduleName + "/";
		destFolder = baseModelFolder + "PROCESS";
		batchFolder = baseModelFolder + "PROCESS";
		srcFolder = baseModelFolder + "ORIGINAL";
		successFolder = baseModelFolder + "SUCCESS";
		failFolder = baseModelFolder + "FAIL";
		File src = new File(srcFolder);
		if (!src.exists()) { // baseFolder/moduleName/srcFolder not
			// exists,建dir
			log.info("src.mkdir()=" + src.mkdir());
		}
		File dest = new File(destFolder);
		if (!dest.exists()) { // baseFolder/moduleName/destFolder not
			// exists,建dir
			log.info("dest.mkdir()=" + dest.mkdir());
		}
		File batch = new File(batchFolder);
		if (!batch.exists()) { // baseFolder/moduleName/batchFolder not
			// exists,建dir
			log.info("batch.mkdir()=" + batch.mkdir());
		}
		File success = new File(successFolder);
		if (!success.exists()) { // baseFolder/moduleName/successFolder not
			// exists,建dir
			log.info("success.mkdir()=" + success.mkdir());
		}
		File fail = new File(failFolder);
		if (!fail.exists()) { // baseFolder/moduleName/failFolder not
			// exists,建dir
			log.info("fail.mkdir()=" + fail.mkdir());
		}

		if (!"POSMOVImportData".equals(moduleName) && !"POSImportData".equals(moduleName)){
			String date = DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD);
			srcFolder = srcFolder + "/" + date;
			successFolder = successFolder + "/" + date;
			failFolder = failFolder + "/" + date;

			src = new File(srcFolder);
			if (!src.exists()) {
				log.info("src.mkdir()=" + src.mkdir());
			}
			success = new File(successFolder);
			if (!success.exists()) {
				log.info("success.mkdir()=" + success.mkdir());
			}
			fail = new File(failFolder);
			if (!fail.exists()) {
				log.info("fail.mkdir()=" + fail.mkdir());
			}	   
		}
		log.info("srcFolder " + srcFolder);
	}

	public static String getT1(String src) throws IOException // T1格式轉換用
	{
		log.info("LCMSDoc.LCMSDoc getT1=" + src);
		String dest = "";
		byte[] bytes = src.getBytes();
		if (bytes.length >= 220) {
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 5)
			+ "{#}" + subString(bytes, 5, 10) + "{#}"
			+ subString(bytes, 10, 11) + "{#}"
			+ subString(bytes, 11, 15) + "{#}"
			+ subString(bytes, 15, 17) + "{#}"
			+ subString(bytes, 17, 31) + "{#}"
			+ subString(bytes, 31, 39) + "{#}"
			+ subString(bytes, 39, 47) + "{#}"
			+ subString(bytes, 47, 55) + "{#}"
			+ subString(bytes, 55, 69) + "{#}"
			+ subString(bytes, 69, 77) + "{#}"
			+ subString(bytes, 77, 78) + "{#}"
			+ subString(bytes, 78, 81) + "{#}"
			+ subString(bytes, 81, 93) + "{#}"
			+ subString(bytes, 93, 99) + "{#}"
			+ subString(bytes, 99, 111) + "{#}"
			+ subString(bytes, 111, 118) + "{#}"
			+ subString(bytes, 118, 188) + "{#}"
			+ subString(bytes, 188, 190) + "{#}"
			+ subString(bytes, 190, 204) + "{#}"
			+ subString(bytes, 204, 212) + "{#}"
			+ subString(bytes, 212, 220);
			if (bytes.length > 220) {
				dest = dest + "{#}" + subString(bytes, 220, bytes.length);
			}
		} else {
			dest = "Size Error";
		}
		return dest;
	}

	public static String getT2(String src) throws IOException // T2格式轉換用
	{
		String dest = "";
		byte[] bytes = src.getBytes();
		if (bytes.length >= 222) {
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16)
			+ "{#}" + subString(bytes, 16, 20) + "{#}"
			+ subString(bytes, 20, 35) + "{#}"
			+ subString(bytes, 35, 105) + "{#}"
			+ subString(bytes, 105, 120) + "{#}"
			+ subString(bytes, 120, 135) + "{#}"
			+ subString(bytes, 135, 175) + "{#}"
			+ subString(bytes, 175, 187) + "{#}"
			+ subString(bytes, 187, 201) + "{#}"
			+ subString(bytes, 201, 204) + "{#}"
			+ subString(bytes, 204, 218) + "{#}"
			+ subString(bytes, 218, 222);
			if (bytes.length > 222) {
				dest = dest + "{#}" + subString(bytes, 222, bytes.length);
			}
		} else {
			dest = "Size Error";
		}
		return dest;
	}

	public static String getT3(String src) throws IOException // T3格式轉換用
	{
		String dest = "";
		byte[] bytes = src.getBytes();
		if (bytes.length >= 38) {
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16)
			+ "{#}" + subString(bytes, 16, 20) + "{#}"
			+ subString(bytes, 20, 38);
			if (bytes.length > 38) {
				dest = dest + "{#}" + subString(bytes, 38, bytes.length);
			}
		} else {
			dest = "Size Error";
		}
		return dest;
	}

	public static String getT4(String src) throws IOException // T4格式轉換用
	{
		String dest = "";
		byte[] bytes = src.getBytes();
		if (bytes.length >= 33) {
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16)
			+ "{#}" + subString(bytes, 16, 28) + "{#}"
			+ subString(bytes, 28, 32) + "{#}"
			+ subString(bytes, 32, 33);
			if (bytes.length > 33) {
				dest = dest + "{#}" + subString(bytes, 33, bytes.length);
			}
		} else {
			dest = "Size Error";
		}
		return dest;
	}

	// 取字串某範圍內Bytes(for中文)
	public static String subString(byte[] bytes, int srcBegin, int srcEnd)
	throws IOException {
		byte[] res = new byte[srcEnd - srcBegin];
		String result = "";
		for (int i = 0; i < res.length; i++) {
			res[i] = bytes[srcBegin + i];
		}
		result = new String(res);
		return result;

	}

	// 報關單檔案(CmDeclarationImportData)轉檔
	public boolean fileTransfer(String srcFilename) {
		log.info("LCMSDoc.fileTransfer srcFilename=" + srcFilename);
		// SRC->DEST
		boolean result = true;
		try {
			String destFilename = srcFilename + ".txt";
			String resultString = new String();
			String getfr;
			/** ** open file from srcFolder/srcFilename *** */
			BufferedReader fr = new BufferedReader(new FileReader(srcFolder
					+ "/" + srcFilename));
			// OutputStreamWriter fw = new OutputStreamWriter(new
			// FileOutputStream(destFolder+"/"+destFilename),"UTF-8");
			BufferedWriter fw = new BufferedWriter(new FileWriter(destFolder
					+ "/" + destFilename));
			while (fr.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr.readLine(); // 取得一行輸入
				char fun = getfr.charAt(1);
				resultString = "";
				switch (fun) {
				case '1':
					resultString = getT1(getfr);
					break;
				case '2':
					resultString = getT2(getfr);
					break;
				case '3':
					resultString = getT3(getfr);
					break;
				case '4':
					resultString = getT4(getfr);
					break;
				default:
					break;
				}
				if (resultString.equals("Size Error")) {
					result = false;
				}
				StringBuffer strbr = new StringBuffer(resultString);
				fw.write(strbr.toString()); // 寫入檔案
				fw.newLine();
				// fw.flush();
			}// while fr.ready()
			fr.close(); // close file
			fw.close(); // close file
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	private void performSOPImport(File[] baseFiles, String importFileRootPath, HashMap uiProperties) throws Exception{

		String brandCode = null;
		String loginUser = null;
		String processName = "";
		String errorMsg = null;
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);    
		if (userObj != null) {
			loginUser = userObj.getEmployeeCode();
			brandCode = userObj.getBrandCode();
		}
		processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
		Date executeDate = (Date)uiProperties.get("actualExecuteDate");
		String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
		String uuid = (String)uiProperties.get("uuidCode");
		soFolder = srcFolder + "/" + brandCode;
		//建立品牌業績資料夾
		File soRoot = new File(soFolder);
		if(!soRoot.exists()){
			FileUtils.forceMkdir(soRoot);
		}
		//建立backup資料夾
		File backUpRoot = new File(importFileRootPath + BACKUP_FOLDER + "/" + executeDateStr);
		if(!backUpRoot.exists()){
			FileUtils.forceMkdir(backUpRoot);
		}
		//建立轉入成功後放置的資料夾
		String successFilePath = successFolder + "/" + brandCode + "/" + executeDateStr;
		uiProperties.put(ImportDBService.SUCCESS_FILE_PATH, successFilePath);
		File successRoot = new File(successFilePath);
		if(!successRoot.exists()){
			FileUtils.forceMkdir(successRoot);
		}	
		SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils
		.getApplicationContext().getBean("siSystemLogService");
		//===============複製並刪除欲上傳的檔案===========================
		for (int i = 0; i < baseFiles.length; i++) {
			String fileName = null;
			try{
				fileName = baseFiles[i].getName().toUpperCase();    
				if (fileName.startsWith("H") || fileName.toUpperCase().startsWith("D")){
					FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
					FileUtils.copyFileToDirectory(baseFiles[i], soRoot);
					baseFiles[i].delete();
				}
			}catch(Exception ex){
				errorMsg = "複製並刪除" + fileName + "失敗，原因：" + ex.toString();
				log.error(errorMsg);
				siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);   
			}
		}
		//=========================SO==================================
		File[] soFiles = soRoot.listFiles();
		for(int i = 0; i < soFiles.length; i++){
			String srcFileName = null;
			try{
				srcFileName = soFiles[i].getName();
				boolean isLineFileExist = true;
				if (srcFileName.toUpperCase().startsWith("H")) {
					String headFileName = srcFileName;
					String lineFileName = "D" + srcFileName.substring(1);
					File lineFile = new File(soFolder + "/" + lineFileName);
					if(!lineFile.exists()){
						lineFileName = "d" + srcFileName.substring(1);
						lineFile = new File(soFolder + "/" + lineFileName);
						if(!lineFile.exists()){
							isLineFileExist = false;
						}
					}

					if(isLineFileExist){
						try{
							uiProperties.put("actualRootPath", soFolder);
							sopDbfFileProcess(headFileName, lineFileName, uiProperties);
						}catch(Exception ex){
							errorMsg = "POS業績匯入" + headFileName + "、" + lineFileName + "失敗，原因：" +  ex.toString();
							log.error(errorMsg);
							siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
						}		    
					}else{
						errorMsg = "POS業績匯入" + headFileName + "失敗，沒有相對應的明細檔：" +  lineFileName;
						log.error(errorMsg);
						siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
					}		
				}  
			}catch(Exception ex){
				errorMsg = "POS業績匯入" + srcFileName + "失敗，原因：" +  ex.toString();
				log.error(errorMsg);
				siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
			}
		}	
	}

	private void performPOSMovementImport(File[] baseFiles, String importFileRootPath, HashMap uiProperties) throws Exception{

		String brandCode = null;
		String loginUser = null;
		String processName = "";
		String errorMsg = null;
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);    
		if (userObj != null) {
			loginUser = userObj.getEmployeeCode();
			brandCode = userObj.getBrandCode();
		}
		processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
		Date executeDate = (Date)uiProperties.get("actualExecuteDate");
		String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
		String uuid = (String)uiProperties.get("uuidCode");
		msFolder = srcFolder + "/" + brandCode + "/MS";
		rtFolder = srcFolder + "/" + brandCode + "/RT";
		//建立MS資料夾
		File msRoot = new File(msFolder);
		if(!msRoot.exists()){
			FileUtils.forceMkdir(msRoot);
		}
		//建立RT資料夾
		File rtRoot = new File(rtFolder);
		if(!rtRoot.exists()){
			FileUtils.forceMkdir(rtRoot);
		}
		//建立backup資料夾
		File backUpRoot = new File(importFileRootPath + BACKUP_FOLDER + "/" + executeDateStr);
		if(!backUpRoot.exists()){
			FileUtils.forceMkdir(backUpRoot);
		}
		//建立轉入成功後放置的資料夾
		String successFilePath = successFolder + "/" + brandCode + "/" + executeDateStr;
		uiProperties.put(ImportDBService.SUCCESS_FILE_PATH, successFilePath);
		File successRoot = new File(successFilePath);
		if(!successRoot.exists()){
			FileUtils.forceMkdir(successRoot);
		}	
		SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils
		.getApplicationContext().getBean("siSystemLogService");
		//===============複製並刪除欲上傳的檔案===========================
		for (int i = 0; i < baseFiles.length; i++) {
			String fileName = null;
			try{
				fileName = baseFiles[i].getName().toUpperCase();    
				if (fileName.startsWith("M") || fileName.toUpperCase().startsWith("S")){
					FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
					FileUtils.copyFileToDirectory(baseFiles[i], msRoot);
					baseFiles[i].delete();
				}else if(fileName.startsWith("R") || fileName.toUpperCase().startsWith("T")){
					FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
					FileUtils.copyFileToDirectory(baseFiles[i], rtRoot);
					baseFiles[i].delete();
				}
			}catch(Exception ex){
				errorMsg = "複製並刪除" + fileName + "失敗，原因：" + ex.toString();
				log.error(errorMsg);
				siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);   
			}
		}
		//=========================MS==================================
		File[] msFiles = msRoot.listFiles();
		for(int i = 0; i < msFiles.length; i++){
			String srcFileName = null;
			try{
				srcFileName = msFiles[i].getName();
				boolean isLineFileExist = true;
				if (srcFileName.toUpperCase().startsWith("M")) {
					String headFileName = srcFileName;
					String lineFileName = "S" + srcFileName.substring(1);
					File lineFile = new File(msFolder + "/" + lineFileName);
					if(!lineFile.exists()){
						lineFileName = "s" + srcFileName.substring(1);
						lineFile = new File(msFolder + "/" + lineFileName);
						if(!lineFile.exists()){
							isLineFileExist = false;
						}
					}

					if(isLineFileExist){
						try{
							uiProperties.put("actualRootPath", msFolder);
							posMovDbfFileProcess(headFileName, lineFileName, uiProperties);
						}catch(Exception ex){
							errorMsg = "POS調撥匯入" + headFileName + "、" + lineFileName + "失敗，原因：" +  ex.toString();
							log.error(errorMsg);
							siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
						}		    
					}else{
						errorMsg = "POS調撥匯入" + headFileName + "失敗，沒有相對應的明細檔：" +  lineFileName;
						log.error(errorMsg);
						siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
					}		
				}
			}catch(Exception ex){
				errorMsg = "POS調撥匯入" + srcFileName + "失敗，原因：" +  ex.toString();
				log.error(errorMsg);
				siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
			}
		}
		//=========================RT=====================================
		File[] rtFiles = rtRoot.listFiles();
		for(int i = 0; i < rtFiles.length; i++){
			String srcFileName = null;
			try{
				srcFileName = rtFiles[i].getName();
				boolean isLineFileExist = true;
				if (srcFileName.toUpperCase().startsWith("R")) {
					String headFileName = srcFileName;
					String lineFileName = "T" + srcFileName.substring(1);
					File lineFile = new File(rtFolder + "/" + lineFileName);
					if(!lineFile.exists()){
						lineFileName = "t" + srcFileName.substring(1);
						lineFile = new File(rtFolder + "/" + lineFileName);
						if(!lineFile.exists()){
							isLineFileExist = false;
						}
					}

					if(isLineFileExist){
						try{
							uiProperties.put("actualRootPath", rtFolder);
							posMovDbfFileProcess(headFileName, lineFileName, uiProperties);
						}catch(Exception ex){
							errorMsg = "POS調撥匯入" + headFileName + "、" + lineFileName + "失敗，原因：" +  ex.toString();
							log.error(errorMsg);
							siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
						}		    
					}else{
						errorMsg = "POS調撥匯入" + headFileName + "失敗，沒有相對應的明細檔：" +  lineFileName;
						log.error(errorMsg);
						siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
					}		
				}
			}catch(Exception ex){
				errorMsg = "POS調撥匯入" + srcFileName + "失敗，原因：" +  ex.toString();
				log.error(errorMsg);
				siSystemLogService.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
			}
		}	
	}

	// 整批檔案轉檔 轉入ERP系統
	public String folderTransfer(HashMap uiProperties) throws Exception {

		SiSystemLogService siSystemLogService = null;
		String result = "銷售上傳完成 ";
		String processName = "BATCH_IMPORT";
		String subProcessName = null;
		String uuid = null;
		String brandCode = null;
		String loginUser = null;
		String msg = null;
		Date executeDate = new Date();
		try {
			log.info("LCMSDoc.folderTransfer baseFolder=" + baseFolder
					+ ",module =" + module + ",uiProperties="
					+ uiProperties.toString());
			// 遠端+Batch->SRC
			String dts[] = new String[1];
			String specialParam = (String) uiProperties.get(SystemConfig.SPRCIAL_PARAM);
			User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
			uuid = UUID.randomUUID().toString();
			uiProperties.put("actualExecuteDate", executeDate);
			uiProperties.put("uuidCode", uuid);
			String autoJobControl = (String)uiProperties.get("autoJobControl");
			String transferFolder = (String)uiProperties.get("transferFolder");
			if (null != userObj) {
				loginUser = userObj.getEmployeeCode();
				brandCode = userObj.getBrandCode();
			}
			if(StringUtils.hasText(brandCode)){
				brandCode = brandCode.trim().toUpperCase();    
			}else{
				throw new Exception("無法取得欲執行匯入的品牌代號！");
			}
			//===============記錄批次匯入程序起始==================
			siSystemLogService = (SiSystemLogService) SpringUtils.getApplicationContext().getBean("siSystemLogService");
			siSystemLogService.createSystemLog(processName, MessageStatus.LOG_INFO, "批次匯入程序開始執行...", executeDate, uuid, loginUser);
			//==================================================

			if (StringUtils.hasText(specialParam)) {
				dts = specialParam.split(",");
			} else {
				Date yesterDay = DateUtils.addDays(new Date(), -1);
				String oneDt = DateUtils.formatChangeToTWDate(yesterDay);
				dts[0] = oneDt;
			}

			BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
			.getApplicationContext().getBean("buCommonPhraseService");
			BuCommonPhraseLine bucpl = buCommonPhraseService.getBuCommonPhraseLine("ImportDBType", module);
			if(bucpl == null){
				throw new Exception("查詢" + module + "的匯入路徑失敗！");
			}

			String[] sFiles = new String[5]; // 拿取設在DB中Parameter1~5的遠端位置
			sFiles[0] = bucpl.getParameter1();
			sFiles[1] = bucpl.getParameter2();
			sFiles[2] = bucpl.getParameter3();
			sFiles[3] = bucpl.getParameter4();
			sFiles[4] = bucpl.getParameter5();

			List<String> importFilePath = new ArrayList();
			if("tw.com.tm.erp.importdb.POSMOVImportData".equals(module)){//百貨調撥匯入
				if("T1BS".equals(brandCode)){
					importFilePath.add(sFiles[1]);
				}else if("T1CO".equals(brandCode)){
					importFilePath.add(sFiles[2]);
					//importFilePath.add(sFiles[3]);
				}else if("T1GS".equals(brandCode)){
					importFilePath.add(sFiles[4]);
				}else if("T1HC".equals(brandCode)){
					importFilePath.add(sFiles[0]);
				}
				subProcessName = brandCode + "_" + SiPosLogService.PROCESS_NAME_MOVE_UP;
				try{
					SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "調撥資料上傳....", executeDate,
							uuid, loginUser);
					uiProperties.put(ImportDBService.PRCCESS_NAME, subProcessName);
					for(int i = 0; i < importFilePath.size(); i++){
						String importFileRootPath = importFilePath.get(i);
						if (StringUtils.hasText(importFileRootPath)) {
							log.info("importFileRootPath = " + importFileRootPath);
							File rootFolder = new File(importFileRootPath + UP_FOLDER);
							if (rootFolder.exists()) {
								//取得路徑下所有檔案
								File[] filesRmtAll = rootFolder.listFiles();
								performPOSMovementImport(filesRmtAll, importFileRootPath, uiProperties);
							}else {
								msg = "請確認檔案上傳路徑是否正確路徑:" + importFileRootPath + UP_FOLDER;
								result += msg;
								log.error(msg);
								siSystemLogService.createSystemLog(subProcessName, MessageStatus.ERROR,  msg, executeDate, uuid, loginUser);		    
							}
						}			
					}	    
				}catch(Exception ex){
					msg = "執行" + brandCode + "調撥資料上傳失敗，原因：" + ex.toString();
					result += msg;
					log.error(msg);
					siSystemLogService.createSystemLog(subProcessName, MessageStatus.ERROR,  msg + ex.toString(), executeDate, uuid, loginUser);   
				}finally {
					siSystemLogService.createSystemLog(subProcessName, MessageStatus.LOG_INFO,  brandCode + "調撥資料上傳程序結束...", executeDate, uuid, loginUser);   
				}		
			}else if("tw.com.tm.erp.importdb.POSImportData".equals(module)){//百貨業績匯入
				if("T1BS".equals(brandCode)){
					importFilePath.add(sFiles[1]);
				}else if("T1CO".equals(brandCode)){
					importFilePath.add(sFiles[2]);
					importFilePath.add(sFiles[3]);
				}else if("T1GS".equals(brandCode)){
					importFilePath.add(sFiles[4]);
				}else if("T1HC".equals(brandCode)){
					importFilePath.add(sFiles[0]);
				}			
				subProcessName = brandCode + "_" + SiPosLogService.PROCESS_NAME_SOP_UP;
				try{
					SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, "開始執行" + brandCode + "業績資料上傳....", executeDate,
							uuid, loginUser);
					//==============================建立過帳記錄======================================
					SoPostingTallyService postingTallyService = (SoPostingTallyService)SpringUtils.getApplicationContext().getBean("soPostingTallyService");
					postingTallyService.createPostingTallyWithRangeOfDate(DateUtils.addDays(executeDate, -1), brandCode, loginUser);
					//==============================================================================
					uiProperties.put(ImportDBService.PRCCESS_NAME, subProcessName);
					for(int i = 0; i < importFilePath.size(); i++){
						String importFileRootPath = importFilePath.get(i);
						if (StringUtils.hasText(importFileRootPath)) {
							log.info("importFileRootPath = " + importFileRootPath);
							File rootFolder = new File(importFileRootPath + UP_FOLDER);
							if (rootFolder.exists()) {
								//取得路徑下所有檔案
								File[] filesRmtAll = rootFolder.listFiles();
								performSOPImport(filesRmtAll, importFileRootPath, uiProperties);
							}else {
								msg = "請確認檔案上傳路徑是否正確路徑:" + importFileRootPath + UP_FOLDER;
								result += msg;
								log.error(msg);
								siSystemLogService.createSystemLog(subProcessName, MessageStatus.ERROR,  msg, executeDate, uuid, loginUser);		    
							}
						}			
					}	    
				}catch(Exception ex){
					msg = "執行" + brandCode + "業績資料上傳失敗，原因：" + ex.toString();
					result += msg;
					log.error(msg);
					siSystemLogService.createSystemLog(subProcessName, MessageStatus.ERROR,  msg + ex.toString(), executeDate, uuid, loginUser);   
				}finally {
					siSystemLogService.createSystemLog(subProcessName, MessageStatus.LOG_INFO,  brandCode + "業績資料上傳程序結束...", executeDate, uuid, loginUser);   
				}		
			}else if("tw.com.tm.erp.importdb.T2PosImportData".equals(module)){	//T2業績上傳	
				String actualImportFilePaths [] = sFiles[0].split(","); 
				
				for(int i=0;i<actualImportFilePaths.length;i++){
					String actualImportFilePath = actualImportFilePaths[i];
					if(StringUtils.hasText(actualImportFilePath) && StringUtils.hasText(transferFolder)){
						actualImportFilePath = actualImportFilePath + transferFolder;
					}		
					importFilePath.add(actualImportFilePath);
				}
				
				String backupPath = sFiles[1];
				subProcessName = brandCode + "_" + SiPosLogService.PROCESS_NAME_SOP_UP;
				
				Date transactionShortDate = DateUtils.getShortDate(executeDate);
					
				//Date transactionShortDate = DateUtils.getShortDate(DateUtils.addDays(executeDate, -1));
				//if("MP".equalsIgnoreCase(transferFolder)){
					//transactionShortDate = DateUtils.getShortDate(executeDate);
				//}
				String importType = brandCode;
				String jobCheckMsg = "";
				if(StringUtils.hasText(transferFolder)){
					importType += "(" + transferFolder + ")業績資料上傳";
					jobCheckMsg = transferFolder;
				}else{
					importType += "業績資料上傳";
				}

				try{
					SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, "開始執行" + importType + "....", executeDate,
							uuid, loginUser);
					//==============================建立過帳記錄======================================
					try{
						SoPostingTallyService postingTallyService = (SoPostingTallyService)SpringUtils.getApplicationContext().getBean("soPostingTallyService");
						//postingTallyService.createT2PostingTallyWithRangeOfDate(transactionShortDate, brandCode, loginUser);
					}catch(Exception ex1){
						msg = "執行" + importType + "程序建立過帳記錄失敗，原因：" + ex1.toString();
						result += msg;
						log.error(msg);
						SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.ERROR,  msg + ex1.toString(), executeDate, uuid, loginUser);
					}
					
					//==============================================================================
					if(!StringUtils.hasText(transferFolder)){
						throw new Exception("請確認設定檔中是否有設定檔案上傳資料夾！");
					}
					
					uiProperties.put(ImportDBService.PRCCESS_NAME, subProcessName);
					for(int i = 0; i < importFilePath.size(); i++){
						String importFileRootPath = importFilePath.get(i);
						if (StringUtils.hasText(importFileRootPath)) {
							log.info("importFileRootPath = " + importFileRootPath);
							File rootFolder = new File(importFileRootPath);
							if (rootFolder.exists()) {
								//取得路徑下所有檔案
								File[] filesRmtAll = rootFolder.listFiles();
								performT2SOPImport(filesRmtAll, importFileRootPath, backupPath, uiProperties);
							}else {
								msg = "請確認檔案上傳路徑是否正確路徑:" + importFileRootPath;
								result += msg;
								log.error(msg);
								SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.ERROR,  msg, executeDate, uuid, loginUser);		    
							}
						}			
					}
				}catch(Exception ex){
					msg = "執行" + importType + "失敗，原因：" + ex.toString();
					result += msg;
					log.error(msg);
					SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.ERROR,  msg + ex.toString(), executeDate, uuid, loginUser);   
				}finally {
					SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.LOG_INFO,  importType + "程序結束...", executeDate, uuid, loginUser);
					//=========================寫入JOB_CHECKING=========================
					if("Y".equals(autoJobControl) && StringUtils.hasText(transferFolder)){
						Date posFinishDate = new Date();
						SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, "開始執行" + jobCheckMsg + "寫入JOB_CHECKING....", executeDate, uuid, loginUser);
						JobCheckingService jobCheckingService = (JobCheckingService)SpringUtils.getApplicationContext().getBean("jobCheckingService");
						jobCheckingService.triggerReportJob(transactionShortDate, "SO", posFinishDate, subProcessName, executeDate, uuid, loginUser, transferFolder, autoJobControl);
						jobCheckingService.triggerReportJob(transactionShortDate, "IM", posFinishDate, subProcessName, executeDate, uuid, loginUser, transferFolder, autoJobControl);		
						jobCheckingService.triggerReportJob(transactionShortDate, "DEL", posFinishDate, subProcessName, executeDate, uuid, loginUser, transferFolder, autoJobControl);//add by joeywu 20100701 for 重轉資料時，啟動report重作			
						SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, jobCheckMsg + "寫入JOB_CHECKING程序結束....", executeDate, uuid, loginUser);	
					}
				}		
			}else{
				for (int index = 0; index < dts.length; index++) {
					String dt = dts[index];
					for (int s = 0; s < sFiles.length; s++) {
						String sFile = sFiles[s];
						// log.info("測試要執行的 module=" + module + ";資料的來源
						// sFile=" + sFile);
						if (StringUtils.hasText(sFile)) {
							File fileRmt = new File(sFile);
							if (fileRmt.exists()) {
								File[] filesRmt;
								// 依不同的module,對檔案做不同處理
								// log.info("判斷要執行的 module=" + module);
								filesRmt = fileRmt.listFiles();
								// COPY 符合命名規則的檔案 DB中設定的網路磁碟機->batchFolder
								for (int i = 0; i < filesRmt.length; i++) {
									// log.info("將資料由 來源 " + sFile + "/" +
									// filesRmt[i].getName() + ",目的" + batchFolder +
									// "/"
									// + filesRmt[i].getName());
									FileTools.CopyFile(sFile + "/" + filesRmt[i].getName(), batchFolder + "/" + filesRmt[i].getName());
								}

								//20090202 shan 複製FAIL的資料重轉
								List failFiles = new ArrayList();
								FileTools.listAllSubDirectoryFiles(failFolder,failFiles);
								for(int i = 0 ; i < failFiles.size() ; i++){
									File failFile = (File)failFiles.get(i);
									File toFile = new File(batchFolder + "/" + failFile.getName());
									FileTools.MoveFile(failFile,toFile);
								}							

							} else {
								// write log
								throw new Exception("請確認檔案上傳路徑是否正確 路徑:" + sFile);
							}
						} else {
							// write log
						}
					}
				}
				//=====================KK=========================
				File file = new File(batchFolder);
				if (file.exists()) {
					File[] files = file.listFiles();
					if (files.length > 0) {		     
						for (int i = 0; i < files.length; i++) {
							String srcFilename = files[i].getName();
							if (module.equals("tw.com.tm.erp.importdb.CmDeclarationImportData")) { // 報關單
								String destFilename = srcFilename;
								BufferedReader fr;
								BufferedWriter fw;
								String getfr;
								destFilename = srcFilename + ".txt";
								String resultString = new String();

								/** ** open file ******** */
								fr = new BufferedReader(new FileReader(batchFolder + "/" + srcFilename));
								fw = new BufferedWriter(new FileWriter(destFolder + "/" + destFilename));
								while (fr.ready())// 如果檔案沒有讀完，就繼續處理
								{
									getfr = fr.readLine(); // 取得一行輸入
									char fun = getfr.charAt(1);
									resultString = "";
									switch (fun) {
									case '1':
										resultString = getT1(getfr);
										break;
									case '2':
										resultString = getT2(getfr);
										break;
									case '3':
										resultString = getT3(getfr);
										break;
									case '4':
										resultString = getT4(getfr);
										break;
									default:
										break;
									}
									if (resultString.equals("Size Error")) {
										result = "檔案內容不符合規範";
									}
									StringBuffer strbr = new StringBuffer(resultString);
									fw.write(strbr.toString()); // 寫入檔案
									fw.newLine();
								}
								fr.close(); // close file
								fw.close(); // close file
								fr = new BufferedReader(new FileReader(
										batchFolder + "/" + srcFilename));
								fw = new BufferedWriter(new FileWriter(
										srcFolder + "/" + srcFilename));
								while (fr.ready())// 如果檔案沒有讀完，就繼續處理
								{
									getfr = fr.readLine(); // 取得一行輸入
									StringBuffer strbr = new StringBuffer(getfr);
									fw.write(strbr.toString()); // 寫入檔案
									fw.newLine();
								}
								fr.close(); // close file
								fw.close(); // close file
								result = result + fileProcess(destFilename, module, uiProperties);

								File sourceFile = new File(batchFolder + "/" + srcFilename);
								sourceFile.delete();
							}
						}
					} else {
						log.error("LCMSDoc.folderTransfer 路徑中查無檔案 " + batchFolder);
					}
				} else {
					log.error("LCMSDoc.folderTransfer 查無路徑 " + batchFolder);
				}
				//===========================		
			}	    
		} catch (Exception ex) {
			msg = "執行批次轉檔失敗，原因：" + ex.toString();
			result = result += msg;
			log.error(msg);
			siSystemLogService.createSystemLog(processName, MessageStatus.ERROR,  msg + ex.toString(), executeDate, uuid, loginUser);   
			
		}finally {
			siSystemLogService.createSystemLog(processName, MessageStatus.LOG_INFO,  "批次匯入程序結束...", executeDate, uuid, loginUser);   
		}
		return result;
	}

	private String posFileProcess(String sourceFileName, String infoClassName, HashMap uiProperties){

		log.info("LCMSDoc.fileProcess sourceFileName=" + sourceFileName);
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		String brandCode = userObj.getBrandCode();
		String result = "檔案匯入結果:";
		String combineFilePath = (String)uiProperties.get(ImportDBService.COMBINE_FILE_PATH);
		//================================================
		FileTools.setDEFAULTFILEENCODING(FileTools.SYSTEMENCODING);
		String tmp = FileTools.ReadFromFile(combineFilePath);
		String baseFilePath = destFolder + "/" + brandCode + "/UTF8_" + sourceFileName;
		FileTools.WriteToFile(baseFilePath, tmp, "UTF-8");
		uiProperties.put(ImportDBService.BASE_FILE_PATH, baseFilePath);
		//================================================
		try {
			ImportDBService serv = (ImportDBService) SpringUtils.getApplicationContext().getBean("importDBService");
			String resultMsg = serv.importDB(baseFilePath, infoClassName, uiProperties);    
			result += resultMsg;
		} catch (Exception e) {
			result += "失敗(" + e.toString() + ")";
		}
		return result;
	}
	public String fileProcess(String sourceFileName, String infoClassName, HashMap uiProperties) throws java.io.IOException { // 檔案搬移及後續呼叫
		log.info("LCMSDoc.fileProcess sourceFileName=" + sourceFileName + ",infoClassName=" + infoClassName);
		// 1.報關單:DEST->Success(/Fail)
		// 2.其他:SRC->DEST->Success(/Fail)
		String result = "檔案匯入結果:";
		// ImportDBService serv = new ImportDBService();
		if (!infoClassName.equals("tw.com.tm.erp.importdb.CmDeclarationImportData")) {
			// log.info("Copy from " + srcFolder + "/" +
			// sourceFileName + " to " + destFolder + "/" + sourceFileName);
			String destFilename = sourceFileName;
			if (sourceFileName.toLowerCase().endsWith(".xls")) {
				FileTools.CopyFile(srcFolder + "/" + sourceFileName, destFolder + "/" + destFilename);
			} else {
				String getfr;
				/** ** open file ******** */
				BufferedReader fr0 = new BufferedReader(new FileReader(srcFolder + "/" + sourceFileName));
				BufferedWriter fw0 = new BufferedWriter(new FileWriter(destFolder + "/" + destFilename));
				while (fr0.ready())// 如果檔案沒有讀完，就繼續處理
				{
					getfr = fr0.readLine(); // 取得一行輸入
					StringBuffer strbr = new StringBuffer(getfr);
					fw0.write(strbr.toString()); // 寫入檔案
					fw0.newLine();
				}
				fr0.close(); // close file
				fw0.close(); // close file
			}
		}
		File sourceFile;
		if (sourceFileName.toLowerCase().endsWith(".xls")) {
			sourceFile = new File(destFolder + "/" + sourceFileName);
		} else {
			FileTools.setDEFAULTFILEENCODING(FileTools.SYSTEMENCODING);
			String tmp = FileTools.ReadFromFile(destFolder + "/" + sourceFileName);
			// log.info("tmp="+tmp);
			FileTools.WriteToFile(destFolder + "/UTF8_" + sourceFileName, tmp, "UTF-8");
			sourceFile = new File(destFolder + "/" + sourceFileName);
			sourceFile.delete();
			sourceFileName = "UTF8_" + sourceFileName;
		}

		String outFileName = "";
		try {
			ImportDBService serv = (ImportDBService) SpringUtils.getApplicationContext().getBean("importDBService");
			// log.info("filePath=" + destFolder + "/" +
			// sourceFileName);
			String resultMsg = serv.importDB(destFolder + "/" + sourceFileName, infoClassName, uiProperties);
			result = result + resultMsg;
			// log.info("檔案匯入結果 " + resultMsg);
			outFileName = successFolder + "/" + sourceFileName;
		} catch (Exception e) {
			result = result + "失敗(" + e.toString() + ")";
			outFileName = failFolder + "/" + sourceFileName;
		}
		FileTools.CopyFile(destFolder + "/" + sourceFileName, outFileName);

		sourceFile = new File(destFolder + "/" + sourceFileName);
		sourceFile.delete();
		return result;
	}

	// 報關單(ImDistributionImportData)
	public String xlsFileProcess(String fileName, String infoClassName,
			HashMap uiProperties) throws Exception {
		log.info("LCMSDoc.xlsFileProcess fileName=" + fileName
				+ ",infoClassName=" + infoClassName);
		String ORDERNO = "";
		List Shops = new ArrayList();
		List Items = new ArrayList();
		List Quanities = new ArrayList();
		List resultAllDatas = new ArrayList();
		String resultMsg = "檔案匯入結果:";
		String sourceFileName = "";
		String outFileName = "";
		try {
			File importFile = new File(fileName);
			if (importFile.exists()) {
				sourceFileName = importFile.getName();
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
						fileName));
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				if (null != sheet) {
					Iterator rows = sheet.rowIterator();
					if (rows.hasNext()) {
						HSSFRow row = (HSSFRow) rows.next();
						Iterator cells = row.cellIterator();
						if (cells.hasNext()) {
							HSSFCell cell = (HSSFCell) cells.next();
							try {
								ORDERNO = (String) ExcelUtils.getCellData(cell);
							} catch (Exception ex) {
								throw new Exception("配貨單號有問題");
							}
							List tmpData = new ArrayList();
							tmpData.add("T0");
							tmpData.add(ORDERNO);
							resultAllDatas.add(tmpData);
						}
						while (cells.hasNext()) {
							HSSFCell cell = (HSSFCell) cells.next();
							Shops.add(ExcelUtils.getCellData(cell));
						}
					} else {
						resultMsg = resultMsg + "失敗(Excel Sheet 是空白)";
						throw new Exception("Excel Sheet 是空白" + fileName);
					}
					while (rows.hasNext()) {
						HSSFRow row = (HSSFRow) rows.next();
						Iterator cells = row.cellIterator();

						if (cells.hasNext()) {
							HSSFCell cell = (HSSFCell) cells.next();
							Items.add(ExcelUtils.getCellData(cell));
						}

						cells.next();

						List quanty = new ArrayList();
						while (cells.hasNext()) {
							HSSFCell cell = (HSSFCell) cells.next();
							quanty.add(String.valueOf(ExcelUtils
									.getCellData(cell)));
						}
						Quanities.add(quanty);
					}

				} else {
					resultMsg = resultMsg + "失敗(Excel Sheet 不符合規範)";
					throw new Exception("Excel Sheet 不符合規範" + fileName);
				}
			} else {
				resultMsg = resultMsg + "失敗(匯入檔案找不到)";
				throw new Exception("匯入檔案找不到" + fileName);
			}
			for (int i = 0; i < Items.size(); i++) {
				List quanty = (ArrayList) Quanities.get(i);
				for (int j = 1; j < Shops.size(); j++) {
					List tmpData = new ArrayList();
					tmpData.add("T1");
					tmpData.add(Items.get(i));
					tmpData.add(Shops.get(j));
					tmpData.add(String.valueOf(quanty.get(j - 1)));
					resultAllDatas.add(tmpData);
				}
			}

			ImportDBService serv = (ImportDBService) SpringUtils
			.getApplicationContext().getBean("importDBService");
			resultMsg = resultMsg
			+ serv
			.importDB(resultAllDatas, infoClassName,
					uiProperties);
			outFileName = successFolder + "/" + sourceFileName;
		} catch (Exception e) {
			resultMsg = resultMsg + "失敗(" + e.toString() + ")";
			outFileName = failFolder + "/" + sourceFileName;
		}
		FileTools.CopyFile(fileName, outFileName);
		return resultMsg;
	}

	// POS(POSImportData) Head & Line csv檔案處理
	public String csvFileProcess(String headFileName, String lineFileName,
			HashMap uiProperties) throws java.io.IOException {
		log.info("LCMSDoc.csvFileProcess headFileName=" + headFileName
				+ ",lineFileName=" + lineFileName);
		String result = "";
		// ImportDBService serv = new ImportDBService();
		try {
			String combineFileName = String.valueOf(new Date().getTime())
			+ ".csv";
			log.info("Combine from " + srcFolder + "/" + headFileName
					+ "& " + lineFileName + "to " + srcFolder + "/"
					+ combineFileName);

			String getfr;
			String MSNO = "";
			String MDATE = "";
			String MSHOPNO = "";
			String MSEQNO = "";
			/** ** open file ******** */
			BufferedReader fr0 = new BufferedReader(new FileReader(srcFolder
					+ "/" + headFileName));
			if (fr0.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr0.readLine(); // 取得一行輸入
				MSNO = getfr.split(",")[1];
				MDATE = getfr.split(",")[2];
				MSHOPNO = getfr.split(",")[3];
			}
			fr0.close(); // close file

			BufferedReader fr = null;
			BufferedWriter fw = null;
			double taxSum = 0D;
			double MSLTOTSum = 0;
			double saleAmountSum = 0;
			String[] res = new String[3];
			HashMap sum = new HashMap();
			fr = new BufferedReader(new FileReader(srcFolder + "/"
					+ lineFileName));
			/** ** open file ******** */
			while (fr.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr.readLine(); // 取得一行輸入
				// StringBuffer strbr=new StringBuffer(getfr);
				String[] tokens = getfr.split(",");
				if (tokens[1].equals(MSNO)) {
					// int MQTY = java.lang.Integer.parseInt(tokens[5]);
					double MQTY = Double.valueOf(tokens[5]).doubleValue();
					if ((MQTY == 0)
							&& (Double.valueOf(tokens[7]).doubleValue() < 0)) {
						MQTY = -1;
					} else {
						MQTY = 1;
					}
					if (tokens[11].equals(MSEQNO)) {
						taxSum = taxSum
						+ Double.valueOf(tokens[7]).doubleValue()
						* 0.05;
						MSLTOTSum = MSLTOTSum
						+ Double.valueOf(tokens[7]).doubleValue();
						saleAmountSum = saleAmountSum
						+ Double.valueOf(tokens[13]).doubleValue()
						* MQTY;
					} else {
						res = new String[3];
						res[0] = String.valueOf(taxSum);
						res[1] = String.valueOf(saleAmountSum);
						res[2] = String.valueOf(MSLTOTSum);
						// log.info(MSEQNO+":Sum="+res[0]+";"+res[1]+";"+res[2]);
						sum.put(MSEQNO, res); // 寫入前一MSEQNO的Sum
						taxSum = Double.valueOf(tokens[7]).doubleValue() * 0.05;
						MSLTOTSum = Double.valueOf(tokens[7]).doubleValue();
						saleAmountSum = Double.valueOf(tokens[13])
						.doubleValue()
						* MQTY;
					}
					MSEQNO = tokens[11];
				}
			}// while fr.ready()
			fr.close(); // close file
			res = new String[3];
			res[0] = String.valueOf(taxSum);
			res[1] = String.valueOf(saleAmountSum);
			res[2] = String.valueOf(MSLTOTSum);
			// log.info(MSEQNO+":Fin
			// Sum="+res[0]+";"+res[1]+";"+res[2]);
			sum.put(MSEQNO, res); // 因寫入前一MSEQNO的Sum,所以必須寫最後一MSEQNO

			MSEQNO = "";
			fr = new BufferedReader(new FileReader(srcFolder + "/"
					+ lineFileName));
			fw = new BufferedWriter(new FileWriter(srcFolder + "/"
					+ combineFileName));
			/** ** open file ******** */
			while (fr.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr.readLine(); // 取得一行輸入
				String[] tokens = getfr.split(",");
				if (tokens[1].equals(MSNO)) {
					double MQTY = Double.valueOf(tokens[5]).doubleValue();
					if ((MQTY == 0)
							&& (Double.valueOf(tokens[7]).doubleValue() < 0)) {
						MQTY = -1;
					} else {
						MQTY = 1;
					}
					String strDbr = " ";
					String strHbr = " ";
					// String MVTYPE = tokens[12];
					String MTRREM = " ";
					if (!getfr.endsWith(",")) {
						MTRREM = tokens[17];
					}
					/**
					 * if (MVTYPE.equals("0")) { MVTYPE = "4"; } else if
					 * (MVTYPE.equals("1")) { MVTYPE = "5"; } else { MVTYPE =
					 * "1"; }
					 */
					// String seq=tokens[11];
					if (!tokens[11].equals(MSEQNO)) {
						// res=(String [])sum.get(seq);
						// log.info("sum="+seq+";"+res[0]+";"+res[1]+";"+res[2]);
						strHbr = "S0,SOP," + tokens[12] + "," + MDATE + ","
						+ tokens[10] + "," + MSNO + ",Z9,TW,TWD,"
						+ MSHOPNO + "," + tokens[9] + ",2,3,5,"
						+ ((String[]) sum.get(tokens[11]))[0] + ","
						+ MDATE + "," + MDATE + ","
						+ ((String[]) sum.get(tokens[11]))[1] + ","
						+ ((String[]) sum.get(tokens[11]))[2] + ","
						+ tokens[11] + ",Y," + MTRREM + ",SIGNING,"
						+ tokens[9] + "," + tokens[9];
						fw.write(strHbr); // 寫入檔案
						fw.newLine();
					}
					strDbr = "S1,"
						+ tokens[3]
						         + ","
						         + tokens[13]
						                  + ","
						                  + String.valueOf(MQTY)
						                  + ","
						                  + String.valueOf(Double.valueOf(tokens[13])
						                		  .doubleValue()
						                		  * MQTY)
						                		  + ","
						                		  + String.valueOf(100 - Double.valueOf(tokens[6])
						                				  .doubleValue())
						                				  + ","
						                				  + tokens[4]
						                				           + ","
						                				           + tokens[7]
						                				                    + ","
						                				                    + MDATE
						                				                    + ","
						                				                    + MDATE
						                				                    + ",Y,3,5,"
						                				                    + String.valueOf(Double.valueOf(tokens[7])
						                				                    		.doubleValue() * 0.05) + ",SIGNING,"
						                				                    		+ tokens[9] + "," + tokens[9];
					fw.write(strDbr); // 寫入檔案
					fw.newLine();
					MSEQNO = tokens[11];
				}
			}// while fr.ready()
			fr.close(); // close file
			fw.close(); // close file
			result = fileProcess(combineFileName, module, uiProperties);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
		}
		return result;
	}

	// POS(POSImportData) Head & Line dbf檔案處理
	public String dbfFileProcess(String headFileName, String lineFileName, HashMap uiProperties) throws java.io.IOException {
		log.info("LCMSDoc.dbfFileProcess headFileName=" + headFileName + ",lineFileName=" + lineFileName);
		uiProperties.put("headFileName", headFileName);//設置匯入的headFileName
		String result = "";
		// ImportDBService serv = new ImportDBService();
		try {
			String combineFileName = String.valueOf(new Date().getTime()) + ".csv";
			log.info("Combine from " + srcFolder + "/" + headFileName + "& " + lineFileName + "to " + srcFolder + "/"
					+ combineFileName);

			String MSNO = "";
			String MDATE = "";
			String MSHOPNO = "";
			String MSEQNO = "";

			List<Object[]> headRecords = DBFUtils.readDBF(srcFolder + "/" + headFileName);
			List<Object[]> lineRecords = DBFUtils.readDBF(srcFolder + "/" + lineFileName);
			for (int i = 0; i < headRecords.size(); i++) {
				Object[] heads = headRecords.get(i);
				MSNO = heads[0].toString().trim();
				MDATE = heads[1].toString().trim();
				MSHOPNO = heads[2].toString().trim();
			}
			uiProperties.put("msNo", MSNO);//設置msno
			uiProperties.put("fileSalesDate", MDATE);//設置匯入的銷售日期
			uiProperties.put("fileShopCode", MSHOPNO);//設置匯入的專櫃


			double taxSum = 0D;
			double MSLTOTSum = 0D;
			double saleAmountSum = 0D;
			String[] res = new String[3];
			HashMap sum = new HashMap();

			for (int i = 0; i < lineRecords.size(); i++) {
				Object[] tmps = lineRecords.get(i);
				String[] tokens = new String[tmps.length];
				for (int j = 0; j < tmps.length; j++) {
					tokens[j] = tmps[j].toString();
				}
				if (tokens[0].trim().equals(MSNO)) {
					double MQTY = Double.valueOf(tokens[4].trim()).doubleValue();
					// 20090108 shan fix
					// int MQTY = java.lang.Integer.parseInt(tokens[4]);
					if (MQTY == 0D) {
						if (Double.parseDouble(tokens[6].trim()) < 0D) {
							MQTY = -1D;
						} else {
							MQTY = 1D;
						}
					}

					if (tokens[10].trim().equals(MSEQNO)) {
						taxSum = taxSum + (java.lang.Double.parseDouble(tokens[6].trim())) * 0.05;
						MSLTOTSum = MSLTOTSum + java.lang.Double.parseDouble(tokens[6].trim());
						saleAmountSum = saleAmountSum + java.lang.Double.parseDouble(tokens[12].trim()) * MQTY;
					} else {
						res = new String[3];
						res[0] = String.valueOf(taxSum);
						res[1] = String.valueOf(saleAmountSum);
						res[2] = String.valueOf(MSLTOTSum);
						// log.info(MSEQNO+":Sum="+res[0]+";"+res[1]+";"+res[2]);
						sum.put(MSEQNO, res); // 寫入前一MSEQNO的Sum
						taxSum = java.lang.Double.parseDouble(tokens[6].trim()) * 0.05;
						MSLTOTSum = java.lang.Double.parseDouble(tokens[6].trim());
						saleAmountSum = java.lang.Double.parseDouble(tokens[12].trim()) * MQTY;
					}
					MSEQNO = tokens[10].trim();
				}
			}

			res = new String[3];
			res[0] = String.valueOf(taxSum);
			res[1] = String.valueOf(saleAmountSum);
			res[2] = String.valueOf(MSLTOTSum);
			sum.put(MSEQNO, res); // 因寫入前一MSEQNO的Sum,所以必須寫最後一MSEQNO

			MSEQNO = "";
			BufferedWriter fw = new BufferedWriter(new FileWriter(srcFolder + "/" + combineFileName));

			for (int i = 0; i < lineRecords.size(); i++) {
				Object[] tmps = lineRecords.get(i);
				String[] tokens = new String[tmps.length];
				for (int j = 0; j < tmps.length; j++) {
					tokens[j] = tmps[j].toString();
				}
				if (tokens[0].trim().equals(MSNO)) {
					double MQTY = Double.valueOf(tokens[4].trim()).doubleValue();
					// 20090108 shan fix
					// int MQTY = java.lang.Integer.parseInt(tokens[4]);
					if (MQTY == 0D) {
						if (Double.parseDouble(tokens[6].trim()) < 0D) {
							MQTY = -1D;
						} else {
							MQTY = 1D;
						}
					}
					String strDbr = " ";
					String strHbr = " ";
					// String MVTYPE = tokens[12];
					String MTRREM = " ";
					if (tokens.length > 16) {
						MTRREM = tokens[16].trim();
					}
					/**
					 * if (MVTYPE.equals("0")) { MVTYPE = "4"; } else if
					 * (MVTYPE.equals("1")) { MVTYPE = "5"; } else { MVTYPE =
					 * "1"; }
					 */
					// String seq=tokens[11];
					if (!tokens[10].trim().equals(MSEQNO)) {
						// res=(String [])sum.get(seq);
						// log.info("sum="+seq+";"+res[0]+";"+res[1]+";"+res[2]);
						strHbr = "S0,SOP," + tokens[11].trim() + "," + MDATE + "," + tokens[9].trim() + "," + MSNO + ",Z9,TW,NTD,"
						+ MSHOPNO + "," + tokens[8].trim() + ",2,3,5," + ((String[]) sum.get(tokens[10].trim()))[0] + "," + MDATE
						+ "," + MDATE + "," + ((String[]) sum.get(tokens[10].trim()))[1] + ","
						+ ((String[]) sum.get(tokens[10].trim()))[2] + "," + tokens[10].trim() + ",Y,SIGNING,"
						+ tokens[8].trim() + "," + tokens[8].trim() + "," + headFileName;
						fw.write(strHbr); // 寫入檔案
						fw.newLine();
					}
					// 20090109 shan add field tokens[10] for watch
					strDbr = "S1," + tokens[2].trim() + "," + tokens[12].trim() + "," + String.valueOf(MQTY) + ","
					+ String.valueOf(Double.valueOf(tokens[12].trim()).doubleValue() * MQTY) + ","
					+ String.valueOf(Double.valueOf(tokens[5].trim()).doubleValue()) + "," + tokens[3].trim() + ","
					+ tokens[6].trim() + "," + MDATE + "," + MDATE + ",Y,3,5,"
					+ String.valueOf(Double.valueOf(tokens[6].trim()).doubleValue() * 0.05) + "," + tokens[8].trim() + ","
					+ tokens[8].trim() + "," + MTRREM;
					fw.write(strDbr); // 寫入檔案
					fw.newLine();
					MSEQNO = tokens[10].trim();
				}
			}

			fw.close(); // close file
			result = fileProcess(combineFileName, module, uiProperties);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
		}
		return result;
	}

	public String sopDbfFileProcess(String headFileName, String lineFileName, HashMap uiProperties) throws Exception {
		log.info("LCMSDoc.sopDbfFileProcess headFileName=" + headFileName + ",ineFileName" + lineFileName);
		uiProperties.put(ImportDBService.UPLOAD_HEAD_FILE_NAME, headFileName);
		uiProperties.put(ImportDBService.UPLOAD_LINE_FILE_NAME, lineFileName);
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		String brandCode = userObj.getBrandCode();
		String actualRootPath = (String)uiProperties.get("actualRootPath");
		String combineFileName = String.valueOf(new Date().getTime()) + ".csv";
		log.info("Combine from " + actualRootPath + "/" + headFileName
				+ "& " + lineFileName + "to " + destFolder + "/" + brandCode + "/"
				+ combineFileName);
		//=============轉成csv檔的原檔案路徑============================
		String fileName = destFolder + "/" + brandCode + "/" + combineFileName;
		uiProperties.put(ImportDBService.COMBINE_FILE_PATH, fileName);
		File combineFileFolder = new File(destFolder + "/" + brandCode);
		if(!combineFileFolder.exists()){
			FileUtils.forceMkdir(combineFileFolder);
		}
		//=============調撥head及line的原檔案路徑=======================
		String headFilePath = actualRootPath + "/" + headFileName;
		String lineFilePath = actualRootPath + "/" + lineFileName;
		uiProperties.put(ImportDBService.UPLOAD_HEAD_FILE_PATH, headFilePath);
		uiProperties.put(ImportDBService.UPLOAD_LINE_FILE_PATH, lineFilePath);	
		//===========================================================
		List<Object[]> headRecords = DBFUtils.readDBF(headFilePath);
		List<Object[]> lineRecords = DBFUtils.readDBF(lineFilePath);

		String MSNO = "";
		String MDATE = "";
		String MSHOPNO = "";
		String MSEQNO = "";
		for (int i = 0; i < headRecords.size(); i++) {
			Object[] heads = headRecords.get(i);
			MSNO = heads[0].toString().trim();
			MDATE = heads[1].toString().trim();
			MSHOPNO = heads[2].toString().trim();
		}
		uiProperties.put("msNo", MSNO);//設置msno
		uiProperties.put("fileSalesDate", MDATE);//設置匯入的銷售日期
		uiProperties.put("fileShopCode", MSHOPNO);//設置匯入的專櫃

		double taxSum = 0D;
		double MSLTOTSum = 0D;
		double saleAmountSum = 0D;
		String[] res = new String[3];
		HashMap sum = new HashMap();

		for (int i = 0; i < lineRecords.size(); i++) {
			Object[] tmps = lineRecords.get(i);
			String[] tokens = new String[tmps.length];
			for (int j = 0; j < tmps.length; j++) {
				tokens[j] = tmps[j].toString();
			}
			if (tokens[0].trim().equals(MSNO)) {
				double MQTY = Double.valueOf(tokens[4].trim()).doubleValue();
				if (MQTY == 0D) {
					if (Double.parseDouble(tokens[6].trim()) < 0D) {
						MQTY = -1D;
					} else {
						MQTY = 1D;
					}
				}

				if (tokens[10].trim().equals(MSEQNO)) {
					taxSum = taxSum + (java.lang.Double.parseDouble(tokens[6].trim())) * 0.05;
					MSLTOTSum = MSLTOTSum + java.lang.Double.parseDouble(tokens[6].trim());
					saleAmountSum = saleAmountSum + java.lang.Double.parseDouble(tokens[12].trim()) * MQTY;
				} else {
					res = new String[3];
					res[0] = String.valueOf(taxSum);
					res[1] = String.valueOf(saleAmountSum);
					res[2] = String.valueOf(MSLTOTSum);
					// log.info(MSEQNO+":Sum="+res[0]+";"+res[1]+";"+res[2]);
					sum.put(MSEQNO, res); // 寫入前一MSEQNO的Sum
					taxSum = java.lang.Double.parseDouble(tokens[6].trim()) * 0.05;
					MSLTOTSum = java.lang.Double.parseDouble(tokens[6].trim());
					saleAmountSum = java.lang.Double.parseDouble(tokens[12].trim()) * MQTY;
				}
				MSEQNO = tokens[10].trim();
			}
		}

		res = new String[3];
		res[0] = String.valueOf(taxSum);
		res[1] = String.valueOf(saleAmountSum);
		res[2] = String.valueOf(MSLTOTSum);
		sum.put(MSEQNO, res); // 因寫入前一MSEQNO的Sum,所以必須寫最後一MSEQNO

		MSEQNO = "";
		BufferedWriter fw = new BufferedWriter(new FileWriter(fileName));

		for (int i = 0; i < lineRecords.size(); i++) {
			Object[] tmps = lineRecords.get(i);
			String[] tokens = new String[tmps.length];
			for (int j = 0; j < tmps.length; j++) {
				tokens[j] = tmps[j].toString();
			}
			if (tokens[0].trim().equals(MSNO)) {
				double MQTY = Double.valueOf(tokens[4].trim()).doubleValue();
				if (MQTY == 0D) {
					if (Double.parseDouble(tokens[6].trim()) < 0D) {
						MQTY = -1D;
					} else {
						MQTY = 1D;
					}
				}
				String strDbr = " ";
				String strHbr = " ";
				// String MVTYPE = tokens[12];
				String MTRREM = " ";
				if (tokens.length > 16) {
					MTRREM = tokens[16].trim();
				}
				/**
				 * if (MVTYPE.equals("0")) { MVTYPE = "4"; } else if
				 * (MVTYPE.equals("1")) { MVTYPE = "5"; } else { MVTYPE =
				 * "1"; }
				 */
				// String seq=tokens[11];
				// 20160928 maco add field tokens[13] 客戶年齡層(SO_SALES_ORDER_HEAD.SALS_TYPE)
				if (!tokens[10].trim().equals(MSEQNO)) {
					// res=(String [])sum.get(seq);
					// log.info("sum="+seq+";"+res[0]+";"+res[1]+";"+res[2]);
					strHbr = "S0,SOP," + tokens[11].trim() + "," + MDATE + "," + tokens[9].trim() + "," + MSNO + ",Z9,TW,NTD,"
					+ MSHOPNO + "," + tokens[8].trim() + ",2,3,5," + ((String[]) sum.get(tokens[10].trim()))[0] + "," + MDATE
					+ "," + MDATE + "," + ((String[]) sum.get(tokens[10].trim()))[1] + ","
					+ ((String[]) sum.get(tokens[10].trim()))[2] + "," + tokens[10].trim() + ",Y,SIGNING,"
					+ tokens[8].trim() + "," + tokens[8].trim() + "," + headFileName + "," + tokens[13].trim();
					fw.write(strHbr); // 寫入檔案
					fw.newLine();
				}
				// 20090109 shan add field tokens[10] for watch

				strDbr = "S1," + tokens[2].trim() + "," + tokens[12].trim() + "," + String.valueOf(MQTY) + ","
				+ String.valueOf(Double.valueOf(tokens[12].trim()).doubleValue() * MQTY) + ","
				+ String.valueOf(Double.valueOf(tokens[5].trim()).doubleValue()) + "," + tokens[3].trim() + ","
				+ tokens[6].trim() + "," + MDATE + "," + MDATE + ",Y,3,5,"
				+ String.valueOf(Double.valueOf(tokens[6].trim()).doubleValue() * 0.05) + "," + tokens[8].trim() + ","
				+ tokens[8].trim() + "," + MTRREM;
				fw.write(strDbr); // 寫入檔案
				fw.newLine();
				MSEQNO = tokens[10].trim();
			}
		}

		fw.flush();
		fw.close(); // close file
		return posFileProcess(combineFileName, module, uiProperties);
	}

	/**
	 * POS 調撥單 上傳
	 * 
	 * @param headFileName
	 * @param lineFileName
	 * @param uiProperties
	 * @return
	 * @throws Exception
	 */

	public String posMovDbfFileProcess(String headFileName,
			String lineFileName, HashMap uiProperties) throws Exception {
		log.info("LCMSDoc.posMovDbfFileProcess headFileName=" + headFileName
				+ ",ineFileName" + lineFileName);
		uiProperties.put(ImportDBService.UPLOAD_HEAD_FILE_NAME, headFileName);
		uiProperties.put(ImportDBService.UPLOAD_LINE_FILE_NAME, lineFileName);
		//uiProperties.put(ImportDBService.PROCESS_LOG_NO, UUID.randomUUID().toString());
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		String brandCode = userObj.getBrandCode();
		String actualRootPath = (String)uiProperties.get("actualRootPath");
		// ImportDBService serv = new ImportDBService();
		// try {
		/*BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext()
		.getBean("buShopDAO");

	Object[] headFileNameInfo = SiPosLogUtil
		.parserUploadFileName(headFileName);
	Date headFileNameDeliveryDate = (Date) headFileNameInfo[0];
	String headFileNameMachineCode = (String) headFileNameInfo[1];
	String headFileNameShopCode = (String) headFileNameInfo[2];

	Object[] lineFileNameInfo = SiPosLogUtil
		.parserUploadFileName(lineFileName);
	Date lineFileNameDeliveryDate = (Date) lineFileNameInfo[0];
	String lineFileNameMachineCode = (String) lineFileNameInfo[1];
	String lineFileNameShopCode = (String) lineFileNameInfo[2];

	String brandCode = null;
	if (StringUtils.hasText(headFileNameShopCode)) {
	    BuShop buShop = buShopDAO.findById(headFileNameShopCode);
	    brandCode = buShop.getBrandCode();

	    uiProperties.put(ImportDBService.SHOP_CODE, headFileNameShopCode);
	    uiProperties.put(ImportDBService.BRAND_CODE, brandCode);
	} else if (StringUtils.hasText(lineFileNameShopCode)) {
	    BuShop buShop = buShopDAO.findById(headFileNameShopCode);
	    brandCode = buShop.getBrandCode();

	    uiProperties.put(ImportDBService.SHOP_CODE, lineFileNameShopCode);
	    uiProperties.put(ImportDBService.BRAND_CODE, brandCode);
	}
	String createdBy = userObj.getEmployeeCode();
	String processName = SiPosLogService.PROCESS_NAME_MOVE_UP;*/

		String headType = headFileName.substring(0, 1);
		String combineFileName = String.valueOf(new Date().getTime()) + ".csv";
		log.info("Combine from " + actualRootPath + "/" + headFileName
				+ "& " + lineFileName + "to " + destFolder + "/" + brandCode + "/"
				+ combineFileName);
		//=============轉成csv檔的原檔案路徑============================
		String fileName = destFolder + "/" + brandCode + "/" + combineFileName;
		uiProperties.put(ImportDBService.COMBINE_FILE_PATH, fileName);
		//=============調撥head及line的原檔案路徑=======================
		String headFilePath = actualRootPath + "/" + headFileName;
		String lineFilePath = actualRootPath + "/" + lineFileName;
		uiProperties.put(ImportDBService.UPLOAD_HEAD_FILE_PATH, headFilePath);
		uiProperties.put(ImportDBService.UPLOAD_LINE_FILE_PATH, lineFilePath);	
		//===========================================================
		List<Object[]> headRecords = DBFUtils.readDBF(headFilePath);
		List<Object[]> lineRecords = DBFUtils.readDBF(lineFilePath);

		StringBuffer cvsData = new StringBuffer();
		// HEAD
		for (int i = 0; i < headRecords.size(); i++) {
			Object[] heads = headRecords.get(i);
			cvsData.append("M0");
			cvsData.append(",");
			cvsData.append(heads[0].toString());
			cvsData.append(",");
			cvsData.append(heads[1].toString());
			cvsData.append(",");
			cvsData.append(heads[2].toString());
			cvsData.append(",");
			cvsData.append(heads[3].toString());
			cvsData.append(",");

			/**TODO TW2AD*/
			String deliveryDate = heads[4].toString();
			if (StringUtils.hasText(deliveryDate)) {
				Date tDate = DateUtils.parseTDate(DateUtils.C_DATA_PATTON_YYMMDD, deliveryDate);
				deliveryDate = DateUtils.format(tDate,DateUtils.C_DATA_PATTON_YYYYMMDD);
			}

			cvsData.append(deliveryDate);
			cvsData.append(",");
			cvsData.append(heads[5].toString());
			cvsData.append(",");
			// 20090121 shan add
			cvsData.append(headType);
			cvsData.append(",");
			cvsData.append(headFileName);
			cvsData.append('\n');
		}

		// LINE
		for (int i = 0; i < lineRecords.size(); i++) {
			Object[] lines = lineRecords.get(i);
			cvsData.append("M1");
			cvsData.append(",");
			cvsData.append(lines[0].toString());
			cvsData.append(",");
			cvsData.append(lines[1].toString());
			if (i < lineRecords.size() - 1)
				cvsData.append('\n');
		}

		if (cvsData.length() > 0) {
			FileTools.WriteToFile(fileName, cvsData.toString());
		}

		return posFileProcess(combineFileName, module, uiProperties);
	}

	public static String getUploadFolder(String moduleName) { // 建立上傳檔案目錄
		baseFolder = tw.com.tm.erp.utils.LCMSDoc.class.getResource("/")
		.getPath();
		String newModuleName = moduleName.substring(
				moduleName.lastIndexOf(".") + 1, moduleName.length());
		String date = DateUtils.format(new Date(),
				DateUtils.C_DATA_PATTON_YYYYMMDD);
		// DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		// String date = df.format(new Date()).replaceAll("/", "");
		File f = new File(LCMSDoc.baseFolder + newModuleName + "/ORIGINAL/"
				+ date);
		if (!f.exists())
			f.mkdirs();
		return f.getPath();
	}

	private void performT2SOPImport(File[] baseFiles, String importFileRootPath, String backupPath, HashMap uiProperties) throws Exception{
		log.info("====ENTER===LCMSDOC.performT2SOPImport====");
		String loginUser = null;
		String processName = "";
		String errorMsg = null;
		User userObj = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);    
		if (userObj != null) {
			loginUser = userObj.getEmployeeCode();
		}
		processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
		Date executeDate = (Date)uiProperties.get("actualExecuteDate");
		//馬祖完稅修正(wade) 2013.09.17
		boolean isPreviousDay = true;
		try{isPreviousDay = uiProperties.get("isPreviousDay") == null ? true : (Boolean)uiProperties.get("isPreviousDay");}
		catch(ClassCastException e){}
		String executeDateStr = DateUtils.format(isPreviousDay ? DateUtils.addDays(executeDate, -1) : executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD); // 西元年機場用
		String transactionTWDate = DateUtils.formatToTWDate(isPreviousDay ? DateUtils.addDays(executeDate, -1) : executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD);  // 民國機場用
		String uuid = (String)uiProperties.get("uuidCode");
		String autoJobControl = (String)uiProperties.get("autoJobControl");
		String transferFolder = (String)uiProperties.get("transferFolder");
		if("MP".equalsIgnoreCase(transferFolder)){ // 馬祖完稅 當天
			executeDateStr = DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
			transactionTWDate = DateUtils.formatToTWDate(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
		}

		soFolder = srcFolder;
		String actualBackupPath = backupPath + "/" + executeDateStr;
		String successFilePath = successFolder;
		String failFilePath = failFolder;
		if(StringUtils.hasText(transferFolder)){
			soFolder = soFolder + "/" + transferFolder;
			actualBackupPath = importFileRootPath + "/backup/" + executeDateStr;
			successFilePath = successFilePath + "/" + transferFolder;
			failFilePath = failFilePath + "/" + transferFolder;
		}

		//建立品牌業績資料夾
		File soRoot = new File(soFolder);
		if(!soRoot.exists()){
			FileUtils.forceMkdir(soRoot);
		}
		//建立backup資料夾
		File backUpRoot = new File(actualBackupPath);	
		if(!backUpRoot.exists()){
			FileUtils.forceMkdir(backUpRoot);
		}
		//建立轉入成功後放置的資料夾
		uiProperties.put(ImportDBService.SUCCESS_FILE_PATH, successFilePath);
		File successRoot = new File(successFilePath);
		if(!successRoot.exists()){
			FileUtils.forceMkdir(successRoot);
		}
		//建立轉入失敗後放置的資料夾
		uiProperties.put(ImportDBService.FAIL_FILE_PATH, failFilePath);
		File fileRoot = new File(failFilePath);
		if(!fileRoot.exists()){
			FileUtils.forceMkdir(fileRoot);
		}
		//===============複製並刪除欲上傳的檔案===========================
		System.out.println("原始來源目錄抓取的檔案總數 = " + baseFiles.length);
		for (int i = 0; i < baseFiles.length; i++) {
			String fileName = null;
			try{
				fileName = baseFiles[i].getName().toUpperCase();
				if (fileName.startsWith("H") || fileName.startsWith("D") || fileName.startsWith("P")){
					log.info("fileName = " + fileName);
					// TW2AD
					String fileTWDate = null;
					String tranCheckDate = null;
					if(isAD(fileName)){
						// 表示西元年 
						fileTWDate = fileName.substring(1, 9);
						tranCheckDate = executeDateStr;
					}else{
						// 表示民國年
						fileTWDate = fileName.substring(1, 7);
						tranCheckDate = transactionTWDate;
					}
					log.info("tranCheckDate = " + tranCheckDate);
					log.info("fileTWDate = " + fileTWDate);
					if(("Y".equals(autoJobControl) && tranCheckDate.equals(fileTWDate)) || !"Y".equals(autoJobControl)){
						FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
						//T2POS轉檔不用H檔
						if(!fileName.startsWith("H")){
							FileUtils.copyFileToDirectory(baseFiles[i], soRoot);
						}
						baseFiles[i].delete();
					}
				}
			}catch(Exception ex){
				errorMsg = "複製並刪除" + fileName + "失敗，原因：" + ex.toString();
				log.error(errorMsg);
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);   
			}
		}
		//=========================SO==================================
		File[] soFiles = soRoot.listFiles();
		String srcFileName = null;
		String fileKey = null;
		String posMachineCode = null;
		int dot = 0;
		HashMap filterMap = new HashMap();
		System.out.println("SO==="+soFiles.length);
		for(int i = 0; i < soFiles.length; i++){
			try{
				if (soFiles[i].exists()) {
					// 先將FileName轉成大寫
					srcFileName = soFiles[i].getName().toUpperCase();
					int position = FilenameUtils.indexOfExtension(srcFileName);
					// TW2AD
					int checklen = 10; // 預設10 DYYMMDD+兩碼機號+一碼序號
					if(isAD(srcFileName)){
						checklen = 12; // 12 DYYYYMMDD+兩碼機號+一碼序號
					} 

					if(position != checklen){ // 限制檔名長度
						throw new ValidationErrorException("檔案(" + srcFileName + ")格式錯誤，無法執行轉入程序！" );
					}
					// 以D檔為依據查詢相關檔案
					if (srcFileName.startsWith("D")) {
						dot = srcFileName.indexOf(".");
						fileKey = srcFileName.substring(1, dot-1);  // 如:YYMMDDM7
						log.info("fileKey = " + fileKey);
						if(filterMap.get(fileKey) == null){
							filterMap.put(fileKey, fileKey);
							posMachineCode = srcFileName.substring(dot-3, dot-1);	// 取得小數點前倒數二三碼	           
							log.info("posMachineCode = " + posMachineCode);			    			    
							// 查詢出D開頭的相關檔案
							List transactionResults = (List) FileUtils.listFiles(soRoot, FileFilterUtils.prefixFileFilter("D" + fileKey),null);
							if (transactionResults != null && transactionResults.size() > 0) {
								// 查詢出P開頭的相關檔案
								List paymentResults = (List) FileUtils.listFiles(soRoot,FileFilterUtils.prefixFileFilter("P"+ fileKey), null);
								if (paymentResults == null || paymentResults.size() == 0) {
									throw new Exception("查無識別碼(" + fileKey + ")相關付款明細檔案！");
								}
								//將transactionResults及paymentResults傳入method進行轉檔至temp
								processT2SoFile(transactionResults, paymentResults, fileKey, posMachineCode, uiProperties);
							} else {
								throw new Exception("查無識別碼(" + fileKey + ")相關交易明細檔案！");
							}
						}	
					}
				}
			}catch(Exception ex){
				errorMsg = "POS業績匯入" + srcFileName + "失敗，原因：" +  ex.toString();
				log.error(errorMsg);
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
			}
		}	
		log.info("====LEAVE===LCMSDOC.performT2SOPImport====");
	}

	public void processT2SoFile(List transactionResults, List paymentResults, String fileKey, String posMachineCode, HashMap uiProperties) throws ValidationErrorException, Exception {
		log.info("====ENTER===LCMSDOC.processT2SoFile====");
		SoPostingTallyService postingTallyService = (SoPostingTallyService) SpringUtils.getApplicationContext().getBean("soPostingTallyService");
		SoPostingTally postingTallyPO = null;	
		String posTwDate = null;
		Date posDate = null;
		String transactionDateTmp = null;
		try{
			// TW2AD
			//SO_POSTING_TALLY資料中先將此機台lock	
			// fileKey YYMMDD+兩碼機台
			posTwDate = fileKey.substring(0, fileKey.length()-2);  // YYMMDD  去尾剩日期
			transactionDateTmp = DateUtils.formatTWToDate(posTwDate);
			posDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, transactionDateTmp);
			postingTallyPO = postingTallyService.findSoPostingTallyById(posMachineCode, posDate);
			log.info("postingTallyPO="+postingTallyPO);
			if(postingTallyPO != null){
				if("Y".equals(postingTallyPO.getReserve5())){
					throw new ValidationErrorException("POS機碼(" + posMachineCode + ")、交易日期(" + transactionDateTmp + ")由另一程序鎖定中，無法執行轉檔程序！"); 
				}		
				postingTallyPO.setReserve5("Y");
				postingTallyService.updateSoPostingTally(postingTallyPO);

				//於TmpImportPosItem將salesOrderDate、posMachineCode相符資料刪除
				TmpImportPosItemService tmpImportPosItemService =(TmpImportPosItemService)SpringUtils.getApplicationContext().getBean("tmpImportPosItemService");
				tmpImportPosItemService.deletePosIetmByIdentification(posDate, posMachineCode);
				//於TmpImportPosPayment將salesOrderDate、posMachineCode相符資料刪除		
				TmpImportPosPaymentService tmpImportPosPaymentService =(TmpImportPosPaymentService)SpringUtils.getApplicationContext().getBean("tmpImportPosPaymentService");
				tmpImportPosPaymentService.deletePosPaymentByIdentification(posDate, posMachineCode);
				//=============================執行解析轉檔==========================
				T2PosImportData t2POSImportData = (T2PosImportData) SpringUtils.getApplicationContext().getBean("t2PosImportData");
				t2POSImportData.parsingSoFile(transactionResults, paymentResults, fileKey, posDate, posMachineCode, uiProperties);
			}else{
				throw new NoSuchDataException("查無POS機碼(" + posMachineCode + ")、交易日期(" + transactionDateTmp + ")的過帳記錄資料，無法執行轉檔程序！");
			}
		}catch(ValidationErrorException fe){
			throw new ValidationErrorException(fe.getMessage());
		}catch(Exception ex){
			if(postingTallyPO != null){
				try{
					postingTallyPO.setReserve5("N");
					postingTallyService.updateSoPostingTally(postingTallyPO);
				}catch(Exception ex1){
					log.error("更新POS機碼(" + posMachineCode + ")、交易日期(" + transactionDateTmp + ")的過帳記錄失敗，原因：" + ex1.getMessage());
				}
			}
			throw new Exception(ex.getMessage());
		}	
		log.info("====LEAVE===LCMSDOC.processT2SoFile====");
	}

	/**
	 * 檔名是否民國年
	 * @param fileName
	 * @return
	 */
	private boolean isAD(String fileName){
		if(fileName.substring(1,3).equalsIgnoreCase("20")){
			return true;
		}else{
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			/**
			 * LCMSDoc doc = new
			 * LCMSDoc("C:/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF",
			 * "tw.com.tm.erp.importdb.CmDeclarationImportData");
			 * doc.folderTransfer(null);
			 */
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}