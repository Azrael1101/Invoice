package tw.com.tm.erp.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.ecs.xhtml.iframe;

import tw.com.tm.erp.utils.FtpUtils;

public class Ftp_Test {

	
	static String workingDirectory = "";
	final static String _SYB = "/";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
			
			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
			ftp.login();
			
			FTPClient ftpClient = ftp.getFClient();
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			String path = workingDirectory+"T2"+_SYB+"1"+_SYB+"AE"+_SYB+"XXPM104F";
			System.out.println("path:"+path);
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + path);
			String nowPath = path;
			
			String projectPath = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/images/";
			//ftpClient.listFiles(pathname);
			for(String name:ftpClient.listNames(path)){
				System.out.println(name);
				//rtnList.add("images" + _SYB + name);
				//picName = name;
				//ftp.downloadFile(projectPath + _SYB + name, nowPath + _SYB + name);
			}
			
			//ftpGetOne("T2","1","AE","XXPM104F");
			
//			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
//			ftp.login();
//			
//			FTPClient ftpClient = ftp.getFClient();
//			workingDirectory = ftpClient.printWorkingDirectory();
//			System.out.println("workDir:"+workingDirectory);
//			
//			// into brand_code
//			String brand_code_path = "T2";
//			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + brand_code_path);
//			workingDirectory = ftpClient.printWorkingDirectory();
//			System.out.println("workDir:"+workingDirectory);
//			
//			// into item_category
//			String item_category_path = "1";
//			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_category_path);
//			workingDirectory = ftpClient.printWorkingDirectory();
//			System.out.println("workDir:"+workingDirectory);
//			
//			// into item_brand
//			String item_brand_path = "AE";
//			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_brand_path);
//			workingDirectory = ftpClient.printWorkingDirectory();
//			System.out.println("workDir:"+workingDirectory);
//			
//			// into item_code
//			String item_code_path = "005829980719F";
//			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_code_path);
//			workingDirectory = ftpClient.printWorkingDirectory();
//			System.out.println("workDir:"+workingDirectory);
//			
//			String projectPath = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/images";
//			// ls & download all item file
//			for(String name:ftpClient.listNames()){
//				System.out.println(workingDirectory + _SYB + name);
//				
//				ftp.downloadFile(projectPath + _SYB + name, workingDirectory + _SYB + name);
//				
//			}
//			
			
			//ftp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public List<Map> ftpGetByCategoryType(String brand,String category){
		List<Map> items = new ArrayList();
		try{
			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
			ftp.login();
			
			FTPClient ftpClient = ftp.getFClient();
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			String path = workingDirectory+brand+_SYB+category;
			System.out.println("path:"+path);
			String projectPath = "D:/TM/workspace/erp_dev_20210702/work/images";
			if(null != ftpClient.listNames(path)) {
				for(String sss:ftpClient.listNames(path)){
					if(ftpClient.changeWorkingDirectory(sss)){
						//dir
						for(String ss:ftpClient.listNames(sss)){
							System.out.println("ss:"+ss);
							System.out.println("itemCode:"+ss.substring(ss.lastIndexOf("/")+1));
							for(String s:ftpClient.listNames(ss)){
								if(s.toLowerCase().indexOf(".txt")<=0){
									Map ftpInfo = new HashMap();
									ftpInfo.put("itemCode", ss.substring(ss.lastIndexOf("/")+1));
									System.out.println("download s:"+s);
									System.out.println("itemFileName:"+projectPath+_SYB+ftpInfo.get("itemCode")+_SYB+s.substring(s.lastIndexOf("/")+1));
									ftpInfo.put("src", "images"+_SYB+ftpInfo.get("itemCode")+_SYB+s.substring(s.lastIndexOf("/")+1));
									File isEx = new File(projectPath+_SYB+ftpInfo.get("itemCode"));
									if(!isEx.exists()||!isEx.isDirectory()){
										isEx.mkdir();
										System.out.print("mkdir");
									}
									ftp.downloadFile(projectPath+_SYB+ftpInfo.get("itemCode")+_SYB+s.substring(s.lastIndexOf("/")+1) , s);
									items.add(ftpInfo);
								}
							}
							
						}
					}else{
						//file
						System.out.println("file path:"+sss);
					}
				}
			}
			ftp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		return items;
	}
	
	public String ftpGetOne(String brand,String category,String itemBrand,String itemCode){
		String picName = itemCode+".jpg";
		try{
			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
			ftp.login();
			
			FTPClient ftpClient = ftp.getFClient();
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			String path = workingDirectory+brand+_SYB+category+_SYB+itemBrand+_SYB+itemCode;
			System.out.println("path:"+path);
			ftpClient.changeWorkingDirectory(path);
			
			
			String projectPath = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/images";
			//pics = ftpClient.listNames(path);
			//picName = pics[0];
			System.out.println("pics[0]:");
			ftp.downloadFile(projectPath + _SYB + itemCode+".jpg" , path + _SYB + itemCode+".jpg");
			
//			for(String name:pics){
//				System.out.println(name);
//				//rtnList.add("images" + _SYB + name);
//				picName = name;
//				ftp.downloadFile(projectPath + _SYB + name.substring(name.indexOf(itemCode)) , name);
//			}
			
			ftp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		return picName;
	}
	
	public List ftpGet(String brand,String category,String itemBrand,String itemCode){
		List rtnList = new ArrayList();
		try{
			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
			ftp.login();
			
			FTPClient ftpClient = ftp.getFClient();
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			// into brand_code
			String brand_code_path = brand;
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + brand_code_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			// into item_category
			String item_category_path = category;
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_category_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			// into item_brand
			String item_brand_path = itemBrand;
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_brand_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			// into item_code
			String item_code_path = itemCode;
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_code_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("workDir:"+workingDirectory);
			
			String projectPath = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/images/";
			// ls & download all item file
			for(String name:ftpClient.listNames()){
				System.out.println(workingDirectory + _SYB + name);
				rtnList.add("images" + _SYB + name);
				ftp.downloadFile(projectPath + _SYB + name, workingDirectory + _SYB + name);
			}
			ftp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnList;
	}
	
	public int ftpFileTest(String brand_code_path,String item_category_path,String item_brand_path,String item_code_path){
		int fileCount = 0;
		String projectPath = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/images";
		try{
			FtpUtils ftp = new FtpUtils("kwedev","Password001","10.0.96.86",21,"/");
			ftp.login();
			
			FTPClient ftpClient = ftp.getFClient();
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("1workDir:"+workingDirectory+",");
			for(String name:ftpClient.listNames()){
				System.out.println("dor::"+workingDirectory + _SYB + name);
			}
			
			// into brand_code
			//String brand_code_path = "T2";
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + brand_code_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("2workDir:"+workingDirectory);
			for(String name:ftpClient.listNames()){
				System.out.println("dor::"+workingDirectory + _SYB + name);
			}
			
			// into item_category
			//String item_category_path = "1";
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_category_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("3workDir:"+workingDirectory);
			for(String name:ftpClient.listNames()){
				System.out.println("dor::"+workingDirectory + _SYB + name);
			}
			
			// into item_brand
			//String item_brand_path = "AE";
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_brand_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("4workDir:"+workingDirectory);
			
			for(String name:ftpClient.listNames()){
				System.out.println(workingDirectory + _SYB + name);
			}
			
			// into item_code
			//String item_code_path = "005829980719F";
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_code_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("5workDir:"+workingDirectory);
			for(String name:ftpClient.listNames()){
				System.out.println("dor::"+workingDirectory + _SYB + name);
			}
			
			//String item_code_path = "005829980719F";
			ftpClient.changeWorkingDirectory(workingDirectory + _SYB + item_code_path);
			workingDirectory = ftpClient.printWorkingDirectory();
			System.out.println("6workDir:"+workingDirectory);
			for(String name:ftpClient.listNames()){
				System.out.println("dor::"+workingDirectory + _SYB + name);
			}
			
			// ls & download all item file
			for(String name:ftpClient.listNames()){
				System.out.println(workingDirectory + _SYB + name);
				fileCount++;
				ftp.downloadFile(projectPath + _SYB + name, workingDirectory + _SYB + name);
			}
			
			ftp.logout();
		}catch(Exception e){
			e.printStackTrace();
		}
		return fileCount;
	}
	
}
