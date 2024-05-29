<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <%@page import="java.util.Map"%>
<%@ page language="Java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 
 <%
 
//  	Map requestFromMap = request.getParameterMap();
//  	requestFromMap.entrySet()
//  	System.out.println();
 	String vChannelType = request.getParameter("vChannelType");
 	String vCategoryType = request.getParameter("vCategoryType");
 	String vJsName = request.getParameter("vJsName");
 	
 	if(vChannelType == null)
 		vChannelType = "";
 	
 	if(vCategoryType == null)
 		vCategoryType = "";
 	
 	
 	
 	if( vJsName == null || "".equals(vJsName)){
 		vJsName = "ommChannelSearch";
 	}
 %>
<html><head>
<meta content='no-cache' http-equiv='Pragma'></meta>
<meta content='no-cache' http-equiv='Cache-Control'></meta>
<script type='text/javascript' src='js/default.js' language='JavaScript'></script>
<script type='text/javascript' src='js/calendar.js' language='JavaScript'></script>
<script type='text/javascript' src='js/calendar-zh_TW.js' language='JavaScript'></script>
<script type='text/javascript' src='js/calendar-setup.js' language='JavaScript'></script>
<script type='text/javascript' src='js/selectbox.js' language='JavaScript'></script>
<link href='css/calendar-system.css' rel='stylesheet'>
<link href='css/default.css' rel='stylesheet'>
<link href='css/erp.css' rel='stylesheet'>
</head>
<body><!-- vat JS files -->

<script type="text/javascript" src="js\json2.js"></script>
<script type="text/javascript" src="js\vat.js"></script>
<script type="text/javascript" src="js\vat-message.js"></script>
<script type="text/javascript" src="js\vat-utils.js"></script>
<script type="text/javascript" src="js\vat-tab-m.js"></script>
<script type="text/javascript" src="js\vat-ajax.js"></script>
<script type="text/javascript" src="js\vat-form.js"></script>
<script type="text/javascript" src="js\vat-block.js"></script>
<!-- <script type="text/javascript" src="js\ommChannel.js"></script> -->

<script type="text/javascript" src="js\<%=vJsName%>.js"></script>
<!--  

-->
 
<div id="vatBeginDiv" style="position:absolute; top:10px; left:15px; height:900px; filter:progid:DXImageTransform.Microsoft.BasicImage(enable=false);">
<form accept-charset='UTF-8' id='main' method='POST' action='' enctype='application/x-www-form-urlencoded' onSubmit=''>
<!-- 
form  action ='/erp/Im_Item:create:20091106.page' 
<input name='#loginBrandCode' type='HIDDEN' value='T2'>
<input name='#loginEmployeeCode' type='HIDDEN' value='T96085'>
<input name='#formId' type='HIDDEN' value=''>
<input name='#assignmentId' type='HIDDEN' value=''>
<input name='#processId' type='HIDDEN' value=''>
<input name='#isOppositePicker' type='HIDDEN' value=''>
<input name='#userType' type='HIDDEN' value='ITEM'>
-->
<script type="text/javascript">
  var vChannelType = '<%=vChannelType%>';
  var vCategoryType = '<%=vCategoryType%>';
  kweImBlock(); 
</script>   
</form>
</div>
<script type="text/javascript"> 
<!--
 
-->
</script>
<script type="text/javascript"> 
<!-- 
 
-->  
</script> 
</body></html>
