<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.*"%>
<%@page import="javax.servlet.ServletOutputStream"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.constants.*"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.hssf.util.Region"%>
<%@page import="org.apache.commons.httpclient.*"%>
<%@page import="org.apache.commons.httpclient.methods.*"%>
<%@page import="org.apache.commons.httpclient.params.HttpMethodParams"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%
	String responseTagStart = "<";
	String responseTagEnd = ">";

	StringBuffer returnData = new StringBuffer();
	String processSqlCode = request.getParameter("process_sql_code");
	String processObjectName = request.getParameter("process_object_name");
	String processObjectMethodName = request.getParameter("process_object_method_name");
	String processSqlCodeFields[] = null;
	if (null != processSqlCode) {

		//取得PROPERTIES 組出SQL
		String QueryNativeSqlProFile = "/query_native_sql.properties";
		Properties queryNativeSqlPro = new Properties();
		queryNativeSqlPro.load(NativeQueryDAO.class.getResourceAsStream(QueryNativeSqlProFile));
		String processSql = queryNativeSqlPro.getProperty(processSqlCode);
		String processSqlField = queryNativeSqlPro.getProperty(processSqlCode + "Fields");
		//System.out.println("processSqlField =" + processSqlField);
		processSqlCodeFields = StringTools.split(processSqlField, ",");
		//System.out.println("processSqlField len =" + processSqlCodeFields.length);
		Enumeration paramNames = request.getParameterNames();
		StringBuffer replaceParam = null;
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String values[] = request.getParameterValues(paramName);
			if (values.length > 0) {
				System.out.println(paramName + "=" + values[0]);
				replaceParam = new StringBuffer();
				replaceParam.append("{#");
				replaceParam.append(paramName);
				replaceParam.append("}");
				String value = "'" + values[0] + "'";
				processSql = StringUtils.replace(processSql, replaceParam.toString(), value);
			}
		}
		
		//執行SQL
		NativeQueryDAO nativeQueryDAO = (NativeQueryDAO) SpringUtils.getApplicationContext().getBean("nativeQueryDAO");
		List results = nativeQueryDAO.executeNativeSql(processSql);
		if (results.size() > 0) {
			//建立回傳資料
			Object record = results.get(0);
			Object records[] = null;
			try {
				records = (Object[]) results.get(0);
			} catch (Exception ex) {
			}
			if (null != records && records.length > 0) {
				for (int index = 0; index < records.length; index++) {
					returnData.append(responseTagStart);
					returnData.append(processSqlCodeFields[index].trim());
					returnData.append("=");
					returnData.append(records[index]);
					returnData.append(responseTagEnd);
				}
			} else if (null != record) {
				returnData.append(responseTagStart);
				returnData.append(processSqlCodeFields[0].trim());
				returnData.append("=");
				returnData.append(record);
				returnData.append(responseTagEnd);
			} else {
				returnData.append("資料內容為空..");
			}
		} else {
			returnData.append("查無資料..");
		}
		
	}else if(null != processObjectName ){	
		//RUN 物件
		Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
		Object processObjReturn = MethodUtils.invokeExactMethod(processObj,processObjectMethodName,request.getParameterMap()); //回傳回來一定是List<Properties>
		if( null != processObjReturn ){
			Properties processObjReturnPro = (Properties) processObjReturn ;
			Enumeration keys = processObjReturnPro.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				String value = processObjReturnPro.getProperty(key);				
				returnData.append(responseTagStart);
				returnData.append(key);
				returnData.append("=");
				returnData.append(value);
				returnData.append(responseTagEnd);				
			}
		}
				
	} else {
		returnData.append("請確認 process_sql_code 參數是否存在..");
	}
	out.println(returnData.toString());
%>