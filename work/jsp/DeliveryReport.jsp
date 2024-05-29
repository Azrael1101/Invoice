<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.springframework.util.StringUtils"%>

<%
    String errorMsg = null;
    try {
		String reportUrl = request.getParameter("reportUrl");
		String reportFileName = request.getParameter("reportFileName");
		String crypto = request.getParameter("crypto");
		String prompt0 = request.getParameter("prompt0");
		String prompt1 = request.getParameter("prompt1");
		String prompt2 = request.getParameter("prompt2");
		String prompt3 = request.getParameter("prompt3");
		String prompt4 = request.getParameter("prompt4");
		String url = reportUrl + reportFileName + "?crypto=" + crypto + "&prompt0=" + prompt0 + "&prompt1=" + prompt1 + "&prompt2=" + prompt2 + "&prompt3=" + prompt3 + "&prompt4=" + prompt4;	
		response.sendRedirect(url);
    } catch (Exception ex) {
        errorMsg = ex.getMessage();
        //out.println(errorMsg);
    }
%>

<script language="javascript">
  <%
  if (StringUtils.hasText(errorMsg)){
  %>
      alert("<%=errorMsg%>");
      window.close();
  <%
  }
  %>
</script>
