package tw.com.tm.erp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import tw.com.tm.erp.importdb.FTPFubonImportData;

public class FtpUtils {
    private static final Log log = LogFactory.getLog(FtpUtils.class);
    
    private FTPClient fClient;   
    private String username;    //登錄FTP 用戶名   
    private String password;    //用户密码，支持强密码   
    private String url;         //FTP 地址   
    private int port;   
    private String remoteDir;   
    public FTPClient getFClient() {
	return fClient;
    }
    public void setFClient(FTPClient client) {
	fClient = client;
    }
    public FtpUtils(String username, String password, String url, int port,   
	    String remoteDir) {   
	super();   
	this.username = username;   
	this.password = password;   
	this.url = url;   
	this.port = port;   
	this.remoteDir = remoteDir;   
	fClient = new FTPClient();   
    }   
    /**  
     * 登陆FTP服务器  
     * @throws Exception   
     */  
    public boolean login()throws Exception{   
	boolean flag = false;   
	try {   
	    //FTP參數設定
	    FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_NT);         
	    config.setServerTimeZoneId(TimeZone.getDefault().getID());             
	    fClient.setDefaultTimeout(10000); 
	    fClient.setControlEncoding("UTF8"); 
	    fClient.enterLocalPassiveMode();//設置文件傳輸模式);//
	    
	    fClient.configure(config);    
	    if(this.port > 0) {   
		fClient.connect(url, port);   
	    } else {   
		fClient.connect(url);//到生產環境中不能用端口號   
	    }   
	    int reply = this.fClient.getReplyCode();   
	    if(!FTPReply.isPositiveCompletion(reply)) {   
		fClient.disconnect();   
		log.error("FTP server refused connection." + url );   
		// TODO    
		return false;   
	    }   
	    fClient.login(username, password);   
	    fClient.changeWorkingDirectory(this.remoteDir);   
	    fClient.setFileType(FTPClient.BINARY_FILE_TYPE);      
	    log.info("成功登錄FTP服務器：" + url +" 端口:" + port +" 目錄：" + remoteDir);   
	    flag = true;   
	} catch (SocketException e) { 
	    log.error("FTP server refused connection." + url);       
	    throw new Exception("發生Socket問題原因:"+e.toString());
	} catch (IOException e) {              
	    throw new Exception("發生IO問題原因:"+e.toString());
	}   
	return flag;   
    }
    
    /**  
     * 關閉FTP 連接  
     */    
    public void logout()throws Exception{   
	if(null != this.fClient && this.fClient.isConnected()){            
	    try {   
		fClient.logout();   
		fClient.disconnect();   
	    } catch (IOException e) {                      
		throw new Exception("嘗試登出發生問題，原因:"+e.toString()); 
	    }      
	    log.info("close FTP server .");   
	}   
    }   
	
    /**  
     * 下載文件  
     * @param localFilePath 本地文件名及路徑  
     * @param remoteFileName 遠程文件名稱  
     * @return  
     */
    public boolean downloadFile(String localFilePath, String remoteFileName)throws Exception {
	BufferedOutputStream outStream = null;
	boolean success = false;
	try {
	    log.info("localFilePath = " + localFilePath);
	    log.info("remoteFileName = " + remoteFileName);
	    
	    FileOutputStream  fos = new FileOutputStream(localFilePath);

//	    fClient.setBufferSize(1024);
	    
	    success = fClient.retrieveFile(remoteFileName, fos);
	    fos.close();
//	    outStream = new BufferedOutputStream(new FileOutputStream(
//		    localFilePath));
//	    success = fClient.retrieveFile(remoteFileName, outStream);
	    
	    log.info("是否下載文件成功 = " + success);
	} catch (FileNotFoundException e) {
	    throw new Exception("找檔案發生錯誤，原因:" + e.toString());
	} catch (IOException e) {
	    throw new Exception("存取檔案發生錯誤，原因:" + e.toString());
	} finally {
	    if (outStream != null) {
		try {
		    outStream.close();
		} catch (IOException e) {
		    throw new Exception("嘗試關閉資源發生錯誤，原因:" + e.toString());
		}
	    }
	}
	return success;
    }      
    
    /**  
     * 上傳文件  
     * @param localFilePath 本地文件名及路徑  
     * @param remoteFileName 遠程文件名稱  
     * @return  
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)throws Exception {
	BufferedInputStream inStream = null;
	boolean success = false;
	try {
	    log.info("localFilePath = " + localFilePath);
	    log.info("remoteFileName = " + remoteFileName);
	    
	    inStream = new BufferedInputStream(new FileInputStream(localFilePath));

//	    fClient.setBufferSize(1024);
	    
	    success = fClient.storeFile(remoteFileName, inStream);
	    inStream.close();
//	    outStream = new BufferedOutputStream(new FileOutputStream(
//		    localFilePath));
//	    success = fClient.retrieveFile(remoteFileName, outStream);
	    
	    log.info("是否上傳文件成功 = " + success);
	} catch (FileNotFoundException e) {
	    throw new Exception("找檔案發生錯誤，原因:" + e.toString());
	} catch (IOException e) {
	    throw new Exception("存取檔案發生錯誤，原因:" + e.toString());
	} finally {
	    if (inStream != null) {
		try {
		    inStream.close();
		} catch (IOException e) {
		    throw new Exception("嘗試關閉資源發生錯誤，原因:" + e.toString());
		}
	    }
	}
	return success;
    }
    
}
