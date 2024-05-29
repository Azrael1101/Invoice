<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.ImItemOnHandView" %>
<%@page import="tw.com.tm.erp.hbm.bean.ImItem" %>
<!DOCTYPE html>
<html>
  <head>
<%
//String itemCode;
String brandCode;
String categoryType;
String loginUser;
String functionCode;
String itemCode;
ImItem item = null;
List<ImItemOnHandView> returnOnhandResult = new ArrayList(0);
List<Map> resultLists = null;
try{
	ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
	ImMovementMainService imMovementMainService = (ImMovementMainService)SpringUtils.getApplicationContext().getBean("imMovementMainService");
	
	loginUser = request.getParameter("login");
	itemCode = request.getParameter("itemCode");
	functionCode = request.getParameter("functionCode");
	categoryType = request.getParameter("categoryType");
	imItemService.savelog(loginUser,itemCode,functionCode,categoryType);
}catch(Exception ex){
	%>
	<script language=JavaScript>
      let url = "http://60.250.137.187:58083/erp_dev_20210702/errorpage.jsp";
      window.location.href = url;
    </script>
	<%
}
%>
  </head>
  <body class="w3-light-grey">
    
  </body>
</html>



