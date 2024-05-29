<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%
	String springConfigFile = request.getParameter("springConfigFile");
	if( "tServ".equalsIgnoreCase(springConfigFile) ){
		springConfigFile = "spring-config-test.xml" ;
	}else if( "lServ".equalsIgnoreCase(springConfigFile) ){
		springConfigFile = "spring-config-local.xml" ;
	}else{
		springConfigFile = "spring-config.xml" ;
	}
	SpringUtils.setSpringConfigFile(springConfigFile);
	BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
	buCommonPhraseService.clearBuCommonPhraseData() ;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>clean cache</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    ReSet Spring Config File <%=springConfigFile%> Finish ... <br>
  </body>
</html>
