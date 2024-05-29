<%
/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA : 
 * PG : Weichun.Liao
 * Filename : ImInventoryCountCompare.jsp
 * Function : 
 * 
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Jun 9, 2010		Weichun.Liao	Create
 *--------------------------------------------------------------------------------------- 
 */
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" tw.com.tm.erp.utils.sp.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="java.util.*"%>
<%
try{
	// 取得庫別資料
	ImInventoryCountService imInventoryCountService = (ImInventoryCountService)SpringUtils.getApplicationContext().getBean("imInventoryCountService");
	imInventoryCountService.getImInventoryCountItemInfo(request);
	//out.println("<script>alert('下載成功');</script>");
	out.print("complete");
}catch(Exception ex){
	ex.printStackTrace();
	//out.println("<script>alert('" + ex + "');</script>");
	out.print("error:" + ex.toString());
} 
%>
