<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" -->
<%@ page contentType="text/html; charset=UTF-8" %>


<html>
  <head>
    <title>Login Page</title>
    <link href="css/default.css" rel="stylesheet" type="text/css">
  </head>

  <body>
  <%=session.getAttribute("user_name")%>
  <br>
  <%=session.getAttribute("user_domain")%>
  <br>
  <%=session.getAttribute("user_brand")%>
  </body>
</html>
 