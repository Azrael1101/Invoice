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
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.hbm.service.SiSystemLogService;
import tw.com.tm.erp.utils.dbf.DBFUtils;

/**
 * 外部檔案匯入 ERP 系統
 * 
 * @author SAM,T02049
 * 
 */
public class LCMSDocTemp {

	private static final Log log = LogFactory.getLog(LCMSDocTemp.class);
	private static String baseFolder = "D:/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes";
	private String srcFolder = baseFolder + "/ORIGINAL"; // 上傳檔案原始放置目錄
	private String destFolder = baseFolder + "/PROCESS"; // 上傳檔案處理放置目錄
	private String batchFolder = baseFolder + "/PROCESS"; // 上傳檔案批次放置目錄
	private String successFolder = baseFolder + "/SUCCESS"; // 上傳檔案匯入DB後成功放置目錄
	private String failFolder = baseFolder + "/FAIL"; // 上傳檔案匯入DB後失敗放置目錄
	private String module = "";

	/*
	 * private static String PdcdataFp = ""; private static String PdcdataRc =
	 * ""; private static String PdcdataRh = ""; private static String
	 * PdcdataRhA = ""; private static final int
	 * POS_MOV_HEAD_DBF_FILE_FIELD_LENGTH[] = { 11, 6, 8, 8, 6, 19 };
	 */

	// 預設建構子
	public LCMSDocTemp() {
	}

	// 建構方式完整基礎路徑,模組名稱(含package)
	public LCMSDocTemp(String basePath, String moduleName) throws Exception {
		log.info("LCMSDoc.LCMSDoc moduleName=" + moduleName + ",basePath=" + basePath);
		baseFolder = basePath;
		module = moduleName;
		Vector type = StringTools.SubStrings(moduleName, ".");
		String mod = (String) type.get(type.size() - 1);
		moduleName = mod;
		File file0 = new File(baseFolder + "/" + moduleName);
		if (!file0.exists()) { // baseFolder/moduleName not exists,建dir
			System.out.println("file0.mkdir()=" + file0.mkdir());
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
			System.out.println("src.mkdir()=" + src.mkdir());
		}
		File dest = new File(destFolder);
		if (!dest.exists()) { // baseFolder/moduleName/destFolder not
			// exists,建dir
			System.out.println("dest.mkdir()=" + dest.mkdir());
		}
		File batch = new File(batchFolder);
		if (!batch.exists()) { // baseFolder/moduleName/batchFolder not
			// exists,建dir
			System.out.println("batch.mkdir()=" + batch.mkdir());
		}
		File success = new File(successFolder);
		if (!success.exists()) { // baseFolder/moduleName/successFolder not
			// exists,建dir
			System.out.println("success.mkdir()=" + success.mkdir());
		}
		File fail = new File(failFolder);
		if (!fail.exists()) { // baseFolder/moduleName/failFolder not
			// exists,建dir
			System.out.println("fail.mkdir()=" + fail.mkdir());
		}

		// Date now = new Date(); // src,success,fail Folder下,依日期建子目錄
		// DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		// String date = df.format(now).replaceAll("/", "");
		String date = DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD);
		srcFolder = srcFolder + "/" + date;
		successFolder = successFolder + "/" + date;
		failFolder = failFolder + "/" + date;

		src = new File(srcFolder);
		if (!src.exists()) {
			System.out.println("src.mkdir()=" + src.mkdir());
		}
		success = new File(successFolder);
		if (!success.exists()) {
			System.out.println("success.mkdir()=" + success.mkdir());
		}
		fail = new File(failFolder);
		if (!fail.exists()) {
			System.out.println("fail.mkdir()=" + fail.mkdir());
		}

		System.out.println("srcFolder " + srcFolder);

	}

	public static String getT1(String src) throws IOException // T1格式轉換用
	{
		log.info("LCMSDoc.LCMSDoc getT1=" + src);
		String dest = "";
		byte[] bytes = src.getBytes();
		if (bytes.length >= 220) {
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 5) + "{#}" + subString(bytes, 5, 10) + "{#}"
					+ subString(bytes, 10, 11) + "{#}" + subString(bytes, 11, 15) + "{#}" + subString(bytes, 15, 17) + "{#}"
					+ subString(bytes, 17, 31) + "{#}" + subString(bytes, 31, 39) + "{#}" + subString(bytes, 39, 47) + "{#}"
					+ subString(bytes, 47, 55) + "{#}" + subString(bytes, 55, 69) + "{#}" + subString(bytes, 69, 77) + "{#}"
					+ subString(bytes, 77, 78) + "{#}" + subString(bytes, 78, 81) + "{#}" + subString(bytes, 81, 93) + "{#}"
					+ subString(bytes, 93, 99) + "{#}" + subString(bytes, 99, 111) + "{#}" + subString(bytes, 111, 118) + "{#}"
					+ subString(bytes, 118, 188) + "{#}" + subString(bytes, 188, 190) + "{#}" + subString(bytes, 190, 204) + "{#}"
					+ subString(bytes, 204, 212) + "{#}" + subString(bytes, 212, 220);
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
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16) + "{#}" + subString(bytes, 16, 20) + "{#}"
					+ subString(bytes, 20, 35) + "{#}" + subString(bytes, 35, 105) + "{#}" + subString(bytes, 105, 120) + "{#}"
					+ subString(bytes, 120, 135) + "{#}" + subString(bytes, 135, 175) + "{#}" + subString(bytes, 175, 187) + "{#}"
					+ subString(bytes, 187, 201) + "{#}" + subString(bytes, 201, 204) + "{#}" + subString(bytes, 204, 218) + "{#}"
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
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16) + "{#}" + subString(bytes, 16, 20) + "{#}"
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
			dest = subString(bytes, 0, 2) + "{#}" + subString(bytes, 2, 16) + "{#}" + subString(bytes, 16, 28) + "{#}"
					+ subString(bytes, 28, 32) + "{#}" + subString(bytes, 32, 33);
			if (bytes.length > 33) {
				dest = dest + "{#}" + subString(bytes, 33, bytes.length);
			}
		} else {
			dest = "Size Error";
		}
		return dest;
	}

	// 取字串某範圍內Bytes(for中文)
	public static String subString(byte[] bytes, int srcBegin, int srcEnd) throws IOException {
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
			BufferedReader fr = new BufferedReader(new FileReader(srcFolder + "/" + srcFilename));
			// OutputStreamWriter fw = new OutputStreamWriter(new
			// FileOutputStream(destFolder+"/"+destFilename),"UTF-8");
			BufferedWriter fw = new BufferedWriter(new FileWriter(destFolder + "/" + destFilename));
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

	// 整批檔案轉檔 轉入ERP系統
	public String folderTransfer(HashMap uiProperties) throws Exception {
		log.info("LCMSDoc.folderTransfer baseFolder=" + baseFolder + ",module =" + module + ",uiProperties=" + uiProperties.toString());
		// 遠端+Batch->SRC
		String result = "";

		String dts[] = new String[1];
		String specialParam = (String) uiProperties.get(SystemConfig.SPRCIAL_PARAM);
		if (StringUtils.hasText(specialParam)) {
			dts = specialParam.split(",");
		} else {
			Date fileDate = DateUtils.addDays(new Date(), -1);
			String oneDt = DateUtils.formatChangeToTWDate(fileDate);
			dts[0] = oneDt;
		}

		/*
		 * 20090121 shan modify String year = new
		 * String().valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1911);
		 * String month = new
		 * String().valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1); if
		 * (month.length() < 2) { month = "0" + month; } // String day = new //
		 * String().valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); //
		 * test String day = new
		 * String().valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) -
		 * 1); if (day.length() < 2) { day = "0" + day; }
		 */

		// String dt = year + month + day; // 取得民國年月日
		// System.out.println("dt=" + dt);
		SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils.getApplicationContext().getBean("siSystemLogService");
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils.getApplicationContext().getBean(
				"buCommonPhraseService");
		// System.out.println("測試要執行的 ImportDBType.module=" + module);
		BuCommonPhraseLine bucpl = buCommonPhraseService.getBuCommonPhraseLine("ImportDBType", module);
		try {
			String[] sFiles = new String[4]; // 拿取設在DB中Parameter1~4的遠端位置
			sFiles[0] = bucpl.getParameter1();
			sFiles[1] = bucpl.getParameter2();
			sFiles[2] = bucpl.getParameter3();
			sFiles[3] = bucpl.getParameter4();
			for (int index = 0; index < dts.length; index++) {
				String dt = dts[index];
				for (int s = 0; s < sFiles.length; s++) {
					String sFile = sFiles[s];
					// System.out.println("測試要執行的 module=" + module + ";資料的來源
					// sFile=" + sFile);
					if (StringUtils.hasText(sFile)) {
						File fileRmt = new File(sFile);
						if (fileRmt.exists()) {
							File[] filesRmt;
							// 依不同的module,對檔案做不同處理
							// System.out.println("判斷要執行的 module=" + module);
							if (module.equals("tw.com.tm.erp.importdb.POSImportData")) {
								// System.out.println("LCMSDoc.folderTransfer do
								// tw.com.tm.erp.importdb.POSImportData ");
								batchFolder = srcFolder;
								File[] filesRmtAll = fileRmt.listFiles();
								List tmp = new ArrayList();
								for (int i = 0; i < filesRmtAll.length; i++) {
									// System.out.println("LCMSDoc.folderTransfer
									// do
									// tw.com.tm.erp.importdb.POSImportData
									// field
									// name[" +
									// filesRmtAll[i].getName().toUpperCase() +
									// "]");
									if ((filesRmtAll[i].getName().toUpperCase().startsWith("H" + dt))
											|| ((filesRmtAll[i].getName().toUpperCase().startsWith("D" + dt)))) {
										tmp.add(filesRmtAll[i]);
									}
								}
								filesRmt = new File[tmp.size()];
								for (int j = 0; j < tmp.size(); j++) {
									filesRmt[j] = (File) tmp.get(j);
								}
							} else if (module.equals("tw.com.tm.erp.importdb.POSMOVImportData")) {
								// System.out.println("LCMSDoc.folderTransfer do
								// tw.com.tm.erp.importdb.POSMOVImportData ");
								batchFolder = srcFolder;
								File[] filesRmtAll = fileRmt.listFiles();
								List tmp = new ArrayList();
								for (int i = 0; i < filesRmtAll.length; i++) {
									// System.out.println("LCMSDoc.folderTransfer
									// do
									// tw.com.tm.erp.importdb.POSMOVImportData
									// field
									// name[" +
									// filesRmtAll[i].getName().toUpperCase() +
									// "]["
									// +
									// filesRmtAll[i].getName().toUpperCase().startsWith("M"
									// + dt) + "]");
									if (filesRmtAll[i].getName().toUpperCase().startsWith("M" + dt)
											|| filesRmtAll[i].getName().toUpperCase().startsWith("S" + dt)
											|| filesRmtAll[i].getName().toUpperCase().startsWith("R" + dt)
											|| filesRmtAll[i].getName().toUpperCase().startsWith("T" + dt)) {
										tmp.add(filesRmtAll[i]);
									}
								}
								filesRmt = new File[tmp.size()];
								for (int j = 0; j < tmp.size(); j++) {
									filesRmt[j] = (File) tmp.get(j);
								}
							} else {
								filesRmt = fileRmt.listFiles();
							}
							
							// COPY 符合命名規則的檔案 DB中設定的網路磁碟機->batchFolder
							for (int i = 0; i < filesRmt.length; i++) {
								// System.out.println("將資料由 來源 " + sFile + "/" +
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
			// System.out.println("LCMSDoc.folderTransfer do batchFolder=" +
			// batchFolder);
			File file = new File(batchFolder);
			if (file.exists()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						String srcFilename = files[i].getName();
						if (module.equals("tw.com.tm.erp.importdb.CmDeclarationImportData")) { // 報關單
							// System.out.println("LCMSDoc.folderTransfer do
							// tw.com.tm.erp.importdb.CmDeclarationImportData
							// ");
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
							}// while fr.ready()
							fr.close(); // close file
							fw.close(); // close file

							// System.out.println(batchFolder + "/" +
							// srcFilename + "->" + destFolder + "/" +
							// destFilename);
							fr = new BufferedReader(new FileReader(batchFolder + "/" + srcFilename));
							fw = new BufferedWriter(new FileWriter(srcFolder + "/" + srcFilename));
							while (fr.ready())// 如果檔案沒有讀完，就繼續處理
							{
								getfr = fr.readLine(); // 取得一行輸入
								StringBuffer strbr = new StringBuffer(getfr);
								fw.write(strbr.toString()); // 寫入檔案
								fw.newLine();
							}// while fr.ready()
							fr.close(); // close file
							fw.close(); // close file
							result = result + fileProcess(destFilename, module, uiProperties);

							File sourceFile = new File(batchFolder + "/" + srcFilename);
							sourceFile.delete();
						} else if (module.equals("tw.com.tm.erp.importdb.POSImportData")) { // POS
							// System.out.println("LCMSDoc.folderTransfer do
							// tw.com.tm.erp.importdb.POSImportData ");
							if (srcFilename.toUpperCase().startsWith("H")) {
								String headFileName = srcFilename;
								String lineFileName = "D" + srcFilename.substring(1);
								result = result + dbfFileProcess(headFileName, lineFileName, uiProperties);
							}
						} else if (module.equals("tw.com.tm.erp.importdb.POSMOVImportData")) { // POSMOV
							// System.out.println("LCMSDoc.folderTransfer do
							// tw.com.tm.erp.importdb.POSMOVImportData ");
							// MOV
							if (srcFilename.toUpperCase().startsWith("M")) {
								String headFileName = srcFilename;
								String lineFileName = "S" + srcFilename.substring(1);
								result = result + posMovDbfFileProcess(headFileName, lineFileName, uiProperties);
							} else if (srcFilename.toUpperCase().startsWith("R")) {
								String headFileName = srcFilename;
								String lineFileName = "T" + srcFilename.substring(1);
								result = result + posMovDbfFileProcess(headFileName, lineFileName, uiProperties);
							}
						}
					}
				} else {
					log.error("LCMSDoc.folderTransfer 路徑中查無檔案 " + batchFolder);
				}
			} else {
				log.error("LCMSDoc.folderTransfer 查無路徑 " + batchFolder);
			}
		} catch (Exception e) {
			SiSystemLog sysLog = new SiSystemLog();
			siSystemLogService.createSystemLog(sysLog);
			e.printStackTrace();
			result = result + e.toString();

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
			// System.out.println("Copy from " + srcFolder + "/" +
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
			// System.out.println("tmp="+tmp);
			FileTools.WriteToFile(destFolder + "/UTF8_" + sourceFileName, tmp, "UTF-8");
			sourceFile = new File(destFolder + "/" + sourceFileName);
			sourceFile.delete();
			sourceFileName = "UTF8_" + sourceFileName;
		}

		String outFileName = "";
		try {
			ImportDBService serv = (ImportDBService) SpringUtils.getApplicationContext().getBean("importDBService");
			// System.out.println("filePath=" + destFolder + "/" +
			// sourceFileName);
			String resultMsg = serv.importDB(destFolder + "/" + sourceFileName, infoClassName, uiProperties);
			result = result + resultMsg;
			// System.out.println("檔案匯入結果 " + resultMsg);
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
	public String xlsFileProcess(String fileName, String infoClassName, HashMap uiProperties) throws Exception {
		log.info("LCMSDoc.xlsFileProcess fileName=" + fileName + ",infoClassName=" + infoClassName);
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
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
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
							quanty.add(String.valueOf(ExcelUtils.getCellData(cell)));
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

			ImportDBService serv = (ImportDBService) SpringUtils.getApplicationContext().getBean("importDBService");
			resultMsg = resultMsg + serv.importDB(resultAllDatas, infoClassName, uiProperties);
			outFileName = successFolder + "/" + sourceFileName;
		} catch (Exception e) {
			resultMsg = resultMsg + "失敗(" + e.toString() + ")";
			outFileName = failFolder + "/" + sourceFileName;
		}
		FileTools.CopyFile(fileName, outFileName);
		return resultMsg;
	}

	// POS(POSImportData) Head & Line csv檔案處理
	public String csvFileProcess(String headFileName, String lineFileName, HashMap uiProperties) throws java.io.IOException {
		log.info("LCMSDoc.csvFileProcess headFileName=" + headFileName + ",lineFileName=" + lineFileName);
		String result = "";
		// ImportDBService serv = new ImportDBService();
		try {
			String combineFileName = String.valueOf(new Date().getTime()) + ".csv";
			System.out.println("Combine from " + srcFolder + "/" + headFileName + "& " + lineFileName + "to " + srcFolder + "/"
					+ combineFileName);

			String getfr;
			String MSNO = "";
			String MDATE = "";
			String MSHOPNO = "";
			String MSEQNO = "";
			/** ** open file ******** */
			BufferedReader fr0 = new BufferedReader(new FileReader(srcFolder + "/" + headFileName));
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
			fr = new BufferedReader(new FileReader(srcFolder + "/" + lineFileName));
			/** ** open file ******** */
			while (fr.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr.readLine(); // 取得一行輸入
				// StringBuffer strbr=new StringBuffer(getfr);
				String[] tokens = getfr.split(",");
				if (tokens[1].equals(MSNO)) {
					// int MQTY = java.lang.Integer.parseInt(tokens[5]);
					double MQTY = Double.valueOf(tokens[5]).doubleValue();
					if ((MQTY == 0) && (Double.valueOf(tokens[7]).doubleValue() < 0)) {
						MQTY = -1;
					} else {
						MQTY = 1;
					}
					if (tokens[11].equals(MSEQNO)) {
						taxSum = taxSum + Double.valueOf(tokens[7]).doubleValue() * 0.05;
						MSLTOTSum = MSLTOTSum + Double.valueOf(tokens[7]).doubleValue();
						saleAmountSum = saleAmountSum + Double.valueOf(tokens[13]).doubleValue() * MQTY;
					} else {
						res = new String[3];
						res[0] = String.valueOf(taxSum);
						res[1] = String.valueOf(saleAmountSum);
						res[2] = String.valueOf(MSLTOTSum);
						// System.out.println(MSEQNO+":Sum="+res[0]+";"+res[1]+";"+res[2]);
						sum.put(MSEQNO, res); // 寫入前一MSEQNO的Sum
						taxSum = Double.valueOf(tokens[7]).doubleValue() * 0.05;
						MSLTOTSum = Double.valueOf(tokens[7]).doubleValue();
						saleAmountSum = Double.valueOf(tokens[13]).doubleValue() * MQTY;
					}
					MSEQNO = tokens[11];
				}
			}// while fr.ready()
			fr.close(); // close file
			res = new String[3];
			res[0] = String.valueOf(taxSum);
			res[1] = String.valueOf(saleAmountSum);
			res[2] = String.valueOf(MSLTOTSum);
			// System.out.println(MSEQNO+":Fin
			// Sum="+res[0]+";"+res[1]+";"+res[2]);
			sum.put(MSEQNO, res); // 因寫入前一MSEQNO的Sum,所以必須寫最後一MSEQNO

			MSEQNO = "";
			fr = new BufferedReader(new FileReader(srcFolder + "/" + lineFileName));
			fw = new BufferedWriter(new FileWriter(srcFolder + "/" + combineFileName));
			/** ** open file ******** */
			while (fr.ready())// 如果檔案沒有讀完，就繼續處理
			{
				getfr = fr.readLine(); // 取得一行輸入
				String[] tokens = getfr.split(",");
				if (tokens[1].equals(MSNO)) {
					double MQTY = Double.valueOf(tokens[5]).doubleValue();
					if ((MQTY == 0) && (Double.valueOf(tokens[7]).doubleValue() < 0)) {
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
						// System.out.println("sum="+seq+";"+res[0]+";"+res[1]+";"+res[2]);
						strHbr = "S0,SOP," + tokens[12] + "," + MDATE + "," + tokens[10] + "," + MSNO + ",Z9,TW,TWD," + MSHOPNO + ","
								+ tokens[9] + ",2,3,5," + ((String[]) sum.get(tokens[11]))[0] + "," + MDATE + "," + MDATE + ","
								+ ((String[]) sum.get(tokens[11]))[1] + "," + ((String[]) sum.get(tokens[11]))[2] + "," + tokens[11]
								+ ",Y," + MTRREM + ",SIGNING," + tokens[9] + "," + tokens[9];
						fw.write(strHbr); // 寫入檔案
						fw.newLine();
					}
					strDbr = "S1," + tokens[3] + "," + tokens[13] + "," + String.valueOf(MQTY) + ","
							+ String.valueOf(Double.valueOf(tokens[13]).doubleValue() * MQTY) + ","
							+ String.valueOf(100 - Double.valueOf(tokens[6]).doubleValue()) + "," + tokens[4] + "," + tokens[7] + ","
							+ MDATE + "," + MDATE + ",Y,3,5," + String.valueOf(Double.valueOf(tokens[7]).doubleValue() * 0.05)
							+ ",SIGNING," + tokens[9] + "," + tokens[9];
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
			System.out.println("Combine from " + srcFolder + "/" + headFileName + "& " + lineFileName + "to " + srcFolder + "/"
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
						// System.out.println(MSEQNO+":Sum="+res[0]+";"+res[1]+";"+res[2]);
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
						// System.out.println("sum="+seq+";"+res[0]+";"+res[1]+";"+res[2]);
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

	/**
	 * POS 調撥單 上傳
	 * 
	 * @param headFileName
	 * @param lineFileName
	 * @param uiProperties
	 * @return
	 */

	public String posMovDbfFileProcess(String headFileName, String lineFileName, HashMap uiProperties) {
		log.info("LCMSDoc.posMovDbfFileProcess headFileName=" + headFileName + ",ineFileName" + lineFileName);
		String result = "";
		// ImportDBService serv = new ImportDBService();
		try {
			String headType = headFileName.substring(0, 1);
			String combineFileName = String.valueOf(new Date().getTime()) + ".csv";
			System.out.println("Combine from " + srcFolder + "/" + headFileName + "& " + lineFileName + "to " + srcFolder + "/"
					+ combineFileName);
			String fileName = srcFolder + "/" + combineFileName;
			// BufferedWriter fw = new BufferedWriter(new FileWriter(srcFolder +
			// "/" + combineFileName));

			File hF = new File(srcFolder + "/" + headFileName);
			File lF = new File(srcFolder + "/" + lineFileName);

			if (hF.exists() && lF.exists()) {
				List<Object[]> headRecords = DBFUtils.readDBF(srcFolder + "/" + headFileName);
				List<Object[]> lineRecords = DBFUtils.readDBF(srcFolder + "/" + lineFileName);

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

					String deliveryDate = heads[4].toString();
					if (StringUtils.hasText(deliveryDate)) {
						Date tDate = DateUtils.parseTDate(DateUtils.C_DATA_PATTON_YYMMDD, deliveryDate);
						deliveryDate = DateUtils.format(tDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
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

				result = fileProcess(combineFileName, module, uiProperties);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.toString();
		}
		return result;
	}

	public static String getUploadFolder(String moduleName) { // 建立上傳檔案目錄
		baseFolder = tw.com.tm.erp.utils.LCMSDocTemp.class.getResource("/").getPath();
		String newModuleName = moduleName.substring(moduleName.lastIndexOf(".") + 1, moduleName.length());
		String date = DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD);
		// DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		// String date = df.format(new Date()).replaceAll("/", "");
		File f = new File(LCMSDocTemp.baseFolder + newModuleName + "/ORIGINAL/" + date);
		if (!f.exists())
			f.mkdirs();
		return f.getPath();
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
