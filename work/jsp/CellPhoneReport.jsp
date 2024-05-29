<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="java.util.*,java.io.*,java.lang.*,tw.com.tm.erp.utils.*,javax.servlet.ServletOutputStream,tw.com.tm.erp.hbm.bean.*,tw.com.tm.erp.hbm.dao.*,org.springframework.util.StringUtils,tw.com.tm.erp.hbm.service.*,tw.com.tm.erp.hbm.SpringUtils,org.apache.commons.httpclient.*,org.apache.commons.httpclient.methods.*,org.apache.commons.httpclient.params.HttpMethodParams"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html" />
	</head>
	
	<BODY>
		<%
			response.sendRedirect("http://10.1.94.161:8080/crystal/MobileReport/m_so01/m_so01_20081118.html");
			/*
		    SiMenuService siMenuService = (SiMenuService)SpringUtils.getApplicationContext().getBean("siMenuService");
			//siMenuService.getBrandUserReportManager(brandCode,loginName,"R","R",functionCode);
			
			String url = "http://10.1.94.161:8080/crystal/MobileReport/m_so01/m_so01_20081118.html" ;
			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();

			// Create a method instance.
			GetMethod method = new GetMethod(url);

			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

			try {
				// Execute the method.
				int statusCode = client.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + method.getStatusLine());
				}

				// Read the response body.
				byte[] responseBody = method.getResponseBody();

				// Deal with the response.
				// Use caution: ensure correct character encoding and is not binary data
				System.out.println(new String(responseBody));
				
				String rep = new String(responseBody,"utf-8");
				out.println(rep);

			} catch (HttpException e) {
				System.err.println("Fatal protocol violation: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Fatal transport error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				// Release the connection.
				method.releaseConnection();
			}
			*/
		%>
	</body>
</html>
