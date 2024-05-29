package tw.com.tm.erp.servlet;
import java.util.List;
import java.util.Properties;
import tw.com.tm.erp.utils.DES;
import tw.com.tm.erp.hbm.SpringUtils; 
import tw.com.tm.erp.hbm.service.SiMenuService; 
import com.inet.report.*;
public class MyReportServlet extends ReportServlet{
	private static final long serialVersionUID = -2882525223939889L;
	public void checkProperties(Properties reportProperties, Object servletRequest) throws ReportException {
		try{
			String encryText=null;
			String decryText=null;
			boolean isSupervisor=false;
			if(reportProperties.get("user")!=null){
				if(((String)reportProperties.get("user")).equals("supervisor")){
					isSupervisor=true;
				}
			}
			if(isSupervisor){
				reportProperties.remove("user");
			}else{
				if((reportProperties.get("crypto")==null)||((String)reportProperties.get("crypto")).equals("")){
					throw new com.inet.report.ReportException("請先登入BPM系統!",0);
				}        
				encryText = ((String)reportProperties.get("crypto")).toLowerCase();
				// System.out.println("encryText="+encryText);
				DES des = new DES();
				decryText = des.decrypt(encryText);
				System.out.println("decryText="+decryText);       
				String brandCode=decryText.split("@@")[0];
				String loginName=decryText.split("@@")[1];
				String functionCode=decryText.split("@@")[2];
				/*** 
				 * if(new java.util.Date().after(new java.util.Date((new java.lang.Long(decryText.split("@@")[3]).longValue()+100000)))){ //100秒內
				 * 	throw new com.inet.report.ReportException("已超過系統授權時間!",0);}
				 **/
				System.out.println("brandCode="+brandCode+";loginName="+loginName+";functionCode="+functionCode);
				SiMenuService siMenuService = (SiMenuService) SpringUtils.getApplicationContext().getBean("siMenuService");    
				List rightResult = siMenuService.getBrandUserReportManager(brandCode, loginName, "R", "R", functionCode);
				System.out.println("rightResult="+rightResult+";rightResult.size()="+rightResult.size());
				if(!(rightResult.size()>0)){
					System.out.println("登入者身分不符合授權原則");
					throw new com.inet.report.ReportException("登入者身分不符合授權原則!",0);
				}
			}
			super.checkProperties(reportProperties, servletRequest);
		}catch(Exception e){
			throw new com.inet.report.ReportException(e.toString(),0);
		}
	}
}
