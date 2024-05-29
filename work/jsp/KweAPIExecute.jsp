<%
/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : KweAPIExecute.jsp.jsp
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Sep 28, 2010	Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.action.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%
		PosDUAction posDUAction = (PosDUAction) SpringUtils.getApplicationContext().getBean("posDUAction");
		if ( null != request.getParameter("actionInfo") && !"".equals(request.getParameter("actionInfo")) && 
			 null != request.getParameter("company")  && !"".equals(request.getParameter("company")) ) {
		    try{
		    	String returnValue = "";
		    	returnValue = posDUAction.executePosDU(request);
		    	System.out.println("requestId = " + request.getParameter("requestId") + " returnValue = " + returnValue);
				out.print(returnValue);
		    }catch(Exception e){
				System.out.println("error on requestId = " + request.getParameter("requestId"));
				e.printStackTrace();
				out.print(-1);
		    }
		} else{
			out.print(-1);
		}
%>
