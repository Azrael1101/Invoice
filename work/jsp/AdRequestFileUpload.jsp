<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="java.util.*"%>

<%
String errMsg =request.getParameter("errorMsg");
System.out.println(errMsg);
String orderType = request.getParameter("orderType");
String headId = request.getParameter("headId");
String loginEmployeeCode = request.getParameter("loginEmployeeCode");
String ownerType = request.getParameter("ownerType");

GnFileDAO gnFileDAO = (GnFileDAO) SpringUtils.getApplicationContext().getBean("gnFileDAO");

String typeValue = request.getParameter("typeValue");

String types = gnFileDAO.findByPropertyToHtmlString("GN_TYPE","other",typeValue); 

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>檔案上傳</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="../css/erp.css">
	

  </head>
  
  <body>
  <%
  if(null!=orderType && null!=headId && null!=loginEmployeeCode){
  	if(null != errMsg && errMsg.length()>0){
  	%>
  	<h4><font color="red"><%=errMsg %></font></h4>
  	<%}
  %>
  
  <form name="uploadForm" enctype="multipart/form-data" method="post" action="FileUploader.jsp?loginEmployeeCode=<%=loginEmployeeCode %>&orderType=<%=orderType %>&headId=<%=headId %>&ownerType=<%=ownerType %>">
		<table class="default">
			<tr><th colspan="2">插入附件</th></tr>
			<tr>
				<td>檔案位置<font color="red">*</font></td>
				<td><input type="file" name="filePath"/></td>
			</tr>
			<tr>
				<td>說明<font color="red">*</font></td>
				<td><input type="text" name="description" maxlength=400/>
				</td>
			</tr>
			<tr>
				<td>類型<font color="red">*</font></td>
				<td><select name="applyType">
				<%=types %>
				 </select>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="上傳" /></td>
			</tr>
		</table>
		<input type="hidden" name="loginEmployeeCode" value="<%=loginEmployeeCode %>" />
		<input type="hidden" name="orderType" value="<%=orderType %>" />
		<input type="hidden" name="headId" value="<%=headId %>" />
		<input type="hidden" name="ownerType" value="<%=ownerType %>" />
	</form>
<%}else{ %>
	<h2 style=color:red>請確認參數是否傳遞錯誤</h2>
<%} %>
  </body>
</html>
