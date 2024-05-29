<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.ImItemOnHandView" %>
<%@page import="tw.com.tm.erp.hbm.bean.ImItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="generator" content="HTML Tidy for HTML5 for Windows version 5.5.24" />
    <title>production_search</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" type="text/css" href="css/product.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<%

List<ImItemOnHandView> returnOnhandResult = new ArrayList(0);
List<Map> resultLists = null;
List<Map> promoteList = null;
ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
ImMovementMainService imMovementMainService = (ImMovementMainService)SpringUtils.getApplicationContext().getBean("imMovementMainService");

try{
	resultLists = imItemService.getInfomation("T2");
		
		if(null!=resultLists){
			System.out.println("resultLists:"+resultLists);
			for(Map itemMap:resultLists){
				 
			}
		}
		
}catch(Exception ex){
	ex.printStackTrace();
}
%>
</head>
    <body></body>
</html>


