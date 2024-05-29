package tw.com.tm.erp.importdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import com.jcraft.jsch.ChannelSftp; 
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.util.StringUtils;

import com.taipeifubon.util.TfbZip;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerCard;
import tw.com.tm.erp.hbm.bean.BuCustomerCardEvent; 
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.service.BuAddressBookService;
import tw.com.tm.erp.hbm.service.BuCustomerWithAddressViewService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.FtpUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SftpUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.TxtFormatUtil;

import com.jcraft.jsch.ChannelSftp; 

public class FTPFubonImportData {
    private static final Log log = LogFactory.getLog(FTPFubonImportData.class);
    
    private BuAddressBookService buAddressBookService;
    //private BuCustomerWithAddressViewService buCustomerWithAddressViewService;
    //private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    //private BuAddressBookDAO buAddressBookDAO;
    //private BuCustomerDAO buCustomerDAO;
    
    public static String KEY_UUID = "UUID";  
    public static String KEY_EXECUTE_DATE = "EXECUTE_DATE"; 
    public static String KEY_SUFFIX_DATE = "SUFFIX_DATE"; 
    
    private static String CARD_MAP_FIEL_NAME = "FUBCDNO."; // 卡號對應檔
    private static String CARD_FIEL_NAME = "FUBCUS."; // 卡片資料檔
    private static String EVENT_FIEL_NAME = "FUBABR."; // 事故資料檔
    
    public static String PROCESS_NAME = "FUBON_IMPORT"; 
    private static final String CONFIG_FILE = "../importdb/txt2DB.properties";
   
    public void setBuAddressBookService(BuAddressBookService buAddressBookService) {
        this.buAddressBookService = buAddressBookService;
    }
    
    /*
    public void setBuCustomerWithAddressViewService(
    	BuCustomerWithAddressViewService buCustomerWithAddressViewService) {
        this.buCustomerWithAddressViewService = buCustomerWithAddressViewService;
    }

    public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
        this.buCustomerDAO = buCustomerDAO;
    }

    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
        this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
        this.buAddressBookDAO = buAddressBookDAO;
    }
    */

    public void execute(Map map){
    	Date executeDate = new Date(); 
    	String uuid = null;
    	uuid = UUID.randomUUID().toString();
    	
    	String yyyyMMddHHMMSS = DateUtils.format(executeDate, "yyyyMMddHHmmss"); 
    	String suffixDate = DateUtils.format(executeDate, DateUtils.C_DATA_PATTON_YYYYMMDD); 

    	Map parameterMap = new HashMap();
    	parameterMap.put(KEY_UUID, uuid);
    	parameterMap.put(KEY_EXECUTE_DATE, executeDate);
    	parameterMap.put(KEY_SUFFIX_DATE, suffixDate);

    	String fileNameDate = null; // YYYYMMDD
    	if(map.containsKey("FILE_NAME_DATE")){
    		fileNameDate = (String)map.get("FILE_NAME_DATE");
    		parameterMap.put(KEY_SUFFIX_DATE, fileNameDate);
    	}

    	try {
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "富邦 FTP下載開始", executeDate, uuid, "SYS");
    		TxtFormatUtil.loadConfig(CONFIG_FILE);
    		LinkedMap configMap = TxtFormatUtil.getTxtInfo(PROCESS_NAME);

    		// 下載過來的目錄
    		String targetRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
    			targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
    		}

    		// 存放備份的地方
    		String backupRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_BACKUP")){
    			backupRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_BACKUP" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_BACKUP 配置");
    		}

    		// 存放error的地方
    		String errorRoot = null;
    		if(configMap.containsKey(PROCESS_NAME + "_TARGET_ERROR")){
    			errorRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ERROR" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ERROR 配置");
    		}

    		File folder = new File(targetRoot);	// 下載存檔
    		backupRoot +=  "/"+yyyyMMddHHMMSS;
    		errorRoot +=  "/"+yyyyMMddHHMMSS;
    		File folder_bak = new File(backupRoot);	// 下載備份
    		File folder_err = new File(errorRoot);	// 下載err

    		if(!folder.isDirectory()){
    			folder.mkdir();
    		}
    		if(!folder_bak.isDirectory()){
    			folder_bak.mkdir();
    		}	 
    		if(!folder_err.isDirectory()){
    			folder_err.mkdir();
    		}

    		// 	FTP下載
    		//log.info("下載中...");
    		if(download(configMap, parameterMap)){
    			SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "下載檔案成功", executeDate, uuid, "SYS");
    			// 解壓縮
    			unZip(configMap, targetRoot, folder);
    			SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "解壓縮檔案成功", executeDate, uuid, "SYS");

    			if(folder.isDirectory()){
    				String[] list = sortFileNames(folder.list()); // 將事故資料卡往最後放
    				if(list.length == 0){
    					log.info("本次無匯入的檔案");
    					SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "本次無匯入的檔案", executeDate, uuid, "SYS");
    				}else{
//  					log.info("此次匯入的檔案數為  = " + list.length);
    					for (int i = 0; i < list.length; i++) {
    						//log.info("檔案  = " + folder + "/" + list[i]);
//  						log.info("讀檔中...");
    						File declarationFile = new File(targetRoot+ "/" + list[i]);
    						String[] fileNames = (String[])configMap.get( PROCESS_NAME + TxtFormatUtil.FILE_NAMES);
    						BufferedReader in = null; 
    						try {
    							for (String fileName : fileNames) {	// 每個檔案從配置檔找到對應的配置
//  								log.info("fileName:" + fileName );
//  								log.info("list[i]:" + list[i] );
//  								log.info("suffixDate:" + suffixDate );
    								if(list[i].indexOf(fileName) > -1 && list[i].lastIndexOf("ZIP") == -1){ // suffixDate 排除zip
    									//log.info("解析檔案:" + list[i] );
//  									InputStreamReader isr = new InputStreamReader(new FileInputStream(declarationFile), "UTF-8");
    									in =  new BufferedReader(new FileReader(declarationFile)); //  targetRoot + "/" + list[i]

    									if(CARD_MAP_FIEL_NAME.equals(fileName)){ // 卡號對應檔
    										parseCardMapTxt(in, parameterMap, configMap);
    									}else if(CARD_FIEL_NAME.equals(fileName)){ // 卡片資料檔
    										// 只解析要的檔
    										parseTxtToAddressBookAndBuCustomerCardBean(in, parameterMap, configMap);
    									}else if(EVENT_FIEL_NAME.equals(fileName)){ // 檔案事故檔
    										parseTxtToBuCustomerCardEventBean(in, parameterMap, configMap);
    									}
    									in.close();
    									break;
    								}else{
    									//log.info("不用解析的檔案:" + list[i] );
    								}
    							}	
    							moveFilesToDestination(declarationFile, folder_bak);
    						} catch (Exception e) {
    							log.error(e.getMessage());
    							SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"富邦聯名卡匯入發生錯誤，原因 ：" + e.getMessage(),executeDate, uuid, "SYS");
    							moveFilesToDestination(declarationFile, folder_err);
    						}finally {
    							if (in != null) {
    								try {
    									in.close();
    								} catch (IOException e) {
    									throw new Exception("嘗試關閉檔案資源發生錯誤，原因:" + e.toString());
    								}
    							}
    						}
    					} // 每個檔案
    					SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "下載檔案更新資料庫結束", executeDate, uuid, "SYS");
    				} // 目錄下總共檔案
    			}else{
    				throw new Exception("此路徑應為路徑而不是檔案");
    			}
    		}else{
    			throw new Exception("FTP富邦下載失敗");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error(e.getMessage());
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "FTP富邦批次匯入發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
    	} finally{
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_INFO, "富邦 FTP下載結束", executeDate, uuid, "SYS");
    		// 寄給維護人員
    		MailUtils.systemErrorLogSendMail(PROCESS_NAME, MessageStatus.LOG_ERROR,uuid);
    	}
    }
    
    /**
     * 下載
     * @param processName
     * @param targetPath
     * @param configMap
     * @return
     * @throws Exception
     */
    private boolean download(LinkedMap configMap, Map parameterMap)throws Exception{
    	boolean result = false;

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

    	String downLoadRoot = null;
    	if(configMap.containsKey(PROCESS_NAME + "_DOWNLOAD_ROOT")){
    		downLoadRoot = (String)configMap.get(PROCESS_NAME + "_DOWNLOAD_ROOT" );
    	}else{
    		throw new Exception("查無此:"+PROCESS_NAME + "_DOWNLOAD_ROOT 配置");
    	}

    	// 要存放下載過來的目錄
    	String targetRoot = null;
    	if(configMap.containsKey(PROCESS_NAME + "_TARGET_ROOT")){
    		targetRoot = (String)configMap.get(PROCESS_NAME + "_TARGET_ROOT" );
    	}else{
    		throw new Exception("查無此:"+PROCESS_NAME + "_TARGET_ROOT 配置");
    	}

    	SftpUtils sftp = new SftpUtils(account,password,url,NumberUtils.getInt(port),downLoadRoot);
    	// 登入
    	if(sftp.login()){
    		Vector<ChannelSftp.LsEntry> vector = sftp.listFiles();
    		
    		File folder = new File(targetRoot);	// 存放地點存檔
    		
    		if(folder.isDirectory()){
    			sftp.changeDirectory();
    			for(ChannelSftp.LsEntry ftpFiles : vector){
        			//log.info(ftpFiles.getFilename().toString());
        			String fileName = ftpFiles.getFilename().toString(); 
            		if(fileName.indexOf(suffixDate) > -1 ){ // 只抓當天的日期
            			log.info("--------------"+fileName+"--------------"); 
    					// 下載每個文件
        				if(sftp.download(fileName, targetRoot+"\\"+fileName)){
    						//log.info("下載文件:"+ fileName +" 成功");
    						result = true;
    					}else{
    						//log.info("下載文件:"+ fileName +" 失敗");
    						result = false;
    					}
    				}
        		}
    		}else{
    			throw new Exception("此路徑不是目錄");
    		}
    	}
    	
    	// 登出
    	sftp.logout();
    	
    	return result;
    }
    
    /**
     * 解析卡號對應檔
     * @param in
     * @param parameterMap
     * @param configMap
     * @throws Exception
     */
    public void parseCardMapTxt(BufferedReader in, Map parameterMap, Map configMap) throws Exception{
    	log.info(" parseCardMapTxt start ");
    	String uuid = (String)parameterMap.get(KEY_UUID);
    	Date executeDate = (Date)parameterMap.get(KEY_EXECUTE_DATE);
    	String line;
    	StringBuffer sb = new StringBuffer();
    	try{
    		while((line = in.readLine())!= null ){
    			BuAddressBook buAddressBook = new BuAddressBook();
    			BuCustomerCard buCustomerCard = new BuCustomerCard();
    			Map beanMap = new HashMap();
    			beanMap.put("BuAddressBook", buAddressBook);
    			beanMap.put("BuCustomerCard", buCustomerCard);

    			//log.info("line = " + line);
    			byte[] lineBytes = line.getBytes();

    			//log.info("lineBytes = " + lineBytes);
    			sb.append(line);
    			switch (sb.charAt(0)) {			// 只讀第一行字為D的
    			case 'D':
    				String[] fields = (String[])configMap.get(CARD_MAP_FIEL_NAME+TxtFormatUtil.FIELD);
    				for (String fieldRow : fields) { // 每個欄位配置
    					String[] fieldAttr = StringTools.StringToken(fieldRow, TxtFormatUtil.SYMBOL); 
    					/*
					    log.info("欄位  = " + fieldAttr[0]);
					    log.info("bean = " + fieldAttr[1]);
					    log.info("起始  = " + fieldAttr[2]);
					    log.info("結束  = " + fieldAttr[3]);
    					 */
    					Object importObj = null;
    					if(beanMap.containsKey(fieldAttr[1])){	// 取得對應的bean, 如果fieldAttr[1]等於buAddressBook物件則傳buAddressBook,反之傳buCustomer
    						importObj = beanMap.get(fieldAttr[1]);
    					}else{
    						log.info("欄位查無對應的bean:" + fieldAttr[1]);
    					}

    					byte[] fieldBytes = new byte[NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1];

    					System.arraycopy(lineBytes, NumberUtils.getInt(fieldAttr[2]), fieldBytes, 0, (NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1));
    					String fieldValue = new String(fieldBytes).trim();
    					setObjectToBean(importObj,fieldAttr[0],fieldValue);
    				}
    				try {
    					buAddressBookService.saveTxtCardMap(buAddressBook, buCustomerCard, parameterMap);
    				} catch (Exception e) {
    					log.error(e.getMessage());
    					SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "匯入卡號對應檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
    				}
    				break;
    			default:
    				log.info(" 非需要區塊:" + sb.charAt(0));
    			break;
    			}
    			sb.delete(0, sb.length());
    			log.info("cardNo = " + buCustomerCard.getCardNo()); 
    		} 
    	}catch(Exception e){
    		log.error(e.getMessage());
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "富邦聯名卡匯入卡號對應檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
//  		throw new Exception(e.getMessage());
    	}
    }
    
    
    /**
     * 將Txt轉成資料庫對應AddressBook和BuCustomerCard的Bean
     * @param in
     * @param parameterMap
     * @param configMap
     * @throws Exception
     */
    public void parseTxtToAddressBookAndBuCustomerCardBean(BufferedReader in, Map parameterMap, Map configMap) throws Exception{
    	log.info(" executeRead start ");
    	String uuid = (String)parameterMap.get(KEY_UUID);
    	Date executeDate = (Date)parameterMap.get(KEY_EXECUTE_DATE);
    	String line;
    	StringBuffer sb = new StringBuffer();
    	try{
    		while((line = in.readLine())!= null ){
    			BuAddressBook buAddressBook = new BuAddressBook();
    			BuCustomerCard buCustomerCard = new BuCustomerCard();
    			Map beanMap = new HashMap();
    			beanMap.put("BuAddressBook", buAddressBook);
    			beanMap.put("BuCustomerCard", buCustomerCard);
    			byte[] lineBytes = line.getBytes();
    			sb.append(line);
    			switch (sb.charAt(0)) {			// 只讀第一行字為D的
    			case 'D':
    				String[] fields = (String[])configMap.get(CARD_FIEL_NAME+TxtFormatUtil.FIELD);
    				for (String fieldRow : fields) { // 每個欄位配置
    					
    					String[] fieldAttr = StringTools.StringToken(fieldRow, TxtFormatUtil.SYMBOL); 
    					
    					/*
    					log.info("欄位  = " + fieldAttr[0]);
    					log.info("bean = " + fieldAttr[1]);
    					log.info("起始  = " + fieldAttr[2]);
    					log.info("結束  = " + fieldAttr[3]);
    					*/
    					
    					Object importObj = null;
//  					Iterator beanIt = beanMap.keySet().iterator();
//  					while (beanIt.hasNext()) {	// 取得對應的bean, 如果fieldAttr[1]等於buAddressBook物件則傳buAddressBook,反之傳buCustomer
//  					String beanKey = (String) beanIt.next();
//  					if(beanKey.equals(fieldAttr[1])){
//  					importObj = beanMap.get(beanKey);
//  					}
//  					}
    					if(beanMap.containsKey(fieldAttr[1])){	// 取得對應的bean, 如果fieldAttr[1]等於buAddressBook物件則傳buAddressBook,反之傳buCustomer
    						importObj = beanMap.get(fieldAttr[1]);
    					}else{
    						log.info("欄位查無對應的bean:" + fieldAttr[1]);
    					}

    					byte[] fieldBytes = new byte[NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1];
//  					log.info("lineBytes = " + new String(lineBytes));
//  					log.info("fieldBytes = " + new String(fieldBytes));
//  					log.info("System.arraycopy(a," + fieldAttr[3] + ", b, 0, " + (NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1)+")");
    					System.arraycopy(lineBytes, NumberUtils.getInt(fieldAttr[2]), fieldBytes, 0, (NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1));
    					String fieldValue = new String(fieldBytes).trim();
    					log.info("fieldName = " + fieldAttr[0] + " fieldValue = " + fieldValue);
    					setObjectToBean(importObj,fieldAttr[0],fieldValue);
    				}
    				try {
    					buAddressBookService.saveTxtAddressBookAndBuCustomerCardBean(buAddressBook, buCustomerCard);
    				} catch (Exception e) {
    					log.error(e.getMessage());
    					SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "匯入卡片基本檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
    				}
    				break;
    			default:
    				log.info(" 非需要區塊:" + sb.charAt(0));
    			break;
    			}
    			sb.delete(0, sb.length());
    			log.info("cardNo = " + buCustomerCard.getCardNo()); 
    		} 
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error(e.getMessage());
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "富邦聯名卡匯入卡片基本檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
//  		throw new Exception(e.getMessage());
    	}
    }
    
    /**
     * 將Txt轉成資料庫對應BuCustomerEvent的Bean
     * @param in
     * @param parameterMap
     * @param configMap
     * @throws Exception
     */
    public void parseTxtToBuCustomerCardEventBean(BufferedReader in, Map parameterMap, Map configMap) throws Exception{
    	log.info(" executeRead start ");
    	String uuid = (String)parameterMap.get(KEY_UUID);
    	Date executeDate = (Date)parameterMap.get(KEY_EXECUTE_DATE);
    	String line;
    	StringBuffer sb = new StringBuffer();
    	try{
    		while((line = in.readLine())!= null ){
    			BuCustomerCardEvent buCustomerCardEvent = new BuCustomerCardEvent();
    			Map beanMap = new HashMap();
    			beanMap.put("BuCustomerCardEvent", buCustomerCardEvent);

    			byte[] lineBytes = line.getBytes();
    			sb.append(line);
    			switch (sb.charAt(0)) {			// 只讀第一行字為D的
    			case 'D':
//  				log.info("區塊D");
//  				String[] fileNames = (String[])configMap.get( PROCESS_NAME + TxtFormatUtil.FILE_NAMES);
//  				for (String fileName : fileNames) {	// 每個檔案
    				String[] fields = (String[])configMap.get(EVENT_FIEL_NAME+TxtFormatUtil.FIELD);

    				for (String fieldRow : fields) { // 每個欄位配置
    					String[] fieldAttr = StringTools.StringToken(fieldRow, TxtFormatUtil.SYMBOL); 
    					/*
    					log.info("欄位  = " + fieldAttr[0]);
    					log.info("bean = " + fieldAttr[1]);
    					log.info("起始  = " + fieldAttr[2]);
    					log.info("結束  = " + fieldAttr[3]);
						*/
    					Object importObj = null;
    					if(beanMap.containsKey(fieldAttr[1])){	// 取得對應的bean, 如果fieldAttr[1]等於buAddressBook物件則傳buAddressBook,反之傳buCustomer
    						importObj = beanMap.get(fieldAttr[1]);
    					}else{
    						log.info("欄位查無對應的bean:" + fieldAttr[1]);
    					}

    					byte[] fieldBytes = new byte[NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1];
    					log.info("lineBytes = " + new String(lineBytes));
    					log.info("fieldBytes = " + new String(fieldBytes));
    					log.info("System.arraycopy(a," + fieldAttr[3] + ", b, 0, " + (NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1)+")");
    					System.arraycopy(lineBytes, NumberUtils.getInt(fieldAttr[2]), fieldBytes, 0, (NumberUtils.getInt(fieldAttr[3]) - NumberUtils.getInt(fieldAttr[2])+1));
    					String fieldValue = new String(fieldBytes).trim();
    					log.info("fieldName = " + fieldAttr[0] + " fieldValue = " + fieldValue);
    					setObjectToBean(importObj,fieldAttr[0],fieldValue);
    				}
    				try {
    					buAddressBookService.saveTxtBuCustomerCardEventBean(buCustomerCardEvent); 
    				} catch (Exception e) {
    					log.error(e.getMessage());
    					SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "匯入事故檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
    				}
    				log.info("buAddressBookService.saveTxtBuCustomerCardEventBean 結束 ");
//  				}
    				break;
    			default:
    				log.info(" 非需要區塊:" + sb.charAt(0));
    			break;
    			}
    			sb.delete(0, sb.length());
    			log.info("sb.delete 結束 ");
    		} 
    	}catch(Exception e){
    		log.error(e.getMessage());
    		SiSystemLogUtils.createSystemLog(PROCESS_NAME, MessageStatus.LOG_ERROR, "富邦聯名卡匯入事故檔發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
//  		throw new Exception(e.getMessage());
    	}
    }
    
    /**
     * 將欄位寫入bean裡
     * @param head
     * @param field
     * @param fieldValue
     * @throws Exception
     */
    private void setObjectToBean(Object head, Object field, String fieldValue)throws Exception{
    	try {
    		Class type = PropertyUtils.getPropertyType(head,(String) field);
    		if (StringUtils.hasText(fieldValue)) {
    			if (type == String.class)
    				PropertyUtils.setNestedProperty(head,(String) field, fieldValue);
    			else if (type == Long.class)
    				PropertyUtils.setNestedProperty(head,(String) field, NumberUtils.getLong(fieldValue));
    			else if (type == Double.class)
    				PropertyUtils.setNestedProperty(head,(String) field, NumberUtils.getDouble(fieldValue));
    			else if (type == Date.class) {
    				Date d = null;
    				//log.info("type = Date.class");
    				//log.info("fieldValue.trim().length() = "+ fieldValue.trim().length());
    				if (fieldValue.trim().length() > 6){
    					d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,fieldValue);
    				}else if(fieldValue.trim().length() == 6){
    					d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYMMDD, fieldValue);
    				}else if(fieldValue.trim().length() == 4){
    					d = DateUtils.parseDate("YYMM", fieldValue); 
    				}
    				PropertyUtils.setNestedProperty(head,(String) field, d);
//  				log.info("塞入結束");
    			}
//  			log.info("塞入結束1");
    		}
//  		log.info("塞入結束2");
    	} catch (Exception e) {
    		log.error("將欄位寫入bean時發生問題 ，原因:"+e.toString());
    		throw new Exception("嘗試將欄位寫入bean時發生錯誤 ，原因:"+e.toString());
    	}
    }
    
    /**
     * 解壓縮
     * @param targetRoot
     * @param folder
     */
    private void unZip(LinkedMap configMap, String targetRoot, File folder)throws Exception{
//  	TfbZip.unzipSingleFileWithPassword(targetRoot+"/"+CARD_FIEL_NAME+"20100408.zip", targetRoot , "1qaz2wsx"); // 27183523 suffixDate
//  	TfbZip.unzipSingleFileWithPassword(targetRoot+"/"+EVENT_FIEL_NAME+"20100408.zip", targetRoot , "1qaz2wsx"); // 27183523 suffixDate
    	if(folder.isDirectory()){
    		String unZipPassword = null;
    		if(configMap.containsKey(PROCESS_NAME + "_UNZIP_PASSWORD")){
    			unZipPassword = (String)configMap.get(PROCESS_NAME + "_UNZIP_PASSWORD" );
    		}else{
    			throw new Exception("查無此:"+PROCESS_NAME + "_UNZIP_PASSWORD 配置");
    		}
    		String[] list = folder.list();
    		for (String name : list) {
    			if(name.lastIndexOf("ZIP") > -1 ){
    				TfbZip.unzipSingleFileWithPassword(targetRoot+"/"+name, targetRoot , unZipPassword);
    			}
    		}
    	}else{
    		log.error("查此路徑不為目錄");
    	}
    }
    
    /**
     * 排序檔名, 以防寫入順序的問題
     * @param fileNames
     * @return
     */
    private String[] sortFileNames(String[] fileNames){
	String tmp = null;
	String[] newFileNames = fileNames.clone();
	for (int i = 0; i < newFileNames.length; i++) {
	    if(newFileNames[i].indexOf(EVENT_FIEL_NAME) > -1 && newFileNames[i].lastIndexOf("ZIP") == -1){ // 將事故資料卡移至最後
		log.info("移動前 " +newFileNames[newFileNames.length - 1]);
		tmp = newFileNames[newFileNames.length - 1];
		newFileNames[newFileNames.length - 1] = newFileNames[i];
		log.info("移動後 " +newFileNames[newFileNames.length - 1]);
		newFileNames[i] = tmp;
		break;
	    }
	}
	return newFileNames;
    }
    
    /**
     * 將檔案移至目的地
     * @param declarationFile
     * @param moveRoot
     */
    private void moveFilesToDestination(File declarationFile, File moveRoot){
    	try {
    		FileUtils.copyFileToDirectory(declarationFile, moveRoot);
//  		log.info("移檔完...");
    		declarationFile.delete();
//  		log.info("刪檔完...");
    	} catch (Exception e) {
    		log.error("搬移並刪除檔案失敗，原因：" + e.toString());  
    	}
    }
}
