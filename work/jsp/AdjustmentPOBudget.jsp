<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="java.util.*"%>
<%
	PoPurchaseOrderHeadService poPurchaseOrderHeadService = (PoPurchaseOrderHeadService) SpringUtils.getApplicationContext()
			.getBean("poPurchaseOrderHeadService");
	List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadService.updateAllPOBudget();	
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
		調整所有進貨單預算資料完成
		<%
			StringBuffer spOut = new StringBuffer();
			for( PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads ){
				spOut.append(poPurchaseOrderHead.getBrandCode());
				spOut.append(".");
				spOut.append(poPurchaseOrderHead.getOrderTypeCode());
				spOut.append(".");
				spOut.append(poPurchaseOrderHead.getOrderNo());
				spOut.append("=");
				spOut.append(poPurchaseOrderHead.getTotalUnitPriceAmount());
				spOut.append("<br>");
			}
		%>
	</body>
</html>
