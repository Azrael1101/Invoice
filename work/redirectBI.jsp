<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" import="tw.com.tm.erp.hbm.bean.BuCommonPhraseHead,tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO,tw.com.tm.erp.utils.DES,tw.com.tm.erp.utils.User,org.springframework.util.StringUtils,tw.com.tm.erp.utils.DES,java.net.InetAddress,java.util.Date,tw.com.tm.erp.utils.User,org.springframework.util.StringUtils,tw.com.tm.erp.hbm.dao.SiMenuLogDAO,tw.com.tm.erp.hbm.bean.SiMenuLog" import="tw.com.tm.erp.hbm.SpringUtils" import="org.springframework.context.ApplicationContext" pageEncoding="utf-8"%>
<html><head> 
<title>Hyperion</title> 
</head> 

<%! 
 //Steve Menu_Log Start 13/2/27
 private ApplicationContext context = SpringUtils.getApplicationContext(); 
 BuCommonPhraseHeadDAO buCommonPhraseHeadDao = (BuCommonPhraseHeadDAO) context.getBean("buCommonPhraseHeadDAO");
 BuCommonPhraseHead bph = buCommonPhraseHeadDao.findById("MenuLogSwitch");
 //Steve Menu_Log End
%>

<%
if(session.getAttribute("userObj")!=null){

String url = request.getParameter("url");
String reportName = request.getParameter("reportName");
System.out.println("url = " + url);

//Steve Menu_Log Start 13/2/27
String menuId = request.getParameter("menuId");
User userObj=(User)session.getAttribute("userObj");
//String serverIP = request.getRemoteAddr();
InetAddress inetAddress = InetAddress.getLocalHost();
String serverIP = inetAddress.getHostAddress();
String employeeCode = userObj.getEmployeeCode();

if("Y".equals(bph.getEnable())){

if(null!=menuId){
SiMenuLogDAO simenulogDAO = (SiMenuLogDAO) context.getBean("siMenuLogDAO");
SiMenuLog simenulog = new  SiMenuLog();
simenulog.setEmployeeCode(employeeCode);
simenulog.setActionMode("Click");
simenulog.setMenuId(menuId);
simenulog.setOpenDate(new Date());
simenulog.setApServerIp(serverIP);
simenulogDAO.save(simenulog); 
}
}
//Steve Menu_Log End

if(url.indexOf("R9") < 0){
	String src=(String)session.getAttribute("user_brand")+"@@"+(String)session.getAttribute("user_name")+"@@"+userObj.getReportLoginName()+"@@"+new String (org.apache.xerces.impl.dv.util.Base64.decode(userObj.getReportPassword()))+"@@"+(java.lang.String.valueOf(new java.util.Date().getTime()));
	DES des = new DES(); 
	String encryText=des.encrypt(src);
	response.sendRedirect("http://10.1.94.153:19000/workspace/default.jsp?reportName="+reportName+"&url="+url+"&crypto="+encryText);
}else{
	String src=(String)session.getAttribute("user_brand")+"@@"+(String)session.getAttribute("user_name")+"@@"+userObj.getReportLoginName()+"@@"+new String ("KWE")+"@@"+(java.lang.String.valueOf(new java.util.Date().getTime()));
	DES des = new DES(); 
	String encryText=des.encrypt(src);
	url = "index.jsp?";
	response.sendRedirect("http://10.1.94.153:19000/workspace/default.jsp?reportName="+reportName+"&url="+url+"&crypto="+encryText);
}
//String REQUEST_TYPE = request.getParameter("REQUEST_TYPE");
//String DOC_UUID = request.getParameter("DOC_UUID");
	//if("T2".equals((String)session.getAttribute("user_brand"))){
		//System.out.println("url = " + url);
		//System.out.println("reportName = " + reportName);
		//response.sendRedirect("http://127.0.0.1:9090/erp/tm.jsp?url="+url+"&crypto="+encryText);
		
		//response.setHeader("Refresh", "0 ; URL=http://10.1.99.112:19000/workspace/default.jsp?url="+url+"&crypto="+encryText);
	//}else{
		//response.sendRedirect("http://10.1.99.152:19000/workspace/default.jsp?reportName="+reportName+"&url="+url+"&crypto="+encryText+"&REQUEST_TYPE="+REQUEST_TYPE+"&DOC_UUID="+DOC_UUID);
	//}
}
else{
%>
<script language="JavaScript">
<!--
opener.document.location.reload();
self.close(); 
// -->
</script>
<%
}
%>




