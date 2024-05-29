<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.SiMenu"%>
<%@page import="java.util.*"%>
<%
	SiMenuDAO siMenuDAO = (SiMenuDAO) SpringUtils.getApplicationContext()
			.getBean("siMenuDAO");
	List<SiMenu> siMenus = siMenuDAO.findByProperty("SiMenu", "parentMenuId", 0L);	
	StringBuilder sb = new StringBuilder("[ ");
	for(int i = 0; i < siMenus.size(); i++ ){
		SiMenu siMenu = siMenus.get(i);
		sb.append( " [ '" + siMenu.getName() + "' ], " );
	}
	int sbLen = sb.length();
	sb.delete( sbLen - 2, sbLen );
	sb.append( " ]");
//	String comboxBoxStr = "[ ['1','1'],['2','2'],['3','3'] ]";
	
//	System.out.println( "comboBox.jsp = " + sb.toString());
	out.print(sb.toString());
%>