package tw.com.tm.erp.exportdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

import com.jcraft.jsch.ChannelSftp;
import com.taipeifubon.util.TfbZip;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerCardDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.FtpUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SftpUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.TxtFormatUtil;
import tw.com.tm.erp.utils.TxtUtil;

public class FTPFubonExportData {
    private static final Log log = LogFactory.getLog(FTPFubonExportData.class);
    
    private static final String CONFIG_FILE = "../exportdb/DB2txt.properties";
    
    public static final String KEY_UUID = "uuid";
    public static final String KEY_EXECUTE_DATE = "executeDate";
    private static final String KEY_SUFFIX_DATE = "SUFFIX_DATE";
    
    public static final String PROCESS_NAME = "FUBON_EXPORT";
    private static final String REPLY_FIEL_NAME = "TASACDNO.";
    private static final String BODY = "D";
    
    private BuAddressBookService buAddressBookService;
    private BuAddressBookDAO buAddressBookDAO;
    private BuCustomerDAO buCustomerDAO;
    private BuCustomerCardDAO buCustomerCardDAO;
    public void setBuCustomerCardDAO(BuCustomerCardDAO buCustomerCardDAO) {
        this.buCustomerCardDAO = buCustomerCardDAO;
    }

    public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
        this.buCustomerDAO = buCustomerDAO;
    }

    public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
        this.buAddressBookDAO = buAddressBookDAO;
    }
    public void setBuAddressBookService(BuAddressBookService buAddressBookService) {
        this.buAddressBookService = buAddressBookService;
    }
    
    public void execute(){
    	Date executeDate = new Date();
    	String uuid = null;

    	String yyyyMMddHHMMSS = DateUtils.format(executeDate, "yyyyMMddHHmmss"); 

    	String suffixDate = DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 

    	uuid = UUID.randomUUID().toString();
    	StringBuffer record = new StringBuffer();
    	List<String> records = new ArrayList();

    	Map parameterMap = new HashMap();
    	parameterMap.put(KEY_UUID, uuid);
    	parameterMap.put(KEY_EXECUTE_DATE, executeDate);
    	parameterMap.put(KEY_SUFFIX_DATE, suffixDate);
    	try {
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "富邦 FTP上載開始", executeDate, uuid, "SYS");
    		TxtFormatUtil.loadConfig(CONFIG_FILE);
    		LinkedMap configMap = TxtFormatUtil.getTxtInfo(PROCESS_NAME);

    		// 要上傳的目錄
    		String targetRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
    			targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
    		}

    		records.add("HTASA "+yyyyMMddHHMMSS+ CommonUtils.insertCharacterWithLimitedLength("", 490, 490, CommonUtils.SPACE, "L") ); // 單頭

    		// 撈資料庫的Table 轉成 txt
    		List<BuCustomerCard> buCustomerCards = buCustomerCardDAO.findByProperty("BuCustomerCard", "and brandCode = ? and enable = ? and isTransmission = ?", new Object[]{"T2", "Y","N"}); // and cardNo is not null
    		//log.info("buCustomerCards = "+ buCustomerCards);
    		//log.info("buCustomerCards.size() = "+ buCustomerCards.size());
    		int i = 0;
    		for (BuCustomerCard buCustomerCard : buCustomerCards) {
    			//log.info("buCustomerCard = "+buCustomerCard);
    			BuCustomer buCustomer = (BuCustomer)buCustomerDAO.findFirstByProperty("BuCustomer","and id.brandCode = ? and id.customerCode = ?", new Object[]{buCustomerCard.getBrandCode(), buCustomerCard.getCustomerCode()});
    			if(null == buCustomer){
    				throw new Exception("查無品牌:" + buCustomerCard.getBrandCode() + " 卡號:"+buCustomerCard.getCustomerCode()+"的客戶資料");
    			}

    			BuCustomerId buCustomerId = buCustomer.getId();
    			//log.info("buCustomer.getAddressBookId() = "+ buCustomer.getAddressBookId());
    			BuAddressBook buAddressBook = (BuAddressBook)buAddressBookDAO.findFirstByProperty("BuAddressBook", "and addressBookId = ?", new Object[]{buCustomer.getAddressBookId()});
    			if(null == buAddressBook){
    				throw new Exception("查 BuAddressBook 無通訊號碼:"+buCustomer.getAddressBookId()+"的通訊資料");
    			}

    			record.append(getLine(configMap, buCustomerCard, buCustomerId, buAddressBook));
    			//log.info(record);
    			records.add(record.toString());	// 單身
    			i++;
    			record.delete(0, record.length());
    		}

    		records.add("T"+CommonUtils.insertCharacterWithLimitedLength(String.valueOf(i), 9, 9, CommonUtils.ZERO, "L") + CommonUtils.insertCharacterWithLimitedLength("", 500, 500, CommonUtils.SPACE, "L") ); // 單尾
    		TxtUtil.exportTxt(targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".txt", records);

    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "資料庫匯出txt("+targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".txt"+")檔成功", executeDate, uuid, "SYS");

    		// 壓縮和加密 
    		String zipPassword = null;
    		if(configMap.containsKey(PROCESS_NAME + "_ZIP_PASSWORD")){
    			zipPassword = (String)configMap.get(PROCESS_NAME + "_ZIP_PASSWORD" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_ZIP_PASSWORD 配置");
    		}
    		//log.info(targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".txt");
    		//log.info(targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".zip");
    		log.info("zipPassword = " + zipPassword);
    		TfbZip.zipSingleFileWithPassword(targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".txt", targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".ZIP" , zipPassword);

    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "壓縮txt("+targetRoot+"/"+REPLY_FIEL_NAME+suffixDate+".txt"+")檔成功", executeDate, uuid, "SYS");

    		// 上傳備份的地方
    		String backupRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_BACKUP")){
    			backupRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_BACKUP" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_BACKUP 配置");
    		}

    		// 上傳error的地方
    		String errorRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_ERROR")){
    			errorRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ERROR" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ERROR 配置");
    		}

    		File folder = new File(targetRoot);		// 上傳存檔
    		backupRoot +=  "/"+yyyyMMddHHMMSS;
    		errorRoot +=  "/"+yyyyMMddHHMMSS;
    		File folder_bak = new File(backupRoot);	// 上傳備份
    		File folder_err = new File(errorRoot);	// 上傳err

    		if(!folder.isDirectory()){
    			folder.mkdir();
    		}
    		if(!folder_bak.isDirectory()){
    			folder_bak.mkdir();
    		}	
    		if(!folder_err.isDirectory()){
    			folder_err.mkdir();
    		}

    		if(folder.isDirectory()){
    			String[] list = folder.list();
    			if(list.length == 0){
    				log.info("本次無匯出的檔案");
    			}else{
    				for (int j = 0; j < list.length; j++) {
    					log.info("此次上傳的檔案數為  = " + list.length);
    					log.info("檔案  = " + folder + "/" + list[j]);
    					log.info("上傳中...");
    					try {
    						// 關鍵
    						upload(configMap, parameterMap);
    						SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "上傳檔案成功", executeDate, uuid, "SYS");

    						// 更新上傳成功的
    						buAddressBookService.updateTxtBuCustomerCard(parameterMap);
    						SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "更新聯名卡是否再上傳欄位結束", executeDate, uuid, "SYS");
    						//log.info("上傳完...");
    						File declarationFile = new File(targetRoot+ "/" + list[j]);
    						moveFilesToDestination(declarationFile, backupRoot);
    					} catch (Exception e) {
    						SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"富邦聯名卡上傳發生錯誤，原因 ：" + e.getMessage(),executeDate, uuid, "SYS");
    						log.error(e.getMessage());
    						File declarationFile = new File(targetRoot+ "/" + list[j]);
    						moveFilesToDestination(declarationFile, errorRoot);
    					}
    				}
    			}
    		}else{
    			throw new Exception("此路徑應為路徑而不是路徑");
    		}
    	} catch (Exception e) {
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "FTP富邦上傳發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
    		log.error(e.getMessage());
    	} finally{
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "富邦 FTP上載結束", executeDate, uuid, "SYS");
    		// 寄給維護人員
    		MailUtils.systemErrorLogSendMail(PROCESS_NAME, MessageStatus.LOG_ERROR,uuid);
    	}
    }
    
    /**
     * 取得每行
     * @param map
     * @param head
     * @return
     * @throws Exception
     */
    public StringBuffer getLine(LinkedMap configMap, BuCustomerCard buCustomerCard, BuCustomerId buCustomerId, BuAddressBook buAddressBook) throws Exception{
	StringBuffer sb = new StringBuffer(BODY);
	int i = 1;
	try {
	    Map beanMap = new HashMap();
	    beanMap.put("BuCustomerCard", buCustomerCard);
	    beanMap.put("BuCustomerId", buCustomerId);
	    beanMap.put("BuAddressBook", buAddressBook);
	    
	    log.info("buAddressBook = " + buAddressBook);
	    
	    String[] fields = (String[])configMap.get(REPLY_FIEL_NAME+TxtFormatUtil.FIELD);
	    for (String fieldRow : fields) { // 每個欄位配置
		String[] fieldAttr = StringTools.StringToken(fieldRow, TxtFormatUtil.SYMBOL); 

		log.info("欄位 = " + fieldAttr[0]);
		log.info("bean = " + fieldAttr[1]);
		log.info("長度  = " + fieldAttr[2]);
		log.info("對齊 = " + fieldAttr[3]);
		
		if(beanMap.containsKey(fieldAttr[1])){	// 取得對應的bean, 如果fieldAttr[1]等於buAddressBook物件則傳buAddressBook,反之傳buCustomer
		    log.info("1 = 1");
		    Object obj = PropertyUtils.getProperty(beanMap.get(fieldAttr[1]), fieldAttr[0]);
		    log.info("1 = 2");
		    if( "birthdayMonth".equals(fieldAttr[0])){
			NumberFormat formatter = new DecimalFormat("00"); 
			obj = formatter.format(obj);
		    }else if("birthdayDay".equals(fieldAttr[0])){
			NumberFormat formatter = new DecimalFormat("00"); 
			obj = formatter.format(obj);
		    }
		    
		    sb.append(CommonUtils.insertCharacterWithLimitedLength(AjaxUtils.getPropertiesValue(obj,""), NumberUtils.getInt(fieldAttr[2]), NumberUtils.getInt(fieldAttr[2]), CommonUtils.SPACE, fieldAttr[3]));
		    log.info("1 = 3");
		    i++;
		}else{
		    log.info("欄位查無對應的bean:" + fieldAttr[1]);
		}
		log.info("1 = 4");
	    }	
	    sb.append(CommonUtils.insertCharacterWithLimitedLength( " ", 452, 452, CommonUtils.SPACE, "L") );
	} catch (Exception e) {
	    throw new Exception("組合第"+i+"筆發生問題 ，原因:"+e.toString());
	}
	return sb;
    }
    
    /**
     * 上傳
     * @param processName
     * @param targetPath
     * @param configMap
     * @return
     * @throws Exception
     */
    private boolean upload(LinkedMap configMap, Map parameterMap)throws Exception{
	boolean result = false;
	boolean hasUpload = false;
	Map fileMap = new HashMap();
	try{
	String suffixDate = (String)parameterMap.get(KEY_SUFFIX_DATE);
	
	String account = null;
	if(configMap.containsKey(PROCESS_NAME + "_ACCOUNT" )){
	    account = (String)configMap.get(PROCESS_NAME + "_ACCOUNT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_ACCOUNT 配置");
	}
	
	String password = null;
	if(configMap.containsKey(PROCESS_NAME + "_PASSWORD")){
	    password = (String)configMap.get(PROCESS_NAME + "_PASSWORD" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_PASSWORD 配置");
	}
	
	String url = null;
	if(configMap.containsKey(PROCESS_NAME + "_URL")){
	    url = (String)configMap.get(PROCESS_NAME + "_URL" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_URL 配置");
	}
	
	String port = null;
	if(configMap.containsKey(PROCESS_NAME + "_PORT")){
	    port = (String)configMap.get(PROCESS_NAME + "_PORT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_PORT 配置");
	}
	
	String upLoadRoot = null;
	if(configMap.containsKey(PROCESS_NAME + "_UPLOAD_ROOT")){
	    upLoadRoot = (String)configMap.get(PROCESS_NAME + "_UPLOAD_ROOT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_UPLOAD_ROOT 配置");
	}
	    
	// 要上傳的檔案
	String targetRoot = null;
	if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
	    targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
	}else{
	    throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
	}
	
	SftpUtils sftp = new SftpUtils(account,password,url,NumberUtils.getInt(port),upLoadRoot);
	// 登入

	if(sftp.login()){
		log.info("開始上傳!!!!");
		Vector<ChannelSftp.LsEntry> vector = sftp.listFiles();
		
		for(ChannelSftp.LsEntry ftpFiles : vector){
			String remoteFileName = ftpFiles.getFilename().toString();
			fileMap.put(remoteFileName, remoteFileName);
		}
		
		File folder = new File(targetRoot);	// 存放地點存檔
		if(folder.isDirectory()){
			String[] list = folder.list();
		    if(list.length == 0){
			    log.info("無上傳的檔案");
		    }else{
		    	sftp.changeDirectory();
		    	for (String fileName : list) {
		    		log.info("檔案 = " + fileName);
			    		
		    		if(!fileMap.containsKey(fileName)){
		    			// 上傳每個文件
					    if(fileName.lastIndexOf("ZIP") > -1 && fileName.lastIndexOf(suffixDate) > -1){ // 上傳zip 且是當天的日期
					    	if(sftp.upload(targetRoot+"/"+fileName)){	
							    log.info("上傳文件成功");
							    result = true;
							}else{
							    log.info("上傳文件失敗");
							    result = false;
							}
					    }
		    		}else{
		    			hasUpload = true;
					    log.info("檔案: "+fileName + "已曾經傳過");
		    		}
		    	}
		    }
		}else{
			throw new Exception("此路徑不是目錄");
	    }
	}else{
	    throw new Exception("登入失敗");
	}

	// 登出
	sftp.logout();

	}catch(Exception e){
		e.printStackTrace();
		log.error("上傳檔案失敗，原因：" + e.toString()); 
	}
	return result;
    }

    /**
     * 將檔案移至目的地
     * @param declarationFile
     * @param moveRoot
     */
    private void moveFilesToDestination(File declarationFile, String moveRoot){
	try {
	    
	    FileUtils.copyFileToDirectory(declarationFile, new File(moveRoot));
//	    log.info("移檔完...");

	    declarationFile.delete();
//	    log.info("刪檔完...");
	} catch (Exception e) {
	    log.error("搬移並刪除檔案失敗，原因：" + e.toString());  
	}
    }
}
