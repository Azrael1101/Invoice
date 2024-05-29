package tw.com.tm.erp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import tw.com.tm.erp.importdb.FTPFubonImportData;
import com.jcraft.jsch.Channel; 
import com.jcraft.jsch.ChannelSftp; 
import com.jcraft.jsch.JSch; 
import com.jcraft.jsch.Session; 
import com.jcraft.jsch.SftpException; 

public class SftpUtils {
    private static final Log log = LogFactory.getLog(SftpUtils.class);
      
    private String username;    //登錄FTP 用戶名   
    private String password;    //用户密码，支持强密码   
    private String url;         //FTP 地址   
    private int port; 
    //private String host;
    private String remoteDir; 
    private ChannelSftp sftp;
    private Session sshSession;
    private Channel channel;

    public ChannelSftp getSftp() {
		return sftp;
	}
	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}
	public SftpUtils(String username, String password, String url, int port,   
	    String remoteDir) {    
	this.username = username;   
	this.password = password;   
	this.url = url;   
	this.port = port;   
	this.remoteDir = remoteDir;
	sftp = new ChannelSftp();   
    }   
    /**  
     * 登陆FTP服务器  
     * @throws Exception   
     */  
    public boolean login()throws Exception{   
    	boolean flag = false;
    	//ChannelSftp sftp = null; 
	    try {   
		    //FTP參數設定
			JSch jsch = new JSch(); 
			jsch.getSession(username, url, port); 
			sshSession = jsch.getSession(username, url, port); 
			log.info("Session created."); 
			sshSession.setPassword(password); 
			Properties sshConfig = new Properties(); 
			sshConfig.put("StrictHostKeyChecking", "no"); 
			sshSession.setConfig(sshConfig); 
			sshSession.connect(); 
			log.info("Session connected."); 
			log.info("Opening Channel."); 
			channel = sshSession.openChannel("sftp"); 
			channel.connect();
			sftp = (ChannelSftp) channel; 
			log.info("Connected to " + url + ".");      
		    log.info("成功登錄FTP服務器：" + url +" 端口:" + port +" 目錄：" + remoteDir);   
		    flag = true;   
		} catch (Exception e) { 
		    log.error("FTP server refused connection." + url);       
		    throw new Exception("發生Socket問題原因:"+e.toString());
		}
		
		return flag;   
	    
    }
    
    /**  
     * 關閉FTP 連接  
     */    
    public void logout()throws Exception{   
		if(null != this.sftp && this.sftp.isConnected()){            
		    try {   
		    	sftp.exit();   
		    	sftp.disconnect();   
		    	sftp=null;
		    	channel.disconnect();
		    	channel = null;
		    	sshSession.disconnect();
		    	sshSession = null;
		    } catch (Exception e) {                      
		    	throw new Exception("嘗試登出發生問題，原因:"+e.toString()); 
		    }      
		    log.info("close FTP server .");   
		}   
    }     
    
    /**  
     * 上傳文件  
     * @param localFilePath 本地文件名及路徑  
     * @param remoteFileName 遠程文件名稱  
     * @return  
     */
    public boolean upload(String localFilePath)throws Exception {
	BufferedInputStream inStream = null;
	boolean success = false;
	try {
	    File file=new File(localFilePath); 
	    FileInputStream fis = new FileInputStream(file);
	    sftp.put(fis, file.getName()); 
	    fis.close();
	    success = true;
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
    
    /** 
    * 下載文件 
    * @param directory 下載目錄 
    * @param downloadFile 下載的文件 
    * @param saveFile 存在本地的路徑 
    * @param sftp 
    */ 
    public boolean download(String downloadFile,String saveFile) {
    	boolean success = false;
	    try { 
		    File file=new File(saveFile); 
		    FileOutputStream fos = new FileOutputStream(file);
		    sftp.get(downloadFile, fos); 
		    fos.close();
		    success = true;
	    } catch (Exception e) { 
	    	e.printStackTrace(); 
	    }
		return success;
    }
    
    /** 
    * 列出目錄下的文件 
    * @param directory 要列出的目錄 
    * @param sftp 
    * @return 
    * @throws SftpException 
    */  
    public Vector listFiles() throws SftpException{ 
    	return sftp.ls(remoteDir); 
    }
    
    public void changeDirectory() throws SftpException{ 
    	sftp.cd(remoteDir); 
    }
    
}
