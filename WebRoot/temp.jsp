<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="java.net.URLDecoder"%>

<%
    Cookie[] cookies = request.getCookies();
    if(cookies != null && cookies.length > 0){
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];     
            if(cookie.getName().equalsIgnoreCase("requestUrl")){
                String requestUrl = URLDecoder.decode(cookie.getValue(), "UTF-8");
                if(StringUtils.hasText(requestUrl)){
                    int keyIdx = requestUrl.indexOf("redirect=Y");
	                if(keyIdx != -1 ){
	                    session.setAttribute("redirectUrl", requestUrl);
	                    cookie.setMaxAge(0);
		            }
	            }
	            break;
            }
        }
    }
     
	String user_domain=((String)request.getParameter("user_domain")).split("@")[1];
    session.setAttribute("user_domain",user_domain);
	session.setAttribute("user_name",request.getParameter("user_name") + "@" + user_domain);
	session.setAttribute("user_brand",request.getParameter("user_brand"));
	session.setAttribute("employee_code",request.getParameter("user_name"));
	response.sendRedirect("index.jsp");
%>


