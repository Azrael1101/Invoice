<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="tw.com.tm.erp.utils.User" %>
<%@page import="tw.com.tm.erp.constants.SystemConfig" %>
<%@page import="tw.com.tm.erp.utils.DES" %>
<%
	DES des = new DES();
	User userObj = (User)session.getAttribute(SystemConfig.USER_SESSION_NAME);	
	String brandCode = userObj.getBrandCode();	
	String employeeCode = userObj.getEmployeeCode();
%>
<form name="lastPage" method="post" action="http://192.168.66.105/mis/index2.jsp">
<input type="hidden" name="au_account" value="<%=des.encrypt(employeeCode) %>">
<input type="hidden" name="brands" value="<%=des.encrypt(brandCode) %>">
<input type="hidden" name="ids" value="<%=des.encrypt(String.valueOf(new Date().getTime()))%>">
<input type="hidden" type="submit">
</form>
<script>
document.lastPage.submit();
</script>