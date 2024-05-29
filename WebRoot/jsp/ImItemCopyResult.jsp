<%
/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : ImItemCopyResult.jsp
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	April 26, 2011	Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="java.util.*"%>
<%
try{
	// 取得庫別資料
	ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
	String msg = imItemService.updateT6COItem();
	//out.println("<script>alert('下載成功');</script>");
	if("".equals(msg))
		out.print("complete");
}catch(Exception ex){
	ex.printStackTrace();
	//out.println("<script>alert('" + ex + "');</script>");
	out.print("error:" + ex.toString());
}
%>
