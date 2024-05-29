<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" import="tw.com.tm.erp.hbm.bean.BuCommonPhraseHead,tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO,tw.com.tm.erp.utils.DES,java.net.InetAddress,java.util.Date,tw.com.tm.erp.utils.User,tw.com.tm.erp.hbm.dao.SiMenuLogDAO,tw.com.tm.erp.hbm.bean.SiMenuLog,org.springframework.context.ApplicationContext,tw.com.tm.erp.hbm.SpringUtils"   pageEncoding="utf-8" %>
<html><head> 
<title>Hyperion</title> 
</head> 

<%! 
//Steve Menu_Log Start 13/2/27
private ApplicationContext context = SpringUtils.getApplicationContext(); 
//Steve Menu_Log End
%>

<%
//ex: redirectCC.jsp?url=http://10.1.99.161:8080/crystal/t2/PO0633.rpt&rpt_para=prompt1=1@@prompt=2

String Para=(String)request.getParameter("rpt_para");
String src=session.getAttribute("user_brand")+"@@"+tw.com.tm.erp.utils.UserUtils.getEmployeeCodeByLoginName((String)session.getAttribute("user_name"))+"@@"+(String)request.getParameter("rpt_name")+"@@"+(java.lang.String.valueOf(new java.util.Date().getTime()));
DES des = new DES(); 
String encryText=des.encrypt(src);
String url=(String)request.getParameter("url");

//Steve Menu_Log Start 13/2/27
BuCommonPhraseHeadDAO buCommonPhraseHeadDao = (BuCommonPhraseHeadDAO) context.getBean("buCommonPhraseHeadDAO");
BuCommonPhraseHead bph = buCommonPhraseHeadDao.findById("MenuLogSwitch");
String menuId = request.getParameter("menuId");
//String serverIP = request.getRemoteAddr();
InetAddress inetAddress = InetAddress.getLocalHost();
String serverIP = inetAddress.getHostAddress();
User userObj=(User)session.getAttribute("userObj");

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

if(Para!=null){
	if(Para.length()>0){
		String addPara="";
		for(int i=0;i<Para.split("@@").length;i++){
			addPara=addPara+"&"+Para.split("@@")[i];
		}
		
		//response.sendRedirect("http://10.1.99.161:8080/crystal/"+(String)request.getParameter("rpt_name")+".rpt?crypto="+encryText+addPara);
		response.sendRedirect(url+"?crypto="+encryText+addPara);
	}
}else{
	//response.sendRedirect("http://10.1.99.161:8080/crystal/"+(String)request.getParameter("rpt_name")+".rpt?crypto="+encryText);
	response.sendRedirect(url+"?crypto="+encryText);
   
}
%>




