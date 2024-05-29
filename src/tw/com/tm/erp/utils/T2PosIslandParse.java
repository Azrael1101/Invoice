package tw.com.tm.erp.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.exportdb.POSIslandsExportData;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.*;
import tw.com.tm.erp.hbm.dao.*;
import tw.com.tm.erp.hbm.service.*;
import tw.com.tm.erp.importdb.T2PosImportData;
import tw.com.tm.erp.importdb.T2PosImportDataMazu;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.DeclarationDataParse;
import tw.com.tm.erp.utils.EmployeeDataParse;
import tw.com.tm.erp.utils.LCMSDoc;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.User;
import tw.com.tm.erp.utils.sp.JobCheckingService;

public class T2PosIslandParse {

    private  ApplicationContext context = SpringUtils.getApplicationContext();
    private  final Log log = LogFactory.getLog(T2PosIslandParse.class);
    private  String baseFolder = null;
    private  String srcFolder = baseFolder + "/ORIGINAL"; // 上傳檔案原始放置目錄
    private  String destFolder = baseFolder + "/PROCESS"; // 上傳檔案處理放置目錄
    private  String batchFolder = baseFolder + "/PROCESS"; // 上傳檔案批次放置目錄
    private  String successFolder = baseFolder + "/SUCCESS"; // 上傳檔案匯入DB後成功放置目錄
    private  String failFolder = baseFolder + "/FAIL"; // 上傳檔案匯入DB後失敗放置目錄
    private  String folder; //目錄
    private String[] sFiles = null;
    private String brandCode = null;
    private String loginUser = null;
    private String transferFolder = null;
    private String autoJobControl = null;

    public T2PosIslandParse(String brandCode, String loginUser, String methodName, String transferFolder,String autoJobControl){
	this.brandCode = brandCode;
	this.loginUser = loginUser;
	this.transferFolder = transferFolder;
	this.autoJobControl = autoJobControl;
	BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) SpringUtils
	.getApplicationContext().getBean("buCommonPhraseService");
	BuCommonPhraseLine bucpl = buCommonPhraseService.getBuCommonPhraseLine("ImportDBType", methodName);
	baseFolder = bucpl.getParameter1();
	srcFolder = baseFolder + "/ORIGINAL"; // 上傳檔案原始放置目錄
	destFolder = baseFolder + "/PROCESS"; // 上傳檔案處理放置目錄
	batchFolder = baseFolder + "/PROCESS"; // 上傳檔案批次放置目錄
	successFolder = baseFolder + "/SUCCESS"; // 上傳檔案匯入DB後成功放置目錄
	failFolder = baseFolder + "/FAIL"; // 上傳檔案匯入DB後失敗放置目錄
	sFiles = new String[5]; // 拿取設在DB中Parameter1~5的遠端位置
	sFiles[0] = bucpl.getParameter1();
	sFiles[1] = bucpl.getParameter2();
	sFiles[2] = bucpl.getParameter3();
	sFiles[3] = bucpl.getParameter4();
	sFiles[4] = bucpl.getParameter5();
    }
	
    /**
     * @param args
     */
    public void execute(String methodName){
	System.out.println("==========START===========");
	//手動轉入
	Date executeDate = new Date();
	String uuid = UUID.randomUUID().toString();
	HashMap uiProperties  = new HashMap();
	User userObj = new User();
	userObj.setBrandCode(brandCode);
	userObj.setEmployeeCode(loginUser);
	uiProperties.put(SystemConfig.USER_SESSION_NAME, userObj);
	uiProperties.put("actualExecuteDate", executeDate);
	uiProperties.put("uuidCode", uuid);
	String importType = brandCode;
	String jobCheckMsg = "";
	Date transactionShortDate = DateUtils.getShortDate(executeDate);
	log.info("transactionShortDate = " + transactionShortDate);
	if(StringUtils.hasText(transferFolder)){
	    importType += "(" + transferFolder + ")業績資料上傳";
	    jobCheckMsg = transferFolder;
	}else{
	    importType += "業績資料上傳";
	}
	List<String> importFilePath = new ArrayList();
	importFilePath.add(sFiles[0]);
	String backupPath = sFiles[1];
	String subProcessName = brandCode + "_" + SiPosLogService.PROCESS_NAME_SOP_UP+"_ISLANDS";
	try{
	    SiSystemLogUtils.createSystemLog( subProcessName, MessageStatus.LOG_INFO, "開始執行" + importType , executeDate, uuid, loginUser);
	    uiProperties.put(ImportDBService.PRCCESS_NAME, subProcessName);
	    for(int i = 0; i < importFilePath.size(); i++){
		String importFileRootPath = importFilePath.get(i);
		if (StringUtils.hasText(importFileRootPath)) {
		    File rootFolder = new File(importFileRootPath);
		    if (rootFolder.exists()) {
			createAllFolders(uiProperties);
			//取得路徑下所有檔案
			System.out.println("rootFolder.exists()");
			System.out.println("importFilePath = " + importFilePath.get(i));
			File[] filesRmtAll = rootFolder.listFiles();
			if("POS".equals(methodName))
			    performT2SOPImport(filesRmtAll, backupPath, uiProperties);
			else if("LAD".equals(methodName))
			    performT2LadingImport(filesRmtAll, backupPath, uiProperties);
			else if("VOID".equals(methodName))
			    performT2VoidImport(filesRmtAll, backupPath, uiProperties);
		    }else {
			String msg = "請確認檔案上傳路徑是否正確路徑:" + importFileRootPath;
			log.error(msg);
			SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.ERROR,  msg, executeDate, uuid, loginUser);		    
		    }
		}
	    }
	    System.out.println("==========END===========");
	}catch (Exception ex) {
	    String msg = "執行" + importType + "失敗，原因：";
	    log.error(msg + ex.toString());
	    SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.ERROR,  msg + ex.toString(), executeDate, uuid, loginUser);
	}finally {
	    SiSystemLogUtils.createSystemLog(subProcessName, MessageStatus.LOG_INFO,  importType + "程序結束...", executeDate, uuid, loginUser);
	    //=========================寫入JOB_CHECKING=========================
	    if("POS".equals(methodName)){
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
	}
    }

    private void createAllFolders(HashMap uiProperties) throws Exception{
	System.out.println("createAllFolders");
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");
	String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	//建立轉入成功後放置的資料夾
	String successFilePath = successFolder + "/" + brandCode + "/" + executeDateStr;
	uiProperties.put(ImportDBService.SUCCESS_FILE_PATH, successFilePath);
	File successRoot = new File(successFilePath);
	if(!successRoot.exists()){
	    FileUtils.forceMkdir(successRoot);
	}
	//建立轉入失敗後放置的資料夾
	String failFilePath = failFolder + "/" + brandCode + "/" + executeDateStr;
	uiProperties.put(ImportDBService.FAIL_FILE_PATH, failFilePath);
	File fileRoot = new File(failFilePath);
	if(!fileRoot.exists()){
	    FileUtils.forceMkdir(fileRoot);
	}
    }
	
    private void performT2SOPImport(File[] baseFiles, String backupPath, HashMap uiProperties) throws Exception{
	System.out.println("performT2SOPImport");
	String processName = "";
	String errorMsg = null;
	processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");
	String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	String uuid = (String)uiProperties.get("uuidCode");
	folder = srcFolder + "/" + brandCode + "/" + executeDateStr;
	//建立品牌業績資料夾
	File soRoot = new File(folder);
	if(!soRoot.exists()){
	    FileUtils.forceMkdir(soRoot);
	}
	//建立backup資料夾
	File backUpRoot = new File(backupPath + "/" + executeDateStr);
	if(!backUpRoot.exists()){
	    FileUtils.forceMkdir(backUpRoot);
	}
	System.out.println("複製並刪除欲上傳的檔案");
	//===============複製並刪除欲上傳的檔案===========================
	for (int i = 0; i < baseFiles.length; i++) {
		String fileName = null;
		try{
			fileName = baseFiles[i].getName().toUpperCase();    
			if (fileName.startsWith("H") || fileName.startsWith("D") || (fileName.startsWith("P") && !fileName.startsWith("PL")) || fileName.startsWith("A")){
				FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
				//T2POS轉檔不用H檔
				if(!fileName.startsWith("H")){
					FileUtils.copyFileToDirectory(baseFiles[i], soRoot);
				}
				baseFiles[i].delete();
			}
		}catch(Exception ex){
			errorMsg = "複製並刪除" + fileName + "失敗，原因：" + ex.toString();
			log.error(errorMsg);
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser);   
		}
	}
	//=========================SO==================================
	System.out.println("=========================SO==================================");
	File[] soFiles = soRoot.listFiles();
	String srcFileName = null;
	String fileKey = null;
	String posMachineCode = null;
	int dot = 0;
	HashMap filterMap = new HashMap();
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

		    if(position != checklen){
			throw new ValidationErrorException("檔案(" + srcFileName + ")格式錯誤，無法執行轉入程序！" );
		    }
		    // 以D檔為依據查詢相關檔案
		    // TW2AD
		    if (srcFileName.startsWith("D")) {
			dot = srcFileName.indexOf(".");
			fileKey = srcFileName.substring(1, dot-1);
			log.info("fileKey = " + fileKey);
			if(filterMap.get(fileKey) == null){
			    filterMap.put(fileKey, fileKey);
			    posMachineCode = srcFileName.substring(dot-3, dot-1);	
			    log.info("posMachineCode = " + posMachineCode);		
			    // 查詢出D開頭的相關檔案
			    List transactionResults = (List) FileUtils.listFiles(soRoot, FileFilterUtils.prefixFileFilter("D" + fileKey),null);
			    if (transactionResults != null && transactionResults.size() > 0) {

				// 查詢出P開頭的相關檔案
				List paymentResults = (List) FileUtils.listFiles(soRoot,FileFilterUtils.prefixFileFilter("P"+ fileKey), null);
				if (paymentResults == null || paymentResults.size() == 0) {
				    throw new Exception("查無識別碼(" + fileKey + ")相關付款明細檔案！");
				}

				List cmResults = (List) FileUtils.listFiles(soRoot,FileFilterUtils.prefixFileFilter("A"+ fileKey), null);
				if (cmResults == null || cmResults.size() == 0) {
				    throw new Exception("查無識別碼(" + fileKey + ")相關報關明細檔案！");
				}
				//將transactionResults及paymentResults傳入method進行轉檔至temp
				System.out.println("=========================processT2SoFile==================================");
				processT2SoFile(transactionResults, paymentResults, cmResults, fileKey, posMachineCode, uiProperties);
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
    }

    public void processT2SoFile(List transactionResults, List paymentResults, List cmResults, String fileKey, String posMachineCode, HashMap uiProperties) throws ValidationErrorException, Exception {
	System.out.println("processT2SoFile");
	SoPostingTallyService postingTallyService = (SoPostingTallyService) SpringUtils.getApplicationContext().getBean("soPostingTallyService");
	SoPostingTally postingTallyPO = null;	
	User userObj = (User)uiProperties.get(SystemConfig.USER_SESSION_NAME);
	String brandCode = userObj.getBrandCode();
	String loginUser = userObj.getEmployeeCode();
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");;
	String posTwDate = null;
	Date posDate = null;
	String transactionDateTmp = null;
	try{
	    // TW2AD
	    //SO_POSTING_TALLY資料中先將此機台lock	
	    posTwDate = fileKey.substring(0, fileKey.length()-2);
	    transactionDateTmp = DateUtils.formatTWToDate(posTwDate);
	    posDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, transactionDateTmp);
	    postingTallyPO = postingTallyService.findSoPostingTallyById(posMachineCode, posDate);
	    if(null == postingTallyPO){
		postingTallyService.createT2PostingTallyWithRangeOfDate(executeDate, brandCode, loginUser);
		postingTallyPO = postingTallyService.findSoPostingTallyById(posMachineCode, posDate);
	    }
	    if(null == postingTallyPO){
		throw new ValidationErrorException("查無POS機碼(" + posMachineCode + ")之相關資訊");
	    }
	    if("Y".equals(postingTallyPO.getReserve5())){
		throw new ValidationErrorException("POS機碼(" + posMachineCode + ")、交易日期(" + transactionDateTmp + ")由另一程序鎖定中，無法執行轉檔程序！"); 
	    }

	    postingTallyPO.setReserve5("Y");
	    postingTallyService.updateSoPostingTally(postingTallyPO);

	    System.out.println("tmpImportPosPaymentService.deletePosPaymentByIdentification");
	    TmpImportPosPaymentService tmpImportPosPaymentService =(TmpImportPosPaymentService)SpringUtils.getApplicationContext().getBean("tmpImportPosPaymentService");
	    tmpImportPosPaymentService.deletePosPaymentByIdentification(posDate, posMachineCode);
	    //=============================執行解析轉檔===========================
	    T2PosImportDataMazu t2PosImportDataMazu = (T2PosImportDataMazu) SpringUtils.getApplicationContext().getBean("t2PosImportDataMazu");
	    t2PosImportDataMazu.parsingSoFile(transactionResults, paymentResults, cmResults, fileKey, posDate, posMachineCode, uiProperties);
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
    }
	
    private void performT2LadingImport(File[] baseFiles, String backupPath, HashMap uiProperties) throws Exception{
	System.out.println("performT2LadingImport");
	String processName = "";
	String errorMsg = null;
	processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");
	String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	String uuid = (String)uiProperties.get("uuidCode");
	folder = srcFolder + "/" + brandCode + "/" + executeDateStr;
	//建立品牌業績資料夾
	File root = new File(folder);
	if(!root.exists()){
	    FileUtils.forceMkdir(root);
	}
	//建立backup資料夾
	File backUpRoot = new File(backupPath + "/" + executeDateStr);
	if(!backUpRoot.exists()){
	    FileUtils.forceMkdir(backUpRoot);
	}
	//===============複製並刪除欲上傳的檔案===========================
	for (int i = 0; i < baseFiles.length; i++) {
	    String fileName = null;
	    try{
		fileName = baseFiles[i].getName().toUpperCase();
		if (fileName.startsWith("B")){
		    if(baseFiles[i].isFile()){
			FileUtils.copyFileToDirectory(baseFiles[i], root);
			FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
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
	File[] files = root.listFiles();
	String srcFileName = null;
	String fileKey = null;
	int dot = 0;
	HashMap filterMap = new HashMap();
	for(int i = 0; i < files.length; i++){
	    try{
		if (files[i].exists()) {
		    // 先將FileName轉成大寫
		    srcFileName = files[i].getName().toUpperCase();
		    int position = FilenameUtils.indexOfExtension(srcFileName);
		    // TW2AD
		    int checklen = 10; // 預設10 DYYMMDD+兩碼機號+一碼序號
		    if(isAD(srcFileName)){
			checklen = 12; // 12 DYYYYMMDD+兩碼機號+一碼序號
		    } 
		    if(position != checklen){
			throw new ValidationErrorException("檔案(" + srcFileName + ")格式錯誤，無法執行轉入程序！" );
		    }
		    // 以B檔為依據查詢相關檔案
		    // TW2AD
		    if (srcFileName.startsWith("B")) {
			dot = srcFileName.indexOf(".");
			fileKey = srcFileName.substring(1, dot-1);
			System.out.println("fileKey = " + fileKey);
			if(filterMap.get(fileKey) == null){
			    filterMap.put(fileKey, fileKey);
			    // 查詢出B開頭的相關檔案
			    List results = (List) FileUtils.listFiles(root, FileFilterUtils.prefixFileFilter("B" + fileKey),null);
			    if (results != null && results.size() > 0) {
				//processT2LadFile(results, uiProperties);
				T2PosImportDataMazu t2PosImportDataMazu = (T2PosImportDataMazu) SpringUtils.getApplicationContext().getBean("t2PosImportDataMazu");
				t2PosImportDataMazu.parsingLadFile(results, uiProperties);
			    } else {
				throw new Exception("查無識別碼(" + fileKey + ")相關提貨明細檔案！");
			    }
			}	
		    }
		}
	    }catch(Exception ex){
		errorMsg = "POS提貨匯入" + srcFileName + "失敗，原因：" +  ex.toString();
		log.error(errorMsg);
		SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
	    }
	}	
    }
	
    private void performT2VoidImport(File[] baseFiles, String backupPath, HashMap uiProperties) throws Exception{
	System.out.println("performT2LadingImport");
	String processName = "";
	String errorMsg = null;
	processName = (String)uiProperties.get(ImportDBService.PRCCESS_NAME);
	Date executeDate = (Date)uiProperties.get("actualExecuteDate");
	String executeDateStr = DateUtils.format(DateUtils.addDays(executeDate, -1), DateUtils.C_DATA_PATTON_YYYYMMDD);
	String uuid = (String)uiProperties.get("uuidCode");
	folder = srcFolder + "/" + brandCode + "/" + executeDateStr;
	System.out.println("folder = " + folder);
	//建立品牌業績資料夾
	File root = new File(folder);
	if(!root.exists()){
	    FileUtils.forceMkdir(root);
	}
	System.out.println("backupPath = " + backupPath);
	//建立backup資料夾
	File backUpRoot = new File(backupPath + "/" + executeDateStr);
	if(!backUpRoot.exists()){
	    FileUtils.forceMkdir(backUpRoot);
	}
	//===============複製並刪除欲上傳的檔案===========================
	for (int i = 0; i < baseFiles.length; i++) {
	    String fileName = null;
	    try{
		fileName = baseFiles[i].getName().toUpperCase();
		if (fileName.startsWith("C")){
		    if(baseFiles[i].isFile()){
			FileUtils.copyFileToDirectory(baseFiles[i], root);
			FileUtils.copyFileToDirectory(baseFiles[i], backUpRoot);
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
	File[] files = root.listFiles();
	String srcFileName = null;
	String fileKey = null;
	int dot = 0;
	HashMap filterMap = new HashMap();
	System.out.println("files.length = " + files.length);
	for(int i = 0; i < files.length; i++){
	    System.out.println("files[i].getName().toUpperCase() = " + files[i].getName().toUpperCase());
	    String posTwDate = null;
	    Date posDate = null;
	    String transactionDateTmp = null;
	    String posMachineCode = null;
	    try{
		if (files[i].exists()) {
		    // 先將FileName轉成大寫
		    // TW2AD
		    srcFileName = files[i].getName().toUpperCase();
		    System.out.println("srcFileName = " + srcFileName);
		    //SO_POSTING_TALLY資料中先將此機台lock	
		    dot = srcFileName.indexOf(".");
		    fileKey = srcFileName.substring(1, dot);  // ??YYMMDDXXZ
		    System.out.println("fileKey = " + fileKey);
		    posTwDate = fileKey.substring(0, fileKey.length()-3);
		    System.out.println("posTwDate = " + posTwDate);
		    transactionDateTmp = DateUtils.formatTWToDate(posTwDate);
		    posDate = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD, transactionDateTmp);
		    int position = FilenameUtils.indexOfExtension(srcFileName);
		    // TW2AD
	            int checklen = 10; // 預設10 DYYMMDD+兩碼機號+一碼序號
	            if(isAD(srcFileName)){
	        	checklen = 12; // 12 DYYYYMMDD+兩碼機號+一碼序號
	            } 
		    System.out.println("position = " + position);
		    if(position != checklen){
			throw new ValidationErrorException("檔案(" + srcFileName + ")格式錯誤，無法執行轉入程序！" );
		    }
		    // 以c檔為依據查詢相關檔案
		    if (srcFileName.startsWith("C")) {
			fileKey = srcFileName.substring(1, dot-1);
			posMachineCode = srcFileName.substring(dot-3, dot-1);
			if(filterMap.get(fileKey) == null){
			    filterMap.put(fileKey, fileKey);
			    // 查詢出c開頭的相關檔案
			    List results = (List) FileUtils.listFiles(root, FileFilterUtils.prefixFileFilter("C" + fileKey),null);
			    if (results != null && results.size() > 0) {
				T2PosImportDataMazu t2PosImportDataMazu = (T2PosImportDataMazu) SpringUtils.getApplicationContext().getBean("t2PosImportDataMazu");
				t2PosImportDataMazu.parsingVoidFile(results, fileKey, posDate, posMachineCode, uiProperties);
			    } else {
				throw new Exception("查無識別碼(" + fileKey + ")相關提貨明細檔案！");
			    }
			}	
		    }
		}
	    }catch(Exception ex){
		errorMsg = "POS作廢匯入" + srcFileName + "失敗，原因：" +  ex.toString();
		log.error(errorMsg);
		SiSystemLogUtils.createSystemLog(processName, MessageStatus.ERROR, errorMsg, executeDate, uuid, loginUser); 
	    }
	}	
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
}
